--CREATE VIEW FOR METADATA IN ISTEP
--(remember to change the schema name for global schema) 

--ISTEP
CREATE OR REPLACE VIEW ROLE AS 
SELECT ROLEID, ROLE_NAME, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME
  FROM prism_global.ROLE
WHERE ROLEID<>9;

CREATE OR REPLACE VIEW ADMIN_DIM AS 
SELECT ADMINID,
       ADMIN_NAME,
       ADMIN_SEASON,
       ADMIN_YEAR,
       IS_CURRENT_ADMIN,
       FILE_LOCATION,
       ADMIN_SEQ,
       DATETIMESTAMP
  FROM prism_global.ADMIN_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW CUSTOMER_INFO AS 
SELECT CUSTOMERID,
       CUSTOMER_NAME,
       DISPLAY_TP_SELECTION,
       FILE_LOCATION,
       SUPPORT_EMAIL,
       SEND_LOGIN_PDF,
       CUSTOMER_CODE,
       DATETIMESTAMP
  FROM prism_global.CUSTOMER_INFO
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW DASH_MENUS AS 
SELECT DB_MENUID,
       MENU_NAME,
       MENU_TYPE,
       MENU_SEQ,
       DESCRIPTION,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_MENUS
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW DASH_REPORTS AS 
SELECT DB_REPORTID,
       REPORT_NAME,
       REPORT_DESC,
       REPORT_TYPE,
       REPORT_FOLDER_URI,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_REPORTS
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW FORM_DIM AS 
SELECT FORMID, FORM_NAME, FORM_CODE, DATETIMESTAMP
  FROM prism_global.FORM_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW GENDER_DIM AS 
SELECT GENDERID, GENDER_NAME, GENDER_CODE, DATETIMESTAMP
  FROM prism_global.GENDER_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW GRADE_DIM AS 
SELECT GRADEID, GRADE_NAME, GRADE_SEQ, GRADE_CODE, DATETIMESTAMP
  FROM prism_global.GRADE_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW ITEMSET_DIM AS 
SELECT ITEMSETID,
       ITEM_NAME,
       ITEM_CODE,
       SESSION_ID,
       ITEM_SEQ,
       POINT_POSSIBLE,
       ITEM_TYPE,
       PDF_FILENAME,
       SUBT_OBJ_MAPID,
       DATETIMESTAMP,
       ITEM_NUMBER,
       ITEM_PART,
       MODEULEID,
	   SUBTESTID
  FROM prism_global.ITEMSET_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW LEVEL_DIM AS 
SELECT LEVELID, LEVEL_NAME, LEVEL_CODE, DATETIMESTAMP
  FROM prism_global.LEVEL_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW OBJECTIVE_DIM AS 
SELECT OBJECTIVEID,
       OBJECTIVE_NAME,
       OBJECTIVE_SEQ,
       OBJECTIVE_TYPE,
       OBJECTIVE_CODE,
       OBJECTIVE_DESC,
       DATETIMESTAMP
  FROM prism_global.OBJECTIVE_DIM
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW PRODUCT AS 
SELECT PRODUCTID,
       PRODUCT_NAME,
       PRODUCT_TYPE,
       PRODUCT_SEQ,
       PRODUCT_CODE,
       IS_IC_REQUIRED,
       IS_EDITABLE,
       FILE_LOCATION,
       DATETIMESTAMP
  FROM prism_global.PRODUCT
 WHERE PROJECTID = 2;


CREATE OR REPLACE VIEW READ_DIM AS 
SELECT READID, READ_NAME, READ_CODE, ITEM_TYPE, DATETIMESTAMP
  FROM prism_global.READ_DIM
 WHERE PROJECTID = 2;


CREATE OR REPLACE VIEW DASH_RPT_ACTION AS 
SELECT DB_ACTIONID,
       ACTION_NAME,
       ACTION_TYPE,
       DESCRIPTION,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_RPT_ACTION
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW DASH_CONTRACT_PROP AS 
SELECT DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE
  FROM prism_global.DASH_CONTRACT_PROP
 WHERE PROJECTID = 2;

CREATE OR REPLACE VIEW ASSESSMENT_DIM AS
SELECT ASSESSMENTID,
       ASSESSMENT_NAME,
       ASSESSMENT_TYPE,
       ASSESSMENT_CODE,
       PRODUCTID,
       DATETIMESTAMP
  FROM prism_global.ASSESSMENT_DIM
 WHERE PRODUCTID IN (SELECT PRODUCTID FROM PRODUCT);
		  
CREATE OR REPLACE VIEW CONTENT_DIM AS
SELECT CONTENTID, CONTENT_NAME, CONTENT_SEQ, ASSESSMENTID, DATETIMESTAMP
  FROM prism_global.CONTENT_DIM
 WHERE ASSESSMENTID IN (SELECT ASSESSMENTID FROM ASSESSMENT_DIM);
 
 CREATE OR REPLACE VIEW GRADE_LEVEL_MAP AS
    SELECT 
    GRADEID,
    LEVEL_MAPID,
    ONLEVEL_FLAG,
    DATETIMESTAMP
      FROM prism_global.GRADE_LEVEL_MAP
     WHERE LEVEL_MAPID IN
           (SELECT LEVEL_MAPID
              FROM prism_global.LEVEL_MAP
             WHERE ASSESSMENTID IN
                   (SELECT ASSESSMENTID
                      FROM ASSESSMENT_DIM));
 
 CREATE OR REPLACE VIEW LEVEL_MAP AS
    SELECT 
    LEVEL_MAPID,
    LEVELID,
    FORMID,
    ASSESSMENTID,
    DATETIMESTAMP
      FROM prism_global.LEVEL_MAP
     WHERE ASSESSMENTID IN
           (SELECT ASSESSMENTID
              FROM ASSESSMENT_DIM);
							  

CREATE OR REPLACE VIEW SUBTEST_DIM AS
SELECT SUBTESTID,
       SUBTEST_NAME,
       SUBTEST_SEQ,
       SUBTEST_CODE,
       SUBTEST_TYPE,
       CONTENTID,
	   CANDIDATE_SUB_SEQ,
       DATETIMESTAMP
  FROM prism_global.SUBTEST_DIM
 WHERE CONTENTID IN (SELECT CONTENTID FROM CONTENT_DIM);

CREATE OR REPLACE VIEW SUBTEST_OBJECTIVE_MAP AS
SELECT SUBT_OBJ_MAPID,
       SUBTESTID,
       OBJECTIVEID,
       LEVEL_MAPID,
       ASSESSMENTID,
       DATETIMESTAMP
  FROM prism_global.SUBTEST_OBJECTIVE_MAP
 WHERE ASSESSMENTID IN (SELECT ASSESSMENTID FROM ASSESSMENT_DIM);


CREATE OR REPLACE VIEW DASH_ACTION_ACCESS AS
SELECT DB_ACT_ACCESSID,
       DB_MENUID,
       DB_REPORTID,
       DB_ACTIONID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       ACTION_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_ACTION_ACCESS
 WHERE CUST_PROD_ID IN
       (SELECT CUST_PROD_ID
          FROM prism_global.CUST_PRODUCT_LINK
         WHERE PRODUCTID IN
               (SELECT PRODUCTID FROM prism_global.PRODUCT WHERE PROJECTID = 2));


CREATE OR REPLACE VIEW CUST_PRODUCT_LINK AS
SELECT CUST_PROD_ID,
       CUSTOMERID,
       PRODUCTID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP
  FROM prism_global.CUST_PRODUCT_LINK
 WHERE PRODUCTID IN
       (SELECT PRODUCTID FROM prism_global.PRODUCT WHERE PROJECTID = 2);
		  

CREATE OR REPLACE VIEW TEST_PROGRAM AS
SELECT TP_ID,
       TP_CODE,
       TP_NAME,
       TP_TYPE,
       NUM_LEVELS,
       TP_MODE,
       CUSTOMERID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP
  FROM prism_global.TEST_PROGRAM
 WHERE CUSTOMERID IN
       (SELECT CUSTOMERID FROM prism_global.CUSTOMER_INFO WHERE PROJECTID = 2)
   AND ADMINID IN
       (SELECT ADMINID FROM prism_global.ADMIN_DIM WHERE PROJECTID = 2);


CREATE OR REPLACE VIEW ORG_TP_STRUCTURE AS
SELECT TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP
  FROM prism_global.ORG_TP_STRUCTURE
 WHERE TP_ID IN (SELECT TP_ID FROM TEST_PROGRAM);
		  
		  
/*CREATE OR REPLACE VIEW ROLE_CUSTOMER AS
SELECT ROLEID, CUSTOMERID, CREATED_DATE_TIME, UPDATED_DATE_TIME
  FROM prism_global.ROLE_CUSTOMER
 WHERE ROLEID IN (SELECT ROLEID FROM ROLE)
   AND CUSTOMERID IN (SELECT CUSTOMERID FROM CUSTOMER_INFO);
*/

CREATE OR REPLACE VIEW DASH_MENU_RPT_ACCESS AS
SELECT DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_MENU_RPT_ACCESS
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK);
		  
CREATE OR REPLACE VIEW DASH_MESSAGE_TYPE AS
SELECT MSG_TYPEID,
       MESSAGE_NAME,
       MESSAGE_TYPE,
       DESCRIPTION,
       CUST_PROD_ID,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_MESSAGE_TYPE
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK);
		  

CREATE OR REPLACE VIEW DASH_MESSAGES AS
SELECT DB_REPORTID,
       MSG_TYPEID,
       REPORT_MSG,
       CUST_PROD_ID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_MESSAGES
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK);
		  
CREATE OR REPLACE VIEW CONDITION_CODES AS
SELECT CONDCODE_ID,
       SUBTESTID,
       COND_CODE,
       COND_CODE_NAME,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.CONDITION_CODES
 WHERE SUBTESTID IN (SELECT SUBTESTID FROM SUBTEST_DIM);
		
CREATE OR REPLACE VIEW ORG_USER_DEFINE_LOOKUP AS
SELECT ROLEID,
       ORG_NODE_LEVEL,
       USER_SEQ,
       USER_NAME,
       USER_PASSWORD,
       CUSTOMERID,
       DATETIMESTAMP
  FROM prism_global.ORG_USER_DEFINE_LOOKUP
 WHERE CUSTOMERID IN (SELECT CUSTOMERID FROM CUSTOMER_INFO);
	
CREATE OR REPLACE VIEW NATL_MEAN_SS_LOOKUP AS
SELECT NMS_LOOKUPID,
       GRADEID,
       LEVELID,
       SUBTESTID,
       SS,
       CUST_PROD_ID,
       DATETIMESTAMP
  FROM prism_global.NATL_MEAN_SS_LOOKUP
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK);
		
CREATE OR REPLACE VIEW NP_MEAN_NCE_LOOKUP AS
SELECT NMN_LOOKUPID, NCE, NP, CUST_PROD_ID, DATETIMESTAMP
  FROM prism_global.NP_MEAN_NCE_LOOKUP
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK);
		
CREATE OR REPLACE VIEW NP_NCE_LOOKUP AS
SELECT NN_LOOKUPID, NP, NCE, CUST_PROD_ID, DATETIMESTAMP
  FROM prism_global.NP_NCE_LOOKUP
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK);
 
CREATE OR REPLACE VIEW PDF_REPORTS AS
SELECT PDF_REPORTID,
       ORG_NODE_LEVEL,
       REPORT_NAME,
       REPORT_CODE,
       CUST_PROD_ID,
       ACTIVATION_STATUS,
       DATETIMESTAMP
  FROM prism_global.PDF_REPORTS
 WHERE CUST_PROD_ID IN (SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK); 
 
CREATE OR REPLACE VIEW FTP_CONFIG AS
SELECT FTPID,
       CUSTOMERID,
       FTP_NAME,
       HOST_NAME,
       FILE_PROTOCOL,
       USER_NAME,
       PASSWORD,
       FTP_LOCATION,
       FTP_MODE,
       ACTIVATION_STATUS,
       DATETIMESTAMP
  FROM prism_global.FTP_CONFIG
 WHERE CUSTOMERID IN (SELECT CUSTOMERID FROM CUSTOMER_INFO);
 
CREATE OR REPLACE VIEW STG_ETL_JOBMASTER AS 
SELECT MAPPING_NAME,
       SESSION_NAME,
       WORKFLOW_NAME,
       REPOSITORY_SERVICE,
       INTEGRATION_SERVICE,
       DOMAIN_NAME,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.ETL_JOBMASTER_CONFIG
 WHERE PROJECTID = 2;
 
CREATE OR REPLACE VIEW STG_DATA_LAYOUT_CONFIG AS
SELECT SEQ_NO,
       COLUMN_NAME,
       START_POSITION,
       END_POSITION,
       LENGTH,
       CATEGORY,
       CATEGORY_TYPE,
       CATEGORY_VALUE,
       DATA_EXTRACT_BLOCK,
       REPLACE_FLAG,
       REPLACE_VALUE,
       IS_BIO_UPDATE,
       DESCRIPTION,
       ACTIVATION_STATUS,
       PRODUCT_CODE,
       DATETIMESTAMP
  FROM prism_global.ETL_PROJECT_CONFIG
 WHERE PROJECTID = 2; 
 
