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
import com.ctb.prism.core.transferobject.ObjectValueTO;
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
	
	public List getSecretQuestions() {
		return parentDAO.getSecretQuestions();
	}

	public boolean checkUserAvailability(String username) {
		return parentDAO.checkUserAvailability(username);
	}
	public boolean checkActiveUserAvailability(String username) {
		return parentDAO.checkActiveUserAvailability(username);
	}
	public boolean isRoleAlreadyTagged(String roleId, String userName){
		return parentDAO.isRoleAlreadyTagged(roleId,userName);
	}

	public ParentTO validateIC(String invitationCode) {
		ParentTO parentTO = parentDAO.validateIC(invitationCode);
		
		if(parentTO == null) {
			parentTO = new ParentTO();
			parentTO.setErrorMsg("IC_INVALID");
		} else if(parentTO.getTotalAvailableCalim() <= parentTO.getTotalAttemptedCalim()) {
			parentTO = new ParentTO();
			parentTO.setErrorMsg("IC_NOTAVAILABLE");
		} else if (IApplicationConstants.INACTIVE_FLAG.equals(parentTO.getIcExpirationStatus()) || IApplicationConstants.INACTIVE_FLAG.equals(parentTO.getIcActivationStatus())) {
			parentTO.setErrorMsg("IC_EXPIRED");
		}else if (IApplicationConstants.DELETED_FLAG.equals(parentTO.getIcActivationStatus())) {
			parentTO.setErrorMsg("IC_INVALID");
		} else {
			parentTO = new ParentTO();
			parentTO = parentDAO.getStudentForIC(invitationCode);
			parentTO.setInvitationCode(invitationCode);
			parentTO.setErrorMsg("NA");
		}
		return parentTO;
	}

	public boolean registerUser(ParentTO parentTO) throws BusinessException {
		return parentDAO.registerUser(parentTO);
	}
	

	public List<StudentTO> getChildrenList( String userName,String clickedTreeNode, String adminYear ) {
		return parentDAO.getChildrenList( userName,clickedTreeNode, adminYear );
	}
	
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam) {
		return parentDAO.getParentList(orgId, adminYear, searchParam);
	
	}
	
	public ArrayList<StudentTO> getStudentList(String orgId, String adminYear, String searchParam, long customerId) {

		return parentDAO.getStudentList(orgId, adminYear, searchParam, customerId);
	}
	
	public ArrayList <ParentTO> searchParent(String parentName, String tenantId, String adminYear,String isExactSeacrh){
		return parentDAO.searchParent( parentName,  tenantId, adminYear,isExactSeacrh);
	}
	
	public String searchParentAutoComplete( String parentName, String tenantId, String adminYear ) {
		return parentDAO.searchParentAutoComplete( parentName, tenantId, adminYear );
	}
	public List<StudentTO> getAssessmentList( String studentBioId ){
		return parentDAO.getAssessmentList( studentBioId );
	}	
	
	public ArrayList <StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId){
		return parentDAO.searchStudent( studentName,  tenantId, adminyear, customerId);
	}
	
	public String searchStudentAutoComplete( String studentName, String tenantId, String adminyear,long customerId ) {
		return parentDAO.searchStudentAutoComplete( studentName, tenantId, adminyear,customerId );
	}
	
	public ArrayList <StudentTO> searchStudentOnRedirect(String studentBioId, String tenantId, long customerId)
	{
		return parentDAO.searchStudentOnRedirect(studentBioId,tenantId,customerId);
	}	
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode,
			String icExpirationStatus, String totalAvailableClaim,String expirationDate) throws Exception {

		return parentDAO.updateAssessmentDetails(studentBioId, administration, invitationcode,
				icExpirationStatus, totalAvailableClaim, expirationDate);
	}

	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException {
		return parentDAO.firstTimeUserLogin(parentTO);
	}

	//Added by Ravi for Manage Profile
	public ParentTO manageParentAccountDetails(String username){
		return parentDAO.manageParentAccountDetails( username );
	}
	//Added by Ravi for Manage Profile
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException {
		return parentDAO.updateUserProfile(parentTO);
	}
	//Added by Ravi for Claim New Invitation
	public boolean addInvitationToAccount(String userName, String invitationCode) {
		if(!parentDAO.checkInvitationCodeClaim(userName, invitationCode)) {
			return parentDAO.addInvitationToAccount(userName, invitationCode);
		}
		return Boolean.FALSE;
	}
	
	public String getSchoolOrgId( String studentBioId ) {
		return parentDAO.getSchoolOrgId(studentBioId);
	}
	public ArrayList<QuestionTO> getSecurityQuestionForUser( String username ) {
		return parentDAO.getSecurityQuestionForUser(username);
	}
	public boolean validateAnswers(String userName,String ans1, String ans2,String ans3,String questionId1,String questionId2,String questionId3){
		return parentDAO.validateAnswers(userName,ans1, ans2,ans3,questionId1,questionId2,questionId3);
	}
	public List<UserTO> getUserNamesByEmail(String emailId)
	{
		return parentDAO.getUserNamesByEmail(emailId);
	}
	
	public boolean regenerateActivationCode(StudentTO student) throws Exception {
		if(parentDAO.generateActivationCode(student)) {
			return parentDAO.disableActivationCode(student);
		} else {
			return false;
		}
	}
	
	//Manage Content - Parent Network - Start
	//Populate filters to search content
	public Map<String, Object> getManageContentFilter(Map<String, Object> paramMap) throws BusinessException {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> customerProductList = loginDAO.getCustomerProduct(paramMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("customerProductList", customerProductList);
		return returnMap;
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String,Object> paramMap) throws BusinessException{
		return parentDAO.populateGrade(paramMap);
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String,Object> paramMap) throws BusinessException{
		return parentDAO.populateSubtest(paramMap);
	}
	
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String,Object> paramMap) throws BusinessException{
		return parentDAO.populateObjective(paramMap);
	}
	
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String,Object> paramMap) throws BusinessException{
		return parentDAO.addNewContent(paramMap);
	}
	
	public List<ManageContentTO> loadManageContent(Map<String,Object> paramMap) throws BusinessException {
		return parentDAO.loadManageContent(paramMap);
	}
	
	public ManageContentTO getContentForEdit(final Map<String,Object> paramMap) throws BusinessException {
		return parentDAO.getContentForEdit(paramMap);
	}
	
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String,Object> paramMap) throws BusinessException {
		return parentDAO.updateContent(paramMap);
	}
	
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String,Object> paramMap) throws BusinessException {
		return parentDAO.deleteContent(paramMap);
	}
	
	public ManageContentTO modifyStandardForEdit(final Map<String,Object> paramMap) throws BusinessException {
		return parentDAO.modifyStandardForEdit(paramMap);
	}
	//Manage Content - Parent Network - End
	
	//Parent Network - Start
	public Map<String,Object> getChildData(final Map<String,Object> paramMap) throws BusinessException{
		String studentOverviewMessage = loginDAO.getSystemConfigurationMessage(paramMap);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> studentSubtest = parentDAO.getStudentSubtest(paramMap);
		
		Map<String,Object> childDataMap = new HashMap<String, Object>();
		childDataMap.put("studentOverviewMessage", studentOverviewMessage);
		childDataMap.put("studentSubtest", studentSubtest);
		return childDataMap;
	}
	
	//Parent Network - End
}
