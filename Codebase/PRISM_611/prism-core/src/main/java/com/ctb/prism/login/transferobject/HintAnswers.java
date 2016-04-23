
package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class HintAnswers implements Serializable
{
    private String AnsValue;

    private String QID;

    public String getAnsValue ()
    {
        return AnsValue;
    }

    public void setAnsValue (String AnsValue)
    {
        this.AnsValue = AnsValue;
    }

    public String getQID ()
    {
        return QID;
    }

    public void setQID (String QID)
    {
        this.QID = QID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AnsValue = "+AnsValue+", QID = "+QID+"]";
    }
}