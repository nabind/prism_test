package com.drc.test;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="student")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlType(propOrder={"drcstudentid", "statecode","examineeid","registrationon","registeredby","lastname","firstname","middleinitial","dob","gender","studentupdatedatetime",
		"org_details"})
public class Student
{
	private String registrationon;

    private String registeredby;

    private String statecode;

    private Org_Details org_details;

    private String dob;

    private String studentupdatedatetime;

    private String lastname;

    private String gender;

    private String middleinitial;

    private String drcstudentid;

    private String firstname;

    private String examineeid;

    public String getRegistrationon ()
    {
        return registrationon;
    }
    
   @XmlAttribute
    public void setRegistrationon (String registrationon)
    {
        this.registrationon = registrationon;
    }

    public String getRegisteredby ()
    {
        return registeredby;
    }

   @XmlAttribute
    public void setRegisteredby (String registeredby)
    {
        this.registeredby = registeredby;
    }

    public String getStatecode ()
    {
        return statecode;
    }

   @XmlAttribute
    public void setStatecode (String statecode)
    {
        this.statecode = statecode;
    }

    public Org_Details getOrg_details ()
    {
        return org_details;
    }

    @XmlElement
    public void setOrg_details (Org_Details org_details)
    {
        this.org_details = org_details;
    }

    public String getDob ()
    {
        return dob;
    }

   @XmlAttribute
    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getStudentupdatedatetime ()
    {
        return studentupdatedatetime;
    }

   @XmlAttribute
    public void setStudentupdatedatetime (String studentupdatedatetime)
    {
        this.studentupdatedatetime = studentupdatedatetime;
    }

    public String getLastname ()
    {
        return lastname;
    }

   @XmlAttribute
    public void setLastname (String lastname)
    {
        this.lastname = lastname;
    }

    public String getGender ()
    {
        return gender;
    }

   @XmlAttribute
    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getMiddleinitial ()
    {
        return middleinitial;
    }

   @XmlAttribute
    public void setMiddleinitial (String middleinitial)
    {
        this.middleinitial = middleinitial;
    }

    public String getDrcstudentid ()
    {
        return drcstudentid;
    }

    @XmlAttribute
    public void setDrcstudentid (String drcstudentid)
    {
        this.drcstudentid = drcstudentid;
    }

    public String getFirstname ()
    {
        return firstname;
    }

   @XmlAttribute
    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    public String getExamineeid ()
    {
        return examineeid;
    }

   @XmlAttribute
    public void setExamineeid (String examineeid)
    {
        this.examineeid = examineeid;
    }

}