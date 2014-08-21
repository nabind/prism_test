package com.ctb.prism.login.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.ctb.prism.login.transferobject.UserTO;
//import com.googlecode.ehcache.annotations.Cacheable;
//import com.googlecode.ehcache.annotations.TriggersRemove;

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
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )")
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
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (#p0).concat(#root.method.name) )")
	private String getUserType(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_TYPE, username);
		if (!lstData.isEmpty()) {
			return IApplicationConstants.PARENT_USER_FLAG;
		} else {
			return IApplicationConstants.ORG_USER_FLAG;
		}
	}
	
	/**
	 * Retrieves and returns loggedin user details
	 * @param username
	 * @return UserTO with associated orgId, userid and firsttime loggedin flag
	 */
	public UserTO getUserDetails(String username) {
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
	
	public UserTO getUserByEmail(String userEmail) throws SystemException
	{
		logger.log(IAppLogger.INFO,	"Enter: LoginDAOImpl - getUserByEmail");
		/*Object[] params = null;
		int[] types = null;
		UserTO user = null;
		
		
		params = new Object[]{userEmail};
		types = new int[]{Types.VARCHAR};	
		List<Map<String,Object>> userRows = getJdbcTemplate().queryForList(IQueryConstants.GET_USER_BY_EMAIL, params, types);
		if(userRows.size() == 1){
			for (Map row : userRows) {			
				user = new UserTO();
				user.setUserName((String)row.get("USER_ID"));
				user.setPassword((String)row.get("USER_PASSWD_HASH"));//"c20ad4d76fe97759aa27a0c99bff6710");
				user.setUserEmail((String)row.get("USER_EMAIL"));
				user.setUserStatus((String)row.get("USER_STATUS"));
				user.setIsAdminFlag((String)row.get("IS_SUPER_ADMIN"));
			}
		} else {
			// all the failure scenario is taken care of by the security framework;
		}
		
		user = new UserTO();
		if("devadmin".equals(userEmail)) {
			user.setUserName("devadmin");
		} else {
			user.setUserName("school");
		}
		
		user.setPassword("e64b78fc3bc91bcbc7dc232ba8ec59e0");
		user.setUserEmail("abc@gmail.com");
		user.setUserStatus("active");
		user.setIsAdminFlag("Y");
		
		logger.log(IAppLogger.INFO,	"Exit: LoginDAOImpl - getUserByEmail");*/
		return getUserDetails(userEmail);
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
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORG_LEVEL, 
				argUserTO.getOrgCode(), argUserTO.getCustomerId(), argUserTO.getOrgNodeLevelStr());
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
	 * @author Arunavo
	 * Retrieves and returns message corresponding configured in database
	 * @param reportName, messageType, messageName
	 * @return message
	 */
	@Cacheable(value = "configCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getSystemConfigurationMessage') )")
	public String getSystemConfigurationMessage(Map<String,Object> paramMap){
		logger.log(IAppLogger.INFO, "Enter: LoginDAOImpl - getSystemConfigurationMessage()");
		long t1 = System.currentTimeMillis();
		
		final String reportName = (String) paramMap.get("REPORT_NAME");
		final String messageType = (String) paramMap.get("MESSAGE_TYPE");
		final String messageName = (String) paramMap.get("MESSAGE_NAME");
		
		String systemMessage = "";
		try {
			systemMessage = (String) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall("{call " + IQueryConstants.GET_SYSTEM_CONFIGURATION_MESSAGE + "}");
					cs.setString(1, reportName);
					cs.setString(2, messageType);
					cs.setString(3, messageName);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					String systemMessageResult = "";
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(4);
						if (rs.next()) {
							systemMessageResult = rs.getString("REPORT_MSG");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return systemMessageResult.trim();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: LoginDAOImpl - getSystemConfigurationMessage() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return systemMessage;
	}
	
	/*
	 * Get custprodid along with product - By Joy
	 * */
	@Cacheable(value = "defaultCache", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( (T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat('getCustomerProduct') )")
	@SuppressWarnings("unchecked")
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)
			throws BusinessException {
		logger.log(IAppLogger.INFO, "Enter: getCustomerProduct()");
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueTOList = null;
		long t1 = System.currentTimeMillis();
		final UserTO loggedinUserTO = (UserTO) paramMap.get("loggedinUserTO");
		try{
			objectValueTOList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(
				    new CallableStatementCreator() {
				        public CallableStatement createCallableStatement(Connection con) throws SQLException {
				        	CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_TEST_ADMINISTRATION + "}");
				            cs.setLong(1, Long.valueOf(loggedinUserTO.getCustomerId()));	
				            cs.setString(2, loggedinUserTO.getOrgId());
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
	public boolean checkUserAvailability(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.VALIDATE_USER_NAME, username);
		if (lstData == null || lstData.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * check provided user availability and return org_nodeid
	 * 
	 * @return String
	 */
	public String getUserOrgNode(String username) {
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.VALIDATE_USER_NAME, username);
		if (lstData != null && !lstData.isEmpty()) {
			String orgNodeId = "";
			List<Map<String, Object>> usrData = null;
			usrData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_USER_ORG, username);
			if (usrData.size() > 0) {
				for (Map<String, Object> fieldDetails : usrData) {
					orgNodeId = fieldDetails.get("NODEID").toString();
				}
			}
			return orgNodeId;
		}
		return null;
	}
	
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_USER, OrgNodeId, oldOrgNodeid, username);
	}
	
	/**
	 * add new user
	 * @param String userId, String tenantId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles
	 * @return UserTO
	 */
	@CacheEvict(value="adminCache", allEntries=true)
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
		
		
		final StringBuilder roles = new StringBuilder();
		try {
			ObjectValueTO currAdmin = getCurrentAdmin();
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
			
			String nodeId = (String) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				String salt = PasswordGenerator.getNextSalt();
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.CREATE_USER);
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
					cs.registerOutParameter(15, oracle.jdbc.OracleTypes.NUMBER);
					cs.registerOutParameter(16, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					String strNodeId = null; 
					try {
						cs.execute();
						strNodeId =  cs.getString(15);
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return strNodeId;
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
	 * get user list for selected role
	 * @param role id
	 * @return List of users
	 */
	public ObjectValueTO getCurrentAdmin() throws SystemException {
		ObjectValueTO to = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.CURR_ADMIN_YEAR);
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
	@Cacheable(value = "configCache", key="(#customerId).concat(#testAdmin).concat(#root.method.name)")
	public String getRootPath(String customerId, String testAdmin) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_ROOT_PATH, new Object[] { customerId, testAdmin }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return rs.getString(1);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.login.dao.ILoginDAO#getMenuMap(java.util.Map)
	 */
	public Map<String, String> getMenuMap(Map<String, Object> paramMap) {
		final Long userId = Long.parseLong((paramMap.get("userId") == null) ? "0" : paramMap.get("userId").toString());
		logger.log(IAppLogger.INFO, "userId = " + userId);
		return (Map<String, String>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(IQueryConstants.SP_GET_MENU_MAP);
				cs.setLong(1, userId);
				cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
				cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				ResultSet rs = null;
				Map<String, String> menuMap = new HashMap<String, String>();
				try {
					cs.execute();
					rs = (ResultSet) cs.getObject(2);
					while (rs.next()) {
						String key = rs.getString("KEY");
						String value = rs.getString("VALUE");
						menuMap.put(key, value);
					}
					Utils.logError(cs.getString(3));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.log(IAppLogger.INFO, "menuMap = " + menuMap);
				return menuMap;
			}
		});
	}
	
}
