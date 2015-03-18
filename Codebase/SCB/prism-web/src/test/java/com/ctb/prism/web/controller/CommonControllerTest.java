package com.ctb.prism.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class CommonControllerTest extends AbstractJUnit4SpringContextTests{
	
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockHttpSession session;
	
	@Autowired
	private CommonController controller;
	TestParams testParams;
	
	@Before
	public void setUp() throws Exception {
		// Populate test params from properties file
		testParams = TestUtil.getTestParams();

		// Create request and response for Controller methods
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session = new MockHttpSession();

		// Set all Session Attributes
		TestUtil.setSessionAttributes(request, testParams);

		// Bypass login
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
			
	}

	@Test
	public void testLoadErrorPage(){
		ModelAndView mv = controller.loadErrorPage(request, response);
		assertEquals(mv.getViewName(), "error/error");	
	}
	
	@Test
	public void testLoadJSPView(){
		ModelAndView mv = controller.loadJSPView(request, response);
		assertNotNull(mv);	
	}
	
	@Test
	public void testLoadEmptyReport(){
		ModelAndView mv = controller.loadEmptyReport("Print-Ready Roster", "The report is empty.");
		assertNotNull(mv);	
	}
}
