package controllers;

import analisadores.AnalisadorLexico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.ResultadoAnalise;

public class AlgoritmoController {

	@FXML
	private TextArea txtAreaAlgoritmo;

	private ResultadoAnalise resultado;

	@FXML
	public void analisarAlgoritmo() {
		AnalisadorLexico analisadorLexico = new AnalisadorLexico(txtAreaAlgoritmo.getText());
		resultado = analisadorLexico.iniciarAnalise();
		abrirResultadoView();
	}

	public void abrirResultadoView() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AnaliseLexica.fxml"));
			Parent root = loader.load();

			AnaliseLexicaController controller = (AnaliseLexicaController) loader.getController();
			controller.populaTabela(resultado.getModels());
			controller.populaErros(resultado.getErros());

			stage.setScene(new Scene(root));
			stage.setTitle("Resultado");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
