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
	 * JDBC template points to missouri database
	 */
	@Autowired
	private JdbcTemplate jdbcTemplateUsmo;
	
	/**
	 * JDBC template points to wisconsin database
	 */
	@Autowired
	private JdbcTemplate jdbcTemplateWisc;

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
		//logger.info("BaseDAO.getJdbcTemplatePrism(), contractName = " + contractName);
		if("inors".equals(contractName)) return jdbcTemplateInors;
		if("tasc".equals(contractName)) return jdbcTemplateTasc;
		if("usmo".equals(contractName)) return jdbcTemplateUsmo;
		if("wisc".equals(contractName)) return jdbcTemplateWisc;
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
	 * @return the jdbcTemplate to access MISSOURI database
	 */
	public JdbcTemplate getJdbcTemplateUsmo() {
		return jdbcTemplateUsmo;
	}
	
	/**
	 * @return the jdbcTemplate to access WISCONSIN database
	 */
	public JdbcTemplate getJdbcTemplateWisc() {
		return jdbcTemplateWisc;
	}

	/**
	 * @return the connection object for prism DB
	 * @throws SQLException
	 */
	public Connection getPrismConnection() throws SQLException {
		return getJdbcTemplatePrism().getDataSource().getConnection();
	}
	
	/**
	 * @author Joy
	 * @return the connection object for prism DB
	 * @throws SQLException
	 */
	public Connection getPrismConnection(String contractName) throws SQLException {
		return getJdbcTemplatePrism(contractName).getDataSource().getConnection();
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
