package core;

import java.util.ArrayList;
import java.util.List;

import enums.InstrucaoEnum;

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
	private Integer[] escopo = new Integer[100]; // Verificar posteriormente o tamanho desse vetor e o propósito dele
	
	
	public AnalisadorSemantico() {
		this.maquinaVirtual = new Hipotetica();
		this.areaInstrucoes = new AreaInstrucoes();
		this.areaLiterais = new AreaLiterais();
	}
	
	public void executarSemantico(int codigoDaAcaoSemantica) {
		switch (codigoDaAcaoSemantica) {
			case 100:
				inicializaPilhas();
				this.tabelaDeSimbolos = new TabelaDeSimbolos(25147);
				Hipotetica.InicializaAI(this.areaInstrucoes);
				Hipotetica.InicializaAL(this.areaLiterais);
				inicializaVariaveis();
				break;
			case 102:
				maquinaVirtual.IncluirAI(this.areaInstrucoes, InstrucaoEnum.AMEM.getCodigo(), 0, numeroVariaveisBloco + 3);
				break;
//			case 130:
//				break;
			default:
				System.out.println("Ação Semântica número " + codigoDaAcaoSemantica + " não implementada");
		}
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
	}
}
