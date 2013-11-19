package com.ctb.prism.inors.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.JobTO;

/**
 * This class is responsible for reading and writing to database.
 * The transactions through this class should be related to report only.
 */
@Repository("inorsDAO")
public class InorsDAOImpl extends BaseDAO implements IInorsDAO {

	/**
	 * Create job for download
	 * 
	 * @param bulkDownloadTO
	 * @return boolean
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
	
	/**
	 * update job status
	 * 
	 * @param bulkDownloadTO
	 * @return boolean
	 */
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_STATUS, 
				bulkDownloadTO.getStatus(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 
	
	/**
	 * update job log
	 * 
	 * @param bulkDownloadTO
	 * @return boolean
	 */
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_LOG, 
				bulkDownloadTO.getLog(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 
	
	/**
	 * update job status and log
	 * 
	 * @param bulkDownloadTO
	 * @return boolean
	 */
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO) {
		getJdbcTemplatePrism().update(IQueryConstants.UPDATE_STATUS_AND_LOG, 
				bulkDownloadTO.getStatus(),
				bulkDownloadTO.getLog(),
				bulkDownloadTO.getJobId());
		bulkDownloadTO.setDbStatus(true);
		return bulkDownloadTO;
	} 
	
	/**
	 * update job after completion
	 * 
	 * @param bulkDownloadTO
	 * @return boolean
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
	
	/**
	 * get job details 
	 * @param jobId
	 * @return
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
	
	/**
	 * Get class and school for the student
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
	
	/**
	 * Get name for a node id
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
	
	
}
