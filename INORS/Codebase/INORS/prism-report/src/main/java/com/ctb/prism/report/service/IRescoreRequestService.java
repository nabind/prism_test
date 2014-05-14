package com.ctb.prism.report.service;

import java.util.List;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

public interface IRescoreRequestService {
	
	public List<RescoreRequestTO> getDnpStudentList(Map<String, Object> paramMap) throws BusinessException;

}
