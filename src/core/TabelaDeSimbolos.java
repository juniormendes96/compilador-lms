package core;

import java.util.Objects;
import exceptions.SimboloJaDeclaradoException;
import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;
import utils.HashUtils;

public class TabelaDeSimbolos {
		
	private Simbolo[] simbolos;
	
	public TabelaDeSimbolos() {
		this.simbolos = new Simbolo[HashUtils.tableSize];
	}
	
	public Simbolo buscar(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		//int index = 50;
		
		if (Objects.nonNull(simbolo)) {
			if(encontrouColisao(index)) {
				System.out.println(simbolo.getNome());
				return this.simbolos[index].buscarSimboloAtual(simbolo);
			}else
				return this.simbolos[index];
		}
		throw new SimboloNaoEncontradoException(simbolo.getNome());
	}
	
	public void inserir(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		//int index = 50;
		
		if (Objects.nonNull(simbolos[index]) && simbolos[index] == simbolo) {
			throw new SimboloJaDeclaradoException(simbolo.getNome());
		}
		
		if(encontrouColisao(index)) {
			simbolos[index].inserirProximoSimbolo(simbolos[index], simbolo);
		}else
			this.simbolos[index] = simbolo;
	}
	
	public void alterar(Simbolo simbolo, Simbolo novoSimbolo) {
		int indexSimboloAntigo = this.getValorHash(simbolo.getNome());
		//int indexSimboloAntigo = 50;
		
		if (Objects.isNull(this.simbolos[indexSimboloAntigo])) {
			throw new SimboloNaoEncontradoException(simbolo.getNome());
		}
		if (encontrouColisao(indexSimboloAntigo)) {
			this.simbolos[indexSimboloAntigo].alterarSimbolo(simbolo, novoSimbolo);
		} else {
			int indexNovoSimbolo = this.getValorHash(novoSimbolo.getNome());
			this.simbolos[indexSimboloAntigo] = null;
			this.simbolos[indexNovoSimbolo] = novoSimbolo;
		}
	}
	
	public void excluir(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		//int index = 50;
		if (Objects.isNull(this.simbolos[index])) {
			throw new SimboloNaoEncontradoException(simbolo.getNome());
		}
		if(encontrouColisao(index)) {
			if(this.simbolos[index].getProximo() == null)
				this.simbolos[index] = null;
			else
				this.simbolos[index].removeSimbolo(this.simbolos[index], simbolo);
		}else {
			this.simbolos[index] = null;
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
		return HashUtils.hash(nome);
	}
	
	private boolean encontrouColisao(int index) {
		Simbolo simbolo = simbolos[index];
		return (simbolo != null) ? true : false;
	}
}
