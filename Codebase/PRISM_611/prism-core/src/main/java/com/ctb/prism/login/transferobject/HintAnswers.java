
package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class HintAnswers implements Serializable
{
    private String ansValue;

    private String qid;

    public String getAnsValue ()
    {
        return ansValue;
    }

    public void setAnsValue (String AnsValue)
    {
        this.ansValue = AnsValue;
    }

    public String getQID ()
    {
        return qid;
    }

    public void setQID (String QID)
    {
        this.qid = QID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AnsValue = "+ansValue+", QID = "+qid+"]";
    }
}