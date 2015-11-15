package com.ctb.prism.admin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;

/**
 * @author TCS
 * 
 */
public interface IAdminDAO {

	/**
	 * @param nodeid
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String, Object> paramMap) throws Exception;

	/**
	 * Returns the userList on load.
	 * 
	 * @param paramUserMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<UserTO> getUserDetailsOnClick(Map<String,Object> paramUserMap) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public UserTO getEditUserData(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap < Id,userId,userName,emailId,password,userStatus,userRoles >
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public boolean updateUser(Map<String, Object> paramMap) throws BusinessException, Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUser(Map<String, Object> paramMap) /*throws Exception*/;

	/**
	 * @param orgLevel
	 * @param customerId
	 * @return
	 */
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) throws Exception;

	/**
	 * add new user
	 * 
	 * @param paramMap
	 * @param searchParamMap
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public UserTO addNewUser(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param userName
	 * @param parentId
	 * @param adminYear
	 * @param isExactSearch
	 * @return
	 */
	public ArrayList<UserTO> searchUser(String userName, String parentId, String adminYear, String isExactSearch, String orgMode);

	/**
	 * @param paramMap
	 * @return
	 */
	public List<EduCenterTO> searchEduUser(Map<String, Object> paramMap);

	/**
	 * @param paramMap
	 * @return
	 */
	public String searchUserAutoComplete(Map<String, Object> paramMap);

	/**
	 * @param tenantId
	 * @param adminYear
	 * @param customerId
	 * @return
	 */
	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId);

	/**
	 * @param parentTenantId
	 * @param adminYear
	 * @param searchParam
	 * @param customerId
	 * @return
	 */
	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam, long customerId, String orgMode, String moreCount);

	/**
	 * @param tenantId
	 * @param adminYear
	 * @param customerId
	 * @param orgMode
	 * @return
	 */
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId, String orgMode);

	/**
	 * @param paramMap
	 * @return
	 */
	public List<OrgTO> searchOrganization(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 */
	public String searchOrgAutoComplete(Map<String, Object> paramMap) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public ArrayList<RoleTO> getRoleDetails() throws Exception;

	/**
	 * @param roleid
	 * @param currentOrg
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public ArrayList<UserTO> getUsersForSelectedRole(String roleid, String currentOrg, String customer) throws Exception;

	/**
	 * @param roleid
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRole(String roleid) throws Exception;

	/**
	 * @param roleid
	 * @return
	 * @throws Exception
	 */
	public RoleTO getRoleDetailsById(Map<String, Object> paramMap) throws Exception;

	/**
	 * Associate user for that role in database through associate button in edit role popup screen.
	 * 
	 * @param roleId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public boolean associateUserToRole(String roleId, String userName) throws Exception;

	/**
	 * Delete user for that role in database through delete button in edit role popup screen.
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception;

	/**
	 * Updates the particular role information in database through save button in edit role popup screen.
	 * 
	 * @param roleId
	 * @param roleName
	 * @param roleDescription
	 * @return
	 * @throws Exception
	 */
	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception;
	
	
	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String, Object> paramMap) throws Exception;

	/**
	 * Returns the organizationList to create a tree structure. for manage organizations.
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String getOrganizationTreeOnRedirect(Map<String, Object> paramMap) throws Exception;

	/**
	 * Reset the user password into ldap/dao.
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public com.ctb.prism.login.transferobject.UserTO resetPassword(Map<String, Object> paramMap) throws Exception;

	/**
	 * Get user list for selected role.
	 * 
	 * @return List of users
	 * @throws SystemException
	 */
	public List<ObjectValueTO> getAllAdmin() throws SystemException;

	/**
	 * Add organization by web service.
	 * 
	 * @param stgOrgTO
	 * @return
	 */
	public String addOrganization(StgOrgTO stgOrgTO);

	/**
	 * Get list of students for a leaf nodes.
	 * 
	 * @param adminYear
	 * @param nodeId
	 * @param customerId
	 * @param gradeId
	 * @return
	 */
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId);

	/**
	 * Returns the organizationList to create a tree structure.
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTreeTO> getHierarchy(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getEducationCenter(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<EduCenterTO> loadEduCenterUsers(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 */
	public List<UserDataTO> getUserData(Map<String, String> paramMap);

	/**
	 * Gets the user for Reset Password.
	 * 
	 * @param paramMap
	 * @return
	 */
	public UserTO getUserForResetPassword(Map<String, String> paramMap)throws SystemException;

	/**
	 * Gets the role list for the user.
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<RoleTO> getEduUserRoleList(Map<String, String> paramMap)throws Exception;
}
