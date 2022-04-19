package com.troika.teddit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable{
	@FXML
	public TextArea freqWords;
	@FXML
	public Text name;
	@FXML
	public Text username;
	@FXML
	public Text gender;
	@FXML
	public Text age;
	@FXML
	public Text email;
	@FXML
	public ImageView limbCond;
	User currentUser = WelcomeController.currentUser;
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		name.setText(currentUser.getName());
		username.setText(currentUser.getUsername());
		gender.setText(currentUser.getGender());
		age.setText(currentUser.getAge());
		email.setText(currentUser.getEmail());
		if(currentUser.hasUpperlimbDisability()){
			limbCond.setImage(new Image("com/troika/teddit/images/icons/checked.png"));
		}else{
			limbCond.setImage(new Image("com/troika/teddit/images/icons/close.png"));
		}
		try{
			freqWords.appendText(DbHandler.getFrequentWords(currentUser.getUsername()));
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	@FXML
	public void changePassword() throws IOException{
		Stage changePassWindow = new Stage();
		changePassWindow.setTitle("Change password");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("changePass.fxml"));
		changePassWindow.setScene(new Scene(loader.load()));
		changePassWindow.show();
	}
}
