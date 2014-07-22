CREATE OR REPLACE FUNCTION SF_GET_GRW_SCHOOL (
                               LoggedInUserId IN USERS.USERID%TYPE
                               ,LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_TEST_USER_SPN_GRW
  * PURPOSE:   To populate the school names drop dowmn  for growth users
  * CREATED:   TCS  26/JUN/2014
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR :PARTHA    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();

  v_User_Level ORG_USERS.ORG_NODE_LEVEL%TYPE;

          
 CURSOR c_Get_School_Name_For_Lvl3
  IS
  SELECT ORG.ORG_NODE_NAME,ORG.ORG_NODEID
  FROM ORG_NODE_DIM ORG
  WHERE ORG.ORG_NODEID=LoggedInUserJasperOrgId 
    AND  ORG.CUSTOMERID = p_customerid
    AND ORG.ORG_NODE_LEVEL = 3
    ORDER BY ORG.ORG_NODE_NAME, ORG.ORG_NODEID ;
    
  CURSOR c_Get_School_Name_For_Lvl2
  IS
  SELECT ORG.ORG_NODE_NAME,ORG.ORG_NODEID
  FROM ORG_NODE_DIM ORG
  WHERE ORG.PARENT_ORG_NODEID=LoggedInUserJasperOrgId 
    AND ORG.CUSTOMERID = p_customerid
    AND ORG.ORG_NODE_LEVEL = 3
    ORDER BY ORG.ORG_NODE_NAME, ORG.ORG_NODEID;  


 CURSOR c_Get_User_Level
 IS
 SELECT ORG_NODE_LEVEL FROM ORG_USERS
 WHERE USERID =LoggedInUserId
 AND ORG_NODE_LEVEL IN (2,3);


BEGIN

        FOR r_Get_User_Level IN c_Get_User_Level
            LOOP
                 v_User_Level := r_Get_User_Level.ORG_NODE_LEVEL;
        END LOOP;
       
        IF v_User_Level= 3 THEN
          FOR r_Get_School_Name_For_Lvl3 IN c_Get_School_Name_For_Lvl3
             LOOP
                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School_Name_For_Lvl3.ORG_NODEID;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School_Name_For_Lvl3.ORG_NODE_NAME;

                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
           END LOOP;
        ELSE
          FOR r_Get_School_Name_For_Lvl2 IN c_Get_School_Name_For_Lvl2
             LOOP
                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School_Name_For_Lvl2.ORG_NODEID;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School_Name_For_Lvl2.ORG_NODE_NAME;

                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
           END LOOP;
         END IF;
       

       IF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -2;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'None Available';

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
     END IF;

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;

END SF_GET_GRW_SCHOOL;
/
