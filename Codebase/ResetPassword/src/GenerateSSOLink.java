import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class GenerateSSOLink {
	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	/**
	 * @param args
	 */
	public static String generateLink(String username, String customerId, String orgNodeCodePath, String orgNodeLevel, boolean admin) {
		String generatedUrl = "";
		String role = admin ? "Admin" : "User";
		try {
			 System.out.println("-------------------------------------------------");
			 String param = "customer_id="+customerId+"&org_node_code="+orgNodeCodePath+"&hierarchy_level="+orgNodeLevel+"&application_name=CTB.COM&time_stamp=2015-11-26T00%3A42%3A45Z&user_role="+role+"&user_name="+username;
			 String signature = DigitalMeasuresHMACQueryStringBuilder.getSignature(param, "CTB");
			 String perfUrl = "https://app1.ctb.com";
			 generatedUrl = perfUrl+"/prism/reports.do?" + param + "&signature=" + signature + "&project=inors";
			 System.out.println(generatedUrl);
			 System.out.println("-------------------------------------------------");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR .......!......!......... ");
		} 
		return generatedUrl;
	}
	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		String sql = "select 'usr' || org_nodeid, customerid, substr(org_node_code_path, 3), org_node_level from org_node_dim where org_node_level in (1,2,3) order by org_node_level";
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		FileWriter writer = null;
		FileWriter writerReg = null;
		try {
			writer = new FileWriter("/opt/tomcat1/inors-AdminSSO.csv");
			writerReg = new FileWriter("/opt/tomcat1/inors-RegularSSO.csv");
			writer.append("Org Node Level");
			writer.append(',');
			writer.append("SSO URL");
			writer.append('\n');

			writerReg.append("Org Node Level");
			writerReg.append(',');
			writerReg.append("SSO URL");
			writerReg.append('\n');

			Class.forName(JDBC_DRIVER);
			System.out
					.println("-------------------------------------------------");
			System.out.println("Connecting to database...");
			String connectionUrl = "jdbc:oracle:thin:@10.160.23.38:1521:istepsa";
			//String connectionUrl = "jdbc:oracle:thin:@10.160.23.70:1521:ehs2clqa";
			String dbUsername = "istepperf";
			String dbPassword = "istepperf14perf";
			//String dbUsername = "istep";
			//String dbPassword = "istep14qa";
			System.out.println("  creating connection with url :"
					+ connectionUrl);
			System.out.println("  db username :" + dbUsername);
			System.out.println("  db password :" + dbPassword);

			conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
			System.out.print("Do you really want to proceed (y/n)?");
			String userInput = reader.nextLine();
			if ("y".equalsIgnoreCase(userInput)) {
				System.out.println("Fetching organizations ...");
				pstmt = conn.prepareCall(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String username = rs.getString(1);
					String customerId = "461182";//rs.getString(2);
					String orgNodeCodePath = rs.getString(3);
					String orgNodeLevel = rs.getString(4);
					String generatedUrl = generateLink(username, customerId, URLEncoder.encode(orgNodeCodePath, "UTF-8"), orgNodeLevel, true);

					writer.append(orgNodeLevel);
					writer.append(',');
					writer.append(generatedUrl);
					writer.append('\n');

					String generatedRegularUrl = generateLink(username, customerId, URLEncoder.encode(orgNodeCodePath, "UTF-8"), orgNodeLevel, false);

					writerReg.append(orgNodeLevel);
					writerReg.append(',');
					writerReg.append(generatedRegularUrl);
					writerReg.append('\n');
				}
			} else {
				System.out.println("Exiting ...");
			}
			System.out.println("-------------------------------------------------");
			System.out.println("Successfully completed .... exiting. ");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR .......!......!......... ");
		} finally {
			try { if(pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if(conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { writer.flush(); } catch (IOException e) { e.printStackTrace(); }
			try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
			try { writerReg.flush(); } catch (IOException e) { e.printStackTrace(); }
			try { writerReg.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}

}
