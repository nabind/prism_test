import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class ResetParentPassword {

	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String userNameParam = "username";
		String userName = "1";
		String pwd = "Pn2014";
		
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
		
		System.out.print("Reset passowrd for all users? (y/n) (default -> n):");
		String allusers=reader.nextLine();
		if(allusers.isEmpty()) allusers = "n";
		
		if("y".equalsIgnoreCase(allusers)) {
			userNameParam = "1";
			System.out.print("Resetting password of all parent users to default one:");
		} else {
			System.out.print("Enter username for which you want to reset password:");
			userName = reader.nextLine();
			if(userName.isEmpty()) {
				System.out.println("Username not provided ... exiting!!");
				System.exit(0);
			}
		}
		
		String sql = "select u.userid, username from users u, user_role r where "+userNameParam+" = ? and u.userid = r.userid " +
				" and r.roleid = 6 "; // parent users only
		String update = "update users set salt = ?, password = ?, is_firsttime_login = 'Y' where userid = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
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
				 System.out.println("Fetching parent user(s) ...");
				 pstmt = conn.prepareCall(sql);
				 pstmt.setString(1, userName);
				 rs = pstmt.executeQuery();
				 while (rs.next()) {
					 String userId = rs.getString(1);
					 String usrname = rs.getString(2);
					 String salt = PasswordGenerator.getNextSalt();
					 String passwd = usrname.substring(0,3) + pwd;
					 String encPass = SaltedPasswordEncoder.encryptPassword(passwd, usrname.toLowerCase() + salt);
					 System.out.println("updating parent user .... " + usrname +" with password ... " + passwd);
					 pst = conn.prepareCall(update);
					 pst.setString(1, salt);
					 pst.setString(2, encPass);
					 pst.setString(3, userId);
					 pst.executeUpdate();
					 
					 pst.close();
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
		}
		
	}

}
