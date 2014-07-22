CREATE OR REPLACE PACKAGE PKG_ISTEP_MATRIX IS

  -- Author  : Parthapratim Mukherjee
  -- Created : 5/24/2013 12:21:13 PM
  -- Purpose :
  -- Public type declarations
/*  PROCEDURE ISTEP_ALL_DATA_LOAD(P_QUERY_TYP VARCHAR2,
                                P_IS_FIRST  VARCHAR2,
                                P_DUMMY     OUT NUMBER);*/
  PROCEDURE ISTEP_GRW_SUBTEST_SCORE_FACT(IS_SPLIT_PARTITION VARCHAR2,
                                        P_CUST_PROD_ID NUMBER) ;                             
  PROCEDURE ISTEP_ALL_DATA_LOAD(P_QUERY_TYP   VARCHAR2,
                                P_IS_FIRST    VARCHAR2,
                                P_CUST_PROD_ID   NUMBER,
                                P_DUMMY     OUT NUMBER);
/*  PROCEDURE ISTEP_MATRIX_REPORT_LOAD(P_QUERY_TYP   VARCHAR2,
                                     P_IS_FIRST_LD VARCHAR2);*/

   PROCEDURE ISTEP_MATRIX_REPORT_LOAD(P_QUERY_TYP   VARCHAR2,
                                     P_IS_FIRST_LD VARCHAR2,
                                      P_CUST_PROD_ID   NUMBER,
                                      P_SUBTESTCODE VARCHAR2);

  PROCEDURE IDX_BUILD(P_IS_FIRST VARCHAR2, P_DUMMY OUT NUMBER);

END PKG_ISTEP_MATRIX;
/
CREATE OR REPLACE PACKAGE BODY PKG_ISTEP_MATRIX AS

 PROCEDURE ISTEP_GRW_SUBTEST_SCORE_FACT(IS_SPLIT_PARTITION VARCHAR2,
                                        P_CUST_PROD_ID NUMBER) IS
                                                      
                                                      
    V_CNT              NUMBER := 0;
    V_PREV_CUST_PROD_ID  CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
    V_CURR_CUST_PROD_ID  CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
    
    CURSOR C_PREV_CUST_PROD_ID IS
     SELECT CPL.CUST_PROD_ID 
    FROM (SELECT CPL.ADMINID,CPL.PRODUCTID,CPL.CUST_PROD_ID ,ADM.ADMIN_YEAR 
           FROM CUST_PRODUCT_LINK CPL,ADMIN_DIM ADM
           WHERE CPL.CUST_PROD_ID =P_CUST_PROD_ID
           AND CPL.ADMINID = ADM.ADMINID) CPL1,
         CUST_PRODUCT_LINK CPL,
         ADMIN_DIM ADM,
         PRODUCT PDT
    WHERE  PDT.PRODUCTID = CPL.PRODUCTID
      AND  CPL.ADMINID = ADM.ADMINID
      AND  ADM.ADMIN_YEAR = CPL1.ADMIN_YEAR-1
      AND  PDT.PRODUCT_CODE LIKE 'ISTEP%';
      
 CURSOR C_GRW_SUBTEST_PARTITION IS  
 SELECT * FROM    
 (SELECT TABLESPACE_NAME,
       TABLE_NAME,
       PARTITION_NAME,
       SUBSTR(PARTITION_NAME,LENGTH(PARTITION_NAME)-3) AS CUST_PROD_ID,
       HIGH_VALUE    
  FROM USER_TAB_PARTITIONS WHERE TABLE_NAME = 'GRW_SUBTEST_SCORE_FACT'
  AND PARTITION_NAME <> 'PART_OTHERS'
  AND SUBSTR(PARTITION_NAME,LENGTH(PARTITION_NAME)-3) = TO_CHAR(P_CUST_PROD_ID)
  UNION ALL
  SELECT NULL AS TABLESPACE_NAME,
       NULL AS TABLE_NAME,
       NULL AS PARTITION_NAME,
       '-1' AS CUST_PROD_ID,
       NULL AS HIGH_VALUE    
  FROM DUAL 
  ORDER BY 4 DESC ) WHERE ROWNUM=1;
    
   /* SELECT PDT.PRODUCT_NAME ,PDT.PRODUCTID,CPL.CUST_PROD_ID,ADM.ADMINID,ADM.ADMIN_YEAR 
      FROM CUST_PRODUCT_LINK CPL,
           PRODUCT PDT,
           ADMIN_DIM ADM
      WHERE PDT.PRODUCTID = CPL.PRODUCTID
      AND  CPL.ADMINID = ADM.ADMINID
      AND  PDT.PRODUCT_CODE LIKE 'ISTEP%'
      AND  ADM.ADMIN_YEAR IN (2013,2012)*/
                                                     
BEGIN  
    SELECT CUST_PROD_ID INTO V_CURR_CUST_PROD_ID
    FROM      
    (SELECT SCR.CUST_PROD_ID FROM  GRW_SUBTEST_SCORE_FACT SCR WHERE SCR.CUST_PROD_ID =P_CUST_PROD_ID AND ROWNUM=1
    UNION ALL
    SELECT -1  AS CUST_PROD_ID FROM DUAL
    ORDER BY 1 DESC) WHERE ROWNUM=1 ;
   
   IF V_CURR_CUST_PROD_ID = P_CUST_PROD_ID THEN
    INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('Start Delete GRW_SUBTEST_SCORE_FACT for' || P_CUST_PROD_ID, SYSDATE);            
    DELETE FROM GRW_SUBTEST_SCORE_FACT WHERE CUST_PROD_ID = P_CUST_PROD_ID;
    INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('End Delete GRW_SUBTEST_SCORE_FACT for' || P_CUST_PROD_ID, SYSDATE);
    
    INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('Start Delete GRW_DUPLICATE_STUDENTTS for' || P_CUST_PROD_ID, SYSDATE);
    DELETE FROM GRW_DUPLICATE_STUDENTS WHERE CUST_PROD_ID = P_CUST_PROD_ID;
    INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('End Delete GRW_DUPLICATE_STUDENTTS for' || P_CUST_PROD_ID, SYSDATE); 
   COMMIT;
   END IF;
   
  FOR R_PREV_CUST_PROD_ID IN C_PREV_CUST_PROD_ID 
  LOOP
         V_PREV_CUST_PROD_ID := R_PREV_CUST_PROD_ID.CUST_PROD_ID;
  END LOOP; 
  
  SELECT CUST_PROD_ID INTO V_CNT    
   FROM  
   (SELECT SCR.CUST_PROD_ID FROM GRW_SUBTEST_SCORE_FACT SCR WHERE SCR.CUST_PROD_ID =V_PREV_CUST_PROD_ID AND ROWNUM=1
    UNION ALL
    SELECT -1  AS CUST_PROD_ID FROM DUAL
    ORDER BY 1 DESC) WHERE ROWNUM = 1;
    
   IF V_CNT > 0 THEN 
     INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('Start Delete GRW_SUBTEST_SCORE_FACT for' || V_PREV_CUST_PROD_ID, SYSDATE);
     DELETE FROM GRW_SUBTEST_SCORE_FACT WHERE CUST_PROD_ID = V_PREV_CUST_PROD_ID;
     INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('End Delete GRW_SUBTEST_SCORE_FACT for' || V_PREV_CUST_PROD_ID, SYSDATE);
     
     INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('Start Delete GRW_DUPLICATE_STUDENTTS for' || V_PREV_CUST_PROD_ID, SYSDATE);
     DELETE FROM GRW_DUPLICATE_STUDENTS WHERE CUST_PROD_ID = V_PREV_CUST_PROD_ID;
     INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('End Delete GRW_DUPLICATE_STUDENTTS for' || V_PREV_CUST_PROD_ID, SYSDATE);
    COMMIT;
    --V_CNT :=0;
  END IF;
   
  IF IS_SPLIT_PARTITION='Y' THEN
  FOR R_GRW_SUBTEST_PARTITION IN C_GRW_SUBTEST_PARTITION 
  LOOP 
      IF  R_GRW_SUBTEST_PARTITION.CUST_PROD_ID <> TO_CHAR(P_CUST_PROD_ID) THEN
      EXECUTE IMMEDIATE 'ALTER TABLE GRW_SUBTEST_SCORE_FACT
                        SPLIT PARTITION PART_OTHERS
                        VALUES ('||P_CUST_PROD_ID||')
                        INTO ( PARTITION PART_C'||P_CUST_PROD_ID||', PARTITION PART_OTHERS )';
                        
      INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('Partition created GRW_SUBTEST_SCORE_FACT for' || P_CUST_PROD_ID, SYSDATE);                  
      
      END IF;
   END LOOP;
  END IF;

 INSERT INTO GRW_DUPLICATE_STUDENTS
  SELECT A.* FROM 
    (SELECT COUNT(GRT.STUDENT_BIO_ID) OVER (PARTITION BY GRT.CUST_PROD_ID,GRT.STUDENT_TEST_AI) AS CNT,
           GRT.STUDENT_BIO_ID, GRT.CUST_PROD_ID,GRT.STUDENT_TEST_AI,GRT.ADMINID,GRT.GRADEID                       
    FROM RESULTS_GRT_FACT GRT WHERE GRT.CUST_PROD_ID IN (P_CUST_PROD_ID,V_PREV_CUST_PROD_ID))A
    WHERE A.CNT >1;
  COMMIT;
  
   INSERT INTO  GRW_SUBTEST_SCORE_FACT   
      SELECT SCR.ORG_NODEID,
           SCR.STUDENT_BIO_ID,
           SCR.CUST_PROD_ID,
           SCR.SUBTESTID,
           SUB.SUBTEST_CODE,
           SCR.GRADEID,
           SCR.ADMINID,
           SCR.SS,
           SCR.PL,
           SYSDATE AS DATETIMESTAMP     
    FROM SUBTEST_SCORE_FACT SCR,SUBTEST_DIM SUB,CUST_PRODUCT_LINK CPL
    WHERE SCR.CUST_PROD_ID IN (P_CUST_PROD_ID,V_PREV_CUST_PROD_ID) 
      AND CPL.CUST_PROD_ID=SCR.CUST_PROD_ID
      AND SCR.ADMINID = CPL.ADMINID
      AND SCR.SUBTESTID = SUB.SUBTESTID
      AND SUB.SUBTEST_CODE IN ('MATH','ELA')
      AND NOT EXISTS (SELECT 1 
                      FROM GRW_DUPLICATE_STUDENTS DUP 
                      WHERE DUP.CUST_PROD_ID= SCR.CUST_PROD_ID
                         AND DUP.ADMINID=SCR.ADMINID
                         AND SCR.STUDENT_BIO_ID=DUP.STUDENT_BIO_ID)
      /*AND SCR.PL IN ('P','A','B')*/;   
   INSERT INTO CHECK_RECS  (TYPE1, TIMETO) VALUES ('Data inserted into GRW_SUBTEST_SCORE_FACT for' || P_CUST_PROD_ID || ', '||V_PREV_CUST_PROD_ID, SYSDATE);  
 COMMIT ;
END ISTEP_GRW_SUBTEST_SCORE_FACT;


  PROCEDURE ISTEP_ALL_DATA_LOAD(P_QUERY_TYP   VARCHAR2,
                                P_IS_FIRST    VARCHAR2,
                                P_CUST_PROD_ID   NUMBER, 
                                P_DUMMY     OUT NUMBER) IS

  BEGIN

    /*To populate all the combinations one by one serially*/
    IF P_QUERY_TYP = 'ALL' THEN
     FOR J IN (SELECT 'ELA' SUB FROM DUAL 
               UNION ALL
               SELECT 'MATH' SUB FROM DUAL )
     LOOP          
      FOR I IN (SELECT 'DNP_DNP' AA
                  FROM DUAL
                UNION ALL
                SELECT 'DNP_PASS' AA
                  FROM DUAL
                UNION ALL
                SELECT 'DNP_PASSP' AA
                  FROM DUAL
                UNION ALL
                SELECT 'PASS_DNP' AA
                  FROM DUAL
                UNION ALL
                SELECT 'PASS_PASS' AA
                  FROM DUAL
                UNION ALL
                SELECT 'PASS_PASSP' AA
                  FROM DUAL
                UNION ALL
                SELECT 'PASSP_DNP' AA
                  FROM DUAL
                UNION ALL
                SELECT 'PASSP_PASS' AA
                  FROM DUAL
                UNION ALL
                SELECT 'PASSP_PASSP' AA FROM DUAL) LOOP
        ISTEP_MATRIX_REPORT_LOAD(I.AA, P_IS_FIRST,P_CUST_PROD_ID,J.SUB );
      END LOOP;
    END LOOP;
    ELSE
      /*To populate all the combinations one by one parallely*/
      FOR J IN (SELECT 'ELA' SUB FROM DUAL 
               UNION ALL
               SELECT 'MATH' SUB FROM DUAL )
       LOOP 
        ISTEP_MATRIX_REPORT_LOAD(P_QUERY_TYP, P_IS_FIRST,P_CUST_PROD_ID,J.SUB);
      END LOOP;
    END IF;

    P_DUMMY := 1;

  END ISTEP_ALL_DATA_LOAD;

  PROCEDURE ISTEP_MATRIX_REPORT_LOAD(P_QUERY_TYP   VARCHAR2,
                                     P_IS_FIRST_LD VARCHAR2,
                                      P_CUST_PROD_ID   NUMBER, 
                                      P_SUBTESTCODE VARCHAR2) IS
    L_PERFLVL_CURR     VARCHAR2(30);
    L_PERFLVL_PREV     VARCHAR2(30);
    V_CNT              NUMBER := 0;
    LV_TYP_VARCHAR_ARR TYP_VARCHAR_ARR;
    V_CNT              NUMBER := 0;
    V_PRODUCTID_PREV   NUMBER := 0;
    V_CUSTOMER_ID NUMBER := 0;

    CURSOR C_SUBTEST IS
      SELECT DISTINCT SUBTESTID
        FROM SUBTEST_DIM
       WHERE SUBTEST_CODE IN (P_SUBTESTCODE)
         AND CONTENTID IN
             (SELECT CONTENTID
                FROM CONTENT_DIM
               WHERE ASSESSMENTID IN
                     (SELECT ASSES.ASSESSMENTID
                        FROM ASSESSMENT_DIM ASSES,CUST_PRODUCT_LINK CPL
                       WHERE ASSES.PRODUCTID = CPL.PRODUCTID
                       AND CPL.CUST_PROD_ID = P_CUST_PROD_ID));

    CURSOR C_USERID IS
      SELECT USR.USERID
        FROM USERS USR,USERS_MAP UMP
       WHERE EXISTS (SELECT 1
                FROM USER_ROLE  URL
               WHERE URL.USERID = USR.USERID
               AND URL.ROLEID = 8 
                 /*AND ROWNUM = 1*/)
            AND UMP.USERID = USR.USERID;
      /*AND username = '104279022750' */         
     /* AND rownum < 501*/ --USERNAME = DECODE(P_USERNAME, 'ALL', USERNAME, P_USERNAME)
      
     
      
      
  BEGIN

    IF P_QUERY_TYP = 'DNP_DNP' THEN 
     dbms_output.put_line('DNP_DNP:- '||P_QUERY_TYP); 
      L_PERFLVL_CURR := 'B';
      L_PERFLVL_PREV := 'B';
    ELSIF P_QUERY_TYP = 'DNP_PASS' THEN
      L_PERFLVL_CURR := 'B';
      L_PERFLVL_PREV := 'A';
    ELSIF P_QUERY_TYP = 'DNP_PASSP' THEN
      L_PERFLVL_CURR := 'B';
      L_PERFLVL_PREV := 'P';
    ELSIF P_QUERY_TYP = 'PASS_DNP' THEN
      L_PERFLVL_CURR := 'A';
      L_PERFLVL_PREV := 'B';
    ELSIF P_QUERY_TYP = 'PASS_PASS' THEN
      L_PERFLVL_CURR := 'A';
      L_PERFLVL_PREV := 'A';
    ELSIF P_QUERY_TYP = 'PASS_PASSP' THEN
      L_PERFLVL_CURR := 'A';
      L_PERFLVL_PREV := 'P';
    ELSIF P_QUERY_TYP = 'PASSP_DNP' THEN
      L_PERFLVL_CURR := 'P';
      L_PERFLVL_PREV := 'B';
    ELSIF P_QUERY_TYP = 'PASSP_PASS' THEN
      L_PERFLVL_CURR := 'P';
      L_PERFLVL_PREV := 'A';
    ELSIF P_QUERY_TYP = 'PASSP_PASSP' THEN
      L_PERFLVL_CURR := 'P';
      L_PERFLVL_PREV := 'P';
    END IF;
    
     SELECT PRODUCTID INTO V_PRODUCTID_PREV
        FROM PRODUCT 
        WHERE PRODUCT_CODE = 
                  (SELECT SUBSTR(PDT.PRODUCT_CODE,1,LENGTH (PDT.PRODUCT_CODE)-2)||SUBSTR(ADM.ADMIN_YEAR-1,-2,2) AS PREV_PRODUCT_CODE
                  FROM PRODUCT PDT,
                       ADMIN_DIM ADM,
                       CUST_PRODUCT_LINK CPL
                  WHERE CPL.CUST_PROD_ID = P_CUST_PROD_ID 
                  AND CPL.ADMINID = ADM.ADMINID
                  AND CPL.PRODUCTID = PDT.PRODUCTID );
        
      SELECT CUSTOMERID INTO V_CUSTOMER_ID
      FROM CUST_PRODUCT_LINK  WHERE CUST_PROD_ID = P_CUST_PROD_ID;
          
                

    OPEN C_USERID;
    LOOP
      FETCH C_USERID BULK COLLECT
        INTO LV_TYP_VARCHAR_ARR LIMIT 500;

      IF P_IS_FIRST_LD = 'N' THEN
           INSERT INTO CHECK_RECS
             (TYPE1, TIMETO)
           VALUES
             ('Start Delete' || P_QUERY_TYP, SYSDATE);
           FORALL I IN LV_TYP_VARCHAR_ARR.FIRST .. LV_TYP_VARCHAR_ARR.LAST
             DELETE FROM PERF_MATRIX_FACT
              WHERE USER_ID = LV_TYP_VARCHAR_ARR(I)
                AND perf_level = P_QUERY_TYP
                AND CUST_PROD_ID = P_CUST_PROD_ID ;
           COMMIT;
           INSERT INTO CHECK_RECS
             (TYPE1, TIMETO)
           VALUES
             ('End Delete ' || P_QUERY_TYP ||' Subtest: '||P_SUBTESTCODE, SYSDATE);
      END IF;

      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('Start ' || P_QUERY_TYP||'| Subtest: '||P_SUBTESTCODE, SYSDATE);

      INSERT INTO MATRIX_SELECTION_LOOKUP_1
      
           SELECT DISTINCT /*+index(USL NIDX_USERS_SEL_LKP1)*/ USR.USERID,
                        USR.USERNAME,
                        CPL.ADMINID,
                        USL.GRADEID,
                        USL.SUBTESTID,
                        USL.CUST_PROD_ID,
                        ou.ORG_NODEID,
                        USC.STUDENT_BIO_ID
          FROM USERS                 USR,
               org_users             ou , 
               USER_SELECTION_LOOKUP USL,
               --ORG_NODE_DIM          ORD,
               USC_LINK              USC,
               CUST_PRODUCT_LINK CPL
         WHERE USR.ACTIVATION_STATUS = 'SS'
           AND usr.userid = ou.userid 
           AND ou.org_node_level = 3 
           --AND USR.USERNAME ='102895183841' .
           AND USR.USERID  IN (SELECT COLUMN_VALUE  FROM TABLE(LV_TYP_VARCHAR_ARR))
           AND USR.USERID = USL.USERID
           AND CPL.CUST_PROD_ID = P_CUST_PROD_ID
           AND CPL.CUST_PROD_ID = USL.CUST_PROD_ID
         --AND ou.ADMINID = CPL.ADMINID (commented as for the user created in the previous admin students will be linked in current admin to this user )         
--           AND ou.USERID = USC.USERID -- Removed to use the new data structure 
          AND ou.org_user_id =  usc.org_user_id
           AND USL.SUBTESTID = USC.SUBTESTID  ; 

      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('End---MATRIX_SELECTION_LOOKUP_1|'||' Subtest: '||P_SUBTESTCODE, SYSDATE);

      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('Start---MATRIX_SELECTION_LOOKUP_2|'||' Subtest: '||P_SUBTESTCODE, SYSDATE);
   

      INSERT INTO MATRIX_SELECTION_LOOKUP_2
        SELECT *
          FROM MATRIX_SELECTION_LOOKUP_1 M
         WHERE EXISTS (SELECT 1
                  FROM GRW_SUBTEST_SCORE_FACT SCR, org_lstnode_link oln 
                 WHERE M.ORG_NODEID = oln.org_nodeid  
                   AND SCR.ORG_NODEID = oln.org_lstnodeid                 
                   AND M.ADMINID = SCR.ADMINID
                   AND M.GRADEID = SCR.GRADEID
                   AND M.SUBTESTID = SCR.SUBTESTID
                   AND M.CUST_PROD_ID = SCR.CUST_PROD_ID
                   AND M.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
                   AND SCR.CUST_PROD_ID = P_CUST_PROD_ID
                   AND SCR.PL = L_PERFLVL_CURR
                   AND ROWNUM = 1);

      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('End---MATRIX_SELECTION_LOOKUP_2|'||' Subtest: '||P_SUBTESTCODE, SYSDATE);
      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('Start---MATRIX_SELECTION_LOOKUP_3|'||' Subtest: '||P_SUBTESTCODE, SYSDATE);
        
    /*          SELECT COUNT(1) INTO lv_cnt3  
      FROM MATRIX_SELECTION_LOOKUP_2; 
      
             dbms_output.put_line('CNT STEP2:- '||lv_cnt3); */
/*BEGIN*/
      FOR R_SUBTESTID IN C_SUBTEST LOOP
        INSERT INTO MATRIX_SELECTION_LOOKUP_3
          SELECT M.*,
                 STD.EXT_STUDENT_ID,
                 --STD.STUDENT_ID,
                 STD.LAST_NAME || ', ' || STD.FIRST_NAME || ' ' ||
                 STD.MIDDLE_NAME AS STDUENT_NAME
               /*(SELECT A.STUDENT_BIO_ID
                FROM STUDENT_BIO_DIM A, ADMIN_DIM ADM, GRADE_DIM grd
               WHERE EXT_STUDENT_ID =STD.EXT_STUDENT_ID-- '200906824'
                 AND ADM.ADMIN_SEQ = (SELECT ADM1.ADMIN_SEQ - 1
                                        FROM ADMIN_DIM ADM1
                                       WHERE ADM1.ADMINID = STD.ADMINID)
                 AND GRD.GRADE_SEQ = (SELECT GRD1.GRADE_SEQ - 1
                                        FROM GRADE_DIM GRD1
                                       WHERE GRD1.GRADEID = STD.GRADEID)
                 AND A.ADMINID = ADM.ADMINID
                 AND A.GRADEID = GRD.GRADEID
                 AND A.CUSTOMERID = V_CUSTOMER_ID
                 AND ROWNUM =1) AS PREV_STUDENT_BIO_ID  */ 
            FROM STUDENT_BIO_DIM           STD,
                 MATRIX_SELECTION_LOOKUP_2 M,
                 ORG_LSTNODE_LINK          OLN
           WHERE STD.STUDENT_BIO_ID = M.STUDENT_BIO_ID
             AND M.SUBTESTID = R_SUBTESTID.SUBTESTID
             AND M.ORG_NODEID = OLN.ORG_NODEID
             AND STD.ORG_NODEID = OLN.ORG_LSTNODEID
            -- AND M.ORG_NODEID = STD.ORG_NODEID
             AND M.GRADEID = STD.GRADEID
             AND M.ADMINID = STD.ADMINID;
      END LOOP;  
/*  EXCEPTION
  WHEN OTHERS THEN 
  NULL;    
  END;  */  
      

      
/*      SELECT COUNT(1) INTO lv_cnt3  
      FROM MATRIX_SELECTION_LOOKUP_3; 
      
             dbms_output.put_line('CNT STEP3:- '||lv_cnt3); */
      
      
      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('End---MATRIX_SELECTION_LOOKUP_3|'||' Subtest: '||P_SUBTESTCODE, SYSDATE);

        
           -- INSERT INTO MATRIX_SELECTION_LOOKUP_temp SELECT * FROM MATRIX_SELECTION_LOOKUP_3 ; 
      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('Start---PERF_MATRIX_FACT|'||' Subtest: '||P_SUBTESTCODE, SYSDATE);          

              INSERT INTO PERF_MATRIX_FACT
                   SELECT 
                   perf_matrix_seq.nextval, 
                   (SELECT org_nodeid FROM student_bio_dim a WHERE a.student_bio_id = curr.student_bio_id) AS org_nodeid,
                   (SELECT cust_prod_id FROM results_grt_fact a WHERE a.student_bio_id = curr.student_bio_id) AS cust_prod_id ,  
                   CURR.STDUENT_NAME AS STDUENT_NAME,
                   CURR.STUDENT_BIO_ID,
                   STD.STUDENT_BIO_ID,
                   --CURR.PREV_STUDENT_BIO_ID,
                   CURR_SUBTEST.SUBTESTID,
                   --PREV_SUBTEST.SUBTESTID,
                   --SCR.ASSESSMENTID,
                   CURR.GRADEID AS CURR_GARDEID,
                   GRD.GRADEID AS PREV_GARDEID,
                   CURR.ADMINID AS CURR_ADMINID,
                   ADM.ADMINID AS PREV_ADMINID,
                   (SELECT SS FROM GRW_SUBTEST_SCORE_FACT SC 
                          WHERE SC.STUDENT_BIO_ID = CURR.STUDENT_BIO_ID 
                            AND SC.SUBTESTID =CURR_SUBTEST.SUBTESTID
                            AND SC.GRADEID =CURR.GRADEID
                            AND SC.ADMINID =  CURR.ADMINID
                            AND SC.CUST_PROD_ID = CURR.CUST_PROD_ID
                            AND SC.PL =  L_PERFLVL_CURR ) AS CURR_SC,
                   SCR.SS AS PERV_SC ,  
                   --CURR.USERNAME,
                   (SELECT userid FROM users usr WHERE usr.username = curr.username) userid , 
                   P_QUERY_TYP AS perf_level, 
                   SYSDATE 
                   --SYSDATE
              FROM MATRIX_SELECTION_LOOKUP_3 CURR,
                   SUBTEST_DIM               CURR_SUBTEST,
                   SUBTEST_DIM               PREV_SUBTEST,
                   STUDENT_BIO_DIM           STD,
                   GRADE_DIM                 GRD,
                   ADMIN_DIM                 ADM,
                   GRW_SUBTEST_SCORE_FACT        SCR
             WHERE CURR_SUBTEST.SUBTEST_CODE = PREV_SUBTEST.SUBTEST_CODE
               AND CURR_SUBTEST.SUBTESTID = CURR.SUBTESTID
               AND PREV_SUBTEST.CONTENTID IN
                   (SELECT CONTENTID
                      FROM CONTENT_DIM
                     WHERE ASSESSMENTID IN
                           (SELECT ASSESSMENTID --,ASSESSMENT_name
                              FROM ASSESSMENT_DIM
                             WHERE PRODUCTID = V_PRODUCTID_PREV))
               AND STD.EXT_STUDENT_ID = CURR.EXT_STUDENT_ID
               AND GRD.GRADE_SEQ =
                   (SELECT GRD1.GRADE_SEQ - 1
                      FROM GRADE_DIM GRD1
                     WHERE GRD1.GRADEID = CURR.GRADEID)
               AND ADM.ADMIN_SEQ =
                   (SELECT ADM1.ADMIN_SEQ - 1
                      FROM ADMIN_DIM ADM1
                     WHERE ADM1.ADMINID = CURR.ADMINID)
               AND scr.cust_prod_id = (SELECT cust_prod_id FROM cust_product_link WHERE productid =V_PRODUCTID_PREV AND customerid =V_CUSTOMER_ID)      
               AND STD.ADMINID = ADM.ADMINID
               AND STD.GRADEID = GRD.GRADEID
               AND SCR.PL = L_PERFLVL_PREV
               AND SCR.SUBTESTID = PREV_SUBTEST.SUBTESTID
               AND SCR.ADMINID = ADM.ADMINID
               AND SCR.GRADEID = GRD.GRADEID
               AND STD.STUDENT_BIO_ID = SCR.STUDENT_BIO_ID
               /*AND CURR.PREV_STUDENT_BIO_ID = SCR.STUDENT_BIO_ID*/;

 COMMIT ; 

      INSERT INTO CHECK_RECS
        (TYPE1, TIMETO)
      VALUES
        ('End ' || P_QUERY_TYP||'| Subtest: '||P_SUBTESTCODE, SYSDATE);

      EXIT WHEN C_USERID%NOTFOUND;
      LV_TYP_VARCHAR_ARR.DELETE;
    END LOOP;
    CLOSE C_USERID;
    
     
  END ISTEP_MATRIX_REPORT_LOAD;

  PROCEDURE IDX_BUILD(P_IS_FIRST VARCHAR2, P_DUMMY OUT NUMBER) IS
    L_COUNT NUMBER := 0;
    L_STATEMENT VARCHAR2(32000);
  BEGIN

    /*Drop index when load starts*/
    IF P_IS_FIRST = 'Y' THEN
      L_COUNT := 0;
      SELECT  COUNT(1) INTO L_COUNT  FROM ALL_INDEXES A WHERE A.INDEX_NAME = 'IDX_PERF_MATRIX_FACT_1';
      IF L_COUNT = 1 THEN
        L_STATEMENT := 'DROP INDEX IDX_PERF_MATRIX_FACT_1';
        EXECUTE IMMEDIATE L_STATEMENT;
      END IF ;
      L_COUNT := 0;
      SELECT  COUNT(1) INTO L_COUNT  FROM ALL_INDEXES A WHERE A.INDEX_NAME = 'IDX_PERF_MATRIX_FACT_2';
      IF L_COUNT = 1 THEN 
        L_STATEMENT := 'DROP INDEX IDX_PERF_MATRIX_FACT_2';
        EXECUTE IMMEDIATE L_STATEMENT;
      END IF ;
    ELSE
      /*Create index on loading complete*/
      L_STATEMENT := 'CREATE INDEX IDX_PERF_MATRIX_FACT_1 ON PERF_MATRIX_FACT (USER_ID,CUST_PROD_ID,SUBTESTID,CURR_GRADEID,PREV_GRADEID,CURR_ADMINID,PREV_ADMINID)';
      EXECUTE IMMEDIATE L_STATEMENT;
      
       L_STATEMENT := 'CREATE INDEX IDX_PERF_MATRIX_FACT_2 ON PERF_MATRIX_FACT (USER_ID,CUST_PROD_ID,SUBTESTID,CURR_GRADEID,PREV_GRADEID,CURR_ADMINID,PREV_ADMINID,PERF_LEVEL)';/*LOCAL PARALLEL 4*/
      EXECUTE IMMEDIATE L_STATEMENT;
    END IF;

    P_DUMMY := 1;

  END IDX_BUILD;

END PKG_ISTEP_MATRIX;
/
