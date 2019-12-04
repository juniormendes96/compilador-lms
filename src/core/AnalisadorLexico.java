package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import enums.ErroEnum;
import enums.TokenEnum;
import exceptions.AnalisadorLexicoException;
import models.Token;

public class AnalisadorLexico {
	
	private static final String ASPAS_SIMPLES = "'";
	private static final String ASPAS_DUPLAS = "\"";
	private static final String VAZIO = "";
	private static final String ESPACO = " ";
	
	private String algoritmo;
	private List<Token> tokens;

	public AnalisadorLexico(String algoritmo) {
		this.algoritmo = algoritmo;
		this.tokens = new ArrayList<>();
	}
	
	public List<Token> iniciarAnalise() {
		this.removerComentarios();
		this.adicionarEspacosNosTokens();
		this.adicionarTokensNaLista();
		this.handleSinaisDeAtribuicaoEComparacao();
		this.handleLiterais();
		this.identificarErros();
		return this.tokens;
	}
	
	private void removerComentarios() {
		this.algoritmo = this.algoritmo.replaceAll("\\(\\*.*?\\*\\)", VAZIO);
	}
	
	private void adicionarTokensNaLista() {
		Scanner scanner = new Scanner(this.algoritmo);
		int linha = 1;
		while (scanner.hasNextLine()) {
			String caracteres[] = scanner.nextLine().split("\\s+");
			for (int i = 0; i < caracteres.length; i++) {
				if (!caracteres[i].equals(TokenEnum.RESERVA_CADEIA_VAZIA.getSimbolo())) this.adicionarToken(caracteres[i], linha);
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
		for (TokenEnum token : TokenEnum.values()) {
			if (token.getCod() != 0 && token.getCod() != 1 && token.getCod() != TokenEnum.ID.getCod() && token.getCod() != TokenEnum.OR.getCod() && token.getDescricao() != "Palavra Reservada") {
				final String replacement = String.format(" %s ", token.getSimbolo());
				if (Character.isLetter(token.getSimbolo().charAt(0))) {
					this.algoritmo = this.algoritmo.replaceAll(String.format("(?i)%s", token.getSimbolo()), replacement);
				} else {
					this.algoritmo = this.algoritmo.replace(token.getSimbolo(), replacement);
				}
			}
		}
	}
	
	private void identificarErros() {
		for(Token token : this.tokens) {
			if(token.getCodigo() == TokenEnum.INTEIRO.getCod()) {
				if(Integer.parseInt(token.getToken()) >= 32767) {
			    	throw new AnalisadorLexicoException(ErroEnum.INTEIRO_FORA_DO_LIMITE.getErro(), token);
			    }
			}
			if (token.getCodigo() == TokenEnum.LIT.getCod()) {
				if(token.getToken().length() > 255) {
					throw new AnalisadorLexicoException(ErroEnum.CARACTERES_FORA_DO_LIMITE.getErro(), token);
				}
			}
			if (token.getCodigo() == TokenEnum.ID.getCod()) {
				if(token.getToken().length() > 30) {
					throw new AnalisadorLexicoException(ErroEnum.IDENTIFICADOR_FORA_DO_LIMITE.getErro(), token);
				}
				if(this.isVariavel(token) && this.isInteger(String.valueOf(token.getToken().charAt(0)))){
					throw new AnalisadorLexicoException(ErroEnum.IDENTIFICADOR_DECLARADO_ERRADO.getErro(), token);
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
	
	private void handleSinaisDeAtribuicaoEComparacao() {
		Iterator<Token> tokensIterator = this.tokens.iterator();
		Token previousToken = null;
		Token currentToken = null;
		while (tokensIterator.hasNext()) {
			currentToken = tokensIterator.next();
			if (Objects.nonNull(previousToken)) {
				if (previousToken.getCodigo() == TokenEnum.DOIS_PONTOS.getCod() && currentToken.getCodigo() == TokenEnum.IGUAL.getCod()) {
					tokensIterator.remove();
					this.tokens.set(this.tokens.indexOf(previousToken), new Token(TokenEnum.ATRIBUICAO, previousToken.getLinha()));
				}
				if (previousToken.getCodigo() == TokenEnum.MENOR.getCod() && currentToken.getCodigo() == TokenEnum.IGUAL.getCod()) {
					tokensIterator.remove();
					this.tokens.set(this.tokens.indexOf(previousToken), new Token(TokenEnum.MENOR_OU_IGUAL, previousToken.getLinha()));
				}
				if (previousToken.getCodigo() == TokenEnum.MAIOR.getCod() && currentToken.getCodigo() == TokenEnum.IGUAL.getCod()) {
					tokensIterator.remove();
					this.tokens.set(this.tokens.indexOf(previousToken), new Token(TokenEnum.MAIOR_OU_IGUAL, previousToken.getLinha()));
				}
				if (previousToken.getCodigo() == TokenEnum.MENOR.getCod() && currentToken.getCodigo() == TokenEnum.MAIOR.getCod()) {
					tokensIterator.remove();
					this.tokens.set(this.tokens.indexOf(previousToken), new Token(TokenEnum.DIFERENTE, previousToken.getLinha()));
				}
			}
			previousToken = currentToken;
		}
	}
	
	private void handleLiterais() {
		final Iterator<Token> tokensIterator = this.tokens.iterator();
		Token currentToken = null;
		int index = 0;
		boolean isLiteral = false;
		String texto = VAZIO;
		while (tokensIterator.hasNext()) {
			currentToken = tokensIterator.next();
			if (isLiteral) {
				texto += currentToken.getToken() + ESPACO;
				if (this.hasFimLiteral(currentToken)) {
					isLiteral = false;
					Token token = this.tokens.get(index);
					token.setToken(texto);
					setAtributosTokenLiteral(token);
					texto = VAZIO;
				}
				tokensIterator.remove();
			} else if (this.hasInicioLiteral(currentToken)) {
				isLiteral = true;
				index = this.tokens.indexOf(currentToken);
				if (currentToken.getToken().length() > 1 && this.hasFimLiteral(currentToken)) {
					setAtributosTokenLiteral(currentToken);
					isLiteral = false;
					texto = VAZIO;
				} else {
					texto += currentToken.getToken() + ESPACO;
				}
			}
		}
	}
	
	private void setAtributosTokenLiteral(Token token) {
		token.setCodigo(TokenEnum.LIT.getCod());
		token.setDescricao(TokenEnum.LIT.getDescricao());
		token.setToken(token.getToken().replace(ASPAS_SIMPLES, VAZIO).replace(ASPAS_DUPLAS, VAZIO));
	}
	
	private boolean hasInicioLiteral(Token token) {
		return token.getToken().startsWith(ASPAS_DUPLAS) || token.getToken().startsWith(ASPAS_SIMPLES);
	}
	
	private boolean hasFimLiteral(Token token) {
		return token.getToken().endsWith(ASPAS_DUPLAS) || token.getToken().endsWith(ASPAS_SIMPLES);
	}

}