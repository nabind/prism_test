package com.ctb.prism.inors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.inors.business.IInorsBusiness;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.JobTO;

@Service("inorsService")
public class InorsServiceImpl implements IInorsService {

	@Autowired
	private IInorsBusiness inorsBusiness;
	
	@Async
	public void batchPDFDownload(String jobId) {
		inorsBusiness.batchPDFDownload(jobId);
	}
	
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO) {
		return inorsBusiness.createJob(bulkDownloadTO);
	}
	
	public BulkDownloadTO getJob(String jobId) {
		return inorsBusiness.getJob(jobId);
	}
	
}
