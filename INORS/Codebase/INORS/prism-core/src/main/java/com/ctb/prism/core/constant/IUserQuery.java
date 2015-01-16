package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IUserQuery {
	
	// query for getting logged-in user details
	public static final String GET_USER_DETAILS = CustomStringUtil.appendString( 
			" SELECT USERS.IS_FIRSTTIME_LOGIN,USERS.USERID,ORG.ORG_NODEID,",
			" ORG.ORG_NODE_LEVEL,USERS.DISPLAY_USERNAME,USERS.ACTIVATION_STATUS,",
			" USERS.PASSWORD,USERS.SALT,USERS.CUSTOMERID CUSTID,USERS.email_address EMAIL, OND.ORG_MODE",
			" FROM USERS USERS,ORG_USERS ORG, ORG_NODE_DIM OND",
			" WHERE UPPER(USERS.USERNAME) = UPPER(?)",
			" AND USERS.USERID = ORG.USERID AND ORG.ORG_NODEID = OND.ORG_NODEID");
	
	public static final String SP_GET_USER_DETAILS = "{CALL PKG_ADMIN_MODULE.SP_GET_USER_DETAILS(?, ?, ?, ?, ?)}";

	public static final String SP_GET_USER_EMAIL = "{CALL PKG_MANAGE_USERS.SP_GET_USER_EMAIL(?, ?, ?)}";
	public static final String SP_CHECK_ORG_HIERARCHY = "{CALL PKG_ADMIN_MODULE.SP_CHECK_ORG_HIERARCHY(?,?,?,?,?)}";
	public static final String SP_CHECK_USER_ROLE_BY_USERNAME = "{CALL PKG_ADMIN_MODULE.SP_CHECK_USER_ROLE_BY_USERNAME(?,?,?,?)}";
	
	/*public static final String GET_EDU_USER_DETAILS = CustomStringUtil.appendString( 
			"SELECT USERS.IS_FIRSTTIME_LOGIN,USERS.USERID,EC.EDU_CENTERID ORG_NODEID, -99 ORG_NODE_LEVEL,",
			" USERS.DISPLAY_USERNAME,USERS.ACTIVATION_STATUS,USERS.PASSWORD,",
			" USERS.SALT, USERS.CUSTOMERID CUSTID,USERS.email_address EMAIL, 'PUBLIC' AS ORG_MODE",
			" FROM USERS USERS, EDU_CENTER_USER_LINK EL,Edu_Center_Details EC",
			" WHERE UPPER(USERS.USERNAME) = UPPER(?)",
			" AND USERS.CUSTOMERID = EC.CUSTOMERID",
			" AND EC.EDU_CENTERID = EL.EDU_CENTERID",
			" AND EL.USERID = USERS.USERID");
	*/
	public static final String SP_GET_EDU_USER_DETAILS = "{CALL PKG_ADMIN_MODULE.SP_GET_EDUUSER_DETAILS(?, ?, ?, ?, ?)}";
	
	// queru to fetch user roles for logged-in user
	/*public static final String SELECT_USER_AUTHORITIES = CustomStringUtil.appendString(
			 "SELECT RLE.ROLE_NAME ROLENAME  ",
			 " FROM USER_ROLE URLE, ROLE RLE, USERS USERS",  
			 " WHERE URLE.ROLEID = RLE.ROLEID  ",  
			 " AND  URLE.USERID = USERS.USERID ",
			 " AND UPPER(USERS.USERNAME) = UPPER(?) ");*/
	
	public static final String SELECT_USER_AUTHORITIES = CustomStringUtil.appendString("SELECT RLE.ROLE_NAME ROLENAME FROM USERS USERS, USER_ROLE URLE, ROLE RLE " +
			" WHERE URLE.USERID = USERS.USERID AND URLE.ROLEID = RLE.ROLEID AND UPPER(USERS.USERNAME) =  UPPER(?) ");


	public static final String SELECT_EDU_USER_AUTHORITIES = CustomStringUtil.appendString(
			 "SELECT RLE.ROLE_NAME ROLENAME  ",
			 " FROM USER_ROLE URLE, ROLE RLE, USERS USERS , EDU_CENTER_USER_LINK EC",  
			 " WHERE USERS.USERID =EC.USERID  ",  
			 " AND  URLE.USERID = USERS.USERID ",
			 " AND  URLE.ROLEID = RLE.ROLEID ",
			 " AND UPPER(USERS.USERNAME) = UPPER(?)");
	
	
	public static final String CHECK_LOGIN_TYPE = CustomStringUtil.appendString(
			"SELECT users.IS_FIRSTTIME_LOGIN ",
			"FROM users users ",
			"WHERE upper(users.username) = upper(?) ");
	
	/*public static final String GET_USER_DETAILS_ON_FIRST_LOAD = CustomStringUtil
	.appendString(" SELECT ABC.USERROWID, ABC.USERID AS USER_ID, ABC.USERNAME, ",
			" ABC.FULLNAME, ABC.STATUS, ABC.ORG_NODE_NAME AS ORG_NAME, ABC.ORG_NODEID AS ORG_ID, ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID " ,
			//", ABC.USER_TYPE  ",
			" FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID ,  ",
			" USR.USERID, USR.USERNAME,  ",
			" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,  ",
			" USR.ACTIVATION_STATUS AS STATUS, HIER.ORG_NODE_NAME,  ",
			" HIER.ORG_nodeID,  HIER.PARENT_ORG_NODEID ",
		//	" , USR.USER_TYPE ",
			" FROM USERS USR, org_users orgUser, (SELECT *  FROM org_node_dim ",
			" START WITH ORG_nodeID = ?   ",
			" CONNECT BY PRIOR ORG_nodeID = PARENT_ORG_NODEID ", 
			" ) HIER  ",
			" WHERE orguser.userid = usr.userid and HIER.ORG_NODEID = orguser.ORG_NODEID AND orguser.ORG_NODE_LEVEL <> 0 ", 
			" AND orguser.adminid = ?  AND  USR.ACTIVATION_STATUS != 'SS' ",
			" ORDER BY UPPER(USR.USERNAME)) ABC ",
			" WHERE ROWNUM <= 15 ",
			" ORDER BY UPPER(ABC.USERNAME)");*/	
	
	public static final String GET_USER_DETAILS_ON_FIRST_LOAD = CustomStringUtil.appendString(
			" SELECT ABC.USERROWID,",
			" ABC.USERID AS USER_ID,",
			" ABC.USERNAME,",
			" ABC.FULLNAME,",
			" ABC.STATUS,",
			" ABC.ORG_NODE_NAME AS ORG_NAME,",
			" ABC.ORG_NODEID AS ORG_ID,",
			" ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID",
			" FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID,",
			" ROWNUM RECRD_CNT,",
			" USR.USERID,",
			" USR.USERNAME,",
			" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,",
			" USR.ACTIVATION_STATUS AS STATUS,",
			" HIER.ORG_NODE_NAME,",
			" HIER.ORG_NODEID,",
			" HIER.PARENT_ORG_NODEID",
			" FROM USERS USR,",
			" ORG_USERS ORGUSER,",
			" (SELECT *  FROM org_node_dim WHERE CUSTOMERID = ? AND ORG_MODE = ?", // new DAO param added
			" START WITH ORG_nodeID = ? ",
			" CONNECT BY PRIOR ORG_nodeID = PARENT_ORG_NODEID ", 
			" UNION SELECT * FROM ORG_NODE_DIM WHERE CUSTOMERID = ? AND ORG_NODEID = ? AND ORG_NODE_LEVEL = 1",
			" ) HIER,  ORG_PRODUCT_LINK OLP ", //Changed for QC 77275
			" WHERE ORGUSER.USERID = USR.USERID",
			" AND HIER.ORG_NODEID = ORGUSER.ORG_NODEID",
			" AND ORGUSER.ORG_NODE_LEVEL <> 0",
			" AND ORGUSER.ORG_NODEID = OLP.ORG_NODEID", //Changed for QC 77275
			" AND OLP.CUST_PROD_ID = ?",				//Changed for QC 77275
			" AND USR.ACTIVATION_STATUS != 'SS'",
			" AND NOT EXISTS (SELECT 1 FROM USER_ROLE WHERE USER_ROLE.ROLEID = ? AND USER_ROLE.USERID = USR.USERID)", // IApplicationConstants.ROLE_PARENT_ID
			" ORDER BY UPPER(USR.USERNAME)) ABC",
			" WHERE ROWNUM <= 15",
			" ORDER BY UPPER(ABC.USERNAME)");
	
	public static final String GET_USER_DETAILS_ON_SCROLL=	CustomStringUtil
	.appendString(" SELECT ABC.USERROWID, ABC.USERID AS USER_ID, ABC.USERNAME, ",
			" ABC.FULLNAME, ABC.STATUS, ABC.ORG_NODE_NAME AS ORG_NAME, ABC.ORG_NODEID AS ORG_ID, ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID ",
			" FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID , ",
			" USR.USERID, USR.USERNAME, ",
			" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
			" USR.ACTIVATION_STATUS AS STATUS, HIER.ORG_NODE_NAME, ",
			" HIER.ORG_NODEID,  HIER.PARENT_ORG_NODEID ",
			" FROM USERS USR, org_users orgUser, (SELECT *  FROM org_node_dim WHERE CUSTOMERID = ? AND ORG_MODE = ?", // new DAO param added
			" START WITH ORG_nodeID = ? ",
			" CONNECT BY PRIOR ORG_nodeID = PARENT_ORG_NODEID ", 
			" UNION SELECT * FROM ORG_NODE_DIM WHERE CUSTOMERID = ? AND ORG_NODEID = ? AND ORG_NODE_LEVEL = 1",
			" ) HIER , ORG_PRODUCT_LINK OLP ", //Changed for QC 77275
			" WHERE orguser.userid = usr.userid and HIER.ORG_NODEID = orguser.ORG_NODEID AND orguser.ORG_NODE_LEVEL <> 0 AND  USR.ACTIVATION_STATUS != 'SS' ",
			" AND NOT EXISTS (SELECT 1 FROM USER_ROLE WHERE USER_ROLE.ROLEID = ? AND USER_ROLE.USERID = USR.USERID)", // IApplicationConstants.ROLE_PARENT_ID
			" AND orguser.ORG_NODEID = OLP.ORG_NODEID", //Changed for QC 77275
			" AND OLP.CUST_PROD_ID = ?",// //Changed for QC 77275
			" AND UPPER(USR.USERNAME) > UPPER(?)  ",
			" ORDER BY UPPER(USR.USERNAME)) ABC ",
			" WHERE ROWNUM <= 15 ",
			" ORDER BY UPPER(ABC.USERNAME)");
	
	public static final String GET_USER_DETAILS_ON_SCROLL_WITH_SRCH_PARAM=	CustomStringUtil
	.appendString(" SELECT ABC.USERROWID, ABC.USERID AS USER_ID, ABC.USERNAME, ",
			" ABC.FULLNAME, ABC.STATUS, ABC.ORG_NODE_NAME AS ORG_NAME, ABC.ORG_NODEID AS ORG_ID, ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID ", 
			" FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID ,  ",
			" USR.USERID, USR.USERNAME,  ",
			" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ", 
			" USR.ACTIVATION_STATUS AS STATUS, HIER.ORG_NODE_NAME,  ",
			" HIER.ORG_NODEID,  HIER.PARENT_ORG_NODEID ",
			" FROM USERS USR, org_users orgUser, (SELECT *  FROM org_node_dim WHERE CUSTOMERID = ? AND ORG_MODE = ?", // new DAO param added
			" START WITH ORG_nodeID = ? ",
			" CONNECT BY PRIOR ORG_nodeID = PARENT_ORG_NODEID ", 
			" UNION SELECT * FROM ORG_NODE_DIM WHERE CUSTOMERID = ? AND ORG_NODEID = ? AND ORG_NODE_LEVEL = 1",
			" ) HIER , ORG_PRODUCT_LINK OLP ", //Changed for QC 77275
			" WHERE orguser.userid = usr.userid and HIER.ORG_NODEID = orguser.ORG_NODEID AND orguser.ORG_NODE_LEVEL <> 0 AND  USR.ACTIVATION_STATUS != 'SS' ",
			" AND NOT EXISTS (SELECT 1 FROM USER_ROLE WHERE USER_ROLE.ROLEID = ? AND USER_ROLE.USERID = USR.USERID)", // IApplicationConstants.ROLE_PARENT_ID
			" AND orguser.ORG_NODEID = OLP.ORG_NODEID",  //Changed for QC 77275
			" AND OLP.CUST_PROD_ID = ? ",// //Changed for QC 77275
			" AND UPPER(USR.USERNAME) > UPPER(?)  ",
			" AND (UPPER(USR.USERNAME) LIKE UPPER(?)  ",
			"   OR UPPER(USR.LAST_NAME)  LIKE UPPER(?)  ",
			" 	OR UPPER(USR.FIRST_NAME) LIKE UPPER(?))  ",
			" ORDER BY UPPER(USR.USERNAME)) ABC ",
			" WHERE ROWNUM <= 15 ",
			" ORDER BY UPPER(ABC.USERNAME)");
	
	public static final String GET_USER_DATA = CustomStringUtil.appendString(
			"SELECT DISTINCT ABC.USERNAME,",
			" ABC.FULLNAME, ABC.STATUS, ABC.ORG_NODE_NAME, ABC.ORG_LABEL, R.DESCRIPTION",
			" FROM (SELECT USR.USERID, USR.USERNAME,",
			" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,",
			" DECODE(USR.ACTIVATION_STATUS, 'AC', 'ENABLED', 'DISABLED') AS STATUS, HIER.ORG_NODE_NAME,",
			" HIER.ORG_NODEID,  HIER.PARENT_ORG_NODEID, OTS.ORG_LABEL",
			" FROM USERS USR, ORG_USERS ORGUSER, ORG_PRODUCT_LINK OLP, (SELECT *  FROM ORG_NODE_DIM",
			" WHERE ORG_MODE = ? AND CUSTOMERID = (SELECT CUSTOMERID FROM USERS WHERE USERID=? AND ROWNUM=1)",
			" START WITH ORG_NODEID = ?",
			" CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER, (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/')",
			" WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL FROM (SELECT  DISTINCT ORG_LEVEL,ORG_LABEL",
			" FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP GROUP BY TEMP.ORG_LEVEL) OTS",
			" WHERE ORGUSER.USERID = USR.USERID AND HIER.ORG_NODEID = ORGUSER.ORG_NODEID",
			" AND ORGUSER.ORG_NODE_LEVEL <> 0 AND  USR.ACTIVATION_STATUS != 'SS'",
			" AND NOT EXISTS (SELECT 1 FROM USER_ROLE WHERE USER_ROLE.ROLEID = ? AND USER_ROLE.USERID = USR.USERID)", // IApplicationConstants.ROLE_PARENT_ID
			" AND ORGUSER.ORG_NODEID = OLP.ORG_NODEID ",
			" AND OLP.CUST_PROD_ID = ? ",
			" AND HIER.ORG_NODE_LEVEL = OTS.ORG_LEVEL",
			" ORDER BY UPPER(USR.USERNAME)) ABC, (SELECT U1.USERID,",
			" LISTAGG(RO.DESCRIPTION, ', ') WITHIN",
			" GROUP(ORDER BY RO.DESCRIPTION) AS DESCRIPTION",
			" FROM USER_ROLE UR, ROLE RO, USERS U1",
			" WHERE UR.USERID = U1.USERID AND RO.ROLEID = UR.ROLEID",
			" GROUP BY U1.USERID) R",
			" WHERE ABC.USERID=R.USERID(+)", // TODO : Populate USER_ROLE table and remove (+) symbol
			" ORDER BY UPPER(ABC.USERNAME)"
			);
	
	public static final String GET_USER_ROLE=CustomStringUtil.appendString( 
			" SELECT RLE.ROLEID AS ROLE_ID, RLE.ROLE_NAME AS ROLE_NAME ",
			" FROM ROLE RLE, USER_ROLE URLE ",
			" WHERE URLE.USERID = ?",
			" AND URLE.ROLEID = RLE.ROLEID",
            " AND RLE.ROLE_NAME NOT IN (?)");
	
	public static final String GET_ROLE_ACSI = " SELECT ROLEID AS ROLE_ID, ROLE_NAME FROM ROLE WHERE ROLE_NAME IN ( 'ROLE_ACSI', 'ROLE_ADMIN') ";
	
	public static final String GET_ROLE_SCHOOL = " SELECT ROLEID AS ROLE_ID, ROLE_NAME FROM ROLE WHERE ROLE_NAME IN ('ROLE_SCHOOL', 'ROLE_ADMIN') ";
	
	public static final String GET_ROLE_TEACHER = " SELECT ROLEID AS ROLE_ID, ROLE_NAME FROM ROLE WHERE ROLE_NAME IN ( 'ROLE_CLASS') ";
		
	public static final String GET_ROLE_DEFAULT = " SELECT ROLEID AS ROLE_ID, ROLE_NAME FROM ROLE WHERE ROLE_NAME IN ( 'ROLE_USER') ";
	
/*	public static final String GET_ROLE = CustomStringUtil.appendString(
			" SELECT RE.ROLEID AS ROLE_ID, RE.ROLE_NAME AS ROLE_NAME, OTS.ORG_LABEL || ' ' || RE.DESCRIPTION AS DESCRIPTION",
			" FROM ROLE RE, USER_ROLE UR, USERS U, ORG_USERS OU,(SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/')",
			" WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL FROM (SELECT  DISTINCT ORG_LEVEL,ORG_LABEL",
			" FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP GROUP BY TEMP.ORG_LEVEL) OTS",
			" WHERE UR.USERID = ? AND U.USERID = ? AND OU.USERID = ?",
			" AND UR.ROLEID=RE.ROLEID AND OU.ORG_NODE_LEVEL=OTS.ORG_LEVEL",
			" AND RE.ROLE_NAME NOT IN (? ,? ,?) ORDER BY RE.ROLEID");*/
	
	public static final String SP_GET_ROLE_USER = "{CALL PKG_MANAGE_USERS.SP_GET_ROLE_USER(?, ?, ?, ?)}";
	
/*	public static final String GET_ROLE_ADD = CustomStringUtil.appendString(
			" SELECT RE.ROLEID    AS ROLE_ID, RE.ROLE_NAME AS ROLE_NAME, RE.DESCRIPTION DESCRIPTION ",
			" FROM ROLE RE WHERE RE.ROLE_NAME NOT IN (? ,? ,?)");*/
	public static final String SP_GET_ROLE_ADD = "{CALL PKG_MANAGE_USERS.SP_GET_ROLE_ADD(?, ?, ?)}";
	
	public static final String GET_USER_DETAILS_ON_EDIT = "{CALL PKG_MANAGE_USERS.SP_GET_USER_DETAILS_ON_EDIT(?, ?, ?, ?)}";
	
/*	public static final String GET_USER_ROLE_ON_EDIT = CustomStringUtil.appendString(
			"SELECT DISTINCT RLE.ROLEID AS ROLE_ID, RLE.ROLE_NAME AS ROLENAME, OTS.ORG_LABEL || ' ' || RLE.DESCRIPTION AS DESCRIPTION",
			" FROM ROLE RLE, USER_ROLE URLE, USERS USR, ORG_USERS OU, (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/')",
			" WITHIN GROUP(ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL FROM (SELECT  DISTINCT ORG_LEVEL,ORG_LABEL",
			" FROM ORG_TP_STRUCTURE ORDER BY ORG_LEVEL) TEMP GROUP BY TEMP.ORG_LEVEL) OTS",
			" WHERE URLE.ROLEID = RLE.ROLEID AND URLE.USERID=USR.USERID AND USR.USERID=OU.USERID",
			" AND OU.ORG_NODE_LEVEL= (SELECT ORG_NODE_LEVEL FROM ORG_USERS WHERE USERID = ?)",
			" AND OTS.ORG_LEVEL=(SELECT ORG_NODE_LEVEL FROM ORG_USERS WHERE USERID = ?)",
			" AND RLE.ROLE_NAME NOT IN (? ,? ,?)",
			" ORDER BY RLE.ROLEID");*/
	
	public static final String GET_USER_ORG_LEVEL = " SELECT ORG_NODE_LEVEL FROM USERS USR, ORG_USERS ORG WHERE ORG.USERID = USR.USERID AND USR.USERID = ? AND rownum = 1 ";
		
	public static final String UPDATE_USER = CustomStringUtil.appendString(
			" UPDATE USERS  SET USERNAME     = ?, ",
			" DISPLAY_USERNAME     = ?,  EMAIL_ADDRESS = ?, ",
			" ACTIVATION_STATUS    = ?,  updated_date_time = SYSDATE WHERE USERID = ?");
	
	public static final String DELETE_USER = "DELETE FROM USERS WHERE USERID = ? ";
	
	public static final String DELETE_USER_ROLE = CustomStringUtil
			.appendString(" DELETE FROM USER_ROLE WHERE USERID = ? ");
	
	public static final String DELETE_ORG_USER = CustomStringUtil
	.appendString(" DELETE FROM ORG_USERS WHERE USERID = ? ");
	
	
	public static final String DELETE_USER_ACTIVITY = CustomStringUtil
	.appendString(" DELETE FROM USER_ACTIVITY_HISTORY WHERE USERID = ? ");
	
	public static final String DELETE_PASSWORD_HISTORY = CustomStringUtil
	.appendString(" DELETE FROM PASSWORD_HISTORY WHERE USERID = ? ");
	
	public static final String INSERT_USER_ROLE = CustomStringUtil
			.appendString("INSERT INTO USER_ROLE (ROLEID, USERID, CREATED_DATE_TIME) ",
						  " VALUES ((SELECT ROLEID FROM ROLE " +
						  "	WHERE ROLE_NAME = ?), ?, SYSDATE)");
	
	public static final String INSERT_USER = CustomStringUtil.appendString(
			" INSERT INTO USERS (USERID, USERNAME, ",
			" DISPLAY_USERNAME, EMAIL_ADDRESS, ACTIVATION_STATUS,",
			" IS_FIRSTTIME_LOGIN, IS_NEW_USER, customerid ) VALUES ",
			" (?, ?, ?, ?, ?, ?, ?, ?)");
	
	public static final String INSERT_ORG_USER = CustomStringUtil.appendString(
			" INSERT INTO ORG_USERS (ORG_USER_ID, USERID, ORG_NODEID, ORG_NODE_LEVEL, ADMINID, ACTIVATION_STATUS,CREATED_DATE_TIME) VALUES ",
			" (?, ?, ?, ?, (select adminid from cust_product_link where cust_prod_id=?),?, sysdate )");
	
	public static final String INSERT_ORG_USER_SSO = CustomStringUtil.appendString(
			" INSERT INTO ORG_USERS (ORG_USER_ID, USERID, ORG_NODEID, ORG_NODE_LEVEL, ADMINID, ACTIVATION_STATUS,CREATED_DATE_TIME) VALUES ",
			" (?, ?, ?, ?, ? ,?, sysdate )");
	
	public static final String INSERT_USER_WITH_PASSWORD = CustomStringUtil.appendString(
			" INSERT INTO USERS (USERID, USERNAME, ",
			" DISPLAY_USERNAME, EMAIL_ADDRESS, ACTIVATION_STATUS,",
			" IS_FIRSTTIME_LOGIN, PASSWORD, SALT, IS_NEW_USER, customerid ) VALUES ",
			" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	
	public static final String USER_SEQ_ID = "SELECT USER_ID_SEQ.NEXTVAL AS PARENT_SEQ_ID FROM DUAL";
	
	public static final String ORGUSER_SEQ_ID = "SELECT ORG_USER_ID_SEQ.NEXTVAL AS ORGUSER_SEQ_ID FROM DUAL";
	
	public static final String DB_REPORT_SEQ_ID = "SELECT DB_REPORT_ID_SEQ.NEXTVAL AS REPORT_SEQ_ID FROM DUAL";
	
	public static final String CHECK_EXISTING_USER = "SELECT userid as USER_ID FROM USERS WHERE upper(USERNAME) = upper(?)";
	public static final String CHECK_EXISTING_REPORT = "SELECT DB_REPORTID AS DB_REPORT_ID FROM DASH_REPORTS  WHERE REPORT_NAME=? AND report_folder_uri=?";
	
	// search users
	public static final String SEARCH_USER = CustomStringUtil
			.appendString( " SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID , ",
					" USR.USERID AS USER_ID,  USR.USERNAME,  ",
					" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ", 
					" USR.LAST_NAME,USR.FIRST_NAME, ",
					" USR.ACTIVATION_STATUS AS STATUS, HIER.ORG_NODE_NAME AS ORG_NAME, ", 
					" HIER.ORG_NODEID AS ORG_ID,  HIER.PARENT_ORG_NODEID AS ORG_PARENT_ID ",
					" FROM USERS USR, org_users orgUsers, (SELECT *  ",
					" FROM org_node_dim ", 
					" WHERE ORG_MODE = ?",
					" START WITH ORG_NODEID = ?  ",
					" CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID", 
					" UNION SELECT * FROM ORG_NODE_DIM WHERE  ORG_NODEID = ? AND ORG_NODE_LEVEL = 1",
					" )HIER , ORG_PRODUCT_LINK OLP ", //Changed for QC 77275
					" WHERE (UPPER(USR.USERNAME) LIKE UPPER(?) ",
					" OR UPPER(USR.LAST_NAME)  LIKE UPPER(?) ",
					" OR UPPER(USR.FIRST_NAME) LIKE UPPER(?)) ",
					//" AND HIER.ORG_NODE_LEVEL <> 0 ",
					" AND orgUsers.ORG_NODE_LEVEL <> 0 ",
					" AND NOT EXISTS (SELECT 1 FROM USER_ROLE WHERE USER_ROLE.ROLEID = ? AND USER_ROLE.USERID = USR.USERID)", // IApplicationConstants.ROLE_PARENT_ID
					" AND USR.ACTIVATION_STATUS != 'SS' ",
					" AND orgUsers.ORG_NODEID = OLP.ORG_NODEID ",  //Changed for QC 77275
					" AND OLP.CUST_PROD_ID =  ?",   //Changed for QC 77275
					" AND orgUsers.ORG_NODEID = HIER.ORG_NODEID ",
					" AND ORGUSERS.USERID = USR.USERID AND ROWNUM <= ? ");
	
	
	public static final String SEARCH_EDU_USER = CustomStringUtil.appendString(
			"SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID, USR.USERID AS USERID,",
            " USR.USERNAME AS USERNAME, USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,",
            " USR.LAST_NAME,USR.FIRST_NAME, USR.ACTIVATION_STATUS AS STATUS,",
            " EDC.EDU_CENTER_NAME AS EDU_CENTER_NAME,EDC.EDU_CENTER_CODE EDU_CENTER_CODE,",
            " EDC.EDU_CENTERID AS EDU_CENTERID ",
            " FROM USERS USR,EDU_CENTER_USER_LINK EDU, EDU_CENTER_DETAILS EDC",
            " WHERE (UPPER(USR.USERNAME) LIKE UPPER(?) OR",
            " UPPER(USR.LAST_NAME) LIKE UPPER(?) OR UPPER(USR.FIRST_NAME) LIKE UPPER(?))",
            " AND USR.USERID = EDU.USERID ",
            " AND EDU.EDU_CENTERID = EDC.EDU_CENTERID",
            " AND EDU.EDU_CENTERID =?",
            " AND ROWNUM <= ?");
	
	public static final String SEARCH_USER_EXACT = CustomStringUtil
	.appendString( " SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID , ",
			" USR.USERID AS USER_ID,  USR.USERNAME,  ",
			" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ", 
			" USR.LAST_NAME,USR.FIRST_NAME, ",
			" USR.ACTIVATION_STATUS AS STATUS, HIER.ORG_NODE_NAME AS ORG_NAME, ", 
		//	" USR.USER_TYPE AS USER_TYPE, ",
			" HIER.ORG_NODEID AS ORG_ID,  HIER.PARENT_ORG_NODEID AS ORG_PARENT_ID ",
			" FROM USERS USR, ORG_USERS orgUsers, (SELECT *  ",
			" FROM ORG_NODE_DIM ", 
			" WHERE ORG_MODE = ? ",
			" START WITH ORG_NODEID = ? ",
			" CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID ",
			" UNION SELECT * FROM ORG_NODE_DIM WHERE ORG_NODEID = ? AND ORG_NODE_LEVEL = 1",
			")HIER,  ORG_PRODUCT_LINK OLP ", //Changed for QC 77275
			" WHERE UPPER(USR.USERNAME) LIKE UPPER(?) ",
			" AND orgUsers.ORG_NODE_LEVEL <> 0 ",
			" AND NOT EXISTS (SELECT 1 FROM USER_ROLE WHERE USER_ROLE.ROLEID = ? AND USER_ROLE.USERID = USR.USERID)", // IApplicationConstants.ROLE_PARENT_ID
			" AND USR.ACTIVATION_STATUS != 'SS' ",
			" AND ORGUSERS.ORG_NODEID = OLP.ORG_NODEID ", //Changed for QC 77275
			" AND OLP.CUST_PROD_ID = ?",  //Changed for QC 77275
			" AND orgUsers.ORG_NODEID = HIER.ORG_NODEID " +
			" AND ORGUSERS.USERID = USR.USERID AND ROWNUM <= ? ");
	
	public static final String ALL_ROLE = CustomStringUtil.appendString("select ROLEID, ROLE_NAME from ROLE where ROLE_NAME !='ROLE_PARENT' ");
	
	public static final String ALL_ASSESSMENTS = CustomStringUtil.appendString("select ASSESSMENTID as ID, ASSESSMENT_NAME as NAME from ASSESSMENT_DIM ");
	
	public static final String ALL_ADMIN_YEAR = CustomStringUtil.appendString(
			" SELECT ADMINID VALUE, ADMIN_NAME NAME FROM ADMIN_DIM ");
	
	
	public static final String CUST_PROD = CustomStringUtil.appendString(
			" SELECT CPL.CUST_PROD_ID VALUE, P.PRODUCT_NAME NAME",
			" FROM PRODUCT P, CUST_PRODUCT_LINK CPL",
			" WHERE P.PRODUCTID = CPL.PRODUCTID",
			" AND CPL.CUSTOMERID = ?",
			" ORDER BY P.PRODUCT_SEQ" );
	
	//It creates problem to add/edit report because Corporation/Diocese have same ID - By Joy
	/*public static final String ALL_ORG_NODE_LEVEL = CustomStringUtil.appendString(
			" SELECT DISTINCT ORG_LEVEL VALUE, ORG_LABEL NAME",
			" FROM ORG_TP_STRUCTURE",
			" ORDER BY VALUE ");*/
	
	/**
	 * Moved to Package - By Joy
	 * This is for TASC and INORS 
	 * */
	//Modify query because Corporation/Diocese are same - By Joy
	public static final String GET_ORG_NODE_LEVEL = "PKG_ADMIN_MODULE.SP_GET_ORG_NODE_LEVEL(?,?)";
	
	public static final String INSERT_EDU_CENTER_USER = CustomStringUtil.appendString(
			"INSERT INTO EDU_CENTER_USER_LINK (EDU_CENTERID, USERID) VALUES (?, ?)");
		
	
	public static final String CREATE_USER = "{CALL PKG_ADMIN_MODULE.SP_CREATE_USER(?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	public static final String CREATE_SSO_USER = "{CALL PKG_ADMIN_MODULE.SP_CREATE_SSO_USER(?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	
	/*public static final String GET_PASSWORD_HISTORY = CustomStringUtil.appendString(
			" select salt, ph.password ",
			" from PASSWORD_HISTORY ph, users u ",
			" where upper(USERNAME) = upper('ctbadmin') order by ph.created_date_time desc ");*/
	
	public static final String SP_GET_PASSWORD_HISTORY = "{CALL PKG_ADMIN_MODULE.SP_GET_PASSWORD_HISTORY(?, ?, ?, ?)}";
	
	
}

