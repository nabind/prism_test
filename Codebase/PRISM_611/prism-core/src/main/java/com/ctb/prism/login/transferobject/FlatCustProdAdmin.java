package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class FlatCustProdAdmin implements Serializable
{
    private String _id;
	
    private FlatCustomers Customers;
    
    private ProjectProp ProjectProp;

    private String ProjectName;

    private String CreatedDate;

    public FlatCustomers getCustomers ()
    {
        return Customers;
    }

    public ProjectProp getProjectProp() {
		return ProjectProp;
	}

	public void setProjectProp(ProjectProp projectProp) {
		ProjectProp = projectProp;
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
