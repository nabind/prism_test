CREATE OR REPLACE PACKAGE PKG_GROUP_DOWNLOADS_MO IS

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_GET_STUDENTS(P_IN_CUST_PROD_ID       IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                            P_IN_ORG_NODE_ID_SCHOOL IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                            P_IN_GRADEIDS           IN VARCHAR2,
                            P_IN_SUBTESTIDS         IN VARCHAR2,
                            P_IN_STUDENT_GROUPS     IN VARCHAR2,
                            P_OUT_CUR_STUDENTS      OUT GET_REFCURSOR,
                            P_OUT_EXCEP_ERR_MSG     OUT VARCHAR2);

  PROCEDURE SP_GET_CODE(P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_TP_CODE(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                           P_OUT_CUR_TP_CODE   OUT GET_REFCURSOR,
                           P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

END PKG_GROUP_DOWNLOADS_MO;
/
CREATE OR REPLACE PACKAGE BODY PKG_GROUP_DOWNLOADS_MO IS

  PROCEDURE SP_GET_STUDENTS(P_IN_CUST_PROD_ID       IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                            P_IN_ORG_NODE_ID_SCHOOL IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                            P_IN_GRADEIDS           IN VARCHAR2,
                            P_IN_SUBTESTIDS         IN VARCHAR2,
                            P_IN_STUDENT_GROUPS     IN VARCHAR2,
                            P_OUT_CUR_STUDENTS      OUT GET_REFCURSOR,
                            P_OUT_EXCEP_ERR_MSG     OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT STUDENT_NAME,
             STUDENT_BIO_ID,
             SCHOOL_NAME,
             GRADE_NAME,
             GRADEID,
             GRADE_CODE,
             EXT_STUDENT_ID,
             LAST_NAME_CAP,
             CURYEAR,
             REGEXP_REPLACE(LISTAGG(SUBTESTID, ',') WITHIN
                            GROUP(ORDER BY SUBTESTID),
                            '([^,]*)(,\1)+($|,)',
                            '\1\3') AS SUBTESTIDS
        FROM (SELECT LAST_NAME || ', ' || FIRST_NAME || ' ' || MIDDLE_NAME STUDENT_NAME,
                     MRSD.STUDENT_BIO_ID STUDENT_BIO_ID,
                     MRSD.SCHOOL_NAME SCHOOL_NAME,
                     GD.GRADE_NAME GRADE_NAME,
                     MRSD.SUBTESTID SUBTESTID,
                     GD.GRADEID GRADEID,
                     GD.GRADE_CODE GRADE_CODE,
                     MRSD.EXT_STUDENT_ID EXT_STUDENT_ID,
                     REGEXP_REPLACE(REGEXP_REPLACE(UPPER(REPLACE(LAST_NAME,
                                                                 '''',
                                                                 '')),
                                                   '[^[:alnum:]'' '']',
                                                   NULL),
                                    '[[:space:]]*',
                                    '') LAST_NAME_CAP,
                     (SELECT SUBSTR(ADMIN_YEAR, 3, 2)
                        FROM ADMIN_DIM
                       WHERE IS_CURRENT_ADMIN = 'Y') CURYEAR
                FROM MV_RPRT_STUD_DETAILS MRSD, GRADE_DIM GD
               WHERE MRSD.GRADEID = GD.GRADEID
                 AND MRSD.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND MRSD.SCHOOL_ORG_NODEID = P_IN_ORG_NODE_ID_SCHOOL
                 AND MRSD.GRADEID IN
                     (WITH T AS (SELECT P_IN_GRADEIDS AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS GRADEID
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      )
                 AND MRSD.SUBTESTID IN
                     (WITH T AS (SELECT P_IN_SUBTESTIDS AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS SUBTESTID
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      )
                 AND MRSD.HOME_SCHOOL IN
                     (WITH T AS
                      (SELECT P_IN_STUDENT_GROUPS AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS STUDENT_GROUPS
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      ))
       GROUP BY STUDENT_NAME,
                STUDENT_BIO_ID,
                SCHOOL_NAME,
                GRADE_NAME,
                GRADEID,
                GRADE_CODE,
                EXT_STUDENT_ID,
                LAST_NAME_CAP,
                CURYEAR
       ORDER BY GRADE_NAME, STUDENT_NAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_STUDENTS;

  PROCEDURE SP_GET_CODE(P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT (SELECT ORG_NODE_CODE
                FROM ORG_NODE_DIM
               WHERE ORG_NODEID = P_IN_ORG_NODE_ID_DISTRICT) DISTRICT_CODE,
             (SELECT ORG_NODE_CODE
                FROM ORG_NODE_DIM
               WHERE ORG_NODEID = P_IN_ORG_NODE_ID_SCHOOL) SCHOOL_CODE
        FROM DUAL;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_CODE;

  PROCEDURE SP_GET_TP_CODE(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                           P_OUT_CUR_TP_CODE   OUT GET_REFCURSOR,
                           P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_TP_CODE FOR
      SELECT TP_CODE, PRODUCTID
        FROM CUST_PRODUCT_LINK CPL, TEST_PROGRAM TP
       WHERE CPL.CUSTOMERID = TP.CUSTOMERID
         AND CPL.ADMINID = TP.ADMINID
         AND CPL.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND TP.ACTIVATION_STATUS = 'AC';
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_TP_CODE;

END PKG_GROUP_DOWNLOADS_MO;
/
