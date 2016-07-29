/*SELECT *
  FROM ARTICLE_METADATA
 WHERE CREATED_DATE_TIME > '28-JUL-2016'
 ORDER BY CREATED_DATE_TIME DESC;
 
SELECT *
  FROM ARTICLE_CONTENT
 WHERE CREATED_DATE_TIME > '28-JUL-2016'
 ORDER BY CREATED_DATE_TIME DESC;*/

ALTER TABLE ARTICLE_CONTENT RENAME TO ARTICLE_CONTENT_BKP1;
ALTER TABLE ARTICLE_METADATA RENAME TO ARTICLE_METADATA_BKP1;

/*
IMPORT THE TABLES 
1. ARTICLE_CONTENT 
2. ARTICLE_METADATA 
*/

ALTER TABLE ARTICLE_CONTENT RENAME TO ARTICLE_CONTENT_BKP2;
ALTER TABLE ARTICLE_METADATA RENAME TO ARTICLE_METADATA_BKP2;

ALTER TABLE ARTICLE_CONTENT_BKP1 RENAME TO ARTICLE_CONTENT;
ALTER TABLE ARTICLE_METADATA_BKP1 RENAME TO ARTICLE_METADATA;

-- RECOMPILE PKG_MANAGE_CONTENT

-- RUN THE BELLOW STATEMENT THROUGH COMMAND PROMPT

SET SERVEROUTPUT ON;

-- RUN THE BELLOW SCRIPT THROUGH SQL WINDOW AND CHECK THE OUTPUT

DECLARE

  P_OUT_STATUS_NUMBER NUMBER(2);
  P_OUT_EXCEP_ERR_MSG VARCHAR2(300);

BEGIN

  DBMS_OUTPUT.PUT_LINE('INSERTION STARTED ');
  FOR REC_CUST_PROD_ID IN (SELECT CUST_PROD_ID
                             FROM CUST_PRODUCT_LINK CPL, PRODUCT P
                            WHERE CPL.PRODUCTID = P.PRODUCTID
                              AND P.PRODUCT_NAME LIKE '%2016%') LOOP
  
    DBMS_OUTPUT.PUT_LINE('INSERT RECORDS FOR CUST_PROD_ID: ' ||
                         REC_CUST_PROD_ID.CUST_PROD_ID);
  
    FOR REC_AMAC IN (SELECT AM.ARTICLEID,
                            AC.ARTICLE_CONTENT,
                            AM.ARTICLE_NAME,
                            AM.CUST_PROD_ID,
                            AM.SUBTESTID,
                            AC.OBJECTIVEID,
                            AM.CATEGORY,
                            AM.CATEGORY_TYPE,
                            AM.SUB_HEADER,
                            AM.GRADEID,
                            AM.PROFICIENCY_LEVEL,
                            AM.RESOLVED_RPRT_STATUS
                       FROM ARTICLE_METADATA_BKP2 AM,
                            ARTICLE_CONTENT_BKP2  AC
                      WHERE AM.ARTICLE_CONTENT_ID = AC.ARTICLE_CONTENT_ID) LOOP
    
      PKG_MANAGE_CONTENT.SP_ADD_NEW_CONTENT(REC_AMAC.ARTICLE_CONTENT,
                                            REC_AMAC.ARTICLE_NAME,
                                            REC_CUST_PROD_ID.CUST_PROD_ID,
                                            REC_AMAC.SUBTESTID,
                                            REC_AMAC.OBJECTIVEID,
                                            REC_AMAC.CATEGORY,
                                            REC_AMAC.CATEGORY_TYPE,
                                            REC_AMAC.SUB_HEADER,
                                            REC_AMAC.GRADEID,
                                            REC_AMAC.PROFICIENCY_LEVEL,
                                            REC_AMAC.RESOLVED_RPRT_STATUS,
                                            P_OUT_STATUS_NUMBER,
                                            P_OUT_EXCEP_ERR_MSG);
    
    END LOOP;
  
  END LOOP;

  DBMS_OUTPUT.PUT_LINE('INSERTION END ');
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('INSERTION COMMITED ');
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
    ROLLBACK;
  
END;