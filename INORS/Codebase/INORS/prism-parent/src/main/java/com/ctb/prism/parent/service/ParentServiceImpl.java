package com.ctb.prism.parent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.business.IParentBusiness;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

@Service("parentService")
public class ParentServiceImpl implements IParentService {

	@Autowired
	private IParentBusiness parentBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getSecretQuestions()
	 */
	public List<QuestionTO> getSecretQuestions(Map<String,Object> paramMap) {
		return parentBusiness.getSecretQuestions(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(Map<String,Object> paramMap) {
		return parentBusiness.checkUserAvailability(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#checkActiveUserAvailability(Map<String, Object>)
	 */
	public boolean checkActiveUserAvailability(Map<String, Object> paramMap) {
		return parentBusiness.checkActiveUserAvailability(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#isRoleAlreadyTagged(java.lang.String, java.lang.String)
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName) {
		return parentBusiness.isRoleAlreadyTagged(roleId, userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#validateIC(java.lang.String)
	 */
	public ParentTO validateIC(final Map<String, Object> paramMap) {
		return parentBusiness.validateIC(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#registerUser(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean registerUser(ParentTO parentTO) throws BusinessException {
		return parentBusiness.registerUser(parentTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getChildrenList(java.lang.String, java.lang.String, java.lang.String,, java.lang.String)
	 */
	public List<StudentTO> getChildrenList(final Map<String, Object> paramMap) {
		return parentBusiness.getChildrenList(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getParentList(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode) {
		return parentBusiness.getParentList(orgId, adminYear, searchParam, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getStudentList(Map<String, Object> paramMap)
	 */
	public ArrayList<StudentTO> getStudentList(Map<String, Object> paramMap) {
		return parentBusiness.getStudentList(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#searchParent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh, String orgMode) {
		return parentBusiness.searchParent(parentName, tenantId, adminYear, isExactSeacrh, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#searchParentAutoComplete(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String searchParentAutoComplete(String parentName, String tenantId, String adminYear, String orgMode) {
		return parentBusiness.searchParentAutoComplete(parentName, tenantId, adminYear, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getAssessmentList(java.lang.String)
	 */
	public List<StudentTO> getAssessmentList(String studentBioId) {
		return parentBusiness.getAssessmentList(studentBioId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#searchStudent(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public ArrayList<StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId, String orgMode) {
		return parentBusiness.searchStudent(studentName, tenantId, adminyear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#searchStudentAutoComplete(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public String searchStudentAutoComplete(String studentName, String tenantId, String adminyear, long customerId, String orgMode) {
		return parentBusiness.searchStudentAutoComplete(studentName, tenantId, adminyear, customerId, orgMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#searchStudentOnRedirect(java.util.Map)
	 */
	public ArrayList<StudentTO> searchStudentOnRedirect(Map<String, Object> paramMap) {
		return parentBusiness.searchStudentOnRedirect(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#updateAssessmentDetails(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode, String icExpirationStatus, String totalAvailableClaim, String expirationDate)
			throws Exception {
		return parentBusiness.updateAssessmentDetails(studentBioId, administration, invitationcode, icExpirationStatus, totalAvailableClaim, expirationDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#firstTimeUserLogin(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException {
		return parentBusiness.firstTimeUserLogin(parentTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#manageParentAccountDetails(java.lang.String)
	 */
	public ParentTO manageParentAccountDetails(String username) {
		return parentBusiness.manageParentAccountDetails(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#updateUserProfile(com.ctb.prism.parent.transferobject.ParentTO)
	 */
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException {
		return parentBusiness.updateUserProfile(parentTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#addInvitationToAccount(java.lang.String, java.lang.String)
	 */
	public boolean addInvitationToAccount(String userName, String invitationCode) {
		return parentBusiness.addInvitationToAccount(userName, invitationCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getSchoolOrgId(java.lang.String)
	 */
	public String getSchoolOrgId(String studentBioId) {
		return parentBusiness.getSchoolOrgId(studentBioId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getSecurityQuestionForUser(Map<String, Object>)
	 */
	public ArrayList<QuestionTO> getSecurityQuestionForUser(Map<String, Object> paramMap){
		return parentBusiness.getSecurityQuestionForUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#validateAnswers(Map<String, Object>)
	 */
	public boolean validateAnswers(Map<String, Object> paramMap) {
		return parentBusiness.validateAnswers(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getUserNamesByEmail(Map<String, Object>)
	 */
	public List<UserTO> getUserNamesByEmail(Map<String, Object> paramMap) {
		return parentBusiness.getUserNamesByEmail(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#regenerateActivationCode(com.ctb.prism.parent.transferobject.StudentTO)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean regenerateActivationCode(StudentTO student) throws Exception {
		return parentBusiness.regenerateActivationCode(student);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.service.IParentService#getManageContentFilter(java.util.Map)
	 */
	public Map<String, Object> getManageContentFilter(Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getManageContentFilter(paramMap);
	}

	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.populateGrade(paramMap);
	}

	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.populateSubtest(paramMap);
	}

	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.populateObjective(paramMap);
	}

	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.addNewContent(paramMap);
	}

	public List<ManageContentTO> loadManageContent(Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.loadManageContent(paramMap);
	}

	public ManageContentTO getContentForEdit(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getContentForEdit(paramMap);
	}

	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.updateContent(paramMap);
	}

	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.deleteContent(paramMap);
	}

	public ManageContentTO modifyGenericForEdit(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.modifyGenericForEdit(paramMap);
	}

	public Map<String, Object> getChildData(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getChildData(paramMap);
	}

	public List<ManageContentTO> getArticleTypeDetails(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getArticleTypeDetails(paramMap);
	}

	public ManageContentTO getArticleDescription(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getArticleDescription(paramMap);
	}

	public List<ManageContentTO> getGradeSubtestInfo(final Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getGradeSubtestInfo(paramMap);
	}

}
