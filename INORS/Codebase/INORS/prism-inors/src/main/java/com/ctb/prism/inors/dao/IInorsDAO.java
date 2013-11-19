package com.ctb.prism.inors.dao;

import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.JobTO;


public interface IInorsDAO {
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO getJob(String jobId);
	public BulkDownloadTO updateStatus(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO updateJobLog(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO updateJobStatusAnsLog(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO updateJob(BulkDownloadTO bulkDownloadTO);
	public BulkDownloadTO getSchoolClass(String studentBioId);
	public BulkDownloadTO getNodeName(String orgNodeid);
}
