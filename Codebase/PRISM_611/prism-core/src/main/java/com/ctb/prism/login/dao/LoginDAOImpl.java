package com.ctb.prism.login.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.ObjectValueTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.PasswordGenerator;
import com.ctb.prism.core.util.SaltedPasswordEncoder;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.MReportTO;
import com.ctb.prism.login.transferobject.MResultTO;
import com.ctb.prism.login.transferobject.MUserTO;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;

@Repository
public class LoginDAOImpl extends BaseDAO implements ILoginDAO{

	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(LoginDAOImpl.class.getName());
	
	/**
	 * This method is used to return user authorities
	 * @param username
	 * @return
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String username) {
		return getGrantedAuthorities(username, 0, "O");
	}

	/**
	 * 
	 * @param username
	 * @param orgLevel
	 * @param userType
	 * @return
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String username, long orgLevel, String userType) {
		List<GrantedAuthority> userPerms = null;

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
			userPerms = getJdbcTemplatePrism().query(IQueryConstants.SELECT_EDU_USER_AUTHORITIES, new String[] { username },
					new RowMapper<GrantedAuthority>() {
						public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new SimpleGrantedAuthority(rs.getString(1));
						}
					});

			userPerms.add(new SimpleGrantedAuthority(CustomStringUtil.appendString(
					IApplicationConstants.ROLE_INIT,
					"LEVEL",
					"" + IApplicationConstants.DEFAULT_LEVELID_VALUE
			)));
		} else {
			userPerms = getJdbcTemplatePrism().query(IQueryConstants.SELECT_USER_AUTHORITIES, new String[] { username/*, String.valueOf(orgLevel)*/ },
					new RowMapper<GrantedAuthority>() {
						public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new SimpleGrantedAuthority(rs.getString(1));
						}
					});
			userPerms.add(new SimpleGrantedAuthority(CustomStringUtil.appendString(
					IApplicationConstants.ROLE_INIT,
					"LEVEL",
					"" + orgLevel
			)));
		}
		return userPerms;
	}
	
	/**
	 * 
	 * @param username
	 * @param orgLevel
	 * @param userType
	 * @return
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String username, long orgLevel, String userType, String contractName) {
		List<GrantedAuthority> userPerms = null;

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
			userPerms = getJdbcTemplatePrism(contractName).query(IQueryConstants.SELECT_EDU_USER_AUTHORITIES, new String[] { username },
					new RowMapper<GrantedAuthority>() {
						public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new SimpleGrantedAuthority(rs.getString(1));
						}
					});

			userPerms.add(new SimpleGrantedAuthority(CustomStringUtil.appendString(
					IApplicationConstants.ROLE_INIT,
					"LEVEL",
					"" + IApplicationConstants.DEFAULT_LEVELID_VALUE
			)));
		} else {
			userPerms = getJdbcTemplatePrism(contractName).query(IQueryConstants.SELECT_USER_AUTHORITIES, new String[] { username/*, String.valueOf(orgLevel)*/ },
					new RowMapper<GrantedAuthority>() {
						public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new SimpleGrantedAuthority(rs.getString(1));
						}
					});
			userPerms.add(new SimpleGrantedAuthority(CustomStringUtil.appendString(
					IApplicationConstants.ROLE_INIT,
					"LEVEL",
					"" + orgLevel
			)));
		}
		return userPerms;
	}
	
	/**
	 * This method is used to return user login status (is first time login)
	 * @param username
	 * @return
	 */
	public String checkFirstTimeLogin(String username) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.CHECK_LOGIN_TYPE, new Object[]{ username }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return (rs.getString(1));
			}
		});
	}
	
	/**
	 * Retrieves and returns tenantId corresponding to the userName
	 * @param userName
	 * @return tenantId
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )")
	} )
	public String getTenantId( String userName ) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_TENANT_ID, new Object[]{ userName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return ((BigDecimal) rs.getObject(1)).toString();
			}
		});
	}

	/**
	 * Returns E if the username is an Education Center User.
	 * Returns O if the username is an Org User
	 * Else returns null
	 * 
	 * @param username
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )")
	} )
	private String getUserType(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_TYPE, username);
		if (!lstData.isEmpty()) {
			return IApplicationConstants.PARENT_USER_FLAG;
		} else {
			return IApplicationConstants.ORG_USER_FLAG;
		}
	}
	
	@Caching( cacheable = {
		@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1).concat(#root.method.name) )"),
		@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1).concat(#root.method.name) )"),
		@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#p1).concat(#root.method.name) )")
	} )
	private String getUserType(String username, String contractName) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_USER_TYPE, username);
		if (!lstData.isEmpty()) {
			return IApplicationConstants.PARENT_USER_FLAG;
		} else {
			lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_EDU_USER_TYPE, username);
			if (!lstData.isEmpty()) {
				return IApplicationConstants.EDU_USER_FLAG;
			} else {
				return IApplicationConstants.ORG_USER_FLAG;
			}
		}
	}
	
	/**
	 * Retrieves and returns loggedin user details
	 * @param username
	 * @return UserTO with associated orgId, userid and firsttime loggedin flag
	 */
	/*public UserTO getUserDetails(String username) {
		UserTO user = null;
		List<Map<String, Object>> lstData = null;

		String userType = getUserType(username);
		logger.log(IAppLogger.INFO, "getUserDetails :: userType = " + userType);

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_EDU_USER_DETAILS, username);
		} else {
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_DETAILS, username);
		}
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				user = new UserTO();
				user.setFirstTimeLogin(fieldDetails.get("IS_FIRSTTIME_LOGIN").toString());
				user.setUserId(fieldDetails.get("USERID").toString());
				user.setOrgId(fieldDetails.get("ORG_NODEID").toString());
				user.setOrgNodeLevel(((BigDecimal) fieldDetails.get("ORG_NODE_LEVEL")).longValue());
				user.setDisplayName((fieldDetails.get("DISPLAY_USERNAME") != null) ? fieldDetails.get("DISPLAY_USERNAME").toString() : "Anonymous");
				user.setUserStatus(fieldDetails.get("ACTIVATION_STATUS").toString());
				user.setUserName(username);

				user.setPassword((fieldDetails.get("PASSWORD") != null) ? fieldDetails.get("PASSWORD").toString() : "");
				user.setSalt(Utils.getSaltWithUser(username, (fieldDetails.get("SALT") != null) ? fieldDetails.get("SALT").toString() : ""));
				user.setRoles(getGrantedAuthorities(username, user.getOrgNodeLevel(), userType));
				user.setIsAdminFlag(IApplicationConstants.FLAG_Y);
				user.setCustomerId(((BigDecimal) fieldDetails.get("CUSTID")).toString());
				user.setUserEmail((fieldDetails.get("EMAIL") != null) ? fieldDetails.get("EMAIL").toString() : "");
				//user.setProduct(fieldDetails.get("PRODUCT").toString());
				user.setUserType(userType);
				user.setOrgMode(fieldDetails.get("ORG_MODE").toString());
			}
		}
		return user;
	}*/
	
	/**
	 * Retrieves and returns loggedin user details
	 * @param username
	 * @return UserTO with associated orgId, userid and firsttime loggedin flag
	 */
	/*public UserTO getUserDetails(String username, String contractName) {
		UserTO user = null;
		List<Map<String, Object>> lstData = null;

		String userType = getUserType(username, contractName);
		logger.log(IAppLogger.INFO, "getUserDetails :: userType = " + userType);

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
			lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_EDU_USER_DETAILS, username);
		} else {
			lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_USER_DETAILS, username);
		}
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				user = new UserTO();
				user.setFirstTimeLogin(fieldDetails.get("IS_FIRSTTIME_LOGIN").toString());
				user.setUserId(fieldDetails.get("USERID").toString());
				user.setOrgId(fieldDetails.get("ORG_NODEID").toString());
				user.setOrgNodeLevel(((BigDecimal) fieldDetails.get("ORG_NODE_LEVEL")).longValue());
				user.setDisplayName((fieldDetails.get("DISPLAY_USERNAME") != null) ? fieldDetails.get("DISPLAY_USERNAME").toString() : "Anonymous");
				user.setUserStatus(fieldDetails.get("ACTIVATION_STATUS").toString());
				user.setUserName(username);

				user.setPassword((fieldDetails.get("PASSWORD") != null) ? fieldDetails.get("PASSWORD").toString() : "");
				user.setSalt(Utils.getSaltWithUser(username, (fieldDetails.get("SALT") != null) ? fieldDetails.get("SALT").toString() : ""));
				user.setRoles(getGrantedAuthorities(username, user.getOrgNodeLevel(), userType, contractName));
				user.setIsAdminFlag(IApplicationConstants.FLAG_Y);
				user.setCustomerId(((BigDecimal) fieldDetails.get("CUSTID")).toString());
				user.setUserEmail((fieldDetails.get("EMAIL") != null) ? fieldDetails.get("EMAIL").toString() : "");
				//user.setProduct(fieldDetails.get("PRODUCT").toString());
				user.setUserType(userType);
				user.setOrgMode(fieldDetails.get("ORG_MODE").toString());
			}
		}
		return user;
	}*/
	
	/**
	 * Retrieves and returns loggedin user details
	 * @param username
	 * @return UserTO with associated orgId, userid and firsttime loggedin flag
	 */
	public UserTO getUserDetails(Map<String, Object> paramMap) {
		UserTO user = null;
		final String username = (String)paramMap.get("username");
		String contractName = null;
		if(paramMap.get("contractName") != null && !paramMap.get("contractName").equals("")) {
			contractName = (String)paramMap.get("contractName");
		} else {
			contractName = Utils.getContractName();
		}
		final String resolvedContractName = contractName;
		final String userType = getUserType(username, contractName);
		
		logger.log(IAppLogger.INFO, "getUserDetails :: username = " + username);
		logger.log(IAppLogger.INFO, "getUserDetails :: contractName = " + contractName);
		logger.log(IAppLogger.INFO, "getUserDetails :: userType = " + userType);

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
						
			return (UserTO) getJdbcTemplatePrism(contractName).execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_EDU_USER_DETAILS);
							cs.setString(1, username);
							cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
							cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
							cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
							cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							UserTO user = new UserTO();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(2);
								while (rs.next()) {
									user.setFirstTimeLogin(rs.getString("IS_FIRSTTIME_LOGIN"));
									user.setUserId(String.valueOf(rs.getLong("USERID")));
									user.setOrgId(String.valueOf(rs.getLong("ORG_NODEID")));
									user.setOrgNodeLevel(rs.getLong("ORG_NODE_LEVEL"));
									user.setDisplayName(rs.getString("DISPLAY_USERNAME") != null ? rs.getString("DISPLAY_USERNAME") : "Anonymous");
									user.setUserStatus(rs.getString("ACTIVATION_STATUS"));
									user.setUserName(username);

									user.setPassword(rs.getString("PASSWORD") != null ? rs.getString("PASSWORD") : "");
									user.setSalt(Utils.getSaltWithUser(username, (rs.getString("SALT") != null) ? rs.getString("SALT") : ""));
									user.setRoles(getGrantedAuthorities(username, user.getOrgNodeLevel(), userType, resolvedContractName));
									user.setIsAdminFlag(IApplicationConstants.FLAG_Y);
									user.setCustomerId(String.valueOf(rs.getLong("CUSTID")));
									user.setUserEmail(rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "");
									user.setUserType(userType);
									user.setOrgMode(rs.getString("ORG_MODE"));
									user.setDefultCustProdId(rs.getLong("DEFAULT_CUST_PROD_ID"));									
								}
								user.setIsPasswordExpired(cs.getString(3));
								user.setIsPasswordWarning(cs.getString(4));
								Utils.logError(cs.getString(5));
								
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return user;
						}
					}
				);
			
		} else {
			return (UserTO) getJdbcTemplatePrism(contractName).execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USER_DETAILS);
							cs.setString(1, username);
							cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
							cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
							cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
							cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							UserTO user = new UserTO();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(2);
								while (rs.next()) {
									user.setFirstTimeLogin(rs.getString("IS_FIRSTTIME_LOGIN"));
									user.setUserId(String.valueOf(rs.getLong("USERID")));
									user.setOrgId(String.valueOf(rs.getLong("ORG_NODEID")));
									user.setOrgNodeLevel(rs.getLong("ORG_NODE_LEVEL"));
									user.setDisplayName(rs.getString("DISPLAY_USERNAME") != null ? rs.getString("DISPLAY_USERNAME") : "Anonymous");
									user.setUserStatus(rs.getString("ACTIVATION_STATUS"));
									user.setUserName(username);

									user.setPassword(rs.getString("PASSWORD") != null ? rs.getString("PASSWORD") : "");
									user.setSalt(Utils.getSaltWithUser(username, (rs.getString("SALT") != null) ? rs.getString("SALT") : ""));
									user.setRoles(getGrantedAuthorities(username, user.getOrgNodeLevel(), userType, resolvedContractName));
									user.setIsAdminFlag(IApplicationConstants.FLAG_Y);
									user.setCustomerId(String.valueOf(rs.getLong("CUSTID")));
									user.setUserEmail(rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "");
									user.setUserType(userType);
									user.setOrgMode(rs.getString("ORG_MODE"));
									user.setDefultCustProdId(rs.getLong("DEFAULT_CUST_PROD_ID"));
								}
								user.setIsPasswordExpired(cs.getString(3));
								user.setIsPasswordWarning(cs.getString(4));
								Utils.logError(cs.getString(5));
								
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return user;
						}
					}
				);
		}	
	
	}

	
	public boolean selectTest(){
		
		String sql = "SELECT WOM_WORKFLOW_PK" +
						", WOM_WORKFLOW_NAME" +
						", WOM_WORKFLOW_DESC" +
						", WOM_WORKFLOW_TYPE" +
						" FROM WF_WORKFLOW_MASTER" +
						" WHERE WOM_IS_VALID=?";
		
		Object[] params = new Object[]{'Y'};
		int[] types = new int[]{Types.CHAR};		
		
		List<Map<String,Object>> rows = getJdbcTemplate().queryForList(sql, params, types);
		for (Map row : rows) {			
			logger.log(IAppLogger.INFO,(String)row.get("WOM_WORKFLOW_NAME"));
		}		
		return true;
	}
	
	/*public UserTO getUserByEmail(String userEmail) throws SystemException {
		return getUserDetails(userEmail);
	}*/
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO,	"Enter: LoginDAOImpl - getUserByEmail");
		
		/*MUserTO user = new MUserTO();
		user.setUserName("ctbadmin");
		user.setPassword("3b3f38da65df4190b84d5611d39f61e19c7677fada8b9806ce0ed7f60567eb45");
		user.setSalt("TRPDXWRykIwXVE4vECwS");
		getMongoTemplate().save(user);*/
		
		String username = (String)paramMap.get("username");
		if("mdadmin".equals(username)) {
			/**
			 * This section is to check if user from mongo db is working
			 */
			String contractName = "";
			if(paramMap.get("contractName") != null && !paramMap.get("contractName").equals("")) {
				contractName = (String)paramMap.get("contractName");
			} else {
				contractName = Utils.getContractName();
			}
			Query searchUserQuery = new Query(Criteria.where("userName").is(username));
			MUserTO savedUser = getMongoTemplatePrism(contractName).findOne(searchUserQuery, MUserTO.class);
			System.out.println("    >> User from MongoDB : " + savedUser.getUserName() + " " + savedUser.getPassword());
			
			UserTO user = new UserTO();
			if(savedUser != null) {
				user.setCustomerId(savedUser.getCustomerId());
				user.setDisplayName(savedUser.getDisplayName());
				user.setFirstTimeLogin(savedUser.getFirstTimeLogin());
				user.setIsAdminFlag("Y");
				user.setIsPasswordExpired("FALSE");
				user.setIsPasswordWarning("FALSE");
				user.setOrgId(savedUser.getOrgNodes().get(0).getOrgNodeid());
				user.setOrgNodeLevel(Long.valueOf(savedUser.getOrgNodeCategory().getOrgNodeLevel()));
				user.setOrgMode(savedUser.getOrgMode());
				user.setPassword(savedUser.getPassword());
				List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
				for(String role : savedUser.getRoles()) {
					auth.add(new SimpleGrantedAuthority(role));
				}
				user.setRoles(auth);
				user.setSalt(Utils.getSaltWithUser(savedUser.getUserName(), savedUser.getSalt()));
				user.setUserId(savedUser.getId());
				user.setUserName(savedUser.getUserName());
				user.setUserEmail(savedUser.getUserEmail());
				user.setUserStatus(savedUser.getUserStatus());
				user.setUserType("O"/*savedUser.getUserType()*/);
				user.setDefultCustProdId(savedUser.getOrgNodes().get(0).getDefultCustProdId());
			}
			return user;
		} else {
			return getUserDetails(paramMap);
		}
	}
	
	/**
	 * get user list for SSO organization
	 * @param role id
	 * @return user details
	 */
	public UserTO getUsersForSSO(String orgId) {

		UserTO userTO = new UserTO();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_USERS_FOR_SSO_ORG, orgId);
		logger.log(IAppLogger.DEBUG, lstData.size()+"");

		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				userTO.setUserName((String) (fieldDetails.get("USERNAME")));
			}
		}
		return userTO;
	}
	
	/**
	 * get org node level for a given orgId
	 */
	public UserTO getOrgLevel(UserTO argUserTO) {
		UserTO userTO = null;
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism(argUserTO.getContractName()).queryForList(IQueryConstants.GET_ORG_LEVEL, 
				argUserTO.getOrgCode(), /*argUserTO.getCustomerId(),*/ argUserTO.getOrgNodeLevelStr());
		logger.log(IAppLogger.DEBUG, lstData.size()+"");

		if (lstData.size() > 0) {
			userTO = new UserTO();
			for (Map<String, Object> fieldDetails : lstData) {
				userTO.setOrgId( ((BigDecimal) (fieldDetails.get("NODEID"))).toString() );
				userTO.setOrgNodeLevel( ((BigDecimal) (fieldDetails.get("ORGLEVEL"))).longValue() );
				userTO.setCustomerId(((BigDecimal) (fieldDetails.get("CUSTOMERID"))).toString());
			}
		}
		return userTO;
	}
	
   /**
	 * Convert to stored procedure and implement all static message as configurable - By Joy
	 * Retrieves and returns message corresponding configured in database
	 * @param reportName, messageType, messageName, contractName
	 * @return message
	 */
	@Caching( cacheable = {
		@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage'))"),
		@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage'))"),
		@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage'))")
	} )
	public String getSystemConfigurationMessage(final Map<String,Object> paramMap){
		logger.log(IAppLogger.INFO, "Enter: LoginDAOImpl - getSystemConfigurationMessage()");
		long t1 = System.currentTimeMillis();
		
		final String reportName = (String) paramMap.get("REPORT_NAME");
		final String messageType = (String) paramMap.get("MESSAGE_TYPE");
		final String messageName = (String) paramMap.get("MESSAGE_NAME");
		final long custProdId = ((Long)paramMap.get("custProdId")).longValue();
		String contractName = (String) paramMap.get("contractName");
		
		
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "Contract Name: "+contractName);
		logger.log(IAppLogger.INFO, "custProdId: "+custProdId);
		
		String systemMessage = "";
		try {
			systemMessage = (String) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.GET_SYSTEM_CONFIGURATION_MESSAGE + "}");
					cs.setString(1, reportName);
					cs.setString(2, messageType);
					cs.setString(3, messageName);
					cs.setLong(4, custProdId);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					String systemMessageResult = "";
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(5);
						if (rs.next()) {
							systemMessageResult = rs.getString("REPORT_MSG");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return systemMessageResult == null ? "" : systemMessageResult.trim();
				}
			});
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "getSystemConfigurationMessage(): " + e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: LoginDAOImpl - getSystemConfigurationMessage() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return systemMessage;
	}
	
	/*
	 * Get custprodid along with product - By Joy
	 * */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getCustomerProduct') )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getCustomerProduct') )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getCustomerProduct') )")
	} )
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)
			throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: getCustomerProduct()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final long loggedInCustomer = Long.valueOf( paramMap.get("loggedInCustomer").toString());
		final String loggedInOrgId = (String) paramMap.get("loggedInOrgId");
	//	final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
	//	if(paramMap.get("loggedInCustomer") != null) {
	//	loggedInCustomer = Long.valueOf( paramMap.get("loggedInCustomer").toString());
	//	loggedInOrgId = (String) paramMap.get("loggedInOrgId");
		/*} else {
			loggedInCustomer = Long.valueOf(loggedinUserTO.getCustomerId());
			loggedInOrgId = loggedinUserTO.getOrgId();
		}*/
		
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_TEST_ADMINISTRATION + "}");
				            cs.setLong(1, loggedInCustomer);	
				            cs.setString(2, loggedInOrgId);
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsCustProd = null;
			        			List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOResult 
			        							= new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			        			try {
									cs.execute();
									rsCustProd = (ResultSet) cs.getObject(3);

									com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
									while(rsCustProd.next()){
										objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
										objectValueTO.setValue(rsCustProd.getString("VALUE"));
										objectValueTO.setName(rsCustProd.getString("NAME"));
										objectValueTOResult.add(objectValueTO);
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return objectValueTOResult;
				        }
				    });
			
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: getCustomerProduct() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return objectValueTOList;
	}
	
	/**
	 * get user list for SSO organization
	 * @param role id
	 * @return user details
	 */
	public UserTO getUsersForSSO(UserTO userTO) {

		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_USERS_FOR_SSO_ORG, userTO.getCustomerId(), userTO.getOrgCode());
		logger.log(IAppLogger.DEBUG, lstData.size()+"");

		if (lstData.size() > 0) {

			for (Map<String, Object> fieldDetails : lstData) {
				userTO.setCustomerId(((BigDecimal)(fieldDetails.get("CUSTOMERID"))).toString());
				userTO.setOrgId(((BigDecimal)(fieldDetails.get("NODEID"))).toString());
			}
		}
		return userTO;
	}
	
	/**
	 * check provided user availability
	 * 
	 * @return boolean
	 */
	public boolean checkUserAvailability(final String username) {
		String tempUsername = "";
		try{
			tempUsername = (String) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.VALIDATE_USER_NAME + "}");
				            cs.setString(1, username);
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsUsername = null;
			        			String usernameResult = "";
			        			try {
									cs.execute();
									rsUsername = (ResultSet) cs.getObject(2);
									if(rsUsername.next()){
										usernameResult = rsUsername.getString("USERNAME");
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return usernameResult;
				        }
				    });
			if("".equals(tempUsername) || tempUsername == null){
				return Boolean.TRUE;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return Boolean.FALSE;
		/*List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.VALIDATE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;*/
	}
	
	public boolean checkUserAvailability(final String username, String contractName) {
		String tempUsername = "";
		try{
			tempUsername = (String) getJdbcTemplatePrism(contractName).execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.VALIDATE_USER_NAME + "}");
				            cs.setString(1, username);
				            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); 
				            cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				            return cs;				      			            
				        }
				    } ,   new CallableStatementCallback<Object>()  {
			        		public Object doInCallableStatement(CallableStatement cs) {
			        			ResultSet rsUsername = null;
			        			String usernameResult = "";
			        			try {
									cs.execute();
									rsUsername = (ResultSet) cs.getObject(2);
									if(rsUsername.next()){
										usernameResult = rsUsername.getString("USERNAME");
									}
									
			        			} catch (SQLException e) {
			        				e.printStackTrace();
			        			}
			        			return usernameResult;
				        }
				    });
			if("".equals(tempUsername) || tempUsername == null){
				return Boolean.TRUE;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return Boolean.FALSE;
		/*List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.VALIDATE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;*/
	}
	
	/**
	 * check provided user availability and return org_nodeid
	 * 
	 * @return String
	 */
	public String getUserOrgNode(String username, String contractName) {
		if (!checkUserAvailability(username, contractName)) {
			String orgNodeId = "";
			List<Map<String, Object>> usrData = null;
			usrData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.GET_USER_ORG, username);
			if (usrData.size() > 0) {
				for (Map<String, Object> fieldDetails : usrData) {
					orgNodeId = fieldDetails.get("NODEID").toString();
				}
			}
			return orgNodeId;
		}
		return null;
	}
	
	
	/*
	 * Update user org
	 * @see com.ctb.prism.login.dao.ILoginDAO#updateUserOrg(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid, String contractName) {
		getJdbcTemplatePrism(contractName).update(IQueryConstants.UPDATE_USER_ORG, OrgNodeId, oldOrgNodeid, username);
	}
	
	/**
	 * add new user
	 * @param String userId, String tenantId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles
	 * @return UserTO
	 */
	@Caching( evict = { 
			@CacheEvict(value = "inorsAdminCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", allEntries = true),
			@CacheEvict(value = "tascAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  allEntries = true),
			@CacheEvict(value = "usmoAdminCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  allEntries = true)
	} )
	public void addNewUser(Map<String,Object> paramMap) throws Exception {
		//UserTO to = null;
		final String userName = (String) paramMap.get("userName");
		final String password = (String) paramMap.get("password");
		final String userDisplayName = (String) paramMap.get("userDisplayName");
		final String emailId = (String) paramMap.get("emailId");
		final String userStatus = (String) paramMap.get("userStatus");
		final String customerId = (String) paramMap.get("customer");
		final String tenantId = (String) paramMap.get("tenantId");
		final String orgLevel = (String) paramMap.get("orgLevel");
		//String adminYear =  (String) paramMap.get("adminYear");
		final String[] userRoles =  (String[]) paramMap.get("userRoles");
		
		final String contractName = (String) paramMap.get("contractName");
		
		
		final StringBuilder roles = new StringBuilder();
		try {
			ObjectValueTO currAdmin = getCurrentAdmin(contractName);
			final String adminYear = currAdmin.getValue();
			
			
			if (userRoles != null) {
				/*
				 * getJdbcTemplatePrism().update( IQueryConstants.INSERT_USER_ROLE, IApplicationConstants.DEFAULT_USER_ROLE, user_seq_id);
				 */
				for (String role : userRoles) {
					roles.append(role).append(",");
				}				
				roles.replace(roles.lastIndexOf(","), roles.lastIndexOf(",")+1, "");				
			}
			
			

			logger.log(IAppLogger.INFO, "Add User");
			
			String nodeId = (String) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				String salt = PasswordGenerator.getNextSalt();
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.CREATE_SSO_USER);
					cs.setString(1, userName);
					cs.setString(2, userDisplayName);
					cs.setString(3, emailId);
					cs.setString(4, userStatus);
					cs.setString(5, IApplicationConstants.FLAG_N);
					cs.setString(6, SaltedPasswordEncoder.encryptPassword(password, Utils.getSaltWithUser(userName, salt)));
					cs.setString(7, salt);
					cs.setString(8, IApplicationConstants.FLAG_N);
					cs.setLong(9, Long.valueOf(customerId));
					cs.setLong(10, Long.valueOf(tenantId));
					cs.setLong(11, Long.valueOf(orgLevel));
					cs.setLong(12, Long.valueOf(adminYear));
					cs.setString(13, IApplicationConstants.ACTIVE_FLAG);
					cs.setString(14, roles.toString());
					cs.setString(15, IApplicationConstants.FLAG_N);
					cs.registerOutParameter(16, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(17, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(18, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					try {
						cs.execute();
						Utils.logError(cs.getString(18));
						if(cs.getString(18) != null && cs.getString(18).length() > 0) {
							throw new BusinessException("Exception occured while getting Organization " + cs.getString(18));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			});
			logger.log(IAppLogger.INFO, "User added in org : " + nodeId);
	
			if (nodeId != null) {
				paramMap.put("nodeId", nodeId);
			}		
			
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while adding user details.", e);
			throw new Exception(e);
		}
		//return to;
	}
	
	/**
	 * get current admin details
	 * @param contractName
	 * @return List of users
	 */
	public ObjectValueTO getCurrentAdmin(String contractName) throws SystemException {
		ObjectValueTO to = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.CURR_ADMIN_YEAR);
		logger.log(IAppLogger.DEBUG, lstData.size()+"");
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				to = new ObjectValueTO();
				to.setValue( ((BigDecimal) (fieldDetails.get("ADMINID"))).toString() );
				to.setName((String) (fieldDetails.get("ADMIN_NAME")));
			}
		}
		
		return to;
	}
	/**
	 * Get root path for customer and product
	 * @param customerId
	 * @param testAdmin
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#customerId).concat(#testAdmin).concat(#contractName).concat(#root.method.name) )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#customerId).concat(#testAdmin).concat(#contractName).concat(#root.method.name) )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#customerId).concat(#testAdmin).concat(#contractName).concat(#root.method.name) )")
	} )
	public String getRootPath(String customerId, String testAdmin, String contractName) {
		logger.log(IAppLogger.INFO, "LoginDAOImpl. getRootPath(), customerId = " + customerId);
		logger.log(IAppLogger.INFO, "LoginDAOImpl. getRootPath(), testAdmin = " + testAdmin);
		logger.log(IAppLogger.INFO, "LoginDAOImpl. getRootPath(), contractName = " + contractName);
		return getJdbcTemplatePrism(contractName).queryForObject(IQueryConstants.GET_ROOT_PATH, new Object[] { customerId, testAdmin }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return rs.getString(1);
			}
		});
	}
	
	/**
	 * Get root path for customer and cust prod
	 * @param customerId
	 * @param custProdId
	 * @return
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="#contractName == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#customerId).concat(#custProdId).concat(#contractName).concat(#root.method.name) )"),
			@Cacheable(value = "tascConfigCache",  condition="#contractName == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#customerId).concat(#custProdId).concat(#contractName).concat(#root.method.name) )"),
			@Cacheable(value = "usmoConfigCache",  condition="#contractName == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#customerId).concat(#custProdId).concat(#contractName).concat(#root.method.name) )")
	} )
	public String getCustPath(String customerId, String custProdId, String contractName) {
		logger.log(IAppLogger.INFO, "LoginDAOImpl. getRootPath(), customerId = " + customerId);
		logger.log(IAppLogger.INFO, "LoginDAOImpl. getRootPath(), testAdmin = " + custProdId);
		logger.log(IAppLogger.INFO, "LoginDAOImpl. getRootPath(), contractName = " + contractName);
		return getJdbcTemplatePrism(contractName).queryForObject(IQueryConstants.GET_CUST_PATH, new Object[] { customerId, custProdId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return rs.getString(1);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.login.dao.ILoginDAO#getMenuMap(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getMenuMap') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getMenuMap') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getMenuMap') )")
	} )
	public Set<MenuTO> getMenuMap(Map<String, Object> paramMap) {
		//final Long userId = Long.parseLong((paramMap.get("userId") == null) ? "0" : paramMap.get("userId").toString());
		//logger.log(IAppLogger.INFO, "userId = " + userId);
		final String roles = (String) paramMap.get("roles");
		final long orgNodeLevel = Long.valueOf(paramMap.get("orgNodeLevel").toString());
		final long custProdId = Long.valueOf(paramMap.get("custProdId").toString());
		String contractName = paramMap.get("contractName").toString();
		if(StringUtils.isEmpty(contractName)) contractName = Utils.getContractName();
		logger.log(IAppLogger.INFO, "roles = " + roles);
		logger.log(IAppLogger.INFO, "orgNodeLevel = " + orgNodeLevel);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		
		if(paramMap.get("database").equals("MongoDB")){
			Aggregation agg = newAggregation(
					unwind("reportAccess"),
					match(Criteria.where("reportAccess.roleid").is("1")
							.andOperator(Criteria.where("reportAccess.org_level").is(String.valueOf(orgNodeLevel))))
				);

			//Convert the aggregation result into a List
			AggregationResults<MResultTO> groupResults 
					= getMongoTemplatePrism(contractName)
					.aggregate(agg, MReportTO.class, MResultTO.class);
			
			List<MResultTO> reportDetails = groupResults.getMappedResults();
			
			System.out.println("    >> User from MongoDB : "
					+ reportDetails.get(0).getReportName() + " " + reportDetails.get(0).getReportFoderURI());
			
			Set<MenuTO> menuSet = new LinkedHashSet<MenuTO>();
			for(int i=0; i < reportDetails.size(); i++) {
				MenuTO to = new MenuTO();
				to.setMenuName(reportDetails.get(i).getMenu());
				to.setReportName(reportDetails.get(i).getReportName());
				to.setReportFolderUri(reportDetails.get(i).getReportFoderURI());
				to.setMenuSequence("1"); // Hard coded for the time
				to.setReportSequence(reportDetails.get(i).getReportAccess().getReportSequence());
				menuSet.add(to);
			}			
			return menuSet;
		} else {
			return (Set<MenuTO>) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_MENU_MAP);
					cs.setString(1, roles);
					cs.setLong(2, orgNodeLevel);
					cs.setLong(3, custProdId);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					Set<MenuTO> menuSet = new LinkedHashSet<MenuTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(4);
						while (rs.next()) {
							MenuTO to = new MenuTO();
							String menuName = rs.getString("MENU_NAME");
							String key = rs.getString("KEY");
							String value = rs.getString("VALUE");
							String menuSeq = rs.getString("MENU_SEQ");
							String reportSeq = rs.getString("REPORT_SEQ");
							to.setMenuName(menuName);
							to.setReportName(key);
							to.setReportFolderUri(value);
							to.setMenuSequence(menuSeq);
							to.setReportSequence(reportSeq);
							menuSet.add(to);
						}
						Utils.logError(cs.getString(5));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					logger.log(IAppLogger.INFO, "menuSet = " + menuSet);
					return menuSet;
				}
			});
		}
		
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getActionMap') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getActionMap') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getActionMap') )")
	} )
	public Map<String,String> getActionMap(Map<String, Object> paramMap) {
		
		/*final Long userId = paramMap.get("userId") == null ? 0 : Long.parseLong((String)paramMap.get("userId"));
		final Long custProdId = paramMap.get("custProdId") == null ? 0: Long.parseLong((String)paramMap.get("custProdId"));*/
		final String roles = (String) paramMap.get("roles");
		final long orgNodeLevel = Long.valueOf(paramMap.get("orgNodeLevel").toString());
		final long custProdId = Long.valueOf(paramMap.get("custProdId").toString());
		
		logger.log(IAppLogger.INFO, "roles = " + roles);
		logger.log(IAppLogger.INFO, "orgNodeLevel = " + orgNodeLevel);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		
		return (Map<String,String>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_ACTION_MAP);
				cs.setString(1, roles);
				cs.setLong(2, orgNodeLevel);
				cs.setLong(3, custProdId);
				cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				Map<String,String> actionMap = new LinkedHashMap<String,String>();
				try {
					cs.execute();
					rs = (ResultSet) cs.getObject(4);
					while (rs.next()) {
						actionMap.put(rs.getString("REPORT_NAME")+ " " + rs.getString("ACTION_NAME"), rs.getString("REPORT_NAME")+ " " + rs.getString("ACTION_NAME"));
					}
					Utils.logError(cs.getString(5));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "actionMap = " + actionMap);
				return actionMap;
			}
		});
	}
	
	
	/*
	 * Making it public as from other module it will be get called
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsConfigCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getContractProerty') )"),
			@Cacheable(value = "tascConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getContractProerty') )"),
			@Cacheable(value = "usmoConfigCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#paramMap) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getContractProerty') )")
	} )
	public Map<String, Object> getContractProerty (Map<String, Object> paramMap) {
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		logger.log(IAppLogger.INFO, "getContractProerty for contract= " + contractName);
		return (Map<String, Object>) getJdbcTemplatePrism(contractName).execute(
			new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_PROPERTY);
					cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					Map<String, Object> propertyMap = new HashMap<String, Object>();
					ResultSet rs = null;
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(1);
						while (rs.next()) {
							propertyMap.put(rs.getString("PROPERY_NAME"), rs.getString("PROPERY_VALUE"));
						}
						Utils.logError(cs.getString(2));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					logger.log(IAppLogger.INFO, "getContractProerty().propertyMap size =" + propertyMap.size());
					return propertyMap;
				}
			}
		);
	}
	
	/**
	 * get all passwords from history
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPasswordHistory(Map<String, Object> paramMap) {		
		
		final String username = (String)paramMap.get("userName");
		
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		
		paramMap.remove("userName"); //This is required so that cache will get data based on single parameter (contract) only  
		Map<String, Object> propertyMap = getContractProerty(paramMap);
		final int pwdHistoryDay =
			propertyMap.get("password.history.day")!= null ? Integer.parseInt((String)propertyMap.get("password.history.day")): 0;
		
		return (List<String>) getJdbcTemplatePrism(contractName).execute(
				new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_PASSWORD_HISTORY);
						cs.setString(1, username);
						cs.setInt(2 ,pwdHistoryDay);
						cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						List<String> pwsString = new LinkedList<String>();
						int count = 0;
						ResultSet rs = null;
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(3);
							while (rs.next()) {
								count++;
								if(count == 1) {
									pwsString.add(rs.getString("SALT"));
								}
								pwsString.add(rs.getString("PASSWORD"));
							}
							Utils.logError(cs.getString(4));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						logger.log(IAppLogger.INFO, "getPasswordHistory().pwsString size =" + pwsString.size());
						return pwsString;
					}
				}
			);
	}
	

//	@Cacheable(value = "adminCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramUserMap)).concat('getUserEmail'))")
	public UserTO getUserEmail(Map<String,Object> paramUserMap) {
		final String userName = (String)paramUserMap.get("username"); 
		String contractName  = (String)paramUserMap.get("contractName"); 
		return (UserTO) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_USER_EMAIL);
				cs.setString(1, userName);
				cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				com.ctb.prism.login.transferobject.UserTO userDetails = new com.ctb.prism.login.transferobject.UserTO();
				try {
					cs.execute();
					rs = (ResultSet) cs.getObject(2);
					if (rs.next()) {
						userDetails.setUserEmail(rs.getString("EMAIL"));
						userDetails.setSalt(rs.getString("SALT"));
					}
					Utils.logError(cs.getString(3));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "getUserEmail().email = " + userDetails.getUserEmail());
				return userDetails;
			}
		});
	}
	
	public boolean checkOrgHierarchy(Map<String, Object> paramMap){
		
		logger.log(IAppLogger.INFO, "Enter: LoginDAOImpl - checkOrgHierarchy()");
		long t1 = System.currentTimeMillis();
		
		final String userName = (String)paramMap.get("username"); 
		final String custProdId = (String)paramMap.get("custProdId"); 
		final String prevOrgId = (String)paramMap.get("prevOrgId");
		String contractName = (String)paramMap.get("contractName"); 
		if(contractName == null) contractName = Utils.getContractName();
		BigDecimal existFlag;
		boolean returnFlag = Boolean.FALSE;
		try{
			existFlag = (BigDecimal)getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_CHECK_ORG_HIERARCHY);
					cs.setString(1, userName);
					cs.setLong(2, Long.parseLong(custProdId));
					cs.setLong(3, Long.parseLong(prevOrgId));
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					BigDecimal existFlag = null;
					try {
						cs.execute();
						existFlag = (BigDecimal)cs.getObject(4);
						Utils.logError(cs.getString(5));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return existFlag;
				}
			});
			
			if(existFlag.intValue() > 0 ){
				returnFlag = Boolean.TRUE;
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: LoginDAOImpl - checkOrgHierarchy() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnFlag;
	}
	
	@SuppressWarnings("unchecked")
	public void checkUserRoleByUsername(Map<String, Object> paramMap){
		logger.log(IAppLogger.INFO, "Enter: LoginDAOImpl - getUserRoleByUsername()");
		long t1 = System.currentTimeMillis();
		
		final String username = (String)paramMap.get("username"); 
		final String contractName = (String)paramMap.get("contractName"); 
		final String userRole = (String)paramMap.get("userRole"); 
		BigDecimal statusFlag = null;
		try{
			statusFlag = (BigDecimal)getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.SP_CHECK_USER_ROLE_BY_USERNAME);
					cs.setString(1, username);
					cs.setString(2, userRole);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					BigDecimal statusFlag = null;
					try {
						cs.execute();
						statusFlag = (BigDecimal)cs.getObject(3);
						Utils.logError(cs.getString(4));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return statusFlag;
				}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: LoginDAOImpl - getUserRoleByUsername() took time: " + String.valueOf(t2 - t1) + "ms");
		}
	}
	
}
