package com.ctb.prism.login.Service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.UserTO;
import java.util.Map;


public interface ILoginService {

	public boolean selectTest();
	public UserTO getUserByEmail(String userEmail) throws SystemException;
	
	public List<GrantedAuthority> getGrantedAuthorities(String username);
	
	public String getTenant(String username);
	
	public boolean checkFirstTimeLogin(String username);
	
	public String getSystemConfigurationMessage(Map<String,Object> paramMap);
	
	public UserTO getUserDetails(String username);
	
	public UserTO getUsersForSSO(UserTO userTO) throws Exception;
	public UserTO getOrgLevel(UserTO userTO);
	public boolean checkUserAvailability(String username);
	public void addNewUser(Map<String,Object> paramMap) throws Exception;
}
