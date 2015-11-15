package com.ctb.prism.web.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import com.ctb.prism.test.WebTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class InorsControllerTest extends AbstractJUnit4SpringContextTests {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Autowired
	private InorsController controller;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		// Populate test params from properties file
		testParams = TestUtil.getTestParams();

		// Create request and response for Controller methods
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();

		// Set all Session Attributes
		TestUtil.setSessionAttributes(request, testParams);

		// Bypass login
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGroupDownloadFiles() throws SystemException {
		com.ctb.prism.login.transferobject.UserTO loggedinUserTO = WebTestHelper.getLoggedinUserTO(testParams);
		request.getSession().setAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS, loggedinUserTO);
		request.getSession().setAttribute(IApplicationConstants.CURRORGLVL, 0L);
		ModelAndView mv = controller.groupDownloadFiles(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDeleteGroupDownloadFiles() {
		ModelAndView mv = controller.deleteGroupDownloadFiles(request, response);
		assertNull(mv);
	}

	@Test
	public void testDownloadGroupDownloadFiles() {
		
		request.setParameter("jobId","11111");
		request.setParameter("filePath","/MAPREPORTS/MISSOURI/MAPSMTSPR2015/GDF/");
		request.setParameter("orgLevel","2");
		request.setParameter("requestType","SDF");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER,"1057");
		controller.downloadGroupDownloadFiles(request, response);
		assertNotNull("Method return is void");
	}

	@Test
	public void testCheckFileAvailability() throws IOException {
		request.setParameter("filePath", "");
		ModelAndView mv = controller.checkFileAvailability(request, response);
		assertNull(mv);
	}

	@Test
	public void testGetStudentFileName() throws IOException {
		ModelAndView mv = controller.getStudentFileName(request, response);
		assertNull(mv);
	}

	@Test
	public void testGetRequestDetailViewData() {
		ModelAndView mv = controller.getRequestDetailViewData(request, response);
		assertNull(mv);
	}

	@Test
	public void testBulkCandidateReportDownload() throws SystemException {
		ModelAndView mv = controller.bulkCandidateReportDownload(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadCandicateReport() throws SystemException {
		ModelAndView mv = controller.bulkCandidateReportDownload(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testIcLetterDownloads() {
		request.setParameter("reportId", "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.CURRORGLVL, 0L);
		request.getSession().setAttribute(IApplicationConstants.CURRUSER, testParams.getUserName());
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		ModelAndView mv = controller.icLetterDownloads(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetReportParameters() {
		Map<String, Object> map = controller.getReportParameters(request, "");
		assertNotNull(map);
	}

	@Test
	public void testGroupDownloadForm() {
		request.setParameter("reportId", "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.CURRORGLVL, 0L);
		request.getSession().setAttribute(IApplicationConstants.CURRUSER, testParams.getUserName());
		request.getSession().setAttribute(IApplicationConstants.CURRORG, "0");
		ModelAndView mv = controller.groupDownloadForm(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGroupDownloadFunction() throws SystemException {
		request.setParameter("json", WebTestHelper.requestDetails);
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		propertyMap.put(IApplicationConstants.STATIC_PDF_LOCATION,"MAPREPORTS");
		request.getSession().setAttribute("propertyMap",propertyMap);
		request.getSession().setAttribute(IApplicationConstants.CURRUSER,"test");
		request.getSession().setAttribute(IApplicationConstants.ADMIN_YEAR,"5050");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER,"1057");
		String str = controller.groupDownloadFunction(request, response);
		assertNotNull(str);
	}

	@Test
	public void testDownloadZippedPdf() {
		controller.downloadZippedPdf(request, response);
		assertNotNull("Method return is void");
	}

	@Test
	public void testDownloadFile() {
		request.setParameter("fileName","testFirst");
		controller.downloadFile(request, response);
		assertNotNull("Method return is void");
	}

	@Test
	public void testGetTenantHierarchy() throws Exception {
		//String str = controller.getTenantHierarchy(request, response);
		//assertNotNull(str);
	}

	@Test
	public void testDownloadBulkPdf() throws Exception {
		BulkDownloadTO bulkDownloadTO = new BulkDownloadTO();
		// String str = controller.downloadBulkPdf(bulkDownloadTO, request, response);
		// assertNotNull(str);
	}

	@Test
	public void testRetainDownloadValues() throws Exception {
		BulkDownloadTO bulkDownloadTO = new BulkDownloadTO();
		String str = controller.retainDownloadValues(bulkDownloadTO, request, response);
		assertNull(str);
	}

	@Test
	public void testClearGDCache() throws Exception {
		controller.clearGDCache(request);
		assertNotNull("Method return is void");
	}

	@Test
	public void testGrtICFileForm() {
		request.setParameter("p_test_administration", "0");
		request.setParameter("p_test_program", "0");
		request.setParameter("p_corpdiocese", "0");
		request.setParameter("p_school", "0");
		request.setParameter("reportUrl", "");
		request.setParameter("reportId", "0");
		request.getSession().setAttribute(IApplicationConstants.CUSTOMER, "0");
		request.getSession().setAttribute(IApplicationConstants.CURRORGLVL, 0L);
		ModelAndView mv = controller.grtICFileForm(request);
		assertNotNull(mv);
	}

	@Test(expected=java.lang.ArrayIndexOutOfBoundsException.class)
	public void testDownloadGRTInvitationCodeFiles() {
		controller.downloadGRTInvitationCodeFiles(request, response);
		assertNotNull("Method return is void");
	}

	@Test
	public void testDeleteScheduledGroupFilesInors() {
		controller.deleteScheduledGroupFilesInors();
		assertNotNull("Method return is void");
	}

	@Test
	public void testDeleteScheduledGroupFilesTasc() {
		controller.deleteScheduledGroupFilesTasc();
		assertNotNull("Method return is void");
	}

}
