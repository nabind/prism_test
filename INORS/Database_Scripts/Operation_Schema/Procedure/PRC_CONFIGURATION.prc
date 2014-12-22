CREATE OR REPLACE PROCEDURE PRC_CONFIGURATION(P_IN_PROJECTID      IN NUMBER,
                                              P_OUT_STATUS_NUMBER OUT NUMBER,
                                              P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  V_DB_MENUID              DASH_MENU_RPT_ACCESS.DB_MENUID%TYPE;
  V_DEFAULT_CUST_PROD_ID   CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE := 0;
  V_ROLEID_ADMIN           ROLE.ROLEID%TYPE;
  V_ROLEID_SUPER           ROLE.ROLEID%TYPE;
  V_ROLEID_CTB             ROLE.ROLEID%TYPE;
  V_ROLEID_EDU_ADMIN       ROLE.ROLEID%TYPE;
  V_DMRA_ACTIVATION_STATUS DASH_MENU_RPT_ACCESS.ACTIVATION_STATUS%TYPE;
  V_MAX_ORG_LEVEL          ORG_TP_STRUCTURE.ORG_LEVEL%TYPE;
  V_MIN_ORG_LEVEL          ORG_TP_STRUCTURE.ORG_LEVEL%TYPE;
  V_ACTION_TYPE            DASH_RPT_ACTION.ACTION_TYPE%TYPE;
  V_DAA_ACTIVATION_STATUS  DASH_ACTION_ACCESS.ACTIVATION_STATUS%TYPE;

BEGIN
  P_OUT_STATUS_NUMBER := 0;

  UPDATE DASH_REPORTS
     SET REPORT_TYPE = 'API'
   WHERE UPPER(REPORT_NAME) IN
         ('GENERIC SYSTEM CONFIGURATION',
          'PRODUCT SPECIFIC SYSTEM CONFIGURATION');

  UPDATE DASH_REPORTS
     SET REPORT_TYPE = 'API_NFCUSTOM'
   WHERE UPPER(REPORT_NAME) IN
         ('STUDENT DATA FILE', 'GROUP DOWNLOAD FILES');

  SELECT DB_MENUID
    INTO V_DB_MENUID
    FROM DASH_MENUS
   WHERE UPPER(MENU_NAME) = UPPER('MANAGE');

  SELECT ROLEID
    INTO V_ROLEID_ADMIN
    FROM ROLE
   WHERE ROLE_NAME = 'ROLE_ADMIN';

  SELECT ROLEID
    INTO V_ROLEID_SUPER
    FROM ROLE
   WHERE ROLE_NAME = 'ROLE_SUPER';

  SELECT ROLEID
    INTO V_ROLEID_EDU_ADMIN
    FROM PRISMGLOBAL.ROLE
   WHERE ROLE_NAME = 'ROLE_EDU_ADMIN';

  SELECT ROLEID INTO V_ROLEID_CTB FROM ROLE WHERE ROLE_NAME = 'ROLE_CTB';

  DELETE FROM DASH_MENU_RPT_ACCESS WHERE DB_MENUID = V_DB_MENUID;

  DELETE FROM DASH_ACTION_ACCESS;

  FOR REC_CUST_PROD_ID IN (SELECT CPL.CUST_PROD_ID
                             FROM CUST_PRODUCT_LINK CPL, CUSTOMER_INFO CI
                            WHERE CPL.CUSTOMERID = CI.CUSTOMERID
                              AND CPL.ACTIVATION_STATUS = 'AC') LOOP
  
    V_DEFAULT_CUST_PROD_ID := REC_CUST_PROD_ID.CUST_PROD_ID;
  
    --INSERT INTO DASH_MENU_RPT_ACCESS START
    FOR REC_DASH_REPORTS IN (SELECT *
                               FROM DASH_REPORTS
                              WHERE UPPER(REPORT_NAME) IN
                                    ('MANAGE USERS',
                                     'MANAGE ORGANIZATIONS',
                                     'MANAGE STUDENTS',
                                     'MANAGE PARENTS')) LOOP
    
      V_DMRA_ACTIVATION_STATUS := REC_DASH_REPORTS.ACTIVATION_STATUS;
    
      SELECT MAX(OTS.ORG_LEVEL)
        INTO V_MAX_ORG_LEVEL
        FROM ORG_TP_STRUCTURE OTS, TEST_PROGRAM TP, CUSTOMER_INFO CI
       WHERE OTS.TP_ID = TP.TP_ID
         AND TP.CUSTOMERID = CI.CUSTOMERID;
    
      SELECT MIN(OTS.ORG_LEVEL)
        INTO V_MIN_ORG_LEVEL
        FROM ORG_TP_STRUCTURE OTS, TEST_PROGRAM TP, CUSTOMER_INFO CI
       WHERE OTS.TP_ID = TP.TP_ID
         AND TP.CUSTOMERID = CI.CUSTOMERID;
    
      FOR REC_ORG_LEVEL IN (SELECT DISTINCT OTS.ORG_LEVEL AS ORG_LEVEL
                              FROM ORG_TP_STRUCTURE OTS,
                                   TEST_PROGRAM     TP,
                                   CUSTOMER_INFO    CI
                             WHERE OTS.TP_ID = TP.TP_ID
                               AND TP.CUSTOMERID = CI.CUSTOMERID
                             ORDER BY OTS.ORG_LEVEL) LOOP
      
        IF P_IN_PROJECTID = 2 AND REC_ORG_LEVEL.ORG_LEVEL = V_MAX_ORG_LEVEL THEN
          V_DMRA_ACTIVATION_STATUS := 'IN';
        END IF;
      
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
          (V_DB_MENUID,
           REC_DASH_REPORTS.DB_REPORTID,
           V_ROLEID_ADMIN,
           REC_ORG_LEVEL.ORG_LEVEL,
           V_DEFAULT_CUST_PROD_ID,
           REC_DASH_REPORTS.DB_REPORTID,
           V_DMRA_ACTIVATION_STATUS,
           SYSDATE);
      
      END LOOP;
    
    END LOOP;
  
    --MANAGE EDUCATION CENTER USERS  (-99 - ROLE EDU ADMIN) AND (STATE - ROLE SUPER, ROLE CTB)
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS';
  
    IF P_IN_PROJECTID = 1 THEN
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
        (V_DB_MENUID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS'),
         V_ROLEID_EDU_ADMIN,
         -99,
         V_DEFAULT_CUST_PROD_ID,
         (SELECT DB_REPORTID
            FROM DASH_REPORTS
           WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS'),
         V_DMRA_ACTIVATION_STATUS,
         SYSDATE);
    END IF;
  
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS'),
       V_ROLEID_SUPER,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS'),
       V_DMRA_ACTIVATION_STATUS,
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS'),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE EDUCATION CENTER USERS'),
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --MANAGE REPORTS  STATE - ROLE SUPER, ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS';
  
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'),
       V_ROLEID_SUPER,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'),
       V_DMRA_ACTIVATION_STATUS,
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE REPORTS'),
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --MANAGE CONTENT  STATE - ROLE SUPER, ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT';
  
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'),
       V_ROLEID_SUPER,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'),
       V_DMRA_ACTIVATION_STATUS,
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'MANAGE CONTENT'),
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
  
    --RESET PASSWORD  STATE - ROLE SUPER, ROLE CTB
    SELECT ACTIVATION_STATUS
      INTO V_DMRA_ACTIVATION_STATUS
      FROM DASH_REPORTS
     WHERE UPPER(REPORT_NAME) = 'RESET PASSWORD';
  
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'RESET PASSWORD'),
       V_ROLEID_SUPER,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'RESET PASSWORD'),
       V_DMRA_ACTIVATION_STATUS,
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
      (V_DB_MENUID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'RESET PASSWORD'),
       V_ROLEID_CTB,
       V_MIN_ORG_LEVEL,
       V_DEFAULT_CUST_PROD_ID,
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_NAME) = 'RESET PASSWORD'),
       V_DMRA_ACTIVATION_STATUS,
       SYSDATE);
    --INSER INTO DASH_MENU_RPT_ACCESS END
  
    -- INSERT INTO DASH_ACTION_ACCESS START
    FOR REC_DASH_REPORTS IN (SELECT *
                               FROM DASH_REPORTS
                              WHERE REPORT_TYPE = 'API_LINK'
                                 OR REPORT_TYPE = 'API_CUSTOM'
                                 OR UPPER(REPORT_NAME) IN
                                    ('STUDENT DATA FILE',
                                     'GROUP DOWNLOAD FILES')) LOOP
    
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
        V_ACTION_TYPE := 'GDF';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'GROUP DOWNLOAD FILES' THEN
        V_ACTION_TYPE := 'GDF';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'GRT/IC FILE DOWNLOAD' THEN
        V_ACTION_TYPE := 'GDF';
      ELSIF UPPER(REC_DASH_REPORTS.REPORT_NAME) = 'STUDENT DATA FILE' THEN
        V_ACTION_TYPE := 'GDF';
      END IF;
    
      FOR REC_DASH_RPT_ACTION IN (SELECT *
                                    FROM DASH_RPT_ACTION
                                   WHERE ACTION_TYPE = V_ACTION_TYPE
                                      OR ACTION_TYPE = 'GEN'
                                   ORDER BY ACTION_NAME) LOOP
      
        FOR REC_DASH_MENU_RPT_ACCESS IN (SELECT *
                                           FROM DASH_MENU_RPT_ACCESS
                                          WHERE DB_REPORTID =
                                                REC_DASH_REPORTS.DB_REPORTID
                                            AND CUST_PROD_ID =
                                                V_DEFAULT_CUST_PROD_ID) LOOP
        
          V_DAA_ACTIVATION_STATUS := REC_DASH_MENU_RPT_ACCESS.ACTIVATION_STATUS;
        
          IF P_IN_PROJECTID = 1 AND UPPER(REC_DASH_RPT_ACTION.ACTION_NAME) =
             'SELECT ORGANIZATION MODE' THEN
            V_DAA_ACTIVATION_STATUS := 'IN';
          END IF;
        
          IF P_IN_PROJECTID = 1 AND
             UPPER(REC_DASH_RPT_ACTION.ACTION_TYPE) = 'USR' AND
             UPPER(REC_DASH_RPT_ACTION.ACTION_NAME) = 'DOWNLOAD USERS' THEN
            V_DAA_ACTIVATION_STATUS := 'IN';
          END IF;
        
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
             CREATED_DATE_TIME)
          VALUES
            (SEQ_DASH_ACTION_ACCESS.NEXTVAL,
             REC_DASH_MENU_RPT_ACCESS.DB_MENUID,
             REC_DASH_REPORTS.DB_REPORTID,
             REC_DASH_RPT_ACTION.DB_ACTIONID,
             REC_DASH_MENU_RPT_ACCESS.ROLEID,
             REC_DASH_MENU_RPT_ACCESS.ORG_LEVEL,
             V_DEFAULT_CUST_PROD_ID,
             REC_DASH_RPT_ACTION.DB_ACTIONID,
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
END PRC_CONFIGURATION;
/
