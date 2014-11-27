CREATE OR REPLACE PACKAGE PKG_GROUP_DOWNLOADS IS

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_GET_STUDENTS_ALL_C_ALL_G(P_IN_USERID               IN USERS.USERID%TYPE,
                                        P_TEST_ADMINISTRATION     IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM         NUMBER,
                                        P_IN_CUSTOMERID           CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_GROUP_FILE           IN VARCHAR2,
                                        P_IN_COLLATION_HIERARCHY  IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_STUDENTS_ALL_C_ONE_G(P_IN_USERID               IN USERS.USERID%TYPE,
                                        P_TEST_ADMINISTRATION     IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM         NUMBER,
                                        P_IN_CUSTOMERID           CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_GRADE_ID             IN GRADE_DIM.GRADEID%TYPE,
                                        P_IN_COLLATION_HIERARCHY  IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_STUDENTS_ONE_C_ALL_G(P_TEST_ADMINISTRATION     IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_CUSTOMERID           CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_USERID               IN USERS.USERID%TYPE,
                                        P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_CLASS    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM         NUMBER,
                                        P_IN_GROUP_FILE           IN VARCHAR2,
                                        P_IN_COLLATION_HIERARCHY  IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_STUDENTS_ONE_C_ONE_G(P_TEST_ADMINISTRATION    IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_CUSTOMERID          CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_ORG_NODE_ID_CLASS   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM        NUMBER,
                                        P_IN_GRADE_ID            IN GRADE_DIM.GRADEID%TYPE,
                                        P_IN_COLLATION_HIERARCHY IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS       OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

END PKG_GROUP_DOWNLOADS;
/
CREATE OR REPLACE PACKAGE BODY PKG_GROUP_DOWNLOADS IS

  -- Get Students for all classes for all grades
  PROCEDURE SP_GET_STUDENTS_ALL_C_ALL_G(P_IN_USERID               IN USERS.USERID%TYPE,
                                        P_TEST_ADMINISTRATION     IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM         NUMBER,
                                        P_IN_CUSTOMERID           CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_GROUP_FILE           IN VARCHAR2,
                                        P_IN_COLLATION_HIERARCHY  IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
                                        
    ORG_NODE_ID_LOGGEDIN ORG_NODE_DIM.ORG_NODEID%TYPE;
    CUSTPRODID           CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  
  BEGIN
    DBMS_OUTPUT.PUT_LINE(P_IN_COLLATION_HIERARCHY);
    SELECT ORG_NODEID
      INTO ORG_NODE_ID_LOGGEDIN
      FROM ORG_USERS
     WHERE USERID = P_IN_USERID;
    DBMS_OUTPUT.PUT_LINE(ORG_NODE_ID_LOGGEDIN);
    SELECT CUST_PROD_ID
      INTO CUSTPRODID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = P_TEST_ADMINISTRATION
       AND CUSTOMERID = P_IN_CUSTOMERID;
    DBMS_OUTPUT.PUT_LINE(CUSTPRODID);
  
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT SBD.STUDENT_BIO_ID ID,
             (SELECT ORG_NODE_NAME
                FROM ORG_NODE_DIM A
               WHERE A.ORG_NODEID = SBD.ORG_NODEID) CLASS,
             SBD.STUDENT_LAST_NAME,
             SBD.STUDENT_FIRST_NAME,
             SBD.STUDENT_MIDDLE_INITIAL MIDDLE_NAME,
             REPLACE(SBD.STUDENT_LAST_NAME || ', ' ||
                     SBD.STUDENT_FIRST_NAME || ' ' ||
                     SBD.STUDENT_MIDDLE_INITIAL,
                     ' ',
                     ' ') AS NAME,
             (SELECT GRADE_NAME
                FROM GRADE_DIM A
               WHERE A.GRADEID = SBD.GRADEID) GRADE,
             IC.ACTIVATION_STATUS IC_FLAG,
             DECODE(IC.FILENAME, NULL, ' ', IC.FILENAME) IC,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE REPORT_NAME = 'ISR'
                 AND CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS ISR_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE REPORT_NAME = 'ISR'
                       AND CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE REPORT_NAME = 'ISR'
                        AND CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) ISR,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE P.REPORT_NAME = 'IP'
                 AND P.CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS IP_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE P.REPORT_NAME = 'IP'
                       AND P.CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE P.REPORT_NAME = 'IP'
                        AND P.CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) IP
        FROM RESULTS_GRT_FACT SBD, INVITATION_CODE IC
      -- Main WHERE, Previous lines of the query are same for all 4 queries
       WHERE SBD.ORG_NODEID IN
             (SELECT VC1 AS ORG_NODEID
                FROM TABLE (SELECT SF_GET_CLASS(ORG_NODE_ID_LOGGEDIN,
                                                P_TEST_ADMINISTRATION,
                                                P_IN_ORG_NODE_ID_DISTRICT,
                                                P_IN_ORG_NODE_ID_SCHOOL,
                                                '-1',
                                                P_IN_TEST_PROGRAM,
                                                P_IN_CUSTOMERID,
                                                'ADD_ALL')
                              FROM DUAL))
         AND SBD.GRADEID IN (SELECT VC1 AS GRADEID
                               FROM TABLE (SELECT SF_GET_GRADE_DWNLD(ORG_NODE_ID_LOGGEDIN,
                                                                     P_TEST_ADMINISTRATION,
                                                                     P_IN_ORG_NODE_ID_DISTRICT,
                                                                     P_IN_ORG_NODE_ID_SCHOOL,
                                                                     P_IN_GROUP_FILE,
                                                                     P_IN_TEST_PROGRAM,
                                                                     P_IN_CUSTOMERID,
                                                                     'SCHOOL_OTH')
                                             FROM DUAL))
         AND SBD.CUST_PROD_ID = CUSTPRODID
         AND ISPUBLIC = P_IN_TEST_PROGRAM
         AND SBD.STUDENT_BIO_ID = IC.STUDENT_BIO_ID(+)
       ORDER BY CASE WHEN P_IN_COLLATION_HIERARCHY = '11' THEN CLASS
                     WHEN P_IN_COLLATION_HIERARCHY = '12' THEN NAME
                END,
                SBD.STUDENT_LAST_NAME,
                SBD.STUDENT_FIRST_NAME,
                SBD.STUDENT_MIDDLE_INITIAL;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_STUDENTS_ALL_C_ALL_G;

  -- Get Students for all classes for a particular grade
  PROCEDURE SP_GET_STUDENTS_ALL_C_ONE_G(P_IN_USERID               IN USERS.USERID%TYPE,
                                        P_TEST_ADMINISTRATION     IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM         NUMBER,
                                        P_IN_CUSTOMERID           CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_GRADE_ID             IN GRADE_DIM.GRADEID%TYPE,
                                        P_IN_COLLATION_HIERARCHY  IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
    ORG_NODE_ID_LOGGEDIN ORG_NODE_DIM.ORG_NODEID%TYPE;
    CUSTPRODID           CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  
  BEGIN
    DBMS_OUTPUT.PUT_LINE(P_IN_COLLATION_HIERARCHY);
    SELECT ORG_NODEID
      INTO ORG_NODE_ID_LOGGEDIN
      FROM ORG_USERS
     WHERE USERID = P_IN_USERID;
    DBMS_OUTPUT.PUT_LINE(ORG_NODE_ID_LOGGEDIN);
    SELECT CUST_PROD_ID
      INTO CUSTPRODID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = P_TEST_ADMINISTRATION
       AND CUSTOMERID = P_IN_CUSTOMERID;
    DBMS_OUTPUT.PUT_LINE(CUSTPRODID);
  
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT SBD.STUDENT_BIO_ID ID,
             (SELECT ORG_NODE_NAME
                FROM ORG_NODE_DIM A
               WHERE A.ORG_NODEID = SBD.ORG_NODEID) CLASS,
             SBD.STUDENT_LAST_NAME,
             SBD.STUDENT_FIRST_NAME,
             SBD.STUDENT_MIDDLE_INITIAL MIDDLE_NAME,
             REPLACE(SBD.STUDENT_LAST_NAME || ', ' ||
                     SBD.STUDENT_FIRST_NAME || ' ' ||
                     SBD.STUDENT_MIDDLE_INITIAL,
                     ' ',
                     ' ') AS NAME,
             (SELECT GRADE_NAME
                FROM GRADE_DIM A
               WHERE A.GRADEID = SBD.GRADEID) GRADE,
             IC.ACTIVATION_STATUS IC_FLAG,
             DECODE(IC.FILENAME, NULL, ' ', IC.FILENAME) IC,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE REPORT_NAME = 'ISR'
                 AND CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS ISR_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE REPORT_NAME = 'ISR'
                       AND CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE REPORT_NAME = 'ISR'
                        AND CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) ISR,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE P.REPORT_NAME = 'IP'
                 AND P.CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS IP_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE P.REPORT_NAME = 'IP'
                       AND P.CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE P.REPORT_NAME = 'IP'
                        AND P.CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) IP
        FROM RESULTS_GRT_FACT SBD, INVITATION_CODE IC
      -- Main WHERE, Previous lines of the query are same for all 4 queries
       WHERE SBD.ORG_NODEID IN
             (SELECT VC1 AS ORG_NODEID
                FROM TABLE (SELECT SF_GET_CLASS(ORG_NODE_ID_LOGGEDIN,
                                                P_TEST_ADMINISTRATION,
                                                P_IN_ORG_NODE_ID_DISTRICT,
                                                P_IN_ORG_NODE_ID_SCHOOL,
                                                P_IN_GRADE_ID,
                                                P_IN_TEST_PROGRAM,
                                                P_IN_CUSTOMERID,
                                                'ADD_ALL')
                              FROM DUAL))
         AND SBD.GRADEID = P_IN_GRADE_ID
         AND SBD.CUST_PROD_ID = CUSTPRODID
         AND ISPUBLIC = P_IN_TEST_PROGRAM
         AND SBD.STUDENT_BIO_ID = IC.STUDENT_BIO_ID(+)
       ORDER BY CASE WHEN P_IN_COLLATION_HIERARCHY = '11' THEN CLASS
                     WHEN P_IN_COLLATION_HIERARCHY = '12' THEN NAME
                END,
                SBD.STUDENT_LAST_NAME,
                SBD.STUDENT_FIRST_NAME,
                SBD.STUDENT_MIDDLE_INITIAL;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_STUDENTS_ALL_C_ONE_G;

  -- Get Students for a particular class for all grades
  PROCEDURE SP_GET_STUDENTS_ONE_C_ALL_G(P_TEST_ADMINISTRATION     IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_CUSTOMERID           CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_USERID               IN USERS.USERID%TYPE,
                                        P_IN_ORG_NODE_ID_DISTRICT IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_SCHOOL   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_ORG_NODE_ID_CLASS    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM         NUMBER,
                                        P_IN_GROUP_FILE           IN VARCHAR2,
                                        P_IN_COLLATION_HIERARCHY  IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS        OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
    ORG_NODE_ID_LOGGEDIN ORG_NODE_DIM.ORG_NODEID%TYPE;
    CUSTPRODID           CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  
  BEGIN
    DBMS_OUTPUT.PUT_LINE(P_IN_COLLATION_HIERARCHY);
    SELECT ORG_NODEID
      INTO ORG_NODE_ID_LOGGEDIN
      FROM ORG_USERS
     WHERE USERID = P_IN_USERID;
    DBMS_OUTPUT.PUT_LINE(ORG_NODE_ID_LOGGEDIN);
    SELECT CUST_PROD_ID
      INTO CUSTPRODID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = P_TEST_ADMINISTRATION
       AND CUSTOMERID = P_IN_CUSTOMERID;
    DBMS_OUTPUT.PUT_LINE(CUSTPRODID);
  
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT SBD.STUDENT_BIO_ID ID,
             (SELECT ORG_NODE_NAME
                FROM ORG_NODE_DIM A
               WHERE A.ORG_NODEID = SBD.ORG_NODEID) CLASS,
             SBD.STUDENT_LAST_NAME,
             SBD.STUDENT_FIRST_NAME,
             SBD.STUDENT_MIDDLE_INITIAL MIDDLE_NAME,
             REPLACE(SBD.STUDENT_LAST_NAME || ', ' ||
                     SBD.STUDENT_FIRST_NAME || ' ' ||
                     SBD.STUDENT_MIDDLE_INITIAL,
                     ' ',
                     ' ') AS NAME,
             (SELECT GRADE_NAME
                FROM GRADE_DIM A
               WHERE A.GRADEID = SBD.GRADEID) GRADE,
             IC.ACTIVATION_STATUS IC_FLAG,
             DECODE(IC.FILENAME, NULL, ' ', IC.FILENAME) IC,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE REPORT_NAME = 'ISR'
                 AND CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS ISR_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE REPORT_NAME = 'ISR'
                       AND CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE REPORT_NAME = 'ISR'
                        AND CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) ISR,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE P.REPORT_NAME = 'IP'
                 AND P.CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS IP_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE P.REPORT_NAME = 'IP'
                       AND P.CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE P.REPORT_NAME = 'IP'
                        AND P.CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) IP
        FROM RESULTS_GRT_FACT SBD, INVITATION_CODE IC
      -- Main WHERE, Previous lines of the query are same for all 4 queries
       WHERE SBD.ORG_NODEID = P_IN_ORG_NODE_ID_CLASS
         AND SBD.GRADEID IN (SELECT VC1 AS GRADEID
                               FROM TABLE (SELECT SF_GET_GRADE_DWNLD(ORG_NODE_ID_LOGGEDIN,
                                                                     P_TEST_ADMINISTRATION,
                                                                     P_IN_ORG_NODE_ID_DISTRICT,
                                                                     P_IN_ORG_NODE_ID_SCHOOL,
                                                                     P_IN_GROUP_FILE,
                                                                     P_IN_TEST_PROGRAM,
                                                                     P_IN_CUSTOMERID,
                                                                     'SCHOOL_OTH')
                                             FROM DUAL))
         AND SBD.CUST_PROD_ID = CUSTPRODID
         AND ISPUBLIC = P_IN_TEST_PROGRAM
         AND SBD.STUDENT_BIO_ID = IC.STUDENT_BIO_ID(+)
       ORDER BY CASE WHEN P_IN_COLLATION_HIERARCHY = '11' THEN CLASS
                     WHEN P_IN_COLLATION_HIERARCHY = '12' THEN NAME
                END,
                SBD.STUDENT_LAST_NAME,
                SBD.STUDENT_FIRST_NAME,
                SBD.STUDENT_MIDDLE_INITIAL;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_STUDENTS_ONE_C_ALL_G;

  -- Get Students for a particular class for a particular grade
  PROCEDURE SP_GET_STUDENTS_ONE_C_ONE_G(P_TEST_ADMINISTRATION    IN PRODUCT.PRODUCTID%TYPE,
                                        P_IN_CUSTOMERID          CUSTOMER_INFO.CUSTOMERID%TYPE,
                                        P_IN_ORG_NODE_ID_CLASS   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                        P_IN_TEST_PROGRAM        NUMBER,
                                        P_IN_GRADE_ID            IN GRADE_DIM.GRADEID%TYPE,
                                        P_IN_COLLATION_HIERARCHY IN VARCHAR2,
                                        P_OUT_CUR_STUDENTS       OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    CUSTPRODID CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  
  BEGIN
    DBMS_OUTPUT.PUT_LINE(P_IN_COLLATION_HIERARCHY);
    SELECT CUST_PROD_ID
      INTO CUSTPRODID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = P_TEST_ADMINISTRATION
       AND CUSTOMERID = P_IN_CUSTOMERID;
    DBMS_OUTPUT.PUT_LINE(CUSTPRODID);
  
    OPEN P_OUT_CUR_STUDENTS FOR
      SELECT SBD.STUDENT_BIO_ID ID,
             (SELECT ORG_NODE_NAME
                FROM ORG_NODE_DIM A
               WHERE A.ORG_NODEID = SBD.ORG_NODEID) CLASS,
             SBD.STUDENT_LAST_NAME,
             SBD.STUDENT_FIRST_NAME,
             SBD.STUDENT_MIDDLE_INITIAL MIDDLE_NAME,
             REPLACE(SBD.STUDENT_LAST_NAME || ', ' ||
                     SBD.STUDENT_FIRST_NAME || ' ' ||
                     SBD.STUDENT_MIDDLE_INITIAL,
                     ' ',
                     ' ') AS NAME,
             (SELECT GRADE_NAME
                FROM GRADE_DIM A
               WHERE A.GRADEID = SBD.GRADEID) GRADE,
             IC.ACTIVATION_STATUS IC_FLAG,
             DECODE(IC.FILENAME, NULL, ' ', IC.FILENAME) IC,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE REPORT_NAME = 'ISR'
                 AND CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS ISR_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE REPORT_NAME = 'ISR'
                       AND CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE REPORT_NAME = 'ISR'
                        AND CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) ISR,
             (SELECT S.IS_FILE_EXISTS
                FROM STUDENT_PDF_FILES S, PDF_REPORTS P
               WHERE P.REPORT_NAME = 'IP'
                 AND P.CUST_PROD_ID = CUSTPRODID
                 AND P.PDF_REPORTID = S.PDF_REPORTID
                 AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID) AS IP_FLAG,
             DECODE((SELECT S.FILENAME
                      FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                     WHERE P.REPORT_NAME = 'IP'
                       AND P.CUST_PROD_ID = CUSTPRODID
                       AND P.PDF_REPORTID = S.PDF_REPORTID
                       AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID),
                    NULL,
                    ' ',
                    (SELECT S.FILENAME
                       FROM STUDENT_PDF_FILES S, PDF_REPORTS P
                      WHERE P.REPORT_NAME = 'IP'
                        AND P.CUST_PROD_ID = CUSTPRODID
                        AND P.PDF_REPORTID = S.PDF_REPORTID
                        AND S.STUDENT_BIO_ID = SBD.STUDENT_BIO_ID)) IP
        FROM RESULTS_GRT_FACT SBD, INVITATION_CODE IC
      -- Main WHERE, Previous lines of the query are same for all 4 queries
       WHERE SBD.ORG_NODEID = P_IN_ORG_NODE_ID_CLASS
         AND SBD.GRADEID = P_IN_GRADE_ID
         AND SBD.CUST_PROD_ID = CUSTPRODID
         AND ISPUBLIC = P_IN_TEST_PROGRAM
         AND SBD.STUDENT_BIO_ID = IC.STUDENT_BIO_ID(+)
      
       ORDER BY CASE WHEN P_IN_COLLATION_HIERARCHY = '11' THEN CLASS
                     WHEN P_IN_COLLATION_HIERARCHY = '12' THEN NAME
                END,
                SBD.STUDENT_LAST_NAME,
                SBD.STUDENT_FIRST_NAME,
                SBD.STUDENT_MIDDLE_INITIAL;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_STUDENTS_ONE_C_ONE_G;

END PKG_GROUP_DOWNLOADS;
/
