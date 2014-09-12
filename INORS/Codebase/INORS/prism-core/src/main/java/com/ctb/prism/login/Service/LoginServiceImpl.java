package com.ctb.prism.login.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.business.ILoginBusiness;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;

/**
 * @author TCS
 * 
 */
@Service
public class LoginServiceImpl implements ILoginService {

	@Autowired
	private ILoginBusiness loginBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#selectTest()
	 */
	public boolean selectTest() {
		return loginBusiness.selectTest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getUserByEmail(java.lang.String)
	 */
	/*	public UserTO getUserByEmail(String userEmail) throws SystemException {
		return loginBusiness.getUserByEmail(userEmail);
	}*/
	
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException {
		return loginBusiness.getUserByEmail(paramMap);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getGrantedAuthorities(java.lang.String)
	 */
	public List<GrantedAuthority> getGrantedAuthorities(String username) {
		return loginBusiness.getGrantedAuthorities(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getTenant(java.lang.String)
	 */
	public String getTenant(String username) {
		return loginBusiness.getTenant(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getSystemConfigurationMessage(java.util.Map)
	 */
	public Map<String,Object> getSystemConfigurationMessage(Map<String, Object> paramMap) {
		return loginBusiness.getSystemConfigurationMessage(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#checkFirstTimeLogin(java.lang.String)
	 */
	public boolean checkFirstTimeLogin(String username) {
		return loginBusiness.checkFirstTimeLogin(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getUserDetails(java.lang.String)
	 */
/*	public UserTO getUserDetails(String username) {
		return loginBusiness.getUserDetails(username);
	}*/
	
	public UserTO getUserDetails(Map<String, Object> paramMap) {
		return loginBusiness.getUserDetails(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getUsersForSSO(com.ctb.prism.login.transferobject.UserTO)
	 */
	public UserTO getUsersForSSO(UserTO userTO) throws Exception {
		return loginBusiness.getUsersForSSO(userTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getOrgLevel(com.ctb.prism.login.transferobject.UserTO)
	 */
	public UserTO getOrgLevel(UserTO userTO) {
		return loginBusiness.getOrgLevel(userTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(String username) {
		return loginBusiness.checkUserAvailability(username);
	}
	
	public String getUserOrgNode(String username, String contractName) {
		return loginBusiness.getUserOrgNode(username,contractName);
	}
	
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid, String contractName) {
		loginBusiness.updateUserOrg(username, OrgNodeId, oldOrgNodeid, contractName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#addNewUser(java.util.Map)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addNewUser(Map<String, Object> paramMap) throws Exception {
		loginBusiness.addNewUser(paramMap);
	}
	
	public String getRootPath(String customerId, String testAdmin) {
		return loginBusiness.getRootPath(customerId, testAdmin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.login.Service.ILoginService#getMenuMap(java.util.Map)
	 */
	public Set<MenuTO> getMenuMap(Map<String, Object> paramMap) {
		return loginBusiness.getMenuMap(paramMap);
	}
	
	public Map<String,String> getActionMap(Map<String, Object> paramMap) {
		return loginBusiness.getActionMap(paramMap);
	}
	
	public String getContractProerty (Map<String, Object> paramMap) {
		return loginBusiness.getContractProerty(paramMap);
	}
}
