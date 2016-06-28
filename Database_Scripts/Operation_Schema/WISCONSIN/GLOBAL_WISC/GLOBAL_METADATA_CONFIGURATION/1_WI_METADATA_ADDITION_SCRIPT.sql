--PROJECT_DIM
--SELECT * FROM PROJECT_DIM
INSERT INTO PROJECT_DIM (PROJECTID, PROJECT_NAME, CREATED_DATE_TIME, UPDATED_DATE_TIME) 
VALUES ((SELECT MAX(PROJECTID)+1 FROM PROJECT_DIM ), 'Wisconsin Forward Exam', SYSDATE, SYSDATE);

COMMIT;

SELECT * FROM PROJECT_DIM
---CUSTOMER_INFO
INSERT INTO CUSTOMER_INFO
SELECT  (SELECT MAX(CUSTOMERID) FROM CUSTOMER_INFO)+ROWNUM AS CUSTOMERID,
       'Wisconsin State DPI' AS CUSTOMER_NAME,
       'Y' AS DISPLAY_TP_SELECTION,
       '/WIREPORTS/WISCONSIN' AS FILE_LOCATION,
       NULL AS SUPPORT_EMAIL,
       NULL AS SEND_LOGIN_PDF,
       'WI' AS CUSTOMER_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM DUAL; 
COMMIT;     
--SELECT * FROM CUSTOMER_INFO;

--ADMIN_DIM;
INSERT INTO ADMIN_DIM
SELECT (SELECT MAX(ADMINID)+1 FROM ADMIN_DIM) AS ADMINID,
       'Wisconsin Spring 2016' AS ADMIN_NAME,
       'SPRING/SUMMER' AS ADMIN_SEASON,
        2016 AS ADMIN_YEAR,
        'Y' AS IS_CURRENT_ADMIN,
        2016 AS FILE_LOCATION,
        1 AS ADMIN_SEQ,
        (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
        SYSDATE AS DATETIMESTAMP
 FROM DUAL;       
COMMIT;
--SELECT * FROM ADMIN_DIM;


-- PRODUCT;
INSERT INTO PRODUCT
SELECT (SELECT MAX(PRODUCTID) FROM PRODUCT)+1 AS PRODUCTID,
       'Wisconsin Forward Exam 2016' AS PRODUCT_NAME,
       'OL' AS PRODUCT_TYPE,
       1 AS PRODUCT_SEQ,
       '2016' AS PRODUCT_CODE,
       'N' AS IS_IC_REQUIRED,
       'Y' AS IS_EDITABLE,
       '/2016' AS FILE_LOCATION,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM DUAL;       
COMMIT;
--SELECT * FROM PRODUCT

--CUST_PRODUCT_LINK
INSERT INTO CUST_PRODUCT_LINK
SELECT (SELECT MAX(CUST_PROD_ID) FROM CUST_PRODUCT_LINK)+ ROWNUM AS CUST_PROD_ID,
       CI.CUSTOMERID,       
       PDT.PRODUCTID,
       ADM.ADMINID,
       'AC' AS ACTIVATION_STATUS,
       SYSDATE AS DATETIMESTAMP
FROM CUSTOMER_INFO CI,
     PRODUCT PDT,
     ADMIN_DIM ADM,
     PROJECT_DIM PJT
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND PJT.PROJECTID = PDT.PROJECTID
  AND CI.PROJECTID = PDT.PROJECTID 
  AND ADM.PROJECTID = PDT.PROJECTID ;
  COMMIT;
--SELECT * FROM CUST_PRODUCT_LINK;  
 
--ASSESSMENT_DIM;
INSERT INTO ASSESSMENT_DIM
SELECT (SELECT MAX(ASSESSMENTID) FROM ASSESSMENT_DIM)+ROWNUM AS ASSESSMENTID,
       'Wisconsin Forward Exam 2016' AS ASSESSMENT_NAME,
       'AC' AS ASSESSMENT_TYPE,
       '2016' AS ASSESSMENT_CODE,
       PDT.PRODUCTID,
       SYSDATE AS DATETIMESTAMP
FROM PRODUCT PDT,
     PROJECT_DIM PJT
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND PJT.PROJECTID = PDT.PROJECTID;
 COMMIT;
--SELECT * FROM ASSESSMENT_DIM; 


--TEST_PROGRAM
INSERT INTO TEST_PROGRAM
SELECT (SELECT MAX(TP_ID) FROM TEST_PROGRAM)+ ROWNUM AS TP_ID,
       '2016' AS TP_CODE,
       'Wisconsin Spring 2016' AS TP_NAME,
       'PUBLIC' AS TP_TYPE,
       3 AS NUM_LEVELS,
       'OL' AS TP_MODE,
       CI.CUSTOMERID,
       ADM.ADMINID,
       'AC' AS ACTIVATION_STATUS,
       SYSDATE AS DATETIMESTAMP
FROM CUSTOMER_INFO CI,
     ADMIN_DIM ADM,
     PROJECT_DIM PJT
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND CI.PROJECTID = PJT.PROJECTID 
  AND ADM.PROJECTID = PJT.PROJECTID ;
COMMIT;
--SELECT * FROM TEST_PROGRAM;

--ORG_TP_STRUCTURE
INSERT INTO ORG_TP_STRUCTURE
SELECT TP.TP_ID,
       LVL.ORG_LEVEL,
       LVL.ORG_LABEL,
       SYSDATE AS DATETIMESTAMP
FROM CUSTOMER_INFO CI,
     ADMIN_DIM ADM,
     PROJECT_DIM PJT,
     TEST_PROGRAM TP,
     (SELECT 1 AS ORG_LEVEL , 'State' AS ORG_LABEL FROM DUAL
     UNION ALL
     SELECT 2 AS ORG_LEVEL , 'District' AS ORG_LABEL FROM DUAL
     UNION ALL
     SELECT 3 AS ORG_LEVEL , 'School' AS ORG_LABEL FROM DUAL) LVL
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND CI.PROJECTID = PJT.PROJECTID 
  AND ADM.PROJECTID = PJT.PROJECTID
  AND TP.CUSTOMERID=CI.CUSTOMERID
  AND TP.ADMINID = ADM.ADMINID;
   COMMIT;
--SELECT * FROM ORG_TP_STRUCTURE;

--CONTENT_DIM
INSERT INTO CONTENT_DIM
SELECT (SELECT MAX(CONTENTID) FROM CONTENT_DIM)+ ROWNUM AS CONTENTID,
       LVL.NAME AS CONTENT_NAME,
       LVL.SEQ AS CONTENT_SEQ,
       ASES.ASSESSMENTID,
       SYSDATE AS DATETIMESTAMP
FROM PRODUCT PDT,
     PROJECT_DIM PJT,
     ASSESSMENT_DIM ASES,
    (SELECT 1 AS SEQ , 'English Language Arts' AS NAME FROM DUAL
     UNION ALL
     SELECT 2 AS SEQ , 'Mathematics' AS NAME FROM DUAL
     UNION ALL
     SELECT 3 AS SEQ , 'Science' AS NAME FROM DUAL
     UNION ALL
     SELECT 4 AS SEQ , 'Social Studies' AS NAME FROM DUAL) LVL
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND PJT.PROJECTID = PDT.PROJECTID
  AND PDT.PRODUCTID =ASES.PRODUCTID ;
 COMMIT;
--SELECT * FROM CONTENT_DIM;

--SUBTEST_DIM
INSERT INTO SUBTEST_DIM
SELECT (SELECT MAX(SUBTESTID) FROM SUBTEST_DIM )+ROWNUM AS SUBTESTID,
		A.SUBTEST_NAME,
		A.SUBTEST_SEQ,
		A.SUBTEST_CODE,
		A.SUBTEST_TYPE,
		A.CONTENTID,
		A.CANDIDATE_SUB_SEQ,
		A.DATETIMESTAMP
FROM(SELECT CONT.CONTENT_NAME AS SUBTEST_NAME,
			CONT.CONTENT_SEQ AS SUBTEST_SEQ,
			'0'||CONT.CONTENT_SEQ AS SUBTEST_CODE,
			'S' AS SUBTEST_TYPE,
			CONT.CONTENTID,
			NULL AS CANDIDATE_SUB_SEQ,
			SYSDATE AS DATETIMESTAMP
			FROM PRODUCT PDT,
				 PROJECT_DIM PJT,
				 ASSESSMENT_DIM ASES,
				 CONTENT_DIM CONT
			WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
			  AND PJT.PROJECTID = PDT.PROJECTID
			  AND PDT.PRODUCTID =ASES.PRODUCTID 
			  AND CONT.ASSESSMENTID = ASES.ASSESSMENTID
			  ORDER BY CONT.CONTENT_NAME ,
					   CONT.CONTENT_SEQ ,
					   '0'||CONT.CONTENT_SEQ,
					    SUBTEST_TYPE,
					   CONT.CONTENTID)A;
       
 COMMIT;
--SELECT * FROM SUBTEST_DIM ;


--OBJECTIVE_DIM
--SELECT * FROM OBJECTIVE_DIM WHERE PROJECTID = 4 AND OBJECTIVE_NAME='Political Science and Citizenship'
--UPDATE OBJECTIVE_DIM SET OBJECTIVE_CODE = 3 WHERE PROJECTID = 5 AND OBJECTIVE_NAME='Political Science and Citizenship'
--DELETE FROM OBJECTIVE_DIM WHERE PROJECTID = 4
ALTER TABLE OBJECTIVE_DIM MODIFY OBJECTIVE_NAME VARCHAR2(65);
ALTER TABLE OBJECTIVE_DIM MODIFY OBJECTIVE_DESC VARCHAR2(300);
INSERT INTO OBJECTIVE_DIM
SELECT (SELECT MAX(OBJECTIVEID) FROM OBJECTIVE_DIM)+ROWNUM AS OBJECTIVEID,
       META_OBJ.NME AS OBJECTIVE_NAME,
       (SELECT MAX(OBJECTIVE_SEQ) FROM OBJECTIVE_DIM)+ROWNUM AS OBJECTIVE_SEQ,
       META_OBJ.TYP AS OBJECTIVE_TYPE,
       META_OBJ.CODE AS OBJECTIVE_CODE,
       META_OBJ.DESCRIPTION AS OBJECTIVE_DESC,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM (select 'Reading Key Ideas and Details' AS NME,          '01' AS CODE,  '01' AS TYP,     '01' AS SUBTEST_CODE,  'A' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Reading Craft & Structure/Integration of Knowledge & Ideas' AS NME,          '02' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Reading Vocabulary Use' AS NME,          '03' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language Text Types and Purposes' AS NME,          '04' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language Research' AS NME,      '05' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language Language Conventions' AS NME,    '06' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'F' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language Listening' AS NME,      '07' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'G' AS DESCRIPTION FROM DUAL
      UNION ALL
	  ---
      select 'Operations and Algebraic Thinking' AS NME,        '01' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'A' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Number and Operations in Base Ten' AS NME,        '02' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Number and Operations - Fractions' AS NME,        '03' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Measurement and Data' AS NME,          '04' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Ratios and Proportional Relationships' AS NME,          '05' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'F' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'The Number System' AS NME,        '06' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'G' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Expressions and Equations' AS NME,  '07' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'H' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Functions' AS NME,          '08' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'J' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Geometry' AS NME,        '09' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Statistics and Probability' AS NME,        '10' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'I' AS DESCRIPTION FROM DUAL
      UNION ALL
      ---
	  select 'Science Connections & Nature of Science' AS NME,          '01' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'A/B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Science Inquiry' AS NME,        '02' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Physical Science' AS NME,  '03' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Earth and Space Science' AS NME,        '04' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Life & Environmental Science' AS NME,          '05' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'F' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Science Applications and Social Perspectives' AS NME,        '06' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'G/H' AS DESCRIPTION FROM DUAL
	  UNION ALL
	  ---	  
      select 'Geography' AS NME,        '01' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'A' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'History' AS NME,  '02' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Political Science and Citizenship' AS NME,        '03' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Economics' AS NME,          '04' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'The Behavioral Sciences' AS NME,        '05' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      ORDER BY SUBTEST_CODE,CODE)META_OBJ;

COMMIT;
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Reading - Key Ideas and Details' WHERE OBJECTIVE_NAME = 'Reading Key Ideas and Details' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Reading - Craft & Structure/Integration of Knowledge & Ideas' WHERE OBJECTIVE_NAME = 'Reading Craft & Structure/Integration of Knowledge & Ideas' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Reading - Vocabulary Use' WHERE OBJECTIVE_NAME = 'Reading Vocabulary Use' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Writing/Language - Text Types and Purposes' WHERE OBJECTIVE_NAME = 'Writing/Language Text Types and Purposes' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Writing/Language - Research' WHERE OBJECTIVE_NAME = 'Writing/Language Research' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Writing/Language - Language Conventions' WHERE OBJECTIVE_NAME = 'Writing/Language Language Conventions' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_NAME = 'Listening' WHERE OBJECTIVE_NAME = 'Writing/Language Listening' AND PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Wisconsin Forward Exam');
COMMIT;

/*UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can read closely and analytically to comprehend a range of increasingly complex literary and informational texts.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Reading');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can produce effective and well-grounded writing for a range of purposes and audiences.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Writing');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can employ effective speaking and listening skills for a range of purposes and audiences.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Listening');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can engage in research and inquiry to investigate topics, and to analyze, integrate, and present information.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Research');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can explain and apply mathematical concepts and carry out mathematical procedures with precision and fluency.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Concepts and Procedures');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can solve a range of complex, well-posed problems in pure and applied mathematics, making productive use of knowledge and problem-solving strategies. Students can analyze complex, real-world scenarios and can construct and use mathematical models to interpret and solve problems.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Prob. Solving & Modeling & Data Anal.');
UPDATE OBJECTIVE_DIM SET OBJECTIVE_DESC = q'[Students can clearly and precisely construct viable arguments to support their own reasoning and to critique the reasoning of others.]'
WHERE PROJECTID = (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME = 'Missouri')
AND TRIM(OBJECTIVE_NAME) = TRIM('Communicating Reasoning');*/
--SELECT * FROM OBJECTIVE_DIM;      
 
--GRADE_DIM;
INSERT INTO GRADE_DIM
SELECT (SELECT MAX(GRADEID) FROM GRADE_DIM)+ ROWNUM AS GRADEID,
       'Grade '||TO_CHAR(LEVEL+2) AS GRADE_NAME,
       LEVEL AS GRADE_SEQ,
       '0'||TO_CHAR(LEVEL+2) AS GRADE_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM DUAL CONNECT BY LEVEL <= 6;
COMMIT;
INSERT INTO GRADE_DIM 
     SELECT (SELECT MAX(GRADEID) FROM GRADE_DIM)+ ROWNUM AS GRADEID,
       'Grade 10' AS GRADE_NAME,
       (SELECT MAX(GRADE_SEQ) FROM GRADE_DIM 
               WHERE PROJECTID = (SELECT PROJECTID 
                                    FROM PROJECT_DIM 
                                    WHERE PROJECT_NAME='Wisconsin Forward Exam')  
                                    )+ ROWNUM AS GRADE_SEQ,
        '10' AS GRADE_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP  
    FROM DUAL;
 COMMIT;                               
--SELECT * FROM GRADE_DIM;

--LEVEL_DIM;
INSERT INTO LEVEL_DIM
     SELECT (SELECT MAX(LEVELID) FROM LEVEL_DIM)+ROWNUM AS LEVELID,
       'Level '||(LEVEL+2) AS LEVEL_NAME,
       'WI' AS LEVEL_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
       FROM DUAL CONNECT BY LEVEL<=6;
      
       COMMIT;
       
  INSERT INTO LEVEL_DIM 
     SELECT (SELECT MAX(LEVELID) FROM LEVEL_DIM)+ ROWNUM AS LEVELID,
       'Level 10' AS LEVEL_NAME,
        'WI' AS LEVEL_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP  
    FROM DUAL;
 COMMIT;      
       
--SELECT * FROM LEVEL_DIM; 

--FORM_DIM
INSERT INTO FORM_DIM
SELECT (SELECT MAX(FORMID) FROM FORM_DIM) + ROWNUM AS FORMID,
       'UNKNOWN' AS FORM_NAME,
       'UN' AS FORM_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM DUAL;       
COMMIT;
/*INSERT INTO FORM_DIM
SELECT (SELECT MAX(FORMID) FROM FORM_DIM) + ROWNUM AS FORMID,
       'Z' AS FORM_NAME,
       'Z' AS FORM_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Missouri') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM DUAL;       
COMMIT;*/
--SELECT * FROM FORM_DIM;


--GENDER_DIM
INSERT INTO GENDER_DIM
SELECT (SELECT MAX(GENDERID) FROM GENDER_DIM)+ROWNUM AS GENDERID,
       GND.GENDER_NAME,
       GND.GENDER_CODE,
       (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Wisconsin Forward Exam') AS PROJECTID,
       SYSDATE AS DATETIMESTAMP
FROM (SELECT 'Male' AS GENDER_NAME , 'M' AS GENDER_CODE FROM dual
     UNION
     SELECT 'Female' AS GENDER_NAME , 'F' AS GENDER_CODE FROM dual)GND;
 
COMMIT; 

/*INSERT INTO GENDER_DIM 
VALUES 
(
  (SELECT MAX(GENDERID)+1 FROM GENDER_DIM), 
  'Unknown', 
  'U', 
  (SELECT PROJECTID FROM PROJECT_DIM WHERE PROJECT_NAME='Missouri'), 
  SYSDATE
  );  
COMMIT;    */
--SELECT * FROM GENDER_DIM;

--LEVEL_MAP
INSERT INTO LEVEL_MAP
SELECT (SELECT MAX(LEVEL_MAPID) FROM LEVEL_MAP) + ROWNUM AS LEVEL_MAPID,
       A.LEVELID,
       A.FORMID,
       A.ASSESSMENTID,
       A.DATETIMESTAMP
FROM        
(SELECT 
       LVL.LEVELID,
       FRM.FORMID,
       ASES.ASSESSMENTID,
       SYSDATE AS DATETIMESTAMP
FROM LEVEL_DIM LVL,
     FORM_DIM FRM,
     ASSESSMENT_DIM ASES,
     PRODUCT PDT,
     PROJECT_DIM PJT
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND PJT.PROJECTID = PDT.PROJECTID
  AND ASES.PRODUCTID = PDT.PRODUCTID
  AND FRM.PROJECTID = PJT.PROJECTID
  AND LVL.PROJECTID = PJT.PROJECTID
  ORDER BY LVL.LEVELID)A;
 
COMMIT; 
---SELECT * FROM LEVEL_MAP; 
   
--GRADE_LEVEL_MAP
INSERT INTO GRADE_LEVEL_MAP
                SELECT LVL_GRD_MAP.GRADEID,
                       LMP.LEVEL_MAPID,
                       'Y' ONLEVEL_FLAG,
                       SYSDATE AS DATETIMESTAMP
                FROM FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                    (SELECT GRADEID,LEVELID,GRD.PROJECTID  
                        FROM ( SELECT GRADEID ,GRADE_SEQ,PROJECTID FROM GRADE_DIM WHERE PROJECTID = 4)GRD,
                             (SELECT LEVELID ,ROWNUM AS LEVEL_SEQ FROM LEVEL_DIM WHERE  PROJECTID = 4 ORDER BY LEVEL_NAME)LVL
                        WHERE  GRD.GRADE_SEQ =  LVL.LEVEL_SEQ) LVL_GRD_MAP
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL_GRD_MAP.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL_GRD_MAP.LEVELID
                  AND LMP.FORMID = FRM.FORMID;
COMMIT;
--SELECT * FROM GRADE_LEVEL_MAP;   


--SUBTEST_OBJECTIVE_MAP
--DROP TABLE STG_WI_SUBTEST_OBJECTIVE_MAP
CREATE TABLE STG_WI_SUBTEST_OBJECTIVE_MAP
 AS 
 (select 'Reading - Key Ideas and Details' AS NME,          '01' AS CODE,  '01' AS TYP,     '01' AS SUBTEST_CODE,  'A' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Reading - Craft & Structure/Integration of Knowledge & Ideas' AS NME,          '02' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Reading - Vocabulary Use' AS NME,          '03' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language - Text Types and Purposes' AS NME,          '04' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language - Research' AS NME,      '05' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Writing/Language - Language Conventions' AS NME,    '06' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'F' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Listening' AS NME,      '07' AS CODE,  '01' AS TYP,  '01' AS SUBTEST_CODE,  'G' AS DESCRIPTION FROM DUAL
      UNION ALL
	  ---
      select 'Operations and Algebraic Thinking' AS NME,        '01' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'A' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Number and Operations in Base Ten' AS NME,        '02' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Number and Operations - Fractions' AS NME,        '03' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Measurement and Data' AS NME,          '04' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Ratios and Proportional Relationships' AS NME,          '05' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'F' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'The Number System' AS NME,        '06' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'G' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Expressions and Equations' AS NME,  '07' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'H' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Functions' AS NME,          '08' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'J' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Geometry' AS NME,        '09' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Statistics and Probability' AS NME,        '10' AS CODE,  '02' AS TYP,  '02' AS SUBTEST_CODE,  'I' AS DESCRIPTION FROM DUAL
      UNION ALL
      ---
	  select 'Science Connections & Nature of Science' AS NME,          '01' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'A/B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Science Inquiry' AS NME,        '02' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Physical Science' AS NME,  '03' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Earth and Space Science' AS NME,        '04' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Life & Environmental Science' AS NME,          '05' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'F' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Science Applications and Social Perspectives' AS NME,        '06' AS CODE,  '03' AS TYP,  '03' AS SUBTEST_CODE,  'G/H' AS DESCRIPTION FROM DUAL
	  UNION ALL
	  ---	  
      select 'Geography' AS NME,        '01' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'A' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'History' AS NME,  '02' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'B' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Political Science and Citizenship' AS NME,        '03' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'C' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'Economics' AS NME,          '04' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'D' AS DESCRIPTION FROM DUAL
      UNION ALL
      select 'The Behavioral Sciences' AS NME,        '05' AS CODE,  '04' AS TYP,  '04' AS SUBTEST_CODE,  'E' AS DESCRIPTION FROM DUAL
      ) ORDER BY SUBTEST_CODE,CODE;



 --SUBTEST_OBJECTIVE_MAP (RUN BOTH THE INSERT QUERIES IN THE GIVEN ORDER)
 --SELECT * FROM SUBTEST_OBJECTIVE_MAP
          --FOR ELA
            INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '01'
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE NOT IN ('10')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                  
                  COMMIT;
                  
            --FOR MATH INSERT1  
                 INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '02'
                  AND STG_OBJ.CODE  IN ('01','02','03','04','09')
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE  IN ('03','04','05')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
              COMMIT;
              
              /*--FOR MATH INSERT2
               INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '02'
                  AND STG_OBJ.CODE  IN ('05')
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE  NOT IN ('10')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                        
             COMMIT;*/
             
             --FOR MATH INSERT3
               INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '02'
                  AND STG_OBJ.CODE  IN ('05','06','07','09','10')
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE  IN ('06','07')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                        
             COMMIT;
            /*
            --FOR MATH INSERT4
               INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '02'
                  AND STG_OBJ.CODE  IN ('07','08','09')
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE  IN ('06','07','08')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                        
             COMMIT;*/
             
             --FOR MATH INSERT5
               INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '02'
                  AND STG_OBJ.CODE  IN ('06','07','08','09','10')
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE  IN ('08')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                        
             COMMIT; 
             
             --FOR SCI
            INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '03'
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE IN ('04','08')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                  
                  COMMIT;
             
             
              --FOR SS
            INSERT INTO SUBTEST_OBJECTIVE_MAP 
                 SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP)+ROWNUM AS SUBT_OBJ_MAPID,
                        A.SUBTESTID,
                        A.OBJECTIVEID,
                        A.LEVEL_MAPID,
                        A.ASSESSMENTID ,
                        A.DATETIMESTAMP
                 FROM (SELECT SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID,
                        SYSDATE AS DATETIMESTAMP
                 FROM LEVEL_DIM LVL,
                     FORM_DIM FRM,
                     PROJECT_DIM PJT,
                     LEVEL_MAP LMP,
                     GRADE_LEVEL_MAP GLM,
                     ASSESSMENT_DIM ASES,
                     PRODUCT PDT,
                     STG_WI_SUBTEST_OBJECTIVE_MAP STG_OBJ,
                     SUBTEST_DIM SUB,
                     OBJECTIVE_DIM OBJ,
                     CONTENT_DIM CONT,
                     GRADE_DIM GRD
                WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                  AND PJT.PROJECTID = PDT.PROJECTID
                  AND ASES.PRODUCTID = PDT.PRODUCTID
                  AND FRM.PROJECTID = PJT.PROJECTID
                  AND LVL.PROJECTID = PJT.PROJECTID 
                  AND LMP.ASSESSMENTID = ASES.ASSESSMENTID  
                  AND LMP.LEVELID = LVL.LEVELID
                  AND LMP.FORMID = FRM.FORMID
                  AND CONT.ASSESSMENTID =ASES.ASSESSMENTID 
                  AND CONT.CONTENTID = SUB.CONTENTID
                  AND SUB.SUBTEST_CODE = STG_OBJ.SUBTEST_CODE
                  AND STG_OBJ.NME = OBJ.OBJECTIVE_NAME
                  AND STG_OBJ.TYP = OBJ.OBJECTIVE_TYPE
                  AND OBJ.PROJECTID = PJT.PROJECTID
                  AND GLM.LEVEL_MAPID = LMP.LEVEL_MAPID
                  AND STG_OBJ.SUBTEST_CODE  = '04'
                  AND GRD.PROJECTID = PJT.PROJECTID 
                  AND GRD.GRADEID = GLM.GRADEID
                  AND GRD.GRADE_CODE IN ('04','08','10')
                  ORDER BY SUB.SUBTESTID,
                        OBJ.OBJECTIVEID,
                        GLM.LEVEL_MAPID,
                        ASES.ASSESSMENTID)A;
                  
                  COMMIT;
  ---SELECT * FROM SUBTEST_OBJECTIVE_MAP WHERE TRUNC(DATETIMESTAMP) = TRUNC(SYSDATE) 
  ---SELECT * FROM PROJECT_DIM
  ---delete FROM SUBTEST_OBJECTIVE_MAP  WHERE OBJECTIVEID IN (SELECT OBJECTIVEID FROM OBJECTIVE_DIM WHERE PROJECTID = 4)           
             
-- ITEMSET_DIM             
ALTER TABLE ITEMSET_DIM MODIFY ITEM_NAME VARCHAR2(50);
ALTER TABLE ITEMSET_DIM DROP CONSTRAINT CHK_ITEM_TYPE;
ALTER TABLE ITEMSET_DIM  ADD CONSTRAINT CHK_ITEM_TYPE CHECK (ITEM_TYPE IN ('SR', 'CR', 'GR','OBJ','TE','MC1','AS1','TD1','MC2','AS2','TD2'));             
--DELETE FROM ITEMSET_DIM WHERE PROJECTID = 4; 
COMMIT;

SELECT MAX (ITEMSETID) FROM ITEMSET_DIM;
DROP SEQUENCE SEQ_ITEMSETID;

CREATE SEQUENCE SEQ_ITEMSETID
MINVALUE 1
MAXVALUE 9999999999999999999999999999
START WITH 11048
INCREMENT BY 1
NOCACHE;


      ----CHANGE THE SUBTEST ID'S AND PROJECTID BEFORE INSERT
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- ELA', '1001', null, 1, null, 'MC1', null, null, 2066, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Autoscored- ELA', '1002', null, 2, null, 'AS1', null, null, 2066, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Text-Dependent Analysis - ELA', '1003', null, 3, null, 'TD1', null, null, 2066, null, null, null, 4, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- Math', '1004', null, 1, null, 'MC1', null, null, 2067, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Autoscored- Math', '1005', null, 2, null, 'AS1', null, null, 2067, null, null, null, 4, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- Science', '1006', null, 1, null, 'MC1', null, null, 2068, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned Autoscored- Science', '1007', null, 2, null, 'AS1', null, null, 2068, null, null, null, 4, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Points Earned MC- Social Study', '1008', null, 1, null, 'MC1', null, null, 2069, null, null, null, 4, SYSDATE);
---
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC- ELA', '1009', null, 4, null, 'MC2', null, null, 2066, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Autoscored- ELA', '1010', null, 5, null, 'AS2', null, null, 2066, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Text-Dependent Analysis - ELA', '1011', null, 6, null, 'TD2', null, null, 2066, null, null, null, 4, SYSDATE);

---
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC - Math', '1012', null, 3, null, 'MC2', null, null, 2067, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Autoscored - Math', '1013', null, 4, null, 'AS2', null, null, 2067, null, null, null, 4, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC - Science', '1014', null, 3, null, 'MC2', null, null, 2068, null, null, null, 4, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to Autoscored - Science', '1015', null, 4, null, 'AS2', null, null, 2068, null, null, null, 4, SYSDATE);
--
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (SEQ_ITEMSETID.NEXTVAL, 'Answers to MC - Social Study', '1016', null, 2, null, 'MC2', null, null, 2069, null, null, null, 4, SYSDATE);
commit;

--SELECT * FROM ITEMSET_DIM WHERE TRUNC(DATETIMESTAMP) = TRUNC(SYSDATE)

---SCORE_TYPE_LOOKUP
--SELECT * FROM SCORE_TYPE_LOOKUP
ALTER TABLE SCORE_TYPE_LOOKUP MODIFY PL_CODE VARCHAR2(5);
DECLARE
V_CUST_PROD_ID NUMBER;
BEGIN 
  SELECT CUST.CUST_PROD_ID INTO V_CUST_PROD_ID
FROM CUSTOMER_INFO CI,
     PRODUCT PDT,
     ADMIN_DIM ADM,
     PROJECT_DIM PJT,
     CUST_PRODUCT_LINK CUST
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND PJT.PROJECTID = PDT.PROJECTID
  AND CI.PROJECTID = PDT.PROJECTID 
  AND ADM.PROJECTID = PDT.PROJECTID
  AND CUST.CUSTOMERID = CI.CUSTOMERID
  AND CUST.ADMINID = ADM.ADMINID;
  
INSERT INTO SCORE_TYPE_LOOKUP VALUES (SEQ_SCORE_LKP_ID.NEXTVAL,'SUBTEST','SUB_ACHV_LVL',1,'Below Basic',V_CUST_PROD_ID,SYSDATE,SYSDATE,'BB');  
INSERT INTO SCORE_TYPE_LOOKUP VALUES (SEQ_SCORE_LKP_ID.NEXTVAL,'SUBTEST','SUB_ACHV_LVL',2,'Basic',V_CUST_PROD_ID,SYSDATE,SYSDATE,'Basic'); 
INSERT INTO SCORE_TYPE_LOOKUP VALUES (SEQ_SCORE_LKP_ID.NEXTVAL,'SUBTEST','SUB_ACHV_LVL',3,'Proficient',V_CUST_PROD_ID,SYSDATE,SYSDATE,'Prof'); 
INSERT INTO SCORE_TYPE_LOOKUP VALUES (SEQ_SCORE_LKP_ID.NEXTVAL,'SUBTEST','SUB_ACHV_LVL',4,'Advanced',V_CUST_PROD_ID,SYSDATE,SYSDATE,'Adv'); 
INSERT INTO SCORE_TYPE_LOOKUP VALUES (SEQ_SCORE_LKP_ID.NEXTVAL,'SUBTEST','SUB_ACHV_LVL',-1,'Not Tested',V_CUST_PROD_ID,SYSDATE,SYSDATE,'Blank'); 
  
COMMIT;  
END;

--UPDATE SCORE_TYPE_LOOKUP SET SCORE_VALUE_NAME = TRIM(SCORE_VALUE_NAME) WHERE SCORE_VALUE_NAME = ' Below Basic'
/**************************** START: IN THE WISCONSIN SCHEMA*********************************************

--ORG_NODE_DIM
ALTER TABLE ORG_NODE_DIM DROP CONSTRAINT CHK_ORG_MODE;
ALTER TABLE ORG_NODE_DIM ADD CONSTRAINT CHK_ORG_MODE CHECK (ORG_MODE IN ('OL','PP','BO','PUBLIC', 'NON-PUBLIC','1','2','3'));

--STUDENT_BIO_DIM
ALTER TABLE STUDENT_BIO_DIM DROP CONSTRAINT CHK_STUDENT_MODE;
ALTER TABLE STUDENT_BIO_DIM ADD CONSTRAINT CHK_STUDENT_MODE CHECK (STUDENT_MODE IN ('OL','PP','1','2','3'));

/**************************** END: IN THE WISCONSIN SCHEMA*********************************************/


-- SCORE_TYPE_LOOKUP             
/*INSERT INTO SCORE_TYPE_LOOKUP
SELECT  SEQ_SCORE_LKP_ID.NEXTVAL AS SCORE_TYPEID,
        A.CATEGORY,
        A.SCORE_TYPE,
        A.SCORE_VALUE,
        A.SCORE_VALUE_NAME,    
        B.CUST_PROD_ID,
        SYSDATE AS CREATED_DATE_TIME,
        SYSDATE AS UPDATED_DATE_TIME
 FROM       
(SELECT 'SUBTEST' AS CATEGORY, 'SUB_ACHV_LVL' AS SCORE_TYPE, '0' AS SCORE_VALUE  ,  'Level Not Determined' AS SCORE_VALUE_NAME FROM DUAL
UNION     
SELECT 'SUBTEST' AS CATEGORY, 'SUB_ACHV_LVL' AS SCORE_TYPE, '1' AS SCORE_VALUE  ,  'Below Basic' AS SCORE_VALUE_NAME FROM DUAL  
UNION
SELECT 'SUBTEST' AS CATEGORY, 'SUB_ACHV_LVL' AS SCORE_TYPE, '2' AS SCORE_VALUE  ,  'Basic' AS SCORE_VALUE_NAME FROM DUAL
UNION
SELECT 'SUBTEST' AS CATEGORY, 'SUB_ACHV_LVL' AS SCORE_TYPE, '3' AS SCORE_VALUE  ,  'Proficient' AS SCORE_VALUE_NAME FROM DUAL
UNION
SELECT 'SUBTEST' AS CATEGORY, 'SUB_ACHV_LVL' AS SCORE_TYPE, '4' AS SCORE_VALUE  ,  'Advanced' AS SCORE_VALUE_NAME FROM DUAL
UNION
SELECT 'OBJECTIVE' AS CATEGORY, 'OBJ_ACHV_LVL' AS SCORE_TYPE, '0' AS SCORE_VALUE  ,  'Level Not Determined' AS SCORE_VALUE_NAME FROM DUAL
UNION
SELECT 'OBJECTIVE' AS CATEGORY, 'OBJ_ACHV_LVL' AS SCORE_TYPE, '1' AS SCORE_VALUE  ,  'Below Standard' AS SCORE_VALUE_NAME FROM DUAL
UNION
SELECT 'OBJECTIVE' AS CATEGORY, 'OBJ_ACHV_LVL' AS SCORE_TYPE, '2' AS SCORE_VALUE  ,  'At/Near Standard' AS SCORE_VALUE_NAME FROM DUAL
UNION
SELECT 'OBJECTIVE' AS CATEGORY, 'OBJ_ACHV_LVL' AS SCORE_TYPE, '3' AS SCORE_VALUE  ,  'Above Standard' AS SCORE_VALUE_NAME FROM DUAL)A,
(SELECT CUST_PROD_ID FROM CUST_PRODUCT_LINK WHERE CUSTOMERID = (SELECT CUSTOMERID FROM CUSTOMER_INFO WHERE CUSTOMER_NAME='Missouri  MAP – Test'))B;
  
 --SELECT * FROM SCORE_TYPE_LOOKUP;
 COMMIT; */



/********* LIST OF TABLES POPULATED IN ORDER**********
PROJECT_DIM
CUSTOMER_INFO
ADMIN_DIM
PRODUCT
CUST_PRODUCT_LINK
ASSESSMENT_DIM
TEST_PROGRAM
ORG_TP_STRUCTURE
CONTENT_DIM
SUBTEST_DIM
OBJECTIVE_DIM
GRADE_DIM
LEVEL_DIM
FORM_DIM
GENDER_DIM
LEVEL_MAP
GRADE_LEVEL_MAP
SUBTEST_OBJECTIVE_MAP
SCORE_TYPE_LOOKUP
*************************************************/

/*******
1.ON GLOBAL SCHEMA RUN 5_GLOBAL_GRANT_MO.sql TO GIVE GRANT TO MISSOURI DB

*******/
