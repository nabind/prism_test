package com.ctb.prism.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.business.IReportBusiness;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownload;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
//import com.ctb.prism.report.transferobject.ReportFilterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;

@Service("reportService")
public class ReportServiceImpl implements IReportService {

	@Autowired
	private IReportBusiness reportBusiness;
	
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		return reportBusiness.getFilledReport(jasperReport, parameters);
	}
	
	public void removeReportCache() {
		reportBusiness.removeReportCache();
	}
	public void removeCache() {
		reportBusiness.removeCache();
	}
	public JasperReport getReportJasperObject(String reportPath) {
		return reportBusiness.getReportJasperObject(reportPath);
	}
	public JasperReport getReportJasperObjectForName(String reportname) {
		return reportBusiness.getReportJasperObjectForName(reportname);
	}
	public List<ReportTO> getReportJasperObjectList(final String reportPath) throws JRException {
		return reportBusiness.getReportJasperObjectList(reportPath);
	}

	public List<InputControlTO> getInputControlDetails(String reportPath) {
		return reportBusiness.getInputControlDetails(reportPath);
	}
	public List<InputControlTO> getAllInputControls() {
		return reportBusiness.getAllInputControls();
	}
	
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String assessmentId, String combAssessmentId,
			String reportUrl ) {
		return reportBusiness.getDefaultFilter(tos, userName, assessmentId, combAssessmentId, reportUrl );
	}
	
	public List<ObjectValueTO> getValuesOfSingleInput(String query) {
		return reportBusiness.getValuesOfSingleInput(query);
	}
	
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String changedObject, 
			String changedValue, Map<String, String> replacableParams, Object clazz) throws SystemException {
		return reportBusiness.getValuesOfSingleInput(query, userName, changedObject, changedValue, replacableParams, clazz);
	}
	public List<ReportTO> getAllReportList(Map<String,Object> paramMap) {
		return reportBusiness.getAllReportList(paramMap);
	}
	

	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean updateReport(String reportId, String reportName,
			String reportUrl, String isEnabled, String[] roles) {
		return reportBusiness.updateReport(reportId, reportName, reportUrl, isEnabled, roles);
	}
	
	public boolean updateReportNew(ReportTO reportTO)
	{
		return reportBusiness.updateReportNew(reportTO);
	}
	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean deleteReport(String reportId) throws SystemException {
		return reportBusiness.deleteReport(reportId);
	}
	
	public List<AssessmentTO> getAssessments(boolean parentReports)
	{
		return reportBusiness.getAssessments(parentReports);
	}
	public ReportTO addNewDashboard(ReportParameterTO reportParameterTO)throws Exception{
		return reportBusiness.addNewDashboard(reportParameterTO);
	}
	public ReportTO getReport(String reportIdentifier) throws SystemException {
		return reportBusiness.getReport(reportIdentifier);
	}
	
	public Map<String,Object> loadManageMessage(final Map<String,Object> paramMap) throws SystemException{
		return reportBusiness.loadManageMessage(paramMap);
	}
	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public int saveManageMessage(final List<ManageMessageTO> manageMessageTOList) throws SystemException{
		return reportBusiness.saveManageMessage(manageMessageTOList);
	}
	
	public List<JobTrackingTO> getAllGroupDownloadFiles(Map<String,Object> paramMap)throws SystemException{
		return reportBusiness.getAllGroupDownloadFiles(paramMap);
	}
	public List<JobTrackingTO> getRequestDetail(Map<String,Object> paramMap)throws SystemException{
		return reportBusiness.getRequestDetail(paramMap);
	}
	
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public boolean deleteGroupFiles(String Id) throws Exception {

		return reportBusiness.deleteGroupFiles(Id);
	}
	
	public Map<String,Object> getReportMessageFilter(final Map<String,Object> paramMap) throws SystemException{
		return reportBusiness.getReportMessageFilter(paramMap);
	}
	
	

}
