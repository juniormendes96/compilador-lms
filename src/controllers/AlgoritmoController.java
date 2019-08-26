package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import analisadores.AnalisadorLexico;
import analisadores.AnalisadorSintatico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Erro;
import models.ResultadoAnalise;
import models.Token;

public class AlgoritmoController implements Initializable {

	@FXML
	private TableView<Token> tableViewResultado;
	@FXML
	private TableColumn<Token, Integer> columnCodigo;
	@FXML
	private TableColumn<Token, String> columnToken;
	@FXML
	private TableColumn<Token, String> columnDescricao;
	@FXML
	private TableColumn<Token, Integer> columnLinha;
	@FXML
	private TextArea textAreaErros;
	@FXML
	private TextArea txtAreaAlgoritmo;
	@FXML
	private Tab tabPane;

	private ResultadoAnalise resultadoLexico;
	private String resultadoSintatico;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		columnToken.setCellValueFactory(new PropertyValueFactory<>("token"));
		columnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		columnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
	}
	
	@FXML
	public void analisarAlgoritmo() {
		
		if(!txtAreaAlgoritmo.getText().trim().isEmpty()) {
			AnalisadorLexico analisadorLexico = new AnalisadorLexico(txtAreaAlgoritmo.getText());
			resultadoLexico = analisadorLexico.iniciarAnalise();
			
			AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(resultadoLexico.getTokens());
			resultadoSintatico = analisadorSintatico.iniciarAnalise();
			
			if(resultadoSintatico != "") 
				resultadoLexico.getErros().add(new Erro(resultadoSintatico));
			
			tabPane.setText("");
			tabPane.setGraphic(new Label("Resultado"));
			tabPane.getGraphic().setStyle("-fx-text-fill: green;");
			
			populaTabela(resultadoLexico.getTokens());
			populaErros(resultadoLexico.getErros());
		} else {
			exibeMsg("Insira um algoritmo", 
					"Antes de compilar é necessário inserir um algoritmo para análise", 
					"Não foi possível compilar o algoritmo.", 
					AlertType.WARNING);
		}
	}

	public void populaTabela(List<Token> linhasTabela) {
		tableViewResultado.setItems(FXCollections.observableArrayList(linhasTabela));
	}

	public void populaErros(List<Erro> erros) {
		if (!erros.isEmpty()) {
			textAreaErros.setStyle("-fx-text-inner-color: red;");
			textAreaErros.setText("");
			erros.stream().forEach(erro -> textAreaErros.setText(textAreaErros.getText() + erro.getMensagem() + "\n"));
		} else {
			textAreaErros.setText("Código sem erro. Compilado com sucesso!");
		}
	}
	
	private void exibeMsg(String titulo, String cabecalho, String msg, AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecalho);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
}
