package com.ctb.prism.web.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.GenericStack;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.service.IParentService;
import com.ctb.prism.parent.transferobject.ManageContentTO;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Joy
 * version 1.1
 */
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
	@RequestMapping(value="/getChildData")
	public ModelAndView getChildData(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException{
		
		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getChildData()");
		long t1 = System.currentTimeMillis();
		ModelAndView modelAndView = new ModelAndView("parent/studentSubtest");
		Map<String,Object> childDataMap = null;
		String studentBioId = request.getParameter("studentBioId");
		String testElementId = request.getParameter("testElementId");
		String studentName = request.getParameter("studentName");
		long studentGradeId = Long.parseLong(request.getParameter("studentGradeId"));
		String studentGradeName = request.getParameter("studentGradeName");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		request.getSession().setAttribute(IApplicationConstants.PARENT_REPORT, IApplicationConstants.TRUE);
		
		//Don't use this Parent Network flow. This is required for report. 
		request.getSession().setAttribute(IApplicationConstants.TEST_ELEMENT_ID, testElementId);
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("REPORT_NAME",  IApplicationConstants.GENERIC_REPORT_NAME);
		paramMap.put("MESSAGE_TYPE", IApplicationConstants.GENERIC_MESSAGE_TYPE);
		paramMap.put("MESSAGE_NAME", IApplicationConstants.CHILDREN_OVERVIEW);
		paramMap.put("customerId", loggedinUserTO.getCustomerId());
		paramMap.put("testElementId", testElementId);
		paramMap.put("custProdId", loggedinUserTO.getDefultCustProdId());
		
		try{
			childDataMap = parentService.getChildData(paramMap);
			childDataMap.put("studentBioId", studentBioId);
			childDataMap.put("studentName", studentName);
			childDataMap.put("studentGradeId", studentGradeId);
			childDataMap.put("testElementId", testElementId);
			childDataMap.put("studentGradeName", studentGradeName);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getChildData() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		modelAndView.addObject("childDataMap", childDataMap);
		request.getSession().setAttribute(IApplicationConstants.CHILD_MAP, childDataMap);
		return modelAndView;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * Get Standards/objective and associated activities for selected subtest,grade.
	 */
	@RequestMapping(value="/getStandardActivity")
	public ModelAndView getStandardActivity(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getStandardActivity()");
		long t1 = System.currentTimeMillis();
		
		//To implement back functionality - By Joy
		storeUrl(request);
		
		final Map<String,Object> paramMap = new HashMap<String,Object>(); 
		final long studentBioId = Long.parseLong(request.getParameter("studentBioId"));  
		final long subtestId = Long.parseLong(request.getParameter("subtestId")); 
		final long studentGradeId = Long.parseLong(request.getParameter("studentGradeId"));
		final String studentGradeName = request.getParameter("studentGradeName");
		final String studentName = request.getParameter("studentName");
		long custProdId = Long.parseLong(request.getParameter("custProdId")); 
		
		ModelAndView modelAndView = new ModelAndView("parent/standardActivity");
		if(studentBioId != 0){
			modelAndView.addObject("studentName", studentName);
		}else{
			modelAndView.addObject("studentName", "-1");
		}
		
		List<ManageContentTO> standardActivityDetailsList=null;
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("studentName", studentName);
		paramMap.put("studentGradeId", studentGradeId);
		paramMap.put("subtestId", subtestId);
		paramMap.put("contentType", IApplicationConstants.CONTENT_TYPE_ACT);
		paramMap.put("custProdId", custProdId);
		
		try{
			standardActivityDetailsList = parentService.getArticleTypeDetails(paramMap);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getStandardActivity() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		
		modelAndView.addObject("standardActivityDetailsList", standardActivityDetailsList);
		modelAndView.addObject("studentGradeName", studentGradeName);
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
	@RequestMapping(value="/getStandardIndicator")
	public ModelAndView getStandardIndicator(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException{
		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getStandardIndicator()");
		long t1 = System.currentTimeMillis();
		
		//To implement back functionality - By Joy
		storeUrl(request);
		
		final Map<String,Object> paramMap = new HashMap<String,Object>(); 
		final long studentBioId = Long.parseLong(request.getParameter("studentBioId"));  
		final long subtestId = Long.parseLong(request.getParameter("subtestId")); 
		final long studentGradeId = Long.parseLong(request.getParameter("studentGradeId"));
		final String studentGradeName = request.getParameter("studentGradeName");
		final String studentName = request.getParameter("studentName");
        long custProdId = Long.parseLong(request.getParameter("custProdId"));  
		
		ModelAndView modelAndView = new ModelAndView("parent/standardIndicator");
		if(studentBioId != 0){
			modelAndView.addObject("studentName", studentName);
		}else{
			modelAndView.addObject("studentName", "-1");
		}
		
		List<ManageContentTO> standardIndicatorDetailsList=null;
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("studentName", studentName);
		paramMap.put("studentGradeId", studentGradeId);
		paramMap.put("subtestId", subtestId);
		paramMap.put("contentType", IApplicationConstants.CONTENT_TYPE_IND);
		paramMap.put("custProdId", custProdId);
		
		try{
			standardIndicatorDetailsList = parentService.getArticleTypeDetails(paramMap);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getStandardIndicator() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		
		modelAndView.addObject("standardIndicatorDetailsList", standardIndicatorDetailsList);
		modelAndView.addObject("studentGradeName", studentGradeName);
		return modelAndView;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * Get description of any article like Standard/Activity/Indicator.
	 */
	@RequestMapping(value="/getArticleDescription")
	public ModelAndView getArticleDescription(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getArticleDescription()");
		long t1 = System.currentTimeMillis();
		ModelAndView modelAndView = new ModelAndView("parent/contentDetails");
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		long studentBioId = 0;
		long articleId = 0;
		String contentType = "";
		long subtestId = 0; 
		long studentGradeId = 0;
		String studentGradeName = "";
		long menuId = 0;
		long custProdId = 0;
		
		custProdId = Long.parseLong(request.getParameter("custProdId"));  
		studentBioId = Long.parseLong(request.getParameter("studentBioId"));  
		articleId = Long.parseLong(request.getParameter("articleId")); 
		contentType = (String)request.getParameter("contentType");
		studentGradeId = Long.parseLong(request.getParameter("studentGradeId"));
		studentGradeName = request.getParameter("studentGradeName");
		menuId = Long.parseLong(request.getParameter("menuId")); 
		String menuName = (String)request.getParameter("menuName");
		if(menuId ==  Long.parseLong(propertyLookup.get("menuId.content.rsc"))){
			subtestId = Long.parseLong(request.getParameter("subtestId")); 
		}
		ManageContentTO manageContentTO = null;
		
		paramMap.put("custProdId", custProdId);
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("articleId", articleId);
		paramMap.put("contentType", contentType);
		paramMap.put("subtestId", subtestId);
		paramMap.put("studentGradeId", studentGradeId);
		
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
		modelAndView.addObject("menuId", menuId);
		modelAndView.addObject("menuName", menuName);
		modelAndView.addObject("studentGradeName", studentGradeName);
		modelAndView.addObject("studentBioId", studentBioId);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/getBrowseContent", method=RequestMethod.GET)
	public ModelAndView getBrowseContent(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getBrowseContent()");
		ModelAndView modelAndView = new ModelAndView("parent/browseContent");
		return modelAndView;
	}
	
	@RequestMapping(value="/getStandardMatters", method=RequestMethod.GET)
	public ModelAndView getStandardMatters(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getStandardMatters()");
		ModelAndView modelAndView = new ModelAndView("parent/exploreStudentStandardMatter");
		return modelAndView;
	}
	
		
	@RequestMapping(value="/getEverydayActivity", method=RequestMethod.GET)
	public ModelAndView getEverydayActivity(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getEveryDayActivitiesDetails()");
		ModelAndView modelAndView = new ModelAndView("parent/everyDayActivitiesDetail");
		modelAndView.addObject("gradeName",request.getParameter("studentGradeName"));
		return modelAndView;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * Get grade and subtest/subject combination for different menu.
	 */
	@RequestMapping(value="/getGradeSubtestInfo")
	public ModelAndView getGradeSubtestInfo(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - getGradeSubtestInfo()");
		long t1 = System.currentTimeMillis();
		
		//To implement back functionality - By Joy
		storeUrl(request);
		
		ModelAndView modelAndView = new ModelAndView("parent/gradeSubject");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		long menuId = Long.parseLong(request.getParameter("menuId")); 
		String menuName = (String)request.getParameter("menuName");
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("loggedinUserTO", loggedinUserTO);
		
		List<ManageContentTO> gradeSubtestList=null;
		try{
			gradeSubtestList = parentService.getGradeSubtestInfo(paramMap);
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - getGradeSubtestInfo() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		modelAndView.addObject("gradeSubtestList", gradeSubtestList);
		modelAndView.addObject("menuId", menuId);
		modelAndView.addObject("menuName", menuName);
		return modelAndView;
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * To store the request URL in Stack - By Joy
	 */
	private void storeUrl(HttpServletRequest request)throws ServletException, IOException,BusinessException{
		String requestURL = "";
		requestURL = request.getServletPath() + "?" + request.getQueryString();
		GenericStack<String> urlStack = (GenericStack<String>)request.getSession().getAttribute(IApplicationConstants.URL_STACK);
		if(urlStack == null){
			urlStack = new GenericStack<String>();
		}
		urlStack.push(requestURL);
		request.getSession().setAttribute(IApplicationConstants.URL_STACK, urlStack);
	}
	
	/**
	 * @author Joy
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 * To implement back functionality - By Joy
	 */
	@RequestMapping(value="/historyBack", method=RequestMethod.GET)
	public void historyBack(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException,BusinessException{

		logger.log(IAppLogger.INFO, "Enter: ParentNetworkController - historyBack()");
		String requestURL = "";
		GenericStack<String> urlStack = (GenericStack<String>)request.getSession().getAttribute(IApplicationConstants.URL_STACK);
		if(!urlStack.isEmpty()){
			requestURL = urlStack.pop();
			//fromBack not required - By Joy
			//requestURL += "&fromBack=true";
		}else{
			logger.log(IAppLogger.ERROR, "URL STACK is empty");
		}
		request.getRequestDispatcher(requestURL).forward(request, response);
		logger.log(IAppLogger.INFO, "Exit: ParentNetworkController - historyBack()");	
	}

}
