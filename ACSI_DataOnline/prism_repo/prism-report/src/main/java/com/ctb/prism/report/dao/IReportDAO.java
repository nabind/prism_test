package com.ctb.prism.report.dao;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportTO;

public interface IReportDAO {
	public void removeReportCache();
	public void removeCache();
	
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;
	public JasperPrint getFilledReportNoCache(JasperReport jasperReport, Map<String, Object> parameters) throws Exception;
	JasperReport getReportJasperObject(String reportPath);
	public JasperReport getReportJasperObjectForName(String reportname);
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException;
	
	List<InputControlTO> getInputControlDetails(String reportPath);
	
	public List<ObjectValueTO> getValuesOfSingleInput(String query);
	
	
	public List<ReportTO> getAllReportList();
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles );
	public boolean deleteReport(String reportId)throws SystemException;
	
	List<AssessmentTO> getAssessments(boolean parentReports);
	
	
	public String getTenantId( String userName );
	public ReportTO addNewDashboard(String reportName, String reportDescription, String reportType,
			String reportUri, String assessmentType, String reportStatus, String[] userRoles)throws Exception;
}
