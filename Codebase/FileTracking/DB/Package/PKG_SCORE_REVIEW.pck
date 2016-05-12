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
    V_COMMENT              VARCHAR2(50);
    V_TMP_MSG              VARCHAR2(50);
    V_INCOMING_SCR_ROW_CNT NUMBER;
    V_ACTUAL_SCR_ROW_CNT   NUMBER;
  
    CURSOR GET_SCR_COUNT IS
      SELECT COUNT(1) CNT
        FROM (WITH T AS (SELECT P_STATUS_STR FROM DUAL)
               SELECT REGEXP_SUBSTR(P_STATUS_STR, '[^,]+', 1, LEVEL)
                 FROM T
               CONNECT BY LEVEL <=
                          LENGTH(REGEXP_REPLACE(P_STATUS_STR, '[^,]*')) + 1) A;
  
  
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
      SELECT DISTINCT STUDENT_BIO_ID,
                      SUBTESTID,
                      HSE,
                      NCE,
                      SS,
                      OPR_HSE,
                      OPR_SS,
                      OPR_NCR
        FROM SCR_SUBTEST_SCORE_FACT
       WHERE SCR_ID = V_SCR_ID_STR;
  
  BEGIN
    FOR REC4 IN GET_SCR_COUNT LOOP
      V_INCOMING_SCR_ROW_CNT := REC4.CNT;
    END LOOP;
  
    SELECT COUNT(1)
      INTO V_ACTUAL_SCR_ROW_CNT
      FROM SCR_SUBTEST_SCORE_FACT
     WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND SUBTESTID = P_SUBTEST_ID;
  
    IF V_ACTUAL_SCR_ROW_CNT <> V_INCOMING_SCR_ROW_CNT THEN
      P_OUTPUT_MSG := 'There is a newer record with the same score(s). Please approve the record with the latest processed date.';
    
    ELSE
    
      FOR REC5 IN GET_TEMP_STR(P_STATUS_STR) LOOP
        V_SCR_ID := SUBSTR(REC5.COL1, 0, (INSTR(REC5.COL1, '~') - 1));
        V_STATUS := SUBSTR(REC5.COL1, (INSTR(REC5.COL1, '~') + 1));
      
        UPDATE SCR_SUBTEST_SCORE_FACT
           SET SCR_STATUS = V_STATUS, UPDATED_DATE_TIME = SYSDATE
         WHERE SCR_ID = V_SCR_ID;
        COMMIT;
      
        IF V_STATUS = 'AP' THEN
          FOR REC6 IN GET_STUDENT_DETAILS(V_SCR_ID) LOOP
            IF ((REC6.OPR_HSE > REC6.HSE) OR (REC6.OPR_NCR > REC6.NCE) OR
               (REC6.OPR_SS > REC6.SS)) THEN
              P_OUTPUT_MSG := 'The selected score(s) are lower than other available scores.';
            END IF;
          
            UPDATE SUBTEST_SCORE_FACT
               SET HSE           = REC6.HSE,
                   NCE           = REC6.NCE,
                   SS            = REC6.SS,
                   DATETIMESTAMP = SYSDATE
             WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
               AND SUBTESTID = P_SUBTEST_ID;
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
             WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID;
          END LOOP;
        
          FOR REC8 IN (SELECT *
                         FROM SCR_STUDENT_DEMO_VALUES
                        WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID) LOOP
            UPDATE STUDENT_DEMO_VALUES
               SET STU_DEMO_VALID = REC8.STU_DEMO_VALID,
                   DEMOID         = REC8.DEMOID,
                   DEMO_VALID     = REC8.DEMO_VALID,
                   DEMO_VALUE     = REC8.DEMO_VALUE,
                   DATETIMESTAMP  = SYSDATE
             WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID;
          END LOOP;
        
          UPDATE SCR_SUBTEST_SCORE_FACT
             SET SCR_STATUS = 'PR', UPDATED_DATE_TIME = SYSDATE /*, IS_ACTIVE = 'N'*/
           WHERE SCR_ID = V_SCR_ID;
        
          P_OUTPUT_MSG := 'Student details updated successfully.';
        ELSE
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
             WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID;
          END LOOP;
        
          FOR REC8 IN (SELECT *
                         FROM SCR_STUDENT_DEMO_VALUES
                        WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID) LOOP
            UPDATE STUDENT_DEMO_VALUES
               SET STU_DEMO_VALID = REC8.STU_DEMO_VALID,
                   DEMOID         = REC8.DEMOID,
                   DEMO_VALID     = REC8.DEMO_VALID,
                   DEMO_VALUE     = REC8.DEMO_VALUE,
                   DATETIMESTAMP  = SYSDATE
             WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID;
          END LOOP;
        
          UPDATE SCR_SUBTEST_SCORE_FACT
             SET SCR_STATUS = 'RJ', UPDATED_DATE_TIME = SYSDATE /*, IS_ACTIVE = 'N'*/
           WHERE SCR_ID = V_SCR_ID;
        
          P_OUTPUT_MSG := 'Student details updated successfully';
        END IF;
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
      P_ERR_MSG    := UPPER(SQLERRM);
      P_OUTPUT_MSG := 'Student details has been approved with Error';
      UPDATE SCR_SUBTEST_SCORE_FACT
         SET SCR_STATUS        = 'AE',
             UPDATED_DATE_TIME = SYSDATE,
             SCR_ERR_MSG       = P_ERR_MSG
       WHERE SCR_ID = V_SCR_ID;
  END SP_SAVE_REVIEW_SCORE;
END PKG_SCORE_REVIEW;
/
