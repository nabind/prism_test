package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class PwdHistory implements Serializable 
{
    private String date;

    private String pwd;

    public String getDate ()
    {
        return date;
    }

    public void setDate (String Date)
    {
        this.date = Date;
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