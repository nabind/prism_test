/**
 * 
 */
package com.ctb.prism.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.webservice.transferobject.ContentDetailsTO;
import com.ctb.prism.webservice.transferobject.ContentScoreDetailsTO;
import com.ctb.prism.webservice.transferobject.ContentScoreTO;
import com.ctb.prism.webservice.transferobject.CustHierarchyDetailsTO;
import com.ctb.prism.webservice.transferobject.DemoTO;
import com.ctb.prism.webservice.transferobject.ItemResponseTO;
import com.ctb.prism.webservice.transferobject.ItemResponsesDetailsTO;
import com.ctb.prism.webservice.transferobject.ObjectiveScoreDetailsTO;
import com.ctb.prism.webservice.transferobject.ObjectiveScoreTO;
import com.ctb.prism.webservice.transferobject.OrgDetailsTO;
import com.ctb.prism.webservice.transferobject.RosterDetailsTO;
import com.ctb.prism.webservice.transferobject.StudentBioTO;
import com.ctb.prism.webservice.transferobject.StudentDataLoadTO;
import com.ctb.prism.webservice.transferobject.StudentDemoTO;
import com.ctb.prism.webservice.transferobject.StudentDetailsTO;
import com.ctb.prism.webservice.transferobject.StudentListTO;
import com.ctb.prism.webservice.transferobject.StudentSurveyBioTO;
import com.ctb.prism.webservice.transferobject.SubtestAccommodationTO;
import com.ctb.prism.webservice.transferobject.SubtestAccommodationsTO;
import com.ctb.prism.webservice.transferobject.SurveyBioTO;

/**
 * @author d-abir_dutta
 * @author Amitabha Roy
 *
 */
@Repository
public class UsabilityDAOImpl extends BaseDAO implements IUsabilityDAO {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityDAOImpl.class.getName());

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)
	 */
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - saveUsabilityData");
		
		try {
			int count = getJdbcTemplatePrism().update(IQueryConstants.INSERT_USABILITY_LOG, 
				usability.getReportUrl(),usability.getReportId(),usability.getReportName(),
				usability.getUsername(),usability.getCurrentOrg());
			logger.log(IAppLogger.INFO, "saveUsabilityData - count = " + count);
			logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - saveUsabilityData");
			return true;
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Error occurred while updating user details.", e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#updateStagingData(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO updateStagingData(final StudentListTO studentListTO, final StudentDataLoadTO studentDataLoadTO) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - updateStagingData");
		// insert org hierarchy
		insertOrgHierarchy(studentListTO, studentDataLoadTO);

		// insert student bio
		insertStudentBio(studentListTO, studentDataLoadTO);

		// insert content
		insertContent(studentListTO, studentDataLoadTO);

		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - updateStagingData");
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#updatePartition(java.lang.String)
	 */
	public synchronized void updatePartition(String partitionName) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - updatePartition");
		int count = getJdbcTemplatePrism().update(
		        IQueryConstants.UPDATE_PARTITION, 
		        IApplicationConstants.INACTIVE_FLAG, 
		        IApplicationConstants.ACTIVE_FLAG,
		        partitionName
		    );
		logger.log(IAppLogger.INFO, "updatePartition - count = " + count);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - updatePartition");
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#checkPartition()
	 */
	public synchronized String checkPartition() throws Exception {
		logger.log(IAppLogger.INFO, "Return: UsabilityDAOImpl - checkPartition");
		return getJdbcTemplatePrism().queryForObject(IQueryConstants.GET_PARTITION, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				String partitionName = (rs.getString(1));
				int count = getJdbcTemplatePrism().update(
			        IQueryConstants.UPDATE_PARTITION, 
			        IApplicationConstants.ACTIVE_FLAG, 
			        IApplicationConstants.INACTIVE_FLAG,
			        partitionName
			    );
				logger.log(IAppLogger.INFO, "checkPartition - count = " + count);
				logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - checkPartition");
				if(count > 0) return partitionName;
				else return null;
			}
		});
	}

	/**
	 * This method should not be called more than once within a single session.
	 * 
	 * @return staging sequence or the process id
	 */
	public long getStagingSeq() {
		logger.log(IAppLogger.INFO, "Return: UsabilityDAOImpl - getStagingSeq");
		return getJdbcTemplatePrism().queryForLong(IQueryConstants.GET_STAGING_SEQ);
	}

	
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#createProces(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO)
			throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - createProces");
		
		long stagingSeq = getStagingSeq();
		int count = getJdbcTemplatePrism().update(
	        IQueryConstants.CREATE_PROCESS_STATUS, stagingSeq, studentDataLoadTO.getPartitionName()
	    );
		logger.log(IAppLogger.INFO, "createProces - count = " + count);
		studentDataLoadTO.setProcessId(stagingSeq);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - createProces");
		return studentDataLoadTO;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#insertOrgHierarchy(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO insertOrgHierarchy(final StudentListTO studentListTO, final StudentDataLoadTO studentDataLoadTO)
			throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - insertOrgHierarchy");
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		for(final RosterDetailsTO rosterDetailsTO : rosterDetailsList) {
			final CustHierarchyDetailsTO custHierarchyDetailsTO = rosterDetailsTO.getCustHierarchyDetailsTO();
			if(custHierarchyDetailsTO.isDataChanged()) {
				// create org details
				createOrgDetails(custHierarchyDetailsTO.getCollOrgDetailsTO(), custHierarchyDetailsTO, studentDataLoadTO);
				
				// create least node hierarchy details
				createLeastNodeHireDetails(custHierarchyDetailsTO.getCollOrgDetailsTO(), custHierarchyDetailsTO, studentDataLoadTO);
			}
		}
		studentDataLoadTO.setSuccess(true);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - insertOrgHierarchy");
		return studentDataLoadTO;
	}
	
	/**
	 * 
	 * @param orgDetailsList
	 * @param orgNodeId
	 * @param parentOrgNodeId
	 * @return
	 */
	private String getOrgNodeCodePath(List<OrgDetailsTO> orgDetailsList, String orgNodeId, String parentOrgNodeId){
		Collections.sort(orgDetailsList, new OrgDetailsTOComparator());
		StringBuilder orgNodePath = new StringBuilder();
		orgNodePath.insert(0, orgNodeId);
		while (!("0".equals(parentOrgNodeId))) {
			orgNodePath.insert(0, "~");
			orgNodePath.insert(0, parentOrgNodeId);
			parentOrgNodeId = getGrandParentOrgNodeId(orgDetailsList, parentOrgNodeId);
		}
		orgNodePath.insert(0, "0~");
		return orgNodePath.toString();
	}
	
	/**
	 * 
	 * @param orgDetailsList
	 * @param parentOrgNodeId
	 * @return
	 */
	private String getGrandParentOrgNodeId(List<OrgDetailsTO> orgDetailsList, String parentOrgNodeId) {
		String s = "";
		for (OrgDetailsTO orgDetailsTO : orgDetailsList) {
			if (parentOrgNodeId.equals(orgDetailsTO.getOrgNodeId())) {
				s = orgDetailsTO.getParentOrgCode();
				break;
			}
		}
		return s;
	}
	
	/**
	 * Comparator to sort OrgDetailsTO by Parent Org Code
	 *
	 */
	class OrgDetailsTOComparator implements Comparator<OrgDetailsTO> {
		public int compare(OrgDetailsTO first, OrgDetailsTO second) {
			String firstParentOrgCode = first.getParentOrgCode().toUpperCase();
			String secondParentOrgCode = second.getParentOrgCode().toUpperCase();
			// ascending order
			return firstParentOrgCode.compareTo(secondParentOrgCode);
		}
	}
	
	/**
	 * This method makes a list of all orgNodeId-ParentOrgNodeId pairs, both from xml and database
	 * 
	 * @param orgDetailsTOList
	 * @return
	 */
	private List<OrgDetailsTO> getExistingOrgMap(List<OrgDetailsTO> orgDetailsTOList){
		OrgDetailsTO orgDetailsTO = null;
		List<OrgDetailsTO> orgMapList = new ArrayList<OrgDetailsTO>(orgDetailsTOList);
		List<Map<String, Object>> orgListMap = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORG_MAP);
		if ((orgListMap !=null) && (!orgListMap.isEmpty())){
			for (Map<String, Object> data : orgListMap) {
				orgDetailsTO = new OrgDetailsTO();
				orgDetailsTO.setOrgNodeId(data.get("ORG_NODEID").toString());
				orgDetailsTO.setParentOrgCode(data.get("PARENT_ORG_NODEID").toString());
				orgMapList.add(orgDetailsTO);
			}
		}
		return new ArrayList<OrgDetailsTO>(orgMapList);
	}

	/**
	 * Insert HIERARCHY details
	 * 
	 * @param orgDetailsTOList
	 * @param custHierarchyDetailsTO
	 * @param studentDataLoadTO
	 */
	private void createOrgDetails(final List<OrgDetailsTO> orgDetailsTOList, final CustHierarchyDetailsTO custHierarchyDetailsTO, final StudentDataLoadTO studentDataLoadTO) {
		final List<OrgDetailsTO> orgList = getExistingOrgMap(orgDetailsTOList);
		int[] counts = getJdbcTemplatePrism().batchUpdate(IQueryConstants.CREATE_HIER_DETAILS, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				OrgDetailsTO orgDetailsTO = orgDetailsTOList.get(i);
				String orgNodeCodePath = getOrgNodeCodePath(orgList, orgDetailsTO.getOrgNodeId(), orgDetailsTO.getParentOrgCode());
				ps.setLong(1, getStagingSeq());
				ps.setString(2, orgDetailsTO.getOrgName());
				ps.setString(3, orgDetailsTO.getOrgCode());
				ps.setString(4, orgDetailsTO.getOrgLabel()); // org type
				ps.setString(5, orgDetailsTO.getOrgLevel());
				ps.setString(6, custHierarchyDetailsTO.getMaxHierarchy());
				ps.setString(7, orgDetailsTO.getOrgNodeId()); // struct element
				ps.setString(8, orgDetailsTO.getOrgCode()); // special codes
				ps.setString(9, "OL"); // org mode
				ps.setString(10, orgDetailsTO.getParentOrgCode());
				ps.setString(11, orgNodeCodePath/*orgDetailsTO.getParentOrgCode() + "~" + orgDetailsTO.getOrgCode()*/); // org node code path
				ps.setString(12, orgDetailsTO.getOrgName()); // org tp - get from test_program for OL and cust id
				ps.setString(13, custHierarchyDetailsTO.getCustomerId()); // customer id 
				ps.setString(14, custHierarchyDetailsTO.getTestName()); // product id
				ps.setLong(15, studentDataLoadTO.getProcessId());
				ps.setString(16, studentDataLoadTO.getPartitionName());
				ps.setLong(17, Long.parseLong(orgDetailsTO.getOrgNodeId()));
			}
			public int getBatchSize() {
				return orgDetailsTOList.size();
			}
		});
		logger.log(IAppLogger.INFO, "createOrgDetails - counts = " + counts);
	}

	/**
	 * Insert LEAST NODE details
	 * 
	 * @param orgDetailsTOList
	 * @param custHierarchyDetailsTO
	 * @param studentDataLoadTO
	 */
	private void createLeastNodeHireDetails(List<OrgDetailsTO> orgDetailsTOList, CustHierarchyDetailsTO custHierarchyDetailsTO, StudentDataLoadTO studentDataLoadTO) {
		OrgDetailsTO leastNode = null;
		for(OrgDetailsTO orgDetailsTO : orgDetailsTOList) {
			if((orgDetailsTO.getOrgLevel() != null) && (orgDetailsTO.getOrgLevel().equals(custHierarchyDetailsTO.getMaxHierarchy()))) {
				leastNode = orgDetailsTO;
				break;
			}
		}
		if (leastNode != null) {
			int count = getJdbcTemplatePrism().update(
		        IQueryConstants.CREATE_LSTNODE_HIER_DETAILS,
		        studentDataLoadTO.getProcessId(), custHierarchyDetailsTO.getMaxHierarchy(), // 1 & 2
				leastNode.getOrgName(),// 3
				leastNode.getOrgCode(),// 4
				leastNode.getOrgLabel(), // 5
				leastNode.getOrgLevel(),// 6
				null, // org node id //7
				studentDataLoadTO.getProcessId(), custHierarchyDetailsTO.getMaxHierarchy(), // 8, 9
				studentDataLoadTO.getPartitionName() // 10
		    );
			logger.log(IAppLogger.INFO, "createLeastNodeHireDetails - count = " + count);
		} else
			logger.log(IAppLogger.INFO, "createLeastNodeHireDetails - skipping least node creation");
	}

	/**
	 * Insert student BIO, SURVEY-BIO and DEMO details
	 * 
	 * @param studentListTO - all web service data
	 * @param studentDataLoadTO - contains partition name and process id
	 * @return studentDataLoadTO - with success indicator
	 */
	public StudentDataLoadTO insertStudentBio(StudentListTO studentListTO, final StudentDataLoadTO studentDataLoadTO) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - insertStudentBio");
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		for(final RosterDetailsTO rosterDetailsTO : rosterDetailsList) {
			StudentDetailsTO studentDetailsTO = rosterDetailsTO.getStudentDetailsTO();
			
			// create student bio details
			createStudentBioDetails(studentDetailsTO.getStudentBioTO(), studentDataLoadTO, rosterDetailsTO.getRosterId(),
					rosterDetailsTO.getCustHierarchyDetailsTO());
			
			// create student demo details
			try {
				createStudentDemoDetails(studentDetailsTO.getStudentDemoTO(), studentDataLoadTO);
			} catch (Exception e) {
				logger.log(IAppLogger.ERROR, "createStudentDemoDetails - " + e.getMessage());
			}
			
			// create survey bio details
			try {
				createStudentSurveyBioDetails(studentDetailsTO.getStudentSurveyBioTO(), studentDataLoadTO);
			} catch (Exception e) {
				logger.log(IAppLogger.ERROR, "createStudentSurveyBioDetails - " + e.getMessage());
				// e.printStackTrace();
			}
		}
		studentDataLoadTO.setSuccess(true);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - insertStudentBio");
		return studentDataLoadTO;
	}

	/**
	 * Create Student BIO details
	 * 
	 * @param studentBioTO
	 * @param studentDataLoadTO
	 * @param rosterId
	 */
	private void createStudentBioDetails(StudentBioTO studentBioTO, StudentDataLoadTO studentDataLoadTO
			, String rosterId, CustHierarchyDetailsTO custHierarchyDetailsTO) {
		if(studentBioTO.isDataChanged()) {
			long studentBioId = getStagingSeq();
			int count = getJdbcTemplatePrism().update(IQueryConstants.CREATE_STG_BIO_DETAILS,
					studentBioId, //1
			        studentBioTO.getFirstName(), //2
			        studentBioTO.getMiddleInit(), //3
			        studentBioTO.getLastName(), //4
			        studentBioTO.getBirthDate(), //5
			        studentBioTO.getGender(), //6
			        studentBioTO.getGrade(), //7
			        null, // EDU_CENTER,", //8
			        null, // BARCODE,", //9
			        studentBioTO.getOasStudentId(), // SPECIAL_CODES,", //10
			        "OL", // STUDENT_MODE,", //11
			        studentBioTO.getOasStudentId(), // STRUC_ELEMENT,", //12
			        rosterId, // TEST_ELEMENT_ID,", //13
			        studentBioTO.getOasStudentId(), // INT_STUDENT_ID,", //14
			        studentBioTO.getExamineeId(), // EXT_STUDENT_ID,", //15
			        studentBioTO.getOasStudentId(), // LITHOCODE,", //16
			        studentDataLoadTO.getProcessId(), custHierarchyDetailsTO.getMaxHierarchy(), // STU_LSTNODE_HIER_ID //17 & 17.2
			        null, // IS_BIO_UPDATE_CMPL //18
			        studentDataLoadTO.getProcessId(), //19
			        IApplicationConstants.FLAG_Y, //  NEED_PRISM_CONSUME //20
			        studentDataLoadTO.getPartitionName() //21
			    );
			logger.log(IAppLogger.INFO, "StudentBio - count = " + count);
			studentDataLoadTO.setStudentBioDetailsId(studentBioId);
		}		
	}
	
	/**
	 * Create DEMO details
	 * 
	 * @param demoList
	 * @param studentDataLoadTO
	 * @param contentCode
	 */
	private void insertIntoStgStdDemoDetails(List<DemoTO> demoList, StudentDataLoadTO studentDataLoadTO, String contentCode){
		Connection dsConnection = null;
		PreparedStatement dsPstmt = null;
		Context envContext = null;
		try {
            envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/prism");
            dsConnection = ds.getConnection();
            dsPstmt = dsConnection.prepareStatement(IQueryConstants.CREATE_STG_DEMO_DETAILS);
			for (DemoTO demoTO : demoList) {
				dsPstmt.setString(1, demoTO.getDemoName());
				dsPstmt.setString(2, demoTO.getDemovalue());
				dsPstmt.setLong(3, studentDataLoadTO.getStudentBioDetailsId());
				dsPstmt.setString(4, contentCode); 
				dsPstmt.setString(5, IApplicationConstants.FLAG_Y); // TODO : CHECK
				dsPstmt.setLong(6, studentDataLoadTO.getProcessId());
				dsPstmt.setString(7, studentDataLoadTO.getPartitionName());
				dsPstmt.setString(8, null); // TODO : IS_BIO_UPDATE
				dsPstmt.addBatch();
			}
			int[] counts = dsPstmt.executeBatch();
			logger.log(IAppLogger.INFO, "createStudentDemoDetails - counts = " + counts);
        }  catch (SQLException e) {
        	logger.log(IAppLogger.ERROR, "SQLException in createStudentDemoDetails - " + e.getMessage());
        } catch (NamingException e) {
        	logger.log(IAppLogger.ERROR, "NamingException in createStudentDemoDetails - " + e.getMessage());
        } finally {
			try {
				dsPstmt.close();
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, "createStudentDemoDetails - " + e.getMessage());
			}
			try {
				dsConnection.close();
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, "createStudentDemoDetails - " + e.getMessage());
			}
		}
	}
	
	/**
	 * Insert Student DEMO details 
	 * 
	 * @param studentDemoTO
	 * @param studentDataLoadTO
	 */
	private void createStudentDemoDetails(StudentDemoTO studentDemoTO, StudentDataLoadTO studentDataLoadTO) {
		if (studentDemoTO.isDataChanged()) {
			insertIntoStgStdDemoDetails(studentDemoTO.getCollDemoTO(), studentDataLoadTO, null);
		}
	}

	/**
	 * Insert SURVEY BIO details
	 * 
	 * @param studentSurveyBioTO
	 * @param studentDataLoadTO
	 */
	private void createStudentSurveyBioDetails(StudentSurveyBioTO studentSurveyBioTO, StudentDataLoadTO studentDataLoadTO) {
		if (studentSurveyBioTO.isDataChanged()) {
			List<SurveyBioTO> surveyBioList = studentSurveyBioTO.getCollSurveyBioTO();
			List<DemoTO> demoList = new ArrayList<DemoTO>();
			for (SurveyBioTO surveyBioTO : surveyBioList) {
				DemoTO demoTO = new DemoTO();
				demoTO.setDemoName(surveyBioTO.getSurveyName());
				demoTO.setDemovalue(surveyBioTO.getSurveyValue());
				demoList.add(demoTO);
			}
			insertIntoStgStdDemoDetails(demoList, studentDataLoadTO, null);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#insertContent(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO insertContent(StudentListTO studentListTO,
			StudentDataLoadTO studentDataLoadTO) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - insertContent");
		final Long studentBioDetailsId = studentDataLoadTO.getStudentBioDetailsId();
		final Long processId = studentDataLoadTO.getProcessId();
		final String partitionName = studentDataLoadTO.getPartitionName();
		final long stagingSeq = studentDataLoadTO.getProcessId();
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		for(final RosterDetailsTO rosterDetailsTO : rosterDetailsList) {
			List<ContentDetailsTO> contentDetails = rosterDetailsTO.getCollContentDetailsTO();
			for(ContentDetailsTO contentDetailsTO : contentDetails) {
				if(contentDetailsTO.isDataChanged()) {
					// create content/subtest details
					createContentDetails(contentDetailsTO, studentDataLoadTO, stagingSeq);

					// create subtest accommodations
					try {
						createSubtestAccomodations(contentDetailsTO.getContentCode(), contentDetailsTO.getSubtestAccommodationsTO(), studentDataLoadTO);						
					} catch (Exception e) {
						logger.log(IAppLogger.ERROR, "createSubtestAccomodations - " + e.getMessage());
						// e.printStackTrace();
					}

					// create item Responses
					try {
						createItemResponses(rosterDetailsTO.getStudentDetailsTO().getStudentBioTO(),
								contentDetailsTO.getContentCode(), 
								contentDetailsTO.getItemResponsesDetailsTO(), 
								studentDataLoadTO);						
					} catch (Exception e) {
						logger.log(IAppLogger.ERROR, "createItemResponses - " + e.getMessage());
						// e.printStackTrace();
					}

					// update subtest scores : single entry per content/subtest ?
					updateSubtestScores(contentDetailsTO.getContentScoreDetailsTO(), studentBioDetailsId, processId, partitionName);

					// Create objective details : multiple entries per content/subtest
					createObjectiveDetails(contentDetailsTO.getCollObjectiveScoreDetailsTO(), contentDetailsTO, studentDataLoadTO, studentBioDetailsId, processId, partitionName);
				}
			}
		}
		studentDataLoadTO.setSuccess(true);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - insertContent");
		return studentDataLoadTO;
	}

	/**
	 * Update OBJECTIVE SCORES 
	 * 
	 * @param objectiveScoreList
	 * @param studentBioDetailsId
	 * @param processId
	 * @param partitionName
	 */
	private void updateObjectiveScores(List<ObjectiveScoreTO> objectiveScoreList, final Long studentBioDetailsId, final Long processId, final String partitionName) {
		// TODO : Check batch update instead of for-loop for dynamic query string
		List<Integer> counts = new ArrayList<Integer>();
		for (ObjectiveScoreTO objectiveScoreTO : objectiveScoreList) {
			int count = getJdbcTemplatePrism().update(
				CustomStringUtil.replaceCharacterInString('~', getStgStdObjectiveDetailsColumnName(objectiveScoreTO.getScoreType()), IQueryConstants.UPDATE_STG_OBJECTIVE_DETAILS), // Update query
				objectiveScoreTO.getValue(), // value
				studentBioDetailsId, processId, partitionName);
			counts.add(count);
		}
		logger.log(IAppLogger.INFO, "ContentScoreDetails - counts = " + counts);
	}
	
	/**
	 * This method is used to convert xml field name to a valid table column
	 * name. 
	 * 
	 * @param fieldName
	 *            xml field name
	 * @return valid table column name
	 */
	private String getStgStdObjectiveDetailsColumnName(String fieldName) {
		String columnName = fieldName;
		if ("NC".equals(columnName)) {
			columnName = "NCR";
		} else if ("CC".equals(columnName)) {
			columnName = "COND_CODE";
		} else if ("NP".equals(columnName)) {
			columnName = "PP";
		} else if ("MA".equals(columnName)) {
			// TODO : MA not found in STG_STD_OBJECTIVE_DETAILS table
		} else if ("MR".equals(columnName)) {
			// TODO : MR not found in STG_STD_OBJECTIVE_DETAILS table
		}
		return columnName;
	}
	
	/**
	 * This method is used to convert xml field name to a valid table column
	 * name.
	 * 
	 * @param fieldName
	 *            xml field name
	 * @return valid table column name
	 */
	private String getStgStdSubtestDetailsColumnName(String fieldName) {
		String columnName = fieldName;
		if ("NC".equals(columnName)) {
			columnName = "NCR";
		} else if ("SSR".equals(columnName)) {
			// TODO : SSR not found in STG_STD_SUBTEST_DETAILS table
		}
		return columnName;
	}

	/**
	 * Insert OBJECTIVE details 
	 * 
	 * @param objectiveScoreDetailsTOList
	 * @param contentDetailsTO
	 * @param studentDataLoadTO
	 * @param studentBioDetailsId
	 * @param processId
	 * @param partitionName
	 */
	private void createObjectiveDetails(List<ObjectiveScoreDetailsTO> objectiveScoreDetailsTOList, ContentDetailsTO contentDetailsTO, StudentDataLoadTO studentDataLoadTO, Long studentBioDetailsId, Long processId, String partitionName) {
		for(ObjectiveScoreDetailsTO objectiveScoreDetailsTO : objectiveScoreDetailsTOList) {
			int count = getJdbcTemplatePrism().update(
		        IQueryConstants.CREATE_STG_OBJECTIVE_DETAILS,
		        studentDataLoadTO.getStudentBioDetailsId(), //1
		        contentDetailsTO.getContentCode(), // content name //2
		        null, // test form //3
		        contentDetailsTO.getDateTestTaken(), //4
		        objectiveScoreDetailsTO.getObjectiveCode(), /*objectiveScoreDetailsTO.getObjectiveName(),*/ //5
		        studentDataLoadTO.getProcessId(), //6
		        studentDataLoadTO.getPartitionName() //7
		    );
			logger.log(IAppLogger.INFO, "insertContent - count = " + count);
			
			// update objective scores
			updateObjectiveScores(objectiveScoreDetailsTO.getCollObjectiveScoreTO(), studentBioDetailsId, processId, partitionName);
		}		
	}

	/**
	 * Update SUBTEST SCORES 
	 * 
	 * @param contentScoreDetailsTO
	 * @param studentBioDetailsId
	 * @param processId
	 * @param partitionName
	 */
	private void updateSubtestScores(ContentScoreDetailsTO contentScoreDetailsTO, final Long studentBioDetailsId, final Long processId, final String partitionName) {
		final List<ContentScoreTO> contentScoreList = contentScoreDetailsTO.getCollContentScoreTO();
		// TODO : Check batch update instead of for-loop for dynamic sql
		for (ContentScoreTO contentScoreTO : contentScoreList) {
			getJdbcTemplatePrism().update(
				CustomStringUtil.replaceCharacterInString('~', getStgStdSubtestDetailsColumnName(contentScoreTO.getScoreType()), IQueryConstants.UPDATE_STG_SUBTEST_DETAILS), // Update query
				contentScoreTO.getScoreValue(), // value
				studentBioDetailsId,
				processId,
				partitionName
			);
		}
		//logger.log(IAppLogger.INFO, "ContentScoreDetails - counts = " + counts);
	}

	/**
	 * Insert ITEM_RESPONSE details
	 * 
	 * @param itemResponsesDetailsTO
	 * @param studentBioDetailsId
	 * @param processId
	 * @param partitionName
	 */
	private void createItemResponses(StudentBioTO studentBioTO, String contentCode, 
			ItemResponsesDetailsTO itemResponsesDetailsTO, StudentDataLoadTO studentDataLoadTO) {
		Connection dsConnection = null;
		PreparedStatement dsPstmt = null;
		Context envContext = null;
		try {
            envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/prism");
            dsConnection = ds.getConnection();

            dsPstmt = dsConnection.prepareStatement(IQueryConstants.CREATE_STG_ITEM_RESPONSE_DETAILS);

            List<ItemResponseTO> itemResponseList = itemResponsesDetailsTO.getItemResponseTO();
			for (ItemResponseTO itemResponseTO : itemResponseList) {
				dsPstmt.setLong(1, studentDataLoadTO.getStudentBioDetailsId());
				dsPstmt.setString(2, contentCode); // TODO : CONTENT_CODE
				dsPstmt.setString(3, itemResponseTO.getScoreValue());
				dsPstmt.setString(4, null); // TODO : TEST_FORM
				dsPstmt.setString(5, studentBioTO.getGrade());  // TODO : GRADE
				dsPstmt.setString(6, itemResponseTO.getItemSetType());
				dsPstmt.setString(7, itemResponseTO.getItemCode());
				dsPstmt.setString(8, null); // TODO : ITEM_NAME
				dsPstmt.setString(9, null); // TODO : READ_CODE
				dsPstmt.setString(10, itemResponseTO.getScoreValue());
				dsPstmt.setString(11, studentDataLoadTO.getPartitionName());
				dsPstmt.setLong(12, studentDataLoadTO.getProcessId());
				dsPstmt.addBatch();
			}
			int[] counts = dsPstmt.executeBatch();
			logger.log(IAppLogger.INFO, "createItemResponses - counts = " + counts.length);
        }  catch (SQLException e) {
        	logger.log(IAppLogger.ERROR, "SQLException in createItemResponses - " + e.getMessage());
        } catch (NamingException e) {
        	logger.log(IAppLogger.ERROR, "NamingException in createItemResponses - " + e.getMessage());
        } finally {
			try {
				dsPstmt.close();
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, "createItemResponses - " + e.getMessage());
			}
			try {
				dsConnection.close();
			} catch (Exception e) {
				logger.log(IAppLogger.WARN, "createItemResponses - " + e.getMessage());
			}
		}
	
	}

	/**
	 * Insert SUBTEST_ACCOMODATION with valid content code
	 * 
	 * @param contentCode
	 * @param subtestAccommodationsTO
	 * @param studentDataLoadTO
	 */
	private void createSubtestAccomodations(String contentCode, final SubtestAccommodationsTO subtestAccommodationsTO, final StudentDataLoadTO studentDataLoadTO) {
		List<SubtestAccommodationTO> accommodationList = subtestAccommodationsTO.getCollSubtestAccommodationTO();
		List<DemoTO> demoList = new ArrayList<DemoTO>();
		for (SubtestAccommodationTO subtestAccommodationTO : accommodationList) {
			DemoTO demoTO = new DemoTO();
			demoTO.setDemoName(subtestAccommodationTO.getName());
			demoTO.setDemovalue(subtestAccommodationTO.getValue());
			demoList.add(demoTO);
		}
		insertIntoStgStdDemoDetails(demoList, studentDataLoadTO, contentCode);
	}

	/**
	 * Create STBTEST details
	 * 
	 * @param contentDetailsTO
	 * @param studentDataLoadTO
	 * @param subtestDetailsId
	 */
	private void createContentDetails(ContentDetailsTO contentDetailsTO, StudentDataLoadTO studentDataLoadTO, long subtestDetailsId) {
		int count = getJdbcTemplatePrism().update(
	        IQueryConstants.CREATE_STG_SUBTEST_DETAILS,
	        //subtestDetailsId, //1
	        studentDataLoadTO.getStudentBioDetailsId(), //2
	        contentDetailsTO.getStatusCode(), //3
	        contentDetailsTO.getContentCode(), // content name //4
	        contentDetailsTO.getScoringMethod(), // scoring status //5
	        null, // test form //6
	        contentDetailsTO.getDateTestTaken(), //7
	        studentDataLoadTO.getProcessId(), //8
	        IApplicationConstants.FLAG_Y, //9
	        studentDataLoadTO.getPartitionName() //10
	    );
		logger.log(IAppLogger.INFO, "ContentDetails - count = " + count);
		studentDataLoadTO.setSubtestDetailsId(subtestDetailsId);
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#insertItems(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO insertItems(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO)
			throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - insertItems");
		int count = getJdbcTemplatePrism().update(
	        IQueryConstants.CREATE_STG_ITEM_RESPONSE_DETAILS
	    );	
		logger.log(IAppLogger.INFO, "insertItems - count = " + count);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - insertItems");
		return null;
	}

}
