package com.ctb.prism.login.transferobject;

import java.io.Serializable;
import java.util.Date;

public class PwdHistory implements Serializable 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date date;

    private String pwd;

    public Date getDate ()
    {
        return date;
    }

    public void setDate (Date date)
    {
        this.date = date;
    }

    public String getPwd ()
    {
        return pwd;
    }

    public void setPwd (String Pwd)
    {
        this.pwd = Pwd;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Date = "+date+", Pwd = "+pwd+"]";
    }
}