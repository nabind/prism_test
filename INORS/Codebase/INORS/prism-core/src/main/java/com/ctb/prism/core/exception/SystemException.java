package com.ctb.prism.core.exception;

import org.apache.log4j.Logger;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

/**
 * This is the class for handling any exceptions except <i>BusinessExceptions</i> which could occur throughout the application. Throwing this exception will cause system to stop there and ask user to
 * contact system administrator. It inherits <b>Exception</b>
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class SystemException extends Exception {

	/**
	 * Auto generated serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A Logger object is used to log messages for a specific system or application component.
	 */
	private static final IAppLogger logger = LogFactory.getLoggerInstance(SystemException.class.getName());

	/**
	 * @param message
	 */
	public SystemException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	public SystemException() {
		super();
	}

	/**
	 * This is the constructor of the class which calls the super constructor of the Exception class and executes the expected behavior of the exception based on the throwable type.
	 * 
	 * @param throwable
	 *            - This can accept any Exception
	 */
	public SystemException(Throwable throwable) {
		super(throwable);
		logger.log(IAppLogger.ERROR, throwable.getMessage(), throwable);
	}

	/**
	 * @param string
	 * @param log
	 * @param e
	 */
	public SystemException(String string, Logger log, Exception e) {
		super(e);
		logger.log(IAppLogger.ERROR, e.getMessage(), e);
	}
}
