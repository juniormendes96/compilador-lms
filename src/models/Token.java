package models;

public class Token {

	private Integer codigo;
	private String token;
	private String descricao;
	private int linha;

	public Token(Integer codigo, String token, String descricao, int linha) {
		this.codigo = codigo;
		this.token = token;
		this.descricao = descricao;
		this.linha = linha;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getToken() {
		return token;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	

}
