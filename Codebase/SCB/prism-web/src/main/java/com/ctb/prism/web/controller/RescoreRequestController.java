package com.ctb.prism.web.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.exception.BusinessException;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.parent.transferobject.ManageContentTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.service.IRescoreRequestService;
import com.ctb.prism.report.transferobject.ReportMessageTO;
import com.ctb.prism.report.transferobject.RescoreRequestTO;
import com.ctb.prism.report.transferobject.RescoreSubtestTO;
import com.google.gson.Gson;

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
	
	// @Autowired
	// private Utils utils;
	
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
	@SuppressWarnings("unchecked")
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
			
			//service call to get student's re-score data
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("testAdministrationVal", testAdministrationVal);
			paramMap.put("testProgram", testProgram);
			paramMap.put("corpDiocese", corpDiocese);
			paramMap.put("school", school);
			paramMap.put("grade", grade);
			paramMap.put("loggedinUserTO", loggedinUserTO);
			
			Map<String, Object> rescoreRequestMap = rescoreRequestService.getRescoreRequestForm(paramMap);
			List<RescoreRequestTO> dnpStudentList = (List<RescoreRequestTO>) rescoreRequestMap.get("dnpStudentList");
			List<RescoreRequestTO> notDnpStudents = (List<RescoreRequestTO>) rescoreRequestMap.get("notDnpStudents");
			List<RescoreRequestTO> notDnpStudentList = (List<RescoreRequestTO>) rescoreRequestMap.get("notDnpStudentList");
		
			//Add object to modelAndView
			modelAndView.addObject("reportUrl", reportUrl);
			modelAndView.addObject("testAdministrationVal", testAdministrationVal);
			modelAndView.addObject("testProgram", testProgram);
			modelAndView.addObject("corpDiocese", corpDiocese);
			modelAndView.addObject("school", school);
			modelAndView.addObject("grade", grade);
			modelAndView.addObject("dnpStudentList", dnpStudentList);
			modelAndView.addObject("notDnpStudents", notDnpStudents);
			modelAndView.addObject("notDnpStudentList", notDnpStudentList);
			
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - rescoreRequestForm() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		
		List<ReportMessageTO> reportMessages = getAllReportMessages(request, testAdministrationVal);
		modelAndView.addObject("reportMessages", reportMessages);
		String dataloadMessage = getReportMessage(reportMessages, IApplicationConstants.DASH_MESSAGE_TYPE.DM.toString(), IApplicationConstants.DATALOAD_MESSAGE);
		logger.log(IAppLogger.INFO, "dataloadMessage=" + dataloadMessage);
		modelAndView.addObject("dataloadMessage", dataloadMessage);
		return modelAndView;
	}
	
	/**
	 * Rescore request for parent users
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/rescoreRequestFormParent", method = RequestMethod.GET)
	public ModelAndView rescoreRequestFormParent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		return rescoreRequestForm2015(request, response);
	}
	
	/**
	 * Opens the Rescore Request Form and populate required data for ISTEP+ Spring 2015.
	 * 
	 * @return
	 * @throws ServletException, IOException,BusinessException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rescoreRequestForm2015", method = RequestMethod.GET)
	public ModelAndView rescoreRequestForm2015(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestController - rescoreRequestForm2015()");
		long t1 = System.currentTimeMillis();
		
		ModelAndView modelAndView = new ModelAndView("report/rescoreRequestForm2015");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		
		String reportUrl = (String) request.getParameter("reportUrl");
		String reportId = (String) request.getParameter("reportId");
		String testAdministrationVal = (String) request.getParameter("p_test_administration");
		String testProgram = (String) request.getParameter("p_test_program");
		String corpDiocese = (String) request.getParameter("p_corpdiocese");
		String school = (String) request.getParameter("p_school");
		String grade = (String) request.getParameter("p_grade");
		String studentBioId = (String) request.getParameter("p_student");
		
		logger.log(IAppLogger.INFO, "reportUrl=" + reportUrl);
		logger.log(IAppLogger.INFO, "testAdministration=" + testAdministrationVal);
		logger.log(IAppLogger.INFO, "testProgram=" + testProgram);
		logger.log(IAppLogger.INFO, "corpDiocese=" + corpDiocese);
		logger.log(IAppLogger.INFO, "school=" + school);
		logger.log(IAppLogger.INFO, "grade=" + grade);
		logger.log(IAppLogger.INFO, "studentBioId=" + studentBioId);
		
		
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
					studentBioId = CustomStringUtil.getNotNullString(parameters.get("p_student"));
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
			logger.log(IAppLogger.INFO, "studentBioId=" + studentBioId);
			
			//service call to get student's re-score data
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("testAdministrationVal", testAdministrationVal);
			paramMap.put("testProgram", testProgram);
			paramMap.put("corpDiocese", corpDiocese);
			paramMap.put("school", school);
			paramMap.put("grade", grade);
			paramMap.put("studentBioId", studentBioId);
			paramMap.put("loggedinUserTO", loggedinUserTO);
			request.getSession().setAttribute("GDF_testadmin", testAdministrationVal);
			
			Map<String, Object> rescoreRequestMap = rescoreRequestService.getRescoreRequestForm2015(paramMap);
			String studentFullName = (String)rescoreRequestMap.get("studentFullName");
			String requestedDate = (String)rescoreRequestMap.get("requestedDate");
			String ipFileName = (String)rescoreRequestMap.get("ipFileName");
			RescoreSubtestTO rescoreElaTO = (RescoreSubtestTO)rescoreRequestMap.get("rescoreElaTO");
			RescoreSubtestTO rescoreMathTO = (RescoreSubtestTO)rescoreRequestMap.get("rescoreMathTO");
			RescoreSubtestTO rescoreScienceTO = (RescoreSubtestTO)rescoreRequestMap.get("rescoreScienceTO");
			
			
			//Add object to modelAndView
			modelAndView.addObject("reportUrl", reportUrl);
			modelAndView.addObject("testAdministrationVal", testAdministrationVal);
			modelAndView.addObject("testProgram", testProgram);
			modelAndView.addObject("corpDiocese", corpDiocese);
			modelAndView.addObject("school", school);
			modelAndView.addObject("grade", grade);
			modelAndView.addObject("studentBioId", studentBioId);
			modelAndView.addObject("studentFullName", studentFullName);
			modelAndView.addObject("requestedDate", requestedDate);
			modelAndView.addObject("ipFileName", ipFileName);
			modelAndView.addObject("rescoreElaTO", rescoreElaTO);
			modelAndView.addObject("rescoreMathTO", rescoreMathTO);
			modelAndView.addObject("rescoreScienceTO", rescoreScienceTO);
			
			List<ReportMessageTO> reportMessages = getAllReportMessages(request, testAdministrationVal);
			modelAndView.addObject("reportMessages", reportMessages);
			String dataloadMessage = getReportMessage(reportMessages, IApplicationConstants.DASH_MESSAGE_TYPE.DM.toString(), IApplicationConstants.DATALOAD_MESSAGE);
			logger.log(IAppLogger.INFO, "dataloadMessage=" + dataloadMessage);
			modelAndView.addObject("dataloadMessage", dataloadMessage);
			
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - rescoreRequestForm2015() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return modelAndView;
	}
	
	private List<ReportMessageTO> getAllReportMessages(HttpServletRequest request, String testAdministrationVal) {
		String reportId = (String) request.getParameter("reportId");
		String productId = (testAdministrationVal == null) ? IApplicationConstants.DEFAULT_PRODUCT_ID : testAdministrationVal;
		String customerId = (String) request.getSession().getAttribute(IApplicationConstants.CUSTOMER);
		String orgNodeLevel = ((Long) request.getSession().getAttribute(IApplicationConstants.CURRORGLVL)).toString();
		String currentUserId = (String) request.getSession().getAttribute(IApplicationConstants.CURRUSERID);
		logger.log(IAppLogger.INFO, "reportId=" + reportId);
		logger.log(IAppLogger.INFO, "productId=" + productId);
		logger.log(IAppLogger.INFO, "customerId=" + customerId);
		logger.log(IAppLogger.INFO, "orgNodeLevel=" + orgNodeLevel);
		Map<String, Object> messageParamMap = new HashMap<String, Object>();
		messageParamMap.put("REPORT_ID", reportId);
		messageParamMap.put("REPORT_URL", (String) request.getParameter("reportUrl"));
		messageParamMap.put("PRODUCT_ID", productId);
		messageParamMap.put("CUSTOMER_ID", customerId);
		messageParamMap.put("ORG_NODE_LEVEL", orgNodeLevel);
		messageParamMap.put("USER_ID", currentUserId);
		return reportService.getAllReportMessages(messageParamMap);
	}
	
	private String getReportMessage(List<ReportMessageTO> reportMessages, String messageType, String messageName) {
		String message = null;
		if ((reportMessages != null) && (!reportMessages.isEmpty())) {
			for (ReportMessageTO reportMessage : reportMessages) {
				if ((messageType.equals(reportMessage.getMessageType())) && (messageName.equals(reportMessage.getMessageName()))) {
					message = reportMessage.getMessage();
					break;
				}
			}
		}
		return message;
	}
	
	
	/**
	 * Re-score request submission
	 */
	@RequestMapping(value = "/submitRescoreRequest", method = RequestMethod.GET)
	public @ResponseBody 
	String submitRescoreRequest(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestController - submitRescoreRequest()");
		long t1 = System.currentTimeMillis();
		
		long itemNumber = Long.parseLong(request.getParameter("itemNumber"));
		long subtestId = Long.parseLong(request.getParameter("subtestId"));
		long sessionId = Long.parseLong(request.getParameter("sessionId"));
		String moduleId = request.getParameter("moduleId");
		long studentBioId = Long.parseLong(request.getParameter("studentBioId"));
		String requestedStatus = request.getParameter("requestedStatus");
		String requestedDate = request.getParameter("requestedDate"); 
		long rrfId = Long.parseLong(request.getParameter("rrfId"));
	
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		long userId = Long.parseLong(loggedinUserTO.getUserId());
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("itemNumber", itemNumber);
		paramMap.put("subtestId", subtestId);
		paramMap.put("sessionId", sessionId);
		paramMap.put("moduleId", moduleId);
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("requestedStatus", requestedStatus);
		paramMap.put("requestedDate", requestedDate);
		paramMap.put("userId", userId);
		paramMap.put("rrfId", rrfId);
		
		com.ctb.prism.core.transferobject.ObjectValueTO statusTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			statusTO = rescoreRequestService.submitRescoreRequest(paramMap); 
			jsonString = gson.toJson(statusTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - submitRescoreRequest() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return jsonString;
    }
	
	
	@RequestMapping(value = "/resetItemState", method = RequestMethod.GET)
	public @ResponseBody 
	String resetItemState(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestController - resetItemState()");
		long t1 = System.currentTimeMillis();
		
		long subtestId = Long.parseLong(request.getParameter("subtestId"));
		long studentBioId = Long.parseLong(request.getParameter("studentBioId"));
		String requestedStatus = "N";
		
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		long userId = Long.parseLong(loggedinUserTO.getUserId());
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("subtestId", subtestId);
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("userId", userId);
		paramMap.put("requestedStatus", requestedStatus);
		
		com.ctb.prism.core.transferobject.ObjectValueTO statusTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			statusTO = rescoreRequestService.resetItemState(paramMap); 
			jsonString = gson.toJson(statusTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - resetItemState() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return jsonString;
    }
	
	
	@RequestMapping(value = "/resetItemDate", method = RequestMethod.GET)
	public @ResponseBody 
	String resetItemDate(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestController - resetItemDate()");
		long t1 = System.currentTimeMillis();
		
		long studentBioId = Long.parseLong(request.getParameter("studentBioId"));
		
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		long userId = Long.parseLong(loggedinUserTO.getUserId());
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("requestedDate", request.getParameter("requestedDate"));
		paramMap.put("userId", userId);
		
		com.ctb.prism.core.transferobject.ObjectValueTO statusTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			statusTO = rescoreRequestService.resetItemDate(paramMap); 
			jsonString = gson.toJson(statusTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - resetItemDate() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return jsonString;
    }
	
	
	@RequestMapping(value = "/addStudent", method = RequestMethod.GET)
	public @ResponseBody 
	String addStudent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException,BusinessException {
		logger.log(IAppLogger.INFO, "Enter: RescoreRequestController - addStudent()");
		long t1 = System.currentTimeMillis();
		
		long studentBioId = Long.parseLong(request.getParameter("studentBioId"));
		String grade = request.getParameter("grade");
		
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("studentBioId", studentBioId);
		paramMap.put("grade", grade);
		
		RescoreRequestTO rescoreStudentTO = null;
		Gson gson = new Gson();
		String jsonString = "";
		try{
			rescoreStudentTO = rescoreRequestService.getNotDnpStudentDetails(paramMap); 
			jsonString = gson.toJson(rescoreStudentTO);
			logger.log(IAppLogger.INFO, "jsonString of status:");
			logger.log(IAppLogger.INFO, jsonString);
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			long t2 = System.currentTimeMillis();
			logger.log(IAppLogger.INFO, "Exit: RescoreRequestController - addStudent() took time: "+String.valueOf(t2 - t1)+"ms");
		}
		return jsonString;
    }

}
