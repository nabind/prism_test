package com.ctb.prism.inors.business;

import java.util.List;
import java.util.Map;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;

public interface IInorsBusiness {
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);

	public BulkDownloadTO getJob(String jobId);

	public void batchPDFDownload(String jobId);

	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO);

	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO);

	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO);

	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO);

	/**
	 * Method to download GRT/Invitation Code Files
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap);

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
}
