package com.ctb.prism.parent.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.security.tokens.UsernamePasswordAuthenticationToken;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ParentTO;
import com.ctb.prism.parent.transferobject.QuestionTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/test-context.xml" })
@TransactionConfiguration(transactionManager = "txManager")
public class ParentDAOImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ParentDAOImpl parentDao;

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
		List<QuestionTO> secretQuestionList = parentDao.getSecretQuestions(paramMap);
		assertEquals(secretQuestionList.size(), 11);
	}

	@Test
	@Ignore
	public void testCheckUserAvailability() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCheckActiveUserAvailability() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIsRoleAlreadyTagged() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testValidateIC() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetStudentForIC() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRegisterUser() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	@Rollback(false)
	public void testSaveInvitationCodeClaim() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUpdateInvitationCodeClaimCount() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetChildrenList() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetParentList() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSearchParent() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSearchParentAutoComplete() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetStudentList() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSearchStudentAutoComplete() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSearchStudent() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSearchStudentOnRedirect() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetAssessmentList() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUpdateAssessmentDetails() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFirstTimeUserLogin() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testManageParentAccountDetails() {
		ParentTO parentTO = parentDao.manageParentAccountDetails(username);
		assertNotNull(parentTO);
		assertEquals(parentTO.getUserId(), 1L);
	}

	@Test
	@Ignore
	public void testGetParentSecretQuestionDetails() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetSecurityQuestionForUser() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testValidateAnswers() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetUserNamesByEmail() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUpdateUserProfile() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeletePasswordHistAnswer() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCheckInvitationCodeClaim() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAddInvitationToAccount() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetSchoolOrgId() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRegenerateActivationCode() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPopulateGrade() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPopulateSubtest() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPopulateObjective() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAddNewContent() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLoadManageContent() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetContentForEdit() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUpdateContent() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteContent() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testModifyGenericForEdit() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetStudentSubtest() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetArticleTypeDetails() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetArticleDescription() {
		assertNotNull("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetGradeSubtestInfo() {
		assertNotNull("Not yet implemented");
	}

}
