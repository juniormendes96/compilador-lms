package models;

import java.util.List;

public class ResultadoAnalise {

	private List<Token> tokens;
	private List<Erro> erros;

	public ResultadoAnalise(List<Token> tokens, List<Erro> erros) {
		this.tokens = tokens;
		this.erros = erros;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<Erro> getErros() {
		return erros;
	}

}
