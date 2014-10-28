--For ISTEP
select * from DASH_MESSAGE_TYPE where message_name = 'Children Overview';
delete from dash_messages where msg_typeid = 1037 and cust_prod_id in (5023,5024);
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
	   
--ADHOC DASH_MESSAGES LOAD AFTER NEW DML'S are applied. Need to check DB_REPORTID
/*INSERT INTO DASH_MESSAGES
SELECT (case
         when DB_REPORTID = 2051 then
          10119
         when DB_REPORTID = 2052 then
          10120
         else
          DB_REPORTID
       end) DB_REPORTID,
       MSG_TYPEID,
       REPORT_MSG,
       CUST_PROD_ID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM prism_global.DASH_MESSAGES a
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM prism_global.DASH_MESSAGE_TYPE
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM'))
   AND DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM prism_global.DASH_REPORTS
         WHERE UPPER(REPORT_NAME) LIKE '%SYSTEM CONFIGURATION')
   and not exists (select 1
          from DASH_MESSAGES d
         where d.DB_REPORTID = a.DB_REPORTID
           and d.MSG_TYPEID = a.MSG_TYPEID
           and d.CUST_PROD_ID = a.CUST_PROD_ID);
*/		   
