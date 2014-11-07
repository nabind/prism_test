package com.ctb.prism.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.security.tokens.UsernamePasswordAuthenticationToken;
import com.ctb.prism.login.transferobject.UserTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/test-context.xml" })
public class ParentControllerTest extends AbstractJUnit4SpringContextTests {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Autowired
	private ParentController controller;

	private String username = "ctbadmin";

	@Before
	public void setUp() throws Exception {
		request = new MockHttpServletRequest();
		request.getSession().setAttribute(IApplicationConstants.CURRUSER, username);
		response = new MockHttpServletResponse();
		String password = "Passwd12";
		String contractName = "inors";
		UserTO user = new UserTO();
		user.setUserName(username);
		user.setPassword(password);
		user.setContractName(contractName);
		UserDetails targetUser = new AuthenticatedUser(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword());
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public void testMyAccount() throws Exception {
		ModelAndView mv = controller.myAccount(request, response);
		assertNotNull(mv);
		assertEquals(mv.getViewName(), "user/profile");
	}

}
