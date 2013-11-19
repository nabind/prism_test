package com.ctb.prism.web.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import oracle.sql.CLOB;

import org.apache.commons.lang.StringEscapeUtils;


public class Utility {


	
	public static String sanitizeHtmlValue(String input) {
		String result = null;
		if (input != null) {
			result = StringEscapeUtils.escapeHtml(input);
			if (result != null) {
				result = result.replaceAll("'", "&#39;");
			}
		}
		return result;
	}

	public static String sanitizeHtmlValue(long input) {
		String tempInput = String.valueOf(input);
		String result = sanitizeHtmlValue(tempInput);
		return result;
	}

	public static String sanitizeHtmlValue(int input) {
		String tempInput = String.valueOf(input);
		String result = sanitizeHtmlValue(tempInput);
		return result;
	}

	public static String sanitizeHtmlValue(Object input) {
		String tempInput = String.valueOf(input);
		String result = sanitizeHtmlValue(tempInput);
		return result;
	}

	public static String sanitizeHtmlValue(double input) {
		String tempInput = String.valueOf(input);
		String result = sanitizeHtmlValue(tempInput);
		return result;
	}

	public static String sanitizeHtmlValue(boolean input) {
		String tempInput = String.valueOf(input);
		String result = sanitizeHtmlValue(tempInput);
		return result;
	}
}
