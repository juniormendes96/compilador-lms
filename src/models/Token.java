package models;

public class Token {

	private Integer codigo;
	private String token;
	private String descricao;
	private Integer linha;

	public Token(Integer codigo, String token, String descricao, Integer linha) {
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
	
	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}
	
}
