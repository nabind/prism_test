package com.ctb.prism.inors.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.transferobject.BaseTO;
import com.ctb.prism.inors.business.IInorsBusiness;
import com.ctb.prism.inors.transferobject.BulkDownloadTO;
import com.ctb.prism.inors.transferobject.LayoutTO;

/**
 * @author TCS
 * 
 */
@Service("inorsService")
public class InorsServiceImpl implements IInorsService {

	@Autowired
	private IInorsBusiness inorsBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#batchPDFDownload(java.lang.String)
	 */
	@Async
	public void batchPDFDownload(String jobId) {
		inorsBusiness.batchPDFDownload(jobId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#createJob(com.ctb.prism.inors.transferobject.BulkDownloadTO)
	 */
	public BulkDownloadTO createJob(BulkDownloadTO bulkDownloadTO) {
		return inorsBusiness.createJob(bulkDownloadTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#getJob(java.lang.String)
	 */
	public BulkDownloadTO getJob(String jobId) {
		return inorsBusiness.getJob(jobId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#getDownloadData(java.util.Map)
	 */
	public List<? extends BaseTO> getDownloadData(Map<String, String> paramMap) {
		return inorsBusiness.getDownloadData(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#populateDistrictGrt(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateDistrictGrt(Map<String, String> paramMap) {
		return inorsBusiness.populateDistrictGrt(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#populateSchoolGrt(java.util.Map)
	 */
	public List<com.ctb.prism.core.transferobject.ObjectValueTO> populateSchoolGrt(Map<String, String> paramMap) {
		return inorsBusiness.populateSchoolGrt(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#getProductNameById(java.lang.Long)
	 */
	public String getProductNameById(Long productId) {
		return inorsBusiness.getProductNameById(productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#getTableData(java.util.Map, java.util.ArrayList)
	 */
	public ArrayList<ArrayList<LayoutTO>> getTableData(Map<String, String> paramMap, ArrayList<LayoutTO> rowDataLayout) {
		return inorsBusiness.getTableData(paramMap, rowDataLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ctb.prism.inors.service.IInorsService#getCurrentAdminYear()
	 */
	public String getCurrentAdminYear() {
		return inorsBusiness.getCurrentAdminYear();
	}

}
