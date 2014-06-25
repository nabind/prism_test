package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IParentQuery {

	//Fix for TD 78161 - By Joy
	public static final String VALIDATE_INVITATION_CODE = CustomStringUtil.appendString(
			" SELECT INV.ICID,",
			" INV.TOTAL_AVAILABLE,",
			" INV.TOTAL_ATTEMPT,",
			" DECODE(SIGN(TRUNC(INV.EXPIRATION_DATE) - TRUNC(SYSDATE)), -1, 'IN', 'AC') AS EXPIRATION_STATUS,",
			" INV.ACTIVATION_STATUS AS ACTIVATION_STATUS,",
			" NVL((SELECT ICC.ORG_USER_ID",
			" FROM INVITATION_CODE_CLAIM ICC",
			" WHERE ICC.ICID = INV.ICID",
			" AND ICC.ORG_USER_ID = ?),0) ALREADY_CLAIMED",
			" FROM INVITATION_CODE INV",
			" WHERE INV.INVITATION_CODE = ?",
			" AND INV.ACTIVATION_STATUS = 'AC'");

	public static final String GET_STUDENT_FOR_INVITATION_CODE = CustomStringUtil.appendString(
			" SELECT IC.STUDENT_FULL_NAME  AS STUDENT_NAME, ",
			" P.PRODUCT_NAME AS ADMINISTRATION, ",
			" GRD.GRADE_NAME AS GRADE , OND.ORG_NODE_NAME",
			" FROM INVITATION_CODE IC, ",
			" CUST_PRODUCT_LINK CPL, ",
			" PRODUCT       P, ",
			" GRADE_DIM       GRD , ORG_NODE_DIM OND",
			" WHERE IC.CUST_PROD_ID = CPL.CUST_PROD_ID ",
			" AND CPL.PRODUCTID =  P.PRODUCTID ", 
			" AND IC.GRADE_ID = GRD.GRADEID ",
			" AND IC.ORG_NODEID = OND.ORG_NODEID",
			" AND IC.ACTIVATION_STATUS = 'AC' ",
			" AND IC.INVITATION_CODE = ?");

	/**
	 * Query to insert newly registered user
	 */
	public static final String INSERT_USER_DATA = CustomStringUtil.appendString(
			" INSERT INTO USERS ",
			" (USERID, USERNAME, DISPLAY_USERNAME, ",
			" LAST_NAME, FIRST_NAME, MIDDLE_NAME, ",
			" EMAIL_ADDRESS, PHONE_NO, COUNTRY, ",
			" ZIPCODE, STREET, CITY, STATE,CUSTOMERID, ",
			" IS_FIRSTTIME_LOGIN,IS_NEW_USER, ",
			" ACTIVATION_STATUS, CREATED_DATE_TIME)",
			" VALUES (?, ?, ?, ?, ?, '', ?, ?, ?, ?, ?, ?,?,",
			" (select DISTINCT inv.customerid from org_node_dim org, invitation_code inv where inv.org_nodeid = org.org_nodeid and INV.ACTIVATION_STATUS = 'AC' AND inv.invitation_code = ?),?,'Y', ",
			" 'AC', SYSDATE)");

	public static final String INSERT_USER_DATA_WITH_PASSWD = CustomStringUtil.appendString(
			" INSERT INTO USERS ",
			" (USERID, USERNAME, DISPLAY_USERNAME, ",
			" LAST_NAME, FIRST_NAME, MIDDLE_NAME, ",
			" EMAIL_ADDRESS, PHONE_NO, COUNTRY, ",
			" ZIPCODE, STREET, CITY, STATE,CUSTOMERID,",
			" IS_FIRSTTIME_LOGIN,IS_NEW_USER, ",
			" ACTIVATION_STATUS, CREATED_DATE_TIME, PASSWORD, SALT)",
			" VALUES (?, ?, ?, ?, ?, '', ?, ?, ?, ?, ?, ?,?,",
			" (SELECT DISTINCT CPL.CUSTOMERID FROM CUST_PRODUCT_LINK CPL, INVITATION_CODE INV WHERE CPL.CUST_PROD_ID = INV.CUST_PROD_ID  AND INV.ACTIVATION_STATUS = 'AC' AND INV.INVITATION_CODE = ?),?,'N',",
			" 'AC', SYSDATE, ?, ?)");
	
	//As INVITATION_CODE keeps school org_nodeid instead of class - See nex object - By Joy
	/*public static final String CHECK_ORG_USER_PARENT = CustomStringUtil.appendString(
			" SELECT ORG_USER_ID",
			" FROM ORG_USERS OU",
			" WHERE OU.USERID = ?",
			" AND OU.ORG_NODEID =",
			" (SELECT N.PARENT_ORG_NODEID",
			" FROM INVITATION_CODE M, ORG_NODE_DIM N",
			" WHERE M.ORG_NODEID = N.ORG_NODEID",
			" AND M.INVITATION_CODE = ?)");*/
	
	//As INVITATION_CODE keeps school org_nodeid instead of class - By Joy
	public static final String CHECK_ORG_USER_PARENT = CustomStringUtil.appendString(
			" SELECT OU.ORG_USER_ID",
			" FROM ORG_USERS OU, INVITATION_CODE IC",
			" WHERE OU.ORG_NODEID = IC.ORG_NODEID",
			" AND OU.USERID = ?",
			" AND IC.INVITATION_CODE = ?");
	
	
	public static final String GET_USERID_PARENT = CustomStringUtil.appendString(
			" SELECT USERID FROM USERS U WHERE UPPER(U.USERNAME) = UPPER(?)");
	
	public static final String INSERT_ORG_USER_PARENT = CustomStringUtil.appendString(
			" INSERT INTO ORG_USERS (ORG_USER_ID, USERID,ORG_NODEID,ORG_NODE_LEVEL,ADMINID, ACTIVATION_STATUS,CREATED_DATE_TIME) VALUES ",
			" (?, ?,(SELECT M.ORG_NODEID FROM INVITATION_CODE M  WHERE M.INVITATION_CODE = ?),'3', ",
			" (SELECT CUST_PRODUCT_LINK.ADMINID FROM INVITATION_CODE,CUST_PRODUCT_LINK WHERE INVITATION_CODE.CUST_PROD_ID = CUST_PRODUCT_LINK.CUST_PROD_ID ",
			" AND INVITATION_CODE=?), ?, sysdate )");

	/**
	 * Query to assign user role to the new registered user
	 */
	public static final String ADD_ROLE_TO_REGISTERED_USER = "INSERT INTO USER_ROLE VALUES ((SELECT USERID FROM USERS WHERE upper(USERNAME) LIKE upper(?)),(SELECT ROLEID FROM ROLE WHERE ROLE_NAME LIKE ?),SYSDATE,SYSDATE)";

	public static final String INSERT_INVITATION_CODE_CLAIM_DATA = CustomStringUtil.appendString(
			" INSERT INTO INVITATION_CODE_CLAIM ",
			" (INVITATION_CODE_CLAIM_ID,ICID,ORG_USER_ID, ",
			" ACTIVATION_STATUS,CLAIM_DATE,UPDATED_DATE_TIME)",
			" VALUES ",
			" (INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL,(SELECT ICID FROM INVITATION_CODE INV where INV.ACTIVATION_STATUS = 'AC' and INV.INVITATION_CODE = ? and rownum = 1),?,",
			" 'AC',SYSDATE, SYSDATE)");

	public static final String UPDATE_INVITATION_CODE_CLAIM_COUNT = CustomStringUtil.appendString(
			" UPDATE INVITATION_CODE ",
			" SET TOTAL_ATTEMPT = (SELECT TOTAL_ATTEMPT ",
			" FROM INVITATION_CODE ",
			" WHERE INVITATION_CODE = ? AND ACTIVATION_STATUS = 'AC')+1 ",
			" WHERE INVITATION_CODE = ?	");
	public static final String UPDATE_AVAILABLE_INVITATION_CODE_CLAIM_COUNT = CustomStringUtil.appendString(
			" UPDATE INVITATION_CODE ",
			" SET TOTAL_AVAILABLE = (SELECT TOTAL_AVAILABLE ",
			" FROM INVITATION_CODE ",
			" WHERE INVITATION_CODE = ? AND ACTIVATION_STATUS = 'AC')-1 ",
			" WHERE INVITATION_CODE = ?	");
	public static final String CHECK_AVAILABLE_INVITATION_CODE_CLAIM_COUNT = CustomStringUtil.appendString(
			" SELECT IC.TOTAL_AVAILABLE AS CLAIM_AVAILABILITY",
			" FROM INVITATION_CODE IC",
			" WHERE IC.INVITATION_CODE = ? AND IC.ACTIVATION_STATUS = 'AC' ");

	public static final String CHECK_INVITATION_CODE_CLAIM = CustomStringUtil.appendString(
			" SELECT INVITATION_CODE_CLAIM_ID",
			" FROM INVITATION_CODE_CLAIM ICC, INVITATION_CODE IC, USERS USR",
			" WHERE ICC.ORG_USER_ID = USR.USERID AND upper(USR.USERNAME) = upper(?) AND",
			" IC.ICID = ICC.ICID AND",
			" IC.INVITATION_CODE = ? AND IC.ACTIVATION_STATUS = 'AC' ");

	public static final String ADD_INVITATION_CODE_TO_ACCOUNT = CustomStringUtil.appendString(
					" INSERT INTO INVITATION_CODE_CLAIM",
					" (INVITATION_CODE_CLAIM_ID,",
					" ORG_USER_ID,",
					" ICID,",
					" ACTIVATION_STATUS,",
					" CLAIM_DATE)",
					" VALUES",
					" (INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL,",
					" ?,",
					" (SELECT ICID",
					" FROM INVITATION_CODE IC",
					" WHERE IC.INVITATION_CODE = ?",
					" AND IC.ACTIVATION_STATUS = 'AC'),",
					" 'AC',",
					" SYSDATE)");

	//Fix to implement cust_prod_id properly - By Joy
	public static final String GET_PARENT_DETAILS_ON_FIRST_LOAD = CustomStringUtil.appendString(
			"SELECT ABC.USERID, ABC.USERNAME,ABC.FULLNAME, ABC.STATUS, TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,",
			" ABC.ORG_NODE_NAME, ABC.ORG_NODEID FROM (SELECT USR.USERID, USR.USERNAME,USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,",
			" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT,HIER.ORG_NODE_NAME, HIER.ORG_NODEID FROM USERS USR, USER_ROLE UR,ORG_USERS OU,",
			" (SELECT * FROM ORG_NODE_DIM WHERE ORG_MODE = ? START WITH ORG_NODEID =? CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID ) HIER ",
			" WHERE USR.USERID=OU.USERID and OU.ORG_NODEID=HIER.ORG_NODEID",
			" AND USR.USERID = UR.USERID AND UR.ROLEID = ?",
			" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL WHERE OPL.ORG_NODEID = HIER.ORG_NODEID AND OPL.CUST_PROD_ID = ?)",
			" ORDER BY UPPER(USR.USERNAME)) ABC WHERE ROWNUM <= 15 ORDER BY UPPER(ABC.USERNAME)");

	public static final String GET_PARENT_DETAILS_BY_USERNAME = CustomStringUtil.appendString(
			" SELECT USERID, USERNAME, DISPLAY_USERNAME, LAST_NAME, FIRST_NAME, MIDDLE_NAME,",
			" EMAIL_ADDRESS, PHONE_NO, COUNTRY, ZIPCODE, STATE, STREET,CITY FROM USERS USR WHERE UPPER(USR.USERNAME)=UPPER(?)");

	public static final String GET_PARENT_SECURITY_QUESTION = CustomStringUtil.appendString(
			" SELECT Q.PH_QUESTIONID AS QUESTION_ID, ",
			" Q.QUESTION_VALUE AS QUESTION,",
			" Q.QUESTION_SEQ AS SNO, ",
			" A.PH_ANSWERID AS ANSWER_ID, ",
			" A.ANSWER_VALUE AS ANSWER ",
			" FROM PWD_HINT_QUESTIONS Q, PWD_HINT_ANSWERS A, USERS U ",
			" WHERE U.USERID = A.USERID ",
			" AND Q.PH_QUESTIONID = A.PH_QUESTIONID ",
			" AND UPPER(U.USERNAME) = UPPER(?) ",
			" AND U.ACTIVATION_STATUS in ('SS','AC') ",
			" ORDER BY A.PH_ANSWERID");

	// Added by Ravi for Manage Profile
	public static final String DELETE_ANSWER_DATA = CustomStringUtil.appendString(
			"DELETE FROM PWD_HINT_ANSWERS WHERE USERID = ?");
	// Added by Ravi for Manage Profile
	public static final String UPDATE_USER_DATA = CustomStringUtil.appendString(
			"UPDATE USERS SET LAST_NAME = ?, FIRST_NAME = ?, EMAIL_ADDRESS = ?, PHONE_NO = ?, COUNTRY = ?, ZIPCODE = ?, STATE=? , STREET=?, CITY=?, DISPLAY_USERNAME=?,UPDATED_DATE_TIME = sysdate WHERE USERID = ?");

	//Fix to implement cust_prod_id properly - By Joy
	public static final String GET_PARENT_DETAILS_ON_SCROLL = CustomStringUtil.appendString(
			"SELECT ABC.USERID, ABC.USERNAME, ABC.FULLNAME, ABC.STATUS,TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,",
			" ABC.ORG_NODE_NAME, ABC.ORG_NODEID FROM (SELECT USR.USERID, USR.USERNAME,USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,",
			" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT,HIER.ORG_NODE_NAME, HIER.ORG_NODEID FROM USERS USR,USER_ROLE UR,ORG_USERS OU,",
			" (SELECT * FROM ORG_NODE_DIM WHERE ORG_MODE = ?  START WITH ORG_NODEID = ? CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER ",
			" WHERE USR.USERID=OU.USERID and OU.ORG_NODEID=HIER.ORG_NODEID",
			" AND USR.USERID = UR.USERID  AND UR.ROLEID = ?",
			" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL WHERE OPL.ORG_NODEID = HIER.ORG_NODEID AND OPL.CUST_PROD_ID = ?)",
			" AND UPPER(USR.USERNAME) > UPPER(?) ORDER BY UPPER(USR.USERNAME)) ABC WHERE ROWNUM <= 15 ORDER BY UPPER(ABC.USERNAME)");

	//Fix to implement cust_prod_id properly - By Joy
	public static final String GET_PARENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM = CustomStringUtil.appendString(
					"SELECT ABC.USERID, ABC.USERNAME,ABC.FULLNAME, ABC.STATUS,TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT, ",
					" ABC.ORG_NODE_NAME, ABC.ORG_NODEID FROM (SELECT USR.USERID, USR.USERNAME,USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
					" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT, HIER.ORG_NODE_NAME, HIER.ORG_NODEID ",
					" FROM USERS USR,USER_ROLE UR, ORG_USERS OU,(SELECT * FROM ORG_NODE_DIM WHERE ORG_MODE = ? START WITH ORG_NODEID = ? CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID ) HIER ",
					" WHERE USR.USERID=OU.USERID and OU.ORG_NODEID=HIER.ORG_NODEID",
					" AND USR.USERID = UR.USERID AND UR.ROLEID = ?",
					" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL WHERE OPL.ORG_NODEID = HIER.ORG_NODEID AND OPL.CUST_PROD_ID = ?)",
					" AND UPPER(USR.USERNAME) > UPPER(?) AND (UPPER(USR.USERNAME) LIKE UPPER(?) OR UPPER(USR.LAST_NAME) LIKE UPPER(?) OR UPPER(USR.FIRST_NAME) LIKE UPPER(?)) ORDER BY UPPER(USR.USERNAME)) ABC WHERE ROWNUM <= 15 ORDER BY UPPER(ABC.USERNAME)");

	// Query to retrieve list of children of the logged in parent.
	public static final String SEARCH_CHILDREN = CustomStringUtil.appendString(
			"SELECT ST.LAST_NAME || ',' || ST.FIRST_NAME || ' ' || ST.MIDDLE_NAME AS STUDENT_NAME,",
			" ST.STUDENT_BIO_ID STUDENT_BIO_ID,",
			" GRD.GRADE_NAME AS STUDENTGRADE,",
			" AD.ADMIN_SEASON || ' ' || AD.ADMIN_YEAR AS ADMIN_SEASON_YEAR,",
			" AD.ADMIN_SEQ,",
			" AD.ADMINID ADMINID",
			" FROM STUDENT_BIO_DIM ST,",
			" ADMIN_DIM AD,",
			" GRADE_DIM GRD,",
			" INVITATION_CODE IC,",
			" INVITATION_CODE_CLAIM ICC,",
			" ORG_USERS OU,",
			" USERS U ",
			" WHERE ST.ADMINID = AD.ADMINID",
			" AND ST.ADMINID = IC.ADMINID",
			" AND ST.GRADEID = GRD.GRADEID",
			" AND IC.ICID = ICC.ICID",
			" AND ICC.ORG_USER_ID = OU.ORG_USER_ID",
			" AND OU.USERID = U.USERID ",
			" and ic.org_nodeid = st.org_nodeid ",
			" and icc.activation_status = 'AC' ",
			" AND IC.activation_status = 'AC'",
			" AND upper(U.USERNAME) = upper(?) ",
			" ORDER BY AD.ADMIN_SEQ DESC");

	/**
	 * Query to search parent.
	 */
	//Fix to implement cust_prod_id properly - By Joy
	public static final String SEARCH_PARENT = CustomStringUtil.appendString(
			"SELECT ABC.USERID, ABC.USERNAME,ABC.FULLNAME, ABC.STATUS,ABC.LAST_NAME,ABC.FIRST_NAME,TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,",
			" ABC.ORG_NODE_NAME, ABC.ORG_NODEID FROM (SELECT USR.USERID, ",
			" USR.USERNAME, USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
			" USR.LAST_NAME,USR.FIRST_NAME,USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT,HIER.ORG_NODE_NAME, ",
			" HIER.ORG_NODEID FROM USERS USR ,USER_ROLE UR, ORG_USERS OU, (SELECT * FROM ORG_NODE_DIM WHERE ORG_MODE = ? START WITH ORG_NODEID = ? ",
			" CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER WHERE USR.USERID=OU.USERID ",
			" and OU.ORG_NODEID=HIER.ORG_NODEID",
			" AND USR.USERID = UR.USERID  AND UR.ROLEID = ?",
			" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL WHERE OPL.ORG_NODEID = HIER.ORG_NODEID AND OPL.CUST_PROD_ID = ?)",
			" AND (UPPER(USR.USERNAME) LIKE UPPER(?) ",
			" OR UPPER(USR.LAST_NAME) LIKE UPPER(?) OR UPPER(USR.FIRST_NAME) LIKE UPPER(?)) ORDER BY UPPER(USR.USERNAME)) ABC ",
			" WHERE ROWNUM<=?");

	
	public static final String SEARCH_PARENT_EXACT = CustomStringUtil.appendString(
			"SELECT ABC.USERID, ABC.USERNAME,ABC.FULLNAME, ABC.STATUS,ABC.LAST_NAME,ABC.FIRST_NAME,TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,",
			" ABC.ORG_NODE_NAME, ABC.ORG_NODEID FROM (SELECT USR.USERID, ",
			" USR.USERNAME, USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
			" USR.LAST_NAME,USR.FIRST_NAME,USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT,HIER.ORG_NODE_NAME, ",
			" HIER.ORG_NODEID FROM USERS USR, USER_ROLE UR ,ORG_USERS OU, (SELECT * FROM ORG_NODE_DIM WHERE ORG_MODE = ? START WITH ORG_NODEID = ? ",
			" CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER WHERE USR.USERID=OU.USERID ",
			" and OU.ORG_NODEID=HIER.ORG_NODEID",
			" AND USR.USERID = UR.USERID AND UR.ROLEID = ?",
			" AND EXISTS (SELECT 1 FROM ORG_PRODUCT_LINK OPL WHERE OPL.ORG_NODEID = HIER.ORG_NODEID AND OPL.CUST_PROD_ID = ?)",
			" AND (UPPER(USR.USERNAME) LIKE UPPER(?)) ",
			" ORDER BY UPPER(USR.USERNAME)) ABC ",
			" WHERE ROWNUM<=?");

	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String GET_STUDENT_DETAILS_MANAGE_STUDENT = "PKG_MANAGE_STUDENT.SP_GET_STUDENT_DETAILS(?,?,?,?,?,?,?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String GET_PARENT_DETAILS_FOR_STUDENT = "PKG_MANAGE_STUDENT.SP_GET_PARENT_FOR_STUDENT(?,?,?,?)";

	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * Fix for Amit Da's mail: RE: Latest code is deployed into DEV and QA (in manage student view assessment tab is not opening)[IC.TEST_ELEMENT_ID - missing] - By Joy
	 */
	public static final String GET_ASSESSMENT_LIST = "PKG_MANAGE_STUDENT.SP_GET_ASSESSMENT_FOR_STUDENT(?,?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * Fix for TD 78101 - Add exists clause with ORG_PRODUCT_LINK 
	 * and Use Exists clause instead of in clause for ORG_NODEID for better performance - By Joy
	 * */
	public static final String SEARCH_STUDENT = "PKG_MANAGE_STUDENT.SP_SEARCH_STUDENT(?,?,?,?,?,?,?,?)";

	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String SEARCH_STUDENT_ON_REDIRECT = "PKG_MANAGE_STUDENT.SP_SEARCH_STUDENT_REDIRECT(?,?,?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String UPDATE_ASSESSMENT = "PKG_MANAGE_STUDENT.SP_UPDATE_ASSESSMENT(?,?,?,?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 *  Fix for 78188 - By Joy
	 *  Query modified for Reset Activation Code issue - By Joy
	 */
	public static final String REGENERATE_ACTIVATION_CODE = "PKG_MANAGE_STUDENT.SP_REGENERATE_ACTIVATION_CODE(?,?,?,?)";

	/**
	 * update user profile when they login for the first time
	 */
	public static final String UPDATE_FIRSTTIMEUSERLOGIN_DATA = CustomStringUtil.appendString(
			" UPDATE USERS SET LAST_NAME = ?, ",
			" FIRST_NAME =?, ",
			" EMAIL_ADDRESS = ? , PHONE_NO =? , COUNTRY = ? , ",
			" ZIPCODE=? , STATE=? , STREET=? ,CITY=?",
			" WHERE USERID = ? ");

	/**
	 * update user profile when they login for the first time
	 */
	public static final String UPDATE_FIRSTTIMEUSERFLAG_DATA = " UPDATE USERS SET IS_FIRSTTIME_LOGIN = ? WHERE upper(USERNAME) =upper(?)";
	public static final String UPDATE_PASSWORD_DATA = " UPDATE USERS SET IS_FIRSTTIME_LOGIN = ?, password = ? , salt = ? WHERE upper(USERNAME) =upper(?)";

	public static final String FETCH_SCHOOLID_FOR_STUDENT = CustomStringUtil.appendString(
			" select ORG.level3_jasper_orgid from student_bio_dim std, ORG_NODE_DIM org ",
			" where ORG.ORG_NODEID = std.ORG_NODEID and std.student_bio_id = ? ");

	public static final String STUDENT_LIST_FOR_TREE = CustomStringUtil.appendString(
			" select last_name || ' ' || first_name || ' ' || middle_name NAME, grd.grade_name GRADE, stu.student_bio_id ID from student_bio_dim stu, grade_dim grd ",
			" where stu.gradeid = grd.gradeid and org_nodeid = (select org_lstnodeid from ORG_LSTNODE_LINK where org_nodeid = ? and adminid = ? and rownum = 1) and adminid = ? and customerid = ? and stu.gradeid = ? ");

	public static final String GET_TEST_ADMINISTRATION = "PKG_MANAGE_CONTENT.SP_GET_CUST_PROD_DETAILS(?,?,?,?)";

	public static final String GET_GRADE = "PKG_MANAGE_CONTENT.SP_GET_GRADE_DETAILS(?,?,?)";

	public static final String GET_SUBTEST = "PKG_MANAGE_CONTENT.SP_GET_SUBTEST_DETAILS(?,?,?,?)";

	public static final String GET_OBJECTIVE = "PKG_MANAGE_CONTENT.SP_GET_OBJECTIVE_DETAILS(?,?,?,?)";

	public static final String ADD_NEW_CONTENT = "PKG_MANAGE_CONTENT.SP_ADD_NEW_CONTENT(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String GET_MANAGE_CONTENT_LIST = "PKG_MANAGE_CONTENT.SP_GET_CONTENT_DETAILS(?,?,?,?,?,?)";

	public static final String GET_MANAGE_CONTENT_LIST_MORE = "PKG_MANAGE_CONTENT.SP_GET_CONTENT_DETAILS_MORE(?,?,?,?,?,?,?)";

	public static final String GET_MANAGE_CONTENT_FOR_EDIT = "PKG_MANAGE_CONTENT.SP_GET_CONTENT_DETAILS_EDIT(?,?,?)";

	public static final String UPDATE_CONTENT = "PKG_MANAGE_CONTENT.SP_UPDATE_CONTENT_DETAILS(?,?,?,?,?,?,?)";

	public static final String DELETE_CONTENT = "PKG_MANAGE_CONTENT.SP_DELETE_CONTENT_DETAILS(?,?,?)";

	public static final String GET_OBJECTIVE_DETAILS_FOR_EDIT = "PKG_MANAGE_CONTENT.SP_GET_OBJECTIVE_DETAILS_EDIT(?,?)";

	public static final String GET_GENERIC_DETAILS_FOR_EDIT = "PKG_MANAGE_CONTENT.SP_GET_GENERIC_DETAILS_EDIT(?,?,?,?,?,?,?,?)";

	public static final String GET_STUDENT_SUBTEST = "PKG_PARENT_NETWORK.SP_GET_SUBTEST_DETAILS(?,?,?)";

	public static final String GET_ARTICLE_TYPE_DETAILS = "PKG_PARENT_NETWORK.SP_GET_ARTICLE_TYPE_DETAILS(?,?,?,?,?,?,?)";

	public static final String GET_ARTICLE_DESCRIPTION = "PKG_PARENT_NETWORK.SP_GET_ARTICLE_DESCRIPTION(?,?,?,?,?,?,?,?)";

	public static final String GET_GRADE_SUBTEST_INFO = "PKG_PARENT_NETWORK.SP_GET_GRADE_SUBTEST_DETAILS(?,?,?)";

	public static final String GET_STUDENT_DETAILS = "PKG_PARENT_NETWORK.SP_GET_STUDENT_DETAILS(?,?,?)";
	
	public static final String GET_STUDENT_DETAILS_ADMIN = "PKG_PARENT_NETWORK.SP_GET_STUDENT_DETAILS_ADMIN(?,?,?,?,?,?)";
	
	public static final String CREATE_PARENT="Pkg_Admin_Module.Sp_Create_Parent(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
}