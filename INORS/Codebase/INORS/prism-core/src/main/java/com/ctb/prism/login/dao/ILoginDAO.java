package com.ctb.prism.login.dao;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.UserTO;
import java.util.Map;


public interface ILoginDAO {

	public boolean selectTest();
	public UserTO getUserByEmail(String userEmail) throws SystemException;
	public List<GrantedAuthority> getGrantedAuthorities(String username);
	public String getTenantId( String userName );
	public String getSystemConfigurationMessage(Map<String,Object> paramMap);
	public String checkFirstTimeLogin(String username);
	public UserTO getUserDetails(String username);
	public UserTO getUsersForSSO(String orgId) throws Exception;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)throws BusinessException;
	public UserTO getUsersForSSO(UserTO userTO) throws Exception;
	public UserTO getOrgLevel(UserTO userTO);
	public boolean checkUserAvailability(String username);
	public void addNewUser(Map<String,Object> paramMap) throws Exception;
}
