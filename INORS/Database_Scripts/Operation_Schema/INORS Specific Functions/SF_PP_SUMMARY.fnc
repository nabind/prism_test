CREATE OR REPLACE FUNCTION SF_PP_SUMMARY (     LoggedInUserJasperOrgId IN STFD_FACT.ORG_NODEID%TYPE,
                                               p_test_administration IN PRODUCT.PRODUCTID%TYPE,
                                               p_test_program IN VARCHAR,                                               
                                               p_corpdioceseAll IN STFD_FACT.ORG_NODEID%TYPE,
                                               p_schoolAll IN STFD_FACT.ORG_NODEID%TYPE,                                               
                                               p_grade IN STFD_FACT.GRADEID%TYPE,
                                               p_Subtest IN VARCHAR,
                                               p_customerid IN CUST_PRODUCT_LINK.CUSTOMERID%TYPE)
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_PP_SUMMARY
  * PURPOSE:   summary query Of Proficiency Performance Summary
  * CREATED:   TCS  17/DEC/2013
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


 CURSOR c_Prof_Perf_Summ
  IS
     SELECT DISTINCT CASE
              WHEN p_Subtest = 'English/Language Arts' THEN
                   SUMT.TOTAL_ELA_PROF_MEAN_SS
              WHEN p_Subtest = 'Mathematics' THEN
                   SUMT.TOTAL_MATH_PROF_MEAN_SS
              WHEN p_Subtest = 'Science' THEN
                   SUMT.TOTAL_SCIENCE_PROF_MEAN_SS
              ELSE
                   SUMT.TOTAL_SOCIAL_PROF_MEAN_SS
              END AS MEAN_SCALE_SCORE ,

			        CUT.PASS AS PASS_CUT_SCORE,
              CUT.PASSPLUS AS PASSPLUS_CUT_SCORE,
              CUT.LOSS AS LPSS,
              CUT.HOSS AS HPSS,
              CUT.PASSPLUS ||'-'||CUT.HOSS AS PASSPLUS_LOW_HIGH_SS_RANGE,
              CUT.PASS ||'-'||(CUT.PASSPLUS-1) AS PASS_LOW_HIGH_SS_RANGE,
              CUT.LOSS ||'-'||(CUT.PASS-1) AS DNP_LOW_HIGH_SS_RANGE,           

			  MAX (STFD.STANDARDDEVIATION) AS STANDARDDEVIATION,

			  CASE
              WHEN p_Subtest = 'English/Language Arts' THEN
                   MEDIA.LOW_OBTAINED_ELA_SCALE_SCORE
              WHEN p_Subtest = 'Mathematics' THEN
                   MEDIA.LOW_OBTAINED_MATH_SCALE_SCORE
              WHEN p_Subtest = 'Science' THEN
                   MEDIA.LOW_OBTAINED_SCIE_SCALE_SCORE
              ELSE
                   MEDIA.LOW_OBTAINED_SOCIAL_SCALESCORE
              END AS LOWEST_SS_OBTAINED ,

			  CASE
              WHEN p_Subtest = 'English/Language Arts' THEN
                   MEDIA.HIGH_OBTAINED_ELA_SCALE_SCORE
              WHEN p_Subtest = 'Mathematics' THEN
                   MEDIA.HIGH_OBTAINED_MATH_SCALE_SCORE
              WHEN p_Subtest = 'Science' THEN
                   MEDIA.HIGH_OBTAINED_SCIE_SCALE_SCORE
              ELSE
                   MEDIA.HIGH_OBTAINED_SOCIA_SCALESCORE
              END AS HIGHEST_SS_OBTAINED ,

			  SPPR.PASSPLUS_MSS,
			  SPPR.PASSPLUS_SD,
			  SPPR.PASSPLUS_LOW_SS_OBTAINED,
			  SPPR.PASSPLUS_HIGH_SS_OBTAINED,

              CASE
              WHEN p_Subtest = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_PASSP_ELA
              WHEN p_Subtest = 'Mathematics' THEN
                   MEDIA.TOT_NUM_PASSP_MATH
              WHEN p_Subtest = 'Science' THEN
                   MEDIA.NUM_PASSP_SCIE
              ELSE
                   MEDIA. NUM_PASSP_SOCIAL
              END AS NUM_PASSP,

			  SPPR.PASS_MSS,
			  SPPR.PASS_SD,
			  SPPR.PASS_LOW_SS_OBTAINED,
			  SPPR.PASS_HIGH_SS_OBTAINED,

            CASE
              WHEN p_Subtest = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_PASS_ELA
              WHEN p_Subtest = 'Mathematics' THEN
                   MEDIA.TOT_NUM_PASS_MATH
              WHEN p_Subtest = 'Science' THEN
                   MEDIA.NUM_PASS_SCIE
              ELSE
                   MEDIA. NUM_PASS_SOCIAL
              END AS NUM_PASS,

			  SPPR.DNP_MSS,
			  SPPR.DNP_SD,
		      SPPR.DNP_LOW_SS_OBTAINED,
		      SPPR.DNP_HIGH_SS_OBTAINED,


            CASE
              WHEN p_Subtest = 'English/Language Arts' THEN
                     MEDIA.TOT_NUM_DNP_ELA
              WHEN p_Subtest = 'Mathematics' THEN
                   MEDIA.TOT_NUM_DNP_MATH
              WHEN p_Subtest = 'Science' THEN
                   MEDIA.NUM_DNP_SCIE
              ELSE
                   MEDIA.NUM_DNP_SOCIAL
              END AS NUM_DNP


              FROM STFD_FACT STFD ,
                   SUMT_FACT SUMT,
				           SPPR_FACT SPPR,
                   MEDIA_FACT MEDIA,
                   CUTSCORESCALESCORE CUT,
				   PRODUCT PDT,
                   CUST_PRODUCT_LINK CUST,
				   ORG_PRODUCT_LINK OPL,
                   ORG_TEST_PROGRAM_LINK OTPLK,
				   TEST_PROGRAM TP
              WHERE ((p_corpdioceseAll = -1 AND p_schoolAll = -1 AND STFD.ORG_NODEID = LoggedInUserJasperOrgId) OR
                       (p_corpdioceseAll <> -1 AND p_schoolAll = -1 AND STFD.ORG_NODEID = p_corpdioceseAll) OR
                       (p_corpdioceseAll <> -1 AND p_schoolAll <> -1 AND STFD.ORG_NODEID = p_schoolAll))
			   AND PDT.PRODUCTID = p_test_administration
               AND TP.TP_TYPE = DECODE(p_test_program, 1, 'PUBLIC', 'NON-PUBLIC')
			   AND PDT.PRODUCTID = CUST.PRODUCTID
			   AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
               AND STFD.GRADEID = p_grade
               AND CUST.CUSTOMERID = p_customerid

               AND STFD.ADMINID =CUST.ADMINID
               AND STFD.CUST_PROD_ID =  CUST.CUST_PROD_ID
			   AND OPL.ORG_NODEID = OTPLK.ORG_NODEID
               AND STFD.ORG_NODEID = SUMT.ORG_NODEID
               AND STFD.ORG_NODEID = MEDIA.ORG_NODEID
			   AND STFD.ORG_NODEID = SPPR.ORG_NODEID
               AND STFD.CUST_PROD_ID = SUMT.CUST_PROD_ID
               AND STFD.CUST_PROD_ID = MEDIA.CUST_PROD_ID
			   AND STFD.CUST_PROD_ID = SPPR.CUST_PROD_ID
               AND STFD.ADMINID = SUMT.ADMINID
               AND STFD.ADMINID = MEDIA.ADMINID
			   AND STFD.ADMINID = SPPR.ADMINID
               AND STFD.GRADEID = SUMT.GRADEID
               AND STFD.GRADEID = MEDIA.GRADEID
			   AND STFD.GRADEID = SPPR.GRADEID
               AND STFD.LEVELID = SUMT.LEVELID
               AND STFD.LEVELID = MEDIA.LEVELID
             AND STFD.LEVELID = SPPR.LEVELID
             AND STFD.ISPUBLIC = DECODE(p_test_program,1,1,0)
             AND STFD.ISPUBLIC = SUMT.ISPUBLIC
             AND STFD.ISPUBLIC = MEDIA.ISPUBLIC
             AND STFD.ISPUBLIC = SPPR.ISPUBLIC
               AND CUT.CUST_PROD_ID =STFD.CUST_PROD_ID
               AND CUT.GRADEID =STFD.GRADEID
               AND CUT.LEVELID =STFD.LEVELID
               AND CUT.SUBTESTID =STFD.SUBTESTID
               AND OTPLK.ORG_NODEID = STFD.ORG_NODEID
               AND OTPLK.TP_ID = TP.TP_ID
               AND TP.CUSTOMERID = CUST.CUSTOMERID
               AND TP.ADMINID = CUST.ADMINID
               AND ((p_Subtest = 'English/Language Arts'
                    AND SUMT.ENGLANG_ARTS_SUBTESTID = MEDIA.ENGLANG_ARTS_SUBTESTID
                    AND SUMT.ENGLANG_ARTS_SUBTESTID = STFD.SUBTESTID
					AND SUMT.ENGLANG_ARTS_SUBTESTID = SPPR.SUBTESTID)
                    OR
                    (p_Subtest = 'Mathematics'
                    AND SUMT.MATHEMATICS_SUBTESTID = MEDIA.MATHEMATICS_SUBTESTID
                    AND SUMT.MATHEMATICS_SUBTESTID = STFD.SUBTESTID
					AND SUMT.MATHEMATICS_SUBTESTID = SPPR.SUBTESTID)
                    OR
                    (p_Subtest = 'Science'
                    AND SUMT.SCIENCE_SUBTESTID = MEDIA.SCIENCE_SUBTESTID
                    AND SUMT.SCIENCE_SUBTESTID = STFD.SUBTESTID
					AND SUMT.SCIENCE_SUBTESTID = SPPR.SUBTESTID)
                    OR
                    (p_Subtest = 'Social Studies'
                    AND SUMT.SOCIAL_SUBTESTID = MEDIA.SOCIAL_SUBTESTID
                    AND SUMT.SOCIAL_SUBTESTID = STFD.SUBTESTID
					AND SUMT.SOCIAL_SUBTESTID = SPPR.SUBTESTID))
          AND ROWNUM = 1
              GROUP BY CASE
					  WHEN p_Subtest = 'English/Language Arts' THEN
						   SUMT.TOTAL_ELA_PROF_MEAN_SS
					  WHEN p_Subtest = 'Mathematics' THEN
						   SUMT.TOTAL_MATH_PROF_MEAN_SS
					  WHEN p_Subtest = 'Science' THEN
						   SUMT.TOTAL_SCIENCE_PROF_MEAN_SS
					  ELSE
						   SUMT.TOTAL_SOCIAL_PROF_MEAN_SS
					  END,

					  CUT.PASS,
            CUT.PASSPLUS,
            CUT.LOSS,
            CUT.HOSS,
            CUT.PASSPLUS ||'-'||CUT.HOSS,
            CUT.PASS ||'-'||(CUT.PASSPLUS-1),
            CUT.LOSS ||'-'||(CUT.PASS-1),


					  CASE
					  WHEN p_Subtest = 'English/Language Arts' THEN
						   MEDIA.LOW_OBTAINED_ELA_SCALE_SCORE
					  WHEN p_Subtest = 'Mathematics' THEN
						   MEDIA.LOW_OBTAINED_MATH_SCALE_SCORE
					  WHEN p_Subtest = 'Science' THEN
						   MEDIA.LOW_OBTAINED_SCIE_SCALE_SCORE
					  ELSE
						   MEDIA.LOW_OBTAINED_SOCIAL_SCALESCORE
					  END,

					  CASE
					  WHEN p_Subtest = 'English/Language Arts' THEN
						   MEDIA.HIGH_OBTAINED_ELA_SCALE_SCORE
					  WHEN p_Subtest = 'Mathematics' THEN
						   MEDIA.HIGH_OBTAINED_MATH_SCALE_SCORE
					  WHEN p_Subtest = 'Science' THEN
						   MEDIA.HIGH_OBTAINED_SCIE_SCALE_SCORE
					  ELSE
						   MEDIA.HIGH_OBTAINED_SOCIA_SCALESCORE
					  END,

					  SPPR.PASSPLUS_MSS,
					  SPPR.PASSPLUS_SD,
					  SPPR.PASSPLUS_LOW_SS_OBTAINED,
					  SPPR.PASSPLUS_HIGH_SS_OBTAINED,

					  CASE
					  WHEN p_Subtest = 'English/Language Arts' THEN
							 MEDIA.TOT_NUM_PASSP_ELA
					  WHEN p_Subtest = 'Mathematics' THEN
						   MEDIA.TOT_NUM_PASSP_MATH
					  WHEN p_Subtest = 'Science' THEN
						   MEDIA.NUM_PASSP_SCIE
					  ELSE
						   MEDIA. NUM_PASSP_SOCIAL
					  END,

					  SPPR.PASS_MSS,
					  SPPR.PASS_SD,
					  SPPR.PASS_LOW_SS_OBTAINED,
					  SPPR.PASS_HIGH_SS_OBTAINED,

					CASE
					  WHEN p_Subtest = 'English/Language Arts' THEN
							 MEDIA.TOT_NUM_PASS_ELA
					  WHEN p_Subtest = 'Mathematics' THEN
						   MEDIA.TOT_NUM_PASS_MATH
					  WHEN p_Subtest = 'Science' THEN
						   MEDIA.NUM_PASS_SCIE
					  ELSE
						   MEDIA. NUM_PASS_SOCIAL
					  END,

					  SPPR.DNP_MSS,
					  SPPR.DNP_SD,
					  SPPR.DNP_LOW_SS_OBTAINED,
					  SPPR.DNP_HIGH_SS_OBTAINED,


					CASE
					  WHEN p_Subtest = 'English/Language Arts' THEN
							 MEDIA.TOT_NUM_DNP_ELA
					  WHEN p_Subtest = 'Mathematics' THEN
						   MEDIA.TOT_NUM_DNP_MATH
					  WHEN p_Subtest = 'Science' THEN
						   MEDIA.NUM_DNP_SCIE
					  ELSE
						   MEDIA.NUM_DNP_SOCIAL
					  END;       



BEGIN
       
         FOR r_Prof_Perf_Summ IN c_Prof_Perf_Summ
                LOOP
                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Prof_Perf_Summ.MEAN_SCALE_SCORE;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Prof_Perf_Summ.PASS_CUT_SCORE;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Prof_Perf_Summ.PASSPLUS_CUT_SCORE;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Prof_Perf_Summ.LPSS;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Prof_Perf_Summ.HPSS;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Prof_Perf_Summ.PASSPLUS_LOW_HIGH_SS_RANGE;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Prof_Perf_Summ.PASS_LOW_HIGH_SS_RANGE;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Prof_Perf_Summ.DNP_LOW_HIGH_SS_RANGE;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Prof_Perf_Summ.STANDARDDEVIATION;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Prof_Perf_Summ.LOWEST_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Prof_Perf_Summ.HIGHEST_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Prof_Perf_Summ.PASSPLUS_MSS;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Prof_Perf_Summ.PASSPLUS_SD;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Prof_Perf_Summ.PASSPLUS_LOW_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Prof_Perf_Summ.PASSPLUS_HIGH_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Prof_Perf_Summ.NUM_PASSP;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Prof_Perf_Summ.PASS_MSS;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Prof_Perf_Summ.PASS_SD;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Prof_Perf_Summ.PASS_LOW_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc20 := r_Prof_Perf_Summ.PASS_HIGH_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc21 := r_Prof_Perf_Summ.NUM_PASS;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc22 := r_Prof_Perf_Summ.DNP_MSS;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc23 := r_Prof_Perf_Summ.DNP_SD;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc24 := r_Prof_Perf_Summ.DNP_LOW_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc25 := r_Prof_Perf_Summ.DNP_HIGH_SS_OBTAINED;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc26 := r_Prof_Perf_Summ.NUM_DNP;
                 
                


                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       END LOOP;
      


RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_PP_SUMMARY;
/
