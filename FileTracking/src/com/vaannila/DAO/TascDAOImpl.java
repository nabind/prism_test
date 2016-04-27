package com.vaannila.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.StudentDetailsWinTO;
import com.vaannila.TO.TASCProcessTO;
import com.vaannila.util.JDCConnectionDriver;
import com.vaannila.TO.StudentDetailsTO;


public class TascDAOImpl {
	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:tasc";
	
	/**
	 * Get all process list
	 * @throws Exception
	 */
	public List<TASCProcessTO> getProcess(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getProcess()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TASCProcessTO processTO = null;
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("select PROCESS_ID,FILE_NAME,SOURCE_SYSTEM,HIER_VALIDATION,BIO_VALIDATION,DEMO_VALIDATION,CONTENT_VALIDATION,");
		queryBuff.append("OBJECTIVE_VALIDATION,ITEM_VALIDATION,WKF_PARTITION_NAME,DATETIMESTAMP,(SELECT GETSTATUS(PROCESS_ID) FROM DUAL) STSTUS, ER_VALIDATION ");
		//queryBuff.append(",to_number(datetimestamp - to_date('01-JAN-1970','DD-MON-YYYY')) * (24 * 60 * 60 * 1000) long_time ");
		queryBuff.append("from stg_process_status ");
		// queryBuff.append(" WHERE rownum<10 ");
		if(searchProcess != null) {
			queryBuff.append(" WHERE 1 = 1 ");
			if(searchProcess.getCreatedDate() != null && searchProcess.getCreatedDate().trim().length() > 0
					&& searchProcess.getUpdatedDate() != null && searchProcess.getUpdatedDate().trim().length() > 0) {
				queryBuff.append("AND (DATETIMESTAMP between to_date(?, 'MM/DD/YYYY') and to_date(?, 'MM/DD/YYYY')+1) ");
			}
			if(searchProcess.getStructElement() != null && searchProcess.getStructElement().trim().length() > 0) 
				queryBuff.append("AND SOURCE_SYSTEM = ? ");
		}
		queryBuff.append(" order by PROCESS_ID desc ");
		String query = queryBuff.toString();
		// System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			if(searchProcess != null) {
				if(searchProcess.getCreatedDate() != null && searchProcess.getCreatedDate().trim().length() > 0
						&& searchProcess.getUpdatedDate() != null && searchProcess.getUpdatedDate().trim().length() > 0) {
					pstmt.setString(++count, searchProcess.getCreatedDate());
					pstmt.setString(++count, searchProcess.getUpdatedDate());
				}
				if(searchProcess.getStructElement() != null && searchProcess.getStructElement().trim().length() > 0) 
					pstmt.setString(++count, searchProcess.getStructElement());
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new TASCProcessTO();
				processTO.setProcessId(rs.getString("PROCESS_ID"));
				processTO.setFileName(rs.getString("FILE_NAME"));
				processTO.setSourceSystem(rs.getString("SOURCE_SYSTEM"));
				processTO.setHierValidation(rs.getString("HIER_VALIDATION"));
				processTO.setBioValidation(rs.getString("BIO_VALIDATION"));
				processTO.setDemoValidation(rs.getString("DEMO_VALIDATION"));
				processTO.setContentValidation(rs.getString("CONTENT_VALIDATION"));
				processTO.setObjValidation(rs.getString("OBJECTIVE_VALIDATION"));
				processTO.setItemValidation(rs.getString("ITEM_VALIDATION"));
				processTO.setWkfPartitionName(rs.getString("WKF_PARTITION_NAME"));
				processTO.setDateTimestamp(rs.getString("DATETIMESTAMP"));
				processTO.setOverallStatus(rs.getString("STSTUS"));
				processTO.setErValidation(rs.getString("ER_VALIDATION") == null ? " " : rs.getString("ER_VALIDATION"));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		System.out.println("Exit: getProcess()");
		return processList;
	}
	
	/**
	 * Method to update the process log
	 * @param processId
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public String getProcessLog(String processId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String processLog = "";
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			String query = "SELECT U.PROCESS_LOG FROM STG_PROCESS_STATUS U WHERE U.PROCESS_ID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, processId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				processLog = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return processLog;
	}
	
	/**
	 * Method to get success and failure count for last 90 days
	 * @param processId
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public List<TASCProcessTO> getProcessCountList() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			TASCProcessTO processTO = null;
			String query = "SELECT BB.D, NVL(PP.COUN, 0) AS COUN, NVL(OL.COUN, 0) AS COUN2, BB.c, NVL(ERR_COUNT.E, 0), NVL(COM_COUNT.S, 0) FROM "+
							" (SELECT TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY') AS D, COUNT(1) AS COUN FROM STG_PROCESS_STATUS where source_system = 'PP' GROUP BY TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY')) PP, "+ 
							" (SELECT TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY') AS D, COUNT(1) AS COUN FROM STG_PROCESS_STATUS where source_system = 'OL' GROUP BY TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY')) OL, "+
							" (SELECT TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY') AS D,COUNT(1) AS E FROM STG_PROCESS_STATUS O WHERE (SELECT GETSTATUS(O.PROCESS_ID) FROM DUAL) = 'ER' GROUP BY TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY')) ERR_COUNT, "+
							"        (SELECT TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY') AS D,COUNT(1) AS S FROM STG_PROCESS_STATUS O WHERE (SELECT GETSTATUS(O.PROCESS_ID) FROM DUAL) = 'CO' GROUP BY TO_CHAR(DATETIMESTAMP, 'MM-DD-YYYY')) COM_COUNT, "+
							" (SELECT TO_CHAR(TO_DATE(TO_CHAR((SELECT MIN(DATETIMESTAMP) FROM STG_PROCESS_STATUS),'MM-DD-YYYY'),'MM-DD-YYYY') - 1 + ROWNUM, 'MM-DD-YYYY') AS D , "+
							 " TO_CHAR((SELECT MIN(DATETIMESTAMP) FROM STG_PROCESS_STATUS),'YYYY,MM,DD') as c "+
								" FROM ALL_OBJECTS WHERE TO_DATE(TO_CHAR((SELECT MIN(DATETIMESTAMP) FROM STG_PROCESS_STATUS),'MM-DD-YYYY'),'MM-DD-YYYY') - 1 + ROWNUM <= TO_DATE(TO_CHAR((SELECT MAX(DATETIMESTAMP) "+ 
								" FROM STG_PROCESS_STATUS),'MM-DD-YYYY'), 'MM-DD-YYYY')) BB WHERE PP.D(+) = BB.D and OL.D(+) = BB.D AND ERR_COUNT.D(+) = BB.D AND COM_COUNT.D(+) = BB.D "+
								" and TO_DATE(BB.D, 'MM-DD-YYYY') >= to_date((TO_CHAR(SYSDATE-90, 'MM-DD-YYYY')), 'MM-DD-YYYY') ORDER BY BB.D";  
			pstmt = conn.prepareCall(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new TASCProcessTO();
				processTO.setDateTimestamp(rs.getString(1));
				processTO.setPpCount(rs.getString(2));
				processTO.setOlCount(rs.getString(3));
				processTO.setDateTimestamp(rs.getString(4));
				processTO.setErCount(rs.getString(5));
				processTO.setCoCount(rs.getString(6));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return processList;
	}

	public List<TASCProcessTO> getTestElementIdList(String processId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TASCProcessTO> testElementIdList = new ArrayList<TASCProcessTO>();
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			TASCProcessTO process = null;
			String query = "SELECT TEST_ELEMENT_ID, description FROM ER_EXCEPTION_DATA WHERE PROCESS_ID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, processId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				process = new TASCProcessTO();
				process.setTestElementId((rs.getString("TEST_ELEMENT_ID") != null ? rs.getString("TEST_ELEMENT_ID") : ""));
				process.setErValidationError((rs.getString("description") != null ? rs.getString("description") : ""));
				testElementIdList.add(process);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return testElementIdList;
	}

	public Map<String, String> getStudentDetails(String processId, String testElementId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String> studentDetails = new HashMap<String, String>();
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			
			String query = "SELECT DISTINCT TRIM(SSBD.LAST_NAME || ', ' || SSBD.FIRST_NAME || ' ' || SSBD.MIDDLE_NAME) STUDENT_NAME,"
							+ " SSBD.BIRTHDATE DOB, SSBD.GENDER, SSBD.GRADE, SSBD.BARCODE, SSBD.STRUC_ELEMENT, SSBD.EXT_STUDENT_ID,"
							+ " CI.CUSTOMER_NAME,"
							+ " SHD.ORG_NAME,"
							+ " EED.EXCEPTION_CODE, EED.DESCRIPTION"
							+ " FROM STG_STD_BIO_DETAILS SSBD,"
							+ " STG_HIER_DETAILS    SHD,"
							+ " ER_EXCEPTION_DATA   EED,"
							+ " CUSTOMER_INFO       CI"
							+ " WHERE SSBD.TEST_ELEMENT_ID = ?"
							+ " AND SSBD.STU_LSTNODE_HIER_ID = SHD.STG_HIERARCHY_DETAILS_ID"
							//+ " AND SSBD.EXT_STUDENT_ID = EED.ER_UUID"
							+ " AND SSBD.TEST_ELEMENT_ID = EED.TEST_ELEMENT_ID"
							+ " AND SHD.CUSTOMER_ID = CI.CUSTOMERID";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, testElementId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				studentDetails.put("STUDENT_NAME", rs.getString("STUDENT_NAME"));
				studentDetails.put("DOB", rs.getString("DOB"));
				studentDetails.put("GENDER", rs.getString("GENDER"));
				studentDetails.put("GRADE", rs.getString("GRADE"));
				studentDetails.put("BARCODE", rs.getString("BARCODE"));
				studentDetails.put("STRUC_ELEMENT", rs.getString("STRUC_ELEMENT"));
				studentDetails.put("EXT_STUDENT_ID", rs.getString("EXT_STUDENT_ID"));
				studentDetails.put("CUSTOMER_NAME", rs.getString("CUSTOMER_NAME"));
				studentDetails.put("ORG_NAME", rs.getString("ORG_NAME"));
				studentDetails.put("EXCEPTION_CODE", rs.getString("EXCEPTION_CODE"));
				studentDetails.put("DESCRIPTION", rs.getString("DESCRIPTION"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return studentDetails;
	}
	
	/**
	 * @author Joy
	 * @param erSsHistId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getStudentHist(String erSsHistId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String> studentDetails = new HashMap<String, String>();
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("SELECT CTB_CUSTOMER_ID,");
		queryBuff.append("STATENAME,");
		queryBuff.append("DATEOFBIRTH,");
		queryBuff.append("GENDER,");
		queryBuff.append("GOVERNMENTID,");
		queryBuff.append("GOVERNMENTIDTYPE,");
		queryBuff.append("ADDRESS1,");
		queryBuff.append("CITY,");
		queryBuff.append("COUNTY,");
		queryBuff.append("STATE,");
		queryBuff.append("ZIP,");
		queryBuff.append("EMAIL,");
		queryBuff.append("ALTERNATEEMAIL,");
		queryBuff.append("PRIMARYPHONENUMBER,");
		queryBuff.append("CELLPHONENUMBER,");
		queryBuff.append("ALTERNATENUMBER,");
		queryBuff.append("RESOLVED_ETHNICITY_RACE,");
		queryBuff.append("HOMELANGUAGE,");
		queryBuff.append("EDUCATIONLEVEL,");
		queryBuff.append("ATTENDCOLLEGE,");
		queryBuff.append("CONTACT,");
		queryBuff.append("EXAMINEECOUNTYPARISHCODE,");
		queryBuff.append("REGISTEREDON,");
		queryBuff.append("REGISTEREDATTESTCENTER,");
		queryBuff.append("REGISTEREDATTESTCENTERCODE,");
		queryBuff.append("SCHEDULE_ID,");
		queryBuff.append("TIMEOFDAY,");
		queryBuff.append("DATECHECKEDIN,");
		queryBuff.append("CONTENT_TEST_TYPE,");
		queryBuff.append("CONTENT_TEST_CODE,");
		queryBuff.append("TASCREADINESS,");
		queryBuff.append("ECC,");
		queryBuff.append("TESTCENTERCODE,");
		queryBuff.append("TESTCENTERNAME,");
		queryBuff.append("REGST_TC_COUNTYPARISHCODE,");
		queryBuff.append("SCHED_TC_COUNTYPARISHCODE");
		queryBuff.append(" FROM ER_STUDENT_SCHED_HISTORY");
		queryBuff.append(" WHERE ER_SS_HISTID = ?");
		String query = queryBuff.toString();
		System.out.println(query);
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1,Long.parseLong(erSsHistId));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				studentDetails.put("CTB_CUSTOMER_ID", rs.getString("CTB_CUSTOMER_ID") != null ? rs.getString("CTB_CUSTOMER_ID") : "");
				studentDetails.put("STATENAME", rs.getString("STATENAME") != null ? rs.getString("STATENAME") : "");
				studentDetails.put("DATEOFBIRTH", rs.getString("DATEOFBIRTH") !=null ? rs.getString("DATEOFBIRTH") : "");
				studentDetails.put("GENDER", rs.getString("GENDER") != null ? rs.getString("GENDER") : "");
				studentDetails.put("GOVERNMENTID", rs.getString("GOVERNMENTID") != null ? rs.getString("GOVERNMENTID") : "");
				studentDetails.put("GOVERNMENTIDTYPE", rs.getString("GOVERNMENTIDTYPE") != null ? rs.getString("GOVERNMENTIDTYPE") : "");
				studentDetails.put("ADDRESS1", rs.getString("ADDRESS1") != null ? rs.getString("ADDRESS1") : "");
				studentDetails.put("CITY", rs.getString("CITY") != null ? rs.getString("CITY") : "");
				studentDetails.put("COUNTY", rs.getString("COUNTY") != null ? rs.getString("COUNTY") : "");
				studentDetails.put("STATE", rs.getString("STATE") != null ? rs.getString("STATE") : "");
				studentDetails.put("ZIP", rs.getString("ZIP") != null ? rs.getString("ZIP") : "");
				studentDetails.put("EMAIL", rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "");
				studentDetails.put("ALTERNATEEMAIL", rs.getString("ALTERNATEEMAIL") != null ? rs.getString("ALTERNATEEMAIL") : "");
				studentDetails.put("PRIMARYPHONENUMBER", rs.getString("PRIMARYPHONENUMBER") != null ? rs.getString("PRIMARYPHONENUMBER") : "");
				studentDetails.put("CELLPHONENUMBER", rs.getString("CELLPHONENUMBER") != null ? rs.getString("CELLPHONENUMBER") : "");
				studentDetails.put("ALTERNATENUMBER", rs.getString("ALTERNATENUMBER") != null ? rs.getString("ALTERNATENUMBER") : "");
				studentDetails.put("RESOLVED_ETHNICITY_RACE", rs.getString("RESOLVED_ETHNICITY_RACE") != null ? rs.getString("RESOLVED_ETHNICITY_RACE") : "");
				studentDetails.put("HOMELANGUAGE", rs.getString("HOMELANGUAGE") != null ? rs.getString("HOMELANGUAGE") : "");
				studentDetails.put("EDUCATIONLEVEL", rs.getString("EDUCATIONLEVEL") != null ? rs.getString("EDUCATIONLEVEL") : "");
				studentDetails.put("ATTENDCOLLEGE", rs.getString("ATTENDCOLLEGE") != null ? rs.getString("ATTENDCOLLEGE") : "");
				studentDetails.put("CONTACT", rs.getString("CONTACT") != null ? rs.getString("CONTACT") : "");
				studentDetails.put("EXAMINEECOUNTYPARISHCODE", rs.getString("EXAMINEECOUNTYPARISHCODE") != null ? rs.getString("EXAMINEECOUNTYPARISHCODE") : "");
				studentDetails.put("REGISTEREDON", rs.getString("REGISTEREDON") != null ? rs.getString("REGISTEREDON") : "");
				studentDetails.put("REGISTEREDATTESTCENTER", rs.getString("REGISTEREDATTESTCENTER") != null ? rs.getString("REGISTEREDATTESTCENTER") : "");
				studentDetails.put("REGISTEREDATTESTCENTERCODE", rs.getString("REGISTEREDATTESTCENTERCODE") !=null ? rs.getString("REGISTEREDATTESTCENTERCODE") : "" );
				studentDetails.put("SCHEDULE_ID", rs.getString("SCHEDULE_ID") != null ? rs.getString("SCHEDULE_ID") : "");
				studentDetails.put("TIMEOFDAY", rs.getString("TIMEOFDAY") != null ? rs.getString("TIMEOFDAY") : "");
				studentDetails.put("DATECHECKEDIN", rs.getString("DATECHECKEDIN") != null ? rs.getString("DATECHECKEDIN") : "");
				studentDetails.put("CONTENT_TEST_TYPE", rs.getString("CONTENT_TEST_TYPE") != null ? rs.getString("CONTENT_TEST_TYPE") : "");
				studentDetails.put("CONTENT_TEST_CODE", rs.getString("CONTENT_TEST_CODE") != null ? rs.getString("CONTENT_TEST_CODE") : "");
				studentDetails.put("TASCREADINESS", rs.getString("TASCREADINESS") != null ? rs.getString("TASCREADINESS") : "");
				studentDetails.put("ECC", rs.getString("ECC") != null ? rs.getString("ECC") : "");
				studentDetails.put("TESTCENTERCODE", rs.getString("TESTCENTERCODE") != null ? rs.getString("TESTCENTERCODE") : "");
				studentDetails.put("TESTCENTERNAME", rs.getString("TESTCENTERNAME") != null ? rs.getString("TESTCENTERNAME") : "");
				studentDetails.put("REGST_TC_COUNTYPARISHCODE", rs.getString("REGST_TC_COUNTYPARISHCODE") != null ? rs.getString("REGST_TC_COUNTYPARISHCODE") : "");
				studentDetails.put("SCHED_TC_COUNTYPARISHCODE", rs.getString("SCHED_TC_COUNTYPARISHCODE") != null ? rs.getString("SCHED_TC_COUNTYPARISHCODE") : "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return studentDetails;
	}
	
	/**
	 * @author Joy
	 * @param erExcdId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getMoreInfo(String erExcdId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String> moreInfoMap = new HashMap<String, String>();
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("SELECT TESTING_SITE_CODE,");
		queryBuff.append("TESTING_SITE_NAME,");
		queryBuff.append("TEST_LANGUAGE,");
		queryBuff.append("LITHOCODE,");
		queryBuff.append("TO_CHAR(SCORING_DATE,'MM/DD/YYYY') SCORING_DATE,");
		queryBuff.append("TO_CHAR(SCANNED_DATE,'MM/DD/YYYY') SCANNED_DATE,");
		queryBuff.append("SRC_STUDENT_NAME,");
		queryBuff.append("NCR_SCORE,");
		queryBuff.append("CONTENT_STATUS_CODE,");
		queryBuff.append("SCAN_BATCH,");
		queryBuff.append("SCAN_STACK,");
		queryBuff.append("SCAN_SEQUENCE,");
		queryBuff.append("BIO_IMAGES");
		queryBuff.append(" FROM ER_EXCEPTION_DATA");
		queryBuff.append(" WHERE ER_EXCDID = ?");
		String query = queryBuff.toString();
		System.out.println(query);
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1,Long.parseLong(erExcdId));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				moreInfoMap.put("TESTING_SITE_CODE", rs.getString("TESTING_SITE_CODE") != null ? rs.getString("TESTING_SITE_CODE") : "");
				moreInfoMap.put("TESTING_SITE_NAME", rs.getString("TESTING_SITE_NAME") != null ? rs.getString("TESTING_SITE_NAME") : "");
				moreInfoMap.put("TEST_LANGUAGE", rs.getString("TEST_LANGUAGE") != null ? rs.getString("TEST_LANGUAGE") : "");
				moreInfoMap.put("LITHOCODE", rs.getString("LITHOCODE") != null ? rs.getString("LITHOCODE") : "");
				moreInfoMap.put("SCORING_DATE", rs.getString("SCORING_DATE") != null ? rs.getString("SCORING_DATE") : "");
				moreInfoMap.put("SCANNED_DATE", rs.getString("SCANNED_DATE") != null ? rs.getString("SCANNED_DATE") : "");
				moreInfoMap.put("LAST_NAME", rs.getString("SRC_STUDENT_NAME") != null ? rs.getString("SRC_STUDENT_NAME") : "");
				moreInfoMap.put("NCR_SCORE", rs.getString("NCR_SCORE") != null ? rs.getString("NCR_SCORE") : "");
				moreInfoMap.put("CONTENT_STATUS_CODE", rs.getString("CONTENT_STATUS_CODE") != null ? rs.getString("CONTENT_STATUS_CODE") : "");
				moreInfoMap.put("SCAN_BATCH", rs.getString("SCAN_BATCH") != null ? rs.getString("SCAN_BATCH") : "");
				moreInfoMap.put("SCAN_STACK", rs.getString("SCAN_STACK") != null ? rs.getString("SCAN_STACK") : "");
				moreInfoMap.put("SCAN_SEQUENCE", rs.getString("SCAN_SEQUENCE") != null ? rs.getString("SCAN_SEQUENCE") : "");
				moreInfoMap.put("BIO_IMAGES", rs.getString("BIO_IMAGES") != null ? rs.getString("BIO_IMAGES") : "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return moreInfoMap;
	}
	
	
	/**
	 * @author Joy
	 * @param erExcdId
	 * @return
	 * @throws Exception
	 */
	public String getErrorLog(String erExcdId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String errorLog = "";
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("SELECT 'ERROR CODE-' || EXCEPTION_CODE || ': ' || DESCRIPTION");
		queryBuff.append(" FROM ER_EXCEPTION_DATA");
		queryBuff.append(" WHERE ER_EXCDID = ?");
		String query = queryBuff.toString();
		System.out.println(query);
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1, Long.parseLong(erExcdId));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				errorLog = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return errorLog;
	}
	
	/**
	 * @author Joy
	 * Get Searched records page wise
	 * @throws Exception
	 */
	//TODO: Need to implement cache
	public List<StudentDetailsTO> getProcessErPaging(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getProcessErPaging()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		StudentDetailsTO studentDetailsTO = null;
		List<StudentDetailsTO> studentDetailsTOList = new ArrayList<StudentDetailsTO>();
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			int placeHolderTotalRecCount = 0;
			int placeHolderDataOnline = 0;
			int placeHolderDataCsv = 0;
			int placeHolderErrorMsg = 0;
			String query = "";
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				query = "{call PKG_FILE_TRACKING.SP_GET_DATA_ER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}else{
				query = "{call PKG_FILE_TRACKING.SP_GET_DATA_OL_PP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}
			System.out.println("DATA query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, searchProcess.getProcessStatus());
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateFrom());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateTo());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				cs.setString(++count, "%"+searchProcess.getUuid()+"%");
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				cs.setString(++count, "%"+searchProcess.getLastName()+"%");
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
				cs.setLong(++count, Long.parseLong(searchProcess.getExceptionCode()));
			}else{
				cs.setLong(++count, -1);
			}
			
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					cs.setLong(++count, Long.parseLong(searchProcess.getRecordId()));
				}else{
					cs.setLong(++count, -1);
				}
			}else{
				cs.setString(++count, searchProcess.getSourceSystem());
			}
			
			if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
				cs.setLong(++count, Long.parseLong(searchProcess.getProcessId()));
			}else{
				cs.setLong(++count, -1);
			}
			if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
				cs.setString(++count, searchProcess.getStateCode());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
				cs.setString(++count, searchProcess.getForm());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
				cs.setString(++count, searchProcess.getTestElementId());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
				cs.setString(++count, searchProcess.getBarcode());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getSearchParam() != null && searchProcess.getSearchParam().trim().length() > 0){
				cs.setString(++count, searchProcess.getSearchParam());
			}else{
				cs.setString(++count, "-1");
			}
			cs.setString(++count, searchProcess.getSortCol());
			cs.setString(++count, searchProcess.getSortDir());
			cs.setLong(++count, searchProcess.getFromRowNum());
			cs.setLong(++count, searchProcess.getToRowNum());				
			placeHolderTotalRecCount = ++count;
			cs.registerOutParameter(placeHolderTotalRecCount, OracleTypes.NUMBER);
			placeHolderDataOnline = ++count;
			cs.registerOutParameter(placeHolderDataOnline, OracleTypes.CURSOR);
			placeHolderDataCsv = ++count;
			cs.registerOutParameter(placeHolderDataCsv, OracleTypes.CURSOR);
			placeHolderErrorMsg = ++count;
			cs.registerOutParameter(placeHolderErrorMsg, OracleTypes.VARCHAR);
			
			cs.execute();
			String errorMessage = cs.getString(placeHolderErrorMsg);
			if (errorMessage == null || errorMessage.isEmpty()) {
				if("CSV".equals(searchProcess.getMode())){
					System.out.println("Fetching data for CSV");
					rs = (ResultSet) cs.getObject(placeHolderDataCsv);
				}else{
					rs = (ResultSet) cs.getObject(placeHolderDataOnline);
					System.out.println("Fetching data for Online Display");
				}
				while(rs.next()) {
					studentDetailsTO = new StudentDetailsTO();
					studentDetailsTO.setStudentName(rs.getString("STUDENTNAME")!=null ? rs.getString("STUDENTNAME") : "");
					studentDetailsTO.setUuid(rs.getString("UUID")!=null ? rs.getString("UUID") : "");
					studentDetailsTO.setTestElementId(rs.getString("TEST_ELEMENT_ID") != null ? rs.getString("TEST_ELEMENT_ID") : "");
					studentDetailsTO.setProcessId(rs.getString("PROCESS_ID") != null ? rs.getString("PROCESS_ID") : "");
					studentDetailsTO.setExceptionCode(rs.getString("EXCEPTION_CODE") != null ? rs.getString("EXCEPTION_CODE") : "");
					studentDetailsTO.setSourceSystem(rs.getString("SOURCE_SYSTEM") !=null ? rs.getString("SOURCE_SYSTEM") : "");
					studentDetailsTO.setOverallStatus(rs.getString("EXCEPTION_STATUS") != null ? rs.getString("EXCEPTION_STATUS") : "");
					studentDetailsTO.setErSsHistId(rs.getString("ER_SS_HISTID") != null ? rs.getString("ER_SS_HISTID") : "");
					studentDetailsTO.setBarcode(rs.getString("BARCODE") != null ? rs.getString("BARCODE") : "");
					studentDetailsTO.setDateScheduled(rs.getString("DATE_SCHEDULED") != null ? rs.getString("DATE_SCHEDULED") : "");
					studentDetailsTO.setStateCode(rs.getString("STATE_CODE") != null ? rs.getString("STATE_CODE") : "");
					studentDetailsTO.setForm(rs.getString("FORM") != null ? rs.getString("FORM") : "");
					studentDetailsTO.setErExcdId(rs.getString("ER_EXCDID") != null ? rs.getString("ER_EXCDID") : "");
					studentDetailsTO.setSubtestName(rs.getString("SUBTEST") != null ? rs.getString("SUBTEST") : "");
					studentDetailsTO.setProcessedDate(rs.getString("PROCESSED_DATE") != null ? rs.getString("PROCESSED_DATE") : "");
					studentDetailsTO.setTestCenterCode(rs.getString("TESTING_SITE_CODE") != null ? rs.getString("TESTING_SITE_CODE") : "");
					studentDetailsTO.setTestCenterName(rs.getString("TESTING_SITE_NAME") != null ? rs.getString("TESTING_SITE_NAME") : "");
					studentDetailsTO.setSourceSystemDesc(searchProcess.getSourceSystemDesc());
					studentDetailsTO.setProcessedDateFrom(searchProcess.getProcessedDateFrom());
					studentDetailsTO.setProcessedDateTo(searchProcess.getProcessedDateTo());
					studentDetailsTO.setErrorLog(rs.getString("ERROR_DESCRIPTION") != null ? rs.getString("ERROR_DESCRIPTION") : "");
					
					if("ERESOURCE".equals(searchProcess.getSourceSystem())){
						studentDetailsTO.setCtbCustomerId(rs.getString("CTB_CUSTOMER_ID") != null ? rs.getString("CTB_CUSTOMER_ID") : "");
						studentDetailsTO.setStateName(rs.getString("STATENAME") != null ? rs.getString("STATENAME") : "");
						studentDetailsTO.setDob(rs.getString("DATEOFBIRTH") !=null ? rs.getString("DATEOFBIRTH") : "");
						studentDetailsTO.setGender(rs.getString("GENDER") != null ? rs.getString("GENDER") : "");
						studentDetailsTO.setGovermentId(rs.getString("GOVERNMENTID") != null ? rs.getString("GOVERNMENTID") : "");
						studentDetailsTO.setGovermentIdType(rs.getString("GOVERNMENTIDTYPE") != null ? rs.getString("GOVERNMENTIDTYPE") : "");
						studentDetailsTO.setAddress(rs.getString("ADDRESS1") != null ? rs.getString("ADDRESS1") : "");
						studentDetailsTO.setCity(rs.getString("CITY") != null ? rs.getString("CITY") : "");
						studentDetailsTO.setCounty(rs.getString("COUNTY") != null ? rs.getString("COUNTY") : "");
						studentDetailsTO.setState(rs.getString("STATE") != null ? rs.getString("STATE") : "");
						studentDetailsTO.setZip(rs.getString("ZIP") != null ? rs.getString("ZIP") : "");
						studentDetailsTO.setEmail(rs.getString("EMAIL") != null ? rs.getString("EMAIL") : "");
						studentDetailsTO.setAlternateEmail(rs.getString("ALTERNATEEMAIL") != null ? rs.getString("ALTERNATEEMAIL") : "");
						studentDetailsTO.setPrimaryPhoneNumber(rs.getString("PRIMARYPHONENUMBER") != null ? rs.getString("PRIMARYPHONENUMBER") : "");
						studentDetailsTO.setCellPhoneNumber(rs.getString("CELLPHONENUMBER") != null ? rs.getString("CELLPHONENUMBER") : "");
						studentDetailsTO.setAlternatePhoneNumber(rs.getString("ALTERNATENUMBER") != null ? rs.getString("ALTERNATENUMBER") : "");
						studentDetailsTO.setResolvedEthnicityRace(rs.getString("RESOLVED_ETHNICITY_RACE") != null ? rs.getString("RESOLVED_ETHNICITY_RACE") : "");
						studentDetailsTO.setHomeLanguage(rs.getString("HOMELANGUAGE") != null ? rs.getString("HOMELANGUAGE") : "");
						studentDetailsTO.setEducationLevel(rs.getString("EDUCATIONLEVEL") != null ? rs.getString("EDUCATIONLEVEL") : "");
						studentDetailsTO.setAttendCollege(rs.getString("ATTENDCOLLEGE") != null ? rs.getString("ATTENDCOLLEGE") : "");
						studentDetailsTO.setContact(rs.getString("CONTACT") != null ? rs.getString("CONTACT") : "");
						studentDetailsTO.setExamineeCountyParishCode(rs.getString("EXAMINEECOUNTYPARISHCODE") != null ? rs.getString("EXAMINEECOUNTYPARISHCODE") : "");
						studentDetailsTO.setRegisteredOn(rs.getString("REGISTEREDON") != null ? rs.getString("REGISTEREDON") : "");
						studentDetailsTO.setRegisteredTestCenter(rs.getString("REGISTEREDATTESTCENTER") != null ? rs.getString("REGISTEREDATTESTCENTER") : "");
						studentDetailsTO.setRegisteredTestCenterCode(rs.getString("REGISTEREDATTESTCENTERCODE") !=null ? rs.getString("REGISTEREDATTESTCENTERCODE") : "");
						studentDetailsTO.setScheduleId(rs.getString("SCHEDULE_ID") != null ? rs.getString("SCHEDULE_ID") : "");
						studentDetailsTO.setTimeOfDay(rs.getString("TIMEOFDAY") != null ? rs.getString("TIMEOFDAY") : "");
						studentDetailsTO.setCheckedInDate(rs.getString("DATECHECKEDIN") != null ? rs.getString("DATECHECKEDIN") : "");
						studentDetailsTO.setContentTestType(rs.getString("CONTENT_TEST_TYPE") != null ? rs.getString("CONTENT_TEST_TYPE") : "");
						studentDetailsTO.setContentTestCode(rs.getString("CONTENT_TEST_CODE") != null ? rs.getString("CONTENT_TEST_CODE") : "");
						studentDetailsTO.setTascRadiness(rs.getString("TASCREADINESS") != null ? rs.getString("TASCREADINESS") : "");
						studentDetailsTO.setEcc(rs.getString("ECC") != null ? rs.getString("ECC") : "");
						studentDetailsTO.setRegstTcCountyParishCode(rs.getString("REGST_TC_COUNTYPARISHCODE") != null ? rs.getString("REGST_TC_COUNTYPARISHCODE") : "");
						studentDetailsTO.setSchedTcCountyParishCode(rs.getString("SCHED_TC_COUNTYPARISHCODE") != null ? rs.getString("SCHED_TC_COUNTYPARISHCODE") : "");
					}else{
						studentDetailsTO.setTestLanguage(rs.getString("TEST_LANGUAGE") != null ? rs.getString("TEST_LANGUAGE") : "");
						studentDetailsTO.setLithocode(rs.getString("LITHOCODE") != null ? rs.getString("LITHOCODE") : "");
						studentDetailsTO.setScoringDate(rs.getString("SCORING_DATE") != null ? rs.getString("SCORING_DATE") : "");
						studentDetailsTO.setScannedDate(rs.getString("SCANNED_DATE") != null ? rs.getString("SCANNED_DATE") : "");
						studentDetailsTO.setNcrScore(rs.getString("NCR_SCORE") != null ? rs.getString("NCR_SCORE") : "");
						studentDetailsTO.setContentStatusCode(rs.getString("CONTENT_STATUS_CODE") != null ? rs.getString("CONTENT_STATUS_CODE") : "");
						studentDetailsTO.setScanBatch(rs.getString("SCAN_BATCH") != null ? rs.getString("SCAN_BATCH") : "");
						studentDetailsTO.setScanStack(rs.getString("SCAN_STACK") != null ? rs.getString("SCAN_STACK") : "");
						studentDetailsTO.setScanSequence(rs.getString("SCAN_SEQUENCE") != null ? rs.getString("SCAN_SEQUENCE") : "");
						studentDetailsTO.setBioImages(rs.getString("BIO_IMAGES") != null ? rs.getString("BIO_IMAGES") : "");
					}
					studentDetailsTOList.add(studentDetailsTO);
				}
			}else{
				System.out.println("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: getProcessErPaging() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentDetailsTOList;
	}
	
	/**
	 * @author Joy
	 * Get total record count for search criteria
	 * @throws Exception
	 */
	//TODO: Need to implement cache
	public long getTotalRecordCount(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getTotalRecord()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		long totalRecordCount = 0;
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			int placeHolderTotalRecCount = 0;
			int placeHolderData = 0;
			int placeHolderErrorMsg = 0;
			String query = "";
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				query = "{call PKG_FILE_TRACKING.SP_GET_DATA_ER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}else{
				query = "{call PKG_FILE_TRACKING.SP_GET_DATA_OL_PP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}
			System.out.println("COUNT query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, searchProcess.getProcessStatus());
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateFrom());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateTo());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				cs.setString(++count, "%"+searchProcess.getUuid()+"%");
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				cs.setString(++count, "%"+searchProcess.getLastName()+"%");
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
				cs.setLong(++count, Long.parseLong(searchProcess.getExceptionCode()));
			}else{
				cs.setLong(++count, -1);
			}
			
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					cs.setLong(++count, Long.parseLong(searchProcess.getRecordId()));
				}else{
					cs.setLong(++count, -1);
				}
			}else{
				cs.setString(++count, searchProcess.getSourceSystem());
			}
			
			if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
				cs.setLong(++count, Long.parseLong(searchProcess.getProcessId()));
			}else{
				cs.setLong(++count, -1);
			}
			if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
				cs.setString(++count, searchProcess.getStateCode());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
				cs.setString(++count, searchProcess.getForm());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
				cs.setString(++count, searchProcess.getTestElementId());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
				cs.setString(++count, searchProcess.getBarcode());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getSearchParam() != null && searchProcess.getSearchParam().trim().length() > 0){
				cs.setString(++count, searchProcess.getSearchParam());
			}else{
				cs.setString(++count, "-1");
			}
			cs.setString(++count, "-1");
			cs.setString(++count, "-1");
			cs.setLong(++count, searchProcess.getFromRowNum());
			cs.setLong(++count, searchProcess.getToRowNum());				
			placeHolderTotalRecCount = ++count;
			cs.registerOutParameter(placeHolderTotalRecCount, OracleTypes.NUMBER);
			placeHolderData = ++count;
			cs.registerOutParameter(placeHolderData, OracleTypes.CURSOR);
			placeHolderData = ++count;
			cs.registerOutParameter(placeHolderData, OracleTypes.CURSOR);
			placeHolderErrorMsg = ++count;
			cs.registerOutParameter(placeHolderErrorMsg, OracleTypes.VARCHAR);
			
			cs.execute();
			String errorMessage = cs.getString(placeHolderErrorMsg);
			if (errorMessage == null || errorMessage.isEmpty()) {
				totalRecordCount = cs.getLong(placeHolderTotalRecCount);
			}else{
				System.out.println("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: getTotalRecord() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return totalRecordCount;
	}
	
	/**
	 * @author Joy
	 * Get Searched records page wise
	 * @throws Exception
	 */
	public List<StudentDetailsWinTO> getResultWin(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getResultWin()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		StudentDetailsWinTO studentDetailsTO = null;
		List<StudentDetailsWinTO> studentDetailsTOList = new ArrayList<StudentDetailsWinTO>();
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			int placeHolderTotalRecCount = 0;
			int placeHolderData = 0;
			int placeHolderDataCsv = 0;
			int placeHolderErrorMsg = 0;
			String query = "{call PKG_FILE_TRACKING.SP_GET_DATA_WINSCORE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			System.out.println("query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, searchProcess.getProcessStatus());
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateFrom());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateTo());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getImagingId() != null && searchProcess.getImagingId().trim().length() > 0){
				cs.setString(++count, searchProcess.getImagingId());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
				cs.setString(++count, searchProcess.getBarcode());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getSearchParam() != null && searchProcess.getSearchParam().trim().length() > 0){
				cs.setString(++count, searchProcess.getSearchParam());
			}else{
				cs.setString(++count, "-1");
			}
			cs.setString(++count, searchProcess.getSortCol());
			cs.setString(++count, searchProcess.getSortDir());
			cs.setLong(++count, searchProcess.getFromRowNum());
			cs.setLong(++count, searchProcess.getToRowNum());				
			placeHolderTotalRecCount = ++count;
			cs.registerOutParameter(placeHolderTotalRecCount, OracleTypes.NUMBER);
			placeHolderData = ++count;
			cs.registerOutParameter(placeHolderData, OracleTypes.CURSOR);
			placeHolderDataCsv = ++count;
			cs.registerOutParameter(placeHolderDataCsv, OracleTypes.CURSOR);
			placeHolderErrorMsg = ++count;
			cs.registerOutParameter(placeHolderErrorMsg, OracleTypes.VARCHAR);
			
			cs.execute();
			String errorMessage = cs.getString(placeHolderErrorMsg);
			if (errorMessage == null || errorMessage.isEmpty()) {
				if("CSV".equals(searchProcess.getMode())){
					System.out.println("Fetching data for CSV");
					rs = (ResultSet) cs.getObject(placeHolderDataCsv);
				}else{
					rs = (ResultSet) cs.getObject(placeHolderData);
					System.out.println("Fetching data for Online Display");
				}
				while(rs.next()) {
					studentDetailsTO = new StudentDetailsWinTO();
					studentDetailsTO.setScanBatch(rs.getString("SCAN_BATCH")!=null ? rs.getString("SCAN_BATCH") : "");
					studentDetailsTO.setDistrictNumber(rs.getString("DISTRICT_NUMBER")!=null ? rs.getString("DISTRICT_NUMBER") : "");
					studentDetailsTO.setSchoolNumber(rs.getString("SCHOOL_NUMBER") != null ? rs.getString("SCHOOL_NUMBER") : "");
					studentDetailsTO.setUuid(rs.getString("UUID") != null ? rs.getString("UUID") : "");
					studentDetailsTO.setBarcode(rs.getString("BARCODE") != null ? rs.getString("BARCODE") : "");
					studentDetailsTO.setForm(rs.getString("TEST_FORM") != null ? rs.getString("TEST_FORM") : "");
					studentDetailsTO.setBraille(rs.getString("BRAILLE")!=null ? rs.getString("BRAILLE") : "");
					studentDetailsTO.setLargePrint(rs.getString("LARGE_PRINT")!=null ? rs.getString("LARGE_PRINT") : "");
					studentDetailsTO.setDateTestTaken(rs.getString("DATE_TEST_TAKEN") != null ? rs.getString("DATE_TEST_TAKEN") : "");
					studentDetailsTO.setLoginDate(rs.getString("LOGINDATE") != null ? rs.getString("LOGINDATE") : "");
					studentDetailsTO.setScanDate(rs.getString("SCAN_DATE") != null ? rs.getString("SCAN_DATE") : "");
					studentDetailsTO.setWinsExportDate(rs.getString("WINS_EXPORT_DATE") != null ? rs.getString("WINS_EXPORT_DATE") : "");
					studentDetailsTO.setImagingId(rs.getString("IMAGING_ID")!=null ? rs.getString("IMAGING_ID") : "");
					studentDetailsTO.setOrgTpName(rs.getString("ORGTP_NAME")!=null ? rs.getString("ORGTP_NAME") : "");
					studentDetailsTO.setLastName(rs.getString("LAST_NAME") != null ? rs.getString("LAST_NAME") : "");
					studentDetailsTO.setFirstName(rs.getString("FIRST_NAME") != null ? rs.getString("FIRST_NAME") : "");
					studentDetailsTO.setMiddleInitial(rs.getString("MIDDLE_INITIAL") != null ? rs.getString("MIDDLE_INITIAL") : "");
					studentDetailsTO.setLithoCode(rs.getString("LITHOCODE") != null ? rs.getString("LITHOCODE") : "");
					studentDetailsTO.setScanStack(rs.getString("SCAN_STACK")!=null ? rs.getString("SCAN_STACK") : "");
					studentDetailsTO.setScanSequence(rs.getString("SCAN_SEQ")!=null ? rs.getString("SCAN_SEQ") : "");
					studentDetailsTO.setWinsDocId(rs.getString("WINS_DOCID") != null ? rs.getString("WINS_DOCID") : "");
					studentDetailsTO.setComodityCode(rs.getString("COMMODITY_CODE") != null ? rs.getString("COMMODITY_CODE") : "");
					studentDetailsTO.setWinStatus(rs.getString("WINSTATUS") != null ? rs.getString("WINSTATUS") : "");
					studentDetailsTO.setPrismProcessStatus(rs.getString("PRISM_PROCESS_STATUS") != null ? rs.getString("PRISM_PROCESS_STATUS") : "");
					studentDetailsTO.setPrismProcessStatusDesc(rs.getString("PRISM_PROCESS_STATUS") != null ? rs.getString("PRISM_PROCESS_STATUS") : "");
					studentDetailsTO.setImageFilePath(rs.getString("IMAGE_FILEPATH") != null ? rs.getString("IMAGE_FILEPATH") : "");
					studentDetailsTO.setImageFileName(rs.getString("IMAGE_FILENAMES") != null ? rs.getString("IMAGE_FILENAMES") : "");
					
					studentDetailsTOList.add(studentDetailsTO);
				}
			}else{
				System.out.println("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: getResultWin() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentDetailsTOList;
	}
	
	/**
	 * @author Joy
	 * Get total record count for given search criteria
	 * @throws Exception
	 */
	public long getTotalRecordCountWin(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getTotalRecordCountWin()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		long totalRecordCount = 0;
		
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			int placeHolderTotalRecCount = 0;
			int placeHolderData = 0;
			int placeHolderDataCsv = 0;
			int placeHolderErrorMsg = 0;
			String query = "";
			query = "{call PKG_FILE_TRACKING.SP_GET_DATA_WINSCORE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			System.out.println("COUNT query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, searchProcess.getProcessStatus());
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateFrom());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				cs.setString(++count, searchProcess.getProcessedDateTo());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getImagingId() != null && searchProcess.getImagingId().trim().length() > 0){
				cs.setString(++count, searchProcess.getImagingId());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
				cs.setString(++count, searchProcess.getBarcode());
			}else{
				cs.setString(++count, "-1");
			}
			if(searchProcess.getSearchParam() != null && searchProcess.getSearchParam().trim().length() > 0){
				cs.setString(++count, searchProcess.getSearchParam());
			}else{
				cs.setString(++count, "-1");
			}
			cs.setString(++count, "-1");
			cs.setString(++count, "-1");
			cs.setLong(++count, searchProcess.getFromRowNum());
			cs.setLong(++count, searchProcess.getToRowNum());				
			placeHolderTotalRecCount = ++count;
			cs.registerOutParameter(placeHolderTotalRecCount, OracleTypes.NUMBER);
			placeHolderData = ++count;
			cs.registerOutParameter(placeHolderData, OracleTypes.CURSOR);
			placeHolderDataCsv = ++count;
			cs.registerOutParameter(placeHolderDataCsv, OracleTypes.CURSOR);
			placeHolderErrorMsg = ++count;
			cs.registerOutParameter(placeHolderErrorMsg, OracleTypes.VARCHAR);
			
			cs.execute();
			String errorMessage = cs.getString(placeHolderErrorMsg);
			if (errorMessage == null || errorMessage.isEmpty()) {
				totalRecordCount = cs.getLong(placeHolderTotalRecCount);
				System.out.println("totalRecordCount: "+totalRecordCount);
			}else{
				System.out.println("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: getTotalRecordCountWin() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return totalRecordCount;
	}
	
	
	public List<StudentDetailsTO> getCombinedProcess(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getCombinedProcess()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentDetailsTO processTO = null;
		List<StudentDetailsTO> processList = new ArrayList<StudentDetailsTO>();
		StringBuffer queryBuff = new StringBuffer();
				
		queryBuff.append("select s.state_code state, s.subtest_code, s.subtest_name, s.form_code form, e.content_test_code, s.student_mode type, pp_oas_linkedid, s.barcode,s.status_code, s.ss, s.student_bio_id, s.test_element_id, s.name, s.datetimestamp,s.hse,e.uuid ");
		queryBuff.append(" ,e.comments ");
		queryBuff.append(" from (select c.customerid, c.customer_name, c.customer_code as state_code, s.ext_student_id as UUID, s.barcode, sd.subtest_code, f.form_code, s.student_bio_id, s.test_element_id, s.last_name || ', ' || s.first_name || ' ' || s.middle_name name, s.middle_name, UPPER(TRIM(s.last_name)) AS last_name, sd.subtest_name, TO_CHAR(sf.test_date, 'DD-MON-YYYY') AS test_date, sf.status_code, sf.datetimestamp, s.student_mode, sf.hse, sf.ss from student_bio_dim    s, customer_info      c, subtest_score_fact sf, subtest_dim        sd, form_dim           f where s.customerid = c.customerid and s.student_bio_id = sf.student_bio_id and sf.subtestid = sd.subtestid and sf.formid = f.formid and s.student_mode = 'OL') s, (select s.state_code, s.uuid, t.barcode, t.form, UPPER(TRIM(S.LASTNAME)) AS LAST_NAME, TO_CHAR(T.DATE_SCHEDULED, 'DD-MON-YYYY') AS DATE_SCHEDULED, t.content_area_code, t.pp_oas_linkedid, t.content_test_code ");
		queryBuff.append(" ,s.comments ");
		queryBuff.append(" from er_student_demo s, er_test_schedule t where s.er_studid = t.er_studid) e where s.state_code = e.state_code and s.UUID = e.UUID and s.form_code = e.form and s.subtest_code = e.content_area_code AND s.test_element_id = e.pp_oas_linkedid ");
		
		if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
			queryBuff.append(" and s.test_element_id = ? ");
		}
		if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
			queryBuff.append(" and s.uuid like ? ");
		}
		if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
			queryBuff.append(" and s.state_code = ? ");
		}
		
		queryBuff.append("union ");
		
		queryBuff.append("select s.state_code state, s.subtest_code, s.subtest_name, s.form_code form, e.content_test_code, s.student_mode type, pp_oas_linkedid, s.barcode,s.status_code, s.ss, s.student_bio_id, s.test_element_id, s.name, s.datetimestamp,s.hse,e.uuid ");
		queryBuff.append(" ,e.comments ");
		queryBuff.append(" from (select c.customerid, c.customer_name, c.customer_code as state_code, s.ext_student_id as UUID, s.barcode, sd.subtest_code, f.form_code, s.student_bio_id, s.test_element_id, s.last_name || ', ' || s.first_name || ' ' || s.middle_name name, s.middle_name, s.last_name, sd.subtest_name, TO_CHAR(sf.test_date, 'DD-MON-YYYY') AS test_date, sf.status_code, sf.datetimestamp, s.student_mode, sf.hse, sf.ss from student_bio_dim    s, customer_info      c, subtest_score_fact sf, subtest_dim        sd, form_dim           f where s.customerid = c.customerid and s.student_bio_id = sf.student_bio_id and sf.subtestid = sd.subtestid and sf.formid = f.formid and s.student_mode = 'PP') s, (select s.state_code, s.uuid, t.barcode, t.form, t.content_area_code, t.pp_oas_linkedid, t.content_test_code ");
		queryBuff.append(" ,s.comments ");
		queryBuff.append(" from er_student_demo s, er_test_schedule t where s.er_studid = t.er_studid) e where s.state_code = e.state_code and s.UUID = e.UUID and s.barcode = e.barcode and s.form_code = e.form and s.subtest_code = e.content_area_code and s.test_element_id = e.pp_oas_linkedid ");
		if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
			queryBuff.append(" and s.test_element_id = ? ");
		}
		if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
			queryBuff.append(" and s.uuid like ? ");
		}
		if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
			queryBuff.append(" and s.state_code = ? ");
		}
		queryBuff.append("order by 1, 2 ");
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
				pstmt.setString(++count, searchProcess.getTestElementId() );
			}
			if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
				pstmt.setString(++count, "%"+searchProcess.getUuid()+"%" );
			}
			if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
				pstmt.setString(++count, searchProcess.getStateCode() );
			}
			if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
				pstmt.setString(++count, searchProcess.getTestElementId() );
			}
			if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
				pstmt.setString(++count, "%"+searchProcess.getUuid()+"%" );
			}
			if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
				pstmt.setString(++count, searchProcess.getStateCode() );
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new StudentDetailsTO();
				processTO.setStateCode(rs.getString(1));
				processTO.setSubtestName(rs.getString(3));
				processTO.setForm(rs.getString(4));
				processTO.setContentTestCode(rs.getString(5));
				processTO.setSourceSystem(rs.getString(6));
				processTO.setPpOaslinkedId(rs.getString(7));
				processTO.setBarcode(rs.getString(8));
				processTO.setStatusCode(rs.getString(9));
				processTO.setSs(rs.getString(10));
				processTO.setStudentBioId(rs.getString(11));
				processTO.setTestElementId(rs.getString(12));
				processTO.setStudentName(rs.getString(13));
				processTO.setUpdatedDate(rs.getString(14));
				processTO.setHse(rs.getString(15));
				processTO.setUuid(rs.getString(16));
				processTO.setComments(rs.getString(17));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		System.out.println("Exit: getCombinedProcess()");
		return processList;
	}
	
	public List<StudentDetailsTO> getERBucket(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getERBucket()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentDetailsTO processTO = null;
		List<StudentDetailsTO> processList = new ArrayList<StudentDetailsTO>();
		StringBuffer queryBuff = new StringBuffer();
				
		queryBuff.append("select schedule_id, content_area_code, content_test_code, decode(content_test_type,0,'OL','PP') type, pp_oas_linkedid, barcode, form,  testcentername, s.created_date_time, s.updated_date_time, uuid, state_code, lastname || ', ' || firstname || ' ' || middlename name, s.date_scheduled, d.COMMENTS,s.er_test_schedid  from er_test_schedule s, er_student_demo d  where d.er_studid = s.er_studid ");
		if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
			queryBuff.append(" and uuid like ? ");
		}
		if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
			queryBuff.append(" and state_code = ? ");
		}
		if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
			queryBuff.append(" and pp_oas_linkedid = ? ");
		}
		
		queryBuff.append("order by 2 ");
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
				pstmt.setString(++count, "%"+searchProcess.getUuid()+"%" );
			}
			if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
				pstmt.setString(++count, searchProcess.getStateCode() );
			}
			if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
				pstmt.setString(++count, searchProcess.getTestElementId());
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new StudentDetailsTO();
				processTO.setScheduleId(rs.getString(1));
				processTO.setContantArea(rs.getString(2));
				processTO.setContentTestCode(rs.getString(3));
				processTO.setSourceSystem(rs.getString(4));
				processTO.setPpOaslinkedId(rs.getString(5));
				processTO.setBarcode(rs.getString(6));
				processTO.setForm(rs.getString(7));
				processTO.setTestCenterName(rs.getString(8));
				processTO.setCreatedDate(rs.getString(9));
				processTO.setUpdatedDate(rs.getString(10));
				processTO.setUuid(rs.getString(11));
				processTO.setStateCode(rs.getString(12));
				processTO.setStudentName(rs.getString(13));
				processTO.setDateScheduled(rs.getString(14));
				processTO.setComments(rs.getString(15));
				processTO.setErTestSchId(rs.getString(16));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		System.out.println("Exit: getERBucket()");
		return processList;
	}
	
	public List<StudentDetailsTO> getERError(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getERError()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentDetailsTO processTO = null;
		List<StudentDetailsTO> processList = new ArrayList<StudentDetailsTO>();
		StringBuffer queryBuff = new StringBuffer();
				
		queryBuff.append("select distinct exc.er_uuid,exc.content_code, exc.state_code, exc.form, exc.last_name, exc.barcode, hist.date_scheduled, description, hist.content_test_code, decode(hist.content_test_type,'0','OL','PP') type, trunc(datetimestamp), hist.schedule_id from er_exception_data exc, er_student_sched_history hist  where hist.er_ss_histid = exc.er_ss_histid  and source_system = 'ERESOURCE'  ");
		if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
			queryBuff.append(" and exc.er_uuid like ? ");
		}
		if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
			queryBuff.append(" and exc.state_code = ? ");
		}
		if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
			queryBuff.append(" and test_element_id = ? ");
		}
		
		//queryBuff.append(" group by exc.er_uuid,exc.content_code, exc.state_code, exc.form, exc.last_name, exc.barcode, hist.date_scheduled, description, hist.content_test_code, decode(hist.content_test_type,'0','OL','PP'), trunc(datetimestamp),hist.schedule_id ");
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
				pstmt.setString(++count, "%"+searchProcess.getUuid()+"%" );
			}
			if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
				pstmt.setString(++count, searchProcess.getStateCode() );
			}
			if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
				pstmt.setString(++count, searchProcess.getTestElementId());
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new StudentDetailsTO();
				processTO.setUuid(rs.getString(1));
				processTO.setContantArea(rs.getString(2));
				processTO.setStateCode(rs.getString(3));
				processTO.setForm(rs.getString(4));
				processTO.setStudentName(rs.getString(5));
				processTO.setBarcode(rs.getString(6));
				processTO.setTestDate(rs.getString(7));
				processTO.setErrorLog(rs.getString(8));
				processTO.setContentTestCode(rs.getString(9));
				processTO.setSourceSystem(rs.getString(10));
				processTO.setCreatedDate(rs.getString(11));
				processTO.setScheduleId(rs.getString(12));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		System.out.println("Exit: getERError()");
		return processList;
	}
	
	public List<StudentDetailsTO> getOasPPError(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getOasPPError()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentDetailsTO processTO = null;
		List<StudentDetailsTO> processList = new ArrayList<StudentDetailsTO>();
		StringBuffer queryBuff = new StringBuffer();
				
		queryBuff.append("select source_system, er_uuid, test_element_id, state_code, last_name, form, barcode, test_date, description, created_date_time,updated_date_time, content_code from er_exception_data exc where source_system in ( 'PP', 'OAS' ) and exc.exception_status not in ('IN') ");
		if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
			queryBuff.append(" and er_uuid like ? ");
		}
		if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
			queryBuff.append(" and state_code = ? ");
		}
		if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
			queryBuff.append(" and test_element_id = ? ");
		}
		
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			if(searchProcess != null && searchProcess.getUuid() != null && searchProcess.getUuid().length() > 0) {
				pstmt.setString(++count, "%"+searchProcess.getUuid()+"%" );
			}
			if(searchProcess != null && searchProcess.getStateCode() != null && searchProcess.getStateCode().length() > 0) {
				pstmt.setString(++count, searchProcess.getStateCode() );
			}
			if(searchProcess != null && searchProcess.getTestElementId() != null && searchProcess.getTestElementId().length() > 0) {
				pstmt.setString(++count, searchProcess.getTestElementId());
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new StudentDetailsTO();
				processTO.setSourceSystem(rs.getString(1));
				processTO.setUuid(rs.getString(2));
				processTO.setTestElementId(rs.getString(3));
				processTO.setStateCode(rs.getString(4));
				processTO.setStudentName(rs.getString(5));
				processTO.setForm(rs.getString(6));
				processTO.setBarcode(rs.getString(7));
				processTO.setTestDate(rs.getString(8));
				processTO.setErrorLog(rs.getString(9));
				processTO.setCreatedDate(rs.getString(10));
				processTO.setUpdatedDate(rs.getString(11));
				processTO.setContantArea(rs.getString(12));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		System.out.println("Exit: getOasPPError()");
		return processList;
	}
	
	public int saveComments(StudentDetailsTO studentDetailsTO )  throws Exception {
		
		System.out.println("Enter: saveComment()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updatedRows = 0;
		
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("UPDATE ER_STUDENT_DEMO SET COMMENTS = ? WHERE UUID = ? AND STATE_CODE = ?");
		
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, studentDetailsTO.getComments());
			pstmt.setString(2, studentDetailsTO.getUuid());
			pstmt.setString(3, studentDetailsTO.getStateCode());
			
			updatedRows = pstmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		
		
		System.out.println("Exit: saveComment()");
		return updatedRows;
	}
	
	
	public List<TASCProcessTO> getScoreReview(SearchProcess searchProcess) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TASCProcessTO processTO = null;
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append(" select s.last_name || ', ' || s.first_name || ' ' || s.middle_name STUD_NAME, s.ext_student_id UUID,c.customer_code,sub.subtest_name,f.opr_ncr, f.opr_ss, f.opr_hse, f.subtestid, max(f.created_date_time) datetimestamp, s.student_bio_id ");
		queryBuff.append(" from SCR_STUDENT_BIO_DIM s, SCR_SUBTEST_SCORE_FACT f, customer_info c, form_dim fr, subtest_dim sub ");
		queryBuff.append(" where s.student_bio_id = f.student_bio_id and s.customerid = c.customerid and fr.formid = f.formid and sub.subtestid = f.subtestid ");
		if(searchProcess != null) {
			//queryBuff.append(" WHERE 1 = 1 ");
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0
					&& searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0) {
				queryBuff.append("AND (f.created_date_time between to_date(?, 'MM/DD/YYYY') and to_date(?, 'MM/DD/YYYY')+1) ");
			}
			if(searchProcess.getSourceSystem() != null && searchProcess.getSourceSystem().trim().length() > 0
					&& !"-1".equals(searchProcess.getSourceSystem())) 
				queryBuff.append("AND s.student_mode = ? ");
			if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0) 
				queryBuff.append("AND f.scr_status = ? ");
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0) 
				queryBuff.append("AND s.ext_student_id = ? ");
			if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0) 
				queryBuff.append("AND c.customer_code = ? ");
			if(searchProcess.getStatus() != null && searchProcess.getStatus().trim().length() > 0) 
				queryBuff.append("AND f.scr_status = ? ");
		}
		queryBuff.append(" group by s.last_name || ', ' || s.first_name || ' ' || s.middle_name , s.ext_student_id , c.customer_code, sub.subtest_name, f.opr_ncr, f.opr_ss, f.opr_hse, f.subtestid, s.student_bio_id order by datetimestamp ");
		String query = queryBuff.toString();
		 System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			if(searchProcess != null) {
				if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0
						&& searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0) {
					pstmt.setString(++count, searchProcess.getProcessedDateFrom());
					pstmt.setString(++count, searchProcess.getProcessedDateTo());
				}
				if(searchProcess.getSourceSystem() != null && searchProcess.getSourceSystem().trim().length() > 0 
						&& !"-1".equals(searchProcess.getSourceSystem())) 
					pstmt.setString(++count, searchProcess.getSourceSystem());
				if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0) 
					pstmt.setString(++count, searchProcess.getStateCode());
				if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0) 
					pstmt.setString(++count, searchProcess.getUuid());
				if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0) 
					pstmt.setString(++count, searchProcess.getStateCode());
				if(searchProcess.getStatus() != null && searchProcess.getStatus().trim().length() > 0) 
					pstmt.setString(++count, searchProcess.getStatus());
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new TASCProcessTO();
				processTO.setStudentName(rs.getString(1));
				processTO.setUuid(rs.getString(2));
				processTO.setStateCode(rs.getString(3));
				processTO.setSubtestName(rs.getString(4));
				processTO.setNc(rs.getString(5));
				processTO.setSs(rs.getString(6));
				processTO.setHse(rs.getString(7));
				processTO.setSubtest(rs.getString(8));
				processTO.setDateTimestamp(rs.getString(9));
				processTO.setStudentBioId(rs.getString(10));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return processList;
	}
	
	public List<TASCProcessTO> getReviewResult(SearchProcess searchProcess) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TASCProcessTO processTO = null;
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append(" select s.formid, s.ss, s.ncr, s.hse, s.created_date_time, s.scr_comment, s.scr_status, s.is_active ");
		queryBuff.append(" from scr_subtest_score_fact s ");
		queryBuff.append(" where student_bio_id = ? and subtestid = ? ");
		queryBuff.append(" order by created_date_time ");
		String query = queryBuff.toString();
		 System.out.println(query);
		try {
			conn = BaseDAO.connect(DATA_SOURCE);
			int count = 0;
			pstmt = conn.prepareCall(query);
			pstmt.setString(++count, searchProcess.getStudentBioId());
			pstmt.setString(++count, searchProcess.getSubtestId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new TASCProcessTO();
				processTO.setForm(rs.getString(1));
				processTO.setSs(rs.getString(2));
				processTO.setNc(rs.getString(3));
				processTO.setHse(rs.getString(4));
				processTO.setDateTimestamp(rs.getString(5));
				processTO.setComments(rs.getString(6));
				processTO.setStatus(rs.getString(7));
				processTO.setIsActive(rs.getString(8));
				processList.add(processTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return processList;
	}
	
}
