package com.ctb.prism.parent.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.sql.SQLException;
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
public class ParentBusinessImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IParentBusiness parentBusiness;

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
		Map<String, Object> returnMap = parentBusiness.getManageContentFilter(ParentTestHelper.helpGetManageContentFilter(testParams));
		assertNotNull(returnMap);
	}

	@Test
	public void testGetChildData() throws BusinessException {
		Map<String, Object> childDataMap = parentBusiness.getChildData(ParentTestHelper.helpGetChildData(testParams));
		assertNotNull(childDataMap);
	}

	@Test
	public void testGetSecretQuestions() {
		List<QuestionTO> secretQuestionList = parentBusiness.getSecretQuestions(ParentTestHelper.helpGetSecretQuestions(testParams));
		assertEquals(secretQuestionList.size(), Integer.parseInt(testParams.getNoOfSecretQuestions()));
	}

	@Test
	public void testCheckUserAvailability() {
		boolean availability = parentBusiness.checkUserAvailability(ParentTestHelper.helpCheckUserAvailability(testParams));
		assertNotNull(availability);
	}

	@Test
	public void testCheckActiveUserAvailability() {
		boolean availability = parentBusiness.checkActiveUserAvailability(ParentTestHelper.helpCheckActiveUserAvailability(testParams));
		assertNotNull(availability);
	}

	@Test
	public void testIsRoleAlreadyTagged() {
		String roleId = "1";
		boolean availability = parentBusiness.isRoleAlreadyTagged(roleId, testParams.getUserName());
		assertNotNull(availability);
	}

	@Test
	public void testValidateIC() {
		ParentTO parent = parentBusiness.validateIC(ParentTestHelper.helpValidateIC(testParams));
		assertEquals(parent.getErrorMsg(), "NA");
	}

	@Test
	public void testValidateICBusinessLogic() throws Exception {
		Method method = ParentBusinessImpl.class.getDeclaredMethod("validateICBusinessLogic", ParentTO.class, Map.class);
		method.setAccessible(true);

		// if (parent == null) {
		ParentTO parent = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		parent = (ParentTO) method.invoke(parentBusiness, parent, paramMap);
		assertEquals(parent.getErrorMsg(), "IC_INVALID");

		// } else if (parent.getTotalAvailableCalim() == 0) {
		parent = new ParentTO();
		parent.setTotalAvailableCalim(0);
		parent = (ParentTO) method.invoke(parentBusiness, parent, paramMap);
		assertEquals(parent.getErrorMsg(), "IC_NOTAVAILABLE");

		// } else if
		// (IApplicationConstants.INACTIVE_FLAG.equals(parent.getIcExpirationStatus())
		// ||
		// IApplicationConstants.INACTIVE_FLAG.equals(parent.getIcActivationStatus()))
		// {
		parent = new ParentTO();
		parent.setTotalAvailableCalim(1);
		parent.setIcExpirationStatus("IN");
		parent.setIcActivationStatus("IN");
		parent = (ParentTO) method.invoke(parentBusiness, parent, paramMap);
		assertEquals(parent.getErrorMsg(), "IC_EXPIRED");

		// } else if
		// (IApplicationConstants.DELETED_FLAG.equals(parent.getIcActivationStatus()))
		// {
		parent = new ParentTO();
		parent.setTotalAvailableCalim(1);
		parent.setIcExpirationStatus("AC");
		parent.setIcActivationStatus("DE");
		parent = (ParentTO) method.invoke(parentBusiness, parent, paramMap);
		assertEquals(parent.getErrorMsg(), "IC_INVALID");

		// } else if (parent.getIsAlreadyClaimed() > 0) {
		parent = new ParentTO();
		parent.setTotalAvailableCalim(1);
		parent.setIcExpirationStatus("AC");
		parent.setIcActivationStatus("AC");
		parent.setIsAlreadyClaimed(1);
		parent = (ParentTO) method.invoke(parentBusiness, parent, paramMap);
		assertEquals(parent.getErrorMsg(), "IC_ALREADY_CLAIMED");
	}

	@Test
	public void testRegisterUser() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean value = parentBusiness.registerUser(to);
		assertNotNull(value);
	}

	@Test
	public void testGetChildrenList() {
		List<StudentTO> studentList = parentBusiness.getChildrenList(ParentTestHelper.helpGetChildrenList(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetParentList() {
		String orgId = "0";
		String adminYear = "";
		String searchParam = "";
		String orgMode = "";
		String moreCount = "0";
		ArrayList<ParentTO> parentList = parentBusiness.getParentList(orgId, adminYear, searchParam, orgMode, moreCount);
		assertNotNull(parentList);
	}

	@Test
	public void testSearchParent() {
		String parentName = "";
		String tenantId = "0";
		String adminYear = "";
		String isExactSeacrh = "";
		String orgMode = "";
		ArrayList<ParentTO> parentList = parentBusiness.searchParent(parentName, tenantId, adminYear, isExactSeacrh, orgMode);
		assertNotNull(parentList);
	}

	@Test
	public void testSearchParentAutoComplete() {
		String parentName = "";
		String tenantId = "0";
		String adminYear = "";
		String orgMode = "";
		String parentJson = parentBusiness.searchParentAutoComplete(parentName, tenantId, adminYear, orgMode);
		assertNull(parentJson);
	}

	@Test
	public void testGetStudentList() {
		ArrayList<StudentTO> studentList = parentBusiness.getStudentList(ParentTestHelper.helpGetStudentList(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testSearchStudentAutoComplete() {
		String studentName = "";
		String tenantId = "0";
		String adminyear = "";
		long customerId = 0L;
		String orgMode = "";
		String studentListJson = parentBusiness.searchStudentAutoComplete(studentName, tenantId, adminyear, customerId, orgMode);
		assertNull(studentListJson);
	}

	@Test
	public void testSearchStudent() {
		String studentName = "";
		String tenantId = "0";
		String adminyear = "";
		long customerId = 0L;
		String orgMode = "";
		ArrayList<StudentTO> studentList = parentBusiness.searchStudent(studentName, tenantId, adminyear, customerId, orgMode);
		assertNotNull(studentList);
	}

	@Test
	public void testSearchStudentOnRedirect() {
		ArrayList<StudentTO> studentList = parentBusiness.searchStudentOnRedirect(ParentTestHelper.helpSearchStudentOnRedirect(testParams));
		assertNotNull(studentList);
	}

	@Test
	public void testGetAssessmentList() {
		String testElementId = "";
		List<StudentTO> studentList = parentBusiness.getAssessmentList(testElementId);
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
		boolean returnFlag = parentBusiness.updateAssessmentDetails(studentBioId, administration, invitationcode, icExpirationStatus, totalAvailableClaim,
				expirationDate);
		assertNotNull(returnFlag);
	}

	@Test
	public void testFirstTimeUserLogin() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setUserName(testParams.getUserName());
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean status = parentBusiness.firstTimeUserLogin(to);
		assertNotNull(status);
	}

	@Test
	public void testManageParentAccountDetails() {
		ParentTO to = parentBusiness.manageParentAccountDetails(testParams.getUserName());
		assertNotNull(to);
	}

	@Test
	public void testGetSecurityQuestionForUser() {
		ArrayList<QuestionTO> questionList = parentBusiness.getSecurityQuestionForUser(ParentTestHelper.helpGetSecurityQuestionForUser(testParams));
		assertNotNull(questionList);
	}

	@Test
	public void testValidateAnswers() {
		boolean value = parentBusiness.validateAnswers(ParentTestHelper.helpValidateAnswers(testParams));
		assertNotNull(value);
	}

	@Test
	public void testGetUserNamesByEmail() {
		List<UserTO> userList = parentBusiness.getUserNamesByEmail(ParentTestHelper.helpGetUserNamesByEmail(testParams));
		assertNotNull(userList);
	}

	@Test
	public void testUpdateUserProfile() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		boolean status = parentBusiness.updateUserProfile(to);
		assertNotNull(status);
	}

	@Test
	public void testAddInvitationToAccount() {
		boolean status = parentBusiness.addInvitationToAccount(ParentTestHelper.helpAddInvitationToAccount(testParams));
		assertNotNull(status);
	}

	@Test
	public void testGetSchoolOrgId() {
		String studentBioId = "7725740";
		String schoolId = parentBusiness.getSchoolOrgId(studentBioId);
		assertNotNull(schoolId);
	}

	@Test(expected = BusinessException.class)
	public void testRegenerateActivationCode() throws Exception {
		StudentTO student = new StudentTO();
		boolean status = parentBusiness.regenerateActivationCode(student);
		assertNotNull(status);
	}

	@Test
	public void testPopulateGrade() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> gradeList = parentBusiness.populateGrade(ParentTestHelper.helpPopulateGrade(testParams));
		assertNotNull(gradeList);
	}

	@Test
	public void testPopulateSubtest() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentBusiness.populateSubtest(ParentTestHelper.helpPopulateSubtest(testParams));
		assertNotNull(subtestList);
	}

	@Test
	public void testPopulateObjective() throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectiveList = parentBusiness.populateObjective(ParentTestHelper
				.helpPopulateObjective(testParams));
		assertNotNull(objectiveList);
	}

	@Test
	public void testAddNewContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentBusiness.addNewContent(ParentTestHelper.helpAddNewContent(testParams));
		assertNotNull(to);
	}

	@Test(expected = BusinessException.class)
	// TODO
	public void testLoadManageContent() throws BusinessException {
		List<ManageContentTO> contentList = parentBusiness.loadManageContent(ParentTestHelper.helpLoadManageContent(testParams));
		assertNotNull(contentList);
	}

	@Test
	public void testGetContentForEdit() throws BusinessException {
		ManageContentTO content = parentBusiness.getContentForEdit(ParentTestHelper.helpGetContentForEdit(testParams));
		assertNull(content);
	}

	@Test
	public void testUpdateContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentBusiness.updateContent(ParentTestHelper.helpUpdateContent(testParams));
		assertNotNull(to);
	}

	@Test
	public void testDeleteContent() throws BusinessException {
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentBusiness.deleteContent(ParentTestHelper.helpDeleteContent(testParams));
		assertNotNull(to);
	}

	@Test(expected = SQLException.class)
	// TODO
	public void testModifyGenericForEdit() throws BusinessException {
		ManageContentTO to = parentBusiness.modifyGenericForEdit(ParentTestHelper.helpModifyGenericForEdit(testParams));
		assertNotNull(to);
	}

	@Test
	public void testGetArticleTypeDetails() throws BusinessException {
		List<ManageContentTO> contentList = parentBusiness.getArticleTypeDetails(ParentTestHelper.helpGetArticleTypeDetails(testParams));
		assertNotNull(contentList);
	}

	@Test(expected = SQLException.class)
	// TODO
	public void testGetArticleDescription() throws BusinessException {
		ManageContentTO content = parentBusiness.getArticleDescription(ParentTestHelper.helpGetArticleDescription(testParams));
		assertNotNull(content);
	}

	@Test
	public void testGetGradeSubtestInfo() throws BusinessException {
		List<ManageContentTO> contentList = parentBusiness.getGradeSubtestInfo(ParentTestHelper.helpGetGradeSubtestInfo(testParams));
		assertNotNull(contentList);
	}
}
