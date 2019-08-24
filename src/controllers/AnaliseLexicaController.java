package controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Erro;
import models.Token;

public class AnaliseLexicaController implements Initializable {

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		columnToken.setCellValueFactory(new PropertyValueFactory<>("token"));
		columnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		columnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
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

}
