package com.ctb.prism.inors.dao;

import static com.ctb.prism.inors.constant.InorsDownloadConstants.YEAR_2010;
import static com.ctb.prism.inors.constant.InorsDownloadConstants.YEAR_2011;
import static com.ctb.prism.inors.constant.InorsDownloadConstants.YEAR_2012;
import static com.ctb.prism.inors.constant.InorsDownloadConstants.YEAR_2013;
import static com.ctb.prism.inors.util.InorsDownloadUtil.wrap;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.constant.InorsDownloadConstants;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.GrtTO;
import com.ctb.prism.inors.transferobject.InvitationCodeTO;
import com.ctb.prism.inors.transferobject.ObjectValueTO;
import com.ctb.prism.inors.util.InorsDownloadUtil;

/**
 * This class is responsible for reading and writing to database.
 * The transactions through this class should be related to report only.
 */
@Repository("inorsDAO")
public class InorsDAOImpl extends BaseDAO implements IInorsDAO {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsDAOImpl.class.getName());
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#createJob(com.ctb.prism.inors.
	 * transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO) {
		long jobId = getJdbcTemplatePrism().queryForLong(IQueryConstants.JOB_SEQ_ID);
		getJdbcTemplatePrism().update(IQueryConstants.CREATE_JOB, 
				jobId,
				bulkDownloadTO.getFileName(),
				bulkDownloadTO.getQuerysheetFile(),
				bulkDownloadTO.getUdatedBy(),
				bulkDownloadTO.getSelectedNodes(),
				bulkDownloadTO.getStudentCount(),
				bulkDownloadTO.getEmail(),
				bulkDownloadTO.getReportUrl(),
				bulkDownloadTO.getTestAdministration(),
				bulkDownloadTO.getCustomerId(),
				bulkDownloadTO.getGrade(),
				bulkDownloadTO.getRequestType(),
				bulkDownloadTO.getGroupFile(),
				bulkDownloadTO.getCollationHierarchy(),
				bulkDownloadTO.getDownloadMode());
		bulkDownloadTO.setJobId(jobId);	
		return bulkDownloadTO;
	} 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#updateStatus(com.ctb.prism.inors.
	 * transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_STATUS, 
				bulkDownloadTO.getStatus(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#updateJobLog(com.ctb.prism.inors.
	 * transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_LOG, 
				bulkDownloadTO.getLog(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 
	
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.dao.IInorsDAO#updateJobStatusAnsLog(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_STATUS_AND_LOG, 
				bulkDownloadTO.getStatus(),
				bulkDownloadTO.getLog(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#updateJob(com.ctb.prism.inors.
	 * transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_JOB, 
				bulkDownloadTO.getStatus(),
				bulkDownloadTO.getFileLocation(),
				bulkDownloadTO.getFileSize(),
				bulkDownloadTO.getLog(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getJob(java.lang.String)
	 */
	public BulkDownloadTO getJob(String jobId) {
		BulkDownloadTO bulkDownloadTO = null;
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.JOB_DETAILS, jobId);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				bulkDownloadTO = new BulkDownloadTO();
				bulkDownloadTO.setJobId( ((BigDecimal) (fieldDetails.get("ID"))).longValue() );
				bulkDownloadTO.setFileName((String) (fieldDetails.get("FILE_NAME")));
				bulkDownloadTO.setFileLocation((String) (fieldDetails.get("LOC")));
				bulkDownloadTO.setQuerysheetFile((String) (fieldDetails.get("QSHEET")));
				bulkDownloadTO.setUdatedBy(((BigDecimal) (fieldDetails.get("userid"))).longValue());
				bulkDownloadTO.setSelectedNodes((String) (fieldDetails.get("DETAILS")));
				bulkDownloadTO.setStatus((String) (fieldDetails.get("status")));
				bulkDownloadTO.setPercentageDone(((BigDecimal) (fieldDetails.get("PERCT"))).toString());
				bulkDownloadTO.setRequestedDate(((java.sql.Timestamp)(fieldDetails.get("REQ_DT"))).toString());
				bulkDownloadTO.setCompletedDate( (fieldDetails.get("COMP_DT") != null) ? ((java.sql.Timestamp)(fieldDetails.get("COMP_DT"))).toString() : null);
				bulkDownloadTO.setFileSize((String) (fieldDetails.get("SIZ")));
				bulkDownloadTO.setEmail((String) (fieldDetails.get("email")));
				bulkDownloadTO.setCustomerId(((BigDecimal) (fieldDetails.get("customerid"))).toString());
				bulkDownloadTO.setGrade( (fieldDetails.get("gradeid") != null) ? ((BigDecimal) (fieldDetails.get("gradeid"))).toString() : "");
				bulkDownloadTO.setTestAdministration( (fieldDetails.get("adminid") != null) ? ((BigDecimal) (fieldDetails.get("adminid"))).toString() : "");
				bulkDownloadTO.setReportUrl((String) (fieldDetails.get("URL")));
				bulkDownloadTO.setRequestType((String) (fieldDetails.get("TYPE")));
				bulkDownloadTO.setLog((String) (fieldDetails.get("log")));
				bulkDownloadTO.setDownloadMode((String) (fieldDetails.get("D_TYPE")));
			}
		}
		return bulkDownloadTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getSchoolClass(java.lang.String)
	 */
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
	
	@SuppressWarnings("unchecked")
	private List<GrtTO> getGRTList(final String orgNodeId, final String parentOrgNodeId) {
		long t1 = System.currentTimeMillis();
		List<GrtTO> grtList = null;
		try {
			grtList = (List<GrtTO>) getJdbcTemplatePrism().execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.GET_RESULTS_GRT);
							cs.setLong(1, Long.parseLong(parentOrgNodeId));
							cs.setLong(2, Long.parseLong(orgNodeId));
							cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							List<GrtTO> grtTOResult = new ArrayList<GrtTO>();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(3);
								GrtTO to = null;
								while (rs.next()) {
									to = new GrtTO();
									to.setL_TapeMode(wrap(rs.getString("TAPE_MODE"), '"'));
									to.setL_Orgtstgpgm(wrap(rs.getString("ORGTSTGPGM"), '"'));
									to.setL_CorporationorDioceseName(wrap("", '"')); // TODO
									to.setL_CorporationorDioceseNumber(wrap("", '"')); // TODO
									to.setL_SchoolName(wrap("", '"')); // TODO
									to.setL_SchoolNumber(wrap("", '"')); // TODO
									to.setL_TeacherName(wrap(rs.getString("TEACHER_NAME"), '"'));
									to.setL_TeacherElementNumberCTBUse(wrap(rs.getString("TEACHER_ELEMENT"), '"'));
									to.setL_Grade(wrap("", '"')); // TODO
									to.setL_City(wrap(rs.getString("CITY"), '"'));
									to.setL_State(wrap(rs.getString("STATE"), '"'));
									to.setL_ISTEPTestName(wrap(rs.getString("ISTEP_TEST_NAME"), '"')); // ISTEP_TEST_NAME
									to.setL_ISTEPBook(wrap(rs.getString("ISTEP_BOOK_NUM"), '"')); // ISTEP_BOOK_NUM
									to.setL_ISTEPForm(wrap(rs.getString("ISTEP_FORM"), '"')); // ISTEP_FORM
									to.setL_TestDateMMDDYY(wrap(rs.getString("TEST_DATE"), '"')); // TEST_DATE
									to.setL_StudentLastName(wrap(rs.getString("STUDENT_LAST_NAME"), '"'));
									to.setL_StudentFirstName(wrap(rs.getString("STUDENT_FIRST_NAME"), '"'));
									to.setL_StudentMiddleInitial(wrap(rs.getString("STUDENT_MIDDLE_INITIAL"), '"'));
									to.setL_StudentsGender(wrap(rs.getString("STUDENTS_GENDER"), '"'));
									to.setL_BirthDateMMDDYY(wrap(rs.getString("BIRTH_DATE"), '"'));
									to.setL_ChronologicalAgeInMonths(wrap(rs.getString("CHRONOLOGICAL_AGE_IN_MONTHS"), '"'));
									to.setL_Ethnicity(wrap(rs.getString("ETHNICITY_K"), '"'));
									to.setL_RaceAmericanIndianAlaskaNative(wrap(rs.getString("RACE_AMERICAN_INDIAN"), '"')); // RACE_AMERICAN_INDIAN
									to.setL_RaceAsian(wrap(rs.getString("RACE_ASIAN"), '"')); // RACE_ASIAN
									to.setL_RaceBlackorAfricanAmerican(wrap(rs.getString("RACE_BLACK"), '"')); // RACE_BLACK
									to.setL_RaceNativeHawaiianorOtherPacificIslander(wrap(rs.getString("RACE_PACIFIC_ISLANDER"), '"')); // RACE_PACIFIC_ISLANDER
									to.setL_RaceWhite(wrap(rs.getString("RACE_WHITE"), '"')); // RACE_WHITE
									to.setL_Filler1(wrap("", '"')); // TODO
									to.setL_StudentTestNumberAI(wrap(rs.getString("STUDENT_TEST_AI"), '"')); // STUDENT_TEST_AI
									to.setL_SpecialCodeJ(wrap("", '"')); // TODO
									to.setL_ResolvedEthnicityK(wrap("", '"')); // TODO
									to.setL_SpecialEducationL(wrap(rs.getString("SPECIAL_EDUCATION_L"), '"')); // SPECIAL_EDUCATION_L
									to.setL_ExceptionalityM(wrap("", '"')); // TODO
									to.setL_SocioEconomicStatusN(wrap(rs.getString("SOCIOECONOMIC_STATUS_N"), '"')); // SOCIOECONOMIC_STATUS_N
									to.setL_Section504O(wrap(rs.getString("SECTION_504_O"), '"')); // SECTION_504_O
									to.setL_EnglishLearnerELP(wrap("", '"')); // TODO - 
									to.setL_MigrantQ(wrap(rs.getString("MIGRANT_Q"), '"')); // MIGRANT_Q
									to.setL_LocaluseR(wrap(rs.getString("LOCAL_USE_R"), '"')); // LOCAL_USE_R
									to.setL_LocaluseS(wrap(rs.getString("LOCAL_USE_S"), '"')); // LOCAL_USE_S
									to.setL_LocaluseT(wrap(rs.getString("LOCAL_USE_T"), '"')); // LOCAL_USE_T
									to.setL_MatchUnmatchU(wrap(rs.getString("MATCH_UNMATCH_U"), '"')); // MATCH_UNMATCH_U
									to.setL_DuplicateV(wrap(rs.getString("DUPLICATE_V"), '"')); // DUPLICATE_V
									to.setL_CTBUseOnlyW(wrap("", '"')); // TODO - 
									to.setL_SpecialCodeX(wrap(rs.getString("SPECIAL_CODE_X"), '"')); // SPECIAL_CODE_X
									to.setL_SpecialCodeY(wrap(rs.getString("SPECIAL_CODE_Y"), '"')); // SPECIAL_CODE_Y
									to.setL_SpecialCodeZ(wrap(rs.getString("SPECIAL_CODE_Z"), '"')); // SPECIAL_CODE_Z
									to.setL_AccommodationsEla(wrap(rs.getString("ACCOMMODATIONS_ELA_P"), '"')); // ACCOMMODATIONS_ELA_P
									to.setL_AccommodationsMath(wrap(rs.getString("ACCOMMODATIONS_MATH_Q"), '"')); // ACCOMMODATIONS_MATH_Q
									to.setL_AccommodationsScience(wrap(rs.getString("ACCOMMODATIONS_SCIENCE_X"), '"')); // ACCOMMODATIONS_SCIENCE_X
									to.setL_AccommodationsSocialStudies(wrap(rs.getString("ACCOMMODATIONS_SOCIAL"), '"')); // ACCOMMODATIONS_SOCIAL
									to.setL_CorporationUseID(wrap(rs.getString("CORPORATION_USE_ID"), '"')); // CORPORATION_USE_ID
									to.setL_CustomerUse(wrap(rs.getString("CUSTOMER_USE"), '"')); // CUSTOMER_USE
									to.setL_Filler2(wrap(rs.getString("FILLER2"), '"')); // FILLER2
									to.setL_Filler3(wrap(rs.getString("FILLER3"), '"')); // FILLER3
									to.setL_ElaPFIndicator(wrap(rs.getString("ELA_PF_INDICATOR"), '"')); // ELA_PF_INDICATOR
									to.setL_MathPFIndicator(wrap(rs.getString("MATH_PF_INDICATOR"), '"')); // MATH_PF_INDICATOR
									to.setL_SciencePFIndicator(wrap(rs.getString("SCIENCE_PF_INDICATOR"), '"')); // SCIENCE_PF_INDICATOR
									to.setL_SocialStudiesPFIndicator(wrap(rs.getString("SOCIAL_PF_INDICATOR"), '"')); // SOCIAL_PF_INDICATOR
									to.setL_ElaNumberCorrect(wrap(rs.getString("ENGLANG_ARTS_NUM_CORRECT"), '"')); // ENGLANG_ARTS_NUM_CORRECT
									to.setL_MathNumberCorrect(wrap(rs.getString("MATHEMATICS_NUM_CORRECT"), '"')); // MATHEMATICS_NUM_CORRECT
									to.setL_ScienceNumberCorrect(wrap(rs.getString("SCIENCE_NUM_CORRECT"), '"')); // SCIENCE_NUM_CORRECT
									to.setL_SocialStudiesNumberCorrect(wrap(rs.getString("SOCIAL_NUM_CORRECT"), '"')); // SOCIAL_NUM_CORRECT
									to.setL_ElaScaleScore(wrap(rs.getString("ENGLAN_ARTS_SCALE_SCORE"), '"')); // ENGLAN_ARTS_SCALE_SCORE
									to.setL_MathScaleScore(wrap(rs.getString("MATHEMATICS_SCALE_SCORE"), '"')); // MATHEMATICS_SCALE_SCORE
									to.setL_ScienceScaleScore(wrap(rs.getString("SCIENCE_SCALE_SCORE"), '"')); // SCIENCE_SCALE_SCORE
									to.setL_SocialStudiesScaleScore(wrap(rs.getString("SOCIAL_SCALE_SCORE"), '"')); // SOCIAL_SCALE_SCORE
									to.setL_ElaScaleScoreSEM(wrap(rs.getString("ENGLAN_ARTS_SCALE_SCORE_SEM"), '"')); // ENGLAN_ARTS_SCALE_SCORE_SEM
									to.setL_MathScaleScoreSEM(wrap(rs.getString("MATHEMATICS_SCALE_SCORE_SEM"), '"')); // MATHEMATICS_SCALE_SCORE_SEM
									to.setL_ScienceScaleScoreSEM(wrap(rs.getString("SCIENCE_SCALE_SCORE_SEM"), '"')); // SCIENCE_SCALE_SCORE_SEM
									to.setL_SocialStudiesScaleScoreSEM(wrap(rs.getString("SOCIAL_SCALE_SCORE_SEM"), '"')); // SOCIAL_SCALE_SCORE_SEM
									to.setL_MasteryIndicator1(wrap(rs.getString("MASTERY_INDICATOR_1"), '"')); // MASTERY_INDICATOR_1
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
									to.setL_ELACRSession2ItemResponses(wrap(rs.getString("ELA_CR_SESSION_2"), '"')); // ELA_CR_SESSION_2
									to.setL_ELACRSession3ItemResponses(wrap(rs.getString("ELA_CR_SESSION_3"), '"')); // ELA_CR_SESSION_3
									to.setL_MathCRSession1ItemResponses(wrap(rs.getString("MATH_CR_SESSION_1"), '"')); // MATH_CR_SESSION_1
									to.setL_ScienceCRSession4ItemResponses(wrap("", '"')); // TODO
									to.setL_SocialStudiesCRSession4ItemResponses(wrap("", '"')); // TODO
									to.setL_Filler4(wrap(rs.getString("FILLER4"), '"')); // FILLER4
									to.setL_SchoolPersonnelNumberSPN1(wrap(rs.getString("SPN_1"), '"'));
									to.setL_SchoolPersonnelNumberSPN2(wrap(rs.getString("SPN_2"), '"'));
									to.setL_SchoolPersonnelNumberSPN3(wrap(rs.getString("SPN_3"), '"'));
									to.setL_SchoolPersonnelNumberSPN4(wrap(rs.getString("SPN_4"), '"'));
									to.setL_SchoolPersonnelNumberSPN5(wrap(rs.getString("SPN_5"), '"'));
									to.setL_Filler5(wrap("", '"')); // TODO
									to.setL_CGRComputerGeneratedResponsefrom(wrap(rs.getString("CGR"), '"'));
									to.setL_CTBUseOnlyAppliedSkillsPPImageID(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyAppliedSkillsOASImageID(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyMCPPImagingid(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyMCOASimagingid(wrap("", '"')); // TODO
									to.setL_BarcodeIDAppliedSkills(wrap("", '"')); // TODO
									to.setL_BarcodeIDMultipleChoice(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyTestForm(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyMCBlankbookFlag(wrap(rs.getString("MC_BLANK_BOOK_FLAG"), '"'));
									to.setL_CTBUseOnlyTestFormAppliedSkillsFieldPilotTest(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyTestFormMCFieldPilotTest(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyFutureUseOASTestedIndicatorAppliedSkillstest(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyOASTestedIndicatorMultipleChoiceTest(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyStructureLevel(wrap("", '"')); // TODO
									to.setL_CTBUseOnlyElementNumber(wrap("", '"')); // TODO
									to.setL_ResolvedReportingStatusEla(wrap(rs.getString("RESOLVED_REPORTING_STATUS_ELA"), '"'));
									to.setL_ResolvedReportingStatusMath(wrap(rs.getString("RESOLVED_REPORTING_STATUS_MATH"), '"'));
									to.setL_ResolvedReportingStatusScience(wrap(rs.getString("RESLVD_REPOR_STUS_SCNCE"), '"'));
									to.setL_ResolvedReportingStatusSocialStudies(wrap("", '"')); // TODO
									to.setL_OPIIPICut1(wrap(rs.getString("OPIIPI_CUT_1"), '"'));
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
									grtTOResult.add(to);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return grtTOResult;
						}
					});
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: InorsDAOImpl - getGRTList() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return grtList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		String type = paramMap.get("type");
		String year = paramMap.get("year");
		String parentOrgNodeId = paramMap.get("parentOrgNodeId");
		String orgNodeId = paramMap.get("orgNodeId");
		logger.log(IAppLogger.INFO, "type=" + type + ", year=" + year + ", parentOrgNodeId=" + parentOrgNodeId + ", orgNodeId=" + orgNodeId);
		if (InorsDownloadConstants.IC.equals(type)) {
			List<InvitationCodeTO> icData = new ArrayList<InvitationCodeTO>();
			if (YEAR_2012.equals(year) || YEAR_2013.equals(year)) {
				// TODO : database code instead of mock objects
				icData = getICList(orgNodeId);
			}
			return icData;
		} else if (InorsDownloadConstants.GRT.equals(type)) {
			List<GrtTO> grtData = new ArrayList<GrtTO>();
			if (YEAR_2010.equals(year) || YEAR_2011.equals(year) || YEAR_2012.equals(year) || YEAR_2013.equals(year)) {
				grtData = getGRTList(orgNodeId, parentOrgNodeId);
			}
			return grtData;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<InvitationCodeTO> getICList(final String orgNodeId) {

		long t1 = System.currentTimeMillis();
		List<InvitationCodeTO> icList = null;
		try {
			icList = (List<InvitationCodeTO>) getJdbcTemplatePrism().execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.GET_IC);
							cs.setLong(1, Long.parseLong(orgNodeId));
							cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							List<InvitationCodeTO> icTOList = new ArrayList<InvitationCodeTO>();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(2);
								InvitationCodeTO to = null;
								while (rs.next()) {
									to = new InvitationCodeTO();
									to.setCorporationorDioceseName(wrap(rs.getString("DISTRICT_NAME"), '"'));
									to.setCorporationorDioceseNumber(wrap(rs.getString("DISTRICT_NUMBER"), '"'));
									to.setSchoolName(wrap(rs.getString("SCHOOL_NAME"), '"'));
									to.setSchoolNumber(wrap(rs.getString("SCHOOL_NUMBER"), '"'));
									to.setGrade(wrap(rs.getString("GRADE_NAME"), '"'));
									to.setAdministrationName(wrap("", '"')); // TODO
									to.setiSTEPInvitationCode(wrap(rs.getString("INVITATION_CODE"), '"'));
									to.setInvitationCodeExpirationDate(wrap(rs.getString("EXPIRATION_DATE"), '"'));
									to.setStudentLastName(wrap(rs.getString("LAST_NAME"), '"'));
									to.setStudentFirstName(wrap(rs.getString("FIRST_NAME"), '"'));
									to.setStudentMiddleInitial(wrap(rs.getString("MIDDLE_NAME"), '"'));
									to.setStudentsGender(wrap(rs.getString("GENDER_CODE"), '"'));
									to.setBirthDate(wrap(rs.getString("BIRTHDATE"), '"'));
									to.setStudentTestNumber(wrap(rs.getString("TEST_ELEMENT_ID"), '"'));
									to.setCorporationStudentID(wrap(rs.getString("EXT_STUDENT_ID"), '"'));
									to.setcTBUSEBarcodeID(wrap(rs.getString("BARCODE"), '"'));
									to.setTeacherName(wrap("", '"')); // TODO
									to.setcTBUSEOrgtstgpgm(wrap(rs.getString("TP_CODE"), '"'));
									to.setcTBUSETeacherElementNumber(wrap("", '"')); // TODO
									to.setcTBUSEStudentElementNumber(wrap("", '"')); // TODO
									icTOList.add(to);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return icTOList;
						}
					});
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentDAOImpl - getCustomerProduct() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return icList;
	
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getDistricts()
	 */
	@SuppressWarnings("unchecked")
	public List<ObjectValueTO> getDistricts() {
		/*List<ObjectValueTO> objectValueList = new ArrayList<ObjectValueTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_DISTRICTS);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				ObjectValueTO objectValueTO = new ObjectValueTO();
				objectValueTO.setName(fieldDetails.get("ORG_NODE_NAME").toString());
				objectValueTO.setValue(fieldDetails.get("ORG_NODEID").toString());
				objectValueList.add(objectValueTO);
			}
		}
		return objectValueList;*/

		long t1 = System.currentTimeMillis();
		List<ObjectValueTO> districtList = null;
		try {
			districtList = (List<ObjectValueTO>) getJdbcTemplatePrism().execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.GET_DISTRICTS);
							cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							List<ObjectValueTO> objectValueList = new ArrayList<ObjectValueTO>();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(1);
								ObjectValueTO to = null;
								while (rs.next()) {
									to = new ObjectValueTO();
									to.setValue(rs.getString("ORG_NODEID"));
									to.setName(rs.getString("ORG_NODE_NAME"));
									objectValueList.add(to);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return objectValueList;
						}
					});
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: InorsDAOImpl - getDistricts() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return districtList;
	
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.dao.IInorsDAO#populateSchool(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ObjectValueTO> populateSchool(final Long parentOrgNodeId){
		/*List<ObjectValueTO> objectValueList = new ArrayList<ObjectValueTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_SCHOOLS, parentOrgNodeId);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				ObjectValueTO objectValueTO = new ObjectValueTO();
				objectValueTO.setName(fieldDetails.get("ORG_NODE_NAME").toString());
				objectValueTO.setValue(fieldDetails.get("ORG_NODEID").toString());
				objectValueList.add(objectValueTO);
			}
		}
		return objectValueList;*/
		long t1 = System.currentTimeMillis();
		List<ObjectValueTO> districtList = null;
		try {
			districtList = (List<ObjectValueTO>) getJdbcTemplatePrism().execute(
					new CallableStatementCreator() {
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement cs = con.prepareCall(IQueryConstants.GET_SCHOOLS);
							cs.setLong(1, parentOrgNodeId);							
							cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
							return cs;
						}
					}, new CallableStatementCallback<Object>() {
						public Object doInCallableStatement(CallableStatement cs) {
							ResultSet rs = null;
							List<ObjectValueTO> objectValueList = new ArrayList<ObjectValueTO>();
							try {
								cs.execute();
								rs = (ResultSet) cs.getObject(2);
								ObjectValueTO to = null;
								while (rs.next()) {
									to = new ObjectValueTO();
									to.setValue(rs.getString("ORG_NODEID"));
									to.setName(rs.getString("ORG_NODE_NAME"));
									objectValueList.add(to);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							return objectValueList;
						}
					});
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: InorsDAOImpl - populateSchool() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return districtList;
	}

	
}
