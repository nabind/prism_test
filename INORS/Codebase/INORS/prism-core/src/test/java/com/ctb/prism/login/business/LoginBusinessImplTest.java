package com.ctb.prism.login.business;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.test.LoginTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class LoginBusinessImplTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private ILoginBusiness loginBusiness;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelectTest() {
		boolean status = loginBusiness.selectTest();
		assertNotNull(status);
	}

	@Test
	public void testGetUserByEmail() throws SystemException {
		Map<String, Object> paramMap = LoginTestHelper.helpGetUserByEmail(testParams);
		UserTO user = loginBusiness.getUserByEmail(paramMap);
		assertNotNull(user);
	}

	@Test
	public void testGetGrantedAuthorities() {
		List<GrantedAuthority> list = loginBusiness.getGrantedAuthorities(testParams.getUserName());
		assertNotNull(list);
	}

	@Test
	public void testGetTenant() {
		String tenant = loginBusiness.getTenant(testParams.getUserName());
		assertNotNull(tenant);
	}

	@Test
	public void testGetSystemConfigurationMessage() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetSystemConfigurationMessage(testParams);
		Map<String,Object> outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_TEACHER_LOGIN_PAGE);
		outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_PARENT_LOGIN_PAGE);
		outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_TEACHER_HOME_PAGE);
		outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_PARENT_HOME_PAGE);
		outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_GROWTH_HOME_PAGE);
		outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
		
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, "ANYSTRING");
		outputMap = loginBusiness.getSystemConfigurationMessage(paramMap);
		assertNotNull(outputMap);
	}

	@Test
	public void testCheckFirstTimeLogin() {
		boolean status = loginBusiness.checkFirstTimeLogin(testParams.getUserName());
		assertNotNull(status);
	}

	@Test
	public void testGetUserDetails() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetUserDetails(testParams);
		UserTO user = loginBusiness.getUserDetails(paramMap);
		assertNotNull(user);
	}

	@Test
	public void testGetUsersForSSO() throws Exception {
		UserTO userTO = LoginTestHelper.getUserTO();
		UserTO user = loginBusiness.getUsersForSSO(userTO);
		assertNotNull(user);
	}

	@Test
	public void testGetOrgLevel() {
		UserTO userTO = LoginTestHelper.getUserTO();
		UserTO user = loginBusiness.getOrgLevel(userTO);
		assertNotNull(user);
	}

	@Test
	public void testCheckUserAvailability() {
		boolean status = loginBusiness.checkUserAvailability(testParams.getUserName());
		assertNotNull(status);
	}

	@Test
	public void testGetUserOrgNode() {
		String orgNode = loginBusiness.getUserOrgNode(testParams.getUserName(), testParams.getContractName());
		assertNotNull(orgNode);
	}

	@Test
	public void testUpdateUserOrg() {
		String orgNodeId = "0";
		String oldOrgNodeid = "0";
		loginBusiness.updateUserOrg(testParams.getUserName(), orgNodeId, oldOrgNodeid, testParams.getContractName());
		assertNotNull("Return type is void");
	}

	@Test
	public void testAddNewUser() throws Exception {
		Map<String, Object> paramMap = LoginTestHelper.helpGetUserDetails(testParams);
		loginBusiness.addNewUser(paramMap);
		assertNotNull("Return type is void");
	}

	@Test
	public void testGetRootPath() {
		String customerId = "0";
		String testAdmin = "0";
		String rootPath = loginBusiness.getRootPath(customerId, testAdmin, testParams.getContractName());
		assertNotNull(rootPath);
	}

	@Test
	public void testGetMenuMap() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetMenuMap(testParams);
		Set<MenuTO> menuMap = loginBusiness.getMenuMap(paramMap);
		assertNotNull(menuMap);
	}

	@Test
	public void testGetActionMap() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetActionMap(testParams);
		Map<String,String> actionMap = loginBusiness.getActionMap(paramMap);
		assertNotNull(actionMap);
	}

	@Test
	public void testGetContractProerty() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetContractProerty(testParams);
		Map<String, Object> outputMap = loginBusiness.getContractProerty (paramMap);
		assertNotNull(outputMap);
	}

	@Test
	public void testGetPasswordHistory() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetPasswordHistory(testParams);
		List<String> list = loginBusiness.getPasswordHistory(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testCheckOrgHierarchy() {
		Map<String, Object> paramMap = LoginTestHelper.helpCheckOrgHierarchy(testParams);
		boolean status = loginBusiness.checkOrgHierarchy(paramMap);
		assertNotNull(status);
	}

	@Test
	public void testCheckUserRoleByUsername() {
		Map<String, Object> paramMap = LoginTestHelper.helpCheckUserRoleByUsername(testParams);
		loginBusiness.checkUserRoleByUsername(paramMap);
		assertNotNull("Return type is void");
	}

}
