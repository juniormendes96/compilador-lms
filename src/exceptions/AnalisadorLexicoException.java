package exceptions;

import models.Token;

public class AnalisadorLexicoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AnalisadorLexicoException(String erro, Token token) {
		super(String.format("%s (Token %s linha %s)", erro, token.getToken(), token.getLinha()));
	}
}
