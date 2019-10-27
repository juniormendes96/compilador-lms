package hipotetica;

import constants.Constants;
import enums.InstrucaoEnum;

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar as informações 
 * de uma instrução.
 * Esta classe, bem como as classes "AreaInstrucoes", "AreaLiterais"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
public class Tipos{
	public Integer codigo; 
	public Integer op1;
	public Integer op2;
	
	// Atributo criado para mostrar o endereço da instrução na tabela
	public Integer endereco;
	
	// Atributos criados para mostrar o op1 e op2 na tabela, convertendo para hifen caso seja vazio
	public String displayOp1;
	public String displayOp2;
	
  /**
   * Construtor sem parâmetros.
   * Todos os atributos são inicializados com valores padrões.
   */
	Tipos(){
	     codigo=0;
	   	 op1=0;
   	 	 op2=0;
   	}
	
	
	// Getters para mostrar o conteúdo na tabela
	
	public Integer getEndereco() {
		return endereco;
	}

	public Integer getCodigo() {
		return codigo;
	}
	
	public Integer getOp1() {
		return op1;
	}
	
	public Integer getOp2() {
		return op2;
	}
	
	public String getDisplayOp1() {
		return op1 == Constants.VAZIO ? "-" : op1.toString();
	}
	
	public String getDisplayOp2() {
		return op2 == Constants.VAZIO ? "-" : op2.toString();
	}
	
	public String getNome() {
		for (InstrucaoEnum instrucao : InstrucaoEnum.values()) {
			if (this.codigo == instrucao.getCodigo()) {
				return instrucao.name();
			}
		}
		return null;
	}
	
}
