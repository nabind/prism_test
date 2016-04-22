package com.ctb.prism.login.transferobject;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document
public class MReportAccessTO extends BaseTO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String org_level;
	
	private String activationStatus;
	
	private String customerCode;
	
	private MActionAccessTO[] actionAccess;
	
	private String reportSequence;
	
	private String role;
	
	public String getOrg_level ()
	{
	    return org_level;
	}
	
	public void setOrg_level (String org_level)
	{
	    this.org_level = org_level;
	}
	
	public String getActivationStatus ()
	{
	    return activationStatus;
	}
	
	public void setActivationStatus (String activationStatus)
	{
	    this.activationStatus = activationStatus;
	}
	
	public String getCustomerCode ()
	{
	    return customerCode;
	}
	
	public void setCustomerCode (String customerCode)
	{
	    this.customerCode = customerCode;
	}
	
	public MActionAccessTO[] getActionAccess ()
	{
	    return actionAccess;
	}
	
	public void setActionAccess (MActionAccessTO[] actionAccess)
	{
	    this.actionAccess = actionAccess;
	}
	
	public String getReportSequence ()
	{
	    return reportSequence;
	}
	
	public void setReportSequence (String reportSequence)
	{
	    this.reportSequence = reportSequence;
	}
	
	public String getRole ()
	{
	    return role;
	}
	
	public void setRole (String role)
	{
	    this.role = role;
	}
	
	@Override
	public String toString()
	{
	    return "ClassPojo [org_level = "+org_level+", activationStatus = "+activationStatus+", customerCode = "+customerCode+", actionAccess = "+actionAccess+", reportSequence = "+reportSequence+", role = "+role+"]";
	}
}
