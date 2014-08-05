create table STG_USC_LINK
(
  ORG_USER_ID    NUMBER,
  CUST_PROD_ID   NUMBER,
  STUDENT_BIO_ID NUMBER not null,
  SUBTESTID      NUMBER,
  DATETIMESTAMP  DATE
);
------------------
alter table users add (USER_TYPE varchar2(10) default 'FE', activation_status1 varchar2(2),
created_date_time1  date default sysdate NOT NULL  ,updated_date_time1  date );

update users
set activation_status1=activation_status
,created_date_time1 =created_date_time
,updated_date_time1=updated_date_time;

alter table users modify (activation_status1 NOT NULL);

alter table users drop(activation_status,created_date_time,updated_date_time);

alter table users rename column activation_status1 to activation_status;
alter table users rename column created_date_time1 to created_date_time;
alter table users rename column updated_date_time1 to updated_date_time;

alter table USERS
  add constraint CHK_USERS_1
  check (USER_TYPE IN ('GRW','GRW_P', 'FE','PU','SSO','TU'));
----------------
create or replace function SF_USER_ID_GEN
  return number as
  LV_USER_ID varchar2(4000);
begin
  LV_USER_ID:=USER_ID_SEQ.NEXTVAL;
  return LV_USER_ID;
end SF_USER_ID_GEN;
----------------
create or replace function SF_ORG_USER_ID_GEN(IN_EXISTS NUMBER,IN_USERID NUMBER)
  return number as
  LV_ORG_USER_ID varchar2(4000);
begin

  IF IN_EXISTS = 1
  THEN
  
    SELECT ORG_USER_ID INTO LV_ORG_USER_ID FROM ORG_USERS OU
    WHERE OU.USERID = IN_USERID;
    
    return LV_ORG_USER_ID;
  
  ELSE
  
    LV_ORG_USER_ID:=ORG_USER_ID_SEQ.NEXTVAL;
    
    return LV_ORG_USER_ID;
  
  END IF;

  
end SF_ORG_USER_ID_GEN;
---------------

CREATE OR REPLACE PROCEDURE PRC_REFRESH_PRINCIPAL_USER
AS
begin

 DBMS_MVIEW.REFRESH('MV_PRINCIPAL_USER_SEL_LOOKUP', 'C');
 DBMS_MVIEW.REFRESH('MV_LVL2_PRCPL_USER_SEL_LOOKUP', 'C');

end PRC_REFRESH_PRINCIPAL_USER;
-------------
create index IDX_RESULTS_GRT_FACT_4 on RESULTS_GRT_FACT(CUST_PROD_ID,STUDENT_TEST_AI); 