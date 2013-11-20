package com.ctb.prism.core.constant;

import com.ctb.prism.core.util.CustomStringUtil;

/**
 * This interface contains the constants used in the application
 * 
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public interface IParentQuery {
	
	public static final String VALIDATE_INVITATION_CODE = CustomStringUtil
	.appendString(
			" SELECT INV.ICID," ,
			" INV.TOTAL_AVAILABLE_IC_CLAIM," , 
			" INV.TOTAL_ATTEMPT_IC_CLAIM," ,
			" DECODE(SIGN(INV.INVITATION_EXPIRATION_DATE - SYSDATE),-1,'IN','AC') AS EXPIRATION_STATUS," ,
			" INV.ACTIVATION_STATUS AS ACTIVATION_STATUS",
			" FROM INVITATION_CODE INV" ,
			" WHERE INV.INVITATION_CODE = ?" ,
			" AND INV.ACTIVATION_STATUS = 'AC'" ,
			" AND rownum = 1");
			

	public static final String GET_STUDENT_FOR_INVITATION_CODE = CustomStringUtil.appendString(
			" SELECT STD.FIRST_NAME || ' ' || STD.MIDDLE_NAME || ' ' || STD.LAST_NAME AS STUDENT_NAME," ,
			     " ADM.ADMIN_NAME AS ADMINISTRATION, " ,
			     " 'Grade ' || grd.grade_name AS GRADE " ,
			     " FROM INVITATION_CODE IC, STUDENT_BIO_DIM STD, ADMIN_DIM ADM, GRADE_DIM GRD " ,
			     " WHERE STD.ADMINID = IC.ADMINID " ,
			     " AND ADM.ADMINID = STD.ADMINID " ,
			     " and ic.org_nodeid = std.org_nodeid ",
			     " AND GRD.GRADEID = STD.GRADEID AND IC.ACTIVATION_STATUS = 'AC' " ,
			     " AND IC.INVITATION_CODE = ? ");
	
	//Query to insert newly registered user
	public static final String INSERT_USER_DATA = CustomStringUtil.appendString(
			" INSERT INTO USERS  " ,
			" (USERID,   USERNAME,   DISPLAY_USERNAME, " ,
			"  LAST_NAME,   FIRST_NAME,   MIDDLE_NAME, " ,
			"  EMAIL_ADDRESS,   PHONE_NO,   COUNTRY, " ,
			"  ZIPCODE,  STREET, CITY, STATE, ORG_ID, " ,
			"  ORG_LEVEL,   IS_FIRSTTIME_LOGIN,  ADMINID, " ,
			"  ACTIVATION_STATUS,   CREATED_DATE_TIME)" ,
			"  VALUES  (?, ?, ?, ?, ?, '', ?, ?, ?, ?, ?, ?,?," ,
			"  (select DISTINCT level3_jasper_orgid from org_node_dim org, invitation_code inv where inv.org_nodeid = org.org_nodeid and INV.ACTIVATION_STATUS = 'AC' AND inv.invitation_code = ?), " ,
			"  0, ?,  (SELECT adminid FROM ADMIN_DIM ADM WHERE ADM.IS_CURRENT_ADMIN = 'Y'), " ,
			"  'AC',   SYSDATE)");
	
	public static final String INSERT_USER_DATA_WITH_PASSWD = CustomStringUtil.appendString(
			" INSERT INTO USERS  " ,
			" (USERID,   USERNAME,   DISPLAY_USERNAME, " ,
			"  LAST_NAME,   FIRST_NAME,   MIDDLE_NAME, " ,
			"  EMAIL_ADDRESS,   PHONE_NO,   COUNTRY, " ,
			"  ZIPCODE,  STREET, CITY, STATE, ORG_ID, " ,
			"  ORG_LEVEL,   IS_FIRSTTIME_LOGIN,  ADMINID, " ,
			"  ACTIVATION_STATUS,   CREATED_DATE_TIME, PASSWORD, SALT)" ,
			"  VALUES  (?, ?, ?, ?, ?, '', ?, ?, ?, ?, ?, ?,?," ,
			"  (select DISTINCT level3_jasper_orgid from org_node_dim org, invitation_code inv where inv.org_nodeid = org.org_nodeid and INV.ACTIVATION_STATUS = 'AC' AND inv.invitation_code = ?), " ,
			"  0, ?,  (SELECT adminid FROM ADMIN_DIM ADM WHERE ADM.IS_CURRENT_ADMIN = 'Y'), " ,
			"  'AC',   SYSDATE, ?, ?)");
	
	//Query to assign user role to the new registered user
	public static final String ADD_ROLE_TO_REGISTERED_USER = CustomStringUtil.appendString(
			"INSERT INTO USER_ROLE VALUES ((SELECT USERID FROM USERS WHERE upper(USERNAME) LIKE upper(?)),(SELECT ROLE_ID FROM ROLE WHERE ROLE_NAME LIKE ?),SYSDATE,SYSDATE)");
		
	
	public static final String INSERT_INVITATION_CODE_CLAIM_DATA =  CustomStringUtil.appendString(
			" INSERT INTO INVITATION_CODE_CLAIM  " ,
			" (INVITATION_CODE_CLAIM_ID,   ORG_USER_ID,   ICID, " ,
			" ACTIVATION_STATUS,   CREATED_DATE_TIME)" ,
			" VALUES  " ,
			" (INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL,   ?,   (SELECT ICID FROM INVITATION_CODE INV where INV.ACTIVATION_STATUS = 'AC' and INV.INVITATION_CODE = ? and rownum = 1), " ,
			" 'AC',  SYSDATE)");
	
	public static final String UPDATE_INVITATION_CODE_CLAIM_COUNT =CustomStringUtil.appendString(
			" UPDATE INVITATION_CODE " ,
			" SET TOTAL_ATTEMPT_IC_CLAIM = (SELECT TOTAL_ATTEMPT_IC_CLAIM " ,
			" FROM INVITATION_CODE " ,
			" WHERE INVITATION_CODE = ? AND ACTIVATION_STATUS = 'AC')+1 " ,
			" WHERE INVITATION_CODE = ?	");
	public static final String UPDATE_AVAILABLE_INVITATION_CODE_CLAIM_COUNT =CustomStringUtil.appendString(
			" UPDATE INVITATION_CODE " ,
			" SET TOTAL_AVAILABLE_IC_CLAIM = (SELECT TOTAL_AVAILABLE_IC_CLAIM " ,
			" FROM INVITATION_CODE " ,
			" WHERE INVITATION_CODE = ? AND ACTIVATION_STATUS = 'AC')-1 " ,
			" WHERE INVITATION_CODE = ?	");
	public static final String CHECK_AVAILABLE_INVITATION_CODE_CLAIM_COUNT =CustomStringUtil.appendString(
			" SELECT (IC.TOTAL_AVAILABLE_IC_CLAIM - TOTAL_ATTEMPT_IC_CLAIM) AS CLAIM_AVAILABILITY",
			" FROM INVITATION_CODE IC", 
			" WHERE UPPER(IC.INVITATION_CODE) = UPPER(?) AND IC.ACTIVATION_STATUS = 'AC' ");
	
	public static final String CHECK_INVITATION_CODE_CLAIM =CustomStringUtil.appendString(
			" SELECT INVITATION_CODE_CLAIM_ID" ,
			" FROM INVITATION_CODE_CLAIM ICC, INVITATION_CODE IC, USERS USR" ,
			" WHERE ICC.ORG_USER_ID = USR.USERID AND upper(USR.USERNAME) = upper(?) AND" ,
			" IC.ICID = ICC.ICID AND" ,
			" IC.INVITATION_CODE = ? AND IC.ACTIVATION_STATUS = 'AC' ");
	    		   
	public static final String ADD_INVITATION_CODE_TO_ACCOUNT =  CustomStringUtil.appendString(
			" INSERT INTO INVITATION_CODE_CLAIM  " ,
			" (INVITATION_CODE_CLAIM_ID,   ORG_USER_ID,   ICID, " ,
			" ACTIVATION_STATUS,   CREATED_DATE_TIME)" ,
			" VALUES  " ,
			" (INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL,   (SELECT USER_ID FROM USERS WHERE upper(USERNAME) = upper(?) ),   (SELECT ICID FROM INVITATION_CODE IC WHERE IC.INVITATION_CODE = ? AND IC.ACTIVATION_STATUS = 'AC'), " ,
			" 'AC',  SYSDATE)");
	
	public static final String GET_PARENT_DETAILS_ON_FIRST_LOAD = CustomStringUtil
	.appendString(  " SELECT ABC.USER_ID, ABC.USERNAME,",
					" ABC.FULLNAME, ABC.STATUS, " ,
					" TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT," ,
					" ABC.ORG_NAME, ABC.ORG_ID " ,
					" FROM (SELECT USR.USERID, USR.USERNAME, ",
					" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
					" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT, ",
					" HIER.ORG_NAME, HIER.ORG_ID ",
					" FROM USERS USR, (SELECT *  FROM ORG_HIERARCHY ",
					" START WITH ORG_ID = ?  ",
					" CONNECT BY PRIOR ORG_ID = ORG_PARENT_ID ",
					" ) HIER ",
					" WHERE HIER.ORG_ID = USR.ORG_ID AND USR.ORG_LEVEL = 0 ",
					" ORDER BY UPPER(USR.USERNAME)) ABC",
					" WHERE ROWNUM <= 15",
					" ORDER BY UPPER(ABC.USERNAME)");
	

	public static final String GET_PARENT_DETAILS_BY_USERNAME = CustomStringUtil
	.appendString(  " SELECT USERID, USERNAME, DISPLAY_USERNAME, LAST_NAME, FIRST_NAME, MIDDLE_NAME,",
					" EMAIL_ADDRESS, PHONE_NO, COUNTRY, ZIPCODE, STATE, STREET,CITY FROM USERS USR WHERE UPPER(USR.USERNAME)=UPPER(?)");
	

	public static final String GET_PARENT_SECURITY_QUESTION = CustomStringUtil
	.appendString( " SELECT Q.PH_QUESTIONID  AS QUESTION_ID, ", 
				   " Q.QUESTION_VALUE AS QUESTION,",
				   " Q.QUESTION_SEQ   AS SNO, ",
				   " A.PH_ANSWERID    AS ANSWER_ID, ",
				   " A.ANSWER_VALUE   AS ANSWER ",
	" FROM PWD_HINT_QUESTIONS Q, PWD_HINT_ANSWERS A, USERS U ",
	" WHERE U.USERID = A.USERID ",
	" AND Q.PH_QUESTIONID = A.PH_QUESTIONID ",
	" AND UPPER(U.USERNAME) = UPPER(?) ",
	" AND U.ACTIVATION_STATUS ='AC' ",
	" ORDER BY A.PH_ANSWERID");
	
	//Added by Ravi for Manage Profile
	public static final String DELETE_ANSWER_DATA = CustomStringUtil.appendString("DELETE FROM PWD_HINT_ANSWERS WHERE USERID = ?");
	//Added by Ravi for Manage Profile
	public static final String UPDATE_USER_DATA = CustomStringUtil.appendString("UPDATE USERS SET LAST_NAME = ?, FIRST_NAME = ?, EMAIL_ADDRESS = ?, PHONE_NO = ?, COUNTRY  = ?, ZIPCODE = ?, STATE=? , STREET=?, CITY=?, DISPLAY_USERNAME=?,UPDATED_DATE_TIME = sysdate  WHERE USERID = ?");
	

	public static final String GET_PARENT_DETAILS_ON_SCROLL = CustomStringUtil
	.appendString(  " SELECT ABC.USER_ID, ABC.USERNAME,",
					" ABC.FULLNAME, ABC.STATUS, " ,
					" TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT," ,
					" ABC.ORG_NAME, ABC.ORG_ID " ,
					" FROM (SELECT USR.USERID, USR.USERNAME, ",
					" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
					" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT, ",
					" HIER.ORG_NAME, HIER.ORG_ID ",
					" FROM USERS USR, (SELECT *  FROM ORG_HIERARCHY ",
					" START WITH ORG_ID = ?  ",
					" CONNECT BY PRIOR ORG_ID = ORG_PARENT_ID ",
					" ) HIER ",
					" WHERE HIER.ORG_ID = USR.ORG_ID AND USR.ORG_LEVEL = 0 ",
					" AND UPPER(USR.USERNAME) > UPPER(?) ", 
					" ORDER BY UPPER(USR.USERNAME)) ABC",
					" WHERE ROWNUM <= 15",
					" ORDER BY UPPER(ABC.USERNAME)");
	
	public static final String GET_PARENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM = CustomStringUtil
	.appendString(  " SELECT ABC.USER_ID, ABC.USERNAME,",
					" ABC.FULLNAME, ABC.STATUS, " ,
					" TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT," ,
					" ABC.ORG_NAME, ABC.ORG_ID " ,
					" FROM (SELECT USR.USERID, USR.USERNAME, ",
					" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, ",
					" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT, ",
					" HIER.ORG_NAME, HIER.ORG_ID ",
					" FROM USERS USR, (SELECT *  FROM ORG_HIERARCHY ",
					" START WITH ORG_ID = ?  ",
					" CONNECT BY PRIOR ORG_ID = ORG_PARENT_ID ",
					" ) HIER ",
					" WHERE USR.ORG_LEVEL = 0 ",
					" AND UPPER(USR.USERNAME) > UPPER(?) ", 
					" AND (UPPER(USR.USERNAME) LIKE UPPER(?) ",
					" OR UPPER(USR.LAST_NAME) LIKE UPPER(?)",
					" OR UPPER(USR.FIRST_NAME) LIKE UPPER(?))", 
					" ORDER BY UPPER(USR.USERNAME)) ABC",
					" WHERE ROWNUM <= 15",
					" ORDER BY UPPER(ABC.USERNAME)");

	// Query to retrieve list of children of the logged in parent.
	public static final String SEARCH_CHILDREN = CustomStringUtil.appendString(
		"SELECT ST.LAST_NAME || ',' || ST.FIRST_NAME || ' ' || ST.MIDDLE_NAME AS STUDENT_NAME,",
           " ST.STUDENT_BIO_ID,",
           " GRD.GRADE_NAME AS STUDENTGRADE,",
           " AD.ADMIN_SEASON || ' ' || AD.ADMIN_YEAR AS ADMIN_SEASON_YEAR,",
           " AD.ADMIN_SEQ,",
           " AD.ADMINID",
        " FROM STUDENT_BIO_DIM  ST,",
           " ADMIN_DIM AD,",
           " GRADE_DIM GRD,",
           " INVITATION_CODE IC,",
           " INVITATION_CODE_CLAIM ICC,",
           " USERS U",              
	    " WHERE ST.ADMINID = AD.ADMINID",
	       " AND ST.ADMINID = IC.ADMINID",
	       " AND ST.GRADEID=GRD.GRADEID",
	       " AND ST.ADMINID = IC.ADMINID",
	       " AND IC.ICID = ICC.ICID",
	       " AND ICC.ORG_USER_ID = U.USERID",
	       " and ic.org_nodeid = st.org_nodeid ",
	       " and icc.activation_status = 'AC' ",
	       " AND IC.activation_status = 'AC'",
	       " AND upper(U.USERNAME) = upper(?) ",
	       " ORDER BY AD.ADMIN_SEQ DESC");
	// Query to search parent.
	public static final String SEARCH_PARENT = CustomStringUtil.appendString(
													  	" SELECT ABC.USER_ID, ABC.USERNAME,",
														" ABC.FULLNAME, ABC.STATUS,ABC.LAST_NAME,ABC.FIRST_NAME, " ,
														" TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT," ,
														" ABC.ORG_NAME, ABC.ORG_ID " ,
														" FROM (SELECT USR.USERID, USR.USERNAME, ",
														" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, USR.LAST_NAME,USR.FIRST_NAME, ",
														" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT, ",
														" HIER.ORG_NAME, HIER.ORG_ID ",
														" FROM USERS USR, (SELECT *  FROM ORG_HIERARCHY ",
														" START WITH ORG_ID = ?  ",
														" CONNECT BY PRIOR ORG_ID = ORG_PARENT_ID ",
														" ) HIER ",
														" WHERE HIER.ORG_ID = USR.ORG_ID AND USR.ORG_LEVEL = 0 ",
														" AND (UPPER(USR.USERNAME) LIKE UPPER(?) ",
														" OR UPPER(USR.LAST_NAME) LIKE UPPER(?)",
														" OR UPPER(USR.FIRST_NAME) LIKE UPPER(?))", 
														" ORDER BY UPPER(USR.USERNAME)) ABC",
														" WHERE ROWNUM<=?");
	
	public static final String SEARCH_PARENT_EXACT = CustomStringUtil.appendString(
								  	" SELECT ABC.USER_ID, ABC.USERNAME,",
									" ABC.FULLNAME, ABC.STATUS,ABC.LAST_NAME,ABC.FIRST_NAME, " ,
									" TO_CHAR(ABC.LAST_LOGIN_ATTEMPT,'MM/DD/YY') AS LAST_LOGIN_ATTEMPT," ,
									" ABC.ORG_NAME, ABC.ORG_ID " ,
									" FROM (SELECT USR.USERID, USR.USERNAME, ",
									" USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME, USR.LAST_NAME,USR.FIRST_NAME, ",
									" USR.ACTIVATION_STATUS AS STATUS, USR.LAST_LOGIN_ATTEMPT, ",
									" HIER.ORG_NAME, HIER.ORG_ID ",
									" FROM USERS USR, (SELECT *  FROM ORG_HIERARCHY ",
									" START WITH ORG_ID = ?  ",
									" CONNECT BY PRIOR ORG_ID = ORG_PARENT_ID ",
									" ) HIER ",
									" WHERE HIER.ORG_ID = USR.ORG_ID AND USR.ORG_LEVEL = 0 ",
									" AND UPPER(USR.USERNAME) LIKE UPPER(?) ",
									" ORDER BY UPPER(USR.USERNAME)) ABC",
									" WHERE ROWNUM<=?");
	
/*	public static final String GET_STUDENT_DETAILS_ON_FIRST_LOAD = CustomStringUtil
											.appendString( "SELECT STU.ROWIDENTIFIER,STU.STUDENTNAME, STU.STUDENT_BIO_ID , STU.STRUCTURE_ELEMENT,STU.STUDENTGRADE, STU.SCHOOL ",
															" FROM", 
															" (SELECT ST.ROWIDENTIFIER,ST.STUDENTNAME, ST.STUDENT_BIO_ID , ST.STRUCTURE_ELEMENT, GRD.GRADE_NAME AS STUDENTGRADE, ORG.level3_element_name AS SCHOOL ",
															" FROM (SELECT STD.LAST_NAME || ',' || STD.FIRST_NAME || ' ' || STD.MIDDLE_NAME AS STUDENTNAME,",
											                " STD.LAST_NAME||STD.FIRST_NAME||STD.MIDDLE_NAME||'_'||to_char(STD.STUDENT_BIO_ID)AS ROWIDENTIFIER,",
											                " STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,",
											                " STD.STRUCTURE_ELEMENT AS STRUCTURE_ELEMENT,",
											                " STD.GRADEID,",
											                " STD.ORG_NODEID",
												            " FROM STUDENT_BIO_DIM STD ) ST,", 
												            " GRADE_DIM GRD,",
												            " ORG_NODE_DIM ORG",
												            " WHERE ORG.ORG_NODEID = ST.ORG_NODEID", 
														    " AND ST.GRADEID = GRD.GRADEID",
														    " and ORG.adminid = ? ",
														    " AND (ORG.LEVEL1_JASPER_ORGID = ?", 
													        " OR ORG.LEVEL2_JASPER_ORGID = ?",
													        " OR ORG.LEVEL3_JASPER_ORGID =?",
													        " OR ORG.LEVEL4_JASPER_ORGID = ?)",
													        " ORDER BY ST.ROWIDENTIFIER )STU",
															" WHERE  ROWNUM  <=15");*/

	public static final String GET_STUDENT_DETAILS_ON_FIRST_LOAD = CustomStringUtil
											.appendString(  "Select * From ( ",
															" Select Std.Last_Name || Std.First_Name || Std.Middle_Name || '_' ||",
															" To_Char(Std.Student_Bio_Id) As Rowidentifier, ",
															" Std.Last_Name || ', ' || Std.First_Name || ' ' ||",
															" Std.Middle_Name As Studentname,",
															" Std.Student_Bio_Id,",
															" Std.test_element_id As TESTELEMENTID,",
															" Std.int_student_id As INTSTUDENTID,",
															" Std.ext_student_id As EXTSTUDENTID,",
															" Std.STUDENT_MODE,",
															" Grd.Grade_Name As Studentgrade,",
															" Org.org_node_name As School",
															" From Student_Bio_Dim Std, Grade_Dim Grd,Org_Node_Dim Org",
															" Where Org.Org_Nodeid = Std.Org_Nodeid",
															" And Std.Gradeid = Grd.Gradeid",
															" And std.Adminid= ?",
															" And org.org_nodeid In (Select org_lstnodeid From org_lstnode_link  Where org_nodeid =?)",
															" And org.customerId = std.customerId",
															" And org.customerId = ?",
															" Order By Rowidentifier)  Where Rownum <= 15");
													


	public static final String GET_STUDENT_DETAILS_ON_SCROLL = CustomStringUtil.appendString(
			"SELECT STU.ROWIDENTIFIER, STU.STUDENTNAME,STU.STUDENT_BIO_ID, ",
			" STU.TESTELEMENTID AS TESTELEMENTID,STU.INTSTUDENTID AS INTSTUDENTID,",
			" STU.EXTSTUDENTID AS EXTSTUDENTID, STU.STUDENTGRADE, STU.SCHOOL",
			" FROM (SELECT ST.ROWIDENTIFIER,ST.STUDENTNAME,ST.STUDENT_BIO_ID,",
			" ST.TESTELEMENTID,ST.INTSTUDENTID,ST.EXTSTUDENTID,",
			" GRD.GRADE_NAME AS STUDENTGRADE, ORG.ORG_NODE_NAME AS SCHOOL",
			" FROM (SELECT STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||",
			" STD.MIDDLE_NAME AS STUDENTNAME,",
			" STD.LAST_NAME || STD.FIRST_NAME || STD.MIDDLE_NAME || '_' ||",
			" TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,",
			" STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,",
			" STD.TEST_ELEMENT_ID AS TESTELEMENTID,",
			" STD.INT_STUDENT_ID AS INTSTUDENTID,",
			" STD.EXT_STUDENT_ID AS EXTSTUDENTID,",
			" STD.GRADEID,STD.ORG_NODEID,STD.CUSTOMERID",
			" FROM STUDENT_BIO_DIM STD",
			" WHERE STD.ADMINID = ?",
			" AND STD.CUSTOMERID = ?) ST,",
			" GRADE_DIM GRD, ORG_NODE_DIM ORG",
			" WHERE ORG.ORG_NODEID = ST.ORG_NODEID",
			" AND ST.GRADEID = GRD.GRADEID",
			" AND ST.CUSTOMERID = ORG.CUSTOMERID",
			" AND ORG.CUSTOMERID = ?",
			" AND ORG.ORG_NODEID IN (SELECT ORG_LSTNODEID FROM ORG_LSTNODE_LINK  WHERE ORG_NODEID = ?)",
			" AND ST.ROWIDENTIFIER > ?",
			" ORDER BY ST.ROWIDENTIFIER) STU",
			" WHERE ROWNUM <= 15");
	
	public static final String GET_STUDENT_DETAILS_ON_SCROLL_WITH_SRCH_PARAM = CustomStringUtil
	.appendString( "SELECT STU.ROWIDENTIFIER, STU.STUDENTNAME,STU.STUDENT_BIO_ID,",
			" STU.TESTELEMENTID AS TESTELEMENTID,STU.INTSTUDENTID AS INTSTUDENTID,",
			" STU.EXTSTUDENTID AS EXTSTUDENTID,STU.STUDENTGRADE,STU.SCHOOL",
			" FROM (SELECT ST.ROWIDENTIFIER,ST.STUDENTNAME,ST.STUDENT_BIO_ID,ST.TESTELEMENTID,",
			" ST.INTSTUDENTID,ST.EXTSTUDENTID,GRD.GRADE_NAME AS STUDENTGRADE,ORG.ORG_NODE_NAME AS SCHOOL",
			" FROM (SELECT STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||",
			" STD.MIDDLE_NAME AS STUDENTNAME," ,
			" STD.LAST_NAME || STD.FIRST_NAME || STD.MIDDLE_NAME || '_' ||",
			" TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,",
			" STD.STUDENT_BIO_ID AS STUDENT_BIO_ID, STD.TEST_ELEMENT_ID AS TESTELEMENTID,",
			" STD.INT_STUDENT_ID AS INTSTUDENTID,STD.EXT_STUDENT_ID AS EXTSTUDENTID,STD.GRADEID,STD.ORG_NODEID,STD.CUSTOMERID",
			" FROM STUDENT_BIO_DIM STD WHERE STD.ADMINID = ? AND STD.CUSTOMERID = ?) ST,",
			" GRADE_DIM GRD, ORG_NODE_DIM ORG",
			" WHERE ORG.ORG_NODEID = ST.ORG_NODEID AND ST.GRADEID = GRD.GRADEID",
			" AND ORG.CUSTOMERID = ST.CUSTOMERID AND ORG.CUSTOMERID = ? ",
			" AND ORG.ORG_NODEID IN (SELECT ORG_LSTNODEID FROM ORG_LSTNODE_LINK WHERE ORG_NODEID =?)",
			" AND ST.ROWIDENTIFIER > ? AND UPPER(ST.STUDENTNAME) LIKE UPPER(?)",
			" ORDER BY ST.ROWIDENTIFIER) STU",
			" WHERE ROWNUM <= 15");

	public static final String GET_PARENT_DETAILS_FOR_CHILDREN = CustomStringUtil.appendString(
															 " SELECT USR.USERNAME, IC.INVITATION_CODE, IC.ACTIVATION_STATUS ",
															 " FROM INVITATION_CODE_CLAIM ICC,",
															 " INVITATION_CODE       IC, ", 
															 " USERS                 USR,",
															 " STUDENT_BIO_DIM       STD",
															 " WHERE STD.STUDENT_BIO_ID = ?",
															 " AND STD.STRC_ELEMENT = IC.STU_STRC_ELEMENT",
															 " AND IC.ICID = ICC.ICID",
															 " AND IC.ADMINID = STD.ADMINID",
															 " AND IC.ACTIVATION_STATUS = 'AC'",
															 " AND ICC.ORG_USER_ID = USR.USERID",
															 " AND USR.CUSTOMERID = STD.CUSTOMERID",
															 " AND USR.CUSTOMERID = ?");
	
	public static final String GET_ASSESSMENT_LIST = CustomStringUtil.appendString(
															 " SELECT STD.STUDENT_BIO_ID, ",
															 " IC.INVITATION_CODE,TO_CHAR(IC.INVITATION_EXPIRATION_DATE,'mm/dd/yyyy') AS EXPIRATION_DATE, ",
															 " IC.TOTAL_AVAILABLE_IC_CLAIM,",
															 " DECODE(SIGN(IC.INVITATION_EXPIRATION_DATE - SYSDATE),-1,'Expired','Active') AS EXPIRATION_STATUS, ",
															 " ADM.ADMIN_SEASON || ' ' || ADM.ADMIN_YEAR AS ASSESSMENT_YEAR ",
															 " FROM INVITATION_CODE IC, STUDENT_BIO_DIM STD, ADMIN_DIM ADM",
															 " WHERE IC.ADMINID = STD.ADMINID  ",
															 " AND IC.ACTIVATION_STATUS = 'AC' ",
															 " AND IC.ADMINID=STD.ADMINID",
															 " AND ADM.ADMINID=IC.ADMINID",
															 " AND STD.STUDENT_BIO_ID = ?");
	
	public static final String SEARCH_STUDENT = CustomStringUtil
	.appendString( "SELECT STU.ROWIDENTIFIER,STU.STUDENTNAME,STU.STUDENT_BIO_ID, ",
				   " STU.TESTELEMENT,STU.GRADE_NAME AS STUDENTGRADE,STU.SCHOOL AS SCHOOL",
				   " FROM (SELECT ST.ROWIDENTIFIER,ST.STUDENTNAME,ST.STUDENT_BIO_ID,",
				   " ST.TESTELEMENT,GRD.GRADE_NAME,ORG.ORG_NODE_NAME AS SCHOOL",
				   " FROM (SELECT STD.LAST_NAME || ',' || STD.FIRST_NAME || ' ' ||",
				   " STD.MIDDLE_NAME AS STUDENTNAME,",
				   " STD.LAST_NAME || STD.FIRST_NAME || STD.MIDDLE_NAME || '_' ||",
				   " TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,",
				   " STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,",
				   " STD.TEST_ELEMENT_ID AS TESTELEMENT,",
				   " STD.GRADEID, STD.ORG_NODEID, STD.CUSTOMERID",
				   " FROM STUDENT_BIO_DIM STD",
				   " WHERE STD.ADMINID = ?) ST, GRADE_DIM GRD,ORG_NODE_DIM ORG",
				   " WHERE ORG.ORG_NODEID = ST.ORG_NODEID",
				   " AND ST.GRADEID = GRD.GRADEID ",
				   " AND ORG.ORG_NODEID In (Select org_lstnodeid From org_lstnode_link  Where org_nodeid =?)",
				   " AND UPPER(ST.STUDENTNAME) LIKE UPPER(?)",
				   " AND ST.CUSTOMERID = ORG.CUSTOMERID AND ORG.CUSTOMERID = ?",
				   " ORDER BY ST.ROWIDENTIFIER) STU",
				   " WHERE ROWNUM <= ?");
			
public static final String SEARCH_STUDENT_ON_REDIRECT = CustomStringUtil
.appendString( "SELECT DISTINCT (ABC.STUDENT_BIO_ID) AS STUDENT_BIO_ID,",
        " ABC.STUDENT_STRUC_ELEMENT,",
        " ABC.STUDENTNAME,",
        " ABC.ROWIDENTIFIER,",
        " ABC.ORG_NAME,",
        " ABC.STUDENTGRADE,",
        " ABC.ORG_ID",
        " FROM (SELECT STD.LAST_NAME||','||STD.FIRST_NAME  || ' ' || STD.MIDDLE_NAME",
        " AS STUDENTNAME,",
        " STD.LAST_NAME||STD.FIRST_NAME||STD.MIDDLE_NAME||'_'||to_char(STD.STUDENT_BIO_ID)AS ROWIDENTIFIER,",
        " STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,",
        " GRD.GRADE_NAME AS STUDENTGRADE,",
        " IC.STUDENT_STRUC_ELEMENT AS STUDENT_STRUC_ELEMENT,",
        " USR.ACTIVATION_STATUS AS STATUS,",
        " IC.ICID AS INVITATIONCODE,",
        " IC.ACTIVATION_STATUS AS ACTIVATIONSTATUS,",
        " HIER.ORG_NAME,",
        " HIER.ORG_ID",
        " FROM USERS USR,",
        " STUDENT_BIO_DIM STD,",
        " GRADE_DIM GRD,",
        " INVITATION_CODE IC,",
        " INVITATION_CODE_CLAIM ICC,",
        " (SELECT *",
        " FROM ORG_HIERARCHY",
        " START WITH ORG_ID = ?",
        " CONNECT BY PRIOR ORG_ID = ORG_PARENT_ID",
        " ) HIER",
        " WHERE HIER.ORG_ID = USR.ORG_ID",
        " AND USR.ORG_LEVEL = 0",
        " AND USR.USERID = ICC.USER_ID AND IC.ACTIVATION_STATUS = 'AC' ",
        " AND ICC.ICID = IC.ICID",
        " AND IC.ADMINID = STD.ADMINID",
        " AND STD.GRADEID=GRD.GRADEID) ABC",
        " WHERE ABC.STUDENT_BIO_ID =(?)", 
        " ORDER BY UPPER(ABC.STUDENTNAME)");

		public static final String UPDATE_ASSESSMENT = CustomStringUtil.appendString(
			"UPDATE INVITATION_CODE IC SET  IC.TOTAL_AVAILABLE_IC_CLAIM = ?, IC.INVITATION_EXPIRATION_DATE= TO_DATE(?,'mm/dd/yyyy')",
			" WHERE IC.INVITATION_CODE = ? AND IC.ACTIVATION_STATUS = 'AC' ",
			" AND IC.ADMINID = (SELECT STD.ADMINID  FROM STUDENT_BIO_DIM STD WHERE STD.STUDENT_BIO_ID = ?)");
		
		//by Deepak update user profile when they login for the first time
		public static final String UPDATE_FIRSTTIMEUSERLOGIN_DATA = CustomStringUtil.appendString(
			" UPDATE USERS  SET LAST_NAME = ?, ",
			" FIRST_NAME =?,  "  ,
			"  EMAIL_ADDRESS = ?  ,   PHONE_NO =?   ,   COUNTRY = ? , " ,
			"  ZIPCODE=? , STATE=? , STREET=? ,CITY=?" ,
			"  WHERE USERID = ? ");
		
		//by Deepak update user profile when they login for the first time
		public static final String UPDATE_FIRSTTIMEUSERFLAG_DATA = " UPDATE USERS  SET IS_FIRSTTIME_LOGIN = ? WHERE upper(USERNAME) =upper(?)";
		public static final String UPDATE_PASSWORD_DATA = " UPDATE USERS  SET IS_FIRSTTIME_LOGIN = ?, password = ? , salt = ? WHERE upper(USERNAME) =upper(?)";
		
		public static final String FETCH_SCHOOLID_FOR_STUDENT = CustomStringUtil.appendString(
			" select ORG.level3_jasper_orgid from student_bio_dim std, ORG_NODE_DIM org ",
			" where ORG.ORG_NODEID = std.ORG_NODEID and std.student_bio_id = ? " );
	
	
		public static final String UPDATE_ACTIVATION_CODE = CustomStringUtil.appendString(
				"UPDATE INVITATION_CODE IC SET  IC.ACTIVATION_STATUS = 'IN', IC.UPDATED_DATE_TIME = sysdate",
	            " WHERE IC.INVITATION_CODE = ? AND IC.ACTIVATION_STATUS = 'AC' ",
	            " AND IC.ADMINID = (SELECT STD.ADMINID  FROM STUDENT_BIO_DIM STD WHERE STD.STUDENT_BIO_ID = ?)");
	    
	    public static final String ADD_NEW_INVITATION_CODE =  CustomStringUtil.appendString(
				"insert into INVITATION_CODE",
					  " select NVITATION_CODE_ID_SEQ.Nextval, (select sf_gen_invite_code from DUAL), student_struc_element, total_available_ic_claim, total_attempt_ic_claim, org_nodeid, adminid, invitation_expiration_date, 'AC', created_by_id, sysdate, sysdate, 'N'", 
					  " from INVITATION_CODE",
					  " where invitation_code = ? and activation_status = 'AC' AND ADMINID = (SELECT STD.ADMINID  FROM STUDENT_BIO_DIM STD WHERE STD.STUDENT_BIO_ID = ?) ");


	    public static final String STUDENT_LIST_FOR_TREE = CustomStringUtil.appendString(
	    		" select last_name || ' ' || first_name || ' ' || middle_name NAME, grd.grade_name GRADE, stu.student_bio_id ID from student_bio_dim stu, grade_dim grd ",
	    		" where stu.gradeid = grd.gradeid and org_nodeid = (select org_lstnodeid from ORG_LSTNODE_LINK where org_nodeid = ? and adminid = ? and rownum = 1) and adminid = ? and customerid = ? and stu.gradeid = ? " );
	    
	    public static final String GET_TEST_ADMINISTRATION = CustomStringUtil.appendString("PKG_MANAGE_CONTENT.SP_GET_CUST_PROD_DETAILS(?,?,?)");
	    
	    public static final String GET_GRADE = CustomStringUtil.appendString("PKG_MANAGE_CONTENT.SP_GET_GRADE_DETAILS(?,?,?)");
	    
	    public static final String GET_SUBTEST = CustomStringUtil.appendString("PKG_MANAGE_CONTENT.SP_GET_SUBTEST_DETAILS(?,?,?)");
	    
	    public static final String GET_OBJECTIVE = CustomStringUtil.appendString("PKG_MANAGE_CONTENT.SP_GET_OBJECTIVE_DETAILS(?,?,?,?)");
	    
	    public static final String ADD_NEW_CONTENT = CustomStringUtil.appendString("PKG_MANAGE_CONTENT.SP_ADD_NEW_CONTENT(?,?,?,?,?,?,?,?,?,?,?,?)");
}

