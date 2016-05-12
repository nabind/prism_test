package com.vaannila.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

import com.vaannila.TO.StudentDetailsTO;
import com.vaannila.util.JDCConnectionDriver;

public class SupportDAOImpl {
	
	static JDCConnectionDriver driver = null;
	static String TASC_DATA_SOURCE = "jdbc:jdc:tasc";

	/**
	 * @author Abir
	 * Unlock the schedule for selected sub-test of a specific student
	 * @throws Exception
	 */
	
	public String unlockStudnet(StudentDetailsTO studentTO, boolean undo) throws Exception {
		System.out.println("Enter: unlockStudnet()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		String errorMessage = null;
		try {
			conn = BaseDAO.connect(TASC_DATA_SOURCE);
			int count = 0;
	
			String query = "";
			if(!undo)
				query = "{call PKG_FILE_TRACKING_SUPPORT.SP_UNLOCK_STUDENT_SCHEDULE(?,?,?,?)}";
			else
				query = "{call PKG_FILE_TRACKING_SUPPORT.SP_UNLOCK_STUDENT_SCHE_UNDO(?,?,?,?)}";
			
			System.out.println("unlock query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, studentTO.getUuid());
			cs.setString(++count, studentTO.getStateCode());
			cs.setLong(++count, Long.valueOf(studentTO.getScheduleId()));
			cs.registerOutParameter(++count, OracleTypes.VARCHAR);
			
			cs.execute();
			errorMessage = cs.getString(count);
			
			if (errorMessage == null || errorMessage.isEmpty()) {
				errorMessage = "Student has been unlocked";
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
			System.out.println("Exit: unlockStudnet() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
	/**
	 * @author Abir
	 * Invalidate the schedule for selected sub-test of a specific student
	 * @throws Exception
	 */
	
	public String invalidateSch(StudentDetailsTO studentTO, boolean undo) throws Exception {
		System.out.println("Enter: invalidateSch()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		String errorMessage = null;
		try {
			conn = BaseDAO.connect(TASC_DATA_SOURCE);
			int count = 0;
	
			String query = "";
			if(!undo)
				query = "{call PKG_FILE_TRACKING_SUPPORT.SP_INVALIDATE_SCHEDULE(?,?,?,?)}";
			else
				query = "{call PKG_FILE_TRACKING_SUPPORT.SP_INVALIDATE_SCHEDULE_UNDO(?,?,?,?)}";
			
			System.out.println("invalidate schedule query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, studentTO.getUuid());
			cs.setString(++count, studentTO.getStateCode());
			cs.setLong(++count, Long.valueOf(studentTO.getErTestSchId()));
			cs.registerOutParameter(++count, OracleTypes.VARCHAR);
			
			cs.execute();
			errorMessage = cs.getString(count);
			
			if (errorMessage == null || errorMessage.isEmpty()) {
				errorMessage = "Student scheduled has been invalidated";
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
			System.out.println("Exit: invalidateSch() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
	/**
	 * @author Abir
	 * Invalidate the student for selected sub-test of a specific student
	 * @throws Exception
	 */
	
	public String invalidate(StudentDetailsTO studentTO, boolean undo) throws Exception {
		System.out.println("Enter: invalidate()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		String errorMessage = null;
		try {
			conn = BaseDAO.connect(TASC_DATA_SOURCE);
			int count = 0;
	
			String query = "";
			if(!undo)
				query = "{call PKG_FILE_TRACKING_SUPPORT.SP_INVALIDATE_STUDENT(?,?,?,?)}";
			else
				query = "{call PKG_FILE_TRACKING_SUPPORT.SP_INVALIDATE_STUDENT_UNDO(?,?,?,?)}";
			
			System.out.println("invalidate student query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, studentTO.getUuid());
			cs.setString(++count, studentTO.getStateCode());
			cs.setLong(++count, Long.valueOf(studentTO.getErTestSchId()));
			cs.registerOutParameter(++count, OracleTypes.VARCHAR);
			
			cs.execute();
			errorMessage = cs.getString(count);
			
			if (errorMessage == null || errorMessage.isEmpty()) {
				errorMessage = "Student has been invalidated";
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
			System.out.println("Exit: invalidate() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
	
	/**
	 * @author Abir
	 * delete the student
	 * @throws Exception
	 */
	
	public String deleteStudent(StudentDetailsTO studentTO) throws Exception {
		System.out.println("Enter: deleteStudent()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		String errorMessage = null;
		try {
			conn = BaseDAO.connect(TASC_DATA_SOURCE);
			int count = 0;
	
			String query = "{call PKG_FILE_TRACKING_SUPPORT.SP_DELETE_STUDENT(?,?)}";
						
			System.out.println("delete student query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, studentTO.getStudentBioId());
			cs.registerOutParameter(++count, OracleTypes.VARCHAR);
			
			cs.execute();
			errorMessage = cs.getString(count);
			
			if (errorMessage == null || errorMessage.isEmpty()) {
				errorMessage = "Student has been deleted";
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
			System.out.println("Exit: deleteStudent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
}
