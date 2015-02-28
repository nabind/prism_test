/******************APPLY THIS IN THE QA SCHEMA******************************************/
CREATE TABLE GRW_USERS_2013 AS 
SELECT U.* 
FROM USERS U, ORG_USERS O 
WHERE U.USERID = O.USERID 
AND O.ADMINID = 101 
AND U.USER_TYPE IN ('GRW','GRW_P') ;
 --SELECT COUNT (1) FROM GRW_USERS_2013; ----12838
GRANT SELECT ON GRW_USERS_2013 TO ISTEP_MASTER;
----------------------------------------------------------------------
CREATE TABLE GRW_ORG_USERS_2013 
AS 
SELECT O.* 
FROM USERS U, ORG_USERS O 
WHERE U.USERID = O.USERID 
AND O.ADMINID = 101 
AND U.USER_TYPE IN ('GRW','GRW_P') ;
--SELECT COUNT (1) FROM GRW_ORG_USERS_2013; ----12838
GRANT SELECT ON GRW_ORG_USERS_2013 TO ISTEP_MASTER;
----------------------------------------------------------------------
CREATE TABLE GRW_USER_SELECTION_LOOKUP_2013 
AS
SELECT * FROM USER_SELECTION_LOOKUP  
WHERE CUST_PROD_ID =5001 
AND USERID IN (SELECT USERID FROM GRW_ORG_USERS_2013);
--SELECT COUNT (1) FROM GRW_USER_SELECTION_LOOKUP_2013; ----23919
GRANT SELECT ON GRW_USER_SELECTION_LOOKUP_2013 TO ISTEP_MASTER;
----------------------------------------------------------------------
CREATE TABLE GRW_USC_LINK_2013
AS 
SELECT ULK.ORG_USER_ID,
       ULK.CUST_PROD_ID,
       ULK.STUDENT_BIO_ID,
       (SELECT STD.TEST_ELEMENT_ID FROM STUDENT_BIO_DIM STD WHERE STD.STUDENT_BIO_ID= ULK.STUDENT_BIO_ID ) AS TEST_ELEMENT_ID,
       ULK.SUBTESTID,
       ULK.DATETIMESTAMP 
FROM USC_LINK ULK
WHERE CUST_PROD_ID = 5001
AND ORG_USER_ID IN (SELECT ORG_USER_ID FROM GRW_ORG_USERS_2013);
--SELECT COUNT (1) FROM GRW_USC_LINK_2013; ----785905
GRANT SELECT ON GRW_USC_LINK_2013 TO ISTEP_MASTER;
---------------------------------------------------------------------
CREATE TABLE GRW_PERF_MATRIX_FACT_2013
AS 
SELECT PERF_MATRIX_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       STUDENT_NAME,
       CURR_STUDENT_BIO_ID,
       (SELECT STD.TEST_ELEMENT_ID FROM STUDENT_BIO_DIM STD WHERE STD.STUDENT_BIO_ID= CURR_STUDENT_BIO_ID ) AS CURR_TEST_ELEMENT_ID,
       PREV_STUDENT_BIO_ID,
       (SELECT STD.TEST_ELEMENT_ID FROM STUDENT_BIO_DIM STD WHERE STD.STUDENT_BIO_ID= PREV_STUDENT_BIO_ID ) AS PREV_TEST_ELEMENT_ID,
       SUBTESTID,
       CURR_GRADEID,
       PREV_GRADEID,
       CURR_ADMINID,
       PREV_ADMINID,
       CURR_SC,
       PREV_SC,
       USER_ID,
       PERF_LEVEL,
       DATETIMESTAMP FROM PERF_MATRIX_FACT 
WHERE CUST_PROD_ID = 5001
AND USER_ID IN (SELECT DISTINCT USERID FROM GRW_USER_SELECTION_LOOKUP_2013);
--SELECT COUNT (1) FROM GRW_PERF_MATRIX_FACT_2013; ----729243
GRANT SELECT ON GRW_PERF_MATRIX_FACT_2013 TO ISTEP_MASTER;
--------------------------------------------------------------------- 

