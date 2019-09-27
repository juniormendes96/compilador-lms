package core;

import java.util.Objects;

import exceptions.SimboloJaDeclaradoException;
import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;
import utils.HashUtils;

public class TabelaDeSimbolos {
	
	private final int TABLE_SIZE = 25147;
	
	private Simbolo[] simbolos;
	
	public TabelaDeSimbolos() {
		this.simbolos = new Simbolo[TABLE_SIZE];
	}
	
	public Simbolo buscar(String nome) {
		Simbolo simbolo = this.simbolos[this.getValorHash(nome)];
		if (Objects.nonNull(simbolo)) {
			if(!simbolo.getNome().equals(nome))
				return simbolo.buscarSimboloAtual(simbolo);
			else
				return simbolo;
		}
		throw new SimboloNaoEncontradoException(nome);
	}
	
	public void inserir(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		if (Objects.nonNull(simbolos[index])) {
			throw new SimboloJaDeclaradoException(simbolo.getNome());
		}
		if(this.simbolos[index] != null) 
			encontrouColisao(index, simbolo);
		else
			this.simbolos[index] = simbolo;
	}
	
	public void alterar(Simbolo simbolo, Simbolo novoSimbolo) {
		int indexSimboloAntigo = this.getValorHash(simbolo.getNome());
		
		if (Objects.isNull(this.simbolos[indexSimboloAntigo])) {
			throw new SimboloNaoEncontradoException(novoSimbolo.getNome());
		}

		if (simbolo.getNome().equals(novoSimbolo.getNome())) {
			this.simbolos[indexSimboloAntigo] = novoSimbolo;
		} else if(this.simbolos[indexSimboloAntigo] != simbolo) {
			this.simbolos[indexSimboloAntigo].alterarSimbolo(simbolo, novoSimbolo);
		} else {
			int indexNovoSimbolo = this.getValorHash(novoSimbolo.getNome());
			this.simbolos[indexSimboloAntigo] = null;
			this.simbolos[indexNovoSimbolo] = novoSimbolo;
		}
	}
	
	public void excluir(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		if (Objects.isNull(this.simbolos[index])) {
			throw new SimboloNaoEncontradoException(simbolo.getNome());
		}
		if(simbolo.getNome().equals(this.simbolos[index].getNome()))
			this.simbolos[index] = null;
		else
			this.simbolos[index].removeSimbolo(simbolo);
	}
	
	public void mostrarConteudo() {
		for (int i = 0; i < this.simbolos.length; i++) {
			if (Objects.nonNull(this.simbolos[i])) {
				System.out.println(this.simbolos[i]);
			}
		}
	}
	
	private int getValorHash(String nome) {
		return HashUtils.hash(nome, TABLE_SIZE);
	}
	
	private void encontrouColisao(int index, Simbolo simbolo) {
		this.simbolos[index].inserirProximoSimbolo(simbolo);
	}
}
