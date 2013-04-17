package com.ctb.prism.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.prism.admin.business.IAdminBusiness;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.exception.BusinessException;

@Service("adminService")
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private IAdminBusiness adminBusiness;

	public ArrayList<OrgTO> getOrganizationDetailsOnFirstLoad(String nodeid)
			throws Exception {

		return adminBusiness.getOrganizationDetailsOnFirstLoad(nodeid);

	}
	
	public ArrayList<OrgTO> getOrganizationDetailsOnClick(String nodeid)
			throws Exception {

		return adminBusiness.getOrganizationDetailsOnClick(nodeid);

	}

	public ArrayList<UserTO> getUserDetailsOnClick(String nodeid,String currorg, String adminYear, String searchParam)
			throws Exception {

		return adminBusiness.getUserDetailsOnClick(nodeid,currorg,adminYear, searchParam);

	}

	public UserTO getEditUserData(String nodeid) throws Exception {

		return adminBusiness.getEditUserData(nodeid);

	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean updateUser(String Id, String userId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles) throws BusinessException, Exception {

		return adminBusiness.updateUser(Id, userId, userName, emailId,
				password, userStatus, userRoles);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean deleteUser(String Id, String userName, String password)
			throws Exception {

		return adminBusiness.deleteUser(Id, userName, password);
	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public List<RoleTO> getRoleOnAddUser(String orgLevel) {

		return adminBusiness.getRoleOnAddUser(orgLevel);

	}

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public UserTO addNewUser(String userId, String tenantId, String userName,
			String emailId, String password, String userStatus,
			String[] userRoles,String orgLevel, String adminYear) throws BusinessException, Exception {

		return adminBusiness.addNewUser(userId, tenantId, userName, emailId,
				password, userStatus, userRoles, orgLevel, adminYear);
	}

	public ArrayList <UserTO> searchUser(String userName, String parentId, String adminYear,String isExactSearch){
		return adminBusiness.searchUser( userName,  parentId, adminYear,isExactSearch);
	}
	
	public String searchUserAutoComplete( String userName, String parentId, String adminYear ) {
		return adminBusiness.searchUserAutoComplete( userName, parentId, adminYear );
	}

	public List<OrgTO> getOrganizationList(String tenantId, String adminYear) {
		return adminBusiness.getOrganizationList(tenantId, adminYear);
	}

	public List<OrgTO> getOrganizationChildren(String parentTenantId, String adminYear, String searchParam) {
		return adminBusiness.getOrganizationChildren(parentTenantId, adminYear, searchParam);
	}
	
	public OrgTO getTotalUserCount(String tenantId, String adminYear) {
		return adminBusiness.getTotalUserCount(tenantId, adminYear);
	}


	public List<OrgTO> searchOrganization(String orgName, String tenantId, String adminYear) {
		return adminBusiness.searchOrganization(orgName, tenantId, adminYear);
	}

	public String searchOrgAutoComplete(String orgName, String tenantId, String adminYear) {
		return adminBusiness.searchOrgAutoComplete(orgName, tenantId, adminYear);
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
	public ArrayList<OrgTreeTO> getOrganizationTree(String nodeid,String currOrg,boolean isFirstLoad, String adminYear)throws Exception {
		return adminBusiness.getOrganizationTree(nodeid,currOrg,isFirstLoad, adminYear);
	}
		
	public ArrayList<OrgTreeTO> getOrgTree(String nodeid,boolean isFirstLoad, String adminYear)throws Exception {
		return adminBusiness.getOrgTree(nodeid,isFirstLoad, adminYear);
	}
	public String getOrganizationTreeOnRedirect(String selectedOrgId,String parentOrgId,String userId,String userName, boolean isRedirected) throws Exception{
		return adminBusiness.getOrganizationTreeOnRedirect(selectedOrgId,parentOrgId,userId,userName, isRedirected);
	}
	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public String resetPassword(String userName) throws Exception {
		return adminBusiness.resetPassword(userName);
	}
	
	public List<ObjectValueTO> getAllAdmin() throws Exception {
		return adminBusiness.getAllAdmin();
	}
}
