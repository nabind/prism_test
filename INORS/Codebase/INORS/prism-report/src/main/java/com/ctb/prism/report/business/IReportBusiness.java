package com.ctb.prism.report.business;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownload;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
//import com.ctb.prism.report.transferobject.ReportFilterTO;
import com.ctb.prism.report.transferobject.ReportTO;

public interface IReportBusiness {
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
	public Map<String, Object> loadManageMessage(final Map<String,Object> paramMap) throws SystemException;
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTO) throws SystemException;
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String,Object> paramMap)throws SystemException;
	public boolean deleteGroupFiles(String Id)	throws Exception;
	public Map<String,Object> getReportMessageFilter(final Map<String,Object> paramMap) throws SystemException;
}
