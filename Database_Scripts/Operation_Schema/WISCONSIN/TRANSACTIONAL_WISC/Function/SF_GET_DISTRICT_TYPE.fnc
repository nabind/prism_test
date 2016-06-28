CREATE OR REPLACE FUNCTION SF_GET_DISTRICT_TYPE (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ

IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_DISTRICT_TYPE
  * PURPOSE:   To populate District Type drop down
  * CREATED:   TCS  06/JUN/2016
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
  
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;


 CURSOR c_Get_District_Type_For_State
  IS
   SELECT 'PUBLIC' AS ID,'Public (Default)' AS DIST_TYPE FROM DUAL
   UNION ALL
   SELECT 'PRIVATE' AS ID,'Private' AS DIST_TYPE FROM DUAL
   UNION ALL
   SELECT 'CHOICE' AS ID,'Choice' AS DIST_TYPE FROM DUAL
   ORDER BY 1;

 CURSOR c_Get_District_Type_For_Other
  IS
    SELECT ORG_MODE AS ID,
           DECODE(ORG_MODE,'PUBLIC','Public','PRIVATE','Private','Choice') AS DIST_TYPE
    FROM ORG_NODE_DIM WHERE ORG_NODEID = LoggedInUserJasperOrgId;



BEGIN
       ----get the org_node_level
      SELECT ORG.ORG_NODE_LEVEL INTO v_OrgNodeLevel FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;
  
      IF v_OrgNodeLevel = 1 THEN 
        FOR r_Get_District_Type_For_State IN c_Get_District_Type_For_State
           LOOP
               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_District_Type_For_State.ID;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_District_Type_For_State.DIST_TYPE;

               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
        END LOOP;
      ELSE
          FOR r_Get_District_Type_For_Other IN c_Get_District_Type_For_Other
           LOOP
               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_District_Type_For_Other.ID;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_District_Type_For_Other.DIST_TYPE;

               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
         END LOOP;
      END IF;
        



RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;

END SF_GET_DISTRICT_TYPE;
/
