package com.ctb.prism.core.exception;

import org.apache.log4j.Logger;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

/**
 * This is the class for handling any exceptions except <i>BusinessExceptions</i>
 * which could occur throughout the application. Throwing this exception will
 * cause system to stop there and ask user to contact system administrator. It
 * inherits <b>Exception</b>
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class CPSecurityException extends SecurityException {

	/**
	 * Auto generated serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A Logger object is used to log messages for a specific system or
	 * application component.
	 */
	private static final IAppLogger logger = LogFactory
			.getLoggerInstance(SecurityException.class.getName());

	protected CPSecurityException(String message) {
		super(message);
	}
	
	public CPSecurityException() {
		super();
	}
	
	/**
	 * This is the constructor of the class which calls the super constructor of
	 * the Exception class and executes the expected behavior of the exception
	 * based on the throwable type.
	 * 
	 * @param throwable -
	 *            This can accept any Exception
	 */
	public CPSecurityException(Throwable throwable) {
		super(throwable);
		logger.log(IAppLogger.ERROR, throwable.getMessage(), throwable);
	}

	public CPSecurityException(String string, Logger log, Exception e) {
		super(e);
		logger.log(IAppLogger.ERROR, e.getMessage(), e);
	}
}
