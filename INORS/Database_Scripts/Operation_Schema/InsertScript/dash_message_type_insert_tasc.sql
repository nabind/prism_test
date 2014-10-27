insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
	   cust_prod_id,
       created_date_time)
    values
      (1087,
       'Common Header',
       'GSCM',
       'Message configuration for common header',
	   3001,
       sysdate);
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1088,
       'Common Footer',
       'GSCM',
       'Message configuration for common footer',
       3001,
       sysdate);	   
       
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1089,
       'Teacher Login Footer',
       'GSCM',
       'Footer for teacher login screen',
       3001,
       sysdate);   
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1090,
       'Parent Login Footer',
       'GSCM',
       'Footer for parent login screen',
       3001,
       sysdate);	   
       
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1092,
       'Landing Page Content',
       'GSCM',
       'Image or Content for Landing page',
       3001,
       sysdate);
	   
DELETE FROM DASH_MESSAGES
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM DASH_MESSAGE_TYPE
         WHERE UPPER(MESSAGE_NAME) IN ('PARENT LOG IN', 'TEACHER LOG IN'));

DELETE FROM DASH_MESSAGE_TYPE
 WHERE UPPER(MESSAGE_NAME) IN ('PARENT LOG IN', 'TEACHER LOG IN');	   

insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1093,
       'Teacher Login Page Content',
       'GSCM',
       'Image or Content for Teacher Login page',
       3001,
       sysdate);
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1094,
       'Parent Login Page Content',
       'GSCM',
       'Image or Content for Parent Login page',
       3001,
       sysdate);	   
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1095,
       'Teacher Login Outage Content',
       'GSCM',
       'Content of outage for Teacher Login page',
       3001,
       sysdate);	   
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1096,
       'Parent Login Outage Content',
       'GSCM',
       'Content of outage for Parent Login page',
       3001,
       sysdate);	

insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1097,
       'Teacher Home Page',
       'GSCM',
       'Content of Teacher Home Page',
       3001,
       sysdate);

insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1098,
       'Parent Home Page',
       'GSCM',
       'Content of Parent Home Page',
       3001,
       sysdate);	  

insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1099,
       'Teacher Home Footer',
       'GSCM',
       'Footer for teacher home screen',
       3001,
       sysdate);   
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1100,
       'Parent Home Footer',
       'GSCM',
       'Footer for parent home screen',
       3001,
       sysdate);	   
