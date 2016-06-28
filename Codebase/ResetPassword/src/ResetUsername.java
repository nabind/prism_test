import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class ResetUsername {

	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String userNameParam = "username";
		String userName = "1";
		String pwd = "9rc^wH7KRg[B";
		
		// TODO Auto-generated method stub
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter the DB HOST URL (default -> 10.160.23.70):");
		String host=reader.nextLine();
		if(host.isEmpty()) host = "10.160.23.70";
		
		System.out.print("Enter the DB PORT (default -> 1521):");
		String port=reader.nextLine();
		if(port.isEmpty()) port = "1521";
		
		System.out.print("Enter the DB Username (default -> istep_qa):");
		String username=reader.nextLine();
		if(username.isEmpty()) username = "istep_qa";
		
		System.out.print("Enter the DB Password (default -> istep_qa):");
		String password=reader.nextLine();
		if(password.isEmpty()) password = "istep_qa";
		
		System.out.print("Enter the DB SID (default -> ehs2clqa):");
		String sid=reader.nextLine();
		if(sid.isEmpty()) sid = "ehs2clqa";
		/*
		System.out.print("Reset username for all SSO users? (y/n) (default -> y):");
		String allusers=reader.nextLine();
		if(allusers.isEmpty()) allusers = "y";
		
		if("y".equalsIgnoreCase(allusers)) {
			userNameParam = "1";
		} else {
			System.out.print("Enter SSO username for which you want to reset username:");
			userName = reader.nextLine();
			if(userName.isEmpty()) {
				System.out.println("Username not provided ... exiting!!");
				System.exit(0);
			}
		}
		*/
		//String sql = "SELECT (substr(username, 0, INSTR(username,tp_code)-2)) || o.org_nodeid || '9rc^wH7KRg[B' as u_name, u.userid, username,salt FROM users u, test_program, org_users o where o.userid = u.userid and u.Username LIKE '%'||tp_code||'%' and username like '%rc^%'";
		String sql = "SELECT username, u.userid, username, salt FROM users u where username like '%9rc^%'";
		//String sql = "SELECT username, u.userid, salt FROM users u where username like '%rc^%'";
		String update = "update users set password = ? where userid = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		long count = 0;
		try {
			 Class.forName(JDBC_DRIVER);
			 System.out.println("-------------------------------------------------");
			 System.out.println("Connecting to database...");
			 String connectionUrl = "jdbc:oracle:thin:@"+host+":"+port+":"+sid+"";
			 System.out.println("  creating connection with url :" + connectionUrl);
			 System.out.println("  db username :" + username);
			 System.out.println("  db password :" + password);
			 
			 conn = DriverManager.getConnection("jdbc:oracle:thin:@"+host+":"+port+":"+sid+"", username, password);
			 System.out.print("Do you really want to proceed (y/n)?");
			 String userInput = reader.nextLine();
			 if("y".equalsIgnoreCase(userInput)) {
				 System.out.println("Fetching user(s) ...");
				 pstmt = conn.prepareCall(sql);
				 //pstmt.setString(1, userName);
				 rs = pstmt.executeQuery();
				 conn.setAutoCommit(false);
				 while (rs.next()) {
					 String usr = rs.getString(1);
					 String userid = rs.getString(2);
					 String uname = rs.getString(3);
					 String salt = rs.getString(4);
					 usr = (usr.length() > 30)? usr.substring(0, 30) : usr;
					 //String salt = uname; //PasswordGenerator.getNextSalt();
					 String encPass = SaltedPasswordEncoder.encryptPassword(pwd, usr.toLowerCase() + salt);
					 System.out.println("updating user "+ ++count +" .... " + uname +" with username ... " + usr);
					 pst = conn.prepareCall(update);
					 //pst.setString(1, usr);
					 pst.setString(1, encPass);
					 pst.setString(2, userid);
					 pst.executeUpdate();
					 
					 pst.close();
				 }
			 } else {
				 System.out.println("Exiting ...");
			 }
			 conn.commit();
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
		}
		
	}

}
