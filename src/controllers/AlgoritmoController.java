package controllers;

import analisadores.AnalisadorLexico;
import analisadores.AnalisadorSintatico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Erro;
import models.ResultadoAnalise;

public class AlgoritmoController {

	@FXML
	private TextArea txtAreaAlgoritmo;

	private ResultadoAnalise resultadoLexico;
	private String resultadoSintatico;

	@FXML
	public void analisarAlgoritmo() {
		
		AnalisadorLexico analisadorLexico = new AnalisadorLexico(txtAreaAlgoritmo.getText());
		resultadoLexico = analisadorLexico.iniciarAnalise();
		
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(resultadoLexico.getTokens());
		resultadoSintatico = analisadorSintatico.iniciarAnalise();
		
		if(resultadoSintatico != "") resultadoLexico.getErros().add(new Erro(resultadoSintatico));
		
		abrirResultadoView();
	}

	public void abrirResultadoView() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AnaliseLexica.fxml"));
			Parent root = loader.load();

			AnaliseLexicaController controller = (AnaliseLexicaController) loader.getController();
			controller.populaTabela(resultadoLexico.getTokens());
			controller.populaErros(resultadoLexico.getErros());

			stage.setScene(new Scene(root));
			stage.setTitle("Resultado");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
