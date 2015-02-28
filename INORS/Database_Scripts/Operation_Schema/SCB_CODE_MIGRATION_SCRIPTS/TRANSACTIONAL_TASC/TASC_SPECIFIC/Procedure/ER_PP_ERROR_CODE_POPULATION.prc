CREATE OR REPLACE PROCEDURE ER_PP_ERROR_CODE_POPULATION(P_PROCESS_ID IN NUMBER) IS

  V_ERROR_CODE      VARCHAR2(100) := NULL;
  V_ERROR_FIELD     VARCHAR2(100) := NULL;
  V_REC_COUNT       NUMBER := 0;
  V_COUNT           NUMBER := 0;
  V_FILENAME        VARCHAR2(100) := NULL;


  CURSOR C_STAGING_DATA_PP IS
    SELECT DISTINCT BIO.TEST_ELEMENT_ID AS TEST_ELEMENT_ID,
                    HIER.ORG_CODE AS STATECODE,
                    BIO.EXT_STUDENT_ID AS EXT_STUDENT_ID,
                    BIO.BARCODE AS BARCODE,
                    TO_NUMBER(TRIM(EXCP.CONTENT_CODE)) AS CONTENTAREA,
                    SUB.TEST_FORM AS TEST_FORM,
                    EXCP.ER_EXCDID AS ERR_ID
      FROM STG_STD_BIO_DETAILS     BIO,
           STG_STD_SUBTEST_DETAILS SUB,
           STG_HIER_DETAILS        HIER,
           SUBTEST_DIM             DIM,
           ER_EXCEPTION_DATA       EXCP
     WHERE BIO.PROCESS_ID = P_PROCESS_ID
       AND BIO.WKF_PARTITION_NAME IN ('ER_EXCP' /*, 'BR_EXCP'*/
           )
       AND BIO.STUDENT_BIO_DETAILS_ID = SUB.STUDENT_BIO_DETAILS_ID
       AND BIO.PROCESS_ID = SUB.PROCESS_ID
       AND BIO.WKF_PARTITION_NAME = SUB.WKF_PARTITION_NAME
       AND HIER.WKF_PARTITION_NAME =
           (SELECT DISTINCT WKF_PARTITION_NAME
              FROM STG_PROCESS_STATUS
             WHERE PROCESS_ID = P_PROCESS_ID)
       AND BIO.PROCESS_ID = HIER.PROCESS_ID
       AND HIER.ORG_LEVEL = 1
       AND DIM.SUBTEST_TYPE != 'C'
       AND SUB.CONTENT_NAME = DIM.SUBTEST_CODE
       AND NVL(SUB.STATUS_CODE, '-999') <> '3'
       AND EXCP.PROCESS_ID = P_PROCESS_ID
       AND BIO.TEST_ELEMENT_ID = EXCP.TEST_ELEMENT_ID
       AND DECODE(EXCP.CONTENT_CODE,
                  NULL,
                  '-999',
                  TO_NUMBER(TRIM(SUB.CONTENT_NAME))) =
           NVL(EXCP.CONTENT_CODE, '-999')
       AND EXCP.EXCEPTION_CODE != '351'
     ORDER BY TEST_ELEMENT_ID, CONTENTAREA, ERR_ID;

BEGIN

  SELECT COUNT(1)
    INTO V_REC_COUNT
    FROM STG_PROCESS_STATUS
   WHERE PROCESS_ID = P_PROCESS_ID
     AND ER_VALIDATION = 'IN';

  IF (V_REC_COUNT > 0) THEN
  
    FOR ERR_REC IN C_STAGING_DATA_PP LOOP
    
      SELECT COUNT(DEMO.ER_STUDID)
        INTO V_COUNT
        FROM ER_STUDENT_DEMO DEMO, ER_TEST_SCHEDULE TEST
       WHERE DEMO.ER_STUDID = TEST.ER_STUDID
         AND DEMO.STATE_CODE = ERR_REC.STATECODE
         AND DEMO.UUID = ERR_REC.EXT_STUDENT_ID;
    
      IF (V_COUNT = 0) THEN
        --UUID ERROR_CODE
        V_ERROR_FIELD := 'STATE_CODE/UUID';
        V_ERROR_CODE  := '301';
      ELSE
      
        SELECT COUNT(DEMO.ER_STUDID)
          INTO V_COUNT
          FROM ER_STUDENT_DEMO DEMO, ER_TEST_SCHEDULE TEST
         WHERE DEMO.ER_STUDID = TEST.ER_STUDID
           AND DEMO.STATE_CODE = ERR_REC.STATECODE
           AND DEMO.UUID = ERR_REC.EXT_STUDENT_ID
           AND TEST.BARCODE = ERR_REC.BARCODE;
      
        IF (V_COUNT = 0) THEN
          --BARCODE ERROR_CODE
          V_ERROR_FIELD := 'BARCODE';
          V_ERROR_CODE  := '302';
        ELSE
        
          SELECT COUNT(DEMO.ER_STUDID)
            INTO V_COUNT
            FROM ER_STUDENT_DEMO DEMO, ER_TEST_SCHEDULE TEST
           WHERE DEMO.ER_STUDID = TEST.ER_STUDID
             AND DEMO.STATE_CODE = ERR_REC.STATECODE
             AND DEMO.UUID = ERR_REC.EXT_STUDENT_ID
             AND TEST.BARCODE = ERR_REC.BARCODE
             AND TEST.CONTENT_AREA_CODE = ERR_REC.CONTENTAREA;
        
          IF (V_COUNT = 0) THEN
            --CONTENTAREA ERROR_CODE
            V_ERROR_FIELD := 'CONTENT_AREA_CODE';
            V_ERROR_CODE  := '303';
          ELSE
          
            SELECT COUNT(DEMO.ER_STUDID)
              INTO V_COUNT
              FROM ER_STUDENT_DEMO DEMO, ER_TEST_SCHEDULE TEST
             WHERE DEMO.ER_STUDID = TEST.ER_STUDID
               AND DEMO.STATE_CODE = ERR_REC.STATECODE
               AND DEMO.UUID = ERR_REC.EXT_STUDENT_ID
               AND TEST.BARCODE = ERR_REC.BARCODE
               AND TEST.CONTENT_AREA_CODE = ERR_REC.CONTENTAREA
               AND TEST.FORM = ERR_REC.TEST_FORM;
          
            IF (V_COUNT = 0) THEN
              --FORM ERROR_CODE
              V_ERROR_FIELD := 'FORM';
              V_ERROR_CODE  := '304';
            END IF;
          END IF;
        END IF;
      END IF;
      --  END IF;
    
    IF ((ERR_REC.EXT_STUDENT_ID IS NOT NULL OR LENGTH(ERR_REC.EXT_STUDENT_ID) != 0) AND
         (ERR_REC.BARCODE IS NOT NULL OR LENGTH(ERR_REC.BARCODE) != 0)) THEN
        
          UPDATE ER_EXCEPTION_DATA DATA
             SET DATA.EXCEPTION_CODE = V_ERROR_CODE,
                 DATA.DESCRIPTION    = DATA.DESCRIPTION ||
                                       ' And The Mismatch Field is: ' ||
                                       V_ERROR_FIELD
           WHERE DATA.ER_EXCDID = ERR_REC.ERR_ID;
   
  
  END IF; 
      --POPULATE STG_ER_EXCP_TEMP TABLE
    
      V_FILENAME := 'TASC_PP_ER_EXCEPTION_' || P_PROCESS_ID || '.CSV';
    
      INSERT INTO STG_ER_EXCP_TEMP
        (FILENAME,
         PROCESS_ID,
         SOURCE_SYSTEM,
         STATE_CODE,
         STUDENT_ID,
         TEST_ELEMENT_ID,
         BARCODE,
         LASTNAME,
         CONTENT_CODE,
         ERROR_CODE,
         PROCESS_DATE)
      VALUES
        (V_FILENAME,
         P_PROCESS_ID,
         'PP',
         ERR_REC.STATECODE,
         ERR_REC.EXT_STUDENT_ID,
         ERR_REC.TEST_ELEMENT_ID,
         ERR_REC.BARCODE,
         '',
         ERR_REC.CONTENTAREA,
         (SELECT DATA.EXCEPTION_CODE
            FROM ER_EXCEPTION_DATA DATA
           WHERE DATA.ER_EXCDID = ERR_REC.ERR_ID),
         SYSDATE);
      
    END LOOP;
  END IF;
  COMMIT;

END ER_PP_ERROR_CODE_POPULATION;
/