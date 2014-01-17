/**
 * 
 */
package com.prism.constant;

import com.prism.util.CustomStringUtil;

/**
 * @author Amitabha Roy
 * 
 */
public interface Constants {
	public static final String PROPERTIES_FILE = "inors.properties";

	public static String LOGIN_PDF = "L";
	public static String ALL_PDF = "A";
	public static String IC_PDF = "I";

	public static final boolean THROW_EXCEPTION_ON_LOAD_FAILURE = true;
	public static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
	public static final String SUFFIX = ".properties";

	public static final String GET_SCHOOL_DETAILS = CustomStringUtil.appendString("SELECT ORG_NODEID,", // 1
			"   ORG_NODE_NAME,", // 2
			"   ORG_NODE_CODE,", // 3
			"   ORG_NODE_LEVEL,", // 4
			"   STRC_ELEMENT,", // 5
			"   SPECIAL_CODES,", // 6
			"   PARENT_ORG_NODEID,", // 7
			"   ORG_NODE_CODE_PATH,",// 8
			"   EMAILS,", // 9
			"   CUSTOMERID,", // 10
			"   TO_CHAR(SYSDATE, 'DDMM') AS DATE_STR,", // 11
			"   TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR", // 12
			" FROM ORG_NODE_DIM", " WHERE ORG_NODEID = ?");

	public static final String GET_SCHOOL_USERS = CustomStringUtil.appendString("SELECT U.USERID,", "   U.USERNAME,", "   U.DISPLAY_USERNAME,", "   O.ORG_NODEID,", "   O.ORG_NODE_LEVEL,",
			"   O.ORG_NODE_NAME,", "   O.ORG_NODE_CODE_PATH", " FROM ORG_USERS OU,", "   USERS U,", "   (SELECT ORG_NODEID, ORG_NODE_LEVEL, ORG_NODE_NAME, ORG_NODE_CODE_PATH ",
			"   FROM ORG_NODE_DIM", "     WHERE ORG_NODEID       = ?", "   ) O", " WHERE OU.USERID         = U.USERID", " AND OU.ORG_NODEID       =O.ORG_NODEID", " AND U.ACTIVATION_STATUS = 'AC'",
			" AND U.ACTIVATION_STATUS not in ('SS')", " AND U.IS_NEW_USER       = 'Y'");

	public static final String GET_SCHOOL_USERS_HIERARCHICAL = CustomStringUtil.appendString("SELECT U.USERID,", "   U.USERNAME,", "   U.DISPLAY_USERNAME,", "   O.ORG_NODEID,",
			"   O.ORG_NODE_LEVEL,", "   O.ORG_NODE_NAME,", "   O.ORG_NODE_CODE_PATH", " FROM ORG_USERS OU,", "   USERS U,", "   (SELECT *", "   FROM ORG_NODE_DIM",
			"     START WITH ORG_NODEID       = ?", "     CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID", "   ) O", " WHERE OU.USERID         = U.USERID", " AND OU.ORG_NODEID       =O.ORG_NODEID",
			" AND U.ACTIVATION_STATUS = 'AC'", " AND U.ACTIVATION_STATUS not in ('SS')", " AND U.IS_NEW_USER       = 'Y'");

	public static final String GET_PARENT_ORG_NODEID = "SELECT PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";

	public static final String GET_ORG_MAP = "SELECT ORG_NODEID, ORG_NODE_NAME FROM ORG_NODE_DIM WHERE ORG_NODEID IN (?)";

	public static final String GET_ORG_MAP_ORIGINAL = CustomStringUtil.appendString(" SELECT ORG_NODEID, ORG_NODE_NAME", " FROM ORG_NODE_DIM", " WHERE ORG_NODEID IN", "   (SELECT ORG_NODE FROM",
			"     (SELECT DISTINCT ORG_NODEID AS ORG_NODE_N,", "       HIGHEST_ORG_NODE          AS ORG_NODE_H,", "       PARENT_ORG_NODEID         AS ORG_NODE_P", "     FROM ORGUSER_MAPPING",
			"     WHERE ORG_NODEID IN (?))", "     UNPIVOT (ORG_NODE FOR product_code IN (ORG_NODE_N AS 'N', ORG_NODE_H AS 'H', ORG_NODE_P AS 'P')))");

	public static final String UPDATE_NEW_USER_FLAG = "UPDATE users SET is_new_user = 'N', password = ?, salt = ?, updated_date_time=sysdate WHERE is_new_user = 'Y' AND USERNAME = ? ";

	public static final String GET_SUPPORT_EMAIL_FOR_CUSTOMER = "SELECT SUPPORT_EMAIL FROM CUSTOMER_INFO WHERE CUSTOMERID = ?";

	public static final String GET_SCHOOL_USERS_WITH_ROLES = "SELECT R.DESCRIPTION FROM USER_ROLE UR, ROLE R WHERE UR.USERID = ? AND UR.ROLEID = R.ROLEID ORDER BY DESCRIPTION";

	public static final String GET_PROCESS_ID_NO_CONDITION = "SELECT U.PROCESSID FROM ORG_PROCESS_STATUS U WHERE U.STRUC_ELEMENT = ? ";

	public static final String GET_CURRENT_ADMIN_YEAR = "SELECT ADMINID FROM ADMIN_DIM WHERE CURRENT_ADMIN = 'Y'";

	public static final String NEW_STUDENTS_LOADED = CustomStringUtil.appendString(" SELECT COUNT(*) AS NEW_STUDENT ", " FROM STUDENT_BIO_DIM STD, INVITATION_CODE IC, ",
			"    ORG_NODE_DIM ORG,ADMIN_DIM ADM,GRADE_DIM GRD ", " WHERE  ORG.LEVEL3_JASPER_ORGID=? ", "     AND IC.STUDENT_STRUC_ELEMENT = STD.STRUCTURE_ELEMENT ",
			"     AND STD.ORG_NODEID = ORG.ORG_NODEID ", "     AND  STD.ADMINID = ADM.ADMINID ", "     AND STD.GRADEID = GRD.GRADEID ", "     AND IC.ACTIVATION_STATUS = 'AC' ",
			"     AND IC.NEW_CODE = 'Y' ", "     AND IC.ADMINID = STD.ADMINID ", "     AND IC.ADMINID = (SELECT ADMINID FROM ADMIN_DIM WHERE CURRENT_ADMIN = 'Y')");
}
