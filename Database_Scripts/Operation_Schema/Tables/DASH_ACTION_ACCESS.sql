create table DASH_ACTION_ACCESS
(
DB_ACT_ACCESSID NUMBER,
DB_MENUID NUMBER not null,
DB_REPORTID NUMBER not null,
DB_ACTIONID NUMBER not null,
ROLEID NUMBER not null,
ORG_LEVEL NUMBER not null,
CUST_PROD_ID      NUMBER not null,
ACTION_SEQ        NUMBER not null,
ACTIVATION_STATUS VARCHAR2(2) not null,
CREATED_DATE_TIME DATE default sysdate,
UPDATED_DATE_TIME DATE
);

ALTER TABLE DASH_ACTION_ACCESS add constraint PK_DASH_ACTION_ACCESS
primary key (DB_ACT_ACCESSID,DB_MENUID,DB_REPORTID,DB_ACTIONID,ROLEID,ORG_LEVEL);

alter table DASH_ACTION_ACCESS
  add constraint FK_DASH_ACTION_ACCESS_1 foreign key (DB_REPORTID)
  references DASH_REPORTS (DB_REPORTID);
alter table DASH_ACTION_ACCESS
  add constraint FK_DASH_ACTION_ACCESS_2 foreign key (ROLEID)
  references ROLE (ROLEID);
alter table DASH_ACTION_ACCESS
  add constraint FK_DASH_ACTION_ACCESS_3 foreign key (CUST_PROD_ID)
  references CUST_PRODUCT_LINK (CUST_PROD_ID);
alter table DASH_ACTION_ACCESS
  add constraint FK_DASH_ACTION_ACCESS_4 foreign key (DB_MENUID)
  references DASH_MENUS (DB_MENUID);
alter table DASH_ACTION_ACCESS
  add constraint FK_DASH_ACTION_ACCESS_5 foreign key (DB_ACTIONID)
  references DASH_RPT_ACTION (DB_ACTIONID);  