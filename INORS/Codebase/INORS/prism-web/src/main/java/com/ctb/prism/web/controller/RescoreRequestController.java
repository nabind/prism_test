package com.ctb.prism.web.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.GenericStack;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.service.IRescoreRequestService;
import com.ctb.prism.report.transferobject.InputControlTO;
 
import com.ctb.prism.report.transferobject.RescoreStudentTO;

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
public class RescoreRequestController {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(RescoreRequestController.class.getName());
	
	@Autowired	
	private InorsController inorsController;
	
	@Autowired	
	private IReportService reportService;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private IRescoreRequestService rescoreRequestService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	
	/**
	 * Opens the Rescore Request Form and populate required data.
	 * 
	 * @return
	 * @throws ServletException, IOException,BusinessException
	 */
	@RequestMapping(value = "/rescoreRequestForm", method = RequestMethod.GET)
	public ModelAndView rescoreRequestForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestController - rescoreRequestForm()");
		long t1 = System.currentTimeMillis();
		
		ModelAndView modelAndView = new ModelAndView("report/rescoreRequestForm");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		
		String reportUrl = (String) request.getParameter("reportUrl");
		String reportId = (String) request.getParameter("reportId");
		String testAdministrationVal = (String) request.getParameter("p_test_administration");
		String testProgram = (String) request.getParameter("p_test_program");
		String corpDiocese = (String) request.getParameter("p_corpdiocese");
		String school = (String) request.getParameter("p_school");
		String grade = (String) request.getParameter("p_grade");
		
		logger.log(IAppLogger.INFO, "testAdministration=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);
		logger.log(IAppLogger.INFO, "grade=" + grade);
		logger.log(IAppLogger.INFO, "reportUrl=" + reportUrl);
		
		try{
			if ((testAdministrationVal == null) || ("null".equalsIgnoreCase(testAdministrationVal))) {
				Map<String, Object> parameters = inorsController.getReportParameters(request, reportUrl);
				request.getSession().setAttribute(IApplicationConstants.REPORT_TYPE_CUSTOM + "parameters" + reportUrl, parameters);
				if ((parameters != null) && (!parameters.isEmpty())) {
					testAdministrationVal = CustomStringUtil.getNotNullString(parameters.get("p_test_administration"));
					testProgram = CustomStringUtil.getNotNullString(parameters.get("p_test_program"));
					corpDiocese = CustomStringUtil.getNotNullString(parameters.get("p_corpdiocese"));
					school = CustomStringUtil.getNotNullString(parameters.get("p_school"));
					grade = CustomStringUtil.getNotNullString(parameters.get("p_grade"));
				}
			} else {
				Map<String, String[]> sessionParams = (Map<String, String[]>) request.getSession().getAttribute("_REMEMBER_ME_ALL_");
				if(sessionParams == null) sessionParams = new HashMap<String, String[]>();
				Iterator itr = request.getParameterMap().entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) itr.next();
					request.getSession().setAttribute("_REMEMBER_ME_" + mapEntry.getKey(), mapEntry.getValue());
					sessionParams.put((String) mapEntry.getKey(), (String[]) mapEntry.getValue());
				}
				request.getSession().setAttribute("_REMEMBER_ME_ALL_", sessionParams);
			}
			
			logger.log(IAppLogger.INFO, "testAdministrationVal=" + testAdministrationVal);
			logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
			logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
			logger.log(IAppLogger.INFO, "school=" + school);
			logger.log(IAppLogger.INFO, "grade=" + grade);
			
			//TODO
			//service call to get student, report message etc
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("testAdministrationVal", testAdministrationVal);
			paramMap.put("testProgram", testProgram);
			paramMap.put("corpDiocese", corpDiocese);
			paramMap.put("school", school);
			paramMap.put("grade", grade);
			List<RescoreStudentTO> rescoreStudentList = rescoreRequestService.getRescoreStudentList(paramMap);
		
			//Add object to modelAndView
			modelAndView.addObject("reportUrl", reportUrl);
			modelAndView.addObject("testAdministrationVal", testAdministrationVal);
			modelAndView.addObject("testProgram", testProgram);
			modelAndView.addObject("corpDiocese", corpDiocese);
			modelAndView.addObject("school", school);
			modelAndView.addObject("grade", grade);
			modelAndView.addObject("rescoreStudentList", rescoreStudentList);
			
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - rescoreRequestForm() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return modelAndView;
	}

}
