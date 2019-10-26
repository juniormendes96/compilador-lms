package models;

// Classe criada para mostrar os literais na tabela de literais
public class Literal {
	private Integer endereco;
	private String nome;
	
	public Literal(Integer endereco, String nome) {
		this.endereco = endereco;
		this.nome = nome;
	}

	public Integer getEndereco() {
		return endereco;
	}

	public String getNome() {
		return nome;
	}
	
}
