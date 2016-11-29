package com.ctb.prism.login.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.security.CustomGrantedAuthority;
import com.ctb.prism.login.transferobject.UserTO;
import com.googlecode.ehcache.annotations.Cacheable;

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
		List<GrantedAuthority> userPerms = getJdbcTemplatePrism().query(
				IQueryConstants.SELECT_USER_AUTHORITIES,
				new String[] { username }, new RowMapper<GrantedAuthority>() {
					public GrantedAuthority mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new SimpleGrantedAuthority(rs.getString(1));
					}
				});
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
	@Cacheable(cacheName = "tenantId")
	public String getTenantId( String userName ) {
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_TENANT_ID, new Object[]{ userName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				return ((BigDecimal) rs.getObject(1)).toString();
			}
		});
	}
	
	/**
	 * Retrieves and returns loggedin user details
	 * @param username
	 * @return UserTO with associated orgId, userid and firsttime loggedin flag
	 */
	public UserTO getUserDetails(String username) {
		UserTO user = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(IQueryConstants.GET_USER_DETAILS, username);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				user = new UserTO();
				user.setFirstTimeLogin(fieldDetails.get("IS_FIRSTTIME_LOGIN").toString());
				user.setUserId(fieldDetails.get("USER_ID").toString());
				user.setOrgId(fieldDetails.get("ORG_ID").toString());
				user.setDisplayName((fieldDetails.get("DISPLAY_USERNAME") != null) ? 
						fieldDetails.get("DISPLAY_USERNAME").toString() : "Anonymous");
				user.setUserStatus(fieldDetails.get("ACTIVATION_STATUS").toString());
				user.setUserName(username);
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
		Object[] params = null;
		int[] types = null;
		UserTO user = null;
		
		
		/*params = new Object[]{userEmail};
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
		}*/
		
		user = new UserTO();
		if("abc@gmail.com".equals(userEmail)) {
			user.setUserName("rod");
		} else {
			user.setUserName("school");
		}
		
		user.setPassword("c20ad4d76fe97759aa27a0c99bff6710");
		user.setUserEmail("abc@gmail.com");
		user.setUserStatus("active");
		user.setIsAdminFlag("Y");
		
		logger.log(IAppLogger.INFO,	"Exit: LoginDAOImpl - getUserByEmail");
		return user;
	}
}
