CREATE OR REPLACE PACKAGE PKG_MENU_ACCESS IS

  -- Author  : TCS
  -- Created : 8/20/2014 4:06:11 PM
  -- Purpose : Menu Access

  TYPE REF_CURSOR IS REF CURSOR;

  PROCEDURE SP_GET_MENU_MAP(P_IN_ROLES          IN VARCHAR2,
                            P_IN_ORG_NODE_LEVEL IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                            P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                            P_OUT_REF_CURSOR    OUT REF_CURSOR,
                            P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_ACTION_MAP(P_IN_ROLES          IN VARCHAR2,
                              P_IN_ORG_NODE_LEVEL IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                              P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                              P_OUT_REF_CURSOR    OUT REF_CURSOR,
                              P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE GET_ALL_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                    P_IN_ROLES            IN VARCHAR2,
                                    P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                    P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                    P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                    P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE GET_GROWTH_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                       P_IN_ROLEID           IN ROLE.ROLEID%TYPE,
                                       P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                       P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE GET_EDU_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                    P_IN_ROLES            IN VARCHAR2,
                                    P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                    P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                    P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                    P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE GET_ALL_BUT_GRW_ASS_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                     P_IN_ROLES            IN VARCHAR2,
                                     P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                     P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                     P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                     P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

END PKG_MENU_ACCESS;
/
CREATE OR REPLACE PACKAGE BODY PKG_MENU_ACCESS IS

  -- SP_GET_MENU_MAP
  PROCEDURE SP_GET_MENU_MAP(P_IN_ROLES          IN VARCHAR2,
                            P_IN_ORG_NODE_LEVEL IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                            P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                            P_OUT_REF_CURSOR    OUT REF_CURSOR,
                            P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
    /*SELECT DISTINCT DM.MENU_NAME,
                                      DR.REPORT_NAME KEY,
                                      DR.REPORT_FOLDER_URI VALUE,
                                      DM.MENU_SEQ,
                                      DMRA.REPORT_SEQ
                        FROM DASH_MENU_RPT_ACCESS DMRA, DASH_REPORTS DR, DASH_MENUS DM
                       WHERE DMRA.DB_MENUID = DM.DB_MENUID
                         AND DMRA.DB_REPORTID = DR.DB_REPORTID
                         AND EXISTS (SELECT 1
                                FROM USER_ROLE A
                               WHERE A.ROLEID = DMRA.ROLEID
                                 AND A.USERID = P_IN_USERID)
                         AND NOT EXISTS
                       (SELECT 1
                                FROM INVITATION_CODE_CLAIM C, ORG_USERS K
                               WHERE C.ORG_USER_ID = K.ORG_USER_ID
                                 AND K.USERID = P_IN_USERID)
                         AND (EXISTS (SELECT 1
                                        FROM ORG_USERS A
                                       WHERE A.ORG_NODE_LEVEL = DMRA.ORG_LEVEL
                                         AND A.USERID = P_IN_USERID) OR
                              (EXISTS (SELECT 1
                                                FROM EDU_CENTER_USER_LINK A
                                               WHERE A.USERID = P_IN_USERID)))
                         AND DR.ACTIVATION_STATUS = 'AC'
                         AND DMRA.ACTIVATION_STATUS = 'AC'
                       ORDER BY DM.MENU_SEQ, DMRA.REPORT_SEQ;*/

      SELECT DISTINCT DM.MENU_NAME,
                      DR.REPORT_NAME KEY,
                      DR.REPORT_FOLDER_URI VALUE,
                      DM.MENU_SEQ,
                      DMRA.REPORT_SEQ
        FROM DASH_MENU_RPT_ACCESS DMRA, DASH_REPORTS DR, DASH_MENUS DM
       WHERE DMRA.DB_MENUID = DM.DB_MENUID
         AND DMRA.DB_REPORTID = DR.DB_REPORTID
         AND NOT EXISTS
       (SELECT 1
                FROM (SELECT ROLEID
                        FROM ROLE
                       WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                                  FROM DUAL)
                        SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                          FROM T
                        CONNECT BY LEVEL <=
                                   LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
                      )
               WHERE ROLEID = 6)

         AND DMRA.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                          FROM DUAL)
                SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                  FROM T
                CONNECT BY LEVEL <=
                           LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
              )
         AND ((DMRA.ORG_LEVEL = P_IN_ORG_NODE_LEVEL) OR
             (DMRA.ORG_LEVEL = -99))
         AND DR.ACTIVATION_STATUS = 'AC'
         AND DMRA.ACTIVATION_STATUS = 'AC'
         AND DMRA.CUST_PROD_ID = P_IN_CUST_PROD_ID
       ORDER BY DM.MENU_SEQ, DMRA.REPORT_SEQ;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_MENU_MAP;

  -- SP_GET_ACTION_MAP
  PROCEDURE SP_GET_ACTION_MAP(P_IN_ROLES          IN VARCHAR2,
                              P_IN_ORG_NODE_LEVEL IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                              P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                              P_OUT_REF_CURSOR    OUT REF_CURSOR,
                              P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

    -- V_CUST_PRODID NUMBER := 0;

  BEGIN

    /* IF P_IN_CUST_PROD_ID = 0 THEN

      SELECT A.DEFAULT_CUST_PROD_ID
        INTO V_CUST_PRODID
        FROM (SELECT CPL.CUST_PROD_ID DEFAULT_CUST_PROD_ID,
                     DENSE_RANK() OVER(PARTITION BY ORG.USERID ORDER BY P.PRODUCT_SEQ DESC) AS MAX_VAL
                FROM ORG_USERS         ORG,
                     ORG_PRODUCT_LINK  OPL,
                     CUST_PRODUCT_LINK CPL,
                     PRODUCT           P
               WHERE ORG.USERID = P_IN_USERID
                 AND ORG.ORG_NODEID = OPL.ORG_NODEID
                 AND OPL.CUST_PROD_ID = CPL.CUST_PROD_ID
                 AND CPL.PRODUCTID = P.PRODUCTID) A
       WHERE A.MAX_VAL = 1
      UNION
      SELECT CUST_PROD_ID
        FROM EDU_CENTER_USER_LINK
       WHERE USERID = P_IN_USERID;

    ELSE
      V_CUST_PRODID := P_IN_CUST_PROD_ID;
    END IF;*/

    OPEN P_OUT_REF_CURSOR FOR

    /* SELECT DR.REPORT_NAME, DRA.ACTION_NAME
                        FROM DASH_ACTION_ACCESS DAA, DASH_REPORTS DR, DASH_RPT_ACTION DRA
                       WHERE DAA.DB_REPORTID = DR.DB_REPORTID
                         AND DAA.DB_ACTIONID = DRA.DB_ACTIONID
                         AND EXISTS
                       (SELECT 1
                                FROM USER_ROLE A
                               WHERE A.ROLEID = DAA.ROLEID
                                 AND A.USERID = P_IN_USERID)
                         AND (EXISTS (SELECT 1
                                        FROM ORG_USERS A
                                       WHERE A.ORG_NODE_LEVEL = DAA.ORG_LEVEL
                                         AND A.USERID = P_IN_USERID) OR
                              (EXISTS (SELECT 1
                                                FROM EDU_CENTER_USER_LINK A
                                               WHERE A.USERID = P_IN_USERID)))
                         AND DR.ACTIVATION_STATUS = 'AC'
                         AND DAA.ACTIVATION_STATUS = 'AC'
                         AND DAA.CUST_PROD_ID = V_CUST_PRODID
                       GROUP BY DR.REPORT_NAME, DRA.ACTION_NAME
                       ORDER BY DR.REPORT_NAME;*/

      SELECT DR.REPORT_NAME, DRA.ACTION_NAME
        FROM DASH_ACTION_ACCESS DAA, DASH_REPORTS DR, DASH_RPT_ACTION DRA
       WHERE DAA.DB_REPORTID = DR.DB_REPORTID
         AND DAA.DB_ACTIONID = DRA.DB_ACTIONID
         AND NOT EXISTS
       (SELECT 1
                FROM (SELECT ROLEID
                        FROM ROLE
                       WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                                  FROM DUAL)
                        SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                          FROM T
                        CONNECT BY LEVEL <=
                                   LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
                      )
               WHERE ROLEID = 6)

         AND DAA.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                          FROM DUAL)
                SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                  FROM T
                CONNECT BY LEVEL <=
                           LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
              )
         AND ((DAA.ORG_LEVEL = P_IN_ORG_NODE_LEVEL) OR
             (DAA.ORG_LEVEL = -99))
         AND DR.ACTIVATION_STATUS = 'AC'
         AND DAA.ACTIVATION_STATUS = 'AC'
         AND DAA.CUST_PROD_ID = P_IN_CUST_PROD_ID
       GROUP BY DR.REPORT_NAME, DRA.ACTION_NAME
       ORDER BY DR.REPORT_NAME;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_ACTION_MAP;

  PROCEDURE GET_ALL_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                    P_IN_ROLES            IN VARCHAR2,
                                    P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                    P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                    P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                    P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
      /*SELECT DISTINCT ASS.DB_MENUID MENU_ID,
                      ASS.MENU_NAME MENU_NAME,
                      RE.DB_REPORTID REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
         AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                          FROM DUAL)
                SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                  FROM T
                CONNECT BY LEVEL <=
                           LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
              )
         AND ACC.ORG_LEVEL = P_IN_ORG_NODE_LEVEL
         AND ACC.CUST_PROD_ID = P_IN_CUST_PROD_ID
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;*/
       
       SELECT DISTINCT ASS.DB_MENUID MENU_ID,
                      ASS.MENU_NAME MENU_NAME,
                      RE.DB_REPORTID REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL ORGLEVEL
        FROM (SELECT DB_REPORTID,ACTIVATION_STATUS,REPORT_NAME,REPORT_TYPE,REPORT_FOLDER_URI FROM DASH_REPORTS RE1 WHERE RE1.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE) AND RE1.ACTIVATION_STATUS = 'AC')RE , 
              DASH_MENUS ASS, 
              DASH_MENU_RPT_ACCESS ACC
       WHERE /*RE.REPORT_TYPE LIKE ('API%')
         AND */ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
         --AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                          FROM DUAL)
                SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                  FROM T
                CONNECT BY LEVEL <=
                           LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
              )
         AND ACC.ORG_LEVEL =P_IN_ORG_NODE_LEVEL
         AND ACC.CUST_PROD_ID = P_IN_CUST_PROD_ID
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;
       
       
       
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END GET_ALL_ASSESSMENT_LIST;

  PROCEDURE GET_GROWTH_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                       P_IN_ROLEID           IN ROLE.ROLEID%TYPE,
                                       P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                       P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
      SELECT DISTINCT ASS.DB_MENUID MENU_ID,
                      ASS.MENU_NAME MENU_NAME,
                      RE.DB_REPORTID REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
         AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ROLEID = P_IN_ROLEID
         AND ACC.ORG_LEVEL = P_IN_ORG_NODE_LEVEL
         AND ACC.CUST_PROD_ID = P_IN_CUST_PROD_ID
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END GET_GROWTH_ASSESSMENT_LIST;

  PROCEDURE GET_EDU_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                    P_IN_ROLES            IN VARCHAR2,
                                    P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                    P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                    P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                    P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
      SELECT DISTINCT ASS.DB_MENUID MENU_ID,
                      ASS.MENU_NAME MENU_NAME,
                      RE.DB_REPORTID REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      -99 ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
         AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                          FROM DUAL)
                SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                  FROM T
                CONNECT BY LEVEL <=
                           LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
              )
         AND ACC.ORG_LEVEL = P_IN_ORG_NODE_LEVEL
         AND ACC.CUST_PROD_ID = P_IN_CUST_PROD_ID
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END GET_EDU_ASSESSMENT_LIST;

  PROCEDURE GET_ALL_BUT_GRW_ASS_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                     P_IN_ROLES            IN VARCHAR2,
                                     P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                     P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                     P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                     P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
      SELECT DISTINCT ASS.DB_MENUID MENU_ID,
                      ASS.MENU_NAME MENU_NAME,
                      RE.DB_REPORTID REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
         AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ACTIVATION_STATUS = 'AC'
         AND NOT EXISTS
       (SELECT 1
                FROM DASH_MENU_RPT_ACCESS R
               WHERE R.DB_REPORTID = ACC.DB_REPORTID
                 AND R.ROLEID = 8
                 AND R.ORG_LEVEL = ACC.ORG_LEVEL)
         AND ACC.ORG_LEVEL = P_IN_ORG_NODE_LEVEL
         AND ACC.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND ACC.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES AS TXT
                                          FROM DUAL)
                SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                  FROM T
                CONNECT BY LEVEL <=
                           LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
              )
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END GET_ALL_BUT_GRW_ASS_LIST;

END PKG_MENU_ACCESS;
/
