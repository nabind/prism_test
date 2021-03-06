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
  
    V_COUNT NUMBER := 0;
  
  BEGIN
    SELECT COUNT(1)
      INTO V_COUNT
      FROM ROLE
     WHERE ROLE_NAME IN
           (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
             SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
               FROM T
             CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
            )
       AND ROLEID = 12;
  
    IF V_COUNT = 0 THEN
      --For other than rescore user                                                     
      OPEN P_OUT_REF_CURSOR FOR
      
        SELECT DISTINCT DM.MENU_NAME,
                        DR.REPORT_NAME       KEY,
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
                         WHERE ROLE_NAME IN
                               (WITH T AS
                                (SELECT P_IN_ROLES AS TXT FROM DUAL)
                                 SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                                   FROM T
                                 CONNECT BY LEVEL <=
                                            LENGTH(REGEXP_REPLACE(TXT,
                                                                  '[^,]*')) + 1
                                ))
                 WHERE ROLEID = 6)
              
           AND DMRA.ROLEID IN
               (SELECT ROLEID
                  FROM ROLE
                 WHERE ROLE_NAME IN
                       (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                         SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                           FROM T
                         CONNECT BY LEVEL <=
                                    LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                        ))
           AND ((DMRA.ORG_LEVEL = P_IN_ORG_NODE_LEVEL) OR
               (DMRA.ORG_LEVEL = -99))
              --AND DR.ACTIVATION_STATUS = 'AC'
           AND DMRA.ACTIVATION_STATUS = 'AC'
           AND DMRA.CUST_PROD_ID = P_IN_CUST_PROD_ID
         ORDER BY DM.MENU_SEQ, DMRA.REPORT_SEQ;
    
    ELSE
      -- For rescore user
      OPEN P_OUT_REF_CURSOR FOR
      
        SELECT DISTINCT DM.MENU_NAME,
                        DR.REPORT_NAME       KEY,
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
                         WHERE ROLE_NAME IN
                               (WITH T AS
                                (SELECT P_IN_ROLES AS TXT FROM DUAL)
                                 SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                                   FROM T
                                 CONNECT BY LEVEL <=
                                            LENGTH(REGEXP_REPLACE(TXT,
                                                                  '[^,]*')) + 1
                                ))
                 WHERE ROLEID = 6)
              
           AND DMRA.ROLEID IN
               (SELECT ROLEID
                  FROM ROLE
                 WHERE ROLE_NAME IN
                       (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                         SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                           FROM T
                         CONNECT BY LEVEL <=
                                    LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                        )
                   AND ROLEID = 12)
           AND ((DMRA.ORG_LEVEL = P_IN_ORG_NODE_LEVEL) OR
               (DMRA.ORG_LEVEL = -99))
              --AND DR.ACTIVATION_STATUS = 'AC'
           AND DMRA.ACTIVATION_STATUS = 'AC'
           AND DMRA.CUST_PROD_ID = P_IN_CUST_PROD_ID
         ORDER BY DM.MENU_SEQ, DMRA.REPORT_SEQ;
    END IF;
  
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
  
  BEGIN
  
    OPEN P_OUT_REF_CURSOR FOR
      SELECT DR.REPORT_NAME, DRA.ACTION_NAME
        FROM DASH_ACTION_ACCESS DAA, DASH_REPORTS DR, DASH_RPT_ACTION DRA
       WHERE DAA.DB_REPORTID = DR.DB_REPORTID
         AND DAA.DB_ACTIONID = DRA.DB_ACTIONID
         AND NOT EXISTS
       (SELECT 1
                FROM (SELECT ROLEID
                        FROM ROLE
                       WHERE ROLE_NAME IN
                             (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                               SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                                 FROM T
                               CONNECT BY LEVEL <=
                                          LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                              ))
               WHERE ROLEID = 6)
            
         AND DAA.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN
                     (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      ))
         AND ((DAA.ORG_LEVEL = P_IN_ORG_NODE_LEVEL) /*OR
                                                    (DAA.ORG_LEVEL = -99)*/
             )
            --AND DR.ACTIVATION_STATUS = 'AC'
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
      SELECT DISTINCT ASS.DB_MENUID        MENU_ID,
                      ASS.MENU_NAME        MENU_NAME,
                      RE.DB_REPORTID       REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE          TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL        ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
            --AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ACTIVATION_STATUS = 'AC'
         AND ACC.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN
                     (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      ))
         AND ACC.ORG_LEVEL = P_IN_ORG_NODE_LEVEL
         AND ACC.CUST_PROD_ID = P_IN_CUST_PROD_ID
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END GET_ALL_ASSESSMENT_LIST;

  /*To get menu item for growth user and rescore user*/
  PROCEDURE GET_GROWTH_ASSESSMENT_LIST(P_IN_REPORT_TYPE_LIKE IN VARCHAR2,
                                       P_IN_ROLEID           IN ROLE.ROLEID%TYPE,
                                       P_IN_ORG_NODE_LEVEL   IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                       P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_OUT_REF_CURSOR      OUT REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
      SELECT DISTINCT ASS.DB_MENUID        MENU_ID,
                      ASS.MENU_NAME        MENU_NAME,
                      RE.DB_REPORTID       REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE          TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL        ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
            --AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ACTIVATION_STATUS = 'AC'
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
      SELECT DISTINCT ASS.DB_MENUID        MENU_ID,
                      ASS.MENU_NAME        MENU_NAME,
                      RE.DB_REPORTID       REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE          TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      -99                  ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
            --AND RE.ACTIVATION_STATUS = 'AC'
         AND ACC.ACTIVATION_STATUS = 'AC'
         AND ACC.ROLEID IN
             (SELECT ROLEID
                FROM ROLE
               WHERE ROLE_NAME IN
                     (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      ))
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
      SELECT DISTINCT ASS.DB_MENUID        MENU_ID,
                      ASS.MENU_NAME        MENU_NAME,
                      RE.DB_REPORTID       REPORT_ID,
                      REPORT_NAME,
                      REPORT_FOLDER_URI,
                      RE.ACTIVATION_STATUS STATUS,
                      REPORT_TYPE          TYPE,
                      ASS.MENU_SEQ,
                      ACC.REPORT_SEQ,
                      ACC.ORG_LEVEL        ORGLEVEL
        FROM DASH_REPORTS RE, DASH_MENUS ASS, DASH_MENU_RPT_ACCESS ACC
       WHERE RE.REPORT_TYPE LIKE (P_IN_REPORT_TYPE_LIKE)
         AND ASS.DB_MENUID = ACC.DB_MENUID
         AND ACC.DB_REPORTID = RE.DB_REPORTID
            --AND RE.ACTIVATION_STATUS = 'AC'
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
               WHERE ROLE_NAME IN
                     (WITH T AS (SELECT P_IN_ROLES AS TXT FROM DUAL)
                       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_ID_LEVEL_ID
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                      ))
       ORDER BY ASS.MENU_SEQ, ACC.REPORT_SEQ, RE.REPORT_NAME DESC;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END GET_ALL_BUT_GRW_ASS_LIST;

END PKG_MENU_ACCESS;
/
