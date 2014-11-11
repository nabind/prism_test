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
	" WHERE RF.URI LIKE ? AND RF.ID = RE.PARENT_FOLDER AND RE.ID = FR.ID and (FR.FILE_TYPE = 'jrxml' OR FR.FILE_TYPE = 'img') ",
	" ORDER BY RE.LABEL "); //  and (ROWNUM  <=1 or re.label = 'Main jrxml') //Added order by in order to maintain subreport order in the report

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

	/**
	 * @author Joy
	 * It returns all report details for manage reports screen and
	 * retrieve a report based on the report id
	 * */ 
	public static final String GET_DASHBOARD_DETAILS = "PKG_MANAGE_REPORT.SP_GET_REPORT_LIST(?,?,?,?)";
	
	/**
	 * @author Joy
	 * Add a new report
	 * */
	public static final String ADD_REPORT = "PKG_MANAGE_REPORT.SP_ADD_REPORT(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * @author Joy
	 * Edit a report
	 * */
	public static final String EDIT_REPORT = "PKG_MANAGE_REPORT.SP_EDIT_REPORT(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * @author Joy
	 * Delete a report
	 * */
	public static final String DELETE_REPORT = "PKG_MANAGE_REPORT.SP_DELETE_REPORT(?,?,?,?)";

	// query to group download files
	public static final String GET_GROUP_DOWNLOAD_LIST = CustomStringUtil.appendString(
		"SELECT JT.JOB_ID, JT.REQUEST_TYPE, JT.REQUEST_DETAILS, JT.REQUEST_SUMMARY, JT.EXTRACT_CATEGORY,",
		" JT.JOB_NAME, DCP.DB_PROPERY_VALUE || JT.REQUEST_FILENAME REQUEST_FILENAME, JT.UPDATED_DATE_TIME,",
		" JT.CREATED_DATE_TIME, JT.FILE_SIZE",
		" FROM JOB_TRACKING JT, DASH_CONTRACT_PROP DCP",
		" WHERE JT.JOB_STATUS != 'DL' AND JT.USERID = ?",
		" AND DCP.DB_PROPERTY_NAME = 'static.pdf.location'",
		" ORDER BY CREATED_DATE_TIME DESC"
	);

	// query to group download request Details
	public static final String GET_GROUP_DOWNLOAD_REQUEST_VIEW = CustomStringUtil.appendString(
	"select * from job_tracking where job_Id=?");
	
	public static final String GET_ALL_ASSESSMENT_LIST = "{CALL PKG_MENU_ACCESS.GET_ALL_ASSESSMENT_LIST(?, ?, ?, ?, ?)}";
	public static final String GET_GROWTH_ASSESSMENT_LIST = "{CALL PKG_MENU_ACCESS.GET_GROWTH_ASSESSMENT_LIST(?, ?, ?, ?, ?)}";
	public static final String GET_EDU_ASSESSMENT_LIST = "{CALL PKG_MENU_ACCESS.GET_EDU_ASSESSMENT_LIST(?, ?, ?, ?, ?)}";
	public static final String GET_ALL_BUT_GROWTH_ASSESSMENT_LIST = "{CALL PKG_MENU_ACCESS.GET_ALL_BUT_GRW_ASS_LIST(?, ?, ?, ?, ?)}";

	public static final String GET_LIST_OF_ROLES = CustomStringUtil.appendString(
			"SELECT SF_GET_LIST_OF_ROLES(?) ROLES from DUAL"); 

	public static final String REPORT_TYPE = CustomStringUtil.appendString("select REPORT_TYPE as TYPE, DB_REPORTID as ID, " +
	" REPORT_FOLDER_URI As URI from dash_reports WHERE (REPORT_FOLDER_URI = ? OR DB_REPORTID = ?) ");

	public static final String DELETE_GROUP_FILES = CustomStringUtil.appendString("update job_tracking set job_status='DL',updated_date_time=SYSDATE where job_id=?");
	public static final String DELETE_SCHEDULED_GROUP_FILES = CustomStringUtil.appendString("update job_tracking set job_status='DL',updated_date_time=SYSDATE,job_log=job_log || ? where job_id=?");
	public static final String GET_DELETE_SCHEDULED_GROUP_DOWNLOAD_LIST = CustomStringUtil.appendString("select request_filename,job_id from job_tracking where job_status!='DL' and SYSDATE>=created_date_time+?");

	public static final String GET_PROCESS_SEQ = "SELECT SEQ_ISTEP_PROCESS_ID.NEXTVAL STAGING_SEQ FROM DUAL";
	public static final String INSERT_JOB_TRACKING = CustomStringUtil.appendString(
					"INSERT INTO JOB_TRACKING",
					" (JOB_ID, USERID,",
					" JOB_NAME, EXTRACT_STARTDATE, EXTRACT_CATEGORY, EXTRACT_FILETYPE, REQUEST_TYPE, REQUEST_SUMMARY,",
					" REQUEST_DETAILS,",
					" REQUEST_FILENAME, REQUEST_EMAIL, JOB_LOG, JOB_STATUS,",
					" ADMINID, CUSTOMERID, CREATED_DATE_TIME) VALUES ",
					"(?, ?, ?, SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT ADMINID FROM CUST_PRODUCT_LINK WHERE CUSTOMERID=? AND PRODUCTID=? AND ROWNUM=1), ?, SYSDATE)");
	public static final String INSERT_JOB_TRACKING_DATE = CustomStringUtil.appendString(
			" insert into JOB_TRACKING(job_id,userid,",
			" job_name,extract_startdate,extract_enddate,extract_category,extract_filetype,request_type,",
			" request_summary,request_details,request_filename, request_email,job_log,job_status,adminid,",
			" customerid,created_date_time,updated_date_time, OTHER_REQUEST_PARAMS) ",
			" VALUES (?,?,?,TO_DATE(?,'MM/DD/YYYY'),TO_DATE(?,'MM/DD/YYYY'),?,?,?,?,?,?,?,?,?, ",
			"(select adminid from admin_dim Where is_current_admin = 'Y'),?,SYSDATE,SYSDATE,?)");
	public static final String INSERT_JOB_TRACKING_DEFAULT_DATE = CustomStringUtil.appendString(
			" insert into JOB_TRACKING(job_id,userid,",
			" job_name,extract_startdate,extract_enddate,extract_category,extract_filetype,request_type,",
			" request_summary,request_details,request_filename, request_email,job_log,job_status,adminid,",
			" customerid,created_date_time,updated_date_time, OTHER_REQUEST_PARAMS) ",
			" VALUES (?,?,?,SYSDATE,SYSDATE+10,?,?,?,?,?,?,?,?,?, ",
			"(select adminid from admin_dim Where is_current_admin = 'Y'),?,SYSDATE,SYSDATE,?)");
	
	public static final String UPDATE_JOB_TRACKING = "UPDATE JOB_TRACKING SET EXTRACT_ENDDATE=SYSDATE+#, REQUEST_FILENAME=?, REQUEST_SUMMARY=?, JOB_LOG=?, JOB_STATUS=?, UPDATED_DATE_TIME=SYSDATE, FILE_SIZE=? WHERE JOB_ID=?";
	
	public static final String UPDATE_FILENAME_JOB_TRACKING="UPDATE JOB_TRACKING SET REQUEST_FILENAME=?,UPDATED_DATE_TIME=SYSDATE WHERE JOB_ID=?";	
	
	public static final String GET_FILENAME_GD = CustomStringUtil.appendString(
			" SELECT DISTINCT 'IN'||",
			" FACT.TEST_DATE||'.'||",
			" FACT.ISTEP_TEST_NAME||'.'||",
			" FACT.ORGTSTGPGM||'.'||",
			" DISTRICT.ORG_NODE_NAME||'.'||",
			" DISTRICT.ORG_NODEID ||'.'||",
			" SCHOOL.ORG_NODE_NAME ||'.'||",
			" SCHOOL.ORG_NODEID ||'.'||",
			" FACT.GRADEID",
			" /*STUDENTELEMENTNO ||'.'||",
			" RESORTID */ FILE_NAME",
			" FROM ORG_NODE_DIM DISTRICT,",
			" ORG_NODE_DIM SCHOOL,",
			" ORG_LSTNODE_LINK LINK1,",
			" ORG_LSTNODE_LINK LINK2,",
			" RESULTS_GRT_FACT FACT",
			" WHERE FACT.ORG_NODEID IN (SELECT DISTINCT ORG_NODEID FROM ORG_NODE_DIM WHERE PARENT_ORG_NODEID=?)",
			" AND FACT.ORG_NODEID = LINK2.ORG_LSTNODEID",
			" AND LINK2.ORG_NODEID = DISTRICT.ORG_NODEID",
			" AND DISTRICT.ORG_NODE_LEVEL = 2",
			" AND DISTRICT.ORG_NODEID = SCHOOL.PARENT_ORG_NODEID",
			" AND SCHOOL.ORG_NODEID = LINK1.ORG_NODEID",
			" AND LINK1.ORG_LSTNODEID = FACT.ORG_NODEID",
			" AND SCHOOL.ORG_NODE_LEVEL = 3",
			" AND ROWNUM=1"
			);

	public static final String GET_REQUEST_SUMMARY = CustomStringUtil.appendString(
			"SELECT 'EXTRACT_STARTDATE' KEY, TO_CHAR(EXTRACT_STARTDATE, 'DD-MON-YYYY HH24:MI:SS') VALUE FROM JOB_TRACKING WHERE JOB_ID = ?",
			" UNION ALL",
			" SELECT 'PRODUCT_NAME' KEY, PRODUCT_NAME VALUE FROM PRODUCT WHERE PRODUCTID = ?",
			" UNION ALL",
			" SELECT 'GRADE_NAME' KEY, GRADE_NAME VALUE FROM GRADE_DIM WHERE GRADEID = ?",
			" UNION ALL",
			" SELECT TO_CHAR(ORG_NODEID) KEY, ORG_NODE_NAME VALUE FROM ORG_NODE_DIM WHERE ORG_NODEID IN (~)"
			);
	public static final String GET_STUDENT_SELECTION_STATISTICS = CustomStringUtil.appendString(
			"SELECT COUNT(DISTINCT SCHOOL.ORG_NODE_NAME) SCHOOLS,",
			" COUNT(DISTINCT CLASS.ORG_NODE_NAME) CLASSES,",
			" COUNT(SBD.STUDENT_BIO_ID) STUDENTS",
			" FROM STUDENT_BIO_DIM SBD, ORG_NODE_DIM CLASS, ORG_NODE_DIM SCHOOL",
			" WHERE SBD.ORG_NODEID = CLASS.ORG_NODEID",
			" AND CLASS.PARENT_ORG_NODEID = SCHOOL.ORG_NODEID",
			" AND CLASS.ORG_NODE_LEVEL = 4",
			" AND SCHOOL.ORG_NODE_LEVEL = 3",
			"AND SBD.STUDENT_BIO_ID IN (~)"
			);
}

