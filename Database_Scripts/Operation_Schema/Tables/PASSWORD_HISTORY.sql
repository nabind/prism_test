-- Create table
create table PASSWORD_HISTORY
(
  PWD_HISTORYID     NUMBER not null,
  USERID            NUMBER not null,
  PASSWORD          VARCHAR2(100) not null,
  CREATED_DATE_TIME DATE default sysdate not null,
  UPDATED_DATE_TIME DATE
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
-- Create/Recreate primary, unique and foreign key constraints 
alter table PASSWORD_HISTORY
  add constraint PK_PASSWORD_HISTORY primary key (PWD_HISTORYID)
  using index 
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
