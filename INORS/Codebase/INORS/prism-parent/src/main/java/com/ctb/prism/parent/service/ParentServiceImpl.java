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
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;


@Service("parentService")
public class ParentServiceImpl implements IParentService {
	
	@Autowired
	private IParentBusiness parentBusiness;

	public List getSecretQuestions() {
		return parentBusiness.getSecretQuestions();
	}

	public boolean checkUserAvailability(String username) {
		return parentBusiness.checkUserAvailability(username);
	}
	public boolean checkActiveUserAvailability(String username) {
		return parentBusiness.checkActiveUserAvailability(username);
	}
	public boolean isRoleAlreadyTagged(String roleId, String userName){
		return parentBusiness.isRoleAlreadyTagged(roleId,userName);
	}

	public ParentTO validateIC(String invitationCode) {
		return parentBusiness.validateIC(invitationCode);
	}

	public boolean registerUser(ParentTO parentTO) throws BusinessException {
		return parentBusiness.registerUser(parentTO);
	}
	
	public List<StudentTO> getChildrenList( String userName,String clickedTreeNode, String adminYear ) {
		return parentBusiness.getChildrenList( userName,clickedTreeNode, adminYear );
	}
	
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam) {

		return parentBusiness.getParentList(orgId, adminYear, searchParam);
	
		
	}
	
	public ArrayList<StudentTO> getStudentList(String orgId, String adminYear, String searchParam, long customerId) {

		return parentBusiness.getStudentList(orgId, adminYear, searchParam, customerId);
	
		
	}
	
	
	
	public ArrayList <ParentTO> searchParent(String parentName, String tenantId, String adminYear,String isExactSeacrh){
		return parentBusiness.searchParent( parentName,  tenantId, adminYear,isExactSeacrh);
	}
	
	public String searchParentAutoComplete( String parentName, String tenantId, String adminYear ) {
		return parentBusiness.searchParentAutoComplete( parentName, tenantId, adminYear );
	}

	public List<StudentTO> getAssessmentList( String studentBioId ){
		return parentBusiness.getAssessmentList( studentBioId );
	}
	
	public ArrayList <StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId){
		return parentBusiness.searchStudent( studentName,  tenantId, adminyear, customerId);
	}
	
	public String searchStudentAutoComplete( String studentName, String tenantId, String adminyear,long customerId ) {
		return parentBusiness.searchStudentAutoComplete( studentName, tenantId, adminyear, customerId );
	}
	
	public ArrayList <StudentTO> searchStudentOnRedirect(String studentBioId, String tenantId, long customerId)
	{
		return parentBusiness.searchStudentOnRedirect(studentBioId,tenantId,customerId);
	}

	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode,
			String icExpirationStatus, String totalAvailableClaim,String expirationDate) throws Exception {

		return parentBusiness.updateAssessmentDetails(studentBioId, administration, invitationcode,
				icExpirationStatus, totalAvailableClaim, expirationDate);
	}
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException {
		return parentBusiness.firstTimeUserLogin(parentTO);
	}

	//Added by Ravi for Manage Profile
	public ParentTO manageParentAccountDetails(String username){
		return parentBusiness.manageParentAccountDetails(username);
	}
	//Added by Ravi for Manage Profile
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException {
		return parentBusiness.updateUserProfile(parentTO);
	}
	//Added by Ravi for Claim New Invitation
	public boolean addInvitationToAccount(String userName, String invitationCode){
		return parentBusiness.addInvitationToAccount(userName, invitationCode);
	}
	
	public String getSchoolOrgId( String studentBioId ) {
		return parentBusiness.getSchoolOrgId(studentBioId);
	}
	
	public ArrayList<QuestionTO> getSecurityQuestionForUser( String username ) {
		return parentBusiness.getSecurityQuestionForUser(username);
	}
	
	public boolean validateAnswers(String userName,String ans1, String ans2,String ans3,String questionId1,String questionId2,String questionId3){
		return parentBusiness.validateAnswers(userName,ans1, ans2,ans3,questionId1,questionId2,questionId3);
	}
	public List<UserTO> getUserNamesByEmail(String emailId)
	{
		return parentBusiness.getUserNamesByEmail(emailId);
	}
	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean regenerateActivationCode(StudentTO student) throws Exception {
		return parentBusiness.regenerateActivationCode(student);
	}

	//Manage Content - Parent Network - Start	
	//Populate filters to search content
	public Map<String, Object> getManageContentFilter(Map<String, Object> paramMap) throws BusinessException {
		return parentBusiness.getManageContentFilter(paramMap);
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String,Object> paramMap) throws BusinessException{
		return parentBusiness.populateGrade(paramMap);
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String,Object> paramMap) throws BusinessException{
		return parentBusiness.populateSubtest(paramMap);
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String,Object> paramMap) throws BusinessException{
		return parentBusiness.populateObjective(paramMap);
	}

	//Manage Content - Parent Network - End
	
}
