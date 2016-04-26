package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class SSO implements Serializable
{
    private SSOProperties eresource;

    private SSOProperties oas;
    
    private SSOProperties drc;

	public SSOProperties getEresource() {
		return eresource;
	}

	public void setEresource(SSOProperties eresource) {
		this.eresource = eresource;
	}

	public SSOProperties getOas() {
		return oas;
	}

	public void setOas(SSOProperties oas) {
		this.oas = oas;
	}

	public SSOProperties getDrc() {
		return drc;
	}

	public void setDrc(SSOProperties drc) {
		this.drc = drc;
	}

    

}
