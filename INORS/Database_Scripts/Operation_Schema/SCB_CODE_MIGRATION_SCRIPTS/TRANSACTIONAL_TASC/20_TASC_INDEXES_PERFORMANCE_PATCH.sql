--(Index names containing _TM were created by Travis)
DROP INDEX MV_STUDENT_DETAILS_INDX_TM;
CREATE INDEX MV_STUDENT_DETAILS_INDX_TM ON MV_STUDENT_DETAILS
(ORG_NODEID, ADMINID, CUSTOMERID, GRADEID, SUBTESTID, STUDENT_BIO_ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX MV_STUDENT_DETAILS_INDX_TM2;
CREATE INDEX MV_STUDENT_DETAILS_INDX_TM2 ON MV_STUDENT_DETAILS
(STUDENT_BIO_ID, DATE_TEST_TAKEN)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_INVITATION_CODE_5;
create index IDX_INVITATION_CODE_5 on INVITATION_CODE (INVITATION_CODE,ACTIVATION_STATUS) LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_EDU_CENTER_DETAILS;
create index IDX_EDU_CENTER_DETAILS on EDU_CENTER_DETAILS (edu_centerid,customerid) LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;            

DROP INDEX IDX_ORG_PRODUCT_LINK_2;
create index IDX_ORG_PRODUCT_LINK_2 on ORG_PRODUCT_LINK (ORG_NODEID, CUST_PROD_ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;     

DROP INDEX IDX_ORG_PRODUCT_LINK;       
create index IDX_ORG_PRODUCT_LINK on ORG_PRODUCT_LINK (CUST_PROD_ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;            

DROP INDEX SUBTEST_SCORE_FACT_INDX2;
create index SUBTEST_SCORE_FACT_INDX2 on SUBTEST_SCORE_FACT (SUBTESTID, TEST_DATE, STUDENT_BIO_ID, CUST_PROD_ID, ASSESSMENTID, GENDERID, GRADEID, FORMID, ADMINID)
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

 DROP INDEX  SUBTEST_SCORE_FACT_INDX5;
create index SUBTEST_SCORE_FACT_INDX5 on SUBTEST_SCORE_FACT (SS, HSE, STATUS_CODE, SUBTESTID, GRADEID, FORMID, STUDENT_BIO_ID)
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;
  
DROP INDEX   IDX_MV_CUST_PRODUCT_LINK_1 ;
create index IDX_MV_CUST_PRODUCT_LINK_1 on CUST_PRODUCT_LINK (CUSTOMERID, CUST_PROD_ID, ADMINID)
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX MV_STUDENT_FILE_DOWNLOAD_INDX;
create index MV_STUDENT_FILE_DOWNLOAD_INDX on MV_STUDENT_FILE_DOWNLOAD (GRADEID, TEST_LANGUAGE_VAL, RESOLVED_ETHNICITY_VAL, LOCAL_USE_1_VAL, LOCAL_USE_2_VAL, LOCAL_USE_3_VAL, LOCAL_USE_4_VAL, LOCAL_USE_5_VAL, EDUCATION_CENTER_CODE, FORM_RSTR, ROSTER_ID, ORG_NODEID)
LOGGING
tablespace USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_PASSWORD_HISTORY;
create index IDX_PASSWORD_HISTORY on PASSWORD_HISTORY (USERID)
  tablespace USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_MV_SUBTST_SCORE_TYPE_MAP_1;
create index IDX_MV_SUBTST_SCORE_TYPE_MAP_1 on MV_SUBTEST_SCORE_TYPE_MAP (CUSTOMERID, SUBTESTID)
tablespace USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX mv_ORG_TP_STRUCTURE_IDX;
CREATE INDEX mv_ORG_TP_STRUCTURE_IDX ON MV_ORG_TP_STRUCTURE(ORG_LEVEL,ORG_LABEL)
tablespace USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_ORG_NODE_DIM_2;
create index IDX_ORG_NODE_DIM_2 on ORG_NODE_DIM (PARENT_ORG_NODEID, ORG_NODEID)
  tablespace USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_ORG_NODE_DIM_TM4;
create unique index IDX_ORG_NODE_DIM_TM4 on ORG_NODE_DIM (ORG_NODEID, PARENT_ORG_NODEID, ORG_NODE_LEVEL)
  tablespace USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;
  
DROP INDEX   IDX_ORG_USERS;
  create index IDX_ORG_USERS on ORG_USERS (USERID)
  tablespace USERS
  PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_ORG_USERS_1;
create index IDX_ORG_USERS_1 on ORG_USERS (USERID, ORG_NODE_LEVEL)
  tablespace USERS
  PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;

DROP INDEX IDX_ORG_USERS_2;
create index IDX_ORG_USERS_2 on ORG_USERS (ORG_NODEID, ORG_USER_ID)
  tablespace USERS
  PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
NOPARALLEL;


  
  
  