package core;

public class AnalisadorSemantico {

	private Hipotetica maquinaVirtual;
	
	public AnalisadorSemantico() {
		this.maquinaVirtual = new Hipotetica();
	}
	
	public void executarSemantico(int codigoDaAcaoSemantica) {
		System.out.println("Ação Semântica número #"+codigoDaAcaoSemantica +" executado!");
		
		switch (codigoDaAcaoSemantica) {
			case 100:
				break;
			case 102:
				break;
			case 130:
				break;
		}
	}
}
