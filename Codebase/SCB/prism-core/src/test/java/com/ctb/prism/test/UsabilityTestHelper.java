package com.ctb.prism.test;

import com.ctb.prism.core.transferobject.UsabilityTO;

public class UsabilityTestHelper {
	
	public static UsabilityTO helpSaveUsabilityData(TestParams testParams) {
		UsabilityTO usability = new UsabilityTO();
		usability.setLoginAs(true);
		usability.setUserId("4075095");
		usability.setCustomerId("1013");
		usability.setActivityTypeId(1L);
		usability.setReportUrl("/public/Missouri/Report/Student_Roster_files");
		usability.setIpAddress("192.0.0.1");
		return usability;
	}
}
