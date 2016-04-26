package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class SSOProperties implements Serializable
{
	private String ssoSource;
	
	private String hmacSecretKey;

    private String ssoRedirectLogout;

    private String ssoRedirectLoginfail;

	public String getSsoSource() {
		return ssoSource;
	}

	public void setSsoSource(String ssoSource) {
		this.ssoSource = ssoSource;
	}

	public String getHmacSecretKey() {
		return hmacSecretKey;
	}

	public void setHmacSecretKey(String hmacSecretKey) {
		this.hmacSecretKey = hmacSecretKey;
	}

	public String getSsoRedirectLogout() {
		return ssoRedirectLogout;
	}

	public void setSsoRedirectLogout(String ssoRedirectLogout) {
		this.ssoRedirectLogout = ssoRedirectLogout;
	}

	public String getSsoRedirectLoginfail() {
		return ssoRedirectLoginfail;
	}

	public void setSsoRedirectLoginfail(String ssoRedirectLoginfail) {
		this.ssoRedirectLoginfail = ssoRedirectLoginfail;
	}

    
}
