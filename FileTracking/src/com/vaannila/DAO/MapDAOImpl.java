package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaannila.TO.SearchProcess;
import com.vaannila.TO.StudentDetailsTO;
import com.vaannila.TO.TASCProcessTO;
import com.vaannila.util.JDCConnectionDriver;


public class MapDAOImpl {
	static JDCConnectionDriver driver = null;
	static String DATA_SOURCE = "jdbc:jdc:usmo";
	
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
		queryBuff.append("select TASK_ID, PROCESS_ID,FILE_NAME,HIER_VALIDATION,BIO_VALIDATION,DEMO_VALIDATION,CONTENT_VALIDATION,");
		queryBuff.append("OBJECTIVE_VALIDATION,ITEM_VALIDATION,WKF_PARTITION_NAME,DATETIMESTAMP,(SELECT getMapStatus(TASK_ID) FROM DUAL) STATUS ");
		//queryBuff.append(",to_number(datetimestamp - to_date('01-JAN-1970','DD-MON-YYYY')) * (24 * 60 * 60 * 1000) long_time ");
		queryBuff.append("from stg_task_status ");
		// queryBuff.append(" WHERE rownum<10 ");
		if(searchProcess != null) {
			queryBuff.append(" WHERE 1 = 1 ");
			if(searchProcess.getCreatedDate() != null && searchProcess.getCreatedDate().trim().length() > 0
					&& searchProcess.getUpdatedDate() != null && searchProcess.getUpdatedDate().trim().length() > 0) {
				queryBuff.append("AND (DATETIMESTAMP between to_date(?, 'MM/DD/YYYY') and to_date(?, 'MM/DD/YYYY')+1) ");
			}
			
		}
		queryBuff.append(" order by PROCESS_ID desc ");
		String query = queryBuff.toString();
		// System.out.println(query);
		try {
			driver = MAPConnectionProvider.getDriver();
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
				processTO.setTaskId(rs.getString("TASK_ID"));
				processTO.setProcessId(rs.getString("PROCESS_ID"));
				processTO.setFileName(rs.getString("FILE_NAME"));
				processTO.setHierValidation(rs.getString("HIER_VALIDATION"));
				processTO.setBioValidation(rs.getString("BIO_VALIDATION"));
				processTO.setDemoValidation(rs.getString("DEMO_VALIDATION"));
				processTO.setContentValidation(rs.getString("CONTENT_VALIDATION"));
				processTO.setObjValidation(rs.getString("OBJECTIVE_VALIDATION"));
				processTO.setItemValidation(rs.getString("ITEM_VALIDATION"));
				processTO.setWkfPartitionName(rs.getString("WKF_PARTITION_NAME"));
				processTO.setDateTimestamp(rs.getString("DATETIMESTAMP"));
				processTO.setOverallStatus(rs.getString("STATUS"));
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
	public String getProcessLog(String taskId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String processLog = "";
		try {
			driver = MAPConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "SELECT U.TASK_LOG FROM stg_task_status U WHERE U.TASK_ID = ?";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, taskId);
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
	 * Get all students that have error
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public List<StudentDetailsTO> getStudentDetails(String taskId) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<StudentDetailsTO> students = new ArrayList<StudentDetailsTO>();
		try {
			driver = MAPConnectionProvider.getDriver();
			conn = driver.connect(DATA_SOURCE, null);
			
			String query = "select district_code, school_code, grade, drc_student_id, '(' || suffix || ') ' || last_name || ', ' || first_name || ' ' || middle_name as STUDENT_NAME, " +
						" birth_date, gender, test_date, content_area_title, " +
						" scale_score, err_code, log from student_data_extract_err where validation_flag = 'N' and task_id = ? ";
			pstmt = conn.prepareCall(query);
			pstmt.setString(1, taskId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				StudentDetailsTO student = new StudentDetailsTO();
				student.setStudentName(rs.getString("STUDENT_NAME"));
				student.setDob(rs.getString("birth_date"));
				student.setGender(rs.getString("gender"));
				student.setGrade(rs.getString("grade"));
				student.setDistrict(rs.getString("district_code"));
				student.setSchool(rs.getString("school_code"));
				student.setDrcStudentId(rs.getString("drc_student_id"));
				student.setTestDate(rs.getString("test_date"));
				student.setContantArea(rs.getString("content_area_title"));
				student.setErrorCode(rs.getString("err_code"));
				student.setErrorLog(rs.getString("log"));
				
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {rs.close();} catch (Exception e2) {}
			try {pstmt.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
		}
		return students;
	}
	
	
	
}
