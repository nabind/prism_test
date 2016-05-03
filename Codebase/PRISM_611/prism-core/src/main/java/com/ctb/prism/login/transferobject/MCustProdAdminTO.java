package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ProjectCustAdminProd")
public class MCustProdAdminTO implements Serializable
{
	@Id
    private String _id;
	
    private Customers[] customers;
    
    private ProjectProp uiConfig;

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

	public Customers[] getCustomers() {
		return customers;
	}

	public void setCustomers(Customers[] customers) {
		this.customers = customers;
	}

	public ProjectProp getUiConfig() {
		return uiConfig;
	}

	public void setUiConfig(ProjectProp uiConfig) {
		this.uiConfig = uiConfig;
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
