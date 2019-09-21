package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import core.AnalisadorLexico;
import core.AnalisadorSintatico;
import exceptions.AnalisadorLexicoException;
import exceptions.AnalisadorSintaticoException;
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
			try {
				AnalisadorLexico analisadorLexico = new AnalisadorLexico(txtAreaAlgoritmo.getText());
				List<Token> tokens = analisadorLexico.iniciarAnalise();	
				
				populaTabela(tokens);
				
				AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokens);
				analisadorSintatico.iniciarDescendentePreditivo();			
			
				printMensagemSucesso();
			
			} catch (AnalisadorLexicoException analisadorLexicoException) {
				populaTabela(null);
				printErro(analisadorLexicoException.getMessage());
			} catch (AnalisadorSintaticoException analisadorSintaticoException) {
				printErro(analisadorSintaticoException.getMessage());
			}
			
		} else {
			exibeMsg("Insira um algoritmo", 
					"Antes de compilar � necess�rio inserir um algoritmo para an�lise", 
					"N�o foi poss�vel compilar o algoritmo.", 
					AlertType.WARNING);
		}
	}

	@FXML
	public void abrirArquivo() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto","*.txt")); 	
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
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
		    txtAreaAlgoritmo.setText(sb.toString());
		   
		} finally {
			reader.close();
		}
	}
	
	private void populaTabela(List<Token> linhasTabela) {
		tableViewResultado.setItems(Objects.nonNull(linhasTabela) ? FXCollections.observableArrayList(linhasTabela) : null);
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
	
	private void printErro(String erro) {
		setStyles(true);
		textAreaErros.setText(erro);
		tabPane.getSelectionModel().select(tabResultado);
	}
	
	private void printMensagemSucesso() {
		setStyles(false);
		textAreaErros.setText("C�digo sem erro. Compilado com sucesso!");
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
