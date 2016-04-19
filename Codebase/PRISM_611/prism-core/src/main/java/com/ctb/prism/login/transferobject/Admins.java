package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class Admins extends BaseTO 
{
    private String Name;

    private String Seq;

    private Products[] Products;

    private String FileLoc;

    private String IsCurrentAdmin;

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

    public Products[] getProducts ()
    {
        return Products;
    }

    public void setProducts (Products[] Products)
    {
        this.Products = Products;
    }

    public String getFileLoc ()
    {
        return FileLoc;
    }

    public void setFileLoc (String FileLoc)
    {
        this.FileLoc = FileLoc;
    }

    public String getIsCurrentAdmin ()
    {
        return IsCurrentAdmin;
    }

    public void setIsCurrentAdmin (String IsCurrentAdmin)
    {
        this.IsCurrentAdmin = IsCurrentAdmin;
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
        return "ClassPojo [Name = "+Name+", Seq = "+Seq+", Products = "+Products+", FileLoc = "+FileLoc+", IsCurrentAdmin = "+IsCurrentAdmin+", Code = "+Code+"]";
    }
}
