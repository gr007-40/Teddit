package com.troika.teddit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ChangePassController{
	@FXML
	public PasswordField currPwd;
	@FXML
	public PasswordField newPwd;
	@FXML
	public PasswordField newCpwd;
	User user = WelcomeController.currentUser;
	
	public void changePass(ActionEvent actionEvent) throws NoSuchAlgorithmException, SQLException{
		if(currPwd.getText().isEmpty() || newPwd.getText().isEmpty() || newCpwd.getText().isEmpty()){
			System.out.println("Invalid password.");
			Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid password.");
			alert.show();
			return;
		}
		MessageDigest md = MessageDigest.getInstance("MD5");
		String password = currPwd.getText();
		md.update(password.getBytes(StandardCharsets.UTF_8));
		byte[] digest = md.digest();
		StringBuilder sb = new StringBuilder();
		for(byte b : digest){
			sb.append(String.format("%02x", b));
		}
		String hash = sb.toString();
		boolean verified = DbHandler.isVerifiedUser(user.getUsername(), hash);
		if(verified){
			String npwd = newPwd.getText();
			md.update(npwd.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest();
			StringBuilder builder = new StringBuilder();
			for(byte b : bytes){
				builder.append(String.format("%02x", b));
			}
			String nhash = builder.toString();
			String ncpwd = newCpwd.getText();
			md.update(ncpwd.getBytes(StandardCharsets.UTF_8));
			byte[] digest1 = md.digest();
			StringBuilder builder1 = new StringBuilder();
			for(byte b : digest1){
				builder1.append(String.format("%02x", b));
			}
			String nchash = builder1.toString();
			if(nhash.equals(nchash)){
				DbHandler.updatePassword(user.getUsername(), nhash);
				System.out.println("Password changed successfully");
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password changed successfully");
				alert.show();
			}else{
				System.out.println("Passwords do not match.");
				Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match.");
				alert.show();
			}
		}else{
			System.out.println("Invalid password.");
			Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid password.");
			alert.show();
		}
	}
}
