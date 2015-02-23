CREATE OR REPLACE PACKAGE PKG_ADMIN_MODULE IS

  -- AUTHOR  : D-ABIR_DUTTA
  -- CREATED : 3/28/2014 9:31:03 AM
  -- PURPOSE : ADMIN MODULE

  TYPE REF_CURSOR IS REF CURSOR;

  PROCEDURE SP_CREATE_USER(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                           P_IN_DISPNAME          IN USERS.DISPLAY_USERNAME%TYPE,
                           P_IN_EMAILID           IN USERS.EMAIL_ADDRESS%TYPE,
                           P_IN_USERSTATUS        IN USERS.ACTIVATION_STATUS%TYPE,
                           P_IN_FIRSTTIME_LOGIN   IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                           P_IN_PASSWORD          IN USERS.PASSWORD%TYPE,
                           P_IN_SALT              IN USERS.SALT%TYPE,
                           P_IN_NEW_USER          IN USERS.IS_NEW_USER%TYPE,
                           P_IN_CUSTOMERID        IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                           P_IN_ORGNODEID         IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                           P_IN_ORG_LVL           IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                           P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                           P_IN_ACTIVE_FLAG       IN VARCHAR2,
                           P_IN_ROLES             IN VARCHAR2,
                           P_IN_IS_EDU            IN VARCHAR2,
                           P_OUT_USER_REF_CURSOR  OUT REF_CURSOR,
                           P_OUT_ROLES_REF_CURSOR OUT REF_CURSOR,
                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_CREATE_SSO_USER(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                               P_IN_DISPNAME          IN USERS.DISPLAY_USERNAME%TYPE,
                               P_IN_EMAILID           IN USERS.EMAIL_ADDRESS%TYPE,
                               P_IN_USERSTATUS        IN USERS.ACTIVATION_STATUS%TYPE,
                               P_IN_FIRSTTIME_LOGIN   IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                               P_IN_PASSWORD          IN USERS.PASSWORD%TYPE,
                               P_IN_SALT              IN USERS.SALT%TYPE,
                               P_IN_NEW_USER          IN USERS.IS_NEW_USER%TYPE,
                               P_IN_CUSTOMERID        IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                               P_IN_ORGNODEID         IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                               P_IN_ORG_LVL           IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                               P_IN_ADMINID           IN ADMIN_DIM.ADMINID%TYPE,
                               P_IN_ACTIVE_FLAG       IN VARCHAR2,
                               P_IN_ROLES             IN VARCHAR2,
                               P_IN_IS_EDU            IN VARCHAR2,
                               P_OUT_USER_REF_CURSOR  OUT REF_CURSOR,
                               P_OUT_ROLES_REF_CURSOR OUT REF_CURSOR,
                               P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_CREATE_PARENT(P_IN_USERNAME         IN USERS.USERNAME%TYPE,
                             P_IN_DISPNAME         IN USERS.DISPLAY_USERNAME%TYPE,
                             P_IN_EMAILID          IN USERS.EMAIL_ADDRESS%TYPE,
                             P_IN_USERSTATUS       IN USERS.ACTIVATION_STATUS%TYPE,
                             P_IN_FIRSTTIME_LOGIN  IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                             P_IN_PASSWORD         IN USERS.PASSWORD%TYPE,
                             P_IN_SALT             IN USERS.SALT%TYPE,
                             P_IN_NEW_USER         IN USERS.IS_NEW_USER%TYPE,
                             P_IN_INVITAIONCODE    IN INVITATION_CODE.INVITATION_CODE%TYPE,
                             P_IN_MOBILE           IN USERS.PHONE_NO%TYPE,
                             P_IN_COUNTRY          IN USERS.COUNTRY%TYPE,
                             P_IN_ZIP              IN USERS.ZIPCODE%TYPE,
                             P_IN_STREET           IN USERS.STREET%TYPE,
                             P_IN_CITY             IN USERS.CITY%TYPE,
                             P_IN_STATE            IN USERS.STATE%TYPE,
                             P_IN_LASTNAME         IN USERS.LAST_NAME%TYPE,
                             P_IN_FIRSTNAME        IN USERS.FIRST_NAME%TYPE,
                             P_IN_PH_QUESTIONIDS   IN VARCHAR2,
                             P_IN_PH_ANSWER_VALUES IN VARCHAR2,
                             P_OUT_USERID          OUT NUMBER,
                             P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE SP_GET_USER_DETAILS(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                                P_OUT_REF_CURSOR       OUT REF_CURSOR,
                                V_OUT_PASSWORD_EXPIRED OUT VARCHAR2,
                                V_OUT_PASSWORD_WARNING OUT VARCHAR2,
                                P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_GET_EDUUSER_DETAILS(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                                   P_OUT_REF_CURSOR       OUT REF_CURSOR,
                                   V_OUT_PASSWORD_EXPIRED OUT VARCHAR2,
                                   V_OUT_PASSWORD_WARNING OUT VARCHAR2,
                                   P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_GET_ORG_NODE_LEVEL(P_OUT_CUR_ORG_NODE_LEVEL OUT REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  PROCEDURE SP_SAVE_PASSWORD_HISTORY(P_IN_USERID         IN USERS.USERID%TYPE,
                                     P_IN_PASSWORD       IN USERS.PASSWORD%TYPE,
                                     P_OUT_STATUS_NUMBER OUT NUMBER,
                                     P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_PASSWORD_HISTORY(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                    P_IN_LIMIT          IN NUMBER,
                                    P_OUT_REF_CURSOR    OUT REF_CURSOR,
                                    P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_CHECK_ORG_HIERARCHY(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                   P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_ORGNODEID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                   P_OUT_EXIST_FLAG    OUT NUMBER,
                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_CHECK_USER_ROLE_BY_USERNAME(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                           P_IN_USER_ROLE      IN VARCHAR2,
                                           P_OUT_STATUS_NUMBER OUT NUMBER,
                                           P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);
  
   PROCEDURE SP_DELETE_USER(P_IN_USERID         IN USERS.USERID%TYPE,
                            P_IN_PURPOSE        IN VARCHAR2,
                            P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);                                           

END PKG_ADMIN_MODULE;
/
CREATE OR REPLACE PACKAGE BODY PKG_ADMIN_MODULE IS

  ---- ADD ADMIN USERS.
  PROCEDURE SP_CREATE_USER(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                           P_IN_DISPNAME          IN USERS.DISPLAY_USERNAME%TYPE,
                           P_IN_EMAILID           IN USERS.EMAIL_ADDRESS%TYPE,
                           P_IN_USERSTATUS        IN USERS.ACTIVATION_STATUS%TYPE,
                           P_IN_FIRSTTIME_LOGIN   IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                           P_IN_PASSWORD          IN USERS.PASSWORD%TYPE,
                           P_IN_SALT              IN USERS.SALT%TYPE,
                           P_IN_NEW_USER          IN USERS.IS_NEW_USER%TYPE,
                           P_IN_CUSTOMERID        IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                           P_IN_ORGNODEID         IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                           P_IN_ORG_LVL           IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                           P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                           P_IN_ACTIVE_FLAG       IN VARCHAR2,
                           P_IN_ROLES             IN VARCHAR2,
                           P_IN_IS_EDU            IN VARCHAR2,
                           P_OUT_USER_REF_CURSOR  OUT REF_CURSOR,
                           P_OUT_ROLES_REF_CURSOR OUT REF_CURSOR,
                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  
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
    
      IF P_IN_IS_EDU = 'N' THEN
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
      ELSIF P_IN_IS_EDU = 'Y' THEN
        INSERT INTO EDU_CENTER_USER_LINK
          (EDU_CENTERID, USERID, CUST_PROD_ID)
        VALUES
          (P_IN_ORGNODEID, USERSEQID, P_IN_CUST_PROD_ID);
      END IF;
    
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
    
      INSERT INTO PASSWORD_HISTORY
        (PWD_HISTORYID, USERID, PASSWORD, CREATED_DATE_TIME)
      VALUES
        (SEQ_PASSWORD_HISTORY.NEXTVAL, USERSEQID, P_IN_PASSWORD, SYSDATE);
    
      PKG_MANAGE_USERS.SP_GET_USER_DETAILS_ON_EDIT(USERSEQID,
                                                   P_OUT_USER_REF_CURSOR,
                                                   P_OUT_ROLES_REF_CURSOR,
                                                   P_OUT_EXCEP_ERR_MSG);
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_CREATE_USER;

  -- ADD SSO users
  PROCEDURE SP_CREATE_SSO_USER(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                               P_IN_DISPNAME          IN USERS.DISPLAY_USERNAME%TYPE,
                               P_IN_EMAILID           IN USERS.EMAIL_ADDRESS%TYPE,
                               P_IN_USERSTATUS        IN USERS.ACTIVATION_STATUS%TYPE,
                               P_IN_FIRSTTIME_LOGIN   IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                               P_IN_PASSWORD          IN USERS.PASSWORD%TYPE,
                               P_IN_SALT              IN USERS.SALT%TYPE,
                               P_IN_NEW_USER          IN USERS.IS_NEW_USER%TYPE,
                               P_IN_CUSTOMERID        IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                               P_IN_ORGNODEID         IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                               P_IN_ORG_LVL           IN ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE,
                               P_IN_ADMINID           IN ADMIN_DIM.ADMINID%TYPE,
                               P_IN_ACTIVE_FLAG       IN VARCHAR2,
                               P_IN_ROLES             IN VARCHAR2,
                               P_IN_IS_EDU            IN VARCHAR2,
                               P_OUT_USER_REF_CURSOR  OUT REF_CURSOR,
                               P_OUT_ROLES_REF_CURSOR OUT REF_CURSOR,
                               P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  
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
         P_IN_ADMINID,
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
    
      INSERT INTO PASSWORD_HISTORY
        (PWD_HISTORYID, USERID, PASSWORD, CREATED_DATE_TIME)
      VALUES
        (SEQ_PASSWORD_HISTORY.NEXTVAL, USERSEQID, P_IN_PASSWORD, SYSDATE);
    
      PKG_MANAGE_USERS.SP_GET_USER_DETAILS_ON_EDIT(USERSEQID,
                                                   P_OUT_USER_REF_CURSOR,
                                                   P_OUT_ROLES_REF_CURSOR,
                                                   P_OUT_EXCEP_ERR_MSG);
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_CREATE_SSO_USER;

  PROCEDURE SP_CREATE_PARENT(P_IN_USERNAME         IN USERS.USERNAME%TYPE,
                             P_IN_DISPNAME         IN USERS.DISPLAY_USERNAME%TYPE,
                             P_IN_EMAILID          IN USERS.EMAIL_ADDRESS%TYPE,
                             P_IN_USERSTATUS       IN USERS.ACTIVATION_STATUS%TYPE,
                             P_IN_FIRSTTIME_LOGIN  IN USERS.IS_FIRSTTIME_LOGIN%TYPE,
                             P_IN_PASSWORD         IN USERS.PASSWORD%TYPE,
                             P_IN_SALT             IN USERS.SALT%TYPE,
                             P_IN_NEW_USER         IN USERS.IS_NEW_USER%TYPE,
                             P_IN_INVITAIONCODE    IN INVITATION_CODE.INVITATION_CODE%TYPE,
                             P_IN_MOBILE           IN USERS.PHONE_NO%TYPE,
                             P_IN_COUNTRY          IN USERS.COUNTRY%TYPE,
                             P_IN_ZIP              IN USERS.ZIPCODE%TYPE,
                             P_IN_STREET           IN USERS.STREET%TYPE,
                             P_IN_CITY             IN USERS.CITY%TYPE,
                             P_IN_STATE            IN USERS.STATE%TYPE,
                             P_IN_LASTNAME         IN USERS.LAST_NAME%TYPE,
                             P_IN_FIRSTNAME        IN USERS.FIRST_NAME%TYPE,
                             P_IN_PH_QUESTIONIDS   IN VARCHAR2,
                             P_IN_PH_ANSWER_VALUES IN VARCHAR2,
                             P_OUT_USERID          OUT NUMBER,
                             P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  
    INCOUNT                  NUMBER := 0;
    USERSEQID                USERS.USERID%TYPE := 0;
    ORGUSERSEQID             ORG_USERS.ORG_USER_ID%TYPE := 0;
    CLAIMAVAILABILITY        NUMBER := 0;
    P_NODE_ID                NUMBER := 0;
    P_OUT_STATUS_PH_ANSWER   NUMBER := 0;
    P_OUT_STATUS_PWD_HISTORY NUMBER := 0;
    P_OUT_EXCEP_ERR_MSG1     VARCHAR2(1000);
  
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
      
        PKG_MY_ACCOUNT.SP_SAVE_PH_ANSWER(USERSEQID,
                                         P_IN_PH_QUESTIONIDS,
                                         '',
                                         P_IN_PH_ANSWER_VALUES,
                                         P_OUT_STATUS_PH_ANSWER,
                                         P_OUT_EXCEP_ERR_MSG1);
      
        IF P_OUT_STATUS_PH_ANSWER = 1 THEN
        
          SP_SAVE_PASSWORD_HISTORY(USERSEQID,
                                   P_IN_PASSWORD,
                                   P_OUT_STATUS_PWD_HISTORY,
                                   P_OUT_EXCEP_ERR_MSG1);
        
          IF P_OUT_STATUS_PWD_HISTORY = 1 THEN
            P_OUT_USERID := USERSEQID;
          ELSE
            ROLLBACK;
            P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG1;
          END IF;
        ELSE
          ROLLBACK;
          P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG1;
        END IF;
      
      END IF;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_USERID        := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_CREATE_PARENT;

  -- SP_GET_USER_DETAILS
  -- THIS REQURIED DURING LOGIN AND FETCH DEFAULT CUST PROD ID
  PROCEDURE SP_GET_USER_DETAILS(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                                P_OUT_REF_CURSOR       OUT REF_CURSOR,
                                V_OUT_PASSWORD_EXPIRED OUT VARCHAR2,
                                V_OUT_PASSWORD_WARNING OUT VARCHAR2,
                                P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  
    V_PASSWORD_EXPIRY    NUMBER := 0;
    V_PASSWORD_WARNING   NUMBER := 0;
    V_LAST_PASSWORD_DATE PASSWORD_HISTORY.CREATED_DATE_TIME%TYPE;
  
  BEGIN
  
    OPEN P_OUT_REF_CURSOR FOR
    
      SELECT A.*
        FROM (SELECT USERS.IS_FIRSTTIME_LOGIN,
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
                     CPL.CUST_PROD_ID DEFAULT_CUST_PROD_ID,
                     DENSE_RANK() OVER(PARTITION BY USERS.USERID ORDER BY P.PRODUCT_SEQ DESC) AS MAX_VAL
                FROM USERS USERS,
                     ORG_USERS ORG,
                     ORG_NODE_DIM OND,
                     ORG_PRODUCT_LINK OPL,
                     CUST_PRODUCT_LINK CPL,
                     PRODUCT P,
                     (SELECT ADMINID, ADMIN_YEAR
                        FROM ADMIN_DIM a
                       WHERE  IS_CURRENT_ADMIN = 'Y' 
                       OR exists 
                             (SELECT ADMIN_YEAR
                                FROM ADMIN_DIM b
                                where b.ADMIN_YEAR <= a.ADMIN_YEAR)) ADMIN
               WHERE UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)
                 AND USERS.USERID = ORG.USERID
                 AND ORG.ORG_NODEID = OND.ORG_NODEID
                 AND OND.ORG_NODEID = OPL.ORG_NODEID
                 AND OPL.CUST_PROD_ID = CPL.CUST_PROD_ID
                 AND CPL.CUSTOMERID = USERS.CUSTOMERID
                 AND CPL.PRODUCTID = P.PRODUCTID
                 AND ADMIN.ADMINID = CPL.ADMINID) A
       WHERE A.MAX_VAL = 1;
  
    SELECT DB_PROPERY_VALUE
      INTO V_PASSWORD_EXPIRY
      FROM DASH_CONTRACT_PROP
     WHERE UPPER(DB_PROPERTY_NAME) = 'PASSWORD.EXPIRY';
  
    SELECT nvl(trunc(MAX(PH.CREATED_DATE_TIME)), TRUNC(sysdate))
      INTO V_LAST_PASSWORD_DATE
      FROM PASSWORD_HISTORY PH, USERS
     WHERE UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)
       AND USERS.USERID = PH.USERID;
  
    SELECT CASE
             WHEN trunc(sysdate) >= V_LAST_PASSWORD_DATE + V_PASSWORD_EXPIRY THEN
              'TRUE'
             ELSE
              'FALSE'
           END
      INTO V_OUT_PASSWORD_EXPIRED
      FROM DUAL;
  
    IF V_OUT_PASSWORD_EXPIRED = 'FALSE' THEN
    
      SELECT DB_PROPERY_VALUE
        INTO V_PASSWORD_WARNING
        FROM DASH_CONTRACT_PROP
       WHERE UPPER(DB_PROPERTY_NAME) = 'PASSWORD.EXPIRY.WARNING';
    
      SELECT CASE
               WHEN (trunc(sysdate) >
                    (V_LAST_PASSWORD_DATE + V_PASSWORD_WARNING)) THEN
                'TRUE'
               ELSE
                'FALSE'
             END
        INTO V_OUT_PASSWORD_WARNING
        FROM DUAL;
    ELSE
      V_OUT_PASSWORD_WARNING := 'TRUE';
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_USER_DETAILS;

  -- SP_GET_EDUUSER_DETAILS
  -- THIS REQURIED DURING LOGIN FOR EDU CENTER USER AND FETCH DEFAULT CUST PROD ID
  PROCEDURE SP_GET_EDUUSER_DETAILS(P_IN_USERNAME          IN USERS.USERNAME%TYPE,
                                   P_OUT_REF_CURSOR       OUT REF_CURSOR,
                                   V_OUT_PASSWORD_EXPIRED OUT VARCHAR2,
                                   V_OUT_PASSWORD_WARNING OUT VARCHAR2,
                                   P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  
    V_PASSWORD_EXPIRY    NUMBER := 0;
    V_PASSWORD_WARNING   NUMBER := 0;
    V_LAST_PASSWORD_DATE PASSWORD_HISTORY.CREATED_DATE_TIME%TYPE;
  
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
             USERS.EMAIL_ADDRESS EMAIL,
             'PUBLIC' AS ORG_MODE,
             EL.CUST_PROD_ID DEFAULT_CUST_PROD_ID
        FROM USERS USERS, EDU_CENTER_USER_LINK EL, EDU_CENTER_DETAILS EC
       WHERE UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)
         AND USERS.CUSTOMERID = EC.CUSTOMERID
         AND EC.EDU_CENTERID = EL.EDU_CENTERID
         AND EL.USERID = USERS.USERID;
  
    SELECT DB_PROPERY_VALUE
      INTO V_PASSWORD_EXPIRY
      FROM DASH_CONTRACT_PROP
     WHERE UPPER(DB_PROPERTY_NAME) = 'PASSWORD.EXPIRY';
  
    SELECT nvl(trunc(MAX(PH.CREATED_DATE_TIME)), TRUNC(SYSDATE))
      INTO V_LAST_PASSWORD_DATE
      FROM PASSWORD_HISTORY PH, USERS
     WHERE UPPER(USERS.USERNAME) = UPPER(P_IN_USERNAME)
       AND USERS.USERID = PH.USERID;
  
    SELECT CASE
             WHEN trunc(sysdate) >= V_LAST_PASSWORD_DATE + V_PASSWORD_EXPIRY THEN
              'TRUE'
             ELSE
              'FALSE'
           END
      INTO V_OUT_PASSWORD_EXPIRED
      FROM DUAL;
  
    IF V_OUT_PASSWORD_EXPIRED = 'FALSE' THEN
    
      SELECT DB_PROPERY_VALUE
        INTO V_PASSWORD_WARNING
        FROM DASH_CONTRACT_PROP
       WHERE UPPER(DB_PROPERTY_NAME) = 'PASSWORD.EXPIRY.WARNING';
    
      SELECT CASE
               WHEN (trunc(sysdate) >
                    (V_LAST_PASSWORD_DATE + V_PASSWORD_WARNING)) THEN
                'TRUE'
               ELSE
                'FALSE'
             END
        INTO V_OUT_PASSWORD_WARNING
        FROM DUAL;
    
    ELSE
      V_OUT_PASSWORD_WARNING := 'TRUE';
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_GET_EDUUSER_DETAILS;

  PROCEDURE SP_SAVE_PASSWORD_HISTORY(P_IN_USERID         IN USERS.USERID%TYPE,
                                     P_IN_PASSWORD       IN USERS.PASSWORD%TYPE,
                                     P_OUT_STATUS_NUMBER OUT NUMBER,
                                     P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
  
    INSERT INTO PASSWORD_HISTORY
      (PWD_HISTORYID,
       PASSWORD,
       USERID,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
    VALUES
      (SEQ_PASSWORD_HISTORY.NEXTVAL,
       P_IN_PASSWORD,
       P_IN_USERID,
       SYSDATE,
       SYSDATE);
  
    P_OUT_STATUS_NUMBER := 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
      P_OUT_STATUS_NUMBER := 0;
  END SP_SAVE_PASSWORD_HISTORY;

  PROCEDURE SP_GET_PASSWORD_HISTORY(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                    P_IN_LIMIT          IN NUMBER,
                                    P_OUT_REF_CURSOR    OUT REF_CURSOR,
                                    P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
    
      SELECT *
        FROM (SELECT SALT, PH.PASSWORD, PH.CREATED_DATE_TIME
                FROM PASSWORD_HISTORY PH, USERS U
               WHERE UPPER(USERNAME) = UPPER(P_IN_USERNAME)
                 AND PH.USERID = U.USERID
               ORDER BY PH.CREATED_DATE_TIME DESC)
       WHERE ROWNUM < P_IN_LIMIT + 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
    
  END SP_GET_PASSWORD_HISTORY;

  PROCEDURE SP_GET_ORG_NODE_LEVEL(P_OUT_CUR_ORG_NODE_LEVEL OUT REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    V_ROLE_FLAG NUMBER(1) := 0;
  BEGIN
  
    SELECT COUNT(1)
      INTO V_ROLE_FLAG
      FROM ROLE
     WHERE ROLE_NAME = 'ROLE_EDU_ADMIN';
  
    IF V_ROLE_FLAG = 0 THEN
      OPEN P_OUT_CUR_ORG_NODE_LEVEL FOR
        SELECT TAB.VALUE VALUE, LISTAGG(TAB.NAME, '/') WITHIN
         GROUP(
         ORDER BY NAME) AS NAME
          FROM (SELECT DISTINCT ORG_LEVEL VALUE, ORG_LABEL NAME
                  FROM ORG_TP_STRUCTURE
                 ORDER BY ORG_LEVEL) TAB
         GROUP BY TAB.VALUE;
    ELSE
      OPEN P_OUT_CUR_ORG_NODE_LEVEL FOR
        SELECT TAB.VALUE VALUE, LISTAGG(TAB.NAME, '/') WITHIN
         GROUP(
         ORDER BY NAME) AS NAME
          FROM (SELECT DISTINCT ORG_LEVEL VALUE, ORG_LABEL NAME
                  FROM ORG_TP_STRUCTURE
                 ORDER BY ORG_LEVEL) TAB
         GROUP BY TAB.VALUE
        UNION
        SELECT -99 VALUE, 'Education Center' NAME FROM DUAL ORDER BY VALUE;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_ORG_NODE_LEVEL;

  PROCEDURE SP_CHECK_ORG_HIERARCHY(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                   P_IN_CUST_PROD_ID   IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_ORGNODEID      IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                   P_OUT_EXIST_FLAG    OUT NUMBER,
                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
  
    SELECT COUNT(1)
      INTO P_OUT_EXIST_FLAG
      FROM USERS U,
           ORG_USERS OU,
           (SELECT A.ORG_NODEID
              FROM (SELECT OND.ORG_NODEID,
                           OND.ORG_NODE_NAME,
                           OND.PARENT_ORG_NODEID,
                           OND.ORG_NODE_LEVEL,
                           OND.ORG_MODE
                      FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL
                     WHERE OPL.ORG_NODEID = OND.ORG_NODEID
                       AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID
                       AND OND.ORG_NODE_LEVEL <> 0) A
            CONNECT BY PRIOR ORG_NODEID = PARENT_ORG_NODEID
             START WITH ORG_NODEID = P_IN_ORGNODEID) TAB
     WHERE U.USERID = OU.USERID
       AND OU.ORG_NODEID = TAB.ORG_NODEID
       AND U.USERNAME = P_IN_USERNAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_CHECK_ORG_HIERARCHY;

  PROCEDURE SP_CHECK_USER_ROLE_BY_USERNAME(P_IN_USERNAME       IN USERS.USERNAME%TYPE,
                                           P_IN_USER_ROLE      IN VARCHAR2,
                                           P_OUT_STATUS_NUMBER OUT NUMBER,
                                           P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
    V_ROLE_COUNT NUMBER(2);
  
  BEGIN
    P_OUT_STATUS_NUMBER := 0;
    
    SELECT DISTINCT COUNT(RLE.ROLE_NAME)
      INTO V_ROLE_COUNT
      FROM USER_ROLE URLE, ROLE RLE, USERS USR
     WHERE USR.USERNAME = P_IN_USERNAME
       AND URLE.USERID = USR.USERID
       AND RLE.ROLEID = URLE.ROLEID
       AND RLE.ROLE_NAME = 'ROLE_ADMIN';
  
    IF P_IN_USER_ROLE = 'ROLE_ADMIN' THEN
    
      IF V_ROLE_COUNT = 0 THEN
        INSERT INTO USER_ROLE
          (ROLEID, USERID, CREATED_DATE_TIME)
        VALUES
          ((SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_ADMIN'),
           (SELECT USERID FROM USERS WHERE USERNAME = P_IN_USERNAME),
           SYSDATE);
      END IF;
    
    ELSIF P_IN_USER_ROLE = 'ROLE_USER' THEN
    
      IF V_ROLE_COUNT = 1 THEN
        DELETE FROM USER_ROLE
         WHERE ROLEID =
               (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_ADMIN')
           AND USERID =
               (SELECT USERID FROM USERS WHERE USERNAME = P_IN_USERNAME);
      END IF;
    
    END IF;
  
    P_OUT_STATUS_NUMBER := 1;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_CHECK_USER_ROLE_BY_USERNAME;
  
  
   PROCEDURE SP_DELETE_USER(P_IN_USERID         IN USERS.USERID%TYPE,
                            P_IN_PURPOSE        IN VARCHAR2,
                            P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
                                           
   BEGIN
         
     DELETE FROM PWD_HINT_ANSWERS WHERE USERID = P_IN_USERID;
     DELETE FROM USER_ROLE WHERE USERID = P_IN_USERID;
     
     IF P_IN_PURPOSE = 'eduCenterUsers' THEN
        DELETE FROM EDU_CENTER_USER_LINK WHERE USERID = P_IN_USERID;
     ELSE
        DELETE FROM ORG_USERS WHERE USERID =  P_IN_USERID;
     END IF;
     
     --DELETE FROM USER_ACTIVITY_HISTORY WHERE USERID = P_IN_USERID;
     DELETE FROM PASSWORD_HISTORY WHERE USERID = P_IN_USERID;
     DELETE FROM USERS WHERE USERID =P_IN_USERID;
     
    
     
  EXCEPTION
    WHEN OTHERS THEN
         P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));  
                                 
   END SP_DELETE_USER;                                        
  

END PKG_ADMIN_MODULE;
/
