/**
 * 
 */
package com.ctb.prism.core.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author TCS-1
 * 
 */
public abstract class BaseDAO {

	/**
	 * logger available to subclasses
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * JDBC template points to jasper server database
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * JDBC template points to prism database
	 */
	@Autowired
	private JdbcTemplate jdbcTemplatePrism;

	/**
	 * @return the jdbcTemplate to access jasper server database
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @return the jdbcTemplate to access prism database
	 */
	public JdbcTemplate getJdbcTemplatePrism() {
		return jdbcTemplatePrism;
	}

	/**
	 * @return the connection object for prism DB
	 * @throws SQLException
	 */
	public Connection getPrismConnection() throws SQLException {
		return getJdbcTemplatePrism().getDataSource().getConnection();
	}

	// @Autowired
	// public void setDataSource(DataSource dataSource) {
	// this.jdbcTemplate = new JdbcTemplate(dataSource);
	// }

	// @Autowired
	// public void setDataSourcePrism(DataSource dataSource) {
	// this.jdbcTemplatePrism = new JdbcTemplate(dataSource);
	// }

}
