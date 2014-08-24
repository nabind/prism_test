package com.ctb.prism.login.Service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.UserTO;

/**
 * @author TCS
 * 
 */
public interface ILoginService {

	/**
	 * @return
	 */
	public boolean selectTest();

	/**
	 * @param userEmail
	 * @return
	 * @throws SystemException
	 */
	public UserTO getUserByEmail(String userEmail) throws SystemException;
	public UserTO getUserByEmail(String userEmail, String contractName) throws SystemException;

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
	public UserTO getUserDetails(String username);

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
	public String getUserOrgNode(String username);
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid);


	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public void addNewUser(Map<String, Object> paramMap) throws Exception;
	public String getRootPath(String customerId, String testAdmin);

	/**
	 * @param paramMap
	 * @return
	 */
	public Map<String, String> getMenuMap(Map<String, Object> paramMap);
}
