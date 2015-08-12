/**
 * 
 */
package com.ctb.prism.core.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.core.transferobject.ProcessTO;
import com.ctb.prism.core.transferobject.StudentDataExtractTO;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
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
import com.thoughtworks.xstream.XStream;

/**
 * @author d-abir_dutta
 * @author Amitabha Roy
 *
 */
@Repository
public class UsabilityDAOImpl extends BaseDAO implements IUsabilityDAO {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityDAOImpl.class.getName());
	//private String TEST_FORM = "";
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#saveUsabilityData(com.ctb.prism.core.transferobject.UsabilityTO)
	 */
	public boolean saveUsabilityData(UsabilityTO usability) throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - saveUsabilityData");
		logger.log(IAppLogger.INFO, usability.toString());
		try {
			int count = getJdbcTemplatePrism().update((usability.isLoginAs())? IQueryConstants.INSERT_LoginAsACTIVITY_LOG : IQueryConstants.INSERT_ACTIVITY_LOG, 
				usability.getUserId(),usability.getCustomerId(),usability.getActivityTypeId(),
				usability.getReportUrl(),usability.getIpAddress());
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
	 * Insert rosterId to block any duplicate student parallel processing
	 * @param rosterId
	 * @throws Exception
	 */
	public synchronized String getRoster(final String rosterId)
			throws Exception {
		List<String> strLst  = getJdbcTemplatePrism().query(IQueryConstants.GET_ROSTER, new Object[]{ rosterId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int col) throws SQLException {
				String outRoster = (rs.getString(1));
				return outRoster;
			}
		});
		if ( strLst.isEmpty() ) {
			return null;
		} else return strLst.get(0);
	}
	
	public synchronized boolean createRoster(String rosterId)
			throws Exception {
		int count = getJdbcTemplatePrism().update(IQueryConstants.CREATE_ROSTER, rosterId);
		if(count > 0) return true;
		else return false;
	}
	
	/**
	 * Delete the roster when web service validation is completed
	 * @param rosterId
	 * @throws Exception
	 */
	public void removeRoster(String rosterId)
			throws Exception {
		getJdbcTemplatePrism().update(IQueryConstants.REMOVE_ROSTER, rosterId);
	}

	/**
	 * This method should not be called more than once within a single session.
	 * 
	 * @return staging sequence or the process id
	 */
	private long getStagingSeq() {
		logger.log(IAppLogger.INFO, "Return: UsabilityDAOImpl - getStagingSeq");
		return getJdbcTemplatePrism().queryForLong(IQueryConstants.GET_STAGING_SEQ);
	}

	
	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#createProces(com.ctb.prism.webservice.transferobject.StudentListTO, com.ctb.prism.webservice.transferobject.StudentDataLoadTO)
	 */
	public StudentDataLoadTO createProces(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO)
			throws Exception {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - createProces");
		cleanStagingTables(studentDataLoadTO.getPartitionName());
		long stagingSeq = getStagingSeq();
		int count = getJdbcTemplatePrism().update(
	        IQueryConstants.CREATE_PROCESS_STATUS, stagingSeq, studentDataLoadTO.getPartitionName()
	    );
		logger.log(IAppLogger.INFO, "createProces - count = " + count);
		studentDataLoadTO.setProcessId(stagingSeq);
		logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - createProces");
		return studentDataLoadTO;
	}
	
	/**
	 * Get validation status
	 * @param processTO
	 * @return
	 * @throws Exception
	 */
	public ProcessTO getProces(ProcessTO processTO) throws Exception {
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(
				IQueryConstants.GET_PROCESS_STATUS, processTO.getProcessId());
		logger.log(IAppLogger.DEBUG, lstData.size()+"");
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				processTO.setHierValidation((String) (fieldDetails.get("hier_validation")));
				processTO.setBioValidation((String) (fieldDetails.get("bio_validation")));
				processTO.setDemoValidation((String) (fieldDetails.get("demo_validation")));
				processTO.setContentValidation((String) (fieldDetails.get("content_validation")));
				processTO.setObjectiveValidation((String) (fieldDetails.get("objective_validation")));
				processTO.setItemValidation((String) (fieldDetails.get("item_validation")));
				processTO.setProcessLog((String) fieldDetails.get("process_log"));
				processTO.setErValidation((String) fieldDetails.get("er_validation"));
			}
		}
		return processTO;
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
			if(custHierarchyDetailsTO != null && custHierarchyDetailsTO.isDataChanged()) {
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
	private String getOrgNodeCodePath(List<OrgDetailsTO> orgDetailsList, String orgNodeCode, String parentOrgNodeCode){
		Collections.sort(orgDetailsList, new OrgDetailsTOComparator());
		StringBuilder orgNodePath = new StringBuilder();
		orgNodePath.insert(0, orgNodeCode);
		int count = 0;
		while (!("0".equals(parentOrgNodeCode))) {
			count ++;
			if(count > 20) break; // some data issue are there ... 
			orgNodePath.insert(0, "~");
			orgNodePath.insert(0, parentOrgNodeCode);
			parentOrgNodeCode = getGrandParentOrgNodeId(orgDetailsList, parentOrgNodeCode);
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
			if (parentOrgNodeId.equals(orgDetailsTO.getOrgCode())) {
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
	private class OrgDetailsTOComparator implements Comparator<OrgDetailsTO> {
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
	private Map<String, String> getOrgMap(List<OrgDetailsTO> orgDetailsTOList){
		long methodStart = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Enter : getOrgMap()");
		Map<String, String> orgNodeCodePathMap = new HashMap<String, String>();
		if(!orgDetailsTOList.isEmpty()){
			String idList = "";
			for (OrgDetailsTO to : orgDetailsTOList) {
				idList = idList + "'" + to.getOrgCode() + "'" + "," + "'" + to.getParentOrgCode() + "'" + ",";
			}
			idList = idList.substring(0, idList.length() - 1);
			logger.log(IAppLogger.INFO, "idList = " + idList);

			// Add records from database
			long queryStart = System.currentTimeMillis();
			String sql = CustomStringUtil.replaceCharacterInString('?', idList, IQueryConstants.GET_ORG_MAP);
			logger.log(IAppLogger.INFO, "sql = " + sql);
			List<Map<String, Object>> orgListMap = getJdbcTemplatePrism().queryForList(sql);
			long queryEnd = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Query executed in " + CustomStringUtil.getHMSTimeFormat(queryEnd - queryStart));
			if ((orgListMap !=null) && (!orgListMap.isEmpty())){
				for (Map<String, Object> data : orgListMap) {
					String orgNodeId = data.get("ORG_NODEID").toString();
					String orgNodeLevel = data.get("ORG_NODE_LEVEL").toString();
					String highestOrgNodeLevel = data.get("HIGHEST_ORG_NODE").toString();
					String parentOrgNodeId = data.get("PARENT_ORG_NODEID").toString();
					if ("1".equals(orgNodeLevel)) {
						orgNodeCodePathMap.put(orgNodeId, CustomStringUtil.appendString("0~", orgNodeId));
					} else if ("2".equals(orgNodeLevel)) {
						orgNodeCodePathMap.put(orgNodeId, CustomStringUtil.appendString("0~", parentOrgNodeId, "~", orgNodeId));
					} else if ("3".equals(orgNodeLevel)) {
						orgNodeCodePathMap.put(orgNodeId, CustomStringUtil.appendString("0~", highestOrgNodeLevel, "~", parentOrgNodeId, "~", orgNodeId));
					}
				}
			}
			
			// Add records from xml input
			// There is no validation, it is assumed that the xml data is valid
			for (OrgDetailsTO to : orgDetailsTOList) {
				String orgNodeId = to.getOrgNodeId();
				String orgNodeLevel = to.getOrgLevel();
				String parentOrgNodeId = to.getParentOrgCode();
				if (!orgNodeCodePathMap.containsKey(orgNodeId)) {
					if (!orgNodeCodePathMap.containsKey(parentOrgNodeId)) {
						if("0".equals(parentOrgNodeId) || parentOrgNodeId == null)
							orgNodeCodePathMap.put(orgNodeId, CustomStringUtil.appendString("0~", orgNodeId));
						else
							orgNodeCodePathMap.put(orgNodeId, CustomStringUtil.appendString("0~", parentOrgNodeId, "~", orgNodeId));
					} else {
						orgNodeCodePathMap.put(orgNodeId, CustomStringUtil.appendString(orgNodeCodePathMap.get(parentOrgNodeId), "~", orgNodeId));
					}
				}
			}
		}
		long methodEnd = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "getOrgMap() executed in " + CustomStringUtil.getHMSTimeFormat(methodEnd - methodStart));
		return orgNodeCodePathMap;
	}

	/**
	 * Insert HIERARCHY details
	 * 
	 * @param orgDetailsTOList
	 * @param custHierarchyDetailsTO
	 * @param studentDataLoadTO
	 */
	private void createOrgDetails(final List<OrgDetailsTO> orgDetailsTOList, final CustHierarchyDetailsTO custHierarchyDetailsTO, final StudentDataLoadTO studentDataLoadTO) {
		long start = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "Enter : createOrgDetails()");
		//final Map<String, String> orgMap = getOrgMap(orgDetailsTOList);
		//logger.log(IAppLogger.INFO, "orgMap.size() = " + orgMap.size());
		
		SortedMap<Integer,OrgDetailsTO> categories = new TreeMap<Integer,OrgDetailsTO>();
		List<OrgDetailsTO> orderedOrgDetailsTOList = new LinkedList<OrgDetailsTO>();
		for(OrgDetailsTO orgDetailsTO : orgDetailsTOList) {
			categories.put(new Integer(orgDetailsTO.getOrgLevel()), orgDetailsTO);
		}
		Iterator<OrgDetailsTO> iter = categories.values().iterator();
		final SortedMap<String,String> nodePath = new TreeMap<String,String>();
		String path = "0";
		while(iter.hasNext()) {
			OrgDetailsTO orgDetailsTO = iter.next();
			path = path + "~" + orgDetailsTO.getOrgCode();
			nodePath.put(orgDetailsTO.getOrgLevel(), path);
		}
		
		int[] counts = getJdbcTemplatePrism().batchUpdate(IQueryConstants.CREATE_HIER_DETAILS, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				OrgDetailsTO orgDetailsTO = orgDetailsTOList.get(i);
				//String orgNodeCodePath = orgMap.get(orgDetailsTO.getOrgNodeId()); 
				String customerId = custHierarchyDetailsTO.getCustomerId();
						//getOrgNodeCodePath(orgMap, orgDetailsTO.getOrgNodeId(), orgDetailsTO.getParentOrgCode());
				logger.log(IAppLogger.INFO, orgDetailsTO.getOrgNodeId()+ " - " + nodePath.get(orgDetailsTO.getOrgLevel()));
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
				ps.setString(11, nodePath.get(orgDetailsTO.getOrgLevel()) /*orgNodeCodePath //orgDetailsTO.getParentOrgCode() + "~" + orgDetailsTO.getOrgCode()*/); // org node code path
				ps.setString(12, customerId); // org tp - get from test_program for OL and cust id
				ps.setString(13, customerId); // prism customer id 
				ps.setString(14, custHierarchyDetailsTO.getTestName()); // product id
				ps.setLong(15, studentDataLoadTO.getProcessId());
				ps.setString(16, studentDataLoadTO.getPartitionName());
				ps.setLong(17, Long.parseLong(orgDetailsTO.getOrgNodeId()));
			}
			public int getBatchSize() {
				return orgDetailsTOList.size();
			}
		});
		logger.log(IAppLogger.INFO, "Enter : createOrgDetails() - counts = " + counts);
		long end = System.currentTimeMillis();
		logger.log(IAppLogger.INFO, "createOrgDetails() executed in " + CustomStringUtil.getHMSTimeFormat(end - start));
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
				leastNode.getOrgNodeId(), // org node id //7
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
		if(rosterDetailsList != null) {
			for(final RosterDetailsTO rosterDetailsTO : rosterDetailsList) {
				/* commenting as - content code should be null for student bio and survey bio
				String contentCode = "";
				if ((rosterDetailsTO.getCollContentDetailsTO() != null) && (!rosterDetailsTO.getCollContentDetailsTO().isEmpty()))
					contentCode = rosterDetailsTO.getCollContentDetailsTO().get(0).getContentCode();*/
				StudentDetailsTO studentDetailsTO = rosterDetailsTO.getStudentDetailsTO();
				
				// create student bio details
				createStudentBioDetails(studentDetailsTO.getStudentBioTO(), studentDataLoadTO, rosterDetailsTO.getRosterId(),
						rosterDetailsTO.getCustHierarchyDetailsTO(), studentDetailsTO.getStudentSurveyBioTO());
				
				// create student demo details
				createStudentDemoDetails(studentDetailsTO.getStudentDemoTO(), studentDataLoadTO, null);
				
				// create survey bio details
				createStudentSurveyBioDetails(studentDetailsTO.getStudentSurveyBioTO(), studentDataLoadTO, null);
			}
			studentDataLoadTO.setSuccess(true);
			logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - insertStudentBio");
		}
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
			, String rosterId, CustHierarchyDetailsTO custHierarchyDetailsTO, StudentSurveyBioTO studentSurveyBioTO) {
		if(studentBioTO != null && studentBioTO.isDataChanged()) {
			// fetch education center id from survey bio
			String eduCenterId = "";
			if (studentSurveyBioTO != null && studentSurveyBioTO.isDataChanged()) {
				List<SurveyBioTO> surveyBioList = studentSurveyBioTO.getCollSurveyBioTO();
				if(surveyBioList != null) {
					for (SurveyBioTO surveyBioTO : surveyBioList) {
						if("Edu_Ctr_Cd".equals(surveyBioTO.getSurveyName())) {
							eduCenterId = surveyBioTO.getSurveyValue();
							break;
						}
					}
				}
			}
			
			long studentBioId = getStagingSeq();
			int count = getJdbcTemplatePrism().update(IQueryConstants.CREATE_STG_BIO_DETAILS,
					studentBioId, //1
			        studentBioTO.getFirstName(), //2
			        studentBioTO.getMiddleInit(), //3
			        studentBioTO.getLastName(), //4
			        studentBioTO.getBirthDate(), //5
			        studentBioTO.getGender(), //6
			        studentBioTO.getGrade(), //7
			        eduCenterId, // EDU_CENTER,", //8
			        null, // BARCODE,", //9
			        studentBioTO.getOasStudentId(), // SPECIAL_CODES,", //10
			        "OL", // STUDENT_MODE,", //11
			        studentBioTO.getOasStudentId(), // STRUC_ELEMENT,", //12
			        rosterId, // TEST_ELEMENT_ID,", //13
			        studentBioTO.getOasStudentId(), // INT_STUDENT_ID,", //14
			        studentBioTO.getExamineeId(), // EXT_STUDENT_ID,", //15
			        studentBioTO.getOasStudentId(), // LITHOCODE,", //16
			        studentDataLoadTO.getProcessId(), custHierarchyDetailsTO.getMaxHierarchy(), // STU_LSTNODE_HIER_ID //17 & 17.2
			        IApplicationConstants.FLAG_N, // IS_BIO_UPDATE_CMPL //18
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
            DataSource ds = (DataSource)initContext.lookup("jdbc/tasc");
            dsConnection = ds.getConnection();
            dsPstmt = dsConnection.prepareStatement(IQueryConstants.CREATE_STG_DEMO_DETAILS);
            if(demoList != null) {
				for (DemoTO demoTO : demoList) {
					//if("Test_Form".equals(demoTO.getDemoName())) TEST_FORM = demoTO.getDemovalue();
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
            }
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
	private void createStudentDemoDetails(StudentDemoTO studentDemoTO, StudentDataLoadTO studentDataLoadTO, String contentCode) {
		if (studentDemoTO != null && studentDemoTO.isDataChanged()) {
			insertIntoStgStdDemoDetails(studentDemoTO.getCollDemoTO(), studentDataLoadTO, contentCode);
		}
	}

	/**
	 * Insert SURVEY BIO details
	 * 
	 * @param studentSurveyBioTO
	 * @param studentDataLoadTO
	 * @param contentCode 
	 */
	private void createStudentSurveyBioDetails(StudentSurveyBioTO studentSurveyBioTO, StudentDataLoadTO studentDataLoadTO, String contentCode) {
		if (studentSurveyBioTO != null && studentSurveyBioTO.isDataChanged()) {
			List<SurveyBioTO> surveyBioList = studentSurveyBioTO.getCollSurveyBioTO();
			List<DemoTO> demoList = new ArrayList<DemoTO>();
			if(surveyBioList != null) {
				for (SurveyBioTO surveyBioTO : surveyBioList) {
					DemoTO demoTO = new DemoTO();
					demoTO.setDemoName(surveyBioTO.getSurveyName());
					demoTO.setDemovalue(surveyBioTO.getSurveyValue());
					demoList.add(demoTO);
				}
				insertIntoStgStdDemoDetails(demoList, studentDataLoadTO, contentCode);
			}
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
			if(contentDetails != null) {
				// fetch the form 
				String testForm = null;
				StudentDetailsTO studentDetailsTO = rosterDetailsTO.getStudentDetailsTO();
				if(studentDetailsTO != null) {
					StudentDemoTO studentDemoTO = studentDetailsTO.getStudentDemoTO(); 
					if(studentDemoTO != null) {
						List<DemoTO> demoList = studentDemoTO.getCollDemoTO();
						if(demoList != null && !demoList.isEmpty()) {
							for (DemoTO demoTO : demoList) {
								if("Test_Form".equals(demoTO.getDemoName())) {
									testForm = demoTO.getDemovalue();
									break;
								}
							}
						}
					}
				}
				// end : fetch the form
				for(ContentDetailsTO contentDetailsTO : contentDetails) {
					if(contentDetailsTO.isDataChanged()) {
						// create content/subtest details
						createContentDetails(contentDetailsTO, studentDataLoadTO, stagingSeq, testForm);
	
						// create subtest accommodations
						createSubtestAccomodations(contentDetailsTO.getContentCode(), contentDetailsTO.getSubtestAccommodationsTO(), studentDataLoadTO);						
	
						// create item Responses
						createItemResponses(rosterDetailsTO.getStudentDetailsTO().getStudentBioTO(),
									contentDetailsTO.getContentCode(), 
									contentDetailsTO.getItemResponsesDetailsTO(), 
									studentDataLoadTO, testForm);						
	
						// update subtest scores : single entry per content/subtest ?
						updateSubtestScores(contentDetailsTO.getContentScoreDetailsTO(), studentBioDetailsId, processId, 
								partitionName, contentDetailsTO.getContentCode());
	
						// Create objective details : multiple entries per content/subtest
						createObjectiveDetails(contentDetailsTO.getCollObjectiveScoreDetailsTO(), contentDetailsTO, 
								studentDataLoadTO, studentBioDetailsId, processId, partitionName, testForm);
					}
				}
			} else {
				logger.log(IAppLogger.INFO, "Subtest details is not present");
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
	private void updateObjectiveScores(List<ObjectiveScoreTO> objectiveScoreList, 
			final Long studentBioDetailsId, final Long processId, 
			final String partitionName, String objectiveCode, String ContentCode) {
		// TODO : Check batch update instead of for-loop for dynamic query string
		List<Integer> counts = new ArrayList<Integer>();
		if(objectiveScoreList != null) {
			for (ObjectiveScoreTO objectiveScoreTO : objectiveScoreList) {
				if(!"MR".equals(objectiveScoreTO.getScoreType())) {
					int count = getJdbcTemplatePrism().update(
						CustomStringUtil.replaceCharacterInString('~', getStgStdObjectiveDetailsColumnName(objectiveScoreTO.getScoreType()), IQueryConstants.UPDATE_STG_OBJECTIVE_DETAILS), // Update query
						objectiveScoreTO.getValue(), // value
						studentBioDetailsId, processId, partitionName,
						ContentCode, objectiveCode);
					counts.add(count);
				}
			}
			logger.log(IAppLogger.INFO, "ContentScoreDetails - counts = " + counts);
		}
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
			columnName = "PL";
		} else if ("MR".equals(columnName)) {
			// TODO : MR not found in STG_STD_OBJECTIVE_DETAILS table
		} else if ("OSC".equals(columnName)) {
			columnName = "INRC";
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
	private void createObjectiveDetails(List<ObjectiveScoreDetailsTO> objectiveScoreDetailsTOList, 
			ContentDetailsTO contentDetailsTO, StudentDataLoadTO studentDataLoadTO, Long studentBioDetailsId, 
			Long processId, String partitionName, String testForm) {
		if(objectiveScoreDetailsTOList != null) {
			for(ObjectiveScoreDetailsTO objectiveScoreDetailsTO : objectiveScoreDetailsTOList) {
				int count = getJdbcTemplatePrism().update(
			        IQueryConstants.CREATE_STG_OBJECTIVE_DETAILS,
			        studentDataLoadTO.getStudentBioDetailsId(), //1
			        contentDetailsTO.getContentCode(), // content name //2
			        testForm, // test form //3
			        contentDetailsTO.getDateTestTaken(), //4
			        objectiveScoreDetailsTO.getObjectiveCode(), /*objectiveScoreDetailsTO.getObjectiveName(),*/ //5
			        studentDataLoadTO.getProcessId(), //6
			        studentDataLoadTO.getPartitionName() //7
			    );
				logger.log(IAppLogger.INFO, "insertContent - count = " + count);
				
				// update objective scores
				updateObjectiveScores(objectiveScoreDetailsTO.getCollObjectiveScoreTO(), 
						studentBioDetailsId, processId, partitionName, 
						objectiveScoreDetailsTO.getObjectiveCode(), contentDetailsTO.getContentCode());
			}	
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
	private void updateSubtestScores(ContentScoreDetailsTO contentScoreDetailsTO, 
			final Long studentBioDetailsId, final Long processId, 
			final String partitionName, String contentCode) {
		if(contentScoreDetailsTO != null) {
			final List<ContentScoreTO> contentScoreList = contentScoreDetailsTO.getCollContentScoreTO();
			// TODO : Check batch update instead of for-loop for dynamic sql
			if(contentScoreList != null) {
				for (ContentScoreTO contentScoreTO : contentScoreList) {
					if(!"SSR".equals(contentScoreTO.getScoreType())) {
						getJdbcTemplatePrism().update(
							CustomStringUtil.replaceCharacterInString('~', getStgStdSubtestDetailsColumnName(contentScoreTO.getScoreType()), IQueryConstants.UPDATE_STG_SUBTEST_DETAILS), // Update query
							contentScoreTO.getScoreValue(), // value
							studentBioDetailsId,
							processId,
							partitionName,
							contentCode
						);
					}
				}
			}
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
			ItemResponsesDetailsTO itemResponsesDetailsTO, StudentDataLoadTO studentDataLoadTO, String testForm) {
		Connection dsConnection = null;
		PreparedStatement dsPstmt = null;
		Context envContext = null;
		try {
			if(itemResponsesDetailsTO != null) {
	            envContext = new InitialContext();
	            Context initContext  = (Context)envContext.lookup("java:/comp/env");
	            DataSource ds = (DataSource)initContext.lookup("jdbc/tasc");
	            dsConnection = ds.getConnection();
	
	            dsPstmt = dsConnection.prepareStatement(IQueryConstants.CREATE_STG_ITEM_RESPONSE_DETAILS);
	
	            List<ItemResponseTO> itemResponseList = itemResponsesDetailsTO.getItemResponseTO();
	            if(itemResponseList != null) {
					for (ItemResponseTO itemResponseTO : itemResponseList) {
						dsPstmt.setLong(1, studentDataLoadTO.getStudentBioDetailsId());
						dsPstmt.setString(2, contentCode); // TODO : CONTENT_CODE
						dsPstmt.setString(3, itemResponseTO.getScoreValue());
						dsPstmt.setString(4, testForm); // TODO : TEST_FORM
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
	            }
			} else {
				logger.log(IAppLogger.INFO, "itemResponsesDetailsTO is blank/null");
			}
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
		if(subtestAccommodationsTO != null) {
			List<SubtestAccommodationTO> accommodationList = subtestAccommodationsTO.getCollSubtestAccommodationTO();
			List<DemoTO> demoList = new ArrayList<DemoTO>();
			if(accommodationList != null) {
				for (SubtestAccommodationTO subtestAccommodationTO : accommodationList) {
					DemoTO demoTO = new DemoTO();
					demoTO.setDemoName(subtestAccommodationTO.getName());
					demoTO.setDemovalue(subtestAccommodationTO.getValue());
					demoList.add(demoTO);
				}
				insertIntoStgStdDemoDetails(demoList, studentDataLoadTO, contentCode);
			}
		}
	}

	/**
	 * Create STBTEST details
	 * 
	 * @param contentDetailsTO
	 * @param studentDataLoadTO
	 * @param subtestDetailsId
	 */
	private void createContentDetails(ContentDetailsTO contentDetailsTO, 
			StudentDataLoadTO studentDataLoadTO, long subtestDetailsId, String testForm) {
		int count = getJdbcTemplatePrism().update(
	        IQueryConstants.CREATE_STG_SUBTEST_DETAILS,
	        //subtestDetailsId, //1
	        studentDataLoadTO.getStudentBioDetailsId(), //2
	        contentDetailsTO.getStatusCode(), //3
	        contentDetailsTO.getContentCode(), // content name //4
	        contentDetailsTO.getScoringMethod(), // scoring status //5
	        testForm, // test form //6
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
	
	/**
	 * Insert job details
	 * @param jobTrackingTO
	 */
	public JobTrackingTO insertIntoJobTracking(JobTrackingTO jobTrackingTO) throws Exception {

		long jobId = getJdbcTemplatePrism().queryForLong(IQueryConstants.JOB_SEQ_ID);
		
		String job_name = jobTrackingTO.getJobName();
		String extract_enddate = jobTrackingTO.getExtractEnddate();
		String extract_category = jobTrackingTO.getExtractCategory();
		String extract_filetype = jobTrackingTO.getExtractFiletype();
		String request_type = jobTrackingTO.getRequestType();
		String request_summary = jobTrackingTO.getRequestSummary();
		String request_details = jobTrackingTO.getRequestDetails();
		String request_filename = jobTrackingTO.getRequestFilename();
		String request_email = jobTrackingTO.getRequestEmail();
		String job_log = jobTrackingTO.getJobLog();
		String job_status = jobTrackingTO.getJobStatus();
		long number = jobTrackingTO.getNumber();
		//String adminid = jobTrackingTO.getAdminId();
		String customerid = jobTrackingTO.getCustomerId();

		if (jobTrackingTO.getExtractStartdate() == null || extract_enddate == null) {
			getJdbcTemplatePrism().update(IQueryConstants.INSERT_JOB_TRACKING_DEFAULT_DATE, jobId,
					jobTrackingTO.getUserId(), job_name, extract_category,
					extract_filetype, request_type, request_summary,
					request_details, request_filename, request_email, job_log,
					job_status, customerid,
					jobTrackingTO.getOtherRequestparams());
		} else {
			getJdbcTemplatePrism().update(IQueryConstants.INSERT_JOB_TRACKING_DATE, jobId,
					jobTrackingTO.getUserId(), job_name,
					jobTrackingTO.getExtractStartdate(), extract_enddate,
					extract_category, extract_filetype, request_type,
					request_summary, request_details, request_filename,
					request_email, job_log, job_status, customerid,
					jobTrackingTO.getOtherRequestparams());
		}
		jobTrackingTO.setJobId(jobId);
		return jobTrackingTO;
	}

	/**
	 * Deletes all records from STG_HIER_DETAILS, STG_LSTNODE_HIER_DETAILS,
	 * STG_STD_BIO_DETAILS, STG_STD_DEMO_DETAILS, STG_STD_SUBTEST_DETAILS,
	 * STG_STD_OBJECTIVE_DETAILS, STG_ITEM_RESPONSE_DETAILS for a given partion
	 * 
	 * @param partitionName
	 */
	private void cleanStagingTables(String partitionName) {
		logger.log(IAppLogger.INFO, "partitionName = " + partitionName);
		int count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_HIER_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_HIER_DETAILS = " + count);
		count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_LSTNODE_HIER_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_LSTNODE_HIER_DETAILS = " + count);
		count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_STD_BIO_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_STD_BIO_DETAILS = " + count);
		count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_STD_DEMO_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_STD_DEMO_DETAILS = " + count);
		count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_STD_SUBTEST_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_STD_SUBTEST_DETAILS = " + count);
		count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_STD_OBJECTIVE_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_STD_OBJECTIVE_DETAILS = " + count);
		count = getJdbcTemplatePrism().update(IQueryConstants.CLEAN_STG_ITEM_RESPONSE_DETAILS, partitionName);
		logger.log(IAppLogger.INFO, "STG_ITEM_RESPONSE_DETAILS = " + count);
	}

	/**
	 * this fetch student data posted from OAS
	 * @return all StudentListTO in a map with key = WS_ROSTER_DATA.WS_ROSTER_DATA_ID
	 */
	public Map<Long, StudentListTO> getStudentListTO() {
		logger.log(IAppLogger.INFO, "Enter getStudentListTO ");
		Map<Long, StudentListTO> studentMapTOs = new HashMap<Long, StudentListTO>();
		StudentListTO studentListTO = null;
		long wsRosterDataId = 0;

		List<Map<String, Object>> lstData = getJdbcTemplatePrism()
				.queryForList(
						IQueryConstants.GET_WS_ROSTER_DATA,
						IApplicationConstants.WS_ROSTER_DATA_STATUS.NR
								.toString());
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {

				wsRosterDataId = ((BigDecimal) fieldDetails
						.get("WS_ROSTER_DATA_ID")).longValue();
			//	Clob requestObj = (Clob) fieldDetails.get("REQUEST_OBJECT");
				String requestObj = (String)fieldDetails.get("REQUEST_OBJECT");
				requestObj = requestObj.replaceAll("com.ctb.prism.web.controller", "com.ctb.prism.webservice.transferobject");
				/*studentListTO = convertByteArrayStudentListTO(requestObj,
						wsRosterDataId);*/
				XStream xmlDriver = new XStream();
				studentListTO = (StudentListTO) xmlDriver.fromXML(requestObj);
				if (studentListTO != null) {
					updateWSRosterData(IApplicationConstants.WS_ROSTER_DATA_STATUS.IP.toString(),"",wsRosterDataId);
					studentMapTOs.put(wsRosterDataId, studentListTO);
				}
			}
		}
		logger.log(IAppLogger.INFO, "Exit getStudentListTO ");
		return studentMapTOs;

	}

	/**
	 * @deprecated
	 */
	private StudentListTO convertByteArrayStudentListTO(String requestObj,//Clob requestObj,
			long wsRosterDataId) {
		StudentListTO studentListTO = null;
		logger.log(IAppLogger.INFO,
				"Enter convertByteArrayStudentListTO with ws_roster_id "
						+ wsRosterDataId);
		try {
			//byte[] data = IOUtils.toByteArray(requestObj.getAsciiStream());
			byte[] data = requestObj.getBytes();
			Object obj = Utils.deserialize(data);
			if (obj instanceof StudentListTO) {
				studentListTO = (StudentListTO) obj;
				// Update to in progress
				updateWSRosterData(IApplicationConstants.WS_ROSTER_DATA_STATUS.IP.toString(),"",wsRosterDataId);
			}
		/*} catch (IOException e) {
			updateWSRosterData(
					IApplicationConstants.WS_ROSTER_DATA_STATUS.ER.toString(),
					CustomStringUtil.appendString("Error:", e.getMessage()),
					wsRosterDataId);
			logger.log(IAppLogger.ERROR, e.getMessage());
		} catch (SQLException e) {
			updateWSRosterData(
					IApplicationConstants.WS_ROSTER_DATA_STATUS.ER.toString(),
					CustomStringUtil.appendString("Error:", e.getMessage()),
					wsRosterDataId);
			logger.log(IAppLogger.ERROR, e.getMessage());*/
		} catch (Exception e) {
			updateWSRosterData(
					IApplicationConstants.WS_ROSTER_DATA_STATUS.ER.toString(),
					CustomStringUtil.appendString("Error:", e.getMessage()),
					wsRosterDataId);
			logger.log(IAppLogger.ERROR, e.getMessage());
		}
		logger.log(IAppLogger.INFO, "Exit convertByteArrayStudentListTO ");
		return studentListTO;
	}

	/**
	 * Update WS data consuming status
	 * @param status
	 * @param msg
	 * @param processid
	 * @return
	 */
	public int updateWSRosterData(String status, String msg, long processid) {

		int count = getJdbcTemplatePrism().update(
				IQueryConstants.UPDATE_WS_ROSTER_DATA, status, msg, processid);

		return count;
	}
	
	
	/**
	 * Update WS data consuming status
	 * @param StudentDataLoadTO
	 * @param wsRosterDataId
	 * @return
	 */
	public int updateWSRosterData(StudentDataLoadTO studentDataLoadTO, long wsRosterDataId) {
		logger.log(IAppLogger.INFO, "ENTER updateWSRosterData with WS_ROSTER_DATA_ID: " +wsRosterDataId );
		String status = studentDataLoadTO.getStatus();
		
		if("SUCCESS".equals(studentDataLoadTO.getStatus())) 
			status = IApplicationConstants.WS_ROSTER_DATA_STATUS.CO.toString();
		else 
			status = IApplicationConstants.WS_ROSTER_DATA_STATUS.ER.toString();
		String errorMessage = "";
		
		if((studentDataLoadTO.getErrorMessages() != null)) {
			for(String errorMsg : studentDataLoadTO.getErrorMessages()) {
				errorMessage = CustomStringUtil.appendString(errorMessage, "\n", errorMsg);
			}
		}
		int count = getJdbcTemplatePrism().update(
				IQueryConstants.UPDATE_WS_ROSTER_DATA,status,
					CustomStringUtil.appendString("Process Id: " +studentDataLoadTO.getProcessId(),
												  " Partotion Name: "+studentDataLoadTO.getPartitionName(),
												  " ", errorMessage),
					wsRosterDataId);
		logger.log(IAppLogger.INFO, "Exit updateWSRosterData ");
		return count;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#getFileSize(java.lang.String)
	 */
	public JobTrackingTO getFileSize(String jobId) {
		JobTrackingTO jobTrackingTO = null;
		List<Map<String, Object>> lstData = new ArrayList<Map<String, Object>>();
		logger.log(IAppLogger.INFO, "Get File Size by JobID: " +jobId);
		if(jobId != null)
			lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_FILE_SIZE, jobId);
		
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				jobTrackingTO = new JobTrackingTO();
				jobTrackingTO.setJobId(((BigDecimal)fieldDetails.get("JOB_ID")).longValue());
				jobTrackingTO.setRequestFilename((String)(fieldDetails.get("REQUEST_FILENAME")));
				jobTrackingTO.setFileSize((String)(fieldDetails.get("FILE_SIZE")));
			}
		}
		return jobTrackingTO;
	}

	/* (non-Javadoc)
	 * @see com.ctb.prism.core.dao.IUsabilityDAO#updateFileSize(com.ctb.prism.core.transferobject.JobTrackingTO)
	 */
	public JobTrackingTO updateFileSize(JobTrackingTO jobTrackingTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_FILE_SIZE,jobTrackingTO.getFileSize(),jobTrackingTO.getJobId());
		jobTrackingTO.setSuccess(true);
		return jobTrackingTO;
	}
	
	public void generateStudentXMLExtract(Map<String, Object> paramMap){
		final Long customerId =  Long.parseLong((String)paramMap.get("customerId"));
		final String startDate = ((String)paramMap.get("startDate")).replaceAll("/", "");
		final String endDate = ((String)paramMap.get("endDate")).replaceAll("/", "");
		final String contractName  = (String)paramMap.get("contractName");
		logger.log(IAppLogger.INFO, "generateStudentXMLExtract() is being called" );
		logger.log(IAppLogger.INFO,"customerId : "+customerId);
		logger.log(IAppLogger.INFO,"startDate : "+startDate);
		logger.log(IAppLogger.INFO,"endDate : "+endDate);
		
		boolean status = false;
		long t1 = System.currentTimeMillis();
		try {
			status =  (Boolean) getJdbcTemplatePrism(contractName).execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					cs = con.prepareCall(IQueryConstants.SP_CUSTOMER_STD_EXTRACT_ONLINE);
					cs.setLong(1, customerId);
					cs.setLong(2,  startDate.length()>0 || endDate.length()>0 ? -1L : 30L);
					cs.setString(3, startDate.length()>0 ? startDate.replaceAll("/", ""):"NA");
					cs.setString(4, endDate.length()>0 ? endDate.replaceAll("/", ""): "NA");
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					try {
						cs.execute();						
					} catch (SQLException e) {
        				e.printStackTrace();
        				return false;
        			}
					logger.log(IAppLogger.INFO, "generateStudentXMLExtract() has been called" );
        			return true;
				}	
			});
		} catch(Exception e){
			e.printStackTrace();			
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ReportDAOImpl - deleteReport() took time: "+String.valueOf(t2 - t1)+"ms");
		}		
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.parent.dao.IParentDAO#getContentForEdit(java.util.Map)
	 */
	public StudentDataExtractTO getClobXMLFile(final Map<String, Object> paramMap) {
		logger.log(IAppLogger.INFO, "Enter: UsabilityDAOImpl - getClobXMLFile()");
		long t1 = System.currentTimeMillis();
		StudentDataExtractTO studentDataExtractTO = null;
		final long customer = Long.valueOf((String)paramMap.get("customer"));
		final long jobId = Long.valueOf((String)paramMap.get("jobId")); 

		try {
			studentDataExtractTO = (StudentDataExtractTO) getJdbcTemplatePrism().execute(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call " + IQueryConstants.GET_CLOB_XML_FILE + "}");
					cs.setLong(1, jobId);
					cs.setLong(2, customer);
					cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
					cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
					return cs;
				}
			}, new CallableStatementCallback<Object>() {
				public Object doInCallableStatement(CallableStatement cs) {
					ResultSet rs = null;
					StudentDataExtractTO studentDataExtractTOResult = null;
					try {
						cs.execute();
						rs = (ResultSet) cs.getObject(3);
						if (rs.next()) {
							studentDataExtractTOResult = new StudentDataExtractTO();
							studentDataExtractTOResult.setJobId(rs.getLong("JOB_ID"));
							studentDataExtractTOResult.setCustomerId(rs.getLong("CUSTOMERID"));
							studentDataExtractTOResult.setStudentDataXML(Utils.convertClobToString((Clob) rs.getClob("STUDENTDATA_XML")));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return studentDataExtractTOResult;
				}
			});
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
		} finally {
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: UsabilityDAOImpl - getClobXMLFile() took time: " + String.valueOf(t2 - t1) + "ms");
		}
		return studentDataExtractTO;
	}
	
	public void storeWebserviceLog(StudentListTO studentListTO, StudentDataLoadTO studentDataLoadTO) {
		if(studentListTO.getRosterDetailsTO() != null) {
			for(RosterDetailsTO rosterDetailsTO : studentListTO.getRosterDetailsTO() ) {
				StudentDetailsTO studentDetailsTO = rosterDetailsTO.getStudentDetailsTO();
				StudentBioTO studentBioTO = (studentDetailsTO != null)? studentDetailsTO.getStudentBioTO() : null;
				CustHierarchyDetailsTO custHierarchyDetailsTO = rosterDetailsTO.getCustHierarchyDetailsTO();
				
				String listString = "";
				for (String msg : studentDataLoadTO.getErrorMessages()) {
				    listString += msg + "\n";
				}
				
				getJdbcTemplatePrism().update(IQueryConstants.STORE_WS_LOG, 
						studentDataLoadTO.getProcessId(), 
						rosterDetailsTO.getRosterId(),
						(custHierarchyDetailsTO.getCollOrgDetailsTO() != null && !custHierarchyDetailsTO.getCollOrgDetailsTO().isEmpty())? custHierarchyDetailsTO.getCollOrgDetailsTO().get(0).getOrgCode() : "",
						(studentBioTO != null) ? studentBioTO.getExamineeId() : "",
						studentDataLoadTO.getStatus(),
						studentDataLoadTO.getStatusCode(),
						studentDataLoadTO.getSummary(),
						listString);
			}
		}
		
	}

	
}
