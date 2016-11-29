package com.ctb.prism.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Interacts with the database.
 * 
 * @author 516615
 */
public class DbHandler
{
	/*private static final String url = "jdbc:oracle:thin:@//10.160.23.70:1521/ehs2clqa";
	private static final String userName = "DEV_ACSI_PRISM";
	private static final String password = "devacsi";

	static
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	*//**
	 * Returns connection object
	 * 
	 * @return
	 *//*
	public static Connection getConnection()
	{
		Connection con = null;
		try
		{
			con = DriverManager.getConnection(url, userName, password);
			con.setAutoCommit(false);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return con;
	}*/
}
