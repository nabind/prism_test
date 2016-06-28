import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ResetPassword {

	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String userNameParam = "username";
		String userName = "1";
		String pwd = "Passwd12";
		
		// TODO Auto-generated method stub
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter the DB HOST URL (default -> 10.160.18.38):");
		String host=reader.nextLine();
		if(host.isEmpty()) host = "10.160.18.38";
		
		System.out.print("Enter the DB PORT (default -> 1521):");
		String port=reader.nextLine();
		if(port.isEmpty()) port = "1521";
		
		System.out.print("Enter the DB Username (default -> wisconsin):");
		String username=reader.nextLine();
		if(username.isEmpty()) username = "wisconsin";
		
		System.out.print("Enter the DB Password (default -> wisconsin16prod):");
		String password=reader.nextLine();
		if(password.isEmpty()) password = "wisconsin16prod";
		
		System.out.print("Enter the DB SID (default -> INDPROD):");
		String sid=reader.nextLine();
		if(sid.isEmpty()) sid = "INDPROD";
		
		System.out.print("Reset passowrd for all users? (y/n) (default -> n):");
		String allusers=reader.nextLine();
		if(allusers.isEmpty()) allusers = "n";
		
		if("y".equalsIgnoreCase(allusers)) {
			userNameParam = "1";
			System.out.print("Enter the password you want to set for all users (press enter to use default):");
			if(reader.nextLine().isEmpty()) System.out.println("Password not provided, using the default one.");
			pwd = (reader.nextLine().isEmpty() ? pwd : reader.nextLine());
		} else {
			System.out.print("Enter username for which you want to reset password:");
			userName = reader.nextLine();
			if(userName.isEmpty()) {
				System.out.println("Username not provided ... exiting!!");
				System.exit(0);
			}
			System.out.print("Enter the password you want to set for '"+userName+"' (press enter to use default):");
			pwd = reader.nextLine();
			if(pwd.isEmpty()) {
				System.out.println("Password not provided, using the default one.");
				pwd = "Passwd12";
			}
		}
		
		String sql = "select userid, username from users where "+userNameParam+" = ? ";//and trunc(updated_date_time) <> trunc(sysdate)";
		String sqlbatch = "select userid, username from users where trunc(updated_date_time) <> trunc(sysdate) and rownum < 500";
		//String sqlbatch = "select userid, username from users where userid in ( 6567,6568,6569,6570,6571,6572,6567,6569,6570) and  trunc(updated_date_time) <> trunc(sysdate)";
		
		String sqlParent = "select u.userid, u.username from users u,user_role ur where u.userid = ur.userid and roleid =6 and "+userNameParam+" = ? and trunc(updated_date_time) <> trunc(sysdate)";
		String update = "update users set password = ?,salt =?,is_firsttime_login='N',updated_date_time=sysdate where userid = ? ";
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
			 
			 conn = DriverManager.getConnection("jdbc:oracle:thin:@//"+host+":"+port+"/"+sid+"", username, password);
			 System.out.print("Do you really want to proceed (y/n)?");
			 String userInput = reader.nextLine();
			 
		//	 while(true){
				 if("y".equalsIgnoreCase(userInput)) {
					 System.out.println("Fetching user(s) ...");
					 
					 if("y".equalsIgnoreCase(allusers)) {
						 pstmt = conn.prepareCall(sqlbatch); //For Al user
					 } else {
						 pstmt = conn.prepareCall(sql);   //For single user
						 pstmt.setString(1, userName);
					 }
					// pstmt = conn.prepareCall(sqlParent); //For parent
					 
					 rs = pstmt.executeQuery();
					 int count =0;
					 UserTO userTO = null;
					 List<UserTO> userList = new ArrayList<UserTO>();
					 while (rs.next()) {
						 userTO = new UserTO();
						 userTO.setUserid(rs.getString(1));
						 userTO.setUserName(rs.getString(2));
						 userList.add(userTO);
						 System.out.println("count:"+ count++);
					 }
					 if(userList.size()==0){//to break the infinite loop
						 //break;
						 System.out.println("No data found");
					 }
					 
					 count =0;
					 pst = conn.prepareCall(update);
					 for( UserTO user : userList){
						 String userId = user.getUserid();
						 String usr = user.getUserName();
						 
						 String salt =  PasswordGenerator.getNextSalt();//rs.getString(3);
						 String encPass = SaltedPasswordEncoder.encryptPassword(pwd, usr.toLowerCase() + salt);
						 System.out.println("updating user .... " + usr +" with password ... " + pwd);
						 pst.setString(1, encPass);
						 pst.setString(2, salt);
						 pst.setString(3, userId);
						 pst.addBatch();
						 count = count+1;						 
						 System.out.println(count + " users Updated"  );						 
						 }	
					 pst.executeBatch();
					 pst.close();
				 } else {
					 System.out.println("Exiting ...");
				 }
		//	 }
			 
			 
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
