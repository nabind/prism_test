package com.ctb.prism.inors.service;

import java.util.List;
import java.util.Map;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.ObjectValueTO;

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

	public List<ObjectValueTO> getDistricts();

	public List<ObjectValueTO> populateSchool(Long parentOrgNodeId);

}
