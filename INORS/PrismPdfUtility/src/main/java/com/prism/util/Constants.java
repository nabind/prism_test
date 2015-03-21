package com.prism.util;

/**
 * @author Amitabha Roy
 * 
 */
public interface Constants {
	public static final String ARG_0_INORS = "I";
	public static final String ARG_0_INORS_GRWOTH= "P";
	public static final String ARG_0_TASC = "T";
	public static final String ARG_0_MISSOURI = "M";

	public static final String INORS_PROPERTIES_FILE = "inors.properties";
	public static final String INORS_JDBC_PROPERTIES_FILE = "inors_jdbc.properties";

	public static final String INORS_GROWTH_PROPERTIES_FILE = "inors_grw.properties";
	public static final String INORS_GROWTH_JDBC_PROPERTIES_FILE = "inors_grw_jdbc.properties";

	public static final String TASC_PROPERTIES_FILE = "tasc.properties";
	public static final String TASC_JDBC_PROPERTIES_FILE = "tasc_jdbc.properties";
	
	public static final String MAP_PROPERTIES_FILE = "map.properties";
	public static final String MAP_JDBC_PROPERTIES_FILE = "map_jdbc.properties";

	/*
	 * L : Login PDF A: All = Login + IC I: IC for school S: IC for individual
	 * student in a school X: BIO_STUDENT_EXTRACT
	 */
	public static enum ARGS_OPTIONS {
		L, A, I, S, X
	};

	public static enum ORG_STATUS {
		SU, ER, CP, IP
	};

	public static final boolean THROW_EXCEPTION_ON_LOAD_FAILURE = true;
	public static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
	public static final String SUFFIX = ".properties";

	public static final String GET_SCHOOL_DETAILS = CustomStringUtil.appendString("SELECT SCHOOL.ORG_NODE_NAME AS SCHOOL_NAME,",
			" SCHOOL.ORG_NODE_CODE AS SCHOOL_CODE,", " DISTRICT.ORG_NODE_NAME AS DISTRICT_NAME,", " DISTRICT.ORG_NODE_CODE AS DISTRICT_CODE,",
			" DISTRICT.ORG_MODE AS ORG_MODE,", " SCHOOL.ORG_NODEID,", " SCHOOL.ORG_NODE_LEVEL,", " SCHOOL.STRC_ELEMENT,", " SCHOOL.SPECIAL_CODES,",
			" SCHOOL.PARENT_ORG_NODEID,", " SCHOOL.ORG_NODE_CODE_PATH,", " SCHOOL.EMAILS,", " SCHOOL.CUSTOMERID,", " TO_CHAR(SYSDATE, 'DDMM') AS DATE_STR,",
			" TO_CHAR(SYSDATE, 'DDMMYYHH24MISS') AS DATE_STR_WT_YEAR", " FROM ORG_NODE_DIM SCHOOL, ORG_NODE_DIM DISTRICT",
			" WHERE SCHOOL.PARENT_ORG_NODEID = DISTRICT.ORG_NODEID", " AND SCHOOL.ORG_NODE_LEVEL = 3", " AND DISTRICT.ORG_NODE_LEVEL = 2",
			" AND SCHOOL.ORG_NODEID = ?");

	public static final String GET_SCHOOL_USERS = CustomStringUtil.appendString(
			"SELECT U.USERID, U.USERNAME, U.DISPLAY_USERNAME, O.ORG_NODEID, O.ORG_NODE_LEVEL, O.ORG_NODE_NAME,",
			" O.ORG_NODE_CODE_PATH FROM ORG_USERS OU, USERS U, (SELECT ORG_NODEID, ORG_NODE_LEVEL, ORG_NODE_NAME,", " ORG_NODE_CODE_PATH FROM ORG_NODE_DIM",
			" WHERE ORG_NODEID = ?) O WHERE OU.USERID = U.USERID AND OU.ORG_NODEID =O.ORG_NODEID AND U.ACTIVATION_STATUS = 'AC'",
			" AND U.ACTIVATION_STATUS not in ('SS') AND U.IS_NEW_USER = 'Y'");

	public static final String GET_SCHOOL_USERS_HIERARCHICAL = CustomStringUtil.appendString("SELECT U.USERID, U.USERNAME, U.DISPLAY_USERNAME, O.ORG_NODEID,",
			" O.ORG_NODE_LEVEL, O.ORG_NODE_NAME, O.ORG_NODE_CODE_PATH FROM ORG_USERS OU, USERS U, (SELECT * FROM ORG_NODE_DIM",
			" START WITH ORG_NODEID = ? CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID ) O ", " WHERE OU.USERID = U.USERID AND OU.ORG_NODEID =O.ORG_NODEID",
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
			"SELECT FILENAME IC_FILE_LOC FROM STUDENT_BIO_DIM STD, INVITATION_CODE IC, ORG_NODE_DIM ORG,",
			" ADMIN_DIM ADM, GRADE_DIM GRD WHERE ORG.ORG_NODEID=? AND IC.STUDENT_BIO_ID = STD.STUDENT_BIO_ID",
			" AND STD.ORG_NODEID = ORG.ORG_NODEID AND STD.ADMINID= ADM.ADMINID",
			" AND STD.GRADEID= GRD.GRADEID AND IC.ACTIVATION_STATUS = 'AC' AND IC.IS_NEW_IC= 'Y' AND IC.ADMINID = STD.ADMINID",
			" AND IC.ADMINID IN (SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y')");

	public static final String GET_ORG_LABEL_MAP = CustomStringUtil.appendString(
			"SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL",
			" FROM (SELECT DISTINCT ORG_LEVEL,ORG_LABEL FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP GROUP BY TEMP.ORG_LEVEL");

	public static final String UPDATE_PROCESS_STATUS = "UPDATE ORG_PROCESS_STATUS SET PROCESS_STATUS = ?, UPDATED_DATE_TIME = SYSDATE WHERE PROCESSID = ?";

	public static final String UPDATE_MAIL_STATUS = "UPDATE ORG_PROCESS_STATUS SET TARGET_EMAIL_STATUS = ?, UPDATED_DATE_TIME = SYSDATE WHERE PROCESSID = ?";
	public static final String GET_STUDENT_ID_LIST = CustomStringUtil
			.appendString("SELECT DISTINCT TEST_ELEMENT_ID FROM INVITATION_CODE WHERE ORG_NODEID = ? AND IS_NEW_IC = 'Y' ORDER BY 1 DESC");
	public static final String GET_NEW_STUDENTS = CustomStringUtil
			.appendString("SELECT TEST_ELEMENT_ID FROM INVITATION_CODE WHERE ORG_NODEID = ? AND IS_NEW_IC = 'Y' ");
	public static final String GET_STUDENT_ID_LIST_FROM_EXT = "SELECT DISTINCT BIO_EXTRACTID FROM BIO_STUDENT_EXTRACT WHERE SCHOOL_ORG_NODEID = ?";
	public static final String UPDATE_STUDENT_PDF_LOC = "UPDATE INVITATION_CODE SET FILENAME = ?, IS_NEW_IC='N', UPDATED_DATE_TIME = SYSDATE WHERE TEST_ELEMENT_ID = ?";

	public static final String GET_PRODUCT_NAME = CustomStringUtil.appendString("SELECT PRODUCT_NAME", " FROM PRODUCT", " WHERE PRODUCT_NAME LIKE 'ISTEP%'",
			" AND SUBSTR(PRODUCT_NAME, -4) = (SELECT ADMIN_YEAR FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN='Y' AND ROWNUM=1)");

	public static final String GET_ORG = "SELECT ORG_NODEID, ORG_NODE_NAME, PARENT_ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODEID = ? AND ORG_NODE_LEVEL = ?";

	public static final String GET_ROOT_PATH = CustomStringUtil.appendString("SELECT DISTINCT CUST.FILE_LOCATION || PROD.FILE_LOCATION || '/IC/'",
			" FROM CUSTOMER_INFO     CUST,", " PRODUCT           PROD,", " CUST_PRODUCT_LINK LIN,", " ORG_PRODUCT_LINK  OPL,", " INVITATION_CODE   IC",
			" WHERE LIN.CUSTOMERID = CUST.CUSTOMERID", " AND LIN.PRODUCTID = PROD.PRODUCTID", " AND LIN.CUST_PROD_ID = OPL.CUST_PROD_ID(+)",
			" AND IC.ORG_NODEID = ?", " AND PROD.PRODUCT_NAME LIKE '%ISTEP%'", " AND LIN.CUST_PROD_ID = IC.CUST_PROD_ID", " AND IC.TEST_ELEMENT_ID = ?");
	
	public static final String GET_ROOT_PATH_CURR_ADMIN =  CustomStringUtil.appendString("SELECT DISTINCT CUST.FILE_LOCATION || PROD.FILE_LOCATION || '/IC' ",
			" FROM CUSTOMER_INFO CUST, PRODUCT PROD, CUST_PRODUCT_LINK LIN, ADMIN_DIM ADM ",
			"  WHERE LIN.CUSTOMERID = CUST.CUSTOMERID AND LIN.PRODUCTID = PROD.PRODUCTID ",
			"  AND PROD.PRODUCT_NAME LIKE '%ISTEP%' AND LIN.ADMINID = ADM.ADMINID ",
			" AND ADM.IS_CURRENT_ADMIN = 'Y' AND CUST.CUSTOMERID = ?");
}
