package com.ctb.prism.test;

import java.util.Date;

import com.ctb.prism.core.transferobject.MUsabilityTO;

public class UsabilityTestHelper {
	
	public static MUsabilityTO helpSaveUsabilityData(TestParams testParams) {
		MUsabilityTO usability = new MUsabilityTO();
		usability.setActivity("login");
		usability.setActivity_Date(new Date());
		usability.setActivity_Details("Login");
		usability.setCreated("mdadmin");
		usability.setCustomerCode("FL");
		usability.setIp_Address("10.160.23.50");
		usability.setUserName_id("mdadmin");
		return usability;
	}
}
