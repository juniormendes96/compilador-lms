package core;

import java.util.Objects;

import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;
import utils.HashUtils;

public class TabelaDeSimbolos {
	
	private final int tableSize;
	
	private Simbolo[] simbolos;
	
	public TabelaDeSimbolos(int tableSize) {
		this.tableSize = tableSize;
		this.simbolos = new Simbolo[this.tableSize];
	}
	
	public boolean existe(String nome, int nivel) {
		try {
			this.buscar(nome, nivel);
			return true;
		} catch (SimboloNaoEncontradoException e) {
			return false;
		}
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
		if (Objects.isNull(primeiroSimboloDaPosicao)) {
			throw new SimboloNaoEncontradoException(nome);
		}
		if (primeiroSimboloDaPosicao.getNome().equals(nome) && primeiroSimboloDaPosicao.getNivel() == nivel) {
			this.simbolos[index] = primeiroSimboloDaPosicao.getProximo();
		} else {
			primeiroSimboloDaPosicao.excluir(nome, nivel);
		}
	}
	
	public void excluirPorNivel(int nivel) {
		for (int i = 0; i < simbolos.length - 1; i++) {
			Simbolo simbolo = simbolos[i];
			if (Objects.nonNull(simbolo)) {
				if (simbolo.getNivel() == nivel && Objects.isNull(simbolo.getProximo())) {
					simbolos[i] = null;
				} else {
					if (Objects.nonNull(simbolo.getProximo())) {
						simbolo.excluir(nivel);
					}
				}
			}
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
