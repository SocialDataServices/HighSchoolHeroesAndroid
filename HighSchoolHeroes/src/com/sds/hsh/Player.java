package com.sds.hsh;

import java.text.Collator;
import java.util.Comparator;

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

class AscendingNameComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {
		Collator c = Collator.getInstance();
		return c.compare(first.lastName, second.lastName);
	}
}

class DescendingNameComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {
		Collator c = Collator.getInstance();
		return -(c.compare(first.lastName, second.lastName));
	}
}

class AscendingNumberComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		return first.number - second.number;
	}		
}

class DescendingNumberComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		return -(first.number - second.number);
	}		
}

class AscendingWeightComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		return first.weight - second.weight;
	}		
}

class DescendingWeightComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		return -(first.weight - second.weight);
	}		
}

class AscendingPositionComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		Collator c = Collator.getInstance();
		return c.compare(first.position, second.position);
	}		
}

class DescendingPositionComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		Collator c = Collator.getInstance();
		return -(c.compare(first.position, second.position));
	}		
}

class AscendingYearComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		
		int player1year, player2year;
		
		if(first.year.equals("Sr."))
			player1year = 4;
		else if(first.year.equals("Jr."))
			player1year = 3;
		else if(first.year.equals("So."))
			player1year = 2;
		else
			player1year = 1;
		
		
		if(second.year.equals("Sr."))
			player2year = 4;
		else if(second.year.equals("Jr."))
			player2year = 3;
		else if(second.year.equals("So."))
			player2year = 2;
		else
			player2year = 1;
		
		return player1year - player2year;
	}		
}

class DescendingYearComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {	
		
		int player1year, player2year;
		
		if(first.year.equals("Sr."))
			player1year = 4;
		else if(first.year.equals("Jr."))
			player1year = 3;
		else if(first.year.equals("So."))
			player1year = 2;
		else
			player1year = 1;
		
		
		if(second.year.equals("Sr."))
			player2year = 4;
		else if(second.year.equals("Jr."))
			player2year = 3;
		else if(second.year.equals("So."))
			player2year = 2;
		else
			player2year = 1;
		
		return -(player1year - player2year);
	}		
}

class AscendingHeightComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {			
		
		String[] p1 = first.height.split("-");
		String[] p2 = second.height.split("-");
		
		int p1inches = Integer.parseInt(p1[0]) * 12 + Integer.parseInt(p1[1]);
		int p2inches = Integer.parseInt(p2[0]) * 12 + Integer.parseInt(p2[1]);
		return p1inches - p2inches;
		
		
	}		
}

class DescendingHeightComparer implements Comparator<Player> {
	public int compare(Player first, Player second) {			
		
		String[] p1 = first.height.split("-");
		String[] p2 = second.height.split("-");
		
		int p1inches = Integer.parseInt(p1[0]) * 12 + Integer.parseInt(p1[1]);
		int p2inches = Integer.parseInt(p2[0]) * 12 + Integer.parseInt(p2[1]);
		return -(p1inches - p2inches);
		
		
	}		
}