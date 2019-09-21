package exceptions;

public class SimboloJaDeclaradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SimboloJaDeclaradoException(String nome) {
		super(String.format("O símbolo %s já foi declarado", nome));
	}
}
