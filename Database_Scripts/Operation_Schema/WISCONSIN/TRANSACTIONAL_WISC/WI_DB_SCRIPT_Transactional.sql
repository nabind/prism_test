
-- Create sequence 
create sequence SEQ_WI_PROCESS_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


-- Create sequence 
create sequence SEQ_TASK_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

alter table ORG_NODE_DIM
  drop constraint CHK_ORG_MODE;

alter table ORG_NODE_DIM
  add constraint CHK_ORG_MODE
  check (ORG_MODE IN ('PUBLIC','PRIVATE','CHOICE','-99'));

  
ALTER TABLE STUDENT_BIO_DIM ADD CONSTRAINT UNI_STUDENT_ID UNIQUE (TEST_ELEMENT_ID,CUSTOMERID,ADMINID);  

ALTER TABLE STG_ORG_NODE_DIM ADD TASK_ID NUMBER;

--Procedure
PRC_MV_REFRESH_STUD_DETAILS.prc

ALTER TABLE ORG_NODE_DIM_BKP MODIFY ORG_MODE VARCHAR2(20);
ALTER TABLE ORG_NODE_DIM MODIFY ORG_MODE VARCHAR2(20);
ALTER TABLE STUDENT_BIO_DIM MODIFY STUDENT_MODE VARCHAR2(2);

ALTER TABLE STG_ORG_LSTNODE_LINK ADD STG_ORG_LSTNODE_ID NUMBER;
ALTER TABLE ITEM_SCORE_FACT MODIFY OBJECTIVEID NULL;
ALTER TABLE ITEM_SCORE_FACT MODIFY READID NULL; 


--WI_SUBTEST_CONFIG
CREATE TABLE WI_SUBTEST_CONFIG AS
SELECT P.PRODUCTID,
       P.PRODUCT_CODE,
       A.ASSESSMENTID,
       (SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y') AS ADMINID,
       C.CONTENTID,
       S.SUBTESTID,
       SYSDATE AS DATETIMESTAMP
  FROM PRODUCT P,
       ASSESSMENT_DIM A,
       CONTENT_DIM C,
       SUBTEST_DIM S
 WHERE P.PRODUCTID = A.PRODUCTID
   AND A.ASSESSMENTID = C.ASSESSMENTID
   AND C.CONTENTID = S.CONTENTID;

--GRD_FRM_LVL_MAP   
CREATE MATERIALIZED VIEW GRD_FRM_LVL_MAP
REFRESH COMPLETE ON DEMAND
AS
SELECT F.FORMID,
       F.FORM_NAME,
       F.FORM_CODE,
       G.GRADEID ,
       L.LEVELID ,
       L.ASSESSMENTID
FROM FORM_DIM F,GRADE_LEVEL_MAP G,LEVEL_MAP L
WHERE L.FORMID = F.FORMID
  AND L.LEVEL_MAPID = G.LEVEL_MAPID;   
  
--STUDENT_MAP  
ALTER TABLE STUDENT_MAP ADD GRADEID NUMBER;

CREATE TABLE STUDENT_TEMP_DATA (STUDENT_BIO_ID NUMBER,DATETIMESTAMP DATE);

--WI_RAW_DATA: FIRST STAGING TABLE
CREATE TABLE STG_WI_GRT_DATA
(
STG_BIO_ID NUMBER PRIMARY KEY,
ADMIN_YEAR NUMBER,
GRADE VARCHAR2(2),
CESA VARCHAR2(2),
SCHOOL_TYPE NUMBER,
DISTRICT_NAME VARCHAR2(50),
DISTRICT_NUMBER VARCHAR2(4),
DISTRICT_AGENCY_TYPE VARCHAR2(2),
DISTRICT_AGENCY_KEY VARCHAR2(5),
SCHOOL_NAME VARCHAR2(50),
SCHOOL_NUMBER VARCHAR2(4),
SCHOOL_AGENCY_TYPE VARCHAR2(2),
SCHOOL_AGENCY_KEY VARCHAR2(5),
DISTRICT_RESIDENCE_NUMBER VARCHAR2(4),
DISTRICT_RESIDENCE_AGENCY_TYPE VARCHAR2(2),
SCHOOL_RESIDENCE_NUMBER VARCHAR2(4),
SCHOOL_RESIDENCE_AGENCY_TYPE VARCHAR2(2),
WI_STUDENT_NUMBER VARCHAR2(10),
STUDENT_LAST_NAME VARCHAR2(35),
STUDENT_FIRST_NAME VARCHAR2(35),
STUDENT_MIDDLE_NAME VARCHAR2(35),
LOCAL_STUDENT_ID VARCHAR2(30),
DOB VARCHAR2(8),
GENDER VARCHAR2(1),
ETHNICITY VARCHAR2(1),
SCHOOL_ENROLLED VARCHAR2(1),
DISTRICT_ENROLLED VARCHAR2(1),
DISTRICT_IEP_PLACEMENT VARCHAR2(1),
NEW_STUDENT_TO_COUNTRY VARCHAR2(1),
DISABILITY_STATUS VARCHAR2(1),
PRIMARY_DISABILITY_CODE VARCHAR2(3),
EP_STATUS VARCHAR2(1),
EP VARCHAR2(1),
ECONOMIC_STATUS VARCHAR2(1),
MIGRANT_STATUS VARCHAR2(1),
REASON_NOT_TESTED_ELA VARCHAR2(3),
REASON_NOT_TESTED_MATH VARCHAR2(3),
REASON_NOT_TESTED_SCI VARCHAR2(3),
REASON_NOT_TESTED_SS VARCHAR2(3),
CHOICE_PROGRAM_PARTICIPANT VARCHAR2(1),
PRECODE_RECORD_SOURCE VARCHAR2(1),
ELA_ATTEMPTED_STATUS VARCHAR2(1),
MATH_ATTEMPTED_STATUS VARCHAR2(1),
SCI_ATTEMPTED_STATUS VARCHAR2(1),
SS_ATTEMPTED_STATUS VARCHAR2(1),
ELA_PROFICIENCY NUMBER,
MATH_PROFICIENCY NUMBER,
SCI_PROFICIENCY NUMBER,
SS_PROFICIENCY NUMBER,
SCALE_SCORE_ELA NUMBER,
SCALE_SCORE_MATH NUMBER,
SCALE_SCORE_SCI NUMBER, 
SCALE_SCORE_SS NUMBER,
STD_ERR_SCALE_SCORE_ELA NUMBER,
STD_ERR_SCALE_SCORE_MATH NUMBER,
STD_ERR_SCALE_SCORE_SCI NUMBER,
STD_ERR_SCALE_SCORE_SS NUMBER,
POINTS_EARNED_ELA_MC NUMBER,
POINTS_EARNED_ELA_AUTOSCORED NUMBER,
POINTS_EARNED_ELA_TDA NUMBER,
POINTS_EARNED_ELA_TOTAL NUMBER,
POINTS_EARNED_MATH_MC NUMBER,
POINTS_EARNED_MATH_AUTOSCORED NUMBER,
POINTS_EARNED_MATH_TOTAL NUMBER,
POINTS_EARNED_SCI_MC NUMBER,
POINTS_EARNED_SCI_AUTOSCORED NUMBER,
POINTS_EARNED_SCI_TOTAL NUMBER,
POINTS_EARNED_SS_MC NUMBER,
POINTS_EARNED_SS_TOTAL NUMBER,
ELA_NCE NUMBER,
MATH_NCE NUMBER,
SCI_NCE NUMBER,
SS_NCE NUMBER,
ELA_PERCENTILE NUMBER,
MATH_PERCENTILE NUMBER,
SCI_PERCENTILE NUMBER,
SS_PERCENTILE NUMBER,
POINTS_EARNED_ELA_A NUMBER,
POINTS_EARNED_ELA_B NUMBER,
POINTS_EARNED_ELA_C NUMBER,
POINTS_EARNED_ELA_D NUMBER,
POINTS_EARNED_ELA_E NUMBER,
POINTS_EARNED_ELA_F NUMBER,
POINTS_EARNED_ELA_G NUMBER,
POINTS_EARNED_MATH_A NUMBER,
POINTS_EARNED_MATH_B NUMBER,
POINTS_EARNED_MATH_C NUMBER,
POINTS_EARNED_MATH_D NUMBER,
POINTS_EARNED_MATH_E NUMBER,
POINTS_EARNED_MATH_F NUMBER,
POINTS_EARNED_MATH_G NUMBER,
POINTS_EARNED_MATH_H NUMBER,
POINTS_EARNED_MATH_I NUMBER,
POINTS_EARNED_MATH_J NUMBER,
POINTS_EARNED_SCI_AB NUMBER,
POINTS_EARNED_SCI_C NUMBER,
POINTS_EARNED_SCI_D NUMBER,
POINTS_EARNED_SCI_E NUMBER,
POINTS_EARNED_SCI_F NUMBER,
POINTS_EARNED_SCI_GH NUMBER,
POINTS_EARNED_SS_A NUMBER,
POINTS_EARNED_SS_B NUMBER,
POINTS_EARNED_SS_C NUMBER,
POINTS_EARNED_SS_D NUMBER,
POINTS_EARNED_SS_E NUMBER,
SPI_ELA_A NUMBER,
SPI_ELA_B NUMBER,
SPI_ELA_C NUMBER,
SPI_ELA_D NUMBER,
SPI_ELA_E NUMBER,
SPI_ELA_F NUMBER,
SPI_ELA_G NUMBER,
SPI_MATH_A NUMBER,
SPI_MATH_B NUMBER,
SPI_MATH_C NUMBER,
SPI_MATH_D NUMBER,
SPI_MATH_E NUMBER,
SPI_MATH_F NUMBER,
SPI_MATH_G NUMBER,
SPI_MATH_H NUMBER,
SPI_MATH_I NUMBER,
SPI_MATH_J NUMBER,
SPI_SCI_AB NUMBER,
SPI_SCI_C NUMBER,
SPI_SCI_D NUMBER,
SPI_SCI_E NUMBER,
SPI_SCI_F NUMBER,
SPI_SCI_GH NUMBER,
SPI_SS_A NUMBER,
SPI_SS_B NUMBER,
SPI_SS_C NUMBER,
SPI_SS_D NUMBER,
SPI_SS_E NUMBER,
SPI_LEVEL_ELA_A NUMBER,
SPI_LEVEL_ELA_B NUMBER,
SPI_LEVEL_ELA_C NUMBER,
SPI_LEVEL_ELA_D NUMBER,
SPI_LEVEL_ELA_E NUMBER,
SPI_LEVEL_ELA_F NUMBER,
SPI_LEVEL_ELA_G NUMBER,
SPI_LEVEL_MATH_A NUMBER,
SPI_LEVEL_MATH_B NUMBER,
SPI_LEVEL_MATH_C NUMBER,
SPI_LEVEL_MATH_D NUMBER,
SPI_LEVEL_MATH_E NUMBER,
SPI_LEVEL_MATH_F NUMBER,
SPI_LEVEL_MATH_G NUMBER,
SPI_LEVEL_MATH_H NUMBER,
SPI_LEVEL_MATH_I NUMBER,
SPI_LEVEL_MATH_J NUMBER,
SPI_LEVEL_SCI_AB NUMBER,
SPI_LEVEL_SCI_C NUMBER,
SPI_LEVEL_SCI_D NUMBER,
SPI_LEVEL_SCI_E NUMBER,
SPI_LEVEL_SCI_F NUMBER,
SPI_LEVEL_SCI_GH NUMBER,
SPI_LEVEL_SS_A NUMBER,
SPI_LEVEL_SS_B NUMBER,
SPI_LEVEL_SS_C NUMBER,
SPI_LEVEL_SS_D NUMBER,
SPI_LEVEL_SS_E NUMBER,
SPI_UP_LMT_ELA_A NUMBER,
SPI_UP_LMT_ELA_B NUMBER,
SPI_UP_LMT_ELA_C NUMBER,
SPI_UP_LMT_ELA_D NUMBER,
SPI_UP_LMT_ELA_E NUMBER,
SPI_UP_LMT_ELA_F NUMBER,
SPI_UP_LMT_ELA_G NUMBER,
SPI_UP_LMT_MATH_A NUMBER,
SPI_UP_LMT_MATH_B NUMBER,
SPI_UP_LMT_MATH_C NUMBER,
SPI_UP_LMT_MATH_D NUMBER,
SPI_UP_LMT_MATH_E NUMBER,
SPI_UP_LMT_MATH_F NUMBER,
SPI_UP_LMT_MATH_G NUMBER,
SPI_UP_LMT_MATH_H NUMBER,
SPI_UP_LMT_MATH_I NUMBER,
SPI_UP_LMT_MATH_J NUMBER,
SPI_UP_LMT_SCI_AB NUMBER,
SPI_UP_LMT_SCI_C NUMBER,
SPI_UP_LMT_SCI_D NUMBER,
SPI_UP_LMT_SCI_E NUMBER,
SPI_UP_LMT_SCI_F NUMBER,
SPI_UP_LMT_SCI_GH NUMBER,
SPI_UP_LMT_SS_A NUMBER,
SPI_UP_LMT_SS_B NUMBER,
SPI_UP_LMT_SS_C NUMBER,
SPI_UP_LMT_SS_D NUMBER,
SPI_UP_LMT_SS_E NUMBER,
SPI_LW_LMT_ELA_A NUMBER,
SPI_LW_LMT_ELA_B NUMBER,
SPI_LW_LMT_ELA_C NUMBER,
SPI_LW_LMT_ELA_D NUMBER,
SPI_LW_LMT_ELA_E NUMBER,
SPI_LW_LMT_ELA_F NUMBER,
SPI_LW_LMT_ELA_G NUMBER,
SPI_LW_LMT_MATH_A NUMBER,
SPI_LW_LMT_MATH_B NUMBER,
SPI_LW_LMT_MATH_C NUMBER,
SPI_LW_LMT_MATH_D NUMBER,
SPI_LW_LMT_MATH_E NUMBER,
SPI_LW_LMT_MATH_F NUMBER,
SPI_LW_LMT_MATH_G NUMBER,
SPI_LW_LMT_MATH_H NUMBER,
SPI_LW_LMT_MATH_I NUMBER,
SPI_LW_LMT_MATH_J NUMBER,
SPI_LW_LMT_SCI_AB NUMBER,
SPI_LW_LMT_SCI_C NUMBER,
SPI_LW_LMT_SCI_D NUMBER,
SPI_LW_LMT_SCI_E NUMBER,
SPI_LW_LMT_SCI_F NUMBER,
SPI_LW_LMT_SCI_GH NUMBER,
SPI_LW_LMT_SS_A NUMBER,
SPI_LW_LMT_SS_B NUMBER,
SPI_LW_LMT_SS_C NUMBER,
SPI_LW_LMT_SS_D NUMBER,
SPI_LW_LMT_SS_E NUMBER,
ELA_MC_ANSWERS VARCHAR2(100),
ELA_AUTOSCORED_ANSWERS VARCHAR2(100),
ELA_TDA_ANSWERS VARCHAR2(20),
MATH_MC_ANSWERS VARCHAR2(100),
MATH_AUTOSCORED_ANSWERS VARCHAR2(100),
SCI_MC_ANSWERS VARCHAR2(100),
SCI_AUTOSCORED_ANSWERS VARCHAR2(100),
SS_MC_ANSWERS VARCHAR2(100),
ELA_CC VARCHAR2(1),
MATH_CC VARCHAR2(1),
SCI_CC VARCHAR2(1),
SS_CC VARCHAR2(1),
ELA_CTC VARCHAR2(1),
MATH_CTC VARCHAR2(1),
SCI_CTC VARCHAR2(1),
SS_CTC VARCHAR2(1),
ELA_RC VARCHAR2(1),
MATH_RC VARCHAR2(1),
SCI_RC VARCHAR2(1),
SS_RC VARCHAR2(1),
ELA_MSK VARCHAR2(1),
MATH_MSK VARCHAR2(1),
SCI_MSK VARCHAR2(1),
SS_MSK VARCHAR2(1),
ELA_TTS VARCHAR2(1),
MATH_TTS VARCHAR2(1),
SCI_TTS VARCHAR2(1),
SS_TTS VARCHAR2(1),
ELA_ST VARCHAR2(1),
MATH_ST VARCHAR2(1),
SCI_ST VARCHAR2(1),
SS_ST VARCHAR2(1),
ELA_VSL VARCHAR2(1),
MATH_VSL VARCHAR2(1),
SCI_VSL VARCHAR2(1),
SS_VSL VARCHAR2(1),
ELA_TTS_READING VARCHAR2(1),
ELA_BRL VARCHAR2(1),
MATH_BRL VARCHAR2(1),
SCI_BRL VARCHAR2(1),
SS_BRL VARCHAR2(1),
ELA_POD VARCHAR2(1),
MATH_POD VARCHAR2(1),
SCI_POD VARCHAR2(1),
SS_POD VARCHAR2(1),
MATH_BD VARCHAR2(1),
SCI_BD VARCHAR2(1),
SS_BD VARCHAR2(1),
ELA_MGF VARCHAR2(1),
MATH_MGF VARCHAR2(1),
SCI_MGF VARCHAR2(1),
SS_MGF VARCHAR2(1),
ELA_NB VARCHAR2(1),
MATH_NB VARCHAR2(1),
SCI_NB VARCHAR2(1),
SS_NB VARCHAR2(1),
ELA_RA VARCHAR2(1),
MATH_RA VARCHAR2(1),
SCI_RA VARCHAR2(1),
SS_RA VARCHAR2(1),
ELA_SCRIBE VARCHAR2(1),
MATH_SCRIBE VARCHAR2(1),
SCI_SCRIBE VARCHAR2(1),
SS_SCRIBE VARCHAR2(1),
ELA_SS VARCHAR2(1),
MATH_SS VARCHAR2(1),
SCI_SS VARCHAR2(1),
SS_SS VARCHAR2(1),
ELA_ARO VARCHAR2(1),
MATH_ARO VARCHAR2(1),
SCI_ARO VARCHAR2(1),
SS_ARO VARCHAR2(1),
MATH_ABACUS VARCHAR2(1),
MATH_CALCULATOR VARCHAR2(1),
MATH_MULT_TABLE VARCHAR2(1),
READING_READ_ALOUD VARCHAR2(1),
BRAILLE VARCHAR2(1),
ELA_TEST_START_DATE VARCHAR2(8),
ELA_TEST_END_DATE VARCHAR2(8),
MATH_TEST_START_DATE VARCHAR2(8),
MATH_TEST_END_DATE VARCHAR2(8),
SCI_TEST_START_DATE VARCHAR2(8),
SCI_TEST_END_DATE VARCHAR2(8),
SS_TEST_START_DATE VARCHAR2(8),
SS_TEST_END_DATE VARCHAR2(8),
DRC_STUDENT_ID VARCHAR2(31),
CUSTOMERID NUMBER,
PRODUCTID NUMBER,
ADMINID NUMBER,
PROCESS_ID NUMBER,
FILE_NAME VARCHAR2(256), 
TASK_ID NUMBER,
IS_FILE_VALID VARCHAR2(1),
VALIDATION_STATUS VARCHAR2(2),
VALIDATION_LOG VARCHAR2(400),
DATETIMESTAMP DATE
);

ALTER TABLE STG_WI_GRT_DATA MODIFY DISTRICT_AGENCY_KEY VARCHAR2(10);
ALTER TABLE STG_WI_GRT_DATA MODIFY SCHOOL_AGENCY_KEY VARCHAR2(10);

--Deploy below Procedure
--PRC_WI_OPR_DATA_REMOVE