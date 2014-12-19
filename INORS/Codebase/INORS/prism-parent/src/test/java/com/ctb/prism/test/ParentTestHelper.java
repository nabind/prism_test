package com.ctb.prism.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

/**
 * This class provides input parameters for test methods.
 * 
 * @author TCS
 *
 */
public class ParentTestHelper {

	public static List<QuestionTO> getQuestionList(TestParams testParams) {
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		QuestionTO question = getQuestionTO();
		questionToList.add(question);

		question = new QuestionTO();
		question.setQuestionId(2L);
		question.setAnswerId(2L);
		question.setAnswer("2");
		questionToList.add(question);
		question = new QuestionTO();
		question.setQuestionId(3L);
		question.setAnswerId(3L);
		question.setAnswer("3");
		questionToList.add(question);
		return questionToList;
	}

	public static Map<String, Object> helpGetSecretQuestions(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpCheckUserAvailability(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", testParams.getUserName());
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpCheckActiveUserAvailability(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", testParams.getUserName());
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpValidateIC(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invitationCode", testParams.getInvitationCode());
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetStudentForIC(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invitationCode", testParams.getInvitationCode());
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public ParentTO helpRegisterUser(TestParams testParams) throws BusinessException {
		ParentTO to = new ParentTO();
		return to;
	}

	public static Map<String, Object> helpGetChildrenList(TestParams testParams) {
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
		return paramMap;
	}

	public static Map<String, Object> helpGetStudentList(TestParams testParams) {
		String orgId = "0";
		String adminYear = "";
		String searchParam = "abc";
		long currCustomer = 0L;
		String orgMode = "";
		String moreCount = "0";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scrollId", orgId);
		paramMap.put("adminYear", adminYear);
		paramMap.put("searchParam", searchParam);
		paramMap.put("currCustomer", currCustomer);
		paramMap.put("orgMode", orgMode);
		paramMap.put("moreCount", moreCount);

		return paramMap;
	}

	public static Map<String, Object> helpSearchStudentOnRedirect(TestParams testParams) {
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

		return paramMap;
	}

	public ParentTO helpFirstTimeUserLogin(TestParams testParams) throws BusinessException {
		ParentTO to = new ParentTO();
		to.setUserName(testParams.getUserName());
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		return to;
	}

	public static Map<String, Object> helpGetSecurityQuestionForUser(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put("username", testParams.getUserName());
		return paramMap;
	}

	public static Map<String, Object> helpValidateAnswers(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put("username", testParams.getUserName());
		List<QuestionTO> questionToList = ParentTestHelper.getQuestionList(testParams);
		paramMap.put("questionToList", questionToList);
		return paramMap;
	}

	public static Map<String, Object> helpGetUserNamesByEmail(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put("emailId", "a@a.a");
		return paramMap;
	}

	public ParentTO helpUpdateUserProfile(TestParams testParams) throws BusinessException {
		ParentTO to = new ParentTO();
		to.setQuestionToList(ParentTestHelper.getQuestionList(testParams));
		return to;
	}

	public static Map<String, Object> helpAddInvitationToAccount(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("curruser", testParams.getUserName());
		paramMap.put("invitationCode", testParams.getInvitationCode());
		return paramMap;
	}

	public StudentTO helpRegenerateActivationCode(TestParams testParams) throws Exception {
		StudentTO student = new StudentTO();
		return student;
	}

	public static Map<String, Object> helpPopulateGrade(TestParams testParams) throws BusinessException {
		long custProdId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		return paramMap;
	}

	public static Map<String, Object> helpPopulateSubtest(TestParams testParams) throws BusinessException {
		long custProdId = 0L;
		long gradeId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custProdId", custProdId);
		paramMap.put("gradeId", gradeId);
		return paramMap;
	}

	public static Map<String, Object> helpPopulateObjective(TestParams testParams) throws BusinessException {
		long subtestId = 0L;
		long gradeId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("subtestId", subtestId);
		paramMap.put("gradeId", gradeId);
		return paramMap;
	}

	public static Map<String, Object> helpAddNewContent(TestParams testParams) throws BusinessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		ManageContentTO manageContentTO = getManageContentTO();
		paramMap.put("manageContentTO", manageContentTO);
		return paramMap;
	}

	public static Map<String, Object> helpLoadManageContent(TestParams testParams) throws BusinessException {
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

		return paramMap;
	}

	public static Map<String, Object> helpGetContentForEdit(TestParams testParams) throws BusinessException {
		long contentId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contentId", contentId);
		return paramMap;
	}

	public static Map<String, Object> helpUpdateContent(TestParams testParams) throws BusinessException {
		ManageContentTO content = getManageContentTO();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("manageContentTO", content);
		return paramMap;
	}

	public static Map<String, Object> helpDeleteContent(TestParams testParams) throws BusinessException {
		long contentId = 0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contentId", contentId);
		return paramMap;
	}

	public static Map<String, Object> helpModifyGenericForEdit(TestParams testParams) throws BusinessException {
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

		return paramMap;
	}

	public static Map<String, Object> helpGetStudentSubtest(TestParams testParams) throws BusinessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String testElementId = "";
		paramMap.put("testElementId", testElementId);
		return paramMap;
	}

	public static Map<String, Object> helpGetArticleTypeDetails(TestParams testParams) throws BusinessException {
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

		return paramMap;
	}

	public static Map<String, Object> helpGetArticleDescription(TestParams testParams) throws BusinessException {
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

		return paramMap;
	}

	public static Map<String, Object> helpGetGradeSubtestInfo(TestParams testParams) throws BusinessException {
		UserTO user = new UserTO();
		user.setCustomerId("0");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedinUserTO", user);
		return paramMap;
	}

	public static Map<String, Object> helpGetManageContentFilter(TestParams testParams) {
		UserTO user = new UserTO();
		user.setCustomerId("0");
		user.setOrgId("0");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedinUserTO", user);
		return paramMap;
	}

	public static Map<String, Object> helpGetChildData(TestParams testParams) {
		String reportName = "";
		final String messageType = "";
		final String messageName = "";
		final long custProdId = 0L;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", reportName);
		paramMap.put("MESSAGE_TYPE", messageType);
		paramMap.put("MESSAGE_NAME", messageName);
		paramMap.put("custProdId", custProdId);
		paramMap.put("contractName", testParams.getContractName());

		String testElementId = "";
		paramMap.put("testElementId", testElementId);

		return paramMap;
	}

	public static UserTO getUserTO() {
		UserTO user = new UserTO();
		user.setUserName("");
		user.setFirstName("");
		user.setLastName("");
		user.setLastLoginTime(new Timestamp(0));
		user.setPassword("");
		user.setUserEmail("");
		user.setIsAdminFlag("");
		user.setUserStatus("");
		user.setActive(true);
		user.setOrgCode("");
		user.setOrgId("0");
		user.setOrgNodeLevel(1L);
		user.setOrgNodeLevelStr("");
		user.setFirstTimeLogin("");
		user.setUserId("0");
		user.setDisplayName("");
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		user.setRoles(roles);
		user.setSalt("");
		user.setCustomerId("0");
		user.setProduct("");
		user.setUserType("");
		user.setAdminId("");
		user.setOrgMode("");
		user.setContractName("");
		user.setDefultCustProdId(0L);
		user.setIsPasswordExpired("");
		user.setIsPasswordWarning("");
		return user;
	}

	public static ParentTO getParentTO(TestParams testParams) {
		ParentTO to = new ParentTO();
		to.setUserId(0L);
		to.setSuccessCode("");
		to.setInvitationCode(testParams.getInvitationCode());
		to.setUserName("");
		to.setDisplayName("");
		to.setFirstName("");
		to.setLastName("");
		to.setPassword("");
		to.setMail("");
		to.setMobile("");
		to.setStreet("");
		to.setCity("");
		to.setState("");
		to.setZipCode("");
		to.setCountry("");
		List<StudentTO> studentToList = new ArrayList<StudentTO>();
		studentToList.add(getStudentTO(testParams));
		to.setStudentToList(studentToList);
		to.setQuestionToList(getQuestionList(testParams));
		to.setUserName(testParams.getUserName());
		return to;
	}

	public static StudentTO getStudentTO(TestParams testParams) {
		StudentTO student = new StudentTO();
		student.setStudentName("");
		student.setGrade("");
		student.setAdministration("");
		student.setStudentTestNumber("");
		List<ParentTO> parentAccount = new ArrayList<ParentTO>();
		student.setParentAccount(parentAccount);
		student.setStructureElement("");
		student.setStudentBioId(0L);
		student.setInvitationcode(testParams.getInvitationCode());
		student.setActivationStatus("");
		student.setOrgName("");
		student.setOrgId(0L);
		student.setIcExpirationStatus("");
		student.setExpirationDate("");
		student.setTotalAvailableClaim(0L);
		student.setAdminid("0");
		student.setStudentMode("");
		student.setStudentGradeId(0L);
		student.setTestElementId("");
		student.setBioExists(0L);
		student.setSchoolName("");
		student.setIcLetterUri("");
		student.setIcLetterPath("");
		return student;
	}

	public static QuestionTO getQuestionTO() {
		QuestionTO question = new QuestionTO();
		question.setQuestionId(1L);
		question.setQuestion("");
		question.setAnswerId(1L);
		question.setAnswer("");
		question.setSno(0L);
		return question;
	}

	public static ManageContentTO getManageContentTO() {
		ManageContentTO content = new ManageContentTO();
		content.setContentId(0L);
		content.setContentName("");
		content.setSubHeader("");
		content.setGradeName("");
		content.setPerformanceLevel("");
		content.setStatusCode("");
		content.setContentDescription("");
		content.setSubObjMapId(0L);
		content.setCustProdId(0L);
		content.setContentTypeName("");
		content.setContentType("");
		content.setGradeId(0l);
		content.setSubtestId(0L);
		content.setObjectiveId(0L);
		content.setObjectiveName("");
		content.setSubtestName("");
		content.setProficiencyLevel("");
		content.setObjContentId(0L);
		content.setObjectiveDesc("");
		return content;
	}
}
