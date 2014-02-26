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
		queryBuff.append("OBJECTIVE_VALIDATION,ITEM_VALIDATION,WKF_PARTITION_NAME,DATETIMESTAMP, ");
		queryBuff.append("to_number(datetimestamp - to_date('01-JAN-1970','DD-MON-YYYY')) * (24 * 60 * 60 * 1000) long_time ");
		queryBuff.append("from stg_process_status ");
		if(searchProcess != null) {
			queryBuff.append(" WHERE 1 = 1 ");
			if(searchProcess.getCreatedDate() != null && searchProcess.getCreatedDate().trim().length() > 0
					&& searchProcess.getUpdatedDate() != null & searchProcess.getUpdatedDate().trim().length() > 0) {
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
						&& searchProcess.getUpdatedDate() != null & searchProcess.getUpdatedDate().trim().length() > 0) {
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
				processTO.setTimeMili(rs.getString(12));
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
	
	
	
}
