package com.vaannila.util;

import java.util.List;
import java.util.Map;

public class Utils {
	public static String convertListToCommaString(List<String> list) {
		String s = "";
		if (list != null && !list.isEmpty()) {
			s = list.toString();
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}

	public static String mapToJson(Map<String, String> studentDetailsMap) {
		StringBuffer studentDetails = new StringBuffer();
		studentDetails.append("{");
		int count = studentDetailsMap.size();
		int i = 0;
		for (Map.Entry<String, String> entry : studentDetailsMap.entrySet()) {
			i = i + 1;
			String key = entry.getKey();
			String value = entry.getValue();
			studentDetails.append("\"" + key + "\" : \"" + value + "\"");
			if (i != count) {
				studentDetails.append(", ");
			}
		}
		studentDetails.append("}");
		return studentDetails.toString();
	}
}
