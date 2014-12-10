CREATE OR REPLACE FUNCTION SF_GET_TEST_ADMINISTRATION (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_TEST_ADMINISTRATION
  * PURPOSE:   To populate the product (Test Adminstration) drop down
  * CREATED:   TCS  13/MAR/2014
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
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;
 
  
CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;
 
 CURSOR c_Get_Product
  IS
  SELECT PDT.PRODUCTID,
         PDT.PRODUCT_NAME,
         PDT.PRODUCT_SEQ
        FROM PRODUCT PDT,
             ADMIN_DIM ADM,
             (SELECT DISTINCT ADMIN_YEAR,IS_CURRENT_ADMIN FROM ADMIN_DIM)   ADM1,
             CUST_PRODUCT_LINK CUST
        WHERE CUST.CUSTOMERID = p_customerid
          AND CUST.PRODUCTID = PDT.PRODUCTID
          AND CUST.ADMINID = ADM.ADMINID
          AND ADM.ADMIN_YEAR <=  ADM1.ADMIN_YEAR
          AND ADM1.IS_CURRENT_ADMIN = 'Y'
        ORDER BY PDT.PRODUCT_SEQ DESC;
          
 CURSOR c_Get_Product_Specific
  IS
   SELECT PDT.PRODUCTID,
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
      ORDER BY PDT.PRODUCT_SEQ DESC;         
        
 
BEGIN

        FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
        END LOOP;
       
       IF v_OrgNodeLevel >1 THEN
        FOR r_Get_Product_Specific IN c_Get_Product_Specific
           LOOP
               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Product_Specific.PRODUCTID;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Product_Specific.PRODUCT_NAME;              

               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
        END LOOP;
       ELSE 
         FOR r_Get_Product IN c_Get_Product
           LOOP
               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Product.PRODUCTID;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Product.PRODUCT_NAME;              

               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
        END LOOP;
       END IF;
        
RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;      
       
END SF_GET_TEST_ADMINISTRATION;
/
