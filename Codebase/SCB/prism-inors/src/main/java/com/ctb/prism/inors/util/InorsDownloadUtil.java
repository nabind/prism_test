/**
 * 
 */
package com.ctb.prism.inors.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beanio.stream.RecordIOException;

import com.ctb.prism.admin.transferobject.UserDataTO;
import com.ctb.prism.admin.transferobject.UserTO;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.util.CustomStringUtil;

/**
 * @author Amitabha Roy
 * 
 */
public class InorsDownloadUtil {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsDownloadUtil.class.getName());

	public static byte[] getTableDataBytes(ArrayList<ArrayList<String>> tableData, final String delimiter) {
		if (tableData != null && !tableData.isEmpty()) {
			CharArrayWriter out = new CharArrayWriter();
			try {
				for (ArrayList<String> rowData : tableData) {
					int i = 0;
					for (String data : rowData) {
						i = i + 1;
						out.write(data);
						if (i < rowData.size())
							out.write(delimiter);
					}
					out.write("\n");
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "Table Data bytes created");
			} catch (IOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();

			}
			return out.toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	/**
	 * This method is used to enclose the data with double quote
	 * 
	 * @param data
	 * @param wrapChar
	 * @return
	 */
	public static String wrap(Object data, char wrapChar) {
		if ((data == null) || ("NULL").equalsIgnoreCase(data.toString().trim())) {
			data = "";
		}
		StringBuilder sb = new StringBuilder(data.toString());
		sb.insert(0, wrapChar);
		sb.insert(sb.length(), wrapChar);
		return sb.toString();
	}

	/**
	 * Creates a byte array from the UserData list
	 * 
	 * @param userList
	 * @param delimiter
	 * @return
	 */
	public static byte[] getUserDataBytes(List<UserDataTO> userList, String delimiter) {
		if (userList != null) {
			logger.log(IAppLogger.INFO, "User : " + userList.size());
			userList.add(0, getUserDataTOHeader());

			CharArrayWriter out = new CharArrayWriter();
			try {
				for (UserDataTO user : userList) {
					out.write(user.getUserId());
					out.write(delimiter);
					out.write(user.getFullName());
					out.write(delimiter);
					out.write(user.getStatus());
					out.write(delimiter);
					out.write(user.getOrgName());
					out.write(delimiter);
					out.write(user.getUserRoles());
					out.write("\n");
				}
				out.flush();
				out.close();
				logger.log(IAppLogger.INFO, "User Byte Array [" + out.size() + "] Created Successfully");
			} catch (RecordIOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.log(IAppLogger.ERROR, "", e);
				e.printStackTrace();
			}
			return out.toString().getBytes();
		} else {
			return "No Records Found".getBytes();
		}
	}

	/**
	 * Method to create TO representation of the header record. It is used to write the header inside the file.
	 * 
	 * @return TO representation of the header record
	 */
	private static UserDataTO getUserDataTOHeader() {
		UserDataTO header = new UserDataTO();
		header.setUserId("User Id");
		header.setFullName("Full Name");
		header.setStatus("Status");
		header.setOrgName("Org Name");
		header.setUserRoles("User Roles");
		return header;
	}

	/**
	 * Converts List<UserTO> to List<UserDataTO>.
	 * 
	 * @param userList
	 * @return
	 */
	public static List<UserDataTO> getUserDataList(List<UserTO> userList) {
		List<UserDataTO> userDataList = new ArrayList<UserDataTO>();
		for (UserTO userTO : userList) {
			UserDataTO userDataTO = new UserDataTO();
			userDataTO.setUserId(String.valueOf(userTO.getUserId()));
			userDataTO.setFullName(userTO.getUserName());
			userDataTO.setStatus(userTO.getStatus());
			userDataTO.setOrgName(userTO.getTenantName());
			String userRoles = userTO.getAvailableRoleList().toString();
			userRoles = CustomStringUtil.replaceCharacterInString('[', "", userRoles);
			userRoles = CustomStringUtil.replaceCharacterInString(']', "", userRoles);
			userDataTO.setUserRoles("User Roles");
			userDataList.add(userDataTO);
		}
		return userDataList;
	}

	/**
	 * Maps a ResultSet to a tableDataLayout.
	 * 
	 * @param rs
	 * @param StringList
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<ArrayList<String>> getTableDataFromResultSet(ResultSet rs, ArrayList<String> aliasList, ArrayList<String> headerList) throws SQLException {
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();

		// Add Header
		ArrayList<String> headerData = new ArrayList<String>();
		for (String headerText : headerList) {
			headerData.add(headerText.trim());
		}
		tableData.add(headerData);

		// Add Rows
		while (rs != null && rs.next()) { // Rows
			ArrayList<String> rowData = new ArrayList<String>();
			for (String columnAlias : aliasList) { // Columns
				columnAlias = columnAlias.trim();
				String data = "";
				if ("*".equals(columnAlias)) {
					data = (wrap("", '"'));
				} else {
					try {
						data = (wrap(rs.getString(columnAlias), '"'));
					} catch (Exception e) {
						data = (wrap("", '"'));
						logger.log(IAppLogger.ERROR, columnAlias + ": " + e.getMessage());
					}
				}
				rowData.add(data);
			}
			tableData.add(rowData);
		}
		return tableData;
	}

	public static ArrayList<String> getRowDataLayout(String commaString) {
		ArrayList<String> rowDataLayout = new ArrayList<String>();
		if (commaString == null || commaString.isEmpty()) {
			logger.log(IAppLogger.WARN, "Invalid comma separated string: " + commaString);
			return rowDataLayout;
		}
		String[] tokens = commaString.split("\\|");
		for (int i = 0; i < tokens.length; i++) {
			rowDataLayout.add(tokens[i]);
		}
		return rowDataLayout;
	}

}
