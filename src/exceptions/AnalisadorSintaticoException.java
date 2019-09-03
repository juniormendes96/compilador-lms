package exceptions;

public class AnalisadorSintaticoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AnalisadorSintaticoException(String erro, Integer linha) {
		super("ERRO SINTÁTICO: " + erro + " na linha " + linha);
	}
	
}
