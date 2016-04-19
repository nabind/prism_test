package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class OrgUser extends BaseTO 
{
    private String IsActive;

    private String Org_id;
    
    private String OrgName;

    public String getIsActive ()
    {
        return IsActive;
    }

    public void setIsActive (String IsActive)
    {
        this.IsActive = IsActive;
    }

    public String getOrg_id ()
    {
        return Org_id;
    }

    public void setOrg_id (String Org_id)
    {
        this.Org_id = Org_id;
    }

    public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [IsActive = "+IsActive+", Org_id = "+Org_id+", OrgName = "+OrgName+"]";
    }
}