package com.troika.teddit;

import javafx.scene.control.Alert;

import java.sql.*;

public class DbHandler{
	static Connection connection;
	static PreparedStatement statement;
	static ResultSet resultset;
	static PreparedStatement verify;
	static PreparedStatement query;
	
	public static void establishConnection() throws SQLException{
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/teddit", "user", "password");
		System.out.println("Successfully established connection to the database.");
	}
	
	public static void insertIntoDatabase(String fullname, String email, String username, int age, String gender,
										  String pwdHash, boolean hasUpperLimbDisability) throws SQLException{
		statement = connection.prepareStatement("insert into user values(?,?,?,?,?,?,?)");
		statement.setString(1, fullname);
		statement.setString(2, email);
		statement.setString(3, username);
		statement.setInt(4, age);
		statement.setString(5, gender);
		statement.setString(6, pwdHash);
		statement.setBoolean(7, hasUpperLimbDisability);
		try{
			statement.execute();
		}catch(SQLException e){
			System.out.println((e.getMessage()));
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.show();
		}finally{
			statement.close();
		}
	}
	
	public static void closeConnection() throws SQLException{
		connection.close();
		System.out.println("Database closed successfully");
	}
	
	public static boolean isVerifiedUser(String username, String hash) throws SQLException{
		verify = connection.prepareStatement("select username from user where username = ? and pwdHash = ?");
		verify.setString(1, username);
		verify.setString(2, hash);
		String res = "";
		try{
			resultset = verify.executeQuery();
			while(resultset.next()){
				res = resultset.getString(1);
			}
			return res.equals(username);
		}catch(SQLException e){
			System.out.println(e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.show();
		}
		return false;
	}
	
	public static User getUser(String username) throws SQLException{
		query = connection.prepareStatement("select * from user where username = ?");
		query.setString(1, username);
		resultset = query.executeQuery();
		String name = null;
		String email = null;
		String gender = null;
		int age = 0;
		boolean hasUpperLimbDisability = false;
		while(resultset.next()){
			name = resultset.getString("fullname");
			email = resultset.getString("email");
			gender = resultset.getString("gender");
			age = resultset.getInt("age");
			hasUpperLimbDisability = resultset.getBoolean("hasUpperLimbDisability");
		}
		
		return new User(name, username, email, gender, age, hasUpperLimbDisability);
	}
	
	public static void updateWordsFrequency(String username, String message) throws SQLException{
		String[] words = message.split(" ");
		for(String word : words){
			updateFrequency(username, word);
		}
	}
	
	private static void updateFrequency(String username, String word) throws SQLException{
		query = connection.prepareStatement("select frequency from words where user=? and word=?");
		query.setString(1, username);
		query.setString(2, word);
		int freq = 0;
		resultset = query.executeQuery();
		while(resultset.next()){
			freq = resultset.getInt("frequency");
		}
		
		if(0 == freq){
			statement = connection.prepareStatement("insert into words values(?,?,?)");
			statement.setString(1, word);
			statement.setInt(2, 1);
			statement.setString(3, username);
			statement.execute();
		}else{
			statement = connection.prepareStatement("update words set frequency=? where user=? and word=?");
			statement.setInt(1, freq + 1);
			statement.setString(2, username);
			statement.setString(3, word);
			statement.execute();
		}
	}
	
	public static String getFrequentWords(String username) throws SQLException{
		query =
				connection.prepareStatement("select word,frequency from words where user=? order by frequency desc " + "limit " + "30");
		query.setString(1, username);
		resultset = query.executeQuery();
		StringBuilder res = new StringBuilder();
		res.append("Word\t\t\t\tFrequency\n").append("----------------\t\t\t---------\n");
		while(resultset.next()){
			String word = resultset.getString("word");
			int freq = resultset.getInt("frequency");
			res.append(word).append("\t\t\t\t\t").append(freq).append("\n");
		}
		return res.toString();
	}
	
	public static void updatePassword(String username, String nhash) throws SQLException{
		statement = connection.prepareStatement("update user set pwdHash=? where username=?");
		statement.setString(2, username);
		statement.setString(1, nhash);
		statement.execute();
	}
}
