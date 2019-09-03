package exceptions;

public class AnalisadorSintaticoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AnalisadorSintaticoException(String erro, Integer linha) {
		super(String.format("ERRO SINTÁTICO: %s na linha %s", erro, linha.toString()));
	}
	
}
