package com.ctb.prism.core.util;

public class CustomStringUtil {

	public static String manageString(String value) {
		if (value.length() > 18) {
			value = value.substring(0, 15) + "...";
		}
		return value;
	}

	public static String capitalizeFirstCharacter(String someString) {
		if (someString == null)
			return null;
		if (someString.length() < 2)
			return someString;
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
		if (input == null)
			return null;
		return input.replaceAll("\"", "'");
	}

	public static String getJasperParameterString(String inputControlId) {
		return CustomStringUtil.appendString("$P{", inputControlId, "}");
	}

	/**
	 * Replaces all occurrences of a sub-string in a string.
	 * 
	 * @param text
	 *            The string where it will replace <code>oldsub</code> with
	 *            <code>newsub</code>.
	 * @return String The string after the replacements.
	 */
	public static String replace(String text, String oldsub, String newsub,
			boolean caseInsensitive, boolean firstOnly) {
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
	public static String replaceCharacterInString(char c, String newValue,
			String query) {
		StringBuffer sb = new StringBuffer(query);
		int index = query.indexOf(c);
		sb.replace(index, index + 1, newValue);
		return sb.toString();
	}
}
