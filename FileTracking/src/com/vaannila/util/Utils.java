package com.vaannila.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;


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
	
	/**
	 * @author Joy
	 * @param response
	 * @param data
	 * @param fileName
	 * @param contentType
	 */
	public static void browserDownload(HttpServletResponse response, byte[] data, String fileName, String contentType) {
		response.setContentType(contentType);
		response.setContentLength(data.length);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		try {
			FileCopyUtils.copy(data, response.getOutputStream());
			System.out.println(fileName + "[" + data.length + "] written to output stream");
		} catch (IOException e) {
			System.out.println(fileName + " - "+e);
			e.printStackTrace();
		}
	}
}
