-- Run this script in the ISTEP Production Environment -- 
----(remember to change the schema name of ISTEP blank schema) 

REVOKE SELECT ON ORG_NODE_DIM FROM ISTEP_POC;
REVOKE SELECT ON ORG_PRODUCT_LINK FROM ISTEP_POC;
REVOKE SELECT ON ORG_TEST_PROGRAM_LINK FROM ISTEP_POC;
REVOKE SELECT ON USERS FROM ISTEP_POC;
REVOKE SELECT ON ORG_USERS FROM ISTEP_POC;
REVOKE SELECT ON EDU_CENTER_DETAILS FROM ISTEP_POC;
REVOKE SELECT ON EDU_CENTER_USER_LINK FROM ISTEP_POC;
REVOKE SELECT ON STUDENT_BIO_DIM FROM ISTEP_POC;
REVOKE SELECT ON SUBTEST_SCORE_FACT FROM ISTEP_POC;
REVOKE SELECT ON OBJECTIVE_SCORE_FACT FROM ISTEP_POC;
REVOKE SELECT ON ITEM_SCORE_FACT FROM ISTEP_POC;
REVOKE SELECT ON GRADE_SELECTION_LOOKUP FROM ISTEP_POC;
REVOKE SELECT ON DEMOGRAPHIC FROM ISTEP_POC;
REVOKE SELECT ON DEMOGRAPHIC_VALUES FROM ISTEP_POC;
REVOKE SELECT ON STU_SUBTEST_DEMO_VALUES FROM ISTEP_POC;
REVOKE SELECT ON STUDENT_PDF_FILES FROM ISTEP_POC;
REVOKE SELECT ON USERS_MAP FROM ISTEP_POC;
REVOKE SELECT ON ORG_LSTNODE_LINK FROM ISTEP_POC;
REVOKE SELECT ON STUDENT_DEMO_VALUES FROM ISTEP_POC;
REVOKE SELECT ON ORG_PDF_FILES FROM ISTEP_POC;
REVOKE SELECT ON INVITATION_CODE FROM ISTEP_POC;
REVOKE SELECT ON INVITATION_CODE_CLAIM FROM ISTEP_POC;
REVOKE SELECT ON INVITATION_CODE_LOOKUP FROM ISTEP_POC;
REVOKE SELECT ON USC_LINK FROM ISTEP_POC;
REVOKE SELECT ON USER_ROLE FROM ISTEP_POC;
REVOKE SELECT ON USER_SELECTION_LOOKUP FROM ISTEP_POC;
REVOKE SELECT ON PWD_HINT_ANSWERS FROM ISTEP_POC;
REVOKE SELECT ON ARTICLE_CONTENT FROM ISTEP_POC;
REVOKE SELECT ON ARTICLE_METADATA FROM ISTEP_POC;
REVOKE SELECT ON USER_ACTIVITY_HISTORY FROM ISTEP_POC;
REVOKE SELECT ON RESCORE_REQUEST_FORM FROM ISTEP_POC;
REVOKE SELECT ON JOB_TRACKING FROM ISTEP_POC;
REVOKE SELECT ON BIO_STUDENT_EXTRACT FROM ISTEP_POC;
REVOKE SELECT ON PERF_MATRIX_FACT FROM ISTEP_POC;
REVOKE SELECT ON CUTSCOREIPI FROM ISTEP_POC;
REVOKE SELECT ON CUTSCORESCALESCORE FROM ISTEP_POC;
REVOKE SELECT ON STATE_MEAN_IPI_SCORE FROM ISTEP_POC;
REVOKE SELECT ON DISAGGREGATION_CATEGORY FROM ISTEP_POC;
REVOKE SELECT ON DISAGGREGATION_CATEGORY_TYPE FROM ISTEP_POC;
REVOKE SELECT ON ASFD_ORDERBY FROM ISTEP_POC;
REVOKE SELECT ON ASFD_FACT FROM ISTEP_POC;
REVOKE SELECT ON STFD_FACT FROM ISTEP_POC;
REVOKE SELECT ON DISA_FACT FROM ISTEP_POC;
REVOKE SELECT ON SPPR_FACT FROM ISTEP_POC;
REVOKE SELECT ON ACAD_STD_SUMM_FACT FROM ISTEP_POC;
REVOKE SELECT ON UDTR_ROSTER_FACT FROM ISTEP_POC;
REVOKE SELECT ON UDTR_SUMM_FACT FROM ISTEP_POC;
REVOKE SELECT ON CLASS_SUMM_FACT FROM ISTEP_POC;
REVOKE SELECT ON SUMT_FACT FROM ISTEP_POC;
REVOKE SELECT ON MEDIA_FACT FROM ISTEP_POC;
REVOKE SELECT ON RESULTS_GRT_FACT FROM ISTEP_POC;
REVOKE SELECT ON USABILITY_LOG FROM ISTEP_POC;
REVOKE SELECT ON STU_DEMO_VAL FROM ISTEP_POC;
REVOKE SELECT ON STU_SUB_DETAILS FROM ISTEP_POC;
REVOKE SELECT ON PERF_LOG FROM ISTEP_POC;
REVOKE SELECT ON ORGUSER_DETAILS FROM ISTEP_POC;
REVOKE SELECT ON STG_DATA_LAYOUT_CONFIG FROM ISTEP_POC;
REVOKE SELECT ON STG_ETL_JOBMASTER FROM ISTEP_POC;
REVOKE SELECT ON STG_PROCESS_STATUS FROM ISTEP_POC;