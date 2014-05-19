package com.ctb.prism.report.dao;

import java.util.List;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

public interface IRescoreRequestDAO {
	
	public List<RescoreRequestTO> getDnpStudentList(Map<String, Object> paramMap)throws BusinessException;
	
	public com.ctb.prism.core.transferobject.ObjectValueTO submitRescoreRequest(final Map<String, Object> paramMap) throws BusinessException;
	
}
