package com.vaannila.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;

import com.vaannila.TO.StudentDetailsTO;
import com.vaannila.util.JDCConnectionDriver;

public class SupportDAOImpl {
	
	static JDCConnectionDriver driver = null;
	static String TASC_DATA_SOURCE = "jdbc:jdc:tasc";
	private static final Logger logger = Logger.getLogger(SupportDAOImpl.class);
	
	/**
	 * @author Abir
	 * Unlock the schedule for selected sub-test of a specific student
	 * @throws Exception
	 */
	
	public String unlockStudnet(StudentDetailsTO studentTO, boolean undo) throws Exception {
		logger.info("Enter: unlockStudnet()");
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
			
			logger.info("unlock query: "+query);
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
				logger.error("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			logger.info("Exit: unlockStudnet() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
	/**
	 * @author Abir
	 * Invalidate the schedule for selected sub-test of a specific student
	 * @throws Exception
	 */
	
	public String invalidateSch(StudentDetailsTO studentTO, boolean undo) throws Exception {
		logger.info("Enter: invalidateSch()");
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
			
			logger.info("invalidate schedule query: "+query);
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
				logger.error("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			logger.info("Exit: invalidateSch() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
	/**
	 * @author Abir
	 * Invalidate the student for selected sub-test of a specific student
	 * @throws Exception
	 */
	
	public String invalidate(StudentDetailsTO studentTO, boolean undo) throws Exception {
		logger.info("Enter: invalidate()");
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
			
			logger.info("invalidate student query: "+query);
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
				logger.error("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			logger.info("Exit: invalidate() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
	
	/**
	 * @author Abir
	 * delete the student
	 * @throws Exception
	 */
	
	public String deleteStudent(StudentDetailsTO studentTO) throws Exception {
		logger.info("Enter: deleteStudent()");
		long t1 = System.currentTimeMillis();
		Connection conn = null;
		CallableStatement cs = null;
		String errorMessage = null;
		try {
			conn = BaseDAO.connect(TASC_DATA_SOURCE);
			int count = 0;
	
			String query = "{call PKG_FILE_TRACKING_SUPPORT.SP_DELETE_STUDENT(?,?)}";
						
			logger.info("delete student query: "+query);
			cs = conn.prepareCall(query);
			
			cs.setString(++count, studentTO.getStudentBioId());
			cs.registerOutParameter(++count, OracleTypes.VARCHAR);
			
			cs.execute();
			errorMessage = cs.getString(count);
			
			if (errorMessage == null || errorMessage.isEmpty()) {
				errorMessage = "Student has been deleted";
			}else{
				logger.error("errorMessage: "+errorMessage);
				throw new Exception(errorMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {cs.close();} catch (Exception e2) {}
			try {conn.close();} catch (Exception e2) {}
			long t2 = System.currentTimeMillis();
			logger.info("Exit: deleteStudent() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return errorMessage;
	}
	
}
