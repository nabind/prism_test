CREATE OR REPLACE PACKAGE PKG_FILE_TRACKING AS

  TYPE GET_REFCURSOR IS REF CURSOR;

  PROCEDURE SP_GET_DATA_ER(P_PROCESS_STATUS         IN VARCHAR2,
                           P_DATE_FROM              IN VARCHAR2,
                           P_DATE_TO                IN VARCHAR2,
                           P_UUID                   IN VARCHAR2,
                           P_LAST_NAME              IN VARCHAR2,
                           P_EX_CODE                IN NUMBER,
                           P_RECORD_ID              IN NUMBER,
                           P_PROCESS_ID             IN NUMBER,
                           P_STATE_CODE             IN VARCHAR2,
                           P_FORM                   IN VARCHAR2,
                           P_TEST_ELEMENT_ID        IN VARCHAR2,
                           P_TEST_BARCODE           IN VARCHAR2,
                           P_SEARCH_PARAM           IN VARCHAR2,
                           P_ORDERED_COLUMN         IN VARCHAR2,
                           P_ORDER                  IN VARCHAR2,
                           P_ROWNUM_FROM            IN NUMBER,
                           P_ROWNUM_TO              IN NUMBER,
                           P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                           P_OUT_CUR_ER_DATA        OUT GET_REFCURSOR,
                           P_OUT_CUR_ER_DATA_CSV    OUT GET_REFCURSOR,
                           P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  PROCEDURE SP_GET_DATA_OL_PP(P_PROCESS_STATUS         IN VARCHAR2,
                              P_DATE_FROM              IN VARCHAR2,
                              P_DATE_TO                IN VARCHAR2,
                              P_UUID                   IN VARCHAR2,
                              P_LAST_NAME              IN VARCHAR2,
                              P_EX_CODE                IN NUMBER,
                              P_SOURCE_SYSTEM          IN VARCHAR2,
                              P_PROCESS_ID             IN NUMBER,
                              P_STATE_CODE             IN VARCHAR2,
                              P_FORM                   IN VARCHAR2,
                              P_TEST_ELEMENT_ID        IN VARCHAR2,
                              P_TEST_BARCODE           IN VARCHAR2,
                              P_SEARCH_PARAM           IN VARCHAR2,
                              P_ORDERED_COLUMN         IN VARCHAR2,
                              P_ORDER                  IN VARCHAR2,
                              P_ROWNUM_FROM            IN NUMBER,
                              P_ROWNUM_TO              IN NUMBER,
                              P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                              P_OUT_CUR_ER_DATA        OUT GET_REFCURSOR,
                              P_OUT_CUR_ER_DATA_CSV    OUT GET_REFCURSOR,
                              P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  PROCEDURE SP_GET_DATA_WINSCORE(P_PROCESS_STATUS         IN VARCHAR2,
                                 P_DATE_FROM              IN VARCHAR2,
                                 P_DATE_TO                IN VARCHAR2,
                                 P_IMAGING_ID             IN VARCHAR2,
                                 P_BARCODE                IN VARCHAR2,
                                 P_SEARCH_PARAM           IN VARCHAR2,
                                 P_ORDERED_COLUMN         IN VARCHAR2,
                                 P_ORDER                  IN VARCHAR2,
                                 P_ROWNUM_FROM            IN NUMBER,
                                 P_ROWNUM_TO              IN NUMBER,
                                 P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                                 P_OUT_CUR_DATA           OUT GET_REFCURSOR,
                                 P_OUT_CUR_DATA_CSV       OUT GET_REFCURSOR,
                                 P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  PROCEDURE SP_GET_DATA_GHI(P_PROCESS_STATUS         IN VARCHAR2,
                            P_DATE_FROM              IN VARCHAR2,
                            P_DATE_TO                IN VARCHAR2,
                            P_DRC_STUDENT_ID         IN VARCHAR2,
                            P_DRC_DOCUMENT_ID        IN VARCHAR2,
                            P_UUID                   IN VARCHAR2,
                            P_LAST_NAME              IN VARCHAR2,
                            P_STATE_CODE             IN VARCHAR2,
                            P_FORM                   IN VARCHAR2,
                            P_TEST_ELEMENT_ID        IN VARCHAR2,
                            P_BARCODE                IN VARCHAR2,
                            P_SEARCH_PARAM           IN VARCHAR2,
                            P_ORDERED_COLUMN         IN VARCHAR2,
                            P_ORDER                  IN VARCHAR2,
                            P_ROWNUM_FROM            IN NUMBER,
                            P_ROWNUM_TO              IN NUMBER,
                            P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                            P_OUT_CUR_DATA           OUT GET_REFCURSOR,
                            P_OUT_CUR_DATA_CSV       OUT GET_REFCURSOR,
                            P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);
END PKG_FILE_TRACKING;
/
CREATE OR REPLACE PACKAGE BODY PKG_FILE_TRACKING AS

  PROCEDURE SP_GET_DATA_ER(P_PROCESS_STATUS         IN VARCHAR2,
                           P_DATE_FROM              IN VARCHAR2,
                           P_DATE_TO                IN VARCHAR2,
                           P_UUID                   IN VARCHAR2,
                           P_LAST_NAME              IN VARCHAR2,
                           P_EX_CODE                IN NUMBER,
                           P_RECORD_ID              IN NUMBER,
                           P_PROCESS_ID             IN NUMBER,
                           P_STATE_CODE             IN VARCHAR2,
                           P_FORM                   IN VARCHAR2,
                           P_TEST_ELEMENT_ID        IN VARCHAR2,
                           P_TEST_BARCODE           IN VARCHAR2,
                           P_SEARCH_PARAM           IN VARCHAR2,
                           P_ORDERED_COLUMN         IN VARCHAR2,
                           P_ORDER                  IN VARCHAR2,
                           P_ROWNUM_FROM            IN NUMBER,
                           P_ROWNUM_TO              IN NUMBER,
                           P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                           P_OUT_CUR_ER_DATA        OUT GET_REFCURSOR,
                           P_OUT_CUR_ER_DATA_CSV    OUT GET_REFCURSOR,
                           P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    V_QUERY_PAGING             CLOB := '';
    V_QUERY_ACTUAL             CLOB := '';
    V_QUERY_TOTAL_RECORD_COUNT CLOB := '';
    V_EXCEPTION_STATUS         VARCHAR2(100) := '';
    V_SEARCH_PARAM             VARCHAR2(100);
    V_SEARCH_PARAM_COUNT       NUMBER := 0;
    V_CUR_TOTAL_RECORD_COUNT   GET_REFCURSOR;
  
  BEGIN
  
    V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SELECT DISTINCT ESSH.LASTNAME || '', '' || ESSH.FIRSTNAME || '' '' ||
                    ESSH.MIDDLENAME STUDENTNAME,
                    ESSH.UUID UUID,
                    TO_CHAR(NVL(EED.TEST_ELEMENT_ID, ''NA'')) TEST_ELEMENT_ID,
                    NVL(TO_CHAR(EED.PROCESS_ID), ''NA'') PROCESS_ID,
                    TO_CHAR(NVL(EED.EXCEPTION_CODE, ''NA'')) EXCEPTION_CODE,
                    NVL(EED.SOURCE_SYSTEM, ''ERESOURCE'') SOURCE_SYSTEM,
                    NVL(EED.EXCEPTION_STATUS, ''CO'') EXCEPTION_STATUS,
                    ESSH.ER_SS_HISTID ER_SS_HISTID,
                    ESSH.BARCODE BARCODE,
                    ESSH.DATE_SCHEDULED DATE_SCHEDULED,
                    ESSH.STATE_CODE STATE_CODE,
                    ESSH.FORM FORM,
                    ESSH.DATETIMESTAMP,
                    NVL(EED.ER_EXCDID, 0) ER_EXCDID,
                    (SELECT SUBTEST_NAME
                       FROM SUBTEST_DIM
                      WHERE SUBTEST_CODE = ESSH.CONTENT_AREA_CODE) SUBTEST,
                    ESSH.TESTCENTERCODE TESTING_SITE_CODE,
                    ESSH.TESTCENTERNAME TESTING_SITE_NAME,
                    ESSH.CTB_CUSTOMER_ID CTB_CUSTOMER_ID,
                    ESSH.STATENAME STATENAME,
                    ESSH.DATEOFBIRTH DATEOFBIRTH,
                    ESSH.GENDER GENDER,
                    ESSH.GOVERNMENTID GOVERNMENTID,
                    ESSH.GOVERNMENTIDTYPE GOVERNMENTIDTYPE,
                    ESSH.ADDRESS1 ADDRESS1,
                    ESSH.CITY CITY,
                    ESSH.COUNTY COUNTY,
                    ESSH.STATE STATE,
                    ESSH.ZIP ZIP,
                    ESSH.EMAIL EMAIL,
                    ESSH.ALTERNATEEMAIL ALTERNATEEMAIL,
                    ESSH.PRIMARYPHONENUMBER PRIMARYPHONENUMBER,
                    ESSH.CELLPHONENUMBER CELLPHONENUMBER,
                    ESSH.ALTERNATENUMBER ALTERNATENUMBER,
                    ESSH.RESOLVED_ETHNICITY_RACE RESOLVED_ETHNICITY_RACE,
                    ESSH.HOMELANGUAGE HOMELANGUAGE,
                    ESSH.EDUCATIONLEVEL EDUCATIONLEVEL,
                    ESSH.ATTENDCOLLEGE ATTENDCOLLEGE,
                    ESSH.CONTACT CONTACT,
                    ESSH.EXAMINEECOUNTYPARISHCODE EXAMINEECOUNTYPARISHCODE,
                    ESSH.REGISTEREDON REGISTEREDON,
                    ESSH.REGISTEREDATTESTCENTER REGISTEREDATTESTCENTER,
                    ESSH.REGISTEREDATTESTCENTERCODE REGISTEREDATTESTCENTERCODE,
                    ESSH.SCHEDULE_ID SCHEDULE_ID,
                    ESSH.TIMEOFDAY TIMEOFDAY,
                    ESSH.DATECHECKEDIN DATECHECKEDIN,
                    ESSH.CONTENT_TEST_TYPE CONTENT_TEST_TYPE,
                    ESSH.CONTENT_TEST_CODE CONTENT_TEST_CODE,
                    ESSH.TASCREADINESS TASCREADINESS,
                    ESSH.ECC ECC,
                    ESSH.REGST_TC_COUNTYPARISHCODE REGST_TC_COUNTYPARISHCODE,
                    ESSH.SCHED_TC_COUNTYPARISHCODE SCHED_TC_COUNTYPARISHCODE,
                    DECODE(NVL(EED.ER_EXCDID, 0), 0, '''', ''ERROR CODE-'' || EED.EXCEPTION_CODE || '': '' || EED.DESCRIPTION) ERROR_DESCRIPTION,
                    TO_CHAR(ESSH.DATETIMESTAMP, ''MM/DD/YYYY HH24:MI:SS'') PROCESSED_DATE
      FROM ER_STUDENT_SCHED_HISTORY ESSH
      LEFT OUTER JOIN ER_EXCEPTION_DATA EED
        ON ESSH.ER_SS_HISTID = EED.ER_SS_HISTID
     WHERE 1 = 1';
  
    IF P_PROCESS_STATUS <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.EXCEPTION_STATUS =  ''' ||
                        P_PROCESS_STATUS || '''';
    END IF;
  
    IF P_DATE_FROM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(ESSH.DATETIMESTAMP) >= TO_DATE(''' ||
                        P_DATE_FROM || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_DATE_TO <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(ESSH.DATETIMESTAMP) <= TO_DATE(''' ||
                        P_DATE_TO || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_UUID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND ESSH.UUID LIKE ''%' ||
                        P_UUID || '%''';
    END IF;
  
    IF P_LAST_NAME <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND UPPER(ESSH.LASTNAME) LIKE UPPER(''%' ||
                        P_LAST_NAME || '%'')';
    END IF;
  
    IF P_EX_CODE <> -1 THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.EXCEPTION_CODE = ' ||
                        P_EX_CODE;
    END IF;
  
    IF P_RECORD_ID <> -1 THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND ESSH.ER_SS_HISTID = ' ||
                        P_RECORD_ID;
    END IF;
  
    IF P_PROCESS_ID <> -1 THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.PROCESS_ID = ' ||
                        P_PROCESS_ID;
    END IF;
  
    IF P_STATE_CODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND ESSH.STATE_CODE =  ''' ||
                        P_STATE_CODE || '''';
    END IF;
  
    IF P_FORM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND ESSH.FORM =  ''' || P_FORM || '''';
    END IF;
  
    IF P_TEST_ELEMENT_ID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND ESSH.TEST_ELEMENT_ID =  ''' ||
                        P_TEST_ELEMENT_ID || '''';
    END IF;
  
    IF P_TEST_BARCODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND ESSH.BARCODE =  ''' ||
                        P_TEST_BARCODE || '''';
    END IF;
  
    IF P_SEARCH_PARAM <> '-1' THEN
      V_QUERY_ACTUAL := 'SELECT * FROM (' || V_QUERY_ACTUAL ||
                        ') TAB_SEARCH WHERE UPPER(STUDENTNAME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(UUID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TEST_ELEMENT_ID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(PROCESS_ID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(EXCEPTION_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(ER_SS_HISTID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(BARCODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DATE_SCHEDULED) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(STATE_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(FORM) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(ER_EXCDID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(PROCESSED_DATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SUBTEST) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM || '%'')';
    
      V_SEARCH_PARAM := '%' || P_SEARCH_PARAM || '%';
    
      SELECT COUNT(TAB.EXCEPTION_STATUS)
        INTO V_SEARCH_PARAM_COUNT
        FROM (WITH T AS (SELECT 'ERROR,COMPLETED,INVALIDATED' AS TXT
                           FROM DUAL)
               SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS EXCEPTION_STATUS
                 FROM T
               CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1) TAB
                WHERE TAB.EXCEPTION_STATUS LIKE UPPER(V_SEARCH_PARAM);
    
    
      IF V_SEARCH_PARAM_COUNT <> 0 THEN
        IF V_SEARCH_PARAM_COUNT = 3 THEN
          V_EXCEPTION_STATUS := '''ER''' || ',' || '''CO''' || ',' ||
                                '''IN''';
        ELSE
          V_EXCEPTION_STATUS := '';
          FOR REC IN (SELECT TAB.EXCEPTION_STATUS ES
                        FROM (WITH T AS (SELECT 'ERROR,COMPLETED,INVALIDATED' AS TXT
                                           FROM DUAL)
                               SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS EXCEPTION_STATUS
                                 FROM T
                               CONNECT BY LEVEL <=
                                          LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1) TAB
                                WHERE TAB.EXCEPTION_STATUS LIKE
                                      UPPER(V_SEARCH_PARAM)
                      ) LOOP
            V_SEARCH_PARAM := REC.ES;
            IF V_SEARCH_PARAM = 'ERROR' THEN
              V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''ER''' || ',';
            ELSIF V_SEARCH_PARAM = 'COMPLETED' THEN
              V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''CO''' || ',';
            ELSIF V_SEARCH_PARAM = 'INVALIDATED' THEN
              V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''IN''' || ',';
            END IF;
          END LOOP;
          V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''''';
        END IF;
      
        /*DBMS_OUTPUT.PUT_LINE('V_EXCEPTION_STATUS: ' || V_EXCEPTION_STATUS);*/
      
        IF V_SEARCH_PARAM_COUNT <> 0 THEN
          V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' OR EXCEPTION_STATUS IN ( ' ||
                            V_EXCEPTION_STATUS || ')';
        END IF;
      END IF;
    END IF;
  
    V_QUERY_TOTAL_RECORD_COUNT := V_QUERY_TOTAL_RECORD_COUNT ||
                                  'SELECT COUNT(1) TOTAL_RECORD_COUNT FROM (' ||
                                  V_QUERY_ACTUAL || ') TAB';
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_TOTAL_RECORD_COUNT: ' ||
    V_QUERY_TOTAL_RECORD_COUNT);*/
  
    OPEN V_CUR_TOTAL_RECORD_COUNT FOR V_QUERY_TOTAL_RECORD_COUNT;
    IF V_CUR_TOTAL_RECORD_COUNT%ISOPEN THEN
      LOOP
        FETCH V_CUR_TOTAL_RECORD_COUNT
          INTO P_OUT_TOTAL_RECORD_COUNT;
        EXIT WHEN V_CUR_TOTAL_RECORD_COUNT%NOTFOUND;
        /*DBMS_OUTPUT.PUT_LINE('P_OUT_TOTAL_RECORD_COUNT: ' ||
        P_OUT_TOTAL_RECORD_COUNT);*/
      END LOOP;
      CLOSE V_CUR_TOTAL_RECORD_COUNT;
    END IF;
  
    IF P_ORDERED_COLUMN <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ORDER BY ';
    END IF;
  
    IF P_ORDERED_COLUMN = '1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ER_SS_HISTID ';
    ELSIF P_ORDERED_COLUMN = '2' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STUDENTNAME ';
    ELSIF P_ORDERED_COLUMN = '3' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' UUID ';
    ELSIF P_ORDERED_COLUMN = '4' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TEST_ELEMENT_ID ';
    ELSIF P_ORDERED_COLUMN = '5' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_CODE ';
    ELSIF P_ORDERED_COLUMN = '6' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_STATUS ';
    ELSIF P_ORDERED_COLUMN = '7' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BARCODE ';
    ELSIF P_ORDERED_COLUMN = '8' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DATE_SCHEDULED ';
    ELSIF P_ORDERED_COLUMN = '9' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STATE_CODE ';
    ELSIF P_ORDERED_COLUMN = '10' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FORM ';
    ELSIF P_ORDERED_COLUMN = '11' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SUBTEST ';
    ELSIF P_ORDERED_COLUMN = '12' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' PROCESSED_DATE ';
    END IF;
  
    IF P_ORDER = 'asc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ASC ';
    ELSIF P_ORDER = 'desc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DESC ';
    END IF;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_ACTUAL: ' || V_QUERY_ACTUAL);*/
    OPEN P_OUT_CUR_ER_DATA_CSV FOR V_QUERY_ACTUAL;
  
    V_QUERY_PAGING := V_QUERY_PAGING ||
                      'SELECT *
        FROM (SELECT ROWNUM RNUM, A.* FROM (' ||
                      V_QUERY_ACTUAL || ') A) WHERE RNUM <= ' ||
                      P_ROWNUM_TO || '
       AND RNUM >= ' || P_ROWNUM_FROM;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_PAGING: ' || V_QUERY_PAGING);*/
    OPEN P_OUT_CUR_ER_DATA FOR V_QUERY_PAGING;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_DATA_ER;

  PROCEDURE SP_GET_DATA_OL_PP(P_PROCESS_STATUS         IN VARCHAR2,
                              P_DATE_FROM              IN VARCHAR2,
                              P_DATE_TO                IN VARCHAR2,
                              P_UUID                   IN VARCHAR2,
                              P_LAST_NAME              IN VARCHAR2,
                              P_EX_CODE                IN NUMBER,
                              P_SOURCE_SYSTEM          IN VARCHAR2,
                              P_PROCESS_ID             IN NUMBER,
                              P_STATE_CODE             IN VARCHAR2,
                              P_FORM                   IN VARCHAR2,
                              P_TEST_ELEMENT_ID        IN VARCHAR2,
                              P_TEST_BARCODE           IN VARCHAR2,
                              P_SEARCH_PARAM           IN VARCHAR2,
                              P_ORDERED_COLUMN         IN VARCHAR2,
                              P_ORDER                  IN VARCHAR2,
                              P_ROWNUM_FROM            IN NUMBER,
                              P_ROWNUM_TO              IN NUMBER,
                              P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                              P_OUT_CUR_ER_DATA        OUT GET_REFCURSOR,
                              P_OUT_CUR_ER_DATA_CSV    OUT GET_REFCURSOR,
                              P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    V_QUERY_PAGING             CLOB := '';
    V_QUERY_ACTUAL             CLOB := '';
    V_QUERY_TOTAL_RECORD_COUNT CLOB := '';
    V_EXCEPTION_STATUS         VARCHAR2(100) := '';
    V_SEARCH_PARAM             VARCHAR2(100);
    V_SEARCH_PARAM_COUNT       NUMBER := 0;
    V_CUR_TOTAL_RECORD_COUNT   GET_REFCURSOR;
  
  BEGIN
  
    V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                      ' SELECT EED.LAST_NAME STUDENTNAME,
       EED.ER_UUID UUID,
       EED.TEST_ELEMENT_ID TEST_ELEMENT_ID,
       EED.PROCESS_ID PROCESS_ID,
       EED.EXCEPTION_CODE EXCEPTION_CODE,
       EED.SOURCE_SYSTEM SOURCE_SYSTEM,
       EED.EXCEPTION_STATUS EXCEPTION_STATUS,
       0 ER_SS_HISTID,
       EED.BARCODE BARCODE,
       TO_CHAR(EED.TEST_DATE,''MM/DD/YYYY'') DATE_SCHEDULED,
       EED.STATE_CODE STATE_CODE,
       EED.FORM FORM,
       EED.ER_EXCDID ER_EXCDID,
       (SELECT SUBTEST_NAME
          FROM SUBTEST_DIM
         WHERE SUBTEST_CODE = EED.CONTENT_CODE) SUBTEST,
       EED.TESTING_SITE_CODE TESTING_SITE_CODE,
       EED.TESTING_SITE_NAME TESTING_SITE_NAME,
       EED.TEST_LANGUAGE TEST_LANGUAGE,
       EED.LITHOCODE LITHOCODE,
       TO_CHAR(EED.SCORING_DATE,''MM/DD/YYYY'') SCORING_DATE,
       TO_CHAR(EED.SCANNED_DATE,''MM/DD/YYYY'') SCANNED_DATE,
       EED.NCR_SCORE NCR_SCORE,
       EED.CONTENT_STATUS_CODE CONTENT_STATUS_CODE,
       EED.SCAN_BATCH SCAN_BATCH,
       EED.SCAN_STACK SCAN_STACK,
       EED.SCAN_SEQUENCE SCAN_SEQUENCE,
       EED.BIO_IMAGES BIO_IMAGES,
       DECODE(NVL(EED.ER_EXCDID, 0),
              0,
              '''',
              ''ERROR CODE-'' || EED.EXCEPTION_CODE || '': '' || EED.DESCRIPTION) ERROR_DESCRIPTION,
       TO_CHAR(EED.CREATED_DATE_TIME, ''MM/DD/YYYY HH24:MI:SS'') PROCESSED_DATE
       FROM ER_EXCEPTION_DATA EED
       WHERE EED.SOURCE_SYSTEM =  ''' ||
                      P_SOURCE_SYSTEM || '''';
  
    IF P_PROCESS_STATUS <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.EXCEPTION_STATUS =  ''' ||
                        P_PROCESS_STATUS || '''';
    END IF;
  
    IF P_DATE_FROM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(EED.CREATED_DATE_TIME) >= TO_DATE(''' ||
                        P_DATE_FROM || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_DATE_TO <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(EED.CREATED_DATE_TIME) <= TO_DATE(''' ||
                        P_DATE_TO || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_UUID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.ER_UUID LIKE ''%' ||
                        P_UUID || '%''';
    END IF;
  
    IF P_LAST_NAME <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND UPPER(EED.LAST_NAME) LIKE UPPER(''%' ||
                        P_LAST_NAME || '%'')';
    END IF;
  
    IF P_EX_CODE <> -1 THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.EXCEPTION_CODE = ' ||
                        P_EX_CODE;
    END IF;
  
    IF P_PROCESS_ID <> -1 THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.PROCESS_ID = ' ||
                        P_PROCESS_ID;
    END IF;
  
    IF P_STATE_CODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.STATE_CODE =  ''' ||
                        P_STATE_CODE || '''';
    END IF;
  
    IF P_FORM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.FORM =  ''' || P_FORM || '''';
    END IF;
  
    IF P_TEST_ELEMENT_ID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.TEST_ELEMENT_ID =  ''' ||
                        P_TEST_ELEMENT_ID || '''';
    END IF;
  
    IF P_TEST_BARCODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EED.BARCODE =  ''' ||
                        P_TEST_BARCODE || '''';
    END IF;
  
    IF P_SEARCH_PARAM <> '-1' THEN
      V_QUERY_ACTUAL := 'SELECT * FROM (' || V_QUERY_ACTUAL ||
                        ') TAB_SEARCH WHERE UPPER(STUDENTNAME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(UUID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TEST_ELEMENT_ID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(PROCESS_ID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(EXCEPTION_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(BARCODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DATE_SCHEDULED) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(STATE_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(FORM) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(ER_EXCDID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(PROCESSED_DATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SUBTEST) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM || '%'')';
    
      V_SEARCH_PARAM := '%' || P_SEARCH_PARAM || '%';
    
      SELECT COUNT(TAB.EXCEPTION_STATUS)
        INTO V_SEARCH_PARAM_COUNT
        FROM (WITH T AS (SELECT 'ERROR,COMPLETED,INVALIDATED' AS TXT
                           FROM DUAL)
               SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS EXCEPTION_STATUS
                 FROM T
               CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1) TAB
                WHERE TAB.EXCEPTION_STATUS LIKE UPPER(V_SEARCH_PARAM);
    
    
      IF V_SEARCH_PARAM_COUNT <> 0 THEN
        IF V_SEARCH_PARAM_COUNT = 3 THEN
          V_EXCEPTION_STATUS := '''ER''' || ',' || '''CO''' || ',' ||
                                '''IN''';
        ELSE
          V_EXCEPTION_STATUS := '';
          FOR REC IN (SELECT TAB.EXCEPTION_STATUS ES
                        FROM (WITH T AS (SELECT 'ERROR,COMPLETED,INVALIDATED' AS TXT
                                           FROM DUAL)
                               SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS EXCEPTION_STATUS
                                 FROM T
                               CONNECT BY LEVEL <=
                                          LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1) TAB
                                WHERE TAB.EXCEPTION_STATUS LIKE
                                      UPPER(V_SEARCH_PARAM)
                      ) LOOP
            V_SEARCH_PARAM := REC.ES;
            IF V_SEARCH_PARAM = 'ERROR' THEN
              V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''ER''' || ',';
            ELSIF V_SEARCH_PARAM = 'COMPLETED' THEN
              V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''CO''' || ',';
            ELSIF V_SEARCH_PARAM = 'INVALIDATED' THEN
              V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''IN''' || ',';
            END IF;
          END LOOP;
          V_EXCEPTION_STATUS := V_EXCEPTION_STATUS || '''''';
        END IF;
      
        /*DBMS_OUTPUT.PUT_LINE('V_EXCEPTION_STATUS: ' || V_EXCEPTION_STATUS);*/
      
        IF V_SEARCH_PARAM_COUNT <> 0 THEN
          V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' OR EXCEPTION_STATUS IN ( ' ||
                            V_EXCEPTION_STATUS || ')';
        END IF;
      END IF;
    END IF;
  
    V_QUERY_TOTAL_RECORD_COUNT := V_QUERY_TOTAL_RECORD_COUNT ||
                                  'SELECT COUNT(1) FROM (' ||
                                  V_QUERY_ACTUAL || ') TAB';
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_TOTAL_RECORD_COUNT: ' ||
    V_QUERY_TOTAL_RECORD_COUNT);*/
  
    OPEN V_CUR_TOTAL_RECORD_COUNT FOR V_QUERY_TOTAL_RECORD_COUNT;
    IF V_CUR_TOTAL_RECORD_COUNT%ISOPEN THEN
      LOOP
        FETCH V_CUR_TOTAL_RECORD_COUNT
          INTO P_OUT_TOTAL_RECORD_COUNT;
        EXIT WHEN V_CUR_TOTAL_RECORD_COUNT%NOTFOUND;
        /*DBMS_OUTPUT.PUT_LINE('P_OUT_TOTAL_RECORD_COUNT: ' ||
        P_OUT_TOTAL_RECORD_COUNT);*/
      END LOOP;
      CLOSE V_CUR_TOTAL_RECORD_COUNT;
    END IF;
  
    IF P_ORDERED_COLUMN <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ORDER BY ';
    END IF;
  
    IF P_ORDERED_COLUMN = '1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' PROCESS_ID ';
    ELSIF P_ORDERED_COLUMN = '2' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STUDENTNAME ';
    ELSIF P_ORDERED_COLUMN = '3' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' UUID ';
    ELSIF P_ORDERED_COLUMN = '4' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TEST_ELEMENT_ID ';
    ELSIF P_ORDERED_COLUMN = '5' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_CODE ';
    ELSIF P_ORDERED_COLUMN = '6' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_STATUS ';
    ELSIF P_ORDERED_COLUMN = '7' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BARCODE ';
    ELSIF P_ORDERED_COLUMN = '8' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DATE_SCHEDULED ';
    ELSIF P_ORDERED_COLUMN = '9' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STATE_CODE ';
    ELSIF P_ORDERED_COLUMN = '10' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FORM ';
    ELSIF P_ORDERED_COLUMN = '11' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SUBTEST ';
    ELSIF P_ORDERED_COLUMN = '12' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' PROCESSED_DATE ';
    END IF;
  
    IF P_ORDER = 'asc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ASC ';
    ELSIF P_ORDER = 'desc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DESC ';
    END IF;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_ACTUAL: ' || V_QUERY_ACTUAL);*/
    OPEN P_OUT_CUR_ER_DATA_CSV FOR V_QUERY_ACTUAL;
  
    V_QUERY_PAGING := V_QUERY_PAGING ||
                      'SELECT *
        FROM (SELECT ROWNUM RNUM, A.* FROM (' ||
                      V_QUERY_ACTUAL || ') A) WHERE RNUM <= ' ||
                      P_ROWNUM_TO || '
       AND RNUM >= ' || P_ROWNUM_FROM;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_PAGING: ' || V_QUERY_PAGING);*/
    OPEN P_OUT_CUR_ER_DATA FOR V_QUERY_PAGING;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_DATA_OL_PP;

  PROCEDURE SP_GET_DATA_WINSCORE(P_PROCESS_STATUS         IN VARCHAR2,
                                 P_DATE_FROM              IN VARCHAR2,
                                 P_DATE_TO                IN VARCHAR2,
                                 P_IMAGING_ID             IN VARCHAR2,
                                 P_BARCODE                IN VARCHAR2,
                                 P_SEARCH_PARAM           IN VARCHAR2,
                                 P_ORDERED_COLUMN         IN VARCHAR2,
                                 P_ORDER                  IN VARCHAR2,
                                 P_ROWNUM_FROM            IN NUMBER,
                                 P_ROWNUM_TO              IN NUMBER,
                                 P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                                 P_OUT_CUR_DATA           OUT GET_REFCURSOR,
                                 P_OUT_CUR_DATA_CSV       OUT GET_REFCURSOR,
                                 P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    V_QUERY_PAGING             CLOB := '';
    V_QUERY_ACTUAL             CLOB := '';
    V_QUERY_TOTAL_RECORD_COUNT CLOB := '';
    V_CUR_TOTAL_RECORD_COUNT   GET_REFCURSOR;
  
  BEGIN
  
    V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SELECT SCAN_BATCH,
                           DISTRICT_NUMBER,
                           SCHOOL_NUMBER,
                           UUID,
                           BARCODE,
                           TEST_FORM,
                           BRAILLE,
                           LARGE_PRINT,
                           DATE_TEST_TAKEN,
                           TO_CHAR(LOGINDATE, ''MM/DD/YYYY HH24:MI:SS'') LOGINDATE,
                           TO_CHAR(SCAN_DATE, ''MM/DD/YYYY HH24:MI:SS'') SCAN_DATE,
                           TO_CHAR(WINS_EXPORT_DATE, ''MM/DD/YYYY HH24:MI:SS'') WINS_EXPORT_DATE,
                           IMAGING_ID,
                           ORGTP_NAME,
                           LAST_NAME,
                           FIRST_NAME,
                           MIDDLE_INITIAL,
                           LITHOCODE,
                           SCAN_STACK,
                           SCAN_SEQ,
                           WINS_DOCID,
                           COMMODITY_CODE,
                           WINSTATUS,
                           PRISM_PROCESS_STATUS,
                           IMAGE_FILEPATH,
                           IMAGE_FILENAMES
                      FROM WINS_DOC_INFO
                     WHERE 1=1 ';
  
    IF P_PROCESS_STATUS <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND PRISM_PROCESS_STATUS =  ''' ||
                        P_PROCESS_STATUS || '''';
    END IF;
  
    IF P_DATE_FROM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(WINS_EXPORT_DATE) >= TO_DATE(''' ||
                        P_DATE_FROM || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_DATE_TO <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(WINS_EXPORT_DATE) <= TO_DATE(''' ||
                        P_DATE_TO || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_IMAGING_ID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND IMAGING_ID LIKE ''%' ||
                        P_IMAGING_ID || '%''';
    END IF;
  
    IF P_BARCODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND BARCODE LIKE ''%' ||
                        P_BARCODE || '%''';
    END IF;
  
    IF P_SEARCH_PARAM <> '-1' THEN
      V_QUERY_ACTUAL := 'SELECT * FROM (' || V_QUERY_ACTUAL ||
                        ') TAB_SEARCH WHERE UPPER(SCAN_BATCH) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DISTRICT_NUMBER) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SCHOOL_NUMBER) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(UUID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(BARCODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TEST_FORM) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(BRAILLE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(LARGE_PRINT) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DATE_TEST_TAKEN) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(LOGINDATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SCAN_DATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(WINS_EXPORT_DATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM || '%'')';
    END IF;
  
    V_QUERY_TOTAL_RECORD_COUNT := V_QUERY_TOTAL_RECORD_COUNT ||
                                  'SELECT COUNT(1) FROM (' ||
                                  V_QUERY_ACTUAL || ') TAB';
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_TOTAL_RECORD_COUNT: ' ||
    V_QUERY_TOTAL_RECORD_COUNT);*/
  
    OPEN V_CUR_TOTAL_RECORD_COUNT FOR V_QUERY_TOTAL_RECORD_COUNT;
    IF V_CUR_TOTAL_RECORD_COUNT%ISOPEN THEN
      LOOP
        FETCH V_CUR_TOTAL_RECORD_COUNT
          INTO P_OUT_TOTAL_RECORD_COUNT;
        EXIT WHEN V_CUR_TOTAL_RECORD_COUNT%NOTFOUND;
        /*DBMS_OUTPUT.PUT_LINE('P_OUT_TOTAL_RECORD_COUNT: ' ||
        P_OUT_TOTAL_RECORD_COUNT);*/
      END LOOP;
      CLOSE V_CUR_TOTAL_RECORD_COUNT;
    END IF;
  
    IF P_ORDERED_COLUMN <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ORDER BY ';
    END IF;
  
    IF P_ORDERED_COLUMN = '1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SCAN_BATCH ';
    ELSIF P_ORDERED_COLUMN = '2' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DISTRICT_NUMBER ';
    ELSIF P_ORDERED_COLUMN = '3' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SCHOOL_NUMBER ';
    ELSIF P_ORDERED_COLUMN = '4' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' UUID ';
    ELSIF P_ORDERED_COLUMN = '5' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BARCODE ';
    ELSIF P_ORDERED_COLUMN = '6' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TEST_FORM ';
    ELSIF P_ORDERED_COLUMN = '7' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BRAILLE ';
    ELSIF P_ORDERED_COLUMN = '8' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' LARGE_PRINT ';
    ELSIF P_ORDERED_COLUMN = '9' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DATE_TEST_TAKEN ';
    ELSIF P_ORDERED_COLUMN = '10' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' LOGINDATE ';
    ELSIF P_ORDERED_COLUMN = '11' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SCAN_DATE ';
    ELSIF P_ORDERED_COLUMN = '12' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' WINS_EXPORT_DATE ';
    END IF;
  
    IF P_ORDER = 'asc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ASC ';
    ELSIF P_ORDER = 'desc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DESC ';
    END IF;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_ACTUAL: ' || V_QUERY_ACTUAL);*/
    OPEN P_OUT_CUR_DATA_CSV FOR V_QUERY_ACTUAL;
  
    V_QUERY_PAGING := V_QUERY_PAGING ||
                      'SELECT *
        FROM (SELECT ROWNUM RNUM, A.* FROM (' ||
                      V_QUERY_ACTUAL || ') A) WHERE RNUM <= ' ||
                      P_ROWNUM_TO || '
       AND RNUM >= ' || P_ROWNUM_FROM;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_PAGING: ' || V_QUERY_PAGING);*/
    OPEN P_OUT_CUR_DATA FOR V_QUERY_PAGING;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_DATA_WINSCORE;

  PROCEDURE SP_GET_DATA_GHI(P_PROCESS_STATUS         IN VARCHAR2,
                            P_DATE_FROM              IN VARCHAR2,
                            P_DATE_TO                IN VARCHAR2,
                            P_DRC_STUDENT_ID         IN VARCHAR2,
                            P_DRC_DOCUMENT_ID        IN VARCHAR2,
                            P_UUID                   IN VARCHAR2,
                            P_LAST_NAME              IN VARCHAR2,
                            P_STATE_CODE             IN VARCHAR2,
                            P_FORM                   IN VARCHAR2,
                            P_TEST_ELEMENT_ID        IN VARCHAR2,
                            P_BARCODE                IN VARCHAR2,
                            P_SEARCH_PARAM           IN VARCHAR2,
                            P_ORDERED_COLUMN         IN VARCHAR2,
                            P_ORDER                  IN VARCHAR2,
                            P_ROWNUM_FROM            IN NUMBER,
                            P_ROWNUM_TO              IN NUMBER,
                            P_OUT_TOTAL_RECORD_COUNT OUT NUMBER,
                            P_OUT_CUR_DATA           OUT GET_REFCURSOR,
                            P_OUT_CUR_DATA_CSV       OUT GET_REFCURSOR,
                            P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    V_QUERY_PAGING             CLOB := '';
    V_QUERY_ACTUAL             CLOB := '';
    V_QUERY_TOTAL_RECORD_COUNT CLOB := '';
    V_CUR_TOTAL_RECORD_COUNT   GET_REFCURSOR;
  
  BEGIN
  
    V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SELECT *
  FROM (SELECT ESRIH.PROCESS_ID RECORD_ID,
               ESRIH.FILENAME FILE_NAME,
               ESRIH.STUDENT_CREATED_DATE FILE_GENERATION_DATE_TIME,
               '''' ORGID_TP,
               ESRIH.DRC_STUDENTID DRC_STUDENTID,
               (SELECT C.CUSTOMER_CODE
                  FROM CUSTOMER_INFO C
                 WHERE C.CUSTOMERID = ESRIH.CUSTOMERID) STATE_CODE,
               ESRIH.EXAMINEE_ID EXAMINEEID,
               ESRI.VALIDATION_STATUS PRISM_PROCESS_STATUS,
               ESRI.VALIDATION_LOG ERROR_CODE_ERROR_DESCRIPTION,
               ESRIH.LAST_NAME LAST_NAME,
               ESRIH.LAST_NAME || '',
                      '' || ESRIH.FIRST_NAME || '', '' ||
               ESRIH.MIDDLE_NAME STUDENT_NAME,
               ESRIH.BIRTHDATE DOB,
               (SELECT GENDER_CODE
                  FROM GENDER_DIM G
                 WHERE G.GENDERID = ESRIH.GENDERID) GENDER,
               TO_CHAR(ESRI.DATETIMESTAMP, ''MM/DD/YYYY HH24:MI:SS'') PRISM_PROCESS_DATE,
               ESRIH.ORG_NODE_CODE_PATH ORGPATH,
               (SELECT ORG_NODE_CODE
                  FROM ORG_NODE_DIM
                 WHERE ORG_NODEID = ESRIH.ORG_NODEID) TEST_CENTER_CODE,
               (SELECT ORG_NODE_NAME
                  FROM ORG_NODE_DIM
                 WHERE ORG_NODEID = ESRIH.ORG_NODEID) TEST_CENTER_NAME,
               '''' DOCUMENTID,
               '''' SCHEDULEID,
               '''' TCASCHEDULEDATE,
               '''' IMAGINGID,
               '''' LITHOCODE,
               '''' TESTMODE,
               '''' TESTLANGUAGE,
               '''' CONTENTNAME,
               '''' FORM,
               '''' BARCODEID,
               '''' DATETESTTAKEN,
               '''' CONTENT_SCORE,
               '''' SCALE_SCORE,
               '''' STATUS_CODE_CONTENT,
               '''' CONTENT_TEST_CODE,
               '''' SCANNED_PROCESS_DATE,
               '''' TEST_ELEMENT_ID,
               '''' BARCODE
          FROM ERR_STUDENT_REG_INFO ESRI, ERR_STUDENT_REG_INFO_HIST ESRIH
         WHERE ESRI.STG_REG_INFO_ID = ESRIH.STG_REG_INFO_ID
        UNION ALL
        SELECT ESDIH.PROCESS_ID RECORD_ID,
               ESDIH.FILENAME FILE_NAME,
               ESDIH.DOC_CREATED_DATE FILE_GENERATION_DATE_TIME,
               (SELECT DISTINCT TP.TP_CODE
                  FROM TEST_PROGRAM TP
                 WHERE TP.CUSTOMERID = ESDIH.CUSTOMERID
                   AND TP.ADMINID = ESDIH.ADMINID
                   AND TP_MODE = ESDIH.TEST_MODE) ORGID_TP,
               ESDIH.DRC_STUDENTID DRC_STUDENTID,
               (SELECT C.CUSTOMER_CODE
                  FROM CUSTOMER_INFO C
                 WHERE C.CUSTOMERID = ESDIH.CUSTOMERID) STATE_CODE,
               ESBDH.EXT_STUDENT_ID EXAMINEEID,
               ESDI.VALIDATION_STATUS PRISM_PROCESS_STATUS,
               ESDI.VALIDATION_LOG ERROR_CODE_ERROR_DESCRIPTION,
               ESBDH.LAST_NAME LAST_NAME,
               ESBDH.LAST_NAME || '',
                      '' || ESBDH.FIRST_NAME || '', '' ||
               ESBDH.MIDDLE_NAME STUDENT_NAME,
               ESBDH.BIRTHDATE DOB,
               (SELECT GENDER_CODE
                  FROM GENDER_DIM G
                 WHERE G.GENDERID = ESBDH.GENDERID) GENDER,
               TO_CHAR(ESDI.DATETIMESTAMP, ''MM/DD/YYYY HH24:MI:SS'') PRISM_PROCESS_DATE,
               (SELECT ORG_NODE_CODE_PATH
                  FROM ORG_NODE_DIM
                 WHERE ORG_NODEID = ESDIH.ORG_NODEID) ORGPATH,
               (SELECT ORG_NODE_CODE
                  FROM ORG_NODE_DIM
                 WHERE ORG_NODEID = ESDIH.ORG_NODEID) TEST_CENTER_CODE,
               (SELECT ORG_NODE_NAME
                  FROM ORG_NODE_DIM
                 WHERE ORG_NODEID = ESDIH.ORG_NODEID) TEST_CENTER_NAME,
               TO_CHAR(ESDIH.DOCUMENTID) DOCUMENTID,
               ESDIH.SCHEDULE_ID SCHEDULEID,
               ESDIH.TCA_SCHEDULED_DATE TCASCHEDULEDATE,
               ESDIH.IMAGING_ID IMAGINGID,
               ESDIH.LITHOCODE LITHOCODE,
               ESDIH.TEST_MODE TESTMODE,
               ESDIH.TEST_LANGUAGE TESTLANGUAGE,
               (SELECT SUBTEST_NAME
                  FROM SUBTEST_DIM
                 WHERE SUBTEST_CODE = ESDIH.CONTENT_CODE) CONTENTNAME,
               ESDIH.FORM FORM,
               ESDIH.BARCODE_ID BARCODEID,
               (SELECT TO_CHAR(DATE_TEST_TAKEN)
                  FROM ERR_SUBTEST_SCORE_FACT_HIST
                 WHERE STG_STUDENT_DOCID = ESDI.STG_STUDENT_DOCID) DATETESTTAKEN,
               (SELECT TO_CHAR(NCR)
                  FROM ERR_SUBTEST_SCORE_FACT_HIST
                 WHERE STG_STUDENT_DOCID = ESDI.STG_STUDENT_DOCID) CONTENT_SCORE,
               (SELECT TO_CHAR(SS)
                  FROM ERR_SUBTEST_SCORE_FACT_HIST
                 WHERE STG_STUDENT_DOCID = ESDI.STG_STUDENT_DOCID) SCALE_SCORE,
               DECODE(ESDIH.STATUS_CODE,
                      ''3'',
                      ''OM'',
                      ''5'',
                      ''INV'',
                      ''6'',
                      ''SUP'',
                      ''7'',
                      ''NA'',
                      ''8'',
                      ''SIP'') STATUS_CODE_CONTENT,
               (SELECT DEMO_VALUE
                  FROM ERR_STUDENT_DEMO_HIST
                 WHERE STG_STUDENT_BIO_ID = ESBDH.STG_STUDENT_BIO_ID
                   AND SUBTESTID =
                       (SELECT SUBTESTID
                          FROM SUBTEST_DIM
                         WHERE SUBTEST_CODE = ESDIH.CONTENT_CODE)
                   AND DEMOID =
                       (SELECT DEMOID
                          FROM DEMOGRAPHIC
                         WHERE DEMO_CODE LIKE ''Cont_Tst_Cd%''
                           AND CUSTOMERID = ESDIH.CUSTOMERID
                           AND SUBTESTID =
                               (SELECT SUBTESTID
                                  FROM SUBTEST_DIM
                                 WHERE SUBTEST_CODE = ESDIH.CONTENT_CODE))) CONTENT_TEST_CODE,
               '''' SCANNED_PROCESS_DATE,
               ESBDH.TEST_ELEMENT_ID TEST_ELEMENT_ID,
               ESDIH.BARCODE_ID BARCODE
          FROM ERR_STUDENT_DOC_INFO      ESDI,
               ERR_STUDENT_DOC_INFO_HIST ESDIH,
               ERR_STUDENT_BIO_DIM_HIST  ESBDH
         WHERE ESDI.STG_STUDENT_DOCID = ESDIH.STG_STUDENT_DOCID
           AND ESDIH.STG_STUDENT_BIO_ID(+) = ESBDH.STG_STUDENT_BIO_ID)
 WHERE 1 = 1';
  
    IF P_PROCESS_STATUS <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND PRISM_PROCESS_STATUS =  ''' ||
                        P_PROCESS_STATUS || '''';
    END IF;
  
    IF P_DATE_FROM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(PRISM_PROCESS_DATE) >= TO_DATE(''' ||
                        P_DATE_FROM || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_DATE_TO <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND TRUNC(PRISM_PROCESS_DATE) <= TO_DATE(''' ||
                        P_DATE_TO || ''', ''MM/DD/YYYY'')';
    END IF;
  
    IF P_DRC_STUDENT_ID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND DRC_STUDENTID LIKE ''%' ||
                        P_DRC_STUDENT_ID || '%''';
    END IF;
  
    IF P_DRC_DOCUMENT_ID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND DOCUMENTID LIKE ''%' ||
                        P_DRC_DOCUMENT_ID || '%''';
    END IF;
  
    IF P_UUID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND EXAMINEEID LIKE ''%' ||
                        P_UUID || '%''';
    END IF;
  
    IF P_LAST_NAME <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL ||
                        ' AND UPPER(LAST_NAME) LIKE UPPER(''%' ||
                        P_LAST_NAME || '%'')';
    END IF;
  
    IF P_STATE_CODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND STATE_CODE =  ''' ||
                        P_STATE_CODE || '''';
    END IF;
  
    IF P_FORM <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND FORM =  ''' || P_FORM || '''';
    END IF;
  
    IF P_TEST_ELEMENT_ID <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND TEST_ELEMENT_ID =  ''' ||
                        P_TEST_ELEMENT_ID || '''';
    END IF;
  
    IF P_BARCODE <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' AND BARCODE =  ''' || P_BARCODE || '''';
    END IF;
  
    IF P_SEARCH_PARAM <> '-1' THEN
      V_QUERY_ACTUAL := 'SELECT * FROM (' || V_QUERY_ACTUAL ||
                        ') TAB_SEARCH WHERE UPPER(RECORD_ID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(FILE_NAME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(FILE_GENERATION_DATE_TIME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(ORGID_TP) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DRC_STUDENTID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(STATE_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(EXAMINEEID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(ERROR_CODE_ERROR_DESCRIPTION) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(STUDENT_NAME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DOB) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(GENDER) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(PRISM_PROCESS_DATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(ORGPATH) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TEST_CENTER_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TEST_CENTER_NAME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DOCUMENTID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SCHEDULEID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TCASCHEDULEDATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(IMAGINGID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(LITHOCODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TESTMODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TESTLANGUAGE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(CONTENTNAME) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(FORM) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(BARCODEID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(DATETESTTAKEN) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(CONTENT_SCORE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SCALE_SCORE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(STATUS_CODE_CONTENT) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(CONTENT_TEST_CODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(SCANNED_PROCESS_DATE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(TEST_ELEMENT_ID) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM ||
                        '%'') OR UPPER(BARCODE) LIKE UPPER(''%' ||
                        P_SEARCH_PARAM || '%'')';
    END IF;
  
    V_QUERY_TOTAL_RECORD_COUNT := V_QUERY_TOTAL_RECORD_COUNT ||
                                  'SELECT COUNT(1) FROM (' ||
                                  V_QUERY_ACTUAL || ') TAB';
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_TOTAL_RECORD_COUNT: ' ||
    V_QUERY_TOTAL_RECORD_COUNT);*/
  
    OPEN V_CUR_TOTAL_RECORD_COUNT FOR V_QUERY_TOTAL_RECORD_COUNT;
    IF V_CUR_TOTAL_RECORD_COUNT%ISOPEN THEN
      LOOP
        FETCH V_CUR_TOTAL_RECORD_COUNT
          INTO P_OUT_TOTAL_RECORD_COUNT;
        EXIT WHEN V_CUR_TOTAL_RECORD_COUNT%NOTFOUND;
        /*DBMS_OUTPUT.PUT_LINE('P_OUT_TOTAL_RECORD_COUNT: ' ||
        P_OUT_TOTAL_RECORD_COUNT);*/
      END LOOP;
      CLOSE V_CUR_TOTAL_RECORD_COUNT;
    END IF;
  
    IF P_ORDERED_COLUMN <> '-1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ORDER BY ';
    END IF;
  
    IF P_ORDERED_COLUMN = '1' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' RECORD_ID ';
    ELSIF P_ORDERED_COLUMN = '2' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FILE_NAME ';
    ELSIF P_ORDERED_COLUMN = '3' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FILE_GENERATION_DATE_TIME ';
    ELSIF P_ORDERED_COLUMN = '4' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ORGID_TP ';
    ELSIF P_ORDERED_COLUMN = '5' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DRC_STUDENTID ';
    ELSIF P_ORDERED_COLUMN = '6' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STATE_CODE ';
    ELSIF P_ORDERED_COLUMN = '7' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXAMINEEID ';
    ELSIF P_ORDERED_COLUMN = '8' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ERROR_CODE_ERROR_DESCRIPTION ';
    ELSIF P_ORDERED_COLUMN = '9' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STUDENT_NAME ';
    ELSIF P_ORDERED_COLUMN = '10' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DOB ';
    ELSIF P_ORDERED_COLUMN = '11' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' GENDER ';
    ELSIF P_ORDERED_COLUMN = '12' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' PRISM_PROCESS_DATE ';
    ELSIF P_ORDERED_COLUMN = '13' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ORGPATH ';
    ELSIF P_ORDERED_COLUMN = '14' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TEST_CENTER_CODE ';
    ELSIF P_ORDERED_COLUMN = '15' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TEST_CENTER_NAME ';
    ELSIF P_ORDERED_COLUMN = '16' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DOCUMENTID ';
    ELSIF P_ORDERED_COLUMN = '17' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SCHEDULEID ';
    ELSIF P_ORDERED_COLUMN = '18' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TCASCHEDULEDATE ';
    ELSIF P_ORDERED_COLUMN = '19' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' IMAGINGID ';
    ELSIF P_ORDERED_COLUMN = '20' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' LITHOCODE ';
    ELSIF P_ORDERED_COLUMN = '21' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TESTMODE ';
    ELSIF P_ORDERED_COLUMN = '22' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' TESTLANGUAGE ';
    ELSIF P_ORDERED_COLUMN = '23' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' CONTENTNAME ';
    ELSIF P_ORDERED_COLUMN = '24' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FORM ';
    ELSIF P_ORDERED_COLUMN = '25' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DATETESTTAKEN ';
    ELSIF P_ORDERED_COLUMN = '26' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BARCODEID ';
    ELSIF P_ORDERED_COLUMN = '27' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' CONTENT_SCORE ';
    ELSIF P_ORDERED_COLUMN = '28' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' CONTENT_TEST_CODE ';
    ELSIF P_ORDERED_COLUMN = '29' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SCALE_SCORE ';
    ELSIF P_ORDERED_COLUMN = '30' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SCANNED_PROCESS_DATE ';
    ELSIF P_ORDERED_COLUMN = '31' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STATUS_CODE_CONTENT ';
    END IF;
  
    IF P_ORDER = 'asc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' ASC ';
    ELSIF P_ORDER = 'desc' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DESC ';
    END IF;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_ACTUAL: ' || V_QUERY_ACTUAL);*/
    OPEN P_OUT_CUR_DATA_CSV FOR V_QUERY_ACTUAL;
  
    V_QUERY_PAGING := V_QUERY_PAGING ||
                      'SELECT *
        FROM (SELECT ROWNUM RNUM, A.* FROM (' ||
                      V_QUERY_ACTUAL || ') A) WHERE RNUM <= ' ||
                      P_ROWNUM_TO || '
       AND RNUM >= ' || P_ROWNUM_FROM;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_PAGING: ' || V_QUERY_PAGING);*/
    OPEN P_OUT_CUR_DATA FOR V_QUERY_PAGING;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_DATA_GHI;

END PKG_FILE_TRACKING; --END OF PACKAGE
/
