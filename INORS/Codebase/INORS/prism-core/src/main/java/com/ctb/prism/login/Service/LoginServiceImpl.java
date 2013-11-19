package com.ctb.prism.login.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.business.ILoginBusiness;
import com.ctb.prism.login.transferobject.UserTO;


@Service
public class LoginServiceImpl implements ILoginService{
	
	@Autowired
	private ILoginBusiness loginBusiness;
	
	public boolean selectTest(){
		return loginBusiness.selectTest();
	}
	public UserTO getUserByEmail(String userEmail) throws SystemException{
		return loginBusiness.getUserByEmail(userEmail);
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(String username) {
		return loginBusiness.getGrantedAuthorities(username);
	}
	
	public String getTenant(String username) {
		return loginBusiness.getTenant(username);
	}
	public boolean checkFirstTimeLogin(String username) {
		return loginBusiness.checkFirstTimeLogin(username);
	}
	public UserTO getUserDetails(String username) {
		return loginBusiness.getUserDetails(username);
	}
	public UserTO getUsersForSSO(String orgId) throws Exception {
		return loginBusiness.getUsersForSSO(orgId);
	}
	public UserTO getOrgLevel(UserTO userTO) {
		return loginBusiness.getOrgLevel(userTO);
	}
	
}
