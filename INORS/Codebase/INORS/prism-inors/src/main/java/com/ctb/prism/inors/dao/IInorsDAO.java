package com.ctb.prism.inors.dao;

import java.util.List;
import java.util.Map;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.ObjectValueTO;

public interface IInorsDAO {

	/**
	 * Create job for download
	 * 
	 * @param bulkDownloadTO
	 * @return
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);

	/**
	 * get job details
	 * 
	 * @param jobId
	 * @return
	 */
	public BulkDownloadTO getJob(String jobId);

	/**
	 * update job status
	 * 
	 * @param bulkDownloadTO
	 * @return
	 */
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO);

	/**
	 * update job log
	 * 
	 * @param bulkDownloadTO
	 * @return
	 */
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO);

	/**
	 * update job status and log
	 * 
	 * @param bulkDownloadTO
	 * @return
	 */
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO);

	/**
	 * update job after completion
	 * 
	 * @param bulkDownloadTO
	 * @return
	 */
	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO);

	/**
	 * Get class and school for the student
	 * 
	 * @param studentBioId
	 * @return
	 */
	public BulkDownloadTO getSchoolClass(String studentBioId);

	/**
	 * Get name for a node id
	 * 
	 * @param orgNodeid
	 * @return
	 */
	public BulkDownloadTO getNodeName(String orgNodeid);

	/**
	 * Method to download GRT/Invitation Code Files
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap);

	/**
	 * Returns the ORG_NODE_NAME-ORG_NODEID list by ORG_NODE_LEVEL
	 * 
	 * @return
	 */
	public List<ObjectValueTO> getOrgNodes(String orgNodeLevel);

	public List<com.ctb.prism.inors.transferobject.ObjectValueTO> populateSchool(Long parentOrgNodeId);
}
