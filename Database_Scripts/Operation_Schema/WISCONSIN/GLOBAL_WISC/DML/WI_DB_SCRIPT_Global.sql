--ETL_JOBMASTER_CONFIG:PRISMGLOBAL
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_WI_GRT_DATA_LOAD','S_STG_WI_GRT_DATA_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_WI_GRT_DATA_VALIDATE','S_STG_WI_GRT_DATA_VALIDATE','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_ORG_NODE_DIM_LOAD','S_STG_ORG_NODE_DIM_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_ORG_NODE_DIM_BKP_LOAD','S_ORG_NODE_DIM_BKP_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_ORG_NODE_LOAD','S_OPR_ORG_NODE_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_ORG_PRODUCT_LINK_LOAD','S_OPR_ORG_PRODUCT_LINK_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_ORG_LSTNODE_LINK_LOAD','S_STG_ORG_LSTNODE_LINK_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_ORG_LSTNODE_LINK_LOAD','S_OPR_ORG_LSTNODE_LINK_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_CHECK_PRODUCT','S_CHECK_PRODUCT','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_ERROR_PRODUCT_EMAIL','S_ERROR_PRODUCT_EMAIL','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_STG_ORG_LOAD','S_PROCESS_STATUS_STG_ORG_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_STD_BIO_DETAILS_LOAD','S_STG_STD_BIO_DETAILS_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_STG_BIO_LOAD','S_PROCESS_STATUS_STG_BIO_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_STD_DEMO_DETAILS_LOAD','S_STG_STD_DEMO_DETAILS_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_STG_DEMO_LOAD','S_PROCESS_STATUS_STG_DEMO_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_STD_SUBTEST_DETAILS_LOAD','S_STG_STD_SUBTEST_DETAILS_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_STG_SUBTEST_LOAD','S_PROCESS_STATUS_STG_SUBTEST_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_STD_OBJECTIVE_DETAILS_LOAD','S_STG_STD_OBJECTIVE_DETAILS_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_STG_OBJECTIVE_LOAD','S_PROCESS_STATUS_STG_OBJECTIVE_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_STG_ITEM_RESPONSE_DETAILS_LOAD','S_STG_ITEM_RESPONSE_DETAILS_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_STG_ITEM_LOAD','S_PROCESS_STATUS_STG_ITEM_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_STUDENT_BIO_DIM_LOAD','S_OPR_STUDENT_BIO_DIM_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_BIO_LOAD','S_PROCESS_STATUS_OPR_BIO_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_STUDENT_DEMO_VALUES_LOAD','S_OPR_STUDENT_DEMO_VALUES_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_STUDENT_DEMO_LOAD','S_PROCESS_STATUS_OPR_STUDENT_DEMO_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_STU_SUBTEST_DEMO_VALUES_LOAD','S_OPR_STU_SUBTEST_DEMO_VALUES_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_SUBTEST_DEMO_LOAD','S_PROCESS_STATUS_OPR_SUBTEST_DEMO_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_SUBTEST_SCORE_FACT_LOAD','S_OPR_SUBTEST_SCORE_FACT_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_SUBTEST_FACT_LOAD','S_PROCESS_STATUS_OPR_SUBTEST_FACT_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_OBJECTIVE_SCORE_FACT_LOAD','S_OPR_OBJECTIVE_SCORE_FACT_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_OBJECTIVE_FACT_LOAD','S_PROCESS_STATUS_OPR_OBJECTIVE_FACT_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_OPR_ITEM_SCORE_FACT_LOAD','S_OPR_ITEM_SCORE_FACT_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_ITEM_FACT_LOAD','S_PROCESS_STATUS_OPR_ITEM_FACT_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);
INSERT INTO ETL_JOBMASTER_CONFIG(MAPPING_NAME,SESSION_NAME,WORKFLOW_NAME,REPOSITORY_SERVICE,INTEGRATION_SERVICE,DOMAIN_NAME,PROJECTID,CREATED_DATE_TIME,UPDATED_DATE_TIME) VALUES ('M_PROCESS_STATUS_OPR_ORG_LOAD','S_PROCESS_STATUS_OPR_ORG_LOAD','WKF_WISCONSIN_PRISM_GRT_LOAD',NULL,NULL,NULL,5,SYSDATE,SYSDATE);

alter table ITEMSET_DIM
  DROP constraint CHK_ITEM_TYPE;

alter table ITEMSET_DIM
  ADD constraint CHK_ITEM_TYPE
  check (ITEM_TYPE IN ('SR', 'CR', 'GR','OBJ','TE','MC1','AS1','TD1','MC2','AS2','TD2'));

DELETE FROM ITEMSET_DIM WHERE PROJECTID = 5; 

insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- ELA', '1001', null, 1, null, 'MC1', null, null, 2066, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Autoscored- ELA', '1002', null, 2, null, 'AS1', null, null, 2066, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Text-Dependent Analysis - ELA', '1003', null, 3, null, 'TD1', null, null, 2066, null, null, null, 5, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- Math', '1004', null, 1, null, 'MC1', null, null, 2067, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Autoscored- Math', '1005', null, 2, null, 'AS1', null, null, 2067, null, null, null, 5, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- Science', '1006', null, 1, null, 'MC1', null, null, 2068, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Autoscored- Science', '1007', null, 2, null, 'AS1', null, null, 2068, null, null, null, 5, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- Social Study', '1008', null, 1, null, 'MC1', null, null, 2069, null, null, null, 5, SYSDATE);
---
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC- ELA', '1009', null, 4, null, 'MC2', null, null, 2066, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Autoscored- ELA', '1010', null, 5, null, 'AS2', null, null, 2066, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Text-Dependent Analysis - ELA', '1011', null, 6, null, 'TD2', null, null, 2066, null, null, null, 5, SYSDATE);

---
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC - Math', '1012', null, 3, null, 'MC2', null, null, 2067, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Autoscored - Math', '1013', null, 4, null, 'AS2', null, null, 2067, null, null, null, 5, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC - Science', '1014', null, 3, null, 'MC2', null, null, 2068, null, null, null, 5, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Autoscored - Science', '1015', null, 4, null, 'AS2', null, null, 2068, null, null, null, 5, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC - Social Study', '1016', null, 2, null, 'MC2', null, null, 2069, null, null, null, 5, SYSDATE);
commit;