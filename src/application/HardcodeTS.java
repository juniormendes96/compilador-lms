package application;

import java.util.Arrays;

import core.TabelaDeSimbolos;
import enums.CategoriaSimboloEnum;
import exceptions.SimboloJaDeclaradoException;
import exceptions.SimboloNaoEncontradoException;
import models.Simbolo;

public class HardcodeTS {

	public static void breakLine() {
		System.out.print("\n");
		System.out.println("-------------------------------------------------------------");
		System.out.print("\n");
	}

	public static void main(String[] args) throws Exception {
		TabelaDeSimbolos tabela = new TabelaDeSimbolos(25147);
		
		// Inserir 10 elementos
		System.out.println("# INSERINDO SÍMBOLOS");
		Simbolo elemento1 = new Simbolo("var1", CategoriaSimboloEnum.CONSTANTE, 0, 1, 1);
		Simbolo duplicado = new Simbolo("var1", CategoriaSimboloEnum.CONSTANTE, 0, 1, 1);
		Simbolo elemento2 = new Simbolo("var2", CategoriaSimboloEnum.PARAMETRO, 0, 2, 2);
		Simbolo elemento3 = new Simbolo("var3", CategoriaSimboloEnum.VARIAVEL, 0, 3, 3);
		Simbolo elemento4 = new Simbolo("var4", CategoriaSimboloEnum.PROCEDURE, 0, 4, 4);
		Simbolo elemento5 = new Simbolo("var5", CategoriaSimboloEnum.PARAMETRO, 0, 5, 5);
		Simbolo elemento6 = new Simbolo("var6", CategoriaSimboloEnum.PROCEDURE, 0, 6, 6);
		Simbolo elemento7 = new Simbolo("var7", CategoriaSimboloEnum.VARIAVEL, 0, 7, 7);
		Simbolo elemento8 = new Simbolo("var8", CategoriaSimboloEnum.CONSTANTE, 0, 8, 8);
		Simbolo elemento9 = new Simbolo("var9", CategoriaSimboloEnum.PROCEDURE, 0, 9, 9);
		Simbolo elemento10 = new Simbolo("var10", CategoriaSimboloEnum.VARIAVEL, 0, 10, 10);
		
		
		Arrays.asList(elemento1, elemento2, elemento3, elemento4, elemento5, elemento6, elemento7, elemento8, elemento9, elemento10).forEach(elemento -> tabela.inserir(elemento));
		try {
			tabela.inserir(duplicado);
		} catch (SimboloJaDeclaradoException e) {
			System.out.println(e.getMessage() + "\n");
		}
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Alterar dados de 5 elementos
		System.out.println("# ALTERANDO SÍMBOLOS");
		tabela.alterar("var1", 0, 10, 10);
		tabela.alterar("var2", 0, 20, 20);
		tabela.alterar("var3", 0, 30, 30);
		tabela.alterar("var4", 0, 40, 40);
		tabela.alterar("var5", 0, 50, 50);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Excluir 3 elementos
		System.out.println("# EXCLUINDO SÍMBOLOS");
		tabela.excluir("var8");
		tabela.excluir("var9");
		tabela.excluir("var10");
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();		
		
		System.out.println("# BUSCANDO SÍMBOLOS");
		try {
			// Fazer uma busca por nome de 3 elementos que estão na tabela. Mostrar os dados completos dos elementos encontrados
			System.out.println(tabela.buscar("var1"));
			System.out.println(tabela.buscar("var2"));
			System.out.println(tabela.buscar("var3"));
			
			// Fazer uma busca por 1 elemento inexistente na tabela. Mostrar mensagem informando que o elemento não foi encontrado
			tabela.buscar("var8");
		} catch (SimboloNaoEncontradoException e) {
			System.out.println(e.getMessage() + "\n");
		}
	}
}
