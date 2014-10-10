package com.ctb.prism.report.business;

import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

/**
 * @author TCS
 *
 */
public interface IRescoreRequestBusiness {

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getRescoreRequestForm(Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO submitRescoreRequest(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemState(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemDate(final Map<String, Object> paramMap) throws BusinessException;

	/**
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public RescoreRequestTO getNotDnpStudentDetails(Map<String, Object> paramMap) throws BusinessException;
}
