----1 --- RUN THE 12_TASC_PROD_TO_TASC_GRANT.SQL TO GIVE GRANTS FIST
----2 --- THEN CREATE THE BELOW SYNONYM
create synonym TASC_LOOKUP_DATA_SWAP for prismglobal.TASC_LOOKUP_DATA_SWAP;
-----STG_PROCESS_STATUS
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
         DATETIMESTAMP FROM TASCPRODCOPY.STG_PROCESS_STATUS 
MINUS 
SELECT   PROCESS_ID,
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
         DATETIMESTAMP FROM STG_PROCESS_STATUS; 
  
-----STG_DATA_LAYOUT_CONFIG       
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
FROM TASCPRODCOPY.STG_DATA_LAYOUT_CONFIG MINUS SELECT SEQ_NO,
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
 FROM STG_DATA_LAYOUT_CONFIG; 
 
-----STG_ETL_JOBMASTER   
SELECT * FROM TASCPRODCOPY.STG_ETL_JOBMASTER MINUS SELECT * FROM STG_ETL_JOBMASTER ;

-----STG_HIER_DETAILS   
SELECT STG_HIERARCHY_DETAILS_ID,
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
 FROM TASCPRODCOPY.STG_HIER_DETAILS MINUS
SELECT STG_HIERARCHY_DETAILS_ID,
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
ADMINID,
ORGTP,
CUSTOMER_ID,
PRODUCT_ID,
PROCESS_ID,
WKF_PARTITION_NAME,
DATETIMESTAMP FROM STG_HIER_DETAILS; 

-----STG_HIER_DATA_LAYOUT_CONFIG ,STG_HIER_PROCESS_STATUS, STG_ITEM_RESPONSE_DETAILS,STG_LSTNODE_HIER_DETAILS
SELECT * FROM TASCPRODCOPY.STG_HIER_DATA_LAYOUT_CONFIG MINUS SELECT * FROM STG_HIER_DATA_LAYOUT_CONFIG; 
SELECT * FROM TASCPRODCOPY.STG_HIER_PROCESS_STATUS MINUS SELECT * FROM STG_HIER_PROCESS_STATUS; 
SELECT * FROM TASCPRODCOPY.STG_ITEM_RESPONSE_DETAILS MINUS SELECT * FROM STG_ITEM_RESPONSE_DETAILS; 
SELECT * FROM TASCPRODCOPY.STG_LSTNODE_HIER_DETAILS MINUS SELECT * FROM STG_LSTNODE_HIER_DETAILS ;

-----STG_ORG_LSTNODE_LINK 
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
FROM TASCPRODCOPY.STG_ORG_LSTNODE_LINK 
MINUS 
SELECT CUSTOMERID,
          ORG_NODE_LEVEL,
          ORG_NODE_CODE_PATH,
          LST_ORG_NODE_LEVEL,
          LST_ORG_NODE_CODE_PATH,
          DATETIMESTAMP
FROM STG_ORG_LSTNODE_LINK;

-----STG_ORG_NODE_DIM 
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
FROM TASCPRODCOPY.STG_ORG_NODE_DIM 
MINUS 
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
        CUSTOMER_ID,
        PROCESS_ID,
        DATETIMESTAMP 
FROM STG_ORG_NODE_DIM; 

-----STG_STD_DEMO_DETAILS 
SELECT DEMO_CODE,
            DEMO_VALUE,
            STUDENT_BIO_DETAILS_ID,
            CONTENT_CODE,
            NEED_PRISM_CONSUME,
            PROCESS_ID,
            WKF_PARTITION_NAME,
            IS_BIO_UPDATE,
            DATETIMESTAMP FROM TASCPRODCOPY.STG_STD_DEMO_DETAILS 
MINUS 
SELECT   DEMO_CODE,
            DEMO_VALUE,
            STUDENT_BIO_DETAILS_ID,
            CONTENT_CODE,
            NEED_PRISM_CONSUME,
            PROCESS_ID,
            WKF_PARTITION_NAME,
            IS_BIO_UPDATE,
            DATETIMESTAMP
FROM STG_STD_DEMO_DETAILS; 

-----STG_STD_OBJECTIVE_DETAILS 
SELECT STUDENT_BIO_DETAILS_ID,
      CONTENT_NAME,
      TEST_FORM,
      DATE_TEST_TAKEN,
      OBJECTIVE_NAME,
      COND_CODE,
      NCR,
      OS,
      TO_CHAR(OPI) AS OPI,
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
  FROM TASCPRODCOPY.STG_STD_OBJECTIVE_DETAILS 
  MINUS 
SELECT STUDENT_BIO_DETAILS_ID,
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
 FROM STG_STD_OBJECTIVE_DETAILS; 
 
-----STG_STD_SUBTEST_DETAILS  
SELECT STUDENT_BIO_DETAILS_ID,
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
        TO_CHAR(PL) PL,
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
        DATETIMESTAMP FROM TASCPRODCOPY.STG_STD_SUBTEST_DETAILS
         MINUS 
         SELECT STUDENT_BIO_DETAILS_ID,
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
      FROM STG_STD_SUBTEST_DETAILS ; 

-----STG_TASC_HIER_EXTRACT, ORG_USER_MAP_TEMP  , NY_DATA_STUDENT_IMPORT, STG_STUD_DOWNLOAD_DATA_LAYOUT ,STUDENT_CHECK
SELECT * FROM TASCPRODCOPY.STG_TASC_HIER_EXTRACT MINUS SELECT * FROM STG_TASC_HIER_EXTRACT ; 
SELECT * FROM TASCPRODCOPY.ORG_USER_MAP_TEMP MINUS SELECT * FROM ORG_USER_MAP_TEMP; 
SELECT * FROM TASCPRODCOPY.NY_DATA_STUDENT_IMPORT MINUS SELECT * FROM NY_DATA_STUDENT_IMPORT; 
SELECT * FROM TASCPRODCOPY.STG_STUD_DOWNLOAD_DATA_LAYOUT MINUS SELECT * FROM STG_STUD_DOWNLOAD_DATA_LAYOUT ;
SELECT * FROM TASCPRODCOPY.STUDENT_CHECK MINUS SELECT * FROM STUDENT_CHECK; 

-----ORG_NODE_DIM
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
UPDATED_DATE_TIME FROM TASCPRODCOPY.ORG_NODE_DIM 
MINUS 
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
    CUSTOMERID,
    CREATED_DATE_TIME,
    UPDATED_DATE_TIME FROM ORG_NODE_DIM;

-----ORG_PRODUCT_LINK 
SELECT * FROM TASCPRODCOPY.ORG_PRODUCT_LINK MINUS SELECT * FROM ORG_PRODUCT_LINK; 

-----ORG_LSTNODE_LINK 
SELECT ORG_LSTNODEID,
         ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,
         DATETIMESTAMP
FROM TASCPRODCOPY.ORG_LSTNODE_LINK
MINUS 
SELECT ORG_LSTNODEID,
         ORG_NODEID,
          ADMINID,
         DATETIMESTAMP
FROM ORG_LSTNODE_LINK ; 

---ORG_TEST_PROGRAM_LINK
SELECT   ORG_NODEID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'TP_ID'
                AND PRESENT_VALUE_NUM = TP_ID),
             TP_ID) TP_ID,
         DATETIMESTAMP
    FROM TASCPRODCOPY.ORG_TEST_PROGRAM_LINK MINUS SELECT * FROM ORG_TEST_PROGRAM_LINK; 

----USERS    
SELECT  USERID,
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
         UPDATED_DATE_TIME FROM TASCPRODCOPY.USERS MINUS SELECT * FROM USERS; 
 
----ORG_USERS        
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
         UPDATED_DATE_TIME FROM TASCPRODCOPY.ORG_USERS MINUS SELECT * FROM ORG_USERS; 

--- EDU_CENTER_DETAILS      
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
         UPDATED_DATE_TIME FROM TASCPRODCOPY.EDU_CENTER_DETAILS MINUS SELECT * FROM EDU_CENTER_DETAILS;
         
         
------STUDENT_BIO_DIM         
 SELECT STUDENT_BIO_ID,
         FIRST_NAME,
         MIDDLE_NAME,
         LAST_NAME,
         BIRTHDATE,
         TO_CHAR(TEST_ELEMENT_ID) AS TEST_ELEMENT_ID,
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
         UPDATED_DATE_TIME FROM TASCPRODCOPY.STUDENT_BIO_DIM MINUS SELECT * FROM STUDENT_BIO_DIM; 
         
----SUBTEST_SCORE_FACT         
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
         TO_CHAR(PL) AS PL,
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
         DATETIMESTAMP FROM TASCPRODCOPY.SUBTEST_SCORE_FACT MINUS SELECT * FROM SUBTEST_SCORE_FACT; 
         
----OBJECTIVE_SCORE_FACT         
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
         TO_CHAR(OPI) AS OPI,
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
         DATETIMESTAMP FROM TASCPRODCOPY.OBJECTIVE_SCORE_FACT MINUS SELECT * FROM OBJECTIVE_SCORE_FACT; 
         
----ITEM_SCORE_FACT        
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
     FROM TASCPRODCOPY.ITEM_SCORE_FACT MINUS SELECT * FROM ITEM_SCORE_FACT;
      
----GRADE_SELECTION_LOOKUP     
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
         DATETIMESTAMP FROM TASCPRODCOPY.GRADE_SELECTION_LOOKUP MINUS SELECT * FROM GRADE_SELECTION_LOOKUP;
         
----DEMOGRAPHIC          
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
         DATETIMESTAMP FROM TASCPRODCOPY.DEMOGRAPHIC MINUS SELECT * FROM DEMOGRAPHIC;
         
----DEMOGRAPHIC_VALUES          
SELECT * FROM TASCPRODCOPY.DEMOGRAPHIC_VALUES MINUS SELECT * FROM DEMOGRAPHIC_VALUES;
----STU_SUBTEST_DEMO_VALUES 
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
         DATETIMESTAMP FROM TASCPRODCOPY.STU_SUBTEST_DEMO_VALUES 
MINUS SELECT STU_TST_DEMO_VALID,
         STUDENT_BIO_ID,
         SUBTESTID,
         DEMOID,
         DEMO_VALID,
         DATE_TEST_TAKEN,
         DATETIMESTAMP
    FROM STU_SUBTEST_DEMO_VALUES;
 
----STUDENT_PDF_FILES   
----SELECT * FROM TASCPRODCOPY.STUDENT_PDF_FILES MINUS SELECT * FROM STUDENT_PDF_FILES; 

----STUDENT_DEMO_VALUES,ORG_PDF_FILES
SELECT * FROM TASCPRODCOPY.STUDENT_DEMO_VALUES MINUS SELECT * FROM STUDENT_DEMO_VALUES; 
SELECT * FROM TASCPRODCOPY.ORG_PDF_FILES MINUS SELECT * FROM ORG_PDF_FILES; 

--SELECT * FROM TASCPRODCOPY.INVITATION_CODE ; ---NO RECORD
--SELECT * FROM TASCPRODCOPY.INVITATION_CODE_CLAIM ; ---NO RECORD

----USER_ROLE,PWD_HINT_ANSWERS
SELECT * FROM TASCPRODCOPY.USER_ROLE MINUS SELECT * FROM USER_ROLE; 
SELECT * FROM TASCPRODCOPY.PWD_HINT_ANSWERS MINUS SELECT * FROM PWD_HINT_ANSWERS; 

----USER_ACTIVITY_HISTORY
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
         DATETIMESTAMP FROM TASCPRODCOPY.USER_ACTIVITY_HISTORY MINUS SELECT * FROM USER_ACTIVITY_HISTORY; 
         
----JOB_TRACKING         
SELECT COUNT(1) AS CNT FROM TASCPRODCOPY.JOB_TRACKING MINUS SELECT COUNT(1)AS CNT FROM JOB_TRACKING;

SELECT JOB_ID,
         USERID,
         JOB_NAME,
         EXTRACT_STARTDATE,
         EXTRACT_ENDDATE,
         EXTRACT_CATEGORY,
         EXTRACT_FILETYPE,
         REQUEST_TYPE,
         REQUEST_SUMMARY,
         --REQUEST_DETAILS,
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
    FROM TASCPRODCOPY.JOB_TRACKING 
    MINUS SELECT JOB_ID,
        USERID,
        JOB_NAME,
        EXTRACT_STARTDATE,
        EXTRACT_ENDDATE,
        EXTRACT_CATEGORY,
        EXTRACT_FILETYPE,
        REQUEST_TYPE,
        REQUEST_SUMMARY,
        --REQUEST_DETAILS,
        REQUEST_FILENAME,
        REQUEST_EMAIL,
        JOB_LOG,
        JOB_STATUS,
        ADMINID,
        CUSTOMERID,
        CREATED_DATE_TIME,
        UPDATED_DATE_TIME,
        OTHER_REQUEST_PARAMS,
        FILE_SIZE
        FROM JOB_TRACKING;
SELECT JOB_ID,
        USERID,
        JOB_NAME,
        EXTRACT_STARTDATE,
        EXTRACT_ENDDATE,
        EXTRACT_CATEGORY,
        EXTRACT_FILETYPE,
        REQUEST_TYPE,
        REQUEST_SUMMARY,
        --REQUEST_DETAILS,
        REQUEST_FILENAME,
        REQUEST_EMAIL,
        JOB_LOG,
        JOB_STATUS,
        ADMINID,
        CUSTOMERID,
        CREATED_DATE_TIME,
        UPDATED_DATE_TIME,
        OTHER_REQUEST_PARAMS,
        FILE_SIZE
        FROM JOB_TRACKING
MINUS                
SELECT JOB_ID,
         USERID,
         JOB_NAME,
         EXTRACT_STARTDATE,
         EXTRACT_ENDDATE,
         EXTRACT_CATEGORY,
         EXTRACT_FILETYPE,
         REQUEST_TYPE,
         REQUEST_SUMMARY,
         --REQUEST_DETAILS,
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
    FROM TASCPRODCOPY.JOB_TRACKING; 
      
        
SELECT  COUNT(1) FROM TASCPRODCOPY.JOB_TRACKING  A ,JOB_TRACKING B
WHERE A.JOB_ID = B.JOB_ID
AND DBMS_LOB.COMPARE(A.REQUEST_DETAILS,B.REQUEST_DETAILS) <>0; 

----TABLE_1_STU
SELECT STUDENT_BIO_ID,
         FIRST_NAME,
         MIDDLE_NAME,
         LAST_NAME,
         BIRTHDATE,
         TO_CHAR(TEST_ELEMENT_ID) AS TEST_ELEMENT_ID,
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
         DEMO_VALUE FROM TASCPRODCOPY.TABLE_1_STU MINUS SELECT * FROM TABLE_1_STU; 
         
----  STU_DEMO_VAL, STU_SUB_DETAILS     
SELECT * FROM TASCPRODCOPY.STU_DEMO_VAL MINUS SELECT * FROM STU_DEMO_VAL; 
SELECT * FROM TASCPRODCOPY.STU_SUB_DETAILS MINUS SELECT * FROM STU_SUB_DETAILS;

--- STUDENTDATA_EXTRACT
SELECT COUNT(1) AS CNT FROM TASCPRODCOPY.STUDENTDATA_EXTRACT MINUS SELECT COUNT(1) AS CNT FROM STUDENTDATA_EXTRACT;

SELECT EXTRACTID,
         JOB_ID,
         --STUDENTDATA_XML,
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
  FROM TASCPRODCOPY.STUDENTDATA_EXTRACT 
  MINUS
  SELECT EXTRACTID,
         JOB_ID,
         ---STUDENTDATA_XML,
          CUSTOMERID,
         TOTAL_STUDENT,
         FILE_NAME,
         FILE_PATH,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME
    FROM STUDENTDATA_EXTRACT  ; 
 
/*SELECT COUNT(1) AS CNT FROM TASCPRODCOPY.STUDENTDATA_EXTRACT A ,STUDENTDATA_EXTRACT B
WHERE A.EXTRACTID = B.EXTRACTID
 AND DBMS_LOB.COMPARE(A.STUDENTDATA_XML,B.STUDENTDATA_XML) <>0; */ 

---- PERF_LOG   
SELECT * FROM TASCPRODCOPY.PERF_LOG MINUS SELECT * FROM PERF_LOG; 

---- ORGUSER_MAPPING_SEARCH   
SELECT  ORG_NODEID,
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
         NMBR_OF_ORGS FROM TASCPRODCOPY.ORGUSER_MAPPING_SEARCH MINUS SELECT * FROM ORGUSER_MAPPING_SEARCH ;
         
----ORGUSER_MAPPING          
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
         NMBR_OF_ORGS FROM TASCPRODCOPY.ORGUSER_MAPPING MINUS SELECT * FROM ORGUSER_MAPPING; 

----ORGUSER_DETAILS  ,JOB_PARTITION_STATUS      
SELECT * FROM TASCPRODCOPY.ORGUSER_DETAILS MINUS SELECT * FROM ORGUSER_DETAILS; 
SELECT * FROM TASCPRODCOPY.JOB_PARTITION_STATUS MINUS SELECT * FROM JOB_PARTITION_STATUS; 

----EDU_CENTER_USER_LINK 
SELECT EDU_CENTERID,
        USERID,
        (SELECT CPL.CUST_PROD_ID
           FROM USERS USR, CUST_PRODUCT_LINK CPL
          WHERE USR.CUSTOMERID = CPL.CUSTOMERID
            AND USR.USERID = E.USERID) CUST_PROD_ID,
        DATETIMESTAMP FROM TASCPRODCOPY.EDU_CENTER_USER_LINK E MINUS SELECT * FROM EDU_CENTER_USER_LINK; 

-----  STG_STD_BIO_DETAILS      
SELECT                STUDENT_BIO_DETAILS_ID,
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
                      TO_CHAR(TEST_ELEMENT_ID) AS TEST_ELEMENT_ID,
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
  FROM TASCPRODCOPY.STG_STD_BIO_DETAILS 
  MINUS
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
              FROM STG_STD_BIO_DETAILS; 
              
----  ORG_NODE_DIM_BKP            
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
     FROM TASCPRODCOPY.ORG_NODE_DIM_BKP MINUS SELECT * FROM ORG_NODE_DIM_BKP; 
     
     
-----3 ----- DROP THE SYNONYM
drop synonym TASC_LOOKUP_DATA_SWAP;
----4----- REVOKE THE GRANT BY RUNNING  16_TASC_PROD_TO_TASC_REVOKE.SQL    
