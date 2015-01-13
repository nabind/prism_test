package com.ctb.prism.admin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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
import com.ctb.prism.admin.transferobject.EduCenterTreeTO;
import com.ctb.prism.admin.transferobject.ObjectValueTO;
import com.ctb.prism.admin.transferobject.OrgTO;
import com.ctb.prism.admin.transferobject.OrgTreeTO;
import com.ctb.prism.admin.transferobject.PwdHintTO;
import com.ctb.prism.admin.transferobject.RoleTO;
import com.ctb.prism.admin.transferobject.StgOrgTO;
import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentScoreDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentScoreTO;
import com.ctb.prism.admin.transferobject.studentdata.CustHierarchyDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.DemoTO;
import com.ctb.prism.admin.transferobject.studentdata.ItemResponseTO;
import com.ctb.prism.admin.transferobject.studentdata.ItemResponsesDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ObjectiveScoreDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ObjectiveScoreTO;
import com.ctb.prism.admin.transferobject.studentdata.OrgDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.RosterDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentBioTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentDemoTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentListTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentSurveyBioTO;
import com.ctb.prism.admin.transferobject.studentdata.SubtestAccommodationTO;
import com.ctb.prism.admin.transferobject.studentdata.SubtestAccommodationsTO;
import com.ctb.prism.admin.transferobject.studentdata.SurveyBioTO;
import com.ctb.prism.admin.util.StudentDataConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.test.AdminTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class AdminServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IAdminService adminService;

	TestParams testParams;

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
		ArrayList<OrgTO> orgList = adminService.getOrganizationDetailsOnFirstLoad(AdminTestHelper.helpGetOrganizationDetailsOnFirstLoad(testParams));
		assertNotNull(orgList);
	}

	@Test
	public void testGetOrganizationDetailsOnClick() throws Exception {
		ArrayList<OrgTO> orgList = adminService.getOrganizationDetailsOnClick(AdminTestHelper.helpGetOrganizationDetailsOnClick(testParams));
		assertNotNull(orgList);
	}

	@Test
	public void testGetUserDetailsOnClick() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetUserDetailsOnClick(testParams);
		ArrayList<UserTO> userList = adminService.getUserDetailsOnClick(paramMap);
		assertNotNull(userList);

		paramMap.put("NODEID", "0_0");
		userList = adminService.getUserDetailsOnClick(paramMap);
		assertNotNull(userList);

		paramMap.put("SEARCHPARAM", "abc");
		userList = adminService.getUserDetailsOnClick(paramMap);
		assertNotNull(userList);
	}

	@Test
	public void testGetEditUserData() throws Exception {
		UserTO user = adminService.getEditUserData(AdminTestHelper.helpEditUserData(testParams));
		assertNotNull(user);
	}

	@Test
	public void testUpdateUser() throws BusinessException, Exception {
		String Id = "";
		String userId = "";
		String userName = "";
		String emailId = "";
		String password = "";
		String userStatus = "";
		String[] userRoles = new String[] { "" };
		String salt = "";
		boolean status = adminService.updateUser(Id, userId, userName, emailId, password, userStatus, userRoles, salt);
		assertNotNull(status);
	}

	@Test
	public void testDeleteUser() {
		boolean status = adminService.deleteUser(AdminTestHelper.helpDeleteUser(testParams));
		assertNotNull(status);
	}

	@Test
	public void testGetRoleOnAddUser() throws Exception {
		String orgLevel = "0";
		String customerId = "0";
		List<RoleTO> role = adminService.getRoleOnAddUser(orgLevel, customerId);
		assertNotNull(role);
	}

	@Test
	public void testAddNewUser() throws Exception {
		UserTO user = adminService.addNewUser(AdminTestHelper.helpAddNewUser(testParams));
		assertNotNull(user);
	}

	@Test
	public void testSearchUser() {
		String userName = "";
		String parentId = "0";
		String adminYear = "0";
		String isExactSearch = "N";
		String orgMode = "";
		ArrayList<UserTO> userList = adminService.searchUser(userName, parentId, adminYear, isExactSearch, orgMode);
		assertNotNull(userList);
	}

	@Test
	public void testSearchEduUser() {
		Map<String, Object> paramMap = AdminTestHelper.helpSearchEduUser(testParams);
		List<EduCenterTO> userList = adminService.searchEduUser(paramMap);
		assertNotNull(userList);

		paramMap.put("isExactSearch", "Y");
		userList = adminService.searchEduUser(paramMap);
		assertNotNull(userList);
	}

	@Test
	public void testSearchUserAutoComplete() {
		String user = adminService.searchUserAutoComplete(AdminTestHelper.helpSearchUserAutoComplete(testParams));
		assertNotNull(user);
	}

	@Test
	public void testGetOrganizationList() {
		String tenantId = "0";
		String adminYear = "0";
		long customerId = 0L;
		List<OrgTO> orgList = adminService.getOrganizationList(tenantId, adminYear, customerId);
		assertNotNull(orgList);
	}

	@Test
	public void testGetOrganizationChildren() {
		String parentTenantId = "0";
		String adminYear = "0";
		String searchParam = "";
		long customerId = 0L;
		String orgMode = "";
		String moreCount = "15";
		List<OrgTO> orgList = adminService.getOrganizationChildren(parentTenantId, adminYear, searchParam, customerId, orgMode, moreCount);
		assertNotNull(orgList);
	}

	@Test
	public void testGetTotalUserCount() {
		String tenantId = "0";
		String adminYear = "0";
		long customerId = 0L;
		String orgMode = "";
		OrgTO to = adminService.getTotalUserCount(tenantId, adminYear, customerId, orgMode);
		assertNotNull(to);
	}

	@Test
	public void testSearchOrganization() throws Exception {
		List<OrgTO> orgList = adminService.searchOrganization(AdminTestHelper.helpSearchOrganization(testParams));
		assertNotNull(orgList);
	}

	@Test
	public void testSearchOrgAutoComplete() throws Exception {
		String org = adminService.searchOrgAutoComplete(AdminTestHelper.helpSearchOrgAutoComplete(testParams));
		assertNotNull(org);
	}

	@Test
	public void testGetRoleDetails() throws Exception {
		ArrayList<RoleTO> roleList = adminService.getRoleDetails();
		assertNotNull(roleList);
	}

	@Test
	public void testGetUsersForSelectedRole() throws Exception {
		String roleid = "0";
		String currentOrg = "0";
		String customer = "0";
		ArrayList<UserTO> userList = adminService.getUsersForSelectedRole(roleid, currentOrg, customer);
		assertNotNull(userList);
	}

	@Test
	public void testDeleteRole() throws Exception {
		String roleid = "0";
		boolean status = adminService.deleteRole(roleid);
		assertNotNull(status);
	}

	@Test
	public void testGetRoleDetailsById() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetRoleDetailsById(testParams);
		RoleTO role = adminService.getRoleDetailsById(paramMap);
		assertNotNull(role);
		
		paramMap.put("moreRole", "false");
		role = adminService.getRoleDetailsById(paramMap);
		assertNotNull(role);
	}

	@Test
	public void testAssociateUserToRole() throws Exception {
		String roleId = "0";
		String userName = "";
		boolean status = adminService.associateUserToRole(roleId, userName);
		assertNotNull(status);
	}

	@Test
	public void testDeleteUserFromRole() throws Exception {
		String roleId = "0";
		String userId = "0";
		boolean status = adminService.deleteUserFromRole(roleId, userId);
		assertNotNull(status);
	}

	@Test
	public void testSaveRole() throws Exception {
		String roleId = "0";
		String roleName = "TEST";
		String roleDescription = "Test";
		boolean status = adminService.saveRole(roleId, roleName, roleDescription);
		assertNotNull(status);
	}

	@Test
	public void testGetOrganizationTree() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetOrganizationTree(testParams);
		ArrayList<OrgTreeTO> orgTreeList = adminService.getOrganizationTree(paramMap);
		assertNotNull(orgTreeList);

		paramMap.put("nodeid", "0_0");
		paramMap.put("isFirstLoad", false);
		orgTreeList = adminService.getOrganizationTree(paramMap);
		assertNotNull(orgTreeList);
	}

	@Test
	public void testGetOrgTree() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetOrgTree(testParams);
		ArrayList<OrgTreeTO> orgTreeList = adminService.getOrgTree(paramMap);
		assertNotNull(orgTreeList);

		paramMap.put("isFirstLoad", false);
		orgTreeList = adminService.getOrgTree(paramMap);
		assertNotNull(orgTreeList);
	}

	@Test
	public void testGetOrganizationTreeOnRedirect() throws Exception {
		Map<String, Object> paramMap = AdminTestHelper.helpGetOrganizationTreeOnRedirect(testParams);
		String org = adminService.getOrganizationTreeOnRedirect(paramMap);
		assertNotNull(org);

		paramMap.put("isRedirected", false);
		org = adminService.getOrganizationTreeOnRedirect(paramMap);
		assertNotNull(org);
	}

	@Test
	public void testResetPassword() throws Exception {
		com.ctb.prism.login.transferobject.UserTO user = adminService.resetPassword(AdminTestHelper.helpResetPassword(testParams));
		assertNotNull(user);
	}

	@Test
	public void testGetAllAdmin() throws Exception {
		List<ObjectValueTO> list = adminService.getAllAdmin();
		assertNotNull(list);
	}

	@Test
	public void testAddOrganization() {
		StgOrgTO stgOrgTO = new StgOrgTO();
		String org = adminService.addOrganization(stgOrgTO);
		assertNotNull(org);
	}

	@Test
	public void testGetAllStudents() {
		String adminYear = "0";
		String nodeId = "0";
		String customerId = "0";
		String gradeId = "0";
		List<ObjectValueTO> studentList = adminService.getAllStudents(adminYear, nodeId, customerId, gradeId);
		assertNotNull(studentList);
	}

	@Test
	public void testGetHierarchy() throws Exception {
		ArrayList<OrgTreeTO> orgTreeList = adminService.getHierarchy(AdminTestHelper.helpGetHierarchy(testParams));
		assertNotNull(orgTreeList);
	}

	@Test
	public void testDownloadStudentFile() throws SystemException {
		List<StudentDataTO> studentList = adminService.downloadStudentFile(AdminTestHelper.helpDownloadStudentFile(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetEducationCenter() throws SystemException {
		Map<String, Object> paramMap = AdminTestHelper.helpGetEducationCenter(testParams);
		Map<String, Object> outputMap = adminService.getEducationCenter(paramMap);
		assertNotNull(outputMap);

		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = (com.ctb.prism.login.transferobject.UserTO) paramMap.get("loggedinUserTO");
		loggedinUserTO.setCustomerId("0");
		loggedinUserTO.setUserId("0");
		outputMap = adminService.getEducationCenter(paramMap);
		assertNotNull(outputMap);
	}

	@Test
	public void testLoadEduCenterUsers() throws SystemException {
		List<EduCenterTO> userList = adminService.loadEduCenterUsers(AdminTestHelper.helpLoadEduCenterUsers(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testGetUserData() {
		List<UserDataTO> userList = adminService.getUserData(AdminTestHelper.helpGetUserData(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testGetCustomerProduct() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> custProdList = adminService
				.getCustomerProduct(AdminTestHelper.helpGetCustomerProduct(testParams));
		assertNotNull(custProdList);
	}

	@Test
	public void testGetUserForResetPassword() throws Exception {
		UserTO user = adminService.getUserForResetPassword(AdminTestHelper.helpGetUserForResetPassword(testParams));
		assertNotNull(user);
	}

	@Test
	public void testGetEduUserRoleList() throws Exception {
		List<RoleTO> roleList = adminService.getEduUserRoleList(AdminTestHelper.helpGetEduUserRoleList(testParams));
		assertNotNull(roleList);
	}

	@Test
	public void testEduCenterTO() {
		EduCenterTO to = AdminTestHelper.getEduCenterTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getEduCenterId());
		assertNotNull(to.getEduCenterCode());
		assertNotNull(to.getEduCenterName());
		assertNotNull(to.getUserId());
		assertNotNull(to.getUserName());
		assertNotNull(to.getFullName());
		assertNotNull(to.getStatus());
		assertNotNull(to.getRoleList());
	}

	@Test
	public void testEduCenterTreeTO() {
		EduCenterTreeTO to = AdminTestHelper.getEduCenterTreeTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getState());
		assertNotNull(to.getData());
		assertNotNull(to.getEduTreeId());
		assertNotNull(to.getMetadata());
		assertNotNull(to.getAttr());
		assertNotNull(to.getEduCenterName());
		assertNotNull(to.getId());
	}

	@Test
	public void testObjectValueTO() {
		ObjectValueTO to = AdminTestHelper.getObjectValueTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getName());
		assertNotNull(to.getValue());
		assertNotNull(to.getOther());
	}

	@Test
	public void testOrgTO() {
		OrgTO to = AdminTestHelper.getOrgTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getTenantId());
		assertNotNull(to.getTenantName());
		assertNotNull(to.getParentNameAlias());
		assertNotNull(to.getSelectedOrgId());
		assertNotNull(to.getAdminName());
		assertNotNull(to.getParentTenantId());
		assertNotNull(to.getNoOfChildOrgs());
		assertNotNull(to.getNoOfUsers());
		assertNotNull(to.getOrgLevel());
		assertNotNull(to.getId());
		assertNotNull(to.getCustomerId());
		assertNotNull(to.getAdminId());
		assertNotNull(to.getClassName());
	}

	@Test
	public void testPwdHintTO() {
		PwdHintTO to = AdminTestHelper.getPwdHintTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getUserId());
		assertNotNull(to.getQuestionId());
		assertNotNull(to.getQuestionValue());
		assertNotNull(to.getQuestionSequence());
		assertNotNull(to.getQuestionActivationStatus());
		assertNotNull(to.getAnswerId());
		assertNotNull(to.getAnswerValue());
	}

	@Test
	public void testRoleTO() {
		RoleTO to = AdminTestHelper.getRoleTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getRoleId());
		assertNotNull(to.getRoleName());
		assertNotNull(to.getRoleDescription());
		assertNotNull(to.getLabel());
		assertNotNull(to.getDefaultSelection());
		assertNotNull(to.isEnabled());
		assertNotNull(to.getUserList());
	}

	@Test
	public void testOrgTreeTO() {
		OrgTreeTO to = AdminTestHelper.getOrgTreeTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getState());
		assertNotNull(to.getData());
		assertNotNull(to.getOrgTreeId());
		assertNotNull(to.getMetadata());
		assertNotNull(to.getAttr());
	}

	@Test
	public void testUserTO() {
		UserTO to = AdminTestHelper.getUserTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getUserId());
		assertNotNull(to.getUserName());
		assertNotNull(to.getUserDisplayName());
		assertNotNull(to.getTenantId());
		assertNotNull(to.getParentId());
		assertNotNull(to.getLoggedInOrgId());
		assertNotNull(to.getTenantName());
		assertNotNull(to.getFirstName());
		assertNotNull(to.getMiddleName());
		assertNotNull(to.getLastName());
		assertNotNull(to.getPassword());
		assertNotNull(to.getStatus());
		assertNotNull(to.getEmailId());
		assertNotNull(to.getUserType());
		assertNotNull(to.isFirstTimeUser());
		assertNotNull(to.getAvailableRoleList());
		assertNotNull(to.getMasterRoleList());

		assertNotNull(to.getId());
		assertNotNull(to.getEduCenterCode());
		assertNotNull(to.getEduCenterName());
		assertNotNull(to.getPhoneNumber());
		assertNotNull(to.getStreet());
		assertNotNull(to.getCity());
		assertNotNull(to.getState());
		assertNotNull(to.getZip());
		assertNotNull(to.getCountry());
		assertNotNull(to.getData());
		assertNotNull(to.getEduTreeId());
		assertNotNull(to.getMetadata());
		assertNotNull(to.getAttr());

		assertNotNull(to.getPwdHintList());
	}

	@Test
	public void testStudentDataTO() {
		StudentDataTO to = AdminTestHelper.getStudentDataTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getCustID());
		assertNotNull(to.getRosterID());
		assertNotNull(to.getHierarchyA_Name());
		assertNotNull(to.getHierarchyA_Code());
		assertNotNull(to.getHierarchyA_SpCodes());
		assertNotNull(to.getHierarchyB_Name());
		assertNotNull(to.getHierarchyB_Code());
		assertNotNull(to.getHierarchyB_SpCodes());
		assertNotNull(to.getHierarchyC_Name());
		assertNotNull(to.getHierarchyC_Code());
		assertNotNull(to.getHierarchyC_SpCodes());
		assertNotNull(to.getHierarchyD_Name());
		assertNotNull(to.getHierarchyD_Code());
		assertNotNull(to.getHierarchyD_SpCodes());
		assertNotNull(to.getHierarchyE_Name());
		assertNotNull(to.getHierarchyE_Code());
		assertNotNull(to.getHierarchyE_SpCodes());
		assertNotNull(to.getHierarchyF_Name());
		assertNotNull(to.getHierarchyF_Code());
		assertNotNull(to.getHierarchyF_SpCodes());
		assertNotNull(to.getHierarchyG_Name());
		assertNotNull(to.getHierarchyG_Code());
		assertNotNull(to.getHierarchyG_SpCodes());
		assertNotNull(to.getLastName());
		assertNotNull(to.getFirstName());
		assertNotNull(to.getMiddleInitial());
		assertNotNull(to.getDateOfBirth());
		assertNotNull(to.getForm());
		assertNotNull(to.getChangedFormFlag());
		assertNotNull(to.getTestLang());
		assertNotNull(to.getTestMode());
		assertNotNull(to.getGender());
		assertNotNull(to.getResolvedEthnicity());
		assertNotNull(to.getStudentID());
		assertNotNull(to.getPhoneNumber());
		assertNotNull(to.getLithocode());
		assertNotNull(to.getImagingID());
		assertNotNull(to.getStreetAddress());
		assertNotNull(to.getAptNo());
		assertNotNull(to.getCity());
		assertNotNull(to.getState());
		assertNotNull(to.getZipCode());
		assertNotNull(to.getCounty_Code());
		assertNotNull(to.getEduc_Center_Code());
		assertNotNull(to.getAcc_Audio_Rd());
		assertNotNull(to.getAcc_Audio_Wr());
		assertNotNull(to.getAcc_Audio_Math1());
		assertNotNull(to.getAcc_Audio_Math2());
		assertNotNull(to.getAcc_Audio_Sc());
		assertNotNull(to.getAcc_Audio_SS());
		assertNotNull(to.getAcc_Breaks_Rd());
		assertNotNull(to.getAcc_Breaks_Wr());
		assertNotNull(to.getAcc_Breaks_Math1());
		assertNotNull(to.getAcc_Breaks_Math2());
		assertNotNull(to.getAcc_Breaks_SC());
		assertNotNull(to.getAcc_Breaks_SS());
		assertNotNull(to.getAcc_Calculator_Rd());
		assertNotNull(to.getAcc_Calculator_Wr());
		assertNotNull(to.getAcc_Calculator_Math1());
		assertNotNull(to.getAcc_Calculator_SS());
		assertNotNull(to.getAcc_Duration1_25_Rd());
		assertNotNull(to.getAcc_Duration1_25_Wr());
		assertNotNull(to.getAcc_Duration1_25_Math1());
		assertNotNull(to.getAcc_Duration1_25_Math2());
		assertNotNull(to.getAcc_Duration1_25_Sc());
		assertNotNull(to.getAcc_Duration1_25_SS());
		assertNotNull(to.getAcc_Duration1_5_Rd());
		assertNotNull(to.getAcc_Duration1_5_Wr());
		assertNotNull(to.getAcc_Duration1_5_Math1());
		assertNotNull(to.getAcc_Duration1_5_Math2());
		assertNotNull(to.getAcc_Duration1_5_Sc());
		assertNotNull(to.getAcc_Duration1_5_SS());
		assertNotNull(to.getAcc_Duration2_0_Rd());
		assertNotNull(to.getAcc_Duration2_0_Wr());
		assertNotNull(to.getAcc_Duration2_0_Math1());
		assertNotNull(to.getAcc_Duration2_0_Math2());
		assertNotNull(to.getAcc_Duration2_0_Sc());
		assertNotNull(to.getAcc_Duration2_0_SS());
		assertNotNull(to.getAcc_PhysicalSupport_Rd());
		assertNotNull(to.getAcc_PhysicalSupport_Wr());
		assertNotNull(to.getAcc_PhysicalSupport_Math1());
		assertNotNull(to.getAcc_PhysicalSupport_Math2());
		assertNotNull(to.getAcc_PhysicalSupport_Sc());
		assertNotNull(to.getAcc_PhysicalSupport_SS());
		assertNotNull(to.getAcc_Scribe_Rd());
		assertNotNull(to.getAcc_Scribe_Wr());
		assertNotNull(to.getAcc_Scribe_Math1());
		assertNotNull(to.getAcc_Scribe_Math2());
		assertNotNull(to.getAcc_Scribe_Sc());
		assertNotNull(to.getAcc_Scribe_SS());
		assertNotNull(to.getAcc_TechDevice_Rd());
		assertNotNull(to.getAcc_TechDevice_Wr());
		assertNotNull(to.getAcc_TechDevice_Math1());
		assertNotNull(to.getAcc_TechDevice_Math2());
		assertNotNull(to.getAcc_TechDevice_Sc());
		assertNotNull(to.getAcc_TechDevice_SS());
		assertNotNull(to.getAcc_SepRoom_Rd());
		assertNotNull(to.getAcc_SepRoom_Wr());
		assertNotNull(to.getAcc_SepRoom_Math1());
		assertNotNull(to.getAcc_SepRoom_Math2());
		assertNotNull(to.getAcc_SepRoom_Sc());
		assertNotNull(to.getAcc_SepRoom_SS());
		assertNotNull(to.getAcc_SmGroup_Rd());
		assertNotNull(to.getAcc_SmGroup_Wr());
		assertNotNull(to.getAcc_SmGroup_Math1());
		assertNotNull(to.getAcc_SmGroup_Math2());
		assertNotNull(to.getAcc_SmGroup_Sc());
		assertNotNull(to.getAcc_SmGroup_SS());
		assertNotNull(to.getAcc_Other_Rd());
		assertNotNull(to.getAcc_Other_Wr());
		assertNotNull(to.getAcc_Other_Math1());
		assertNotNull(to.getAcc_Other_Math2());
		assertNotNull(to.getAcc_Other_Sc());
		assertNotNull(to.getAcc_Other_SS());
		assertNotNull(to.getExaminer_Ack_Accomm());
		assertNotNull(to.getHomeLang());
		assertNotNull(to.getK_12_Educ_Completed());
		assertNotNull(to.getSubj_Taken_Passed_Rd());
		assertNotNull(to.getSubj_Taken_Passed_Wr());
		assertNotNull(to.getSubj_Taken_Passed_Math());
		assertNotNull(to.getSubj_Taken_Passed_Sc());
		assertNotNull(to.getSubj_Taken_Passed_SS());
		assertNotNull(to.getSubj_Taken_Passed_None());
		assertNotNull(to.getNo_Times_Taken_Rd());
		assertNotNull(to.getNo_Times_Taken_Wr());
		assertNotNull(to.getNo_Times_Taken_Math());
		assertNotNull(to.getNo_Times_Taken_Sc());
		assertNotNull(to.getNo_Times_Taken_SS());
		assertNotNull(to.getRetake_Rd());
		assertNotNull(to.getRetake_Wr());
		assertNotNull(to.getRetake_Math());
		assertNotNull(to.getRetake_Sc());
		assertNotNull(to.getRetake_SS());
		assertNotNull(to.getRetake_None());
		assertNotNull(to.getTake_Readiness_Assessment());
		assertNotNull(to.getPrepare_County_Prog());
		assertNotNull(to.getPrepare_Sch_Dist_Prog());
		assertNotNull(to.getPrepare_Military_Prog());
		assertNotNull(to.getPrepare_Religious_Prog());
		assertNotNull(to.getPrepare_Purchased_My_Own_Study_Books());
		assertNotNull(to.getPrepare_Online_Study_Prog());
		assertNotNull(to.getPrepare_Homeschool());
		assertNotNull(to.getPrepare_Tutor());
		assertNotNull(to.getPrepare_Self_Taught());
		assertNotNull(to.getRecent_Class_Rd());
		assertNotNull(to.getRecent_Class_Wr());
		assertNotNull(to.getRecent_Class_Math());
		assertNotNull(to.getRecent_Class_Sc());
		assertNotNull(to.getRecent_Class_SS());
		assertNotNull(to.getMonths_Studied_Subj_Rd());
		assertNotNull(to.getMonths_Studied_Subj_Wr());
		assertNotNull(to.getMonths_Studied_Subj_Math());
		assertNotNull(to.getMonths_Studied_Subj_Sc());
		assertNotNull(to.getMonths_Studied_Subj_SS());
		assertNotNull(to.getGrade_in_Subj_Rd());
		assertNotNull(to.getGrade_in_Subj_Wr());
		assertNotNull(to.getGrade_in_Subj_Math());
		assertNotNull(to.getGrade_in_Subj_Sc());
		assertNotNull(to.getGrade_in_Subj_SS());
		assertNotNull(to.getTestFormat_Braille());
		assertNotNull(to.getTestFormat_LP());
		assertNotNull(to.getLocal_Field_1());
		assertNotNull(to.getLocal_Field_2());
		assertNotNull(to.getLocal_Field_3());
		assertNotNull(to.getLocal_Field_4());
		assertNotNull(to.getLocal_Field_5());
		assertNotNull(to.getLocal_Field_6());
		assertNotNull(to.getLocal_Field_7());
		assertNotNull(to.getLocal_Field_8());
		assertNotNull(to.getLocal_Field_9());
		assertNotNull(to.getLocal_Field_10());
		assertNotNull(to.getLocal_Field_11());
		assertNotNull(to.getLocal_Field_12());
		assertNotNull(to.getLocal_Field_13());
		assertNotNull(to.getLocal_Field_14());
		assertNotNull(to.getLocal_Field_15());
		assertNotNull(to.getLocal_Field_16());
		assertNotNull(to.getLocal_Field_17());
		assertNotNull(to.getLocal_Field_18());
		assertNotNull(to.getLocal_Field_19());
		assertNotNull(to.getLocal_Field_20());
		assertNotNull(to.getCandidate_Acknowledgement());
		// Reading , len
		assertNotNull(to.getReading_dateTestTaken());
		assertNotNull(to.getReading_numberCorrect());
		assertNotNull(to.getReading_scaleScore());
		assertNotNull(to.getReading_hSE_Score());
		assertNotNull(to.getReading_percentileRank());
		assertNotNull(to.getReading_nCE());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_1());
		assertNotNull(to.getReading_not_All_items_atmpt_1());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_2());
		assertNotNull(to.getReading_not_All_items_atmpt_2());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_3());
		assertNotNull(to.getReading_not_All_items_atmpt_3());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_4());
		assertNotNull(to.getReading_not_All_items_atmpt_4());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_5());
		assertNotNull(to.getReading_not_All_items_atmpt_5());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_6());
		assertNotNull(to.getReading_not_All_items_atmpt_6());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_7());
		assertNotNull(to.getReading_not_All_items_atmpt_7());
		assertNotNull(to.getReading_std_Obj_Mstry_Scr_8());
		assertNotNull(to.getReading_not_All_items_atmpt_8());
		// Writing , len
		assertNotNull(to.getWriting_dateTestTaken());
		assertNotNull(to.getWriting_numberCorrect());
		assertNotNull(to.getWriting_scaleScore());
		assertNotNull(to.getWriting_hSE_Score());
		assertNotNull(to.getWriting_percentileRank());
		assertNotNull(to.getWriting_nCE());
		assertNotNull(to.getWriting_std_Obj_Mstry_Scr_1());
		assertNotNull(to.getWriting_not_All_items_atmpt_1());
		assertNotNull(to.getWriting_std_Obj_Mstry_Scr_2());
		assertNotNull(to.getWriting_not_All_items_atmpt_2());
		assertNotNull(to.getWriting_std_Obj_Mstry_Scr_3());
		assertNotNull(to.getWriting_not_All_items_atmpt_3());
		assertNotNull(to.getWriting_std_Obj_Mstry_Scr_4());
		assertNotNull(to.getWriting_not_All_items_atmpt_4());
		// ELA Test , leh
		assertNotNull(to.getEla_scaleScore());
		assertNotNull(to.getEla_hSE_Score());
		assertNotNull(to.getEla_percentileRank());
		assertNotNull(to.getEla_nCE());
		// Mathemat , leh
		assertNotNull(to.getMath_dateTestTaken());
		assertNotNull(to.getMath_numberCorrect());
		assertNotNull(to.getMath_scaleScore());
		assertNotNull(to.getMath_hSE_Score());
		assertNotNull(to.getMath_percentileRank());
		assertNotNull(to.getMath_nCE());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_1());
		assertNotNull(to.getMath_not_All_items_atmpt_1());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_2());
		assertNotNull(to.getMath_not_All_items_atmpt_2());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_3());
		assertNotNull(to.getMath_not_All_items_atmpt_3());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_4());
		assertNotNull(to.getMath_not_All_items_atmpt_4());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_5());
		assertNotNull(to.getMath_not_All_items_atmpt_5());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_6());
		assertNotNull(to.getMath_not_All_items_atmpt_6());
		assertNotNull(to.getMath_std_Obj_Mstry_Scr_7());
		assertNotNull(to.getMath_not_All_items_atmpt_7());
		// Science , len
		assertNotNull(to.getScience_dateTestTaken());
		assertNotNull(to.getScience_numberCorrect());
		assertNotNull(to.getScience_scaleScore());
		assertNotNull(to.getScience_hSE_Score());
		assertNotNull(to.getScience_percentileRank());
		assertNotNull(to.getScience_nCE());
		assertNotNull(to.getScience_std_Obj_Mstry_Scr_1());
		assertNotNull(to.getScience_not_All_items_atmpt_1());
		assertNotNull(to.getScience_std_Obj_Mstry_Scr_2());
		assertNotNull(to.getScience_not_All_items_atmpt_2());
		assertNotNull(to.getScience_std_Obj_Mstry_Scr_3());
		assertNotNull(to.getScience_not_All_items_atmpt_3());
		assertNotNull(to.getScience_std_Obj_Mstry_Scr_4());
		assertNotNull(to.getScience_not_All_items_atmpt_4());
		assertNotNull(to.getScience_std_Obj_Mstry_Scr_5());
		assertNotNull(to.getScience_not_All_items_atmpt_5());
		// Social S , leh
		assertNotNull(to.getSocial_dateTestTaken());
		assertNotNull(to.getSocial_numberCorrect());
		assertNotNull(to.getSocial_scaleScore());
		assertNotNull(to.getSocial_hSE_Score());
		assertNotNull(to.getSocial_percentileRank());
		assertNotNull(to.getSocial_nCE());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_1());
		assertNotNull(to.getSocial_not_All_items_atmpt_1());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_2());
		assertNotNull(to.getSocial_not_All_items_atmpt_2());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_3());
		assertNotNull(to.getSocial_not_All_items_atmpt_3());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_4());
		assertNotNull(to.getSocial_not_All_items_atmpt_4());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_5());
		assertNotNull(to.getSocial_not_All_items_atmpt_5());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_6());
		assertNotNull(to.getSocial_not_All_items_atmpt_6());
		assertNotNull(to.getSocial_std_Obj_Mstry_Scr_7());
		assertNotNull(to.getSocial_not_All_items_atmpt_7());
		// Overall , len
		assertNotNull(to.getOverall_scaleScore());
		assertNotNull(to.getOverall_hSE_Score());
		assertNotNull(to.getOverall_percentileRank());
		assertNotNull(to.getOverall_nCE());
		// OAS Sett , leh
		assertNotNull(to.getOasReading_screenReader());
		assertNotNull(to.getOasReading_onlineCalc());
		assertNotNull(to.getOasReading_testPause());
		assertNotNull(to.getOasReading_highlighter());
		assertNotNull(to.getOasReading_blockingRuler());
		assertNotNull(to.getOasReading_magnifyingGlass());
		assertNotNull(to.getOasReading_fontAndBkgndClr());
		assertNotNull(to.getOasReading_largeFont());
		assertNotNull(to.getOasReading_musicPlayer());
		assertNotNull(to.getOasReading_extendedTime());
		assertNotNull(to.getOasReading_maskingTool());
		// OAS Sett , leh
		assertNotNull(to.getOasWriting_screenReader());
		assertNotNull(to.getOasWriting_onlineCalc());
		assertNotNull(to.getOasWriting_testPause());
		assertNotNull(to.getOasWriting_highlighter());
		assertNotNull(to.getOasWriting_blockingRuler());
		assertNotNull(to.getOasWriting_magnifyingGlass());
		assertNotNull(to.getOasWriting_fontAndBkgndClr());
		assertNotNull(to.getOasWriting_largeFont());
		assertNotNull(to.getOasWriting_musicPlayer());
		assertNotNull(to.getOasWriting_extendedTime());
		assertNotNull(to.getOasWriting_maskingTool());
		// OAS Sett , leh
		assertNotNull(to.getOasMath_screenReader());
		assertNotNull(to.getOasMath_onlineCalc());
		assertNotNull(to.getOasMath_testPause());
		assertNotNull(to.getOasMath_highlighter());
		assertNotNull(to.getOasMath_blockingRuler());
		assertNotNull(to.getOasMath_magnifyingGlass());
		assertNotNull(to.getOasMath_fontAndBkgndClr());
		assertNotNull(to.getOasMath_largeFont());
		assertNotNull(to.getOasMath_musicPlayer());
		assertNotNull(to.getOasMath_extendedTime());
		assertNotNull(to.getOasMath_maskingTool());
		// OAS Sett , leh
		assertNotNull(to.getOasScience_screenReader());
		assertNotNull(to.getOasScience_onlineCalc());
		assertNotNull(to.getOasScience_testPause());
		assertNotNull(to.getOasScience_highlighter());
		assertNotNull(to.getOasScience_blockingRuler());
		assertNotNull(to.getOasScience_magnifyingGlass());
		assertNotNull(to.getOasScience_fontAndBkgndClr());
		assertNotNull(to.getOasScience_largeFont());
		assertNotNull(to.getOasScience_musicPlayer());
		assertNotNull(to.getOasScience_extendedTime());
		assertNotNull(to.getOasScience_maskingTool());
		// OAS Sett , leh
		assertNotNull(to.getOasSocial_screenReader());
		assertNotNull(to.getOasSocial_onlineCalc());
		assertNotNull(to.getOasSocial_testPause());
		assertNotNull(to.getOasSocial_highlighter());
		assertNotNull(to.getOasSocial_blockingRuler());
		assertNotNull(to.getOasSocial_magnifyingGlass());
		assertNotNull(to.getOasSocial_fontAndBkgndClr());
		assertNotNull(to.getOasSocial_largeFont());
		assertNotNull(to.getOasSocial_musicPlayer());
		assertNotNull(to.getOasSocial_extendedTime());
		assertNotNull(to.getOasSocial_maskingTool());
		// Item Res , leh
		assertNotNull(to.getReadingItems_SR());
		assertNotNull(to.getReadingItems_FU());
		assertNotNull(to.getWritingItems_SR());
		assertNotNull(to.getWritingItems_CR());
		assertNotNull(to.getWritingItems_FU());
		assertNotNull(to.getMathItems_SR());
		assertNotNull(to.getMathItems_GR_Status());
		assertNotNull(to.getMathItems_GR_Edited());
		assertNotNull(to.getMathItems_FU());
		assertNotNull(to.getScienceItems_SR());
		assertNotNull(to.getScienceItems_FU());
		assertNotNull(to.getSocialStudies_SR());
		assertNotNull(to.getSocialStudies_FU());
		assertNotNull(to.getcTBUseField());
	}

	@Test
	public void testUserDataTO() {
		UserDataTO to = AdminTestHelper.getUserDataTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getUserId());
		assertNotNull(to.getFullName());
		assertNotNull(to.getStatus());
		assertNotNull(to.getOrgName());
		assertNotNull(to.getUserRoles());
	}

	@Test
	public void testStgOrgTO() {
		StgOrgTO to = AdminTestHelper.getStgOrgTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getOrgNodeId());
		assertNotNull(to.getOrgNodeName());
		assertNotNull(to.getOrgNodeCode());
		assertNotNull(to.getOrgNodeLevel());
		assertNotNull(to.getStrucElement());
		assertNotNull(to.getSpecialCodes());
		assertNotNull(to.getOrgMode());
		assertNotNull(to.getParentOrgNodeId());
		assertNotNull(to.getOrgNodeCodePath());
		assertNotNull(to.getEmail());
		assertNotNull(to.getAdminId());
		assertNotNull(to.getCustomerId());
	}

	@Test
	public void testContentDetailsTO() {
		ContentDetailsTO to = AdminTestHelper.getContentDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getContentCode());
		assertNotNull(to.getScoringMethod());
		assertNotNull(to.getStatusCode());
		assertNotNull(to.getDateTestTaken());
		assertNotNull(to.isDataChanged());
		assertNotNull(to.getSubtestAccommodationsTO());
		assertNotNull(to.getItemResponsesDetailsTO());
		assertNotNull(to.getContentScoreDetailsTO());
		assertNotNull(to.getCollObjectiveScoreDetailsTO());
	}

	@Test
	public void testContentScoreDetailsTO() {
		ContentScoreDetailsTO to = AdminTestHelper.getContentScoreDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getCollContentScoreTO());
	}

	@Test
	public void testContentScoreTO() {
		ContentScoreTO to = AdminTestHelper.getContentScoreTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getScoreType());
		assertNotNull(to.getScoreValue());
	}

	@Test
	public void testCustHierarchyDetailsTO() {
		CustHierarchyDetailsTO to = AdminTestHelper.getCustHierarchyDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getCustomerId());
		assertNotNull(to.getMaxHierarchy());
		assertNotNull(to.isDataChanged());
		assertNotNull(to.getCollOrgDetailsTO());
	}

	@Test
	public void testDemoTO() {
		DemoTO to = AdminTestHelper.getDemoTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getDemoName());
		assertNotNull(to.getDemovalue());
	}

	@Test
	public void testItemResponseTO() {
		ItemResponseTO to = AdminTestHelper.getItemResponseTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getItemSetType());
		assertNotNull(to.getScoreValue());
		assertNotNull(to.getItemCode());
	}

	@Test
	public void testItemResponsesDetailsTO() {
		ItemResponsesDetailsTO to = AdminTestHelper.getItemResponsesDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getItemResponseTO());
	}

	@Test
	public void testObjectiveScoreDetailsTO() {
		ObjectiveScoreDetailsTO to = AdminTestHelper.getObjectiveScoreDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getObjectiveName());
		assertNotNull(to.getObjectiveCode());
		assertNotNull(to.getCollObjectiveScoreTO());
	}

	@Test
	public void testObjectiveScoreTO() {
		ObjectiveScoreTO to = AdminTestHelper.getObjectiveScoreTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getScoreType());
		assertNotNull(to.getValue());
	}

	@Test
	public void testOrgDetailsTO() {
		OrgDetailsTO to = AdminTestHelper.getOrgDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getOrgName());
		assertNotNull(to.getOrgLabel());
		assertNotNull(to.getOrgLevel());
		assertNotNull(to.getOrgNodeId());
		assertNotNull(to.getOrgCode());
		assertNotNull(to.getParentOrgCode());
	}

	@Test
	public void testRosterDetailsTO() {
		RosterDetailsTO to = AdminTestHelper.getRosterDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getRosterId());
		assertNotNull(to.getCustHierarchyDetailsTO());
		assertNotNull(to.getStudentDetailsTO());
		assertNotNull(to.getCollContentDetailsTO());
	}

	@Test
	public void testStudentBioTO() {
		StudentBioTO to = AdminTestHelper.getStudentBioTO(testParams);
		assertNotNull(to);
		assertNotNull(to.isDataChanged());
		assertNotNull(to.getOasStudentId());
		assertNotNull(to.getLastName());
		assertNotNull(to.getFirstName());
		assertNotNull(to.getMiddleInit());
		assertNotNull(to.getGender());
		assertNotNull(to.getGrade());
		assertNotNull(to.getExamineeId());
		assertNotNull(to.getBirthDate());
		assertNotNull(to.getChrnlgclAge());
	}

	@Test
	public void testStudentDemoTO() {
		StudentDemoTO to = AdminTestHelper.getStudentDemoTO(testParams);
		assertNotNull(to);
		assertNotNull(to.isDataChanged());
		assertNotNull(to.getCollDemoTO());
	}

	@Test
	public void testStudentDetailsTO() {
		StudentDetailsTO to = AdminTestHelper.getStudentDetailsTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getStudentBioTO());
		assertNotNull(to.getStudentDemoTO());
		assertNotNull(to.getStudentSurveyBioTO());
	}

	@Test
	public void testStudentListTO() {
		StudentListTO to = AdminTestHelper.getStudentListTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getRosterDetailsTO());
	}

	@Test
	public void testStudentSurveyBioTO() {
		StudentSurveyBioTO to = AdminTestHelper.getStudentSurveyBioTO(testParams);
		assertNotNull(to);
		assertNotNull(to.isDataChanged());
		assertNotNull(to.getCollSurveyBioTO());
	}

	@Test
	public void testSubtestAccommodationTO() {
		SubtestAccommodationTO to = AdminTestHelper.getSubtestAccommodationTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getName());
		assertNotNull(to.getValue());
	}

	@Test
	public void testSubtestAccommodationsTO() {
		SubtestAccommodationsTO to = AdminTestHelper.getSubtestAccommodationsTO(testParams);
		assertNotNull(to.getCollSubtestAccommodationTO());
		assertNotNull(to);
	}

	@Test
	public void testSurveyBioTO() {
		SurveyBioTO to = AdminTestHelper.getSurveyBioTO(testParams);
		assertNotNull(to);
		assertNotNull(to.getSurveyName());
		assertNotNull(to.getSurveyValue());
	}

	@Test
	public void testStudentDataConstants() {
		StudentDataConstants studentDataConstants = new StudentDataConstants();
		assertEquals(studentDataConstants.ORG_NODE_LEVEL, new Long(1));
	}
}
