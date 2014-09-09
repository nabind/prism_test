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
                                          P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                          P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_ONSCROLL(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                  P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                  P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                  P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                  P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_ON_FIRST_LOAD(P_IN_CUSTOMER_ID    IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                       P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                       P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USER_ROLE(P_IN_USER_ID        IN USERS.USERID%TYPE,
                             P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                             P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_SEARCH(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                P_IN_SEARCH_PARAM   IN VARCHAR2,
                                P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USERS_SEARCH_EXACT(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                      P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                      P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                      P_IN_SEARCH_PARAM   IN VARCHAR2,
                                      P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USER_RESET_PASSWORD(P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                       P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_ORG_NODE_LEVEL IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_USER_PWD_HINT_LIST(P_IN_USERID         IN USERS.USERID%TYPE,
                                      P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_VALIDATE_USERNAME(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                 P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                 P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

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
                     (SELECT *
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_MODE = P_IN_ORGMODE
                       START WITH ORG_NODEID = P_IN_TENANT_ID
                      CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
                      UNION
                      SELECT *
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
       WHERE ROWNUM <= 15
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
                     (SELECT *
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_MODE = P_IN_ORGMODE
                       START WITH ORG_NODEID = P_IN_TENANT_ID
                      CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
                      UNION
                      SELECT *
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
       WHERE ROWNUM <= 15
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
                     (SELECT *
                        FROM ORG_NODE_DIM
                       WHERE CUSTOMERID = P_IN_CUSTOMER_ID
                         AND ORG_MODE = P_IN_ORGMODE
                       START WITH ORG_NODEID = P_IN_TENANT_ID
                      CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
                      UNION
                      SELECT *
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
       WHERE ROWNUM <= 15
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
                        FROM ORG_TP_STRUCTURE
                       ORDER BY ORG_LEVEL) TEMP
               GROUP BY TEMP.ORG_LEVEL) OTS
       WHERE USR.USERID = P_IN_USER_ID
         AND URLE.USERID = USR.USERID
         AND RLE.ROLEID = URLE.ROLEID
         AND USR.USERID = OU.USERID
         AND OU.ORG_NODE_LEVEL = OTS.ORG_LEVEL
       ORDER BY ROLEID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_USER_ROLE;

  --SEARCH_USER
  PROCEDURE SP_GET_USERS_SEARCH(P_IN_ORGMODE        IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                P_IN_TENANT_ID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                P_IN_ROLE_ID        IN ROLE.ROLEID%TYPE,
                                P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                P_IN_SEARCH_PARAM   IN VARCHAR2,
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
             (SELECT *
                FROM ORG_NODE_DIM
               WHERE ORG_MODE = P_IN_ORGMODE
               START WITH ORG_NODEID = P_IN_TENANT_ID
              CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
              UNION
              SELECT *
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
         AND ROWNUM <= 15
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
             (SELECT *
                FROM ORG_NODE_DIM
               WHERE ORG_MODE = P_IN_ORGMODE
               START WITH ORG_NODEID = P_IN_TENANT_ID
              CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
              UNION
              SELECT *
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
         AND ROWNUM <= 15
       ORDER BY UPPER(USR.USERNAME);
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_USERS_SEARCH_EXACT;

  -- SP_GET_USER_RESET_PASSWORD
  PROCEDURE SP_GET_USER_RESET_PASSWORD(P_IN_USER_NAME      IN USERS.USERNAME%TYPE,
                                       P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_ORG_NODE_LEVEL IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
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
             U.ACTIVATION_STATUS,
             HIER.ORG_NODE_NAME,
             HIER.ORG_NODEID,
             HIER.PARENT_ORG_NODEID
        FROM USERS U,
             ORG_USERS OU,
             (SELECT *
                FROM ORG_NODE_DIM
               START WITH ORG_NODEID = P_IN_ORG_NODEID
              CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
              UNION
              SELECT *
                FROM ORG_NODE_DIM
               WHERE ORG_NODEID = P_IN_ORG_NODEID
                 AND ORG_NODE_LEVEL = P_IN_ORG_NODE_LEVEL) HIER
       WHERE UPPER(U.USERNAME) = UPPER(P_IN_USER_NAME)
         AND NOT EXISTS (SELECT 1
                FROM USER_ROLE
               WHERE USER_ROLE.ROLEID IN (6, 7)
                 AND USER_ROLE.USERID = U.USERID)
         AND OU.ORG_NODEID = HIER.ORG_NODEID
         AND OU.USERID = U.USERID
         AND OU.ORG_NODE_LEVEL <> 0;
  
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
       ORDER BY Q.QUESTION_SEQ;
  
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

END PKG_MANAGE_USERS;
/
