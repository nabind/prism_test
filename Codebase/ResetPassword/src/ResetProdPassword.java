import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class ResetProdPassword {

	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String userNameParam = "username";
		String userName = "1";
		String pwd = "Passwd12";
		
		String username = "TASC";
		String password = "tasc14pprd";
		
		// TODO Auto-generated method stub
		Scanner reader = new Scanner(System.in);
		
		System.out.print("Enter username for which you want to reset password:");
		userName = reader.nextLine();
		if(userName.isEmpty()) {
			System.out.println("Username not provided ... exiting!!");
			System.exit(0);
		}
		System.out.print("Enter the password you want to set for '"+userName+"' (press enter to use default --> Passwd12):");
		pwd = reader.nextLine();
		if(pwd.isEmpty()) {
			System.out.println("Password not provided, using the default one.");
			pwd = "Passwd12";
		}
		
		
		String sql = "select userid, username, salt from users where "+userNameParam+" = ?";
		String update = "update users set password = ? where userid = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			 Class.forName(JDBC_DRIVER);
			 System.out.println("-------------------------------------------------");
			 System.out.println("Connecting to database...");
			 String connectionUrl = "jdbc:oracle:thin:@10.160.18.30:1521:ehs2clqa";
			 System.out.println("  creating connection with url :" + connectionUrl);
			 System.out.println("  db username :" + username);
			 System.out.println("  db password :" + password);
			 
			 conn = DriverManager.getConnection(connectionUrl, username, password);
			 System.out.print("Do you really want to proceed (y/n)?");
			 String userInput = reader.nextLine();
			 if("y".equalsIgnoreCase(userInput)) {
				 System.out.println("Fetching user(s) ...");
				 pstmt = conn.prepareCall(sql);
				 pstmt.setString(1, userName);
				 rs = pstmt.executeQuery();
				 while (rs.next()) {
					 String userId = rs.getString(1);
					 String usr = rs.getString(2);
					 String salt = rs.getString(3);
					 String encPass = SaltedPasswordEncoder.encryptPassword(pwd, usr.toLowerCase() + salt);
					 System.out.println("updating user .... " + usr +" with password ... " + pwd);
					 pst = conn.prepareCall(update);
					 pst.setString(1, encPass);
					 pst.setString(2, userId);
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
