CREATE OR REPLACE FUNCTION SF_GET_CLASS (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_grade IN GRADE_DIM.GRADEID%TYPE
                             ,p_test_program  NUMBER
                             ,p_customerid CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_type IN VARCHAR )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_CLASS
  * PURPOSE:   To GET different CLASSES
  * CREATED:   TCS  11/DEC/2013
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
  v_SchoolId ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_GradeId GRADE_DIM.GRADEID%TYPE;
  v_test_program NUMBER;
   --v_CUSTID CONSTANT NUMBER := 1000;
   v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;


 CURSOR c_Get_Class_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,p_SchoolId ORG_NODE_DIM.ORG_NODEID%TYPE,test_program1 NUMBER ,p_Gradeid GRADE_DIM.GRADEID%TYPE)
  IS
  SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND,
       GRADE_SELECTION_LOOKUP GSL
 WHERE CPL.PRODUCTID = p_ProductId
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
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
   AND ORPD.ORG_NODEID = OND.ORG_NODEID
   AND OND.ORG_NODEID  = GSL.ORG_NODEID
   AND GSL.GRADEID = p_Gradeid
    AND ((v_OrgNodeLevel<>4 AND OND.PARENT_ORG_NODEID = p_SchoolId)
         OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = LoggedInUserJasperOrgId))
      AND OND.ORG_NODE_LEVEL = 4
 ORDER BY OND.ORG_NODE_NAME;

CURSOR c_Get_Class_Cascade (test_program4 NUMBER)
  IS
  SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND,
       GRADE_SELECTION_LOOKUP GSL
 WHERE CPL.PRODUCTID = p_test_administration
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_MODE = DECODE(test_program4,
                            -99,
                            'PUBLIC',
                            NULL,
                            'PUBLIC',
                            0,
                            'NON-PUBLIC',
                            1,
                            'PUBLIC')
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND ORPD.ORG_NODEID =OND.ORG_NODEID
   AND OND.ORG_NODEID  = GSL.ORG_NODEID
   AND GSL.GRADEID = p_grade
   AND ((v_OrgNodeLevel<>4 AND OND.PARENT_ORG_NODEID = p_school)
         OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = LoggedInUserJasperOrgId))
    AND OND.ORG_NODE_LEVEL = 4
 ORDER BY 2;



 CURSOR c_Get_Class_For_Level4 (p_Product_Id PRODUCT.PRODUCTID%TYPE,test_program NUMBER ,p_Grade_id GRADE_DIM.GRADEID%TYPE)
  IS
  SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM  CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND,
       GRADE_SELECTION_LOOKUP GSL
 WHERE CPL.PRODUCTID = p_Product_Id
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_MODE= DECODE(test_program,
                            -99,
                            'PUBLIC',
                            NULL,
                            'PUBLIC',
                            0,
                            'NON-PUBLIC',
                            1,
                            'PUBLIC')
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND ORPD.ORG_NODEID = OND.ORG_NODEID
   AND OND.ORG_NODEID  = GSL.ORG_NODEID
   AND GSL.GRADEID = p_Grade_id
   AND ((v_OrgNodeLevel=4  AND  OND.ORG_NODEID = LoggedInUserJasperOrgId))
   AND OND.ORG_NODE_LEVEL = 4
 ORDER BY 2;


CURSOR c_Get_Corp_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,test_program2 NUMBER)
  IS
    SELECT A.ORG_NODEID FROM
    (SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
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

CURSOR c_Get_School_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,p_CorpId ORG_NODE_DIM.ORG_NODEID%TYPE,test_program3 NUMBER)
  IS
  SELECT A.ORG_NODEID FROM
  (SELECT OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.PRODUCTID = p_ProductId
   AND OND.ORG_MODE = DECODE(test_program3,
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
 ORDER BY 2) A
 WHERE ROWNUM =1 ;




 CURSOR c_Get_Grade (Product_Id PRODUCT.PRODUCTID%TYPE,org_id ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT A.GRADEID FROM
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
     AND CUST.PRODUCTID= Product_Id
     AND GRD.GRADEID = GSL.GRADEID
  ORDER BY 3) A
  WHERE ROWNUM=1;

 CURSOR c_Get_Grade_For_ClassPrfGrpng (Product_Id PRODUCT.PRODUCTID%TYPE,org_id ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT A.GRADEID FROM
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
     AND CUST.PRODUCTID= Product_Id
     AND GRD.GRADEID = GSL.GRADEID
     AND GRD.GRADEID NOT IN (10005,10006)
  ORDER BY 3) A
  WHERE ROWNUM=1;




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
        ORDER BY PDT.PRODUCT_SEQ DESC)A
        WHERE ROWNUM = 1);

 /*CURSOR c_Get_Product_For_Class
  IS
  SELECT PDT.PRODUCTID,PDT.PRODUCT_NAME
    FROM ORG_PRODUCT_LINK OPRD,
         CUST_PRODUCT_LINK CUST,
         PRODUCT PDT
    WHERE OPRD.ORG_NODEID = LoggedInUserJasperOrgId
      AND OPRD.CUST_PROD_ID = CUST.CUST_PROD_ID
      AND PDT.PRODUCTID = CUST.PRODUCTID
      AND CUST.CUSTOMERID = p_customerid
      AND ROWNUM =1;*/

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

       -----for class level users
       IF v_OrgNodeLevel = 4 THEN

          IF  p_test_administration = -99 THEN
            ---get default product
            FOR r_Get_Product_Specific IN c_Get_Product_Specific
               LOOP
                    v_ProductId := r_Get_Product_Specific.PRODUCTID;
            END LOOP;
          ELSE
              v_ProductId :=  p_test_administration;
          END IF;

            ---get the default Grade for the class
            IF p_type = 'CLASS_PRF_GRPNG' THEN---for class proficiency grouping
                FOR r_Get_Grade_For_ClassPrfGrpng IN c_Get_Grade_For_ClassPrfGrpng (v_ProductId,LoggedInUserJasperOrgId)
                LOOP
                       v_GradeId := r_Get_Grade_For_ClassPrfGrpng.GRADEID;
                END LOOP;
            ELSE
                FOR r_Get_Grade IN c_Get_Grade (v_ProductId,LoggedInUserJasperOrgId)
                LOOP
                       v_GradeId := r_Get_Grade.GRADEID;
                END LOOP;
            END IF;

            ---get class for level 4 user
            FOR r_Get_Class_For_Level4 IN c_Get_Class_For_Level4 (v_ProductId,v_test_program,v_GradeId)
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Class_For_Level4.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Class_For_Level4.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
            END LOOP;

        ELSIF  /* p_test_administration = -99   OR  p_test_administration IS NULL
           OR p_corpdiocese =-99 OR p_corpdiocese IS NULL
           OR*/
           p_school = -99 OR p_school IS NULL OR
           p_grade = -99   OR  p_grade IS NULL  THEN

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

             ---get default School
            FOR r_Get_School_Default IN c_Get_School_Default (v_ProductId,v_CorpId,v_test_program)
            LOOP
                   v_SchoolId := r_Get_School_Default.ORG_NODEID;
            END LOOP;

            ---get default Grade
            IF p_type = 'CLASS_PRF_GRPNG' THEN ---for class proficiency grouping
                FOR r_Get_Grade_For_ClassPrfGrpng IN c_Get_Grade_For_ClassPrfGrpng (v_ProductId,LoggedInUserJasperOrgId)
                LOOP
                       v_GradeId := r_Get_Grade_For_ClassPrfGrpng.GRADEID;
                END LOOP;
            ELSE
                FOR r_Get_Grade IN c_Get_Grade (v_ProductId,LoggedInUserJasperOrgId)
                LOOP
                       v_GradeId := r_Get_Grade.GRADEID;
                END LOOP;
            END IF;

            ---get default Class
            FOR r_Get_Class_Default IN c_Get_Class_Default (v_ProductId,v_SchoolId,v_test_program,v_GradeId)
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Class_Default.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Class_Default.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
            END LOOP;

          ELSE
           FOR r_Get_Class_Cascade IN c_Get_Class_Cascade(v_test_program)
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Class_Cascade.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Class_Cascade.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
            END LOOP;

      END IF;

      IF p_type = 'ADD_ALL' AND t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT >= 1 AND v_OrgNodeLevel <> 4  THEN
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'All Classes';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

       ----added for  group dwnload
       ELSIF p_type = 'ADD_ALL' AND p_grade= -1  THEN
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'All Classes';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

       ELSIF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

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
END SF_GET_CLASS;
/
