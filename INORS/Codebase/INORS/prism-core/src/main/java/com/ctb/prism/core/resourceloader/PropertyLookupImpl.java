package com.ctb.prism.core.resourceloader;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.stereotype.Component;

import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;



/**
 * This class is responsible for looking up the property values.
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
@Component("propertyLookup")
public class PropertyLookupImpl implements IPropertyLookup {

	/**
	 * A Logger object is used to log messages for a specific system or
	 * application component.
	 */
	private static IAppLogger logger = LogFactory
			.getLoggerInstance(PropertyLookupImpl.class.getName());
	
	private CompositeConfiguration compositeConfiguration;
	
	/**
	 * Base constructor
	 */
	public PropertyLookupImpl() {
		try {
			compositeConfiguration = new CompositeConfiguration();
			PropertiesConfiguration propConfig = new PropertiesConfiguration("messages.properties");
			propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());			
			compositeConfiguration.addConfiguration(propConfig);

			propConfig = new PropertiesConfiguration("Bundle.properties");
			propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());			
			compositeConfiguration.addConfiguration(propConfig);

			PropertiesConfiguration constConfig = new PropertiesConfiguration("jasperreports.properties");
			constConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
			compositeConfiguration.addConfiguration(constConfig);
			
			constConfig = new PropertiesConfiguration("ldap.properties");
			constConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
			compositeConfiguration.addConfiguration(constConfig);
		} catch (Exception e) {
			logger.log(IAppLogger.ERROR, "Unable to load properties", e);
		}
	}
	
	/**
	 * This method returns the value of a specific property
	 * 
	 * @param key - The key against which the property will be retrieved
	 * @return The Property value
	 */
	public String get(String key) {
		return this.compositeConfiguration.getString(key);
	}

	public String get(String key, String[] args) {
		String msg = this.compositeConfiguration.getString(key);
		if (args != null && args.length>0) {
			java.text.MessageFormat formatter = new java.text.MessageFormat("");   
			formatter.applyPattern(msg);
			msg = formatter.format(args);
		}
		return msg;
	}	
	
	public List<String> getList(String key) {
		return this.compositeConfiguration.getList(key);
	}
	
	/**
	 * @return the compositeConfiguration
	 */
	public CompositeConfiguration getCompositeConfiguration() {
		return compositeConfiguration;
	}

	/**
	 * @param compositeConfiguration the compositeConfiguration to set
	 */
	public void setCompositeConfiguration(
			CompositeConfiguration compositeConfiguration) {
		this.compositeConfiguration = compositeConfiguration;
	}

}
