/*
@author: Joy
*/

begin
/* 105 is the adminid for 2014 - Use same msg_typeid for same message type */
  for rec in (select cust_prod_id from cust_product_link where adminid = 105) loop
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1055,
       'Foot Note',
       'FN',
       'Report page ---Foot Note(used for Report Legend)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1059,
       'Report Legend',
       'RL',
       'Report page ---Report Legend (Report footer section)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1054,
       'Embargo Notice',
       'EN',
       'Report page ---Embargo Notice(above report data)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1056,
       'Dataload Message',
       'DM',
       'Report page ---(for not showing the data to user if active)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1057,
       'Report Message',
       'RM',
       'Report page ---Report Message(below the report header)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1050,
       'More Info',
       'RSCM',
       'Report/Dashboard page --- More Info(next to pdf button)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1060,
       'Report Privacy Notice',
       'RPN',
       'Report page ---Report Privacy Notice (Report footer section)',
       rec.cust_prod_id,
       sysdate);
  
    insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1058,
       'Report Purpose',
       'RP',
       'Report page ---Report Purpose (Report footer section)',
       rec.cust_prod_id,
       sysdate);
  
  end loop;
  commit;
end;
