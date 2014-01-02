package com.ctb.prism.parent.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;


public interface IParentBusiness {

	public List getSecretQuestions();
	public boolean checkUserAvailability(String username);
	public boolean checkActiveUserAvailability(String username);
	public boolean isRoleAlreadyTagged(String roleId, String userName);
	
	public ParentTO validateIC(String invitationCode);
	public boolean registerUser(ParentTO parentTO) throws BusinessException;
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam);
	public ArrayList<StudentTO> getStudentList(String orgId, String adminYear, String searchParam, long customerId);
	
	public List<StudentTO> getChildrenList( String userName,String clickedTreeNode, String adminYear);
	
	
	public ArrayList <ParentTO> searchParent(String parentName, String tenantId, String adminYear,String isExactSeacrh);
	public String searchParentAutoComplete( String parentName, String tenantId, String adminYear );
	public List<StudentTO> getAssessmentList( String studentBioId );	
	public ArrayList <StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId);
	public String searchStudentAutoComplete( String studentName, String tenantId, String adminyear, long customerId );
	public ArrayList <StudentTO> searchStudentOnRedirect(String studentBioId, String tenantId, long customerId);
	
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode,
			String icExpirationStatus, String totalAvailableClaim, String expirationDate) throws Exception;
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException;

	//Added by Ravi for Manage Profile
	public ParentTO manageParentAccountDetails(String username);
	//Added by Ravi for Manage Profile
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException;
	//Added by Ravi for Claim New Invitation
	public boolean addInvitationToAccount(String userName, String invitationCode);
	
	public String getSchoolOrgId( String studentBioId );
	public ArrayList<QuestionTO> getSecurityQuestionForUser(String username);
	public boolean validateAnswers(String userName,String ans1, String ans2,String ans3,String questionId1,String questionId2,String questionId3);
	public List<UserTO> getUserNamesByEmail(String emailId);
	public boolean regenerateActivationCode(StudentTO student) throws Exception;
	
	//Manage Content - Parent Network - Start
	//Populate filters to search content
	public Map<String,Object> getManageContentFilter(final Map<String,Object> paramMap) throws BusinessException;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String,Object> paramMap) throws BusinessException;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String,Object> paramMap) throws BusinessException;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String,Object> paramMap) throws BusinessException;
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String,Object> paramMap) throws BusinessException;
	public List<ManageContentTO> loadManageContent(Map<String,Object> paramMap)throws BusinessException;
	public ManageContentTO getContentForEdit(final Map<String,Object> paramMap) throws BusinessException;
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String,Object> paramMap) throws BusinessException;
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String,Object> paramMap) throws BusinessException;
	public ManageContentTO modifyStandardForEdit(final Map<String,Object> paramMap) throws BusinessException;
	public ManageContentTO modifyGenericForEdit(final Map<String,Object> paramMap) throws BusinessException;
	//Manage Content - Parent Network - End
	
	//Parent Network - Start
	public Map<String,Object> getChildData(final Map<String,Object> paramMap) throws BusinessException;
	public List<ManageContentTO>  getArticleTypeDetails( final Map<String,Object> paramMap) throws BusinessException;
	public ManageContentTO  getArticleDescription( final Map<String,Object> paramMap) throws BusinessException;
	public List<ManageContentTO> getGradeSubtestInfo(final Map<String,Object> paramMap) throws BusinessException;
	//Parent Network - End
	
}
