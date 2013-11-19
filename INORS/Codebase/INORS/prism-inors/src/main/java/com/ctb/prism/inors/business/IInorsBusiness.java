package com.ctb.prism.inors.business;

import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.JobTO;


public interface IInorsBusiness {
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO getJob(String jobId);
	public void batchPDFDownload(String jobId);
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO);
}
