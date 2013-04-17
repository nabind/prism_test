package com.ctb.prism.web.util;

public class ManageStringwithTooltip {
	
	public static String manageString(String value) {
		if (value.length() > 18) {
			
			value = value.substring(0,15)+"...";
		}	
		return value;	
	}
}
