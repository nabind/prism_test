package com.ctb.prism.parent.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.security.tokens.UsernamePasswordAuthenticationToken;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/test-context.xml" })
public class ParentServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IParentService parentService;

	// @Value("${test.userName}")
	private String userName = "ctbadmin";

	// @Value("${test.password}")
	private String password = "Passwd12";

	// @Value("${test.contractName}")
	private String contractName = "inors";

	// @Value("${test.noOfSecretQuestions}")
	private int noOfSecretQuestions = 11;
	private String invitationCode = "EZM6-7DJZ-69RS-S3ZC";

	@Before
	public void setUp() throws Exception {
		UserTO user = new UserTO();
		user.setUserName(userName);
		user.setPassword(password);
		user.setContractName(contractName);
		UserDetails targetUser = new AuthenticatedUser(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword());
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSecretQuestions() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", contractName);
		List<QuestionTO> secretQuestionList = parentService.getSecretQuestions(paramMap);
		assertEquals(secretQuestionList.size(), noOfSecretQuestions);
	}

	@Test
	public void testCheckUserAvailability() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", userName);
		paramMap.put("contractName", contractName);
		boolean availability = parentService.checkUserAvailability(paramMap);
		assertNotNull(availability);
	}

	@Test
	public void testCheckActiveUserAvailability() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", userName);
		paramMap.put("contractName", contractName);
		boolean availability = parentService.checkActiveUserAvailability(paramMap);
		assertNotNull(availability);
	}

	@Test
	public void testIsRoleAlreadyTagged() {
		String roleId = "1";
		boolean availability = parentService.isRoleAlreadyTagged(roleId, userName);
		assertNotNull(availability);
	}

	@Test
	public void testValidateIC() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invitationCode", invitationCode);
		paramMap.put("contractName", contractName);
		ParentTO to = parentService.validateIC(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testRegisterUser() throws BusinessException {
		ParentTO to = new ParentTO();
		boolean value = parentService.registerUser(to);
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
		List<StudentTO> studentList = parentService.getChildrenList(paramMap);
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

		ArrayList<StudentTO> studentList = parentService.getStudentList(paramMap);
		assertNull(studentList);
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
	public void testGetAssessmentList() {
		String testElementId = "";
		List<StudentTO> studentList = parentService.getAssessmentList(testElementId);
		assertNotNull(studentList);
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

		ArrayList<StudentTO> studentList = parentService.searchStudentOnRedirect(paramMap);
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
		to.setUserName(userName);
		to.setQuestionToList(getQuestionList());
		boolean status = parentService.firstTimeUserLogin(to);
		assertNotNull(status);
	}

	@Test
	public void testManageParentAccountDetails() {
		ParentTO to = parentService.manageParentAccountDetails(userName);
		assertNotNull(to);
	}

	@Test
	public void testUpdateUserProfile() throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(getQuestionList());
		boolean status = parentService.updateUserProfile(to);
		assertNotNull(status);
	}

	@Test
	public void testAddInvitationToAccount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("curruser", userName);
		paramMap.put("invitationCode", invitationCode);
		boolean status = parentService.addInvitationToAccount(paramMap);
		assertNotNull(status);
	}

	@Test
	public void testGetSchoolOrgId() {
		String studentBioId = "7725740";
		String schoolId = parentService.getSchoolOrgId(studentBioId);
		assertNotNull(schoolId);
	}

	@Test
	public void testGetSecurityQuestionForUser() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", contractName);
		paramMap.put("username", userName);
		ArrayList<QuestionTO> questionList = parentService.getSecurityQuestionForUser(paramMap);
		assertNotNull(questionList);
	}

	@Test
	public void testValidateAnswers() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", contractName);
		paramMap.put("username", userName);
		List<QuestionTO> questionToList = getQuestionList();
		paramMap.put("questionToList", questionToList);
		boolean value = parentService.validateAnswers(paramMap);
		assertNotNull(value);
	}

	@Test
	public void testGetUserNamesByEmail() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", contractName);
		paramMap.put("emailId", "a@a.a");
		List<UserTO> userList = parentService.getUserNamesByEmail(paramMap);
		assertNotNull(userList);
	}

	@Test
	public void testRegenerateActivationCode() throws Exception {
		StudentTO student = new StudentTO();
		boolean status = parentService.regenerateActivationCode(student);
		assertNotNull(status);
	}

	@Test
	public void testGetManageContentFilter() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testPopulateGrade() throws BusinessException {
		long custProdId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> gradeList = parentService.populateGrade(paramMap);
		assertNotNull(gradeList);
	}

	@Test
	public void testPopulateSubtest() throws BusinessException {
		long custProdId = 0L;
		long gradeId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		paramMap.put("gradeId", gradeId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> subtestList = parentService.populateSubtest(paramMap);
		assertNotNull(subtestList);
	}

	@Test
	public void testPopulateObjective() throws BusinessException {
		long subtestId = 0L;
		long gradeId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("subtestId", subtestId);
		paramMap.put("gradeId", gradeId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> objectiveList = parentService.populateObjective(paramMap);
		assertNotNull(objectiveList);
	}

	@Test
	public void testAddNewContent() throws BusinessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		ManageContentTO manageContentTO = new ManageContentTO();
		paramMap.put("manageContentTO", manageContentTO);
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentService.addNewContent(paramMap);
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

		List<ManageContentTO> contentList = parentService.loadManageContent(paramMap);
		assertNotNull(contentList);
	}

	@Test
	public void testGetContentForEdit() throws BusinessException {
		long contentId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contentId", contentId);
		ManageContentTO content = parentService.getContentForEdit(paramMap);
		assertNotNull(content);
	}

	@Test
	public void testUpdateContent() throws BusinessException {
		ManageContentTO content = new ManageContentTO();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("manageContentTO", content);
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentService.updateContent(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testDeleteContent() throws BusinessException {
		long contentId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contentId", contentId);
		com.ctb.prism.core.transferobject.ObjectValueTO to = parentService.deleteContent(paramMap);
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

		ManageContentTO to = parentService.modifyGenericForEdit(paramMap);
		assertNotNull(to);
	}

	@Test
	public void testGetChildData() {
		assertNotNull("Not yet implemented");
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

		List<ManageContentTO> contentList = parentService.getArticleTypeDetails(paramMap);
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

		ManageContentTO content = parentService.getArticleDescription(paramMap);
		assertNotNull(content);
	}

	@Test
	public void testGetGradeSubtestInfo() throws BusinessException {
		UserTO user = new UserTO();
		user.setCustomerId("0");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedinUserTO", user);
		List<ManageContentTO> contentList = parentService.getGradeSubtestInfo(paramMap);
		assertNotNull(contentList);
	}

	private List<QuestionTO> getQuestionList() {
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		QuestionTO question = new QuestionTO();
		question.setQuestionId(1L);
		question.setAnswer("1");
		questionToList.add(question);
		question = new QuestionTO();
		question.setQuestionId(2L);
		question.setAnswer("2");
		questionToList.add(question);
		question = new QuestionTO();
		question.setQuestionId(3L);
		question.setAnswer("3");
		questionToList.add(question);
		return questionToList;
	}

}
