package com.ctb.prism.report.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.report.business.IRescoreRequestBusiness;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

@Service("rescoreRequestService")
public class RescoreRequestServiceImpl implements IRescoreRequestService {

	@Autowired
	private IRescoreRequestBusiness rescoreRequestBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.service.IRescoreRequestService#getRescoreRequestForm
	 * (java.util.Map)
	 */
	public Map<String, Object> getRescoreRequestForm(Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestBusiness.getRescoreRequestForm(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.service.IRescoreRequestService#submitRescoreRequest
	 * (java.util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO submitRescoreRequest(final Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestBusiness.submitRescoreRequest(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.service.IRescoreRequestService#resetItemState(java
	 * .util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemState(final Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestBusiness.resetItemState(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.service.IRescoreRequestService#resetItemDate(java
	 * .util.Map)
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemDate(final Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestBusiness.resetItemDate(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ctb.prism.report.service.IRescoreRequestService#getNotDnpStudentDetails
	 * (java.util.Map)
	 */
	public RescoreRequestTO getNotDnpStudentDetails(Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestBusiness.getNotDnpStudentDetails(paramMap);
	}
}
