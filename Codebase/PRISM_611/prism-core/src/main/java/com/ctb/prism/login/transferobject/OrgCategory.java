package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class OrgCategory implements Serializable
{
    private String category;

    private String level;

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String Category)
    {
        this.category = Category;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String Level)
    {
        this.level = Level;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Category = "+category+", Level = "+level+"]";
    }
}