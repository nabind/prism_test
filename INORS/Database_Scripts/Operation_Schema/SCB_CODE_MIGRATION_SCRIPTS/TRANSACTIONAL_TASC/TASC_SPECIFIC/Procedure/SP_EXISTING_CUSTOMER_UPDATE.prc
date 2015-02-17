CREATE OR REPLACE PROCEDURE SP_EXISTING_CUSTOMER_UPDATE(P_CUSTOMER_ID IN NUMBER,
                                                        OUT_ERR_MSG   OUT VARCHAR2) IS

  V_DEMOID    NUMBER;
  V_LEN       NUMBER;
  V_DEMO_CODE NUMBER := 0;
  V_REC_COUNT NUMBER := 0;
  CURSOR C_DEMOGRAPHIC_CONFIG IS
    SELECT DEMO.DEMO_NAME         AS DEMO_NAME,
           DEMO.DEMO_CODE         AS DEMO_CODE,
           DEMO.DEMO_MODE         AS DEMO_MODE,
           DEMO.SUBTESTID         AS SUBTESTID,
           DEMO.CATEGORY          AS CATEGORY,
           DEMO.IS_DEMO_VALUE_AVL AS IS_DEMO_VALUE_AVL
      FROM DEMO_CONFIG DEMO
     ORDER BY DEMO.SL_NO;

BEGIN

  FOR DEMO_DET IN C_DEMOGRAPHIC_CONFIG LOOP
  
    SELECT COUNT(D.DEMO_CODE)
      INTO V_DEMO_CODE
      FROM DEMOGRAPHIC D
     WHERE D.CUSTOMERID = P_CUSTOMER_ID
       AND D.DEMO_CODE = DEMO_DET.DEMO_CODE;
  
    IF (V_DEMO_CODE = 0) THEN
    
      INSERT INTO DEMOGRAPHIC
        (DEMOID,
         DEMO_NAME,
         DEMO_CODE,
         DEMO_MODE,
         CUSTOMERID,
         SUBTESTID,
         CATEGORY,
         IS_DEMO_VALUE_AVL,
         DATETIMESTAMP)
      VALUES
        (SEQ_DEMOID.NEXTVAL,
         DEMO_DET.DEMO_NAME,
         DEMO_DET.DEMO_CODE,
         DEMO_DET.DEMO_MODE,
         P_CUSTOMER_ID,
         DEMO_DET.SUBTESTID,
         DEMO_DET.CATEGORY,
         DEMO_DET.IS_DEMO_VALUE_AVL,
         SYSDATE)
      RETURNING DEMOID INTO V_DEMOID;
    
    END IF;
  
    SELECT COUNT(DEMOID)
      INTO V_REC_COUNT
      FROM DEMOGRAPHIC_VALUES DV
     WHERE DEMOID IN (SELECT DEMOID
                        FROM DEMOGRAPHIC
                       WHERE CUSTOMERID = P_CUSTOMER_ID
                         AND DEMO_CODE = DEMO_DET.DEMO_CODE);
  
    IF (DEMO_DET.DEMO_CODE = 'Fld_Tst_Form' OR
       DEMO_DET.DEMO_CODE = 'Test_Form') THEN
    
      SELECT DEMOID
        INTO V_DEMOID
        FROM DEMOGRAPHIC
       WHERE CUSTOMERID = P_CUSTOMER_ID
         AND DEMO_CODE = DEMO_DET.DEMO_CODE;
    
      SELECT COUNT(DEMOID)
        INTO V_REC_COUNT
        FROM DEMOGRAPHIC_VALUES DV
       WHERE DEMOID IN (SELECT DEMOID
                          FROM DEMOGRAPHIC
                         WHERE CUSTOMERID = P_CUSTOMER_ID
                           AND DEMO_CODE = DEMO_DET.DEMO_CODE)
         AND (DV.DEMO_VALUE_CODE LIKE 'D%' OR DV.DEMO_VALUE_CODE LIKE 'E%' OR
             DV.DEMO_VALUE_CODE LIKE 'F%');
    
    END IF;
  
    IF (V_REC_COUNT = 0 AND DEMO_DET.IS_DEMO_VALUE_AVL = 'Y') THEN
    
      SELECT LENGTH(D.DEMO_VALUE_CODE) -
             LENGTH(REPLACE(DEMO_VALUE_CODE, ',', '')) + 1
        INTO V_LEN
        FROM DEMO_CONFIG D
       WHERE D.DEMO_CODE = DEMO_DET.DEMO_CODE
         AND D.IS_DEMO_VALUE_AVL = DEMO_DET.IS_DEMO_VALUE_AVL;
    
      INSERT INTO DEMOGRAPHIC_VALUES
        (DEMO_VALID,
         DEMOID,
         DEMO_VALUE_NAME,
         DEMO_VALUE_CODE,
         DEMO_VALUE_SEQ,
         IS_DEFAULT,
         DATETIMESTAMP)
        SELECT SEQ_DEMO_VALID.NEXTVAL,
               V_DEMOID,
               A.DEMO_VALUE_NAME,
               A.DEMO_VALUE_CODE,
               A.N,
               A.IS_DEFAULT,
               SYSDATE
          FROM (SELECT DEMO.SL_NO AS SL_NO,
                       DEMO.DEMO_CODE,
                       REGEXP_SUBSTR(DEMO.DEMO_VALUE_NAME, '[^,]+', 1, N) AS DEMO_VALUE_NAME,
                       REGEXP_SUBSTR(DEMO.DEMO_VALUE_CODE, '[^,]+', 1, N) AS DEMO_VALUE_CODE,
                       DEMO.IS_DEFAULT AS IS_DEFAULT,
                       N
                  FROM DEMO_CONFIG DEMO,
                       (SELECT LEVEL N FROM DUAL CONNECT BY LEVEL <= V_LEN)
                 WHERE DEMO.IS_DEMO_VALUE_AVL = DEMO_DET.IS_DEMO_VALUE_AVL
                   AND N <= REGEXP_COUNT(DEMO_VALUE_NAME, '[^,]+')
                   AND DEMO.DEMO_CODE = DEMO_DET.DEMO_CODE
                 GROUP BY DEMO.SL_NO,
                          DEMO.DEMO_CODE,
                          DEMO.DEMO_VALUE_NAME,
                          DEMO.DEMO_VALUE_CODE,
                          DEMO.IS_DEFAULT,
                          N
                 ORDER BY DEMO.SL_NO, N) A;
    
    END IF;
  
  END LOOP;
  COMMIT;

EXCEPTION

  WHEN OTHERS THEN
    ROLLBACK;
    OUT_ERR_MSG := 'ERROR OCCURED';
  
END SP_EXISTING_CUSTOMER_UPDATE;
/
