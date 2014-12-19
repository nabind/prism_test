/**
 * 
 */
package com.ctb.prism.parent.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.FileUtil;
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
	
	@Autowired
	private IRepositoryService repositoryService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ParentBusinessImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getSecretQuestions()
	 */
	public List<QuestionTO> getSecretQuestions(Map<String,Object> paramMap) {
		return parentDAO.getSecretQuestions(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#checkUserAvailability(java.lang.String)
	 */
	public boolean checkUserAvailability(Map<String,Object> paramMap) {
		return parentDAO.checkUserAvailability(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#checkActiveUserAvailability(Map<String, Object>)
	 */
	public boolean checkActiveUserAvailability(Map<String, Object> paramMap)  {
		return parentDAO.checkActiveUserAvailability(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#isRoleAlreadyTagged(java.lang.String, java.lang.String)
	 */
	public boolean isRoleAlreadyTagged(String roleId, String userName) {
		return parentDAO.isRoleAlreadyTagged(roleId, userName);
	}

	/**
	 * Modified by Joy
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#validateIC(java.lang.String)
	 */
	public ParentTO validateIC(final Map<String, Object> paramMap) {
		ParentTO parentTO = null;
		parentTO = parentDAO.validateIC(paramMap);
		// Business Logic moved to another private method for JUnit code coverage
		parentTO = validateICBusinessLogic(parentTO, paramMap);
		return parentTO;
	}

	/**
	 * @param parent
	 * @return
	 */
	private ParentTO validateICBusinessLogic(ParentTO parent, Map<String, Object> paramMap) {
		if (parent == null) {
			parent = new ParentTO();
			parent.setErrorMsg("IC_INVALID");
		} else if (parent.getTotalAvailableCalim() == 0) {
			parent = new ParentTO();
			parent.setErrorMsg("IC_NOTAVAILABLE");
		} else if (IApplicationConstants.INACTIVE_FLAG.equals(parent.getIcExpirationStatus())
				|| IApplicationConstants.INACTIVE_FLAG.equals(parent.getIcActivationStatus())) {
			parent.setErrorMsg("IC_EXPIRED");
		} else if (IApplicationConstants.DELETED_FLAG.equals(parent.getIcActivationStatus())) {
			parent.setErrorMsg("IC_INVALID");
		} else if (parent.getIsAlreadyClaimed() > 0) {
			parent.setErrorMsg("IC_ALREADY_CLAIMED");
		} else {
			parent = parentDAO.getStudentForIC(paramMap);
			parent.setInvitationCode((String) paramMap.get("invitationCode"));
			parent.setErrorMsg("NA");
		}
		return parent;
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

	/* (non-Javadoc)
	 * @see com.ctb.prism.parent.business.IParentBusiness#getParentList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList<ParentTO> getParentList(String orgId, String adminYear, String searchParam, String orgMode, String moreCount) {
		return parentDAO.getParentList(orgId, adminYear, searchParam, orgMode, moreCount);

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
	public boolean addInvitationToAccount(Map<String,Object> paramMap) {
		return parentDAO.addInvitationToAccount(paramMap);
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
	 * @see com.ctb.prism.parent.business.IParentBusiness#getSecurityQuestionForUser(Map<String, Object>)
	 */
	public ArrayList<QuestionTO> getSecurityQuestionForUser(Map<String, Object> paramMap) {
		return parentDAO.getSecurityQuestionForUser(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#validateAnswers(Map<String, Object>)
	 */
	public boolean validateAnswers(Map<String, Object> paramMap) {
		return parentDAO.validateAnswers(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#getUserNamesByEmail(Map<String, Object>)
	 */
	public List<UserTO> getUserNamesByEmail(Map<String, Object> paramMap) {
		return parentDAO.getUserNamesByEmail(paramMap);
	}

	/*
	 * Method modified for modified IC upload - By Joy
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.business.IParentBusiness#regenerateActivationCode(com.ctb.prism.parent.transferobject.StudentTO)
	 */
	public boolean regenerateActivationCode(final StudentTO student) throws Exception {
		
		boolean returnFlag = parentDAO.regenerateActivationCode(student);
		logger.log(IAppLogger.INFO, "student.getIcLetterUri(): " + student.getIcLetterUri());
		logger.log(IAppLogger.INFO, "student.getIcLetterPath(): " + student.getIcLetterPath());
		
		FileOutputStream fos = null;
		InputStream is = null;
		File file = null;
		String tempPath = propertyLookup.get("pdfGenPathIC");
		try{
			tempPath = tempPath + FileUtil.getFileNameFromFilePath(student.getIcLetterPath());
			logger.log(IAppLogger.INFO, "Temporary IC PDF Path: " + tempPath);
			
			fos = new FileOutputStream(tempPath);
			URL url = new URL(student.getIcLetterUri());
			
			if(student.getIcLetterUri().startsWith("http://")){
				
				HttpURLConnection httpUrlConn = (HttpURLConnection)url.openConnection();
				logger.log(IAppLogger.INFO, "http connection created successfully");
				
				logger.log(IAppLogger.INFO, "URL about to hit");
				String contentType = httpUrlConn.getContentType();
				logger.log(IAppLogger.INFO, "URL fired");
				
				if(contentType == null){
					throw new BusinessException("FAILED.[Problem with fired URL]");
				}
				
				if (!"application/pdf".equals(contentType)) {
					throw new BusinessException("FAILED.[This is not a PDF.]");
				} else {
					is = httpUrlConn.getInputStream();
					IOUtils.copy(is, fos);
					IOUtils.closeQuietly(fos);
				}
				
			}else if(student.getIcLetterUri().startsWith("https://")){
				HttpsURLConnection httpsUrlConn = (HttpsURLConnection)url.openConnection();
				logger.log(IAppLogger.INFO, "https connection created successfully");
				
				logger.log(IAppLogger.INFO, "URL about to hit");
				String contentType = httpsUrlConn.getContentType();
				logger.log(IAppLogger.INFO, "URL fired");
				
				if(contentType == null){
					throw new BusinessException("FAILED.[Problem with fired URL]");
				}
				
				if (!"application/pdf".equals(contentType)) {
					throw new BusinessException("FAILED.[This is not a PDF.]");
				} else {
					is = httpsUrlConn.getInputStream();
					IOUtils.copy(is, fos);
					IOUtils.closeQuietly(fos);
				}
			}
			logger.log(IAppLogger.INFO, "IC PDF created at: " + tempPath);
			
			file = new File(tempPath);
			logger.log(IAppLogger.INFO, "Temporary IC PDF Created at: " + file.getAbsolutePath());
			//repositoryService.uploadAsset(student.getIcLetterPath(), is);
			repositoryService.uploadAsset(FileUtil.getDirFromFilePath(student.getIcLetterPath()), file);
			logger.log(IAppLogger.INFO, "IC PDF Uploaded at: " + student.getIcLetterPath());
		}catch(Exception e){
			returnFlag = false;
			throw new BusinessException(e.getMessage());
		}finally{
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
			if(file!=null && file.delete()){
				logger.log(IAppLogger.INFO, "Temporary IC PDF Deleted");
			}else{
				throw new BusinessException("FAILED.[Temporary IC PDF can't be deleted]");
			}
		}
		return returnFlag;
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
