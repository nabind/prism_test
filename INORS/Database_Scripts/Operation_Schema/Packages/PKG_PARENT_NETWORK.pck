CREATE OR REPLACE PACKAGE PKG_PARENT_NETWORK IS

  TYPE GET_REFCURSOR IS REF CURSOR;
  PROCEDURE SP_GET_STUDENT_DETAILS(P_IN_PARENT_NAME          IN USERS.USERNAME%TYPE,
                                   P_OUT_CUR_STUDENT_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_SUBTEST_DETAILS(P_IN_TESTELEMENTID            IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                   P_OUT_CUR_SUBTEST_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_ARTICLE_TYPE_DETAILS(P_IN_STUDENT_BIO_ID        IN NUMBER,
                                        P_IN_SUBTESTID             IN NUMBER,
                                        P_IN_GRADEID               IN NUMBER,
                                        P_IN_CATEGORY_TYPE         IN VARCHAR2,
                                        P_OUT_CUR_STANDARD_DETAILS OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2);

  PROCEDURE SP_GET_ARTICLE_DESCRIPTION(P_IN_CUST_PROD_ID         IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_IN_STUDENT_BIO_ID       IN NUMBER,
                                       P_IN_ID                   IN NUMBER,
                                       P_IN_GRADEID              IN GRADE_DIM.GRADEID%TYPE,
                                       P_IN_SUBTESTID            IN SUBTEST_DIM.SUBTESTID%TYPE,
                                       P_IN_CATEGORY_TYPE        IN VARCHAR2,
                                       P_OUT_CUR_ART_DESCRIPTION OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_GRADE_SUBTEST_DETAILS(P_IN_CUSTOMERID             IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                         P_OUT_CUR_GRADE_SUBTEST_DET OUT GET_REFCURSOR,
                                         P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2);

END PKG_PARENT_NETWORK;
/
CREATE OR REPLACE PACKAGE BODY PKG_PARENT_NETWORK IS

  -------------------------
  PROCEDURE SP_GET_STUDENT_DETAILS(P_IN_PARENT_NAME          IN USERS.USERNAME%TYPE,
                                   P_OUT_CUR_STUDENT_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
  
  BEGIN
  
    P_OUT_CUR_STUDENT_DETAILS := NULL;
  
    OPEN P_OUT_CUR_STUDENT_DETAILS FOR
    
    SELECT DISTINCT IC.TEST_ELEMENT_ID AS TEST_ELEMENT_ID,
                    IC.STUDENT_FULL_NAME AS STUDENT_NAME,
                    GRD.GRADEID AS STUDENT_GRADEID,
                    GRD.GRADE_NAME AS STUDENT_GRADE,
                    LINK.CUST_PROD_ID ADMINID,
                    AD.ADMIN_SEASON || ' ' || AD.ADMIN_YEAR AS ADMIN_SEASON_YEAR,
                    AD.ADMIN_SEQ,
                    NVL((SELECT 1
                          FROM STUDENT_BIO_DIM
                         WHERE TEST_ELEMENT_ID = IC.TEST_ELEMENT_ID),
                        0) BIO_EXISTS,
                NVL(IC.STUDENT_BIO_ID, 0) STUDENT_BIO_ID
      FROM USERS                 U,
           ORG_USERS             OU,
           INVITATION_CODE_CLAIM ICC,
           INVITATION_CODE       IC,
           CUST_PRODUCT_LINK     LINK,
           ADMIN_DIM             AD,
           GRADE_DIM             GRD
     WHERE UPPER(U.USERNAME) = UPPER(P_IN_PARENT_NAME)
       AND OU.USERID = U.USERID
       AND OU.ORG_NODE_LEVEL = 3
       AND OU.ORG_USER_ID = ICC.ORG_USER_ID
       AND ICC.ACTIVATION_STATUS = 'AC'
       AND IC.ACTIVATION_STATUS = 'AC'
       AND IC.ICID = ICC.ICID
       AND IC.CUST_PROD_ID = LINK.CUST_PROD_ID
       AND LINK.ADMINID = AD.ADMINID
       AND IC.GRADE_ID = GRD.GRADEID
     ORDER BY AD.ADMIN_SEQ;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_STUDENT_DETAILS;

  ----------------------------------
  PROCEDURE SP_GET_SUBTEST_DETAILS(P_IN_TESTELEMENTID            IN STUDENT_BIO_DIM.TEST_ELEMENT_ID%TYPE,
                                   P_OUT_CUR_SUBTEST_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
  
  BEGIN
  
    P_OUT_CUR_SUBTEST_DETAILS := NULL;
  
    OPEN P_OUT_CUR_SUBTEST_DETAILS FOR
    
      SELECT DISTINCT DIM.SUBTESTID     VALUE,
                      DIM.SUBTEST_NAME  NAME,
                      FACT.CUST_PROD_ID OTHER
        FROM STUDENT_BIO_DIM    BIO_DIM,
             SUBTEST_SCORE_FACT FACT,
             SUBTEST_DIM        DIM
       WHERE BIO_DIM.TEST_ELEMENT_ID = P_IN_TESTELEMENTID
         AND BIO_DIM.STUDENT_BIO_ID = FACT.STUDENT_BIO_ID
         AND FACT.SUBTESTID = DIM.SUBTESTID
       ORDER BY DIM.SUBTEST_NAME;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_SUBTEST_DETAILS;

  -------------------------------
  PROCEDURE SP_GET_ARTICLE_TYPE_DETAILS(P_IN_STUDENT_BIO_ID        IN NUMBER,
                                        P_IN_SUBTESTID             IN NUMBER,
                                        P_IN_GRADEID               IN NUMBER,
                                        P_IN_CATEGORY_TYPE         IN VARCHAR2,
                                        P_OUT_CUR_STANDARD_DETAILS OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2) IS
  
  BEGIN
  
    P_OUT_CUR_STANDARD_DETAILS := NULL;
  
    IF P_IN_STUDENT_BIO_ID = 0 THEN
    
      OPEN P_OUT_CUR_STANDARD_DETAILS FOR
        SELECT DISTINCT OBJ.OBJECTIVEID AS STANDARD_ID,
                        OBJ.OBJECTIVE_DESC AS STANDARD_NAME,
                        META.ARTICLEID AS ARTICLEID,
                        META.ARTICLE_NAME AS ARTICLE_NAME,
                        META.SUB_HEADER AS ARTICLE_SUB_HEADER,
                        '-1' AS PROFICENCY_LEVEL,
                        NVL((SELECT ARTICLEID
                              FROM ARTICLE_METADATA
                             WHERE SUBT_OBJ_MAPID = MAP1.SUBT_OBJ_MAPID
                               AND CUST_PROD_ID = META.CUST_PROD_ID
                               AND CATEGORY_TYPE = 'STD'),
                            -1) STD_ARTICLEID,
                        META.CUST_PROD_ID CUST_PROD_ID
          FROM SUBTEST_OBJECTIVE_MAP MAP1,
               SUBTEST_DIM           SUB,
               OBJECTIVE_DIM         OBJ,
               GRADE_DIM             GDIM,
               ARTICLE_METADATA      META
         WHERE GDIM.GRADEID = P_IN_GRADEID
           AND GDIM.GRADEID = META.GRADEID
           AND SUB.SUBTESTID = P_IN_SUBTESTID
           AND SUB.SUBTESTID = META.SUBTESTID
           AND SUB.SUBTESTID = MAP1.SUBTESTID
           AND MAP1.SUBT_OBJ_MAPID = META.SUBT_OBJ_MAPID
           AND MAP1.OBJECTIVEID = OBJ.OBJECTIVEID
           AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         ORDER BY OBJ.OBJECTIVEID, META.ARTICLE_NAME;
    
    ELSE
    
      OPEN P_OUT_CUR_STANDARD_DETAILS FOR
        SELECT DISTINCT OBJ.OBJECTIVEID AS STANDARD_ID,
                        OBJ.OBJECTIVE_DESC AS STANDARD_NAME,
                        META.ARTICLEID AS ARTICLEID,
                        META.ARTICLE_NAME AS ARTICLE_NAME,
                        META.SUB_HEADER AS ARTICLE_SUB_HEADER,
                        OBJ_FACT.PL AS PROFICENCY_LEVEL,
                        NVL((SELECT ARTICLEID
                              FROM ARTICLE_METADATA
                             WHERE SUBT_OBJ_MAPID = MAP1.SUBT_OBJ_MAPID
                               AND CUST_PROD_ID = META.CUST_PROD_ID
                               AND CATEGORY_TYPE = 'STD'),
                            -1) STD_ARTICLEID,
                        META.CUST_PROD_ID CUST_PROD_ID
          FROM STUDENT_BIO_DIM       SBD,
               OBJECTIVE_SCORE_FACT  OBJ_FACT,
               SUBTEST_OBJECTIVE_MAP MAP1,
               OBJECTIVE_DIM         OBJ,
               ARTICLE_METADATA      META
         WHERE SBD.STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID
           AND SBD.STUDENT_BIO_ID = OBJ_FACT.STUDENT_BIO_ID
           AND SBD.GRADEID = OBJ_FACT.GRADEID
           AND OBJ_FACT.GRADEID = META.GRADEID
           AND OBJ_FACT.SUBTESTID = P_IN_SUBTESTID
           AND OBJ_FACT.SUBTESTID = MAP1.SUBTESTID
           AND OBJ_FACT.SUBTESTID = META.SUBTESTID
           AND OBJ_FACT.OBJECTIVEID = MAP1.OBJECTIVEID
           AND MAP1.SUBT_OBJ_MAPID = META.SUBT_OBJ_MAPID
           AND MAP1.OBJECTIVEID = OBJ.OBJECTIVEID
           AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         ORDER BY OBJ.OBJECTIVEID, META.ARTICLE_NAME;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_ARTICLE_TYPE_DETAILS;

  ------------------------------------------
  PROCEDURE SP_GET_ARTICLE_DESCRIPTION(P_IN_CUST_PROD_ID         IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                       P_IN_STUDENT_BIO_ID       IN NUMBER,
                                       P_IN_ID                   IN NUMBER,
                                       P_IN_GRADEID              IN GRADE_DIM.GRADEID%TYPE,
                                       P_IN_SUBTESTID            IN SUBTEST_DIM.SUBTESTID%TYPE,
                                       P_IN_CATEGORY_TYPE        IN VARCHAR2,
                                       P_OUT_CUR_ART_DESCRIPTION OUT GET_REFCURSOR,
                                       P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
  
  BEGIN
  
    P_OUT_CUR_ART_DESCRIPTION := NULL;
  
    IF P_IN_CATEGORY_TYPE = 'STD' THEN
    
      OPEN P_OUT_CUR_ART_DESCRIPTION FOR
        SELECT META.ARTICLEID AS ARTICLEID,
               (SELECT OBJECTIVE_DESC
                  FROM OBJECTIVE_DIM
                 WHERE OBJECTIVEID = CON.OBJECTIVEID) AS ARTICLE_NAME,
               '' AS ARTICLE_SUBHEADER,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
         WHERE META.ARTICLEID = P_IN_ID
           AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY META.ARTICLE_NAME;
    
    ELSIF P_IN_CATEGORY_TYPE = 'ACT' OR P_IN_CATEGORY_TYPE = 'IND' THEN
    
      OPEN P_OUT_CUR_ART_DESCRIPTION FOR
        SELECT META.ARTICLEID      AS ARTICLEID,
               META.ARTICLE_NAME   AS ARTICLE_NAME,
               META.SUB_HEADER     AS ARTICLE_SUBHEADER,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
         WHERE META.ARTICLEID = P_IN_ID
           AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY META.ARTICLE_NAME;
    
    ELSIF P_IN_CATEGORY_TYPE = 'RSC' THEN
    
      IF P_IN_STUDENT_BIO_ID = 0 THEN
      
        OPEN P_OUT_CUR_ART_DESCRIPTION FOR
          SELECT META.ARTICLEID      AS ARTICLEID,
                 META.ARTICLE_NAME   AS ARTICLE_NAME,
                 META.SUB_HEADER     AS ARTICLE_SUBHEADER,
                 CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
            FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
           WHERE META.CATEGORY_TYPE = 'RSC'
             AND META.GRADEID = P_IN_GRADEID
             AND META.SUBTESTID = P_IN_SUBTESTID
             AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
             AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
           ORDER BY META.ARTICLE_NAME;
      
      ELSE
      
        OPEN P_OUT_CUR_ART_DESCRIPTION FOR
          SELECT META.ARTICLEID      AS ARTICLEID,
                 META.ARTICLE_NAME   AS ARTICLE_NAME,
                 META.SUB_HEADER     AS ARTICLE_SUBHEADER,
                 CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
            FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
           WHERE META.CATEGORY_TYPE = 'RSC'
             AND META.GRADEID = P_IN_GRADEID
             AND META.SUBTESTID = P_IN_SUBTESTID
             AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
             AND META.CUST_PROD_ID =
                 (SELECT CUST_PROD_ID
                    FROM RESULTS_GRT_FACT
                   WHERE STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID)
           ORDER BY META.ARTICLE_NAME;
      
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'EDA' OR P_IN_CATEGORY_TYPE = 'ATT' THEN
    
      IF P_IN_STUDENT_BIO_ID = 0 THEN
      
        OPEN P_OUT_CUR_ART_DESCRIPTION FOR
          SELECT META.ARTICLEID      AS ARTICLEID,
                 META.ARTICLE_NAME   AS ARTICLE_NAME,
                 META.SUB_HEADER     AS ARTICLE_SUBHEADER,
                 CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
            FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
           WHERE META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
             AND META.GRADEID = P_IN_GRADEID
             AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
             AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
           ORDER BY META.ARTICLE_NAME;
      
      ELSE
      
        OPEN P_OUT_CUR_ART_DESCRIPTION FOR
          SELECT META.ARTICLEID      AS ARTICLEID,
                 META.ARTICLE_NAME   AS ARTICLE_NAME,
                 META.SUB_HEADER     AS ARTICLE_SUBHEADER,
                 CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
            FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
           WHERE META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
             AND META.GRADEID = P_IN_GRADEID
             AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
             AND META.CUST_PROD_ID =
                 (SELECT CUST_PROD_ID
                    FROM RESULTS_GRT_FACT
                   WHERE STUDENT_BIO_ID = P_IN_STUDENT_BIO_ID)
           ORDER BY META.ARTICLE_NAME;
      
      END IF;
    
    ELSE
      OPEN P_OUT_CUR_ART_DESCRIPTION FOR
        SELECT 'ERROR IN THE PARAMETER ' FROM DUAL;
    
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_ARTICLE_DESCRIPTION;

  ---------------------------------------

  PROCEDURE SP_GET_GRADE_SUBTEST_DETAILS(P_IN_CUSTOMERID             IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                         P_OUT_CUR_GRADE_SUBTEST_DET OUT GET_REFCURSOR,
                                         P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2) IS
  
  BEGIN
  
    P_OUT_CUR_GRADE_SUBTEST_DET := NULL;
  
    OPEN P_OUT_CUR_GRADE_SUBTEST_DET FOR
    
      SELECT GRADE_ID, GRADE_NAME, SUBTEST_ID, SUBTEST_NAME, CUST_PROD_ID
        FROM (SELECT DISTINCT GDIM.GRADE_SEQ,
                              GDIM.GRADEID AS GRADE_ID,
                              GDIM.GRADE_NAME AS GRADE_NAME,
                              DIM.SUBTESTID AS SUBTEST_ID,
                              DIM.SUBTEST_NAME AS SUBTEST_NAME,
                              CUST.CUST_PROD_ID CUST_PROD_ID
                FROM CUST_PRODUCT_LINK CUST,
                     GRADE_LEVEL_MAP GRA_LEV_MAP,
                     SUBTEST_OBJECTIVE_MAP OBJ_MAP,
                     ASSESSMENT_DIM ASSDIM,
                     CONTENT_DIM CON,
                     SUBTEST_DIM DIM,
                     GRADE_DIM GDIM,
                     (SELECT VALUE
                        FROM (SELECT DISTINCT CUST.CUST_PROD_ID VALUE,
                                              P.PRODUCT_SEQ,
                                              DENSE_RANK() OVER(PARTITION BY CUST.CUSTOMERID ORDER BY ADMIN.ADMIN_YEAR DESC NULLS LAST) AS SEQ
                                FROM CUST_PRODUCT_LINK CUST,
                                     PRODUCT P,
                                     (SELECT ADMINID, ADMIN_YEAR
                                        FROM ADMIN_DIM
                                       WHERE ADMIN_YEAR <=
                                             (SELECT ADMIN_YEAR
                                                FROM ADMIN_DIM
                                               WHERE IS_CURRENT_ADMIN = 'Y')) ADMIN
                               WHERE CUST.CUSTOMERID = P_IN_CUSTOMERID
                                 AND CUST.ADMINID = ADMIN.ADMINID
                                 AND CUST.PRODUCTID = P.PRODUCTID
                                 AND P.PRODUCT_NAME LIKE 'ISTEP%') A
                       WHERE A.SEQ <= 1
                       ORDER BY A.SEQ, A.PRODUCT_SEQ DESC) CUST1
              
               WHERE CUST.CUST_PROD_ID = CUST1.VALUE
                 AND CUST.PRODUCTID = ASSDIM.PRODUCTID
                 AND ASSDIM.ASSESSMENTID = CON.ASSESSMENTID
                 AND CON.CONTENTID = DIM.CONTENTID
                 AND GRA_LEV_MAP.LEVEL_MAPID = OBJ_MAP.LEVEL_MAPID
                 AND OBJ_MAP.SUBTESTID = DIM.SUBTESTID
                 AND GRA_LEV_MAP.GRADEID = GDIM.GRADEID
               ORDER BY GDIM.GRADE_SEQ);
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_GRADE_SUBTEST_DETAILS;

--END OF PACKAGE
END PKG_PARENT_NETWORK;
/
