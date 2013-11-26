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
			" insert into GROUP_DOWNLOAD_STATUS ",
			" (jobid, pdf_file_name, query_sheet_file, userid, job_details, status, ",
			" percentage_done, request_date, student_count, email, reporturl, adminid, ",
			" customerid, gradeid, request_type, group_file, collation_hierarchy, download_type)",
			" values (?, ?, ?, ?, ?, 'QU', 0, sysdate, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)") ;
	
	public static final String JOB_SEQ_ID = "SELECT JOB_SEQ.nextval AS JOB_SEQ FROM DUAL";
	
	public static final String JOB_DETAILS = CustomStringUtil.appendString(
			" select jobid ID, pdf_file_name as FILE_NAME, file_location as LOC, ",
			" query_sheet_file as QSHEET, userid, job_details as DETAILS, status, ",
			" percentage_done as PERCT, request_date as REQ_DT, completion_date as COMP_DT, ",
			" file_size as SIZ, email, customerid, gradeid, adminid, reporturl URL, request_type TYPE, ",
			" log, download_type D_TYPE ",
			" from group_download_status where jobid = ? ");
	
	public static final String UPDATE_STATUS = "UPDATE GROUP_DOWNLOAD_STATUS SET STATUS = ? WHERE JOBID = ?";
	
	public static final String UPDATE_LOG = "UPDATE GROUP_DOWNLOAD_STATUS SET LOG = ? WHERE JOBID = ?";
	
	public static final String UPDATE_STATUS_AND_LOG = "UPDATE GROUP_DOWNLOAD_STATUS SET STATUS = ? , SET LOG = ? WHERE JOBID = ?";
	
	public static final String UPDATE_JOB = CustomStringUtil.appendString(
			" Update GROUP_DOWNLOAD_STATUS set status = ?, file_location = ?, file_size = ?, log = ?,",
			" completion_date = sysdate where jobid = ? ");
	
	public static final String GET_SCHOOL_CLASS_FOR_STUDENT = CustomStringUtil.appendString(
			" select org_nodeid CLS, org_node_name CLS_NAME, node.parent_org_nodeid SCH, (select org_node_name from org_node_dim where org_nodeid = node.parent_org_nodeid) SCH_NAME ", 
			" from org_node_dim node where org_nodeid = ( ", 
			" select org.parent_org_nodeid from student_bio_dim std, org_node_dim org where std.org_nodeid = org.org_nodeid ",
			" and student_bio_id = ? ) ");
	
	public static final String GET_NODE_NAME = "SELECT ORG_NODE_NAME ORG_NAME FROM ORG_NODE_DIM WHERE ORG_NODEID = ?";
	
	public static final String GET_DISTRICTS = "{CALL PKG_GET_MIG_RESULTS_GRT.SP_ORG_NODE_DETAILS_DISTRICT(?)}";
	
	public static final String GET_SCHOOLS = "{CALL PKG_GET_MIG_RESULTS_GRT.SP_ORG_NODE_DETAILS_SCHOOL(?, ?)}";
	
	public static final String GET_RESULTS_GRT = "{CALL PKG_GET_MIG_RESULTS_GRT.SP_GET_MIG_RESULT_GRT(?, ?, ?)}";
	
	public static final String GET_IC= "{CALL PKG_GET_MIG_RESULTS_GRT.SP_GET_INVITATION_DETAILS(?, ?)}";
	
}

