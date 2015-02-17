CREATE OR REPLACE FUNCTION SF_GET_GRADE_RESCORE (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_class IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_program  NUMBER
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_type IN VARCHAR )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GRADE
  * PURPOSE:   To GET different grades + "All Grades" option for Rescore Summary
  * CREATED:   TCS  18/JUN/2014
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR :353639    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();
  v_none_available NUMBER;

 CURSOR c_Get_Grade_Rescore 
  IS
  SELECT vc1 AS GRADEID ,vc2 AS GRADE_NAME, vc2 AS GRADE_SEQ
                                                   FROM TABLE (SELECT  SF_GET_GRADE (
                                                                LoggedInUserJasperOrgId
                                                               ,p_test_administration
                                                               ,p_corpdiocese
                                                               ,p_school
                                                               ,p_class
                                                               ,p_test_program
                                                               ,p_customerid
                                                               ,p_type)FROM dual)
                                                ORDER BY 3;

   
BEGIN
      

   FOR r_Get_Grade_Rescore IN c_Get_Grade_Rescore
                    LOOP
                             t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                             t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_Rescore.GRADEID;
                             t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_Rescore.GRADE_NAME;
                             t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_Rescore.GRADE_SEQ;

                             t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                             t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                             
                             IF r_Get_Grade_Rescore.GRADEID = -2 THEN
                                v_none_available:= -2;
                             ELSE 
                                 v_none_available:= 0;
                             END IF;   
   END LOOP;
   
   IF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT >=1 AND v_none_available <> -2  THEN
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'All Grades';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;
            
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

   ELSIF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0 THEN

            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -2;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'None Available';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -2;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
     END IF;

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
  --RAISE;
    RETURN NULL;
END SF_GET_GRADE_RESCORE;
/
