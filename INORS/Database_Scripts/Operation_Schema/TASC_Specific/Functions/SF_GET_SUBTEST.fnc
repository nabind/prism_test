CREATE OR REPLACE FUNCTION SF_GET_SUBTEST (
                              i_LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,i_ProductId  IN PRODUCT.PRODUCTID%TYPE
                             ,i_GradeId  IN GRADE_DIM.GRADEID%TYPE
                             ,i_AdminId  IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,i_Start_Test_Date IN VARCHAR
                             ,i_End_Test_Date IN VARCHAR
                             ,i_type IN NUMBER )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_SUBTEST
  * PURPOSE:   To GET different subtest
  * CREATED:   TCS  06/NOV/2013
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR     DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();
  v_ELA CONSTANT  VARCHAR2(40):='3';
  v_OverAllComp CONSTANT  VARCHAR2(40):='7';
  v_Date_Start VARCHAR2(40);
  v_Date_End VARCHAR2(40);




 CURSOR c_Get_Subtest_Obj (p_Date_Start IN VARCHAR,p_Date_End IN VARCHAR)
  IS
  SELECT DISTINCT SOM.SUBTESTID, SOM.SUBTEST_NAME, SOM.SUBTEST_SEQ
    FROM MV_SUB_OBJ_FORM_MAP SOM
   WHERE SOM.SUBTEST_CODE NOT IN (v_ELA, v_OverAllComp)
     AND EXISTS
   (SELECT 1
            FROM ORG_LSTNODE_LINK      OLNK,
                 OBJECTIVE_SCORE_FACT OFT,
                 CUST_PRODUCT_LINK    CUST
           WHERE OLNK.ORG_NODEID = i_LoggedInUserJasperOrgId
             AND OLNK.ORG_LSTNODEID = OFT.ORG_NODEID
             AND OLNK.ADMINID= OFT.ADMINID
             AND OFT.SUBTESTID = SOM.SUBTESTID
             AND OFT.OBJECTIVEID = SOM.OBJECTIVEID
             AND OFT.FORMID = SOM.FORMID
             AND CUST.PRODUCTID = DECODE(i_ProductId,
                                         -99,
                                         (SELECT PRODUCTID
                                            FROM PRODUCT
                                           WHERE PRODUCT_SEQ = 1
                                             AND ROWNUM = 1),
                                         NULL,
                                         (SELECT PRODUCTID
                                            FROM PRODUCT
                                           WHERE PRODUCT_SEQ = 1
                                             AND ROWNUM = 1),
                                         i_ProductId)
             AND CUST.CUST_PROD_ID = OFT.CUST_PROD_ID
             AND CUST.ADMINID = OFT.ADMINID
                --AND OFT.GRADEID = i_GradeId
             AND OFT.LEVELID = SOM.LEVELID
             AND OFT.ASSESSMENTID = SOM.ASSESSMENTID
             /*AND OFT.SS > 0*/ --commented as directed during QA 03/27/2015 
             )

          UNION

           SELECT  DISTINCT SOM.SUBTESTID,
                   SOM.SUBTEST_NAME,
                   SOM.SUBTEST_SEQ
            FROM MV_SUB_OBJ_FORM_MAP SOM
            WHERE  SOM.SUBTEST_CODE NOT IN (v_ELA, v_OverAllComp)
            AND EXISTS (SELECT 1 FROM    STUDENT_BIO_DIM STD,
                                         OBJECTIVE_SCORE_FACT OFT ,
                                         CUST_PRODUCT_LINK CUST,
                                         EDU_CENTER_DETAILS EDTLS
                           WHERE EDTLS.EDU_CENTERID = i_LoggedInUserJasperOrgId
                             AND EDTLS.EDU_CENTERID = STD.EDU_CENTERID
                             AND EDTLS.CUSTOMERID = STD.CUSTOMERID
                             AND EDTLS.CUSTOMERID = CUST.CUSTOMERID
                             AND STD.ORG_NODEID = OFT.ORG_NODEID
                             AND OFT.STUDENT_BIO_ID = STD.STUDENT_BIO_ID
                             AND CUST.PRODUCTID = DECODE(i_ProductId,
                                                   -99,
                                                   (SELECT PRODUCTID
                                                      FROM PRODUCT
                                                     WHERE PRODUCT_SEQ = 1
                                                       AND ROWNUM = 1),
                                                   NULL,
                                                   (SELECT PRODUCTID
                                                      FROM PRODUCT
                                                     WHERE PRODUCT_SEQ = 1
                                                       AND ROWNUM = 1),
                                                   i_ProductId)
                             AND CUST.CUST_PROD_ID = OFT.CUST_PROD_ID
                             AND CUST.ADMINID = OFT.ADMINID
                             --AND STD.GRADEID = i_GradeId
                             AND STD.GRADEID = OFT.GRADEID
                             AND STD.ADMINID = OFT.ADMINID
                             AND OFT.SUBTESTID = SOM.SUBTESTID
                             AND OFT.LEVELID = SOM.LEVELID
                             AND OFT.ASSESSMENTID = SOM.ASSESSMENTID
                             AND OFT.objectiveID = SOM.objectiveID
                             AND OFT.FORMID = SOM.FORMID
                             /*AND OFT.SS > 0*/ --commented as directed during QA 03/27/2015
                             )
              ORDER BY 3;

  CURSOR c_Get_Subtest_Sub
  IS
          SELECT DISTINCT SOM.SUBTESTID, SOM.SUBTEST_NAME, SOM.SUBTEST_SEQ
          FROM MV_SUB_OBJ_FORM_MAP SOM,
               ORG_LSTNODE_LINK OLNK,
               ASSESSMENT_DIM ASES,
               GRADE_SELECTION_LOOKUP GSL
         WHERE OLNK.ORG_NODEID = i_LoggedInUserJasperOrgId
             AND OLNK.ORG_LSTNODEID = GSL.ORG_NODEID
             AND OLNK.ADMINID= GSL.ADMINID
             AND GSL.ASSESSMENTID = ASES.ASSESSMENTID
             AND GSL.GRADEID = DECODE(i_GradeId,
                                    -99,
                                    (SELECT GRADEID
                                       FROM GRADE_DIM
                                      WHERE GRADE_NAME = 'AD'
                                        AND ROWNUM = 1),
                                    NULL,
                                    (SELECT GRADEID
                                       FROM GRADE_DIM
                                      WHERE GRADE_NAME = 'AD'
                                        AND ROWNUM = 1),
                                    i_GradeId)
           AND ASES.PRODUCTID = DECODE(i_ProductId,
                                       -99,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       NULL,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       i_ProductId)
           AND SOM.FORMID = GSL.FORMID
           AND SOM.ASSESSMENTID = GSL.ASSESSMENTID
           AND SOM.LEVELID = GSL.LEVELID

        UNION

        SELECT DISTINCT SOM.SUBTESTID, SOM.SUBTEST_NAME, SOM.SUBTEST_SEQ
          FROM MV_SUB_OBJ_FORM_MAP SOM,
               EDU_CENTER_DETAILS EDTLS
         WHERE  EDTLS.EDU_CENTERID =  i_LoggedInUserJasperOrgId
           AND EXISTS (SELECT 1
                  FROM SUBTEST_SCORE_FACT SCR, STUDENT_BIO_DIM STD,CUST_PRODUCT_LINK CUST
                 WHERE STD.ORG_NODEID = SCR.ORG_NODEID
                   AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                   AND STD.EDU_CENTERID = EDTLS.EDU_CENTERID
                   AND STD.CUSTOMERID = EDTLS.CUSTOMERID
                   AND CUST.CUSTOMERID = EDTLS.CUSTOMERID
                   AND SCR.SUBTESTID = SOM.SUBTESTID
                   AND SCR.LEVELID = SOM.LEVELID
                   AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
                   AND SCR.FORMID = SOM.FORMID
                    AND CUST.PRODUCTID = DECODE(i_ProductId,
                                       -99,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       NULL,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       i_ProductId)
                    AND CUST.CUST_PROD_ID = SCR.CUST_PROD_ID
                    AND CUST.ADMINID = SCR.ADMINID
                   AND SCR.SS > 0)
         ORDER BY 3;

        CURSOR c_Get_Subtest_Hse
            IS
        SELECT DISTINCT SOM.SUBTESTID, SOM.SUBTEST_NAME, SOM.SUBTEST_SEQ
          FROM MV_SUB_OBJ_FORM_MAP SOM,
               ORG_LSTNODE_LINK OLNK,
               ASSESSMENT_DIM ASES,
               GRADE_SELECTION_LOOKUP GSL
         WHERE OLNK.ORG_NODEID = i_LoggedInUserJasperOrgId
             AND OLNK.ORG_LSTNODEID = GSL.ORG_NODEID
             AND OLNK.ADMINID= GSL.ADMINID
             AND GSL.ASSESSMENTID = ASES.ASSESSMENTID
             AND GSL.GRADEID = DECODE(i_GradeId,
                                    -99,
                                    (SELECT GRADEID
                                       FROM GRADE_DIM
                                      WHERE GRADE_NAME = 'AD'
                                        AND ROWNUM = 1),
                                    NULL,
                                    (SELECT GRADEID
                                       FROM GRADE_DIM
                                      WHERE GRADE_NAME = 'AD'
                                        AND ROWNUM = 1),
                                    i_GradeId)
             AND ASES.PRODUCTID = DECODE(i_ProductId,
                                       -99,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       NULL,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       i_ProductId)
             AND SOM.FORMID = GSL.FORMID
             AND SOM.ASSESSMENTID = GSL.ASSESSMENTID
             AND SOM.LEVELID = GSL.LEVELID
             AND SOM.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)

        UNION

        SELECT DISTINCT SOM.SUBTESTID, SOM.SUBTEST_NAME, SOM.SUBTEST_SEQ
          FROM MV_SUB_OBJ_FORM_MAP SOM,
               EDU_CENTER_DETAILS EDTLS
         WHERE  EDTLS.EDU_CENTERID = i_LoggedInUserJasperOrgId
           AND SOM.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
           AND EXISTS (SELECT 1
                  FROM SUBTEST_SCORE_FACT SCR, STUDENT_BIO_DIM STD , CUST_PRODUCT_LINK CUST
                 WHERE STD.ORG_NODEID = SCR.ORG_NODEID
                   AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                   AND STD.EDU_CENTERID = EDTLS.EDU_CENTERID
                   AND STD.CUSTOMERID = EDTLS.CUSTOMERID
                   AND CUST.CUSTOMERID = EDTLS.CUSTOMERID
                   AND SCR.SUBTESTID = SOM.SUBTESTID
                   AND SCR.LEVELID = SOM.LEVELID
                   AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
                   AND SCR.FORMID = SOM.FORMID
                   AND CUST.PRODUCTID = DECODE(i_ProductId,
                                       -99,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       NULL,
                                       (SELECT PRODUCTID
                                          FROM PRODUCT
                                         WHERE PRODUCT_SEQ = 1
                                           AND ROWNUM = 1),
                                       i_ProductId)
                    AND CUST.CUST_PROD_ID = SCR.CUST_PROD_ID
                    AND CUST.ADMINID = SCR.ADMINID
                   AND SCR.SS > 0)
         ORDER BY 3;




BEGIN

--dbms_output.put_line();
       IF i_type = 1  THEN

         IF i_ProductId = -99 THEN
           v_Date_End := to_char(trunc(SYSDATE),'MM/DD/RRRR');
           v_Date_Start := to_char(trunc(SYSDATE-30),'MM/DD/RRRR');
          -- dbms_output.put_line(v_Date_Start);
          ELSE
           v_Date_End := i_Start_Test_Date;
           v_Date_Start :=i_End_Test_Date;
         END IF;

          FOR r_Get_Subtest_Obj IN c_Get_Subtest_Obj(v_Date_Start,v_Date_End)
              LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest_Obj.SUBTESTID;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest_Obj.SUBTEST_NAME;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest_Obj.SUBTEST_SEQ;

                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

              END LOOP;
         ELSIF i_type = 3  THEN
          FOR r_Get_Subtest_Hse IN c_Get_Subtest_Hse
              LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest_Hse.SUBTESTID;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest_Hse.SUBTEST_NAME;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest_Hse.SUBTEST_SEQ;

                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

              END LOOP;
         ELSE
            FOR r_Get_Subtest_Sub IN c_Get_Subtest_Sub
              LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Subtest_Sub.SUBTESTID;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Subtest_Sub.SUBTEST_NAME;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Subtest_Sub.SUBTEST_SEQ;

                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

              END LOOP;

       END IF;


RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
  --RAISE;
    RETURN NULL;
END SF_GET_SUBTEST;
/
