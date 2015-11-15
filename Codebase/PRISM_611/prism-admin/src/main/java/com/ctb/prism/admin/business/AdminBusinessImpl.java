/**
 * 
 */
package com.ctb.prism.admin.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.admin.dao.IAdminDAO;
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
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.login.dao.ILoginDAO;

/**
 * @author TCS
 * 
 */

@Component("adminBusiness")
public class AdminBusinessImpl implements IAdminBusiness {

	@Autowired
	private IAdminDAO adminDAO;

	@Autowired
	private ILoginDAO loginDAO;
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(AdminBusinessImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrganizationDetailsOnFirstLoad(Map<String, Object>)
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getOrganizationDetailsOnFirstLoad(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrganizationDetailsOnClick(java.util.Map)
	 */
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getOrganizationDetailsOnClick(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getUserDetailsOnClick(Map<String,Object> paramUserMap)
	 */
	public ArrayList<UserTO> getUserDetailsOnClick(Map<String,Object> paramUserMap) throws Exception {
		return adminDAO.getUserDetailsOnClick(paramUserMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getEditUserData(java.util.Map)
	 */
	public UserTO getEditUserData(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getEditUserData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#updateUser(Map<String, Object>)
	 */
	public boolean updateUser(Map<String, Object> paramMap) throws BusinessException, Exception {
		return adminDAO.updateUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#deleteUser(java.util.Map)
	 */
	public boolean deleteUser(Map<String, Object> paramMap) /*throws Exception*/ {
		return adminDAO.deleteUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getRoleOnAddUser(java.lang.String, java.lang.String)
	 */
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) throws Exception{
		return adminDAO.getRoleOnAddUser(orgLevel, customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#addNewUser(java.util.Map,java.util.Map)
	 */
	public UserTO  addNewUser(Map<String, Object> paramMap) throws Exception{
		return adminDAO.addNewUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#searchUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<UserTO> searchUser(String userName, String parentId, String adminYear, String isExactSearch, String orgMode) {
		return adminDAO.searchUser(userName, parentId, adminYear, isExactSearch, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#searchEduUser(java.util.Map)
	 */
	public List<EduCenterTO> searchEduUser(Map<String, Object> paramMap) {
		return adminDAO.searchEduUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#searchUserAutoComplete(java.util.Map)
	 */
	public String searchUserAutoComplete(Map<String, Object> paramMap) {
		return adminDAO.searchUserAutoComplete(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrganizationList(java.lang.String, java.lang.String, long)
	 */
	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId) {
		return adminDAO.getOrganizationList(tenantId, adminYear, customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrganizationChildren(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam, long customerId, String orgMode, String moreCount) {
		return adminDAO.getOrganizationChildren(parentTenantId, adminYear, searchParam, customerId, orgMode, moreCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#searchOrganization(Map<String, Object>)
	 */
	public List<OrgTO> searchOrganization(Map<String, Object> paramMap) throws Exception{
		return adminDAO.searchOrganization(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getTotalUserCount(java.lang.String, java.lang.String, long, String orgMode)
	 */
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId, String orgMode) {
		return adminDAO.getTotalUserCount(tenantId, adminYear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#searchOrgAutoComplete(Map<String, Object>)
	 */
	public String searchOrgAutoComplete(Map<String, Object> paramMap) throws Exception{
		return adminDAO.searchOrgAutoComplete(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getRoleDetails()
	 */
	public ArrayList<RoleTO> getRoleDetails() throws Exception {
		return adminDAO.getRoleDetails();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getUsersForSelectedRole(java.lang.String)
	 */
	public ArrayList<UserTO> getUsersForSelectedRole(String roleid, String currentOrg, String customer) throws Exception{
		return adminDAO.getUsersForSelectedRole(roleid, currentOrg,  customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#deleteRole(java.lang.String)
	 */
	public boolean deleteRole(String roleid) throws Exception {
		return adminDAO.deleteRole(roleid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getRoleDetailsById(java.lang.String)
	 */
	public RoleTO getRoleDetailsById(Map<String,Object> paramMap) throws Exception {
		return adminDAO.getRoleDetailsById(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#associateUserToRole(java.lang.String, java.lang.String)
	 */
	public boolean associateUserToRole(String roleId, String userName) throws Exception {
		return adminDAO.associateUserToRole(roleId, userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#deleteUserFromRole(java.lang.String, java.lang.String)
	 */
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception {
		return adminDAO.deleteUserFromRole(roleId, userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#saveRole(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception {
		return adminDAO.saveRole(roleId, roleName, roleDescription);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrganizationTree(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getOrganizationTree(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrgTree(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getOrgTree(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getOrgTree(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getOrganizationTreeOnRedirect(Map<String, Object>)
	 */
	public String getOrganizationTreeOnRedirect(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getOrganizationTreeOnRedirect(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#resetPassword(Map<String, Object>)
	 */
	public com.ctb.prism.login.transferobject.UserTO resetPassword(Map<String, Object> paramMap) throws Exception {
		return adminDAO.resetPassword(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getAllAdmin()
	 */
	public List<ObjectValueTO> getAllAdmin() throws Exception {
		return adminDAO.getAllAdmin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#addOrganization(com.ctb.prism.admin.transferobject.StgOrgTO)
	 */
	public String addOrganization(StgOrgTO stgOrgTO) {
		return adminDAO.addOrganization(stgOrgTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getAllStudents(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId) {
		return adminDAO.getAllStudents(adminYear, nodeId, customerId, gradeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getHierarchy(java.util.Map)
	 */
	public ArrayList<OrgTreeTO> getHierarchy(Map<String, Object> paramMap) throws Exception {
		return adminDAO.getHierarchy(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#downloadStudentFile(java.util.Map)
	 */
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException {
		return adminDAO.downloadStudentFile(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getEducationCenter(java.util.Map)
	 */
	public Map<String, Object> getEducationCenter(final Map<String, Object> paramMap) throws SystemException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<com.ctb.prism.core.transferobject.ObjectValueTO> eduCentreList = adminDAO.getEducationCenter(paramMap);
		if(eduCentreList.size() > 0) {
			returnMap.put("eduCentreList", eduCentreList);
			returnMap.put("state", eduCentreList.get(0).getOther());
		} else {
			logger.log(IAppLogger.INFO, "No Education center available for customer "+ paramMap.get("customerId"));
		}
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#loadEduCenterUsers(java.util.Map)
	 */
	public List<EduCenterTO> loadEduCenterUsers(final Map<String, Object> paramMap) throws SystemException {
		return adminDAO.loadEduCenterUsers(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getUserData(java.util.Map)
	 */
	public List<UserDataTO> getUserData(Map<String, String> paramMap) {
		return adminDAO.getUserData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getCustomerProduct(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String, Object> paramMap) throws BusinessException {
		return loginDAO.getCustomerProduct(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getUserForResetPassword(java.util.Map)
	 */
	public UserTO getUserForResetPassword(Map<String, String> paramMap) throws Exception{
		return adminDAO.getUserForResetPassword(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getEduUserRoleList(java.util.Map)
	 */
	public List<RoleTO> getEduUserRoleList(Map<String, String> paramMap) throws Exception{
		return adminDAO.getEduUserRoleList(paramMap);
	}
}
