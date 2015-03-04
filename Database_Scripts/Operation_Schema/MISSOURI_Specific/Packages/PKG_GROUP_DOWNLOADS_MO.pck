CREATE OR REPLACE PACKAGE PKG_GROUP_DOWNLOADS_MO IS

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_GET_STUDENTS(P_TEST_ADMINISTRATION   IN PRODUCT.PRODUCTID%TYPE,
                            P_IN_ORG_NODE_ID_SCHOOL IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                            P_IN_GRADEID            IN NUMBER,
                            P_IN_SUBTESTID          IN VARCHAR2,
                            P_IN_STUDENT_GROUPS     IN NUMBER,
                            P_OUT_CUR_STUDENTS      OUT GET_REFCURSOR,
                            P_OUT_EXCEP_ERR_MSG     OUT VARCHAR2);

END PKG_GROUP_DOWNLOADS_MO;
/
CREATE OR REPLACE PACKAGE BODY PKG_GROUP_DOWNLOADS_MO IS

  PROCEDURE SP_GET_STUDENTS(P_TEST_ADMINISTRATION   IN PRODUCT.PRODUCTID%TYPE,
                            P_IN_ORG_NODE_ID_SCHOOL IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                            P_IN_GRADEID            IN NUMBER,
                            P_IN_SUBTESTID          IN VARCHAR2,
                            P_IN_STUDENT_GROUPS     IN NUMBER,
                            P_OUT_CUR_STUDENTS      OUT GET_REFCURSOR,
                            P_OUT_EXCEP_ERR_MSG     OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT DISTINCT LAST_NAME || ', ' || FIRST_NAME || ' ' || MIDDLE_NAME STUDENT_NAME,
                      MRSD.STUDENT_BIO_ID STUDENT_BIO_ID,
                      MRSD.SCHOOL_NAME SCHOOL_NAME,
                      GD.GRADE_NAME GRADE_NAME
        FROM MV_RPRT_STUD_DETAILS MRSD, GRADE_DIM GD
       WHERE MRSD.GRADEID = GD.GRADEID
         AND MRSD.CUST_PROD_ID = P_TEST_ADMINISTRATION
         AND MRSD.SCHOOL_ORG_NODEID = P_IN_ORG_NODE_ID_SCHOOL
         AND MRSD.GRADEID = P_IN_GRADEID
         AND MRSD.SUBTESTID IN
             (WITH T AS (SELECT P_IN_SUBTESTID AS TXT FROM DUAL)
               SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS SUBTESTID
                 FROM T
               CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
              )
         AND (MRSD.HOME_SCHOOL = P_IN_STUDENT_GROUPS)
       ORDER BY STUDENT_NAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_STUDENTS;

END PKG_GROUP_DOWNLOADS_MO;
/