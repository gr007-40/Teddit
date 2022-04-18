package com.troika.teddit;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;

public class SignupController{
	@FXML
	private TextField i_age;
	@FXML
	private TextField tf_fullName;
	@FXML
	private TextField tf_email;
	@FXML
	private TextField tf_username;
	@FXML
	private TextField genderText;
	@FXML
	private CheckBox accept_upper_limb_disability;
	@FXML
	private CheckBox agreement;
	@FXML
	private PasswordField pf_password;
	@FXML
	private PasswordField pf_password1;
	@FXML
	private AnchorPane anchorRoot;
	@FXML
	private StackPane parentContainer;
	
	@FXML
	public void signIn() throws IOException{
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
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
	public void signUp() throws NoSuchAlgorithmException{
		String password = pf_password.getText();
		String password1 = pf_password1.getText();
		String fullname = tf_fullName.getText();
		String email = tf_email.getText();
		String username = tf_username.getText();
		int age = 0;
		String gender = genderText.getText();
		String hash;
		if(password.isEmpty() || password1.isEmpty()){
			Alert alert = new Alert(Alert.AlertType.ERROR, "password field is empty");
			alert.show();
			return;
		}
		if(password.equals(password1)){
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for(byte b : digest){
				sb.append(String.format("%02x", b));
			}
			hash = sb.toString();
		}else{
			Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match");
			alert.show();
			return;
		}
		if(!agreement.isSelected()){
			Alert alert = new Alert(Alert.AlertType.ERROR, "Please agree our terms and conditions to sign up");
			alert.show();
			return;
		}
		try{
			age = Integer.parseInt(i_age.getText());
		}catch(NumberFormatException e){
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage() + " for age");
			alert.show();
			return;
		}
		if(fullname.isEmpty() || email.isEmpty() || username.isEmpty() || gender.isEmpty()){
			Alert alert = new Alert(Alert.AlertType.ERROR, "empty input field(s)");
			alert.show();
			return;
		}
		try{
			DbHandler.insertIntoDatabase(fullname, email, username, age, gender, hash,
										 accept_upper_limb_disability.isSelected());
			System.out.println("Successfully signed up as user: " + username);
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully signed up as user: " + username);
			alert.show();
			signIn();
		}catch(SQLException | IOException e){
			System.out.println(e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.show();
		}
	}
}
