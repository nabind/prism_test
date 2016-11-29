package com.ctb.prism.admin.dao;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.exception.BusinessException;

/**
 * @author TCS
 * 
 */
public interface IAdminDAO {

	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid)
	throws Exception;
	
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(String nodeid)
			throws Exception;

	public ArrayList<UserTO> getUserDetailsOnClick(String nodeid,String currorg, String adminYear, String searchParam)
			throws Exception;

	public UserTO getEditUserData(String nodeid) throws Exception;

	public boolean updateUser(String Id, String userId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles) throws BusinessException, Exception;

	public boolean deleteUser(String Id, String userName, String password)
			throws Exception;

	public List<RoleTO> getRoleOnAddUser(String orgLevel);

	public UserTO addNewUser(String userId, String tenantId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles, String orgLevel, String adminYear) throws BusinessException, Exception;
	

	public ArrayList <UserTO> searchUser(String userName, String parentId, String adminYear,String isExactSearch);
	
	public String searchUserAutoComplete( String userName, String parentId, String adminYear );	

	public List<OrgTO> getOrganizationList(String tenantId, String adminYear);

	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam);
	public OrgTO getTotalUserCount(String tenantId, String adminYear);

	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear);

	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear);

	public ArrayList<RoleTO> getRoleDetails() throws Exception;

	public ArrayList<UserTO> getUsersForSelectedRole(String roleid)
			throws Exception;

	public boolean deleteRole(String roleid) throws Exception;

	public RoleTO getRoleDetailsById(String roleid) throws Exception;
	
	public boolean associateUserToRole(String roleId, String userName) throws Exception;
	
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception;
	
	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception;
	
	public ArrayList<OrgTreeTO> getOrganizationTree(String nodeid,String currOrg,boolean isFirstLoad, String adminYear)throws Exception;
	public ArrayList<OrgTreeTO> getOrgTree(String nodeid,boolean isFirstLoad, String adminYear) throws Exception;
	public String getOrganizationTreeOnRedirect(String selectedOrgId,String parentOrgId,String userId,String userName, boolean isRedirected) throws Exception;

	public String resetPassword(String userName) throws Exception ;
	
	public List<com.ctb.prism.admin.transferobject.ObjectValueTO> getAllAdmin() throws Exception;
}
