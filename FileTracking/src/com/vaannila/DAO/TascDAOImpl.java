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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
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
		String processLog = "";
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		try {
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
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
	 * Get Searched records
	 * @throws Exception
	 */
	public List<StudentDetailsTO> getProcessEr(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getProcessEr()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentDetailsTO studentDetailsTO = null;
		List<StudentDetailsTO> studentDetailsTOList = new ArrayList<StudentDetailsTO>();
		StringBuffer queryBuff = new StringBuffer();
		
		if("ERESOURCE".equals(searchProcess.getSourceSystem())){
			//temporary patch needed for production
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				getERQueryForDate(queryBuff);
			} else {
				queryBuff.append("SELECT DISTINCT ESSH.LASTNAME || ', ' || ESSH.FIRSTNAME || ' ' || ESSH.MIDDLENAME STUDENTNAME,");
				queryBuff.append(" ESSH.UUID UUID,");
				queryBuff.append(" TO_CHAR(NVL(EED.TEST_ELEMENT_ID, 'NA')) TEST_ELEMENT_ID,");
				queryBuff.append(" NVL(TO_CHAR(EED.PROCESS_ID), 'NA') PROCESS_ID,");
				queryBuff.append(" TO_CHAR(NVL(EED.EXCEPTION_CODE, 'NA')) EXCEPTION_CODE,");
				queryBuff.append(" NVL(EED.SOURCE_SYSTEM, 'ERESOURCE') SOURCE_SYSTEM,");
				queryBuff.append(" NVL(EED.EXCEPTION_STATUS, 'CO') EXCEPTION_STATUS,");
				queryBuff.append(" ESSH.ER_SS_HISTID ER_SS_HISTID,");
				queryBuff.append(" ESSH.BARCODE BARCODE,");
				queryBuff.append(" ESSH.DATE_SCHEDULED DATE_SCHEDULED,");
				queryBuff.append(" ESSH.STATE_CODE STATE_CODE,");
				queryBuff.append(" ESSH.FORM FORM,");
				queryBuff.append(" ESSH.DATETIMESTAMP,");
				queryBuff.append(" NVL(EED.ER_EXCDID, 0) ER_EXCDID,");
				queryBuff.append(" (SELECT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTEST_CODE = ESSH.CONTENT_AREA_CODE) SUBTEST,");
				queryBuff.append(" ESSH.TESTCENTERCODE TESTING_SITE_CODE,");
				queryBuff.append(" ESSH.TESTCENTERNAME TESTING_SITE_NAME,");
				queryBuff.append(" ESSH.CTB_CUSTOMER_ID CTB_CUSTOMER_ID,");
				queryBuff.append(" ESSH.STATENAME STATENAME,");
				queryBuff.append(" ESSH.DATEOFBIRTH DATEOFBIRTH,");
				queryBuff.append(" ESSH.GENDER GENDER,");
				queryBuff.append(" ESSH.GOVERNMENTID GOVERNMENTID,");
				queryBuff.append(" ESSH.GOVERNMENTIDTYPE GOVERNMENTIDTYPE,");
				queryBuff.append(" ESSH.ADDRESS1 ADDRESS1,");
				queryBuff.append(" ESSH.CITY CITY,");
				queryBuff.append(" ESSH.COUNTY COUNTY,");
				queryBuff.append(" ESSH.STATE STATE,");
				queryBuff.append(" ESSH.ZIP ZIP,");
				queryBuff.append(" ESSH.EMAIL EMAIL,");
				queryBuff.append(" ESSH.ALTERNATEEMAIL ALTERNATEEMAIL,");
				queryBuff.append(" ESSH.PRIMARYPHONENUMBER PRIMARYPHONENUMBER,");
				queryBuff.append(" ESSH.CELLPHONENUMBER CELLPHONENUMBER,");
				queryBuff.append(" ESSH.ALTERNATENUMBER ALTERNATENUMBER,");
				queryBuff.append(" ESSH.RESOLVED_ETHNICITY_RACE RESOLVED_ETHNICITY_RACE,");
				queryBuff.append(" ESSH.HOMELANGUAGE HOMELANGUAGE,");
				queryBuff.append(" ESSH.EDUCATIONLEVEL EDUCATIONLEVEL,");
				queryBuff.append(" ESSH.ATTENDCOLLEGE ATTENDCOLLEGE,");
				queryBuff.append(" ESSH.CONTACT CONTACT,");
				queryBuff.append(" ESSH.EXAMINEECOUNTYPARISHCODE EXAMINEECOUNTYPARISHCODE,");
				queryBuff.append(" ESSH.REGISTEREDON REGISTEREDON,");
				queryBuff.append(" ESSH.REGISTEREDATTESTCENTER REGISTEREDATTESTCENTER,");
				queryBuff.append(" ESSH.REGISTEREDATTESTCENTERCODE REGISTEREDATTESTCENTERCODE,");
				queryBuff.append(" ESSH.SCHEDULE_ID SCHEDULE_ID,");
				queryBuff.append(" ESSH.TIMEOFDAY TIMEOFDAY,");
				queryBuff.append(" ESSH.DATECHECKEDIN DATECHECKEDIN,");
				queryBuff.append(" ESSH.CONTENT_TEST_TYPE CONTENT_TEST_TYPE,");
				queryBuff.append(" ESSH.CONTENT_TEST_CODE CONTENT_TEST_CODE,");
				queryBuff.append(" ESSH.TASCREADINESS TASCREADINESS,");
				queryBuff.append(" ESSH.ECC ECC,");
				queryBuff.append(" ESSH.REGST_TC_COUNTYPARISHCODE REGST_TC_COUNTYPARISHCODE,");
				queryBuff.append(" ESSH.SCHED_TC_COUNTYPARISHCODE SCHED_TC_COUNTYPARISHCODE,");
				queryBuff.append(" DECODE(NVL(EED.ER_EXCDID, 0), 0, '', 'ERROR CODE-' || EED.EXCEPTION_CODE || ': ' || EED.DESCRIPTION) ERROR_DESCRIPTION,");
				queryBuff.append(" TO_CHAR(ESSH.DATETIMESTAMP, 'MM/DD/YYYY HH:mm:ss') PROCESSED_DATE");
				queryBuff.append(" FROM ER_STUDENT_SCHED_HISTORY ESSH");
				queryBuff.append(" LEFT OUTER JOIN ER_EXCEPTION_DATA EED");
				queryBuff.append(" ON ESSH.ER_SS_HISTID = EED.ER_SS_HISTID");
				queryBuff.append(" WHERE 1 = 1");
				if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
					queryBuff.append(" AND TRUNC(ESSH.DATETIMESTAMP) >= TO_DATE(?, 'MM/DD/YYYY')");
				}
				if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
					queryBuff.append(" AND TRUNC(ESSH.DATETIMESTAMP) <= TO_DATE(?, 'MM/DD/YYYY')");
				}
				if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
					queryBuff.append(" AND ESSH.UUID LIKE ?");
				}
				if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
					queryBuff.append(" AND UPPER(ESSH.LASTNAME) LIKE UPPER(?)");
				}
				if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
					queryBuff.append(" AND EED.EXCEPTION_CODE = ?");
				}
				if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					queryBuff.append(" AND ESSH.ER_SS_HISTID = ?");
				}
				if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
					queryBuff.append(" AND EED.PROCESS_ID = ?");
				}
				if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
					queryBuff.append(" AND ESSH.STATE_CODE = ?");
				}
				if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
					queryBuff.append(" AND ESSH.FORM = ?");
				}
				if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
					queryBuff.append(" AND EED.TEST_ELEMENT_ID = ?");
				}
				if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
					queryBuff.append(" AND ESSH.BARCODE = ?");
				}
				queryBuff.append(" ORDER BY ESSH.DATETIMESTAMP DESC, STUDENTNAME");
			}
			
		} else{
			queryBuff.append("SELECT nvl(EED.LAST_NAME,ESD.lastname) || ', ' || ESD.FIRSTNAME || ' ' || ESD.MIDDLENAME STUDENTNAME,");
			queryBuff.append(" EED.ER_UUID UUID,");
			queryBuff.append(" TO_CHAR(NVL(EED.TEST_ELEMENT_ID, 'NA')) TEST_ELEMENT_ID,");
			queryBuff.append(" NVL(TO_CHAR(EED.PROCESS_ID), 'NA') PROCESS_ID,");
			queryBuff.append(" TO_CHAR(NVL(EED.EXCEPTION_CODE, 'NA')) EXCEPTION_CODE,");
			queryBuff.append(" NVL(EED.SOURCE_SYSTEM, 'NA') SOURCE_SYSTEM,");
			queryBuff.append(" NVL(EED.EXCEPTION_STATUS, 'NA') EXCEPTION_STATUS,");
			queryBuff.append(" 0 ER_SS_HISTID,");
			queryBuff.append(" EED.BARCODE BARCODE,");
			queryBuff.append(" TO_CHAR(EED.TEST_DATE, 'MM/DD/YYYY') DATE_SCHEDULED,");
			queryBuff.append(" EED.STATE_CODE STATE_CODE,");
			queryBuff.append(" EED.FORM FORM,");
			queryBuff.append(" EED.CREATED_DATE_TIME DATETIMESTAMP,");
			queryBuff.append(" NVL(EED.ER_EXCDID,0) ER_EXCDID,");
			queryBuff.append(" (SELECT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTEST_CODE = EED.CONTENT_CODE) SUBTEST,");
			queryBuff.append(" EED.TESTING_SITE_CODE TESTING_SITE_CODE,");
			queryBuff.append(" EED.TESTING_SITE_NAME TESTING_SITE_NAME,");
			queryBuff.append(" DECODE(NVL(EED.ER_EXCDID, 0), 0, '', 'ERROR CODE-' || EED.EXCEPTION_CODE || ': ' || EED.DESCRIPTION) ERROR_DESCRIPTION,");
			queryBuff.append(" TO_CHAR(EED.CREATED_DATE_TIME, 'MM/DD/YYYY HH:mm:ss') PROCESSED_DATE");
			queryBuff.append(" FROM ER_EXCEPTION_DATA EED,");
			queryBuff.append(" ER_STUDENT_DEMO   ESD");
			queryBuff.append(" WHERE EED.ER_UUID = ESD.UUID");
			queryBuff.append(" and (eed.state_code is null or eed.state_code = esd.state_code)");
			//queryBuff.append(" AND EED.STATE_CODE = ESD.STATE");
			queryBuff.append(" AND EED.SOURCE_SYSTEM = ?");
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				queryBuff.append(" AND TRUNC(EED.CREATED_DATE_TIME) >= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				queryBuff.append(" AND TRUNC(EED.CREATED_DATE_TIME) <= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				queryBuff.append(" AND EED.ER_UUID LIKE ?");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				queryBuff.append(" AND UPPER(EED.LAST_NAME) LIKE UPPER(?)");
			}
			if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
				queryBuff.append(" AND EED.EXCEPTION_CODE = ?");
			}
			/*if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
				queryBuff.append(" AND EED.ER_SS_HISTID = ?");
			}*/
			if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
				queryBuff.append(" AND EED.PROCESS_ID = ?");
			}
			if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
				queryBuff.append(" AND EED.STATE_CODE = ?");
			}
			if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
				queryBuff.append(" AND EED.FORM = ?");
			}
			if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
				queryBuff.append(" AND EED.TEST_ELEMENT_ID = ?");
			}
			if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
				queryBuff.append(" AND EED.BARCODE = ?");
			}

			queryBuff.append(" UNION");
			
			queryBuff.append(" SELECT SSBD.LAST_NAME || ', ' || SSBD.FIRST_NAME || ' ' || SSBD.MIDDLE_NAME STUDENTNAME,");
			queryBuff.append(" SSBD.EXT_STUDENT_ID UUID,");
			queryBuff.append(" TO_CHAR(NVL(SSBD.TEST_ELEMENT_ID, 'NA')) TEST_ELEMENT_ID,");
			queryBuff.append(" NVL(TO_CHAR(SPS.PROCESS_ID), 'NA') PROCESS_ID,");
			//queryBuff.append(" 'NA' EXCEPTION_CODE,");
			queryBuff.append(" TO_CHAR(NVL(EED.EXCEPTION_CODE, 'NA')) EXCEPTION_CODE,");
			queryBuff.append(" NVL(EED.SOURCE_SYSTEM, 'NA') SOURCE_SYSTEM,");
			//queryBuff.append(" 'NA' EXCEPTION_STATUS,");
			queryBuff.append(" NVL(EED.EXCEPTION_STATUS, 'NA') EXCEPTION_STATUS, ");
			queryBuff.append(" 0 ER_SS_HISTID,");
			queryBuff.append(" SSBD.BARCODE BARCODE,");
			queryBuff.append(" TO_CHAR(SSSD.DATE_TEST_TAKEN, 'MM/DD/YYYY') DATE_SCHEDULED,");
			queryBuff.append(" EED.state_code STATE_CODE,");
			queryBuff.append(" SSSD.TEST_FORM FORM,");
			queryBuff.append(" SPS.DATETIMESTAMP DATETIMESTAMP,");
			//queryBuff.append(" 0 ER_EXCDID,");
			queryBuff.append(" NVL(EED.ER_EXCDID,0) ER_EXCDID,");
			queryBuff.append(" (SELECT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTEST_CODE = SSSD.CONTENT_NAME) SUBTEST,");
			queryBuff.append(" 'NA' TESTING_SITE_CODE,");
			queryBuff.append(" 'NA' TESTING_SITE_NAME,");
			//queryBuff.append(" '' ERROR_DESCRIPTION,");
			queryBuff.append(" DECODE(NVL(EED.ER_EXCDID, 0),0,'','ERROR CODE-' || EED.EXCEPTION_CODE || ': ' || EED.DESCRIPTION) ERROR_DESCRIPTION, ");
			queryBuff.append(" TO_CHAR(SPS.DATETIMESTAMP, 'MM/DD/YYYY HH:mm:ss') PROCESSED_DATE");
			queryBuff.append(" FROM STG_STD_BIO_DETAILS     SSBD,");
			queryBuff.append(" STG_STD_SUBTEST_DETAILS SSSD,");
			queryBuff.append(" STG_HIER_DETAILS        SHD,");
			queryBuff.append(" STG_PROCESS_STATUS      SPS,");
			queryBuff.append(" ER_EXCEPTION_DATA EED ");
			queryBuff.append(" WHERE ssbd.wkf_partition_name = 'ER_EXCP' AND SSSD.WKF_PARTITION_NAME = 'ER_EXCP' ");
			queryBuff.append(" AND SSBD.STUDENT_BIO_DETAILS_ID = SSSD.STUDENT_BIO_DETAILS_ID");
			queryBuff.append(" and SSBD.TEST_ELEMENT_ID = EED.TEST_ELEMENT_ID");
			queryBuff.append(" AND EED.PROCESS_ID = SPS.PROCESS_ID");
			//queryBuff.append(" AND SSBD.WKF_PARTITION_NAME = 'BR_EXCP'");
			queryBuff.append(" AND SSSD.CONTENT_NAME = EED.CONTENT_CODE ");
			//queryBuff.append(" AND SHD.ORG_LEVEL = 1");
			queryBuff.append(" AND EED.SOURCE_SYSTEM = ?");
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				queryBuff.append(" AND TRUNC(SPS.DATETIMESTAMP) >= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				queryBuff.append(" AND TRUNC(SPS.DATETIMESTAMP) <= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				queryBuff.append(" AND SSBD.EXT_STUDENT_ID LIKE ?");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				queryBuff.append(" AND UPPER(SSBD.LAST_NAME) LIKE UPPER(?)");
			}
			if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
				queryBuff.append(" AND SPS.PROCESS_ID = ?");
			}
			if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
				queryBuff.append(" AND SHD.ORG_CODE = ?");
			}
			if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
				queryBuff.append(" AND SSSD.TEST_FORM = ?");
			}
			if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
				queryBuff.append(" AND SSBD.TEST_ELEMENT_ID = ?");
			}
			if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
				queryBuff.append(" AND SSBD.BARCODE = ?");
			}
			queryBuff.append(" ORDER BY DATETIMESTAMP DESC, STUDENTNAME");
		}
			
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			int count = 0;
			pstmt = conn.prepareCall(query);
			
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getProcessedDateFrom());
				}
				if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getProcessedDateTo());
				}
				if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
					pstmt.setString(++count, "%"+searchProcess.getUuid()+"%");
				}
				if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
					pstmt.setString(++count, "%"+searchProcess.getLastName()+"%");
				}
				if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getExceptionCode()));
				}
				if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getRecordId()));
				}
				if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getProcessId()));
				}
				if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getStateCode());
				}
				if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getForm());
				}
				if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getTestElementId());
				}
				if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getBarcode());
				}
			}else{
				pstmt.setString(++count, searchProcess.getSourceSystem());
				if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getProcessedDateFrom());
				}
				if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getProcessedDateTo());
				}
				if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
					pstmt.setString(++count, "%"+searchProcess.getUuid()+"%");
				}
				if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
					pstmt.setString(++count, "%"+searchProcess.getLastName()+"%");
				}
				if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getExceptionCode()));
				}
				/*if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getRecordId()));
				}*/
				if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getProcessId()));
				}
				if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getStateCode());
				}
				if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getForm());
				}
				if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getTestElementId());
				}
				if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getBarcode());
				}
				//pstmt.setString(++count, ("OAS".equals(searchProcess.getSourceSystem())? "OL" : searchProcess.getSourceSystem()));
				pstmt.setString(++count, searchProcess.getSourceSystem());
				if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getProcessedDateFrom());
				}
				if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getProcessedDateTo());
				}
				if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
					pstmt.setString(++count, "%"+searchProcess.getUuid()+"%");
				}
				if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
					pstmt.setString(++count, "%"+searchProcess.getLastName()+"%");
				}
				if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getProcessId()));
				}
				if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getStateCode());
				}
				if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getForm());
				}
				if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getTestElementId());
				}
				if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
					pstmt.setString(++count, searchProcess.getBarcode());
				}
			}
			
			rs = pstmt.executeQuery();
			boolean hasData = false;
			while(rs.next()) {
				hasData = true;
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
				studentDetailsTO.setTestCenterCode(rs.getString("TESTING_SITE_CODE") != null ? rs.getString("TESTING_SITE_CODE") : "");
				studentDetailsTO.setTestCenterName(rs.getString("TESTING_SITE_NAME") != null ? rs.getString("TESTING_SITE_NAME") : "");
				studentDetailsTO.setSourceSystemDesc(searchProcess.getSourceSystemDesc());
				studentDetailsTO.setProcessedDateFrom(searchProcess.getProcessedDateFrom());
				studentDetailsTO.setProcessedDateTo(searchProcess.getProcessedDateTo());
				studentDetailsTO.setErrorLog(rs.getString("ERROR_DESCRIPTION") != null ? rs.getString("ERROR_DESCRIPTION") : "");
				studentDetailsTO.setProcessedDate(rs.getString("PROCESSED_DATE") != null ? rs.getString("PROCESSED_DATE") : "");
				
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
				}
				studentDetailsTOList.add(studentDetailsTO);
			} 
			
			if(!hasData) {
				// this section is added for which we don't have any data in staging table
				if(!"ERESOURCE".equals(searchProcess.getSourceSystem())){
					try {pstmt.close();} catch (Exception e2) {}
					try {rs.close();} catch (Exception e2) {}
					
					queryBuff = new StringBuffer();
					queryBuff.append("select ex.last_name STUDENTNAME, ex.er_uuid UUID, ex.test_element_id TEST_ELEMENT_ID, " );
					queryBuff.append(" ex.process_id PROCESS_ID, ex.exception_code EXCEPTION_CODE, ex.source_system SOURCE_SYSTEM, " );
					queryBuff.append(" ex.exception_status EXCEPTION_STATUS, 0 ER_SS_HISTID, ex.barcode BARCODE, ex.test_date DATE_SCHEDULED, " );
					queryBuff.append(" ex.state_code STATE_CODE, ex.form FORM, ex.created_date_time DATETIMESTAMP, ex.er_excdid ER_EXCDID, " );
					queryBuff.append(" ex.content_code SUBTEST, 'NA' TESTING_SITE_CODE, 'NA' TESTING_SITE_NAME, ex.description ERROR_DESCRIPTION, " );
					queryBuff.append(" ex.created_date_time PROCESSED_DATE " );
					queryBuff.append(" from er_exception_data ex" );
					queryBuff.append(" where ex.source_system = ? ");
					if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
						queryBuff.append(" AND TRUNC(ex.created_date_time) >= TO_DATE(?, 'MM/DD/YYYY')");
					}
					if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
						queryBuff.append(" AND TRUNC(ex.created_date_time) <= TO_DATE(?, 'MM/DD/YYYY')");
					}
					if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
						queryBuff.append(" AND ex.er_uuid LIKE ?");
					}
					if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
						queryBuff.append(" AND UPPER(ex.last_name) LIKE UPPER(?)");
					}
					if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
						queryBuff.append(" AND ex.process_id = ?");
					}
					if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
						queryBuff.append(" AND ex.state_code = ?");
					}
					if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
						queryBuff.append(" AND ex.form = ?");
					}
					if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
						queryBuff.append(" AND ex.test_element_id = ?");
					}
					if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
						queryBuff.append(" AND ex.barcode = ?");
					}
					queryBuff.append(" ORDER BY ex.created_date_time DESC, ex.last_name");
					
					query = queryBuff.toString();
					System.out.println(query);
					count = 0;
					pstmt = conn.prepareCall(query);
					pstmt.setString(++count, searchProcess.getSourceSystem());
					if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
						pstmt.setString(++count, searchProcess.getProcessedDateFrom());
					}
					if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
						pstmt.setString(++count, searchProcess.getProcessedDateTo());
					}
					if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
						pstmt.setString(++count, "%"+searchProcess.getUuid()+"%");
					}
					if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
						pstmt.setString(++count, "%"+searchProcess.getLastName()+"%");
					}
					if(searchProcess.getProcessId() != null && searchProcess.getProcessId().trim().length() > 0){
						pstmt.setLong(++count, Long.parseLong(searchProcess.getProcessId()));
					}
					if(searchProcess.getStateCode() != null && searchProcess.getStateCode().trim().length() > 0){
						pstmt.setString(++count, searchProcess.getStateCode());
					}
					if(searchProcess.getForm() != null && searchProcess.getForm().trim().length() > 0){
						pstmt.setString(++count, searchProcess.getForm());
					}
					if(searchProcess.getTestElementId() != null && searchProcess.getTestElementId().trim().length() > 0){
						pstmt.setString(++count, searchProcess.getTestElementId());
					}
					if(searchProcess.getBarcode() != null && searchProcess.getBarcode().trim().length() > 0){
						pstmt.setString(++count, searchProcess.getBarcode());
					}
					
					rs = pstmt.executeQuery();
					while(rs.next()) {
						hasData = true;
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
						studentDetailsTO.setTestCenterCode(rs.getString("TESTING_SITE_CODE") != null ? rs.getString("TESTING_SITE_CODE") : "");
						studentDetailsTO.setTestCenterName(rs.getString("TESTING_SITE_NAME") != null ? rs.getString("TESTING_SITE_NAME") : "");
						studentDetailsTO.setSourceSystemDesc(searchProcess.getSourceSystemDesc());
						studentDetailsTO.setProcessedDateFrom(searchProcess.getProcessedDateFrom());
						studentDetailsTO.setProcessedDateTo(searchProcess.getProcessedDateTo());
						studentDetailsTO.setErrorLog(rs.getString("ERROR_DESCRIPTION") != null ? rs.getString("ERROR_DESCRIPTION") : "");
						studentDetailsTO.setProcessedDate(rs.getString("PROCESSED_DATE") != null ? rs.getString("PROCESSED_DATE") : "");
						studentDetailsTOList.add(studentDetailsTO);
					}
				
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: getProcessEr() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentDetailsTOList;
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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
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
		queryBuff.append("TESTING_SITE_NAME");
		queryBuff.append(" FROM ER_EXCEPTION_DATA");
		queryBuff.append(" WHERE ER_EXCDID = ?");
		String query = queryBuff.toString();
		System.out.println(query);
		
		try {
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			pstmt = conn.prepareCall(query);
			pstmt.setLong(1,Long.parseLong(erExcdId));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				moreInfoMap.put("TESTING_SITE_CODE", rs.getString("TESTING_SITE_CODE") != null ? rs.getString("TESTING_SITE_CODE") : "");
				moreInfoMap.put("TESTING_SITE_NAME", rs.getString("TESTING_SITE_NAME") != null ? rs.getString("TESTING_SITE_NAME") : "");
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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
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
	
	private StringBuffer getERQueryForDate(StringBuffer queryBuff){
		queryBuff.append("SELECT DISTINCT ESSH.LASTNAME || ', ' || ESSH.FIRSTNAME || ' ' || ESSH.MIDDLENAME STUDENTNAME,")
		.append(" ESSH.UUID UUID,")
		.append(" TO_CHAR(NVL((SELECT EED.TEST_ELEMENT_ID FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID), 'NA')) TEST_ELEMENT_ID,")
		.append(" NVL(TO_CHAR((SELECT EED.PROCESS_ID FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID)), 'NA') PROCESS_ID,")
		.append(" TO_CHAR(NVL((SELECT EED.EXCEPTION_CODE FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID), 'NA')) EXCEPTION_CODE,")
		.append(" NVL((SELECT EED.SOURCE_SYSTEM FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID), 'ERESOURCE') SOURCE_SYSTEM,")
		.append(" NVL((SELECT EED.EXCEPTION_STATUS FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID), 'CO') EXCEPTION_STATUS,")
		.append(" ESSH.ER_SS_HISTID ER_SS_HISTID, ESSH.BARCODE BARCODE, ESSH.DATE_SCHEDULED DATE_SCHEDULED, ESSH.STATE_CODE STATE_CODE, ESSH.FORM FORM,ESSH.DATETIMESTAMP,")
		.append(" NVL((SELECT EED.ER_EXCDID FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID), 0) ER_EXCDID,")
		.append(" (SELECT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTEST_CODE = ESSH.CONTENT_AREA_CODE) SUBTEST,")
		.append(" ESSH.TESTCENTERCODE TESTING_SITE_CODE, ESSH.TESTCENTERNAME TESTING_SITE_NAME, ESSH.CTB_CUSTOMER_ID CTB_CUSTOMER_ID, ESSH.STATENAME STATENAME,")
		.append(" ESSH.DATEOFBIRTH DATEOFBIRTH, ESSH.GENDER GENDER, ESSH.GOVERNMENTID GOVERNMENTID, ESSH.GOVERNMENTIDTYPE GOVERNMENTIDTYPE, ESSH.ADDRESS1 ADDRESS1,")
		.append(" ESSH.CITY CITY, ESSH.COUNTY COUNTY, ESSH.STATE STATE, ESSH.ZIP ZIP, ESSH.EMAIL EMAIL, ESSH.ALTERNATEEMAIL ALTERNATEEMAIL, ESSH.PRIMARYPHONENUMBER PRIMARYPHONENUMBER,")
		.append(" ESSH.CELLPHONENUMBER CELLPHONENUMBER, ESSH.ALTERNATENUMBER ALTERNATENUMBER, ESSH.RESOLVED_ETHNICITY_RACE RESOLVED_ETHNICITY_RACE, ESSH.HOMELANGUAGE HOMELANGUAGE,")
		.append(" ESSH.EDUCATIONLEVEL EDUCATIONLEVEL, ESSH.ATTENDCOLLEGE ATTENDCOLLEGE, ESSH.CONTACT CONTACT, ESSH.EXAMINEECOUNTYPARISHCODE EXAMINEECOUNTYPARISHCODE,")
		.append(" ESSH.REGISTEREDON REGISTEREDON, ESSH.REGISTEREDATTESTCENTER REGISTEREDATTESTCENTER, ESSH.REGISTEREDATTESTCENTERCODE REGISTEREDATTESTCENTERCODE, ESSH.SCHEDULE_ID SCHEDULE_ID,")
		.append(" ESSH.TIMEOFDAY TIMEOFDAY, ESSH.DATECHECKEDIN DATECHECKEDIN, ESSH.CONTENT_TEST_TYPE CONTENT_TEST_TYPE, ESSH.CONTENT_TEST_CODE CONTENT_TEST_CODE, ESSH.TASCREADINESS TASCREADINESS,")
		.append(" ESSH.ECC ECC, ESSH.REGST_TC_COUNTYPARISHCODE REGST_TC_COUNTYPARISHCODE, ESSH.SCHED_TC_COUNTYPARISHCODE SCHED_TC_COUNTYPARISHCODE,")
		.append(" DECODE(NVL((SELECT EED.ER_EXCDID FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID), 0),0,'', 'ERROR CODE-' || (SELECT EED.EXCEPTION_CODE FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID) || ': ' ||")
		.append(" (SELECT EED.DESCRIPTION FROM ER_EXCEPTION_DATA EED WHERE EED.ER_SS_HISTID = ESSH.ER_SS_HISTID)) ERROR_DESCRIPTION, TO_CHAR(ESSH.DATETIMESTAMP, 'MM/DD/YYYY  HH:mm:ss') PROCESSED_DATE")
		.append(" FROM ER_STUDENT_SCHED_HISTORY ESSH")
		.append(" WHERE TRUNC(ESSH.DATETIMESTAMP) >= TO_DATE(?, 'MM/DD/YYYY')")
		.append(" AND TRUNC(ESSH.DATETIMESTAMP) <= TO_DATE(?, 'MM/DD/YYYY')")
		.append(" ORDER BY ESSH.DATETIMESTAMP DESC, STUDENTNAME");
		return queryBuff;
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
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			int count = 0;
			int placeHolderTotalRecCount = 0;
			int placeHolderData = 0;
			int placeHolderErrorMsg = 0;
			String query = "";
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				query = "{call PKG_FILE_TRACKING_1.SP_GET_DATA_ER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}else{
				query = "{call PKG_FILE_TRACKING_1.SP_GET_DATA_OL_PP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}
			cs = conn.prepareCall(query);
			
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
			cs.registerOutParameter(placeHolderTotalRecCount, OracleTypes.CURSOR);
			placeHolderData = ++count;
			cs.registerOutParameter(placeHolderData, OracleTypes.CURSOR);
			placeHolderErrorMsg = ++count;
			cs.registerOutParameter(placeHolderErrorMsg, OracleTypes.VARCHAR);
			
			cs.execute();
			String errorMessage = cs.getString(placeHolderErrorMsg);
			if (errorMessage == null || errorMessage.isEmpty()) {
				rs = (ResultSet) cs.getObject(placeHolderData);
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
		ResultSet rs = null;
		long totalRecordCount = 0;
		
		try {
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			int count = 0;
			int placeHolderTotalRecCount = 0;
			int placeHolderData = 0;
			int placeHolderErrorMsg = 0;
			String query = "";
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
				query = "{call PKG_FILE_TRACKING_1.SP_GET_DATA_ER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}else{
				query = "{call PKG_FILE_TRACKING_1.SP_GET_DATA_OL_PP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}
			System.out.println("query: "+query);
			cs = conn.prepareCall(query);
			
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
			cs.registerOutParameter(placeHolderTotalRecCount, OracleTypes.CURSOR);
			placeHolderData = ++count;
			cs.registerOutParameter(placeHolderData, OracleTypes.CURSOR);
			placeHolderErrorMsg = ++count;
			cs.registerOutParameter(placeHolderErrorMsg, OracleTypes.VARCHAR);
			
			cs.execute();
			String errorMessage = cs.getString(placeHolderErrorMsg);
			if (errorMessage == null || errorMessage.isEmpty()) {
				rs = (ResultSet) cs.getObject(placeHolderTotalRecCount);
				if(rs.next()) {
					totalRecordCount = rs.getLong("TOTAL_RECORD_COUNT");
				}
			}else{
				System.out.println("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			System.out.println("Exit: getTotalRecord() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return totalRecordCount;
	}
	
}
