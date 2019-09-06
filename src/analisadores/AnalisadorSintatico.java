package analisadores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import constants.Constants;
import enums.TokenEnum;
import exceptions.AnalisadorSintaticoException;
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

	public void iniciarDescendentePreditivo() {
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
				} else { // topo da pilha não é igual ao simbolo da entrada atual
					this.lancaErro();
				}
			} else { // não é terminal
				valorMatrizDeParsing = this.getValorMatrizDeParsing(topoDaPilha, proximaEntrada);
				if (valorMatrizDeParsing != -1) {
					this.retiraTopoDaPilha();
					this.empilhaProducoesOrdemDescrescente(valorMatrizDeParsing);
				} else { // valor retornado da matriz de parsing é -1 , então é erro
					this.lancaErro();
				}
			}
		}
	}

	private void empilhaValoresIniciais() {
		this.pilha.add(DOLLAR);
		this.pilha.add(START_SYMBOL);
	}

	private int getValorMatrizDeParsing(int topoDaPilha, int proximaEntrada) {
		return PARSER_TABLE[topoDaPilha - FIRST_NON_TERMINAL][proximaEntrada - 1];
	}

	private void empilhaProducoesOrdemDescrescente(int valorMatrizDeParsing) {
		for (int i = PRODUCTIONS[valorMatrizDeParsing].length - 1; i >= 0; i--) {
			if (PRODUCTIONS[valorMatrizDeParsing][i] != 0) {
				this.pilha.add(PRODUCTIONS[valorMatrizDeParsing][i]);
			}
		}
	}

	private void lancaErro() {
		if(this.getTopoDaPilha() < FIRST_NON_TERMINAL) {
			throw new AnalisadorSintaticoException(PARSER_ERROR[this.getTopoDaPilha()], this.getLinhaDoErro());
		} else { // Caso erro n�o seja um terminal avaliar as colunas da Matriz de Parsing
			StringBuilder mensagemDeErro = new StringBuilder("Era esperado o(s) seguinte(s) token(s):");
			for(int i = 0; i < PARSER_TABLE[this.getTopoDaPilha() - FIRST_NON_TERMINAL].length; i++) {
				if(PARSER_TABLE[this.getTopoDaPilha() - FIRST_NON_TERMINAL][i] != -1) {
					final int codigoTokenEsperado = i + 1;
					TokenEnum tokenEnum = Arrays.asList(TokenEnum.values()).stream().filter(value -> value.getCod() == codigoTokenEsperado).findFirst().orElse(null);
					if (Objects.nonNull(tokenEnum)) mensagemDeErro.append(String.format(" %s", tokenEnum.getSimbolo()));
				}
			}
			throw new AnalisadorSintaticoException(mensagemDeErro.toString(), this.getLinhaDoErro());
		}
	}
	
	private int getLinhaDoErro() {
		return this.tokens.get(this.tokens.size() - this.fila.size()).getLinha();
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
		return topoDaPilha < FIRST_NON_TERMINAL;
	}

	private boolean pilhaVazia() {
		return this.getTopoDaPilha() == DOLLAR;
	}

	private void preencheFila() {
		this.fila = this.tokens.stream().map(Token::getCodigo).collect(Collectors.toList());
	}
}
