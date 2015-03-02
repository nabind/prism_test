CREATE OR REPLACE FUNCTION SF_GET_SUBTEST (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_district IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_grade IN GRADE_DIM.GRADEID%TYPE
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_SUBTEST
  * PURPOSE:   To GET different subtests
  * CREATED:   TCS  20/FEB/2015
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
  
  v_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;
  v_district  ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_school  ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_grade  GRADE_DIM.GRADEID%TYPE;

 CURSOR c_Get_Subtest (p_Cust_Product_Id_3 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,p_gradeid GRADE_DIM.GRADEID%TYPE)
  IS
  SELECT DISTINCT SUB.SUBTESTID,
                SUB.SUBTEST_NAME, 
                SUB.SUBTEST_SEQ
      FROM SUBTEST_OBJECTIVE_MAP SOM,
           GRADE_LEVEL_MAP GLM,
           CUST_PRODUCT_LINK CUST,
           ASSESSMENT_DIM ASES,
           SUBTEST_DIM SUB
      WHERE GLM.GRADEID = p_gradeid
      AND GLM.LEVEL_MAPID =  SOM.LEVEL_MAPID
      AND SOM.ASSESSMENTID = ASES.ASSESSMENTID
      AND CUST.CUSTOMERID = p_customerid
      AND CUST.PRODUCTID = ASES.PRODUCTID
      AND CUST.CUST_PROD_ID = p_Cust_Product_Id_3
      AND SOM.SUBTESTID =SUB.SUBTESTID
      ORDER BY SUB.SUBTEST_SEQ;
      
      
      
  CURSOR c_Get_Grade (p_Cust_Product_Id_2 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,org_id ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
    SELECT A.GRADEID
    FROM 
     (SELECT  DISTINCT  GRD.GRADEID,
       GRD.GRADE_NAME,
       GRD.GRADE_SEQ
  FROM GRADE_SELECTION_LOOKUP GSL,
       CUST_PRODUCT_LINK CUST,
       ASSESSMENT_DIM ASSD,
       GRADE_DIM GRD,
       ORG_PRODUCT_LINK OLNK
  WHERE  GSL.ADMINID = CUST.ADMINID
     AND GSL.ORG_NODEID = org_id
     AND CUST.CUSTOMERID = p_customerid
     AND CUST.CUST_PROD_ID = OLNK.CUST_PROD_ID
     AND GSL.ORG_NODEID=OLNK.ORG_NODEID
     AND cust.productid = assd.productid
     AND ASSD.ASSESSMENTID = GSL.ASSESSMENTID
     AND CUST.CUST_PROD_ID= p_Cust_Product_Id_2
     AND GRD.GRADEID = GSL.GRADEID
  ORDER BY GRD.GRADE_SEQ)A
  WHERE ROWNUM=1;

  
  CURSOR c_Get_School (p_Cust_Product_Id_1 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,p_District_Id ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT A.ORG_NODEID FROM 
  (SELECT OND.ORG_NODEID, OND.ORG_NODE_CODE||' '||OND.ORG_NODE_NAME AS ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.CUST_PROD_ID = p_Cust_Product_Id_1
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_NODEID = ORPD.ORG_NODEID
   AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = p_District_Id)
        OR (v_OrgNodeLevel=2 AND OND.PARENT_ORG_NODEID = p_District_Id)
        OR (v_OrgNodeLevel=3  AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
        )
   AND OND.ORG_NODE_LEVEL = 3
 ORDER BY 2)A
 WHERE ROWNUM=1;
  
  
  
  CURSOR c_Get_District (p_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE)
  IS
    SELECT A.ORG_NODEID FROM
    (SELECT  OND.ORG_NODEID, OND.ORG_NODE_CODE||' '||OND.ORG_NODE_NAME AS ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.CUST_PROD_ID = p_Cust_ProductId
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND ORPD.ORG_NODEID = OND.ORG_NODEID
   AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = LoggedInUserJasperOrgId)
        OR (v_OrgNodeLevel=2 AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
        OR (v_OrgNodeLevel <> 1
            AND v_OrgNodeLevel <>2
            AND OND.ORG_NODEID = (SELECT DISTINCT ORG.ORG_NODEID
                                FROM  ORG_NODE_DIM ORG
                                WHERE ORG.ORG_NODE_LEVEL= 2
                                START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId
                                CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)))
   AND OND.ORG_NODE_LEVEL = 2
 ORDER BY 2) A
   WHERE ROWNUM=1;
 
   
  CURSOR c_Get_Product_Specific
  IS
   SELECT  B.CUST_PROD_ID
    FROM
 (SELECT CUST.CUST_PROD_ID,
          PDT.PRODUCT_NAME,
          PDT.PRODUCT_SEQ
    FROM ORG_PRODUCT_LINK OPRD,
         CUST_PRODUCT_LINK CUST,
         PRODUCT PDT,
         ADMIN_DIM ADM,
        (SELECT DISTINCT ADMIN_YEAR,IS_CURRENT_ADMIN FROM ADMIN_DIM)   ADM1
    WHERE OPRD.ORG_NODEID =LoggedInUserJasperOrgId
      AND OPRD.CUST_PROD_ID = CUST.CUST_PROD_ID
      AND PDT.PRODUCTID = CUST.PRODUCTID
      AND CUST.CUSTOMERID = p_customerid
      AND CUST.ADMINID = ADM.ADMINID
      AND ADM.ADMIN_YEAR <=  ADM1.ADMIN_YEAR
      AND ADM1.IS_CURRENT_ADMIN = 'Y'
      ORDER BY PDT.PRODUCT_SEQ DESC)B
      WHERE ROWNUM = 1;

 CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;


  BEGIN

      FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
       END LOOP;

      IF (p_grade = -99) OR (p_grade IS NULL) THEN
          -----get  default product
         FOR r_Get_Product_Specific IN c_Get_Product_Specific
         LOOP
                v_Cust_ProductId := r_Get_Product_Specific.CUST_PROD_ID;
         END LOOP;
          -----get default district
         FOR r_Get_District IN c_Get_District (v_Cust_ProductId)
         LOOP
                 v_district := r_Get_District.ORG_NODEID;
         END LOOP;
          -----get default school
         FOR r_Get_School IN c_Get_School (v_Cust_ProductId,v_district)
         LOOP
                 v_school := r_Get_School.ORG_NODEID;
         END LOOP;
          -----get default grade
          
          --UNCOMMENT THE BELOW IF-FOR CONDITION IF THERE IS A "ALL" OPTION IN THE SCHOOL DROP DOWN
         /*IF (v_district IS NOT NULL) AND (v_school IS NOT NULL) THEN
          FOR r_Get_Grade IN c_Get_Grade (v_Cust_ProductId,v_district)*/---in default case send disrtict id since school will show "All(-1)" option
        
          FOR r_Get_Grade IN c_Get_Grade (v_Cust_ProductId,v_school) 
          LOOP
               v_grade := r_Get_Grade.GRADEID;
          END LOOP;
         --END IF; 
          -----get default subtest
          FOR r_Get_Subtest IN c_Get_Subtest (v_Cust_ProductId,v_grade)
          LOOP
              t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest.SUBTESTID;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest.SUBTEST_NAME;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest.SUBTEST_SEQ;

              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP;
      ELSE
          FOR r_Get_Subtest IN c_Get_Subtest (p_test_administration,p_grade)
          LOOP
              t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest.SUBTESTID;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest.SUBTEST_NAME;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest.SUBTEST_SEQ;

              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP;
     END IF;
     
      IF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

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
    RETURN NULL;
END SF_GET_SUBTEST;
/
