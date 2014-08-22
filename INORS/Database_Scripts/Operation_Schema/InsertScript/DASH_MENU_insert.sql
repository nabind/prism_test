prompt PL/SQL Developer import file
prompt Created on Friday, August 22, 2014 by 233208
set feedback off
set define off
prompt Disabling triggers for DASH_MENUS...
alter table DASH_MENUS disable all triggers;
prompt Deleting DASH_MENUS...
delete from DASH_MENUS;
commit;
prompt Loading DASH_MENUS...
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 'Reports', 'CUSTOM', 1, 'Reports', to_date('18-10-2013', 'dd-mm-yyyy'), to_date('18-10-2013', 'dd-mm-yyyy'));
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (103, 'Resources', 'CUSTOM', 4, 'Resources', to_date('30-10-2013 17:28:30', 'dd-mm-yyyy hh24:mi:ss'), null);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (102, 'Downloads', 'CUSTOM', 3, 'Downloads', to_date('18-10-2013', 'dd-mm-yyyy'), to_date('18-10-2013', 'dd-mm-yyyy'));
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (104, 'Useful Links', 'CUSTOM', 5, 'Useful Links', to_date('30-10-2013', 'dd-mm-yyyy'), null);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (105, 'Manage', 'CUSTOM', 2, 'Manage', to_date('20-08-2014 03:56:01', 'dd-mm-yyyy hh24:mi:ss'), null);
commit;
prompt 5 records loaded
prompt Enabling triggers for DASH_MENUS...
alter table DASH_MENUS enable all triggers;
set feedback on
set define on
prompt Done.
