package com.vaannila.util;

import java.util.List;

public class Utils {
	public static String convertListToCommaString(List<String> list) {
		String s = "";
		if (list != null && !list.isEmpty()) {
			s = list.toString();
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}
}
