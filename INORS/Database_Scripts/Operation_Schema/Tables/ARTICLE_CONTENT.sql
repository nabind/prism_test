-- Create table
create table ARTICLE_CONTENT
(
  ARTICLE_CONTENT_ID NUMBER not null,
  ARTICLE_CONTENT    CLOB,
  DESCRIPTION        VARCHAR2(4000),
  OBJECTIVEID        NUMBER,
  CREATED_DATE_TIME  DATE,
  UPDATED_DATE_TIME  DATE
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
alter table ARTICLE_CONTENT
  add constraint PK_ARTICLE_CONTENT primary key (ARTICLE_CONTENT_ID)
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
alter table ARTICLE_CONTENT
  add constraint FK_ARTICLE_CONTENT_1 foreign key (OBJECTIVEID)
  references OBJECTIVE_DIM (OBJECTIVEID);
