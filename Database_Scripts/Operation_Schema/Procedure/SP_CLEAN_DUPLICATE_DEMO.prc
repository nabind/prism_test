CREATE OR REPLACE PROCEDURE SP_CLEAN_DUPLICATE_DEMO IS

  V_DATE_DETECTED DATE;
  V_COUNT_DUP     NUMBER;
  V_TYP_NUMBER_ARR TYP_NUMBER_ARR;


CURSOR C_DUP_STU_DEMO_VALUES
IS
SELECT DISTINCT A.STUDENT_BIO_ID
FROM (SELECT STUDENT_BIO_ID, DEMOID, DEMO_VALID, DEMO_VALUE, COUNT(1)
  FROM STUDENT_DEMO_VALUES
 WHERE DEMOID NOT IN
       (SELECT DEMOID FROM DEMOGRAPHIC WHERE DEMO_CODE = 'Test_Format')
   AND DATETIMESTAMP > TO_DATE('01-AUG-2015','DD-MON-YYYY')
 GROUP BY STUDENT_BIO_ID, DEMOID, DEMO_VALID, DEMO_VALUE
HAVING COUNT(1) > 1)A;


CURSOR C_DUP_STU_SUB_DEMO_VALUES
IS
SELECT A.STUDENT_BIO_ID
FROM(SELECT DISTINCT STUDENT_BIO_ID, COUNT(1)
  FROM STU_SUBTEST_DEMO_VALUES
 WHERE DATETIMESTAMP > TO_DATE('01-AUG-2015','DD-MON-YYYY')
 GROUP BY STUDENT_BIO_ID, SUBTESTID, DEMOID, DEMO_VALID, DEMO_VALUE
HAVING COUNT(1) > 1) A ;

CURSOR C_DUP_STU_DEMO_COUNT
IS
SELECT COUNT(DISTINCT A.STUDENT_BIO_ID) AS STUDENT_COUNT
FROM (SELECT STUDENT_BIO_ID, DEMOID, DEMO_VALID, DEMO_VALUE, COUNT(1)
  FROM STUDENT_DEMO_VALUES
 WHERE DEMOID NOT IN
       (SELECT DEMOID FROM DEMOGRAPHIC WHERE DEMO_CODE = 'Test_Format')
   AND DATETIMESTAMP > TO_DATE('01-AUG-2015','DD-MON-YYYY')
 GROUP BY STUDENT_BIO_ID, DEMOID, DEMO_VALID, DEMO_VALUE
HAVING COUNT(1) > 1)A;


CURSOR C_DUP_STU_SUB_DEMO_COUNT
IS
SELECT COUNT(A.STUDENT_BIO_ID) AS STUDENT_COUNT
FROM(SELECT DISTINCT STUDENT_BIO_ID, COUNT(1)
  FROM STU_SUBTEST_DEMO_VALUES
 WHERE DATETIMESTAMP > TO_DATE('01-AUG-2015','DD-MON-YYYY')
 GROUP BY STUDENT_BIO_ID, SUBTESTID, DEMOID, DEMO_VALID, DEMO_VALUE
HAVING COUNT(1) > 1) A ;


BEGIN

  V_DATE_DETECTED := SYSDATE;
  V_COUNT_DUP     := 0;

   DBMS_OUTPUT.PUT_LINE('/*****************************STUDENT_DEMO_VALUES************************************/');

  ---DELETE ANY EXISTING PREVIOUS RECORDS INSERTED IN BACKUP TABLE STUDENT_DEMO_VALUES_DUP IF THE PROC RAN MORE THAN ONCE
  FOR R_DUP_STU_DEMO_COUNT IN C_DUP_STU_DEMO_COUNT
  LOOP
      IF R_DUP_STU_DEMO_COUNT.STUDENT_COUNT > 0 THEN
        SELECT COUNT(STU_DEMO_VALID)
          INTO V_COUNT_DUP
          FROM STUDENT_DEMO_VALUES_DUP
         WHERE TRUNC(DATE_DETECTED) = TRUNC(V_DATE_DETECTED);

        IF V_COUNT_DUP >= 1 THEN
          DELETE FROM STUDENT_DEMO_VALUES_DUP
           WHERE TRUNC(DATE_DETECTED) = TRUNC(V_DATE_DETECTED);
          COMMIT;
          DBMS_OUTPUT.PUT_LINE(V_COUNT_DUP ||
                               ' LAST INSERTED DUPLICATE RECORDS DELETED FROM BACKUP TABLE STUDENT_DEMO_VALUES_DUP FOR DATE_DETECTED =' ||
                               V_DATE_DETECTED);
        END IF;
    END IF;
  END LOOP;


  --- TABLE FOR THE BACK UP OF DUPLICATE RECORDS OF STUDENT_DEMO_VALUES
  OPEN C_DUP_STU_DEMO_VALUES;
  LOOP
      FETCH C_DUP_STU_DEMO_VALUES BULK COLLECT
        INTO V_TYP_NUMBER_ARR LIMIT 50;

    INSERT INTO STUDENT_DEMO_VALUES_DUP
      SELECT A.CUSTOMERID,
             A.RANK,
             A.CNT,
             A.STU_DEMO_VALID,
             A.STUDENT_BIO_ID,
             A.DEMOID,
             A.DEMO_VALID,
             A.DEMO_VALUE,
             A.DATETIMESTAMP,
             V_DATE_DETECTED AS DATE_DETECTED
        FROM (SELECT COUNT(S.STU_DEMO_VALID) OVER(PARTITION BY S.STUDENT_BIO_ID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE ORDER BY S.STUDENT_BIO_ID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE) AS CNT,
                     DENSE_RANK() OVER(PARTITION BY S.STUDENT_BIO_ID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE ORDER BY S.STUDENT_BIO_ID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE, S.DATETIMESTAMP, S.STU_DEMO_VALID) AS RANK,
                     D.CUSTOMERID,
                     S.*
                FROM STUDENT_DEMO_VALUES S, DEMOGRAPHIC D
               WHERE S.DEMOID NOT IN
                     (SELECT DEMOID
                        FROM DEMOGRAPHIC
                       WHERE DEMO_CODE = 'Test_Format')
                 AND S.DEMOID = D.DEMOID
                 AND S.STUDENT_BIO_ID IN  (SELECT COLUMN_VALUE FROM TABLE(V_TYP_NUMBER_ARR))) A
       WHERE A.CNT > 1;
    COMMIT;

    EXIT WHEN C_DUP_STU_DEMO_VALUES%NOTFOUND;
      V_TYP_NUMBER_ARR.DELETE;
    END LOOP;

  CLOSE C_DUP_STU_DEMO_VALUES;


  SELECT COUNT(1)
    INTO V_COUNT_DUP
    FROM STUDENT_DEMO_VALUES_DUP
   WHERE DATE_DETECTED = V_DATE_DETECTED;

  DBMS_OUTPUT.PUT_LINE(V_COUNT_DUP ||
                       ' NEW DUPLICATE DEMO FOUND IN STUDENT_DEMO_VALUES TABLE.');
  DBMS_OUTPUT.PUT_LINE('TO GET THE DUPLICATE RECORDS QUERY STUDENT_DEMO_VALUES_DUP WITH DATE_DETECTED = ' ||
                       V_DATE_DETECTED);

  DBMS_OUTPUT.PUT_LINE(CHR(10));



  DBMS_OUTPUT.PUT_LINE('/*****************************STU_SUBTEST_DEMO_VALUES************************************/');
  ---DELETE ANY EXISTING PREVIOUS RECORDS INSERTED IN BACKUP TABLE STU_SUBTEST_DEMO_VALUES_DUP IF THE PROC RAN MORE THAN ONCE
 FOR R_DUP_STU_SUB_DEMO_COUNT IN C_DUP_STU_SUB_DEMO_COUNT
  LOOP
    IF R_DUP_STU_SUB_DEMO_COUNT.STUDENT_COUNT > 0 THEN
        SELECT COUNT(STU_TST_DEMO_VALID)
          INTO V_COUNT_DUP
          FROM STU_SUBTEST_DEMO_VALUES_DUP
         WHERE TRUNC(DATE_DETECTED) = TRUNC(V_DATE_DETECTED);

        IF V_COUNT_DUP >= 1 THEN
          DELETE FROM STU_SUBTEST_DEMO_VALUES_DUP
           WHERE TRUNC(DATE_DETECTED) = TRUNC(V_DATE_DETECTED);
          COMMIT;
          DBMS_OUTPUT.PUT_LINE(V_COUNT_DUP ||
                               ' LAST INSERTED DUPLICATE RECORDS DELETED FROM BACKUP TABLE STU_SUBTEST_DEMO_VALUES_DUP FOR DATE_DETECTED =' ||
                               V_DATE_DETECTED);
        END IF;
      END IF;
 END LOOP;

  --- TABLE FOR THE BACK UP OF DUPLICATE RECORDS OF STU_SUBTEST_DEMO_VALUES
  OPEN C_DUP_STU_SUB_DEMO_VALUES;
  LOOP
      FETCH C_DUP_STU_SUB_DEMO_VALUES BULK COLLECT
        INTO V_TYP_NUMBER_ARR LIMIT 50;


    INSERT INTO STU_SUBTEST_DEMO_VALUES_DUP
      SELECT A.CUSTOMERID,
             A.RANK,
             A.CNT,
             A.STU_TST_DEMO_VALID,
             A.STUDENT_BIO_ID,
             A.SUBTESTID,
             A.DEMOID,
             A.DEMO_VALID,
             A.DEMO_VALUE,
             A.DATE_TEST_TAKEN,
             A.DATETIMESTAMP,
             V_DATE_DETECTED AS DATE_DETECTED
        FROM (SELECT COUNT(S.STU_TST_DEMO_VALID) OVER(PARTITION BY S.STUDENT_BIO_ID, S.SUBTESTID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE ORDER BY S.STUDENT_BIO_ID, S.SUBTESTID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE) AS CNT,
                     DENSE_RANK() OVER(PARTITION BY S.STUDENT_BIO_ID, S.SUBTESTID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE ORDER BY S.STUDENT_BIO_ID, S.SUBTESTID, S.DEMOID, S.DEMO_VALID, S.DEMO_VALUE, S.STU_TST_DEMO_VALID, S.DATE_TEST_TAKEN, S.DATETIMESTAMP) AS RANK,
                     D.CUSTOMERID,
                     S.*
                FROM STU_SUBTEST_DEMO_VALUES S, DEMOGRAPHIC D
               WHERE S.DEMOID = D.DEMOID
                AND S.STUDENT_BIO_ID IN  (SELECT COLUMN_VALUE FROM TABLE(V_TYP_NUMBER_ARR))) A
       WHERE A.CNT > 1;
    COMMIT;

  EXIT WHEN C_DUP_STU_SUB_DEMO_VALUES%NOTFOUND;
      V_TYP_NUMBER_ARR.DELETE;
    END LOOP;

  CLOSE C_DUP_STU_SUB_DEMO_VALUES;



  SELECT COUNT(1)
    INTO V_COUNT_DUP
    FROM STU_SUBTEST_DEMO_VALUES_DUP
   WHERE DATE_DETECTED = V_DATE_DETECTED;

  DBMS_OUTPUT.PUT_LINE(V_COUNT_DUP ||
                       ' NEW DUPLICATE DEMO FOUND IN STU_SUBTEST_DEMO_VALUES TABLE.');
  DBMS_OUTPUT.PUT_LINE('TO GET THE DUPLICATE RECORDS QUERY STU_SUBTEST_DEMO_VALUES_DUP WITH DATE_DETECTED = ' ||
                       V_DATE_DETECTED);

  DBMS_OUTPUT.PUT_LINE(CHR(10));

  --DELETE QUERY TO KEEP DISTINCT RECORDS IN STUDENT_DEMO_VALUES
  DELETE FROM STUDENT_DEMO_VALUES
   WHERE STU_DEMO_VALID IN
         (SELECT STU_DEMO_VALID
            FROM STUDENT_DEMO_VALUES_DUP
           WHERE RANK <> CNT
             AND DATE_DETECTED = V_DATE_DETECTED);
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('DUPLICATE ENTRIES REMOVED FROM STUDENT_DEMO_VALUES TABLE');

  --DELETE QUERY TO KEEP DISTINCT RECORDS IN STUDENT_DEMO_VALUES
  DELETE FROM STU_SUBTEST_DEMO_VALUES
   WHERE STU_TST_DEMO_VALID IN
         (SELECT STU_TST_DEMO_VALID
            FROM STU_SUBTEST_DEMO_VALUES_DUP
           WHERE RANK <> CNT
             AND DATE_DETECTED = V_DATE_DETECTED);
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('DUPLICATE ENTRIES REMOVED FROM STU_SUBTEST_DEMO_VALUES TABLE');

  --- UPDATE RECORDS OF TABLE_1_STU
  UPDATE TABLE_1_STU T
     SET T.STUDENT_BIO_ID = T.STUDENT_BIO_ID
   WHERE T.STUDENT_BIO_ID IN
         (SELECT DISTINCT STUDENT_BIO_ID
            FROM STUDENT_DEMO_VALUES_DUP
           WHERE RANK = CNT
             AND DATE_DETECTED = V_DATE_DETECTED
          UNION
          SELECT DISTINCT STUDENT_BIO_ID
            FROM STU_SUBTEST_DEMO_VALUES_DUP
           WHERE RANK = CNT
             AND DATE_DETECTED = V_DATE_DETECTED);
  COMMIT;
  -- SELECT * FROM MLOG$_TABLE_1_STU; --217
  DBMS_OUTPUT.PUT_LINE('INTERMEDIATE TABLE TABLE_1_STU TABLE REFRESHED FOR THE CORRECTED DEMO');

  --- REFRESH THE MVIEW MV_STUDENT_DETAILS
  DBMS_MVIEW.REFRESH('MV_STUDENT_DETAILS', 'F');
  DBMS_OUTPUT.PUT_LINE('MV_STUDENT_DETAILS REFERSEHED');

  --- UPDATE RECORDS OF STUDENT_BIO_DIM
  UPDATE STUDENT_BIO_DIM S
     --SET S.UPDATED_DATE_TIME = SYSDATE
     SET S.STUDENT_BIO_ID = S.STUDENT_BIO_ID
   WHERE S.STUDENT_BIO_ID IN
         (SELECT DISTINCT STUDENT_BIO_ID
            FROM STUDENT_DEMO_VALUES_DUP
           WHERE RANK = CNT
             AND DATE_DETECTED = V_DATE_DETECTED
          UNION
          SELECT DISTINCT STUDENT_BIO_ID
            FROM STU_SUBTEST_DEMO_VALUES_DUP
           WHERE RANK = CNT
             AND DATE_DETECTED = V_DATE_DETECTED);
  COMMIT;

  SELECT COUNT(1) INTO V_COUNT_DUP FROM MLOG$_STUDENT_BIO_DIM;

  --- REFRESH THE MVIEW MV_STUDENT_FILE_DOWNLOAD
  DBMS_MVIEW.REFRESH('MV_STUDENT_FILE_DOWNLOAD', 'F');
  DBMS_OUTPUT.PUT_LINE('MV_STUDENT_FILE_DOWNLOAD REFRESHED');

  --- SHOW NUMBER OF STUDENTS IMPACTED
  DBMS_OUTPUT.PUT_LINE(CHR(10));
  DBMS_OUTPUT.PUT_LINE(V_COUNT_DUP || ' STUDENTS IMPACTED ');

  ---RUN THE BELOW COMMENTED QUERY TO CHECK THE IMPACTED STUDENTS
  /*SELECT * FROM STUDENT_BIO_DIM
  WHERE STUDENT_BIO_ID IN (SELECT DISTINCT STUDENT_BIO_ID FROM STUDENT_DEMO_VALUES_DUP WHERE RANK = CNT AND TRUNC(DATE_DETECTED) = TRUNC(SYSDATE)
                       UNION
                       SELECT  DISTINCT STUDENT_BIO_ID FROM STU_SUBTEST_DEMO_VALUES_DUP WHERE RANK = CNT AND TRUNC(DATE_DETECTED) = TRUNC(SYSDATE));*/

EXCEPTION
  WHEN OTHERS THEN
    RAISE;

END SP_CLEAN_DUPLICATE_DEMO;
/
