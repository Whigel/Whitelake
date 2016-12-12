package nrp;

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

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class HelloWorld {
	
	public static final String HTML_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator;
	
	
	//TODO: think about a machine learn approach that the mashine learnes key factores.
	
	public static void main(String[] args) {
        get("/", (req, res) ->{
        	Map<String, Object> model = new HashMap<>();
        	return getHtml(model, "Homepage.html");
        });
        
        get("/match", (req, res) ->{
        	Set<String> params = req.queryParams();
        	if(params.contains("home") && params.contains("against")){
        		return req.queryParams("home") + " vs " + req.queryParams("against");
        		
        		//TODO: crawl soccer statistics
        		
        		//TODO: crawl top rate
        		
        		//TODO: create Team objects with the crawled data
        		
        		//TODO: calculate score
        		
        		//TODO: write scores into file/database 
        		
        		
        	}
        	return "Ups some parameter is missing";
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
