CREATE OR REPLACE PACKAGE PKG_GET_MIG_RESULTS_GRT IS

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_ORG_NODE_DETAILS_DISTRICT(P_IN_TEST_TYPE             IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                         P_OUT_CUR_ORG_NODE_DETAILS OUT GET_REFCURSOR,
                                         P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2);

  PROCEDURE SP_ORG_NODE_DETAILS_SCHOOL(P_IN_ORG_NODEID            IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_TEST_TYPE             IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                       P_OUT_CUR_ORG_NODE_DETAILS OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2);

  PROCEDURE SP_GET_ALL_MIG_RESULT_GRT(P_IN_PRODUCTID        IN CUST_PRODUCT_LINK.PRODUCTID%TYPE,
                                      P_IN_CUSTOMERID       IN CUST_PRODUCT_LINK.CUSTOMERID%TYPE,
                                      P_IN_ORG_NODEID       IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_TEST_TYPE        IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                      P_OUT_CUR_MIG_DETAILS OUT GET_REFCURSOR,
                                      P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE SP_GET_MIG_RESULT_GRT(P_IN_PRODUCTID        IN CUST_PRODUCT_LINK.PRODUCTID%TYPE,
                                  P_IN_CUSTOMERID       IN CUST_PRODUCT_LINK.CUSTOMERID%TYPE,
                                  P_IN_DIST_ORG_NODEID  IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_SCH_ORG_NODEID   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_TEST_TYPE        IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                  P_OUT_CUR_MIG_DETAILS OUT GET_REFCURSOR,
                                  P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE SP_GET_ALL_INVITATION_DETAILS(P_IN_PRODUCTID               IN PRODUCT.PRODUCTID%TYPE,
                                          P_IN_ORG_NODEID              IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                          P_IN_TEST_TYPE               IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                          P_OUT_CUR_INVITATION_DETAILS OUT GET_REFCURSOR,
                                          P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2);

  PROCEDURE SP_GET_INVITATION_DETAILS(P_IN_PRODUCTID               IN PRODUCT.PRODUCTID%TYPE,
                                      P_IN_ORG_NODEID              IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_TEST_TYPE               IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                      P_OUT_CUR_INVITATION_DETAILS OUT GET_REFCURSOR,
                                      P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2);

END PKG_GET_MIG_RESULTS_GRT;
/
CREATE OR REPLACE PACKAGE BODY PKG_GET_MIG_RESULTS_GRT IS

  --PROCEDURE TO FETCH ORG_NODE_DETAILS BASED ON DISTRICT.
  PROCEDURE SP_ORG_NODE_DETAILS_DISTRICT(P_IN_TEST_TYPE             IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                         P_OUT_CUR_ORG_NODE_DETAILS OUT GET_REFCURSOR,
                                         P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2) IS
  
  BEGIN
  
    OPEN P_OUT_CUR_ORG_NODE_DETAILS FOR
      SELECT DISTINCT DIM.ORG_NODEID        DISTRICT_ID,
                      DIM.ORG_NODE_NAME     DISTRICT_NAME,
                      DIM.PARENT_ORG_NODEID STATE_ID
        FROM ORG_LSTNODE_LINK LINK1,
             RESULTS_GRT_FACT FACT,
             ORG_NODE_DIM     DIM
      
       WHERE FACT.ORG_NODEID = LINK1.ORG_LSTNODEID
         AND LINK1.ORG_NODEID = DIM.ORG_NODEID
         AND DIM.ORG_NODE_LEVEL = 2
         AND FACT.ISPUBLIC = P_IN_TEST_TYPE --- 1 STANDS FOR PUBLIC & 0 FOR NON-PUBLIC
       ORDER BY DISTRICT_NAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_ORG_NODE_DETAILS_DISTRICT;

  --PROCEDURE TO FETCH ORG_NODE_DETAILS BASED ON SCHOOL OF A PARTICULAR DISTRICT.
  PROCEDURE SP_ORG_NODE_DETAILS_SCHOOL(P_IN_ORG_NODEID            IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_TEST_TYPE             IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                       P_OUT_CUR_ORG_NODE_DETAILS OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_ORG_NODE_DETAILS FOR
      SELECT DISTINCT DIM.ORG_NODEID        SCHOOL_ID,
                      DIM.ORG_NODE_NAME     SCHOOL_NAME,
                      DIM.PARENT_ORG_NODEID DISTRICT_NAME
        FROM ORG_LSTNODE_LINK LINK1,
             RESULTS_GRT_FACT FACT,
             ORG_NODE_DIM     DIM
      
       WHERE DIM.PARENT_ORG_NODEID = P_IN_ORG_NODEID
         AND FACT.ORG_NODEID = LINK1.ORG_LSTNODEID
         AND LINK1.ORG_NODEID = DIM.ORG_NODEID
         AND DIM.ORG_NODE_LEVEL = 3
         AND FACT.ISPUBLIC = P_IN_TEST_TYPE
       ORDER BY SCHOOL_NAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_ORG_NODE_DETAILS_SCHOOL;

  ----PROCEDURE TO FETCH ALL MIG_RESULTS_GRT BASED ON DISTRICTID.
  PROCEDURE SP_GET_ALL_MIG_RESULT_GRT(P_IN_PRODUCTID        IN CUST_PRODUCT_LINK.PRODUCTID%TYPE,
                                      P_IN_CUSTOMERID       IN CUST_PRODUCT_LINK.CUSTOMERID%TYPE,
                                      P_IN_ORG_NODEID       IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_TEST_TYPE        IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                      P_OUT_CUR_MIG_DETAILS OUT GET_REFCURSOR,
                                      P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  
    CUSTPRODID CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
    --ADM_ID ORG_USERS.ADMINID%TYPE;
  BEGIN
  
    SELECT CUST_PROD_ID
      INTO CUSTPRODID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = P_IN_PRODUCTID
       AND CUSTOMERID = P_IN_CUSTOMERID;
  
    DBMS_OUTPUT.PUT_LINE(CUSTPRODID);
  
    OPEN P_OUT_CUR_MIG_DETAILS FOR
      SELECT DISTINCT FACT.TAPE_MODE,
                      FACT.ORGTSTGPGM,
                      DISTRICT.ORG_NODE_NAME DISTRICT_NAME,
                      DISTRICT.ORG_NODE_CODE DISTRICT_ID,
                      SCHOOL.ORG_NODE_NAME SCHOOL_NAME,
                      SCHOOL.ORG_NODE_CODE SCHOOL_ID,
                      FACT.TEACHER_NAME,
                      FACT.TEACHER_ELEMENT,
                      GR.GRADE_CODE GRADEID,
                      FACT.CITY,
                      FACT.STATE,
                      FACT.ISTEP_TEST_NAME,
                      FACT.ISTEP_BOOK_NUM,
                      FACT.ISTEP_FORM,
                      FACT.TEST_DATE,
                      FACT.STUDENT_LAST_NAME,
                      FACT.STUDENT_FIRST_NAME,
                      FACT.STUDENT_MIDDLE_INITIAL,
                      FACT.STUDENTS_GENDER,
                      FACT.BIRTH_DATE,
                      FACT.CHRONOLOGICAL_AGE_IN_MONTHS,
                      FACT.ETHNICITY_HISPANIC ETHNICITY,
                      FACT.RACE_AMERICAN_INDIAN,
                      FACT.RACE_ASIAN,
                      FACT.RACE_BLACK,
                      FACT.RACE_PACIFIC_ISLANDER,
                      FACT.RACE_WHITE,
                      '' FILLER_1, -- TODO -- NF
                      FACT.STUDENT_TEST_AI,
                      FACT.LOCAL_USE_J SPECIAL_CODE_J,
                      FACT.ETHNICITY_K,
                      FACT.SPECIAL_EDUCATION_L,
                      FACT.EXCEPTIONALITY_U EXCEPTIONALITY_M,
                      FACT.SOCIOECONOMIC_STATUS_N,
                      FACT.SECTION_504_O,
                      FACT.LEPESL_V ENGLISH_LEARNER_P,
                      FACT.MIGRANT_Q,
                      FACT.LOCAL_USE_R,
                      FACT.LOCAL_USE_S,
                      FACT.LOCAL_USE_T,
                      FACT.MATCH_UNMATCH_U,
                      FACT.DUPLICATE_V,
                      FACT.NOT_USED_W CTB_USE_ONLY_W,
                      FACT.SPECIAL_CODE_X,
                      FACT.SPECIAL_CODE_Y,
                      FACT.SPECIAL_CODE_Z,
                      FACT.ACCOMMODATIONS_ELA_P ACCOMMODATIONS_ELA,
                      FACT.ACCOMMODATIONS_MATH_Q ACCOMMODATIONS_MATH,
                      FACT.ACCOMMODATIONS_SCIENCE_X ACCOMMODATIONS_SCIENCE,
                      FACT.ACCOMMODATIONS_SOCIAL ACCOMMODATIONS_SS,
                      FACT.CORPORATION_USE_ID,
                      FACT.CUSTOMER_USE,
                      '' FILLER_2, -- TODO -- NF
                      '' FILLER_3, -- TODO -- NF
                      FACT.ELA_PF_INDICATOR,
                      FACT.MATH_PF_INDICATOR,
                      FACT.SCIENCE_PF_INDICATOR,
                      FACT.SOCIAL_PF_INDICATOR,
                      FACT.ENGLANG_ARTS_NUM_CORRECT,
                      FACT.MATHEMATICS_NUM_CORRECT,
                      FACT.SCIENCE_NUM_CORRECT,
                      FACT.SOCIAL_NUM_CORRECT,
                      FACT.ENGLAN_ARTS_SCALE_SCORE,
                      FACT.MATHEMATICS_SCALE_SCORE,
                      FACT.SCIENCE_SCALE_SCORE,
                      FACT.SOCIAL_SCALE_SCORE,
                      FACT.ENGLAN_ARTS_SCALE_SCORE_SEM,
                      FACT.MATHEMATICS_SCALE_SCORE_SEM,
                      FACT.SCIENCE_SCALE_SCORE_SEM,
                      FACT.SOCIAL_SCALE_SCORE_SEM,
                      FACT.MASTERY_INDICATOR_1,
                      FACT.MASTERY_INDICATOR_2,
                      FACT.MASTERY_INDICATOR_3,
                      FACT.MASTERY_INDICATOR_4,
                      FACT.MASTERY_INDICATOR_5,
                      FACT.MASTERY_INDICATOR_6,
                      FACT.MASTERY_INDICATOR_7,
                      FACT.MASTERY_INDICATOR_8,
                      FACT.MASTERY_INDICATOR_9,
                      FACT.MASTERY_INDICATOR_10,
                      FACT.MASTERY_INDICATOR_11,
                      FACT.MASTERY_INDICATOR_12,
                      FACT.MASTERY_INDICATOR_13,
                      FACT.MASTERY_INDICATOR_14,
                      FACT.MASTERY_INDICATOR_15,
                      FACT.MASTERY_INDICATOR_16,
                      FACT.MASTERY_INDICATOR_17,
                      FACT.MASTERY_INDICATOR_18,
                      FACT.MASTERY_INDICATOR_19,
                      FACT.MASTERY_INDICATOR_20,
                      FACT.MASTERY_INDICATOR_21,
                      FACT.MASTERY_INDICATOR_22,
                      FACT.MASTERY_INDICATOR_23,
                      FACT.MASTERY_INDICATOR_24,
                      FACT.MASTERY_INDICATOR_25,
                      FACT.MASTERY_INDICATOR_26,
                      FACT.MASTERY_INDICATOR_27,
                      FACT.MASTERY_INDICATOR_28,
                      FACT.MASTERY_INDICATOR_29,
                      FACT.MASTERY_INDICATOR_30,
                      FACT.MASTERY_INDICATOR_31,
                      FACT.MASTERY_INDICATOR_32,
                      FACT.MASTERY_INDICATOR_33,
                      FACT.MASTERY_INDICATOR_34,
                      FACT.MASTERY_INDICATOR_35,
                      FACT.MASTERY_INDICATOR_36,
                      FACT.MASTERY_INDICATOR_37,
                      FACT.MASTERY_INDICATOR_38,
                      FACT.MASTERY_INDICATOR_39,
                      FACT.MASTERY_INDICATOR_40,
                      FACT.OPIIPI_1,
                      FACT.OPIIPI_2,
                      FACT.OPIIPI_3,
                      FACT.OPIIPI_4,
                      FACT.OPIIPI_5,
                      FACT.OPIIPI_6,
                      FACT.OPIIPI_7,
                      FACT.OPIIPI_8,
                      FACT.OPIIPI_9,
                      FACT.OPIIPI_10,
                      FACT.OPIIPI_11,
                      FACT.OPIIPI_12,
                      FACT.OPIIPI_13,
                      FACT.OPIIPI_14,
                      FACT.OPIIPI_15,
                      FACT.OPIIPI_16,
                      FACT.OPIIPI_17,
                      FACT.OPIIPI_18,
                      FACT.OPIIPI_19,
                      FACT.OPIIPI_20,
                      FACT.OPIIPI_21,
                      FACT.OPIIPI_22,
                      FACT.OPIIPI_23,
                      FACT.OPIIPI_24,
                      FACT.OPIIPI_25,
                      FACT.OPIIPI_26,
                      FACT.OPIIPI_27,
                      FACT.OPIIPI_28,
                      FACT.OPIIPI_29,
                      FACT.OPIIPI_30,
                      FACT.OPIIPI_31,
                      FACT.OPIIPI_32,
                      FACT.OPIIPI_33,
                      FACT.OPIIPI_34,
                      FACT.OPIIPI_35,
                      FACT.OPIIPI_36,
                      FACT.OPIIPI_37,
                      FACT.OPIIPI_38,
                      FACT.OPIIPI_39,
                      FACT.OPIIPI_40,
                      FACT.ELA_CR_SESSION_2,
                      FACT.ELA_CR_SESSION_3,
                      FACT.MATH_CR_SESSION_1,
                      FACT.SCIENCE_CR_SESSION_1 SCIENCE_CR_SESSION_4,
                      FACT.SOCIAL_CR_SESSION_1 SS_CR_SESSION_4,
                      '' FILLER_4, -- TODO -- NF
                      FACT.SPN_1,
                      FACT.SPN_2,
                      FACT.SPN_3,
                      FACT.SPN_4,
                      FACT.SPN_5,
                      '' FILLER_5, -- TODO -- NF
                      FACT.CGR,
                      FACT.IMAGEID_APPLIED_SKILLS_PP,
                      FACT.IMAGEID_APPLIED_SKILLS_OAS,
                      FACT.IMAGEID_MC_PP,
                      FACT.IMAGEID_MC_OAS,
                      FACT.BARCODE_ID_MULTIPLE_CHOICE BARCODE_ID_APPLIED_SKILLS,
                      FACT.BARCODE_ID_MULTIPLE_CHOICE,
                      FACT.TEST_FORM_SET_TO_DEFAULT_FLAG TEST_FORM_DEFAULT_FLAG,
                      FACT.MC_BLANK_BOOK_FLAG MC_BLANK_BOOK_FLAG,
                      FACT.TEST_FORM_APP_SKLS_FID_TEST TEST_FORM_AS,
                      FACT.TEST_FORM_MC_FIELD_TEST TEST_FORM_MC,
                      FACT.OAS_TSTD_IND_APPL_SKLS_TST OAS_INDICATOR_AS,
                      FACT.OAS_TESTED_INDICATOR_MC_TEST OAS_INDICATOR_MC,
                      FACT.STRUCTURE_LEVEL STRUCTURE_LEVEL,
                      FACT.ELEMENT_NUMBER ELEMENT_NUMBER,
                      FACT.RESOLVED_REPORTING_STATUS_ELA,
                      FACT.RESOLVED_REPORTING_STATUS_MATH,
                      FACT.RESLVD_REPOR_STUS_SCNCE,
                      FACT.RESLVD_REPOR_STUS__SOCSTUD,
                      FACT.OPIIPI_CUT_1 OPIIPI_CUT_1,
                      FACT.OPIIPI_CUT_2 OPIIPI_CUT_2,
                      FACT.OPIIPI_CUT_3 OPIIPI_CUT_3,
                      FACT.OPIIPI_CUT_4 OPIIPI_CUT_4,
                      FACT.OPIIPI_CUT_5 OPIIPI_CUT_5,
                      FACT.OPIIPI_CUT_6 OPIIPI_CUT_6,
                      FACT.OPIIPI_CUT_7 OPIIPI_CUT_7,
                      FACT.OPIIPI_CUT_8 OPIIPI_CUT_8,
                      FACT.OPIIPI_CUT_9 OPIIPI_CUT_9,
                      FACT.OPIIPI_CUT_10 OPIIPI_CUT_10,
                      FACT.OPIIPI_CUT_11 OPIIPI_CUT_11,
                      FACT.OPIIPI_CUT_12 OPIIPI_CUT_12,
                      FACT.OPIIPI_CUT_13 OPIIPI_CUT_13,
                      FACT.OPIIPI_CUT_14 OPIIPI_CUT_14,
                      FACT.OPIIPI_CUT_15 OPIIPI_CUT_15,
                      FACT.OPIIPI_CUT_16 OPIIPI_CUT_16,
                      FACT.OPIIPI_CUT_17 OPIIPI_CUT_17,
                      FACT.OPIIPI_CUT_18 OPIIPI_CUT_18,
                      FACT.OPIIPI_CUT_19 OPIIPI_CUT_19,
                      FACT.OPIIPI_CUT_20 OPIIPI_CUT_20,
                      FACT.OPIIPI_CUT_21 OPIIPI_CUT_21,
                      FACT.OPIIPI_CUT_22 OPIIPI_CUT_22,
                      FACT.OPIIPI_CUT_23 OPIIPI_CUT_23,
                      FACT.OPIIPI_CUT_24 OPIIPI_CUT_24,
                      FACT.OPIIPI_CUT_25 OPIIPI_CUT_25,
                      FACT.OPIIPI_CUT_26 OPIIPI_CUT_26,
                      FACT.OPIIPI_CUT_27 OPIIPI_CUT_27,
                      FACT.OPIIPI_CUT_28 OPIIPI_CUT_28,
                      FACT.OPIIPI_CUT_29 OPIIPI_CUT_29,
                      FACT.OPIIPI_CUT_30 OPIIPI_CUT_30,
                      FACT.OPIIPI_CUT_31 OPIIPI_CUT_31,
                      FACT.OPIIPI_CUT_32 OPIIPI_CUT_32,
                      FACT.OPIIPI_CUT_33 OPIIPI_CUT_33,
                      FACT.OPIIPI_CUT_34 OPIIPI_CUT_34,
                      FACT.OPIIPI_CUT_35 OPIIPI_CUT_35,
                      FACT.OPIIPI_CUT_36 OPIIPI_CUT_36,
                      FACT.OPIIPI_CUT_37 OPIIPI_CUT_37,
                      FACT.OPIIPI_CUT_38 OPIIPI_CUT_38,
                      FACT.OPIIPI_CUT_39 OPIIPI_CUT_39,
                      FACT.OPIIPI_CUT_40 OPIIPI_CUT_40,
                      FACT.GRADE_FROM_SIQ GRADE_FROM_SIQ,
                      FACT.OAS_TSTD_IND_APPL_SKLS_TST OAS_TESTED,
                      FACT.BARCODE BARCODE,
                      FACT.CTB_USE_OPUNIT OP_UNIT_NUMBER,
                      FACT.AS_PP_IREAD,
                      FACT.AS_PO_IREAD,
                      FACT.ELA_CR_SESSION_1,
                      FACT.ELA_MC_SESSION_1,
                      FACT.ELA_MC_SESSION_2,
                      FACT.ELA_MC_SESSION_3,
                      FACT.MATH_MC_SESSION_1,
                      FACT.MATH_MC_SESSION_2,
                      FACT.SCIENCE_CR_SESSION_1,
                      FACT.SOCIAL_CR_SESSION_1,
                      FACT.SCIENCE_MC_SESSION_1,
                      FACT.SCIENCE_MC_SESSION_2,
                      FACT.SOCIAL_MC_SESSION_1,
                      FACT.SOCIAL_MC_SESSION_2,
                      FACT.GRIDDED_ITEMS_STATUS,
                      FACT.GRID_ITEMS_RIGHT_RESP_ARR,
                      FACT.IMAGEID_ELA,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 1, 2))) AS_PO_IREAD_1,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 3, 2))) AS_PO_IREAD_2,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 5, 2))) AS_PO_IREAD_3,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 7, 2))) AS_PO_IREAD_4,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 9, 2))) AS_PO_IREAD_5,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 11, 2))) AS_PO_IREAD_6,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 13, 2))) AS_PO_IREAD_7,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 15, 2))) AS_PO_IREAD_8,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 17, 2))) AS_PO_IREAD_9,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 19, 2))) AS_PO_IREAD_10,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 1, 2))) AS_PP_IREAD_1,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 3, 2))) AS_PP_IREAD_2,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 5, 2))) AS_PP_IREAD_3,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 7, 2))) AS_PP_IREAD_4,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 9, 2))) AS_PP_IREAD_5,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 11, 2))) AS_PP_IREAD_6,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 13, 2))) AS_PP_IREAD_7,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 15, 2))) AS_PP_IREAD_8,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 17, 2))) AS_PP_IREAD_9,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 19, 2))) AS_PP_IREAD_10
        FROM ORG_NODE_DIM     DISTRICT,
             ORG_NODE_DIM     SCHOOL,
             ORG_LSTNODE_LINK LINK1,
             ORG_LSTNODE_LINK LINK2,
             RESULTS_GRT_FACT FACT,
             GRADE_DIM        GR
       WHERE DISTRICT.ORG_NODEID = P_IN_ORG_NODEID
         AND SCHOOL.ORG_NODEID = LINK1.ORG_NODEID
         AND LINK1.ORG_LSTNODEID = FACT.ORG_NODEID
         AND SCHOOL.PARENT_ORG_NODEID = DISTRICT.ORG_NODEID
         AND DISTRICT.ORG_NODEID = LINK2.ORG_NODEID
         AND LINK2.ORG_LSTNODEID = FACT.ORG_NODEID
         AND FACT.ISPUBLIC = P_IN_TEST_TYPE
         AND FACT.CUST_PROD_ID = CUSTPRODID
         AND FACT.GRADEID = GR.GRADEID
       ORDER BY DISTRICT.ORG_NODE_NAME,
                SCHOOL.ORG_NODE_NAME,
                GR.GRADE_CODE,
                FACT.STUDENT_LAST_NAME,
                FACT.STUDENT_FIRST_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_ALL_MIG_RESULT_GRT;

  ----PROCEDURE TO FETCH MIG_RESULTS_FACT BASED ON DISTRICTID & SCHOOLID.
  PROCEDURE SP_GET_MIG_RESULT_GRT(P_IN_PRODUCTID        IN CUST_PRODUCT_LINK.PRODUCTID%TYPE,
                                  P_IN_CUSTOMERID       IN CUST_PRODUCT_LINK.CUSTOMERID%TYPE,
                                  P_IN_DIST_ORG_NODEID  IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_SCH_ORG_NODEID   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_TEST_TYPE        IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                  P_OUT_CUR_MIG_DETAILS OUT GET_REFCURSOR,
                                  P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  
    CUSTPRODID CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
    --ADM_ID ORG_USERS.ADMINID%TYPE;
  BEGIN
  
    SELECT CUST_PROD_ID
      INTO CUSTPRODID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = P_IN_PRODUCTID
       AND CUSTOMERID = P_IN_CUSTOMERID;
  
    OPEN P_OUT_CUR_MIG_DETAILS FOR
      SELECT DISTINCT FACT.TAPE_MODE,
                      FACT.ORGTSTGPGM,
                      DISTRICT.ORG_NODE_NAME DISTRICT_NAME,
                      DISTRICT.ORG_NODE_CODE DISTRICT_ID,
                      SCHOOL.ORG_NODE_NAME SCHOOL_NAME,
                      SCHOOL.ORG_NODE_CODE SCHOOL_ID,
                      FACT.TEACHER_NAME,
                      FACT.TEACHER_ELEMENT,
                      GR.GRADE_CODE GRADEID,
                      FACT.CITY,
                      FACT.STATE,
                      FACT.ISTEP_TEST_NAME,
                      FACT.ISTEP_BOOK_NUM,
                      FACT.ISTEP_FORM,
                      FACT.TEST_DATE,
                      FACT.STUDENT_LAST_NAME,
                      FACT.STUDENT_FIRST_NAME,
                      FACT.STUDENT_MIDDLE_INITIAL,
                      FACT.STUDENTS_GENDER,
                      FACT.BIRTH_DATE,
                      FACT.CHRONOLOGICAL_AGE_IN_MONTHS,
                      FACT.ETHNICITY_HISPANIC ETHNICITY,
                      FACT.RACE_AMERICAN_INDIAN,
                      FACT.RACE_ASIAN,
                      FACT.RACE_BLACK,
                      FACT.RACE_PACIFIC_ISLANDER,
                      FACT.RACE_WHITE,
                      '' FILLER_1, -- TODO -- NF
                      FACT.STUDENT_TEST_AI,
                      FACT.LOCAL_USE_J SPECIAL_CODE_J,
                      FACT.ETHNICITY_K,
                      FACT.SPECIAL_EDUCATION_L,
                      FACT.EXCEPTIONALITY_U EXCEPTIONALITY_M,
                      FACT.SOCIOECONOMIC_STATUS_N,
                      FACT.SECTION_504_O,
                      FACT.LEPESL_V ENGLISH_LEARNER_P,
                      FACT.MIGRANT_Q,
                      FACT.LOCAL_USE_R,
                      FACT.LOCAL_USE_S,
                      FACT.LOCAL_USE_T,
                      FACT.MATCH_UNMATCH_U,
                      FACT.DUPLICATE_V,
                      FACT.NOT_USED_W CTB_USE_ONLY_W,
                      FACT.SPECIAL_CODE_X,
                      FACT.SPECIAL_CODE_Y,
                      FACT.SPECIAL_CODE_Z,
                      FACT.ACCOMMODATIONS_ELA_P ACCOMMODATIONS_ELA,
                      FACT.ACCOMMODATIONS_MATH_Q ACCOMMODATIONS_MATH,
                      FACT.ACCOMMODATIONS_SCIENCE_X ACCOMMODATIONS_SCIENCE,
                      FACT.ACCOMMODATIONS_SOCIAL ACCOMMODATIONS_SS,
                      FACT.CORPORATION_USE_ID,
                      FACT.CUSTOMER_USE,
                      '' FILLER_2, -- TODO -- NF
                      '' FILLER_3, -- TODO -- NF
                      FACT.ELA_PF_INDICATOR,
                      FACT.MATH_PF_INDICATOR,
                      FACT.SCIENCE_PF_INDICATOR,
                      FACT.SOCIAL_PF_INDICATOR,
                      FACT.ENGLANG_ARTS_NUM_CORRECT,
                      FACT.MATHEMATICS_NUM_CORRECT,
                      FACT.SCIENCE_NUM_CORRECT,
                      FACT.SOCIAL_NUM_CORRECT,
                      FACT.ENGLAN_ARTS_SCALE_SCORE,
                      FACT.MATHEMATICS_SCALE_SCORE,
                      FACT.SCIENCE_SCALE_SCORE,
                      FACT.SOCIAL_SCALE_SCORE,
                      FACT.ENGLAN_ARTS_SCALE_SCORE_SEM,
                      FACT.MATHEMATICS_SCALE_SCORE_SEM,
                      FACT.SCIENCE_SCALE_SCORE_SEM,
                      FACT.SOCIAL_SCALE_SCORE_SEM,
                      FACT.MASTERY_INDICATOR_1,
                      FACT.MASTERY_INDICATOR_2,
                      FACT.MASTERY_INDICATOR_3,
                      FACT.MASTERY_INDICATOR_4,
                      FACT.MASTERY_INDICATOR_5,
                      FACT.MASTERY_INDICATOR_6,
                      FACT.MASTERY_INDICATOR_7,
                      FACT.MASTERY_INDICATOR_8,
                      FACT.MASTERY_INDICATOR_9,
                      FACT.MASTERY_INDICATOR_10,
                      FACT.MASTERY_INDICATOR_11,
                      FACT.MASTERY_INDICATOR_12,
                      FACT.MASTERY_INDICATOR_13,
                      FACT.MASTERY_INDICATOR_14,
                      FACT.MASTERY_INDICATOR_15,
                      FACT.MASTERY_INDICATOR_16,
                      FACT.MASTERY_INDICATOR_17,
                      FACT.MASTERY_INDICATOR_18,
                      FACT.MASTERY_INDICATOR_19,
                      FACT.MASTERY_INDICATOR_20,
                      FACT.MASTERY_INDICATOR_21,
                      FACT.MASTERY_INDICATOR_22,
                      FACT.MASTERY_INDICATOR_23,
                      FACT.MASTERY_INDICATOR_24,
                      FACT.MASTERY_INDICATOR_25,
                      FACT.MASTERY_INDICATOR_26,
                      FACT.MASTERY_INDICATOR_27,
                      FACT.MASTERY_INDICATOR_28,
                      FACT.MASTERY_INDICATOR_29,
                      FACT.MASTERY_INDICATOR_30,
                      FACT.MASTERY_INDICATOR_31,
                      FACT.MASTERY_INDICATOR_32,
                      FACT.MASTERY_INDICATOR_33,
                      FACT.MASTERY_INDICATOR_34,
                      FACT.MASTERY_INDICATOR_35,
                      FACT.MASTERY_INDICATOR_36,
                      FACT.MASTERY_INDICATOR_37,
                      FACT.MASTERY_INDICATOR_38,
                      FACT.MASTERY_INDICATOR_39,
                      FACT.MASTERY_INDICATOR_40,
                      FACT.OPIIPI_1,
                      FACT.OPIIPI_2,
                      FACT.OPIIPI_3,
                      FACT.OPIIPI_4,
                      FACT.OPIIPI_5,
                      FACT.OPIIPI_6,
                      FACT.OPIIPI_7,
                      FACT.OPIIPI_8,
                      FACT.OPIIPI_9,
                      FACT.OPIIPI_10,
                      FACT.OPIIPI_11,
                      FACT.OPIIPI_12,
                      FACT.OPIIPI_13,
                      FACT.OPIIPI_14,
                      FACT.OPIIPI_15,
                      FACT.OPIIPI_16,
                      FACT.OPIIPI_17,
                      FACT.OPIIPI_18,
                      FACT.OPIIPI_19,
                      FACT.OPIIPI_20,
                      FACT.OPIIPI_21,
                      FACT.OPIIPI_22,
                      FACT.OPIIPI_23,
                      FACT.OPIIPI_24,
                      FACT.OPIIPI_25,
                      FACT.OPIIPI_26,
                      FACT.OPIIPI_27,
                      FACT.OPIIPI_28,
                      FACT.OPIIPI_29,
                      FACT.OPIIPI_30,
                      FACT.OPIIPI_31,
                      FACT.OPIIPI_32,
                      FACT.OPIIPI_33,
                      FACT.OPIIPI_34,
                      FACT.OPIIPI_35,
                      FACT.OPIIPI_36,
                      FACT.OPIIPI_37,
                      FACT.OPIIPI_38,
                      FACT.OPIIPI_39,
                      FACT.OPIIPI_40,
                      FACT.ELA_CR_SESSION_2,
                      FACT.ELA_CR_SESSION_3,
                      FACT.MATH_CR_SESSION_1,
                      FACT.SCIENCE_CR_SESSION_1 SCIENCE_CR_SESSION_4,
                      FACT.SOCIAL_CR_SESSION_1 SS_CR_SESSION_4,
                      '' FILLER_4, -- TODO -- NF
                      FACT.SPN_1,
                      FACT.SPN_2,
                      FACT.SPN_3,
                      FACT.SPN_4,
                      FACT.SPN_5,
                      '' FILLER_5, -- TODO -- NF
                      FACT.CGR,
                      FACT.IMAGEID_APPLIED_SKILLS_PP,
                      FACT.IMAGEID_APPLIED_SKILLS_OAS,
                      FACT.IMAGEID_MC_PP,
                      FACT.IMAGEID_MC_OAS,
                      FACT.BARCODE_ID_MULTIPLE_CHOICE BARCODE_ID_APPLIED_SKILLS,
                      FACT.BARCODE_ID_MULTIPLE_CHOICE,
                      FACT.TEST_FORM_SET_TO_DEFAULT_FLAG TEST_FORM_DEFAULT_FLAG,
                      FACT.MC_BLANK_BOOK_FLAG MC_BLANK_BOOK_FLAG,
                      FACT.TEST_FORM_APP_SKLS_FID_TEST TEST_FORM_AS,
                      FACT.TEST_FORM_MC_FIELD_TEST TEST_FORM_MC,
                      FACT.OAS_TSTD_IND_APPL_SKLS_TST OAS_INDICATOR_AS,
                      FACT.OAS_TESTED_INDICATOR_MC_TEST OAS_INDICATOR_MC,
                      FACT.STRUCTURE_LEVEL STRUCTURE_LEVEL,
                      FACT.ELEMENT_NUMBER ELEMENT_NUMBER,
                      FACT.RESOLVED_REPORTING_STATUS_ELA,
                      FACT.RESOLVED_REPORTING_STATUS_MATH,
                      FACT.RESLVD_REPOR_STUS_SCNCE,
                      FACT.RESLVD_REPOR_STUS__SOCSTUD,
                      FACT.OPIIPI_CUT_1 OPIIPI_CUT_1,
                      FACT.OPIIPI_CUT_2 OPIIPI_CUT_2,
                      FACT.OPIIPI_CUT_3 OPIIPI_CUT_3,
                      FACT.OPIIPI_CUT_4 OPIIPI_CUT_4,
                      FACT.OPIIPI_CUT_5 OPIIPI_CUT_5,
                      FACT.OPIIPI_CUT_6 OPIIPI_CUT_6,
                      FACT.OPIIPI_CUT_7 OPIIPI_CUT_7,
                      FACT.OPIIPI_CUT_8 OPIIPI_CUT_8,
                      FACT.OPIIPI_CUT_9 OPIIPI_CUT_9,
                      FACT.OPIIPI_CUT_10 OPIIPI_CUT_10,
                      FACT.OPIIPI_CUT_11 OPIIPI_CUT_11,
                      FACT.OPIIPI_CUT_12 OPIIPI_CUT_12,
                      FACT.OPIIPI_CUT_13 OPIIPI_CUT_13,
                      FACT.OPIIPI_CUT_14 OPIIPI_CUT_14,
                      FACT.OPIIPI_CUT_15 OPIIPI_CUT_15,
                      FACT.OPIIPI_CUT_16 OPIIPI_CUT_16,
                      FACT.OPIIPI_CUT_17 OPIIPI_CUT_17,
                      FACT.OPIIPI_CUT_18 OPIIPI_CUT_18,
                      FACT.OPIIPI_CUT_19 OPIIPI_CUT_19,
                      FACT.OPIIPI_CUT_20 OPIIPI_CUT_20,
                      FACT.OPIIPI_CUT_21 OPIIPI_CUT_21,
                      FACT.OPIIPI_CUT_22 OPIIPI_CUT_22,
                      FACT.OPIIPI_CUT_23 OPIIPI_CUT_23,
                      FACT.OPIIPI_CUT_24 OPIIPI_CUT_24,
                      FACT.OPIIPI_CUT_25 OPIIPI_CUT_25,
                      FACT.OPIIPI_CUT_26 OPIIPI_CUT_26,
                      FACT.OPIIPI_CUT_27 OPIIPI_CUT_27,
                      FACT.OPIIPI_CUT_28 OPIIPI_CUT_28,
                      FACT.OPIIPI_CUT_29 OPIIPI_CUT_29,
                      FACT.OPIIPI_CUT_30 OPIIPI_CUT_30,
                      FACT.OPIIPI_CUT_31 OPIIPI_CUT_31,
                      FACT.OPIIPI_CUT_32 OPIIPI_CUT_32,
                      FACT.OPIIPI_CUT_33 OPIIPI_CUT_33,
                      FACT.OPIIPI_CUT_34 OPIIPI_CUT_34,
                      FACT.OPIIPI_CUT_35 OPIIPI_CUT_35,
                      FACT.OPIIPI_CUT_36 OPIIPI_CUT_36,
                      FACT.OPIIPI_CUT_37 OPIIPI_CUT_37,
                      FACT.OPIIPI_CUT_38 OPIIPI_CUT_38,
                      FACT.OPIIPI_CUT_39 OPIIPI_CUT_39,
                      FACT.OPIIPI_CUT_40 OPIIPI_CUT_40,
                      FACT.GRADE_FROM_SIQ GRADE_FROM_SIQ,
                      FACT.OAS_TSTD_IND_APPL_SKLS_TST OAS_TESTED,
                      FACT.BARCODE BARCODE,
                      FACT.CTB_USE_OPUNIT OP_UNIT_NUMBER,
                      FACT.AS_PP_IREAD,
                      FACT.AS_PO_IREAD,
                      FACT.ELA_CR_SESSION_1,
                      FACT.ELA_MC_SESSION_1,
                      FACT.ELA_MC_SESSION_2,
                      FACT.ELA_MC_SESSION_3,
                      FACT.MATH_MC_SESSION_1,
                      FACT.MATH_MC_SESSION_2,
                      FACT.SCIENCE_CR_SESSION_1,
                      FACT.SOCIAL_CR_SESSION_1,
                      FACT.SCIENCE_MC_SESSION_1,
                      FACT.SCIENCE_MC_SESSION_2,
                      FACT.SOCIAL_MC_SESSION_1,
                      FACT.SOCIAL_MC_SESSION_2,
                      FACT.GRIDDED_ITEMS_STATUS,
                      FACT.GRID_ITEMS_RIGHT_RESP_ARR,
                      FACT.IMAGEID_ELA,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 1, 2))) AS_PO_IREAD_1,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 3, 2))) AS_PO_IREAD_2,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 5, 2))) AS_PO_IREAD_3,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 7, 2))) AS_PO_IREAD_4,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 9, 2))) AS_PO_IREAD_5,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 11, 2))) AS_PO_IREAD_6,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 13, 2))) AS_PO_IREAD_7,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 15, 2))) AS_PO_IREAD_8,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 17, 2))) AS_PO_IREAD_9,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PO_IREAD, 19, 2))) AS_PO_IREAD_10,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 1, 2))) AS_PP_IREAD_1,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 3, 2))) AS_PP_IREAD_2,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 5, 2))) AS_PP_IREAD_3,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 7, 2))) AS_PP_IREAD_4,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 9, 2))) AS_PP_IREAD_5,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 11, 2))) AS_PP_IREAD_6,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 13, 2))) AS_PP_IREAD_7,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 15, 2))) AS_PP_IREAD_8,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 17, 2))) AS_PP_IREAD_9,
                      LTRIM(RTRIM(SUBSTR(FACT.AS_PP_IREAD, 19, 2))) AS_PP_IREAD_10
        FROM ORG_NODE_DIM     DISTRICT,
             ORG_NODE_DIM     SCHOOL,
             ORG_LSTNODE_LINK LINK1,
             ORG_LSTNODE_LINK LINK2,
             RESULTS_GRT_FACT FACT,
             GRADE_DIM        GR
       WHERE DISTRICT.ORG_NODEID = P_IN_DIST_ORG_NODEID
         AND SCHOOL.ORG_NODEID = P_IN_SCH_ORG_NODEID
         AND SCHOOL.ORG_NODEID = LINK1.ORG_NODEID
         AND LINK1.ORG_LSTNODEID = FACT.ORG_NODEID
         AND SCHOOL.PARENT_ORG_NODEID = DISTRICT.ORG_NODEID
         AND DISTRICT.ORG_NODEID = LINK2.ORG_NODEID
         AND LINK2.ORG_LSTNODEID = FACT.ORG_NODEID
         AND FACT.ISPUBLIC = P_IN_TEST_TYPE
         AND FACT.CUST_PROD_ID = CUSTPRODID
         AND FACT.GRADEID = GR.GRADEID
       ORDER BY DISTRICT.ORG_NODE_NAME,
                SCHOOL.ORG_NODE_NAME,
                GR.GRADE_CODE,
                FACT.STUDENT_LAST_NAME,
                FACT.STUDENT_FIRST_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_MIG_RESULT_GRT;

  ---PROCEDURE TO FETCH INVITATION DETAILS  
  PROCEDURE SP_GET_ALL_INVITATION_DETAILS(P_IN_PRODUCTID               IN PRODUCT.PRODUCTID%TYPE,
                                          P_IN_ORG_NODEID              IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                          P_IN_TEST_TYPE               IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                          P_OUT_CUR_INVITATION_DETAILS OUT GET_REFCURSOR,
                                          P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_INVITATION_DETAILS FOR
      SELECT DISTINCT DISTRICT.ORG_NODE_NAME DISTRICT_NAME,
                      DISTRICT.ORG_NODE_CODE DISTRICT_NUMBER,
                      SCHOOL.ORG_NODE_NAME SCHOOL_NAME,
                      SCHOOL.ORG_NODE_CODE SCHOOL_NUMBER,
                      GR.GRADE_CODE,
                      TO_NUMBER(GR.GRADE_CODE) GRADE_NAME,
                      RGF.ISTEP_TEST_NAME,
                      IC.INVITATION_CODE,
                      TO_CHAR(IC.EXPIRATION_DATE, 'MMDDYY') EXPIRATION_DATE,
                      SBD.LAST_NAME,
                      SBD.FIRST_NAME,
                      SBD.MIDDLE_NAME,
                      GENDER.GENDER_CODE,
                      SBD.BIRTHDATE,
                      RGF.STUDENT_TEST_AI,
                      RGF.CORPORATION_USE_ID,
                      RGF.BARCODE_ID_MULTIPLE_CHOICE,
                      RGF.TEACHER_NAME,
                      RGF.ORGTSTGPGM,
                      RGF.TEACHER_ELEMENT,
                      RGF.ELEMENT_NUMBER STUDENT_ELEMENT_NUMBER,
                      SBD.BARCODE STUDENT_BARCODE
        FROM (SELECT STUDENT_BIO_ID,
                     ORG_NODEID,
                     TEST_ELEMENT_ID,
                     GRADEID,
                     GENDERID,
                     LAST_NAME,
                     FIRST_NAME,
                     MIDDLE_NAME,
                     BIRTHDATE,
                     EXT_STUDENT_ID,
                     BARCODE
                FROM STUDENT_BIO_DIM) SBD,
             (SELECT STUDENT_BIO_ID,
                     ISPUBLIC,
                     BARCODE_ID_MULTIPLE_CHOICE,
                     TEACHER_NAME,
                     ORGTSTGPGM,
                     TEACHER_ELEMENT,
                     ELEMENT_NUMBER,
                     CORPORATION_USE_ID,
                     ISTEP_TEST_NAME,
                     STUDENT_TEST_AI
                FROM RESULTS_GRT_FACT
               WHERE ISPUBLIC = P_IN_TEST_TYPE) RGF,
             (SELECT ORG_NODEID, ORG_NODE_CODE, ORG_NODE_NAME
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 2
                 AND ORG_NODEID = P_IN_ORG_NODEID) DISTRICT,
             (SELECT ORG_NODEID,
                     ORG_NODE_CODE,
                     ORG_NODE_NAME,
                     PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 3) SCHOOL,
             (SELECT ORG_NODEID, ORG_NODE_NAME, PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 4) KLASS,
             (SELECT GRADEID, GRADE_CODE FROM GRADE_DIM) GR,
             (SELECT GENDERID, GENDER_CODE FROM GENDER_DIM) GENDER,
             (SELECT TEST_ELEMENT_ID,
                     CUST_PROD_ID,
                     INVITATION_CODE,
                     EXPIRATION_DATE
                FROM INVITATION_CODE) IC,
             (SELECT PRODUCTID, CUST_PROD_ID FROM CUST_PRODUCT_LINK) CPL,
             (SELECT PRODUCTID, PRODUCT_NAME
                FROM PRODUCT
               WHERE PRODUCTID = P_IN_PRODUCTID) P
       WHERE SBD.STUDENT_BIO_ID = RGF.STUDENT_BIO_ID
         AND SBD.ORG_NODEID = KLASS.ORG_NODEID
         AND KLASS.PARENT_ORG_NODEID = SCHOOL.ORG_NODEID
         AND SCHOOL.PARENT_ORG_NODEID = DISTRICT.ORG_NODEID
         AND SBD.TEST_ELEMENT_ID = IC.TEST_ELEMENT_ID
         AND SBD.GRADEID = GR.GRADEID
         AND SBD.GENDERID = GENDER.GENDERID
         AND CPL.PRODUCTID = P.PRODUCTID
         AND IC.CUST_PROD_ID = CPL.CUST_PROD_ID
         AND IC.EXPIRATION_DATE >= SYSDATE
       ORDER BY DISTRICT.ORG_NODE_NAME,
                SCHOOL.ORG_NODE_NAME,
                GR.GRADE_CODE,
                SBD.LAST_NAME,
                SBD.FIRST_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_ALL_INVITATION_DETAILS;

  ---PROCEDURE TO FETCH INVITATION DETAILS  
  PROCEDURE SP_GET_INVITATION_DETAILS(P_IN_PRODUCTID               IN PRODUCT.PRODUCTID%TYPE,
                                      P_IN_ORG_NODEID              IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                      P_IN_TEST_TYPE               IN RESULTS_GRT_FACT.ISPUBLIC%TYPE,
                                      P_OUT_CUR_INVITATION_DETAILS OUT GET_REFCURSOR,
                                      P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_INVITATION_DETAILS FOR
      SELECT DISTINCT DISTRICT.ORG_NODE_NAME DISTRICT_NAME,
                      DISTRICT.ORG_NODE_CODE DISTRICT_NUMBER,
                      SCHOOL.ORG_NODE_NAME SCHOOL_NAME,
                      SCHOOL.ORG_NODE_CODE SCHOOL_NUMBER,
                      GR.GRADE_CODE,
                      TO_NUMBER(GR.GRADE_CODE) GRADE_NAME,
                      RGF.ISTEP_TEST_NAME,
                      IC.INVITATION_CODE,
                      TO_CHAR(IC.EXPIRATION_DATE, 'MMDDYY') EXPIRATION_DATE,
                      SBD.LAST_NAME,
                      SBD.FIRST_NAME,
                      SBD.MIDDLE_NAME,
                      GENDER.GENDER_CODE,
                      SBD.BIRTHDATE,
                      RGF.STUDENT_TEST_AI,
                      RGF.CORPORATION_USE_ID,
                      RGF.BARCODE_ID_MULTIPLE_CHOICE,
                      RGF.TEACHER_NAME,
                      RGF.ORGTSTGPGM,
                      RGF.TEACHER_ELEMENT,
                      RGF.ELEMENT_NUMBER STUDENT_ELEMENT_NUMBER,
                      SBD.BARCODE STUDENT_BARCODE
        FROM (SELECT STUDENT_BIO_ID,
                     ORG_NODEID,
                     TEST_ELEMENT_ID,
                     GRADEID,
                     GENDERID,
                     LAST_NAME,
                     FIRST_NAME,
                     MIDDLE_NAME,
                     BIRTHDATE,
                     EXT_STUDENT_ID,
                     BARCODE
                FROM STUDENT_BIO_DIM) SBD,
             (SELECT STUDENT_BIO_ID,
                     ISPUBLIC,
                     BARCODE_ID_MULTIPLE_CHOICE,
                     TEACHER_NAME,
                     ORGTSTGPGM,
                     TEACHER_ELEMENT,
                     ELEMENT_NUMBER,
                     CORPORATION_USE_ID,
                     ISTEP_TEST_NAME,
                     STUDENT_TEST_AI
                FROM RESULTS_GRT_FACT
               WHERE ISPUBLIC = P_IN_TEST_TYPE) RGF,
             (SELECT ORG_NODEID, ORG_NODE_CODE, ORG_NODE_NAME
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 2) DISTRICT,
             (SELECT ORG_NODEID,
                     ORG_NODE_CODE,
                     ORG_NODE_NAME,
                     PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 3
                 AND ORG_NODEID = P_IN_ORG_NODEID) SCHOOL,
             (SELECT ORG_NODEID, ORG_NODE_NAME, PARENT_ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 4) KLASS,
             (SELECT GRADEID, GRADE_CODE FROM GRADE_DIM) GR,
             (SELECT GENDERID, GENDER_CODE FROM GENDER_DIM) GENDER,
             (SELECT TEST_ELEMENT_ID,
                     CUST_PROD_ID,
                     INVITATION_CODE,
                     EXPIRATION_DATE
                FROM INVITATION_CODE) IC,
             (SELECT PRODUCTID, CUST_PROD_ID FROM CUST_PRODUCT_LINK) CPL,
             (SELECT PRODUCTID, PRODUCT_NAME
                FROM PRODUCT
               WHERE PRODUCTID = P_IN_PRODUCTID) P
       WHERE SBD.STUDENT_BIO_ID = RGF.STUDENT_BIO_ID
         AND SBD.ORG_NODEID = KLASS.ORG_NODEID
         AND KLASS.PARENT_ORG_NODEID = SCHOOL.ORG_NODEID
         AND SCHOOL.PARENT_ORG_NODEID = DISTRICT.ORG_NODEID
         AND SBD.TEST_ELEMENT_ID = IC.TEST_ELEMENT_ID
         AND SBD.GRADEID = GR.GRADEID
         AND SBD.GENDERID = GENDER.GENDERID
         AND CPL.PRODUCTID = P.PRODUCTID
         AND IC.CUST_PROD_ID = CPL.CUST_PROD_ID
         AND IC.EXPIRATION_DATE >= SYSDATE
       ORDER BY DISTRICT.ORG_NODE_NAME,
                SCHOOL.ORG_NODE_NAME,
                GR.GRADE_CODE,
                SBD.LAST_NAME,
                SBD.FIRST_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_INVITATION_DETAILS;
  --END OF PACKAGE
END PKG_GET_MIG_RESULTS_GRT;
/
