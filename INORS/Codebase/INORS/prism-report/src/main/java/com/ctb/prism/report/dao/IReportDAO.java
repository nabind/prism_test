package com.ctb.prism.report.dao;

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
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.webservice.transferobject.ReportActionTO;

public interface IReportDAO {
	/**
	 * 
	 */
	public void removeReportCache();

	/**
	 * 
	 */
	public void removeCache();

	public void removeCache(String cacheKey);

	public void removeConfigurationCache(String contract);
	
	public boolean removeInorsCache(String contract);
	public boolean removeTascCache(String contract);
	
	public void removeInorsConfig(String contract);
	public void removeTascConfig(String contract);

	/**
	 * This method returns report print object filled with data.
	 * 
	 * @param jasperReport
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;

	/**
	 * Same report - without putting the same in cache.
	 * 
	 * @param jasperReport
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public JasperPrint getFilledReportNoCache(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;

	public JasperPrint getFilledReportIC(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;

	/**
	 * Returns {@link JasperReport} object of a particular report by compiling
	 * the JRXML file retrieved from database.
	 * 
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
	 * This method is to fetch all reports (including subreports).
	 * 
	 * @param reportPath
	 *            Report URL
	 * @return
	 * @throws JRException
	 */
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException;

	/**
	 * Returns the input control details of a particular report
	 * 
	 * @param reportPath
	 * @return
	 */
	public List<InputControlTO> getInputControlDetails(String reportPath);

	/**
	 * @return
	 */
	public List<InputControlTO> getAllInputControls();

	/**
	 * Fetch all values for a single input control.
	 * 
	 * @param query
	 * @return
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query);

	/**
	 * Returns the information( report id, report name, report url, the user
	 * roles who can access the report etc) of all reports.
	 * 
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
	public boolean deleteReport(Map<String, Object> paramMap) throws SystemException;

	/**
	 * Retrieves assessments details from database.
	 * 
	 * 
	 * @param paramMap
	 * @return List of all available assessments {@link AssessmentTO} along with
	 *         corresponding report details {@link ReportTO}
	 */
	public List<AssessmentTO> getAssessments(Map<String, Object> paramMap);

	/**
	 * Retrieves and returns tenantId corresponding to the userName.
	 * 
	 * @param userName
	 * @return
	 */
	public String getTenantId(String userName);

	/**
	 * @param reportParameterTO
	 * @return
	 * @throws Exception
	 */
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO) throws Exception;

	/**
	 * @return
	 */
	public List<ObjectValueTO> getAllRoles();

	/**
	 * @return
	 */
	public List<ObjectValueTO> getAllAssessments();

	/**
	 * @param reportIdentifier
	 * @return
	 */
	public ReportTO getReport(String reportIdentifier);

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<ManageMessageTO> loadManageMessage(final Map<String, Object> paramMap) throws SystemException;

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
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getAdminYear(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getOrgNodeLevel(final Map<String, Object> paramMap) throws SystemException;

	/**
	 * For Group Download listing.
	 * 
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 */
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String, Object> paramMap) throws SystemException;

	/**
	 * For Group Download listing.
	 * 
	 * @param paramMap
	 * @return
	 * @throws SystemException
	 * @author Arunava
	 */
	public List<JobTrackingTO> getRequestDetail(Map<String, Object> paramMap) throws SystemException;

	/**
	 * For Group Download deleting.
	 * 
	 * @param Id
	 * @return
	 * @throws Exception
	 * @author Arunava
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
	 * @param to
	 * @return
	 */
	public String createJobTracking(GroupDownloadTO to);

	/**
	 * @param to
	 * @return
	 */
	public Map<String, String> getGDFilePaths(GroupDownloadTO to);

	/**
	 * @param paramMap
	 * @return
	 */
	public String getSystemConfigurationMessage(Map<String, Object> paramMap);

	/**
	 * @param paramMap
	 * @return
	 */
	public String getReportMessage(Map<String, Object> paramMap);

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
	 */
	public String updateDataForActions(Map<String, Object> paramMap);
	
	public void updateJobTrackingTable(String jobId, String filePathcontractName);

	public List<ObjectValueTO> getStudentList(String query);
	
	public Map<String, String> getGenericSystemConfigurationMessages(Map<String, Object> paramMap);
}
