package com.ctb.prism.login.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.exception.SystemException;
import com.ctb.prism.core.transferobject.ObjectValueTO;
import com.ctb.prism.login.transferobject.MenuTO;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.test.LoginTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
@TransactionConfiguration(transactionManager = "txManager")
public class LoginDAOImplTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private LoginDAOImpl loginDao;

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
	public void testGetGrantedAuthoritiesString() {
		List<GrantedAuthority> list = loginDao.getGrantedAuthorities(testParams.getUserName());
		assertNotNull(list);
	}

	@Test
	public void testGetGrantedAuthoritiesStringLongString() {
		long orgLevel = 0L;
		String userType = "";
		List<GrantedAuthority> list = loginDao.getGrantedAuthorities(testParams.getUserName(), orgLevel, userType);
		assertNotNull(list);
	}

	@Test
	public void testGetGrantedAuthoritiesStringLongStringString() {
		long orgLevel = 0L;
		String userType = "";
		List<GrantedAuthority> list = loginDao.getGrantedAuthorities(testParams.getUserName(), orgLevel, userType, testParams.getContractName());
		assertNotNull(list);
	}

	@Test
	public void testCheckFirstTimeLogin() {
		String str = loginDao.checkFirstTimeLogin(testParams.getUserName());
		assertNotNull(str);
	}

	@Test
	public void testGetTenantId() {
		String str = loginDao.getTenantId( testParams.getUserName() );
		assertNotNull(str);
	}

	@Test
	public void testGetUserDetails() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetUserDetails(testParams);
		UserTO user = loginDao.getUserDetails(paramMap);
		assertNotNull(user);
	}

	@Test
	public void testGetUserByEmail() throws SystemException {
		Map<String, Object> paramMap = LoginTestHelper.helpGetUserDetails(testParams);
		UserTO user = loginDao.getUserByEmail(paramMap);
		assertNotNull(user);
	}

	@Test
	public void testGetOrgLevel() {
		UserTO userTO = LoginTestHelper.getUserTO(testParams);
		UserTO user = loginDao.getOrgLevel(userTO);
		assertNull(user);
	}

	@Test
	public void testGetSystemConfigurationMessage() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetSystemConfigurationMessage(testParams);
		String str = loginDao.getSystemConfigurationMessage(paramMap);
		assertNotNull(str);
	}

	@Test
	public void testGetCustomerProduct() throws BusinessException {
		Map<String, Object> paramMap = LoginTestHelper.helpGetCustomerProduct(testParams);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> list = loginDao.getCustomerProduct(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testGetUsersForSSOUserTO() {
		UserTO userTO = LoginTestHelper.getUserTO(testParams);
		UserTO user = loginDao.getUsersForSSO(userTO);
		assertNotNull(user);
	}

	@Test
	public void testCheckUserAvailabilityString() {
		boolean status = loginDao.checkUserAvailability(testParams.getUserName());
		assertNotNull(status);
	}

	@Test
	public void testCheckUserAvailabilityStringString() {
		boolean status = loginDao.checkUserAvailability(testParams.getUserName(), testParams.getContractName());
		assertNotNull(status);
	}

	@Test
	public void testGetUserOrgNode() {
		String str = loginDao.getUserOrgNode(testParams.getUserName(), testParams.getContractName());
		assertNotNull(str);
	}

	@Test
	public void testUpdateUserOrg() {
		String OrgNodeId = "0";
		String oldOrgNodeid = "0";
		loginDao.updateUserOrg(testParams.getUserName(), OrgNodeId, oldOrgNodeid, testParams.getContractName());
		assertNotNull("Return type is void");
	}

	@Test
	public void testAddNewUser() throws Exception {
		Map<String, Object> paramMap = LoginTestHelper.helpAddNewUser(testParams);
		loginDao.addNewUser(paramMap);
		assertNotNull("Return type is void");
	}

	@Test
	public void testGetCurrentAdmin() throws SystemException {
		ObjectValueTO to = loginDao.getCurrentAdmin(testParams.getContractName());
		assertNotNull(to);
	}

	@Test
	public void testGetRootPath() {
		String customerId = "1059";
		String testAdmin = "5050";
		String str = loginDao.getRootPath(customerId, testAdmin, testParams.getContractName());
		assertNotNull(str);
	}

	@Test
	public void testGetMenuMap() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetMenuMap(testParams);
		Set<MenuTO> set = loginDao.getMenuMap(paramMap);
		assertNotNull(set);
	}

	@Test
	public void testGetActionMap() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetActionMap(testParams);
		Map<String,String> map = loginDao.getActionMap(paramMap);
		assertNotNull(map);
	}

	@Test
	public void testGetContractProerty() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetContractProerty(testParams);
		Map<String, Object> map = loginDao.getContractProerty (paramMap);
		assertNotNull(map);
	}

	@Test
	public void testGetPasswordHistory() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetPasswordHistory(testParams);
		List<String> list = loginDao.getPasswordHistory(paramMap);
		assertNotNull(list);
	}

	@Test
	public void testGetUserEmail() {
		Map<String, Object> paramMap = LoginTestHelper.helpGetUserEmail(testParams);
		UserTO user = loginDao.getUserEmail(paramMap);
		assertNotNull(user);
	}

	@Test
	public void testCheckOrgHierarchy() {
		Map<String, Object> paramMap = LoginTestHelper.helpCheckOrgHierarchy(testParams);
		boolean status = loginDao.checkOrgHierarchy(paramMap);
		assertNotNull(status);
	}

	@Test
	public void testCheckUserRoleByUsername() {
		Map<String, Object> paramMap = LoginTestHelper.helpCheckOrgHierarchy(testParams);
		loginDao.checkUserRoleByUsername(paramMap);
		assertNotNull("Return type is void");
	}

}
