create index IDX_ESRIH_1 on ERR_STUDENT_REG_INFO_HIST (STG_REG_INFO_ID)
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

create index IDX_ESDIH_1 on ERR_STUDENT_DOC_INFO_HIST (STG_STUDENT_DOCID,STG_STUDENT_BIO_ID)
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
  
create index IDX_ESBDH_1 on ERR_STUDENT_BIO_DIM_HIST (STG_STUDENT_BIO_ID)
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
