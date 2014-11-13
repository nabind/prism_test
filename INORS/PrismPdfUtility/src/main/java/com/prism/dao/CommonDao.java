package com.prism.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public abstract class CommonDao {
	private static final Logger logger = Logger.getLogger(CommonDao.class);

	protected void releaseResources(Connection conn, Statement stmt, ResultSet rs) {
		try {
			rs.close();
		} catch (Exception e) {
			logger.warn("Not able to close ResultSet");
		}
		try {
			stmt.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			conn.close();
		} catch (Exception e) {
			logger.warn("Not able to close Connection");
		}
	}

	protected void releaseResources(Connection conn, Statement stmt, Statement stmt1) {
		try {
			stmt1.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			stmt.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			conn.close();
		} catch (Exception e) {
			logger.warn("Not able to close Connection");
		}
	}

	protected void releaseResources(Connection conn, Statement stmt, Statement stmt1, Statement stmt2) {
		try {
			stmt2.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			stmt1.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			stmt.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			conn.close();
		} catch (Exception e) {
			logger.warn("Not able to close Connection");
		}
	}

	protected void releaseResources(Connection conn, Statement stmt) {
		try {
			stmt.close();
		} catch (Exception e) {
			logger.warn("Not able to close Statement");
		}
		try {
			conn.close();
		} catch (Exception e) {
			logger.warn("Not able to close Connection");
		}
	}
}