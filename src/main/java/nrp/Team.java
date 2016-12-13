package nrp;

import static jodd.lagarto.dom.jerry.Jerry.jerry;

import java.util.List;

import jodd.lagarto.dom.jerry.Jerry;

public class Team {
	
	
	//TODO: add all relevant infos about teams
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
	private int gamesPlayed;
	
	
	public Team(String teamName){
		this.teamName = teamName;
	}


	public static Team createTeamFromHtml(String html) {
		
		Jerry jerry = jerry(html);
    	
		System.out.println("Team X");
		
    	String teamName = jerry.$("#content div div table tr h1").html();
		
    	System.out.println(teamName);
    	try{
    	System.out.println(jerry.$("#content div div.row div.five.columns table:nth-child(11) tr:nth-child(2) td:nth-child(2) ").html());
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
		
		int gamesPlayed = Integer.parseInt(jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(5) b").html());
		
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
		team.setGamesPlayed(gamesPlayed);
		
		team.setPointsAway(3 * wonAway + drawAway);
		team.setPointsHome(3 * wonHome + drawHome);
		return team;
    	}
    	catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
    	return null;
	}
	
	public float calculateHomeScore(Team away){
		return (this.scoredHome / 14f )* (away.concededAway / 14f);
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


	public int getGamesPlayed() {
		return gamesPlayed;
	}


	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	
	

}
