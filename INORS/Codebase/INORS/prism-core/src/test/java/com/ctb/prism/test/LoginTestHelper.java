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
		paramMap.put("contractName", testParams.getContractName());
		paramMap.put(IApplicationConstants.PURPOSE_PRISM, IApplicationConstants.PURPOSE_LANDING_PAGE);
		return paramMap;
	}

	public static Map<String, Object> helpGetUserDetails(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static UserTO getUserTO() {
		UserTO user = new UserTO();
		return user;
	}

	public static Map<String, Object> helpGetMenuMap(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetActionMap(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
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
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpAddNewUser(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}

	public static Map<String, Object> helpGetUserEmail(TestParams testParams) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractName", testParams.getContractName());
		return paramMap;
	}
}
