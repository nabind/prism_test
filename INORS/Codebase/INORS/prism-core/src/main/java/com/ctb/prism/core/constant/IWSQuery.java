package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the web service
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IWSQuery {
	
	public static final String GET_STAGING_SEQ = "SELECT SEQ_TASC_PROCESS_ID.NEXTVAL STAGING_SEQ FROM DUAL";
	
	public static final String GET_PARTITION = "SELECT WKF_PARTITION_NAME FROM JOB_PARTITION_STATUS WHERE STATUS = 'IN' AND ROWNUM = 1";
	
	public static final String UPDATE_PARTITION = "UPDATE JOB_PARTITION_STATUS SET STATUS = ? WHERE STATUS = ? AND WKF_PARTITION_NAME = ?";
	
	public static final String CREATE_PROCESS_STATUS = CustomStringUtil.appendString(
		" insert into stg_process_status (process_id, source_system, hier_validation, bio_validation, demo_validation, ",
		" content_validation, objective_validation, item_validation, wkf_partition_name, datetimestamp) ",
		" values (?, 'OL', 'AC', 'AC', 'AC', 'AC', 'AC', 'AC', ?, SYSDATE) ") ;
	
	public static final String CREATE_HIER_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_HIER_DETAILS (",
			"     STG_HIERARCHY_DETAILS_ID,", //1
			"     ORG_NAME,",                 //2
			"     ORG_CODE,",                 //3
			"     ORG_TYPE,",                 //4
			"     ORG_LEVEL,",                //5
			"     MAX_HIERARCHY,",            //6
			"     STRC_ELEMENT,",             //7
			"     SPECIAL_CODES,",            //8
			"     ORG_MODE,",                 //9
			"     PARENT_ORG_CODE,",          //10
			"     ORG_NODE_CODE_PATH,",       //11
			"     ORGTP,",                    //12
			"     CUSTOMER_ID,",              //13
			"     PRODUCT_ID,",               //14
			"     PROCESS_ID,",               //15
			"     WKF_PARTITION_NAME,",       //16
			"     ADMINID,",
			"     DATETIMESTAMP,",
			"     ORG_NODE_ID",               //17
			" ) VALUES (",
			"     ?,", //1
			"     ?,", //2
			"     ?,", //3
			"     ?,", //4
			"     ?,", //5
			"     ?,", //6
			"     ?,", //7
			"     ?,", //8
			"     ?,", //9
			"     ?,", //10
			"     ?,", //11
			"     ?,", //12
			"     (SELECT CUSTOMERID FROM TEST_PROGRAM WHERE TP_CODE = ?),", //13
			"     (SELECT PRODUCTID FROM PRODUCT WHERE PRODUCT_NAME = ?),",  //14
			"     ?,", //15
			"     ?,", //16
			"     (SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y'),",
			"     SYSDATE,",
			"     ?", //17
			" )"
		);
	
	public static final String CREATE_LSTNODE_HIER_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_LSTNODE_HIER_DETAILS (",
			"     STU_LSTNODE_HIER_ID,", // 1 & 2
			"     ORG_NAME,",// 3
			"     ORG_CODE,",// 4
			"     ORG_TYPE,",// 5
			"     ORG_LEVEL,",// 6
			"     ORG_NODE_ID,",// 7
			"     ORG_NODE_PATH,",// 8 & 9
			"     WKF_PARTITION_NAME,",// 10
			"     DATETIMESTAMP",// 11
			" ) VALUES (", 
			"	  (SELECT STG_HIERARCHY_DETAILS_ID FROM STG_HIER_DETAILS WHERE PROCESS_ID = ? AND ORG_LEVEL = ?), ", // 1 & 2
			"     ?,",// 3
			"     ?,",// 4
			"     ?,",// 5
			"     ?,",// 6
			"     ?,",// 7
			"     (SELECT ORG_NODE_CODE_PATH FROM STG_HIER_DETAILS WHERE PROCESS_ID = ? AND ORG_LEVEL = ?), ", // 8 & 9
			"     ?,",// 10
			"     SYSDATE",// 11
			" )"
		);
	
	public static final String CREATE_STG_BIO_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_BIO_DETAILS (",
			"     STUDENT_BIO_DETAILS_ID,", //1
			"     FIRST_NAME,", //2
			"     MIDDLE_NAME,", //3
			"     LAST_NAME,", //4
			"     BIRTHDATE,", //5
			"     GENDER,", //6
			"     GRADE,", //7
			"     EDU_CENTER,", //8
			"     BARCODE,", //9
			"     SPECIAL_CODES,", //10
			"     STUDENT_MODE,", //11
			"     STRUC_ELEMENT,", //12
			"     TEST_ELEMENT_ID,", //13
			"     INT_STUDENT_ID,", //14
			"     EXT_STUDENT_ID,", //15
			"     LITHOCODE,", //16
			"     STU_LSTNODE_HIER_ID,", //17
			"     IS_BIO_UPDATE_CMPL,", //18
			"     PROCESS_ID,", //19
			"     NEED_PRISM_CONSUME,", //20
			"     WKF_PARTITION_NAME,", //21
			"     DATETIMESTAMP", //22
			" ) VALUES (",
			"     ?,", //1
			"     ?,", //2
			"     ?,", //3
			"     ?,", //4
			"     ?,", //5
			"     ?,", //6
			"     ?,", //7
			"     ?,", //8
			"     ?,", //9
			"     ?,", //10
			"     ?,", //11
			"     ?,", //12
			"     ?,", //13
			"     ?,", //14
			"     ?,", //15
			"     ?,", //16
			"     (SELECT STG_HIERARCHY_DETAILS_ID FROM STG_HIER_DETAILS WHERE PROCESS_ID = ? AND ORG_LEVEL = ?), ", // 17 & 17.1
			"     ?,", //18
			"     ?,", //19
			"     ?,", //20
			"     ?,", //21
			"     SYSDATE", //22
			" )"
		);
	
	public static final String CREATE_STG_DEMO_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_DEMO_DETAILS (",
			"     DEMO_CODE,",	//1
			"     DEMO_VALUE,",	//2
			"     STUDENT_BIO_DETAILS_ID,",	//3
			"     CONTENT_CODE,",	//4
			"     NEED_PRISM_CONSUME,",	//5
			"     PROCESS_ID,",	//6
			"     WKF_PARTITION_NAME,",	//7
			"     IS_BIO_UPDATE,", // 8
			"     DATETIMESTAMP",	//9
			" ) VALUES (",
			"     ?,",	//1
			"     ?,",	//2
			"     ?,",	//3
			"     ?,",	//4
			"     ?,",	//5
			"     ?,",	//6
			"     ?,",	//7
			"     ?,",	//8
			"     SYSDATE",	//9
			" )"
		);
	
	public static final String CREATE_STG_SUBTEST_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_SUBTEST_DETAILS (",
			//"     SUBTEST_DETAILS_ID,",	//1
			"     STUDENT_BIO_DETAILS_ID,",	//2
			"     STATUS_CODE,",	//3
			"     CONTENT_NAME,",	//4
			"     SCORING_STATUS,",	//5
			"     TEST_FORM,",	//6
			"     DATE_TEST_TAKEN,",	//7
			"     PROCESS_ID,",	//8
			"     NEED_PRISM_CONSUME,",	//9
			"     WKF_PARTITION_NAME,",	//10
			"     DATETIMESTAMP",	//11
			" ) VALUES (",
			//"     ?,",	//1
			"     ?,",	//2
			"     ?,",	//3
			"     ?,",	//4
			"     ?,",	//5
			"     ?,",	//6
			"     TO_DATE(?, 'MMDDYYYY'),",	//7
			"     ?,",	//8
			"     ?,",	//9
			"     ?,",	//10
			"     SYSDATE",	//11
			" )"
			);
	
	public static final String UPDATE_STG_SUBTEST_DETAILS = CustomStringUtil.appendString(
			"UPDATE STG_STD_SUBTEST_DETAILS",
			" SET ~", // 1
			" = ?,",	// 2
			" DATETIMESTAMP              =SYSDATE",
			" WHERE STUDENT_BIO_DETAILS_ID = ?",	//3
			" AND PROCESS_ID               =?",	//4
			" AND WKF_PARTITION_NAME       =?"	//5
			);
		
	public static final String CREATE_STG_SUBTEST_DETAILS_SCORE = CustomStringUtil.appendString(
		" INSERT INTO STG_STD_SUBTEST_DETAILS (STUDENT_BIO_DETAILS_ID, STATUS_CODE, CONTENT_NAME, SCORING_STATUS, TEST_FORM, DATE_TEST_TAKEN, ",
		" AAGE, AANCE,AANP, AANS, AASS, ACSIP, ACSIS, ACSIN, CSI, CSIL, CSIU, DIFF, GE, HSE, LEX, LEXL, LEXU, NCE, NCR, NP, NPA, NPG, NPL, NPH, NS, NSA, NSG, OM, ",
		" OMS, OP, OPM, PC, PL, PP, PR, SEM, SNPC, SS, QTL, QTLL, QTLU, PROCESS_ID, NEED_PRISM_CONSUME, WKF_PARTITION_NAME, DATETIMESTAMP) ",
		" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE) ");
	
	public static final String CREATE_STG_OBJECTIVE_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_STD_OBJECTIVE_DETAILS (",
			"     STUDENT_BIO_DETAILS_ID,",	//1
			"     CONTENT_NAME,",	//2
			"     TEST_FORM,",	//3
			"     DATE_TEST_TAKEN,",	//4
			"     OBJECTIVE_NAME,",	//5
			"     PROCESS_ID,",	//6
			"     WKF_PARTITION_NAME,",	//7
			"     DATETIMESTAMP",	//8
			" ) VALUES (",
			"     ?,",	//1
			"     ?,",	//2
			"     ?,",	//3
			"     TO_DATE(?, 'MMDDYYYY'),",	//4
			"     ?,",	//5
			"     ?,",	//6
			"     ?,",	//7
			"     SYSDATE",	//8
			" )"
		);
	
	public static String UPDATE_STG_OBJECTIVE_DETAILS = CustomStringUtil.appendString(
			"UPDATE STG_STD_OBJECTIVE_DETAILS",
			" SET ~                        = ?,",
			" DATETIMESTAMP                =SYSDATE",
			" WHERE STUDENT_BIO_DETAILS_ID = ?",
			" AND PROCESS_ID               = ?",
			" AND WKF_PARTITION_NAME       = ?"
		);
	
	public static final String CREATE_STG_OBJECTIVE_DETAILS_SCORE = CustomStringUtil.appendString(
			" INSERT INTO STG_STD_OBJECTIVE_DETAILS (STUDENT_BIO_DETAILS_ID, CONTENT_NAME, TEST_FORM, DATE_TEST_TAKEN, ",
			" OBJECTIVE_NAME, NCR, OS, OPI, OPIQ, OPIP, PC, PP, PL, SS, INRC, PROCESS_ID, WKF_PARTITION_NAME, DATETIMESTAMP) ",
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE) ");
	
	public static final String CREATE_STG_ITEM_RESPONSE_DETAILS = CustomStringUtil.appendString(
			"INSERT INTO STG_ITEM_RESPONSE_DETAILS (",
			"    STUDENT_BIO_DETAILS_ID,",	//1
			"    CONTENT_CODE,",	//2
			"    OBJECTIVE_CODE,",	//3
			"    TEST_FORM,",	//4
			"    GRADE,",	//5
			"    ITEM_TYPE,",	//6
			"    ITEM_CODE,",	//7
			"    ITEM_NAME,",	//8
			"    READ_CODE,",	//9
			"    SCORE_VALUE,",	//10
			"    WKF_PARTITION_NAME,",	//11
			"    PROCESS_ID,",	//12
			"    DATETIMESTAMP",	//13
			" ) VALUES (",
			"    ?,",	//1
			"    ?,",	//2
			"    ?,",	//3
			"    ?,",	//4
			"    ?,",	//5
			"    ?,",	//6
			"    ?,",	//7
			"    ?,",	//8
			"    ?,",	//9
			"    ?,",	//10
			"    ?,",	//11
			"    ?,",	//12
			"    SYSDATE",	//13
			" )"
		);
	
	public static final String GET_ORG_MAP = "SELECT ORG_NODEID, PARENT_ORG_NODEID FROM ORG_NODE_DIM";
	
	public static final String CLEAN_STG_HIER_DETAILS = "DELETE FROM STG_HIER_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_LSTNODE_HIER_DETAILS = "DELETE FROM STG_LSTNODE_HIER_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_BIO_DETAILS = "DELETE FROM STG_STD_BIO_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_DEMO_DETAILS = "DELETE FROM STG_STD_DEMO_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_SUBTEST_DETAILS = "DELETE FROM STG_STD_SUBTEST_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_STD_OBJECTIVE_DETAILS = "DELETE FROM STG_STD_OBJECTIVE_DETAILS WHERE WKF_PARTITION_NAME = ?";
	public static final String CLEAN_STG_ITEM_RESPONSE_DETAILS = "DELETE FROM STG_ITEM_RESPONSE_DETAILS WHERE WKF_PARTITION_NAME = ?";

	public static final String GET_WS_ROSTER_DATA = "SELECT WS_ROSTER_DATA_ID,REQUEST_OBJECT FROM WS_ROSTER_DATA WHERE STATUS = ?";

	public static final String UPDATE_WS_ROSTER_DATA = "UPDATE WS_ROSTER_DATA SET STATUS = ?,RESPONSE_LOG = ?, UPDATED_TIME =SYSDATE WHERE WS_ROSTER_DATA_ID = ?";
	
	public static final String GET_PROCESS_STATUS = CustomStringUtil.appendString(
			" select hier_validation, bio_validation, demo_validation, ",
			" content_validation, objective_validation, item_validation, process_log ",
			" FROM stg_process_status where process_id = ? ");
}

