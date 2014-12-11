@\dash_message_type_disable_constraint.sql
@\run_prc_msg_typeid_correction.sql;
@\menu_message_type_insert.sql;
@\dash_message_type_insert_inors.sql;
@\dash_message_type_insert_tasc.sql;
@\dash_message_type_enable_constraint.sql -- delete the irrelevant data from dash_message_type first
@\DASH_REPORT_insert.sql;
@\DASH_REPORT_ACTION_insert.sql;
@\gscm_tasc.sql;
@\customer_info_update.sql;
@\EDU_USER_ROLE_UPDATE.sql;
--Need to run environment wise (eg: performence = perf)
--update dash_contract_prop set db_propery_value  = 'ISTEPREPORTS/perf' where db_property_name  = 'static.pdf.location' and projectid = 2;
--update dash_contract_prop set db_propery_value = 'TASCREPORTS/perf' where db_property_name = 'static.pdf.location' and projectid = 1;