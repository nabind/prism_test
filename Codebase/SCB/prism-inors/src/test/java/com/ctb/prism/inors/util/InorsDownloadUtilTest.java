package com.ctb.prism.inors.util;

import static org.junit.Assert.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class InorsDownloadUtilTest  extends AbstractJUnit4SpringContextTests {

/*	InorsDownloadUtil util;

	@Before
	public void setUp() throws Exception {
		util = new InorsDownloadUtil();
	}

	@After
	public void tearDown() throws Exception {
	}*/

	@Test
	public void testGetTableDataBytes() {
		ArrayList<ArrayList<String>> tableData = null;
		String delimiter = ",";
		byte[] bytes = InorsDownloadUtil.getTableDataBytes(tableData, delimiter);
		assertNotNull(bytes);

		tableData = new ArrayList<ArrayList<String>>();
		ArrayList<String> listData = new ArrayList<String>();
		listData.add("Hello");
		tableData.add(listData);
		bytes = InorsDownloadUtil.getTableDataBytes(tableData, delimiter);
		assertNotNull(bytes);
	}

	@Test
	public void testWrap() {
		Object data = null;
		char wrapChar = '"';
		String s = InorsDownloadUtil.wrap(data, wrapChar);
		assertNotNull(s);

		data = "Hello";
		s = InorsDownloadUtil.wrap(data, wrapChar);
		assertNotNull(s);
	}

	@Test
	public void testGetUserDataBytes() {
		List<UserDataTO> userList = null;
		String delimiter = ",";
		byte[] bytes = InorsDownloadUtil.getUserDataBytes(userList, delimiter);
		assertNotNull(bytes);

		userList = new ArrayList<UserDataTO>();
		userList.add(new UserDataTO());
		bytes = InorsDownloadUtil.getUserDataBytes(userList, delimiter);
		assertNotNull(bytes);
	}

	@Test
	public void testGetUserDataList() {
		List<UserTO> userList = new ArrayList<UserTO>();
		userList.add(new UserTO());
		List<UserDataTO> userDataList = InorsDownloadUtil.getUserDataList(userList);
		assertNotNull(userDataList);
	}

	@Test
	public void testGetTableDataFromResultSet() throws SQLException {
		ResultSet rs = null;
		ArrayList<String> aliasList = new ArrayList<String>();
		ArrayList<String> headerList = new ArrayList<String>();
		ArrayList<ArrayList<String>> lostOfList = InorsDownloadUtil.getTableDataFromResultSet(rs, aliasList, headerList);
	}

	@Test
	public void testGetRowDataLayout() {
		String commaString = null;
		ArrayList<String> stringList = InorsDownloadUtil.getRowDataLayout(commaString);
		assertNotNull(stringList);
		
		commaString = "A,B";
		stringList = InorsDownloadUtil.getRowDataLayout(commaString);
		assertNotNull(stringList);
	}

}
