CREATE OR REPLACE FUNCTION SF_GET_TEST_PRGM (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE                        )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_TEST_PRGM
  * PURPOSE:   To GET  TEST PROGRAM
  * CREATED:   TCS  14/DEC/2013
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
  -- v_CUSTID CONSTANT NUMBER :=1000;
 -- v_TP_NONPUBLIC CONSTANT  VARCHAR2(10):='NON-PUBLIC';
    v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;


 CURSOR c_Get_TestPgm_State 
  IS
  SELECT A.TEST_PROGRAM_TYPE_SEQ,A.TEST_PROGRAM_TYPE FROM 
  (SELECT 1 AS Test_Program_Type_Seq , 'Public Schools' AS  Test_Program_Type FROM DUAL
    UNION ALL
    SELECT 0 AS Test_Program_Type_Seq , 'Non Public Schools' AS  Test_Program_Type FROM DUAL) A 
    ORDER BY A.TEST_PROGRAM_TYPE_SEQ DESC;

CURSOR c_Get_TestPgm_Other (p_org_nodeid ORG_NODE_DIM.ORG_NODEID%TYPE) 
  IS
  SELECT (CASE
               WHEN A.org_mode = 'PUBLIC' THEN
                1
               ELSE
                0
             END) Test_Program_Type_Seq,
             (CASE
               WHEN A.org_mode= 'PUBLIC' THEN
                'Public Schools'
               ELSE
                'Non Public Schools'
             END) Test_Program_Type
    FROM 
    (SELECT DISTINCT ORG.org_mode/*TP.TP_TYPE */
    FROM ORG_NODE_DIM ORG
    WHERE ORG.ORG_NODEID = p_org_nodeid
      AND ORG.CUSTOMERID = p_customerid
    )A 
    ORDER BY 1 DESC;




CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;

BEGIN
       FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
        END LOOP;


         IF  v_OrgNodeLevel = 1 THEN
                  FOR r_Get_TestPgm_State IN c_Get_TestPgm_State
                      LOOP
                          t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                          
                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1:= r_Get_TestPgm_State.Test_Program_Type_Seq;
                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2:= r_Get_TestPgm_State.Test_Program_Type;
                             
                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                                        
                       END LOOP;
          ELSE  
                   FOR r_Get_TestPgm_Other IN c_Get_TestPgm_Other(LoggedInUserJasperOrgId)
                      LOOP
                          t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                          
                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1:= r_Get_TestPgm_Other.Test_Program_Type_Seq;
                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2:= r_Get_TestPgm_Other.Test_Program_Type;
                             
                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                                        
                       END LOOP;         
                            
         END IF;

           

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_GET_TEST_PRGM;
/
