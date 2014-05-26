-- Create/Recreate indexes 
create index RESCORE_REQUEST_FORM_INDX1 on RESCORE_REQUEST_FORM (STUDENT_BIO_ID, CUST_PROD_ID, ORG_NODEID, ITEMSETID, GRADEID, SUBTESTID, OBJECTIVEID)
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