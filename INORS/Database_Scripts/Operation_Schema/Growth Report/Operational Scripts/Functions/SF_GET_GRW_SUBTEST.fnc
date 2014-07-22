CREATE OR REPLACE FUNCTION SF_GET_GRW_SUBTEST(
                               LoggedInUserId IN USERS.USERID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                              ,p_UserId IN USERS.USERID%TYPE
                              ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                              ,p_grade IN GRADE_DIM.GRADEID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GRW_SUBTEST
  * PURPOSE:   To populate the SUBTEST drop down
  * CREATED:   TCS  26/JUN/2014
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR :PARTHA(353639)    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();

  v_PRINCIPAL_USER_TYPE CONSTANT  VARCHAR2(10):='GRW_P';
  v_NORMAL_USER_TYPE CONSTANT  VARCHAR2(10):='GRW';
  v_User_Type USERS.USER_TYPE%TYPE;
  v_User_Level ORG_USERS.ORG_NODE_LEVEL%TYPE;
  v_UserId  USERS.USERID%TYPE;
  v_ProductId  PRODUCT.PRODUCTID%TYPE;
  v_3RD_GARDE  CONSTANT NUMBER := 10001;
  v_GradeId GRADE_DIM.GRADEID%TYPE;
  v_ELA_CODE CONSTANT  VARCHAR2(10):='ELA';
  v_MATH_CODE CONSTANT  VARCHAR2(10):='MATH';
  
  

CURSOR c_Get_Subtest (user_id2 USERS.USERID%TYPE,ProductId2  PRODUCT.PRODUCTID%TYPE, GradeId2 GRADE_DIM.GRADEID%TYPE)
IS
 SELECT DISTINCT SUB.SUBTEST_NAME,
                SUB.SUBTESTID,
                SUB.SUBTEST_SEQ
  FROM USER_SELECTION_LOOKUP USL,
       CUST_PRODUCT_LINK CPL,
       PRODUCT PDT,
       SUBTEST_DIM SUB
  WHERE USL.USERID=user_id2
    AND USL.CUST_PROD_ID = CPL.CUST_PROD_ID
    AND CPL.CUSTOMERID =p_customerid
    AND CPL.PRODUCTID = PDT.PRODUCTID
    AND USL.GRADEID = GradeId2
    AND CPL.PRODUCTID = ProductId2
    AND USL.GRADEID <>v_3RD_GARDE
    AND USL.SUBTESTID = SUB.SUBTESTID
    AND SUB.SUBTEST_CODE IN (v_ELA_CODE,v_MATH_CODE)
    ORDER BY SUB.SUBTEST_SEQ;

CURSOR c_Get_Grade_Default (user_id1 USERS.USERID%TYPE,ProductId1  PRODUCT.PRODUCTID%TYPE)
IS
 SELECT A.GRADEID
 FROM 
 (SELECT DISTINCT GRD.GRADE_NAME,
                GRD.GRADEID,
                GRD.GRADE_SEQ
  FROM USER_SELECTION_LOOKUP USL,
       CUST_PRODUCT_LINK CPL,
       PRODUCT PDT,
       GRADE_DIM GRD
  WHERE USL.USERID=user_id1
    AND USL.CUST_PROD_ID = CPL.CUST_PROD_ID
    AND CPL.CUSTOMERID =p_customerid
    AND CPL.PRODUCTID = PDT.PRODUCTID
    AND USL.GRADEID = GRD.GRADEID
    AND CPL.PRODUCTID = ProductId1
    AND GRD.GRADEID <>v_3RD_GARDE
    ORDER BY GRD.GRADE_SEQ)A
    WHERE ROWNUM=1;


CURSOR c_Get_Test_Admin_Deafult (user_id USERS.USERID%TYPE)
IS
SELECT A.PRODUCTID
FROM
(
SELECT DISTINCT PDT.PRODUCTID,
                PDT.PRODUCT_SEQ
  FROM USER_SELECTION_LOOKUP USL,
       CUST_PRODUCT_LINK CPL,
       PRODUCT PDT
  WHERE USL.USERID=user_id
    AND USL.CUST_PROD_ID = CPL.CUST_PROD_ID
    AND CPL.CUSTOMERID =p_customerid
    AND CPL.PRODUCTID = PDT.PRODUCTID
    ORDER BY PDT.PRODUCT_SEQ DESC) A
 WHERE ROWNUM = 1;



 CURSOR c_Get_Principal_User_Default
  IS
  SELECT A.NORMAL_GRW_USERID
  FROM
  (SELECT DISTINCT PSL.NORMAL_GRW_USER_SPN ,
         PSL.NORMAL_GRW_USERID
  FROM MV_PRINCIPAL_USER_SEL_LOOKUP PSL
  WHERE PSL.PRINCIPAL_USERID=LoggedInUserId
    AND PSL.CUSTOMERID = p_customerid
  ORDER BY PSL.NORMAL_GRW_USER_SPN ,
           PSL.NORMAL_GRW_USERID) A
  WHERE ROWNUM = 1 ;


CURSOR c_Get_Lvl2_Prncpl_User_Default
    IS 
    SELECT A.NORMAL_GRW_USERID
     FROM          
   (SELECT DISTINCT PSL.NORMAL_GRW_USER_SPN ,
         PSL.NORMAL_GRW_USERID
    FROM MV_LVL2_PRCPL_USER_SEL_LOOKUP PSL
    WHERE PSL.DISTRICT_PRINCIPAL_USERID=LoggedInUserId
      AND PSL.CUSTOMERID = p_customerid
    ORDER BY PSL.NORMAL_GRW_USER_SPN ,
             PSL.NORMAL_GRW_USERID) A
  WHERE ROWNUM=1; 
  

 CURSOR c_Get_Grwth_User_Type
 IS
 SELECT USER_TYPE
 FROM USERS
 WHERE USER_TYPE IN(v_PRINCIPAL_USER_TYPE,v_NORMAL_USER_TYPE)
 AND USERID =LoggedInUserId
 AND CUSTOMERID = p_customerid;
 
 CURSOR c_Get_User_Level
 IS
 SELECT ORG_NODE_LEVEL FROM ORG_USERS   
 WHERE USERID =LoggedInUserId;
 
 

BEGIN
       ---get the user type
        FOR r_Get_Grwth_User_Type IN c_Get_Grwth_User_Type
            LOOP
                   v_User_Type := r_Get_Grwth_User_Type.USER_TYPE;
        END LOOP;


        IF p_grade = -99 OR p_UserId = -99 THEN

           IF v_User_Type = v_PRINCIPAL_USER_TYPE THEN
              ---get the user level
              FOR r_Get_User_Level IN c_Get_User_Level
                LOOP
                     v_User_Level := r_Get_User_Level.ORG_NODE_LEVEL;
                END LOOP;  
              ---get the first default user in the list
              IF v_User_Level= 3 THEN
               ---get the first school level principal user in the list 
                FOR r_Get_Principal_User_Default IN c_Get_Principal_User_Default
                   LOOP
                       v_UserId := r_Get_Principal_User_Default.NORMAL_GRW_USERID;
                END LOOP;
              ELSE 
                ---get the first district level principal user in the list 
                FOR r_Get_Lvl2_Prncpl_User_Default IN c_Get_Lvl2_Prncpl_User_Default
                   LOOP
                       v_UserId := r_Get_Lvl2_Prncpl_User_Default.NORMAL_GRW_USERID;
                END LOOP;
              END IF;  
           ELSE
                     v_UserId := LoggedInUserId;
           END IF;
           ---get the default product
            FOR r_Get_Test_Admin_Deafult IN c_Get_Test_Admin_Deafult(v_UserId)
             LOOP
              v_ProductId := r_Get_Test_Admin_Deafult.PRODUCTID;
            END LOOP;
            ---get the default grade 
            FOR r_Get_Grade_Default IN c_Get_Grade_Default(v_UserId,v_ProductId)
             LOOP
              v_GradeId := r_Get_Grade_Default.GRADEID;
            END LOOP;

        ELSE
              v_UserId := p_UserId;
              v_ProductId := p_test_administration;
              v_GradeId := p_grade;
        END IF;

        ---get the subtests list
        FOR r_Get_Subtest IN c_Get_Subtest(v_UserId,v_ProductId,v_GradeId)
           LOOP
           t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest.SUBTESTID;
           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest.SUBTEST_NAME;
           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest.SUBTEST_SEQ;

           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
        END LOOP;


       IF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -2;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'None Available';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 :=-2;


            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
     END IF;


RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;

END SF_GET_GRW_SUBTEST;
/
