package com.ctb.prism.login.transferobject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document
public class MFlatReportsTO  extends BaseTO{
	
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String _id;
	private String reportName;
	private String reportFolderURI;
	private String reportType;
	private String activationStatus;
	private String menu;
	private String menuSequence;
	
	public String getMenuSequence() {
		return menuSequence;
	}
	public void setMenuSequence(String menuSequence) {
		this.menuSequence = menuSequence;
	}
	private MReportAccess reportAccess;
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportFolderURI() {
		return reportFolderURI;
	}
	public void setReportFolderURI(String reportFolderURI) {
		this.reportFolderURI = reportFolderURI;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getActivationStatus() {
		return activationStatus;
	}
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public MReportAccess getReportAccess() {
		return reportAccess;
	}
	public void setReportAccess(MReportAccess reportAccess) {
		this.reportAccess = reportAccess;
	}
	
}
