package com.ctb.prism.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.prism.admin.business.IAdminBusiness;
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

@Service("adminService")
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private IAdminBusiness adminBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrganizationDetailsOnFirstLoad(java.lang.String)
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid) throws Exception {
		return adminBusiness.getOrganizationDetailsOnFirstLoad(nodeid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrganizationDetailsOnClick(java.util.Map)
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String, Object> paramMap) throws Exception {
		return adminBusiness.getOrganizationDetailsOnClick(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getUserDetailsOnClick(Map<String,Object> paramUserMap)
	 */
	public ArrayList<UserTO> getUserDetailsOnClick(Map<String,Object> paramUserMap) throws Exception {
		return adminBusiness.getUserDetailsOnClick(paramUserMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getEditUserData(java.util.Map)
	 */
	public UserTO getEditUserData(Map<String, Object> paramMap) throws Exception {
		return adminBusiness.getEditUserData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#updateUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean updateUser(String Id, String userId, String userName, String emailId, String password, String userStatus, String[] userRoles) throws BusinessException, Exception {
		return adminBusiness.updateUser(Id, userId, userName, emailId, password, userStatus, userRoles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#deleteUser(java.util.Map)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteUser(Map<String, Object> paramMap) /*throws Exception*/ {
		return adminBusiness.deleteUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getRoleOnAddUser(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) {
		return adminBusiness.getRoleOnAddUser(orgLevel, customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#addNewUser(java.util.Map)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public UserTO addNewUser(Map<String, Object> paramMap) {
		return adminBusiness.addNewUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#searchUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<UserTO> searchUser(String userName, String parentId, String adminYear, String isExactSearch, String orgMode) {
		return adminBusiness.searchUser(userName, parentId, adminYear, isExactSearch, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#searchEduUser(java.util.Map)
	 */
	public List<EduCenterTO> searchEduUser(Map<String, Object> paramMap) {
		return adminBusiness.searchEduUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#searchUserAutoComplete(java.util.Map)
	 */
	public String searchUserAutoComplete(Map<String, Object> paramMap) {
		return adminBusiness.searchUserAutoComplete(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrganizationList(java.lang.String, java.lang.String, long)
	 */
	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId) {
		return adminBusiness.getOrganizationList(tenantId, adminYear, customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrganizationChildren(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam, long customerId, String orgMode) {
		return adminBusiness.getOrganizationChildren(parentTenantId, adminYear, searchParam, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getTotalUserCount(java.lang.String, java.lang.String, long, String orgMode)
	 */
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId, String orgMode) {
		return adminBusiness.getTotalUserCount(tenantId, adminYear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#searchOrganization(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId, String orgMode) {
		return adminBusiness.searchOrganization(orgName, tenantId, adminYear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#searchOrgAutoComplete(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId, String orgMode) {
		return adminBusiness.searchOrgAutoComplete(orgName, tenantId, adminYear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getRoleDetails()
	 */
	public ArrayList<RoleTO> getRoleDetails() throws Exception {
		return adminBusiness.getRoleDetails();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getUsersForSelectedRole(java.lang.String)
	 */
	public ArrayList<UserTO> getUsersForSelectedRole(String roleid, String currentOrg, String customer) throws Exception {
		return adminBusiness.getUsersForSelectedRole(roleid, currentOrg, customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#deleteRole(java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteRole(String roleid) throws Exception {
		return adminBusiness.deleteRole(roleid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getRoleDetailsById(java.lang.String)
	 */
	public RoleTO getRoleDetailsById(Map<String,Object> paramMap) throws Exception {
		return adminBusiness.getRoleDetailsById(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#associateUserToRole(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean associateUserToRole(String roleId, String userName) throws Exception {
		return adminBusiness.associateUserToRole(roleId, userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#deleteUserFromRole(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception {
		return adminBusiness.deleteUserFromRole(roleId, userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#saveRole(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception {
		return adminBusiness.saveRole(roleId, roleName, roleDescription);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrganizationTree(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String, Object> paramMap) throws Exception {
		return adminBusiness.getOrganizationTree(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrgTree(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception {
		return adminBusiness.getOrgTree(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getOrganizationTreeOnRedirect(java.lang.String, java.lang.String, java.lang.String, long, boolean)
	 */
	public String getOrganizationTreeOnRedirect(String selectedOrgId, String parentOrgId, String userId, long customerId, boolean isRedirected) throws Exception {
		return adminBusiness.getOrganizationTreeOnRedirect(selectedOrgId, parentOrgId, userId, customerId, isRedirected);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#resetPassword(java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public com.ctb.prism.login.transferobject.UserTO resetPassword(String userName) throws Exception {
		return adminBusiness.resetPassword(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getAllAdmin()
	 */
	public List<ObjectValueTO> getAllAdmin() throws Exception {
		return adminBusiness.getAllAdmin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#addOrganization(com.ctb.prism.admin.transferobject.StgOrgTO)
	 */
	public String addOrganization(StgOrgTO stgOrgTO) {
		return adminBusiness.addOrganization(stgOrgTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getAllStudents(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId) {
		return adminBusiness.getAllStudents(adminYear, nodeId, customerId, gradeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getHierarchy(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getHierarchy(Map<String, Object> paramMap) throws Exception {
		return adminBusiness.getHierarchy(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#downloadStudentFile(java.util.Map)
	 */
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException {
		return adminBusiness.downloadStudentFile(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getEducationCenter(java.util.Map)
	 */
	public Map<String, Object> getEducationCenter(final Map<String, Object> paramMap) throws SystemException {
		return adminBusiness.getEducationCenter(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#loadEduCenterUsers(java.util.Map)
	 */
	public List<EduCenterTO> loadEduCenterUsers(final Map<String, Object> paramMap) throws SystemException {
		return adminBusiness.loadEduCenterUsers(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getUserData(java.util.Map)
	 */
	public List<UserDataTO> getUserData(Map<String, String> paramMap) {
		return adminBusiness.getUserData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.service.IAdminService#getCustomerProduct(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String, Object> paramMap) throws BusinessException {
		return adminBusiness.getCustomerProduct(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.service.IAdminService#getUserForResetPassword(java.util.Map)
	 */
	public UserTO getUserForResetPassword(Map<String, String> paramMap) {
		return adminBusiness.getUserForResetPassword(paramMap);
	}
}
