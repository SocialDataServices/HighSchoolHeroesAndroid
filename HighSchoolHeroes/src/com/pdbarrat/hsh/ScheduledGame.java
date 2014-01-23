package com.pdbarrat.hsh;

import java.util.Calendar;

public class ScheduledGame {
	
	String homeTeamName, awayTeamName;
	boolean didHomeTeamWin;
	int gameId, gameDay, gameMonth, gameYear, homeTeamPoints, awayTeamPoints;
	
	public ScheduledGame(String homeTeam, String awayTeam, boolean homeWon, int homePoints, int awayPoints, Calendar date) {
		
		this.homeTeamName = homeTeam;
		this.awayTeamName = awayTeam;
		this.didHomeTeamWin = homeWon;
		this.homeTeamPoints = homePoints;
		this.awayTeamPoints = awayPoints;
		this.gameDay = date.get(Calendar.DAY_OF_MONTH);
		this.gameMonth = date.get(Calendar.MONTH) + 1;
		this.gameYear = date.get(Calendar.YEAR);
	}
	
}