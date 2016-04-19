package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class PwdHistory extends BaseTO 
{
    private String Date;

    private String Pwd;

    public String getDate ()
    {
        return Date;
    }

    public void setDate (String Date)
    {
        this.Date = Date;
    }

    public String getPwd ()
    {
        return Pwd;
    }

    public void setPwd (String Pwd)
    {
        this.Pwd = Pwd;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Date = "+Date+", Pwd = "+Pwd+"]";
    }
}