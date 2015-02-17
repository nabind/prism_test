/* -- LOOKUP TABLE Run this below insert script before running the whole script. 
   ---insert all values in upper case here
   ----change the value for the PRESENT_VALUE_CHAR column with the balnk source ISTEP and TASC schema
INSERT INTO TASC_LOOKUP_DATA_SWAP(PRESENT_VALUE_CHAR,COLUMN_TYPE) values('ISTEP_PRODCOPY','SOURCE_SCHEMA_ISTEP');

INSERT INTO TASC_LOOKUP_DATA_SWAP(PRESENT_VALUE_CHAR,COLUMN_TYPE) values('TASC_PRODCOPY','SOURCE_SCHEMA_TASC');

*/

--PROJECT_DIM
--SELECT * FROM PROJECT_DIM;
insert into PROJECT_DIM (PROJECTID, PROJECT_NAME, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (2, 'ISTEP', SYSDATE, SYSDATE);
commit;

--ADMIN_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
   INSERT /*+ append */ INTO ADMIN_DIM
   (
        ADMINID,
        ADMIN_NAME,
        ADMIN_SEASON,
        ADMIN_YEAR,
        IS_CURRENT_ADMIN,
        FILE_LOCATION,
        ADMIN_SEQ,
        PROJECTID,
        DATETIMESTAMP   
   ) 
  select ADMINID,
         ADMIN_NAME,
         ADMIN_SEASON,
         ADMIN_YEAR,
         IS_CURRENT_ADMIN,
         FILE_LOCATION,
         ADMIN_SEQ,
         2 PROJECTID,
         DATETIMESTAMP
    from ' || V_SCHEMA_NAME ||'.ADMIN_DIM';

    --commit;
end;

--PRODUCT

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */ INTO PRODUCT
        (PRODUCTID,
         PRODUCT_NAME,
         PRODUCT_TYPE,
         PRODUCT_SEQ,
         PRODUCT_CODE,
         IS_IC_REQUIRED,
         IS_EDITABLE,
         FILE_LOCATION,
         PROJECTID,
         DATETIMESTAMP)
        select PRODUCTID,
               PRODUCT_NAME,
               PRODUCT_TYPE,
               PRODUCT_SEQ,
               PRODUCT_CODE,
               IS_IC_REQUIRED,
               IS_EDITABLE,
               FILE_LOCATION,
               2 PROJECTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || '.PRODUCT ';

  --commit;
end;

--ASSESSMENT_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */ INTO ASSESSMENT_DIM
        (ASSESSMENTID,
         ASSESSMENT_NAME,
         ASSESSMENT_TYPE,
         ASSESSMENT_CODE,
         PRODUCTID,
         DATETIMESTAMP)
        select ASSESSMENTID,
               ASSESSMENT_NAME,
               ASSESSMENT_TYPE,
               ASSESSMENT_CODE,
               PRODUCTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || '.ASSESSMENT_DIM ';

  --commit;
end;

-- FORM_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */ INTO FORM_DIM
        (FORMID, FORM_NAME, FORM_CODE, PROJECTID, DATETIMESTAMP)
        select FORMID, FORM_NAME, FORM_CODE, 2 PROJECTID, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .FORM_DIM ';

  --commit;
end;

--GENDER_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
    INSERT /*+ append */ INTO GENDER_DIM
      (GENDERID, GENDER_NAME, GENDER_CODE, PROJECTID, DATETIMESTAMP)
      select GENDERID, GENDER_NAME, GENDER_CODE, 2 PROJECTID, DATETIMESTAMP
        from ' || V_SCHEMA_NAME || ' .GENDER_DIM ';

  --commit;
end;

--CONTENT_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */ INTO CONTENT_DIM
        (CONTENTID, CONTENT_NAME, CONTENT_SEQ, ASSESSMENTID, DATETIMESTAMP)
        select CONTENTID, CONTENT_NAME, CONTENT_SEQ, ASSESSMENTID, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .CONTENT_DIM ';

  --commit;
end;

--CUSTOMER_INFO

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO CUSTOMER_INFO
        (CUSTOMERID,
         CUSTOMER_NAME,
         DISPLAY_TP_SELECTION,
         FILE_LOCATION,
         SUPPORT_EMAIL,
         SEND_LOGIN_PDF,
         CUSTOMER_CODE,
         PROJECTID,
         DATETIMESTAMP)
        select CUSTOMERID,
               CUSTOMER_NAME,
               DISPLAY_TP_SELECTION,
               FILE_LOCATION,
               SUPPORT_EMAIL,
               NULL SEND_LOGIN_PDF,
               NULL CUSTOMER_CODE,
               2 PROJECTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .CUSTOMER_INFO ';

  --commit;
end;

--CUST_PRODUCT_LINK

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO CUST_PRODUCT_LINK
        (CUST_PROD_ID,
         CUSTOMERID,
         PRODUCTID,
         ADMINID,
         ACTIVATION_STATUS,
         DATETIMESTAMP)
        select CUST_PROD_ID,
               CUSTOMERID,
               PRODUCTID,
               ADMINID,
               ACTIVATION_STATUS,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .CUST_PRODUCT_LINK ';

  --commit;
end;

--GRADE_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO GRADE_DIM
        (GRADEID, GRADE_NAME, GRADE_SEQ, GRADE_CODE, PROJECTID, DATETIMESTAMP)
        select GRADEID,
               GRADE_NAME,
               GRADE_SEQ,
               GRADE_CODE,
               2 PROJECTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .GRADE_DIM ';

  --commit;
end;

--LEVEL_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
    INSERT /*+ append */
    INTO LEVEL_DIM
      (LEVELID, LEVEL_NAME, LEVEL_CODE, PROJECTID, DATETIMESTAMP)
      select LEVELID, LEVEL_NAME, LEVEL_CODE, 2 PROJECTID, DATETIMESTAMP
        from ' || V_SCHEMA_NAME || ' .LEVEL_DIM ';

  --commit;
end;

--LEVEL_MAP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
        INSERT /*+ append */
        INTO LEVEL_MAP
          (LEVEL_MAPID, LEVELID, FORMID, ASSESSMENTID, DATETIMESTAMP)
          select LEVEL_MAPID, LEVELID, FORMID, ASSESSMENTID, DATETIMESTAMP
            from ' || V_SCHEMA_NAME || ' .LEVEL_MAP ';

  --commit;
end;

--GRADE_LEVEL_MAP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO GRADE_LEVEL_MAP
        (GRADEID, LEVEL_MAPID, ONLEVEL_FLAG, DATETIMESTAMP)
        select GRADEID, LEVEL_MAPID, ONLEVEL_FLAG, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .GRADE_LEVEL_MAP ';

  --commit;
end;

--ROLE(Same for ISTEP and TASC)

insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION,CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1, 'ROLE_USER', 'Users',SYSDATE, SYSDATE);
insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION,CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (2, 'ROLE_CTB', 'CTB User',SYSDATE, SYSDATE);
insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION,CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (3, 'ROLE_ADMIN', 'Admin User',SYSDATE, SYSDATE);
insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION,CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (6, 'ROLE_PARENT', 'Parent User',SYSDATE, SYSDATE);
insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION,CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (7, 'ROLE_SUPER', 'Super User',SYSDATE, SYSDATE);
insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION,CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (8, 'ROLE_GRW', 'Growth Report User',SYSDATE, SYSDATE);
insert into ROLE (ROLEID, ROLE_NAME, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (9, 'ROLE_EDU_ADMIN', 'Education Center Admin User',SYSDATE, SYSDATE);
--commit;

--SUBTEST_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
    INSERT /*+ append */
    INTO SUBTEST_DIM
      (SUBTESTID,
       SUBTEST_NAME,
       SUBTEST_SEQ,
       SUBTEST_CODE,
       SUBTEST_TYPE,
       CONTENTID,
       CANDIDATE_SUB_SEQ,
       DATETIMESTAMP)
      select SUBTESTID,
             SUBTEST_NAME,
             SUBTEST_SEQ,
             SUBTEST_CODE,
             SUBTEST_TYPE,
             CONTENTID,
			       NULL CANDIDATE_SUB_SEQ,
             DATETIMESTAMP
        from ' || V_SCHEMA_NAME || ' .SUBTEST_DIM ';

  --commit;
end;

--OBJECTIVE_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
        INSERT /*+ append */
        INTO OBJECTIVE_DIM
          (OBJECTIVEID,
           OBJECTIVE_NAME,
           OBJECTIVE_SEQ,
           OBJECTIVE_TYPE,
           OBJECTIVE_CODE,
           OBJECTIVE_DESC,
           PROJECTID,
           DATETIMESTAMP)
          select OBJECTIVEID,
                 OBJECTIVE_NAME,
                 OBJECTIVE_SEQ,
                 OBJECTIVE_TYPE,
                 OBJECTIVE_CODE,
                 OBJECTIVE_DESC,
                 2 PROJECTID,
                 DATETIMESTAMP
            from ' || V_SCHEMA_NAME || ' .OBJECTIVE_DIM ';

  --commit;
end;

--SUBTEST_OBJECTIVE_MAP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO SUBTEST_OBJECTIVE_MAP
        (SUBT_OBJ_MAPID,
         SUBTESTID,
         OBJECTIVEID,
         LEVEL_MAPID,
         ASSESSMENTID,
         DATETIMESTAMP)
        select SUBT_OBJ_MAPID,
               SUBTESTID,
               OBJECTIVEID,
               LEVEL_MAPID,
               ASSESSMENTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .SUBTEST_OBJECTIVE_MAP ';

  --commit;
end;

--ITEMSET_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
          INSERT /*+ append */
          INTO ITEMSET_DIM
            (ITEMSETID,
             ITEM_NAME,
             ITEM_CODE,
             SESSION_ID,
             ITEM_SEQ,
             POINT_POSSIBLE,
             ITEM_TYPE,
             PDF_FILENAME,
             SUBT_OBJ_MAPID,
             SUBTESTID,
             --OBJECTIVEID,
             ITEM_NUMBER,
             ITEM_PART,
             MODEULEID,
             PROJECTID,
             DATETIMESTAMP)
            select ITEMSETID,
                   ITEM_NAME,
                   ITEM_CODE,
                   SESSION_ID,
                   ITEM_SEQ,
                   POINT_POSSIBLE,
                   ITEM_TYPE,
                   PDF_FILENAME,
                   SUBT_OBJ_MAPID,
                   NULL SUBTESTID,
                   --NULL OBJECTIVEID,
                   ITEM_NUMBER,
                   ITEM_PART,
                   MODEULEID,
                   2 PROJECTID,
                   DATETIMESTAMP
              from ' || V_SCHEMA_NAME || ' .ITEMSET_DIM ';

  --commit;
end;


--TEST_PROGRAM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO TEST_PROGRAM
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
        select TP_ID,
               TP_CODE,
               TP_NAME,
               TP_TYPE,
               NUM_LEVELS,
               TP_MODE,
               CUSTOMERID,
               ADMINID,
               ACTIVATION_STATUS,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .TEST_PROGRAM ';

  --commit;
end;

--ORG_TP_STRUCTURE

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO ORG_TP_STRUCTURE
        (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
        select TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .ORG_TP_STRUCTURE ';

  --commit;
end;

--ORG_USER_DEFINE_LOOKUP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO ORG_USER_DEFINE_LOOKUP
        (ROLEID,
         ORG_NODE_LEVEL,
         USER_SEQ,
         USER_NAME,
         USER_PASSWORD,
         CUSTOMERID,
         DATETIMESTAMP)
        select ROLEID,
               ORG_NODE_LEVEL,
               USER_SEQ,
               USER_NAME,
               USER_PASSWORD,
               CUSTOMERID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .ORG_USER_DEFINE_LOOKUP ';

  --commit;
end;

--PWD_HINT_QUESTIONS(Both ISTEP and TASC)
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (801, 'What was your first pet''s name?', 1, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (802, 'What is your mother''s maiden name?', 2, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (803, 'What street did you live on as a child?', 3, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (804, 'What High School did you attend?', 4, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (668, 'What is your favorite pet''s name?', 5, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (669, 'What are the last 4 digits of your phone number?', 6, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (670, 'What is the name of the street you grew up on?', 7, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (671, 'What is the name of the city you grew up in?', 8, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (672, 'What is the name of your childhood best friend?', 9, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (673, 'What is the name of your birthplace?', 10, 'AC', SYSDATE, SYSDATE);
insert into PWD_HINT_QUESTIONS (PH_QUESTIONID, QUESTION_VALUE, QUESTION_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (674, 'What is your favorite teacher''s name?', 11, 'AC', SYSDATE, SYSDATE);
--commit;

--DASH_MENUS

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 'Reports', 'CUSTOM', 1, 'Reports', 2, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (103, 'Resources', 'CUSTOM', 4, 'Resources', 2, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (102, 'Downloads', 'CUSTOM', 3, 'Downloads', 2, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (104, 'Useful Links', 'CUSTOM', 5, 'Useful Links', 2, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (105, 'Manage', 'CUSTOM', 2, 'Manage', 2, SYSDATE, SYSDATE);
--commit;

--DASH_REPORTS

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO DASH_REPORTS
        (DB_REPORTID,
         REPORT_NAME,
         REPORT_DESC,
         REPORT_TYPE,
         REPORT_FOLDER_URI,
         ACTIVATION_STATUS,
         PROJECTID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        select DB_REPORTID,
               REPORT_NAME,
               REPORT_DESC,
               REPORT_TYPE,
               REPORT_FOLDER_URI,
               ACTIVATION_STATUS,
               2 PROJECTID,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME
          from ' || V_SCHEMA_NAME || ' .DASH_REPORTS ';

  --commit;
end;

--DASH_MENU_RPT_ACCESS

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO DASH_MENU_RPT_ACCESS
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
        select DB_MENUID,
               DB_REPORTID,
               ROLEID,
               ORG_LEVEL,
               CUST_PROD_ID,
               REPORT_SEQ,
               2,
               ACTIVATION_STATUS,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME
          from ' || V_SCHEMA_NAME || ' .DASH_MENU_RPT_ACCESS ';

  --commit;
end;

--DASH_MESSAGE_TYPE

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO DASH_MESSAGE_TYPE
        (MSG_TYPEID,
         MESSAGE_NAME,
         MESSAGE_TYPE,
         DESCRIPTION,
         CUST_PROD_ID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        select MSG_TYPEID,
               MESSAGE_NAME,
               MESSAGE_TYPE,
               DESCRIPTION,
               CUST_PROD_ID,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME
          from ' || V_SCHEMA_NAME || ' .DASH_MESSAGE_TYPE WHERE MSG_TYPEID <> 1085 ';

  --commit;
end;

--DASH_MESSAGES

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO DASH_MESSAGES
        (DB_REPORTID,
         MSG_TYPEID,
         REPORT_MSG,
         CUST_PROD_ID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        select DB_REPORTID,
               MSG_TYPEID,
               REPORT_MSG,
               CUST_PROD_ID,
               ACTIVATION_STATUS,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME
          from ' || V_SCHEMA_NAME || ' .DASH_MESSAGES WHERE MSG_TYPEID <> 1085 ';

  --commit;
end;

--ADHOC DEV LOAD AFTER NEW DML'S are applied
/*INSERT INTO DASH_MESSAGES
SELECT (case
         when DB_REPORTID = 2051 then
          10119
         when DB_REPORTID = 2052 then
          10120
         else
          DB_REPORTID
       end) DB_REPORTID,
       MSG_TYPEID,
       REPORT_MSG,
       CUST_PROD_ID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_MESSAGES a
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM prism_global.DASH_MESSAGE_TYPE
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM'))
   AND DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM prism_global.DASH_REPORTS
         WHERE UPPER(REPORT_NAME) LIKE '%SYSTEM CONFIGURATION')
   and not exists (select 1
          from DASH_MESSAGES d
         where d.DB_REPORTID = a.DB_REPORTID
           and d.MSG_TYPEID = a.MSG_TYPEID
           and d.CUST_PROD_ID = a.CUST_PROD_ID);
*/		   


--ACTIVITY_TYPE(Both ISTEP and TASC)

insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1, 'Login', 'Login', 'Login Activity Tracking', SYSDATE, SYSDATE);
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (2, 'Reports', 'Report', 'Report URL Tracking', SYSDATE, SYSDATE);
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (3, 'Admin', 'Admin', 'User Admin URL Tracking', SYSDATE, SYSDATE);
--commit;

--DASH_CONTRACT_PROP

insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'role.not.added', 'ROLE_CTB,ROLE_PARENT,ROLE_SUPER,ROLE_EDU_ADMIN', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'orglvl.user.not.added', '0', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'orglvl.admin.not.added', '4', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'hmac.secret.key', 'BTCguSF49hYaPmAfe9Q29LtsQ2X', 'CTB.COM|inors', 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'aws.inors.cacheS3', 'Cache_Keys/INORS/', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'password.history.day', '3', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'title.tab.home.application', 'Indiana Online Reporting System', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'title.tab.application', 'Indiana Online Reporting System', null, 2);
insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,DB_PROPERTY_NAME,DB_PROPERY_VALUE,SSO_SOURCE,PROJECTID)
values
  (seq_dash_contract_prop.nextval,'password.expiry','90',null,2);   
insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,DB_PROPERTY_NAME,DB_PROPERY_VALUE,SSO_SOURCE,PROJECTID)
values
  (seq_dash_contract_prop.nextval,'password.expiry.warning','85',null,2); 
insert into dash_contract_prop(db_propertyid,db_property_name,db_propery_value,projectid)
values (seq_dash_contract_prop.nextval,'static.pdf.location','ISTEPREPORTS/qa',2);  
--commit;

--PDF_REPORTS
declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';
   
  EXECUTE IMMEDIATE ' INSERT /*+APPEND*/
INTO PDF_REPORTS
  (PDF_REPORTID,
   ORG_NODE_LEVEL,
   REPORT_NAME,
   REPORT_CODE,
   CUST_PROD_ID,
   ACTIVATION_STATUS,
   DATETIMESTAMP)
  SELECT PDF_REPORTID,
         ORG_NODE_LEVEL,
         REPORT_NAME,
         REPORT_CODE,
         CUST_PROD_ID,
         ACTIVATION_STATUS,
         DATETIMESTAMP
    FROM ' || V_SCHEMA_NAME || '.PDF_REPORTS';

  --commit;
end;

--ETL_PROJECT_CONFIG

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';
   
EXECUTE IMMEDIATE 'INSERT
INTO ETL_PROJECT_CONFIG
  (SEQ_NO,
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
   DATETIMESTAMP,
   PRODUCT_CODE,
   PROJECTID)
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
         DATETIMESTAMP,
         PRODUCT_CODE,
         2
    FROM ' || V_SCHEMA_NAME || '.STG_DATA_LAYOUT_CONFIG';
 END; 
 
COMMIT;	

--ETL_JOBMASTER_CONFIG

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_ISTEP';
   
EXECUTE IMMEDIATE 'INSERT 
INTO ETL_JOBMASTER_CONFIG
  (MAPPING_NAME,
   SESSION_NAME,
   WORKFLOW_NAME,
   REPOSITORY_SERVICE,
   INTEGRATION_SERVICE,
   DOMAIN_NAME,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME,
   PROJECTID)
  SELECT MAPPING_NAME,
         SESSION_NAME,
         WORKFLOW_NAME,
         REPOSITORY_SERVICE,
         INTEGRATION_SERVICE,
         DOMAIN_NAME,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME,
         2
    FROM ' || V_SCHEMA_NAME || '.STG_ETL_JOBMASTER';
 END; 
 
COMMIT;	

