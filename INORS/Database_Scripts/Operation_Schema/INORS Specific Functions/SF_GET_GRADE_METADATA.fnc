CREATE OR REPLACE FUNCTION SF_GET_GRADE_METADATA (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                              ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                              ,p_productid IN PRODUCT.PRODUCTID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GRADE_METADATA
  * PURPOSE:   To populate the GRADE drop down
  * CREATED:   TCS  19/MAR/2014
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
  v_ProductId PRODUCT.PRODUCTID%TYPE;


CURSOR c_Get_Grade_Metadata(product_id PRODUCT.PRODUCTID%TYPE)
IS
SELECT DISTINCT GRD.GRADE_NAME,GRD.GRADEID,GRD.GRADE_SEQ
FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
     ASSESSMENT_DIM ASES,
     CUST_PRODUCT_LINK CUST,
     GRADE_DIM GRD
WHERE CUST.CUSTOMERID = p_customerid
  AND CUST.PRODUCTID = product_id
  AND CUST.PRODUCTID = ASES.PRODUCTID
  AND ASES.ASSESSMENTID = VW.ASSESSMENTID
  AND GRD.GRADEID = VW.GRADEID
  ORDER BY GRD.GRADE_SEQ  ;




CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;

 CURSOR c_Get_Product
  IS
 ( SELECT A.PRODUCTID
       FROM
       (SELECT A.PRODUCTID,
           A.PRODUCT_NAME
    FROM
        (SELECT CUST.CUSTOMERID ,
               ADM.ADMIN_YEAR,
               PDT.PRODUCTID,
               PDT.PRODUCT_NAME,
               PDT.PRODUCT_SEQ,
               DENSE_RANK() OVER (PARTITION BY CUST.CUSTOMERID ORDER BY ADM.ADMIN_YEAR DESC NULLS LAST) AS SEQ
        FROM PRODUCT PDT,
             ADMIN_DIM ADM,
             (SELECT DISTINCT ADMIN_YEAR,IS_CURRENT_ADMIN FROM ADMIN_DIM)   ADM1,
             CUST_PRODUCT_LINK CUST
        WHERE CUST.CUSTOMERID = p_customerid
          AND CUST.PRODUCTID = PDT.PRODUCTID
          AND CUST.ADMINID = ADM.ADMINID
          AND ADM.ADMIN_YEAR <=  ADM1.ADMIN_YEAR
          AND ADM1.IS_CURRENT_ADMIN = 'Y') A
          WHERE A.SEQ<=4
          ORDER BY A.SEQ,A.PRODUCT_SEQ DESC)A
        WHERE ROWNUM = 1);

 CURSOR c_Get_Product_For_Class
  IS
  SELECT PDT.PRODUCTID
    FROM ORG_PRODUCT_LINK OPRD,
         CUST_PRODUCT_LINK CUST,
         PRODUCT PDT
    WHERE OPRD.ORG_NODEID = LoggedInUserJasperOrgId
      AND OPRD.CUST_PROD_ID = CUST.CUST_PROD_ID
      AND PDT.PRODUCTID = CUST.PRODUCTID
      AND CUST.CUSTOMERID = p_customerid
      AND ROWNUM = 1 ;


BEGIN

        IF p_productid = -99 OR p_productid IS NULL THEN
            FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
                LOOP
                       v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
            END LOOP;

           IF v_OrgNodeLevel = 4 THEN
            FOR r_Get_Product_For_Class IN c_Get_Product_For_Class
               LOOP
                   v_ProductId := r_Get_Product_For_Class.PRODUCTID;
            END LOOP;
           ELSE
             FOR r_Get_Product IN c_Get_Product
               LOOP
                  v_ProductId := r_Get_Product.PRODUCTID;
            END LOOP;
           END IF;
            FOR r_Get_Grade_Metadata IN c_Get_Grade_Metadata(v_ProductId)
                LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_Metadata.GRADEID;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_Metadata.GRADE_NAME;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_Metadata.GRADE_SEQ;

                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP;
         ELSE
            FOR r_Get_Grade_Metadata IN c_Get_Grade_Metadata(p_productid)
              LOOP
                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_Metadata.GRADEID;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_Metadata.GRADE_NAME;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_Metadata.GRADE_SEQ;

                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
              END LOOP;
         END IF;

         IF  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

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

END SF_GET_GRADE_METADATA;
/
