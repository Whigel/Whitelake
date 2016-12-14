package nrp;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class HelloWorld {
	
	public static final String HTML_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator;
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	//TODO: think about a machine learn approach that the machin learnes key factores. Clustering e.g. Convert Teams into Vectors vectors will point in serten direction
	
	public static void main(String[] args) {
		
		/*NodeList nodes = null;
    	try{
    	Document doc = getXMLDoc("Teams.xml");
    	nodes = doc.getElementsByTagName("team");
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	List<Node> nodes2 = new LinkedList<Node>();
    	for(int i = 0; i<nodes.getLength(); i++){
    		String name = ((Element)nodes.item(0)).getElementsByTagName("name").item(0).getTextContent());
    		nodes2.add(nodes.item(0));
    	}*/
    	
        get("/", (req, res) ->{
        	Map<String, Object> model = new HashMap<>();
        	return getHtml(model, "Homepage.html");
        });
        
        get("/match", (req, res) ->{
        	Set<String> params = req.queryParams();
        	if(params.contains("home") && params.contains("away")){
        		//return req.queryParams("home") + " vs " + req.queryParams("away");
        	
        		//TODO: Create Team & leauge enums in HashMap
        		int awayId = Integer.parseInt(req.queryParams("away")); 
        		int homeId = Integer.parseInt(req.queryParams("home")); 
        		
        		int teamHomeId = 21; //21 is Schalke in Germany
        		
        		int teamAwayId = 23; //21 is Schalke in Germany
        		
        		//TODO: crawl soccer statistics
        		
        		String htmlHome = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+homeId);
        		
        		String htmlAway = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+awayId);
        		//TODO: crawl tip rate
        		
        		//TODO: create Team objects with the crawled data
        		
        		Team home = Team.createTeamFromHtml(htmlHome);
        		
        		Team away = Team.createTeamFromHtml(htmlAway);
        		
        		//TODO: calculate score
        		
        		MatchCalculator match = new MatchCalculator();
        		Float score = match.calculateScore(home, away);
        		
        		//TODO: write scores into file/database 
        		Map<String, Object> model = new HashMap<>();
            	model.put("home", home);
            	model.put("away", away);
            	model.put("score", score);
            	return getHtml(model, "MatchStatistics.html");
        	}
        	return "Ups some parameter is missing";
        });
        
        get("/teams", (req, res)->{
        	
        	NodeList nodes = null;
        	try{
	        	Document doc = getXMLDoc("Teams.xml");
	        	nodes = doc.getElementsByTagName("team");
        	}catch (Exception e) {
				e.printStackTrace();
			}
        	
        	Map<String, Object> model = new HashMap<>();
        	List<Element> nodes2 = new LinkedList<Element>();
        	for(int i = 0; i<nodes.getLength(); i++){
        		Element element = ((Element)nodes.item(i));//.getElementsByTagName("name").item(0).getTextContent());
        		nodes2.add(element);
        	}
        	model.put("nodes", nodes2);
        	
        	return getHtml(model, "Teams.html");
        });
        
        get("/teamstats", (req, res)->{
        	
        	String teamId = req.queryParams("team");
        	String html = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+teamId);
    		
        	Team team = Team.createTeamFromHtml(html);
        	System.out.println(team.getTeamName());
        	Map<String, Object> model = new HashMap<>();
        	model.put("team", team);
        	
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
	
	

	private static Document getXMLDoc(String path) throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File(HTML_PATH+path);
        DocumentBuilderFactory dbFactory 
           = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(inputFile);
        
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
