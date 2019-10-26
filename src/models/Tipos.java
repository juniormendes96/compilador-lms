package models;

import enums.InstrucaoEnum;

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar as informações 
 * de uma instrução.
 * Esta classe, bem como as classes "AreaInstrucoes", "AreaLiterais"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
public class Tipos{
	public int codigo; 
	public int op1;
	public int op2;
	
	// Atributo criado para mostrar o endereço da instrução na tabela
	public int endereco;
	
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
	
	public String getNome() {
		for (InstrucaoEnum instrucao : InstrucaoEnum.values()) {
			if (this.codigo == instrucao.getCodigo()) {
				return instrucao.name();
			}
		}
		return null;
	}
	
}
