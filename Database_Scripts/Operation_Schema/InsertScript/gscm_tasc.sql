DECLARE

  V_DB_REPORTID DASH_REPORTS.DB_REPORTID%TYPE;

BEGIN

  SELECT DB_REPORT_ID_SEQ.NEXTVAL INTO V_DB_REPORTID FROM DUAL;

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (V_DB_REPORTID,
     'Generic System Configuration',
     'Generic System Configuration',
     'API',
     'IN',
     SYSDATE,
     1);

  FOR REC IN (SELECT CPL.CUST_PROD_ID
                FROM CUST_PRODUCT_LINK CPL, CUSTOMER_INFO CI
               WHERE CPL.CUSTOMERID = CI.CUSTOMERID
                 AND CPL.ACTIVATION_STATUS = 'AC'
                 AND CI.PROJECTID = 1) LOOP
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       PROJECTID)
    VALUES
      ((SELECT DB_MENUID
         FROM DASH_MENUS
        WHERE UPPER(MENU_NAME) = 'REPORTS'
          AND PROJECTID = 1),
       V_DB_REPORTID,
       (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_CTB'),
       1,
       REC.CUST_PROD_ID,
       V_DB_REPORTID,
       'AC',
       SYSDATE,
       1);
  END LOOP;

  SELECT DB_REPORT_ID_SEQ.NEXTVAL INTO V_DB_REPORTID FROM DUAL;

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (V_DB_REPORTID,
     'Product Specific System Configuration',
     'Product Specific System Configuration',
     'API',
     'IN',
     SYSDATE,
     1);

  FOR REC IN (SELECT CPL.CUST_PROD_ID
                FROM CUST_PRODUCT_LINK CPL, CUSTOMER_INFO CI
               WHERE CPL.CUSTOMERID = CI.CUSTOMERID
                 AND CPL.ACTIVATION_STATUS = 'AC'
                 AND CI.PROJECTID = 1) LOOP
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       PROJECTID)
    VALUES
      ((SELECT DB_MENUID
         FROM DASH_MENUS
        WHERE UPPER(MENU_NAME) = 'REPORTS'
          AND PROJECTID = 1),
       V_DB_REPORTID,
       (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_CTB'),
       1,
       REC.CUST_PROD_ID,
       V_DB_REPORTID,
       'AC',
       SYSDATE,
       1);
  END LOOP;

END;
/
