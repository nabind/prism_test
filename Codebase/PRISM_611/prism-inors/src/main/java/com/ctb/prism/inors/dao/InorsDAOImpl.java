package com.ctb.prism.inors.dao;

import static com.ctb.prism.inors.util.InorsDownloadUtil.wrap;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.transferobject.StudentPDFLogTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;

/**
 * This class is responsible for reading and writing to database. The transactions through this class should be related to report only.
 * 
 * @author TCS
 * 
 */
//@Repository("inorsDAO")
public class InorsDAOImpl extends BaseDAO implements IInorsDAO {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsDAOImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#createJob(com.ctb.prism.inors. transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO) {
		long jobId = getJdbcTemplatePrism().queryForObject(IQueryConstants.JOB_SEQ_ID, Long.class);
		getJdbcTemplatePrism().update(IQueryConstants.CREATE_JOB, jobId, bulkDownloadTO.getFileName(), bulkDownloadTO.getQuerysheetFile(), bulkDownloadTO.getUdatedBy(),
				bulkDownloadTO.getSelectedNodes(), bulkDownloadTO.getStudentCount(), bulkDownloadTO.getEmail(), bulkDownloadTO.getReportUrl(), bulkDownloadTO.getTestAdministration(),
				bulkDownloadTO.getCustomerId(), bulkDownloadTO.getGrade(), bulkDownloadTO.getRequestType(), bulkDownloadTO.getGroupFile(), bulkDownloadTO.getCollationHierarchy(),
				bulkDownloadTO.getDownloadMode());
		bulkDownloadTO.setJobId(jobId);
		return bulkDownloadTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#updateStatus(com.ctb.prism.inors. transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_STATUS, bulkDownloadTO.getStatus(), bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#updateJobLog(com.ctb.prism.inors. transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_LOG, bulkDownloadTO.getLog(), bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	}

	/**
	 * update job after completion
	 * 
	 * @param JobTrackingTO
	 * @return boolean
	 */
	public JobTrackingTO updateJob(JobTrackingTO jobTrackingTO) {
		getJdbcTemplatePrism(jobTrackingTO.getContractName()).update(IQueryConstants.UPDATE_JOB, 
				jobTrackingTO.getJobStatus(),
				jobTrackingTO.getRequestFilename(),
				jobTrackingTO.getJobLog(),
				jobTrackingTO.getFileSize(),
				jobTrackingTO.getJobId());
		jobTrackingTO.setSuccess(true);
		return jobTrackingTO;
	}

	/**
	 * update job status and log
	 * 
	 * @param jobTrackingTO
	 * @return boolean
	 */
	public JobTrackingTO updateJobStatusAndLog(JobTrackingTO jobTrackingTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_STATUS_AND_LOG, jobTrackingTO.getJobStatus(), jobTrackingTO.getJobLog(), jobTrackingTO.getJobId());
		jobTrackingTO.setSuccess(true);
		return jobTrackingTO;
	}

	/**
	 * get job details
	 * 
	 * @param jobId
	 * @return
	 */
	public JobTrackingTO getJob(String jobId, String contractName) {
		JobTrackingTO jobTrackingTO = null;
		try {
			List<Map<String, Object>> lstData = null;
			logger.log(IAppLogger.INFO, "Get job = " + jobId);
			lstData = getJdbcTemplatePrism(contractName).queryForList(IQueryConstants.JOB_DETAILS, jobId);
			if (lstData.size() > 0) {
				for (Map<String, Object> fieldDetails : lstData) {
					jobTrackingTO = new JobTrackingTO();
					jobTrackingTO.setJobId(((BigDecimal) (fieldDetails.get("JOB_ID"))).longValue());
					if(fieldDetails.get("USERID") != null)  jobTrackingTO.setUserId(((BigDecimal) (fieldDetails.get("USERID"))).toString()) ;
					// jobTrackingTO.setUserName((String) (fieldDetails.get("username")));
					jobTrackingTO.setJobName((String) (fieldDetails.get("JOB_NAME")));
					jobTrackingTO.setExtractCategory((String) (fieldDetails.get("EXTRACT_CATEGORY")));
					jobTrackingTO.setExtractFiletype((String) (fieldDetails.get("EXTRACT_FILETYPE")));
					jobTrackingTO.setRequestType((String) (fieldDetails.get("REQUEST_TYPE")));
					jobTrackingTO.setRequestSummary((String) (fieldDetails.get("REQUEST_SUMMARY")));
					jobTrackingTO.setRequestDetails((String) (fieldDetails.get("REQUEST_DETAILS")));
					jobTrackingTO.setRequestFilename((String) (fieldDetails.get("REQUEST_FILENAME")));
					jobTrackingTO.setRequestEmail((String) (fieldDetails.get("REQUEST_EMAIL")));
					jobTrackingTO.setJobLog((String) (fieldDetails.get("JOB_LOG")));
					jobTrackingTO.setJobStatus((String) (fieldDetails.get("JOB_STATUS")));
					jobTrackingTO.setAdminId((((BigDecimal) fieldDetails.get("ADMINID")) != null) ? ((BigDecimal) fieldDetails.get("ADMINID")).toString() : null);
					jobTrackingTO.setCustomerId((((BigDecimal) fieldDetails.get("CUSTOMERID")) != null) ? ((BigDecimal) fieldDetails.get("CUSTOMERID")).toString() : null);
					jobTrackingTO.setOtherRequestparams((String) (fieldDetails.get("OTHER_REQUEST_PARAMS")));
				}
			} else {
				logger.log(IAppLogger.INFO, "No job found !!");
			}
		} catch (Exception ex) {
			logger.log(IAppLogger.ERROR, "Error getting job details" + ex.getMessage());
			ex.printStackTrace();
		}
		return jobTrackingTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getSchoolClass(java.lang.String)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors', #p0, 'getSchoolClass' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc', #p0, 'getSchoolClass' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo', #p0, 'getSchoolClass' )")
	} )
	public BulkDownloadTO getSchoolClass(String studentBioId) {
		BulkDownloadTO bulkDownloadTO = null;
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_SCHOOL_CLASS_FOR_STUDENT, studentBioId);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				bulkDownloadTO = new BulkDownloadTO();
				bulkDownloadTO.setClassNodes(((BigDecimal) (fieldDetails.get("CLS"))).toString());
				bulkDownloadTO.setOrgClass((String) (fieldDetails.get("CLS_NAME")));
				bulkDownloadTO.setSchoolNodes(((BigDecimal) (fieldDetails.get("SCH"))).toString());
				bulkDownloadTO.setSchool((String) (fieldDetails.get("SCH_NAME")));
			}
		}
		return bulkDownloadTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getNodeName(java.lang.String)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors', #p0, 'getNodeName' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc', #p0, 'getNodeName' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo', #p0, 'getNodeName' )")
	} )
	public BulkDownloadTO getNodeName(String orgNodeid) {
		BulkDownloadTO bulkDownloadTO = null;
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_NODE_NAME, orgNodeid);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				bulkDownloadTO = new BulkDownloadTO();
				bulkDownloadTO.setOrgClass((String) (fieldDetails.get("ORG_NAME")));
			}
		}
		return bulkDownloadTO;
	}

	/**
	 * Fetches the GRT list from the database. All field level data are wrapped using double quotes. Field level data validations may apply to specific field to support multiple layouts. Setter
	 * methods set valid data for that layout to achieve better performance and to avoid multiple getter or setter calls.
	 * 
	 * @param orgNodeId
	 * @param parentOrgNodeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors', #p0, #p1, #p2, #p3, #p4, 'getGRTList' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc', #p0, #p1, #p2, #p3, #p4, 'getGRTList' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo', #p0, #p1, #p2, #p3, #p4, 'getGRTList' )")
	} )
	private List<GrtTO> getGRTList(final Long customerId, final String productId, final String testProgram, final String districtId, final String schoolId) {
		logger.log(IAppLogger.INFO, "Enter: getGRTList()");
		List<GrtTO> grtList = new ArrayList<GrtTO>();
		try {
			if ("-1".equals(schoolId)) {
				logger.log(IAppLogger.INFO, "All Schools");
				grtList = (List<GrtTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_ALL_RESULTS_GRT);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, customerId);
						cs.setLong(3, Long.parseLong(districtId));
						cs.setString(4, testProgram);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<GrtTO> grtTOResult = new ArrayList<GrtTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(5);
							grtTOResult = getGrtListFromResultSet(rs);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return grtTOResult;
					}
				});
				logger.log(IAppLogger.INFO, "All Schools grtList.size(): " + grtList.size());
			} else {
				logger.log(IAppLogger.INFO, "schoolId=" + schoolId);
				grtList = (List<GrtTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_RESULTS_GRT);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, customerId);
						cs.setLong(3, Long.parseLong(districtId));
						cs.setLong(4, Long.parseLong(schoolId));
						cs.setString(5, testProgram);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<GrtTO> grtTOResult = new ArrayList<GrtTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(6);
							grtTOResult = getGrtListFromResultSet(rs);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return grtTOResult;
					}
				});
				logger.log(IAppLogger.INFO, "grtList.size(): " + grtList.size() +" for school id " + schoolId);
			}
		} finally {
			logger.log(IAppLogger.INFO, "Exit: getGRTList()");
		}
		return grtList;
	}
	
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getGRTTableData'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).listKey(#aliasList)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#headerList)),'inors' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getGRTTableData'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).listKey(#aliasList)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#headerList)),'tasc' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getGRTTableData'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).listKey(#aliasList)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#headerList)),'usmo' )")
	} )
	private ArrayList<ArrayList<String>> getGRTTableData(Map<String, String> paramMap, final ArrayList<String> aliasList, final ArrayList<String> headerList) {
		logger.log(IAppLogger.INFO, "Enter: getGRTList()");
		final Long customerId = Long.valueOf(paramMap.get("customerId"));
		final String productId = paramMap.get("productId");
		final String testProgram = paramMap.get("testProgram");
		final String districtId = paramMap.get("parentOrgNodeId");
		final String schoolId = paramMap.get("orgNodeId");

		logger.log(IAppLogger.INFO, "customerId = " + customerId);
		logger.log(IAppLogger.INFO, "productId = " + productId);
		logger.log(IAppLogger.INFO, "testProgram = " + testProgram);
		logger.log(IAppLogger.INFO, "districtId = " + districtId);
		logger.log(IAppLogger.INFO, "schoolId = " + schoolId);

		ArrayList<ArrayList<String>> grtList = new ArrayList<ArrayList<String>>();
		try {
			if ("-1".equals(schoolId)) {
				logger.log(IAppLogger.INFO, "All Schools");
				grtList = (ArrayList<ArrayList<String>>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_ALL_RESULTS_GRT);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, customerId);
						cs.setLong(3, Long.parseLong(districtId));
						cs.setString(4, testProgram);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						ArrayList<ArrayList<String>> grtTOResult = new ArrayList<ArrayList<String>>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(5);
							grtTOResult = InorsDownloadUtil.getTableDataFromResultSet(rs, aliasList, headerList);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return grtTOResult;
					}
				});
				logger.log(IAppLogger.INFO, "All Schools grtList.size(): " + grtList.size());
			} else {
				logger.log(IAppLogger.INFO, "schoolId=" + schoolId);
				grtList = (ArrayList<ArrayList<String>>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_RESULTS_GRT);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, customerId);
						cs.setLong(3, Long.parseLong(districtId));
						cs.setLong(4, Long.parseLong(schoolId));
						cs.setString(5, testProgram);
						cs.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(7, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						ArrayList<ArrayList<String>> grtTOResult = new ArrayList<ArrayList<String>>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(6);
							grtTOResult = InorsDownloadUtil.getTableDataFromResultSet(rs, aliasList, headerList);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return grtTOResult;
					}

				});
				logger.log(IAppLogger.INFO, "grtList.size(): " + grtList.size() + " for school id " + schoolId);
			}
		} finally {
			logger.log(IAppLogger.INFO, "Exit: getGRTList()");
		}
		return grtList;
	}

	/**
	 * Creates a List of GrtTO from a ResultSet
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<GrtTO> getGrtListFromResultSet(ResultSet rs) throws SQLException {
		List<GrtTO> grtTOResult = new ArrayList<GrtTO>();
		while (rs.next()) {
			GrtTO to = new GrtTO();
			to.setL_TapeMode(wrap(rs.getString("TAPE_MODE"), '"'));
			to.setL_Orgtstgpgm(wrap(rs.getString("ORGTSTGPGM"), '"'));
			to.setL_CorporationorDioceseName(wrap(rs.getString("DISTRICT_NAME"), '"'));
			to.setL_CorporationorDioceseNumber(wrap(rs.getString("DISTRICT_ID"), '"'));
			to.setL_SchoolName(wrap(rs.getString("SCHOOL_NAME"), '"'));
			to.setL_SchoolNumber(wrap(rs.getString("SCHOOL_ID"), '"'));
			to.setL_TeacherName(wrap(rs.getString("TEACHER_NAME"), '"'));
			to.setL_TeacherElementNumberCTBUse(wrap(rs.getString("TEACHER_ELEMENT"), '"'));
			to.setL_Grade(wrap(rs.getString("GRADEID"), '"'));
			to.setL_City(wrap(rs.getString("CITY"), '"'));
			to.setL_State(wrap(rs.getString("STATE"), '"'));
			to.setL_ISTEPTestName(wrap(rs.getString("ISTEP_TEST_NAME"), '"'));
			to.setL_ISTEPBook(wrap(rs.getString("ISTEP_BOOK_NUM"), '"'));
			to.setL_ISTEPForm(wrap(rs.getString("ISTEP_FORM"), '"'));
			to.setL_TestDateMMDDYY(wrap(rs.getString("TEST_DATE"), '"'));
			to.setL_StudentLastName(wrap(rs.getString("STUDENT_LAST_NAME"), '"'));
			to.setL_StudentFirstName(wrap(rs.getString("STUDENT_FIRST_NAME"), '"'));
			to.setL_StudentMiddleInitial(wrap(rs.getString("STUDENT_MIDDLE_INITIAL"), '"'));
			to.setL_StudentsGender(wrap(rs.getString("STUDENTS_GENDER"), '"'));
			to.setL_BirthDateMMDDYY(wrap(rs.getString("BIRTH_DATE"), '"'));
			to.setL_ChronologicalAgeInMonths(wrap(rs.getString("CHRONOLOGICAL_AGE_IN_MONTHS"), '"'));
			to.setL_Ethnicity(wrap(rs.getString("ETHNICITY"), '"')); // TODO
			to.setL_RaceAmericanIndianAlaskaNative(wrap(rs.getString("RACE_AMERICAN_INDIAN"), '"'));
			to.setL_RaceAsian(wrap(rs.getString("RACE_ASIAN"), '"'));
			to.setL_RaceBlackorAfricanAmerican(wrap(rs.getString("RACE_BLACK"), '"'));
			to.setL_RaceNativeHawaiianorOtherPacificIslander(wrap(rs.getString("RACE_PACIFIC_ISLANDER"), '"'));
			to.setL_RaceWhite(wrap(rs.getString("RACE_WHITE"), '"'));
			to.setL_Filler1(wrap(rs.getString("FILLER_1"), '"')); // TODO
			to.setL_StudentTestNumberAI(wrap(rs.getString("STUDENT_TEST_AI"), '"'));
			to.setL_SpecialCodeJ(wrap(rs.getString("SPECIAL_CODE_J"), '"')); // TODO
			to.setL_ResolvedEthnicityK(wrap(rs.getString("ETHNICITY_K"), '"'));
			to.setL_SpecialEducationL(wrap(rs.getString("SPECIAL_EDUCATION_L"), '"'));
			to.setL_ExceptionalityM(wrap(rs.getString("EXCEPTIONALITY_M"), '"'));
			to.setL_SocioEconomicStatusN(wrap(rs.getString("SOCIOECONOMIC_STATUS_N"), '"'));
			to.setL_Section504O(wrap(rs.getString("SECTION_504_O"), '"'));
			to.setL_EnglishLearnerELP(wrap(rs.getString("ENGLISH_LEARNER_P"), '"')); // TODO -
			to.setL_MigrantQ(wrap(rs.getString("MIGRANT_Q"), '"'));
			to.setL_LocaluseR(wrap(rs.getString("LOCAL_USE_R"), '"'));
			to.setL_LocaluseS(wrap(rs.getString("LOCAL_USE_S"), '"'));
			to.setL_LocaluseT(wrap(rs.getString("LOCAL_USE_T"), '"'));
			to.setL_MatchUnmatchU(wrap(rs.getString("MATCH_UNMATCH_U"), '"'));
			to.setL_DuplicateV(wrap(rs.getString("DUPLICATE_V"), '"'));
			to.setL_CTBUseOnlyW(wrap(rs.getString("CTB_USE_ONLY_W"), '"')); // TODO -
			to.setL_SpecialCodeX(wrap(rs.getString("SPECIAL_CODE_X"), '"'));
			to.setL_SpecialCodeY(wrap(rs.getString("SPECIAL_CODE_Y"), '"'));
			to.setL_SpecialCodeZ(wrap(rs.getString("SPECIAL_CODE_Z"), '"'));
			to.setL_AccommodationsEla(wrap(rs.getString("ACCOMMODATIONS_ELA"), '"'));
			to.setL_AccommodationsMath(wrap(rs.getString("ACCOMMODATIONS_MATH"), '"')); // TODO
			to.setL_AccommodationsScience(wrap(rs.getString("ACCOMMODATIONS_SCIENCE"), '"')); // TODO
			to.setL_AccommodationsSocialStudies(wrap(rs.getString("ACCOMMODATIONS_SS"), '"')); // TODO
			to.setL_CorporationUseID(wrap(rs.getString("CORPORATION_USE_ID"), '"'));
			to.setL_CustomerUse(wrap(rs.getString("CUSTOMER_USE"), '"'));
			to.setL_Filler2(wrap(rs.getString("FILLER_2"), '"')); // TODO
			to.setL_Filler3(wrap(rs.getString("FILLER_3"), '"')); // TODO
			to.setL_ElaPFIndicator(wrap(rs.getString("ELA_PF_INDICATOR"), '"'));
			to.setL_MathPFIndicator(wrap(rs.getString("MATH_PF_INDICATOR"), '"'));
			to.setL_SciencePFIndicator(wrap(rs.getString("SCIENCE_PF_INDICATOR"), '"'));
			to.setL_SocialStudiesPFIndicator(wrap(rs.getString("SOCIAL_PF_INDICATOR"), '"'));
			to.setL_ElaNumberCorrect(wrap(rs.getString("ENGLANG_ARTS_NUM_CORRECT"), '"'));
			to.setL_MathNumberCorrect(wrap(rs.getString("MATHEMATICS_NUM_CORRECT"), '"'));
			to.setL_ScienceNumberCorrect(wrap(rs.getString("SCIENCE_NUM_CORRECT"), '"'));
			to.setL_SocialStudiesNumberCorrect(wrap(rs.getString("SOCIAL_NUM_CORRECT"), '"'));
			to.setL_ElaScaleScore(wrap(rs.getString("ENGLAN_ARTS_SCALE_SCORE"), '"'));
			to.setL_MathScaleScore(wrap(rs.getString("MATHEMATICS_SCALE_SCORE"), '"'));
			to.setL_ScienceScaleScore(wrap(rs.getString("SCIENCE_SCALE_SCORE"), '"'));
			to.setL_SocialStudiesScaleScore(wrap(rs.getString("SOCIAL_SCALE_SCORE"), '"'));
			to.setL_ElaScaleScoreSEM(wrap(rs.getString("ENGLAN_ARTS_SCALE_SCORE_SEM"), '"'));
			to.setL_MathScaleScoreSEM(wrap(rs.getString("MATHEMATICS_SCALE_SCORE_SEM"), '"'));
			to.setL_ScienceScaleScoreSEM(wrap(rs.getString("SCIENCE_SCALE_SCORE_SEM"), '"'));
			to.setL_SocialStudiesScaleScoreSEM(wrap(rs.getString("SOCIAL_SCALE_SCORE_SEM"), '"'));
			to.setL_MasteryIndicator1(wrap(rs.getString("MASTERY_INDICATOR_1"), '"'));
			to.setL_MasteryIndicator2(wrap(rs.getString("MASTERY_INDICATOR_2"), '"'));
			to.setL_MasteryIndicator3(wrap(rs.getString("MASTERY_INDICATOR_3"), '"'));
			to.setL_MasteryIndicator4(wrap(rs.getString("MASTERY_INDICATOR_4"), '"'));
			to.setL_MasteryIndicator5(wrap(rs.getString("MASTERY_INDICATOR_5"), '"'));
			to.setL_MasteryIndicator6(wrap(rs.getString("MASTERY_INDICATOR_6"), '"'));
			to.setL_MasteryIndicator7(wrap(rs.getString("MASTERY_INDICATOR_7"), '"'));
			to.setL_MasteryIndicator8(wrap(rs.getString("MASTERY_INDICATOR_8"), '"'));
			to.setL_MasteryIndicator9(wrap(rs.getString("MASTERY_INDICATOR_9"), '"'));
			to.setL_MasteryIndicator10(wrap(rs.getString("MASTERY_INDICATOR_10"), '"'));
			to.setL_MasteryIndicator11(wrap(rs.getString("MASTERY_INDICATOR_11"), '"'));
			to.setL_MasteryIndicator12(wrap(rs.getString("MASTERY_INDICATOR_12"), '"'));
			to.setL_MasteryIndicator13(wrap(rs.getString("MASTERY_INDICATOR_13"), '"'));
			to.setL_MasteryIndicator14(wrap(rs.getString("MASTERY_INDICATOR_14"), '"'));
			to.setL_MasteryIndicator15(wrap(rs.getString("MASTERY_INDICATOR_15"), '"'));
			to.setL_MasteryIndicator16(wrap(rs.getString("MASTERY_INDICATOR_16"), '"'));
			to.setL_MasteryIndicator17(wrap(rs.getString("MASTERY_INDICATOR_17"), '"'));
			to.setL_MasteryIndicator18(wrap(rs.getString("MASTERY_INDICATOR_18"), '"'));
			to.setL_MasteryIndicator19(wrap(rs.getString("MASTERY_INDICATOR_19"), '"'));
			to.setL_MasteryIndicator20(wrap(rs.getString("MASTERY_INDICATOR_20"), '"'));
			to.setL_MasteryIndicator21(wrap(rs.getString("MASTERY_INDICATOR_21"), '"'));
			to.setL_MasteryIndicator22(wrap(rs.getString("MASTERY_INDICATOR_22"), '"'));
			to.setL_MasteryIndicator23(wrap(rs.getString("MASTERY_INDICATOR_23"), '"'));
			to.setL_MasteryIndicator24(wrap(rs.getString("MASTERY_INDICATOR_24"), '"'));
			to.setL_MasteryIndicator25(wrap(rs.getString("MASTERY_INDICATOR_25"), '"'));
			to.setL_MasteryIndicator26(wrap(rs.getString("MASTERY_INDICATOR_26"), '"'));
			to.setL_MasteryIndicator27(wrap(rs.getString("MASTERY_INDICATOR_27"), '"'));
			to.setL_MasteryIndicator28(wrap(rs.getString("MASTERY_INDICATOR_28"), '"'));
			to.setL_MasteryIndicator29(wrap(rs.getString("MASTERY_INDICATOR_29"), '"'));
			to.setL_MasteryIndicator30(wrap(rs.getString("MASTERY_INDICATOR_30"), '"'));
			to.setL_MasteryIndicator31(wrap(rs.getString("MASTERY_INDICATOR_31"), '"'));
			to.setL_MasteryIndicator32(wrap(rs.getString("MASTERY_INDICATOR_32"), '"'));
			to.setL_MasteryIndicator33(wrap(rs.getString("MASTERY_INDICATOR_33"), '"'));
			to.setL_MasteryIndicator34(wrap(rs.getString("MASTERY_INDICATOR_34"), '"'));
			to.setL_MasteryIndicator35(wrap(rs.getString("MASTERY_INDICATOR_35"), '"'));
			to.setL_MasteryIndicator36(wrap(rs.getString("MASTERY_INDICATOR_36"), '"'));
			to.setL_MasteryIndicator37(wrap(rs.getString("MASTERY_INDICATOR_37"), '"'));
			to.setL_MasteryIndicator38(wrap(rs.getString("MASTERY_INDICATOR_38"), '"'));
			to.setL_MasteryIndicator39(wrap(rs.getString("MASTERY_INDICATOR_39"), '"'));
			to.setL_MasteryIndicator40(wrap(rs.getString("MASTERY_INDICATOR_40"), '"'));
			to.setL_OPIIPI1(wrap(rs.getString("OPIIPI_1"), '"'));
			to.setL_OPIIPI2(wrap(rs.getString("OPIIPI_2"), '"'));
			to.setL_OPIIPI3(wrap(rs.getString("OPIIPI_3"), '"'));
			to.setL_OPIIPI4(wrap(rs.getString("OPIIPI_4"), '"'));
			to.setL_OPIIPI5(wrap(rs.getString("OPIIPI_5"), '"'));
			to.setL_OPIIPI6(wrap(rs.getString("OPIIPI_6"), '"'));
			to.setL_OPIIPI7(wrap(rs.getString("OPIIPI_7"), '"'));
			to.setL_OPIIPI8(wrap(rs.getString("OPIIPI_8"), '"'));
			to.setL_OPIIPI9(wrap(rs.getString("OPIIPI_9"), '"'));
			to.setL_OPIIPI10(wrap(rs.getString("OPIIPI_10"), '"'));
			to.setL_OPIIPI11(wrap(rs.getString("OPIIPI_11"), '"'));
			to.setL_OPIIPI12(wrap(rs.getString("OPIIPI_12"), '"'));
			to.setL_OPIIPI13(wrap(rs.getString("OPIIPI_13"), '"'));
			to.setL_OPIIPI14(wrap(rs.getString("OPIIPI_14"), '"'));
			to.setL_OPIIPI15(wrap(rs.getString("OPIIPI_15"), '"'));
			to.setL_OPIIPI16(wrap(rs.getString("OPIIPI_16"), '"'));
			to.setL_OPIIPI17(wrap(rs.getString("OPIIPI_17"), '"'));
			to.setL_OPIIPI18(wrap(rs.getString("OPIIPI_18"), '"'));
			to.setL_OPIIPI19(wrap(rs.getString("OPIIPI_19"), '"'));
			to.setL_OPIIPI20(wrap(rs.getString("OPIIPI_20"), '"'));
			to.setL_OPIIPI21(wrap(rs.getString("OPIIPI_21"), '"'));
			to.setL_OPIIPI22(wrap(rs.getString("OPIIPI_22"), '"'));
			to.setL_OPIIPI23(wrap(rs.getString("OPIIPI_23"), '"'));
			to.setL_OPIIPI24(wrap(rs.getString("OPIIPI_24"), '"'));
			to.setL_OPIIPI25(wrap(rs.getString("OPIIPI_25"), '"'));
			to.setL_OPIIPI26(wrap(rs.getString("OPIIPI_26"), '"'));
			to.setL_OPIIPI27(wrap(rs.getString("OPIIPI_27"), '"'));
			to.setL_OPIIPI28(wrap(rs.getString("OPIIPI_28"), '"'));
			to.setL_OPIIPI29(wrap(rs.getString("OPIIPI_29"), '"'));
			to.setL_OPIIPI30(wrap(rs.getString("OPIIPI_30"), '"'));
			to.setL_OPIIPI31(wrap(rs.getString("OPIIPI_32"), '"'));
			to.setL_OPIIPI32(wrap(rs.getString("OPIIPI_32"), '"'));
			to.setL_OPIIPI33(wrap(rs.getString("OPIIPI_33"), '"'));
			to.setL_OPIIPI34(wrap(rs.getString("OPIIPI_34"), '"'));
			to.setL_OPIIPI35(wrap(rs.getString("OPIIPI_35"), '"'));
			to.setL_OPIIPI36(wrap(rs.getString("OPIIPI_36"), '"'));
			to.setL_OPIIPI37(wrap(rs.getString("OPIIPI_37"), '"'));
			to.setL_OPIIPI38(wrap(rs.getString("OPIIPI_38"), '"'));
			to.setL_OPIIPI39(wrap(rs.getString("OPIIPI_39"), '"'));
			to.setL_OPIIPI40(wrap(rs.getString("OPIIPI_40"), '"'));
			to.setL_ELACRSession2ItemResponses(wrap(rs.getString("ELA_CR_SESSION_2"), '"'));
			to.setL_ELACRSession3ItemResponses(wrap(rs.getString("ELA_CR_SESSION_3"), '"'));
			to.setL_MathCRSession1ItemResponses(wrap(rs.getString("MATH_CR_SESSION_1"), '"'));
			to.setL_ScienceCRSession4ItemResponses(wrap(rs.getString("SCIENCE_CR_SESSION_4"), '"')); // TODO
			to.setL_SocialStudiesCRSession4ItemResponses(wrap(rs.getString("SS_CR_SESSION_4"), '"')); // TODO
			to.setL_Filler4(wrap(rs.getString("FILLER_4"), '"'));
			to.setL_SchoolPersonnelNumberSPN1(wrap(rs.getString("SPN_1"), '"'));
			to.setL_SchoolPersonnelNumberSPN2(wrap(rs.getString("SPN_2"), '"'));
			to.setL_SchoolPersonnelNumberSPN3(wrap(rs.getString("SPN_3"), '"'));
			to.setL_SchoolPersonnelNumberSPN4(wrap(rs.getString("SPN_4"), '"'));
			to.setL_SchoolPersonnelNumberSPN5(wrap(rs.getString("SPN_5"), '"'));
			to.setL_Filler5(wrap(rs.getString("FILLER_5"), '"')); // TODO
			to.setL_CGRComputerGeneratedResponsefrom(wrap(rs.getString("CGR"), '"'));
			to.setL_CTBUseOnlyAppliedSkillsPPImageID(wrap(rs.getString("IMAGEID_APPLIED_SKILLS_PP"), '"'));
			to.setL_CTBUseOnlyAppliedSkillsOASImageID(wrap(rs.getString("IMAGEID_APPLIED_SKILLS_OAS"), '"'));
			to.setL_CTBUseOnlyMCPPImagingid(wrap(rs.getString("IMAGEID_MC_PP"), '"'));
			to.setL_CTBUseOnlyMCOASimagingid(wrap(rs.getString("IMAGEID_MC_OAS"), '"'));
			to.setL_BarcodeIDAppliedSkills(wrap(rs.getString("BARCODE_ID_APPLIED_SKILLS"), '"')); // TODO
			to.setL_BarcodeIDMultipleChoice(wrap(rs.getString("BARCODE_ID_MULTIPLE_CHOICE"), '"'));
			to.setL_CTBUseOnlyTestForm(wrap(rs.getString("TEST_FORM_DEFAULT_FLAG"), '"')); // TODO
			to.setL_CTBUseOnlyMCBlankbookFlag(wrap(rs.getString("MC_BLANK_BOOK_FLAG"), '"')); // TODO
			to.setL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest(wrap(rs.getString("TEST_FORM_AS"), '"')); // TODO
			to.setL_CTBUseOnlyTestFormMCFieldPilotTest(wrap(rs.getString("TEST_FORM_MC"), '"')); // TODO
			to.setL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest(wrap(rs.getString("OAS_INDICATOR_AS"), '"')); // TODO
			to.setL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest(wrap(rs.getString("OAS_INDICATOR_MC"), '"')); // TODO
			to.setL_CTBUseOnlyStructureLevel(wrap(rs.getString("STRUCTURE_LEVEL"), '"')); // TODO
			to.setL_CTBUseOnlyElementNumber(wrap(rs.getString("ELEMENT_NUMBER"), '"')); // TODO
			to.setL_ResolvedReportingStatusEla(wrap(rs.getString("RESOLVED_REPORTING_STATUS_ELA"), '"'));
			to.setL_ResolvedReportingStatusMath(wrap(rs.getString("RESOLVED_REPORTING_STATUS_MATH"), '"'));
			to.setL_ResolvedReportingStatusScience(wrap(rs.getString("RESLVD_REPOR_STUS_SCNCE"), '"'));
			to.setL_ResolvedReportingStatusSocialStudies(wrap(rs.getString("RESLVD_REPOR_STUS__SOCSTUD"), '"')); // TODO
			to.setL_OPIIPICut1(wrap(rs.getString("OPIIPI_CUT_1"), '"')); // TODO
			to.setL_OPIIPICut2(wrap(rs.getString("OPIIPI_CUT_2"), '"'));
			to.setL_OPIIPICut3(wrap(rs.getString("OPIIPI_CUT_3"), '"'));
			to.setL_OPIIPICut4(wrap(rs.getString("OPIIPI_CUT_4"), '"'));
			to.setL_OPIIPICut5(wrap(rs.getString("OPIIPI_CUT_5"), '"'));
			to.setL_OPIIPICut6(wrap(rs.getString("OPIIPI_CUT_6"), '"'));
			to.setL_OPIIPICut7(wrap(rs.getString("OPIIPI_CUT_7"), '"'));
			to.setL_OPIIPICut8(wrap(rs.getString("OPIIPI_CUT_8"), '"'));
			to.setL_OPIIPICut9(wrap(rs.getString("OPIIPI_CUT_9"), '"'));
			to.setL_OPIIPICut10(wrap(rs.getString("OPIIPI_CUT_10"), '"'));
			to.setL_OPIIPICut11(wrap(rs.getString("OPIIPI_CUT_11"), '"'));
			to.setL_OPIIPICut12(wrap(rs.getString("OPIIPI_CUT_12"), '"'));
			to.setL_OPIIPICut13(wrap(rs.getString("OPIIPI_CUT_13"), '"'));
			to.setL_OPIIPICut14(wrap(rs.getString("OPIIPI_CUT_14"), '"'));
			to.setL_OPIIPICut15(wrap(rs.getString("OPIIPI_CUT_15"), '"'));
			to.setL_OPIIPICut16(wrap(rs.getString("OPIIPI_CUT_16"), '"'));
			to.setL_OPIIPICut17(wrap(rs.getString("OPIIPI_CUT_17"), '"'));
			to.setL_OPIIPICut18(wrap(rs.getString("OPIIPI_CUT_18"), '"'));
			to.setL_OPIIPICut19(wrap(rs.getString("OPIIPI_CUT_19"), '"'));
			to.setL_OPIIPICut20(wrap(rs.getString("OPIIPI_CUT_20"), '"'));
			to.setL_OPIIPICut21(wrap(rs.getString("OPIIPI_CUT_21"), '"'));
			to.setL_OPIIPICut22(wrap(rs.getString("OPIIPI_CUT_22"), '"'));
			to.setL_OPIIPICut23(wrap(rs.getString("OPIIPI_CUT_23"), '"'));
			to.setL_OPIIPICut24(wrap(rs.getString("OPIIPI_CUT_24"), '"'));
			to.setL_OPIIPICut25(wrap(rs.getString("OPIIPI_CUT_25"), '"'));
			to.setL_OPIIPICut26(wrap(rs.getString("OPIIPI_CUT_26"), '"'));
			to.setL_OPIIPICut27(wrap(rs.getString("OPIIPI_CUT_27"), '"'));
			to.setL_OPIIPICut28(wrap(rs.getString("OPIIPI_CUT_28"), '"'));
			to.setL_OPIIPICut29(wrap(rs.getString("OPIIPI_CUT_29"), '"'));
			to.setL_OPIIPICut30(wrap(rs.getString("OPIIPI_CUT_30"), '"'));
			to.setL_OPIIPICut31(wrap(rs.getString("OPIIPI_CUT_31"), '"'));
			to.setL_OPIIPICut32(wrap(rs.getString("OPIIPI_CUT_32"), '"'));
			to.setL_OPIIPICut33(wrap(rs.getString("OPIIPI_CUT_33"), '"'));
			to.setL_OPIIPICut34(wrap(rs.getString("OPIIPI_CUT_34"), '"'));
			to.setL_OPIIPICut35(wrap(rs.getString("OPIIPI_CUT_35"), '"'));
			to.setL_OPIIPICut36(wrap(rs.getString("OPIIPI_CUT_36"), '"'));
			to.setL_OPIIPICut37(wrap(rs.getString("OPIIPI_CUT_37"), '"'));
			to.setL_OPIIPICut38(wrap(rs.getString("OPIIPI_CUT_38"), '"'));
			to.setL_OPIIPICut39(wrap(rs.getString("OPIIPI_CUT_39"), '"'));
			to.setL_OPIIPICut40(wrap(rs.getString("OPIIPI_CUT_40"), '"'));
			to.setL_Form(wrap(rs.getString("FACT.FORM"), '"'));
			to.setL_Filler(wrap(rs.getString("FILLER"), '"'));
			to.setL_FinalFormUsedforScoring(wrap(rs.getString("FORM_FOR_SCORING"), '"'));
			to.setL_TextReaderIndicatorPart1(wrap(rs.getString("FACT.TEXT_READER_INDICATOR_PART_1"), '"'));
			to.setL_TextReaderIndicatorPart2(wrap(rs.getString("FACT.TEXT_READER_INDICATOR_PART_2"), '"'));
			to.setL_ElaCrSession3(wrap(rs.getString("FACT.ELA_CR_SESSION_3"), '"'));
			to.setL_ElaCrSession4bItemResp(wrap(rs.getString("FACT.ELA_CR_SESSION_4B_ITEM_RESP"), '"'));
			to.setL_ElaCrSession5ItemResp(wrap(rs.getString("FACT.ELA_CR_SESSION_5_ITEM_RESP"), '"'));
			to.setL_ElaCrSession6bItemResp(wrap(rs.getString("FACT.ELA_CR_SESSION_6B_ITEM_RESP"), '"'));
			to.setL_MathCrSession2ItemResp(wrap(rs.getString("FACT.MATH_CR_SESSION_2_ITEM_RESP"), '"'));
			to.setL_SciCrSession7ItemResp(wrap(rs.getString("FACT.SCI_CR_SESSION_7_ITEM_RESP"), '"'));
			grtTOResult.add(to);
		}
		return grtTOResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		String type = paramMap.get("type");
		logger.log(IAppLogger.INFO, "type = " + type);
		Long customerId = Long.valueOf((String) paramMap.get("customerId"));
		logger.log(IAppLogger.INFO, "customerId = " + customerId);
		String productId = paramMap.get("productId");
		logger.log(IAppLogger.INFO, "productId = " + productId);
		String testProgram = paramMap.get("testProgram");
		logger.log(IAppLogger.INFO, "testProgram = " + testProgram);
		String districtId = paramMap.get("parentOrgNodeId");
		logger.log(IAppLogger.INFO, "districtId = " + districtId);
		String schoolId = paramMap.get("orgNodeId");
		logger.log(IAppLogger.INFO, "schoolId = " + schoolId);
		if (InorsDownloadConstants.IC.equals(type)) {
			return getICList(productId, testProgram, districtId, schoolId);
		} else if (InorsDownloadConstants.GRT.equals(type)) {
			return getGRTList(customerId, productId, testProgram, districtId, schoolId);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getTableData(java.util.Map, java.util.ArrayList)
	 */
	public ArrayList<ArrayList<String>> getTabulerData(Map<String, String> paramMap, ArrayList<String> aliasList, ArrayList<String> headerList) {
		String type = paramMap.get("type");
		logger.log(IAppLogger.INFO, "type = " + type);
		String customerId = paramMap.get("customerId");
		logger.log(IAppLogger.INFO, "customerId = " + customerId);
		String productId = paramMap.get("productId");
		logger.log(IAppLogger.INFO, "productId = " + productId);
		String testProgram = paramMap.get("testProgram");
		logger.log(IAppLogger.INFO, "testProgram = " + testProgram);
		String districtId = paramMap.get("parentOrgNodeId");
		logger.log(IAppLogger.INFO, "districtId = " + districtId);
		String schoolId = paramMap.get("orgNodeId");
		logger.log(IAppLogger.INFO, "schoolId = " + schoolId);
		if (InorsDownloadConstants.IC.equals(type)) {
			return getICTableData(paramMap, aliasList, headerList);
		} else if (InorsDownloadConstants.GRT.equals(type)) {
			return getGRTTableData(paramMap, aliasList, headerList);
		} else {
			return null;
		}
	}

	/**
	 * Fetches the IC list from the database. All field level data are wrapped using double quotes. Field level data validations may apply to specific field to support multiple layouts. Setter methods
	 * set valid data for that layout to achieve better performance and to avoid multiple getter or setter calls.
	 * 
	 * @param orgNodeId
	 * @param year
	 * @param year2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors',  #p0, #p1, #p2, #p3, 'getICList' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc',  #p0, #p1, #p2, #p3, 'getICList' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo',  #p0, #p1, #p2, #p3, 'getICList' )")
	} )
	private List<InvitationCodeTO> getICList(final String productId, final String testProgram, final String districtId, final String schoolId) {
		logger.log(IAppLogger.INFO, "Enter: getICList()");
		List<InvitationCodeTO> icList = null;
		try {
			if ("-1".equals(schoolId)) {
				logger.log(IAppLogger.INFO, "All Schools");
				icList = (List<InvitationCodeTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_ALL_IC);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, Long.parseLong(districtId));
						cs.setLong(3, Long.parseLong(testProgram));
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<InvitationCodeTO> icTOList = new ArrayList<InvitationCodeTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(4);
							icTOList = getICTOList(rs);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return icTOList;
					}
				});
			} else {
				logger.log(IAppLogger.INFO, "schoolId=" + schoolId);
				icList = (List<InvitationCodeTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_IC);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, Long.parseLong(schoolId));
						cs.setLong(3, Long.parseLong(testProgram));
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						List<InvitationCodeTO> icTOList = new ArrayList<InvitationCodeTO>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(4);
							icTOList = getICTOList(rs);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return icTOList;
					}
				});
			}
		} finally {
			logger.log(IAppLogger.INFO, "icList.size(): " + icList.size());
		}
		logger.log(IAppLogger.INFO, "Exit: getICList()");
		return icList;
	}
	
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getICTableData'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).listKey(#aliasList)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#headerList)),'inors' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getICTableData'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).listKey(#aliasList)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#headerList)),'tasc' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getICTableData'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).listKey(#aliasList)).concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#headerList)),'usmo' )")
	} )
	private ArrayList<ArrayList<String>> getICTableData(Map<String, String> paramMap, final ArrayList<String> aliasList, final ArrayList<String> headerList) {
		logger.log(IAppLogger.INFO, "Enter: getICList()");
		final String userName = paramMap.get("userName");
		final String productId = paramMap.get("productId");
		final String testProgram = paramMap.get("testProgram");
		final String districtId = paramMap.get("parentOrgNodeId");
		final String schoolId = paramMap.get("orgNodeId");

		logger.log(IAppLogger.INFO, "userName = " + userName);
		logger.log(IAppLogger.INFO, "productId = " + productId);
		logger.log(IAppLogger.INFO, "testProgram = " + testProgram);
		logger.log(IAppLogger.INFO, "districtId = " + districtId);
		logger.log(IAppLogger.INFO, "schoolId = " + schoolId);
		ArrayList<ArrayList<String>> icList = null;
		try {
			if ("-1".equals(schoolId)) {
				logger.log(IAppLogger.INFO, "All Schools");
				icList = (ArrayList<ArrayList<String>>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_ALL_IC);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, Long.parseLong(districtId));
						cs.setLong(3, Long.parseLong(testProgram));
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						ArrayList<ArrayList<String>> icTOList = new ArrayList<ArrayList<String>>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(4);
							icTOList = InorsDownloadUtil.getTableDataFromResultSet(rs, aliasList, headerList);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return icTOList;
					}
				});
			} else {
				logger.log(IAppLogger.INFO, "schoolId=" + schoolId);
				icList = (ArrayList<ArrayList<String>>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement cs = con.prepareCall(IQueryConstants.GET_IC);
						cs.setLong(1, Long.parseLong(productId));
						cs.setLong(2, Long.parseLong(schoolId));
						cs.setLong(3, Long.parseLong(testProgram));
						cs.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
						cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public Object doInCallableStatement(CallableStatement cs) {
						ResultSet rs = null;
						ArrayList<ArrayList<String>> icTOList = new ArrayList<ArrayList<String>>();
						try {
							cs.execute();
							rs = (ResultSet) cs.getObject(4);
							icTOList = InorsDownloadUtil.getTableDataFromResultSet(rs, aliasList, headerList);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return icTOList;
					}
				});
			}
		} finally {
			logger.log(IAppLogger.INFO, "icList.size(): " + icList.size());
		}
		logger.log(IAppLogger.INFO, "Exit: getICList()");
		return icList;
	}

	/**
	 * Creates a List of InvitationCodeTO from a ResultSet
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<InvitationCodeTO> getICTOList(ResultSet rs) throws SQLException {
		List<InvitationCodeTO> icTOList = new ArrayList<InvitationCodeTO>();
		while (rs.next()) {
			InvitationCodeTO to = new InvitationCodeTO();
			to.setCorporationorDioceseName(wrap(rs.getString("DISTRICT_NAME"), '"'));
			to.setCorporationorDioceseNumber(wrap(rs.getString("DISTRICT_NUMBER"), '"'));
			to.setSchoolName(wrap(rs.getString("SCHOOL_NAME"), '"'));
			to.setSchoolNumber(wrap(rs.getString("SCHOOL_NUMBER"), '"'));
			to.setGrade(wrap(rs.getString("GRADE_NAME"), '"'));
			to.setAdministrationName(wrap(rs.getString("PRODUCT_NAME"), '"')); // TODO
			to.setISTEPInvitationCode(wrap(rs.getString("INVITATION_CODE"), '"'));
			to.setInvitationCodeExpirationDate(wrap(rs.getString("EXPIRATION_DATE"), '"'));
			to.setStudentLastName(wrap(rs.getString("LAST_NAME"), '"'));
			to.setStudentFirstName(wrap(rs.getString("FIRST_NAME"), '"'));
			to.setStudentMiddleInitial(wrap(rs.getString("MIDDLE_NAME"), '"'));
			to.setStudentsGender(wrap(rs.getString("GENDER_CODE"), '"'));
			to.setBirthDate(wrap(rs.getString("BIRTHDATE"), '"'));
			to.setStudentTestNumber(wrap(rs.getString("TEST_ELEMENT_ID"), '"'));
			to.setCorporationStudentID(wrap(rs.getString("EXT_STUDENT_ID"), '"'));
			to.setCTBUSEBarcodeID(wrap(rs.getString("BARCODE_ID_MULTIPLE_CHOICE"), '"'));
			to.setTeacherName(wrap(rs.getString("TEACHER_NAME"), '"')); // TODO
			to.setCTBUSEOrgtstgpgm(wrap(rs.getString("ORGTSTGPGM"), '"'));
			to.setCTBUSETeacherElementNumber(wrap(rs.getString("TEACHER_ELEMENT"), '"')); // TODO
			to.setCTBUSEStudentElementNumber(wrap(rs.getString("STUDENT_ELEMENT_NUMBER"), '"')); // TODO
			icTOList.add(to);
		}
		return icTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getDistricts()
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'populateDistrictGrt'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'inors' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'populateDistrictGrt'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'tasc' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'populateDistrictGrt'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'usmo' )")
	} )
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGrt(Map<String, String> paramMap) {
		long t1 = System.currentTimeMillis();
		final Long testProgram = Long.parseLong(paramMap.get("testProgram"));
		logger.log(IAppLogger.INFO, "testProgram: " + testProgram);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> districtList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
		try {
			districtList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.GET_DISTRICTS);
					cs.setLong(1, testProgram);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						com.ctb.prism.core.transferobject.ObjectValueTO to = null;
						while (rs.next()) {
							to = new com.ctb.prism.core.transferobject.ObjectValueTO();
							to.setValue(rs.getString(1));
							to.setName(rs.getString(2));
							to.setOther(rs.getString(3));
							objectValueList.add(to);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return objectValueList;
				}
			});
			logger.log(IAppLogger.INFO, "Districts: " + districtList.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: getDistricts() took time: " + CustomStringUtil.getHMSTimeFormat(t2 - t1));
		}
		return districtList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#populateSchool(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'populateSchoolGrt'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'inors' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'populateSchoolGrt'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'tasc' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'populateSchoolGrt'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'usmo' )")
	} )
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGrt(Map<String, String> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: populateSchoolGrt()");
		long t1 = System.currentTimeMillis();
		final Long testProgram = Long.parseLong(paramMap.get("testProgram"));
		logger.log(IAppLogger.INFO, "testProgram: " + testProgram);
		final Long districtId = Long.parseLong(paramMap.get("districtId"));
		logger.log(IAppLogger.INFO, "districtId: " + districtId);
		List<com.ctb.prism.core.transferobject.ObjectValueTO> schoolList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
		try {
			schoolList = (List<com.ctb.prism.core.transferobject.ObjectValueTO>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.GET_SCHOOLS);
					cs.setLong(1, districtId);
					cs.setLong(2, testProgram);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					List<com.ctb.prism.core.transferobject.ObjectValueTO> objectValueList = new ArrayList<com.ctb.prism.core.transferobject.ObjectValueTO>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(3);
						com.ctb.prism.core.transferobject.ObjectValueTO to = null;
						while (rs.next()) {
							to = new com.ctb.prism.core.transferobject.ObjectValueTO();
							to.setValue(rs.getString("SCHOOL_ID"));
							to.setName(rs.getString("SCHOOL_NAME"));
							objectValueList.add(to);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return objectValueList;
				}
			});
			logger.log(IAppLogger.INFO, "Schools: " + schoolList.size());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: populateSchoolGrt() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return schoolList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getProductNameById(java.lang.Long)
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors', #p0, 'getProductNameById' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc', #p0, 'getProductNameById' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo', #p0, 'getProductNameById' )")
	} )
	public String getProductNameById(Long productId) {
		String productName = null;
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_PRODUCT_NAME_BY_ID, productId);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				productName = fieldDetails.get("PRODUCT_NAME").toString();
			}
		}
		return productName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getCurrentAdminYear()
	 */
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('inors', 'getCurrentAdminYear' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('tasc', 'getCurrentAdminYear' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).generateKey('usmo', 'getCurrentAdminYear' )")
	} )
	public String getCurrentAdminYear() {
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		String currentAdminYear = year.toString();
		List<Map<String, Object>> lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_CURRENT_ADMIN_YEAR);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				try {
					int i = Integer.parseInt(fieldDetails.get("ADMIN_YEAR").toString());
					currentAdminYear = String.valueOf(i);
				} catch (Exception e) {
					logger.log(IAppLogger.WARN, "Check the Current Admin Query. Returning System Year.");
				}
			}
		}
		return currentAdminYear;
	}
	
	/**
	 * @author Joy Kumar Pal
	 * @param paramMap
	 * @return returnMap
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getCode'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'inors' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getCode'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'tasc' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getCode'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'usmo')")
	} )
	public Map<String,Object> getCode(Map<String,Object> paramMap){
		logger.log(IAppLogger.INFO, "Enter: getCode()");
		long t1 = System.currentTimeMillis();
		
		final long districtId = Long.parseLong((String) paramMap.get("district"));
		final long schoolId = Long.parseLong((String) paramMap.get("school"));
		final String contractName = (String) paramMap.get("contractName");
		
		logger.log(IAppLogger.INFO, "districtId: " + districtId);
		logger.log(IAppLogger.INFO, "schoolId: " + schoolId);
		
		Map<String,Object> returnMap = null;
		try {
			returnMap = (Map<String,Object>) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.GET_CODE);
					cs.setLong(1, districtId);
					cs.setLong(2, schoolId);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					Map<String,Object> returnMapRs = new HashMap<String,Object>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(3);
						if(rs.next()) {
							returnMapRs.put("districtCode", rs.getString("DISTRICT_CODE"));
							returnMapRs.put("schoolCode", rs.getString("SCHOOL_CODE"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return returnMapRs;
				}
			});
			logger.log(IAppLogger.INFO, "paramMap: " + paramMap.size());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: getCode() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnMap;
	}
	
	/**
	 * @author Joy Kumar Pal
	 * @param paramMap
	 * @return returnMap
	 */
	@SuppressWarnings("unchecked")
	@Caching( cacheable = {
			@Cacheable(value = "inorsDefaultCache", condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'inors'", key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getCode'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'inors' )"),
			@Cacheable(value = "tascDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'tasc'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getCode'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'tasc' )"),
			@Cacheable(value = "usmoDefaultCache",  condition="T(com.ctb.prism.core.util.CacheKeyUtils).fetchContract() == 'usmo'",  key="T(com.ctb.prism.core.util.CacheKeyUtils).encryptedKey( 'getCode'.concat(T(com.ctb.prism.core.util.CacheKeyUtils).mapKey(#paramMap)),'usmo' )")
	} )
	public Map<String,Object> getTpCode(Map<String,Object> paramMap){
		logger.log(IAppLogger.INFO, "Enter: getTpCode()");
		long t1 = System.currentTimeMillis();
		
		final long custProdId = Long.parseLong((String) paramMap.get("custProdId"));
		
		logger.log(IAppLogger.INFO, "custProdId: " + custProdId);
		
		Map<String,Object> returnMap = null;
		try {
			returnMap = (Map<String,Object>) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(IQueryConstants.GET_TP_CODE);
					cs.setLong(1, custProdId);
					cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					Map<String,Object> returnMapRs = new HashMap<String,Object>();
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(2);
						if(rs.next()) {
							returnMapRs.put("tpCode", rs.getString("TP_CODE"));
							returnMapRs.put("productId", rs.getString("PRODUCTID"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return returnMapRs;
				}
			});
			logger.log(IAppLogger.INFO, "paramMap: " + paramMap.size());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: getTpCode() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return returnMap;
	}
	
	/*
	 * Insert pdf log for PDF utility
	 * @param StudentPDFLogTO
	 * @param contractName
	 */
	public void auditPDFUtiity(StudentPDFLogTO studentPDFLogTO,String contractName) {
		logger.log(IAppLogger.INFO, "student:" + studentPDFLogTO.getStudentBioId());
		logger.log(IAppLogger.INFO, "subtest:" + studentPDFLogTO.getSubtestId());
		logger.log(IAppLogger.INFO, "school:" + studentPDFLogTO.getOrgNodeId());
		logger.log(IAppLogger.INFO, "ISR File:" + studentPDFLogTO.getFileName());

		getJdbcTemplatePrism(contractName).update(IQueryConstants.CREATE_STUDENT_PDF_LOG,
				studentPDFLogTO.getStudentBioId(),
				studentPDFLogTO.getSubtestId(),
				studentPDFLogTO.getOrgNodeId(),
				studentPDFLogTO.getFileName());		
	}
	

}
