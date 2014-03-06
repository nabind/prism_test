package com.ctb.prism.login.security.test;

import java.io.FileWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.ctb.prism.login.security.encoder.DigitalMeasuresHMACQueryStringBuilder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class SSOTest {
	
	private static String REQUEST_URL = "http://10.160.23.51:8080/inors";
	private static String REQUEST_PATH = "/reports.do?";
	
	private static String IP = "127.0.0.1";
	private static String SECRET_KEY =  "BTCguSF49hYaPmAfe9Q29LtsQ2X";
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
	
	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	private static SecretKey secretKey;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Mac messageAuthenticationCode = Mac.getInstance(ENCODING_ALGORITHM);
		secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ENCODING_ALGORITHM);
		messageAuthenticationCode.init(secretKey);
		
		FileWriter writerAdmin = new FileWriter("C:\\Temp\\AdminSSO.csv");
		FileWriter writerRegular = new FileWriter("C:\\Temp\\RegularSSO.csv");
		
		String message = "";
		String sql = "SELECT 'customer_id=461182|org_node_code=' || 'org_node_code_path' || "+
					 " '|hierarchy_level=' || org_node_level ||  "+
					 " '|application_name=CTB.COM|time_stamp=2014-07-07T18%3A45%3A34Z|user_role=Admin' || "+
					 " '|user_name=usr' || org_nodeid , org_node_code_path,ORG_NODE_NAME, ORG_NODE_LEVEL"+
					 " FROM org_node_dim "+
					 " WHERE org_node_level IN (1, 2, 3) order by org_node_level";
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			
			writerAdmin.append("Organization Name");
			writerAdmin.append(",");
			writerAdmin.append("Organization Type");
			writerAdmin.append(",");
			writerAdmin.append("URL");
			writerAdmin.append('\n');
			
			writerRegular.append("Organization Name");
			writerRegular.append(",");
			writerRegular.append("Organization Type");
			writerRegular.append(",");
			writerRegular.append("URL");
			writerRegular.append('\n');
			
			Class.forName(JDBC_DRIVER);
			String connectionUrl = "jdbc:oracle:thin:@10.160.23.70:1521:ehs2clqa";
			conn = DriverManager.getConnection(connectionUrl, "istep_qa", "istep_qa");
			pstmt = conn.prepareCall(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				message = rs.getString(1);
				String org_path = rs.getString(2);
				org_path = org_path.substring(2);
				message = message.replaceAll("\\|", "&").replace("org_node_code_path", URLEncoder.encode(org_path, "UTF-8"));
				// Admin user
				messageAuthenticationCode.update(message.getBytes());
				byte[] digest = messageAuthenticationCode.doFinal();
				String sig = Base64.encode(digest);
				System.out.println(REQUEST_URL+REQUEST_PATH + message + "&signature=" + URLEncoder.encode(sig, "UTF-8"));
				
				writerAdmin.append(rs.getString(3));
				writerAdmin.append(",");
				writerAdmin.append(orgType(rs.getInt(4)));
				writerAdmin.append(",");
				writerAdmin.append(REQUEST_PATH + message + "&signature=" + URLEncoder.encode(sig, "UTF-8"));
				writerAdmin.append('\n');
				
				// regular user
				message = message.replaceAll("Admin", "Regular");
				messageAuthenticationCode.update(message.getBytes());
				digest = messageAuthenticationCode.doFinal();
				sig = Base64.encode(digest);
				System.out.println(REQUEST_URL+REQUEST_PATH + message + "&signature=" + URLEncoder.encode(sig, "UTF-8"));
				
				writerRegular.append(rs.getString(3));
				writerRegular.append(",");
				writerRegular.append(orgType(rs.getInt(4)));
				writerRegular.append(",");
				writerRegular.append(REQUEST_PATH + message + "&signature=" + URLEncoder.encode(sig, "UTF-8"));
				writerRegular.append('\n');
			}
			writerAdmin.flush();
			writerRegular.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR .......!......!......... ");
		} finally {
			try { if(pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			
			try { if(writerAdmin != null) writerAdmin.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(writerRegular != null) writerRegular.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		
		
		
		
		
		
		/*
		DigitalMeasuresHMACQueryStringBuilder queryStringBuilder = new DigitalMeasuresHMACQueryStringBuilder(SECRET_KEY, 60*24*60*60, ENCODING_ALGORITHM);
		queryStringBuilder.setTimeZone(timeZone);
		queryStringBuilder.setENCODING_ALGORITHM(ENCODING_ALGORITHM);
		queryStringBuilder.setURL_ENCODING(URL_ENCODING);
	
		String requestParam = queryStringBuilder.buildAuthenticatedQueryString(customer_id, org_code, org_level, application_name, user_role, user_name);
		System.out.println(REQUEST_URL + "?" + requestParam);*/
		
		//requestWithQueryString(requestParam);
		
	//	invalidRequestWithQueryString(requestParam.replace("validUntil=2013", "validUntil=2012"));
	
	}
	
	private static String orgType(int orgLevel) {
		if(orgLevel == 1) return "State";
		if(orgLevel == 2) return "District/Corp";
		if(orgLevel == 3) return "School";
		if(orgLevel == 4) return "Class";
		return "";
	}
	
	private static void requestWithQueryString(String queryString) {
		String URL = REQUEST_URL + "?" + queryString;
		HttpHeaders  headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity req = new HttpEntity(headers);
	    RestTemplate rest = new RestTemplate();
	    System.out.println( rest.postForLocation(URL, req) );
	}
	
	private static void invalidRequestWithQueryString(String queryString) {
		String URL = REQUEST_URL + "?" + queryString;
		HttpHeaders  headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity req = new HttpEntity(headers);
	    RestTemplate rest = new RestTemplate();
	    System.out.println( rest.postForLocation(URL, req) );
	}
	
	private static void requestWithHMACinHeader() {
	        String URL = REQUEST_URL;
	        
	        HttpHeaders  headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.add("apikey", customer_id);
	        headers.add("validUntil", "2013-10-11T14%3A17%3A01Z");
	        headers.add("ipAddress", IP);
	        headers.add("signature", "yimYEWLSCaI1Dj4KlcfbmNZeg3E=");
	        
	        HttpEntity req = new HttpEntity(headers);
	        RestTemplate rest = new RestTemplate();
	        System.out.println( rest.postForLocation(URL, req) );
	        
	}

        
}
