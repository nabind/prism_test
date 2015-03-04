create or replace package PKG_PROC_MVIEW_REFRESH is

  -- Author  : Debashis Deb
  -- Created : 12/12/2013 12:41:47 PM
  -- Purpose : This Package will be used at the ETL side.

  PROCEDURE PROC_ORG_NODE_DIM_HIER_REFERSH ;
  PROCEDURE PROC_STUDENT_REFRESH (P_PROCESS_ID IN NUMBER) ;


end PKG_PROC_MVIEW_REFRESH;
/
create or replace package body PKG_PROC_MVIEW_REFRESH is

  PROCEDURE PROC_ORG_NODE_DIM_HIER_REFERSH
  IS
  BEGIN
      
     ---crerated Mview CUSTOMER_INFO by dropping the View CUSTOMER_INFO as a part of performance tuning to remove nested loops
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'CUSTOMER_INFO',
           'MVIEW',
           'REFRESH STARTED',
           'START',
           SYSDATE);
        COMMIT;
        DBMS_MVIEW.REFRESH('CUSTOMER_INFO', 'C');
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'CUSTOMER_INFO',
           'MVIEW',
           'REFRESH COMPLETED',
           'END',
           SYSDATE);
        COMMIT;    
     ---crerated Mview CUST_PRODUCT_LINK by dropping the View CUST_PRODUCT_LINK as a part of performance tuning to remove nested loops
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'CUST_PRODUCT_LINK',
           'MVIEW',
           'REFRESH STARTED',
           'START',
           SYSDATE);
        COMMIT;
        DBMS_MVIEW.REFRESH('CUST_PRODUCT_LINK', 'C');
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'CUST_PRODUCT_LINK',
           'MVIEW',
           'REFRESH COMPLETED',
           'END',
           SYSDATE);
        COMMIT;
        
        ---crerated Mview MV_ORG_TP_STRUCTURE by dropping the View MV_ORG_TP_STRUCTURE as a part of performance tuning of the manage module packages
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'MV_ORG_TP_STRUCTURE',
           'MVIEW',
           'REFRESH STARTED',
           'START',
           SYSDATE);
        COMMIT;
        DBMS_MVIEW.REFRESH('MV_ORG_TP_STRUCTURE', '?');
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'MV_ORG_TP_STRUCTURE',
           'MVIEW',
           'REFRESH COMPLETED',
           'END',
           SYSDATE);
        COMMIT;
  
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'MV_TEST_PROGRAM',
           'MVIEW',
           'REFRESH STARTED',
           'START',
           SYSDATE);
        COMMIT;
        DBMS_MVIEW.REFRESH('MV_TEST_PROGRAM', 'C');
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'MV_TEST_PROGRAM',
           'MVIEW',
           'REFRESH COMPLETED',
           'END',
           SYSDATE);
        COMMIT;


        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'ORG_NODE_DIM_HIER',
           'MVIEW',
           'REFRESH STARTED',
           'START',
           SYSDATE);
        COMMIT;
        DBMS_MVIEW.REFRESH('ORG_NODE_DIM_HIER', 'C');
        INSERT INTO PERF_LOG
        VALUES
          (PERF_LOG_SEQ.NEXTVAL,
           'ORG_NODE_DIM_HIER',
           'MVIEW',
           'REFRESH COMPLETED',
           'END',
           SYSDATE);
        COMMIT;
        END ;

PROCEDURE PROC_STUDENT_REFRESH(P_PROCESS_ID IN NUMBER) IS
BEGIN
---crerated Mview SUBTEST_DIM by dropping the View SUBTEST_DIM as a part of performance tuning to remove nested loops
INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'SUBTEST_DIM',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('SUBTEST_DIM', 'C');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'SUBTEST_DIM',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

---crerated Mview FORM_DIM by dropping the View FORM_DIM as a part of performance tuning to remove nested loops
INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'FORM_DIM',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('FORM_DIM', 'C');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'FORM_DIM',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;
  
  ---crerated Mview GENDER_DIM by dropping the View GENDER_DIM as a part of performance tuning to remove nested loops
INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'GENDER_DIM',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('GENDER_DIM', 'C');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'GENDER_DIM',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;
  
   ---crerated Mview OBJECTIVE_DIM by dropping the View OBJECTIVE_DIM as a part of performance tuning to remove nested loops
INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'OBJECTIVE_DIM',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('OBJECTIVE_DIM', 'C');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'OBJECTIVE_DIM',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'TABLE_1_STU',
     'TABLE',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;

  PKG_PRF_ORG_USR.SP_STUDENT_DEMO_VALUES(P_PROCESS_ID);

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'TABLE_1_STU',
     'TABLE',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_STUDENT_DETAILS',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('MV_STUDENT_DETAILS', 'F');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_STUDENT_DETAILS',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_STUDENT_FILE_DOWNLOAD',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('MV_STUDENT_FILE_DOWNLOAD', 'F');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_STUDENT_FILE_DOWNLOAD',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

   INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_SUB_OBJ_FORM_MAP',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('MV_SUB_OBJ_FORM_MAP', 'C');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_SUB_OBJ_FORM_MAP',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_SUBTEST_SCORE_TYPE_MAP',
     'MVIEW',
     'REFRESH STARTED',
     'START',
     SYSDATE);
  COMMIT;
  DBMS_MVIEW.REFRESH('MV_SUBTEST_SCORE_TYPE_MAP', 'C');

  INSERT INTO PERF_LOG
  VALUES
    (PERF_LOG_SEQ.NEXTVAL,
     'MV_SUBTEST_SCORE_TYPE_MAP',
     'MVIEW',
     'REFRESH COMPLETED',
     'END',
     SYSDATE);
  COMMIT;

END PROC_STUDENT_REFRESH;
end PKG_PROC_MVIEW_REFRESH;
/
