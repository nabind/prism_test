create or replace package PKG_RPRT_STUD_DETAILS is

  -- Author  : 353639
  -- Created : 2/18/2015 6:00:33 PM
  -- Purpose : 
  
   FUNCTION SF_GET_STUDENT_BIO(     P_STUDENT_BIO_ID NUMBER,
                                  P_CUSTOMERID NUMBER,
                                  P_ADMINID NUMBER,
                                  P_GRADEID NUMBER,
                                  P_GENDERID NUMBER,
                                  P_COLUMNTYPE     VARCHAR2) 
    RETURN VARCHAR2
    DETERMINISTIC
    RESULT_CACHE ;
    
    FUNCTION SF_GET_DISTRICTID(     P_ORG_NODEID NUMBER,
                                  P_CUSTOMERID NUMBER) 
    RETURN NUMBER
    DETERMINISTIC  ;
    
    FUNCTION SF_GET_SCHOOL_NAME(    P_ORG_NODEID NUMBER,
                                  P_CUSTOMERID NUMBER) 
    RETURN VARCHAR2
    DETERMINISTIC
    RESULT_CACHE;
    
    FUNCTION SF_GET_DISTRICT_NAME(    P_ORG_NODEID NUMBER,
                                  P_CUSTOMERID NUMBER) 
    RETURN VARCHAR2
    DETERMINISTIC
    RESULT_CACHE;
  
  FUNCTION SF_GET_SUBTEST_DEMO_VALID(  /*P_DEMOCODE       VARCHAR2,*/
                                     P_SUBTESTID   NUMBER,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC;

 FUNCTION SF_GET_STUDENT_DEMO_VALID(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC;
    
 FUNCTION SF_GET_SUBTEST_DEMO_EXAMINER(  /*P_DEMOCODE       VARCHAR2,*/
                                     P_SUBTESTID   NUMBER,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC ;
    
 FUNCTION SF_GET_OBJECTIVEIDS( P_STUDENT_BIO_ID NUMBER,
                              P_SUBTESTID   NUMBER,
                              P_GRADEID NUMBER,
                              P_LEVELID NUMBER,
                              P_FORMID NUMBER,
                              P_CUST_PROD_ID NUMBER,
                              P_ADMINID NUMBER)
    RETURN VARCHAR2 
    DETERMINISTIC
    RESULT_CACHE ;
    
 FUNCTION SF_GET_SCORE_RANGE(P_COLUMNTYPE VARCHAR2,
                              P_SUBTESTID   NUMBER,
                              P_GRADEID NUMBER,
                              P_LEVELID NUMBER,
                              P_CUST_PROD_ID NUMBER
                              ) RETURN NUMBER
    DETERMINISTIC 
    RESULT_CACHE;    
    
FUNCTION SF_GET_OBJECTIVE_SCORE(  subtest_code VARCHAR2,
                                  objective_CODE   VARCHAR2,
                                  P_STUDENT_BIO_ID NUMBER,
                                  p_columntype     VARCHAR2,
                                  p_subtest_id     NUMBER)
    RETURN VARCHAR2 DETERMINISTIC ;
    

end PKG_RPRT_STUD_DETAILS;
/
CREATE OR REPLACE PACKAGE BODY PKG_RPRT_STUD_DETAILS is

 FUNCTION SF_GET_STUDENT_BIO(     P_STUDENT_BIO_ID NUMBER,
                                  P_CUSTOMERID NUMBER,
                                  P_ADMINID NUMBER,
                                  P_GRADEID NUMBER,
                                  P_GENDERID NUMBER,
                                  P_COLUMNTYPE     VARCHAR2) 
    RETURN VARCHAR2
    DETERMINISTIC
    RESULT_CACHE RELIES_ON (STUDENT_BIO_DIM)
    IS
    
    V_SQL          VARCHAR2(32000);
    LV_SCORE_VALUE VARCHAR2(50);
    
  BEGIN

    V_SQL := 'SELECT ' || P_COLUMNTYPE ||
             ' FROM STUDENT_BIO_DIM WHERE STUDENT_BIO_ID = :P_STUDENT_BIO_ID
                AND GRADEID=:P_GRADEID 
                AND ADMINID =:P_ADMINID
                AND GENDERID = :P_GENDERID 
                AND CUSTOMERID =:P_CUSTOMERID';
    DBMS_OUTPUT.PUT_LINE(V_SQL);
    EXECUTE IMMEDIATE V_SQL
      INTO LV_SCORE_VALUE
      USING P_STUDENT_BIO_ID, P_GRADEID, P_ADMINID,P_GENDERID,P_CUSTOMERID;
    RETURN LV_SCORE_VALUE;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_SCORE_VALUE := '';
      RETURN LV_SCORE_VALUE;
  END SF_GET_STUDENT_BIO;
  
 
  FUNCTION SF_GET_DISTRICTID(     P_ORG_NODEID NUMBER,
                                  P_CUSTOMERID NUMBER) 
    RETURN NUMBER
    DETERMINISTIC
    IS
    LV_ORGID NUMBER;
    
  BEGIN

     SELECT   PARENT_ORG_NODEID
      INTO LV_ORGID
      FROM ORG_NODE_DIM
     WHERE ORG_NODEID = P_ORG_NODEID
       AND CUSTOMERID = P_CUSTOMERID;
       
    RETURN LV_ORGID;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_ORGID := NULL;
      RETURN LV_ORGID;
  END SF_GET_DISTRICTID;
  
  FUNCTION SF_GET_SCHOOL_NAME(    P_ORG_NODEID NUMBER,
                                  P_CUSTOMERID NUMBER) 
    RETURN VARCHAR2
    DETERMINISTIC
    RESULT_CACHE RELIES_ON (ORG_NODE_DIM)
    IS
    LV_ORG_NAME VARCHAR2(100);
    
  BEGIN

     SELECT   ORG_NODE_NAME
      INTO LV_ORG_NAME
      FROM ORG_NODE_DIM
     WHERE ORG_NODEID = P_ORG_NODEID
       AND CUSTOMERID = P_CUSTOMERID;
    RETURN LV_ORG_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_ORG_NAME := '';
      RETURN LV_ORG_NAME;
  END SF_GET_SCHOOL_NAME;
  
  FUNCTION SF_GET_DISTRICT_NAME(    P_ORG_NODEID NUMBER,
                                  P_CUSTOMERID NUMBER) 
    RETURN VARCHAR2
    DETERMINISTIC
    RESULT_CACHE RELIES_ON (ORG_NODE_DIM)
    IS
    LV_ORG_NAME VARCHAR2(100);
    
  BEGIN

  SELECT ORG_NODE_NAME INTO LV_ORG_NAME
  FROM ORG_NODE_DIM WHERE LEVEL =2 AND CUSTOMERID = P_CUSTOMERID
  START WITH ORG_NODEID = P_ORG_NODEID 
  CONNECT BY NOCYCLE PRIOR PARENT_ORG_NODEID = ORG_NODEID;
  
    RETURN LV_ORG_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_ORG_NAME := '';
      RETURN LV_ORG_NAME;
  END SF_GET_DISTRICT_NAME;
  

FUNCTION SF_GET_SUBTEST_DEMO_VALID(  /*P_DEMOCODE       VARCHAR2,*/
                                     P_SUBTESTID   NUMBER,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC IS
    LV_DEMO_VALID NUMBER;
  BEGIN
    SELECT   DEMO_VALUES.DEMO_VALUE
      INTO LV_DEMO_VALID
      FROM STU_SUBTEST_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.SUBTESTID = P_SUBTESTID --P_DEMOCODE
                        AND CUSTOMERID = P_CUSTOMERID
                        AND DEMO.DEMO_CODE LIKE 'Eductr_First_Nm_%')
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND EXISTS (SELECT 1
              FROM DEMOGRAPHIC_VALUES B
             WHERE DEMO_VALUES.DEMO_VALID = B.DEMO_VALID
               AND B.DEMO_VALUE_CODE <> '<BLANK>');

    RETURN LV_DEMO_VALID;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      LV_DEMO_VALID := NULL;
      RETURN LV_DEMO_VALID;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_DEMO_VALID := NULL;
      RETURN LV_DEMO_VALID;
  END SF_GET_SUBTEST_DEMO_VALID;
  
  FUNCTION SF_GET_SUBTEST_DEMO_EXAMINER(  /*P_DEMOCODE       VARCHAR2,*/
                                     P_SUBTESTID   NUMBER,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(100);
  BEGIN
    SELECT  DEMO_VALUES.DEMO_VALUE
      INTO LV_DEMO_VALID
      FROM STU_SUBTEST_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.SUBTESTID = P_SUBTESTID --P_DEMOCODE
                        AND CUSTOMERID = P_CUSTOMERID
                        AND DEMO.DEMO_CODE LIKE 'Eductr_First_Nm_%')
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND EXISTS (SELECT 1
              FROM DEMOGRAPHIC_VALUES B
             WHERE DEMO_VALUES.DEMO_VALID = B.DEMO_VALID
               AND B.DEMO_VALUE_CODE <> '<BLANK>');

    RETURN LV_DEMO_VALID;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      LV_DEMO_VALID := NULL;
      RETURN LV_DEMO_VALID;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_DEMO_VALID := NULL;
      RETURN LV_DEMO_VALID;
  END SF_GET_SUBTEST_DEMO_EXAMINER;
 

FUNCTION SF_GET_OBJECTIVEIDS( P_STUDENT_BIO_ID NUMBER,
                              P_SUBTESTID   NUMBER,
                              P_GRADEID NUMBER,
                              P_LEVELID NUMBER,
                              P_FORMID NUMBER,
                              P_CUST_PROD_ID NUMBER,
                              P_ADMINID NUMBER)
    RETURN VARCHAR2 
    DETERMINISTIC
    RESULT_CACHE RELIES_ON (OBJECTIVE_SCORE_FACT)
    IS
    
    LV_OBJECTIVE_IDS VARCHAR2(1000);
  BEGIN
    SELECT LISTAGG(OBJ.OBJECTIVEID, ',') WITHIN GROUP (ORDER BY OBJ.OBJECTIVE_SEQ) 
       INTO LV_OBJECTIVE_IDS
    FROM OBJECTIVE_SCORE_FACT OFT,OBJECTIVE_DIM OBJ 
    WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
    AND SUBTESTID = P_SUBTESTID
    AND GRADEID = P_GRADEID
    AND LEVELID = P_LEVELID
    AND FORMID=P_FORMID
    AND CUST_PROD_ID = P_CUST_PROD_ID
    AND ADMINID = P_ADMINID
    AND OFT.OBJECTIVEID= OBJ.OBJECTIVEID;

    RETURN LV_OBJECTIVE_IDS;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      LV_OBJECTIVE_IDS := NULL;
      RETURN LV_OBJECTIVE_IDS;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_OBJECTIVE_IDS := NULL;
      RETURN LV_OBJECTIVE_IDS;
  END SF_GET_OBJECTIVEIDS; 
  
 FUNCTION SF_GET_STUDENT_DEMO_VALID(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC IS
    LV_DEMO_VALID NUMBER;
  BEGIN
    SELECT DEMO_VALID
      INTO LV_DEMO_VALID
      FROM STUDENT_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT d.DEMOID
                       FROM DEMOGRAPHIC d
                      WHERE D.DEMO_CODE = P_DEMOCODE
                        AND d.CUSTOMERID = P_CUSTOMERID)
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND EXISTS (SELECT 1
              FROM demographic_values b
             WHERE DEMO_VALUES.demo_valid = b.demo_valid
               AND b.demo_value_code <> '<BLANK>');

    RETURN LV_DEMO_VALID;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN

      LV_DEMO_VALID := NULL;
      RETURN LV_DEMO_VALID;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_DEMO_VALID := NULL;
      RETURN LV_DEMO_VALID;
  END SF_GET_STUDENT_DEMO_VALID;
  
   FUNCTION SF_GET_SCORE_RANGE(P_COLUMNTYPE VARCHAR2,
                              P_SUBTESTID   NUMBER,
                              P_GRADEID NUMBER,
                              P_LEVELID NUMBER,
                              P_CUST_PROD_ID NUMBER
                              ) RETURN NUMBER
    DETERMINISTIC 
    RESULT_CACHE RELIES_ON(MV_PROF_LEVEL_SCORE_RANGE)
    IS
    v_sql          VARCHAR2(32000);
    lv_score_value NUMBER;
  BEGIN

    v_sql := 'SELECT ' || P_COLUMNTYPE ||
             ' FROM MV_PROF_LEVEL_SCORE_RANGE 
             WHERE CUST_PROD_ID = :P_CUST_PROD_ID
               AND SUBTESTID=:P_SUBTESTID 
               AND GRADEID=:P_GRADEID
               AND LEVELID =:P_LEVELID';
               
    dbms_output.put_line(v_sql);
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_CUST_PROD_ID, P_SUBTESTID, P_GRADEID,P_LEVELID;
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      lv_score_value := '';
      RETURN lv_score_value;
  END SF_GET_SCORE_RANGE;
  
   FUNCTION SF_GET_OBJECTIVE_SCORE( subtest_code VARCHAR2,
                                  objective_CODE   VARCHAR2,
                                  P_STUDENT_BIO_ID NUMBER,
                                  p_columntype     VARCHAR2,
                                  p_subtest_id     NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value VARCHAR2(40);
  BEGIN

    v_sql := 'select ' || p_columntype ||
             ' from objective_score_fact where student_bio_id = :P_STUDENT_BIO_ID
            and subtestid=:p_subtest_id 
            and objectiveid in (SELECT OBJECTIVEID FROM OBJECTIVE_DIM WHERE OBJECTIVE_CODE = :objective_CODE)
            and EXISTS (SELECT 1 FROM SUBTEST_DIM WHERE SUBTESTID =:p_subtest_id AND SUBTEST_CODE=:subtest_code)';
    dbms_output.put_line(v_sql);
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID, p_subtest_id, objective_CODE,p_subtest_id,subtest_code;
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      lv_score_value := '';
      RETURN lv_score_value;
  END SF_GET_OBJECTIVE_SCORE;
  
END PKG_RPRT_STUD_DETAILS;
/
