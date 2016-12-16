package nrp;

import static jodd.lagarto.dom.jerry.Jerry.jerry;

import java.io.StringWriter;
import java.util.List;

import org.python.util.PythonInterpreter;

import jodd.lagarto.dom.jerry.Jerry;

public class Team {
	
	
	private static final int LAST_GAMES_COUNT = 5;
	public List<Boolean> lastFiveMatches;
	
	private String teamName;
	private int wonHome;
	private int lostHome;
	private int drawHome;
	private int wonAway;
	private int lostAway;
	private int drawAway;
	private int pointsHome;
	private int pointsAway;
	private int concededHome;
	private int concededAway;
	private int scoredHome;
	private int scoredAway;
	private int gamesPlayedHome;
	private int gamesPlayedAway;
		
	public Team(String teamName){
		this.teamName = teamName;
	}


	public static Team createTeamFromHtml(String html) {
		
		Jerry jerry = jerry(html);
    	
		System.out.println("Team X");
		
    	String teamName = jerry.$("#content div div table tr h1").html();
		
    	System.out.println(teamName);
    	try{
    	//System.out.println(jerry.$("#content div div.row div.five.columns table:nth-child(11) ").html());
		int wonHome = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(2) td:nth-child(2) ").html());
		int lostHome = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(2) td:nth-child(3) ").html());
		int drawHome = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(2) td:nth-child(4) ").html());
		
		int wonAway = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(3) td:nth-child(2)").html());
		int lostAway = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(3) td:nth-child(3)").html());
		int drawAway = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(3) td:nth-child(4)").html());
    	
		int scoredHome = Integer.parseInt(jerry.$("#content div div.row div.five.columns table:nth-child(11) tr:nth-child(2) td:nth-child(2) ").html());
		int scoredAway = Integer.parseInt(jerry.$("#content div div.row div.five.columns table:nth-child(11) tr:nth-child(2) td:nth-child(3) ").html());
		
		int concededHome = Integer.parseInt(jerry.$("#content div div.row div.five.columns table:nth-child(11) tr:nth-child(3) td:nth-child(2) ").html());
		int concededAway = Integer.parseInt(jerry.$("#content div div.row div.five.columns table:nth-child(11) tr:nth-child(3) td:nth-child(3) ").html());
		
		int gamesPlayedHome = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(2) td:nth-child(5) b").html());
		int gamesPlayedAway = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(3) td:nth-child(5) b").html());
		
		//TODO: Crawl all Matches all goals 
		for(int i = 3; i < 3+LAST_GAMES_COUNT; i++){
			System.out.println(jerry.$("#content div div.row div:nth-child(1) tr:nth-child("+i+") td:nth-child(3) b").html());
		}
		
		
		
		
		
		
		//List<String> games = getGamesFromHtml(jerry);
		Team team = new Team(teamName);
		team.setDrawAway(drawAway);
		team.setDrawHome(drawHome);
		team.setLostAway(lostAway);
		team.setLostHome(lostHome);
		team.setWonAway(wonAway);
		team.setWonHome(wonHome);
		team.setConcededAway(concededAway);
		team.setConcededHome(concededHome);
		team.setScoredAway(scoredAway);
		team.setScoredHome(scoredHome);
		team.setGamesPlayedHome(gamesPlayedHome);
		team.setGamesPlayedAway(gamesPlayedAway);
		
		team.setPointsAway(3 * wonAway + drawAway);
		team.setPointsHome(3 * wonHome + drawHome);
		return team;
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	private static List<String> getGamesFromHtml(Jerry jerry) {
		int i = 1;
		while(true){
			
			System.out.println(jerry.$("#content div div.row div.seven.columns").html());
			i++;
			if(i>100) break;
		}
		return null;
	}

	public float calculateHomeScore(Team away){
		//System.out.println(this.scoredHome +"/"+(float) this.gamesPlayedHome + "*" +"("+away.getConcededAway() +"/" +(float) away.getGamesPlayedAway());
		return (this.scoredHome /(float) this.gamesPlayedHome ) * (away.getConcededAway() /(float) away.getGamesPlayedAway());
	}
	
	public float calculateAwayScore(Team home){
		//System.out.println(this.scoredHome +"/"+(float) this.gamesPlayedHome + "*" +"("+away.getConcededAway() +"/" +(float) away.getGamesPlayedAway());
		return (this.getScoredAway() /(float) this.getGamesPlayedAway() ) * (home.getConcededHome() /(float) home.getGamesPlayedHome());
	}
	
	public String getTeamName() {
		return teamName;
	}


	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}


	public int getPointsAway() {
		return pointsAway;
	}


	public void setPointsAway(int pointsAway) {
		this.pointsAway = pointsAway;
	}


	public int getWonHome() {
		return wonHome;
	}


	public void setWonHome(int wonHome) {
		this.wonHome = wonHome;
	}


	public int getLostHome() {
		return lostHome;
	}


	public void setLostHome(int lostHome) {
		this.lostHome = lostHome;
	}


	public int getDrawHome() {
		return drawHome;
	}


	public void setDrawHome(int drawHome) {
		this.drawHome = drawHome;
	}


	public int getWonAway() {
		return wonAway;
	}


	public void setWonAway(int wonAway) {
		this.wonAway = wonAway;
	}


	public int getLostAway() {
		return lostAway;
	}


	public void setLostAway(int lostAway) {
		this.lostAway = lostAway;
	}


	public int getDrawAway() {
		return drawAway;
	}


	public void setDrawAway(int drawAway) {
		this.drawAway = drawAway;
	}


	public int getPointsHome() {
		return pointsHome;
	}


	public void setPointsHome(int pointsHome) {
		this.pointsHome = pointsHome;
	}


	public int getConcededHome() {
		return concededHome;
	}


	public void setConcededHome(int concededHome) {
		this.concededHome = concededHome;
	}


	public int getConcededAway() {
		return concededAway;
	}


	public void setConcededAway(int concededAway) {
		this.concededAway = concededAway;
	}


	public int getScoredHome() {
		return scoredHome;
	}


	public void setScoredHome(int scoredHome) {
		this.scoredHome = scoredHome;
	}


	public int getScoredAway() {
		return scoredAway;
	}


	public void setScoredAway(int scoredAway) {
		this.scoredAway = scoredAway;
	}

	public int getGamesPlayedAway() {
		return gamesPlayedAway;
	}


	public void setGamesPlayedAway(int gamesPlayedAway) {
		this.gamesPlayedAway = gamesPlayedAway;
	}


	public List<Boolean> getLastFiveMatches() {
		return lastFiveMatches;
	}


	public void setLastFiveMatches(List<Boolean> lastFiveMatches) {
		this.lastFiveMatches = lastFiveMatches;
	}


	public int getGamesPlayedHome() {
		return gamesPlayedHome;
	}


	public void setGamesPlayedHome(int gamesPlayedHome) {
		this.gamesPlayedHome = gamesPlayedHome;
	}
	

}
