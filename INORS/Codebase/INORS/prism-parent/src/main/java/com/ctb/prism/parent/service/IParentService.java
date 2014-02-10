package com.ctb.prism.parent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;
import com.ctb.prism.parent.transferobject.StudentTO;

public interface IParentService {

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
	public ParentTO validateIC(String invitationCode);

	/**
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean registerUser(ParentTO parentTO) throws BusinessException;

	/**
	 * @param userName
	 * @param clickedTreeNode
	 * @param adminYear
	 * @return
	 */
	public List<StudentTO> getChildrenList(String userName, String clickedTreeNode, String adminYear);

	/**
	 * @param orgId
	 * @param adminYear
	 * @param searchParam
	 * @return
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode);

	/**
	 * @param orgId
	 * @param adminYear
	 * @param searchParam
	 * @param customerId
	 * @return
	 */
	public ArrayList<StudentTO> getStudentList(String orgId, String adminYear, String searchParam, long customerId);

	/**
	 * @param parentName
	 * @param tenantId
	 * @param adminYear
	 * @param isExactSeacrh
	 * @return
	 */
	public ArrayList<ParentTO> searchParent(String parentName, String tenantId, String adminYear, String isExactSeacrh, String orgMode);

	/**
	 * @param parentName
	 * @param tenantId
	 * @param adminYear
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
	 * @param parentTO
	 * @return
	 * @throws BusinessException
	 */
	public boolean firstTimeUserLogin(ParentTO parentTO) throws BusinessException;

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
