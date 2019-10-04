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
		System.out.println("INSERINDO SÍMBOLOS");
		Simbolo elemento1 = new Simbolo("Elemento 1", CategoriaSimboloEnum.CONSTANTE, 1, 1, 1);
		Simbolo elemento2 = new Simbolo("Elemento 2", CategoriaSimboloEnum.PARAMETRO, 1, 2, 2);
		Simbolo elemento3 = new Simbolo("Elemento 3", CategoriaSimboloEnum.VARIAVEL, 1, 3, 3);
		Simbolo elemento4 = new Simbolo("Elemento 4", CategoriaSimboloEnum.PROCEDURE, 1, 4, 4);
		Simbolo elemento5 = new Simbolo("Elemento 5", CategoriaSimboloEnum.PARAMETRO, 1, 5, 5);
		Simbolo elemento6 = new Simbolo("Elemento 6", CategoriaSimboloEnum.PROCEDURE, 1, 6, 6);
		Simbolo elemento7 = new Simbolo("Elemento 7", CategoriaSimboloEnum.VARIAVEL, 1, 7, 7);
		Simbolo elemento8 = new Simbolo("Elemento 8", CategoriaSimboloEnum.CONSTANTE, 1, 8, 8);
		Simbolo elemento9 = new Simbolo("Elemento 9", CategoriaSimboloEnum.PROCEDURE, 1, 9, 9);
		Simbolo elemento10 = new Simbolo("Elemento 10", CategoriaSimboloEnum.VARIAVEL, 1, 10, 10);
		Arrays.asList(elemento1, elemento2, elemento3, elemento4, elemento5, elemento6, elemento7, elemento8, elemento9, elemento10).forEach(elemento -> tabela.inserir(elemento));
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Alterar dados de 5 elementos
		System.out.println("ALTERANDO SÍMBOLOS");
		tabela.atualizar("Elemento 1", 1, 10, 10);
		tabela.atualizar("Elemento 2", 1, 20, 20);
		tabela.atualizar("Elemento 3", 1, 30, 30);
		tabela.atualizar("Elemento 4", 1, 40, 40);
		tabela.atualizar("Elemento 5", 1, 50, 50);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Excluir 3 elementos
		System.out.println("EXCLUINDO SÍMBOLOS");
		tabela.excluir("Elemento 8", 1);
		tabela.excluir("Elemento 9", 1);
		tabela.excluir("Elemento 10", 1);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Fazer uma busca por 1 elemento inexistente na tabela. Mostrar mensagem informando que o elemento não foi encontrado
		System.out.println("BUSCANDO SÍMBOLOS");
		try {
			tabela.buscar("JAVA");
		} catch (SimboloNaoEncontradoException e) {
			System.out.println(e.getMessage() + "\n");
		}
		
		// Fazer uma busca por nome de 3 elementos que estão na tabela. Mostrar os dados completos dos elementos encontrados
		System.out.println(tabela.buscar("Elemento 1"));
		System.out.println(tabela.buscar("Elemento 2"));
		System.out.println(tabela.buscar("Elemento 3"));

	}
}
