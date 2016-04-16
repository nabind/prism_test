package com.ctb.prism.login.transferobject;

public class Address
{
    private String Street;

    private String County;

    private String State;

    private String ZipCode;

    private String City;

    public String getStreet ()
    {
        return Street;
    }

    public void setStreet (String Street)
    {
        this.Street = Street;
    }

    public String getCounty ()
    {
        return County;
    }

    public void setCounty (String County)
    {
        this.County = County;
    }

    public String getState ()
    {
        return State;
    }

    public void setState (String State)
    {
        this.State = State;
    }

    public String getZipCode ()
    {
        return ZipCode;
    }

    public void setZipCode (String ZipCode)
    {
        this.ZipCode = ZipCode;
    }

    public String getCity ()
    {
        return City;
    }

    public void setCity (String City)
    {
        this.City = City;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Street = "+Street+", County = "+County+", State = "+State+", ZipCode = "+ZipCode+", City = "+City+"]";
    }
}