package com.ctb.prism.login.transferobject;

import java.io.Serializable;

public class Admins implements Serializable
{
    private String name;

    private String seq;

    private Products[] products;

    private String fileLoc;

    private String isCurrentAdmin;

    private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public Products[] getProducts() {
		return products;
	}

	public void setProducts(Products[] products) {
		this.products = products;
	}

	public String getFileLoc() {
		return fileLoc;
	}

	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}

	public String getIsCurrentAdmin() {
		return isCurrentAdmin;
	}

	public void setIsCurrentAdmin(String isCurrentAdmin) {
		this.isCurrentAdmin = isCurrentAdmin;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    
}
