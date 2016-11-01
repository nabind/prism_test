package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IOrgQuery {
	
/*	public static final String GET_CURR_TENANT_DETAILS = CustomStringUtil
	.appendString(" SELECT ORG_NODEID, ORG_NODE_NAME, PARENT_ORG_NODEID, ORG_NODE_LEVEL, ORG_MODE",
			" FROM ORG_NODE_DIM",
			" WHERE ORG_NODEID = ? AND CUSTOMERID = ?",
			" ORDER BY ORG_NODE_NAME");*/
	
	public static final String SP_GET_CURR_TENANT_DETAILS = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_CURR_TENANT_DETAILS(?, ?, ?, ?)}";
	
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
	
	public static final String GET_TENANT_DETAILS_ORG_MODE = CustomStringUtil
			.appendString("SELECT OND.ORG_NODEID,OND.ORG_NODE_NAME ,OND.PARENT_ORG_NODEID,OND.ORG_NODE_LEVEL " +
					" FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL " +
					" WHERE OPL.ORG_NODEID = OND.ORG_NODEID AND OPL.CUST_PROD_ID = ? " +
					" AND OND.ORG_MODE = ? AND PARENT_ORG_NODEID = ? AND CUSTOMERID = ?	AND OND.ORG_NODE_LEVEL <> 0	ORDER BY ORG_NODE_NAME");
	
/*	public static final String GET_TENANT_DETAILS = CustomStringUtil
	.appendString("SELECT OND.ORG_NODEID,OND.ORG_NODE_NAME ,OND.PARENT_ORG_NODEID,OND.ORG_NODE_LEVEL, OND.ORG_MODE " +
			" FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL " +
			" WHERE OPL.ORG_NODEID = OND.ORG_NODEID AND OPL.CUST_PROD_ID = ? " +
			" AND PARENT_ORG_NODEID = ? AND CUSTOMERID = ?	AND OND.ORG_NODE_LEVEL <> 0	ORDER BY ORG_NODE_NAME");*/
	
	public static final String SP_GET_TENANT_DETAILS = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_TENANT_DETAILS(?, ?, ?, ?, ?)}";
	
	// get all organization list based on tenantId
	public static final String SP_GET_ORGANIZATION_LIST = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_ORGANIZATION_LIST(?, ?, ?, ?, ?)}";
	// retrieves the organization list children for the org tree based on the org parent id
	public static final String SP_GET_ORG_CHILDREN_LIST = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_ORG_CHILDREN_LIST(?, ?, ?, ?, ?)}";
		
	public static final String GET_TENANT_DETAILS_NON_ROOT = CustomStringUtil
			.appendString("SELECT OND.ORG_NODEID,OND.ORG_NODE_NAME ,OND.PARENT_ORG_NODEID,OND.ORG_NODE_LEVEL, OND.ORG_MODE " +
					" FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL " +
					" WHERE OPL.ORG_NODEID = OND.ORG_NODEID AND OPL.CUST_PROD_ID = ? " +
					" AND PARENT_ORG_NODEID = ? AND CUSTOMERID = ? AND OND.ORG_MODE = ?	AND OND.ORG_NODE_LEVEL <> 0	ORDER BY ORG_NODE_NAME");
	
	public static final String SP_GET_TENANT_DETAILS_NON_ROOT = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_TENANT_DETAILS_NON_ROOT(?, ?, ?, ?, ?, ?)}";
	
	/*public static final String GET_TENANT_DETAILS_NON_ACSI = CustomStringUtil
	.appendString(" SELECT ORG_NODEID,",
			" ORG_NODE_NAME ,PARENT_ORG_NODEID,",
			" ORG_NODE_LEVEL FROM ORG_NODE_DIM WHERE PARENT_ORG_NODEID = ?",
			" AND ORG_NODEID= ? AND CUSTOMERID = ?",
			" ORDER BY ORG_NODE_NAME");*/
	
	/*public static final String GET_TENANT_DETAILS_NON_ACSI = CustomStringUtil
			.appendString(" SELECT OND.ORG_NODEID,",
							" OND.ORG_NODE_NAME,",
							" OND.PARENT_ORG_NODEID,",
							" OND.ORG_NODE_LEVEL",
							" FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL",
							" WHERE OPL.ORG_NODEID = OND.ORG_NODEID",
							" AND OPL.CUST_PROD_ID = ?",
							" AND OND.ORG_MODE = ?",
							" AND PARENT_ORG_NODEID = ?",
							" AND CUSTOMERID = ?",
							" AND OND.ORG_NODE_LEVEL > ?",
							" ORDER BY ORG_NODE_NAME ");*/
	
	/*public static final String GET_TENANT_DETAILS_NON_ACSI = CustomStringUtil
			.appendString(" SELECT OND.ORG_NODEID,",
							" OND.ORG_NODE_NAME,",
							" OND.PARENT_ORG_NODEID,",
							" OND.ORG_NODE_LEVEL, OND.ORG_MODE",
							" FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL",
							" WHERE OPL.ORG_NODEID = OND.ORG_NODEID",
							" AND OPL.CUST_PROD_ID = ?",
							" AND OND.ORG_MODE = ?",
							" AND OND.PARENT_ORG_NODEID = ?",
							" AND OND.ORG_NODEID = ?",
							" AND OND.CUSTOMERID = ?",
							" UNION",
							" SELECT OND.ORG_NODEID,",
							" OND.ORG_NODE_NAME,",
							" OND.PARENT_ORG_NODEID,",
							" OND.ORG_NODE_LEVEL,",
							" OND.ORG_MODE",
							" FROM ORG_NODE_DIM OND",
							" WHERE OND.ORG_NODEID = ?",
							" AND OND.ORG_NODE_LEVEL = 1",
							" ORDER BY ORG_NODE_NAME ");*/
	
	public static final String SP_GET_TENANT_DETAILS_NON_ACSI = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_TENANT_DETAILS_NON_ACSI(?, ?, ?, ?, ?, ?, ?)}";
	
/*	public static final String GET_ORG_HIERARCHY_ON_REDIRECT = CustomStringUtil
	.appendString("SELECT TO_CHAR(O.ORG_NODEID) AS ORG_ID,O.ORG_NODE_NAME,O.PARENT_ORG_NODEID,O.ORG_NODE_LEVEL",
      			  " FROM ORG_NODE_DIM O",
                  " WHERE O.CUSTOMERID = ?",
                  " START WITH ORG_NODEID = ?",
                  " CONNECT BY PRIOR PARENT_ORG_NODEID = ORG_NODEID ",
                  " AND O.ORG_NODE_LEVEL >= (SELECT DISTINCT U.ORG_NODE_LEVEL", 
                                        " FROM ORG_USERS U ",
                                        " WHERE U.ORG_NODEID = ?", 
                                        " AND U.USERID = ?)",
                  " ORDER BY O.ORG_NODE_LEVEL, O.PARENT_ORG_NODEID, O.ORG_NODE_NAME");*/
	
    public static final String SP_GET_ORG_HIER_ON_REDIRECT = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_GET_ORG_HIER_ON_REDIRECT(?, ?, ?, ?, ?)}";
	
	

/*	public static final String SEARCH_ORGANNIZATION = CustomStringUtil.appendString("SELECT O.ORG_NODEID,",
			" O.ORG_NODE_NAME,",
			" (SELECT NVL(COUNT(1), 0) FROM ORG_NODE_DIM m WHERE m.PARENT_ORG_NODEID  = O.ORG_NODEID ",
			" AND m.CUSTOMERID = ? ",
			" AND EXISTS (SELECT 1 ",
			"			FROM ORG_PRODUCT_LINK OPL ",
			"			WHERE m.ORG_NODEID = OPL.ORG_NODEID ",
			"			AND OPL.CUST_PROD_ID = ?) ",
			" ) CHILD_ORG_NO ",
			" FROM ORG_NODE_DIM O",
			" WHERE UPPER(O.ORG_NODE_NAME ) LIKE UPPER(?) ",
			" AND ORG_MODE = ?",
			" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL ", 
            "       WHERE   O.ORG_NODEID = OPL.ORG_NODEID ",
            " 		AND OPL.CUST_PROD_ID = ?)",
			" AND O.ORG_NODE_LEVEL  > = (SELECT ORG_NODE_LEVEL  FROM ORG_NODE_DIM WHERE ORG_NODEID  = ?) ",
			" CONNECT BY NOCYCLE PRIOR O.ORG_NODEID = PARENT_ORG_NODEID START WITH ORG_NODEID = ? ");*/
	
	public static final String SP_SEARCH_ORGANNIZATION = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_SEARCH_ORGANNIZATION(?, ?, ?, ?, ?, ?, ?)}";
	
	
	// search organization auto complete
/*	public static final String SEARCH_ORG_AUTO_COMPLETE = CustomStringUtil.appendString("SELECT ABC.ORG_NODE_NAME ",
			" FROM (SELECT O.ORG_NODE_NAME, O.ORG_NODE_LEVEL ",
			" FROM ORG_NODE_DIM O  ",
			" WHERE O.CUSTOMERID = ? ",
			" AND O.ORG_MODE = ?",
			" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL ", 
            "       WHERE   O.ORG_NODEID = OPL.ORG_NODEID ",
            " 		AND OPL.CUST_PROD_ID = ?)",
			" START WITH O.ORG_NODEID = ? ",
			" CONNECT BY NOCYCLE PRIOR O.ORG_NODEID = PARENT_ORG_NODEID ",
			" ORDER BY O.ORG_NODE_NAME) ABC ",
			" WHERE UPPER(ABC.ORG_NODE_NAME) LIKE UPPER(?) ",
			" AND ROWNUM <= 100");*/
			
	public static final String SP_SEARCH_ORG_AUTO_COMPLETE = "{CALL PKG_MANAGE_ORGANIZATIONS.SP_SEARCH_ORG_AUTO_COMPLETE(?, ?, ?, ?, ?, ?, ?)}";			
	
	public static final String INSERT_STG_ORG_NODE = CustomStringUtil.appendString(
			"INSERT INTO STG_ORG_NODE_DIM ",
			" (ORG_NODEID,ORG_NODE_NAME,ORG_NODE_CODE,ORG_NODE_LEVEL,STRC_ELEMENT,SPECIAL_CODES," ,
			"  ORG_MODE,PARENT_ORG_NODEID,ORG_NODE_CODE_PATH,EMAIL,ADMINID,CUSTOMERID,CREATED_DATE_TIME,UPDATED_DATE_TIME)",
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdatetime(), sysdatetime())");
	
}

