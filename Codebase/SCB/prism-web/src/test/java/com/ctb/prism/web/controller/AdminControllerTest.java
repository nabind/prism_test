package com.ctb.prism.web.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.test.WebTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class AdminControllerTest extends AbstractJUnit4SpringContextTests {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockHttpSession session;

	@Autowired
	private AdminController controller;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		// Populate test params from properties file
		testParams = TestUtil.getTestParams();

		// Create request and response for Controller methods
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session = new MockHttpSession();

		// Set all Session Attributes
		TestUtil.setSessionAttributes(request, testParams);

		// Bypass login
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOrganizations() {
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.setParameter("AdminYear", "0");
		
		request.getSession().setAttribute(IApplicationConstants.ORG_MODE, "0");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		
		ModelAndView mv = controller.getOrganizations(request, response);
		assertNotNull(mv);
		
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "1000");
		mv = controller.getOrganizations(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetOrgChildren() {
		String str = controller.getOrgChildren(request, response);
		assertNull(str);
	}

	@Test
	public void testGetUserCount() {
		String str = controller.getUserCount(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchOrganization() {
		String str = controller.searchOrganization(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchOrgAutoComplete() {
		String str = controller.searchOrgAutoComplete(request, response);
		assertNull(str);
	}

	@Test
	public void testManageUser() throws Exception {
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.ORG_MODE, "0");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		request.setParameter("AdminYear", "0");
		ModelAndView mv = controller.manageUser(request, response, session);
		assertNotNull(mv);
	}

	@Test
	public void testGetTenantHierarchy() throws Exception {
		String str = controller.getTenantHierarchy(request, response);
		assertNull(str);
	}

	@Test
	public void testUserDetails() throws Exception {
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		request.getSession().setAttribute(IApplicationConstants.ORG_MODE, "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.setParameter("tenantId", "0");
		request.setParameter("AdminYear", "0");
		String str = controller.userDetails(request, response);
		assertNull(str);
	}

	@Test
	public void testEditUserData() throws Exception {
		String str = controller.editUserData(request, response);
		assertNull(str);
	}

	@Test
	public void testUpdateUser() throws IOException {
		ModelAndView mv = controller.updateUser(request, response);
		assertNull(mv);
	}

	@Test
	public void testGetRoleOnAddUser() throws Exception {
		String str = controller.getRoleOnAddUser(request, response);
		assertNull(str);
	}

	@Test
	public void testAddNewUser() throws IOException {
		ModelAndView mv = controller.addNewUser(request, response);
		assertNull(mv);
	}

	@Test
	public void testDeleteUser() throws IOException {
		ModelAndView mv = controller.deleteUser(request, response);
		assertNull(mv);
	}

	@Test
	public void testSearchUser() {
		String str = controller.searchUser(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchUserAutoComplete() {
		String str = controller.searchUserAutoComplete(request, response);
		assertNull(str);
	}

	@Test
	public void testManageRole() throws Exception {
		ModelAndView mv = controller.manageRole(request, response, session);
		assertNotNull(mv);
	}

	@Test
	public void testEditRole() throws Exception {
		String str = controller.editRole(request, response, session);
		assertNull(str);
	}

	@Test
	public void testUpdateRole() {
		ModelAndView mv = controller.updateRole(request, response);
		assertNull(mv);
	}

	@Test
	public void testDeleteRole() throws Exception {
		ModelAndView mv = controller.deleteRole(request, response, session);
		assertNull(mv);
	}

	@Test
	public void testAssociateUser() throws Exception {
		String str = controller.associateUser(request, response, session);
		assertNull(str);
	}

	@Test
	public void testDissociateUser() throws Exception {
		String str = controller.dissociateUser(request, response, session);
		assertNull(str);
	}

	@Test
	public void testRedirectToUser() throws Exception {
		request.setParameter("AdminYear", "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.ORG_MODE, "0");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		ModelAndView mv = controller.redirectToUser(request, response, session);
		assertNull(mv);
	}

	@Test
	public void testManageParent() throws Exception {
		request.setParameter("AdminYear", "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.ORG_MODE, "0");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		ModelAndView mv = controller.manageParent(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testManageStudent() throws Exception {
		request.setParameter("AdminYear", "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.PRODUCT_NAME, "");
		request.getSession().setAttribute(IApplicationConstants.ORG_MODE, "0");
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		ModelAndView mv = controller.manageStudent(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetParentDetailsOnScroll() throws Exception {
		String str = controller.getParentDetailsOnScroll(request, response);
		assertNull(str);
	}

	@Test
	public void testGetStudentDetailsOnScroll() throws Exception {
		String str = controller.getStudentDetailsOnScroll(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchParentAutoComplete() {
		String str = controller.searchParentAutoComplete(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchParent() {
		String str = controller.searchParent(request, response);
		assertNull(str);
	}

	@Test
	public void testGetStudentAssessmentList() {
		String str = controller.getStudentAssessmentList(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchStudentAutoComplete() {
		String str = controller.searchStudentAutoComplete(request, response);
		assertNull(str);
	}

	@Test
	public void testSearchStudent() {
		String str = controller.searchStudent(request, response);
		assertNull(str);
	}

	@Test
	public void testRedirectToStudent() throws Exception {
		ModelAndView mv = controller.redirectToStudent(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testUpdateAssessmentDetails() {
		ModelAndView mv = controller.updateAssessmentDetails(request, response);
		assertNull(mv);
	}

	@Test
	public void testResetPassword() {
		MockHttpServletRequest newRequest = new MockHttpServletRequest();
		newRequest.getSession().setAttribute(IApplicationConstants.CURRUSER, "abcd");
		String str = controller.resetPassword(newRequest, response);
		assertNull(str);
	}

	@Test
	public void testUpdateAdminYear() {
		String str = controller.updateAdminYear(request, response);
		assertNull(str);
	}

	@Test
	public void testResetPrismActions() {
		String str = controller.resetPrismActions(request, response);
		assertNotNull(str);
	}

	@Test
	public void testRegenerateActivationCode() throws IOException {
		String str = controller.regenerateActivationCode(request, response);
		assertNull(str);
	}

	@Test
	public void testStudentFileDownload() {
		ModelAndView mv = controller.studentFileDownload(request);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadStudentFile() {
		String str = controller.downloadStudentFile(request, response);
		assertNull(str);
	}

	@Test
	public void testGetFileSize() {
		controller.getFileSize(request, response);
		assertNotNull("Method return is void");
	}

	@Test
	public void testManageEducationCenter() throws Exception {
		ModelAndView mv = controller.manageEducationCenter(request, response, session);
		assertNotNull(mv);
	}

	@Test(expected=com.ctb.prism.core.exception.BusinessException.class)
	public void testLoadEduCenterUsers() throws Exception {
		request.setParameter("eduCenterId", "0");
		controller.loadEduCenterUsers(request, response, session);
		assertNotNull("Method return is void");
	}

	@Test
	public void testDownloadUsers() {
		controller.downloadUsers(request, response);
		assertNotNull("Method return is void");
	}

	@Test
	public void testResetUserPasswordForm() {
		ModelAndView mv = controller.resetUserPasswordForm();
		assertNotNull(mv);
	}

	@Test
	public void testGetUserForResetPassword() {
		String str = controller.getUserForResetPassword(request);
		assertNotNull(str);
	}

	@Test
	public void testResetUserPassword() {
		MockHttpServletRequest newRequest = new MockHttpServletRequest();
		newRequest.getSession().setAttribute(IApplicationConstants.CURRUSER, "abcd");
		String str = controller.resetUserPassword(newRequest);
		assertNotNull(str);
	}

}
