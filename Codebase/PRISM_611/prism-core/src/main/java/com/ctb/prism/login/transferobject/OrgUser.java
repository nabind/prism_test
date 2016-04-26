package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class OrgUser implements Serializable 
{
    private String isActive;

    private String org_id;
    
    private String orgName;

    public String getIsActive ()
    {
        return isActive;
    }

    public void setIsActive (String IsActive)
    {
        this.isActive = IsActive;
    }

    public String getOrg_id ()
    {
        return org_id;
    }

    public void setOrg_id (String Org_id)
    {
        this.org_id = Org_id;
    }

    public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		orgName = orgName;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [IsActive = "+isActive+", Org_id = "+org_id+", OrgName = "+orgName+"]";
    }
}