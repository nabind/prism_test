package com.ctb.prism.test;

import java.util.HashMap;
import java.util.Map;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.login.transferobject.UserTO;

/**
 * This class provides input parameters for test methods.
 * 
 * @author TCS
 *
 */
public class LoginTestHelper {

	public static Map<String, Object> helpGetUserByEmail(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetSystemConfigurationMessage(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("REPORT_NAME", "Generic System Configuration");
		paramMap.put("MESSAGE_TYPE", "GSCM");
		paramMap.put("MESSAGE_NAME", "Common Header");
		paramMap.put("custProdId", 5050L);
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_LANDING_PAGE);
		return paramMap;
	}

	public static Map<String, Object> helpGetUserDetails(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName", "test");
		paramMap.put("password", "test");
		paramMap.put("userDisplayName", "test");
		paramMap.put("emailId", "test@ctb.com");
		paramMap.put("userStatus", "AC");
		paramMap.put("customer", "1059");
		paramMap.put("tenantId", "18134");
		paramMap.put("orgLevel", "2");
		paramMap.put("userRoles", "ROLE_USER");
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static UserTO getUserTO(TestParams testParams) {
		UserTO user = new UserTO();
		user.setContractName(testParams.getContractName());
		return user;
	}

	public static Map<String, Object> helpGetMenuMap(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roles", "");
		paramMap.put("orgNodeLevel", "0");
		paramMap.put("custProdId", 0L);
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetActionMap(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roles", "");
		paramMap.put("orgNodeLevel", "0");
		paramMap.put("custProdId", 0L);
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetContractProerty(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetPasswordHistory(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpCheckOrgHierarchy(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", "test");
		paramMap.put("custProdId", "5050");
		paramMap.put("prevOrgId", "18134");
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpCheckUserRoleByUsername(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetCustomerProduct(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loggedInCustomer", "0");
		paramMap.put("loggedInOrgId", "");
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpAddNewUser(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName", "test");
		paramMap.put("password", "test");
		paramMap.put("userDisplayName", "test");
		paramMap.put("emailId", "test@ctb.com");
		paramMap.put("userStatus", "AC");
		paramMap.put("customer", "1059");
		paramMap.put("tenantId", "18134");
		paramMap.put("orgLevel", "2");
		paramMap.put("userRoles", "ROLE_USER");
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetUserEmail(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}
}
