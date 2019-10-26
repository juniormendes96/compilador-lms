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
import core.AnalisadorSemantico;
import core.AnalisadorSintatico;
import exceptions.AnalisadorLexicoException;
import exceptions.AnalisadorSintaticoException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import models.Tipos;
import models.Token;

public class AlgoritmoController implements Initializable {

	/* TABELA LEXICO */
	@FXML
	private TableView<Token> tableViewTokens;
	@FXML
	private TableColumn<Token, Integer> columnCodigo;
	@FXML
	private TableColumn<Token, String> columnToken;
	@FXML
	private TableColumn<Token, String> columnDescricao;
	@FXML
	private TableColumn<Token, Integer> columnLinha;
	
	/* TABELA CODIGO INTERMEDIARIO */
	@FXML
	private TableView<Tipos> tableViewCodigoIntermediario;
	@FXML
	private TableColumn<Tipos, Integer> columnEndereco;
	@FXML
	private TableColumn<Tipos, Integer> columnInstrucao;
	@FXML
	private TableColumn<Tipos, Integer> columnOperacao1;	
	@FXML
	private TableColumn<Tipos, Integer> columnOperacao2;
	
	/* AREA DE LITERAL */
	@FXML
	private TableView<Token> tableViewAreaDeLiteral;
	@FXML
	private TableColumn<Token, Integer> columnEnderecoLiteral;
	@FXML
	private TableColumn<Token, String> columnLiteral;
	
	@FXML
	private TextArea textAreaErros;
	@FXML
	private TextArea txtAreaAlgoritmo;
	@FXML
	private TabPane tabPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		columnToken.setCellValueFactory(new PropertyValueFactory<>("token"));
		columnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		columnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
		
		columnEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
		columnInstrucao.setCellValueFactory(new PropertyValueFactory<>("nome"));
		columnOperacao1.setCellValueFactory(new PropertyValueFactory<>("op1"));
		columnOperacao2.setCellValueFactory(new PropertyValueFactory<>("op2"));
	}
	
	@FXML
	public void analisarAlgoritmo() {
		if(!txtAreaAlgoritmo.getText().trim().isEmpty()) {
			try {
				AnalisadorLexico analisadorLexico = new AnalisadorLexico(txtAreaAlgoritmo.getText());
				List<Token> tokens = analisadorLexico.iniciarAnalise();	
				
				populaTabelaTokens(tokens);
				
				AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
				
				AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokens);
				analisadorSintatico.iniciarDescendentePreditivo(analisadorSemantico);
				
				List<Tipos> instrucoes = analisadorSemantico.obterInstrucoes();
				populaTabelaCodigoIntermediario(instrucoes);
			
				printMensagemSucesso();
			
			} catch (AnalisadorLexicoException analisadorLexicoException) {
				populaTabelaTokens(null);
				printErro(analisadorLexicoException.getMessage());
			} catch (AnalisadorSintaticoException analisadorSintaticoException) {
				printErro(analisadorSintaticoException.getMessage());
			}
			
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
	
	private void populaTabelaTokens(List<Token> linhasTabela) {
		tableViewTokens.setItems(Objects.nonNull(linhasTabela) ? FXCollections.observableArrayList(linhasTabela) : null);
	}
	
	private void populaTabelaCodigoIntermediario(List<Tipos> linhasTabela) {
		tableViewCodigoIntermediario.setItems(Objects.nonNull(linhasTabela) ? FXCollections.observableArrayList(linhasTabela) : null);
	}
	
	private void setStyles(boolean erro) {
		if (erro) {
			textAreaErros.setStyle("-fx-text-inner-color: red;");
		} else {
			textAreaErros.setStyle("-fx-text-inner-color: green;");
		}
	}
	
	private void printErro(String erro) {
		setStyles(true);
		textAreaErros.setText(erro);
	}
	
	private void printMensagemSucesso() {
		setStyles(false);
		textAreaErros.setText("Código sem erro. Compilado com sucesso!");
	}
	
	private void exibeMsg(String titulo, String cabecalho, String msg, AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecalho);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
}
