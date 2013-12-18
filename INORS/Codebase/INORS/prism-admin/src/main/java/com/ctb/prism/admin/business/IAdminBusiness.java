package com.ctb.prism.admin.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.admin.transferobject.ObjectValueTO;

public interface IAdminBusiness {

	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid)
			throws Exception;	
	
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String,Object> paramMap)
			throws Exception;

	public ArrayList<UserTO> getUserDetailsOnClick(String nodeid,String currorg, String adminYear, String searchParam,String customerid)
			throws Exception;

	public UserTO getEditUserData(Map<String,Object> paramMap) throws Exception;

	public boolean updateUser(String Id, String userId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles) throws BusinessException, Exception;

	public boolean deleteUser(Map<String,Object> paramMap)
			throws Exception;

	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId);

	public UserTO addNewUser(Map<String,Object> paramMap) throws BusinessException, Exception;

	public String searchUserAutoComplete(Map<String,Object> paramMap);
	
	public ArrayList <UserTO> searchUser(String userName, String parentId, String adminYear,String isExactSearch);
	
	public List<EduCenterTO> searchEduUser(Map<String,Object> paramMap);
	
	// manage org
	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId);

	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear,String searchParam, long customerId);
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId);

	
	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId);

	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId);

	// manage roles
	public ArrayList<RoleTO> getRoleDetails() throws Exception;

	public ArrayList<UserTO> getUsersForSelectedRole(String roleid)
			throws Exception;

	public boolean deleteRole(String roleid) throws Exception;

	public RoleTO getRoleDetailsById(String roleid) throws Exception;
	public boolean associateUserToRole(String roleId, String userName) throws Exception;
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception;
	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception;
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String,Object> paramMap)throws Exception;
	public ArrayList<OrgTreeTO> getOrgTree(Map<String,Object> paramMap) throws Exception;
	public String getOrganizationTreeOnRedirect(String selectedOrgId,String parentOrgId,String userId,long customerId, boolean isRedirected) throws Exception;
	public String resetPassword(String userName) throws Exception ;
	public List<ObjectValueTO> getAllAdmin() throws Exception;
	public String addOrganization(StgOrgTO stgOrgTO);
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId);
	public ArrayList<OrgTreeTO> getHierarchy(Map<String,Object> paramMap)throws Exception;
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException;
	public Map<String,Object> getEducationCenter(final Map<String,Object> paramMap)throws SystemException;
	public List<EduCenterTO> loadEduCenterUsers(final Map<String,Object> paramMap) throws SystemException;

	public List<UserDataTO> getUserData(Map<String, String> paramMap);
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)throws BusinessException;
	
}
