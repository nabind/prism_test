@\dash_message_type_disable_constraint.sql;
@\run_prc_msg_typeid_correction.sql; 
/*delete the irrelevant data from dash_message_type AND DASH_MESSAGES
SELECT * FROM DASH_MESSAGE_TYPE WHERE MESSAGE_TYPE = 'GSCM' ORDER BY MSG_TYPEID;
SELECT MSG_TYPEID, MESSAGE_NAME
  FROM DASH_MESSAGE_TYPE
 WHERE MESSAGE_TYPE = 'GSCM'
 GROUP BY MSG_TYPEID, MESSAGE_NAME
 HAVING COUNT(MSG_TYPEID) > 1;

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1037 AND CUST_PROD_ID <> 5001;
DELETE FROM DASH_MESSAGE_TYPE WHERE MSG_TYPEID = 1037 AND CUST_PROD_ID <> 5001;
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1038 AND CUST_PROD_ID <> 5001;
DELETE FROM DASH_MESSAGE_TYPE WHERE MSG_TYPEID = 1038 AND CUST_PROD_ID <> 5001;
*/
@\menu_message_type_insert.sql;
@\dash_message_type_insert_inors.sql;
@\dash_message_type_insert_tasc.sql;
@\gscm_tasc.sql;
@\dash_messages_insert.sql;
@\dash_message_type_enable_constraint.sql; -- delete the irrelevant data from dash_message_type first
@\DASH_REPORT_insert.sql;
@\DASH_REPORT_ACTION_insert.sql;
@\DASH_CONTRACT_PROP_insert.sql;
@\customer_info_update.sql;
/*Need to run one time
UPDATE DASH_REPORTS SET REPORT_NAME = 'User Guide' WHERE REPORT_NAME = 'Users Guide';
UPDATE DASH_REPORTS SET REPORT_NAME = 'Admin User Guide' WHERE REPORT_NAME = 'Admin Users Guide';
/*
----------------------For QA
update DASH_CONTRACT_PROP
   set db_propery_value = 'https://renqa.ctb.com/ctb.com/control/main'
 where projectid = 2
   and db_property_name = 'sso.redirect.logout';

update DASH_CONTRACT_PROP
   set db_propery_value = 'https://renqa.ctb.com/ctb.com/control/main'
 where projectid = 2
   and db_property_name = 'sso.redirect.loginfail';

update DASH_CONTRACT_PROP
   set db_propery_value = 'https://oastest.ctb.com/SessionWeb/login.jsp'
 where projectid = 1
   and db_property_name = 'sso.redirect.logout';

update DASH_CONTRACT_PROP
   set db_propery_value = 'https://oastest.ctb.com/SessionWeb/login.jsp'
 where projectid = 1
   and db_property_name = 'sso.redirect.loginfail';
   
----------------------For PROD
update DASH_CONTRACT_PROP
   set db_propery_value = 'https://ctb.com/ctb.com/control/main'
 where projectid = 2
   and db_property_name = 'sso.redirect.logout';

update DASH_CONTRACT_PROP
   set db_propery_value = 'https://ctb.com/ctb.com/control/main'
 where projectid = 2
   and db_property_name = 'sso.redirect.loginfail';

update DASH_CONTRACT_PROP
   set db_propery_value = 'https://oas.ctb.com/SessionWeb/login.jsp'
 where projectid = 1
   and db_property_name = 'sso.redirect.logout';

update DASH_CONTRACT_PROP
   set db_propery_value = 'https://oas.ctb.com/SessionWeb/login.jsp'
 where projectid = 1
   and db_property_name = 'sso.redirect.loginfail';   
   
*/