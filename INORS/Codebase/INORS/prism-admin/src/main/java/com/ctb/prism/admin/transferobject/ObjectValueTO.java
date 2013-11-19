package com.ctb.prism.admin.transferobject;

import com.ctb.prism.core.transferobject.BaseTO;
/**
 * This class is the base Transfer Object. Other Transfer Objects should
 * extend this class
 *
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class ObjectValueTO extends BaseTO {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	private String other;
	
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
