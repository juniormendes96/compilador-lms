package analisadores;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import constants.Constants;
import models.Token;

public class AnalisadorSintatico implements Constants {

	private List<Integer> pilha;
	private List<Integer> fila;
	private List<Token> tokens;

	public AnalisadorSintatico(List<Token> tokens) {
		// Cria a pilha e a fila
		this.pilha = new ArrayList<>();
		this.fila = new ArrayList<>();
		this.tokens = tokens;
	}

	public String iniciarDescendentePreditivo() {
		this.preencheFila();
		this.empilhaValoresIniciais();

		int topoDaPilha;
		int proximaEntrada;

		int valorMatrizDeParsing;

		while (!pilhaVazia()) {
			topoDaPilha = this.getTopoDaPilha();
			proximaEntrada = this.getPrimeiroDaFila();

			if (isTerminal(topoDaPilha) || pilhaVazia()) {
				if (topoDaPilha == proximaEntrada) {
					this.retiraTopoDaPilha();
					this.retiraPrimeiroDaFila();
				} else {
					return erro(topoDaPilha);
				}
			} else { // não é terminal
				valorMatrizDeParsing = this.getValorMatrizDeParsing(topoDaPilha, proximaEntrada);
				if (valorMatrizDeParsing != -1) {
					this.retiraTopoDaPilha();
					this.empilhaProducoesOrdemDescrescente(valorMatrizDeParsing);
				} else {
					return erro(topoDaPilha);
				}
			}
		}
		return "";
	}

	private void empilhaValoresIniciais() {
		this.pilha.add(Constants.DOLLAR);
		this.pilha.add(Constants.START_SYMBOL);
	}

	private int getValorMatrizDeParsing(int topoDaPilha, int proximaEntrada) {
		return Constants.PARSER_TABLE[topoDaPilha - Constants.FIRST_NON_TERMINAL][proximaEntrada - 1];
	}

	private void empilhaProducoesOrdemDescrescente(int valorMatrizDeParsing) {
		for (int i = Constants.PRODUCTIONS[valorMatrizDeParsing].length - 1; i >= 0; i--) {
			if (Constants.PRODUCTIONS[valorMatrizDeParsing][i] != 0) {
				this.pilha.add(Constants.PRODUCTIONS[valorMatrizDeParsing][i]);
			}
		}
	}

	private String erro(int topoDaPilha) {
		return "ERRO SINTÁTICO: " + Constants.PARSER_ERROR[topoDaPilha] + " na linha "
				+ this.tokens.get(this.tokens.size() - this.fila.size()).getLinha();
	}

	private int getTopoDaPilha() {
		return this.pilha.get(this.pilha.size() - 1);
	}

	private int getPrimeiroDaFila() {
		return this.fila.get(0);
	}

	private void retiraPrimeiroDaFila() {
		this.fila.remove(0);
	}

	private void retiraTopoDaPilha() {
		this.pilha.remove(pilha.size() - 1);
	}

	private boolean isTerminal(int topoDaPilha) {
		return topoDaPilha < Constants.FIRST_NON_TERMINAL;
	}

	private boolean pilhaVazia() {
		return this.getTopoDaPilha() == Constants.DOLLAR;
	}

	private void preencheFila() {
		this.fila = this.tokens.stream().map(Token::getCodigo).collect(Collectors.toList());
		for (Integer f : fila)
			System.out.println(f);
	}
}
