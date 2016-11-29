package com.ctb.prism.login.Service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.UserTO;

public interface ILoginService {

	public boolean selectTest();
	public UserTO getUserByEmail(String userEmail) throws SystemException;
	
	public List<GrantedAuthority> getGrantedAuthorities(String username);
	
	public String getTenant(String username);
	
	public boolean checkFirstTimeLogin(String username);
	
	public UserTO getUserDetails(String username);
}
