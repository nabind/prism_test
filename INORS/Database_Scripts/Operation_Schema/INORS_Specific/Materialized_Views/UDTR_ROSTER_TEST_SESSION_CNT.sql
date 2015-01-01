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
