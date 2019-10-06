package core;

import java.util.Objects;

import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;
import utils.HashUtils;

public class TabelaDeSimbolos {
	
	private final int tableSize;
	
	private Simbolo[] simbolos;
	
	public TabelaDeSimbolos(int tableSize) {
		this.simbolos = new Simbolo[tableSize];
		this.tableSize = tableSize;
	}
	
	public Simbolo buscar(String nome) {
		Simbolo primeiroSimboloDaPosicao = this.simbolos[this.getValorHash(nome)];
		if (Objects.nonNull(primeiroSimboloDaPosicao)) {
			return primeiroSimboloDaPosicao.buscarUltimoNivel(nome).orElseThrow(() -> new SimboloNaoEncontradoException(nome));
		}
		throw new SimboloNaoEncontradoException(nome);
	}
	
	public Simbolo buscar(String nome, int nivel) {
		Simbolo primeiroSimboloDaPosicao = this.simbolos[this.getValorHash(nome)];
		if (Objects.nonNull(primeiroSimboloDaPosicao)) {
			return primeiroSimboloDaPosicao.buscarPorNivel(nome, nivel).orElseThrow(() -> new SimboloNaoEncontradoException(nome));
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
		Simbolo primeiroSimboloDaPosicao = this.simbolos[index];
		if (primeiroSimboloDaPosicao.getNome().equals(nome) && primeiroSimboloDaPosicao.getNivel() == nivel) {
			this.simbolos[index] = primeiroSimboloDaPosicao.getProximo();
		} else {
			primeiroSimboloDaPosicao.excluir(nome, nivel);
		}
	}
	
	public void mostrarConteudo() {
		for (int i = 0; i < this.simbolos.length; i++) {
			Simbolo simbolo = this.simbolos[i];
			if (Objects.nonNull(simbolo)) {
				System.out.println(simbolo);
				if (Objects.nonNull(simbolo.getProximo())) {
					simbolo.getProximo().getProximosSimbolos().stream().forEach(simb -> System.out.println("> " + simb));
				}
			}
		}
	}
	
	private int getValorHash(String nome) {
		return HashUtils.hash(nome, tableSize);
	}
}
