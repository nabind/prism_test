package com.ctb.prism.inors.service;

import java.util.List;
import java.util.Map;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;

public interface IInorsService {
	public void batchPDFDownload(String jobId);

	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);

	public BulkDownloadTO getJob(String jobId);

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

}
