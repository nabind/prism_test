CREATE OR REPLACE FUNCTION SF_GET_RANK_BY_SUBTEST_SCORE (i_Student_ID  IN student_bio_dim.student_bio_id%TYPE
                                                ,i_SubTest_Id  IN VARCHAR2
                                                ,i_Score_Type  IN VARCHAR2
                                                ,i_Product_Id IN product.productid%TYPE
                                                ,i_Grade_Id IN grade_dim.gradeid%TYPE
                                                ,i_User_Name IN users.username%TYPE)
  RETURN VARCHAR2 IS

   /*******************************************************************************
  * FUNCTION:  sf_get_Rank_By_Subtest_Score
  * PURPOSE:   To implement the ranking by different subtest and score combination in roster report
  * CREATED:   TCS  09/Oct/2013
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR     DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

  v_Score NUMBER := 0;

  CURSOR c_Get_Score
  IS

  SELECT
    CASE
    WHEN i_Score_Type = 'SS' THEN scr.SS
    WHEN i_Score_Type = 'HSE' THEN to_number(scr.HSE)
    WHEN i_Score_Type = 'PR' THEN scr.PR
    WHEN i_Score_Type = 'NCE' THEN scr.NCE
    ELSE 0
    END score
  FROM
    subtest_score_fact scr,
    (SELECT CUSTOMERID,ADMINID FROM ORGUSER_MAPPING WHERE USERNAME=i_User_Name AND ROWNUM=1) c,
    CUST_PRODUCT_LINK cust,
    assessment_dim ases
  WHERE  CUST.PRODUCTID=i_Product_Id
  AND ases.assessmentid=i_Product_Id
  AND CUST.CUSTOMERID=C.CUSTOMERID
  AND CUST.ADMINID = C.ADMINID
  AND CUST.ACTIVATION_STATUS='AC'
  AND SCR.GRADEID=i_Grade_Id
  AND scr.student_bio_id = i_Student_ID
  AND scr.adminid = C.ADMINID
  AND SCR.ASSESSMENTID=ASES.ASSESSMENTID
  AND ((to_char(scr.subtestid) = i_SubTest_Id )
     OR(i_SubTest_Id IS NULL));
BEGIN
  FOR r_Get_Score IN c_Get_Score
  LOOP
    v_Score := r_Get_Score.score;
  END LOOP;
  RETURN to_char(999 - nvl(v_Score,0), '000');
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_GET_RANK_BY_SUBTEST_SCORE;
/
