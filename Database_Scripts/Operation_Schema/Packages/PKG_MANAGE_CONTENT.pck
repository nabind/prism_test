CREATE OR REPLACE PACKAGE PKG_MANAGE_CONTENT AS

  TYPE GET_REFCURSOR IS REF CURSOR;
  PROCEDURE SP_GET_CUST_PROD_DETAILS(P_IN_CUSTOMERID             IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                     P_IN_ORG_NODEID             IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                     P_OUT_CUR_CUST_PROD_DETAILS OUT GET_REFCURSOR,
                                     P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2);

  PROCEDURE SP_GET_GRADE_DETAILS(P_IN_CUST_PROD_ID       IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                 P_OUT_CUR_GRADE_DETAILS OUT GET_REFCURSOR,
                                 P_OUT_EXCEP_ERR_MSG     OUT VARCHAR2);

  PROCEDURE SP_GET_SUBTEST_DETAILS(P_IN_CUST_PROD_ID         IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_GRADEID              IN GRADE_DIM.GRADEID%TYPE,
                                   P_OUT_CUR_SUBTEST_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2);

  PROCEDURE SP_GET_OBJECTIVE_DETAILS(P_IN_SUBTESTID              IN SUBTEST_DIM.SUBTESTID%TYPE,
                                     P_IN_GRADEID                IN GRADE_DIM.GRADEID%TYPE,
                                     P_OUT_CUR_OBJECTIVE_DETAILS OUT GET_REFCURSOR,
                                     P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2);

  PROCEDURE SP_GET_PERFORMANCE_LEVEL(P_IN_CUST_PROD_ID           IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                     P_IN_CONTENT_TYPE_ID        IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                     P_OUT_CUR_OBJECTIVE_DETAILS OUT GET_REFCURSOR,
                                     P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2);

  PROCEDURE SP_ADD_NEW_CONTENT(P_IN_CONTENT_DESCRIPTION IN VARCHAR2,
                               P_IN_ARTICLE_NAME        IN ARTICLE_METADATA.ARTICLE_NAME%TYPE,
                               P_IN_CUST_PROD_ID        IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                               P_IN_SUBTESTID           IN SUBTEST_DIM.SUBTESTID%TYPE,
                               P_IN_OBJECTIVEID         IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                               P_IN_CATEGORY            IN ARTICLE_METADATA.CATEGORY%TYPE,
                               P_IN_CATEGORY_TYPE       IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                               P_IN_SUB_HEADER          IN ARTICLE_METADATA.SUB_HEADER%TYPE,
                               P_IN_GRADEID             IN GRADE_DIM.GRADEID%TYPE,
                               P_IN_PROF_LEVEL          IN ARTICLE_METADATA.PROFICIENCY_LEVEL%TYPE,
                               P_IN_STATUS_CODE         IN ARTICLE_METADATA.RESOLVED_RPRT_STATUS%TYPE,
                               P_OUT_STATUS_NUMBER      OUT NUMBER,
                               P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  /*PROCEDURE SP_GET_OBJECTIVE_DETAILS_EDIT(P_IN_OBJECTIVEID              IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
  P_OUT_CUR_CONTECT_DESCRIPTION OUT GET_REFCURSOR);*/

  PROCEDURE SP_GET_GENERIC_DETAILS_EDIT(P_IN_CUST_PROD_ID             IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                        P_IN_GRADEID                  IN GRADE_DIM.GRADEID%TYPE,
                                        P_IN_SUBTESTID                IN SUBTEST_DIM.SUBTESTID%TYPE,
                                        P_IN_OBJECTIVEID              IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                        P_IN_CATEGORY_TYPE            IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                        P_IN_PERFORMANCE_LEVEL        IN ARTICLE_METADATA.PROFICIENCY_LEVEL%TYPE,
                                        P_IN_STATUS_CODE              IN ARTICLE_METADATA.RESOLVED_RPRT_STATUS%TYPE,
                                        P_OUT_CUR_GENERIC_DESCRIPTION OUT GET_REFCURSOR);

  PROCEDURE SP_GET_CONTENT_DETAILS(P_IN_CUST_PROD_ID          IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_SUBTESTID             IN SUBTEST_DIM.SUBTESTID%TYPE,
                                   P_IN_OBJECTIVEID           IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                   P_IN_CATEGORY_TYPE         IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                   P_IN_MORE_COUNT            IN NUMBER,
                                   P_OUT_CUR_METADATA_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2);

  PROCEDURE SP_GET_CONTENT_DETAILS_MORE(P_IN_CUST_PROD_ID          IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                        P_IN_SUBTESTID             IN SUBTEST_DIM.SUBTESTID%TYPE,
                                        P_IN_OBJECTIVEID           IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                        P_IN_LASTID                IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                        P_IN_CATEGORY_TYPE         IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                        P_IN_MORE_COUNT            IN NUMBER,
                                        P_OUT_CUR_METADATA_DETAILS OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2);

  PROCEDURE SP_GET_CONTENT_DETAILS_EDIT(P_IN_METADATAID            IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                        P_OUT_CUR_METADATA_DETAILS OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2);

  PROCEDURE SP_UPDATE_CONTENT_DETAILS(P_IN_METADATAID          IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                      P_IN_ARTICLE_NAME        IN ARTICLE_METADATA.ARTICLE_NAME%TYPE,
                                      P_IN_SUB_HEADER          IN ARTICLE_METADATA.SUB_HEADER%TYPE,
                                      P_IN_CONTENT_DESCRIPTION IN VARCHAR2,
                                      P_IN_PROFICIENCY_LEVEL   IN ARTICLE_METADATA.PROFICIENCY_LEVEL%TYPE,
                                      P_OUT_STATUS_NUMBER      OUT NUMBER,
                                      P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  PROCEDURE SP_DELETE_CONTENT_DETAILS(P_IN_METADATAID     IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                      P_OUT_STATUS_NUMBER OUT NUMBER,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_COPY_CONTENT(P_IN_OLD_CUST_PROD_ID IN NUMBER,
                            P_IN_NEW_CUST_PROD_ID IN NUMBER,
                            P_IN_SUBTEST_CODE     IN VARCHAR,
                            P_IN_CATEGORY_TYPE    IN VARCHAR,
                            P_OUT_DATA_COUNT      OUT NUMBER,
                            P_OUT_STATUS_NUMBER   OUT NUMBER,
                            P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

END PKG_MANAGE_CONTENT;
/
CREATE OR REPLACE PACKAGE BODY PKG_MANAGE_CONTENT AS

  --THIS PROCEDURE TAKES CUSTOMERID AS INPUT & RETURN CUST_PROD_ID & PRODUCT_NAME
  PROCEDURE SP_GET_CUST_PROD_DETAILS(P_IN_CUSTOMERID             IN CUSTOMER_INFO.CUSTOMERID%TYPE,
                                     P_IN_ORG_NODEID             IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                     P_OUT_CUR_CUST_PROD_DETAILS OUT GET_REFCURSOR,
                                     P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2) IS
  
  BEGIN
    OPEN P_OUT_CUR_CUST_PROD_DETAILS FOR
      SELECT VALUE, NAME
        FROM (SELECT DISTINCT CUST.CUST_PROD_ID VALUE,
                              P.PRODUCT_NAME    NAME,
                              P.PRODUCT_SEQ /*,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  DENSE_RANK() OVER(PARTITION BY CUST.CUSTOMERID ORDER BY ADMIN.ADMIN_YEAR DESC NULLS LAST) AS SEQ*/
                FROM CUST_PRODUCT_LINK CUST,
                     PRODUCT P,
                     ORG_PRODUCT_LINK OPL,
                     (SELECT ADMINID, ADMIN_YEAR
                        FROM ADMIN_DIM
                       WHERE ADMIN_YEAR <=
                             (SELECT ADMIN_YEAR
                                FROM ADMIN_DIM
                               WHERE IS_CURRENT_ADMIN = 'Y')) ADMIN
               WHERE CUST.CUSTOMERID = P_IN_CUSTOMERID
                 AND CUST.ADMINID = ADMIN.ADMINID
                 AND CUST.PRODUCTID = P.PRODUCTID
                 AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
                 AND OPL.ORG_NODEID = P_IN_ORG_NODEID) A
      --WHERE A.SEQ <= 4
       ORDER BY /*A.SEQ,*/ A.PRODUCT_SEQ DESC;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_CUST_PROD_DETAILS;

  --THIS PROCEDURE TAKES CUST_PROD_ID AS INPUT & RETURN GRADEID & GRADE_NAME
  PROCEDURE SP_GET_GRADE_DETAILS(P_IN_CUST_PROD_ID       IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                 P_OUT_CUR_GRADE_DETAILS OUT GET_REFCURSOR,
                                 P_OUT_EXCEP_ERR_MSG     OUT VARCHAR2) IS
  
  BEGIN
  
    OPEN P_OUT_CUR_GRADE_DETAILS FOR
    
      SELECT VALUE, NAME
        FROM (SELECT DISTINCT GRA_DIM.GRADE_SEQ,
                              GRA_DIM.GRADEID    VALUE,
                              GRA_DIM.GRADE_NAME NAME
                FROM CUST_PRODUCT_LINK CUST,
                     ASSESSMENT_DIM    ASS_DIM,
                     LEVEL_MAP         L_MAP,
                     GRADE_LEVEL_MAP   GRA_LEV_MAP,
                     GRADE_DIM         GRA_DIM
               WHERE CUST.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND CUST.PRODUCTID = ASS_DIM.PRODUCTID
                 AND ASS_DIM.ASSESSMENTID = L_MAP.ASSESSMENTID
                 AND L_MAP.LEVEL_MAPID = GRA_LEV_MAP.LEVEL_MAPID
                 AND GRA_LEV_MAP.GRADEID = GRA_DIM.GRADEID
               ORDER BY GRA_DIM.GRADE_SEQ);
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_GRADE_DETAILS;

  --THIS PROCEDURE TAKES CUST_PROD_ID AS INPUT & RETURN SUBTESTID & SUBTEST_NAME
  PROCEDURE SP_GET_SUBTEST_DETAILS(P_IN_CUST_PROD_ID         IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_GRADEID              IN GRADE_DIM.GRADEID%TYPE,
                                   P_OUT_CUR_SUBTEST_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG       OUT VARCHAR2) IS
  
  BEGIN
  
    OPEN P_OUT_CUR_SUBTEST_DETAILS FOR
      SELECT VALUE, NAME
        FROM (SELECT DISTINCT DIM.SUBTEST_SEQ,
                              DIM.SUBTESTID    VALUE,
                              DIM.SUBTEST_NAME NAME
              
                FROM CUST_PRODUCT_LINK     CUST,
                     GRADE_LEVEL_MAP       GRA_LEV_MAP,
                     SUBTEST_OBJECTIVE_MAP OBJ_MAP,
                     ASSESSMENT_DIM        ASSDIM,
                     CONTENT_DIM           CON,
                     SUBTEST_DIM           DIM
               WHERE CUST.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND GRA_LEV_MAP.GRADEID = P_IN_GRADEID
                 AND CUST.PRODUCTID = ASSDIM.PRODUCTID
                 AND ASSDIM.ASSESSMENTID = CON.ASSESSMENTID
                 AND CON.CONTENTID = DIM.CONTENTID
                 AND GRA_LEV_MAP.LEVEL_MAPID = OBJ_MAP.LEVEL_MAPID
                 AND OBJ_MAP.SUBTESTID = DIM.SUBTESTID
               ORDER BY DIM.SUBTEST_SEQ);
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_SUBTEST_DETAILS;

  --THIS PROCEDURE TAKES SUBTESTID AS INPUT & RETURN SUBT_OBJ_MAPID & OBJECTIVE_NAME
  PROCEDURE SP_GET_OBJECTIVE_DETAILS(P_IN_SUBTESTID              IN SUBTEST_DIM.SUBTESTID%TYPE,
                                     P_IN_GRADEID                IN GRADE_DIM.GRADEID%TYPE,
                                     P_OUT_CUR_OBJECTIVE_DETAILS OUT GET_REFCURSOR,
                                     P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2) IS
  
  BEGIN
  
    OPEN P_OUT_CUR_OBJECTIVE_DETAILS FOR
      SELECT VALUE, NAME
        FROM (SELECT DISTINCT OBJ_DIM.OBJECTIVE_SEQ,
                              OBJ_DIM.OBJECTIVEID    VALUE,
                              OBJ_DIM.OBJECTIVE_NAME NAME
                FROM SUBTEST_OBJECTIVE_MAP SUBMAP,
                     GRADE_LEVEL_MAP       GRA_LEV_MAP,
                     GRADE_DIM             GRA_DIM,
                     OBJECTIVE_DIM         OBJ_DIM
               WHERE SUBMAP.SUBTESTID = P_IN_SUBTESTID
                 AND GRA_LEV_MAP.LEVEL_MAPID = SUBMAP.LEVEL_MAPID
                 AND GRA_LEV_MAP.GRADEID = GRA_DIM.GRADEID
                 AND GRA_DIM.GRADEID = P_IN_GRADEID
                 AND SUBMAP.OBJECTIVEID = OBJ_DIM.OBJECTIVEID
               ORDER BY OBJ_DIM.OBJECTIVE_SEQ);
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_OBJECTIVE_DETAILS;

  --THIS PROCEDURE TAKES CUST_PROD_ID,CATEGORY_TYPE AS INPUT & RETURN SCORE_VALUE & SCORE_VALUE_NAME
  PROCEDURE SP_GET_PERFORMANCE_LEVEL(P_IN_CUST_PROD_ID           IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                     P_IN_CONTENT_TYPE_ID        IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                     P_OUT_CUR_OBJECTIVE_DETAILS OUT GET_REFCURSOR,
                                     P_OUT_EXCEP_ERR_MSG         OUT VARCHAR2) IS
  
  BEGIN
  
    IF P_IN_CONTENT_TYPE_ID = 'SPL' THEN
      OPEN P_OUT_CUR_OBJECTIVE_DETAILS FOR
        SELECT VALUE, NAME
          FROM (SELECT SCORE_VALUE VALUE, SCORE_VALUE_NAME NAME
                  FROM SCORE_TYPE_LOOKUP
                 WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID
                   AND CATEGORY = 'SUBTEST'
                 ORDER BY SCORE_VALUE DESC);
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_PERFORMANCE_LEVEL;

  --PROCEDURE TO INSERT DATA INTO ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
  PROCEDURE SP_ADD_NEW_CONTENT(P_IN_CONTENT_DESCRIPTION IN VARCHAR2,
                               P_IN_ARTICLE_NAME        IN ARTICLE_METADATA.ARTICLE_NAME%TYPE,
                               P_IN_CUST_PROD_ID        IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                               P_IN_SUBTESTID           IN SUBTEST_DIM.SUBTESTID%TYPE,
                               P_IN_OBJECTIVEID         IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                               P_IN_CATEGORY            IN ARTICLE_METADATA.CATEGORY%TYPE,
                               P_IN_CATEGORY_TYPE       IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                               P_IN_SUB_HEADER          IN ARTICLE_METADATA.SUB_HEADER%TYPE,
                               P_IN_GRADEID             IN GRADE_DIM.GRADEID%TYPE,
                               P_IN_PROF_LEVEL          IN ARTICLE_METADATA.PROFICIENCY_LEVEL%TYPE,
                               P_IN_STATUS_CODE         IN ARTICLE_METADATA.RESOLVED_RPRT_STATUS%TYPE,
                               P_OUT_STATUS_NUMBER      OUT NUMBER,
                               P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    V_NUM     NUMBER := 0;
    STATUS_NO NUMBER := 0;
    MAPID     SUBTEST_OBJECTIVE_MAP.SUBT_OBJ_MAPID%TYPE;
    G_ID      GRADE_DIM.GRADEID%TYPE;
    CON_ID    ARTICLE_CONTENT.ARTICLE_CONTENT_ID%TYPE;
    V_COUNT   NUMBER := 0;
  BEGIN
  
    IF P_IN_CATEGORY_TYPE = 'STD' THEN
    
      SELECT SUBT_OBJ_MAPID
        INTO MAPID
        FROM SUBTEST_OBJECTIVE_MAP
       WHERE SUBTESTID = P_IN_SUBTESTID
         AND OBJECTIVEID = P_IN_OBJECTIVEID;
    
      SELECT COUNT(1)
        INTO V_COUNT
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND SUBTESTID = P_IN_SUBTESTID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND SUBT_OBJ_MAPID = MAPID;
    
      IF V_COUNT = 0 THEN
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL,
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           P_IN_OBJECTIVEID,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           SUBT_OBJ_MAPID,
           SUBTESTID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL,
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           MAPID,
           P_IN_SUBTESTID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL,
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE CUST_PROD_ID = P_IN_CUST_PROD_ID
           AND GRADEID = P_IN_GRADEID
           AND SUBTESTID = P_IN_SUBTESTID
           AND SUBT_OBJ_MAPID = MAPID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'ACT' OR P_IN_CATEGORY_TYPE = 'IND' THEN
      --FETCH SUBTEST_OBJECTIVE_MAP DEPENDING UPON SUBTESTIS & OBJECTIVEID
      SELECT SUBT_OBJ_MAPID
        INTO MAPID
        FROM SUBTEST_OBJECTIVE_MAP
       WHERE SUBTESTID = P_IN_SUBTESTID
         AND OBJECTIVEID = P_IN_OBJECTIVEID;
    
      INSERT INTO ARTICLE_CONTENT
        (ARTICLE_CONTENT_ID,
         ARTICLE_CONTENT,
         DESCRIPTION,
         OBJECTIVEID,
         CREATED_DATE_TIME)
      VALUES
        (ARTICLE_CONTENT_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_CONTENT_SEQ SEQUENCE TO POPULATE DATA.
         P_IN_CONTENT_DESCRIPTION,
         NULL,
         NULL,
         SYSDATE)
      RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
    
      -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
      INSERT INTO ARTICLE_METADATA
        (ARTICLEID,
         ARTICLE_NAME,
         CUST_PROD_ID,
         SUBT_OBJ_MAPID,
         SUBTESTID,
         ARTICLE_CONTENT_ID,
         CATEGORY,
         CATEGORY_TYPE,
         CATEGORY_SEQ,
         SUB_HEADER,
         DESCRIPTION,
         GRADEID,
         PROFICIENCY_LEVEL,
         RESOLVED_RPRT_STATUS,
         CREATED_DATE_TIME)
      VALUES
        (ARTICLE_METADATA_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_METADATA_SEQ SEQUENCE TO POPULATE DATA.
         P_IN_ARTICLE_NAME,
         P_IN_CUST_PROD_ID,
         MAPID,
         P_IN_SUBTESTID,
         V_NUM,
         P_IN_CATEGORY,
         P_IN_CATEGORY_TYPE,
         METADATA_CATEGORY_SEQ.NEXTVAL, -- WE ARE USING METADATA_CATEGORY_SEQ SEQUENCE TO POPULATE DATA.
         P_IN_SUB_HEADER,
         NULL,
         P_IN_GRADEID,
         P_IN_PROF_LEVEL,
         P_IN_STATUS_CODE,
         SYSDATE);
    
    ELSIF P_IN_CATEGORY_TYPE = 'RSC' THEN
    
      SELECT COUNT(1)
        INTO V_COUNT
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND SUBTESTID = P_IN_SUBTESTID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID;
    
      IF V_COUNT = 0 THEN
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_CONTENT_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           NULL,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           SUBTESTID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_METADATA_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           P_IN_SUBTESTID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL, -- WE ARE USING METADATA_CATEGORY_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE GRADEID = P_IN_GRADEID
           AND SUBTESTID = P_IN_SUBTESTID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND CUST_PROD_ID = P_IN_CUST_PROD_ID;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'OAR' OR P_IN_CATEGORY_TYPE = 'RBS' THEN
    
      SELECT COUNT(1)
        INTO V_COUNT
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND SUBTESTID = P_IN_SUBTESTID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL
         AND RESOLVED_RPRT_STATUS = P_IN_STATUS_CODE;
    
      IF V_COUNT = 0 THEN
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_CONTENT_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           NULL,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           SUBTESTID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_METADATA_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           P_IN_SUBTESTID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL, -- WE ARE USING METADATA_CATEGORY_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE GRADEID = P_IN_GRADEID
           AND SUBTESTID = P_IN_SUBTESTID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND CUST_PROD_ID = P_IN_CUST_PROD_ID
           AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL
           AND RESOLVED_RPRT_STATUS = P_IN_STATUS_CODE;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'SPL' THEN
    
      SELECT COUNT(1)
        INTO V_COUNT
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND SUBTESTID = P_IN_SUBTESTID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL;
    
      IF V_COUNT = 0 THEN
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL,
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           NULL,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           SUBTESTID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL,
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           P_IN_SUBTESTID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL,
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE GRADEID = P_IN_GRADEID
           AND SUBTESTID = P_IN_SUBTESTID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND CUST_PROD_ID = P_IN_CUST_PROD_ID
           AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'OPL' THEN
      /* FORM A IS HARD CODED FOR MO */
      SELECT SUBT_OBJ_MAPID
        INTO MAPID
        FROM SUBTEST_OBJECTIVE_MAP SOM,
             GRADE_LEVEL_MAP       GLM,
             LEVEL_MAP             LM,
             FORM_DIM              FD
       WHERE SOM.LEVEL_MAPID = GLM.LEVEL_MAPID
         AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
         AND LM.FORMID = FD.FORMID
         AND FD.FORM_NAME = 'A'
         AND SUBTESTID = P_IN_SUBTESTID
         AND OBJECTIVEID = P_IN_OBJECTIVEID
         AND GLM.GRADEID = P_IN_GRADEID;
    
      SELECT COUNT(1)
        INTO V_COUNT
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND SUBTESTID = P_IN_SUBTESTID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL
         AND SUBT_OBJ_MAPID = MAPID;
    
      IF V_COUNT = 0 THEN
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL,
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           P_IN_OBJECTIVEID,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           SUBT_OBJ_MAPID,
           SUBTESTID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL,
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           MAPID,
           P_IN_SUBTESTID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL,
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE GRADEID = P_IN_GRADEID
           AND SUBTESTID = P_IN_SUBTESTID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND CUST_PROD_ID = P_IN_CUST_PROD_ID
           AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL
           AND SUBT_OBJ_MAPID = MAPID;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'EDA' OR P_IN_CATEGORY_TYPE = 'ATT' THEN
    
      SELECT COUNT(GRADEID)
        INTO G_ID
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID;
    
      IF G_ID = 0 THEN
      
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_CONTENT_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           NULL,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL, -- WE ARE USING ARTICLE_METADATA_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL, -- WE ARE USING METADATA_CATEGORY_SEQ SEQUENCE TO POPULATE DATA.
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE GRADEID = P_IN_GRADEID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND CUST_PROD_ID = P_IN_CUST_PROD_ID;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;
    
    END IF;
  
    STATUS_NO           := 1;
    P_OUT_STATUS_NUMBER := STATUS_NO;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN TOO_MANY_ROWS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN DUP_VAL_ON_INDEX THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    
  END SP_ADD_NEW_CONTENT;

  --FETCH CONTENT DESCRIPTION BASED ON OBJECTIVEID.
  /*PROCEDURE SP_GET_OBJECTIVE_DETAILS_EDIT(P_IN_OBJECTIVEID              IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                          P_OUT_CUR_CONTECT_DESCRIPTION OUT GET_REFCURSOR) IS
  
  BEGIN
  
    OPEN P_OUT_CUR_CONTECT_DESCRIPTION FOR
  
      SELECT ARTICLE_CONTENT_ID,
             ARTICLE_CONTENT AS CONTENT_DESCRIPTION,
             OBJECTIVEID
        FROM ARTICLE_CONTENT
       WHERE OBJECTIVEID = P_IN_OBJECTIVEID;
  
  END SP_GET_OBJECTIVE_DETAILS_EDIT;*/

  ---------------------------------------------
  PROCEDURE SP_GET_GENERIC_DETAILS_EDIT(P_IN_CUST_PROD_ID             IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                        P_IN_GRADEID                  IN GRADE_DIM.GRADEID%TYPE,
                                        P_IN_SUBTESTID                IN SUBTEST_DIM.SUBTESTID%TYPE,
                                        P_IN_OBJECTIVEID              IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                        P_IN_CATEGORY_TYPE            IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                        P_IN_PERFORMANCE_LEVEL        IN ARTICLE_METADATA.PROFICIENCY_LEVEL%TYPE,
                                        P_IN_STATUS_CODE              IN ARTICLE_METADATA.RESOLVED_RPRT_STATUS%TYPE,
                                        P_OUT_CUR_GENERIC_DESCRIPTION OUT GET_REFCURSOR) IS
  
    V_DATA_COUNT NUMBER;
    V_MAPID      SUBTEST_OBJECTIVE_MAP.SUBT_OBJ_MAPID%TYPE;
  BEGIN
  
    IF P_IN_CATEGORY_TYPE = 'STD' THEN
    
      SELECT COUNT(META.ARTICLEID)
        INTO V_DATA_COUNT
        FROM ARTICLE_METADATA META, SUBTEST_DIM SDIM, GRADE_DIM GDIM
       WHERE GDIM.GRADEID = P_IN_GRADEID
         AND GDIM.GRADEID = META.GRADEID
         AND SDIM.SUBTESTID = P_IN_SUBTESTID
         AND SDIM.SUBTESTID = META.SUBTESTID
         AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND META.SUBT_OBJ_MAPID =
             (SELECT SUBT_OBJ_MAPID
                FROM SUBTEST_OBJECTIVE_MAP
               WHERE SUBTESTID = P_IN_SUBTESTID
                 AND OBJECTIVEID = P_IN_OBJECTIVEID);
    
      IF V_DATA_COUNT = 0 THEN
        OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
          SELECT 0 METADATA_ID,
                 TO_CLOB('Click here to add text') AS CONTENT_DESCRIPTION,
                 (SELECT OBJECTIVE_DESC
                    FROM OBJECTIVE_DIM
                   WHERE OBJECTIVEID = P_IN_OBJECTIVEID) OBJECTIVE_DESC
            FROM DUAL;
      ELSE
        OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
          SELECT METADATA.M_ID METADATA_ID,
                 CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION,
                 (SELECT OBJECTIVE_DESC
                    FROM OBJECTIVE_DIM
                   WHERE OBJECTIVEID = P_IN_OBJECTIVEID) OBJECTIVE_DESC
            FROM (SELECT META.ARTICLEID M_ID, META.ARTICLE_CONTENT_ID A_ID
                    FROM ARTICLE_METADATA META,
                         SUBTEST_DIM      SDIM,
                         GRADE_DIM        GDIM
                   WHERE GDIM.GRADEID = P_IN_GRADEID
                     AND GDIM.GRADEID = META.GRADEID
                     AND SDIM.SUBTESTID = P_IN_SUBTESTID
                     AND SDIM.SUBTESTID = META.SUBTESTID
                     AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                     AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
                     AND META.SUBT_OBJ_MAPID =
                         (SELECT SUBT_OBJ_MAPID
                            FROM SUBTEST_OBJECTIVE_MAP
                           WHERE SUBTESTID = P_IN_SUBTESTID
                             AND OBJECTIVEID = P_IN_OBJECTIVEID)) METADATA,
                 ARTICLE_CONTENT CON
           WHERE METADATA.A_ID = CON.ARTICLE_CONTENT_ID
           ORDER BY METADATA_ID;
      END IF;
    
    ELSIF P_IN_CATEGORY_TYPE = 'RSC' THEN
    
      OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
        SELECT METADATA.M_ID       METADATA_ID,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM (SELECT META.ARTICLEID M_ID, META.ARTICLE_CONTENT_ID A_ID
                  FROM ARTICLE_METADATA META,
                       SUBTEST_DIM      SDIM,
                       GRADE_DIM        GDIM
                 WHERE GDIM.GRADEID = P_IN_GRADEID
                   AND GDIM.GRADEID = META.GRADEID
                   AND SDIM.SUBTESTID = P_IN_SUBTESTID
                   AND SDIM.SUBTESTID = META.SUBTESTID
                   AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                   AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID) METADATA,
               ARTICLE_CONTENT CON
         WHERE METADATA.A_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY METADATA_ID;
    
    ELSIF P_IN_CATEGORY_TYPE = 'OAR' OR P_IN_CATEGORY_TYPE = 'RBS' THEN
    
      OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
        SELECT METADATA.M_ID       METADATA_ID,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM (SELECT META.ARTICLEID M_ID, META.ARTICLE_CONTENT_ID A_ID
                  FROM ARTICLE_METADATA META,
                       SUBTEST_DIM      SDIM,
                       GRADE_DIM        GDIM
                 WHERE GDIM.GRADEID = P_IN_GRADEID
                   AND GDIM.GRADEID = META.GRADEID
                   AND SDIM.SUBTESTID = P_IN_SUBTESTID
                   AND SDIM.SUBTESTID = META.SUBTESTID
                   AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                   AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
                   AND META.PROFICIENCY_LEVEL = P_IN_PERFORMANCE_LEVEL
                   AND META.RESOLVED_RPRT_STATUS = P_IN_STATUS_CODE) METADATA,
               ARTICLE_CONTENT CON
         WHERE METADATA.A_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY METADATA_ID;
    
    ELSIF P_IN_CATEGORY_TYPE = 'SPL' THEN
    
      OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
        SELECT METADATA.M_ID       METADATA_ID,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM (SELECT META.ARTICLEID M_ID, META.ARTICLE_CONTENT_ID A_ID
                  FROM ARTICLE_METADATA META,
                       SUBTEST_DIM      SDIM,
                       GRADE_DIM        GDIM
                 WHERE GDIM.GRADEID = P_IN_GRADEID
                   AND GDIM.GRADEID = META.GRADEID
                   AND SDIM.SUBTESTID = P_IN_SUBTESTID
                   AND SDIM.SUBTESTID = META.SUBTESTID
                   AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                   AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
                   AND META.PROFICIENCY_LEVEL = P_IN_PERFORMANCE_LEVEL) METADATA,
               ARTICLE_CONTENT CON
         WHERE METADATA.A_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY METADATA_ID;
    
    ELSIF P_IN_CATEGORY_TYPE = 'OPL' THEN
    
      /* FORM A IS HARD CODED FOR MO */
      SELECT SUBT_OBJ_MAPID
        INTO V_MAPID
        FROM SUBTEST_OBJECTIVE_MAP SOM,
             GRADE_LEVEL_MAP       GLM,
             LEVEL_MAP             LM,
             FORM_DIM              FD
       WHERE SOM.LEVEL_MAPID = GLM.LEVEL_MAPID
         AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
         AND LM.FORMID = FD.FORMID
         AND FD.FORM_NAME = 'A'
         AND SUBTESTID = P_IN_SUBTESTID
         AND OBJECTIVEID = P_IN_OBJECTIVEID
         AND GLM.GRADEID = P_IN_GRADEID;
    
      OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
        SELECT METADATA.M_ID       METADATA_ID,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM (SELECT META.ARTICLEID M_ID, META.ARTICLE_CONTENT_ID A_ID
                  FROM ARTICLE_METADATA META
                 WHERE META.GRADEID = P_IN_GRADEID
                   AND META.SUBTESTID = P_IN_SUBTESTID
                   AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                   AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID
                   AND META.PROFICIENCY_LEVEL = P_IN_PERFORMANCE_LEVEL
                   AND META.SUBT_OBJ_MAPID = V_MAPID) METADATA,
               ARTICLE_CONTENT CON
         WHERE METADATA.A_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY METADATA_ID;
    
    ELSIF P_IN_CATEGORY_TYPE = 'EDA' OR P_IN_CATEGORY_TYPE = 'ATT' THEN
    
      OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
        SELECT METADATA.M_ID       METADATA_ID,
               CON.ARTICLE_CONTENT AS CONTENT_DESCRIPTION
          FROM (SELECT META.ARTICLEID M_ID, META.ARTICLE_CONTENT_ID A_ID
                  FROM ARTICLE_METADATA META,
                       SUBTEST_DIM      SDIM,
                       GRADE_DIM        GDIM
                 WHERE GDIM.GRADEID = P_IN_GRADEID
                   AND GDIM.GRADEID = META.GRADEID
                   AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                   AND META.CUST_PROD_ID = P_IN_CUST_PROD_ID) METADATA,
               ARTICLE_CONTENT CON
         WHERE METADATA.A_ID = CON.ARTICLE_CONTENT_ID
         ORDER BY METADATA_ID;
    
    ELSE
    
      OPEN P_OUT_CUR_GENERIC_DESCRIPTION FOR
        SELECT 'Error in Input Details' FROM DUAL;
    
    END IF;
  
  END SP_GET_GENERIC_DETAILS_EDIT;

  --PROCEDURE TO FETCH DATA FROM ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
  PROCEDURE SP_GET_CONTENT_DETAILS(P_IN_CUST_PROD_ID          IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                   P_IN_SUBTESTID             IN SUBTEST_DIM.SUBTESTID%TYPE,
                                   P_IN_OBJECTIVEID           IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                   P_IN_CATEGORY_TYPE         IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                   P_IN_MORE_COUNT            IN NUMBER,
                                   P_OUT_CUR_METADATA_DETAILS OUT GET_REFCURSOR,
                                   P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2) IS
  
    MAPID SUBTEST_OBJECTIVE_MAP.SUBT_OBJ_MAPID%TYPE;
  BEGIN
  
    SELECT SUBT_OBJ_MAPID
      INTO MAPID
      FROM SUBTEST_OBJECTIVE_MAP
     WHERE SUBTESTID = P_IN_SUBTESTID
       AND OBJECTIVEID = P_IN_OBJECTIVEID;
  
    OPEN P_OUT_CUR_METADATA_DETAILS FOR
      SELECT METADATA_ID, NAME, SUB_HEADER, GRADE, PROFICIENCY_LEVEL
        FROM (SELECT META.ARTICLEID         METADATA_ID,
                     META.ARTICLE_NAME      NAME,
                     META.SUB_HEADER        SUB_HEADER,
                     DIM.GRADE_NAME         GRADE,
                     META.PROFICIENCY_LEVEL PROFICIENCY_LEVEL
                FROM ARTICLE_METADATA      META,
                     CUST_PRODUCT_LINK     CUST,
                     SUBTEST_OBJECTIVE_MAP SUBMAP,
                     GRADE_DIM             DIM
               WHERE META.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND META.CUST_PROD_ID = CUST.CUST_PROD_ID
                 AND META.SUBT_OBJ_MAPID = MAPID
                 AND META.SUBT_OBJ_MAPID = SUBMAP.SUBT_OBJ_MAPID
                 AND META.GRADEID = DIM.GRADEID
                 AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
               ORDER BY META.ARTICLEID)
       WHERE ROWNUM <= P_IN_MORE_COUNT;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_GET_CONTENT_DETAILS;

  --PROCEDURE TO FETCH MORE DATA FROM ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
  PROCEDURE SP_GET_CONTENT_DETAILS_MORE(P_IN_CUST_PROD_ID          IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                        P_IN_SUBTESTID             IN SUBTEST_DIM.SUBTESTID%TYPE,
                                        P_IN_OBJECTIVEID           IN OBJECTIVE_DIM.OBJECTIVEID%TYPE,
                                        P_IN_LASTID                IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                        P_IN_CATEGORY_TYPE         IN ARTICLE_METADATA.CATEGORY_TYPE%TYPE,
                                        P_IN_MORE_COUNT            IN NUMBER,
                                        P_OUT_CUR_METADATA_DETAILS OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2) IS
  
    MAPID SUBTEST_OBJECTIVE_MAP.SUBT_OBJ_MAPID%TYPE;
  BEGIN
  
    SELECT SUBT_OBJ_MAPID
      INTO MAPID
      FROM SUBTEST_OBJECTIVE_MAP
     WHERE SUBTESTID = P_IN_SUBTESTID
       AND OBJECTIVEID = P_IN_OBJECTIVEID;
  
    OPEN P_OUT_CUR_METADATA_DETAILS FOR
      SELECT METADATA_ID, NAME, SUB_HEADER, GRADE, PROFICIENCY_LEVEL
        FROM (SELECT META.ARTICLEID         METADATA_ID,
                     META.ARTICLE_NAME      NAME,
                     META.SUB_HEADER        SUB_HEADER,
                     DIM.GRADE_NAME         GRADE,
                     META.PROFICIENCY_LEVEL PROFICIENCY_LEVEL
                FROM ARTICLE_METADATA      META,
                     CUST_PRODUCT_LINK     CUST,
                     SUBTEST_OBJECTIVE_MAP SUBMAP,
                     GRADE_DIM             DIM
               WHERE META.CUST_PROD_ID = P_IN_CUST_PROD_ID
                 AND META.CUST_PROD_ID = CUST.CUST_PROD_ID
                 AND META.SUBT_OBJ_MAPID = MAPID
                 AND META.SUBT_OBJ_MAPID = SUBMAP.SUBT_OBJ_MAPID
                 AND META.GRADEID = DIM.GRADEID
                 AND META.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                 AND META.ARTICLEID > P_IN_LASTID
               ORDER BY META.ARTICLEID)
       WHERE ROWNUM <= P_IN_MORE_COUNT;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
    
  END SP_GET_CONTENT_DETAILS_MORE;

  -- PROCEDURE TO EDIT SINGLE ROW DATA FOR ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
  PROCEDURE SP_GET_CONTENT_DETAILS_EDIT(P_IN_METADATAID            IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                        P_OUT_CUR_METADATA_DETAILS OUT GET_REFCURSOR,
                                        P_OUT_EXCEP_ERR_MSG        OUT VARCHAR2) IS
  
    CONTENT_ID ARTICLE_CONTENT.ARTICLE_CONTENT_ID%TYPE;
  BEGIN
  
    SELECT ARTICLE_CONTENT_ID
      INTO CONTENT_ID
      FROM ARTICLE_METADATA
     WHERE ARTICLEID = P_IN_METADATAID;
  
    OPEN P_OUT_CUR_METADATA_DETAILS FOR
      SELECT META.ARTICLEID         METADATA_ID,
             META.ARTICLE_NAME      NAME,
             META.SUB_HEADER        SUB_HEADER,
             CON.ARTICLE_CONTENT    CONTENT_DESCRIPTION,
             META.PROFICIENCY_LEVEL PROFICIENCY_LEVEL
        FROM ARTICLE_METADATA META, ARTICLE_CONTENT CON
       WHERE META.ARTICLEID = P_IN_METADATAID
         AND META.ARTICLE_CONTENT_ID = CON.ARTICLE_CONTENT_ID
         AND CON.ARTICLE_CONTENT_ID = CONTENT_ID;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    
  END SP_GET_CONTENT_DETAILS_EDIT;

  --PROCEDURE TO UPDATE DATA FOR ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
  PROCEDURE SP_UPDATE_CONTENT_DETAILS(P_IN_METADATAID          IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                      P_IN_ARTICLE_NAME        IN ARTICLE_METADATA.ARTICLE_NAME%TYPE,
                                      P_IN_SUB_HEADER          IN ARTICLE_METADATA.SUB_HEADER%TYPE,
                                      P_IN_CONTENT_DESCRIPTION IN VARCHAR2,
                                      P_IN_PROFICIENCY_LEVEL   IN ARTICLE_METADATA.PROFICIENCY_LEVEL%TYPE,
                                      P_OUT_STATUS_NUMBER      OUT NUMBER,
                                      P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2) IS
  
    STATUS_NO  NUMBER := 0;
    CONTENT_ID ARTICLE_CONTENT.ARTICLE_CONTENT_ID%TYPE;
    V_NUM      NUMBER := 0;
  BEGIN
  
    SELECT COUNT(ARTICLE_CONTENT_ID)
      INTO V_NUM
      FROM ARTICLE_METADATA
     WHERE ARTICLE_CONTENT_ID =
           (SELECT ARTICLE_CONTENT_ID
              FROM ARTICLE_METADATA
             WHERE ARTICLEID = P_IN_METADATAID);
  
    SELECT ARTICLE_CONTENT_ID
      INTO CONTENT_ID
      FROM ARTICLE_METADATA
     WHERE ARTICLEID = P_IN_METADATAID;
  
    UPDATE ARTICLE_CONTENT
       SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
           UPDATED_DATE_TIME = SYSDATE
     WHERE ARTICLE_CONTENT_ID = CONTENT_ID;
  
    UPDATE ARTICLE_METADATA
       SET ARTICLE_NAME      = P_IN_ARTICLE_NAME,
           SUB_HEADER        = P_IN_SUB_HEADER,
           PROFICIENCY_LEVEL = P_IN_PROFICIENCY_LEVEL,
           UPDATED_DATE_TIME = SYSDATE
     WHERE ARTICLEID = P_IN_METADATAID;
  
    IF (V_NUM > 1) THEN
      STATUS_NO := 2;
    ELSE
      STATUS_NO := 1;
    END IF;
    P_OUT_STATUS_NUMBER := STATUS_NO;
  EXCEPTION
  
    WHEN NO_DATA_FOUND THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN TOO_MANY_ROWS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN DUP_VAL_ON_INDEX THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    
  END SP_UPDATE_CONTENT_DETAILS;

  --PROCEDURE TO DELETE DATA FROM ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
  PROCEDURE SP_DELETE_CONTENT_DETAILS(P_IN_METADATAID     IN ARTICLE_METADATA.ARTICLEID%TYPE,
                                      P_OUT_STATUS_NUMBER OUT NUMBER,
                                      P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
    STATUS_NO  NUMBER := 0;
    CONTENT_ID ARTICLE_CONTENT.ARTICLE_CONTENT_ID%TYPE;
    V_NUM      NUMBER := 0;
  BEGIN
  
    SELECT ARTICLE_CONTENT_ID
      INTO CONTENT_ID
      FROM ARTICLE_METADATA
     WHERE ARTICLEID = P_IN_METADATAID;
  
    SELECT COUNT(ARTICLE_CONTENT_ID)
      INTO V_NUM
      FROM ARTICLE_METADATA
     WHERE ARTICLE_CONTENT_ID =
           (SELECT ARTICLE_CONTENT_ID
              FROM ARTICLE_METADATA
             WHERE ARTICLEID = P_IN_METADATAID);
  
    DELETE FROM ARTICLE_METADATA WHERE ARTICLEID = P_IN_METADATAID;
    IF (V_NUM > 1) THEN
      STATUS_NO           := 2;
      P_OUT_EXCEP_ERR_MSG := 'METADATA DETAILS IS DELETED BUT CONTENTID CAN NOT BE DELETED BECAUSE IT IS ASSOCIATED WITH OTHER METADATA ALSO';
    ELSE
      DELETE FROM ARTICLE_CONTENT WHERE ARTICLE_CONTENT_ID = CONTENT_ID;
    
      STATUS_NO           := 1;
      P_OUT_EXCEP_ERR_MSG := 'RECORD IS SUCCESSFULLY DELETED FROM ARTICLE_METADATA & ARTICLE_CONTENT';
    END IF;
    P_OUT_STATUS_NUMBER := STATUS_NO;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    
  END SP_DELETE_CONTENT_DETAILS;

  /* PROCEDURE TO COPY CONTENT INTO ARTICLE_CONTENT & ARTICLE_METADATA TABLE.
     NOW IT IS APPLICABLE ONLY FOR MO SPL
     THIS PROCEDURE WILL CREATE NEW CONTENT AND NEW METADATA AND LINK THOSE ACCORDINGLY
  */
  PROCEDURE SP_COPY_CONTENT(P_IN_OLD_CUST_PROD_ID IN NUMBER,
                            P_IN_NEW_CUST_PROD_ID IN NUMBER,
                            P_IN_SUBTEST_CODE     IN VARCHAR,
                            P_IN_CATEGORY_TYPE    IN VARCHAR,
                            P_OUT_DATA_COUNT      OUT NUMBER,
                            P_OUT_STATUS_NUMBER   OUT NUMBER,
                            P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  
    V_NUM            NUMBER := 0;
    STATUS_NO        NUMBER := 0;
    V_OLD_ADMIN_YEAR ADMIN_DIM.ADMIN_YEAR%TYPE;
    V_NEW_ADMIN_YEAR ADMIN_DIM.ADMIN_YEAR%TYPE;
    --MAPID     SUBTEST_OBJECTIVE_MAP.SUBT_OBJ_MAPID%TYPE;
    --G_ID      GRADE_DIM.GRADEID%TYPE;
    --CON_ID    ARTICLE_CONTENT.ARTICLE_CONTENT_ID%TYPE;
    V_COUNT NUMBER := 0;
    --V_DEMO_CUSTOMER VARCHAR2(32) DEFAULT 'Missouri_MAP_Demo_Customer';
  BEGIN
  
    SELECT ADMIN_YEAR
      INTO V_OLD_ADMIN_YEAR
      FROM ADMIN_DIM AD, CUST_PRODUCT_LINK CPL
     WHERE AD.ADMINID = CPL.ADMINID
       AND CUST_PROD_ID = P_IN_OLD_CUST_PROD_ID;
  
    SELECT ADMIN_YEAR
      INTO V_NEW_ADMIN_YEAR
      FROM ADMIN_DIM AD, CUST_PRODUCT_LINK CPL
     WHERE AD.ADMINID = CPL.ADMINID
       AND CUST_PROD_ID = P_IN_NEW_CUST_PROD_ID;
  
    IF P_IN_CATEGORY_TYPE = 'SPL' THEN
    
      DELETE FROM ARTICLE_METADATA
       WHERE CUST_PROD_ID = P_IN_NEW_CUST_PROD_ID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND (P_IN_SUBTEST_CODE = '-1' OR
             SUBTESTID IN
             (SELECT SUBTESTID
                 FROM SUBTEST_DIM
                WHERE SUBTEST_CODE IN
                      (WITH T AS (SELECT P_IN_SUBTEST_CODE AS TXT FROM DUAL)
                        SELECT REGEXP_SUBSTR(TXT, '[^,]+', 1, LEVEL) AS SUBTEST_CODE
                          FROM T
                        CONNECT BY LEVEL <=
                                   LENGTH(REGEXP_REPLACE(TXT, '[^,]*')) + 1
                       )));
    
      FOR REC IN (SELECT (SELECT ARTICLE_CONTENT
                            FROM ARTICLE_CONTENT
                           WHERE ARTICLE_CONTENT_ID = AM.ARTICLE_CONTENT_ID) AS ARTICLE_CONTENT,
                         AM.ARTICLEID,
                         AM.ARTICLE_NAME,
                         AM.CUST_PROD_ID,
                         AM.SUBTESTID,
                         AM.ARTICLE_CONTENT_ID,
                         AM.CATEGORY,
                         AM.CATEGORY_TYPE,
                         AM.CATEGORY_SEQ,
                         AM.SUB_HEADER,
                         AM.DESCRIPTION,
                         AM.GRADEID,
                         AM.PROFICIENCY_LEVEL,
                         AM.RESOLVED_RPRT_STATUS
                    FROM ARTICLE_METADATA AM
                   WHERE AM.CUST_PROD_ID = P_IN_OLD_CUST_PROD_ID
                     AND AM.CATEGORY_TYPE = P_IN_CATEGORY_TYPE
                     AND (P_IN_SUBTEST_CODE = '-1' OR
                         SUBTESTID IN
                         (SELECT SUBTESTID
                             FROM SUBTEST_DIM
                            WHERE SUBTEST_CODE IN (WITH T AS (SELECT P_IN_SUBTEST_CODE AS TXT
                                                      FROM DUAL)
                                                    SELECT REGEXP_SUBSTR(TXT,
                                                                         '[^,]+',
                                                                         1,
                                                                         LEVEL) AS SUBTEST_CODE
                                                      FROM T
                                                    CONNECT BY LEVEL <=
                                                               LENGTH(REGEXP_REPLACE(TXT,
                                                                                     '[^,]*')) + 1
                                                   )))) LOOP
      
        IF V_OLD_ADMIN_YEAR <> V_NEW_ADMIN_YEAR THEN
          -- INSERT RECORD INTO ARTICLE_CONTENT TABLE.
          INSERT INTO ARTICLE_CONTENT
            (ARTICLE_CONTENT_ID, ARTICLE_CONTENT, CREATED_DATE_TIME)
          VALUES
            (ARTICLE_CONTENT_SEQ.NEXTVAL, REC.ARTICLE_CONTENT, SYSDATE)
          RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
        ELSE
          V_NUM := REC.ARTICLE_CONTENT_ID;
        END IF;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        IF V_OLD_ADMIN_YEAR = 2015 AND V_NEW_ADMIN_YEAR = 2016 THEN
          INSERT INTO ARTICLE_METADATA
            (ARTICLEID,
             ARTICLE_NAME,
             CUST_PROD_ID,
             SUBTESTID,
             ARTICLE_CONTENT_ID,
             CATEGORY,
             CATEGORY_TYPE,
             CATEGORY_SEQ,
             SUB_HEADER,
             DESCRIPTION,
             GRADEID,
             PROFICIENCY_LEVEL,
             RESOLVED_RPRT_STATUS,
             CREATED_DATE_TIME)
          VALUES
            (ARTICLE_METADATA_SEQ.NEXTVAL,
             REC.ARTICLE_NAME,
             P_IN_NEW_CUST_PROD_ID,
             (SELECT SD.SUBTESTID
                FROM MO_SUBTEST_CONFIG MSC,
                     SUBTEST_DIM       SD,
                     CUST_PRODUCT_LINK CPL
               WHERE MSC.SUBTESTID = SD.SUBTESTID
                 AND MSC.PRODUCTID = CPL.PRODUCTID
                 AND CPL.CUST_PROD_ID = P_IN_NEW_CUST_PROD_ID
                 AND SD.SUBTEST_CODE =
                     (SELECT SUBTEST_CODE
                        FROM SUBTEST_DIM
                       WHERE SUBTESTID = REC.SUBTESTID)),
             V_NUM,
             METADATA_CATEGORY_SEQ.NEXTVAL,
             REC.CATEGORY_TYPE,
             REC.CATEGORY_SEQ,
             REC.SUB_HEADER,
             REC.DESCRIPTION,
             REC.GRADEID,
             DECODE(REC.PROFICIENCY_LEVEL,
                    '0',
                    '0',
                    TO_NUMBER(REC.PROFICIENCY_LEVEL) + 1),
             REC.RESOLVED_RPRT_STATUS,
             SYSDATE);
        ELSE
          INSERT INTO ARTICLE_METADATA
            (ARTICLEID,
             ARTICLE_NAME,
             CUST_PROD_ID,
             SUBTESTID,
             ARTICLE_CONTENT_ID,
             CATEGORY,
             CATEGORY_TYPE,
             CATEGORY_SEQ,
             SUB_HEADER,
             DESCRIPTION,
             GRADEID,
             PROFICIENCY_LEVEL,
             RESOLVED_RPRT_STATUS,
             CREATED_DATE_TIME)
          VALUES
            (ARTICLE_METADATA_SEQ.NEXTVAL,
             REC.ARTICLE_NAME,
             P_IN_NEW_CUST_PROD_ID,
             (SELECT SD.SUBTESTID
                FROM MO_SUBTEST_CONFIG MSC,
                     SUBTEST_DIM       SD,
                     CUST_PRODUCT_LINK CPL
               WHERE MSC.SUBTESTID = SD.SUBTESTID
                 AND MSC.PRODUCTID = CPL.PRODUCTID
                 AND CPL.CUST_PROD_ID = P_IN_NEW_CUST_PROD_ID
                 AND SD.SUBTEST_CODE =
                     (SELECT SUBTEST_CODE
                        FROM SUBTEST_DIM
                       WHERE SUBTESTID = REC.SUBTESTID)),
             V_NUM,
             METADATA_CATEGORY_SEQ.NEXTVAL,
             REC.CATEGORY_TYPE,
             REC.CATEGORY_SEQ,
             REC.SUB_HEADER,
             REC.DESCRIPTION,
             REC.GRADEID,
             REC.PROFICIENCY_LEVEL,
             REC.RESOLVED_RPRT_STATUS,
             SYSDATE);
        END IF;
      
        V_COUNT := V_COUNT + 1;
      
      END LOOP;
    
      /* ELSIF P_IN_CATEGORY_TYPE = 'OPL' THEN
      SELECT 1 FROM DUAL;
      --TODO
      FORM A IS HARD CODED FOR MO */
    
      /*SELECT SUBT_OBJ_MAPID
        INTO MAPID
        FROM SUBTEST_OBJECTIVE_MAP SOM,
             GRADE_LEVEL_MAP       GLM,
             LEVEL_MAP             LM,
             FORM_DIM              FD
       WHERE SOM.LEVEL_MAPID = GLM.LEVEL_MAPID
         AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
         AND LM.FORMID = FD.FORMID
         AND FD.FORM_NAME = 'A'
         AND SUBTESTID = P_IN_SUBTESTID
         AND OBJECTIVEID = P_IN_OBJECTIVEID
         AND GLM.GRADEID = P_IN_GRADEID;
      
      SELECT COUNT(1)
        INTO V_COUNT
        FROM ARTICLE_METADATA
       WHERE GRADEID = P_IN_GRADEID
         AND SUBTESTID = P_IN_SUBTESTID
         AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
         AND CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL
         AND SUBT_OBJ_MAPID = MAPID;
      
      IF V_COUNT = 0 THEN
        INSERT INTO ARTICLE_CONTENT
          (ARTICLE_CONTENT_ID,
           ARTICLE_CONTENT,
           DESCRIPTION,
           OBJECTIVEID,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_CONTENT_SEQ.NEXTVAL,
           P_IN_CONTENT_DESCRIPTION,
           NULL,
           P_IN_OBJECTIVEID,
           SYSDATE)
        RETURNING ARTICLE_CONTENT_ID INTO V_NUM;
      
        -- INSERT RECORD INTO ARTICLE_METADATA TABLE.
        INSERT INTO ARTICLE_METADATA
          (ARTICLEID,
           ARTICLE_NAME,
           CUST_PROD_ID,
           SUBT_OBJ_MAPID,
           SUBTESTID,
           ARTICLE_CONTENT_ID,
           CATEGORY,
           CATEGORY_TYPE,
           CATEGORY_SEQ,
           SUB_HEADER,
           DESCRIPTION,
           GRADEID,
           PROFICIENCY_LEVEL,
           RESOLVED_RPRT_STATUS,
           CREATED_DATE_TIME)
        VALUES
          (ARTICLE_METADATA_SEQ.NEXTVAL,
           P_IN_ARTICLE_NAME,
           P_IN_CUST_PROD_ID,
           MAPID,
           P_IN_SUBTESTID,
           V_NUM,
           P_IN_CATEGORY,
           P_IN_CATEGORY_TYPE,
           METADATA_CATEGORY_SEQ.NEXTVAL,
           P_IN_SUB_HEADER,
           NULL,
           P_IN_GRADEID,
           P_IN_PROF_LEVEL,
           P_IN_STATUS_CODE,
           SYSDATE);
      
      ELSE
      
        SELECT ARTICLE_CONTENT_ID
          INTO CON_ID
          FROM ARTICLE_METADATA
         WHERE GRADEID = P_IN_GRADEID
           AND SUBTESTID = P_IN_SUBTESTID
           AND CATEGORY_TYPE = P_IN_CATEGORY_TYPE
           AND CUST_PROD_ID = P_IN_CUST_PROD_ID
           AND PROFICIENCY_LEVEL = P_IN_PROF_LEVEL
           AND SUBT_OBJ_MAPID = MAPID;
      
        UPDATE ARTICLE_CONTENT
           SET ARTICLE_CONTENT   = P_IN_CONTENT_DESCRIPTION,
               UPDATED_DATE_TIME = SYSDATE
         WHERE ARTICLE_CONTENT_ID = CON_ID;
      
      END IF;*/
    
    END IF;
  
    STATUS_NO           := 1;
    P_OUT_STATUS_NUMBER := STATUS_NO;
    P_OUT_DATA_COUNT    := V_COUNT;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN TOO_MANY_ROWS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN DUP_VAL_ON_INDEX THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := STATUS_NO;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
      ROLLBACK;
    
  END SP_COPY_CONTENT;

END PKG_MANAGE_CONTENT; --END OF PACKAGE
/
