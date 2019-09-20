package models;

public class TabelaDeSimbolos {

	private String nome;
	private int categoria; //1-Variavel 2-Constante 3-Procedure 4-Parametro
	private int nivel;
	private int geralA;
	private int geralB;
	private int proximo; //Ponteiro do Hash
	
	public TabelaDeSimbolos(String nome, int categoria, int nivel, int geralA, int geralB, int proximo) {
		this.nome = nome;
		this.categoria = categoria;
		this.nivel = nivel;
		this.geralA = geralA;
		this.geralB = geralB;
		this.proximo = proximo;
	}

	@Override
	public String toString() {
		return nome + " - " +categoria + " - " + nivel + " - " + geralA + " - " + geralB + " - " + proximo;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getCategoria() {
		return categoria;
	}
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getGeralA() {
		return geralA;
	}
	public void setGeralA(int geralA) {
		this.geralA = geralA;
	}
	public int getGeralB() {
		return geralB;
	}
	public void setGeralB(int geralB) {
		this.geralB = geralB;
	}
	public int getProximo() {
		return proximo;
	}
	public void setProximo(int proximo) {
		this.proximo = proximo;
	}
}
