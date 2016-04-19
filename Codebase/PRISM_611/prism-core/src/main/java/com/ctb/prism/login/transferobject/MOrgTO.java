package com.ctb.prism.login.transferobject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document(collection = "Org")
public class MOrgTO extends BaseTO
{
	@Id
	private String _id;
	 
    private String Name;

    private String Category;

    private String[] AdminCodes;

    private String UpdatedDate;

    private String Parent_id;

    private String SpCodes;

    private String Project_id;

    private String CustomerCode;

    private String CreatedDate;

    private String Level;

    private String StrucElement;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getCategory ()
    {
        return Category;
    }

    public void setCategory (String Category)
    {
        this.Category = Category;
    }

    public String[] getAdminCodes ()
    {
        return AdminCodes;
    }

    public void setAdminCodes (String[] AdminCodes)
    {
        this.AdminCodes = AdminCodes;
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
        return UpdatedDate;
    }

    public void setUpdatedDate (String UpdatedDate)
    {
        this.UpdatedDate = UpdatedDate;
    }

    public String getParent_id ()
    {
        return Parent_id;
    }

    public void setParent_id (String Parent_id)
    {
        this.Parent_id = Parent_id;
    }

    public String getSpCodes ()
    {
        return SpCodes;
    }

    public void setSpCodes (String SpCodes)
    {
        this.SpCodes = SpCodes;
    }

    public String getProject_id ()
    {
        return Project_id;
    }

    public void setProject_id (String Project_id)
    {
        this.Project_id = Project_id;
    }

    public String getCustomerCode ()
    {
        return CustomerCode;
    }

    public void setCustomerCode (String CustomerCode)
    {
        this.CustomerCode = CustomerCode;
    }

    public String getCreatedDate ()
    {
        return CreatedDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    public String getLevel ()
    {
        return Level;
    }

    public void setLevel (String Level)
    {
        this.Level = Level;
    }

    public String getStrucElement ()
    {
        return StrucElement;
    }

    public void setStrucElement (String StrucElement)
    {
        this.StrucElement = StrucElement;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+Name+", Category = "+Category+", AdminCodes = "+AdminCodes+", _id = "+_id+", UpdatedDate = "+UpdatedDate+", Parent_id = "+Parent_id+", SpCodes = "+SpCodes+", Project_id = "+Project_id+", CustomerCode = "+CustomerCode+", CreatedDate = "+CreatedDate+", Level = "+Level+", StrucElement = "+StrucElement+"]";
    }
}
