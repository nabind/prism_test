package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.*;
import javax.sql.DataSource;

public class BaseDAO {
	static String DATA_SOURCE_TASC = "jdbc:jdc:tasc";
	static String DATA_SOURCE_USMO = "jdbc:jdc:usmo";
	
	public static Connection connect(String dataSource) throws NamingException, SQLException{
		Context initContext = new InitialContext();
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		DataSource ds = null;
		if(DATA_SOURCE_TASC.equals(dataSource)){
			ds = (DataSource)envContext.lookup("jdbc/tasc");
		}else if(DATA_SOURCE_USMO.equals(dataSource)){
			ds = (DataSource)envContext.lookup("jdbc/usmo");
		}
		Connection conn = ds.getConnection();
		return conn;
	}

}
