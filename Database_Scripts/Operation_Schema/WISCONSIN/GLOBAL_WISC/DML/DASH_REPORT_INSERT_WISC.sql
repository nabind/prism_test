--FOR WISC
SET DEFINE OFF;
DECLARE
  V_PROJECTID            PROJECT_DIM.PROJECTID%TYPE;
  V_DEFAULT_CUST_PROD_ID CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE := 0;
BEGIN

  SELECT PROJECTID
    INTO V_PROJECTID
    FROM PROJECT_DIM
   WHERE UPPER(PROJECT_NAME) LIKE 'WISCONSIN%';

  DELETE FROM DASH_MENU_RPT_ACCESS
   WHERE PROJECTID = V_PROJECTID
     AND DB_REPORTID IN
         (SELECT DB_REPORTID FROM DASH_REPORTS WHERE PROJECTID = V_PROJECTID);

  DELETE FROM DASH_ACTION_ACCESS
   WHERE PROJECTID = V_PROJECTID
     AND DB_REPORTID IN
         (SELECT DB_REPORTID FROM DASH_REPORTS WHERE PROJECTID = V_PROJECTID);

  DELETE FROM DASH_MESSAGES
   WHERE DB_REPORTID IN
         (SELECT DB_REPORTID FROM DASH_REPORTS WHERE PROJECTID = V_PROJECTID);

  DELETE FROM DASH_REPORTS WHERE PROJECTID = V_PROJECTID;

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     REPORT_FOLDER_URI,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'Manage Reports',
     'Manage Reports',
     'API_LINK',
     'manageReports.do',
     'AC',
     SYSDATE,
     V_PROJECTID);

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'Generic System Configuration',
     'Generic System Configuration',
     'API',
     'IN',
     SYSDATE,
     V_PROJECTID);

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'Product Specific System Configuration',
     'Product Specific System Configuration',
     'API',
     'IN',
     SYSDATE,
     V_PROJECTID);

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     REPORT_FOLDER_URI,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'User''s Guide to Interpreting Reports',
     'User Guide for WISC',
     'API_NFCUSTOM',
     'resourcepdf|resourcepdf.do?pdfFileName=/Static_PDF/Users_Guide_to_Interpreting_Reports.pdf',
     'AC',
     SYSDATE,
     V_PROJECTID);   
     
  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     REPORT_FOLDER_URI,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'http://dpi.wi.gov/assessment/forward',
     'External Link',
     'API_NFCUSTOM',
     'extLinks|http://dpi.wi.gov/assessment/forward',
     'AC',
     SYSDATE,
     V_PROJECTID);   

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     REPORT_FOLDER_URI,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'Student Roster',
     'Student Roster for WISC',
     'API_TABLE',
     '/public/Wisconsin/Report/Student_Roster_files',
     'AC',
     SYSDATE,
     V_PROJECTID);

  INSERT INTO DASH_REPORTS
    (DB_REPORTID,
     REPORT_NAME,
     REPORT_DESC,
     REPORT_TYPE,
     REPORT_FOLDER_URI,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     PROJECTID)
  VALUES
    (DB_REPORT_ID_SEQ.NEXTVAL,
     'Summary Dashboard',
     'Summary Dashboard for WISC',
     'API',
     '/public/Wisconsin/Report/Summary_Dashboard_files',
     'AC',
     SYSDATE,
     V_PROJECTID);

  COMMIT;

END;
/
SET DEFINE ON;