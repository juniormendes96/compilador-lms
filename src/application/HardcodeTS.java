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
		
		System.out.println("Nome - Categoria - Nível - GeralA - GeralB - Próximo\n");
		
		// Inserir 10 elementos
		System.out.println("INSERINDO SÍMBOLOS");
		Simbolo elemento1 = new Simbolo("var1", CategoriaSimboloEnum.CONSTANTE, 1, 1, 1);
		Simbolo elemento2 = new Simbolo("var2", CategoriaSimboloEnum.PARAMETRO, 1, 2, 2);
		Simbolo elemento3 = new Simbolo("var3", CategoriaSimboloEnum.VARIAVEL, 1, 3, 3);
		Simbolo elemento4 = new Simbolo("var4", CategoriaSimboloEnum.PROCEDURE, 1, 4, 4);
		Simbolo elemento5 = new Simbolo("var5", CategoriaSimboloEnum.PARAMETRO, 1, 5, 5);
		Simbolo elemento6 = new Simbolo("var6", CategoriaSimboloEnum.PROCEDURE, 1, 6, 6);
		Simbolo elemento7 = new Simbolo("var7", CategoriaSimboloEnum.VARIAVEL, 1, 7, 7);
		Simbolo elemento8 = new Simbolo("var8", CategoriaSimboloEnum.CONSTANTE, 1, 8, 8);
		Simbolo elemento9 = new Simbolo("var9", CategoriaSimboloEnum.PROCEDURE, 1, 9, 9);
		Simbolo elemento10 = new Simbolo("var10", CategoriaSimboloEnum.VARIAVEL, 1, 10, 10);
		Arrays.asList(elemento1, elemento2, elemento3, elemento4, elemento5, elemento6, elemento7, elemento8, elemento9, elemento10).forEach(elemento -> tabela.inserir(elemento));
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Alterar dados de 5 elementos
		System.out.println("ALTERANDO SÍMBOLOS");
		tabela.atualizar("var1", 1, 10, 10);
		tabela.atualizar("var2", 1, 20, 20);
		tabela.atualizar("var3", 1, 30, 30);
		tabela.atualizar("var4", 1, 40, 40);
		tabela.atualizar("var5", 1, 50, 50);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		// Excluir 3 elementos
		
		System.out.println("EXCLUINDO SÍMBOLOS");
		tabela.excluir("var8", 1);
		tabela.excluir("var9", 1);
		tabela.excluir("var10", 1);
		
		// Mostrar conteúdo da tabela
		tabela.mostrarConteudo();
		breakLine();
		
		System.out.println("BUSCANDO SÍMBOLOS");
		try {
			// Fazer uma busca por nome de 3 elementos que estão na tabela. Mostrar os dados completos dos elementos encontrados
			System.out.println(tabela.buscar("var1"));
			System.out.println(tabela.buscar("var2"));
			System.out.println(tabela.buscar("var3"));
			
			// Fazer uma busca por 1 elemento inexistente na tabela. Mostrar mensagem informando que o elemento não foi encontrado
			tabela.buscar("JAVA");
		} catch (SimboloNaoEncontradoException e) {
			System.out.println("\n" + e.getMessage());
		}
	}
}
