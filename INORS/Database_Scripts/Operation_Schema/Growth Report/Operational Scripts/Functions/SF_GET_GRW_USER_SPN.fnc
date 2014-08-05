CREATE OR REPLACE FUNCTION SF_GET_GRW_USER_SPN (
                               LoggedInUserId IN USERS.USERID%TYPE
                               ,LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                              ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_TEST_USER_SPN_GRW
  * PURPOSE:   To populate the growth user SPNs for (Teacher SPN) drop down
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

  v_PRINCIPAL_USER_TYPE CONSTANT  VARCHAR2(10):='GRW_P';
  v_NORMAL_USER_TYPE CONSTANT  VARCHAR2(10):='GRW';
  v_User_Type USERS.USER_TYPE%TYPE;
  v_User_Level ORG_USERS.ORG_NODE_LEVEL%TYPE;
  v_School_Orgid ORG_NODE_DIM.ORG_NODEID%TYPE;

 CURSOR c_Get_Principal_User (p_Schoolid1 ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT DISTINCT PSL.NORMAL_GRW_USER_SPN ,
         PSL.NORMAL_GRW_USERID
  FROM MV_PRINCIPAL_USER_SEL_LOOKUP PSL
  WHERE PSL.PRINCIPAL_USERID=LoggedInUserId
    AND PSL.CUSTOMERID = p_customerid
    AND PSL.ORG_NODEID = p_Schoolid1
  ORDER BY PSL.NORMAL_GRW_USER_SPN ,
           PSL.NORMAL_GRW_USERID ;
           
   CURSOR c_Get_Lvl2_Principal_User (p_Schoolid ORG_NODE_DIM.ORG_NODEID%TYPE)
    IS          
   SELECT DISTINCT PSL.NORMAL_GRW_USER_SPN ,
         PSL.NORMAL_GRW_USERID
  FROM MV_LVL2_PRCPL_USER_SEL_LOOKUP PSL
  WHERE PSL.DISTRICT_PRINCIPAL_USERID=LoggedInUserId
    AND PSL.CUSTOMERID = p_customerid
    AND PSL.SCHOOL_ORG_NODEID = p_Schoolid
  ORDER BY PSL.NORMAL_GRW_USER_SPN ,
           PSL.NORMAL_GRW_USERID; 
           
 CURSOR c_Get_School_Name_For_Lvl2
  IS
   SELECT A.ORG_NODEID FROM
  (SELECT ORG.ORG_NODE_NAME,ORG.ORG_NODEID
  FROM ORG_NODE_DIM ORG
  WHERE ORG.PARENT_ORG_NODEID=LoggedInUserJasperOrgId 
    AND ORG.CUSTOMERID = p_customerid
    AND ORG.ORG_NODE_LEVEL = 3
    ORDER BY ORG.ORG_NODE_NAME, ORG.ORG_NODEID)A
    WHERE ROWNUM=1;            
           
                  
           
  CURSOR c_Get_Normal_User
  IS
     SELECT SUBSTR(USERNAME,1,8) AS NORMAL_GRW_USER_SPN,
            USERID AS NORMAL_GRW_USERID
     FROM USERS
     WHERE USERID =LoggedInUserId
     AND USER_TYPE = v_NORMAL_USER_TYPE
     AND CUSTOMERID = p_customerid;          

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

        FOR r_Get_Grwth_User_Type IN c_Get_Grwth_User_Type
            LOOP
                   v_User_Type := r_Get_Grwth_User_Type.USER_TYPE;
        END LOOP;
                  
        
        IF v_User_Type = v_PRINCIPAL_USER_TYPE THEN
           FOR r_Get_User_Level IN c_Get_User_Level
            LOOP
                 v_User_Level := r_Get_User_Level.ORG_NODE_LEVEL;
            END LOOP;
             IF v_User_Level= 3 THEN 
                FOR r_Get_Principal_User IN c_Get_Principal_User (LoggedInUserJasperOrgId)
                  LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Principal_User.NORMAL_GRW_USERID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Principal_User.NORMAL_GRW_USER_SPN;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP;
               ELSE
                IF p_School=-99 THEN
                     ---get the first default school in the list
                    FOR r_Get_School_Name_For_Lvl2 IN c_Get_School_Name_For_Lvl2
                       LOOP
                          v_School_Orgid:= r_Get_School_Name_For_Lvl2.ORG_NODEID;
                     END LOOP; 
                ELSE 
                    v_School_Orgid:= p_School;
                END IF;
                         
                FOR r_Get_Lvl2_Principal_User IN c_Get_Lvl2_Principal_User(v_School_Orgid)
                   LOOP
                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Lvl2_Principal_User.NORMAL_GRW_USERID;
                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Lvl2_Principal_User.NORMAL_GRW_USER_SPN;

                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                 END LOOP;
             END IF;                                                             
         ELSE 
               FOR r_Get_Normal_User IN c_Get_Normal_User
                 LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Normal_User.NORMAL_GRW_USERID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Normal_User.NORMAL_GRW_USER_SPN;

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

END SF_GET_GRW_USER_SPN;
/
