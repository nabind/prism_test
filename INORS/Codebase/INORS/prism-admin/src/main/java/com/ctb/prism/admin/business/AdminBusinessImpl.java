/**
 * 
 */
package com.ctb.prism.admin.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.admin.dao.IAdminDAO;
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

/**
 * @author TCS
 * 
 */

@Component("adminBusiness")
public class AdminBusinessImpl implements IAdminBusiness {

	@Autowired
	private IAdminDAO adminDAO;

	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid)
		throws Exception {

		return adminDAO.getOrganizationDetailsOnFirstLoad(nodeid);

	}
	
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(Map<String,Object> paramMap)
			throws Exception {

		return adminDAO.getOrganizationDetailsOnClick(paramMap);

	}

	public ArrayList<UserTO> getUserDetailsOnClick(String nodeid,String currorg, String adminYear, String searchParam,String customerid)
			throws Exception {

		return adminDAO.getUserDetailsOnClick(nodeid,currorg,adminYear, searchParam,customerid);

	}

	public UserTO getEditUserData(Map<String,Object> paramMap) throws Exception {

		return adminDAO.getEditUserData(paramMap);

	}

	public boolean updateUser(String Id, String userId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles) throws BusinessException, Exception {

		return adminDAO.updateUser(Id, userId, userName, emailId, password,
				userStatus, userRoles);
	}

	public boolean deleteUser(Map<String,Object> paramMap)
			throws Exception {

		return adminDAO.deleteUser(paramMap);
	}

	public List<RoleTO> getRoleOnAddUser(String orgLevel, String customerId) {

		return adminDAO.getRoleOnAddUser(orgLevel, customerId);

	}

	public UserTO addNewUser(Map<String,Object> paramMap) throws BusinessException, Exception {

		return adminDAO.addNewUser(paramMap);
	}

	
	public ArrayList <UserTO> searchUser(String userName, String parentId, String adminYear,String isExactSearch){
		return adminDAO.searchUser( userName,  parentId, adminYear,isExactSearch);
	}
	
	public List<EduCenterTO> searchEduUser(Map<String,Object> paramMap) {
		return adminDAO.searchEduUser( paramMap );
	}
	
	public String searchUserAutoComplete(Map<String,Object> paramMap) {
		return adminDAO.searchUserAutoComplete(paramMap);
	}
	
	public List<OrgTO> getOrganizationList(String tenantId, String adminYear, long customerId) {
		return adminDAO.getOrganizationList(tenantId, adminYear, customerId);
	}

	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam, long customerId) {
		return adminDAO.getOrganizationChildren(parentTenantId, adminYear, searchParam, customerId);
	}

	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear, long customerId) {
		return adminDAO.searchOrganization(orgName, tenantId, adminYear, customerId);
	}
	
	public OrgTO getTotalUserCount(String tenantId, String adminYear, long customerId) {
		return adminDAO.getTotalUserCount(tenantId, adminYear, customerId);
	}


	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear, long customerId) {
		return adminDAO.searchOrgAutoComplete(orgName, tenantId, adminYear ,customerId);
	}

	public ArrayList<RoleTO> getRoleDetails() throws Exception {
		return adminDAO.getRoleDetails();
	}

	public ArrayList<UserTO> getUsersForSelectedRole(String roleid)
			throws Exception {
		return adminDAO.getUsersForSelectedRole(roleid);
	}

	public boolean deleteRole(String roleid) throws Exception {
		return adminDAO.deleteRole(roleid);
	}

	public RoleTO getRoleDetailsById(String roleid) throws Exception {
		return adminDAO.getRoleDetailsById(roleid);
	}

	
	public boolean associateUserToRole(String roleId, String userName) throws Exception{
		return  adminDAO.associateUserToRole(roleId, userName);
	}

	public boolean deleteUserFromRole(String roleId, String userId) throws Exception{
		return  adminDAO.deleteUserFromRole(roleId, userId);
	}

	public boolean saveRole(String roleId, String roleName, String roleDescription) throws Exception{
		return  adminDAO.saveRole(roleId, roleName, roleDescription);
	}
	public ArrayList<OrgTreeTO> getOrganizationTree(Map<String,Object> paramMap)throws Exception 
	{
		return adminDAO.getOrganizationTree(paramMap);
	}
	public ArrayList<OrgTreeTO> getOrgTree(Map<String,Object> paramMap)throws Exception {
		return adminDAO.getOrgTree(paramMap);
	}
	public String getOrganizationTreeOnRedirect(String selectedOrgId,String parentOrgId,String userId,long customerId, boolean isRedirected) throws Exception{
		return adminDAO.getOrganizationTreeOnRedirect(selectedOrgId,parentOrgId,userId,customerId,isRedirected);
	}
	public String resetPassword(String userName) throws Exception {
		return adminDAO.resetPassword(userName);
	}
	
	public List<ObjectValueTO> getAllAdmin() throws Exception {
		return adminDAO.getAllAdmin();
	}
	
	public String addOrganization(StgOrgTO stgOrgTO) {
		return adminDAO.addOrganization(stgOrgTO);
	}
	
	public List<ObjectValueTO> getAllStudents(String adminYear, String nodeId, String customerId, String gradeId) {
		return adminDAO.getAllStudents(adminYear, nodeId, customerId, gradeId);
	}
	
	public ArrayList<OrgTreeTO> getHierarchy(Map<String,Object> paramMap)throws Exception {
		return adminDAO.getHierarchy(paramMap);
	}
	
	public List<StudentDataTO> downloadStudentFile(final Map<String, Object> paramMap) throws SystemException {
		return adminDAO.downloadStudentFile(paramMap);
	}
	
	public Map<String,Object> getEducationCenter(final Map<String,Object> paramMap)throws SystemException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<com.ctb.prism.core.transferobject.ObjectValueTO> eduCentreList = adminDAO.getEducationCenter(paramMap);
		returnMap.put("eduCentreList", eduCentreList);
		returnMap.put("state", eduCentreList.get(0).getOther());
		return returnMap;
	}
	
	public List<EduCenterTO> loadEduCenterUsers(final Map<String,Object> paramMap) throws SystemException{
		return adminDAO.loadEduCenterUsers(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.admin.business.IAdminBusiness#getUserData(java.util.Map)
	 */
	public List<UserDataTO> getUserData(Map<String, String> paramMap) {
		return adminDAO.getUserData(paramMap);
	}
	
	public List<com.ctb.prism.admin.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap) throws BusinessException
	{
		return adminDAO.getCustomerProduct(paramMap);
	}
}
