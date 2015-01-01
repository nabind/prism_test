CREATE OR REPLACE FUNCTION SF_GET_SUBTEST (
                              i_LoggedInUserName IN USERS.USERNAME%TYPE
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
  SELECT  DISTINCT SUB.SUBTESTID,
                   SUB.SUBTEST_NAME,
                   SUB.SUBTEST_SEQ
            FROM SUBTEST_OBJECTIVE_MAP SOM,
                 SUBTEST_DIM SUB,
                 LEVEL_MAP LM,
                 form_dim FRM
            WHERE SUB.SUBTESTID = SOM.SUBTESTID
              AND LM.LEVEL_MAPID=SOM.LEVEL_MAPID
              AND frm.formid = lm.formid
              AND EXISTS (SELECT 1 FROM ORGUSER_MAPPING OUSR ,
                                      OBJECTIVE_SCORE_FACT  OFT,
                                      CUST_PRODUCT_LINK CUST
                        WHERE UPPER(OUSR.USERNAME) = UPPER(i_LoggedInUserName)
                        AND OUSR.LOWEST_NODEID = OFT.ORG_NODEID
                        AND OFT.SUBTESTID = SOM.SUBTESTID
                        AND OFT.objectiveID = SOM.objectiveID
                        AND OFT.FORMID = FRM.FORMID
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
                        AND OFT.LEVELID = LM.LEVELID
                        AND OFT.ASSESSMENTID = SOM.ASSESSMENTID
                              AND OFT.SS IS NOT NULL
                              AND OFT.SS > 0
                            /* AND ((NVL(p_Date_Start,'-1')= '-1' AND NVL(p_Date_End,'-1') = '-1')
                                 OR
                                 (OFT.TEST_DATE BETWEEN TO_DATE(p_Date_Start,'MM/DD/RRRR')  AND TO_DATE(p_Date_End,'MM/DD/RRRR')))*/)

              AND SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
            UNION
           SELECT  DISTINCT SUB.SUBTESTID,
                   SUB.SUBTEST_NAME,
                   SUB.SUBTEST_SEQ
            FROM SUBTEST_OBJECTIVE_MAP SOM,
                 SUBTEST_DIM SUB,
                 LEVEL_MAP LM,
                 form_dim FRM
            WHERE SUB.SUBTESTID = SOM.SUBTESTID
              AND LM.LEVEL_MAPID=SOM.LEVEL_MAPID
              AND frm.formid = lm.formid
               AND  EXISTS (SELECT 1 FROM  USERS USR,
                                         EDU_CENTER_USER_LINK ELINK,
                                          STUDENT_BIO_DIM STD,
                                        OBJECTIVE_SCORE_FACT OFT ,
                                        CUST_PRODUCT_LINK CUST
                           WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                            -- AND USR.CUSTOMERID = 1002
                             AND ELINK.USERID = USR.USERID
                             AND std.edu_centerid = ELINK.edu_centerid
                             AND   USR.CUSTOMERID = STD.CUSTOMERID
                             AND STD.ORG_NODEID = OFT.ORG_NODEID
                              AND OFT.STUDENT_BIO_ID = STD.STUDENT_BIO_ID
                              AND CUST.CUST_PROD_ID = ELINK.CUST_PROD_ID
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
                             AND OFT.SUBTESTID = SOM.SUBTESTID
                             AND OFT.LEVELID = LM.LEVELID
                              AND OFT.ASSESSMENTID = SOM.ASSESSMENTID
                        AND OFT.objectiveID = SOM.objectiveID
                        AND OFT.FORMID = FRM.FORMID
                                  AND OFT.SS > 0
                                  /*AND ((NVL(p_Date_Start,'-1')= '-1' AND NVL(p_Date_End,'-1') = '-1')
                                 OR
                                 (OFT.TEST_DATE BETWEEN TO_DATE(p_Date_Start,'MM/DD/RRRR')  AND TO_DATE(p_Date_End,'MM/DD/RRRR')))*/)
             AND SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
              ORDER BY 3;

  CURSOR c_Get_Subtest_Sub
  IS
          SELECT DISTINCT SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ
          FROM (SELECT DISTINCT SUBTESTID, LEVEL_MAPID, ASSESSMENTID
                  FROM SUBTEST_OBJECTIVE_MAP) SOM,
               SUBTEST_DIM SUB,
               ASSESSMENT_DIM ASES,
               GRADE_SELECTION_LOOKUP GSL,
               LEVEL_MAP LM,
               USERS USR,
               ORG_USERS OU
         WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
           AND USR.USERID = OU.USERID
              -- AND  USR.ACTIVATION_STATUS='AC'
           AND OU.ORG_NODEID = GSL.ORG_NODEID
           AND OU.ADMINID = GSL.ADMINID
           AND OU.ACTIVATION_STATUS = 'AC'
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
           AND LM.FORMID = GSL.FORMID
           AND LM.ASSESSMENTID = GSL.ASSESSMENTID
           AND LM.LEVELID = GSL.LEVELID
           AND LM.LEVEL_MAPID = SOM.LEVEL_MAPID
           AND LM.ASSESSMENTID = SOM.ASSESSMENTID
           AND SOM.SUBTESTID = SUB.SUBTESTID
           AND USR.CUSTOMERID =
               DECODE(i_AdminId,
                      -99,
                      (SELECT CUST.CUSTOMERID
                         FROM CUSTOMER_INFO CUST, USERS USR
                        WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                          AND USR.CUSTOMERID = CUST.CUSTOMERID
                          AND ROWNUM = 1),
                      NULL,
                      (SELECT CUST.CUSTOMERID
                         FROM CUSTOMER_INFO CUST, USERS USR
                        WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                          AND USR.CUSTOMERID = CUST.CUSTOMERID
                          AND ROWNUM = 1),
                      i_AdminId)
        UNION

        SELECT DISTINCT SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ
          FROM (SELECT DISTINCT SUBTESTID, LEVEL_MAPID, ASSESSMENTID
                  FROM SUBTEST_OBJECTIVE_MAP) SOM,
               SUBTEST_DIM SUB,
               LEVEL_MAP LM,
               FORM_DIM FRM,
               USERS USR,
               EDU_CENTER_USER_LINK ELINK,
               EDU_CENTER_DETAILS EDTLS

         WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
           AND ELINK.USERID = USR.USERID
           AND EDTLS.CUSTOMERID = USR.CUSTOMERID
           AND EDTLS.EDU_CENTERID = ELINK.EDU_CENTERID
           AND LM.FORMID = FRM.FORMID
           AND LM.LEVEL_MAPID = SOM.LEVEL_MAPID

           AND SOM.SUBTESTID = SUB.SUBTESTID
           AND EXISTS (SELECT 1
                  FROM SUBTEST_SCORE_FACT SCR, STUDENT_BIO_DIM STD,CUST_PRODUCT_LINK CUST
                 WHERE STD.ORG_NODEID = SCR.ORG_NODEID
                   AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                   AND STD.EDU_CENTERID = EDTLS.EDU_CENTERID
                   AND SUB.SUBTESTID = SCR.SUBTESTID
                   AND SCR.LEVELID = LM.LEVELID
                   AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
                   AND SCR.FORMID = FRM.FORMID
                   AND CUST.CUST_PROD_ID = ELINK.CUST_PROD_ID
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
                   AND SCR.SS IS NOT NULL
                   AND SCR.SS > 0)
         ORDER BY 3;

        CURSOR c_Get_Subtest_Hse
            IS
              SELECT DISTINCT SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ
          FROM (SELECT DISTINCT SUBTESTID, LEVEL_MAPID, ASSESSMENTID
                  FROM SUBTEST_OBJECTIVE_MAP) SOM,
               SUBTEST_DIM SUB,
               ASSESSMENT_DIM ASES,
               GRADE_SELECTION_LOOKUP GSL,
               LEVEL_MAP LM,
               USERS USR,
               ORG_USERS OU
         WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
           AND USR.USERID = OU.USERID
              -- AND  USR.ACTIVATION_STATUS='AC'
           AND OU.ORG_NODEID = GSL.ORG_NODEID
           AND OU.ADMINID = GSL.ADMINID
           AND OU.ACTIVATION_STATUS = 'AC'
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
           AND LM.FORMID = GSL.FORMID
           AND LM.ASSESSMENTID = GSL.ASSESSMENTID
           AND LM.LEVELID = GSL.LEVELID
           AND LM.LEVEL_MAPID = SOM.LEVEL_MAPID
           AND LM.ASSESSMENTID = SOM.ASSESSMENTID
           AND SOM.SUBTESTID = SUB.SUBTESTID
           AND SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
           AND USR.CUSTOMERID =
               DECODE(i_AdminId,
                      -99,
                      (SELECT CUST.CUSTOMERID
                         FROM CUSTOMER_INFO CUST, USERS USR
                        WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                          AND USR.CUSTOMERID = CUST.CUSTOMERID
                          AND ROWNUM = 1),
                      NULL,
                      (SELECT CUST.CUSTOMERID
                         FROM CUSTOMER_INFO CUST, USERS USR
                        WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                          AND USR.CUSTOMERID = CUST.CUSTOMERID
                          AND ROWNUM = 1),
                      i_AdminId)
        UNION

        SELECT DISTINCT SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ
          FROM (SELECT DISTINCT SUBTESTID, LEVEL_MAPID, ASSESSMENTID
                  FROM SUBTEST_OBJECTIVE_MAP) SOM,
               SUBTEST_DIM SUB,
               LEVEL_MAP LM,
               FORM_DIM FRM,
               USERS USR,
               EDU_CENTER_USER_LINK ELINK,
               EDU_CENTER_DETAILS EDTLS

         WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
           AND ELINK.USERID = USR.USERID
           AND EDTLS.CUSTOMERID = USR.CUSTOMERID
           AND EDTLS.EDU_CENTERID = ELINK.EDU_CENTERID
           AND LM.FORMID = FRM.FORMID
           AND LM.LEVEL_MAPID = SOM.LEVEL_MAPID
           AND SOM.SUBTESTID = SUB.SUBTESTID
           AND SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
           AND EXISTS (SELECT 1
                  FROM SUBTEST_SCORE_FACT SCR, STUDENT_BIO_DIM STD , CUST_PRODUCT_LINK CUST
                 WHERE STD.ORG_NODEID = SCR.ORG_NODEID
                   AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                   AND STD.EDU_CENTERID = EDTLS.EDU_CENTERID
                   AND SUB.SUBTESTID = SCR.SUBTESTID
                   AND SCR.LEVELID = LM.LEVELID
                   AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
                   AND SCR.FORMID = FRM.FORMID
                   AND CUST.CUST_PROD_ID = ELINK.CUST_PROD_ID
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
                   AND SCR.SS IS NOT NULL
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
  RAISE;
    RETURN NULL;
END SF_GET_SUBTEST;
/
