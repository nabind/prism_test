package com.ctb.prism.login.transferobject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;


@Document(collection="Reports")
public class MReportsTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	
	@Id	
	private String _id;
	
	private String reportType;
	
	private MReportMessageTO[] reportMessage;
	
	private String CustProdAdmin_Info_Id;
	
	private String menu;
	
	private String reportFolderURI;
	
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
	
	public String getCustProdAdmin_Info_Id ()
	{
	    return CustProdAdmin_Info_Id;
	}
	
	public void setCustProdAdmin_Info_Id (String CustProdAdmin_Info_Id)
	{
	    this.CustProdAdmin_Info_Id = CustProdAdmin_Info_Id;
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
	    return "ClassPojo [reportType = "+reportType+", _id = "+_id+", reportMessage = "+reportMessage+", CustProdAdmin_Info_Id = "+CustProdAdmin_Info_Id+", menu = "+menu+", reportFolderURI = "+reportFolderURI+", menuSequence = "+menuSequence+", reportName = "+reportName+", reportAccess = "+reportAccess+"]";
	}
	
}
