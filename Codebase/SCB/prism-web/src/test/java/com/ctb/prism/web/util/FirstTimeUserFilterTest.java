package com.ctb.prism.web.util;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class FirstTimeUserFilterTest extends AbstractJUnit4SpringContextTests{

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockHttpSession session;
	
	private MockFilterChain chain;
	
	@Autowired
	FirstTimeUserFilter firstTimeUserFilter;
	
	TestParams testParams;
	
	@Before
	public void setUp() throws Exception {
		// Populate test params from properties file
		testParams = TestUtil.getTestParams();

		// Create request and response for Controller methods
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session = new MockHttpSession();
		
		chain = new MockFilterChain();

		// Set all Session Attributes
		TestUtil.setSessionAttributes(request, testParams);

		// Bypass login
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoFilter() throws IOException, ServletException {
		request.getSession().setAttribute(IApplicationConstants.FIRST_TIME_LOGIN,"true");
		firstTimeUserFilter.doFilter(request, response, chain);
		
	}

	@Test
	public void testRequiresExitUser() {
		request.setRequestURI("http://10.160.23.51:8080/prism/userlogin.do?theme=tasc");
		assertTrue(firstTimeUserFilter.requiresExitUser(request));
	}

	@Test
	public void testRequiresSwitchUser() {
		request.setRequestURI("http://10.160.23.51:8080/prism/userlogin.do?theme=tasc");
		assertTrue(firstTimeUserFilter.requiresSwitchUser(request));
	}

	@Test
	public void testRequiresChangepassword() {
		request.setRequestURI("http://10.160.23.51:8080/prism/userlogin.do?theme=tasc");
		assertTrue(firstTimeUserFilter.requiresChangepassword(request));
	}

}
