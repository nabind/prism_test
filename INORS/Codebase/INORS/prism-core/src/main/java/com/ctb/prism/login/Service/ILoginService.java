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
	public String getSystemConfigurationMessage(Map<String, Object> paramMap);

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

	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public void addNewUser(Map<String, Object> paramMap) throws Exception;
}
