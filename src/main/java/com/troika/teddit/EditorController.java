package com.troika.teddit;

import javafx.application.Platform;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditorController implements Initializable, Runnable{
	
	private final FileChooser fileChooser = new FileChooser();
	private final AtomicBoolean running = new AtomicBoolean(false);
	@FXML
	public ToggleButton tog;
	private recognizer rec = new recognizer();
	@FXML
	private TextArea textArea;
	private Thread worker;
	String username = WelcomeController.currentUser.getUsername();
	private Stage stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".txt", "*.txt"),
												 new FileChooser.ExtensionFilter("All Files", "*.*"));
		try{
			rec.startRecognition();
		}catch(LineUnavailableException | IOException e){
			e.printStackTrace();
		}
	}
	
	@FXML
	public void exit(){
		if(textArea.getText().isEmpty()){
			Platform.exit();
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit without saving?", ButtonType.YES, ButtonType.NO,
								ButtonType.CANCEL);
		
		alert.setTitle("Confirm");
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.YES){
			Platform.exit();
		}
		if(alert.getResult() == ButtonType.NO){
			save();
			Platform.exit();
		}
		stop();
		rec.stopRecognition();
	}
	
	@FXML
	private void save(){
		try{
			fileChooser.setTitle("Save As");
			File file = fileChooser.showSaveDialog(stage);
			
			if(null != file){
				PrintWriter savedText = new PrintWriter(file, StandardCharsets.UTF_8);
				BufferedWriter out = new BufferedWriter(savedText);
				out.write(textArea.getText());
				out.close();
			}
		}catch(FileNotFoundException e){
			System.out.println("Error: " + e);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@FXML
	public void openFile(){
		fileChooser.setTitle("Open File");
		File file = fileChooser.showOpenDialog(stage);
		
		if(null != file){
			textArea.clear();
			readText(file);
		}
	}
	
	private void readText(File file){
		String text;
		
		try(BufferedReader buffReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))){
			while(true){
				String s = buffReader.readLine();
				if(null == (text = s))
					break;
				textArea.appendText(text + "\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@FXML
	public void newFile(){
		textArea.clear();
	}
	
	@FXML
	public void about(){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		
		alert.setTitle("About");
		alert.setHeaderText("A project for CSE-4408 By Troika");
		alert.setContentText("Teddit is a offline voice controlled text editor.\n" + "Made with vosk.");
		alert.showAndWait();
	}
	
	@FXML
	public void fontSize(ActionEvent e){
		String choice = ((Styleable) e.getSource()).getId();
		
		switch(choice){
			case "small" -> textArea.setStyle("-fx-font-size: 16px;");
			case "large" -> textArea.setStyle("-fx-font-size: 24px;");
			case "defalt" -> textArea.setStyle("-fx-font-size: 20px;");
			default -> textArea.setStyle("-fx-font-size: 20px;");
		}
	}
	
	@Override
	public void run(){
		running.set(true);
		while(running.get()){
			String message = rec.getMessage();
			if(!message.isEmpty()){
				textArea.appendText(" " + message);
				try{
					DbHandler.updateWordsFrequency(username, message);
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void toggle(){
		if(Objects.equals(tog.getText(), "Keyboard")){
			tog.setText("Voice+");
			start();
		}else{
			tog.setText("Keyboard");
			stop();
		}
	}
	
	public void start(){
		worker = new Thread(this);
		worker.start();
	}
	
	public void stop(){
		running.set(false);
	}
	
	public void profile() throws IOException{
		Stage profileWindow = new Stage();
		profileWindow.setTitle("profile");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
		profileWindow.setScene(new Scene(loader.load()));
		profileWindow.show();
	}
}
