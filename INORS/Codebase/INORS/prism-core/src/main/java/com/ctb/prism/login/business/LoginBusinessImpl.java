package com.ctb.prism.login.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.UserTO;
import java.util.Map;




@Component
public class LoginBusinessImpl implements ILoginBusiness{

	private static final IAppLogger logger = LogFactory.getLoggerInstance(LoginBusinessImpl.class.getName());
	
	@Autowired
	private ILoginDAO loginDAO;
	
	public boolean selectTest(){
		return loginDAO.selectTest();
	}
	
	public UserTO getUserByEmail(String userEmail) throws SystemException{
		return loginDAO.getUserByEmail(userEmail);
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(String username) {
		return loginDAO.getGrantedAuthorities(username);
	}
	
	public String getTenant(String username) {
		return loginDAO.getTenantId(username);
	}

	public String getSystemConfigurationMessage(Map<String,Object> paramMap) {
		return loginDAO.getSystemConfigurationMessage(paramMap);
	}
	

	public boolean checkFirstTimeLogin(String username) {
		if(IApplicationConstants.FLAG_Y.equals( loginDAO.checkFirstTimeLogin(username) )) {
			return true;
		} else {
			return false;
		}
	}

	public UserTO getUserDetails(String username) {
		return loginDAO.getUserDetails(username);
	}
	
	public UserTO getUsersForSSO(String orgId) throws Exception {
		return loginDAO.getUsersForSSO(orgId);
	}
	
	public UserTO getOrgLevel(UserTO userTO) {
		return loginDAO.getOrgLevel(userTO);
	}
}
