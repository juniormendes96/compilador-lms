package analisadores;

import java.util.ArrayList;
import java.util.List;

import enums.ErroEnum;
import enums.TokenEnum;
import models.Erro;
import models.ResultadoAnalise;
import models.Token;

public class AnalisadorLexico {
	
	private String algoritmo;
	private List<Token> tokens;
	private List<Erro> erros;
	private String[] linhas;
	private String[] caracteres;

	public AnalisadorLexico(String algoritmo) {
		this.algoritmo = algoritmo;
		this.tokens = new ArrayList<>();
		this.erros = new ArrayList<>();
	}
	
	public ResultadoAnalise iniciarAnalise() {
		this.formatarAlgoritmo();
		this.setCaracteresELinhas();
		this.gerarListaDeTokens();
		this.tratarListaDeTokens();
		this.identificarErros();
		return new ResultadoAnalise(this.tokens, this.erros);
	}
	
	private void setCaracteresELinhas() {
		this.caracteres = this.algoritmo.split("\\s+");
		this.linhas = this.algoritmo.split("\r?\n");
	}
	
	private void formatarAlgoritmo() {
		this.algoritmo = this.algoritmo.toUpperCase();
		for (TokenEnum token : TokenEnum.values()) {
			if (token.getCod() != 0 && token.getCod() != TokenEnum.OR.getCod() && token.getDescricao() != "Palavra Reservada") {
				this.algoritmo = this.algoritmo.replace(token.getSimbolo(), String.format(" %s ", token.getSimbolo()));
			}
		}
	}
	
	private void identificarErros() {
		int numeroDaLinha = 0;
		for(Token token : tokens) {
			numeroDaLinha = this.encontrarLinhaDoToken(token.getToken());
			if(token.getCodigo() == TokenEnum.INTEIRO.getCod()) {
				if(Integer.parseInt(token.getToken()) >= 32767) {
			    	this.erros.add(new Erro(ErroEnum.INTEIRO_FORA_DO_LIMITE.getErro() + " (Token linha da tabela: "+ numeroDaLinha+")", numeroDaLinha));
			    }
			} else if (token.getCodigo() == TokenEnum.LIT.getCod()) {
				if(token.getToken().length() > 255) {
					this.erros.add(new Erro(ErroEnum.CARACTERES_FORA_DO_LIMITE.getErro() + " (Token linha da tabela: "+ numeroDaLinha+")", numeroDaLinha));
				}
			} else if (token.getCodigo() == TokenEnum.ID.getCod()) {
				if(token.getToken().length() > 30) {
					this.erros.add(new Erro(ErroEnum.IDENTIFICADOR_FORA_DO_LIMITE.getErro() + " (Token linha da tabela: "+ numeroDaLinha+")", numeroDaLinha));
				}
				if(isInteger(String.valueOf(token.getToken().charAt(0)))){
					this.erros.add(new Erro(ErroEnum.IDENTIFICADOR_DECLARADO_ERRADO.getErro() + " (Token linha da tabela: "+ numeroDaLinha+")", numeroDaLinha));
				}
			}
		}
	}
	
	private int encontrarLinhaDoToken(String token) {
		int linha = 0;
		for (String c : linhas) {
			linha++;
			if (c.contains(token)) {
				linhas[linha - 1] = c.substring(c.indexOf("") + token.length());
				return linha;
			}
		}
		return 0;
	}
	
	private void gerarListaDeTokens() {
		boolean comentario = false;
		boolean literal = false;
		String textoLiteral = "";
		int numeroDaLinha = 0;

		for (int i = 0; i < this.caracteres.length; i++) {
			numeroDaLinha = this.encontrarLinhaDoToken(this.caracteres[i]);
			if(this.hasInicioLiteral(this.caracteres[i])) {
				if(this.caracteres[i].length() == 1) {
					textoLiteral += String.format(" %s", this.caracteres[i]);
					literal = !literal;
					if(!literal) {
						this.tokens.add(new Token(TokenEnum.LIT.getCod(), textoLiteral, TokenEnum.LIT.getDescricao(), numeroDaLinha));
						textoLiteral = "";
					}	
				} else {
					textoLiteral += this.caracteres[i];
					if (this.hasFimLiteral(this.caracteres[i])) {
						this.tokens.add(new Token(TokenEnum.LIT.getCod(), textoLiteral, TokenEnum.LIT.getDescricao(), numeroDaLinha));
						textoLiteral = "";
						literal = false;
					}
					literal = true;
				}
				continue;
			}
			
			if((isInicioComentario(i))) {
				comentario = true;
				continue;
			}
			
			if((i > 0 && isFimComentario(i))) {
				comentario = false;
				continue;
			}

			if (literal) {
				textoLiteral += String.format(" %s", this.caracteres[i]);
				if (this.hasFimLiteral(this.caracteres[i])) {
					this.tokens.add(new Token(TokenEnum.LIT.getCod(), textoLiteral, TokenEnum.LIT.getDescricao(), numeroDaLinha));
					textoLiteral = "";
					literal = false;
					continue;
				}
				continue;
			}

			if(isInteger(this.caracteres[i]) && !comentario) {
				this.tokens.add(new Token(TokenEnum.INTEIRO.getCod(), this.caracteres[i], TokenEnum.INTEIRO.getDescricao(), numeroDaLinha));
				continue;
			}
			
			if (!comentario) {
				boolean tokenEncontrado = false;
				for (TokenEnum token : TokenEnum.values()) {
					if (token.getSimbolo().equals(this.caracteres[i])) {
						this.tokens.add(new Token(token.getCod(), token.getSimbolo(), token.getDescricao(), numeroDaLinha));
						tokenEncontrado = !tokenEncontrado;
					}
				}
				if(!tokenEncontrado) {
					this.tokens.add(new Token(TokenEnum.ID.getCod(), this.caracteres[i], TokenEnum.ID.getDescricao(), numeroDaLinha));
					tokenEncontrado = !tokenEncontrado;
				}				
			}
		}
	}
	
	private boolean isInicioComentario(int indice) {
		if ((this.caracteres[indice].equals("(") && this.caracteres[indice + 1].equals("*"))) {
			return true;
		}
		return false;
	}
	
	private boolean isFimComentario(int indice) {
		if((this.caracteres[indice - 1].equals("*") && this.caracteres[indice].equals(")"))) {
			return true;
		}
		return false;
	}
	
	private boolean hasInicioLiteral(String s) {
		if (s.startsWith("'") || s.startsWith("\"")) {
			return true;
		}
		return false;
	}
	
	private boolean hasFimLiteral(String s) {
		if (s.endsWith("'") || s.endsWith("\"")) {
			return true;
		}
		return false;
	}
	
	private boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	private void tratarListaDeTokens() {
		int numeroDaLinha = 0;
		for (int i = 0; i < this.tokens.size() - 1; i++) {
			if (this.tokens.get(i).getToken().equals(":") && this.tokens.get(i + 1).getToken().equals("=")) {
				numeroDaLinha = encontrarLinhaDoToken(":=");
				this.tokens.set(i, new Token(TokenEnum.ATRIBUICAO.getCod(), TokenEnum.ATRIBUICAO.getSimbolo(),
						TokenEnum.ATRIBUICAO.getDescricao(), numeroDaLinha));
				this.tokens.remove(i + 1);
			}
			if (this.tokens.get(i).getToken().equals(">") && this.tokens.get(i + 1).getToken().equals("=")) {
				numeroDaLinha = encontrarLinhaDoToken(">=");
				this.tokens.set(i, new Token(TokenEnum.MAIOR_OU_IGUAL.getCod(), TokenEnum.MAIOR_OU_IGUAL.getSimbolo(),
						TokenEnum.MAIOR_OU_IGUAL.getDescricao(), numeroDaLinha));
				this.tokens.remove(i + 1);
			}
			if (this.tokens.get(i).getToken().equals("<") && this.tokens.get(i + 1).getToken().equals("=")) {
				numeroDaLinha = encontrarLinhaDoToken("<=");
				this.tokens.set(i, new Token(TokenEnum.MENOR_OU_IGUAL.getCod(), TokenEnum.MENOR_OU_IGUAL.getSimbolo(),
						TokenEnum.MENOR_OU_IGUAL.getDescricao(), numeroDaLinha));
				this.tokens.remove(i + 1);
			}
			if (this.tokens.get(i).getToken().equals("<") && this.tokens.get(i + 1).getToken().equals(">")) {
				numeroDaLinha = encontrarLinhaDoToken("<>");
				this.tokens.set(i, new Token(TokenEnum.DIFERENTE.getCod(), TokenEnum.DIFERENTE.getSimbolo(),
						TokenEnum.DIFERENTE.getDescricao(), numeroDaLinha));
				this.tokens.remove(i + 1);
			}
		}
	}

}
