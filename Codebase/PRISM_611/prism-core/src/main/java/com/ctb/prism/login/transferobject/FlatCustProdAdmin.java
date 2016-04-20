package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class FlatCustProdAdmin extends BaseTO
{
    private String _id;
	
    private FlatCustomers Customers;

    private String ProjectName;

    private String CreatedDate;

    public FlatCustomers getCustomers ()
    {
        return Customers;
    }

    public void setCustomers (FlatCustomers Customers)
    {
        this.Customers = Customers;
    }

    public String getProjectName ()
    {
        return ProjectName;
    }

    public void setProjectName (String ProjectName)
    {
        this.ProjectName = ProjectName;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getCreatedDate ()
    {
        return CreatedDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Customers = "+Customers+", ProjectName = "+ProjectName+", _id = "+_id+", CreatedDate = "+CreatedDate+"]";
    }
}
