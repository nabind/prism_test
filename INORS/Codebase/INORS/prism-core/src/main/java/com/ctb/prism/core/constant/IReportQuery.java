package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IReportQuery {

	// query to retrieve JRXML from database
	public static final String GET_JASPER_REPORT_OBJECT = CustomStringUtil.appendString(
			"SELECT FR.FILE_TYPE, FR.DATA DATA, re.label LABEL FROM JIRESOURCEFOLDER RF, JIRESOURCE RE, JIFILERESOURCE FR" ,
	" WHERE RF.URI LIKE ? AND RF.ID = RE.PARENT_FOLDER AND RE.ID = FR.ID and (FR.FILE_TYPE = 'jrxml' OR FR.FILE_TYPE = 'img') "); //  and (ROWNUM  <=1 or re.label = 'Main jrxml')

	// query to retrieve JRXML from database for given file name
	public static final String GET_JASPER_REPORT_OBJECT_FOR_NAME = CustomStringUtil.appendString(
			"SELECT FR.DATA DATA, re.label LABEL FROM JIRESOURCEFOLDER RF, JIRESOURCE RE, JIFILERESOURCE FR" ,
	" WHERE re.label = ? AND RF.ID = RE.PARENT_FOLDER AND RE.ID = FR.ID and FR.FILE_TYPE = 'jrxml' and ROWNUM  <=1 ");


	// query to retrieve input control details of a report
	public static final String GET_INPUT_CONTROL_DETAILS = CustomStringUtil.appendString(
			"SELECT RE.LABEL LBL, RUIC.INPUT_CONTROL_ID CONTROLID, " ,
			"RE.NAME LABELID, RUIC.CONTROL_INDEX SEQ, IC.TYPE TYPE, IC.DATA_TYPE DATATYPE, IC.READONLY READ_ONLY, IC.VISIBLE VISIBLE, " ,
			"IC.MANDATORY MANDATORY, IC.QUERY_VALUE_COLUMN QUERY_VALUE_COLUMN, (SELECT DATASOURCE FROM JIQUERY WHERE ID=IC.LIST_QUERY) DATASOURCE, " ,
			"(SELECT SQL_QUERY FROM JIQUERY WHERE ID=IC.LIST_QUERY) SQL_QUERY, F_GET_LIST_OF_VALUES(IC.LIST_OF_VALUES) LIST_OF_VALUES, " ,
			"DT.type FIELD_TYPE, UTL_RAW.CAST_TO_VARCHAR2(substr(DT.minvalue, 15)) MIN_LENGTH, UTL_RAW.CAST_TO_VARCHAR2(substr(DT.max_value, 15)) MAX_LENGTH ",
			"FROM JIREPORTUNITINPUTCONTROL RUIC, JIRESOURCE RE, JIINPUTCONTROL IC, jidatatype DT " ,
			"WHERE REPORT_UNIT_ID = (SELECT RU.ID FROM JIREPORTUNIT RU, JIRESOURCE R, JIRESOURCEFOLDER RF " ,
			"WHERE RU.MAINREPORT = R.ID AND R.PARENT_FOLDER = RF.ID AND RF.URI = ? ) and DT.id(+) = IC.DATA_TYPE " ,
	"AND RE.ID = RUIC.INPUT_CONTROL_ID AND RUIC.INPUT_CONTROL_ID = IC.ID ORDER BY SEQ");

	public static final String GET_ALL_INPUT_CONTROLS = CustomStringUtil.appendString(
			"SELECT distinct RE.NAME LABELID ",
			"FROM JIREPORTUNITINPUTCONTROL RUIC, JIRESOURCE RE, JIINPUTCONTROL IC ",
	"WHERE RE.ID = RUIC.INPUT_CONTROL_ID AND RUIC.INPUT_CONTROL_ID = IC.ID");

	// main query for manage reports screen. It returns all report details
	/*public static final String GET_ALL_REPORT_LIST = CustomStringUtil.appendString(
           " SELECT ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME, ",
			" REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID, STATUS, ",
			" ROLES, LISTAGG(ORG_LABEL, ',') WITHIN  ",
			" GROUP( ORDER BY ORG_LABEL) AS ORG_NODE_LEVEL, MENUID, MENUNAME ",
			" FROM (SELECT  ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID, ",
			" REPORT_NAME,REPORT_FOLDER_URI,STATUS,ORG_LABEL,LISTAGG(ROLE_NAME, ',') WITHIN ",
			" GROUP( ORDER BY ROLE_NAME) AS ROLES,PRODUCT_NAME,PRODUCTID, MENUID, MENUNAME ",
			" FROM ( ",
			" SELECT DISTINCT RE.DB_REPORTID ID, RE.REPORT_DESC, ",
			" RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME, ",
			" RE.REPORT_FOLDER_URI,RE.ACTIVATION_STATUS STATUS,ROLE_NAME,ORG_LABEL, ",
			" P.PRODUCT_NAME, P.PRODUCTID, DMRA.DB_MENUID MENUID, DMENU.menu_name MENUNAME ",
			" FROM DASH_REPORTS RE ",
			" JOIN DASH_MENU_RPT_ACCESS DMRA ON RE.DB_REPORTID = DMRA.DB_REPORTID ",
			" JOIN dash_menus DMENU ON dmenu.db_menuid = DMRA.db_menuid ",
			" JOIN ROLE R ON R.ROLEID = DMRA.ROLEID ",
			" JOIN ORG_TP_STRUCTURE OTS ON OTS.ORG_LEVEL = DMRA.ORG_LEVEL ",
			" JOIN CUST_PRODUCT_LINK CPL ON CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID ",
			" JOIN PRODUCT P ON P.PRODUCTID = CPL.PRODUCTID ) ",
			" GROUP BY ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME, ",
			" REPORT_FOLDER_URI,STATUS,PRODUCT_NAME,PRODUCTID,ORG_LABEL,MENUID, MENUNAME )  ",
			" GROUP BY   ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME, ",
			" REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID,STATUS,ROLES, MENUID,MENUNAME");*/

	public static final String GET_ALL_REPORT_LIST = CustomStringUtil.appendString(
			" SELECT ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME, ",
			" REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID, STATUS, ",
			" ROLES, LISTAGG(ORG_LABEL, ',') WITHIN  ",
			" GROUP( ORDER BY ORG_LABEL) AS ORG_NODE_LEVEL, MENUID, MENUNAME ",
			" FROM (SELECT  ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID, ",
			" REPORT_NAME,REPORT_FOLDER_URI,STATUS,ORG_LABEL,LISTAGG(ROLE_NAME, ',') WITHIN ",
			" GROUP( ORDER BY ROLE_NAME) AS ROLES,PRODUCT_NAME,PRODUCTID, MENUID, MENUNAME ",
			" FROM ( ",
			" SELECT DISTINCT RE.DB_REPORTID ID, RE.REPORT_DESC, ",
			" RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME, ",
			" RE.REPORT_FOLDER_URI,RE.ACTIVATION_STATUS STATUS,ROLE_NAME,ORG_LABEL, ",
			" P.PRODUCT_NAME, P.PRODUCTID, DMRA.DB_MENUID MENUID, DMENU.menu_name MENUNAME ",
			" FROM DASH_REPORTS RE ",
			" JOIN DASH_MENU_RPT_ACCESS DMRA ON RE.DB_REPORTID = DMRA.DB_REPORTID ",
			" JOIN dash_menus DMENU ON dmenu.db_menuid = DMRA.db_menuid ",
			" JOIN ROLE R ON R.ROLEID = DMRA.ROLEID ",
			" JOIN ORG_TP_STRUCTURE OTS ON OTS.ORG_LEVEL = DMRA.ORG_LEVEL ",
			" JOIN CUST_PRODUCT_LINK CPL ON CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID and CPL.CUSTOMERID=?",
			" JOIN PRODUCT P ON P.PRODUCTID = CPL.PRODUCTID" ,
			" UNION ",
			" SELECT DISTINCT RE.DB_REPORTID ID, RE.REPORT_DESC,",
			" RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME,",
			" RE.REPORT_FOLDER_URI,RE.ACTIVATION_STATUS STATUS,ROLE_NAME,'Education Center' ORG_LABEL,",
			" P.PRODUCT_NAME, P.PRODUCTID,DMRA.DB_MENUID MENUID, DMENU.menu_name MENUNAME",
			" FROM DASH_REPORTS RE ",
			" JOIN DASH_MENU_RPT_ACCESS DMRA ON RE.DB_REPORTID = DMRA.DB_REPORTID",
			" JOIN dash_menus DMENU ON dmenu.db_menuid = DMRA.db_menuid  JOIN ROLE R ON R.ROLEID = DMRA.ROLEID",
			" JOIN CUST_PRODUCT_LINK CPL ON CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID and CPL.CUSTOMERID=?" +
			" JOIN PRODUCT P ON P.PRODUCTID = CPL.PRODUCTID",
			"  WHERE DMRA.ORG_LEVEL = ?)",
			" GROUP BY ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME, ",
			" REPORT_FOLDER_URI,STATUS,PRODUCT_NAME,PRODUCTID,ORG_LABEL,MENUID, MENUNAME )  ",
			" GROUP BY   ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME, ",
	" REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID,STATUS,ROLES, MENUID,MENUNAME");

	// retrieve a report based on the report id
	/*public static final String GET_DASHBOARD_DETAILS = CustomStringUtil.appendString("select distinct RE.DB_REPORTID ID,RE.REPORT_DESC,RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME," +
			 " RE.REPORT_FOLDER_URI, RE.ACTIVATION_STATUS STATUS,SF_GET_LIST_OF_ROLES(?) ROLES,sf_get_list_of_level(?) ORG_NODE_LEVEL " +
	 " from DASH_REPORTS RE join Dash_Menu_Rpt_Access DMRA on RE.DB_REPORTID=DMRA.DB_REPORTID AND RE.DB_REPORTID=?");*/


	/*public static final String GET_DASHBOARD_DETAILS = CustomStringUtil.appendString(
        		   " SELECT ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME,",
                   " REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID, STATUS,",
                   " ROLES, LISTAGG(ORG_LABEL, ',') WITHIN ",
                   " GROUP( ORDER BY ORG_LABEL) AS ORG_NODE_LEVEL ,MENUID, MENUNAME ",
                   " FROM (SELECT  ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,",
                   " REPORT_NAME,REPORT_FOLDER_URI,STATUS,ORG_LABEL,LISTAGG(ROLE_NAME, ',') WITHIN",
                   " GROUP( ORDER BY ROLE_NAME) AS ROLES,PRODUCT_NAME,PRODUCTID , MENUID, MENUNAME",
                   " FROM (",
                   " SELECT DISTINCT RE.DB_REPORTID ID, RE.REPORT_DESC,",
                   " RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME,",
                   " RE.REPORT_FOLDER_URI,RE.ACTIVATION_STATUS STATUS,ROLE_NAME,ORG_LABEL,",
                   " P.PRODUCT_NAME, P.PRODUCTID , DMRA.DB_MENUID MENUID, DMENU.menu_name MENUNAME ",
                   " FROM DASH_REPORTS RE",
                   " JOIN DASH_MENU_RPT_ACCESS DMRA ON RE.DB_REPORTID = DMRA.DB_REPORTID",
                   " JOIN dash_menus DMENU ON dmenu.db_menuid = DMRA.db_menuid ",
                   " JOIN ROLE R ON R.ROLEID = DMRA.ROLEID",
                   " JOIN ORG_TP_STRUCTURE OTS ON OTS.ORG_LEVEL = DMRA.ORG_LEVEL",
                   " JOIN CUST_PRODUCT_LINK CPL ON CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID",
                   " JOIN PRODUCT P ON P.PRODUCTID = CPL.PRODUCTID " ,
                   " where RE.DB_REPORTID = ?)",
                   " GROUP BY ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME,",
                   " REPORT_FOLDER_URI,STATUS,PRODUCT_NAME,PRODUCTID,ORG_LABEL,MENUID, MENUNAME  ) ",
                   " GROUP BY   ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME,",
                   " REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID,STATUS,ROLES, MENUID,MENUNAME");*/


	public static final String GET_DASHBOARD_DETAILS = CustomStringUtil.appendString(
			" SELECT ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME,",
			" REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID, STATUS,",
			" ROLES, LISTAGG(ORG_LABEL, ',') WITHIN ",
			" GROUP( ORDER BY ORG_LABEL) AS ORG_NODE_LEVEL ,MENUID, MENUNAME ",
			" FROM (SELECT  ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,",
			" REPORT_NAME,REPORT_FOLDER_URI,STATUS,ORG_LABEL,LISTAGG(ROLE_NAME, ',') WITHIN",
			" GROUP( ORDER BY ROLE_NAME) AS ROLES,PRODUCT_NAME,PRODUCTID , MENUID, MENUNAME",
			" FROM (",
			" SELECT DISTINCT RE.DB_REPORTID ID, RE.REPORT_DESC,",
			" RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME,",
			" RE.REPORT_FOLDER_URI,RE.ACTIVATION_STATUS STATUS,ROLE_NAME,ORG_LABEL,",
			" P.PRODUCT_NAME, P.PRODUCTID , DMRA.DB_MENUID MENUID, DMENU.menu_name MENUNAME ",
			" FROM DASH_REPORTS RE",
			" JOIN DASH_MENU_RPT_ACCESS DMRA ON RE.DB_REPORTID = DMRA.DB_REPORTID",
			" JOIN dash_menus DMENU ON dmenu.db_menuid = DMRA.db_menuid ",
			" JOIN ROLE R ON R.ROLEID = DMRA.ROLEID",
			" JOIN ORG_TP_STRUCTURE OTS ON OTS.ORG_LEVEL = DMRA.ORG_LEVEL",
			" JOIN CUST_PRODUCT_LINK CPL ON CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID and CPL.CUSTOMERID=?",
			" JOIN PRODUCT P ON P.PRODUCTID = CPL.PRODUCTID " ,
			" WHERE RE.DB_REPORTID = ?" ,
			" UNION" ,
			" SELECT DISTINCT RE.DB_REPORTID ID, RE.REPORT_DESC,",
			" RE.REPORT_TYPE,DMRA.CUST_PROD_ID,RE.REPORT_NAME,",
			" RE.REPORT_FOLDER_URI,RE.ACTIVATION_STATUS STATUS,ROLE_NAME,'Education Center' ORG_LABEL,",
			" P.PRODUCT_NAME, P.PRODUCTID , DMRA.DB_MENUID MENUID, DMENU.menu_name MENUNAME ",
			" FROM DASH_REPORTS RE",
			" JOIN DASH_MENU_RPT_ACCESS DMRA ON RE.DB_REPORTID = DMRA.DB_REPORTID",
			" JOIN dash_menus DMENU ON dmenu.db_menuid = DMRA.db_menuid ",
			" JOIN ROLE R ON R.ROLEID = DMRA.ROLEID",
			" JOIN CUST_PRODUCT_LINK CPL ON CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID and CPL.CUSTOMERID=?",
			" JOIN PRODUCT P ON P.PRODUCTID = CPL.PRODUCTID " ,
			" WHERE RE.DB_REPORTID = ?  AND DMRA.ORG_LEVEL = ?)",
			" GROUP BY ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME,",
			" REPORT_FOLDER_URI,STATUS,PRODUCT_NAME,PRODUCTID,ORG_LABEL,MENUID, MENUNAME  ) ",
			" GROUP BY   ID,REPORT_DESC,REPORT_TYPE,CUST_PROD_ID,REPORT_NAME,",
	" REPORT_FOLDER_URI,PRODUCT_NAME,PRODUCTID,STATUS,ROLES, MENUID,MENUNAME");

	// query to group download files
	public static final String GET_GROUP_DOWNLOAD_LIST = CustomStringUtil.appendString(
	"select * from job_tracking where job_status != 'DL' and userid=? order by created_date_time desc");

	// query to group download request Details
	public static final String GET_GROUP_DOWNLOAD_REQUEST_VIEW = CustomStringUtil.appendString(
	"select * from job_tracking where job_Id=?");
	// query to update report table
	public static final String UPDATE_REPORT = CustomStringUtil.appendString(
			"UPDATE DASHBOARD_REPORTS SET REPORT_NAME = ?, REPORT_FOLDER_URI  = ?, " ,
	"ACTIVATION_STATUS = ? WHERE DB_REPORTID = ?");

	// query to update report table
	public static final String UPDATE_REPORT_NEW = CustomStringUtil.appendString(
			"UPDATE DASH_REPORTS SET REPORT_NAME = ?,REPORT_DESC = ?, REPORT_FOLDER_URI  = ?, " ,
	"ACTIVATION_STATUS = ?, REPORT_TYPE=?,updated_date_time = SYSDATE WHERE DB_REPORTID = ?");


	// query to delete from report and roles association table report_roles
	public static final String DELETE_REPORT_ROLE = "DELETE FROM DASH_MENU_RPT_ACCESS WHERE DB_REPORTID = ?";

	// query to delete the report from the  reports table
	public static final String DELETE_REPORT = "DELETE  FROM DASH_REPORTS WHERE DB_REPORTID = ?";

	// query to insert report role relation in report_role table
	public static final String INSERT_REPORT_ROLE =  CustomStringUtil.appendString(
			" INSERT INTO DASH_MENU_RPT_ACCESS ",
			" (DB_MENUID,DB_REPORTID,ROLEID,ORG_LEVEL,CUST_PROD_ID,REPORT_SEQ,",
			" ACTIVATION_STATUS, ",
			" CREATED_DATE_TIME, ",
			" UPDATED_DATE_TIME) ",
			" VALUES ",
	" (?,?,(SELECT ROLEID FROM ROLE WHERE ROLE_NAME = ? ),?,?,?,?, SYSDATE, SYSDATE) " );
	// query to add report
	public static final String INSERT_REPORT =CustomStringUtil.appendString(
			"INSERT INTO DASH_REPORTS",
			"(db_reportid, report_name, report_desc, report_type, report_folder_uri,activation_status, created_date_time, updated_date_time)",
			"VALUES",
	"(?, ?, ?, ?,?, ?, SYSDATE, SYSDATE)" );

	// query to retrieve assessment details( including report details )
	public static final String GET_ALL_ASSESSMENT_LIST = CustomStringUtil.appendString(
			/*"SELECT DISTINCT ASS.ASSESSMENTID ASSESSMENT_ID,",
			" ASS.ASSESSMENT_NAME ASSESSMENT_NAME,RE.DB_REPORTID REPORT_ID,",
			" REPORT_NAME, REPORT_FOLDER_URI, RE.ACTIVATION_STATUS STATUS, SF_GET_LIST_OF_ROLES(RE.DB_REPORTID) ROLES, REPORT_TYPE TYPE ",
			" FROM DASH_REPORTS RE, ASSESSMENT_DIM ASS, CUST_PRODUCT_LINK LINK ",
			" WHERE RE.REPORT_TYPE LIKE (?) ",
			" AND RE.ACTIVATION_STATUS = '" +IApplicationConstants.ACTIVE_FLAG + 
			"' ORDER BY ASS.ASSESSMENTID,RE.REPORT_NAME DESC");*/
			"SELECT DISTINCT ASS.DB_MENUID MENU_ID, ",
			" ASS.MENU_NAME MENU_NAME,RE.DB_REPORTID REPORT_ID, ",
			" REPORT_NAME, REPORT_FOLDER_URI, RE.ACTIVATION_STATUS STATUS, SF_GET_LIST_OF_ROLES(RE.DB_REPORTID) ROLES, ", 
			" REPORT_TYPE TYPE, ASS.MENU_SEQ, acc.report_seq, acc.org_level ORGLEVEL ",
			" FROM DASH_REPORTS RE, dash_menus ASS, dash_menu_rpt_access acc, CUST_PRODUCT_LINK LINK ", 
			" WHERE RE.REPORT_TYPE LIKE (?) ",
			" and ass.db_menuid = acc.db_menuid ",
			" and acc.db_reportid = re.db_reportid ",
			" AND RE.ACTIVATION_STATUS IN ('AC','SS') ",
	" ORDER BY ASS.MENU_SEQ, acc.report_seq, RE.REPORT_NAME DESC");



	public static final String REPORT_TYPE = CustomStringUtil.appendString("select REPORT_TYPE as TYPE, DB_REPORTID as ID, " +
	" REPORT_FOLDER_URI As URI from dash_reports WHERE (REPORT_FOLDER_URI = ? OR DB_REPORTID = ?) ");

	public static final String DELETE_GROUP_FILES = CustomStringUtil.appendString("update job_tracking set job_status='DL',updated_date_time=SYSDATE where job_id=?");
	public static final String GET_DELETE_SCHEDULED_GROUP_DOWNLOAD_LIST = CustomStringUtil.appendString("select request_filename,job_id from job_tracking where job_status!='DL' and SYSDATE>=created_date_time+?");

	public static final String GET_PROCESS_SEQ = "SELECT SEQ_ISTEP_PROCESS_ID.NEXTVAL STAGING_SEQ FROM DUAL";
	public static final String INSERT_JOB_TRACKING = CustomStringUtil.appendString(
					"INSERT INTO JOB_TRACKING",
					" (JOB_ID, USERID,",
					" JOB_NAME,",
					" EXTRACT_STARTDATE, EXTRACT_ENDDATE,",
					" EXTRACT_CATEGORY, EXTRACT_FILETYPE, REQUEST_TYPE, REQUEST_SUMMARY,",
					" REQUEST_DETAILS,",
					" REQUEST_FILENAME, REQUEST_EMAIL, JOB_LOG, JOB_STATUS,",
					" ADMINID, CUSTOMERID,",
					" CREATED_DATE_TIME, UPDATED_DATE_TIME,",
					" OTHER_REQUEST_PARAMS, FILE_SIZE) VALUES ",
					"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
}

