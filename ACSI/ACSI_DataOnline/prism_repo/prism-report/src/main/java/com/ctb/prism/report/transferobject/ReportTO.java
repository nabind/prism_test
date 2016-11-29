package com.ctb.prism.report.transferobject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JasperReport;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.transferobject.BaseTO;

/**
 * Transfer object holds details of a report. Report name, report URL, the roles who can access the report etc.
 */
public class ReportTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	
	private long reportId;
	private String reportName;
	private String reportUrl; 
	private boolean enabled;
	private List<IApplicationConstants.ROLE_TYPE>/*List<IApplicationConstants.USER_TYPE>*/ roles = new ArrayList<IApplicationConstants.ROLE_TYPE>();//new ArrayList<IApplicationConstants.USER_TYPE>();
	private String assessmentName;
	private String allRoles;
	private boolean regularUser;
	private boolean accessDenied;
	private String otherUrl;
	private boolean firstTimeLogin;
	private String tabCount; 
	private String currentTabNumber;
	private JasperReport compiledReport;
	private boolean mainReport;
	private InputStream image;
	private boolean jrxml;
	private String productName;
	private String studentBioId;
	
	public String getStudentBioId() {
		return studentBioId;
	}
	public void setStudentBioId(String studentBioId) {
		this.studentBioId = studentBioId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCurrentTabNumber() {
		return ((currentTabNumber == null || currentTabNumber == "")? "0" : currentTabNumber);
	}
	public void setCurrentTabNumber(String currentTabNumber) {
		this.currentTabNumber = currentTabNumber;
	}
	public boolean isJrxml() {
		return jrxml;
	}
	public void setJrxml(boolean jrxml) {
		this.jrxml = jrxml;
	}
	public InputStream getImage() {
		return image;
	}
	public void setImage(InputStream image) {
		this.image = image;
	}
	public boolean isMainReport() {
		return mainReport;
	}
	public void setMainReport(boolean mainReport) {
		this.mainReport = mainReport;
	}
	public JasperReport getCompiledReport() {
		return compiledReport;
	}
	public void setCompiledReport(JasperReport compiledReport) {
		this.compiledReport = compiledReport;
	}
	public String getTabCount() {
		return ((tabCount == null || tabCount == "")? "0" : tabCount);
	}
	public void setTabCount(String tabCount) {
		this.tabCount = tabCount;
	}
	public boolean isFirstTimeLogin() {
		return firstTimeLogin;
	}
	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}
	public String getOtherUrl() {
		return otherUrl;
	}
	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}
	public boolean isAccessDenied() {
		return accessDenied;
	}
	public void setAccessDenied(boolean accessDenied) {
		this.accessDenied = accessDenied;
	}
	public boolean isRegularUser() {
		return regularUser;
	}
	public void setRegularUser(boolean regularUser) {
		this.regularUser = regularUser;
	}
	public String getAllRoles() {
		return allRoles;
	}
	public void setAllRoles(String allRoles) {
		this.allRoles = allRoles;
	}
	public long getReportId() {
		return reportId;
	}
	public void setReportId(long reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportUrl() {
		return reportUrl;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	public List<IApplicationConstants.ROLE_TYPE> getRoles() {
		return roles;
	}
	public void setRoles(List<IApplicationConstants.ROLE_TYPE> roles) {
		this.roles = roles;
	}
	public void addRole(IApplicationConstants.ROLE_TYPE userRole )
	{
		this.roles.add(userRole);
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getAssessmentName() {
		return assessmentName;
	}
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
}
