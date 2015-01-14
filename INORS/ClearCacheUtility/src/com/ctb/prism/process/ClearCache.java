package com.ctb.prism.process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import com.ctb.prism.security.DigitalMeasuresHMACQueryStringBuilder;

public class ClearCache {

	//private static String REQUEST_URL = "http://10.160.23.12:8080/prism/clearContractCache.do";
	//private static String SECRET_KEY =  "BTCguSF49hYaPmAfe9Q29LtsQ2X";
	private static String URL_ENCODING = "UTF-8";
	private static String ENCODING_ALGORITHM = "HmacSHA1";
	private static String timeZone = "GMT";
	
	static ResourceBundle rb = ResourceBundle.getBundle("com.ctb.prism.bundle");
	
	private static String REQUEST_URL = "/prism/clearContractCache.do";


	
public static void main(String[] args) throws Exception {
		REQUEST_URL = "http://" + rb.getString("server.url") + REQUEST_URL;
		String SECRET_KEY = rb.getString("hmac.secret.key");
			
		if(args.length > 0){
			String theme = args[0];
			DigitalMeasuresHMACQueryStringBuilder queryStringBuilder = new DigitalMeasuresHMACQueryStringBuilder(SECRET_KEY, 3600*60, ENCODING_ALGORITHM);
			queryStringBuilder.setTimeZone(timeZone);
			queryStringBuilder.setENCODING_ALGORITHM(ENCODING_ALGORITHM);
			queryStringBuilder.setURL_ENCODING(URL_ENCODING);
		
			String requestParam = queryStringBuilder.buildAuthenticatedQueryString(theme);
			System.out.println(REQUEST_URL + "?" + requestParam);
			
			requestWithQueryString(requestParam);
		} else {
			System.out.println("Please provide theme as parameter");
		}
	
	}


 private static void requestWithQueryString(String queryString) throws IOException
	{	
	 	String request =  REQUEST_URL + "?" + queryString;
		URL url = new URL(request); 
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
	    connection.setDoOutput(true); 
	    connection.setInstanceFollowRedirects(false); 
	    connection.setRequestMethod("GET"); 
	    connection.setRequestProperty("Content-Type", "text/plain"); 
	    connection.setRequestProperty("charset", "utf-8");
	    connection.connect();
	}

}
