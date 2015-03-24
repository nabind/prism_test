CREATE OR REPLACE PACKAGE PKG_INORS_CREATE_CUSTOMER AUTHID CURRENT_USER IS

  PROCEDURE SP_INORS_CREATE_CUSTOMER(IN_EXISTING_CUSTOMER_ID  IN NUMBER,
                                     IN_PRODUCT_CODE          IN VARCHAR2,
                                     IN_NEW_CUSTOMER_NAME     IN VARCHAR2,
                                     IN_OLD_PUBLIC_ORGTP      IN VARCHAR2,
                                     IN_OLD_NON_PUBLIC_ORGTP  IN VARCHAR2,
                                     IN_NEW_PUBLIC_ORGTP      IN VARCHAR2,
                                     IN_NEW_NON_PUBLIC_ORGTP  IN VARCHAR2,
                                     IN_INORS_CTB_COM_TP_CODE IN VARCHAR2,
                                     OUT_INORS_CTB_COM_STATUS OUT VARCHAR2,
                                     OUT_STATUS_FLAG          OUT VARCHAR2);

  PROCEDURE SP_INORS_DELETE_CUSTOMER(IN_CUSTOMER_ID            IN NUMBER,
                                     IN_PRODUCT_CODE_TO_DELETE IN VARCHAR2,
                                     IN_PUBLIC_ORGTP           IN VARCHAR2,
                                     IN_NON_PUBLIC_ORGTP       IN VARCHAR2,
                                     IN_INORS_CTB_COM_TP_CODE  IN VARCHAR2,
                                     OUT_INORS_CTB_COM_STATUS  OUT VARCHAR2,
                                     OUT_STATUS_FLAG           OUT VARCHAR2);

  /*PROCEDURE SP_INITIAL_PRODUCT_CONFIG(IN_CTB_PROJECTID          IN VARCHAR2,
                                      IN_CTB_PROJECTID_APS      IN VARCHAR2,
                                      IN_PRODUCT_CODE           IN VARCHAR2,
                                      IN_PRODUCT_NAME           IN VARCHAR2,
                                      IN_PUBLIC_ORGTP           IN VARCHAR2,
                                      IN_NONPUBLIC_ORGTP        IN VARCHAR2,
                                      IN_PUBLIC_BRAILE_ORGTP    IN VARCHAR2,
                                      IN_NONPUBLIC_BRAILE_ORGTP IN VARCHAR2,
                                      OUT_STATUS_FLAG_NEW       OUT NUMBER);*/

END PKG_INORS_CREATE_CUSTOMER;
/
CREATE OR REPLACE PACKAGE BODY PKG_INORS_CREATE_CUSTOMER IS

  PROCEDURE PRC_CREATE_TABLE_PARTITION(IN_CUST_PROD_ID IN NUMBER,
                                       OUT_STATUS_MSG  OUT VARCHAR2,
                                       OUT_STATUS      OUT NUMBER) AS
  
    V_PARTITION_EXISTS NUMBER;
  
  BEGIN
  
    SELECT COUNT(1)
      INTO V_PARTITION_EXISTS
      FROM USER_TAB_PARTITIONS
     WHERE TABLE_NAME = 'RESULTS_GRT_FACT'
       AND PARTITION_NAME = 'PART_P' || IN_CUST_PROD_ID;
  
    IF V_PARTITION_EXISTS = 0 THEN
      EXECUTE IMMEDIATE 'ALTER TABLE RESULTS_GRT_FACT
  SPLIT PARTITION PART_OTHERS
  VALUES (' || IN_CUST_PROD_ID || ')
  INTO ( PARTITION PART_P' || IN_CUST_PROD_ID ||
                        ', PARTITION PART_OTHERS )';
    
      OUT_STATUS_MSG := ' Partition PART_P' || IN_CUST_PROD_ID ||
                        ' created successfully on Table RESULTS_GRT_FACT';
    
      OUT_STATUS := 0;
    
      DBMS_OUTPUT.PUT_LINE(' Partition PART_P' || IN_CUST_PROD_ID ||
                           ' created successfully on Table RESULTS_GRT_FACT');
    
    ELSE
    
      OUT_STATUS_MSG := ' Partition PART_P' || IN_CUST_PROD_ID ||
                        ' is already present on Table RESULTS_GRT_FACT';
    
      OUT_STATUS := 0;
    
      DBMS_OUTPUT.PUT_LINE(' Partition PART_P' || IN_CUST_PROD_ID ||
                           ' is already present on Table RESULTS_GRT_FACT');
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
    
      OUT_STATUS_MSG := ' Partition PART_P' || IN_CUST_PROD_ID ||
                        'creation failed : Error is ' || SQLERRM;
      OUT_STATUS     := 1;
    
      DBMS_OUTPUT.PUT_LINE('Partition PART_P' || IN_CUST_PROD_ID ||
                           'creation failed : Error is ' || SQLERRM);
    
  END PRC_CREATE_TABLE_PARTITION;

  PROCEDURE PRC_REBUILD_IDEXES(IN_TABLE_NAME  IN VARCHAR2,
                               OUT_STATUS_MSG OUT VARCHAR2,
                               OUT_STATUS     OUT NUMBER) AS
  
    V_INDEX_NAME VARCHAR2(30);
  
  BEGIN
  
    FOR REC IN (SELECT U.INDEX_NAME
                  FROM USER_INDEXES U
                 WHERE U.TABLE_NAME = IN_TABLE_NAME) LOOP
    
      V_INDEX_NAME := REC.INDEX_NAME;
    
      EXECUTE IMMEDIATE 'ALTER INDEX ' || V_INDEX_NAME ||
                        ' REBUILD ONLINE ';
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('INDEX REBUILD DONE SUCCESSFULLY FOR TABLE ' ||
                         IN_TABLE_NAME);
  
    OUT_STATUS_MSG := 'INDEX REBUILD DONE SUCCESSFULLY FOR TABLE ' ||
                      IN_TABLE_NAME;
  
    OUT_STATUS := 0;
  
  EXCEPTION
    WHEN OTHERS THEN
    
      OUT_STATUS_MSG := 'INDEX REBUILD FAILED FOR INDEX ' || V_INDEX_NAME ||
                        ' on table ' || IN_TABLE_NAME || ' Error is : ' ||
                        SQLERRM;
      OUT_STATUS     := 1;
      DBMS_OUTPUT.PUT_LINE('INDEX REBUILD FAILED FOR INDEX ' ||
                           V_INDEX_NAME || ' on table ' || IN_TABLE_NAME ||
                           ' Error is : ' || SQLERRM);
    
  END PRC_REBUILD_IDEXES;

  PROCEDURE SP_INORS_CREATE_CUSTOMER(IN_EXISTING_CUSTOMER_ID  IN NUMBER,
                                     IN_PRODUCT_CODE          IN VARCHAR2,
                                     IN_NEW_CUSTOMER_NAME     IN VARCHAR2,
                                     IN_OLD_PUBLIC_ORGTP      IN VARCHAR2,
                                     IN_OLD_NON_PUBLIC_ORGTP  IN VARCHAR2,
                                     IN_NEW_PUBLIC_ORGTP      IN VARCHAR2,
                                     IN_NEW_NON_PUBLIC_ORGTP  IN VARCHAR2,
                                     IN_INORS_CTB_COM_TP_CODE IN VARCHAR2,
                                     OUT_INORS_CTB_COM_STATUS OUT VARCHAR2,
                                     OUT_STATUS_FLAG          OUT VARCHAR2) IS
  
    LV_STATE_ORG_NODE_ID           NUMBER;
    LV_STATE_ORG_NODE_ID_EXISTS    NUMBER;
    LV_STATE_ORG_NODE_EXIST_CUST   NUMBER;
    V_CUSTOMER_ID                  NUMBER;
    V_PRODUCT_ID                   NUMBER;
    V_TP_ID                        NUMBER;
    V_OLD_PUBLIC_TP_ID             NUMBER;
    V_OLD_NON_PUBLIC_TP_ID         NUMBER;
    V_OLD_CUST_PROD_ID             NUMBER;
    V_NEW_CUST_PROD_ID             NUMBER;
    V_SEQ_COUNTER                  NUMBER;
    V_SEQ_COUNTER_2                NUMBER;
  --  V_MSG_TYPEID                   NUMBER;
    V_DISAGGREGATIONCATEGORYTYPEID NUMBER;
    V_DISAGGREGATIONCATEGORYID     NUMBER;
  --  V_CUTSCOREIPIID                NUMBER;
    V_CUTSCORESCALESCOREID         NUMBER;
    V_ASFD_ORDERBYID               NUMBER;
    V_ARTICLE_METADATA             NUMBER;
    V_PDF_REPORTS                  NUMBER;
    LV_NEW_CUSTOMER_EXISTS         NUMBER;
    V_CUST_PROD_EXISTS             NUMBER;
    V_PRODUCT_EDITABLE             NUMBER;
    V_ORG_TP_CHECK                 NUMBER;
    V_ADMINID                      NUMBER;
    V_OLD_CUST_PROD_EXISTS         NUMBER;
    V_INORS_CTB_COM                NUMBER;
    V_INORS_CTB_COM_CODE           NUMBER;
    V_OLD_CUSTOMER_ID_EXISTS       NUMBER;
    V_OUT_STATUS_MSG               VARCHAR2(32000);
    V_OUT_STATUS                   NUMBER(5);
    V_PRODUCT_MULTIPLE_EDITABLE    NUMBER;
    EXCEPTION_CUST_PROD_EXISTS EXCEPTION;
    EXCEPTION_PRODUCT_NOT_EDITABLE EXCEPTION;
    EXCEPTION_OLD_ORGTP_NOT_VALID EXCEPTION;
    EXCEPTION_NEW_ORGTP_NOT_VALID EXCEPTION;
    EXCEPTION_OLD_CUST_PROD_EXISTS EXCEPTION;
    EXCEPTION_CTB_COM_TP_CODE EXCEPTION;
    EXCEPTION_OLD_CUSTOMER_EXISTS EXCEPTION;
    EXCEPTION_OLD_CUSTOMER_STATE EXCEPTION;
    EXCEPTION_PARTITION_CREAT_FAIL EXCEPTION;
    EXCEPTION_INDEX_REBUILD_FAIL EXCEPTION;
    EXCEPTION_MULTI_PRODUCT_EDIT EXCEPTION;
  
  BEGIN
  
    SELECT COUNT(1)
      INTO V_PRODUCT_MULTIPLE_EDITABLE
      FROM PRODUCT PRD2
     WHERE PRD2.IS_EDITABLE = 'Y'
       AND PRD2.PROJECTID = 2;
  
    IF V_OLD_CUSTOMER_ID_EXISTS > 1 THEN
    
      RAISE EXCEPTION_MULTI_PRODUCT_EDIT;
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_OLD_CUSTOMER_ID_EXISTS
      FROM CUSTOMER_INFO CI
     WHERE CI.CUSTOMERID = IN_EXISTING_CUSTOMER_ID;
  
    IF V_OLD_CUSTOMER_ID_EXISTS = 0 THEN
    
      RAISE EXCEPTION_OLD_CUSTOMER_EXISTS;
    
    END IF;
    
    
  
    SELECT COUNT(1)
      INTO V_PRODUCT_EDITABLE
      FROM PRODUCT PRD1
     WHERE PRD1.PRODUCT_CODE = IN_PRODUCT_CODE
       AND PRD1.IS_EDITABLE = 'Y';
  
    IF V_PRODUCT_EDITABLE = 0 THEN
    
      RAISE EXCEPTION_PRODUCT_NOT_EDITABLE;
    
    END IF;
  
    SELECT A.ADMINID
      INTO V_ADMINID
      FROM ADMIN_DIM A
     WHERE A.IS_CURRENT_ADMIN = 'Y'
       AND A.PROJECTID = 2;
  
    SELECT PRODUCTID
      INTO V_PRODUCT_ID
      FROM PRODUCT
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE;
  
    SELECT COUNT(1)
      INTO V_OLD_CUST_PROD_EXISTS
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = V_PRODUCT_ID
       AND CUSTOMERID = IN_EXISTING_CUSTOMER_ID
       AND ADMINID = V_ADMINID;
  
    IF V_OLD_CUST_PROD_EXISTS = 0 THEN
    
      RAISE EXCEPTION_OLD_CUST_PROD_EXISTS;
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_ORG_TP_CHECK
      FROM TEST_PROGRAM TP1
     WHERE TP1.CUSTOMERID = IN_EXISTING_CUSTOMER_ID
       AND TP1.TP_CODE IN (IN_OLD_PUBLIC_ORGTP, IN_OLD_NON_PUBLIC_ORGTP);
  
    IF V_ORG_TP_CHECK <> 2 THEN
    
      RAISE EXCEPTION_OLD_ORGTP_NOT_VALID;
    
    END IF;
  
    SELECT COUNT(1)
      INTO LV_NEW_CUSTOMER_EXISTS
      FROM CUSTOMER_INFO
     WHERE CUSTOMER_NAME = IN_NEW_CUSTOMER_NAME;
  
    IF LV_NEW_CUSTOMER_EXISTS = 0 THEN
    
      -- Insert into CUSTOMER_INFO
    
      SELECT MAX(CUSTOMERID) + 1
        INTO V_CUSTOMER_ID
        FROM PRISMGLOBAL.CUSTOMER_INFO;
    
      INSERT INTO CUSTOMER_INFO
        (CUSTOMERID,
         CUSTOMER_NAME,
         DISPLAY_TP_SELECTION,
         FILE_LOCATION,
         DATETIMESTAMP,
         SUPPORT_EMAIL,
         SEND_LOGIN_PDF,
         CUSTOMER_CODE,
         PROJECTID )
        SELECT V_CUSTOMER_ID,
               IN_NEW_CUSTOMER_NAME,
               CINF.DISPLAY_TP_SELECTION,
               CINF.FILE_LOCATION,
               SYSDATE,
               CINF.SUPPORT_EMAIL,
               NULL,
               CINF.CUSTOMER_CODE,
               CINF.PROJECTID
          FROM CUSTOMER_INFO CINF
         WHERE CINF.CUSTOMERID = IN_EXISTING_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Inserted data into CUSTOMER_INFO');
    
      -- Insert into org_user_define_lookup
      INSERT INTO ORG_USER_DEFINE_LOOKUP
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
               USER_NAME,
               USER_PASSWORD,
               V_CUSTOMER_ID,
               SYSDATE
          FROM ORG_USER_DEFINE_LOOKUP OUD
         WHERE OUD.CUSTOMERID = IN_EXISTING_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Inserted data into org_user_define_lookup');
    
    ELSE
    
      SELECT COUNT(1)
        INTO V_CUST_PROD_EXISTS
        FROM CUST_PRODUCT_LINK CPL,
             CUSTOMER_INFO     CI,
             PRODUCT           PRD,
             ADMIN_DIM         ADM
       WHERE CPL.CUSTOMERID = CI.CUSTOMERID
         AND CI.CUSTOMER_NAME = IN_NEW_CUSTOMER_NAME
         AND PRD.PRODUCT_CODE = IN_PRODUCT_CODE
         AND PRD.IS_EDITABLE = 'Y'
         AND CPL.PRODUCTID = PRD.PRODUCTID
         AND ADM.IS_CURRENT_ADMIN = 'Y'
         AND ADM.ADMINID = CPL.ADMINID;
    
      IF V_CUST_PROD_EXISTS <> 0 THEN
      
        RAISE EXCEPTION_CUST_PROD_EXISTS;
      
      END IF;
    
      SELECT CUSTOMERID
        INTO V_CUSTOMER_ID
        FROM CUSTOMER_INFO
       WHERE CUSTOMER_NAME = IN_NEW_CUSTOMER_NAME;
    
    END IF;
  
    -- INSERT RECORD FOR 'INORS CTB.COM' INTO TEST_PROGRAM
  
    SELECT COUNT(1)
      INTO V_INORS_CTB_COM
      FROM TEST_PROGRAM TST
     WHERE TST.TP_NAME = 'INORS CTB.COM'
       AND TST.CUSTOMERID = V_CUSTOMER_ID
       AND TST.ADMINID = V_ADMINID;
  
    IF V_INORS_CTB_COM = 0 AND LENGTH(TRIM(IN_INORS_CTB_COM_TP_CODE)) <> 0 AND
       IN_INORS_CTB_COM_TP_CODE IS NOT NULL THEN
    
      SELECT COUNT(1)
        INTO V_INORS_CTB_COM_CODE
        FROM TEST_PROGRAM TST1
       WHERE TST1.TP_CODE = IN_INORS_CTB_COM_TP_CODE
         AND TST1.CUSTOMERID = V_CUSTOMER_ID;
    
      IF (V_INORS_CTB_COM_CODE <> 0) THEN
      
        RAISE EXCEPTION_CTB_COM_TP_CODE;
      
      END IF;
    
      SELECT MAX(TP_ID) + 1 INTO V_TP_ID FROM PRISMGLOBAL.TEST_PROGRAM;
    
      INSERT INTO TEST_PROGRAM
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
        (V_TP_ID,
         IN_INORS_CTB_COM_TP_CODE,
         'INORS CTB.COM',
         'PUBLIC',
         4,
         'OL',
         V_CUSTOMER_ID,
         V_ADMINID,
         'AC',
         SYSDATE);
    
      DBMS_OUTPUT.PUT_LINE('Inserted data into TEST_PROGRAM for INORS CTB.COM TP_CODE : ' ||
                           IN_INORS_CTB_COM_TP_CODE);
    
      OUT_INORS_CTB_COM_STATUS := 'TP_NAME: INORS CTB.COM entry made in TEST_PROGRAM for Customer ID: ' ||
                                  V_CUSTOMER_ID || ' and Admin Year ID: ' ||
                                  V_ADMINID;
    
    ELSIF V_INORS_CTB_COM > 0 THEN
    
      OUT_INORS_CTB_COM_STATUS := 'INORS CTB.COM entry already present for Customer ID: ' ||
                                  V_CUSTOMER_ID || ' and Admin Year ID: ' ||
                                  V_ADMINID;
    
    ELSIF LENGTH(TRIM(IN_INORS_CTB_COM_TP_CODE)) = 0 OR
          IN_INORS_CTB_COM_TP_CODE IS NULL THEN
    
      OUT_INORS_CTB_COM_STATUS := 'INORS CTB.COM entry not made as input field IN_INORS_CTB_COM_TP_CODE is empty or NULL';
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_ORG_TP_CHECK
      FROM TEST_PROGRAM TP1
     WHERE TP1.CUSTOMERID = V_CUSTOMER_ID
       AND TP1.TP_CODE IN (IN_NEW_PUBLIC_ORGTP, IN_NEW_NON_PUBLIC_ORGTP)
       AND TP1.ADMINID = V_ADMINID;
  
    IF V_ORG_TP_CHECK > 0 THEN
    
      RAISE EXCEPTION_NEW_ORGTP_NOT_VALID;
    
    END IF;
  
    -- Insert into TEST_PROGRAM and ORG_TP_STRUCTURE
  
    SELECT MAX(TP_ID) + 1 INTO V_TP_ID FROM PRISMGLOBAL.TEST_PROGRAM;
  
    SELECT TP_ID
      INTO V_OLD_PUBLIC_TP_ID
      FROM TEST_PROGRAM
     WHERE TP_CODE = IN_OLD_PUBLIC_ORGTP
       AND CUSTOMERID = IN_EXISTING_CUSTOMER_ID
       AND ADMINID = V_ADMINID;
  
    SELECT TP_ID
      INTO V_OLD_NON_PUBLIC_TP_ID
      FROM TEST_PROGRAM
     WHERE TP_CODE = IN_OLD_NON_PUBLIC_ORGTP
       AND CUSTOMERID = IN_EXISTING_CUSTOMER_ID
       AND ADMINID = V_ADMINID;
  
    INSERT INTO TEST_PROGRAM
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
      SELECT V_TP_ID,
             IN_NEW_PUBLIC_ORGTP,
             T.TP_NAME,
             T.TP_TYPE,
             T.NUM_LEVELS,
             T.TP_MODE,
             V_CUSTOMER_ID,
             V_ADMINID,
             T.ACTIVATION_STATUS,
             SYSDATE
        FROM TEST_PROGRAM T
       WHERE T.TP_ID = V_OLD_PUBLIC_TP_ID;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into TEST_PROGRAM for public orgtp');
  
    INSERT INTO ORG_TP_STRUCTURE
      (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
      (SELECT V_TP_ID, O.ORG_LEVEL, O.ORG_LABEL, SYSDATE
         FROM ORG_TP_STRUCTURE O
        WHERE O.TP_ID = V_OLD_PUBLIC_TP_ID);
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into ORG_TP_STRUCTURE for public orgtp');
  
    INSERT INTO TEST_PROGRAM
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
      SELECT V_TP_ID + 1,
             IN_NEW_NON_PUBLIC_ORGTP,
             T.TP_NAME,
             T.TP_TYPE,
             T.NUM_LEVELS,
             T.TP_MODE,
             V_CUSTOMER_ID,
             V_ADMINID,
             T.ACTIVATION_STATUS,
             SYSDATE
        FROM TEST_PROGRAM T
       WHERE T.TP_ID = V_OLD_NON_PUBLIC_TP_ID;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into TEST_PROGRAM for non public orgtp');
  
    INSERT INTO ORG_TP_STRUCTURE
      (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
      (SELECT V_TP_ID + 1, O.ORG_LEVEL, O.ORG_LABEL, SYSDATE
         FROM ORG_TP_STRUCTURE O
        WHERE O.TP_ID = V_OLD_NON_PUBLIC_TP_ID);
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into ORG_TP_STRUCTURE for non public orgtp');
  
    -- Insert into CUST_PRODUCT_LINK
  
    SELECT CUST_PROD_ID
      INTO V_OLD_CUST_PROD_ID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = V_PRODUCT_ID
       AND CUSTOMERID = IN_EXISTING_CUSTOMER_ID
       AND ADMINID = V_ADMINID;
  
    SELECT MAX(CUST_PROD_ID) + 1
      INTO V_NEW_CUST_PROD_ID
      FROM PRISMGLOBAL.CUST_PRODUCT_LINK;
  
    INSERT INTO CUST_PRODUCT_LINK
      (CUST_PROD_ID,
       CUSTOMERID,
       PRODUCTID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP)
      SELECT V_NEW_CUST_PROD_ID,
             V_CUSTOMER_ID,
             C.PRODUCTID,
             C.ADMINID,
             C.ACTIVATION_STATUS,
             SYSDATE
        FROM CUST_PRODUCT_LINK C
       WHERE C.CUST_PROD_ID = V_OLD_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into CUST_PRODUCT_LINK');
  
    /*    INSERT INTO dash_message_type
      (msg_typeid,
       message_name,
       message_type,
       description,
       cust_prod_id,
       created_date_time,
       updated_date_time)
      select dash_message_seq.NEXTVAL,
             message_name,
             message_type,
             description,
             V_NEW_CUST_PROD_ID,
             sysdate,
             null
        from dash_message_type
       where cust_prod_id = V_OLD_CUST_PROD_ID;
    
    DBMS_OUTPUT.PUT_LINE('Inserted data into DASH_MESSAGE_TYPE');*/
  
    -- Insert into org_node_dim, state entry for new customer if doesn't exists
  
    SELECT COUNT(1)
      INTO LV_STATE_ORG_NODE_ID_EXISTS
      FROM ISTEP.ORG_NODE_DIM OND1
     WHERE OND1.ORG_NODE_LEVEL = 1
       AND OND1.CUSTOMERID = V_CUSTOMER_ID;
  
    SELECT COUNT(1)
      INTO LV_STATE_ORG_NODE_EXIST_CUST
      FROM ISTEP.ORG_NODE_DIM
     WHERE ORG_NODE_LEVEL = 1
       AND CUSTOMERID = IN_EXISTING_CUSTOMER_ID;
  
    IF LV_STATE_ORG_NODE_EXIST_CUST = 0 THEN
    
      RAISE EXCEPTION_OLD_CUSTOMER_STATE;
    
    END IF;
  
    IF LV_STATE_ORG_NODE_ID_EXISTS = 0 THEN
    
      LV_STATE_ORG_NODE_ID := ISTEP.SEQ_ORG_NODE_DIM.NEXTVAL;
    
      INSERT INTO ISTEP.ORG_NODE_DIM
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
        SELECT LV_STATE_ORG_NODE_ID,
               ORG_NODE_NAME,
               ORG_NODE_CODE,
               ORG_NODE_LEVEL,
               STRC_ELEMENT,
               SPECIAL_CODES,
               ORG_MODE,
               PARENT_ORG_NODEID,
               ORG_NODE_CODE_PATH,
               EMAILS,
               V_CUSTOMER_ID,
               SYSDATE,
               SYSDATE
          FROM ISTEP.ORG_NODE_DIM
         WHERE ORG_NODE_LEVEL = 1
           AND CUSTOMERID = IN_EXISTING_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Inserted data into org_node_dim for State otgTP');
    
    ELSE
    
      SELECT ORG_NODEID
        INTO LV_STATE_ORG_NODE_ID
        FROM ISTEP.ORG_NODE_DIM
       WHERE ORG_NODE_LEVEL = 1
         AND CUSTOMERID = V_CUSTOMER_ID;
    
    END IF;
  
    -- Insert into org_product_link for state org nodeid for new customer
  
    INSERT INTO ISTEP.ORG_PRODUCT_LINK
      (ORG_PROD_ID, ORG_NODEID, CUST_PROD_ID, DATETIMESTAMP)
    VALUES
      (ISTEP.SEQ_ORG_PRODUCT_LINK.NEXTVAL,
       LV_STATE_ORG_NODE_ID,
       V_NEW_CUST_PROD_ID,
       SYSDATE);
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into org_product_link for State otgTP');
  
    -- Insert into org_test_program_link for state org nodeid for new customer
  
    INSERT INTO ISTEP.ORG_TEST_PROGRAM_LINK
      (ORG_NODEID, TP_ID, DATETIMESTAMP)
    VALUES
      (LV_STATE_ORG_NODE_ID, V_TP_ID, SYSDATE);
  
    INSERT INTO ISTEP.ORG_TEST_PROGRAM_LINK
      (ORG_NODEID, TP_ID, DATETIMESTAMP)
    VALUES
      (LV_STATE_ORG_NODE_ID, V_TP_ID + 1, SYSDATE);
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into org_test_program_link for State otgTP');
  
  -- INSERT INTO DASH_ACTION_ACCESS
  
    INSERT INTO DASH_ACTION_ACCESS
      (DB_ACT_ACCESSID,
       DB_MENUID,
       DB_REPORTID,
       DB_ACTIONID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       ACTION_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT SEQ_DASH_ACTION_ACCESS.NEXTVAL DB_ACT_ACCESSID,
             DB_MENUID,
             DB_REPORTID,
             DB_ACTIONID,
             ROLEID,
             ORG_LEVEL,
             V_NEW_CUST_PROD_ID CUST_PROD_ID,
             ACTION_SEQ,
             ACTIVATION_STATUS,
             SYSDATE CREATED_DATE_TIME,
             SYSDATE UPDATED_DATE_TIME
        FROM DASH_ACTION_ACCESS
       WHERE CUST_PROD_ID = V_OLD_CUST_PROD_ID;
      
      -- DASH_MENU_RPT_ACCESS
      
       INSERT INTO DASH_MENU_RPT_ACCESS(DB_MENUID,
                                  DB_REPORTID,
                                  ROLEID,
                                  ORG_LEVEL,
                                  CUST_PROD_ID,
                                  REPORT_SEQ,
                                  ACTIVATION_STATUS,
                                  CREATED_DATE_TIME)
        SELECT DB_MENUID,
               DB_REPORTID,
               ROLEID,
               ORG_LEVEL,
               V_NEW_CUST_PROD_ID CUST_PROD_ID,
               REPORT_SEQ,
               ACTIVATION_STATUS,
               SYSDATE CREATED_DATE_TIME
                FROM DASH_MENU_RPT_ACCESS
               WHERE CUST_PROD_ID = V_OLD_CUST_PROD_ID;
  
  
    /* INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (101,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Generic System Configuration'),
       2,
       1,
       V_NEW_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Generic System Configuration'),
       'AC',
       SYSDATE);
    
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (101,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Product Specific System Configuration'),
       2,
       1,
       V_NEW_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE REPORT_NAME = 'Product Specific System Configuration'),
       'AC',
       SYSDATE);*/
  
    --  Insert into DASH_MESSAGE_TYPE and DASH_MESSAGES
  
    --SELECT MAX(MSG_TYPEID) INTO V_MSG_TYPEID FROM DASH_MESSAGE_TYPE;
  
    FOR REC IN (SELECT MSG_TYPEID, MESSAGE_NAME, MESSAGE_TYPE, DESCRIPTION
                  FROM DASH_MESSAGE_TYPE
                 WHERE CUST_PROD_ID = V_OLD_CUST_PROD_ID
                   AND MESSAGE_TYPE <> 'GSCM') LOOP
    
      -- V_SEQ_COUNTER := RPTMSGTYPE_SEQ.nextval;
    
      --V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO DASH_MESSAGE_TYPE
        (MSG_TYPEID,
         MESSAGE_NAME,
         MESSAGE_TYPE,
         DESCRIPTION,
         CUST_PROD_ID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
      VALUES
        ( --V_SEQ_COUNTER,
         REC.MSG_TYPEID,
         REC.MESSAGE_NAME,
         REC.MESSAGE_TYPE,
         REC.DESCRIPTION,
         V_NEW_CUST_PROD_ID,
         SYSDATE,
         SYSDATE);
    
      INSERT INTO DASH_MESSAGES
        (DB_REPORTID,
         MSG_TYPEID,
         REPORT_MSG,
         CUST_PROD_ID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
        SELECT DB_REPORTID,
               --V_SEQ_COUNTER,
               MSG_TYPEID,
               REPORT_MSG,
               V_NEW_CUST_PROD_ID,
               ACTIVATION_STATUS,
               SYSDATE,
               SYSDATE
          FROM DASH_MESSAGES DM
         WHERE DM.CUST_PROD_ID = V_OLD_CUST_PROD_ID
           AND DM.MSG_TYPEID = REC.MSG_TYPEID;
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into DASH_MESSAGE_TYPE and DASH_MESSAGES ');
  
    --  Insert into DISAGGREGATION_CATEGORY_TYPE and DISAGGREGATION_CATEGORY
  
    V_SEQ_COUNTER := 0;
  
    SELECT MAX(DISAGGREGATIONCATEGORYTYPEID)
      INTO V_DISAGGREGATIONCATEGORYTYPEID
      FROM ISTEP.DISAGGREGATION_CATEGORY_TYPE;
  
    FOR REC IN (SELECT DISAGGREGATIONCATEGORYTYPEID,
                       DISAGGREGATIONCATEGORYTYPENAME,
                       ORDERBY
                  FROM ISTEP.DISAGGREGATION_CATEGORY_TYPE
                 WHERE CUST_PROD_ID = V_OLD_CUST_PROD_ID) LOOP
    
      V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO ISTEP.DISAGGREGATION_CATEGORY_TYPE
        (DISAGGREGATIONCATEGORYTYPEID,
         DISAGGREGATIONCATEGORYTYPENAME,
         ORDERBY,
         CUST_PROD_ID,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
      VALUES
        (V_DISAGGREGATIONCATEGORYTYPEID + V_SEQ_COUNTER,
         REC.DISAGGREGATIONCATEGORYTYPENAME,
         REC.ORDERBY,
         V_NEW_CUST_PROD_ID,
         SYSDATE,
         SYSDATE);
    
      SELECT MAX(DISAGGREGATIONCATEGORYID)
        INTO V_DISAGGREGATIONCATEGORYID
        FROM ISTEP.DISAGGREGATION_CATEGORY;
    
      V_SEQ_COUNTER_2 := 0;
    
      FOR REC2 IN (SELECT V_DISAGGREGATIONCATEGORYTYPEID + V_SEQ_COUNTER DISAGGREGATIONCATEGORYTYPEID,
                          DISAGGREGATIONCATEGORYCODE,
                          DISAGGREGATIONCATEGORYNAME,
                          ORDERBY,
                          V_NEW_CUST_PROD_ID CUST_PROD_ID,
                          SYSDATE CREATED_DATE_TIME,
                          SYSDATE UPDATED_DATE_TIME
                     FROM ISTEP.DISAGGREGATION_CATEGORY DC
                    WHERE DC.CUST_PROD_ID = V_OLD_CUST_PROD_ID
                      AND DC.DISAGGREGATIONCATEGORYTYPEID =
                          REC.DISAGGREGATIONCATEGORYTYPEID) LOOP
      
        V_SEQ_COUNTER_2 := V_SEQ_COUNTER_2 + 1;
      
        INSERT INTO ISTEP.DISAGGREGATION_CATEGORY
          (DISAGGREGATIONCATEGORYID,
           DISAGGREGATIONCATEGORYTYPEID,
           DISAGGREGATIONCATEGORYCODE,
           DISAGGREGATIONCATEGORYNAME,
           ORDERBY,
           CUST_PROD_ID,
           CREATED_DATE_TIME,
           UPDATED_DATE_TIME)
        VALUES
          (V_DISAGGREGATIONCATEGORYID + V_SEQ_COUNTER_2,
           REC2.DISAGGREGATIONCATEGORYTYPEID,
           REC2.DISAGGREGATIONCATEGORYCODE,
           REC2.DISAGGREGATIONCATEGORYNAME,
           REC2.ORDERBY,
           REC2.CUST_PROD_ID,
           REC2.CREATED_DATE_TIME,
           REC2.UPDATED_DATE_TIME
           
           );
      
      END LOOP;
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into DISAGGREGATION_CATEGORY_TYPE and DISAGGREGATION_CATEGORY');
  
    --  Insert into CUTSCOREIPI
  
    --SELECT MAX(CUTSCOREIPIID) INTO V_CUTSCOREIPIID FROM CUTSCOREIPI;
  
    V_SEQ_COUNTER := 0;
  
    FOR REC IN (SELECT CI.GRADEID,
                       CI.SUBTESTID,
                       V_NEW_CUST_PROD_ID CUST_PROD_ID,
                       CI.OBJECTIVEID,
                       CI.IPI_AT_PASS,
                       SYSDATE CREATED_DATE_TIME,
                       SYSDATE UPDATED_DATE_TIME
                  FROM ISTEP.CUTSCOREIPI CI
                 WHERE CI.CUST_PROD_ID = V_OLD_CUST_PROD_ID) LOOP
    
      V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO ISTEP.CUTSCOREIPI
        (CUTSCOREIPIID,
         GRADEID,
         SUBTESTID,
         CUST_PROD_ID,
         OBJECTIVEID,
         IPI_AT_PASS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
      VALUES
        (--V_CUTSCOREIPIID + V_SEQ_COUNTER,
         ISTEP.SEQ_CUTSCOREIPI.NEXTVAL,
         REC.GRADEID,
         REC.SUBTESTID,
         REC.CUST_PROD_ID,
         REC.OBJECTIVEID,
         REC.IPI_AT_PASS,
         REC.CREATED_DATE_TIME,
         REC.UPDATED_DATE_TIME);
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into CUTSCOREIPI');
  
    --  Insert into CUTSCORESCALESCORE
  
    SELECT MAX(CUTSCORESCALESCOREID)
      INTO V_CUTSCORESCALESCOREID
      FROM ISTEP.CUTSCORESCALESCORE;
  
    V_SEQ_COUNTER := 0;
  
    FOR REC IN (SELECT GRADEID,
                       LEVELID,
                       SUBTESTID,
                       V_NEW_CUST_PROD_ID CUST_PROD_ID,
                       LOSS,
                       HOSS,
                       PASS,
                       PASSPLUS,
                       SYSDATE CREATED_DATE_TIME,
                       SYSDATE UPDATED_DATE_TIME
                  FROM ISTEP.CUTSCORESCALESCORE CSI
                 WHERE CSI.CUST_PROD_ID = V_OLD_CUST_PROD_ID) LOOP
    
      V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO ISTEP.CUTSCORESCALESCORE
        (CUTSCORESCALESCOREID,
         GRADEID,
         LEVELID,
         SUBTESTID,
         CUST_PROD_ID,
         LOSS,
         HOSS,
         PASS,
         PASSPLUS,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME)
      VALUES
        (V_CUTSCORESCALESCOREID + V_SEQ_COUNTER,
         REC.GRADEID,
         REC.LEVELID,
         REC.SUBTESTID,
         REC.CUST_PROD_ID,
         REC.LOSS,
         REC.HOSS,
         REC.PASS,
         REC.PASSPLUS,
         REC.CREATED_DATE_TIME,
         REC.UPDATED_DATE_TIME);
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into CUTSCORESCALESCORE');
  
    --  Insert into ASFD_ORDERBY
  
    SELECT MAX(ASFD_ORDERBYID) INTO V_ASFD_ORDERBYID FROM ISTEP.ASFD_ORDERBY;
  
    V_SEQ_COUNTER := 0;
  
    FOR REC IN (SELECT OBJECTIVEID,
                       OBJECTIVE_NAME,
                       GRADEID,
                       SUBTESTID,
                       ORDERBY,
                       V_NEW_CUST_PROD_ID CUST_PROD_ID
                  FROM ISTEP.ASFD_ORDERBY ASFD
                 WHERE ASFD.CUST_PROD_ID = V_OLD_CUST_PROD_ID) LOOP
    
      V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO ISTEP.ASFD_ORDERBY
        (ASFD_ORDERBYID,
         OBJECTIVEID,
         OBJECTIVE_NAME,
         GRADEID,
         SUBTESTID,
         ORDERBY,
         CUST_PROD_ID)
      VALUES
        (V_ASFD_ORDERBYID + V_SEQ_COUNTER,
         REC.OBJECTIVEID,
         REC.OBJECTIVE_NAME,
         REC.GRADEID,
         REC.SUBTESTID,
         REC.ORDERBY,
         REC.CUST_PROD_ID);
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into ASFD_ORDERBY');
  
    --  Insert into ARTICLE_METADATA
  
    SELECT MAX(ARTICLEID) INTO V_ARTICLE_METADATA FROM ISTEP.ARTICLE_METADATA;
  
    V_SEQ_COUNTER := 0;
  
    FOR REC IN (SELECT ARTICLE_NAME,
                       V_NEW_CUST_PROD_ID CUST_PROD_ID,
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
                       SYSDATE CREATED_DATE_TIME,
                       SYSDATE UPDATED_DATE_TIME
                  FROM ISTEP.ARTICLE_METADATA ARTC
                 WHERE ARTC.CUST_PROD_ID = V_OLD_CUST_PROD_ID
                 ORDER BY ARTC.ARTICLEID) LOOP
    
      V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO ISTEP.ARTICLE_METADATA
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
      VALUES
        (ISTEP.article_metadata_seq.nextval,
         REC.ARTICLE_NAME,
         REC.CUST_PROD_ID,
         REC.SUBT_OBJ_MAPID,
         REC.SUBTESTID,
         REC.ARTICLE_CONTENT_ID,
         REC.CATEGORY,
         REC.CATEGORY_TYPE,
         REC.CATEGORY_SEQ,
         REC.SUB_HEADER,
         REC.DESCRIPTION,
         REC.GRADEID,
         REC.PROFICIENCY_LEVEL,
         REC.RESOLVED_RPRT_STATUS,
         REC.CREATED_DATE_TIME,
         REC.UPDATED_DATE_TIME);
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into ARTICLE_METADATA' ||
                         V_OLD_CUST_PROD_ID);
  
    --  Insert into PDF_REPORTS
  
    SELECT MAX(PDF_REPORTID) INTO V_PDF_REPORTS FROM PDF_REPORTS;
  
    V_SEQ_COUNTER := 0;
  
    FOR REC IN (SELECT ORG_NODE_LEVEL,
                       REPORT_NAME,
                       REPORT_CODE,
                       V_NEW_CUST_PROD_ID CUST_PROD_ID,
                       ACTIVATION_STATUS,
                       SYSDATE DATETIMESTAMP
                  FROM PDF_REPORTS PDF
                 WHERE PDF.CUST_PROD_ID = V_OLD_CUST_PROD_ID) LOOP
    
      V_SEQ_COUNTER := V_SEQ_COUNTER + 1;
    
      INSERT INTO PDF_REPORTS
        (PDF_REPORTID,
         ORG_NODE_LEVEL,
         REPORT_NAME,
         REPORT_CODE,
         CUST_PROD_ID,
         ACTIVATION_STATUS,
         DATETIMESTAMP)
      VALUES
        (V_PDF_REPORTS + V_SEQ_COUNTER,
         REC.ORG_NODE_LEVEL,
         REC.REPORT_NAME,
         REC.REPORT_CODE,
         REC.CUST_PROD_ID,
         REC.ACTIVATION_STATUS,
         REC.DATETIMESTAMP);
    
    END LOOP;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data into PDF_REPORTS');
  
    --INSERT into STATE_MEAN_IPI_SCORE
  
    INSERT INTO ISTEP.STATE_MEAN_IPI_SCORE
      (STATE_MEAN_IPI_SCOREID,
       GRADEID,
       SUBTESTID,
       CUST_PROD_ID,
       OBJECTIVEID,
       ISPUBLIC,
       MEAN_IPI,
       CREATED_DATE_TIME,
       UPDATED_DATE_TIME)
      SELECT ISTEP.SEQ_STATE_MEAN_IPI_SCORE.NEXTVAL AS STATE_MEAN_IPI_SCOREID,
             SMIS.GRADEID,
             SMIS.SUBTESTID,
             V_NEW_CUST_PROD_ID AS CUST_PROD_ID,
             SMIS.OBJECTIVEID,
             SMIS.ISPUBLIC,
             SMIS.MEAN_IPI,
             SYSDATE AS CREATED_DATE_TIME,
             SYSDATE AS UPDATED_DATE_TIME
        FROM ISTEP.STATE_MEAN_IPI_SCORE SMIS
       WHERE SMIS.CUST_PROD_ID = V_OLD_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Inserted data to STATE_MEAN_IPI_SCORE');
  
    /*PRC_CREATE_TABLE_PARTITION(V_NEW_CUST_PROD_ID,
                               V_OUT_STATUS_MSG,
                               V_OUT_STATUS);
  
    IF V_OUT_STATUS = 1 THEN
    
      RAISE EXCEPTION_PARTITION_CREAT_FAIL;
    
    END IF;
  
    OUT_STATUS_FLAG := 'SUCCESSFUL ' || V_OUT_STATUS_MSG;
  
    V_OUT_STATUS := NULL;*/
  
    -- Rebuild index for RESULTS_GRT_FACT
  
    PRC_REBUILD_IDEXES('RESULTS_GRT_FACT', V_OUT_STATUS_MSG, V_OUT_STATUS);
  
    IF V_OUT_STATUS = 1 THEN
    
      RAISE EXCEPTION_INDEX_REBUILD_FAIL;
    
    END IF;
  
    COMMIT;
  
    OUT_STATUS_FLAG := OUT_STATUS_FLAG || V_OUT_STATUS_MSG;
  
  EXCEPTION
  
    /*WHEN EXCEPTION_PARTITION_CREAT_FAIL THEN
    
      DBMS_OUTPUT.PUT_LINE('Partition creation failed' || V_OUT_STATUS_MSG);
    
      ROLLBACK;
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Partition creation failed ' ||
                         V_OUT_STATUS_MSG;*/
    
    WHEN EXCEPTION_INDEX_REBUILD_FAIL THEN
    
      DBMS_OUTPUT.PUT_LINE('Index rebuild failed ' || V_OUT_STATUS_MSG);
    
      ROLLBACK;
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Index rebuild failed ' ||
                         V_OUT_STATUS_MSG;
    
    WHEN EXCEPTION_OLD_CUSTOMER_EXISTS THEN
    
      DBMS_OUTPUT.PUT_LINE('Existing customer ID is not present');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Existing customer ID is not present';
    
    WHEN EXCEPTION_CUST_PROD_EXISTS THEN
    
      DBMS_OUTPUT.PUT_LINE('Product code entry already present for this new customer name');
    
      ROLLBACK;
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Product code entry already present for this new customer name ';
    
    WHEN EXCEPTION_PRODUCT_NOT_EDITABLE THEN
    
      DBMS_OUTPUT.PUT_LINE('Product code is not editable or Product entry is not present');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Product code is not editable or Product entry is not present';
    
    WHEN EXCEPTION_OLD_ORGTP_NOT_VALID THEN
    
      DBMS_OUTPUT.PUT_LINE('INVALID Old ORG TPs :Old org TPs entered are not mapped with existing customer id');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: INVALID Old ORG TPs :Old org TPs entered are not mapped with existing customer id';
    
    WHEN EXCEPTION_NEW_ORGTP_NOT_VALID THEN
    
      DBMS_OUTPUT.PUT_LINE('INVALID New ORG TPs :New org TPs entered are already mapped with new customer name for some product');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: INVALID New ORG TPs :New org TPs entered are already mapped with new customer name for some product';
    
    WHEN EXCEPTION_OLD_CUST_PROD_EXISTS THEN
    
      DBMS_OUTPUT.PUT_LINE('Product not associated with existing customer for current admin year');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Product not associated with existing customer for current admin year';
    
    WHEN EXCEPTION_CTB_COM_TP_CODE THEN
    
      DBMS_OUTPUT.PUT_LINE('CTB_COM_TP_CODE already exists for this customer for some other admin year');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as CTB_COM_TP_CODE already exists  for this customer for some other admin year';
    
      OUT_STATUS_FLAG := 'FAILED: CTB_COM_TP_CODE already exists  for this customer for some other admin year';
    
    WHEN EXCEPTION_OLD_CUSTOMER_STATE THEN
    
      DBMS_OUTPUT.PUT_LINE('No state id associated with existing customer in ORG_NODE_DIM');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: No state id associated with existing customer in ORG_NODE_DIM';
    
    WHEN EXCEPTION_MULTI_PRODUCT_EDIT THEN
    
      DBMS_OUTPUT.PUT_LINE('Multiple product is editable');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Multiple product is editable';
    
  END SP_INORS_CREATE_CUSTOMER;

  PROCEDURE SP_INORS_DELETE_CUSTOMER(IN_CUSTOMER_ID            IN NUMBER,
                                     IN_PRODUCT_CODE_TO_DELETE IN VARCHAR2,
                                     IN_PUBLIC_ORGTP           IN VARCHAR2,
                                     IN_NON_PUBLIC_ORGTP       IN VARCHAR2,
                                     IN_INORS_CTB_COM_TP_CODE  IN VARCHAR2,
                                     OUT_INORS_CTB_COM_STATUS  OUT VARCHAR2,
                                     OUT_STATUS_FLAG           OUT VARCHAR2) IS
  
    V_STATE_ORG_NODE_ID       NUMBER;
    V_CUST_PROD_ID            NUMBER;
    V_PRODUCT_ID              NUMBER;
    V_CUSTOMER_CAN_BE_DELETED NUMBER;
    V_PRODUCT_EDITABLE        NUMBER;
    V_ORG_TP_CHECK            NUMBER;
    V_TOTAL_PRODUCT_EDITABLE  NUMBER;
    V_ADMINID                 NUMBER;
    V_CUST_PROD_EXISTS        NUMBER;
    V_INORS_CTB_COM           NUMBER;
    V_CUSTOMER_ID_EXISTS      NUMBER;
    EXCEPTION_PRODUCT_NOT_EDITABLE EXCEPTION;
    EXCEPTION_ORGTP_NOT_VALID EXCEPTION;
    EXCEPTION_MORE_PROD_EDITABLE EXCEPTION;
    EXCEPTION_CUST_PROD_EXISTS EXCEPTION;
    EXCEPTION_CUSTOMER_EXISTS EXCEPTION;
  
  BEGIN
  
    SELECT COUNT(1)
      INTO V_CUSTOMER_ID_EXISTS
      FROM CUSTOMER_INFO CI
     WHERE CI.CUSTOMERID = IN_CUSTOMER_ID;
  
    IF V_CUSTOMER_ID_EXISTS = 0 THEN
    
      RAISE EXCEPTION_CUSTOMER_EXISTS;
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_PRODUCT_EDITABLE
      FROM PRODUCT PRD1
     WHERE PRD1.PRODUCT_CODE = IN_PRODUCT_CODE_TO_DELETE
       AND PRD1.IS_EDITABLE = 'Y';
  
    IF V_PRODUCT_EDITABLE = 0 THEN
    
      RAISE EXCEPTION_PRODUCT_NOT_EDITABLE;
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_TOTAL_PRODUCT_EDITABLE
      FROM PRODUCT PRD2
     WHERE PRD2.IS_EDITABLE = 'Y';
  
    IF V_TOTAL_PRODUCT_EDITABLE > 1 THEN
    
      RAISE EXCEPTION_MORE_PROD_EDITABLE;
    
    END IF;
  
    SELECT A.ADMINID
      INTO V_ADMINID
      FROM ADMIN_DIM A
     WHERE A.IS_CURRENT_ADMIN = 'Y';
  
    SELECT PRODUCTID
      INTO V_PRODUCT_ID
      FROM PRODUCT
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE_TO_DELETE;
  
    SELECT COUNT(1)
      INTO V_CUST_PROD_EXISTS
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = V_PRODUCT_ID
       AND CUSTOMERID = IN_CUSTOMER_ID
       AND ADMINID = V_ADMINID;
  
    IF V_CUST_PROD_EXISTS = 0 THEN
    
      RAISE EXCEPTION_CUST_PROD_EXISTS;
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_ORG_TP_CHECK
      FROM TEST_PROGRAM TP1
     WHERE TP1.CUSTOMERID = IN_CUSTOMER_ID
       AND TP1.TP_CODE IN (IN_PUBLIC_ORGTP, IN_NON_PUBLIC_ORGTP);
  
    IF V_ORG_TP_CHECK <> 2 THEN
    
      RAISE EXCEPTION_ORGTP_NOT_VALID;
    
    END IF;
  
    SELECT CUST_PROD_ID
      INTO V_CUST_PROD_ID
      FROM CUST_PRODUCT_LINK
     WHERE PRODUCTID = V_PRODUCT_ID
       AND CUSTOMERID = IN_CUSTOMER_ID
       AND ADMINID = V_ADMINID;
  
    --  DELETE FROM PDF_REPORTS
  
    DELETE FROM PDF_REPORTS WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from PDF_REPORTS');
  
    --  DELETE FROM ARTICLE_METADATA
  
    DELETE FROM ISTEP.ARTICLE_METADATA WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from ARTICLE_METADATA');
  
    --  DELETE FROM ASFD_ORDERBY
  
    DELETE FROM ISTEP.ASFD_ORDERBY WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from ASFD_ORDERBY');
  
    --  DELETE FROM CUTSCORESCALESCORE
  
    DELETE FROM ISTEP.CUTSCORESCALESCORE WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from CUTSCORESCALESCORE');
  
    --  DELETE FROM CUTSCOREIPI
  
    DELETE FROM ISTEP.CUTSCOREIPI WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from CUTSCOREIPI');
  
    --  DELETE FROM DISAGGREGATION_CATEGORY
  
    DELETE FROM ISTEP.DISAGGREGATION_CATEGORY
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from DISAGGREGATION_CATEGORY');
  
    --  DELETE FROM DISAGGREGATION_CATEGORY_TYPE
  
    DELETE FROM ISTEP.DISAGGREGATION_CATEGORY_TYPE
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from DISAGGREGATION_CATEGORY_TYPE');
                         
    -- DELETE FROM DASH_ACTION_ACCESS
    
     DELETE FROM DASH_ACTION_ACCESS WHERE CUST_PROD_ID = V_CUST_PROD_ID;                    
  
    --  DELETE FROM DASH_MENU_RPT_ACCESS
  
    DELETE FROM DASH_MENU_RPT_ACCESS WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from DASH_MENU_RPT_ACCESS');
  
    --  DELETE FROM DASH_MESSAGES
  
    DELETE FROM DASH_MESSAGES WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from DASH_MESSAGES');
  
    --  DELETE FROM DASH_MESSAGE_TYPE
  
    DELETE FROM DASH_MESSAGE_TYPE WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from DASH_MESSAGE_TYPE');
  
    --Delete from STATE_MEAN_IPI_SCORE
  
    DELETE FROM ISTEP.STATE_MEAN_IPI_SCORE SMIS
     WHERE SMIS.CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from STATE_MEAN_IPI_SCORE');
  
    SELECT ORG_NODEID
      INTO V_STATE_ORG_NODE_ID
      FROM ISTEP.ORG_NODE_DIM
     WHERE ORG_NODE_LEVEL = 1
       AND CUSTOMERID = IN_CUSTOMER_ID;
  
    -- Delete from org_product_link for state org nodeid for new customer
  
    DELETE FROM ISTEP.ORG_PRODUCT_LINK
     WHERE ORG_NODEID = V_STATE_ORG_NODE_ID
       AND CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from ORG_PRODUCT_LINK for state org tp');
  
    -- DELETE FROM CUST_PRODUCT_LINK
  
    DELETE FROM CUST_PRODUCT_LINK WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from CUST_PRODUCT_LINK');
  
    -- DELETE from org_test_program_link for state org nodeid for new customer
  
    DELETE FROM ISTEP.ORG_TEST_PROGRAM_LINK
     WHERE ORG_NODEID = V_STATE_ORG_NODE_ID
       AND TP_ID IN
           (SELECT T1.TP_ID
              FROM TEST_PROGRAM T1
             WHERE T1.TP_CODE IN (IN_PUBLIC_ORGTP, IN_NON_PUBLIC_ORGTP)
               AND CUSTOMERID = IN_CUSTOMER_ID);
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from org_test_program_link for state org tp');
  
    -- DELETE FROM   ORG_TP_STRUCTURE and TEST_PROGRAM
  
    DELETE FROM ORG_TP_STRUCTURE T
     WHERE T.TP_ID IN
           (SELECT T1.TP_ID
              FROM TEST_PROGRAM T1
             WHERE T1.TP_CODE IN (IN_PUBLIC_ORGTP, IN_NON_PUBLIC_ORGTP)
               AND CUSTOMERID = IN_CUSTOMER_ID);
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from ORG_TP_STRUCTURE');
  
    DELETE FROM TEST_PROGRAM
     WHERE TP_ID IN
           (SELECT T1.TP_ID
              FROM TEST_PROGRAM T1
             WHERE T1.TP_CODE IN (IN_PUBLIC_ORGTP, IN_NON_PUBLIC_ORGTP)
               AND CUSTOMERID = IN_CUSTOMER_ID);
  
    DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                         ' rows from TEST_PROGRAM');
  
    -- DELETE RECORD FOR 'INORS CTB.COM' FROM TEST_PROGRAM
  
    SELECT COUNT(1)
      INTO V_INORS_CTB_COM
      FROM TEST_PROGRAM TST
     WHERE TST.TP_NAME = 'INORS CTB.COM'
       AND TST.CUSTOMERID = IN_CUSTOMER_ID
       AND TST.ADMINID = V_ADMINID
       AND TST.TP_CODE = IN_INORS_CTB_COM_TP_CODE;
  
    IF V_INORS_CTB_COM > 0 AND LENGTH(TRIM(IN_INORS_CTB_COM_TP_CODE)) <> 0 AND
       IN_INORS_CTB_COM_TP_CODE IS NOT NULL THEN
    
      DELETE FROM TEST_PROGRAM
       WHERE TP_NAME = 'INORS CTB.COM'
         AND TP_CODE = IN_INORS_CTB_COM_TP_CODE
         AND ADMINID = V_ADMINID
         AND CUSTOMERID = IN_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                           ' rows from TEST_PROGRAM for TP_NAME: INORS CTB.COM and TP_CODE: ' ||
                           IN_INORS_CTB_COM_TP_CODE ||
                           ' for Customer ID: ' || IN_CUSTOMER_ID ||
                           ' and Admin Year ID: ' || V_ADMINID);
    
      OUT_INORS_CTB_COM_STATUS := 'TP_NAME: INORS CTB.COM and TP_CODE: ' ||
                                  IN_INORS_CTB_COM_TP_CODE ||
                                  ' entry deleted from TEST_PROGRAM for Customer ID: ' ||
                                  IN_CUSTOMER_ID || ' and Admin Year ID: ' ||
                                  V_ADMINID;
    
    ELSIF LENGTH(TRIM(IN_INORS_CTB_COM_TP_CODE)) = 0 AND
          IN_INORS_CTB_COM_TP_CODE IS NULL THEN
    
      OUT_INORS_CTB_COM_STATUS := 'INORS CTB.COM entry not deleted as field IN_INORS_CTB_COM_TP_CODE is empty or NULL';
    
    ELSE
    
      OUT_INORS_CTB_COM_STATUS := 'INORS CTB.COM entry not deleted as no entry present for it in current admin';
    
    END IF;
  
    SELECT COUNT(1)
      INTO V_CUSTOMER_CAN_BE_DELETED
      FROM CUST_PRODUCT_LINK
     WHERE CUSTOMERID = IN_CUSTOMER_ID;
  
    IF V_CUSTOMER_CAN_BE_DELETED = 0 THEN
    
      -- DELETE RECORD FOR 'INORS CTB.COM' FROM TEST_PROGRAM
    
      DELETE FROM TEST_PROGRAM
       WHERE TP_NAME = 'INORS CTB.COM'
         AND ADMINID = V_ADMINID
         AND CUSTOMERID = IN_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('If rows exists then, deleted ' || SQL%ROWCOUNT ||
                           ' rows from TEST_PROGRAM for INORS CTB.COM for customer id: ' ||
                           IN_CUSTOMER_ID || ' and Admin Id: ' ||
                           V_ADMINID);
    
      IF (SQL%ROWCOUNT > 0) THEN
      
        OUT_INORS_CTB_COM_STATUS := 'Customer deleted hence INORS CTB.COM entry deleted from TEST_PROGRAM for customer id: ' ||
                                    IN_CUSTOMER_ID || ' and Admin Id: ' ||
                                    V_ADMINID;
      
      END IF;
    
      -- Delete from org_node_dim for state org nodeid for new customer
    
      DELETE FROM ISTEP.ORG_NODE_DIM
       WHERE ORG_NODEID = V_STATE_ORG_NODE_ID
         AND CUSTOMERID = IN_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                           ' rows from org_node_dim for State orgid entry for the customer');
    
      -- DELETE FROM org_user_define_lookup
    
      DELETE FROM ORG_USER_DEFINE_LOOKUP WHERE CUSTOMERID = IN_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                           ' rows from org_user_define_lookup');
    
      -- DELETE FROM CUSTOMER_INFO
    
      DELETE FROM CUSTOMER_INFO WHERE CUSTOMERID = IN_CUSTOMER_ID;
    
      DBMS_OUTPUT.PUT_LINE('Deleted ' || SQL%ROWCOUNT ||
                           ' rows from CUSTOMER_INFO');
    
    END IF;
  
    COMMIT;
  
    OUT_STATUS_FLAG := 'SUCCESSFUL';
  
  EXCEPTION
  
    WHEN EXCEPTION_CUSTOMER_EXISTS THEN
    
      DBMS_OUTPUT.PUT_LINE('Customer ID is not present');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not made as whole Customer creation transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Customer ID is not present';
    
    WHEN EXCEPTION_PRODUCT_NOT_EDITABLE THEN
    
      DBMS_OUTPUT.PUT_LINE('Product code is not editable or Product entry is not present');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not deleted as whole Customer deletion transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: Product code is not editable or Product entry is not present';
    
    WHEN EXCEPTION_ORGTP_NOT_VALID THEN
    
      DBMS_OUTPUT.PUT_LINE('INVALID ORG TPs : Org TPs entered are not mapped with the customer id');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not deleted as whole Customer deletion transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: INVALID ORG TPs : Org TPs entered are not mapped with the customer id';
    
    WHEN EXCEPTION_MORE_PROD_EDITABLE THEN
    
      DBMS_OUTPUT.PUT_LINE('More than one product is editable');
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not deleted as whole Customer deletion transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
      OUT_STATUS_FLAG := 'FAILED: More than one product is editable';
    
    WHEN EXCEPTION_CUST_PROD_EXISTS THEN
    
      DBMS_OUTPUT.PUT_LINE('Product not associated with the customer for current admin year');
    
      OUT_STATUS_FLAG := 'FAILED: Product not associated with the customer for current admin year';
    
      OUT_INORS_CTB_COM_STATUS := 'CTB_COM_TP_CODE entry not deleted as whole Customer deletion transaction failed or IN_INORS_CTB_COM_TP_CODE was empty';
    
  END SP_INORS_DELETE_CUSTOMER;

  /*--------------------------------------------------------
  Initial Customer 1000 configuration for new Product.
  --------------------------------------------------------*/

  /*PROCEDURE SP_INITIAL_PRODUCT_CONFIG(IN_CTB_PROJECTID          IN VARCHAR2,
                                      IN_CTB_PROJECTID_APS      IN VARCHAR2,
                                      IN_PRODUCT_CODE           IN VARCHAR2,
                                      IN_PRODUCT_NAME           IN VARCHAR2,
                                      IN_PUBLIC_ORGTP           IN VARCHAR2,
                                      IN_NONPUBLIC_ORGTP        IN VARCHAR2,
                                      IN_PUBLIC_BRAILE_ORGTP    IN VARCHAR2,
                                      IN_NONPUBLIC_BRAILE_ORGTP IN VARCHAR2,
                                      OUT_STATUS_FLAG_NEW       OUT NUMBER) IS
  
    V_PRODUCT_EXISTS      NUMBER;
    V_CUST_PROD_ID_EXISTS NUMBER;
    V_ORG_PROD_ID_EXISTS  NUMBER;
    V_PRODUCTID           NUMBER;
    V_PRODUCTSEQ          NUMBER;
    V_CUST_PROD_ID        NUMBER;
    V_ORG_PROD_ID         NUMBER;
    V_ADMINID             NUMBER;
    V_ADMIN_YEAR          NUMBER;
    V_ORG_NODEID_STATE    NUMBER;
    LV_VALIDATION         NUMBER;
  
    V_TP_ID_PUBLIC           NUMBER;
    V_TP_ID_NONPUBLIC        NUMBER;
    V_TP_ID_PUBLIC_BRAILE    NUMBER;
    V_TP_ID_NONPUBLIC_BRAILE NUMBER;
  
    V_TP_ID_P_EXISTS         NUMBER;
    V_TP_ID_NP_EXISTS        NUMBER;
    V_TP_ID_P_BRAILE_EXISTS  NUMBER;
    V_TP_ID_NP_BRAILE_EXISTS NUMBER;
  
    V_TP_ID_P_OTPL         NUMBER;
    V_TP_ID_NP_OTPL        NUMBER;
    V_TP_ID_P_BRAILE_OTPL  NUMBER;
    V_TP_ID_NP_BRAILE_OTPL NUMBER;
  
    TYPE LV_ORG_LABEL IS VARRAY(4) OF VARCHAR2(25);
    V_ORG_LABEL LV_ORG_LABEL;
    TYPE LV_CONTENT_NAME IS VARRAY(4) OF VARCHAR2(25);
    V_CONTENT_NAME LV_CONTENT_NAME;
    TYPE LV_SUBTEST_CODE IS VARRAY(4) OF VARCHAR2(10);
    V_SUBTEST_CODE LV_SUBTEST_CODE;
    CNT            NUMBER;
    CNT2           NUMBER;
  
    V_ASSESSMENTID_EXIST NUMBER;
    V_ASSESSMENTID       NUMBER;
    V_CONTENTID          NUMBER;
    V_SUBTESTID          NUMBER;
    V_LEVEL_MAP          NUMBER;
    V_SUBT_OBJ_MAP       NUMBER;
    V_ITEMSETID          NUMBER;
    ERR_MSG              VARCHAR2(200);
  
  BEGIN
    OUT_STATUS_FLAG_NEW := 0;
  
    --STG_PRODUCT POPULATION
  
    SELECT COUNT(1)
      INTO V_PRODUCT_EXISTS
      FROM STG_PRODUCT \*Need to change Later*\
     WHERE UPPER(PRODUCT_CODE) = UPPER(IN_PRODUCT_CODE);
  
    SELECT ADMINID, ADMIN_YEAR
      INTO V_ADMINID, V_ADMIN_YEAR
      FROM ADMIN_DIM
     WHERE IS_CURRENT_ADMIN = 'Y';
  
    IF V_PRODUCT_EXISTS = 0 THEN
    
      SELECT MAX(PRODUCTID) + 1 INTO V_PRODUCTID FROM STG_PRODUCT;
      SELECT MAX(PRODUCT_SEQ) + 1 INTO V_PRODUCTSEQ FROM STG_PRODUCT;
      INSERT INTO STG_PRODUCT
        (PRODUCTID,
         PRODUCT_NAME,
         PRODUCT_TYPE,
         PRODUCT_SEQ,
         PRODUCT_CODE,
         IS_IC_REQUIRED,
         IS_EDITABLE,
         FILE_LOCATION,
         DATETIMESTAMP)
      VALUES
        (V_PRODUCTID,
         IN_PRODUCT_NAME,
         '',
         V_PRODUCTSEQ,
         IN_PRODUCT_CODE,
         CASE WHEN IN_PRODUCT_CODE LIKE 'ISTEPS%' THEN 'Y' ELSE 'N' END,
         'N',
         '/' || V_ADMIN_YEAR || '/' || IN_PRODUCT_CODE,
         SYSDATE);
    
    ELSE
    
      SELECT PRODUCTID
        INTO V_PRODUCTID
        FROM STG_PRODUCT
       WHERE UPPER(PRODUCT_CODE) = UPPER(IN_PRODUCT_CODE);
    
      DBMS_OUTPUT.PUT_LINE('PRODUCTID ALREADY EXIST IN THE PRODUCT TABLE');
    END IF;
  
    --STG_CUST_PRODUCT_LINK POPULATION
  
    SELECT COUNT(1)
      INTO V_CUST_PROD_ID_EXISTS
      FROM STG_CUST_PRODUCT_LINK
     WHERE PRODUCTID = V_PRODUCTID;
  
    IF V_CUST_PROD_ID_EXISTS = 0 THEN
    
      SELECT MAX(CUST_PROD_ID) + 1
        INTO V_CUST_PROD_ID
        FROM STG_CUST_PRODUCT_LINK;
    
      INSERT INTO STG_CUST_PRODUCT_LINK
        (CUST_PROD_ID,
         CUSTOMERID,
         PRODUCTID,
         ADMINID,
         ACTIVATION_STATUS,
         DATETIMESTAMP)
      VALUES
        (V_CUST_PROD_ID, 1000, V_PRODUCTID, V_ADMINID, 'AC', SYSDATE);
    
    ELSE
    
      SELECT CUST_PROD_ID
        INTO V_CUST_PROD_ID
        FROM STG_CUST_PRODUCT_LINK
       WHERE PRODUCTID = V_PRODUCTID;
    
      DBMS_OUTPUT.PUT_LINE('CUST_PROD_ID ALREADY EXIST IN THE CUST_PRODUCT_LINK TABLE');
    END IF;
  
    --STG_ORG_PRODUCT_LINK POPULATION
  
    SELECT COUNT(1)
      INTO V_ORG_PROD_ID_EXISTS
      FROM STG_ORG_PRODUCT_LINK
     WHERE CUST_PROD_ID = V_CUST_PROD_ID;
  
    SELECT ORG_NODEID
      INTO V_ORG_NODEID_STATE
      FROM ORG_NODE_DIM
     WHERE ORG_NODE_LEVEL = 1
       AND CUSTOMERID = 1000;
  
    IF V_ORG_PROD_ID_EXISTS = 0 THEN
    
      INSERT INTO STG_ORG_PRODUCT_LINK
        (ORG_PROD_ID, ORG_NODEID, CUST_PROD_ID, DATETIMESTAMP)
      VALUES
        (SEQ_ORG_PRODUCT_LINK.NEXTVAL,
         V_ORG_NODEID_STATE,
         V_CUST_PROD_ID,
         SYSDATE);
    
    ELSE
    
      SELECT ORG_PROD_ID
        INTO V_ORG_PROD_ID
        FROM STG_ORG_PRODUCT_LINK
       WHERE CUST_PROD_ID = V_CUST_PROD_ID;
    
      DBMS_OUTPUT.PUT_LINE('ORGANISATION DETAILS ALREADY EXIST IN THE STG_ORG_PRODUCT_LINK TABLE');
    END IF;
  
    \*STG_TEST_PROGRAM POPULATION*\
  
    SELECT COUNT(1)
      INTO V_TP_ID_PUBLIC
      FROM STG_TEST_PROGRAM
     WHERE TP_CODE = IN_PUBLIC_ORGTP;
  
    SELECT COUNT(1)
      INTO V_TP_ID_NONPUBLIC
      FROM STG_TEST_PROGRAM
     WHERE TP_CODE = IN_NONPUBLIC_ORGTP;
  
    SELECT COUNT(1)
      INTO V_TP_ID_PUBLIC_BRAILE
      FROM STG_TEST_PROGRAM
     WHERE TP_CODE = IN_PUBLIC_BRAILE_ORGTP;
  
    SELECT COUNT(1)
      INTO V_TP_ID_NONPUBLIC_BRAILE
      FROM STG_TEST_PROGRAM
     WHERE TP_CODE = IN_NONPUBLIC_BRAILE_ORGTP;
  
    --STG_TEST_PROGRAM PUBLIC TP_CODE POPULATION
  
    IF V_TP_ID_PUBLIC = 0 AND IN_PUBLIC_ORGTP IS NOT NULL THEN
    
      SELECT MAX(TP_ID) + 1 INTO V_TP_ID_PUBLIC FROM STG_TEST_PROGRAM;
    
      INSERT INTO STG_TEST_PROGRAM
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
        (V_TP_ID_PUBLIC,
         IN_PUBLIC_ORGTP,
         IN_PRODUCT_NAME || ' Public',
         'PUBLIC',
         4,
         'PP',
         1000,
         V_ADMINID,
         'AC',
         SYSDATE);
    
    ELSIF V_TP_ID_PUBLIC <> 0 AND IN_PUBLIC_ORGTP IS NOT NULL THEN
    
      SELECT TP_ID
        INTO V_TP_ID_PUBLIC
        FROM STG_TEST_PROGRAM
       WHERE TP_CODE = IN_PUBLIC_ORGTP;
      DBMS_OUTPUT.PUT_LINE('PUBLIC_ORGTP DETAILS ALREADY EXIST IN THE STG_TEST_PROGRAM TABLE');
    
    END IF;
  
    --STG_TEST_PROGRAM NONPUBLIC TP_CODE POPULATION
  
    IF V_TP_ID_NONPUBLIC = 0 AND IN_NONPUBLIC_ORGTP IS NOT NULL THEN
    
      SELECT MAX(TP_ID) + 1 INTO V_TP_ID_NONPUBLIC FROM STG_TEST_PROGRAM;
    
      INSERT INTO STG_TEST_PROGRAM
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
        (V_TP_ID_NONPUBLIC,
         IN_NONPUBLIC_ORGTP,
         IN_PRODUCT_NAME || ' Non-Public',
         'NON-PUBLIC',
         4,
         'PP',
         1000,
         V_ADMINID,
         'AC',
         SYSDATE);
    
    ELSIF V_TP_ID_NONPUBLIC <> 0 AND IN_NONPUBLIC_ORGTP IS NOT NULL THEN
    
      SELECT TP_ID
        INTO V_TP_ID_NONPUBLIC
        FROM STG_TEST_PROGRAM
       WHERE TP_CODE = IN_NONPUBLIC_ORGTP;
    
      DBMS_OUTPUT.PUT_LINE('NONPUBLIC_ORGTP DETAILS ALREADY EXIST IN THE STG_TEST_PROGRAM TABLE');
    
    END IF;
  
    --STG_TEST_PROGRAM PUBLIC BRAILE TP_CODE POPULATION
  
    IF V_TP_ID_PUBLIC_BRAILE = 0 AND IN_PUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      SELECT MAX(TP_ID) + 1
        INTO V_TP_ID_PUBLIC_BRAILE
        FROM STG_TEST_PROGRAM;
    
      INSERT INTO STG_TEST_PROGRAM
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
        (V_TP_ID_PUBLIC_BRAILE,
         IN_PUBLIC_BRAILE_ORGTP,
         IN_PRODUCT_NAME || ' Public_Braile',
         'PUBLIC',
         4,
         'PP',
         1000,
         V_ADMINID,
         'AC',
         SYSDATE);
    
    ELSIF V_TP_ID_PUBLIC_BRAILE <> 0 AND IN_PUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      SELECT TP_ID
        INTO V_TP_ID_PUBLIC_BRAILE
        FROM STG_TEST_PROGRAM
       WHERE TP_CODE = IN_PUBLIC_BRAILE_ORGTP;
    
      DBMS_OUTPUT.PUT_LINE('PUBLIC_BRAILE_ORGTP DETAILS ALREADY EXIST IN THE STG_TEST_PROGRAM TABLE');
    
    END IF;
  
    --STG_TEST_PROGRAM NONPUBLIC BRAILE TP_CODE POPULATION
  
    IF V_TP_ID_NONPUBLIC_BRAILE = 0 AND
       IN_NONPUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      SELECT MAX(TP_ID) + 1
        INTO V_TP_ID_NONPUBLIC_BRAILE
        FROM STG_TEST_PROGRAM;
    
      INSERT INTO STG_TEST_PROGRAM
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
        (V_TP_ID_NONPUBLIC_BRAILE,
         IN_NONPUBLIC_BRAILE_ORGTP,
         IN_PRODUCT_NAME || ' Non-Public_Braile',
         'NON-PUBLIC',
         4,
         'PP',
         1000,
         V_ADMINID,
         'AC',
         SYSDATE);
    
    ELSIF V_TP_ID_NONPUBLIC_BRAILE <> 0 AND
          IN_NONPUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      SELECT TP_ID
        INTO V_TP_ID_NONPUBLIC_BRAILE
        FROM STG_TEST_PROGRAM
       WHERE TP_CODE = IN_NONPUBLIC_BRAILE_ORGTP;
    
      DBMS_OUTPUT.PUT_LINE('NONPUBLIC_BRAILE ORGTP DETAILS ALREADY EXIST IN THE STG_TEST_PROGRAM TABLE');
    
    END IF;
  
    -- STG_ORG_TP_STRUCTURE POPULATION
  
    V_ORG_LABEL := LV_ORG_LABEL('State', 'Corporation', 'School', 'Class');
    CNT         := V_ORG_LABEL.COUNT;
  
    SELECT COUNT(1)
      INTO V_TP_ID_P_EXISTS
      FROM STG_ORG_TP_STRUCTURE
     WHERE TP_ID = V_TP_ID_PUBLIC;
  
    SELECT COUNT(1)
      INTO V_TP_ID_NP_EXISTS
      FROM STG_ORG_TP_STRUCTURE
     WHERE TP_ID = V_TP_ID_NONPUBLIC;
  
    SELECT COUNT(1)
      INTO V_TP_ID_P_BRAILE_EXISTS
      FROM STG_ORG_TP_STRUCTURE
     WHERE TP_ID = V_TP_ID_PUBLIC_BRAILE;
  
    SELECT COUNT(1)
      INTO V_TP_ID_NP_BRAILE_EXISTS
      FROM STG_ORG_TP_STRUCTURE
     WHERE TP_ID = V_TP_ID_NONPUBLIC_BRAILE;
  
    -- PUBLIC TP_CODE
  
    IF V_TP_ID_P_EXISTS = 0 AND IN_PUBLIC_ORGTP IS NOT NULL THEN
    
      FOR I IN 1 .. CNT LOOP
      
        INSERT INTO STG_ORG_TP_STRUCTURE
          (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
        VALUES
          (V_TP_ID_PUBLIC, I, V_ORG_LABEL(I), SYSDATE);
      
      END LOOP;
    
    ELSIF V_TP_ID_P_EXISTS <> 0 AND IN_PUBLIC_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('PUBLIC TP_ID ALREADY EXIST IN THE STG_ORG_TP_STRUCTURE TABLE');
    
    END IF;
  
    --NONPUBLIC TP_CODE
  
    IF V_TP_ID_NP_EXISTS = 0 AND IN_NONPUBLIC_ORGTP IS NOT NULL THEN
    
      FOR I IN 1 .. CNT LOOP
      
        INSERT INTO STG_ORG_TP_STRUCTURE
          (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
        VALUES
          (V_TP_ID_NONPUBLIC, I, V_ORG_LABEL(I), SYSDATE);
      
      END LOOP;
    
    ELSIF V_TP_ID_NP_EXISTS <> 0 AND IN_NONPUBLIC_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('NONPUBLIC TP_ID ALREADY EXIST IN THE STG_ORG_TP_STRUCTURE TABLE');
    
    END IF;
  
    --PUBLIC BRAILE TP_CODE
  
    IF V_TP_ID_P_BRAILE_EXISTS = 0 AND IN_PUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      FOR I IN 1 .. CNT LOOP
      
        INSERT INTO STG_ORG_TP_STRUCTURE
          (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
        VALUES
          (V_TP_ID_PUBLIC_BRAILE, I, V_ORG_LABEL(I), SYSDATE);
      
      END LOOP;
    
    ELSIF V_TP_ID_P_BRAILE_EXISTS <> 0 AND
          IN_PUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('PUBLIC BRAILE_TP_ID ALREADY EXIST IN THE STG_ORG_TP_STRUCTURE TABLE');
    
    END IF;
  
    --NONPUBLIC BRAILE TP_CODE
  
    IF V_TP_ID_NP_BRAILE_EXISTS = 0 AND
       IN_NONPUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      FOR I IN 1 .. CNT LOOP
      
        INSERT INTO STG_ORG_TP_STRUCTURE
          (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
        VALUES
          (V_TP_ID_NONPUBLIC_BRAILE, I, V_ORG_LABEL(I), SYSDATE);
      
      END LOOP;
    
    ELSIF V_TP_ID_NP_BRAILE_EXISTS <> 0 AND
          IN_NONPUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('NONPUBLIC BRAILE_TP_ID ALREADY EXIST IN THE STG_ORG_TP_STRUCTURE TABLE');
    
    END IF;
  
    --STG_ORG_TEST_PROGRAM_LINK Population
  
    SELECT COUNT(1)
      INTO V_TP_ID_P_OTPL
      FROM STG_ORG_TEST_PROGRAM_LINK
     WHERE TP_ID = V_TP_ID_PUBLIC;
  
    SELECT COUNT(1)
      INTO V_TP_ID_NP_OTPL
      FROM STG_ORG_TEST_PROGRAM_LINK
     WHERE TP_ID = V_TP_ID_NONPUBLIC;
  
    SELECT COUNT(1)
      INTO V_TP_ID_P_BRAILE_OTPL
      FROM STG_ORG_TEST_PROGRAM_LINK
     WHERE TP_ID = V_TP_ID_PUBLIC_BRAILE;
  
    SELECT COUNT(1)
      INTO V_TP_ID_NP_BRAILE_OTPL
      FROM STG_ORG_TEST_PROGRAM_LINK
     WHERE TP_ID = V_TP_ID_NONPUBLIC_BRAILE;
  
    --PUBLIC TP_ID
  
    IF V_TP_ID_P_OTPL = 0 AND IN_PUBLIC_ORGTP IS NOT NULL THEN
    
      INSERT INTO STG_ORG_TEST_PROGRAM_LINK
        (ORG_NODEID, TP_ID, DATETIMESTAMP)
      VALUES
        (V_ORG_NODEID_STATE, V_TP_ID_PUBLIC, SYSDATE);
    
    ELSIF V_TP_ID_P_OTPL <> 0 AND IN_PUBLIC_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('PUBLIC TP_ID ALREADY EXIST IN THE ORG_TEST_PROGRAM_LINK TABLE');
    
    END IF;
  
    --NONPUBLIC TP_ID
  
    IF V_TP_ID_NP_OTPL = 0 AND IN_NONPUBLIC_ORGTP IS NOT NULL THEN
    
      INSERT INTO STG_ORG_TEST_PROGRAM_LINK
        (ORG_NODEID, TP_ID, DATETIMESTAMP)
      VALUES
        (V_ORG_NODEID_STATE, V_TP_ID_NONPUBLIC, SYSDATE);
    
    ELSIF V_TP_ID_NP_OTPL <> 0 AND IN_NONPUBLIC_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('NONPUBLIC TP_ID ALREADY EXIST IN THE ORG_TEST_PROGRAM_LINK TABLE');
    
    END IF;
  
    --PUBLIC BRAILE TP_ID
  
    IF V_TP_ID_P_BRAILE_OTPL = 0 AND IN_PUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      INSERT INTO STG_ORG_TEST_PROGRAM_LINK
        (ORG_NODEID, TP_ID, DATETIMESTAMP)
      VALUES
        (V_ORG_NODEID_STATE, V_TP_ID_PUBLIC_BRAILE, SYSDATE);
    
    ELSIF V_TP_ID_P_BRAILE_OTPL <> 0 AND IN_PUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('PUBLIC BRAILE_TP_ID ALREADY EXIST IN THE ORG_TEST_PROGRAM_LINK TABLE');
    
    END IF;
  
    --NONPUBLIC BRAILE TP_ID
  
    IF V_TP_ID_NP_BRAILE_OTPL = 0 AND IN_NONPUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      INSERT INTO STG_ORG_TEST_PROGRAM_LINK
        (ORG_NODEID, TP_ID, DATETIMESTAMP)
      VALUES
        (V_ORG_NODEID_STATE, V_TP_ID_NONPUBLIC_BRAILE, SYSDATE);
    
    ELSIF V_TP_ID_NP_BRAILE_OTPL <> 0 AND
          IN_NONPUBLIC_BRAILE_ORGTP IS NOT NULL THEN
    
      DBMS_OUTPUT.PUT_LINE('NONPUBLIC BRAILE_TP_ID ALREADY EXIST IN THE ORG_TEST_PROGRAM_LINK TABLE');
    
    END IF;
  
    --STG_ASSESSMENT_DIM Population
  
    SELECT COUNT(1)
      INTO V_ASSESSMENTID_EXIST
      FROM STG_ASSESSMENT_DIM
     WHERE PRODUCTID = V_PRODUCTID;
  
    IF V_ASSESSMENTID_EXIST = 0 THEN
    
      SELECT MAX(ASSESSMENTID) + 1
        INTO V_ASSESSMENTID
        FROM STG_ASSESSMENT_DIM;
    
      INSERT INTO STG_ASSESSMENT_DIM
        (ASSESSMENTID,
         ASSESSMENT_NAME,
         ASSESSMENT_TYPE,
         ASSESSMENT_CODE,
         PRODUCTID,
         DATETIMESTAMP)
      VALUES
        (V_ASSESSMENTID, IN_PRODUCT_NAME, ' ', ' ', V_PRODUCTID, SYSDATE);
    
    ELSE
    
      SELECT ASSESSMENTID
        INTO V_ASSESSMENTID
        FROM STG_ASSESSMENT_DIM
       WHERE PRODUCTID = V_PRODUCTID;
    
      DBMS_OUTPUT.PUT_LINE('ASSESSMENT DETAILS ALREADY EXIST FOR THIS ' ||
                           V_PRODUCTID ||
                           ' IN THE STG_ASSESSMENT_DIM TABLE');
    END IF;
  
    --STG_CONTENT_DIM and STG_SUBTEST_DIM Population
  
    SELECT COUNT(1)
      INTO V_CONTENTID
      FROM STG_CONTENT_DIM
     WHERE ASSESSMENTID = V_ASSESSMENTID;
  
    IF V_CONTENTID = 0 THEN
    
      V_CONTENT_NAME := LV_CONTENT_NAME('English/Language Arts',
                                        'Mathematics',
                                        'Science',
                                        'Social Studies');
      CNT            := V_CONTENT_NAME.COUNT;
    
      V_SUBTEST_CODE := LV_SUBTEST_CODE('ELA', 'MATH', 'SCI', 'SS');
      CNT2           := V_SUBTEST_CODE.COUNT;
    
      IF ((IN_PRODUCT_NAME LIKE 'ISTEP%') OR
         (IN_PRODUCT_NAME LIKE 'IMAST%')) THEN
      
        FOR I IN 1 .. CNT LOOP
        
          SELECT MAX(CONTENTID) + 1 INTO V_CONTENTID FROM STG_CONTENT_DIM;
        
          INSERT INTO STG_CONTENT_DIM
            (CONTENTID,
             CONTENT_NAME,
             CONTENT_SEQ,
             ASSESSMENTID,
             DATETIMESTAMP)
          VALUES
            (V_CONTENTID, V_CONTENT_NAME(I), I, V_ASSESSMENTID, SYSDATE);
        
          SELECT COUNT(1)
            INTO V_SUBTESTID
            FROM STG_SUBTEST_DIM
           WHERE CONTENTID = V_CONTENTID;
        
          IF V_SUBTESTID = 0 THEN
          
            SELECT MAX(SUBTESTID) + 1
              INTO V_SUBTESTID
              FROM STG_SUBTEST_DIM;
          
            INSERT INTO STG_SUBTEST_DIM
              (SUBTESTID,
               SUBTEST_NAME,
               SUBTEST_SEQ,
               SUBTEST_CODE,
               SUBTEST_TYPE,
               CONTENTID,
               DATETIMESTAMP)
            VALUES
              (V_SUBTESTID,
               V_CONTENT_NAME(I),
               I,
               V_SUBTEST_CODE(I),
               'S',
               V_CONTENTID,
               SYSDATE);
          
          ELSE
          
            DBMS_OUTPUT.PUT_LINE('SUBTEST DETAILS ALREADY EXIST FOR THIS ' ||
                                 V_CONTENTID ||
                                 ' IN THE STG_SUBTEST_DIM TABLE');
          
          END IF;
        
        END LOOP;
      
      ELSIF IN_PRODUCT_NAME LIKE 'IREAD%' THEN
      
        SELECT MAX(CONTENTID) + 1 INTO V_CONTENTID FROM STG_CONTENT_DIM;
      
        INSERT INTO STG_CONTENT_DIM
          (CONTENTID,
           CONTENT_NAME,
           CONTENT_SEQ,
           ASSESSMENTID,
           DATETIMESTAMP)
        VALUES
          (V_CONTENTID, 'Reading', 1, V_ASSESSMENTID, SYSDATE);
      
        SELECT COUNT(1)
          INTO V_SUBTESTID
          FROM STG_SUBTEST_DIM
         WHERE CONTENTID = V_CONTENTID;
      
        IF V_SUBTESTID = 0 THEN
        
          SELECT MAX(SUBTESTID) + 1 INTO V_SUBTESTID FROM STG_SUBTEST_DIM;
        
          INSERT INTO STG_SUBTEST_DIM
            (SUBTESTID,
             SUBTEST_NAME,
             SUBTEST_SEQ,
             SUBTEST_CODE,
             SUBTEST_TYPE,
             CONTENTID,
             DATETIMESTAMP)
          VALUES
            (V_SUBTESTID, 'Reading', 1, 'READ', 'S', V_CONTENTID, SYSDATE);
        
        ELSE
        
          DBMS_OUTPUT.PUT_LINE('SUBTEST DETAILS ALREADY EXIST FOR THIS ' ||
                               V_CONTENTID ||
                               ' IN THE STG_SUBTEST_DIM TABLE');
        
        END IF;
      
      ELSE
      
        DBMS_OUTPUT.PUT_LINE('PRODUCT DETAILS DOES NOT MATCH');
      
      END IF;
    
    ELSE
      DBMS_OUTPUT.PUT_LINE('CONTENT DETAILS ALREADY EXIST FOR THIS ' ||
                           V_ASSESSMENTID ||
                           ' IN THE STG_CONTENT_DIM TABLE');
    END IF;
  
    --STG_LEVEL_MAP AND STG_GRADE_LEVEL_MAP Population
  
    SELECT COUNT(1)
      INTO V_LEVEL_MAP
      FROM STG_LEVEL_MAP
     WHERE ASSESSMENTID = V_ASSESSMENTID;
  
    IF V_LEVEL_MAP = 0 THEN
    
      IF ((IN_PRODUCT_NAME LIKE 'ISTEP%') OR
         (IN_PRODUCT_NAME LIKE 'IMAST%')) THEN
      
        SELECT COUNT(1) + 12 INTO CNT FROM LEVEL_DIM;
      
        FOR I IN 13 .. CNT LOOP
        
          CNT2 := I - 10;
        
          SELECT MAX(LEVEL_MAPID) + 1 INTO V_LEVEL_MAP FROM STG_LEVEL_MAP;
        
          INSERT INTO STG_LEVEL_MAP
            (LEVEL_MAPID, LEVELID, FORMID, ASSESSMENTID, DATETIMESTAMP)
          VALUES
            (V_LEVEL_MAP,
             (SELECT LEVELID FROM LEVEL_DIM WHERE LEVEL_CODE = I),
             30001,
             V_ASSESSMENTID,
             SYSDATE);
        
          INSERT INTO STG_GRADE_LEVEL_MAP
            (GRADEID, LEVEL_MAPID, ONLEVEL_FLAG, DATETIMESTAMP)
          VALUES
            ((SELECT GRADEID
               FROM GRADE_DIM
              WHERE GRADE_CODE = '0' || to_char(CNT2)),
             V_LEVEL_MAP,
             'Y',
             SYSDATE);
        
        END LOOP;
      
      ELSIF IN_PRODUCT_NAME LIKE 'IREAD%' THEN
      
        SELECT MAX(LEVEL_MAPID) + 1 INTO V_LEVEL_MAP FROM STG_LEVEL_MAP;
      
        INSERT INTO STG_LEVEL_MAP
          (LEVEL_MAPID, LEVELID, FORMID, ASSESSMENTID, DATETIMESTAMP)
        VALUES
          (V_LEVEL_MAP, 20001, 30001, V_ASSESSMENTID, SYSDATE);
      
        INSERT INTO STG_GRADE_LEVEL_MAP
          (GRADEID, LEVEL_MAPID, ONLEVEL_FLAG, DATETIMESTAMP)
        VALUES
          (10001, V_LEVEL_MAP, 'Y', SYSDATE);
      
      ELSE
        DBMS_OUTPUT.PUT_LINE('PRODUCT DETAILS DOES NOT MATCH');
      END IF;
    
    ELSE
    
      DBMS_OUTPUT.PUT_LINE('LEVEL_MAPID IS ALREADY EXIST FOR THE ASSESSMENTID ' ||
                           V_ASSESSMENTID || ' IN THE STG_LEVEL_MAP');
    
    END IF;
  
    SELECT COUNT(1)
      INTO LV_VALIDATION
      FROM ASSESSMENT_DIM  ASM,
           LEVEL_DIM       LVL,
           FORM_DIM        FM,
           GRADE_DIM       GD,
           LEVEL_MAP       LMAP,
           GRADE_LEVEL_MAP GLMAP
     WHERE LMAP.LEVELID = LVL.LEVELID
       AND LMAP.FORMID = FM.FORMID
       AND LMAP.ASSESSMENTID = ASM.ASSESSMENTID
       AND GLMAP.GRADEID = GD.GRADEID
       AND GLMAP.LEVEL_MAPID = LMAP.LEVEL_MAPID
       AND ASM.ASSESSMENTID = V_ASSESSMENTID;
  
    IF LV_VALIDATION = 0 THEN
    
      RAISE_APPLICATION_ERROR(-20101,
                              'NO RECORDS FETCHED IN VALIDATION QUERY');
    
    END IF;
  
    --POPULATE STG_SUBTEST_OBJECTIVE_MAP
  
    SELECT COUNT(1)
      INTO V_SUBT_OBJ_MAP
      FROM STG_SUBTEST_OBJECTIVE_MAP
     WHERE ASSESSMENTID = V_ASSESSMENTID;
  
    IF V_SUBT_OBJ_MAP = 0 THEN
    
      EXECUTE IMMEDIATE 'INSERT INTO STG_SUBTEST_OBJECTIVE_MAP
      
        (SUBT_OBJ_MAPID,
         SUBTESTID,
         OBJECTIVEID,
         LEVEL_MAPID,
         ASSESSMENTID,
         DATETIMESTAMP)
      
        SELECT (SELECT MAX(SUBT_OBJ_MAPID) FROM SUBTEST_OBJECTIVE_MAP) +
               ROWNUM AS SUBT_OBJ_MAPID,
               SD.SUBTESTID,
               OBJ.OBJECTIVEID,
               GLM.LEVEL_MAPID,
               CD.ASSESSMENTID,
               SYSDATE
        
          FROM STG_OBJECTIVE_DIM OBJ,
               (select distinct --obj.ctb_project_id,
                                obj.ctb_objective_id,
                                obj.ctb_objective_code,
                                substr(cont.ctb_content_area_code,
                                       length(cont.ctb_content_area_code) - 1,
                                       2),
                                replace(obj.ctb_objective_title,
                                        ''*'',
                                        chr(134)) ctb_objective_title,
                                -- obj.ctb_content_id,
                                cont.ctb_content_area_code as content_grade,
                                (SELECT CASE
                                          WHEN ''' ||
                        IN_PRODUCT_CODE ||
                        ''' NOT LIKE
                                               ''IREADS%'' AND
                                               ISTEP_SUBTEST_CODE = ''ELA'' THEN
                                           ''ELA''
                                          WHEN ''' ||
                        IN_PRODUCT_CODE ||
                        ''' LIKE ''IREADS%'' AND
                                               ISTEP_SUBTEST_CODE = ''ELA'' THEN
                                           ''READ''
                                          ELSE
                                           ISTEP_SUBTEST_CODE
                                        END ISTEP_SUBTEST_CODE
                                   FROM CTB_CONTENT_SUBTEST_MAP A               
                                  WHERE A.ctb_content_area_TITLE =
                                        cont.ctb_content_area_title) ctb_content_area_title
                  from delivery.ctb_objective   obj,
                       delivery.ctb_content     cont,
                       delivery.ctb_itemobj     iobj,
                       delivery.ctb_module_item mi,
                       delivery.ctb_book        bk
                 where TRIM(obj.ctb_project_id) IN (' ||
                        IN_CTB_PROJECTID || ')
                   and cont.ctb_project_id = obj.ctb_project_id
                   and cont.ctb_content_id = obj.ctb_content_id
                   and bk.ctb_project_id = obj.ctb_project_id
                   and mi.ctb_module_id = bk.ctb_module_id
                   and obj.ctb_objective_id = iobj.ctb_objective_id
                   and mi.ctb_item_id = iobj.ctb_item_id) obj2,
               STG_SUBTEST_DIM SD,
               STG_CONTENT_DIM CD,
               CTB_CONTENT_LEVEL_MAP CLM,
               STG_LEVEL_MAP LM,
               STG_GRADE_LEVEL_MAP GLM
         WHERE TRIM(OBJ.OBJECTIVE_CODE) = TRIM(obj2.ctb_objective_code)
           AND TRIM(OBJ.OBJECTIVE_NAME) = TRIM(obj2.ctb_objective_title)
           AND SD.SUBTEST_CODE = OBJ2.ctb_content_area_title
           AND CD.CONTENTID = SD.CONTENTID
           AND CD.ASSESSMENTID = ' || V_ASSESSMENTID || '
           AND TRIM(CLM.CONTENT_GRADE_CODE) = TRIM(obj2.content_grade)
           AND CLM.LEVEL_ID = LM.LEVELID
           AND LM.LEVEL_MAPID = GLM.LEVEL_MAPID
           and LM.ASSESSMENTID = CD.ASSESSMENTID';
    
      IF SQL%ROWCOUNT = 0 then
        RAISE_APPLICATION_ERROR(-20101,
                                'NO RECORDS INSERTED IN STG_SUBTEST_OBJECTIVE_MAP TABLE');
      END IF;
    
    ELSE
      DBMS_OUTPUT.PUT_LINE('SUBTEST_OBJECTIVE DEATILS ARE ALREADY EXIST FOR THE ASSESSMENTID ' ||
                           V_ASSESSMENTID ||
                           ' IN THE STG_SUBTEST_OBJECTIVE_MAP');
    
    END IF;
  
    IF (IN_PRODUCT_NAME LIKE 'ISTEP%') THEN
    
      --STG_ITEMSET_DIM 'CR' Records Population
    
      SELECT COUNT(1)
        INTO V_ITEMSETID
        FROM STG_ITEMSET_DIM
       WHERE SUBT_OBJ_MAPID IN
             (SELECT SUBT_OBJ_MAPID
                FROM STG_SUBTEST_OBJECTIVE_MAP
               WHERE ASSESSMENTID = V_ASSESSMENTID)
         AND ITEM_TYPE = 'CR';
    
      IF V_ITEMSETID = 0 THEN
      
        INSERT INTO STG_ITEMSET_DIM
          (ITEMSETID,
           ITEM_NAME,
           ITEM_CODE,
           SESSION_ID,
           ITEM_SEQ,
           POINT_POSSIBLE,
           ITEM_TYPE,
           PDF_FILENAME,
           SUBT_OBJ_MAPID,
           DATETIMESTAMP,
           ITEM_NUMBER,
           ITEM_PART,
           MODEULEID)
        
          SELECT ITEMSETID,
                 ITEM_NAME,
                 ITEM_CODE,
                 SESSION_ID,
                 ITEM_SEQ,
                 POINTS_POSSIBLE,
                 ITEM_TYPE,
                 PDF_FILENAME,
                 SUBT_OBJ_MAPID,
                 DATETIMESTAMP,
                 ITEM_NUMBER,
                 ITEM_PART,
                 MODEULEID
            FROM (SELECT (SELECT MAX(ITEMSETID) FROM ITEMSET_DIM) + ROWNUM AS ITEMSETID,
                         T.*
                    FROM (SELECT DISTINCT CTB_ITEM_NO || CTB_PART AS ITEM_NAME,
                                          IO.CTB_ITEM_ID AS ITEM_CODE,
                                          --SUBSTR(CTB_MODULE_TITLE, 9, 1) AS SESSION_ID,
                                          REGEXP_REPLACE(CTB_MODULE_TITLE,
                                                         '[A-Za-z:/]') AS SESSION_ID,
                                          DENSE_RANK() OVER(PARTITION BY C.CTB_CONTENT_ID ORDER BY BL.CTB_LEVEL_ID, BL.CTB_MODULE_NAME, B.CTB_MODULE_SEQ_NO, MI.CTB_ITEM_NO, MI.CTB_PART NULLS LAST) AS ITEM_SEQ,
                                          CTB_NO_SCORE_POINTS AS POINTS_POSSIBLE,
                                          'CR' AS ITEM_TYPE,
                                          NULL AS PDF_FILENAME,
                                          CTB_ITEM_NO AS ITEM_NUMBER,
                                          CTB_PART AS ITEM_PART,
                                          MI.CTB_MODULE_ID AS MODEULEID,
                                          SYSDATE AS DATETIMESTAMP,
                                          C.CTB_CONTENT_ID,
                                          MI.CTB_SUBTEST_ID,
                                          IO.CTB_OBJECTIVE_ID,
                                          C.CTB_CONTENT_AREA_CODE
                          
                            FROM DELIVERY.CTB_PUBLICATION  PUB,
                                 DELIVERY.CTB_BOOKLIST     BL,
                                 DELIVERY.CTB_CONTENT      C,
                                 DELIVERY.CTB_MODULE_ITEM  MI,
                                 DELIVERY.CTB_ITEM         I,
                                 DELIVERY.CTB_PAGE_ELEMENT PE,
                                 DELIVERY.CTB_BOOK         B,
                                 DELIVERY.CTB_ITEMOBJ      IO,
                                 DELIVERY.CTB_OBJECTIVE    OBJ,
                                 DELIVERY.CTB_SUBTESTS     S
                           WHERE PUB.CTB_PROJECT_ID = IN_CTB_PROJECTID_APS
                             AND PUB.CTB_PROJECT_ID = BL.CTB_PROJECT_ID
                             AND BL.CTB_PROJECT_ID = C.CTB_PROJECT_ID
                             AND C.CTB_CONTENT_ID = MI.CTB_CONTENT_ID
                             AND BL.CTB_MODULE_ID = MI.CTB_MODULE_ID
                             AND MI.CTB_ITEM_ID = I.CTB_ITEM_ID
                             AND MI.CTB_ELEMENT_ID = PE.CTB_ELEMENT_ID
                             AND PE.CTB_MODULE_ID = MI.CTB_MODULE_ID
                             AND B.CTB_PROJECT_ID = PUB.CTB_PROJECT_ID
                             AND B.CTB_MODULE_ID = MI.CTB_MODULE_ID
                             AND MI.CTB_ITEM_ID = IO.CTB_ITEM_ID
                             AND IO.CTB_OBJECTIVE_ID = OBJ.CTB_OBJECTIVE_ID
                             AND TRIM(IO.CTB_PRIMARY_OBJ) = 'T'
                             AND C.CTB_CONTENT_ID = OBJ.CTB_CONTENT_ID
                             AND PUB.CTB_PROJECT_ID = S.CTB_PROJECT_ID
                             AND MI.CTB_SUBTEST_ID = S.CTB_SUBTEST_ID) T) T1,
                 (SELECT CTB_OBJECTIVE_ID, CTB_CONTENT_ID, SUBT_OBJ_MAPID
                    FROM STG_OBJECTIVE_DIM OBJ,
                         (SELECT DISTINCT OBJ.CTB_OBJECTIVE_ID,
                                          OBJ.CTB_OBJECTIVE_CODE,
                                          SUBSTR(CONT.CTB_CONTENT_AREA_CODE,
                                                 LENGTH(CONT.CTB_CONTENT_AREA_CODE) - 1,
                                                 2),
                                          REPLACE(OBJ.CTB_OBJECTIVE_TITLE,
                                                  '*',
                                                  CHR(134)) CTB_OBJECTIVE_TITLE,
                                          OBJ.CTB_CONTENT_ID,
                                          CONT.CTB_CONTENT_AREA_CODE AS CONTENT_GRADE,
                                          (SELECT CASE
                                                    WHEN 'ISTEPS14' NOT LIKE
                                                         'IREADS%' AND ISTEP_SUBTEST_CODE =
                                                         'ELA' THEN
                                                     'ELA'
                                                    WHEN 'ISTEPS14' LIKE
                                                         'IREADS%' AND ISTEP_SUBTEST_CODE =
                                                         'ELA' THEN
                                                     'READ'
                                                    ELSE
                                                     ISTEP_SUBTEST_CODE
                                                  END ISTEP_SUBTEST_CODE
                                             FROM CTB_CONTENT_SUBTEST_MAP A
                                            WHERE A.CTB_CONTENT_AREA_TITLE =
                                                  CONT.CTB_CONTENT_AREA_TITLE) CTB_CONTENT_AREA_TITLE
                            FROM DELIVERY.CTB_OBJECTIVE   OBJ,
                                 DELIVERY.CTB_CONTENT     CONT,
                                 DELIVERY.CTB_ITEMOBJ     IOBJ,
                                 DELIVERY.CTB_MODULE_ITEM MI,
                                 DELIVERY.CTB_BOOK        BK
                           WHERE TRIM(OBJ.CTB_PROJECT_ID) =
                                 IN_CTB_PROJECTID_APS
                             AND CONT.CTB_PROJECT_ID = OBJ.CTB_PROJECT_ID
                             AND CONT.CTB_CONTENT_ID = OBJ.CTB_CONTENT_ID
                             AND BK.CTB_PROJECT_ID = OBJ.CTB_PROJECT_ID
                             AND MI.CTB_MODULE_ID = BK.CTB_MODULE_ID
                             AND OBJ.CTB_OBJECTIVE_ID = IOBJ.CTB_OBJECTIVE_ID
                             AND MI.CTB_ITEM_ID = IOBJ.CTB_ITEM_ID) OBJ2,
                         STG_SUBTEST_DIM SD,
                         STG_CONTENT_DIM CD,
                         CTB_CONTENT_LEVEL_MAP CLM,
                         STG_LEVEL_MAP LM,
                         STG_GRADE_LEVEL_MAP GLM,
                         STG_SUBTEST_OBJECTIVE_MAP SUB
                   WHERE TRIM(OBJ.OBJECTIVE_CODE) =
                         TRIM(OBJ2.CTB_OBJECTIVE_CODE)
                     AND TRIM(OBJ.OBJECTIVE_NAME) =
                         TRIM(OBJ2.CTB_OBJECTIVE_TITLE)
                     AND SD.SUBTEST_CODE = OBJ2.CTB_CONTENT_AREA_TITLE
                     AND CD.CONTENTID = SD.CONTENTID
                     AND CD.ASSESSMENTID = V_ASSESSMENTID
                     AND TRIM(CLM.CONTENT_GRADE_CODE) =
                         TRIM(OBJ2.CONTENT_GRADE)
                     AND CLM.LEVEL_ID = LM.LEVELID
                     AND LM.LEVEL_MAPID = GLM.LEVEL_MAPID
                     AND LM.ASSESSMENTID = CD.ASSESSMENTID
                     AND SUB.SUBTESTID = SD.SUBTESTID
                     AND SUB.OBJECTIVEID = OBJ.OBJECTIVEID
                     AND SUB.LEVEL_MAPID = GLM.LEVEL_MAPID
                     AND SUB.ASSESSMENTID = CD.ASSESSMENTID) T2
           WHERE T1.CTB_CONTENT_ID = T2.CTB_CONTENT_ID
             AND T1.CTB_OBJECTIVE_ID = T2.CTB_OBJECTIVE_ID;
      
        IF SQL%ROWCOUNT = 0 then
          RAISE_APPLICATION_ERROR(-20101,
                                  'NO RECORDS INSERTED IN STG_ITEMSET_DIM TABLE FOR CR RECORDS');
        END IF;
      
      ELSE
        DBMS_OUTPUT.PUT_LINE('ITEMSET DEATILS ARE ALREADY EXIST FOR THE ASSESSMENTID ' ||
                             V_ASSESSMENTID ||
                             ' IN THE ITEMSET_DIM FOR CR RECORDS');
      
      END IF;
      
      
      --STG_ITEMSET_DIM 'OBJ' Records Population
      
      \***************************************************************
      
      
      
      
      
      
      
      ***************************************************************\     
      
      
    
    END IF;
  
    OUT_STATUS_FLAG_NEW := 1;
    DBMS_OUTPUT.PUT_LINE('INSERTION STATUS =' || OUT_STATUS_FLAG_NEW);
  
    COMMIT;
  
  EXCEPTION
    WHEN OTHERS THEN
    
      ERR_MSG := SUBSTR(SQLERRM, 1, 200);
      DBMS_OUTPUT.PUT_LINE('EXCEPTION: ' || ERR_MSG);
      ROLLBACK;
    
  END SP_INITIAL_PRODUCT_CONFIG;*/

END PKG_INORS_CREATE_CUSTOMER;
/
