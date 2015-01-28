package com.ctb.prism.test;

import java.util.HashMap;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;

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

	public static Map<String, Object> helpGetFilledReport(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetFilledReportForPDF(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetAllReportList(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static ReportTO getReportTO(TestParams testParams) {
		ReportTO reportTO = new ReportTO();
		return reportTO;
	}

	public static Map<String, Object> helpDeleteReport(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetAssessments(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static ReportParameterTO getReportParameterTO(TestParams testParams) {
		ReportParameterTO to = new ReportParameterTO();
		return to;
	}

	public static Map<String, Object> helpLoadManageMessage(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetAllGroupDownloadFiles(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetRequestDetail(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetScheduledGroupFiles(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpUpdateScheduledGroupFiles(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetReportMessageFilter(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static GroupDownloadTO getGroupDownloadTO() {
		GroupDownloadTO to = new GroupDownloadTO();
		return to;
	}

	public static Map<String, Object> helpGetSystemConfigurationMessage(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetReportMessage(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetAllReportMessages(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetProductsForEditActions(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetActionsForEditActions(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetActionAccess(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpUpdateDataForActions(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

	public static Map<String, Object> helpGetGenericSystemConfigurationMessages(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		return paramMap;
	}

}
