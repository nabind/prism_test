package com.ctb.prism.test;

import java.util.HashMap;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;

/**
 * This class provides input parameters for test methods.
 * 
 * @author TCS
 *
 */
public class ReportTestHelper {

	public static Map<String, Object> helpGetRescoreRequestForm(TestParams testParams) throws BusinessException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("testAdministrationVal", "");
		paramMap.put("school", "");
		paramMap.put("grade", "");
		UserTO loggedinUserTO = getUserTO();
		paramMap.put("loggedinUserTO", loggedinUserTO);
		return paramMap;
	}

	public static Map<String, Object> helpSubmitRescoreRequest(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemNumber", 0L);
		paramMap.put("subtestId", 0L);
		paramMap.put("sessionId", 0L);
		paramMap.put("moduleId", "");
		paramMap.put("studentBioId", 0L);
		paramMap.put("requestedStatus", "");
		paramMap.put("requestedDate", "");
		paramMap.put("userId", 0L);
		return paramMap;
	}

	public static Map<String, Object> helpResetItemState(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("subtestId", 0L);
		paramMap.put("studentBioId", 0L);
		paramMap.put("userId", 0L);
		paramMap.put("requestedStatus", "");
		return paramMap;
	}

	public static Map<String, Object> helpResetItemDate(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("studentBioId", 0L);
		paramMap.put("userId", 0L);
		paramMap.put("requestedDate", "");
		return paramMap;
	}

	public static Map<String, Object> helpGetNotDnpStudentDetails(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("studentBioId", 0L);
		return paramMap;
	}

	private static UserTO getUserTO() {
		UserTO to = new UserTO();
		return to;
	}

}
