package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class OrgCategory extends BaseTO 
{
    private String Category;

    private String Level;

    public String getCategory ()
    {
        return Category;
    }

    public void setCategory (String Category)
    {
        this.Category = Category;
    }

    public String getLevel ()
    {
        return Level;
    }

    public void setLevel (String Level)
    {
        this.Level = Level;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Category = "+Category+", Level = "+Level+"]";
    }
}