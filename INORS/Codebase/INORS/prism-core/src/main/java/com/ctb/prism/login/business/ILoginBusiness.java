package com.ctb.prism.login.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;

public interface ILoginBusiness {

	public boolean selectTest();
	//public UserTO getUserByEmail(String userEmail) throws SystemException;
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException;

	public List<GrantedAuthority> getGrantedAuthorities(String username);
	
	public String getTenant(String username);
	
	public Map<String,Object> getSystemConfigurationMessage(Map<String,Object> paramMap);
	
	public boolean checkFirstTimeLogin(String username);
	
	//public UserTO getUserDetails(String username);
	
	public UserTO getUserDetails(Map<String, Object> paramMap);
	
	public UserTO getUsersForSSO(UserTO userTO) throws Exception;
	public UserTO getOrgLevel(UserTO userTO);
	public boolean checkUserAvailability(String username);
	public String getUserOrgNode(String username, String contractName);
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid, String contractName);
	public void addNewUser(Map<String,Object> paramMap) throws Exception;
	public String getRootPath(String customerId, String testAdmin);
	public Set<MenuTO> getMenuMap(Map<String, Object> paramMap);
	public Map<String,String> getActionMap(Map<String, Object> paramMap);
	public Map<String, Object> getContractProerty (Map<String, Object> paramMap);
}
