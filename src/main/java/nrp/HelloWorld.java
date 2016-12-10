package nrp;

import static spark.Spark.*;

public class HelloWorld {
	public static void main(String[] args) {
        get("/", (req, res) -> "<html><h1> NIKLAS WAR HIER! </h1> </html>");
        
        post("/result",  (req, res) -> "<html><h1> NIKLAS WAR HIER! </h1> </html>");
    }
}
