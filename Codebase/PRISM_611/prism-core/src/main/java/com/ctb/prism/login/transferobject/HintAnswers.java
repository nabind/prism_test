
package com.ctb.prism.login.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;

public class HintAnswers extends BaseTO 
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