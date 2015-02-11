@\dash_message_type_disable_constraint.sql;
@\run_prc_msg_typeid_correction.sql;
@\menu_message_type_insert.sql;
@\dash_message_type_insert_inors.sql;
@\dash_message_type_insert_tasc.sql;
@\dash_messages_insert.sql;
@\dash_message_type_enable_constraint.sql -- delete the irrelevant data from dash_message_type first
@\DASH_REPORT_insert.sql;
@\DASH_REPORT_ACTION_insert.sql;
@\DASH_CONTRACT_PROP_insert.sql;
@\gscm_tasc.sql;
@\customer_info_update.sql;
@\adhoc_dml.sql;
--Need to run one time
--UPDATE DASH_REPORTS SET REPORT_NAME = 'User Guide' WHERE REPORT_NAME = 'Users Guide';
--UPDATE DASH_REPORTS SET REPORT_NAME = 'Admin User Guide' WHERE REPORT_NAME = 'Admin Users Guide';
--Need to run environment wise (eg: performence = perf)
--update dash_contract_prop set db_propery_value  = 'ISTEPREPORTS/perf' where db_property_name  = 'static.pdf.location' and projectid = 2;
--update dash_contract_prop set db_propery_value = 'TASCREPORTS/perf' where db_property_name = 'static.pdf.location' and projectid = 1;
/*
update PRISMGLOBAL.DASH_CONTRACT_PROP
   set db_propery_value = 'https://renqa.ctb.com/ctb.com/control/main'
 where projectid = 2
   and db_property_name = 'sso.redirect.logout';

update PRISMGLOBAL.DASH_CONTRACT_PROP
   set db_propery_value = 'https://renqa.ctb.com/ctb.com/control/main'
 where projectid = 2
   and db_property_name = 'sso.redirect.loginfail';

update PRISMGLOBAL.DASH_CONTRACT_PROP
   set db_propery_value = 'https://oastest.ctb.com/SessionWeb/login.jsp'
 where projectid = 1
   and db_property_name = 'sso.redirect.logout';

update PRISMGLOBAL.DASH_CONTRACT_PROP
   set db_propery_value = 'https://oastest.ctb.com/SessionWeb/login.jsp'
 where projectid = 1
   and db_property_name = 'sso.redirect.loginfail';
*/