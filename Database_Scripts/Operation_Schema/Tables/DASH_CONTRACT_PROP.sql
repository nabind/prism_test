-- Create table
create table DASH_CONTRACT_PROP
(
  DB_PROPERTYID    NUMBER not null,
  DB_PROPERTY_NAME VARCHAR2(50),
  DB_PROPERY_VALUE VARCHAR2(100),
  SSO_SOURCE       VARCHAR2(100)
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
alter table DASH_CONTRACT_PROP
  add constraint PK_DASH_CONTRACT_PROP primary key (DB_PROPERTYID)
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
