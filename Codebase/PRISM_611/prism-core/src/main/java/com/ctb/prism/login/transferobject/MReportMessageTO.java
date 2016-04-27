package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MReportMessageTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String product_id;

    private String teacherLoginPageContent;

    private String embargoNotice;

    private String reportPrivacyNotice;

    private String parentHomeFooter;

    private String commonHeader;

    private String parentLoginOutageContent;

    private String parentLoginPageContent;

    private String customerCode;

    private String blockLogin;

    private String dataloadMessage;

    private String growthHomePage;

    private String menuMessage;

    private String landingPageContent;

    private String gdfHeaderMessage;

    private String teacherHomePage;

    private String commonFooter;

    private String reportMessage;

    private String commonLogIn;

    private String adminCode;

    private String moreInfo;

    private String footNote;

    private String childrenOverview;

    private String parentLoginFooter;

    private String parentHomePage;

    private String gdfNotice;

    private String teacherLoginFooter;

    private String teacherLoginOutageContent;

    private String groupDownloadInstruction;

    private String teacherHomeFooter;

    private String reportPurpose;

    private String reportLegend;

    public String getProduct_id ()
    {
        return product_id;
    }

    public void setProduct_id (String product_id)
    {
        this.product_id = product_id;
    }

    public String getTeacherLoginPageContent ()
    {
        return teacherLoginPageContent;
    }

    public void setTeacherLoginPageContent (String teacherLoginPageContent)
    {
        this.teacherLoginPageContent = teacherLoginPageContent;
    }

    public String getEmbargoNotice ()
    {
        return embargoNotice;
    }

    public void setEmbargoNotice (String embargoNotice)
    {
        this.embargoNotice = embargoNotice;
    }

    public String getReportPrivacyNotice ()
    {
        return reportPrivacyNotice;
    }

    public void setReportPrivacyNotice (String reportPrivacyNotice)
    {
        this.reportPrivacyNotice = reportPrivacyNotice;
    }

    public String getParentHomeFooter ()
    {
        return parentHomeFooter;
    }

    public void setParentHomeFooter (String parentHomeFooter)
    {
        this.parentHomeFooter = parentHomeFooter;
    }

    public String getCommonHeader ()
    {
        return commonHeader;
    }

    public void setCommonHeader (String commonHeader)
    {
        this.commonHeader = commonHeader;
    }

    public String getParentLoginOutageContent ()
    {
        return parentLoginOutageContent;
    }

    public void setParentLoginOutageContent (String parentLoginOutageContent)
    {
        this.parentLoginOutageContent = parentLoginOutageContent;
    }

    public String getParentLoginPageContent ()
    {
        return parentLoginPageContent;
    }

    public void setParentLoginPageContent (String parentLoginPageContent)
    {
        this.parentLoginPageContent = parentLoginPageContent;
    }

    public String getCustomerCode ()
    {
        return customerCode;
    }

    public void setCustomerCode (String customerCode)
    {
        this.customerCode = customerCode;
    }

    public String getBlockLogin ()
    {
        return blockLogin;
    }

    public void setBlockLogin (String blockLogin)
    {
        this.blockLogin = blockLogin;
    }

    public String getDataloadMessage ()
    {
        return dataloadMessage;
    }

    public void setDataloadMessage (String dataloadMessage)
    {
        this.dataloadMessage = dataloadMessage;
    }

    public String getGrowthHomePage ()
    {
        return growthHomePage;
    }

    public void setGrowthHomePage (String growthHomePage)
    {
        this.growthHomePage = growthHomePage;
    }

    public String getMenuMessage ()
    {
        return menuMessage;
    }

    public void setMenuMessage (String menuMessage)
    {
        this.menuMessage = menuMessage;
    }

    public String getLandingPageContent ()
    {
        return landingPageContent;
    }

    public void setLandingPageContent (String landingPageContent)
    {
        this.landingPageContent = landingPageContent;
    }

    public String getGdfHeaderMessage ()
    {
        return gdfHeaderMessage;
    }

    public void setGdfHeaderMessage (String gdfHeaderMessage)
    {
        this.gdfHeaderMessage = gdfHeaderMessage;
    }

    public String getTeacherHomePage ()
    {
        return teacherHomePage;
    }

    public void setTeacherHomePage (String teacherHomePage)
    {
        this.teacherHomePage = teacherHomePage;
    }

    public String getCommonFooter ()
    {
        return commonFooter;
    }

    public void setCommonFooter (String commonFooter)
    {
        this.commonFooter = commonFooter;
    }

    public String getReportMessage ()
    {
        return reportMessage;
    }

    public void setReportMessage (String reportMessage)
    {
        this.reportMessage = reportMessage;
    }

    public String getCommonLogIn ()
    {
        return commonLogIn;
    }

    public void setCommonLogIn (String commonLogIn)
    {
        this.commonLogIn = commonLogIn;
    }

    public String getAdminCode ()
    {
        return adminCode;
    }

    public void setAdminCode (String adminCode)
    {
        this.adminCode = adminCode;
    }

    public String getMoreInfo ()
    {
        return moreInfo;
    }

    public void setMoreInfo (String moreInfo)
    {
        this.moreInfo = moreInfo;
    }

    public String getFootNote ()
    {
        return footNote;
    }

    public void setFootNote (String footNote)
    {
        this.footNote = footNote;
    }

    public String getChildrenOverview ()
    {
        return childrenOverview;
    }

    public void setChildrenOverview (String childrenOverview)
    {
        this.childrenOverview = childrenOverview;
    }

    public String getParentLoginFooter ()
    {
        return parentLoginFooter;
    }

    public void setParentLoginFooter (String parentLoginFooter)
    {
        this.parentLoginFooter = parentLoginFooter;
    }

    public String getParentHomePage ()
    {
        return parentHomePage;
    }

    public void setParentHomePage (String parentHomePage)
    {
        this.parentHomePage = parentHomePage;
    }

    public String getGdfNotice ()
    {
        return gdfNotice;
    }

    public void setGdfNotice (String gdfNotice)
    {
        this.gdfNotice = gdfNotice;
    }

    public String getTeacherLoginFooter ()
    {
        return teacherLoginFooter;
    }

    public void setTeacherLoginFooter (String teacherLoginFooter)
    {
        this.teacherLoginFooter = teacherLoginFooter;
    }

    public String getTeacherLoginOutageContent ()
    {
        return teacherLoginOutageContent;
    }

    public void setTeacherLoginOutageContent (String teacherLoginOutageContent)
    {
        this.teacherLoginOutageContent = teacherLoginOutageContent;
    }

    public String getGroupDownloadInstruction ()
    {
        return groupDownloadInstruction;
    }

    public void setGroupDownloadInstruction (String groupDownloadInstruction)
    {
        this.groupDownloadInstruction = groupDownloadInstruction;
    }

    public String getTeacherHomeFooter ()
    {
        return teacherHomeFooter;
    }

    public void setTeacherHomeFooter (String teacherHomeFooter)
    {
        this.teacherHomeFooter = teacherHomeFooter;
    }

    public String getReportPurpose ()
    {
        return reportPurpose;
    }

    public void setReportPurpose (String reportPurpose)
    {
        this.reportPurpose = reportPurpose;
    }

    public String getReportLegend ()
    {
        return reportLegend;
    }

    public void setReportLegend (String reportLegend)
    {
        this.reportLegend = reportLegend;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [product_id = "+product_id+", teacherLoginPageContent = "+teacherLoginPageContent+", embargoNotice = "+embargoNotice+", reportPrivacyNotice = "+reportPrivacyNotice+", parentHomeFooter = "+parentHomeFooter+", commonHeader = "+commonHeader+", parentLoginOutageContent = "+parentLoginOutageContent+", parentLoginPageContent = "+parentLoginPageContent+", customerCode = "+customerCode+", blockLogin = "+blockLogin+", dataloadMessage = "+dataloadMessage+", growthHomePage = "+growthHomePage+", menuMessage = "+menuMessage+", landingPageContent = "+landingPageContent+", gdfHeaderMessage = "+gdfHeaderMessage+", teacherHomePage = "+teacherHomePage+", commonFooter = "+commonFooter+", reportMessage = "+reportMessage+", commonLogIn = "+commonLogIn+", adminCode = "+adminCode+", moreInfo = "+moreInfo+", footNote = "+footNote+", childrenOverview = "+childrenOverview+", parentLoginFooter = "+parentLoginFooter+", parentHomePage = "+parentHomePage+", gdfNotice = "+gdfNotice+", teacherLoginFooter = "+teacherLoginFooter+", teacherLoginOutageContent = "+teacherLoginOutageContent+", groupDownloadInstruction = "+groupDownloadInstruction+", teacherHomeFooter = "+teacherHomeFooter+", reportPurpose = "+reportPurpose+", reportLegend = "+reportLegend+"]";
    }
}
