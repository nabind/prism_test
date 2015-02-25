CREATE OR REPLACE PROCEDURE SP_CREATE_CTBADMIN(IN_CUSTOMER_ID IN NUMBER) IS

  V_USER_ID        NUMBER;
  V_CUSTOMER_CODE  VARCHAR2(100);
  V_STATE_ID       NUMBER;
  V_USER_SEQ_ID    NUMBER;
  V_ADMIN_ID       NUMBER;
  V_SSO_USERID     NUMBER;
  V_SSO_USER_ORGID NUMBER;

BEGIN
  SELECT CUSTOMERid
    INTO V_CUSTOMER_CODE
    FROM CUSTOMER_INFO
   WHERE CUSTOMERID = IN_CUSTOMER_ID;

  IF V_CUSTOMER_CODE IS NULL THEN
    DBMS_OUTPUT.PUT_LINE('Customer not exists');
  ELSE
    SELECT MAX(U.USERID)
      INTO V_USER_ID
      FROM USERS U, ORG_USERS OU, USER_ROLE UR
     WHERE U.USERID = OU.USERID
       AND OU.USERID = UR.USERID
       AND U.CUSTOMERID = IN_CUSTOMER_ID
       AND UR.ROLEID = 2;

    SELECT MAX(USERID)
      INTO V_SSO_USERID
      FROM USERS
     WHERE UPPER(USERNAME) = 'DUMMYSSOUSER';

    SELECT ORG_NODEID
      INTO V_STATE_ID
      FROM ORG_NODE_DIM
     WHERE CUSTOMERID = IN_CUSTOMER_ID
       AND ORG_NODE_LEVEL = 1
       AND ROWNUM = 1;

    SELECT ADMINID
      INTO V_ADMIN_ID
      FROM ADMIN_DIM
     WHERE IS_CURRENT_ADMIN = 'Y';

  END IF;
  IF V_SSO_USERID IS NULL THEN

    SELECT USER_ID_SEQ.NEXTVAL INTO V_SSO_USER_ORGID FROM DUAL;

    INSERT INTO USERS
      (USERID,
       USERNAME,
       DISPLAY_USERNAME,
       LAST_NAME,
       FIRST_NAME,
       MIDDLE_NAME,
       EMAIL_ADDRESS,
       PHONE_NO,
       STREET,
       COUNTRY,
       CITY,
       ZIPCODE,
       STATE,
       CUSTOMERID,
       IS_FIRSTTIME_LOGIN,
       LAST_LOGIN_ATTEMPT,
       PASSWORD_EXPR_DATE,
       IS_NEW_USER,
       PASSWORD,
       SALT,
       LAST_LOGIN_DATE,
       SIGNED_USER_AGREEMENT,
       AUTO_GENERATED_USER,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
    VALUES
      (V_SSO_USER_ORGID,
       'dummyssouser',
       'ssouser',
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       1001,
       'N',
       NULL,
       NULL,
       'N',
       '9bff29259f90760f6ef3a1d3de988cc3549245a04bf6ede56f6b9a15db01dc6f',
       'hGjF61UY6A0xbbOvAuVu',
       NULL,
       NULL,
       NULL,
       'SS',
       SYSDATE,
       NULL);

    INSERT INTO ORG_USERS
      (ORG_USER_ID,
       USERID,
       ORG_NODEID,
       ORG_NODE_LEVEL,
       ADMINID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
    VALUES
      (org_user_id_seq.nextval,
       V_SSO_USER_ORGID,
       V_STATE_ID,
       1,
       V_ADMIN_ID,
       'AC',
       SYSDATE,
       SYSDATE);

    INSERT INTO USER_ROLE
      (USERID, ROLEID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
    VALUES
      (V_SSO_USER_ORGID, 1, SYSDATE, SYSDATE);

  END IF;

  COMMIT;

  IF V_USER_ID IS NULL THEN

    SELECT USER_ID_SEQ.NEXTVAL INTO V_USER_SEQ_ID FROM DUAL;

    INSERT INTO USERS
      (USERID,
       USERNAME,
       DISPLAY_USERNAME,
       LAST_NAME,
       FIRST_NAME,
       MIDDLE_NAME,
       EMAIL_ADDRESS,
       PHONE_NO,
       STREET,
       COUNTRY,
       CITY,
       ZIPCODE,
       STATE,
       CUSTOMERID,
       IS_FIRSTTIME_LOGIN,
       LAST_LOGIN_ATTEMPT,
       PASSWORD_EXPR_DATE,
       IS_NEW_USER,
       PASSWORD,
       SALT,
       LAST_LOGIN_DATE,
       SIGNED_USER_AGREEMENT,
       AUTO_GENERATED_USER,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
    VALUES
      (V_USER_SEQ_ID,
       'ctbadmin' || IN_CUSTOMER_ID,
       'ctbadm' || V_CUSTOMER_CODE,
       V_CUSTOMER_CODE || 'ISTEP',
       'CTB',
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       NULL,
       IN_CUSTOMER_ID,
       'N',
       NULL,
       NULL,
       'N',
       'ABCD',
       'y5ihlLuNF9v8rekOd2Qy',
       NULL,
       NULL,
       NULL,
       'SS',
       SYSDATE,
       SYSDATE);

    INSERT INTO ORG_USERS
      (ORG_USER_ID,
       USERID,
       ORG_NODEID,
       ORG_NODE_LEVEL,
       ADMINID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
    VALUES
      (org_user_id_seq.nextval,
       V_USER_SEQ_ID,
       V_STATE_ID,
       1,
       V_ADMIN_ID,
       'AC',
       SYSDATE,
       SYSDATE);

    FOR I IN 1 .. 3 LOOP
      INSERT INTO USER_ROLE
        (USERID, ROLEID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
      VALUES
        (V_USER_SEQ_ID, I, SYSDATE, SYSDATE);

    END LOOP;
  END IF;
  COMMIT;

END SP_CREATE_CTBADMIN;
/
