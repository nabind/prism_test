/**
 * 
 */
package com.ctb.prism.core.dao;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.jaspersoft.mongodb.connection.MongoDbConnection;
import com.jaspersoft.mongodb.connection.MongoDbConnectionManager;



/**
 * @author TCS-1
 * 
 */
public abstract class BaseDAO {

	/**
	 * logger available to subclasses
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	
	@SuppressWarnings("deprecation")
	@Autowired
	private MongoFactoryBean mongo;
	
	@Autowired
	private MongoTemplate mongoTemplateInors;
	@Autowired
	private MongoTemplate mongoTemplateTasc;
	@Autowired
	private MongoTemplate mongoTemplateUsmo;
	public MongoOperations getMongoTemplatePrism() {
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		if(currentAuth != null) {
			AuthenticatedUser authenticatedUser = (AuthenticatedUser) currentAuth.getPrincipal();
			return getMongoTemplatePrism(authenticatedUser.getContractName());
		}
		return null;
	}
	
	//@Produces
	public MongoOperations getMongoTemplatePrism(String contractName) {
		logger.info("BaseDAO.getJdbcTemplatePrism(), contractName = " + contractName);
		if("inors".equals(contractName)) return mongoTemplateInors;
		if("tasc".equals(contractName)) return mongoTemplateTasc;
		if("usmo".equals(contractName)) return mongoTemplateUsmo;
		else return null;
	}
	
	/**
	 * @return the connection object for prism DB
	 * @throws SQLException
	 */
	public MongoDbConnection getPrismMongoConnection(String contractName){
		logger.info("BaseDAO.getJdbcTemplatePrism(), contractName = " + contractName);
		/*if("inors".equals(contractName)) return new MongoDbConnection(mongo, "inors");
		if("tasc".equals(contractName)) return new MongoDbConnection(mongo, "tasc");
		if("usmo".equals(contractName)) return new MongoDbConnection(mongo, "usmo");*/
		
		MongoDbConnection connection = null;
		try {		
		String mongoURI = mongo.getObject().getAddress().toString();
		
		if("inors".equals(contractName))  mongoURI = "mongodb://"+ mongoURI +"/inors";
        //if("tasc".equals(contractName))  mongoURI = "mongodb://"+ mongoURI +"/tasc"; 
        if("tasc".equals(contractName))  mongoURI = "mongodb://"+ mongoURI +"/drc_mongo";
        if("usmo".equals(contractName)) mongoURI = "mongodb://"+ mongoURI  +"/usmo";
        
        
        	connection = new MongoDbConnection(mongoURI, null, null);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
     }

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
