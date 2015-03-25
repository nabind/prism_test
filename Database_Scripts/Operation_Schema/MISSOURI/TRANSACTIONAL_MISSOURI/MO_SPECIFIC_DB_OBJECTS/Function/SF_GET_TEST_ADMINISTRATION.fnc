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



 CURSOR c_Get_Product_Specific
  IS
   SELECT CUST.CUST_PROD_ID,
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


        FOR r_Get_Product_Specific IN c_Get_Product_Specific
           LOOP
               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Product_Specific.CUST_PROD_ID;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Product_Specific.PRODUCT_NAME;

               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
        END LOOP;



RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;

END SF_GET_TEST_ADMINISTRATION;
/
