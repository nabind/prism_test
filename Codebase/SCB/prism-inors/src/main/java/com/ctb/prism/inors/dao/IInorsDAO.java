package com.ctb.prism.inors.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.StudentPDFLogTO;

/**
 * @author TCS
 * 
 */
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
	public JobTrackingTO getJob(String jobId, String contractName);

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
	public JobTrackingTO updateJobStatusAndLog(JobTrackingTO bulkDownloadTO);

	/**
	 * update job after completion
	 * 
	 * @param bulkDownloadTO
	 * @return
	 */
	public JobTrackingTO updateJob(JobTrackingTO bulkDownloadTO);

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
	 * @param paramMap
	 * @param rowDataLayout
	 * @return
	 */
	public ArrayList<ArrayList<String>> getTabulerData(Map<String, String> paramMap, ArrayList<String> aliasList, ArrayList<String> headerList);

	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGrt(Map<String, String> paramMap);

	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGrt(Map<String, String> paramMap);

	/**
	 * Gets the Product name for a given Product id.
	 * 
	 * @param productId
	 * @return
	 */
	public String getProductNameById(Long productId);

	/**
	 * @return
	 */
	public String getCurrentAdminYear();
	
	/**
	 * @author Joy Kumar Pal
	 * @param paramMap
	 * @return returnMap
	 */
	public Map<String,Object> getCode(Map<String,Object> paramMap);
	
	/**
	 * @author Joy Kumar Pal
	 * @param paramMap
	 * @return returnMap
	 */
	public Map<String,Object> getTpCode(Map<String,Object> paramMap);

	/*
	 * @author Abir
	 * @param
	 */
	public void auditPDFUtiity(StudentPDFLogTO studentPDFLogTO); 
}
