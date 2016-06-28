--ETL load
CREATE INDEX IDX_STG_ITEM_RESP_DETAILS_1 ON STG_ITEM_RESPONSE_DETAILS (PROCESS_ID,TASK_ID,GRADE,ITEM_TYPE,ITEM_CODE,CONTENT_CODE,TEST_FORM);
CREATE INDEX IDX_STG_STD_SUBTEST_DETAILS_31 ON STG_STD_SUBTEST_DETAILS (PROCESS_ID,TASK_ID,CONTENT_NAME,TEST_FORM,GRADE_ID);
CREATE INDEX IDX_STUDENT_BIO_DETAILS_ID ON STG_STD_SUBTEST_DETAILS (STUDENT_BIO_DETAILS_ID);
CREATE INDEX IDX_STUDENT_MAP_21 ON STUDENT_MAP (STUDENT_BIO_DETAILS_ID,PROCESS_ID,CUSTOMERID);
create index IDX_STUDENT_TEMP_DATA_TEMP on STUDENT_TEMP_DATA (STUDENT_BIO_ID);

--MV load
CREATE INDEX IDX_STUDENT_DEMO_VALUES_21 ON STUDENT_DEMO_VALUES (DEMOID,STUDENT_BIO_ID);
CREATE INDEX IDX_OBJECTIVE_SCORE_FACT_21 ON OBJECTIVE_SCORE_FACT (STUDENT_BIO_ID,SUBTESTID,OBJECTIVEID);

---input controls
CREATE INDEX IDX_ORG_PRODUCT_LINK ON ORG_PRODUCT_LINK (CUST_PROD_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_PRODUCT_LINK_2 ON ORG_PRODUCT_LINK (ORG_NODEID, CUST_PROD_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_NODE_DIM_1 ON ORG_NODE_DIM (ORG_NODE_CODE) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_NODE_DIM_2 ON ORG_NODE_DIM (PARENT_ORG_NODEID, ORG_NODEID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_NODE_DIM_3 ON ORG_NODE_DIM (ORG_NODEID,CUSTOMERID);
CREATE INDEX IDX_ORG_NODE_DIM_4 ON ORG_NODE_DIM (CUSTOMERID,ORG_NODEID,ORG_MODE,ORG_NODE_LEVEL);
CREATE INDEX IDX_GRADE_ORG_MODE_MAP_1 ON GRADE_ORG_MODE_MAP(ORG_NODEID,ASSESSMENTID,GRADEID,ORG_MODE);

----Report INDEXES
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_1 ON MV_RPRT_STUD_DETAILS(CUST_PROD_ID);
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_2 ON MV_RPRT_STUD_DETAILS(SCHOOL_ORG_NODEID);
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_3 ON MV_RPRT_STUD_DETAILS(GRADEID);
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_4 ON MV_RPRT_STUD_DETAILS(SUBTESTID);
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_5 ON MV_RPRT_STUD_DETAILS(DISTRICT_ORG_NODEID);
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_6 ON MV_RPRT_STUD_DETAILS(DISTRICT_MODE);
CREATE BITMAP INDEX BIDX_MV_RPRT_STUD_DETAILS_7 ON MV_RPRT_STUD_DETAILS(SCHOOL_MODE);

----Java Modules
CREATE UNIQUE INDEX IDX_USERNAME_1 ON USERS (UPPER(USERNAME)) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_USERS_1 ON ORG_USERS (USERID, ORG_NODE_LEVEL) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORG_USERS_2 ON ORG_USERS (ORG_NODEID, ORG_USER_ID) TABLESPACE FCAT_IDX1;
CREATE INDEX IDX_ORGUSER_DETAILS_1 ON ORGUSER_DETAILS (USERID, ORG_NODEID, CUSTOMERID, ADMINID);
CREATE UNIQUE INDEX IDX_USERNAME_1 ON USERS (UPPER(USERNAME)) TABLESPACE USERS;