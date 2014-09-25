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
	   5001,
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
       5001,
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
       5001,
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
       5001,
       sysdate);		   
       
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time)
    values
      (1091,
       'Menu Message',
       'PSCM',
       'Message configuration for right sided menu',
       5001,
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
       5001,
       sysdate);
	   
DELETE FROM DASH_MESSAGES
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM DASH_MESSAGE_TYPE
         WHERE UPPER(MESSAGE_NAME) IN ('PARENT LOG IN', 'TEACHER LOG IN'));

DELETE FROM DASH_MESSAGE_TYPE
 WHERE UPPER(MESSAGE_NAME) IN ('PARENT LOG IN', 'TEACHER LOG IN');	   
 
UPDATE DASH_MESSAGE_TYPE
   SET MESSAGE_NAME = 'Teacher Home Page'
 WHERE MESSAGE_NAME = 'Inors Home Page' 

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
       5001,
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
       5001,
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
       5001,
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
       5001,
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
       5001,
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
       5001,
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
       5001,
       sysdate);		   
