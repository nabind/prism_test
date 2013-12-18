package com.ctb.prism.web.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.ctb.prism.core.util.Utils;
import com.ctb.prism.login.transferobject.UserTO;
import com.ctb.prism.report.service.IReportService;
import com.ctb.prism.report.transferobject.ManageMessageTO;
import com.ctb.prism.web.form.ManageMessageForm;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ManageMessageController {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(ManageMessageController.class.getName());
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	@RequestMapping(value = "/getReportMessageFilter", method = RequestMethod.GET)
	public ModelAndView getReportMessageFilter(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException,BusinessException{
		
		logger.log(IAppLogger.INFO, "Enter: ManageMessageController - getReportMessageFilter()");
		Map<String,Object> serviceMapReportMessageFilter = null;
		ModelAndView modelAndView = new ModelAndView("report/reportMessageSearch");
		UserTO loggedinUserTO = (UserTO) request.getSession().getAttribute(IApplicationConstants.LOGGEDIN_USER_DETAILS);
		long reportId = Long.parseLong(request.getParameter("reportId"));
		String reportName = request.getParameter("reportName");
		String reportUrl = request.getParameter("reportUrl");
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("reportId", reportId);
		paramMap.put("loggedinUserTO", loggedinUserTO);
		try{
			if(reportId != 0){
				serviceMapReportMessageFilter = reportService.getReportMessageFilter(paramMap);
				serviceMapReportMessageFilter.put("reportId", reportId);
				serviceMapReportMessageFilter.put("reportName", reportName);
				serviceMapReportMessageFilter.put("reportUrl", reportUrl);
				modelAndView.addObject("serviceMapReportMessageFilter",serviceMapReportMessageFilter);
			}
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}
		logger.log(IAppLogger.INFO, "Exit: ManageMessageController - getReportMessageFilter()");
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;
	}
	
	@RequestMapping(value = "/ajaxJSP/loadManageMessage", method = RequestMethod.POST)
	public ModelAndView loadManageMessage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException,BusinessException{
		
		logger.log(IAppLogger.INFO, "Enter: ManageMessageController - loadManageMessage");
		Map<String,Object> serviceMapManageMessage = null;
		ModelAndView modelAndView = new ModelAndView("report/reportMessage");
		long reportId = Long.parseLong(request.getParameter("reportId"));
		String reportName = request.getParameter("reportName");
		String reportUrl = request.getParameter("reportUrl");
		long custProdId=0L;
		if(null==request.getParameter("custProdId")){
		custProdId = 5001L;}
		else{
		custProdId = Long.parseLong(request.getParameter("custProdId"));
		}
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("reportId", reportId);
		paramMap.put("reportName", reportName);
		paramMap.put("reportUrl", reportUrl);
		paramMap.put("custProdId", custProdId);
		
		try{
			if(reportId != 0){
				serviceMapManageMessage = reportService.loadManageMessage(paramMap);
				serviceMapManageMessage.put("reportId", reportId);
				serviceMapManageMessage.put("reportName", reportName);
				serviceMapManageMessage.put("reportUrl", reportUrl);
				modelAndView.addObject("serviceMapManageMessage",serviceMapManageMessage);
			}
		}catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}
		logger.log(IAppLogger.INFO, "Exit: ManageMessageController - loadManageMessage");
		modelAndView.addObject("PDCT_NAME",propertyLookup.get("PDCT_NAME"));
		return modelAndView;
	}
	
	@RequestMapping(value = "/ajaxJSP/saveManageMessage", method = RequestMethod.POST)
    public ModelAndView saveManageMessage(@ModelAttribute("manageMessageForm") ManageMessageForm manageMessageForm,
    		HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
		logger.log(IAppLogger.INFO, "Enter: ManageMessageController - saveManageMessage");
		int saveStatus = 0;
		try{
		    List<ManageMessageTO> manageMessageTOList = manageMessageForm.getManageMessageTOList(); 
		    if(null != manageMessageTOList && manageMessageTOList.size() > 0) {
	            saveStatus = reportService.saveManageMessage(manageMessageTOList);
	        }
		    
	    }catch(Exception e){
			logger.log(IAppLogger.ERROR, "", e);
			throw new BusinessException("Problem Occured");
		}finally{
			response.setContentType("text/plain");
			response.getWriter().write( "{\"status\":\""+saveStatus+"\"}" );
		}
        logger.log(IAppLogger.INFO, "Exit: ManageMessageController - saveManageMessage");
        return null;
    }

}
