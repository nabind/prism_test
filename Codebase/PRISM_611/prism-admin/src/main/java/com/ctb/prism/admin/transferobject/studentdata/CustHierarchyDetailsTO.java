package com.ctb.prism.admin.transferobject.studentdata;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class CustHierarchyDetailsTO {

	@XStreamAsAttribute
	@XStreamAlias("Customer_ID")
	private String customerId;

	@XStreamAsAttribute
	@XStreamAlias("Max_Hierarchy")
	private String maxHierarchy;

	@XStreamAsAttribute
	@XStreamAlias("IsDataChange")
	private boolean dataChanged = false;

	@XStreamImplicit
	private List<OrgDetailsTO> collOrgDetailsTO;

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
