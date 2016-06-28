CREATE OR REPLACE FUNCTION SF_GET_GRADE (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_district_type IN VARCHAR2
                             ,p_district IN VARCHAR2
                             ,p_school IN VARCHAR2
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_is_summary IN VARCHAR2
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GRADE
  * PURPOSE:   To GET different grades
  * CREATED:   TCS  02/JUN/2016
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

  v_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;
  v_district  ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_school  ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_District_Type VARCHAR2(10);
  
  
 CURSOR c_Get_Grade (p_Cust_Product_Id_3 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,org_id_1 ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS

     SELECT  DISTINCT  GRD.GRADEID,
       GRD.GRADE_NAME,
       GRD.GRADE_SEQ
  FROM GRADE_SELECTION_LOOKUP GSL,
       CUST_PRODUCT_LINK CUST,
       ASSESSMENT_DIM ASSD,
       GRADE_DIM GRD,
       ORG_PRODUCT_LINK OLNK
  WHERE  GSL.ADMINID = CUST.ADMINID
     AND GSL.ORG_NODEID = org_id_1
     AND CUST.CUSTOMERID = p_customerid
     AND CUST.CUST_PROD_ID = OLNK.CUST_PROD_ID
     AND GSL.ORG_NODEID=OLNK.ORG_NODEID
     AND cust.productid = assd.productid
     AND ASSD.ASSESSMENTID = GSL.ASSESSMENTID
     AND CUST.CUST_PROD_ID= p_Cust_Product_Id_3
     AND GRD.GRADEID = GSL.GRADEID
  ORDER BY GRD.GRADE_SEQ;


 CURSOR c_Get_Grade_Deafult (p_Cust_Product_Id_2 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,org_id ORG_NODE_DIM.ORG_NODEID%TYPE,district_type_3 VARCHAR2)
  IS
     SELECT  DISTINCT  GRD.GRADEID,
       GRD.GRADE_NAME,
       GRD.GRADE_SEQ
  FROM GRADE_ORG_MODE_MAP GMODE,
       CUST_PRODUCT_LINK CUST,
       ASSESSMENT_DIM ASSD,
       GRADE_DIM GRD,
       ORG_PRODUCT_LINK OLNK
  WHERE  GMODE.ADMINID = CUST.ADMINID
     AND GMODE.ORG_NODEID = org_id
     AND CUST.CUSTOMERID = p_customerid
     AND CUST.CUST_PROD_ID = OLNK.CUST_PROD_ID
     AND GMODE.ORG_NODEID=OLNK.ORG_NODEID
     AND cust.productid = assd.productid
     AND ASSD.ASSESSMENTID = GMODE.ASSESSMENTID
     AND CUST.CUST_PROD_ID= p_Cust_Product_Id_2
     AND GRD.GRADEID = GMODE.GRADEID
     AND GMODE.ORG_MODE = district_type_3
  ORDER BY GRD.GRADE_SEQ;
  

CURSOR c_Get_School (p_Cust_Product_Id_1 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,p_District_Id ORG_NODE_DIM.ORG_NODEID%TYPE,district_type_2 VARCHAR2)
  IS
  SELECT A.ORG_NODEID FROM
  (SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME||' '||OND.ORG_NODE_CODE AS ORG_NODE_NAME
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
   AND OND.ORG_MODE = district_type_2
 ORDER BY 2)A
 WHERE ROWNUM=1;



  CURSOR c_Get_District (p_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,district_type_1 VARCHAR2)
  IS
    SELECT A.ORG_NODEID FROM
    (SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME||' '||OND.ORG_NODE_CODE AS ORG_NODE_NAME
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
   AND OND.ORG_MODE = district_type_1
 ORDER BY 2) A
   WHERE ROWNUM=1;



CURSOR c_Get_District_Type_Default
  IS
  SELECT ORG_MODE FROM ORG_NODE_DIM WHERE ORG_NODEID = LoggedInUserJasperOrgId AND ORG_NODE_LEVEL<>1
  UNION ALL
  SELECT 'PUBLIC' AS ORG_MODE FROM ORG_NODE_DIM WHERE ORG_NODEID = LoggedInUserJasperOrgId AND ORG_NODE_LEVEL=1;
   


CURSOR c_Get_Product_Specific
  IS
   SELECT  B.CUST_PROD_ID,
           B.ADMIN_NAME
   FROM
 (SELECT CUST.CUST_PROD_ID,
          ADM.ADMIN_NAME,
          ADM.ADMIN_SEQ
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
      ORDER BY ADM.ADMIN_SEQ DESC)B
      WHERE ROWNUM = 1;
      
CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;

BEGIN
       FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
       END LOOP;


       IF  ( p_district = -99 OR p_district IS NULL ) AND ( p_school = -99   OR  p_school IS NULL)  THEN
          -----get  default product
         FOR r_Get_Product_Specific IN c_Get_Product_Specific
         LOOP
                v_Cust_ProductId := r_Get_Product_Specific.CUST_PROD_ID;
         END LOOP;
         
         ---to get default district type
             FOR r_Get_District_Type_Default IN c_Get_District_Type_Default
               LOOP
                    v_District_Type := r_Get_District_Type_Default.ORG_MODE;
             END LOOP;
          IF  p_is_summary <> 1 THEN    
               -----get default district
             FOR r_Get_District IN c_Get_District (v_Cust_ProductId,v_District_Type)
             LOOP
                     v_district := r_Get_District.ORG_NODEID;
             END LOOP;
              -----get default school
             FOR r_Get_School IN c_Get_School (v_Cust_ProductId,v_district,v_District_Type)
             LOOP
                     v_school := r_Get_School.ORG_NODEID;
             END LOOP;
             
             -----get default grade FOR ROSTER
              FOR r_Get_Grade_Deafult IN c_Get_Grade_Deafult (v_Cust_ProductId,v_school,v_District_Type)
              LOOP
                  t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_Deafult.GRADEID;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_Deafult.GRADE_NAME;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_Deafult.GRADE_SEQ;

                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
              END LOOP;
         ELSE
          -----get default grade FOR SUMMARY
          FOR r_Get_Grade_Deafult IN c_Get_Grade_Deafult (v_Cust_ProductId,LoggedInUserJasperOrgId,v_District_Type)
          LOOP
              t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_Deafult.GRADEID;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_Deafult.GRADE_NAME;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_Deafult.GRADE_SEQ;

              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP;       
         END IF;       
         
      ------for ALL option in both district and school   
      ELSIF  (p_district = -1 AND  p_school = -1 AND p_is_summary = 1) THEN
          FOR r_Get_Grade_Deafult IN c_Get_Grade_Deafult (p_test_administration,LoggedInUserJasperOrgId,p_district_type)
            LOOP
                t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_Deafult.GRADEID;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_Deafult.GRADE_NAME;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_Deafult.GRADE_SEQ;

                t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP; 
      ------for ALL option in school     
      ELSIF (p_district <> -1 AND  p_school = -1 AND p_is_summary = 1) THEN        
           FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_district)
          LOOP
              t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
              t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP;  
           
      ELSE 
          FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_school)
          LOOP
              t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
              t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

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
  RAISE;
    RETURN NULL;
END SF_GET_GRADE;
/
