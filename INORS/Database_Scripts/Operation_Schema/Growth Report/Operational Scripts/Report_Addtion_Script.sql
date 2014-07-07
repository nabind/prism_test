-- insert into DASH_REPORTS
insert into DASH_REPORTS (DB_REPORTID, REPORT_NAME, REPORT_DESC, REPORT_TYPE, REPORT_FOLDER_URI, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1363, 'Group Performance Matrix Report', 'Group Performance Matrix Report', 'API', '/public/INORS/Report/IStep_Growth_Matrix_1_files', 'AC', to_date('18-06-2014 09:25:56', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:42', 'dd-mm-yyyy hh24:mi:ss'));
commit;

-- insert into DASH_MENU_RPT_ACCESS
insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 1, 1, 5017, 1363, 'AC', to_date('30-06-2014 08:54:43', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:43', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 1, 2, 5017, 1363, 'AC', to_date('30-06-2014 08:54:43', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:43', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 1, 3, 5017, 1363, 'AC', to_date('30-06-2014 08:54:43', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:43', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 7, 1, 5017, 1363, 'AC', to_date('30-06-2014 08:54:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:44', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 7, 2, 5017, 1363, 'AC', to_date('30-06-2014 08:54:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:44', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 7, 3, 5017, 1363, 'AC', to_date('30-06-2014 08:54:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:44', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 8, 1, 5017, 1363, 'AC', to_date('30-06-2014 08:54:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:45', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 8, 2, 5017, 1363, 'AC', to_date('30-06-2014 08:54:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:45', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1363, 8, 3, 5017, 1363, 'AC', to_date('30-06-2014 08:54:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:45', 'dd-mm-yyyy hh24:mi:ss'));
commit;