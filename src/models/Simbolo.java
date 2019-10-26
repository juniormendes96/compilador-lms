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
	private Integer nivel;
	private Integer geralA;
	private Integer geralB;
	private Simbolo proximo;
	
	public Simbolo(String nome, CategoriaSimboloEnum categoria, Integer nivel, Integer geralA, Integer geralB) {
		this.nome = nome;
		this.categoria = categoria;
		this.nivel = nivel;
		this.geralA = geralA;
		this.geralB = geralB;
		this.proximo = null;
	}
	
	public Optional<Simbolo> buscarUltimoNivel(String nome) {
		return this.getProximosSimbolos()
				.stream()
				.filter(simbolo -> simbolo.nome.equals(nome))
				.max(Comparator.comparing(simbolo -> simbolo.nivel));
	}
	
	public Optional<Simbolo> buscarPorNivel(String nome, Integer nivel) {
		return this.getProximosSimbolos()
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
	
	public void atualizar(int geralA, Integer geralB) {
		this.geralA = geralA;
		this.geralB = geralB;
	}
	
	public void excluir(String nome, Integer nivel) {
		 Optional<Simbolo> simboloAnterior = this.getProximosSimbolos()
			.stream()
			.filter(simbolo -> simbolo.proximo.nome.equals(nome) && simbolo.proximo.nivel == nivel)
			.findFirst();
		 if (simboloAnterior.isPresent()) {
			 simboloAnterior.map(simbolo -> simbolo.proximo = simbolo.proximo.proximo);
		 } else {
			 throw new SimboloNaoEncontradoException(nome);
		 }
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
