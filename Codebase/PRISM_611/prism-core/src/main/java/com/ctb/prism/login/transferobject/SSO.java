package com.ctb.prism.login.transferobject;

public class SSO
{
    private SSOProperties ERESOURCE;

    private SSOProperties OAS;
    
    private SSOProperties DRC;

    public SSOProperties getERESOURCE ()
    {
        return ERESOURCE;
    }

    public void setERESOURCE (SSOProperties ERESOURCE)
    {
        this.ERESOURCE = ERESOURCE;
    }

    public SSOProperties getOAS ()
    {
        return OAS;
    }

    public void setOAS (SSOProperties OAS)
    {
        this.OAS = OAS;
    }

    public SSOProperties getDRC() {
		return DRC;
	}

	public void setDRC(SSOProperties dRC) {
		DRC = dRC;
	}

}
