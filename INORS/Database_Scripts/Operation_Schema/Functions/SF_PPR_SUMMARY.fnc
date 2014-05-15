CREATE OR REPLACE FUNCTION SF_PPR_SUMMARY (   p_Subtest_Name IN VARCHAR,
                                               p_test_administration IN VARCHAR,
                                               p_customer_id IN CUSTOMER_INFO.CUSTOMERID%TYPE ,
                                               p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                               p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                               p_class IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                               p_grade IN GRADE_DIM.GRADEID%TYPE,
                                               p_test_program IN VARCHAR)
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_PPR_SUMMARY
  * PURPOSE:   Summary section of Proficiency Roster
  * CREATED:   TCS  11/DEC/2013
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


 CURSOR cur_ppr_summary_school (ip_Subtest_Name VARCHAR)
  IS
            SELECT CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                   MEDIA.HIGH_OBTAINED_ELA_SCALE_SCORE
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.HIGH_OBTAINED_MATH_SCALE_SCORE
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.HIGH_OBTAINED_SCIE_SCALE_SCORE
              ELSE
                   MEDIA.HIGH_OBTAINED_SOCIA_SCALESCORE
              END AS HIGHEST_SS_OBTAINED ,
              CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                   SUMT.TOTAL_ELA_PROF_MEAN_SS
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   SUMT.TOTAL_MATH_PROF_MEAN_SS
              WHEN ip_Subtest_Name = 'Science' THEN
                   SUMT.TOTAL_SCIENCE_PROF_MEAN_SS
              ELSE
                   SUMT.TOTAL_SOCIAL_PROF_MEAN_SS
              END AS MEAN_SCALE_SCORE ,
              CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                   MEDIA.LOW_OBTAINED_ELA_SCALE_SCORE
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.LOW_OBTAINED_MATH_SCALE_SCORE
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.LOW_OBTAINED_SCIE_SCALE_SCORE
              ELSE
                   MEDIA.LOW_OBTAINED_SOCIAL_SCALESCORE
              END AS LOWEST_SS_OBTAINED ,
              (SELECT MAX (STFD.STANDARDDEVIATION)
                  FROM STFD_FACT STFD
                    WHERE STFD.ORG_NODEID = MEDIA.ORG_NODEID
                    AND STFD.CUST_PROD_ID = MEDIA.CUST_PROD_ID
                    AND STFD.ADMINID = MEDIA.ADMINID
                    AND STFD.SUBTESTID = CUT.SUBTESTID
                    AND STFD.GRADEID=MEDIA.GRADEID
                    AND STFD.LEVELID = MEDIA.LEVELID
                    AND STFD.ISPUBLIC=MEDIA.ISPUBLIC
                    ) AS STANDARDDEVIATION,
              CUT.LOSS ||'-'||CUT.HOSS  AS L_H_SCORE_POSSIBLE,
              CUT.LOSS ||'-'||(CUT.PASS-1) AS DNP,
              CUT.PASS ||'-'||(CUT.PASSPLUS-1) AS PASS,
              CUT.PASSPLUS ||'-'||(CUT.HOSS) AS PASS_PLS,
              CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_PASSP_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_NUM_PASSP_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.NUM_PASSP_SCIE
              ELSE
                   MEDIA. NUM_PASSP_SOCIAL
              END AS NUM_PASSP,
              CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                    MEDIA.TOT_PERC_PASSP_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_PERC_PASSP_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.PERC_PASSP_SCIE
              ELSE
                   MEDIA.PERC_PASSP_SOCIAL
              END AS PERC_PASSP,
            CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_PASS_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_NUM_PASS_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.NUM_PASS_SCIE
              ELSE
                   MEDIA. NUM_PASS_SOCIAL
              END AS NUM_PASS,
             CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                    MEDIA.TOT_PERC_PASS_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_PERC_PASS_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.PERC_PASS_SCIE
              ELSE
                   MEDIA.PERC_PASS_SOCIAL
              END AS PERC_PASS,
            CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_DNP_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_NUM_DNP_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.NUM_DNP_SCIE
              ELSE
                   MEDIA.NUM_DNP_SOCIAL
              END AS NUM_DNP,
           CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                    MEDIA.TOT_PERC_DNP_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_PERC_DNP_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.PERC_DNP_SCIE
              ELSE
                   MEDIA.PERC_DNP_SOCIAL
              END AS PERC_DNP,
            CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_UNDTRM_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_NUM_UNDTRM_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.NUM_UNDTRM_SCIE
              ELSE
                   MEDIA.NUM_UNDTRM_SOCIAL
              END AS NUM_UNDTRM,
           CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                    MEDIA.TOT_PERC_UNDTRM_ELA
              WHEN ip_Subtest_Name = 'Mathematics' THEN
                   MEDIA.TOT_PERC_UNDTRM_MATH
              WHEN ip_Subtest_Name = 'Science' THEN
                   MEDIA.PERC_UNDTRM_SCIE
              ELSE
                   MEDIA.PERC_UNDTRM_SOCIAL
              END AS PERC_UNDTRM  ,
           CASE
              WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                   CASE
                       WHEN IS_NUMBER(MEDIA.TOT_NUM_UNDTRM_ELA)  = -99 THEN MEDIA.TOT_NUM_UNDTRM_ELA
                       ELSE
                         TO_CHAR( CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_PASSP_ELA) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_PASSP_ELA,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_PASS_ELA) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_PASS_ELA,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_DNP_ELA) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_DNP_ELA,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_UNDTRM_ELA) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_UNDTRM_ELA,0) )
                             END)
                      END
               WHEN ip_Subtest_Name = 'Mathematics' THEN
                   CASE
                       WHEN IS_NUMBER(MEDIA.TOT_NUM_UNDTRM_MATH) = -99 THEN MEDIA.TOT_NUM_UNDTRM_MATH
                       ELSE
                         TO_CHAR( CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_PASSP_MATH) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_PASSP_MATH,0) )
                          END
                         +
                          CASE
                             WHEN IS_NUMBER( MEDIA.TOT_NUM_PASS_ELA )= -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_PASS_MATH,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_DNP_ELA) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_DNP_MATH,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.TOT_NUM_UNDTRM_ELA) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.TOT_NUM_UNDTRM_MATH,0) )
                          END)
                      END
                  WHEN ip_Subtest_Name = 'Science' THEN
                   CASE
                       WHEN IS_NUMBER(MEDIA.NUM_UNDTRM_SCIE) = -99 THEN MEDIA.NUM_UNDTRM_SCIE
                       ELSE
                         TO_CHAR( CASE
                             WHEN IS_NUMBER( MEDIA.NUM_PASSP_SCIE) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_PASSP_SCIE,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_PASS_SCIE) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_PASS_SCIE,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_DNP_SCIE) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_DNP_SCIE,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_UNDTRM_SCIE) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_UNDTRM_SCIE,0) )
                          END)
                      END
                 ELSE
                   CASE
                       WHEN IS_NUMBER(MEDIA.NUM_UNDTRM_SOCIAL)  = -99 THEN MEDIA.NUM_UNDTRM_SOCIAL
                       ELSE
                         TO_CHAR( CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_PASSP_SOCIAL)  = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_PASSP_SOCIAL ,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_PASS_SOCIAL ) = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_PASS_SOCIAL ,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_DNP_SOCIAL)  = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_DNP_SOCIAL ,0) )
                          END
                         +
                          CASE
                             WHEN  IS_NUMBER(MEDIA.NUM_UNDTRM_SOCIAL)  = -99 THEN 0
                             ELSE TO_NUMBER (NVL(MEDIA.NUM_UNDTRM_SOCIAL ,0) )
                          END)
                    END
                END  AS  NO_OF_STUDENTS_LISTED
              FROM --STFD_FACT STFD ,
                   SUMT_FACT SUMT,
                   MEDIA_FACT MEDIA,
                   CUTSCORESCALESCORE CUT,
                   CUST_PRODUCT_LINK CUST
                   --ORG_TEST_PROGRAM_LINK OTPLK,
                 --TEST_PROGRAM TP
              WHERE CUST.CUSTOMERID = p_customer_id
               AND CUST.PRODUCTID =  p_test_administration
               AND MEDIA.ORG_NODEID=p_school
               AND MEDIA.GRADEID = p_grade
               AND MEDIA.ADMINID =CUST.ADMINID
               AND MEDIA.CUST_PROD_ID =  CUST.CUST_PROD_ID
               AND MEDIA.ORG_NODEID = SUMT.ORG_NODEID
               --AND STFD.ORG_NODEID = MEDIA.ORG_NODEID
               AND MEDIA.CUST_PROD_ID = SUMT.CUST_PROD_ID
               --AND STFD.CUST_PROD_ID = MEDIA.CUST_PROD_ID
               AND MEDIA.ADMINID = SUMT.ADMINID
               --AND STFD.ADMINID = MEDIA.ADMINID
               AND MEDIA.GRADEID = SUMT.GRADEID
               --AND STFD.GRADEID = MEDIA.GRADEID
               AND MEDIA.LEVELID = SUMT.LEVELID
               --AND STFD.LEVELID = MEDIA.LEVELID
               AND MEDIA.ISPUBLIC = p_test_program
               AND MEDIA.ISPUBLIC = SUMT.ISPUBLIC
               --AND STFD.ISPUBLIC = MEDIA.ISPUBLIC
               AND CUT.CUST_PROD_ID =MEDIA.CUST_PROD_ID
               AND CUT.GRADEID =MEDIA.GRADEID
               AND CUT.LEVELID =MEDIA.LEVELID
               --AND CUT.SUBTESTID =STFD.SUBTESTID
               --AND OTPLK.ORG_NODEID = STFD.ORG_NODEID
               --AND OTPLK.TP_ID = TP.TP_ID
               --AND TP.TP_TYPE = DECODE (p_test_program,1,'PUBLIC','NON-PUBLIC')
               --AND TP.CUSTOMERID = CUST.CUSTOMERID
               --AND TP.ADMINID = CUST.ADMINID
               AND ((ip_Subtest_Name = 'English/Language Arts'
                    AND SUMT.ENGLANG_ARTS_SUBTESTID = MEDIA.ENGLANG_ARTS_SUBTESTID
                    AND SUMT.ENGLANG_ARTS_SUBTESTID = CUT.SUBTESTID)
                    OR
                    (ip_Subtest_Name = 'Mathematics'
                    AND SUMT.MATHEMATICS_SUBTESTID = MEDIA.MATHEMATICS_SUBTESTID
                    AND SUMT.MATHEMATICS_SUBTESTID = CUT.SUBTESTID)
                    OR
                    (ip_Subtest_Name = 'Science'
                    AND SUMT.SCIENCE_SUBTESTID = MEDIA.SCIENCE_SUBTESTID
                    AND SUMT.SCIENCE_SUBTESTID = CUT.SUBTESTID)
                    OR
                    (ip_Subtest_Name = 'Social Studies'
                    AND SUMT.SOCIAL_SUBTESTID = MEDIA.SOCIAL_SUBTESTID
                    AND SUMT.SOCIAL_SUBTESTID = CUT.SUBTESTID));                                      
                        
 ---------------------------------class level summary---------------------------
 CURSOR cur_ppr_summary_class (ip_Subtest_Name VARCHAR)
  IS
              SELECT  CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_HIGH_SCALE_SCORE_OBTAINED
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_HIGH_SCALE_SCORE_OBTAINED
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_HIGH_SCALE_SCORE_OBTAINED
                      ELSE
                           CLS.SOC_HIGH_SCALE_SCORE_OBTAINED
                      END AS HIGHEST_SS_OBTAINED ,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_MEAN_SCALE_SCORE
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_MEAN_SCALE_SCORE
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_MEAN_SCALE_SCORE
                      ELSE
                           CLS.SOC_MEAN_SCALE_SCORE
                      END AS MEAN_SCALE_SCORE,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_LOW_SCALE_SCORE_OBTAINED
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_LOW_SCALE_SCORE_OBTAINED
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_LOW_SCALE_SCORE_OBTAINED
                      ELSE
                           CLS.SOC_LOW_SCALE_SCORE_OBTAINED
                      END AS LOWEST_SS_OBTAINED,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_STANDARD_DEVIATION
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_STANDARD_DEVIATION
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_STANDARD_DEVIATION
                      ELSE
                           CLS.SOC_STANDARD_DEVIATION
                      END AS STANDARDDEVIATION,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_LOW_SCALE_POSSIBLE||'-'||ELA_HIGH_SCALE_POSSIBLE
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_LOW_SCALE_POSSIBLE||'-'||MATH_HIGH_SCALE_POSSIBLE
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_LOW_SCALE_POSSIBLE||'-'||SCI_HIGH_SCALE_POSSIBLE
                      ELSE
                           CLS.SOC_LOW_SCALE_POSSIBLE||'-'||SOC_HIGH_SCALE_POSSIBLE
                      END AS L_H_SCORE_POSSIBLE,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_UND_SCALE_SCORE_RANGE
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_UND_SCALE_SCORE_RANGE
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_UND_SCALE_SCORE_RANGE
                      ELSE
                           CLS.SOC_UND_SCALE_SCORE_RANGE
                      END AS DNP,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_PASS_SCALE_SCORE_RANGE
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_PASS_SCALE_SCORE_RANGE
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_PASS_SCALE_SCORE_RANGE
                      ELSE
                           CLS.SOC_PASS_SCALE_SCORE_RANGE
                      END AS PASS,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_PASSP_SCALE_SCORE_RANGE
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_PASSP_SCALE_SCORE_RANGE
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_PASSP_SCALE_SCORE_RANGE
                      ELSE
                           CLS.SOC_PASSP_SCALE_SCORE_RANGE
                      END AS PASS_PLS,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_NUM_STUDENT_PASSP
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_NUM_STUDENT_PASSP
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_NUM_STUDENT_PASSP
                      ELSE
                           CLS.SOC_NUM_STUDENT_PASSP
                      END AS NUM_PASSP,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_PERC_STUDENT_PASSP
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_PERC_STUDENT_PASSP
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_PERC_STUDENT_PASSP
                      ELSE
                           CLS.SOC_PERC_STUDENT_PASSP
                      END AS PERC_PASSP,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_NUM_STUDENT_PASS
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_NUM_STUDENT_PASS
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_NUM_STUDENT_PASS
                      ELSE
                           CLS.SOC_NUM_STUDENT_PASS
                      END AS NUM_PASS,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_PERC_STUDENT_PASS
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_PERC_STUDENT_PASS
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_PERC_STUDENT_PASS
                      ELSE
                           CLS.SOC_PERC_STUDENT_PASS
                      END AS PERC_PASS,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_NUM_STUDENT_DNP
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_NUM_STUDENT_DNP
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_NUM_STUDENT_DNP
                      ELSE
                           CLS.SOC_NUM_STUDENT_DNP
                      END AS NUM_DNP,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_PERC_STUDENT_DNP
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_PERC_STUDENT_DNP
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_PERC_STUDENT_DNP
                      ELSE
                           CLS.SOC_PERC_STUDENT_DNP
                      END AS PERC_DNP,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_NUM_STUDENT_UNDETERMINED
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_NUM_STUDENT_UNDETERMINED
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_NUM_STUDENT_UNDETERMINED
                      ELSE
                           CLS.SOC_NUM_STUDENT_UNDETERMINED
                      END AS NUM_UNDTRM,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           CLS.ELA_PERC_STUDENT_UNDETERMINED
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           CLS.MATH_PERC_STUDENT_UNDETERMINED
                      WHEN ip_Subtest_Name = 'Science' THEN
                           CLS.SCI_PERC_STUDENT_UNDETERMINED
                      ELSE
                           CLS.SOC_PERC_STUDENT_UNDETERMINED
                      END AS PERC_UNDTRM,
                      CASE
                      WHEN ip_Subtest_Name = 'English/Language Arts' THEN
                           IS_NUMBER (CLS.ELA_NO_STUDENTS_LISTED)
                      WHEN ip_Subtest_Name = 'Mathematics' THEN
                           IS_NUMBER (CLS.MATH_NO_STUDENT_LISTED)
                      WHEN ip_Subtest_Name = 'Science' THEN
                           IS_NUMBER (CLS.SCI_NO_STUDENT_LISTED)
                      ELSE
                           IS_NUMBER (CLS.SOC_NO_STUDENT_LISTED)
                      END AS NO_OF_STUDENTS_LISTED

              FROM CLASS_SUMM_FACT CLS,
                   CUST_PRODUCT_LINK CUST
              WHERE  CLS.ORG_NODEID =p_class
                AND CLS.CUST_PROD_ID =CUST.CUST_PROD_ID
                AND CLS.ADMINID = CUST.ADMINID
                AND CLS.ISPUBLIC = p_test_program
                AND CLS.GRADEID=p_grade
                AND CUST.CUSTOMERID = p_customer_id
                AND CUST.PRODUCTID = p_test_administration;
                                      
                        
CURSOR cur_ppr_summary_subtest
IS 
SELECT A.SUBTEST_NAME
FROM 
(SELECT TRIM( SUBSTR ( txt
                     , INSTR (txt, ',', 1, level ) + 1
                     , INSTR (txt, ',', 1, level+1
                     )
               - INSTR (txt, ',', 1, level) -1 ) ) AS subtest_name
        FROM ( SELECT ','||p_Subtest_Name||',' AS txt
                  FROM dual )
         CONNECT BY level <=
                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1)A
   WHERE A.SUBTEST_NAME NOT IN ('null') ;
              

BEGIN
       IF p_class = -1 THEN
                FOR  r_ppr_summary_subtest IN cur_ppr_summary_subtest
                LOOP 
                    FOR r_cur_ppr_summary IN cur_ppr_summary_school(r_ppr_summary_subtest.SUBTEST_NAME)
                    LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_cur_ppr_summary.HIGHEST_SS_OBTAINED;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_cur_ppr_summary.MEAN_SCALE_SCORE;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_cur_ppr_summary.LOWEST_SS_OBTAINED;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_cur_ppr_summary.STANDARDDEVIATION;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_cur_ppr_summary.L_H_SCORE_POSSIBLE;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_cur_ppr_summary.DNP;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_cur_ppr_summary.PASS;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_cur_ppr_summary.PASS_PLS;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_cur_ppr_summary.NUM_PASSP;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_cur_ppr_summary.PERC_PASSP;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_cur_ppr_summary.NUM_PASS;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_cur_ppr_summary.PERC_PASS;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_cur_ppr_summary.NUM_DNP;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_cur_ppr_summary.PERC_DNP;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_cur_ppr_summary.NUM_UNDTRM;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_cur_ppr_summary.PERC_UNDTRM;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_cur_ppr_summary.NO_OF_STUDENTS_LISTED;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_ppr_summary_subtest.SUBTEST_NAME;


                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);                    
                     END LOOP;
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                 END LOOP;    
       ELSE      
                FOR  r_ppr_summary_subtest IN cur_ppr_summary_subtest
                LOOP
                  FOR r_cur_ppr_summary IN cur_ppr_summary_class(r_ppr_summary_subtest.SUBTEST_NAME)
                  LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_cur_ppr_summary.HIGHEST_SS_OBTAINED;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_cur_ppr_summary.MEAN_SCALE_SCORE;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_cur_ppr_summary.LOWEST_SS_OBTAINED;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_cur_ppr_summary.STANDARDDEVIATION;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_cur_ppr_summary.L_H_SCORE_POSSIBLE;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_cur_ppr_summary.DNP;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_cur_ppr_summary.PASS;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_cur_ppr_summary.PASS_PLS;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_cur_ppr_summary.NUM_PASSP;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_cur_ppr_summary.PERC_PASSP;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_cur_ppr_summary.NUM_PASS;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_cur_ppr_summary.PERC_PASS;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_cur_ppr_summary.NUM_DNP;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_cur_ppr_summary.PERC_DNP;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_cur_ppr_summary.NUM_UNDTRM;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_cur_ppr_summary.PERC_UNDTRM;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_cur_ppr_summary.NO_OF_STUDENTS_LISTED;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_ppr_summary_subtest.SUBTEST_NAME;



                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);                  
                   END LOOP;
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                 END LOOP;   
       END IF;
       

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_PPR_SUMMARY;
/
