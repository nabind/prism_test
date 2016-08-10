CREATE OR REPLACE PROCEDURE PRC_CONFIGURATION_USMO(P_OUT_STATUS_NUMBER OUT NUMBER,
                                                   P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  V_DB_MANAGE_MENUID       DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE;
  V_DB_REPORTS_MENUID      DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE;
  V_DB_DOWNLOADS_MENUID    DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE;
  V_DB_RESOURCES_MENUID    DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE;
  V_DB_UL_MENUID           DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE;
  V_CUST_PROD_ID           CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE := 0;
  V_ROLEID_ADMIN           ROLE.ROLEID%TYPE;
  V_ROLEID_SUPER           ROLE.ROLEID%TYPE;
  V_ROLEID_CTB             ROLE.ROLEID%TYPE;
  V_ROLEID_USER            ROLE.ROLEID%TYPE;
  V_DMRA_ACTIVATION_STATUS DASH_MENU_RPT_ACCESS.ACTIVATION_STATUS%TYPE;
  V_MIN_ORG_LEVEL          ORG_TP_STRUCTURE.ORG_LEVEL%TYPE := 1;
  V_ACTION_TYPE            DASH_RPT_ACTION.ACTION_TYPE%TYPE;
  V_DAA_ACTIVATION_STATUS  DASH_ACTION_ACCESS.ACTIVATION_STATUS%TYPE;
  V_PROJECTID              PROJECT_DIM.PROJECTID%TYPE;

BEGIN
  P_OUT_STATUS_NUMBER := 0;

  SELECT PROJECTID
    INTO V_PROJECTID
    FROM PROJECT_DIM
   WHERE UPPER(PROJECT_NAME) LIKE 'MISSOURI%';

  SELECT DB_MENUID
    INTO V_DB_MANAGE_MENUID
    FROM DASH_MENUS
   WHERE UPPER(MENU_NAME) = UPPER('MANAGE')
     AND PROJECTID = V_PROJECTID;

  SELECT DB_MENUID
    INTO V_DB_REPORTS_MENUID
    FROM DASH_MENUS
   WHERE UPPER(MENU_NAME) = UPPER('REPORTS')
     AND PROJECTID = V_PROJECTID;

  SELECT DB_MENUID
    INTO V_DB_DOWNLOADS_MENUID
    FROM DASH_MENUS
   WHERE UPPER(MENU_NAME) = UPPER('DOWNLOADS')
     AND PROJECTID = V_PROJECTID;

  SELECT DB_MENUID
    INTO V_DB_RESOURCES_MENUID
    FROM DASH_MENUS
   WHERE UPPER(MENU_NAME) = UPPER('RESOURCES')
     AND PROJECTID = V_PROJECTID;

  SELECT DB_MENUID
    INTO V_DB_UL_MENUID
    FROM DASH_MENUS
   WHERE UPPER(MENU_NAME) = UPPER('USEFUL LINKS')
     AND PROJECTID = V_PROJECTID;

  SELECT ROLEID
    INTO V_ROLEID_ADMIN
    FROM ROLE
   WHERE ROLE_NAME = 'ROLE_ADMIN';

  SELECT ROLEID
    INTO V_ROLEID_SUPER
    FROM ROLE
   WHERE ROLE_NAME = 'ROLE_SUPER';

  SELECT ROLEID INTO V_ROLEID_USER FROM ROLE WHERE ROLE_NAME = 'ROLE_USER';

  SELECT ROLEID INTO V_ROLEID_CTB FROM ROLE WHERE ROLE_NAME = 'ROLE_CTB';

  DELETE FROM DASH_MENU_RPT_ACCESS
   WHERE DB_MENUID IN
         (V_DB_MANAGE_MENUID, V_DB_REPORTS_MENUID, V_DB_DOWNLOADS_MENUID,
          V_DB_RESOURCES_MENUID, V_DB_UL_MENUID)
     AND PROJECTID = V_PROJECTID;

  --INSERT INTO DASH_MENU_RPT_ACCESS START
  FOR REC_CUST_PROD_ID IN (SELECT CPL.CUST_PROD_ID
                             FROM CUST_PRODUCT_LINK CPL, CUSTOMER_INFO CI
                            WHERE CPL.CUSTOMERID = CI.CUSTOMERID
                              AND CPL.ACTIVATION_STATUS = 'AC'
                              AND CI.PROJECTID = V_PROJECTID) LOOP
  
    V_CUST_PROD_ID := REC_CUST_PROD_ID.CUST_PROD_ID;
  
    --MANAGE REPORTS  STATE - ROLE SUPER, ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'
       AND PROJECTID = V_PROJECTID;
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_MANAGE_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_SUPER,
       V_MIN_ORG_LEVEL,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_MANAGE_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --MANAGE CONTENT  STATE - ROLE SUPER, ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'
       AND PROJECTID = V_PROJECTID;
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_MANAGE_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_SUPER,
       V_MIN_ORG_LEVEL,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_MANAGE_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --GENERIC SYSTEM CONFIGURATION  STATE - ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
       AND PROJECTID = V_PROJECTID;
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_REPORTS_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --PRODUCT SPECIFIC SYSTEM CONFIGURATION  STATE - ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'PRODUCT SPECIFIC SYSTEM CONFIGURATION'
       AND PROJECTID = V_PROJECTID;
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_REPORTS_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'PRODUCT SPECIFIC SYSTEM CONFIGURATION'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'PRODUCT SPECIFIC SYSTEM CONFIGURATION'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --GROUP DOWNLOADS  STATE DISTRICT SCHOOL  - ROLE USER
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'GROUP DOWNLOADS'
       AND PROJECTID = V_PROJECTID;
  
    FOR REC_ORG_LEVEL IN (SELECT DISTINCT ORG_LEVEL
                            FROM ORG_TP_STRUCTURE OTS,
                                 TEST_PROGRAM     TP,
                                 CUSTOMER_INFO    CI
                           WHERE OTS.TP_ID = TP.TP_ID
                             AND TP.CUSTOMERID = CI.CUSTOMERID
                             AND PROJECTID = V_PROJECTID) LOOP
    
      INSERT INTO DASH_MENU_RPT_ACCESS
        (DB_MENUID,
         DB_REPORTID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         REPORT_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (V_DB_DOWNLOADS_MENUID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'GROUP DOWNLOADS'
             AND PROJECTID = V_PROJECTID),
         V_ROLEID_USER,
         REC_ORG_LEVEL.ORG_LEVEL,
         V_CUST_PROD_ID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'GROUP DOWNLOADS'
             AND PROJECTID = V_PROJECTID),
         V_PROJECTID,
         V_DMRA_ACTIVATION_STATUS,
         SYSDATE);
    
    END LOOP;
  
    --GROUP DOWNLOAD FILES  STATE DISTRICT SCHOOL  - ROLE USER
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'GROUP DOWNLOAD FILES'
       AND PROJECTID = V_PROJECTID;
  
    FOR REC_ORG_LEVEL IN (SELECT DISTINCT ORG_LEVEL
                            FROM ORG_TP_STRUCTURE OTS,
                                 TEST_PROGRAM     TP,
                                 CUSTOMER_INFO    CI
                           WHERE OTS.TP_ID = TP.TP_ID
                             AND TP.CUSTOMERID = CI.CUSTOMERID
                             AND PROJECTID = V_PROJECTID) LOOP
    
      INSERT INTO DASH_MENU_RPT_ACCESS
        (DB_MENUID,
         DB_REPORTID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         REPORT_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (V_DB_DOWNLOADS_MENUID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'GROUP DOWNLOAD FILES'
             AND PROJECTID = V_PROJECTID),
         V_ROLEID_USER,
         REC_ORG_LEVEL.ORG_LEVEL,
         V_CUST_PROD_ID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'GROUP DOWNLOAD FILES'
             AND PROJECTID = V_PROJECTID),
         V_PROJECTID,
         V_DMRA_ACTIVATION_STATUS,
         SYSDATE);
    
    END LOOP;
  
    --USER GUIDE  STATE DISTRICT SCHOOL  - ROLE USER
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = UPPER('User''s Guide')
       AND PROJECTID = V_PROJECTID;
  
    FOR REC_ORG_LEVEL IN (SELECT DISTINCT ORG_LEVEL
                            FROM ORG_TP_STRUCTURE OTS,
                                 TEST_PROGRAM     TP,
                                 CUSTOMER_INFO    CI
                           WHERE OTS.TP_ID = TP.TP_ID
                             AND TP.CUSTOMERID = CI.CUSTOMERID
                             AND PROJECTID = V_PROJECTID) LOOP
    
      INSERT INTO DASH_MENU_RPT_ACCESS
        (DB_MENUID,
         DB_REPORTID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         REPORT_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (V_DB_RESOURCES_MENUID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = UPPER('User''s Guide')
             AND PROJECTID = V_PROJECTID),
         V_ROLEID_USER,
         REC_ORG_LEVEL.ORG_LEVEL,
         V_CUST_PROD_ID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = UPPER('User''s Guide')
             AND PROJECTID = V_PROJECTID),
         V_PROJECTID,
         V_DMRA_ACTIVATION_STATUS,
         SYSDATE);
    
    END LOOP;
  
    --HTTP://DESE.MO.GOV/  STATE DISTRICT SCHOOL  - ROLE USER
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'HTTP://DESE.MO.GOV/'
       AND PROJECTID = V_PROJECTID;
  
    FOR REC_ORG_LEVEL IN (SELECT DISTINCT ORG_LEVEL
                            FROM ORG_TP_STRUCTURE OTS,
                                 TEST_PROGRAM     TP,
                                 CUSTOMER_INFO    CI
                           WHERE OTS.TP_ID = TP.TP_ID
                             AND TP.CUSTOMERID = CI.CUSTOMERID
                             AND PROJECTID = V_PROJECTID) LOOP
    
      INSERT INTO DASH_MENU_RPT_ACCESS
        (DB_MENUID,
         DB_REPORTID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         REPORT_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (V_DB_UL_MENUID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'HTTP://DESE.MO.GOV/'
             AND PROJECTID = V_PROJECTID),
         V_ROLEID_USER,
         REC_ORG_LEVEL.ORG_LEVEL,
         V_CUST_PROD_ID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'HTTP://DESE.MO.GOV/'
             AND PROJECTID = V_PROJECTID),
         V_PROJECTID,
         V_DMRA_ACTIVATION_STATUS,
         SYSDATE);
    
    END LOOP;
  
    --STUDENT ROSTER  STATE DISTRICT SCHOOL  - ROLE USER
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'STUDENT ROSTER'
       AND PROJECTID = V_PROJECTID;
  
    FOR REC_ORG_LEVEL IN (SELECT DISTINCT ORG_LEVEL
                            FROM ORG_TP_STRUCTURE OTS,
                                 TEST_PROGRAM     TP,
                                 CUSTOMER_INFO    CI
                           WHERE OTS.TP_ID = TP.TP_ID
                             AND TP.CUSTOMERID = CI.CUSTOMERID
                             AND PROJECTID = V_PROJECTID) LOOP
    
      INSERT INTO DASH_MENU_RPT_ACCESS
        (DB_MENUID,
         DB_REPORTID,
         ROLEID,
         ORG_LEVEL,
         CUST_PROD_ID,
         REPORT_SEQ,
         PROJECTID,
         ACTIVATION_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (V_DB_REPORTS_MENUID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'STUDENT ROSTER'
             AND PROJECTID = V_PROJECTID),
         V_ROLEID_USER,
         REC_ORG_LEVEL.ORG_LEVEL,
         V_CUST_PROD_ID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'STUDENT ROSTER'
             AND PROJECTID = V_PROJECTID),
         V_PROJECTID,
         V_DMRA_ACTIVATION_STATUS,
         SYSDATE);
    
    END LOOP;
  
    --GUIDE TO INTERPRETING RESULTS &#40;GIR&#41;  STATE - ROLE USER
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) =
           'GUIDE TO INTERPRETING RESULTS &#40;GIR&#41;'
       AND PROJECTID = V_PROJECTID;
  
    INSERT INTO DASH_MENU_RPT_ACCESS
      (DB_MENUID,
       DB_REPORTID,
       ROLEID,
       ORG_LEVEL,
       CUST_PROD_ID,
       REPORT_SEQ,
       PROJECTID,
       ACTIVATION_STATUS,
       CREATED_DATE_TIME)
    VALUES
      (V_DB_RESOURCES_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) =
               'GUIDE TO INTERPRETING RESULTS &#40;GIR&#41;'
           AND PROJECTID = V_PROJECTID),
       V_ROLEID_USER,
       1,
       V_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) =
               'GUIDE TO INTERPRETING RESULTS &#40;GIR&#41;'
           AND PROJECTID = V_PROJECTID),
       V_PROJECTID,
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --INSERT INTO DASH_MENU_RPT_ACCESS END
  
    DELETE FROM DASH_ACTION_ACCESS
     WHERE PROJECTID = V_PROJECTID
       AND CUST_PROD_ID = V_CUST_PROD_ID;
    -- INSERT INTO DASH_ACTION_ACCESS START
    FOR REC_DASH_REPORTS IN (SELECT *
                               FROM DASH_REPORTS
                              WHERE ACTIVATION_STATUS = 'AC'
                                AND (REPORT_TYPE = 'API_LINK' OR
                                    REPORT_TYPE = 'API_CUSTOM' OR
                                    UPPER(REPORT_NAME) IN
                                    ('GROUP DOWNLOAD FILES',
                                     'STUDENT ROSTER'))
                                AND PROJECTID = V_PROJECTID) LOOP
    
      IF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'MANAGE USERS' THEN
        V_ACTION_TYPE := 'USR';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'MANAGE ORGANIZATIONS' THEN
        V_ACTION_TYPE := 'ORG';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'MANAGE STUDENTS' THEN
        V_ACTION_TYPE := 'STD';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) =
            'MANAGE EDUCATION CENTER USERS' THEN
        V_ACTION_TYPE := 'USR';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'MANAGE PARENTS' THEN
        V_ACTION_TYPE := 'PAR';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'MANAGE REPORTS' THEN
        V_ACTION_TYPE := 'RPT';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'MANAGE CONTENT' THEN
        V_ACTION_TYPE := 'CON';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'RESET PASSWORD' THEN
        V_ACTION_TYPE := 'RPW';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'GROUP DOWNLOADS' THEN
        V_ACTION_TYPE := 'GDL';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'GROUP DOWNLOAD FILES' THEN
        V_ACTION_TYPE := 'GDF';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'GRT/IC FILE DOWNLOAD' THEN
        V_ACTION_TYPE := 'GRTIC';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'STUDENT DATA FILE' THEN
        V_ACTION_TYPE := 'SDF';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'STUDENT ROSTER' THEN
        V_ACTION_TYPE := 'DRPT';
      END IF;
    
      FOR REC_DASH_RPT_ACTION IN (SELECT *
                                    FROM DASH_RPT_ACTION
                                   WHERE ACTION_TYPE = V_ACTION_TYPE
                                     AND PROJECTID = V_PROJECTID
                                   ORDER BY ACTION_NAME) LOOP
      
        FOR REC_DASH_MENU_RPT_ACCESS IN (SELECT *
                                           FROM DASH_MENU_RPT_ACCESS
                                          WHERE DB_REPORTID =
                                                REC_DASH_REPORTS.DB_REPORTID
                                            AND CUST_PROD_ID =
                                                V_CUST_PROD_ID
                                            AND PROJECTID = V_PROJECTID) LOOP
        
          V_DAA_ACTIVATION_STATUS := REC_DASH_MENU_RPT_ACCESS.ACTIVATION_STATUS;
        
          INSERT INTO DASH_ACTION_ACCESS
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
          VALUES
            (SEQ_DASH_ACTION_ACCESS.NEXTVAL,
             REC_DASH_MENU_RPT_ACCESS.DB_MENUID,
             REC_DASH_REPORTS.DB_REPORTID,
             REC_DASH_RPT_ACTION.DB_ACTIONID,
             REC_DASH_MENU_RPT_ACCESS.ROLEID,
             REC_DASH_MENU_RPT_ACCESS.ORG_LEVEL,
             V_CUST_PROD_ID,
             REC_DASH_RPT_ACTION.DB_ACTIONID,
             V_PROJECTID,
             V_DAA_ACTIVATION_STATUS,
             SYSDATE);
        
        END LOOP;
      
      END LOOP;
    
    END LOOP;
    -- INSERT INTO DASH_ACTION_ACCESS END
  
  END LOOP;

  COMMIT;
  P_OUT_STATUS_NUMBER := 1;

EXCEPTION
  WHEN OTHERS THEN
    P_OUT_STATUS_NUMBER := 0;
    P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
    ROLLBACK;
END PRC_CONFIGURATION_USMO;
/