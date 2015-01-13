package com.ctb.prism.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {

	Utils utils;

	@Before
	public void setUp() throws Exception {
		utils = new Utils();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetContractName() {
		String contractName = utils.getContractName();
		assertNotNull(contractName);
	}

	@Test
	public void testGetContractNameString() {
		String contractName = utils.getContractName("inors");
		assertNotNull(contractName);
	}

	@Test
	public void testGetContractNameNoLogin() {
		String contractName = utils.getContractNameNoLogin("inors");
		assertNotNull(contractName);
	}

	@Test
	public void testGetSaltWithUser() {
		String userName = null;
		String salt = "";
		String saltWithUse = utils.getSaltWithUser(userName, salt);
		assertNotNull(saltWithUse);

		userName = "abcd";
		saltWithUse = utils.getSaltWithUser(userName, salt);
		assertNotNull(saltWithUse);
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
		List<String> list = new ArrayList<String>();
		list.add("abcd");
		String commaString = utils.convertListToCommaString(list);
		assertNotNull(commaString);
	}

	@Test
	public void testObjectToJson() {
		Object obj = "abcd";
		String json = utils.objectToJson(obj);
		assertNotNull(json);
	}

	@Test
	public void testJsonToObject() {
		String json = "{}";
		Object obj = utils.jsonToObject(json, Object.class);
		assertNotNull(obj);
	}

	@Test
	public void testIsOdd() {
		int n = 5;
		boolean odd = utils.isOdd(n);
		assertEquals(odd, true);

		n = 6;
		odd = utils.isOdd(n);
		assertEquals(odd, false);
	}

	@Test
	public void testSerialize() {
		Object obj = "abcd";
		byte[] bytes = utils.serialize(obj);
		assertNotNull(bytes);
	}

	@Test
	public void testDeserialize() {
		byte[] bytes = "abcd".getBytes();
		Object obj = utils.deserialize(bytes);
		assertNotNull(obj);
	}

	@Test
	public void testConvertSpecialCharToHtmlChar() {
		String input = "†";
		String output = utils.convertSpecialCharToHtmlChar(input);
		assertEquals(output, "&dagger;");
	}

	@Test
	public void testEncryptData() {
		String input = "abcd";
		String output = utils.encryptData(input);
		assertNotNull(output);
	}

	@Test
	public void testDecryptData() {
		String input = "abcd";
		String output = utils.decryptData(input);
		assertNotNull(output);
	}

	@Test
	public void testLogError() {
		String input = "Error log test";
		utils.logError(input);
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
		String[] strArr = new String[] {};
		char separator = '*';
		String str = utils.arrayToSeparatedString(strArr, separator);
		assertNotNull(str);

		strArr = new String[] { "abcd" };
		str = utils.arrayToSeparatedString(strArr, separator);
		assertNotNull(str);
	}

	@Test
	public void testWriteToFile() {
		String fileName = "testWriteToFile.txt";
		String content = "abcd";
		File f = utils.writeToFile(fileName, content);
		assertNotNull(f);
	}

	@Test
	public void testObjectByteArrayToPrimitiveByteArray() {
		Byte[] inputBytes = new Byte[3];
		inputBytes[0] = new Byte("1");
		inputBytes[1] = new Byte("2");
		inputBytes[2] = new Byte("3");
		byte[] outputBytes = utils.objectByteArrayToPrimitiveByteArray(inputBytes);
		assertNotNull(outputBytes);
	}

	@Test
	public void testPrimitiveByteArrayToObjectByteArray() {
		byte[] inputBytes = "abcd".getBytes();
		Byte[] outputBytes = utils.primitiveByteArrayToObjectByteArray(inputBytes);
		assertNotNull(outputBytes);
	}

}
