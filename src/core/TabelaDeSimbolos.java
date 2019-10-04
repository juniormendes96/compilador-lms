package core;

import java.util.Objects;

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
			return simbolo.buscarUltimoNivel(nome).orElseThrow(() -> new SimboloNaoEncontradoException(nome));
		}
		throw new SimboloNaoEncontradoException(nome);
	}
	
	public Simbolo buscar(String nome, int nivel) {
		Simbolo simbolo = this.simbolos[this.getValorHash(nome)];
		if (Objects.nonNull(simbolo)) {
			return simbolo.buscarPorNivel(nome, nivel).orElseThrow(() -> new SimboloNaoEncontradoException(nome));
		}
		throw new SimboloNaoEncontradoException(nome);
	}
	
	public void inserir(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		if (Objects.isNull(simbolos[index])) {
			this.simbolos[index] = simbolo;
		} else {
			this.simbolos[index].inserir(simbolo);
		}
	}
	
	public void atualizar(String nome, int nivel, int geralA, int geralB) {
		Simbolo simbolo = this.buscar(nome, nivel);
		simbolo.atualizar(geralA, geralB);
	}
	
	public void excluir(String nome, int nivel) {
		int index = this.getValorHash(nome);
		Simbolo simbolo = this.simbolos[index];
		if (simbolo.getNome().equals(nome) && simbolo.getNivel() == nivel) {
			this.simbolos[index] = simbolo.getProximo();
		} else {
			simbolo.excluir(nome, nivel);
		}
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
}
