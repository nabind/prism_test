package com.ctb.prism.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetContractName() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetContractNameString() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetContractNameNoLogin() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetSaltWithUser() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testIsValidRole() {
		boolean isValidRole = Utils.isValidRole("USER_PARENT");
		assertEquals(isValidRole, true);
		boolean isInvalidRole = Utils.isValidRole("USER_XYZ");
		assertEquals(isInvalidRole, false);
	}

	@Test
	public void testIsValidRoles() {
		boolean isValidRoles = Utils.isValidRoles("ROLE_PARENT");
		assertEquals(isValidRoles, true);
		boolean isInvalidRoles = Utils.isValidRoles("ROLE_XYZ");
		assertEquals(isInvalidRoles, false);
	}

	@Test
	public void testGetRole() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetRoles() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testValidatePassword() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetAcsiPdfLocation() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testSetAcsiPdfLocation() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testConvertInputStreamToFile() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testConvertInputStreamToString() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetFileDataFromInputStream() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetDateTime() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetDateTimeString() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testConvertClobToString() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testBatchUpdateCheck() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testConvertListToCommaString() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testObjectToJson() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testJsonToObject() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testIsOdd() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testSerialize() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testDeserialize() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testConvertSpecialCharToHtmlChar() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testEncryptData() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testDecryptData() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testLogError() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testAttachCSSClassToMenuSet() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testGetMenuFromSet() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testArrayToSeparatedString() {
		assertNotNull("Not yet implemented");
	}

	@Test
	public void testWriteToFile() {
		assertNotNull("Not yet implemented");
	}

}
