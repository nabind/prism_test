package com.ctb.prism.core.logger;

/**
 * This is the interface which is used for performing all
 * the logging activity of the application.
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
**/
public interface IAppLogger {
	/**
	 * The TRACE Level designates finer-grained 
	 * informational events than the DEBUG
	**/
	public static final int TRACE = 1;
	
	/**
	 * The DEBUG Level designates fine-grained 
	 * informational events that are 
	 * most useful to debug an application. 
	**/
	public static final int INFO = 2;
	
	/**
	 * The INFO level designates informational 
	 * messages that highlight the progress 
	 * of the application at coarse-grained level. 
	**/
	public static final int DEBUG = 3;
	
	/**
	 * The WARN level designates 
	 * potentially harmful situations. 
	**/
	public static final int WARN = 4;
	
	/**
	 * The ERROR level designates error
	 * events that might still allow the 
	 * application to continue running. 					
	**/
	public static final int ERROR = 5;
	
	
	/**
	 * The FATAL level designates very
	 *  severe error events that will 
	 *  presumably lead the application to abort. 
	**/
	public static final int OFF = 6;	
	
	/**
	 * This generic form is intended 
	 * to be used by wrappers. 
	 *
	 * @param level The log level
	 * @param message The exception message
	**/
	public void log(int level, String message);
	
	/**
	 * This generic form is intended 
	 * to be used by wrappers. 
	 *
	 * @param level The log level
	 * @param message The exception message
	 * @param thrower The exception throwable 
	**/
	public void log(int level, String message, Throwable thrower);
}
