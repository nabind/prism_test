package com.ctb.prism.parent.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
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

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;
import com.ctb.prism.test.ParentTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class ParentServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IParentService parentService;

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
	public void testGetManageContentFilter() throws BusinessException {
		Map<String, Object> returnMap = parentService.getManageContentFilter(ParentTestHelper.helpGetManageContentFilter(testParams));
		assertNotNull(returnMap);
	}

	@Test
	public void testGetChildData() throws BusinessException {
		Map<String, Object> childDataMap = parentService.getChildData(ParentTestHelper.helpGetChildData(testParams));
		assertNotNull(childDataMap);
	}

	@Test
	public void testGetSecretQuestions() {
		List<QuestionTO> secretQuestionList = parentService.getSecretQuestions(ParentTestHelper.helpGetSecretQuestions(testParams));
		assertEquals(secretQuestionList.size(), Integer.parseInt(testParams.getNoOfSecretQuestions()));
	}

	@Test
	public void testCheckUserAvailability() {
		boolean availability = parentService.checkUserAvailability(ParentTestHelper.helpCheckUserAvailability(testParams));
		assertNotNull(availability);
	}

	@Test
	public void testCheckActiveUserAvailability() {
		boolean availability = parentService.checkActiveUserAvailability(ParentTestHelper.helpCheckActiveUserAvailability(testParams));
		assertNotNull(availability);
	}

	@Test
	public void testIsRoleAlreadyTagged() {
		String roleId = "1";
		boolean availability = parentService.isRoleAlreadyTagged(roleId, testParams.getUserName());
		assertNotNull(availability);
	}

	@Test
	public void testValidateIC() {
		ParentTO to = parentService.validateIC(ParentTestHelper.helpValidateIC(testParams));
		assertNotNull(to);
	}

	@Test
	public void testRegisterUser() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean value = parentService.registerUser(to);
		assertNotNull(value);
	}

	@Test
	public void testGetChildrenList() {
		List<StudentTO> studentList = parentService.getChildrenList(ParentTestHelper.helpGetChildrenList(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetParentList() {
		String orgId = "0";
		String adminYear = "";
		String searchParam = "";
		String orgMode = "";
		String moreCount = "0";
		ArrayList<ParentTO> parentList = parentService.getParentList(orgId, adminYear, searchParam, orgMode, moreCount);
		assertNotNull(parentList);
	}

	@Test
	public void testSearchParent() {
		String parentName = "";
		String tenantId = "0";
		String adminYear = "";
		String isExactSeacrh = "";
		String orgMode = "";
		ArrayList<ParentTO> parentList = parentService.searchParent(parentName, tenantId, adminYear, isExactSeacrh, orgMode);
		assertNotNull(parentList);
	}

	@Test
	public void testSearchParentAutoComplete() {
		String parentName = "";
		String tenantId = "0";
		String adminYear = "";
		String orgMode = "";
		String parentJson = parentService.searchParentAutoComplete(parentName, tenantId, adminYear, orgMode);
		assertNull(parentJson);
	}

	@Test
	public void testGetStudentList() {
		ArrayList<StudentTO> studentList = parentService.getStudentList(ParentTestHelper.helpGetStudentList(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testSearchStudentAutoComplete() {
		String studentName = "";
		String tenantId = "0";
		String adminyear = "";
		long customerId = 0L;
		String orgMode = "";
		String studentListJson = parentService.searchStudentAutoComplete(studentName, tenantId, adminyear, customerId, orgMode);
		assertNull(studentListJson);
	}

	@Test
	public void testSearchStudent() {
		String studentName = "";
		String tenantId = "0";
		String adminyear = "";
		long customerId = 0L;
		String orgMode = "";
		ArrayList<StudentTO> studentList = parentService.searchStudent(studentName, tenantId, adminyear, customerId, orgMode);
		assertNotNull(studentList);
	}

	@Test
	public void testSearchStudentOnRedirect() {
		ArrayList<StudentTO> studentList = parentService.searchStudentOnRedirect(ParentTestHelper.helpSearchStudentOnRedirect(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetAssessmentList() {
		String testElementId = "";
		List<StudentTO> studentList = parentService.getAssessmentList(testElementId);
		assertNotNull(studentList);
	}

	@Test
	public void testUpdateAssessmentDetails() throws Exception {
		String studentBioId = "";
		String administration = "";
		String invitationcode = "";
		String icExpirationStatus = "";
		String totalAvailableClaim = "";
		String expirationDate = "";
		boolean returnFlag = parentService.updateAssessmentDetails(studentBioId, administration, invitationcode, icExpirationStatus, totalAvailableClaim,
				expirationDate);
		assertNotNull(returnFlag);
	}

	@Test
	public void testFirstTimeUserLogin() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setUserName(testParams.getUserName());
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean status = parentService.firstTimeUserLogin(to);
		assertNotNull(status);
	}

	@Test
	public void testManageParentAccountDetails() {
		ParentTO to = parentService.manageParentAccountDetails(testParams.getUserName());
		assertNotNull(to);
	}

	@Test
	public void testGetSecurityQuestionForUser() {
		ArrayList<QuestionTO> questionList = parentService.getSecurityQuestionForUser(ParentTestHelper.helpGetSecurityQuestionForUser(testParams));
		assertNotNull(questionList);
	}

	@Test
	public void testValidateAnswers() {
		boolean value = parentService.validateAnswers(ParentTestHelper.helpValidateAnswers(testParams));
		assertNotNull(value);
	}

	@Test
	public void testGetUserNamesByEmail() {
		List<UserTO> userList = parentService.getUserNamesByEmail(ParentTestHelper.helpGetUserNamesByEmail(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testUpdateUserProfile() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean status = parentService.updateUserProfile(to);
		assertNotNull(status);
	}

	@Test
	public void testAddInvitationToAccount() {
		boolean status = parentService.addInvitationToAccount(ParentTestHelper.helpAddInvitationToAccount(testParams));
		assertNotNull(status);
	}

	@Test
	public void testGetSchoolOrgId() {
		String studentBioId = "7725740";
		String schoolId = parentService.getSchoolOrgId(studentBioId);
		assertNotNull(schoolId);
	}

	@Test
	public void testRegenerateActivationCode() throws Exception {
		StudentTO student = new StudentTO();
		boolean status = parentService.regenerateActivationCode(student);
		assertNotNull(status);
	}

	@Test
	public void testPopulateGrade() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> gradeList = parentService.populateGrade(ParentTestHelper.helpPopulateGrade(testParams));
		assertNotNull(gradeList);
	}

	@Test
	public void testPopulateSubtest() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentService.populateSubtest(ParentTestHelper.helpPopulateSubtest(testParams));
		assertNotNull(subtestList);
	}

	@Test
	public void testPopulateObjective() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectiveList = parentService.populateObjective(ParentTestHelper
				.helpPopulateObjective(testParams));
		assertNotNull(objectiveList);
	}

	@Test
	public void testAddNewContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentService.addNewContent(ParentTestHelper.helpAddNewContent(testParams));
		assertNotNull(to);
	}

	@Test(expected = BusinessException.class)
	// TODO
	public void testLoadManageContent() throws BusinessException {
		List<ManageContentTO> contentList = parentService.loadManageContent(ParentTestHelper.helpLoadManageContent(testParams));
		assertNotNull(contentList);
	}

	@Test
	public void testGetContentForEdit() throws BusinessException {
		ManageContentTO content = parentService.getContentForEdit(ParentTestHelper.helpGetContentForEdit(testParams));
		assertNull(content);
	}

	@Test
	public void testUpdateContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentService.updateContent(ParentTestHelper.helpUpdateContent(testParams));
		assertNotNull(to);
	}

	@Test
	public void testDeleteContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentService.deleteContent(ParentTestHelper.helpDeleteContent(testParams));
		assertNotNull(to);
	}

	@Test(expected = SQLException.class)
	// TODO
	public void testModifyGenericForEdit() throws BusinessException {
		ManageContentTO to = parentService.modifyGenericForEdit(ParentTestHelper.helpModifyGenericForEdit(testParams));
		assertNotNull(to);
	}

	@Test
	public void testGetArticleTypeDetails() throws BusinessException {
		List<ManageContentTO> contentList = parentService.getArticleTypeDetails(ParentTestHelper.helpGetArticleTypeDetails(testParams));
		assertNotNull(contentList);
	}

	@Test(expected = SQLException.class)
	// TODO
	public void testGetArticleDescription() throws BusinessException {
		ManageContentTO content = parentService.getArticleDescription(ParentTestHelper.helpGetArticleDescription(testParams));
		assertNotNull(content);
	}

	@Test
	public void testGetGradeSubtestInfo() throws BusinessException {
		List<ManageContentTO> contentList = parentService.getGradeSubtestInfo(ParentTestHelper.helpGetGradeSubtestInfo(testParams));
		assertNotNull(contentList);
	}

}
