package nrp;

import static jodd.lagarto.dom.jerry.Jerry.jerry;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jodd.lagarto.dom.jerry.Jerry;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class HelloWorld {
	
	public static final String HTML_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator;
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	//TODO: think about a machine learn approach that the machin learnes key factores. Clustering e.g. Convert Teams into Vectors vectors will point in serten direction
	
	public static void main(String[] args) {
        get("/", (req, res) ->{
        	Map<String, Object> model = new HashMap<>();
        	return getHtml(model, "Homepage.html");
        });
        
        get("/match", (req, res) ->{
        	Set<String> params = req.queryParams();
        	//if(params.contains("home") && params.contains("against")){
        		//return req.queryParams("home") + " vs " + req.queryParams("against");
        	
        		//TODO: Create Team & leauge enums in HashMap
        	
        		int teamId = 21; //21 is Schalke in Germany
        		
        		String html = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+teamId);
        		
        		Jerry jerry = jerry(html);
        		
        		System.out.print(jerry.$("td:nth-child(2) b"));
        		//TODO: crawl soccer statistics
        		
        		//TODO: crawl tip rate
        		
        		//TODO: create Team objects with the crawled data
        		
        		//TODO: calculate score
        		
        		//TODO: write scores into file/database 
        		
        		return html;
        	//}
        	//return "Ups some parameter is missing";
        });
        
        get("/teamstats", (req, res)->{
        	
        	String teamId = req.queryParams("team");
        	String html = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+teamId);
    		
        	Jerry jerry = jerry(html);
        	
        	String teamName = jerry.$("#content div div table tr h1").html();
    		
    		String wins = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(2) font b").html();
    		String draws = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(3) font b").html();
    		String losses = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(4) font b").html();
    		
        	Map<String, Object> model = new HashMap<>();
        	model.put("wins", wins);
        	model.put("draws", draws);
        	model.put("losses", losses);
        	model.put("teamName", teamName);
        	
        	return getHtml(model, "TeamStatistics.html");
        });
        
        post("/result",  (req, res) -> {
        	String name = req.queryParams("first") + " " + req.queryParams("last");
        	String birthday = req.queryParams("birthday");
        	
        	int daysLeft = Datumsrechner.tageBisZumGeburtstag(birthday);
        	
        	Map<String, Object> model = new HashMap<>();
        	model.put("daystogo", daysLeft);
        	model.put("name", name);
        	
        	if (daysLeft == 0 ){
        		return getHtml(model, "HappyBirthday.html");
        	}
        	
        	return getHtml(model, "DaysTogo.html");
        });
    }
	
	

	private static String getHtml(Map<String, Object>  model, String path) throws IOException{
		
		return  new VelocityTemplateEngine().render(new ModelAndView(model, path));
		//return readFile(HTML_PATH + path,  Charset.defaultCharset());
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
	  	return new String(encoded, encoding);
	}
}
