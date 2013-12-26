package com.ctb.prism.report.service;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;

public interface IReportService {
	public void removeReportCache();
	public void removeCache();
	
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;
	JasperReport getReportJasperObject(String reportPath);
	public JasperReport getReportJasperObjectForName(String reportname);
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException;
	
	List<InputControlTO> getInputControlDetails(String reportPath);
	public List<InputControlTO> getAllInputControls();
	Object getDefaultFilter(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId,
			String reportUrl );
	
	public List<ObjectValueTO> getValuesOfSingleInput(String query);
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String changedObject, 
			String changedValue, Map<String, String> replacableParams, Object clazz) throws SystemException;
	
	public List<ReportTO> getAllReportList(Map<String,Object> paramMap);
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles );
	public boolean updateReportNew(ReportTO reportTO);
	public boolean deleteReport(String reportId)throws SystemException;
	List<AssessmentTO> getAssessments(boolean parentReports);
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO)throws Exception;
	public ReportTO getReport(String reportIdentifier) throws SystemException;
	public Map<String,Object> loadManageMessage(final Map<String,Object> paramMap) throws SystemException;
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException;
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String,Object> paramMap)throws SystemException;
	public List<JobTrackingTO> getRequestDetail(Map<String,Object> paramMap)throws SystemException;
	public boolean deleteGroupFiles(String Id) throws Exception;
	public void deleteScheduledGroupFiles(String gdfExpiryTime) throws Exception;
	public Map<String,Object> getReportMessageFilter(final Map<String,Object> paramMap) throws SystemException;

	/**
	 * Creates the list of test administrations
	 * 
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getTestAdministrations();

	/**
	 * Creates the list of districts
	 * 
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGD(GroupDownloadTO to);

	/**
	 * Creates the list of grades
	 * 
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateGradeGD(GroupDownloadTO to);

	/**
	 * 
	 * @param to
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGD(GroupDownloadTO to);

	/**
	 * 
	 * @param to
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateClassGD(GroupDownloadTO to);

	/**
	 * 
	 * @param to
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateStudentTableGD(GroupDownloadTO to);

	/**
	 * 
	 * @param to
	 * @return
	 */
	public List<String> getFilePathGD(GroupDownloadTO to);
}
