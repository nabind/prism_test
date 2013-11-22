package com.ctb.prism.inors.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.business.IInorsBusiness;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.ObjectValueTO;

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

	/* (non-Javadoc)
	 * @see com.ctb.prism.inors.service.IInorsService#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		return inorsBusiness.getDownloadData(paramMap);
	}

	public List<ObjectValueTO> getOrgNodes(String orgNodeLevel) {
		return inorsBusiness.getOrgNodes(orgNodeLevel);
	}
	
	public List<ObjectValueTO> populateSchool(Long parentOrgNodeId){
		return inorsBusiness.populateSchool(parentOrgNodeId);
	}
	
}
