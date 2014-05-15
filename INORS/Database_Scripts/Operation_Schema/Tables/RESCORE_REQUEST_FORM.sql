-- Create table
create table RESCORE_REQUEST_FORM
(
  RRF_ID                     NUMBER not null,
  STUDENT_BIO_ID             NUMBER,
  CUST_PROD_ID               NUMBER,
  ORG_NODEID                 NUMBER,
  ADMINID                    NUMBER,
  GRADEID                    NUMBER,
  LEVELID                    NUMBER,
  CONTENTID                  NUMBER,
  SUBTESTID                  NUMBER,
  OBJECTIVEID                NUMBER,
  ITEMSETID                  NUMBER,
  ORIGINAL_SCORE             VARCHAR2(20),
  ORIGINAL_PERFORMANCE_LEVEL VARCHAR2(20),
  ELIGIBLE_FOR_RESCORE       VARCHAR2(5),
  IS_REQUESTED               VARCHAR2(5),
  REQUESTED_DATE             DATE,
  REQUESTED_USERID           NUMBER,
  CREATED_DATE_TIME          DATE default SYSDATE,
  UPDATED_DATE_TIME          DATE
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
alter table RESCORE_REQUEST_FORM
  add constraint PK_RESCORE_REQUEST_FORM primary key (RRF_ID)
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
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_1 foreign key (STUDENT_BIO_ID)
  references STUDENT_BIO_DIM (STUDENT_BIO_ID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_10 foreign key (ITEMSETID)
  references ITEMSET_DIM (ITEMSETID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_2 foreign key (CUST_PROD_ID)
  references CUST_PRODUCT_LINK (CUST_PROD_ID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_3 foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_4 foreign key (ADMINID)
  references ADMIN_DIM (ADMINID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_5 foreign key (GRADEID)
  references GRADE_DIM (GRADEID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_6 foreign key (LEVELID)
  references LEVEL_DIM (LEVELID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_7 foreign key (CONTENTID)
  references CONTENT_DIM (CONTENTID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_8 foreign key (SUBTESTID)
  references SUBTEST_DIM (SUBTESTID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_9 foreign key (OBJECTIVEID)
  references OBJECTIVE_DIM (OBJECTIVEID);
alter table RESCORE_REQUEST_FORM
  add constraint FK_RESCORE_REQUEST_FORM_11 foreign key (REQUESTED_USERID)
  references USERS (USERID);  
