package com.ctb.prism.web.controller;

import static org.junit.Assert.assertNotNull;

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

import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

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
		ModelAndView mv = controller.groupDownloadFiles(request, response);
		assertNotNull(mv);
	}

	/*@Test
	public void testDeleteGroupDownloadFiles() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadGroupDownloadFiles() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testCheckFileAvailability() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetStudentFileName() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetRequestDetailViewData() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testBulkCandidateReportDownload() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadCandicateReport() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testIcLetterDownloads() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetReportParameters() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGroupDownloadForm() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGroupDownloadFunction() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadZippedPdf() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadFile() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGetTenantHierarchy() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadBulkPdf() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testRetainDownloadValues() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testClearGDCache() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testGrtICFileForm() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDownloadGRTInvitationCodeFiles() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDeleteScheduledGroupFilesInors() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testDeleteScheduledGroupFilesTasc() {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}*/

}
