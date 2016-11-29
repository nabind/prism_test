package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.vaannila.util.JDCConnectionDriver;


public class RepoDAOImpl {
	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:acsi";
	public RepoDAOImpl(Properties prop) throws Exception {
		try {
			String dbURL = prop.getProperty("dbRepoURL"); 
			String dbUserName = prop.getProperty("dbRepoUserName"); 
			String dbPassword = prop.getProperty("dbRepoPassword"); 
			
			driver = new JDCConnectionDriver("oracle.jdbc.driver.OracleDriver"
					, dbURL, dbUserName, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	
	
}
