package com.ctb.prism.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.servlet.ServletException;

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

import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class ParentControllerTest extends AbstractJUnit4SpringContextTests {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Autowired
	private ParentController controller;

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
	public void testParentRegistration() throws IOException {
		ModelAndView mv = controller.parentRegistration(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testAddNewInvitation() throws IOException {
		ModelAndView mv = controller.addNewInvitation(request, response);
		assertNull(mv);
	}

	@Test
	public void testRegister() throws IOException {
		request.setParameter("qsn1", "1");
		request.setParameter("qsn2", "2");
		request.setParameter("qsn3", "3");
		ModelAndView mv = controller.register(request, response);
		assertNotNull(mv);
	}

	@Test
	public void testMyAccount() throws ServletException, IOException {
		ModelAndView mv = controller.myAccount(request, response);
		assertNotNull(mv);
		System.out.println("testMyAccount.mv.getViewName() = " + mv.getViewName());
		assertEquals(mv.getViewName(), "user/profile");
	}

	@Test
	public void testUpdateProfile() throws IOException {
		ModelAndView mv = controller.updateProfile(request, response);
		assertNull(mv);
	}

	@Test
	public void testValidateCode() throws IOException {
		String json = controller.validateCode(request, response);
		assertNull(json);
	}

	@Test
	public void testCheckAvailability() throws IOException {
		String json = controller.checkAvailability(request, response);
		assertNull(json);
	}

	@Test
	public void testGetChildrenList() {
		String json = controller.getChildrenList(request, response);
		assertNull(json);
	}

	@Test
	public void testValidatePwd() {
		ModelAndView mv = controller.validatePwd(request, response);
		assertNull(mv);
	}

}
