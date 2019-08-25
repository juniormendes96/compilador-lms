package analisadores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import enums.ErroEnum;
import enums.TokenEnum;
import models.Erro;
import models.ResultadoAnalise;
import models.Token;

public class AnalisadorLexico {
	
	private static final String ASPAS_SIMPLES = "'";
	private static final String ASPAS_DUPLAS = "\"";
	
	private String algoritmo;
	private List<Token> tokens;
	private List<Erro> erros;

	public AnalisadorLexico(String algoritmo) {
		this.algoritmo = algoritmo;
		this.tokens = new ArrayList<>();
		this.erros = new ArrayList<>();
	}
	
	public ResultadoAnalise iniciarAnalise() {
		this.removerComentarios();
		this.adicionarEspacosNosTokens();
		this.adicionarTokensNaLista();
		this.handleSinaisDeAtribuicao();
		this.handleLiterais();
		this.identificarErros();
		return new ResultadoAnalise(this.tokens, this.erros);
	}
	
	private void removerComentarios() {
		this.algoritmo = this.algoritmo.replaceAll("\\(\\*.*?\\*\\)", "");
	}
	
	private void adicionarTokensNaLista() {
		Scanner scanner = new Scanner(this.algoritmo);
		int linha = 1;
		while (scanner.hasNextLine()) {
			String caracteres[] = scanner.nextLine().split("\\s+");
			for (int i = 0; i < caracteres.length; i++) {
				this.adicionarToken(caracteres[i], linha);
			}
			linha++;
		}
		scanner.close();
	}
	
	private void adicionarToken(String caractere, int linha) {
		Token token = null;
		if (this.isInteger(caractere)) {
			token = new Token(TokenEnum.INTEIRO.getCod(), caractere, TokenEnum.INTEIRO.getDescricao(), linha);
		} else {
			for (TokenEnum tokenEnum : TokenEnum.values()) {
				if (tokenEnum.getSimbolo().equalsIgnoreCase(caractere)) {
					token = new Token(tokenEnum.getCod(), caractere, tokenEnum.getDescricao(), linha);
					break;
				}
			}
		}
		if (Objects.isNull(token)) {
			token = new Token(TokenEnum.ID.getCod(), caractere, TokenEnum.ID.getDescricao(), linha);
		}
		this.tokens.add(token);
	}

	private void adicionarEspacosNosTokens() {
		this.algoritmo = this.algoritmo.toUpperCase();
		for (TokenEnum token : TokenEnum.values()) {
			if (token.getCod() != 0 && token.getCod() != TokenEnum.OR.getCod() && token.getDescricao() != "Palavra Reservada") {
				final String replacement = String.format(" %s ", token.getSimbolo());
				this.algoritmo = this.algoritmo.replace(token.getSimbolo(), replacement);
			}
		}
	}
	
	private void identificarErros() {
		for(Token token : this.tokens) {
			if(token.getCodigo() == TokenEnum.INTEIRO.getCod()) {
				if(Integer.parseInt(token.getToken()) >= 32767) {
			    	this.erros.add(new Erro(ErroEnum.INTEIRO_FORA_DO_LIMITE.getErro() + " (Token " + token.getToken() + " linha " + token.getLinha() + ")", token.getLinha()));
			    }
			}
			if (token.getCodigo() == TokenEnum.LIT.getCod()) {
				if(token.getToken().length() > 255) {
					this.erros.add(new Erro(ErroEnum.CARACTERES_FORA_DO_LIMITE.getErro() + " (Token " + token.getToken() + " linha " + token.getLinha() + ")", token.getLinha()));
				}
			}
			if (token.getCodigo() == TokenEnum.ID.getCod()) {
				if(token.getToken().length() > 30) {
					this.erros.add(new Erro(ErroEnum.IDENTIFICADOR_FORA_DO_LIMITE.getErro() + " (Token " + token.getToken() + " linha " + token.getLinha() + ")", token.getLinha()));
				}
				if(this.isVariavel(token) && this.isInteger(String.valueOf(token.getToken().charAt(0)))){
					this.erros.add(new Erro(ErroEnum.IDENTIFICADOR_DECLARADO_ERRADO.getErro() + " (Token " + token.getToken() + " linha " + token.getLinha() + ")", token.getLinha()));
				}
			}
		}
	}
	
	private boolean isVariavel(Token token) {
		int index = this.tokens.indexOf(token);
		Token tokenAnterior = index == 0 ? null : this.tokens.get(index - 1);
		return Objects.nonNull(tokenAnterior) && (tokenAnterior.getCodigo() == TokenEnum.VAR.getCod()
				|| tokenAnterior.getCodigo() == TokenEnum.CONST.getCod()
				|| tokenAnterior.getCodigo() == TokenEnum.PROGRAM.getCod()
				|| tokenAnterior.getCodigo() == TokenEnum.PROCEDURE.getCod()
				|| this.isVariavel(tokenAnterior));
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
	
	private void handleSinaisDeAtribuicao() {
		Iterator<Token> tokensIterator = this.tokens.iterator();
		Token previousToken = null;
		Token currentToken = null;
		while (tokensIterator.hasNext()) {
			currentToken = tokensIterator.next();
			if (Objects.nonNull(previousToken) && previousToken.getCodigo() == TokenEnum.DOIS_PONTOS.getCod() && currentToken.getCodigo() == TokenEnum.IGUAL.getCod()) {
				tokensIterator.remove();
				this.tokens.set(this.tokens.indexOf(previousToken),
						new Token(TokenEnum.ATRIBUICAO.getCod(), TokenEnum.ATRIBUICAO.getSimbolo(),
								TokenEnum.ATRIBUICAO.getDescricao(), previousToken.getLinha()));
			}
			previousToken = currentToken;
		}
	}
	
	private void handleLiterais() {
		Iterator<Token> tokensIterator = this.tokens.iterator();
		Token currentToken = null;
		int index = 0;
		boolean literal = false;
		String texto = "";
		while (tokensIterator.hasNext()) {
			currentToken = tokensIterator.next();
			if (literal) {
				texto += currentToken.getToken() + " ";
				if (this.hasFimLiteral(currentToken)) {
					literal = false;
					this.tokens.get(index).setCodigo(TokenEnum.LIT.getCod());
					this.tokens.get(index).setDescricao(TokenEnum.LIT.getDescricao());
					this.tokens.get(index).setToken(texto);
					texto = "";
				}
				tokensIterator.remove();
			} else if (this.hasInicioLiteral(currentToken)) {
				literal = true;
				index = this.tokens.indexOf(currentToken);
				if (currentToken.getToken().length() > 1 && this.hasFimLiteral(currentToken)) {
					currentToken.setCodigo(TokenEnum.LIT.getCod());
					currentToken.setDescricao(TokenEnum.LIT.getDescricao());
					literal = false;
					texto = "";
				} else {
					texto += currentToken.getToken() + " ";
				}
			}
		}
	}
	
	private boolean hasInicioLiteral(Token token) {
		return token.getToken().startsWith(ASPAS_DUPLAS) || token.getToken().startsWith(ASPAS_SIMPLES);
	}
	
	private boolean hasFimLiteral(Token token) {
		return token.getToken().endsWith(ASPAS_DUPLAS) || token.getToken().endsWith(ASPAS_SIMPLES);
	}

}
