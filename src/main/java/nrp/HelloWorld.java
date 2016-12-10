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

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class HelloWorld {
	
	public static final String HTML_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator;
	
	public static void main(String[] args) {
        get("/", (req, res) ->{
        	
        	return getHtml("Homepage.html");
        });
        
        post("/result",  (req, res) -> {
        	System.out.println(req.attributes());
        	return "THIS IS SHIT";
        });
    }
	
	private static String getHtml(String path) throws IOException{
		Map<String, Object> model = new HashMap<>();
		return  new VelocityTemplateEngine().render(new ModelAndView(model, path));
		//return readFile(HTML_PATH + path,  Charset.defaultCharset());
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
	  	return new String(encoded, encoding);
	}
}
