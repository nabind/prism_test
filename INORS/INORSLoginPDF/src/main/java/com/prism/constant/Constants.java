package com.prism.constant;

import com.prism.util.CustomStringUtil;

/**
 * @author Amitabha Roy
 * 
 */
public interface Constants {
	public static final String PROPERTIES_FILE = "inors.properties";

	public static enum ARGS_OPTIONS {
		L, A, I
	};

	public static enum ORG_STATUS {
		SU, ER, CP, IP
	};

	public static final boolean THROW_EXCEPTION_ON_LOAD_FAILURE = true;
	public static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
	public static final String SUFFIX = ".properties";

	public static final String GET_SCHOOL_DETAILS = CustomStringUtil.appendString("SELECT ORG_NODEID,", // 1
			" ORG_NODE_NAME,", // 2
			" ORG_NODE_CODE,", // 3
			" ORG_NODE_LEVEL,", // 4
			" STRC_ELEMENT,", // 5
			" SPECIAL_CODES,", // 6
			" PARENT_ORG_NODEID,", // 7
			" ORG_NODE_CODE_PATH,",// 8
			" EMAILS,", // 9
			" CUSTOMERID,", // 10
			" TO_CHAR(SYSDATE, 'DDMM') AS DATE_STR,", // 11
			" TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR", // 12
			" FROM ORG_NODE_DIM WHERE ORG_NODEID = ?");

	public static final String GET_SCHOOL_USERS = CustomStringUtil.appendString(
			"SELECT U.USERID, U.USERNAME, U.DISPLAY_USERNAME, O.ORG_NODEID, O.ORG_NODE_LEVEL, O.ORG_NODE_NAME,",
			" O.ORG_NODE_CODE_PATH FROM ORG_USERS OU, USERS U, (SELECT ORG_NODEID, ORG_NODE_LEVEL, ORG_NODE_NAME,",
			" ORG_NODE_CODE_PATH FROM ORG_NODE_DIM",
			" WHERE ORG_NODEID = ?) O WHERE OU.USERID = U.USERID AND OU.ORG_NODEID =O.ORG_NODEID AND U.ACTIVATION_STATUS = 'AC'",
			" AND U.ACTIVATION_STATUS not in ('SS') AND U.IS_NEW_USER = 'Y'");

	public static final String GET_SCHOOL_USERS_HIERARCHICAL = CustomStringUtil.appendString(
			"SELECT U.USERID, U.USERNAME, U.DISPLAY_USERNAME, O.ORG_NODEID,",
			" O.ORG_NODE_LEVEL, O.ORG_NODE_NAME, O.ORG_NODE_CODE_PATH FROM ORG_USERS OU, USERS U, (SELECT * FROM ORG_NODE_DIM",
			" START WITH ORG_NODEID = ? CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID ) O ",
			" WHERE OU.USERID = U.USERID AND OU.ORG_NODEID =O.ORG_NODEID",
			" AND U.ACTIVATION_STATUS = 'AC' AND U.ACTIVATION_STATUS not in ('SS') AND U.IS_NEW_USER = 'Y'");

	public static final String GET_PARENT_ORG_NODEID = "SELECT PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";

	public static final String GET_ORG_MAP = "SELECT ORG_NODEID, ORG_NODE_NAME FROM ORG_NODE_DIM WHERE ORG_NODEID IN (?)";

	public static final String GET_ORG_MAP_ORIGINAL = CustomStringUtil.appendString(
			"SELECT ORG_NODEID, ORG_NODE_NAME FROM ORG_NODE_DIM WHERE ORG_NODEID IN (SELECT ORG_NODE FROM",
			" (SELECT DISTINCT ORG_NODEID AS ORG_NODE_N, HIGHEST_ORG_NODE AS ORG_NODE_H, PARENT_ORG_NODEID AS ORG_NODE_P",
			" FROM ORGUSER_MAPPING WHERE ORG_NODEID IN (?))",
			" UNPIVOT (ORG_NODE FOR product_code IN (ORG_NODE_N AS 'N', ORG_NODE_H AS 'H', ORG_NODE_P AS 'P')))");

	public static final String UPDATE_NEW_USER_FLAG = "UPDATE users SET is_new_user = 'N', password = ?, salt = ?, updated_date_time=sysdate WHERE is_new_user = 'Y' AND USERNAME = ? ";

	public static final String GET_SUPPORT_EMAIL_FOR_CUSTOMER = "SELECT SUPPORT_EMAIL FROM CUSTOMER_INFO WHERE CUSTOMERID = ?";

	public static final String GET_SCHOOL_USERS_WITH_ROLES = "SELECT R.DESCRIPTION FROM USER_ROLE UR, ROLE R WHERE UR.USERID = ? AND UR.ROLEID = R.ROLEID ORDER BY DESCRIPTION";

	public static final String GET_PROCESS_ID_NO_CONDITION = "SELECT PROCESSID FROM ORG_PROCESS_STATUS WHERE STRUC_ELEMENT = ?";

	public static final String GET_CURRENT_ADMIN_YEAR = "SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y'";

	public static final String GET_IC_LETTER_PATH_LIST = CustomStringUtil.appendString(
			"SELECT IC_FILE_LOC FROM STUDENT_BIO_DIM STD, INVITATION_CODE IC, ORG_NODE_DIM ORG,",
			" ADMIN_DIM ADM, GRADE_DIM GRD WHERE ORG.ORG_NODEID=? AND IC.STUDENT_BIO_ID = STD.STUDENT_BIO_ID",
			" AND STD.ORG_NODEID = ORG.ORG_NODEID AND STD.ADMINID= ADM.ADMINID",
			" AND STD.GRADEID= GRD.GRADEID AND IC.ACTIVATION_STATUS = 'AC' AND IC.IS_NEW_IC= 'Y' AND IC.ADMINID = STD.ADMINID",
			" AND IC.ADMINID IN (SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y')");

	public static final String GET_ORG_LABEL_MAP = CustomStringUtil.appendString(
			"SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL",
			" FROM (SELECT DISTINCT ORG_LEVEL,ORG_LABEL FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP GROUP BY TEMP.ORG_LEVEL");

	public static final String UPDATE_PROCESS_STATUS = "UPDATE ORG_PROCESS_STATUS SET PROCESS_STATUS = ?, UPDATED_DATE_TIME = SYSDATE WHERE PROCESSID = ?";

	public static final String UPDATE_MAIL_STATUS = "UPDATE ORG_PROCESS_STATUS SET TARGET_EMAIL_STATUS = ?, UPDATED_DATE_TIME = SYSDATE WHERE PROCESSID = ?";
	public static final String GET_STUDENT_ID_LIST = "SELECT DISTINCT STUDENT_BIO_ID FROM STUDENT_BIO_DIM WHERE ORG_NODEID = ?";

}
