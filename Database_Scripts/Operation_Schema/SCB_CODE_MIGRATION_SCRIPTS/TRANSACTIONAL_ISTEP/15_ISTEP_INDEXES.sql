CREATE INDEX IDX_ORG_NODE_DIM_1 ON ORG_NODE_DIM (ORG_NODE_CODE) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_NODE_DIM_2 ON ORG_NODE_DIM (PARENT_ORG_NODEID, ORG_NODEID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_ORG_PRODUCT_LINK ON ORG_PRODUCT_LINK (CUST_PROD_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_PRODUCT_LINK_2 ON ORG_PRODUCT_LINK (ORG_NODEID, CUST_PROD_ID) TABLESPACE FCAT_IDX1;

CREATE UNIQUE INDEX IDX_USERNAME_1 ON USERS (UPPER(USERNAME)) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_ORG_USERS_1 ON ORG_USERS (USERID, ORG_NODE_LEVEL) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_USERS_2 ON ORG_USERS (ORG_NODEID, ORG_USER_ID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_STUDENT_BIO_DIM_1 ON STUDENT_BIO_DIM (ORG_NODEID, GRADEID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_STUDENT_BIO_DIM_9 ON STUDENT_BIO_DIM (TEST_ELEMENT_ID, ORG_NODEID) TABLESPACE FCAT_IDX1;

CREATE BITMAP INDEX BIDX_SUBTEST_SCORE_FACT_1 ON SUBTEST_SCORE_FACT (CUST_PROD_ID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_SUBTEST_SCORE_FACT_2 ON SUBTEST_SCORE_FACT (STUDENT_BIO_ID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_SUBTEST_SCORE_FACT_3 ON SUBTEST_SCORE_FACT (SUBTESTID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_SUBTEST_SCORE_FACT_4 ON SUBTEST_SCORE_FACT (GRADEID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_SUBTEST_SCORE_FACT_5 ON SUBTEST_SCORE_FACT (ADMINID) LOCAL TABLESPACE FCAT_IDX1;

CREATE BITMAP INDEX BIDX_OBJECTIVE_SCORE_FACT_1 ON OBJECTIVE_SCORE_FACT (CUST_PROD_ID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_OBJECTIVE_SCORE_FACT_2 ON OBJECTIVE_SCORE_FACT (STUDENT_BIO_ID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_OBJECTIVE_SCORE_FACT_3 ON OBJECTIVE_SCORE_FACT (SUBTESTID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_OBJECTIVE_SCORE_FACT_4 ON OBJECTIVE_SCORE_FACT (OBJECTIVEID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_OBJECTIVE_SCORE_FACT_5 ON OBJECTIVE_SCORE_FACT (GRADEID) LOCAL TABLESPACE FCAT_IDX1;
CREATE BITMAP INDEX BIDX_OBJECTIVE_SCORE_FACT_6 ON OBJECTIVE_SCORE_FACT (ADMINID) LOCAL TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_STUDENT_PDF_FILES ON STUDENT_PDF_FILES (PDF_REPORTID, STUDENT_BIO_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_STUDENT_PDF_FILES_1 ON STUDENT_PDF_FILES (PDF_REPORTID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_STUDENT_DEMO_VALUES_1 ON STUDENT_DEMO_VALUES (STUDENT_BIO_ID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_INVITATION_CODE_1 ON INVITATION_CODE (STUDENT_BIO_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_INVITATION_CODE_2 ON INVITATION_CODE (TEST_ELEMENT_ID, GRADE_ID, CUST_PROD_ID, ACTIVATION_STATUS, IS_NEW_IC) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_INVITATION_CODE_3 ON INVITATION_CODE (STUDENT_BIO_ID, CUST_PROD_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_INVITATION_CODE_4 ON INVITATION_CODE (INT_STUDENT_ID, CUST_PROD_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_INVITATION_CODE_5 ON INVITATION_CODE (INVITATION_CODE) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_INVITATION_CODE_6 ON INVITATION_CODE (NVL(INT_STUDENT_ID,TEST_ELEMENT_ID), CUST_PROD_ID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_INVITATION_CODE_CLAIM_1 ON INVITATION_CODE_CLAIM (ORG_USER_ID, ICID, ACTIVATION_STATUS) TABLESPACE FCAT_IDX1; 

CREATE INDEX RESCORE_REQUEST_FORM_INDX1 ON RESCORE_REQUEST_FORM (STUDENT_BIO_ID, CUST_PROD_ID, ORG_NODEID, ITEMSETID, GRADEID, SUBTESTID, OBJECTIVEID) TABLESPACE FCAT_IDX1;
CREATE INDEX RESCORE_REQUEST_FORM_INDX2 ON RESCORE_REQUEST_FORM (STUDENT_BIO_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX RESCORE_REQUEST_FORM_INDX3 ON RESCORE_REQUEST_FORM (UPDATED_DATE_TIME) TABLESPACE FCAT_IDX1;
CREATE INDEX RESCORE_REQUEST_FORM_INDX6 ON RESCORE_REQUEST_FORM (STUDENT_BIO_ID, ORIGINAL_PERFORMANCE_LEVEL) TABLESPACE FCAT_IDX1;
CREATE INDEX RESCORE_REQUEST_FORM_INDX7 ON RESCORE_REQUEST_FORM (ELIGIBLE_FOR_RESCORE, ORG_NODEID, GRADEID, CUST_PROD_ID, STUDENT_BIO_ID, ITEMSETID, SUBTESTID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_JOB_TRACKING_1 ON JOB_TRACKING (USERID, JOB_STATUS) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_BIO_STUDENT_EXTRACT_1 ON BIO_STUDENT_EXTRACT (BIO_EXTRACTID, SCHOOL_ORG_NODEID, CUST_PROD_ID, TEST_ELEMENT_ID, GRADE) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_BIO_STUDENT_EXTRACT_2 ON BIO_STUDENT_EXTRACT (TEST_ELEMENT_ID, CUST_PROD_ID, INT_STUDENT_ID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_PERF_MATRIX_FACT_1 ON PERF_MATRIX_FACT (USER_ID, CUST_PROD_ID, SUBTESTID, CURR_GRADEID, PREV_GRADEID, CURR_ADMINID, PREV_ADMINID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_PERF_MATRIX_FACT_2 ON PERF_MATRIX_FACT (USER_ID, CUST_PROD_ID, SUBTESTID, CURR_GRADEID, PREV_GRADEID, CURR_ADMINID, PREV_ADMINID, PERF_LEVEL) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_CUTSCORESCALESCORE_1 ON CUTSCORESCALESCORE (CUST_PROD_ID, GRADEID, LEVELID, SUBTESTID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_ASFD_ORDERBY_1 ON ASFD_ORDERBY (CUST_PROD_ID, GRADEID, SUBTESTID, OBJECTIVEID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_ASFD_FACT_1 ON ASFD_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, ISPUBLIC, GRADEID, LEVELID, SUBTESTID, OBJECTIVEID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_STFD1 ON STFD_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC, SUBTESTID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_DISA_FACT_1 ON DISA_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, ISPUBLIC, GRADEID, LEVELID, SUBTESTID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_SPPR1 ON SPPR_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC, SUBTESTID) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_ACAD_STD_SUMM_FACT_2 ON ACAD_STD_SUMM_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC, CTB_OBJECTIVE_CODE) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ACAD_STD_SUMM_FACT_3 ON ACAD_STD_SUMM_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_CLASS_SUMM_FACT_1 ON CLASS_SUMM_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_SUMT1 ON SUMT_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_MEDIA1 ON MEDIA_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC) TABLESPACE FCAT_IDX1;

CREATE INDEX IDX_RESULTS_GRT_FACT1 ON RESULTS_GRT_FACT (CUST_PROD_ID, ADMINID, ORG_NODEID, GRADEID, ORGTSTGPGM, ISPUBLIC) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_RESULTS_GRT_FACT2 ON RESULTS_GRT_FACT (CUST_PROD_ID, ORG_NODEID, ADMINID, GRADEID, ISPUBLIC) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_RESULTS_GRT_FACT_2 ON RESULTS_GRT_FACT (CUST_PROD_ID, ORG_NODEID, GRADEID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_RESULTS_GRT_FACT_3 ON RESULTS_GRT_FACT (STUDENTID, ORGTSTGPGM, ELEMENT_NUMBER, STRUCTURE_LEVEL) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_RESULTS_GRT_FACT_4 ON RESULTS_GRT_FACT (CUST_PROD_ID, STUDENT_TEST_AI) TABLESPACE FCAT_IDX1;
CREATE INDEX RESULTS_GRT_FACT_STUD_BIO_ID ON RESULTS_GRT_FACT (STUDENT_BIO_ID) TABLESPACE FCAT_IDX1;

create index IDX_ORGUSER_DETAILS_1 on ORGUSER_DETAILS (USERID, ORG_NODEID, CUSTOMERID, ADMINID);