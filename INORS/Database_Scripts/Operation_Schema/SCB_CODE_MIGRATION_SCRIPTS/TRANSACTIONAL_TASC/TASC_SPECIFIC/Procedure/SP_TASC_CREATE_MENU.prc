CREATE OR REPLACE PROCEDURE SP_TASC_CREATE_MENU(P_CUSTOMER_ID NUMBER,
                                                RESULT        OUT VARCHAR2) IS
  V_CUSTOMER_ID     NUMBER;
  V_PRODUCT_ID      NUMBER;
  V_CUST_PROD_ID    NUMBER;
  V_ADMIN_ID        NUMBER;
  V_TP_ID           NUMBER;
  V_TP_COUNT        NUMBER;
  V_DASH_ACCESS     NUMBER;
  V_ORG_USER_COUNT  NUMBER;
  V_SCORE_LKP_COUNT NUMBER;
  V_DEMOID          NUMBER;
  V_ORG_NODE_ID     NUMBER;

/*  CURSOR C_DEMO IS
    SELECT DEMOID,
           DEMO_NAME,
           DEMO_CODE,
           DEMO_MODE,
           CUSTOMERID,
           SUBTESTID,
           CATEGORY,
           IS_DEMO_VALUE_AVL
      FROM TASC_MASTER.DEMOGRAPHIC
     WHERE CUSTOMERID = 1001;*/

BEGIN

  SELECT CUST_PROD_ID
    INTO V_CUST_PROD_ID
    FROM CUST_PRODUCT_LINK
   WHERE CUSTOMERID = P_CUSTOMER_ID;

  DELETE FROM DASH_MENU_RPT_ACCESS WHERE CUST_PROD_ID = V_CUST_PROD_ID;

  INSERT INTO DASH_MENU_RPT_ACCESS
    (DB_MENUID,
     DB_REPORTID,
     ROLEID,
     ORG_LEVEL,
     CUST_PROD_ID,
     REPORT_SEQ,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     UPDATED_DATE_TIME)
    SELECT DB_MENUID,
           DB_REPORTID,
           ROLEID,
           ORG_LEVEL,
           V_CUST_PROD_ID,
           REPORT_SEQ,
           'AC',
           SYSDATE,
           SYSDATE
      FROM TASC_MASTER.DASH_MENU_RPT_ACCESS
     WHERE CUST_PROD_ID = 3001;

  COMMIT;
  RESULT := 1;
  /*EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;*/

END SP_TASC_CREATE_MENU;
/
