package models;

import enums.CategoriaIdentificadorEnum;

public class Simbolo {

	private String nome;
	private CategoriaIdentificadorEnum categoria;
	private int nivel;
	private int geralA;
	private int geralB;
	private int proximo;
	
	public Simbolo(String nome, CategoriaIdentificadorEnum categoria, int nivel, int geralA, int geralB, int proximo) {
		this.nome = nome;
		this.categoria = categoria;
		this.nivel = nivel;
		this.geralA = geralA;
		this.geralB = geralB;
		this.proximo = proximo;
	}

	@Override
	public String toString() {
		return nome + " - " +categoria + " - " + nivel + " - " + geralA + " - " + geralB + " - " + proximo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public CategoriaIdentificadorEnum getCategoria() {
		return categoria;
	}
	
	public int getNivel() {
		return nivel;
	}
	
	public int getGeralA() {
		return geralA;
	}
	
	public int getGeralB() {
		return geralB;
	}
	
	public int getProximo() {
		return proximo;
	}
}
