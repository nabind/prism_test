package com.drc.test;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="org")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlType(propOrder={"level", "orgcode","orgname"})
public class Org
{
	private String orgcode;

    private String orgname;

    private String level;

    public String getOrgcode ()
    {
        return orgcode;
    }

   @XmlAttribute
    public void setOrgcode (String orgcode)
    {
        this.orgcode = orgcode;
    }

    public String getOrgname ()
    {
        return orgname;
    }

   @XmlAttribute
    public void setOrgname (String orgname)
    {
        this.orgname = orgname;
    }

    public String getLevel ()
    {
        return level;
    }

   @XmlAttribute
    public void setLevel (String level)
    {
        this.level = level;
    }

}
		