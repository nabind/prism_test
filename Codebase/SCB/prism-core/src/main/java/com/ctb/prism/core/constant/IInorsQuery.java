package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IInorsQuery {
	
	// query for getting logged-in user details
	public static final String CREATE_JOB = CustomStringUtil.appendString(
			"  insert into GROUP_DOWNLOAD_STATUS  (",
			" jobid, pdf_file_name, query_sheet_file, userid, job_details, status,  percentage_done, ",
			" request_date, student_count, email, reporturl, adminid,  customerid, gradeid, request_type, ",
			" group_file, collation_hierarchy, download_type) values (?, ?, ?, ?, ?, 'QU', 0, sysdatetime(), ",
			" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ") ;
	
	public static final String JOB_SEQ_ID = "SELECT NEXT VALUE FOR JOB_SEQ";
	
	public static final String JOB_DETAILS = "SELECT * FROM JOB_TRACKING WHERE JOB_ID = ?";
	
	public static final String UPDATE_STATUS = "UPDATE GROUP_DOWNLOAD_STATUS SET STATUS = ? WHERE JOBID = ?";
	
	public static final String UPDATE_LOG = "UPDATE GROUP_DOWNLOAD_STATUS SET LOG = ? WHERE JOBID = ?";
	
	public static final String UPDATE_STATUS_AND_LOG = CustomStringUtil.appendString("UPDATE JOB_TRACKING SET JOB_STATUS = ?, ",
			" JOB_LOG = ?, UPDATED_DATE_TIME = sysdatetime() WHERE JOB_ID = ?");
	
	public static final String UPDATE_JOB = CustomStringUtil.appendString(
			"  UPDATE JOB_TRACKING SET JOB_STATUS = ?, REQUEST_FILENAME = ?, JOB_LOG = ?, FILE_SIZE = ?,",
			" EXTRACT_ENDDATE = sysdatetime(), UPDATED_DATE_TIME = sysdatetime() WHERE JOB_ID = ? ");
	
	public static final String GET_SCHOOL_CLASS_FOR_STUDENT = CustomStringUtil.appendString(
			" select org_nodeid CLS, org_node_name CLS_NAME, node.parent_org_nodeid SCH, (select org_node_name from org_node_dim where org_nodeid = node.parent_org_nodeid) SCH_NAME ", 
			" from org_node_dim node where org_nodeid = ( ", 
			" select org.parent_org_nodeid from student_bio_dim std, org_node_dim org where std.org_nodeid = org.org_nodeid ",
			" and student_bio_id = ? ) ");
	
	public static final String GET_NODE_NAME = "SELECT ORG_NODE_NAME ORG_NAME FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";
	
	public static final String GET_DISTRICTS = "{CALL FACT.PKG_GET_MIG_RESULTS_GRT$SP_ORG_NODE_DETAILS_DISTRICT(?, ?)}";
	
	public static final String GET_SCHOOLS = "{CALL FACT.PKG_GET_MIG_RESULTS_GRT$SP_ORG_NODE_DETAILS_SCHOOL(?, ?, ?)}";
	
	public static final String GET_RESULTS_GRT = "{CALL FACT.PKG_GET_MIG_RESULTS_GRT$SP_GET_MIG_RESULT_GRT(?, ?, ?, ?, ?, ?)}";
	
	public static final String GET_ALL_RESULTS_GRT = "{CALL FACT.PKG_GET_MIG_RESULTS_GRT$SP_GET_ALL_MIG_RESULT_GRT(?, ?, ?, ?, ?)}";
	
	public static final String GET_IC= "{CALL FACT.PKG_GET_MIG_RESULTS_GRT$SP_GET_INVITATION_DETAILS(?, ?, ?, ?)}";
	
	public static final String GET_ALL_IC= "{CALL FACT.PKG_GET_MIG_RESULTS_GRT$SP_GET_ALL_INVITATION_DETAILS(?, ?, ?, ?)}";
	
	public static final String GET_PRODUCT_NAME_BY_ID = "SELECT TOP(1) PRODUCT_NAME FROM PRODUCT WHERE PRODUCTID = ?";
	
	public static final String GET_CODE= "{CALL FACT.PKG_GROUP_DOWNLOADS_MO$SP_GET_CODE(?, ?, ?)}";
	
	public static final String GET_TP_CODE= "{CALL FACT.PKG_GROUP_DOWNLOADS_MO$SP_GET_TP_CODE(?, ?)}";
	
	public static final String CREATE_STUDENT_PDF_LOG = CustomStringUtil.appendString(
			"INSERT INTO STUDENT_PDF_LOG (STUDENT_BIO_ID,SUBTESTID,ORG_NODEID,FILENAME,CREATED_DATE_TIME) ",
			" VALUES ( ?, ?, ?, ?, sysdatetime())");
	
}

