package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import constants.Constants;
import enums.InstrucaoEnum;
import models.AreaInstrucoes;
import models.AreaLiterais;
import models.Literal;
import models.Tipos;
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
	private Integer numeroVariaveisBloco;
	private Integer deslocamento;
	private Integer ponteiroLit; 
	private Integer[] escopo = new Integer[100]; // Verificar posteriormente o tamanho desse vetor e o propósito dele
	
	
	public AnalisadorSemantico() {
		this.maquinaVirtual = new Hipotetica();
		this.areaInstrucoes = new AreaInstrucoes();
		this.areaLiterais = new AreaLiterais();
	}
	
	public void executarSemantico(int codigoDaAcaoSemantica, Token tokenAnterior) {
		switch (codigoDaAcaoSemantica) {
			case 100:
				inicializaPilhas();
				this.tabelaDeSimbolos = new TabelaDeSimbolos(25147);
				Hipotetica.InicializaAI(this.areaInstrucoes);
				Hipotetica.InicializaAL(this.areaLiterais);
				inicializaVariaveis();
				break;
			case 101:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.PARA.getCodigo(), -1, -1);
				break;
			case 102:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.AMEM.getCodigo(), 0, numeroVariaveisBloco + deslocamento);
				break;
			case 130:
				maquinaVirtual.IncluirAL(this.areaLiterais, tokenAnterior.getToken());
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.IMPRL.getCodigo(), Constants.VAZIO, ponteiroLit);
				ponteiroLit++;
				break;
			default:
				System.out.println("Ação Semântica número " + codigoDaAcaoSemantica + " não implementada");
		}
	}
	
	public List<Tipos> obterInstrucoes() {
		List<Tipos> lista = Arrays.asList(this.areaInstrucoes.AI).stream().filter(item -> item.codigo != -1).collect(Collectors.toList());
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
		this.numeroVariaveisBloco = 0;
		this.deslocamento = 3;
		this.ponteiroLit = 0;
	}
}
