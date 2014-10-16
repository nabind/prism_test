package com.ctb.prism.report.transferobject;

import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

public class ActionTO extends BaseTO {
	private static final long serialVersionUID = 1L;

	private String reportId;
	private String reportName;
	private List<ObjectValueTO> custProdLinkList;
	private List<ObjectValueTO> roleList;
	private List<ObjectValueTO> levelList;
	private List<ObjectValueTO> actionList;

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

	public List<ObjectValueTO> getCustProdLinkList() {
		return custProdLinkList;
	}

	public void setCustProdLinkList(List<ObjectValueTO> custProdLinkList) {
		this.custProdLinkList = custProdLinkList;
	}

	public List<ObjectValueTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<ObjectValueTO> roleList) {
		this.roleList = roleList;
	}

	public List<ObjectValueTO> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<ObjectValueTO> levelList) {
		this.levelList = levelList;
	}

	public List<ObjectValueTO> getActionList() {
		return actionList;
	}

	public void setActionList(List<ObjectValueTO> actionList) {
		this.actionList = actionList;
	}

}
