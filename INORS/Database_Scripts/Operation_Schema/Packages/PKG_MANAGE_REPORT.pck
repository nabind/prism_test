CREATE OR REPLACE PACKAGE PKG_MANAGE_REPORT AS

  -- Author  : Joy Kumar Pal
  -- Created : 8/20/2014 12:25:11 PM
  -- Purpose : To manage all the messages dynamically

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_GET_SYSTEM_MESSAGE(P_IN_REPORT_NAME    IN DASH_REPORTS.REPORT_NAME%TYPE,
                                  P_IN_MESSAGE_TYPE   IN DASH_MESSAGE_TYPE.MESSAGE_TYPE%TYPE,
                                  P_IN_MESSAGE_NAME   IN DASH_MESSAGE_TYPE.MESSAGE_NAME%TYPE,
                                  P_IN_USERID         IN USERS.USERID%TYPE,
                                  P_OUT_CUR_MESSAGE   OUT GET_REFCURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_REPORT_MESSAGE_LIST(P_IN_REPORT_ID      IN DASH_REPORTS.DB_REPORTID%TYPE,
                                       P_IN_MESSAGE_TYPE   IN DASH_MESSAGE_TYPE.MESSAGE_TYPE%TYPE,
                                       P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_OUT_CUR_MESSAGE   OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_REPORT_LIST(P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                               P_IN_REPORT_ID      IN DASH_REPORTS.DB_REPORTID%TYPE,
                               P_OUT_CUR_REPORT    OUT GET_REFCURSOR,
                               P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_ADD_REPORT(P_IN_REPORT_NAME       IN DASH_REPORTS.REPORT_NAME%TYPE,
                          P_IN_REPORT_DESC       IN DASH_REPORTS.REPORT_DESC%TYPE,
                          P_IN_REPORT_TYPE       IN DASH_REPORTS.REPORT_TYPE%TYPE,
                          P_IN_REPORT_FOLDER_URI IN DASH_REPORTS.REPORT_FOLDER_URI%TYPE,
                          P_IN_ACTIVATION_STATUS IN DASH_REPORTS.ACTIVATION_STATUS%TYPE,
                          P_IN_USER_ROLES        IN VARCHAR2,
                          P_IN_ORG_NODE_LEVELS   IN VARCHAR2,
                          P_IN_DB_MENUID         IN DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE,
                          P_IN_CUST_PROD_IDS     IN VARCHAR2,
                          P_IN_CUSTOMERID        IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                          P_OUT_STATUS_NUMBER    OUT NUMBER,
                          P_OUT_CUR_REPORT_NEW   OUT GET_REFCURSOR,
                          P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_EDIT_REPORT(P_IN_DB_REPORTID       DASH_REPORTS.DB_REPORTID%TYPE,
                           P_IN_REPORT_NAME       DASH_REPORTS.REPORT_NAME%TYPE,
                           P_IN_REPORT_DESC       DASH_REPORTS.REPORT_DESC%TYPE,
                           P_IN_REPORT_TYPE       DASH_REPORTS.REPORT_TYPE%TYPE,
                           P_IN_REPORT_FOLDER_URI DASH_REPORTS.REPORT_FOLDER_URI%TYPE,
                           P_IN_ACTIVATION_STATUS DASH_REPORTS.ACTIVATION_STATUS%TYPE,
                           P_IN_USER_ROLES        VARCHAR2,
                           P_IN_ORG_NODE_LEVELS   VARCHAR2,
                           P_IN_DB_MENUID         DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE,
                           P_IN_CUST_PROD_IDS     IN VARCHAR2,
                           P_IN_REPORT_SEQ        DASH_MENU_RPT_ACCESS.REPORT_SEQ%TYPE,
                           P_OUT_STATUS_NUMBER    OUT NUMBER,
                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_DELETE_REPORT(P_IN_DB_REPORTID    DASH_REPORTS.DB_REPORTID%TYPE,
                             P_OUT_STATUS_NUMBER OUT NUMBER,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_EDIT_ACTION_DATA(P_IN_REPORTID       IN DASH_REPORTS.DB_REPORTID%TYPE,
                                P_OUT_REPORT_CURSOR OUT GET_REFCURSOR,
                                P_OUT_ACTION_CURSOR OUT GET_REFCURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

END PKG_MANAGE_REPORT;
/
CREATE OR REPLACE PACKAGE BODY PKG_MANAGE_REPORT AS

  /*
  THIS PROCEDURE RETURNS MESSAGES FOR 
  LANDING PAGE,LOGIN PAGE.
  */
  PROCEDURE SP_GET_SYSTEM_MESSAGE(P_IN_REPORT_NAME    IN DASH_REPORTS.REPORT_NAME%TYPE,
                                  P_IN_MESSAGE_TYPE   IN DASH_MESSAGE_TYPE.MESSAGE_TYPE%TYPE,
                                  P_IN_MESSAGE_NAME   IN DASH_MESSAGE_TYPE.MESSAGE_NAME%TYPE,
                                  P_IN_USERID         IN USERS.USERID%TYPE,
                                  P_OUT_CUR_MESSAGE   OUT GET_REFCURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
    V_CUST_PRODID NUMBER := 0;
  
  BEGIN
    IF P_IN_MESSAGE_TYPE = 'GSCM' THEN
    
      OPEN P_OUT_CUR_MESSAGE FOR
        SELECT DM.REPORT_MSG AS REPORT_MSG
          FROM DASH_REPORTS DR, DASH_MESSAGES DM, DASH_MESSAGE_TYPE DMT
         WHERE DM.MSG_TYPEID = DMT.MSG_TYPEID
           AND DR.DB_REPORTID = DM.DB_REPORTID
           AND DR.REPORT_NAME = P_IN_REPORT_NAME
           AND DMT.MESSAGE_TYPE = P_IN_MESSAGE_TYPE
           AND DMT.MESSAGE_NAME = P_IN_MESSAGE_NAME
           AND DM.ACTIVATION_STATUS = 'AC';
    
    ELSIF P_IN_MESSAGE_TYPE = 'PSCM' THEN
    
      SELECT A.DEFAULT_CUST_PROD_ID
        INTO V_CUST_PRODID
        FROM (SELECT CPL.CUST_PROD_ID DEFAULT_CUST_PROD_ID,
                     DENSE_RANK() OVER(PARTITION BY ORG.USERID ORDER BY P.PRODUCT_SEQ DESC) AS MAX_VAL
                FROM ORG_USERS ORG,
                     ORG_PRODUCT_LINK OPL,
                     CUST_PRODUCT_LINK CPL,
                     PRODUCT P,
                     (SELECT ADMINID, ADMIN_YEAR
                        FROM ADMIN_DIM
                       WHERE ADMIN_YEAR <=
                             (SELECT ADMIN_YEAR
                                FROM ADMIN_DIM
                               WHERE IS_CURRENT_ADMIN = 'Y')) ADMIN
               WHERE ORG.USERID = P_IN_USERID
                 AND ORG.ORG_NODEID = OPL.ORG_NODEID
                 AND OPL.CUST_PROD_ID = CPL.CUST_PROD_ID
                 AND CPL.PRODUCTID = P.PRODUCTID
                 AND ADMIN.ADMINID = CPL.ADMINID) A
       WHERE A.MAX_VAL = 1
      UNION
      SELECT CUST_PROD_ID
        FROM EDU_CENTER_USER_LINK
       WHERE USERID = P_IN_USERID;
    
      IF V_CUST_PRODID = 0 THEN
        SELECT CUST_PROD_ID
          INTO V_CUST_PRODID
          FROM EDU_CENTER_USER_LINK
         WHERE USERID = P_IN_USERID;
      END IF;
    
      OPEN P_OUT_CUR_MESSAGE FOR
        SELECT DM.REPORT_MSG AS REPORT_MSG
          FROM DASH_REPORTS DR, DASH_MESSAGES DM, DASH_MESSAGE_TYPE DMT
         WHERE DM.MSG_TYPEID = DMT.MSG_TYPEID
           AND DMT.CUST_PROD_ID = DM.CUST_PROD_ID
           AND DR.DB_REPORTID = DM.DB_REPORTID
           AND DR.REPORT_NAME = P_IN_REPORT_NAME
           AND DMT.MESSAGE_TYPE = P_IN_MESSAGE_TYPE
           AND DMT.MESSAGE_NAME = P_IN_MESSAGE_NAME
           AND DMT.CUST_PROD_ID = V_CUST_PRODID
           AND DM.ACTIVATION_STATUS = 'AC';
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_SYSTEM_MESSAGE;

  /*
  THIS PROCEDURE RETURNS THE GENERIC MESSAGES FOR 
  CONFIGURE MESSAGE PAGE.
  */
  PROCEDURE SP_GET_REPORT_MESSAGE_LIST(P_IN_REPORT_ID      IN DASH_REPORTS.DB_REPORTID%TYPE,
                                       P_IN_MESSAGE_TYPE   IN DASH_MESSAGE_TYPE.MESSAGE_TYPE%TYPE,
                                       P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_OUT_CUR_MESSAGE   OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
  
    IF P_IN_MESSAGE_TYPE = 'GSCM' THEN
      /*
         RULES:
      1. THERE SHOULD BE ONLY ONE ENTRY FOR MESSAGE_TYPE(GSCM) IN DASH_MESSAGE_TYPE 
         ASSOCIATED WITH DEFAULT CUST_PROD_ID(5001)
      2.IT DOES NOT DEPENDS UPON CUST_PROD_ID.
         */
      OPEN P_OUT_CUR_MESSAGE FOR
        SELECT DMT.MSG_TYPEID       MESSAGE_TYPEID,
               DMT.MESSAGE_NAME     MESSAGE_NAME,
               DMT.MESSAGE_TYPE     MESSAGE_TYPE,
               DMT.DESCRIPTION      MESSAGE_DESC,
               DM.REPORT_MSG        MESSAGE,
               P_IN_REPORT_ID       REPORTID,
               DMT.CUST_PROD_ID     CUST_PROD_ID,
               DM.ACTIVATION_STATUS ACTIVATION_STATUS
          FROM DASH_MESSAGE_TYPE DMT, DASH_MESSAGES DM
         WHERE DMT.CUST_PROD_ID = DM.CUST_PROD_ID(+)
           AND DMT.MSG_TYPEID = DM.MSG_TYPEID(+)
           AND DM.DB_REPORTID(+) = P_IN_REPORT_ID
           AND DMT.MESSAGE_TYPE IN ('GSCM')
         ORDER BY DMT.MESSAGE_NAME;
    
    ELSIF P_IN_MESSAGE_TYPE = 'PSCM' THEN
      OPEN P_OUT_CUR_MESSAGE FOR
        SELECT DMT.MSG_TYPEID       MESSAGE_TYPEID,
               DMT.MESSAGE_NAME     MESSAGE_NAME,
               DMT.MESSAGE_TYPE     MESSAGE_TYPE,
               DMT.DESCRIPTION      MESSAGE_DESC,
               DM.REPORT_MSG        MESSAGE,
               P_IN_REPORT_ID       REPORTID,
               P_IN_CUST_PROD_ID    CUST_PROD_ID,
               DM.ACTIVATION_STATUS ACTIVATION_STATUS
          FROM DASH_MESSAGE_TYPE DMT, DASH_MESSAGES DM
         WHERE DMT.CUST_PROD_ID = DM.CUST_PROD_ID(+)
           AND DMT.MSG_TYPEID = DM.MSG_TYPEID(+)
           AND DM.DB_REPORTID(+) = P_IN_REPORT_ID
           AND DMT.MESSAGE_TYPE IN ('PSCM')
           AND DMT.CUST_PROD_ID = P_IN_CUST_PROD_ID
         ORDER BY DMT.MESSAGE_NAME;
    
    ELSE
      OPEN P_OUT_CUR_MESSAGE FOR
        SELECT DMT.MSG_TYPEID       MESSAGE_TYPEID,
               DMT.MESSAGE_NAME     MESSAGE_NAME,
               DMT.MESSAGE_TYPE     MESSAGE_TYPE,
               DMT.DESCRIPTION      MESSAGE_DESC,
               DM.REPORT_MSG        MESSAGE,
               P_IN_REPORT_ID       REPORTID,
               P_IN_CUST_PROD_ID    CUST_PROD_ID,
               DM.ACTIVATION_STATUS ACTIVATION_STATUS
          FROM DASH_MESSAGE_TYPE DMT, DASH_MESSAGES DM
         WHERE DMT.CUST_PROD_ID = DM.CUST_PROD_ID(+)
           AND DMT.MSG_TYPEID = DM.MSG_TYPEID(+)
           AND DM.DB_REPORTID(+) = P_IN_REPORT_ID
           AND DMT.MESSAGE_TYPE NOT IN ('PSCM', 'GSCM')
           AND DMT.CUST_PROD_ID = P_IN_CUST_PROD_ID
         ORDER BY DMT.MESSAGE_NAME;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_REPORT_MESSAGE_LIST;

  /*
  THIS PROCEDURE RETURNS THE REPORTS IN MANAGE REPORT SCREEN 
  OR RETURN A PARTICULAR REPORT DETAILS BASED ON REPORT ID(FOR EDIT).
  */
  PROCEDURE SP_GET_REPORT_LIST(P_IN_CUSTOMERID     IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                               P_IN_REPORT_ID      IN DASH_REPORTS.DB_REPORTID%TYPE,
                               P_OUT_CUR_REPORT    OUT GET_REFCURSOR,
                               P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
    IF P_IN_REPORT_ID = '-99' THEN
      OPEN P_OUT_CUR_REPORT FOR
        SELECT ID,
               REPORT_DESC,
               REPORT_TYPE,
               LTRIM(REGEXP_REPLACE(' ' || LISTAGG(CUST_PROD_ID, ', ')
                                    WITHIN GROUP(ORDER BY CUST_PROD_ID),
                                    '([^,]*)(,\1)+($|,)',
                                    '\1\3')) AS CUST_PROD_ID,
               LTRIM(REGEXP_REPLACE(' ' || LISTAGG(ROLE_NAME, ', ') WITHIN
                                    GROUP(ORDER BY ROLE_NAME),
                                    '([^,]*)(,\1)+($|,)',
                                    '\1\3')) AS ROLES,
               LTRIM(REGEXP_REPLACE(' ' || LISTAGG(ORG_LABEL, ', ') WITHIN
                                    GROUP(ORDER BY ORG_LABEL),
                                    '([^,]*)(,\1)+($|,)',
                                    '\1\3')) AS ORG_NODE_LEVEL,
               REPORT_NAME,
               REPORT_FOLDER_URI,
               STATUS,
               MENUID,
               MENU_SEQ,
               REPORT_SEQ,
               MENUNAME
          FROM (SELECT RE.DB_REPORTID ID,
                       RE.REPORT_DESC,
                       RE.REPORT_TYPE,
                       DMRA.CUST_PROD_ID,
                       RE.REPORT_NAME,
                       RE.REPORT_FOLDER_URI,
                       RE.ACTIVATION_STATUS STATUS,
                       ROLE_NAME,
                       ORG_LABEL,
                       DMRA.DB_MENUID MENUID,
                       DMENU.MENU_SEQ,
                       DMRA.REPORT_SEQ,
                       DMENU.MENU_NAME MENUNAME
                  FROM DASH_REPORTS         RE,
                       DASH_MENU_RPT_ACCESS DMRA,
                       DASH_MENUS           DMENU,
                       ROLE                 R,
                       ORG_TP_STRUCTURE     OTS,
                       CUST_PRODUCT_LINK    CPL
                 WHERE RE.DB_REPORTID = DMRA.DB_REPORTID
                   AND DMENU.DB_MENUID = DMRA.DB_MENUID
                   AND R.ROLEID = DMRA.ROLEID
                   AND OTS.ORG_LEVEL = DMRA.ORG_LEVEL
                   AND CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID
                   AND CPL.CUSTOMERID = P_IN_CUSTOMERID
                UNION
                SELECT RE.DB_REPORTID ID,
                       RE.REPORT_DESC,
                       RE.REPORT_TYPE,
                       DMRA.CUST_PROD_ID,
                       RE.REPORT_NAME,
                       RE.REPORT_FOLDER_URI,
                       RE.ACTIVATION_STATUS STATUS,
                       ROLE_NAME,
                       'Education Center' ORG_LABEL,
                       DMRA.DB_MENUID MENUID,
                       DMENU.MENU_SEQ,
                       DMRA.REPORT_SEQ,
                       DMENU.MENU_NAME MENUNAME
                  FROM DASH_REPORTS         RE,
                       DASH_MENU_RPT_ACCESS DMRA,
                       DASH_MENUS           DMENU,
                       ROLE                 R,
                       CUST_PRODUCT_LINK    CPL,
                       PRODUCT              P
                 WHERE RE.DB_REPORTID = DMRA.DB_REPORTID
                   AND DMENU.DB_MENUID = DMRA.DB_MENUID
                   AND R.ROLEID = DMRA.ROLEID
                   AND CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID
                   AND P.PRODUCTID = CPL.PRODUCTID
                   AND CPL.CUSTOMERID = P_IN_CUSTOMERID
                   AND DMRA.ORG_LEVEL = -99)
         GROUP BY ID,
                  REPORT_DESC,
                  REPORT_TYPE,
                  REPORT_NAME,
                  REPORT_FOLDER_URI,
                  STATUS,
                  MENUID,
                  MENU_SEQ,
                  REPORT_SEQ,
                  MENUNAME;
    ELSE
      OPEN P_OUT_CUR_REPORT FOR
        SELECT ID,
               REPORT_DESC,
               REPORT_TYPE,
               LTRIM(REGEXP_REPLACE(' ' || LISTAGG(CUST_PROD_ID, ', ')
                                    WITHIN GROUP(ORDER BY CUST_PROD_ID),
                                    '([^,]*)(,\1)+($|,)',
                                    '\1\3')) AS CUST_PROD_ID,
               LTRIM(REGEXP_REPLACE(' ' || LISTAGG(ROLE_NAME, ', ') WITHIN
                                    GROUP(ORDER BY ROLE_NAME),
                                    '([^,]*)(,\1)+($|,)',
                                    '\1\3')) AS ROLES,
               LTRIM(REGEXP_REPLACE(' ' || LISTAGG(ORG_LABEL, ', ') WITHIN
                                    GROUP(ORDER BY ORG_LABEL),
                                    '([^,]*)(,\1)+($|,)',
                                    '\1\3')) AS ORG_NODE_LEVEL,
               REPORT_NAME,
               REPORT_FOLDER_URI,
               STATUS,
               MENUID,
               MENU_SEQ,
               REPORT_SEQ,
               MENUNAME
          FROM (SELECT RE.DB_REPORTID ID,
                       RE.REPORT_DESC,
                       RE.REPORT_TYPE,
                       DMRA.CUST_PROD_ID,
                       RE.REPORT_NAME,
                       RE.REPORT_FOLDER_URI,
                       RE.ACTIVATION_STATUS STATUS,
                       ROLE_NAME,
                       ORG_LABEL,
                       DMRA.DB_MENUID MENUID,
                       DMENU.MENU_SEQ,
                       DMRA.REPORT_SEQ,
                       DMENU.MENU_NAME MENUNAME
                  FROM DASH_REPORTS         RE,
                       DASH_MENU_RPT_ACCESS DMRA,
                       DASH_MENUS           DMENU,
                       ROLE                 R,
                       ORG_TP_STRUCTURE     OTS,
                       CUST_PRODUCT_LINK    CPL
                 WHERE RE.DB_REPORTID = DMRA.DB_REPORTID
                   AND DMENU.DB_MENUID = DMRA.DB_MENUID
                   AND R.ROLEID = DMRA.ROLEID
                   AND OTS.ORG_LEVEL = DMRA.ORG_LEVEL
                   AND CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID
                   AND CPL.CUSTOMERID = P_IN_CUSTOMERID
                   AND RE.DB_REPORTID = P_IN_REPORT_ID
                UNION
                SELECT RE.DB_REPORTID ID,
                       RE.REPORT_DESC,
                       RE.REPORT_TYPE,
                       DMRA.CUST_PROD_ID,
                       RE.REPORT_NAME,
                       RE.REPORT_FOLDER_URI,
                       RE.ACTIVATION_STATUS STATUS,
                       ROLE_NAME,
                       'Education Center' ORG_LABEL,
                       DMRA.DB_MENUID MENUID,
                       DMENU.MENU_SEQ,
                       DMRA.REPORT_SEQ,
                       DMENU.MENU_NAME MENUNAME
                  FROM DASH_REPORTS         RE,
                       DASH_MENU_RPT_ACCESS DMRA,
                       DASH_MENUS           DMENU,
                       ROLE                 R,
                       CUST_PRODUCT_LINK    CPL,
                       PRODUCT              P
                 WHERE RE.DB_REPORTID = DMRA.DB_REPORTID
                   AND DMENU.DB_MENUID = DMRA.DB_MENUID
                   AND R.ROLEID = DMRA.ROLEID
                   AND CPL.CUST_PROD_ID = DMRA.CUST_PROD_ID
                   AND P.PRODUCTID = CPL.PRODUCTID
                   AND CPL.CUSTOMERID = P_IN_CUSTOMERID
                   AND DMRA.ORG_LEVEL = -99
                   AND RE.DB_REPORTID = P_IN_REPORT_ID)
         GROUP BY ID,
                  REPORT_DESC,
                  REPORT_TYPE,
                  REPORT_NAME,
                  REPORT_FOLDER_URI,
                  STATUS,
                  MENUID,
                  MENU_SEQ,
                  REPORT_SEQ,
                  MENUNAME;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_REPORT_LIST;

  /*
  THIS PROCEDURE ADD A NEW REPORT AND RETURS THE NEW REPORT DATA
  */
  PROCEDURE SP_ADD_REPORT(P_IN_REPORT_NAME       IN DASH_REPORTS.REPORT_NAME%TYPE,
                          P_IN_REPORT_DESC       IN DASH_REPORTS.REPORT_DESC%TYPE,
                          P_IN_REPORT_TYPE       IN DASH_REPORTS.REPORT_TYPE%TYPE,
                          P_IN_REPORT_FOLDER_URI IN DASH_REPORTS.REPORT_FOLDER_URI%TYPE,
                          P_IN_ACTIVATION_STATUS IN DASH_REPORTS.ACTIVATION_STATUS%TYPE,
                          P_IN_USER_ROLES        IN VARCHAR2,
                          P_IN_ORG_NODE_LEVELS   IN VARCHAR2,
                          P_IN_DB_MENUID         IN DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE,
                          P_IN_CUST_PROD_IDS     IN VARCHAR2,
                          P_IN_CUSTOMERID        IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                          P_OUT_STATUS_NUMBER    OUT NUMBER,
                          P_OUT_CUR_REPORT_NEW   OUT GET_REFCURSOR,
                          P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  
    V_DB_REPORTID DASH_REPORTS.DB_REPORTID%TYPE := 0;
  
    CURSOR DB_REPORTID_CUR(P_IN_REPORT_NAME VARCHAR2, P_IN_REPORT_FOLDER_URI VARCHAR2) IS
      SELECT DB_REPORTID
        FROM DASH_REPORTS
       WHERE REPORT_NAME = P_IN_REPORT_NAME
         AND REPORT_FOLDER_URI = P_IN_REPORT_FOLDER_URI;
  
    V_DB_REPORTID_CUR DB_REPORTID_CUR%ROWTYPE;
  
    P_OUT_EXCEP_ERR_MSG1 VARCHAR2(100);
  
  BEGIN
  
    P_OUT_STATUS_NUMBER := 0;
  
    OPEN DB_REPORTID_CUR(P_IN_REPORT_NAME, P_IN_REPORT_FOLDER_URI);
    LOOP
      FETCH DB_REPORTID_CUR
        INTO V_DB_REPORTID_CUR;
      EXIT WHEN DB_REPORTID_CUR%NOTFOUND;
      V_DB_REPORTID := V_DB_REPORTID_CUR.DB_REPORTID;
    END LOOP;
    CLOSE DB_REPORTID_CUR;
  
    IF V_DB_REPORTID = 0 THEN
    
      SELECT DB_REPORT_ID_SEQ.NEXTVAL INTO V_DB_REPORTID FROM DUAL;
    
      INSERT INTO DASH_REPORTS
        (DB_REPORTID,
         REPORT_NAME,
         REPORT_DESC,
         REPORT_TYPE,
         REPORT_FOLDER_URI,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (V_DB_REPORTID,
         P_IN_REPORT_NAME,
         P_IN_REPORT_DESC,
         P_IN_REPORT_TYPE,
         P_IN_REPORT_FOLDER_URI,
         P_IN_ACTIVATION_STATUS,
         SYSDATE);
    
      FOR REC_CUST_PROD_ID IN (WITH T AS (SELECT P_IN_CUST_PROD_IDS AS TXT
                                             FROM DUAL)SELECT REGEXP_SUBSTR(TXT,
                                                     '[^,]+',
                                                     1,
                                                     LEVEL) AS CUST_PROD_ID
                                 FROM T
                               CONNECT BY LEVEL <=
                                          LENGTH(REGEXP_REPLACE(TXT,
                                                                '[^,]*')) + 1) LOOP
      
        FOR REC_ROLE IN (WITH T AS (SELECT P_IN_USER_ROLES AS TXT FROM DUAL)SELECT REGEXP_SUBSTR(TXT,
                                               '[^,]+',
                                               1,
                                               LEVEL) AS ROLE_NAME
                           FROM T
                         CONNECT BY LEVEL <=
                                    LENGTH(REGEXP_REPLACE(TXT,
                                                          '[^,]*')) + 1) LOOP
        
          FOR REC_ORG_NODE_LEVEL IN (WITH T AS (SELECT P_IN_ORG_NODE_LEVELS AS TXT
                                                   FROM DUAL)SELECT REGEXP_SUBSTR(TXT,
                                                           '[^,]+',
                                                           1,
                                                           LEVEL) AS ORG_NODE_LEVEL
                                       FROM T
                                     CONNECT BY LEVEL <=
                                                LENGTH(REGEXP_REPLACE(TXT,
                                                                      '[^,]*')) + 1) LOOP
          
            INSERT INTO DASH_MENU_RPT_ACCESS
              (DB_MENUID,
               DB_REPORTID,
               ROLEID,
               ORG_LEVEL,
               CUST_PROD_ID,
               REPORT_SEQ,
               ACTIVATION_STATUS,
               CREATED_DATE_TIME)
            VALUES
              (P_IN_DB_MENUID,
               V_DB_REPORTID,
               (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = REC_ROLE.ROLE_NAME),
               REC_ORG_NODE_LEVEL.ORG_NODE_LEVEL,
               REC_CUST_PROD_ID.CUST_PROD_ID,
               V_DB_REPORTID,
               'AC',
               SYSDATE);
          
          END LOOP;
        END LOOP;
      END LOOP;
    
      SP_GET_REPORT_LIST(P_IN_CUSTOMERID,
                         V_DB_REPORTID,
                         P_OUT_CUR_REPORT_NEW,
                         P_OUT_EXCEP_ERR_MSG1);
      P_OUT_STATUS_NUMBER := 1;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_ADD_REPORT;

  /*
  THIS PROCEDURE EDITS A REPORT
  */
  PROCEDURE SP_EDIT_REPORT(P_IN_DB_REPORTID       DASH_REPORTS.DB_REPORTID%TYPE,
                           P_IN_REPORT_NAME       DASH_REPORTS.REPORT_NAME%TYPE,
                           P_IN_REPORT_DESC       DASH_REPORTS.REPORT_DESC%TYPE,
                           P_IN_REPORT_TYPE       DASH_REPORTS.REPORT_TYPE%TYPE,
                           P_IN_REPORT_FOLDER_URI DASH_REPORTS.REPORT_FOLDER_URI%TYPE,
                           P_IN_ACTIVATION_STATUS DASH_REPORTS.ACTIVATION_STATUS%TYPE,
                           P_IN_USER_ROLES        VARCHAR2,
                           P_IN_ORG_NODE_LEVELS   VARCHAR2,
                           P_IN_DB_MENUID         DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE,
                           P_IN_CUST_PROD_IDS     IN VARCHAR2,
                           P_IN_REPORT_SEQ        DASH_MENU_RPT_ACCESS.REPORT_SEQ%TYPE,
                           P_OUT_STATUS_NUMBER    OUT NUMBER,
                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  
  BEGIN
  
    P_OUT_STATUS_NUMBER := 0;
  
    UPDATE DASH_REPORTS
       SET REPORT_NAME       = P_IN_REPORT_NAME,
           REPORT_DESC       = P_IN_REPORT_DESC,
           REPORT_FOLDER_URI = P_IN_REPORT_FOLDER_URI,
           ACTIVATION_STATUS = P_IN_ACTIVATION_STATUS,
           REPORT_TYPE       = P_IN_REPORT_TYPE,
           UPDATED_DATE_TIME = SYSDATE
     WHERE DB_REPORTID = P_IN_DB_REPORTID;
  
    DELETE FROM DASH_MENU_RPT_ACCESS WHERE DB_REPORTID = P_IN_DB_REPORTID;
  
    FOR REC_CUST_PROD_ID IN (WITH T AS (SELECT P_IN_CUST_PROD_IDS AS TXT
                                           FROM DUAL)SELECT REGEXP_SUBSTR(TXT,
                                                   '[^,]+',
                                                   1,
                                                   LEVEL) AS CUST_PROD_ID
                               FROM T
                             CONNECT BY LEVEL <=
                                        LENGTH(REGEXP_REPLACE(TXT,
                                                              '[^,]*')) + 1) LOOP
    
      FOR REC_ROLE IN (WITH T AS (SELECT P_IN_USER_ROLES AS TXT FROM DUAL)SELECT REGEXP_SUBSTR(TXT,
                                             '[^,]+',
                                             1,
                                             LEVEL) AS ROLE_NAME
                         FROM T
                       CONNECT BY LEVEL <=
                                  LENGTH(REGEXP_REPLACE(TXT,
                                                        '[^,]*')) + 1) LOOP
      
        FOR REC_ORG_NODE_LEVEL IN (WITH T AS (SELECT P_IN_ORG_NODE_LEVELS AS TXT
                                                 FROM DUAL)SELECT NVL(REGEXP_SUBSTR(TXT,
                                                             '[^,]+',
                                                             1,
                                                             LEVEL),
                                               -999) AS ORG_NODE_LEVEL
                                     FROM T
                                   CONNECT BY LEVEL <=
                                              LENGTH(REGEXP_REPLACE(TXT,
                                                                    '[^,]*')) + 1) LOOP
          IF REC_ORG_NODE_LEVEL.ORG_NODE_LEVEL <> -999 THEN
            INSERT INTO DASH_MENU_RPT_ACCESS
              (DB_MENUID,
               DB_REPORTID,
               ROLEID,
               ORG_LEVEL,
               CUST_PROD_ID,
               REPORT_SEQ,
               ACTIVATION_STATUS,
               CREATED_DATE_TIME)
            VALUES
              (P_IN_DB_MENUID,
               P_IN_DB_REPORTID,
               (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = REC_ROLE.ROLE_NAME),
               REC_ORG_NODE_LEVEL.ORG_NODE_LEVEL,
               REC_CUST_PROD_ID.CUST_PROD_ID,
               P_IN_REPORT_SEQ,
               'AC',
               SYSDATE);
          END IF;
        END LOOP;
      END LOOP;
    END LOOP;
  
    P_OUT_STATUS_NUMBER := 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_EDIT_REPORT;

  /*
  THIS PROCEDURE DELETES A REPORT AND ITS CORRESPONDING META DATA
  */
  PROCEDURE SP_DELETE_REPORT(P_IN_DB_REPORTID    DASH_REPORTS.DB_REPORTID%TYPE,
                             P_OUT_STATUS_NUMBER OUT NUMBER,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    DELETE FROM DASH_MENU_RPT_ACCESS WHERE DB_REPORTID = P_IN_DB_REPORTID;
  
    DELETE FROM DASH_MESSAGES DM WHERE DM.DB_REPORTID = P_IN_DB_REPORTID;
  
    DELETE FROM DASH_REPORTS WHERE DB_REPORTID = P_IN_DB_REPORTID;
  
    P_OUT_STATUS_NUMBER := 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_DELETE_REPORT;

  PROCEDURE SP_EDIT_ACTION_DATA(P_IN_REPORTID       IN DASH_REPORTS.DB_REPORTID%TYPE,
                                P_OUT_REPORT_CURSOR OUT GET_REFCURSOR,
                                P_OUT_ACTION_CURSOR OUT GET_REFCURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REPORT_CURSOR FOR
      SELECT DMRA.DB_REPORTID,
             DR.REPORT_NAME,
             DMRA.CUST_PROD_ID,
             P.PRODUCT_NAME,
             DMRA.ROLEID,
             R.ROLE_NAME,
             DMRA.ORG_LEVEL,
             OTS.ORG_LABEL
        FROM DASH_MENU_RPT_ACCESS DMRA,
             DASH_REPORTS DR,
             PRODUCT P,
             CUST_PRODUCT_LINK CPL,
             ROLE R,
             (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
               GROUP(
               ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                        FROM ORG_TP_STRUCTURE
                       ORDER BY ORG_LEVEL) TEMP
               GROUP BY TEMP.ORG_LEVEL
              UNION
              SELECT -99, 'EDUCATION CENTER' FROM DUAL) OTS
       WHERE DMRA.DB_REPORTID = P_IN_REPORTID
         AND DMRA.DB_REPORTID = DR.DB_REPORTID
         AND DMRA.CUST_PROD_ID = CPL.CUST_PROD_ID
         AND CPL.PRODUCTID = P.PRODUCTID
         AND DMRA.ROLEID = R.ROLEID
         AND DMRA.ORG_LEVEL = OTS.ORG_LEVEL
       ORDER BY DMRA.DB_REPORTID,
                DMRA.CUST_PROD_ID,
                DMRA.ROLEID,
                DMRA.ORG_LEVEL;
  
    OPEN P_OUT_ACTION_CURSOR FOR
      SELECT DISTINCT DB_ACTIONID, ACTION_NAME, ACTIVATION_STATUS
        FROM (SELECT DAA.DB_ACTIONID, DRA.ACTION_NAME, DAA.ACTIVATION_STATUS
                FROM DASH_ACTION_ACCESS DAA,
                     DASH_REPORTS DR,
                     DASH_RPT_ACTION DRA,
                     PRODUCT P,
                     CUST_PRODUCT_LINK CPL,
                     ROLE R,
                     (SELECT TEMP.ORG_LEVEL,
                             LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
                       GROUP(
                       ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                        FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                                FROM ORG_TP_STRUCTURE
                               ORDER BY ORG_LEVEL) TEMP
                       GROUP BY TEMP.ORG_LEVEL
                      UNION
                      SELECT -99, 'EDUCATION CENTER' FROM DUAL) OTS
               WHERE DAA.DB_REPORTID = P_IN_REPORTID
                 AND DAA.DB_REPORTID = DR.DB_REPORTID
                 AND DAA.DB_ACTIONID = DRA.DB_ACTIONID
                 AND DAA.CUST_PROD_ID = CPL.CUST_PROD_ID
                 AND DAA.ROLEID = R.ROLEID
                 AND DAA.ORG_LEVEL = OTS.ORG_LEVEL
                 AND CPL.PRODUCTID = P.PRODUCTID
               ORDER BY DAA.DB_REPORTID,
                        DAA.DB_ACTIONID,
                        DAA.CUST_PROD_ID,
                        DAA.ROLEID,
                        DAA.ORG_LEVEL);
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_EDIT_ACTION_DATA;

END PKG_MANAGE_REPORT; --END OF PACKAGE
/
