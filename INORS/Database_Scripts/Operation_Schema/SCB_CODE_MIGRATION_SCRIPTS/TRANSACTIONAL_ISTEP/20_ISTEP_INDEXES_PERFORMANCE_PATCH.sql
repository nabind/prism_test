--2/26 Indexes (Index names containing _TM were created by Travis)
DROP INDEX IDX_ORG_PRODUCT_LINK_TM2;
CREATE INDEX IDX_ORG_PRODUCT_LINK_TM2 ON ORG_PRODUCT_LINK
(CUST_PROD_ID, ORG_NODEID )
LOGGING
TABLESPACE FCAT_IDX1
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

DROP INDEX IDX_SUBTEST_SCORE_FACT_3_TM;
CREATE INDEX IDX_SUBTEST_SCORE_FACT_3_TM ON SUBTEST_SCORE_FACT
(ORG_NODEID, CUST_PROD_ID, SUBTESTID, ADMINID, GRADEID, SS, HSE, STATUS_CODE, STUDENT_BIO_ID, GENDERID, FORMID, ASSESSMENTID)
LOGGING
TABLESPACE FCAT_IDX1
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
PARALLEL 8;
ALTER INDEX IDX_SUBTEST_SCORE_FACT_3_TM PARALLEL 1;

DROP INDEX BIDX_SUBTEST_SCORE_FACT_2_TM;
CREATE INDEX BIDX_SUBTEST_SCORE_FACT_2_TM ON SUBTEST_SCORE_FACT
(STUDENT_BIO_ID, ORG_NODEID, SUBTESTID, GRADEID, ADMINID, GENDERID)
  TABLESPACE FCAT_IDX1
  PCTFREE    10
  INITRANS   2
  MAXTRANS   255
  STORAGE    (
              BUFFER_POOL      DEFAULT
              FLASH_CACHE      DEFAULT
              CELL_FLASH_CACHE DEFAULT
             )
PARALLEL 8;
ALTER INDEX BIDX_SUBTEST_SCORE_FACT_2_TM PARALLEL 1;

DROP  INDEX DEMO_CODE_UK_TM;
CREATE  INDEX DEMO_CODE_UK_TM ON DEMOGRAPHIC
(DEMO_CODE, CUSTOMERID, DEMOID)
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


DROP  INDEX DEMOGRAPHIC_VALUES_IDXTM1;
CREATE  INDEX DEMOGRAPHIC_VALUES_IDXTM1 ON DEMOGRAPHIC_VALUES
(DEMO_VALID, DEMOID, DEMO_VALUE_NAME)
LOGGING
TABLESPACE FCAT_IDX1
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

DROP INDEX ORG_LSTNODE_LINK_TM1;
CREATE INDEX ORG_LSTNODE_LINK_TM1 ON ORG_LSTNODE_LINK
(ORG_NODEID, ORG_LSTNODEID, ADMINID)
LOGGING
TABLESPACE FCAT_IDX1
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

DROP INDEX IDX_USERNAME_1_TM;
CREATE UNIQUE INDEX IDX_USERNAME_1_TM ON USERS
(USERNAME)
LOGGING
TABLESPACE FCAT_IDX1
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

-- The following indexes where created on the morning of 2/24
-- prior to my run request.
DROP INDEX VW_SUBTEST_GRADE_OBJ_MAP_IDX1;
CREATE INDEX VW_SUBTST_GRADE_OBJ_MAP_IDXTM1 ON VW_SUBTEST_GRADE_OBJECTIVE_MAP
(SUBTESTID, SUBTEST_NAME)
LOGGING
TABLESPACE FCAT_IDX1
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


DROP INDEX VW_SUBTST_GRADE_OBJ_MAP_IDX2;
CREATE INDEX VW_SUBTST_GRADE_OBJ_MAP_IDXTM2 ON VW_SUBTEST_GRADE_OBJECTIVE_MAP
(OBJECTIVEID, SUBTESTID, SUBTEST_NAME, GRADEID, LEVELID)
LOGGING
TABLESPACE FCAT_IDX1
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

DROP INDEX VW_SUBTEST_GRADE_OBJ_MAP_IDX3;
CREATE INDEX VW_SUBTST_GRADE_OBJ_MAP_IDXTM3 ON VW_SUBTEST_GRADE_OBJECTIVE_MAP
(GRADEID, OBJECTIVEID, SUBTESTID, SUBTEST_NAME, LEVELID)
LOGGING
TABLESPACE FCAT_IDX1
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

--DROP INDEX IDX_ORG_NODE_DIM_4;
DROP INDEX IDX_ORG_NODE_DIM_TM4;
CREATE UNIQUE INDEX IDX_ORG_NODE_DIM_TM4 ON ORG_NODE_DIM
(ORG_NODEID, PARENT_ORG_NODEID, ORG_NODE_LEVEL)
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

DROP INDEX IDX_ORG_USERS_3;
CREATE INDEX IDX_ORG_USERS_3 ON ORG_USERS (userid)
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
create index IDX_INVITATION_CODE_5 on INVITATION_CODE (INVITATION_CODE,ACTIVATION_STATUS) 
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

DROP INDEX IDX_EDU_CENTER_DETAILS;
create index IDX_EDU_CENTER_DETAILS on EDU_CENTER_DETAILS (edu_centerid,customerid)LOGGING
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

DROP INDEX IDX_PASSWORD_HISTORY;
create index IDX_PASSWORD_HISTORY on PASSWORD_HISTORY (USERID)
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

DROP INDEX IDX_ORG_NODE_DIM_5;
create index IDX_ORG_NODE_DIM_5 on ORG_NODE_DIM (ORG_NODE_LEVEL, CUSTOMERID, PARENT_ORG_NODEID, ORG_NODEID)
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

DROP INDEX mv_ORG_TP_STRUCTURE_IDX;
CREATE INDEX mv_ORG_TP_STRUCTURE_IDX ON MV_ORG_TP_STRUCTURE(ORG_LEVEL,ORG_LABEL)
LOGGING
TABLESPACE FCAT_IDX1
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

DROP INDEX IDX_ORG_NODE_DIM_TM2;
create index IDX_ORG_NODE_DIM_TM2 on ORG_NODE_DIM (PARENT_ORG_NODEID, ORG_NODEID, ORG_NODE_NAME, ORG_MODE)
  tablespace FCAT_IDX1
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
  tablespace FCAT_IDX1
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
  tablespace FCAT_IDX1
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

DROP INDEX IDX_ORG_USERS_TM2;
create index IDX_ORG_USERS_TM2 on ORG_USERS (ORG_NODEID, ORG_USER_ID, USERID, ORG_NODE_LEVEL)
tablespace FCAT_IDX1
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

DROP INDEX STEPPERF.ORG_USERS_USERID_IDX;
create index ORG_USERS_USERID_IDX on ORG_USERS (USERID, ORG_NODEID)
  tablespace FCAT_IDX1
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

DROP INDEX PK_ORG_USERS_TM1;
create unique index PK_ORG_USERS_TM1 on ORG_USERS (ORG_USER_ID, USERID)
  tablespace FCAT_IDX1
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

DROP INDEX USERS_TM1;
CREATE INDEX USERS_TM1 ON USERS
(USERID, ACTIVATION_STATUS, USERNAME, LAST_NAME, FIRST_NAME)
LOGGING
TABLESPACE FCAT_IDX1
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

DROP INDEX IDX1_JOB_TRACKING;
create index IDX1_JOB_TRACKING on JOB_TRACKING (CREATED_DATE_TIME DESC)
  tablespace FCAT_IDX1
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
DROP INDEX IDX_JOB_TRACKING_1;
create index IDX_JOB_TRACKING_1 on JOB_TRACKING (USERID, JOB_STATUS)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

  

