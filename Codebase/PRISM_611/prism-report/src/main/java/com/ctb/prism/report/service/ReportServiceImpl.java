package com.ctb.prism.report.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.prism.core.Service.IRepositoryService;
import com.ctb.prism.core.Service.IUsabilityService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.report.api.FillManagerImpl;
import com.ctb.prism.report.api.IFillManager;
import com.ctb.prism.report.business.IReportBusiness;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.IReportFilterTOFactory;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.webservice.transferobject.ReportActionTO;

@Service("reportService")
public class ReportServiceImpl implements IReportService {

	@Autowired
	private IReportBusiness reportBusiness;
	
	@Autowired
	IUsabilityService usabilityService;
	
	@Autowired
	private IRepositoryService repositoryService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getFilledReport(net.sf. jasperreports.engine.JasperReport, java.util.Map)
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		return reportBusiness.getFilledReport(jasperReport, parameters);
	}
	
	@Async
	public void getFilledReportForPDF(JasperReport jasperReport, Map<String, Object> parameters, boolean isPrinterFriendly, 
			String user, String sessionParam) throws Exception {
		parameters.put("p_Is3D", IApplicationConstants.FLAG_N);
		JasperPrint jasperPrint = reportBusiness.getFilledReport(jasperReport, parameters);
		/** session to cache **/
		//usabilityService.getSetCache(user, CustomStringUtil.appendString(sessionParam, Utils.getContractName()), jasperPrint);
		usabilityService.getSetCache(user, CustomStringUtil.appendString(sessionParam, (String)parameters.get("contractName")), jasperPrint);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#removeReportCache()
	 */
	public void removeReportCache() {
		reportBusiness.removeReportCache();
	}
	
	public void removeConfigurationCache(String contract) {
		reportBusiness.removeConfigurationCache(contract);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#removeCache()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#removeCache()
	 */
	public void removeCache() {
		//reportBusiness.removeCache();
		try {
			//removeCache("inors");
			//removeCache("tasc");
			//removeCacheSimpleDB("inors");
			//removeCacheSimpleDB("tasc");
			reportBusiness.removeCache();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Clear cache and remove keys from AWS Simple DB
	 * @param contractName
	 */
	public void removeCacheSimpleDB(String contractName) {
		reportBusiness.removeCache(contractName);
	}
	/*
	 * Remove cache based on contract
	 * @see com.ctb.prism.report.service.IReportService#removeCache(java.lang.String)
	 */
	@Deprecated
	public void removeCache(String contractName) throws IOException {
		String s3loc = null;
		if("inors".equals(contractName)) s3loc = propertyLookup.get("aws.inors.cacheS3");
		if("tasc".equals(contractName))  s3loc = propertyLookup.get("aws.tasc.cacheS3");
		
		InputStream inputStream = repositoryService.getAssetInputStream(s3loc+IApplicationConstants.CACHE_KEY_FILE);
		
		if(reportBusiness.removeCache(inputStream)){
			//Delete cache key file from from s3 after successful clear cache 
			repositoryService.removeAsset(s3loc+IApplicationConstants.CACHE_KEY_FILE);
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getReportJasperObject(java .lang.String)
	 */
	public JasperReport getReportJasperObject(String reportPath) {
		return reportBusiness.getReportJasperObject(reportPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getReportJasperObjectForName (java.lang.String)
	 */
	public JasperReport getReportJasperObjectForName(String reportname) {
		return reportBusiness.getReportJasperObjectForName(reportname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getReportJasperObjectList (java.lang.String)
	 */
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		return reportBusiness.getReportJasperObjectList(reportPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getInputControlDetails(java .lang.String)
	 */
	public List<InputControlTO> getInputControlDetails(String reportPath) {
		return reportBusiness.getInputControlDetails(reportPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAllInputControls()
	 */
	public List<InputControlTO> getAllInputControls() {
		return reportBusiness.getAllInputControls();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getDefaultFilter(java.util .List, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String customerId, String assessmentId, String combAssessmentId, String reportUrl, 
			Map<String, Object> sessionParams, String userId, String currentOrg) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(Utils.usernameNeeded(reportUrl)) {
			paramMap.put("userName", userName);
			paramMap.put("userId", userId);
		}
		paramMap.put("contractName", Utils.getContractName());
		paramMap.put("tos", tos);
		//paramMap.put("userName", userName);
		paramMap.put("customerId", customerId);
		paramMap.put("assessmentId", assessmentId);
		paramMap.put("combAssessmentId", combAssessmentId);
		paramMap.put("reportUrl", reportUrl);
		paramMap.put("sessionParams", sessionParams);
		//paramMap.put("userId", userId);
		paramMap.put("currentOrg", currentOrg);
		
		//return reportBusiness.getDefaultFilter(tos,userName,customerId,assessmentId,combAssessmentId,reportUrl,sessionParams,userId,currentOrg);
		return reportBusiness.getDefaultFilter(paramMap);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getValuesOfSingleInput(java .lang.String)
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query) {
		return reportBusiness.getValuesOfSingleInput(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getValuesOfSingleInput(java .lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.lang.Object, java.lang.String)
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String reportUrl, String query, String userName, String customerId, String changedObject, String changedValue, Map<String, String> replacableParams, Object clazz, String userId)
	throws SystemException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(Utils.usernameNeeded(reportUrl)) {
			paramMap.put("userName", userName); 
			paramMap.put("userId", userId);
		}
		paramMap.put("reportUrl", reportUrl);
		paramMap.put("query", query);
		//paramMap.put("userName", userName); 
		paramMap.put("customerId", customerId);
		paramMap.put("changedObject", changedObject);
		paramMap.put("changedValue", changedValue);
		paramMap.put("replacableParams", replacableParams);
		paramMap.put("obj", clazz);
		//paramMap.put("userId", userId);
		//return reportBusiness.getValuesOfSingleInput(query, userName, customerId, changedObject, changedValue, replacableParams, clazz, userId);
		return reportBusiness.getValuesOfSingleInput(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAllReportList(java.util .Map)
	 */
	public List<ReportTO> getAllReportList(Map<String, Object> paramMap) {
		return reportBusiness.getAllReportList(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#updateReportNew(com.ctb.prism .report.transferobject.ReportTO)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean updateReportNew(ReportTO reportTO) {
		return reportBusiness.updateReportNew(reportTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#deleteReport(java.util .Map)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor=SystemException.class)
	public boolean deleteReport(Map<String, Object> paramMap) throws SystemException {
		return reportBusiness.deleteReport(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAssessments(paramMap)
	 */
	public List<AssessmentTO> getAssessments(Map<String, Object> paramMap) {
		return reportBusiness.getAssessments(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#addNewDashboard(com.ctb.prism .report.transferobject.ReportParameterTO)
	 */
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO) throws Exception {
		return reportBusiness.addNewDashboard(reportParameterTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getReport(java.lang.String)
	 */
	public ReportTO getReport(String reportIdentifier) throws SystemException {
		return reportBusiness.getReport(reportIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#loadManageMessage(java.util .Map)
	 */
	public Map<String, Object> loadManageMessage(final Map<String, Object> paramMap) throws SystemException {
		return reportBusiness.loadManageMessage(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#saveManageMessage(java.util .List)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor=SystemException.class)
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException {
		return reportBusiness.saveManageMessage(manageMessageTOList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAllGroupDownloadFiles( java.util.Map)
	 */
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String, Object> paramMap) throws SystemException {
		return reportBusiness.getAllGroupDownloadFiles(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getRequestDetail(java.util .Map)
	 */
	public List<JobTrackingTO> getRequestDetail(Map<String, Object> paramMap) throws SystemException {
		return reportBusiness.getRequestDetail(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#deleteGroupFiles(java.lang .String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteGroupFiles(String Id) throws Exception {
		return reportBusiness.deleteGroupFiles(Id);
	}

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<Long, String> getScheduledGroupFiles(Map<String, Object> paramMap) throws Exception {
		return reportBusiness.getScheduledGroupFiles(paramMap);
	}
	
	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public void updateScheduledGroupFiles(Map<String, Object> paramMap) throws Exception{
		reportBusiness.updateScheduledGroupFiles(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getReportMessageFilter(java .util.Map)
	 */
	public Map<String, Object> getReportMessageFilter(final Map<String, Object> paramMap) throws SystemException {
		return reportBusiness.getReportMessageFilter(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getTestAdministrations()
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getTestAdministrations() {
		return reportBusiness.getTestAdministrations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#populateStudentTableGD(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public List<GroupDownloadStudentTO> populateStudentTableGD(GroupDownloadTO to) {
		return reportBusiness.populateStudentTableGD(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getGDFilePaths(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public Map<String, String> getGDFilePaths(GroupDownloadTO to) {
		return reportBusiness.getGDFilePaths(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#createJobTracking(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String createJobTracking(GroupDownloadTO to) {
		return reportBusiness.createJobTracking(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getSystemConfigurationMessage(java.util.Map)
	 */
	public String getSystemConfigurationMessage(Map<String, Object> paramMap) {
		return reportBusiness.getSystemConfigurationMessage(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getProcessDataGD(java.lang.String)
	 */
	public JobTrackingTO getProcessDataGD(String processId, String contractName) {
		return reportBusiness.getProcessDataGD(processId, contractName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getConventionalFileNameGD(java.lang.Long)
	 */
	public String getConventionalFileNameGD(Long orgNodeId) {
		return reportBusiness.getConventionalFileNameGD(orgNodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#updateJobTracking(com.ctb.prism.report.transferobject.GroupDownloadTO)
	 */
	public int updateJobTracking(GroupDownloadTO to) {
		return reportBusiness.updateJobTracking(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getReportMessage(java.util.Map)
	 */
	public String getReportMessage(Map<String, Object> paramMap) {
		return reportBusiness.getReportMessage(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getRequestSummary(java.lang.String)
	 */
	public String getRequestSummary(String requestDetails, String contractName) {
		return reportBusiness.getRequestSummary(requestDetails, contractName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAllReportMessages(java.util.Map)
	 */
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap) {
		return reportBusiness.getAllReportMessages(paramMap);
	}
	
	
	@Autowired private IReportFilterTOFactory reportFilterFactory;
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ReportServiceImpl.class.getName());
	
	/*@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'",
					key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('getReportParameter').concat(#currentOrg).concat(#reportUrl).concat( T(com.ctb.prism.core.util.CacheKeyUtils).string(#getFullList) ).concat( T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#param) ) )"),
			@Cacheable(value = "tascDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",
					key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('getReportParameter').concat(#currentOrg).concat(#reportUrl).concat( T(com.ctb.prism.core.util.CacheKeyUtils).string(#getFullList) ).concat( T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#param) ) )"),
			@Cacheable(value = "usmoDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",
					key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('getReportParameter').concat(#currentOrg).concat(#reportUrl).concat( T(com.ctb.prism.core.util.CacheKeyUtils).string(#getFullList) ).concat( T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#param) ) )")
	} )*/
	public Map<String, Object> getReportParameter(List<InputControlTO> allInputControls, Object reportFilterTO, 
			JasperReport jasperReport, boolean getFullList, HttpServletRequest req, String reportUrl, String currentOrg) {
		//long start = System.currentTimeMillis();
		Class<?> c = reportFilterFactory.getReportFilterTO();
		//long end1 = System.currentTimeMillis();
		//System.out.println(CustomStringUtil.getHMSTimeFormat(end1 - start)+" <<<< Time Taken: no cache getReportParameter - report filter >>>> ");
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			String loggedInUserJasperOrgId = (String) c.getMethod("getLoggedInUserJasperOrgId").invoke(reportFilterTO);
			parameters.put(IApplicationConstants.JASPER_ORG_PARAM, loggedInUserJasperOrgId);
			if(Utils.usernameNeeded(reportUrl)) {
				String userName = (String) c.getMethod("getLoggedInUserName").invoke(reportFilterTO);
				parameters.put(IApplicationConstants.JASPER_USER_PARAM, userName);
				parameters.put(IApplicationConstants.JASPER_USERID_PARAM, (String) req.getSession().getAttribute(IApplicationConstants.CURRUSERID));
			}
			//parameters.put(IApplicationConstants.JASPER_USER_PARAM, userName);
			//parameters.put(IApplicationConstants.JASPER_USERID_PARAM, (String) req.getSession().getAttribute(IApplicationConstants.CURRUSERID));
			parameters.put(IApplicationConstants.JASPER_CUSTOMERID_PARAM, (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER));
			if (allInputControls != null) {
				for (InputControlTO inputControlTO : allInputControls) {
					String label = inputControlTO.getLabelId();
					String fieldName = CustomStringUtil.capitalizeFirstCharacter(inputControlTO.getLabelId());
					Method m = c.getMethod(CustomStringUtil.appendString("get", fieldName));
					@SuppressWarnings("unchecked")
					// List<ObjectValueTO> listOfValues = (List<ObjectValueTO>) m.invoke(reportFilterTO);
					List<ObjectValueTO> listOfValues = (List<ObjectValueTO>) m.invoke(reportFilterTO);

					/** PATCH FOR DEFAULT SUBTEST AND SCORE TYPE POPULATION (Multiselect) */
					// get default list for multiselect subtest
					// this code is a path for subtest to meet business requirement to show default subtest list
					/**  */
					String[] defaultValues = null;
					boolean checkDefault = false;
					List<String> defaultInputNames = new ArrayList<String>();
					Map<String, String[]> defaultInputValues = new HashMap<String, String[]>();
					
					try {
						for (IApplicationConstants.PATCH_FOR_SUBTEST subtest : IApplicationConstants.PATCH_FOR_SUBTEST.values()) {
							if (subtest.name().equals(inputControlTO.getLabelId())) {
								defaultInputNames.add(inputControlTO.getLabelId());
								for (int i = 0; i < jasperReport.getParameters().length; i++) {
									if (inputControlTO.getLabelId().equals(jasperReport.getParameters()[i].getName())) {
										String value = jasperReport.getParameters()[i].getDefaultValueExpression().getText();
										value = value.replace("Arrays.asList(", "");
										value = value.replace(")", "");
										value = value.replace("\"", "");
										defaultValues = value.split(",");
										checkDefault = true;
										break;
									}
								}
								defaultInputValues.put(inputControlTO.getLabelId(), defaultValues);
								// break;
							}
						}
					} catch (Exception e) {
						logger.log(IAppLogger.WARN, CustomStringUtil.appendString("Some error occured for subtest multiselect : ", e.getMessage()));
					}
					/** END : PATCH FOR DEFAULT SUBTEST AND SCORE TYPE POPULATION (Multiselect) */

					/***NEW***/
					String switchUser = (String) req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN);
					boolean isSwitchUser = false;
					// check if user logs-in first time
					if (switchUser != null && switchUser.trim().length() > 0) {
						isSwitchUser = true;
					}
					
					String[] valueFromSession = isSwitchUser==false? getFromSession(req, label) : null ;
					/***NEW***/
					
					Map<String, Object> sessionParameters = null;
					if (req != null)
						sessionParameters = (Map<String, Object>) req.getSession().getAttribute("inputControls");
					if (sessionParameters == null) {
						sessionParameters = new HashMap<String, Object>();
					}

					boolean sessionArray = false;
					if (sessionParameters.get(label) instanceof List<?>) {
						sessionArray = true;
					}

					// patch for input control blank for text type i/p controls
					boolean customReport = false;
					if (IApplicationConstants.TRUE.equals(req.getSession().getAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + req.getParameter("reportUrl")))) {
						customReport = true;
					} else {
						//long start2 = System.currentTimeMillis();
						/*int count = 0;
						while (jasperReport == null && count < 25) {
							count++;
							Thread.sleep(100);
							//jasperReport = (JasperReport) req.getSession().getAttribute(CustomStringUtil.appendString(req.getParameter("reportUrl"), "_", req.getParameter("assessmentId")));
							*//** session to cache **//*
							jasperReport = (JasperReport) 
								usabilityService.getSetCache((String) req.getSession().getAttribute(IApplicationConstants.CURRUSER), 
									CustomStringUtil.appendString(req.getParameter("reportUrl"), "_", req.getParameter("assessmentId")), null);
						}*/
						
						if(jasperReport == null) {
							jasperReport = (JasperReport) 
								usabilityService.getSetCache((String) req.getSession().getAttribute(IApplicationConstants.CURRUSER), 
										CustomStringUtil.appendString(req.getParameter("reportUrl"), "_", req.getParameter("assessmentId"), Utils.getContractName()), null);
						}
						//long end2 = System.currentTimeMillis();
						//System.out.println(CustomStringUtil.getHMSTimeFormat(end2 - start2)+" <<<< Time Taken: no cache getReportParameter:get report from cache >>>> " );
						//long start3 = System.currentTimeMillis();
						
						if(jasperReport == null) {
							jasperReport = getMainReport(getJasperReportObject(reportUrl));
						}
						//long end3 = System.currentTimeMillis();
						//System.out.println(CustomStringUtil.getHMSTimeFormat(end3 - start3)+" <<<< Time Taken: no cache getReportParameter:get report from table >>>> " );
						
					}
					// end patch

					//long start2 = System.currentTimeMillis();
					//if(jasperReport != null)
					if (getFullList) {
						// for input control section
						if (IApplicationConstants.DATA_TYPE_TESTBOX.equals(inputControlTO.getType()) && jasperReport != null) {
							if (!customReport) {
								for (int i = 0; i < jasperReport.getParameters().length; i++) {
									if (inputControlTO.getLabelId().equals(jasperReport.getParameters()[i].getName())) {
										String value = "";
										if (jasperReport.getParameters()[i].getDefaultValueExpression() != null
												&& !"\"\"".equals(jasperReport.getParameters()[i].getDefaultValueExpression().getText())) {
											value = jasperReport.getParameters()[i].getDefaultValueExpression().getText();
										}
										//parameters.put(label, value);
										parameters.put(label, (sessionParameters.get(label) != null) ? sessionParameters.get(label) : value);
										break;
									}
								}
							}
						} else {
							parameters.put(inputControlTO.getLabelId(), listOfValues);
							if (sessionParameters.get(label) != null) {
								Map<String, String[]> selectInputValues = new HashMap<String, String[]>();
								List<String> selectInputNames = new ArrayList<String>();
								if (sessionParameters.get(label) instanceof String) {
									selectInputValues.put(label, new String[] { ((String) sessionParameters.get(label)) });
								} else {
									selectInputValues.put(label, ((List<String>) sessionParameters.get(label)).toArray(new String[0]));
								}
								selectInputNames.add(label);
								parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_SELECTED, label), selectInputValues);
								parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_SELECTED_NAME, label), selectInputNames);
							}
							/***NEW***/
							if(valueFromSession != null) {
								Map<String, String[]> selectInputValues = new HashMap<String, String[]>();
								List<String> selectInputNames = new ArrayList<String>();
								
								/*As discussed with Amit Da, commented the bellow code. - By Joy on 02-MAR-2016 
								 * Student Label will not being used so commented the code  
								 */
								/*if(reportUrl.equals("/public/Missouri/Report/Student_Roster_files") && valueFromSession.length > 1){ 
								} else {	
									selectInputValues.put(label, valueFromSession);
									selectInputNames.add(label);
								}*/
								
								selectInputValues.put(label, valueFromSession);
								selectInputNames.add(label);
								parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_SELECTED, label), selectInputValues);
								parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_SELECTED_NAME, label), selectInputNames);
							}
							/***NEW***/
							if (!checkDefault) {
								parameters.put(inputControlTO.getLabelId(), listOfValues);
							} else {
								parameters.put(inputControlTO.getLabelId(), listOfValues);
								parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_DEFAULT, label), defaultInputValues);
								parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_DEFAULT_NAME, label), defaultInputNames);

								/*
								 * // list need to be modified based on default value List<ObjectValueTO> inputCollection = new ArrayList<ObjectValueTO>(); for(ObjectValueTO objectValue :
								 * listOfValues) { // this field has default values - need to pass values that belongs to this default list if(defaultValues != null) { for(String currentVal :
								 * defaultValues) { if(objectValue.getValue().equals(currentVal)) { inputCollection.add(objectValue); } } } else { inputCollection.add(objectValue); }
								 * parameters.put(IApplicationConstants.CHECK_DEFAULT, defaultValues); parameters.put(IApplicationConstants.CHECK_DEFAULT_NAME, defaultInputNames); }
								 * parameters.put(inputControlTO.getLabelId(), inputCollection);
								 */
						}
						}
					} else {
						// fetch i/p for default values
						if (IApplicationConstants.DATA_TYPE_COLLECTION.equals(inputControlTO.getType())) {
							// passing array
							List<String> inputCollection = new ArrayList<String>();
							for (ObjectValueTO objectValue : listOfValues) {
								if (!checkDefault) {
									inputCollection.add(objectValue.getValue());
									if (IApplicationConstants.EXTENDED_YEAR.equals(inputControlTO.getLabelId())) {
										break;
									}
								} else {
									// this field has default values - need to pass values that belongs to this default list
									if (defaultValues != null) {
										for (String currentVal : defaultValues) {
											if (objectValue.getValue().equals(currentVal)) {
												inputCollection.add(objectValue.getValue());
											}
										}
										parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_DEFAULT, label), defaultInputValues);
										parameters.put(CustomStringUtil.appendString(IApplicationConstants.CHECK_DEFAULT_NAME, label), defaultInputNames);
									} else {
										inputCollection.add(objectValue.getValue());
							}
								}
							}
							 //parameters.put(label, inputCollection);
							parameters.put(label,
									(sessionParameters.get(label) != null) ? ((sessionArray) ? ((List<String>) sessionParameters.get(label)).toArray(new String[0]) : sessionParameters.get(label))
											: inputCollection);
							/***NEW***/
							if(valueFromSession != null) {
								if("p_examiner".equals(label)) {
									// apply this patch for TD 83266
									if(Collections.indexOfSubList(inputCollection, new ArrayList<String>(Arrays.asList(valueFromSession))) != -1) {
										parameters.put(inputControlTO.getLabelId(), new ArrayList<String>(Arrays.asList(valueFromSession)));
									}
								} else {
									parameters.put(inputControlTO.getLabelId(), new ArrayList<String>(Arrays.asList(valueFromSession)));
								}
							} else {
								parameters.put(inputControlTO.getLabelId(), inputCollection);
							}
							/***NEW***/
						} else if (IApplicationConstants.DATA_TYPE_TESTBOX.equals(inputControlTO.getType()) && jasperReport != null) {
							if (!customReport) {
								for (int i = 0; i < jasperReport.getParameters().length; i++) {
									if (label.equals(jasperReport.getParameters()[i].getName())) {
										// String value = jasperReport.getParameters()[i].getDefaultValueExpression().getText();
										String value = "";
										if (jasperReport.getParameters()[i].getDefaultValueExpression() != null
												&& !"\"\"".equals(jasperReport.getParameters()[i].getDefaultValueExpression().getText())) {
											value = jasperReport.getParameters()[i].getDefaultValueExpression().getText();
										}
										 //parameters.put(label, value);
										parameters.put(label, (sessionParameters.get(label) != null) ? ((sessionArray) ? ((List<String>) sessionParameters.get(label)).toArray(new String[0])
												: sessionParameters.get(label)) : value);
										break;
									}
								}
							}
						} else {
							String value = "";
							if (listOfValues != null && listOfValues.size() > 0) {
								if ("Form/Level".equals(inputControlTO.getLabel()) || "Level".equals(inputControlTO.getLabel()) || "Days".equals(inputControlTO.getLabel())) {
									for (ObjectValueTO objectValue : listOfValues) {
										if (objectValue.getName() != null && objectValue.getName().indexOf("Default") != -1) {
											// this block is for form/level
											value = objectValue.getValue();
										}
									}
								} else {
									value = listOfValues.get(0).getValue();
								}
							}
							// fallback code
							if ((value == null || value.length() == 0) && listOfValues != null && listOfValues.size() > 0) {
								value = listOfValues.get(0).getValue();
							}
							//parameters.put(label, value);
							parameters.put(label,
									(sessionParameters.get(label) != null) ? ((sessionArray) ? ((List<String>) sessionParameters.get(label)).toArray(new String[0]) : sessionParameters.get(label))
											: value);
							/***NEW***/
							if(valueFromSession != null && valueFromSession.length > 0) {
								boolean objExists = false;
								for (ObjectValueTO objectValue : listOfValues) {
									if (objectValue.getValue() != null && objectValue.getValue().equals(valueFromSession[0])) {
										parameters.put(inputControlTO.getLabelId(), valueFromSession[0]);
										objExists = true;
										break;
									}
								}
								if(!objExists) parameters.put(inputControlTO.getLabelId(), value);
							} else {
								parameters.put(inputControlTO.getLabelId(), value);
							}
							/***NEW***/
						}
					}
					// patch for TASC - start date and end date
					if("p_Start_Test_Date".equals(label)) {
						// sysdate minus 30 days
						parameters.put(label, Utils.getPreviusDateTime("MM/dd/yyyy", 30));
					}
					if("p_End_Test_Date".equals(label)) {
						// sysdate
						parameters.put(label, Utils.getDateTime("MM/dd/yyyy"));
					}
					//long end2 = System.currentTimeMillis();
					//System.out.println(CustomStringUtil.getHMSTimeFormat(end2 - start2)+" <<<< Time Taken: no cache getReportParameter:get-list "+getFullList+" - report filter >>>> ");
				}
			}
		} catch (Exception e) {
			logger.log(IAppLogger.WARN, CustomStringUtil.appendString("Some error occured : ", e.getMessage()));
			e.printStackTrace();
		}
		//long end = System.currentTimeMillis();
		//System.out.println(CustomStringUtil.getHMSTimeFormat(end - start)+" <<<< Time Taken: no cache getReportParameter:end >>>> ");
		return parameters;
	}
	
	/**
	 * Get jasper report object.
	 * 
	 * @param reportUrl
	 * @return JasperReport object list
	 * @throws DataAccessException
	 * @throws JRException
	 * @throws Exception
	 */
	@com.googlecode.ehcache.annotations.Cacheable(cacheName = "allReports")
	public List<ReportTO> getJasperReportObject(String reportUrl) throws DataAccessException, JRException, Exception {
		// Connection conn = null;
		JasperReport jasperReport = null;
		List<ReportTO> reportList = null;
		try {
			// get compiled jasper report
			// JasperReport jasperReport = getCompliledJrxml(reportUrl);
			boolean mainReportPresent = false;
			reportList = getReportJasperObjectList(reportUrl);
			if (reportList != null && !reportList.isEmpty()) {
				for (ReportTO reportTo : reportList) {
					if (reportTo.isMainReport()) {
						jasperReport = reportTo.getCompiledReport();
						mainReportPresent = true;
						break;
					}
				}

				if (!mainReportPresent) {
					jasperReport = reportList.get(0).getCompiledReport();
				}
			} else {
				// report empty
				throw new Exception("Report not found");
			}

		} catch (DataAccessException exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
			throw new DataAccessException(exception.getMessage()) {
				private static final long serialVersionUID = 1L;
			};
		} catch (JRException exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
			throw new JRException(exception.getMessage());
		} catch (Exception exception) {
			logger.log(IAppLogger.ERROR, exception.getMessage(), exception);
			throw new Exception(exception.getMessage());
		} finally {
			// if(conn != null) try {conn.close();} catch (SQLException e) {}
			logger.log(IAppLogger.INFO, "Exit: ReportController - reportList");
		}
		return reportList;
	}
	
	/**
	 * Retrieve main report object
	 * 
	 * @param jasperReportList
	 * @return
	 * @throws Exception
	 */
	@com.googlecode.ehcache.annotations.Cacheable(cacheName = "mainReport")
	public JasperReport getMainReport(List<ReportTO> jasperReportList) throws Exception {
		JasperReport jasperReport = null;
		boolean mainReportPresent = false;
		if (jasperReportList != null && !jasperReportList.isEmpty()) {
			for (ReportTO reportTo : jasperReportList) {
				if (reportTo.isMainReport()) {
					jasperReport = reportTo.getCompiledReport();
					mainReportPresent = true;
					break;
				}
			}

			if (!mainReportPresent) {
				jasperReport = jasperReportList.get(0).getCompiledReport();
			}
		} else {
			// report empty
			throw new Exception("Report not found");
		}
		return jasperReport;
	}
	
	private String[] getFromSession(HttpServletRequest req, String label) {
		return (String[]) req.getSession().getAttribute("_REMEMBER_ME_" + label);
	}
	
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#parameterValues) == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('fillReportForTableApi').concat(#reportUrl).concat( T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameterValues) ) )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#parameterValues) == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('fillReportForTableApi').concat(#reportUrl).concat( T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameterValues) ) )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract(#parameterValues) == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( ('fillReportForTableApi').concat(#reportUrl).concat( T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#parameterValues) ) )")
	} )
	public JasperPrint fillReportForTableApi(String reportUrl, JasperReport jasperReport, Map<String, Object> parameterValues) 
		throws JRException, SQLException {
		IFillManager fillManager = new FillManagerImpl();
		return fillManager.fillReport(jasperReport, parameterValues);
	}

	public String getStudentFileName(String type, String studentBioId, String custProdId) {
		return reportBusiness.getStudentFileName(type, studentBioId, custProdId);
	}

	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.service.IReportService#getProductsForEditActions(java.util.Map)
	 */
	public List<ReportActionTO> getProductsForEditActions(Map<String, Object> paramMap) {
		return reportBusiness.getProductsForEditActions(paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.service.IReportService#getActionsForEditActions(java.util.Map)
	 */
	public List<ReportActionTO> getActionsForEditActions(Map<String, Object> paramMap) {
		return reportBusiness.getActionsForEditActions(paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.report.service.IReportService#getActionAccess(java.util.Map)
	 */
	public List<ReportActionTO> getActionAccess(Map<String, Object> paramMap) {
		return reportBusiness.getActionAccess(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.report.service.IReportService#updateDataForActions(java.util.Map)
	 */
	public String updateDataForActions(Map<String, Object> paramMap) {
		return reportBusiness.updateDataForActions(paramMap);
	}
	
	public void updateJobTrackingTable(String jobId,String filePath)
	{
		reportBusiness.updateJobTrackingTable(jobId,filePath);	
	}

	public Object getDefaultFilterTasc(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId, String reportUrl) {
		return reportBusiness.getDefaultFilterTasc(tos, userName, assessmentId, combAssessmentId, reportUrl );
	}

	public List<ObjectValueTO> getValuesOfSingleInputTasc(String query, String userName, String changedObject, String changedValue,
			Map<String, String> replacableParams, Object clazz, boolean bulkDownload) throws SystemException {
		return reportBusiness.getValuesOfSingleInputTasc(query, userName, changedObject, changedValue, replacableParams, clazz, bulkDownload);
	}

	public Map<String, String> getGenericSystemConfigurationMessages(Map<String, Object> paramMap) {
		return reportBusiness.getGenericSystemConfigurationMessages(paramMap);
	}

}
