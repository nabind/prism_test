package com.vaannila.DAO;

import java.sql.SQLException;
import java.util.Properties;

import com.vaannila.util.PropertyFile;
import com.vaannila.util.JDCConnectionDriver;


public class LoadConnectionProvider {
	private static JDCConnectionDriver driver = null;
	private static String DATA_SOURCE = "jdbc:jdc:acsi";
	
	private LoadConnectionProvider() {}
	
	private static void loadDriver(Properties prop) throws Exception {
		try {
			String dbURL = prop.getProperty("dbURL"); 
			String dbUserName = prop.getProperty("dbUserName"); 
			String dbPassword = prop.getProperty("dbPassword"); 
			
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
	
	public static JDCConnectionDriver getDriver() throws Exception {
		if(driver == null) {
			Properties prop = PropertyFile.loadProperties("nd.properties");
			loadDriver(prop);
		}
		return driver;
	}
	
}
