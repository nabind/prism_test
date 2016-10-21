package com.drc.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyLoader {

	private static final Logger logger = Logger.getLogger(PropertyLoader.class);

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)}
	 * that uses the current thread's context class loader.
	 * 
	 * @param name
	 * @return
	 */
	/*public static Properties loadProperties(final String name) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loadProperties(name, loader);
	}
	

	
	
	private static Properties loadProperties(String name, ClassLoader loader) {
		if (name == null) {
			throw new IllegalArgumentException("null input: name");
		}
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		if (name.endsWith(ApplicationConstants.SUFFIX)) {
			int to = name.length() - ApplicationConstants.SUFFIX.length();
			name = name.substring(0, to);
		}
		Properties result = null;
		InputStream in = null;
		try {
			if (loader == null) {
				loader = ClassLoader.getSystemClassLoader();
			}
			if (ApplicationConstants.LOAD_AS_RESOURCE_BUNDLE) {
				name = name.replace('/', '.');
				// Throws MissingResourceException on lookup failures:
				final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(), loader);
				result = new Properties();
				for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
					final String key = (String) keys.nextElement();
					final String value = rb.getString(key);
					result.put(key, value);
				}
			} else {
				name = name.replace('.', '/');
				if (!name.endsWith(ApplicationConstants.SUFFIX)) {
					name = name.concat(ApplicationConstants.SUFFIX);
				}
				// Returns null on lookup failures:
				in = loader.getResourceAsStream(name);
				if (in != null) {
					result = new Properties();
					result.load(in); // Can throw IOException
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Throwable ignore) {
					logger.warn(ignore.getMessage());
				}
		}
		if (ApplicationConstants.THROW_EXCEPTION_ON_LOAD_FAILURE && (result == null)) {
			throw new IllegalArgumentException("could not load [" + name + "]" + " as "
					+ (ApplicationConstants.LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
		}
		return result;
	}*/
	
	
	public static Properties loadProperties(String name) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream in = loader.getResourceAsStream(name);
		Properties prop = null;
		
		//getting the properties file for local environment
		if (in == null) {
			try {
				in = new FileInputStream(ApplicationConstants.LOCAL_AES_DECRYPTION_PROPERTIES_FILE);
			} catch (FileNotFoundException e) {
				prop = null;
			}
		}
		if (in != null) {
			prop = new Properties();
			try {
				prop.load(in);
			} catch (IOException e) {
				prop = null;
				
			}finally {
				if (in != null)
					try {
						in.close();
					} catch (Throwable ignore) {
						logger.warn(ignore.getMessage());
					}
			}
		}
		if (prop == null) {
			throw new IllegalArgumentException("Could not load properties file");
		}
		return prop;
	}
	
}
