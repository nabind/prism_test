package com.prism.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

public class CustomStringUtil {

	private static final Logger logger = Logger.getLogger(CustomStringUtil.class);

	public static String manageString(String value) {
		if (value.length() > 18) {
			value = value.substring(0, 15) + "...";
		}
		return value;
	}

	public static String capitalizeFirstCharacter(String someString) {
		if (someString == null) {
			return null;
		}
		if (someString.length() < 2) {
			return someString;
		}
		StringBuffer buf = new StringBuffer(someString.length());
		buf.append(someString.substring(0, 1).toUpperCase());
		buf.append(someString.substring(1));
		return buf.toString();
	}

	public static String appendString(String... strings) {
		StringBuffer buf = new StringBuffer();
		for (String n : strings) {
			buf.append(n);
		}
		return buf.toString();
	}

	public static String escapeQuote(String input) {
		if (input == null) {
			return null;
		}
		return input.replaceAll("\"", "'");
	}

	public static String getJasperParameterString(String inputControlId) {
		return appendString("$P{", inputControlId, "}");
	}

	/**
	 * Replaces all occurrences of a sub-string in a string.
	 * 
	 * @param text
	 *            The string where it will replace <code>oldsub</code> with
	 *            <code>newsub</code>.
	 * @return String The string after the replacements.
	 */
	public static String replace(String text, String oldsub, String newsub, boolean caseInsensitive, boolean firstOnly) {
		StringBuffer buf;
		int tln;
		int oln = oldsub.length();

		if (oln == 0) {
			int nln = newsub.length();
			if (nln == 0) {
				return text;
			} else {
				if (firstOnly) {
					return newsub + text;
				} else {
					tln = text.length();
					buf = new StringBuffer(tln + (tln + 1) * nln);
					buf.append(newsub);
					for (int i = 0; i < tln; i++) {
						buf.append(text.charAt(i));
						buf.append(newsub);
					}
					return buf.toString();
				}
			}
		} else {
			oldsub = caseInsensitive ? oldsub.toLowerCase() : oldsub;
			String input = caseInsensitive ? text.toLowerCase() : text;
			int e = input.indexOf(oldsub);
			if (e == -1) {
				return text;
			}
			int b = 0;
			tln = text.length();
			buf = new StringBuffer(tln + Math.max(newsub.length() - oln, 0) * 3);
			do {
				buf.append(text.substring(b, e));
				buf.append(newsub);
				b = e + oln;
				e = input.indexOf(oldsub, b);
			} while (e != -1 && !firstOnly);
			buf.append(text.substring(b));
			return buf.toString();
		}
	}

	/**
	 * This method replaces a character from a String
	 * 
	 * @param c
	 * @param newValue
	 * @param query
	 * @return replaced string
	 */
	public static String replaceCharacterInString(char c, String newValue, String query) {
		StringBuffer sb = new StringBuffer(query);
		int index = query.indexOf(c);
		sb.replace(index, index + 1, newValue);
		return sb.toString();
	}

	/**
	 * Usage: CustomStringUtil.replaceAll(fileName, "/", "\\\\"); It will
	 * replace all occurances of "/" to "\\"
	 * 
	 * @param s
	 * @param old
	 * @param now
	 * @return
	 */
	public static String replaceAll(String s, String old, String now) {
		int i = s.indexOf(old);
		while (i > 0) {
			s = s.substring(0, i) + now + s.substring(i + 1);
			i = s.indexOf(old);
		}
		return s;
	}

	// Istep

	/**
	 * @param str
	 * @param occurrence
	 * @param val
	 * @return
	 */
	public static String setQueryString(String str, int occurrence, String val) {
		if (occurrence > 0) {
			// Skip all '?' before occurrence
			for (int i = 1; i < occurrence; i++) {
				str = str.replaceFirst("\\?", "`");
			}
			// Actual replacement
			str = str.replaceFirst("\\?", "?" + val);
			// Revert '`' to '?'
			str = str.replaceAll("`", "?");
		}
		return str;
	}

	/**
	 * Use it before printing SQL
	 * 
	 * @param str
	 * @return
	 */
	public static String getQueryString(String str) {
		return str.replaceAll("\\?", "");
	}

	/**
	 * Creates a new array with args[1] to args[last element].
	 * 
	 * @param args
	 * @return
	 */
	public static String[] getAllButFirstArg(String[] args) {
		int length = (args.length > 0) ? args.length - 1 : 0;
		String[] argArray = new String[length];
		System.arraycopy(args, 1, argArray, 0, length);
		return argArray;
	}

	public static String getDateTimeInors(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
		return dateformatter.format(cal.getTime());
	}

	// chops a list into non-view sublists of length L
	public static <T> List<List<T>> splitTheList(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		if (L == 0) {
			parts.add(list);
			return parts;
		}
		final int N = list.size();
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

	public static void main(String[] args) {
		String str = "select * from abc where a = ? and b = ? and c = ?";
		str = setQueryString(str, 2, "B");
		str = setQueryString(str, 1, "A");
		str = setQueryString(str, 3, "C");
		str = getQueryString(str);
		logger.info(str);
	}
}
