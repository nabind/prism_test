package com.ctb.prism.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.ctb.prism.login.security.provider.AuthenticatedUser;
import com.ctb.prism.login.security.tokens.UsernamePasswordAuthenticationToken;
import com.ctb.prism.login.transferobject.UserTO;

public class TestUtil {

	private static final String PROPERTY_FILE_NAME = "test.properties";

	/**
	 * This method bypasses login. It can be used from outside the web
	 * application where logging in is not possible or it is not required.
	 * 
	 * @param testParams
	 */
	public static void byPassLogin(TestParams testParams) {
		UserTO user = new UserTO();
		user.setUserName(testParams.getUserName());
		user.setPassword(testParams.getPassword());
		user.setContractName(testParams.getContractName());
		UserDetails targetUser = new AuthenticatedUser(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword());
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	public static TestParams getTestParams() {
		TestUtil testUtil = new TestUtil();
		Properties testProperties = testUtil.load(PROPERTY_FILE_NAME);
		String testPropertyFileName = testProperties.getProperty("testName").toLowerCase() + ".properties";
		System.out.println("Loading " + testPropertyFileName + " ...");
		Properties prismProperties = testUtil.load(testPropertyFileName);
		return testUtil.mapProperties(prismProperties);
	}

	/**
	 * Creates the bean from the property file.
	 * 
	 * @param prop
	 * @return
	 */
	private TestParams mapProperties(Properties prop) {
		TestParams params = new TestParams();
		params.setContractName(prop.getProperty("test.contractName"));
		params.setUserName(prop.getProperty("test.userName"));
		params.setPassword(prop.getProperty("test.password"));
		params.setNoOfSecretQuestions(prop.getProperty("test.noOfSecretQuestions"));
		params.setInvitationCode(prop.getProperty("test.invitationCode"));

		System.out.println("contractName: " + params.getContractName());
		return params;

	}

	/**
	 * Loads the property file.
	 * 
	 * @param fileName
	 * @return
	 */
	private Properties load(String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = getClass().getClassLoader().getResourceAsStream(fileName);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
