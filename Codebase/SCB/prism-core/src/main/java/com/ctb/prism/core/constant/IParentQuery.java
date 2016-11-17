package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IParentQuery {

	/**
	 * Moved to package and reduce DB call - By Joy
	 * Fix for TD 78161 - By Joy
	 */
	public static final String VALIDATE_INVITATION_CODE = "FACT.PKG_PARENT_NETWORK$SP_VALIDATE_IC(?,?,?)";

	/**
	 * Moved to package - By Joy
	 * Fix for TD 78161 - By Joy
	 */
	public static final String GET_STUDENT_FOR_INVITATION_CODE = "FACT.PKG_PARENT_NETWORK$SP_GET_STUDENT_FOR_IC(?,?)";

	/**
	 * Query to assign user role to the new registered user
	 */
	public static final String ADD_ROLE_TO_REGISTERED_USER = "INSERT INTO USER_ROLE SELECT (SELECT USERID FROM USERS WHERE upper(USERNAME) LIKE upper(?)),(SELECT ROLEID FROM ROLE WHERE ROLE_NAME LIKE ?),SYSDATETIME(),SYSDATETIME()";

	/**
	 * @author Joy
	 * Query to add new student to parent
	 */
	public static final String ADD_INVITATION_CODE_TO_ACCOUNT = "FACT.PKG_PARENT_NETWORK$SP_CLAIM_INVITATION(?,?,?,?)";

	public static final String GET_ACCOUNT_DETAILS = "FACT.PKG_MY_ACCOUNT$SP_GET_ACCOUNT_DETAILS(?,?)";

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
	public static final String UPDATE_USER_DATA = "FACT.PKG_MY_ACCOUNT$SP_UPDATE_USER_ACCOUNT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	// Query to retrieve list of children of the logged in parent.
	public static final String SEARCH_CHILDREN = CustomStringUtil.appendString(
			"SELECT ISNULL(ST.LAST_NAME,'') + ',' +ISNULL(ST.FIRST_NAME,'') + ' ' +ISNULL(ST.MIDDLE_NAME,'') AS STUDENT_NAME,",
			" ST.STUDENT_BIO_ID STUDENT_BIO_ID,",
			" GRD.GRADE_NAME AS STUDENTGRADE,",
			" ISNULL(AD.ADMIN_SEASON,'')+ ' '+ISNULL(AD.ADMIN_YEAR,'') AS ADMIN_SEASON_YEAR,",
			" AD.ADMIN_SEQ,",
			" AD.ADMINID ADMINID",
			" FROM STUDENT_BIO_DIM ST,",
			" ADMIN_DIM AD,",
			" GRADE_DIM GRD,",
			" INVITATION_CODE IC,",
			" INVITATION_CODE_CLAIM ICC,",
			" ORG_USERS OU,",
			" USERS U",
			" WHERE ST.ADMINID = AD.ADMINID",
			" AND ST.ADMINID = IC.ADMINID",
			" AND ST.GRADEID = GRD.GRADEID",
			" AND IC.ICID = ICC.ICID",
			" AND ICC.ORG_USER_ID = OU.ORG_USER_ID",
			" AND OU.USERID = U.USERID",
			" and ic.org_nodeid = st.org_nodeid",
			" and icc.activation_status = 'AC'",
			" AND IC.activation_status = 'AC'",
			" AND upper(U.USERNAME) = upper(?)",
			" ORDER BY AD.ADMIN_SEQ DESC");
	
	/*
	 * Moved to PKG_MANAGE_PARENT by Joy
	 */
	public static final String GET_PARENT_DETAILS_MANAGE_PARENT = "{ CALL FACT.PKG_MANAGE_PARENT$SP_GET_PARENT_DETAILS(?, ?, ?, ?, ?, ?, ?, ?)}";
	
	/*
	 * Moved to PKG_MANAGE_PARENT by Joy
	 * Query to search parent auto complete
	 */
	public static final String SEARCH_PARENT = "FACT.PKG_MANAGE_PARENT$SP_SEARCH_PARENT(?,?,?,?,?,?,?,?)";

	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String GET_STUDENT_DETAILS_MANAGE_STUDENT = "{ CALL FACT.PKG_MANAGE_STUDENT$SP_GET_STUDENT_DETAILS(?, ?, ?, ?, ?, ?, ?,  ?)}";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String GET_PARENT_DETAILS_FOR_STUDENT = "FACT.PKG_MANAGE_STUDENT$SP_GET_PARENT_FOR_STUDENT(?,?,?)";

	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * Fix for Amit Da's mail: RE: Latest code is deployed into DEV and QA (in manage student view assessment tab is not opening)[IC.TEST_ELEMENT_ID - missing] - By Joy
	 */
	public static final String GET_ASSESSMENT_LIST = "FACT.PKG_MANAGE_STUDENT$SP_GET_ASSESSMENT_FOR_STUDENT(?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 * Fix for TD 78101 - Add exists clause with ORG_PRODUCT_LINK 
	 * and Use Exists clause instead of in clause for ORG_NODEID for better performance - By Joy
	 * */
	public static final String SEARCH_STUDENT = "FACT.PKG_MANAGE_STUDENT$SP_SEARCH_STUDENT(?,?,?,?,?,?,?)";

	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String SEARCH_STUDENT_ON_REDIRECT = "FACT.PKG_MANAGE_STUDENT$SP_SEARCH_STUDENT_REDIRECT(?,?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 */
	public static final String UPDATE_ASSESSMENT = "FACT.PKG_MANAGE_STUDENT$SP_UPDATE_ASSESSMENT(?,?,?,?,?)";
	
	/*
	 * Moved to PKG_MANAGE_STUDENT by Joy
	 *  Fix for 78188 - By Joy
	 *  Query modified for Reset Activation Code issue - By Joy
	 */
	public static final String REGENERATE_ACTIVATION_CODE = "FACT.PKG_MANAGE_STUDENT$SP_REGENERATE_ACTIVATION_CODE(?,?,?,?,?)";

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
	public static final String UPDATE_PASSWORD_HISTORY = CustomStringUtil.appendString(
			" insert into PASSWORD_HISTORY (PWD_HISTORYID, PASSWORD, USERID, CREATED_DATE_TIME, ",
			" UPDATED_DATE_TIME) SELECT NEXT VALUE FOR SEQ_PASSWORD_HISTORY, ?,",
			" (select userid from users  where upper(USERNAME) =upper(?)), sysdatetime(), sysdatetime() ");
	
	public static final String SP_RESET_PASSWORD ="{CALL FACT.PKG_MANAGE_USERS$SP_RESET_PASSWORD(?, ?, ?, ?, ?)}";

	public static final String FETCH_SCHOOLID_FOR_STUDENT = CustomStringUtil.appendString(
			" select ORG.ORG_NODEID from student_bio_dim std, ORG_NODE_DIM org ",
			" where ORG.ORG_NODEID = std.ORG_NODEID and std.student_bio_id = ? ");

	public static final String STUDENT_LIST_FOR_TREE = CustomStringUtil.appendString(
			" select ISNULL(last_name,'')+' '+ISNULL(first_name,'')+' '+ISNULL(middle_name,'') NAME, grd.grade_name GRADE, stu.student_bio_id ID from student_bio_dim stu, grade_dim grd ",
			" where stu.gradeid = grd.gradeid and org_nodeid = (select TOP(1) org_lstnodeid from ORG_LSTNODE_LINK where org_nodeid = ? and adminid = ?) and adminid = ? and customerid = ? and stu.gradeid = ? ");

	public static final String GET_TEST_ADMINISTRATION = "FACT.PKG_MANAGE_CONTENT$SP_GET_CUST_PROD_DETAILS(?,??)";

	public static final String GET_GRADE = "FACT.PKG_MANAGE_CONTENT$SP_GET_GRADE_DETAILS(?,?)";

	public static final String GET_SUBTEST = "FACT.PKG_MANAGE_CONTENT$SP_GET_SUBTEST_DETAILS(?,?,?)";

	public static final String GET_OBJECTIVE = "FACT.PKG_MANAGE_CONTENT$SP_GET_OBJECTIVE_DETAILS(?,?,?)";
	
	public static final String GET_PERFORMANCE_LEVEL = "FACT.PKG_MANAGE_CONTENT$SP_GET_PERFORMANCE_LEVEL(?,?,?)";

	public static final String ADD_NEW_CONTENT = "FACT.PKG_MANAGE_CONTENT$SP_ADD_NEW_CONTENT(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String GET_MANAGE_CONTENT_LIST = "{CALL FACT.PKG_MANAGE_CONTENT$SP_GET_CONTENT_DETAILS(?, ?, ?, ?, ?, ?, ?)}";

	public static final String GET_MANAGE_CONTENT_LIST_MORE = "{ CALL FACT.PKG_MANAGE_CONTENT$SP_GET_CONTENT_DETAILS_MORE(?, ?, ?, ?, ?,  ?, ?)}";

	public static final String GET_MANAGE_CONTENT_FOR_EDIT = "FACT.PKG_MANAGE_CONTENT$SP_GET_CONTENT_DETAILS_EDIT(?,?)";

	public static final String UPDATE_CONTENT = "FACT.PKG_MANAGE_CONTENT$SP_UPDATE_CONTENT_DETAILS(?,?,?,?,?,?,?)";

	public static final String DELETE_CONTENT = "FACT.PKG_MANAGE_CONTENT$SP_DELETE_CONTENT_DETAILS(?,?,?)";

	public static final String GET_OBJECTIVE_DETAILS_FOR_EDIT = "FACT.PKG_MANAGE_CONTENT$SP_GET_OBJECTIVE_DETAILS_EDIT(?,?)";

	public static final String GET_GENERIC_DETAILS_FOR_EDIT = "FACT.PKG_MANAGE_CONTENT$SP_GET_GENERIC_DETAILS_EDIT(?,?,?,?,?,?,?)";

	public static final String GET_STUDENT_SUBTEST = "FACT.PKG_PARENT_NETWORK$SP_GET_SUBTEST_DETAILS(?,?)";

	public static final String GET_ARTICLE_TYPE_DETAILS = "FACT.PKG_PARENT_NETWORK$SP_GET_ARTICLE_TYPE_DETAILS(?,?,?,?,?,?)";

	public static final String GET_ARTICLE_DESCRIPTION = "FACT.PKG_PARENT_NETWORK$SP_GET_ARTICLE_DESCRIPTION(?,?,?,?,?,?,?)";

	public static final String GET_GRADE_SUBTEST_INFO = "FACT.PKG_PARENT_NETWORK$SP_GET_GRADE_SUBTEST_DETAILS(?,?)";

	public static final String GET_STUDENT_DETAILS = "FACT.PKG_PARENT_NETWORK$SP_GET_STUDENT_DETAILS(?,?)";
	
	public static final String GET_STUDENT_DETAILS_ADMIN = "FACT.PKG_PARENT_NETWORK$SP_GET_STUDENT_DETAILS_ADMIN(?,?,?,?,?)";
	
	public static final String CREATE_PARENT="FACT.Pkg_Admin_Module$Sp_Create_Parent(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
}