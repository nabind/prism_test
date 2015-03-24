--- (remember to change the name of the individual TASC source schema in this script e.g. change tascprodcopy to TASCPRODCOPY)
--- (remember to change the name of the global schema in this script e.g. change prism_global to prismglobal)
create synonym TASC_LOOKUP_DATA_SWAP for prismglobal.TASC_LOOKUP_DATA_SWAP;

alter trigger TRG_DASH_ACTION_ACCESS disable;
alter trigger TRG_DASH_MENU_RPT_ACCESS disable;
alter trigger TRG_DASH_REPORTS disable;
alter trigger TRG_DASH_RPT_ACTION disable;
alter trigger TRG_DEMO_VALID disable;
alter trigger TRG_ORG_USER_MAP disable;
alter trigger TRG_ORG_USER_MAP_STMT disable;

--ORG_NODE_DIM
INSERT /*+APPEND*/
INTO ORG_NODE_DIM
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
  SELECT ORG_NODEID,
         ORG_NODE_NAME,
         ORG_NODE_CODE,
         ORG_NODE_LEVEL,
         STRC_ELEMENT,
         SPECIAL_CODES,
         ORG_MODE,
         PARENT_ORG_NODEID,
         ORG_NODE_CODE_PATH,
         EMAILS,
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.ORG_NODE_DIM;

COMMIT;

--ORG_PRODUCT_LINK

INSERT /*+APPEND*/
INTO ORG_PRODUCT_LINK
  (ORG_PROD_ID, ORG_NODEID, CUST_PROD_ID, DATETIMESTAMP)
  SELECT ORG_PROD_ID, ORG_NODEID, CUST_PROD_ID, DATETIMESTAMP
    FROM tascprodcopy.ORG_PRODUCT_LINK;

COMMIT;

--ORG_TEST_PROGRAM_LINK

INSERT /*+APPEND*/
INTO ORG_TEST_PROGRAM_LINK
  (ORG_NODEID, TP_ID, DATETIMESTAMP)
  SELECT ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'TP_ID'
                AND PRESENT_VALUE_NUM = TP_ID),
             TP_ID) TP_ID,
         DATETIMESTAMP
    FROM tascprodcopy.ORG_TEST_PROGRAM_LINK;

COMMIT;

--USERS

INSERT /*+APPEND*/
INTO USERS
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
   USER_TYPE,
   ACTIVATION_STATUS,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT USERID,
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
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         IS_FIRSTTIME_LOGIN,
         LAST_LOGIN_ATTEMPT,
         PASSWORD_EXPR_DATE,
         IS_NEW_USER,
         PASSWORD,
         SALT,
         LAST_LOGIN_DATE,
         SIGNED_USER_AGREEMENT,
         AUTO_GENERATED_USER,
         NULL USER_TYPE,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.USERS;

COMMIT;

--ORG_USERS (PKG_PRF_ORG_USR must be present or else trigger will fail)

INSERT /*+APPEND*/
INTO ORG_USERS
  (ORG_USER_ID,
   USERID,
   ORG_NODEID,
   ORG_NODE_LEVEL,
   ADMINID,
   ACTIVATION_STATUS,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT ORG_USER_ID,
         USERID,
         ORG_NODEID,
         ORG_NODE_LEVEL,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.ORG_USERS;

COMMIT;

--EDU_CENTER_DETAILS 

INSERT /*+APPEND*/
INTO EDU_CENTER_DETAILS
  (EDU_CENTERID,
   EDU_CENTER_CODE,
   EDU_CENTER_NAME,
   CUSTOMERID,
   ADDRESS,
   CITY,
   ZIPCODE,
   STATE,
   EMAILS,
   PHONE_NO,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT EDU_CENTERID,
         EDU_CENTER_CODE,
         EDU_CENTER_NAME,
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         ADDRESS,
         CITY,
         ZIPCODE,
         STATE,
         EMAILS,
         PHONE_NO,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.EDU_CENTER_DETAILS;

COMMIT;

--EDU_CENTER_USER_LINK 

INSERT /*+APPEND*/
INTO EDU_CENTER_USER_LINK
  (EDU_CENTERID, USERID,CUST_PROD_ID, DATETIMESTAMP)
 SELECT EDU_CENTERID,
        USERID,
        (SELECT CPL.CUST_PROD_ID
           FROM USERS USR, CUST_PRODUCT_LINK CPL
          WHERE USR.CUSTOMERID = CPL.CUSTOMERID
            AND USR.USERID = E.USERID) CUST_PROD_ID,
        DATETIMESTAMP
   FROM tascprodcopy.EDU_CENTER_USER_LINK E;

COMMIT;

--STUDENT_BIO_DIM 

INSERT /*+APPEND*/
INTO STUDENT_BIO_DIM
  (STUDENT_BIO_ID,
   FIRST_NAME,
   MIDDLE_NAME,
   LAST_NAME,
   BIRTHDATE,
   TEST_ELEMENT_ID,
   INT_STUDENT_ID,
   EXT_STUDENT_ID,
   LITHOCODE,
   GENDERID,
   GRADEID,
   EDU_CENTERID,
   BARCODE,
   SPECIAL_CODES,
   STUDENT_MODE,
   ORG_NODEID,
   CUSTOMERID,
   ADMINID,
   IS_BIO_UPDATE_CMPL,
   PP_IMAGING_ID,
   OAS_IMAGING_ID,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT STUDENT_BIO_ID,
         FIRST_NAME,
         MIDDLE_NAME,
         LAST_NAME,
         BIRTHDATE,
         TEST_ELEMENT_ID,
         INT_STUDENT_ID,
         EXT_STUDENT_ID,
         LITHOCODE,
         (nvl((select SWAPPED_VALUE_NUM
                from TASC_LOOKUP_DATA_SWAP
               where COLUMN_TYPE = 'GENDER_ID'
                 AND PRESENT_VALUE_NUM = GENDERID),
              GENDERID)) GENDERID,
         GRADEID,
         EDU_CENTERID,
         BARCODE,
         SPECIAL_CODES,
         STUDENT_MODE,
         ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         IS_BIO_UPDATE_CMPL,
         NULL PP_IMAGING_ID,
         NULL OAS_IMAGING_ID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.STUDENT_BIO_DIM;

COMMIT;


--SUBTEST_SCORE_FACT 

INSERT /*+APPEND*/
INTO SUBTEST_SCORE_FACT
  (SUBTEST_FACTID,
   ORG_NODEID,
   CUST_PROD_ID,
   ASSESSMENTID,
   STUDENT_BIO_ID,
   CONTENTID,
   SUBTESTID,
   GENDERID,
   GRADEID,
   LEVELID,
   FORMID,
   ADMINID,
   AAGE,
   AANCE,
   AANP,
   AANS,
   AASS,
   ACSIP,
   ACSIS,
   ACSIN,
   CSI,
   CSIL,
   CSIU,
   DIFF,
   GE,
   HSE,
   LEX,
   LEXL,
   LEXU,
   NCE,
   NCR,
   NP,
   NPA,
   NPG,
   NPL,
   NPH,
   NS,
   NSA,
   NSG,
   OM,
   OMS,
   OP,
   OPM,
   PC,
   PL,
   PP,
   PR,
   SEM,
   SNPC,
   SS,
   QTL,
   QTLL,
   QTLU,
   STATUS_CODE,
   TEST_DATE,
   DATETIMESTAMP)
  SELECT SUBTEST_FACTID,
         ORG_NODEID,
         CUST_PROD_ID,
         ASSESSMENTID,
         STUDENT_BIO_ID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CONTENT_ID'
                AND PRESENT_VALUE_NUM = CONTENTID),
             CONTENTID) CONTENTID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,
         (nvl((select SWAPPED_VALUE_NUM
                from TASC_LOOKUP_DATA_SWAP
               where COLUMN_TYPE = 'GENDER_ID'
                 AND PRESENT_VALUE_NUM = GENDERID),
              GENDERID)) GENDERID,
         GRADEID,
         LEVELID,
         FORMID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         AAGE,
         AANCE,
         AANP,
         AANS,
         AASS,
         ACSIP,
         ACSIS,
         ACSIN,
         CSI,
         CSIL,
         CSIU,
         DIFF,
         GE,
         HSE,
         LEX,
         LEXL,
         LEXU,
         NCE,
         NCR,
         NP,
         NPA,
         NPG,
         NPL,
         NPH,
         NS,
         NSA,
         NSG,
         OM,
         OMS,
         OP,
         OPM,
         PC,
         PL,
         PP,
         PR,
         SEM,
         SNPC,
         SS,
         QTL,
         QTLL,
         QTLU,
         STATUS_CODE,
         TEST_DATE,
         DATETIMESTAMP
    FROM tascprodcopy.SUBTEST_SCORE_FACT;

COMMIT;

--OBJECTIVE_SCORE_FACT 

INSERT /*+APPEND*/
INTO OBJECTIVE_SCORE_FACT
  (OBJECTIVE_FACTID,
   ORG_NODEID,
   CUST_PROD_ID,
   ASSESSMENTID,
   STUDENT_BIO_ID,
   CONTENTID,
   SUBTESTID,
   OBJECTIVEID,
   GENDERID,
   GRADEID,
   LEVELID,
   FORMID,
   ADMINID,
   NCR,
   OS,
   OPI,
   OPI_CUT,
   MEAN_IPI,
   OPIQ,
   OPIP,
   PC,
   PP,
   SS,
   PL,
   INRC,
   CONDCODE_ID,
   TEST_DATE,
   DATETIMESTAMP)
  SELECT OBJECTIVE_FACTID,
         ORG_NODEID,
         CUST_PROD_ID,
         ASSESSMENTID,
         STUDENT_BIO_ID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CONTENT_ID'
                AND PRESENT_VALUE_NUM = CONTENTID),
             CONTENTID) CONTENTID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'OBJECTIVE_ID'
                AND PRESENT_VALUE_NUM = OBJECTIVEID),
             OBJECTIVEID) OBJECTIVEID,
         (nvl((select SWAPPED_VALUE_NUM
                from TASC_LOOKUP_DATA_SWAP
               where COLUMN_TYPE = 'GENDER_ID'
                 AND PRESENT_VALUE_NUM = GENDERID),
              GENDERID)) GENDERID,
         GRADEID,
         LEVELID,
         FORMID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         NCR,
         OS,
         OPI,
         NULL OPI_CUT,
         NULL MEAN_IPI,
         OPIQ,
         OPIP,
         PC,
         PP,
         SS,
         PL,
         INRC,
         CONDCODE_ID,
         TEST_DATE,
         DATETIMESTAMP
    FROM tascprodcopy.OBJECTIVE_SCORE_FACT;

COMMIT;

--ITEM_SCORE_FACT 

INSERT /*+APPEND*/
INTO ITEM_SCORE_FACT
  (ITEM_FACTID,
   ORG_NODEID,
   CUST_PROD_ID,
   ASSESSMENTID,
   STUDENT_BIO_ID,
   CONTENTID,
   SUBTESTID,
   OBJECTIVEID,
   GRADEID,
   LEVELID,
   FORMID,
   ADMINID,
   ITEMSETID,
   READID,
   SCORE_VALUES,
   DATETIMESTAMP)
  SELECT ITEM_FACTID,
         ORG_NODEID,
         CUST_PROD_ID,
         ASSESSMENTID,
         STUDENT_BIO_ID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CONTENT_ID'
                AND PRESENT_VALUE_NUM = CONTENTID),
             CONTENTID) CONTENTID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'OBJECTIVE_ID'
                AND PRESENT_VALUE_NUM = OBJECTIVEID),
             OBJECTIVEID) OBJECTIVEID,
         GRADEID,
         LEVELID,
         FORMID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ITEMSET_ID'
                AND PRESENT_VALUE_NUM = ITEMSETID),
             ITEMSETID) ITEMSETID,
         READID,
         SCORE_VALUES,
         DATETIMESTAMP
    FROM tascprodcopy.ITEM_SCORE_FACT;

COMMIT;

--GRADE_SELECTION_LOOKUP 

INSERT /*+APPEND*/
INTO GRADE_SELECTION_LOOKUP
  (ORG_NODEID,
   ASSESSMENTID,
   ADMINID,
   GRADEID,
   LEVELID,
   FORMID,
   DATETIMESTAMP)
  SELECT ORG_NODEID,
         ASSESSMENTID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         GRADEID,
         LEVELID,
         FORMID,
         DATETIMESTAMP
    FROM tascprodcopy.GRADE_SELECTION_LOOKUP;

COMMIT;

--DEMOGRAPHIC 

INSERT /*+APPEND*/
INTO DEMOGRAPHIC
  (DEMOID,
   DEMO_NAME,
   DEMO_CODE,
   DEMO_MODE,
   CUSTOMERID,
   SUBTESTID,
   CATEGORY,
   IS_DEMO_VALUE_AVL,
   DATETIMESTAMP)
  SELECT DEMOID,
         DEMO_NAME,
         DEMO_CODE,
         DEMO_MODE,
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,	
         CATEGORY,
         IS_DEMO_VALUE_AVL,
         DATETIMESTAMP
    FROM tascprodcopy.DEMOGRAPHIC;

COMMIT;

--DEMOGRAPHIC_VALUES 

ALTER TRIGGER TRG_DEMO_VALID DISABLE;

INSERT /*+APPEND*/
INTO DEMOGRAPHIC_VALUES
  (DEMO_VALID,
   DEMOID,
   DEMO_VALUE_NAME,
   DEMO_VALUE_CODE,
   DEMO_VALUE_SEQ,
   IS_DEFAULT,
   DATETIMESTAMP)
  SELECT DEMO_VALID,
         DEMOID,
         DEMO_VALUE_NAME,
         DEMO_VALUE_CODE,
         DEMO_VALUE_SEQ,
         IS_DEFAULT,
         DATETIMESTAMP
    FROM tascprodcopy.DEMOGRAPHIC_VALUES;

COMMIT;

ALTER TRIGGER TRG_DEMO_VALID ENABLE;  

--ORG_LSTNODE_LINK 

INSERT /*+APPEND*/
INTO ORG_LSTNODE_LINK
  (ORG_LSTNODEID, ORG_NODEID, ADMINID, DATETIMESTAMP)
  SELECT ORG_LSTNODEID,
         ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         DATETIMESTAMP
    FROM tascprodcopy.ORG_LSTNODE_LINK;

COMMIT;

--STU_SUBTEST_DEMO_VALUES 

INSERT /*+APPEND*/
INTO STU_SUBTEST_DEMO_VALUES
  (STU_TST_DEMO_VALID,
   STUDENT_BIO_ID,
   SUBTESTID,
   DEMOID,
   DEMO_VALID,
   DATE_TEST_TAKEN,
   DATETIMESTAMP)
  SELECT STU_TST_DEMO_VALID,
         STUDENT_BIO_ID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,
         DEMOID,
         DEMO_VALID,
         DATE_TEST_TAKEN,
         DATETIMESTAMP
    FROM tascprodcopy.STU_SUBTEST_DEMO_VALUES;

COMMIT;

--STUDENT_DEMO_VALUES 

INSERT /*+APPEND*/
INTO STUDENT_DEMO_VALUES
  (STU_DEMO_VALID,
   STUDENT_BIO_ID,
   DEMOID,
   DEMO_VALID,
   DEMO_VALUE,
   DATETIMESTAMP)
  SELECT STU_DEMO_VALID,
         STUDENT_BIO_ID,
         DEMOID,
         DEMO_VALID,
         DEMO_VALUE,
         DATETIMESTAMP
    FROM tascprodcopy.STUDENT_DEMO_VALUES;

COMMIT;

--USER_ROLE 

INSERT /*+APPEND*/
INTO USER_ROLE
  (USERID, ROLEID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
  SELECT USERID, ROLEID, CREATED_DATE_TIME, UPDATED_DATE_TIME
    FROM tascprodcopy.USER_ROLE;

COMMIT;

--PWD_HINT_ANSWERS 

INSERT /*+APPEND*/
INTO PWD_HINT_ANSWERS
  (PH_ANSWERID,
   USERID,
   PH_QUESTIONID,
   ANSWER_VALUE,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT PH_ANSWERID,
         USERID,
         PH_QUESTIONID,
         ANSWER_VALUE,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.PWD_HINT_ANSWERS;

COMMIT;

--USER_ACTIVITY_HISTORY 

INSERT /*+APPEND*/
INTO USER_ACTIVITY_HISTORY
  (ACTIVITYID,
   USERID,
   CUSTOMERID,
   ACTY_TYPEID,
   ACTIVITY_DATE,
   ACTIVITY_DETAILS,
   IP_ADDRESS,
   DATETIMESTAMP)
  SELECT ACTIVITYID,
         USERID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
         ACTY_TYPEID,
         ACTIVITY_DATE,
         ACTIVITY_DETAILS,
         IP_ADDRESS,
         DATETIMESTAMP
    FROM tascprodcopy.USER_ACTIVITY_HISTORY;

COMMIT;

--JOB_TRACKING 

INSERT /*+APPEND*/
INTO JOB_TRACKING
  (JOB_ID,
   USERID,
   JOB_NAME,
   EXTRACT_STARTDATE,
   EXTRACT_ENDDATE,
   EXTRACT_CATEGORY,
   EXTRACT_FILETYPE,
   REQUEST_TYPE,
   REQUEST_SUMMARY,
   REQUEST_DETAILS,
   REQUEST_FILENAME,
   REQUEST_EMAIL,
   JOB_LOG,
   JOB_STATUS,
   ADMINID,
   CUSTOMERID,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME,
   OTHER_REQUEST_PARAMS,
   FILE_SIZE)
  SELECT JOB_ID,
         USERID,
         JOB_NAME,
         EXTRACT_STARTDATE,
         EXTRACT_ENDDATE,
         EXTRACT_CATEGORY,
         EXTRACT_FILETYPE,
         REQUEST_TYPE,
         REQUEST_SUMMARY,
         REQUEST_DETAILS,
         REQUEST_FILENAME,
         REQUEST_EMAIL,
         JOB_LOG,
         JOB_STATUS,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,	
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME,
         OTHER_REQUEST_PARAMS,
         FILE_SIZE
    FROM tascprodcopy.JOB_TRACKING;

COMMIT;

--TABLE_1_STU 

INSERT /*+APPEND*/
INTO TABLE_1_STU
  (STUDENT_BIO_ID,
   FIRST_NAME,
   MIDDLE_NAME,
   LAST_NAME,
   BIRTHDATE,
   TEST_ELEMENT_ID,
   INT_STUDENT_ID,
   EXT_STUDENT_ID,
   STUDENT_ID,
   GENDERID,
   GRADEID,
   EDU_CENTERID,
   ORG_NODEID,
   STUDENT_MODE,
   ADMINID,
   CUSTOMERID,
   SUBTESTID,
   SUBTEST_NAME,
   SUBTEST_SEQ,
   DATE_TEST_TAKEN,
   DEMO_VALUE)
  SELECT STUDENT_BIO_ID,
         FIRST_NAME,
         MIDDLE_NAME,
         LAST_NAME,
         BIRTHDATE,
         TEST_ELEMENT_ID,
         INT_STUDENT_ID,
         EXT_STUDENT_ID,
         STUDENT_ID,
         (nvl((select SWAPPED_VALUE_NUM
                from TASC_LOOKUP_DATA_SWAP
               where COLUMN_TYPE = 'GENDER_ID'
                 AND PRESENT_VALUE_NUM = GENDERID),
              GENDERID)) GENDERID,
         GRADEID,
         EDU_CENTERID,
         ORG_NODEID,
         STUDENT_MODE,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,
         SUBTEST_NAME,
         SUBTEST_SEQ,
         DATE_TEST_TAKEN,
         DEMO_VALUE
    FROM tascprodcopy.TABLE_1_STU;

COMMIT;




--PERF_LOG 

INSERT /*+APPEND*/
INTO PERF_LOG
  (LOG_ID, OBJECT_NAME, OBJECT_TYPE, DETAILS, STATUS, DATETIME)
  SELECT LOG_ID, OBJECT_NAME, OBJECT_TYPE, DETAILS, STATUS, DATETIME
    FROM tascprodcopy.PERF_LOG;

COMMIT;

--ORGUSER_MAPPING_SEARCH 

INSERT /*+APPEND*/
INTO ORGUSER_MAPPING_SEARCH
  (ORG_NODEID,
   ORG_NODE_NAME,
   USERID,
   USERNAME,
   CUSTOMERID,
   HIGHEST_ORG_NODE,
   PARENT_ORG_NODEID,
   ADMINID,
   LOWEST_NODEID,
   ORG_NODE_LEVEL,
   LEVEL8_NAME,
   LEVEL8_CODE,
   EXAMINER_NAME,
   EXAMINER_CODE,
   LEVEL7_NAME,
   LEVEL7_CODE,
   LEVEL6_NAME,
   LEVEL6_CODE,
   LEVEL5_NAME,
   LEVEL5_CODE,
   LEVEL4_NAME,
   LEVEL4_CODE,
   LEVEL3_NAME,
   LEVEL3_CODE,
   LEVEL2_NAME,
   LEVEL2_CODE,
   LEVEL1_NAME,
   LEVEL1_CODE,
   NMBR_OF_ORGS)
  SELECT ORG_NODEID,
         ORG_NODE_NAME,
         USERID,
         USERNAME,
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         HIGHEST_ORG_NODE,
         PARENT_ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,	
         LOWEST_NODEID,
         ORG_NODE_LEVEL,
         LEVEL8_NAME,
         LEVEL8_CODE,
         EXAMINER_NAME,
         EXAMINER_CODE,
         LEVEL7_NAME,
         LEVEL7_CODE,
         LEVEL6_NAME,
         LEVEL6_CODE,
         LEVEL5_NAME,
         LEVEL5_CODE,
         LEVEL4_NAME,
         LEVEL4_CODE,
         LEVEL3_NAME,
         LEVEL3_CODE,
         LEVEL2_NAME,
         LEVEL2_CODE,
         LEVEL1_NAME,
         LEVEL1_CODE,
         NMBR_OF_ORGS
    FROM tascprodcopy.ORGUSER_MAPPING_SEARCH;

COMMIT;

--ORGUSER_MAPPING 

INSERT /*+APPEND*/
INTO ORGUSER_MAPPING
  (ORG_NODEID,
   ORG_NODE_NAME,
   USERID,
   USERNAME,
   CUSTOMERID,
   HIGHEST_ORG_NODE,
   PARENT_ORG_NODEID,
   ADMINID,
   LOWEST_NODEID,
   ORG_NODE_LEVEL,
   LEVEL8_NAME,
   LEVEL8_CODE,
   EXAMINER_NAME,
   EXAMINER_CODE,
   LEVEL7_NAME,
   LEVEL7_CODE,
   LEVEL6_NAME,
   LEVEL6_CODE,
   LEVEL5_NAME,
   LEVEL5_CODE,
   LEVEL4_NAME,
   LEVEL4_CODE,
   LEVEL3_NAME,
   LEVEL3_CODE,
   LEVEL2_NAME,
   LEVEL2_CODE,
   LEVEL1_NAME,
   LEVEL1_CODE,
   NMBR_OF_ORGS)
  SELECT ORG_NODEID,
         ORG_NODE_NAME,
         USERID,
         USERNAME,
         nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
         HIGHEST_ORG_NODE,
         PARENT_ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,	
         LOWEST_NODEID,
         ORG_NODE_LEVEL,
         LEVEL8_NAME,
         LEVEL8_CODE,
         EXAMINER_NAME,
         EXAMINER_CODE,
         LEVEL7_NAME,
         LEVEL7_CODE,
         LEVEL6_NAME,
         LEVEL6_CODE,
         LEVEL5_NAME,
         LEVEL5_CODE,
         LEVEL4_NAME,
         LEVEL4_CODE,
         LEVEL3_NAME,
         LEVEL3_CODE,
         LEVEL2_NAME,
         LEVEL2_CODE,
         LEVEL1_NAME,
         LEVEL1_CODE,
         NMBR_OF_ORGS
    FROM tascprodcopy.ORGUSER_MAPPING;

COMMIT;


--JOB_PARTITION_STATUS 

INSERT /*+APPEND*/
INTO JOB_PARTITION_STATUS
  (JOB_PARTITION_ID, WKF_PARTITION_NAME, LOAD_MODE, STATUS, DATETIMESTAMP)
  SELECT JOB_PARTITION_ID,
         WKF_PARTITION_NAME,
         LOAD_MODE,
         STATUS,
         DATETIMESTAMP
    FROM tascprodcopy.JOB_PARTITION_STATUS;

COMMIT;

/*--STG_DATA_LAYOUT_CONFIG 

INSERT 
INTO STG_DATA_LAYOUT_CONFIG
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
   DATETIMESTAMP)
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
         DATETIMESTAMP
    FROM tascprodcopy.STG_DATA_LAYOUT_CONFIG;

COMMIT;

--STG_ETL_JOBMASTER 

INSERT 
INTO STG_ETL_JOBMASTER
  (MAPPING_NAME,
   SESSION_NAME,
   WORKFLOW_NAME,
   REPOSITORY_SERVICE,
   INTEGRATION_SERVICE,
   DOMAIN_NAME,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT MAPPING_NAME,
         SESSION_NAME,
         WORKFLOW_NAME,
         REPOSITORY_SERVICE,
         INTEGRATION_SERVICE,
         DOMAIN_NAME,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.STG_ETL_JOBMASTER;

COMMIT;
*/

--STG_HIER_DATA_LAYOUT_CONFIG 

INSERT /*+APPEND*/
INTO STG_HIER_DATA_LAYOUT_CONFIG
  (SEQ_NO,
   COLUMN_NAME,
   START_POSITION,
   END_POSITION,
   LENGTH,
   CATEGORY_TYPE,
   CATEGORY_VALUE,
   DESCRIPTION,
   ACTIVATION_STATUS,
   DATETIMESTAMP)
  SELECT SEQ_NO,
         COLUMN_NAME,
         START_POSITION,
         END_POSITION,
         LENGTH,
         CATEGORY_TYPE,
         CATEGORY_VALUE,
         DESCRIPTION,
         ACTIVATION_STATUS,
         DATETIMESTAMP
    FROM tascprodcopy.STG_HIER_DATA_LAYOUT_CONFIG;

COMMIT;

--STUDENTDATA_EXTRACT 

INSERT /*+APPEND*/
INTO STUDENTDATA_EXTRACT
  (EXTRACTID,
   JOB_ID,
   STUDENTDATA_XML,
   CUSTOMERID,
   TOTAL_STUDENT,
   FILE_NAME,
   FILE_PATH,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME)
  SELECT EXTRACTID,
         JOB_ID,
         STUDENTDATA_XML,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
         TOTAL_STUDENT,
         FILE_NAME,
         FILE_PATH,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.STUDENTDATA_EXTRACT;

COMMIT;
---run tge below block id the above query to populate STUDENTDATA_EXTRACT is takin much time
--to run this block p_insrt_stdn_extrc_data has to be compiled first
/*
begin
 dbms_scheduler.create_job
 (job_name       => 'INSERT_STUDENT_EXTRACT_DATA',
  job_type       => 'PLSQL_BLOCK',
  job_action     =>'BEGIN p_insrt_stdn_extrc_data; END;',
  start_date     => SYSDATE+1/24,
  enabled        => TRUE);
end;*/
----run the below select to check the job status "RUNNING" or completed (i.e. no record)
--SELECT STATE FROM USER_SCHEDULER_JOBS WHERE JOB_NAME='INSERT_STUDENT_EXTRACT_DATA';

--STG_PROCESS_STATUS

INSERT /*+APPEND*/
INTO STG_PROCESS_STATUS
  (PROCESS_ID,
   FILE_NAME,
   SOURCE_SYSTEM,
   HIER_VALIDATION,
   BIO_VALIDATION,
   DEMO_VALIDATION,
   CONTENT_VALIDATION,
   OBJECTIVE_VALIDATION,
   ITEM_VALIDATION,
   WKF_PARTITION_NAME,
   PROCESS_LOG,
   DATETIMESTAMP)
  SELECT PROCESS_ID,
         FILE_NAME,
         SOURCE_SYSTEM,
         HIER_VALIDATION,
         BIO_VALIDATION,
         DEMO_VALIDATION,
         CONTENT_VALIDATION,
         OBJECTIVE_VALIDATION,
         ITEM_VALIDATION,
         WKF_PARTITION_NAME,
         PROCESS_LOG,
         DATETIMESTAMP
    FROM tascprodcopy.STG_PROCESS_STATUS;

COMMIT;	

--STG_HIER_PROCESS_STATUS

INSERT /*+APPEND*/
INTO STG_HIER_PROCESS_STATUS
  (PROCESS_ID, FILE_NAME, HIER_VALIDATION, PROCESS_LOG, DATETIMESTAMP)
  SELECT PROCESS_ID, FILE_NAME, HIER_VALIDATION, PROCESS_LOG, DATETIMESTAMP
    FROM tascprodcopy.STG_HIER_PROCESS_STATUS;

COMMIT;	

INSERT INTO ORG_NODE_DIM_BKP
  SELECT ORG_NODEID,
         ORG_NODE_NAME,
         ORG_NODE_CODE,
         ORG_NODE_LEVEL,
         STRC_ELEMENT,
         SPECIAL_CODES,
         ORG_MODE,
         PARENT_ORG_NODEID,
         ORG_NODE_CODE_PATH,
         EMAILS,
         NVL((SELECT SWAPPED_VALUE_NUM
               FROM TASC_LOOKUP_DATA_SWAP
              WHERE COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM tascprodcopy.ORG_NODE_DIM_BKP;
	
commit;	

---03/19/2015 --- START **** PARTHA --ADDED SCRIPTS FOR FEW MORE STAGING TABLES THAT ARE NOT THERE INITIALLY

--STG_HIER_DETAILS
INSERT /*+APPEND*/ INTO STG_HIER_DETAILS
SELECT 
STG_HIERARCHY_DETAILS_ID,
ORG_NODE_ID,
ORG_NAME,
ORG_CODE,
ORG_TYPE,
ORG_LEVEL,
MAX_HIERARCHY,
STRC_ELEMENT,
SPECIAL_CODES,
ORG_MODE,
PARENT_ORG_CODE,
ORG_NODE_CODE_PATH,
EMAIL,
nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
ORGTP,
NVL((SELECT SWAPPED_VALUE_NUM
               FROM TASC_LOOKUP_DATA_SWAP
              WHERE COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMER_ID),
             CUSTOMER_ID) CUSTOMER_ID,
PRODUCT_ID,
PROCESS_ID,
WKF_PARTITION_NAME,
DATETIMESTAMP
FROM TASCPRODCOPY.STG_HIER_DETAILS;
--TRUNCATE TABLE STG_HIER_DETAILS;
COMMIT;

--STG_ITEM_RESPONSE_DETAILS
INSERT /*+APPEND*/ INTO STG_ITEM_RESPONSE_DETAILS
SELECT 
STUDENT_BIO_DETAILS_ID,
CONTENT_CODE,
OBJECTIVE_CODE,
TEST_FORM,
GRADE,
ITEM_TYPE,
ITEM_CODE,
ITEM_NAME,
READ_CODE,
SCORE_VALUE,
WKF_PARTITION_NAME,
PROCESS_ID,
DATETIMESTAMP
FROM TASCPRODCOPY.STG_ITEM_RESPONSE_DETAILS;

COMMIT;

--STG_ITEM_RESPONSE_DETAILS
INSERT /*+APPEND*/ INTO STG_LSTNODE_HIER_DETAILS
SELECT 
STU_LSTNODE_HIER_ID,
ORG_NAME,
ORG_CODE,
ORG_TYPE,
ORG_LEVEL,
ORG_NODE_ID,
ORG_NODE_PATH,
WKF_PARTITION_NAME,
DATETIMESTAMP
FROM TASCPRODCOPY.STG_LSTNODE_HIER_DETAILS;
COMMIT;

--STG_ORG_LSTNODE_LINK
INSERT /*+APPEND*/ 
INTO STG_ORG_LSTNODE_LINK
        ( CUSTOMERID,
          ORG_NODE_LEVEL,
          ORG_NODE_CODE_PATH,
          LST_ORG_NODE_LEVEL,
          LST_ORG_NODE_CODE_PATH,
          DATETIMESTAMP
        )
SELECT NVL((SELECT SWAPPED_VALUE_NUM
               FROM TASC_LOOKUP_DATA_SWAP
              WHERE COLUMN_TYPE = 'CUSTOMER_ID'
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
ORG_NODE_LEVEL,
ORG_NODE_CODE_PATH,
LST_ORG_NODE_LEVEL,
LST_ORG_NODE_CODE_PATH,
DATETIMESTAMP
FROM TASCPRODCOPY.STG_ORG_LSTNODE_LINK;
COMMIT;

---STG_ORG_NODE_DIM
INSERT /*+APPEND*/  INTO
      STG_ORG_NODE_DIM
                (ORG_TP,
                STG_ORG_NODE_DIM_ID,
                ORG_NODE_NAME,
                ORG_NODE_LABEL,
                ORG_NODE_CODE,
                ORG_NODE_LEVEL,
                STRC_ELEMENT,
                SPECIAL_CODES,
                ORG_MODE,
                PARENT_ORG_CODE,
                PARENT_ORG_LEVEL,
                ORG_NODE_CODE_PATH,
                EMAIL_1,
                EMAIL_2,
                EMAIL_3,
                CUSTOMER_ID,
                PROCESS_ID,
                DATETIMESTAMP)
        SELECT 
        ORG_TP,
        STG_ORG_NODE_DIM_ID,
        ORG_NODE_NAME,
        ORG_NODE_LABEL,
        ORG_NODE_CODE,
        ORG_NODE_LEVEL,
        STRC_ELEMENT,
        SPECIAL_CODES,
        ORG_MODE,
        PARENT_ORG_CODE,
        PARENT_ORG_LEVEL,
        ORG_NODE_CODE_PATH,
        EMAIL_1,
        EMAIL_2,
        EMAIL_3,
        NVL((SELECT SWAPPED_VALUE_NUM
                       FROM TASC_LOOKUP_DATA_SWAP
                      WHERE COLUMN_TYPE = 'CUSTOMER_ID'
                        AND PRESENT_VALUE_NUM = CUSTOMER_ID),
                     CUSTOMER_ID) CUSTOMER_ID,
        PROCESS_ID,
        DATETIMESTAMP
 FROM TASCPRODCOPY.STG_ORG_NODE_DIM;
  
 COMMIT;      

---STG_STD_DEMO_DETAILS
INSERT /*+APPEND*/ INTO
 STG_STD_DEMO_DETAILS
                (DEMO_CODE,
                DEMO_VALUE,
                STUDENT_BIO_DETAILS_ID,
                CONTENT_CODE,
                NEED_PRISM_CONSUME,
                PROCESS_ID,
                WKF_PARTITION_NAME,
                IS_BIO_UPDATE,
                DATETIMESTAMP)
     SELECT DEMO_CODE,
            DEMO_VALUE,
            STUDENT_BIO_DETAILS_ID,
            CONTENT_CODE,
            NEED_PRISM_CONSUME,
            PROCESS_ID,
            WKF_PARTITION_NAME,
            IS_BIO_UPDATE,
            DATETIMESTAMP           
FROM TASCPRODCOPY.STG_STD_DEMO_DETAILS;

 COMMIT; 

---STG_STD_OBJECTIVE_DETAILS
INSERT /*+APPEND*/ INTO 
       STG_STD_OBJECTIVE_DETAILS(STUDENT_BIO_DETAILS_ID,
                              CONTENT_NAME,
                              TEST_FORM,
                              DATE_TEST_TAKEN,
                              OBJECTIVE_NAME,
                              COND_CODE,
                              NCR,
                              OS,
                              OPI,
                              OPIQ,
                              OPIP,
                              PC,
                              PP,
                              PL,
                              SS,
                              INRC,
                              OBJECTIVE_SS_RANGE,
                              PROCESS_ID,
                              WKF_PARTITION_NAME,
                              DATETIMESTAMP)
      SELECT                        
      STUDENT_BIO_DETAILS_ID,
      CONTENT_NAME,
      TEST_FORM,
      DATE_TEST_TAKEN,
      OBJECTIVE_NAME,
      COND_CODE,
      NCR,
      OS,
      OPI,
      OPIQ,
      OPIP,
      PC,
      PP,
      PL,
      SS,
      INRC,
      OBJECTIVE_SS_RANGE,
      PROCESS_ID,
      WKF_PARTITION_NAME,
      DATETIMESTAMP
FROM TASCPRODCOPY.STG_STD_OBJECTIVE_DETAILS;
COMMIT;

---STG_STD_SUBTEST_DETAILS
INSERT /*+APPEND*/ INTO STG_STD_SUBTEST_DETAILS 
                           (STUDENT_BIO_DETAILS_ID,
                            STATUS_CODE,
                            CONTENT_NAME,
                            SCORING_STATUS,
                            TEST_FORM,
                            DATE_TEST_TAKEN,
                            AAGE,
                            AANCE,
                            AANP,
                            AANS,
                            AASS,
                            ACSIP,
                            ACSIS,
                            ACSIN,
                            CSI,
                            CSIL,
                            CSIU,
                            DIFF,
                            GE,
                            HSE,
                            LEX,
                            LEXL,
                            LEXU,
                            NCE,
                            NCR,
                            NP,
                            NPA,
                            NPG,
                            NPL,
                            NPH,
                            NS,
                            NSA,
                            NSG,
                            OM,
                            OMS,
                            OP,
                            OPM,
                            PC,
                            PL,
                            PP,
                            PR,
                            SEM,
                            SNPC,
                            SS,
                            QTL,
                            QTLL,
                            QTLU,
                            SUBTEST_SS_RANGE,
                            PROCESS_ID,
                            NEED_PRISM_CONSUME,
                            WKF_PARTITION_NAME,
                            DATETIMESTAMP)
        SELECT 
        STUDENT_BIO_DETAILS_ID,
        STATUS_CODE,
        CONTENT_NAME,
        SCORING_STATUS,
        TEST_FORM,
        DATE_TEST_TAKEN,
        AAGE,
        AANCE,
        AANP,
        AANS,
        AASS,
        ACSIP,
        ACSIS,
        ACSIN,
        CSI,
        CSIL,
        CSIU,
        DIFF,
        GE,
        HSE,
        LEX,
        LEXL,
        LEXU,
        NCE,
        NCR,
        NP,
        NPA,
        NPG,
        NPL,
        NPH,
        NS,
        NSA,
        NSG,
        OM,
        OMS,
        OP,
        OPM,
        PC,
        PL,
        PP,
        PR,
        SEM,
        SNPC,
        SS,
        QTL,
        QTLL,
        QTLU,
        SUBTEST_SS_RANGE,
        PROCESS_ID,
        NEED_PRISM_CONSUME,
        WKF_PARTITION_NAME,
        DATETIMESTAMP
        FROM TASCPRODCOPY.STG_STD_SUBTEST_DETAILS;
        
        COMMIT;


---STG_TASC_HIER_EXTRACT
INSERT /*+APPEND*/ INTO  STG_TASC_HIER_EXTRACT
SELECT * FROM TASCPRODCOPY.STG_TASC_HIER_EXTRACT ;

COMMIT; 

---NY_DATA_STUDENT_IMPORT
INSERT /*+APPEND*/ INTO  NY_DATA_STUDENT_IMPORT
SELECT * FROM TASCPRODCOPY.NY_DATA_STUDENT_IMPORT;
COMMIT;

---STG_STD_BIO_DETAILS
INSERT /*+APPEND*/ INTO STG_STD_BIO_DETAILS
                    ( STUDENT_BIO_DETAILS_ID,
                      FIRST_NAME,
                      BIOFLAG_FIRST_NAME,
                      MIDDLE_NAME,
                      BIOFLAG_MIDDLE_NAME,
                      LAST_NAME,
                      BIOFLAG_LAST_NAME,
                      BIRTHDATE,
                      BIOFLAG_BIRTHDATE,
                      GENDER,
                      BIOFLAG_GENDER,
                      GRADE,
                      BIOFLAG_GRADE,
                      EDU_CENTER,
                      BIOFLAG_EDU_CENTER,
                      BARCODE,
                      BIOFLAG_BARCODE,
                      SPECIAL_CODES,
                      BIOFLAG_SPECIAL_CODES,
                      STUDENT_MODE,
                      STRUC_ELEMENT,
                      BIOFLAG_STRUC_ELEMENT,
                      TEST_ELEMENT_ID,
                      INT_STUDENT_ID,
                      BIOFLAG_INT_STUDENT_ID,
                      EXT_STUDENT_ID,
                      BIOFLAG_EXT_STUDENT_ID,
                      LITHOCODE,
                      BIOFLAG_LITHOCODE,
                      STU_LSTNODE_HIER_ID,
                      IS_BIO_UPDATE_CMPL,
                      PROCESS_ID,
                      NEED_PRISM_CONSUME,
                      WKF_PARTITION_NAME,
                      DATETIMESTAMP
                      )
SELECT STUDENT_BIO_DETAILS_ID,
        FIRST_NAME,
        BIOFLAG_FIRST_NAME,
        MIDDLE_NAME,
        BIOFLAG_MIDDLE_NAME,
        LAST_NAME,
        BIOFLAG_LAST_NAME,
        BIRTHDATE,
        BIOFLAG_BIRTHDATE,
        GENDER,
        BIOFLAG_GENDER,
        GRADE,
        BIOFLAG_GRADE,
        EDU_CENTER,
        BIOFLAG_EDU_CENTER,
        BARCODE,
        BIOFLAG_BARCODE,
        SPECIAL_CODES,
        BIOFLAG_SPECIAL_CODES,
        STUDENT_MODE,
        STRUC_ELEMENT,
        BIOFLAG_STRUC_ELEMENT,
        TEST_ELEMENT_ID,
        INT_STUDENT_ID,
        BIOFLAG_INT_STUDENT_ID,
        EXT_STUDENT_ID,
        BIOFLAG_EXT_STUDENT_ID,
        LITHOCODE,
        BIOFLAG_LITHOCODE,
        STU_LSTNODE_HIER_ID,
        IS_BIO_UPDATE_CMPL,
        PROCESS_ID,
        NEED_PRISM_CONSUME,
        WKF_PARTITION_NAME,
        DATETIMESTAMP
         FROM TASCPRODCOPY.STG_STD_BIO_DETAILS;


---03/19/2015 --- END **** PARTHA --ADDED SCRIPTS FOR FEW MORE STAGING TABLES THAT ARE NOT THERE INITIALLY

alter trigger TRG_DASH_ACTION_ACCESS enable;
alter trigger TRG_DASH_MENU_RPT_ACCESS enable;
alter trigger TRG_DASH_REPORTS enable;
alter trigger TRG_DASH_RPT_ACTION enable;
alter trigger TRG_DEMO_VALID enable;
alter trigger TRG_ORG_USER_MAP enable;
alter trigger TRG_ORG_USER_MAP_STMT enable;


drop synonym TASC_LOOKUP_DATA_SWAP;
