INSERT INTO job_partition_status
select (select max(JOB_PARTITION_ID) from job_partition_status) + rownum AS JOB_PARTITION_ID,
       'WKF_PARTITION_' ||
       ((select max(JOB_PARTITION_ID) from job_partition_status) + rownum) WKF_PARTITION_NAME,
       'OAS' LOAD_MODE,
       'IN' STATUS,
       DATETIMESTAMP
  from job_partition_status
  WHERE ROWNUM<=50;
  
INSERT INTO job_partition_status
select (select max(JOB_PARTITION_ID) from job_partition_status) + rownum AS JOB_PARTITION_ID,
       'ER_EXCP'
        WKF_PARTITION_NAME,
       'EXCEPTION' LOAD_MODE,
       'IN' STATUS,
       DATETIMESTAMP
  from job_partition_status
  WHERE LOAD_MODE='PP';
  
INSERT INTO job_partition_status
select (select max(JOB_PARTITION_ID) from job_partition_status) + rownum AS JOB_PARTITION_ID,
       'BR_EXCP'
        WKF_PARTITION_NAME,
       'EXCEPTION' LOAD_MODE,
       'IN' STATUS,
       DATETIMESTAMP
  from job_partition_status
  WHERE LOAD_MODE='PP'; 
  

--INSERT INTO ER_EXCEPTION TABLE
INSERT ALL
INTO ER_EXCEPTION VALUES(100,'Other Errors','ER')
INTO ER_EXCEPTION VALUES(101,'UUID Missing','ER')
INTO ER_EXCEPTION VALUES(102,'State-Code Missing','ER')
INTO ER_EXCEPTION VALUES(103,'ScheduleID Missing','ER')
INTO ER_EXCEPTION VALUES(104,'Date-Scheduled Missing','ER')
INTO ER_EXCEPTION VALUES(105,'Content-Area-Code Missing','ER')
INTO ER_EXCEPTION VALUES(106,'Content-Test-Code Missing','ER')
INTO ER_EXCEPTION VALUES(107,'Content-Test-Type Missing','ER')
INTO ER_EXCEPTION VALUES(108,'Student-Last-Name Missing','ER')
INTO ER_EXCEPTION VALUES(131,'State-Code Invalid','ER')
INTO ER_EXCEPTION VALUES(132,'Date-Scheduled Invalid','ER')
INTO ER_EXCEPTION VALUES(133,'Content-Area-Code Invalid','ER')
INTO ER_EXCEPTION VALUES(134,'Content-Test-Code Invalid','ER')
INTO ER_EXCEPTION VALUES(135,'Content-Test-Type Invalid','ER')
INTO ER_EXCEPTION VALUES(136,'Gender is Invalid','ER')
INTO ER_EXCEPTION VALUES(137,'Resolved Ethnicity is Invalid','ER')
INTO ER_EXCEPTION VALUES(138,'Date checked-in is invalid','ER')
INTO ER_EXCEPTION VALUES(139,'Date-of-birth is invalid','ER')
INTO ER_EXCEPTION VALUES(151,'Content-Test-Code Duplicate','ER')
INTO ER_EXCEPTION VALUES(152,'ScheduleID Duplicate','ER')
INTO ER_EXCEPTION VALUES(153,'Duplicate schedule','ER')
INTO ER_EXCEPTION VALUES(197,'Multiple test open for a Content','ER')
INTO ER_EXCEPTION VALUES(198,'Barcode Missing for PBT','ER')
INTO ER_EXCEPTION VALUES(199,'No-Further-Update in eR-Repository','ER')

INTO ER_EXCEPTION VALUES(200,'Other Errors','OAS')
INTO ER_EXCEPTION VALUES(201,'UUID Missing','OAS')
INTO ER_EXCEPTION VALUES(202,'Student-Last-Name Missing','OAS')
INTO ER_EXCEPTION VALUES(203,'Content-Code Missing','OAS')
INTO ER_EXCEPTION VALUES(204,'Form Missing','OAS')
INTO ER_EXCEPTION VALUES(205,'Date-Test-Taken Missing','OAS')
INTO ER_EXCEPTION VALUES(231,'Content-Code Invalid','OAS')
INTO ER_EXCEPTION VALUES(232,'Date-Test-Taken Invalid','OAS')
INTO ER_EXCEPTION VALUES(251,'All_Omitted_Content_Code','OAS')
INTO ER_EXCEPTION VALUES(289,'Student-Not-Found in eR-Repository ','OAS')
INTO ER_EXCEPTION VALUES(299,'No-Match-Found in eR-Repository','OAS')

INTO ER_EXCEPTION VALUES(300,'Other Errors','PP')
INTO ER_EXCEPTION VALUES(301,'UUID Missing','PP')
INTO ER_EXCEPTION VALUES(302,'Barcode Missing','PP')
INTO ER_EXCEPTION VALUES(303,'Content-Code Missing','PP')
INTO ER_EXCEPTION VALUES(304,'Form Missing','PP')
INTO ER_EXCEPTION VALUES(331,'Content-Code Invalid','PP')
INTO ER_EXCEPTION VALUES(351,'All_Omitted_Content_Code','PP')
INTO ER_EXCEPTION VALUES(389,'Student-Not-Found in eR-Repository ','PP')
INTO ER_EXCEPTION VALUES(399,'No-Match-Found in eR-Repository','PP')
SELECT * FROM DUAL;

  
--STG_DATA_LAYOUT_CONFIG
--SELECT MAX(SEQ_NO) FROM STG_DATA_LAYOUT_CONFIG; --737

--POPULATE BARCODE_NUMBER
/*
INSERT INTO STG_DATA_LAYOUT_CONFIG VALUES(738,'BARCODE_NUMBER',10825,10832,8,
                                          'STG_STD_BIO_DETAILS','BARCODE_NUMBER','BARCODE_NUMBER','ASSESSMENT',
										  'N','','Y','As captured from student answer document','AC',SYSDATE);

*/

-----------------------------------

										  
--POPULATE STATE_SPECIAL_CODE
/* INSERT INTO STG_DATA_LAYOUT_CONFIG VALUES
  (739,'STATE_SPECIAL_CODE',53,54,2,'STG_HIER_DETAILS','LEVEL_1','STATE_SPECIAL_CODE','ORG','N','','',
   'Name as provided by customer. The field can not be all blank, but rather blanks are allowed within the name. 
    Element A would be always Top level.','AC',SYSDATE);
*/

-----------------------------------------
	
 -- HIERARCHY STATE_ORG_CODE ENTRY.
 INSERT INTO STG_HIER_DATA_LAYOUT_CONFIG
  (SEQ_NO,
   COLUMN_NAME,
   START_POSITION,
   END_POSITION,
   LENGTH,
   CATEGORY_TYPE,
   CATEGORY_VALUE,
   ACTIVATION_STATUS,
   DATETIMESTAMP)
VALUES
(
(SELECT max(SEQ_NO)+1
  FROM STG_HIER_DATA_LAYOUT_CONFIG),
 'STATE_ORG_CODE' ,
 11,
 12,
 2,
 'LEVEL_1',
 'STATE_ORG_CODE',
 'AC',
 SYSDATE
 );

--STG_ETL_JOBMASTER DML SCRIPT
/*
INSERT ALL
INTO STG_ETL_JOBMASTER VALUES
('M_PP_E_RESOURCE_STAGING_LEVEL_VALIDATION','S_PP_E_RESOURCE_STAGING_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_PP_E_RESOURCE_STUDENT_LEVEL_VALIDATION','S_PP_E_RESOURCE_STUDENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_PP_E_RESOURCE_CONTENT_LEVEL_VALIDATION','S_PP_E_RESOURCE_CONTENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_OAS_E_RESOURCE_STAGING_LEVEL_VALIDATION','S_OAS_E_RESOURCE_STAGING_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_OAS_E_RESOURCE_STUDENT_LEVEL_VALIDATION','S_OAS_E_RESOURCE_STUDENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_OAS_E_RESOURCE_CONTENT_LEVEL_VALIDATION','S_OAS_E_RESOURCE_CONTENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_ER_PP_OAS_LINKEDID_UPDATE','S_ER_PP_OAS_LINKEDID_UPDATE','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_PROCESS_VALIDATION_EMAIL','S_PP_PROCESS_VALIDATION_EMAIL','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_PP_PROCESS_VALIDATION_CHECK','S_PP_PROCESS_VALIDATION_CHECK','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO STG_ETL_JOBMASTER VALUES
('M_OAS_PROCESS_VALIDATION_CHECK','S_OAS_PROCESS_VALIDATION_CHECK','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
SELECT * FROM DUAL;
*/

--ENTRY FOR PRISMGLOBAL



--FORM_DIM
/*
INSERT ALL
INTO FORM_DIM VALUES(306,'D','D',SYSDATE)
INTO FORM_DIM VALUES(307,'E','E',SYSDATE)
INTO FORM_DIM VALUES(308,'F','F',SYSDATE)
INTO FORM_DIM VALUES(309,'BR-D','BR-D',SYSDATE)
INTO FORM_DIM VALUES(310,'BR-E','BR-E',SYSDATE)
INTO FORM_DIM VALUES(311,'BR-F','BR-F',SYSDATE)
SELECT * FROM DUAL;--NEED TO PATCH IN PRISMGLOBAL
*/

--global schema


--DEMOGRAPHIC_VALUES ENTRY RESPECTIVE OF DEMOGRAPHIC TEST_FORM ENTRY
/*
insert into demographic_values
SELECT SEQ_DEMO_VALID.NEXTVAL AS DEMO_VALID,
D.DEMOID AS DEMOID,
DECODE(DV.DEMO_VALUE_NAME,
        'A1','D1',
        'A2','D2',
        'A3','D3',
        'A4','D4',
        'A5','D5',
        'A6','D6',
        'A7','D7',
        'B1','E1',
        'B2','E2',
        'B3','E3',
        'B4','E4',
        'B5','E5',
        'B6','E6',
        'B7','E7',
        'C1','F1',
        'C2','F2',
        'C3','F3',
        'C4','F4',
        'C5','F5',
        'C6','F6',
        'C7','F7',
        'A','D',
        'B','E',
        'C','F',
        'BR-A','BR-D',
        'BR-B','BR-E',
        DV.DEMO_VALUE_NAME) AS DEMO_VALUE_NAME,
DECODE(DV.DEMO_VALUE_CODE,
        'A1','D1',
        'A2','D2',
        'A3','D3',
        'A4','D4',
        'A5','D5',
        'A6','D6',
        'A7','D7',
        'B1','E1',
        'B2','E2',
        'B3','E3',
        'B4','E4',
        'B5','E5',
        'B6','E6',
        'B7','E7',
        'C1','F1',
        'C2','F2',
        'C3','F3',
        'C4','F4',
        'C5','F5',
        'C6','F6',
        'C7','F7',
        'A','D',
        'B','E',
        'C','F',
        'BR-A','BR-D',
        'BR-B','BR-E',
        DV.DEMO_VALUE_CODE) AS DEMO_VALUE_CODE,
DV.DEMO_VALUE_SEQ,
DV.IS_DEFAULT,
SYSDATE AS DATETIMESTAMP
  FROM DEMOGRAPHIC D, DEMOGRAPHIC_VALUES DV
WHERE D.DEMOID = DV.DEMOID
   AND D.DEMO_CODE IN ('Fld_Tst_Form', 'Test_Form')
   AND D.CUSTOMERID = 1001
   AND INSTR(DV.DEMO_VALUE_NAME, '8') = 0;
   
   
    INSERT INTO DEMOGRAPHIC_VALUES VALUES
 (
 SEQ_DEMO_VALID.NEXTVAL,
 108229,
 'BR-F',
 'BR-F',
 6,
 'N',
 SYSDATE
 );
*/

--LEVEL_MAP INSERT   
 /*  INSERT INTO LEVEL_MAP
  SELECT ((SELECT MAX(LEVEL_MAPID) FROM LEVEL_MAP) + ROWNUM) AS LEVEL_MAPID,
      M.LEVELID,
       M.FORMID,
       M.ASSESSMENTID,
       SYSDATE AS DATETIMESTAMP
  FROM (SELECT LD.LEVELID, F.FORMID, 1002 AS ASSESSMENTID
          FROM LEVEL_DIM LD, FORM_DIM F) M
WHERE NOT EXISTS (SELECT NULL
          FROM LEVEL_MAP LM
         WHERE LM.LEVELID = M.LEVELID
           AND LM.FORMID = M.FORMID
           AND LM.ASSESSMENTID = M.ASSESSMENTID)
*/		   


--INSERT INTO DEMO_CONFIG TABLE
INSERT INTO DEMO_CONFIG VALUES (1,'RegisteredAtTestCenterCountyParishCode','Stdnt_Reg_Tccp_Cd','BO',NULL,NULL,'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG VALUES (2,'RegisteredAtTestCenterCode','Stdnt_Reg_Tc_Cd','BO',NULL,NULL,'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG VALUES (3,'Sched_DateChkdIn','Cont_Dt_Chk_Read','BO',2047,NULL,'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG VALUES (4,'Sched_DateChkdIn','Cont_Dt_Chk_Wrt','BO',2048,NULL,'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG VALUES (5,'Sched_DateChkdIn','Cont_Dt_Chk_Math','BO',2050,NULL,'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG VALUES (6,'Sched_DateChkdIn','Cont_Dt_Chk_Sci','BO',2051,NULL,'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG VALUES (7,'Sched_DateChkdIn','Cont_Dt_Chk_Sc','BO',2052,NULL,'N',NULL,NULL,NULL);
------------
INSERT INTO DEMO_CONFIG  VALUES (8,'Sched_ECC','Cont_Ecc_Read','BO', 2047, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (9,'Sched_ECC','Cont_Ecc_Wrt', 'BO', 2048, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (10,'Sched_ECC','Cont_Ecc_Math', 'BO', 2050, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (11,'Sched_ECC','Cont_Ecc_Sci', 'BO', 2051, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (12,'Sched_ECC','Cont_Ecc_Sc', 'BO', 2052, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (13,'Sched_TC_Ctr_Cd','Cont_Tc_Cd_Read', 'BO', 2047, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (14,'Sched_TC_Ctr_Cd','Cont_Tc_Cd_Wrt', 'BO', 2048, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (15,'Sched_TC_Ctr_Cd','Cont_Tc_Cd_Math', 'BO', 2050, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (16,'Sched_TC_Ctr_Cd','Cont_Tc_Cd_Sci', 'BO', 2051, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (17,'Sched_TC_Ctr_Cd','Cont_Tc_Cd_Sc', 'BO', 2052, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (18,'Sched_TC_CountyParishCode','Cont_Sch_Tp_Cd_Read', 'BO', 2047, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (19,'Sched_TC_CountyParishCode','Cont_Sch_Tp_Cd_Wrt', 'BO', 2048, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (20,'Sched_TC_CountyParishCode','Cont_Sch_Tp_Cd_Math', 'BO', 2050, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (21,'Sched_TC_CountyParishCode','Cont_Sch_Tp_Cd_Sci', 'BO', 2051, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (22,'Sched_TC_CountyParishCode','Cont_Sch_Tp_Cd_Sc', 'BO', 2052, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (23,'Schedule_ID','Cont_Schld_Id_Read', 'BO', 2047, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (24,'Schedule_ID','Cont_Schld_Id_Wrt', 'BO', 2048, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (25,'Schedule_ID','Cont_Schld_Id_Math', 'BO', 2050, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (26,'Schedule_ID','Cont_Schld_Id_Sci', 'BO', 2051, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (27,'Schedule_ID','Cont_Schld_Id_Sc', 'BO', 2052, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (28,'Sched_Date','Cont_Dt_Schld_Read', 'BO', 2047, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (29,'Sched_Date','Cont_Dt_Schld_Wrt', 'BO', 2048, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (30,'Sched_Date','Cont_Dt_Schld_Math', 'BO', 2050, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (31,'Sched_Date','Cont_Dt_Schld_Sci', 'BO', 2051, NULL, 'N',NULL,NULL,NULL);
INSERT INTO DEMO_CONFIG  VALUES (32,'Sched_Date','Cont_Dt_Schld_Sc', 'BO', 2052, NULL, 'N',NULL,NULL,NULL); 
----------------------
INSERT INTO DEMO_CONFIG  VALUES (33,'TASCReadiness','Stdnt_Tasc_Rd', 'BO', NULL, NULL, 'Y','Y,N, ','Y,N,<BLANK>','N'); 
INSERT INTO DEMO_CONFIG  VALUES (34,'Content_Test_Code','Cont_Tst_Cd_Read', 'BO', 2047, NULL, 'Y',' ,Y','<BLANK>,Y','N');
INSERT INTO DEMO_CONFIG  VALUES (35,'Content_Test_Code','Cont_Tst_Cd_Wrt', 'BO', 2048, NULL, 'Y',' ,Y','<BLANK>,Y','N');
INSERT INTO DEMO_CONFIG  VALUES (36,'Content_Test_Code','Cont_Tst_Cd_Math', 'BO', 2050, NULL, 'Y',' ,Y','<BLANK>,Y','N');
INSERT INTO DEMO_CONFIG  VALUES (37,'Content_Test_Code','Cont_Tst_Cd_Sci', 'BO', 2051, NULL, 'Y',' ,Y','<BLANK>,Y','N');
INSERT INTO DEMO_CONFIG  VALUES (38,'Content_Test_Code','Cont_Tst_Cd_Sc', 'BO', 2052, NULL, 'Y',' ,Y','<BLANK>,Y','N');
----------------------------------------
INSERT INTO DEMO_CONFIG VALUES (39,'Test Form','Test_Form', 'BO', NULL, NULL, 'Y','D,E,F,BR-E,BR-F','D,E,F,BR-E,BR-F','N');
INSERT INTO DEMO_CONFIG  
VALUES (40,'Field Test Form','Fld_Tst_Form', 'OL', NULL, NULL, 'Y',
'D1,D2,D3,D4,D5,D6,D7,E1,E2,E3,E4,E5,E6,E7,F1,F2,F3,F4,F5,F6,F7',
'D1,D2,D3,D4,D5,D6,D7,E1,E2,E3,E4,E5,E6,E7,F1,F2,F3,F4,F5,F6,F7','N'); 

