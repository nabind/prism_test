package com.ctb.prism.report.service;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;

public interface IReportService {
	/**
	 * 
	 */
	public void removeReportCache();

	/**
	 * 
	 */
	public void removeCache();

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
	 * @param assessmentId
	 * @param combAssessmentId
	 * @param reportUrl
	 * @return
	 */
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId, String reportUrl, Map<String, Object> sessionParams);

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
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String changedObject, String changedValue, Map<String, String> replacableParams, Object clazz)
			throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 */
	public List<ReportTO> getAllReportList(Map<String, Object> paramMap);

	/**
	 * @param reportId
	 * @param reportName
	 * @param reportUrl
	 * @param isEnabled
	 * @param roles
	 * @return
	 */
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles);

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
	public List<AssessmentTO> getAssessments(boolean parentReports);

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
	public List<String> getGDFilePaths(GroupDownloadTO to);

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

}
