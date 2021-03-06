package com.ctb.prism.login.transferobject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ProjectProp implements Serializable
{
    private String staticPdfLocation;

    private String pwdHistoryDay;

    private String orglvlUserNotAdded;

    private String pwdExpiryWarning;

    private String[] roleNotAdded;

    private String orglvlAdminNotAdded;

    private String titleTabHomeApplication;

    private String pwdExpiry;

    private String titleTabApplication;
    
    private SSO sso;
    
    private Messages[] messages;
    
    
    public Map<String, Object> getProperties() {
    	Map<String, Object> propertyMap = new HashMap<String, Object>();
		
		propertyMap.put("static.pdf.location", getStaticPdfLocation());
		propertyMap.put("password.history.day", getPwdHistoryDay());
		propertyMap.put("orglvl.user.not.added", getOrglvlUserNotAdded());
		propertyMap.put("password.expiry.warning", getPwdExpiryWarning());
		propertyMap.put("role.not.added", StringUtils.join(getRoleNotAdded(), ','));
		propertyMap.put("orglvl.admin.not.added", getOrglvlAdminNotAdded());
		propertyMap.put("title.tab.home.application", getTitleTabHomeApplication());
		propertyMap.put("password.expiry", getPwdExpiry());
		propertyMap.put("title.tab.application", getTitleTabApplication());
		
		//OAS SSO
		SSOProperties ssoPropOAS = getSso().getOas();
		if(ssoPropOAS != null) {
			propertyMap.put("sso.redirect.loginfail~"+ssoPropOAS.getSsoSource(), ssoPropOAS.getSsoRedirectLoginfail());
			propertyMap.put("sso.redirect.logout~"+ssoPropOAS.getSsoSource(), ssoPropOAS.getSsoRedirectLogout());
			propertyMap.put("hmac.secret.key~"+ssoPropOAS.getSsoSource(), ssoPropOAS.getHmacSecretKey());
		}
		//ER Web service
		SSOProperties ssoPropER = getSso().getEresource();
		if(ssoPropER != null) {
			propertyMap.put("hmac.secret.key~"+ssoPropER.getSsoSource(), ssoPropER.getHmacSecretKey());
		}
		//DRC SSO
		SSOProperties ssoPropDRC = getSso().getDrc();
		if(ssoPropDRC != null) {
			propertyMap.put("sso.redirect.loginfail~"+ssoPropDRC.getSsoSource(), ssoPropDRC.getSsoRedirectLoginfail());
			propertyMap.put("sso.redirect.logout~"+ssoPropDRC.getSsoSource(), ssoPropDRC.getSsoRedirectLogout());
			propertyMap.put("hmac.secret.key~"+ssoPropDRC.getSsoSource(), ssoPropDRC.getHmacSecretKey());
		}
		
		return propertyMap;
    }

	public Messages[] getMessages() {
		return messages;
	}

	public void setMessages(Messages[] messages) {
		this.messages = messages;
	}

	public String getStaticPdfLocation() {
		return staticPdfLocation;
	}

	public void setStaticPdfLocation(String staticPdfLocation) {
		this.staticPdfLocation = staticPdfLocation;
	}

	public String getPwdHistoryDay() {
		return pwdHistoryDay;
	}

	public void setPwdHistoryDay(String pwdHistoryDay) {
		this.pwdHistoryDay = pwdHistoryDay;
	}

	public String getOrglvlUserNotAdded() {
		return orglvlUserNotAdded;
	}

	public void setOrglvlUserNotAdded(String orglvlUserNotAdded) {
		this.orglvlUserNotAdded = orglvlUserNotAdded;
	}

	public String getPwdExpiryWarning() {
		return pwdExpiryWarning;
	}

	public void setPwdExpiryWarning(String pwdExpiryWarning) {
		this.pwdExpiryWarning = pwdExpiryWarning;
	}

	public String[] getRoleNotAdded() {
		return roleNotAdded;
	}

	public void setRoleNotAdded(String[] roleNotAdded) {
		this.roleNotAdded = roleNotAdded;
	}

	public String getOrglvlAdminNotAdded() {
		return orglvlAdminNotAdded;
	}

	public void setOrglvlAdminNotAdded(String orglvlAdminNotAdded) {
		this.orglvlAdminNotAdded = orglvlAdminNotAdded;
	}

	public String getTitleTabHomeApplication() {
		return titleTabHomeApplication;
	}

	public void setTitleTabHomeApplication(String titleTabHomeApplication) {
		this.titleTabHomeApplication = titleTabHomeApplication;
	}

	public String getPwdExpiry() {
		return pwdExpiry;
	}

	public void setPwdExpiry(String pwdExpiry) {
		this.pwdExpiry = pwdExpiry;
	}

	public String getTitleTabApplication() {
		return titleTabApplication;
	}

	public void setTitleTabApplication(String titleTabApplication) {
		this.titleTabApplication = titleTabApplication;
	}

	public SSO getSso() {
		return sso;
	}

	public void setSso(SSO sso) {
		this.sso = sso;
	}
    
   
}
