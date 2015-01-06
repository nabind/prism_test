package com.ctb.prism.admin.util;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentListTO;
import com.ctb.prism.test.AdminTestHelper;
import com.ctb.prism.test.TestParams;
import com.ctb.prism.test.TestUtil;

public class StudentDataUtilTest {

	StudentDataUtil util;

	TestParams testParams;

	@Before
	public void setUp() throws Exception {
		util = new StudentDataUtil();
		testParams = TestUtil.getTestParams();
		TestUtil.byPassLogin(testParams);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateDelimitedByteArray() {
		List<StudentDataTO> students = new ArrayList<StudentDataTO>();
		students.add(AdminTestHelper.getStudentDataTO(testParams));
		char delimiter = ',';
		byte[] bytes = util.createDelimitedByteArray(students, delimiter);
		assertNotNull(bytes);
	}

	@Test
	public void testCreateCSVByteArray() {
		List<StudentDataTO> students = new ArrayList<StudentDataTO>();
		students.add(AdminTestHelper.getStudentDataTO(testParams));
		byte[] bytes = util.createCSVByteArray(students);
		assertNotNull(bytes);
	}

	@Test
	public void testCreateFixedLengthFile() {
		List<StudentDataTO> students = new ArrayList<StudentDataTO>();
		students.add(AdminTestHelper.getStudentDataTO(testParams));
		util.createFixedLengthFile(students, "");
	}

	@Test
	public void testCreateXMLByteArray() {
		List<StudentDataTO> students = new ArrayList<StudentDataTO>();
		students.add(AdminTestHelper.getStudentDataTO(testParams));
		byte[] bytes = util.createXMLByteArray(students);
		assertNotNull(bytes);
	}

	@Test
	public void testCreatePasswordProtectedZipFile() {
		List<StudentDataTO> students = new ArrayList<StudentDataTO>();
		students.add(AdminTestHelper.getStudentDataTO(testParams));
		ZipFile file = util.createPasswordProtectedZipFile(1L, students, "DAT", "Passwd12".toCharArray());
		assertNotNull(file);
	}

	@Test
	public void testCleanup() {
		util.cleanup();
	}

	@Test
	public void testPaddedWrapIntObject() {
		int limit = 3;
		Object strObject = "abcd";
		String val = util.paddedWrap(limit, strObject);
		assertNotNull(val);

		val = util.paddedWrap(limit, null);
		assertNotNull(val);
	}

	@Test
	public void testPaddedWrapIntObjectChar() {
		int limit = 3;
		Object strObject = "abcd";
		char c = '*';
		String val = util.paddedWrap(limit, strObject, c);
		assertNotNull(val);
	}

	@Test
	public void testPaddedWrapIntObjectCharInt() {
		int limit = 3;
		Object strObject = "abcd";
		char c = '*';
		int align = StudentDataConstants.ALIGN_RIGHT;
		String val = util.paddedWrap(limit, strObject, c, align);
		assertNotNull(val);
	}

	@Test
	public void testGetLast4Chars() {
		Object obj = "01234";
		String last4Chars = util.getLast4Chars(obj);
		assertNotNull(last4Chars);

		last4Chars = util.getLast4Chars(null);
		assertNotNull(last4Chars);
	}

	@Test
	public void testGetXMLString() {
		StudentListTO to = AdminTestHelper.getStudentListTO(testParams);
		String xml = util.getXMLString(to);
		assertNotNull(xml);
	}

	@Test
	public void testMockStudentList() {
		StudentListTO student = util.mockStudentList();
		assertNotNull(student);
	}

	@Test
	public void testGetMockStudents() {
		List<StudentDataTO> studentList = util.getMockStudents();
		assertNotNull(studentList);
	}

	@Test
	public void testGetHeader() {
		StudentDataTO header = util.getHeader();
		assertNotNull(header);
	}

}
