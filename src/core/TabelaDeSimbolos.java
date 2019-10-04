package core;

import java.util.Objects;
import exceptions.SimboloJaDeclaradoException;
import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;
import utils.HashUtils;

public class TabelaDeSimbolos {
		
	private Simbolo[] simbolos;
	public int tableSize = 25147;
	
	public TabelaDeSimbolos() {
		this.simbolos = new Simbolo[tableSize];
	}
	
	public Simbolo buscar(String nome) {
		int index = 50;// this.getValorHash(nome);
		
		Simbolo simbolo = null;
		
		if(encontrouColisao(index))
			simbolo = this.simbolos[index].buscarSimbolo(simbolos[index], nome);
		
		if(simbolo.getNome()!=nome) 
			throw new SimboloNaoEncontradoException(nome);
		
		return simbolo;
	}
	
	public void inserir(Simbolo simbolo) {
		int index = 50;//this.getValorHash(simbolo.getNome());
		
		if (Objects.nonNull(simbolos[index]) && simbolos[index] == simbolo) {
			throw new SimboloJaDeclaradoException(simbolo.getNome());
		}
		
		if(encontrouColisao(index)) {
			simbolos[index].inserirProximoSimbolo(simbolos[index], simbolo);
		}else
			this.simbolos[index] = simbolo;
	}
	
	public void alterar(String nome, int geralA, int geralB) {
		int index = 50;//this.getValorHash(nome);
		
		if (!encontrouColisao(index)) {
			throw new SimboloNaoEncontradoException(nome);
		}
		
		this.simbolos[index].alterarSimbolo(simbolos[index], nome, geralA, geralB);
	}
	
	public void excluir(String nome) {
		int index = 50;// this.getValorHash(nome);

		if (Objects.isNull(this.simbolos[index])) {
			throw new SimboloNaoEncontradoException(nome);
		}
		
		if(encontrouColisao(index)) {
			if(this.simbolos[index].getProximo() == null)
				this.simbolos[index] = null;
			else
				this.simbolos[index].removeSimbolo(this.simbolos[index], nome);
		}
	}
	
	public void mostrarConteudo() {
		for (int i = 0; i < this.simbolos.length; i++) {
			if (Objects.nonNull(this.simbolos[i])) {
				System.out.println(this.simbolos[i]);
				Simbolo prox = this.simbolos[i].getProximo();
				while(prox != null) {
					System.out.println(	"> "+prox);
					prox = prox.getProximo();			
				}			
			}
		}
	}
	
	private int getValorHash(String nome) {
		return HashUtils.hash(nome, tableSize);
	}
	
	private boolean encontrouColisao(int index) {
		Simbolo simbolo = simbolos[index];
		return (simbolo != null) ? true : false;
	}
}
