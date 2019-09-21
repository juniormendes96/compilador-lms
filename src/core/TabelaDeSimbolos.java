package core;

import java.util.Arrays;
import java.util.Objects;

import enums.CategoriaIdentificadorEnum;
import exceptions.IdentificadorJaDeclaradoException;
import exceptions.IdentificadorNaoEncontradoException;
import models.Identificador;
import utils.HashUtils;

public class TabelaDeSimbolos {
	
	private final int tableSize = 25147;
	
	private Identificador[] identificadores;
	private int[] hashTable;
	
	public TabelaDeSimbolos() {
		this.identificadores = new Identificador[tableSize];
		this.iniciarHashTable();
	}
	
	public Identificador buscar(String nome) {
		Identificador identificador = this.identificadores[this.getValorHash(nome)];
		if (Objects.nonNull(identificador)) {
			return identificador;
		}
		throw new IdentificadorNaoEncontradoException(nome);
	}
	
	public void inserir(Identificador identificador) {
		int index = this.getValorHash(identificador.getNome());
		if (hashTable[index] > 0) {
			throw new IdentificadorJaDeclaradoException(identificador.getNome());
		}
		this.identificadores[index] = identificador;
		hashTable[index]++;
	}
	
	public void mostrarConteudo() {
		for (int i = 0; i < this.hashTable.length; i++) {
			if (this.hashTable[i] != 0) {
				System.out.println(this.identificadores[i]);
			}
		}
	}
	
	public void alterar(Identificador identificador, Identificador novoIdentificador) {
		int indexIdentificadorAntigo = this.getValorHash(identificador.getNome());
		
		if (this.hashTable[indexIdentificadorAntigo] <= 0) {
			throw new IdentificadorNaoEncontradoException(novoIdentificador.getNome());
		}

		if (identificador.getNome().equals(novoIdentificador.getNome())) {
			this.identificadores[indexIdentificadorAntigo] = novoIdentificador;
		} else {
			int indexNovoIdentificador = this.getValorHash(novoIdentificador.getNome());
			this.identificadores[indexIdentificadorAntigo] = null;
			this.identificadores[indexNovoIdentificador] = novoIdentificador;

			this.hashTable[indexIdentificadorAntigo]--;
			this.hashTable[indexNovoIdentificador]++;
		}
	}
	
	public void excluir(Identificador identificador) {
		int index = this.getValorHash(identificador.getNome());
		if (this.hashTable[index] <= 0) {
			throw new IdentificadorNaoEncontradoException(identificador.getNome());
		}
		this.identificadores[index] = null;
		this.hashTable[index] = 0;
	}
	
	private int getValorHash(String nome) {
		return HashUtils.hash(nome, tableSize);
	}
	
	private void iniciarHashTable() {
		this.hashTable = new int[tableSize];
		for (int i = 0; i < tableSize; i++) {
			this.hashTable[i] = 0;
		}
	}
	
	public static void main(String[] args) throws Exception {
		TabelaDeSimbolos tabela = new TabelaDeSimbolos();
		
		// Inserir 10 elementos
		Identificador elemento1 = new Identificador("Elemento 1", CategoriaIdentificadorEnum.CONSTANTE, 1, 1, 1, 1);
		Identificador elemento2 = new Identificador("Elemento 2", CategoriaIdentificadorEnum.PARAMETRO, 2, 2, 2, 2);
		Identificador elemento3 = new Identificador("Elemento 3", CategoriaIdentificadorEnum.VARIAVEL, 3, 3, 3, 3);
		Identificador elemento4 = new Identificador("Elemento 4", CategoriaIdentificadorEnum.PROCEDURE, 4, 4, 4, 4);
		Identificador elemento5 = new Identificador("Elemento 5", CategoriaIdentificadorEnum.PARAMETRO, 5, 5, 5, 5);
		Identificador elemento6 = new Identificador("Elemento 6", CategoriaIdentificadorEnum.PROCEDURE, 6, 6, 6, 6);
		Identificador elemento7 = new Identificador("Elemento 7", CategoriaIdentificadorEnum.VARIAVEL, 7, 7, 7, 7);
		Identificador elemento8 = new Identificador("Elemento 8", CategoriaIdentificadorEnum.CONSTANTE, 8, 8, 8, 8);
		Identificador elemento9 = new Identificador("Elemento 9", CategoriaIdentificadorEnum.PROCEDURE, 9, 9, 9, 9);
		Identificador elemento10 = new Identificador("Elemento 10", CategoriaIdentificadorEnum.VARIAVEL, 10, 10, 10, 10);
		for (Identificador identificador : Arrays.asList(elemento1, elemento2, elemento3, elemento4, elemento5, elemento6, elemento7, elemento8, elemento9, elemento10)) {
			tabela.inserir(identificador);
		}
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		System.out.print("\n");
		
		// Alterar dados de 5 elementos
		tabela.alterar(elemento1, new Identificador("Elemento 1 editado", CategoriaIdentificadorEnum.PROCEDURE, 1, 1, 1, 1));
		tabela.alterar(elemento2, new Identificador("Elemento 2 editado", CategoriaIdentificadorEnum.VARIAVEL, 2, 2, 2, 2));
		tabela.alterar(elemento3, new Identificador("Elemento 3 editado", CategoriaIdentificadorEnum.CONSTANTE, 3, 3, 3, 3));
		tabela.alterar(elemento4, new Identificador("Elemento 4 editado", CategoriaIdentificadorEnum.PARAMETRO, 4, 4, 4, 4));
		tabela.alterar(elemento5, new Identificador("Elemento 5 editado", CategoriaIdentificadorEnum.PARAMETRO, 5, 5, 5, 5));
		
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
		} catch(IdentificadorNaoEncontradoException e) {
			System.out.println("Identificador não encontrado\n");
		}
		
		// Fazer uma busca por nome de 3 elementos que estão na tabela. Mostrar os dados completos dos elementos encontrados
		System.out.println(tabela.buscar("Elemento 1 editado"));
		System.out.println(tabela.buscar("Elemento 2 editado"));
		System.out.println(tabela.buscar("Elemento 3 editado"));

	}

}
