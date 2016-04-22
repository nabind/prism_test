package com.ctb.prism.login.transferobject;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document
public class MActionAccessTO extends BaseTO {
	
	private static final long serialVersionUID = 1L;
	private String status;

    private String action;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getAction ()
    {
        return action;
    }

    public void setAction (String action)
    {
        this.action = action;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", action = "+action+"]";
    }
}
