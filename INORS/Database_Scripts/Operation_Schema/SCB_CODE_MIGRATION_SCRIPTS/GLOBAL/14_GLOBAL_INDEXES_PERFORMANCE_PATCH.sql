--(Index names containing _TM were created by Travis)
DROP INDEX DASHBOARD_MENU_RPT_ACCESS_TM1;
CREATE  INDEX DASHBOARD_MENU_RPT_ACCESS_TM1 ON DASH_MENU_RPT_ACCESS
(DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID)
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

DROP INDEX DASHBOARD_MENU_RPT_ACCESS_TM2;
CREATE  INDEX DASHBOARD_MENU_RPT_ACCESS_TM2 ON DASH_MENU_RPT_ACCESS
( CUST_PROD_ID, ROLEID, ORG_LEVEL)
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

--DROP  INDEX PRODUCT_IDX1;
DROP  INDEX PRODUCT_IDXTM1;
CREATE  INDEX PRODUCT_IDXTM1 ON PRODUCT
(PRODUCTID, PROJECTID, PRODUCT_CODE, PRODUCT_NAME)
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


--DROP INDEX CUST_PRODUCT_LI_IDX3;
DROP INDEX CUST_PRODUCT_LI_IDXTM3;
CREATE INDEX CUST_PRODUCT_LI_IDXTM3 ON CUST_PRODUCT_LINK
(CUSTOMERID, PRODUCTID, CUST_PROD_ID, ADMINID )
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

DROP INDEX IDX1_ADMIN_DIM;
create index IDX1_ADMIN_DIM ON  ADMIN_DIM("PROJECTID","ADMINID")
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

DROP INDEX IDX1_CUSTOMER_INFO;
create index IDX1_CUSTOMER_INFO ON  CUSTOMER_INFO("PROJECTID","CUSTOMERID")
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

DROP INDEX IDX_TEST_PROGRAM_3;
create index IDX_TEST_PROGRAM_3 on TEST_PROGRAM ("ADMINID", "CUSTOMERID")
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

DROP INDEX IDX_ROLE_1;
create index IDX_ROLE_1 on ROLE (ROLEID, ROLE_NAME)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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

DROP INDEX IDX1_GRADE_DIM;
CREATE INDEX IDX1_GRADE_DIM ON GRADE_DIM(GRADEID,GRADE_NAME)
tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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


DROP INDEX IDX_DASH_CONTRACT_PROP;
CREATE INDEX IDX_DASH_CONTRACT_PROP ON DASH_CONTRACT_PROP(DB_PROPERTY_NAME,DB_PROPERY_VALUE)
tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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

DROP INDEX IDX_PRODUCT_DIM_1;
CREATE INDEX IDX_PRODUCT_DIM_1 on PRODUCT (PROJECTID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    INITIAL          64K
    NEXT             1M
    MAXSIZE          UNLIMITED
    MINEXTENTS       1
    MAXEXTENTS       UNLIMITED
    PCTINCREASE      0
    BUFFER_POOL      DEFAULT
    FLASH_CACHE      DEFAULT
    CELL_FLASH_CACHE DEFAULT
  )NOPARALLEL;

DROP INDEX CUST_PRODUCT_LI_IDX1_BI;
CREATE INDEX CUST_PRODUCT_LI_IDX1_BI on CUST_PRODUCT_LINK (PRODUCTID, CUSTOMERID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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


DROP INDEX IDX_PWD_HINT_QUESTIONS;
create index IDX_PWD_HINT_QUESTIONS on PWD_HINT_QUESTIONS (QUESTION_SEQ)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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
  
  
DROP INDEX CUST_PRODUCT_LI_IDXTM2;  
create index CUST_PRODUCT_LI_IDXTM2 on CUST_PRODUCT_LINK (PRODUCTID, CUST_PROD_ID, ADMINID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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

DROP INDEX IDX_PRODUCT_DIM_TM2;
create index IDX_PRODUCT_DIM_TM2 on PRODUCT (PROJECTID, PRODUCTID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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


DROP INDEX DASH_MESSAGE_TYPE_UNIQUE_TM1;
create unique index DASH_MESSAGE_TYPE_UNIQUE_TM1 on DASH_MESSAGE_TYPE (MESSAGE_TYPE, MESSAGE_NAME, CUST_PROD_ID, MSG_TYPEID)
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
  
DROP INDEX INDX_DASH_REPORTS_TM1;  
create index INDX_DASH_REPORTS_TM1 on DASH_REPORTS (PROJECTID, DB_REPORTID, REPORT_NAME)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
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

----MISSING INDEXES FROM PERF 03/27/2015
CREATE UNIQUE INDEX PK_FORM_DIM ON FORM_DIM 
(FORMID) 
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
           ) 
NOPARALLEL; 
  
CREATE UNIQUE INDEX PK_GENDER_DIM ON GENDER_DIM 
(GENDERID) 
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
           ) 
NOPARALLEL; 
  
CREATE UNIQUE INDEX PK_LEVEL_MAP ON LEVEL_MAP 
(LEVEL_MAPID) 
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
           ) 
NOPARALLEL; 
  
CREATE UNIQUE INDEX PK_SUBJECT_DIM ON SUBTEST_DIM 
(SUBTESTID) 
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
           ) 
NOPARALLEL; 


  
