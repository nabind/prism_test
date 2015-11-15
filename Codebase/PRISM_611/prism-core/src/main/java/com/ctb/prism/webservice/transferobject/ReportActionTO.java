package com.ctb.prism.webservice.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * 
 * @author 796763
 *
 */
public class ReportActionTO extends BaseTO {
	private static final long serialVersionUID = 1L;

	private String reportId;
	private String reportName;

	private String custProdId;
	private String productName;

	private String roleId;
	private String roleName;

	private String levelId;
	private String levelName;

	private String actionId;
	private String actionName;

	private String activationStatus;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getCustProdId() {
		return custProdId;
	}

	public void setCustProdId(String custProdId) {
		this.custProdId = custProdId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

}
