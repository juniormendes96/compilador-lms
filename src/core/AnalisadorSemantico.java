package core;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import constants.Constants;
import enums.CategoriaSimboloEnum;
import enums.ContextoEnum;
import enums.InstrucaoEnum;
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
	
	private List<Integer> pilhaIf;
	private List<Integer> pilhaWhile;
	private List<Integer> pilhaRepeat;
	private List<Integer> pilhaProcedure;
	private List<Integer> pilhaCase;
	private List<Integer> pilhaFor;
	
	private Integer nivelAtual;
	private Integer posicaoLivre;
	private Integer numeroVariaveis;
	private Integer numeroParametros;
	private Integer deslocamento;
	private Integer ponteiroLit; 
	private Integer[] escopo = new Integer[100]; // Verificar posteriormente o tamanho desse vetor e o propósito dele
	private CategoriaSimboloEnum tipoIdentificador;
	private ContextoEnum contexto;
	
	public AnalisadorSemantico() {
		this.maquinaVirtual = new Hipotetica();
		this.areaInstrucoes = new AreaInstrucoes();
		this.areaLiterais = new AreaLiterais();
	}
	
	public void interpretarMaquinaVirtual() {
		System.out.println("Máquina Virtual interpretada");
		Hipotetica.Interpreta(this.areaInstrucoes, this.areaLiterais);
	}
	
	public void executarSemantico(int codigoDaAcaoSemantica, Token tokenAnterior) {
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
				
//			Após declaração de variável
			case 102:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.AMEM.getCodigo(), 0, numeroVariaveis + deslocamento);
				break;
				
//			Encontrado o nome de rótulo, de variável, ou de parâmetro de procedure em declaração
			case 104:
				if (tabelaDeSimbolos.existe(tokenAnterior.getToken(), nivelAtual)) {
					throw new AnalisadorSemanticoException(
							String.format("Erro semântico na linha %s: o simbolo %s já foi declarado",
									tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				}
				if (tipoIdentificador.equals(CategoriaSimboloEnum.VARIAVEL)) {
					tabelaDeSimbolos.inserir(new Simbolo(tokenAnterior.getToken(), CategoriaSimboloEnum.VARIAVEL, nivelAtual, deslocamento + numeroVariaveis, null));
					numeroVariaveis++;
				} else if (tipoIdentificador.equals(CategoriaSimboloEnum.PARAMETRO)) {
					tabelaDeSimbolos.inserir(new Simbolo(tokenAnterior.getToken(), CategoriaSimboloEnum.PARAMETRO, nivelAtual, deslocamento + numeroParametros, null));
					numeroParametros++;
				}
				break;
				
//			Antes de lista de identificadores em declaração de variáveis	
			case 107:
				tipoIdentificador = CategoriaSimboloEnum.VARIAVEL;
				break;
				
//			Comando READLN início
			case 128:
				contexto = ContextoEnum.READLN;
				break;
							
//			Identificador de variável
			case 129:
				try {
					Simbolo simbolo = tabelaDeSimbolos.buscar(tokenAnterior.getToken(), nivelAtual);
					if (contexto == ContextoEnum.READLN) {
						maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.LEIT.getCodigo(), Constants.VAZIO, Constants.VAZIO);
						int deslocamentoDoToken = simbolo.getGeralA();
						maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.ARMZ.getCodigo(), nivelAtual, deslocamentoDoToken);
					} else if (contexto == ContextoEnum.EXPRESSAO) {
						if (simbolo.getCategoria() == CategoriaSimboloEnum.PROCEDURE) {
							throw new AnalisadorSemanticoException(
									String.format("Erro semântico na linha %s: o simbolo %s é um Procedure",
											tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
						} else if (simbolo.getCategoria() == CategoriaSimboloEnum.CONSTANTE){ 
							maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, Integer.parseInt(tokenAnterior.getToken()));
						} else {
							int deslocamentoDoToken = simbolo.getGeralA();
							maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.ARMZ.getCodigo(), nivelAtual, deslocamentoDoToken);
						}
					}
				} catch (SimboloNaoEncontradoException e) {
					throw new AnalisadorSemanticoException(
							String.format("Erro semântico na linha %s: o simbolo %s não foi declarado",
									tokenAnterior.getLinha().toString(), tokenAnterior.getToken()));
				}
				break;
					
//			WRITELN - após literal na instrução WRITELN
			case 130:
				maquinaVirtual.IncluirAL(this.areaLiterais, tokenAnterior.getToken());
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.IMPRL.getCodigo(), Constants.VAZIO, ponteiroLit);
				ponteiroLit++;
				break;
			
//			Expressão – inteiro
			case 154:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.CRCT.getCodigo(), Constants.VAZIO, Integer.parseInt(tokenAnterior.getToken()));
				break;
				
			default:
				System.out.println("Ação Semântica número " + codigoDaAcaoSemantica + " não implementada");
		}
	}
	
	public List<Tipos> obterInstrucoes() {
		List<Tipos> lista = Arrays.asList(this.areaInstrucoes.AI).stream().filter(item -> item.codigo != Constants.VAZIO).collect(Collectors.toList());
		int endereco = 1;
		for (Tipos instrucao : lista) {
			instrucao.endereco = endereco;
			endereco++;
		}
		return lista;
	}
	
	public List<Literal> obterLiterais() {
		List<String> lista = Arrays.asList(this.areaLiterais.AL).stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
		List<Literal> literais = new ArrayList<>();
		int endereco = 1;
		for (String literal : lista) {
			literais.add(new Literal(endereco, literal));
			endereco++;
		}
		return literais;
	}
	
	private void inicializaPilhas() {
		this.pilhaIf = new ArrayList<>();
		this.pilhaWhile = new ArrayList<>();
		this.pilhaRepeat = new ArrayList<>();
		this.pilhaProcedure = new ArrayList<>();
		this.pilhaCase = new ArrayList<>();
		this.pilhaFor = new ArrayList<>();
	}
	
	private void inicializaVariaveis() {
		this.nivelAtual = 0;
		this.posicaoLivre = 1;
		this.escopo[0] = 1;
		this.numeroVariaveis = 0;
		this.numeroParametros = 0;
		this.deslocamento = 3;
		this.ponteiroLit = 0;
	}
}
