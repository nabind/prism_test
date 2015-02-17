create or replace package PKG_STUDENT_FILE_DOWNLOAD is

  FUNCTION SF_GET_STUDENT_DEMO_VALID(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC;

  FUNCTION SF_GET_STUD_DEMO_VALID_ETHI(P_DEMOCODE       VARCHAR2,
                                       P_STUDENT_BIO_ID NUMBER,
                                       P_CUSTOMERID     IN NUMBER)
    RETURN NUMBER DETERMINISTIC;

  FUNCTION SF_GET_STUDENT_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC;

  FUNCTION SF_GET_SUBTEST_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC;
    
  FUNCTION SF_GET_SUBTEST_DEMO_VALUE_ER(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
  RETURN VARCHAR2 DETERMINISTIC;  

  FUNCTION SF_GET_STUDENT_DEMO_VAL(P_DEMOCODE       VARCHAR2,
                                   P_STUDENT_BIO_ID NUMBER,
                                   P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC;

  FUNCTION SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE   IN VARCHAR2,
                                P_STUDENT_BIO_ID IN NUMBER,
                                p_columntype     IN VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE   IN VARCHAR2,
                                    P_STUDENT_BIO_ID IN NUMBER,
                                    p_columntype     IN VARCHAR2)
    RETURN NUMBER DETERMINISTIC;

  FUNCTION SF_GET_CUST_IDS(P_STUDENT_BIO_ID IN NUMBER,
                           p_columntype     IN VARCHAR2) RETURN NUMBER
    DETERMINISTIC;

  FUNCTION SF_GET_SUBTEST_DATE(P_SUBTEST_CODE   IN VARCHAR2,
                               P_STUDENT_BIO_ID IN NUMBER,
                               p_columntype     IN VARCHAR2) RETURN DATE
    DETERMINISTIC;

  /*FUNCTION SF_GET_objective_SCORE
  (objective_ID NUMBER, P_STUDENT_BIO_ID NUMBER, p_columntype VARCHAR2,p_subtest_id NUMBER)
  RETURN VARCHAR2 DETERMINISTIC;*/

  FUNCTION SF_GET_objective_SCORE(objective_CODE   VARCHAR2,
                                  P_STUDENT_BIO_ID NUMBER,
                                  p_columntype     VARCHAR2,
                                  p_subtest_id     NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION SF_GET_item_SCORE(objective_ID     NUMBER,
                             P_STUDENT_BIO_ID NUMBER,
                             p_columntype     VARCHAR2,
                             p_subtest_id     NUMBER,
                             p_itemsetid      NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION SF_GET_GENDER_CODE(P_GENDER_ID IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION SF_GET_ITEM_CODE(p_itemsetid IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION SF_GET_ITEM_TYPE(p_itemsetid IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION fn_GET_ORG_NAME(P_ORG_NODEID IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION fn_GET_ORG_NODE_CODE(P_ORG_NODEID IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC;

  FUNCTION SF_GET_STUDENT_DEMO_VALUE_TF(P_DEMOCODE       VARCHAR2,
                                        P_DEMO_VALCODE   VARCHAR2,
                                        P_STUDENT_BIO_ID NUMBER,
                                        P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC;

end PKG_STUDENT_FILE_DOWNLOAD;
/
create or replace package body pkg_student_file_download is

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

  FUNCTION SF_GET_STUD_DEMO_VALID_ETHI(P_DEMOCODE       VARCHAR2,
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
    /*        AND EXISTS (SELECT 1 FROM demographic_values b
                           WHERE DEMO_VALUES.demo_valid = b.demo_valid
                           AND  b.demo_value_code <> '<BLANK>' )*/
    ;

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
  END SF_GET_STUD_DEMO_VALID_ETHI;

  FUNCTION SF_GET_STUDENT_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(30);
  BEGIN
    SELECT (SELECT demo_value_code
              FROM demographic_values
             WHERE demo_valid = DEMO_VALUES.DEMO_VALID)
      INTO LV_DEMO_VALID
      FROM STUDENT_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE
                        AND CUSTOMERID = P_CUSTOMERID)
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
  END SF_GET_STUDENT_DEMO_VALUE;

  FUNCTION SF_GET_SUBTEST_DEMO_VALUE(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(30);
  BEGIN
    SELECT (SELECT demo_value_code
              FROM demographic_values
             WHERE demo_valid = DEMO_VALUES.DEMO_VALID)
      INTO LV_DEMO_VALID
      FROM STU_SUBTEST_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE
                        AND CUSTOMERID = P_CUSTOMERID)
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
  END SF_GET_SUBTEST_DEMO_VALUE;
  
  FUNCTION SF_GET_SUBTEST_DEMO_VALUE_ER(P_DEMOCODE       VARCHAR2,
                                     P_STUDENT_BIO_ID NUMBER,
                                     P_CUSTOMERID     IN NUMBER)
   RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(32);
  BEGIN
    SELECT STU.DEMO_VALUE INTO LV_DEMO_VALID
          FROM STU_SUBTEST_DEMO_VALUES STU,
               DEMOGRAPHIC DEM
          WHERE DEM.DEMO_CODE = P_DEMOCODE
          AND DEM.CUSTOMERID = P_CUSTOMERID
          AND DEM.SUBTESTID = STU.SUBTESTID
          AND DEM.DEMOID = STU.DEMOID 
          AND STU.STUDENT_BIO_ID = P_STUDENT_BIO_ID;
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
  END SF_GET_SUBTEST_DEMO_VALUE_ER;

  FUNCTION SF_GET_STUDENT_DEMO_VAL(P_DEMOCODE       VARCHAR2,
                                   P_STUDENT_BIO_ID NUMBER,
                                   P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(32);
  BEGIN
    SELECT DEMO_VALUE
      INTO LV_DEMO_VALID
      FROM STUDENT_DEMO_VALUES DEMO_VALUES
     WHERE DEMOID = (SELECT DEMOID
                       FROM DEMOGRAPHIC DEMO
                      WHERE DEMO.DEMO_CODE = P_DEMOCODE
                        AND CUSTOMERID = P_CUSTOMERID)
       AND DEMO_VALUES.STUDENT_BIO_ID = P_STUDENT_BIO_ID
    /*  AND EXISTS (SELECT 1 FROM demographic_values b
                           WHERE DEMO_VALUES.demo_valid = b.demo_valid
                           AND  b.demo_value_code <> '<BLANK>' )*/
    ;

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
  END SF_GET_STUDENT_DEMO_VAL;

  /*FUNCTION SF_GET_SUBTEST_SCORE
  (SUBTEST_ID NUMBER, P_STUDENT_BIO_ID NUMBER, p_columntype VARCHAR2)
  RETURN VARCHAR2  DETERMINISTIC
  IS
  v_sql VARCHAR2(32000);
  lv_score_value VARCHAR2(40);
  BEGIN

     v_sql:= 'select '|| p_columntype||' from subtest_score_fact where student_bio_id = :P_STUDENT_BIO_ID
              and subtestid=:SUBTEST_ID' ;
     EXECUTE IMMEDIATE     v_sql INTO lv_score_value
     USING            P_STUDENT_BIO_ID ,SUBTEST_ID ;
     RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
         lv_score_value:=NULL;
      RETURN lv_score_value;
  END ; */

  FUNCTION SF_GET_SUBTEST_SCORE(P_SUBTEST_CODE   IN VARCHAR2,
                                P_STUDENT_BIO_ID IN NUMBER,
                                p_columntype     IN VARCHAR2) RETURN VARCHAR2
    DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value VARCHAR2(40);
  BEGIN

    v_sql := 'select ' || p_columntype ||
             ' from subtest_score_fact where student_bio_id = :P_STUDENT_BIO_ID
            and subtestid =(SELECT SUBTESTID FROM SUBTEST_DIM WHERE SUBTEST_CODE = :P_SUBTEST_CODE)';
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID, P_SUBTEST_CODE;
    dbms_output.put_line(v_sql);
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      lv_score_value := NULL;
      RETURN lv_score_value;
  END;

  --added for student roster
  FUNCTION SF_GET_SUBTEST_SCORE_VAL(P_SUBTEST_CODE   IN VARCHAR2,
                                    P_STUDENT_BIO_ID IN NUMBER,
                                    p_columntype     IN VARCHAR2)
    RETURN NUMBER DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value NUMBER;
  BEGIN

    v_sql := 'select ' || p_columntype ||
             ' from subtest_score_fact where student_bio_id = :P_STUDENT_BIO_ID
            and subtestid =(SELECT SUBTESTID FROM SUBTEST_DIM WHERE SUBTEST_CODE = :P_SUBTEST_CODE)';
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID, P_SUBTEST_CODE;
    dbms_output.put_line(v_sql);
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      lv_score_value := NULL;
      RETURN lv_score_value;
  END;

  --added for student roster
  FUNCTION SF_GET_CUST_IDS(P_STUDENT_BIO_ID IN NUMBER,
                           p_columntype     IN VARCHAR2) RETURN NUMBER
    DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value NUMBER;
  BEGIN

    v_sql := 'select DISTINCT ' || p_columntype ||
             ' from subtest_score_fact where student_bio_id = :P_STUDENT_BIO_ID  AND ROWNUM=1';

    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID;
    dbms_output.put_line(v_sql);
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      lv_score_value := NULL;
      RETURN lv_score_value;
  END;

  --added for student roster
  FUNCTION SF_GET_SUBTEST_DATE(P_SUBTEST_CODE   IN VARCHAR2,
                               P_STUDENT_BIO_ID IN NUMBER,
                               p_columntype     IN VARCHAR2) RETURN DATE
    DETERMINISTIC IS
    v_sql         VARCHAR2(32000);
    lv_date_value DATE;
  BEGIN

    v_sql := 'select TO_DATE(' || p_columntype ||
             ') from subtest_score_fact where student_bio_id = :P_STUDENT_BIO_ID
            and subtestid =(SELECT SUBTESTID FROM SUBTEST_DIM WHERE SUBTEST_CODE = :P_SUBTEST_CODE)';
    EXECUTE IMMEDIATE v_sql
      INTO lv_date_value
      USING P_STUDENT_BIO_ID, P_SUBTEST_CODE;
    dbms_output.put_line(v_sql);
    RETURN lv_date_value;
  EXCEPTION
    WHEN OTHERS THEN
      lv_date_value := NULL;
      RETURN lv_date_value;
  END;

  FUNCTION SF_GET_objective_SCORE(objective_CODE   VARCHAR2,
                                  P_STUDENT_BIO_ID NUMBER,
                                  p_columntype     VARCHAR2,
                                  p_subtest_id     NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value VARCHAR2(40);
  BEGIN

    v_sql := 'select ' || p_columntype ||
             ' from objective_score_fact where student_bio_id = :P_STUDENT_BIO_ID
            and subtestid=:p_subtest_id and objectiveid=(SELECT OBJECTIVEID FROM OBJECTIVE_DIM WHERE OBJECTIVE_CODE = :objective_CODE)';
    dbms_output.put_line(v_sql);
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID, p_subtest_id, objective_CODE;
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      lv_score_value := '';
      RETURN lv_score_value;
  END;
  FUNCTION SF_GET_item_SCORE(objective_ID     NUMBER,
                             P_STUDENT_BIO_ID NUMBER,
                             p_columntype     VARCHAR2,
                             p_subtest_id     NUMBER,
                             p_itemsetid      NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    v_sql          VARCHAR2(32000);
    lv_score_value VARCHAR2(400);
  BEGIN

    v_sql := 'select ' || p_columntype ||
             ' from item_score_fact where student_bio_id = :P_STUDENT_BIO_ID
            and subtestid=:p_subtest_id and nvl(objectiveid,1)=:objective_ID and itemsetid =:p_itemsetid';
    dbms_output.put_line(v_sql);
    EXECUTE IMMEDIATE v_sql
      INTO lv_score_value
      USING P_STUDENT_BIO_ID, p_subtest_id, objective_ID, p_itemsetid;
    RETURN lv_score_value;
  EXCEPTION
    WHEN OTHERS THEN
      lv_score_value := '';
      RETURN lv_score_value;
  END;

  FUNCTION SF_GET_GENDER_CODE(P_GENDER_ID IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    lv_gender_code VARCHAR2(30);

  BEGIN
    SELECT gender_code
      INTO lv_gender_code
      FROM gender_dim
     WHERE genderid = P_GENDER_ID;

    RETURN lv_gender_code;

  END;

  FUNCTION SF_GET_ITEM_CODE(p_itemsetid IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    lv_item_code VARCHAR2(30);

  BEGIN
    SELECT item_code
      INTO lv_item_code
      FROM itemset_dim
     WHERE itemsetid = p_itemsetid;

    RETURN lv_item_code;

  END;

  FUNCTION SF_GET_ITEM_TYPE(p_itemsetid IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    lv_item_TYPE VARCHAR2(30);

  BEGIN
    SELECT item_type
      INTO lv_item_TYPE
      FROM itemset_dim
     WHERE itemsetid = p_itemsetid;

    RETURN lv_item_TYPE;

  END;

  FUNCTION FN_GET_ORG_NAME(P_ORG_NODEID IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    LV_ORG_NAME VARCHAR2(400);
  BEGIN
    SELECT ORG_NODE_NAME
      INTO LV_ORG_NAME
      FROM ORG_NODE_DIM
     WHERE ORG_NODEID = P_ORG_NODEID;

    RETURN LV_ORG_NAME;
  EXCEPTION
    WHEN OTHERS THEN
      LV_ORG_NAME := NULL;
      RETURN LV_ORG_NAME;
  END FN_GET_ORG_NAME;

  FUNCTION fn_GET_ORG_NODE_CODE(P_ORG_NODEID IN NUMBER) RETURN VARCHAR2
    DETERMINISTIC IS
    LV_ORG_CODE VARCHAR2(400);
  BEGIN
    SELECT ORG_NODE_CODE
      INTO LV_ORG_CODE
      FROM ORG_NODE_DIM
     WHERE ORG_NODEID = P_ORG_NODEID;

    RETURN LV_ORG_CODE;
  EXCEPTION
    WHEN OTHERS THEN
      LV_ORG_CODE := NULL;
      RETURN LV_ORG_CODE;

  END fn_GET_ORG_NODE_CODE;

  FUNCTION SF_GET_STUDENT_DEMO_VALUE_TF(P_DEMOCODE       VARCHAR2,
                                        P_DEMO_VALCODE   VARCHAR2,
                                        P_STUDENT_BIO_ID NUMBER,
                                        P_CUSTOMERID     IN NUMBER)
    RETURN VARCHAR2 DETERMINISTIC IS
    LV_DEMO_VALID VARCHAR2(30);
  BEGIN
    SELECT (SELECT DEMO_VALUE_CODE
              FROM DEMOGRAPHIC_VALUES
             WHERE DEMO_VALID = SDV.DEMO_VALID)
      INTO LV_DEMO_VALID
      FROM STUDENT_DEMO_VALUES SDV
     WHERE SDV.STUDENT_BIO_ID = P_STUDENT_BIO_ID
       AND EXISTS (SELECT 1
              FROM DEMOGRAPHIC D, DEMOGRAPHIC_VALUES DV
             WHERE D.DEMOID = DV.DEMOID
               AND D.CUSTOMERID = P_CUSTOMERID
               AND D.DEMO_CODE = P_DEMOCODE
               AND DV.DEMO_VALUE_CODE = P_DEMO_VALCODE
               AND D.DEMOID = SDV.DEMOID
               AND DV.DEMO_VALID = SDV.DEMO_VALID);

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
  END SF_GET_STUDENT_DEMO_VALUE_TF;

end pkg_student_file_download;
/
