package com.ctb.prism.admin.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.admin.transferobject.EduCenterTO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.test.AdminTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class AdminDAOImplTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	IAdminDAO adminDAO;
	TestParams testParams;
	TestParams testParams2;
	
	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOrganizationDetailsOnFirstLoad() throws Exception {
		ArrayList<OrgTO> orgList = adminDAO.getOrganizationDetailsOnFirstLoad(AdminTestHelper.helpGetOrganizationDetailsOnFirstLoad(testParams));
		assertNotNull(orgList);
	}

	@Test
	public void testGetOrganizationDetailsOnClick() throws Exception {
		ArrayList<OrgTO> orgList = adminDAO.getOrganizationDetailsOnClick(AdminTestHelper.helpGetOrganizationDetailsOnClick(testParams));
		assertNotNull(orgList);
	}

	@Test
	public void testGetOrganizationTree()throws Exception  {
		Map<String, Object> paramMap = AdminTestHelper.helpGetOrganizationTree(testParams);
		ArrayList<OrgTreeTO> orgTreeList = adminDAO.getOrganizationTree(paramMap);
		assertNotNull(orgTreeList);

		paramMap.put("nodeid", "0_0");
		paramMap.put("isFirstLoad", false);
		orgTreeList = adminDAO.getOrganizationTree(paramMap);
		assertNotNull(orgTreeList);
	}

	@Test
	public void testGetOrganizationTreeOnRedirect()throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetOrganizationTreeOnRedirect(testParams);
		String org = adminDAO.getOrganizationTreeOnRedirect(paramMap);
		assertNotNull(org);

		paramMap.put("isRedirected", false);
		org = adminDAO.getOrganizationTreeOnRedirect(paramMap);
		assertNull(org);
	}

	@Test
	public void testGetOrgTree() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetOrgTree(testParams);
		ArrayList<OrgTreeTO> orgTreeList = adminDAO.getOrgTree(paramMap);
		assertNotNull(orgTreeList);

		paramMap.put("isFirstLoad", false);
		orgTreeList = adminDAO.getOrgTree(paramMap);
		assertNotNull(orgTreeList);
	}

	@Test
	public void testGetUserDetailsOnClick() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetUserDetailsOnClick(testParams);
		ArrayList<UserTO> userList = adminDAO.getUserDetailsOnClick(paramMap);
		assertNotNull(userList);

		paramMap.put("NODEID", "0_0");
		userList = adminDAO.getUserDetailsOnClick(paramMap);
		assertNotNull(userList);

		paramMap.put("SEARCHPARAM", "abc");
		userList = adminDAO.getUserDetailsOnClick(paramMap);
		assertNotNull(userList);
	}

	@Test
	public void testGetEditUserData() throws Exception{
		UserTO user = adminDAO.getEditUserData(AdminTestHelper.helpEditUserData(testParams));
		assertNotNull(user);
	}

	@Test
	public void testGetRoleOnAddUser() throws Exception{
		String orgLevel = "2";
		String customerId = "10195";
		List<RoleTO> role = adminDAO.getRoleOnAddUser(orgLevel, customerId);
		assertNotNull(role);
	}

	@Test
	public void testUpdateUser() throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		paramMap.put("Id", "");
		paramMap.put("userId", "1");
		paramMap.put("userName", "test");
		paramMap.put("emailId", "test@ctb.com");
		paramMap.put("password", "test");
		paramMap.put("userStatus", "AC");
		paramMap.put("userRoles", new String[] { "ROLE_USER" });
		paramMap.put("salt", "haI0iamff8ICeNNFWmV6");
		paramMap.put("contractName", "inors");
		boolean status = adminDAO.updateUser(paramMap);
		assertNotNull(status);
	}

	@Test
	public void testDeleteUser() {
		boolean status = adminDAO.deleteUser(AdminTestHelper.helpDeleteUser(testParams));
		assertNotNull(status);
	}

	@Test
	public void testAddNewUser()  throws Exception{
		UserTO user = adminDAO.addNewUser(AdminTestHelper.helpAddNewUser(testParams));
		assertNotNull(user);
	}

	@Test
	public void testSearchUser() {
		String userName = "test";
		String parentId = "0";
		String adminYear = "5050";
		String isExactSearch = "N";
		String orgMode = "PUBLIC";
		ArrayList<UserTO> userList = adminDAO.searchUser(userName, parentId, adminYear, isExactSearch, orgMode);
		assertNotNull(userList);
	}

	@Test
	public void testSearchEduUser() {
		Map<String, Object> paramMap = AdminTestHelper.helpSearchEduUser(testParams);
		List<EduCenterTO> userList = adminDAO.searchEduUser(paramMap);
		assertNotNull(userList);

		paramMap.put("isExactSearch", "Y");
		userList = adminDAO.searchEduUser(paramMap);
		assertNotNull(userList);
	}

	@Test
	public void testSearchUserAutoComplete() {
		String user = adminDAO.searchUserAutoComplete(AdminTestHelper.helpSearchUserAutoComplete(testParams));
		assertNull(user);
	}

	@Test
	public void testGetOrganizationList() {
		String tenantId = "10195";
		String adminYear = "5074";
		long customerId = 1057L;
		List<OrgTO> orgList = adminDAO.getOrganizationList(tenantId, adminYear, customerId);
		assertNotNull(orgList);
	}

	@Test
	public void testGetOrganizationChildren() {
		String parentTenantId = "9889";
		String adminYear = "5074";
		String searchParam = "";
		String customerId = "1057";
		String orgMode = "PUBLIC";
		String moreCount = "15";
		List<OrgTO> orgList = adminDAO.getOrganizationChildren(parentTenantId, adminYear, searchParam, customerId, orgMode, moreCount);
		assertNotNull(orgList);
	}

	@Test
	public void testGetTotalUserCount() {
		String tenantId = "10195";
		String adminYear = "5074";
		String customerId = "1057";
		String orgMode = "PUBLIC";
		OrgTO to = adminDAO.getTotalUserCount(tenantId, adminYear, customerId, orgMode);
		assertNotNull(to);
	}

	@Test
	public void testSearchOrganization() throws Exception{
		List<OrgTO> orgList = adminDAO.searchOrganization(AdminTestHelper.helpSearchOrganization(testParams));
		assertNotNull(orgList);
	}

	@Test
	public void testSearchOrgAutoComplete() throws Exception{
		String org = adminDAO.searchOrgAutoComplete(AdminTestHelper.helpSearchOrgAutoComplete(testParams));
		assertNotNull(org);
	}

	@Test
	public void testGetRoleDetails() throws Exception{
		ArrayList<RoleTO> roleList = adminDAO.getRoleDetails();
		assertNotNull(roleList);
	}

	@Test
	public void testGetRoleDetailsById() throws Exception{
		Map<String, Object> paramMap = AdminTestHelper.helpGetRoleDetailsById(testParams);
		RoleTO role = adminDAO.getRoleDetailsById(paramMap);
		assertNotNull(role);
		
		paramMap.put("moreRole", "false");
		role = adminDAO.getRoleDetailsById(paramMap);
		assertNotNull(role);
	}

	@Test
	public void testGetUsersForSelectedRole() throws Exception {
		String roleid = "1";
		String currentOrg = "10195";
		String customer = "1057L";
		ArrayList<UserTO> userList = adminDAO.getUsersForSelectedRole(roleid, currentOrg, customer);
		assertNotNull(userList);
	}

	/*@Test
	public void testUpdateRole() throws Exception{
		RoleTO roleTo = new RoleTO();
		ArrayList<UserTO> userTOs = adminDAO.updateRole(roleTo);
	}*/

	@Test
	public void testDeleteRole()throws Exception {
		String roleid = "1";
		boolean status = adminDAO.deleteRole(roleid);
		assertNotNull(status);
	}

	@Test
	public void testAssociateUserToRole() throws Exception{
		String roleId = "3";
		String userName = "testuser";
		boolean status = adminDAO.associateUserToRole(roleId, userName);
		assertNotNull(status);
	}

	@Test
	public void testDeleteUserFromRole() throws Exception{
		String roleId = "1";
		String userId = "testuser";
		boolean status = adminDAO.deleteUserFromRole(roleId, userId);
		assertNotNull(status);
	}

	@Test
	public void testSaveRole() throws Exception {
		String roleId = "1";
		String roleName = "ROLE_USER";
		String roleDescription = "USER";
		boolean status = adminDAO.saveRole(roleId, roleName, roleDescription);
		assertNotNull(status);
	}

	@Test
	public void testResetPassword()  throws Exception{
		com.ctb.prism.login.transferobject.UserTO user = adminDAO.resetPassword(AdminTestHelper.helpResetPassword(testParams));
		assertNotNull(user);
	}

	@Test
	public void testGetAllAdmin() throws Exception{
		List<ObjectValueTO> list = adminDAO.getAllAdmin();
		assertNotNull(list);
	}

	@Test
	public void testAddOrganization() {
		StgOrgTO stgOrgTO = new StgOrgTO();
		String org = adminDAO.addOrganization(stgOrgTO);
		assertNotNull(org);
	}

	@Test
	public void testGetAllStudents() {
		String adminYear = "5074";
		String nodeId = "10195";
		String customerId = "1057L";
		String gradeId = "10007";
		List<ObjectValueTO> studentList = adminDAO.getAllStudents(adminYear, nodeId, customerId, gradeId);
		assertNotNull(studentList);
	}

	@Test
	public void testGetHierarchy() throws Exception {
		ArrayList<OrgTreeTO> orgTreeList = adminDAO.getHierarchy(AdminTestHelper.helpGetHierarchy(testParams));
		assertNotNull(orgTreeList);
	}

	@Test
	public void testDownloadStudentFile() throws Exception {
		List<StudentDataTO> studentList = adminDAO.downloadStudentFile(AdminTestHelper.helpDownloadStudentFile(testParams));
		assertNull(studentList);
	}

	@Test
	public void testGetEducationCenter() throws Exception{
		Map<String, Object> paramMap = AdminTestHelper.helpGetEducationCenter(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> outputMap = adminDAO.getEducationCenter(paramMap);
		assertNotNull(outputMap);

		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) paramMap.get("loggedinUserTO");
		loggedinUserTO.setCustomerId("0");
		loggedinUserTO.setUserId("0");
		outputMap = adminDAO.getEducationCenter(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put("loggedinUserTO", null);
		outputMap = adminDAO.getEducationCenter(paramMap);
		assertNotNull(outputMap);
	}

	@Test
	public void testLoadEduCenterUsers()throws Exception {
		List<EduCenterTO> userList = adminDAO.loadEduCenterUsers(AdminTestHelper.helpLoadEduCenterUsers(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testGetUserData() throws Exception {
		List<UserDataTO> userList = adminDAO.getUserData(AdminTestHelper.helpGetUserData(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testGetUserForResetPassword() throws Exception {
		UserTO user = adminDAO.getUserForResetPassword(AdminTestHelper.helpGetUserForResetPassword(testParams));
		assertNotNull(user);
	}


	@Test
	public void testGetEduUserRoleList() throws Exception {
		List<RoleTO> roleList = adminDAO.getEduUserRoleList(AdminTestHelper.helpGetEduUserRoleList(testParams));
		assertNotNull(roleList);
	}

}
