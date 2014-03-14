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
import com.ctb.prism.inors.transferobject.LayoutTO;

/**
 * @author Amitabha Roy
 * 
 */
public class InorsDownloadUtil {

	private static final IAppLogger logger = LogFactory.getLoggerInstance(InorsDownloadUtil.class.getName());

	public static byte[] getTableDataBytes(ArrayList<ArrayList<LayoutTO>> tableData, final String delimiter) {
		if (tableData != null && !tableData.isEmpty()) {
			CharArrayWriter out = new CharArrayWriter();
			try {
				// Write the Header
				for (LayoutTO to : tableData.get(0)) {
					out.write(to.getHeaderText());
					out.write(delimiter);
				}
				out.write("\n");

				// Write the row wise data
				for (ArrayList<LayoutTO> rowData : tableData) {
					for (LayoutTO to : rowData) {
						out.write(to.getColumnData());
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
	 * @param layoutTOList
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<ArrayList<LayoutTO>> getTableDataFromResultSet(ResultSet rs, ArrayList<LayoutTO> layoutTOList) throws SQLException {
		ArrayList<ArrayList<LayoutTO>> tableData = new ArrayList<ArrayList<LayoutTO>>();
		while (rs.next()) {
			for (int i = 0; i < layoutTOList.size(); i++) {
				LayoutTO to = layoutTOList.get(i);
				String columnAlias = to.getColumnAlias();
				if ("*".equals(columnAlias)) {
					to.setColumnData(wrap("", '"'));
				} else {
					to.setColumnData(wrap(rs.getString(columnAlias), '"'));
				}
			}
			tableData.add(layoutTOList);
		}
		return tableData;
	}

	/**
	 * Maps the Headers with the Aliases.
	 * 
	 * @param headers
	 * @param aliases
	 * @return
	 */
	public static ArrayList<LayoutTO> getRowDataLayout(String headers, String aliases) {
		ArrayList<LayoutTO> rowDataLayout = new ArrayList<LayoutTO>();
		String[] headerTokens = headers.split("\\|");
		String[] aliasTokens = aliases.split("\\|");
		int headerCount = headerTokens.length;
		int aliasCount = aliasTokens.length;
		logger.log(IAppLogger.INFO, "Headers = " + headerCount);
		logger.log(IAppLogger.INFO, "Aliases = " + aliasCount);
		if (headerCount != aliasCount) {
			logger.log(IAppLogger.WARN, "Headers and Aliases do not match. Please configure Properties for them.");
		}
		for (int i = 0; i < headerCount; i++) {
			rowDataLayout.add(new LayoutTO(i + 1, headerTokens[i].trim(), aliasTokens[i].trim(), ""));
		}
		return rowDataLayout;
	}

	/**
	 * Prints a rowDataLayout.
	 * 
	 * @param rowDataLayout
	 */
	public static void print(ArrayList<LayoutTO> rowDataLayout) {
		for (LayoutTO to : rowDataLayout) {
			System.out.println(to);
		}
	}

	/**
	 * Prints a tableDataLayout.
	 * 
	 * @param tableDataLayout
	 */
	public static void printTable(ArrayList<ArrayList<LayoutTO>> tableDataLayout) {
		for (ArrayList<LayoutTO> rowDataLayout : tableDataLayout) {
			print(rowDataLayout);
		}
	}
}
