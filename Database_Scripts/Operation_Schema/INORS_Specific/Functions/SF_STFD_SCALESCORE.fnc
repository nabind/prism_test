CREATE OR REPLACE FUNCTION SF_STFD_SCALESCORE (     LoggedInUserJasperOrgId IN STFD_FACT.ORG_NODEID%TYPE,
                                               p_test_administration IN PRODUCT.PRODUCTID%TYPE,
                                               p_test_program IN VARCHAR,                                               
                                               p_corpdioceseAll IN STFD_FACT.ORG_NODEID%TYPE,                                                                                              
                                               p_grade IN STFD_FACT.GRADEID%TYPE,
                                               p_subtest1 IN VARCHAR,
                                               p_subtest2 IN VARCHAR,
                                               p_subtest3 IN VARCHAR,
                                               p_subtest4 IN VARCHAR,
                                               p_subtest5 IN VARCHAR,
                                               p_subtest6 IN VARCHAR)
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_STFD_SCALESCORE
  * PURPOSE:   Scalescore query Of Academic Standards Frequency Distribution
  * CREATED:   TCS  20/DEC/2013
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR     DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();


 CURSOR c_Stfd_ScaleScore
  IS
     SELECT DECODE(SUB1.SCALESCORE,
              NULL,
              DECODE(SUB2.SCALESCORE,
                     NULL,
                     DECODE(SUB3.SCALESCORE,
                            NULL,
                            DECODE(SUB4.SCALESCORE,
                                   NULL,
                                   DECODE(SUB5.SCALESCORE,
                                          NULL,
                                          SUB6.SCALESCORE,
                                          SUB5.SCALESCORE),
                                   SUB4.SCALESCORE),
                            SUB3.SCALESCORE),
                     SUB2.SCALESCORE),
              SUB1.SCALESCORE) AS SCALESCORE1,
       DECODE(SUB1.SUBTEST_NAME,
              NULL,
              (SELECT DISTINCT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTESTID = p_subtest1),
              SUB1.SUBTEST_NAME) AS SUB1_SUBTEST_NAME,
       SUB1.FREQUENCY AS SUB1_FREQUENCY,
       SUB1.CUMULATIVEFREQUENCY AS SUB1_CUMULATIVE_FREQUENCY,
       SUB1.PERCENT AS SUB1_PERCENT,
       SUB1.CUMULATIVEPERCENT AS SUB1_CUMULATIVE_PERCENT,
       DECODE(SUB2.SUBTEST_NAME,
              NULL,
              (SELECT DISTINCT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTESTID = p_subtest2),
              SUB2.SUBTEST_NAME) AS SUB2_SUBTEST_NAME,
       SUB2.FREQUENCY SUB2_FREQUENCY,
       SUB2.CUMULATIVEFREQUENCY AS SUB2_CUMULATIVE_FREQUENCY,
       SUB2.PERCENT AS SUB2_PERCENT,
       SUB2.CUMULATIVEPERCENT AS SUB2_CUMULATIVE_PERCENT,
       DECODE(SUB3.SUBTEST_NAME,
              NULL,
              (SELECT DISTINCT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTESTID = p_subtest3),
              SUB3.SUBTEST_NAME) AS SUB3_SUBTEST_NAME,
       SUB3.FREQUENCY AS SUB3_FREQUENCY,
       SUB3.CUMULATIVEFREQUENCY AS SUB3_CUMULATIVE_FREQUENCY,
       SUB3.PERCENT AS SUB3_PERCENT,
       SUB3.CUMULATIVEPERCENT AS SUB3_CUMULATIVE_PERCENT,
       DECODE(SUB4.SUBTEST_NAME,
              NULL,
              (SELECT DISTINCT SUBTEST_NAME FROM SUBTEST_DIM WHERE SUBTESTID = p_subtest4),
              SUB4.SUBTEST_NAME) AS SUB4_SUBTEST_NAME,
       SUB4.FREQUENCY AS SUB4_FREQUENCY,
       SUB4.CUMULATIVEFREQUENCY AS SUB4_CUMULATIVE_FREQUENCY,
       SUB4.PERCENT AS SUB4_PERCENT,
       SUB4.CUMULATIVEPERCENT AS SUB4_CUMULATIVE_PERCENT,
       DECODE(SUB5.SUBTEST_NAME, NULL, '', SUB5.SUBTEST_NAME) AS SUB5_SUBTEST_NAME,
       SUB5.FREQUENCY AS SUB5_FREQUENCY,
       SUB5.CUMULATIVEFREQUENCY AS SUB5_CUMULATIVE_FREQUENCY,
       SUB5.PERCENT AS SUB5_PERCENT,
       SUB5.CUMULATIVEPERCENT AS SUB5_CUMULATIVE_PERCENT,
       DECODE(SUB6.SUBTEST_NAME, NULL, '', SUB6.SUBTEST_NAME) AS SUB6_SUBTEST_NAME,
       SUB6.FREQUENCY AS SUB6_FREQUENCY,
       SUB6.CUMULATIVEFREQUENCY AS SUB6_CUMULATIVE_FREQUENCY,
       SUB6.PERCENT AS SUB6_PERCENT,
       SUB6.CUMULATIVEPERCENT AS SUB6_CUMULATIVE_PERCENT,
       DECODE(SUB1.SCALESCORE,
              NULL,
              DECODE(SUB2.SCALESCORE,
                     NULL,
                     DECODE(SUB3.SCALESCORE,
                            NULL,
                            DECODE(SUB4.SCALESCORE,
                                   NULL,
                                   DECODE(SUB5.SCALESCORE,
                                          NULL,
                                          SUB6.SCALESCORE,
                                          SUB5.SCALESCORE),
                                   SUB4.SCALESCORE),
                            SUB3.SCALESCORE),
                     SUB2.SCALESCORE),
              SUB1.SCALESCORE) AS SCALESCORE2,
       CNT.SUBTEST_CNT
  FROM (SELECT COUNT(DISTINCT SCT.SUBTESTID) AS SUBTEST_CNT
          FROM STFD_FACT             SCT,
               PRODUCT               PDT,
               CUST_PRODUCT_LINK     CPL,
               ORG_PRODUCT_LINK      OPL,
               ORG_TEST_PROGRAM_LINK OLINK,
               TEST_PROGRAM          TP
         WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
           AND PDT.PRODUCTID = p_test_administration
           AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
       AND SCT.GRADEID = p_grade
           AND PDT.PRODUCTID = CPL.PRODUCTID
           AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
           AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
           AND SCT.ORG_NODEID = OPL.ORG_NODEID
           AND OPL.ORG_NODEID = OLINK.ORG_NODEID
           AND OLINK.TP_ID = TP.TP_ID
       AND SCT.ADMINID = CPL.ADMINID
       AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
           AND SCT.SCALESCORE IS NOT NULL) CNT,
       (SELECT DISTINCT SCT.SCALESCORE,
                        SCT.FREQUENCY,
                        SCT.CUMULATIVEFREQUENCY,
                        SCT.PERCENT,
                        SCT.CUMULATIVEPERCENT,
                        SUB.SUBTEST_NAME
          FROM STFD_FACT             SCT,
               VW_SUBTEST_GRADE_OBJECTIVE_MAP           SUB,
               PRODUCT               PDT,
               CUST_PRODUCT_LINK     CPL,
               ORG_PRODUCT_LINK      OPL,
               ORG_TEST_PROGRAM_LINK OLINK,
               TEST_PROGRAM          TP
         WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
           AND PDT.PRODUCTID = p_test_administration
           AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
           AND PDT.PRODUCTID = CPL.PRODUCTID
           AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
           AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
           AND SCT.ORG_NODEID = OPL.ORG_NODEID
           AND OPL.ORG_NODEID = OLINK.ORG_NODEID
           AND OLINK.TP_ID = TP.TP_ID
           AND SCT.GRADEID = p_grade
           AND SCT.SCALESCORE IS NOT NULL
           AND SCT.SUBTESTID = SUB.SUBTESTID
       AND SCT.GRADEID = SUB.GRADEID
       AND SCT.LEVELID = SUB.LEVELID
       AND SCT.ADMINID = CPL.ADMINID
       AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
           AND SCT.SUBTESTID = p_subtest1) SUB1
  FULL OUTER JOIN (SELECT DISTINCT SCT.SCALESCORE,
                                   SCT.FREQUENCY,
                                   SCT.CUMULATIVEFREQUENCY,
                                   SCT.PERCENT,
                                   SCT.CUMULATIVEPERCENT,
                                   SUB.SUBTEST_NAME
                     FROM STFD_FACT             SCT,
                          VW_SUBTEST_GRADE_OBJECTIVE_MAP           SUB,
                          PRODUCT               PDT,
                          CUST_PRODUCT_LINK     CPL,
                          ORG_PRODUCT_LINK      OPL,
                          ORG_TEST_PROGRAM_LINK OLINK,
                          TEST_PROGRAM          TP
                    WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
                      AND PDT.PRODUCTID = p_test_administration
                      AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
                      AND PDT.PRODUCTID = CPL.PRODUCTID
                      AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
                      AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
                      AND SCT.ORG_NODEID = OPL.ORG_NODEID
                      AND OPL.ORG_NODEID = OLINK.ORG_NODEID
                      AND OLINK.TP_ID = TP.TP_ID
                      AND SCT.GRADEID = p_grade
                      AND SCT.SCALESCORE IS NOT NULL
                      AND SCT.SUBTESTID = SUB.SUBTESTID
            AND SCT.GRADEID = SUB.GRADEID
            AND SCT.LEVELID = SUB.LEVELID
              AND SCT.ADMINID = CPL.ADMINID
            AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
                      AND SCT.SUBTESTID = p_subtest2) SUB2 ON SUB2.SCALESCORE =
                                                        SUB1.SCALESCORE
  FULL OUTER JOIN (SELECT DISTINCT SCT.SCALESCORE,
                                   SCT.FREQUENCY,
                                   SCT.CUMULATIVEFREQUENCY,
                                   SCT.PERCENT,
                                   SCT.CUMULATIVEPERCENT,
                                   SUB.SUBTEST_NAME
                     FROM STFD_FACT             SCT,
                          VW_SUBTEST_GRADE_OBJECTIVE_MAP           SUB,
                          PRODUCT               PDT,
                          CUST_PRODUCT_LINK     CPL,
                          ORG_PRODUCT_LINK      OPL,
                          ORG_TEST_PROGRAM_LINK OLINK,
                          TEST_PROGRAM          TP
                    WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
                      AND PDT.PRODUCTID = p_test_administration
                      AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
                      AND PDT.PRODUCTID = CPL.PRODUCTID
                      AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
                      AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
                      AND SCT.ORG_NODEID = OPL.ORG_NODEID
                      AND OPL.ORG_NODEID = OLINK.ORG_NODEID
                      AND OLINK.TP_ID = TP.TP_ID
                      AND SCT.GRADEID = p_grade
                      AND SCT.SCALESCORE IS NOT NULL
                      AND SCT.SUBTESTID = SUB.SUBTESTID
            AND SCT.GRADEID = SUB.GRADEID
            AND SCT.LEVELID = SUB.LEVELID
              AND SCT.ADMINID = CPL.ADMINID
            AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
                      AND SCT.SUBTESTID = p_subtest3) SUB3 ON SUB1.SCALESCORE =
                                                        SUB3.SCALESCORE
                                                     OR SUB2.SCALESCORE =
                                                        SUB3.SCALESCORE
  FULL OUTER JOIN (SELECT DISTINCT SCT.SCALESCORE,
                                   SCT.FREQUENCY,
                                   SCT.CUMULATIVEFREQUENCY,
                                   SCT.PERCENT,
                                   SCT.CUMULATIVEPERCENT,
                                   SUB.SUBTEST_NAME
                     FROM STFD_FACT             SCT,
                          VW_SUBTEST_GRADE_OBJECTIVE_MAP           SUB,
                          PRODUCT               PDT,
                          CUST_PRODUCT_LINK     CPL,
                          ORG_PRODUCT_LINK      OPL,
                          ORG_TEST_PROGRAM_LINK OLINK,
                          TEST_PROGRAM          TP
                    WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
                      AND PDT.PRODUCTID = p_test_administration
                      AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
                      AND PDT.PRODUCTID = CPL.PRODUCTID
                      AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
                      AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
                      AND SCT.ORG_NODEID = OPL.ORG_NODEID
                      AND OPL.ORG_NODEID = OLINK.ORG_NODEID
                      AND OLINK.TP_ID = TP.TP_ID
                      AND SCT.GRADEID = p_grade
                      AND SCT.SCALESCORE IS NOT NULL
                      AND SCT.SUBTESTID = SUB.SUBTESTID
            AND SCT.GRADEID = SUB.GRADEID
            AND SCT.LEVELID = SUB.LEVELID
              AND SCT.ADMINID = CPL.ADMINID
            AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
                      AND SCT.SUBTESTID = p_subtest4) SUB4 ON SUB1.SCALESCORE =
                                                        SUB4.SCALESCORE
                                                     OR SUB2.SCALESCORE =
                                                        SUB4.SCALESCORE
                                                     OR SUB3.SCALESCORE =
                                                        SUB4.SCALESCORE

  FULL OUTER JOIN (SELECT DISTINCT SCT.SCALESCORE,
                                   SCT.FREQUENCY,
                                   SCT.CUMULATIVEFREQUENCY,
                                   SCT.PERCENT,
                                   SCT.CUMULATIVEPERCENT,
                                   SUB.SUBTEST_NAME
                     FROM STFD_FACT             SCT,
                          VW_SUBTEST_GRADE_OBJECTIVE_MAP           SUB,
                          PRODUCT               PDT,
                          CUST_PRODUCT_LINK     CPL,
                          ORG_PRODUCT_LINK      OPL,
                          ORG_TEST_PROGRAM_LINK OLINK,
                          TEST_PROGRAM          TP
                    WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
                      AND PDT.PRODUCTID = p_test_administration
                      AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
                      AND PDT.PRODUCTID = CPL.PRODUCTID
                      AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
                      AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
                      AND SCT.ORG_NODEID = OPL.ORG_NODEID
                      AND OPL.ORG_NODEID = OLINK.ORG_NODEID
                      AND OLINK.TP_ID = TP.TP_ID
                      AND SCT.GRADEID = p_grade
                      AND SCT.SCALESCORE IS NOT NULL
                      AND SCT.SUBTESTID = SUB.SUBTESTID
            AND SCT.GRADEID = SUB.GRADEID
            AND SCT.LEVELID = SUB.LEVELID
              AND SCT.ADMINID = CPL.ADMINID
            AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
                      AND SCT.SUBTESTID = p_subtest5) SUB5 ON SUB1.SCALESCORE =
                                                     SUB5.SCALESCORE
                                                  OR SUB2.SCALESCORE =
                                                     SUB5.SCALESCORE
                                                  OR SUB3.SCALESCORE =
                                                     SUB5.SCALESCORE
                                                  OR SUB4.SCALESCORE =
                                                     SUB5.SCALESCORE

  FULL OUTER JOIN (SELECT DISTINCT SCT.SCALESCORE,
                                   SCT.FREQUENCY,
                                   SCT.CUMULATIVEFREQUENCY,
                                   SCT.PERCENT,
                                   SCT.CUMULATIVEPERCENT,
                                   SUB.SUBTEST_NAME
                     FROM STFD_FACT             SCT,
                          VW_SUBTEST_GRADE_OBJECTIVE_MAP           SUB,
                          PRODUCT               PDT,
                          CUST_PRODUCT_LINK     CPL,
                          ORG_PRODUCT_LINK      OPL,
                          ORG_TEST_PROGRAM_LINK OLINK,
                          TEST_PROGRAM          TP
                    WHERE ((p_corpdioceseAll = -1 AND SCT.ORG_NODEID = LoggedInUserJasperOrgId)
            OR
            (p_corpdioceseAll<>-1 AND SCT.ORG_NODEID = p_corpdioceseAll))
                      AND PDT.PRODUCTID = p_test_administration
                      AND TP.TP_TYPE = DECODE(p_test_program,1,'PUBLIC','NON-PUBLIC')
                      AND PDT.PRODUCTID = CPL.PRODUCTID
                      AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
                      AND SCT.CUST_PROD_ID = CPL.CUST_PROD_ID
                      AND SCT.ORG_NODEID = OPL.ORG_NODEID
                      AND OPL.ORG_NODEID = OLINK.ORG_NODEID
                      AND OLINK.TP_ID = TP.TP_ID
                      AND SCT.GRADEID = p_grade
                      AND SCT.SCALESCORE IS NOT NULL
                      AND SCT.SUBTESTID = SUB.SUBTESTID
            AND SCT.GRADEID = SUB.GRADEID
            AND SCT.LEVELID = SUB.LEVELID
              AND SCT.ADMINID = CPL.ADMINID
            AND SCT.ISPUBLIC = DECODE(p_test_program,1,1,0)
                      AND SCT.SUBTESTID = p_subtest6) SUB6 ON SUB1.SCALESCORE =
                                                     SUB6.SCALESCORE
                                                  OR SUB2.SCALESCORE =
                                                     SUB6.SCALESCORE
                                                  OR SUB3.SCALESCORE =
                                                     SUB6.SCALESCORE
                                                  OR SUB4.SCALESCORE =
                                                     SUB6.SCALESCORE
                                                  OR SUB5.SCALESCORE =
                                                     SUB6.SCALESCORE
ORDER BY 1 DESC;       



BEGIN
       
         FOR r_Stfd_ScaleScore IN c_Stfd_ScaleScore
                LOOP
                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Stfd_ScaleScore.SCALESCORE1;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Stfd_ScaleScore.SUB1_SUBTEST_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Stfd_ScaleScore.SUB1_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Stfd_ScaleScore.SUB1_CUMULATIVE_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Stfd_ScaleScore.SUB1_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Stfd_ScaleScore.SUB1_CUMULATIVE_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Stfd_ScaleScore.SUB2_SUBTEST_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Stfd_ScaleScore.SUB2_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Stfd_ScaleScore.SUB2_CUMULATIVE_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Stfd_ScaleScore.SUB2_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Stfd_ScaleScore.SUB2_CUMULATIVE_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Stfd_ScaleScore.SUB3_SUBTEST_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Stfd_ScaleScore.SUB3_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Stfd_ScaleScore.SUB3_CUMULATIVE_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Stfd_ScaleScore.SUB3_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Stfd_ScaleScore.SUB3_CUMULATIVE_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Stfd_ScaleScore.SUB4_SUBTEST_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Stfd_ScaleScore.SUB4_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Stfd_ScaleScore.SUB4_CUMULATIVE_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc20 := r_Stfd_ScaleScore.SUB4_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc21 := r_Stfd_ScaleScore.SUB4_CUMULATIVE_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc22 := r_Stfd_ScaleScore.SUB5_SUBTEST_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc23 := r_Stfd_ScaleScore.SUB5_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc24 := r_Stfd_ScaleScore.SUB5_CUMULATIVE_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc25 := r_Stfd_ScaleScore.SUB5_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc27 := r_Stfd_ScaleScore.SUB5_CUMULATIVE_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc28 := r_Stfd_ScaleScore.SUB6_SUBTEST_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc29 := r_Stfd_ScaleScore.SUB6_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc30 := r_Stfd_ScaleScore.SUB6_CUMULATIVE_FREQUENCY;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc31 := r_Stfd_ScaleScore.SUB6_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc32 := r_Stfd_ScaleScore.SUB6_CUMULATIVE_PERCENT;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc33 := r_Stfd_ScaleScore.SCALESCORE2;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc34 := r_Stfd_ScaleScore.SUBTEST_CNT;
                 
                


                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       END LOOP;
      


RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_STFD_SCALESCORE;
/
