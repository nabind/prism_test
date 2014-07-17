create index IDX_ORG_USER_2 on ORG_USERS (USERID, ORG_NODEID, ORG_NODE_LEVEL)
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