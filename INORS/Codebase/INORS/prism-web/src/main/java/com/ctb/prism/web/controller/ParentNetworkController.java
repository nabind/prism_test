package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.web.util.JsonUtil;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.Gson;

@Controller
public class ParentNetworkController {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ParentNetworkController.class.getName());
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private IParentService parentService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	/*
	 * Get Student's sub test and overview content
	 */
	@RequestMapping(value="/getChildData", method=RequestMethod.GET)
	public ModelAndView getChildData(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException{
		
		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getChildData()");
		long t1 = System.currentTimeMillis();
		ModelAndView modelAndView = new ModelAndView("parent/studentSubtest");
		Map<String,Object> childDataMap = null;
		String studentBioId = request.getParameter("studentBioId");
		
		request.getSession().setAttribute(IApplicationConstants.PARENT_REPORT, IApplicationConstants.TRUE);
		request.getSession().setAttribute(IApplicationConstants.STUDENT_BIO_ID, studentBioId);
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("REPORT_NAME",  IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.PRODUCT_SPECIFIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.CHILDREN_OVERVIEW);
		paramMap.put("studentBioId", studentBioId);
		
		try{
			childDataMap = parentService.getChildData(paramMap);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getChildData() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		modelAndView.addObject("childDataMap", childDataMap);
		return modelAndView;
	}
}
