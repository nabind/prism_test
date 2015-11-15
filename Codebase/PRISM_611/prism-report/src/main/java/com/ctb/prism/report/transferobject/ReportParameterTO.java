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
public class ReportParameterTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	
	private long reportId;
	private String reportName;
	private String reportUrl; 
	private String reportType;
	private String reportStatus;
	private boolean enabled;
	private List<IApplicationConstants.ROLE_TYPE>/*List<IApplicationConstants.USER_TYPE>*/ roles = new ArrayList<IApplicationConstants.ROLE_TYPE>();//new ArrayList<IApplicationConstants.USER_TYPE>();
	private String assessmentName;
	private Long linkName;
	private String allOrgNode;
	private String reportDescription;
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
	private List<ObjectValueTO> objectList;
	private List<ObjectValueTO> objectList2;
	private List<ObjectValueTO> customerProductList;
	private List<ObjectValueTO> orgNodeLevelList;
	private List<ObjectValueTO> adminYear;
	private String customerId;
	private String[] customerProductArr;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	private String[] userRoles;
	private String[] orgNodeLevel;
	
	private String reportApiUrl;
	private String customUrl;
	private String refreshButtonClass;
	private String scrolling;
	private String menuId;
	private String menuName;
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public String getScrolling() {
		return (scrolling == null)? "no" : scrolling;
	}
	public void setScrolling(String scrolling) {
		this.scrolling = scrolling;
	}
	public String getRefreshButtonClass() {
		return (refreshButtonClass == null)? "reportButton" : refreshButtonClass;
	}
	public void setRefreshButtonClass(String refreshButtonClass) {
		this.refreshButtonClass = refreshButtonClass;
	}
	public String getCustomUrl() {
		return customUrl;
	}
	public void setCustomUrl(String customUrl) {
		this.customUrl = customUrl;
	}
	public String getReportApiUrl() {
		return (reportApiUrl != null && !"".equals(reportApiUrl)) ? reportApiUrl : IApplicationConstants.REPORT_API_URL;
	}
	public void setReportApiUrl(String reportApiUrl) {
		this.reportApiUrl = reportApiUrl;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public List<ObjectValueTO> getObjectList2() {
		return objectList2;
	}
	public void setObjectList2(List<ObjectValueTO> objectList2) {
		this.objectList2 = objectList2;
	}
	public List<ObjectValueTO> getObjectList() {
		return objectList;
	}
	public void setObjectList(List<ObjectValueTO> objectList) {
		this.objectList = objectList;
	}
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
		/*if(reportUrl != null && reportUrl.indexOf("|") != -1) {
			setCustomUrl(reportUrl.substring(reportUrl.indexOf("|")+1, reportUrl.length()));
			this.reportUrl = reportUrl.substring(0, reportUrl.indexOf("|"));
		} else {*/
			this.reportUrl = reportUrl;
		/*}*/
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
	public Long getLinkName() {
		return linkName;
	}
	public void setLinkName(Long linkName) {
		this.linkName = linkName;
	}
	public List<ObjectValueTO> getCustomerProductList() {
		return customerProductList;
	}
	public void setCustomerProductList(List<ObjectValueTO> customerProductList) {
		this.customerProductList = customerProductList;
	}
	public List<ObjectValueTO> getOrgNodeLevelList() {
		return orgNodeLevelList;
	}
	public void setOrgNodeLevelList(List<ObjectValueTO> orgNodeLevelList) {
		this.orgNodeLevelList = orgNodeLevelList;
	}
	public List<ObjectValueTO> getAdminYear() {
		return adminYear;
	}
	public void setAdminYear(List<ObjectValueTO> adminYear) {
		this.adminYear = adminYear;
	}
	public String getAllOrgNode() {
		return allOrgNode;
	}
	public void setAllOrgNode(String allOrgNode) {
		this.allOrgNode = allOrgNode;
	}
	public String getReportDescription() {
		return reportDescription;
	}
	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String[] getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(String[] userRoles) {
		this.userRoles = userRoles;
	}
	public String[] getOrgNodeLevel() {
		return orgNodeLevel;
	}
	public void setOrgNodeLevel(String[] orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}
	public String[] getCustomerProductArr() {
		return customerProductArr;
	}
	public void setCustomerProductArr(String[] customerProductArr) {
		this.customerProductArr = customerProductArr;
	}
}
