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
	
	public void inserirProximoSimbolo(Simbolo novo) {
		Simbolo atual = this.proximo;
		while(true) {
			if(atual.proximo == null) {
				this.proximo = novo;
				break;
			} else {
				atual = atual.proximo;
			}
		}
	}
	
	public Simbolo buscarSimboloAtual(Simbolo simbolo){
		Simbolo atual = simbolo;
		
		while(atual!=null && !atual.getNome().equals(simbolo.getNome())){
			atual=atual.proximo;
		}
		
		if(atual!=null)
			return atual;
		return null;
	}
	
	public Simbolo buscarSimboloAnterior(Simbolo simbolo){
		Simbolo anterior = simbolo;
		
		while(anterior!=null && anterior.proximo != simbolo){
			anterior=anterior.proximo;
		}
		
		if(anterior!=null)
			return anterior;
		return null;
	}
	
	
	public void removeSimbolo(Simbolo simbolo) {
		Simbolo atual = buscarSimboloAtual(simbolo);
		Simbolo anterior = buscarSimboloAnterior(simbolo);
		if(atual.proximo != null)
			anterior.proximo = atual.proximo;
		atual = null;		
	}
	
	public void alterarSimbolo(Simbolo simbolo, Simbolo novoSimbolo) {
		Simbolo atual = buscarSimboloAtual(simbolo);
		if(atual.getNome().equals(novoSimbolo.getNome()))
			atual = novoSimbolo;		
	}
	
	@Override
	public String toString() {
		return nome + " - " +categoria + " - " + nivel + " - " + geralA + " - " + geralB + (Objects.nonNull(proximo) ? " - " + proximo.getNome() : "");
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
