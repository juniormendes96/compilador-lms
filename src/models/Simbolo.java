package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import enums.CategoriaSimboloEnum;
import exceptions.SimboloNaoEncontradoException;

public class Simbolo {

	private String nome;
	private CategoriaSimboloEnum categoria;
	private int nivel;
	private int geralA;
	private int geralB;
	private Simbolo proximo;
	
	public Simbolo(String nome, CategoriaSimboloEnum categoria, int nivel, int geralA, int geralB) {
		this.nome = nome;
		this.categoria = categoria;
		this.nivel = nivel;
		this.geralA = geralA;
		this.geralB = geralB;
		this.proximo = null;
	}
	
	public Optional<Simbolo> buscarUltimoNivel(String nome) {
		List<Simbolo> simbolos = this.getProximosSimbolos();
		return simbolos
				.stream()
				.filter(simbolo -> simbolo.nome.equals(nome))
				.max(Comparator.comparing(simbolo -> simbolo.nivel));
	}
	
	public Optional<Simbolo> buscarPorNivel(String nome, int nivel) {
		List<Simbolo> simbolos = this.getProximosSimbolos();
		return simbolos
				.stream()
				.filter(simbolo -> simbolo.nome.equals(nome) && simbolo.nivel == nivel)
				.findFirst();
	}
	
	public void inserir(Simbolo simbolo) {
		if (Objects.isNull(this.proximo)) {
			this.proximo = simbolo;
		} else {
			this.proximo.inserir(simbolo);
		}
	}
	
	public void atualizar(int geralA, int geralB) {
		this.geralA = geralA;
		this.geralB = geralB;
	}
	
	public void excluir(String nome, int nivel) {
		this.getProximosSimbolos()
			.stream()
			.filter(simbolo -> simbolo.proximo.nome.equals(nome) && simbolo.proximo.nivel == nivel)
			.findFirst()
			.map(simbolo -> simbolo.proximo = Objects.nonNull(simbolo.proximo.proximo) ? simbolo.proximo.proximo : null)
			.orElseThrow(() -> new SimboloNaoEncontradoException(nome));
	}
	
	public String getNome() {
		return nome;
	}
	
	public CategoriaSimboloEnum getCategoria() {
		return categoria;
	}
	
	public int getNivel() {
		return nivel;
	}
	
	public int getGeralA() {
		return geralA;
	}
	
	public int getGeralB() {
		return geralB;
	}
	
	public Simbolo getProximo() {
		return proximo;
	}
	
	@Override
	public String toString() {
		return nome + " - " +categoria + " - " + nivel + " - " + geralA + " - " + geralB + " - " + (Objects.nonNull(proximo) ? proximo.getNome() : null);
	}
	
	public List<Simbolo> getProximosSimbolos() {
		List<Simbolo> lista = new ArrayList<>();
		Simbolo simbolo = this;
		while (Objects.nonNull(simbolo)) {
			lista.add(simbolo);
			simbolo = simbolo.proximo;
		}
		return lista;
	}

}
