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
import com.ctb.prism.report.transferobject.ReportTO;
import com.ctb.prism.web.util.JsonUtil;
import com.ctb.prism.webservice.transferobject.StudentDetailsTO;
 
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
		String studentName = request.getParameter("studentName");
		long studentGradeId = Long.parseLong(request.getParameter("studentGradeId"));
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		request.getSession().setAttribute(IApplicationConstants.PARENT_REPORT, IApplicationConstants.TRUE);
		request.getSession().setAttribute(IApplicationConstants.STUDENT_BIO_ID, studentBioId);
		request.getSession().setAttribute(IApplicationConstants.STUDENT_GRADEID, studentGradeId);
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("REPORT_NAME",  IApplicationConstants.PRODUCT_SPECIFIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.PRODUCT_SPECIFIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.CHILDREN_OVERVIEW);
		paramMap.put("customerId", loggedinUserTO.getCustomerId());
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("userId", loggedinUserTO.getUserId());
		
		try{
			childDataMap = parentService.getChildData(paramMap);
			childDataMap.put("studentName", studentName);
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
	
	/**
	 * @author Arunava
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * Get Student's standards
	 */
	
	@RequestMapping(value="/getArticleTypeDetails", method=RequestMethod.GET)
	public ModelAndView getArticleTypeDetails(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getArticleTypeDetails()");
		long t1 = System.currentTimeMillis();
		ModelAndView modelAndView = new ModelAndView("parent/studentStandards");
		final Map<String,Object> paramMap = new HashMap<String,Object>(); 
		final long studentBioId = Long.parseLong((String) request.getSession().getAttribute(IApplicationConstants.STUDENT_BIO_ID)); 
		final long subtestId = Long.parseLong(request.getParameter("subtestId")); 
		final long studentGradeId = (Long) request.getSession().getAttribute(IApplicationConstants.STUDENT_GRADEID);
		final String contentType = IApplicationConstants.CONTENT_TYPE_ACT;
		final String studentName = request.getParameter("studentName");
		List<ManageContentTO> articleTypeDetailsList=null;
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("studentName", studentName);
		paramMap.put("studentGradeId", studentGradeId);
		paramMap.put("subtestId", subtestId);
		paramMap.put("contentType", contentType);
		try{
			articleTypeDetailsList = parentService.getArticleTypeDetails(paramMap);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getArticleTypeDetails() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		modelAndView.addObject("studentName", studentName);
		modelAndView.addObject("articleTypeDetailsList", articleTypeDetailsList);
		return modelAndView;
	}
	
	public ModelAndView getArticleDescription(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getArticleDescription()");
		long t1 = System.currentTimeMillis();
		ModelAndView modelAndView = new ModelAndView("parent/contentDetails");
		final Map<String,Object> paramMap = new HashMap<String,Object>(); 
		final long articleId = Long.parseLong((String) paramMap.get("articleId")); 
		final String contentType = (String) paramMap.get("contentType");
		ManageContentTO manageContentTO=null;
		paramMap.put("articleId", articleId);
		paramMap.put("contentType", contentType);
		try{
			manageContentTO = parentService.getArticleDescription(paramMap);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getArticleDescription() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		modelAndView.addObject("articleTypeDescription", manageContentTO);
		return modelAndView;
	}




}
