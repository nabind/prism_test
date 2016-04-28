package com.ctb.prism.report.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.report.transferobject.AssessmentTO;
import com.ctb.prism.report.transferobject.GroupDownloadStudentTO;
import com.ctb.prism.report.transferobject.GroupDownloadTO;
import com.ctb.prism.report.transferobject.InputControlTO;
import com.ctb.prism.report.transferobject.JobTrackingTO;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.report.transferobject.ObjectValueTO;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.ReportParameterTO;
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.test.ReportTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.ctb.prism.webservice.transferobject.ReportActionTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class ReportServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IReportService reportService;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=java.lang.NullPointerException.class)
	public void testGetFilledReport() throws Exception {
		JasperReport jasperReport = null;
		Map<String, Object> parameters = ReportTestHelper.helpGetFilledReport(testParams);
		JasperPrint jp = reportService.getFilledReport(jasperReport, parameters);
		assertNotNull(jp);
	}

	@Test(expected=java.lang.NullPointerException.class)
	public void testGetFilledReportForPDF() throws Exception {
		JasperReport jasperReport = null;
		Map<String, Object> parameters = ReportTestHelper.helpGetFilledReportForPDF(testParams);
		boolean isPrinterFriendly = false;
		String user = "";
		String sessionParam = "";
		reportService.getFilledReportForPDF(jasperReport, parameters, isPrinterFriendly, user, sessionParam);
		assertNotNull("Return type is void");
	}

	@Test
	public void testRemoveReportCache() {
		reportService.removeReportCache();
		assertNotNull("Return type is void");
	}

	@Test
	public void testRemoveConfigurationCache() {
		reportService.removeConfigurationCache(testParams.getContractName());
		assertNotNull("Return type is void");
	}

	@Test
	public void testRemoveCache() {
		reportService.removeCache();
		assertNotNull("Return type is void");
	}

	@Test
	public void testRemoveCacheSimpleDB() {
		reportService.removeCacheSimpleDB(testParams.getContractName());
		assertNotNull("Return type is void");
	}

	@Test(expected=java.lang.NullPointerException.class)
	public void testRemoveCacheString() throws IOException {
		reportService.removeCache(testParams.getContractName());
		assertNotNull("Return type is void");
	}

	@Test(expected=org.springframework.dao.EmptyResultDataAccessException.class)
	public void testGetReportJasperObject() {
		String reportPath = "";
		JasperReport jr = reportService.getReportJasperObject(reportPath);
		assertNotNull(jr);
	}

	@Test(expected=org.springframework.dao.EmptyResultDataAccessException.class)
	public void testGetReportJasperObjectForName() {
		String reportname = "";
		JasperReport jr = reportService.getReportJasperObjectForName(reportname);
		assertNotNull(jr);
	}

	@Test
	public void testGetReportJasperObjectList() throws JRException {
		String reportPath = "";
		List<ReportTO> list = reportService.getReportJasperObjectList(reportPath);
		assertNotNull(list);
	}

	@Test
	public void testGetInputControlDetails() {
		String reportPath = "";
		List<InputControlTO> list = reportService.getInputControlDetails(reportPath);
		assertNull(list);
	}

	@Test
	public void testGetAllInputControls() {
		List<InputControlTO> list = reportService.getAllInputControls();
		assertNotNull(list);
	}

	@Test
	public void testGetDefaultFilter() {
		List<InputControlTO> tos = new ArrayList<InputControlTO>();
		String userName = "";
		String customerId = "0";
		String assessmentId = "0";
		String combAssessmentId = "0";
		String reportUrl = "";
		Map<String, Object> sessionParams = new HashMap<String, Object>();
		String userId = "0";
		String currentOrg = "0";
		Object obj = reportService.getDefaultFilter(tos, userName, customerId, assessmentId, combAssessmentId, reportUrl, sessionParams, userId, currentOrg, "", "");
		assertNotNull(obj);
	}

	@Test(expected=org.springframework.jdbc.UncategorizedSQLException.class)
	public void testGetValuesOfSingleInputString() {
		String query = "";
		List<ObjectValueTO> list = reportService.getValuesOfSingleInput(query);
		assertNotNull(list);
	}

	@Test(expected=com.ctb.prism.core.exception.SystemException.class)
	public void testGetValuesOfSingleInputStringStringStringStringStringMapOfStringStringObjectString() throws SystemException {
		String reportUrl= "";
		String query = "";
		String userName = "";
		String customerId = "0";
		String changedObject = "";
		String changedValue = "";
		Map<String, String> replacableParams = new HashMap<String, String>();
		Object clazz = null;
		String userId = "0";
		List<ObjectValueTO> list = reportService.getValuesOfSingleInput(reportUrl , query, userName, customerId, changedObject, changedValue, replacableParams, clazz, userId);
		assertNotNull(list);
	}

	@Test
	public void testGetAllReportList() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetAllReportList(testParams);
		List<ReportTO> list = reportService.getAllReportList(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testUpdateReportNew() {
		ReportTO reportTO = ReportTestHelper.getReportTO(testParams);
		boolean status = reportService.updateReportNew(reportTO);
		assertNotNull(status);
	}

	@Test
	public void testDeleteReport() throws SystemException {
		Map<String, Object> paramMap = ReportTestHelper.helpDeleteReport(testParams);
		boolean status = reportService.deleteReport(paramMap);
		assertNotNull(status);
	}

	@Test(expected=java.lang.StringIndexOutOfBoundsException.class)
	public void testGetAssessments() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetAssessments(testParams);
		List<AssessmentTO> list = reportService.getAssessments(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testAddNewDashboard() throws Exception {
		ReportParameterTO reportParameterTO = ReportTestHelper.getReportParameterTO(testParams);
		ReportTO to = reportService.addNewDashboard(reportParameterTO);
		assertNull(to);
	}

	@Test
	public void testGetReport() throws SystemException {
		String reportIdentifier = "";
		ReportTO to = reportService.getReport(reportIdentifier);
		assertNotNull(to);
	}

	@Test
	public void testLoadManageMessage() throws SystemException {
		Map<String, Object> paramMap = ReportTestHelper.helpLoadManageMessage(testParams);
		Map<String, Object> map = reportService.loadManageMessage(paramMap);
		assertNotNull(map);
	}

	@Test(expected=com.ctb.prism.core.exception.SystemException.class)
	public void testSaveManageMessage() throws SystemException {
		List<ManageMessageTO> manageMessageTOList = new ArrayList<ManageMessageTO>();
		int i = reportService.saveManageMessage(manageMessageTOList);
		assertNotNull(i);
	}

	@Test
	public void testGetAllGroupDownloadFiles() throws SystemException {
		Map<String, Object> paramMap = ReportTestHelper.helpGetAllGroupDownloadFiles(testParams);
		List<JobTrackingTO> list = reportService.getAllGroupDownloadFiles(paramMap);
		assertNull(list);
	}

	@Test
	public void testGetRequestDetail() throws SystemException {
		Map<String, Object> paramMap = ReportTestHelper.helpGetRequestDetail(testParams);
		List<JobTrackingTO> list = reportService.getRequestDetail(paramMap);
		assertNull(list);
	}

	@Test
	public void testDeleteGroupFiles() throws Exception {
		String id = "0";
		boolean status = reportService.deleteGroupFiles(id);
		assertNotNull(status);
	}

	@Test
	public void testGetScheduledGroupFiles() throws Exception {
		Map<String, Object> paramMap = ReportTestHelper.helpGetScheduledGroupFiles(testParams);
		Map<Long, String> map = reportService.getScheduledGroupFiles(paramMap);
		assertNotNull(map);
	}

	@Test
	public void testUpdateScheduledGroupFiles() throws Exception {
		Map<String, Object> paramMap = ReportTestHelper.helpUpdateScheduledGroupFiles(testParams);
		reportService.updateScheduledGroupFiles(paramMap);
		assertNotNull("Return type is void");
	}

	@Test
	public void testGetReportMessageFilter() throws SystemException {
		Map<String, Object> paramMap = ReportTestHelper.helpGetReportMessageFilter(testParams);
		Map<String, Object> map = reportService.getReportMessageFilter(paramMap);
		assertNotNull(map);
	}

	@Test
	public void testGetTestAdministrations() {
		List<com.ctb.prism.core.transferobject.ObjectValueTO> list = reportService.getTestAdministrations();
		assertNotNull(list);
	}

	@Test
	public void testPopulateStudentTableGD() {
		GroupDownloadTO to = ReportTestHelper.getGroupDownloadTO();
		List<GroupDownloadStudentTO> list = reportService.populateStudentTableGD(to);
		assertNotNull(list);
	}

	@Test
	public void testGetGDFilePaths() {
		GroupDownloadTO to = ReportTestHelper.getGroupDownloadTO();
		Map<String, String> map = reportService.getGDFilePaths(to);
		assertNotNull(map);
	}

	@Test(expected=org.springframework.dao.DataIntegrityViolationException.class)
	public void testCreateJobTracking() {
		GroupDownloadTO to = ReportTestHelper.getGroupDownloadTO();
		String str = reportService.createJobTracking(to);
		assertNotNull(str);
	}

	@Test
	public void testGetSystemConfigurationMessage() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetSystemConfigurationMessage(testParams);
		String str = reportService.getSystemConfigurationMessage(paramMap);
		assertNotNull(str);
	}

	@Test
	public void testGetProcessDataGD() {
		String processId = "0";
		String contractName = testParams.getContractName();
		JobTrackingTO to = reportService.getProcessDataGD(processId, contractName);
		assertNotNull(to);
	}

	@Test
	public void testGetConventionalFileNameGD() {
		Long orgNodeId = 0L;
		String str = reportService.getConventionalFileNameGD(orgNodeId);
		assertNull(str);
	}

	@Test(expected=java.lang.NullPointerException.class)
	public void testUpdateJobTracking() {
		GroupDownloadTO to = ReportTestHelper.getGroupDownloadTO();
		int i = reportService.updateJobTracking(to);
		assertNotNull(i);
	}

	@Test
	public void testGetReportMessage() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetReportMessage(testParams);
		String str = reportService.getReportMessage(paramMap);
		assertNull(str);
	}

	@Test
	public void testGetRequestSummary() {
		String contractName = testParams.getContractName();
		String str = reportService.getRequestSummary(ReportTestHelper.requestDetails, contractName);
		assertNotNull(str);
	}

	@Test
	public void testGetAllReportMessages() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetAllReportMessages(testParams);
		List<ReportMessageTO> list = reportService.getAllReportMessages(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testGetReportParameter() {
		List<InputControlTO> allInputControls = new ArrayList<InputControlTO>();
		Object reportFilterTO = null;
		JasperReport jasperReport = null;
		boolean getFullList = false;
		HttpServletRequest req = null;
		String reportUrl = "";
		String currentOrg = "0";
	//	Map<String, String[]> param = new HashMap<String, String[]>();
		Map<String, Object> map = reportService.getReportParameter(allInputControls, reportFilterTO, jasperReport, getFullList, req, reportUrl, currentOrg);
		assertNotNull(map);
	}

	@Test(expected=java.lang.Exception.class)
	public void testGetJasperReportObject() throws DataAccessException, JRException, Exception {
		String reportUrl = "";
		List<ReportTO> list = reportService.getJasperReportObject(reportUrl);
		assertNotNull(list);
	}

	@Test(expected=java.lang.Exception.class)
	public void testGetMainReport() throws Exception {
		List<ReportTO> jasperReportList = new ArrayList<ReportTO>();
		JasperReport jr = reportService.getMainReport(jasperReportList);
		assertNotNull(jr);
	}

	@Test(expected=java.sql.SQLException.class)
	public void testFillReportForTableApi() throws JRException, SQLException {
		String reportUrl = "";
		JasperReport jasperReport = null;
		 Map<String, Object> parameterValues = new HashMap<String, Object>();
		JasperPrint jp = reportService.fillReportForTableApi(reportUrl, jasperReport, parameterValues);
		assertNotNull(jp);
	}

	@Test
	public void testGetStudentFileName() {
		String type = "";
		String studentBioId = "0";
		String custProdId = "0";
		String str = reportService.getStudentFileName(type, studentBioId, custProdId);
		assertNotNull(str);
	}

	@Test
	public void testGetProductsForEditActions() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetProductsForEditActions(testParams);
		List<ReportActionTO> list = reportService.getProductsForEditActions(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testGetActionsForEditActions() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetActionsForEditActions(testParams);
		List<ReportActionTO> list = reportService.getActionsForEditActions(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testGetActionAccess() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetActionAccess(testParams);
		List<ReportActionTO> list = reportService.getActionAccess(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testUpdateDataForActions() {
		Map<String, Object> paramMap = ReportTestHelper.helpUpdateDataForActions(testParams);
		String str = reportService.updateDataForActions(paramMap);
		assertNull(str);
	}

	@Test
	public void testUpdateJobTrackingTable() {
		String jobId = "0";
		String filePath = "";
		reportService.updateJobTrackingTable(jobId, filePath);
		assertNotNull("Return type is void");
	}

	@Test(expected=org.springframework.dao.EmptyResultDataAccessException.class)
	public void testGetDefaultFilterTasc() {
		List<InputControlTO> tos = new ArrayList<InputControlTO>();
		String userName = "";
		String assessmentId = "0";
		String combAssessmentId = "0";
		String reportUrl = "";
		Object obj = reportService.getDefaultFilterTasc(tos, userName, assessmentId, combAssessmentId, reportUrl);
		assertNotNull(obj);
	}

	@Test(expected=com.ctb.prism.core.exception.SystemException.class)
	public void testGetValuesOfSingleInputTasc() throws SystemException {
		String query = "";
		String userName = "";
		String changedObject = "";
		String changedValue = "";
		Map<String, String> replacableParams = new HashMap<String, String>();
		Object clazz = null;
		boolean bulkDownload = false;
		List<ObjectValueTO> list = reportService.getValuesOfSingleInputTasc(query, userName, changedObject, changedValue, replacableParams, clazz, bulkDownload);
		assertNotNull(list);
	}

	@Test
	public void testGetGenericSystemConfigurationMessages() {
		Map<String, Object> paramMap = ReportTestHelper.helpGetGenericSystemConfigurationMessages(testParams);
		Map<String, String> list = reportService.getGenericSystemConfigurationMessages(paramMap);
		assertNotNull(list);
	}

}
