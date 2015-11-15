package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;
import java.util.List;

public class CustHierarchyDetailsTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String customerId;
	private String maxHierarchy;
	private boolean dataChanged = false;
	private String testName;
	private List<OrgDetailsTO> collOrgDetailsTO;
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getMaxHierarchy() {
		return maxHierarchy;
	}
	public void setMaxHierarchy(String maxHierarchy) {
		this.maxHierarchy = maxHierarchy;
	}
	public boolean isDataChanged() {
		return dataChanged;
	}
	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}
	public List<OrgDetailsTO> getCollOrgDetailsTO() {
		return collOrgDetailsTO;
	}
	public void setCollOrgDetailsTO(List<OrgDetailsTO> collOrgDetailsTO) {
		this.collOrgDetailsTO = collOrgDetailsTO;
	}
	
	
}
