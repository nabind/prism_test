package com.ctb.prism.login.transferobject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProjectProp implements Serializable
{
    private String StaticPdfLocation;

    private String PasswordHistoryDay;

    private String OrglvlUserNotAdded;

    private String PasswordExpiryWarning;

    private String RoleNotAdded;

    private String OrglvlAdminNotAdded;

    private String TitleTabHomeApplication;

    private String PasswordExpiry;

    private String TitleTabApplication;
    
    private SSO SSO;
    
    public Map<String, Object> getProperties() {
    	Map<String, Object> propertyMap = new HashMap<String, Object>();
		
		propertyMap.put("static.pdf.location", getStaticPdfLocation());
		propertyMap.put("password.history.day", getPasswordHistoryDay());
		propertyMap.put("orglvl.user.not.added", getOrglvlUserNotAdded());
		propertyMap.put("password.expiry.warning", getPasswordExpiryWarning());
		propertyMap.put("role.not.added", getRoleNotAdded());
		propertyMap.put("orglvl.admin.not.added", getOrglvlAdminNotAdded());
		propertyMap.put("title.tab.home.application", getTitleTabHomeApplication());
		propertyMap.put("password.expiry", getPasswordExpiry());
		propertyMap.put("title.tab.application", getTitleTabApplication());
		
		//OAS SSO
		SSOProperties ssoPropOAS = getSSO().getOAS();
		if(ssoPropOAS != null) {
			propertyMap.put("sso.redirect.loginfail~"+ssoPropOAS.getSSOSource(), ssoPropOAS.getSsoRedirectLoginfail());
			propertyMap.put("sso.redirect.logout~"+ssoPropOAS.getSSOSource(), ssoPropOAS.getSsoRedirectLogout());
			propertyMap.put("hmac.secret.key~"+ssoPropOAS.getSSOSource(), ssoPropOAS.getHmacSecretKey());
		}
		//ER Web service
		SSOProperties ssoPropER = getSSO().getERESOURCE();
		if(ssoPropER != null) {
			propertyMap.put("hmac.secret.key~"+ssoPropER.getSSOSource(), ssoPropER.getHmacSecretKey());
		}
		//DRC SSO
		SSOProperties ssoPropDRC = getSSO().getDRC();
		if(ssoPropDRC != null) {
			propertyMap.put("sso.redirect.loginfail~"+ssoPropDRC.getSSOSource(), ssoPropDRC.getSsoRedirectLoginfail());
			propertyMap.put("sso.redirect.logout~"+ssoPropDRC.getSSOSource(), ssoPropDRC.getSsoRedirectLogout());
			propertyMap.put("hmac.secret.key~"+ssoPropDRC.getSSOSource(), ssoPropDRC.getHmacSecretKey());
		}
		
		return propertyMap;
    }
    
    public String getStaticPdfLocation ()
    {
        return StaticPdfLocation;
    }

    public void setStaticPdfLocation (String StaticPdfLocation)
    {
        this.StaticPdfLocation = StaticPdfLocation;
    }

    public String getPasswordHistoryDay ()
    {
        return PasswordHistoryDay;
    }

    public void setPasswordHistoryDay (String PasswordHistoryDay)
    {
        this.PasswordHistoryDay = PasswordHistoryDay;
    }

    public String getOrglvlUserNotAdded ()
    {
        return OrglvlUserNotAdded;
    }

    public void setOrglvlUserNotAdded (String OrglvlUserNotAdded)
    {
        this.OrglvlUserNotAdded = OrglvlUserNotAdded;
    }

    public String getPasswordExpiryWarning ()
    {
        return PasswordExpiryWarning;
    }

    public void setPasswordExpiryWarning (String PasswordExpiryWarning)
    {
        this.PasswordExpiryWarning = PasswordExpiryWarning;
    }

    public SSO getSSO ()
    {
        return SSO;
    }

    public void setSSO (SSO SSO)
    {
        this.SSO = SSO;
    }

    public String getRoleNotAdded ()
    {
        return RoleNotAdded;
    }

    public void setRoleNotAdded (String RoleNotAdded)
    {
        this.RoleNotAdded = RoleNotAdded;
    }

    public String getOrglvlAdminNotAdded ()
    {
        return OrglvlAdminNotAdded;
    }

    public void setOrglvlAdminNotAdded (String OrglvlAdminNotAdded)
    {
        this.OrglvlAdminNotAdded = OrglvlAdminNotAdded;
    }

    public String getTitleTabHomeApplication ()
    {
        return TitleTabHomeApplication;
    }

    public void setTitleTabHomeApplication (String TitleTabHomeApplication)
    {
        this.TitleTabHomeApplication = TitleTabHomeApplication;
    }

    public String getPasswordExpiry ()
    {
        return PasswordExpiry;
    }

    public void setPasswordExpiry (String PasswordExpiry)
    {
        this.PasswordExpiry = PasswordExpiry;
    }

    public String getTitleTabApplication ()
    {
        return TitleTabApplication;
    }

    public void setTitleTabApplication (String TitleTabApplication)
    {
        this.TitleTabApplication = TitleTabApplication;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [StaticPdfLocation = "+StaticPdfLocation+", PasswordHistoryDay = "+PasswordHistoryDay+", OrglvlUserNotAdded = "+OrglvlUserNotAdded+", PasswordExpiryWarning = "+PasswordExpiryWarning+", SSO = "+SSO+", RoleNotAdded = "+RoleNotAdded+", OrglvlAdminNotAdded = "+OrglvlAdminNotAdded+", TitleTabHomeApplication = "+TitleTabHomeApplication+", PasswordExpiry = "+PasswordExpiry+", TitleTabApplication = "+TitleTabApplication+"]";
    }
}
