package com.ctb.prism.core.exception;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;

/**
 * This is the class for handling any business exceptions which could occur when a business logic will not be satisfied throughout the application. This class inherits <b>Exception</b>
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class BusinessException extends SystemException {

	private static final long serialVersionUID = 1L;

	private static final IAppLogger logger = LogFactory.getLoggerInstance(SystemException.class.getName());

	private String code;

	private String[] params;

	private String customExceptionMessage;

	/**
	 * @param code
	 * @param params
	 */
	public BusinessException(String code, String[] params) {
		super();
		this.code = code;
		this.params = params;
		logger.log(IAppLogger.ERROR, "Workflow Business Exception - CODE:" + code);
	}

	/**
	 * @param message
	 */
	public BusinessException(String message) {
		super(message);
		this.customExceptionMessage = message;
		logger.log(IAppLogger.ERROR, "Workflow Business Exception - CUSTOM MESSAGE: " + message);
	}

	/**
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * @return
	 */
	public String getCustomExceptionMessage() {
		return this.customExceptionMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return this.code;
	}
}
