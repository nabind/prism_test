INSERT INTO ETL_PROJECT_CONFIG
  (SEQ_NO,
   COLUMN_NAME,
   START_POSITION,
   END_POSITION,
   LENGTH,
   CATEGORY,
   CATEGORY_TYPE,
   CATEGORY_VALUE,
   DATA_EXTRACT_BLOCK,
   REPLACE_FLAG,
   REPLACE_VALUE,
   IS_BIO_UPDATE,
   DESCRIPTION,
   ACTIVATION_STATUS,
   PROJECTID,
   DATETIMESTAMP)

VALUES
  ((SELECT MAX(SEQ_NO) + 1 FROM ETL_PROJECT_CONFIG),
   'BARCODE_NUMBER',
   10825,
   10832,
   8,
   'STG_STD_BIO_DETAILS',
   'BARCODE_NUMBER',
   'BARCODE_NUMBER',
   'ASSESSMENT',
   'N',
   '',
   'Y',
   'As captured from student answer document',
   'AC',
   1,
   SYSDATE);
   
   	 INSERT INTO ETL_PROJECT_CONFIG
   (SEQ_NO,
    COLUMN_NAME,
    START_POSITION,
    END_POSITION,
    LENGTH,
    CATEGORY,
    CATEGORY_TYPE,
    CATEGORY_VALUE,
    DATA_EXTRACT_BLOCK,
    REPLACE_FLAG,
    REPLACE_VALUE,
    IS_BIO_UPDATE,
    DESCRIPTION,
    ACTIVATION_STATUS,
    PROJECTID,
    DATETIMESTAMP)
 VALUES
   ((SELECT MAX(SEQ_NO) + 1 FROM ETL_PROJECT_CONFIG),
    'STATE_SPECIAL_CODE',
    53,
    54,
    2,
    'STG_HIER_DETAILS',
    'LEVEL_1',
    'STATE_SPECIAL_CODE',
    'ORG',
    'N',
    '',
    '',
    'Name as provided by customer. The field can not be all blank, but rather blanks are allowed within the name. 
    Element A would be always Top level.',
    'AC',
    1,
    SYSDATE);
	
	INSERT ALL
INTO ETL_JOBMASTER_CONFIG VALUES
('M_PP_E_RESOURCE_STAGING_LEVEL_VALIDATION','S_PP_E_RESOURCE_STAGING_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_PP_E_RESOURCE_STUDENT_LEVEL_VALIDATION','S_PP_E_RESOURCE_STUDENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_PP_E_RESOURCE_CONTENT_LEVEL_VALIDATION','S_PP_E_RESOURCE_CONTENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_OAS_E_RESOURCE_STAGING_LEVEL_VALIDATION','S_OAS_E_RESOURCE_STAGING_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_OAS_E_RESOURCE_STUDENT_LEVEL_VALIDATION','S_OAS_E_RESOURCE_STUDENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_OAS_E_RESOURCE_CONTENT_LEVEL_VALIDATION','S_OAS_E_RESOURCE_CONTENT_LEVEL_VALIDATION','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_ER_PP_OAS_LINKEDID_UPDATE','S_ER_PP_OAS_LINKEDID_UPDATE','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_PROCESS_VALIDATION_EMAIL','S_PP_PROCESS_VALIDATION_EMAIL','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_PP_PROCESS_VALIDATION_CHECK','S_PP_PROCESS_VALIDATION_CHECK','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
INTO ETL_JOBMASTER_CONFIG VALUES
('M_OAS_PROCESS_VALIDATION_CHECK','S_OAS_PROCESS_VALIDATION_CHECK','WKF_TASC_PRISM_LOAD','','','',1,SYSDATE,SYSDATE)
SELECT * FROM DUAL;


           INSERT ALL
INTO FORM_DIM VALUES(306,'D','D',1,SYSDATE)
INTO FORM_DIM VALUES(307,'E','E',1,SYSDATE)
INTO FORM_DIM VALUES(308,'F','F',1,SYSDATE)
INTO FORM_DIM VALUES(309,'BR-E','BR-E',1,SYSDATE)
INTO FORM_DIM VALUES(310,'BR-F','BR-F',1,SYSDATE)
SELECT * FROM DUAL;

	   INSERT INTO LEVEL_MAP
  SELECT ((SELECT MAX(LEVEL_MAPID) FROM LEVEL_MAP) + ROWNUM) AS LEVEL_MAPID,
      M.LEVELID,
       M.FORMID,
       M.ASSESSMENTID,
       SYSDATE AS DATETIMESTAMP
  FROM (SELECT LD.LEVELID, F.FORMID, 1001 AS ASSESSMENTID
          FROM LEVEL_DIM LD, FORM_DIM F 
          where ld.projectid = 1 and ld.projectid = f.projectid) M
WHERE NOT EXISTS (SELECT NULL
          FROM LEVEL_MAP LM
         WHERE LM.LEVELID = M.LEVELID
           AND LM.FORMID = M.FORMID
           AND LM.ASSESSMENTID = M.ASSESSMENTID)

--SUBTEST_OBJECTIVE_MAP INSERT (run this for all the new LEVEL_MAPID's created)
--SELECT * FROM LEVEL_MAP;
INSERT INTO SUBTEST_OBJECTIVE_MAP   
select (select max(subt_obj_mapid) from SUBTEST_OBJECTIVE_MAP) + rownum SUBT_OBJ_MAPID,
       SUBTESTID,
       OBJECTIVEID,
       40081 LEVEL_MAPID, --RUN FOR 40081-40085
       ASSESSMENTID,
       SYSDATE DATETIMESTAMP
  from SUBTEST_OBJECTIVE_MAP
 where level_mapid = 550;
 
 
/* SELECT COUNT(1),LEVEL_MAPID FROM SUBTEST_OBJECTIVE_MAP WHERE LEVEL_MAPID IN (40081,40082,40083,40084,40085)
 GROUP BY LEVEL_MAPID; --23 per LEVEL_MAPID*/
