package com.ctb.prism.report.business;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.amazonaws.services.simpledb.model.Item;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
//import com.ctb.prism.report.transferobject.ReportFilterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.webservice.transferobject.ReportActionTO;

public interface IReportBusiness {
	/**
	 * 
	 */
	public void removeReportCache();

	/**
	 * 
	 */
	public void removeCache();
	
	public boolean removeCache(InputStream input);
	
	public boolean removeCache(List<Item> cacheKeyList, String contractName);
	
	public boolean removeCache(String contractName);
	
	public void removeConfigurationCache(String contract);

	/**
	 * @param jasperReport
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;

	/**
	 * @param reportPath
	 * @return
	 */
	public JasperReport getReportJasperObject(String reportPath);

	/**
	 * @param reportname
	 * @return
	 */
	public JasperReport getReportJasperObjectForName(String reportname);

	/**
	 * @param reportPath
	 * @return
	 * @throws JRException
	 */
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException;

	/**
	 * @param reportPath
	 * @return
	 */
	public List<InputControlTO> getInputControlDetails(String reportPath);

	/**
	 * @return
	 */
	public List<InputControlTO> getAllInputControls();

	/**
	 * @param tos
	 * @param userName
	 * @param customerId
	 * @param assessmentId
	 * @param combAssessmentId
	 * @param reportUrl
	 * @param sessionParams
	 * @param userId
	 * @return
	 */
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String customerId, String assessmentId, String combAssessmentId, String reportUrl, 
			Map<String, Object> sessionParams, String userId, String tenantId);

	/**
	 * @param query
	 * @return
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query);

	/**
	 * @param query
	 * @param userName
	 * @param customerId
	 * @param changedObject
	 * @param changedValue
	 * @param replacableParams
	 * @param clazz
	 * @param userId
	 * @return
	 * @throws SystemException
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String customerId, String changedObject, String changedValue, Map<String, String> replacableParams, Object clazz, String userId)
			throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 */
	public List<ReportTO> getAllReportList(Map<String, Object> paramMap);

	/**
	 * @param reportTO
	 * @return
	 */
	public boolean updateReportNew(ReportTO reportTO);

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public boolean deleteReport(Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 */
	public List<AssessmentTO> getAssessments(Map<String, Object> paramMap);

	/**
	 * @param reportParameterTO
	 * @return
	 * @throws Exception
	 */
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO) throws Exception;

	/**
	 * @param reportIdentifier
	 * @return
	 * @throws SystemException
	 */
	public ReportTO getReport(String reportIdentifier) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public Map<String, Object> loadManageMessage(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param manageMessageTO
	 * @return
	 * @throws SystemException
	 */
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTO) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<JobTrackingTO> getRequestDetail(Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param Id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGroupFiles(String Id) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<Long, String> getScheduledGroupFiles(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap
	 * @throws Exception
	 */
	public void updateScheduledGroupFiles(Map<String, Object> paramMap) throws Exception;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public Map<String, Object> getReportMessageFilter(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * Creates the list of test administrations
	 * 
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getTestAdministrations();

	/**
	 * 
	 * @param to
	 * @return
	 */
	public List<GroupDownloadStudentTO> populateStudentTableGD(GroupDownloadTO to);

	/**
	 * 
	 * @param to
	 * @return
	 */
	public Map<String, String> getGDFilePaths(GroupDownloadTO to);

	/**
	 * @param to
	 * @return
	 */
	public String createJobTracking(GroupDownloadTO to);

	public String getSystemConfigurationMessage(Map<String, Object> paramMap);

	/**
	 * @param processId
	 * @return
	 */
	public JobTrackingTO getProcessDataGD(String processId, String contractName);

	/**
	 * @param orgNodeId
	 * @return
	 */
	public String getConventionalFileNameGD(Long orgNodeId);

	/**
	 * Updates the JOB_TRACKING table.
	 * 
	 * @param to
	 * @return
	 */
	public int updateJobTracking(GroupDownloadTO to);

	/**
	 * @param paramMap
	 * @return
	 */
	public String getReportMessage(Map<String, Object> paramMap);

	/**
	 * @param requestDetails
	 * @return
	 */
	public String getRequestSummary(String requestDetails, String contractName);

	/**
	 * @param paramMap
	 * @return
	 */
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap);

	public String getStudentFileName(String type, String studentBioId, String custProdId);

	public int updateJobTrackingStatus(String contractName, String jobId, String jobStatus, String jobLog);

	/**
	 * Returns ReportActionTO object for Report Data.
	 * 
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<ReportActionTO> getProductsForEditActions(Map<String, Object> paramMap);

	/**
	 * Returns a collection of ReportActionTO objects for Actions data.
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<ReportActionTO> getActionsForEditActions(Map<String, Object> paramMap);
	
	/**
	 * @param paramMap
	 * @return
	 */
	public List<ReportActionTO> getActionAccess(Map<String, Object> paramMap);

	/**
	 * @param paramMap
	 * @return
	 */
	public String updateDataForActions(Map<String, Object> paramMap);
	
	public void updateJobTrackingTable(String jobId,String filePath);
	
	Object getDefaultFilterTasc(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId, String reportUrl);

	public List<ObjectValueTO> getValuesOfSingleInputTasc(String query, String userName, String changedObject, String changedValue,
			Map<String, String> replacableParams, Object clazz, boolean bulkDownload) throws SystemException;

	public Map<String, String> getGenericSystemConfigurationMessages(Map<String, Object> paramMap);
}
