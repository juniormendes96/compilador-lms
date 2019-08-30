package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;import java.util.List;
import java.util.ResourceBundle;

import analisadores.AnalisadorLexico;
import analisadores.AnalisadorSintatico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
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
	private TabPane tabPane;
	@FXML
	private Tab tabResultado;

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
			
			if (resultadoLexico.getErros().isEmpty()) {
				AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(resultadoLexico.getTokens());
				resultadoSintatico = analisadorSintatico.iniciarDescendentePreditivo();
				if (resultadoSintatico.isEmpty()) {
					printMensagemSucesso();
				} else {
					printErroAnalisadorSintatico(resultadoSintatico);
				}
			} else {
				printErrosAnalisadorLexico(resultadoLexico.getErros());
			}
			
			populaTabela(resultadoLexico.getTokens());
		} else {
			exibeMsg("Insira um algoritmo", 
					"Antes de compilar é necessário inserir um algoritmo para análise", 
					"Não foi possível compilar o algoritmo.", 
					AlertType.WARNING);
		}
	}

	@FXML
	public void abrirArquivo() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto","*.txt")); 	
		fileChooser.setInitialDirectory(new File("C:/"));
		File textoSelec = fileChooser.showOpenDialog(null);
		
		
		BufferedReader reader = new BufferedReader(new FileReader (textoSelec));
		
		try {
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			
			while (line != null) {
				sb.append(line);
		        sb.append("\n");
		        line = reader.readLine();
		    }
			System.out.println("Arquivo lido com sucesso "+textoSelec.getName());
		    txtAreaAlgoritmo.setText(sb.toString());
		   
		} finally {
			reader.close();
		}
	}
	
	private void populaTabela(List<Token> linhasTabela) {
		tableViewResultado.setItems(FXCollections.observableArrayList(linhasTabela));
	}
	
	private void setStyles(boolean erro) {
		if (erro) {
			tabResultado.setStyle("-fx-text-fill: red;");
			textAreaErros.setStyle("-fx-text-inner-color: red;");
		} else {
			tabResultado.setStyle("-fx-text-fill: green;");
			textAreaErros.setStyle("-fx-text-inner-color: green;");
		}
	}
	
	private void printErrosAnalisadorLexico(List<Erro> erros) {
		setStyles(true);
		erros.stream().forEach(erro -> textAreaErros.setText(textAreaErros.getText() + erro.getMensagem() + "\n"));
		tabPane.getSelectionModel().select(tabResultado);
	}
	
	private void printErroAnalisadorSintatico(String erro) {
		setStyles(true);
		textAreaErros.setText(erro);
		tabPane.getSelectionModel().select(tabResultado);
	}
	
	private void printMensagemSucesso() {
		setStyles(false);
		textAreaErros.setText("Código sem erro. Compilado com sucesso!");
		tabPane.getSelectionModel().select(tabResultado);
	}
	
	private void exibeMsg(String titulo, String cabecalho, String msg, AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecalho);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
}
