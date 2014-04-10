package com.ctb.prism.core.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
	 * Usability log Before method for capturing the report URL execution
	 * 
	 * @param joinPoint
	 */
	@Before("execution(* com.ctb.prism.web.controller.ReportController.openReportHtml(..)) && args(req, res)")
	public void logReportURL(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 2;
		captureActivityLog(joinPoint, req, res, activityLogType);

	}
	
	/**
	 * Usability log Before method for capturing the report URL execution
	 * 
	 * @param joinPoint
	 */
	@Before("execution(* com.ctb.prism.web.controller.ReportController.openReportApiHtml(..)) && args(req, res)")
	public void logReportURLApi(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 2;
		captureActivityLog(joinPoint, req, res, activityLogType);

	}

	/**
	 * Usability log After Login method execution/ or after the Login as functionality.
	 * 
	 * @param joinPoint
	 * @After("execution(* com.ctb.prism.web.controller.LoginController.validateUser(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)) && args(req, res)")
	 */
	@After("execution(* com.ctb.prism.web.controller.LoginController.validateUser(..)) && args(req, res)")
	public void logLogin(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 1;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution
	 * 
	 * @param joinPoint
	 */
	/**
	 * Usability log After for capturing Admin functionality method execution Edit/Save user profile
	 * 
	 * @param joinPoint
	 */

	@After(" execution(* com.ctb.prism.web.controller.ParentController.updateProfile(..)) && args(req, res)")
	public void logUserProfile(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution Edit user/Change Password in the user management
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.AdminController.updateUser(..))  && args(req, res)")
	public void logupdateUser(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution Delete user in the user management
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.AdminController.deleteUser(..))  && args(req, res)")
	public void logdeleteUser(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution Add user in the user management
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.AdminController.addNewUser(..))  && args(req, res)")
	public void logaddNewUser(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution Update Report in the Manage Reports
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.ReportController.updateReportNew(..))  && args(req, res)")
	public void logupdateReportNew(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution delete Report in the manage reports
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.ReportController.deleteReport(..))  && args(req, res)")
	public void logdeleteReport(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution Update role in the Manage Roles
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.AdminController.updateRole(..))  && args(req, res)")
	public void logupdateRole(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	/**
	 * Usability log After for capturing Admin functionality method execution disassociate role in the manage roles
	 * 
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller.AdminController.dissociateUser(..))  && args(req, res)")
	public void logdissociateUser(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res) {
		long activityLogType = 3;
		captureActivityLog(joinPoint, req, res, activityLogType);
	}

	public void captureActivityLog(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse res, Long activityType) {
		UsabilityTO usability = new UsabilityTO();
		String userId = null;
		try {
			String reportUrl = null;
			logMessage(joinPoint, "Usability logger : ", IAppLogger.INFO);
			if (req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN) == null) {
				userId = (String) req.getSession().getAttribute(IApplicationConstants.CURRUSERID);
			} else {
				usability.setLoginAs(true);
				userId = (String) req.getSession().getAttribute(IApplicationConstants.PREV_ADMIN);
			}
			// String currentOrg = (String)
			// req.getSession().getAttribute(IApplicationConstants.CURRORG);
			String customerId = (String) req.getSession().getAttribute(IApplicationConstants.CUSTOMER);
			long activityTypeId = activityType;
			if (activityType == 2) {
				reportUrl = req.getParameter("reportUrl");
			} else {
				reportUrl = req.getRequestURI();
			}
			// to fetch client IP address
			String ipAddress = req.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = req.getRemoteAddr();
			}

			// String ipAddress = (String)
			// req.getSession().getAttribute(IApplicationConstants.ipAddress);
			// String ipAddress = "";
			usability.setUserId(userId);
			usability.setCustomerId(customerId);
			usability.setActivityTypeId(activityTypeId);
			usability.setReportUrl(reportUrl);
			usability.setIpAddress(ipAddress);
			logger.log(IAppLogger.INFO, usability.toString());
			// usabilityService.saveUsabilityData(usability);

			if (!usabilityService.saveUsabilityData(usability)) {
				logMessage(joinPoint, "ERROR while inserting - usability details : ", IAppLogger.ERROR);
			}

			// TODO call service to log report accessed by user with timestamp
			/* write code here to log usability */
		} catch (Throwable e) {
			logMessage(joinPoint, "ERROR while logging - usability details : " + e.getMessage(), IAppLogger.ERROR);
		}
	}

	/**
	 * Logs messages using Spring AOP
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	private void logMessage(JoinPoint joinPoint, String message, int logLevel) {
		logger.log(logLevel, CustomStringUtil.appendString(message, ". ", joinPoint.getTarget().getClass().getName(), " - ", joinPoint.getSignature().getName()));
	}

}
