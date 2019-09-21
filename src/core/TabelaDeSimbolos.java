package core;

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
		int index = this.getValorHash(identificador.getNome());
		if (this.hashTable[index] <= 0) {
			throw new IdentificadorNaoEncontradoException(novoIdentificador.getNome());
		}
		this.identificadores[index] = novoIdentificador;
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
		tabela.inserir(new Identificador("Teste", CategoriaIdentificadorEnum.CONSTANTE, 1, 1, 1, 1));
		tabela.mostrarConteudo();
	}

}
