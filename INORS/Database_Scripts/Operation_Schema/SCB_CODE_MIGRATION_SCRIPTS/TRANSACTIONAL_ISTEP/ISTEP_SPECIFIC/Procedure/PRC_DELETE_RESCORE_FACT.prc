CREATE OR REPLACE PROCEDURE PRC_DELETE_RESCORE_FACT(IN_ADMIN_ID   NUMBER,
                                                    IN_PROCESS_ID NUMBER) AS

  LV_ROW_LIMIT    NUMBER := 1000;
  LV_LOG          VARCHAR2(4000);
  LV_CUST_PROD_ID NUMBER;
  LV_STUDENT_ID   INT_ARRAY := INT_ARRAY();

  CURSOR CUR_STUDENT_BIO_ID IS
    SELECT ST.STUDENT_BIO_ID
      FROM STUDENT_BIO_DIM   ST,
           RESULTS_GRT_FACT  OL,
           CUST_PRODUCT_LINK CP,
           PRODUCT           P
     WHERE ST.STUDENT_BIO_ID = OL.STUDENT_BIO_ID
       AND OL.CUST_PROD_ID = CP.CUST_PROD_ID
       AND CP.PRODUCTID = P.PRODUCTID
       AND P.IS_EDITABLE = 'Y'
       AND CP.CUSTOMERID =
           (SELECT DISTINCT H.CUSTOMER_ID
              FROM STG_HIER_DETAILS H
             WHERE H.PROCESS_ID = IN_PROCESS_ID)
       AND ST.ADMINID = IN_ADMIN_ID
       AND NOT EXISTS
     (SELECT 1
              FROM STG_STD_BIO_DETAILS BIO, STG_PROCESS_STATUS P
             WHERE BIO.PROCESS_ID = P.PROCESS_ID
               AND P.HIER_VALIDATION = 'CO'
               AND P.BIO_VALIDATION = 'VA'
               AND P.DEMO_VALIDATION = 'VA'
               AND P.CONTENT_VALIDATION = 'VA'
               AND P.OBJECTIVE_VALIDATION = 'VA'
               AND ST.TEST_ELEMENT_ID = BIO.TEST_ELEMENT_ID);

  /*    SELECT ST.STUDENT_BIO_ID
   FROM STUDENT_BIO_DIM   ST,
        ORG_PRODUCT_LINK  OL,
        CUST_PRODUCT_LINK CP,
        PRODUCT           P
  WHERE ST.ORG_NODEID = OL.ORG_NODEID
    AND OL.CUST_PROD_ID = CP.CUST_PROD_ID
    AND CP.PRODUCTID = P.PRODUCTID
    AND P.IS_EDITABLE = 'Y'
    AND CP.CUSTOMERID =
        (SELECT DISTINCT H.CUSTOMER_ID
           FROM STG_HIER_DETAILS H
          WHERE H.PROCESS_ID = IN_PROCESS_ID)
    AND ST.ADMINID = IN_ADMIN_ID
    AND NOT EXISTS
  (SELECT 1
           FROM STG_STD_BIO_DETAILS BIO, STG_PROCESS_STATUS P
          WHERE BIO.PROCESS_ID = P.PROCESS_ID
            AND P.HIER_VALIDATION = 'CO'
            AND P.BIO_VALIDATION = 'VA'
            AND P.DEMO_VALIDATION = 'VA'
            AND P.CONTENT_VALIDATION = 'VA'
            AND P.OBJECTIVE_VALIDATION = 'VA'
            AND ST.TEST_ELEMENT_ID = BIO.TEST_ELEMENT_ID);*/


BEGIN

  -- Disable Constraints for STUDENT_BIO_DIM deletion

  LV_LOG := LV_LOG || ' PROCEDURE STARTS: DELETING STUDENT IDs ' || chr(10);

  EXECUTE IMMEDIATE 'alter table STUDENT_PDF_FILES
    disable constraint FK_STUDENT_PDF_FILES_3';

  EXECUTE IMMEDIATE 'alter table SUBTEST_SCORE_FACT
    disable constraint FK_SUBTEST_SCORE_FACT_10';

  EXECUTE IMMEDIATE 'alter table OBJECTIVE_SCORE_FACT
    disable constraint FK_OBJECTIVE_SCORE_FACT_1';

  EXECUTE IMMEDIATE 'alter table INVITATION_CODE
  disable constraint FK_IC_STUD_BIO_ID';

  EXECUTE IMMEDIATE 'alter table STUDENT_DEMO_VALUES
  disable constraint FK_STUDENT_DEMO_VALUES_1';

  EXECUTE IMMEDIATE 'alter table STU_SUBTEST_DEMO_VALUES
  disable constraint FK_STU_SUBTEST_DEMO_VALUES_1';

  EXECUTE IMMEDIATE 'alter table UDTR_ROSTER_FACT
  disable constraint FK_UDTR_ROSTER_FACT_5';

  LV_LOG := LV_LOG || ' Constraints disabled ' || chr(10);

  SELECT CP.CUST_PROD_ID
    INTO LV_CUST_PROD_ID
    FROM CUST_PRODUCT_LINK CP, PRODUCT P
   WHERE CP.PRODUCTID = P.PRODUCTID
     AND P.IS_EDITABLE = 'Y'
     AND CP.CUSTOMERID = (SELECT H.CUSTOMER_ID
                            FROM STG_HIER_DETAILS H
                           WHERE H.PROCESS_ID = IN_PROCESS_ID
                             AND ROWNUM = 1)
     AND CP.ADMINID = IN_ADMIN_ID;

  OPEN CUR_STUDENT_BIO_ID;

  LOOP
  
    FETCH CUR_STUDENT_BIO_ID BULK COLLECT
      INTO LV_STUDENT_ID limit LV_ROW_LIMIT;
  
    EXIT WHEN LV_STUDENT_ID.COUNT = 0;
  
    -- INVITATION_CODE
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      UPDATE INVITATION_CODE
         SET STUDENT_BIO_ID = NULL
       WHERE STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID)
         AND cust_prod_id = LV_CUST_PROD_ID;
  
    /*LV_LOG := LV_LOG ||
    ' Number of Records Updated STUDENT_BIO_ID to NULL from INVITATION_CODE : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- RESULTS_GRT_FACT
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM RESULTS_GRT_FACT RSGRT
       WHERE RSGRT.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID)
         AND RSGRT.cust_prod_id = LV_CUST_PROD_ID;
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from RESULTS_GRT_FACT : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- OBJECTIVE_SCORE_FACT
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM OBJECTIVE_SCORE_FACT OSF
       WHERE OSF.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID)
         AND OSF.cust_prod_id = LV_CUST_PROD_ID;
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from OBJECTIVE_SCORE_FACT : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- SUBTEST_SCORE_FACT
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM SUBTEST_SCORE_FACT SCF
       WHERE SCF.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID)
         AND SCF.cust_prod_id = LV_CUST_PROD_ID;
    /*
    LV_LOG := LV_LOG ||
              ' Number of Records deleted from SUBTEST_SCORE_FACT : ' ||
              SQL%ROWCOUNT || chr(10);*/
  
    -- STUDENT_DEMO_VALUES
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM STUDENT_DEMO_VALUES SDV
       WHERE SDV.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID);
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from STUDENT_DEMO_VALUES : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- STU_SUBTEST_DEMO_VALUES
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM STU_SUBTEST_DEMO_VALUES SSDV
       WHERE SSDV.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID);
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from STU_SUBTEST_DEMO_VALUES : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- STUDENT_PDF_FILES
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM STUDENT_PDF_FILES SSPF
       WHERE SSPF.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID);
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from STUDENT_PDF_FILES : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- UDTR_ROSTER_FACT
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM UDTR_ROSTER_FACT UDTR
       WHERE UDTR.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID)
         AND UDTR.cust_prod_id = LV_CUST_PROD_ID;
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from UDTR_ROSTER_FACT : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
    -- STUDENT_BIO_DIM
  
    FORALL C_STUDENT_ID IN 1 .. LV_STUDENT_ID.COUNT
      DELETE FROM STUDENT_BIO_DIM SBD
       WHERE SBD.STUDENT_BIO_ID = LV_STUDENT_ID(C_STUDENT_ID);
  
    /*    LV_LOG := LV_LOG ||
    ' Number of Records deleted from STUDENT_BIO_DIM : ' ||
    SQL%ROWCOUNT || chr(10);*/
  
  END LOOP;

  COMMIT;

  -- Enable constraints which were disabled for STUDENT_BIO_DIM deletion

  EXECUTE IMMEDIATE 'alter table STUDENT_PDF_FILES
    enable constraint FK_STUDENT_PDF_FILES_3';

  EXECUTE IMMEDIATE 'alter table SUBTEST_SCORE_FACT
    enable constraint FK_SUBTEST_SCORE_FACT_10';

  EXECUTE IMMEDIATE 'alter table OBJECTIVE_SCORE_FACT
    enable constraint FK_OBJECTIVE_SCORE_FACT_1';

  EXECUTE IMMEDIATE 'alter table INVITATION_CODE
  enable constraint FK_IC_STUD_BIO_ID';

  EXECUTE IMMEDIATE 'alter table STUDENT_DEMO_VALUES
  enable constraint FK_STUDENT_DEMO_VALUES_1';

  EXECUTE IMMEDIATE 'alter table STU_SUBTEST_DEMO_VALUES
  enable constraint FK_STU_SUBTEST_DEMO_VALUES_1';

  EXECUTE IMMEDIATE 'alter table UDTR_ROSTER_FACT
  enable constraint FK_UDTR_ROSTER_FACT_5';

  LV_LOG := LV_LOG || ' Constraints enabled ' || chr(10);

  LV_LOG := LV_LOG || ' Procedure completed ' || chr(10);

  UPDATE STG_PROCESS_STATUS STP
     SET STP.PROCESS_LOG = STP.PROCESS_LOG || LV_LOG
   WHERE STP.PROCESS_ID = IN_PROCESS_ID;

  COMMIT;

EXCEPTION

  WHEN OTHERS THEN
  
    ROLLBACK;
  
    EXECUTE IMMEDIATE 'alter table STUDENT_PDF_FILES
    enable constraint FK_STUDENT_PDF_FILES_3';
  
    EXECUTE IMMEDIATE 'alter table SUBTEST_SCORE_FACT
    enable constraint FK_SUBTEST_SCORE_FACT_10';
  
    EXECUTE IMMEDIATE 'alter table OBJECTIVE_SCORE_FACT
    enable constraint FK_OBJECTIVE_SCORE_FACT_1';
  
    EXECUTE IMMEDIATE 'alter table INVITATION_CODE
    enable constraint FK_IC_STUD_BIO_ID';
  
    EXECUTE IMMEDIATE 'alter table STUDENT_DEMO_VALUES
    enable constraint FK_STUDENT_DEMO_VALUES_1';
  
    EXECUTE IMMEDIATE 'alter table STU_SUBTEST_DEMO_VALUES
    enable constraint FK_STU_SUBTEST_DEMO_VALUES_1';
  
    EXECUTE IMMEDIATE 'alter table UDTR_ROSTER_FACT
    enable constraint FK_UDTR_ROSTER_FACT_5';
  
    LV_LOG := LV_LOG || ' Constraints enabled ' || chr(10);
    LV_LOG := LV_LOG || ' Procedure Failed due to error ' || SQLERRM ||
              chr(10);
  
    UPDATE STG_PROCESS_STATUS STP
       SET STP.PROCESS_LOG = STP.PROCESS_LOG || LV_LOG
     WHERE STP.PROCESS_ID = IN_PROCESS_ID;
  
    COMMIT;
    
    RAISE;
  
END PRC_DELETE_RESCORE_FACT;
/
