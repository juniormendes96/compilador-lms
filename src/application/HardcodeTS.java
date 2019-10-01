package application;

import java.util.Arrays;

import core.TabelaDeSimbolos;
import enums.CategoriaSimboloEnum;
import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;

public class HardcodeTS {

	public static void breakLine() {
		System.out.print("\n");
		System.out.println("-------------------------------------------------------------");
		System.out.print("\n");
	}

	public static void main(String[] args) throws Exception {
		TabelaDeSimbolos tabela = new TabelaDeSimbolos();
		
		// Inserir 10 elementos
		System.out.println("# INSERINDO SÍMBOLOS");
		Simbolo elemento1 = new Simbolo("Elemento 1", CategoriaSimboloEnum.CONSTANTE, 1, 1, 1);
		Simbolo elemento2 = new Simbolo("Elemento 2", CategoriaSimboloEnum.PARAMETRO, 2, 2, 2);
		Simbolo elemento3 = new Simbolo("Elemento 3", CategoriaSimboloEnum.VARIAVEL, 3, 3, 3);
		Simbolo elemento4 = new Simbolo("Elemento 4", CategoriaSimboloEnum.PROCEDURE, 4, 4, 4);
		Simbolo elemento5 = new Simbolo("Elemento 5", CategoriaSimboloEnum.PARAMETRO, 5, 5, 5);
		Simbolo elemento6 = new Simbolo("Elemento 6", CategoriaSimboloEnum.PROCEDURE, 6, 6, 6);
		Simbolo elemento7 = new Simbolo("Elemento 7", CategoriaSimboloEnum.VARIAVEL, 7, 7, 7);
		Simbolo elemento8 = new Simbolo("Elemento 8", CategoriaSimboloEnum.CONSTANTE, 8, 8, 8);
		Simbolo elemento9 = new Simbolo("Elemento 9", CategoriaSimboloEnum.PROCEDURE, 9, 9, 9);
		Simbolo elemento10 = new Simbolo("Elemento 10", CategoriaSimboloEnum.VARIAVEL, 10, 10, 10);
		Arrays.asList(elemento1, elemento2, elemento3, elemento4, elemento5, elemento6, elemento7, elemento8, elemento9, elemento10).forEach(elemento -> tabela.inserir(elemento));
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Alterar dados de 5 elementos
		System.out.println("# ALTERANDO SÍMBOLOS");
		tabela.alterar(elemento1, new Simbolo("Elemento 1", CategoriaSimboloEnum.PROCEDURE, 1, 1, 5));
		tabela.alterar(elemento2, new Simbolo("Elemento 2", CategoriaSimboloEnum.VARIAVEL, 2, 2, 4));
		tabela.alterar(elemento3, new Simbolo("Elemento 3", CategoriaSimboloEnum.CONSTANTE, 3, 3, 2));
		tabela.alterar(elemento4, new Simbolo("Elemento 4", CategoriaSimboloEnum.PARAMETRO, 4, 4, 1));
		tabela.alterar(elemento5, new Simbolo("Elemento 5", CategoriaSimboloEnum.PARAMETRO, 5, 5, 3));
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Excluir 3 elementos
		System.out.println("# EXCLUINDO SÍMBOLOS");
		tabela.excluir(elemento8);
		tabela.excluir(elemento9);
		tabela.excluir(elemento10);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();		
		
		System.out.println("# BUSCANDO SÍMBOLOS");
		try {
			// Fazer uma busca por 1 elemento inexistente na tabela. Mostrar mensagem informando que o elemento não foi encontrado
			tabela.buscar(elemento8);
		} catch (SimboloNaoEncontradoException e) {
			System.out.println(e.getMessage() + "\n");
		}
			// Fazer uma busca por nome de 3 elementos que estão na tabela. Mostrar os dados completos dos elementos encontrados
		System.out.println(tabela.buscar(elemento1));
		System.out.println(tabela.buscar(elemento2));
		System.out.println(tabela.buscar(elemento3));

	}
}
