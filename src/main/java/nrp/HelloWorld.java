package nrp;

import static spark.Spark.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloWorld {
	
	public static final String HTML_PATH = "src"+File.separator+"main"+File.separator+"resources"+File.separator;
	
	public static void main(String[] args) {
        get("/", (req, res) ->{
        	
        	return getHtml("Homepage.html");
        });
        
        post("/result",  (req, res) -> {
        	System.out.println(req.params("first"));
        	return "THIS IS SHIT";
        });
    }
	
	private static String getHtml(String path) throws IOException{
		return readFile(HTML_PATH + path,  Charset.defaultCharset());
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
	  	return new String(encoded, encoding);
	}
}
