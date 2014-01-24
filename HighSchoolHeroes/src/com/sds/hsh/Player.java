package com.sds.hsh;

public class Player {
	
	String lastName, firstName, height, bio, year, position;
	int number, weight;
	
	public Player(String firstName, String lastName, String height, int weight, int num, String year, String position)  {
		
		this.lastName = lastName;
		this.firstName = firstName;
		this.bio = "";
		this.height = height;
		this.weight = weight;
		this.number = num;
		this.year = year;
		this.position = position;
	}
	
}