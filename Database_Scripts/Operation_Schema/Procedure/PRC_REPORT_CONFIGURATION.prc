CREATE OR REPLACE PROCEDURE PRC_REPORT_CONFIGURATION(P_IN_PROJECTID      IN NUMBER,
                                                     P_OUT_STATUS_NUMBER OUT NUMBER,
                                                     P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS

  V_DEFAULT_CUST_PROD_ID CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE := 0;
  V_COUNT                NUMBER(1) := 0;

BEGIN
  P_OUT_STATUS_NUMBER := 0;

  IF P_IN_PROJECTID = 1 THEN
    V_DEFAULT_CUST_PROD_ID := 3005;
  ELSIF P_IN_PROJECTID = 2 THEN
    V_DEFAULT_CUST_PROD_ID := 5001;
  END IF;

  FOR REC_DR IN (SELECT DISTINCT DB_REPORTID
                   FROM DASH_REPORTS) LOOP
  
    FOR REC_CUST_PROD_ID IN (SELECT CPL.CUST_PROD_ID
                               FROM CUST_PRODUCT_LINK CPL, CUSTOMER_INFO CI
                              WHERE CPL.CUSTOMERID = CI.CUSTOMERID
                                AND CPL.ACTIVATION_STATUS = 'AC') LOOP
    
      FOR REC_DMRA IN (SELECT *
                         FROM DASH_MENU_RPT_ACCESS
                        WHERE DB_REPORTID = REC_DR.DB_REPORTID
                          AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID) LOOP
      
        SELECT COUNT(1)
          INTO V_COUNT
          FROM DASH_MENU_RPT_ACCESS
         WHERE DB_MENUID = REC_DMRA.DB_MENUID
           AND DB_REPORTID = REC_DMRA.DB_REPORTID
           AND ROLEID = REC_DMRA.ROLEID
           AND ORG_LEVEL = REC_DMRA.ORG_LEVEL
           AND CUST_PROD_ID = REC_CUST_PROD_ID.CUST_PROD_ID;
      
        IF V_COUNT = 0 THEN
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
            (REC_DMRA.DB_MENUID,
             REC_DMRA.DB_REPORTID,
             REC_DMRA.ROLEID,
             REC_DMRA.ORG_LEVEL,
             REC_CUST_PROD_ID.CUST_PROD_ID,
             REC_DMRA.DB_REPORTID,
             REC_DMRA.ACTIVATION_STATUS,
             SYSDATE);
        END IF;
      
      END LOOP;
    
    END LOOP;
  END LOOP;

  COMMIT;
  P_OUT_STATUS_NUMBER := 1;

EXCEPTION
  WHEN OTHERS THEN
    P_OUT_STATUS_NUMBER := 0;
    P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
    ROLLBACK;
END PRC_REPORT_CONFIGURATION;
/
