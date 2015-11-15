package com.ctb.prism.webservice.transferobject;

import java.io.Serializable;

public class DemoTO implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477773094826787095L;

	private String demoName;
	private String demovalue;
	public static enum DEMO_NAME { Examinee_ph, Stdnt_Addrss, Student_APT, Student_City, Student_St, Student_ZIP, Stdnt_Ctn_Cd };
	
	public String getDemoName() {
		return demoName;
	}
	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}
	public String getDemovalue() {
		if("Examinee_ph".equals(demoName) 
				|| "Stdnt_Addrss".equals(demoName) 
				|| "Student_APT".equals(demoName) 
				|| "Student_City".equals(demoName) 
				|| "Student_St".equals(demoName) 
				|| "Student_ZIP".equals(demoName) 
				|| "Stdnt_Ctn_Cd".equals(demoName) ) {
			return demovalue;
		}
		return (demovalue != null && demovalue.length() > 0)? demovalue : "<BLANK>";
	}
	public void setDemovalue(String demovalue) {
		this.demovalue = demovalue;
	}
	
	
}
