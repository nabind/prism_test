CREATE OR REPLACE FUNCTION SF_MO_GENERATE_LABEL (
                              p_test_administration IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE
                             ,p_student_selection IN VARCHAR2
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_MO_GENERATE_LABEL
  * PURPOSE:   To generate Label 
  * CREATED:   TCS  08/MAY/2015
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR:353639    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();
  
  v_counter NUMBER;
  v_student_bio_id STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE;
  v_student_max_occurence NUMBER;

 CURSOR c_Get_Student_Label
  IS
  SELECT MAX(A.STUDENT_CNT) OVER ( PARTITION BY  A.DISTRICT_CODE,
                                               A.SCHOOL_CODE,
                                               A.GRADEID,
                                               A.STUDENT_BIO_ID
                                   )  AS STUDENT_MAX_OCCURENCE,
     A.*                                       
  FROM 
   (
      SELECT  COUNT (1) OVER ( PARTITION BY STUDET.DISTRICT_CODE,
                                                     STUDET.SCHOOL_CODE,
                                                     STUDET.GRADEID,
                                                     STUDET.STUDENT_BIO_ID
                                         ORDER BY STUDET.DISTRICT_CODE,
                                                  STUDET.SCHOOL_CODE,
                                                  STUDET.GRADEID,
                                                  STUDET.LAST_NAME||', '||STUDET.FIRST_NAME||' '||STUDET.MIDDLE_NAME,
                                                  STUDET.SUBTEST_SEQ)  AS STUDENT_CNT,                                     
             STUDET.DISTRICT_NAME,
             STUDET.SCHOOL_NAME,
             STUDET.DISTRICT_CODE,
             STUDET.SCHOOL_CODE,
             STUDET.GRADEID,
             (SELECT GRADE_NAME FROM GRADE_DIM WHERE GRADEID = STUDET.GRADEID)AS GRADE_NAME,
             (SELECT GRADE_SEQ FROM GRADE_DIM WHERE GRADEID = STUDET.GRADEID)AS GRADE_SEQ,
             (SELECT GRADE_CODE FROM GRADE_DIM WHERE GRADEID = STUDET.GRADEID)AS GRADE_CODE,
             STUDET.STUDENT_BIO_ID,
             STUDET.LAST_NAME||', '||STUDET.FIRST_NAME||' '||STUDET.MIDDLE_NAME AS STUDENT_NAME,
             STUDET.EXT_STUDENT_ID,
             STUDET.BIRTHDATE,--SF_CONV_TO_DATE(STUDET.BIRTHDATE,'MM/DD/RRRR') AS BIRTHDATE,
             STUDET.SUBTEST_NAME,
             STUDET.SUBTEST_CODE,
             STUDET.SUBTEST_SEQ,
             CASE
             WHEN STUDET.PL ='1' THEN 'Below Basic'
             WHEN STUDET.PL ='2' THEN 'Basic'
             WHEN STUDET.PL ='3' THEN 'Proficient'
             WHEN STUDET.PL ='4' THEN 'Advanced'
             ELSE 'Level Not Determined'
             END AS PROF_NAME,
             STUDET.TEST_DATE,
             STUDET.SS
      FROM MV_RPRT_STUD_DETAILS STUDET
      WHERE STUDET.CUST_PROD_ID = p_test_administration
      AND (p_student_selection = '-1' 
           OR
           p_student_selection = '1' AND STUDET.APPEAL_INDICATOR = 'Y')
      ORDER BY DISTRICT_CODE,
               SCHOOL_CODE,
               GRADE_SEQ,
               STUDENT_NAME,
               SUBTEST_SEQ)A
      ORDER BY A.DISTRICT_CODE,
               A.SCHOOL_CODE,
               A.GRADE_SEQ,
               A.STUDENT_NAME,
               A.SUBTEST_SEQ;



  BEGIN
  
      v_counter:=0;
      v_student_max_occurence := 0;
      
      FOR r_Get_Student_Label IN c_Get_Student_Label
      LOOP
      t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
      v_counter:=r_Get_Student_Label.STUDENT_CNT;
      v_student_max_occurence := r_Get_Student_Label.STUDENT_MAX_OCCURENCE;
      
      IF v_counter =1 AND v_student_max_occurence <> 1 THEN
          v_student_bio_id:= r_Get_Student_Label.STUDENT_BIO_ID;
          
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Student_Label.STUDENT_CNT;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Student_Label.DISTRICT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Student_Label.SCHOOL_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Student_Label.DISTRICT_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Student_Label.SCHOOL_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Student_Label.GRADEID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Student_Label.GRADE_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Student_Label.GRADE_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Student_Label.GRADE_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Student_Label.STUDENT_BIO_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Student_Label.STUDENT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Get_Student_Label.EXT_STUDENT_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Get_Student_Label.BIRTHDATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Get_Student_Label.SUBTEST_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Get_Student_Label.SUBTEST_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Get_Student_Label.SUBTEST_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Get_Student_Label.PROF_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Get_Student_Label.TEST_DATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Get_Student_Label.SS;
          
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          
     ELSIF v_counter =1 AND v_student_max_occurence = 1 THEN
                 
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Student_Label.STUDENT_CNT;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Student_Label.DISTRICT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Student_Label.SCHOOL_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Student_Label.DISTRICT_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Student_Label.SCHOOL_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Student_Label.GRADEID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Student_Label.GRADE_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Student_Label.GRADE_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Student_Label.GRADE_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Student_Label.STUDENT_BIO_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Student_Label.STUDENT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Get_Student_Label.EXT_STUDENT_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Get_Student_Label.BIRTHDATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Get_Student_Label.SUBTEST_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Get_Student_Label.SUBTEST_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Get_Student_Label.SUBTEST_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Get_Student_Label.PROF_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Get_Student_Label.TEST_DATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Get_Student_Label.SS;
          
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := (r_Get_Student_Label.STUDENT_CNT)+1;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Student_Label.DISTRICT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Student_Label.SCHOOL_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Student_Label.DISTRICT_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Student_Label.SCHOOL_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Student_Label.GRADEID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := NULL;--r_Get_Student_Label.GRADE_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Student_Label.GRADE_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Student_Label.GRADE_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Student_Label.STUDENT_BIO_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Student_Label.STUDENT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := NULL;--r_Get_Student_Label.EXT_STUDENT_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := NULL;--r_Get_Student_Label.BIRTHDATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := NULL;--r_Get_Student_Label.SUBTEST_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := NULL;--r_Get_Student_Label.SUBTEST_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := 99;--r_Get_Student_Label.SUBTEST_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := NULL;--r_Get_Student_Label.PROF_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 :=NULL;--r_Get_Student_Label.TEST_DATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := NULL;--r_Get_Student_Label.SS;
          
                    
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
             
       ELSIF v_student_bio_id = r_Get_Student_Label.STUDENT_BIO_ID AND MOD(v_counter,2)=0 THEN
          
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Student_Label.STUDENT_CNT;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Student_Label.DISTRICT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Student_Label.SCHOOL_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Student_Label.DISTRICT_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Student_Label.SCHOOL_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Student_Label.GRADEID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Student_Label.GRADE_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Student_Label.GRADE_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Student_Label.GRADE_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Student_Label.STUDENT_BIO_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Student_Label.STUDENT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Get_Student_Label.EXT_STUDENT_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Get_Student_Label.BIRTHDATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Get_Student_Label.SUBTEST_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Get_Student_Label.SUBTEST_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Get_Student_Label.SUBTEST_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Get_Student_Label.PROF_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Get_Student_Label.TEST_DATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Get_Student_Label.SS;
          
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       
      ELSIF MOD(v_counter,2)<>0 AND v_student_max_occurence = 3 THEN
         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Student_Label.STUDENT_CNT;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Student_Label.DISTRICT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Student_Label.SCHOOL_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Student_Label.DISTRICT_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Student_Label.SCHOOL_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Student_Label.GRADEID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Student_Label.GRADE_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Student_Label.GRADE_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Student_Label.GRADE_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Student_Label.STUDENT_BIO_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Student_Label.STUDENT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Get_Student_Label.EXT_STUDENT_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Get_Student_Label.BIRTHDATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Get_Student_Label.SUBTEST_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Get_Student_Label.SUBTEST_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Get_Student_Label.SUBTEST_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Get_Student_Label.PROF_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Get_Student_Label.TEST_DATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Get_Student_Label.SS;
          
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          
          
          
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := (r_Get_Student_Label.STUDENT_CNT)+1;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Student_Label.DISTRICT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Student_Label.SCHOOL_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Student_Label.DISTRICT_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Student_Label.SCHOOL_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Student_Label.GRADEID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := NULL;--r_Get_Student_Label.GRADE_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Student_Label.GRADE_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Student_Label.GRADE_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Student_Label.STUDENT_BIO_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Student_Label.STUDENT_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := NULL;--r_Get_Student_Label.EXT_STUDENT_ID;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := NULL;--r_Get_Student_Label.BIRTHDATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := NULL;--r_Get_Student_Label.SUBTEST_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := NULL;--r_Get_Student_Label.SUBTEST_CODE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := 99;--r_Get_Student_Label.SUBTEST_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := NULL;--r_Get_Student_Label.PROF_NAME;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 :=NULL;--r_Get_Student_Label.TEST_DATE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := NULL;--r_Get_Student_Label.SS;
          
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          
      END IF;
       
      END LOOP;

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
  RAISE;
    RETURN NULL;
END SF_MO_GENERATE_LABEL;
/
