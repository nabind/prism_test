package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MReportAccessTO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 private String activationStatus;

	    private String customerCode;

	    private String[] roles;

	    private MActionAccessTO[] actionAccess;

	    private String reportSequence;

	    private String orgLevel;

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

	    public String[] getRoles ()
	    {
	        return roles;
	    }

	    public void setRoles (String[] roles)
	    {
	        this.roles = roles;
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

	    public String getOrgLevel ()
	    {
	        return orgLevel;
	    }

	    public void setOrgLevel (String orgLevel)
	    {
	        this.orgLevel = orgLevel;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [activationStatus = "+activationStatus+", customerCode = "+customerCode+", roles = "+roles+", actionAccess = "+actionAccess+", reportSequence = "+reportSequence+", orgLevel = "+orgLevel+"]";
	    }
}
