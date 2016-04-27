package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class MFlatActionAccessTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private MActionAccessTO actionAccess;

    private String reportName;

    public MActionAccessTO getActionAccess ()
    {
        return actionAccess;
    }

    public void setActionAccess (MActionAccessTO actionAccess)
    {
        this.actionAccess = actionAccess;
    }

    public String getReportName ()
    {
        return reportName;
    }

    public void setReportName (String reportName)
    {
        this.reportName = reportName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [actionAccess = "+actionAccess+", reportName = "+reportName+"]";
    }
}
