package com.ctb.prism.inors.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ctb.prism.core.constant.IQueryConstants;
import com.ctb.prism.core.dao.BaseDAO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.transferobject.BaseTO;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		String type = paramMap.get("type");
		String layout = paramMap.get("layout");
		logger.log(IAppLogger.INFO, "type=" + type + ", layout=" + layout);
		if ("IC".equals(type)) {
			if ("2012".equals(layout) || "2013".equals(layout)) {
				// TODO : database code instead of mock objects
				List<InvitationCodeTO> icData = new ArrayList<InvitationCodeTO>();
				icData.add(InorsDownloadUtil.getMockInvitationCodeTO());
				return icData;
			}
		} else if ("GRT".equals(type)) {
			if ("2010".equals(layout) || "2011".equals(layout) || "2012".equals(layout) || "2013".equals(layout)) {
				// TODO : database code instead of mock objects
				List<GrtTO> icData = new ArrayList<GrtTO>();
				icData.add(InorsDownloadUtil.getMockGRTTO());
				return icData;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.dao.IInorsDAO#getOrgNodes()
	 */
	public List<ObjectValueTO> getOrgNodes(String orgNodeLevel) {
		List<ObjectValueTO> objectValueList = new ArrayList<ObjectValueTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORG_NODES_BY_LEVEL, orgNodeLevel);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				ObjectValueTO objectValueTO = new ObjectValueTO();
				objectValueTO.setName(fieldDetails.get("ORG_NODE_NAME").toString());
				objectValueTO.setValue(fieldDetails.get("ORG_NODEID").toString());
				objectValueList.add(objectValueTO);
			}
		}
		return objectValueList;
	}
	
	public List<ObjectValueTO> populateSchool(Long parentOrgNodeId){
		List<ObjectValueTO> objectValueList = new ArrayList<ObjectValueTO>();
		List<Map<String, Object>> lstData = null;
		lstData = getJdbcTemplatePrism().queryForList(IQueryConstants.GET_ORG_NODES_BY_PARENT, parentOrgNodeId);
		if (lstData.size() > 0) {
			for (Map<String, Object> fieldDetails : lstData) {
				ObjectValueTO objectValueTO = new ObjectValueTO();
				objectValueTO.setName(fieldDetails.get("ORG_NODE_NAME").toString());
				objectValueTO.setValue(fieldDetails.get("ORG_NODEID").toString());
				objectValueList.add(objectValueTO);
			}
		}
		return objectValueList;
	}
}
