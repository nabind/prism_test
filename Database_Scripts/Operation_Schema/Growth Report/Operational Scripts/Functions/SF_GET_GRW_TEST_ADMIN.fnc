CREATE OR REPLACE FUNCTION SF_GET_GRW_TEST_ADMIN(
                               LoggedInUserId IN USERS.USERID%TYPE
                              ,LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                              ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_UserId IN USERS.USERID%TYPE                              
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GRW_TEST_ADMIN
  * PURPOSE:   To populate the products  drop down
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
  v_UserId  USERS.USERID%TYPE;
  v_User_Level ORG_USERS.ORG_NODE_LEVEL%TYPE;
  v_School_Orgid ORG_NODE_DIM.ORG_NODEID%TYPE;



CURSOR c_Get_Test_Admin (user_id USERS.USERID%TYPE)
IS 
SELECT A.PRODUCT_NAME_1||' '||A.PREV_ADMIN||'-'||A.CURR_ADMIN AS PRODUCT_NAME,
       A.PRODUCTID,
       A.PRODUCT_SEQ
FROM 
( 
SELECT DISTINCT PDT.PRODUCT_NAME ,
                SUBSTR (PDT.PRODUCT_NAME,1,LENGTH (PDT.PRODUCT_NAME)-4) AS PRODUCT_NAME_1,
                ADM.ADMIN_YEAR-1 AS PREV_ADMIN,
                SUBSTR(ADM.ADMIN_YEAR,3,2) AS CURR_ADMIN,  
                PDT.PRODUCTID,
                PDT.PRODUCT_SEQ
  FROM USER_SELECTION_LOOKUP USL,
       CUST_PRODUCT_LINK CPL,
       PRODUCT PDT,
       ADMIN_DIM ADM
  WHERE USL.USERID=user_id
    AND USL.CUST_PROD_ID = CPL.CUST_PROD_ID
    AND CPL.CUSTOMERID =p_customerid
    AND CPL.PRODUCTID = PDT.PRODUCTID
    AND CPL.ADMINID = ADM.ADMINID) A
 ORDER BY A.PRODUCT_SEQ  DESC;



 CURSOR c_Get_Principal_User_Default (p_Schoolid1 ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT A.NORMAL_GRW_USERID
  FROM 
  (SELECT DISTINCT PSL.NORMAL_GRW_USER_SPN ,
         PSL.NORMAL_GRW_USERID
  FROM MV_PRINCIPAL_USER_SEL_LOOKUP PSL
  WHERE PSL.PRINCIPAL_USERID=LoggedInUserId
    AND PSL.CUSTOMERID = p_customerid
    AND PSL.ORG_NODEID = p_Schoolid1
  ORDER BY PSL.NORMAL_GRW_USER_SPN ,
           PSL.NORMAL_GRW_USERID ) A
  WHERE ROWNUM = 1 ;
  
    
  CURSOR c_Get_Lvl2_Prncpl_User_Default (p_Schoolid ORG_NODE_DIM.ORG_NODEID%TYPE)
    IS 
    SELECT A.NORMAL_GRW_USERID
     FROM          
   (SELECT DISTINCT PSL.NORMAL_GRW_USER_SPN ,
         PSL.NORMAL_GRW_USERID
  FROM MV_LVL2_PRCPL_USER_SEL_LOOKUP PSL
  WHERE PSL.DISTRICT_PRINCIPAL_USERID=LoggedInUserId
    AND PSL.CUSTOMERID = p_customerid
    AND PSL.SCHOOL_ORG_NODEID = p_Schoolid
  ORDER BY PSL.NORMAL_GRW_USER_SPN ,
           PSL.NORMAL_GRW_USERID) A
  WHERE ROWNUM=1; 
                  
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

                
        IF p_school = -99 OR p_UserId = -99 THEN 
          
           IF v_User_Type = v_PRINCIPAL_USER_TYPE THEN
             ---get the user level
              FOR r_Get_User_Level IN c_Get_User_Level
                LOOP
                     v_User_Level := r_Get_User_Level.ORG_NODE_LEVEL;
                END LOOP;  
              ---get the first default user in the list
              IF v_User_Level= 3 THEN
                ---get the first school level principal user in the list 
                FOR r_Get_Principal_User_Default IN c_Get_Principal_User_Default (LoggedInUserJasperOrgId)
                   LOOP
                       v_UserId := r_Get_Principal_User_Default.NORMAL_GRW_USERID;
                END LOOP;
              ELSE 
               ---get the first default school in the list
                    FOR r_Get_School_Name_For_Lvl2 IN c_Get_School_Name_For_Lvl2
                       LOOP
                          v_School_Orgid:= r_Get_School_Name_For_Lvl2.ORG_NODEID;
                     END LOOP; 
                ---get the first district level principal user in the list 
                FOR r_Get_Lvl2_Prncpl_User_Default IN c_Get_Lvl2_Prncpl_User_Default (v_School_Orgid)
                   LOOP
                       v_UserId := r_Get_Lvl2_Prncpl_User_Default.NORMAL_GRW_USERID;
                END LOOP;
              END IF;  
           ELSE 
                     v_UserId := LoggedInUserId; 
           END IF;   
        ELSE 
              v_UserId := p_UserId;
        END IF;      
                 
        ---get the products list 
        FOR r_Get_Test_Admin IN c_Get_Test_Admin(v_UserId)
           LOOP
           t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Test_Admin.PRODUCTID;
           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Test_Admin.PRODUCT_NAME;
           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Test_Admin.PRODUCT_SEQ;

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

END SF_GET_GRW_TEST_ADMIN;
/
