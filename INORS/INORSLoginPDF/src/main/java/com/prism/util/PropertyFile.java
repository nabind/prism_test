package com.prism.util;

import static com.prism.constant.Constants.LOAD_AS_RESOURCE_BUNDLE;
import static com.prism.constant.Constants.SUFFIX;
import static com.prism.constant.Constants.THROW_EXCEPTION_ON_LOAD_FAILURE;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import com.prism.constant.Constants;

public class PropertyFile {

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)} that uses the current thread's context class loader.
	 * 
	 * @param name
	 * @return
	 */
	public static Properties loadProperties(final String name) {
		return loadProperties(name, Thread.currentThread().getContextClassLoader());
	}

	public static void main(String[] args) {
		Properties p = loadProperties(Constants.PROPERTIES_FILE);

		System.out.println("done " + p.get("TXT_ONE"));
	}

	/**
	 * Looks up a resource named 'name' in the classpath. The resource must map to a file with .properties extention. The name is assumed to be absolute and can use either "/" or "." for package
	 * segment separation with an optional leading "/" and optional ".properties" suffix. Thus, the following names refer to the same resource: some.pkg.PropertyFileResource
	 * some.pkg.PropertyFileResource.properties some/pkg/PropertyFileResource some/pkg/PropertyFileResource.properties /some/pkg/PropertyFileResource /some/pkg/PropertyFileResource.properties
	 * 
	 * @param name
	 *            classpath resource name [may not be null]
	 * @param loader
	 *            classloader through which to load the resource [null is equivalent to the application loader]
	 * @return
	 */
	private static Properties loadProperties(String name, ClassLoader loader) {
		if (name == null)
			throw new IllegalArgumentException("null input: name");

		if (name.startsWith("/"))
			name = name.substring(1);

		if (name.endsWith(Constants.SUFFIX)) {
			int to = name.length() - SUFFIX.length();
			name = name.substring(0, to);
		}

		Properties result = null;

		InputStream in = null;
		try {
			if (loader == null)
				loader = ClassLoader.getSystemClassLoader();

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

				if (!name.endsWith(SUFFIX))
					name = name.concat(SUFFIX);

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
				}
		}

		if (THROW_EXCEPTION_ON_LOAD_FAILURE && (result == null)) {
			throw new IllegalArgumentException("could not load [" + name + "]" + " as " + (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
		}

		return result;
	}

}
