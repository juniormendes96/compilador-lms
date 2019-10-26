package exceptions;

public class AnalisadorSemanticoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AnalisadorSemanticoException(String erro) {
		super(erro);
	}
}
