package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;


/**
 * This interface contains the constants used in the web service
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IERQuery {
	
	public static final String CREATE_SP_HISTORY = CustomStringUtil.appendString(
			"FACT.PKG_ER_MODULE$SP_CREATE_HISTORY(",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?)");
	
	public static final String CREATE_SP_EXCEPTION = CustomStringUtil.appendString(
			"FACT.PKG_ER_MODULE$SP_CREATE_EXCEPTION(",
			"?,?,?,?,?,?,?,?,?)");
	
	public static final String CREATE_UPDATE_SP_BIO = CustomStringUtil.appendString(
			"FACT.PKG_ER_MODULE$SP_CREATE_STUDENT_DEMO(",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?)");
	
	public static final String CREATE_UPDATE_SP_SCHEDULE = CustomStringUtil.appendString(
			"FACT.PKG_ER_MODULE$SP_CREATE_TEST_SCHEDULE(",
			"?,?,?,?,?,?,?,?,?,?,",
			"?,?,?,?,?,?,?,?,?,?,?)");
	
	public static final String GET_STUDENT = CustomStringUtil.appendString( //"select customerid, org_nodeid from student_bio_dim where student_bio_id = ?";
			"SELECT STD.CUSTOMERID customerid, STD.org_nodeid org_nodeid ",
			"FROM ORG_NODE_DIM ORG ,  ",
			"     ORG_LSTNODE_LINK OLNK,  ",
			"     STUDENT_BIO_DIM STD  ",
			"WHERE ORG.ORG_NODE_LEVEL = 1  ",
			"  AND ORG.ORG_NODE_CODE =  ?  ",
			"  AND ORG.ORG_NODEID = OLNK.ORG_NODEID  ",
			"  AND ORG.CUSTOMERID = STD.customerid ", 
			"  AND OLNK.ORG_LSTNODEID = STD.ORG_NODEID  ",
			"  AND OLNK.ADMINID = STD.ADMINID    ",
			"  AND STD.student_bio_id = ? ",
			"  AND STD.EXT_STUDENT_ID = ?");

	
}

