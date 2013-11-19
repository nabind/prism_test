package com.ctb.prism.inors.service;

import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.JobTO;


public interface IInorsService {
	public void batchPDFDownload(String jobId);
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO getJob(String jobId);
	
}
