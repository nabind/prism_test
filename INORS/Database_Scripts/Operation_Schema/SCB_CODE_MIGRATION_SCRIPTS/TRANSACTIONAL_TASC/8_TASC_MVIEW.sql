----remember to change the schema name for global schema in the mviews MV_SUB_OBJ_FORM_MAP and MV_SUBTEST_SCORE_TYPE_MAP
----pkg_student_file_download must be there
--ORG_NODE_DIM_HIER
CREATE MATERIALIZED VIEW ORG_NODE_DIM_HIER
REFRESH COMPLETE ON DEMAND
AS
SELECT ORG_NODEID,
            ORG_NODE_NAME,
               PARENT_ORG_NODEID,
               ORG_NODE_CODE,
               ORG_NODE_LEVEL,
               CUSTOMERID,
               HIGHEST_ORG_NODE,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     1),'-99'))
                              ) LEVEL1_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     2),'-99'))
                              ) LEVEL2_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     3),'-99'))
                              ) LEVEL3_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     4),'-99'))
                              ) LEVEL4_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     5),'-99'))
                              ) LEVEL5_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     6),'-99'))
                              ) LEVEL6_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     7),'-99'))
                              ) LEVEL7_NAME,
               pkg_student_file_download.FN_GET_ORG_NAME(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     8),'-99'))
                              ) LEVEL8_NAME,
           pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     1),'-99'))
                              ) LEVEL1_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     2),'-99'))
                              ) LEVEL2_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     3),'-99'))
                              ) LEVEL3_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     4),'-99'))
                              ) LEVEL4_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     5),'-99'))
                              ) LEVEL5_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     6),'-99'))
                              ) LEVEL6_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     7),'-99'))
                              ) LEVEL7_CODE,
               pkg_student_file_download.fn_GET_ORG_NODE_CODE(TO_NUMBER(NVL(REGEXP_SUBSTR(ORG_NODE_CODE_PATH,
                                                     '[^~]+',
                                                     1,
                                                     8),'-99'))
                              ) LEVEL8_CODE,
                              REGEXP_COUNT(ORG_NODE_CODE_PATH, '~', 1, 'i') AS NMBR_OF_ORGS
      FROM (SELECT DISTINCT ORG.ORG_NODEID,
               ORG.ORG_NODE_NAME,
               ORG.PARENT_ORG_NODEID,
               ORG.ORG_NODE_CODE,
               ORG.ORG_NODE_LEVEL,
               ORG.CUSTOMERID,
               CONNECT_BY_ROOT(ORG.ORG_NODEID) AS HIGHEST_ORG_NODE,
               LTRIM(SYS_CONNECT_BY_PATH(ORG.ORG_NODEID, '~'),'~') AS ORG_NODE_CODE_PATH,
               LTRIM(SYS_CONNECT_BY_PATH(ORG.ORG_NODE_NAME, '~'),'~') AS ORG_NODE_NAME_PATH
        FROM  ORG_NODE_DIM ORG
        START WITH ORG.ORG_NODE_LEVEL =1
        CONNECT BY NOCYCLE PRIOR  ORG.ORG_NODEID=ORG.PARENT_ORG_NODEID );
        
--TABLE_1_STU

CREATE MATERIALIZED VIEW LOG ON TABLE_1_STU;

CREATE MATERIALIZED VIEW MV_STUDENT_DETAILS
REFRESH FAST ON DEMAND
AS
SELECT STUDENT_BIO_ID,
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
       student_mode,
       ADMINID,
       CUSTOMERID,
       SUBTESTID,
       SUBTEST_NAME,
       SUBTEST_SEQ,
       DATE_TEST_TAKEN,
   /* SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'RESOLVED ETHNICITY/RACE:') + (LENGTH('RESOLVED ETHNICITY/RACE:')),
              INSTR(DEMO_VALUE, '|', 1, 113) -
              (INSTR(DEMO_VALUE, 'RESOLVED ETHNICITY/RACE:') + (LENGTH('RESOLVED ETHNICITY/RACE:')))) ETHINICITY,*/
    pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Addrss', STUDENT_BIO_ID, CUSTOMERID) AS STREET_NAME,
    pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_APT', STUDENT_BIO_ID, CUSTOMERID) AS APT_NUMBR,
    pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_City', STUDENT_BIO_ID,CUSTOMERID) AS CITY,
    pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_St', STUDENT_BIO_ID,CUSTOMERID) AS STATE,
    pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_ZIP', STUDENT_BIO_ID,CUSTOMERID) AS ZIP_CODE,
    pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Examinee_ph', STUDENT_BIO_ID,CUSTOMERID) AS TELEPHONE_NUMBR,
    PKG_STUDENT_FILE_DOWNLOAD.SF_GET_STUDENT_DEMO_VALUE_TF('Test_Format','BR',STUDENT_BIO_ID,CUSTOMERID) AS BRAILLE,
    PKG_STUDENT_FILE_DOWNLOAD.SF_GET_STUDENT_DEMO_VALUE_TF('Test_Format','LP',STUDENT_BIO_ID,CUSTOMERID) AS LARGE_PRINT,
   -- pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Rslvd_Ethnic', STUDENT_BIO_ID,CUSTOMERID) AS ETHINICITY,
    pkg_student_file_download.SF_GET_STUD_DEMO_VALID_ETHI( 'Rslvd_Ethnic', STUDENT_BIO_ID,CUSTOMERID) AS ETHINICITY,
    'NA' GENDER,
      /* SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'TEST LANGUAGE:') + (LENGTH('TEST LANGUAGE:')),
              INSTR(DEMO_VALUE, '|', 1, 133) -
              (INSTR(DEMO_VALUE, 'TEST LANGUAGE:') + (LENGTH('TEST LANGUAGE:')))) LANGUAGE,*/
       pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Test_Lan', STUDENT_BIO_ID,CUSTOMERID) LANGUAGE  ,
       /*SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'READINESS ASSESSMENT:') + (LENGTH('READINESS ASSESSMENT:')),
              INSTR(DEMO_VALUE, '|', 1, 106) -
              (INSTR(DEMO_VALUE, 'READINESS ASSESSMENT:') + (LENGTH('READINESS ASSESSMENT:')))) REDINESS_ASSESSMENT,*/
  pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Rdns_Assmnt', STUDENT_BIO_ID,CUSTOMERID) AS  REDINESS_ASSESSMENT ,
            /*SUBSTR(DEMO_VALUE,
                    INSTR(DEMO_VALUE, 'TEST-FORMAT:') + (LENGTH('TEST-FORMAT:')),
                    INSTR(DEMO_VALUE, '|', 1, 134) -
                    (INSTR(DEMO_VALUE, 'TEST-FORMAT:') + (LENGTH('TEST-FORMAT:'))))  TEST_FORMAT   ,*/
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Test_Format', STUDENT_BIO_ID,CUSTOMERID) TEST_FORMAT ,
              /*  SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'LOCAL USE 1:') + (LENGTH('LOCAL USE 1:')),
              INSTR(DEMO_VALUE, '|', 1, 63) -
              (INSTR(DEMO_VALUE, 'LOCAL USE 1:') + (LENGTH('LOCAL USE 1:')))) STATE_QUESTION_1,*/
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_1', STUDENT_BIO_ID,CUSTOMERID) STATE_QUESTION_1 ,
          /*     SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'LOCAL USE 2:') + (LENGTH('LOCAL USE 2:')),
              INSTR(DEMO_VALUE, '|', 1, 65) -
              (INSTR(DEMO_VALUE, 'LOCAL USE 2:') + (LENGTH('LOCAL USE 2:')))) STATE_QUESTION_2,*/
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_2', STUDENT_BIO_ID,CUSTOMERID) STATE_QUESTION_2 ,
         /*      SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'LOCAL USE 3:') + (LENGTH('LOCAL USE 3:')),
              INSTR(DEMO_VALUE, '|', 1, 66) -
              (INSTR(DEMO_VALUE, 'LOCAL USE 3:') + (LENGTH('LOCAL USE 3:')))) STATE_QUESTION_3,*/
  pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_3', STUDENT_BIO_ID,CUSTOMERID) STATE_QUESTION_3 ,
     /*           SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'LOCAL USE 4:') + (LENGTH('LOCAL USE 4:')),
              INSTR(DEMO_VALUE, '|', 1, 67) -
              (INSTR(DEMO_VALUE, 'LOCAL USE 4:') + (LENGTH('LOCAL USE 4:')))) STATE_QUESTION_4,*/
   pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_4', STUDENT_BIO_ID,CUSTOMERID) STATE_QUESTION_4 ,
   /*            SUBSTR(DEMO_VALUE,
              INSTR(DEMO_VALUE, 'LOCAL USE 5:') + (LENGTH('LOCAL USE 5:')),
              INSTR(DEMO_VALUE, '|', 1, 68) -
              (INSTR(DEMO_VALUE, 'LOCAL USE 5:') + (LENGTH('LOCAL USE 5:')))) STATE_QUESTION_5*/
              pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_5', STUDENT_BIO_ID,CUSTOMERID) STATE_QUESTION_5
     FROM TABLE_1_STU;
     
--MV_STUDENT_FILE_DOWNLOAD     
     
CREATE MATERIALIZED VIEW LOG ON STUDENT_BIO_DIM;     
     
CREATE MATERIALIZED VIEW MV_STUDENT_FILE_DOWNLOAD
REFRESH FAST ON DEMAND
AS
SELECT
b.CUSTOMERID AS pRISM_CUSTOMER_ID,
B.STUDENT_BIO_ID AS ROSTER_ID,
b.adminid AS ADMINID,
b.created_date_time,
b.updated_date_time,
b.org_nodeid AS org_nodeid,
--N.ORG_NODE_LEVEL AS ORG_NODE_LEVEL,
--N.ORG_NODE_NAME AS ELEMENT_HIERARCHY_NAME,
--N.ORG_NODE_CODE AS ELEMENT_ELEMENT_NUMBER,
--N.SPECIAL_CODES AS ELEMENT_SPECIAL_CODES,
B.LAST_NAME AS LAST_NAME,
B.FIRST_NAME AS FIRST_NAME,
B.MIDDLE_NAME AS MIDDLE_NAME,
B.BIRTHDATE AS BIRTHDATE,
---FOR STUDENT ROSTER
B.GENDERID,
B.STUDENT_MODE,
B.BARCODE AS Barcode_id,
B.TEST_ELEMENT_ID AS Test_Element,
B.EXT_STUDENT_ID,
CASE
  WHEN LENGTH(TO_CHAR(B.EXT_STUDENT_ID)) >= 5 THEN
   'XXXXX' || SUBSTR(TO_CHAR(B.EXT_STUDENT_ID), 6)
  WHEN B.EXT_STUDENT_ID IS NULL OR
       LENGTH(TO_CHAR(B.EXT_STUDENT_ID)) = 0 THEN
  B.EXT_STUDENT_ID
  ELSE
   'XXXXX'
END AS STUDENT_ID,
---START: NEW FEILDS ADDED FOR ER
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Ecc_Math', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_ECC_MA,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Ecc_Read', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_ECC_Rd,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Ecc_Sc', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_ECC_SS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Ecc_Sci', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_ECC_Sc,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Ecc_Wrt', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_ECC_Wr,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Sch_Tp_Cd_Math', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_PC_MA,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Sch_Tp_Cd_Read', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_PC_Rd,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Sch_Tp_Cd_Sc', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_PC_SS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Sch_Tp_Cd_Sci', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_PC_Sc,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Sch_Tp_Cd_Wrt', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_PC_Wr,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Schld_Id_Math', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ScID_MA,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Schld_Id_Read', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ScID_Rd,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Schld_Id_Sc', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ScID_SS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Schld_Id_Sci', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ScID_Sc,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Schld_Id_Wrt', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ScID_Wr,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Tc_Cd_Math', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_TCC_MA,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Tc_Cd_Read', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_TCC_Rd,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Tc_Cd_Sc', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_TCC_SS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Tc_Cd_Sci', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_TCC_Sc,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE_ER ('Cont_Tc_Cd_Wrt', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Schd_TCC_Wr,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Reg_Tccp_Cd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Reg_TC_County,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Reg_Tc_Cd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS Reg_TC_Code,
---END: NEW FEILDS ADDED FOR ER
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE('Test_Form', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TEST_FORM,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Form_Change_Flag', B.STUDENT_BIO_ID,b.CUSTOMERID) AS FORM_CHANGE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Test_Lan', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TEST_LANGUAGE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Test_Lan', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TEST_LANGUAGE_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Test_Format', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TEST_FORMAT,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Test_Format', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TEST_FORMAT_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Tst_Platform', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TEST_MODE,
pkg_student_file_download.SF_GET_GENDER_CODE(b.GENDERID )    AS GENDER,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rslvd_Ethnic', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RESOLVED_ETHNICITY,
pkg_student_file_download.SF_GET_STUD_DEMO_VALID_ETHI( 'Rslvd_Ethnic', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RESOLVED_ETHNICITY_VAL,
B.EXT_STUDENT_ID AS EXAMINEE_ID,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Examinee_ph', B.STUDENT_BIO_ID,b.CUSTOMERID) AS EXAMINEE_PHONE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Examinee_ph', B.STUDENT_BIO_ID,b.CUSTOMERID) AS EXAMINEE_PHONE_VAL,
B.LITHOCODE AS LITHOCODE,
B.INT_STUDENT_ID AS IMAGING_ID,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Addrss', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ADDRESS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Addrss', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ADDRESS_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_APT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS APT,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_APT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS APT_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_City', B.STUDENT_BIO_ID,b.CUSTOMERID) AS CITY,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_City', B.STUDENT_BIO_ID,b.CUSTOMERID) AS CITY_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_St', B.STUDENT_BIO_ID,b.CUSTOMERID) AS STATE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_St', B.STUDENT_BIO_ID,b.CUSTOMERID) AS STATE_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_ZIP', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ZIP,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Student_ZIP', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ZIP_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Ctn_Cd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS COUNTY_CODE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VAL( 'Stdnt_Ctn_Cd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS COUNTY_CODE_VAL,
B.EDU_CENTERID AS EDUCATION_CENTER_CODE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Audio_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_AUDIO_ALT_PRESN,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Audio_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_AUDIO_ALT_PRESN,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Audio_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_AUDIO_ALT_PRESN,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Audio_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_AUDIO_ALT_PRESN,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Audio_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_AUDIO_ALT_PRESN,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Audio_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_AUDIO_ALT_PRESN,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Breaks_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_BREAKS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Breaks_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_BREAKS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Breaks_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_BREAKS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Breaks_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_BREAKS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Breaks_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_BREAKS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Breaks_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_BREAKS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Calc_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_CALCULATOR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Calc_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_CALCULATOR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Calc_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_CALCULATOR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Calc_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_CALCULATOR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.25_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_DUR_1_25T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.25_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_DUR_1_25T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drn_1.25_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_DUR_1_25T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drn_1.25_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_DUR_1_25T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.25_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_DUR_1_25T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.25_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_DUR_1_25T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.5_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_DUR_1_5T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.5_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_DUR_1_5T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.5_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_DUR_1_5T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.5_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_DUR_1_5T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.5_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_DUR_1_5T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_1.5_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_DUR_1_5T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_2_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_DUR_2T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_2_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_DUR_2T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_2_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_DUR_2T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_2_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_DUR_2T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_2_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_DUR_2T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Drtn_2_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_DUR_2T,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Phl_Sprt_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_PHYSICAL_SUPP,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Phl_Sprt_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_PHYSICAL_SUPP,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Phl_Sprt_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_PHYSICAL_SUPP,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Phl_Sprt_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_PHYSICAL_SUPP,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Phl_Sprt_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_PHYSICAL_SUPP,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Phl_Sprt_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_PHYSICAL_SUPP,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scribe_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_SCRIBE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scribe_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_SCRIBE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scribe_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_SCRIBE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scribe_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_SCRIBE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scribe_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_SCRIBE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scribe_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_SCRIBE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tech_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_TECNOLOGY_DEVICE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tech_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_TECHNOLOGY_DEVICE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tech_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_TECNOLOGY_DEVICE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tech_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_TECNOLOGY_DEVICE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tech_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_TECHNOLOGY_DEVICE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tech_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_TECHNOLOGY_DEVICE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sprt_Rm_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_SEPARATE_ROOM,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sprt_Rm_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_SEPARATE_ROOM,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sprt_Rm_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_SEPARATE_ROOM,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sprt_Rm_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_SEPARATE_ROOM,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sprt_Rm_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_SEPARATE_ROOM,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sprt_Rm_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_SEPARATE_ROOM,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sml_Grp_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_SMALL_GRP_SETTING,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sml_Grp_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_SMALL_GRP_SETTING,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sml_Grp_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_SMALL_GRP_SETTING,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sml_Grp_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_SMALL_GRP_SETTING,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sml_Grp_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_SMALL_GRP_SETTING,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Sml_Grp_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_SMALL_GRP_SETTING,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Other_READ', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_OTHER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Other_WRIT', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_OTHER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Other_MATH_P1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART1_OTHER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Other_MATH_P2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PART2_OTHER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Other_SCI', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_OTHER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Other_SS', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_OTHER,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Exmne_Wver', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WAIVER,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Hme_Language', B.STUDENT_BIO_ID,b.CUSTOMERID) AS HOME_LANGUAGE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Hghst_Edu', B.STUDENT_BIO_ID,b.CUSTOMERID) AS K_12_EDUC_COMPLETED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rd_Passed', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_PASSED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Wr_Passed', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_PASSED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Ma_Passed', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PASSED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Sc_Passed', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_PASSED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'SS_Passed', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_PASSED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'None_Passed', B.STUDENT_BIO_ID,b.CUSTOMERID) AS NONE_PASSED,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rd_Times_Tkn', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_T_TAKEN,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Wr_Times_Tkn', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_T_TAKEN,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Ma_Times_Tkn', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_T_TAKEN,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Sc_Times_Tkn', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_T_TAKEN,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'SS_Times_Tkn', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_T_TAKEN,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rd_Retake', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_RETAKE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Wr_Retake', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_RETAKE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Ma_Retake', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_RETAKE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Sc_Retake', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_RETAKE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'SS_Retake', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_RETAKE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'None_Retake', B.STUDENT_BIO_ID,b.CUSTOMERID) AS NONE_RETAKE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rdns_Assmnt', B.STUDENT_BIO_ID,b.CUSTOMERID) AS READINESS_ASSESSMENT,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Rdns_Assmnt', B.STUDENT_BIO_ID,b.CUSTOMERID) AS READINESS_ASSESSMENT_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Cnty_Prog', B.STUDENT_BIO_ID,b.CUSTOMERID) AS COUNTY_PROGRAM,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Schl_Prog', B.STUDENT_BIO_ID,b.CUSTOMERID) AS DISTRICT_PROGRAM,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Mtry_Prog', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MILITARY_PROGRAM,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Rlgn_Prog', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RELIGIOUS_PROGRAM,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Stdy_Book', B.STUDENT_BIO_ID,b.CUSTOMERID) AS pURCHASED_BOOKS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Onln_Prog', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ONLINE_STUDY_PROGRAM,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Homeschl', B.STUDENT_BIO_ID,b.CUSTOMERID) AS HOMESCHOOL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Tutor', B.STUDENT_BIO_ID,b.CUSTOMERID) AS TUTOR,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Pr_Slf_Tht', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SELF_TAUGHT,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rd_cls_3_mts', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_PAST_3MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Wr_cls_3_mts', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_PAST_3MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Ma_cls_3_mts', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_PAST_3MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Sc_cls_3_mts', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_PAST_3MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'SS_cls_3_mts', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_PAST_3MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rd_mnths', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_HOW_MANY_MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Wr_mnths', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_HOW_MANY_MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Ma_mnths', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_HOW_MANY_MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Sc_mnths', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_HOW_MANY_MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'SS_mnths', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_HOW_MANY_MONTHS,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Rd_Grd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_WHAT_GRADE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Wr_Grd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_WHAT_GRADE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Ma_Grd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_WHAT_GRADE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Sc_Grd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_WHAT_GRADE,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'SS_Grd', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_WHAT_GRADE,
PKG_STUDENT_FILE_DOWNLOAD.SF_GET_STUDENT_DEMO_VALUE_TF('Test_Format','BR',B.STUDENT_BIO_ID,b.CUSTOMERID) AS BRAILLE,
PKG_STUDENT_FILE_DOWNLOAD.SF_GET_STUDENT_DEMO_VALUE_TF('Test_Format','LP',B.STUDENT_BIO_ID,b.CUSTOMERID) AS LARGE_PRINT,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_1,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_1_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_2,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_2_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_3', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_3,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_3', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_3_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_4,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_4_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_5,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALID( 'Local_Use_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_5_VAL,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_6,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_7', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_7,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_8', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_8,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_9', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_9,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_10', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_10,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_11', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_11,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_12', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_12,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_13', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_13,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_14', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_14,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_15', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_15,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_16', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_16,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_17', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_17,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_18', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_18,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_19', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_19,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Local_Use_20', B.STUDENT_BIO_ID,b.CUSTOMERID) AS LOCAL_USE_20,
pkg_student_file_download.SF_GET_STUDENT_DEMO_VALUE( 'Exmne_Ack', B.STUDENT_BIO_ID,b.CUSTOMERID) AS ACKNOWLEDGEMENT,
/*SSF.TEST_DATE AS DATE_TEST_TAKEN_RD_SUBTEST,
SSF.NCR AS RD_NUMBER_CORRECT,
SSF.SS AS RD_SCALE_SCORE,
SSF.HSE AS RD_HIGH_SC_EQ,
SSF.PR AS RD_PERCENTILE_RANK,
SSF.NCE AS RD_NORMAL_CURVE_EQ,*/
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '1' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS DATE_TEST_TAKEN_RD_SUBTEST,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '1',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS RD_NUMBER_CORRECT,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '1',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS RD_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '1',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS RD_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '1',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS RD_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '1',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS RD_NORMAL_CURVE_EQ,
-- Status Codes for All Subtest
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '1',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS RD_STATUS_CODE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '2',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS WR_STATUS_CODE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS ELA_STATUS_CODE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '4',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS MATH_STATUS_CODE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '5',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS SCI_STATUS_CODE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '6',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS SOC_STATUS_CODE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'STATUS_CODE' ) AS OC_STATUS_CODE_RSTR,
b.GRADEID AS GRADEID,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'FORMID' ) AS FORM_RSTR,
pkg_student_file_download.SF_GET_CUST_IDS(P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'ASSESSMENTID' ) AS ASSESSMENTID,
--
pkg_student_file_download.SF_GET_CUST_IDS(P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'CUST_PROD_ID' ) AS CUST_PROD_ID,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'101', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_1_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'101', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_1_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'102', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_2_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'102', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_2_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'103', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_3_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'103', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_3_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'104', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_4_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'104', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_4_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'105', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_5_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'105', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_5_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'106', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_6_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'106', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_6_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'107', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_7_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'107', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_7_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'108', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2047) AS RD_OBJ_8_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'108', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2047) AS RD_OBJ_8_NOT_ALL_ATMPT_FLAG,
/*SSF.TEST_DATE AS DATE_TEST_TAKEN_WR_SUBTEST,
SSF.NCR AS WR_NUMBER_CORRECT,
SSF.SS AS WR_SCALE_SCORE,
SSF.HSE AS WR_HIGH_SC_EQ,
SSF.PR AS WR_PERCENTILE_RANK,
SSF.NCE AS WR_NORMAL_CURVE_EQ,*/
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '2' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS DATE_TEST_TAKEN_WR_SUBTEST,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '2',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS WR_NUMBER_CORRECT,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '2',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS WR_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '2',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS WR_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '2',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS WR_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '2',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS WR_NORMAL_CURVE_EQ,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'207', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2048) AS WR_OBJ_1_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'207', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2048) AS WR_OBJ_1_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'208', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2048)  AS WR_OBJ_2_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'208', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2048) AS WR_OBJ_2_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2048) AS WR_OBJ_3_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2048) AS WR_OBJ_3_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2048) AS WR_OBJ_4_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2048) AS WR_OBJ_4_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS ELA_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS ELA_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS ELA_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS ELA_NORMAL_CURVE_EQ,
/*SSF.TEST_DATE AS DATE_TEST_TAKEN_MATH_SUBTEST,
SSF.NCR AS MATH_NUMBER_CORRECT,
SSF.SS AS MATH_SCALE_SCORE,
SSF.HSE AS MATH_HIGH_SC_EQ,
SSF.PR AS MATH_PERCENTILE_RANK,
SSF.NCE AS MATH_NORMAL_CURVE_EQ,*/
----FOR STUDENT ROSTER
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '3' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS ELA_TEST_DATE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS ELA_NCR_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS ELA_SS_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS ELA_HSE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS ELA_PR_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '3',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS ELA_NCE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '4' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS DATE_TEST_TAKEN_MATH_SUBTEST,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '4',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS MATH_NUMBER_CORRECT,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '4',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS  MATH_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '4',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS  MATH_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '4',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS MATH_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '4',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS MATH_NORMAL_CURVE_EQ,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'401', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_1_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'401', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_1_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'402', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_2_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'402', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_2_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'403', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_3_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'403', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_3_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'404', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_4_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'404', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_4_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'405', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_5_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'405', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_5_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'406', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_6_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'406', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_6_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'407', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2050) AS MATH_OBJ_7_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'407', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2050) AS MATH_OBJ_7_NOT_ALL_ATMPT_FLAG,
/*SSF.TEST_DATE AS DATE_TEST_TAKEN_SCI_SUBTEST,
SSF.NCR AS SCI_NUMBER_CORRECT,
SSF.SS AS SCI_SCALE_SCORE,
SSF.HSE AS SCI_HIGH_SC_EQ,
SSF.PR AS SCI_PERCENTILE_RANK,
SSF.NCE AS SCI_NORMAL_CURVE_EQ,
*/
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '5' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS DATE_TEST_TAKEN_SCI_SUBTEST,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '5',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS SCI_NUMBER_CORRECT,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '5',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' )  AS SCI_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '5',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' )  AS SCI_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '5',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS SCI_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '5',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS SCI_NORMAL_CURVE_EQ,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'501', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2051) AS SCI_OBJ_1_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'501', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2051) AS SCI_OBJ_1_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'502', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2051) AS SCI_OBJ_2_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'502', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2051) AS SCI_OBJ_2_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'503', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2051) AS SCI_OBJ_3_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'503', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2051) AS SCI_OBJ_3_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'504', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2051) AS SCI_OBJ_4_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'504', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2051) AS SCI_OBJ_4_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'505', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2051) AS SCI_OBJ_5_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'505', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2051) AS SCI_OBJ_5_NOT_ALL_ATMPT_FLAG,
/*SSF.TEST_DATE AS DATE_TEST_TAKEN_SS_SUBTEST,
SSF.NCR AS SOC_NUMBER_CORRECT,
SSF.SS AS SOC_SCALE_SCORE,
SSF.HSE AS SOC_HIGH_SC_EQ,
SSF.PR AS SOC_PERCENTILE_RANK,
SSF.NCE AS SOC_NORMAL_CURVE_EQ,
*/
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '6' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS DATE_TEST_TAKEN_SS_SUBTEST,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '6',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS SOC_NUMBER_CORRECT,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '6',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS SOC_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '6',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS SOC_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '6',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS SOC_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '6',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS SOC_NORMAL_CURVE_EQ,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'601', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_1_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'601', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_1_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'602', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_2_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'602', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_2_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'603', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_3_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'603', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_3_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'604', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_4_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'604', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_4_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'605', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_5_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'605', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_5_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'606', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_6_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'606', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_6_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'607', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'PL',p_subtest_id=>2052) AS SOC_OBJ_7_MASTERY,
pkg_student_file_download.SF_GET_objective_SCORE(objective_CODE=>'607', P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID, p_columntype =>'INRC',p_subtest_id=>2052) AS SOC_OBJ_7_NOT_ALL_ATMPT_FLAG,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS OVR_COMP_SCORE_SCALE_SCORE,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS OVR_COMP_SCORE_HIGH_SC_EQ,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS OVR_COMP_SCORE_PERCENTILE_RANK,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS OVR_COMP_SCORE_NORMAL_CURVE_EQ,
----FOR STUDENT ROSTER
pkg_student_file_download.SF_GET_SUBTEST_DATE(P_SUBTEST_CODE => '7' ,P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'TEST_DATE' ) AS OC_TEST_DATE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCR' ) AS OC_NCR_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'SS' ) AS OC_SS_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'HSE' ) AS OC_HSE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'PR' ) AS OC_PR_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE => '7',P_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,p_columntype =>'NCE' ) AS OC_NCE_RSTR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scrn_Rdr_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_SCREEN_READER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Oln_Calc_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_ONLINE_CALC,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tst_Pse_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_TEST_PAUSE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Hlghtr_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_HIGHLIGHTER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Blkg_Rlr_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_BLOCKING_RULER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mag_Gls_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_MAGNIFYING_GLASS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Ft_Bk_Cr_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_FONT_AND_BKGND_CLR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Lrg_Font_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_LARGE_FONT,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Msc_Plr_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_MUSIC_PLAYER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Extnd_Tm_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_EXTENDED_TIME,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mskng_Tl_1', B.STUDENT_BIO_ID,b.CUSTOMERID) AS RD_MASKING_TOOL,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scrn_Rdr_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_SCREEN_READER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Oln_Calc_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_ONLINE_CALC,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tst_Pse_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_TEST_PAUSE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Hlghtr_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_HIGHLIGHTER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Blkg_Rlr_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_BLOCKING_RULER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mag_Gls_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_MAGNIFYING_GLASS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Ft_Bk_Cr_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_FONT_AND_BKGND_CLR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Lrg_Font_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_LARGE_FONT,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Msc_Plr_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_MUSIC_PLAYER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Extnd_Tm_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_EXTENDED_TIME,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mskng_Tl_2', B.STUDENT_BIO_ID,b.CUSTOMERID) AS WR_MASKING_TOOL,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scrn_Rdr_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_SCREEN_READER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Oln_Calc_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_ONLINE_CALC,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tst_Pse_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_TEST_PAUSE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Hlghtr_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_HIGHLIGHTER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Blkg_Rlr_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_BLOCKING_RULER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mag_Gls_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_MAGNIFYING_GLASS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Ft_Bk_Cr_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_FONT_AND_BKGND_CLR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Lrg_Font_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_LARGE_FONT,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Msc_Plr_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_MUSIC_PLAYER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Extnd_Tm_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_EXTENDED_TIME,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mskng_Tl_4', B.STUDENT_BIO_ID,b.CUSTOMERID) AS MATH_MASKING_TOOL,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scrn_Rdr_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_SCREEN_READER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Oln_Calc_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_ONLINE_CALC,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tst_Pse_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_TEST_PAUSE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Hlghtr_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_HIGHLIGHTER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Blkg_Rlr_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_BLOCKING_RULER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mag_Gls_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_MAGNIFYING_GLASS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Ft_Bk_Cr_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_FONT_AND_BKGND_CLR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Lrg_Font_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_LARGE_FONT,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Msc_Plr_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_MUSIC_PLAYER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Extnd_Tm_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_EXTENDED_TIME,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mskng_Tl_5', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SCI_MASKING_TOOL,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Scrn_Rdr_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_SCREEN_READER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Oln_Calc_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_ONLINE_CALC,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Tst_Pse_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_TEST_PAUSE,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Hlghtr_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_HIGHLIGHTER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Blkg_Rlr_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_BLOCKING_RULER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mag_Gls_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_MAGNIFYING_GLASS,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Ft_Bk_Cr_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_FONT_AND_BKGND_CLR,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Lrg_Font_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_LARGE_FONT,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Msc_Plr_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_MUSIC_PLAYER,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Extnd_Tm_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_EXTENDED_TIME,
pkg_student_file_download.SF_GET_SUBTEST_DEMO_VALUE( 'Acc_Mskng_Tl_6', B.STUDENT_BIO_ID,b.CUSTOMERID) AS SS_MASKING_TOOL,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2047,p_itemsetid =>5172) AS RD_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5172) AS RD_SR_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5172) AS RD_SR_TYPE,
--ISF.SCORE_VALUES AS RD_SR_RESPONSE,
' ' AS RD_ITEMS_FU,
--ISF.SCORE_VALUES AS WR_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2048,p_itemsetid =>5174) AS WR_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5174) AS WR_SR_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5174) AS WR_SR_TYPE,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2048,p_itemsetid =>5173) AS WR_CR_RESPONSE,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5173) AS WR_CR_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5173) AS WR_CR_TYPE,
' ' AS WR_ITEMS_FU,
--ISF.SCORE_VALUES AS MATH_ITEM_CODE_SR,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2050,p_itemsetid =>5175) AS MATH_SR_RESPONSE ,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5175) AS  MATH_SR_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5175) AS  MATH_SR_TYPE,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2050,p_itemsetid =>5176) AS MATH_GR_RESPONSE,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5176) AS MATH_GR_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5176) AS MATH_GR_TYPE,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2050,p_itemsetid =>5176) AS MATH_ITEM_CODE_GR_STATUS,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2050,p_itemsetid =>5177) AS MATH_ITEM_CODE_GR_EDITED,
' ' AS MATH_ITEMS_FU,
--ISF.SCORE_VALUES AS SCI_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2051,p_itemsetid =>5178) AS SCI_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5178) AS  SCI_ITEM_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5178) AS  SCI_ITEM_type,
' ' AS SCI_ITEMS_FU,
--ISF.SCORE_VALUES AS SOC_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_SCORE(objective_ID =>1,
                     p_STUDENT_BIO_ID =>B.STUDENT_BIO_ID,
                     p_columntype =>'SCORE_VALUES',
                     p_subtest_id =>2052,p_itemsetid =>5179) AS SOC_SR_RESPONSE,
pkg_student_file_download.SF_GET_item_code(p_itemsetid =>5179) AS  SOC_ITEM_CODE,
pkg_student_file_download.SF_GET_ITEM_TYPE(p_itemsetid =>5179) AS  SOC_ITEM_type,
' ' AS SS_FU,
' ' AS CTB_USE_FIELD
FROM student_bio_dim b;

--MV_TEST_PROGRAM

CREATE MATERIALIZED VIEW MV_TEST_PROGRAM
REFRESH COMPLETE ON DEMAND
AS
SELECT LVL2.CUSTOMERID,
       LVL2.NUM_LEVELS,
       CASE
       WHEN LVL2.NUM_LEVELS=1 THEN
            LVL2.LVL_NAME
       ELSE
            SUBSTR(LVL2.LVL_NAME,1, INSTR(LVL2.LVL_NAME,'~',1,1)-1 )
       END AS ORG_LEVEL1_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=2 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,1)+1,LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,1))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,1)+1, INSTR(LVL2.LVL_NAME,'~',1,2)-INSTR(LVL2.LVL_NAME,'~',1,1)-1 )
       END AS ORG_LEVEL2_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=3 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,2)+1,LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,2))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,2)+1, INSTR(LVL2.LVL_NAME,'~',1,3)-INSTR(LVL2.LVL_NAME,'~',1,2)-1 )
       END AS ORG_LEVEL3_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=4 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,3)+1, LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,3))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,3)+1, INSTR(LVL2.LVL_NAME,'~',1,4)-INSTR(LVL2.LVL_NAME,'~',1,3)-1 )
       END AS ORG_LEVEL4_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=5 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,4)+1,LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,4))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,4)+1, INSTR(LVL2.LVL_NAME,'~',1,5)-INSTR(LVL2.LVL_NAME,'~',1,4)-1 )
       END AS ORG_LEVEL5_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=6 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,5)+1,LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,5))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,5)+1, INSTR(LVL2.LVL_NAME,'~',1,6)-INSTR(LVL2.LVL_NAME,'~',1,5)-1 )
       END AS ORG_LEVEL6_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=7 THEN
       SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,6)+1, LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,6))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,6)+1, INSTR(LVL2.LVL_NAME,'~',1,7)-INSTR(LVL2.LVL_NAME,'~',1,6)-1 )
       END AS ORG_LEVEL7_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=8 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,7)+1,LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,7))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,7)+1, INSTR(LVL2.LVL_NAME,'~',1,8)-INSTR(LVL2.LVL_NAME,'~',1,7)-1 )
       END AS ORG_LEVEL8_TYPE,
       CASE
       WHEN LVL2.NUM_LEVELS=9 THEN
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,8)+1,LENGTH(LVL2.LVL_NAME)-INSTR(LVL2.LVL_NAME,'~',1,8))
       ELSE
            SUBSTR(LVL2.LVL_NAME,INSTR(LVL2.LVL_NAME,'~',1,8)+1,INSTR(LVL2.LVL_NAME,'~',1,8)-INSTR(LVL2.LVL_NAME,'~',1,7)-1 )
       END AS ORG_LEVEL9_TYPE
FROM (SELECT LVL1.CUSTOMERID,LVL1.NUM_LEVELS,LISTAGG (LVL1.ORG_LABEL,'~') WITHIN GROUP (ORDER BY LVL1.ORG_LEVEL) AS LVL_NAME
          FROM (SELECT DISTINCT ORG_SRTUC.ORG_LEVEL ,ORG_SRTUC.ORG_LABEL ,TP.NUM_LEVELS,TP.CUSTOMERID
                  FROM TEST_PROGRAM TP,ORG_TP_STRUCTURE ORG_SRTUC
                   WHERE TP.ACTIVATION_STATUS = 'AC'
                     AND ORG_SRTUC.TP_ID = TP.TP_ID
                     ORDER BY  ORG_SRTUC.ORG_LEVEL)LVL1
                     GROUP BY LVL1.CUSTOMERID,
                              LVL1.NUM_LEVELS)LVL2;
	

	
CREATE MATERIALIZED VIEW MV_SUB_OBJ_FORM_MAP
REFRESH COMPLETE ON DEMAND
AS
SELECT  DISTINCT SUB.SUBTESTID,
                   SUB.SUBTEST_NAME,
                   SUB.SUBTEST_CODE,
                   SUB.SUBTEST_SEQ,
                   FRM.formid,
                   FRM.FORM_NAME,
                   LM.LEVELID,
                   SOM.ASSESSMENTID ,
                   SOM.objectiveID
                   /* *******************************************************
                    * This MATERIALIZED VIEW is created as a part of performance
                    * improvemnt (query tunning) for the input control queries of
                    * Subtest (SF_GET_SUBTEST) and Form (SF_GET_FORM)
                    * which were taking long time to execute.
                    * DATE: Feb-10-2015
                    * AUTHOR: Partha
                    *********************************************************/
            FROM SUBTEST_OBJECTIVE_MAP SOM,
                 --SUBTEST_DIM 
                 (SELECT SUBTESTID,
                   SUBTEST_NAME,
                   SUBTEST_SEQ,
                   SUBTEST_CODE,
                   SUBTEST_TYPE,
                   CONTENTID,
                   CANDIDATE_SUB_SEQ,
                   DATETIMESTAMP
                  FROM PRISMGLOBAL.SUBTEST_DIM
                 WHERE CONTENTID IN (SELECT CONTENTID FROM CONTENT_DIM))SUB,
                 LEVEL_MAP LM,
                 --FORM_DIM 
                 (SELECT FORMID, FORM_NAME, FORM_CODE, DATETIMESTAMP
                   FROM PRISMGLOBAL.FORM_DIM
                  WHERE PROJECTID = 1)FRM
            WHERE SUB.SUBTESTID = SOM.SUBTESTID
              AND LM.LEVEL_MAPID=SOM.LEVEL_MAPID
              AND FRM.FORMID = LM.FORMID
              AND SOM.ASSESSMENTID = LM.ASSESSMENTID;	


			  
CREATE MATERIALIZED VIEW MV_SUBTEST_SCORE_TYPE_MAP
REFRESH COMPLETE ON DEMAND
AS
SELECT  SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ, SUB.SUBTEST_CODE, SUB.SUBTEST_TYPE, SUB.CONTENTID, SUB.CANDIDATE_SUB_SEQ ,
              STL.CATEGORY,STL.SCORE_TYPE,STL.SCORE_VALUE,STL.SCORE_VALUE_NAME,STL.CUST_PROD_ID,
             CUST.CUSTOMERID
			 /* *******************************************************
                    * This MATERIALIZED VIEW is created as a part of performance
                    * improvemnt for the HSE dashboard
                    * which were taking long time to execute.
                    * DATE: Feb-23-2015
                    * AUTHOR: Partha
                    *********************************************************/
FROM
(SELECT * FROM SCORE_TYPE_LOOKUP WHERE CATEGORY = 'CONTENT' AND SCORE_TYPE = 'HSE') STL,
--SUBTEST_DIM 
(SELECT SUBTESTID,
       SUBTEST_NAME,
       SUBTEST_SEQ,
       SUBTEST_CODE,
       SUBTEST_TYPE,
       CONTENTID,
     CANDIDATE_SUB_SEQ,
       DATETIMESTAMP
  FROM PRISMGLOBAL.SUBTEST_DIM
 WHERE CONTENTID IN (SELECT CONTENTID FROM CONTENT_DIM))SUB,
--CUST_PRODUCT_LINK 
(SELECT CUST_PROD_ID,
       CUSTOMERID,
       PRODUCTID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP
  FROM PRISMGLOBAL.CUST_PRODUCT_LINK
 WHERE PRODUCTID IN
       (SELECT PRODUCTID FROM PRISMGLOBAL.PRODUCT WHERE PROJECTID = 1))CUST
WHERE STL.CUST_PROD_ID = CUST.CUST_PROD_ID;			  


CREATE MATERIALIZED VIEW MV_ORG_TP_STRUCTURE
REFRESH FORCE ON DEMAND
AS
SELECT TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP
					/* *******************************************************
                    * This MATERIALIZED VIEW is created as a part of performance
                    * improvemnt for the queries of the manage modules
                    * DATE: Mar-02-2015
                    * AUTHOR: Partha
                    *********************************************************/
  FROM PRISMGLOBAL.ORG_TP_STRUCTURE
 WHERE TP_ID IN (SELECT TP_ID FROM TEST_PROGRAM); 