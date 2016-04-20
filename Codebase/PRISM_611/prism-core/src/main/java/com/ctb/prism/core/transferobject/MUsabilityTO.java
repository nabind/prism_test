package com.ctb.prism.core.transferobject;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "User_Activity_History")
public class MUsabilityTO extends BaseTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String _id;
	
	private String userName_id;
	
	private String customerCode;
	
	private String activity;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date activity_Date;
	
	private String activity_Details;
	
	private String ip_Address;
	
	private boolean loginAs;
	
	private String created;

	public String getUserName_id() {
		return userName_id;
	}

	public void setUserName_id(String userName_id) {
		this.userName_id = userName_id;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getActivity_Date() {
		return activity_Date;
	}

	public void setActivity_Date(Date activity_Date) {
		this.activity_Date = activity_Date;
	}

	public String getActivity_Details() {
		return activity_Details;
	}

	public void setActivity_Details(String activity_Details) {
		this.activity_Details = activity_Details;
	}

	public String getIp_Address() {
		return ip_Address;
	}

	public void setIp_Address(String ip_Address) {
		this.ip_Address = ip_Address;
	}

	public boolean isLoginAs() {
		return loginAs;
	}

	public void setLoginAs(boolean loginAs) {
		this.loginAs = loginAs;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	
	
	
	
}
