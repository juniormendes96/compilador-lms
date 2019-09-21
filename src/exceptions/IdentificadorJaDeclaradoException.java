package exceptions;

public class IdentificadorJaDeclaradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IdentificadorJaDeclaradoException(String nome) {
		super(String.format("O identificador %s já foi declarado", nome));
	}
}
