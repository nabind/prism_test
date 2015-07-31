CREATE OR REPLACE FUNCTION SF_GET_SCHOOL_ISTEPS15 (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_program  NUMBER
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_type IN VARCHAR )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_SCHOOL
  * PURPOSE:   To GET different SCHOOLS
  * CREATED:   TCS  20/JUL/2015
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
 -- v_TP_PUBLIC CONSTANT  VARCHAR2(10) :='PUBLIC';
 -- v_TP_NONPUBLIC CONSTANT  VARCHAR2(10):='NON-PUBLIC';
  v_ProductId PRODUCT.PRODUCTID%TYPE;
  v_CorpId ORG_NODE_DIM.ORG_NODEID%TYPE;
   v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;
   v_test_program NUMBER;
   --v_CUSTID CONSTANT NUMBER := 1000;
   v_type VARCHAR2(50);


 CURSOR c_Get_School_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,p_CorpId ORG_NODE_DIM.ORG_NODEID%TYPE,test_program2 NUMBER)
  IS
  SELECT OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.PRODUCTID = p_ProductId
   AND OND.ORG_MODE = DECODE(test_program2,
                            -99,
                            'PUBLIC',
                            NULL,
                            'PUBLIC',
                            0,
                            'NON-PUBLIC',
                            1,
                            'PUBLIC')
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_NODEID = ORPD.ORG_NODEID
   AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = p_CorpId)
        OR (v_OrgNodeLevel=2 AND OND.PARENT_ORG_NODEID = p_CorpId)
        OR (v_OrgNodeLevel=3  AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
        OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = (SELECT DISTINCT ORG.ORG_NODEID
                                FROM  ORG_NODE_DIM ORG
                                WHERE ORG.ORG_NODE_LEVEL= 3
                                START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId
                                CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)))
   AND OND.ORG_NODE_LEVEL = 3
 ORDER BY 2;

CURSOR c_Get_School_Cascade (test_program1 NUMBER)
  IS
  SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.PRODUCTID = p_test_administration
   AND OND.ORG_MODE = DECODE(test_program1,
                            -99,
                            'PUBLIC',
                            NULL,
                            'PUBLIC',
                            0,
                            'NON-PUBLIC',
                            1,
                            'PUBLIC')
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_NODEID = ORPD.ORG_NODEID
   AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = p_corpdiocese)
        OR (v_OrgNodeLevel=2 AND OND.PARENT_ORG_NODEID = p_corpdiocese)
        OR (v_OrgNodeLevel=3  AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
        OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = (SELECT DISTINCT ORG.ORG_NODEID
                                FROM  ORG_NODE_DIM ORG
                                WHERE ORG.ORG_NODE_LEVEL= 3
                                START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId
                                CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)))
   AND OND.ORG_NODE_LEVEL = 3
 ORDER BY 2;


CURSOR c_Get_Corp_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,test_program NUMBER)
  IS
    SELECT A.ORG_NODEID FROM
    ( SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.PRODUCTID = p_ProductId
   AND OND.ORG_MODE = DECODE(test_program,
                            -99,
                            'PUBLIC',
                            NULL,
                            'PUBLIC',
                            0,
                            'NON-PUBLIC',
                            1,
                            'PUBLIC')
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



CURSOR c_Get_Product_Default
  IS
  ( SELECT A.PRODUCTID
       FROM
       (SELECT PDT.PRODUCTID,
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
          AND PDT.PRODUCT_CODE = 'ISTEPS15'
        ORDER BY PDT.PRODUCT_SEQ DESC)A
        WHERE ROWNUM = 1);

CURSOR c_Get_Product_Specific
  IS
   SELECT  B.PRODUCTID,
           B.PRODUCT_NAME
   FROM
 (SELECT PDT.PRODUCTID,
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
      AND PDT.PRODUCT_CODE = 'ISTEPS15'
      ORDER BY PDT.PRODUCT_SEQ DESC)B
      WHERE ROWNUM = 1;

 CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL AS ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;


CURSOR c_Get_TestPgm_Default (p_org_nodeid ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT B.Test_Program_Type_Seq
  FROM
  (
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
    ORDER BY 1 DESC) B WHERE  ROWNUM = 1 ;


BEGIN
      v_type :=  p_type;

       FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
       END LOOP;

       ------FOR TEST PROGRAM
       IF (p_test_program =-99 OR p_test_program IS NULL ) AND v_OrgNodeLevel = 1 THEN
          v_test_program := 1;
        ELSIF (p_test_program =-99 OR p_test_program IS NULL ) AND v_OrgNodeLevel <> 1 THEN

              FOR r_Get_TestPgm_Default IN c_Get_TestPgm_Default(LoggedInUserJasperOrgId)
                LOOP
                       v_test_program := r_Get_TestPgm_Default.Test_Program_Type_Seq;
              END LOOP;
         ELSE
           v_test_program := p_test_program;
       END IF;



      IF   (p_corpdiocese = -1 OR  p_corpdiocese = -99 ) AND v_type = 'ACAD_STDS_SUMM' AND  v_OrgNodeLevel =1 THEN --for academic standards summary and state level user only
                  v_type :='';
                  t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'All Schools';
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

         ELSIF  (p_corpdiocese = -99  AND v_type = 'ACAD_STDS_SUMM' AND v_OrgNodeLevel <>1) --- for Academic Standards Summary
               OR(p_corpdiocese = -99  AND v_type <> 'ADD_ALL')
               OR(p_corpdiocese  IS NULL  AND v_type <> 'ADD_ALL')
               OR(p_corpdiocese = -99  AND v_type = 'GRT_IC_DWNLD') THEN---For GRT/IC file download
                ---get default product
                IF  v_OrgNodeLevel > 1 THEN
                 FOR r_Get_Product_Specific IN c_Get_Product_Specific
                   LOOP
                        v_ProductId := r_Get_Product_Specific.PRODUCTID;
                 END LOOP;
                ELSE
                    FOR r_Get_Product_Default IN c_Get_Product_Default
                    LOOP
                           v_ProductId := r_Get_Product_Default.PRODUCTID;
                    END LOOP;
                END IF;

                ---get default Corp
                FOR r_Get_Corp_Default IN c_Get_Corp_Default (v_ProductId,v_test_program)
                LOOP
                       v_CorpId := r_Get_Corp_Default.ORG_NODEID;
                END LOOP;

                ---get default Schools
                FOR r_Get_School_Default IN c_Get_School_Default (v_ProductId,v_CorpId,v_test_program)
                LOOP
                         t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School_Default.ORG_NODEID;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School_Default.ORG_NODE_NAME;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP;

         ELSIF   (p_corpdiocese = -1 OR  p_corpdiocese = -99 ) AND  v_type = 'ADD_ALL' THEN
                 v_type :='';
                t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'All Schools';
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

                t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

          ELSE
           FOR r_Get_School_Cascade IN c_Get_School_Cascade(v_test_program)
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School_Cascade.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School_Cascade.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
            END LOOP;

      END IF;

      IF (v_type = 'ADD_ALL' OR v_type = 'GRT_IC_DWNLD' OR v_type = 'ACAD_STDS_SUMM') AND t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT >=1 /*AND v_OrgNodeLevel <= 2*/  THEN
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'All Schools';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       ELSIF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

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
END SF_GET_SCHOOL_ISTEPS15;
/
