package com.ctb.prism.report.service;

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

@Service("reportService")
public class ReportServiceImpl implements IReportService {

	@Autowired
	private IReportBusiness reportBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getFilledReport(net.sf. jasperreports.engine.JasperReport, java.util.Map)
	 */
	public JasperPrint getFilledReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
		return reportBusiness.getFilledReport(jasperReport, parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#removeReportCache()
	 */
	public void removeReportCache() {
		reportBusiness.removeReportCache();
	}
	
	public void removeConfigurationCache() {
		reportBusiness.removeConfigurationCache();
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
		reportBusiness.removeCache();
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
	public Object getDefaultFilter(List<InputControlTO> tos, String userName, String customerId, String assessmentId, String combAssessmentId, String reportUrl, Map<String, Object> sessionParams, String userId) {
		return reportBusiness.getDefaultFilter(tos, userName, customerId, assessmentId, combAssessmentId, reportUrl, sessionParams, userId);
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
	 * @see com.ctb.prism.report.service.IReportService#getValuesOfSingleInput(java .lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.lang.Object)
	 */
	public List<ObjectValueTO> getValuesOfSingleInput(String query, String userName, String customerId, String changedObject, String changedValue, Map<String, String> replacableParams, Object clazz)
			throws SystemException {
		return reportBusiness.getValuesOfSingleInput(query, userName, customerId, changedObject, changedValue, replacableParams, clazz);
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
	 * @see com.ctb.prism.report.service.IReportService#updateReport(java.lang.String , java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean updateReport(String reportId, String reportName, String reportUrl, String isEnabled, String[] roles) {
		return reportBusiness.updateReport(reportId, reportName, reportUrl, isEnabled, roles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#updateReportNew(com.ctb.prism .report.transferobject.ReportTO)
	 */
	public boolean updateReportNew(ReportTO reportTO) {
		return reportBusiness.updateReportNew(reportTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#deleteReport(java.lang.String )
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteReport(String reportId) throws SystemException {
		return reportBusiness.deleteReport(reportId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAssessments(boolean)
	 */
	public List<AssessmentTO> getAssessments(boolean parentReports) {
		return reportBusiness.getAssessments(parentReports);
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#deleteScheduledGroupFiles (java.lang.String)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#deleteScheduledGroupFiles (java.lang.String)
	 */
	public void deleteScheduledGroupFiles(String gdfExpiryTime) throws Exception {
		reportBusiness.deleteScheduledGroupFiles(gdfExpiryTime);
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
	public JobTrackingTO getProcessDataGD(String processId) {
		return reportBusiness.getProcessDataGD(processId);
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
	public String getRequestSummary(String requestDetails) {
		return reportBusiness.getRequestSummary(requestDetails);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.report.service.IReportService#getAllReportMessages(java.util.Map)
	 */
	public List<ReportMessageTO> getAllReportMessages(Map<String, Object> paramMap) {
		return reportBusiness.getAllReportMessages(paramMap);
	}
}
