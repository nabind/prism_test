package com.ctb.prism.login.transferobject;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ctb.prism.core.transferobject.BaseTO;

@Document
public class MReportMessageTO extends BaseTO{

	 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	private String customerCode;

	private String name;

	public String getMessage ()
	{
		return message;
	}

	public void setMessage (String message)
	{
		this.message = message;
	}

	public String getCustomerCode ()
	{
		return customerCode;
	}

	public void setCustomerCode (String customerCode)
	{
		this.customerCode = customerCode;
	}

	public String getName ()
	{
		return name;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [message = "+message+", customerCode = "+customerCode+", name = "+name+"]";
	}
}
