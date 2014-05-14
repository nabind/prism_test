package com.ctb.prism.report.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.report.dao.IRescoreRequestDAO;
import com.ctb.prism.report.transferobject.RescoreRequestTO;

@Component("rescoreRequestBusiness")
public class RescoreRequestBusinessImpl implements IRescoreRequestBusiness {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(RescoreRequestBusinessImpl.class.getName());

	@Autowired
	private IRescoreRequestDAO rescoreRequestDAO;

	public List<RescoreRequestTO> getDnpStudentList(Map<String, Object> paramMap)throws BusinessException {
		return rescoreRequestDAO.getDnpStudentList(paramMap);
	}
}
