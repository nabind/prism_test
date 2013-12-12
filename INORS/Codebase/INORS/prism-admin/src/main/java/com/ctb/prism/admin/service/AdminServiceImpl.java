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

@Service("adminService")
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private IAdminBusiness adminBusiness;

	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid)
			throws Exception {

		return adminBusiness.getOrganizationDetailsOnFirstLoad(nodeid);

	}
	
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(String nodeid,String orgMode)
			throws Exception {

		return adminBusiness.getOrganizationDetailsOnClick(nodeid,orgMode);

	}

	public ArrayList<UserTO> getUserDetailsOnClick(String nodeid,String currorg, String adminYear, String searchParam,String customerid)
			throws Exception {

		return adminBusiness.getUserDetailsOnClick(nodeid,currorg,adminYear, searchParam,customerid);

	}

	public UserTO getEditUserData(Map<String,Object> paramMap) throws Exception {

		return adminBusiness.getEditUserData(paramMap);

	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean updateUser(String Id, String userId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles) throws BusinessException, Exception {

		return adminBusiness.updateUser(Id, userId, userName, emailId,
				password, userStatus, userRoles);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean deleteUser(Map<String,Object> paramMap)
			throws Exception {

		return adminBusiness.deleteUser(paramMap);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) {

		return adminBusiness.getRoleOnAddUser(orgLevel, customerId);

	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public UserTO addNewUser(Map<String,Object> paramMap) throws BusinessException, Exception {

		return adminBusiness.addNewUser(paramMap);
	}

	public ArrayList <UserTO> searchUser(String userName, String parentId, String adminYear,String isExactSearch){
		return adminBusiness.searchUser( userName,  parentId, adminYear,isExactSearch);
	}
	
	public List<EduCenterTO> searchEduUser(Map<String,Object> paramMap){
		return adminBusiness.searchEduUser( paramMap);
	}
	
	
	public String searchUserAutoComplete(Map<String,Object> paramMap) {
		return adminBusiness.searchUserAutoComplete(paramMap);
	}

	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId) {
		return adminBusiness.getOrganizationList(tenantId, adminYear, customerId);
	}

	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam, long customerId) {
		return adminBusiness.getOrganizationChildren(parentTenantId, adminYear, searchParam, customerId);
	}
	
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId) {
		return adminBusiness.getTotalUserCount(tenantId, adminYear, customerId);
	}


	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId) {
		return adminBusiness.searchOrganization(orgName, tenantId, adminYear, customerId);
	}

	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId) {
		return adminBusiness.searchOrgAutoComplete(orgName, tenantId, adminYear ,customerId);
	}

	public ArrayList<RoleTO> getRoleDetails() throws Exception {
		return adminBusiness.getRoleDetails();
	}

	public ArrayList<UserTO> getUsersForSelectedRole(String roleid)
			throws Exception {
		return adminBusiness.getUsersForSelectedRole(roleid);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean deleteRole(String roleid) throws Exception {
		return adminBusiness.deleteRole(roleid);
	}

	public RoleTO getRoleDetailsById(String roleid) throws Exception {
		return adminBusiness.getRoleDetailsById(roleid);
	}

	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean associateUserToRole(String roleId, String userName) throws Exception{
		return  adminBusiness.associateUserToRole(roleId, userName);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean deleteUserFromRole(String roleId, String userId) throws Exception{
		return  adminBusiness.deleteUserFromRole(roleId, userId);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception{
		return  adminBusiness.saveRole(roleId, roleName, roleDescription);
	}
	public ArrayList<OrgTreeTO> getOrganizationTree(String nodeid,String currOrg,boolean isFirstLoad, String adminYear, long customerId, String orgMode)throws Exception {
		return adminBusiness.getOrganizationTree(nodeid,currOrg,isFirstLoad, adminYear, customerId,orgMode);
	}
	
	public ArrayList<OrgTreeTO> getOrgTree(String nodeid,boolean isFirstLoad, String adminYear, long customerId)throws Exception {
		return adminBusiness.getOrgTree(nodeid,isFirstLoad, adminYear, customerId);
	}
	public String getOrganizationTreeOnRedirect(String selectedOrgId,String parentOrgId,String userId,long customerId, boolean isRedirected) throws Exception{
		return adminBusiness.getOrganizationTreeOnRedirect(selectedOrgId,parentOrgId,userId,customerId, isRedirected);
	}
	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public String resetPassword(String userName) throws Exception {
		return adminBusiness.resetPassword(userName);
	}
	
	public List<ObjectValueTO> getAllAdmin() throws Exception {
		return adminBusiness.getAllAdmin();
	}
	
	public String addOrganization(StgOrgTO stgOrgTO) {
		return adminBusiness.addOrganization(stgOrgTO);
	}
	
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId) {
		return adminBusiness.getAllStudents(adminYear, nodeId, customerId, gradeId);
	}
	
	public ArrayList<OrgTreeTO> getHierarchy(String nodeid, String adminYear, long customerId, String school)throws Exception {
		return adminBusiness.getHierarchy(nodeid, adminYear, customerId, school);
	}
	
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException {
		return adminBusiness.downloadStudentFile(paramMap);
	}
	
	public Map<String,Object> getEducationCenter(final Map<String,Object> paramMap)throws SystemException {
		return adminBusiness.getEducationCenter(paramMap);
	}
	
	public List<EduCenterTO> loadEduCenterUsers(final Map<String,Object> paramMap) throws SystemException {
		return adminBusiness.loadEduCenterUsers(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.service.IAdminService#getUserData(java.util.Map)
	 */
	public List<UserDataTO> getUserData(Map<String, String> paramMap){
		return adminBusiness.getUserData(paramMap);
	}
}
