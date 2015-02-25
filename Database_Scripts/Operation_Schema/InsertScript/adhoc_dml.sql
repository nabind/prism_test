--For ISTEP
   
/*   
CREATE DATABASE LINK PRISM_QA
  CONNECT TO PRISMGLOBAL IDENTIFIED BY PRISMGLOBAL14QA
  USING '(DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = 10.160.23.70)(PORT = 1521))
    (CONNECT_DATA =  
      (SERVER = DEDICATED)
      (SERVICE_NAME = EHS2CLQA)))';

drop table dash_messages_qa;
create table dash_messages_qa as
select * from dash_messages@PRISM_QA;

drop table Dash_Reports_qa;
create table Dash_Reports_qa as
select * from Dash_Reports@PRISM_QA;

select * from Dash_Reports_qa where report_name like '%System Con%';
10116	Generic System Configuration
10117	Product Specific System Configuration
101	Product Specific System Configuration
983	Generic System Configuration

select * from Dash_Reports where report_name like '%System Con%';
101	Product Specific System Configuration
983	Generic System Configuration
10116	Generic System Configuration
10117	Product Specific System Configuration

SELECT distinct MSG_TYPEID
          FROM DASH_MESSAGE_TYPE
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM');
         
SELECT distinct MSG_TYPEID
          FROM DASH_MESSAGE_TYPE@PRISM_QA
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM');   
         
select * 
  FROM DASH_MESSAGES
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM DASH_MESSAGE_TYPE
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM'));
         
delete FROM DASH_MESSAGES
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM DASH_MESSAGE_TYPE
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM'));         

INSERT INTO DASH_MESSAGES
SELECT (CASE
         WHEN DB_REPORTID = 10116 THEN
          10116
         WHEN DB_REPORTID = 10117 THEN
          10117
         ELSE
          DB_REPORTID
       END) DB_REPORTID,
       MSG_TYPEID,
       REPORT_MSG,
       CUST_PROD_ID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME
  FROM DASH_MESSAGES_QA A
 WHERE MSG_TYPEID IN
       (SELECT MSG_TYPEID
          FROM DASH_MESSAGE_TYPE
         WHERE MESSAGE_TYPE IN ('GSCM', 'PSCM'))
   AND DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM DASH_REPORTS_QA
         WHERE UPPER(REPORT_NAME) LIKE '%SYSTEM CONFIGURATION')
   AND NOT EXISTS (SELECT 1
          FROM DASH_MESSAGES D
         WHERE D.DB_REPORTID = A.DB_REPORTID
           AND D.MSG_TYPEID = A.MSG_TYPEID
           AND D.CUST_PROD_ID = A.CUST_PROD_ID);
		   
SELECT DR.DB_REPORTID,
       DM.MSG_TYPEID,
       DMT.MESSAGE_NAME,
       DM.REPORT_MSG,
       DM.CUST_PROD_ID --DM.REPORT_MSG AS REPORT_MSG
  FROM DASH_REPORTS DR, DASH_MESSAGES DM, DASH_MESSAGE_TYPE DMT
 WHERE DM.MSG_TYPEID = DMT.MSG_TYPEID
   AND DR.DB_REPORTID = DM.DB_REPORTID
   AND DR.REPORT_NAME = 'Generic System Configuration'
   AND DMT.MESSAGE_TYPE = 'GSCM'
     --AND DMT.MESSAGE_NAME = P_IN_MESSAGE_NAME
   AND DM.ACTIVATION_STATUS = 'AC'
 ORDER BY DR.DB_REPORTID, DM.MSG_TYPEID, DM.CUST_PROD_ID;
 
*/		   
