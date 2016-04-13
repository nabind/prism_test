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
                    TO_CHAR(ESSH.DATETIMESTAMP, ''MM/DD/YYYY'') PROCESSED_DATE
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
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' PROCESS_ID ';
    ELSIF P_ORDERED_COLUMN = '6' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_CODE ';
    ELSIF P_ORDERED_COLUMN = '7' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_STATUS ';
    ELSIF P_ORDERED_COLUMN = '8' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BARCODE ';
    ELSIF P_ORDERED_COLUMN = '9' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DATE_SCHEDULED ';
    ELSIF P_ORDERED_COLUMN = '10' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STATE_CODE ';
    ELSIF P_ORDERED_COLUMN = '11' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FORM ';
    ELSIF P_ORDERED_COLUMN = '12' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SUBTEST ';
    ELSIF P_ORDERED_COLUMN = '13' THEN
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
                      V_QUERY_ACTUAL || ') A WHERE ROWNUM <= ' ||
                      P_ROWNUM_TO || ')
       WHERE RNUM >= ' || P_ROWNUM_FROM;
  
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
       TO_CHAR(EED.CREATED_DATE_TIME, ''MM/DD/YYYY'') PROCESSED_DATE
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
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' PROCESS_ID ';
    ELSIF P_ORDERED_COLUMN = '6' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_CODE ';
    ELSIF P_ORDERED_COLUMN = '7' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' EXCEPTION_STATUS ';
    ELSIF P_ORDERED_COLUMN = '8' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' BARCODE ';
    ELSIF P_ORDERED_COLUMN = '9' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' DATE_SCHEDULED ';
    ELSIF P_ORDERED_COLUMN = '10' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' STATE_CODE ';
    ELSIF P_ORDERED_COLUMN = '11' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' FORM ';
    ELSIF P_ORDERED_COLUMN = '12' THEN
      V_QUERY_ACTUAL := V_QUERY_ACTUAL || ' SUBTEST ';
    ELSIF P_ORDERED_COLUMN = '13' THEN
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
                      V_QUERY_ACTUAL || ') A WHERE ROWNUM <= ' ||
                      P_ROWNUM_TO || ')
       WHERE RNUM >= ' || P_ROWNUM_FROM;
  
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
                           TO_CHAR(LOGINDATE, ''YYYY-MM-DD HH:mm:ss'') LOGINDATE,
                           TO_CHAR(SCAN_DATE, ''YYYY-MM-DD HH:mm:ss'') SCAN_DATE,
                           TO_CHAR(WINS_EXPORT_DATE, ''YYYY-MM-DD HH:mm:ss'') WINS_EXPORT_DATE,
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
                      V_QUERY_ACTUAL || ') A WHERE ROWNUM <= ' ||
                      P_ROWNUM_TO || ')
       WHERE RNUM >= ' || P_ROWNUM_FROM;
  
    /*DBMS_OUTPUT.PUT_LINE('V_QUERY_PAGING: ' || V_QUERY_PAGING);*/
    OPEN P_OUT_CUR_DATA FOR V_QUERY_PAGING;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_DATA_WINSCORE;

END PKG_FILE_TRACKING; --END OF PACKAGE
/
