package core;

import java.util.Arrays;
import java.util.Objects;

import enums.CategoriaIdentificadorEnum;
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
			return simbolo;
		}
		throw new SimboloNaoEncontradoException(nome);
	}
	
	public void inserir(Simbolo simbolo) {
		int index = this.getValorHash(simbolo.getNome());
		if (Objects.nonNull(simbolos[index])) {
			throw new SimboloJaDeclaradoException(simbolo.getNome());
		}
		this.simbolos[index] = simbolo;
	}
	
	public void mostrarConteudo() {
		for (int i = 0; i < this.simbolos.length; i++) {
			if (Objects.nonNull(this.simbolos[i])) {
				System.out.println(this.simbolos[i]);
			}
		}
	}
	
	public void alterar(Simbolo simbolo, Simbolo novoSimbolo) {
		int indexIdentificadorAntigo = this.getValorHash(simbolo.getNome());
		
		if (Objects.isNull(this.simbolos[indexIdentificadorAntigo])) {
			throw new SimboloNaoEncontradoException(novoSimbolo.getNome());
		}

		if (simbolo.getNome().equals(novoSimbolo.getNome())) {
			this.simbolos[indexIdentificadorAntigo] = novoSimbolo;
		} else {
			int indexNovoIdentificador = this.getValorHash(novoSimbolo.getNome());
			this.simbolos[indexIdentificadorAntigo] = null;
			this.simbolos[indexNovoIdentificador] = novoSimbolo;
		}
	}
	
	public void excluir(Simbolo identificador) {
		int index = this.getValorHash(identificador.getNome());
		if (Objects.isNull(this.simbolos[index])) {
			throw new SimboloNaoEncontradoException(identificador.getNome());
		}
		this.simbolos[index] = null;
	}
	
	private int getValorHash(String nome) {
		return HashUtils.hash(nome, TABLE_SIZE);
	}

	public static void main(String[] args) throws Exception {
		TabelaDeSimbolos tabela = new TabelaDeSimbolos();
		
		// Inserir 10 elementos
		Simbolo elemento1 = new Simbolo("Elemento 1", CategoriaIdentificadorEnum.CONSTANTE, 1, 1, 1, 1);
		Simbolo elemento2 = new Simbolo("Elemento 2", CategoriaIdentificadorEnum.PARAMETRO, 2, 2, 2, 2);
		Simbolo elemento3 = new Simbolo("Elemento 3", CategoriaIdentificadorEnum.VARIAVEL, 3, 3, 3, 3);
		Simbolo elemento4 = new Simbolo("Elemento 4", CategoriaIdentificadorEnum.PROCEDURE, 4, 4, 4, 4);
		Simbolo elemento5 = new Simbolo("Elemento 5", CategoriaIdentificadorEnum.PARAMETRO, 5, 5, 5, 5);
		Simbolo elemento6 = new Simbolo("Elemento 6", CategoriaIdentificadorEnum.PROCEDURE, 6, 6, 6, 6);
		Simbolo elemento7 = new Simbolo("Elemento 7", CategoriaIdentificadorEnum.VARIAVEL, 7, 7, 7, 7);
		Simbolo elemento8 = new Simbolo("Elemento 8", CategoriaIdentificadorEnum.CONSTANTE, 8, 8, 8, 8);
		Simbolo elemento9 = new Simbolo("Elemento 9", CategoriaIdentificadorEnum.PROCEDURE, 9, 9, 9, 9);
		Simbolo elemento10 = new Simbolo("Elemento 10", CategoriaIdentificadorEnum.VARIAVEL, 10, 10, 10, 10);
		Arrays.asList(elemento1, elemento2, elemento3, elemento4, elemento5, elemento6, elemento7, elemento8, elemento9, elemento10).forEach(elemento -> tabela.inserir(elemento));
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		System.out.print("\n");
		
		// Alterar dados de 5 elementos
		tabela.alterar(elemento1, new Simbolo("Elemento 1 editado", CategoriaIdentificadorEnum.PROCEDURE, 1, 1, 1, 1));
		tabela.alterar(elemento2, new Simbolo("Elemento 2 editado", CategoriaIdentificadorEnum.VARIAVEL, 2, 2, 2, 2));
		tabela.alterar(elemento3, new Simbolo("Elemento 3 editado", CategoriaIdentificadorEnum.CONSTANTE, 3, 3, 3, 3));
		tabela.alterar(elemento4, new Simbolo("Elemento 4 editado", CategoriaIdentificadorEnum.PARAMETRO, 4, 4, 4, 4));
		tabela.alterar(elemento5, new Simbolo("Elemento 5 editado", CategoriaIdentificadorEnum.PARAMETRO, 5, 5, 5, 5));
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		System.out.print("\n");
		
		// Excluir 3 elementos
		tabela.excluir(elemento8);
		tabela.excluir(elemento9);
		tabela.excluir(elemento10);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		System.out.print("\n");
		
		// Fazer uma busca por 1 elemento inexistente na tabela. Mostrar mensagem informando que o elemento não foi encontrado
		try {
			tabela.buscar("Elemento inexistente");
		} catch (SimboloNaoEncontradoException e) {
			System.out.println("Símbolo não encontrado\n");
		}
		
		// Fazer uma busca por nome de 3 elementos que estão na tabela. Mostrar os dados completos dos elementos encontrados
		System.out.println(tabela.buscar("Elemento 1 editado"));
		System.out.println(tabela.buscar("Elemento 2 editado"));
		System.out.println(tabela.buscar("Elemento 3 editado"));

	}

}
