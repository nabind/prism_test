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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ctb.prism.login.security.provider.AuthenticatedUser;

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
	private JdbcTemplate jdbcTemplateInors;
	
	/**
	 * JDBC template points to tasc database
	 */
	@Autowired
	private JdbcTemplate jdbcTemplateTasc;

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
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		if(currentAuth != null) {
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) currentAuth.getPrincipal();
			return getJdbcTemplatePrism(authenticatedUser.getContractName());
		}
		return null;
	}
	
	public JdbcTemplate getJdbcTemplatePrism(String contractName) {
		if("inors".equals(contractName)) return jdbcTemplateInors;
		if("tasc".equals(contractName)) return jdbcTemplateTasc;
		else return null;
	}
	
	
	/**
	 * @return the jdbcTemplate to access INORS database
	 */
	public JdbcTemplate getJdbcTemplateInors() {
		return jdbcTemplateInors;
	}
	
	/**
	 * @return the jdbcTemplate to access TASC database
	 */
	public JdbcTemplate getJdbcTemplateTasc() {
		return jdbcTemplateTasc;
	}

	/**
	 * @return the connection object for prism DB
	 * @throws SQLException
	 */
	public Connection getPrismConnection() throws SQLException {
		return getJdbcTemplatePrism().getDataSource().getConnection();
	}
	
	/**
	 * @return the connection object for tasc DB
	 * @throws SQLException
	 */
	public Connection getTascConnection() throws SQLException {
		return getJdbcTemplateTasc().getDataSource().getConnection();
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
