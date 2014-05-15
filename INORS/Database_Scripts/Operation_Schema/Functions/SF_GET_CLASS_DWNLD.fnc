CREATE OR REPLACE FUNCTION SF_GET_CLASS_DWNLD (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_grade IN GRADE_DIM.GRADEID%TYPE
                             ,p_generate_file IN VARCHAR
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
  /*v_ProductId PRODUCT.PRODUCTID%TYPE;
  v_CorpId ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_SchoolId ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_GradeId GRADE_DIM.GRADEID%TYPE;
  v_test_program NUMBER;*/
   v_CUSTID CONSTANT NUMBER := 1000;
   v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;




CURSOR c_Get_Class
  IS
  SELECT DISTINCT OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM PRODUCT               PDT,
       CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_TEST_PROGRAM_LINK OTPLNK,
       TEST_PROGRAM          TP,
       ORG_NODE_DIM          OND,
       GRADE_SELECTION_LOOKUP GSL
 WHERE PDT.PRODUCTID = p_test_administration
   AND TP.TP_TYPE = DECODE(p_test_program,
                                            0,
                                            'NON-PUBLIC',
                                            1,
                                            'PUBLIC',
                                            -99,
                                            (SELECT A.Test_Program_Type_Seq
                                              FROM
                                              (SELECT vc1 as Test_Program_Type_Seq,vc2 as Test_Program_Type
                                                  FROM TABLE(SELECT SF_GET_TEST_PRGM (
                                                                 LoggedInUserJasperOrgId
                                                                 ,p_customerid
                                                                      )  from dual))A WHERE ROWNUM=1),
                                             NULL,
                                             (SELECT A.Test_Program_Type_Seq
                                              FROM
                                              (SELECT vc1 as Test_Program_Type_Seq,vc2 as Test_Program_Type
                                                  FROM TABLE(SELECT SF_GET_TEST_PRGM (
                                                                 LoggedInUserJasperOrgId
                                                                 ,p_customerid
                                                                      )  from dual))A WHERE ROWNUM=1))
   AND PDT.PRODUCTID = CPL.PRODUCTID
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND ORPD.ORG_NODEID = OTPLNK.ORG_NODEID
   AND OTPLNK.TP_ID = TP.TP_ID
   AND OND.ORG_NODEID = OTPLNK.ORG_NODEID
   AND OND.ORG_NODEID  = GSL.ORG_NODEID
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND CPL.CUSTOMERID = p_customerid
   AND TP.ADMINID = CPL.ADMINID
   AND TP.CUSTOMERID = CPL.CUSTOMERID   
   AND CPL.CUSTOMERID=v_CUSTID
   AND p_grade = -1
   AND GSL.GRADEID IN (SELECT A.GRADEID
                                          FROM
                                              (SELECT vc1 AS GRADEID ,vc2 AS GRADE_NAME, vc2 AS GRADE_SEQ
                                                 FROM TABLE (SELECT  SF_GET_GRADE_DWNLD (
                                                              LoggedInUserJasperOrgId
                                                             ,p_test_administration
                                                             ,p_corpdiocese
                                                             ,p_school
                                                             ,p_generate_file
                                                             ,p_test_program
                                                             ,p_customerid
                                                             ,p_type)FROM dual)
                                              ORDER BY 3)	A
                                              )
   AND ((v_OrgNodeLevel<>4 AND OND.PARENT_ORG_NODEID = p_school)
         OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = LoggedInUserJasperOrgId))
   --AND OND.PARENT_ORG_NODEID = p_school
   AND OND.ORG_NODE_LEVEL = 4


   UNION ALL


   SELECT DISTINCT OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM PRODUCT               PDT,
       CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_TEST_PROGRAM_LINK OTPLNK,
       TEST_PROGRAM          TP,
       ORG_NODE_DIM          OND,
       GRADE_SELECTION_LOOKUP GSL
 WHERE PDT.PRODUCTID = p_test_administration
   AND TP.TP_TYPE = DECODE(p_test_program,
                                            0,
                                            'NON-PUBLIC',
                                            1,
                                            'PUBLIC',
                                            -99,
                                            (SELECT A.Test_Program_Type_Seq
                                              FROM
                                              (SELECT vc1 as Test_Program_Type_Seq,vc2 as Test_Program_Type
                                                  FROM TABLE(SELECT SF_GET_TEST_PRGM (
                                                                 LoggedInUserJasperOrgId
                                                                 ,p_customerid
                                                                      )  from dual))A WHERE ROWNUM=1),
                                             NULL,
                                             (SELECT A.Test_Program_Type_Seq
                                              FROM
                                              (SELECT vc1 as Test_Program_Type_Seq,vc2 as Test_Program_Type
                                                  FROM TABLE(SELECT SF_GET_TEST_PRGM (
                                                                 LoggedInUserJasperOrgId
                                                                 ,p_customerid
                                                                      )  from dual))A WHERE ROWNUM=1))
   AND PDT.PRODUCTID = CPL.PRODUCTID
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND ORPD.ORG_NODEID = OTPLNK.ORG_NODEID
   AND OTPLNK.TP_ID = TP.TP_ID
   AND OND.ORG_NODEID = OTPLNK.ORG_NODEID
   AND OND.ORG_NODEID  = GSL.ORG_NODEID
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND CPL.CUSTOMERID = p_customerid
   AND TP.ADMINID = CPL.ADMINID
   AND TP.CUSTOMERID = CPL.CUSTOMERID
   AND CPL.CUSTOMERID=v_CUSTID
   AND p_grade <> -1
   AND GSL.GRADEID = DECODE (p_grade
                                            ,-99
                                            ,(SELECT A.GRADEID
                                              FROM
                                              (SELECT vc1 AS GRADEID ,vc2 AS GRADE_NAME, vc2 AS GRADE_SEQ
                                                 FROM TABLE (SELECT  SF_GET_GRADE_DWNLD (
                                                              LoggedInUserJasperOrgId
                                                             ,p_test_administration
                                                             ,p_corpdiocese
                                                             ,p_school
                                                             ,p_generate_file
                                                             ,p_test_program
                                                             ,p_customerid
                                                             ,p_type)FROM dual)
                                              ORDER BY 3)	A
                                              WHERE ROWNUM = 1)
                                              ,NULL
                                              ,(SELECT A.GRADEID
                                                FROM
                                                (SELECT vc1 AS GRADEID ,vc2 AS GRADE_NAME, vc2 AS GRADE_SEQ
                                                   FROM TABLE (SELECT  SF_GET_GRADE_DWNLD (
                                                                LoggedInUserJasperOrgId
                                                               ,p_test_administration
                                                               ,p_corpdiocese
                                                               ,p_school
                                                               ,p_generate_file
                                                               ,p_test_program
                                                               ,p_customerid
                                                               ,p_type)FROM dual)
                                                ORDER BY 3)	A
                                                WHERE ROWNUM = 1)
                                               ,p_grade)
   AND ((v_OrgNodeLevel<>4 AND OND.PARENT_ORG_NODEID = p_school)
         OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = LoggedInUserJasperOrgId))
   --AND OND.PARENT_ORG_NODEID = p_school
   AND OND.ORG_NODE_LEVEL = 4


 ORDER BY 2;
 
 

      

CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;



BEGIN
       FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
       END LOOP;
       
       
      FOR r_Get_Class IN c_Get_Class
            LOOP
                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Class.ORG_NODEID;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Class.ORG_NODE_NAME;
                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
        END LOOP;

      IF p_type = 'ADD_ALL' AND t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT >= 1  THEN
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
    RETURN NULL;
END SF_GET_CLASS_DWNLD;
/
