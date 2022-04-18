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
		while(resultset.next()){
			name = resultset.getString("fullname");
			email = resultset.getString("email");
			gender = resultset.getString("gender");
			age = resultset.getInt("age");
		}
		
		return new User(name, username, email, gender, age);
	}
	
}
