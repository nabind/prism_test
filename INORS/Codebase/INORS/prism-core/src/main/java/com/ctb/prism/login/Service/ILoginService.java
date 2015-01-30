package com.ctb.prism.login.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;

/**
 * @author TCS
 * 
 */
public interface ILoginService {

	/**
	 * @param userEmail
	 * @return
	 * @throws SystemException
	 */
//	public UserTO getUserByEmail(String userEmail) throws SystemException;
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param username
	 * @return
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String username);

	/**
	 * @param username
	 * @return
	 */
	public String getTenant(String username);

	/**
	 * @param username
	 * @return
	 */
	public boolean checkFirstTimeLogin(String username);

	/**
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getSystemConfigurationMessage(Map<String, Object> paramMap);

	/**
	 * @param username
	 * @return
	 */
	//public UserTO getUserDetails(String username);

	/**
	 * @param paramMap
	 * @return
	 */
	public UserTO getUserDetails(Map<String, Object> paramMap);
	
	/**
	 * @param userTO
	 * @return
	 * @throws Exception
	 */
	public UserTO getUsersForSSO(UserTO userTO) throws Exception;

	/**
	 * @param userTO
	 * @return
	 */
	public UserTO getOrgLevel(UserTO userTO);

	/**
	 * @param username
	 * @return
	 */
	public boolean checkUserAvailability(String username);
	public String getUserOrgNode(String username, String contractName);
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid, String contractName);


	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public void addNewUser(Map<String, Object> paramMap) throws Exception;
	public String getRootPath(String customerId, String testAdmin, String contractName);

	/**
	 * @param paramMap
	 * @return
	 */
	public Set<MenuTO> getMenuMap(Map<String, Object> paramMap);
	
	/**
	 * @param paramMap
	 * @return
	 */
	public Map<String,String> getActionMap(Map<String, Object> paramMap);
	
	public Map<String, Object>  getContractProerty (Map<String, Object> paramMap);
	
	public List<String> getPasswordHistory(Map<String, Object> paramMap);
	
	public boolean checkOrgHierarchy(Map<String, Object> paramMap);

	public void checkUserRoleByUsername(Map<String, Object> paramMap);
}
