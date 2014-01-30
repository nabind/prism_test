package com.ctb.prism.parent.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

/**
 * @author TCS
 * 
 */
public interface IParentDAO {

	/**
	 * @return
	 */
	public List getSecretQuestions();

	/**
	 * @param username
	 * @return
	 */
	public boolean checkUserAvailability(String username);

	/**
	 * @param username
	 * @return
	 */
	public boolean checkActiveUserAvailability(String username);

	/**
	 * @param roleId
	 * @param userName
	 * @return
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName);

	/**
	 * @param invitationCode
	 * @return
	 */
	public ParentTO getStudentForIC(String invitationCode);

	/**
	 * @param invitationCode
	 * @return
	 */
	public ParentTO validateIC(String invitationCode);

	/**
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean registerUser(ParentTO parentTO) throws BusinessException;

	/**
	 * Returns the parentList on load.
	 * 
	 * @param orgId
	 * @param adminYear
	 * @param searchParam
	 * @return
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam);

	/**
	 * @param orgId
	 * @param adminYear
	 * @param searchParam
	 * @param customerId
	 * @return
	 */
	public ArrayList<StudentTO> getStudentList(String orgId, String adminYear, String searchParam, long customerId);

	/**
	 * @param userName
	 * @param clickedTreeNode
	 * @param adminYear
	 * @return
	 */
	public List<StudentTO> getChildrenList(String userName, String clickedTreeNode, String adminYear);

	/**
	 * Searches and returns the parent names(use like operator) as a JSON string. Performs case insensitive searching. This method is used to perform auto complete in search box.
	 * 
	 * @param parentName
	 *            Search String treated as parent name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @param adminYear
	 * @param isExactSeacrh
	 * @return
	 */
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh);

	/**
	 * @param parentName
	 * @param tenantId
	 * @param adminYear
	 * @return
	 */
	public String searchParentAutoComplete(String parentName, String tenantId, String adminYear);

	/**
	 * @param studentBioId
	 * @return
	 */
	public List<StudentTO> getAssessmentList(String studentBioId);

	/**
	 * @param studentName
	 * @param tenantId
	 * @param adminyear
	 * @param customerId
	 * @return
	 */
	public ArrayList<StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId);

	/**
	 * @param studentName
	 * @param tenantId
	 * @param adminyear
	 * @param customerId
	 * @return
	 */
	public String searchStudentAutoComplete(String studentName, String tenantId, String adminyear, long customerId);

	/**
	 * @param studentBioId
	 * @param tenantId
	 * @param customerId
	 * @return
	 */
	public ArrayList<StudentTO> searchStudentOnRedirect(String studentBioId, String tenantId, long customerId);

	/**
	 * @param studentBioId
	 * @param administration
	 * @param invitationcode
	 * @param icExpirationStatus
	 * @param totalAvailableClaim
	 * @param expirationDate
	 * @return
	 * @throws Exception
	 */
	public boolean updateAssessmentDetails(String studentBioId, String administration, String invitationcode, String icExpirationStatus, String totalAvailableClaim, String expirationDate)
			throws Exception;

	/**
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException;

	/**
	 * @param username
	 * @return
	 */
	public ParentTO manageParentAccountDetails(String username);

	/**
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException;

	/**
	 * @param userName
	 * @param invitationCode
	 * @return
	 */
	public boolean checkInvitationCodeClaim(String userName, String invitationCode);

	/**
	 * @param userName
	 * @param invitationCode
	 * @return
	 */
	public boolean addInvitationToAccount(String userName, String invitationCode);

	/**
	 * @param studentBioId
	 * @return
	 */
	public String getSchoolOrgId(String studentBioId);

	/**
	 * @param username
	 * @return
	 */
	public ArrayList<QuestionTO> getSecurityQuestionForUser(String username);

	/**
	 * @param userName
	 * @param ans1
	 * @param ans2
	 * @param ans3
	 * @param questionId1
	 * @param questionId2
	 * @param questionId3
	 * @return
	 */
	public boolean validateAnswers(String userName, String ans1, String ans2, String ans3, String questionId1, String questionId2, String questionId3);

	/**
	 * @param emailId
	 * @return
	 */
	public List<UserTO> getUserNamesByEmail(String emailId);

	/**
	 * @param student
	 * @return
	 * @throws Exception
	 */
	public boolean generateActivationCode(StudentTO student) throws Exception;

	/**
	 * @param student
	 * @return
	 * @throws Exception
	 */
	public boolean disableActivationCode(StudentTO student) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<ManageContentTO> loadManageContent(Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ManageContentTO getContentForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 * As Standard/Objective is dependent upon Test Administration, so the code is blocked by Joy
	 * Remove it after testing
	 */
	//public ManageContentTO modifyStandardForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ManageContentTO modifyGenericForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getStudentSubtest(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<ManageContentTO> getArticleTypeDetails(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ManageContentTO getArticleDescription(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<ManageContentTO> getGradeSubtestInfo(final Map<String, Object> paramMap) throws BusinessException;
}
