package com.ctb.prism.test;

import com.ctb.prism.core.transferobject.UsabilityTO;

public class UsabilityTestHelper {
	
	public static UsabilityTO helpSaveUsabilityData() {
		//MUsabilityTO usability = new MUsabilityTO();
		UsabilityTO usability = new UsabilityTO();
		usability.setActivityTypeId(1001L);
		usability.setCreatedAt(null);
			
		return usability;
	}
}
