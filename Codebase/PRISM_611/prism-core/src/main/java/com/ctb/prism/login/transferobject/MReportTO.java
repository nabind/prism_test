package com.ctb.prism.login.transferobject;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document(collection="reports")
public class MReportTO extends BaseTO {
	
	@Id
	private String _id;
	private String reportName;
	private String reportFolderURI;
	private String reportType;
	private String activationStatus;
	private String menu;
	private List<MReportAccess> reportAccess;
	
	
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
	public List<MReportAccess> getReportAccess() {
		return reportAccess;
	}
	public void setReportAccess(List<MReportAccess> reportAccess) {
		this.reportAccess = reportAccess;
	}
	
}
