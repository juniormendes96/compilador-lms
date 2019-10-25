package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import core.AnalisadorSemantico;
import constants.Constants;
import enums.TokenEnum;
import exceptions.AnalisadorSintaticoException;
import models.Token;

public class AnalisadorSintatico implements Constants {

	private List<Integer> pilha;
	private List<Token> tokens;
	private AnalisadorSemantico analisadorSemantico;

	public AnalisadorSintatico(List<Token> tokens) {
		// Cria a pilha e a fila
		this.pilha = new ArrayList<>();
		this.tokens = tokens;
		this.analisadorSemantico = new AnalisadorSemantico();
	}

	public void iniciarDescendentePreditivo() {
		this.empilhaValoresIniciais(); // Empilhando a pilha

		int topoDaPilha;
		Token proximaEntrada;
		Token tokenAnterior = null;

		int valorMatrizDeParsing;

		while (!pilhaVazia() && !this.tokens.isEmpty()) {		// Termina quando o topo da pilha for $ ou quando a fila de tokens estiver vazia
			
			topoDaPilha = this.getTopoDaPilha();
			proximaEntrada = this.getPrimeiroDaFila();
			
			System.out.println("Topo da pilha: " + topoDaPilha);
			if (isTerminal(topoDaPilha) || pilhaVazia()) {
				if (topoDaPilha == proximaEntrada.getCodigo()) {
					this.retiraTopoDaPilha();
					this.retiraPrimeiroDaFila();
				} else { // Topo da pilha não é igual ao simbolo da entrada atual, entao lança erro
					this.lancaErro();
				}
			} else if (!isAcaoSemantica(topoDaPilha)){ // Não é terminal e não é ação semântica
				valorMatrizDeParsing = this.getValorMatrizDeParsing(topoDaPilha, proximaEntrada.getCodigo());
				if (valorMatrizDeParsing != -1) {	
					this.retiraTopoDaPilha();
					this.empilhaProducoesOrdemDescrescente(valorMatrizDeParsing);
				} else { // Valor retornado da matriz de parsing é -1, então lança erro
					this.lancaErro();
				}
			} else { // é uma ação semântica
				this.analisadorSemantico.executarSemantico(codigoDaAcaoSemantica(topoDaPilha), tokenAnterior);
				this.retiraTopoDaPilha();			
			}
			tokenAnterior = proximaEntrada;
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
		if(this.getTopoDaPilha() < FIRST_NON_TERMINAL) { // Caso topo da pilha seja terminal, lança o erro com token do topo da pilha
			throw new AnalisadorSintaticoException(PARSER_ERROR[this.getTopoDaPilha()], this.getLinhaDoErro());
		} else { // Caso topo da pilha seja um não terminal, avaliar as colunas da Matriz de Parsing
			StringBuilder mensagemDeErro = new StringBuilder("Era esperado o(s) seguinte(s) token(s):");
			for(int i = 0; i < FIRST_NON_TERMINAL - 1; i++) {
				if(PARSER_TABLE[this.getTopoDaPilha() - FIRST_NON_TERMINAL][i] != -1) {  // Coleta a linha correspondente ao topo da pilha na matriz de parsing com valores diferentes de -1
					final int codigoTokenEsperado = i + 1;		// O token esperado é baseado na coluna atual
					TokenEnum tokenEnum = Arrays.asList(TokenEnum.values()).stream().filter(value -> value.getCod() == codigoTokenEsperado).findFirst().orElse(null); // Compara o id com tabela de tokens
					if (Objects.nonNull(tokenEnum)) 
						mensagemDeErro.append(String.format(" %s", tokenEnum.getSimbolo()));	// Cria a mensagem de erro para ser enviada
				}
			}
			throw new AnalisadorSintaticoException(mensagemDeErro.toString(), this.getLinhaDoErro());
		}
	}
	
	private int getLinhaDoErro() {
		return this.tokens.get(this.tokens.size() - this.tokens.size()).getLinha();
	}
	
	private int getTopoDaPilha() {
		return this.pilha.get(this.pilha.size() - 1);
	}

	private Token getPrimeiroDaFila() {
		return this.tokens.get(0);
	}

	private void retiraPrimeiroDaFila() {
		this.tokens.remove(0);
	}

	private void retiraTopoDaPilha() {
		this.pilha.remove(pilha.size() - 1);
	}

	private boolean isTerminal(int topoDaPilha) {
		return topoDaPilha < FIRST_NON_TERMINAL;
	}
	
	
	private boolean isAcaoSemantica(int topoDaPilha) {
		return topoDaPilha >= FIRST_SEMANTIC_ACTION;
	}

	public int codigoDaAcaoSemantica(int topoDaPilha) {
		return topoDaPilha - FIRST_SEMANTIC_ACTION;
	}
	
	private boolean pilhaVazia() {
		return this.getTopoDaPilha() == DOLLAR;
	}

}
