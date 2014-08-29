CREATE OR REPLACE PACKAGE PKG_ADMIN_MODULE IS

  -- AUTHOR  : D-ABIR_DUTTA
  -- CREATED : 3/28/2014 9:31:03 AM
  -- PURPOSE : ADMIN MODULE
  
  TYPE REF_CURSOR IS REF CURSOR;
  
PROCEDURE SP_CREATE_USER (P_IN_USERNAME         IN USERS.USERNAME%TYPE,
                          P_IN_DISPNAME         IN USERS.DISPLAY_USERNAME%TYPE,
                          P_IN_EMAILID          IN USERS.EMAIL_ADDRESS%TYPE,
                          P_IN_USERSTATUS       IN USERS.ACTIVATION_STATUS%TYPE,
                          P_IN_FIRSTTIME_LOGIN  IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                          P_IN_PASSWORD         IN USERS.PASSWORD%TYPE, 
                          P_IN_SALT             IN USERS.SALT%TYPE,
                          P_IN_NEW_USER         IN USERS.IS_NEW_USER%TYPE,
                          P_IN_CUSTOMERID       IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                          P_IN_ORGNODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                          P_IN_ORG_LVL          IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                          P_IN_CUST_PROD_ID     IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                          P_IN_ACTIVE_FLAG      IN VARCHAR2,
                          P_IN_ROLES            IN VARCHAR2,
                          P_OUT_NODE_ID         OUT NUMBER,
                          P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);
                                      
                                      
  PROCEDURE SP_CREATE_PARENT(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                             P_IN_DISPNAME        IN USERS.DISPLAY_USERNAME%TYPE,
                             P_IN_EMAILID         IN USERS.EMAIL_ADDRESS%TYPE,
                             P_IN_USERSTATUS      IN USERS.ACTIVATION_STATUS%TYPE,
                             P_IN_FIRSTTIME_LOGIN IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                             P_IN_PASSWORD        IN USERS.PASSWORD%TYPE,
                             P_IN_SALT            IN USERS.SALT%TYPE,
                             P_IN_NEW_USER        IN USERS.IS_NEW_USER%TYPE,
                             P_IN_INVITAIONCODE   IN INVITATION_CODE.INVITATION_CODE%TYPE,
                             P_IN_MOBILE          IN USERS.PHONE_NO%TYPE,
                             P_IN_COUNTRY         IN USERS.COUNTRY%TYPE,
                             P_IN_ZIP             IN USERS.ZIPCODE%TYPE,
                             P_IN_STREET          IN USERS.STREET%TYPE,
                             P_IN_CITY            IN USERS.CITY%TYPE,
                             P_IN_STATE           IN USERS.STATE%TYPE,
                             P_IN_LASTNAME        IN USERS.LAST_NAME%TYPE,
                             P_IN_FIRSTNAME       IN USERS.FIRST_NAME%TYPE,
                             P_OUT_USERID         OUT NUMBER,
                             P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2);  
   
  PROCEDURE SP_GET_USER_DETAILS(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                                P_OUT_REF_CURSOR    OUT REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);
                                
  PROCEDURE SP_GET_EDUUSER_DETAILS(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                                P_OUT_REF_CURSOR    OUT REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);                                                                                                 


END PKG_ADMIN_MODULE;
/
CREATE OR REPLACE PACKAGE BODY PKG_ADMIN_MODULE IS

  ---- ADD ADMIN AND SSO USERS.
  PROCEDURE SP_CREATE_USER(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                           P_IN_DISPNAME        IN USERS.DISPLAY_USERNAME%TYPE,
                           P_IN_EMAILID         IN USERS.EMAIL_ADDRESS%TYPE,
                           P_IN_USERSTATUS      IN USERS.ACTIVATION_STATUS%TYPE,
                           P_IN_FIRSTTIME_LOGIN IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                           P_IN_PASSWORD        IN USERS.PASSWORD%TYPE,
                           P_IN_SALT            IN USERS.SALT%TYPE,
                           P_IN_NEW_USER        IN USERS.IS_NEW_USER%TYPE,
                           P_IN_CUSTOMERID      IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                           P_IN_ORGNODEID       IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                           P_IN_ORG_LVL         IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                           P_IN_CUST_PROD_ID    IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                           P_IN_ACTIVE_FLAG     IN VARCHAR2,
                           P_IN_ROLES           IN VARCHAR2,
                           P_OUT_NODE_ID        OUT NUMBER,
                           P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  
    INCOUNT   NUMBER := 0;
    USERSEQID USERS.USERID%TYPE := 0;
  
  BEGIN
    SELECT COUNT(1)
      INTO INCOUNT
      FROM USERS
     WHERE UPPER(USERNAME) = UPPER(P_IN_USERNAME);
  
    IF INCOUNT = 0 THEN
      SELECT USER_ID_SEQ.NEXTVAL INTO USERSEQID FROM DUAL;
    
      INSERT INTO USERS
        (USERID,
         USERNAME,
         DISPLAY_USERNAME,
         EMAIL_ADDRESS,
         ACTIVATION_STATUS,
         IS_FIRSTTIME_LOGIN,
         PASSWORD,
         SALT,
         IS_NEW_USER,
         CUSTOMERID)
      VALUES
        (USERSEQID,
         P_IN_USERNAME,
         P_IN_DISPNAME,
         P_IN_EMAILID,
         P_IN_USERSTATUS,
         P_IN_FIRSTTIME_LOGIN,
         P_IN_PASSWORD,
         P_IN_SALT,
         P_IN_NEW_USER,
         P_IN_CUSTOMERID);
    
      INSERT INTO ORG_USERS
        (ORG_USER_ID,
         USERID,
         ORG_NODEID,
         ORG_NODE_LEVEL,
         ADMINID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (ORG_USER_ID_SEQ.NEXTVAL,
         USERSEQID,
         P_IN_ORGNODEID,
         P_IN_ORG_LVL,
         (SELECT ADMINID
            FROM CUST_PRODUCT_LINK
           WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID),
         P_IN_ACTIVE_FLAG,
         SYSDATE);
    
      FOR I IN (SELECT ROLEID
                  FROM ROLE
                 WHERE ROLE_NAME IN (WITH T AS (SELECT P_IN_ROLES FROM DUAL)
                -- END OF SAMPLE DATA
                  SELECT REGEXP_SUBSTR(P_IN_ROLES, '[^,]+', 1, LEVEL)
                    FROM T
                  CONNECT BY LEVEL <=
                             LENGTH(REGEXP_REPLACE(P_IN_ROLES, '[^,]*')) + 1)
                )
      
       LOOP
        INSERT INTO USER_ROLE
          (ROLEID, USERID, CREATED_DATE_TIME)
        VALUES
          (I.ROLEID, USERSEQID, SYSDATE);
      
      END LOOP;
    
      P_OUT_NODE_ID := P_IN_ORGNODEID;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_CREATE_USER;

  ---- ADD PARENT USERS.
  PROCEDURE SP_CREATE_PARENT(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                             P_IN_DISPNAME        IN USERS.DISPLAY_USERNAME%TYPE,
                             P_IN_EMAILID         IN USERS.EMAIL_ADDRESS%TYPE,
                             P_IN_USERSTATUS      IN USERS.ACTIVATION_STATUS%TYPE,
                             P_IN_FIRSTTIME_LOGIN IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                             P_IN_PASSWORD        IN USERS.PASSWORD%TYPE,
                             P_IN_SALT            IN USERS.SALT%TYPE,
                             P_IN_NEW_USER        IN USERS.IS_NEW_USER%TYPE,
                             P_IN_INVITAIONCODE   IN INVITATION_CODE.INVITATION_CODE%TYPE,
                             P_IN_MOBILE          IN USERS.PHONE_NO%TYPE,
                             P_IN_COUNTRY         IN USERS.COUNTRY%TYPE,
                             P_IN_ZIP             IN USERS.ZIPCODE%TYPE,
                             P_IN_STREET          IN USERS.STREET%TYPE,
                             P_IN_CITY            IN USERS.CITY%TYPE,
                             P_IN_STATE           IN USERS.STATE%TYPE,
                             P_IN_LASTNAME        IN USERS.LAST_NAME%TYPE,
                             P_IN_FIRSTNAME       IN USERS.FIRST_NAME%TYPE,
                             P_OUT_USERID         OUT NUMBER,
                             P_OUT_EXCEP_ERR_MSG  OUT VARCHAR2) IS
  
    INCOUNT           NUMBER := 0;
    USERSEQID         USERS.USERID%TYPE := 0;
    ORGUSERSEQID      ORG_USERS.ORG_USER_ID%TYPE := 0;
    CLAIMAVAILABILITY NUMBER := 0;
    P_NODE_ID         NUMBER := 0;
  
  BEGIN
  
    SELECT IC.TOTAL_AVAILABLE
      INTO CLAIMAVAILABILITY
      FROM INVITATION_CODE IC
     WHERE IC.INVITATION_CODE = P_IN_INVITAIONCODE
       AND IC.ACTIVATION_STATUS = 'AC'
       AND IC.EXPIRATION_DATE >= SYSDATE;
  
    IF CLAIMAVAILABILITY > 0 THEN
      SELECT COUNT(1)
        INTO INCOUNT
        FROM USERS
       WHERE UPPER(USERNAME) = UPPER(P_IN_USERNAME);
    
      IF INCOUNT = 0 THEN
        SELECT USER_ID_SEQ.NEXTVAL INTO USERSEQID FROM DUAL;
      
        INSERT INTO USERS
          (USERID,
           USERNAME,
           DISPLAY_USERNAME,
           LAST_NAME,
           FIRST_NAME,
           MIDDLE_NAME,
           EMAIL_ADDRESS,
           PHONE_NO,
           COUNTRY,
           ZIPCODE,
           STREET,
           CITY,
           STATE,
           CUSTOMERID,
           IS_FIRSTTIME_LOGIN,
           IS_NEW_USER,
           ACTIVATION_STATUS,
           CREATED_DATE_TIME,
           PASSWORD,
           SALT)
        VALUES
          (USERSEQID,
           P_IN_USERNAME,
           P_IN_DISPNAME,
           P_IN_LASTNAME,
           P_IN_FIRSTNAME,
           '',
           P_IN_EMAILID,
           P_IN_MOBILE,
           P_IN_COUNTRY,
           P_IN_ZIP,
           P_IN_STREET,
           P_IN_CITY,
           P_IN_STATE,
           (SELECT DISTINCT CPL.CUSTOMERID
              FROM CUST_PRODUCT_LINK CPL, INVITATION_CODE INV
             WHERE CPL.CUST_PROD_ID = INV.CUST_PROD_ID
               AND INV.ACTIVATION_STATUS = 'AC'
               AND INV.INVITATION_CODE = P_IN_INVITAIONCODE),
           P_IN_FIRSTTIME_LOGIN,
           P_IN_NEW_USER,
           P_IN_USERSTATUS,
           SYSDATE,
           P_IN_PASSWORD,
           P_IN_SALT);
      
        SELECT M.ORG_NODEID
          INTO P_NODE_ID
          FROM INVITATION_CODE M
         WHERE M.INVITATION_CODE = P_IN_INVITAIONCODE;
      
        SELECT ORG_USER_ID_SEQ.NEXTVAL INTO ORGUSERSEQID FROM DUAL;
      
        INSERT INTO ORG_USERS
          (ORG_USER_ID,
           USERID,
           ORG_NODEID,
           ORG_NODE_LEVEL,
           ADMINID,
           ACTIVATION_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ORGUSERSEQID,
           USERSEQID,
           P_NODE_ID,
           3,
           (SELECT CUST_PRODUCT_LINK.ADMINID
              FROM INVITATION_CODE, CUST_PRODUCT_LINK
             WHERE INVITATION_CODE.CUST_PROD_ID =
                   CUST_PRODUCT_LINK.CUST_PROD_ID
               AND INVITATION_CODE = P_IN_INVITAIONCODE),
           'AC',
           SYSDATE);
      
        FOR I IN (SELECT ROLEID
                    FROM ROLE
                   WHERE ROLE_NAME IN ('ROLE_USER', 'ROLE_PARENT')) LOOP
          INSERT INTO USER_ROLE
            (ROLEID, USERID, CREATED_DATE_TIME)
          VALUES
            (I.ROLEID, USERSEQID, SYSDATE);
        
        END LOOP;
      
        INSERT INTO INVITATION_CODE_CLAIM
          (INVITATION_CODE_CLAIM_ID,
           ICID,
           ORG_USER_ID,
           ACTIVATION_STATUS,
           CLAIM_DATE,
           UPDATED_DATE_TIME)
        VALUES
          (INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL,
           (SELECT ICID
              FROM INVITATION_CODE INV
             WHERE INV.ACTIVATION_STATUS = 'AC'
               AND INV.INVITATION_CODE = P_IN_INVITAIONCODE
               AND ROWNUM = 1),
           ORGUSERSEQID,
           'AC',
           SYSDATE,
           SYSDATE);
      
        UPDATE INVITATION_CODE
           SET TOTAL_ATTEMPT     = (SELECT TOTAL_ATTEMPT
                                      FROM INVITATION_CODE
                                     WHERE INVITATION_CODE =
                                           P_IN_INVITAIONCODE
                                       AND ACTIVATION_STATUS = 'AC') + 1,
               TOTAL_AVAILABLE   = (SELECT TOTAL_AVAILABLE
                                      FROM INVITATION_CODE
                                     WHERE INVITATION_CODE =
                                           P_IN_INVITAIONCODE
                                       AND ACTIVATION_STATUS = 'AC') - 1,
               UPDATED_DATE_TIME = SYSDATE
         WHERE INVITATION_CODE = P_IN_INVITAIONCODE;
      
      END IF;
    
      P_OUT_USERID := USERSEQID;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_CREATE_PARENT;

  -- SP_GET_USER_DETAILS
  -- this requried during login and fetch default cust prod id
  PROCEDURE SP_GET_USER_DETAILS(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                                P_OUT_REF_CURSOR    OUT REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
    
     SELECT USERS.IS_FIRSTTIME_LOGIN,
        USERS.USERID,
        ORG.ORG_NODEID,
        ORG.ORG_NODE_LEVEL,
        USERS.DISPLAY_USERNAME,
        USERS.ACTIVATION_STATUS,
        USERS.PASSWORD,
        USERS.SALT,
        USERS.CUSTOMERID CUSTID,
        USERS.EMAIL_ADDRESS EMAIL,
        OND.ORG_MODE,
        OPL.CUST_PROD_ID DEFAULT_CUST_PROD_ID
   FROM USERS USERS, ORG_USERS ORG, ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL
  WHERE UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)
    AND USERS.USERID = ORG.USERID
    AND ORG.ORG_NODEID = OND.ORG_NODEID
    AND OND.ORG_NODEID = OPL.ORG_NODEID
    AND EXISTS
  (SELECT 1
           FROM CUST_PRODUCT_LINK CPL, PRODUCT P
          WHERE CPL.PRODUCTID = P.PRODUCTID
            AND P.PRODUCT_SEQ = (SELECT MAX(PRODUCT_SEQ) FROM PRODUCT)
            AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
            AND EXISTS
          (SELECT 1
                   FROM USERS
                  WHERE USERS.CUSTOMERID = CPL.CUSTOMERID
                    AND UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)));

  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_USER_DETAILS;
  
  
  -- SP_GET_EDUUSER_DETAILS
  -- this requried during login for edu center user and fetch default cust prod id
  PROCEDURE SP_GET_EDUUSER_DETAILS(P_IN_USERNAME        IN USERS.USERNAME%TYPE,
                                P_OUT_REF_CURSOR    OUT REF_CURSOR,
                                P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
    
    SELECT USERS.IS_FIRSTTIME_LOGIN,
       USERS.USERID,
       EC.EDU_CENTERID ORG_NODEID,
       -99 ORG_NODE_LEVEL,
       USERS.DISPLAY_USERNAME,
       USERS.ACTIVATION_STATUS,
       USERS.PASSWORD,
       USERS.SALT,
       USERS.CUSTOMERID CUSTID,
       USERS.email_address EMAIL,
       'PUBLIC' AS ORG_MODE,
       0 DEFAULT_CUST_PROD_ID
  FROM USERS USERS, EDU_CENTER_USER_LINK EL, Edu_Center_Details EC
 WHERE UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)
   AND USERS.CUSTOMERID = EC.CUSTOMERID
   AND EC.EDU_CENTERID = EL.EDU_CENTERID
   AND EL.USERID = USERS.USERID;

  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_EDUUSER_DETAILS;

END PKG_ADMIN_MODULE;
/
