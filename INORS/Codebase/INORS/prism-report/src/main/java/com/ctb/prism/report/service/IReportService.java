package com.ctb.prism.report.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.dao.DataAccessException;

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
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.webservice.transferobject.ReportActionTO;

public interface IReportService {
	/**
	 * 
	 */
	public void removeReportCache();

	/**
	 * 
	 */
	public void removeCache();

	public void removeCache(String contractName) throws IOException;

	public void removeConfigurationCache();

	/**
	 * @param jasperReport
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;

	public void getFilledReportForPDF(JasperReport jasperReport, Map<String, Object> parameters, boolean isPrinterFriendly, 
			String user, String sessionParam) throws Exception;

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
			Map<String, Object> sessionParams, String userId, String currentOrg);

	/**
	 * @param query
	 * @return
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query);

	/**
	 * @param query
	 * @param userName
	 * @param changedObject
	 * @param changedValue
	 * @param replacableParams
	 * @param clazz
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
	 * @param reportId
	 * @return
	 * @throws SystemException
	 */
	public boolean deleteReport(String reportId) throws SystemException;

	/**
	 * @param parentReports
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
	 * @param manageMessageTOList
	 * @return
	 * @throws SystemException
	 */
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException;

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
	 * @param gdfExpiryTime
	 * @throws Exception
	 */
	public void deleteScheduledGroupFiles(String gdfExpiryTime) throws Exception;

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
	 * 
	 * @param to
	 * @return
	 */
	public String createJobTracking(GroupDownloadTO to);

	/**
	 * @param paramMap
	 * @return
	 */
	public String getSystemConfigurationMessage(Map<String, Object> paramMap);

	/**
	 * @param processId
	 * @return
	 */
	public JobTrackingTO getProcessDataGD(String processId);

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
	public String getRequestSummary(String requestDetails);

	/**
	 * @param paramMap
	 * @return
	 */
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap);

	public Map<String, Object> getReportParameter(List<InputControlTO> allInputControls, 
			Object reportFilterTO, JasperReport jasperReport, boolean getFullList, HttpServletRequest req, 
			String reportUrl, String currentOrg, Map<String, String[]> param) ;
	
	public JasperPrint fillReportForTableApi(String reportUrl, JasperReport jasperReport, Map<String, Object> parameterValues) 
		throws JRException, SQLException;

	public List<ReportTO> getJasperReportObject(String reportUrl) throws DataAccessException, JRException, Exception;

	public JasperReport getMainReport(List<ReportTO> jasperReportList) throws Exception;

	public String getStudentFileName(String type, String studentBioId, String custProdId);

	/**
	 * Returns ReportActionTO object for Report Data.
	 * 
	 * 
	 * @param paramMap
	 * @return
	 */
	public ReportActionTO getReportDataForEditActions(Map<String, Object> paramMap);

	/**
	 * Returns a collection of ReportActionTO objects for Actions data.
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<ReportActionTO> getActionDataForEditActions(Map<String, Object> paramMap);

	/**
	 * @param paramMap
	 */
	public String updateDataForActions(Map<String, Object> paramMap);

	public void updateJobTrackingTable(String jobId,String filePath);

}
