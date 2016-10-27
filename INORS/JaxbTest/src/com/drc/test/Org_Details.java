package com.drc.test;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="org_details")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlType(propOrder={"orgid", "tp","org"})
public class Org_Details
{
	private String orgid;

    private String tp;

    private Org org;

    public String getOrgid ()
    {
        return orgid;
    }

    @XmlAttribute
    public void setOrgid (String orgid)
    {
        this.orgid = orgid;
    }

    public String getTp ()
    {
        return tp;
    }

    @XmlAttribute
    public void setTp (String tp)
    {
        this.tp = tp;
    }

    public Org getOrg ()
    {
        return org;
    }

    @XmlElement
    public void setOrg (Org org)
    {
        this.org = org;
    }

 }