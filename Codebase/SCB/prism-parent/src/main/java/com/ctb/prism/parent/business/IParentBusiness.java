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

	/**
	 * @return
	 */
	public List<QuestionTO> getSecretQuestions(Map<String,Object> paramMap);

	/**
	 * @param paramMap
	 * @return
	 */
	public boolean checkUserAvailability(Map<String,Object> paramMap);

	/**
	 * @param username
	 * @return
	 */
	public boolean checkActiveUserAvailability(Map<String, Object> paramMap); 

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
	public ParentTO validateIC(final Map<String, Object> paramMap);

	/**
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean registerUser(ParentTO parentTO) throws BusinessException;

	/**
	 * @param orgId
	 * @param adminYear
	 * @param searchParam
	 * @param orgMode
	 * @param moreCount
	 * @return
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode, String moreCount);

	/**
	 * @param paramMap
	 * @return
	 */
	public ArrayList<StudentTO> getStudentList(Map<String, Object> paramMap);

	/**
	 * @param userName
	 * @param clickedTreeNode
	 * @param adminYear
	 * @param orgMode
	 * @return
	 */
	public List<StudentTO> getChildrenList(final Map<String, Object> paramMap);

	/**
	 * @param parentName
	 * @param tenantId
	 * @param adminYear
	 * @param isExactSeacrh
	 * @param orgMode
	 * @return
	 */
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh, String orgMode);

	/**
	 * @param parentName
	 * @param tenantId
	 * @param adminYear
	 * @param orgMode
	 * @return
	 */
	public String searchParentAutoComplete(String parentName, String tenantId, String adminYear, String orgMode);

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
	public ArrayList<StudentTO> searchStudent(String studentName, String tenantId, String adminyear, long customerId, String orgMode);

	/**
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
	public boolean addInvitationToAccount(Map<String,Object> paramMap);

	/**
	 * @param studentBioId
	 * @return
	 */
	public String getSchoolOrgId(String studentBioId);

	/**
	 * @param paramMap
	 * @return
	 */
	public ArrayList<QuestionTO> getSecurityQuestionForUser(Map<String, Object> paramMap);

	/**
	 * @param paramMap
	 * @return
	 */
	public boolean validateAnswers(Map<String, Object> paramMap);

	/**
	 * @param Map<String, Object>
	 * @return
	 */
	public List<UserTO> getUserNamesByEmail(Map<String, Object> paramMap);

	/**
	 * @param student
	 * @return
	 * @throws Exception
	 */
	public boolean regenerateActivationCode(StudentTO student) throws Exception;

	/**
	 * Populate filters to search content.
	 * 
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getManageContentFilter(final Map<String, Object> paramMap) throws BusinessException;

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
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populatePerformanceLevel(final Map<String, Object> paramMap) throws BusinessException;

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
	 */
	public ManageContentTO modifyGenericForEdit(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getChildData(final Map<String, Object> paramMap) throws BusinessException;

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
