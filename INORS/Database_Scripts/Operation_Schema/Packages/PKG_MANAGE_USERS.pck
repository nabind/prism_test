CREATE OR REPLACE PACKAGE PKG_MANAGE_USERS IS

  -- AUTHOR  : AMITABHA ROY
  -- CREATED : 6/23/2014 3:06:11 PM
  -- PURPOSE : MANAGE USERS

  TYPE GET_REF_CURSOR IS REF CURSOR;

  PROCEDURE SP_GET_USERS_ONSCROLL_WITH_SP(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                          P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                          P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                          P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                          P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                          P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                          P_IN_SEARCH_PARAM   IN VARCHAR2,
										                      P_IN_MORE_COUNT     IN NUMBER,
                                          P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                          P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_ONSCROLL(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                  P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                  P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                  P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
								                  P_IN_MORE_COUNT     IN NUMBER,
                                  P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_ON_FIRST_LOAD(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                       P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                       P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
									                     P_IN_MORE_COUNT     IN NUMBER,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USER_ROLE(P_IN_USER_ID        IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_EDU_USER_ROLE(P_IN_USER_ID        IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_SEARCH(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                P_IN_SEARCH_PARAM   IN VARCHAR2,
								                P_IN_MORE_COUNT     IN NUMBER,
                                P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_SEARCH_EXACT(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                      P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                      P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                      P_IN_SEARCH_PARAM   IN VARCHAR2,
									                    P_IN_MORE_COUNT IN NUMBER,
                                      P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USER_RESET_PASSWORD(P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USER_PWD_HINT_LIST(P_IN_USERID         IN USERS.USERID%TYPE,
                                      P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_VALIDATE_USERNAME(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                 P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                 P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_ROLE_ADD(P_IN_ROLE            IN VARCHAR2,
                            P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                            P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_ROLE_USER(P_IN_ROLE            IN VARCHAR2,
                             P_IN_USERID          IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR     OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

  PROCEDURE SP_GET_USER_EMAIL(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                             P_OUT_REF_CURSOR     OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);

  PROCEDURE SP_RESET_PASSWORD(P_IN_USERNAME                    IN USERS.USERNAME%TYPE,
                             P_IN_PASSWORD                    IN USERS.PASSWORD%TYPE,
                             P_IN_SALT                        IN USERS.SALT%TYPE,
                             P_IN_IS_FIRSTTIME_LOGIN          IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                             P_OUT_EXCEP_ERR_MSG              OUT VARCHAR2);

  PROCEDURE SP_GET_ROLE_DETAILS(P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                 P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);


  PROCEDURE SP_GET_USER_DETAILS_ON_EDIT (P_IN_USERID          IN USERS.USERID%TYPE,
                                         P_OUT_USER_REF_CURSOR     OUT GET_REF_CURSOR,
                                         P_OUT_ROLES_REF_CURSOR    OUT GET_REF_CURSOR,
                                         P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2 );

END PKG_MANAGE_USERS;
/
CREATE OR REPLACE PACKAGE BODY PKG_MANAGE_USERS IS

  -- GET_USER_DETAILS_ON_SCROLL_WITH_SRCH_PARAM
  PROCEDURE SP_GET_USERS_ONSCROLL_WITH_SP(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                          P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                          P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                          P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                          P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                          P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                          P_IN_SEARCH_PARAM   IN VARCHAR2,
										                      P_IN_MORE_COUNT     IN NUMBER,
                                          P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                          P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT ABC.USERROWID,
             ABC.USERID AS USER_ID,
             ABC.USERNAME,
             ABC.FULLNAME,
             ABC.STATUS,
             ABC.ORG_NODE_NAME AS ORG_NAME,
             ABC.ORG_NODEID AS ORG_ID,
             ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID
        FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID,
                     USR.USERID,
                     USR.USERNAME,
                     USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                     USR.ACTIVATION_STATUS AS STATUS,
                     HIER.ORG_NODE_NAME,
                     HIER.ORG_NODEID,
                     HIER.PARENT_ORG_NODEID
                FROM USERS USR,
                     ORG_USERS ORGUSER,
                     (SELECT ORG_NODE_NAME,ORG_NODEID,PARENT_ORG_NODEID
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_MODE = P_IN_ORGMODE
                       START WITH ORG_NODEID = P_IN_TENANT_ID
                      CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
                      UNION
                      SELECT ORG_NODE_NAME,ORG_NODEID,PARENT_ORG_NODEID
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_NODEID = P_IN_TENANT_ID
                         AND ORG_NODE_LEVEL = 1) HIER,
                     ORG_PRODUCT_LINK OLP
               WHERE ORGUSER.USERID = USR.USERID
                 AND HIER.ORG_NODEID = ORGUSER.ORG_NODEID
                 AND ORGUSER.ORG_NODE_LEVEL <> 0
                 AND USR.ACTIVATION_STATUS != 'SS'
                 AND NOT EXISTS
               (SELECT 1
                        FROM USER_ROLE
                       WHERE USER_ROLE.ROLEID = P_IN_ROLE_ID
                         AND USER_ROLE.USERID = USR.USERID)
                 AND ORGUSER.ORG_NODEID = OLP.ORG_NODEID
                 AND OLP.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND UPPER(USR.USERNAME) > UPPER(P_IN_USER_NAME)
                 AND (UPPER(USR.USERNAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
                     UPPER(USR.LAST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
                     UPPER(USR.FIRST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM))
               ORDER BY UPPER(USR.USERNAME)) ABC
       WHERE ROWNUM <= P_IN_MORE_COUNT
       ORDER BY UPPER(ABC.USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USERS_ONSCROLL_WITH_SP;

  -- GET_USER_DETAILS_ON_SCROLL
  PROCEDURE SP_GET_USERS_ONSCROLL(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                  P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                  P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                  P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
								                  P_IN_MORE_COUNT     IN NUMBER,
                                  P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT ABC.USERROWID,
             ABC.USERID AS USER_ID,
             ABC.USERNAME,
             ABC.FULLNAME,
             ABC.STATUS,
             ABC.ORG_NODE_NAME AS ORG_NAME,
             ABC.ORG_NODEID AS ORG_ID,
             ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID
        FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID,
                     USR.USERID,
                     USR.USERNAME,
                     USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                     USR.ACTIVATION_STATUS AS STATUS,
                     HIER.ORG_NODE_NAME,
                     HIER.ORG_NODEID,
                     HIER.PARENT_ORG_NODEID
                FROM USERS USR,
                     ORG_USERS ORGUSER,
                     (SELECT ORG_NODE_NAME,ORG_NODEID,PARENT_ORG_NODEID
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_MODE = P_IN_ORGMODE
                       START WITH ORG_NODEID = P_IN_TENANT_ID
                      CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
                      UNION
                      SELECT ORG_NODE_NAME,ORG_NODEID,PARENT_ORG_NODEID
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_NODEID = P_IN_TENANT_ID
                         AND ORG_NODE_LEVEL = 1) HIER,
                     ORG_PRODUCT_LINK OLP
               WHERE ORGUSER.USERID = USR.USERID
                 AND HIER.ORG_NODEID = ORGUSER.ORG_NODEID
                 AND ORGUSER.ORG_NODE_LEVEL <> 0
                 AND USR.ACTIVATION_STATUS != 'SS'
                 AND NOT EXISTS
               (SELECT 1
                        FROM USER_ROLE
                       WHERE USER_ROLE.ROLEID = P_IN_ROLE_ID
                         AND USER_ROLE.USERID = USR.USERID)
                 AND ORGUSER.ORG_NODEID = OLP.ORG_NODEID
                 AND OLP.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND UPPER(USR.USERNAME) > UPPER(P_IN_USER_NAME)
               ORDER BY UPPER(USR.USERNAME)) ABC
       WHERE ROWNUM <= P_IN_MORE_COUNT
       ORDER BY UPPER(ABC.USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USERS_ONSCROLL;

  -- GET_USER_DETAILS_ON_FIRST_LOAD
  PROCEDURE SP_GET_USERS_ON_FIRST_LOAD(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                       P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                       P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
									                     P_IN_MORE_COUNT     IN NUMBER,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT ABC.USERROWID,
             ABC.USERID AS USER_ID,
             ABC.USERNAME,
             ABC.FULLNAME,
             ABC.STATUS,
             ABC.ORG_NODE_NAME AS ORG_NAME,
             ABC.ORG_NODEID AS ORG_ID,
             ABC.PARENT_ORG_NODEID AS ORG_PARENT_ID
        FROM (SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID,
                     ROWNUM RECRD_CNT,
                     USR.USERID,
                     USR.USERNAME,
                     USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
                     USR.ACTIVATION_STATUS AS STATUS,
                     HIER.ORG_NODE_NAME,
                     HIER.ORG_NODEID,
                     HIER.PARENT_ORG_NODEID
                FROM USERS USR,
                     ORG_USERS ORGUSER,
                     (SELECT ORG_NODE_NAME, ORG_NODEID, PARENT_ORG_NODEID
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_MODE = P_IN_ORGMODE
                       START WITH ORG_NODEID = P_IN_TENANT_ID
                      CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
                      UNION
                      SELECT ORG_NODE_NAME, ORG_NODEID, PARENT_ORG_NODEID
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_NODEID = P_IN_TENANT_ID
                         AND ORG_NODE_LEVEL = 1) HIER,
                     ORG_PRODUCT_LINK OLP
               WHERE ORGUSER.USERID = USR.USERID
                 AND HIER.ORG_NODEID = ORGUSER.ORG_NODEID
                 AND ORGUSER.ORG_NODE_LEVEL <> 0
                 AND ORGUSER.ORG_NODEID = OLP.ORG_NODEID
                 AND OLP.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND USR.ACTIVATION_STATUS != 'SS'
                 AND NOT EXISTS
               (SELECT 1
                        FROM USER_ROLE
                       WHERE USER_ROLE.ROLEID = P_IN_ROLE_ID
                         AND USER_ROLE.USERID = USR.USERID)
               ORDER BY UPPER(USR.USERNAME)) ABC
       WHERE ROWNUM <= P_IN_MORE_COUNT
       ORDER BY UPPER(ABC.USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USERS_ON_FIRST_LOAD;

  -- GET_USER_ROLE
  PROCEDURE SP_GET_USER_ROLE(P_IN_USER_ID        IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
     /* SELECT DISTINCT RLE.ROLEID,
                      RLE.ROLE_NAME,
                      OTS.ORG_LABEL,
                      RLE.DESCRIPTION
        FROM USER_ROLE URLE,
             ROLE RLE,
             USERS USR,
             ORG_USERS OU,
             (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
               GROUP(
               ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                        FROM ORG_TP_STRUCTURE
                       ORDER BY ORG_LEVEL) TEMP
               GROUP BY TEMP.ORG_LEVEL) OTS
       WHERE USR.USERID = P_IN_USER_ID
         AND URLE.USERID = USR.USERID
         AND RLE.ROLEID = URLE.ROLEID
         AND USR.USERID = OU.USERID
         AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
       ORDER BY ROLEID;*/
       
       
       SELECT DISTINCT RLE.ROLEID,
                      RLE.ROLE_NAME,
                      OTS.ORG_LABEL,
                      RLE.DESCRIPTION
        FROM USER_ROLE URLE,
             ROLE RLE,
             USERS USR,
             ORG_USERS OU,
             (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
               GROUP(
               ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                        FROM MV_ORG_TP_STRUCTURE
                       ORDER BY ORG_LEVEL) TEMP
               GROUP BY TEMP.ORG_LEVEL) OTS
         WHERE  USR.USERID = P_IN_USER_ID
         AND URLE.USERID = USR.USERID
         AND URLE.USERID = P_IN_USER_ID
         AND RLE.ROLEID = URLE.ROLEID
         AND OU.USERID  = P_IN_USER_ID
         AND USR.USERID = OU.USERID
         AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
       ORDER BY ROLEID;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USER_ROLE;

  PROCEDURE SP_GET_EDU_USER_ROLE(P_IN_USER_ID        IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
	  SELECT R.ROLEID, R.ROLE_NAME, R.DESCRIPTION FROM USER_ROLE UR, ROLE R WHERE UR.ROLEID = R.ROLEID AND UR.USERID = P_IN_USER_ID
      ORDER BY R.ROLEID;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_EDU_USER_ROLE;

  --SEARCH_USER
  PROCEDURE SP_GET_USERS_SEARCH(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                P_IN_SEARCH_PARAM   IN VARCHAR2,
								                P_IN_MORE_COUNT     IN NUMBER,
                                P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID,
             USR.USERID AS USER_ID,
             USR.USERNAME,
             USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
             USR.LAST_NAME,
             USR.FIRST_NAME,
             USR.ACTIVATION_STATUS AS STATUS,
             HIER.ORG_NODE_NAME AS ORG_NAME,
             HIER.ORG_NODEID AS ORG_ID,
             HIER.PARENT_ORG_NODEID AS ORG_PARENT_ID
        FROM USERS USR,
             ORG_USERS ORGUSERS,
             (SELECT ORG_NODE_NAME, ORG_NODEID, PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_MODE = P_IN_ORGMODE
               START WITH ORG_NODEID = P_IN_TENANT_ID
              CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
              UNION
              SELECT ORG_NODE_NAME, ORG_NODEID, PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODEID = P_IN_TENANT_ID
                 AND ORG_NODE_LEVEL = 1) HIER,
             ORG_PRODUCT_LINK OLP
       WHERE (UPPER(USR.USERNAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
             UPPER(USR.LAST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM) OR
             UPPER(USR.FIRST_NAME) LIKE UPPER(P_IN_SEARCH_PARAM))
         AND ORGUSERS.ORG_NODE_LEVEL <> 0
         AND NOT EXISTS (SELECT 1
                FROM USER_ROLE
               WHERE USER_ROLE.ROLEID = P_IN_ROLE_ID
                 AND USER_ROLE.USERID = USR.USERID)
         AND USR.ACTIVATION_STATUS != 'SS'
         AND ORGUSERS.ORG_NODEID = OLP.ORG_NODEID
         AND OLP.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND ORGUSERS.ORG_NODEID = HIER.ORG_NODEID
         AND ORGUSERS.USERID = USR.USERID
         AND ROWNUM <= P_IN_MORE_COUNT
       ORDER BY UPPER(USR.USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USERS_SEARCH;

  --SEARCH_USER_EXACT
  PROCEDURE SP_GET_USERS_SEARCH_EXACT(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                      P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                      P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                      P_IN_SEARCH_PARAM   IN VARCHAR2,
									                    P_IN_MORE_COUNT IN NUMBER,
                                      P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT ROWIDTOCHAR(USR.ROWID) AS USERROWID,
             USR.USERID AS USER_ID,
             USR.USERNAME,
             USR.LAST_NAME || ' ' || USR.FIRST_NAME AS FULLNAME,
             USR.LAST_NAME,
             USR.FIRST_NAME,
             USR.ACTIVATION_STATUS AS STATUS,
             HIER.ORG_NODE_NAME AS ORG_NAME,
             HIER.ORG_NODEID AS ORG_ID,
             HIER.PARENT_ORG_NODEID AS ORG_PARENT_ID
        FROM USERS USR,
             ORG_USERS ORGUSERS,
             (SELECT ORG_NODE_NAME, ORG_NODEID, PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_MODE = P_IN_ORGMODE
               START WITH ORG_NODEID = P_IN_TENANT_ID
              CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
              UNION
              SELECT ORG_NODE_NAME, ORG_NODEID, PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODEID = P_IN_TENANT_ID
                 AND ORG_NODE_LEVEL = 1) HIER,
             ORG_PRODUCT_LINK OLP
       WHERE UPPER(USR.USERNAME) LIKE UPPER(P_IN_SEARCH_PARAM)
         AND ORGUSERS.ORG_NODE_LEVEL <> 0
         AND NOT EXISTS (SELECT 1
                FROM USER_ROLE
               WHERE USER_ROLE.ROLEID = P_IN_ROLE_ID
                 AND USER_ROLE.USERID = USR.USERID)
         AND USR.ACTIVATION_STATUS != 'SS'
         AND ORGUSERS.ORG_NODEID = OLP.ORG_NODEID
         AND OLP.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND ORGUSERS.ORG_NODEID = HIER.ORG_NODEID
         AND ORGUSERS.USERID = USR.USERID
         AND ROWNUM <= P_IN_MORE_COUNT
       ORDER BY UPPER(USR.USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USERS_SEARCH_EXACT;

  -- SP_GET_USER_RESET_PASSWORD
  PROCEDURE SP_GET_USER_RESET_PASSWORD(P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
		    SELECT U.USERID,
             U.USERNAME,
             U.DISPLAY_USERNAME,
             U.FIRST_NAME,
             U.MIDDLE_NAME,
             U.LAST_NAME,
             U.EMAIL_ADDRESS,
             U.PHONE_NO,
             U.STREET,
             U.COUNTRY,
             U.CITY,
             U.ZIPCODE,
             U.STATE,
             U.ACTIVATION_STATUS
        FROM USERS U
       WHERE UPPER(U.USERNAME) = UPPER(P_IN_USER_NAME)
         AND NOT EXISTS (SELECT 1
                FROM USER_ROLE
               WHERE USER_ROLE.ROLEID IN (2,7)
                 AND USER_ROLE.USERID = U.USERID)
         AND (U.User_Type  in ('GRW_P','GRW') OR U.ACTIVATION_STATUS <> 'SS');

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USER_RESET_PASSWORD;

  -- SP_GET_USER_PWD_HINT_LIST
  PROCEDURE SP_GET_USER_PWD_HINT_LIST(P_IN_USERID         IN USERS.USERID%TYPE,
                                      P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT U.USERID,
             Q.PH_QUESTIONID,
             Q.QUESTION_VALUE,
             Q.QUESTION_SEQ,
             Q.ACTIVATION_STATUS,
             A.PH_ANSWERID,
             A.ANSWER_VALUE
        FROM PWD_HINT_QUESTIONS Q, PWD_HINT_ANSWERS A, USERS U
       WHERE U.USERID = P_IN_USERID
         AND U.USERID = A.USERID
         AND A.PH_QUESTIONID = Q.PH_QUESTIONID
       /*ORDER BY Q.QUESTION_SEQ*/;--Commented out as  index created for this column woth sort

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_GET_USER_PWD_HINT_LIST;

  PROCEDURE SP_VALIDATE_USERNAME(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                 P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                 P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT USR.USERNAME AS USERNAME
        FROM USERS USR
       WHERE UPPER(USR.USERNAME) = UPPER(P_IN_USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_VALIDATE_USERNAME;


 PROCEDURE SP_GET_ROLE_ADD( P_IN_ROLE            IN VARCHAR2,
                            P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                            P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
     SELECT RE.ROLEID      AS ROLEID,
            RE.ROLE_NAME   AS ROLE_NAME,
            RE.DESCRIPTION DESCRIPTION
       FROM ROLE RE
      WHERE RE.ROLE_NAME NOT in (WITH T AS (SELECT P_IN_ROLE AS TXT FROM DUAL)
       SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_NAME
         FROM T
       CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_ROLE_ADD;


PROCEDURE SP_GET_ROLE_USER(  P_IN_ROLE            IN VARCHAR2,
                             P_IN_USERID          IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR     OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
       /*SELECT RE.ROLEID AS ROLEID,
              RE.ROLE_NAME AS ROLE_NAME,
              OTS.ORG_LABEL || ' ' || RE.DESCRIPTION AS DESCRIPTION
         FROM ROLE RE,
              USER_ROLE UR,
              USERS U,
              ORG_USERS OU,
              (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
                GROUP(
                ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                 FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                         FROM ORG_TP_STRUCTURE
                        ORDER BY ORG_LEVEL) TEMP
                GROUP BY TEMP.ORG_LEVEL) OTS
        WHERE UR.USERID = P_IN_USERID
          AND U.USERID = P_IN_USERID
          AND OU.USERID = P_IN_USERID
          AND UR.ROLEID = RE.ROLEID
          AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
          AND RE.ROLE_NAME NOT IN (WITH T AS (SELECT P_IN_ROLE AS TXT
                                                FROM DUAL)
         SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_NAME
           FROM T
         CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
          ORDER BY RE.ROLEID;*/
          
          SELECT RE.ROLEID AS ROLEID,
              RE.ROLE_NAME AS ROLE_NAME,
              OTS.ORG_LABEL || ' ' || RE.DESCRIPTION AS DESCRIPTION
         FROM ROLE RE,
              USER_ROLE UR,
              USERS U,
              ORG_USERS OU,
              (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
                GROUP(
                ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                 FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                         FROM MV_ORG_TP_STRUCTURE
                        ORDER BY ORG_LEVEL) TEMP
                GROUP BY TEMP.ORG_LEVEL) OTS
        WHERE UR.USERID = P_IN_USERID
          AND U.USERID = P_IN_USERID
          AND U.USERID = UR.USERID
          AND OU.USERID = P_IN_USERID
          AND OU.USERID = UR.USERID
          AND UR.ROLEID = RE.ROLEID
          AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
          AND RE.ROLE_NAME NOT IN (WITH T AS (SELECT P_IN_ROLE AS TXT
                                                FROM DUAL)
         SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS ROLE_NAME
           FROM T
         CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1)
          ORDER BY RE.ROLEID;


  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_ROLE_USER;

  PROCEDURE SP_GET_USER_EMAIL(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                              P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                              P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT EMAIL_ADDRESS EMAIL,SALT
        FROM USERS
       WHERE UPPER(USERNAME) = UPPER(P_IN_USERNAME);

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_USER_EMAIL;


   PROCEDURE SP_RESET_PASSWORD(P_IN_USERNAME                    IN USERS.USERNAME%TYPE,
                               P_IN_PASSWORD                    IN USERS.PASSWORD%TYPE,
                               P_IN_SALT                        IN USERS.SALT%TYPE,
                               P_IN_IS_FIRSTTIME_LOGIN          IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                               P_OUT_EXCEP_ERR_MSG              OUT VARCHAR2) IS
   BEGIN

      UPDATE USERS
         SET IS_FIRSTTIME_LOGIN = P_IN_IS_FIRSTTIME_LOGIN,
             PASSWORD           = P_IN_PASSWORD,
             SALT               = P_IN_SALT
       WHERE UPPER(USERNAME) = UPPER(P_IN_USERNAME);

      INSERT INTO PASSWORD_HISTORY
        (PWD_HISTORYID,
         PASSWORD,
         USERID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
      VALUES
        (SEQ_PASSWORD_HISTORY.NEXTVAL,
         P_IN_PASSWORD,
         (SELECT USERID
            FROM USERS
           WHERE UPPER(USERNAME) = UPPER(P_IN_USERNAME)),
         SYSDATE,
         SYSDATE);

   EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

   END SP_RESET_PASSWORD;

  PROCEDURE SP_GET_ROLE_DETAILS(P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                 P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  BEGIN

    OPEN P_OUT_REF_CURSOR FOR
      SELECT ROLEID ROLE_ID, ROLE_NAME, DESCRIPTION
        FROM ROLE
       ORDER BY ROLE_NAME;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_ROLE_DETAILS;


  PROCEDURE SP_GET_USER_DETAILS_ON_EDIT (P_IN_USERID          IN USERS.USERID%TYPE,
                                         P_OUT_USER_REF_CURSOR     OUT GET_REF_CURSOR,
                                         P_OUT_ROLES_REF_CURSOR    OUT GET_REF_CURSOR,
                                         P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2 ) IS
  BEGIN
    OPEN P_OUT_USER_REF_CURSOR FOR
      SELECT USR.USERID AS ID,
             USR.DISPLAY_USERNAME AS USERNAME,
             USR.USERNAME AS USERID,
             NVL(USR.EMAIL_ADDRESS, '') AS EMAIL,
             USR.ACTIVATION_STATUS AS STATUS
        FROM USERS USR
       WHERE USR.USERID = P_IN_USERID;

    OPEN P_OUT_ROLES_REF_CURSOR FOR
       SELECT DISTINCT RLE.ROLEID AS ROLE_ID,
                       RLE.ROLE_NAME AS ROLENAME,
                       OTS.ORG_LABEL || ' ' || RLE.DESCRIPTION AS DESCRIPTION
         FROM ROLE RLE,
              USER_ROLE URLE,
               ORG_USERS OU,
              (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
                GROUP(
                ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                 FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                         FROM ORG_TP_STRUCTURE
                        ORDER BY ORG_LEVEL) TEMP
                GROUP BY TEMP.ORG_LEVEL) OTS
        WHERE URLE.ROLEID = RLE.ROLEID
        AND URLE.USERID =  OU.USERID
          AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
             AND OU.ORG_NODE_LEVEL =
              (SELECT ORG_NODE_LEVEL FROM ORG_USERS WHERE USERID = P_IN_USERID)
          AND OTS.ORG_LEVEL =
              (SELECT ORG_NODE_LEVEL FROM ORG_USERS WHERE USERID = P_IN_USERID)
          AND RLE.ROLE_NAME NOT IN ('ROLE_CTB', 'ROLE_PARENT', 'ROLE_SUPER')
        ORDER BY RLE.ROLEID;
        
        -- THE FOLLOWING TUNED QUERY IS CHANGING THE FUNCTIONALITY 
        /*SELECT DISTINCT RLE.ROLEID AS ROLE_ID,
                       RLE.ROLE_NAME AS ROLENAME,
                       OTS.ORG_LABEL || ' ' || RLE.DESCRIPTION AS DESCRIPTION
         FROM ROLE RLE,
              USER_ROLE URLE,
               ORG_USERS OU,
              (SELECT TEMP.ORG_LEVEL, LISTAGG(TEMP.ORG_LABEL, '/') WITHIN
                GROUP(
                ORDER BY TEMP.ORG_LEVEL) AS ORG_LABEL
                 FROM (SELECT DISTINCT ORG_LEVEL, ORG_LABEL
                         FROM ORG_TP_STRUCTURE
                        ORDER BY ORG_LEVEL) TEMP
                GROUP BY TEMP.ORG_LEVEL) OTS
        WHERE URLE.ROLEID = RLE.ROLEID
        AND URLE.USERID =  OU.USERID
          AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
             AND OU.USERID  = P_IN_USERID       
             AND URLE.USERID =   P_IN_USERID
          AND RLE.ROLE_NAME NOT IN ('ROLE_CTB', 'ROLE_PARENT', 'ROLE_SUPER')
        ORDER BY RLE.ROLEID;*/
        

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_USER_DETAILS_ON_EDIT;

END PKG_MANAGE_USERS;
/
