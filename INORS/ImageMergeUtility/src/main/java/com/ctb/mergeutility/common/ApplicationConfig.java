package com.ctb.mergeutility.common;


import java.io.IOException;
/*AppIlcation config responsible for loading the configuration properties */
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationConfig {
	private Properties configProp = new Properties(); 
	private static ApplicationConfig applicationConfig = null;
	private static final Logger logger = Logger
			.getLogger(ApplicationConfig.class);
	private ApplicationConfig() {
		logger.info("Loading properties....");
		populateProperties();
		logger.info("Loading properties Done....");
	}

	public static ApplicationConfig loadApplicationConfig() {
		if (applicationConfig == null) {
			applicationConfig = new ApplicationConfig();
		}
		return applicationConfig;
	}
	private void populateProperties() {		
		//InputStream in = this.getClass().getResourceAsStream("/com/ctb/mergeutility/common/application.properties");						
		//configProp.load(in);			
		configProp = PropertyFile.loadProperties("application.properties");
	}	
	public Properties getConfigProp() {
		return configProp;
	}	
}// end of class
