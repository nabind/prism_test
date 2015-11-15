package com.ctb.prism.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ctb.prism.core.dao.UsabilityDAOImpl;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

public class CustomStringUtilTest {

	CustomStringUtil customStringUtil;
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityDAOImpl.class.getName());

	@Before
	public void setUp() throws Exception {
		customStringUtil = new CustomStringUtil();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testManageString() {
		String inputString = "012345678901234567890";
		String outputString = customStringUtil.manageString(inputString);
		assertEquals(outputString, "012345678901234...");
	}

	@Test
	public void testCapitalizeFirstCharacter() {
		String inputString = "abcd";
		String outputString = customStringUtil.capitalizeFirstCharacter(inputString);
		assertEquals(outputString, "Abcd");
	}

	@Test
	public void testAppendString() {
		String outputString = customStringUtil.appendString("Hello", "World");
		assertEquals(outputString, "HelloWorld");
	}

	@Test
	public void testEscapeQuote() {
		String inputString = "ab\"cd";
		String outputString = customStringUtil.escapeQuote(inputString);
		assertEquals(outputString, "ab'cd");
	}

	@Test
	public void testGetJasperParameterString() {
		String inputString = "abcd";
		String outputString = customStringUtil.getJasperParameterString(inputString);
		assertEquals(outputString, "$P{abcd}");
	}

	@Test
	public void testGetJasperParameterStringRegx() {
		String inputString = "abcd";
		String outputString = customStringUtil.getJasperParameterStringRegx(inputString);
		assertEquals(outputString, "\\$[P][{]abcd[}]");
	}

	@Test
	public void testReplace() {
		String text = "abcd";
		String oldsub = "";
		String newsub = "";
		boolean caseInsensitive = false;
		boolean firstOnly = false;
		String outputString = customStringUtil.replace(text, oldsub, newsub, caseInsensitive, firstOnly);
		assertEquals(outputString, "abcd");
		
		oldsub = "d";
		newsub = "e";
		outputString = customStringUtil.replace(text, oldsub, newsub, caseInsensitive, firstOnly);
		assertEquals(outputString, "abce");
	}

	@Test
	public void testReplaceAll() {
		String inputString = "abcd";
		String old = "";
		String now = "";
		String outputString = customStringUtil.replaceAll(inputString, old, now);
		assertEquals(outputString, "abcd");
	}

	@Test
	public void testReplaceCharacterInString() {
		char c = '?';
		String newValue = "1";
		String query = "SELECT * FROM USERS WHERE USERID = ?";
		String outputString = customStringUtil.replaceCharacterInString(c, newValue, query);
		assertEquals(outputString, "SELECT * FROM USERS WHERE USERID = 1");
		
		query = "SELECT * FROM USERS";
		outputString = customStringUtil.replaceCharacterInString(c, newValue, query);
		assertEquals(outputString, "SELECT * FROM USERS");
	}

	@Test
	public void testGetHMSTimeFormat() {
		long millis = 1421061450825L;
		String outputString = customStringUtil.getHMSTimeFormat(millis);
		assertEquals(outputString, "394739:17:30:825");
	}

	@Test
	public void testGetNotNullString() {
		String inputString = "";
		String outputString = customStringUtil.getNotNullString(inputString);
		assertEquals(outputString, "");
	}
	
	@Test
	public void testLog() {
		logger.log(IAppLogger.INFO, "Log INFO Test");
		logger.log(IAppLogger.WARN, "Log WARN Test");
		logger.log(IAppLogger.ERROR, "Log ERROR Test");
	}

}
