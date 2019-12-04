package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import constants.Constants;
import enums.CategoriaSimboloEnum;
import enums.ContextoEnum;
import enums.InstrucaoEnum;
import enums.TokenEnum;
import exceptions.AnalisadorSemanticoException;
import exceptions.SimboloNaoEncontradoException;
import hipotetica.AreaInstrucoes;
import hipotetica.AreaLiterais;
import hipotetica.Hipotetica;
import hipotetica.Tipos;
import models.Literal;
import models.Simbolo;
import models.Token;

public class AnalisadorSemantico {

	private Hipotetica maquinaVirtual;
	private TabelaDeSimbolos tabelaDeSimbolos;
	private AreaInstrucoes areaInstrucoes;
	private AreaLiterais areaLiterais;
	
	private LinkedList<Integer> pilhaIf;
	private LinkedList<Integer> pilhaWhile;
	private LinkedList<Integer> pilhaRepeat;
	private LinkedList<Integer> pilhaProcedure;
	private LinkedList<Integer> pilhaCase;
	private LinkedList<Integer> pilhaFor;
	private LinkedList<Simbolo> pilhaSimbolo;
	
	private Integer nivelAtual;
	private Integer numeroVariaveis;
	private Integer numeroParametros;
	private Integer deslocamento;
	private Integer ponteiroLit; 
	private CategoriaSimboloEnum tipoIdentificador;
	private ContextoEnum contexto;
	private Simbolo variavelDeAtribuicao;
	private Simbolo novaConstante;
	private Simbolo novaProcedure;
	private boolean temParametro;
	
	// Vari�vel auxiliar utilizada pelo FOR
	private Integer geralASimbolo;
	
	private Integer nivelSimbolo;
	
	public AnalisadorSemantico() {
		this.maquinaVirtual = new Hipotetica();
		this.areaInstrucoes = new AreaInstrucoes();
		this.areaLiterais = new AreaLiterais();
	}
	
	public void interpretarMaquinaVirtual() {
		System.out.println("M�quina Virtual interpretada");
		Hipotetica.Interpreta(this.areaInstrucoes, this.areaLiterais);
	}
	
	public void executarSemantico(int codigoDaAcaoSemantica, Token tokenAnterior) {
		
		// Vari�veis auxiliares utilizadas nos cases
		int enderecoDSVS;
		int enderecoDSVF;
		int diferencaDeNivel;
		int valorDecimal = 0;
		
		if (tokenAnterior.getCodigo() == TokenEnum.ID.getCod()) {
			tokenAnterior.setToken(tokenAnterior.getToken().toLowerCase());
		}
		
		switch (codigoDaAcaoSemantica) {
		
//			Reconhecendo o nome do programa
			case 100:
				inicializaPilhas();
				this.tabelaDeSimbolos = new TabelaDeSimbolos(25147);
				Hipotetica.InicializaAI(this.areaInstrucoes);
				Hipotetica.InicializaAL(this.areaLiterais);
				inicializaVariaveis();
				break;
				
//			Final de programa
			case 101:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.PARA.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Ap�s declara��o de vari�vel
			case 102:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.AMEM.getCodigo(), Constants.VAZIO, numeroVariaveis + deslocamento);
				break;
				
//			Encontrado o nome de r�tulo, de vari�vel, ou de par�metro de procedure em declara��o
			case 104:
				if (tabelaDeSimbolos.existe(tokenAnterior.getToken(), nivelAtual)) {
					throw new AnalisadorSemanticoException(
							String.format("Erro sem�ntico na linha %s: o simbolo %s j� foi declarado",
									tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				}
				if (tipoIdentificador.equals(CategoriaSimboloEnum.VARIAVEL)) {
					tabelaDeSimbolos.inserir(new Simbolo(tokenAnterior.getToken(), CategoriaSimboloEnum.VARIAVEL, nivelAtual, deslocamento + numeroVariaveis, null));
					numeroVariaveis++;
				} else if (tipoIdentificador.equals(CategoriaSimboloEnum.PARAMETRO)) {
					Simbolo simboloAtual = new Simbolo(tokenAnterior.getToken(), CategoriaSimboloEnum.PARAMETRO, nivelAtual, deslocamento + numeroParametros, null);		
					tabelaDeSimbolos.inserir(simboloAtual);
					this.pilhaSimbolo.push(simboloAtual);
					numeroParametros++;
				}
				break;

//			Reconhecido nome de constante em declara��o 
			case 105:
				if (tabelaDeSimbolos.existe(tokenAnterior.getToken(), nivelAtual)) {	
					throw new AnalisadorSemanticoException(
						String.format("Erro sem�ntico na linha %s: o simbolo %s j� foi declarado",
								tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				} else {
					novaConstante = new Simbolo(tokenAnterior.getToken(), CategoriaSimboloEnum.CONSTANTE, nivelAtual, null, null);
					tabelaDeSimbolos.inserir(novaConstante);
				}
				break;
		
// 			Reconhecido valor de constante em declara��o
			case 106:
				novaConstante.setGeralA(Integer.parseInt(tokenAnterior.getToken()));
				break;
						
//			Antes de lista de identificadores em declara��o de vari�veis	
			case 107:
				tipoIdentificador = CategoriaSimboloEnum.VARIAVEL;
				break;
				
//			Ap�s nome de procedure, em declara��o 		
			case 108:
				novaProcedure = new Simbolo(tokenAnterior.getToken(), CategoriaSimboloEnum.PROCEDURE, nivelAtual, maquinaVirtual.enderecoProximaInstrucao + 1, 0);
				tabelaDeSimbolos.inserir(novaProcedure);

				nivelAtual++;
				temParametro = false;
				numeroParametros = 0;
				numeroVariaveis = 0;
				break;
				
//			Ap�s declara��o de procedure	
			case 109:
				if (temParametro) {
					tabelaDeSimbolos.atualizar(novaProcedure.getNome(), novaProcedure.getNivel(), novaProcedure.getGeralA(), numeroParametros);
					
					// Adicionando cada parametro ao deslocamento
					for (int i = 0; i < numeroParametros; i++) {
						Simbolo parametroAtual = this.pilhaSimbolo.removeLast();			
						parametroAtual.setGeralA(-(numeroParametros - i));
					}
				}
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVS.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaProcedure.push(maquinaVirtual.enderecoProximaInstrucao - 1);
				this.pilhaProcedure.push(numeroParametros);
				numeroParametros = 0;
				break;

// 			Fim de procedure 
			case 110:
				int parametros = this.pilhaProcedure.pop();
				int endereco = this.pilhaProcedure.pop();
				
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.RETU.getCodigo(), Constants.VAZIO, parametros);
				maquinaVirtual.AlterarAI(this.areaInstrucoes, endereco, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao);

				tabelaDeSimbolos.excluirPorNivel(nivelAtual);
				nivelAtual--;
				break;

//			Antes de par�metros formais de procedures 		
			case 111:
				tipoIdentificador = CategoriaSimboloEnum.PARAMETRO;
				temParametro = true;
				break;
				
//			Atribui��o parte esquerda 				
			case 114:
				try {					
					Simbolo simbolo = tabelaDeSimbolos.buscar(tokenAnterior.getToken());
					if (simbolo.getCategoria() != CategoriaSimboloEnum.VARIAVEL) {
						throw new AnalisadorSemanticoException(
								String.format("Erro sem�ntico na linha %s: o simbolo %s n�o � uma vari�vel",
										tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
					} else {
						variavelDeAtribuicao = simbolo;
					}
				} catch (SimboloNaoEncontradoException e) {
					   String.format("Erro sem�ntico na linha %s: o simbolo %s n�o existe",
					     tokenAnterior.getLinha().toString(), tokenAnterior.getToken());
				}
				break;

// 			Ap�s express�o em atribui��o
			case 115:
				diferencaDeNivel = nivelAtual - variavelDeAtribuicao.getNivel();
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.ARMZ.getCodigo(), diferencaDeNivel, variavelDeAtribuicao.getGeralA());
				break;
			
//			Chamada de procedure
			case 116:
				try {
					Simbolo simboloAtual = tabelaDeSimbolos.buscar(tokenAnterior.getToken());
					
					if (simboloAtual.getCategoria() == CategoriaSimboloEnum.PROCEDURE) {
						novaProcedure = simboloAtual;
					} else {
						throw new AnalisadorSemanticoException(
								String.format("Erro sem�ntico na linha %s: o simbolo %s n�o � uma procedure",
										tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
					}
					numeroParametros = 0;
				} catch (SimboloNaoEncontradoException e) {
					throw new AnalisadorSemanticoException(
							String.format("Erro sem�ntico na linha %s: o simbolo %s n�o foi declarado",
									tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				}
				break;
				
//			Ap�s comando call
			case 117:
				if (novaProcedure.getGeralB() != numeroParametros) {
					throw new AnalisadorSemanticoException(
							String.format("Erro sem�ntico na linha %s: o n�mero de par�metros � inv�lido",
									tokenAnterior.getLinha().toString()));
				} else {
					diferencaDeNivel = nivelAtual - novaProcedure.getNivel();
					maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CALL.getCodigo(), diferencaDeNivel, novaProcedure.getGeralA());
				}
				break;

//			Ap�s express�o, em comando call
			case 118:
				numeroParametros++;
				break;
				
//			Ap�s express�o num comando IF	
			case 120:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVF.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaIf.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o da instru��o acima
				break;
			
//			Ap�s instru��o IF
			case 121:
				enderecoDSVS = this.pilhaIf.peek();
				maquinaVirtual.AlterarAI(this.areaInstrucoes, enderecoDSVS, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao); // LC
				break;
			
//			Ap�s dom�nio do THEN, antes do ELSE
			case 122:
				enderecoDSVF = this.pilhaIf.peek();
				maquinaVirtual.AlterarAI(this.areaInstrucoes, enderecoDSVF, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao + 1); // LC + 1
				
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVS.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaIf.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o da instru��o acima
				break;

//			Comando WHILE antes da express�o
			case 123:
				this.pilhaWhile.push(maquinaVirtual.enderecoProximaInstrucao);
				break;
			
//			Comando WHILE depois da express�o
			case 124:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVF.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaWhile.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o da instru��o acima
				break;
				
//			Ap�s comando WHILE
			case 125:
				enderecoDSVF = this.pilhaWhile.pop();
				maquinaVirtual.AlterarAI(this.areaInstrucoes, enderecoDSVF, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao + 1); // LC + 1
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVS.getCodigo(), Constants.VAZIO, this.pilhaWhile.peek());
				break;
				
//			Comando REPEAT - in�cio
			case 126:
				this.pilhaRepeat.push(maquinaVirtual.enderecoProximaInstrucao);
				break;
				
//			Comando REPEAT - fim
			case 127:
				enderecoDSVF = this.pilhaRepeat.peek();
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVF.getCodigo(), Constants.VAZIO, enderecoDSVF);
				break;
					
//			Comando READLN in�cio
			case 128:
				contexto = ContextoEnum.READLN;
				break;
					
//			Identificador de vari�vel
			case 129:
				try {
					Simbolo simbolo = tabelaDeSimbolos.buscar(tokenAnterior.getToken());
					
					if (contexto == ContextoEnum.READLN) {
						maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.LEIT.getCodigo(), Constants.VAZIO, Constants.VAZIO);
						int deslocamentoDoToken = simbolo.getGeralA();
						diferencaDeNivel = simbolo.getNivel() - nivelAtual;
						maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.ARMZ.getCodigo(), diferencaDeNivel, deslocamentoDoToken);
					} else if (contexto == ContextoEnum.EXPRESSAO) {
						if (simbolo.getCategoria() == CategoriaSimboloEnum.PROCEDURE) {
							throw new AnalisadorSemanticoException(
									String.format("Erro sem�ntico na linha %s: o simbolo %s � um Procedure",
											tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
						} else if (simbolo.getCategoria() == CategoriaSimboloEnum.CONSTANTE){ 
							valorDecimal = simbolo.getGeralA();
							maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, valorDecimal);
						} else {
							int deslocamentoDoToken = simbolo.getGeralA();
							diferencaDeNivel = nivelAtual - simbolo.getNivel();
							maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRVL.getCodigo(), diferencaDeNivel, deslocamentoDoToken);
						}
					}
				} catch (SimboloNaoEncontradoException e) {
					throw new AnalisadorSemanticoException(
							String.format("Erro sem�ntico na linha %s: o simbolo %s n�o foi declarado",
									tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				}
				break;
					
//			WRITELN - ap�s literal na instru��o WRITELN
			case 130:
				maquinaVirtual.IncluirAL(this.areaLiterais, tokenAnterior.getToken());
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.IMPRL.getCodigo(), Constants.VAZIO, ponteiroLit);
				ponteiroLit++;
				break;
				
//			WRITELN ap�s express�o
			case 131:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.IMPR.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;

//			Ap�s palavra reservada CASE
			case 132:
				this.pilhaCase.push(-1);
				break;

//			Ap�s comando CASE
			case 133:
				enderecoDSVS = this.pilhaCase.pop();
				while (enderecoDSVS != -1) {
					maquinaVirtual.AlterarAI(this.areaInstrucoes, enderecoDSVS, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao); // LC
					enderecoDSVS = this.pilhaCase.pop();
				}
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.AMEM.getCodigo(), Constants.VAZIO, -1);
				break;
	
// 			Ramo do CASE ap�s inteiro, �ltimo da lista 
			case 134:
				valorDecimal = Integer.parseInt(tokenAnterior.getToken());
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.COPI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, valorDecimal);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMIG.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				
				resolverPendenciasDSVT(maquinaVirtual.enderecoProximaInstrucao + 1);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVF.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaCase.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o instru��o acima
				break;
				
//	 		Ap�s comando em CASE
			case 135:
				enderecoDSVF = this.pilhaCase.pop();
				maquinaVirtual.AlterarAI(this.areaInstrucoes, enderecoDSVF, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao + 1); // LC + 1
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVS.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaCase.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o instru��o acima
				break;
			
//		 	Ramo do CASE: ap�s inteiro 
			case 136:
				valorDecimal = Integer.parseInt(tokenAnterior.getToken());
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.COPI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, valorDecimal);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMIG.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVT.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaCase.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o instru��o acima
				break;
				
//			Ap�s vari�vel controle comando FOR
			case 137:
				try {
					Simbolo simbolo = tabelaDeSimbolos.buscar(tokenAnterior.getToken());
					if (simbolo.getCategoria() != CategoriaSimboloEnum.VARIAVEL) {
						throw new AnalisadorSemanticoException(
								String.format("Erro sem�ntico na linha %s: o simbolo %s n�o � uma vari�vel",
										tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
					}
					this.geralASimbolo = simbolo.getGeralA();
					this.nivelSimbolo = simbolo.getNivel();
				} catch (SimboloNaoEncontradoException e) {
					throw new AnalisadorSemanticoException(
							String.format("Erro sem�ntico na linha %s: o simbolo %s n�o foi declarado",
									tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				}
				break;
			
//			Ap�s express�o - valor inicial
			case 138:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.ARMZ.getCodigo(), this.nivelSimbolo, this.geralASimbolo);
				break;
				
//			Ap�s express�o - valor final
			case 139:
				diferencaDeNivel = this.nivelSimbolo - nivelAtual;
				this.pilhaFor.push(maquinaVirtual.enderecoProximaInstrucao);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.COPI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRVL.getCodigo(), diferencaDeNivel, this.geralASimbolo);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMAI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVF.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				this.pilhaFor.push(maquinaVirtual.enderecoProximaInstrucao - 1); // endere�o instru��o acima
				break;
				
//			Ap�s comando em FOR
			case 140:
				diferencaDeNivel = this.nivelSimbolo - nivelAtual;
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRVL.getCodigo(), diferencaDeNivel, this.geralASimbolo);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, 1);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.SOMA.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.ARMZ.getCodigo(), diferencaDeNivel, this.geralASimbolo);
				
				enderecoDSVF = this.pilhaFor.pop();
				maquinaVirtual.AlterarAI(this.areaInstrucoes, enderecoDSVF, Constants.VAZIO, maquinaVirtual.enderecoProximaInstrucao + 1); // LC + 1
				
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DSVS.getCodigo(), Constants.VAZIO, this.pilhaFor.peek());
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.AMEM.getCodigo(), Constants.VAZIO, -1);
				break;
				
//			Compara��o - igual
			case 141:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMIG.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
			
//			Compara��o - menor
			case 142:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMME.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Compara��o - maior
			case 143:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMMA.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
			
//			Compara��o - maior ou igual
			case 144:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMAI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Compara��o - menor ou igual
			case 145:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMEI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Compara��o - diferente
			case 146:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CMDF.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o - operando com sinal un�rio
			case 147:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.INVR.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o � soma
			case 148:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.SOMA.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o � subtra��o
			case 149:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.SUBT.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o - or
			case 150:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DISJ.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o � multiplica��o
			case 151:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.MULT.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o � divis�o
			case 152:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.DIVI.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o - and
			case 153:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CONJ.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o � inteiro
			case 154:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, Integer.parseInt(tokenAnterior.getToken()));
				break;
			
//			Expresso - not
			case 155:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.NEGA.getCodigo(), Constants.VAZIO, Constants.VAZIO);
				break;
				
//			Express�o - vari�vel
			case 156:
				contexto = ContextoEnum.EXPRESSAO;
				break;
				
			default:
				System.out.println("A��o Sem�ntica n�mero " + codigoDaAcaoSemantica + " n�o implementada");
		}
	}
	
	public List<Tipos> obterInstrucoes() {
		List<Tipos> lista = Arrays.asList(this.areaInstrucoes.AI).stream().filter(item -> item.codigo != -1).collect(Collectors.toList());
		int endereco = 0;
		for (Tipos instrucao : lista) {
			instrucao.endereco = endereco;
			endereco++;
		}
		return lista;
	}
	
	public List<Literal> obterLiterais() {
		List<String> lista = Arrays.asList(this.areaLiterais.AL).stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
		List<Literal> literais = new ArrayList<>();
		int endereco = 0;
		for (String literal : lista) {
			literais.add(new Literal(endereco, literal));
			endereco++;
		}
		return literais;
	}
	
	private void resolverPendenciasDSVT(int enderecoDSVT) {
		Iterator<Integer> pilhaCaseIterator = this.pilhaCase.iterator();
		while (pilhaCaseIterator.hasNext()) {
			int endereco = pilhaCaseIterator.next();
			if (endereco != -1 && getInstrucaoByEndereco(endereco).codigo == InstrucaoEnum.DSVT.getCodigo()) {
				maquinaVirtual.AlterarAI(this.areaInstrucoes, endereco, Constants.VAZIO, enderecoDSVT);
				pilhaCaseIterator.remove();
			}
		}
	}
	
	private Tipos getInstrucaoByEndereco(int endereco) {
		return this.obterInstrucoes().stream().filter(instrucao -> instrucao.endereco == endereco).findFirst().orElse(null);
	}
	
	private void inicializaPilhas() {
		this.pilhaIf = new LinkedList<>();
		this.pilhaWhile = new LinkedList<>();
		this.pilhaRepeat = new LinkedList<>();
		this.pilhaProcedure = new LinkedList<>();
		this.pilhaCase = new LinkedList<>();
		this.pilhaFor = new LinkedList<>();
		this.pilhaSimbolo = new LinkedList<>();
	}
	
	private void inicializaVariaveis() {
		this.nivelAtual = 0;
		this.numeroVariaveis = 0;
		this.numeroParametros = 0;
		this.deslocamento = 3;
		this.ponteiroLit = 0;
	}
}
