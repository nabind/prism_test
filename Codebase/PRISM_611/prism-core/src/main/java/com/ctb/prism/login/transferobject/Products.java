package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class Products extends BaseTO 
{
    private String Name;

    private String Seq;

    private String FileLoc;

    private String Code;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getSeq ()
    {
        return Seq;
    }

    public void setSeq (String Seq)
    {
        this.Seq = Seq;
    }

    public String getFileLoc ()
    {
        return FileLoc;
    }

    public void setFileLoc (String FileLoc)
    {
        this.FileLoc = FileLoc;
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
        return "ClassPojo [Name = "+Name+", Seq = "+Seq+", FileLoc = "+FileLoc+", Code = "+Code+"]";
    }
}
