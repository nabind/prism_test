package com.ctb.prism.report.service;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.report.business.IRescoreRequestBusiness;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service("rescoreRequestService")
public class RescoreRequestServiceImpl implements IRescoreRequestService {

	@Autowired
	private IRescoreRequestBusiness rescoreRequestBusiness;
	
	public List<RescoreRequestTO> getDnpStudentList(Map<String, Object> paramMap) throws BusinessException {
		return rescoreRequestBusiness.getDnpStudentList(paramMap);
	}
	
	public com.ctb.prism.core.transferobject.ObjectValueTO submitRescoreRequest(final Map<String, Object> paramMap) throws BusinessException{
		return rescoreRequestBusiness.submitRescoreRequest(paramMap);
	}
	
	public com.ctb.prism.core.transferobject.ObjectValueTO resetItemState(final Map<String, Object> paramMap) throws BusinessException{
		return rescoreRequestBusiness.resetItemState(paramMap);
	}
}
