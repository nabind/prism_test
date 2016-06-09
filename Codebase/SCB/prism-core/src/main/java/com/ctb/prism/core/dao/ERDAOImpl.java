package com.ctb.prism.core.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.webservice.erTransferobject.BioDetails;
import com.ctb.prism.webservice.erTransferobject.DemoDetails;
import com.ctb.prism.webservice.erTransferobject.OrgDetails;
import com.ctb.prism.webservice.erTransferobject.ScheduleDetails;
import com.ctb.prism.webservice.erTransferobject.StudentDetails;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;

@Repository
public class ERDAOImpl extends BaseDAO implements IERDAO {
	

	private static final IAppLogger logger = LogFactory.getLoggerInstance(ERDAOImpl.class.getName());

	/**
	 * This method is to store each ER request into history table
	 */
	public StudentDataLoadTO saveERHistory(final StudentDetails studentDetails, 
			StudentDataLoadTO studentDataLoadTO, final ScheduleDetails scheduleDetails) {
		final StudentDataLoadTO dataLoadTO = studentDataLoadTO;
		long historyId = (Long) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			int count =1;
			//String salt = PasswordGenerator.getNextSalt();
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				BioDetails bioDetails = studentDetails.getBioDetails();
				DemoDetails demoDetails = studentDetails.getDemoDetails();
				OrgDetails orgDetails = studentDetails.getOrgDetails();
				
				CallableStatement cs = con.prepareCall("{call " +IQueryConstants.CREATE_SP_HISTORY + "}");
				cs.setString(count++, studentDetails.getUUID());
				cs.setLong(count++, toLong(orgDetails.getcTBCustomerID()));
				cs.setString(count++, orgDetails.getStateCode());
				cs.setString(count++, orgDetails.getStateName());
				cs.setString(count++, bioDetails.getFirstName());
				cs.setString(count++, bioDetails.getMiddleName());
				cs.setString(count++, bioDetails.getLastName());
				cs.setString(count++, bioDetails.getDateOfBirth());
				cs.setString(count++, bioDetails.getGender());
				cs.setString(count++, demoDetails.getGovernmentID());
				cs.setString(count++, demoDetails.getGovernmentIDType());
				cs.setString(count++, demoDetails.getAddress1());
				cs.setString(count++, demoDetails.getAddress2());
				cs.setString(count++, demoDetails.getCity());
				cs.setString(count++, demoDetails.getCounty());
				cs.setString(count++, demoDetails.getState());
				cs.setString(count++, demoDetails.getZip());
				cs.setString(count++, demoDetails.getEmail());
				cs.setString(count++, demoDetails.getAlternateEmail());
				cs.setString(count++, demoDetails.getPrimaryPhoneNumber());
				cs.setString(count++, demoDetails.getCellPhoneNumber());
				cs.setString(count++, demoDetails.getAlternateNumber());
				cs.setString(count++, demoDetails.getResolvedEthnicityRace());
				cs.setString(count++, demoDetails.getHomeLanguage());
				cs.setString(count++, demoDetails.getEducationLevel());
				cs.setString(count++, demoDetails.getAttendCollege());
				cs.setString(count++, demoDetails.getContact());
				cs.setString(count++, demoDetails.getExamineeCcountyParishCode());
				cs.setString(count++, demoDetails.getRegisteredOn());
				cs.setString(count++, demoDetails.getRegisteredAtTestCenter());
				cs.setString(count++, demoDetails.getRegisteredAtTestCenterCode());
				cs.setString(count++, demoDetails.getRegisteredAtTestCenterCountyParishCode());
				cs.setString(count++, scheduleDetails.getScheduleID());
				cs.setString(count++, scheduleDetails.getDateScheduled());
				cs.setString(count++, scheduleDetails.getTimeOfDay());
				cs.setString(count++, scheduleDetails.getDateCheckedIn());
				cs.setString(count++, scheduleDetails.getContentAreaCode());
				cs.setString(count++, scheduleDetails.getContentTestType());
				cs.setString(count++, scheduleDetails.getContentTestCode());
				cs.setString(count++, scheduleDetails.getBarcode());
				cs.setString(count++, scheduleDetails.getForm());
				cs.setString(count++, scheduleDetails.gettASCReadiness());
				cs.setString(count++, scheduleDetails.getECC());
				cs.setString(count++, scheduleDetails.getTestCenterCode());
				cs.setString(count++, scheduleDetails.getTestCenterName());
				cs.setString(count++, scheduleDetails.getScheduledAtTestCenterCountyParishCode());
				
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				long historyId = 0; 
				try {
					cs.execute();
					historyId = cs.getLong(47);
					logger.log(IAppLogger.DEBUG, "ER HISTORY INSERT DONE");
					logger.log(IAppLogger.INFO, "ER history created with history id: " + historyId);
				} catch (SQLException e) {
					dataLoadTO.setSuccess(false);
					dataLoadTO.setSummary("ERR:900" + " - " + e.getMessage());
					e.printStackTrace();
				}
				return historyId;
			}
		});
		dataLoadTO.setObjectiveDetailsId(historyId);
		return dataLoadTO;
	}
	
	private Date toDate(String date) {
		String dateFormat = "MMddyyyy";
		if(!isThisDateValid(date, dateFormat)) return new java.sql.Date(0);
		try {
			if(date != null && date.trim().length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				java.util.Date utilDate = sdf.parse(date);
				java.sql.Date sqlDate = new Date(utilDate.getTime()); 
				return sqlDate;
			} else {
				return null;
			}
		} catch (ParseException e) {}
		return new java.sql.Date(0);
	}
	
	private static java.sql.Timestamp toDateTimestamp(String dateTime) {
    	String dateFormat = "yyyyMMddHHmmss";
		if(!isThisDateValid(dateTime, dateFormat)) return new java.sql.Timestamp(0);
		try {
			if(dateTime != null && dateTime.trim().length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				java.util.Date utilDate = sdf.parse(dateTime);
				java.sql.Timestamp  sqlDate = new java.sql.Timestamp(utilDate.getTime()); 
				return sqlDate;
			} else {
				return null;
			}
		} catch (ParseException e) {}
		return new java.sql.Timestamp(0);
	}
	
	public static boolean isThisDateValid(String dateToValidate, String dateFromat){
    	if(dateToValidate == null || dateFromat == null){
			return false;
		} else if(dateToValidate.length() != dateFromat.length()) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			//if not valid, it will throw ParseException
			java.util.Date date = sdf.parse(dateToValidate);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private long toLong(String str) {
		try {
			if(str != null && str.trim().length() > 0) {
				return Long.valueOf(str);
			}
		} catch (Exception e) {}
		return -99;
	}
	
	/**
	 * Add exception entry
	 * @param studentDetails
	 * @param historyId
	 * @param errorMessage
	 * @return
	 */
	public String exceptionHistoryUpdate(final StudentDetails studentDetails, final String historyId,
			final List<String> errorMessage) {
		String exceptionId = (String) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			int count =1;
			//String salt = PasswordGenerator.getNextSalt();
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String errCode = "100";
				StringBuffer errorBuff = new StringBuffer();
				if(errorMessage != null && !errorMessage.isEmpty()) {
					for(String errMsg : errorMessage) {
						if(errMsg != null) {
							errMsg = errMsg.replaceAll("ORA", "ERR");
							errorBuff.append(errMsg).append("~");
						}
					}
				}
				
				CallableStatement cs = con.prepareCall("{call " +IQueryConstants.CREATE_SP_EXCEPTION + "}");
				cs.setString(count++, "ERESOURCE");
				cs.setString(count++, studentDetails.getUUID());
				cs.setLong(count++, toLong(historyId));
				cs.setString(count++, errorBuff.toString()); // description
				cs.setString(count++, errCode); // ERROR CODE
				cs.setString(count++, IApplicationConstants.JOB_STATUS.ER.toString()); // exception status
				
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				String exceptionId = null; 
				try {
					cs.execute();
					exceptionId = cs.getString(7);
					logger.log(IAppLogger.DEBUG, "ER EXCEPTION INSERT DONE");
					logger.log(IAppLogger.INFO, "ER exception created with exception id: " + exceptionId);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return exceptionId;
			}
		});
		return exceptionId;
	}
	
	/**
	 * Create/update student bio
	 * @param studentDetails
	 * @param studentDataLoadTO
	 * @return
	 */
	public StudentDataLoadTO saveStudentBio(final StudentDetails studentDetails, final StudentDataLoadTO studentDataLoadTO) {
		StudentDataLoadTO dataLoadTO = studentDataLoadTO;
		long studId = (Long) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			int count =1;
			//String salt = PasswordGenerator.getNextSalt();
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				BioDetails bioDetails = studentDetails.getBioDetails();
				DemoDetails demoDetails = studentDetails.getDemoDetails();
				OrgDetails orgDetails = studentDetails.getOrgDetails();
				
				CallableStatement cs = con.prepareCall("{call " +IQueryConstants.CREATE_UPDATE_SP_BIO + "}");
				cs.setString(count++, studentDetails.getUUID());
				cs.setString(count++, orgDetails.getStateCode());
				cs.setString(count++, orgDetails.getStateName());
				cs.setLong(count++, toLong(orgDetails.getcTBCustomerID()));
				cs.setString(count++, bioDetails.getFirstName());
				cs.setString(count++, bioDetails.getMiddleName());
				cs.setString(count++, bioDetails.getLastName());
				//cs.setDate(count++, toDate(bioDetails.getDateOfBirth()));
				cs.setString(count++, bioDetails.getDateOfBirth());
				cs.setString(count++, bioDetails.getGender());
				cs.setString(count++, demoDetails.getGovernmentID());
				cs.setString(count++, demoDetails.getGovernmentIDType());
				cs.setString(count++, demoDetails.getAddress1());
				cs.setString(count++, demoDetails.getAddress2());
				cs.setString(count++, demoDetails.getCity());
				cs.setString(count++, demoDetails.getCounty());
				cs.setString(count++, demoDetails.getState());
				cs.setString(count++, demoDetails.getZip());
				cs.setString(count++, demoDetails.getEmail());
				cs.setString(count++, demoDetails.getAlternateEmail());
				cs.setString(count++, demoDetails.getPrimaryPhoneNumber());
				cs.setString(count++, demoDetails.getCellPhoneNumber());
				cs.setString(count++, demoDetails.getAlternateNumber());
				cs.setString(count++, demoDetails.getResolvedEthnicityRace());
				cs.setString(count++, demoDetails.getHomeLanguage());
				cs.setString(count++, demoDetails.getEducationLevel());
				cs.setString(count++, demoDetails.getAttendCollege());
				cs.setString(count++, demoDetails.getContact());
				cs.setString(count++, demoDetails.getExamineeCcountyParishCode());
				//cs.setTimestamp(count++, toDateTimestamp(demoDetails.getRegisteredOn()));
				cs.setString(count++, demoDetails.getRegisteredOn());
				cs.setString(count++, demoDetails.getRegisteredAtTestCenter());
				cs.setString(count++, demoDetails.getRegisteredAtTestCenterCode());
				cs.setString(count++, demoDetails.getRegisteredAtTestCenterCountyParishCode());
				
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				long studId = 0; 
				try {
					cs.execute();
					studId = cs.getLong(33);
					String errorMsg = cs.getString(35);
					if(errorMsg != null) {
						studentDataLoadTO.setSuccess(false);
						studentDataLoadTO.setSummary(errorMsg);
						logger.log(IAppLogger.INFO, "Data validation error while creating/updating bio: " + studId);
					} else {
						logger.log(IAppLogger.DEBUG, "ER BIO INSER/UPDATE DONE");
						logger.log(IAppLogger.INFO, "ER bio created/updated with er_stud id: " + studId);
					}
				} catch (SQLException e) {
					studentDataLoadTO.setSuccess(false);
					studentDataLoadTO.setSummary("ERR:900" + " - " + e.getMessage());
					e.printStackTrace();
				}
				return studId;
			}
		});
		dataLoadTO.setStudentBioDetailsId(studId);
		return dataLoadTO;
	}
	
	/**
	 * Create/update schedule
	 * @param studentDataLoadTO
	 * @param scheduleDetails
	 * @return
	 */
	public StudentDataLoadTO saveSchedule(final StudentDetails studentDetails, final StudentDataLoadTO studentDataLoadTO, final ScheduleDetails scheduleDetails) {
		StudentDataLoadTO dataLoadTO = studentDataLoadTO;
		long scheduleId = (Long) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
			int count =1;
			//String salt = PasswordGenerator.getNextSalt();
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				
				CallableStatement cs = con.prepareCall("{call " +IQueryConstants.CREATE_UPDATE_SP_SCHEDULE + "}");
				BioDetails bioDetails = studentDetails.getBioDetails();
				OrgDetails orgDetails = studentDetails.getOrgDetails();
				cs.setString(count++, studentDetails.getUUID());
				cs.setString(count++, CustomStringUtil.appendString(
						bioDetails.getLastName(), ", ", bioDetails.getFirstName(), " ", bioDetails.getMiddleName()));
				cs.setLong(count++, studentDataLoadTO.getStudentBioDetailsId());
				cs.setLong(count++, toLong(scheduleDetails.getScheduleID()));
				//cs.setTimestamp(count++, toDateTimestamp(scheduleDetails.getDateScheduled()));
				cs.setString(count++, scheduleDetails.getDateScheduled());
				cs.setString(count++, scheduleDetails.getTimeOfDay());
				//cs.setTimestamp(count++, toDateTimestamp(scheduleDetails.getDateCheckedIn()));
				cs.setString(count++, scheduleDetails.getDateCheckedIn());
				cs.setLong(count++, toLong(scheduleDetails.getContentAreaCode()));
				cs.setString(count++, scheduleDetails.getContentTestType());
				cs.setLong(count++, toLong(scheduleDetails.getContentTestCode()));
				cs.setString(count++, scheduleDetails.getBarcode());
				cs.setString(count++, scheduleDetails.getForm());
				cs.setString(count++, scheduleDetails.gettASCReadiness());
				cs.setString(count++, scheduleDetails.getECC());
				cs.setString(count++, scheduleDetails.getTestCenterCode());
				cs.setString(count++, scheduleDetails.getTestCenterName());
				cs.setString(count++, scheduleDetails.getScheduledAtTestCenterCountyParishCode());
				cs.setString(count++, orgDetails.getStateCode());
				
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.NUMBER);
				cs.registerOutParameter(count++, oracle.jdbc.OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) {
				long scheduleId = 0; 
				try {
					cs.execute();
					scheduleId = cs.getLong(19);
					String errorMsg = cs.getString(21);
					if(errorMsg != null) {
						studentDataLoadTO.setSuccess(false);
						studentDataLoadTO.setSummary(errorMsg);
						logger.log(IAppLogger.INFO, "Data validation error while creating/updating schedule: " + scheduleId);
					} else {
						logger.log(IAppLogger.DEBUG, "ER SCHEDULE INSERT/UPDATE DONE");
						logger.log(IAppLogger.INFO, "ER schedule created/updated with er_test_schedule id: " + scheduleId);
					}
				} catch (SQLException e) {
					studentDataLoadTO.setSuccess(false);
					studentDataLoadTO.setSummary("ERR:900" + " - " + e.getMessage());
					e.printStackTrace();
				}
				return scheduleId;
			}
		});
		dataLoadTO.setSubtestDetailsId(scheduleId);
		return dataLoadTO;
	}
	
	/**
	 * Get student details for a student BIO ID
	 */
	public JobTrackingTO getStudent(final JobTrackingTO jobTrackingTO) throws Exception {
		List<Map<String,Object>> dataList = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_STUDENT, jobTrackingTO.getOrgCode(), jobTrackingTO.getStudentBioId(), jobTrackingTO.getUuid());
		if ( dataList != null && dataList.size() > 0 ) {
			for (Map<String, Object> data : dataList) {
				jobTrackingTO.setClikedOrgId(((BigDecimal) data.get("org_nodeid")).longValue());
				jobTrackingTO.setCustomerId(((BigDecimal) data.get("customerid")).toString());
			}
		}
		return jobTrackingTO;
	}
	
}
