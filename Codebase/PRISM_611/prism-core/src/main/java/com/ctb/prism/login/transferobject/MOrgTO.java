package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Organization")
public class MOrgTO implements Serializable
{
	@Id
	private String _id;
	 
    private String name;

    private String category;

    private String[] adminCodes;

    private String updatedDate;

    private String parent_id;

    private String spCodes;

    private String project_id;

    private String customerCode;

    private String createdDate;

    private String level;

    private String strucElement;

    public String getName ()
    {
        return name;
    }

    public void setName (String Name)
    {
        this.name = Name;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String Category)
    {
        this.category = Category;
    }

    public String[] getAdminCodes ()
    {
        return adminCodes;
    }

    public void setAdminCodes (String[] AdminCodes)
    {
        this.adminCodes = AdminCodes;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getUpdatedDate ()
    {
        return updatedDate;
    }

    public void setUpdatedDate (String UpdatedDate)
    {
        this.updatedDate = UpdatedDate;
    }

    public String getParent_id ()
    {
        return parent_id;
    }

    public void setParent_id (String Parent_id)
    {
        this.parent_id = Parent_id;
    }

    public String getSpCodes ()
    {
        return spCodes;
    }

    public void setSpCodes (String SpCodes)
    {
        this.spCodes = SpCodes;
    }

    public String getProject_id ()
    {
        return project_id;
    }

    public void setProject_id (String Project_id)
    {
        this.project_id = Project_id;
    }

    public String getCustomerCode ()
    {
        return customerCode;
    }

    public void setCustomerCode (String CustomerCode)
    {
        this.customerCode = CustomerCode;
    }

    public String getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.createdDate = CreatedDate;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String Level)
    {
        this.level = Level;
    }

    public String getStrucElement ()
    {
        return strucElement;
    }

    public void setStrucElement (String StrucElement)
    {
        this.strucElement = StrucElement;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+name+", Category = "+category+", AdminCodes = "+adminCodes+", _id = "+_id+", UpdatedDate = "+updatedDate+", Parent_id = "+parent_id+", SpCodes = "+spCodes+", Project_id = "+project_id+", CustomerCode = "+customerCode+", CreatedDate = "+createdDate+", Level = "+level+", StrucElement = "+strucElement+"]";
    }
}
