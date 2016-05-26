package com.ctb.prism.login.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoFactoryBean;
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
import com.ctb.prism.login.transferobject.FlatCustProdAdmin;
import com.ctb.prism.login.transferobject.MCustProdAdminTO;
import com.ctb.prism.login.transferobject.MFlatActionAccessTO;
import com.ctb.prism.login.transferobject.MFlatReportsTO;
import com.ctb.prism.login.transferobject.MFlatUserTO;
import com.ctb.prism.login.transferobject.MReportConfigTO;
import com.ctb.prism.login.transferobject.MUserTO;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.OrgUser;
import com.ctb.prism.login.transferobject.ProjectProp;
import com.ctb.prism.login.transferobject.PwdHistory;
import com.ctb.prism.login.transferobject.UserTO;
import com.jaspersoft.mongodb.connection.MongoDbConnection;

@Repository
public class MLoginDAOImpl extends BaseDAO /*implements ILoginDAO*/{

	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(LoginDAOImpl.class.getName());
	
	
	@SuppressWarnings("deprecation")
	@Autowired
	private MongoFactoryBean mongo;
	
	MongoDbConnection tascConnection = null;
	
	
	
	/**
	 * @return the connection object for prism DB
	 * @throws SQLException
	 */
	@Caching( cacheable = {
			@Cacheable(value = "tascMongoCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key = "T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).string(#contractName)).concat(#root.method.name) )")
	} )
	public MongoDbConnection getPrismMongoConnectionCached(String contractName){
		try {
        
	        if("tasc".equals(contractName)) {
	        	if(tascConnection == null) {
	        		System.out.println(" ------------------------ CREATING new MONGO Connection ------------------------- ");
	        		String mongoURI = mongo.getObject().getAddress().toString();
	        		mongoURI = "mongodb://"+ mongoURI +"/drc_mongo";
	        		tascConnection = new MongoDbConnection(mongoURI, null, null);
	        	} else {
	        		return tascConnection;
	        	}
	        } 
        
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
     }
	
	@CacheEvict(value = { "tascMongoCache" }, allEntries = true)
	public MongoDbConnection getPrismMongoConnection(String contractName){
		try {
        
	        if("tasc".equals(contractName)) {
	        	if(tascConnection == null) {
	        		System.out.println(" ------------------------ CREATING new MONGO Connection ------------------------- ");
	        		String mongoURI = mongo.getObject().getAddress().toString();
	        		mongoURI = "mongodb://"+ mongoURI +"/drc_mongo";
	        		tascConnection = new MongoDbConnection(mongoURI, null, null);
	        	} else {
	        		return tascConnection;
	        	}
	        } 
        
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
     }
	
	/**
	 * This method is used to return user authorities
	 * @param username
	 * @return
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String userName) {
		
		String contractName = Utils.getContractName();
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(userName));
		MUserTO serchUser = getMongoTemplatePrism(contractName).findOne(searchUserQuery, MUserTO.class);
		if(serchUser != null)
			return getGrantedAuthorities(serchUser.getUserRoles(), "0");
		else {
			return null;
		}
	}

	/**
	 * 
	 * @param username
	 * @param orgLevel
	 * @param userType
	 * @return
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String[] userRoles, String orgLevel) {
		List<GrantedAuthority> userPerms = new ArrayList<GrantedAuthority>();
		for (String userRole : userRoles){
				userPerms.add( new SimpleGrantedAuthority(userRole));
			}
			
	     userPerms.add(new SimpleGrantedAuthority(CustomStringUtil.appendString(
					IApplicationConstants.ROLE_INIT,
					"LEVEL","" + orgLevel)));
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
		Map<String,Object> newParamMap = new HashMap<String,Object>();
		newParamMap.put("contractName",contractName);
		Map<String,Object> propertyMap = getContractProerty(newParamMap);
		
		String passwordExp = (String) propertyMap.get("password.expiry");
		String passwordWar = (String) propertyMap.get("password.expiry.warning");
		
		final String resolvedContractName = contractName;
		final String userType = getUserType(username, contractName);
		
		logger.log(IAppLogger.INFO, "getUserDetails :: username = " + username);
		logger.log(IAppLogger.INFO, "getUserDetails :: contractName = " + contractName);
		logger.log(IAppLogger.INFO, "getUserDetails :: userType = " + userType);

		if (IApplicationConstants.EDU_USER_FLAG.equals(userType)) {
						
			/*return (UserTO) getJdbcTemplatePrism(contractName).execute(
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
				);*/
			
			return user;
			
		} else {
			
			Query searchUserQuery = new Query(Criteria.where("_id").is(username));
			MUserTO savedUser = getMongoTemplatePrism(contractName).findOne(searchUserQuery, MUserTO.class);
			
			user = new UserTO();
			user.setFirstTimeLogin(savedUser.getIsFirstTimeLogin());
			//user.setUserId(String.valueOf(rs.getLong("USERID")));
			for(OrgUser org : savedUser.getOrgUser()){
				if (org.getIsActive().equals(IApplicationConstants.FLAG_Y)){
					user.setOrgId(org.getOrg_id());
					break;
				}
			}	
			user.setOrgNodeLevel(Long.valueOf(savedUser.getOrgCategory().getLevel()));
			user.setDisplayName(savedUser.getDisplayName() != null ? savedUser.getDisplayName() : "Anonymous");
			user.setUserStatus(savedUser.getStatus());
			user.setUserName(savedUser.get_id());

			user.setPassword(savedUser.getPassword() != null ? savedUser.getPassword() : "");
			user.setSalt(Utils.getSaltWithUser(username, (savedUser.getSalt() != null) ? savedUser.getSalt() : ""));
			user.setRoles(getGrantedAuthorities(savedUser.getUserRoles(),savedUser.getOrgCategory().getLevel()));
			user.setIsAdminFlag(IApplicationConstants.FLAG_Y);
			user.setCustomerId(savedUser.getCustomerCode());
			user.setUserEmail(savedUser.getEmail() != null ? savedUser.getEmail()  : "");
			user.setUserType(userType);
			user.setOrgMode(savedUser.getOrgCategory().getCategory());
			//user.setDefultCustProdId(rs.getLong("DEFAULT_CUST_PROD_ID"));
			
			PwdHistory[] pwdHistoryArr = savedUser.getPwdHistory(); 			
			List<Date> pwdDates = new ArrayList<Date>();
			for(PwdHistory pwdHistory: pwdHistoryArr){
				pwdDates.add(pwdHistory.getDate());
			}
						
			TimeZone.setDefault(TimeZone.getTimeZone("EDT"));
			
			Date mostRecent = Collections.max(pwdDates);
			Calendar calx = Calendar.getInstance();
			calx.setTime(mostRecent);
			calx.add(Calendar.DATE, Integer.valueOf(passwordExp));
			
			Calendar caly = Calendar.getInstance();
			caly.setTime(new Date());
			
			/* = 1 means > :  = 0 means = :  = -1 means <  */
			if(caly.compareTo(calx)== 1 || caly.compareTo(calx)== 0){
				user.setIsPasswordExpired("TRUE");
			} else {
				user.setIsPasswordExpired("FALSE");
			}
			
			Calendar calz = Calendar.getInstance();
			calz.add(Calendar.DATE, Integer.valueOf(passwordWar));
			
			if(caly.compareTo(calz)== 1){
				user.setIsPasswordWarning("TRUE");
			} else {
				user.setIsPasswordWarning("FALSE");
			}
						
			return user;
			
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
	
	/**
	 * Check if user present in DB
	 */
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException {
		logger.log(IAppLogger.INFO,	"Enter: LoginDAOImpl - getUserByEmail");
		
		String username = (String)paramMap.get("username");
		String contractName = "";
		if(paramMap.get("contractName") != null && !paramMap.get("contractName").equals("")) {
			contractName = (String)paramMap.get("contractName");
		} else {
			contractName = Utils.getContractName();
		}
		Query searchUserQuery = new Query(Criteria.where("_id").is(username));
		MUserTO savedUser = getMongoTemplatePrism(contractName).findOne(searchUserQuery, MUserTO.class);
		//System.out.println("    >> User from MongoDB : " + savedUser.get_id() + " " + savedUser.getPassword());
		
		UserTO user = new UserTO();
		if(savedUser != null) {
			user.setDisplayName(savedUser.getDisplayName());
			user.setFirstTimeLogin(savedUser.getIsFirstTimeLogin());
			user.setIsAdminFlag(IApplicationConstants.FLAG_Y);
			user.setIsPasswordExpired("FALSE");
			user.setIsPasswordWarning("FALSE");
			for(OrgUser org : savedUser.getOrgUser()){
				if (org.getIsActive().equals(IApplicationConstants.FLAG_Y)){
					user.setOrgId(org.getOrg_id());
					break;
				}
			}				
			user.setOrgNodeLevel(Long.valueOf(savedUser.getOrgCategory().getLevel()));
			user.setOrgNodeLevelStr(savedUser.getOrgCategory().getLevel());
			user.setOrgMode(savedUser.getOrgCategory().getCategory());
			user.setOrgId(savedUser.getOrgUser()[0].getOrg_id()); // assuming for non parent users only one org will be there
			user.setPassword(savedUser.getPassword());
			List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
			for(String role : savedUser.getUserRoles()) {
				auth.add(new SimpleGrantedAuthority(role));
			}
			user.setRoles(auth);
			user.setSalt(Utils.getSaltWithUser(savedUser.get_id(), savedUser.getSalt()));
			user.setUserId(savedUser.get_id());
			user.setUserName(savedUser.get_id());
			user.setUserEmail(savedUser.getEmail());
			user.setUserStatus(savedUser.getStatus());
			user.setUserType(savedUser.getUserType());
			user.setCustomerId(savedUser.getCustomerCode());
			user.setProject(savedUser.getProject_id());
			user.setOrgName(savedUser.getOrgUser()[0].getOrgName());
		}
		return user;
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
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)
			throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: getCustomerProduct()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		String project = (String) paramMap.get("project");
		String contractName = (String) paramMap.get("contractName");
		
		try {
			/*TypedAggregation<CustProdAdmin> agg = newAggregation(CustProdAdmin.class, 
					match(Criteria.where("_id").is(project)),	
					unwind("Customers"),
					unwind("Customers.Admins"),
					sort(Sort.Direction.DESC, "Customers.Admins.Seq")
				);*/
			Aggregation agg = newAggregation(
					match(Criteria.where("_id").is(project)),	
					unwind("customers"),
					unwind("customers.admins"),
					sort(Sort.Direction.DESC, "customers.admins.seq")
				);
			
			AggregationResults<FlatCustProdAdmin> groupResults = getMongoTemplatePrism("global").aggregate(agg, MCustProdAdminTO.class, FlatCustProdAdmin.class);
			List<FlatCustProdAdmin> reportDetails = groupResults.getMappedResults();
			
			// get all products
			objectValueTOList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = null;
			for(FlatCustProdAdmin custProdAdmin : reportDetails) {
				objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
				objectValueTO.setValue(custProdAdmin.getCustomers().getAdmins().getCode()); // getting first element of each array because the collection is unwinded
				objectValueTO.setName (custProdAdmin.getCustomers().getAdmins().getName());
				objectValueTOList.add(objectValueTO);
			}
			//temp code
			/*objectValueTOList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			com.ctb.prism.core.transferobject.ObjectValueTO objectValueTO = new com.ctb.prism.core.transferobject.ObjectValueTO();
			objectValueTO.setValue("2015");
			objectValueTO.setName("TASC 2015");
			objectValueTOList.add(objectValueTO);*/
			
			// get the unique list of products
			Set<com.ctb.prism.core.transferobject.ObjectValueTO> uniqueProducts = new HashSet<com.ctb.prism.core.transferobject.ObjectValueTO>(objectValueTOList);

			//move unique set to mail list
			objectValueTOList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
			objectValueTOList.addAll(uniqueProducts);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex.getMessage());
		}
		return objectValueTOList;
		
		/*
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
		return objectValueTOList;*/
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
		Object rolesArr[] = roles.split(",");
		final long orgNodeLevel = Long.valueOf(paramMap.get("orgNodeLevel").toString());
		final long custProdId = Long.valueOf(paramMap.get("custProdId").toString());
		String contractName = paramMap.get("contractName").toString();
		if(StringUtils.isEmpty(contractName)) contractName = Utils.getContractName();
		logger.log(IAppLogger.INFO, "roles = " + roles);
		logger.log(IAppLogger.INFO, "orgNodeLevel = " + orgNodeLevel);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		
		Aggregation agg = newAggregation(
				match(Criteria.where("project_Id").is(Utils.getContractName().toUpperCase())),
				unwind("reportAccess"),
				match(Criteria.where("reportAccess.roles").in(rolesArr)
						.andOperator(Criteria.where("reportAccess.orgLevel").is(String.valueOf(orgNodeLevel)))),
				sort(Sort.Direction.ASC,"menuSequence")
			);

		//Convert the aggregation result into a List
		AggregationResults<MFlatReportsTO> groupResults 
				= getMongoTemplatePrism("global")
				.aggregate(agg, MReportConfigTO.class, MFlatReportsTO.class);
		
		List<MFlatReportsTO> reportDetails = groupResults.getMappedResults();
		
		System.out.println("    >> User from MongoDB : "
				+ reportDetails.get(0).getReportName() + " " + reportDetails.get(0).getReportFolderURI());
		
		Set<MenuTO> menuSet = new LinkedHashSet<MenuTO>();
		for(int i=0; i < reportDetails.size(); i++) {
			MenuTO to = new MenuTO();
			to.setMenuName(reportDetails.get(i).getMenu());
			to.setReportName(reportDetails.get(i).getReportName());
			to.setReportFolderUri(reportDetails.get(i).getReportFolderURI());
			to.setMenuSequence(reportDetails.get(i).getMenuSequence()); 
			to.setReportSequence(reportDetails.get(i).getReportAccess().getReportSequence());
			menuSet.add(to);
		}			
		return menuSet;
		
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
		String roles = (String) paramMap.get("roles");
		Object rolesArr[] = roles.split(",");
		long orgNodeLevel = Long.valueOf(paramMap.get("orgNodeLevel").toString());
		long custProdId = Long.valueOf(paramMap.get("custProdId").toString());
		String customerCode = (String)paramMap.get("customerCode");
		
		logger.log(IAppLogger.INFO, "roles = " + roles);
		logger.log(IAppLogger.INFO, "orgNodeLevel = " + orgNodeLevel);
		logger.log(IAppLogger.INFO, "custProdId = " + custProdId);
		logger.log(IAppLogger.INFO, "customerCode = " + customerCode);
		
		Aggregation agg = newAggregation(
				match(Criteria.where("project_Id").is(Utils.getContractName().toUpperCase())),
				unwind("reportAccess"),
				match(Criteria.where("reportAccess.roles").in(rolesArr)
						.andOperator(Criteria.where("reportAccess.orgLevel").is(String.valueOf(orgNodeLevel)),
									 Criteria.where("reportAccess.customerCode").regex(customerCode),
									 Criteria.where("reportAccess.customerCode").is(String.valueOf(customerCode)))),
				unwind("reportAccess.actionAccess"),
				match(Criteria.where("reportAccess.actionAccess.status").is(IApplicationConstants.ACTIVE_FLAG)),
				project("reportName","reportAccess.actionAccess.action")
		);

		//Convert the aggregation result into a List
		AggregationResults<MFlatActionAccessTO> groupResults 
				= getMongoTemplatePrism("global")
				.aggregate(agg, MReportConfigTO.class, MFlatActionAccessTO.class);
		
		List<MFlatActionAccessTO> reportDetails = groupResults.getMappedResults();
		
		System.out.println("    >> User from MongoDB : "
				+ reportDetails.get(0).getReportName() + " " + reportDetails.get(0).getActionAccess().getAction());
		
		Map<String,String> actionMap = new LinkedHashMap<String,String>();
		for(int i=0; i < reportDetails.size(); i++) {
			actionMap.put(reportDetails.get(i).getReportName()+ " " + reportDetails.get(i).getActionAccess().getAction(), 
					reportDetails.get(i).getReportName()+ " " + reportDetails.get(i).getActionAccess().getAction());

		}
						
		return actionMap;
		
		/*return (Map<String,String>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
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
		});*/
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
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(Utils.getProject(contractName)));
		MCustProdAdminTO custProdAdmin = getMongoTemplatePrism("global").findOne(searchUserQuery, MCustProdAdminTO.class);
		
		// get all products
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		if(custProdAdmin != null) {
			ProjectProp projectProp = custProdAdmin.getUiConfig();
			propertyMap = projectProp.getProperties();
		}
		
		return propertyMap;
	}
	
	/**
	 * get all passwords from history
	 * @param username
	 * @return
	 */
	public List<String> getPasswordHistory(Map<String, Object> paramMap) {		
		
		String username = (String)paramMap.get("userName");
		String contractName = (String) paramMap.get("contractName");
		if(contractName == null) {
			contractName = Utils.getContractName();
		}
		
		Query searchPasswordHisQuery = new Query(Criteria.where("_id").is(username).and("project_id").is(Utils.getProject(contractName)));
		
		searchPasswordHisQuery.fields().include("pwdHistory");
		
		paramMap.remove("userName"); //This is required so that cache will get data based on single parameter (contract) only  
		Map<String, Object> propertyMap = getContractProerty(paramMap);
		final int pwdHistoryDay =
			propertyMap.get("password.history.day")!= null ? Integer.parseInt((String)propertyMap.get("password.history.day")): 0;
		
		searchPasswordHisQuery.limit(pwdHistoryDay);
		
		searchPasswordHisQuery.with(new Sort(Sort.Direction.DESC,"date"));
		
		
		List<PwdHistory> pwdHistoryList = getMongoTemplatePrism(contractName).find(searchPasswordHisQuery, PwdHistory.class);
		List<String> pwsString = new LinkedList<String>();
		if(pwdHistoryList!=null && pwdHistoryList.size() > 0){
			for(int i=0; i< pwdHistoryList.size(); i++)
			pwsString.add(pwdHistoryList.get(i).getPwd());
		}
		
		return pwsString;	
	}
	

//	@Cacheable(value = "adminCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramUserMap)).concat('getUserEmail'))")
	public UserTO getUserEmail(Map<String,Object> paramUserMap) {
		final String userName = (String)paramUserMap.get("username"); 
		String contractName  = (String)paramUserMap.get("contractName"); 
		
		Query searchUserQuery = new Query(Criteria.where("_id").is(userName).and("project_id").is(Utils.getProject(contractName)));
		MUserTO savedUser = getMongoTemplatePrism().findOne(searchUserQuery, MUserTO.class);
		
		UserTO userDetails = new UserTO();
		if( savedUser != null ) {
			userDetails.setUserId(savedUser.get_id());
			userDetails.setUserEmail(savedUser.getEmail());
			userDetails.setSalt(savedUser.getSalt());
		}
		
		return userDetails;
	}
	
	public boolean checkOrgHierarchy(Map<String, Object> paramMap){
		
		logger.log(IAppLogger.INFO, "Enter: LoginDAOImpl - checkOrgHierarchy()");
		long t1 = System.currentTimeMillis();
		
		final String userName = (String)paramMap.get("username"); 
		final String custProdId = (String)paramMap.get("custProdId"); 
		final String prevOrgId = (String)paramMap.get("prevOrgId");
		String contractName = (String)paramMap.get("contractName"); 
		if(contractName == null) contractName = Utils.getContractName();
		long existFlag;
		boolean returnFlag = Boolean.FALSE;
		Object orgArray[] = new Object[1];
		orgArray[0] = "0";
		
		Aggregation agg = newAggregation(
				match(Criteria.where("project_id").is(contractName.toUpperCase())
						.andOperator(Criteria.where("_id").is(userName),Criteria.where("orgCategory.level").nin(orgArray))),
				unwind("orgUser"),
				match(Criteria.where("orgUser.org_id").regex("^"+prevOrgId))); //("/^0~TASCCA/i")));
		
		//Convert the aggregation result into a List
		AggregationResults<MFlatUserTO> groupResults 
				= getMongoTemplatePrism(contractName)
				.aggregate(agg, MUserTO.class, MFlatUserTO.class);
		
		List<MFlatUserTO> usersDetails = groupResults.getMappedResults();
		System.out.println("    >> User from MongoDB : "
				+ usersDetails.get(0).get_id() );

		existFlag = usersDetails.size();
			
			if(existFlag > 0 ){
				returnFlag = Boolean.TRUE;
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
