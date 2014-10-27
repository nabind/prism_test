--For ISTEP
select * from DASH_MESSAGE_TYPE where message_name = 'Children Overview';
delete from DASH_MESSAGE_TYPE where message_name = 'Children Overview' and cust_prod_id in (5023,5024);

select * from DASH_MESSAGE_TYPE where message_name = 'Common Log In'; --1037
delete from dash_messages where msg_typeid = 1037 and cust_prod_id in (5023,5024);
delete from DASH_MESSAGE_TYPE where message_name = 'Common Log In' and cust_prod_id in (5023,5024);

select * from DASH_MESSAGE_TYPE where message_name = 'Teacher Home Page' --1097
delete from dash_messages where msg_typeid = 1097 and cust_prod_id in (5023,5024);
delete from DASH_MESSAGE_TYPE where message_name = 'Teacher Home Page' and cust_prod_id in (5023,5024);
   
insert into dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
     cust_prod_id,
       created_date_time)
    values
      (1086,
       'Growth Home Page',
       'GSCM',
       'Message configuration for Growth Home Page',
     5001,
       sysdate);
