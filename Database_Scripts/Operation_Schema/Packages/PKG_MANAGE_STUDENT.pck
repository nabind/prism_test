CREATE OR REPLACE PACKAGE PKG_MANAGE_STUDENT AS

  -- Author  : 541841
  -- Created : 6/23/2014 2:36:17 PM
  -- Purpose : Manage Student

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_SEARCH_STUDENT_REDIRECT(P_IN_CUST_PROD_ID    IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                       P_OUT_CUR            OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

  PROCEDURE SP_GET_PARENT_FOR_STUDENT(P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                      P_IN_CUSTOMERID      IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                      P_OUT_CUR            OUT GET_REFCURSOR,
                                      P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

  PROCEDURE SP_GET_STUDENT_DETAILS(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                   P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                   P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                   P_IN_STUDENTNAME_ID IN VARCHAR,
                                   P_IN_SEARCH_PARAM   IN VARCHAR,
                                   P_IN_MORE_COUNT     IN NUMBER,
                                   P_OUT_CUR           OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_SEARCH_STUDENT(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                              P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                              P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                              P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                              P_IN_SEARCH_PARAM   IN VARCHAR,
                              P_IN_ROW_NUM        IN VARCHAR,
                              P_OUT_CUR           OUT GET_REFCURSOR,
                              P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_ASSESSMENT_FOR_STUDENT(P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                          P_OUT_CUR            OUT GET_REFCURSOR,
                                          P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

  PROCEDURE SP_UPDATE_ASSESSMENT(P_IN_TOTAL_AVAILABLE IN INVITATION_CODE.TOTAL_AVAILABLE%TYPE,
                                 P_IN_EXPIRATION_DATE IN VARCHAR2,
                                 P_IN_INVITATION_CODE IN INVITATION_CODE.INVITATION_CODE%TYPE,
                                 P_OUT_STATUS_NUMBER  OUT NUMBER,
                                 P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

  PROCEDURE SP_REGENERATE_ACTIVATION_CODE(P_IN_INVITATION_CODE IN INVITATION_CODE.INVITATION_CODE%TYPE,
                                          P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                          P_OUT_IC_LETTER_PATH OUT VARCHAR2,
                                          P_OUT_STATUS_NUMBER  OUT NUMBER,
                                          P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

END PKG_MANAGE_STUDENT;
/
CREATE OR REPLACE PACKAGE BODY PKG_MANAGE_STUDENT AS
  PROCEDURE SP_SEARCH_STUDENT_REDIRECT(P_IN_CUST_PROD_ID    IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                       P_OUT_CUR            OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR FOR
      SELECT STD.TEST_ELEMENT_ID,
             STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
             STD.MIDDLE_NAME AS STUDENTNAME,
             STD.LAST_NAME || STD.FIRST_NAME || STD.MIDDLE_NAME || '_' ||
             TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
             GRD.GRADE_NAME AS STUDENTGRADE,
             NVL(ORG.ORG_NODEID, 0) AS ORG_NODEID,
             ORG.ORG_NODE_NAME
        FROM STUDENT_BIO_DIM STD, GRADE_DIM GRD, ORG_NODE_DIM ORG
       WHERE STD.TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID
         AND STD.GRADEID = GRD.GRADEID
         AND ORG.ORG_NODEID = STD.ORG_NODEID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_SEARCH_STUDENT_REDIRECT;

  PROCEDURE SP_GET_PARENT_FOR_STUDENT(P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                      P_IN_CUSTOMERID      IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                      P_OUT_CUR            OUT GET_REFCURSOR,
                                      P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR FOR
      SELECT USR.USERNAME         USERNAME,
             IC.INVITATION_CODE,
             IC.ACTIVATION_STATUS STATUS
        FROM INVITATION_CODE_CLAIM ICC,
             INVITATION_CODE       IC,
             USERS                 USR,
             STUDENT_BIO_DIM       STD,
             ORG_USERS             ORG,
             CUST_PRODUCT_LINK     LINK
       WHERE STD.TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID
         AND STD.STUDENT_BIO_ID = IC.STUDENT_BIO_ID
         AND IC.ICID = ICC.ICID
         AND LINK.ADMINID = STD.ADMINID
         AND IC.CUST_PROD_ID = LINK.CUST_PROD_ID
         AND IC.ACTIVATION_STATUS = 'AC'
         AND ICC.ORG_USER_ID = ORG.ORG_USER_ID
         AND ORG.USERID = USR.USERID
         AND USR.CUSTOMERID = STD.CUSTOMERID
         AND USR.CUSTOMERID = P_IN_CUSTOMERID
       ORDER BY USR.USERNAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_GET_PARENT_FOR_STUDENT;

  PROCEDURE SP_GET_STUDENT_DETAILS(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                   P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                   P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                   P_IN_STUDENTNAME_ID IN VARCHAR,
                                   P_IN_SEARCH_PARAM   IN VARCHAR,
                                   P_IN_MORE_COUNT     IN NUMBER,
                                   P_OUT_CUR           OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
    CURSOR C_GET_ADMIN_ID(P_IN_CUST_PROD_ID IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE) IS
      SELECT ADMINID
        FROM CUST_PRODUCT_LINK
       WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID;
  
    P_ADMIN_ID CUST_PRODUCT_LINK.ADMINID%TYPE := NULL;
  
  BEGIN
  
    OPEN C_GET_ADMIN_ID(P_IN_CUST_PROD_ID);
    FETCH C_GET_ADMIN_ID
      INTO P_ADMIN_ID;
    CLOSE C_GET_ADMIN_ID;
  
    IF P_IN_STUDENTNAME_ID <> '-99' AND P_IN_SEARCH_PARAM <> '-99' THEN
      OPEN P_OUT_CUR FOR
        SELECT STU.ROWIDENTIFIER  AS ROWIDENTIFIER,
               STU.STUDENTNAME    AS STUDENTNAME,
               STU.STUDENT_BIO_ID AS STUDENTNAME,
               STU.TESTELEMENTID  AS TESTELEMENTID,
               STU.INTSTUDENTID   AS INTSTUDENTID,
               STU.EXTSTUDENTID   AS EXTSTUDENTID,
               STU.STUDENT_MODE   AS STUDENT_MODE,
               STU.STUDENTGRADE   AS STUDENTGRADE,
               STU.SCHOOL         AS SCHOOL,
               P_IN_ORG_NODE_ID   AS TENANTID
          FROM (SELECT ST.ROWIDENTIFIER,
                       ST.STUDENTNAME,
                       ST.STUDENT_BIO_ID,
                       ST.TESTELEMENTID,
                       ST.INTSTUDENTID,
                       ST.EXTSTUDENTID,
                       ST.STUDENT_MODE,
                       GRD.GRADE_NAME    AS STUDENTGRADE,
                       ORG.ORG_NODE_NAME AS SCHOOL
                  FROM (SELECT STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                               STD.MIDDLE_NAME AS STUDENTNAME,
                               STD.LAST_NAME || STD.FIRST_NAME ||
                               STD.MIDDLE_NAME || '_' ||
                               TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
                               STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                               STD.TEST_ELEMENT_ID AS TESTELEMENTID,
                               STD.INT_STUDENT_ID AS INTSTUDENTID,
                               STD.EXT_STUDENT_ID AS EXTSTUDENTID,
                               STD.STUDENT_MODE AS STUDENT_MODE,
                               STD.GRADEID,
                               STD.ORG_NODEID,
                               STD.CUSTOMERID
                          FROM STUDENT_BIO_DIM STD
                         WHERE STD.ADMINID = P_ADMIN_ID
                              /*(SELECT ADMINID
                               FROM CUST_PRODUCT_LINK
                              WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID)*/
                           AND STD.CUSTOMERID = P_IN_CUSTOMERID) ST,
                       GRADE_DIM GRD,
                       ORG_NODE_DIM ORG
                 WHERE ORG.ORG_MODE = P_IN_ORG_MODE
                   AND ORG.ORG_NODEID = ST.ORG_NODEID
                   AND ST.GRADEID = GRD.GRADEID
                   AND ORG.CUSTOMERID = ST.CUSTOMERID
                   AND ORG.CUSTOMERID = P_IN_CUSTOMERID
                   AND ORG.ORG_NODEID IN
                       (SELECT ORG_LSTNODEID
                          FROM ORG_LSTNODE_LINK
                         WHERE ORG_NODEID = P_IN_ORG_NODE_ID)
                   AND ST.ROWIDENTIFIER > P_IN_STUDENTNAME_ID
                   AND UPPER(ST.STUDENTNAME) LIKE UPPER(P_IN_SEARCH_PARAM)
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = ORG.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                 ORDER BY ST.ROWIDENTIFIER) STU
         WHERE ROWNUM <= P_IN_MORE_COUNT;
    ELSIF P_IN_STUDENTNAME_ID <> '-99' AND P_IN_SEARCH_PARAM = '-99' THEN
      OPEN P_OUT_CUR FOR
        SELECT STU.ROWIDENTIFIER  AS ROWIDENTIFIER,
               STU.STUDENTNAME    AS STUDENTNAME,
               STU.STUDENT_BIO_ID AS STUDENT_BIO_ID,
               STU.TESTELEMENTID  AS TESTELEMENTID,
               STU.INTSTUDENTID   AS INTSTUDENTID,
               STU.EXTSTUDENTID   AS EXTSTUDENTID,
               STU.STUDENT_MODE   AS STUDENT_MODE,
               STU.STUDENTGRADE   AS STUDENTGRADE,
               STU.SCHOOL         AS SCHOOL,
               P_IN_ORG_NODE_ID   AS TENANTID
          FROM (SELECT ST.ROWIDENTIFIER,
                       ST.STUDENTNAME,
                       ST.STUDENT_BIO_ID,
                       ST.TESTELEMENTID,
                       ST.INTSTUDENTID,
                       ST.EXTSTUDENTID,
                       ST.STUDENT_MODE,
                       GRD.GRADE_NAME    AS STUDENTGRADE,
                       ORG.ORG_NODE_NAME AS SCHOOL
                  FROM (SELECT STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                               STD.MIDDLE_NAME AS STUDENTNAME,
                               STD.LAST_NAME || STD.FIRST_NAME ||
                               STD.MIDDLE_NAME || '_' ||
                               TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
                               STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                               STD.TEST_ELEMENT_ID AS TESTELEMENTID,
                               STD.INT_STUDENT_ID AS INTSTUDENTID,
                               STD.EXT_STUDENT_ID AS EXTSTUDENTID,
                               STD.STUDENT_MODE AS STUDENT_MODE,
                               STD.GRADEID,
                               STD.ORG_NODEID,
                               STD.CUSTOMERID
                          FROM STUDENT_BIO_DIM STD
                         WHERE STD.ADMINID = P_ADMIN_ID
                              /*(SELECT ADMINID
                               FROM CUST_PRODUCT_LINK
                              WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID)*/
                           AND STD.CUSTOMERID = P_IN_CUSTOMERID) ST,
                       GRADE_DIM GRD,
                       ORG_NODE_DIM ORG
                 WHERE ORG.ORG_MODE = P_IN_ORG_MODE
                   AND ORG.ORG_NODEID = ST.ORG_NODEID
                   AND ST.GRADEID = GRD.GRADEID
                   AND ST.CUSTOMERID = ORG.CUSTOMERID
                   AND ORG.CUSTOMERID = P_IN_CUSTOMERID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_LSTNODE_LINK OLNL
                         WHERE OLNL.ORG_LSTNODEID = ORG.ORG_NODEID
                           AND OLNL.ORG_NODEID = P_IN_ORG_NODE_ID)
                   AND ST.ROWIDENTIFIER > P_IN_STUDENTNAME_ID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = ORG.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                 ORDER BY ST.ROWIDENTIFIER) STU
         WHERE ROWNUM <= P_IN_MORE_COUNT;
    ELSIF P_IN_STUDENTNAME_ID = '-99' AND P_IN_SEARCH_PARAM = '-99' THEN
      OPEN P_OUT_CUR FOR
      /*SELECT *
                FROM (SELECT STD.LAST_NAME || STD.FIRST_NAME || STD.MIDDLE_NAME || '_' ||
                             TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
                             STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                             STD.MIDDLE_NAME AS STUDENTNAME,
                             STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                             STD.TEST_ELEMENT_ID AS TESTELEMENTID,
                             STD.INT_STUDENT_ID AS INTSTUDENTID,
                             STD.EXT_STUDENT_ID AS EXTSTUDENTID,
                             STD.STUDENT_MODE AS STUDENT_MODE,
                             GRD.GRADE_NAME AS STUDENTGRADE,
                             ORG.ORG_NODE_NAME AS SCHOOL,
                             P_IN_ORG_NODE_ID AS TENANTID
                        FROM STUDENT_BIO_DIM STD, GRADE_DIM GRD, ORG_NODE_DIM ORG
                       WHERE ORG.ORG_MODE = P_IN_ORG_MODE
                         AND ORG.ORG_NODEID = STD.ORG_NODEID
                         AND STD.GRADEID = GRD.GRADEID
                         AND STD.ADMINID =
                             (SELECT ADMINID
                                FROM CUST_PRODUCT_LINK
                               WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID)
                         AND EXISTS
                       (SELECT 1
                                FROM ORG_LSTNODE_LINK OLNL
                               WHERE OLNL.ORG_LSTNODEID = ORG.ORG_NODEID
                                 AND OLNL.ORG_NODEID = P_IN_ORG_NODE_ID)
                         AND ORG.CUSTOMERID = STD.CUSTOMERID
                         AND ORG.CUSTOMERID = P_IN_CUSTOMERID
                         AND EXISTS
                       (SELECT 1
                                FROM ORG_PRODUCT_LINK OPL
                               WHERE OPL.ORG_NODEID = ORG.ORG_NODEID
                                 AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                       ORDER BY ROWIDENTIFIER)
               WHERE ROWNUM <= P_IN_MORE_COUNT;*/
      
        SELECT *
          FROM (SELECT STD.LAST_NAME || STD.FIRST_NAME || STD.MIDDLE_NAME || '_' ||
                        TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
                        STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                        STD.MIDDLE_NAME AS STUDENTNAME,
                        STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                        STD.TEST_ELEMENT_ID AS TESTELEMENTID,
                        STD.INT_STUDENT_ID AS INTSTUDENTID,
                        STD.EXT_STUDENT_ID AS EXTSTUDENTID,
                        STD.STUDENT_MODE AS STUDENT_MODE,
                        (SELECT GRADE_NAME
                           FROM GRADE_DIM
                          WHERE GRADEID = STD.GRADEID) AS STUDENTGRADE,
                        ORG.ORG_NODE_NAME AS SCHOOL,
                        P_IN_ORG_NODE_ID AS TENANTID
                   FROM (SELECT CUSTOMERID,
                                ORG_NODEID,
                                LAST_NAME,
                                FIRST_NAME,
                                MIDDLE_NAME,
                                STUDENT_BIO_ID,
                                TEST_ELEMENT_ID,
                                INT_STUDENT_ID,
                                EXT_STUDENT_ID,
                                STUDENT_MODE,
                                GRADEID
                           FROM STUDENT_BIO_DIM
                          WHERE CUSTOMERID = P_IN_CUSTOMERID
                            AND ADMINID = P_ADMIN_ID) STD, /*GRADE_DIM GRD,*/
                        ORG_NODE_DIM ORG
                  WHERE ORG.ORG_MODE = P_IN_ORG_MODE
                    AND ORG.ORG_NODEID = STD.ORG_NODEID
                    AND EXISTS
                  (SELECT 1
                           FROM ORG_LSTNODE_LINK OLNL
                          WHERE OLNL.ORG_LSTNODEID = ORG.ORG_NODEID
                            AND OLNL.ORG_NODEID = P_IN_ORG_NODE_ID
                         /*AND OLNL.ADMINID = P_ADMIN_ID*/
                         ) /*Blocked as we don't have all records in ORG_LSTNODE_LINK for all admin id*/
                   AND ORG.CUSTOMERID = STD.CUSTOMERID
                   AND ORG.CUSTOMERID = P_IN_CUSTOMERID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = ORG.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                 ORDER BY ROWIDENTIFIER)
         WHERE ROWNUM <= P_IN_MORE_COUNT;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_GET_STUDENT_DETAILS;

  PROCEDURE SP_SEARCH_STUDENT(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                              P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                              P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                              P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                              P_IN_SEARCH_PARAM   IN VARCHAR,
                              P_IN_ROW_NUM        IN VARCHAR,
                              P_OUT_CUR           OUT GET_REFCURSOR,
                              P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
    CURSOR c_get_admin_id(P_IN_CUST_PROD_ID IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE) IS
      SELECT ADMINID
        FROM CUST_PRODUCT_LINK
       WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID;
  
    P_ADMIN_ID CUST_PRODUCT_LINK.ADMINID%TYPE := NULL;
  
  BEGIN
    OPEN c_get_admin_id(P_IN_CUST_PROD_ID);
    FETCH c_get_admin_id
      INTO P_ADMIN_ID;
    CLOSE c_get_admin_id;
  
    OPEN P_OUT_CUR FOR
    /*SELECT STU.ROWIDENTIFIER  AS ROWIDENTIFIER,
                   STU.STUDENTNAME    AS STUDENTNAME,
                   STU.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                   STU.TESTELEMENTID  AS TESTELEMENTID,
                   STU.GRADE_NAME     AS STUDENTGRADE,
                   STU.SCHOOL         AS SCHOOL,
                   P_IN_ORG_NODE_ID   AS TENANTID
              FROM (SELECT ST.ROWIDENTIFIER,
                           ST.STUDENTNAME,
                           ST.STUDENT_BIO_ID,
                           ST.TESTELEMENTID,
                           GRD.GRADE_NAME,
                           ORG.ORG_NODE_NAME AS SCHOOL
                      FROM (SELECT STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                                   STD.MIDDLE_NAME AS STUDENTNAME,
                                   STD.LAST_NAME || STD.FIRST_NAME ||
                                   STD.MIDDLE_NAME || '_' ||
                                   TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
                                   STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                                   STD.TEST_ELEMENT_ID AS TESTELEMENTID,
                                   STD.GRADEID,
                                   STD.ORG_NODEID,
                                   STD.CUSTOMERID
                              FROM STUDENT_BIO_DIM STD
                             WHERE STD.ADMINID =
                                   (SELECT ADMINID
                                      FROM CUST_PRODUCT_LINK
                                     WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID)) ST,
                           GRADE_DIM GRD,
                           ORG_NODE_DIM ORG
                     WHERE ORG.ORG_MODE = P_IN_ORG_MODE
                       AND ORG.ORG_NODEID = ST.ORG_NODEID
                       AND ST.GRADEID = GRD.GRADEID
                       AND EXISTS
                     (SELECT 1
                              FROM ORG_LSTNODE_LINK OLNL
                             WHERE OLNL.ORG_LSTNODEID = ORG.ORG_NODEID
                               AND OLNL.ORG_NODEID = P_IN_ORG_NODE_ID)
                       AND UPPER(ST.STUDENTNAME) LIKE UPPER(P_IN_SEARCH_PARAM)
                       AND ST.CUSTOMERID = ORG.CUSTOMERID
                       AND ORG.CUSTOMERID = P_IN_CUSTOMERID
                       AND EXISTS
                     (SELECT 1
                              FROM ORG_PRODUCT_LINK OPL
                             WHERE OPL.ORG_NODEID = ORG.ORG_NODEID
                               AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                     ORDER BY ST.ROWIDENTIFIER) STU
             WHERE ROWNUM <= P_IN_ROW_NUM;*/
    
      SELECT STU.ROWIDENTIFIER  AS ROWIDENTIFIER,
             STU.STUDENTNAME    AS STUDENTNAME,
             STU.STUDENT_BIO_ID AS STUDENT_BIO_ID,
             STU.TESTELEMENTID  AS TESTELEMENTID,
             STU.GRADE_NAME     AS STUDENTGRADE,
             STU.SCHOOL         AS SCHOOL,
             P_IN_ORG_NODE_ID   AS TENANTID
        FROM (SELECT ST.ROWIDENTIFIER,
                      ST.STUDENTNAME,
                      ST.STUDENT_BIO_ID,
                      ST.TESTELEMENTID,
                      (SELECT GRADE_NAME
                         FROM GRADE_DIM
                        WHERE GRADEID = ST.GRADEID) GRADE_NAME,
                      ORG.ORG_NODE_NAME AS SCHOOL
                 FROM (SELECT STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                              STD.MIDDLE_NAME AS STUDENTNAME,
                              STD.LAST_NAME || STD.FIRST_NAME ||
                              STD.MIDDLE_NAME || '_' ||
                              TO_CHAR(STD.STUDENT_BIO_ID) AS ROWIDENTIFIER,
                              STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
                              STD.TEST_ELEMENT_ID AS TESTELEMENTID,
                              STD.GRADEID,
                              STD.ORG_NODEID,
                              STD.CUSTOMERID
                         FROM STUDENT_BIO_DIM STD
                        WHERE STD.ADMINID = P_ADMIN_ID
                       /* (SELECT ADMINID
                        FROM CUST_PRODUCT_LINK
                       WHERE CUST_PROD_ID = 5030)*/
                       ) ST,
                      --GRADE_DIM GRD,
                      ORG_NODE_DIM     ORG,
                      ORG_LSTNODE_LINK OLNL
                WHERE ORG.ORG_MODE = P_IN_ORG_MODE
                  AND ORG.ORG_NODEID = ST.ORG_NODEID
                  AND OLNL.ORG_NODEID = P_IN_ORG_NODE_ID
                  AND OLNL.ORG_LSTNODEID = ORG.ORG_NODEID
                  AND UPPER(ST.STUDENTNAME) LIKE UPPER(P_IN_SEARCH_PARAM)
                  AND ST.CUSTOMERID = ORG.CUSTOMERID
                  AND ORG.CUSTOMERID = P_IN_CUSTOMERID
                     --AND OLNL.ADMINID = P_ADMIN_ID  /*Blocked as we don't have all records in ORG_LSTNODE_LINK for all admin id*/
                 AND EXISTS
               (SELECT 1
                        FROM ORG_PRODUCT_LINK OPL
                       WHERE OPL.ORG_NODEID = ORG.ORG_NODEID
                         AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
               ORDER BY ST.ROWIDENTIFIER) STU
       WHERE ROWNUM <= P_IN_ROW_NUM;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_SEARCH_STUDENT;

  PROCEDURE SP_GET_ASSESSMENT_FOR_STUDENT(P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                          P_OUT_CUR            OUT GET_REFCURSOR,
                                          P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR FOR
      SELECT STD.STUDENT_BIO_ID AS STUDENT_BIO_ID,
             STD.TEST_ELEMENT_ID AS TESTELEMENTID,
             IC.INVITATION_CODE AS INVITATION_CODE,
             TO_CHAR(IC.EXPIRATION_DATE, 'MM/DD/YYYY') AS EXPIRATION_DATE,
             IC.TOTAL_AVAILABLE AS TOTAL_AVAILABLE,
             DECODE(SIGN(IC.EXPIRATION_DATE - SYSDATE),
                    -1,
                    'EXPIRED',
                    'ACTIVE') AS EXPIRATION_STATUS,
             PROD.PRODUCT_NAME AS ASSESSMENT_YEAR
        FROM INVITATION_CODE   IC,
             STUDENT_BIO_DIM   STD,
             PRODUCT           PROD,
             CUST_PRODUCT_LINK LINK
       WHERE LINK.ADMINID = STD.ADMINID
         AND IC.CUST_PROD_ID = LINK.CUST_PROD_ID
         AND IC.ACTIVATION_STATUS = 'AC'
         AND LINK.ADMINID = STD.ADMINID
         AND PROD.PRODUCTID = LINK.PRODUCTID
         AND IC.STUDENT_BIO_ID = STD.STUDENT_BIO_ID
         AND IC.TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_GET_ASSESSMENT_FOR_STUDENT;

  PROCEDURE SP_UPDATE_ASSESSMENT(P_IN_TOTAL_AVAILABLE IN INVITATION_CODE.TOTAL_AVAILABLE%TYPE,
                                 P_IN_EXPIRATION_DATE IN VARCHAR2,
                                 P_IN_INVITATION_CODE IN INVITATION_CODE.INVITATION_CODE%TYPE,
                                 P_OUT_STATUS_NUMBER  OUT NUMBER,
                                 P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    UPDATE INVITATION_CODE IC
       SET IC.TOTAL_AVAILABLE = P_IN_TOTAL_AVAILABLE,
           IC.EXPIRATION_DATE = TO_DATE(P_IN_EXPIRATION_DATE, 'MM/DD/YYYY')
     WHERE IC.INVITATION_CODE = P_IN_INVITATION_CODE
       AND IC.ACTIVATION_STATUS = 'AC';
  
    P_OUT_STATUS_NUMBER := 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
      ROLLBACK;
  END SP_UPDATE_ASSESSMENT;

  PROCEDURE SP_REGENERATE_ACTIVATION_CODE(P_IN_INVITATION_CODE IN INVITATION_CODE.INVITATION_CODE%TYPE,
                                          P_IN_TEST_ELEMENT_ID IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                          P_OUT_IC_LETTER_PATH OUT VARCHAR2,
                                          P_OUT_STATUS_NUMBER  OUT NUMBER,
                                          P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  
    V_INVITATION_CODE VARCHAR2(50) := '';
    V_ORG_NODEID      ORG_NODE_DIM.ORG_NODEID%TYPE;
    V_DISTRICT_CODE   ORG_NODE_DIM.ORG_NODE_CODE%TYPE;
    V_SCHOOL_CODE     ORG_NODE_DIM.ORG_NODE_CODE%TYPE;
    V_FILENAME        INVITATION_CODE.FILENAME%TYPE;
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    SELECT SF_GEN_INVITE_CODE INTO V_INVITATION_CODE FROM DUAL;
  
    SELECT OND.ORG_NODE_CODE, OND.PARENT_ORG_NODEID
      INTO V_SCHOOL_CODE, V_ORG_NODEID
      FROM ORG_NODE_DIM OND, INVITATION_CODE IC
     WHERE OND.ORG_NODEID = IC.ORG_NODEID
       AND IC.INVITATION_CODE = P_IN_INVITATION_CODE
       AND IC.ACTIVATION_STATUS = 'AC'
       AND TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID;
  
    SELECT ORG_NODE_CODE
      INTO V_DISTRICT_CODE
      FROM ORG_NODE_DIM
     WHERE ORG_NODEID = V_ORG_NODEID;
  
    V_FILENAME := '/IC/IC_district_' || V_DISTRICT_CODE || '_school_' ||
                  V_SCHOOL_CODE || '_' || P_IN_TEST_ELEMENT_ID || '.pdf';
  
    INSERT INTO INVITATION_CODE
      SELECT SEQ_IC_ID.NEXTVAL,
             V_INVITATION_CODE,
             TEST_ELEMENT_ID,
             TOTAL_AVAILABLE,
             TOTAL_ATTEMPT,
             EXPIRATION_DATE,
             INT_STUDENT_ID,
             CUST_PROD_ID,
             STUDENT_BIO_ID,
             GRADE_ID,
             STUDENT_FULL_NAME,
             'N',
             V_FILENAME,
             'AC',
             CREATED_SOURCE,
             CREATED_BY_ID,
             BIRTHDATE,
             ORG_NODEID,
             SYSDATE,
             NULL
        FROM INVITATION_CODE
       WHERE INVITATION_CODE = P_IN_INVITATION_CODE
         AND ACTIVATION_STATUS = 'AC'
         AND TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID;
  
    UPDATE INVITATION_CODE IC
       SET IC.ACTIVATION_STATUS = 'IN', IC.UPDATED_DATE_TIME = SYSDATE
     WHERE IC.INVITATION_CODE = P_IN_INVITATION_CODE
       AND IC.ACTIVATION_STATUS = 'AC'
       AND IC.TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID;
  
    SELECT DISTINCT CUST.FILE_LOCATION || PROD.FILE_LOCATION || V_FILENAME
      INTO P_OUT_IC_LETTER_PATH
      FROM CUSTOMER_INFO     CUST,
           PRODUCT           PROD,
           CUST_PRODUCT_LINK LIN,
           ORG_PRODUCT_LINK  OPL,
           INVITATION_CODE   IC
     WHERE LIN.CUSTOMERID = CUST.CUSTOMERID
       AND LIN.PRODUCTID = PROD.PRODUCTID
       AND LIN.CUST_PROD_ID = OPL.CUST_PROD_ID /*(+) COMMENTED OUTER JOIN*/
       AND IC.ORG_NODEID = OPL.ORG_NODEID
       AND PROD.PRODUCT_NAME LIKE '%ISTEP%'
       AND LIN.CUST_PROD_ID = IC.CUST_PROD_ID
       AND IC.TEST_ELEMENT_ID = P_IN_TEST_ELEMENT_ID
       AND IC.ACTIVATION_STATUS = 'AC'
       AND IC.INVITATION_CODE = V_INVITATION_CODE;
  
    P_OUT_STATUS_NUMBER := 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
      ROLLBACK;
  END SP_REGENERATE_ACTIVATION_CODE;

END PKG_MANAGE_STUDENT;
/
