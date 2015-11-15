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
	 * Fetch all questions.
	 * 
	 * @return
	 */
	public List<QuestionTO> getSecretQuestions(Map<String,Object> paramMap);

	/**
	 * Check provided user availability.
	 * 
	 * @param paramMap
	 * @return
	 */
	public boolean checkUserAvailability(Map<String,Object> paramMap);

	/**
	 * Check provided user and activation status for forgot password.
	 * 
	 * @param Map<String, Object>
	 * @return
	 */
	public boolean checkActiveUserAvailability(Map<String, Object> paramMap);

	/**
	 * Check whether role is already tagged to this user.
	 * 
	 * @param roleId
	 * @param userName
	 * @return
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName);

	/**
	 * Fetch all Students for invitation code.
	 * 
	 * @param invitationCode
	 * @return
	 */
	public ParentTO getStudentForIC(final Map<String, Object> paramMap);

	/**
	 * Check provided invitation code availability.
	 * 
	 * @param invitationCode
	 * @return
	 */
	public ParentTO validateIC(final Map<String, Object> paramMap);

	/**
	 * Save parent user information.
	 * 
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
	 * @param orgMode
	 * @param moreCount
	 * @return
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode, final String moreCount);

	/**
	 * @param paramMap
	 * @return
	 */
	public ArrayList<StudentTO> getStudentList(Map<String, Object> paramMap);

	/**
	 * Retrieves the list of the children of the logged in parent. This information is displayed in the home page of the parent login.
	 * 
	 * @param userName
	 *            User Name of the logged in parent Updated by Joy on 23-DEC-2013
	 * @param clickedTreeNode
	 * @param adminYear
	 * @return
	 */
	public List<StudentTO> getChildrenList(final Map<String, Object> paramMap);

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
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh, String orgMode);

	/**
	 * Searches and returns the parent(s) with given name (like operator). Performs case insensitive searching.
	 * 
	 * @param parentName
	 *            Search String treated as parent name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @param adminYear
	 * @return
	 */
	public String searchParentAutoComplete(String parentName, String tenantId, String adminYear, String orgMode);

	/**
	 * Retrieves the list of the children of the logged in parent. This information is displayed in the home page of the parent login.
	 * 
	 * @param studentBioId
	 * @return
	 */
	public List<StudentTO> getAssessmentList(String studentBioId);

	/**
	 * Searches and returns the student names(use like operator) as a JSON string. Performs case insensitive searching. This method is used to perform auto complete in search box.
	 * 
	 * @param studentName
	 *            Search String treated as student name
	 * @param tenantId
	 *            parentId of the logged in user
	 * @param adminyear
	 * @param customerId
	 * @return
	 */
	public ArrayList<StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId, String orgMode);

	/**
	 * Searches and returns the students(s) with given name (like operator). Performs case insensitive searching.
	 * 
	 * @param studentName
	 * @param tenantId
	 * @param adminyear
	 * @param customerId
	 * @return
	 */
	public String searchStudentAutoComplete(String studentName, String tenantId, String adminyear, long customerId, String orgMode);

	/**
	 * @param paramMap
	 * @return
	 */
	public ArrayList<StudentTO> searchStudentOnRedirect(Map<String, Object> paramMap);

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
	 * Save First Time user profile &(ChangePAssword).
	 * 
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException;

	/**
	 * Get parent account details details for manage
	 * 
	 * @param username
	 * @return
	 */
	public ParentTO manageParentAccountDetails(String username);

	/**
	 * Update user profile information on save.
	 * 
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateUserProfile(ParentTO parentTO) throws BusinessException;

	/**
	 * Add invitation code to existing parent account Need to store org_user_id instead of userid Modified By Joy.
	 * 
	 * @param userName
	 * @param invitationCode
	 * @return
	 */
	public boolean addInvitationToAccount(Map<String,Object> paramMap);

	/**
	 * Retrieves and returns school id (leve3_jasper_orgid) for given student bio id.
	 * 
	 * @param studentBioId
	 * @return
	 */
	public String getSchoolOrgId(String studentBioId);

	/**
	 * Get parent secret questions details for Forget password.
	 * 
	 * @param paramMap
	 * @return
	 */
	public ArrayList<QuestionTO> getSecurityQuestionForUser(Map<String, Object> paramMap);

	/**
	 * This function validate the user secret answers.
	 * 
	 * @param paramMap
	 * @return
	 */
	public boolean validateAnswers(Map<String, Object> paramMap);

	/**
	 * This function retrieves the user names for a particular email id.
	 * 
	 * @param Map<String, Object>
	 * @return
	 */
	public List<UserTO> getUserNamesByEmail(Map<String, Object> paramMap);

	/**
	 * To populate grade.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGrade(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * To populate subtest.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSubtest(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * To populate Objective.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateObjective(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Insert content/article along with metadata
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO addNewContent(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Returns content list respective of the search field selected
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<ManageContentTO> loadManageContent(Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Get content details for edit depending upon article_metedata id
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ManageContentTO getContentForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Update content/article along with metadata
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO updateContent(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Delete content/article's meta data and delete content/article if no association present with another mete data.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO deleteContent(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * As Standard/Objective is dependent upon Test Administration, so the code is blocked by Joy Remove it after testing
	 * 
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 * 
	 */
	// public ManageContentTO modifyStandardForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Get Description of Standard/Objective, Resource, Everyday Activity and About the Test
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ManageContentTO modifyGenericForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Get Student's sub test.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getStudentSubtest(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Get Standard and associated activity/indicator. It may or may not depends upon Student.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<ManageContentTO> getArticleTypeDetails(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Get Article description depending upon article id and type/category.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public ManageContentTO getArticleDescription(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * Get grade and associated subtest depends upon logged in customerId for current admin year.
	 * 
	 * @author Joy
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public List<ManageContentTO> getGradeSubtestInfo(final Map<String, Object> paramMap) throws BusinessException;
	
	public boolean regenerateActivationCode(final StudentTO student) throws Exception;
}
