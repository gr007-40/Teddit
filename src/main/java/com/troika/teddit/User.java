package com.troika.teddit;

public class User{
	private String name;
	private String username;
	private String email;
	private String gender;
	private int age;
	
	public User(String name, String username, String email, String gender, int age){
		this.name = name;
		this.username = username;
		this.email = email;
		this.gender = gender;
		this.age = age;
	}
	
	public String getName(){
		return name;
	}
	
}
