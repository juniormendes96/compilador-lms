package models;

import java.util.Objects;

import enums.CategoriaSimboloEnum;

public class Simbolo {

	private String nome;
	private CategoriaSimboloEnum categoria;
	private int nivel;
	private int geralA;
	private int geralB;
	private Simbolo proximo = null;
	
	public Simbolo(String nome, CategoriaSimboloEnum categoria, int nivel, int geralA, int geralB) {
		this.nome = nome;
		this.categoria = categoria;
		this.nivel = nivel;
		this.geralA = geralA;
		this.geralB = geralB;
	}
	
	public void inserirProximoSimbolo(Simbolo primeiro, Simbolo novo) {
		while(true) {
			if(primeiro.proximo == null) {
				primeiro.proximo = novo;
				break;
			} else {
				primeiro = primeiro.proximo;
			}
		}
	}
	
	public void removeSimbolo(Simbolo simbolo, String nome) {
		Simbolo anterior = buscarSimboloAnterior(simbolo, nome);
		simbolo = buscarSimbolo(simbolo, nome);

		if(simbolo.proximo != null)
			anterior.proximo = simbolo.proximo;
		simbolo = null;		
	}
	
	public void alterarSimbolo(Simbolo simbolo, String nome, int geralA, int geralB) {
		simbolo = buscarSimbolo(simbolo, nome);
		simbolo.geralA = geralA;
		simbolo.geralB = geralB;
	}
	
	public Simbolo buscarSimbolo(Simbolo simbolo, String nome){
		if (Objects.nonNull(simbolo.proximo) && !simbolo.getNome().equals(nome))
			return buscarSimbolo(simbolo.proximo, nome);
		return simbolo;
	}
		
	public Simbolo buscarSimboloAnterior(Simbolo primeiro, String nome){		
		while(primeiro.proximo != null) {
			if(primeiro.proximo.getNome().equals(nome))
				return primeiro;
			primeiro = primeiro.proximo;
		}
		return null;
	}	

	@Override
	public String toString() {
		return nome + " - " +categoria + " - " + nivel + " - " + geralA + " - " + geralB + " - " + (Objects.nonNull(proximo) ? proximo.getNome() : null);
	}
	
	public String getNome() {
		return nome;
	}
	
	public CategoriaSimboloEnum getCategoria() {
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
	
	public Simbolo getProximo() {
		return proximo;
	}
	
}
