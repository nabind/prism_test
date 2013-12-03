package com.ctb.prism.admin.util;
 
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.DelimitedParserBuilder;
import org.beanio.builder.FixedLengthParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.builder.XmlParserBuilder;
import org.beanio.stream.RecordIOException;
import org.springframework.util.FileCopyUtils;

import com.ctb.prism.admin.transferobject.StudentDataTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentScoreDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ContentScoreTO;
import com.ctb.prism.admin.transferobject.studentdata.CustHierarchyDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.DemoTO;
import com.ctb.prism.admin.transferobject.studentdata.ItemResponseTO;
import com.ctb.prism.admin.transferobject.studentdata.ItemResponsesDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ObjectiveScoreDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.ObjectiveScoreTO;
import com.ctb.prism.admin.transferobject.studentdata.OrgDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.RosterDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentBioTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentDemoTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentDetailsTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentListTO;
import com.ctb.prism.admin.transferobject.studentdata.StudentSurveyBioTO;
import com.ctb.prism.admin.transferobject.studentdata.SubtestAccommodationTO;
import com.ctb.prism.admin.transferobject.studentdata.SubtestAccommodationsTO;
import com.ctb.prism.admin.transferobject.studentdata.SurveyBioTO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class StudentDataUtil {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(StudentDataUtil.class.getName());

	/**
	 * This method does not create any file in disk
	 * 
	 * @param students
	 */
	public static byte[] createDelimitedByteArray(final List<StudentDataTO> students, final char delimiter){
		StreamFactory factory = StreamFactory.newInstance();
		StreamBuilder builder = new StreamBuilder("StudentData")
				.format("delimited").parser(new DelimitedParserBuilder(delimiter))
				.addRecord(StudentDataTO.class);
		factory.define(builder);
		StringWriter stringWriter = new StringWriter();
		BeanWriter out = factory.createWriter("StudentData", stringWriter);
		try {
			for (StudentDataTO sd : students) {
				out.write(sd);
			}
			out.flush();
			out.close();
			logger.log(IAppLogger.INFO, "Student Data File Created Successfully");
		} catch (RecordIOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		return stringWriter.getBuffer().toString().getBytes();
	}
	
	/**
	 * This method does not create any file in disk
	 * 
	 * @param students
	 */
	public static byte[] createCSVByteArray(final List<StudentDataTO> students){
		StreamFactory factory = StreamFactory.newInstance();
		StreamBuilder builder = new StreamBuilder("StudentData")
				.format("csv").parser(new CsvParserBuilder())
				.addRecord(StudentDataTO.class);
		factory.define(builder);
		StringWriter stringWriter = new StringWriter();
		BeanWriter out = factory.createWriter("StudentData", stringWriter);
		try {
			for (StudentDataTO sd : students) {
				out.write(sd);
			}
			out.flush();
			out.close();
			logger.log(IAppLogger.INFO, "Student Data File Created Successfully");
		} catch (RecordIOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		return stringWriter.getBuffer().toString().getBytes();
	}

	/**
	 * This method creates a fixed length file in disk
	 * 
	 * @param students
	 */
	public static void createFixedLengthFile(final List<StudentDataTO> students, final String filePath){
		StreamFactory factory = StreamFactory.newInstance();
		StreamBuilder builder = new StreamBuilder("StudentData")
				.format("fixedlength").parser(new FixedLengthParserBuilder())
				.addRecord(StudentDataTO.class);
		factory.define(builder);
		BeanWriter out = factory.createWriter("StudentData", new File(filePath));
		try {
			for (StudentDataTO sd : students) {
				out.write(sd);
			}
			out.flush();
			out.close();
			logger.log(IAppLogger.INFO, "Student Data File Created Successfully");
		} catch (RecordIOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * This method does not create any file in disk
	 * 
	 * @param students
	 */
	public static byte[] createXMLByteArray(final List<StudentDataTO> students){
		StreamFactory factory = StreamFactory.newInstance();
		StreamBuilder builder = new StreamBuilder("StudentData")
				.format("xml").parser(new XmlParserBuilder())
				.addRecord(StudentDataTO.class);
		factory.define(builder);
		StringWriter stringWriter = new StringWriter();
		BeanWriter out = factory.createWriter("StudentData", stringWriter);
		try {
			for (StudentDataTO sd : students) {
				out.write(sd);
			}
			out.flush();
			out.close();
			logger.log(IAppLogger.INFO, "Student Data File Created Successfully");
		} catch (RecordIOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		}
		return stringWriter.getBuffer().toString().getBytes();
	}
	
	/**
	 * This method creates a password protected zip file in disk
	 * 
	 * @param orgNodeLevel
	 * @param studentDataList
	 * @param fileType
	 * @param pwd
	 * @return
	 */
	public static ZipFile createPasswordProtectedZipFile(final Long orgNodeLevel, List<StudentDataTO> studentDataList, final String fileType, final char[] pwd) {
		logger.log(IAppLogger.INFO, "Creating student data file");
		for (StudentDataTO s : studentDataList) {
			if (StudentDataConstants.ORG_NODE_LEVEL.equals(orgNodeLevel)) {
				s.setStudentID(paddedWrap(StudentDataConstants.COL034, s.getStudentID(), ' ', StudentDataConstants.ALIGN_RIGHT));
			} else {
				String last4Chars = getLast4Chars(s.getStudentID());
				s.setStudentID(paddedWrap(StudentDataConstants.COL034, last4Chars, 'x', StudentDataConstants.ALIGN_RIGHT));
			}
		}
		byte[] bytes = null;
		String inputFilePath = null;
		if (fileType.equals("DAT")) {
			studentDataList.add(0, getHeader());
			bytes = createDelimitedByteArray(studentDataList, ',');
			inputFilePath = StudentDataConstants.STUDENT_DATA_FILE_DAT_PATH;
		} else if (fileType.equals("CSV")) {
			studentDataList.add(0, getHeader());
			bytes = createDelimitedByteArray(studentDataList, '\t');
			inputFilePath = StudentDataConstants.STUDENT_DATA_FILE_CSV_PATH;
		} else if (fileType.equals("XML")) {
			studentDataList.addAll(0, getMockStudents());
			bytes = createXMLByteArray(studentDataList);
			inputFilePath = StudentDataConstants.STUDENT_DATA_FILE_XML_PATH;
		}

		ZipFile zipFile = null;
		try {
			File file = new File(inputFilePath);
			FileCopyUtils.copy(bytes, file);

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			if (StudentDataConstants.ORG_NODE_LEVEL.equals(orgNodeLevel)) {
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(pwd);
			}

			zipFile = new ZipFile(StudentDataConstants.STUDENT_DATA_ZIP_FILE_PATH);
			zipFile.addFile(file, parameters);
			logger.log(IAppLogger.INFO, "Student data file created");
		} catch (ZipException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, "", e);
			e.printStackTrace();
		} 
		return zipFile;
	}
	
	/**
	 * This method deletes all temporary files created in disk
	 */
	public static void cleanup(){
			new File(StudentDataConstants.STUDENT_DATA_FILE_DAT_PATH).delete();
			new File(StudentDataConstants.STUDENT_DATA_FILE_XML_PATH).delete();
			new File(StudentDataConstants.STUDENT_DATA_ZIP_FILE_PATH).delete();
	}

	/**
	 * Used to add leading space to a String input. Additionally wraps the
	 * String with double quote
	 * 
	 * @param limit
	 * @param str
	 * @return
	 */
	public static String paddedWrap(int limit, Object str) {
		return paddedWrap(limit, str, ' ', StudentDataConstants.ALIGN_LEFT);
	}
	
	/**
	 * Used to add leading space to a String input. Additionally wraps the
	 * String with double quote
	 * 
	 * @param limit
	 * @param str
	 * @param c
	 * @return
	 */
	public static String paddedWrap(int limit, Object str, char c) {
		return paddedWrap(limit, str, c, StudentDataConstants.ALIGN_LEFT);
	}

	/**
	 * Used to add leading character to a String input. Additionally wraps the
	 * String with double quote
	 * 
	 * @param limit
	 * @param str
	 * @param c
	 * @param align
	 * @return
	 */
	public static String paddedWrap(int limit, Object strObject, char c, int align) {
		if (strObject == null) {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < limit - 2; i++) {
				buf.insert(0, c);
			}
			buf.insert(0, '"');
			buf.insert(buf.length(), '"');
			return buf.toString();
		}
		String str = strObject.toString();
		int length = str.length();
		StringBuilder buf = new StringBuilder(str);

		if ((length + 2) > limit) {
			// throw new StringIndexOutOfBoundsException("Length of String is " + length + ", Expected <= " + (limit - 2));
			str = str.substring(0, limit);
		}
		for (int i = 0; i < limit - length - 2; i++) {
			if (align == StudentDataConstants.ALIGN_RIGHT)
				buf.insert(0, c);
			else
				buf.insert(buf.length(), c);
		}
		buf.insert(0, '"');
		buf.insert(buf.length(), '"');

		return buf.toString();
	}
	
	/**
	 * Returns last 4 characters of the ExamineeId
	 * 
	 * @param obj
	 * @return
	 */
	public static String getLast4Chars(Object obj) {
		if (obj == null){
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < StudentDataConstants.COL034; i++) {
				buf.append(" ");
			}
			return buf.toString();
		}
		String str = obj.toString();
		return str.substring(str.length() - 4);
	}
	
	public static String getXMLString(StudentListTO to) {
		XStream xs = new XStream(new StaxDriver(new XmlFriendlyNameCoder("_-", "_")));
		xs.autodetectAnnotations(true);
		String xml = xs.toXML(to);
		System.out.println(xml);
		return xml;
	}

	public static StudentListTO mockStudentList() {
		StudentListTO studentListTO = new StudentListTO();
		studentListTO.setRosterDetailsTO(mockRosterDetailsList());
		return studentListTO;
	}

	private static List<RosterDetailsTO> mockRosterDetailsList() {
		List<RosterDetailsTO> rosterDetailsList = new ArrayList<RosterDetailsTO>();
		rosterDetailsList.add(mockRosterDetailsTO());
		return rosterDetailsList;
	}

	private static RosterDetailsTO mockRosterDetailsTO() {
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		rosterDetailsTO.setRosterId("1234567890");
		rosterDetailsTO.setCustHierarchyDetailsTO(mockCustHierarchyDetails());
		rosterDetailsTO.setStudentDetailsTO(mockStudentDetails());
		rosterDetailsTO.setCollContentDetailsTO(mockCollContentDetailsList());
		return rosterDetailsTO;
	}

	private static List<ContentDetailsTO> mockCollContentDetailsList() {
		List<ContentDetailsTO> contentDetailsList = new ArrayList<ContentDetailsTO>();
		contentDetailsList.add(mockContentDetailsTO());
		return contentDetailsList;
	}

	private static ContentDetailsTO mockContentDetailsTO() {
		ContentDetailsTO contentDetailsTO = new ContentDetailsTO();
		contentDetailsTO.setContentCode("1");
		contentDetailsTO.setScoringMethod("I");
		contentDetailsTO.setStatusCode("");
		contentDetailsTO.setDateTestTaken("09252013");
		contentDetailsTO.setDataChanged(true);
		contentDetailsTO.setSubtestAccommodationsTO(mockSubtestAccommodationsTO());
		contentDetailsTO.setItemResponsesDetailsTO(mockItemResponsesDetailsTO());
		contentDetailsTO.setContentScoreDetailsTO(mockContentScoreDetailsTO());
		contentDetailsTO.setCollObjectiveScoreDetailsTO(mockCollObjectiveScoreDetailsList());
		return contentDetailsTO;
	}

	private static List<ObjectiveScoreDetailsTO> mockCollObjectiveScoreDetailsList() {
		List<ObjectiveScoreDetailsTO> objectiveScoreDetailsList = new ArrayList<ObjectiveScoreDetailsTO>();
		objectiveScoreDetailsList.add(mockObjectiveScoreDetailsTO());
		return objectiveScoreDetailsList;
	}

	private static ObjectiveScoreDetailsTO mockObjectiveScoreDetailsTO() {
		ObjectiveScoreDetailsTO objectiveScoreDetailsTO = new ObjectiveScoreDetailsTO();
		objectiveScoreDetailsTO.setObjectiveName("Obj1_Read");
		objectiveScoreDetailsTO.setObjectiveCode(" ");
		objectiveScoreDetailsTO.setCollObjectiveScoreTO(mockCollObjectiveScoreList());
		return objectiveScoreDetailsTO;
	}

	private static List<ObjectiveScoreTO> mockCollObjectiveScoreList() {
		List<ObjectiveScoreTO> objectiveScoreList = new ArrayList<ObjectiveScoreTO>();
		objectiveScoreList.add(mockObjectiveScoreTO());
		return objectiveScoreList;
	}

	private static ObjectiveScoreTO mockObjectiveScoreTO() {
		ObjectiveScoreTO objectiveScoreTO = new ObjectiveScoreTO();
		objectiveScoreTO.setScoreType("NC");
		objectiveScoreTO.setValue("10");
		return objectiveScoreTO;
	}

	private static ContentScoreDetailsTO mockContentScoreDetailsTO() {
		ContentScoreDetailsTO contentScoreDetailsTO = new ContentScoreDetailsTO();
		contentScoreDetailsTO.setCollContentScoreTO(mockCollContentScoreList());
		return contentScoreDetailsTO;
	}

	private static List<ContentScoreTO> mockCollContentScoreList() {
		List<ContentScoreTO> contentScoreTO = new ArrayList<ContentScoreTO>();
		contentScoreTO.add(mockContentScoreTO());
		return contentScoreTO;
	}

	private static ContentScoreTO mockContentScoreTO() {
		ContentScoreTO contentScoreTO = new ContentScoreTO();
		contentScoreTO.setScoreType("NC");
		contentScoreTO.setScoreValue("777");
		return contentScoreTO;
	}

	private static ItemResponsesDetailsTO mockItemResponsesDetailsTO() {
		ItemResponsesDetailsTO itemResponsesDetailsTO = new ItemResponsesDetailsTO();
		itemResponsesDetailsTO.setItemResponseTO(mockItemResponseList());
		return itemResponsesDetailsTO;
	}

	private static List<ItemResponseTO> mockItemResponseList() {
		List<ItemResponseTO> itemResponseList = new ArrayList<ItemResponseTO>();
		itemResponseList.add(mockItemResponseTO());
		return itemResponseList;
	}

	private static ItemResponseTO mockItemResponseTO() {
		ItemResponseTO itemResponseTO = new ItemResponseTO();
		itemResponseTO.setItemSetType("SR");
		itemResponseTO.setItemCode("01");
		itemResponseTO.setScoreValue("12ABCD34");
		return itemResponseTO;
	}

	private static SubtestAccommodationsTO mockSubtestAccommodationsTO() {
		SubtestAccommodationsTO subtestAccommodationsTO = new SubtestAccommodationsTO();
		subtestAccommodationsTO.setCollSubtestAccommodationTO(mockCollSubtestAccommodation());
		return subtestAccommodationsTO;
	}

	private static List<SubtestAccommodationTO> mockCollSubtestAccommodation() {
		List<SubtestAccommodationTO> subtestAccommodationTO = new ArrayList<SubtestAccommodationTO>();
		subtestAccommodationTO.add(mockSubtestAccommodationTO());
		return subtestAccommodationTO;
	}

	private static SubtestAccommodationTO mockSubtestAccommodationTO() {
		SubtestAccommodationTO subtestAccommodationTO = new SubtestAccommodationTO();
		subtestAccommodationTO.setName("Acc_Audio");
		subtestAccommodationTO.setValue("Y");
		return subtestAccommodationTO;
	}

	private static StudentDetailsTO mockStudentDetails() {
		StudentDetailsTO studentDetailsTO = new StudentDetailsTO();
		studentDetailsTO.setStudentBioTO(mockStudentBioTO());
		studentDetailsTO.setStudentDemoTO(mockStudentDemoTO());
		studentDetailsTO.setStudentSurveyBioTO(mockStudentSurveyBioTO());
		return studentDetailsTO;
	}

	private static StudentSurveyBioTO mockStudentSurveyBioTO() {
		StudentSurveyBioTO studentSurveyBioTO = new StudentSurveyBioTO();
		studentSurveyBioTO.setDataChanged(true);
		studentSurveyBioTO.setCollSurveyBioTO(mockCollSurveyBioList());
		return studentSurveyBioTO;
	}

	private static List<SurveyBioTO> mockCollSurveyBioList() {
		List<SurveyBioTO> surveyBioList = new ArrayList<SurveyBioTO>();
		surveyBioList.add(mockSurveyBioTO());
		return surveyBioList;
	}

	private static SurveyBioTO mockSurveyBioTO() {
		SurveyBioTO surveyBioTO = new SurveyBioTO();
		surveyBioTO.setSurveyName("Rslvd_Ethncty");
		surveyBioTO.setSurveyValue("4");
		return surveyBioTO;
	}

	private static StudentDemoTO mockStudentDemoTO() {
		StudentDemoTO studentDemoTO = new StudentDemoTO();
		studentDemoTO.setDataChanged(true);
		studentDemoTO.setCollDemoTO(mockCollDemoList());
		return studentDemoTO;
	}

	private static List<DemoTO> mockCollDemoList() {
		List<DemoTO> demoList = new ArrayList<DemoTO>();
		demoList.add(mockDemoTO());
		return demoList;
	}

	private static DemoTO mockDemoTO() {
		DemoTO demoTO = new DemoTO();
		demoTO.setDemoName("Test_Lan");
		demoTO.setDemovalue("E");
		return demoTO;
	}

	private static StudentBioTO mockStudentBioTO() {
		StudentBioTO studentBioTO = new StudentBioTO();
		studentBioTO.setDataChanged(true);
		studentBioTO.setOasStudentId("1000000123");
		studentBioTO.setLastName("AA");
		studentBioTO.setFirstName("BB");
		studentBioTO.setMiddleInit("I");
		studentBioTO.setGender("F");
		studentBioTO.setGrade("AD");
		studentBioTO.setExamineeId("123456789");
		studentBioTO.setBirthDate("091298");
		studentBioTO.setChrnlgclAge("15");
		return studentBioTO;
	}

	private static CustHierarchyDetailsTO mockCustHierarchyDetails() {
		CustHierarchyDetailsTO custHierarchyDetailsTO = new CustHierarchyDetailsTO();
		custHierarchyDetailsTO.setCustomerId("A123456789");
		custHierarchyDetailsTO.setMaxHierarchy("4");
		custHierarchyDetailsTO.setDataChanged(true);
		custHierarchyDetailsTO.setCollOrgDetailsTO(mockCollOrgDetailsList());
		return custHierarchyDetailsTO;
	}

	private static List<OrgDetailsTO> mockCollOrgDetailsList() {
		List<OrgDetailsTO> orgDetailsList = new ArrayList<OrgDetailsTO>();
		orgDetailsList.add(mockOrgDetailsTO());
		return orgDetailsList;
	}

	private static OrgDetailsTO mockOrgDetailsTO() {
		OrgDetailsTO orgDetailsTO = new OrgDetailsTO();
		orgDetailsTO.setOrgName("Indiana");
		orgDetailsTO.setOrgLabel("State");
		orgDetailsTO.setOrgLevel("1");
		orgDetailsTO.setOrgNodeId("100001");
		orgDetailsTO.setOrgCode("S001");
		orgDetailsTO.setParentOrgCode("0");
		return orgDetailsTO;
	}

	/**
	 * Returns a List of mock StudentDataTO objects. This method is for testing
	 * purpose only.
	 * 
	 * @return
	 */
	public static List<StudentDataTO> getMockStudents(){
		List<StudentDataTO> students = new ArrayList<StudentDataTO>();

		StudentDataTO s = new StudentDataTO();

		s.setCustID									(paddedWrap(StudentDataConstants.COL001, ""));
		s.setRosterID								(paddedWrap(StudentDataConstants.COL002, ""));
		s.setHierarchyA_Name						(paddedWrap(StudentDataConstants.COL003, ""));
		s.setHierarchyA_Code						(paddedWrap(StudentDataConstants.COL004, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyA_SpCodes						(paddedWrap(StudentDataConstants.COL005, ""));
		s.setHierarchyB_Name						(paddedWrap(StudentDataConstants.COL006, ""));
		s.setHierarchyB_Code						(paddedWrap(StudentDataConstants.COL007, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyB_SpCodes						(paddedWrap(StudentDataConstants.COL008, ""));
		s.setHierarchyC_Name						(paddedWrap(StudentDataConstants.COL009, ""));
		s.setHierarchyC_Code						(paddedWrap(StudentDataConstants.COL010, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyC_SpCodes						(paddedWrap(StudentDataConstants.COL011, ""));
		s.setHierarchyD_Name						(paddedWrap(StudentDataConstants.COL012, ""));
		s.setHierarchyD_Code						(paddedWrap(StudentDataConstants.COL013, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyD_SpCodes						(paddedWrap(StudentDataConstants.COL014, ""));
		s.setHierarchyE_Name						(paddedWrap(StudentDataConstants.COL015, ""));
		s.setHierarchyE_Code						(paddedWrap(StudentDataConstants.COL016, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyE_SpCodes						(paddedWrap(StudentDataConstants.COL017, ""));
		s.setHierarchyF_Name						(paddedWrap(StudentDataConstants.COL018, ""));
		s.setHierarchyF_Code						(paddedWrap(StudentDataConstants.COL019, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyF_SpCodes						(paddedWrap(StudentDataConstants.COL020, ""));
		s.setHierarchyG_Name						(paddedWrap(StudentDataConstants.COL021, ""));
		s.setHierarchyG_Code						(paddedWrap(StudentDataConstants.COL022, "123456", '0', StudentDataConstants.ALIGN_RIGHT)); // Leading 0
		s.setHierarchyG_SpCodes						(paddedWrap(StudentDataConstants.COL023, ""));
		s.setLastName								(paddedWrap(StudentDataConstants.COL024, ""));
		s.setFirstName								(paddedWrap(StudentDataConstants.COL025, ""));
		s.setMiddleInitial							(paddedWrap(StudentDataConstants.COL026, ""));
		s.setDateOfBirth							(paddedWrap(StudentDataConstants.COL027, "12-31-1990")); // MM-DD-YY
		s.setForm									(paddedWrap(StudentDataConstants.COL028, "A")); // A, B, C
		s.setChangedFormFlag						(paddedWrap(StudentDataConstants.COL029, "Y")); // Y, blank
		s.setTestLang								(paddedWrap(StudentDataConstants.COL030, "E")); // E, S
		s.setTestMode								(paddedWrap(StudentDataConstants.COL031, "0")); // 0, 1
		s.setGender									(paddedWrap(StudentDataConstants.COL032, "M")); // M, F, blank
		s.setResolvedEthnicity						(paddedWrap(StudentDataConstants.COL033, "1")); // 1-7, blank
		s.setStudentID								(paddedWrap(StudentDataConstants.COL034, ""));
		s.setPhoneNumber							(paddedWrap(StudentDataConstants.COL035, ""));
		s.setLithocode								(paddedWrap(StudentDataConstants.COL036, ""));
		s.setImagingID								(paddedWrap(StudentDataConstants.COL037, ""));
		s.setStreetAddress							(paddedWrap(StudentDataConstants.COL038, ""));
		s.setAptNo									(paddedWrap(StudentDataConstants.COL039, ""));
		s.setCity									(paddedWrap(StudentDataConstants.COL040, ""));
		s.setState									(paddedWrap(StudentDataConstants.COL041, ""));
		s.setZipCode								(paddedWrap(StudentDataConstants.COL042, ""));
		s.setCounty_Code							(paddedWrap(StudentDataConstants.COL043, ""));
		s.setEduc_Center_Code						(paddedWrap(StudentDataConstants.COL044, ""));
		s.setAcc_Audio_Rd							(paddedWrap(StudentDataConstants.COL045, ""));
		s.setAcc_Audio_Wr							(paddedWrap(StudentDataConstants.COL046, ""));
		s.setAcc_Audio_Math1						(paddedWrap(StudentDataConstants.COL047, ""));
		s.setAcc_Audio_Math2						(paddedWrap(StudentDataConstants.COL048, ""));
		s.setAcc_Audio_Sc							(paddedWrap(StudentDataConstants.COL049, ""));
		s.setAcc_Audio_SS							(paddedWrap(StudentDataConstants.COL050, ""));
		s.setAcc_Breaks_Rd							(paddedWrap(StudentDataConstants.COL051, ""));
		s.setAcc_Breaks_Wr							(paddedWrap(StudentDataConstants.COL052, ""));
		s.setAcc_Breaks_Math1						(paddedWrap(StudentDataConstants.COL053, ""));
		s.setAcc_Breaks_Math2						(paddedWrap(StudentDataConstants.COL054, ""));
		s.setAcc_Breaks_SC							(paddedWrap(StudentDataConstants.COL055, ""));
		s.setAcc_Breaks_SS							(paddedWrap(StudentDataConstants.COL056, ""));
		s.setAcc_Calculator_Rd						(paddedWrap(StudentDataConstants.COL057, ""));
		s.setAcc_Calculator_Wr						(paddedWrap(StudentDataConstants.COL058, ""));
		s.setAcc_Calculator_Math1					(paddedWrap(StudentDataConstants.COL059, ""));
		s.setAcc_Calculator_SS						(paddedWrap(StudentDataConstants.COL060, ""));
		s.setAcc_Duration1_25_Rd					(paddedWrap(StudentDataConstants.COL061, ""));
		s.setAcc_Duration1_25_Wr					(paddedWrap(StudentDataConstants.COL062, ""));
		s.setAcc_Duration1_25_Math1					(paddedWrap(StudentDataConstants.COL063, ""));
		s.setAcc_Duration1_25_Math2					(paddedWrap(StudentDataConstants.COL064, ""));
		s.setAcc_Duration1_25_Sc					(paddedWrap(StudentDataConstants.COL065, ""));
		s.setAcc_Duration1_25_SS					(paddedWrap(StudentDataConstants.COL066, ""));
		s.setAcc_Duration1_5_Rd						(paddedWrap(StudentDataConstants.COL067, ""));
		s.setAcc_Duration1_5_Wr						(paddedWrap(StudentDataConstants.COL068, ""));
		s.setAcc_Duration1_5_Math1					(paddedWrap(StudentDataConstants.COL069, ""));
		s.setAcc_Duration1_5_Math2					(paddedWrap(StudentDataConstants.COL070, ""));
		s.setAcc_Duration1_5_Sc						(paddedWrap(StudentDataConstants.COL071, ""));
		s.setAcc_Duration1_5_SS						(paddedWrap(StudentDataConstants.COL072, ""));
		s.setAcc_Duration2_0_Rd						(paddedWrap(StudentDataConstants.COL073, ""));
		s.setAcc_Duration2_0_Wr						(paddedWrap(StudentDataConstants.COL074, ""));
		s.setAcc_Duration2_0_Math1					(paddedWrap(StudentDataConstants.COL075, ""));
		s.setAcc_Duration2_0_Math2					(paddedWrap(StudentDataConstants.COL076, ""));
		s.setAcc_Duration2_0_Sc						(paddedWrap(StudentDataConstants.COL077, ""));
		s.setAcc_Duration2_0_SS						(paddedWrap(StudentDataConstants.COL078, ""));
		s.setAcc_PhysicalSupport_Rd					(paddedWrap(StudentDataConstants.COL079, ""));
		s.setAcc_PhysicalSupport_Wr					(paddedWrap(StudentDataConstants.COL080, ""));
		s.setAcc_PhysicalSupport_Math1				(paddedWrap(StudentDataConstants.COL081, ""));
		s.setAcc_PhysicalSupport_Math2				(paddedWrap(StudentDataConstants.COL082, ""));
		s.setAcc_PhysicalSupport_Sc					(paddedWrap(StudentDataConstants.COL083, ""));
		s.setAcc_PhysicalSupport_SS					(paddedWrap(StudentDataConstants.COL084, ""));
		s.setAcc_Scribe_Rd							(paddedWrap(StudentDataConstants.COL085, ""));
		s.setAcc_Scribe_Wr							(paddedWrap(StudentDataConstants.COL086, ""));
		s.setAcc_Scribe_Math1						(paddedWrap(StudentDataConstants.COL087, ""));
		s.setAcc_Scribe_Math2						(paddedWrap(StudentDataConstants.COL088, ""));
		s.setAcc_Scribe_Sc							(paddedWrap(StudentDataConstants.COL089, ""));
		s.setAcc_Scribe_SS							(paddedWrap(StudentDataConstants.COL090, ""));
		s.setAcc_TechDevice_Rd						(paddedWrap(StudentDataConstants.COL091, ""));
		s.setAcc_TechDevice_Wr						(paddedWrap(StudentDataConstants.COL092, ""));
		s.setAcc_TechDevice_Math1					(paddedWrap(StudentDataConstants.COL093, ""));
		s.setAcc_TechDevice_Math2					(paddedWrap(StudentDataConstants.COL094, ""));
		s.setAcc_TechDevice_Sc						(paddedWrap(StudentDataConstants.COL095, ""));
		s.setAcc_TechDevice_SS						(paddedWrap(StudentDataConstants.COL096, ""));
		s.setAcc_SepRoom_Rd							(paddedWrap(StudentDataConstants.COL097, ""));
		s.setAcc_SepRoom_Wr							(paddedWrap(StudentDataConstants.COL098, ""));
		s.setAcc_SepRoom_Math1						(paddedWrap(StudentDataConstants.COL099, ""));
		s.setAcc_SepRoom_Math2						(paddedWrap(StudentDataConstants.COL100, ""));
		s.setAcc_SepRoom_Sc							(paddedWrap(StudentDataConstants.COL101, ""));
		s.setAcc_SepRoom_SS							(paddedWrap(StudentDataConstants.COL102, ""));
		s.setAcc_SmGroup_Rd							(paddedWrap(StudentDataConstants.COL103, ""));
		s.setAcc_SmGroup_Wr							(paddedWrap(StudentDataConstants.COL104, ""));
		s.setAcc_SmGroup_Math1						(paddedWrap(StudentDataConstants.COL105, ""));
		s.setAcc_SmGroup_Math2						(paddedWrap(StudentDataConstants.COL106, ""));
		s.setAcc_SmGroup_Sc							(paddedWrap(StudentDataConstants.COL107, ""));
		s.setAcc_SmGroup_SS							(paddedWrap(StudentDataConstants.COL108, ""));
		s.setAcc_Other_Rd							(paddedWrap(StudentDataConstants.COL109, ""));
		s.setAcc_Other_Wr							(paddedWrap(StudentDataConstants.COL110, ""));
		s.setAcc_Other_Math1						(paddedWrap(StudentDataConstants.COL111, ""));
		s.setAcc_Other_Math2						(paddedWrap(StudentDataConstants.COL112, ""));
		s.setAcc_Other_Sc							(paddedWrap(StudentDataConstants.COL113, ""));
		s.setAcc_Other_SS							(paddedWrap(StudentDataConstants.COL114, ""));
		s.setExaminer_Ack_Accomm					(paddedWrap(StudentDataConstants.COL115, ""));
		s.setHomeLang								(paddedWrap(StudentDataConstants.COL116, ""));
		s.setK_12_Educ_Completed					(paddedWrap(StudentDataConstants.COL117, ""));
		s.setSubj_Taken_Passed_Rd					(paddedWrap(StudentDataConstants.COL118, ""));
		s.setSubj_Taken_Passed_Wr					(paddedWrap(StudentDataConstants.COL119, ""));
		s.setSubj_Taken_Passed_Math					(paddedWrap(StudentDataConstants.COL120, ""));
		s.setSubj_Taken_Passed_Sc					(paddedWrap(StudentDataConstants.COL121, ""));
		s.setSubj_Taken_Passed_SS					(paddedWrap(StudentDataConstants.COL122, ""));
		s.setSubj_Taken_Passed_None					(paddedWrap(StudentDataConstants.COL123, ""));
		s.setNo_Times_Taken_Rd						(paddedWrap(StudentDataConstants.COL124, ""));
		s.setNo_Times_Taken_Wr						(paddedWrap(StudentDataConstants.COL125, ""));
		s.setNo_Times_Taken_Math					(paddedWrap(StudentDataConstants.COL126, ""));
		s.setNo_Times_Taken_Sc						(paddedWrap(StudentDataConstants.COL127, ""));
		s.setNo_Times_Taken_SS						(paddedWrap(StudentDataConstants.COL128, ""));
		s.setRetake_Rd								(paddedWrap(StudentDataConstants.COL129, ""));
		s.setRetake_Wr								(paddedWrap(StudentDataConstants.COL130, ""));
		s.setRetake_Math							(paddedWrap(StudentDataConstants.COL131, ""));
		s.setRetake_Sc								(paddedWrap(StudentDataConstants.COL132, ""));
		s.setRetake_SS								(paddedWrap(StudentDataConstants.COL133, ""));
		s.setRetake_None							(paddedWrap(StudentDataConstants.COL134, ""));
		s.setTake_Readiness_Assessment				(paddedWrap(StudentDataConstants.COL135, ""));
		s.setPrepare_County_Prog					(paddedWrap(StudentDataConstants.COL136, ""));
		s.setPrepare_Sch_Dist_Prog					(paddedWrap(StudentDataConstants.COL137, ""));
		s.setPrepare_Military_Prog					(paddedWrap(StudentDataConstants.COL138, ""));
		s.setPrepare_Religious_Prog					(paddedWrap(StudentDataConstants.COL139, ""));
		s.setPrepare_Purchased_My_Own_Study_Books	(paddedWrap(StudentDataConstants.COL140, ""));
		s.setPrepare_Online_Study_Prog				(paddedWrap(StudentDataConstants.COL141, ""));
		s.setPrepare_Homeschool						(paddedWrap(StudentDataConstants.COL142, ""));
		s.setPrepare_Tutor							(paddedWrap(StudentDataConstants.COL143, ""));
		s.setPrepare_Self_Taught					(paddedWrap(StudentDataConstants.COL144, ""));
		s.setRecent_Class_Rd						(paddedWrap(StudentDataConstants.COL145, "Y")); // Y, N, blank
		s.setRecent_Class_Wr						(paddedWrap(StudentDataConstants.COL146, "Y")); // Y, N, blank
		s.setRecent_Class_Math						(paddedWrap(StudentDataConstants.COL147, "Y")); // Y, N, blank
		s.setRecent_Class_Sc						(paddedWrap(StudentDataConstants.COL148, "Y")); // Y, N, blank
		s.setRecent_Class_SS						(paddedWrap(StudentDataConstants.COL149, "Y")); // Y, N, blank
		s.setMonths_Studied_Subj_Rd					(paddedWrap(StudentDataConstants.COL150, "1")); // 1-5, blank
		s.setMonths_Studied_Subj_Wr					(paddedWrap(StudentDataConstants.COL151, "1")); // 1-5, blank
		s.setMonths_Studied_Subj_Math				(paddedWrap(StudentDataConstants.COL152, "1")); // 1-5, blank
		s.setMonths_Studied_Subj_Sc					(paddedWrap(StudentDataConstants.COL153, "1")); // 1-5, blank
		s.setMonths_Studied_Subj_SS					(paddedWrap(StudentDataConstants.COL154, "1")); // 1-5, blank
		s.setGrade_in_Subj_Rd						(paddedWrap(StudentDataConstants.COL155, "A")); // A-F, blank
		s.setGrade_in_Subj_Wr						(paddedWrap(StudentDataConstants.COL156, "A")); // A-F, blank
		s.setGrade_in_Subj_Math						(paddedWrap(StudentDataConstants.COL157, "A")); // A-F, blank
		s.setGrade_in_Subj_Sc						(paddedWrap(StudentDataConstants.COL158, "A")); // A-F, blank
		s.setGrade_in_Subj_SS						(paddedWrap(StudentDataConstants.COL159, "A")); // A-F, blank
		s.setTestFormat_Braille						(paddedWrap(StudentDataConstants.COL160, "")); // Y, blank
		s.setTestFormat_LP							(paddedWrap(StudentDataConstants.COL161, "")); // Y, blank
		s.setLocal_Field_1							(paddedWrap(StudentDataConstants.COL162, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_2							(paddedWrap(StudentDataConstants.COL163, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_3							(paddedWrap(StudentDataConstants.COL164, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_4							(paddedWrap(StudentDataConstants.COL165, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_5							(paddedWrap(StudentDataConstants.COL166, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_6							(paddedWrap(StudentDataConstants.COL167, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_7							(paddedWrap(StudentDataConstants.COL168, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_8							(paddedWrap(StudentDataConstants.COL169, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_9							(paddedWrap(StudentDataConstants.COL170, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_10							(paddedWrap(StudentDataConstants.COL171, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_11							(paddedWrap(StudentDataConstants.COL172, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_12							(paddedWrap(StudentDataConstants.COL173, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_13							(paddedWrap(StudentDataConstants.COL174, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_14							(paddedWrap(StudentDataConstants.COL175, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_15							(paddedWrap(StudentDataConstants.COL176, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_16							(paddedWrap(StudentDataConstants.COL177, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_17							(paddedWrap(StudentDataConstants.COL178, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_18							(paddedWrap(StudentDataConstants.COL179, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_19							(paddedWrap(StudentDataConstants.COL180, "")); // 0 - 9, Multimark '-', Blank
		s.setLocal_Field_20							(paddedWrap(StudentDataConstants.COL181, "")); // 0 - 9, Multimark '-', Blank
		s.setCandidate_Acknowledgement				(paddedWrap(StudentDataConstants.COL182, ""));
		   
		s.setReading_dateTestTaken					(paddedWrap(StudentDataConstants.COL183, ""));
		s.setReading_numberCorrect					(paddedWrap(StudentDataConstants.COL184, ""));
		s.setReading_scaleScore						(paddedWrap(StudentDataConstants.COL185, ""));
		s.setReading_hSE_Score						(paddedWrap(StudentDataConstants.COL186, ""));
		s.setReading_percentileRank					(paddedWrap(StudentDataConstants.COL187, ""));
		s.setReading_nCE							(paddedWrap(StudentDataConstants.COL188, ""));
		s.setReading_std_Obj_Mstry_Scr_1			(paddedWrap(StudentDataConstants.COL189, ""));
		s.setReading_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL190, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_2			(paddedWrap(StudentDataConstants.COL191, ""));
		s.setReading_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL192, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_3			(paddedWrap(StudentDataConstants.COL193, ""));
		s.setReading_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL194, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_4			(paddedWrap(StudentDataConstants.COL195, ""));
		s.setReading_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL196, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_5			(paddedWrap(StudentDataConstants.COL197, ""));
		s.setReading_not_All_items_atmpt_5			(paddedWrap(StudentDataConstants.COL198, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_6			(paddedWrap(StudentDataConstants.COL199, ""));
		s.setReading_not_All_items_atmpt_6			(paddedWrap(StudentDataConstants.COL200, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_7			(paddedWrap(StudentDataConstants.COL201, ""));
		s.setReading_not_All_items_atmpt_7			(paddedWrap(StudentDataConstants.COL202, "*")); // *, blank
		s.setReading_std_Obj_Mstry_Scr_8			(paddedWrap(StudentDataConstants.COL203, ""));
		s.setReading_not_All_items_atmpt_8			(paddedWrap(StudentDataConstants.COL204, "*")); // *, blank
		   
		s.setWriting_dateTestTaken					(paddedWrap(StudentDataConstants.COL205, ""));
		s.setWriting_numberCorrect					(paddedWrap(StudentDataConstants.COL206, ""));
		s.setWriting_scaleScore						(paddedWrap(StudentDataConstants.COL207, ""));
		s.setWriting_hSE_Score						(paddedWrap(StudentDataConstants.COL208, ""));
		s.setWriting_percentileRank					(paddedWrap(StudentDataConstants.COL209, ""));
		s.setWriting_nCE							(paddedWrap(StudentDataConstants.COL210, ""));
		s.setWriting_std_Obj_Mstry_Scr_1			(paddedWrap(StudentDataConstants.COL211, ""));
		s.setWriting_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL212, "*")); // *, blank
		s.setWriting_std_Obj_Mstry_Scr_2			(paddedWrap(StudentDataConstants.COL213, ""));
		s.setWriting_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL214, "*")); // *, blank
		s.setWriting_std_Obj_Mstry_Scr_3			(paddedWrap(StudentDataConstants.COL215, ""));
		s.setWriting_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL216, "*")); // *, blank
		s.setWriting_std_Obj_Mstry_Scr_4			(paddedWrap(StudentDataConstants.COL217, ""));
		s.setWriting_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL218, "*")); // *, blank
		   
		s.setEla_scaleScore							(paddedWrap(StudentDataConstants.COL219, ""));
		s.setEla_hSE_Score							(paddedWrap(StudentDataConstants.COL220, ""));
		s.setEla_percentileRank						(paddedWrap(StudentDataConstants.COL221, ""));
		s.setEla_nCE								(paddedWrap(StudentDataConstants.COL222, ""));
		   
		s.setMath_dateTestTaken						(paddedWrap(StudentDataConstants.COL223, ""));
		s.setMath_numberCorrect						(paddedWrap(StudentDataConstants.COL224, ""));
		s.setMath_scaleScore						(paddedWrap(StudentDataConstants.COL225, ""));
		s.setMath_hSE_Score							(paddedWrap(StudentDataConstants.COL226, ""));
		s.setMath_percentileRank					(paddedWrap(StudentDataConstants.COL227, ""));
		s.setMath_nCE								(paddedWrap(StudentDataConstants.COL228, ""));
		s.setMath_std_Obj_Mstry_Scr_1				(paddedWrap(StudentDataConstants.COL229, ""));
		s.setMath_not_All_items_atmpt_1				(paddedWrap(StudentDataConstants.COL230, "*")); // *, blank
		s.setMath_std_Obj_Mstry_Scr_2				(paddedWrap(StudentDataConstants.COL231, ""));
		s.setMath_not_All_items_atmpt_2				(paddedWrap(StudentDataConstants.COL232, "*")); // *, blank
		s.setMath_std_Obj_Mstry_Scr_3				(paddedWrap(StudentDataConstants.COL233, ""));
		s.setMath_not_All_items_atmpt_3				(paddedWrap(StudentDataConstants.COL234, "*")); // *, blank
		s.setMath_std_Obj_Mstry_Scr_4				(paddedWrap(StudentDataConstants.COL235, ""));
		s.setMath_not_All_items_atmpt_4				(paddedWrap(StudentDataConstants.COL236, "*")); // *, blank
		s.setMath_std_Obj_Mstry_Scr_5				(paddedWrap(StudentDataConstants.COL237, ""));
		s.setMath_not_All_items_atmpt_5				(paddedWrap(StudentDataConstants.COL238, "*")); // *, blank
		s.setMath_std_Obj_Mstry_Scr_6				(paddedWrap(StudentDataConstants.COL239, ""));
		s.setMath_not_All_items_atmpt_6				(paddedWrap(StudentDataConstants.COL240, "*")); // *, blank
		s.setMath_std_Obj_Mstry_Scr_7				(paddedWrap(StudentDataConstants.COL241, ""));
		s.setMath_not_All_items_atmpt_7				(paddedWrap(StudentDataConstants.COL242, "*")); // *, blank
		   
		s.setScience_dateTestTaken					(paddedWrap(StudentDataConstants.COL243, ""));
		s.setScience_numberCorrect					(paddedWrap(StudentDataConstants.COL244, ""));
		s.setScience_scaleScore						(paddedWrap(StudentDataConstants.COL245, ""));
		s.setScience_hSE_Score						(paddedWrap(StudentDataConstants.COL246, ""));
		s.setScience_percentileRank					(paddedWrap(StudentDataConstants.COL247, ""));
		s.setScience_nCE							(paddedWrap(StudentDataConstants.COL248, ""));
		s.setScience_std_Obj_Mstry_Scr_1			(paddedWrap(StudentDataConstants.COL249, ""));
		s.setScience_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL250, "*")); // *, blank
		s.setScience_std_Obj_Mstry_Scr_2			(paddedWrap(StudentDataConstants.COL251, ""));
		s.setScience_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL252, "*")); // *, blank
		s.setScience_std_Obj_Mstry_Scr_3			(paddedWrap(StudentDataConstants.COL253, ""));
		s.setScience_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL254, "*")); // *, blank
		s.setScience_std_Obj_Mstry_Scr_4			(paddedWrap(StudentDataConstants.COL255, ""));
		s.setScience_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL256, "*")); // *, blank
		s.setScience_std_Obj_Mstry_Scr_5			(paddedWrap(StudentDataConstants.COL257, ""));
		s.setScience_not_All_items_atmpt_5			(paddedWrap(StudentDataConstants.COL258, "*")); // *, blank
		   
		s.setSocial_dateTestTaken					(paddedWrap(StudentDataConstants.COL259, ""));
		s.setSocial_numberCorrect					(paddedWrap(StudentDataConstants.COL260, ""));
		s.setSocial_scaleScore						(paddedWrap(StudentDataConstants.COL261, ""));
		s.setSocial_hSE_Score						(paddedWrap(StudentDataConstants.COL262, ""));
		s.setSocial_percentileRank					(paddedWrap(StudentDataConstants.COL263, ""));
		s.setSocial_nCE								(paddedWrap(StudentDataConstants.COL264, ""));
		s.setSocial_std_Obj_Mstry_Scr_1				(paddedWrap(StudentDataConstants.COL265, ""));
		s.setSocial_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL266, "*")); // *, blank
		s.setSocial_std_Obj_Mstry_Scr_2				(paddedWrap(StudentDataConstants.COL267, ""));
		s.setSocial_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL268, "*")); // *, blank
		s.setSocial_std_Obj_Mstry_Scr_3				(paddedWrap(StudentDataConstants.COL269, ""));
		s.setSocial_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL270, "*")); // *, blank
		s.setSocial_std_Obj_Mstry_Scr_4				(paddedWrap(StudentDataConstants.COL271, ""));
		s.setSocial_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL272, "*")); // *, blank
		s.setSocial_std_Obj_Mstry_Scr_5				(paddedWrap(StudentDataConstants.COL273, ""));
		s.setSocial_not_All_items_atmpt_5			(paddedWrap(StudentDataConstants.COL274, "*")); // *, blank
		s.setSocial_std_Obj_Mstry_Scr_6				(paddedWrap(StudentDataConstants.COL275, ""));
		s.setSocial_not_All_items_atmpt_6			(paddedWrap(StudentDataConstants.COL276, "*")); // *, blank
		s.setSocial_std_Obj_Mstry_Scr_7				(paddedWrap(StudentDataConstants.COL277, ""));
		s.setSocial_not_All_items_atmpt_7			(paddedWrap(StudentDataConstants.COL278, "*")); // *, blank
		   
		s.setOverall_scaleScore						(paddedWrap(StudentDataConstants.COL279, ""));
		s.setOverall_hSE_Score						(paddedWrap(StudentDataConstants.COL280, ""));
		s.setOverall_percentileRank					(paddedWrap(StudentDataConstants.COL281, ""));
		s.setOverall_nCE							(paddedWrap(StudentDataConstants.COL282, ""));
		   
		s.setOasReading_screenReader				(paddedWrap(StudentDataConstants.COL283, "Y")); // Y, blank
		s.setOasReading_onlineCalc					(paddedWrap(StudentDataConstants.COL284, "Y")); // Y, blank
		s.setOasReading_testPause					(paddedWrap(StudentDataConstants.COL285, "Y")); // Y, blank
		s.setOasReading_highlighter					(paddedWrap(StudentDataConstants.COL286, "Y")); // Y, blank
		s.setOasReading_blockingRuler				(paddedWrap(StudentDataConstants.COL287, "Y")); // Y, blank
		s.setOasReading_magnifyingGlass				(paddedWrap(StudentDataConstants.COL288, "Y")); // Y, blank
		s.setOasReading_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL289, "Y")); // Y, blank
		s.setOasReading_largeFont					(paddedWrap(StudentDataConstants.COL290, "Y")); // Y, blank
		s.setOasReading_musicPlayer					(paddedWrap(StudentDataConstants.COL291, "Y")); // Y, blank
		s.setOasReading_extendedTime				(paddedWrap(StudentDataConstants.COL292, "Y")); // Y, blank
		s.setOasReading_maskingTool					(paddedWrap(StudentDataConstants.COL293, "Y")); // Y, blank
		   
		s.setOasWriting_screenReader				(paddedWrap(StudentDataConstants.COL294, "Y")); // Y, blank
		s.setOasWriting_onlineCalc					(paddedWrap(StudentDataConstants.COL295, "Y")); // Y, blank
		s.setOasWriting_testPause					(paddedWrap(StudentDataConstants.COL296, "Y")); // Y, blank
		s.setOasWriting_highlighter					(paddedWrap(StudentDataConstants.COL297, "Y")); // Y, blank
		s.setOasWriting_blockingRuler				(paddedWrap(StudentDataConstants.COL298, "Y")); // Y, blank
		s.setOasWriting_magnifyingGlass				(paddedWrap(StudentDataConstants.COL299, "Y")); // Y, blank
		s.setOasWriting_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL300, "Y")); // Y, blank
		s.setOasWriting_largeFont					(paddedWrap(StudentDataConstants.COL301, "Y")); // Y, blank
		s.setOasWriting_musicPlayer					(paddedWrap(StudentDataConstants.COL302, "Y")); // Y, blank
		s.setOasWriting_extendedTime				(paddedWrap(StudentDataConstants.COL303, "Y")); // Y, blank
		s.setOasWriting_maskingTool					(paddedWrap(StudentDataConstants.COL304, "Y")); // Y, blank
		   
		s.setOasMath_screenReader					(paddedWrap(StudentDataConstants.COL305, "Y")); // Y, blank
		s.setOasMath_onlineCalc						(paddedWrap(StudentDataConstants.COL306, "Y")); // Y, blank
		s.setOasMath_testPause						(paddedWrap(StudentDataConstants.COL307, "Y")); // Y, blank
		s.setOasMath_highlighter					(paddedWrap(StudentDataConstants.COL308, "Y")); // Y, blank
		s.setOasMath_blockingRuler					(paddedWrap(StudentDataConstants.COL309, "Y")); // Y, blank
		s.setOasMath_magnifyingGlass				(paddedWrap(StudentDataConstants.COL310, "Y")); // Y, blank
		s.setOasMath_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL311, "Y")); // Y, blank
		s.setOasMath_largeFont						(paddedWrap(StudentDataConstants.COL312, "Y")); // Y, blank
		s.setOasMath_musicPlayer					(paddedWrap(StudentDataConstants.COL313, "Y")); // Y, blank
		s.setOasMath_extendedTime					(paddedWrap(StudentDataConstants.COL314, "Y")); // Y, blank
		s.setOasMath_maskingTool					(paddedWrap(StudentDataConstants.COL315, "Y")); // Y, blank
		   
		s.setOasScience_screenReader				(paddedWrap(StudentDataConstants.COL316, "Y")); // Y, blank
		s.setOasScience_onlineCalc					(paddedWrap(StudentDataConstants.COL317, "Y")); // Y, blank
		s.setOasScience_testPause					(paddedWrap(StudentDataConstants.COL318, "Y")); // Y, blank
		s.setOasScience_highlighter					(paddedWrap(StudentDataConstants.COL319, "Y")); // Y, blank
		s.setOasScience_blockingRuler				(paddedWrap(StudentDataConstants.COL320, "Y")); // Y, blank
		s.setOasScience_magnifyingGlass				(paddedWrap(StudentDataConstants.COL321, "Y")); // Y, blank
		s.setOasScience_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL322, "Y")); // Y, blank
		s.setOasScience_largeFont					(paddedWrap(StudentDataConstants.COL323, "Y")); // Y, blank
		s.setOasScience_musicPlayer					(paddedWrap(StudentDataConstants.COL324, "Y")); // Y, blank
		s.setOasScience_extendedTime				(paddedWrap(StudentDataConstants.COL325, "Y")); // Y, blank
		s.setOasScience_maskingTool					(paddedWrap(StudentDataConstants.COL326, "Y")); // Y, blank
		   
		s.setOasSocial_screenReader					(paddedWrap(StudentDataConstants.COL327, "Y")); // Y, blank
		s.setOasSocial_onlineCalc					(paddedWrap(StudentDataConstants.COL328, "Y")); // Y, blank
		s.setOasSocial_testPause					(paddedWrap(StudentDataConstants.COL329, "Y")); // Y, blank
		s.setOasSocial_highlighter					(paddedWrap(StudentDataConstants.COL330, "Y")); // Y, blank
		s.setOasSocial_blockingRuler				(paddedWrap(StudentDataConstants.COL331, "Y")); // Y, blank
		s.setOasSocial_magnifyingGlass				(paddedWrap(StudentDataConstants.COL332, "Y")); // Y, blank
		s.setOasSocial_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL333, "Y")); // Y, blank
		s.setOasSocial_largeFont					(paddedWrap(StudentDataConstants.COL334, "Y")); // Y, blank
		s.setOasSocial_musicPlayer					(paddedWrap(StudentDataConstants.COL335, "Y")); // Y, blank
		s.setOasSocial_extendedTime					(paddedWrap(StudentDataConstants.COL336, "Y")); // Y, blank
		s.setOasSocial_maskingTool					(paddedWrap(StudentDataConstants.COL337, "Y")); // Y, blank
		   
		s.setReadingItems_SR						(paddedWrap(StudentDataConstants.COL338, ""));
		s.setReadingItems_FU						(paddedWrap(StudentDataConstants.COL339, ""));
		s.setWritingItems_SR						(paddedWrap(StudentDataConstants.COL340, ""));
		s.setWritingItems_CR						(paddedWrap(StudentDataConstants.COL341, ""));
		s.setWritingItems_FU						(paddedWrap(StudentDataConstants.COL342, ""));
		s.setMathItems_SR							(paddedWrap(StudentDataConstants.COL343, ""));
		s.setMathItems_GR_Status					(paddedWrap(StudentDataConstants.COL344, "W")); // W, R, O, I
		s.setMathItems_GR_Edited					(paddedWrap(StudentDataConstants.COL345, ""));
		s.setMathItems_FU							(paddedWrap(StudentDataConstants.COL346, ""));
		s.setScienceItems_SR						(paddedWrap(StudentDataConstants.COL347, ""));
		s.setScienceItems_FU						(paddedWrap(StudentDataConstants.COL348, ""));
		s.setSocialStudies_SR						(paddedWrap(StudentDataConstants.COL349, ""));
		s.setSocialStudies_FU						(paddedWrap(StudentDataConstants.COL350, ""));
		s.setcTBUseField							(paddedWrap(StudentDataConstants.COL351, ""));

		students.add(s);
		
		return students;
	}

	/**
	 * Used to write the header of the Student Data File
	 * 
	 * @return
	 */
	public static StudentDataTO getHeader() {
		StudentDataTO s = new StudentDataTO();

		s.setCustID									(paddedWrap(StudentDataConstants.COL001,"CustID"));
		s.setRosterID								(paddedWrap(StudentDataConstants.COL002,"RosterID"));
		s.setHierarchyA_Name						(paddedWrap(StudentDataConstants.COL003,"HierarchyA_Name"));
		s.setHierarchyA_Code						(paddedWrap(StudentDataConstants.COL004,"HierarchyA_Code"));
		s.setHierarchyA_SpCodes						(paddedWrap(StudentDataConstants.COL005,"HierarchyA_SpCodes"));
		s.setHierarchyB_Name						(paddedWrap(StudentDataConstants.COL006,"HierarchyB_Name"));
		s.setHierarchyB_Code						(paddedWrap(StudentDataConstants.COL007,"HierarchyB_Code"));
		s.setHierarchyB_SpCodes						(paddedWrap(StudentDataConstants.COL008,"HierarchyB_SpCodes"));
		s.setHierarchyC_Name						(paddedWrap(StudentDataConstants.COL009,"HierarchyC_Name"));
		s.setHierarchyC_Code						(paddedWrap(StudentDataConstants.COL010,"HierarchyC_Code"));
		s.setHierarchyC_SpCodes						(paddedWrap(StudentDataConstants.COL011,"HierarchyC_SpCodes"));
		s.setHierarchyD_Name						(paddedWrap(StudentDataConstants.COL012,"HierarchyD_Name"));
		s.setHierarchyD_Code						(paddedWrap(StudentDataConstants.COL013,"HierarchyD_Code"));
		s.setHierarchyD_SpCodes						(paddedWrap(StudentDataConstants.COL014,"HierarchyD_SpCodes"));
		s.setHierarchyE_Name						(paddedWrap(StudentDataConstants.COL015,"HierarchyE_Name"));
		s.setHierarchyE_Code						(paddedWrap(StudentDataConstants.COL016,"HierarchyE_Code"));
		s.setHierarchyE_SpCodes						(paddedWrap(StudentDataConstants.COL017,"HierarchyE_SpCodes"));
		s.setHierarchyF_Name						(paddedWrap(StudentDataConstants.COL018,"HierarchyF_Name"));
		s.setHierarchyF_Code						(paddedWrap(StudentDataConstants.COL019,"HierarchyF_Code"));
		s.setHierarchyF_SpCodes						(paddedWrap(StudentDataConstants.COL020,"HierarchyF_SpCodes"));
		s.setHierarchyG_Name						(paddedWrap(StudentDataConstants.COL021,"HierarchyG_Name"));
		s.setHierarchyG_Code						(paddedWrap(StudentDataConstants.COL022,"HierarchyG_Code"));
		s.setHierarchyG_SpCodes						(paddedWrap(StudentDataConstants.COL023,"HierarchyG_SpCodes"));
		s.setLastName								(paddedWrap(StudentDataConstants.COL024,"LastName"));
		s.setFirstName								(paddedWrap(StudentDataConstants.COL025,"FirstName"));
		s.setMiddleInitial							(paddedWrap(StudentDataConstants.COL026,"MiddleInitial"));
		s.setDateOfBirth							(paddedWrap(StudentDataConstants.COL027,"DateOfBirth"));
		s.setForm									(paddedWrap(StudentDataConstants.COL028,"Form"));
		s.setChangedFormFlag						(paddedWrap(StudentDataConstants.COL029,"ChangedFormFlag"));
		s.setTestLang								(paddedWrap(StudentDataConstants.COL030,"TestLang"));
		s.setTestMode								(paddedWrap(StudentDataConstants.COL031,"TestMode"));
		s.setGender									(paddedWrap(StudentDataConstants.COL032,"Gender"));
		s.setResolvedEthnicity						(paddedWrap(StudentDataConstants.COL033,"ResolvedEthnicity"));
		s.setStudentID								(paddedWrap(StudentDataConstants.COL034,"StudentID"));
		s.setPhoneNumber							(paddedWrap(StudentDataConstants.COL035,"PhoneNumber"));
		s.setLithocode								(paddedWrap(StudentDataConstants.COL036,"Lithocode"));
		s.setImagingID								(paddedWrap(StudentDataConstants.COL037,"ImagingID"));
		s.setStreetAddress							(paddedWrap(StudentDataConstants.COL038,"StreetAddress"));
		s.setAptNo									(paddedWrap(StudentDataConstants.COL039,"AptNo"));
		s.setCity									(paddedWrap(StudentDataConstants.COL040,"City"));
		s.setState									(paddedWrap(StudentDataConstants.COL041,"State"));
		s.setZipCode								(paddedWrap(StudentDataConstants.COL042,"ZipCode"));
		s.setCounty_Code							(paddedWrap(StudentDataConstants.COL043,"County_Code"));
		s.setEduc_Center_Code						(paddedWrap(StudentDataConstants.COL044,"Educ_Center_Code"));
		s.setAcc_Audio_Rd							(paddedWrap(StudentDataConstants.COL045,"Acc_Audio_Rd"));
		s.setAcc_Audio_Wr							(paddedWrap(StudentDataConstants.COL046,"Acc_Audio_Wr"));
		s.setAcc_Audio_Math1						(paddedWrap(StudentDataConstants.COL047,"Acc_Audio_Math1"));
		s.setAcc_Audio_Math2						(paddedWrap(StudentDataConstants.COL048,"Acc_Audio_Math2"));
		s.setAcc_Audio_Sc							(paddedWrap(StudentDataConstants.COL049,"Acc_Audio_Sc"));
		s.setAcc_Audio_SS							(paddedWrap(StudentDataConstants.COL050,"Acc_Audio_SS"));
		s.setAcc_Breaks_Rd							(paddedWrap(StudentDataConstants.COL051,"Acc_Breaks_Rd"));
		s.setAcc_Breaks_Wr							(paddedWrap(StudentDataConstants.COL052,"Acc_Breaks_Wr"));
		s.setAcc_Breaks_Math1						(paddedWrap(StudentDataConstants.COL053,"Acc_Breaks_Math1"));
		s.setAcc_Breaks_Math2						(paddedWrap(StudentDataConstants.COL054,"Acc_Breaks_Math2"));
		s.setAcc_Breaks_SC							(paddedWrap(StudentDataConstants.COL055,"Acc_Breaks_SC"));
		s.setAcc_Breaks_SS							(paddedWrap(StudentDataConstants.COL056,"Acc_Breaks_SS"));
		s.setAcc_Calculator_Rd						(paddedWrap(StudentDataConstants.COL057,"Acc_Calculator_Rd"));
		s.setAcc_Calculator_Wr						(paddedWrap(StudentDataConstants.COL058,"Acc_Calculator_Wr"));
		s.setAcc_Calculator_Math1					(paddedWrap(StudentDataConstants.COL059,"Acc_Calculator_Math1"));
		s.setAcc_Calculator_SS						(paddedWrap(StudentDataConstants.COL060,"Acc_Calculator_SS"));
		s.setAcc_Duration1_25_Rd					(paddedWrap(StudentDataConstants.COL061,"Acc_Duration1.25_Rd"));
		s.setAcc_Duration1_25_Wr					(paddedWrap(StudentDataConstants.COL062,"Acc_Duration1.25_Wr"));
		s.setAcc_Duration1_25_Math1					(paddedWrap(StudentDataConstants.COL063,"Acc_Duration1.25_Math1"));
		s.setAcc_Duration1_25_Math2					(paddedWrap(StudentDataConstants.COL064,"Acc_Duration1.25_Math2"));
		s.setAcc_Duration1_25_Sc					(paddedWrap(StudentDataConstants.COL065,"Acc_Duration1.25_Sc"));
		s.setAcc_Duration1_25_SS					(paddedWrap(StudentDataConstants.COL066,"Acc_Duration1.25_SS"));
		s.setAcc_Duration1_5_Rd						(paddedWrap(StudentDataConstants.COL067,"Acc_Duration1.5_Rd"));
		s.setAcc_Duration1_5_Wr						(paddedWrap(StudentDataConstants.COL068,"Acc_Duration1.5_Wr"));
		s.setAcc_Duration1_5_Math1					(paddedWrap(StudentDataConstants.COL069,"Acc_Duration1.5_Math1"));
		s.setAcc_Duration1_5_Math2					(paddedWrap(StudentDataConstants.COL070,"Acc_Duration1.5_Math2"));
		s.setAcc_Duration1_5_Sc						(paddedWrap(StudentDataConstants.COL071,"Acc_Duration1.5_Sc"));
		s.setAcc_Duration1_5_SS						(paddedWrap(StudentDataConstants.COL072,"Acc_Duration1.5_SS"));
		s.setAcc_Duration2_0_Rd						(paddedWrap(StudentDataConstants.COL073,"Acc_Duration2.0_Rd"));
		s.setAcc_Duration2_0_Wr						(paddedWrap(StudentDataConstants.COL074,"Acc_Duration2.0_Wr"));
		s.setAcc_Duration2_0_Math1					(paddedWrap(StudentDataConstants.COL075,"Acc_Duration2.0_Math1"));
		s.setAcc_Duration2_0_Math2					(paddedWrap(StudentDataConstants.COL076,"Acc_Duration2.0_Math2"));
		s.setAcc_Duration2_0_Sc						(paddedWrap(StudentDataConstants.COL077,"Acc_Duration2.0_Sc"));
		s.setAcc_Duration2_0_SS						(paddedWrap(StudentDataConstants.COL078,"Acc_Duration2.0_SS"));
		s.setAcc_PhysicalSupport_Rd					(paddedWrap(StudentDataConstants.COL079,"Acc_PhysicalSupport_Rd"));
		s.setAcc_PhysicalSupport_Wr					(paddedWrap(StudentDataConstants.COL080,"Acc_PhysicalSupport_Wr"));
		s.setAcc_PhysicalSupport_Math1				(paddedWrap(StudentDataConstants.COL081,"Acc_PhysicalSupport_Math1"));
		s.setAcc_PhysicalSupport_Math2				(paddedWrap(StudentDataConstants.COL082,"Acc_PhysicalSupport_Math2"));
		s.setAcc_PhysicalSupport_Sc					(paddedWrap(StudentDataConstants.COL083,"Acc_PhysicalSupport_Sc"));
		s.setAcc_PhysicalSupport_SS					(paddedWrap(StudentDataConstants.COL084,"Acc_PhysicalSupport_SS"));
		s.setAcc_Scribe_Rd							(paddedWrap(StudentDataConstants.COL085,"Acc_Scribe_Rd"));
		s.setAcc_Scribe_Wr							(paddedWrap(StudentDataConstants.COL086,"Acc_Scribe_Wr"));
		s.setAcc_Scribe_Math1						(paddedWrap(StudentDataConstants.COL087,"Acc_Scribe_Math1"));
		s.setAcc_Scribe_Math2						(paddedWrap(StudentDataConstants.COL088,"Acc_Scribe_Math2"));
		s.setAcc_Scribe_Sc							(paddedWrap(StudentDataConstants.COL089,"Acc_Scribe_Sc"));
		s.setAcc_Scribe_SS							(paddedWrap(StudentDataConstants.COL090,"Acc_Scribe_SS"));
		s.setAcc_TechDevice_Rd						(paddedWrap(StudentDataConstants.COL091,"Acc_TechDevice_Rd"));
		s.setAcc_TechDevice_Wr						(paddedWrap(StudentDataConstants.COL092,"Acc_TechDevice_Wr"));
		s.setAcc_TechDevice_Math1					(paddedWrap(StudentDataConstants.COL093,"Acc_TechDevice_Math1"));
		s.setAcc_TechDevice_Math2					(paddedWrap(StudentDataConstants.COL094,"Acc_TechDevice_Math2"));
		s.setAcc_TechDevice_Sc						(paddedWrap(StudentDataConstants.COL095,"Acc_TechDevice_Sc"));
		s.setAcc_TechDevice_SS						(paddedWrap(StudentDataConstants.COL096,"Acc_TechDevice_SS"));
		s.setAcc_SepRoom_Rd							(paddedWrap(StudentDataConstants.COL097,"Acc_SepRoom_Rd"));
		s.setAcc_SepRoom_Wr							(paddedWrap(StudentDataConstants.COL098,"Acc_SepRoom_Wr"));
		s.setAcc_SepRoom_Math1						(paddedWrap(StudentDataConstants.COL099,"Acc_SepRoom_Math1"));
		s.setAcc_SepRoom_Math2						(paddedWrap(StudentDataConstants.COL100,"Acc_SepRoom_Math2"));
		s.setAcc_SepRoom_Sc							(paddedWrap(StudentDataConstants.COL101,"Acc_SepRoom_Sc"));
		s.setAcc_SepRoom_SS							(paddedWrap(StudentDataConstants.COL102,"Acc_SepRoom_SS"));
		s.setAcc_SmGroup_Rd							(paddedWrap(StudentDataConstants.COL103,"Acc_SmGroup_Rd"));
		s.setAcc_SmGroup_Wr							(paddedWrap(StudentDataConstants.COL104,"Acc_SmGroup_Wr"));
		s.setAcc_SmGroup_Math1						(paddedWrap(StudentDataConstants.COL105,"Acc_SmGroup_Math1"));
		s.setAcc_SmGroup_Math2						(paddedWrap(StudentDataConstants.COL106,"Acc_SmGroup_Math2"));
		s.setAcc_SmGroup_Sc							(paddedWrap(StudentDataConstants.COL107,"Acc_SmGroup_Sc"));
		s.setAcc_SmGroup_SS							(paddedWrap(StudentDataConstants.COL108,"Acc_SmGroup_SS"));
		s.setAcc_Other_Rd							(paddedWrap(StudentDataConstants.COL109,"Acc_Other_Rd"));
		s.setAcc_Other_Wr							(paddedWrap(StudentDataConstants.COL110,"Acc_Other_Wr"));
		s.setAcc_Other_Math1						(paddedWrap(StudentDataConstants.COL111,"Acc_Other_Math1"));
		s.setAcc_Other_Math2						(paddedWrap(StudentDataConstants.COL112,"Acc_Other_Math2"));
		s.setAcc_Other_Sc							(paddedWrap(StudentDataConstants.COL113,"Acc_Other_Sc"));
		s.setAcc_Other_SS							(paddedWrap(StudentDataConstants.COL114,"Acc_Other_SS"));
		s.setExaminer_Ack_Accomm					(paddedWrap(StudentDataConstants.COL115,"Examiner_Ack_Accomm"));
		s.setHomeLang								(paddedWrap(StudentDataConstants.COL116,"HomeLang"));
		s.setK_12_Educ_Completed					(paddedWrap(StudentDataConstants.COL117,"K_12_Educ_Completed"));
		s.setSubj_Taken_Passed_Rd					(paddedWrap(StudentDataConstants.COL118,"Subj_Taken/Passed_Rd"));
		s.setSubj_Taken_Passed_Wr					(paddedWrap(StudentDataConstants.COL119,"Subj_Taken/Passed_Wr"));
		s.setSubj_Taken_Passed_Math					(paddedWrap(StudentDataConstants.COL120,"Subj_Taken/Passed_Math"));
		s.setSubj_Taken_Passed_Sc					(paddedWrap(StudentDataConstants.COL121,"Subj_Taken/Passed_Sc"));
		s.setSubj_Taken_Passed_SS					(paddedWrap(StudentDataConstants.COL122,"Subj_Taken/Passed_SS"));
		s.setSubj_Taken_Passed_None					(paddedWrap(StudentDataConstants.COL123,"Subj_Taken/Passed_None"));
		s.setNo_Times_Taken_Rd						(paddedWrap(StudentDataConstants.COL124,"No_Times_Taken_Rd"));
		s.setNo_Times_Taken_Wr						(paddedWrap(StudentDataConstants.COL125,"No_Times_Taken_Wr"));
		s.setNo_Times_Taken_Math					(paddedWrap(StudentDataConstants.COL126,"No_Times_Taken_Math"));
		s.setNo_Times_Taken_Sc						(paddedWrap(StudentDataConstants.COL127,"No_Times_Taken_Sc"));
		s.setNo_Times_Taken_SS						(paddedWrap(StudentDataConstants.COL128,"No_Times_Taken_SS"));
		s.setRetake_Rd								(paddedWrap(StudentDataConstants.COL129,"Retake_Rd"));
		s.setRetake_Wr								(paddedWrap(StudentDataConstants.COL130,"Retake_Wr"));
		s.setRetake_Math							(paddedWrap(StudentDataConstants.COL131,"Retake_Math"));
		s.setRetake_Sc								(paddedWrap(StudentDataConstants.COL132,"Retake_Sc"));
		s.setRetake_SS								(paddedWrap(StudentDataConstants.COL133,"Retake_SS"));
		s.setRetake_None							(paddedWrap(StudentDataConstants.COL134,"Retake_None"));
		s.setTake_Readiness_Assessment				(paddedWrap(StudentDataConstants.COL135,"Take_Readiness_Assessment"));
		s.setPrepare_County_Prog					(paddedWrap(StudentDataConstants.COL136,"Prepare_County_Prog"));
		s.setPrepare_Sch_Dist_Prog					(paddedWrap(StudentDataConstants.COL137,"Prepare_Sch_Dist_Prog"));
		s.setPrepare_Military_Prog					(paddedWrap(StudentDataConstants.COL138,"Prepare_Military_Prog"));
		s.setPrepare_Religious_Prog					(paddedWrap(StudentDataConstants.COL139,"Prepare_Religious_Prog"));
		s.setPrepare_Purchased_My_Own_Study_Books	(paddedWrap(StudentDataConstants.COL140,"Prepare_Purchased_My_Own_Study_Books"));
		s.setPrepare_Online_Study_Prog				(paddedWrap(StudentDataConstants.COL141,"Prepare_Online_Study_Prog"));
		s.setPrepare_Homeschool						(paddedWrap(StudentDataConstants.COL142,"Prepare_Homeschool"));
		s.setPrepare_Tutor							(paddedWrap(StudentDataConstants.COL143,"Prepare_Tutor"));
		s.setPrepare_Self_Taught					(paddedWrap(StudentDataConstants.COL144,"Prepare_Self_Taught"));
		s.setRecent_Class_Rd						(paddedWrap(StudentDataConstants.COL145,"Recent_Class_Rd"));
		s.setRecent_Class_Wr						(paddedWrap(StudentDataConstants.COL146,"Recent_Class_Wr"));
		s.setRecent_Class_Math						(paddedWrap(StudentDataConstants.COL147,"Recent_Class_Math"));
		s.setRecent_Class_Sc						(paddedWrap(StudentDataConstants.COL148,"Recent_Class_Sc"));
		s.setRecent_Class_SS						(paddedWrap(StudentDataConstants.COL149,"Recent_Class_SS"));
		s.setMonths_Studied_Subj_Rd					(paddedWrap(StudentDataConstants.COL150,"Months_Studied_Subj_Rd"));
		s.setMonths_Studied_Subj_Wr					(paddedWrap(StudentDataConstants.COL151,"Months_Studied_Subj_Wr"));
		s.setMonths_Studied_Subj_Math				(paddedWrap(StudentDataConstants.COL152,"Months_Studied_Subj_Math"));
		s.setMonths_Studied_Subj_Sc					(paddedWrap(StudentDataConstants.COL153,"Months_Studied_Subj_Sc"));
		s.setMonths_Studied_Subj_SS					(paddedWrap(StudentDataConstants.COL154,"Months_Studied_Subj_SS"));
		s.setGrade_in_Subj_Rd						(paddedWrap(StudentDataConstants.COL155,"Grade_in_Subj_Rd"));
		s.setGrade_in_Subj_Wr						(paddedWrap(StudentDataConstants.COL156,"Grade_in_Subj_Wr"));
		s.setGrade_in_Subj_Math						(paddedWrap(StudentDataConstants.COL157,"Grade_in_Subj_Math"));
		s.setGrade_in_Subj_Sc						(paddedWrap(StudentDataConstants.COL158,"Grade_in_Subj_Sc"));
		s.setGrade_in_Subj_SS						(paddedWrap(StudentDataConstants.COL159,"Grade_in_Subj_SS"));
		s.setTestFormat_Braille						(paddedWrap(StudentDataConstants.COL160,"TestFormat_Braille"));
		s.setTestFormat_LP							(paddedWrap(StudentDataConstants.COL161,"TestFormat_LP"));
		s.setLocal_Field_1							(paddedWrap(StudentDataConstants.COL162,"Local_Field_1"));
		s.setLocal_Field_2							(paddedWrap(StudentDataConstants.COL163,"Local_Field_2"));
		s.setLocal_Field_3							(paddedWrap(StudentDataConstants.COL164,"Local_Field_3"));
		s.setLocal_Field_4							(paddedWrap(StudentDataConstants.COL165,"Local_Field_4"));
		s.setLocal_Field_5							(paddedWrap(StudentDataConstants.COL166,"Local_Field_5"));
		s.setLocal_Field_6							(paddedWrap(StudentDataConstants.COL167,"Local_Field_6"));
		s.setLocal_Field_7							(paddedWrap(StudentDataConstants.COL168,"Local_Field_7"));
		s.setLocal_Field_8							(paddedWrap(StudentDataConstants.COL169,"Local_Field_8"));
		s.setLocal_Field_9							(paddedWrap(StudentDataConstants.COL170,"Local_Field_9"));
		s.setLocal_Field_10							(paddedWrap(StudentDataConstants.COL171,"Local_Field_10"));
		s.setLocal_Field_11							(paddedWrap(StudentDataConstants.COL172,"Local_Field_11"));
		s.setLocal_Field_12							(paddedWrap(StudentDataConstants.COL173,"Local_Field_12"));
		s.setLocal_Field_13							(paddedWrap(StudentDataConstants.COL174,"Local_Field_13"));
		s.setLocal_Field_14							(paddedWrap(StudentDataConstants.COL175,"Local_Field_14"));
		s.setLocal_Field_15							(paddedWrap(StudentDataConstants.COL176,"Local_Field_15"));
		s.setLocal_Field_16							(paddedWrap(StudentDataConstants.COL177,"Local_Field_16"));
		s.setLocal_Field_17							(paddedWrap(StudentDataConstants.COL178,"Local_Field_17"));
		s.setLocal_Field_18							(paddedWrap(StudentDataConstants.COL179,"Local_Field_18"));
		s.setLocal_Field_19							(paddedWrap(StudentDataConstants.COL180,"Local_Field_19"));
		s.setLocal_Field_20							(paddedWrap(StudentDataConstants.COL181,"Local_Field_20"));
		s.setCandidate_Acknowledgement				(paddedWrap(StudentDataConstants.COL182,"Candidate_Acknowledgement"));

		s.setReading_dateTestTaken					(paddedWrap(StudentDataConstants.COL183,"DateTestTaken"));
		s.setReading_numberCorrect					(paddedWrap(StudentDataConstants.COL184,"NumberCorrect"));
		s.setReading_scaleScore						(paddedWrap(StudentDataConstants.COL185,"ScaleScore"));
		s.setReading_hSE_Score						(paddedWrap(StudentDataConstants.COL186,"HSE_Score"));
		s.setReading_percentileRank					(paddedWrap(StudentDataConstants.COL187,"PercentileRank"));
		s.setReading_nCE							(paddedWrap(StudentDataConstants.COL188,"NCE"));
		s.setReading_std_Obj_Mstry_Scr_1			(paddedWrap(StudentDataConstants.COL189,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL190,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_2			(paddedWrap(StudentDataConstants.COL191,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL192,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_3			(paddedWrap(StudentDataConstants.COL193,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL194,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_4			(paddedWrap(StudentDataConstants.COL195,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL196,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_5			(paddedWrap(StudentDataConstants.COL197,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_5			(paddedWrap(StudentDataConstants.COL198,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_6			(paddedWrap(StudentDataConstants.COL199,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_6			(paddedWrap(StudentDataConstants.COL200,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_7			(paddedWrap(StudentDataConstants.COL201,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_7			(paddedWrap(StudentDataConstants.COL202,"Not_All_items_atmpt"));
		s.setReading_std_Obj_Mstry_Scr_8			(paddedWrap(StudentDataConstants.COL203,"Std_Obj_Mstry_Scr"));
		s.setReading_not_All_items_atmpt_8			(paddedWrap(StudentDataConstants.COL204,"Not_All_items_atmpt"));

		s.setWriting_dateTestTaken					(paddedWrap(StudentDataConstants.COL205,"DateTestTaken"));
		s.setWriting_numberCorrect					(paddedWrap(StudentDataConstants.COL206,"NumberCorrect"));
		s.setWriting_scaleScore						(paddedWrap(StudentDataConstants.COL207,"ScaleScore"));
		s.setWriting_hSE_Score						(paddedWrap(StudentDataConstants.COL208,"HSE_Score"));
		s.setWriting_percentileRank					(paddedWrap(StudentDataConstants.COL209,"PercentileRank"));
		s.setWriting_nCE							(paddedWrap(StudentDataConstants.COL210,"NCE"));
		s.setWriting_std_Obj_Mstry_Scr_1			(paddedWrap(StudentDataConstants.COL211,"Std_Obj_Mstry_Scr"));
		s.setWriting_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL212,"Not_All_items_atmpt"));
		s.setWriting_std_Obj_Mstry_Scr_2			(paddedWrap(StudentDataConstants.COL213,"Std_Obj_Mstry_Scr"));
		s.setWriting_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL214,"Not_All_items_atmpt"));
		s.setWriting_std_Obj_Mstry_Scr_3			(paddedWrap(StudentDataConstants.COL215,"Std_Obj_Mstry_Scr"));
		s.setWriting_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL216,"Not_All_items_atmpt"));
		s.setWriting_std_Obj_Mstry_Scr_4			(paddedWrap(StudentDataConstants.COL217,"Std_Obj_Mstry_Scr"));
		s.setWriting_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL218,"Not_All_items_atmpt"));

		s.setEla_scaleScore							(paddedWrap(StudentDataConstants.COL219,"ScaleScore"));
		s.setEla_hSE_Score							(paddedWrap(StudentDataConstants.COL220,"HSE_Score"));
		s.setEla_percentileRank						(paddedWrap(StudentDataConstants.COL221,"PercentileRank"));
		s.setEla_nCE								(paddedWrap(StudentDataConstants.COL222,"NCE"));

		s.setMath_dateTestTaken						(paddedWrap(StudentDataConstants.COL223,"DateTestTaken"));
		s.setMath_numberCorrect						(paddedWrap(StudentDataConstants.COL224,"NumberCorrect"));
		s.setMath_scaleScore						(paddedWrap(StudentDataConstants.COL225,"ScaleScore"));
		s.setMath_hSE_Score							(paddedWrap(StudentDataConstants.COL226,"HSE_Score"));
		s.setMath_percentileRank					(paddedWrap(StudentDataConstants.COL227,"PercentileRank"));
		s.setMath_nCE								(paddedWrap(StudentDataConstants.COL228,"NCE"));
		s.setMath_std_Obj_Mstry_Scr_1				(paddedWrap(StudentDataConstants.COL229,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_1				(paddedWrap(StudentDataConstants.COL230,"Not_All_items_atmpt"));
		s.setMath_std_Obj_Mstry_Scr_2				(paddedWrap(StudentDataConstants.COL231,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_2				(paddedWrap(StudentDataConstants.COL232,"Not_All_items_atmpt"));
		s.setMath_std_Obj_Mstry_Scr_3				(paddedWrap(StudentDataConstants.COL233,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_3				(paddedWrap(StudentDataConstants.COL234,"Not_All_items_atmpt"));
		s.setMath_std_Obj_Mstry_Scr_4				(paddedWrap(StudentDataConstants.COL235,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_4				(paddedWrap(StudentDataConstants.COL236,"Not_All_items_atmpt"));
		s.setMath_std_Obj_Mstry_Scr_5				(paddedWrap(StudentDataConstants.COL237,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_5				(paddedWrap(StudentDataConstants.COL238,"Not_All_items_atmpt"));
		s.setMath_std_Obj_Mstry_Scr_6				(paddedWrap(StudentDataConstants.COL239,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_6				(paddedWrap(StudentDataConstants.COL240,"Not_All_items_atmpt"));
		s.setMath_std_Obj_Mstry_Scr_7				(paddedWrap(StudentDataConstants.COL241,"Std_Obj_Mstry_Scr"));
		s.setMath_not_All_items_atmpt_7				(paddedWrap(StudentDataConstants.COL242,"Not_All_items_atmpt"));

		s.setScience_dateTestTaken					(paddedWrap(StudentDataConstants.COL243,"DateTestTaken"));
		s.setScience_numberCorrect					(paddedWrap(StudentDataConstants.COL244,"NumberCorrect"));
		s.setScience_scaleScore						(paddedWrap(StudentDataConstants.COL245,"ScaleScore"));
		s.setScience_hSE_Score						(paddedWrap(StudentDataConstants.COL246,"HSE_Score"));
		s.setScience_percentileRank					(paddedWrap(StudentDataConstants.COL247,"PercentileRank"));
		s.setScience_nCE							(paddedWrap(StudentDataConstants.COL248,"NCE"));
		s.setScience_std_Obj_Mstry_Scr_1			(paddedWrap(StudentDataConstants.COL249,"Std_Obj_Mstry_Scr"));
		s.setScience_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL250,"Not_All_items_atmpt"));
		s.setScience_std_Obj_Mstry_Scr_2			(paddedWrap(StudentDataConstants.COL251,"Std_Obj_Mstry_Scr"));
		s.setScience_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL252,"Not_All_items_atmpt"));
		s.setScience_std_Obj_Mstry_Scr_3			(paddedWrap(StudentDataConstants.COL253,"Std_Obj_Mstry_Scr"));
		s.setScience_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL254,"Not_All_items_atmpt"));
		s.setScience_std_Obj_Mstry_Scr_4			(paddedWrap(StudentDataConstants.COL255,"Std_Obj_Mstry_Scr"));
		s.setScience_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL256,"Not_All_items_atmpt"));
		s.setScience_std_Obj_Mstry_Scr_5			(paddedWrap(StudentDataConstants.COL257,"Std_Obj_Mstry_Scr"));
		s.setScience_not_All_items_atmpt_5			(paddedWrap(StudentDataConstants.COL258,"Not_All_items_atmpt"));

		s.setSocial_dateTestTaken					(paddedWrap(StudentDataConstants.COL259,"DateTestTaken"));
		s.setSocial_numberCorrect					(paddedWrap(StudentDataConstants.COL260,"NumberCorrect"));
		s.setSocial_scaleScore						(paddedWrap(StudentDataConstants.COL261,"ScaleScore"));
		s.setSocial_hSE_Score						(paddedWrap(StudentDataConstants.COL262,"HSE_Score"));
		s.setSocial_percentileRank					(paddedWrap(StudentDataConstants.COL263,"PercentileRank"));
		s.setSocial_nCE								(paddedWrap(StudentDataConstants.COL264,"NCE"));
		s.setSocial_std_Obj_Mstry_Scr_1				(paddedWrap(StudentDataConstants.COL265,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_1			(paddedWrap(StudentDataConstants.COL266,"Not_All_items_atmpt"));
		s.setSocial_std_Obj_Mstry_Scr_2				(paddedWrap(StudentDataConstants.COL267,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_2			(paddedWrap(StudentDataConstants.COL268,"Not_All_items_atmpt"));
		s.setSocial_std_Obj_Mstry_Scr_3				(paddedWrap(StudentDataConstants.COL269,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_3			(paddedWrap(StudentDataConstants.COL270,"Not_All_items_atmpt"));
		s.setSocial_std_Obj_Mstry_Scr_4				(paddedWrap(StudentDataConstants.COL271,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_4			(paddedWrap(StudentDataConstants.COL272,"Not_All_items_atmpt"));
		s.setSocial_std_Obj_Mstry_Scr_5				(paddedWrap(StudentDataConstants.COL273,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_5			(paddedWrap(StudentDataConstants.COL274,"Not_All_items_atmpt"));
		s.setSocial_std_Obj_Mstry_Scr_6				(paddedWrap(StudentDataConstants.COL275,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_6			(paddedWrap(StudentDataConstants.COL276,"Not_All_items_atmpt"));
		s.setSocial_std_Obj_Mstry_Scr_7				(paddedWrap(StudentDataConstants.COL277,"Std_Obj_Mstry_Scr"));
		s.setSocial_not_All_items_atmpt_7			(paddedWrap(StudentDataConstants.COL278,"Not_All_items_atmpt"));

		s.setOverall_scaleScore						(paddedWrap(StudentDataConstants.COL279,"ScaleScore"));
		s.setOverall_hSE_Score						(paddedWrap(StudentDataConstants.COL280,"HSE_Score"));
		s.setOverall_percentileRank					(paddedWrap(StudentDataConstants.COL281,"PercentileRank"));
		s.setOverall_nCE							(paddedWrap(StudentDataConstants.COL282,"NCE"));

		s.setOasReading_screenReader				(paddedWrap(StudentDataConstants.COL283,"ScreenReader"));
		s.setOasReading_onlineCalc					(paddedWrap(StudentDataConstants.COL284,"OnlineCalc"));
		s.setOasReading_testPause					(paddedWrap(StudentDataConstants.COL285,"TestPause"));
		s.setOasReading_highlighter					(paddedWrap(StudentDataConstants.COL286,"Highlighter"));
		s.setOasReading_blockingRuler				(paddedWrap(StudentDataConstants.COL287,"BlockingRuler"));
		s.setOasReading_magnifyingGlass				(paddedWrap(StudentDataConstants.COL288,"MagnifyingGlass"));
		s.setOasReading_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL289,"FontAndBkgndClr"));
		s.setOasReading_largeFont					(paddedWrap(StudentDataConstants.COL290,"LargeFont"));
		s.setOasReading_musicPlayer					(paddedWrap(StudentDataConstants.COL291,"MusicPlayer"));
		s.setOasReading_extendedTime				(paddedWrap(StudentDataConstants.COL292,"ExtendedTime"));
		s.setOasReading_maskingTool					(paddedWrap(StudentDataConstants.COL293,"MaskingTool"));

		s.setOasWriting_screenReader				(paddedWrap(StudentDataConstants.COL294,"ScreenReader"));
		s.setOasWriting_onlineCalc					(paddedWrap(StudentDataConstants.COL295,"OnlineCalc"));
		s.setOasWriting_testPause					(paddedWrap(StudentDataConstants.COL296,"TestPause"));
		s.setOasWriting_highlighter					(paddedWrap(StudentDataConstants.COL297,"Highlighter"));
		s.setOasWriting_blockingRuler				(paddedWrap(StudentDataConstants.COL298,"BlockingRuler"));
		s.setOasWriting_magnifyingGlass				(paddedWrap(StudentDataConstants.COL299,"MagnifyingGlass"));
		s.setOasWriting_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL300,"FontAndBkgndClr"));
		s.setOasWriting_largeFont					(paddedWrap(StudentDataConstants.COL301,"LargeFont"));
		s.setOasWriting_musicPlayer					(paddedWrap(StudentDataConstants.COL302,"MusicPlayer"));
		s.setOasWriting_extendedTime				(paddedWrap(StudentDataConstants.COL303,"ExtendedTime"));
		s.setOasWriting_maskingTool					(paddedWrap(StudentDataConstants.COL304,"MaskingTool"));

		s.setOasMath_screenReader					(paddedWrap(StudentDataConstants.COL305,"ScreenReader"));
		s.setOasMath_onlineCalc						(paddedWrap(StudentDataConstants.COL306,"OnlineCalc"));
		s.setOasMath_testPause						(paddedWrap(StudentDataConstants.COL307,"TestPause"));
		s.setOasMath_highlighter					(paddedWrap(StudentDataConstants.COL308,"Highlighter"));
		s.setOasMath_blockingRuler					(paddedWrap(StudentDataConstants.COL309,"BlockingRuler"));
		s.setOasMath_magnifyingGlass				(paddedWrap(StudentDataConstants.COL310,"MagnifyingGlass"));
		s.setOasMath_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL311,"FontAndBkgndClr"));
		s.setOasMath_largeFont						(paddedWrap(StudentDataConstants.COL312,"LargeFont"));
		s.setOasMath_musicPlayer					(paddedWrap(StudentDataConstants.COL313,"MusicPlayer"));
		s.setOasMath_extendedTime					(paddedWrap(StudentDataConstants.COL314,"ExtendedTime"));
		s.setOasMath_maskingTool					(paddedWrap(StudentDataConstants.COL315,"MaskingTool"));

		s.setOasScience_screenReader				(paddedWrap(StudentDataConstants.COL316,"ScreenReader"));
		s.setOasScience_onlineCalc					(paddedWrap(StudentDataConstants.COL317,"OnlineCalc"));
		s.setOasScience_testPause					(paddedWrap(StudentDataConstants.COL318,"TestPause"));
		s.setOasScience_highlighter					(paddedWrap(StudentDataConstants.COL319,"Highlighter"));
		s.setOasScience_blockingRuler				(paddedWrap(StudentDataConstants.COL320,"BlockingRuler"));
		s.setOasScience_magnifyingGlass				(paddedWrap(StudentDataConstants.COL321,"MagnifyingGlass"));
		s.setOasScience_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL322,"FontAndBkgndClr"));
		s.setOasScience_largeFont					(paddedWrap(StudentDataConstants.COL323,"LargeFont"));
		s.setOasScience_musicPlayer					(paddedWrap(StudentDataConstants.COL324,"MusicPlayer"));
		s.setOasScience_extendedTime				(paddedWrap(StudentDataConstants.COL325,"ExtendedTime"));
		s.setOasScience_maskingTool					(paddedWrap(StudentDataConstants.COL326,"MaskingTool"));

		s.setOasSocial_screenReader					(paddedWrap(StudentDataConstants.COL327,"ScreenReader"));
		s.setOasSocial_onlineCalc					(paddedWrap(StudentDataConstants.COL328,"OnlineCalc"));
		s.setOasSocial_testPause					(paddedWrap(StudentDataConstants.COL329,"TestPause"));
		s.setOasSocial_highlighter					(paddedWrap(StudentDataConstants.COL330,"Highlighter"));
		s.setOasSocial_blockingRuler				(paddedWrap(StudentDataConstants.COL331,"BlockingRuler"));
		s.setOasSocial_magnifyingGlass				(paddedWrap(StudentDataConstants.COL332,"MagnifyingGlass"));
		s.setOasSocial_fontAndBkgndClr				(paddedWrap(StudentDataConstants.COL333,"FontAndBkgndClr"));
		s.setOasSocial_largeFont					(paddedWrap(StudentDataConstants.COL334,"LargeFont"));
		s.setOasSocial_musicPlayer					(paddedWrap(StudentDataConstants.COL335,"MusicPlayer"));
		s.setOasSocial_extendedTime					(paddedWrap(StudentDataConstants.COL336,"ExtendedTime"));
		s.setOasSocial_maskingTool					(paddedWrap(StudentDataConstants.COL337,"MaskingTool"));

		s.setReadingItems_SR						(paddedWrap(StudentDataConstants.COL338,"ReadingItems_SR"));
		s.setReadingItems_FU						(paddedWrap(StudentDataConstants.COL339,"ReadingItems_FU"));
		s.setWritingItems_SR						(paddedWrap(StudentDataConstants.COL340,"WritingItems_SR"));
		s.setWritingItems_CR						(paddedWrap(StudentDataConstants.COL341,"WritingItems_CR"));
		s.setWritingItems_FU						(paddedWrap(StudentDataConstants.COL342,"WritingItems_FU"));
		s.setMathItems_SR							(paddedWrap(StudentDataConstants.COL343,"MathItems_SR"));
		s.setMathItems_GR_Status					(paddedWrap(StudentDataConstants.COL344,"MathItems_GR_Status"));
		s.setMathItems_GR_Edited					(paddedWrap(StudentDataConstants.COL345,"MathItems_GR_Edited"));
		s.setMathItems_FU							(paddedWrap(StudentDataConstants.COL346,"MathItems_FU"));
		s.setScienceItems_SR						(paddedWrap(StudentDataConstants.COL347,"ScienceItems_SR"));
		s.setScienceItems_FU						(paddedWrap(StudentDataConstants.COL348,"ScienceItems_FU"));
		s.setSocialStudies_SR						(paddedWrap(StudentDataConstants.COL349,"SocialStudies_SR"));
		s.setSocialStudies_FU						(paddedWrap(StudentDataConstants.COL350,"SocialStudies_FU"));
		s.setcTBUseField							(paddedWrap(StudentDataConstants.COL351,"CTBUseField"));

		return s;
	}
}
