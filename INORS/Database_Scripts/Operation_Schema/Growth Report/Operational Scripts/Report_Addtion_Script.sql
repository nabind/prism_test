-- insert into DASH_REPORTS
insert into DASH_REPORTS (DB_REPORTID, REPORT_NAME, REPORT_DESC, REPORT_TYPE, REPORT_FOLDER_URI, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1363, 'Group Performance Matrix Report', 'Group Performance Matrix Report', 'API', '/public/INORS/Report/IStep_Growth_Matrix_1_files', 'AC', to_date('18-06-2014 09:25:56', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-06-2014 08:54:42', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_REPORTS (DB_REPORTID, REPORT_NAME, REPORT_DESC, REPORT_TYPE, REPORT_FOLDER_URI, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1382, 'Student Tabular Report', 'Student Tabular Report', 'API_TABLE', '/public/INORS/Report/Student_Tabular_Report_files', 'AC', to_date('26-06-2014 10:06:23', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

commit;

-- insert into DASH_MENU_RPT_ACCESS
------Group Performance Matrix Report
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

-----Student Tabular Report
insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 1, 1, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 1, 2, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 1, 3, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 7, 1, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 7, 2, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 7, 3, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 8, 1, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 8, 2, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));

insert into DASH_MENU_RPT_ACCESS (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL, CUST_PROD_ID, REPORT_SEQ, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 1382, 8, 3, 5001, 1382, 'AC', to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2014 01:52:17', 'dd-mm-yyyy hh24:mi:ss'));
COMMIT;