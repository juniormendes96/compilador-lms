package models;

import enums.TokenEnum;

public class Token {

	private Integer codigo;
	private String token;
	private String descricao;
	private Integer linha;

	public Token(TokenEnum tokenEnum, Integer linha) {
		this.codigo = tokenEnum.getCod();
		this.token = tokenEnum.getSimbolo();
		this.descricao = tokenEnum.getDescricao();
		this.linha = linha;
	}
	
	public Token(Integer codigo, String token, String descricao, Integer linha) {
		this.codigo = codigo;
		this.token = token;
		this.descricao = descricao;
		this.linha = linha;
	}
	
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + "]";
	}
	
}
