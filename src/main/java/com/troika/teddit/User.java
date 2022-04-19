package com.troika.teddit;

public class User{
	private String name;
	private String username;
	private String email;
	private String gender;
	private int age;
	private boolean hasUpperLimbDisability;
	
	public User(String name, String username, String email, String gender, int age, boolean hasUpperLimbDisability){
		this.name = name;
		this.username = username;
		this.email = email;
		this.gender = gender;
		this.age = age;
		this.hasUpperLimbDisability = hasUpperLimbDisability;
	}
	
	public String getName(){
		return name;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getAge(){
		return String.valueOf(age);
	}
	
	public String getGender(){
		return gender;
	}
	
	public boolean hasUpperlimbDisability(){
		return hasUpperLimbDisability;
	}
}
