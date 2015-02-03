create or replace package PKG_TASC_customer_creation
is
PROCEDURE SP_TASC_CREATE_CUSTOMER_NEW(P_CUSTOMER_NAME  IN VARCHAR2,
                                                    P_CUSTOMER_CODE  IN VARCHAR2,
                                                    P_SEND_LOGIN_PDF IN VARCHAR2,
                                                    P_PP_TPCODE      IN VARCHAR2,
                                                    P_OL_TPCODE      IN VARCHAR2,
                                                    P_DATA_LOC       IN VARCHAR2,
                                                    P_SUPPRT_EMAILS  IN VARCHAR2,
													P_PROJECT_ID  	 IN VARCHAR2,
                                                    OUT_RESULT       OUT VARCHAR2);

PROCEDURE SP_TASC_CREATE_CUSTOMER_GRANT;

end PKG_TASC_customer_creation;
/
create or replace package body PKG_TASC_customer_creation
is

PROCEDURE SP_TASC_CREATE_CUSTOMER_NEW(P_CUSTOMER_NAME  IN VARCHAR2,
                                                    P_CUSTOMER_CODE  IN VARCHAR2,
                                                    P_SEND_LOGIN_PDF IN VARCHAR2,
                                                    P_PP_TPCODE      IN VARCHAR2,
                                                    P_OL_TPCODE      IN VARCHAR2,
                                                    P_DATA_LOC       IN VARCHAR2,
                                                    P_SUPPRT_EMAILS  IN VARCHAR2,
													                          P_PROJECT_ID  	 IN VARCHAR2,
                                                    OUT_RESULT       OUT VARCHAR2) IS
  V_CUSTOMER_ID         NUMBER;
  V_PRODUCT_ID          NUMBER;
  V_CUST_PROD_ID        NUMBER;
  V_ADMIN_ID            NUMBER;
  V_TP_ID               NUMBER;
  V_TP_COUNT            NUMBER;
  V_DASH_ACCESS         NUMBER;
  V_ORG_USER_COUNT      NUMBER;
  V_SCORE_LKP_COUNT     NUMBER;
  V_DEMOID              NUMBER;
  V_ORG_NODE_ID         NUMBER;
  V_DASH_ACTN_LKP_COUNT NUMBER;
  V_OLD_CUSTOMERID      NUMBER;
  V_OLD_CUST_PRODID     NUMBER;
  V_OL_TP_ID            NUMBER;
  V_PP_TP_ID            NUMBER;

  CURSOR C_DEMO IS
    SELECT DEMOID,
           DEMO_NAME,
           DEMO_CODE,
           DEMO_MODE,
           CUSTOMERID,
           SUBTESTID,
           CATEGORY,
           IS_DEMO_VALUE_AVL
      FROM TASC.DEMOGRAPHIC
     WHERE CUSTOMERID = V_OLD_CUSTOMERID;

BEGIN

  Select CUSTOMERID
    INTO V_OLD_CUSTOMERID
    from customer_info
   where CUSTOMER_NAME = 'Demo Customer';

  select CUST_PROD_ID
    into V_OLD_CUST_PRODID
    from cust_product_link
   where customerid = V_OLD_CUSTOMERID;

  SELECT TP_ID
    INTO V_OL_TP_ID
    FROM TEST_PROGRAM
   WHERE CUSTOMERID = V_OLD_CUSTOMERID
     AND TP_MODE = 'OL';

  SELECT TP_ID
    INTO V_PP_TP_ID
    FROM TEST_PROGRAM
   WHERE CUSTOMERID = V_OLD_CUSTOMERID
     AND TP_MODE = 'PP';

  SELECT MAX(CUSTOMERID) INTO V_CUSTOMER_ID FROM PRISMGLOBAL.CUSTOMER_INFO;

  IF V_CUSTOMER_ID IS NULL THEN

    V_CUSTOMER_ID := 1003;

    INSERT INTO prismglobal.CUSTOMER_INFO
      (CUSTOMERID,
       CUSTOMER_NAME,
       DISPLAY_TP_SELECTION,
       FILE_LOCATION,
       DATETIMESTAMP,
       SUPPORT_EMAIL,
       SEND_LOGIN_PDF,
       CUSTOMER_CODE,
	   PROJECTID)
    VALUES
      (1003,
       P_CUSTOMER_NAME,
       'Y',
       P_DATA_LOC,
       SYSDATE,
       P_SUPPRT_EMAILS,
       P_SEND_LOGIN_PDF,
       P_CUSTOMER_CODE,
	   P_PROJECT_ID);

    -- COMMIT;

  ELSE

    V_CUSTOMER_ID := V_CUSTOMER_ID + 1;

    INSERT INTO prismglobal.CUSTOMER_INFO
      (CUSTOMERID,
       CUSTOMER_NAME,
       DISPLAY_TP_SELECTION,
       FILE_LOCATION,
       DATETIMESTAMP,
       SUPPORT_EMAIL,
       SEND_LOGIN_PDF,
       CUSTOMER_CODE,
	   PROJECTID)
    VALUES
      (V_CUSTOMER_ID,
       P_CUSTOMER_NAME,
       'Y',
       P_DATA_LOC,
       SYSDATE,
       P_SUPPRT_EMAILS,
       P_SEND_LOGIN_PDF,
       P_CUSTOMER_CODE,
	   P_PROJECT_ID);

  END IF;

  SELECT PRODUCTID INTO V_PRODUCT_ID FROM PRODUCT WHERE projectid = P_PROJECT_ID;

  SELECT MAX(CUST_PROD_ID)
    INTO V_CUST_PROD_ID
    FROM PRISMGLOBAL.CUST_PRODUCT_LINK;

  SELECT ADMINID INTO V_ADMIN_ID FROM ADMIN_DIM WHERE projectid = P_PROJECT_ID;

  IF V_CUST_PROD_ID IS NULL THEN

    V_CUST_PROD_ID := 3001;

    INSERT INTO prismglobal.CUST_PRODUCT_LINK
      (CUST_PROD_ID,
       CUSTOMERID,
       PRODUCTID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP)
    VALUES
      (V_CUST_PROD_ID,
       V_CUSTOMER_ID,
       V_PRODUCT_ID,
       V_ADMIN_ID,
       'AC',
       SYSDATE);

  ELSE

    V_CUST_PROD_ID := V_CUST_PROD_ID + 1;

    INSERT INTO prismglobal.CUST_PRODUCT_LINK
      (CUST_PROD_ID,
       CUSTOMERID,
       PRODUCTID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP)
    VALUES
      (V_CUST_PROD_ID,
       V_CUSTOMER_ID,
       V_PRODUCT_ID,
       V_ADMIN_ID,
       'AC',
       SYSDATE);

  END IF;

  DELETE FROM NP_MEAN_NCE_LOOKUP WHERE CUST_PROD_ID = V_CUST_PROD_ID;

  --COMMIT;

  INSERT INTO NP_MEAN_NCE_LOOKUP
    (NMN_LOOKUPID, NCE, NP, CUST_PROD_ID, DATETIMESTAMP)
    SELECT NMN_LOOKUPID_SEQ.NEXTVAL, NCE, NP, V_CUST_PROD_ID, DATETIMESTAMP
      FROM NP_MEAN_NCE_LOOKUP
     WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;

  -- COMMIT;

  SELECT MAX(TP_ID) INTO V_TP_ID FROM PRISMGLOBAL.TEST_PROGRAM;

  IF V_TP_ID IS NULL THEN
    V_TP_ID := 1000;
  END IF;

  SELECT COUNT(1)
    INTO V_TP_COUNT
    FROM TEST_PROGRAM
   WHERE CUSTOMERID = V_CUSTOMER_ID;

  IF V_TP_COUNT = 0 THEN

    INSERT INTO prismglobal.TEST_PROGRAM
      (TP_ID,
       TP_CODE,
       TP_NAME,
       TP_TYPE,
       NUM_LEVELS,
       TP_MODE,
       CUSTOMERID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP)
    VALUES
      (V_TP_ID + 1,
       P_PP_TPCODE,
       'TASC PP TP CUSTOMER',
       'PUBLIC',
       3,
       'PP',
       V_CUSTOMER_ID,
       V_ADMIN_ID,
       'AC',
       SYSDATE);

    INSERT INTO ORG_TP_STRUCTURE
      (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
      (SELECT V_TP_ID + 1, ORG_LEVEL, ORG_LABEL, SYSDATE
         FROM ORG_TP_STRUCTURE
        WHERE TP_ID = V_PP_TP_ID);

    INSERT INTO prismglobal.TEST_PROGRAM
      (TP_ID,
       TP_CODE,
       TP_NAME,
       TP_TYPE,
       NUM_LEVELS,
       TP_MODE,
       CUSTOMERID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP)
    VALUES
      (V_TP_ID + 2,
       P_OL_TPCODE,
       'TASC OAS TP CUSTOMER',
       'PUBLIC',
       3,
       'OL',
       V_CUSTOMER_ID,
       V_ADMIN_ID,
       'AC',
       SYSDATE);

    INSERT INTO prismglobal.ORG_TP_STRUCTURE
      (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
      SELECT V_TP_ID + 2, ORG_LEVEL, ORG_LABEL, SYSDATE
        FROM ORG_TP_STRUCTURE
       WHERE TP_ID = V_OL_TP_ID;

  END IF;

  -- COMMIT;

  SELECT COUNT(1)
    INTO V_DASH_ACCESS
    FROM DASH_MENU_RPT_ACCESS
   WHERE CUST_PROD_ID = V_CUST_PROD_ID;

  IF V_DASH_ACCESS = 0 THEN

    INSERT INTO prismglobal.DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
	   PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT DB_MENUID,
             DB_REPORTID,
             ROLEID,
             ORG_LEVEL,
             V_CUST_PROD_ID,
             REPORT_SEQ,
			 P_PROJECT_ID ,
             'AC',
             SYSDATE,
             SYSDATE
        FROM DASH_MENU_RPT_ACCESS
       WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;

  END IF;

  --COMMIT;

  SELECT COUNT(1)
    INTO V_ORG_USER_COUNT
    FROM ORG_USER_DEFINE_LOOKUP
   WHERE CUSTOMERID = V_CUSTOMER_ID;

  IF V_ORG_USER_COUNT = 0 THEN

    INSERT INTO prismglobal.ORG_USER_DEFINE_LOOKUP
      (ROLEID,
       ORG_NODE_LEVEL,
       USER_SEQ,
       USER_NAME,
       USER_PASSWORD,
       CUSTOMERID,
       DATETIMESTAMP)
      SELECT ROLEID,
             ORG_NODE_LEVEL,
             USER_SEQ,
             DECODE(USER_NAME, NULL, NULL, USER_NAME || V_CUSTOMER_ID),
             USER_PASSWORD,
             V_CUSTOMER_ID,
             SYSDATE
        FROM ORG_USER_DEFINE_LOOKUP
       WHERE CUSTOMERID = V_OLD_CUSTOMERID;

  END IF;

  --COMMIT;

  SELECT COUNT(1)
    INTO V_SCORE_LKP_COUNT
    FROM SCORE_TYPE_LOOKUP
   WHERE CUST_PROD_ID = V_CUST_PROD_ID;

  IF V_SCORE_LKP_COUNT = 0 THEN

    INSERT INTO prismglobal.SCORE_TYPE_LOOKUP
      (CATEGORY,
       SCORE_TYPE,
       SCORE_VALUE,
       SCORE_VALUE_NAME,
       CUST_PROD_ID,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT CATEGORY,
             SCORE_TYPE,
             SCORE_VALUE,
             SCORE_VALUE_NAME,
             V_CUST_PROD_ID,
             CREATED_DATE_TIME,
             UPDATED_DATE_TIME
        FROM SCORE_TYPE_LOOKUP
       WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;

  END IF;

  --COMMIT;

  FOR R_DEMO IN C_DEMO LOOP

    SELECT TASC.SEQ_DEMOID.NEXTVAL INTO V_DEMOID FROM DUAL;

    INSERT INTO TASC.DEMOGRAPHIC
      (DEMOID,
       DEMO_NAME,
       DEMO_CODE,
       DEMO_MODE,
       CUSTOMERID,
       SUBTESTID,
       CATEGORY,
       IS_DEMO_VALUE_AVL,
       DATETIMESTAMP)
    VALUES
      (V_DEMOID,
       R_DEMO.DEMO_NAME,
       R_DEMO.DEMO_CODE,
       R_DEMO.DEMO_MODE,
       V_CUSTOMER_ID,
       R_DEMO.SUBTESTID,
       R_DEMO.CATEGORY,
       R_DEMO.IS_DEMO_VALUE_AVL,
       SYSDATE);

    INSERT INTO TASC.DEMOGRAPHIC_VALUES
      (DEMOID,
       DEMO_VALUE_NAME,
       DEMO_VALUE_CODE,
       DEMO_VALUE_SEQ,
       IS_DEFAULT,
       DATETIMESTAMP)
      SELECT V_DEMOID,
             DEMO_VALUE_NAME,
             DEMO_VALUE_CODE,
             DEMO_VALUE_SEQ,
             IS_DEFAULT,
             DATETIMESTAMP
        FROM TASC.DEMOGRAPHIC_VALUES
       WHERE DEMOID = R_DEMO.DEMOID;

  END LOOP;

  --COMMIT;

  SELECT MAX(ORG_NODEID) INTO V_ORG_NODE_ID FROM tASC.ORG_NODE_DIM;

  IF V_ORG_NODE_ID IS NULL THEN
    V_ORG_NODE_ID := 1;

    INSERT INTO TASC.ORG_NODE_DIM
      (ORG_NODEID,
       ORG_NODE_NAME,
       ORG_NODE_CODE,
       ORG_NODE_LEVEL,
       STRC_ELEMENT,
       SPECIAL_CODES,
       ORG_MODE,
       PARENT_ORG_NODEID,
       ORG_NODE_CODE_PATH,
       EMAILS,
       CUSTOMERID,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT 0,
             ORG_NODE_NAME,
             ORG_NODE_CODE,
             ORG_NODE_LEVEL,
             STRC_ELEMENT,
             SPECIAL_CODES,
             ORG_MODE,
             0,
             ORG_NODE_CODE_PATH,
             NULL,
             V_CUSTOMER_ID,
             SYSDATE,
             SYSDATE
        FROM TASC.ORG_NODE_DIM
       WHERE CUSTOMERID = V_OLD_CUSTOMERID
         AND ORG_NODE_LEVEL = 0;

  END IF;

  /*  INSERT INTO ORG_NODE_DIM
  (ORG_NODEID,
   ORG_NODE_NAME,
   ORG_NODE_CODE,
   ORG_NODE_LEVEL,
   STRC_ELEMENT,
   SPECIAL_CODES,
   ORG_MODE,
   PARENT_ORG_NODEID,
   ORG_NODE_CODE_PATH,
   EMAILS,
   CUSTOMERID,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT V_ORG_NODE_ID + 1,
         P_STATE_NAME,
         ORG_NODE_CODE,
         ORG_NODE_LEVEL,
         STRC_ELEMENT,
         SPECIAL_CODES,
         ORG_MODE,
         PARENT_ORG_NODEID,
         ORG_NODE_CODE_PATH,
         NULL,
         V_CUSTOMER_ID,
         SYSDATE,
         SYSDATE
    FROM TASC_MASTER.ORG_NODE_DIM
   WHERE CUSTOMERID = 1001
     AND ORG_NODE_LEVEL = 1;*/

  -- INSERT INTO DASH_ACTION_ACCESS

  SELECT COUNT(1)
    INTO V_DASH_ACTN_LKP_COUNT
    FROM DASH_ACTION_ACCESS
   WHERE CUST_PROD_ID = V_CUST_PROD_ID;

  IF V_DASH_ACTN_LKP_COUNT = 0 THEN

    INSERT INTO prismglobal.DASH_ACTION_ACCESS
      (DB_ACT_ACCESSID,
       DB_MENUID,
       DB_REPORTID,
       DB_ACTIONID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       ACTION_SEQ,
	   PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
      SELECT SEQ_DASH_ACTION_ACCESS.NEXTVAL DB_ACT_ACCESSID,
             DB_MENUID,
             DB_REPORTID,
             DB_ACTIONID,
             ROLEID,
             ORG_LEVEL,
             V_CUST_PROD_ID CUST_PROD_ID,
             ACTION_SEQ,
			 P_PROJECT_ID,
             ACTIVATION_STATUS,
             SYSDATE CREATED_DATE_TIME
        FROM DASH_ACTION_ACCESS
       WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;

  END IF;

  COMMIT;

  OUT_RESULT := 'SUCCESSFUL'; 
  
 -- ROLLBACK ; 

EXCEPTION
  WHEN OTHERS THEN
  dbms_output.put_line(SQLERRM); 
    ROLLBACK;
    OUT_RESULT := 'ERROR';
    dbms_output.put_line('Error ->'|| dbms_utility.format_error_backtrace); 

END SP_TASC_CREATE_CUSTOMER_NEW;


procedure SP_TASC_CREATE_CUSTOMER_GRANT
is
begin
null;
end ;

end PKG_TASC_customer_creation ;
/
