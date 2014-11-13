package com.prism.util;

import static com.prism.util.Constants.LOAD_AS_RESOURCE_BUNDLE;
import static com.prism.util.Constants.SUFFIX;
import static com.prism.util.Constants.THROW_EXCEPTION_ON_LOAD_FAILURE;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class PropertyFile {

	private static final Logger logger = Logger.getLogger(PropertyFile.class);

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)}
	 * that uses the current thread's context class loader.
	 * 
	 * @param name
	 * @return
	 */
	public static Properties loadProperties(final String name) {
		return loadProperties(name, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Looks up a resource named 'name' in the classpath. The resource must map
	 * to a file with .properties extention. The name is assumed to be absolute
	 * and can use either "/" or "." for package segment separation with an
	 * optional leading "/" and optional ".properties" suffix. Thus, the
	 * following names refer to the same resource: some.pkg.PropertyFileResource
	 * some.pkg.PropertyFileResource.properties some/pkg/PropertyFileResource
	 * some/pkg/PropertyFileResource.properties /some/pkg/PropertyFileResource
	 * /some/pkg/PropertyFileResource.properties
	 * 
	 * @param name
	 *            classpath resource name [may not be null]
	 * @param loader
	 *            classloader through which to load the resource [null is
	 *            equivalent to the application loader]
	 * 
	 * @return resource converted to java.util.Properties [may be null if the
	 *         resource was not found and THROW_EXCEPTION_ON_LOAD_FAILURE is
	 *         false]
	 * 
	 * @throws IllegalArgumentException
	 *             if the resource was not found and
	 *             THROW_EXCEPTION_ON_LOAD_FAILURE is true
	 */
	private static Properties loadProperties(String name, ClassLoader loader) {
		if (name == null) {
			throw new IllegalArgumentException("null input: name");
		}
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		if (name.endsWith(Constants.SUFFIX)) {
			int to = name.length() - SUFFIX.length();
			name = name.substring(0, to);
		}
		Properties result = null;
		InputStream in = null;
		try {
			if (loader == null) {
				loader = ClassLoader.getSystemClassLoader();
			}
			if (LOAD_AS_RESOURCE_BUNDLE) {
				name = name.replace('/', '.');
				// Throws MissingResourceException on lookup failures:
				final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(), loader);
				result = new Properties();
				for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
					final String key = (String) keys.nextElement();
					final String value = rb.getString(key);
					result.put(key, value);
				}
			} else {
				name = name.replace('.', '/');
				if (!name.endsWith(SUFFIX)) {
					name = name.concat(SUFFIX);
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
		if (THROW_EXCEPTION_ON_LOAD_FAILURE && (result == null)) {
			throw new IllegalArgumentException("could not load [" + name + "]" + " as "
					+ (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
		}
		return result;
	}

	public static String getPrismPropertyFileName(String arg0) {
		if (Constants.ARG_0_INORS.equalsIgnoreCase(arg0)) {
			return Constants.INORS_PROPERTIES_FILE;
		} else if (Constants.ARG_0_ISTEP.equalsIgnoreCase(arg0)) {
			return Constants.ISTEP_PROPERTIES_FILE;
		} else if (Constants.ARG_0_TASC.equalsIgnoreCase(arg0)) {
			return Constants.TASC_PROPERTIES_FILE;
		} else {
			return null;
		}
	}
	
	public static String getPrismJdbcPropertyFileName(String arg0) {
		if (Constants.ARG_0_INORS.equalsIgnoreCase(arg0)) {
			return Constants.INORS_JDBC_PROPERTIES_FILE;
		} else if (Constants.ARG_0_ISTEP.equalsIgnoreCase(arg0)) {
			return Constants.ISTEP_JDBC_PROPERTIES_FILE;
		} else if (Constants.ARG_0_TASC.equalsIgnoreCase(arg0)) {
			return Constants.TASC_JDBC_PROPERTIES_FILE;
		} else {
			return null;
		}
	}
}