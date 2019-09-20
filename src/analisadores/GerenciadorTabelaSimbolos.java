package analisadores;

import java.util.LinkedList;
import java.util.List;

import models.TabelaDeSimbolos;

public class GerenciadorTabelaSimbolos {

	public List<TabelaDeSimbolos> listaTS = new LinkedList<TabelaDeSimbolos>();
	
	public void inserir(TabelaDeSimbolos ts) {
		System.out.println("Elemento Adicionado");
		listaTS.add(ts);
	}
	
	public void visualizarTabelaDeSimbolos() {
		System.out.println("Nome Categoria Nível GeralA GeralB Próximo");
		listaTS.forEach(ts -> System.out.println(ts));
		System.out.println("");
	}

	public void alterar(int index, TabelaDeSimbolos novo) {
		System.out.println("Elemento Alterado");
		listaTS.get(index).setNome(novo.getNome());
		listaTS.get(index).setCategoria(novo.getCategoria());
		listaTS.get(index).setNivel(novo.getNivel());
		listaTS.get(index).setGeralA(novo.getGeralA());
		listaTS.get(index).setGeralB(novo.getGeralB());
		listaTS.get(index).setProximo(novo.getProximo());
	}

	public void excluir(int index) {
		System.out.println("Elemento Excluido");
		listaTS.remove(index);
	}
	
	public void buscarElemento(String nome) {
		TabelaDeSimbolos resultado = listaTS.stream().filter(ts->ts.getNome().equals(nome)).findAny().orElse(null);
		if(resultado != null)
			System.out.println(resultado);
		else
			System.out.println("Elemento inexistente na tabela");
		//return resultado;
	}
	
	public List<TabelaDeSimbolos> getListaTS() {
		return listaTS;
	}
}
