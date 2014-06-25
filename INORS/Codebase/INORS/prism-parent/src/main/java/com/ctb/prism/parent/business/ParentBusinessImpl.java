/**
 * 
 */
package com.ctb.prism.parent.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.dao.ILoginDAO;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.dao.IParentDAO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

/**
 * @author TCS
 * 
 */

@Component("parentBusiness")
public class ParentBusinessImpl implements IParentBusiness {

	@Autowired
	private IParentDAO parentDAO;

	@Autowired
	private ILoginDAO loginDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getSecretQuestions()
	 */
	public List<QuestionTO> getSecretQuestions() {
		return parentDAO.getSecretQuestions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(String username) {
		return parentDAO.checkUserAvailability(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#checkActiveUserAvailability(java.lang.String)
	 */
	public boolean checkActiveUserAvailability(String username) {
		return parentDAO.checkActiveUserAvailability(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#isRoleAlreadyTagged(java.lang.String, java.lang.String)
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName) {
		return parentDAO.isRoleAlreadyTagged(roleId, userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#validateIC(java.lang.String)
	 */
	public ParentTO validateIC(final Map<String, Object> paramMap) {
		
		String invitationCode = (String)paramMap.get("invitationCode");
		
		ParentTO parentTO = parentDAO.validateIC(paramMap);

		if (parentTO == null) {
			parentTO = new ParentTO();
			parentTO.setErrorMsg("IC_INVALID");
		} else if (parentTO.getTotalAvailableCalim() == 0) {
			parentTO = new ParentTO();
			parentTO.setErrorMsg("IC_NOTAVAILABLE");
		} else if (IApplicationConstants.INACTIVE_FLAG.equals(parentTO.getIcExpirationStatus()) || IApplicationConstants.INACTIVE_FLAG.equals(parentTO.getIcActivationStatus())) {
			parentTO.setErrorMsg("IC_EXPIRED");
		} else if (IApplicationConstants.DELETED_FLAG.equals(parentTO.getIcActivationStatus())) {
			parentTO.setErrorMsg("IC_INVALID");
		} else if(parentTO.getIsAlreadyClaimed() > 0){
			parentTO.setErrorMsg("IC_ALREADY_CLAIMED");
		} else {
			parentTO = new ParentTO();
			parentTO = parentDAO.getStudentForIC(invitationCode);
			parentTO.setInvitationCode(invitationCode);
			parentTO.setErrorMsg("NA");
		}
		return parentTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#registerUser(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	public boolean registerUser(ParentTO parentTO) throws BusinessException {
		return parentDAO.registerUser(parentTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getChildrenList(java.lang.String, java.lang.String, java.lang.String, ava.lang.String)
	 */
	public List<StudentTO> getChildrenList(final Map<String, Object> paramMap) {
		return parentDAO.getChildrenList(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getParentList(java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode) {
		return parentDAO.getParentList(orgId, adminYear, searchParam, orgMode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getStudentList(Map<String, Object> paramMap)
	 */
	public ArrayList<StudentTO> getStudentList(Map<String, Object> paramMap) {
		return parentDAO.getStudentList(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#searchParent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh, String orgMode) {
		return parentDAO.searchParent(parentName, tenantId, adminYear, isExactSeacrh, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#searchParentAutoComplete(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String searchParentAutoComplete(String parentName, String tenantId, String adminYear, String orgMode) {
		return parentDAO.searchParentAutoComplete(parentName, tenantId, adminYear, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getAssessmentList(java.lang.String)
	 */
	public List<StudentTO> getAssessmentList(String studentBioId) {
		return parentDAO.getAssessmentList(studentBioId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#searchStudent(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public ArrayList<StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId, String orgMode) {
		return parentDAO.searchStudent(studentName, tenantId, adminyear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#searchStudentAutoComplete(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public String searchStudentAutoComplete(String studentName, String tenantId, String adminyear, long customerId, String orgMode) {
		return parentDAO.searchStudentAutoComplete(studentName, tenantId, adminyear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#searchStudentOnRedirect(java.util.Map)
	 */
	public ArrayList<StudentTO> searchStudentOnRedirect(Map<String, Object> paramMap) {
		return parentDAO.searchStudentOnRedirect(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#updateAssessmentDetails(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode, String icExpirationStatus, String totalAvailableClaim, String expirationDate)
			throws Exception {
		return parentDAO.updateAssessmentDetails(studentBioId, administration, invitationcode, icExpirationStatus, totalAvailableClaim, expirationDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#firstTimeUserLogin(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException {
		return parentDAO.firstTimeUserLogin(parentTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#manageParentAccountDetails(java.lang.String)
	 */
	public ParentTO manageParentAccountDetails(String username) {
		return parentDAO.manageParentAccountDetails(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#updateUserProfile(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException {
		return parentDAO.updateUserProfile(parentTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#addInvitationToAccount(java.lang.String, java.lang.String)
	 */
	public boolean addInvitationToAccount(String userName, String invitationCode) {
		if (!parentDAO.checkInvitationCodeClaim(userName, invitationCode)) {
			return parentDAO.addInvitationToAccount(userName, invitationCode);
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getSchoolOrgId(java.lang.String)
	 */
	public String getSchoolOrgId(String studentBioId) {
		return parentDAO.getSchoolOrgId(studentBioId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getSecurityQuestionForUser(java.lang.String)
	 */
	public ArrayList<QuestionTO> getSecurityQuestionForUser(String username) {
		return parentDAO.getSecurityQuestionForUser(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#validateAnswers(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean validateAnswers(String userName, String ans1, String ans2, String ans3, String questionId1, String questionId2, String questionId3) {
		return parentDAO.validateAnswers(userName, ans1, ans2, ans3, questionId1, questionId2, questionId3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getUserNamesByEmail(java.lang.String)
	 */
	public List<UserTO> getUserNamesByEmail(String emailId) {
		return parentDAO.getUserNamesByEmail(emailId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#regenerateActivationCode(com.ctb.prism.parent.transferobject.StudentTO)
	 */
	public boolean regenerateActivationCode(StudentTO student) throws Exception {
		return parentDAO.regenerateActivationCode(student);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getManageContentFilter(java.util.Map)
	 */
	public Map<String, Object> getManageContentFilter(Map<String, Object> paramMap) throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = loginDAO.getCustomerProduct(paramMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("customerProductList", customerProductList);
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#populateGrade(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.populateGrade(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#populateSubtest(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.populateSubtest(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#populateObjective(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.populateObjective(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#addNewContent(java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.addNewContent(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#loadManageContent(java.util.Map)
	 */
	public List<ManageContentTO> loadManageContent(Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.loadManageContent(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getContentForEdit(java.util.Map)
	 */
	public ManageContentTO getContentForEdit(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.getContentForEdit(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#updateContent(java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.updateContent(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#deleteContent(java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.deleteContent(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#modifyGenericForEdit(java.util.Map)
	 */
	public ManageContentTO modifyGenericForEdit(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.modifyGenericForEdit(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getChildData(java.util.Map)
	 */
	public Map<String, Object> getChildData(final Map<String, Object> paramMap) throws BusinessException {
		String studentOverviewMessage = loginDAO.getSystemConfigurationMessage(paramMap);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> studentSubtest = parentDAO.getStudentSubtest(paramMap);

		Map<String, Object> childDataMap = new HashMap<String, Object>();
		childDataMap.put("studentOverviewMessage", studentOverviewMessage);
		childDataMap.put("studentSubtest", studentSubtest);
		return childDataMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getArticleTypeDetails(java.util.Map)
	 */
	public List<ManageContentTO> getArticleTypeDetails(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.getArticleTypeDetails(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getArticleDescription(java.util.Map)
	 */
	public ManageContentTO getArticleDescription(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.getArticleDescription(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getGradeSubtestInfo(java.util.Map)
	 */
	public List<ManageContentTO> getGradeSubtestInfo(final Map<String, Object> paramMap) throws BusinessException {
		return parentDAO.getGradeSubtestInfo(paramMap);
	}
}
