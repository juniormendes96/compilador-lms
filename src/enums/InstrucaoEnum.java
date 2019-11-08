package enums;

public enum InstrucaoEnum {
	
	RETU(1), // retorno de procedure.
	CRVL(2), // carrega valor na pilha.
	CRCT(3), // carrega constante na pilha.
	ARMZ(4), // armazena conteúdo da pilha(topo) no endereço dado.
	SOMA(5), // operação soma com elementos do topo e sub-topo.
	SUBT(6), // operação de subtração.
	MULT(7), // operação de multiplicação.
	DIVI(8), // operação de divisão.
	INVR(9), // inverte sinal.
	NEGA(10), // operação de negação.
	CONJ(11), // operação AND.
	DISJ(12), // operação de OR.
	CMME(13), // compara menor.
	CMMA(14), // compara maior.
	CMIG(15), // compara igual.
	CMDF(16), // compara diferente.
	CMEI(17), // compara menor igual.
	CMAI(18), // compara maior igual.
	DSVS(19), // desviar sempre.
	DSVF(20), // desviar se falso.
	LEIT(21), // leitura.
	IMPR(22), // imprimir topo da pilha.
	IMPRL(23), // imprimir literal extraído da área de literais.
	AMEM(24), // alocar espaço na área de dados.
	CALL(25), // chamada de procedura “a” no nível “l”.
	PARA(26), // finaliza a execução.
	NADA(27), // nada faz, continua a execução.
	COPI(28), // duplica o topo da pilha
	DSVT(29); // desvia se verdadeiro.
	
	private int codigo;
	
	private InstrucaoEnum(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}
}
