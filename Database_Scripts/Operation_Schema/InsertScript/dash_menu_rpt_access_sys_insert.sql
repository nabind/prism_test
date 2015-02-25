BEGIN

  FOR REC IN (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK
               WHERE CUSTOMERID IN (1005, 1006, 1007,1008)) LOOP
  
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
      (101,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Generic System Configuration'),
       2,
       1,
       REC.CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Generic System Configuration'),
       'AC',
       SYSDATE);
  
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
      (101,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Product Specific System Configuration'),
       2,
       1,
       REC.CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Product Specific System Configuration'),
       'AC',
       SYSDATE);
  END LOOP;

END;
