package nrp;

import static jodd.lagarto.dom.jerry.Jerry.jerry;

import java.util.Arrays;

import jodd.lagarto.dom.jerry.Jerry;

public class MatchCalculator {
	
	
	private static final int START_ID = 19;
	private static final int END_ID = 24;
	private static final int LAST_GAMES_COUNT = 10;
	private static final String[] TEAMS = {"Hoffenheim", "Hertha Berlin", "Schalke 04", "Werder Bremen", "Bayern Munich"};
	public static float calculateScore(Team home, Team against){
		return (float)home.getPointsHome() / (float)against.getPointsAway();
	}
	
	public static void main(String... args) throws Exception{
		
		int range = END_ID - START_ID ;
		int[][] goals = new int[range][range];
		System.out.println(goals[2][0]);
		for(int teamId = START_ID; teamId<END_ID; teamId++){
			
			String html = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+teamId);
			
			Jerry jerry = jerry(html);
			
			String teamName = jerry.$("#content div div table tr h1").html();
			System.out.println(teamName);
			
			//TODO: Crawl all Matches all goals 
			
			for(int i = 3; i < 3+LAST_GAMES_COUNT; i++){
				String enemy = "";
				boolean home = false;
				try{
					enemy = jerry.$("#content div div.row div:nth-child(1) tr:nth-child("+i+") td:nth-child(2) b").html();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if(enemy.equals(teamName)){
					home = true;
					enemy = jerry.$("#content div div.row div:nth-child(1) tr:nth-child("+i+") td:nth-child(4) ").html();
					//System.out.println(enemy);
				}
				else{
					enemy = jerry.$("#content div div.row div:nth-child(1) tr:nth-child("+i+") td:nth-child(2) ").html();
				}
				
				String game = jerry.$("#content div div.row div:nth-child(1) tr:nth-child("+i+") td:nth-child(3) b").html();
				
				int indexTeam = Arrays.asList(TEAMS).indexOf(teamName);
				
				int indexEnemy = Arrays.asList(TEAMS).indexOf(enemy);
				if(indexTeam != -1 && indexEnemy != -1){
					System.out.println(game);
					if(home){
						goals[indexTeam][indexEnemy] = Integer.parseInt(game.charAt(0)+"");
						goals[indexEnemy][indexTeam] = Integer.parseInt(game.charAt(4)+"");
					}
					else{
						goals[indexTeam][indexEnemy] = Integer.parseInt(game.charAt(4)+"");
						goals[indexEnemy][indexTeam] = Integer.parseInt(game.charAt(0)+"");
					}
				}	
			}
		}
		String goal = Arrays.deepToString(goals);
		String pi = HttpHandler.sendGet("http://localhost:8000?games="+goal);
				
		System.out.println(Arrays.deepToString(goals));
		System.out.println(pi);
	}
}
