package com.ctb.prism.core.logger;

/**
 * This is the factory class responsible for instantiating the appropriate
 * implementation of the IAppLogger interface which is used for performing
 * logging activity.
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 **/
public class LogFactory {
	/**
	 * This method is responsible for returning the required implementation of
	 * <b>IAppLogger</b> interface for the given class name. This logger object
	 * will be used for performing all the logging activities for the given
	 * class.
	 * 
	 * @param className
	 *            - This is the name of the class for which logger need to be
	 *            initialized
	 * @return logger - IAppLogger to be used for performing logging activity
	 **/
	public static IAppLogger getLoggerInstance(final String className) {
		IAppLogger logger = new AppLoggerImpl(className);
		return logger;
	}
	
	public static IAppLogger getLoggerInstance(final String className, final String contractName) {
		IAppLogger logger = new AppLoggerImpl(className);
		return logger;
	}
}
