CREATE OR REPLACE PACKAGE PRISM_REPORT IS

  TYPE GET_REFCURSOR IS REF CURSOR;
 PROCEDURE ACADEMIC_STND_SUMM_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_OUT_CUR_ACAD_STND_SUMM OUT GET_REFCURSOR);

  PROCEDURE PROF_ROSTER_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_IN_CLASS               IN VARCHAR2,
                                      P_OUT_CUR_PROF_ROSTER OUT GET_REFCURSOR,
                                      DIST_COUNT OUT GET_REFCURSOR,
                                      SCHOOL_COUNT OUT GET_REFCURSOR,
                                      GRADE_COUNT  OUT GET_REFCURSOR,
                                      CLASS_COUNT  OUT GET_REFCURSOR);

  PROCEDURE ACAD_STND_FREQ_DIST_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_OUT_CUR_ASFD OUT GET_REFCURSOR,
                                      P_OUT_CUR_ASFD1 OUT GET_REFCURSOR);

  PROCEDURE APP_SKILLS_FREQ_DIST_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_IN_SUBJECT             IN VARCHAR2,
                                      P_OUT_CUR_ASFD OUT GET_REFCURSOR,
                                      P_OUT_CUR_ASFD1 OUT GET_REFCURSOR);

   PROCEDURE PROF_PERFOR_SUMM_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_OUT_CUR_PROF_PERF_SUMM OUT GET_REFCURSOR);

   PROCEDURE DISAGGREGATION_SUMM_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_IN_SUBJECT             IN VARCHAR2,
                                      P_OUT_CUR_DISAGG_SUMM OUT GET_REFCURSOR);

END PRISM_REPORT;
/
CREATE OR REPLACE PACKAGE BODY PRISM_REPORT IS


  --ACADEMIC STANDARD SUMMARY REPORT
  PROCEDURE ACADEMIC_STND_SUMM_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_OUT_CUR_ACAD_STND_SUMM OUT GET_REFCURSOR) IS
  
  BEGIN
  
    --STATE MODE
    IF P_IN_CORP = 'ALL' THEN
    
      OPEN P_OUT_CUR_ACAD_STND_SUMM FOR
        SELECT DISTINCT A.SUBTEST_NAME,
                        A.OBID,
                        A.OBJECTIVE_NAME,
                        FACT.ITEMTYPE,
                        FACT.PONUMBERSPOSSIBLE,
                        FACT.IPI_AT_PASS,
                        FACT.STATE_MEANNUMBERCORRECT,
                        FACT.STATE_MEANIPI,
                        FACT.STATE_IPIDIFFERENCE,
                        FACT.STATE_NUMBERMASTERY,
                        FACT.STATE_PERCENTMASTERY
        
          FROM ACAD_STD_SUMM_FACT FACT,
               (SELECT DISTINCT PRO.PRODUCT_NAME TEST_ADMIN,
                                TP.TP_TYPE TEST_PGM,
                                ORG1.ORG_NODEID STID,
                                GDIM.GRADEID GID,
                                GDIM.GRADE_NAME,
                                SDIM.SUBTESTID SID,
                                SDIM.SUBTEST_NAME,
                                ODIM.OBJECTIVEID OBID,
                                ODIM.OBJECTIVE_NAME
                
                  FROM CUST_PRODUCT_LINK      CUST,
                       TEST_PROGRAM           TP,
                       ORG_NODE_DIM           ORG1,
                       GRADE_DIM              GDIM,
                       SUBTEST_DIM            SDIM,
                       ORG_PRODUCT_LINK       ORG_LINK,
                       PRODUCT                PRO,
                       ORG_TEST_PROGRAM_LINK  TEST_LINK,
                       GRADE_SELECTION_LOOKUP GLOOKUP,
                       VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
                       ASSESSMENT_DIM         ASS,
                       OBJECTIVE_DIM          ODIM,
                       USERS                  U,
                       ORG_USERS              OU
                 WHERE CUST.PRODUCTID = PRO.PRODUCTID
                   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
                   AND ORG_LINK.ORG_NODEID = ORG1.ORG_NODEID
                   AND ORG1.ORG_NODE_LEVEL = 1
                   AND ORG1.ORG_NODEID = TEST_LINK.ORG_NODEID
                   AND TEST_LINK.TP_ID = TP.TP_ID
                   AND ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
                   AND GLOOKUP.GRADEID = GDIM.GRADEID
                   AND  CUST.PRODUCTID = ASS.PRODUCTID
                   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
                   AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
                   AND GDIM.GRADEID = VW.GRADEID
                   AND VW.SUBTESTID = SDIM.SUBTESTID
                   AND VW.OBJECTIVEID = ODIM.OBJECTIVEID
                   AND U.USERNAME = 'ctbadmin'
                   AND U.USERID = OU.USERID
                   AND OU.ORG_NODEID = ORG1.ORG_NODEID) A
        
         WHERE A.STID = FACT.ORG_NODEID
           AND A.GID = FACT.GRADEID
           AND A.SID = FACT.SUBTESTID
           AND A.OBID = FACT.OBJECTIVEID
           AND A.TEST_ADMIN = P_IN_TEST_ADMINISTRATION
           AND A.TEST_PGM = P_IN_TEST_PROGRAM
           AND A.GRADE_NAME = P_IN_GRADE
         ORDER BY A.SUBTEST_NAME, A.OBID;
    
    ELSIF P_IN_CORP != 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
    
      OPEN P_OUT_CUR_ACAD_STND_SUMM FOR
        SELECT DISTINCT A.SUBTEST_NAME,
                        A.OBID,
                        A.OBJECTIVE_NAME,
                        FACT.ITEMTYPE,
                        FACT.PONUMBERSPOSSIBLE,
                        FACT.IPI_AT_PASS,
                        FACT.STATE_MEANNUMBERCORRECT,
                        FACT.STATE_MEANIPI,
                        FACT.STATE_IPIDIFFERENCE,
                        FACT.STATE_NUMBERMASTERY,
                        FACT.STATE_PERCENTMASTERY,
                        FACT.CORP_MEANNUMBERCORRECT,
                        FACT.CORP_MEANIPI,
                        FACT.CORP_IPIDIFFERENCE,
                        FACT.CORP_NUMBERMASTERY,
                        FACT.CORP_PERCENTMASTERY
        
          FROM ACAD_STD_SUMM_FACT FACT,
               (SELECT DISTINCT PRO.PRODUCT_NAME TEST_ADMIN,
                                TP.TP_TYPE TEST_PGM,
                                ORG2.ORG_NODEID DISID,
                                ORG2.ORG_NODE_NAME DIS_NAME,
                                GDIM.GRADEID GID,
                                GDIM.GRADE_NAME,
                                SDIM.SUBTESTID SID,
                                SDIM.SUBTEST_NAME,
                                ODIM.OBJECTIVEID OBID,
                                ODIM.OBJECTIVE_NAME
                
                  FROM CUST_PRODUCT_LINK      CUST,
                       TEST_PROGRAM           TP,
                       ORG_NODE_DIM           ORG2,
                       GRADE_DIM              GDIM,
                       VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
                       SUBTEST_DIM            SDIM,
                       ORG_PRODUCT_LINK       ORG_LINK,
                       PRODUCT                PRO,
                       ORG_TEST_PROGRAM_LINK  TEST_LINK,
                       GRADE_SELECTION_LOOKUP GLOOKUP,
                       ASSESSMENT_DIM         ASS,
                       OBJECTIVE_DIM          ODIM,
                       USERS                  U,
                       ORG_USERS              OU
                
                 WHERE CUST.PRODUCTID = PRO.PRODUCTID
                   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
                   AND ORG_LINK.ORG_NODEID = ORG2.ORG_NODEID
                   AND ORG2.ORG_NODE_LEVEL = 2
                   AND ORG2.ORG_NODEID = TEST_LINK.ORG_NODEID
                   AND TEST_LINK.TP_ID = TP.TP_ID
                   AND ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID
                   AND GLOOKUP.GRADEID = GDIM.GRADEID
                   AND CUST.PRODUCTID = ASS.PRODUCTID
                   AND ASS.ASSESSMENTID = VW.ASSESSMENTID
                   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
                   AND VW.SUBTESTID = SDIM.SUBTESTID
                   AND VW.OBJECTIVEID = ODIM.OBJECTIVEID
                   AND U.USERNAME = 'ctbadmin'
                   AND U.USERID = OU.USERID
                   AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID) A
        
         WHERE A.DISID = FACT.ORG_NODEID
           AND A.GID = FACT.GRADEID
           AND A.SID = FACT.SUBTESTID
           AND A.OBID = FACT.OBJECTIVEID
           AND A.TEST_ADMIN = P_IN_TEST_ADMINISTRATION
           AND A.TEST_PGM = P_IN_TEST_PROGRAM
           AND A.DIS_NAME = P_IN_CORP
           AND A.GRADE_NAME = P_IN_GRADE
         ORDER BY A.SUBTEST_NAME, A.OBID;
    
    ELSIF P_IN_CORP != 'ALL' AND P_IN_SCHOOL != 'ALL' THEN
    
      OPEN P_OUT_CUR_ACAD_STND_SUMM FOR
        SELECT DISTINCT A.SUBTEST_NAME,
                        A.OBID,
                        A.OBJECTIVE_NAME,
                        FACT.ITEMTYPE,
                        FACT.PONUMBERSPOSSIBLE,
                        FACT.IPI_AT_PASS,
                        
                        FACT.CORP_MEANNUMBERCORRECT,
                        FACT.CORP_MEANIPI,
                        FACT.CORP_IPIDIFFERENCE,
                        FACT.CORP_NUMBERMASTERY,
                        FACT.CORP_PERCENTMASTERY,
                        FACT.SCHOOL_MEANNUMBERCORRECT,
                        FACT.SCHOOL_MEANIPI,
                        FACT.SCHOOL_IPIDIFFERENCE,
                        FACT.SCHOOL_NUMBERMASTERY,
                        FACT.SCHOOL_PERCENTMASTERY
        
          FROM ACAD_STD_SUMM_FACT FACT,
               (SELECT DISTINCT PRO.PRODUCT_NAME TEST_ADMIN,
                                TP.TP_TYPE TEST_PGM,
                                ORG2.ORG_NODEID DISID,
                                ORG2.ORG_NODE_NAME DIS_NAME,
                                ORG3.ORG_NODEID SCHID,
                                ORG3.ORG_NODE_NAME SCH_NAME,
                                GDIM.GRADEID GID,
                                GDIM.GRADE_NAME,
                                SDIM.SUBTESTID SID,
                                SDIM.SUBTEST_NAME,
                                ODIM.OBJECTIVEID OBID,
                                ODIM.OBJECTIVE_NAME
                
                  FROM CUST_PRODUCT_LINK      CUST,
                       TEST_PROGRAM           TP,
                       ORG_NODE_DIM           ORG2,
                       ORG_NODE_DIM           ORG3,
                       GRADE_DIM              GDIM,
                       VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
                       SUBTEST_DIM            SDIM,
                       ORG_PRODUCT_LINK       ORG_LINK,
                       PRODUCT                PRO,
                       ORG_TEST_PROGRAM_LINK  TEST_LINK,
                       GRADE_SELECTION_LOOKUP GLOOKUP,
                       ASSESSMENT_DIM         ASS,
                       OBJECTIVE_DIM          ODIM,
                       USERS                  U,
                       ORG_USERS              OU
                
                 WHERE CUST.PRODUCTID = PRO.PRODUCTID
                   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
                   AND ORG_LINK.ORG_NODEID = ORG2.ORG_NODEID
                   AND ORG2.ORG_NODE_LEVEL = 2
                   AND ORG2.ORG_NODEID = ORG3.PARENT_ORG_NODEID
                   AND ORG3.ORG_NODEID = TEST_LINK.ORG_NODEID --- MAP SCHOOL LEVEL ORG_NODEID WITH TEST_PROGRAM
                   AND TEST_LINK.TP_ID = TP.TP_ID
                   AND ORG3.ORG_NODEID = GLOOKUP.ORG_NODEID --- MAP SCHOOL LEVEL ORG_NODEID WITH GRADE_SELECTION_LOOKUP
                   AND GLOOKUP.GRADEID = GDIM.GRADEID
                   AND GDIM.GRADEID = VW.GRADEID
                   AND CUST.PRODUCTID = ASS.PRODUCTID
                   AND ASS.ASSESSMENTID = VW.ASSESSMENTID
                   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
                   AND VW.SUBTESTID = SDIM.SUBTESTID
                   AND VW.OBJECTIVEID = ODIM.OBJECTIVEID
                   AND U.USERNAME = 'ctbadmin'
                   AND U.USERID = OU.USERID
                   AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID) A
        
         WHERE A.SCHID = FACT.ORG_NODEID
           AND A.GID = FACT.GRADEID
           AND A.SID = FACT.SUBTESTID
           AND A.OBID = FACT.OBJECTIVEID
           AND A.TEST_ADMIN = P_IN_TEST_ADMINISTRATION
           AND A.TEST_PGM = P_IN_TEST_PROGRAM
           AND A.DIS_NAME = P_IN_CORP
           AND A.SCH_NAME = P_IN_SCHOOL
           AND A.GRADE_NAME = P_IN_GRADE
         ORDER BY A.SUBTEST_NAME, A.OBID;
    
    END IF;
    
    
    
  END ACADEMIC_STND_SUMM_REPORT;


  ---PROFICIENCY ROSTER REPORT
  PROCEDURE PROF_ROSTER_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2,
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_IN_CLASS               IN VARCHAR2,
                                      P_OUT_CUR_PROF_ROSTER OUT GET_REFCURSOR,
                                      DIST_COUNT OUT GET_REFCURSOR,
                                      SCHOOL_COUNT OUT GET_REFCURSOR,
                                      GRADE_COUNT  OUT GET_REFCURSOR,
                                      CLASS_COUNT  OUT GET_REFCURSOR) IS
                                      
   BEGIN
   
   IF   P_IN_CORP != 'ALL' AND  P_IN_SCHOOL != 'ALL' AND P_IN_CLASS = 'ALL' THEN

OPEN P_OUT_CUR_PROF_ROSTER FOR 
SELECT DISTINCT 
                A.NAME,
                A.SUBTEST_NAME,
                FACT.ENGLAN_ARTS_SCALE_SCORE,
                FACT.MATHEMATICS_SCALE_SCORE,
                FACT.SCIENCE_SCALE_SCORE
               
                

FROM RESULTS_GRT_FACT FACT,
(SELECT DISTINCT CUST.CUST_PROD_ID,
                PRO.PRODUCT_NAME,
                TP.TP_TYPE,
                ORG1.ORG_NODEID,
                ORG1.ORG_NODE_NAME DIS_NAME,
                ORG2.ORG_NODEID,
                ORG2.ORG_NODE_NAME SCH_NAME,
                GDIM.GRADEID GID,
                GDIM.GRADE_NAME G_NAME,
                ORG3.ORG_NODEID CL_ID,
                ORG3.ORG_NODE_NAME CL_NAME,
                BIO.STUDENT_BIO_ID,
                BIO.LAST_NAME ||', '||BIO.FIRST_NAME AS NAME,
                SDIM.SUBTESTID,
                SDIM.SUBTEST_NAME
                

  FROM CUST_PRODUCT_LINK      CUST,
       TEST_PROGRAM           TP,
       ORG_NODE_DIM           ORG1,
       ORG_NODE_DIM           ORG2,
       ORG_NODE_DIM           ORG3,
       GRADE_DIM              GDIM,
       SUBTEST_DIM            SDIM,
       SUBTEST_SCORE_FACT     SCORE_FACT,
       ORG_PRODUCT_LINK       ORG_LINK,
       PRODUCT                PRO,
       ORG_TEST_PROGRAM_LINK  TEST_LINK,
       GRADE_SELECTION_LOOKUP GLOOKUP,
       USERS                  U,
       ORG_USERS              OU,
       STUDENT_BIO_DIM        BIO

 WHERE CUST.PRODUCTID = PRO.PRODUCTID
   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
   AND ORG_LINK.ORG_NODEID = ORG1.ORG_NODEID
   AND ORG1.ORG_NODE_LEVEL = 2             --COMPARE WITH DISTRICT LAVEL ORG_NODEID
   AND ORG1.ORG_NODEID = ORG2.PARENT_ORG_NODEID
   AND ORG2.ORG_NODEID = TEST_LINK.ORG_NODEID   --- MAP SCHOOL LEVEL ORG_NODEID WITH TEST_PROGRAM
   AND TEST_LINK.TP_ID = TP.TP_ID                    
   AND ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID    --- MAP SCHOOL LEVEL ORG_NODEID WITH GRADE_SELECTION_LOOKUP
   AND GLOOKUP.GRADEID = GDIM.GRADEID
   AND GDIM.GRADEID    = SCORE_FACT.GRADEID
   AND GLOOKUP.ASSESSMENTID = SCORE_FACT.ASSESSMENTID
  AND U.USERNAME = 'ctbadmin'                        -- LOGIN USER NAME
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG1.PARENT_ORG_NODEID
  AND ORG2.ORG_NODEID = ORG3.PARENT_ORG_NODEID
  AND ORG3.ORG_NODEID = SCORE_FACT.ORG_NODEID
  AND ORG3.ORG_NODEID = BIO.ORG_NODEID
  AND GDIM.GRADEID    = BIO.GRADEID
  AND BIO.STUDENT_BIO_ID = SCORE_FACT.STUDENT_BIO_ID
  AND CUST.CUST_PROD_ID = SCORE_FACT.CUST_PROD_ID
  AND SCORE_FACT.SUBTESTID = SDIM.SUBTESTID) A
 
 WHERE A.CUST_PROD_ID = FACT.CUST_PROD_ID
   AND A.CL_ID = FACT.ORG_NODEID
   AND A.STUDENT_BIO_ID = FACT.STUDENT_BIO_ID
   AND A.GID = FACT.GRADEID
   AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION   --TEST ADMINISTRATION
   AND A.TP_TYPE = P_IN_TEST_PROGRAM          -- TEST PROGRAM.CHANGE IT ACCORDING TO REQUIREMENT
   AND A.DIS_NAME = P_IN_CORP  
   AND A.SCH_NAME = P_IN_SCHOOL 
   AND A.G_NAME = P_IN_GRADE          
 ORDER BY A.NAME,A.SUBTEST_NAME;
 
 

   
   ELSIF P_IN_CORP != 'ALL' AND  P_IN_SCHOOL != 'ALL' AND P_IN_CLASS != 'ALL' THEN 
  
  OPEN P_OUT_CUR_PROF_ROSTER FOR 
   SELECT DISTINCT 
                A.NAME,
                A.SUBTEST_NAME,
                FACT.ENGLAN_ARTS_SCALE_SCORE,
                FACT.MATHEMATICS_SCALE_SCORE,
                FACT.SCIENCE_SCALE_SCORE
               
                

FROM RESULTS_GRT_FACT FACT,
(SELECT DISTINCT CUST.CUST_PROD_ID,
                PRO.PRODUCT_NAME,
                TP.TP_TYPE,
                ORG1.ORG_NODEID,
                ORG1.ORG_NODE_NAME DIS_NAME,
                ORG2.ORG_NODEID,
                ORG2.ORG_NODE_NAME SCH_NAME,
                GDIM.GRADEID GID,
                GDIM.GRADE_NAME G_NAME,
                ORG3.ORG_NODEID CL_ID,
                ORG3.ORG_NODE_NAME CL_NAME,
                BIO.STUDENT_BIO_ID,
                BIO.LAST_NAME ||', '||BIO.FIRST_NAME AS NAME,
                SDIM.SUBTESTID,
                SDIM.SUBTEST_NAME
                

  FROM CUST_PRODUCT_LINK      CUST,
       TEST_PROGRAM           TP,
       ORG_NODE_DIM           ORG1,
       ORG_NODE_DIM           ORG2,
       ORG_NODE_DIM           ORG3,
       GRADE_DIM              GDIM,
       SUBTEST_DIM            SDIM,
       SUBTEST_SCORE_FACT     SCORE_FACT,
       ORG_PRODUCT_LINK       ORG_LINK,
       PRODUCT                PRO,
       ORG_TEST_PROGRAM_LINK  TEST_LINK,
       GRADE_SELECTION_LOOKUP GLOOKUP,
       USERS                  U,
       ORG_USERS              OU,
       STUDENT_BIO_DIM        BIO

 WHERE CUST.PRODUCTID = PRO.PRODUCTID
   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
   AND ORG_LINK.ORG_NODEID = ORG1.ORG_NODEID
   AND ORG1.ORG_NODE_LEVEL = 2             --COMPARE WITH DISTRICT LAVEL ORG_NODEID
   AND ORG1.ORG_NODEID = ORG2.PARENT_ORG_NODEID
   AND ORG2.ORG_NODEID = TEST_LINK.ORG_NODEID   --- MAP SCHOOL LEVEL ORG_NODEID WITH TEST_PROGRAM
   AND TEST_LINK.TP_ID = TP.TP_ID                    
   AND ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID    --- MAP SCHOOL LEVEL ORG_NODEID WITH GRADE_SELECTION_LOOKUP
   AND GLOOKUP.GRADEID = GDIM.GRADEID
   AND GDIM.GRADEID    = SCORE_FACT.GRADEID
   AND GLOOKUP.ASSESSMENTID = SCORE_FACT.ASSESSMENTID
  AND U.USERNAME = 'ctbadmin'                        -- LOGIN USER NAME
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG1.PARENT_ORG_NODEID
  AND ORG2.ORG_NODEID = ORG3.PARENT_ORG_NODEID
  AND ORG3.ORG_NODEID = SCORE_FACT.ORG_NODEID
  AND ORG3.ORG_NODEID = BIO.ORG_NODEID
  AND GDIM.GRADEID    = BIO.GRADEID
  AND BIO.STUDENT_BIO_ID = SCORE_FACT.STUDENT_BIO_ID
  AND CUST.CUST_PROD_ID = SCORE_FACT.CUST_PROD_ID
  AND SCORE_FACT.SUBTESTID = SDIM.SUBTESTID) A
 
 WHERE A.CUST_PROD_ID = FACT.CUST_PROD_ID
   AND A.CL_ID = FACT.ORG_NODEID
   AND A.STUDENT_BIO_ID = FACT.STUDENT_BIO_ID
   AND A.GID = FACT.GRADEID
   AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION   --TEST ADMINISTRATION
   AND A.TP_TYPE = P_IN_TEST_PROGRAM          -- TEST PROGRAM.CHANGE IT ACCORDING TO REQUIREMENT
   AND A.DIS_NAME = P_IN_CORP  --COMMENT THIS LINE TO SEE THE REPORT OF ALL DISTRICT
   AND A.SCH_NAME = P_IN_SCHOOL --COMMENT THIS LINE TO SEE THE REPORT OF ALL SCHOOL UNDER A PARTICULAR DISTRICT OR ALL DISTRICT
   AND A.G_NAME = P_IN_GRADE              -- --COMMENT THIS LINE TO SEE THE REPORT OF ALL GRADE UNDER A PARTICULAR SCHOOL OR ALL SCHOOL
   AND A.CL_NAME = P_IN_CLASS          -----COMMENT THIS LINE TO SEE THE REPORT OF ALL CLASS UNDER A PARTICULAR GRADE.
 ORDER BY A.NAME,A.SUBTEST_NAME;
 
END IF;


  OPEN DIST_COUNT FOR 
  
  
SELECT DISTINCT ORG2.ORG_NODE_NAME          
                
  FROM CUST_PRODUCT_LINK      CUST,
       TEST_PROGRAM           TP,
       ORG_NODE_DIM           ORG2,
       ORG_LSTNODE_LINK       LST,
       ORG_PRODUCT_LINK       ORG_LINK,
       PRODUCT                PRO,
       ORG_TEST_PROGRAM_LINK  TEST_LINK,
       USERS                  U,
       ORG_USERS              OU,
       RESULTS_GRT_FACT       FACT
       
 WHERE CUST.PRODUCTID = PRO.PRODUCTID
   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
   AND ORG_LINK.ORG_NODEID = ORG2.ORG_NODEID
   AND ORG2.ORG_NODE_LEVEL = 2
   AND ORG2.ORG_NODEID     = LST.ORG_NODEID
   AND ORG2.ORG_NODEID = TEST_LINK.ORG_NODEID   
   AND TEST_LINK.TP_ID = TP.TP_ID                   
  AND U.USERNAME = 'ctbadmin'                       
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID
  AND CUST.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND LST.ORG_LSTNODEID = FACT.ORG_NODEID
  AND PRO.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
  AND TP.TP_TYPE = P_IN_TEST_PROGRAM
  ORDER BY ORG2.ORG_NODE_NAME;
  
  
  OPEN SCHOOL_COUNT FOR
  
  

SELECT DISTINCT ORG2.ORG_NODE_NAME, ORG3.ORG_NODE_NAME            
                
  FROM CUST_PRODUCT_LINK      CUST,
       TEST_PROGRAM           TP,
       ORG_NODE_DIM           ORG2, 
       ORG_NODE_DIM           ORG3, 
       ORG_NODE_DIM           ORG4,
       ORG_PRODUCT_LINK       ORG_LINK,
       PRODUCT                PRO,
       ORG_TEST_PROGRAM_LINK  TEST_LINK,
       USERS                  U,
       ORG_USERS              OU,
       RESULTS_GRT_FACT       FACT
       
 WHERE CUST.PRODUCTID = PRO.PRODUCTID
   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
   AND ORG_LINK.ORG_NODEID = ORG2.ORG_NODEID
   AND ORG2.ORG_NODE_LEVEL = 2      
   AND ORG2.ORG_NODEID = ORG3.PARENT_ORG_NODEID      
   AND ORG3.ORG_NODEID = ORG4.PARENT_ORG_NODEID 
   AND ORG3.ORG_NODEID = TEST_LINK.ORG_NODEID   
   AND TEST_LINK.TP_ID = TP.TP_ID                   
  AND U.USERNAME = 'ctbadmin'                       
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID
  AND PRO.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
  AND TP.TP_TYPE = P_IN_TEST_PROGRAM
  AND ORG2.ORG_NODE_NAME = P_IN_CORP
  AND ORG4.ORG_NODEID = FACT.ORG_NODEID
  AND CUST.CUST_PROD_ID = FACT.CUST_PROD_ID
  ORDER BY ORG3.ORG_NODE_NAME;
  
  IF P_IN_CORP != 'ALL' AND  P_IN_SCHOOL != 'ALL' THEN
  OPEN GRADE_COUNT FOR 
  SELECT DISTINCT ORG2.ORG_NODE_NAME,
                ORG3.ORG_NODE_NAME,
                GDIM.GRADE_NAME            
                
  FROM CUST_PRODUCT_LINK      CUST,
       TEST_PROGRAM           TP,
       ORG_NODE_DIM           ORG2, 
       ORG_NODE_DIM           ORG3, 
       ORG_NODE_DIM           ORG4,
       ORG_PRODUCT_LINK       ORG_LINK,
       PRODUCT                PRO,
       ORG_TEST_PROGRAM_LINK  TEST_LINK,
       USERS                  U,
       ORG_USERS              OU,
       RESULTS_GRT_FACT       FACT,
       GRADE_SELECTION_LOOKUP GLOOKUP,
       ASSESSMENT_DIM         ASS,
       GRADE_DIM              GDIM  
       
 WHERE CUST.PRODUCTID = PRO.PRODUCTID
   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
   AND ORG_LINK.ORG_NODEID = ORG2.ORG_NODEID
   AND ORG2.ORG_NODE_LEVEL = 2      
   AND ORG2.ORG_NODEID = ORG3.PARENT_ORG_NODEID      
   AND ORG3.ORG_NODEID = ORG4.PARENT_ORG_NODEID 
   AND ORG3.ORG_NODEID = TEST_LINK.ORG_NODEID   
   AND TEST_LINK.TP_ID = TP.TP_ID                   
  AND U.USERNAME = 'ctbadmin'                       
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID
  AND ORG3.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND CUST.PRODUCTID  = ASS.PRODUCTID
  AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND GLOOKUP.GRADEID  = GDIM.GRADEID
  AND PRO.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
  AND TP.TP_TYPE = P_IN_TEST_PROGRAM
  AND ORG2.ORG_NODE_NAME = P_IN_CORP
  AND ORG3.ORG_NODE_NAME = P_IN_SCHOOL
  AND ORG4.ORG_NODEID = FACT.ORG_NODEID
  AND CUST.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND GDIM.GRADEID = FACT.GRADEID
  ORDER BY ORG3.ORG_NODE_NAME, GDIM.GRADE_NAME;
  END IF;
  
  OPEN CLASS_COUNT FOR
  SELECT DISTINCT ORG2.ORG_NODE_NAME,
                ORG3.ORG_NODE_NAME,
                GDIM.GRADE_NAME,
                ORG4.ORG_NODE_NAME          
                
  FROM CUST_PRODUCT_LINK      CUST,
       TEST_PROGRAM           TP,
       ORG_NODE_DIM           ORG2, 
       ORG_NODE_DIM           ORG3, 
       ORG_NODE_DIM           ORG4,
       ORG_PRODUCT_LINK       ORG_LINK,
       PRODUCT                PRO,
       ORG_TEST_PROGRAM_LINK  TEST_LINK,
       USERS                  U,
       ORG_USERS              OU,
       RESULTS_GRT_FACT       FACT,
       GRADE_SELECTION_LOOKUP GLOOKUP,
       ASSESSMENT_DIM         ASS,
       GRADE_DIM              GDIM  
       
 WHERE CUST.PRODUCTID = PRO.PRODUCTID
   AND CUST.CUST_PROD_ID = ORG_LINK.CUST_PROD_ID
   AND ORG_LINK.ORG_NODEID = ORG2.ORG_NODEID
   AND ORG2.ORG_NODE_LEVEL = 2      
   AND ORG2.ORG_NODEID = ORG3.PARENT_ORG_NODEID      
   AND ORG3.ORG_NODEID = ORG4.PARENT_ORG_NODEID 
   AND ORG3.ORG_NODEID = TEST_LINK.ORG_NODEID   
   AND TEST_LINK.TP_ID = TP.TP_ID                   
  AND U.USERNAME = 'ctbadmin'                       
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID
  AND ORG3.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND CUST.PRODUCTID  = ASS.PRODUCTID
  AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND GLOOKUP.GRADEID  = GDIM.GRADEID
  AND PRO.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
  AND TP.TP_TYPE = P_IN_TEST_PROGRAM
  AND ORG2.ORG_NODE_NAME = P_IN_CORP
  AND ORG3.ORG_NODE_NAME = P_IN_SCHOOL
  AND GDIM.GRADE_NAME = P_IN_GRADE
  AND ORG4.ORG_NODEID = FACT.ORG_NODEID
  AND CUST.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND GDIM.GRADEID = FACT.GRADEID
  ORDER BY ORG3.ORG_NODE_NAME, GDIM.GRADE_NAME, ORG4.ORG_NODE_NAME;
  
  

   
END PROF_ROSTER_REPORT;
  

--ACADEMIC STANDARD FREQUENCY DISTRIBUTION

PROCEDURE ACAD_STND_FREQ_DIST_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2, 
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_OUT_CUR_ASFD OUT GET_REFCURSOR,
                                      P_OUT_CUR_ASFD1 OUT GET_REFCURSOR) IS
                                      
IS_PUBLIC NUMBER;                                      
BEGIN


IF P_IN_CORP = 'ALL' THEN

IF P_IN_TEST_PROGRAM = 'PUBLIC' THEN 
   IS_PUBLIC :=1;
   ELSIF P_IN_TEST_PROGRAM = 'NON-PUBLIC' THEN 
   IS_PUBLIC :=0;
   END IF;



OPEN P_OUT_CUR_ASFD FOR

SELECT DISTINCT DIM.SUBTEST_NAME,
                FACT.SCALESCORE,
                FACT.FREQUENCY,
                FACT.PERCENT,
                FACT.CUMULATIVEFREQUENCY,
                FACT.CUMULATIVEPERCENT
               

FROM STFD_FACT FACT,
SUBTEST_DIM    DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        ORG1.ORG_NODEID,
        ORG1.ORG_NODE_NAME,
        GDIM.GRADEID,
        GDIM.GRADE_NAME


FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
     USERS                       U,
     ORG_USERS                   OU,
     ASSESSMENT_DIM              ASS
     
     
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = ORG1.ORG_NODEID
  AND ORG1.ORG_NODE_LEVEL = 1
  AND ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND  CUST.PRODUCTID = ASS.PRODUCTID
  AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
  AND GDIM.GRADEID = VW.GRADEID
  AND U.USERNAME = 'ctbadmin'
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG1.ORG_NODEID)A
  
  
  WHERE A.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND A.ORG_NODEID = FACT.ORG_NODEID
  AND A.GRADEID = FACT.GRADEID
  AND FACT.SUBTESTID = DIM.SUBTESTID
  AND A.PRODUCT_NAME = 'ISTEP+ Spring 2013'
  AND A.GRADE_NAME = 'Grade 3'
  AND FACT.ISPUBLIC = IS_PUBLIC
ORDER BY FACT.SCALESCORE DESC;




OPEN P_OUT_CUR_ASFD1 FOR
SELECT DISTINCT DIM.SUBTEST_NAME,
FACT.NUMBEROFSTUDENTS,
FACT.HIGHSCORE,
FACT.LOWSCORE,
FACT.LOCALPERCENTILE90,
FACT.LOCALPERCENTILE75,
FACT.LOCALPERCENTILE50,
FACT.LOCALPERCENTILE25,
FACT.LOCALPERCENTILE10,
FACT.MEAN,
FACT.STANDARDDEVIATION,
FACT.PASSPLUS_LOWSCORE || '-' || FACT.PASSPLUS_HIGHSCORE,
FACT.PASS_LOWSCORE || '-' || FACT.PASS_HIGHSCORE,
FACT.DNP_LOWSCORE || '-' || FACT.DNP_HIGHSCORE

FROM STFD_FACT FACT,
SUBTEST_DIM    DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        ORG1.ORG_NODEID,
        ORG1.ORG_NODE_NAME,
        GDIM.GRADEID,
        GDIM.GRADE_NAME


FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
     USERS                       U,
     ORG_USERS                   OU,
     ASSESSMENT_DIM              ASS
     
     
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = ORG1.ORG_NODEID
  AND ORG1.ORG_NODE_LEVEL = 1
  AND ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND  CUST.PRODUCTID = ASS.PRODUCTID
  AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
  AND GDIM.GRADEID = VW.GRADEID
  AND U.USERNAME = 'ctbadmin'
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG1.ORG_NODEID)A
  
  
  WHERE A.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND A.ORG_NODEID = FACT.ORG_NODEID
  AND A.GRADEID = FACT.GRADEID
  AND FACT.SUBTESTID = DIM.SUBTESTID
  AND A.PRODUCT_NAME = 'ISTEP+ Spring 2013'
  AND A.GRADE_NAME = 'Grade 3'
  AND FACT.ISPUBLIC = IS_PUBLIC;



ELSIF P_IN_CORP != 'ALL' THEN

OPEN P_OUT_CUR_ASFD FOR
SELECT DISTINCT DIM.SUBTEST_NAME,
                FACT.SCALESCORE,
                FACT.FREQUENCY,
                FACT.PERCENT,
                FACT.CUMULATIVEFREQUENCY,
                FACT.CUMULATIVEPERCENT
               

FROM STFD_FACT FACT,
SUBTEST_DIM    DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        TEST.TP_TYPE,
        ORG2.ORG_NODEID,
        ORG2.ORG_NODE_NAME,
        GDIM.GRADEID,
        GDIM.GRADE_NAME


FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG2,
     TEST_PROGRAM                TEST,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
     USERS                       U,
     ORG_USERS                   OU,
     ASSESSMENT_DIM              ASS
     
     
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = ORG2.ORG_NODEID
  AND ORG2.ORG_NODE_LEVEL = 2
  AND ORG2.ORG_NODEID = TEST_LINK.ORG_NODEID
  AND TEST_LINK.TP_ID = TEST.TP_ID
  AND ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND  CUST.PRODUCTID = ASS.PRODUCTID
  AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
  AND GDIM.GRADEID = VW.GRADEID
  AND U.USERNAME = 'ctbadmin'
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID)A
  
  
  WHERE A.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND A.ORG_NODEID = FACT.ORG_NODEID
  AND A.GRADEID = FACT.GRADEID
  AND FACT.SUBTESTID = DIM.SUBTESTID
  AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
  AND A.TP_TYPE = P_IN_TEST_PROGRAM
  AND A.ORG_NODE_NAME =  P_IN_CORP
  AND A.GRADE_NAME = P_IN_GRADE
ORDER BY FACT.SCALESCORE DESC;


OPEN P_OUT_CUR_ASFD1 FOR
SELECT DISTINCT DIM.SUBTEST_NAME,
FACT.NUMBEROFSTUDENTS,
FACT.HIGHSCORE,
FACT.LOWSCORE,
FACT.LOCALPERCENTILE90,
FACT.LOCALPERCENTILE75,
FACT.LOCALPERCENTILE50,
FACT.LOCALPERCENTILE25,
FACT.LOCALPERCENTILE10,
FACT.MEAN,
FACT.STANDARDDEVIATION,
FACT.PASSPLUS_LOWSCORE || '-' || FACT.PASSPLUS_HIGHSCORE,
FACT.PASS_LOWSCORE || '-' || FACT.PASS_HIGHSCORE,
FACT.DNP_LOWSCORE || '-' || FACT.DNP_HIGHSCORE
               

FROM STFD_FACT FACT,
SUBTEST_DIM    DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        TEST.TP_TYPE,
        ORG2.ORG_NODEID,
        ORG2.ORG_NODE_NAME,
        GDIM.GRADEID,
        GDIM.GRADE_NAME


FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG2,
     TEST_PROGRAM                TEST,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP VW,
     USERS                       U,
     ORG_USERS                   OU,
     ASSESSMENT_DIM              ASS
     
     
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = ORG2.ORG_NODEID
  AND ORG2.ORG_NODE_LEVEL = 2
  AND ORG2.ORG_NODEID = TEST_LINK.ORG_NODEID
  AND TEST_LINK.TP_ID = TEST.TP_ID
  AND ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND  CUST.PRODUCTID = ASS.PRODUCTID
  AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
  AND GDIM.GRADEID = VW.GRADEID
  AND U.USERNAME = 'ctbadmin'
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = ORG2.PARENT_ORG_NODEID)A
  
  
  WHERE A.CUST_PROD_ID = FACT.CUST_PROD_ID
  AND A.ORG_NODEID = FACT.ORG_NODEID
  AND A.GRADEID = FACT.GRADEID
  AND FACT.SUBTESTID = DIM.SUBTESTID
  AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
  AND A.TP_TYPE = P_IN_TEST_PROGRAM
  AND A.ORG_NODE_NAME =  P_IN_CORP
  AND A.GRADE_NAME = P_IN_GRADE;

END IF;

END ACAD_STND_FREQ_DIST_REPORT;



--APPLIED SKILLS FREQUENCY DISTRIBUTION

 PROCEDURE APP_SKILLS_FREQ_DIST_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2, 
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_IN_SUBJECT             IN VARCHAR2,
                                      P_OUT_CUR_ASFD OUT GET_REFCURSOR,
                                      P_OUT_CUR_ASFD1 OUT GET_REFCURSOR) IS
    
   IS_PUBLIC NUMBER;                                  
   BEGIN
   
   
   IF P_IN_TEST_PROGRAM = 'PUBLIC' THEN 
   IS_PUBLIC :=1;
   ELSIF P_IN_TEST_PROGRAM = 'NON-PUBLIC' THEN 
   IS_PUBLIC :=0;
   END IF;
   
   
   IF  P_IN_CORP = 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
   
   OPEN P_OUT_CUR_ASFD FOR 
   SELECT DISTINCT 
DIM.OBJECTIVE_NAME,
replace(FACT.ITEM_NUMBER,0)||
FACT.ITEM_PART||'-'||
FACT.TEST_NUMBER ||'-'||
DIM.OBJECTIVE_NAME AS ITEM,
FACT.POINTS_POSSIBLE,
FACT.TOTAL_NUMBER_STUDENTS,
FACT.NUMBER_0_PTS_OBTAINED,
FACT.PERCENT_0_PTS_OBTAINED,
FACT.NUMBER_1_PTS_OBTAINED,
FACT.PERCENT_1_PTS_OBTAINED,
FACT.NUMBER_2_PTS_OBTAINED,
FACT.PERCENT_2_PTS_OBTAINED,
FACT.NUMBER_3_PTS_OBTAINED,
FACT.PERCENT_3_PTS_OBTAINED,
FACT.NUMBER_4_PTS_OBTAINED,
FACT.PERCENT_4_PTS_OBTAINED,
FACT.NUMBER_5_PTS_OBTAINED,
FACT.PERCENT_5_PTS_OBTAINED,
FACT.NUMBER_6_PTS_OBTAINED,
FACT.PERCENT_6_PTS_OBTAINED

FROM ASFD_FACT FACT,
     OBJECTIVE_DIM DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        ORG1.ORG_NODEID ST_ID,
        GDIM.GRADEID GID,
        GDIM.GRADE_NAME G_NAME,
        DIM.SUBTESTID SID,
        DIM.SUBTEST_NAME SUB_NAME,
        OBJ.OBJECTIVEID OBJ_ID

FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP        VW,
     SUBTEST_DIM                 DIM,
     OBJECTIVE_DIM               OBJ,
     ASSESSMENT_DIM              ASS
     
     

 WHERE  PRO.PRODUCTID = CUST.PRODUCTID
   AND  CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
   AND  OPL.ORG_NODEID = ORG1.ORG_NODEID
   AND  ORG1.ORG_NODE_LEVEL = 1
   AND  ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
   AND  GLOOKUP.GRADEID = GDIM.GRADEID
   AND  CUST.PRODUCTID = ASS.PRODUCTID
   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
   AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
   AND  GDIM.GRADEID = VW.GRADEID
   AND  VW.SUBTESTID = DIM.SUBTESTID
   AND  VW.OBJECTIVEID = OBJ.OBJECTIVEID)A
   
   WHERE A.ST_ID = FACT.ORG_NODEID
     AND A.GID = FACT.GRADEID
     AND A.SID = FACT.SUBTESTID
     AND A.OBJ_ID = FACT.OBJECTIVEID
     AND FACT.OBJECTIVEID = DIM.OBJECTIVEID
     AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
     AND FACT.ISPUBLIC = IS_PUBLIC
     AND A.G_NAME = P_IN_GRADE
     AND A.SUB_NAME = P_IN_SUBJECT    -- 1.English-Language Arts, 2.Mathematics, 3.Science, 4.Social Studies, 
                                                 -- 5.Reading.
     ORDER BY 
     CASE WHEN DIM.OBJECTIVE_NAME = 'Writing Applications' THEN 1
          WHEN DIM.OBJECTIVE_NAME = 'Lang. Conventions' THEN 2
          WHEN DIM.OBJECTIVE_NAME = 'Literary Text?' THEN 3 
     END, ITEM;
   
   OPEN P_OUT_CUR_ASFD1 FOR 
   SELECT DISTINCT 
DIM.OBJECTIVE_NAME,
replace(FACT.ITEM_NUMBER,0)||
FACT.ITEM_PART||'-'||
FACT.TEST_NUMBER ||'-'||
DIM.OBJECTIVE_NAME AS ITEM,
FACT.TOTAL_NUMBER_STUDENTS,
FACT.NUMBER_COND_CODE_A,
FACT.PERCENT_COND_CODE_A,
FACT.NUMBER_COND_CODE_B,
FACT.PERCENT_COND_CODE_B,
FACT.NUMBER_COND_CODE_C,
FACT.PERCENT_COND_CODE_C,
FACT.NUMBER_COND_CODE_D,
FACT.PERCENT_COND_CODE_D,
FACT.NUMBER_COND_CODE_E,
FACT.PERCENT_COND_CODE_E,
FACT.NUMBER_INVALID_OMITTED,
FACT.PERCENT_INVALID_OMITTED


FROM ASFD_FACT FACT,
     OBJECTIVE_DIM DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        ORG1.ORG_NODEID ST_ID,
        GDIM.GRADEID GID,
        GDIM.GRADE_NAME G_NAME,
        DIM.SUBTESTID SID,
        DIM.SUBTEST_NAME SUB_NAME,
        OBJ.OBJECTIVEID OBJ_ID

FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP        VW,
     SUBTEST_DIM                 DIM,
     OBJECTIVE_DIM               OBJ,
     ASSESSMENT_DIM              ASS
     
     

 WHERE  PRO.PRODUCTID = CUST.PRODUCTID
   AND  CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
   AND  OPL.ORG_NODEID = ORG1.ORG_NODEID
   AND  ORG1.ORG_NODE_LEVEL = 1
   AND  ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
   AND  GLOOKUP.GRADEID = GDIM.GRADEID
   AND  CUST.PRODUCTID = ASS.PRODUCTID
   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
   AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
   AND  GDIM.GRADEID = VW.GRADEID
   AND  VW.SUBTESTID = DIM.SUBTESTID
   AND  VW.OBJECTIVEID = OBJ.OBJECTIVEID)A
   
   WHERE A.ST_ID = FACT.ORG_NODEID
     AND A.GID = FACT.GRADEID
     AND A.SID = FACT.SUBTESTID
     AND A.OBJ_ID = FACT.OBJECTIVEID
     AND FACT.OBJECTIVEID = DIM.OBJECTIVEID
     AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
     AND FACT.ISPUBLIC = IS_PUBLIC
     AND A.G_NAME = P_IN_GRADE
     AND A.SUB_NAME = P_IN_SUBJECT    -- 1.English-Language Arts, 2.Mathematics, 3.Science, 4.Social Studies, 
                                                 -- 5.Reading.
     ORDER BY 
     CASE WHEN DIM.OBJECTIVE_NAME = 'Writing Applications' THEN 1
          WHEN DIM.OBJECTIVE_NAME = 'Lang. Conventions' THEN 2
          WHEN DIM.OBJECTIVE_NAME = 'Literary Text?' THEN 3 
     END, ITEM;
   
   
   ELSIF  P_IN_CORP != 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
   OPEN P_OUT_CUR_ASFD FOR
   SELECT DISTINCT 
DIM.OBJECTIVE_NAME,
replace(FACT.ITEM_NUMBER,0)||
FACT.ITEM_PART||'-'||
FACT.TEST_NUMBER ||'-'||
DIM.OBJECTIVE_NAME AS ITEM,
FACT.POINTS_POSSIBLE,
FACT.TOTAL_NUMBER_STUDENTS,
FACT.NUMBER_0_PTS_OBTAINED,
FACT.PERCENT_0_PTS_OBTAINED,
FACT.NUMBER_1_PTS_OBTAINED,
FACT.PERCENT_1_PTS_OBTAINED,
FACT.NUMBER_2_PTS_OBTAINED,
FACT.PERCENT_2_PTS_OBTAINED,
FACT.NUMBER_3_PTS_OBTAINED,
FACT.PERCENT_3_PTS_OBTAINED,
FACT.NUMBER_4_PTS_OBTAINED,
FACT.PERCENT_4_PTS_OBTAINED,
FACT.NUMBER_5_PTS_OBTAINED,
FACT.PERCENT_5_PTS_OBTAINED,
FACT.NUMBER_6_PTS_OBTAINED,
FACT.PERCENT_6_PTS_OBTAINED


FROM ASFD_FACT FACT,
     OBJECTIVE_DIM DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        TEST.TP_TYPE,
        ORG1.ORG_NODEID DIS_ID,
        ORG1.ORG_NODE_NAME DIS_NAME,
        GDIM.GRADEID GID,
        GDIM.GRADE_NAME G_NAME,
        DIM.SUBTESTID SID,
        DIM.SUBTEST_NAME SUB_NAME,
        OBJ.OBJECTIVEID OBJ_ID

FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     TEST_PROGRAM                TEST,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP        VW,
     SUBTEST_DIM                 DIM,
     OBJECTIVE_DIM               OBJ,
     ASSESSMENT_DIM              ASS
     
     

 WHERE  PRO.PRODUCTID = CUST.PRODUCTID
   AND  CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
   AND  OPL.ORG_NODEID = ORG1.ORG_NODEID
   AND  ORG1.ORG_NODE_LEVEL = 2
   AND  ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
   AND  GLOOKUP.GRADEID = GDIM.GRADEID
   AND  CUST.PRODUCTID = ASS.PRODUCTID
   AND  ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
   AND  GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
   AND  GDIM.GRADEID = VW.GRADEID
   AND  ORG1.ORG_NODEID  = TEST_LINK.ORG_NODEID
   AND  TEST_LINK.TP_ID = TEST.TP_ID
   AND  VW.SUBTESTID = DIM.SUBTESTID
   AND  VW.OBJECTIVEID = OBJ.OBJECTIVEID)A
   
   WHERE A.DIS_ID = FACT.ORG_NODEID
     AND A.GID = FACT.GRADEID
     AND A.SID = FACT.SUBTESTID
     AND A.OBJ_ID = FACT.OBJECTIVEID
     AND FACT.OBJECTIVEID = DIM.OBJECTIVEID
     AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
     AND A.TP_TYPE = P_IN_TEST_PROGRAM
     AND A.DIS_NAME = P_IN_CORP
     AND A.G_NAME = P_IN_GRADE
     AND A.SUB_NAME = P_IN_SUBJECT    -- 1.English-Language Arts, 2.Mathematics, 3.Science, 4.Social Studies, 
                                                 -- 5.Reading.
     ORDER BY 
     CASE WHEN DIM.OBJECTIVE_NAME = 'Writing Applications' THEN 1
          WHEN DIM.OBJECTIVE_NAME = 'Lang. Conventions' THEN 2
          WHEN DIM.OBJECTIVE_NAME = 'Literary Text?' THEN 3 
     END, ITEM;
   OPEN P_OUT_CUR_ASFD1 FOR
   SELECT DISTINCT 
DIM.OBJECTIVE_NAME,
replace(FACT.ITEM_NUMBER,0)||
FACT.ITEM_PART||'-'||
FACT.TEST_NUMBER ||'-'||
DIM.OBJECTIVE_NAME AS ITEM,
FACT.TOTAL_NUMBER_STUDENTS,
FACT.NUMBER_COND_CODE_A,
FACT.PERCENT_COND_CODE_A,
FACT.NUMBER_COND_CODE_B,
FACT.PERCENT_COND_CODE_B,
FACT.NUMBER_COND_CODE_C,
FACT.PERCENT_COND_CODE_C,
FACT.NUMBER_COND_CODE_D,
FACT.PERCENT_COND_CODE_D,
FACT.NUMBER_COND_CODE_E,
FACT.PERCENT_COND_CODE_E,
FACT.NUMBER_INVALID_OMITTED,
FACT.PERCENT_INVALID_OMITTED


FROM ASFD_FACT FACT,
     OBJECTIVE_DIM DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        TEST.TP_TYPE,
        ORG1.ORG_NODEID DIS_ID,
        ORG1.ORG_NODE_NAME DIS_NAME,
        GDIM.GRADEID GID,
        GDIM.GRADE_NAME G_NAME,
        DIM.SUBTESTID SID,
        DIM.SUBTEST_NAME SUB_NAME,
        OBJ.OBJECTIVEID OBJ_ID

FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     TEST_PROGRAM                TEST,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP        VW,
     SUBTEST_DIM                 DIM,
     OBJECTIVE_DIM               OBJ,
     ASSESSMENT_DIM              ASS
     
     

 WHERE  PRO.PRODUCTID = CUST.PRODUCTID
   AND  CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
   AND  OPL.ORG_NODEID = ORG1.ORG_NODEID
   AND  ORG1.ORG_NODE_LEVEL = 2
   AND  ORG1.ORG_NODEID = GLOOKUP.ORG_NODEID
   AND  GLOOKUP.GRADEID = GDIM.GRADEID
   AND  CUST.PRODUCTID = ASS.PRODUCTID
   AND  ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
   AND  GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
   AND  GDIM.GRADEID = VW.GRADEID
   AND  ORG1.ORG_NODEID  = TEST_LINK.ORG_NODEID
   AND  TEST_LINK.TP_ID = TEST.TP_ID
   AND  VW.SUBTESTID = DIM.SUBTESTID
   AND  VW.OBJECTIVEID = OBJ.OBJECTIVEID)A
   
   WHERE A.DIS_ID = FACT.ORG_NODEID
     AND A.GID = FACT.GRADEID
     AND A.SID = FACT.SUBTESTID
     AND A.OBJ_ID = FACT.OBJECTIVEID
     AND FACT.OBJECTIVEID = DIM.OBJECTIVEID
     AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
     AND A.TP_TYPE = P_IN_TEST_PROGRAM
     AND A.DIS_NAME = P_IN_CORP
     AND A.G_NAME = P_IN_GRADE
     AND A.SUB_NAME = P_IN_SUBJECT    -- 1.English-Language Arts, 2.Mathematics, 3.Science, 4.Social Studies, 
                                                 -- 5.Reading.
     ORDER BY 
     CASE WHEN DIM.OBJECTIVE_NAME = 'Writing Applications' THEN 1
          WHEN DIM.OBJECTIVE_NAME = 'Lang. Conventions' THEN 2
          WHEN DIM.OBJECTIVE_NAME = 'Literary Text?' THEN 3 
     END, ITEM;
   
   
   
   
   ELSIF  P_IN_CORP != 'ALL' AND P_IN_SCHOOL != 'ALL' THEN
   
   OPEN P_OUT_CUR_ASFD FOR
   SELECT DISTINCT 
DIM.OBJECTIVE_NAME,
replace(FACT.ITEM_NUMBER,0)||
FACT.ITEM_PART||'-'||
FACT.TEST_NUMBER ||'-'||
DIM.OBJECTIVE_NAME AS ITEM,
FACT.POINTS_POSSIBLE,
FACT.TOTAL_NUMBER_STUDENTS,
FACT.NUMBER_0_PTS_OBTAINED,
FACT.PERCENT_0_PTS_OBTAINED,
FACT.NUMBER_1_PTS_OBTAINED,
FACT.PERCENT_1_PTS_OBTAINED,
FACT.NUMBER_2_PTS_OBTAINED,
FACT.PERCENT_2_PTS_OBTAINED,
FACT.NUMBER_3_PTS_OBTAINED,
FACT.PERCENT_3_PTS_OBTAINED,
FACT.NUMBER_4_PTS_OBTAINED,
FACT.PERCENT_4_PTS_OBTAINED,
FACT.NUMBER_5_PTS_OBTAINED,
FACT.PERCENT_5_PTS_OBTAINED,
FACT.NUMBER_6_PTS_OBTAINED,
FACT.PERCENT_6_PTS_OBTAINED


FROM ASFD_FACT FACT,
     OBJECTIVE_DIM DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        TEST.TP_TYPE,
        ORG1.ORG_NODEID DIS_ID,
        ORG1.ORG_NODE_NAME DIS_NAME,
        ORG2.ORG_NODEID SCH_ID,
        ORG2.ORG_NODE_NAME SCH_NAME,
        GDIM.GRADEID GID,
        GDIM.GRADE_NAME G_NAME,
        DIM.SUBTESTID SID,
        DIM.SUBTEST_NAME SUB_NAME,
        OBJ.OBJECTIVEID OBJ_ID

FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     ORG_NODE_DIM                ORG2,
     TEST_PROGRAM                TEST,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP  VW,
     SUBTEST_DIM                 DIM,
     OBJECTIVE_DIM               OBJ,
     ASSESSMENT_DIM              ASS
     
     

 WHERE  PRO.PRODUCTID = CUST.PRODUCTID
   AND  CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
   AND  OPL.ORG_NODEID = ORG1.ORG_NODEID
   AND  ORG1.ORG_NODE_LEVEL = 2
   AND  ORG1.ORG_NODEID = ORG2.PARENT_ORG_NODEID
   AND  ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID
   AND  GLOOKUP.GRADEID = GDIM.GRADEID
   AND  ORG2.ORG_NODEID  = TEST_LINK.ORG_NODEID
   AND  TEST_LINK.TP_ID = TEST.TP_ID
   AND  CUST.PRODUCTID = ASS.PRODUCTID
   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
   AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
   AND GDIM.GRADEID  = VW.GRADEID
   AND  VW.SUBTESTID = DIM.SUBTESTID
   AND  VW.OBJECTIVEID = OBJ.OBJECTIVEID)A
   
   WHERE A.SCH_ID = FACT.ORG_NODEID
     AND A.GID = FACT.GRADEID
     AND A.SID = FACT.SUBTESTID
     AND A.OBJ_ID = FACT.OBJECTIVEID
     AND FACT.OBJECTIVEID = DIM.OBJECTIVEID
     AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
     AND A.TP_TYPE = P_IN_TEST_PROGRAM
     AND A.DIS_NAME = P_IN_CORP
     AND A.SCH_NAME = P_IN_SCHOOL
     AND A.G_NAME = P_IN_GRADE
     AND A.SUB_NAME = P_IN_SUBJECT    -- 1.English-Language Arts, 2.Mathematics, 3.Science, 4.Social Studies, 
                                                 -- 5.Reading.
     ORDER BY 
     CASE WHEN DIM.OBJECTIVE_NAME = 'Writing Applications' THEN 1
          WHEN DIM.OBJECTIVE_NAME = 'Lang. Conventions' THEN 2
          WHEN DIM.OBJECTIVE_NAME = 'Literary Text?' THEN 3 
     END, ITEM;
     
     OPEN P_OUT_CUR_ASFD1 FOR
     SELECT DISTINCT 
DIM.OBJECTIVE_NAME,
replace(FACT.ITEM_NUMBER,0)||
FACT.ITEM_PART||'-'||
FACT.TEST_NUMBER ||'-'||
DIM.OBJECTIVE_NAME AS ITEM,
FACT.TOTAL_NUMBER_STUDENTS,
FACT.NUMBER_COND_CODE_A,
FACT.PERCENT_COND_CODE_A,
FACT.NUMBER_COND_CODE_B,
FACT.PERCENT_COND_CODE_B,
FACT.NUMBER_COND_CODE_C,
FACT.PERCENT_COND_CODE_C,
FACT.NUMBER_COND_CODE_D,
FACT.PERCENT_COND_CODE_D,
FACT.NUMBER_COND_CODE_E,
FACT.PERCENT_COND_CODE_E,
FACT.NUMBER_INVALID_OMITTED,
FACT.PERCENT_INVALID_OMITTED



FROM ASFD_FACT FACT,
     OBJECTIVE_DIM DIM,
(SELECT DISTINCT PRO.PRODUCT_NAME,
        CUST.CUST_PROD_ID,
        TEST.TP_TYPE,
        ORG1.ORG_NODEID DIS_ID,
        ORG1.ORG_NODE_NAME DIS_NAME,
        ORG2.ORG_NODEID SCH_ID,
        ORG2.ORG_NODE_NAME SCH_NAME,
        GDIM.GRADEID GID,
        GDIM.GRADE_NAME G_NAME,
        DIM.SUBTESTID SID,
        DIM.SUBTEST_NAME SUB_NAME,
        OBJ.OBJECTIVEID OBJ_ID

FROM PRODUCT                     PRO,
     CUST_PRODUCT_LINK           CUST,
     ORG_PRODUCT_LINK            OPL,
     ORG_NODE_DIM                ORG1,
     ORG_NODE_DIM                ORG2,
     TEST_PROGRAM                TEST,
     ORG_TEST_PROGRAM_LINK       TEST_LINK,
     GRADE_DIM                   GDIM,
     GRADE_SELECTION_LOOKUP      GLOOKUP,
     VW_SUBTEST_GRADE_OBJECTIVE_MAP  VW,
     SUBTEST_DIM                 DIM,
     OBJECTIVE_DIM               OBJ,
     ASSESSMENT_DIM              ASS
     
     

 WHERE  PRO.PRODUCTID = CUST.PRODUCTID
   AND  CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
   AND  OPL.ORG_NODEID = ORG1.ORG_NODEID
   AND  ORG1.ORG_NODE_LEVEL = 2
   AND  ORG1.ORG_NODEID = ORG2.PARENT_ORG_NODEID
   AND  ORG2.ORG_NODEID = GLOOKUP.ORG_NODEID
   AND  GLOOKUP.GRADEID = GDIM.GRADEID
   AND  ORG2.ORG_NODEID  = TEST_LINK.ORG_NODEID
   AND  TEST_LINK.TP_ID = TEST.TP_ID
   AND  CUST.PRODUCTID = ASS.PRODUCTID
   AND ASS.ASSESSMENTID = GLOOKUP.ASSESSMENTID
   AND GLOOKUP.ASSESSMENTID = VW.ASSESSMENTID
   AND GDIM.GRADEID  = VW.GRADEID
   AND  VW.SUBTESTID = DIM.SUBTESTID
   AND  VW.OBJECTIVEID = OBJ.OBJECTIVEID)A
   
   WHERE A.SCH_ID = FACT.ORG_NODEID
     AND A.GID = FACT.GRADEID
     AND A.SID = FACT.SUBTESTID
     AND A.OBJ_ID = FACT.OBJECTIVEID
     AND FACT.OBJECTIVEID = DIM.OBJECTIVEID
     AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
     AND A.TP_TYPE = P_IN_TEST_PROGRAM
     AND A.DIS_NAME = P_IN_CORP
     AND A.SCH_NAME = P_IN_SCHOOL
     AND A.G_NAME = P_IN_GRADE
     AND A.SUB_NAME = P_IN_SUBJECT    -- 1.English-Language Arts, 2.Mathematics, 3.Science, 4.Social Studies, 
                                                 -- 5.Reading.
     ORDER BY 
     CASE WHEN DIM.OBJECTIVE_NAME = 'Writing Applications' THEN 1
          WHEN DIM.OBJECTIVE_NAME = 'Lang. Conventions' THEN 2
          WHEN DIM.OBJECTIVE_NAME = 'Literary Text?' THEN 3 
     END, ITEM;
     
     
     END IF;
   
   END APP_SKILLS_FREQ_DIST_REPORT;


--PROFICIENCY PERFORMANCE SUMMARY

PROCEDURE PROF_PERFOR_SUMM_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2, 
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_OUT_CUR_PROF_PERF_SUMM OUT GET_REFCURSOR) IS
   
  IS_PUBLIC NUMBER :=0;                                   
  BEGIN
  
  IF P_IN_CORP != 'ALL' AND P_IN_SCHOOL != 'ALL' THEN
  OPEN P_OUT_CUR_PROF_PERF_SUMM FOR 
  SELECT DISTINCT  A.MPP_ELA_PASSP, A.MPP_ELA_PASS, A.MPP_ELA_DNP, A.MPP_ELA_UNDET, A.MPP_MATH_TOTALS,
                 A.MP_ELA_PASSP,  A.MP_ELA_PASS,  A.MP_ELA_DNP,  A.MP_ELA_UNDET,  A.MP_MATH_TOTALS,
                 A.MDNP_ELA_PASSP, A.MDNP_ELA_PASS, A.MDNP_ELA_DNP, A.MDNP_ELA_UNDET, A.MDNP_MATH_TOTALS,
                 A.MUNDET_ELA_PASSP, A.MUNDET_ELA_PASS, A.MUNDET_ELA_DNP, A.MUNDET_ELA_UNDET, A.MUNDET_MATH_TOTALS,
                 A.ELATOT_ELA_PASSP, A.ELATOT_ELA_PASS, A.ELATOT_ELA_DNP, A.ELATOT_ELA_UNDET, A.TOT_STUDENTS_ELA_MATH,
                 A.ELA_LOW_HIGH,
                 A.MATH_LOW_HIGH,
                 A.SCI_PASSP, A.SCI_PASS, A.SCI_DNP, A.SCI_UNDET, A.TOT_STUDENTS_SCI
FROM
(SELECT   
NUM_PASSP_ELA_PASSP_MATH || ',' || PER_PASSP_ELA_PASSP_MATH AS MPP_ELA_PASSP,
NUM_PASS_ELA_PASSP_MATH || ',' || PERC_PASS_ELA_PASSP_MATH AS MPP_ELA_PASS,
NUM_DNP_ELA_PASSP_MATH || ',' || PERC_DNP_ELA_PASSP_MATH AS MPP_ELA_DNP,
NUM_UNDTRM_ELA_PASSP_MATH || ',' || PERC_UNDTRM_ELA_PASSP_MATH AS MPP_ELA_UNDET,
TOT_NUM_PASSP_MATH || ',' || TOT_PERC_PASSP_MATH AS MPP_MATH_TOTALS,
NUM_PASSP_ELA_PASS_MATH || ',' || PERC_PASSP_ELA_PASS_MATH AS MP_ELA_PASSP,
NUM_PASS_ELA_PASS_MATH || ',' || PERC_PASS_ELA_PASS_MATH AS MP_ELA_PASS,
NUM_DNP_ELA_PASS_MATH || ',' || PERC_DNP_ELA_PASS_MATH AS MP_ELA_DNP,
NUM_UNDTRM_ELA_PASS_MATH || ',' || PERC_UNDTRM_ELA_PASS_MATH AS MP_ELA_UNDET,
TOT_NUM_PASS_MATH || ',' || TOT_PERC_PASS_MATH AS MP_MATH_TOTALS,
NUM_PASSP_ELA_DNP_MATH || ',' || PERC_PASSP_ELA_DNP_MATH AS MDNP_ELA_PASSP,
NUM_PASS_ELA_DNP_MATH || ',' || PERC_PASS_ELA_DNP_MATH AS MDNP_ELA_PASS,
NUM_DNP_ELA_DNP_MATH || ',' || PERC_DNP_ELA_DNP_MATH AS MDNP_ELA_DNP,
NUM_UNDTRM_ELA_DNP_MATH || ',' || PERC_UNDTRM_ELA_DNP_MATH AS MDNP_ELA_UNDET,
TOT_NUM_DNP_MATH || ',' || TOT_PERC_DNP_MATH AS MDNP_MATH_TOTALS,
NUM_PASSP_ELA_UNDTRM_MATH || ',' || PERC_PASSP_ELA_UNDTRM_MATH AS MUNDET_ELA_PASSP,
NUM_PASS_ELA_UNDTRM_MATH || ',' || PERC_PASS_ELA_UNDTRM_MATH AS MUNDET_ELA_PASS,
NUM_DNP_ELA_UNDTRM_MATH || ',' || PERC_DNP_ELA_UNDTRM_MATH AS MUNDET_ELA_DNP,
NUM_UNDTRM_ELA_UNDTRM_MATH || ',' || PERC_UNDTRM_ELA_UNDTRM_MATH AS MUNDET_ELA_UNDET,
TOT_NUM_UNDTRM_MATH || ',' || TOT_PERC_UNDTRM_MATH AS MUNDET_MATH_TOTALS,
TOT_NUM_PASSP_ELA || ',' || TOT_PERC_PASSP_ELA AS ELATOT_ELA_PASSP,
TOT_NUM_PASS_ELA || ',' || TOT_PERC_PASS_ELA AS ELATOT_ELA_PASS,
TOT_NUM_DNP_ELA || ',' || TOT_PERC_DNP_ELA AS ELATOT_ELA_DNP,
TOT_NUM_UNDTRM_ELA || ',' || TOT_PERC_UNDTRM_ELA AS ELATOT_ELA_UNDET,
TOT_STUD_COUNT_ELA_MATH AS TOT_STUDENTS_ELA_MATH,
LOW_OBTAINED_ELA_SCALE_SCORE || '-' || HIGH_OBTAINED_ELA_SCALE_SCORE AS ELA_LOW_HIGH,
LOW_OBTAINED_MATH_SCALE_SCORE || '-' || HIGH_OBTAINED_MATH_SCALE_SCORE AS MATH_LOW_HIGH,
NUM_PASSP_SCIE || ',' || PERC_PASSP_SCIE AS SCI_PASSP,
NUM_PASS_SCIE || ',' || PERC_PASS_SCIE AS SCI_PASS,
NUM_DNP_SCIE || ',' || PERC_DNP_SCIE AS SCI_DNP,
NUM_UNDTRM_SCIE || ',' || PERC_UNDTRM_SCIE AS SCI_UNDET,
TOT_STUD_COUNT_SCIE AS TOT_STUDENTS_SCI,
CUST_PROD_ID,
ORG_NODEID,
GRADEID
FROM MEDIA_FACT)A,
(SELECT DISTINCT PRO.PRODUCT_NAME TEST_ADMIN,
       CUST.CUST_PROD_ID,
       TP.TP_TYPE TEST_PRGM,
       DIM1.ORG_NODEID    DIS_ID,
       DIM1.ORG_NODE_NAME DIS_NAME,
       DIM2.ORG_NODEID    SCH_ID,
       DIM2.ORG_NODE_NAME SCH_NAME,
       GDIM.GRADEID        GID,
       GDIM.GRADE_NAME     GR_NAME,
       SUB.SUBTEST_NAME
      

FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
     PRODUCT                        PRO,
     CUST_PRODUCT_LINK              CUST,
     ORG_PRODUCT_LINK               OPL,
     ORG_TEST_PROGRAM_LINK          OLINK,
     TEST_PROGRAM                   TP,
     ORG_NODE_DIM                   DIM1,
     ORG_NODE_DIM                   DIM2,
     GRADE_DIM                      GDIM,
     GRADE_SELECTION_LOOKUP         GLOOKUP,
     ASSESSMENT_DIM                 ASS_DIM,
     USERS                  U,
     ORG_USERS              OU
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = DIM1.ORG_NODEID
  AND DIM1.ORG_NODE_LEVEL = 2           --COMPARE WITH DISTRICT LAVEL ORG_NODEID
  AND DIM1.ORG_NODEID = DIM2.PARENT_ORG_NODEID
  AND DIM2.ORG_NODEID = OLINK.ORG_NODEID           --- MAP SCHOOL LEVEL ORG_NODEID WITH TEST_PROGRAM
  AND OLINK.TP_ID = TP.TP_ID
  AND DIM2.ORG_NODEID = GLOOKUP.ORG_NODEID      --- MAP SCHOOL LEVEL ORG_NODEID WITH GRADE_SELECTION_LOOKUP
  AND GLOOKUP.ASSESSMENTID = SUB.ASSESSMENTID
  AND GLOOKUP.GRADEID = SUB.GRADEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND CUST.PRODUCTID  = ASS_DIM.PRODUCTID
  AND ASS_DIM.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND U.USERNAME = 'ctbadmin'                        -- LOGIN USER NAME
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = DIM1.PARENT_ORG_NODEID)B
  
  WHERE A.CUST_PROD_ID = B.CUST_PROD_ID
    AND A.ORG_NODEID   = B.SCH_ID
    AND A.GRADEID = B.GID
    AND B.TEST_ADMIN = P_IN_TEST_ADMINISTRATION  --TEST ADMINISTRATION
    AND B.TEST_PRGM  = P_IN_TEST_PROGRAM             -- TEST PROGRAM.CHANGE IT ACCORDING TO REQUIREMENT
    AND B.DIS_NAME   = P_IN_CORP       --COMMENT THIS LINE TO SEE THE REPORT OF ALL DISTRICT
    AND B.SCH_NAME   = P_IN_SCHOOL        --COMMENT THIS LINE TO SEE THE REPORT OF ALL SCHOOL UNDER A PARTICULAR DISTRICT OR ALL DISTRICT
    AND B.GR_NAME      = P_IN_GRADE;          --COMMENT THIS LINE TO SEE THE REPORT OF ALL GRADE UNDER A PARTICULAR SCHOOL OR ALL SCHOOL
  
  
  ELSIF P_IN_CORP != 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
  
  
  OPEN P_OUT_CUR_PROF_PERF_SUMM FOR
  
  SELECT DISTINCT  A.MPP_ELA_PASSP, A.MPP_ELA_PASS, A.MPP_ELA_DNP, A.MPP_ELA_UNDET, A.MPP_MATH_TOTALS,
                 A.MP_ELA_PASSP,  A.MP_ELA_PASS,  A.MP_ELA_DNP,  A.MP_ELA_UNDET,  A.MP_MATH_TOTALS,
                 A.MDNP_ELA_PASSP, A.MDNP_ELA_PASS, A.MDNP_ELA_DNP, A.MDNP_ELA_UNDET, A.MDNP_MATH_TOTALS,
                 A.MUNDET_ELA_PASSP, A.MUNDET_ELA_PASS, A.MUNDET_ELA_DNP, A.MUNDET_ELA_UNDET, A.MUNDET_MATH_TOTALS,
                 A.ELATOT_ELA_PASSP, A.ELATOT_ELA_PASS, A.ELATOT_ELA_DNP, A.ELATOT_ELA_UNDET, A.TOT_STUDENTS_ELA_MATH,
                 A.ELA_LOW_HIGH,
                 A.MATH_LOW_HIGH,
                 A.SCI_PASSP, A.SCI_PASS, A.SCI_DNP, A.SCI_UNDET, A.TOT_STUDENTS_SCI
FROM
(SELECT   
NUM_PASSP_ELA_PASSP_MATH || ',' || PER_PASSP_ELA_PASSP_MATH AS MPP_ELA_PASSP,
NUM_PASS_ELA_PASSP_MATH || ',' || PERC_PASS_ELA_PASSP_MATH AS MPP_ELA_PASS,
NUM_DNP_ELA_PASSP_MATH || ',' || PERC_DNP_ELA_PASSP_MATH AS MPP_ELA_DNP,
NUM_UNDTRM_ELA_PASSP_MATH || ',' || PERC_UNDTRM_ELA_PASSP_MATH AS MPP_ELA_UNDET,
TOT_NUM_PASSP_MATH || ',' || TOT_PERC_PASSP_MATH AS MPP_MATH_TOTALS,
NUM_PASSP_ELA_PASS_MATH || ',' || PERC_PASSP_ELA_PASS_MATH AS MP_ELA_PASSP,
NUM_PASS_ELA_PASS_MATH || ',' || PERC_PASS_ELA_PASS_MATH AS MP_ELA_PASS,
NUM_DNP_ELA_PASS_MATH || ',' || PERC_DNP_ELA_PASS_MATH AS MP_ELA_DNP,
NUM_UNDTRM_ELA_PASS_MATH || ',' || PERC_UNDTRM_ELA_PASS_MATH AS MP_ELA_UNDET,
TOT_NUM_PASS_MATH || ',' || TOT_PERC_PASS_MATH AS MP_MATH_TOTALS,
NUM_PASSP_ELA_DNP_MATH || ',' || PERC_PASSP_ELA_DNP_MATH AS MDNP_ELA_PASSP,
NUM_PASS_ELA_DNP_MATH || ',' || PERC_PASS_ELA_DNP_MATH AS MDNP_ELA_PASS,
NUM_DNP_ELA_DNP_MATH || ',' || PERC_DNP_ELA_DNP_MATH AS MDNP_ELA_DNP,
NUM_UNDTRM_ELA_DNP_MATH || ',' || PERC_UNDTRM_ELA_DNP_MATH AS MDNP_ELA_UNDET,
TOT_NUM_DNP_MATH || ',' || TOT_PERC_DNP_MATH AS MDNP_MATH_TOTALS,
NUM_PASSP_ELA_UNDTRM_MATH || ',' || PERC_PASSP_ELA_UNDTRM_MATH AS MUNDET_ELA_PASSP,
NUM_PASS_ELA_UNDTRM_MATH || ',' || PERC_PASS_ELA_UNDTRM_MATH AS MUNDET_ELA_PASS,
NUM_DNP_ELA_UNDTRM_MATH || ',' || PERC_DNP_ELA_UNDTRM_MATH AS MUNDET_ELA_DNP,
NUM_UNDTRM_ELA_UNDTRM_MATH || ',' || PERC_UNDTRM_ELA_UNDTRM_MATH AS MUNDET_ELA_UNDET,
TOT_NUM_UNDTRM_MATH || ',' || TOT_PERC_UNDTRM_MATH AS MUNDET_MATH_TOTALS,
TOT_NUM_PASSP_ELA || ',' || TOT_PERC_PASSP_ELA AS ELATOT_ELA_PASSP,
TOT_NUM_PASS_ELA || ',' || TOT_PERC_PASS_ELA AS ELATOT_ELA_PASS,
TOT_NUM_DNP_ELA || ',' || TOT_PERC_DNP_ELA AS ELATOT_ELA_DNP,
TOT_NUM_UNDTRM_ELA || ',' || TOT_PERC_UNDTRM_ELA AS ELATOT_ELA_UNDET,
TOT_STUD_COUNT_ELA_MATH AS TOT_STUDENTS_ELA_MATH,
LOW_OBTAINED_ELA_SCALE_SCORE || '-' || HIGH_OBTAINED_ELA_SCALE_SCORE AS ELA_LOW_HIGH,
LOW_OBTAINED_MATH_SCALE_SCORE || '-' || HIGH_OBTAINED_MATH_SCALE_SCORE AS MATH_LOW_HIGH,
NUM_PASSP_SCIE || ',' || PERC_PASSP_SCIE AS SCI_PASSP,
NUM_PASS_SCIE || ',' || PERC_PASS_SCIE AS SCI_PASS,
NUM_DNP_SCIE || ',' || PERC_DNP_SCIE AS SCI_DNP,
NUM_UNDTRM_SCIE || ',' || PERC_UNDTRM_SCIE AS SCI_UNDET,
TOT_STUD_COUNT_SCIE AS TOT_STUDENTS_SCI,
CUST_PROD_ID,
ORG_NODEID,
GRADEID
FROM MEDIA_FACT)A,
(SELECT DISTINCT PRO.PRODUCT_NAME TEST_ADMIN,
       CUST.CUST_PROD_ID,
       TP.TP_TYPE TEST_PRGM,
       DIM1.ORG_NODEID    DIS_ID,
       DIM1.ORG_NODE_NAME DIS_NAME,
       DIM2.ORG_NODEID    SCH_ID,
       DIM2.ORG_NODE_NAME SCH_NAME,
       GDIM.GRADEID        GID,
       GDIM.GRADE_NAME     GR_NAME,
       SUB.SUBTEST_NAME
      

FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
     PRODUCT                        PRO,
     CUST_PRODUCT_LINK              CUST,
     ORG_PRODUCT_LINK               OPL,
     ORG_TEST_PROGRAM_LINK          OLINK,
     TEST_PROGRAM                   TP,
     ORG_NODE_DIM                   DIM1,
     ORG_NODE_DIM                   DIM2,
     GRADE_DIM                      GDIM,
     GRADE_SELECTION_LOOKUP         GLOOKUP,
     ASSESSMENT_DIM                 ASS_DIM,
     USERS                  U,
     ORG_USERS              OU
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = DIM1.ORG_NODEID
  AND DIM1.ORG_NODE_LEVEL = 2           --COMPARE WITH DISTRICT LAVEL ORG_NODEID
  AND DIM1.ORG_NODEID = DIM2.PARENT_ORG_NODEID
  AND DIM2.ORG_NODEID = OLINK.ORG_NODEID           --- MAP SCHOOL LEVEL ORG_NODEID WITH TEST_PROGRAM
  AND OLINK.TP_ID = TP.TP_ID
  AND DIM2.ORG_NODEID = GLOOKUP.ORG_NODEID      --- MAP SCHOOL LEVEL ORG_NODEID WITH GRADE_SELECTION_LOOKUP
  AND GLOOKUP.ASSESSMENTID = SUB.ASSESSMENTID
  AND GLOOKUP.GRADEID = SUB.GRADEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND CUST.PRODUCTID  = ASS_DIM.PRODUCTID
  AND ASS_DIM.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND U.USERNAME = 'ctbadmin'                        -- LOGIN USER NAME
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = DIM1.PARENT_ORG_NODEID)B
  
  WHERE A.CUST_PROD_ID = B.CUST_PROD_ID
    AND A.ORG_NODEID   = B.DIS_ID
    AND A.GRADEID = B.GID
    AND B.TEST_ADMIN = P_IN_TEST_ADMINISTRATION  --TEST ADMINISTRATION
    AND B.TEST_PRGM  = P_IN_TEST_PROGRAM              -- TEST PROGRAM.CHANGE IT ACCORDING TO REQUIREMENT
    AND B.DIS_NAME   = P_IN_CORP       --COMMENT THIS LINE TO SEE THE REPORT OF ALL DISTRICT
    AND B.GR_NAME      = P_IN_GRADE;         --COMMENT THIS LINE TO SEE THE REPORT OF ALL GRADE UNDER A PARTICULAR SCHOOL OR ALL SCHOOL
  
  
  
  ELSIF P_IN_CORP = 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
  
  
  IF P_IN_TEST_PROGRAM = 'PUBLIC' THEN 
   IS_PUBLIC :=1;
   ELSIF P_IN_TEST_PROGRAM = 'NON-PUBLIC' THEN 
   IS_PUBLIC :=0;
   END IF;
  
  
  OPEN P_OUT_CUR_PROF_PERF_SUMM FOR
  
  SELECT DISTINCT  A.MPP_ELA_PASSP, A.MPP_ELA_PASS, A.MPP_ELA_DNP, A.MPP_ELA_UNDET, A.MPP_MATH_TOTALS,
                 A.MP_ELA_PASSP,  A.MP_ELA_PASS,  A.MP_ELA_DNP,  A.MP_ELA_UNDET,  A.MP_MATH_TOTALS,
                 A.MDNP_ELA_PASSP, A.MDNP_ELA_PASS, A.MDNP_ELA_DNP, A.MDNP_ELA_UNDET, A.MDNP_MATH_TOTALS,
                 A.MUNDET_ELA_PASSP, A.MUNDET_ELA_PASS, A.MUNDET_ELA_DNP, A.MUNDET_ELA_UNDET, A.MUNDET_MATH_TOTALS,
                 A.ELATOT_ELA_PASSP, A.ELATOT_ELA_PASS, A.ELATOT_ELA_DNP, A.ELATOT_ELA_UNDET, A.TOT_STUDENTS_ELA_MATH,
                 A.ELA_LOW_HIGH,
                 A.MATH_LOW_HIGH,
                 A.SCI_PASSP, A.SCI_PASS, A.SCI_DNP, A.SCI_UNDET, A.TOT_STUDENTS_SCI
FROM
(SELECT   
NUM_PASSP_ELA_PASSP_MATH || ',' || PER_PASSP_ELA_PASSP_MATH AS MPP_ELA_PASSP,
NUM_PASS_ELA_PASSP_MATH || ',' || PERC_PASS_ELA_PASSP_MATH AS MPP_ELA_PASS,
NUM_DNP_ELA_PASSP_MATH || ',' || PERC_DNP_ELA_PASSP_MATH AS MPP_ELA_DNP,
NUM_UNDTRM_ELA_PASSP_MATH || ',' || PERC_UNDTRM_ELA_PASSP_MATH AS MPP_ELA_UNDET,
TOT_NUM_PASSP_MATH || ',' || TOT_PERC_PASSP_MATH AS MPP_MATH_TOTALS,
NUM_PASSP_ELA_PASS_MATH || ',' || PERC_PASSP_ELA_PASS_MATH AS MP_ELA_PASSP,
NUM_PASS_ELA_PASS_MATH || ',' || PERC_PASS_ELA_PASS_MATH AS MP_ELA_PASS,
NUM_DNP_ELA_PASS_MATH || ',' || PERC_DNP_ELA_PASS_MATH AS MP_ELA_DNP,
NUM_UNDTRM_ELA_PASS_MATH || ',' || PERC_UNDTRM_ELA_PASS_MATH AS MP_ELA_UNDET,
TOT_NUM_PASS_MATH || ',' || TOT_PERC_PASS_MATH AS MP_MATH_TOTALS,
NUM_PASSP_ELA_DNP_MATH || ',' || PERC_PASSP_ELA_DNP_MATH AS MDNP_ELA_PASSP,
NUM_PASS_ELA_DNP_MATH || ',' || PERC_PASS_ELA_DNP_MATH AS MDNP_ELA_PASS,
NUM_DNP_ELA_DNP_MATH || ',' || PERC_DNP_ELA_DNP_MATH AS MDNP_ELA_DNP,
NUM_UNDTRM_ELA_DNP_MATH || ',' || PERC_UNDTRM_ELA_DNP_MATH AS MDNP_ELA_UNDET,
TOT_NUM_DNP_MATH || ',' || TOT_PERC_DNP_MATH AS MDNP_MATH_TOTALS,
NUM_PASSP_ELA_UNDTRM_MATH || ',' || PERC_PASSP_ELA_UNDTRM_MATH AS MUNDET_ELA_PASSP,
NUM_PASS_ELA_UNDTRM_MATH || ',' || PERC_PASS_ELA_UNDTRM_MATH AS MUNDET_ELA_PASS,
NUM_DNP_ELA_UNDTRM_MATH || ',' || PERC_DNP_ELA_UNDTRM_MATH AS MUNDET_ELA_DNP,
NUM_UNDTRM_ELA_UNDTRM_MATH || ',' || PERC_UNDTRM_ELA_UNDTRM_MATH AS MUNDET_ELA_UNDET,
TOT_NUM_UNDTRM_MATH || ',' || TOT_PERC_UNDTRM_MATH AS MUNDET_MATH_TOTALS,
TOT_NUM_PASSP_ELA || ',' || TOT_PERC_PASSP_ELA AS ELATOT_ELA_PASSP,
TOT_NUM_PASS_ELA || ',' || TOT_PERC_PASS_ELA AS ELATOT_ELA_PASS,
TOT_NUM_DNP_ELA || ',' || TOT_PERC_DNP_ELA AS ELATOT_ELA_DNP,
TOT_NUM_UNDTRM_ELA || ',' || TOT_PERC_UNDTRM_ELA AS ELATOT_ELA_UNDET,
TOT_STUD_COUNT_ELA_MATH AS TOT_STUDENTS_ELA_MATH,
LOW_OBTAINED_ELA_SCALE_SCORE || '-' || HIGH_OBTAINED_ELA_SCALE_SCORE AS ELA_LOW_HIGH,
LOW_OBTAINED_MATH_SCALE_SCORE || '-' || HIGH_OBTAINED_MATH_SCALE_SCORE AS MATH_LOW_HIGH,
NUM_PASSP_SCIE || ',' || PERC_PASSP_SCIE AS SCI_PASSP,
NUM_PASS_SCIE || ',' || PERC_PASS_SCIE AS SCI_PASS,
NUM_DNP_SCIE || ',' || PERC_DNP_SCIE AS SCI_DNP,
NUM_UNDTRM_SCIE || ',' || PERC_UNDTRM_SCIE AS SCI_UNDET,
TOT_STUD_COUNT_SCIE AS TOT_STUDENTS_SCI,
CUST_PROD_ID,
ORG_NODEID,
GRADEID,
ISPUBLIC
FROM MEDIA_FACT)A,
(SELECT DISTINCT PRO.PRODUCT_NAME TEST_ADMIN,
       CUST.CUST_PROD_ID,
       TP.TP_TYPE TEST_PRGM,
       DIM1.ORG_NODEID    ST_ID,
       GDIM.GRADEID        GID,
       GDIM.GRADE_NAME     GR_NAME,
       SUB.SUBTEST_NAME
      

FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
     PRODUCT                        PRO,
     CUST_PRODUCT_LINK              CUST,
     ORG_PRODUCT_LINK               OPL,
     ORG_TEST_PROGRAM_LINK          OLINK,
     TEST_PROGRAM                   TP,
     ORG_NODE_DIM                   DIM1,
     GRADE_DIM                      GDIM,
     GRADE_SELECTION_LOOKUP         GLOOKUP,
     ASSESSMENT_DIM                 ASS_DIM,
     USERS                  U,
     ORG_USERS              OU
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = DIM1.ORG_NODEID
  AND DIM1.ORG_NODE_LEVEL = 1          --COMPARE WITH STATE LAVEL ORG_NODEID
  AND DIM1.ORG_NODEID = OLINK.ORG_NODEID           --- MAP DISTRICT LEVEL ORG_NODEID WITH TEST_PROGRAM
  AND OLINK.TP_ID = TP.TP_ID
  AND DIM1.ORG_NODEID = GLOOKUP.ORG_NODEID      --- MAP DISTRICT LEVEL ORG_NODEID WITH GRADE_SELECTION_LOOKUP
  AND GLOOKUP.ASSESSMENTID = SUB.ASSESSMENTID
  AND GLOOKUP.GRADEID = SUB.GRADEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND CUST.PRODUCTID  = ASS_DIM.PRODUCTID
  AND ASS_DIM.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND U.USERNAME = 'ctbadmin'                        -- LOGIN USER NAME
  AND U.USERID = OU.USERID
  AND OU.ORG_NODEID = DIM1.ORG_NODEID)B
  
  WHERE A.CUST_PROD_ID = B.CUST_PROD_ID
    AND A.ORG_NODEID   = B.ST_ID
    AND A.GRADEID = B.GID
    AND B.TEST_ADMIN = P_IN_TEST_ADMINISTRATION  --TEST ADMINISTRATION
    AND A.ISPUBLIC = IS_PUBLIC             -- TEST PROGRAM.CHANGE IT ACCORDING TO REQUIREMENT
    AND B.GR_NAME      = P_IN_GRADE;         --COMMENT THIS LINE TO SEE THE REPORT OF ALL GRADE UNDER A PARTICULAR SCHOOL OR ALL SCHOOL
  
  END IF;
  
  END PROF_PERFOR_SUMM_REPORT;
 
 
  --DISAGGREGATION SUMMARY
  PROCEDURE DISAGGREGATION_SUMM_REPORT(P_IN_TEST_ADMINISTRATION IN VARCHAR2,
                                      P_IN_TEST_PROGRAM        IN VARCHAR2,
                                      P_IN_CORP                IN VARCHAR2,
                                      P_IN_SCHOOL              IN VARCHAR2, 
                                      P_IN_GRADE               IN VARCHAR2,
                                      P_IN_SUBJECT             IN VARCHAR2,
                                      P_OUT_CUR_DISAGG_SUMM OUT GET_REFCURSOR) IS
  
   
   IS_PUBLIC NUMBER;                                  
   BEGIN
   
   
   IF P_IN_TEST_PROGRAM = 'PUBLIC' THEN 
   IS_PUBLIC :=1;
   ELSIF P_IN_TEST_PROGRAM = 'NON-PUBLIC' THEN 
   IS_PUBLIC :=0;
   END IF;
   
   
   IF  P_IN_CORP = 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
   
   OPEN P_OUT_CUR_DISAGG_SUMM FOR 
   


 SELECT DISTINCT DISA_CATE_TYPE.ORDERBY,
                DISA_CATE_TYPE.DISAGGREGATIONCATEGORYTYPENAME,
                DISA_CAT.ORDERBY,
                DISA_CAT.DISAGGREGATIONCATEGORYNAME,
                DISA.TOTALTESTED,
                NUMBERPASSPLUS,
                PERCENTPASSPLUS,
                NUMBERPASS,
                PERCENTPASS,
                TOTALNUMBERPASSING,
                TOTALPERCENTPASSING,
                NUMBERDNP,
                PERCENTDNP,
                NUMBERUND,
                PERCENTUND,
                MEDIANSCALESCORE,
                LOWSCOREOBTAINED,
                HIGHSCOREOBTAINED
  FROM DISA_FACT                    DISA,
       DISAGGREGATION_CATEGORY      DISA_CAT,
       DISAGGREGATION_CATEGORY_TYPE DISA_CATE_TYPE,
(SELECT DISTINCT  PRO.PRODUCT_NAME,
       CUST.CUST_PROD_ID,
       TP.TP_TYPE,
       DIM1.ORG_NODEID STA_ID,
       GDIM.GRADEID GID,
       GDIM.GRADE_NAME G_NAME,
       SUB_DIM.SUBTESTID SID,
       SUB_DIM.SUBTEST_NAME SUB_NAME

FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
     PRODUCT                        PRO,
     CUST_PRODUCT_LINK              CUST,
     ORG_PRODUCT_LINK               OPL,
     ORG_TEST_PROGRAM_LINK          OLINK,
     TEST_PROGRAM                   TP,
     ORG_NODE_DIM                   DIM1,
     GRADE_DIM                      GDIM,
     GRADE_SELECTION_LOOKUP         GLOOKUP,
     ASSESSMENT_DIM                 ASS_DIM,
     SUBTEST_DIM                    SUB_DIM
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = DIM1.ORG_NODEID
  AND DIM1.ORG_NODE_LEVEL = 1
  AND DIM1.ORG_NODEID = OLINK.ORG_NODEID
  AND OLINK.TP_ID = TP.TP_ID
  AND DIM1.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.ASSESSMENTID = SUB.ASSESSMENTID
  AND GLOOKUP.GRADEID = SUB.GRADEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND CUST.PRODUCTID  = ASS_DIM.PRODUCTID
  AND ASS_DIM.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND SUB.SUBTESTID = SUB_DIM.SUBTESTID)A
  
 WHERE A.CUST_PROD_ID = DISA.CUST_PROD_ID
   AND A.STA_ID = DISA.ORG_NODEID
   AND A.GID = DISA.GRADEID
   AND A.SID = DISA.SUBTESTID 
   AND DISA.DISAGGREGATIONCATEGORYID = DISA_CAT.DISAGGREGATIONCATEGORYID
   AND DISA_CATE_TYPE.DISAGGREGATIONCATEGORYTYPEID = DISA_CAT.DISAGGREGATIONCATEGORYTYPEID
   AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
   AND A.TP_TYPE = P_IN_TEST_PROGRAM
   AND A.G_NAME   = P_IN_GRADE
   AND A.SUB_NAME = P_IN_SUBJECT    -- 1.ENGLISH-LANGUAGE ARTS, 2.MATHEMATICS, 3.SCIENCE, 4.SOCIAL STUDIES, -- 5.READING.
   AND DISA.ISPUBLIC = IS_PUBLIC   -- 1. PUBLIC    , 0--NON-PUBLIC                                                
   ORDER BY DISA_CATE_TYPE.ORDERBY,
          DISA_CAT.ORDERBY; 
   
   
   
   
   ELSIF  P_IN_CORP != 'ALL' AND P_IN_SCHOOL = 'ALL' THEN
   OPEN P_OUT_CUR_DISAGG_SUMM FOR
   SELECT DISTINCT DISA_CATE_TYPE.ORDERBY,
               DISA_CATE_TYPE.DISAGGREGATIONCATEGORYTYPENAME,
               DISA_CAT.ORDERBY,
                DISA_CAT.DISAGGREGATIONCATEGORYNAME,
                DISA.TOTALTESTED,
                NUMBERPASSPLUS,
                PERCENTPASSPLUS,
                NUMBERPASS,
                PERCENTPASS,
                TOTALNUMBERPASSING,
                TOTALPERCENTPASSING,
                NUMBERDNP,
                PERCENTDNP,
                NUMBERUND,
                PERCENTUND,
                MEDIANSCALESCORE,
                LOWSCOREOBTAINED,
                HIGHSCOREOBTAINED
  FROM DISA_FACT                    DISA,
       DISAGGREGATION_CATEGORY      DISA_CAT,
       DISAGGREGATION_CATEGORY_TYPE DISA_CATE_TYPE,
(SELECT DISTINCT  PRO.PRODUCT_NAME,
       CUST.CUST_PROD_ID,
       TP.TP_TYPE,
       DIM1.ORG_NODEID DIS_ID,
       DIM1.ORG_NODE_NAME DIS_NAME,
       DIM2.ORG_NODEID SCH_ID,
       DIM2.ORG_NODE_NAME SCH_NAME,
       GDIM.GRADEID GID,
       GDIM.GRADE_NAME G_NAME,
       SUB_DIM.SUBTESTID SID,
       SUB_DIM.SUBTEST_NAME SUB_NAME

FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
     PRODUCT                        PRO,
     CUST_PRODUCT_LINK              CUST,
     ORG_PRODUCT_LINK               OPL,
     ORG_TEST_PROGRAM_LINK          OLINK,
     TEST_PROGRAM                   TP,
     ORG_NODE_DIM                   DIM1,
     ORG_NODE_DIM                   DIM2,
     GRADE_DIM                      GDIM,
     GRADE_SELECTION_LOOKUP         GLOOKUP,
     ASSESSMENT_DIM                 ASS_DIM,
     SUBTEST_DIM                    SUB_DIM
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = DIM1.ORG_NODEID
  AND DIM1.ORG_NODE_LEVEL = 2
  AND DIM1.ORG_NODEID = DIM2.PARENT_ORG_NODEID
  AND DIM2.ORG_NODEID = OLINK.ORG_NODEID
  AND OLINK.TP_ID = TP.TP_ID
  AND DIM2.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.ASSESSMENTID = SUB.ASSESSMENTID
  AND GLOOKUP.GRADEID = SUB.GRADEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND CUST.PRODUCTID  = ASS_DIM.PRODUCTID
  AND ASS_DIM.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND SUB.SUBTESTID = SUB_DIM.SUBTESTID)A
  
 WHERE A.CUST_PROD_ID = DISA.CUST_PROD_ID
   AND A.DIS_ID = DISA.ORG_NODEID
   AND A.GID = DISA.GRADEID
   AND A.SID = DISA.SUBTESTID 
   AND DISA.DISAGGREGATIONCATEGORYID = DISA_CAT.DISAGGREGATIONCATEGORYID
   AND DISA_CATE_TYPE.DISAGGREGATIONCATEGORYTYPEID = DISA_CAT.DISAGGREGATIONCATEGORYTYPEID
   AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
   AND A.TP_TYPE = P_IN_TEST_PROGRAM
   AND A.DIS_NAME = P_IN_CORP
   AND A.G_NAME   = P_IN_GRADE
   AND A.SUB_NAME = P_IN_SUBJECT    -- 1.ENGLISH-LANGUAGE ARTS, 2.MATHEMATICS, 3.SCIENCE, 4.SOCIAL STUDIES, 
                                                 -- 5.READING.
   ORDER BY DISA_CATE_TYPE.ORDERBY,
          DISA_CAT.ORDERBY; 
   
   
   
   
   
   ELSIF  P_IN_CORP != 'ALL' AND P_IN_SCHOOL != 'ALL' THEN
   
   OPEN P_OUT_CUR_DISAGG_SUMM FOR
   SELECT DISTINCT DISA_CATE_TYPE.ORDERBY,
               DISA_CATE_TYPE.DISAGGREGATIONCATEGORYTYPENAME,
               DISA_CAT.ORDERBY,
                DISA_CAT.DISAGGREGATIONCATEGORYNAME,
                DISA.TOTALTESTED,
                NUMBERPASSPLUS,
                PERCENTPASSPLUS,
                NUMBERPASS,
                PERCENTPASS,
                TOTALNUMBERPASSING,
                TOTALPERCENTPASSING,
                NUMBERDNP,
                PERCENTDNP,
                NUMBERUND,
                PERCENTUND,
                MEDIANSCALESCORE,
                LOWSCOREOBTAINED,
                HIGHSCOREOBTAINED
  FROM DISA_FACT                    DISA,
       DISAGGREGATION_CATEGORY      DISA_CAT,
       DISAGGREGATION_CATEGORY_TYPE DISA_CATE_TYPE,
(SELECT DISTINCT  PRO.PRODUCT_NAME,
       CUST.CUST_PROD_ID,
       TP.TP_TYPE,
       DIM1.ORG_NODEID DIS_ID,
       DIM1.ORG_NODE_NAME DIS_NAME,
       DIM2.ORG_NODEID SCH_ID,
       DIM2.ORG_NODE_NAME SCH_NAME,
       GDIM.GRADEID GID,
       GDIM.GRADE_NAME G_NAME,
       SUB_DIM.SUBTESTID SID,
       SUB_DIM.SUBTEST_NAME SUB_NAME

FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP SUB,
     PRODUCT                        PRO,
     CUST_PRODUCT_LINK              CUST,
     ORG_PRODUCT_LINK               OPL,
     ORG_TEST_PROGRAM_LINK          OLINK,
     TEST_PROGRAM                   TP,
     ORG_NODE_DIM                   DIM1,
     ORG_NODE_DIM                   DIM2,
     GRADE_DIM                      GDIM,
     GRADE_SELECTION_LOOKUP         GLOOKUP,
     ASSESSMENT_DIM                 ASS_DIM,
     SUBTEST_DIM                    SUB_DIM
     
WHERE PRO.PRODUCTID = CUST.PRODUCTID
  AND CUST.CUST_PROD_ID = OPL.CUST_PROD_ID
  AND OPL.ORG_NODEID = DIM1.ORG_NODEID
  AND DIM1.ORG_NODE_LEVEL = 2
  AND DIM1.ORG_NODEID = DIM2.PARENT_ORG_NODEID
  AND DIM2.ORG_NODEID = OLINK.ORG_NODEID
  AND OLINK.TP_ID = TP.TP_ID
  AND DIM2.ORG_NODEID = GLOOKUP.ORG_NODEID
  AND GLOOKUP.ASSESSMENTID = SUB.ASSESSMENTID
  AND GLOOKUP.GRADEID = SUB.GRADEID
  AND GLOOKUP.GRADEID = GDIM.GRADEID
  AND CUST.PRODUCTID  = ASS_DIM.PRODUCTID
  AND ASS_DIM.ASSESSMENTID = GLOOKUP.ASSESSMENTID
  AND SUB.SUBTESTID = SUB_DIM.SUBTESTID)A
  
 WHERE A.CUST_PROD_ID = DISA.CUST_PROD_ID
   AND A.SCH_ID = DISA.ORG_NODEID
   AND A.GID = DISA.GRADEID
   AND A.SID = DISA.SUBTESTID 
   AND DISA.DISAGGREGATIONCATEGORYID = DISA_CAT.DISAGGREGATIONCATEGORYID
   AND DISA_CATE_TYPE.DISAGGREGATIONCATEGORYTYPEID = DISA_CAT.DISAGGREGATIONCATEGORYTYPEID
   AND A.PRODUCT_NAME = P_IN_TEST_ADMINISTRATION
   AND A.TP_TYPE = P_IN_TEST_PROGRAM
   AND A.DIS_NAME = P_IN_CORP
   AND A.SCH_NAME = P_IN_SCHOOL
   AND A.G_NAME   = P_IN_GRADE
   AND A.SUB_NAME = P_IN_SUBJECT    -- 1.ENGLISH-LANGUAGE ARTS, 2.MATHEMATICS, 3.SCIENCE, 4.SOCIAL STUDIES, 
                                                 -- 5.READING.
   ORDER BY DISA_CATE_TYPE.ORDERBY,
          DISA_CAT.ORDERBY; 
     
     
     
     
     END IF;
   
   
   
   
   
   END DISAGGREGATION_SUMM_REPORT;



END PRISM_REPORT;
/
