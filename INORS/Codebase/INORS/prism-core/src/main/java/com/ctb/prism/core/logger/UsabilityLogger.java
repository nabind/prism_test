package com.ctb.prism.core.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.Service.IUsabilityService;
import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.transferobject.UsabilityTO;
import com.ctb.prism.core.util.CustomStringUtil;

@Aspect
@Component
public class UsabilityLogger {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(UsabilityLogger.class.getName());
	
	@Autowired
	private IUsabilityService usabilityService;
	
	/**
	 * Usability log Before method execution
	 * @param joinPoint
	 */
	@Before("execution(* com.ctb.prism.web.controller.ReportController.openReportHtml(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)) && args(req, res)")
	public void logAfter(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		UsabilityTO usability = new UsabilityTO();
		try {
			logMessage(joinPoint, "Usability logger : ", IAppLogger.INFO);
			String reportUrl = req.getParameter("reportUrl");
			String reportId = req.getParameter("reportId");
			String reportName = req.getParameter("reportName");
			String username = (String) req.getSession().getAttribute(IApplicationConstants.CURRUSER);
			String currentOrg = (String) req.getSession().getAttribute(IApplicationConstants.CURRORG);
			logger.log(IAppLogger.INFO, "-------- REPORT URL : "+reportUrl);
			logger.log(IAppLogger.INFO, "-------- REPORT ID : "+reportId);
			logger.log(IAppLogger.INFO, "-------- USERNAME : "+username);
			logger.log(IAppLogger.INFO, "-------- ORGANIZATION : "+currentOrg);
			
			usability.setReportUrl(reportUrl);
			usability.setReportId(reportId);
			usability.setReportName(reportName);
			usability.setUsername(username);
			usability.setCurrentOrg(currentOrg);
			
			if(!usabilityService.saveUsabilityData(usability)){
				logMessage(joinPoint, "ERROR while logging - usability details : ", IAppLogger.ERROR);
			}
			// TODO call service to log report accessed by user with timestamp
			/* write code here to log usability */
		} catch (Throwable e) {
			logMessage(joinPoint, "ERROR while logging - usability details : ", IAppLogger.ERROR);
		}
	}
		
	
	
	
	/**
	 * Logs messages using Spring AOP
	 * @param joinPoint
	 * @throws Throwable
	 */
	private void logMessage(JoinPoint joinPoint, String message, int logLevel) {
		logger.log(logLevel, CustomStringUtil.appendString(message, 
				joinPoint.getTarget().getClass().getName(), " - ", joinPoint.getSignature().getName()));
	}

}
