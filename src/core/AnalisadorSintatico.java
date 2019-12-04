package core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import constants.Constants;
import enums.TokenEnum;
import exceptions.AnalisadorSintaticoException;
import models.Token;

public class AnalisadorSintatico implements Constants {

	private LinkedList<Integer> pilha;
	private LinkedList<Token> tokens;

	public AnalisadorSintatico(List<Token> tokens) {
		this.pilha = new LinkedList<>();
		this.tokens = new LinkedList<Token>();
		this.tokens.addAll(tokens);
	}
	
	public void iniciarDescendentePreditivo(AnalisadorSemantico analisadorSemantico) {
		this.empilhaValoresIniciais();

		int topoDaPilha;
		int linhaAtual = 1;
		Token tokenAtual;
		Token tokenAnterior = null;

		int valorMatrizDeParsing;
		
		while (!condicaoParada()) {		// Termina quando o topo da pilha for $ ou quando a fila de tokens estiver vazia
			
			topoDaPilha = this.pilha.peekLast();
			tokenAtual = this.tokens.peek();
			
			if (isTerminal(topoDaPilha) || condicaoParada()) {
				if (topoDaPilha == tokenAtual.getCodigo()) {
					this.pilha.removeLast();
					this.tokens.removeFirst();
					tokenAnterior = tokenAtual;
				} else { // Topo da pilha não é igual ao simbolo da entrada atual, entao lança erro
					this.lancaErro(linhaAtual);
				}
			} else if (!isAcaoSemantica(topoDaPilha)){ // Não é terminal e não é ação semântica
				valorMatrizDeParsing = this.getValorMatrizDeParsing(topoDaPilha, tokenAtual.getCodigo());
				if (valorMatrizDeParsing != -1) {	
					this.pilha.removeLast();
					this.empilhaProducoesOrdemDescrescente(valorMatrizDeParsing);
				} else { // Valor retornado da matriz de parsing é -1, então lança erro
					this.lancaErro(linhaAtual);
				}
			} else { // É uma ação semântica
				analisadorSemantico.executarSemantico(getCodigoAcaoSemantica(topoDaPilha), tokenAnterior);
				this.pilha.removeLast();			
			}
			if (tokenAtual.getToken() != TokenEnum.FIM_ARQUIVO.getSimbolo()) {
				linhaAtual = tokenAtual.getLinha();
			}
		}
	}

	private void empilhaValoresIniciais() {
		this.pilha.add(DOLLAR);
		this.pilha.add(START_SYMBOL);
		this.tokens.add(new Token(TokenEnum.FIM_ARQUIVO, null));
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

	private void lancaErro(int linha) {
		if (this.pilha.peekLast() < FIRST_NON_TERMINAL) {
			throw new AnalisadorSintaticoException(PARSER_ERROR[this.pilha.peekLast()], linha);
		}
		// Caso topo da pilha seja um não terminal, avaliar as colunas da Matriz de Parsing
		StringBuilder mensagemDeErro = new StringBuilder("Era esperado o(s) seguinte(s) token(s):");
		for (int i = 0; i < FIRST_NON_TERMINAL - 1; i++) {
			if (PARSER_TABLE[this.pilha.peekLast() - FIRST_NON_TERMINAL][i] != -1) {  // Coleta a linha correspondente ao topo da pilha na matriz de parsing com valores diferentes de -1
				final int codigoTokenEsperado = i + 1;		// O token esperado é baseado na coluna atual
				TokenEnum tokenEnum = Arrays.asList(TokenEnum.values()).stream().filter(value -> value.getCod() == codigoTokenEsperado).findFirst().orElse(null); // Compara o id com tabela de tokens
				if (Objects.nonNull(tokenEnum)) 
					mensagemDeErro.append(String.format(" %s", tokenEnum.getSimbolo()));
			}
		}
		throw new AnalisadorSintaticoException(mensagemDeErro.toString(), linha);
		
	}

	private boolean isTerminal(int topoDaPilha) {
		return topoDaPilha < FIRST_NON_TERMINAL;
	}
	
	private boolean isAcaoSemantica(int topoDaPilha) {
		return topoDaPilha >= FIRST_SEMANTIC_ACTION;
	}

	public int getCodigoAcaoSemantica(int topoDaPilha) {
		return topoDaPilha - FIRST_SEMANTIC_ACTION;
	}
	
	private boolean condicaoParada() {
		return this.pilha.peekLast() == DOLLAR && this.tokens.peek().getCodigo() == DOLLAR;
	}

}
