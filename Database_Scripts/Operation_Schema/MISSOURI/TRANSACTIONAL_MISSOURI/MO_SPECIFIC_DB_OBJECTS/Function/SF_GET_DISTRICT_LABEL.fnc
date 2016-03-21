CREATE OR REPLACE FUNCTION SF_GET_DISTRICT_LABEL (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_DISTRICT_LABEL
  * PURPOSE:   To GET different DISTRICTS
  * CREATED:   TCS  19/FEB/2015
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR:353639     DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();

  v_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;

  v_org_seq NUMBER;


 CURSOR c_Get_District (p_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE)
  IS
  SELECT  OND.ORG_NODEID, OND.ORG_NODE_CODE||' '||OND.ORG_NODE_NAME AS ORG_NODE_NAME
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
 ORDER BY 2;





CURSOR c_Get_Product_Specific
  IS
   SELECT  B.CUST_PROD_ID,
           B.PRODUCT_NAME
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
      AND PDT.PRODUCT_NAME like '%2015'
      ORDER BY PDT.PRODUCT_SEQ DESC)B
      WHERE ROWNUM = 1;




BEGIN
      v_org_seq := 1;

      ----get the org_node_level
      SELECT ORG.ORG_NODE_LEVEL INTO v_OrgNodeLevel FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;



         IF p_test_administration = -99   OR  p_test_administration IS NULL  THEN

            ---to get default product
             FOR r_Get_Product_Specific IN c_Get_Product_Specific
               LOOP
                    v_Cust_ProductId := r_Get_Product_Specific.CUST_PROD_ID;
             END LOOP;

            FOR r_Get_Corp_Default IN c_Get_District (v_Cust_ProductId)
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Corp_Default.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Corp_Default.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := v_org_seq;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

                    IF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT =1 THEN
                      v_org_seq := v_org_seq+2;
                     END IF;

                    IF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT =2 THEN
                      t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                      t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
                      t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'ALL';
                      t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := v_org_seq-1;

                      t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                      t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

                     END IF;
            END LOOP;

          ELSE
           FOR r_Get_Corp_Cascade IN c_Get_District (p_test_administration)
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Corp_Cascade.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Corp_Cascade.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := v_org_seq;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

                       IF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT =1 THEN
                      v_org_seq := v_org_seq+2;
                     END IF;

                    IF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT =2 THEN
                      t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                      t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
                      t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'ALL';
                      t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := v_org_seq-1;

                      t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                      t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

                     END IF;

            END LOOP;

      END IF;

      IF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

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
END SF_GET_DISTRICT_LABEL;
/
