CREATE OR REPLACE FUNCTION SF_GET_GENERATE_FILE(p_test_administration IN PRODUCT.PRODUCTID%TYPE,
                                                p_customerid          IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                                p_school              IN org_node_dim.org_nodeid%type)
  RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GENERATE_FILE
  * PURPOSE:   To GET DOWNLAOD FILE TYPES
  * CREATED:   TCS  19/FEB/2014
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR :353639    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

  PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ      PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();
  -- v_CustomerId CONSTANT  NUMBER :=1000;
  v_ProductCode PRODUCT.PRODUCT_CODE%TYPE;

  CURSOR c_Get_File_Type(ip_product_type VARCHAR) IS
    SELECT COLUMN_VALUE FILE_CODE,
           DECODE(COLUMN_VALUE,
                  'ISR',
                  'Individual Student Report (ISR)',
                  'IPR',
                  'Image Print',
                  'BOTH',
                  'Both (ISR and Image Print)',
                  'Invitation Letter') AS FILE_NAME
      FROM TABLE(dyn_attr_dtls('ISR', 'IPR', 'BOTH', 'ICL'))
     WHERE 'ISTEP' = ip_product_type
    UNION ALL
    SELECT COLUMN_VALUE FILE_CODE,
           DECODE(COLUMN_VALUE,
                  'ISR',
                  'Individual Student Report (ISR)' /*,'Invitation Letter'*/) AS FILE_NAME
      FROM TABLE(dyn_attr_dtls('ISR' /*,'ICL'*/))
     WHERE 'ISTEP' <> ip_product_type;

  CURSOR c_Get_ProductCode_Default IS
    SELECT B.PRODUCT_CODE
      FROM (SELECT SUBSTR(PDT.PRODUCT_CODE, 1, 5) AS PRODUCT_CODE,
                   PDT.PRODUCT_NAME,
                   PDT.PRODUCT_SEQ
              FROM PRODUCT PDT,
                   ADMIN_DIM ADM,
                   (SELECT DISTINCT ADMIN_YEAR, IS_CURRENT_ADMIN
                      FROM ADMIN_DIM) ADM1,
                   CUST_PRODUCT_LINK CUST
             WHERE CUST.CUSTOMERID = p_customerid
               AND CUST.PRODUCTID = PDT.PRODUCTID
               AND CUST.ADMINID = ADM.ADMINID
               AND ADM.ADMIN_YEAR <= ADM1.ADMIN_YEAR
               AND ADM1.IS_CURRENT_ADMIN = 'Y'
             ORDER BY PDT.PRODUCT_SEQ DESC) B
     WHERE ROWNUM = 1;

  CURSOR c_Get_ProductCode_Cascade(ip_test_administration PRODUCT.PRODUCTID%TYPE) IS(
    SELECT SUBSTR(PDT.PRODUCT_CODE, 1, 5) AS PRODUCT_CODE
      FROM PRODUCT PDT
     WHERE PDT.PRODUCTID = ip_test_administration);

BEGIN

  IF p_test_administration = -99 OR p_test_administration IS NULL THEN
    FOR r_Get_ProductCode_Default IN c_Get_ProductCode_Default LOOP
      v_ProductCode := r_Get_ProductCode_Default.PRODUCT_CODE;
    END LOOP;
  ELSE
    FOR r_Get_ProductCode_Cascade IN c_Get_ProductCode_Cascade(p_test_administration) LOOP
      v_ProductCode := r_Get_ProductCode_Cascade.PRODUCT_CODE;
    END LOOP;
  END IF;

  FOR r_Get_File_Type IN c_Get_File_Type(v_ProductCode) LOOP
    t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
  
    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_File_Type.FILE_CODE;
    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_File_Type.FILE_NAME;
  
    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT) := t_PRS_PGT_GLOBAL_TEMP_OBJ;
  END LOOP;

  RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_GET_GENERATE_FILE;
/
