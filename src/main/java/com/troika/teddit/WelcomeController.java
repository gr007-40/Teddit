package com.troika.teddit;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable{
	static User currentUser;
	@FXML
	private PasswordField pf_password;
	@FXML
	private TextField tf_username;
	@FXML
	private AnchorPane anchorRoot;
	@FXML
	private StackPane parentContainer;
	
	@FXML
	private void signUp() throws IOException{
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup.fxml")));
		parentContainer.getChildren().add(root);
		
		FadeTransition fadeIn = new FadeTransition();
		fadeIn.setDuration(Duration.millis(1000));
		fadeIn.setNode(root);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1.0);
		
		FadeTransition fadeOut = new FadeTransition();
		fadeOut.setDuration(Duration.millis(1000));
		fadeOut.setNode(anchorRoot);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0);
		
		fadeOut.play();
		fadeIn.play();
		
		parentContainer.getChildren().remove(anchorRoot);
	}
	
	@FXML
	void signIn() throws NoSuchAlgorithmException, SQLException, IOException{
		//TODO
		if(pf_password.getText().isEmpty() || tf_username.getText().isEmpty()){
			System.out.println("Invalid username or password.");
			Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.");
			alert.show();
			return;
		}
		MessageDigest md = MessageDigest.getInstance("MD5");
		String password = pf_password.getText();
		md.update(password.getBytes(StandardCharsets.UTF_8));
		byte[] digest = md.digest();
		StringBuilder sb = new StringBuilder();
		for(byte b : digest){
			sb.append(String.format("%02x", b));
		}
		String hash = sb.toString();
		boolean verified = DbHandler.isVerifiedUser(tf_username.getText(), hash);
		if(verified){
			currentUser = DbHandler.getUser(tf_username.getText());
			Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("editor.fxml")));
			parentContainer.getChildren().add(root);
			
			FadeTransition fadeIn = new FadeTransition();
			fadeIn.setDuration(Duration.millis(1000));
			fadeIn.setNode(root);
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1.0);
			
			FadeTransition fadeOut = new FadeTransition();
			fadeOut.setDuration(Duration.millis(1000));
			fadeOut.setNode(anchorRoot);
			fadeOut.setFromValue(1.0);
			fadeOut.setToValue(0);
			
			fadeOut.play();
			fadeIn.play();
			
			parentContainer.getChildren().remove(anchorRoot);
		}else{
			System.out.println("Invalid username or password.");
			Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.");
			alert.show();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		//TODO
	}
}
