package com.ctb.prism.webservice.erTransferobject;

import java.io.Serializable;

public class OrgDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477773094826787095L;

	private String cTBCustomerID;
	private String stateCode;
	private String stateName;
	
	public String getcTBCustomerID() {
		return cTBCustomerID;
	}
	public void setcTBCustomerID(String cTBCustomerID) {
		this.cTBCustomerID = cTBCustomerID;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	
}
