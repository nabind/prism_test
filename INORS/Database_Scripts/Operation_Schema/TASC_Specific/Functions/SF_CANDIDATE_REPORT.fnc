CREATE OR REPLACE FUNCTION SF_CANDIDATE_REPORT (
                              i_LoggedInUserName IN USERS.USERNAME%TYPE
                             ,i_StudentBioId IN VARCHAR --STUDENT_BIO_DIM.STUDENT_BIO_ID%TYPE
                             ,i_FormId  IN FORM_DIM.FORMID%TYPE
                             ,i_ProductId  IN PRODUCT.PRODUCTID%TYPE
                             ,i_OrgID IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,i_GradeId  IN GRADE_DIM.GRADEID%TYPE
                             ,i_CustId  IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,i_StartDate IN VARCHAR2
                             ,i_EndDate IN VARCHAR2
                             ,i_UserType IN VARCHAR2
                             ,i_IsBulk IN NUMBER )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

 /*******************************************************************************
  * FUNCTION:  SF_CANDIDATE_REPORT
  * PURPOSE:   TO GET ALL STUDENTS ALONG WITH THEIR SUBTESTS
  * CREATED:   TCS  11/NOV/2013
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

  CURSOR c_Single_Dwnld_Org
  IS
  SELECT ROW_NUMBER() OVER (PARTITION BY B.STUDENT_BIO_ID ORDER BY B.STUDENT_BIO_ID ) AS ROWNUMBER,B.*
            FROM(
                  SELECT MOD ((ROW_NUMBER() OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY SCR.STUDENT_BIO_ID )),3) AS ROW_VAL,
                        --MOD(ROWNUM,2)AS ROW_VAL,
                         SCR.STUDENT_BIO_ID,
                         SCR.FORMID,
                         SCR.SUBTESTID AS SUBTESTID1,
                         LEAD(SCR.SUBTESTID) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID2,
                         LEAD(SCR.SUBTESTID,2) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID3
                        -- LEAD(SCR.SUBTESTID) OVER (ORDER BY  SCR.STUDENT_BIO_ID,SUB.SUBTEST_SEQ ) AS SUBTESTID2
                  FROM SUBTEST_SCORE_FACT SCR,
                       CUST_PRODUCT_LINK CUST,
                       ORGUSER_MAPPING OUSR,
                       SUBTEST_DIM SUB
                  WHERE SCR.STUDENT_BIO_ID =i_StudentBioId
                  AND  SCR.FORMID = i_FormId
                  AND  CUST.PRODUCTID = i_ProductId
                  AND  CUST.CUSTOMERID = i_CustId
                  AND  CUST.ADMINID = SCR.ADMINID
                  AND  SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                  AND  CUST.ACTIVATION_STATUS ='AC'
                  AND  SCR.GRADEID=i_GradeId
                  AND  SCR.SS>0
                  AND  NVL (TO_CHAR(SCR.STATUS_CODE),'-1') NOT IN ('3','5','6')
                  AND  UPPER (OUSR.USERNAME) = UPPER(i_LoggedInUserName)
                  AND  OUSR.ADMINID= SCR.ADMINID
                  AND  SCR.ORG_NODEID = OUSR.LOWEST_NODEID
                  AND  SUB.SUBTESTID = SCR.SUBTESTID
                  AND  SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
                  ORDER BY SCR.STUDENT_BIO_ID)B
                  WHERE B.ROW_VAL NOT IN (0,2)

    UNION ALL

     SELECT DISTINCT 0 AS ROWNUMBER ,
                    1 AS ROW_VAL,
                    SD.STUDENT_BIO_ID,
                    i_FormId AS FORMID,
                    NULL AS SUBTESTID1 ,
                    NULL AS SUBTESTID2,
                    NULL AS SUBTESTID3
         FROM STUDENT_BIO_DIM SD,ORGUSER_MAPPING OUSR
         WHERE UPPER (OUSR.USERNAME) = UPPER(i_LoggedInUserName)
           AND OUSR.LOWEST_NODEID = SD.ORG_NODEID
           AND OUSR.CUSTOMERID = SD.CUSTOMERID
           AND OUSR.ADMINID = SD.ADMINID
           AND SD.STUDENT_BIO_ID =i_StudentBioId

         ORDER BY 3,1;

       CURSOR c_Single_Dwnld_Org_Search
       IS
       SELECT ROW_NUMBER() OVER (PARTITION BY B.STUDENT_BIO_ID ORDER BY B.STUDENT_BIO_ID ) AS ROWNUMBER,B.*
            FROM(
                  SELECT MOD ((ROW_NUMBER() OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY SCR.STUDENT_BIO_ID )),3) AS ROW_VAL,
                        --MOD(ROWNUM,2)AS ROW_VAL,
                         SCR.STUDENT_BIO_ID,
                         SCR.FORMID,
                         SCR.SUBTESTID AS SUBTESTID1,
                         LEAD(SCR.SUBTESTID) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID2,
                         LEAD(SCR.SUBTESTID,2) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID3
                        -- LEAD(SCR.SUBTESTID) OVER (ORDER BY  SCR.STUDENT_BIO_ID,SUB.SUBTEST_SEQ ) AS SUBTESTID2
                  FROM SUBTEST_SCORE_FACT SCR,
                       CUST_PRODUCT_LINK CUST,
                       ORGUSER_MAPPING_SEARCH OUSR,
                       SUBTEST_DIM SUB
                  WHERE SCR.STUDENT_BIO_ID =i_StudentBioId
                  AND  SCR.FORMID = i_FormId
                  AND  CUST.PRODUCTID = i_ProductId
                  AND  CUST.CUSTOMERID = i_CustId
                  AND  CUST.ADMINID = SCR.ADMINID
                  AND  SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                  AND  CUST.ACTIVATION_STATUS ='AC'
                  AND  SCR.GRADEID=i_GradeId
                  AND  SCR.SS>0
                  AND  NVL (TO_CHAR(SCR.STATUS_CODE),'-1') NOT IN ('3','5','6')
                  AND  UPPER (OUSR.USERNAME) = UPPER(i_LoggedInUserName)
                  AND  OUSR.ADMINID= SCR.ADMINID
                  AND  SCR.ORG_NODEID = OUSR.LOWEST_NODEID
                  AND  SUB.SUBTESTID = SCR.SUBTESTID
                  AND  SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
                  ORDER BY SCR.STUDENT_BIO_ID)B
                  WHERE B.ROW_VAL NOT IN (0,2)

    UNION ALL

     SELECT DISTINCT 0 AS ROWNUMBER ,
                    1 AS ROW_VAL,
                    SD.STUDENT_BIO_ID,
                    i_FormId AS FORMID,
                    NULL AS SUBTESTID1 ,
                    NULL AS SUBTESTID2,
                    NULL AS SUBTESTID3
         FROM STUDENT_BIO_DIM SD,ORGUSER_MAPPING_SEARCH OUSR
         WHERE UPPER (OUSR.USERNAME) = UPPER(i_LoggedInUserName)
           AND OUSR.LOWEST_NODEID = SD.ORG_NODEID
           AND OUSR.CUSTOMERID = SD.CUSTOMERID
           AND OUSR.ADMINID = SD.ADMINID
           AND SD.STUDENT_BIO_ID =i_StudentBioId

         ORDER BY 3,1;


       CURSOR c_Single_Dwnld_EduCenter
        IS
        SELECT ROW_NUMBER() OVER (PARTITION BY B.STUDENT_BIO_ID ORDER BY B.STUDENT_BIO_ID ) AS ROWNUMBER,B.*
                  FROM(
                        SELECT MOD ((ROW_NUMBER() OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY SCR.STUDENT_BIO_ID )),3) AS ROW_VAL,
                              --MOD(ROWNUM,2)AS ROW_VAL,
                               SCR.STUDENT_BIO_ID,
                               SCR.FORMID,
                               SCR.SUBTESTID AS SUBTESTID1,
                               LEAD(SCR.SUBTESTID) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID2,
                               LEAD(SCR.SUBTESTID,2) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID3
                              -- LEAD(SCR.SUBTESTID) OVER (ORDER BY  SCR.STUDENT_BIO_ID,SUB.SUBTEST_SEQ ) AS SUBTESTID2
                        FROM SUBTEST_SCORE_FACT SCR,
                             CUST_PRODUCT_LINK CUST,
                             EDU_CENTER_USER_LINK ELINK,
                             USERS USR,
                             SUBTEST_DIM SUB
                        WHERE SCR.STUDENT_BIO_ID =i_StudentBioId
                        AND  SCR.FORMID = i_FormId
                        AND  CUST.PRODUCTID = i_ProductId
                        AND  CUST.CUSTOMERID = i_CustId
                        AND  CUST.ADMINID = SCR.ADMINID
                        AND  SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                        AND  CUST.ACTIVATION_STATUS ='AC'
                        AND  SCR.GRADEID=i_GradeId
                        AND  SCR.SS>0
                        AND  NVL (TO_CHAR(SCR.STATUS_CODE),'-1') NOT IN ('3','5','6')
                        AND  UPPER (USR.USERNAME) = UPPER(i_LoggedInUserName)
                        AND  USR.CUSTOMERID = CUST.CUSTOMERID
                        AND  ELINK.CUST_PROD_ID = CUST.CUST_PROD_ID
                        AND  USR.USERID = ELINK.USERID
                        AND  EXISTS (SELECT 1 FROM STUDENT_BIO_DIM STD
                                      WHERE STD.STUDENT_BIO_ID = i_StudentBioId
                                      AND   STD.EDU_CENTERID = ELINK.EDU_CENTERID
                                      AND  STD.ADMINID = SCR.ADMINID
                                      AND  STD.GRADEID = SCR.GRADEID
                                      AND  STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                                      AND  STD.GENDERID = SCR.GENDERID
                                      AND  STD.CUSTOMERID = CUST.CUSTOMERID
                                      )
                        AND  SUB.SUBTESTID = SCR.SUBTESTID
                        AND  SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
                        ORDER BY SCR.STUDENT_BIO_ID)B
                        WHERE B.ROW_VAL NOT IN (0,2)
            UNION ALL

            SELECT DISTINCT 0 AS ROWNUMBER ,
                            1 AS ROW_VAL,
                            STD.STUDENT_BIO_ID,
                            i_FormId AS FORMID,
                            NULL AS SUBTESTID1 ,
                            NULL AS SUBTESTID2,
                            NULL AS SUBTESTID3
                      FROM STUDENT_BIO_DIM STD,
                          EDU_CENTER_USER_LINK ELINK,
                          USERS USR
                    WHERE STD.STUDENT_BIO_ID =i_StudentBioId
                     AND  ELINK.EDU_CENTERID = STD.EDU_CENTERID
                     AND  ELINK.USERID = USR.USERID
                     AND  UPPER(USR.USERNAME) = UPPER (i_LoggedInUserName)

              ORDER BY 3,1;

     CURSOR c_Bulk_Dwnld_Org
      IS
      SELECT ROW_NUMBER() OVER (PARTITION BY B.STUDENT_BIO_ID ORDER BY B.STUDENT_BIO_ID ) AS ROWNUMBER,B.*
                FROM(
                      SELECT MOD ((ROW_NUMBER() OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY SCR.STUDENT_BIO_ID )),3) AS ROW_VAL,
                             --MOD(ROWNUM,2)AS ROW_VAL,
                             SCR.STUDENT_BIO_ID,
                             SCR.FORMID,
                             SCR.SUBTESTID AS SUBTESTID1,
                             LEAD(SCR.SUBTESTID) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID2,
                             LEAD(SCR.SUBTESTID,2) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID3
                             --LEAD(SCR.SUBTESTID) OVER (ORDER BY  SCR.STUDENT_BIO_ID,SUB.SUBTEST_SEQ ) AS SUBTESTID2
                      FROM SUBTEST_SCORE_FACT SCR,
                           CUST_PRODUCT_LINK CUST,
                           ORGUSER_MAPPING OUSR,
                           SUBTEST_DIM SUB
                      WHERE SCR.STUDENT_BIO_ID IN  (SELECT TRIM( SUBSTR ( txt
                                                           , INSTR (txt, ',', 1, level ) + 1
                                                           , INSTR (txt, ',', 1, level+1
                                                           )
                                                         - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                      FROM ( SELECT ','||i_StudentBioId||',' AS txt
                                                          FROM dual )
                                                     CONNECT BY level <=
                                                          LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1 )
                      AND  CUST.PRODUCTID = i_ProductId
                      AND  CUST.CUSTOMERID = i_CustId
                      AND  CUST.ADMINID = SCR.ADMINID
                      AND  SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                      AND  CUST.ACTIVATION_STATUS ='AC'
                      AND  SCR.GRADEID=i_GradeId
                      AND  SCR.SS>0
                      AND  NVL (TO_CHAR(SCR.STATUS_CODE),'-1') NOT IN ('3','5','6')
                      AND  UPPER (OUSR.USERNAME) = UPPER(i_LoggedInUserName)
                      AND  OUSR.ADMINID= SCR.ADMINID
                      AND  SCR.ORG_NODEID = OUSR.LOWEST_NODEID
                      AND  SUB.SUBTESTID = SCR.SUBTESTID
                      AND  SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
                      --AND  SCR.TEST_DATE BETWEEN TO_DATE(i_StartDate,'MM/DD/YYYY') AND TO_DATE(i_EndDate,'MM/DD/YYYY')
                      ORDER BY SCR.STUDENT_BIO_ID)B
                      WHERE B.ROW_VAL NOT IN (0,2)

               UNION ALL

                  SELECT DISTINCT 0 AS ROWNUMBER ,
                                  1 AS ROW_VAL,
                                  SD.STUDENT_BIO_ID,
                                  -1 AS FORMID,
                                  NULL AS SUBTESTID1 ,
                                  NULL AS SUBTESTID2,
                                  NULL AS SUBTESTID3
                               FROM MV_STUDENT_DETAILS SD,ORGUSER_MAPPING OUSR
                               WHERE UPPER (OUSR.USERNAME) = UPPER(i_LoggedInUserName)
                                -- AND OUSR.ORG_NODEID = i_OrgID
                                 AND OUSR.LOWEST_NODEID = SD.ORG_NODEID
                                 AND OUSR.CUSTOMERID = SD.CUSTOMERID
                                 AND OUSR.ADMINID = SD.ADMINID
                                 AND SD.STUDENT_BIO_ID IN (SELECT TRIM( SUBSTR ( txt
                                                           , INSTR (txt, ',', 1, level ) + 1
                                                           , INSTR (txt, ',', 1, level+1
                                                           )
                                                         - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                      FROM ( SELECT ','||i_StudentBioId||',' AS txt
                                                          FROM dual )
                                                     CONNECT BY level <=
                                                          LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1 )
                                /*AND SD.DATE_TEST_TAKEN BETWEEN
                                     TO_DATE(i_StartDate,'MM/DD/YYYY') AND TO_DATE(i_EndDate,'MM/DD/YYYY')*/
                     ORDER BY 3,1 ;



   CURSOR c_Bulk_Dwnld_EduCenter
      IS
      SELECT ROW_NUMBER() OVER (PARTITION BY B.STUDENT_BIO_ID ORDER BY B.STUDENT_BIO_ID ) AS ROWNUMBER,B.*
                FROM(
                      SELECT MOD ((ROW_NUMBER() OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY SCR.STUDENT_BIO_ID )),3) AS ROW_VAL,
                            --MOD(ROWNUM,2)AS ROW_VAL,
                             SCR.STUDENT_BIO_ID,
                             SCR.FORMID,
                             SCR.SUBTESTID AS SUBTESTID1,
                             LEAD(SCR.SUBTESTID) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID2,
                             LEAD(SCR.SUBTESTID,2) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID3
                             --LEAD(SCR.SUBTESTID) OVER (ORDER BY  SCR.STUDENT_BIO_ID,SUB.SUBTEST_SEQ ) AS SUBTESTID2
                      FROM SUBTEST_SCORE_FACT SCR,
                           CUST_PRODUCT_LINK CUST,
                           EDU_CENTER_USER_LINK ELINK,
                           USERS USR,
                           SUBTEST_DIM SUB
                      WHERE SCR.STUDENT_BIO_ID IN (SELECT TRIM( SUBSTR ( txt
                                                           , INSTR (txt, ',', 1, level ) + 1
                                                           , INSTR (txt, ',', 1, level+1
                                                           )
                                                         - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                      FROM ( SELECT ','||i_StudentBioId||',' AS txt
                                                          FROM dual )
                                                     CONNECT BY level <=
                                                          LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1 )
                      AND  CUST.PRODUCTID = i_ProductId
                      AND  CUST.CUSTOMERID = i_CustId
                      AND  CUST.ADMINID = SCR.ADMINID
                      AND  SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                      AND  CUST.ACTIVATION_STATUS ='AC'
                      AND  SCR.GRADEID=i_GradeId
                      AND  SCR.SS>0
                      AND  NVL (TO_CHAR(SCR.STATUS_CODE),'-1') NOT IN ('3','5','6')
                      AND  UPPER (USR.USERNAME) = UPPER(i_LoggedInUserName)
                      AND  USR.CUSTOMERID = CUST.CUSTOMERID
                      AND  ELINK.CUST_PROD_ID = CUST.CUST_PROD_ID
                      AND  USR.USERID = ELINK.USERID
                      AND  EXISTS (SELECT 1 FROM STUDENT_BIO_DIM STD
                                    WHERE STD.EDU_CENTERID = ELINK.EDU_CENTERID
                                    AND  STD.ADMINID = SCR.ADMINID
                                    AND  STD.GRADEID = SCR.GRADEID
                                    AND  STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                                    AND  STD.GENDERID = SCR.GENDERID
                                    AND  STD.CUSTOMERID = CUST.CUSTOMERID
                                    )
                      AND  SUB.SUBTESTID = SCR.SUBTESTID
                      AND  SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
                     -- AND  SCR.TEST_DATE BETWEEN TO_DATE(i_StartDate,'MM/DD/YYYY') AND TO_DATE(i_EndDate,'MM/DD/YYYY')
                      ORDER BY SCR.STUDENT_BIO_ID)B
                      WHERE B.ROW_VAL NOT IN (0,2)

             UNION ALL

             SELECT DISTINCT 0 AS ROWNUMBER ,
                          1 AS ROW_VAL,
                           SD.STUDENT_BIO_ID,
                          -1 AS FORMID,
                          NULL AS SUBTESTID1 ,
                          NULL AS SUBTESTID2,
                           NULL AS SUBTESTID3
                     FROM MV_STUDENT_DETAILS SD
                       WHERE  SD.EDU_CENTERID = i_OrgID
                       --AND SD.ADMINID = CUST.ADMINID
                       AND SD.CUSTOMERID = i_CustId
                       AND SD.GRADEID = i_GradeId
                       AND SD.STUDENT_BIO_ID IN (SELECT TRIM( SUBSTR ( txt
                                                           , INSTR (txt, ',', 1, level ) + 1
                                                           , INSTR (txt, ',', 1, level+1
                                                           )
                                                         - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                      FROM ( SELECT ','||i_StudentBioId||',' AS txt
                                                          FROM dual )
                                                     CONNECT BY level <=
                                                          LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1 )
                      /* AND SD.DATE_TEST_TAKEN BETWEEN
                           TO_DATE(i_StartDate,'MM/DD/YYYY') AND TO_DATE(i_EndDate,'MM/DD/YYYY')*/
                ORDER BY 3,1  ;


    CURSOR c_Single_Dwnld_EResource
    IS
    SELECT ROW_NUMBER() OVER (PARTITION BY B.STUDENT_BIO_ID ORDER BY B.STUDENT_BIO_ID ) AS ROWNUMBER,B.*
                FROM(
                      SELECT MOD ((ROW_NUMBER() OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY SCR.STUDENT_BIO_ID )),3) AS ROW_VAL,
                             --MOD(ROWNUM,2)AS ROW_VAL,
                             SCR.STUDENT_BIO_ID,
                             SCR.FORMID,
                             SCR.SUBTESTID AS SUBTESTID1,
                             LEAD(SCR.SUBTESTID) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID2,
                             LEAD(SCR.SUBTESTID,2) OVER (PARTITION BY SCR.STUDENT_BIO_ID ORDER BY  SCR.STUDENT_BIO_ID,SUB.CANDIDATE_SUB_SEQ ) AS SUBTESTID3
                             --LEAD(SCR.SUBTESTID) OVER (ORDER BY  SCR.STUDENT_BIO_ID,SUB.SUBTEST_SEQ ) AS SUBTESTID2
                      FROM SUBTEST_SCORE_FACT SCR,
                           CUST_PRODUCT_LINK CUST,
                           SUBTEST_DIM SUB
                      WHERE SCR.STUDENT_BIO_ID =i_StudentBioId
                      AND  CUST.PRODUCTID = i_ProductId
                      AND  CUST.CUSTOMERID = i_CustId
                      AND  CUST.ADMINID = SCR.ADMINID
                      AND  SCR.CUST_PROD_ID = CUST.CUST_PROD_ID
                      AND  CUST.ACTIVATION_STATUS ='AC'
                      AND  SCR.GRADEID=i_GradeId
                      AND  SCR.SS>0
                      AND  NVL (TO_CHAR(SCR.STATUS_CODE),'-1') NOT IN ('3','5','6')
                      AND  SCR.ORG_NODEID = i_OrgID
                      AND  SUB.SUBTESTID = SCR.SUBTESTID
                      AND  SUB.SUBTEST_CODE NOT IN (v_ELA,v_OverAllComp)
                      --AND  SCR.TEST_DATE BETWEEN TO_DATE(i_StartDate,'MM/DD/YYYY') AND TO_DATE(i_EndDate,'MM/DD/YYYY')
                      ORDER BY SCR.STUDENT_BIO_ID)B
                      WHERE B.ROW_VAL NOT IN (0,2)

               UNION ALL

                  SELECT DISTINCT 0 AS ROWNUMBER ,
                                  1 AS ROW_VAL,
                                  SD.STUDENT_BIO_ID,
                                  -1 AS FORMID,
                                  NULL AS SUBTESTID1 ,
                                  NULL AS SUBTESTID2,
                                  NULL AS SUBTESTID3
                               FROM MV_STUDENT_DETAILS SD
                               WHERE SD.STUDENT_BIO_ID =i_StudentBioId
                                 AND  SD.ORG_NODEID = i_OrgID
                                 AND  SD.CUSTOMERID= i_CustId
                                /*AND SD.DATE_TEST_TAKEN BETWEEN
                                     TO_DATE(i_StartDate,'MM/DD/YYYY') AND TO_DATE(i_EndDate,'MM/DD/YYYY')*/
                     ORDER BY 3,1 ;




BEGIN
  IF i_IsBulk = 1  THEN /* 1 => BULK DWNLD; 2=> SINGLE DWNLD; -1=> MADE TO MAKE THIS CONDITION INVALID*/
      IF i_UserType = 'REGULAR' THEN
        FOR r_Bulk_Dwnld_Org IN c_Bulk_Dwnld_Org
          LOOP
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Bulk_Dwnld_Org.ROWNUMBER;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Bulk_Dwnld_Org.ROW_VAL;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Bulk_Dwnld_Org.STUDENT_BIO_ID;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Bulk_Dwnld_Org.FORMID;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Bulk_Dwnld_Org.SUBTESTID1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Bulk_Dwnld_Org.SUBTESTID2;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Bulk_Dwnld_Org.SUBTESTID3;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP;
       ELSE
         FOR r_Bulk_Dwnld_EduCenter IN c_Bulk_Dwnld_EduCenter
          LOOP
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Bulk_Dwnld_EduCenter.ROWNUMBER;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Bulk_Dwnld_EduCenter.ROW_VAL;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Bulk_Dwnld_EduCenter.STUDENT_BIO_ID;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Bulk_Dwnld_EduCenter.FORMID;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Bulk_Dwnld_EduCenter.SUBTESTID1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Bulk_Dwnld_EduCenter.SUBTESTID2;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Bulk_Dwnld_EduCenter.SUBTESTID3;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP;
      END IF;
  ELSE
     IF i_UserType = 'REGULAR' THEN
        FOR r_Single_Dwnld_Org IN c_Single_Dwnld_Org
                LOOP
                  t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Single_Dwnld_Org.ROWNUMBER;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Single_Dwnld_Org.ROW_VAL;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Single_Dwnld_Org.STUDENT_BIO_ID;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Single_Dwnld_Org.FORMID;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Single_Dwnld_Org.SUBTESTID1;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Single_Dwnld_Org.SUBTESTID2;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Single_Dwnld_Org.SUBTESTID3;

                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP;
         ELSIF  i_UserType = 'REGULAR_SEARCH' THEN
          FOR r_Single_Dwnld_Org_Search IN c_Single_Dwnld_Org_Search
                  LOOP
                    t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Single_Dwnld_Org_Search.ROWNUMBER;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Single_Dwnld_Org_Search.ROW_VAL;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Single_Dwnld_Org_Search.STUDENT_BIO_ID;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Single_Dwnld_Org_Search.FORMID;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Single_Dwnld_Org_Search.SUBTESTID1;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Single_Dwnld_Org_Search.SUBTESTID2;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Single_Dwnld_Org_Search.SUBTESTID3;

                    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                  END LOOP;
         ELSIF  i_UserType = 'ERESOURCE' THEN
          FOR r_Single_Dwnld_EResource IN c_Single_Dwnld_EResource
                  LOOP
                    t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Single_Dwnld_EResource.ROWNUMBER;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Single_Dwnld_EResource.ROW_VAL;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Single_Dwnld_EResource.STUDENT_BIO_ID;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Single_Dwnld_EResource.FORMID;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Single_Dwnld_EResource.SUBTESTID1;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Single_Dwnld_EResource.SUBTESTID2;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Single_Dwnld_EResource.SUBTESTID3;

                    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                  END LOOP;
         ELSE
             FOR r_Single_Dwnld_EduCenter IN c_Single_Dwnld_EduCenter
                LOOP
                  t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Single_Dwnld_EduCenter.ROWNUMBER;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Single_Dwnld_EduCenter.ROW_VAL;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Single_Dwnld_EduCenter.STUDENT_BIO_ID;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Single_Dwnld_EduCenter.FORMID;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Single_Dwnld_EduCenter.SUBTESTID1;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Single_Dwnld_EduCenter.SUBTESTID2;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Single_Dwnld_EduCenter.SUBTESTID3;

                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP;
         END IF;
  END IF;

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_CANDIDATE_REPORT;
/
