package enums;

public enum TokenEnum {

	RESERVA_CADEIA_VAZIA(0, "", "Delimitador"), 
	FIM_ARQUIVO(1, "$", "Fim de Arquivo"),
	MAIS(2, "+", "Operador (Mais)"), 
	MENOS(3, "-", "Operador (Menos)"), 
	VEZES(4, "*", "Operador (Vezes)"),
	DIVISAO(5, "/", "Operador (Divisão)"),
	IGUAL(6, "=", "Operador (Igual)"),
	MAIOR(7, ">", "Operador (Maior)"),
	MAIOR_OU_IGUAL(8, ">=", "Operador (maior ou igual)"),
	MENOR(9, "<", "Operador (Menor)"),
	MENOR_OU_IGUAL(10, "<=", "Operador (Menor ou igual)"),
	DIFERENTE(11, "<>", "Operador (Diferente)"),
	ATRIBUICAO(12, ":=", "Símbolo especial (Atribuição)"), 
	DOIS_PONTOS(13, ":", "Símbolo especial (Dois Pontos)"), 
	PONTO_E_VIRGULA(14, ";", "Símbolo especial (Ponto e vírgula)"), 
	VIRGULA(15, ",", "Símbolo especial (Vírgula)"), 
	PONTO(16, ".", "Símbolo especial (Ponto)"),
	ABRE_PARENTESES(17, "(", "Símbolo especial (Abre parênteses)"),
	FECHA_PARENTESES(18, ")", "Símbolo especial (Fecha parênteses)"), 
	ID(19, "ID", "Identificador"), 
	INTEIRO(20, "INTEIRO",  "Número Inteiro"),
	LIT(21, "LIT", "Literal"),
	PROGRAM(22, "PROGRAM", "Palavra Reservada"),
	CONST(23, "CONST", "Palavra Reservada"),
	VAR(24, "VAR", "Palavra Reservada"),
	PROCEDURE(25, "PROCEDURE", "Palavra Reservada"),
	BEGIN(26, "BEGIN", "Palavra Reservada"),
	END(27, "END", "Palavra Reservada"),
	INTEGER(28, "INTEGER", "Palavra Reservada"), 
	OF(29, "OF", "Palavra Reservada"),
	CALL(30, "CALL", "Palavra Reservada"), 
	IF(31, "IF", "Palavra Reservada"), 
	THEN(32, "THEN", "Palavra Reservada"),
	ELSE(33, "ELSE", "Palavra Reservada"),
	WHILE(34, "WHILE", "Palavra Reservada"),
	DO(35, "DO", "Palavra Reservada"), 
	REPEAT(36, "REPEAT", "Palavra Reservada"), 
	UNTIL(37, "UNTIL", "Palavra Reservada"), 
	READLN(38, "READLN", "Palavra Reservada"),
	WRITELN(39, "WRITELN", "Palavra Reservada"), 
	OR(40, "OR", "Palavra Reservada"),
	AND(41, "AND", "Palavra Reservada"), 
	NOT(42, "NOT", "Palavra Reservada"),  
	FOR(43, "FOR", "Palavra Reservada"), 
	TO(44, "TO", "Palavra Reservada"),
	CASE(45, "CASE", "Palavra Reservada");

	private int cod;
	private String simbolo;
	private String descricao;

	private TokenEnum(int cod, String simbolo, String descricao) {
		this.cod = cod;
		this.simbolo = simbolo;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
