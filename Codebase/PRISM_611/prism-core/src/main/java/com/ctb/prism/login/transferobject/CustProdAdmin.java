package com.ctb.prism.login.transferobject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document(collection = "CustProdAdmin_Info")
public class CustProdAdmin extends BaseTO
{
	@Id
    private String _id;
	
    private Customers[] Customers;
    
    private ProjectProp ProjectProp;

    private String ProjectName;

    private String CreatedDate;

    public Customers[] getCustomers ()
    {
        return Customers;
    }

    public void setCustomers (Customers[] Customers)
    {
        this.Customers = Customers;
    }

    public ProjectProp getProjectProp() {
		return ProjectProp;
	}

	public void setProjectProp(ProjectProp projectProp) {
		ProjectProp = projectProp;
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
