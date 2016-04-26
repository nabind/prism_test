package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class FlatCustProdAdmin implements Serializable
{
    private String _id;
	
    private FlatCustomers customers;
    
    private ProjectProp projectProp;

    private String projectName;

    private String createdDate;
    
    private HintQuestions[] hintQuestions;

    private Roles[] roles;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public FlatCustomers getCustomers() {
		return customers;
	}

	public void setCustomers(FlatCustomers customers) {
		this.customers = customers;
	}

	public ProjectProp getProjectProp() {
		return projectProp;
	}

	public void setProjectProp(ProjectProp projectProp) {
		this.projectProp = projectProp;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public HintQuestions[] getHintQuestions() {
		return hintQuestions;
	}

	public void setHintQuestions(HintQuestions[] hintQuestions) {
		this.hintQuestions = hintQuestions;
	}

	public Roles[] getRoles() {
		return roles;
	}

	public void setRoles(Roles[] roles) {
		this.roles = roles;
	}

    
}
