package com.ctb.prism.parent.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
		paramMap.put("contractName", testParams.getContractName());
		List<QuestionTO> secretQuestionList = parentDao.getSecretQuestions(paramMap);
		assertEquals(secretQuestionList.size(), testParams.getNoOfSecretQuestions());
	}

	@Test
	public void testCheckUserAvailability() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", testParams.getUserName());
		paramMap.put("contractName", testParams.getContractName());
		boolean availability = parentDao.checkUserAvailability(paramMap);
		assertNotNull(availability);
	}

	@Test
	public void testCheckActiveUserAvailability() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", testParams.getUserName());
		paramMap.put("contractName", testParams.getContractName());
		boolean availability = parentDao.checkActiveUserAvailability(paramMap);
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
		paramMap.put("contractName", testParams.getContractName());
		ParentTO to = parentDao.validateIC(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testGetStudentForIC() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invitationCode", testParams.getInvitationCode());
		paramMap.put("contractName", testParams.getContractName());
		ParentTO to = parentDao.getStudentForIC(paramMap);
		assertNotNull(to);
	}

	@Test
	@Ignore
	public void testRegisterUser() throws BusinessException {
		ParentTO to = new ParentTO();
		boolean value = parentDao.registerUser(to);
		assertNotNull(value);
	}

	@Test
	public void testGetChildrenList() {
		String isPN = "";
		String parentName = "";
		String clickedTreeNode = "";
		String adminYear = "";
		String orgMode = "";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isPN", isPN);
		paramMap.put("parentName", parentName);
		paramMap.put("clickedTreeNode", clickedTreeNode);
		paramMap.put("adminYear", adminYear);
		paramMap.put("orgMode", orgMode);
		List<StudentTO> studentList = parentDao.getChildrenList(paramMap);
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
	@Ignore
	public void testGetStudentList() {
		String orgId = "0";
		String adminYear = "";
		String searchParam = "";
		String currCustomer = "0";
		String orgMode = "";
		String moreCount = "0";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scrollId", orgId);
		paramMap.put("adminYear", adminYear);
		paramMap.put("searchParam", searchParam);
		paramMap.put("currCustomer", currCustomer);
		paramMap.put("orgMode", orgMode);
		paramMap.put("moreCount", moreCount);

		ArrayList<StudentTO> studentList = parentDao.getStudentList(paramMap);
		assertNull(studentList);
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
		String tesElementId = "";
		String tenantId = "";
		String customerIdString = "0";
		String orgMode = "";
		String adminYear = "";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("studentBioId", tesElementId);
		paramMap.put("scrollId", tenantId);
		paramMap.put("customer", customerIdString);
		paramMap.put("orgMode", orgMode);
		paramMap.put("adminYear", adminYear);

		ArrayList<StudentTO> studentList = parentDao.searchStudentOnRedirect(paramMap);
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
		to.setQuestionToList(ParentTestHelper.getQuestionList());
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put("username", testParams.getUserName());
		ArrayList<QuestionTO> questionList = parentDao.getSecurityQuestionForUser(paramMap);
		assertNotNull(questionList);
	}

	@Test
	public void testValidateAnswers() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put("username", testParams.getUserName());
		List<QuestionTO> questionToList = ParentTestHelper.getQuestionList();
		paramMap.put("questionToList", questionToList);
		boolean value = parentDao.validateAnswers(paramMap);
		assertNotNull(value);
	}

	@Test
	public void testGetUserNamesByEmail() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put("emailId", "a@a.a");
		List<UserTO> userList = parentDao.getUserNamesByEmail(paramMap);
		assertNotNull(userList);
	}

	@Test
	public void testUpdateUserProfile() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList());
		boolean status = parentDao.updateUserProfile(to);
		assertNotNull(status);
	}

	@Test
	public void testAddInvitationToAccount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("curruser", testParams.getUserName());
		paramMap.put("invitationCode", testParams.getInvitationCode());
		boolean status = parentDao.addInvitationToAccount(paramMap);
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
		long custProdId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> gradeList = parentDao.populateGrade(paramMap);
		assertNotNull(gradeList);
	}

	@Test
	public void testPopulateSubtest() throws BusinessException {
		long custProdId = 0L;
		long gradeId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		paramMap.put("gradeId", gradeId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentDao.populateSubtest(paramMap);
		assertNotNull(subtestList);
	}

	@Test
	public void testPopulateObjective() throws BusinessException {
		long subtestId = 0L;
		long gradeId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("subtestId", subtestId);
		paramMap.put("gradeId", gradeId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectiveList = parentDao.populateObjective(paramMap);
		assertNotNull(objectiveList);
	}

	@Test
	public void testAddNewContent() throws BusinessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		ManageContentTO manageContentTO = new ManageContentTO();
		paramMap.put("manageContentTO", manageContentTO);
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentDao.addNewContent(paramMap);
		assertNull(to);
	}

	@Test
	public void testLoadManageContent() throws BusinessException {
		String custProdId = "0";
		String subtestId = "0";
		String objectiveId = "0";
		String contentTypeId = "0";
		String checkFirstLoad = "";
		String moreCount = "0";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		paramMap.put("subtestId", subtestId);
		paramMap.put("objectiveId", objectiveId);
		paramMap.put("contentTypeId", contentTypeId);
		paramMap.put("checkFirstLoad", checkFirstLoad);
		paramMap.put("moreCount", moreCount);

		List<ManageContentTO> contentList = parentDao.loadManageContent(paramMap);
		assertNotNull(contentList);
	}

	@Test
	public void testGetContentForEdit() throws BusinessException {
		long contentId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contentId", contentId);
		ManageContentTO content = parentDao.getContentForEdit(paramMap);
		assertNotNull(content);
	}

	@Test
	public void testUpdateContent() throws BusinessException {
		ManageContentTO content = new ManageContentTO();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("manageContentTO", content);
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentDao.updateContent(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testDeleteContent() throws BusinessException {
		long contentId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contentId", contentId);
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentDao.deleteContent(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testModifyGenericForEdit() throws BusinessException {
		long custProdId = 0L;
		long gradeId = 0L;
		long subtestId = 0L;
		long objectiveId = 0L;
		String type = "";
		String performanceLevelId = "";
		String statusCodeId = "";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		paramMap.put("gradeId", gradeId);
		paramMap.put("subtestId", subtestId);
		paramMap.put("objectiveId", objectiveId);
		paramMap.put("type", type);
		paramMap.put("performanceLevelId", performanceLevelId);
		paramMap.put("statusCodeId", statusCodeId);

		ManageContentTO to = parentDao.modifyGenericForEdit(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testGetStudentSubtest() throws BusinessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String testElementId = "";
		paramMap.put("testElementId", testElementId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentDao.getStudentSubtest(paramMap);
		assertNotNull(subtestList);
	}

	@Test
	public void testGetArticleTypeDetails() throws BusinessException {
		long studentBioId = 0L;
		long subtestId = 0L;
		long studentGradeId = 0L;
		String contentType = "";
		long custProdId = 0L;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("subtestId", subtestId);
		paramMap.put("studentGradeId", studentGradeId);
		paramMap.put("contentType", contentType);
		paramMap.put("custProdId", custProdId);

		List<ManageContentTO> contentList = parentDao.getArticleTypeDetails(paramMap);
		assertNotNull(contentList);
	}

	@Test
	public void testGetArticleDescription() throws BusinessException {
		long custProdId = 0L;
		long studentBioId = 0L;
		long articleId = 0L;
		String contentType = "";
		long subtestId = 0L;
		long studentGradeId = 0L;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("articleId", articleId);
		paramMap.put("contentType", contentType);
		paramMap.put("subtestId", subtestId);
		paramMap.put("studentGradeId", studentGradeId);

		ManageContentTO content = parentDao.getArticleDescription(paramMap);
		assertNotNull(content);
	}

	@Test
	public void testGetGradeSubtestInfo() throws BusinessException {
		UserTO user = new UserTO();
		user.setCustomerId("0");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedinUserTO", user);
		List<ManageContentTO> contentList = parentDao.getGradeSubtestInfo(paramMap);
		assertNotNull(contentList);
	}
}
