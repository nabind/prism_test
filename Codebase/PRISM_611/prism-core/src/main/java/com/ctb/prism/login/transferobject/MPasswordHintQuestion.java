package com.ctb.prism.login.transferobject;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PasswordHintQuestion")
public class MPasswordHintQuestion  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String _id;

    private String status;

    private String questionSeq;

    private String question;

    private Date createdDate;
    
    private Date updatedDate;

    private String project_Id;

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Date getUpdatedDate ()
    {
        return updatedDate;
    }

    public void setUpdatedDate (Date updatedDate)
    {
        this.updatedDate = updatedDate;
    }

    public String getQuestionSeq ()
    {
        return questionSeq;
    }

    public void setQuestionSeq (String questionSeq)
    {
        this.questionSeq = questionSeq;
    }

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
    }

    public Date getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getProject_Id ()
    {
        return project_Id;
    }

    public void setProject_Id (String project_Id)
    {
        this.project_Id = project_Id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [_id = "+_id+", status = "+status+", updatedDate = "+updatedDate+", questionSeq = "+questionSeq+", question = "+question+", createdDate = "+createdDate+", project_Id = "+project_Id+"]";
    }
	
}
