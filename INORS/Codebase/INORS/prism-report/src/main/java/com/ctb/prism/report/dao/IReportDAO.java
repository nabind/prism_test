package com.ctb.prism.report.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownload;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
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
	public List<InputControlTO> getAllInputControls();
	
	public List<ObjectValueTO> getValuesOfSingleInput(String query);
	
	
	public List<ReportTO> getAllReportList(Map<String,Object> paramMap);
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles );
	public boolean updateReportNew(ReportTO reportTO);
	public boolean deleteReport(String reportId)throws SystemException;
	
	List<AssessmentTO> getAssessments(boolean parentReports);
	
	
	public String getTenantId( String userName );
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO)throws Exception;
	public List<ObjectValueTO> getAllRoles();
	public List<ObjectValueTO> getAllAssessments();
	public ReportTO getReport(String reportIdentifier);
	public List<ManageMessageTO> loadManageMessage(final Map<String,Object> paramMap) throws SystemException;
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getAdminYear(final Map<String,Object> paramMap)throws SystemException;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getCustomerProduct(final Map<String,Object> paramMap)throws SystemException;
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> getOrgNodeLevel(final Map<String,Object> paramMap)throws SystemException;
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String,Object> paramMap) throws SystemException;
	public List<JobTrackingTO> getRequestDetail(Map<String,Object> paramMap) throws SystemException;
	public boolean deleteGroupFiles(String Id) throws Exception;
}
