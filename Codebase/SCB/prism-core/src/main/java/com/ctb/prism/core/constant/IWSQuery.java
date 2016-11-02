package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the web service
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IWSQuery {
	
	public static final String GET_STAGING_SEQ = "SELECT NEXT VALUE FOR SEQ_TASC_PROCESS_ID STAGING_SEQ";
	
	public static final String GET_PARTITION = "SELECT TOP(1) WKF_PARTITION_NAME FROM JOB_PARTITION_STATUS WHERE STATUS = 'IN'";
	
	public static final String UPDATE_PARTITION = "UPDATE JOB_PARTITION_STATUS SET STATUS = ? WHERE STATUS = ? AND WKF_PARTITION_NAME = ?";
	
	public static final String GET_ROSTER = "SELECT TOP(1) rosterid FROM OAS_WS_ROSTER WHERE rosterid = ?";
	
	public static final String CREATE_ROSTER = CustomStringUtil.appendString(
			"  insert into OAS_WS_ROSTER (rosterid, datetimestamp) values (?, SYSDATETIME()) ") ;
	
	public static final String REMOVE_ROSTER = CustomStringUtil.appendString(
			" delete from OAS_WS_ROSTER where rosterid = ? ") ;
	
	public static final String CREATE_PROCESS_STATUS = CustomStringUtil.appendString(
		" insert into stg_process_status (process_id, source_system, hier_validation, bio_validation, demo_validation,",
		" content_validation, objective_validation, item_validation, wkf_partition_name, datetimestamp)  ",
		" values (?, 'OL', 'AC', 'AC', 'AC', 'AC', 'AC', 'AC', ?, SYSDATETIME())") ;
	
	public static final String CREATE_HIER_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_HIER_DETAILS ( ",
			" STG_HIERARCHY_DETAILS_ID, ",
			" ORG_NAME, ",
			" ORG_CODE, ",
			" ORG_TYPE, ",
			" ORG_LEVEL,",
			" MAX_HIERARCHY, ",
			" STRC_ELEMENT, ",
			" SPECIAL_CODES, ",
			" ORG_MODE, ",
			" PARENT_ORG_CODE, ",
			" ORG_NODE_CODE_PATH, ",
			" ORGTP, ",
			" CUSTOMER_ID, ",
			" PRODUCT_ID, ",
			" PROCESS_ID, ",
			" WKF_PARTITION_NAME, ",
			" ADMINID, ",
			" DATETIMESTAMP, ",
			" ORG_NODE_ID ) ",
			" SELECT ?,?,?,?,?,?,?,?,?,?,?,?, ",
			" (SELECT CUSTOMERID FROM TEST_PROGRAM WHERE TP_CODE = ?), ",
			" (SELECT PRODUCTID FROM PRODUCT WHERE PRODUCT_NAME = ?),?,?, ",
			" (SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y'),",
			" SYSDATETIME(),?"
		);
	
	public static final String CREATE_LSTNODE_HIER_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_LSTNODE_HIER_DETAILS ( ",
			" STU_LSTNODE_HIER_ID, ",
			" ORG_NAME, ",
			" ORG_CODE, ",
			" ORG_TYPE, ",
			" ORG_LEVEL, ",
			" ORG_NODE_ID, ",
			" ORG_NODE_PATH, ",
			" WKF_PARTITION_NAME,",
			" DATETIMESTAMP ) ",
			" SELECT  (SELECT TOP(1) STG_HIERARCHY_DETAILS_ID FROM STG_HIER_DETAILS ",
			" WHERE PROCESS_ID = ? AND ORG_LEVEL = ?),?,?,?,?,?,",
			" (SELECT TOP(1) ORG_NODE_CODE_PATH FROM STG_HIER_DETAILS WHERE PROCESS_ID = ? AND ORG_LEVEL = ?), ?, SYSDATETIME()"
		);
	
	public static final String CREATE_STG_BIO_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_BIO_DETAILS ( ",
			" STUDENT_BIO_DETAILS_ID, ",
			" FIRST_NAME, ",
			" MIDDLE_NAME, ",
			" LAST_NAME, ",
			" BIRTHDATE, ",
			" GENDER,  ",
			" GRADE,  ",
			" EDU_CENTER, ",
			" BARCODE, ",
			" SPECIAL_CODES, ",
			" STUDENT_MODE, ",
			" STRUC_ELEMENT, ",
			" TEST_ELEMENT_ID, ",
			" INT_STUDENT_ID, ",
			" EXT_STUDENT_ID, ",
			" LITHOCODE, ",
			" STU_LSTNODE_HIER_ID, ",
			" IS_BIO_UPDATE_CMPL, ",
			" PROCESS_ID, ",
			" NEED_PRISM_CONSUME, ",
			" WKF_PARTITION_NAME, ",
			" DATETIMESTAMP ) ",
			" SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ",
			" (SELECT TOP(1) STG_HIERARCHY_DETAILS_ID FROM STG_HIER_DETAILS WHERE PROCESS_ID = ? ",
			" AND ORG_LEVEL = ?),?,?,?,?, SYSDATETIME()"
		);
	
	public static final String CREATE_STG_DEMO_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_DEMO_DETAILS ( ",
			" DEMO_CODE, ",
			" DEMO_VALUE, ",
			" STUDENT_BIO_DETAILS_ID, ",
			" CONTENT_CODE, ",
			" NEED_PRISM_CONSUME, ",
			" PROCESS_ID, ",
			" WKF_PARTITION_NAME, ",
			" IS_BIO_UPDATE, ",
			" DATETIMESTAMP ) ", 
			" SELECT  ?,?,?,?,?,?,?,?, SYSDATETIME()"
		);
	
	public static final String CREATE_STG_SUBTEST_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_SUBTEST_DETAILS ( ",
			" STUDENT_BIO_DETAILS_ID, ",
			" STATUS_CODE, ",
			" CONTENT_NAME, ",
			" SCORING_STATUS, ",
			" TEST_FORM, ",
			" DATE_TEST_TAKEN, ",
			" PROCESS_ID, ",
			" NEED_PRISM_CONSUME, ",
			" WKF_PARTITION_NAME, ",
			" DATETIMESTAMP ) ",
			" SELECT  ?,?,?,?,?, TO_DATE(?, 'MMDDRRRR'),?,?,?,SYSDATETIME()"
			);
	
	public static final String UPDATE_STG_SUBTEST_DETAILS = CustomStringUtil.appendString(
			"UPDATE STG_STD_SUBTEST_DETAILS SET ~ = ?,",
			" DATETIMESTAMP =SYSDATETIME() ",
			" WHERE STUDENT_BIO_DETAILS_ID = ? ",
			" AND PROCESS_ID =? ",
			" AND WKF_PARTITION_NAME =? AND content_name = ?"
			);
		
	public static final String CREATE_STG_SUBTEST_DETAILS_SCORE = CustomStringUtil.appendString(
		"  INSERT INTO STG_STD_SUBTEST_DETAILS (STUDENT_BIO_DETAILS_ID, STATUS_CODE, CONTENT_NAME, SCORING_STATUS, ",
		" TEST_FORM, DATE_TEST_TAKEN,  AAGE, AANCE,AANP, AANS, AASS, ACSIP, ACSIS, ACSIN, CSI, CSIL, CSIU, DIFF, GE, ",
		" HSE, LEX, LEXL, LEXU, NCE, NCR, NP, NPA, NPG, NPL, NPH, NS, NSA, NSG, OM,  OMS, OP, OPM, PC, PL, PP, PR, SEM, ",
		" SNPC, SS, QTL, QTLL, QTLU, PROCESS_ID, NEED_PRISM_CONSUME, WKF_PARTITION_NAME, DATETIMESTAMP) ",
		" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATETIME())");
	
	public static final String CREATE_STG_OBJECTIVE_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_OBJECTIVE_DETAILS (",
			" STUDENT_BIO_DETAILS_ID,CONTENT_NAME,TEST_FORM, DATE_TEST_TAKEN, ",
			" OBJECTIVE_NAME,PROCESS_ID,WKF_PARTITION_NAME, DATETIMESTAMP ) VALUES ",
			" ( ?, ?, ?, CONVERT(datetime2(0), STUFF(STUFF(?,3,0,'-'),6,0,'-'), 110),?,?,?, SYSDATETIME())"
		);
	
	public static String UPDATE_STG_OBJECTIVE_DETAILS = CustomStringUtil.appendString(
			"UPDATE STG_STD_OBJECTIVE_DETAILS SET ~ = ?,",
			" DATETIMESTAMP  =SYSDATETIME() WHERE STUDENT_BIO_DETAILS_ID = ? ",
			" AND PROCESS_ID = ? AND WKF_PARTITION_NAME = ? AND content_name = ? AND OBJECTIVE_NAME = ? "
		);
	
	public static final String CREATE_STG_OBJECTIVE_DETAILS_SCORE = CustomStringUtil.appendString(
			"  INSERT INTO STG_STD_OBJECTIVE_DETAILS (STUDENT_BIO_DETAILS_ID, ",
			" CONTENT_NAME, TEST_FORM, DATE_TEST_TAKEN,  OBJECTIVE_NAME, NCR, ",
			" OS, OPI, OPIQ, OPIP, PC, PP, PL, SS, INRC, PROCESS_ID, WKF_PARTITION_NAME, DATETIMESTAMP) ",
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATETIME()) ");
	
	public static final String CREATE_STG_ITEM_RESPONSE_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_ITEM_RESPONSE_DETAILS ( STUDENT_BIO_DETAILS_ID, CONTENT_CODE, OBJECTIVE_CODE, ",
			" TEST_FORM, GRADE, ITEM_TYPE, ITEM_CODE, ITEM_NAME, READ_CODE, SCORE_VALUE, WKF_PARTITION_NAME, ",
			" PROCESS_ID,    DATETIMESTAMP ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATETIME() )"
		);
	
	public static final String GET_ORG_MAP = "SELECT DISTINCT m.ORG_NODEID, m.ORG_NODE_LEVEL, m.HIGHEST_ORG_NODE, m.PARENT_ORG_NODEID FROM ORGUSER_MAPPING m, org_node_dim o WHERE m.ORG_NODEID = o.ORG_NODEID and ORG_NODE_CODE IN (?)";
	
	public static final String CLEAN_STG_HIER_DETAILS = "DELETE FROM STG_HIER_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_LSTNODE_HIER_DETAILS = "DELETE FROM STG_LSTNODE_HIER_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_BIO_DETAILS = "DELETE FROM STG_STD_BIO_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_DEMO_DETAILS = "DELETE FROM STG_STD_DEMO_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_SUBTEST_DETAILS = "DELETE FROM STG_STD_SUBTEST_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_OBJECTIVE_DETAILS = "DELETE FROM STG_STD_OBJECTIVE_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_ITEM_RESPONSE_DETAILS = "DELETE FROM STG_ITEM_RESPONSE_DETAILS WHERE WKF_PARTITION_NAME = ?";

	public static final String GET_WS_ROSTER_DATA = "SELECT WS_ROSTER_DATA_ID,REQUEST_OBJECT FROM WS_ROSTER_DATA WHERE STATUS = ?";

	public static final String UPDATE_WS_ROSTER_DATA = "UPDATE WS_ROSTER_DATA SET STATUS = ?,RESPONSE_LOG = ?, UPDATED_TIME =SYSDATETIME() WHERE WS_ROSTER_DATA_ID = ?";
	
	public static final String GET_PROCESS_STATUS = CustomStringUtil.appendString(
			" select hier_validation, bio_validation, demo_validation, ",
			" content_validation, objective_validation, item_validation, process_log, er_validation ",
			" FROM stg_process_status where process_id = ? ");
}

