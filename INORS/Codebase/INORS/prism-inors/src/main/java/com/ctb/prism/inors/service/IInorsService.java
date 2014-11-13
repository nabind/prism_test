package com.ctb.prism.inors.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.core.transferobject.JobTrackingTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;

/**
 * @author TCS
 * 
 */
public interface IInorsService {
	/**
	 * @param jobId
	 */
	public void asyncPDFDownload(String jobId, String contractName);

	/**
	 * @param bulkDownloadTO
	 * @return
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);

	/**
	 * @param jobId
	 * @return
	 */
	public JobTrackingTO getJob(String jobId);

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

}
