package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customers implements Serializable
{
    private String Name;

    private String FileLoc;

    private String CreatedDate;

    private String SupportEmail;

    private Admins[] Admins;

    private String SendLoginPdf;

    private String Code;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getFileLoc ()
    {
        return FileLoc;
    }

    public void setFileLoc (String FileLoc)
    {
        this.FileLoc = FileLoc;
    }

    public String getCreatedDate ()
    {
        return CreatedDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    public String getSupportEmail ()
    {
        return SupportEmail;
    }

    public void setSupportEmail (String SupportEmail)
    {
        this.SupportEmail = SupportEmail;
    }

    public Admins[] getAdmins ()
    {
        return Admins;
    }

    public void setAdmins (Admins[] Admins)
    {
        this.Admins = Admins;
    }

    public String getSendLoginPdf ()
    {
        return SendLoginPdf;
    }

    public void setSendLoginPdf (String SendLoginPdf)
    {
        this.SendLoginPdf = SendLoginPdf;
    }

    public String getCode ()
    {
        return Code;
    }

    public void setCode (String Code)
    {
        this.Code = Code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+Name+", FileLoc = "+FileLoc+", CreatedDate = "+CreatedDate+", SupportEmail = "+SupportEmail+", Admins = "+Admins+", SendLoginPdf = "+SendLoginPdf+", Code = "+Code+"]";
    }
}
