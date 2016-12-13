package nrp;

import static jodd.lagarto.dom.jerry.Jerry.jerry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jodd.lagarto.dom.jerry.Jerry;

public class HttpHandler {
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static String sendGet(String url) throws Exception {


		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}
	
	public static void main(String... args){
		try {
			for (int teamId = 10; teamId < 20; teamId++){
				String html = HttpHandler.sendGet("http://www.soccerstats.com/team.asp?league=germany&teamid="+teamId);
	    		
				//System.out.println(html);
	    		
	    		Jerry jerry = jerry(html);
	    		String teamName = jerry.$("#content div div table tr h1").html();
	    		//String div = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(3) font b").html();
	    		//System.out.println(div);
	    		String wins = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(2) font b").html();
	    		String draw = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(3) font b").html();
	    		String loss = jerry.$("#content div div.row div.five.columns table tr:nth-child(4) td:nth-child(4) font b").html();
	    		
	    		System.out.println(teamName + " statistics: "+wins+"|"+draw+"|"+loss);
	    		//Pattern patter = Pattern.compile("<b>[0-9]*</b>");
	    		//pattern.
	    		//System.out.println(jerry.$("html , body , #container , #contest , div , div , div , table , tbody , tr , td , font , b").html());
				Document doc = Jsoup.connect("http://www.soccerstats.com/team.asp?league=germany&teamid="+teamId).get();
				Elements bold = doc.select("#content");
				System.out.println(bold.size());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
