package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class MenuTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String menuName;
	private String reportName;
	private String reportFolderUri;
	private String menuSequence;
	private String reportSequence;
	private String cssClass;

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportFolderUri() {
		return reportFolderUri;
	}

	public void setReportFolderUri(String reportFolderUri) {
		this.reportFolderUri = reportFolderUri;
	}

	public String getMenuSequence() {
		return menuSequence;
	}

	public void setMenuSequence(String menuSequence) {
		this.menuSequence = menuSequence;
	}

	public String getReportSequence() {
		return reportSequence;
	}

	public void setReportSequence(String reportSequence) {
		this.reportSequence = reportSequence;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((menuName == null) ? 0 : menuName.hashCode());
		result = prime * result
				+ ((reportName == null) ? 0 : reportName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuTO other = (MenuTO) obj;
		if (menuName == null) {
			if (other.menuName != null)
				return false;
		} else if (!menuName.equals(other.menuName))
			return false;
		if (reportName == null) {
			if (other.reportName != null)
				return false;
		} else if (!reportName.equals(other.reportName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MenuTO [menuName=" + menuName + ", reportName=" + reportName
				+ ", reportFolderUri=" + reportFolderUri + ", menuSequence="
				+ menuSequence + ", reportSequence=" + reportSequence
				+ ", cssClass=" + cssClass + "]";
	}

}
