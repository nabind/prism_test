package com.ctb.prism.login.dao;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.UserTO;

public interface ILoginDAO {

	public boolean selectTest();
	public UserTO getUserByEmail(String userEmail) throws SystemException;
	
	public List<GrantedAuthority> getGrantedAuthorities(String username);
	
	public String getTenantId( String userName );
	
	public String checkFirstTimeLogin(String username);
	
	public UserTO getUserDetails(String username);
	
	public UserTO getUsersForSSO(String orgId) throws Exception;
	public UserTO getOrgLevel(UserTO userTO);
}
