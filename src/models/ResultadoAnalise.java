package models;

import java.util.List;

public class ResultadoAnalise {

	private List<Token> models;
	private List<Erro> erros;

	public ResultadoAnalise(List<Token> models, List<Erro> erros) {
		this.models = models;
		this.erros = erros;
	}

	public List<Token> getModels() {
		return models;
	}

	public List<Erro> getErros() {
		return erros;
	}

}
