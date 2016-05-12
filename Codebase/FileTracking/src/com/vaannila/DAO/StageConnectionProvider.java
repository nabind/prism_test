package com.vaannila.DAO;

import java.sql.SQLException;
import java.util.Properties;

import com.vaannila.util.PropertyFile;
import com.vaannila.util.JDCConnectionDriver;


public class StageConnectionProvider {
	private static JDCConnectionDriver driver = null;
	private static String DATA_SOURCE = "jdbc:jdc:acsi";
	
	private StageConnectionProvider() {}
	
	private static void loadDriver(Properties prop) throws Exception {
		try {
			String dbURL = prop.getProperty("dbStageURL"); 
			String dbUserName = prop.getProperty("dbStageUserName"); 
			String dbPassword = prop.getProperty("dbStagePassword"); 
			
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
			Properties prop = PropertyFile.loadProperties("acsi.properties");
			loadDriver(prop);
		}
		return driver;
	}
	
}
