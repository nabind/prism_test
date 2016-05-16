CREATE OR REPLACE PACKAGE PKG_SCORE_REVIEW AS
  TYPE GET_REFCURSOR IS REF CURSOR;
  PROCEDURE SP_GET_SCORES_STUDENTS(P_SOURCE_SYSTEM            IN VARCHAR2,
                                   P_STATUS                   IN VARCHAR2,
                                   P_PROCESSED_DATE_FROM      IN VARCHAR2,
                                   P_PROCESSED_DATE_TO        IN VARCHAR2,
                                   P_UUID                     IN VARCHAR2,
                                   P_STATE_CODE               IN VARCHAR2,
                                   P_OUT_CUR_SCORE_RESULT     OUT GET_REFCURSOR,
                                   P_OUT_CUR_SCORE_RESULT_MSG OUT VARCHAR2);

  PROCEDURE SP_SAVE_REVIEW_SCORE(P_STUDENT_BIO_ID IN NUMBER,
                                 P_SUBTEST_ID     IN NUMBER,
                                 P_STATUS_STR     IN VARCHAR2,
                                 P_COMMENT_STR    IN VARCHAR2,
                                 P_OUTPUT_MSG     OUT VARCHAR2,
                                 P_ERR_MSG        OUT VARCHAR2);

END PKG_SCORE_REVIEW;
/
CREATE OR REPLACE PACKAGE BODY PKG_SCORE_REVIEW AS
  PROCEDURE SP_GET_SCORES_STUDENTS(P_SOURCE_SYSTEM            IN VARCHAR2,
                                   P_STATUS                   IN VARCHAR2,
                                   P_PROCESSED_DATE_FROM      IN VARCHAR2,
                                   P_PROCESSED_DATE_TO        IN VARCHAR2,
                                   P_UUID                     IN VARCHAR2,
                                   P_STATE_CODE               IN VARCHAR2,
                                   P_OUT_CUR_SCORE_RESULT     OUT GET_REFCURSOR,
                                   P_OUT_CUR_SCORE_RESULT_MSG OUT VARCHAR2) IS
  
    V_QUERY_ACTUAL VARCHAR2(4000) := '';
  
  BEGIN
    V_QUERY_ACTUAL := V_QUERY_ACTUAL || 'SELECT S.LAST_NAME || ' ||
                      ''', ''' || ' || S.FIRST_NAME ||''' ||
                      ' '' || S.MIDDLE_NAME STUD_NAME,
                   S.EXT_STUDENT_ID UUID,
                   C.CUSTOMER_CODE,
                   SUB.SUBTEST_NAME,
                   F.OPR_NCR,
                   F.OPR_SS,
                   F.OPR_HSE,
                   F.SUBTESTID,
                   MAX(F.CREATED_DATE_TIME) DATETIMESTAMP,
                   S.STUDENT_BIO_ID,
                   S.TEST_ELEMENT_ID
                   FROM SCR_STUDENT_BIO_DIM    S,
                   SCR_SUBTEST_SCORE_FACT F,
                   CUSTOMER_INFO          C,
                   FORM_DIM               FR,
                   SUBTEST_DIM            SUB
                   WHERE S.STUDENT_BIO_ID = F.STUDENT_BIO_ID
                   AND S.CUSTOMERID = C.CUSTOMERID
                   AND FR.FORMID = F.FORMID
                   AND SUB.SUBTESTID = F.SUBTESTID';
    IF P_SOURCE_SYSTEM <> '-1' OR P_SOURCE_SYSTEM <> '' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND S.STUDENT_MODE = ''' ||
                        P_SOURCE_SYSTEM || '''';
    END IF;
  
    IF P_STATUS <> '-1' OR P_STATUS <> '' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND F.SCR_STATUS = ''' ||
                        P_STATUS || '''';
    END IF;
  
    IF (P_PROCESSED_DATE_FROM <> '-1' OR P_SOURCE_SYSTEM <> '') AND
       (P_PROCESSED_DATE_TO <> '-1' OR P_PROCESSED_DATE_TO <> '') THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND (F.CREATED_DATE_TIME BETWEEN TO_DATE(''' ||
                        P_PROCESSED_DATE_FROM || ''',''MM/DD/YYYY'')' ||
                        ' AND TO_DATE(''' || P_PROCESSED_DATE_TO ||
                        ''',''MM/DD/YYYY'')+1)';
    END IF;
  
    IF P_UUID <> '-1' OR P_UUID <> '' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND S.EXT_STUDENT_ID = ''' ||
                        P_UUID || '''';
    END IF;
  
    IF P_STATE_CODE <> '-1' OR P_STATE_CODE <> '' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND C.CUSTOMER_CODE = ''' ||
                        P_STATE_CODE || '''';
    END IF;
  
    V_QUERY_ACTUAL := V_QUERY_ACTUAL || 'GROUP BY ';
    V_QUERY_ACTUAL := V_QUERY_ACTUAL || 'S.LAST_NAME || ' || ''', ''' ||
                      ' || S.FIRST_NAME ||''' ||
                      ' '' || S.MIDDLE_NAME , 
                                         S.EXT_STUDENT_ID , C.CUSTOMER_CODE, SUB.SUBTEST_NAME, F.OPR_NCR, F.OPR_SS, F.OPR_HSE, F.SUBTESTID, S.STUDENT_BIO_ID, S.TEST_ELEMENT_ID 
                                         ORDER BY DATETIMESTAMP ';
  
    OPEN P_OUT_CUR_SCORE_RESULT FOR V_QUERY_ACTUAL;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_CUR_SCORE_RESULT_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_SCORES_STUDENTS;

  PROCEDURE SP_SAVE_REVIEW_SCORE(P_STUDENT_BIO_ID IN NUMBER,
                                 P_SUBTEST_ID     IN NUMBER,
                                 P_STATUS_STR     IN VARCHAR2,
                                 P_COMMENT_STR    IN VARCHAR2,
                                 P_OUTPUT_MSG     OUT VARCHAR2,
                                 P_ERR_MSG        OUT VARCHAR2) IS
  
    V_SCR_ID               NUMBER;
    V_STU_BIO_ID           NUMBER;
    V_SUBTEST_ID           NUMBER;
    V_STATUS               VARCHAR2(50);
    V_COMMENT              SCR_SUBTEST_SCORE_FACT.SCR_COMMENT%TYPE;
    V_TMP_MSG              VARCHAR2(50);
    V_INCOMING_SCR_ROW_CNT NUMBER;
    V_ACTUAL_SCR_ROW_CNT   NUMBER;
    V_STATUS_FLAG          NUMBER := 0;
  
    CURSOR GET_SCR_COUNT IS
      SELECT COUNT(1) CNT
        FROM (WITH T AS (SELECT P_COMMENT_STR FROM DUAL)
               SELECT REGEXP_SUBSTR(P_COMMENT_STR, '[^,]+', 1, LEVEL)
                 FROM T
               CONNECT BY LEVEL <=
                          LENGTH(REGEXP_REPLACE(P_COMMENT_STR, '[^,]*')) + 1) A;
  
  
    CURSOR GET_TEMP_STR(STR IN VARCHAR2) IS
      SELECT A.COL1
        FROM (WITH T AS (SELECT STR FROM DUAL)
               SELECT REGEXP_SUBSTR(STR, '[^,]+', 1, LEVEL) COL1
                 FROM T
               CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(STR, '[^,]*')) + 1) A;
  
  
    CURSOR GET_SRCID_ID(STR IN VARCHAR2) IS
      SELECT A.COL1
        FROM (WITH T AS (SELECT STR FROM DUAL)
               SELECT REGEXP_SUBSTR(STR, '[^~]+', 1, LEVEL) COL1
                 FROM T
               CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(STR, '[^~]*')) + 1) A
                WHERE ROWNUM = 1;
  
  
    CURSOR GET_STUDENT_DETAILS(V_SCR_ID_STR IN NUMBER) IS
      SELECT * FROM SCR_SUBTEST_SCORE_FACT WHERE SCR_ID = V_SCR_ID_STR;
  
  BEGIN
    FOR REC4 IN GET_SCR_COUNT LOOP
      V_INCOMING_SCR_ROW_CNT := REC4.CNT;
    END LOOP;
  
    SELECT COUNT(1)
      INTO V_ACTUAL_SCR_ROW_CNT
      FROM SCR_SUBTEST_SCORE_FACT
     WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND SUBTESTID = P_SUBTEST_ID
       AND SCR_STATUS IN ('RV', 'AE');
  
    IF V_ACTUAL_SCR_ROW_CNT <> V_INCOMING_SCR_ROW_CNT THEN
      P_OUTPUT_MSG := 'There is a newer record with the same score(s). Please approve the record with the latest processed date.';
    
    ELSE
    
      FOR REC5 IN GET_TEMP_STR(P_STATUS_STR) LOOP
        V_SCR_ID := SUBSTR(REC5.COL1, 0, (INSTR(REC5.COL1, '~') - 1));
        V_STATUS := SUBSTR(REC5.COL1, (INSTR(REC5.COL1, '~') + 1));
      
        UPDATE SCR_SUBTEST_SCORE_FACT
           SET SCR_STATUS = V_STATUS, UPDATED_DATE_TIME = SYSDATE
         WHERE SCR_ID = V_SCR_ID;
      END LOOP;
      COMMIT;
    
      FOR REC5 IN GET_TEMP_STR(P_STATUS_STR) LOOP
        V_SCR_ID := SUBSTR(REC5.COL1, 0, (INSTR(REC5.COL1, '~') - 1));
        V_STATUS := SUBSTR(REC5.COL1, (INSTR(REC5.COL1, '~') + 1));
      
        IF V_STATUS = 'AP' THEN
          V_STATUS_FLAG := 1;
          FOR REC6 IN GET_STUDENT_DETAILS(V_SCR_ID) LOOP
            /*IF ((REC6.OPR_HSE > REC6.HSE) OR (REC6.OPR_NCR > REC6.NCE) OR
               (REC6.OPR_SS > REC6.SS)) THEN
              P_OUTPUT_MSG := 'The selected score(s) are lower than other available scores.';
            ELSE*/
            UPDATE SCR_SUBTEST_SCORE_FACT
               SET SCR_STATUS        = 'PR',
                   UPDATED_DATE_TIME = SYSDATE,
                   OPR_HSE           = REC6.HSE,
                   OPR_NCR           = REC6.NCR,
                   OPR_SS            = REC6.SS /*, IS_ACTIVE = 'N'*/
             WHERE SCR_ID = V_SCR_ID;
          
            UPDATE SUBTEST_SCORE_FACT S
               SET S.HSE            = REC6.HSE,
                   S.NCR            = REC6.NCR,
                   S.SS             = REC6.SS,
                   S.SUBTEST_FACTID = REC6.SUBTEST_FACTID,
                   S.ORG_NODEID     = REC6.ORG_NODEID,
                   S.CUST_PROD_ID   = REC6.CUST_PROD_ID,
                   S.ASSESSMENTID   = REC6.ASSESSMENTID,
                   S.CONTENTID      = REC6.CONTENTID,
                   S.GENDERID       = REC6.GENDERID,
                   S.GRADEID        = REC6.GRADEID,
                   S.LEVELID        = REC6.LEVELID,
                   S.FORMID         = REC6.FORMID,
                   S.ADMINID        = REC6.ADMINID,
                   S.AAGE           = REC6.AAGE,
                   S.AANCE          = REC6.AANCE,
                   S.AANP           = REC6.AANP,
                   S.AANS           = REC6.AANS,
                   S.AASS           = REC6.AASS,
                   S.ACSIP          = REC6.ACSIP,
                   S.ACSIS          = REC6.ACSIS,
                   S.ACSIN          = REC6.ACSIN,
                   S.CSI            = REC6.CSI,
                   S.CSIL           = REC6.CSIL,
                   S.CSIU           = REC6.CSIU,
                   S.DIFF           = REC6.DIFF,
                   S.GE             = REC6.GE,
                   S.LEX            = REC6.LEX,
                   S.LEXL           = REC6.LEXL,
                   S.LEXU           = REC6.LEXU,
                   S.NCE            = REC6.NCE,
                   S.NP             = REC6.NP,
                   S.NPA            = REC6.NPA,
                   S.NPG            = REC6.NPG,
                   S.NPL            = REC6.NPL,
                   S.NPH            = REC6.NPH,
                   S.NS             = REC6.NS,
                   S.NSA            = REC6.NSA,
                   S.NSG            = REC6.NSG,
                   S.OM             = REC6.OM,
                   S.OMS            = REC6.OMS,
                   S.OP             = REC6.OP,
                   S.OPM            = REC6.OPM,
                   S.PC             = REC6.PC,
                   S.PL             = REC6.PL,
                   S.PP             = REC6.PP,
                   S.PR             = REC6.PR,
                   S.SEM            = REC6.SEM,
                   S.SNPC           = REC6.SNPC,
                   S.QTL            = REC6.QTL,
                   S.QTLL           = REC6.QTLL,
                   S.QTLU           = REC6.QTLU,
                   S.STATUS_CODE    = REC6.STATUS_CODE,
                   S.TEST_DATE      = REC6.TEST_DATE,
                   S.DATETIMESTAMP  = SYSDATE
             WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
               AND SUBTESTID = P_SUBTEST_ID;
          
            FOR REC1 IN (SELECT *
                           FROM SCR_OBJECTIVE_SCORE_FACT
                          WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID) LOOP
              UPDATE objective_score_fact O
                 SET O.OBJECTIVE_FACTID = REC1.OBJECTIVE_FACTID,
                     O.ORG_NODEID       = REC1.ORG_NODEID,
                     O.CUST_PROD_ID     = REC1.CUST_PROD_ID,
                     O.ASSESSMENTID     = REC1.ASSESSMENTID,
                     O.CONTENTID        = REC1.CONTENTID,
                     O.SUBTESTID        = REC1.SUBTESTID,
                     O.OBJECTIVEID      = REC1.OBJECTIVEID,
                     O.GENDERID         = REC1.GENDERID,
                     O.GRADEID          = REC1.GRADEID,
                     O.LEVELID          = REC1.LEVELID,
                     O.FORMID           = REC1.FORMID,
                     O.ADMINID          = REC1.ADMINID,
                     O.NCR              = REC1.NCR,
                     O.OS               = REC1.OS,
                     O.OPI              = REC1.OPI,
                     O.OPI_CUT          = REC1.OPI_CUT,
                     O.MEAN_IPI         = REC1.MEAN_IPI,
                     O.OPIQ             = REC1.OPIQ,
                     O.OPIP             = REC1.OPIP,
                     O.PC               = REC1.PC,
                     O.PP               = REC1.PP,
                     O.SS               = REC1.SS,
                     O.PL               = REC1.PL,
                     O.INRC             = REC1.INRC,
                     O.CONDCODE_ID      = REC1.CONDCODE_ID,
                     O.TEST_DATE        = REC1.TEST_DATE,
                     O.DATETIMESTAMP    = SYSDATE
               WHERE O.STUDENT_BIO_ID = P_STUDENT_BIO_ID
                 AND O.OBJECTIVE_FACTID = REC1.OBJECTIVE_FACTID;
            END LOOP;
          
            FOR REC2 IN (SELECT *
                           FROM SCR_ITEM_SCORE_FACT
                          WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID) LOOP
              UPDATE ITEM_SCORE_FACT I
                 SET I.ITEM_FACTID   = REC2.ITEM_FACTID,
                     I.ORG_NODEID    = REC2.ORG_NODEID,
                     I.CUST_PROD_ID  = CUST_PROD_ID,
                     I.ASSESSMENTID  = ASSESSMENTID,
                     I.CONTENTID     = CONTENTID,
                     I.SUBTESTID     = SUBTESTID,
                     I.OBJECTIVEID   = OBJECTIVEID,
                     I.GRADEID       = GRADEID,
                     I.LEVELID       = LEVELID,
                     I.FORMID        = FORMID,
                     I.ADMINID       = ADMINID,
                     I.ITEMSETID     = ITEMSETID,
                     I.READID        = READID,
                     I.SCORE_VALUES  = SCORE_VALUES,
                     I.DATETIMESTAMP = SYSDATE
               WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
                 AND I.ITEM_FACTID = REC2.ITEM_FACTID;
            END LOOP;
            /*END IF;*/
          END LOOP;
        
          P_OUTPUT_MSG := 'Student details updated successfully.';
        ELSIF V_STATUS = 'RJ' THEN
        
          UPDATE SCR_SUBTEST_SCORE_FACT
             SET SCR_STATUS = 'RJ', UPDATED_DATE_TIME = SYSDATE /*, IS_ACTIVE = 'N'*/
           WHERE SCR_ID = V_SCR_ID;
        
          P_OUTPUT_MSG := 'Student details updated successfully';
        END IF;
      END LOOP;
    
      FOR REC7 IN (SELECT *
                     FROM SCR_STUDENT_BIO_DIM
                    WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID) LOOP
        UPDATE STUDENT_BIO_DIM
           SET FIRST_NAME         = REC7.FIRST_NAME,
               MIDDLE_NAME        = REC7.MIDDLE_NAME,
               LAST_NAME          = REC7.LAST_NAME,
               BIRTHDATE          = REC7.BIRTHDATE,
               TEST_ELEMENT_ID    = REC7.TEST_ELEMENT_ID,
               INT_STUDENT_ID     = REC7.INT_STUDENT_ID,
               EXT_STUDENT_ID     = REC7.EXT_STUDENT_ID,
               LITHOCODE          = REC7.LITHOCODE,
               GENDERID           = REC7.GENDERID,
               GRADEID            = REC7.GRADEID,
               EDU_CENTERID       = REC7.EDU_CENTERID,
               BARCODE            = REC7.BARCODE,
               SPECIAL_CODES      = REC7.SPECIAL_CODES,
               STUDENT_MODE       = REC7.STUDENT_MODE,
               ORG_NODEID         = REC7.ORG_NODEID,
               CUSTOMERID         = REC7.CUSTOMERID,
               ADMINID            = REC7.ADMINID,
               IS_BIO_UPDATE_CMPL = REC7.IS_BIO_UPDATE_CMPL,
               PP_IMAGING_ID      = REC7.PP_IMAGING_ID,
               OAS_IMAGING_ID     = REC7.OAS_IMAGING_ID,
               UPDATED_DATE_TIME  = SYSDATE
         WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
           AND TEST_ELEMENT_ID = REC7.TEST_ELEMENT_ID;
      END LOOP;
    
      FOR REC8 IN (SELECT *
                     FROM SCR_STUDENT_DEMO_VALUES
                    WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID) LOOP
        UPDATE STUDENT_DEMO_VALUES D1
           SET D1.STU_DEMO_VALID = REC8.STU_DEMO_VALID,
               D1.DEMOID         = REC8.DEMOID,
               D1.DEMO_VALID     = REC8.DEMO_VALID,
               D1.DEMO_VALUE     = REC8.DEMO_VALUE,
               D1.DATETIMESTAMP  = SYSDATE
         WHERE D1.STUDENT_BIO_ID = P_STUDENT_BIO_ID
           AND D1.STU_DEMO_VALID = REC8.STU_DEMO_VALID;
      END LOOP;
    
      FOR REC9 IN (SELECT *
                     FROM SCR_STU_SUBTEST_DEMO_VALUES
                    WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
                      AND SUBTESTID = P_SUBTEST_ID) LOOP
        UPDATE STU_SUBTEST_DEMO_VALUES D2
           SET D2.STU_TST_DEMO_VALID = REC9.STU_TST_DEMO_VALID,
               D2.DEMOID             = REC9.DEMOID,
               D2.DEMO_VALID         = REC9.DEMO_VALID,
               D2.DEMO_VALUE         = REC9.DEMO_VALUE,
               D2.DATE_TEST_TAKEN    = REC9.DATE_TEST_TAKEN,
               D2.DATETIMESTAMP      = SYSDATE
         WHERE D2.STUDENT_BIO_ID = P_STUDENT_BIO_ID
           AND D2.SUBTESTID = P_SUBTEST_ID
           AND D2.STU_TST_DEMO_VALID = REC9.STU_TST_DEMO_VALID;
      END LOOP;
    
      FOR REC5 IN GET_TEMP_STR(P_COMMENT_STR) LOOP
        V_SCR_ID  := SUBSTR(REC5.COL1, 0, (INSTR(REC5.COL1, '~') - 1));
        V_COMMENT := SUBSTR(REC5.COL1, (INSTR(REC5.COL1, '~') + 1));
      
        UPDATE SCR_SUBTEST_SCORE_FACT
           SET SCR_COMMENT = V_COMMENT, UPDATED_DATE_TIME = SYSDATE
         WHERE SCR_ID = V_SCR_ID;
      
      END LOOP;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_ERR_MSG := UPPER(SQLERRM);
      IF V_STATUS_FLAG = 1 THEN
        P_OUTPUT_MSG := 'Student details has been approved with Error';
        UPDATE SCR_SUBTEST_SCORE_FACT
           SET SCR_STATUS        = 'AE',
               UPDATED_DATE_TIME = SYSDATE,
               SCR_ERR_MSG       = P_ERR_MSG
         WHERE SCR_ID = V_SCR_ID;
      END IF;
  END SP_SAVE_REVIEW_SCORE;
END PKG_SCORE_REVIEW;
/
