CREATE OR REPLACE PACKAGE PKG_MANAGE_PARENT AS

  -- Author  : 541841
  -- Created : 6/23/2014 2:36:17 PM
  -- Purpose : Manage Parent

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_GET_PARENT_DETAILS(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                  P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_USERNAME       IN VARCHAR,
                                  P_IN_SEARCH_PARAM   IN VARCHAR,
                                  P_IN_ROLEID         IN USER_ROLE.ROLEID%TYPE,
								  P_IN_MORE_COUNT     IN NUMBER,
                                  P_OUT_CUR           OUT GET_REFCURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_SEARCH_PARENT(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                             P_IN_ROLEID         IN USER_ROLE.ROLEID%TYPE,
                             P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                             P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                             P_IN_SEARCH_PARAM   IN VARCHAR,
                             P_IN_ROW_NUM        IN VARCHAR,
                             P_IN_IS_EXACT       IN VARCHAR,
                             P_OUT_CUR           OUT GET_REFCURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

END PKG_MANAGE_PARENT;
/
CREATE OR REPLACE PACKAGE BODY PKG_MANAGE_PARENT AS

  PROCEDURE SP_GET_PARENT_DETAILS(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                  P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_USERNAME       IN VARCHAR,
                                  P_IN_SEARCH_PARAM   IN VARCHAR,
                                  P_IN_ROLEID         IN USER_ROLE.ROLEID%TYPE,
								                  P_IN_MORE_COUNT     IN NUMBER,
                                  P_OUT_CUR           OUT GET_REFCURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN
    IF P_IN_USERNAME <> '-99' AND P_IN_SEARCH_PARAM <> '-99' THEN
      OPEN P_OUT_CUR FOR
        SELECT ABC.USERID,
               ABC.USERNAME,
               ABC.FULLNAME,
               ABC.STATUS,
               TO_CHAR(ABC.LAST_LOGIN_ATTEMPT, 'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,
               ABC.ORG_NODE_NAME,
               ABC.ORG_NODEID,
               P_IN_ORG_NODE_ID AS TENANTID
          FROM (SELECT USR.USERID,
                       USR.USERNAME,
                       USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                       USR.ACTIVATION_STATUS AS STATUS,
                       USR.LAST_LOGIN_ATTEMPT,
                       HIER.ORG_NODE_NAME,
                       HIER.ORG_NODEID
                  FROM USERS USR,
                       USER_ROLE UR,
                       ORG_USERS OU,
                       (SELECT ORG_NODE_NAME,ORG_NODEID
                          FROM ORG_NODE_DIM
                         WHERE ORG_MODE = P_IN_ORG_MODE
                         START WITH ORG_NODEID = P_IN_ORG_NODE_ID
                        CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER
                 WHERE USR.USERID = OU.USERID
                   and OU.ORG_NODEID = HIER.ORG_NODEID
                   AND USR.USERID = UR.USERID
                   AND UR.ROLEID = P_IN_ROLEID
                   AND OU.USERID =  UR.USERID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = HIER.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                   AND UPPER(USR.USERNAME) > UPPER(P_IN_USERNAME)
                   AND (UPPER(USR.USERNAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
                       UPPER(USR.LAST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
                       UPPER(USR.FIRST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM))
                 ORDER BY UPPER(USR.USERNAME)) ABC
         WHERE ROWNUM <= P_IN_MORE_COUNT
         ORDER BY UPPER(ABC.USERNAME);

    ELSIF P_IN_USERNAME <> '-99' AND P_IN_SEARCH_PARAM = '-99' THEN
      OPEN P_OUT_CUR FOR
        SELECT ABC.USERID,
               ABC.USERNAME,
               ABC.FULLNAME,
               ABC.STATUS,
               TO_CHAR(ABC.LAST_LOGIN_ATTEMPT, 'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,
               ABC.ORG_NODE_NAME,
               ABC.ORG_NODEID,
               P_IN_ORG_NODE_ID AS TENANTID
          FROM (SELECT USR.USERID,
                       USR.USERNAME,
                       USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                       USR.ACTIVATION_STATUS AS STATUS,
                       USR.LAST_LOGIN_ATTEMPT,
                       HIER.ORG_NODE_NAME,
                       HIER.ORG_NODEID
                  FROM USERS USR,
                       USER_ROLE UR,
                       ORG_USERS OU,
                       (SELECT ORG_NODE_NAME,ORG_NODEID
                          FROM ORG_NODE_DIM
                         WHERE ORG_MODE = P_IN_ORG_MODE
                         START WITH ORG_NODEID = P_IN_ORG_NODE_ID
                        CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER
                 WHERE USR.USERID = OU.USERID
                   and OU.ORG_NODEID = HIER.ORG_NODEID
                   AND USR.USERID = UR.USERID
                   AND OU.USERID =  UR.USERID
                   AND UR.ROLEID = P_IN_ROLEID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = HIER.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                   AND UPPER(USR.USERNAME) > UPPER(P_IN_USERNAME)
                 ORDER BY UPPER(USR.USERNAME)) ABC
         WHERE ROWNUM <= P_IN_MORE_COUNT
         ORDER BY UPPER(ABC.USERNAME);

    ELSIF P_IN_USERNAME = '-99' AND P_IN_SEARCH_PARAM = '-99' THEN
      OPEN P_OUT_CUR FOR
        /*SELECT ABC.USERID,
               ABC.USERNAME,
               ABC.FULLNAME,
               ABC.STATUS,
               TO_CHAR(ABC.LAST_LOGIN_ATTEMPT, 'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,
               ABC.ORG_NODE_NAME,
               ABC.ORG_NODEID,
               P_IN_ORG_NODE_ID AS TENANTID
          FROM (SELECT USR.USERID,
                       USR.USERNAME,
                       USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                       USR.ACTIVATION_STATUS AS STATUS,
                       USR.LAST_LOGIN_ATTEMPT,
                       HIER.ORG_NODE_NAME,
                       HIER.ORG_NODEID
                  FROM USERS USR,
                       USER_ROLE UR,
                       ORG_USERS OU,
                       (SELECT *
                          FROM ORG_NODE_DIM
                         WHERE ORG_MODE = P_IN_ORG_MODE
                         START WITH ORG_NODEID = P_IN_ORG_NODE_ID
                        CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER
                 WHERE USR.USERID = OU.USERID
                   and OU.ORG_NODEID = HIER.ORG_NODEID
                   AND USR.USERID = UR.USERID
                   AND UR.ROLEID = P_IN_ROLEID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = HIER.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                 ORDER BY UPPER(USR.USERNAME)) ABC
         WHERE ROWNUM <= P_IN_MORE_COUNT
         ORDER BY UPPER(ABC.USERNAME);*/
         
          SELECT ABC.USERID,
               ABC.USERNAME,
               ABC.FULLNAME,
               ABC.STATUS,
               TO_CHAR(ABC.LAST_LOGIN_ATTEMPT, 'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,
               ABC.ORG_NODE_NAME,
               ABC.ORG_NODEID,
               P_IN_ORG_NODE_ID AS TENANTID
          FROM (SELECT USR.USERID,
                       USR.USERNAME,
                       USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                       USR.ACTIVATION_STATUS AS STATUS,
                       USR.LAST_LOGIN_ATTEMPT,
                       HIER.ORG_NODE_NAME,
                       HIER.ORG_NODEID
                  FROM USERS USR,
                       USER_ROLE UR,
                       ORG_USERS OU,
                       (SELECT ORG_NODE_NAME,ORG_NODEID
                          FROM ORG_NODE_DIM
                         WHERE ORG_MODE = P_IN_ORG_MODE
                         START WITH ORG_NODEID = P_IN_ORG_NODE_ID
                        CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER
                 WHERE USR.USERID = OU.USERID
                   and OU.ORG_NODEID = HIER.ORG_NODEID
                   AND USR.USERID = UR.USERID
                   AND OU.USERID =  UR.USERID
                   AND UR.ROLEID = P_IN_ROLEID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = HIER.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                 ORDER BY UPPER(USR.USERNAME)) ABC
         WHERE ROWNUM <= P_IN_MORE_COUNT
         ORDER BY UPPER(ABC.USERNAME);

    END IF;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_GET_PARENT_DETAILS;

  PROCEDURE SP_SEARCH_PARENT(P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                             P_IN_ROLEID         IN USER_ROLE.ROLEID%TYPE,
                             P_IN_ORG_MODE       IN ORG_NODE_DIM.ORG_MODE%TYPE,
                             P_IN_ORG_NODE_ID    IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                             P_IN_SEARCH_PARAM   IN VARCHAR,
                             P_IN_ROW_NUM        IN VARCHAR,
                             P_IN_IS_EXACT       IN VARCHAR,
                             P_OUT_CUR           OUT GET_REFCURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN
    IF P_IN_IS_EXACT = 'N' THEN

      OPEN P_OUT_CUR FOR
        SELECT ABC.USERID,
               ABC.USERNAME,
               ABC.FULLNAME,
               ABC.STATUS,
               ABC.LAST_NAME,
               ABC.FIRST_NAME,
               TO_CHAR(ABC.LAST_LOGIN_ATTEMPT, 'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,
               ABC.ORG_NODE_NAME,
               ABC.ORG_NODEID,
               P_IN_ORG_NODE_ID AS TENANTID
          FROM (SELECT USR.USERID,
                       USR.USERNAME,
                       USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                       USR.LAST_NAME,
                       USR.FIRST_NAME,
                       USR.ACTIVATION_STATUS AS STATUS,
                       USR.LAST_LOGIN_ATTEMPT,
                       HIER.ORG_NODE_NAME,
                       HIER.ORG_NODEID
                  FROM USERS USR,
                       USER_ROLE UR,
                       ORG_USERS OU,
                       (SELECT ORG_NODE_NAME,ORG_NODEID
                          FROM ORG_NODE_DIM
                         WHERE ORG_MODE = P_IN_ORG_MODE
                         START WITH ORG_NODEID = P_IN_ORG_NODE_ID
                        CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER
                 WHERE USR.USERID = OU.USERID
                   and OU.ORG_NODEID = HIER.ORG_NODEID
                   AND USR.USERID = UR.USERID
                   AND OU.USERID = UR.USERID
                   AND UR.ROLEID = P_IN_ROLEID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = HIER.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                   AND (UPPER(USR.USERNAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
                       UPPER(USR.LAST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
                       UPPER(USR.FIRST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM))
                 ORDER BY UPPER(USR.USERNAME)) ABC
         WHERE ROWNUM <= P_IN_ROW_NUM;

    ELSE

      OPEN P_OUT_CUR FOR
        SELECT ABC.USERID,
               ABC.USERNAME,
               ABC.FULLNAME,
               ABC.STATUS,
               ABC.LAST_NAME,
               ABC.FIRST_NAME,
               TO_CHAR(ABC.LAST_LOGIN_ATTEMPT, 'MM/DD/YY') AS LAST_LOGIN_ATTEMPT,
               ABC.ORG_NODE_NAME,
               ABC.ORG_NODEID,
               P_IN_ORG_NODE_ID AS TENANTID
          FROM (SELECT USR.USERID,
                       USR.USERNAME,
                       USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                       USR.LAST_NAME,
                       USR.FIRST_NAME,
                       USR.ACTIVATION_STATUS AS STATUS,
                       USR.LAST_LOGIN_ATTEMPT,
                       HIER.ORG_NODE_NAME,
                       HIER.ORG_NODEID
                  FROM USERS USR,
                       USER_ROLE UR,
                       ORG_USERS OU,
                       (SELECT ORG_NODE_NAME,ORG_NODEID
                          FROM ORG_NODE_DIM
                         WHERE ORG_MODE = P_IN_ORG_MODE
                         START WITH ORG_NODEID = P_IN_ORG_NODE_ID
                        CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID) HIER
                 WHERE USR.USERID = OU.USERID
                   and OU.ORG_NODEID = HIER.ORG_NODEID
                   AND USR.USERID = UR.USERID
                   AND OU.USERID = UR.USERID
                   AND UR.ROLEID = P_IN_ROLEID
                   AND EXISTS
                 (SELECT 1
                          FROM ORG_PRODUCT_LINK OPL
                         WHERE OPL.ORG_NODEID = HIER.ORG_NODEID
                           AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
                   AND UPPER(USR.USERNAME) LIKE UPPER(P_IN_SEARCH_PARAM)
                 ORDER BY UPPER(USR.USERNAME)) ABC
         WHERE ROWNUM <= P_IN_ROW_NUM;

    END IF;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 1, 255));
  END SP_SEARCH_PARENT;

END PKG_MANAGE_PARENT;
/
