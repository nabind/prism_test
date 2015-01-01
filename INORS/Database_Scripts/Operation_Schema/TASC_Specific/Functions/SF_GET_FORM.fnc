CREATE OR REPLACE FUNCTION SF_GET_FORM (
                              i_LoggedInUserName IN USERS.USERNAME%TYPE
                             ,i_ProductId  IN PRODUCT.PRODUCTID%TYPE
                             ,i_GradeId  IN GRADE_DIM.GRADEID%TYPE
                             ,i_AdminId  IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,i_SubtestIdSingleSelect IN SUBTEST_DIM.SUBTESTID%TYPE
                             ,i_SubtestIdMultiselect IN VARCHAR2
                             ,i_Start_Test_Date IN VARCHAR
                             ,i_End_Test_Date IN VARCHAR
                             ,i_type IN NUMBER )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  sf_get_form
  * PURPOSE:   To get all form for different subtest
  * CREATED:   TCS  04/OCT/2013
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
  v_Subtest_Id SUBTEST_DIM.SUBTESTID%TYPE;
  v_SubtestMulti_Id VARCHAR2(20000);
  v_Cust_Id CUSTOMER_INFO.CUSTOMERID%TYPE;
  v_ELA CONSTANT  NUMBER(4):=2003;
  v_OverAllComp CONSTANT  NUMBER(4):=2007;
   v_Date_Start VARCHAR2(40);
  v_Date_End VARCHAR2(40);

  CURSOR c_Get_Form_For_Obj (Subtest_Id SUBTEST_DIM.SUBTESTID%TYPE )
  IS
  SELECT DISTINCT FRM.FORMID,FRM.FORM_NAME
          FROM FORM_DIM FRM,
               SUBTEST_OBJECTIVE_MAP SOM,
               LEVEL_MAP LMP
          WHERE SOM.SUBTESTID =  Subtest_Id 
            AND SOM.LEVEL_MAPID=LMP.LEVEL_MAPID
            AND SOM.ASSESSMENTID = LMP.ASSESSMENTID
            AND FRM.FORMID = LMP.FORMID
            AND EXISTS (SELECT 1 FROM ORGUSER_MAPPING OUSR , 
                                      objective_score_fact  oft,
                                      CUST_PRODUCT_LINK CUST
                        WHERE UPPER(OUSR.USERNAME) = UPPER(i_LoggedInUserName)  
                        AND OUSR.LOWEST_NODEID = OFT.ORG_NODEID 
                      --  AND ousr.customerid = oft.customerid 
                        AND OFT.SUBTESTID = SOM.SUBTESTID 
                        AND OFT.objectiveID = SOM.objectiveID 
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
                        AND OFT.CUST_PROD_ID = CUST.CUST_PROD_ID
                        AND OFT.ADMINID = CUST.ADMINID
                        AND OFT.FORMID = FRM.FORMID
                       )  
   UNION 
                        
                       
    SELECT DISTINCT FRM.FORMID,FRM.FORM_NAME
            FROM FORM_DIM FRM,
                 SUBTEST_OBJECTIVE_MAP SOM,
                 LEVEL_MAP LMP
            WHERE SOM.SUBTESTID =  Subtest_Id
               AND SOM.LEVEL_MAPID=LMP.LEVEL_MAPID
              AND SOM.ASSESSMENTID = LMP.ASSESSMENTID
              AND FRM.FORMID = LMP.FORMID
             AND  EXISTS (SELECT 1 FROM  USERS USR,
                                         EDU_CENTER_USER_LINK ELINK,
                                          STUDENT_BIO_DIM STD,             
                                        OBJECTIVE_SCORE_FACT OFT  ,
                                        CUST_PRODUCT_LINK CUST 
                           WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                             AND USR.CUSTOMERID = v_Cust_Id
                             AND ELINK.USERID = USR.USERID 
                             AND std.edu_centerid = ELINK.edu_centerid  
                             AND CUST.CUST_PROD_ID = ELINK.CUST_PROD_ID                           
                             AND STD.ORG_NODEID = OFT.ORG_NODEID
                             AND OFT.STUDENT_BIO_ID = STD.STUDENT_BIO_ID
                             AND STD.GRADEID =  DECODE(i_GradeId,
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
                             AND STD.CUSTOMERID = v_Cust_Id -- changed by Debashis on 29/01/2014 For Performance 
                             AND STD.GRADEID = OFT.GRADEID
                             AND OFT.SUBTESTID = SOM.SUBTESTID 
                             AND OFT.LEVELID = LMP.LEVELID
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
                        AND OFT.CUST_PROD_ID = CUST.CUST_PROD_ID
                        AND OFT.ADMINID = CUST.ADMINID
                        AND OFT.objectiveID = SOM.objectiveID 
                        AND OFT.FORMID = FRM.FORMID
                             AND ROWNUM =1)
               ORDER BY 1;
        
       
      

 CURSOR c_Get_Form_For_Sub (v_SubtestMulti_Id VARCHAR2)
  IS
   SELECT DISTINCT FRM.FORMID,FRM.FORM_NAME
              FROM FORM_DIM FRM,
                   SUBTEST_OBJECTIVE_MAP SOM,
                   LEVEL_MAP LMP
              WHERE SOM.SUBTESTID IN (SELECT TRIM( SUBSTR ( txt
                                                   , INSTR (txt, ',', 1, level ) + 1
                                                   , INSTR (txt, ',', 1, level+1
                                                   )
                                             - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                      FROM ( SELECT ','||v_SubtestMulti_Id||',' AS txt
                                                FROM dual )
                                       CONNECT BY level <=
                                                    LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )
                AND SOM.LEVEL_MAPID=LMP.LEVEL_MAPID
                AND SOM.ASSESSMENTID = LMP.ASSESSMENTID
                AND FRM.FORMID = LMP.FORMID                                   
                AND EXISTS (SELECT 1 FROM SUBTEST_SCORE_FACT SCR,
                                              ORGUSER_MAPPING OUSR,
                                              CUST_PRODUCT_LINK CUST
                                 WHERE UPPER(OUSR.USERNAME) = UPPER(i_LoggedInUserName)
                                   AND OUSR.LOWEST_NODEID = SCR.ORG_NODEID
                                   AND OUSR.ADMINID = SCR.ADMINID
                                   AND OUSR.CUSTOMERID = v_Cust_Id
                                   AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
                                   AND SCR.SUBTESTID = SOM.SUBTESTID
                                   AND SCR.LEVELID = LMP.LEVELID
                                   AND SCR.FORMID = LMP.FORMID 
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
                                    AND SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                                    AND SCR.ADMINID = CUST.ADMINID)
                
          UNION
          SELECT DISTINCT FRM.FORMID,FRM.FORM_NAME
              FROM FORM_DIM FRM,
                   SUBTEST_OBJECTIVE_MAP SOM,
                   LEVEL_MAP LMP
              WHERE SOM.SUBTESTID IN (SELECT TRIM( SUBSTR ( txt
                                                   , INSTR (txt, ',', 1, level ) + 1
                                                   , INSTR (txt, ',', 1, level+1
                                                   )
                                             - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                      FROM ( SELECT ','||v_SubtestMulti_Id||',' AS txt
                                                FROM dual )
                                       CONNECT BY level <=
                                                    LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )
                AND SOM.LEVEL_MAPID=LMP.LEVEL_MAPID
                AND SOM.ASSESSMENTID = LMP.ASSESSMENTID
                AND FRM.FORMID = LMP.FORMID                                   
                 AND EXISTS (SELECT 1 FROM  SUBTEST_SCORE_FACT SCR,
                                            STUDENT_BIO_DIM STD,
                                            --GRADE_SELECTION_LOOKUP GSL ,
                                            EDU_CENTER_USER_LINK ELINK,
                                           -- EDU_CENTER_DETAILS EDTLS,
                                            USERS USR,
                                            CUST_PRODUCT_LINK CUST
                                            --ASSESSMENT_DIM ASES
                                   WHERE UPPER(USR.USERNAME) = UPPER(i_LoggedInUserName)
                                     AND ELINK.USERID = USR.USERID
                                     AND USR.CUSTOMERID = v_Cust_Id
                                     --AND USR.CUSTOMERID = EDTLS.CUSTOMERID
                                     --AND ELINK.EDU_CENTERID = EDTLS.EDU_CENTERID
                                     AND STD.EDU_CENTERID = ELINK.EDU_CENTERID
                                      AND CUST.CUST_PROD_ID = ELINK.CUST_PROD_ID
                                     AND STD.CUSTOMERID = v_Cust_Id
                                     AND STD.ORG_NODEID = SCR.ORG_NODEID
                                     AND STD.ADMINID = SCR.ADMINID
                                     AND STD.GRADEID =DECODE(i_GradeId,
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
                                     AND STD.GRADEID = SCR.GRADEID
                                     AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                                     AND STD.GENDERID = SCR.GENDERID
                                     AND SCR.LEVELID = LMP.LEVELID
                                     AND SCR.FORMID = LMP.FORMID                                     
                                     AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
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
                                    AND SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                                    AND SCR.ADMINID = CUST.ADMINID
                                     AND ROWNUM =1)
                
           ORDER BY 1;



 CURSOR c_Get_Subtest_Obj_Login (Cust_Id CUSTOMER_INFO.CUSTOMERID%TYPE,p_Date_Start IN VARCHAR,p_Date_End IN VARCHAR)
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
                        AND OFT.GRADEID = DECODE(i_GradeId,
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
                        AND OFT.LEVELID = LM.LEVELID 
                        AND OFT.ASSESSMENTID = SOM.ASSESSMENTID
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
                                    AND OFT.CUST_PROD_ID = CUST.CUST_PROD_ID
                                    AND OFT.ADMINID = CUST.ADMINID
                              AND OFT.SS IS NOT NULL
                              AND OFT.SS > 0  
                              /*AND ((NVL(p_Date_Start,'-1')= '-1' AND NVL(p_Date_End,'-1') = '-1') 
                                 OR 
                                 (OFT.TEST_DATE BETWEEN TO_DATE(p_Date_Start,'MM/DD/RRRR')  AND TO_DATE(p_Date_End,'MM/DD/RRRR')))*/)
                                 
              AND SUB.SUBTESTID NOT IN (v_ELA,v_OverAllComp)
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
                              AND CUST.CUST_PROD_ID = ELINK.CUST_PROD_ID
                               AND CUST.CUST_PROD_ID = OFT.CUST_PROD_ID
                             AND   USR.CUSTOMERID = STD.CUSTOMERID
                             AND STD.ORG_NODEID = OFT.ORG_NODEID
                              AND OFT.STUDENT_BIO_ID = STD.STUDENT_BIO_ID
                             --AND STD.GRADEID = i_GradeId
                             AND STD.GRADEID = OFT.GRADEID
                             AND OFT.SUBTESTID = SOM.SUBTESTID 
                             AND OFT.LEVELID = LM.LEVELID 
                              AND OFT.ASSESSMENTID = SOM.ASSESSMENTID
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
                                    AND OFT.CUST_PROD_ID = CUST.CUST_PROD_ID
                                    AND OFT.ADMINID = CUST.ADMINID
                                  AND OFT.SS > 0  
                                   /*AND ((NVL(p_Date_Start,'-1')= '-1' AND NVL(p_Date_End,'-1') = '-1') 
                                 OR 
                                 (OFT.TEST_DATE BETWEEN TO_DATE(p_Date_Start,'MM/DD/RRRR')  AND TO_DATE(p_Date_End,'MM/DD/RRRR')))*/)
             AND SUB.SUBTESTID NOT IN (v_ELA,v_OverAllComp)
              ORDER BY 3;

  CURSOR c_Get_Subtest_Sub_Login (Cust_Id CUSTOMER_INFO.CUSTOMERID%TYPE)
  IS
  SELECT LISTAGG(A.SUBTESTID, ',') WITHIN GROUP (ORDER BY A.SUBTEST_SEQ) AS SUBTEST_MULTI
  FROM
  (SELECT DISTINCT SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ
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
                   AND SCR.SS IS NOT NULL
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
                                    AND SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                                    AND SCR.ADMINID = CUST.ADMINID
                   AND SCR.SS > 0)
         ORDER BY 3) A;

 CURSOR c_Get_Subtest_Hse_Login (Cust_Id CUSTOMER_INFO.CUSTOMERID%TYPE)
  IS
  SELECT LISTAGG(A.SUBTESTID, ',') WITHIN GROUP (ORDER BY A.SUBTEST_SEQ) AS SUBTEST_MULTI
  FROM
  ( SELECT DISTINCT SUB.SUBTESTID, SUB.SUBTEST_NAME, SUB.SUBTEST_SEQ
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
           AND SUB.SUBTESTID NOT IN (v_ELA,v_OverAllComp)
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
           AND SUB.SUBTESTID NOT IN (v_ELA,v_OverAllComp)
           AND EXISTS (SELECT 1
                  FROM SUBTEST_SCORE_FACT SCR, STUDENT_BIO_DIM STD,CUST_PRODUCT_LINK CUST
                 WHERE STD.ORG_NODEID = SCR.ORG_NODEID
                   AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                   AND STD.EDU_CENTERID = EDTLS.EDU_CENTERID
                   AND SUB.SUBTESTID = SCR.SUBTESTID
                   AND SCR.LEVELID = LM.LEVELID
                   AND SCR.ASSESSMENTID = SOM.ASSESSMENTID
                   AND SCR.FORMID = FRM.FORMID
                   AND SCR.SS IS NOT NULL
                   AND CUST.PRODUCTID =DECODE(i_ProductId,
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
                                    AND SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                                    AND SCR.ADMINID = CUST.ADMINID
                   AND SCR.SS > 0)
         ORDER BY 3) A;


  CURSOR c_Get_Customer
  IS
  SELECT CUST.CUSTOMERID
   FROM CUSTOMER_INFO CUST,USERS USR
   WHERE UPPER(USR.USERNAME)=UPPER(i_LoggedInUserName)
     AND USR.CUSTOMERID = CUST.CUSTOMERID
     AND ROWNUM=1;


BEGIN
  IF i_AdminId = -99 THEN
    FOR r_Get_Customer IN c_Get_Customer
    LOOP
      v_Cust_Id := r_Get_Customer.CUSTOMERID;
      v_Date_End := to_char(trunc(SYSDATE),'MM/DD/RRRR');
      v_Date_Start := to_char(trunc(SYSDATE-30),'MM/DD/RRRR');
    END LOOP;
   ELSE
      v_Cust_Id := i_AdminId;
      v_Date_End := i_Start_Test_Date;
      v_Date_Start :=i_End_Test_Date; 
  END IF;


  --------Single Select -------------------
  IF i_SubtestIdSingleSelect = -99 THEN
    FOR r_Get_Subtest_Obj_Login IN c_Get_Subtest_Obj_Login (v_Cust_Id,v_Date_Start,v_Date_End)
    LOOP
      v_Subtest_Id := r_Get_Subtest_Obj_Login.SUBTESTID;
    END LOOP;
   ELSE
      v_Subtest_Id := i_SubtestIdSingleSelect;
  END IF;


  -----Multiselect ----------------------
  IF i_SubtestIdMultiselect = '-99' AND i_type <> 3 THEN
    FOR r_Get_Subtest_Sub_Login IN c_Get_Subtest_Sub_Login (v_Cust_Id)
    LOOP
      v_SubtestMulti_Id := r_Get_Subtest_Sub_Login.SUBTEST_MULTI;
    END LOOP;
   ELSIF  i_SubtestIdMultiselect = '-99' AND i_type = 3 THEN
     FOR r_Get_Subtest_Hse_Login IN c_Get_Subtest_Hse_Login (v_Cust_Id)
      LOOP
        v_SubtestMulti_Id := r_Get_Subtest_Hse_Login.SUBTEST_MULTI;
      END LOOP;
   ELSE
      v_SubtestMulti_Id := i_SubtestIdMultiselect;
  END IF;



    IF v_Cust_Id IS NOT NULL THEN
       IF i_type = 1  THEN
          FOR r_Get_Form_For_Obj IN c_Get_Form_For_Obj(v_Subtest_Id)
              LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Form_For_Obj.FORMID;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Form_For_Obj.FORM_NAME;

                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

              END LOOP;
        ELSE
            FOR r_Get_Form_For_Sub IN c_Get_Form_For_Sub(v_SubtestMulti_Id)
              LOOP
                   t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Form_For_Sub.FORMID;
                   t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Form_For_Sub.FORM_NAME;

                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;

              END LOOP;

       END IF;
   END IF;

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_GET_FORM;
/
