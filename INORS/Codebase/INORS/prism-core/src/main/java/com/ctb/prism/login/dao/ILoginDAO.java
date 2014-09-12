package com.ctb.prism.login.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;


public interface ILoginDAO {

	public boolean selectTest();
//	public UserTO getUserByEmail(String userEmail) throws SystemException;
	public UserTO getUserByEmail(Map<String, Object> paramMap) throws SystemException;
	public List<GrantedAuthority> getGrantedAuthorities(String username);
	public String getTenantId( String userName );
	public String getSystemConfigurationMessage(Map<String,Object> paramMap);
	public String checkFirstTimeLogin(String username);
//	public UserTO getUserDetails(String username);
	public UserTO getUserDetails(Map<String, Object> paramMap);
	public UserTO getUsersForSSO(String orgId) throws Exception;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)throws BusinessException;
	public UserTO getUsersForSSO(UserTO userTO) throws Exception;
	public UserTO getOrgLevel(UserTO userTO);
	public boolean checkUserAvailability(String username);
	public String getUserOrgNode(String username, String contractName);
	public void updateUserOrg(String username, String OrgNodeId, String oldOrgNodeid, String contractName);
	public void addNewUser(Map<String,Object> paramMap) throws Exception;
	public String getRootPath(String customerId, String testAdmin);
	public Set<MenuTO> getMenuMap(Map<String, Object> paramMap);
	public Map<String,String> getActionMap(Map<String, Object> paramMap);
	public String getContractProerty (Map<String, Object> paramMap);
}
