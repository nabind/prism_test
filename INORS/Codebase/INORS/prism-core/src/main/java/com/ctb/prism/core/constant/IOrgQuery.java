package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IOrgQuery {
	
	public static final String GET_CURR_TENANT_DETAILS = CustomStringUtil
	.appendString(" SELECT ORG_NODEID, ORG_NODE_NAME, PARENT_ORG_NODEID, ORG_NODE_LEVEL",
			" FROM ORG_NODE_DIM",
			" WHERE ORG_NODEID = ? AND CUSTOMERID = ?",
			" ORDER BY ORG_NODE_NAME");
	
	public static final String GET_CURR_EDUCATION_DETAILS = CustomStringUtil
	.appendString(" select ecd.EDU_CENTER_CODE,ecd.EDU_CENTERID from EDU_CENTER_DETAILS ecd " +
			      " join EDU_CENTER_USER_LINK ecul on ecd.edu_centerid=ecul.edu_centerid " +
			      " join users u on ecul.userid=u.userid " +
			      " where ecd.customerid=?");

	/*public static final String GET_TENANT_DETAILS = CustomStringUtil
	.appendString(" SELECT ORG_NODEID,",
			" ORG_NODE_NAME ,PARENT_ORG_NODEID,",
			" ORG_NODE_LEVEL FROM ORG_NODE_DIM ",
			" WHERE PARENT_ORG_NODEID = ? AND CUSTOMERID = ? AND ORG_NODE_LEVEL <> 0",
			" ORDER BY ORG_NODE_NAME");*/
	
	public static final String GET_TENANT_DETAILS = CustomStringUtil
	.appendString("SELECT OND.ORG_NODEID,OND.ORG_NODE_NAME ,OND.PARENT_ORG_NODEID,OND.ORG_NODE_LEVEL " +
			" FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL " +
			" WHERE OPL.ORG_NODEID = OND.ORG_NODEID AND OPL.CUST_PROD_ID = ? " +
			" AND OND.ORG_MODE = ? AND PARENT_ORG_NODEID = ? AND CUSTOMERID = ?	AND OND.ORG_NODE_LEVEL <> 0	ORDER BY ORG_NODE_NAME");
	
	
	public static final String GET_TENANT_DETAILS_NON_ACSI = CustomStringUtil
	.appendString(" SELECT ORG_NODEID,",
			" ORG_NODE_NAME ,PARENT_ORG_NODEID,",
			" ORG_NODE_LEVEL FROM ORG_NODE_DIM WHERE PARENT_ORG_NODEID = ?",
			" AND ORG_NODEID= ? AND CUSTOMERID = ?",
			" ORDER BY ORG_NODE_NAME");
	
	public static final String GET_ORG_HIERARCHY_ON_REDIRECT = CustomStringUtil
	.appendString("SELECT TO_CHAR(O.ORG_NODEID) AS ORG_ID,O.ORG_NODE_NAME,O.PARENT_ORG_NODEID,O.ORG_NODE_LEVEL",
      			  " FROM ORG_NODE_DIM O",
                  " WHERE O.CUSTOMERID = ?",
                  " START WITH ORG_NODEID = ?",
                  " CONNECT BY PRIOR PARENT_ORG_NODEID = ORG_NODEID ",
                  " AND O.ORG_NODE_LEVEL >= (SELECT DISTINCT U.ORG_NODE_LEVEL", 
                                        " FROM ORG_USERS U ",
                                        " WHERE U.ORG_NODEID = ?", 
                                        " AND U.USERID = ?)",
                  " ORDER BY O.ORG_NODE_LEVEL, O.PARENT_ORG_NODEID, O.ORG_NODE_NAME");
	
	

	public static final String SEARCH_ORGANNIZATION = CustomStringUtil.appendString("SELECT ORG_NODEID,",
			" ORG_NODE_NAME,",
			" (SELECT NVL(COUNT(1), 0) FROM ORG_NODE_DIM WHERE PARENT_ORG_NODEID  = O.ORG_NODEID ",
			" AND CUSTOMERID = ?) CHILD_ORG_NO ",
			" FROM ORG_NODE_DIM O ",
			" WHERE UPPER(O.ORG_NODE_NAME ) LIKE UPPER(?) ",
			" AND O.ORG_NODE_LEVEL  >= ",
			" (SELECT DISTINCT ORG_NODE_LEVEL  FROM ORG_NODE_DIM WHERE ORG_NODEID  = ?) ",
			" CONNECT BY NOCYCLE PRIOR ORG_NODEID = PARENT_ORG_NODEID START WITH ORG_NODEID = ? ");
	// search organization auto complete
	public static final String SEARCH_ORG_AUTO_COMPLETE = CustomStringUtil.appendString("SELECT ABC.ORG_NODE_NAME ",
			" FROM (SELECT O.ORG_NODE_NAME, O.ORG_NODE_LEVEL ",
			" FROM ORG_NODE_DIM O ",
			" WHERE O.CUSTOMERID = ? ",
			" START WITH ORG_NODEID = ? ",
			" CONNECT BY NOCYCLE PRIOR ORG_NODEID = PARENT_ORG_NODEID ",
			" ORDER BY O.ORG_NODE_NAME) ABC ",
			" WHERE UPPER(ABC.ORG_NODE_NAME) LIKE UPPER(?) ",
			" AND ROWNUM <= 100");
	
	public static final String INSERT_STG_ORG_NODE = CustomStringUtil.appendString(
			"INSERT INTO STG_ORG_NODE_DIM ",
			" (ORG_NODEID,ORG_NODE_NAME,ORG_NODE_CODE,ORG_NODE_LEVEL,STRC_ELEMENT,SPECIAL_CODES," ,
			"  ORG_MODE,PARENT_ORG_NODEID,ORG_NODE_CODE_PATH,EMAIL,ADMINID,CUSTOMERID,CREATED_DATE_TIME,UPDATED_DATE_TIME)",
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)");
	
}

