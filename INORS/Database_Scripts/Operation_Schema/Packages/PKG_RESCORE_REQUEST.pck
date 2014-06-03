CREATE OR REPLACE PACKAGE PKG_RESCORE_REQUEST AS

  TYPE GET_REFCURSOR IS REF CURSOR;
  PROCEDURE SP_GET_DNP_STUDENT_DETAILS(P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                       P_IN_PRODUCTID      IN PRODUCT.PRODUCTID%TYPE,
                                       P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_GRADEID        IN GRADE_DIM.GRADEID%TYPE,
                                       P_OUT_CUR_DNP       OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_SUBMIT_RESCORE_REQUEST(P_IN_STUDENT_BIO_ID   IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                                      P_IN_SUBTESTID        IN SUBTEST_DIM.SUBTESTID %TYPE,
                                      P_IN_SESSION_ID       ITEMSET_DIM.SESSION_ID %TYPE,
                                      P_IN_MODEULEID        ITEMSET_DIM. MODEULEID%TYPE,
                                      P_IN_ITEM_NUMBER      ITEMSET_DIM.ITEM_NUMBER%TYPE,
                                      P_IN_USERID           RESCORE_REQUEST_FORM.REQUESTED_USERID%TYPE,
                                      P_IN_REQUESTED_STATUS RESCORE_REQUEST_FORM.IS_REQUESTED%TYPE,
                                      P_IN_REQUESTED_DATE   RESCORE_REQUEST_FORM.REQUESTED_DATE%TYPE,
                                      P_OUT_STATUS_NUMBER   OUT NUMBER,
                                      P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE SP_RESET_ITEM_STATE(P_IN_SUBTESTID        IN SUBTEST_DIM.SUBTESTID %TYPE,
                                P_IN_STUDENT_BIO_ID   IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                                P_IN_USERID           RESCORE_REQUEST_FORM.REQUESTED_USERID%TYPE,
                                P_IN_REQUESTED_STATUS RESCORE_REQUEST_FORM.IS_REQUESTED%TYPE,
                                P_OUT_STATUS_NUMBER   OUT NUMBER,
                                P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE SP_RESET_ITEM_DATE(P_IN_STUDENT_BIO_ID IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                               P_IN_USERID         RESCORE_REQUEST_FORM.REQUESTED_USERID%TYPE,
                               P_IN_REQUESTED_DATE RESCORE_REQUEST_FORM.REQUESTED_DATE%TYPE,
                               P_OUT_STATUS_NUMBER OUT NUMBER,
                               P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_NOT_DNP_STUDENT(P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                   P_IN_PRODUCTID      IN PRODUCT.PRODUCTID%TYPE,
                                   P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                   P_IN_GRADEID        IN GRADE_DIM.GRADEID%TYPE,
                                   P_OUT_CUR_NOT_DNP   OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_NOT_DNP_STUDENT_DETAILS(P_IN_CUSTOMERID           IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                           P_IN_PRODUCTID            IN PRODUCT.PRODUCTID%TYPE,
                                           P_IN_ORG_NODEID           IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_GRADEID              IN GRADE_DIM.GRADEID%TYPE,
                                           P_IN_STUDENT_BIO_ID       IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                                           P_OUT_CUR_NOT_DNP_DETAILS OUT GET_REFCURSOR,
                                           P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

END PKG_RESCORE_REQUEST;
/
CREATE OR REPLACE PACKAGE BODY PKG_RESCORE_REQUEST AS

  PROCEDURE SP_GET_DNP_STUDENT_DETAILS(P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                       P_IN_PRODUCTID      IN PRODUCT.PRODUCTID%TYPE,
                                       P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_GRADEID        IN GRADE_DIM.GRADEID%TYPE,
                                       P_OUT_CUR_DNP       OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_DNP FOR
      SELECT DISTINCT RRF.STUDENT_BIO_ID STUDENT_BIO_ID,
                      RRF.STUDENT_LAST_NAME || ', ' ||
                      RRF.STUDENT_FIRST_NAME || ' ' ||
                      SUBSTR(RRF.STUDENT_MIDDLE_NAME, 1, 1) STUDENT_FULL_NAME,
                      NVL((SELECT DISTINCT REQUESTED_DATE
                            FROM RESCORE_REQUEST_FORM
                           WHERE UPDATED_DATE_TIME =
                                 (SELECT MAX(UPDATED_DATE_TIME)
                                    FROM RESCORE_REQUEST_FORM
                                   WHERE STUDENT_BIO_ID = RRF.STUDENT_BIO_ID
                                     /*AND IS_REQUESTED = 'Y'*/)),
                          -1) REQUESTED_DATE,
                      RRF.SUBTESTID SUBTESTID,
                      SD.SUBTEST_CODE SUBTEST_CODE,
                      SD.SUBTEST_SEQ SUBTEST_SEQ,
                      ISD.SESSION_ID SESSION_ID,
                      ISD.MODEULEID MODEULEID,
                      RRF.ORIGINAL_PERFORMANCE_LEVEL PERFORMANCE_LEVEL,
                      ISD.ITEM_NUMBER ITEM_NUMBER,
                      NVL(RRF.IS_REQUESTED, 'N') IS_REQUESTED,
                      RRF.REQUESTED_USERID USERID
        FROM RESCORE_REQUEST_FORM RRF,
             ITEMSET_DIM          ISD,
             SUBTEST_DIM          SD,
             CUST_PRODUCT_LINK    CPL,
             ORG_NODE_DIM         OND
       WHERE RRF.ELIGIBLE_FOR_RESCORE = 'Y'
         AND RRF.ORG_NODEID = OND.ORG_NODEID
         AND OND.PARENT_ORG_NODEID = P_IN_ORG_NODEID
         AND RRF.GRADEID = P_IN_GRADEID
         AND RRF.CUST_PROD_ID = CPL.CUST_PROD_ID
         AND CPL.CUSTOMERID = P_IN_CUSTOMERID
         AND CPL.PRODUCTID = P_IN_PRODUCTID
         AND RRF.ITEMSETID = ISD.ITEMSETID
         AND RRF.SUBTESTID = SD.SUBTESTID
         AND EXISTS (SELECT 1
                FROM RESCORE_REQUEST_FORM R
               WHERE R.STUDENT_BIO_ID = RRF.STUDENT_BIO_ID
                 AND R.ORIGINAL_PERFORMANCE_LEVEL = 'B')
       ORDER BY STUDENT_FULL_NAME, SUBTEST_SEQ, SESSION_ID, ITEM_NUMBER;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_DNP_STUDENT_DETAILS;

  PROCEDURE SP_SUBMIT_RESCORE_REQUEST(P_IN_STUDENT_BIO_ID   IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                                      P_IN_SUBTESTID        IN SUBTEST_DIM.SUBTESTID %TYPE,
                                      P_IN_SESSION_ID       ITEMSET_DIM.SESSION_ID %TYPE,
                                      P_IN_MODEULEID        ITEMSET_DIM. MODEULEID%TYPE,
                                      P_IN_ITEM_NUMBER      ITEMSET_DIM.ITEM_NUMBER%TYPE,
                                      P_IN_USERID           RESCORE_REQUEST_FORM.REQUESTED_USERID%TYPE,
                                      P_IN_REQUESTED_STATUS RESCORE_REQUEST_FORM.IS_REQUESTED%TYPE,
                                      P_IN_REQUESTED_DATE   RESCORE_REQUEST_FORM.REQUESTED_DATE%TYPE,
                                      P_OUT_STATUS_NUMBER   OUT NUMBER,
                                      P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    UPDATE RESCORE_REQUEST_FORM RRF
       SET RRF.IS_REQUESTED      = P_IN_REQUESTED_STATUS,
           RRF.UPDATED_DATE_TIME = SYSDATE,
           RRF.REQUESTED_USERID  = P_IN_USERID,
           RRF.REQUESTED_DATE    = P_IN_REQUESTED_DATE
     WHERE RRF.STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID
       AND RRF.SUBTESTID = P_IN_SUBTESTID
       AND RRF.ITEMSETID IN
           (SELECT RRF.ITEMSETID
              FROM RESCORE_REQUEST_FORM RRF, ITEMSET_DIM ISD
             WHERE RRF.ELIGIBLE_FOR_RESCORE = 'Y'
               AND RRF.ITEMSETID = ISD.ITEMSETID
               AND RRF.STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID
               AND ISD.SESSION_ID = P_IN_SESSION_ID
               AND ISD.MODEULEID = P_IN_MODEULEID
               AND ISD.ITEM_NUMBER = P_IN_ITEM_NUMBER);
  
    P_OUT_STATUS_NUMBER := 1;
  
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
  END SP_SUBMIT_RESCORE_REQUEST;

  PROCEDURE SP_RESET_ITEM_STATE(P_IN_SUBTESTID        IN SUBTEST_DIM.SUBTESTID %TYPE,
                                P_IN_STUDENT_BIO_ID   IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                                P_IN_USERID           RESCORE_REQUEST_FORM.REQUESTED_USERID%TYPE,
                                P_IN_REQUESTED_STATUS RESCORE_REQUEST_FORM.IS_REQUESTED%TYPE,
                                P_OUT_STATUS_NUMBER   OUT NUMBER,
                                P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    UPDATE RESCORE_REQUEST_FORM RRF
       SET RRF.IS_REQUESTED      = P_IN_REQUESTED_STATUS,
           RRF.REQUESTED_DATE    = NULL,
           RRF.UPDATED_DATE_TIME = SYSDATE,
           RRF.REQUESTED_USERID  = P_IN_USERID
     WHERE RRF.STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID
       AND RRF.SUBTESTID = P_IN_SUBTESTID;
  
    P_OUT_STATUS_NUMBER := 1;
  
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
  END SP_RESET_ITEM_STATE;

  PROCEDURE SP_RESET_ITEM_DATE(P_IN_STUDENT_BIO_ID IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                               P_IN_USERID         RESCORE_REQUEST_FORM.REQUESTED_USERID%TYPE,
                               P_IN_REQUESTED_DATE RESCORE_REQUEST_FORM.REQUESTED_DATE%TYPE,
                               P_OUT_STATUS_NUMBER OUT NUMBER,
                               P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    UPDATE RESCORE_REQUEST_FORM RRF
       SET /*RRF.IS_REQUESTED      = 'N',*/
           RRF.REQUESTED_DATE    = P_IN_REQUESTED_DATE,
           RRF.UPDATED_DATE_TIME = SYSDATE,
           RRF.REQUESTED_USERID  = P_IN_USERID
     WHERE RRF.STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID;
  
    P_OUT_STATUS_NUMBER := 1;
  
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
  END SP_RESET_ITEM_DATE;

  PROCEDURE SP_GET_NOT_DNP_STUDENT(P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                   P_IN_PRODUCTID      IN PRODUCT.PRODUCTID%TYPE,
                                   P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                   P_IN_GRADEID        IN GRADE_DIM.GRADEID%TYPE,
                                   P_OUT_CUR_NOT_DNP   OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_NOT_DNP FOR
      SELECT DISTINCT RRF.STUDENT_BIO_ID STUDENT_BIO_ID,
                      RRF.STUDENT_LAST_NAME || ', ' ||
                      RRF.STUDENT_FIRST_NAME || ' ' ||
                      SUBSTR(RRF.STUDENT_MIDDLE_NAME, 1, 1) STUDENT_FULL_NAME
        FROM RESCORE_REQUEST_FORM RRF,
             CUST_PRODUCT_LINK    CPL,
             ORG_NODE_DIM         OND
       WHERE RRF.ELIGIBLE_FOR_RESCORE = 'Y'
         AND RRF.ORG_NODEID = OND.ORG_NODEID
         AND OND.PARENT_ORG_NODEID = P_IN_ORG_NODEID
         AND RRF.GRADEID = P_IN_GRADEID
         AND RRF.CUST_PROD_ID = CPL.CUST_PROD_ID
         AND CPL.CUSTOMERID = P_IN_CUSTOMERID
         AND CPL.PRODUCTID = P_IN_PRODUCTID
         AND (RRF.ORIGINAL_PERFORMANCE_LEVEL = 'A' OR
             RRF.ORIGINAL_PERFORMANCE_LEVEL = 'P' OR
             RRF.ORIGINAL_PERFORMANCE_LEVEL = 'U')
         AND NOT EXISTS (SELECT 1
                FROM RESCORE_REQUEST_FORM R
               WHERE R.STUDENT_BIO_ID = RRF.STUDENT_BIO_ID
                 AND R.IS_REQUESTED = 'Y')
         AND NOT EXISTS
       (SELECT 1
                FROM RESCORE_REQUEST_FORM R
               WHERE R.STUDENT_BIO_ID = RRF.STUDENT_BIO_ID
                 AND R.ORIGINAL_PERFORMANCE_LEVEL = 'B')
       ORDER BY STUDENT_FULL_NAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_NOT_DNP_STUDENT;

  PROCEDURE SP_GET_NOT_DNP_STUDENT_DETAILS(P_IN_CUSTOMERID           IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                           P_IN_PRODUCTID            IN PRODUCT.PRODUCTID%TYPE,
                                           P_IN_ORG_NODEID           IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_GRADEID              IN GRADE_DIM.GRADEID%TYPE,
                                           P_IN_STUDENT_BIO_ID       IN STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE,
                                           P_OUT_CUR_NOT_DNP_DETAILS OUT GET_REFCURSOR,
                                           P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
  
  BEGIN
    IF P_IN_STUDENT_BIO_ID = 0 THEN
      OPEN P_OUT_CUR_NOT_DNP_DETAILS FOR
        SELECT DISTINCT RRF.STUDENT_BIO_ID STUDENT_BIO_ID,
                        RRF.STUDENT_LAST_NAME || ', ' ||
                        RRF.STUDENT_FIRST_NAME || ' ' ||
                        SUBSTR(RRF.STUDENT_MIDDLE_NAME, 1, 1) STUDENT_FULL_NAME,
                        NVL((SELECT DISTINCT REQUESTED_DATE
                              FROM RESCORE_REQUEST_FORM
                             WHERE UPDATED_DATE_TIME =
                                   (SELECT MAX(UPDATED_DATE_TIME)
                                      FROM RESCORE_REQUEST_FORM
                                     WHERE STUDENT_BIO_ID =
                                           RRF.STUDENT_BIO_ID
                                       /*AND IS_REQUESTED = 'Y'*/)
                               and rownum = 1),
                            -1) REQUESTED_DATE,
                        RRF.SUBTESTID SUBTESTID,
                        SD.SUBTEST_CODE SUBTEST_CODE,
                        SD.SUBTEST_SEQ SUBTEST_SEQ,
                        ISD.SESSION_ID SESSION_ID,
                        ISD.MODEULEID MODEULEID,
                        RRF.ORIGINAL_PERFORMANCE_LEVEL PERFORMANCE_LEVEL,
                        ISD.ITEM_NUMBER ITEM_NUMBER,
                        NVL(RRF.IS_REQUESTED, 'N') IS_REQUESTED,
                        RRF.REQUESTED_USERID USERID
          FROM RESCORE_REQUEST_FORM RRF,
               ITEMSET_DIM          ISD,
               SUBTEST_DIM          SD,
               CUST_PRODUCT_LINK    CPL,
               ORG_NODE_DIM         OND
         WHERE RRF.ELIGIBLE_FOR_RESCORE = 'Y'
           AND RRF.ORG_NODEID = OND.ORG_NODEID
           AND OND.PARENT_ORG_NODEID = P_IN_ORG_NODEID
           AND RRF.GRADEID = P_IN_GRADEID
           AND RRF.CUST_PROD_ID = CPL.CUST_PROD_ID
           AND CPL.CUSTOMERID = P_IN_CUSTOMERID
           AND CPL.PRODUCTID = P_IN_PRODUCTID
           AND RRF.ITEMSETID = ISD.ITEMSETID
           AND RRF.SUBTESTID = SD.SUBTESTID
           AND (RRF.ORIGINAL_PERFORMANCE_LEVEL = 'A' OR
               RRF.ORIGINAL_PERFORMANCE_LEVEL = 'P' OR
               RRF.ORIGINAL_PERFORMANCE_LEVEL = 'U')
           AND EXISTS (SELECT 1
                  FROM RESCORE_REQUEST_FORM R
                 WHERE R.STUDENT_BIO_ID = RRF.STUDENT_BIO_ID
                   AND R.IS_REQUESTED = 'Y')
           AND NOT EXISTS
         (SELECT 1
                  FROM RESCORE_REQUEST_FORM R
                 WHERE R.STUDENT_BIO_ID = RRF.STUDENT_BIO_ID
                   AND R.ORIGINAL_PERFORMANCE_LEVEL = 'B')
         ORDER BY STUDENT_FULL_NAME, SUBTEST_SEQ, SESSION_ID, ITEM_NUMBER;
    ELSE
      OPEN P_OUT_CUR_NOT_DNP_DETAILS FOR
        SELECT DISTINCT RRF.STUDENT_BIO_ID STUDENT_BIO_ID,
                        RRF.STUDENT_LAST_NAME || ', ' ||
                        RRF.STUDENT_FIRST_NAME || ' ' ||
                        SUBSTR(RRF.STUDENT_MIDDLE_NAME, 1, 1) STUDENT_FULL_NAME,
                        NVL((SELECT DISTINCT REQUESTED_DATE
                              FROM RESCORE_REQUEST_FORM
                             WHERE UPDATED_DATE_TIME =
                                   (SELECT MAX(UPDATED_DATE_TIME)
                                      FROM RESCORE_REQUEST_FORM
                                     WHERE STUDENT_BIO_ID =
                                           RRF.STUDENT_BIO_ID
                                       AND IS_REQUESTED = 'Y')),
                            -1) REQUESTED_DATE,
                        RRF.SUBTESTID SUBTESTID,
                        SD.SUBTEST_CODE SUBTEST_CODE,
                        SD.SUBTEST_SEQ SUBTEST_SEQ,
                        ISD.SESSION_ID SESSION_ID,
                        ISD.MODEULEID MODEULEID,
                        RRF.ORIGINAL_PERFORMANCE_LEVEL PERFORMANCE_LEVEL,
                        ISD.ITEM_NUMBER ITEM_NUMBER,
                        NVL(RRF.IS_REQUESTED, 'N') IS_REQUESTED,
                        RRF.REQUESTED_USERID USERID
          FROM RESCORE_REQUEST_FORM RRF, ITEMSET_DIM ISD, SUBTEST_DIM SD
         WHERE RRF.ELIGIBLE_FOR_RESCORE = 'Y'
           AND RRF.ITEMSETID = ISD.ITEMSETID
           AND RRF.SUBTESTID = SD.SUBTESTID
           AND RRF.STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID
         ORDER BY STUDENT_FULL_NAME, SUBTEST_SEQ, SESSION_ID, ITEM_NUMBER;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_NOT_DNP_STUDENT_DETAILS;

END PKG_RESCORE_REQUEST;
/
