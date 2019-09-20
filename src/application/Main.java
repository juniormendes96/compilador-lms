package application;
import analisadores.GerenciadorTabelaSimbolos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.TabelaDeSimbolos;

/*
* @author Vilmar Mendes Junior e Jonathan Neves
*/
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/views/Algoritmo.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Compilador");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//launch(args);
		GerenciadorTabelaSimbolos TS = new GerenciadorTabelaSimbolos();
		
		//INSERIR 10 ELEMENTOS
		TS.inserir(new TabelaDeSimbolos("var1",1,0,5,0,1));
		TS.inserir(new TabelaDeSimbolos("var",2,1,4,0,2));
		TS.inserir(new TabelaDeSimbolos("conts",3,0,3,1,3));
		TS.inserir(new TabelaDeSimbolos("pro",4,0,7,2,4));
		TS.inserir(new TabelaDeSimbolos("x",1,1,2,0,5));
		TS.inserir(new TabelaDeSimbolos("home",1,0,3,1,6));
		TS.inserir(new TabelaDeSimbolos("hash",2,1,5,1,7));
		TS.inserir(new TabelaDeSimbolos("bit",3,0,4,0,8));
		TS.inserir(new TabelaDeSimbolos("code",4,1,3,2,9));
		TS.inserir(new TabelaDeSimbolos("var2",2,1,5,0,10));
		
		//Mostrar conteúdo da tabela
		TS.visualizarTabelaDeSimbolos();
		
		//ALTERAR 5 ELEMENTOS
		TS.alterar(0, new TabelaDeSimbolos("VARx",2,2,2,2,1));
		TS.alterar(2, new TabelaDeSimbolos("VARy",3,3,0,5,2));
		TS.alterar(3, new TabelaDeSimbolos("VARz",4,4,4,6,3));
		TS.alterar(6, new TabelaDeSimbolos("const1",5,5,0,3,1));
		TS.alterar(7, new TabelaDeSimbolos("add",5,5,0,3,2));
		
		//Mostrar conteúdo da tabela
		TS.visualizarTabelaDeSimbolos();

		//EXCLUIR 3 ELEMENTOS
		TS.excluir(2);
		TS.excluir(4);
		TS.excluir(6);
		
		//Mostrar conteúdo da tabela
		TS.visualizarTabelaDeSimbolos();

		//Buscar 1 elemento inexistente
		TS.buscarElemento("home");
		
		//Buscar 3 elemento por nome
		TS.buscarElemento("VARx");
		TS.buscarElemento("x");
		TS.buscarElemento("add");
	}

}
