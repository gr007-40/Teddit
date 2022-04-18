module com.troika.teddit {
	requires javafx.controls;
	requires javafx.fxml;
	
	requires javafx.graphics;
	requires java.sql;
	requires java.desktop;
	requires com.sun.jna;
	
	opens org.vosk to com.sun.jna;
	opens com.troika.teddit to javafx.fxml;
	exports com.troika.teddit;
}