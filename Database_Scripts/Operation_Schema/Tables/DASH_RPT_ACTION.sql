create table DASH_RPT_ACTION
(
DB_ACTIONID NUMBER,
ACTION_NAME varchar2(30),
ACTION_TYPE varchar2(10) ,
DESCRIPTION varchar2(100),
CREATED_DATE_TIME DATE default sysdate,
UPDATED_DATE_TIME DATE
);

ALTER TABLE DASH_RPT_ACTION add constraint PK_DASH_RPT_ACTION primary key (DB_ACTIONID);

