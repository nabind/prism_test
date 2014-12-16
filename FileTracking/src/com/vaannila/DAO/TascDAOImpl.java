package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.TASCProcessTO;
import com.vaannila.util.JDCConnectionDriver;


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
	 * Method to update the process log
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
								" FROM STG_PROCESS_STATUS),'MM-DD-YYYY'), 'MM-DD-YYYY')) BB WHERE PP.D(+) = BB.D and OL.D(+) = BB.D AND ERR_COUNT.D(+) = BB.D AND COM_COUNT.D(+) = BB.D ORDER BY BB.D";  
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
	 * @author 541841
	 * Get Searched records
	 * @throws Exception
	 */
	public List<TASCProcessTO> getProcessEr(SearchProcess searchProcess) throws Exception {
		System.out.println("Enter: getProcessEr()");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TASCProcessTO processTO = null;
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		StringBuffer queryBuff = new StringBuffer();
		
		if("ERESOURCE".equals(searchProcess.getSourceSystem())){
			queryBuff.append("SELECT DISTINCT ESSH.LASTNAME || ', ' || ESSH.FIRSTNAME || ' ' || ESSH.MIDDLENAME STUDENTNAME,");
			queryBuff.append(" ESSH.UUID UUID,");
			queryBuff.append(" SD.SUBTEST_NAME SUBTEST_NAME,");
			queryBuff.append(" NVL(EED.TEST_ELEMENT_ID, 0) TEST_ELEMENT_ID,");
			queryBuff.append(" NVL(EED.PROCESS_ID, 0) PROCESS_ID,");
			queryBuff.append(" NVL(EED.EXCEPTION_CODE, '0') EXCEPTION_CODE,");
			queryBuff.append(" NVL(EED.SOURCE_SYSTEM, 'NA') SOURCE_SYSTEM,");
			queryBuff.append(" NVL(EED.EXCEPTION_STATUS, 'NA') EXCEPTION_STATUS,");
			queryBuff.append(" ESSH.ER_SS_HISTID ER_SS_HISTID,");
			queryBuff.append(" ESSH.BARCODE BARCODE,");
			queryBuff.append(" TO_CHAR(TO_DATE(ESSH.DATE_SCHEDULED,'YYYYMMDDHHMISS'),'MM/DD/YYYY') DATE_SCHEDULED,");
			queryBuff.append(" ESSH.STATE_CODE STATE_CODE,");
			queryBuff.append(" ESSH.FORM FORM,");
			queryBuff.append(" ESSH.DATETIMESTAMP,");
			queryBuff.append(" EED.ER_EXCDID ER_EXCDID");
			queryBuff.append(" FROM ER_STUDENT_SCHED_HISTORY ESSH, SUBTEST_DIM SD, ER_EXCEPTION_DATA EED");
			queryBuff.append(" WHERE ESSH.CONTENT_AREA_CODE = SD.SUBTEST_CODE");
			queryBuff.append(" AND ESSH.ER_SS_HISTID = EED.ER_SS_HISTID");
			queryBuff.append(" AND EED.SOURCE_SYSTEM = ?");
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				queryBuff.append(" AND ESSH.DATETIMESTAMP >= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				queryBuff.append(" AND ESSH.DATETIMESTAMP <= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				queryBuff.append(" AND ESSH.UUID LIKE ?");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				queryBuff.append(" AND UPPER(ESSH.LASTNAME) LIKE UPPER(?)");
			}
			if(!"ALL".equals(searchProcess.getSubjectCa())){
				queryBuff.append("  AND SD.SUBTEST_CODE = ?");
			}
			if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
				queryBuff.append(" AND EED.EXCEPTION_CODE = ?");
			}
			if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
				queryBuff.append(" AND ESSH.ER_SS_HISTID = ?");
			}
			queryBuff.append(" ORDER BY ESSH.DATETIMESTAMP DESC, STUDENTNAME, SD.SUBTEST_NAME");
		}else{
			queryBuff.append(" SELECT ESD.LASTNAME || ', ' || ESD.FIRSTNAME || ' ' || ESD.MIDDLENAME STUDENTNAME,");
			queryBuff.append(" ESD.UUID UUID,");
			queryBuff.append(" SD.SUBTEST_NAME SUBTEST_NAME,");
			queryBuff.append(" TO_CHAR(NVL(EED.TEST_ELEMENT_ID, 0)) TEST_ELEMENT_ID,");
			queryBuff.append(" NVL(EED.PROCESS_ID, 0) PROCESS_ID,");
			queryBuff.append(" NVL(EED.EXCEPTION_CODE, '0') EXCEPTION_CODE,");
			queryBuff.append(" NVL(EED.SOURCE_SYSTEM, 'NA') SOURCE_SYSTEM,");
			queryBuff.append(" NVL(EED.EXCEPTION_STATUS, 'NA') EXCEPTION_STATUS,");
			queryBuff.append(" NVL(EED.ER_SS_HISTID, 0) ER_SS_HISTID,");
			queryBuff.append(" NVL(ETS.BARCODE,'NA') BARCODE,");
			queryBuff.append(" TO_CHAR(ETS.DATE_SCHEDULED, 'MM/DD/YYYY') DATE_SCHEDULED,");
			queryBuff.append(" ESD.STATE_CODE STATE_CODE,");
			queryBuff.append(" ETS.FORM FORM,");
			queryBuff.append(" EED.CREATED_DATE_TIME DATETIMESTAMP,");
			queryBuff.append(" NVL(EED.ER_EXCDID,0) ER_EXCDID");
			queryBuff.append(" FROM ER_EXCEPTION_DATA EED,");
			queryBuff.append(" ER_STUDENT_DEMO   ESD,");
			queryBuff.append(" ER_TEST_SCHEDULE  ETS,");
			queryBuff.append(" SUBTEST_DIM       SD");
			queryBuff.append(" WHERE EED.ER_UUID = ESD.UUID");
			queryBuff.append(" AND ESD.ER_STUDID = ETS.ER_STUDID");
			queryBuff.append(" AND ETS.CONTENT_AREA_CODE = SD.SUBTEST_CODE");
			queryBuff.append(" AND EED.SOURCE_SYSTEM = ?");
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				queryBuff.append(" AND EED.CREATED_DATE_TIME >= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				queryBuff.append(" AND EED.CREATED_DATE_TIME <= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				queryBuff.append(" AND ESD.UUID LIKE ?");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				queryBuff.append(" AND UPPER(ESD.LASTNAME) LIKE UPPER(?)");
			}
			if(!"ALL".equals(searchProcess.getSubjectCa())){
				queryBuff.append("  AND SD.SUBTEST_CODE = ?");
			}
			if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
				queryBuff.append(" AND EED.EXCEPTION_CODE = ?");
			}
			if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
				queryBuff.append(" AND EED.ER_SS_HISTID = ?");
			}

			queryBuff.append(" UNION");
			
			queryBuff.append(" SELECT SSBD.LAST_NAME || ', ' || SSBD.FIRST_NAME || ' ' || SSBD.MIDDLE_NAME STUDENTNAME,");
			queryBuff.append(" SSBD.EXT_STUDENT_ID UUID,");
			queryBuff.append(" SD.SUBTEST_NAME SUBTEST_NAME,");
			queryBuff.append(" NVL(SSBD.TEST_ELEMENT_ID, 0) TEST_ELEMENT_ID,");
			queryBuff.append(" NVL(SPS.PROCESS_ID, 0) PROCESS_ID,");
			queryBuff.append(" '0' EXCEPTION_CODE,");
			queryBuff.append(" NVL(SPS.SOURCE_SYSTEM, 'NA') SOURCE_SYSTEM,");
			queryBuff.append(" 'NA' EXCEPTION_STATUS,");
			queryBuff.append(" 0 ER_SS_HISTID,");
			queryBuff.append(" NVL(SSBD.BARCODE,'NA') BARCODE,");
			queryBuff.append(" TO_CHAR(SSSD.DATE_TEST_TAKEN, 'MM/DD/YYYY') DATE_SCHEDULED,");
			queryBuff.append(" SHD.ORG_CODE STATE_CODE,");
			queryBuff.append(" SSSD.TEST_FORM FORM,");
			queryBuff.append(" SPS.DATETIMESTAMP DATETIMESTAMP,");
			queryBuff.append(" 0 ER_EXCDID");
			queryBuff.append(" FROM STG_STD_BIO_DETAILS     SSBD,");
			queryBuff.append(" STG_STD_SUBTEST_DETAILS SSSD,");
			queryBuff.append(" STG_HIER_DETAILS        SHD,");
			queryBuff.append(" STG_PROCESS_STATUS      SPS,");
			queryBuff.append(" SUBTEST_DIM             SD");
			queryBuff.append(" WHERE SPS.PROCESS_ID = SSBD.PROCESS_ID");
			queryBuff.append(" AND SPS.PROCESS_ID = SHD.PROCESS_ID");
			queryBuff.append(" AND SSBD.STUDENT_BIO_DETAILS_ID = SSSD.STUDENT_BIO_DETAILS_ID");
			queryBuff.append(" AND SSSD.CONTENT_NAME = SD.SUBTEST_CODE");
			queryBuff.append(" AND SSBD.WKF_PARTITION_NAME = 'BR_EXCP'");
			queryBuff.append(" AND SSBD.WKF_PARTITION_NAME = SHD.WKF_PARTITION_NAME");
			queryBuff.append(" AND SHD.ORG_LEVEL = 1");
			queryBuff.append(" AND SPS.SOURCE_SYSTEM = ?");
			if(searchProcess.getProcessedDateFrom() != null && searchProcess.getProcessedDateFrom().trim().length() > 0){
				queryBuff.append(" AND SPS.DATETIMESTAMP >= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getProcessedDateTo() != null && searchProcess.getProcessedDateTo().trim().length() > 0){
				queryBuff.append(" AND SPS.DATETIMESTAMP <= TO_DATE(?, 'MM/DD/YYYY')");
			}
			if(searchProcess.getUuid() != null && searchProcess.getUuid().trim().length() > 0){
				queryBuff.append(" AND SSBD.EXT_STUDENT_ID LIKE ?");
			}
			if(searchProcess.getLastName() != null && searchProcess.getLastName().trim().length() > 0){
				queryBuff.append(" AND UPPER(SSBD.LAST_NAME) LIKE UPPER(?)");
			}
			if(!"ALL".equals(searchProcess.getSubjectCa())){
				queryBuff.append("  AND SD.SUBTEST_CODE = ?");
			}
			queryBuff.append(" ORDER BY DATETIMESTAMP DESC, STUDENTNAME, SUBTEST_NAME");
		}
			
		String query = queryBuff.toString();
		System.out.println(query);
		try {
			driver = TASCConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			int count = 0;
			pstmt = conn.prepareCall(query);
			
			if("ERESOURCE".equals(searchProcess.getSourceSystem())){
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
				if(!"ALL".equals(searchProcess.getSubjectCa())){
					pstmt.setString(++count, searchProcess.getSubjectCa());
				}
				if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getExceptionCode()));
				}
				if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getRecordId()));
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
				if(!"ALL".equals(searchProcess.getSubjectCa())){
					pstmt.setString(++count, searchProcess.getSubjectCa());
				}
				if(searchProcess.getExceptionCode() != null && searchProcess.getExceptionCode().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getExceptionCode()));
				}
				if(searchProcess.getRecordId() != null && searchProcess.getRecordId().trim().length() > 0){
					pstmt.setLong(++count, Long.parseLong(searchProcess.getRecordId()));
				}
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
				if(!"ALL".equals(searchProcess.getSubjectCa())){
					pstmt.setString(++count, searchProcess.getSubjectCa());
				}
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				processTO = new TASCProcessTO();
				processTO.setStudentName(rs.getString("STUDENTNAME"));
				processTO.setUuid(rs.getString("UUID"));
				processTO.setSubtestName(rs.getString("SUBTEST_NAME"));
				processTO.setTestElementId(rs.getString("TEST_ELEMENT_ID"));
				processTO.setProcessId(rs.getString("PROCESS_ID"));
				processTO.setExceptionCode(rs.getString("EXCEPTION_CODE"));
				processTO.setSourceSystem(rs.getString("SOURCE_SYSTEM"));
				processTO.setOverallStatus(rs.getString("EXCEPTION_STATUS"));
				processTO.setErSsHistId(rs.getString("ER_SS_HISTID"));
				processTO.setBarcode(rs.getString("BARCODE"));
				processTO.setDateScheduled(rs.getString("DATE_SCHEDULED"));
				processTO.setStateCode(rs.getString("STATE_CODE"));
				processTO.setForm(rs.getString("FORM"));
				processTO.setErExcdId(rs.getString("ER_EXCDID"));
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
		System.out.println("Exit: getProcessEr()");
		return processList;
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
	
}
