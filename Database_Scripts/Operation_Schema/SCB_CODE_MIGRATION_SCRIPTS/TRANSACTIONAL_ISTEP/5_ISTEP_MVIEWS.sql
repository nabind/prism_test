--MV_LVL2_PRCPL_USER_SEL_LOOKUP

CREATE MATERIALIZED VIEW MV_LVL2_PRCPL_USER_SEL_LOOKUP
REFRESH COMPLETE ON DEMAND
AS
SELECT USR.CUSTOMERID ,
                   ORG.PARENT_ORG_NODEID AS DISTRICT_ORG_NODEID,
                   ORGUSR.USERID AS DISTRICT_PRINCIPAL_USERID,
                   ORG.ORG_NODEID AS SCHOOL_ORG_NODEID,
                   USR.USERID  AS NORMAL_GRW_USERID,
                   SUBSTR(USR.USERNAME,1,8)  AS NORMAL_GRW_USER_SPN,
                   USL.CUST_PROD_ID,
                   USL.GRADEID,
                   USL.SUBTESTID
              FROM ORG_USERS OUSR, USERS USR,USER_SELECTION_LOOKUP USL,ORG_NODE_DIM ORG,
                 (SELECT USR.USERID,OUSR.ORG_NODEID
                  FROM USERS USR,
                     ORG_USERS OUSR
                  WHERE USER_TYPE = 'GRW_P'
                  AND USR.USERID = OUSR.USERID
                  AND OUSR.ORG_NODE_LEVEL = 2)ORGUSR
              WHERE ORG.PARENT_ORG_NODEID  = ORGUSR.ORG_NODEID
                AND ORG.ORG_NODEID = OUSR.ORG_NODEID
                AND OUSR.ORG_NODE_LEVEL =3
                AND OUSR.USERID = USR.USERID
                AND USR.USER_TYPE = 'GRW'
                AND OUSR.USERID = USL.USERID;
 
--MV_PRINCIPAL_USER_SEL_LOOKUP
                
CREATE MATERIALIZED VIEW MV_PRINCIPAL_USER_SEL_LOOKUP
REFRESH COMPLETE ON DEMAND
AS
SELECT USR.CUSTOMERID ,
                   ORG.ORG_NODEID,
                   ORG.USERID AS PRINCIPAL_USERID,
                   USR.USERID  AS NORMAL_GRW_USERID,
                   SUBSTR(USR.USERNAME,1,8)  AS NORMAL_GRW_USER_SPN,
                   USL.CUST_PROD_ID,
                   USL.GRADEID,
                   USL.SUBTESTID
              FROM ORG_USERS OUSR, USERS USR,USER_SELECTION_LOOKUP USL,
                 (SELECT USR.USERID,OUSR.ORG_NODEID
                  FROM USERS USR,
                     ORG_USERS OUSR
                  WHERE USER_TYPE = 'GRW_P'
                  AND USR.USERID = OUSR.USERID
                  AND OUSR.ORG_NODE_LEVEL = 3)ORG
              WHERE OUSR.ORG_NODEID = ORG.ORG_NODEID
                AND OUSR.USERID = USR.USERID
                AND USR.USER_TYPE = 'GRW'
                AND OUSR.USERID = USL.USERID;
                

--UDTR_ROSTER_TEST_SESSION_CNT
                
CREATE MATERIALIZED VIEW UDTR_ROSTER_TEST_SESSION_CNT
REFRESH COMPLETE ON DEMAND
AS
SELECT 1 AS STUDENT_DETAILS,
       UDTR.ORG_NODEID,
       UDTR.GRADEID,
       UDTR.CUST_PROD_ID,
       UDTR.ADMINID ,
       UDTR.ISPUBLIC,
       COUNT(UDTR.ELA1_TEST_SESSION_NAME) AS ELA1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA2_TEST_SESSION_NAME) AS ELA2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA3_TEST_SESSION_NAME) AS ELA3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA4_TEST_SESSION_NAME) AS ELA4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA5_TEST_SESSION_NAME) AS ELA5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA6_TEST_SESSION_NAME) AS ELA6_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH1_TEST_SESSION_NAME) AS MATH1_TEST_SESSION_NAME_CNT ,
       COUNT(UDTR.MATH2_TEST_SESSION_NAME) AS MATH2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH3_TEST_SESSION_NAME) AS MATH3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH4_TEST_SESSION_NAME) AS MATH4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH5_TEST_SESSION_NAME) AS MATH5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE1_TEST_SESSION_NAME) AS SCIENCE1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE2_TEST_SESSION_NAME) AS SCIENCE2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE3_TEST_SESSION_NAME) AS SCIENCE3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE4_TEST_SESSION_NAME) AS SCIENCE4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL1_TEST_SESSION_NAME) AS SOCIAL1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL2_TEST_SESSION_NAME) AS SOCIAL2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL3_TEST_SESSION_NAME) AS SOCIAL3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL4_TEST_SESSION_NAME) AS SOCIAL4_TEST_SESSION_NAME_CNT,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA2_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA3_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA4_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA5_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA6_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH1_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH2_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH3_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH4_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH5_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE1_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE2_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE3_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE4_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL1_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL2_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL3_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL4_TESTRESULT = 'Test Not Taken'
             THEN 1
             ELSE 0
          END) AS TEST_NOT_TAKEN ,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA2_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA3_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA4_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA5_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA6_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH1_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH2_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH3_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH4_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH5_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE1_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE2_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE3_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE4_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL1_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL2_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL3_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL4_TESTRESULT = 'Test Not Received'
             THEN 1
             ELSE 0
          END) AS TEST_NOT_RECEIVED,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA2_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA3_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA4_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA5_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA6_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH1_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH2_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH3_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH4_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH5_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE1_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE2_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE3_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE4_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL1_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL2_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL3_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL4_TESTRESULT = 'Valid Attempt'
             THEN 1
             ELSE 0
          END) AS VALID_ATTEMPT,
       SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA2_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA3_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA4_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA5_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA6_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH1_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH2_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH3_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH4_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH5_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE1_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE2_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE3_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE4_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL1_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL2_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL3_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL4_TESTRESULT = 'Invalid by School'
             THEN 1
             ELSE 0
          END) AS INVALID_BY_SCHOOL ,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA4_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA5_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA6_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH4_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH5_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE4_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL4_TESTRESULT = 'IMAST Partic.'
             THEN 1
             ELSE 0
          END) AS IMAST_PARTIC
FROM UDTR_ROSTER_FACT UDTR,
     CUST_PRODUCT_LINK CUST
WHERE  UDTR.CUST_PROD_ID = CUST.CUST_PROD_ID
  AND UDTR.ADMINID = CUST.ADMINID
GROUP BY UDTR.ORG_NODEID,
         UDTR.GRADEID,
         UDTR.CUST_PROD_ID,
         UDTR.ADMINID ,
         UDTR.ISPUBLIC
UNION
SELECT  0 AS STUDENT_DETAILS,
       UDTR.ORG_NODEID,
       UDTR.GRADEID,
       UDTR.CUST_PROD_ID,
       UDTR.ADMINID ,
       UDTR.ISPUBLIC,
       COUNT(UDTR.ELA1_TEST_SESSION_NAME) AS ELA1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA2_TEST_SESSION_NAME) AS ELA2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA3_TEST_SESSION_NAME) AS ELA3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA4_TEST_SESSION_NAME) AS ELA4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA5_TEST_SESSION_NAME) AS ELA5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA6_TEST_SESSION_NAME) AS ELA6_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH1_TEST_SESSION_NAME) AS MATH1_TEST_SESSION_NAME_CNT ,
       COUNT(UDTR.MATH2_TEST_SESSION_NAME) AS MATH2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH3_TEST_SESSION_NAME) AS MATH3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH4_TEST_SESSION_NAME) AS MATH4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH5_TEST_SESSION_NAME) AS MATH5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE1_TEST_SESSION_NAME) AS SCIENCE1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE2_TEST_SESSION_NAME) AS SCIENCE2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE3_TEST_SESSION_NAME) AS SCIENCE3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE4_TEST_SESSION_NAME) AS SCIENCE4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL1_TEST_SESSION_NAME) AS SOCIAL1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL2_TEST_SESSION_NAME) AS SOCIAL2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL3_TEST_SESSION_NAME) AS SOCIAL3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL4_TEST_SESSION_NAME) AS SOCIAL4_TEST_SESSION_NAME_CNT  ,
       -1 AS TEST_NOT_TAKEN ,
       -1 AS TEST_NOT_RECEIVED,
       -1 AS VALID_ATTEMPT,
          -1 AS INVALID_BY_SCHOOL ,
          -1 AS IMAST_PARTIC
FROM UDTR_SUMM_FACT UDTR,
     CUST_PRODUCT_LINK CUST
WHERE UDTR.CUST_PROD_ID = CUST.CUST_PROD_ID
  AND UDTR.ADMINID = CUST.ADMINID
  AND NOT EXISTS (SELECT 1 FROM UDTR_ROSTER_FACT UDT
                     WHERE UDT.ORG_NODEID =UDTR.ORG_NODEID
                       AND UDT.GRADEID = UDTR.GRADEID
                       AND UDT.CUST_PROD_ID = UDTR.CUST_PROD_ID
                       AND UDT.ADMINID = UDTR.ADMINID
                       AND UDT.ISPUBLIC =UDTR.ISPUBLIC
                                            )
GROUP BY UDTR.ORG_NODEID,
         UDTR.GRADEID,
         UDTR.CUST_PROD_ID,
         UDTR.ADMINID ,
         UDTR.ISPUBLIC;
         
--VW_SUBTEST_GRADE_OBJECTIVE_MAP
         
CREATE MATERIALIZED VIEW VW_SUBTEST_GRADE_OBJECTIVE_MAP
REFRESH COMPLETE ON DEMAND
AS
SELECT A.ASSESSMENTID,
       A.ASSESSMENT_NAME,
       A.PRODUCTID,
       G.GRADE_CODE,
       G.GRADEID,
       G.GRADE_NAME,
       L.LEVELID,
       L.LEVEL_NAME,
       F.FORMID,
       F.FORM_NAME,
     F.FORM_CODE,
       C.CONTENTID,
       C.CONTENT_NAME,
       C.CONTENT_SEQ,
     S.SUBTESTID,
       S.SUBTEST_NAME,
       S.SUBTEST_SEQ,
     S.SUBTEST_CODE,
       O.OBJECTIVEID,
       O.OBJECTIVE_NAME,
       O.OBJECTIVE_SEQ,
     O.OBJECTIVE_CODE,
       'MASTERY_INDICATOR_'||ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) MAST_IND,
       'OPIIPI_'||ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) OPI_IPI,
       ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) AS rn,
       lm.LEVEL_MAPID,
       som.SUBT_OBJ_MAPID
       
  FROM CONTENT_DIM           C,
       SUBTEST_DIM           S,
       OBJECTIVE_DIM         O,
       SUBTEST_OBJECTIVE_MAP SOM,
       LEVEL_MAP             LM,
       LEVEL_DIM             L,
       FORM_DIM              F,
       GRADE_DIM             G,
       GRADE_LEVEL_MAP       GM,
       ASSESSMENT_DIM        A
 WHERE A.ASSESSMENTID = C.ASSESSMENTID
   AND C.CONTENTID = S.CONTENTID
   AND SOM.SUBTESTID = S.SUBTESTID
   AND SOM.OBJECTIVEID = O.OBJECTIVEID
   AND SOM.LEVEL_MAPID = LM.LEVEL_MAPID
   AND L.LEVELID = LM.LEVELID
   AND F.FORMID = LM.FORMID
   AND G.GRADEID = GM.GRADEID
   AND LM.LEVEL_MAPID = GM.LEVEL_MAPID;