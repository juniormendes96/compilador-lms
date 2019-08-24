package enums;

public enum ErroEnum {
	
	INTEIRO_FORA_DO_LIMITE("ERRO LÉXICO: INTEIRO FORA DO LIMITE - Número inteiro é maior que 32767 ou menor que -32767"), 
	CARACTERES_FORA_DO_LIMITE("ERRO LÉXICO: CARACTERES FORA DO LIMITE - Sequência de caracteres acima de 255 de caracteres"),
	IDENTIFICADOR_FORA_DO_LIMITE("ERRO LÉXICO: IDENTIFICADOR FORA DO LIMITE - Variável deve possuir no máximo 30 caracteres"),
	IDENTIFICADOR_DECLARADO_ERRADO("ERRO LÉXICO: IDENTIFICADOR DECLARADO ERRADO - Variável não pode iniciar com um número");

	private String erro;

	private ErroEnum(String erro) {
		this.erro = erro;
	}

	public String getErro() {
		return erro;
	}

}
