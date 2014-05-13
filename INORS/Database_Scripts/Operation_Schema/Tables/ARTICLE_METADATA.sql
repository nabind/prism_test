-- drop table
drop table ARTICLE_METADATA;

-- Create table
create table ARTICLE_METADATA
(
  ARTICLEID          NUMBER not null,
  ARTICLE_NAME       VARCHAR2(255),
  CUST_PROD_ID       NUMBER not null,
  SUBT_OBJ_MAPID     NUMBER,
  SUBTESTID          NUMBER,
  ARTICLE_CONTENT_ID NUMBER,
  CATEGORY           VARCHAR2(100),
  CATEGORY_TYPE      VARCHAR2(20),
  CATEGORY_SEQ       NUMBER,
  SUB_HEADER         VARCHAR2(255),
  DESCRIPTION        VARCHAR2(4000),
  GRADEID            NUMBER,
  PROFICIENCY_LEVEL  VARCHAR2(100),
  RESOLVED_RPRT_STATUS VARCHAR2(10),
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
alter table ARTICLE_METADATA
  add constraint PK_ARTICLE_METADATA primary key (ARTICLEID)
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
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_1 foreign key (CUST_PROD_ID)
  references CUST_PRODUCT_LINK (CUST_PROD_ID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_2 foreign key (GRADEID)
  references GRADE_DIM (GRADEID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_3 foreign key (SUBTESTID)
  references SUBTEST_DIM (SUBTESTID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_4 foreign key (SUBT_OBJ_MAPID)
  references SUBTEST_OBJECTIVE_MAP (SUBT_OBJ_MAPID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_5 foreign key (ARTICLE_CONTENT_ID)
  references ARTICLE_CONTENT (ARTICLE_CONTENT_ID);
