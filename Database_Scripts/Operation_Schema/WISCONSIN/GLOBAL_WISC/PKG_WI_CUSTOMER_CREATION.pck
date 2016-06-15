CREATE OR REPLACE PACKAGE PKG_WI_CUSTOMER_CREATION IS

  PROCEDURE SP_WI_CREATE_CUSTOMER_NEW(P_CUSTOMER_NAME  IN VARCHAR2,
                                      P_CUSTOMER_CODE  IN VARCHAR2,
                                      P_SEND_LOGIN_PDF IN VARCHAR2,
                                      P_PP_TPCODE      IN VARCHAR2,
                                      P_DATA_LOC      IN VARCHAR2,
                                      P_SUPPRT_EMAILS IN VARCHAR2,
                                      P_PROJECT_ID    IN VARCHAR2,
                                      OUT_RESULT      OUT VARCHAR2);

  PROCEDURE SP_WI_CREATE_TABLE_PARTITION(IN_CUST_PROD_ID              IN NUMBER,
                                         IN_TABLE_NAME                IN VARCHAR2,
                                         IN_MO_INDIVIDUAL_SCHEMA_NAME IN VARCHAR2,
                                         OUT_STATUS_MSG               OUT VARCHAR2,
                                         OUT_STATUS                   OUT NUMBER);
  PROCEDURE SP_WI_REBUILD_IDEXES(IN_TABLE_NAME             IN VARCHAR2,
                                 IN_INDIVIDUAL_SCHEMA_NAME IN VARCHAR2,
                                 OUT_STATUS_MSG            OUT VARCHAR2,
                                 OUT_STATUS                OUT NUMBER);

END PKG_WI_CUSTOMER_CREATION;
/
CREATE OR REPLACE PACKAGE BODY PKG_WI_CUSTOMER_CREATION IS

  PROCEDURE SP_WI_CREATE_CUSTOMER_NEW(P_CUSTOMER_NAME  IN VARCHAR2,
                                      P_CUSTOMER_CODE  IN VARCHAR2,
                                      P_SEND_LOGIN_PDF IN VARCHAR2,
                                      P_PP_TPCODE      IN VARCHAR2,
                                      P_DATA_LOC      IN VARCHAR2,
                                      P_SUPPRT_EMAILS IN VARCHAR2,
                                      P_PROJECT_ID    IN VARCHAR2,
                                      OUT_RESULT      OUT VARCHAR2) IS
  
    V_CUSTOMER_ID         NUMBER;
    V_PRODUCT_ID          NUMBER;
    V_CUST_PROD_ID        NUMBER;
    V_ADMIN_ID            NUMBER;
    V_TP_ID               NUMBER;
    V_TP_COUNT            NUMBER;
    V_DASH_ACCESS         NUMBER;
    V_ORG_USER_COUNT      NUMBER;
    V_SCORE_LKP_COUNT     NUMBER;
    V_DEMOID              NUMBER;
    V_DEMO_VALID          NUMBER;
    V_ORG_NODE_ID         NUMBER;
    V_DASH_ACTN_LKP_COUNT NUMBER;
    V_OLD_CUSTOMERID      NUMBER;
    V_OLD_CUST_PRODID     NUMBER;
    V_PP_TP_ID            NUMBER;
    V_DASH_MESSAGE_TYPE   NUMBER;
    V_DASH_MESSAGES       NUMBER;
    V_CUST_CODE_CHK NUMBER;
    EXP_CUST_CODE_CHK        EXCEPTION;
    V_DEMO_CUSTOMER VARCHAR2 (32) DEFAULT 'Wisconsin State DPI';
  
    CURSOR C_DEMO IS
      SELECT DEMOID,
             DEMO_NAME,
             DEMO_CODE,
             DEMO_MODE,
             CUSTOMERID,
             SUBTESTID,
             CATEGORY,
             IS_DEMO_VALUE_AVL
        FROM WISCONSIN.DEMOGRAPHIC
       WHERE CUSTOMERID = V_OLD_CUSTOMERID;
  
  BEGIN
  
  
    SELECT NVL(COUNT(1), 0)
      INTO V_CUST_CODE_CHK
      FROM PRISMGLOBAL.CUSTOMER_INFO
     WHERE CUSTOMER_CODE = P_CUSTOMER_CODE
       AND PROJECTID = P_PROJECT_ID;
  
    IF V_CUST_CODE_CHK > 0 THEN
      RAISE EXP_CUST_CODE_CHK;
    END IF;
  
    SELECT CUSTOMERID
      INTO V_OLD_CUSTOMERID
      FROM CUSTOMER_INFO
     WHERE CUSTOMER_NAME = V_DEMO_CUSTOMER
     AND PROJECTID = P_PROJECT_ID;
  
    SELECT CUST_PROD_ID
      INTO V_OLD_CUST_PRODID
      FROM CUST_PRODUCT_LINK
     WHERE CUSTOMERID = V_OLD_CUSTOMERID;
  
    SELECT TP_ID
      INTO V_PP_TP_ID
      FROM TEST_PROGRAM
     WHERE CUSTOMERID = V_OLD_CUSTOMERID
       AND TP_MODE = 'OL'
       AND ACTIVATION_STATUS = 'AC';
  
    SELECT MAX(CUSTOMERID)
      INTO V_CUSTOMER_ID
      FROM PRISMGLOBAL.CUSTOMER_INFO;
  
    IF V_CUSTOMER_ID IS NULL THEN
    
      V_CUSTOMER_ID := V_OLD_CUSTOMERID;
    
      INSERT INTO PRISMGLOBAL.CUSTOMER_INFO
        (CUSTOMERID,
         CUSTOMER_NAME,
         DISPLAY_TP_SELECTION,
         FILE_LOCATION,
         DATETIMESTAMP,
         SUPPORT_EMAIL,
         SEND_LOGIN_PDF,
         CUSTOMER_CODE,
         PROJECTID)
      VALUES
        (V_OLD_CUSTOMERID,
         P_CUSTOMER_NAME,
         'Y',
         P_DATA_LOC,
         SYSDATE,
         P_SUPPRT_EMAILS,
         P_SEND_LOGIN_PDF,
         P_CUSTOMER_CODE,
         P_PROJECT_ID);
    
      -- COMMIT;
    
    ELSE
    
      V_CUSTOMER_ID := V_CUSTOMER_ID + 1;
    
      INSERT INTO PRISMGLOBAL.CUSTOMER_INFO
        (CUSTOMERID,
         CUSTOMER_NAME,
         DISPLAY_TP_SELECTION,
         FILE_LOCATION,
         DATETIMESTAMP,
         SUPPORT_EMAIL,
         SEND_LOGIN_PDF,
         CUSTOMER_CODE,
         PROJECTID)
      VALUES
        (V_CUSTOMER_ID,
         P_CUSTOMER_NAME,
         'Y',
         P_DATA_LOC,
         SYSDATE,
         P_SUPPRT_EMAILS,
         P_SEND_LOGIN_PDF,
         P_CUSTOMER_CODE,
         P_PROJECT_ID);
    
    END IF;
  
    SELECT PRODUCTID
      INTO V_PRODUCT_ID
      FROM PRODUCT
     WHERE PROJECTID = P_PROJECT_ID
       AND PRODUCT_CODE = P_PP_TPCODE;
  
    SELECT MAX(CUST_PROD_ID)
      INTO V_CUST_PROD_ID
      FROM PRISMGLOBAL.CUST_PRODUCT_LINK;
  
    SELECT ADMINID
      INTO V_ADMIN_ID
      FROM ADMIN_DIM
     WHERE PROJECTID = P_PROJECT_ID
     AND IS_CURRENT_ADMIN = 'Y';
  
    IF V_CUST_PROD_ID IS NULL THEN
    
      V_CUST_PROD_ID := V_OLD_CUST_PRODID;
    
      INSERT INTO PRISMGLOBAL.CUST_PRODUCT_LINK
        (CUST_PROD_ID,
         CUSTOMERID,
         PRODUCTID,
         ADMINID,
         ACTIVATION_STATUS,
         DATETIMESTAMP)
      VALUES
        (V_CUST_PROD_ID,
         V_CUSTOMER_ID,
         V_PRODUCT_ID,
         V_ADMIN_ID,
         'AC',
         SYSDATE);
    
    ELSE
    
      V_CUST_PROD_ID := V_CUST_PROD_ID + 1;
    
      INSERT INTO PRISMGLOBAL.CUST_PRODUCT_LINK
        (CUST_PROD_ID,
         CUSTOMERID,
         PRODUCTID,
         ADMINID,
         ACTIVATION_STATUS,
         DATETIMESTAMP)
      VALUES
        (V_CUST_PROD_ID,
         V_CUSTOMER_ID,
         V_PRODUCT_ID,
         V_ADMIN_ID,
         'AC',
         SYSDATE);
    
    END IF;
    
   -- DELETE FROM CUTSCORESCALESCORE WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    INSERT INTO WISCONSIN.CUTSCORESCALESCORE
      (CUTSCORESCALESCOREID,
       GRADEID,
       LEVELID,
       SUBTESTID,
       CUST_PROD_ID,
       LOSS,
       HOSS,
       PASS,
       PASSPLUS,
       PL,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT CUTSCORESCALESCOREID,
             GRADEID,
             LEVELID,
             SUBTESTID,
             V_CUST_PROD_ID AS CUST_PROD_ID,
             LOSS,
             HOSS,
             PASS,
             PASSPLUS,
             PL,
             SYSDATE,
             SYSDATE
        FROM WISCONSIN.CUTSCORESCALESCORE
       WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;
  
    INSERT INTO WISCONSIN.ARTICLE_METADATA
      (ARTICLEID,
       ARTICLE_NAME,
       CUST_PROD_ID,
       SUBT_OBJ_MAPID,
       SUBTESTID,
       ARTICLE_CONTENT_ID,
       CATEGORY,
       CATEGORY_TYPE,
       CATEGORY_SEQ,
       SUB_HEADER,
       DESCRIPTION,
       GRADEID,
       PROFICIENCY_LEVEL,
       RESOLVED_RPRT_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT WISCONSIN.ARTICLE_METADATA_SEQ.NEXTVAL AS ARTICLEID,
             ARTICLE_NAME,
             V_CUST_PROD_ID                        AS CUST_PROD_ID,
             SUBT_OBJ_MAPID,
             SUBTESTID,
             ARTICLE_CONTENT_ID,
             CATEGORY,
             CATEGORY_TYPE,
             CATEGORY_SEQ,
             SUB_HEADER,
             DESCRIPTION,
             GRADEID,
             PROFICIENCY_LEVEL,
             RESOLVED_RPRT_STATUS,
             SYSDATE                               AS CREATED_DATE_TIME,
             SYSDATE                               AS UPDATED_DATE_TIME
        FROM WISCONSIN.ARTICLE_METADATA A
       WHERE A.CUST_PROD_ID = V_OLD_CUST_PRODID;
  
    DELETE FROM NP_MEAN_NCE_LOOKUP WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    --COMMIT;
  
    INSERT INTO NP_MEAN_NCE_LOOKUP
      (NMN_LOOKUPID, NCE, NP, CUST_PROD_ID, DATETIMESTAMP)
      SELECT NMN_LOOKUPID_SEQ.NEXTVAL,
             NCE,
             NP,
             V_CUST_PROD_ID,
             DATETIMESTAMP
        FROM NP_MEAN_NCE_LOOKUP
       WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;
  
    -- COMMIT;
  
    SELECT MAX(TP_ID) INTO V_TP_ID FROM PRISMGLOBAL.TEST_PROGRAM;
  
    IF V_TP_ID IS NULL THEN
      V_TP_ID := 1000;
    END IF;
  
    SELECT COUNT(1)
      INTO V_TP_COUNT
      FROM TEST_PROGRAM
     WHERE CUSTOMERID = V_CUSTOMER_ID;
  
    IF V_TP_COUNT = 0 THEN
    
    
      INSERT INTO PRISMGLOBAL.TEST_PROGRAM
        (TP_ID,
         TP_CODE,
         TP_NAME,
         TP_TYPE,
         NUM_LEVELS,
         TP_MODE,
         CUSTOMERID,
         ADMINID,
         ACTIVATION_STATUS,
         DATETIMESTAMP)
      VALUES
        (V_TP_ID + 2,
         P_PP_TPCODE,
         'WISCONSIN CUSTOMER',
         'PUBLIC',
         3,
         'OL',
         V_CUSTOMER_ID,
         V_ADMIN_ID,
         'AC',
         SYSDATE);
    
      INSERT INTO PRISMGLOBAL.ORG_TP_STRUCTURE
        (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
        SELECT V_TP_ID + 2, ORG_LEVEL, ORG_LABEL, SYSDATE
          FROM ORG_TP_STRUCTURE
         WHERE TP_ID = V_PP_TP_ID;
    
    END IF;
  
    -- COMMIT;
  
    SELECT COUNT(1)
      INTO V_ORG_USER_COUNT
      FROM ORG_USER_DEFINE_LOOKUP
     WHERE CUSTOMERID = V_CUSTOMER_ID;
  
    IF V_ORG_USER_COUNT = 0 THEN
    
      INSERT INTO PRISMGLOBAL.ORG_USER_DEFINE_LOOKUP
        (ROLEID,
         ORG_NODE_LEVEL,
         USER_SEQ,
         USER_NAME,
         USER_PASSWORD,
         CUSTOMERID,
         DATETIMESTAMP)
        SELECT ROLEID,
               ORG_NODE_LEVEL,
               USER_SEQ,
               DECODE(USER_NAME, NULL, NULL, USER_NAME || V_CUSTOMER_ID),
               USER_PASSWORD,
               V_CUSTOMER_ID,
               SYSDATE
          FROM ORG_USER_DEFINE_LOOKUP
         WHERE CUSTOMERID = V_OLD_CUSTOMERID;
    
    END IF;
  
    --COMMIT;
  
    SELECT COUNT(1)
      INTO V_SCORE_LKP_COUNT
      FROM SCORE_TYPE_LOOKUP
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    IF V_SCORE_LKP_COUNT = 0 THEN
    
      INSERT INTO PRISMGLOBAL.SCORE_TYPE_LOOKUP
        (CATEGORY,
         SCORE_TYPE,
         SCORE_VALUE,
         SCORE_VALUE_NAME,
         CUST_PROD_ID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        SELECT CATEGORY,
               SCORE_TYPE,
               SCORE_VALUE,
               SCORE_VALUE_NAME,
               V_CUST_PROD_ID,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME
          FROM SCORE_TYPE_LOOKUP
         WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;
    
    END IF;
  
    SELECT MAX(DEMOID) INTO V_DEMOID FROM WISCONSIN.DEMOGRAPHIC;
  
    /*SELECT MAX(DEMO_VALID) 
    INTO V_DEMO_VALID 
    FROM MISSOURI.DEMOGRAPHIC_VALUES;*/
  
    FOR R_DEMO IN C_DEMO LOOP
    
      V_DEMOID := V_DEMOID + 1;
    
      INSERT INTO WISCONSIN.DEMOGRAPHIC
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
        (V_DEMOID,
         R_DEMO.DEMO_NAME,
         R_DEMO.DEMO_CODE,
         R_DEMO.DEMO_MODE,
         V_CUSTOMER_ID,
         R_DEMO.SUBTESTID,
         R_DEMO.CATEGORY,
         R_DEMO.IS_DEMO_VALUE_AVL,
         SYSDATE);
    
      SELECT MAX(DEMO_VALID)
        INTO V_DEMO_VALID
        FROM WISCONSIN.DEMOGRAPHIC_VALUES;
    
      INSERT INTO WISCONSIN.DEMOGRAPHIC_VALUES
        (DEMO_VALID,
         DEMOID,
         DEMO_VALUE_NAME,
         DEMO_VALUE_CODE,
         DEMO_VALUE_SEQ,
         IS_DEFAULT,
         DATETIMESTAMP)
        SELECT V_DEMO_VALID + ROWNUM,
               V_DEMOID,
               DEMO_VALUE_NAME,
               DEMO_VALUE_CODE,
               DEMO_VALUE_SEQ,
               IS_DEFAULT,
               DATETIMESTAMP
          FROM WISCONSIN.DEMOGRAPHIC_VALUES
         WHERE DEMOID = R_DEMO.DEMOID;
    
    END LOOP;
  
    --COMMIT;
  
    SELECT MAX(ORG_NODEID) INTO V_ORG_NODE_ID FROM WISCONSIN.ORG_NODE_DIM
    WHERE CUSTOMERID = V_CUSTOMER_ID;
  
    IF V_ORG_NODE_ID IS NULL THEN
      V_ORG_NODE_ID := 1;
    
      INSERT INTO WISCONSIN.ORG_NODE_DIM
        (ORG_NODEID,
         ORG_NODE_NAME,
         ORG_NODE_CODE,
         ORG_NODE_LEVEL,
         STRC_ELEMENT,
         SPECIAL_CODES,
         ORG_MODE,
         PARENT_ORG_NODEID,
         ORG_NODE_CODE_PATH,
         EMAILS,
         CUSTOMERID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        SELECT (SELECT MAX(ORG_NODEID) FROM WISCONSIN.ORG_NODE_DIM)+1,
               P_CUSTOMER_NAME,
               P_CUSTOMER_CODE,
               1,
               P_CUSTOMER_CODE,
               P_CUSTOMER_CODE,
               '-99',
               0,
               '0~'||P_CUSTOMER_CODE,
               NULL,
               V_CUSTOMER_ID,
               SYSDATE,
               SYSDATE
          FROM WISCONSIN.ORG_NODE_DIM
         WHERE CUSTOMERID = V_OLD_CUSTOMERID
           AND ORG_NODE_LEVEL = 0;
    
    END IF;
  
    -- INSERT INTO DASH_MENU_RPT_ACCESS
    SELECT COUNT(1)
      INTO V_DASH_ACCESS
      FROM DASH_MENU_RPT_ACCESS
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    IF V_DASH_ACCESS = 0 THEN
    
      INSERT INTO PRISMGLOBAL.DASH_MENU_RPT_ACCESS
        (DB_MENUID,
         DB_REPORTID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         REPORT_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        SELECT DB_MENUID,
               DB_REPORTID,
               ROLEID,
               ORG_LEVEL,
               V_CUST_PROD_ID,
               REPORT_SEQ,
               P_PROJECT_ID,
               'AC',
               SYSDATE,
               SYSDATE
          FROM DASH_MENU_RPT_ACCESS
         WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;
    
    END IF;
  
    --COMMIT;
  
    -- INSERT INTO DASH_MESSAGE_TYPE
    SELECT COUNT(1)
      INTO V_DASH_MESSAGE_TYPE
      FROM DASH_MESSAGE_TYPE
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    IF V_DASH_MESSAGE_TYPE = 0 THEN
    
      INSERT INTO PRISMGLOBAL.DASH_MESSAGE_TYPE
        (MSG_TYPEID,
         MESSAGE_NAME,
         MESSAGE_TYPE,
         DESCRIPTION,
         CUST_PROD_ID,
         CREATED_DATE_TIME)
        SELECT MSG_TYPEID,
               MESSAGE_NAME,
               MESSAGE_TYPE,
               DESCRIPTION,
               V_CUST_PROD_ID CUST_PROD_ID,
               SYSDATE
          FROM DASH_MESSAGE_TYPE
         WHERE CUST_PROD_ID = V_OLD_CUST_PRODID
           AND MESSAGE_TYPE <> 'GSCM';
    
    END IF;
  
    -- INSERT INTO DASH_MESSAGES
    SELECT COUNT(1)
      INTO V_DASH_MESSAGES
      FROM DASH_MESSAGES
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    IF V_DASH_MESSAGES = 0 THEN
    
      INSERT INTO PRISMGLOBAL.DASH_MESSAGES
        (DB_REPORTID,
         MSG_TYPEID,
         REPORT_MSG,
         CUST_PROD_ID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
        SELECT DB_REPORTID,
               MSG_TYPEID,
               REPORT_MSG,
               V_CUST_PROD_ID CUST_PROD_ID,
               ACTIVATION_STATUS,
               SYSDATE
          FROM DASH_MESSAGES
         WHERE CUST_PROD_ID = V_OLD_CUST_PRODID
           AND MSG_TYPEID IN (SELECT MSG_TYPEID
                                FROM DASH_MESSAGE_TYPE
                               WHERE MESSAGE_TYPE <> 'GSCM');
    
    END IF;
  
    -- INSERT INTO DASH_ACTION_ACCESS
    SELECT COUNT(1)
      INTO V_DASH_ACTN_LKP_COUNT
      FROM DASH_ACTION_ACCESS
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    IF V_DASH_ACTN_LKP_COUNT = 0 THEN
    
      INSERT INTO PRISMGLOBAL.DASH_ACTION_ACCESS
        (DB_ACT_ACCESSID,
         DB_MENUID,
         DB_REPORTID,
         DB_ACTIONID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         ACTION_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
        SELECT SEQ_DASH_ACTION_ACCESS.NEXTVAL DB_ACT_ACCESSID,
               DB_MENUID,
               DB_REPORTID,
               DB_ACTIONID,
               ROLEID,
               ORG_LEVEL,
               V_CUST_PROD_ID                 CUST_PROD_ID,
               ACTION_SEQ,
               P_PROJECT_ID,
               ACTIVATION_STATUS,
               SYSDATE                        CREATED_DATE_TIME
          FROM DASH_ACTION_ACCESS
         WHERE CUST_PROD_ID = V_OLD_CUST_PRODID;
    
    END IF;
    
    COMMIT;
  
    OUT_RESULT := 'SUCCESSFUL';
  
  EXCEPTION
    WHEN EXP_CUST_CODE_CHK THEN
      ROLLBACK;
      --OUT_RESULT := 'CUSTOMER_CODE_ERROR';
      OUT_RESULT := 'Customer_Code ' || P_CUSTOMER_CODE ||
                    ' Already Exists. Please try with Another Code';
      --raise_application_error(-20000, 'CUSTOMER_CODE '||P_CUSTOMER_CODE||' already existis in CUSTOMER table.');
  
    WHEN OTHERS THEN
      dbms_output.put_line(SQLERRM);
      ROLLBACK;
      OUT_RESULT := 'ERROR';
      dbms_output.put_line('Error ->' ||
                           dbms_utility.format_error_backtrace);
    
  END SP_WI_CREATE_CUSTOMER_NEW;


  PROCEDURE SP_WI_CREATE_TABLE_PARTITION(IN_CUST_PROD_ID              IN NUMBER,
                                         IN_TABLE_NAME                IN VARCHAR2,
                                         IN_MO_INDIVIDUAL_SCHEMA_NAME IN VARCHAR2,
                                         OUT_STATUS_MSG               OUT VARCHAR2,
                                         OUT_STATUS                   OUT NUMBER) AS
  
    V_PARTITION_EXISTS NUMBER;
    V_PART_INITIAL     VARCHAR2(100);
  
  BEGIN
  
    SELECT COUNT(1)
      INTO V_PARTITION_EXISTS
      FROM ALL_TAB_PARTITIONS
     WHERE TABLE_OWNER = IN_MO_INDIVIDUAL_SCHEMA_NAME
       AND TABLE_NAME = IN_TABLE_NAME
       AND SUBSTR(PARTITION_NAME, LENGTH(PARTITION_NAME) - 3) =
           TO_CHAR(IN_CUST_PROD_ID);
  
    V_PART_INITIAL := SUBSTR(IN_TABLE_NAME, 1, 3);
  
    IF V_PARTITION_EXISTS = 0 THEN
    
      EXECUTE IMMEDIATE 'ALTER TABLE ' || IN_MO_INDIVIDUAL_SCHEMA_NAME || '.' ||
                        IN_TABLE_NAME || ' SPLIT PARTITION PART_' ||
                        V_PART_INITIAL || '_OTHERS VALUES (' ||
                        IN_CUST_PROD_ID || ') INTO (PARTITION PART_' ||
                        V_PART_INITIAL || '_P' || IN_CUST_PROD_ID ||
                        ', PARTITION PART_' || V_PART_INITIAL || '_OTHERS)';
      OUT_STATUS_MSG := ' Partition PART_SUB_P' || IN_CUST_PROD_ID ||
                        ' created successfully on Table SUBTEST_SCORE_FACT';
      OUT_STATUS     := 0;
      DBMS_OUTPUT.PUT_LINE(' Partition PART_' || V_PART_INITIAL || '_P' ||
                           IN_CUST_PROD_ID ||
                           ' created successfully on Table ' ||
                           IN_TABLE_NAME);
    
    ELSE
    
      OUT_STATUS_MSG := ' Partition PART_' || V_PART_INITIAL || '_P' ||
                        IN_CUST_PROD_ID || ' is already present on Table ' ||
                        IN_TABLE_NAME;
      OUT_STATUS     := 0;
      DBMS_OUTPUT.PUT_LINE(' Partition PART_' || V_PART_INITIAL || '_P' ||
                           IN_CUST_PROD_ID ||
                           ' is already present on Table ' ||
                           IN_TABLE_NAME);
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      OUT_STATUS_MSG := ' Partition PART_' || V_PART_INITIAL || '_P' ||
                        IN_CUST_PROD_ID || ' creation failed : Error is ' ||
                        SQLERRM;
      OUT_STATUS     := 1;
      DBMS_OUTPUT.PUT_LINE(' PART_' || V_PART_INITIAL || '_P' ||
                           IN_CUST_PROD_ID ||
                           ' creation failed : Error is ' || SQLERRM);
    
  END SP_WI_CREATE_TABLE_PARTITION;

  PROCEDURE SP_WI_REBUILD_IDEXES(IN_TABLE_NAME             IN VARCHAR2,
                                 IN_INDIVIDUAL_SCHEMA_NAME IN VARCHAR2,
                                 OUT_STATUS_MSG            OUT VARCHAR2,
                                 OUT_STATUS                OUT NUMBER) AS
  
    V_INDEX_NAME VARCHAR2(30);
  
  BEGIN
  
    FOR REC IN (SELECT OWNER, INDEX_NAME, INDEX_TYPE
                  FROM ALL_INDEXES
                 WHERE TABLE_OWNER = IN_INDIVIDUAL_SCHEMA_NAME
                   AND TABLE_NAME = IN_TABLE_NAME) LOOP
    
      V_INDEX_NAME := REC.INDEX_NAME;
    
      BEGIN
        EXECUTE IMMEDIATE 'BEGIN ' || IN_INDIVIDUAL_SCHEMA_NAME ||
                          '.PKG_REBUILD_INDEXES.SP_REBUILD_INDEX(' ||
                          '''|| REC.OWNER||''' || ',' ||
                          '''||IN_TABLE_NAME||''' || ',' ||
                          '''|| REC.INDEX_NAME||''' || ',' ||
                          '''|| REC.INDEX_TYPE||''' || '); END;';
      END;
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('INDEX REBUILD DONE SUCCESSFULLY FOR TABLE ' ||
                         IN_TABLE_NAME);
    OUT_STATUS_MSG := 'INDEX REBUILD DONE SUCCESSFULLY FOR TABLE ' ||
                      IN_TABLE_NAME;
    OUT_STATUS     := 0;
  
  EXCEPTION
    WHEN OTHERS THEN
    
      OUT_STATUS_MSG := 'INDEX REBUILD FAILED FOR INDEX ' || V_INDEX_NAME ||
                        ' on table ' || IN_TABLE_NAME || ' Error is : ' ||
                        SQLERRM;
      OUT_STATUS     := 1;
      DBMS_OUTPUT.PUT_LINE('INDEX REBUILD FAILED FOR INDEX ' ||
                           V_INDEX_NAME || ' on table ' || IN_TABLE_NAME ||
                           ' Error is : ' || SQLERRM);
    
  END SP_WI_REBUILD_IDEXES;

END PKG_WI_CUSTOMER_CREATION;
/
