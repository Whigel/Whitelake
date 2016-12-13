package nrp;

public class MatchCalculator {
	
	public static float calculateScore(Team home, Team against){
		return (float)home.getPointsHome() / (float)against.getPointsAway();
	}
}
