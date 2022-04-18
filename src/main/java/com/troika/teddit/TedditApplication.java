package com.troika.teddit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class TedditApplication extends Application{
	
	public static void main(String[] args) throws SQLException{
		DbHandler.establishConnection();
		Application.launch();
		DbHandler.closeConnection();
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException{
		Scene scene = new Scene(new FXMLLoader(getClass().getResource("welcome.fxml")).load());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Teddit");
		primaryStage.minWidthProperty().bind(scene.widthProperty());
		primaryStage.minHeightProperty().bind(scene.heightProperty());
		primaryStage.show();
	}
}