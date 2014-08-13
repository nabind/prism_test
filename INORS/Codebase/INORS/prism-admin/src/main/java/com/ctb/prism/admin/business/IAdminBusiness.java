package com.ctb.prism.admin.business;

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

public interface IAdminBusiness {

	/**
	 * @param nodeid
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param nodeid
	 * @param currorg
	 * @param adminYear
	 * @param searchParam
	 * @param customerid
	 * @return
	 * @throws Exception
	 */
	public ArrayList<UserTO> getUserDetailsOnClick(String nodeid, String currorg, String adminYear, String searchParam, String customerid, String orgMode) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public UserTO getEditUserData(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param Id
	 * @param userId
	 * @param userName
	 * @param emailId
	 * @param password
	 * @param userStatus
	 * @param userRoles
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public boolean updateUser(String Id, String userId, String userName, String emailId, String password, String userStatus, String[] userRoles) throws BusinessException, Exception;

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
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId);

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public UserTO addNewUser(Map<String, Object> paramMap);

	/**
	 * @param paramMap
	 * @return
	 */
	public String searchUserAutoComplete(Map<String, Object> paramMap);

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
	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam, long customerId, String orgMode);

	/**
	 * @param tenantId
	 * @param adminYear
	 * @param customerId
	 * @param orgMode
	 * @return
	 */
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId, String orgMode);

	/**
	 * @param orgName
	 * @param tenantId
	 * @param adminYear
	 * @param customerId
	 * @return
	 */
	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId, String orgMode);

	/**
	 * @param orgName
	 * @param tenantId
	 * @param adminYear
	 * @param customerId
	 * @return
	 */
	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId, String orgMode);

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
	 * @param roleId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public boolean associateUserToRole(String roleId, String userName) throws Exception;

	/**
	 * @param roleId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception;

	/**
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
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param selectedOrgId
	 * @param parentOrgId
	 * @param userId
	 * @param customerId
	 * @param isRedirected
	 * @return
	 * @throws Exception
	 */
	public String getOrganizationTreeOnRedirect(String selectedOrgId, String parentOrgId, String userId, long customerId, boolean isRedirected) throws Exception;

	/**
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public com.ctb.prism.login.transferobject.UserTO resetPassword(String userName) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public List<ObjectValueTO> getAllAdmin() throws Exception;

	/**
	 * @param stgOrgTO
	 * @return
	 */
	public String addOrganization(StgOrgTO stgOrgTO);

	/**
	 * @param adminYear
	 * @param nodeId
	 * @param customerId
	 * @param gradeId
	 * @return
	 */
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId);

	/**
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
	public Map<String, Object> getEducationCenter(final Map<String, Object> paramMap) throws SystemException;

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
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Gets the user for Reset Password.
	 * 
	 * @param paramMap
	 * @return
	 */
	public UserTO getUserForResetPassword(Map<String, String> paramMap);

}
