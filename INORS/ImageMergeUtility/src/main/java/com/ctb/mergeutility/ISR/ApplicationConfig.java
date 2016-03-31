package com.ctb.mergeutility.ISR;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*AppIlcation config responsible for loading the configuration properties */

import com.ctb.mergeutility.common.PropertyFile;

public class ApplicationConfig {
	private Properties configProp = new Properties(); 
	private static ApplicationConfig applicationConfig = null;
	private ApplicationConfig() throws IOException {
		populateProperties();
	}

	public static ApplicationConfig loadApplicationConfig() {
		if (applicationConfig == null) {
				try {
					applicationConfig = new ApplicationConfig();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return applicationConfig;
	}
	private void populateProperties() throws IOException {		
		//InputStream in = this.getClass().getResourceAsStream("application.properties");						
		//configProp.load(in);	
		configProp = PropertyFile.loadProperties("isrupload.properties");
	}	
	public Properties getConfigProp() {
		return configProp;
	}	
}// end of class
