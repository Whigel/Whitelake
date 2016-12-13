package nrp;

import static jodd.lagarto.dom.jerry.Jerry.jerry;

import java.util.ArrayList;
import java.util.List;

import jodd.lagarto.dom.jerry.Jerry;

public class Team {
	
	//TODO: add all relevant infos about teams
	public List<Boolean> lastFiveMatches;
	
	
	private String teamName;
	private int won;
	private int lost;
	private int draw;
	private int points;
	
	public Team(String teamName, int won, int lost, int draw){
		this.teamName = teamName;
		this.won = won;
		this.lost = lost;
		this.draw = draw;
		this.setPoints(won * 3 + draw);
	}


	public static Team createTeamFromHtml(String html) {
		
		Jerry jerry = jerry(html);
    	
    	String teamName = jerry.$("#content div div table tr h1").html();
		
		String won = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(2) font b").html();
		String lost = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(3) font b").html();
		String draw = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(4) font b").html();
		
		System.out.println(won);
		return new Team(teamName, Integer.parseInt(won), Integer.parseInt(lost), Integer.parseInt(draw));
	}


	public String getTeamName() {
		return teamName;
	}


	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}


	public int getWon() {
		return won;
	}


	public void setWon(int won) {
		this.won = won;
	}


	public int getLost() {
		return lost;
	}


	public void setLost(int lost) {
		this.lost = lost;
	}


	public int getDraw() {
		return draw;
	}


	public void setDraw(int draw) {
		this.draw = draw;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}
}
