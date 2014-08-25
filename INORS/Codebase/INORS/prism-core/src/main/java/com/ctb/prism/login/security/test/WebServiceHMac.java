package com.ctb.prism.login.security.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.ctb.prism.login.security.encoder.DigitalMeasuresHMACQueryStringBuilder;

public class WebServiceHMac {
	private static String REQUEST_URL = "http://localhost:8080/idoeonlinereports/AccountService";
	private static String apykey = "217907";
	private static String IP = "127.0.0.1";
	private static String SECRET_KEY = "BTCguSF49hYaPmAfe9Q29LtsQ2X";
	private static String URL_ENCODING = "UTF-8";
	private static String ENCODING_ALGORITHM = "HmacSHA1";
	private static String timeZone = "GMT";
	
	private static String customer_id = "M013883003"; //tp code
	private static String org_code = "D1"; //org_code path
	private static String org_level ="1";
	private static String application_name = "CTB.COM"; 
	private static String user_role = "Admin";
	private static String user_regular = "Regular";
	private static String user_name ="istep_admin_prism";
	
	private static String theme ="inors";
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		DigitalMeasuresHMACQueryStringBuilder queryStringBuilder = new DigitalMeasuresHMACQueryStringBuilder(SECRET_KEY, 60, ENCODING_ALGORITHM);
		queryStringBuilder.setTimeZone(timeZone);
		queryStringBuilder.setENCODING_ALGORITHM(ENCODING_ALGORITHM);
		queryStringBuilder.setURL_ENCODING(URL_ENCODING);
	
		String requestParam = queryStringBuilder.buildAuthenticatedQueryString(customer_id, org_code, org_level, application_name, user_role, user_name, theme);
		System.out.println(REQUEST_URL + "?" + requestParam);
		
	//	requestWithQueryString(requestParam);
		
	//	invalidRequestWithQueryString(requestParam.replace("validUntil=2013", "validUntil=2012"));
	
	}
	
	private static void requestWithQueryString(String queryString) {
		String URL = REQUEST_URL + "?" + queryString;
		HttpHeaders  headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_XML);
		HttpEntity req = new HttpEntity(headers);
	    RestTemplate rest = new RestTemplate();
	    System.out.println( rest.postForLocation(URL, req) );
	}
	
	private static void invalidRequestWithQueryString(String queryString) {
		String URL = REQUEST_URL + "?" + queryString;
		HttpHeaders  headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_XML);
		HttpEntity req = new HttpEntity(headers);
	    RestTemplate rest = new RestTemplate();
	    System.out.println( rest.postForLocation(URL, req) );
	}
	
	private static void requestWithHMACinHeader() {
	        String URL = REQUEST_URL;
	        
	        HttpHeaders  headers = new HttpHeaders();
	        headers.setContentType(MediaType.TEXT_XML);
	        headers.add("apikey", apykey);
	        headers.add("validUntil", "2013-10-11T14%3A17%3A01Z");
	        headers.add("ipAddress", IP);
	        headers.add("signature", "yimYEWLSCaI1Dj4KlcfbmNZeg3E=");
	        
	        HttpEntity req = new HttpEntity(headers);
	        RestTemplate rest = new RestTemplate();
	        System.out.println( rest.postForLocation(URL, req) );
	        
	}

        

}
