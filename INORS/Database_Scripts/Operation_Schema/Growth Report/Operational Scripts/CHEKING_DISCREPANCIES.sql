 CREATE TABLE TEST_STUDENT_PL_UDTR AS 
 SELECT CLS.STUDENT_NAME,
        CLS.CURR_STUDENT_BIO_ID AS GRW_CURR_STUDENT_BIO_ID,
        CLS.PREV_STUDENT_BIO_ID AS GRW_PREV_STUDENT_BIO_ID,
        CLS.CURR_GRADEID AS GRW_CURR_GRADEID,
        SUB.SUBTEST_NAME,
        (SELECT P_F_INDICATOR FROM GRW_SUBTEST_SCORE_FACT@MIG_TO_PNPROD WHERE STUDENT_BIO_ID =CLS.CURR_STUDENT_BIO_ID AND SUBTESTID = CLS.SUBTESTID) AS GRW_CURR_PERF,
        (SELECT P_F_INDICATOR FROM GRW_SUBTEST_SCORE_FACT@MIG_TO_PNPROD WHERE STUDENT_BIO_ID =CLS.PREV_STUDENT_BIO_ID AND SUBTESTID = CLS.SUBTESTID) AS GRW_PREV_PERF,
        (SELECT SCR.PL FROM SUBTEST_SCORE_FACT SCR,RESULTS_GRT_FACT GRT,GRW_STUDENT_BIO_DIM@MIG_TO_PNPROD STD
                 WHERE GRT.ELEMENT_NUMBER = STD.STRUCTURE_ELEMENT
                 AND STD.STUDENT_BIO_ID =CLS.CURR_STUDENT_BIO_ID
                 AND GRT.CUST_PROD_ID = 5001
                 AND SCR.CUST_PROD_ID = GRT.CUST_PROD_ID
                 AND SCR.STUDENT_BIO_ID = GRT.STUDENT_BIO_ID
                 AND SCR.GRADEID = GRT.GRADEID
                 AND SCR.SUBTESTID IN (SELECT SUBTESTID FROM SUBTEST_DIM WHERE SUBTEST_NAME =SUB.SUBTEST_NAME ) ) AS CURR_PERF,
        (SELECT SCR.PL FROM SUBTEST_SCORE_FACT SCR,RESULTS_GRT_FACT GRT,GRW_STUDENT_BIO_DIM@MIG_TO_PNPROD STD
                 WHERE GRT.ELEMENT_NUMBER = STD.STRUCTURE_ELEMENT
                 AND STD.STUDENT_BIO_ID =CLS.PREV_STUDENT_BIO_ID
                 AND GRT.CUST_PROD_ID = 5005
                 AND SCR.CUST_PROD_ID = GRT.CUST_PROD_ID
                 AND SCR.STUDENT_BIO_ID = GRT.STUDENT_BIO_ID
                 AND SCR.GRADEID = GRT.GRADEID
                 AND SCR.SUBTESTID IN (SELECT SUBTESTID FROM SUBTEST_DIM WHERE SUBTEST_NAME =SUB.SUBTEST_NAME ) ) AS PREV_PERF
          
 FROM  GRW_CLS_MATRIX@MIG_TO_PNPROD CLS , GRW_STUDENT_BIO_DIM@MIG_TO_PNPROD STD , GRW_SUBTEST_DIM@MIG_TO_PNPROD SUB
  WHERE CLS.CURR_STUDENT_BIO_ID = STD.STUDENT_BIO_ID 
    AND CLS.SUBTESTID = SUB.SUBTESTID
    AND NOT EXISTS (SELECT 1 FROM PERF_MATRIX_FACT MAT , RESULTS_GRT_FACT GRT , USERS USR
                      WHERE  MAT.CURR_STUDENT_BIO_ID = GRT.STUDENT_BIO_ID   
                        AND  GRT.ELEMENT_NUMBER = STD.STRUCTURE_ELEMENT
                        AND  MAT.USER_ID = USR.USERID
                        AND  UPPER(CLS.USERNAME) =  UPPER(USR.USERNAME) 
                        AND  GRT.CUST_PROD_ID = 5001
                      ) 
    ORDER BY CLS.STUDENT_NAME,CLS.CURR_GRADEID,SUB.SUBTEST_NAME  ;
    
                    
         
    CREATE TABLE grw_1 AS                
  SELECT DISTINCT STUDENT_NAME,
        -- username , 
         CURR_STUDENT_BIO_ID,
         STRUCTURE_ELEMENT,
         SC_FACT.SUBTESTID,         
         GSUBMAP.PRISM_SUBTESTID , 
          SC_FACT.sc , 
         p_f_indicator AS grw_pl_level 
    FROM GRW_CLS_MATRIX@MIG_TO_PNPROD         A,
         GRW_STUDENT_BIO_DIM@MIG_TO_PNPROD    STD,
         GRW_SUBTEST_SCORE_FACT@MIG_TO_PNPROD SC_FACT,
         GRW_SUBTEST_MAP                      GSUBMAP
   WHERE A.CURR_STUDENT_BIO_ID = STD.STUDENT_BIO_ID
     AND STD.STUDENT_BIO_ID = SC_FACT.STUDENT_BIO_ID
    AND  SC_FACT.adminid = 103  
     AND SC_FACT.SUBTESTID = GRW_SUBTESTID
     AND GRW_ADMINID = SC_FACT.adminid 
     AND grw_adminid = 103  ;
     
     CREATE TABLE pl_mismatch AS 
     SELECT STUDENT_NAME,
             a.SUBTESTID,         
             PRISM_SUBTESTID , 
          sc AS grw_scale_score , 
          grw_pl_level , 
          ss AS prism_scale_score, 
          b.pl AS prism_pl_level
      FROM grw_1 a , results_grt_fact r_fact , subtest_score_fact b 
     WHERE a.structure_element = r_fact.element_number 
     AND r_fact.cust_prod_id = 5001 
     AND r_fact.cust_prod_id = b.cust_prod_id 
     AND r_fact.student_bio_id = b.student_bio_id 
     AND a.prism_subtestid = b.subtestid 
     AND grw_pl_level <> b.pl  ;

          
          
          
          