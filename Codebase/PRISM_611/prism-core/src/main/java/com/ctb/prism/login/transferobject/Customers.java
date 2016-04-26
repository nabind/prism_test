package com.ctb.prism.login.transferobject;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

public class Customers implements Serializable
{
    private String name;

    private String fileLoc;

    private String createdDate;

    private String supportEmail;

    private Admins[] admins;

    private String sendLoginPdf;

    private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileLoc() {
		return fileLoc;
	}

	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getSupportEmail() {
		return supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public Admins[] getAdmins() {
		return admins;
	}

	public void setAdmins(Admins[] admins) {
		this.admins = admins;
	}

	public String getSendLoginPdf() {
		return sendLoginPdf;
	}

	public void setSendLoginPdf(String sendLoginPdf) {
		this.sendLoginPdf = sendLoginPdf;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    
}
