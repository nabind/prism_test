DELETE FROM DASH_MESSAGES where CUST_PROD_ID = 5027;
DELETE FROM DASH_MESSAGE_TYPE where CUST_PROD_ID = 5027;
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
	   5027,
       sysdate);

insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
     cust_prod_id,
       created_date_time)
    values
      (1085,
       'Group Download Instruction',
       'GSCM',
       'Instructions message configuration for Group Download page',
	   5027,
       sysdate);
	   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
     cust_prod_id,
       created_date_time)
    values
      (1101,
       'GDF Header Message',
       'GSCM',
       'GDF Header Message',
	   5027,
       sysdate);

insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
     cust_prod_id,
       created_date_time)
    values
      (1102,
       'GDF Notice',
       'GSCM',
       'GDF Notice',
	   5027,
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
       5027,
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
       5027,
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
       5027,
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
       5027,
       sysdate);		   
	   
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
       5027,
       sysdate);	  

INSERT INTO DASH_MESSAGE_TYPE
      (MSG_TYPEID,
       MESSAGE_NAME,
       MESSAGE_TYPE,
       DESCRIPTION,
       CUST_PROD_ID,
       CREATED_DATE_TIME)
    VALUES
      (1091,
       'Menu Message',
       'PSCM',
       'Message configuration for right sided menu',
       5027,
       SYSDATE);	   	   