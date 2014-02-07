package com.ctb.prism.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ctb.prism.core.constant.IApplicationConstants;
import com.ctb.prism.core.constant.IApplicationConstants.ROLE_TYPE;
import com.ctb.prism.core.constant.IApplicationConstants.USER_TYPE;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * This class contains application specific utility methods. This class is not meant to be sub-classed. All the methods declared in this class are static.
 */
public final class Utils {
	private static final IAppLogger logger = LogFactory.getLoggerInstance(Utils.class.getName());

	public static String getSaltWithUser(String userName, String salt) {
		if (userName != null) {
			return CustomStringUtil.appendString(userName.toLowerCase(), salt);
		} else {
			return CustomStringUtil.appendString(userName, salt);
		}
	}

	/**
	 * Validates whether the roleName is a valid role or not. Returns true if the roleName is valid, false otherwise.
	 */
	public static boolean isValidRole(String roleName) {
		USER_TYPE[] userTypes = IApplicationConstants.USER_TYPE.values();
		for (USER_TYPE userType : userTypes) {
			if (userType.toString().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Validates whether the roleName is a valid role or not. Returns true if the roleName is valid, false otherwise.
	 */
	public static boolean isValidRoles(String roleName) {
		ROLE_TYPE[] userTypes = IApplicationConstants.ROLE_TYPE.values();
		for (ROLE_TYPE userType : userTypes) {
			if (userType.toString().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the user role corresponding to the role name. In case the role is not found null is returned.
	 * 
	 * @param roleName
	 * @return {@link USER_TYPE}
	 */
	public static IApplicationConstants.USER_TYPE getRole(String roleName) {
		USER_TYPE[] userTypes = IApplicationConstants.USER_TYPE.values();
		for (USER_TYPE userType : userTypes) {
			if (userType.toString().equals(roleName)) {
				return userType;
			}
		}
		return null;
	}

	/**
	 * Returns the user role corresponding to the role name. In case the role is not found null is returned.
	 * 
	 * @param roleName
	 * @return {@link USER_TYPE}
	 */
	public static IApplicationConstants.ROLE_TYPE getRoles(String roleName) {
		ROLE_TYPE[] roleTypes = IApplicationConstants.ROLE_TYPE.values();
		for (ROLE_TYPE userType : roleTypes) {
			if (userType.toString().equals(roleName)) {
				return userType;
			}
		}
		return null;
	}

	/**
	 * Checks if password confirms CTB's password policy
	 * 
	 * @param passwd
	 * @return
	 */
	public static boolean validatePassword(String passwd) {
		// String pattern ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
		/**
		 * ^ # start-of-string (?=.*[0-9]) # a digit must occur at least once (?=.*[a-z]) # a lower case letter must occur at least once (?=.*[A-Z]) # an upper case letter must occur at least once
		 * (?=.*[@#$%^&+=]) # a special character must occur at least once (?=\S+$) # no whitespace allowed in the entire string .{8,} # anything, at least eight places though $ # end-of-string
		 */

		return (passwd.matches(pattern));
	}

	/**
	 * This parameter is used to locate static PDF saved location
	 */
	private String acsiPdfLocation = "/mnt/ACSIREPORTS/dev/";

	public String getAcsiPdfLocation() {
		return acsiPdfLocation;
	}

	public void setAcsiPdfLocation(String acsiPdfLocation) {
		// this property is set from dispatcher-servlet.xml (<!-- Set PDF location -->) through setter injection
		this.acsiPdfLocation = acsiPdfLocation;
	}

	/**
	 * Create file from a inputStream
	 * 
	 * @param inputStream
	 * @param fileName
	 * @throws IOException
	 */
	public static File convertInputStreamToFile(InputStream inputStream, String fileName) throws IOException {
		OutputStream out = null;
		File file = null;
		try {
			file = new File(fileName);
			// write the inputStream to a FileOutputStream
			out = new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			logger.log(IAppLogger.DEBUG, "File created ... ");
		} catch (IOException e) {
			logger.log(IAppLogger.ERROR, e.getMessage());
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		return file;
	}

	/**
	 * Create String from a inputStream
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public static String convertInputStreamToString(InputStream inputStream) throws IOException {
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		// logger.log(IAppLogger.DEBUG, sb.toString());
		br.close();
		return sb.toString();
	}

	/**
	 * Utility method to convert InputStream to byte[]
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getFileDataFromInputStream(InputStream inputStream) {
		byte[] fileData = null;
		int i = 0;
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		try {
			while ((i = inputStream.read()) != -1) {
				baos.write(i);
			}
			fileData = baos.toByteArray();
		} catch (java.net.MalformedURLException exMalformedURLException) {
			exMalformedURLException.printStackTrace();
		} catch (java.io.IOException exIOException) {
			exIOException.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				baos.close();
			} catch (IOException exIOException) {
				exIOException.printStackTrace();
			}
		}
		return fileData;
	}

	public static String getDateTime() {
		return getDateTime("dd-MM-yyyy HH-mm-ss");
	}

	public static String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(dateFormat);
		return dateformatter.format(cal.getTime());
	}

	/**
	 * To convert Oracle CLOB to String
	 * 
	 * @param Oracle
	 *            CLOB
	 * @return String
	 */
	public static String convertClobToString(final Clob oracleClob) throws SQLException, IOException {
		StringBuffer stringBuffer = new StringBuffer("");
		BufferedReader bufferedReader = null;
		String tempString;
		if (oracleClob != null) {
			bufferedReader = new BufferedReader(oracleClob.getCharacterStream());
			while ((tempString = bufferedReader.readLine()) != null) {
				stringBuffer.append("\n").append(tempString);
			}
		}
		return stringBuffer.toString();
	}

	public static int batchUpdateCheck(final int paramArr[]) throws SQLException, IOException {
		int batchUpdateFlag = 1;
		for (int i : paramArr) {
			if (i == (IApplicationConstants.DEFAULT_BATCH_UPDATE_VALUE + 1) || i < IApplicationConstants.DEFAULT_BATCH_UPDATE_VALUE) {
				batchUpdateFlag = 0;
				break;
			}
		}
		return batchUpdateFlag;
	}

	public static String convertStrArrayToCommaString(final String[] paramStr) {
		StringBuffer returnStrBuff = new StringBuffer("");
		boolean flag = false;
		if (paramStr != null) {
			for (String param : paramStr) {
				returnStrBuff.append(param);
				returnStrBuff.append(",");
				flag = true;
			}
			if (flag) {
				returnStrBuff.deleteCharAt(returnStrBuff.length() - 1);
			}
		}
		return returnStrBuff.toString();
	}

	/**
	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object) {
		String json = null;
		json = new Gson().toJson(object);
		return json;
	}

	/**
	 * @param json
	 * @param klass
	 * @return
	 */
	public static <T> T jsonToObject(String json, Class<T> klass) {
		T object = null;
		try {
			object = new Gson().fromJson(json, klass);
		} catch (JsonSyntaxException e) {
			logger.log(IAppLogger.WARN, klass + " : " + e.getMessage());
		}
		return object;
	}

}
