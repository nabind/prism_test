package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="ReportConfig")
public class MReportConfigTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String reportType;
	
	@Id
    private String _id;

    private MReportMessageTO[] reportMessage;

    private String menu;

    private String reportFolderURI;

    private String project_Id;

    private String menuSequence;

    private String reportName;

    private MReportAccessTO[] reportAccess;

    public String getReportType ()
    {
        return reportType;
    }

    public void setReportType (String reportType)
    {
        this.reportType = reportType;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public MReportMessageTO[] getReportMessage ()
    {
        return reportMessage;
    }

    public void setReportMessage (MReportMessageTO[] reportMessage)
    {
        this.reportMessage = reportMessage;
    }

    public String getMenu ()
    {
        return menu;
    }

    public void setMenu (String menu)
    {
        this.menu = menu;
    }

    public String getReportFolderURI ()
    {
        return reportFolderURI;
    }

    public void setReportFolderURI (String reportFolderURI)
    {
        this.reportFolderURI = reportFolderURI;
    }

    public String getProject_Id ()
    {
        return project_Id;
    }

    public void setProject_Id (String project_Id)
    {
        this.project_Id = project_Id;
    }

    public String getMenuSequence ()
    {
        return menuSequence;
    }

    public void setMenuSequence (String menuSequence)
    {
        this.menuSequence = menuSequence;
    }

    public String getReportName ()
    {
        return reportName;
    }

    public void setReportName (String reportName)
    {
        this.reportName = reportName;
    }

    public MReportAccessTO[] getReportAccess ()
    {
        return reportAccess;
    }

    public void setReportAccess (MReportAccessTO[] reportAccess)
    {
        this.reportAccess = reportAccess;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [reportType = "+reportType+", _id = "+_id+", reportMessage = "+reportMessage+", menu = "+menu+", reportFolderURI = "+reportFolderURI+", project_Id = "+project_Id+", menuSequence = "+menuSequence+", reportName = "+reportName+", reportAccess = "+reportAccess+"]";
    }
	
}
