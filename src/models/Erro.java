package models;

public class Erro {
	
	public String mensagem;
	public int linha;
	
	
	public Erro(String mensagem, int linha) {
		this.mensagem = mensagem + " na linha "+linha;
		this.linha = linha;
	}
	
	public Erro(String mensagem) {
		this.mensagem = mensagem;
	}	
	
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	
}
