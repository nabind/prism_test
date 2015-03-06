-- Create table
create table GLOBAL_SEQ_BUMP
(
  SEQUENCE_NAME  VARCHAR2(30) not null,
  TABLE_NAME     VARCHAR2(30),
  LAST_NUMBER    NUMBER not null,
  INCREMENT_BY   NUMBER not null,
  SEQUENCE_OWNER VARCHAR2(100)
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

