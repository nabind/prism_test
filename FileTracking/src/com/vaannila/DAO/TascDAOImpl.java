package com.vaannila.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TASCProcessTO processTO = null;
		List<TASCProcessTO> processList = new ArrayList<TASCProcessTO>();
		StringBuffer queryBuff = new StringBuffer();
		queryBuff.append("select PROCESS_ID,FILE_NAME,SOURCE_SYSTEM,HIER_VALIDATION,BIO_VALIDATION,DEMO_VALIDATION,CONTENT_VALIDATION,");
		queryBuff.append("OBJECTIVE_VALIDATION,ITEM_VALIDATION,WKF_PARTITION_NAME,DATETIMESTAMP,(SELECT GETSTATUS(PROCESS_ID) FROM DUAL) STSTUS ");
		//queryBuff.append(",to_number(datetimestamp - to_date('01-JAN-1970','DD-MON-YYYY')) * (24 * 60 * 60 * 1000) long_time ");
		queryBuff.append("from stg_process_status ");
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
				processTO.setProcessId(rs.getString(1));
				processTO.setFileName(rs.getString(2));
				processTO.setSourceSystem(rs.getString(3));
				processTO.setHierValidation(rs.getString(4));
				processTO.setBioValidation(rs.getString(5));
				processTO.setDemoValidation(rs.getString(6));
				processTO.setContentValidation(rs.getString(7));
				processTO.setObjValidation(rs.getString(8));
				processTO.setItemValidation(rs.getString(9));
				processTO.setWkfPartitionName(rs.getString(10));
				processTO.setDateTimestamp(rs.getString(11));
				processTO.setOverallStatus(rs.getString(12));
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
			
			String query = "SELECT u.PROCESS_LOG FROM stg_process_status u " +
						"WHERE u.PROCESS_ID = ?";
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
	
	
	
}
