CREATE OR REPLACE PACKAGE PKG_RPRT_STUD_DETAILS IS

  -- Author  : 353639
  -- Created : 2/18/2015 6:00:33 PM
  -- Purpose : 

 FUNCTION SF_GET_CUSTOMER_ID(P_CUST_PROD_ID  NUMBER,
                             P_ADMINID     NUMBER) RETURN NUMBER DETERMINISTIC RESULT_CACHE;
    
    
 FUNCTION SF_GET_STUDENT_BIO(P_STUDENT_BIO_ID NUMBER,
                             P_CUST_PROD_ID  NUMBER,
                             P_ADMINID       NUMBER,
                             P_GENDERID      NUMBER,
                             P_COLUMNTYPE    VARCHAR2) RETURN VARCHAR2 DETERMINISTIC RESULT_CACHE;
    

 FUNCTION SF_GET_SUBTEST_DETAILS(P_SUBTESTID  NUMBER,
                                 P_COLUMNTYPE VARCHAR2) RETURN VARCHAR2 DETERMINISTIC RESULT_CACHE;
                                  
 FUNCTION SF_GET_DISTRICTID(P_ORG_NODEID    NUMBER, 
                            P_CUST_PROD_ID  NUMBER,
                            P_ADMINID       NUMBER)RETURN NUMBER DETERMINISTIC;

 FUNCTION SF_GET_SCHOOL_NAME_CODE(P_ORG_NODEID    NUMBER,
                                  P_CUST_PROD_ID  NUMBER,
                                  P_ADMINID       NUMBER,
                                  P_COLUMNTYPE    VARCHAR2) RETURN VARCHAR2 DETERMINISTIC RESULT_CACHE;    

 FUNCTION SF_GET_DISTRICT_NAME_CODE(P_ORG_NODEID    NUMBER,
                                    P_CUST_PROD_ID  NUMBER,
                                    P_ADMINID       NUMBER,
                                    P_COLUMNTYPE    VARCHAR2) RETURN VARCHAR2 DETERMINISTIC RESULT_CACHE;    
     
 FUNCTION SF_GET_SUBTEST_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                    P_STUDENT_BIO_ID NUMBER,
                                    P_SUBTESTID      NUMBER,
                                    P_CUST_PROD_ID   NUMBER,
                                    P_ADMINID        NUMBER) RETURN VARCHAR2 DETERMINISTIC;
     
 FUNCTION SF_GET_STUDENT_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                    P_STUDENT_BIO_ID NUMBER,
                                    P_CUST_PROD_ID   NUMBER,
                                    P_ADMINID        NUMBER)RETURN VARCHAR2 DETERMINISTIC; 
                                    

 FUNCTION SF_GET_SUBTEST_DEMO_VALID(P_SUBTESTID      NUMBER,
                                    P_STUDENT_BIO_ID NUMBER,
                                    P_CUSTOMERID     IN NUMBER)RETURN NUMBER DETERMINISTIC;
     

 FUNCTION SF_GET_STUDENT_DEMO_VALID(P_DEMOCODE       VARCHAR2,
                                    P_STUDENT_BIO_ID NUMBER,
                                    P_CUST_PROD_ID   NUMBER,
                                    P_ADMINID        NUMBER)RETURN NUMBER DETERMINISTIC;
    

 FUNCTION SF_GET_SUBTEST_DEMO_EXAMINER(P_SUBTESTID       NUMBER,
                                       P_STUDENT_BIO_ID NUMBER,
                                       P_CUST_PROD_ID   NUMBER,
                                       P_ADMINID        NUMBER)RETURN VARCHAR2 DETERMINISTIC;
    

  FUNCTION SF_GET_OBJECTIVE_LIST(P_STUDENT_BIO_ID NUMBER,
                                 P_SUBTESTID      NUMBER,
                                 P_GRADEID        NUMBER,
                                 P_LEVELID        NUMBER,
                                 P_FORMID         NUMBER,
                                 P_CUST_PROD_ID   NUMBER,
                                 P_ADMINID        NUMBER,
                                 P_COLUMNTYPE     VARCHAR2) RETURN VARCHAR2 DETERMINISTIC RESULT_CACHE;
   

  FUNCTION SF_GET_SCORE_RANGE(P_COLUMNTYPE   VARCHAR2,
                              P_SUBTESTID    NUMBER,
                              P_GRADEID      NUMBER,
                              P_LEVELID      NUMBER,
                              P_CUST_PROD_ID NUMBER) RETURN NUMBER DETERMINISTIC RESULT_CACHE;
    

  FUNCTION SF_GET_OBJECTIVE_SCORE(SUBTEST_CODE     VARCHAR2,
                                  OBJECTIVE_CODE   VARCHAR2,
                                  P_STUDENT_BIO_ID NUMBER,
                                  P_COLUMNTYPE     VARCHAR2,
                                  P_SUBTEST_ID     NUMBER) RETURN NUMBER DETERMINISTIC;
                                  
  FUNCTION SF_GET_STATE_CODE(P_STUDENT_BIO_ID NUMBER) RETURN VARCHAR2 DETERMINISTIC;
  FUNCTION SF_GET_TEST_TYPE(P_STUDENT_BIO_ID NUMBER) RETURN VARCHAR2 DETERMINISTIC; 
    
  FUNCTION SF_GET_STUDENT_DEMO_VAL(P_DEMOCODE       VARCHAR2,
                                   P_STUDENT_BIO_ID NUMBER,
                                   P_CUST_PROD_ID   NUMBER,
                                   P_ADMINID        NUMBER) RETURN VARCHAR2 DETERMINISTIC;
    
    
  FUNCTION SF_GET_SUBTEST_DEMO_VAL(P_DEMOCODE       VARCHAR2,
                                   P_STUDENT_BIO_ID NUMBER,
                                   P_SUBTESTID      NUMBER,
                                   P_CUST_PROD_ID   NUMBER,
                                   P_ADMINID        NUMBER)RETURN VARCHAR2 DETERMINISTIC;
    
    
  FUNCTION SF_GET_ITEM_SCORE(P_SUBTEST_ID     NUMBER,
                             P_STUDENT_BIO_ID NUMBER,
                             P_ITEMSET_NAME   VARCHAR2) RETURN VARCHAR2 DETERMINISTIC;

END PKG_RPRT_STUD_DETAILS;
/
CREATE OR REPLACE PACKAGE BODY PKG_RPRT_STUD_DETAILS is

  FUNCTION SF_GET_CUSTOMER_ID(P_CUST_PROD_ID  NUMBER,
                              P_ADMINID        NUMBER) RETURN NUMBER
   DETERMINISTIC 
   RESULT_CACHE RELIES_ON(CUST_PRODUCT_LINK) 
   IS
   
   V_CUSTOMERID NUMBER;
   BEGIN
   SELECT CUSTOMERID INTO V_CUSTOMERID FROM CUST_PRODUCT_LINK WHERE CUST_PROD_ID = P_CUST_PROD_ID AND ADMINID = P_ADMINID; 
   RETURN V_CUSTOMERID;
   EXCEPTION 
    WHEN OTHERS THEN
    --RAISE;
    RETURN NULL;
   END SF_GET_CUSTOMER_ID;
  
  FUNCTION SF_GET_STUDENT_BIO(P_STUDENT_BIO_ID NUMBER,
                              P_CUST_PROD_ID  NUMBER,
                              P_ADMINID        NUMBER,
                              P_GENDERID       NUMBER,
                              P_COLUMNTYPE     VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC RESULT_CACHE RELIES_ON(STUDENT_BIO_DIM) IS
  
    V_SQL          VARCHAR2(32000);
    LV_SCORE_VALUE VARCHAR2(50);
    V_CUSTOMERID NUMBER;
  BEGIN
   V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
   
    V_SQL := 'SELECT ' || P_COLUMNTYPE ||
             ' FROM STUDENT_BIO_DIM WHERE STUDENT_BIO_ID = :P_STUDENT_BIO_ID
                AND ADMINID =:P_ADMINID
                AND GENDERID = :P_GENDERID 
                AND CUSTOMERID =:V_CUSTOMERID';
                            
    DBMS_OUTPUT.PUT_LINE(V_SQL);
    EXECUTE IMMEDIATE V_SQL
      INTO LV_SCORE_VALUE
      USING P_STUDENT_BIO_ID, P_ADMINID, P_GENDERID, V_CUSTOMERID;
    RETURN LV_SCORE_VALUE;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_SCORE_VALUE := '';
      RETURN LV_SCORE_VALUE;
  END SF_GET_STUDENT_BIO;

  FUNCTION SF_GET_SUBTEST_DETAILS(P_SUBTESTID  NUMBER,
                                  P_COLUMNTYPE VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC RESULT_CACHE RELIES_ON(SUBTEST_DIM) IS
  
    V_SQL          VARCHAR2(32000);
    LV_SCORE_VALUE VARCHAR2(50);
  
  BEGIN
  
    V_SQL := 'SELECT ' || P_COLUMNTYPE ||
             ' FROM SUBTEST_DIM WHERE SUBTESTID = :P_SUBTESTID';
    DBMS_OUTPUT.PUT_LINE(V_SQL);
    EXECUTE IMMEDIATE V_SQL
      INTO LV_SCORE_VALUE
      USING P_SUBTESTID;
    RETURN LV_SCORE_VALUE;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_SCORE_VALUE := '';
      RETURN LV_SCORE_VALUE;
  END SF_GET_SUBTEST_DETAILS;

  FUNCTION SF_GET_DISTRICTID(P_ORG_NODEID NUMBER, 
                             P_CUST_PROD_ID  NUMBER,
                             P_ADMINID       NUMBER)RETURN NUMBER
    DETERMINISTIC IS
    LV_ORGID NUMBER;
    V_CUSTOMERID NUMBER;
  BEGIN
    V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
    SELECT PARENT_ORG_NODEID
      INTO LV_ORGID
      FROM ORG_NODE_DIM
     WHERE ORG_NODEID = P_ORG_NODEID
       AND CUSTOMERID =V_CUSTOMERID;
             
    RETURN LV_ORGID;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_ORGID := NULL;
      RETURN LV_ORGID;
  END SF_GET_DISTRICTID;

  FUNCTION SF_GET_SCHOOL_NAME_CODE(P_ORG_NODEID    NUMBER,
                                   P_CUST_PROD_ID  NUMBER,
                                   P_ADMINID       NUMBER,
                                   P_COLUMNTYPE    VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC RESULT_CACHE RELIES_ON(ORG_NODE_DIM) IS
    LV_ORG_NAME VARCHAR2(100);
    V_CUSTOMERID NUMBER;
  BEGIN
   V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
   IF P_COLUMNTYPE='ORG_NODE_NAME' THEN
        SELECT ORG_NODE_NAME
          INTO LV_ORG_NAME
          FROM ORG_NODE_DIM
         WHERE ORG_NODEID = P_ORG_NODEID
           AND CUSTOMERID = V_CUSTOMERID;
   ELSIF P_COLUMNTYPE='ORG_NODE_CODE' THEN
        SELECT ORG_NODE_CODE
          INTO LV_ORG_NAME
          FROM ORG_NODE_DIM
         WHERE ORG_NODEID = P_ORG_NODEID
           AND CUSTOMERID = V_CUSTOMERID;
   END IF; 
              
    RETURN LV_ORG_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_ORG_NAME := '';
      RETURN LV_ORG_NAME;
  END SF_GET_SCHOOL_NAME_CODE;

  FUNCTION SF_GET_DISTRICT_NAME_CODE(P_ORG_NODEID    NUMBER,
                                     P_CUST_PROD_ID  NUMBER,
                                     P_ADMINID       NUMBER,
                                     P_COLUMNTYPE VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC RESULT_CACHE RELIES_ON(ORG_NODE_DIM) IS
    LV_ORG_NAME VARCHAR2(100);
    V_CUSTOMERID NUMBER; 
  BEGIN
  V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
  
  IF P_COLUMNTYPE='ORG_NODE_NAME' THEN
    SELECT ORG_NODE_NAME
      INTO LV_ORG_NAME
      FROM ORG_NODE_DIM
     WHERE LEVEL = 2
       AND CUSTOMERID = V_CUSTOMERID
     START WITH ORG_NODEID = P_ORG_NODEID
    CONNECT BY NOCYCLE PRIOR PARENT_ORG_NODEID = ORG_NODEID;
  ELSIF P_COLUMNTYPE='ORG_NODE_CODE'  THEN
      SELECT ORG_NODE_CODE
      INTO LV_ORG_NAME
      FROM ORG_NODE_DIM
     WHERE LEVEL = 2
       AND CUSTOMERID = V_CUSTOMERID
     START WITH ORG_NODEID = P_ORG_NODEID
    CONNECT BY NOCYCLE PRIOR PARENT_ORG_NODEID = ORG_NODEID;
   END IF;   
  
    RETURN LV_ORG_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      LV_ORG_NAME := '';
      RETURN LV_ORG_NAME;
  END SF_GET_DISTRICT_NAME_CODE;
  
  
  FUNCTION SF_GET_SUBTEST_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_SUBTESTID      NUMBER,
                                     P_CUST_PROD_ID   NUMBER,
                                     P_ADMINID        NUMBER)RETURN VARCHAR2
    DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(30);
    V_SUBTEST_CODE VARCHAR2(10);
    V_CUSTOMERID NUMBER;
  BEGIN
    V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
    
     SELECT DECODE (SUBTEST_CODE,'01','ELA','02','MA','SCI') INTO  V_SUBTEST_CODE
     FROM SUBTEST_DIM WHERE SUBTESTID = P_SUBTESTID;
     
          
    SELECT (SELECT DEMO_VALUE_CODE
              FROM DEMOGRAPHIC_VALUES
             WHERE DEMO_VALID = DEMO_VALUES.DEMO_VALID)
      INTO LV_DEMO_VALID
      FROM STU_SUBTEST_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE||'_'||V_SUBTEST_CODE
                        AND DEMO.SUBTESTID = P_SUBTESTID
                        AND CUSTOMERID = V_CUSTOMERID)
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
  END SF_GET_SUBTEST_DEMO_VALUE;
  
  
  FUNCTION SF_GET_STUDENT_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUST_PROD_ID   NUMBER,
                                     P_ADMINID        NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(30);
    V_CUSTOMERID NUMBER;
  BEGIN
   V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
   
    SELECT (SELECT DEMO_VALUE_CODE
              FROM DEMOGRAPHIC_VALUES
             WHERE DEMO_VALID = DEMO_VALUES.DEMO_VALID)
      INTO LV_DEMO_VALID
      FROM STUDENT_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE
                        AND CUSTOMERID = V_CUSTOMERID)
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
  END SF_GET_STUDENT_DEMO_VALUE;
  
  
  

  FUNCTION SF_GET_SUBTEST_DEMO_VALID( /*P_DEMOCODE       VARCHAR2,*/P_SUBTESTID      NUMBER,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC IS
    LV_DEMO_VALID NUMBER;
  BEGIN
    SELECT DEMO_VALUES.DEMO_VALUE
      INTO LV_DEMO_VALID
      FROM STU_SUBTEST_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID =
           (SELECT DEMOID
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

  FUNCTION SF_GET_SUBTEST_DEMO_EXAMINER(P_SUBTESTID      NUMBER,
                                        P_STUDENT_BIO_ID NUMBER,
                                        P_CUST_PROD_ID   NUMBER,
                                        P_ADMINID        NUMBER)RETURN VARCHAR2
    DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(100);
   
  
  BEGIN
    SELECT EXAMINERID
      INTO LV_DEMO_VALID
      FROM SUBTEST_SCORE_FACT
     WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND SUBTESTID = P_SUBTESTID
       AND CUST_PROD_ID = P_CUST_PROD_ID
       AND ADMINID = P_ADMINID;
  
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

  FUNCTION SF_GET_OBJECTIVE_LIST(P_STUDENT_BIO_ID NUMBER,
                                 P_SUBTESTID      NUMBER,
                                 P_GRADEID        NUMBER,
                                 P_LEVELID        NUMBER,
                                 P_FORMID         NUMBER,
                                 P_CUST_PROD_ID   NUMBER,
                                 P_ADMINID        NUMBER,
                                 P_COLUMNTYPE     VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC RESULT_CACHE RELIES_ON(OBJECTIVE_SCORE_FACT) IS
  
    LV_OBJECTIVE_IDS VARCHAR2(1000);
  BEGIN
    IF P_COLUMNTYPE = 'OBJECTIVEID' THEN
      SELECT LISTAGG(OBJ.OBJECTIVEID, ',') WITHIN GROUP(ORDER BY OBJ.OBJECTIVE_SEQ)
        INTO LV_OBJECTIVE_IDS
        FROM OBJECTIVE_SCORE_FACT OFT, OBJECTIVE_DIM OBJ
       WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
         AND SUBTESTID = P_SUBTESTID
         AND GRADEID = P_GRADEID
         AND LEVELID = P_LEVELID
         AND FORMID = P_FORMID
         AND CUST_PROD_ID = P_CUST_PROD_ID
         AND ADMINID = P_ADMINID
         AND OFT.OBJECTIVEID = OBJ.OBJECTIVEID;
    
    ELSIF P_COLUMNTYPE = 'PL' THEN
      SELECT LISTAGG(NVL(OFT.PL, -1), ',') WITHIN GROUP(ORDER BY OBJ.OBJECTIVE_SEQ)
        INTO LV_OBJECTIVE_IDS
        FROM OBJECTIVE_SCORE_FACT OFT, OBJECTIVE_DIM OBJ
       WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID
         AND SUBTESTID = P_SUBTESTID
         AND GRADEID = P_GRADEID
         AND LEVELID = P_LEVELID
         AND FORMID = P_FORMID
         AND CUST_PROD_ID = P_CUST_PROD_ID
         AND ADMINID = P_ADMINID
         AND OFT.OBJECTIVEID = OBJ.OBJECTIVEID;
    END IF;
  
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
  END SF_GET_OBJECTIVE_LIST;

  FUNCTION SF_GET_STUDENT_DEMO_VALID(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUST_PROD_ID   NUMBER,
                                     P_ADMINID        NUMBER) RETURN NUMBER
    DETERMINISTIC IS
    LV_DEMO_VALID NUMBER;
    V_CUSTOMERID  NUMBER;
  BEGIN
    V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
  
    SELECT DEMO_VALID
      INTO LV_DEMO_VALID
      FROM STUDENT_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT d.DEMOID
                       FROM DEMOGRAPHIC d
                      WHERE D.DEMO_CODE = P_DEMOCODE
                        AND d.CUSTOMERID = V_CUSTOMERID)
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID
      /* AND EXISTS (SELECT 1
              FROM demographic_values b
             WHERE DEMO_VALUES.demo_valid = b.demo_valid
               AND b.demo_value_code <> '<BLANK>')*/;
  
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

  FUNCTION SF_GET_SCORE_RANGE(P_COLUMNTYPE   VARCHAR2,
                              P_SUBTESTID    NUMBER,
                              P_GRADEID      NUMBER,
                              P_LEVELID      NUMBER,
                              P_CUST_PROD_ID NUMBER) RETURN NUMBER
    DETERMINISTIC RESULT_CACHE RELIES_ON(MV_PROF_LEVEL_SCORE_RANGE) IS
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
      USING P_CUST_PROD_ID, P_SUBTESTID, P_GRADEID, P_LEVELID;
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      lv_score_value := '';
      RETURN lv_score_value;
  END SF_GET_SCORE_RANGE;

  FUNCTION SF_GET_OBJECTIVE_SCORE(subtest_code     VARCHAR2,
                                  objective_CODE   VARCHAR2,
                                  P_STUDENT_BIO_ID NUMBER,
                                  p_columntype     VARCHAR2,
                                  p_subtest_id     NUMBER) RETURN NUMBER
    DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value  NUMBER;
  BEGIN
  
    v_sql := 'SELECT ' || p_columntype ||
             ' FROM OBJECTIVE_SCORE_FACT WHERE STUDENT_BIO_ID = :P_STUDENT_BIO_ID
            AND SUBTESTID=:p_subtest_id 
            AND OBJECTIVEID IN (SELECT OBJECTIVEID FROM OBJECTIVE_DIM WHERE OBJECTIVE_CODE = :objective_CODE)
            AND EXISTS (SELECT 1 FROM SUBTEST_DIM WHERE SUBTESTID =:p_subtest_id AND SUBTEST_CODE=:subtest_code)';
    dbms_output.put_line(v_sql);
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID, p_subtest_id, objective_CODE, p_subtest_id, subtest_code;
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      --RAISE;
      lv_score_value := '';
      RETURN lv_score_value;
  END SF_GET_OBJECTIVE_SCORE;
  
---*************NEW************----------------------  
  
FUNCTION SF_GET_STATE_CODE(P_STUDENT_BIO_ID NUMBER) RETURN VARCHAR2
  DETERMINISTIC IS

  V_STATE_CODE VARCHAR2(10);

BEGIN

  SELECT CUSTOMER_CODE
    INTO V_STATE_CODE
    FROM CUSTOMER_INFO
   WHERE CUSTOMERID =
         (SELECT CUSTOMERID
            FROM STUDENT_BIO_DIM
           WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID);
  RETURN V_STATE_CODE;

END SF_GET_STATE_CODE;

FUNCTION SF_GET_TEST_TYPE(P_STUDENT_BIO_ID NUMBER) RETURN VARCHAR2
  DETERMINISTIC IS

  V_TP_CODE VARCHAR2(100);

BEGIN

  SELECT TP_CODE
    INTO V_TP_CODE
    FROM TEST_PROGRAM
   WHERE CUSTOMERID =
         (SELECT CUSTOMERID
            FROM STUDENT_BIO_DIM
           WHERE STUDENT_BIO_ID = P_STUDENT_BIO_ID)
     AND ROWNUM =1;
  RETURN V_TP_CODE;

END SF_GET_TEST_TYPE;

--RETURN STUDENT LEVEL DEMO WHICH HAVE IS_DEMO_AVL = 'N'
FUNCTION SF_GET_STUDENT_DEMO_VAL(P_DEMOCODE       VARCHAR2,
                                 P_STUDENT_BIO_ID NUMBER,
                                 P_CUST_PROD_ID  NUMBER,
                                 P_ADMINID       NUMBER)RETURN VARCHAR2 
    DETERMINISTIC IS
    
    LV_DEMO_VAL VARCHAR2(100);
    V_CUSTOMERID NUMBER;
  BEGIN
     V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
      
              
    SELECT DEMO_VALUES.DEMO_VALUE
      INTO LV_DEMO_VAL
      FROM STUDENT_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE
                        AND CUSTOMERID = V_CUSTOMERID)
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID;

    RETURN LV_DEMO_VAL;
    
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      LV_DEMO_VAL := NULL;
      RETURN LV_DEMO_VAL;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_DEMO_VAL := NULL;
      RETURN LV_DEMO_VAL;
  END SF_GET_STUDENT_DEMO_VAL;
  
  
   --RETURN SUBTEST LEVEL DEMO WHICH HAVE IS_DEMO_AVL = 'N'
  FUNCTION SF_GET_SUBTEST_DEMO_VAL(P_DEMOCODE       VARCHAR2,
                                   P_STUDENT_BIO_ID NUMBER,
                                   P_SUBTESTID      NUMBER,
                                   P_CUST_PROD_ID   NUMBER,
                                   P_ADMINID        NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VAL VARCHAR2(100);
    V_CUSTOMERID NUMBER;
    V_SUBTEST_CODE VARCHAR2(10);
  BEGIN
     V_CUSTOMERID := SF_GET_CUSTOMER_ID(P_CUST_PROD_ID,P_ADMINID);
      
     SELECT DECODE (SUBTEST_CODE,'01','ELA','02','MA','SCI') INTO  V_SUBTEST_CODE
     FROM SUBTEST_DIM WHERE SUBTESTID = P_SUBTESTID;
     
          
    SELECT DEMO_VALUES.DEMO_VALUE
      INTO LV_DEMO_VAL
      FROM STU_SUBTEST_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE||'_'||V_SUBTEST_CODE
                        AND DEMO.SUBTESTID = P_SUBTESTID
                        AND CUSTOMERID = V_CUSTOMERID)
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID;

    RETURN LV_DEMO_VAL;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      LV_DEMO_VAL := NULL;
      RETURN LV_DEMO_VAL;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_DEMO_VAL := NULL;
      RETURN LV_DEMO_VAL;
  END SF_GET_SUBTEST_DEMO_VAL;
  
  
  
  FUNCTION SF_GET_ITEM_SCORE(P_SUBTEST_ID     NUMBER,
                             P_STUDENT_BIO_ID NUMBER,
                             P_ITEMSET_NAME   VARCHAR2) 
                               
    RETURN VARCHAR2 DETERMINISTIC IS
    
    LV_ITEM_SCORE VARCHAR2(100);
    LV_ITEM_ID    NUMBER;
    
    BEGIN
    
    SELECT I.ITEMSETID 
      INTO LV_ITEM_ID
      FROM ITEMSET_DIM I
     WHERE I.SUBTESTID = P_SUBTEST_ID
       AND I.ITEM_NAME LIKE P_ITEMSET_NAME ||'%';
    
    SELECT SCORE_VALUES INTO LV_ITEM_SCORE
      FROM ITEM_SCORE_FACT ITEM
     WHERE ITEM.STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND ITEM.SUBTESTID      = P_SUBTEST_ID
       AND ITEM.ITEMSETID      = LV_ITEM_ID;
  
  
    RETURN LV_ITEM_SCORE;
    
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
      LV_ITEM_SCORE := NULL;
      RETURN LV_ITEM_SCORE;
      --RETURN '';
    WHEN OTHERS THEN
      -- Consider logging the error and then re-raise
      LV_ITEM_SCORE := NULL;
      RETURN LV_ITEM_SCORE;
      
  END SF_GET_ITEM_SCORE;


  

END PKG_RPRT_STUD_DETAILS;
/
