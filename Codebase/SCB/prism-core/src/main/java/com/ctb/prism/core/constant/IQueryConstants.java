package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IQueryConstants extends IUserQuery, IOrgQuery, IParentQuery, IReportQuery, IInorsQuery, IEducationQuery, IWSQuery, IRescoreRequestQuery, IERQuery {
	/** test */
	public static final String GET_USER_BY_EMAIL = "SELECT * FROM DPP_USR_USER USR" +
													" WHERE USR.USER_EMAIL = ?";
	
	//To retrieve prelogin message dynamically
	public static final String GET_SYSTEM_CONFIGURATION_MESSAGE = "FACT.PKG_MANAGE_REPORT$SP_GET_SYSTEM_MESSAGE(?,?,?,?,?)";
	
	// query to retrieve more info message dynamically
	public static final String GET_SYSTEM_CONFIGURATION_MESSAGE_REPORT_SPECIFIC =  CustomStringUtil.appendString(
			"select TOP(1) DM.REPORT_MSG as REPORT_MSG from DASH_REPORTS DR ",
			" join DASH_MENU_RPT_ACCESS DMRA on DR.DB_REPORTID=DMRA.DB_REPORTID ",
			" JOIN DASH_MESSAGES DM on DMRA.DB_REPORTID=DM.DB_REPORTID ",
			" JOIN DASH_MESSAGE_TYPE DMT on  DM.MSG_TYPEID=DMT.MSG_TYPEID ",
			" where DR.DB_REPORTID= ? and DMT.MESSAGE_TYPE= ? and DMT.MESSAGE_NAME= ? and DM.ACTIVATION_STATUS='AC'");
	
	//Fix for TD 77743 - By Joy
	//More info message - By Joy
	public static final String GET_REPORT_MESSAGE_MORE_INFO =  CustomStringUtil.appendString(
			"SELECT DM.REPORT_MSG AS REPORT_MSG",
			" FROM DASH_REPORTS DR, DASH_MESSAGES DM, DASH_MESSAGE_TYPE DMT",
			" WHERE DR.DB_REPORTID = DM.DB_REPORTID",
			" AND DM.MSG_TYPEID = DMT.MSG_TYPEID",
			" AND DM.CUST_PROD_ID = DMT.CUST_PROD_ID",
			" AND DR.DB_REPORTID = ?",
			" AND DMT.MESSAGE_TYPE = ?",
			" AND DMT.MESSAGE_NAME = ?",
			" AND DM.ACTIVATION_STATUS = 'AC'",
			" AND DM.CUST_PROD_ID = (SELECT CUST_PROD_ID",
									" FROM CUST_PRODUCT_LINK",
									" WHERE PRODUCTID = ?",
									" AND CUSTOMERID = ?)");
		
	public static final String GET_REPORT_MESSAGE = CustomStringUtil.appendString(
			"SELECT DM.REPORT_MSG",
			" FROM DASH_REPORTS DR",
			" JOIN (SELECT DISTINCT DB_REPORTID, ORG_LEVEL FROM DASH_MENU_RPT_ACCESS) DMRA ON DR.DB_REPORTID = DMRA.DB_REPORTID",
			" JOIN DASH_MESSAGES DM ON DMRA.DB_REPORTID = DM.DB_REPORTID",
			" JOIN DASH_MESSAGE_TYPE DMT ON DM.MSG_TYPEID = DMT.MSG_TYPEID",
			" WHERE DR.DB_REPORTID = ?",
			" AND DMT.MESSAGE_TYPE = ?",
			" AND DMT.MESSAGE_NAME = ?",
			" AND DM.ACTIVATION_STATUS = 'AC'",
			" AND DM.CUST_PROD_ID = (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK WHERE PRODUCTID = ? AND CUSTOMERID = ?)",
			" AND DMRA.ORG_LEVEL = ?"
			);
	public static final String GET_ALL_REPORT_MESSAGES = CustomStringUtil.appendString(
			"SELECT DMT.MESSAGE_TYPE, DMT.MESSAGE_NAME, DM.REPORT_MSG",
			" FROM DASH_REPORTS DR",
			" JOIN (SELECT DISTINCT DB_REPORTID, ORG_LEVEL FROM DASH_MENU_RPT_ACCESS) DMRA ON DR.DB_REPORTID = DMRA.DB_REPORTID",
			" JOIN DASH_MESSAGES DM ON DMRA.DB_REPORTID = DM.DB_REPORTID",
			" JOIN DASH_MESSAGE_TYPE DMT ON DM.MSG_TYPEID = DMT.MSG_TYPEID",
			" WHERE DR.DB_REPORTID = ?",
			" AND DM.ACTIVATION_STATUS = 'AC' AND DM.CUST_PROD_ID = DMT.CUST_PROD_ID",
			" AND DM.CUST_PROD_ID = (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK WHERE PRODUCTID = ? AND CUSTOMERID = ?)",
			" AND DMRA.ORG_LEVEL = ?",
			" ORDER BY DMT.MESSAGE_TYPE"
			);
	
	// query to retrieve tenant id for a particular username
	public static final String GET_TENANT_ID = "SELECT TOP(1) org_nodeid FROM users users, org_users org WHERE upper(USERNAME) = upper(?) and org.userid = users.userid";
	
	// query to retrieve education center id for a particular username
	public static final String GET_EDU_TENANT_ID = "SELECT TOP(1) EDU.EDU_CENTERID FROM USERS USERS, EDU_CENTER_USER_LINK EDU  WHERE UPPER(USERNAME) = UPPER(?) AND EDU.USERID = USERS.USERID";
	
	public static final String GET_REPORT_ID = "select TOP(1) db_reportid from DASH_REPORTS where report_folder_uri like ?";

	
	// get all organization list based on tenantId
	public static final String GET_ORGANIZATION_LIST = CustomStringUtil.appendString
			("Select Org_Nodeid,", 
			 " Org_Node_Name,",
			 " Parent_Org_Nodeid,",
			 " convert(varchar(4000), ?)  AS SELECTED_ORG_ID,",
			 " Org_Node_Level,",
			 " (Select ISNULL(Count(1), 0) From Org_Node_dim Where Parent_Org_Nodeid = ? And Customerid = ? and org_mode=?) Child_Org_No,",
			 " (Select ISNULL(Sum(1), 0) From Users u, Org_Users o Where u.Userid = o.Userid And u.activation_status in ('AC','SS')",
			 " And o.Org_Nodeid = d.Org_Nodeid Group By o.Org_Nodeid) User_No",
			 " From Org_Node_Dim d",
			 " Where d.org_nodeid = ? And Customerid = ?",
			 " Order By Org_Node_Name");
			
	/*("SELECT ORG_ID,ORG_NAME,O.ORG_PARENT_ID,to_char(?) AS SELECTED_ORG_ID,O.ORG_LEVEL, ",
		       " (SELECT NVL(COUNT(1),0) FROM ORG_HIERARCHY WHERE ORG_PARENT_ID = ? and (adminid = 0 OR adminid = ?) ) CHILD_ORG_NO,",
		       " (SELECT NVL(SUM(COUNT(1)),0 ) FROM USERS U WHERE ORG_ID IN (SELECT ORG_ID",
		       " FROM (SELECT DISTINCT LEVEL1_ORGID AS ORG_ID,LEVEL1_ORG_NAME AS ORG_NAME,0 PARENT_ID",
               " FROM ORG_NODE_DIM L1 UNION",
               " SELECT DISTINCT LEVEL2_ORGID AS ORG_ID,LEVEL2_ORG_NAME AS ORG_NAME,LEVEL1_ORGID PARENT_ID",
               " FROM ORG_NODE_DIM L2 UNION",
               " SELECT DISTINCT LEVEL3_ORGID AS ORG_ID,LEVEL3_ORG_NAME AS ORG_NAME,LEVEL2_ORGID PARENT_ID",
               " FROM ORG_NODE_DIM L3 UNION",
               " SELECT DISTINCT LEVEL4_ORGID AS ORG_ID,LEVEL4_ORG_NAME AS ORG_NAME,LEVEL3_ORGID PARENT_ID",
               " FROM ORG_NODE_DIM L4 where adminid = ? ) CONNECT BY NOCYCLE PRIOR ORG_ID = PARENT_ID START WITH ORG_ID = ?)",
               " and u.org_level NOT IN 0 GROUP BY ORG_ID) USER_NO",
		       " FROM ORG_HIERARCHY O",
		       " WHERE O.ORG_ID = ?");*/
	
	// retrieves the organization list children for the org tree based on the org parent id
	/*public static final String GET_ORG_CHILDREN_LIST = CustomStringUtil.appendString(
			" Select Org_Nodeid,",
			" Org_Node_Name,",
			" Parent_Org_Nodeid ,",
			" to_char(?)   AS SELECTED_ORG_ID,",
			" Org_Node_Level,",
			" (Select Nvl(Count(1), 0) From Org_Node_dim Where Parent_Org_Nodeid = Org_Nodeid And Customerid = ? and org_mode=?) Child_Org_No,",
			" (Select Nvl(Sum(Count(1)), 0) From Users u, Org_Users o Where u.Userid = o.Userid And u.activation_status in ('AC','SS') ",   
			"  And o.Org_Nodeid = d.Org_Nodeid Group By o.Org_Nodeid) User_No, ",   
			" From Org_Node_Dim d",
			" Where d.parent_org_nodeid = ?",
			" And Customerid = ? ",
			" Order By Org_Node_Name");*/
	/*("SELECT ORG_ID,ORG_NAME,O.ORG_PARENT_ID,O.ORG_LEVEL,",
		       " (SELECT NVL(COUNT(1),0) FROM ORG_HIERARCHY WHERE ORG_PARENT_ID = O.ORG_ID AND (o.adminid = 0 OR o.adminid = ?) ) CHILD_ORG_NO,",
		       " (SELECT NVL(SUM(COUNT(1)),0 ) FROM USERS U WHERE ORG_ID IN (SELECT ORG_ID",
		       " FROM (SELECT DISTINCT LEVEL1_JASPER_ORGID AS ORG_ID,LEVEL1_ELEMENT_NAME AS ORG_NAME,0 PARENT_ID",
               " FROM ORG_NODE_DIM L1 UNION",
               " SELECT DISTINCT LEVEL2_JASPER_ORGID AS ORG_ID,LEVEL2_ELEMENT_NAME AS ORG_NAME,LEVEL1_JASPER_ORGID PARENT_ID",
               " FROM ORG_NODE_DIM L2 UNION",
               " SELECT DISTINCT LEVEL3_JASPER_ORGID AS ORG_ID,LEVEL3_ELEMENT_NAME AS ORG_NAME,LEVEL2_JASPER_ORGID PARENT_ID",
               " FROM ORG_NODE_DIM L3 UNION",
               " SELECT DISTINCT LEVEL4_JASPER_ORGID AS ORG_ID,LEVEL4_ELEMENT_NAME AS ORG_NAME,LEVEL3_JASPER_ORGID PARENT_ID",
               " FROM ORG_NODE_DIM L4 where adminid = ?) CONNECT BY NOCYCLE PRIOR ORG_ID = PARENT_ID START WITH ORG_ID = O.ORG_ID)",
               " and u.org_level NOT IN 0 GROUP BY ORG_ID) USER_NO",
		       " FROM ORG_HIERARCHY O",
		       " WHERE O.ORG_PARENT_ID = ?",
		       " AND (o.adminid = 0 OR o.adminid = ?) ",
		       " ORDER BY  ORG_NAME ")*/;
	
	public static final String GET_ORGANIZATION_CHILDREN_LIST = CustomStringUtil.appendString(			
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  ORG_NODE_NAME,	",
			"	  PARENT_ORG_NODEID,	",
			"	  ORG_MODE,	",
			"	  CUSTOMERID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = ?	",
			"	UNION ALL	",
			"	SELECT	",
			"	  O1.ORG_NODEID,	",
			"	  O1.ORG_NODE_NAME,	",
			"	  O1.PARENT_ORG_NODEID,	",
			"	  O1.ORG_MODE,	",
			"	  O1.CUSTOMERID,	",
			"	  h$cte.path + CONVERT(varchar(max), O1.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM O1	",
			"	JOIN h$cte	",
			"	  ON h$cte.ORG_NODEID = O1.PARENT_ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CAST(O1.ORG_NODEID AS varchar(100)) + '|%')	",
			"	SELECT TOP (?)	",
			"	  O.ORG_NODEID,	",
			"	  O.ORG_NODE_NAME,	",
			"	  O.PARENT_ORG_NODEID,	",
			"	  CONVERT(varchar(4000), ?) AS SELECTED_ORG_ID,	",
			"	  (SELECT	",
			"	    ISNULL(COUNT(1), 0)	",
			"	  FROM ORG_NODE_DIM m	",
			"	  WHERE m.PARENT_ORG_NODEID = O.ORG_NODEID	",
			"	  AND m.CUSTOMERID = ?	",
			"	  AND EXISTS (SELECT	",
			"	1	",
			"	  FROM ORG_PRODUCT_LINK OPL	",
			"	  WHERE m.ORG_NODEID = OPL.ORG_NODEID	",
			"	  AND OPL.CUST_PROD_ID = ?))	",
			"	  CHILD_ORG_NO	",
			"	FROM h$cte O	",
			"	WHERE O.ORG_NODEID <> ?	",
			"	AND O.ORG_MODE = ?	",
			"	AND O.CUSTOMERID = ?	",
			"	AND EXISTS (SELECT	",
			"	1	",
			"	FROM ORG_PRODUCT_LINK OPL	",
			"	WHERE O.ORG_NODEID = OPL.ORG_NODEID	",
			"	AND OPL.CUST_PROD_ID = ?)	",
			"	GROUP BY O.ORG_NODEID,	",
			"	         O.ORG_NODE_NAME,	",
			"	         O.PARENT_ORG_NODEID,	",
			"	         O.ORG_MODE	",
			"	ORDER BY O.ORG_NODEID	");
		       
	 // get all organization list based on tenantId on scrolling(on "more" button click)
	public static final String GET_ORGANIZATION_CHILDREN_LIST_ON_SCROLL = CustomStringUtil.appendString(			
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  Org_Nodeid,	",
			"	  Org_Node_Name,	",
			"	  Parent_Org_Nodeid,	",
			"	  ORG_MODE,	",
			"	  Customerid,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), Org_Nodeid) + '|' AS path	",
			"	FROM Org_Node_Dim	",
			"	WHERE Org_Nodeid = ?	",
			"	UNION ALL	",
			"	SELECT	",
			"	  o1.Org_Nodeid,	",
			"	  o1.Org_Node_Name,	",
			"	  o1.Parent_Org_Nodeid,	",
			"	  o1.ORG_MODE,	",
			"	  o1.Customerid,	",
			"	  h$cte.path + CONVERT(varchar(max), o1.Org_Nodeid) + '|' AS path	",
			"	FROM Org_Node_Dim o1	",
			"	JOIN h$cte	",
			"	  ON h$cte.Org_Nodeid = o1.Parent_Org_Nodeid	",
			"	WHERE h$cte.path NOT LIKE '%|' + CAST(o1.Org_Nodeid AS varchar(100)) + '|%')	",
			"	SELECT TOP (?)	",
			"	  o.Org_Nodeid,	",
			"	  o.Org_Node_Name,	",
			"	  o.Parent_Org_Nodeid,	",
			"	  CONVERT(varchar(4000), ?) AS Selected_Org_Id,	",
			"	  (SELECT	",
			"	    ISNULL(COUNT(1), 0)	",
			"	  FROM Org_Node_Dim	",
			"	  WHERE Parent_Org_Nodeid = o.Org_Nodeid	",
			"	  AND Customerid = ?)	",
			"	  Child_Org_No	",
			"	FROM h$cte o	",
			"	WHERE o.Org_Nodeid <> ?	",
			"	AND o.Org_Nodeid > ?	",
			"	AND o.ORG_MODE = ?	",
			"	AND o.Customerid = ?	",
			"	AND EXISTS (SELECT	",
			"	1	",
			"	FROM ORG_PRODUCT_LINK OPL	",
			"	WHERE O.ORG_NODEID = OPL.ORG_NODEID	",
			"	AND OPL.CUST_PROD_ID = ?)	",
			"	GROUP BY o.Org_Nodeid,	",
			"	         o.Org_Node_Name,	",
			"	         o.Parent_Org_Nodeid,	",
			"	         o.ORG_MODE	",
			"	ORDER BY o.Org_Nodeid	");
	
	public static final String GET_ORGANIZATION_CHILDREN_LIST_ON_SCROLL_WITH_SRCH_PARAM = CustomStringUtil.appendString(
			" 	SELECT	",
			" 	  a.org_nodeid,	",
			" 	  a.org_node_name,	",
			" 	  a.parent_org_nodeid,	",
			" 	  a.SELECTED_ORG_ID,	",
			" 	  a.CHILD_ORG_NO	",
			" 	FROM (SELECT	",
			" 	  O.org_nodeid,	",
			" 	  O.org_node_name,	",
			" 	  O.parent_org_nodeid,	",
			" 	  CONVERT(varchar(4000), ?) AS SELECTED_ORG_ID,	",
			" 	  (SELECT	",
			" 	    ISNULL(COUNT(1), 0)	",
			" 	  FROM org_node_dim	",
			" 	  WHERE parent_org_nodeid = O.org_nodeid)	",
			" 	  CHILD_ORG_NO,	",
			" 	  ROW_NUMBER() OVER (ORDER BY O.org_nodeid) ROWNUM	",
			" 	FROM org_node_dim O	",
			" 	WHERE O.org_nodeid <> ?	",
			" 	AND O.org_nodeid > ?	",
			" 	AND O.ORG_MODE = ?	",
			" 	AND UPPER(O.org_node_name) LIKE UPPER(?)	",
			" 	AND o.customerid = ?	",
			" 	AND EXISTS (SELECT	",
			" 	1	",
			" 	FROM ORG_PRODUCT_LINK OPL	",
			" 	WHERE O.ORG_NODEID = OPL.ORG_NODEID	",
			" 	AND OPL.CUST_PROD_ID = ?)	",
			" 	GROUP BY O.org_nodeid,	",
			" 	         O.org_node_name,	",
			" 	         O.parent_org_nodeid,	",
			" 	         O.ORG_MODE) a	",
			" 	WHERE a.ROWNUM <= ?	",
			" 	ORDER BY a.org_nodeid	"); 
	
	public static final String GET_USER_COUNT = CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  *	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = ?	",
			"	UNION ALL	",
			"	SELECT	",
			"	  o.*	",
			"	FROM ORG_NODE_DIM o	",
			"	JOIN h$cte	",
			"	  ON h$cte.ORG_NODEID = o.PARENT_ORG_NODEID)	",
			"	SELECT	",
			"	  ABC.USER_NO,	",
			"	  ADM.ADMIN_NAME	",
			"	FROM (SELECT	",
			"	       COUNT(*) USER_NO,	",
			"	       1 AS MATCH	",
			"	     FROM USERS USR,	",
			"	          ORG_USERS ORGUSER,	",
			"	          (SELECT	",
			"	            *	",
			"	          FROM h$cte m	",
			"	          WHERE m.CUSTOMERID = ?	",
			"	          AND m.ORG_MODE = ?	",
			"	          AND EXISTS (SELECT	",
			"	1	",
			"	          FROM ORG_PRODUCT_LINK OPL	",
			"	          WHERE m.ORG_NODEID = OPL.ORG_NODEID	",
			"	          AND OPL.CUST_PROD_ID = ?)	",
			"	          UNION	",
			"	          SELECT	",
			"	            *	",
			"	          FROM ORG_NODE_DIM	",
			"	          WHERE CUSTOMERID = ?	",
			"	          AND ORG_NODEID = ?	",
			"	          AND ORG_NODE_LEVEL = 1) HIER,	",
			"	          ORG_PRODUCT_LINK OLP	",
			"	     WHERE ORGUSER.USERID = USR.USERID	",
			"	     AND HIER.ORG_NODEID = ORGUSER.ORG_NODEID	",
			"	     AND NOT EXISTS (SELECT	",
			"	1	",
			"	     FROM USER_ROLE UR	",
			"	     WHERE UR.USERID = USR.USERID	",
			"	     AND ROLEID = 6)	",
			"	     AND ORGUSER.ORG_NODEID = OLP.ORG_NODEID	",
			"	     AND OLP.CUST_PROD_ID = ?	",
			"	     AND USR.ACTIVATION_STATUS != 'SS') ABC,	",
			"	     (SELECT	",
			"	       1 AS MATCH,	",
			"	       ADM.ADMIN_NAME	",
			"	     FROM ADMIN_DIM ADM	",
			"	     WHERE ADM.ADMINID = (SELECT	",
			"	       ADMINID	",
			"	     FROM CUST_PRODUCT_LINK	",
			"	     WHERE CUST_PROD_ID = ?)) ADM	",
			"	WHERE ABC.MATCH = ADM.MATCH	");       
	
	// START---- Queries related to Role Management
	public static final String GET_ROLE_DETAILS = "SELECT ROLEID ROLE_ID, ROLE_NAME, DESCRIPTION FROM ROLE ORDER BY ROLE_NAME";
	
	public static final String SP_GET_ROLE_DETAILS = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_ROLE_DETAILS(?)}";
	
	public static final String SP_GET_EDU_USER_ROLE = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_EDU_USER_ROLE(?, ?)}";
	
	public static final String GET_ROLE_DETAILS_BY_ID = "SELECT ROLEID ROLE_ID, ROLE_NAME, DESCRIPTION  FROM ROLE WHERE ROLEID = ?";
	
	/*public static final String GET_USERS_FOR_SELECTED_ROLE = "select ou.userid user_id, ou.username, org.org_NODE_level from users ou, user_role ur, org_users org where ou.userid = ur.userid and ou.userid = org.userid and ur.roleid = ?";*/
	
	public static final String GET_USERS_FOR_SELECTED_ROLE =CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  CUSTOMERID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = ?	",
			"	UNION ALL	",
			"	SELECT	",
			"	  o.ORG_NODEID,	",
			"	  o.CUSTOMERID,	",
			"	  h$cte.path + CONVERT(varchar(max), o.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM o	",
			"	JOIN h$cte	",
			"	  ON h$cte.ORG_NODEID = o.PARENT_ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), o.ORG_NODEID) + '|%')	",
			"	SELECT TOP (15)	",
			"	  OU.USERID USER_ID,	",
			"	  OU.USERNAME,	",
			"	  ORG.ORG_NODE_LEVEL	",
			"	FROM USERS OU,	",
			"	     USER_ROLE UR,	",
			"	     ORG_USERS ORG	",
			"	WHERE OU.USERID = UR.USERID	",
			"	AND OU.USERID = ORG.USERID	",
			"	AND UR.ROLEID = ?	",
			"	AND OU.CUSTOMERID = ?	",
			"	AND ORG.ORG_NODEID IN (SELECT	",
			"	  ORG_NODEID	",
			"	FROM h$cte	",
			"	WHERE CUSTOMERID = ?)	");
	
	public static final String GET_MORE_USERS_FOR_SELECTED_ROLE =CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  CUSTOMERID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = ?	",
			"	UNION ALL	",
			"	SELECT	",
			"	  o.ORG_NODEID,	",
			"	  o.CUSTOMERID,	",
			"	  h$cte.path + CONVERT(varchar(max), o.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM o	",
			"	JOIN h$cte	",
			"	  ON h$cte.ORG_NODEID = o.PARENT_ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), o.ORG_NODEID) + '|%')	",
			"	SELECT TOP (15)	",
			"	  OU.USERID USER_ID,	",
			"	  OU.USERNAME,	",
			"	  ORG.ORG_NODE_LEVEL	",
			"	FROM USERS OU,	",
			"	     USER_ROLE UR,	",
			"	     ORG_USERS ORG	",
			"	WHERE OU.USERID = UR.USERID	",
			"	AND OU.USERID = ORG.USERID	",
			"	AND UR.ROLEID = ?	",
			"	AND OU.CUSTOMERID = ?	",
			"	AND OU.USERID > ?	",
			"	AND ORG.ORG_NODEID IN (SELECT	",
			"	  ORG_NODEID	",
			"	FROM h$cte	",
			"	WHERE CUSTOMERID = ?)	");
	
	public static final String DELETE_ROLE_FROM_ROLES_TABLE ="DELETE FROM ROLE WHERE ROLEID = ?";
	
	public static final String DELETE_ROLE_FROM_USER_ROLE_TABLE ="DELETE FROM USER_ROLE WHERE ROLEID = ?";
	
	public static final String DELETE_ROLE_FROM_ROLE_CUSTOMER_TABLE ="DELETE FROM ROLE_CUSTOMER WHERE ROLEID = ?";
	
	public static final String INSERT_INTO_USER_ROLE ="INSERT INTO USER_ROLE (USERID, ROLEID) SELECT (SELECT USERID FROM USERS WHERE upper(USERNAME) = upper(?) ), ?";
	public static final String INSERT_INTO_ROLE_CUSTOMER = CustomStringUtil.appendString("INSERT INTO ROLE_CUSTOMER (USERID, ROLEID, CUSTOMERID, CREATED_DATE_TIME) ",
								" SELECT (SELECT USER_ID FROM USERS WHERE upper(USERNAME) = upper(?) ), ?, ?, sysdatetime()" );
	
	public static final String IS_ROLE_TAGGED= CustomStringUtil.appendString("SELECT URL.USERID USER_ID FROM USER_ROLE URL WHERE URL.ROLEID=?  ",
																		" AND URL.USERID=(SELECT TOP(1) USERID FROM USERS WHERE UPPER(USERNAME)= UPPER(?))");
	
	public static final String UPDATE_ROLE = "UPDATE ROLE SET ROLE_NAME = ?, DESCRIPTION  = ? WHERE ROLEID = ?";
	
	//dissociate user from role
	public static final String DELETE_USER_FROM_USER_ROLE_TABLE ="DELETE FROM USER_ROLE WHERE ROLEID = ? AND USERID = ?";
	
	//END ---- Queries related to Role Management
	
	
	public static final String VALIDATE_USER_NAME = "FACT.PKG_MANAGE_USERS$SP_VALIDATE_USERNAME(?,?)";
	public static final String GET_USER_ORG = "select ou.org_nodeid NODEID from users u, org_users ou where u.userid = ou.userid and upper(U.USERNAME) = upper(?) ";
	public static final String UPDATE_USER_ORG = "update org_users set org_nodeid = ? where org_nodeid = ? and userid = (select userid from users where upper(USERNAME) = upper(?) )";
	
	
	public static final String VALIDATE_ACTIVE_USER_NAME = "SELECT USR.USERNAME AS USERNAME FROM USERS USR WHERE upper(USR.USERNAME) = upper(?) AND USR.ACTIVATION_STATUS IN ('AC','SS')";
	
	public static final String GET_SECRET_QUESTION = "FACT.PKG_MY_ACCOUNT$SP_GET_SECURITY_QUESTIONS(?,?)";
	
	public static final String PARENT_USER_SEQ_ID = "SELECT NEXT VALUE FOR  USER_ID_SEQ AS PARENT_SEQ_ID";
	
	
	//TODO - Need to delete after moving SP_SAVE_PH_ANSWER() for all users
	public static final String INSERT_ANSWER_DATA = CustomStringUtil.appendString(
			"INSERT INTO PWD_HINT_ANSWERS (PH_ANSWERID, USERID, PH_QUESTIONID, ANSWER_VALUE, CREATED_DATE_TIME) ",
			" VALUES (NEXT VALUE FOR PWD_HINT_ANSWERS_SEQ, ?, ?, ?, SYSDATETIME())");
	
	//TODO - Need to delete after moving SP_SAVE_PH_ANSWER() for all users
    public static final String CHECK_FOR_EXISTING_ANSWER = CustomStringUtil.appendString("SELECT COUNT(*) AS NO_OF_ANSWERS FROM  PWD_HINT_ANSWERS P WHERE P.USERID=?");
    
    //TODO - Need to delete after moving SP_SAVE_PH_ANSWER() for all users
    public static final String UPDATE_ANSWER_DATA = CustomStringUtil.appendString("UPDATE PWD_HINT_ANSWERS	",
																				" 	SET PH_QUESTIONID = ?,	",
																				" 	    ANSWER_VALUE = ?,	",
																				" 	    UPDATED_DATE_TIME = SYSDATETIME()	",
																				" 	WHERE USERID = ?	",
																				" 	AND PH_ANSWERID = ?	");
    
    public static final String ADMIN_YEAR_LIST = "select adminid, admin_name, is_current_admin,admin_season from admin_dim Where is_current_admin = 'Y' order by admin_seq desc";
    public static final String CURR_ADMIN_YEAR = "select adminid, admin_name, is_current_admin,admin_season from admin_dim Where is_current_admin = 'Y'";
    
    		public static final String VALIDATE_SECURITY_ANSWERS= CustomStringUtil.appendString(
    				" 	SELECT	",
    				" 	  CASE	",
    				" 	    WHEN COUNT(U.USERNAME) > 0 THEN 1	",
    				" 	    ELSE -1	",
    				" 	  END AS USERCOUNT	",
    				" 	FROM PWD_HINT_ANSWERS PWD,	",
    				" 	     USERS U,	",
    				" 	     (SELECT	",
    				" 	       U.USERID	",
    				" 	     FROM PWD_HINT_ANSWERS PWD,	",
    				" 	          USERS U	",
    				" 	     WHERE U.USERID = (SELECT TOP (1)	",
    				" 	       USERID	",
    				" 	     FROM USERS	",
    				" 	     WHERE UPPER(USERNAME) = UPPER(?))	",
    				" 	     AND PWD.USERID = U.USERID	",
    				" 	     AND PWD.PH_QUESTIONID = ?	",
    				" 	     AND UPPER(PWD.ANSWER_VALUE) = UPPER(?)) VAL1,	",
    				" 	     (SELECT	",
    				" 	       U.USERID	",
    				" 	     FROM PWD_HINT_ANSWERS PWD,	",
    				" 	          USERS U	",
    				" 	     WHERE U.USERID = (SELECT TOP (1)	",
    				" 	       USERID	",
    				" 	     FROM USERS	",
    				" 	     WHERE UPPER(USERNAME) = UPPER(?))	",
    				" 	     AND PWD.USERID = U.USERID	",
    				" 	     AND PWD.PH_QUESTIONID = ?	",
    				" 	     AND UPPER(PWD.ANSWER_VALUE) = UPPER(?)) VAL2	",
    				" 	WHERE PWD.USERID = VAL1.USERID	",
    				" 	AND VAL1.USERID = VAL2.USERID	",
    				" 	AND U.USERID = PWD.USERID	",
    				" 	AND PWD.PH_QUESTIONID = ?	",
    				" 	AND UPPER(PWD.ANSWER_VALUE) = UPPER(?) ");
    
    public static final String GET_ALL_USERS_BY_EMAIL = "SELECT USERNAME,FIRST_NAME,LAST_NAME FROM users WHERE EMAIL_ADDRESS=? and ACTIVATION_STATUS IN ('AC','SS')";
    
    /*
     * There exists 1-N mapping between parent and org.
	 * As per current data model, the method is not required. 
	 * Blocked by Joy
     * */
    //public static final String UPDATE_PARENT_USER_ORG = "Update Users Set Org_Id = (Select o.level3_jasper_orgid From Invitation_Code i,org_node_dim o  Where i.Activation_Status IN ('AC','SS') And i.Invitation_Code = ? And i.ORG_NODEID =o.org_nodeid And Rownum = 1) Where Upper(Username) = Upper(?)";

    
 // query to insert report role relation in report_role table
	public static final String INSERT_USABILITY_LOG =  CustomStringUtil.appendString(
			" 	INSERT INTO USABILITY_LOG (REPORT_URL, REPORT_ID, REPORT_NAME, USERNAME, CURRENTORG, LOGGED_ON)	",
			" 	  VALUES (?, ?, ?, ?, ?, SYSDATETIME())	");


	public static final String GET_USERS_FOR_SSO_ORG = CustomStringUtil.appendString(
			"   select TOP(1) p.customerid CUSTOMERID, org.org_nodeid NODEID from test_program p, org_node_dim org ",
			" where tp_code = ? and  org_node_code = ?");
	
	public static final String GET_ORG_LEVEL = CustomStringUtil.appendString(
			" select TOP(1) org_nodeid NODEID, org_node_name ORGNAME, org_node_level ORGLEVEL, customerid CUSTOMERID ",
			" from org_node_dim where org_node_code_path = ?  AND org_node_level = ?");
	
	public static final String GET_MANAGE_MESSAGE_LIST = "FACT.PKG_MANAGE_REPORT$SP_GET_REPORT_MESSAGE_LIST(?,?,?,?)";
	
	public static final String DELETE_DASH_MESSAGE = CustomStringUtil.appendString(
			"  DELETE FROM DASH_MESSAGES WHERE DB_REPORTID = ? AND CUST_PROD_ID = ?");
	
	public static final String INSERT_DASH_MESSAGES = CustomStringUtil.appendString(
			"INSERT INTO META.DASH_MESSAGES (DB_REPORTID, MSG_TYPEID, REPORT_MSG, CUST_PROD_ID, ACTIVATION_STATUS, CREATED_DATE_TIME) ",
			" VALUES (?, ?, ?, ?, ?, SYSDATETIME())");
	
	public static final String GET_USER_ROLE = CustomStringUtil.appendString(
			" 	WITH TEMP	",
			" 	AS (SELECT DISTINCT	",
			" 	  ORG_LEVEL,	",
			" 	  ORG_LABEL	",
			" 	FROM ORG_TP_STRUCTURE)	",
			" 	SELECT DISTINCT	",
			" 	  RLE.ROLEID,	",
			" 	  RLE.ROLE_NAME,	",
			" 	  OTS.ORG_LABEL,	",
			" 	  RLE.DESCRIPTION	",
			" 	FROM USER_ROLE URLE,	",
			" 	     ROLE RLE,	",
			" 	     USERS USR,	",
			" 	     ORG_USERS OU,	",
			" 	     (SELECT	",
			" 	       T.ORG_LEVEL,	",
			" 	       STUFF((SELECT	",
			" 	         '/' + ORG_LABEL	",
			" 	       FROM TEMP	",
			" 	       WHERE ORG_LEVEL = T.ORG_LEVEL	",
			" 	       ORDER BY ORG_LEVEL	",
			" 	       FOR xml PATH (''), TYPE)	",
			" 	       .value('.', 'VARCHAR(MAX)'), 1, 1, '') AS ORG_LABEL	",
			" 	     FROM TEMP T	",
			" 	     GROUP BY T.ORG_LEVEL) OTS	",
			" 	WHERE USR.USERID = ?	",
			" 	AND URLE.USERID = USR.USERID	",
			" 	AND RLE.ROLEID = URLE.ROLEID	",
			" 	AND USR.USERID = OU.USERID	",
			" 	AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL	");

	public static final String GET_PRODUCT_NAME=CustomStringUtil.appendString(
			" 	SELECT TOP (1)	",
			" 	  B.PRODUCT_NAME	",
			" 	FROM CUST_PRODUCT_LINK A,	",
			" 	     PRODUCT B	",
			" 	WHERE A.PRODUCTID = B.PRODUCTID	",
			" 	AND A.CUSTOMERID = ? ");
	
	public static final String GET_STUDENT_DATA = CustomStringUtil.appendString(
			" 	SELECT	",
			" 	  *	",
			" 	FROM MV_STUDENT_FILE_DOWNLOAD MSTFD,	",
			" 	     ORGUSER_MAPPING OUM	",
			" 	WHERE MSTFD.ORG_NODEID = OUM.LOWEST_NODEID	",
			" 	AND OUM.USERID = ?	",
			" 	AND EXISTS (SELECT	",
			" 	1	",
			" 	FROM MV_STUDENT_DETAILS MSTD	",
			" 	WHERE MSTD.STUDENT_BIO_ID = MSTFD.ROSTER_ID	",
			" 	AND MSTD.DATE_TEST_TAKEN BETWEEN CONVERT(datetime2(0), ?, 101) AND CONVERT(datetime2(0), ?, 101))	");

	public static final String GET_STUDENT_DATA_1 = CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  *	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = (SELECT	",
			"	  ORG_NODEID	",
			"	FROM ORG_USERS	",
			"	WHERE USERID = 100)	",
			"	UNION ALL	",
			"	SELECT	",
			"	  o.*	",
			"	FROM ORG_NODE_DIM o	",
			"	JOIN h$cte	",
			"	  ON h$cte.ORG_NODEID = o.PARENT_ORG_NODEID)	",
			"	SELECT	",
			"	  U.CUSTOMERID AS PRISM_CUSTOMER_ID,	",
			"	  B.STUDENT_BIO_ID AS ROSTER_ID,	",
			"	  N.ORG_NODE_LEVEL AS ORG_NODE_LEVEL,	",
			"	  N.ORG_NODE_NAME AS ELEMENT_HIERARCHY_NAME,	",
			"	  N.ORG_NODE_CODE AS ELEMENT_ELEMENT_NUMBER,	",
			"	  N.SPECIAL_CODES AS ELEMENT_SPECIAL_CODES,	",
			"	  B.LAST_NAME AS LAST_NAME,	",
			"	  B.FIRST_NAME AS FIRST_NAME,	",
			"	  B.MIDDLE_NAME AS MIDDLE_NAME,	",
			"	  B.BIRTHDATE AS BIRTHDATE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 21, B.STUDENT_BIO_ID) AS TEST_FORM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 205, B.STUDENT_BIO_ID) AS FORM_CHANGE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 20, B.STUDENT_BIO_ID) AS TEST_LANGUAGE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 22, B.STUDENT_BIO_ID) AS TEST_MODE,	",
			"	  G.GENDER_CODE AS GENDER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 24, B.STUDENT_BIO_ID) AS RESOLVED_ETHNICITY,	",
			"	  B.EXT_STUDENT_ID AS EXAMINEE_ID,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 25, B.STUDENT_BIO_ID) AS EXAMINEE_PHONE,	",
			"	  B.LITHOCODE AS LITHOCODE,	",
			"	  B.INT_STUDENT_ID AS IMAGING_ID,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 26, B.STUDENT_BIO_ID) AS ADDRESS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 27, B.STUDENT_BIO_ID) AS APT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 28, B.STUDENT_BIO_ID) AS CITY,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 29, B.STUDENT_BIO_ID) AS STATE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 30, B.STUDENT_BIO_ID) AS ZIP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 31, B.STUDENT_BIO_ID) AS COUNTY_CODE,	",
			"	  B.EDU_CENTERID AS EDUCATION_CENTER_CODE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 115, B.STUDENT_BIO_ID) AS RD_AUDIO_ALT_PRESN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 122, B.STUDENT_BIO_ID) AS WR_AUDIO_ALT_PRESN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 101, B.STUDENT_BIO_ID) AS MATH_PART1_AUDIO_ALT_PRESN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 108, B.STUDENT_BIO_ID) AS MATH_PART2_AUDIO_ALT_PRESN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 129, B.STUDENT_BIO_ID) AS SCI_AUDIO_ALT_PRESN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 136, B.STUDENT_BIO_ID) AS SS_AUDIO_ALT_PRESN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 116, B.STUDENT_BIO_ID) AS RD_BREAKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 123, B.STUDENT_BIO_ID) AS WR_BREAKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 102, B.STUDENT_BIO_ID) AS MATH_PART1_BREAKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 109, B.STUDENT_BIO_ID) AS MATH_PART2_BREAKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 130, B.STUDENT_BIO_ID) AS SCI_BREAKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 137, B.STUDENT_BIO_ID) AS SS_BREAKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 215, B.STUDENT_BIO_ID) AS RD_CALCULATOR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 220, B.STUDENT_BIO_ID) AS WR_CALCULATOR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 206, B.STUDENT_BIO_ID) AS MATH_PART1_CALCULATOR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 229, B.STUDENT_BIO_ID) AS SS_CALCULATOR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 216, B.STUDENT_BIO_ID) AS RD_DUR_1_25T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 221, B.STUDENT_BIO_ID) AS WR_DUR_1_25T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 207, B.STUDENT_BIO_ID) AS MATH_PART1_DUR_1_25T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 211, B.STUDENT_BIO_ID) AS MATH_PART2_DUR_1_25T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 225, B.STUDENT_BIO_ID) AS SCI_DUR_1_25T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 230, B.STUDENT_BIO_ID) AS SS_DUR_1_25T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 117, B.STUDENT_BIO_ID) AS RD_DUR_1_5T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 124, B.STUDENT_BIO_ID) AS WR_DUR_1_5T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 103, B.STUDENT_BIO_ID) AS MATH_PART1_DUR_1_5T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 110, B.STUDENT_BIO_ID) AS MATH_PART2_DUR_1_5T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 131, B.STUDENT_BIO_ID) AS SCI_DUR_1_5T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 138, B.STUDENT_BIO_ID) AS SS_DUR_1_5T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 217, B.STUDENT_BIO_ID) AS RD_DUR_2T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 222, B.STUDENT_BIO_ID) AS WR_DUR_2T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 208, B.STUDENT_BIO_ID) AS MATH_PART1_DUR_2T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 212, B.STUDENT_BIO_ID) AS MATH_PART2_DUR_2T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 226, B.STUDENT_BIO_ID) AS SCI_DUR_2T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 231, B.STUDENT_BIO_ID) AS SS_DUR_2T,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 118, B.STUDENT_BIO_ID) AS RD_PHYSICAL_SUPP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 125, B.STUDENT_BIO_ID) AS WR_PHYSICAL_SUPP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 104, B.STUDENT_BIO_ID) AS MATH_PART1_PHYSICAL_SUPP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 111, B.STUDENT_BIO_ID) AS MATH_PART2_PHYSICAL_SUPP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 132, B.STUDENT_BIO_ID) AS SCI_PHYSICAL_SUPP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 139, B.STUDENT_BIO_ID) AS SS_PHYSICAL_SUPP,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 218, B.STUDENT_BIO_ID) AS RD_SCRIBE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 223, B.STUDENT_BIO_ID) AS WR_SCRIBE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 209, B.STUDENT_BIO_ID) AS MATH_PART1_SCRIBE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 213, B.STUDENT_BIO_ID) AS MATH_PART2_SCRIBE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 227, B.STUDENT_BIO_ID) AS SCI_SCRIBE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 232, B.STUDENT_BIO_ID) AS SS_SCRIBE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 219, B.STUDENT_BIO_ID) AS RD_TECNOLOGY_DEVICE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 224, B.STUDENT_BIO_ID) AS WR_TECHNOLOGY_DEVICE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 210, B.STUDENT_BIO_ID) AS MATH_PART1_TECNOLOGY_DEVICE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 214, B.STUDENT_BIO_ID) AS MATH_PART2_TECNOLOGY_DEVICE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 228, B.STUDENT_BIO_ID) AS SCI_TECHNOLOGY_DEVICE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 233, B.STUDENT_BIO_ID) AS SS_TECHNOLOGY_DEVICE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 119, B.STUDENT_BIO_ID) AS RD_SEPARATE_ROOM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 126, B.STUDENT_BIO_ID) AS WR_SEPARATE_ROOM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 105, B.STUDENT_BIO_ID) AS MATH_PART1_SEPARATE_ROOM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 112, B.STUDENT_BIO_ID) AS MATH_PART2_SEPARATE_ROOM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 133, B.STUDENT_BIO_ID) AS SCI_SEPARATE_ROOM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 140, B.STUDENT_BIO_ID) AS SS_SEPARATE_ROOM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 120, B.STUDENT_BIO_ID) AS RD_SMALL_GRP_SETTING,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 127, B.STUDENT_BIO_ID) AS WR_SMALL_GRP_SETTING,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 106, B.STUDENT_BIO_ID) AS MATH_PART1_SMALL_GRP_SETTING,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 113, B.STUDENT_BIO_ID) AS MATH_PART2_SMALL_GRP_SETTING,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 134, B.STUDENT_BIO_ID) AS SCI_SMALL_GRP_SETTING,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 141, B.STUDENT_BIO_ID) AS SS_SMALL_GRP_SETTING,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 121, B.STUDENT_BIO_ID) AS RD_OTHER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 128, B.STUDENT_BIO_ID) AS WR_OTHER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 107, B.STUDENT_BIO_ID) AS MATH_PART1_OTHER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 114, B.STUDENT_BIO_ID) AS MATH_PART2_OTHER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 135, B.STUDENT_BIO_ID) AS SCI_OTHER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 142, B.STUDENT_BIO_ID) AS SS_OTHER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 97, B.STUDENT_BIO_ID) AS WAIVER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 76, B.STUDENT_BIO_ID) AS HOME_LANGUAGE,	",
			"	  ' ' AS K_12_EDUC_COMPLETED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 43, B.STUDENT_BIO_ID) AS RD_PASSED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 46, B.STUDENT_BIO_ID) AS WR_PASSED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 49, B.STUDENT_BIO_ID) AS MATH_PASSED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 52, B.STUDENT_BIO_ID) AS SCI_PASSED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 55, B.STUDENT_BIO_ID) AS SS_PASSED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 58, B.STUDENT_BIO_ID) AS NONE_PASSED,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 44, B.STUDENT_BIO_ID) AS RD_T_TAKEN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 47, B.STUDENT_BIO_ID) AS WR_T_TAKEN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 50, B.STUDENT_BIO_ID) AS MATH_T_TAKEN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 53, B.STUDENT_BIO_ID) AS SCI_T_TAKEN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 56, B.STUDENT_BIO_ID) AS SS_T_TAKEN,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 45, B.STUDENT_BIO_ID) AS RD_RETAKE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 48, B.STUDENT_BIO_ID) AS WR_RETAKE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 51, B.STUDENT_BIO_ID) AS MATH_RETAKE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 54, B.STUDENT_BIO_ID) AS SCI_RETAKE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 57, B.STUDENT_BIO_ID) AS SS_RETAKE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 59, B.STUDENT_BIO_ID) AS NONE_RETAKE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 41, B.STUDENT_BIO_ID) AS READINESS_ASSESSMENT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 32, B.STUDENT_BIO_ID) AS COUNTY_PROGRAM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 33, B.STUDENT_BIO_ID) AS DISTRICT_PROGRAM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 34, B.STUDENT_BIO_ID) AS MILITARY_PROGRAM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 35, B.STUDENT_BIO_ID) AS RELIGIOUS_PROGRAM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 36, B.STUDENT_BIO_ID) AS PURCHASED_BOOKS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 37, B.STUDENT_BIO_ID) AS ONLINE_STUDY_PROGRAM,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 38, B.STUDENT_BIO_ID) AS HOMESCHOOL,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 39, B.STUDENT_BIO_ID) AS TUTOR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 40, B.STUDENT_BIO_ID) AS SELF_TAUGHT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 60, B.STUDENT_BIO_ID) AS RD_PAST_3MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 63, B.STUDENT_BIO_ID) AS WR_PAST_3MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 66, B.STUDENT_BIO_ID) AS MATH_PAST_3MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 69, B.STUDENT_BIO_ID) AS SCI_PAST_3MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 72, B.STUDENT_BIO_ID) AS SS_PAST_3MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 61, B.STUDENT_BIO_ID) AS RD_HOW_MANY_MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 64, B.STUDENT_BIO_ID) AS WR_HOW_MANY_MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 67, B.STUDENT_BIO_ID) AS MATH_HOW_MANY_MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 70, B.STUDENT_BIO_ID) AS SCI_HOW_MANY_MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 73, B.STUDENT_BIO_ID) AS SS_HOW_MANY_MONTHS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 62, B.STUDENT_BIO_ID) AS RD_WHAT_GRADE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 65, B.STUDENT_BIO_ID) AS WR_WHAT_GRADE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 68, B.STUDENT_BIO_ID) AS MATH_WHAT_GRADE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 71, B.STUDENT_BIO_ID) AS SCI_WHAT_GRADE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 74, B.STUDENT_BIO_ID) AS SS_WHAT_GRADE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 204, B.STUDENT_BIO_ID) AS BRAILLE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 204, B.STUDENT_BIO_ID) AS LARGE_PRINT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 77, B.STUDENT_BIO_ID) AS LOCAL_USE_1,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 78, B.STUDENT_BIO_ID) AS LOCAL_USE_2,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 79, B.STUDENT_BIO_ID) AS LOCAL_USE_3,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 80, B.STUDENT_BIO_ID) AS LOCAL_USE_4,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 81, B.STUDENT_BIO_ID) AS LOCAL_USE_5,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 82, B.STUDENT_BIO_ID) AS LOCAL_USE_6,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 83, B.STUDENT_BIO_ID) AS LOCAL_USE_7,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 84, B.STUDENT_BIO_ID) AS LOCAL_USE_8,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 85, B.STUDENT_BIO_ID) AS LOCAL_USE_9,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 86, B.STUDENT_BIO_ID) AS LOCAL_USE_10,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 87, B.STUDENT_BIO_ID) AS LOCAL_USE_11,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 88, B.STUDENT_BIO_ID) AS LOCAL_USE_12,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 89, B.STUDENT_BIO_ID) AS LOCAL_USE_13,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 90, B.STUDENT_BIO_ID) AS LOCAL_USE_14,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 91, B.STUDENT_BIO_ID) AS LOCAL_USE_15,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 92, B.STUDENT_BIO_ID) AS LOCAL_USE_16,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 93, B.STUDENT_BIO_ID) AS LOCAL_USE_17,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 94, B.STUDENT_BIO_ID) AS LOCAL_USE_18,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 95, B.STUDENT_BIO_ID) AS LOCAL_USE_19,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 96, B.STUDENT_BIO_ID) AS LOCAL_USE_20,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 98, B.STUDENT_BIO_ID) AS ACKNOWLEDGEMENT,	",
			"	  COMVERT(varchar(10), SSF.TEST_DATE, 101) AS DATE_TEST_TAKEN_RD_SUBTEST,	",
			"	  SSF.NCR AS RD_NUMBER_CORRECT,	",
			"	  SSF.SS AS RD_SCALE_SCORE,	",
			"	  SSF.HSE AS RD_HIGH_SC_EQ,	",
			"	  SSF.PR AS RD_PERCENTILE_RANK,	",
			"	  SSF.NCE AS RD_NORMAL_CURVE_EQ,	",
			"	  OSF.PL AS RD_OBJ_1_MASTERY,	",
			"	  '*' AS RD_OBJ_1_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_2_MASTERY,	",
			"	  '*' AS RD_OBJ_2_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_3_MASTERY,	",
			"	  '*' AS RD_OBJ_3_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_4_MASTERY,	",
			"	  '*' AS RD_OBJ_4_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_5_MASTERY,	",
			"	  '*' AS RD_OBJ_5_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_6_MASTERY,	",
			"	  '*' AS RD_OBJ_6_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_7_MASTERY,	",
			"	  '*' AS RD_OBJ_7_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS RD_OBJ_8_MASTERY,	",
			"	  '*' AS RD_OBJ_8_NOT_ALL_ATMPT_FLAG,	",
			"	  CONVERT(varchar(10), SSF.TEST_DATE, 101) AS DATE_TEST_TAKEN_WR_SUBTEST,	",
			"	  SSF.NCR AS WR_NUMBER_CORRECT,	",
			"	  SSF.SS AS WR_SCALE_SCORE,	",
			"	  SSF.HSE AS WR_HIGH_SC_EQ,	",
			"	  SSF.PR AS WR_PERCENTILE_RANK,	",
			"	  SSF.NCE AS WR_NORMAL_CURVE_EQ,	",
			"	  OSF.PL AS WR_OBJ_1_MASTERY,	",
			"	  '*' AS WR_OBJ_1_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS WR_OBJ_2_MASTERY,	",
			"	  '*' AS WR_OBJ_2_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS WR_OBJ_3_MASTERY,	",
			"	  '*' AS WR_OBJ_3_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS WR_OBJ_4_MASTERY,	",
			"	  '*' AS WR_OBJ_4_NOT_ALL_ATMPT_FLAG,	",
			"	  ' ' AS ELA_SCALE_SCORE,	",
			"	  ' ' AS ELA_HIGH_SC_EQ,	",
			"	  ' ' AS ELA_PERCENTILE_RANK,	",
			"	  ' ' AS ELA_NORMAL_CURVE_EQ,	",
			"	  CONVERT(varchar(10), SSF.TEST_DATE, 101) AS DATE_TEST_TAKEN_MATH_SUBTEST,	",
			"	  SSF.NCR AS MATH_NUMBER_CORRECT,	",
			"	  SSF.SS AS MATH_SCALE_SCORE,	",
			"	  SSF.HSE AS MATH_HIGH_SC_EQ,	",
			"	  SSF.PR AS MATH_PERCENTILE_RANK,	",
			"	  SSF.NCE AS MATH_NORMAL_CURVE_EQ,	",
			"	  OSF.PL AS MATH_OBJ_1_MASTERY,	",
			"	  '*' AS MATH_OBJ_1_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS MATH_OBJ_2_MASTERY,	",
			"	  '*' AS MATH_OBJ_2_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS MATH_OBJ_3_MASTERY,	",
			"	  '*' AS MATH_OBJ_3_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS MATH_OBJ_4_MASTERY,	",
			"	  '*' AS MATH_OBJ_4_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS MATH_OBJ_5_MASTERY,	",
			"	  '*' AS MATH_OBJ_5_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS MATH_OBJ_6_MASTERY,	",
			"	  '*' AS MATH_OBJ_6_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS MATH_OBJ_7_MASTERY,	",
			"	  '*' AS MATH_OBJ_7_NOT_ALL_ATMPT_FLAG,	",
			"	  CONVERT(varchar(10), SSF.TEST_DATE, 101) AS DATE_TEST_TAKEN_SCI_SUBTEST,	",
			"	  SSF.NCR AS SCI_NUMBER_CORRECT,	",
			"	  SSF.SS AS SCI_SCALE_SCORE,	",
			"	  SSF.HSE AS SCI_HIGH_SC_EQ,	",
			"	  SSF.PR AS SCI_PERCENTILE_RANK,	",
			"	  SSF.NCE AS SCI_NORMAL_CURVE_EQ,	",
			"	  OSF.PL AS SCI_OBJ_1_MASTERY,	",
			"	  '*' AS SCI_OBJ_1_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SCI_OBJ_2_MASTERY,	",
			"	  '*' AS SCI_OBJ_2_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SCI_OBJ_3_MASTERY,	",
			"	  '*' AS SCI_OBJ_3_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SCI_OBJ_4_MASTERY,	",
			"	  '*' AS SCI_OBJ_4_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SCI_OBJ_5_MASTERY,	",
			"	  '*' AS SCI_OBJ_5_NOT_ALL_ATMPT_FLAG,	",
			"	  CONVERT(varchar(10), SSF.TEST_DATE, 101) AS DATE_TEST_TAKEN_SS_SUBTEST,	",
			"	  SSF.NCR AS SOC_NUMBER_CORRECT,	",
			"	  SSF.SS AS SOC_SCALE_SCORE,	",
			"	  SSF.HSE AS SOC_HIGH_SC_EQ,	",
			"	  SSF.PR AS SOC_PERCENTILE_RANK,	",
			"	  SSF.NCE AS SOC_NORMAL_CURVE_EQ,	",
			"	  OSF.PL AS SOC_OBJ_1_MASTERY,	",
			"	  '*' AS SOC_OBJ_1_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SOC_OBJ_2_MASTERY,	",
			"	  '*' AS SOC_OBJ_2_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SOC_OBJ_3_MASTERY,	",
			"	  '*' AS SOC_OBJ_3_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SOC_OBJ_4_MASTERY,	",
			"	  '*' AS SOC_OBJ_4_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SOC_OBJ_5_MASTERY,	",
			"	  '*' AS SOC_OBJ_5_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SOC_OBJ_6_MASTERY,	",
			"	  '*' AS SOC_OBJ_6_NOT_ALL_ATMPT_FLAG,	",
			"	  OSF.PL AS SOC_OBJ_7_MASTERY,	",
			"	  '*' AS SOC_OBJ_7_NOT_ALL_ATMPT_FLAG,	",
			"	  'N/A' AS OVR_COMP_SCORE_SCALE_SCORE,	",
			"	  'N/A' AS OVR_COMP_SCORE_HIGH_SC_EQ,	",
			"	  'N/A' AS OVR_COMP_SCORE_PERCENTILE_RANK,	",
			"	  'N/A' AS OVR_COMP_SCORE_NORMAL_CURVE_EQ,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 143, B.STUDENT_BIO_ID) AS RD_SCREEN_READER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 148, B.STUDENT_BIO_ID) AS RD_ONLINE_CALC,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 153, B.STUDENT_BIO_ID) AS RD_TEST_PAUSE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 158, B.STUDENT_BIO_ID) AS RD_HIGHLIGHTER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 163, B.STUDENT_BIO_ID) AS RD_BLOCKING_RULER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 168, B.STUDENT_BIO_ID) AS RD_MAGNIFYING_GLASS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 173, B.STUDENT_BIO_ID) AS RD_FONT_AND_BKGND_CLR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 178, B.STUDENT_BIO_ID) AS RD_LARGE_FONT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 183, B.STUDENT_BIO_ID) AS RD_MUSIC_PLAYER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 188, B.STUDENT_BIO_ID) AS RD_EXTENDED_TIME,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 193, B.STUDENT_BIO_ID) AS RD_MASKING_TOOL,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 144, B.STUDENT_BIO_ID) AS WR_SCREEN_READER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 149, B.STUDENT_BIO_ID) AS WR_ONLINE_CALC,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 154, B.STUDENT_BIO_ID) AS WR_TEST_PAUSE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 159, B.STUDENT_BIO_ID) AS WR_HIGHLIGHTER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 164, B.STUDENT_BIO_ID) AS WR_BLOCKING_RULER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 169, B.STUDENT_BIO_ID) AS WR_MAGNIFYING_GLASS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 174, B.STUDENT_BIO_ID) AS WR_FONT_AND_BKGND_CLR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 179, B.STUDENT_BIO_ID) AS WR_LARGE_FONT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 184, B.STUDENT_BIO_ID) AS WR_MUSIC_PLAYER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 189, B.STUDENT_BIO_ID) AS WR_EXTENDED_TIME,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 194, B.STUDENT_BIO_ID) AS WR_MASKING_TOOL,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 145, B.STUDENT_BIO_ID) AS MATH_SCREEN_READER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 150, B.STUDENT_BIO_ID) AS MATH_ONLINE_CALC,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 155, B.STUDENT_BIO_ID) AS MATH_TEST_PAUSE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 160, B.STUDENT_BIO_ID) AS MATH_HIGHLIGHTER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 165, B.STUDENT_BIO_ID) AS MATH_BLOCKING_RULER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 170, B.STUDENT_BIO_ID) AS MATH_MAGNIFYING_GLASS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 175, B.STUDENT_BIO_ID) AS MATH_FONT_AND_BKGND_CLR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 180, B.STUDENT_BIO_ID) AS MATH_LARGE_FONT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 185, B.STUDENT_BIO_ID) AS MATH_MUSIC_PLAYER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 190, B.STUDENT_BIO_ID) AS MATH_EXTENDED_TIME,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 195, B.STUDENT_BIO_ID) AS MATH_MASKING_TOOL,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 146, B.STUDENT_BIO_ID) AS SCI_SCREEN_READER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 151, B.STUDENT_BIO_ID) AS SCI_ONLINE_CALC,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 156, B.STUDENT_BIO_ID) AS SCI_TEST_PAUSE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 161, B.STUDENT_BIO_ID) AS SCI_HIGHLIGHTER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 166, B.STUDENT_BIO_ID) AS SCI_BLOCKING_RULER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 171, B.STUDENT_BIO_ID) AS SCI_MAGNIFYING_GLASS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 176, B.STUDENT_BIO_ID) AS SCI_FONT_AND_BKGND_CLR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 181, B.STUDENT_BIO_ID) AS SCI_LARGE_FONT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 186, B.STUDENT_BIO_ID) AS SCI_MUSIC_PLAYER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 191, B.STUDENT_BIO_ID) AS SCI_EXTENDED_TIME,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 196, B.STUDENT_BIO_ID) AS SCI_MASKING_TOOL,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 147, B.STUDENT_BIO_ID) AS SS_SCREEN_READER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 152, B.STUDENT_BIO_ID) AS SS_ONLINE_CALC,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 157, B.STUDENT_BIO_ID) AS SS_TEST_PAUSE,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 162, B.STUDENT_BIO_ID) AS SS_HIGHLIGHTER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 167, B.STUDENT_BIO_ID) AS SS_BLOCKING_RULER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 172, B.STUDENT_BIO_ID) AS SS_MAGNIFYING_GLASS,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 177, B.STUDENT_BIO_ID) AS SS_FONT_AND_BKGND_CLR,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 182, B.STUDENT_BIO_ID) AS SS_LARGE_FONT,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 187, B.STUDENT_BIO_ID) AS SS_MUSIC_PLAYER,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 192, B.STUDENT_BIO_ID) AS SS_EXTENDED_TIME,	",
			"	  FACT.FN_GET_STUDENT_DEMO_VALUE(SDV.DEMO_VALID, 197, B.STUDENT_BIO_ID) AS SS_MASKING_TOOL,	",
			"	  ISF.SCORE_VALUES AS RD_SR_RESPONSE,	",
			"	  ' ' AS RD_ITEMS_FU,	",
			"	  ISF.SCORE_VALUES AS WR_SR_RESPONSE,	",
			"	  ' ' AS WR_ITEM_CODE_CR,	",
			"	  ' ' AS WR_ITEMS_FU,	",
			"	  ISF.SCORE_VALUES AS MATH_ITEM_CODE_SR,	",
			"	  ' ' AS MATH_ITEM_CODE_GR_STATUS,	",
			"	  ' ' AS MATH_ITEM_CODE_GR_EDITED,	",
			"	  ' ' AS MATH_ITEMS_FU,	",
			"	  ISF.SCORE_VALUES AS SCI_SR_RESPONSE,	",
			"	  ' ' AS SCI_ITEMS_FU,	",
			"	  ISF.SCORE_VALUES AS SOC_SR_RESPONSE,	",
			"	  ' ' AS SS_FU,	",
			"	  ' ' AS CTB_USE_FIELD	",
			"	FROM USERS U	",
			"	JOIN h$cte N	",
			"	  ON U.CUSTOMERID = N.CUSTOMERID	",
			"	JOIN STUDENT_BIO_DIM B	",
			"	  ON N.ORG_NODEID = B.ORG_NODEID	",
			"	JOIN GENDER_DIM G	",
			"	  ON B.GENDERID = G.GENDERID	",
			"	JOIN STUDENT_DEMO_VALUES SDV	",
			"	  ON B.STUDENT_BIO_ID = SDV.STUDENT_BIO_ID	",
			"	JOIN SUBTEST_SCORE_FACT SSF	",
			"	  ON B.STUDENT_BIO_ID = SSF.STUDENT_BIO_ID	",
			"	JOIN OBJECTIVE_SCORE_FACT OSF	",
			"	  ON B.STUDENT_BIO_ID = OSF.STUDENT_BIO_ID	",
			"	LEFT JOIN ITEM_SCORE_FACT ISF	",
			"	  ON B.STUDENT_BIO_ID = ISF.STUDENT_BIO_ID	",
			"	WHERE U.USERID = 100	",
			"	AND ((SSF.TEST_DATE > CONVERT(datetime2(0), '01-Jan-1913', 113)	",
			"	AND SSF.TEST_DATE < CONVERT(datetime2(0), '30-Oct-2013', 113))	",
			"	OR (OSF.TEST_DATE > CONVERT(datetime2(0), '01-Jan-1913', 113)	",
			"	AND OSF.TEST_DATE < CONVERT(datetime2(0), '30-Oct-2013', 113)))	",
			"	AND B.STUDENT_BIO_ID IN (23739415, 23739416, 23739419, 23739420)	");
	
	public static final String GET_EDUCATION_CENTER_ALL = CustomStringUtil.appendString(
			"SELECT ECD.EDU_CENTERID VALUE, ECD.EDU_CENTER_NAME NAME, ECD.STATE OTHER",
			" FROM EDU_CENTER_DETAILS ECD",
			" WHERE ECD.CUSTOMERID = ?");
	
	public static final String GET_EDUCATION_CENTER = CustomStringUtil.appendString(
			"SELECT ECD.EDU_CENTERID VALUE, ECD.EDU_CENTER_NAME NAME, ECD.STATE OTHER",
			" FROM EDU_CENTER_DETAILS ECD, EDU_CENTER_USER_LINK ECUL",
			" WHERE ECD.EDU_CENTERID = ECUL.EDU_CENTERID",
			" AND ECD.CUSTOMERID = ?",
			" AND ECUL.USERID = ?");
	
	public static final String GET_EDU_CENTER_USERS_FIRST_LOAD = CustomStringUtil.appendString(
			" 	SELECT	",
			" 	  USR_TAB.USERROWID AS USERROWID,	",
			" 	  USR_TAB.EDU_CENTERID EDU_CENTERID,	",
			" 	  USR_TAB.EDU_CENTER_CODE EDU_CENTER_CODE,	",
			" 	  USR_TAB.EDU_CENTER_NAME EDU_CENTER_NAME,	",
			" 	  USR_TAB.USERID USERID,	",
			" 	  USR_TAB.USERNAME USERNAME,	",
			" 	  USR_TAB.FULLNAME AS FULLNAME,	",
			" 	  USR_TAB.STATUS STATUS	",
			" 	FROM (SELECT	",
			" 	  CAST(U.USERID AS varchar(18)) AS USERROWID,	",
			" 	  ECD.EDU_CENTERID EDU_CENTERID,	",
			" 	  ECD.EDU_CENTER_CODE EDU_CENTER_CODE,	",
			" 	  ECD.EDU_CENTER_NAME EDU_CENTER_NAME,	",
			" 	  U.USERID USERID,	",
			" 	  U.USERNAME USERNAME,	",
			" 	  ISNULL(U.LAST_NAME, '') + '  ' + ISNULL(U.FIRST_NAME, '') AS FULLNAME,	",
			" 	  U.ACTIVATION_STATUS STATUS,	",
			" 	  ROW_NUMBER() OVER (ORDER BY UPPER(USERNAME)) AS ROWNUM	",
			" 	FROM EDU_CENTER_DETAILS ECD,	",
			" 	     EDU_CENTER_USER_LINK ECUL,	",
			" 	     USERS U	",
			" 	WHERE ECD.EDU_CENTERID = ECUL.EDU_CENTERID	",
			" 	AND ECUL.USERID = U.USERID	",
			" 	AND ECD.CUSTOMERID = ?	",
			" 	AND ECD.EDU_CENTERID = ?) USR_TAB	",
			" 	WHERE ROWNUM <= ?	",
			" 	ORDER BY UPPER(USERNAME)	");
	
	public static final String GET_EDU_CENTER_USERS_ON_SCROLL = CustomStringUtil.appendString(
			" 	SELECT	",
			" 	  USR_TAB.USERROWID AS USERROWID,	",
			" 	  USR_TAB.EDU_CENTERID EDU_CENTERID,	",
			" 	  USR_TAB.EDU_CENTER_CODE EDU_CENTER_CODE,	",
			" 	  USR_TAB.EDU_CENTER_NAME EDU_CENTER_NAME,	",
			" 	  USR_TAB.USERID USERID,	",
			" 	  USR_TAB.USERNAME USERNAME,	",
			" 	  USR_TAB.FULLNAME AS FULLNAME,	",
			" 	  USR_TAB.STATUS STATUS	",
			" 	FROM (SELECT	",
			" 	  CAST(U.USERID AS varchar(18)) AS USERROWID,	",
			" 	  ECD.EDU_CENTERID EDU_CENTERID,	",
			" 	  ECD.EDU_CENTER_CODE EDU_CENTER_CODE,	",
			" 	  ECD.EDU_CENTER_NAME EDU_CENTER_NAME,	",
			" 	  U.USERID USERID,	",
			" 	  U.USERNAME USERNAME,	",
			" 	  ISNULL(U.LAST_NAME, '') + '  ' + ISNULL(U.FIRST_NAME, '') AS FULLNAME,	",
			" 	  U.ACTIVATION_STATUS STATUS,	",
			" 	  ROW_NUMBER() OVER (ORDER BY UPPER(USERNAME)) AS ROWNUM	",
			" 	FROM EDU_CENTER_DETAILS ECD,	",
			" 	     EDU_CENTER_USER_LINK ECUL,	",
			" 	     USERS U	",
			" 	WHERE ECD.EDU_CENTERID = ECUL.EDU_CENTERID	",
			" 	AND ECUL.USERID = U.USERID	",
			" 	AND ECD.CUSTOMERID = ?	",
			" 	AND ECD.EDU_CENTERID = ?	",
			" 	AND UPPER(U.USERNAME) > UPPER(?)) USR_TAB	",
			" 	WHERE ROWNUM <= ?	",
			" 	ORDER BY UPPER(USERNAME)	");
	
	public static final String GET_EDU_CENTER_USERS_ON_SCROLL_SEARCH = CustomStringUtil.appendString(
			" 	SELECT	",
			" 	  USR_TAB.USERROWID AS USERROWID,	",
			" 	  USR_TAB.EDU_CENTERID EDU_CENTERID,	",
			" 	  USR_TAB.EDU_CENTER_CODE EDU_CENTER_CODE,	",
			" 	  USR_TAB.EDU_CENTER_NAME EDU_CENTER_NAME,	",
			" 	  USR_TAB.USERID USERID,	",
			" 	  USR_TAB.USERNAME USERNAME,	",
			" 	  USR_TAB.FULLNAME AS FULLNAME,	",
			" 	  USR_TAB.STATUS STATUS	",
			" 	FROM (SELECT	",
			" 	  CAST(U.USERID AS varchar(18)) AS USERROWID,	",
			" 	  ECD.EDU_CENTERID EDU_CENTERID,	",
			" 	  ECD.EDU_CENTER_CODE EDU_CENTER_CODE,	",
			" 	  ECD.EDU_CENTER_NAME EDU_CENTER_NAME,	",
			" 	  U.USERID USERID,	",
			" 	  U.USERNAME USERNAME,	",
			" 	  ISNULL(U.LAST_NAME, '') + '  ' + ISNULL(U.FIRST_NAME, '') AS FULLNAME,	",
			" 	  U.ACTIVATION_STATUS STATUS,	",
			" 	  ROW_NUMBER() OVER (ORDER BY UPPER(USERNAME)) AS ROWNUM	",
			" 	FROM EDU_CENTER_DETAILS ECD,	",
			" 	     EDU_CENTER_USER_LINK ECUL,	",
			" 	     USERS U	",
			" 	WHERE ECD.EDU_CENTERID = ECUL.EDU_CENTERID	",
			" 	AND ECUL.USERID = U.USERID	",
			" 	AND ECD.CUSTOMERID = ?	",
			" 	AND ECD.EDU_CENTERID = ?	",
			" 	AND (UPPER(U.USERNAME) LIKE UPPER(?)	",
			" 	OR UPPER(U.LAST_NAME) LIKE UPPER(?)	",
			" 	OR UPPER(U.FIRST_NAME) LIKE UPPER(?))	",
			" 	AND UPPER(U.USERNAME) > UPPER(?)) USR_TAB	",
			" 	WHERE ROWNUM <= ?	",
			" 	ORDER BY UPPER(USERNAME)	");
	
	public static final String DELETE_EDU_USER = CustomStringUtil.appendString(
			"DELETE FROM EDU_CENTER_USER_LINK WHERE USERID = ?");

	//public static final String GET_USER_TYPE = "SELECT DECODE(COUNT(1), 0, 'O', 'P') AS USER_TYPE FROM USERS U, ORG_USERS OU WHERE U.USERID=OU.USERID AND UPPER(U.USERNAME) = UPPER(?) AND OU.ORG_NODE_LEVEL=0";
	public static final String GET_USER_TYPE = "select u.userid from users u, user_role ur, role r where u.userid = ur.userid and ur.roleid = r.roleid and r.role_name = 'ROLE_PARENT' and UPPER(U.USERNAME) = UPPER(?)";
	public static final String GET_EDU_USER_TYPE = "SELECT USERID FROM EDU_CENTER_USER_LINK WHERE USERID IN (SELECT USERID FROM USERS WHERE UPPER(USERNAME) = UPPER(?))"; 

	public static final String GET_TEST_ADMINISTRATIONS_GD = "SELECT ADMINID VALUE, ADMIN_NAME NAME, ADMIN_SEQ OTHER FROM ADMIN_DIM ORDER BY ADMIN_SEQ";
	public static final String GET_DISTRICTS_GD = "SELECT DISTINCT ORG_NODEID VALUE, ORG_NODE_NAME NAME, ORG_NODE_LEVEL OTHER FROM ORG_NODE_DIM WHERE ORG_NODE_LEVEL = 2 AND ORG_MODE = ? ORDER BY ORG_NODE_NAME";
	public static final String GET_SCHOOLS_GD = "SELECT DISTINCT ORG_NODEID VALUE, ORG_NODE_NAME NAME, ORG_NODE_LEVEL OTHER FROM ORG_NODE_DIM WHERE ORG_NODE_LEVEL = 3 AND ORG_MODE = ? AND PARENT_ORG_NODEID = ? ORDER BY ORG_NODE_NAME";
	public static final String GET_CLASSES_GD = "SELECT DISTINCT ORG_NODEID VALUE, ORG_NODE_NAME NAME, ORG_NODE_LEVEL OTHER FROM ORG_NODE_DIM WHERE ORG_NODE_LEVEL = 4 AND ORG_MODE = ? AND PARENT_ORG_NODEID = ? ORDER BY ORG_NODE_NAME";
	public static final String GET_GRADES_GD = "SELECT GRADEID VALUE, GRADE_NAME NAME, GRADE_SEQ OTHER FROM GRADE_DIM ORDER BY GRADE_SEQ";
	public static final String GET_ALL_GRADES = "SELECT VC1 AS GRADEID FROM FACT.SF_GET_GRADE_DWNLD(?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_ALL_GRADE_NAMES = "SELECT VC2 AS GRADE_NAME FROM FACT.SF_GET_GRADE_DWNLD(?, ?, ?, ?, ?, ?, ?, ?) WHERE VC1 != -1 ORDER BY 1";
	public static final String GET_SELECTED_CLASS_NAMES = CustomStringUtil.appendString(
			/*"SELECT ALL_ORGS.ORG_NODE_NAME FROM",
			" (SELECT VC1 AS ORG_NODEID,  VC2 AS ORG_NODE_NAME FROM",
			" TABLE(SELECT SF_GET_CLASS(?, -99, -999, -99, -99, -99, ?, 'ADD_ALL') FROM DUAL) WHERE VC1 != -1 ORDER BY 1) ALL_ORGS,",
			" (SELECT DISTINCT ORG_NODEID FROM STUDENT_BIO_DIM WHERE STUDENT_BIO_ID IN (~)) STUDENT_ORGS",
			" WHERE ALL_ORGS.ORG_NODEID=STUDENT_ORGS.ORG_NODEID ORDER BY ALL_ORGS.ORG_NODE_NAME"*/
			"SELECT DISTINCT OND.ORG_NODE_NAME FROM ORG_NODE_DIM OND, STUDENT_BIO_DIM SBD",
			" WHERE OND.ORG_NODEID = SBD.ORG_NODEID AND OND.ORG_NODE_LEVEL = 4 AND SBD.STUDENT_BIO_ID IN (~)",
			" ORDER BY OND.ORG_NODE_NAME"
			);
	
	public static final String GET_IC_FILE_PATHS = "SELECT ICID VALUE, FILENAME NAME FROM INVITATION_CODE WHERE STUDENT_BIO_ID IN (?)";
	/**
	 * All 6 Params are STUDENT_BIO_ID
	 */
	public static final String GET_IC_FILE_PATH = CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  ORG_NODE_NAME,	",
			"	  ORG_NODE_LEVEL,	",
			"	  PARENT_ORG_NODEID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = (SELECT	",
			"	  ORG_NODEID	",
			"	FROM STUDENT_BIO_DIM	",
			"	WHERE STUDENT_BIO_ID = ?)	",
			"	UNION ALL	",
			"	SELECT	",
			"	  O.ORG_NODEID,	",
			"	  O.ORG_NODE_NAME,	",
			"	  O.ORG_NODE_LEVEL,	",
			"	  O.PARENT_ORG_NODEID,	",
			"	  h$cte.path + CONVERT(varchar(max), O.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM O	",
			"	JOIN h$cte	",
			"	  ON h$cte.PARENT_ORG_NODEID = O.ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), O.ORG_NODEID) + '|%')	",
			"	SELECT	",
			"	  IC.FILENAME IC_FILE_LOC,	",
			"	  P_NAME.FILE_NAME IC_FILE_NAME	",
			"	FROM (SELECT DISTINCT	",
			"	       STUDENT_BIO_ID,	",
			"	       FILENAME	",
			"	     FROM INVITATION_CODE) IC,	",
			"	     (SELECT	",
			"	       '?' STUDENT_BIO_ID,	",
			"	       'IN' + ISNULL(FACT.TEST_DATE, '') + '.*.' + ISNULL(FACT.ORGTSTGPGM, '') ",
			"			+ '.' + ISNULL(DISTRICT.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), DISTRICT.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(SCHOOL.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), SCHOOL.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(GRADE.GRADE_CODE, '') + '.' + ISNULL(FACT.ELEMENT_NUMBER, '') + '.IC.pdf' FILE_NAME	",
			"	     FROM RESULTS_GRT_FACT FACT,	",
			"	          GRADE_DIM GRADE,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 2) DISTRICT,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 3) SCHOOL	",
			"	     WHERE FACT.STUDENT_BIO_ID = ?	",
			"	     AND FACT.GRADEID = GRADE.GRADEID	",
			"	     AND FACT.STUDENT_BIO_ID = DISTRICT.STUDENT_BIO_ID	",
			"	     AND FACT.STUDENT_BIO_ID = SCHOOL.STUDENT_BIO_ID) P_NAME	",
			"	WHERE P_NAME.STUDENT_BIO_ID = IC.STUDENT_BIO_ID	");
	
	public static final String GET_STUDENTS_PDF_FILE_PATHS = "SELECT STU_PDF_FILEID VALUE, FILENAME NAME FROM STUDENT_PDF_FILES WHERE STUDENT_BIO_ID IN (?)";
	public static final String GET_STUDENTS_PDF_FILE_PATHS_ISR = CustomStringUtil.appendString(
			" SELECT STU_PDF_FILEID VALUE, FILENAME NAME FROM STUDENT_PDF_FILES SPF, pdf_reports PR" ,
			" where SPF.pdf_reportid = PR.pdf_reportid and  pr.report_name = 'ISR' and STUDENT_BIO_ID IN (?)"
			);
	public static final String GET_STUDENTS_PDF_FILE_PATH_ISR = CustomStringUtil.appendString(
			"SELECT FILENAME RESULT FROM STUDENT_PDF_FILES SPF, PDF_REPORTS PR",
			" WHERE SPF.PDF_REPORTID = PR.PDF_REPORTID AND PR.REPORT_NAME = 'ISR' AND STUDENT_BIO_ID = ?"
			);
	public static final String GET_STUDENTS_PDF_FILE_PATHS_IP = CustomStringUtil.appendString(
			" SELECT STU_PDF_FILEID VALUE, FILENAME NAME FROM STUDENT_PDF_FILES SPF, pdf_reports PR" ,
			" where SPF.pdf_reportid = PR.pdf_reportid and  pr.report_name = 'IP' and STUDENT_BIO_ID IN (?)"
			);
	public static final String GET_STUDENTS_PDF_FILE_PATH_IP = CustomStringUtil.appendString(
			"SELECT FILENAME RESULT FROM STUDENT_PDF_FILES SPF, PDF_REPORTS PR" ,
			" WHERE SPF.PDF_REPORTID = PR.PDF_REPORTID AND PR.REPORT_NAME = 'IP' AND STUDENT_BIO_ID = ?"
			);
	/**
	 * All 7 Params are STUDENT_BIO_ID
	 */
	public static final String GET_STUDENTS_PDF_FILE_PATH_BOTH = CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  ORG_NODE_NAME,	",
			"	  ORG_NODE_LEVEL,	",
			"	  PARENT_ORG_NODEID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = (SELECT	",
			"	  ORG_NODEID	",
			"	FROM STUDENT_BIO_DIM	",
			"	WHERE STUDENT_BIO_ID = ?)	",
			"	UNION ALL	",
			"	SELECT	",
			"	  O.ORG_NODEID,	",
			"	  O.ORG_NODE_NAME,	",
			"	  O.ORG_NODE_LEVEL,	",
			"	  O.PARENT_ORG_NODEID,	",
			"	  h$cte.path + CONVERT(varchar(max), O.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM O	",
			"	JOIN h$cte	",
			"	  ON h$cte.PARENT_ORG_NODEID = O.ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), O.ORG_NODEID) + '|%')	",
			"	SELECT	",
			"	  P_PATH.FILENAME,	",
			"	  ISNULL(P_PATH.PRE, '') + ISNULL(P_NAME.FILE_NAME, '') + '.' + ISNULL(P_PATH.EXT, '') FILE_NAME	",
			"	FROM (SELECT	",
			"	       SPF.STUDENT_BIO_ID,	",
			"	       SPF.FILENAME,	",
			"	       CASE PR.REPORT_NAME	",
			"	         WHEN 'ISR' THEN 'a-'	",
			"	         WHEN 'IP' THEN 'b-'	",
			"	         ELSE ''	",
			"	       END PRE,	",
			"	       PR.REPORT_NAME EXT	",
			"	     FROM STUDENT_PDF_FILES SPF,	",
			"	          PDF_REPORTS PR	",
			"	     WHERE SPF.PDF_REPORTID = PR.PDF_REPORTID	",
			"	     AND STUDENT_BIO_ID = ?	",
			"	     AND PR.REPORT_NAME IN ('IP', 'ISR')) P_PATH,	",
			"	     (SELECT	",
			"	       '?' STUDENT_BIO_ID,	",
			"	       'IN' + ISNULL(FACT.TEST_DATE, '') + '.*.' + ISNULL(FACT.ORGTSTGPGM, '') ",
			"			+ '.' + ISNULL(DISTRICT.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), DISTRICT.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(SCHOOL.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), SCHOOL.ORG_NODEID), '') ",
			" 			+ '.' + ISNULL(GRADE.GRADE_CODE, '') + '.' + ISNULL(FACT.ELEMENT_NUMBER, '') FILE_NAME	",
			"	     FROM RESULTS_GRT_FACT FACT,	",
			"	          GRADE_DIM GRADE,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 2) DISTRICT,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 3) SCHOOL	",
			"	     WHERE FACT.STUDENT_BIO_ID = ?	",
			"	     AND FACT.GRADEID = GRADE.GRADEID	",
			"	     AND FACT.STUDENT_BIO_ID = DISTRICT.STUDENT_BIO_ID	",
			"	     AND FACT.STUDENT_BIO_ID = SCHOOL.STUDENT_BIO_ID) P_NAME	",
			"	WHERE P_NAME.STUDENT_BIO_ID = P_PATH.STUDENT_BIO_ID	");			
		
	/**
	 * All 7 Params are STUDENT_BIO_ID
	 */
	public static final String GET_STUDENTS_PDF_FILE_PATH_ISR_ONLY = CustomStringUtil.appendString(			
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  ORG_NODE_NAME,	",
			"	  ORG_NODE_LEVEL,	",
			"	  PARENT_ORG_NODEID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = (SELECT	",
			"	  ORG_NODEID	",
			"	FROM STUDENT_BIO_DIM	",
			"	WHERE STUDENT_BIO_ID = ?)	",
			"	UNION ALL	",
			"	SELECT	",
			"	  O.ORG_NODEID,	",
			"	  O.ORG_NODE_NAME,	",
			"	  O.ORG_NODE_LEVEL,	",
			"	  O.PARENT_ORG_NODEID,	",
			"	  h$cte.path + CONVERT(varchar(max), O.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM O	",
			"	JOIN h$cte	",
			"	  ON h$cte.PARENT_ORG_NODEID = O.ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), O.ORG_NODEID) + '|%')	",
			"	SELECT	",
			"	  P_PATH.FILENAME,	",
			"	  ISNULL(P_NAME.FILE_NAME, '') + ISNULL(P_PATH.EXT, '') FILE_NAME	",
			"	FROM (SELECT	",
			"	       SPF.STUDENT_BIO_ID,	",
			"	       SPF.FILENAME,	",
			"	       CASE PR.REPORT_NAME	",
			"	         WHEN 'ISR' THEN 'a-'	",
			"	         WHEN 'IP' THEN 'b-'	",
			"	         ELSE ''	",
			"	       END PRE,	",
			"	       PR.REPORT_NAME EXT	",
			"	     FROM STUDENT_PDF_FILES SPF,	",
			"	          PDF_REPORTS PR	",
			"	     WHERE SPF.PDF_REPORTID = PR.PDF_REPORTID	",
			"	     AND STUDENT_BIO_ID = ?	",
			"	     AND PR.REPORT_NAME IN ('ISR')) P_PATH,	",
			"	     (SELECT	",
			"	       '?' STUDENT_BIO_ID,	",
			"	       'IN' + ISNULL(FACT.TEST_DATE, '') + '.*.' + ISNULL(FACT.ORGTSTGPGM, '') ",
			"			+ '.' + ISNULL(DISTRICT.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), DISTRICT.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(SCHOOL.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), SCHOOL.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(GRADE.GRADE_CODE, '') + '.' + ISNULL(FACT.ELEMENT_NUMBER, '') FILE_NAME	",
			"	     FROM RESULTS_GRT_FACT FACT,	",
			"	          GRADE_DIM GRADE,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 2) DISTRICT,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 3) SCHOOL	",
			"	     WHERE FACT.STUDENT_BIO_ID = ?	",
			"	     AND FACT.GRADEID = GRADE.GRADEID	",
			"	     AND FACT.STUDENT_BIO_ID = DISTRICT.STUDENT_BIO_ID	",
			"	     AND FACT.STUDENT_BIO_ID = SCHOOL.STUDENT_BIO_ID) P_NAME	",
			"	WHERE P_NAME.STUDENT_BIO_ID = P_PATH.STUDENT_BIO_ID	");
	/**
	 * All 7 Params are STUDENT_BIO_ID
	 */
	public static final String GET_STUDENTS_PDF_FILE_PATH_IPR_ONLY = CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  ORG_NODE_NAME,	",
			"	  ORG_NODE_LEVEL,	",
			"	  PARENT_ORG_NODEID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = (SELECT	",
			"	  ORG_NODEID	",
			"	FROM STUDENT_BIO_DIM	",
			"	WHERE STUDENT_BIO_ID = ?)	",
			"	UNION ALL	",
			"	SELECT	",
			"	  O.ORG_NODEID,	",
			"	  O.ORG_NODE_NAME,	",
			"	  O.ORG_NODE_LEVEL,	",
			"	  O.PARENT_ORG_NODEID,	",
			"	  h$cte.path + CONVERT(varchar(max), O.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM O	",
			"	JOIN h$cte	",
			"	  ON h$cte.PARENT_ORG_NODEID = O.ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), O.ORG_NODEID) + '|%')	",
			"	SELECT	",
			"	  P_PATH.FILENAME,	",
			"	  ISNULL(P_NAME.FILE_NAME, '') + ISNULL(P_PATH.EXT, '') FILE_NAME	",
			"	FROM (SELECT	",
			"	       SPF.STUDENT_BIO_ID,	",
			"	       SPF.FILENAME,	",
			"	       CASE PR.REPORT_NAME	",
			"	         WHEN 'ISR' THEN 'a-'	",
			"	         WHEN 'IP' THEN 'b-'	",
			"	         ELSE ''	",
			"	       END PRE,	",
			"	       PR.REPORT_NAME EXT	",
			"	     FROM STUDENT_PDF_FILES SPF,	",
			"	          PDF_REPORTS PR	",
			"	     WHERE SPF.PDF_REPORTID = PR.PDF_REPORTID	",
			"	     AND STUDENT_BIO_ID = ?	",
			"	     AND PR.REPORT_NAME IN ('IP')) P_PATH,	",
			"	     (SELECT	",
			"	       '?' STUDENT_BIO_ID,	",
			"	       'IN' + ISNULL(FACT.TEST_DATE, '') + '.*.' + ISNULL(FACT.ORGTSTGPGM, '') ",
			"			+ '.' + ISNULL(DISTRICT.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), DISTRICT.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(SCHOOL.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), SCHOOL.ORG_NODEID), '') ",
			"			+ '.' + ISNULL(GRADE.GRADE_CODE, '') + '.' + ISNULL(FACT.ELEMENT_NUMBER, '') FILE_NAME	",
			"	     FROM RESULTS_GRT_FACT FACT,	",
			"	          GRADE_DIM GRADE,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 2) DISTRICT,	",
			"	          (SELECT	",
			"	            '?' STUDENT_BIO_ID,	",
			"	            ORG_NODEID,	",
			"	            ORG_NODE_NAME	",
			"	          FROM h$cte	",
			"	          WHERE ORG_NODE_LEVEL = 3) SCHOOL	",
			"	     WHERE FACT.STUDENT_BIO_ID = ?	",
			"	     AND FACT.GRADEID = GRADE.GRADEID	",
			"	     AND FACT.STUDENT_BIO_ID = DISTRICT.STUDENT_BIO_ID	",
			"	     AND FACT.STUDENT_BIO_ID = SCHOOL.STUDENT_BIO_ID) P_NAME	",
			"	WHERE P_NAME.STUDENT_BIO_ID = P_PATH.STUDENT_BIO_ID	");

	public static final String GET_GRADE_NAME_BY_ID = "SELECT GRADE_NAME RESULT FROM GRADE_DIM WHERE GRADEID = ?";

	public static final String GET_GD_FILE_NAME_DATA = CustomStringUtil.appendString(
			"SELECT FACT.STUDENT_BIO_ID, FACT.ORG_NODEID, FACT.TEST_DATE, FACT.ORGTSTGPGM, GRADE.GRADE_CODE, FACT.ELEMENT_NUMBER",
			" FROM RESULTS_GRT_FACT FACT, GRADE_DIM GRADE WHERE FACT.STUDENT_BIO_ID = ? AND FACT.GRADEID = GRADE.GRADEID"
			);

	public static final String GET_PARENT_ORG_DATA = CustomStringUtil.appendString(
			" 	WITH h$cte	",
			" 	AS (SELECT	",
			" 	  ORG_NODE_LEVEL,	",
			" 	  ORG_NODEID,	",
			" 	  ORG_NODE_NAME,	",
			" 	  PARENT_ORG_NODEID,	",
			" 	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			" 	FROM ORG_NODE_DIM	",
			" 	WHERE ORG_NODEID = (SELECT	",
			" 	  ORG_NODEID	",
			" 	FROM STUDENT_BIO_DIM	",
			" 	WHERE STUDENT_BIO_ID = ?)	",
			" 	UNION ALL	",
			" 	SELECT	",
			" 	  O.ORG_NODE_LEVEL,	",
			" 	  O.ORG_NODEID,	",
			" 	  O.ORG_NODE_NAME,	",
			" 	  O.PARENT_ORG_NODEID,	",
			" 	  h$cte.path + CONVERT(varchar(max), O.ORG_NODEID) + '|'	",
			" 	FROM ORG_NODE_DIM O	",
			" 	JOIN h$cte	",
			" 	  ON h$cte.PARENT_ORG_NODEID = O.ORG_NODEID	",
			" 	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), O.ORG_NODEID) + '|%')	",
			" 	SELECT DISTINCT	",
			" 	  ORG_NODE_LEVEL,	",
			" 	  ORG_NODEID,	",
			" 	  ORG_NODE_NAME	",
			" 	FROM h$cte	",
			" 	ORDER BY ORG_NODE_LEVEL	");
	/**
	 * All 5 Params are STUDENT_BIO_ID
	 */
	public static final String GET_GD_FILE_NAME = CustomStringUtil.appendString(
			"	WITH h$cte	",
			"	AS (SELECT	",
			"	  ORG_NODEID,	",
			"	  ORG_NODE_NAME,	",
			"	  ORG_NODE_LEVEL,	",
			"	  PARENT_ORG_NODEID,	",
			"	  CONVERT(varchar(max), '|') + CONVERT(varchar(max), ORG_NODEID) + '|' AS path	",
			"	FROM ORG_NODE_DIM	",
			"	WHERE ORG_NODEID = (SELECT	",
			"	  ORG_NODEID	",
			"	FROM STUDENT_BIO_DIM	",
			"	WHERE STUDENT_BIO_ID = ?)	",
			"	UNION ALL	",
			"	SELECT	",
			"	  O.ORG_NODEID,	",
			"	  O.ORG_NODE_NAME,	",
			"	  O.ORG_NODE_LEVEL,	",
			"	  O.PARENT_ORG_NODEID,	",
			"	  h$cte.path + CONVERT(varchar(max), O.ORG_NODEID) + '|'	",
			"	FROM ORG_NODE_DIM O	",
			"	JOIN h$cte	",
			"	  ON h$cte.PARENT_ORG_NODEID = O.ORG_NODEID	",
			"	WHERE h$cte.path NOT LIKE '%|' + CONVERT(varchar(100), O.ORG_NODEID) + '|%')	",
			"	SELECT	",
			"	  'IN' + ISNULL(FACT.TEST_DATE, '') + '.ISTEP.' + ISNULL(FACT.ORGTSTGPGM, '') ",
			"		+ '.' + ISNULL(DISTRICT.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), DISTRICT.ORG_NODEID), '') ",
			"		+ '.' + ISNULL(SCHOOL.ORG_NODE_NAME, '') + '.' + ISNULL(CONVERT(varchar(100), SCHOOL.ORG_NODEID), '') ",
			"		+ '.' + ISNULL(GRADE.GRADE_CODE, '') + '.' + ISNULL(FACT.ELEMENT_NUMBER, '')	",
			"	FROM RESULTS_GRT_FACT FACT,	",
			"	     GRADE_DIM GRADE,	",
			"	     (SELECT	",
			"	       '?' STUDENT_BIO_ID,	",
			"	       ORG_NODEID,	",
			"	       ORG_NODE_NAME	",
			"	     FROM h$cte	",
			"	     WHERE ORG_NODE_LEVEL = 2) DISTRICT,	",
			"	     (SELECT	",
			"	       '?' STUDENT_BIO_ID,	",
			"	       ORG_NODEID,	",
			"	       ORG_NODE_NAME	",
			"	     FROM h$cte	",
			"	     WHERE ORG_NODE_LEVEL = 3) SCHOOL	",
			"	WHERE FACT.STUDENT_BIO_ID = ?	",
			"	AND FACT.GRADEID = GRADE.GRADEID	",
			"	AND FACT.STUDENT_BIO_ID = DISTRICT.STUDENT_BIO_ID	",
			"	AND FACT.STUDENT_BIO_ID = SCHOOL.STUDENT_BIO_ID	");

	public static final String GET_ALL_CLASSES_FOR_ALL_GRADES = CustomStringUtil.appendString(
			"SELECT DISTINCT OND.ORG_NODEID",
			" FROM PRODUCT PDT, CUST_PRODUCT_LINK CPL, ORG_PRODUCT_LINK ORPD, ORG_TEST_PROGRAM_LINK OTPLNK, ORG_NODE_DIM OND, GRADE_SELECTION_LOOKUP GSL",
			" WHERE PDT.PRODUCTID  = ?",
			" AND OND.ORG_MODE     = ?",
			" AND PDT.PRODUCTID    = CPL.PRODUCTID",
			" AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID",
			" AND ORPD.ORG_NODEID  = OTPLNK.ORG_NODEID",
			" AND OND.ORG_NODEID   = OTPLNK.ORG_NODEID",
			" AND OND.ORG_NODEID   = GSL.ORG_NODEID",
			" AND GSL.GRADEID     IN (SELECT A.GRADEID FROM",
			"     (SELECT DISTINCT GRD.GRADEID, GRD.GRADE_NAME, GRD.GRADE_SEQ",
			"     FROM GRADE_SELECTION_LOOKUP GSL, CUST_PRODUCT_LINK CUST, ASSESSMENT_DIM ASSD, GRADE_DIM GRD, ORG_PRODUCT_LINK OLNK",
			"     WHERE GSL.ADMINID     = CUST.ADMINID",
			"     AND GSL.ORG_NODEID    = ?",
			"     AND CUST.CUSTOMERID   = ?",
			"     AND CUST.CUST_PROD_ID = OLNK.CUST_PROD_ID",
			"     AND GSL.ORG_NODEID    =OLNK.ORG_NODEID",
			"     AND CUST.PRODUCTID    = ASSD.PRODUCTID",
			"     AND ASSD.ASSESSMENTID = GSL.ASSESSMENTID",
			"     AND CUST.PRODUCTID    = ?",
			"     AND GRD.GRADEID       = GSL.GRADEID) A)",
			" AND ((?<>4 AND OND.PARENT_ORG_NODEID = ?) OR (?=4 AND OND.ORG_NODEID        = ?))",
			" AND OND.ORG_NODE_LEVEL    = 4",
			" ORDER BY 1"
			);
	
	public static final String GET_ROOT_PATH = CustomStringUtil.appendString(
			"  SELECT TOP(1) ISNULL(cust.file_location,'')+ISNULL(prod.file_location,'') ",
			" FROM customer_info cust, product prod, cust_product_link lin ",
			" WHERE lin.customerid = cust.customerid  AND lin.productid = prod.productid ",
			"AND cust.customerid = ? AND prod.productid = ?");

	public static final String GET_CUST_PATH = CustomStringUtil.appendString(
			"  SELECT TOP(1) ISNULL(cust.file_location,'')+isnull(prod.file_location,'') ",
			" FROM customer_info cust, product prod, cust_product_link lin  ",
			" WHERE lin.customerid = cust.customerid  AND lin.productid = prod.productid ",
			" AND cust.customerid = ? AND lin.CUST_PROD_ID = ?");
	
	public static final String GET_CURRENT_ADMIN_YEAR = "SELECT TOP(1) ADMIN_YEAR FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y'";

	// query to capture the user  activity when clicked login as
		public static final String INSERT_LoginAsACTIVITY_LOG =  CustomStringUtil.appendString(
				" 	INSERT INTO user_activity_history (activityid, userid, customerid, acty_typeid, activity_date, activity_details, ip_address, datetimestamp)	",
				" 	  SELECT	",
				" 	    NEXT VALUE FOR activityid_seq,	",
				" 	    (SELECT	",
				" 	      userid	",
				" 	    FROM users	",
				" 	    WHERE UPPER(username) = UPPER(?)),	",
				" 	    ?,	",
				" 	    ?,	",
				" 	    SYSDATETIME(),	",
				" 	    ?,	",
				" 	    ?,	",
				" 	    SYSDATETIME() ");
		
		// query to capture the user activity
		public static final String INSERT_ACTIVITY_LOG =  CustomStringUtil.appendString(
				" INSERT INTO user_activity_history (activityid, userid, customerid, acty_typeid, activity_date, activity_details, ip_address, datetimestamp) ",
				" VALUES (NEXT VALUE FOR activityid_seq, ?, ?, ?, SYSDATETIME(), ?, ?, SYSDATETIME())" );

		public static final String GET_STUDENT_FILE_NAME = CustomStringUtil.appendString(
				" 	SELECT	",
				" 	  ISNULL(CUST.FILE_LOCATION, '') + ISNULL(PROD.FILE_LOCATION, '') + ISNULL(SPF.FILENAME, '') FILENAME	",
				" 	FROM STUDENT_PDF_FILES SPF,	",
				" 	     PDF_REPORTS PR,	",
				" 	     CUSTOMER_INFO CUST,	",
				" 	     PRODUCT PROD,	",
				" 	     CUST_PRODUCT_LINK LIN	",
				" 	WHERE SPF.STUDENT_BIO_ID = ?	",
				" 	AND PR.REPORT_NAME = ?	",
				" 	AND SPF.PDF_REPORTID = PR.PDF_REPORTID	",
				" 	AND PR.CUST_PROD_ID = LIN.CUST_PROD_ID	",
				" 	AND LIN.CUSTOMERID = CUST.CUSTOMERID	",
				" 	AND LIN.PRODUCTID = PROD.PRODUCTID	",
				" 	AND SPF.is_file_exists = 'Y'	",
				" 	AND LIN.CUST_PROD_ID = ?	");
		
		
		public static final String GET_CURRENT_MSGTYPE = CustomStringUtil.appendString(
				" 	SELECT TOP (1)	",
				" 	  A.MSG_TYPEID MSGTYPEID	",
				" 	FROM DASH_MESSAGE_TYPE A,	",
				" 	     DASH_MESSAGE_TYPE B	",
				" 	WHERE A.CUST_PROD_ID = ?	",
				" 	AND A.CUST_PROD_ID <> B.CUST_PROD_ID	",
				" 	AND B.MSG_TYPEID = ?	",
				" 	AND A.MESSAGE_NAME = B.MESSAGE_NAME	");

		public static final String UPDATE_JOB_TRACKING_STATUS = "UPDATE JOB_TRACKING SET JOB_STATUS = ?, JOB_LOG = ?, UPDATED_DATE_TIME = SYSDATETIME() WHERE JOB_ID = ?";
		
		public static final String SP_GET_STUDENTS_ALL_C_ALL_G = "{CALL FACT.PKG_GROUP_DOWNLOADS$SP_GET_STUDENTS_ALL_C_ALL_G(?, ?, ?, ?, ?, ?, ?, ?,  ?)}";
		public static final String SP_GET_STUDENTS_ALL_C_ONE_G = "{CALL FACT.PKG_GROUP_DOWNLOADS$SP_GET_STUDENTS_ALL_C_ONE_G(?, ?, ?, ?, ?, ?, ?, ?,  ?)}";
		public static final String SP_GET_STUDENTS_ONE_C_ALL_G = "{CALL FACT.PKG_GROUP_DOWNLOADS$SP_GET_STUDENTS_ONE_C_ALL_G(?, ?, ?, ?, ?, ?, ?, ?, ?,  ?)}";
		public static final String SP_GET_STUDENTS_ONE_C_ONE_G = "{CALL FACT.PKG_GROUP_DOWNLOADS$SP_GET_STUDENTS_ONE_C_ONE_G(?, ?, ?, ?,  ?, ?, ?)}";
		public static final String SP_GET_STUDENTS_G_MO = "{CALL FACT.PKG_GROUP_DOWNLOADS_MO$SP_GET_STUDENTS(?, ?, ?, ?, ?,  ?)}";
		
		public static final String SP_GET_USER_ROLE = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_USER_ROLE(?,  ?)}";

		public static final String SP_GET_USERS_ONSCROLL_WITH_SP = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_USERS_ONSCROLL_WITH_SP(?, ?, ?, ?, ?, ?,  ?, ?, ?)}";
		public static final String SP_GET_USERS_ONSCROLL = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_USERS_ONSCROLL(?, ?, ?, ?, ?, ?, ?,  ?)}";
		public static final String SP_GET_USERS_ON_FIRST_LOAD = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_USERS_ON_FIRST_LOAD(?, ?, ?, ?, ?, ?,  ?)}";

		public static final String SP_GET_USER_RESET_PASSWORD = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_USER_RESET_PASSWORD(?,  ?)}";
		public static final String SP_GET_USER_PWD_HINT_LIST = "{CALL FACT.PKG_MANAGE_USERS$SP_GET_USER_PWD_HINT_LIST(?,  ?)}";

		public static final String SP_GET_MENU_MAP = "{CALL FACT.PKG_MENU_ACCESS$SP_GET_MENU_MAP(?, ?, ?, ?)}";
		
		public static final String SP_GET_ACTION_MAP = "{CALL FACT.PKG_MENU_ACCESS$SP_GET_ACTION_MAP(?, ?,  ?, ?)}";

		public static final String GET_FILE_SIZE = CustomStringUtil.appendString(" SELECT JOB_ID,REQUEST_FILENAME,FILE_SIZE FROM JOB_TRACKING WHERE JOB_ID = ?");
		public static final String UPDATE_FILE_SIZE = CustomStringUtil.appendString("UPDATE JOB_TRACKING SET FILE_SIZE = ? WHERE JOB_ID = ?");
		
		public static final String SP_GET_PROPERTY = "{CALL FACT.PKG_CONTRACT_PROPERTY$SP_GET_PROPERTY(?)}";

		public static final String GET_PRODUCTS_EDIT_ACTIONS = "{CALL FACT.PKG_MANAGE_REPORT$GET_PRODUCTS_EDIT_ACTIONS(?,  ?, ?)}";
		public static final String GET_ACTIONS_EDIT_ACTIONS = "{CALL FACT.PKG_MANAGE_REPORT$GET_ACTIONS_EDIT_ACTIONS(?,  ?, ?)}";
		public static final String GET_ACTION_ACCESS = "{CALL FACT.PKG_MANAGE_REPORT$GET_ACTION_ACCESS(?, ?,  ?, ?)}";
		public static final String SP_UPDATE_ACTION_DATA = "{CALL FACT.PKG_MANAGE_REPORT$SP_UPDATE_ACTION_DATA(?, ?, ?, ?, ?)}";
		
		public static final String GET_GENERIC_SYSTEM_CONFIGURATION_MESSAGES = "{CALL FACT.PKG_MANAGE_REPORT$GET_GENERIC_MESSAGES(?,  ?)}";
		
		public static final String SP_CUSTOMER_STD_EXTRACT_ONLINE = "{CALL FACT.PKG_STUDENTDATA_EXTRACT$SP_CUSTOMER_STD_EXTRACT_ONLINE(?, ?, ?, ?, ?)}";
		
		public static final String GET_CLOB_XML_FILE = "FACT.PKG_STUDENTDATA_EXTRACT$SP_GET_CLOB_XML_FILE(?,?,?)";
		
		public static final String STORE_WS_LOG = "INSERT INTO OAS_WS_LOG (processid, rosterid, state, uuid, status, summary, message, datetimestamp) VALUES (?,?,?,?,?,?,?,sysdatetime())";

}

