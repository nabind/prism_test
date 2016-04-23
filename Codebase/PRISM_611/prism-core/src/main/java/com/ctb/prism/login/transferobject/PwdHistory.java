package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class PwdHistory implements Serializable 
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