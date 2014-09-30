package com.ctb.prism.parent.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.security.tokens.UsernamePasswordAuthenticationToken;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/test-context.xml" })
public class ParentBusinessImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private IParentBusiness parentBusiness;

	private String username = "ctbadmin";

	@Before
	public void setUp() throws Exception {
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
	public void testGetSecretQuestions() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", "inors");
		List<QuestionTO> secretQuestionList = parentBusiness.getSecretQuestions(paramMap);
		assertEquals(secretQuestionList.size(), 11);
	}

	@Test
	public void testManageParentAccountDetails() {
		ParentTO parentTO = parentBusiness.manageParentAccountDetails(username);
		assertNotNull(parentTO);
		assertEquals(parentTO.getUserId(), 1L);
	}

}
