CREATE OR REPLACE FUNCTION SF_GET_SUBTEST_ISTEPS15 (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_corpdioceseAll IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_schoolAll IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_class IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_program  NUMBER
                             ,p_grade IN GRADE_DIM.GRADEID%TYPE
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_type IN VARCHAR)
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_SUBTEST
  * PURPOSE:   To GET different subtests
  * CREATED:   TCS  20/JUL/2015
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
  v_ProductId PRODUCT.PRODUCTID%TYPE;
  --v_CustomerId CONSTANT  NUMBER :=1000;
   v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;


 CURSOR c_Get_Subtest (p_test_admin PRODUCT.PRODUCTID%TYPE)
  IS
  SELECT DISTINCT SUB.SUBTESTID,
                   SUB.SUBTEST_NAME,
                   SUB.SUBTEST_SEQ
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
                     ASSESSMENT_DIM         ASES,
                     CUST_PRODUCT_LINK     CPL,
                     ORG_PRODUCT_LINK      OPL,
                     --ORG_TEST_PROGRAM_LINK OLINK,
                     ORG_NODE_DIM      ORG,
                     --TEST_PROGRAM          TP,
                     GRADE_SELECTION_LOOKUP GSL
               WHERE ((p_corpdioceseAll = -1 AND p_schoolAll = -1 AND ORG.ORG_NODEID = (SELECT  ORG.ORG_NODEID FROM  ORG_NODE_DIM ORG WHERE ORG.ORG_NODE_LEVEL= 1 START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID ))
                      OR
                      (p_corpdioceseAll = -99 AND p_schoolAll = -99 AND ORG.ORG_NODEID = (SELECT  ORG.ORG_NODEID FROM  ORG_NODE_DIM ORG WHERE ORG.ORG_NODE_LEVEL= 1 START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID ))
                       OR
                      (p_corpdioceseAll = -1 AND p_schoolAll = -99 AND ORG.ORG_NODEID = (SELECT  ORG.ORG_NODEID FROM  ORG_NODE_DIM ORG WHERE ORG.ORG_NODE_LEVEL= 1 START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID ))
                      OR
                      (p_corpdioceseAll <> -1 AND p_schoolAll = -99 AND ORG.ORG_NODEID = p_corpdioceseAll)
                      OR
                      (p_corpdioceseAll<>-1 AND p_schoolAll=-1 AND ORG.ORG_NODEID = p_corpdioceseAll)
                      OR
                      (p_corpdioceseAll<>-1 AND p_schoolAll<>-1 AND ORG.ORG_NODEID = p_schoolAll))
                 AND ASES.PRODUCTID = p_test_admin
                 AND ASES.ASSESSMENTID = SUB.ASSESSMENTID
                 /*AND ORG.ORG_MODE = DECODE(p_test_program,
                                            0,
                                            'NON-PUBLIC',
                                            1,
                                            'PUBLIC',
                                            -99,
                                            (SELECT DECODE(A.Test_Program_Type_Seq,1,'PUBLIC','NON-PUBLIC') AS TP
                                              FROM
                                              (SELECT vc1 as Test_Program_Type_Seq,vc2 as Test_Program_Type
                                                  FROM TABLE(SELECT SF_GET_TEST_PRGM (
                                                                 LoggedInUserJasperOrgId
                                                                 ,p_customerid
                                                                      )  from dual))A WHERE ROWNUM=1),
                                             NULL,
                                             (SELECT DECODE(A.Test_Program_Type_Seq,1,'PUBLIC','NON-PUBLIC') AS TP
                                              FROM
                                              (SELECT vc1 as Test_Program_Type_Seq,vc2 as Test_Program_Type
                                                  FROM TABLE(SELECT SF_GET_TEST_PRGM (
                                                                 LoggedInUserJasperOrgId
                                                                 ,p_customerid
                                                                      )  from dual))A WHERE ROWNUM=1))*/
                 AND SUB.GRADEID = DECODE (p_grade
                                            ,-99
                                            ,(SELECT A.GRADEID
                                              FROM
                                              (SELECT vc1 AS GRADEID ,vc2 AS GRADE_NAME, vc2 AS GRADE_SEQ
                                                 FROM TABLE (SELECT  SF_GET_GRADE_ISTEPS15 (
                                                              LoggedInUserJasperOrgId
                                                             ,p_test_administration
                                                             ,p_corpdioceseAll
                                                             ,p_schoolAll
                                                             ,p_class
                                                             ,p_test_program
                                                             ,p_customerid
                                                             ,p_type)FROM dual)
                                              ORDER BY 3)	A
                                              WHERE ROWNUM = 1)
                                              ,NULL
                                              ,(SELECT A.GRADEID
                                                FROM
                                                (SELECT vc1 AS GRADEID ,vc2 AS GRADE_NAME, vc2 AS GRADE_SEQ
                                                   FROM TABLE (SELECT  SF_GET_GRADE_ISTEPS15 (
                                                                LoggedInUserJasperOrgId
                                                               ,p_test_administration
                                                               ,p_corpdioceseAll
                                                               ,p_schoolAll
                                                               ,p_class
                                                               ,p_test_program
                                                               ,p_customerid
                                                               ,p_type)FROM dual)
                                                ORDER BY 3)	A
                                                WHERE ROWNUM = 1)
                                               ,p_grade)
                 AND ASES.PRODUCTID = CPL.PRODUCTID
                 AND CPL.CUST_PROD_ID = OPL.CUST_PROD_ID
                 --AND OPL.ORG_NODEID = OLINK.ORG_NODEID
                 AND OPL.ORG_NODEID = ORG.ORG_NODEID
                 AND OPL.ORG_NODEID = GSL.ORG_NODEID
                 AND CPL.ADMINID = GSL.ADMINID
                 AND SUB.GRADEID = GSL.GRADEID
                 AND SUB.ASSESSMENTID = GSL.ASSESSMENTID
                 --AND OLINK.TP_ID = TP.TP_ID
                 ORDER BY 3;





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
          AND PDT.PRODUCT_CODE = 'ISETPS15'
        ORDER BY PDT.PRODUCT_SEQ DESC)A
        WHERE ROWNUM = 1);

 CURSOR c_Get_Product_Specific
  IS
   SELECT  B.PRODUCTID
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
      AND PDT.PRODUCT_CODE = 'ISETPS15'
      AND ADM1.IS_CURRENT_ADMIN = 'Y'
      ORDER BY PDT.PRODUCT_SEQ DESC)B
      WHERE ROWNUM = 1;

 CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;


  BEGIN


      IF p_test_administration = -99 OR p_test_administration IS NULL THEN
        FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
              LOOP
                     v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
        END LOOP;
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
      ELSE
          v_ProductId := p_test_administration;
     END IF;


       FOR r_Get_Subtest IN c_Get_Subtest (v_ProductId)
              LOOP
                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest.SUBTESTID;
                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest.SUBTEST_NAME;
                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest.SUBTEST_SEQ;

                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       END LOOP;

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
END SF_GET_SUBTEST_ISTEPS15;
/
