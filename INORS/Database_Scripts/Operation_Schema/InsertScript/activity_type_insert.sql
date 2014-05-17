prompt PL/SQL Developer import file
prompt Created on Saturday, May 17, 2014 by 233208
set feedback off
set define off
prompt Disabling triggers for ACTIVITY_TYPE...
alter table ACTIVITY_TYPE disable all triggers;
prompt Deleting ACTIVITY_TYPE...
delete from ACTIVITY_TYPE;
commit;
prompt Loading ACTIVITY_TYPE...
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1, 'Login', 'Login', 'Login Activity Tracking', to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'));
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (2, 'Reports', 'Report', 'Report URL Tracking', to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'));
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (3, 'Admin', 'Admin', 'User Admin URL Tracking', to_date('10-04-2014 08:15:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-04-2014 08:15:17', 'dd-mm-yyyy hh24:mi:ss'));
commit;
prompt 3 records loaded
prompt Enabling triggers for ACTIVITY_TYPE...
alter table ACTIVITY_TYPE enable all triggers;
set feedback on
set define on
prompt Done.
