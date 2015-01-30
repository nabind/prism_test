package com.ctb.prism.parent.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

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
@TransactionConfiguration(transactionManager = "txManager")
public class ParentDAOImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ParentDAOImpl parentDao;

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
	public void testGetSecretQuestions() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", testParams.getUserName());
		parentDao.getSecretQuestions(paramMap);

		List<QuestionTO> secretQuestionList = parentDao.getSecretQuestions(ParentTestHelper.helpGetSecretQuestions(testParams));
		assertEquals(secretQuestionList.size(), Integer.parseInt(testParams.getNoOfSecretQuestions()));
	}

	@Test
	public void testCheckUserAvailability() {
		parentDao.checkUserAvailability(new HashMap<String, Object>());

		boolean availability = parentDao.checkUserAvailability(ParentTestHelper.helpCheckUserAvailability(testParams));
		assertNotNull(availability);
	}

	@Test
	public void testCheckActiveUserAvailability() {
		boolean availability = parentDao.checkActiveUserAvailability(ParentTestHelper.helpCheckActiveUserAvailability(testParams));
		assertNotNull(availability);
	}

	@Test
	public void testIsRoleAlreadyTagged() {
		String roleId = "1";
		boolean availability = parentDao.isRoleAlreadyTagged(roleId, testParams.getUserName());
		assertNotNull(availability);
	}

	@Test
	public void testValidateIC() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invitationCode", testParams.getInvitationCode());
		paramMap.put("loggedinUserTO", null);
		parentDao.validateIC(paramMap);

		ParentTO to = parentDao.validateIC(ParentTestHelper.helpValidateIC(testParams));
		assertNotNull(to);
	}

	@Test
	public void testGetStudentForIC() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", null);
		paramMap.put("invitationCode", testParams.getInvitationCode());
		parentDao.getStudentForIC(paramMap);

		ParentTO to = parentDao.getStudentForIC(ParentTestHelper.helpGetStudentForIC(testParams));
		assertNotNull(to);
	}

	@Test
	public void testRegisterUser() throws BusinessException {
		ParentTO to = ParentTestHelper.getParentTO(testParams);
		to.setContractName("");
		to.setFirstTimeUser(false);
		parentDao.registerUser(to);

		to = ParentTestHelper.getParentTO(testParams);
		boolean value = parentDao.registerUser(to);
		assertNotNull(value);
	}

	@Test
	public void testGetChildrenList() {
		Map<String, Object> paramMap = ParentTestHelper.helpGetChildrenList(testParams);
		paramMap.put("isPN", "N");
		List<StudentTO> studentList = parentDao.getChildrenList(paramMap);
		assertNotNull(studentList);

		studentList = parentDao.getChildrenList(ParentTestHelper.helpGetChildrenList(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetParentList() {
		String orgId = "0";
		String adminYear = "";
		String searchParam = "";
		String orgMode = "";
		String moreCount = "0";
		ArrayList<ParentTO> parentList = parentDao.getParentList(orgId, adminYear, searchParam, orgMode, moreCount);
		assertNotNull(parentList);

		orgId = "0_0";
		searchParam = "abc";
		parentList = parentDao.getParentList(orgId, adminYear, searchParam, orgMode, moreCount);
		assertNotNull(parentList);

		orgId = "0_0";
		searchParam = null;
		parentList = parentDao.getParentList(orgId, adminYear, searchParam, orgMode, moreCount);
		assertNotNull(parentList);
	}

	@Test
	public void testSearchParent() {
		String parentName = "";
		String tenantId = "0";
		String adminYear = "";
		String isExactSeacrh = "";
		String orgMode = "";
		ArrayList<ParentTO> parentList = parentDao.searchParent(parentName, tenantId, adminYear, isExactSeacrh, orgMode);
		assertNotNull(parentList);

		isExactSeacrh = "N";
		parentList = parentDao.searchParent(parentName, tenantId, adminYear, isExactSeacrh, orgMode);
		assertNotNull(parentList);
	}

	@Test
	public void testSearchParentAutoComplete() {
		String parentName = "";
		String tenantId = "0";
		String adminYear = "";
		String orgMode = "";
		String parentJson = parentDao.searchParentAutoComplete(parentName, tenantId, adminYear, orgMode);
		assertNull(parentJson);
	}

	@Test
	public void testGetStudentList() {
		String orgId = "0|0";
		Map<String, Object> paramMap = ParentTestHelper.helpGetStudentList(testParams);
		paramMap.put("scrollId", orgId);
		parentDao.getStudentList(paramMap);

		paramMap.put("searchParam", null);
		parentDao.getStudentList(paramMap);

		ArrayList<StudentTO> studentList = parentDao.getStudentList(ParentTestHelper.helpGetStudentList(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testSearchStudentAutoComplete() {
		String studentName = "";
		String tenantId = "0";
		String adminyear = "";
		long customerId = 0L;
		String orgMode = "";
		String studentListJson = parentDao.searchStudentAutoComplete(studentName, tenantId, adminyear, customerId, orgMode);
		assertNull(studentListJson);
	}

	@Test
	public void testSearchStudent() {
		String studentName = "";
		String tenantId = "0";
		String adminyear = "";
		long customerId = 0L;
		String orgMode = "";
		ArrayList<StudentTO> studentList = parentDao.searchStudent(studentName, tenantId, adminyear, customerId, orgMode);
		assertNotNull(studentList);
	}

	@Test
	public void testSearchStudentOnRedirect() {
		ArrayList<StudentTO> studentList = parentDao.searchStudentOnRedirect(ParentTestHelper.helpSearchStudentOnRedirect(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetAssessmentList() {
		String testElementId = "";
		List<StudentTO> studentList = parentDao.getAssessmentList(testElementId);
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
		boolean returnFlag = parentDao.updateAssessmentDetails(studentBioId, administration, invitationcode, icExpirationStatus, totalAvailableClaim,
				expirationDate);
		assertNotNull(returnFlag);
	}

	@Test
	public void testFirstTimeUserLogin() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setUserName(testParams.getUserName());
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean status = parentDao.firstTimeUserLogin(to);
		assertNotNull(status);
	}

	@Test
	public void testManageParentAccountDetails() {
		ParentTO to = parentDao.manageParentAccountDetails(testParams.getUserName());
		assertNotNull(to);
	}

	@Test
	public void testGetSecurityQuestionForUser() {
		ArrayList<QuestionTO> questionList = parentDao.getSecurityQuestionForUser(ParentTestHelper.helpGetSecurityQuestionForUser(testParams));
		assertNotNull(questionList);
	}

	@Test
	public void testValidateAnswers() {
		boolean value = parentDao.validateAnswers(ParentTestHelper.helpValidateAnswers(testParams));
		assertNotNull(value);
	}

	@Test
	public void testGetUserNamesByEmail() {
		List<UserTO> userList = parentDao.getUserNamesByEmail(ParentTestHelper.helpGetUserNamesByEmail(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testUpdateUserProfile() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean status = parentDao.updateUserProfile(to);
		assertNotNull(status);
	}

	@Test
	public void testAddInvitationToAccount() {
		boolean status = parentDao.addInvitationToAccount(ParentTestHelper.helpAddInvitationToAccount(testParams));
		assertNotNull(status);
	}

	@Test
	public void testGetSchoolOrgId() {
		String studentBioId = "7725740";
		String schoolId = parentDao.getSchoolOrgId(studentBioId);
		assertNotNull(schoolId);
	}

	@Test
	public void testRegenerateActivationCode() throws Exception {
		StudentTO student = new StudentTO();
		boolean status = parentDao.regenerateActivationCode(student);
		assertNotNull(status);
	}

	@Test
	public void testPopulateGrade() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> gradeList = parentDao.populateGrade(ParentTestHelper.helpPopulateGrade(testParams));
		assertNotNull(gradeList);
	}

	@Test
	public void testPopulateSubtest() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentDao.populateSubtest(ParentTestHelper.helpPopulateSubtest(testParams));
		assertNotNull(subtestList);
	}

	@Test
	public void testPopulateObjective() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectiveList = parentDao.populateObjective(ParentTestHelper.helpPopulateObjective(testParams));
		assertNotNull(objectiveList);
	}

	@Test
	public void testAddNewContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentDao.addNewContent(ParentTestHelper.helpAddNewContent(testParams));
		assertNotNull(to);
	}

	@Test(expected=com.ctb.prism.core.exception.BusinessException.class)
	public void testLoadManageContent() throws BusinessException {
		Map<String, Object> paramMap = ParentTestHelper.helpLoadManageContent(testParams);
		paramMap.put("checkFirstLoad", "true");
		parentDao.loadManageContent(paramMap);

		List<ManageContentTO> contentList = parentDao.loadManageContent(ParentTestHelper.helpLoadManageContent(testParams));
		assertNotNull(contentList);
	}

	@Test
	public void testGetContentForEdit() throws BusinessException {
		ManageContentTO content = parentDao.getContentForEdit(ParentTestHelper.helpGetContentForEdit(testParams));
		assertNull(content);
	}

	@Test
	public void testUpdateContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentDao.updateContent(ParentTestHelper.helpUpdateContent(testParams));
		assertNotNull(to);
	}

	@Test
	public void testDeleteContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentDao.deleteContent(ParentTestHelper.helpDeleteContent(testParams));
		assertNotNull(to);
	}

	@Test
	public void testModifyGenericForEdit() throws BusinessException {
		ManageContentTO to = parentDao.modifyGenericForEdit(ParentTestHelper.helpModifyGenericForEdit(testParams));
		assertNotNull(to);
	}

	@Test
	public void testGetStudentSubtest() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentDao.getStudentSubtest(ParentTestHelper.helpGetStudentSubtest(testParams));
		assertNotNull(subtestList);
	}

	@Test
	public void testGetArticleTypeDetails() throws BusinessException {
		List<ManageContentTO> contentList = parentDao.getArticleTypeDetails(ParentTestHelper.helpGetArticleTypeDetails(testParams));
		assertNotNull(contentList);
	}

	@Test
	public void testGetArticleDescription() throws BusinessException {
		ManageContentTO content = parentDao.getArticleDescription(ParentTestHelper.helpGetArticleDescription(testParams));
		assertNotNull(content);
	}

	@Test
	public void testGetGradeSubtestInfo() throws BusinessException {
		List<ManageContentTO> contentList = parentDao.getGradeSubtestInfo(ParentTestHelper.helpGetGradeSubtestInfo(testParams));
		assertNotNull(contentList);
	}

	/**
	 * Testing private method by reflection.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetParentAccountDetailsByTestElementId() throws Exception {
		Method method = ParentDAOImpl.class.getDeclaredMethod("getParentAccountDetailsByTestElementId", String.class, long.class);
		method.setAccessible(true);

		String testElementId = null;
		long customerId = 0L;
		method.invoke(parentDao, testElementId, customerId);

		testElementId = "";
		ArrayList<ParentTO> parentList = (ArrayList<ParentTO>) method.invoke(parentDao, testElementId, customerId);
		assertNotNull(parentList);
	}
}
