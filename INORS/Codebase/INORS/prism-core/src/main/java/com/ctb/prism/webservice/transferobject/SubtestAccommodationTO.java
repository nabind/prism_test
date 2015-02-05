package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;

public class SubtestAccommodationTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return (value != null && value.length() > 0)? value : "<BLANK>";
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
