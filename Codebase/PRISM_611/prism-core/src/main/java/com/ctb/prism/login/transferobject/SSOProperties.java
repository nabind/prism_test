package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class SSOProperties implements Serializable
{
	private String SSOSource;
	
	private String HmacSecretKey;

    private String SsoRedirectLogout;

    private String SsoRedirectLoginfail;

    public String getSSOSource() {
		return SSOSource;
	}

	public void setSSOSource(String sSOSource) {
		SSOSource = sSOSource;
	}

	public String getHmacSecretKey ()
    {
        return HmacSecretKey;
    }

    public void setHmacSecretKey (String HmacSecretKey)
    {
        this.HmacSecretKey = HmacSecretKey;
    }

    public String getSsoRedirectLogout ()
    {
        return SsoRedirectLogout;
    }

    public void setSsoRedirectLogout (String SsoRedirectLogout)
    {
        this.SsoRedirectLogout = SsoRedirectLogout;
    }

    public String getSsoRedirectLoginfail ()
    {
        return SsoRedirectLoginfail;
    }

    public void setSsoRedirectLoginfail (String SsoRedirectLoginfail)
    {
        this.SsoRedirectLoginfail = SsoRedirectLoginfail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [HmacSecretKey = "+HmacSecretKey+", SsoRedirectLogout = "+SsoRedirectLogout+", SsoRedirectLoginfail = "+SsoRedirectLoginfail+"]";
    }
}
