CREATE OR REPLACE PROCEDURE SP_DELETE_STG_TABLES(WKF_PARTITION_NAME IN VARCHAR2,
                                                 IN_MODE            IN NUMBER) IS

  
BEGIN
  
    IF IN_MODE = 1 THEN 
    EXECUTE IMMEDIATE ('TRUNCATE TABLE STUDENT_RAW_DATA');
    EXECUTE IMMEDIATE ('TRUNCATE TABLE STUDENT_DATA_EXTRACT');
    END IF;
    IF IN_MODE = 2 THEN 
  EXECUTE IMMEDIATE ('DELETE FROM STG_LSTNODE_HIER_DETAILS WHERE WKF_PARTITION_NAME = ' || WKF_PARTITION_NAME);
  EXECUTE IMMEDIATE ('DELETE FROM STG_HIER_DETAILS WHERE WKF_PARTITION_NAME = ' || WKF_PARTITION_NAME);
  EXECUTE IMMEDIATE ('DELETE FROM STG_ITEM_RESPONSE_DETAILS WHERE WKF_PARTITION_NAME = ' || WKF_PARTITION_NAME);
  EXECUTE IMMEDIATE ('DELETE FROM STG_STD_OBJECTIVE_DETAILS WHERE WKF_PARTITION_NAME = ' || WKF_PARTITION_NAME);
  EXECUTE IMMEDIATE ('DELETE FROM STG_STD_SUBTEST_DETAILS WHERE WKF_PARTITION_NAME = '|| WKF_PARTITION_NAME);
  EXECUTE IMMEDIATE ('DELETE FROM STG_STD_DEMO_DETAILS WHERE WKF_PARTITION_NAME = '|| WKF_PARTITION_NAME);
  EXECUTE IMMEDIATE ('DELETE FROM STG_STD_BIO_DETAILS WHERE WKF_PARTITION_NAME = '|| WKF_PARTITION_NAME);
    END IF;
  COMMIT;  
  
END SP_DELETE_STG_TABLES;
/