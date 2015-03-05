package com.ctb.prism.core.logger;
 
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;

import com.ctb.prism.core.util.CustomStringUtil;
 
@Aspect
public class LoggingAspect {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(LoggingAspect.class.getName());
 
	/**
	 * Log error after throwing
	 * @param joinPoint
	 * @param error
	 */
	@AfterThrowing(pointcut = "execution(* com.ctb.prism.web.controller..*(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

		logger.log(IAppLogger.ERROR, CustomStringUtil.appendString("ERROR : ", joinPoint.getTarget().toString(), " message: ", error.getMessage()));
		if(error instanceof AccessDeniedException) {
			logger.log(IAppLogger.ERROR, "!!!!!!!! ACCESS DENIED !!!!!!!");
		}

	}
	
	/**
	 * Log LDAP related error
	 * @param joinPoint
	 * @param error
	 */
	public void logLDAPError(JoinPoint joinPoint, Throwable error) {

		logAfterThrowing(joinPoint, error);

	}
		      
	/**
	 * AOP log for all controllers
	 * @param joinPoint
	 * @throws Throwable
	 */
	//@Around("execution(* com.ctb.prism.web.controller..*(..))")
	public void logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Invoking DAO : ", 
				joinPoint.getTarget().getClass().getName(), " - ", joinPoint.getSignature().getName()));
		
		joinPoint.proceed(); //continue on the intercepted method
		
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString("Exiting DAO : ", 
				joinPoint.getTarget().getClass().getName(), " - ", joinPoint.getSignature().getName()));
	}
	
	/**
	 * Print log Before method execution
	 * @param joinPoint
	 */
	@Before("execution(* com.ctb.prism.web.controller..*(..))")
	public void logBefore(JoinPoint joinPoint) {
		try {
			logMessage(joinPoint, "Invoking Controller : ");
		} catch (Throwable e) {
			// ignore
		}
	}
	/**
	 * Print log After method execution
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism.web.controller..*(..))")
	public void logAfter(JoinPoint joinPoint) {
		try {
			logMessage(joinPoint, "Exiting Controller : ");
		} catch (Throwable e) {
			// ignore
		}
	}
	
	/**
	 * Print log Before method execution
	 * @param joinPoint
	 */
	@Before("execution(* com.ctb.prism..*.dao..*(..))")
	public void logBeforeDAO(JoinPoint joinPoint) {
		try {
			logMessage(joinPoint, "Invoking DAO : ");
		} catch (Throwable e) {
			// ignore
		}
	}
	/**
	 * Print log After method execution
	 * @param joinPoint
	 */
	@After("execution(* com.ctb.prism..*.dao..*(..))")
	public void logAfterDAO(JoinPoint joinPoint) {
		try {
			logMessage(joinPoint, "Exiting DAO : ");
		} catch (Throwable e) {
			// ignore
		}
	}
	
	
	/**
	 * Logs messages using Spring AOP
	 * @param joinPoint
	 * @throws Throwable
	 */
	private void logMessage(JoinPoint joinPoint, String message) throws Throwable {
		logger.log(IAppLogger.INFO, CustomStringUtil.appendString(message, 
				joinPoint.getTarget().getClass().getName(), " - ", joinPoint.getSignature().getName()));
	}
	
	/**
	 * Save defined messages into table
	 */
	private void logActivity() {
		// TODO call DB to save activity
	}
 
}