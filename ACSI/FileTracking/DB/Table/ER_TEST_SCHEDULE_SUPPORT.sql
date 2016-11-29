-- Create table
create table ER_TEST_SCHEDULE_SUPPORT
(
  ER_TEST_SCHEDID           NUMBER not null,
  ER_STUDID                 NUMBER,
  SCHEDULE_ID               NUMBER,
  DATE_SCHEDULED            DATE,
  TIMEOFDAY                 VARCHAR2(30),
  DATECHECKEDIN             DATE,
  CONTENT_AREA_CODE         NUMBER,
  CONTENT_TEST_CODE         NUMBER,
  CONTENT_TEST_TYPE         VARCHAR2(3),
  PP_OAS_LINKEDID           NUMBER,
  BARCODE                   VARCHAR2(26),
  FORM                      VARCHAR2(4),
  TASCREADINESS             VARCHAR2(2),
  ECC                       VARCHAR2(16),
  TESTCENTERCODE            VARCHAR2(16),
  TESTCENTERNAME            VARCHAR2(100),
  CREATED_DATE_TIME         DATE,
  UPDATED_DATE_TIME         DATE,
  SCHED_TC_COUNTYPARISHCODE VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate indexes 
create unique index IDX_ER_TEST_SCHEDULE_SUPPORT1 on ER_TEST_SCHEDULE_SUPPORT (ER_STUDID, CONTENT_AREA_CODE, CONTENT_TEST_CODE)
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