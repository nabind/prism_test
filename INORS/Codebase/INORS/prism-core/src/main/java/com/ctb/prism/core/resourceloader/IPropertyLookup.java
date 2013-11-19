package com.ctb.prism.core.resourceloader;

import java.util.List;

/**
 * This interface is responsible for looking up the property files.
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IPropertyLookup {

	/**
	 * This method retrieves the value of a specific property from the
	 * properties file
	 * 
	 * @param key - The key against which the property will be retrieved
	 * @return The Property value
	 */
	public String get(String key);
	
	public List<String> getList(String key);
	
	public String get(String key, String[] args);
}
