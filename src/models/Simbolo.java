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
	
	public void removeSimbolo(Simbolo primeiro, Simbolo simbolo) {
		simbolo = buscarSimboloAtual(simbolo);
		Simbolo anterior = buscarSimboloAnterior(primeiro, simbolo);
		
		if(simbolo.proximo != null)
			anterior.proximo = simbolo.proximo;
		simbolo = null;		
	}
	
	public void alterarSimbolo(Simbolo simbolo, Simbolo novoSimbolo) {
		simbolo = buscarSimboloAtual(simbolo);
		simbolo.nome = novoSimbolo.getNome();
		simbolo.categoria = novoSimbolo.getCategoria();
		simbolo.nivel = novoSimbolo.getNivel();
		simbolo.geralA = novoSimbolo.getGeralA();
		simbolo.geralB = novoSimbolo.getGeralB();
	}
	
	public Simbolo buscarSimbolo(Simbolo simbolo, int nivel){
		if (Objects.nonNull(simbolo.proximo) && simbolo.getNivel() == nivel)
			return buscarSimboloAtual(simbolo.proximo);
		return simbolo;
	}
	
	public Simbolo buscarSimboloAtual(Simbolo simbolo){
		if (Objects.nonNull(simbolo.proximo) && simbolo.proximo == simbolo)
			return buscarSimboloAtual(simbolo.proximo);
		return simbolo;
	}
	
	public Simbolo buscarSimboloAnterior(Simbolo primeiro, Simbolo simbolo){		
		while(primeiro.proximo != null) {
			if(primeiro.proximo == simbolo)
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
