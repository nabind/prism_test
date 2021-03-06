--Org subtest combinatio, if PDF is not generated for certain date
SELECT DISTINCT ORG_NODEID || '|' || SUBTESTID
  FROM SUBTEST_SCORE_FACT SSF
 WHERE TRUNC(SSF.DATETIMESTAMP) = '17-OCT-2016'
   AND (STUDENT_BIO_ID, SUBTESTID) IN
       (SELECT STUDENT_BIO_ID, SUBTESTID
          FROM SUBTEST_SCORE_FACT SSF
         WHERE TRUNC(SSF.DATETIMESTAMP) = '17-OCT-2016'
        MINUS
        SELECT DISTINCT SSF.STUDENT_BIO_ID, SSF.SUBTESTID
          FROM SUBTEST_SCORE_FACT SSF, STUDENT_PDF_LOG SPL
         WHERE SSF.STUDENT_BIO_ID = SPL.STUDENT_BIO_ID
           AND SSF.SUBTESTID = SPL.SUBTESTID
           AND TRUNC(SPL.CREATED_DATE_TIME) = TRUNC(SSF.DATETIMESTAMP)
           AND TRUNC(SPL.CREATED_DATE_TIME) = '17-OCT-2016')