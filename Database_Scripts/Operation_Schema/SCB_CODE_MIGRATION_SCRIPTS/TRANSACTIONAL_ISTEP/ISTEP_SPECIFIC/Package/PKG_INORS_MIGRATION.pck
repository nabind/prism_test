CREATE OR REPLACE PACKAGE PKG_INORS_MIGRATION IS

  -- Author  : Debashis Deb(TCS)
  -- Created : 11/1/2013
  -- Purpose : This Package is used for migrating data from Inors to Current PRISM Data Model

/*  PROCEDURE PROC_ORG_NODE_DIM_MIGRATE;

  PROCEDURE PROC_LSTNODE_LINK_MIGRATE;

  PROCEDURE PROC_ORGTESTPROG_LINK_MIGRATE;

  PROCEDURE PROC_ORGPROD_LINK_POPULATE;

  PROCEDURE PROC_STU_BIO_DIM_POPULATE;

  PROCEDURE PROC_SUBTEST_SCORE_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_OBJECTIVE_SCORE_POPULATE(IN_ASSESSMENTID IN NUMBER);

  PROCEDURE PROC_CLASS_SUMM_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_GRT_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_MEDIA_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_SUMT_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_STFD_POPULATE(IN_ASSESSMENT_ID NUMBER); 
  


  PROCEDURE PROC_RESULTS_ASFD_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_DISA_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_SPPR_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_UDTR_SUMM_POP(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_UDTR_ROSTER_POP(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULT_ACAD_SUMM_POP(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_RESULTS_PEID_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PROC_CUTSCR_SCLESCR_POPULATE(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PRC_DISAGGREGATION_CATEGR_TYP(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PRC_DISAGGREGATION_CATEGORY(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PRC_CUTSCOREIPI(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PRC_ASFD_ORDERBY;

  PROCEDURE PRC_GRADE_SELECTION;

  PROCEDURE PRC_USER_UPLOAD;

  PROCEDURE proc_parent_user_load;

  PROCEDURE LOAD_ISR_PDF_FILES (P_CUST_PROD_ID IN NUMBER);

   PROCEDURE LOAD_IP_PDF_FILES (P_CUST_PROD_ID IN NUMBER);

  PROCEDURE PRC_POST_MIGRATION_EXCEP_CHCK(IN_ASSESSMENT_ID NUMBER);

  PROCEDURE PRC_POST_MIGRATION_EXCEP_RPRT(IN_ASSESSMENT_ID NUMBER);

 PROCEDURE PROC_INVITATION_CODE_POP(P_CUST_PRODID IN NUMBER) ;

 PROCEDURE proc_itemset_dim_load ;

   PROCEDURE PROC_ASFD_ITEMSETID_POP(P_CUST_PRODID IN NUMBER) ;

  PROCEDURE PRC_MIGRATION_WRAPPER;

    PROCEDURE PRC_USER_UPLOAD_NEW;
    */
          PROCEDURE PROC_OBJ_SCORE_POPULATE_NEW(IN_ASSESSMENTID IN NUMBER);
          
          PROCEDURE proc_parent_user_load_excep; 

END PKG_INORS_MIGRATION;
/
CREATE OR REPLACE PACKAGE BODY PKG_INORS_MIGRATION IS

 /* PROCEDURE PROC_ORG_NODE_DIM_MIGRATE IS
  BEGIN
    INSERT INTO ORG_NODE_DIM
      SELECT ORG_NODEID,
             ORG_NODE_NAME,
             ORG_NODE_CODE,
             ORG_NODE_LEVEL,
             'NA' STRC_ELEMENT,
             'NA' SPECIAL_CODES,
             ORG_MODE,
             PARENT_ORG_NODEID,
             LTRIM(SYS_CONNECT_BY_PATH(INVIEW.ORG_NODE_CODE, '~'), '~') AS ORG_NODE_CODE_PATH,
             EMAILS,
             CUSTOMERID,
             SYSDATE,
             SYSDATE
        FROM (SELECT ORG_PRISM_ID AS ORG_NODEID,
                     INODE_NAME AS ORG_NODE_NAME,
                     INODE_CODE AS ORG_NODE_CODE,
                     CASE
                       WHEN INODE_TYPE = 'STATE' THEN
                        1
                       WHEN INODE_TYPE = 'DISTRICT' THEN
                        2
                       WHEN INODE_TYPE = 'SCHOOL' THEN
                        3
                       WHEN INODE_TYPE = 'CLASS' THEN
                        4
                     END AS ORG_NODE_LEVEL,
                     NULL AS STRC_ELEMENT,
                     NULL AS SPECIAL_CODES,
                     'PP' AS ORG_MODE,
                     CASE
                       WHEN INODE_TYPE = 'STATE' THEN
                        0
                       WHEN INODE_TYPE = 'DISTRICT' THEN
                        (SELECT ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG
                          WHERE INODE_TYPE = 'STATE'
                            AND INODE_ID = A.INODE_PARENTID)
                       WHEN INODE_TYPE = 'SCHOOL' THEN
                        (SELECT ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG
                          WHERE INODE_TYPE = 'DISTRICT'
                            AND INODE_ID = A.INODE_PARENTID)

                       WHEN INODE_TYPE = 'CLASS' THEN
                        (SELECT ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG
                          WHERE INODE_TYPE = 'SCHOOL'
                            AND INODE_ID = A.INODE_PARENTID)
                     END AS PARENT_ORG_NODEID,
                     NULL AS ORG_NODE_CODE_PATH,
                     NULL AS EMAILS,
                     (SELECT CUSTOMERID
                        FROM CUSTOMER_INFO
                       WHERE CUSTOMER_NAME = 'ISTEP') AS CUSTOMERID
                FROM ISTEP_DATAMIG.RECORD_MIG A) INVIEW
       START WITH INVIEW.ORG_NODE_LEVEL = 1
      CONNECT BY NOCYCLE PRIOR INVIEW.ORG_NODEID = INVIEW.PARENT_ORG_NODEID;

    COMMIT;

    BEGIN
      FOR I IN (SELECT DECODE(UPPER("DistrictTypeDescription"),
                              'PRIVATE',
                              'NON-PUBLIC',
                              UPPER("DistrictTypeDescription")) TYPE,
                       INODE_ID,
                       C.ORG_PRISM_ID
                  FROM ISTEP_DATAMIG.MIG_DISTRICTTYPE A,
                       ISTEP_DATAMIG.MIG_DISTRICT     B,
                       ISTEP_DATAMIG.RECORD_MIG       C
                 WHERE A."DistrictTypeID" = B.DISTRICTTYPEID
                   AND B.DISTRICTID = C.INODE_ID
                   AND C.INODE_TYPE = 'DISTRICT') LOOP
        UPDATE ORG_NODE_DIM
           SET ORG_MODE = I.TYPE
         WHERE ORG_NODEID = I.ORG_PRISM_ID;
      END LOOP;

    END;

    UPDATE ORG_NODE_DIM A
       SET ORG_MODE = (SELECT ORG_MODE
                         FROM ORG_NODE_DIM
                        WHERE ORG_NODE_LEVEL = 2
                          AND ORG_NODEID = A.PARENT_ORG_NODEID)
     WHERE ORG_NODE_LEVEL = 3;

    UPDATE ORG_NODE_DIM A
       SET ORG_MODE = (SELECT ORG_MODE
                         FROM ORG_NODE_DIM
                        WHERE ORG_NODE_LEVEL = 3
                          AND ORG_NODEID = A.PARENT_ORG_NODEID)
     WHERE ORG_NODE_LEVEL = 4;

    COMMIT;

  END PROC_ORG_NODE_DIM_MIGRATE;

  PROCEDURE PROC_LSTNODE_LINK_MIGRATE IS
  BEGIN

    -- State Level Insertions

    -- Issue : Primary Key is based on org_node and last node is but for INORS and state level insertion all the last node is going to remain
    --        the same as well as the State Org node but only Admin id is going change -

    \*INSERT INTO ORG_LSTNODE_LINK
    SELECT GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1,
           CHILD_ORG.ORG_NODEID LSTNODEID,
           101,
           SYSDATE

      FROM ORG_NODE_DIM CHILD_ORG,
           ORG_NODE_DIM PARENT_ORG,
           ORG_NODE_DIM GRAND_PARENT_ORG,
           ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
     WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
       AND PARENT_ORG.ORG_NODE_LEVEL = 3
       AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
       AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
       AND PARENT_ORG.PARENT_ORG_NODEID = GRAND_PARENT_ORG.ORG_NODEID
       AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
       AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
           GREAT_GRAND_PARENT_ORG.ORG_NODEID
    UNION ALL
    SELECT GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1,
           CHILD_ORG.ORG_NODEID LSTNODEID,
           102,
           SYSDATE
      FROM ORG_NODE_DIM CHILD_ORG,
           ORG_NODE_DIM PARENT_ORG,
           ORG_NODE_DIM GRAND_PARENT_ORG,
           ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
     WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
       AND PARENT_ORG.ORG_NODE_LEVEL = 3
       AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
       AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
       AND PARENT_ORG.PARENT_ORG_NODEID = GRAND_PARENT_ORG.ORG_NODEID
       AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
       AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
           GREAT_GRAND_PARENT_ORG.ORG_NODEID
    UNION ALL
    SELECT GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1,
           CHILD_ORG.ORG_NODEID LSTNODEID,
           103,
           SYSDATE
      FROM ORG_NODE_DIM CHILD_ORG,
           ORG_NODE_DIM PARENT_ORG,
           ORG_NODE_DIM GRAND_PARENT_ORG,
           ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
     WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
       AND PARENT_ORG.ORG_NODE_LEVEL = 3
       AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
       AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
       AND PARENT_ORG.PARENT_ORG_NODEID = GRAND_PARENT_ORG.ORG_NODEID
       AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
       AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
           GREAT_GRAND_PARENT_ORG.ORG_NODEID
    UNION ALL
    SELECT GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1,
           CHILD_ORG.ORG_NODEID LSTNODEID,
           104,
           SYSDATE
      FROM ORG_NODE_DIM CHILD_ORG,
           ORG_NODE_DIM PARENT_ORG,
           ORG_NODE_DIM GRAND_PARENT_ORG,
           ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
     WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
       AND PARENT_ORG.ORG_NODE_LEVEL = 3
       AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
       AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
       AND PARENT_ORG.PARENT_ORG_NODEID = GRAND_PARENT_ORG.ORG_NODEID
       AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
       AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
           GREAT_GRAND_PARENT_ORG.ORG_NODEID;

    COMMIT;
    *\
    -- State Level Data Mapped with Lowest Node
    INSERT INTO ORG_LSTNODE_LINK
      SELECT CHILD_ORG.ORG_NODEID LSTNODEID,
             GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1,
             101,
             SYSDATE

        FROM ORG_NODE_DIM CHILD_ORG,
             ORG_NODE_DIM PARENT_ORG,
             ORG_NODE_DIM GRAND_PARENT_ORG,
             ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
       WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
         AND PARENT_ORG.ORG_NODE_LEVEL = 3
         AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
         AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
         AND PARENT_ORG.PARENT_ORG_NODEID = GRAND_PARENT_ORG.ORG_NODEID
         AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
         AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
             GREAT_GRAND_PARENT_ORG.ORG_NODEID;

    --  District Level Insertion
    -- As Discussed with Amalan on 07/11/2013
    -- Lstnode id and orgnode id should not be repitive on year

    INSERT INTO ORG_LSTNODE_LINK
      SELECT LSTNODEID,
             ORG_NODEID_LEVEL2,
             CASE
               WHEN isoftyearid = 22 THEN
                104
               WHEN isoftyearid = 23 THEN
                103
               WHEN isoftyearid = 24 THEN
                102
               WHEN isoftyearid = 25 THEN
                101
             END AS ADMIN_ID,
             SYSDATE
        FROM (SELECT DISTINCT ORG_NODEID_LEVEL2,
                              LSTNODEID,
                              MIN(MISOFY."iSoftYearID") over(PARTITION BY ORG_NODEID_LEVEL2, LSTNODEID) AS isoftyearid,
                              SYSDATE

                FROM ISTEP_DATAMIG.RECORD_MIG REC_MIG,
                     ISTEP_DATAMIG.MIG_DISTRICT MD,
                     ISTEP_DATAMIG.MIG_DISTRICTISOFTYEARCON MDIC,
                     ISTEP_DATAMIG.MIG_ISOFTYEAR MISOFY,
                     (SELECT CHILD_ORG.ORG_NODEID              LSTNODEID,
                             PARENT_ORG.ORG_NODEID             ORG_NODEID_LEVEL3,
                             GRAND_PARENT_ORG.ORG_NODEID       ORG_NODEID_LEVEL2,
                             GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1
                        FROM ORG_NODE_DIM CHILD_ORG,
                             ORG_NODE_DIM PARENT_ORG,
                             ORG_NODE_DIM GRAND_PARENT_ORG,
                             ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
                       WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
                         AND PARENT_ORG.ORG_NODE_LEVEL = 3
                         AND CHILD_ORG.PARENT_ORG_NODEID =
                             PARENT_ORG.ORG_NODEID
                         AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
                         AND PARENT_ORG.PARENT_ORG_NODEID =
                             GRAND_PARENT_ORG.ORG_NODEID
                         AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
                         AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
                             GREAT_GRAND_PARENT_ORG.ORG_NODEID) INVIEW
               WHERE REC_MIG.INODE_ID = MD.DISTRICTID
                 AND REC_MIG.INODE_TYPE = 'DISTRICT'
                 AND MDIC.DISTRICTID = MD.DISTRICTID
                 AND MDIC.ISOFTYEARID = MISOFY."iSoftYearID"
                 AND MISOFY."iSoftYearID" IN (22, 23, 24, 25)
                 AND REC_MIG.ORG_PRISM_ID = INVIEW.ORG_NODEID_LEVEL2);
    \* SELECT DISTINCT ORG_NODEID_LEVEL2,
                      LSTNODEID,
                      CASE
                        WHEN MISOFY."iSoftYearID" = 23 THEN
                         104
                        WHEN MISOFY."iSoftYearID" = 24 THEN
                         103
                        WHEN MISOFY."iSoftYearID" = 25 THEN
                         102
                        WHEN MISOFY."iSoftYearID" = 26 THEN
                         101
                      END AS ADMIN_ID,
                      SYSDATE

        FROM ISTEP_DATAMIG.RECORD_MIG REC_MIG,
             ISTEP_DATAMIG.MIG_DISTRICT MD,
             ISTEP_DATAMIG.MIG_DISTRICTISOFTYEARCON MDIC,
             ISTEP_DATAMIG.MIG_ISOFTYEAR MISOFY,
             (SELECT CHILD_ORG.ORG_NODEID              LSTNODEID,
                     PARENT_ORG.ORG_NODEID             ORG_NODEID_LEVEL3,
                     GRAND_PARENT_ORG.ORG_NODEID       ORG_NODEID_LEVEL2,
                     GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1
                FROM ORG_NODE_DIM CHILD_ORG,
                     ORG_NODE_DIM PARENT_ORG,
                     ORG_NODE_DIM GRAND_PARENT_ORG,
                     ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
               WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
                 AND PARENT_ORG.ORG_NODE_LEVEL = 3
                 AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
                 AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
                 AND PARENT_ORG.PARENT_ORG_NODEID =
                     GRAND_PARENT_ORG.ORG_NODEID
                 AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
                 AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
                     GREAT_GRAND_PARENT_ORG.ORG_NODEID) INVIEW
       WHERE REC_MIG.INODE_ID = MD.DISTRICTID
         AND REC_MIG.INODE_TYPE = 'DISTRICT'
         AND MDIC.DISTRICTID = MD.DISTRICTID
         AND MDIC.ISOFTYEARID = MISOFY."iSoftYearID"
         AND MISOFY."iSoftYearID" IN (23, 24, 25, 26)
         AND REC_MIG.ORG_PRISM_ID = INVIEW.ORG_NODEID_LEVEL2;
    *\
    COMMIT;

    -- School Level Insertion
    INSERT INTO ORG_LSTNODE_LINK
      SELECT LSTNODEID,
             ORG_NODEID_LEVEL3,

             CASE
               WHEN isoftyearid = 22 THEN
                104
               WHEN isoftyearid = 23 THEN
                103
               WHEN isoftyearid = 24 THEN
                102
               WHEN isoftyearid = 25 THEN
                101
             END AS ADMIN_ID,
             SYSDATE
        FROM (SELECT DISTINCT ORG_NODEID_LEVEL3,
                              LSTNODEID,
                              MIN(MISOFY."iSoftYearID") over(PARTITION BY ORG_NODEID_LEVEL3, LSTNODEID) AS isoftyearid,
                              SYSDATE

                FROM ISTEP_DATAMIG.RECORD_MIG REC_MIG,
                     ISTEP_DATAMIG.MIG_SCHOOL MS,
                     ISTEP_DATAMIG.MIG_SCHOOLISOFTYEARCON MSIC,
                     ISTEP_DATAMIG.MIG_ISOFTYEAR MISOFY,
                     (SELECT CHILD_ORG.ORG_NODEID              LSTNODEID,
                             PARENT_ORG.ORG_NODEID             ORG_NODEID_LEVEL3,
                             GRAND_PARENT_ORG.ORG_NODEID       ORG_NODEID_LEVEL2,
                             GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1
                        FROM ORG_NODE_DIM CHILD_ORG,
                             ORG_NODE_DIM PARENT_ORG,
                             ORG_NODE_DIM GRAND_PARENT_ORG,
                             ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
                       WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
                         AND PARENT_ORG.ORG_NODE_LEVEL = 3
                         AND CHILD_ORG.PARENT_ORG_NODEID =
                             PARENT_ORG.ORG_NODEID
                         AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
                         AND PARENT_ORG.PARENT_ORG_NODEID =
                             GRAND_PARENT_ORG.ORG_NODEID
                         AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
                         AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
                             GREAT_GRAND_PARENT_ORG.ORG_NODEID) INVIEW
               WHERE REC_MIG.INODE_ID = MS.SCHOOLID
                 AND REC_MIG.INODE_TYPE = 'SCHOOL'
                 AND MSIC.SCHOOLID = MS.SCHOOLID
                 AND MSIC.ISOFTYEARID = MISOFY."iSoftYearID"
                 AND MISOFY."iSoftYearID" IN (22, 23, 24, 25)
                 AND REC_MIG.ORG_PRISM_ID = INVIEW.ORG_NODEID_LEVEL3);
    \*      SELECT DISTINCT ORG_NODEID_LEVEL3,
                        LSTNODEID,
                        CASE
                          WHEN MISOFY."iSoftYearID" = 23 THEN
                           104
                          WHEN MISOFY."iSoftYearID" = 24 THEN
                           103
                          WHEN MISOFY."iSoftYearID" = 25 THEN
                           102
                          WHEN MISOFY."iSoftYearID" = 26 THEN
                           101
                        END AS ADMIN_ID,
                        SYSDATE

          FROM ISTEP_DATAMIG.RECORD_MIG REC_MIG,
               ISTEP_DATAMIG.MIG_SCHOOL MS,
               ISTEP_DATAMIG.MIG_SCHOOLISOFTYEARCON MSIC,
               ISTEP_DATAMIG.MIG_ISOFTYEAR MISOFY,
               (SELECT CHILD_ORG.ORG_NODEID              LSTNODEID,
                       PARENT_ORG.ORG_NODEID             ORG_NODEID_LEVEL3,
                       GRAND_PARENT_ORG.ORG_NODEID       ORG_NODEID_LEVEL2,
                       GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1
                  FROM ORG_NODE_DIM CHILD_ORG,
                       ORG_NODE_DIM PARENT_ORG,
                       ORG_NODE_DIM GRAND_PARENT_ORG,
                       ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
                 WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
                   AND PARENT_ORG.ORG_NODE_LEVEL = 3
                   AND CHILD_ORG.PARENT_ORG_NODEID = PARENT_ORG.ORG_NODEID
                   AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
                   AND PARENT_ORG.PARENT_ORG_NODEID =
                       GRAND_PARENT_ORG.ORG_NODEID
                   AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
                   AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
                       GREAT_GRAND_PARENT_ORG.ORG_NODEID) INVIEW
         WHERE REC_MIG.INODE_ID = MS.SCHOOLID
           AND REC_MIG.INODE_TYPE = 'SCHOOL'
           AND MSIC.SCHOOLID = MS.SCHOOLID
           AND MSIC.ISOFTYEARID = MISOFY."iSoftYearID"
           AND MISOFY."iSoftYearID" IN (23, 24, 25, 26)
           AND REC_MIG.ORG_PRISM_ID = INVIEW.ORG_NODEID_LEVEL3;
    *\

    INSERT INTO ORG_LSTNODE_LINK
      SELECT LSTNODEID lowest,
             LSTNODEID orgnode,

             CASE
               WHEN ISOFTYEARID = 22 THEN
                104
               WHEN ISOFTYEARID = 23 THEN
                103
               WHEN ISOFTYEARID = 24 THEN
                102
               WHEN ISOFTYEARID = 25 THEN
                101
             END AS ADMIN_ID,
             SYSDATE
        FROM (SELECT DISTINCT LSTNODEID,
                              --LSTNODEID,
                              MIN(MISOFY."iSoftYearID") OVER(PARTITION BY ORG_NODEID_LEVEL3, LSTNODEID) AS ISOFTYEARID,
                              SYSDATE

                FROM ISTEP_DATAMIG.RECORD_MIG REC_MIG,
                     ISTEP_DATAMIG.MIG_CLASS MS,
                     (SELECT *
                        FROM ISTEP_DATAMIG.MIG_CLASSTESTCON A,
                             ISTEP_DATAMIG.MIG_TEST         B
                       WHERE A.TESTID = B."TestID"
                         AND B."iSoftYearID" IN (22, 23, 24, 25)) MSIC,
                     ISTEP_DATAMIG.MIG_ISOFTYEAR MISOFY,
                     (SELECT CHILD_ORG.ORG_NODEID              LSTNODEID,
                             PARENT_ORG.ORG_NODEID             ORG_NODEID_LEVEL3,
                             GRAND_PARENT_ORG.ORG_NODEID       ORG_NODEID_LEVEL2,
                             GREAT_GRAND_PARENT_ORG.ORG_NODEID ORG_NODEID_LEVEL1
                        FROM ORG_NODE_DIM CHILD_ORG,
                             ORG_NODE_DIM PARENT_ORG,
                             ORG_NODE_DIM GRAND_PARENT_ORG,
                             ORG_NODE_DIM GREAT_GRAND_PARENT_ORG
                       WHERE CHILD_ORG.ORG_NODE_LEVEL = 4
                         AND PARENT_ORG.ORG_NODE_LEVEL = 3
                         AND CHILD_ORG.PARENT_ORG_NODEID =
                             PARENT_ORG.ORG_NODEID
                         AND GRAND_PARENT_ORG.ORG_NODE_LEVEL = 2
                         AND PARENT_ORG.PARENT_ORG_NODEID =
                             GRAND_PARENT_ORG.ORG_NODEID
                         AND GREAT_GRAND_PARENT_ORG.ORG_NODE_LEVEL = 1
                         AND GRAND_PARENT_ORG.PARENT_ORG_NODEID =
                             GREAT_GRAND_PARENT_ORG.ORG_NODEID) INVIEW
               WHERE REC_MIG.INODE_ID = MS.CLASSID
                 AND REC_MIG.INODE_TYPE = 'CLASS'
                 AND MSIC.CLASSID = MS.CLASSID
                 AND MSIC."iSoftYearID" = MISOFY."iSoftYearID"
                 AND MISOFY."iSoftYearID" IN (22, 23, 24, 25)
                 AND REC_MIG.ORG_PRISM_ID = INVIEW.LSTNODEID);

    COMMIT;

  END PROC_LSTNODE_LINK_MIGRATE;

  PROCEDURE PROC_ORGTESTPROG_LINK_MIGRATE IS
  BEGIN

    INSERT INTO ORG_TEST_PROGRAM_LINK
      SELECT ORG_NODEID, TP_ID, SYSDATE
        FROM (SELECT ORG_NODEID FROM ORG_NODE_DIM WHERE ORG_NODE_LEVEL = 1) A,
             TEST_PROGRAM;
    COMMIT;

    INSERT INTO ORG_TEST_PROGRAM_LINK
      SELECT ORG_PRISM_ID, TTMAP.TP_ID, SYSDATE
        FROM (SELECT MISOFY."iSoftYearID",
                     MT."TestID",
                     MT."TestName",
                     CASE
                       WHEN MDT."DistrictTypeID" = 1 THEN
                        "PublicOrgTP"
                       WHEN MDT."DistrictTypeID" = 2 THEN
                        "PrivateOrgTP"
                     END AS ORG_TP_CODE,
                     MD.DISTRICTID,
                     REC_MIG.ORG_PRISM_ID,
                     MDT."DistrictTypeDescription"
                FROM ISTEP_DATAMIG.MIG_ISOFTYEAR       MISOFY,
                     ISTEP_DATAMIG.MIG_TEST            MT,
                     ISTEP_DATAMIG.MIG_ORGTPTESTCON    MORGTP,
                     ISTEP_DATAMIG.MIG_DISTRICT        MD,
                     ISTEP_DATAMIG.MIG_DISTRICTTYPE    MDT,
                     ISTEP_DATAMIG.MIG_DISTRICTTESTCON MDTCON,
                     ISTEP_DATAMIG.RECORD_MIG          REC_MIG
               WHERE MISOFY."iSoftYearID" IN (22, 23, 24, 25)
                 AND MT."iSoftYearID" = MISOFY."iSoftYearID"
                 AND MORGTP."TestID" = MT."TestID"
                 AND MT."TestID" = MDTCON.TESTID
                 AND MD.DISTRICTID = MDTCON.DISTRICTID
                 AND MDT."DistrictTypeID" = MD.DISTRICTTYPEID
                 AND REC_MIG.INODE_ID = MD.DISTRICTID
                 AND REC_MIG.INODE_TYPE = 'DISTRICT') INLINE,
             TEST_TP_MAP TTMAP
       WHERE TTMAP.TP_CODE = ORG_TP_CODE;

    COMMIT;

    INSERT INTO ORG_TEST_PROGRAM_LINK
     \* SELECT DISTINCT REC_MIG1.ORG_PRISM_ID, B.TP_ID, SYSDATE
        FROM ORG_TEST_PROGRAM_LINK                B,
             ISTEP_DATAMIG.RECORD_MIG             REC_MIG,
             ISTEP_DATAMIG.MIG_SCHOOL             MS,
             ISTEP_DATAMIG.MIG_SCHOOLISOFTYEARCON MSIC,
             ISTEP_DATAMIG.MIG_ISOFTYEAR          C,
             ISTEP_DATAMIG.RECORD_MIG             REC_MIG1
       WHERE B.ORG_NODEID = REC_MIG.ORG_PRISM_ID
         AND REC_MIG.INODE_ID = MS.DISTRICTID
         AND MS.SCHOOLID = MSIC.SCHOOLID
         AND MS.SCHOOLID = REC_MIG1.INODE_ID
         AND REC_MIG1.INODE_TYPE = 'SCHOOL'
         AND MSIC.ISOFTYEARID = C."iSoftYearID"
            --AND    b.org_nodeid = 360181
         AND C."iSoftYearID" IN (22, 23, 24, 25);*\
        SELECT  REC_MIG.ORG_PRISM_ID, ttm.TP_ID ,SYSDATE
        FROM ISTEP_DATAMIG.MIG_SCHOOL   MS ,
             ISTEP_DATAMIG.mig_schooltestcon mtcon,
             test_tp_map ttm ,
             ISTEP_DATAMIG.RECORD_MIG             REC_MIG ,
             test_program tp
        WHERE ms.schoolid =  mtcon.schoolid
        AND   mtcon.testid = ttm.testid
        AND  ms.schoolid =   REC_MIG.inode_id
        AND  rec_mig.INODE_TYPE='SCHOOL'
        AND  tp.tp_id = ttm.tp_id
        AND EXISTS (SELECT 1 FROM org_node_dim d WHERE REC_MIG.ORG_PRISM_ID = org_nodeid
        AND org_node_level =3
        AND d.org_mode = tp.tp_type );

    COMMIT;

    INSERT INTO ORG_TEST_PROGRAM_LINK
     \* SELECT REC_MIG1.ORG_PRISM_ID, B.TP_ID, SYSDATE
        FROM ORG_TEST_PROGRAM_LINK B,
             ISTEP_DATAMIG.RECORD_MIG REC_MIG,
             ISTEP_DATAMIG.MIG_CLASS MS,
             (SELECT *
                FROM ISTEP_DATAMIG.MIG_CLASSTESTCON A,
                     ISTEP_DATAMIG.MIG_TEST         B
               WHERE A.TESTID = B."TestID") MSIC,
             ISTEP_DATAMIG.MIG_ISOFTYEAR C,
             ISTEP_DATAMIG.RECORD_MIG REC_MIG1
       WHERE EXISTS (SELECT 1
                FROM ORG_NODE_DIM A
               WHERE ORG_NODE_LEVEL = 3
                 AND A.ORG_NODEID = B.ORG_NODEID)
         AND B.ORG_NODEID = REC_MIG.ORG_PRISM_ID
         AND REC_MIG.INODE_ID = MS.SCHOOLID
         AND MS.CLASSID = MSIC.CLASSID
         AND MS.CLASSID = REC_MIG1.INODE_ID
         AND REC_MIG1.INODE_TYPE = 'CLASS'
         AND MSIC."iSoftYearID" = C."iSoftYearID";*\
          SELECT  REC_MIG.ORG_PRISM_ID, ttm.TP_ID ,SYSDATE
          FROM ISTEP_DATAMIG.MIG_CLASS   MS ,
               ISTEP_DATAMIG.mig_CLASSTESTCON mtcon,
               test_tp_map ttm ,
               ISTEP_DATAMIG.RECORD_MIG             REC_MIG ,
               test_program tp
          WHERE ms.classid =  mtcon.classid
          AND   mtcon.testid = ttm.testid
          AND  ms.classid =   REC_MIG.inode_id
          AND  rec_mig.INODE_TYPE='CLASS'
          AND  tp.tp_id = ttm.tp_id
          AND EXISTS (SELECT 1 FROM org_node_dim d WHERE REC_MIG.ORG_PRISM_ID = org_nodeid
          AND org_node_level =4 );

    COMMIT;

  END PROC_ORGTESTPROG_LINK_MIGRATE;

  PROCEDURE PROC_ORGPROD_LINK_POPULATE IS
  BEGIN
   EXECUTE IMMEDIATE 'TRUNCATE TABLE org_product_link';

    INSERT INTO org_product_link
    \* SELECT DISTINCT ROWNUM, ORG_NODEID, CUST_PROD_ID, SYSDATE
                       FROM ORG_TEST_PROGRAM_LINK     A,
                            TEST_PROGRAM_PRODUCT_LINK B,
                            CUST_PRODUCT_LINK         C
                      WHERE A.TP_ID = B.TP_ID
                        AND B.PRODUCTID = C.PRODUCTID
                        AND B.CUSTOMERID = C.CUSTOMERID
                        AND B.ADMINID = C.ADMINID *\

      SELECT ROWNUM, ORG_NODEID, CUST_PROD_ID, SYSDATE
        FROM (SELECT DISTINCT ORG_NODEID, CUST_PROD_ID
                FROM ORG_TEST_PROGRAM_LINK A,
                     test_tp_map           B,
                     CUST_PRODUCT_LINK     C
               WHERE A.TP_ID = B.TP_ID
                 AND B.PRODUCTID = C.PRODUCTID);
    COMMIT;
  END PROC_ORGPROD_LINK_POPULATE;

  PROCEDURE PROC_STU_BIO_DIM_POPULATE IS
  BEGIN
    INSERT \*+APPEND*\ INTO STUDENT_BIO_DIM
    \*SELECT DISTINCT  student_det.STUDENTID,
                           student_det.student_first_name,
                           student_det.STUDENT_MIDDLE_INITIAL,
                           student_det.student_last_name,
                           student_det.BIRTH_DATE ,
                           student_det.STUDENTID AS TEST_ELEMENT_ID,
                           0 AS INT_STUDENT_ID,
                           0 AS EXT_STUDENT_ID,
                           'NA' lithocode,

                           (SELECT genderid FROM gender_dim WHERE GENDER_CODE = STUDENTS_GENDER) genderid,
                           vwsub.gradeid,
                           NULL edu_centerid,
                           NULL barcode ,
                           'NA'  special_codes,
                           'PP' student_mode,
                           (SELECT org_nodeid FROM org_node_dim WHERE org_node_code =ORGTSTGPGM||TEACHER_ELEMENT ) AS org_nodeid ,
                           1000 customerid ,
                           (SELECT adminid FROM TEST_TP_MAP WHERE  ISOFTYEARID=student_det.ISOFTYEARID
                           AND tp_code =student_det.ORGTSTGPGM) adminid ,
                           'N',
                           SYSDATE,
                           SYSDATE
                      FROM (SELECT *
                              FROM ISTEP_DATAMIG.MIG_RESULTS_GRT A,
                                   (SELECT DISTINCT SUBTESTID
                                      FROM SUBTEST_DIM
                                     WHERE CONTENTID IN (SELECT CONTENTID
                                                           FROM CONTENT_DIM
                                                          WHERE ASSESSMENTID = IN_ASSESSMENT_ID))
                             WHERE A.STUDENTID IN (2037350, 1893530, 1974703)) student_det,
                                  istep_datamig.mig_gradelevel mglid,
                                  vw_subtest_grade_objective_map vwsub
                      WHERE           student_det.gradelevelid =mglid."GradeLevelID"
                      AND         vwsub.grade_code = mglid."GradeLevelCode"
                      AND        vwsub.subtestid = student_det.subtestid
                      AND        vwsub.assessmentid = IN_ASSESSMENT_ID; *\
   \*   SELECT STUDENT_BIO_ID,
             STUDENT_FIRST_NAME,
             STUDENT_MIDDLE_INITIAL,
             STUDENT_LAST_NAME,
             BIRTH_DATE,
             STUDENTID,
             0 AS INT_STUDENT_ID,
             0 AS EXT_STUDENT_ID,
             'NA' LITHOCODE,
             (SELECT GENDERID
                FROM GENDER_DIM
               WHERE GENDER_CODE = STUDENTS_GENDER) GENDERID,
             GRADEID,
             NULL EDU_CENTERID,
             NULL BARCODE,
             'NA' SPECIAL_CODES,
             'PP' STUDENT_MODE,
             ORG_NODEID,
             1000 CUSTOMERID,
             ADMINID,
             'N',
             SYSDATE,
             SYSDATE
        FROM RESULTS_GRT_FACT*\

           SELECT STUDENT_BIO_ID,
             STUDENT_FIRST_NAME,
             STUDENT_MIDDLE_INITIAL,
             STUDENT_LAST_NAME,
             BIRTH_DATE,
             STUDENTID,
             ORGTSTGPGM||'_'||STRUCTURE_LEVEL||'_'||ELEMENT_NUMBER AS INT_STUDENT_ID,
             0 AS EXT_STUDENT_ID,
             'NA' LITHOCODE,
             (SELECT GENDERID
                FROM GENDER_DIM
               WHERE GENDER_CODE = STUDENTS_GENDER) GENDERID,
             GRADEID,
             NULL EDU_CENTERID,
             BARCODE BARCODE,
             'NA' SPECIAL_CODES,
             'PP' STUDENT_MODE,
             ORG_NODEID,
             1000 CUSTOMERID,
             ADMINID,
             'N',
             NULL,
             NULL,
             SYSDATE,
             SYSDATE

        FROM RESULTS_GRT_FACT;
    COMMIT;
  END PROC_STU_BIO_DIM_POPULATE;

  PROCEDURE PROC_SUBTEST_SCORE_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT INTO subtest_score_fact
      SELECT subtest_fact_seq.nextval,
             a.org_nodeid,
             cust_prod_id,
             assessmentid,
             student_bio_id,
             contentid,
             subtestid,
             genderid,
             gradeid,
             levelid,
             formid,
             adminid,
             AAGE,
             AANCE,
             AANP,
             AANS,
             AASS,
             ACSIP,
             ACSIS,
             ACSIN,
             CSI,
             CSIL,
             CSIU,
             DIFF,
             GE,
             HSE,
             LEX,
             LEXL,
             LEXU,
             NCE,
             NCR,
             NP,
             NPA,
             NPG,
             NPL,
             NPH,
             NS,
             NSA,
             NSG,
             OM,
             OMS,
             OP,
             OPM,
             PC,
             PL,
             PP,
             PR,
             SEM,
             SNPC,
             SS,
             QTL,
             QTLL,
             QTLU,
             STATUS_CODE,
             to_date(TEST_DATE, 'mm/dd/RRRR'),
             SYSDATE
        FROM (SELECT org_nodeid,
                     cust_prod_id,
                     (SELECT a.assessmentid
                        FROM ASSESSMENT_DIM A, TEST_TP_MAP B
                       WHERE A.PRODUCTID = B.PRODUCTID
                         AND TP_CODE = b.ORGTSTGPGM) AS assessmentid,
                     student_bio_id,
                     b.contentid,
                     b.subtestid,
                     nvl((SELECT GENDERID
                           FROM GENDER_DIM
                          WHERE GENDER_CODE = STUDENTS_GENDER),
                         503) GENDERID,
                     b.gradeid,
                     levelid,
                     b.formid,
                     b.adminid,
                     NULL AAGE,
                     NULL AANCE,
                     NULL AANP,
                     NULL AANS,
                     NULL AASS,
                     NULL ACSIP,
                     NULL ACSIS,
                     NULL ACSIN,
                     NULL CSI,
                     NULL CSIL,
                     NULL CSIU,
                     NULL DIFF,
                     NULL GE,
                     NULL HSE,
                     NULL LEX,
                     NULL LEXL,
                     NULL LEXU,
                     NULL NCE,
                     CASE
                       WHEN ( b.subtest_code = 'ELA' OR b.subtest_code = 'READ' )  THEN
                        ENGLANG_ARTS_NUM_CORRECT
                       WHEN b.subtest_code = 'MATH' THEN
                        MATHEMATICS_NUM_CORRECT
                       WHEN b.subtest_code = 'SCI' THEN
                        SCIENCE_NUM_CORRECT
                       WHEN b.subtest_code = 'SS' THEN
                        SOCIAL_NUM_CORRECT
                     END AS NCR,
                     NULL NP,
                     NULL NPA,
                     NULL NPG,
                     NULL NPL,
                     NULL NPH,
                     NULL NS,
                     NULL NSA,
                     NULL NSG,
                     NULL OM,
                     NULL OMS,
                     NULL OP,
                     NULL OPM,
                     NULL PC,
                     CASE
                       WHEN ( b.subtest_code = 'ELA' OR b.subtest_code = 'READ' ) THEN
                        ELA_PF_INDICATOR
                       WHEN b.subtest_code = 'MATH' THEN
                        MATH_PF_INDICATOR
                       WHEN b.subtest_code = 'SCI' THEN
                        SCIENCE_PF_INDICATOR
                       WHEN b.subtest_code = 'SS' THEN
                        SOCIAL_PF_INDICATOR
                     END AS PL,
                     NULL PP,
                     NULL PR,
                     CASE
                       WHEN ( b.subtest_code = 'ELA' OR b.subtest_code = 'READ' ) THEN
                        ENGLAN_ARTS_SCALE_SCORE_SEM
                       WHEN b.subtest_code = 'MATH' THEN
                        MATHEMATICS_SCALE_SCORE_SEM
                       WHEN b.subtest_code = 'SCI' THEN
                        SCIENCE_SCALE_SCORE_SEM
                       WHEN b.subtest_code = 'SS' THEN
                        SOCIAL_SCALE_SCORE_SEM
                     END AS SEM,
                     NULL AS SNPC, --ENGLAN_ARTS_SCALE_SCORE,
                     CASE
                       WHEN ( b.subtest_code = 'ELA' OR b.subtest_code = 'READ' ) THEN
                        ENGLAN_ARTS_SCALE_SCORE
                       WHEN b.subtest_code = 'MATH' THEN
                        MATHEMATICS_SCALE_SCORE
                       WHEN b.subtest_code = 'SCI' THEN
                        SCIENCE_SCALE_SCORE
                       WHEN b.subtest_code = 'SS' THEN
                        SOCIAL_SCALE_SCORE
                     END AS SS,
                     NULL QTL,
                     NULL QTLL,
                     NULL QTLU,
                     CASE
                       WHEN ( b.subtest_code = 'ELA' OR b.subtest_code = 'READ' )  THEN
                        RESOLVED_REPORTING_STATUS_ELA
                       WHEN b.subtest_code = 'MATH' THEN
                        Resolved_Reporting_Status_Math
                       WHEN b.subtest_code = 'SCI' THEN
                        RESLVD_REPOR_STUS_SCNCE
                       WHEN b.subtest_code = 'SS' THEN
                        RESLVD_REPOR_STUS__SOCSTUD
                     END AS  STATUS_CODE,
                     TEST_DATE
                FROM (SELECT a.*,
                             b.subtestid,
                             b.subtest_code,
                             b.contentid,
                             b.formid
                        FROM RESULTS_GRT_FACT A,
                             cust_product_link c,
                             (SELECT DISTINCT GRADEID,
                                              SUBTESTID,
                                              SUBTEST_CODE,
                                              contentid,
                                              formid
                                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
                              --WHERE GRADEID = 10005
                               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) B
                       WHERE a.cust_prod_id = c.cust_prod_id
                         AND c.productid IN
                             (SELECT productid
                                FROM assessment_dim
                               WHERE assessmentid = IN_ASSESSMENT_ID)
                         AND A.GRADEID = B.GRADEID) b) a;

    COMMIT;
  END;

  PROCEDURE PROC_OBJECTIVE_SCORE_POPULATE(IN_ASSESSMENTID IN NUMBER) IS
  BEGIN
    INSERT INTO objective_score_fact
      SELECT subtest_fact_seq.nextval,
             ORG_NODEID,
             CUST_PROD_ID,
             IN_ASSESSMENTID,
             student_bio_id,
             CONTENTID,
             SUBTESTID,
             OBJECTIVEID,
             nvl((SELECT GENDERID
                   FROM GENDER_DIM
                  WHERE GENDER_CODE = STUDENTS_GENDER),
                 501) GENDERID,
             GRADEID,
             LEVELID,
             FORMID,
             adminid,
             NULL AS NCR,
             NULL AS OS,
             decode(REGEXP_SUBSTR(STR_OPI, '[0-9]+|[a-z]+|[A-Z]+', 1, RN),
                    'X',
                    0,
                    REGEXP_SUBSTR(STR_OPI, '[0-9]+|[a-z]+|[A-Z]+', 1, RN)) AS OPI,
             NULL AS OPIQ,
             NULL AS OPIP,
             NULL AS PC,
             NULL AS PP,
             NULL AS SS,
             SUBSTR(REPLACE(STR_MST, ',', NULL), RN, 1) AS PL,
             NULL AS INRC,
             NULL,
             TEST_DATE,
             SYSDATE

        FROM (SELECT student_bio_id,
                     org_nodeid,
                     a.adminid,
                     a.cust_prod_id,
                     B.RN,
                     B.CONTENTID,
                     B.SUBTESTID,
                     B.OBJECTIVEID,
                     B.GRADEID,
                     B.LEVELID,
                     B.FORMID,
                     STUDENTS_GENDER,
                     to_date(TEST_DATE, 'mm/dd/RRRR') AS TEST_DATE,
                     MASTERY_INDICATOR_1 || ',' || MASTERY_INDICATOR_2 || ',' ||
                     MASTERY_INDICATOR_3 || ',' || MASTERY_INDICATOR_4 || ',' ||
                     MASTERY_INDICATOR_5 || ',' || MASTERY_INDICATOR_6 || ',' ||
                     MASTERY_INDICATOR_7 || ',' || MASTERY_INDICATOR_8 || ',' ||
                     MASTERY_INDICATOR_9 || ',' || MASTERY_INDICATOR_10 || ',' ||
                     MASTERY_INDICATOR_11 || ',' || MASTERY_INDICATOR_12 || ',' ||
                     MASTERY_INDICATOR_13 || ',' || MASTERY_INDICATOR_14 || ',' ||
                     MASTERY_INDICATOR_15 || ',' || MASTERY_INDICATOR_16 || ',' ||
                     MASTERY_INDICATOR_17 || ',' || MASTERY_INDICATOR_18 || ',' ||
                     MASTERY_INDICATOR_19 || ',' || MASTERY_INDICATOR_20 || ',' ||
                     MASTERY_INDICATOR_21 || ',' || MASTERY_INDICATOR_22 || ',' ||
                     MASTERY_INDICATOR_23 || ',' || MASTERY_INDICATOR_24 || ',' ||
                     MASTERY_INDICATOR_25 || ',' || MASTERY_INDICATOR_26 || ',' ||
                     MASTERY_INDICATOR_27 || ',' || MASTERY_INDICATOR_28 || ',' ||
                     MASTERY_INDICATOR_29 || ',' || MASTERY_INDICATOR_30 || ',' ||
                     MASTERY_INDICATOR_31 || ',' || MASTERY_INDICATOR_32 || ',' ||
                     MASTERY_INDICATOR_33 || ',' || MASTERY_INDICATOR_34 || ',' ||
                     MASTERY_INDICATOR_35 AS STR_MST,

                     OPIIPI_1 || ',' || OPIIPI_2 || ',' || OPIIPI_3 || ',' ||
                     OPIIPI_4 || ',' || OPIIPI_5 || ',' || OPIIPI_6 || ',' ||
                     OPIIPI_7 || ',' || OPIIPI_8 || ',' || OPIIPI_9 || ',' ||
                     OPIIPI_10 || ',' || OPIIPI_11 || ',' || OPIIPI_12 || ',' ||
                     OPIIPI_13 || ',' || OPIIPI_14 || ',' || OPIIPI_15 || ',' ||
                     OPIIPI_16 || ',' || OPIIPI_17 || ',' || OPIIPI_18 || ',' ||
                     OPIIPI_19 || ',' || OPIIPI_20 || ',' || OPIIPI_21 || ',' ||
                     OPIIPI_22 || ',' || OPIIPI_23 || ',' || OPIIPI_24 || ',' ||
                     OPIIPI_25 || ',' || OPIIPI_26 || ',' || OPIIPI_27 || ',' ||
                     OPIIPI_28 || ',' || OPIIPI_29 || ',' || OPIIPI_30 || ',' ||
                     OPIIPI_31 || ',' || OPIIPI_32 || ',' || OPIIPI_33 || ',' ||
                     OPIIPI_34 || ',' || OPIIPI_35 AS STR_OPI

                FROM results_grt_fact               A,
                     cust_product_link              c,
                     VW_SUBTEST_GRADE_OBJECTIVE_MAP B
               WHERE b.assessmentid = IN_ASSESSMENTID
                 AND c.productid IN
                     (SELECT productid
                        FROM assessment_dim
                       WHERE assessmentid = IN_ASSESSMENTID)
                 AND a.cust_prod_id = c.cust_prod_id
                 AND A.GRADEID = B.GRADEID);
    COMMIT;
  END;

  PROCEDURE PROC_CLASS_SUMM_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    \* INSERT \*+APPEND*\ INTO CLASS_SUMM_FACT
    (CLASS_SUMM_ID,
     ORG_NODEID,
     CUST_PROD_ID,
     ADMINID,
     GRADEID,
     LEVELID,
     TEST_DATE,
     ELA_STANDARD_DEVIATION,
     ELA_HIGH_SCALE_SCORE_OBTAINED,
     ELA_LOW_SCALE_SCORE_OBTAINED,
     ELA_NUM_STUDENT_PASSP,
     ELA_PERC_STUDENT_PASSP,
     ELA_NUM_STUDENT_PASS,
     ELA_PERC_STUDENT_PASS,
     ELA_NUM_STUDENT_DNP,
     ELA_PERC_STUDENT_DNP,
     ELA_NUM_STUDENT_UNDETERMINED,
     ELA_PERC_STUDENT_UNDETERMINED,
     ELA_NO_STUDENTS_LISTED,
     ELA_MEAN_SCALE_SCORE,
     ELA_NUM_MASTERY1,
     ELA_NUM_MASTERY2,
     ELA_NUM_MASTERY3,
     ELA_NUM_MASTERY4,
     ELA_NUM_MASTERY5,
     ELA_NUM_MASTERY6,
     ELA_NUM_MASTERY7,
     ELA_NUM_MASTERY8,
     ELA_NUM_MASTERY9,
     ELA_NUM_MASTERY10,
     ELA_NUM_MASTERY11,
     ELA_NUM_MASTERY12,
     ELA_PERC_MASTERY1,
     ELA_PERC_MASTERY2,
     ELA_PERC_MASTERY3,
     ELA_PERC_MASTERY4,
     ELA_PERC_MASTERY5,
     ELA_PERC_MASTERY6,
     ELA_PERC_MASTERY7,
     ELA_PERC_MASTERY8,
     ELA_PERC_MASTERY9,
     ELA_PERC_MASTERY10,
     ELA_PERC_MASTERY11,
     ELA_PERC_MASTERY12,
     ELA_LOW_SCALE_POSSIBLE,
     ELA_HIGH_SCALE_POSSIBLE,
     ELA_UND_SCALE_SCORE_RANGE,
     ELA_PASS_SCALE_SCORE_RANGE,
     ELA_PASSP_SCALE_SCORE_RANGE,
     MATH_STANDARD_DEVIATION,
     MATH_HIGH_SCALE_SCORE_OBTAINED,
     MATH_LOW_SCALE_SCORE_OBTAINED,
     MATH_NUM_STUDENT_PASSP,
     MATH_PERC_STUDENT_PASSP,
     MATH_NUM_STUDENT_PASS,
     MATH_PERC_STUDENT_PASS,
     MATH_NUM_STUDENT_DNP,
     MATH_PERC_STUDENT_DNP,
     MATH_NUM_STUDENT_UNDETERMINED,
     MATH_PERC_STUDENT_UNDETERMINED,
     MATH_NO_STUDENT_LISTED,
     MATH_MEAN_SCALE_SCORE,
     MATH_NUM_MASTERY1,
     MATH_NUM_MASTERY2,
     MATH_NUM_MASTERY3,
     MATH_NUM_MASTERY4,
     MATH_NUM_MASTERY5,
     MATH_NUM_MASTERY6,
     MATH_NUM_MASTERY7,
     MATH_NUM_MASTERY8,
     MATH_NUM_MASTERY9,
     MATH_NUM_MASTERY10,
     MATH_NUM_MASTERY11,
     MATH_NUM_MASTERY12,
     MATH_PERC_MASTERY1,
     MATH_PERC_MASTERY2,
     MATH_PERC_MASTERY3,
     MATH_PERC_MASTERY4,
     MATH_PERC_MASTERY5,
     MATH_PERC_MASTERY6,
     MATH_PERC_MASTERY7,
     MATH_PERC_MASTERY8,
     MATH_PERC_MASTERY9,
     MATH_PERC_MASTERY10,
     MATH_PERC_MASTERY11,
     MATH_PERC_MASTERY12,
     MATH_LOW_SCALE_POSSIBLE,
     MATH_HIGH_SCALE_POSSIBLE,
     MATH_UND_SCALE_SCORE_RANGE,
     MATH_PASS_SCALE_SCORE_RANGE,
     MATH_PASSP_SCALE_SCORE_RANGE,
     SCI_STANDARD_DEVIATION,
     SCI_HIGH_SCALE_SCORE_OBTAINED,
     SCI_LOW_SCALE_SCORE_OBTAINED,
     SCI_NUM_STUDENT_PASSP,
     SCI_PERC_STUDENT_PASSP,
     SCI_NUM_STUDENT_PASS,
     SCI_PERC_STUDENT_PASS,
     SCI_NUM_STUDENT_DNP,
     SCI_PERC_STUDENT_DNP,
     SCI_NUM_STUDENT_UNDETERMINED,
     SCI_PERC_STUDENT_UNDETERMINED,
     SCI_NO_STUDENT_LISTED,
     SCI_MEAN_SCALE_SCORE,
     SCI_NUM_MASTERY1,
     SCI_NUM_MASTERY2,
     SCI_NUM_MASTERY3,
     SCI_NUM_MASTERY4,
     SCI_NUM_MASTERY5,
     SCI_NUM_MASTERY6,
     SCI_NUM_MASTERY7,
     SCI_NUM_MASTERY8,
     SCI_NUM_MASTERY9,
     SCI_NUM_MASTERY10,
     SCI_NUM_MASTERY11,
     SCI_NUM_MASTERY12,
     SCI_PERC_MASTERY1,
     SCI_PERC_MASTERY2,
     SCI_PERC_MASTERY3,
     SCI_PERC_MASTERY4,
     SCI_PERC_MASTERY5,
     SCI_PERC_MASTERY6,
     SCI_PERC_MASTERY7,
     SCI_PERC_MASTERY8,
     SCI_PERC_MASTERY9,
     SCI_PERC_MASTERY10,
     SCI_PERC_MASTERY11,
     SCI_PERC_MASTERY12,
     SCI_LOW_SCALE_POSSIBLE,
     SCI_HIGH_SCALE_POSSIBLE,
     SCI_UND_SCALE_SCORE_RANGE,
     SCI_PASS_SCALE_SCORE_RANGE,
     SCI_PASSP_SCALE_SCORE_RANGE,
     SOC_STANDARD_DEVIATION,
     SOC_HIGH_SCALE_SCORE_OBTAINED,
     SOC_LOW_SCALE_SCORE_OBTAINED,
     SOC_NUM_STUDENT_PASSP,
     SOC_PERC_STUDENT_PASSP,
     SOC_NUM_STUDENT_PASS,
     SOC_PERC_STUDENT_PASS,
     SOC_NUM_STUDENT_DNP,
     SOC_PERC_STUDENT_DNP,
     SOC_NUM_STUDENT_UNDETERMINED,
     SOC_PERC_STUDENT_UNDETERMINED,
     SOC_NO_STUDENT_LISTED,
     SOC_MEAN_SCALE_SCORE,
     SOC_NUM_MASTERY1,
     SOC_NUM_MASTERY2,
     SOC_NUM_MASTERY3,
     SOC_NUM_MASTERY4,
     SOC_NUM_MASTERY5,
     SOC_NUM_MASTERY6,
     SOC_NUM_MASTERY7,
     SOC_NUM_MASTERY8,
     SOC_NUM_MASTERY9,
     SOC_NUM_MASTERY10,
     SOC_NUM_MASTERY11,
     SOC_NUM_MASTERY12,
     SOC_PERC_MASTERY1,
     SOC_PERC_MASTERY2,
     SOC_PERC_MASTERY3,
     SOC_PERC_MASTERY4,
     SOC_PERC_MASTERY5,
     SOC_PERC_MASTERY6,
     SOC_PERC_MASTERY7,
     SOC_PERC_MASTERY8,
     SOC_PERC_MASTERY9,
     SOC_PERC_MASTERY10,
     SOC_PERC_MASTERY11,
     SOC_PERC_MASTERY12,
     SOC_LOW_SCALE_POSSIBLE,
     SOC_HIGH_SCALE_POSSIBLE,
     SOC_UND_SCALE_SCORE_RANGE,
     SOC_PASS_SCALE_SCORE_RANGE,
     SOC_PASSP_SCALE_SCORE_RANGE,
     TEACHER_ELEMENT_NUMBER,
     STRUCTURE_LEVEL,
     DATETIMESTAMP)

    SELECT SUBTEST_FACT_SEQ.NEXTVAL,
           (SELECT ORG_NODEID
              FROM ORG_NODE_DIM
             WHERE ORG_NODE_LEVEL = 4
               AND ORG_NODE_CODE =
                   ORG_TEST_PROGRAM || TEACHER_ELEMENT_NUMBER),
           (SELECT CUST_PROD_ID
              FROM CUST_PRODUCT_LINK A
             WHERE A.PRODUCTID = TTM.PRODUCTID
               AND A.ADMINID = TTM.ADMINID
               AND TP_CODE = ORG_TEST_PROGRAM) AS CUST_PROD_ID,
           (SELECT ADMINID
              FROM TEST_TP_MAP
             WHERE TESTID = A.TESTID
               AND TP_CODE = ORG_TEST_PROGRAM) ADMINID,

           --   d.contentid ,
           --   d.subtestid,
           D.GRADEID,
           D.LEVELID,
           TEST_DATE,
           ELA_STANDARD_DEVIATION,
           ELA_HIGH_SCALE_SCORE_OBTAINED,
           ELA_LOW_SCALE_SCORE_OBTAINED,
           ELA_NUM_STUDENT_PASSP,
           ELA_PERC_STUDENT_PASSP,
           ELA_NUM_STUDENT_PASS,
           ELA_PERC_STUDENT_PASS,
           ELA_NUM_STUDENT_DNP,
           ELA_PERC_STUDENT_DNP,
           ELA_NUM_STUDENT_UNDETERMINED,
           ELA_PERC_STUDENT_UNDETERMINED,
           ELA_NO_STUDENTS_LISTED,
           ELA_MEAN_SCALE_SCORE,
           ELA_NUM_MASTERY1,
           ELA_NUM_MASTERY2,
           ELA_NUM_MASTERY3,
           ELA_NUM_MASTERY4,
           ELA_NUM_MASTERY5,
           ELA_NUM_MASTERY6,
           ELA_NUM_MASTERY7,
           ELA_NUM_MASTERY8,
           ELA_NUM_MASTERY9,
           ELA_NUM_MASTERY10,
           ELA_NUM_MASTERY11,
           ELA_NUM_MASTERY12,
           ELA_PERC_MASTERY1,
           ELA_PERC_MASTERY2,
           ELA_PERC_MASTERY3,
           ELA_PERC_MASTERY4,
           ELA_PERC_MASTERY5,
           ELA_PERC_MASTERY6,
           ELA_PERC_MASTERY7,
           ELA_PERC_MASTERY8,
           ELA_PERC_MASTERY9,
           ELA_PERC_MASTERY10,
           ELA_PERC_MASTERY11,
           ELA_PERC_MASTERY12,
           ELA_LOW_SCALE_POSSIBLE,
           ELA_HIGH_SCALE_POSSIBLE,
           ELA_UND_SCALE_SCORE_RANGE,
           ELA_PASS_SCALE_SCORE_RANGE,
           ELA_PASSP_SCALE_SCORE_RANGE,
           MATH_STANDARD_DEVIATION,
           MATH_HIGH_SCALE_SCORE_OBTAINED,
           MATH_LOW_SCALE_SCORE_OBTAINED,
           MATH_NUM_STUDENT_PASSP,
           MATH_PERC_STUDENT_PASSP,
           MATH_NUM_STUDENT_PASS,
           MATH_PERC_STUDENT_PASS,
           MATH_NUM_STUDENT_DNP,
           MATH_PERC_STUDENT_DNP,
           MATH_NUM_STUDENT_UNDETERMINED,
           MATH_PERC_STUDENT_UNDETERMINED,
           MATH_NO_STUDENT_LISTED,
           MATH_MEAN_SCALE_SCORE,
           MATH_NUM_MASTERY1,
           MATH_NUM_MASTERY2,
           MATH_NUM_MASTERY3,
           MATH_NUM_MASTERY4,
           MATH_NUM_MASTERY5,
           MATH_NUM_MASTERY6,
           MATH_NUM_MASTERY7,
           MATH_NUM_MASTERY8,
           MATH_NUM_MASTERY9,
           MATH_NUM_MASTERY10,
           MATH_NUM_MASTERY11,
           MATH_NUM_MASTERY12,
           MATH_PERC_MASTERY1,
           MATH_PERC_MASTERY2,
           MATH_PERC_MASTERY3,
           MATH_PERC_MASTERY4,
           MATH_PERC_MASTERY5,
           MATH_PERC_MASTERY6,
           MATH_PERC_MASTERY7,
           MATH_PERC_MASTERY8,
           MATH_PERC_MASTERY9,
           MATH_PERC_MASTERY10,
           MATH_PERC_MASTERY11,
           MATH_PERC_MASTERY12,
           MATH_LOW_SCALE_POSSIBLE,
           MATH_HIGH_SCALE_POSSIBLE,
           MATH_UND_SCALE_SCORE_RANGE,
           MATH_PASS_SCALE_SCORE_RANGE,
           MATH_PASSP_SCALE_SCORE_RANGE,
           SCI_STANDARD_DEVIATION,
           SCI_HIGH_SCALE_SCORE_OBTAINED,
           SCI_LOW_SCALE_SCORE_OBTAINED,
           SCI_NUM_STUDENT_PASSP,
           SCI_PERC_STUDENT_PASSP,
           SCI_NUM_STUDENT_PASS,
           SCI_PERC_STUDENT_PASS,
           SCI_NUM_STUDENT_DNP,
           SCI_PERC_STUDENT_DNP,
           SCI_NUM_STUDENT_UNDETERMINED,
           SCI_PERC_STUDENT_UNDETERMINED,
           SCI_NO_STUDENT_LISTED,
           SCI_MEAN_SCALE_SCORE,
           SCI_NUM_MASTERY1,
           SCI_NUM_MASTERY2,
           SCI_NUM_MASTERY3,
           SCI_NUM_MASTERY4,
           SCI_NUM_MASTERY5,
           SCI_NUM_MASTERY6,
           SCI_NUM_MASTERY7,
           SCI_NUM_MASTERY8,
           SCI_NUM_MASTERY9,
           SCI_NUM_MASTERY10,
           SCI_NUM_MASTERY11,
           SCI_NUM_MASTERY12,
           SCI_PERC_MASTERY1,
           SCI_PERC_MASTERY2,
           SCI_PERC_MASTERY3,
           SCI_PERC_MASTERY4,
           SCI_PERC_MASTERY5,
           SCI_PERC_MASTERY6,
           SCI_PERC_MASTERY7,
           SCI_PERC_MASTERY8,
           SCI_PERC_MASTERY9,
           SCI_PERC_MASTERY10,
           SCI_PERC_MASTERY11,
           SCI_PERC_MASTERY12,
           SCI_LOW_SCALE_POSSIBLE,
           SCI_HIGH_SCALE_POSSIBLE,
           SCI_UND_SCALE_SCORE_RANGE,
           SCI_PASS_SCALE_SCORE_RANGE,
           SCI_PASSP_SCALE_SCORE_RANGE,
           SOC_STANDARD_DEVIATION,
           SOC_HIGH_SCALE_SCORE_OBTAINED,
           SOC_LOW_SCALE_SCORE_OBTAINED,
           SOC_NUM_STUDENT_PASSP,
           SOC_PERC_STUDENT_PASSP,
           SOC_NUM_STUDENT_PASS,
           SOC_PERC_STUDENT_PASS,
           SOC_NUM_STUDENT_DNP,
           SOC_PERC_STUDENT_DNP,
           SOC_NUM_STUDENT_UNDETERMINED,
           SOC_PERC_STUDENT_UNDETERMINED,
           SOC_NO_STUDENT_LISTED,
           SOC_MEAN_SCALE_SCORE,
           SOC_NUM_MASTERY1,
           SOC_NUM_MASTERY2,
           SOC_NUM_MASTERY3,
           SOC_NUM_MASTERY4,
           SOC_NUM_MASTERY5,
           SOC_NUM_MASTERY6,
           SOC_NUM_MASTERY7,
           SOC_NUM_MASTERY8,
           SOC_NUM_MASTERY9,
           SOC_NUM_MASTERY10,
           SOC_NUM_MASTERY11,
           SOC_NUM_MASTERY12,
           SOC_PERC_MASTERY1,
           SOC_PERC_MASTERY2,
           SOC_PERC_MASTERY3,
           SOC_PERC_MASTERY4,
           SOC_PERC_MASTERY5,
           SOC_PERC_MASTERY6,
           SOC_PERC_MASTERY7,
           SOC_PERC_MASTERY8,
           SOC_PERC_MASTERY9,
           SOC_PERC_MASTERY10,
           SOC_PERC_MASTERY11,
           SOC_PERC_MASTERY12,
           SOC_LOW_SCALE_POSSIBLE,
           SOC_HIGH_SCALE_POSSIBLE,
           SOC_UND_SCALE_SCORE_RANGE,
           SOC_PASS_SCALE_SCORE_RANGE,
           SOC_PASSP_SCALE_SCORE_RANGE,
           TEACHER_ELEMENT_NUMBER,
           STRUCTURE_LEVEL,
           SYSDATE
      FROM ISTEP_DATAMIG.MIG_RESULTS_CLASSSUMMARY A,
           TEST_TP_MAP TTM,
           (SELECT DISTINCT ASSESSMENTID,
                            --subtestid,subtest_name,
                            GRADE_CODE,
                            GRADEID,
                            LEVELID
              FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP) D
     WHERE A.ORG_TEST_PROGRAM = TTM.TP_CODE
       AND A.TESTID = TTM.TESTID
       AND D.ASSESSMENTID = TTM.ASSESSMENTID
       AND A.GRADE = D.GRADE_CODE ; *\

    INSERT \*+APPEND*\
    INTO CLASS_SUMM_FACT
      (CLASS_SUMM_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       ENGLANG_ARTS_SUBTESTID,
       MATHEMATICS_SUBTESTID,
       SCIENCE_SUBTESTID,
       SOCIAL_SUBTESTID,
       GRADEID,
       LEVELID,
       TEST_DATE,
       ELA_STANDARD_DEVIATION,
       ELA_HIGH_SCALE_SCORE_OBTAINED,
       ELA_LOW_SCALE_SCORE_OBTAINED,
       ELA_NUM_STUDENT_PASSP,
       ELA_PERC_STUDENT_PASSP,
       ELA_NUM_STUDENT_PASS,
       ELA_PERC_STUDENT_PASS,
       ELA_NUM_STUDENT_DNP,
       ELA_PERC_STUDENT_DNP,
       ELA_NUM_STUDENT_UNDETERMINED,
       ELA_PERC_STUDENT_UNDETERMINED,
       ELA_NO_STUDENTS_LISTED,
       ELA_MEAN_SCALE_SCORE,
       ELA_NUM_MASTERY1,
       ELA_NUM_MASTERY2,
       ELA_NUM_MASTERY3,
       ELA_NUM_MASTERY4,
       ELA_NUM_MASTERY5,
       ELA_NUM_MASTERY6,
       ELA_NUM_MASTERY7,
       ELA_NUM_MASTERY8,
       ELA_NUM_MASTERY9,
       ELA_NUM_MASTERY10,
       ELA_NUM_MASTERY11,
       ELA_NUM_MASTERY12,
       ELA_PERC_MASTERY1,
       ELA_PERC_MASTERY2,
       ELA_PERC_MASTERY3,
       ELA_PERC_MASTERY4,
       ELA_PERC_MASTERY5,
       ELA_PERC_MASTERY6,
       ELA_PERC_MASTERY7,
       ELA_PERC_MASTERY8,
       ELA_PERC_MASTERY9,
       ELA_PERC_MASTERY10,
       ELA_PERC_MASTERY11,
       ELA_PERC_MASTERY12,
       ELA_LOW_SCALE_POSSIBLE,
       ELA_HIGH_SCALE_POSSIBLE,
       ELA_UND_SCALE_SCORE_RANGE,
       ELA_PASS_SCALE_SCORE_RANGE,
       ELA_PASSP_SCALE_SCORE_RANGE,
       MATH_STANDARD_DEVIATION,
       MATH_HIGH_SCALE_SCORE_OBTAINED,
       MATH_LOW_SCALE_SCORE_OBTAINED,
       MATH_NUM_STUDENT_PASSP,
       MATH_PERC_STUDENT_PASSP,
       MATH_NUM_STUDENT_PASS,
       MATH_PERC_STUDENT_PASS,
       MATH_NUM_STUDENT_DNP,
       MATH_PERC_STUDENT_DNP,
       MATH_NUM_STUDENT_UNDETERMINED,
       MATH_PERC_STUDENT_UNDETERMINED,
       MATH_NO_STUDENT_LISTED,
       MATH_MEAN_SCALE_SCORE,
       MATH_NUM_MASTERY1,
       MATH_NUM_MASTERY2,
       MATH_NUM_MASTERY3,
       MATH_NUM_MASTERY4,
       MATH_NUM_MASTERY5,
       MATH_NUM_MASTERY6,
       MATH_NUM_MASTERY7,
       MATH_NUM_MASTERY8,
       MATH_NUM_MASTERY9,
       MATH_NUM_MASTERY10,
       MATH_NUM_MASTERY11,
       MATH_NUM_MASTERY12,
       MATH_PERC_MASTERY1,
       MATH_PERC_MASTERY2,
       MATH_PERC_MASTERY3,
       MATH_PERC_MASTERY4,
       MATH_PERC_MASTERY5,
       MATH_PERC_MASTERY6,
       MATH_PERC_MASTERY7,
       MATH_PERC_MASTERY8,
       MATH_PERC_MASTERY9,
       MATH_PERC_MASTERY10,
       MATH_PERC_MASTERY11,
       MATH_PERC_MASTERY12,
       MATH_LOW_SCALE_POSSIBLE,
       MATH_HIGH_SCALE_POSSIBLE,
       MATH_UND_SCALE_SCORE_RANGE,
       MATH_PASS_SCALE_SCORE_RANGE,
       MATH_PASSP_SCALE_SCORE_RANGE,
       SCI_STANDARD_DEVIATION,
       SCI_HIGH_SCALE_SCORE_OBTAINED,
       SCI_LOW_SCALE_SCORE_OBTAINED,
       SCI_NUM_STUDENT_PASSP,
       SCI_PERC_STUDENT_PASSP,
       SCI_NUM_STUDENT_PASS,
       SCI_PERC_STUDENT_PASS,
       SCI_NUM_STUDENT_DNP,
       SCI_PERC_STUDENT_DNP,
       SCI_NUM_STUDENT_UNDETERMINED,
       SCI_PERC_STUDENT_UNDETERMINED,
       SCI_NO_STUDENT_LISTED,
       SCI_MEAN_SCALE_SCORE,
       SCI_NUM_MASTERY1,
       SCI_NUM_MASTERY2,
       SCI_NUM_MASTERY3,
       SCI_NUM_MASTERY4,
       SCI_NUM_MASTERY5,
       SCI_NUM_MASTERY6,
       SCI_NUM_MASTERY7,
       SCI_NUM_MASTERY8,
       SCI_NUM_MASTERY9,
       SCI_NUM_MASTERY10,
       SCI_NUM_MASTERY11,
       SCI_NUM_MASTERY12,
       SCI_PERC_MASTERY1,
       SCI_PERC_MASTERY2,
       SCI_PERC_MASTERY3,
       SCI_PERC_MASTERY4,
       SCI_PERC_MASTERY5,
       SCI_PERC_MASTERY6,
       SCI_PERC_MASTERY7,
       SCI_PERC_MASTERY8,
       SCI_PERC_MASTERY9,
       SCI_PERC_MASTERY10,
       SCI_PERC_MASTERY11,
       SCI_PERC_MASTERY12,
       SCI_LOW_SCALE_POSSIBLE,
       SCI_HIGH_SCALE_POSSIBLE,
       SCI_UND_SCALE_SCORE_RANGE,
       SCI_PASS_SCALE_SCORE_RANGE,
       SCI_PASSP_SCALE_SCORE_RANGE,
       SOC_STANDARD_DEVIATION,
       SOC_HIGH_SCALE_SCORE_OBTAINED,
       SOC_LOW_SCALE_SCORE_OBTAINED,
       SOC_NUM_STUDENT_PASSP,
       SOC_PERC_STUDENT_PASSP,
       SOC_NUM_STUDENT_PASS,
       SOC_PERC_STUDENT_PASS,
       SOC_NUM_STUDENT_DNP,
       SOC_PERC_STUDENT_DNP,
       SOC_NUM_STUDENT_UNDETERMINED,
       SOC_PERC_STUDENT_UNDETERMINED,
       SOC_NO_STUDENT_LISTED,
       SOC_MEAN_SCALE_SCORE,
       SOC_NUM_MASTERY1,
       SOC_NUM_MASTERY2,
       SOC_NUM_MASTERY3,
       SOC_NUM_MASTERY4,
       SOC_NUM_MASTERY5,
       SOC_NUM_MASTERY6,
       SOC_NUM_MASTERY7,
       SOC_NUM_MASTERY8,
       SOC_NUM_MASTERY9,
       SOC_NUM_MASTERY10,
       SOC_NUM_MASTERY11,
       SOC_NUM_MASTERY12,
       SOC_PERC_MASTERY1,
       SOC_PERC_MASTERY2,
       SOC_PERC_MASTERY3,
       SOC_PERC_MASTERY4,
       SOC_PERC_MASTERY5,
       SOC_PERC_MASTERY6,
       SOC_PERC_MASTERY7,
       SOC_PERC_MASTERY8,
       SOC_PERC_MASTERY9,
       SOC_PERC_MASTERY10,
       SOC_PERC_MASTERY11,
       SOC_PERC_MASTERY12,
       SOC_LOW_SCALE_POSSIBLE,
       SOC_HIGH_SCALE_POSSIBLE,
       SOC_UND_SCALE_SCORE_RANGE,
       SOC_PASS_SCALE_SCORE_RANGE,
       SOC_PASSP_SCALE_SCORE_RANGE,
       TEACHER_ELEMENT_NUMBER,
       STRUCTURE_LEVEL,
       ISPUBLIC,
       DATETIMESTAMP)

      SELECT RESULTS_CLASSSUMMARYID,
             (SELECT ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 4
                 AND ORG_NODE_CODE =
                     ORG_TEST_PROGRAM || TEACHER_ELEMENT_NUMBER),
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID
                 AND TP_CODE = ORG_TEST_PROGRAM) AS CUST_PROD_ID,
             (SELECT ADMINID
                FROM TEST_TP_MAP
               WHERE TESTID = A.TESTID
                 AND TP_CODE = ORG_TEST_PROGRAM) ADMINID,

             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = A.TESTID
                 AND T.TP_CODE = A.ORG_TEST_PROGRAM
                 AND SUBTEST_CODE = 'ELA') ENGLANG_ARTS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = A.TESTID
                 AND T.TP_CODE = A.ORG_TEST_PROGRAM
                 AND M.SUBTEST_CODE = 'MATH') MATHEMATICS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = A.TESTID
                 AND T.TP_CODE = A.ORG_TEST_PROGRAM
                 AND M.SUBTEST_CODE = 'SCI') SCIENCE_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = A.TESTID
                 AND T.TP_CODE = A.ORG_TEST_PROGRAM
                 AND M.SUBTEST_CODE = 'SS') SOCIAL_SUBTESTID,
             D.GRADEID,
             D.LEVELID,
             TEST_DATE,
             ELA_STANDARD_DEVIATION,
             ELA_HIGH_SCALE_SCORE_OBTAINED,
             ELA_LOW_SCALE_SCORE_OBTAINED,
             ELA_NUM_STUDENT_PASSP,
             ELA_PERC_STUDENT_PASSP,
             ELA_NUM_STUDENT_PASS,
             ELA_PERC_STUDENT_PASS,
             ELA_NUM_STUDENT_DNP,
             ELA_PERC_STUDENT_DNP,
             ELA_NUM_STUDENT_UNDETERMINED,
             ELA_PERC_STUDENT_UNDETERMINED,
             ELA_NO_STUDENTS_LISTED,
             ELA_MEAN_SCALE_SCORE,
             ELA_NUM_MASTERY1,
             ELA_NUM_MASTERY2,
             ELA_NUM_MASTERY3,
             ELA_NUM_MASTERY4,
             ELA_NUM_MASTERY5,
             ELA_NUM_MASTERY6,
             ELA_NUM_MASTERY7,
             ELA_NUM_MASTERY8,
             ELA_NUM_MASTERY9,
             ELA_NUM_MASTERY10,
             ELA_NUM_MASTERY11,
             ELA_NUM_MASTERY12,
             ELA_PERC_MASTERY1,
             ELA_PERC_MASTERY2,
             ELA_PERC_MASTERY3,
             ELA_PERC_MASTERY4,
             ELA_PERC_MASTERY5,
             ELA_PERC_MASTERY6,
             ELA_PERC_MASTERY7,
             ELA_PERC_MASTERY8,
             ELA_PERC_MASTERY9,
             ELA_PERC_MASTERY10,
             ELA_PERC_MASTERY11,
             ELA_PERC_MASTERY12,
             ELA_LOW_SCALE_POSSIBLE,
             ELA_HIGH_SCALE_POSSIBLE,
             ELA_UND_SCALE_SCORE_RANGE,
             ELA_PASS_SCALE_SCORE_RANGE,
             ELA_PASSP_SCALE_SCORE_RANGE,
             MATH_STANDARD_DEVIATION,
             MATH_HIGH_SCALE_SCORE_OBTAINED,
             MATH_LOW_SCALE_SCORE_OBTAINED,
             MATH_NUM_STUDENT_PASSP,
             MATH_PERC_STUDENT_PASSP,
             MATH_NUM_STUDENT_PASS,
             MATH_PERC_STUDENT_PASS,
             MATH_NUM_STUDENT_DNP,
             MATH_PERC_STUDENT_DNP,
             MATH_NUM_STUDENT_UNDETERMINED,
             MATH_PERC_STUDENT_UNDETERMINED,
             MATH_NO_STUDENT_LISTED,
             MATH_MEAN_SCALE_SCORE,
             MATH_NUM_MASTERY1,
             MATH_NUM_MASTERY2,
             MATH_NUM_MASTERY3,
             MATH_NUM_MASTERY4,
             MATH_NUM_MASTERY5,
             MATH_NUM_MASTERY6,
             MATH_NUM_MASTERY7,
             MATH_NUM_MASTERY8,
             MATH_NUM_MASTERY9,
             MATH_NUM_MASTERY10,
             MATH_NUM_MASTERY11,
             MATH_NUM_MASTERY12,
             MATH_PERC_MASTERY1,
             MATH_PERC_MASTERY2,
             MATH_PERC_MASTERY3,
             MATH_PERC_MASTERY4,
             MATH_PERC_MASTERY5,
             MATH_PERC_MASTERY6,
             MATH_PERC_MASTERY7,
             MATH_PERC_MASTERY8,
             MATH_PERC_MASTERY9,
             MATH_PERC_MASTERY10,
             MATH_PERC_MASTERY11,
             MATH_PERC_MASTERY12,
             MATH_LOW_SCALE_POSSIBLE,
             MATH_HIGH_SCALE_POSSIBLE,
             MATH_UND_SCALE_SCORE_RANGE,
             MATH_PASS_SCALE_SCORE_RANGE,
             MATH_PASSP_SCALE_SCORE_RANGE,
             SCI_STANDARD_DEVIATION,
             SCI_HIGH_SCALE_SCORE_OBTAINED,
             SCI_LOW_SCALE_SCORE_OBTAINED,
             SCI_NUM_STUDENT_PASSP,
             SCI_PERC_STUDENT_PASSP,
             SCI_NUM_STUDENT_PASS,
             SCI_PERC_STUDENT_PASS,
             SCI_NUM_STUDENT_DNP,
             SCI_PERC_STUDENT_DNP,
             SCI_NUM_STUDENT_UNDETERMINED,
             SCI_PERC_STUDENT_UNDETERMINED,
             SCI_NO_STUDENT_LISTED,
             SCI_MEAN_SCALE_SCORE,
             SCI_NUM_MASTERY1,
             SCI_NUM_MASTERY2,
             SCI_NUM_MASTERY3,
             SCI_NUM_MASTERY4,
             SCI_NUM_MASTERY5,
             SCI_NUM_MASTERY6,
             SCI_NUM_MASTERY7,
             SCI_NUM_MASTERY8,
             SCI_NUM_MASTERY9,
             SCI_NUM_MASTERY10,
             SCI_NUM_MASTERY11,
             SCI_NUM_MASTERY12,
             SCI_PERC_MASTERY1,
             SCI_PERC_MASTERY2,
             SCI_PERC_MASTERY3,
             SCI_PERC_MASTERY4,
             SCI_PERC_MASTERY5,
             SCI_PERC_MASTERY6,
             SCI_PERC_MASTERY7,
             SCI_PERC_MASTERY8,
             SCI_PERC_MASTERY9,
             SCI_PERC_MASTERY10,
             SCI_PERC_MASTERY11,
             SCI_PERC_MASTERY12,
             SCI_LOW_SCALE_POSSIBLE,
             SCI_HIGH_SCALE_POSSIBLE,
             SCI_UND_SCALE_SCORE_RANGE,
             SCI_PASS_SCALE_SCORE_RANGE,
             SCI_PASSP_SCALE_SCORE_RANGE,
             SOC_STANDARD_DEVIATION,
             SOC_HIGH_SCALE_SCORE_OBTAINED,
             SOC_LOW_SCALE_SCORE_OBTAINED,
             SOC_NUM_STUDENT_PASSP,
             SOC_PERC_STUDENT_PASSP,
             SOC_NUM_STUDENT_PASS,
             SOC_PERC_STUDENT_PASS,
             SOC_NUM_STUDENT_DNP,
             SOC_PERC_STUDENT_DNP,
             SOC_NUM_STUDENT_UNDETERMINED,
             SOC_PERC_STUDENT_UNDETERMINED,
             SOC_NO_STUDENT_LISTED,
             SOC_MEAN_SCALE_SCORE,
             SOC_NUM_MASTERY1,
             SOC_NUM_MASTERY2,
             SOC_NUM_MASTERY3,
             SOC_NUM_MASTERY4,
             SOC_NUM_MASTERY5,
             SOC_NUM_MASTERY6,
             SOC_NUM_MASTERY7,
             SOC_NUM_MASTERY8,
             SOC_NUM_MASTERY9,
             SOC_NUM_MASTERY10,
             SOC_NUM_MASTERY11,
             SOC_NUM_MASTERY12,
             SOC_PERC_MASTERY1,
             SOC_PERC_MASTERY2,
             SOC_PERC_MASTERY3,
             SOC_PERC_MASTERY4,
             SOC_PERC_MASTERY5,
             SOC_PERC_MASTERY6,
             SOC_PERC_MASTERY7,
             SOC_PERC_MASTERY8,
             SOC_PERC_MASTERY9,
             SOC_PERC_MASTERY10,
             SOC_PERC_MASTERY11,
             SOC_PERC_MASTERY12,
             SOC_LOW_SCALE_POSSIBLE,
             SOC_HIGH_SCALE_POSSIBLE,
             SOC_UND_SCALE_SCORE_RANGE,
             SOC_PASS_SCALE_SCORE_RANGE,
             SOC_PASSP_SCALE_SCORE_RANGE,
             TEACHER_ELEMENT_NUMBER,
             STRUCTURE_LEVEL,
             a.ISPUBLIC,
             SYSDATE
        FROM ISTEP_DATAMIG.MIG_RESULTS_CLASSSUMMARY A,
             TEST_TP_MAP TTM,
             (SELECT DISTINCT ASSESSMENTID,
                              --subtestid,subtest_name,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP) D
       WHERE A.ORG_TEST_PROGRAM = TTM.TP_CODE
         AND A.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND A.GRADE = D.GRADE_CODE
         AND d.assessmentid = IN_ASSESSMENT_ID;

    COMMIT;
    --AND subtest_name ='English-Language Arts' ;
  END;

  PROCEDURE PROC_RESULTS_GRT_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO RESULTS_GRT_FACT
      (RESULTS_GRTID,
       STUDENT_BIO_ID,
       ORG_NODEID,
       ADMINID,
       CUST_PROD_ID,
       GRADEID,
       LEVELID,
       FACULTYID,
       STUDENTID,
       TAPE_MODE,
       ORGTSTGPGM,
       CITY,
       STATE,
       NRT_FORM,
       NRT_LEVEL,
       ABILITY_TEST_NAME,
       ABILITY_LEVEL,
       ISTEP_TEST_NAME,
       ISTEP_BOOK_NUM,
       ISTEP_FORM,
       SCORING_PATTERN,
       QUARTER_MONTH,
       TEST_DATE,
       STUDENT_LAST_NAME,
       ELA_PF_INDICATOR,
       MATH_PF_INDICATOR,
       SCIENCE_PF_INDICATOR,
       NOT_USED_1,
       STUDENT_FIRST_NAME,
       STUDENT_MIDDLE_INITIAL,
       STUDENT_TEST_AI,
       LOCAL_USE_J,
       ETHNICITY_K,
       SPECIAL_EDUCATION_L,
       GRADE_M,
       SOCIOECONOMIC_STATUS_N,
       SECTION_504_O,
       ACCOMMODATIONS_ELA_P,
       ACCOMMODATIONS_MATH_Q,
       LOCAL_USE_R,
       LOCAL_USE_S,
       LOCAL_USE_T,
       EXCEPTIONALITY_U,
       LEPESL_V,
       NOT_USED_W,
       ACCOMMODATIONS_SCIENCE_X,
       FROM_UDD_120_MIGRANT_Y,
       FROM_UDD_118_RETEST_FLAG_Z,
       BIRTH_DATE,
       CHRONOLOGICAL_AGE_IN_MONTHS,
       STUDENTS_GENDER,
       NOT_USED_2,
       NO_ISTEP,
       MASTERY_INDICATOR_1,
       MASTERY_INDICATOR_2,
       MASTERY_INDICATOR_3,
       MASTERY_INDICATOR_4,
       MASTERY_INDICATOR_5,
       MASTERY_INDICATOR_6,
       MASTERY_INDICATOR_7,
       MASTERY_INDICATOR_8,
       MASTERY_INDICATOR_9,
       MASTERY_INDICATOR_10,
       MASTERY_INDICATOR_11,
       MASTERY_INDICATOR_12,
       MASTERY_INDICATOR_13,
       MASTERY_INDICATOR_14,
       MASTERY_INDICATOR_15,
       MASTERY_INDICATOR_16,
       MASTERY_INDICATOR_17,
       MASTERY_INDICATOR_18,
       MASTERY_INDICATOR_19,
       MASTERY_INDICATOR_20,
       MASTERY_INDICATOR_21,
       MASTERY_INDICATOR_22,
       MASTERY_INDICATOR_23,
       MASTERY_INDICATOR_24,
       MASTERY_INDICATOR_25,
       MASTERY_INDICATOR_26,
       MASTERY_INDICATOR_27,
       MASTERY_INDICATOR_28,
       MASTERY_INDICATOR_29,
       MASTERY_INDICATOR_30,
       MASTERY_INDICATOR_31,
       MASTERY_INDICATOR_32,
       MASTERY_INDICATOR_33,
       MASTERY_INDICATOR_34,
       MASTERY_INDICATOR_35,
       OPIIPI_1,
       OPIIPI_2,
       OPIIPI_3,
       OPIIPI_4,
       OPIIPI_5,
       OPIIPI_6,
       OPIIPI_7,
       OPIIPI_8,
       OPIIPI_9,
       OPIIPI_10,
       OPIIPI_11,
       OPIIPI_12,
       OPIIPI_13,
       OPIIPI_14,
       OPIIPI_15,
       OPIIPI_16,
       OPIIPI_17,
       OPIIPI_18,
       OPIIPI_19,
       OPIIPI_20,
       OPIIPI_21,
       OPIIPI_22,
       OPIIPI_23,
       OPIIPI_24,
       OPIIPI_25,
       OPIIPI_26,
       OPIIPI_27,
       OPIIPI_28,
       OPIIPI_29,
       OPIIPI_30,
       OPIIPI_31,
       OPIIPI_32,
       OPIIPI_33,
       OPIIPI_34,
       OPIIPI_35,
       ENGLANG_ARTS_SUBTESTID,
       ENGLANG_ARTS_NUM_CORRECT,
       MATHEMATICS_SUBTESTID,
       MATHEMATICS_NUM_CORRECT,
       SCIENCE_SUBTESTID,
       SCIENCE_NUM_CORRECT,
       ENGLAN_ARTS_SCALE_SCORE,
       MATHEMATICS_SCALE_SCORE,
       SCIENCE_SCALE_SCORE,
       TEST_01,
       TEST_02,
       TEST_03,
       TEST_04,
       TEST_05,
       TEST_06,
       TEST01_SCI_TEST07_NOT_USED,
       TEST02_SCI_TEST08_NOT_USED,
       PA01_NOT_USED,
       PA02_OR_PA12_NOT_USED,
       PA03,
       PA04,
       PA05_OR_PA13_TEST_03_SCI,
       PA06,
       PA07,
       PA08,
       GRID_ITEMS_RIGHT_RESP_ARR,
       GRIDDED_ITEMS_STATUS,
       NOT_USED_3,
       IMAGEID_ELA,
       IMAGEID_MATH,
       IMAGE_ID_SCIENCE,
       CORPORATION_USE_ID,
       CUSTOMER_USE,
       STRUCTURE_LEVEL,
       ELEMENT_NUMBER,
       ISPUBLIC,
       TEACHER_NAME,
       BARCODE,
       CLASSID,
       TEACHER_ELEMENT,
       MATCH_UNMATCH_U,
       DUPLICATE_V,
       SPECIAL_CODE_X,
       SPECIAL_CODE_Y,
       SPECIAL_CODE_Z,
       ACCOMMODATIONS_SOCIAL,
       GRADE_FROM_SIQ,
       SOCIAL_PF_INDICATOR,
       SOCIAL_SUBTESTID,
       SOCIAL_NUM_CORRECT,
       SOCIAL_SCALE_SCORE,
       ENGLAN_ARTS_SCALE_SCORE_SEM,
       MATHEMATICS_SCALE_SCORE_SEM,
       SCIENCE_SCALE_SCORE_SEM,
       SOCIAL_SCALE_SCORE_SEM,
       MASTERY_INDICATOR_36,
       MASTERY_INDICATOR_37,
       MASTERY_INDICATOR_38,
       MASTERY_INDICATOR_39,
       MASTERY_INDICATOR_40,
       OPIIPI_36,
       OPIIPI_37,
       OPIIPI_38,
       OPIIPI_39,
       OPIIPI_40,
       ELA_MC_SESSION_1,
       ELA_MC_SESSION_2,
       ELA_MC_SESSION_3,
       MATH_MC_SESSION_1,
       MATH_MC_SESSION_2,
       MATH_MC_SESSION_3,
       SCIENCE_MC_SESSION_1,
       SCIENCE_MC_SESSION_2,
       SCIENCE_MC_SESSION_3,
       SOCIAL_MC_SESSION_1,
       SOCIAL_MC_SESSION_2,
       SOCIAL_MC_SESSION_3,
       ELA_CR_SESSION_1,
       ELA_CR_SESSION_2,
       ELA_CR_SESSION_3,
       MATH_CR_SESSION_1,
       MATH_CR_SESSION_2,
       MATH_CR_SESSION_3,
       SCIENCE_CR_SESSION_1,
       SCIENCE_CR_SESSION_2,
       SCIENCE_CR_SESSION_3,
       SOCIAL_CR_SESSION_1,
       SOCIAL_CR_SESSION_2,
       SOCIAL_CR_SESSION_3,
       FIELDTEST_ELA_MC_SESSION_1,
       FIELDTEST_ELA_MC_SESSION_2,
       FIELDTEST_ELA_MC_SESSION_3,
       FIELDTEST_MATH_MC_SESSION_1,
       FIELDTEST_MATH_MC_SESSION_2,
       FIELDTEST_MATH_MC_SESSION_3,
       FIELDTEST_SCIENCE_MC_SESSION_1,
       FIELDTEST_SCIENCE_MC_SESSION_2,
       FIELDTEST_SCIENCE_MC_SESSION_3,
       FIELDTEST_SOCIAL_MC_SESSION_1,
       FIELDTEST_SOCIAL_MC_SESSION_2,
       FIELDTEST_SOCIAL_MC_SESSION_3,
       FIELDTEST_ELA_CR_SESSION_1,
       FIELDTEST_ELA_CR_SESSION_2,
       FIELDTEST_ELA_CR_SESSION_3,
       FIELDTEST_MATH_CR_SESSION_1,
       FIELDTEST_MATH_CR_SESSION_2,
       FIELDTEST_MATH_CR_SESSION_3,
       FIELDTEST_SCIENCE_CR_SESSION_1,
       FIELDTEST_SCIENCE_CR_SESSION_2,
       FIELDTEST_SCIENCE_CR_SESSION_3,
       FIELDTEST_SOCIAL_CR_SESSION_1,
       FIELDTEST_SOCIAL_CR_SESSION_2,
       FIELDTEST_SOCIAL_CR_SESSION_3,
       FILST_RID_IT_RIGHT_RESPE_ARR,
       FIELDTEST_GRIDDED_ITEMS_STATUS,
       IMAGEID_APPLIED_SKILLS_PP,
       IMAGEID_APPLIED_SKILLS_OAS,
       IMAGEID_MC_PP,
       IMAGEID_MC_OAS,
       BARCODE_ID_MULTIPLE_CHOICE,
       MIGRANT_Q,
       TEST_FORM_SET_TO_DEFAULT_FLAG,
       MC_BLANK_BOOK_FLAG,
       TEST_FORM_APP_SKLS_FID_TEST,
       TEST_FORM_MC_FIELD_TEST,
       OAS_TSTD_IND_APPL_SKLS_TST,
       OAS_TESTED_INDICATOR_MC_TEST,
       SPN_1,
       SPN_2,
       SPN_3,
       SPN_4,
       SPN_5,
       CGR,
       ETHNICITY_HISPANIC,
       RACE_AMERICAN_INDIAN,
       RACE_ASIAN,
       RACE_BLACK,
       RACE_PACIFIC_ISLANDER,
       RACE_WHITE,
       RESOLVED_REPORTING_STATUS_ELA,
       RESOLVED_REPORTING_STATUS_MATH,
       RESLVD_REPOR_STUS_SCNCE,
       RESLVD_REPOR_STUS__SOCSTUD,
       CTB_USE_OPUNIT,
       AS_PO_IREAD,
       AS_PP_IREAD,
       OPIIPI_CUT_1,
       OPIIPI_CUT_2,
       OPIIPI_CUT_3,
       OPIIPI_CUT_4,
       OPIIPI_CUT_5,
       OPIIPI_CUT_6,
       OPIIPI_CUT_7,
       OPIIPI_CUT_8,
       OPIIPI_CUT_9,
       OPIIPI_CUT_10,
       OPIIPI_CUT_11,
       OPIIPI_CUT_12,
       OPIIPI_CUT_13,
       OPIIPI_CUT_14,
       OPIIPI_CUT_15,
       OPIIPI_CUT_16,
       OPIIPI_CUT_17,
       OPIIPI_CUT_18,
       OPIIPI_CUT_19,
       OPIIPI_CUT_20,
       OPIIPI_CUT_21,
       OPIIPI_CUT_22,
       OPIIPI_CUT_23,
       OPIIPI_CUT_24,
       OPIIPI_CUT_25,
       OPIIPI_CUT_26,
       OPIIPI_CUT_27,
       OPIIPI_CUT_28,
       OPIIPI_CUT_29,
       OPIIPI_CUT_30,
       OPIIPI_CUT_31,
       OPIIPI_CUT_32,
       OPIIPI_CUT_33,
       OPIIPI_CUT_34,
       OPIIPI_CUT_35,
       OPIIPI_CUT_36,
       OPIIPI_CUT_37,
       OPIIPI_CUT_38,
       OPIIPI_CUT_39,
       OPIIPI_CUT_40,
       CREATED_DATETIME,
       UPDATED_DATETIME)

      SELECT RESULTS_GRTID,
             SUBTEST_FACT_SEQ.NEXTVAL,
             (SELECT ORG_NODEID
                FROM ORG_NODE_DIM
               WHERE ORG_NODE_LEVEL = 4
                 AND ORG_NODE_CODE = G.ORGTSTGPGM || G.TEACHER_ELEMENT) org_nodeid,
             \*  (SELECT ADMINID
                                                                               FROM TEST_TP_MAP
                                                                              WHERE TTM.TESTID = G.TESTID
                                                                                 AND TTM.TP_CODE = G.ORGTSTGPGM)*\
             TTM. ADMINID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID
                 AND TTM.TP_CODE = G.ORGTSTGPGM) AS CUST_PROD_ID,

             --   d.contentid ,
             --   d.subtestid,
             D.GRADEID,
             D.LEVELID,
             G.FACULTYID,
             G.STUDENTID,
             G.TAPE_MODE,
             G.ORGTSTGPGM,
             G.CITY,
             G.STATE,
             G.NRT_FORM,
             G.NRT_LEVEL,
             G.ABILITY_TEST_NAME,
             G.ABILITY_LEVEL,
             G.ISTEP_TEST_NAME,
             G.ISTEP_BOOK_NUM,
             G.ISTEP_FORM,
             G.SCORING_PATTERN,
             G.QUARTER_MONTH,
             G.TEST_DATE,
             G.STUDENT_LAST_NAME,
             G.ELA_PF_INDICATOR,
             G.MATH_PF_INDICATOR,
             G.SCIENCE_PF_INDICATOR,
             G.NOT_USED_1,
             G.STUDENT_FIRST_NAME,
             G.STUDENT_MIDDLE_INITIAL,
             G.STUDENT_TEST_AI,
             G.LOCAL_USE_J,
             G.ETHNICITY_K,
             G.SPECIAL_EDUCATION_L,
             G.GRADE_M,
             G.SOCIOECONOMIC_STATUS_N,
             G.SECTION_504_O,
             G.ACCOMMODATIONS_ELA_P,
             G.ACCOMMODATIONS_MATH_Q,
             G.LOCAL_USE_R,
             G.LOCAL_USE_S,
             G.LOCAL_USE_T,
             G.EXCEPTIONALITY_U,
             G.LEPESL_V,
             G.NOT_USED_W,
             G.ACCOMMODATIONS_SCIENCE_X,
             G.FROM_UDD_120_MIGRANT_Y,
             G.FROM_UDD_118_RETEST_FLAG_Z,
             G.BIRTH_DATE,
             G.CHRONOLOGICAL_AGE_IN_MONTHS,
             G.STUDENTS_GENDER,
             G.NOT_USED_2,
             G.NO_ISTEP,
             G.MASTERY_INDICATOR_1,
             G.MASTERY_INDICATOR_2,
             G.MASTERY_INDICATOR_3,
             G.MASTERY_INDICATOR_4,
             G.MASTERY_INDICATOR_5,
             G.MASTERY_INDICATOR_6,
             G.MASTERY_INDICATOR_7,
             G.MASTERY_INDICATOR_8,
             G.MASTERY_INDICATOR_9,
             G.MASTERY_INDICATOR_10,
             G.MASTERY_INDICATOR_11,
             G.MASTERY_INDICATOR_12,
             G.MASTERY_INDICATOR_13,
             G.MASTERY_INDICATOR_14,
             G.MASTERY_INDICATOR_15,
             G.MASTERY_INDICATOR_16,
             G.MASTERY_INDICATOR_17,
             G.MASTERY_INDICATOR_18,
             G.MASTERY_INDICATOR_19,
             G.MASTERY_INDICATOR_20,
             G.MASTERY_INDICATOR_21,
             G.MASTERY_INDICATOR_22,
             G.MASTERY_INDICATOR_23,
             G.MASTERY_INDICATOR_24,
             G.MASTERY_INDICATOR_25,
             G.MASTERY_INDICATOR_26,
             G.MASTERY_INDICATOR_27,
             G.MASTERY_INDICATOR_28,
             G.MASTERY_INDICATOR_29,
             G.MASTERY_INDICATOR_30,
             G.MASTERY_INDICATOR_31,
             G.MASTERY_INDICATOR_32,
             G.MASTERY_INDICATOR_33,
             G.MASTERY_INDICATOR_34,
             G.MASTERY_INDICATOR_35,
             G.OPIIPI_1,
             G.OPIIPI_2,
             G.OPIIPI_3,
             G.OPIIPI_4,
             G.OPIIPI_5,
             G.OPIIPI_6,
             G.OPIIPI_7,
             G.OPIIPI_8,
             G.OPIIPI_9,
             G.OPIIPI_10,
             G.OPIIPI_11,
             G.OPIIPI_12,
             G.OPIIPI_13,
             G.OPIIPI_14,
             G.OPIIPI_15,
             G.OPIIPI_16,
             G.OPIIPI_17,
             G.OPIIPI_18,
             G.OPIIPI_19,
             G.OPIIPI_20,
             G.OPIIPI_21,
             G.OPIIPI_22,
             G.OPIIPI_23,
             G.OPIIPI_24,
             G.OPIIPI_25,
             G.OPIIPI_26,
             G.OPIIPI_27,
             G.OPIIPI_28,
             G.OPIIPI_29,
             G.OPIIPI_30,
             G.OPIIPI_31,
             G.OPIIPI_32,
             G.OPIIPI_33,
             G.OPIIPI_34,
             G.OPIIPI_35,
             nvl((SELECT DISTINCT SUBTESTID
                   FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
                  WHERE M.GRADEID = D.GRADEID
                    AND M.ASSESSMENTID = D.ASSESSMENTID
                    AND M.ASSESSMENTID = T.ASSESSMENTID
                    AND T.TESTID = G.TESTID
                    AND T.TP_CODE = G.ORGTSTGPGM
                    AND SUBTEST_CODE = 'ELA'),
                 (SELECT DISTINCT SUBTESTID
                    FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
                   WHERE M.GRADEID = D.GRADEID
                     AND M.ASSESSMENTID = D.ASSESSMENTID
                     AND M.ASSESSMENTID = T.ASSESSMENTID
                     AND T.TESTID = G.TESTID
                     AND T.TP_CODE = G.ORGTSTGPGM
                     AND SUBTEST_CODE = 'READ')) ENGLANG_ARTS_SUBTESTID,
             G.ENGLANG_ARTS_NUM_CORRECT,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGTSTGPGM
                 AND M.SUBTEST_CODE = 'MATH') MATHEMATICS_SUBTESTID,
             G.MATHEMATICS_NUM_CORRECT,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGTSTGPGM
                 AND M.SUBTEST_CODE = 'SCI') SCIENCE_SUBTESTID,
             G.SCIENCE_NUM_CORRECT,
             G.ENGLAN_ARTS_SCALE_SCORE,
             G.MATHEMATICS_SCALE_SCORE,
             G.SCIENCE_SCALE_SCORE,
             G.TEST_01,
             G.TEST_02,
             G.TEST_03,
             G.TEST_04,
             G.TEST_05,
             G.TEST_06,
             G.TEST01_SCI_TEST07_NOT_USED,
             G.TEST02_SCI_TEST08_NOT_USED,
             G.PA01_NOT_USED,
             G.PA02_OR_PA12_NOT_USED,
             G.PA03,
             G.PA04,
             G.PA05_OR_PA13_TEST_03_SCI,
             G.PA06,
             G.PA07,
             G.PA08,
             G.GRID_ITEMS_RIGHT_RESP_ARR,
             G.GRIDDED_ITEMS_STATUS,
             G.NOT_USED_3,
             G.IMAGEID_ELA,
             G.IMAGEID_MATH,
             G.IMAGE_ID_SCIENCE,
             G.CORPORATION_USE_ID,
             G.CUSTOMER_USE,
             G.STRUCTURE_LEVEL,
             G.ELEMENT_,
             trim(G.ISPUBLIC),
             G.TEACHER_NAME,
             G.BARCODE,
             G.CLASSID,
             G.TEACHER_ELEMENT,
             G.MATCH_UNMATCH_U,
             G.DUPLICATE_V,
             G.SPECIAL_CODE_X,
             G.SPECIAL_CODE_Y,
             G.SPECIAL_CODE_Z,
             G.ACCOMMODATIONS_SOCIAL,
             G.GRADE_FROM_SIQ,
             G.SOCIAL_PF_INDICATOR,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGTSTGPGM
                 AND M.SUBTEST_CODE = 'SS') SOCIAL_SUBTESTID,
             G.SOCIAL_NUM_CORRECT,
             G.SOCIAL_SCALE_SCORE,
             G.ENGLAN_ARTS_SCALE_SCORE_SEM,
             G.MATHEMATICS_SCALE_SCORE_SEM,
             G.SCIENCE_SCALE_SCORE_SEM,
             G.SOCIAL_SCALE_SCORE_SEM,
             G.MASTERY_INDICATOR_36,
             G.MASTERY_INDICATOR_37,
             G.MASTERY_INDICATOR_38,
             G.MASTERY_INDICATOR_39,
             G.MASTERY_INDICATOR_40,
             G.OPIIPI_36,
             G.OPIIPI_37,
             G.OPIIPI_38,
             G.OPIIPI_39,
             G.OPIIPI_40,
             G.ELA_MC_SESSION_1,
             G.ELA_MC_SESSION_2,
             G.ELA_MC_SESSION_3,
             G.MATH_MC_SESSION_1,
             G.MATH_MC_SESSION_2,
             G.MATH_MC_SESSION_3,
             G.SCIENCE_MC_SESSION_1,
             G.SCIENCE_MC_SESSION_2,
             G.SCIENCE_MC_SESSION_3,
             G.SOCIAL_MC_SESSION_1,
             G.SOCIAL_MC_SESSION_2,
             G.SOCIAL_MC_SESSION_3,
             G.ELA_CR_SESSION_1,
             G.ELA_CR_SESSION_2,
             G.ELA_CR_SESSION_3,
             G.MATH_CR_SESSION_1,
             G.MATH_CR_SESSION_2,
             G.MATH_CR_SESSION_3,
             G.SCIENCE_CR_SESSION_1,
             G.SCIENCE_CR_SESSION_2,
             G.SCIENCE_CR_SESSION_3,
             G.SOCIAL_CR_SESSION_1,
             G.SOCIAL_CR_SESSION_2,
             G.SOCIAL_CR_SESSION_3,
             G.FIELDTEST_ELA_MC_SESSION_1,
             G.FIELDTEST_ELA_MC_SESSION_2,
             G.FIELDTEST_ELA_MC_SESSION_3,
             G.FIELDTEST_MATH_MC_SESSION_1,
             G.FIELDTEST_MATH_MC_SESSION_2,
             G.FIELDTEST_MATH_MC_SESSION_3,
             G.FIELDTEST_SCIENCE_MC_SESSION_1,
             G.FIELDTEST_SCIENCE_MC_SESSION_2,
             G.FIELDTEST_SCIENCE_MC_SESSION_3,
             G.FIELDTEST_SOCIAL_MC_SESSION_1,
             G.FIELDTEST_SOCIAL_MC_SESSION_2,
             G.FIELDTEST_SOCIAL_MC_SESSION_3,
             G.FIELDTEST_ELA_CR_SESSION_1,
             G.FIELDTEST_ELA_CR_SESSION_2,
             G.FIELDTEST_ELA_CR_SESSION_3,
             G.FIELDTEST_MATH_CR_SESSION_1,
             G.FIELDTEST_MATH_CR_SESSION_2,
             G.FIELDTEST_MATH_CR_SESSION_3,
             G.FIELDTEST_SCIENCE_CR_SESSION_1,
             G.FIELDTEST_SCIENCE_CR_SESSION_2,
             G.FIELDTEST_SCIENCE_CR_SESSION_3,
             G.FIELDTEST_SOCIAL_CR_SESSION_1,
             G.FIELDTEST_SOCIAL_CR_SESSION_2,
             G.FIELDTEST_SOCIAL_CR_SESSION_3,
             G.FILST_RID_IT_RIGHT_RESPE_ARR,
             G.FIELDTEST_GRIDDED_ITEMS_STATUS,
             G.IMAGEID_APPLIED_SKILLS_PP,
             G.IMAGEID_APPLIED_SKILLS_OAS,
             G.IMAGEID_MC_PP,
             G.IMAGEID_MC_OAS,
             G.BARCODE_ID_MULTIPLE_CHOICE,
             G.MIGRANT_Q,
             G.TEST_FORM_SET_TO_DEFAULT_FLAG,
             G.MC_BLANK_BOOK_FLAG,
             G.TEST_FORM_APP_SKLS_FID_TEST,
             G.TEST_FORM_MC_FIELD_TEST,
             G.OAS_TSTD_IND_APPL_SKLS_TST,
             G.OAS_TESTED_INDICATOR_MC_TEST,
             G.SPN_1,
             G.SPN_2,
             G.SPN_3,
             G.SPN_4,
             G.SPN_5,
             G.CGR,
             G.ETHNICITY_HISPANIC,
             G.RACE_AMERICAN_INDIAN,
             G.RACE_ASIAN,
             G.RACE_BLACK,
             G.RACE_PACIFIC_ISLANDER,
             G.RACE_WHITE,
             G.RESOLVED_REPORTING_STATUS_ELA,
             G.RESOLVED_REPORTING_STATUS_MATH,
             G.RESLVD_REPOR_STUS_SCNCE,
             G.RESLVD_REPOR_STUS__SOCSTUD,
             G.CTB_USE_OPUNIT,
             G.AS_PO_IREAD,
             G.AS_PP_IREAD,
             trim(G.OPIIPI_CUT_1),
             trim(G.OPIIPI_CUT_2),
             trim(G.OPIIPI_CUT_3),
             trim(G.OPIIPI_CUT_4),
             trim(G.OPIIPI_CUT_5),
             trim(G.OPIIPI_CUT_6),
             trim(G.OPIIPI_CUT_7),
             trim(G.OPIIPI_CUT_8),
             trim(G.OPIIPI_CUT_9),
             trim(G.OPIIPI_CUT_10),
             trim(G.OPIIPI_CUT_11),
             trim(G.OPIIPI_CUT_12),
             trim(G.OPIIPI_CUT_13),
             trim(G.OPIIPI_CUT_14),
             trim(G.OPIIPI_CUT_15),
             trim(G.OPIIPI_CUT_16),
             trim(G.OPIIPI_CUT_17),
             trim(G.OPIIPI_CUT_18),
             trim(G.OPIIPI_CUT_19),
             trim(G.OPIIPI_CUT_20),
             trim(G.OPIIPI_CUT_21),
             trim(G.OPIIPI_CUT_22),
             trim(G.OPIIPI_CUT_23),
             trim(G.OPIIPI_CUT_24),
             trim(G.OPIIPI_CUT_25),
             trim(G.OPIIPI_CUT_26),
             trim(G.OPIIPI_CUT_27),
             trim(G.OPIIPI_CUT_28),
             trim(G.OPIIPI_CUT_29),
             trim(G.OPIIPI_CUT_30),
             trim(G.OPIIPI_CUT_31),
             trim(G.OPIIPI_CUT_32),
             trim(G.OPIIPI_CUT_33),
             trim(G.OPIIPI_CUT_34),
             trim(G.OPIIPI_CUT_35),
             trim(G.OPIIPI_CUT_36),
             trim(G.OPIIPI_CUT_37),
             trim(G.OPIIPI_CUT_38),
             trim(G.OPIIPI_CUT_39),
             trim(G.OPIIPI_CUT_40),
             SYSDATE,
             SYSDATE
        FROM ISTEP_DATAMIG.MIG_RESULTS_GRT G,
             TEST_TP_MAP TTM,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              --subtestid,subtest_name,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE assessmentid = IN_ASSESSMENT_ID) D
       WHERE G.ORGTSTGPGM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND ttm.assessmentid = IN_ASSESSMENT_ID
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND g.RESULTS_GRTID NOT IN (SELECT results_grtid
                                       FROM migrations.test11 \*WHERE org_nodeid IS NULL*\
                                     )
      --AND rownum < 10000
      ;
    COMMIT;
    --AND subtest_name ='English-Language Arts' ;
  END;

  PROCEDURE PROC_RESULTS_MEDIA_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO MEDIA_FACT
      (MEDIA_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       ENGLANG_ARTS_SUBTESTID,
       MATHEMATICS_SUBTESTID,
       SCIENCE_SUBTESTID,
       SOCIAL_SUBTESTID,
       GRADEID,
       LEVELID,
       TEST_DATE,
       TOT_STUD_TESTED,
       CTB_USE,
       NUM_PASSP_ELA_PASSP_MATH,
       PER_PASSP_ELA_PASSP_MATH,
       NUM_PASS_ELA_PASSP_MATH,
       PERC_PASS_ELA_PASSP_MATH,
       NUM_DNP_ELA_PASSP_MATH,
       PERC_DNP_ELA_PASSP_MATH,
       NUM_UNDTRM_ELA_PASSP_MATH,
       PERC_UNDTRM_ELA_PASSP_MATH,
       TOT_NUM_PASSP_MATH,
       TOT_PERC_PASSP_MATH,
       NUM_PASSP_ELA_PASS_MATH,
       PERC_PASSP_ELA_PASS_MATH,
       NUM_PASS_ELA_PASS_MATH,
       PERC_PASS_ELA_PASS_MATH,
       NUM_DNP_ELA_PASS_MATH,
       PERC_DNP_ELA_PASS_MATH,
       NUM_UNDTRM_ELA_PASS_MATH,
       PERC_UNDTRM_ELA_PASS_MATH,
       TOT_NUM_PASS_MATH,
       TOT_PERC_PASS_MATH,
       NUM_PASSP_ELA_DNP_MATH,
       PERC_PASSP_ELA_DNP_MATH,
       NUM_PASS_ELA_DNP_MATH,
       PERC_PASS_ELA_DNP_MATH,
       NUM_DNP_ELA_DNP_MATH,
       PERC_DNP_ELA_DNP_MATH,
       NUM_UNDTRM_ELA_DNP_MATH,
       PERC_UNDTRM_ELA_DNP_MATH,
       TOT_NUM_DNP_MATH,
       TOT_PERC_DNP_MATH,
       NUM_PASSP_ELA_UNDTRM_MATH,
       PERC_PASSP_ELA_UNDTRM_MATH,
       NUM_PASS_ELA_UNDTRM_MATH,
       PERC_PASS_ELA_UNDTRM_MATH,
       NUM_DNP_ELA_UNDTRM_MATH,
       PERC_DNP_ELA_UNDTRM_MATH,
       NUM_UNDTRM_ELA_UNDTRM_MATH,
       PERC_UNDTRM_ELA_UNDTRM_MATH,
       TOT_NUM_UNDTRM_MATH,
       TOT_PERC_UNDTRM_MATH,
       TOT_NUM_PASSP_ELA,
       TOT_PERC_PASSP_ELA,
       TOT_NUM_PASS_ELA,
       TOT_PERC_PASS_ELA,
       TOT_NUM_DNP_ELA,
       TOT_PERC_DNP_ELA,
       TOT_NUM_UNDTRM_ELA,
       TOT_PERC_UNDTRM_ELA,
       TOT_STUD_COUNT_ELA_MATH,
       TOT_PERC_OF_STUD_ELA_MATH,
       NUM_PASSP_SCIE,
       PERC_PASSP_SCIE,
       NUM_PASS_SCIE,
       PERC_PASS_SCIE,
       NUM_DNP_SCIE,
       PERC_DNP_SCIE,
       NUM_UNDTRM_SCIE,
       PERC_UNDTRM_SCIE,
       TOT_STUD_COUNT_SCIE,
       TOT_PERC_OF_STUD_SCIE,
       LOW_OBTAINED_ELA_SCALE_SCORE,
       HIGH_OBTAINED_ELA_SCALE_SCORE,
       LOW_OBTAINED_MATH_SCALE_SCORE,
       HIGH_OBTAINED_MATH_SCALE_SCORE,
       LOW_OBTAINED_SCIE_SCALE_SCORE,
       HIGH_OBTAINED_SCIE_SCALE_SCORE,
       TOTPERC_STUD_PASSPPASS_ELAMATH,
       TOT_PERC_STUD_PASSP_PASS_ELA,
       TOT_PERC_STUD_PASSP_PASS_MATH,
       TOT_PERC_STUD_PASSP_PASS_SCIE,
       NUM_PASSP_SOCIAL,
       PERC_PASSP_SOCIAL,
       NUM_PASS_SOCIAL,
       PERC_PASS_SOCIAL,
       NUM_DNP_SOCIAL,
       PERC_DNP_SOCIAL,
       NUM_UNDTRM_SOCIAL,
       PERC_UNDTRM_SOCIAL,
       TOT_STUD_COUNT_SOCIAL,
       TOT_PERC_OF_STUDENTS_SOCIAL,
       LOW_OBTAINED_SOCIAL_SCALESCORE,
       HIGH_OBTAINED_SOCIA_SCALESCORE,
       TOT_PERC_STUD_PASSPPASS_SOCIAL,
       STRUCTURE_LEVEL,
       ELEMENT_NUMBER,
       ISPUBLIC,
       DATETIMESTAMP)

      SELECT RESULTS_MEDIAID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,

             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND SUBTEST_CODE = 'ELA') ENGLANG_ARTS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND M.SUBTEST_CODE = 'MATH') MATHEMATICS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND M.SUBTEST_CODE = 'SCI') SCIENCE_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND M.SUBTEST_CODE = 'SS') SOCIAL_SUBTESTID,
             D.GRADEID,
             D.LEVELID,
             G.TEST_DATE,
             G.TOTAL_STUDENTS_TESTED,
             G.CTB_USE,
             G.NO_PSSPLS_ELA_PSSPLS_MATH,
             G.PERC_PSSPLS_ELA_PSSPLS_MATH,
             G.NO_PSS_ELA_PSSPLS_MATH,
             G.PERC_PSS_ELA_PSSPLS_MATH,
             G.NO_DD_NT_PSS_ELA_PSSPLS_MATH,
             G.PERC_DD_NT_PSS_ELA_PSSPLS_MATH,
             G.NUMBER_UNDTRM_ELA_PASSPLS_MATH,
             G.PERC_UNDTRM_ELA_PSSPLS_MATH,
             G.TOTAL_NUMBER_PASSPLS_MATH,
             G.TOTAL_PERCENT_PASSPLS_MATH,
             G.NUMBER_PASSPLS_ELA_PASS_MATH,
             G.PERCENT_PASSPLS_ELA_PASS_MATH,
             G.NUMBER_PASS_ELA_PASS_MATH,
             G.PERCENT_PASS_ELA_PASS_MATH,
             G.NO_DD_NT_PSS_ELA_PSS_MATH,
             G.PERC_DD_NT_PSS_ELA_PSS_MATH,
             G.NUMBER_UNDTRM_ELA_PASS_MATH,
             G.PERCENT_UNDTRM_ELA_PASS_MATH,
             G.TOTAL_NUMBER_PASS_MATH,
             G.TOTAL_PERCENT_PASS_MATH,
             G.NO_PSSPLS_ELA_DD_NT_PSS_MATH,
             G.PERC_PSSPLS_ELA_DD_NT_PSS_MATH,
             G.NO_PSS_ELA_DD_NT_PSS_MATH,
             G.PERC_PSS_ELA_DD_NT_PSS_MATH,
             G.NO_DD_NT_PSS_EL_DD_NT_PSS_MTH,
             G.PER_DD_NT_PSS_EL_DD_NT_PSS_MTH,
             G.NO_UNDTRM_ELA_DD_NT_PSS_MATH,
             G.PERC_UNDTRM_ELA_DD_NT_PSS_MTH,
             G.TOTAL_NO_DD_NT_PSS_MATH,
             G.TOTAL_PERC_DD_NT_PSS_MATH,
             G.NO_PSSPLS_ELA_UNDTRM_MATH,
             G.PERC_PSSPLS_ELA_UNDTRM_MATH,
             G.NO_PSS_ELA_UNDTRM_MATH,
             G.PERC_PSS_ELA_UNDTRM_MATH,
             G.NO_DD_NT_PSS_ELA_UNDTRM_MATH,
             G.PERC_DD_NT_PSS_ELA_UNDTRM_MATH,
             G.NO_UNDTRM_ELA_UNDTRM_MATH,
             G.PERC_UNDTRM_ELA_UNDTRM_MATH,
             G.TOT_NO_UNDTRM_MATH,
             G.TOT_PERC_UNDTRM_MATH,
             G.TOT_NO_PSSPLS_ELA,
             G.TOT_PERC_PSSPLS_ELA,
             G.TOT_NO_PSS_ELA,
             G.TOT_PERC_PSS_ELA,
             G.TOT_NO_DD_NT_PSS_ELA,
             G.TOT_PERC_DD_NT_PSS_ELA,
             G.TOT_NO_UNDTRM_ELA,
             G.TOT_PERC_UNDTRM_ELA,
             G.TOT_STUDENT_COUNT_ELA_MATH,
             G.TOT_PERC_OF_STUDENTS_ELA_MATH,
             G.NO_PSSPLS_SCIENCE,
             G.PERC_PSSPLS_SCIENCE,
             G.NO_PSS_SCIENCE,
             G.PERC_PSS_SCIENCE,
             G.NO_DD_NT_PSS_SCIENCE,
             G.PERC_DD_NT_PSS_SCIENCE,
             G.NO_UNDTRM_SCIENCE,
             G.PERC_UNDTRM_SCIENCE,
             G.TOT_STUDENT_COUNT_SCIENCE,
             G.TOT_PERC_OF_STUDENTS_SCIENCE,
             G.LOWST_OBTND_ELA_SCLE_SCOR,
             G.HIGST_OBTND_ELA_SCLE_SCOR,
             G.LOWST_OBTND_MATH_SCLE_SCOR,
             G.HIGST_OBTND_MATH_SCALE_SCORE,
             G.LOWEST_OBTND_SCN_SCALE_SCORE,
             G.HIGST_OBTND_SCNE_SCALE_SCORE,
             G.TOPRSTWHRPSSPLSORPSSBTHELANDMT,
             G.TOPERSTD_WH_R_PSSPLS_ORPSSELA,
             G.TOPERSTDWH_R_PSSPLS_R_PSS_MTH,
             G.TOPERSTDWH_R_PSSPLS_R_PSS_SCI,
             G.NO_PSSPLS_SOCIAL,
             G.PERC_PSSPLS_SOCIAL,
             G.NO_PSS_SOCIAL,
             G.PERC_PSS_SOCIAL,
             G.NO_DD_NT_PSS_SOCIAL,
             G.PERC_DD_NT_PSS_SOCIAL,
             G.NO_UNDTRM_SOCIAL,
             G.PERC_UNDTRM_SOCIAL,
             G.TOTL_STUDENT_COUNT_SOCIAL,
             G.TOTL_PERC_OF_STDS_SOCIAL,
             G.LOW_OBTND_SOCIAL_SCALE_SCORE,
             G.HIGST_OBTND_SOCIAL_SCALE_SCORE,
             G.TOPERSTDWH_R_PSSPLS_OR_PSS_SOC,
             G.STRUCTURE_LEVEL,
             G.ELEMENT_NO,
             g.ISPUBLIC,
             SYSDATE
        FROM ISTEP_DATAMIG.MIG_RESULTS_MEDIA G,
             TEST_TP_MAP TTM,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              --subtestid,subtest_name,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE assessmentid = IN_ASSESSMENT_ID) D
       WHERE G.ORG_TSTG_PGM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
    --AND subtest_name ='English-Language Arts' ;
  END;

  PROCEDURE PROC_RESULTS_SUMT_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO SUMT_FACT
      (SUMT_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       \*CONTENTID            ,
                                                       SUBTESTID   ,
                                                       OBJECTIVEID   ,*\
       ENGLANG_ARTS_SUBTESTID,
       MATHEMATICS_SUBTESTID,
       SCIENCE_SUBTESTID,
       SOCIAL_SUBTESTID,
       GRADEID,
       LEVELID,
       CRT_Student_Cnt,
       Total_ela_prof_mean_NCR,
       Total_Math_prof_mean_NCR,
       Total_ela_prof_mean_SS,
       Total_Math_prof_mean_SS,
       Total_Science_prof_mean_NCR,
       Total_Science_prof_mean_SS,
       Read_Vocabulary_mean_NCE,
       Read_Total_mean_NCE,
       Lang_Expression_mean_NCE,
       Lang_Mechanics_mean_NCE,
       Lang_Total_mean_NCE,
       math_Cons_And_Appl_mean_NCE,
       math_Computation_mean_NCE,
       math_total_mean_NCE,
       Battery_total_mean_NCE,
       Mean_number_correct_1,
       Mean_number_correct_2,
       Mean_number_correct_3,
       Mean_number_correct_4,
       Mean_number_correct_5,
       Mean_number_correct_6,
       Mean_number_correct_7,
       Mean_number_correct_8,
       Mean_number_correct_9,
       Mean_number_correct_10,
       Mean_number_correct_11,
       Mean_number_correct_12,
       Mean_number_correct_13,
       Mean_number_correct_14,
       Mean_number_correct_15,
       Mean_number_correct_16,
       Mean_number_correct_17,
       Mean_number_correct_18,
       Mean_number_correct_19,
       Mean_number_correct_20,
       Mean_number_correct_21,
       Mean_number_correct_22,
       Mean_number_correct_23,
       Mean_number_correct_24,
       Mean_number_correct_25,
       Mean_number_correct_26,
       Mean_number_correct_27,
       Mean_number_correct_28,
       Mean_number_correct_29,
       Mean_number_correct_30,
       Mean_number_correct_31,
       Mean_percent_correct_1,
       Mean_percent_correct_2,
       Mean_percent_correct_3,
       Mean_percent_correct_4,
       Mean_percent_correct_5,
       Mean_percent_correct_6,
       Mean_percent_correct_7,
       Mean_percent_correct_8,
       Mean_percent_correct_9,
       Mean_percent_correct_10,
       Mean_percent_correct_11,
       Mean_percent_correct_12,
       Mean_percent_correct_13,
       Mean_percent_correct_14,
       Mean_percent_correct_15,
       Mean_percent_correct_16,
       Mean_percent_correct_17,
       Mean_percent_correct_18,
       Mean_percent_correct_19,
       Mean_percent_correct_20,
       Mean_percent_correct_21,
       Mean_percent_correct_22,
       Mean_percent_correct_23,
       Mean_percent_correct_24,
       Mean_percent_correct_25,
       Mean_percent_correct_26,
       Mean_percent_correct_27,
       Mean_percent_correct_28,
       Mean_percent_correct_29,
       Mean_percent_correct_30,
       Mean_percent_correct_31,
       Mean_IPI_1,
       Mean_IPI_2,
       Mean_IPI_3,
       Mean_IPI_4,
       Mean_IPI_5,
       Mean_IPI_6,
       Mean_IPI_7,
       Mean_IPI_8,
       Mean_IPI_9,
       Mean_IPI_10,
       Mean_IPI_11,
       Mean_IPI_12,
       Mean_IPI_13,
       Mean_IPI_14,
       Mean_IPI_15,
       Mean_IPI_16,
       Mean_IPI_17,
       Mean_IPI_18,
       Mean_IPI_19,
       Mean_IPI_20,
       Mean_IPI_21,
       Mean_IPI_22,
       Mean_IPI_23,
       Mean_IPI_24,
       Mean_IPI_25,
       Mean_IPI_26,
       Mean_IPI_27,
       Mean_IPI_28,
       Mean_IPI_29,
       Mean_IPI_30,
       Mean_IPI_31,
       IPI_Difference_1,
       IPI_Difference_2,
       IPI_Difference_3,
       IPI_Difference_4,
       IPI_Difference_5,
       IPI_Difference_6,
       IPI_Difference_7,
       IPI_Difference_8,
       IPI_Difference_9,
       IPI_Difference_10,
       IPI_Difference_11,
       IPI_Difference_12,
       IPI_Difference_13,
       IPI_Difference_14,
       IPI_Difference_15,
       IPI_Difference_16,
       IPI_Difference_17,
       IPI_Difference_18,
       IPI_Difference_19,
       IPI_Difference_20,
       IPI_Difference_21,
       IPI_Difference_22,
       IPI_Difference_23,
       IPI_Difference_24,
       IPI_Difference_25,
       IPI_Difference_26,
       IPI_Difference_27,
       IPI_Difference_28,
       IPI_Difference_29,
       IPI_Difference_30,
       IPI_Difference_31,
       Number_Mastery_1,
       Number_Mastery_2,
       Number_Mastery_3,
       Number_Mastery_4,
       Number_Mastery_5,
       Number_Mastery_6,
       Number_Mastery_7,
       Number_Mastery_8,
       Number_Mastery_9,
       Number_Mastery_10,
       Number_Mastery_11,
       Number_Mastery_12,
       Number_Mastery_13,
       Number_Mastery_14,
       Number_Mastery_15,
       Number_Mastery_16,
       Number_Mastery_17,
       Number_Mastery_18,
       Number_Mastery_19,
       Number_Mastery_20,
       Number_Mastery_21,
       Number_Mastery_22,
       Number_Mastery_23,
       Number_Mastery_24,
       Number_Mastery_25,
       Number_Mastery_26,
       Number_Mastery_27,
       Number_Mastery_28,
       Number_Mastery_29,
       Number_Mastery_30,
       Number_Mastery_31,
       Percent_Mastery_1,
       Percent_Mastery_2,
       Percent_Mastery_3,
       Percent_Mastery_4,
       Percent_Mastery_5,
       Percent_Mastery_6,
       Percent_Mastery_7,
       Percent_Mastery_8,
       Percent_Mastery_9,
       Percent_Mastery_10,
       Percent_Mastery_11,
       Percent_Mastery_12,
       Percent_Mastery_13,
       Percent_Mastery_14,
       Percent_Mastery_15,
       Percent_Mastery_16,
       Percent_Mastery_17,
       Percent_Mastery_18,
       Percent_Mastery_19,
       Percent_Mastery_20,
       Percent_Mastery_21,
       Percent_Mastery_22,
       Percent_Mastery_23,
       Percent_Mastery_24,
       Percent_Mastery_25,
       Percent_Mastery_26,
       Percent_Mastery_27,
       Percent_Mastery_28,
       Percent_Mastery_29,
       Percent_Mastery_30,
       Percent_Mastery_31,
       NRT_Student_Cnt_NRT_Not_Appl,
       Retest_Flag,
       Total_Social_prof_mean_NCR,
       Total_Social_prof_mean_SS,
       Structure_Level,
       Element_Number,
       ispublic,
       DATETIMESTAMP)
      SELECT RESULTS_SUMTID,

             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,

             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND SUBTEST_CODE = 'ELA') ENGLANG_ARTS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND M.SUBTEST_CODE = 'MATH') MATHEMATICS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND M.SUBTEST_CODE = 'SCI') SCIENCE_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORG_TSTG_PGM
                 AND M.SUBTEST_CODE = 'SS') SOCIAL_SUBTESTID,
             D.GRADEID,
             D.LEVELID,
             CRT_STUDENT_COUNT,
             TOTAL_ELA_PROF_MEAN_NCR,
             TOTAL_MATH_PROF_MEAN_NCR,
             TOTAL_ELA_PROF_MEAN_SS,
             TOTAL_MATH_PROF_MEAN_SS,
             TOTAL_SCIENCE_PROF_MEAN_NCR,
             TOTAL_SCIENCE_PROF_MEAN_SS,
             READ_VOCABULARY_MEAN_NCE,
             READ_TOTAL_MEAN_NCE,
             LANGUAGE_EXPRESSION_MEAN_NCE,
             LANGUAGE_MECHANICS_MEAN_NCE,
             LANGUAGE_TOTAL_MEAN_NCE,
             MATH_CONC_AND_APPL_MEAN_NCE,
             MATH_COMPUTATION_MEAN_NCE,
             MATH_TOTAL_MEAN_NCE,
             BATTERY_TOTAL_MEAN_NCE,
             MEAN_NUMBER_CORRECT_1,
             MEAN_NUMBER_CORRECT_2,
             MEAN_NUMBER_CORRECT_3,
             MEAN_NUMBER_CORRECT_4,
             MEAN_NUMBER_CORRECT_5,
             MEAN_NUMBER_CORRECT_6,
             MEAN_NUMBER_CORRECT_7,
             MEAN_NUMBER_CORRECT_8,
             MEAN_NUMBER_CORRECT_9,
             MEAN_NUMBER_CORRECT_10,
             MEAN_NUMBER_CORRECT_11,
             MEAN_NUMBER_CORRECT_12,
             MEAN_NUMBER_CORRECT_13,
             MEAN_NUMBER_CORRECT_14,
             MEAN_NUMBER_CORRECT_15,
             MEAN_NUMBER_CORRECT_16,
             MEAN_NUMBER_CORRECT_17,
             MEAN_NUMBER_CORRECT_18,
             MEAN_NUMBER_CORRECT_19,
             MEAN_NUMBER_CORRECT_20,
             MEAN_NUMBER_CORRECT_21,
             MEAN_NUMBER_CORRECT_22,
             MEAN_NUMBER_CORRECT_23,
             MEAN_NUMBER_CORRECT_24,
             MEAN_NUMBER_CORRECT_25,
             MEAN_NUMBER_CORRECT_26,
             MEAN_NUMBER_CORRECT_27,
             MEAN_NUMBER_CORRECT_28,
             MEAN_NUMBER_CORRECT_29,
             MEAN_NUMBER_CORRECT_30,
             MEAN_NUMBER_CORRECT_31,
             MEAN_PERCENT_CORRECT_1,
             MEAN_PERCENT_CORRECT_2,
             MEAN_PERCENT_CORRECT_3,
             MEAN_PERCENT_CORRECT_4,
             MEAN_PERCENT_CORRECT_5,
             MEAN_PERCENT_CORRECT_6,
             MEAN_PERCENT_CORRECT_7,
             MEAN_PERCENT_CORRECT_8,
             MEAN_PERCENT_CORRECT_9,
             MEAN_PERCENT_CORRECT_10,
             MEAN_PERCENT_CORRECT_11,
             MEAN_PERCENT_CORRECT_12,
             MEAN_PERCENT_CORRECT_13,
             MEAN_PERCENT_CORRECT_14,
             MEAN_PERCENT_CORRECT_15,
             MEAN_PERCENT_CORRECT_16,
             MEAN_PERCENT_CORRECT_17,
             MEAN_PERCENT_CORRECT_18,
             MEAN_PERCENT_CORRECT_19,
             MEAN_PERCENT_CORRECT_20,
             MEAN_PERCENT_CORRECT_21,
             MEAN_PERCENT_CORRECT_22,
             MEAN_PERCENT_CORRECT_23,
             MEAN_PERCENT_CORRECT_24,
             MEAN_PERCENT_CORRECT_25,
             MEAN_PERCENT_CORRECT_26,
             MEAN_PERCENT_CORRECT_27,
             MEAN_PERCENT_CORRECT_28,
             MEAN_PERCENT_CORRECT_29,
             MEAN_PERCENT_CORRECT_30,
             MEAN_PERCENT_CORRECT_31,
             MEAN_IPI_1,
             MEAN_IPI_2,
             MEAN_IPI_3,
             MEAN_IPI_4,
             MEAN_IPI_5,
             MEAN_IPI_6,
             MEAN_IPI_7,
             MEAN_IPI_8,
             MEAN_IPI_9,
             MEAN_IPI_10,
             MEAN_IPI_11,
             MEAN_IPI_12,
             MEAN_IPI_13,
             MEAN_IPI_14,
             MEAN_IPI_15,
             MEAN_IPI_16,
             MEAN_IPI_17,
             MEAN_IPI_18,
             MEAN_IPI_19,
             MEAN_IPI_20,
             MEAN_IPI_21,
             MEAN_IPI_22,
             MEAN_IPI_23,
             MEAN_IPI_24,
             MEAN_IPI_25,
             MEAN_IPI_26,
             MEAN_IPI_27,
             MEAN_IPI_28,
             MEAN_IPI_29,
             MEAN_IPI_30,
             MEAN_IPI_31,
             IPI_DIFFERENCE_1,
             IPI_DIFFERENCE_2,
             IPI_DIFFERENCE_3,
             IPI_DIFFERENCE_4,
             IPI_DIFFERENCE_5,
             IPI_DIFFERENCE_6,
             IPI_DIFFERENCE_7,
             IPI_DIFFERENCE_8,
             IPI_DIFFERENCE_9,
             IPI_DIFFERENCE_10,
             IPI_DIFFERENCE_11,
             IPI_DIFFERENCE_12,
             IPI_DIFFERENCE_13,
             IPI_DIFFERENCE_14,
             IPI_DIFFERENCE_15,
             IPI_DIFFERENCE_16,
             IPI_DIFFERENCE_17,
             IPI_DIFFERENCE_18,
             IPI_DIFFERENCE_19,
             IPI_DIFFERENCE_20,
             IPI_DIFFERENCE_21,
             IPI_DIFFERENCE_22,
             IPI_DIFFERENCE_23,
             IPI_DIFFERENCE_24,
             IPI_DIFFERENCE_25,
             IPI_DIFFERENCE_26,
             IPI_DIFFERENCE_27,
             IPI_DIFFERENCE_28,
             IPI_DIFFERENCE_29,
             IPI_DIFFERENCE_30,
             IPI_DIFFERENCE_31,
             NUMBER_MASTERY_1,
             NUMBER_MASTERY_2,
             NUMBER_MASTERY_3,
             NUMBER_MASTERY_4,
             NUMBER_MASTERY_5,
             NUMBER_MASTERY_6,
             NUMBER_MASTERY_7,
             NUMBER_MASTERY_8,
             NUMBER_MASTERY_9,
             NUMBER_MASTERY_10,
             NUMBER_MASTERY_11,
             NUMBER_MASTERY_12,
             NUMBER_MASTERY_13,
             NUMBER_MASTERY_14,
             NUMBER_MASTERY_15,
             NUMBER_MASTERY_16,
             NUMBER_MASTERY_17,
             NUMBER_MASTERY_18,
             NUMBER_MASTERY_19,
             NUMBER_MASTERY_20,
             NUMBER_MASTERY_21,
             NUMBER_MASTERY_22,
             NUMBER_MASTERY_23,
             NUMBER_MASTERY_24,
             NUMBER_MASTERY_25,
             NUMBER_MASTERY_26,
             NUMBER_MASTERY_27,
             NUMBER_MASTERY_28,
             NUMBER_MASTERY_29,
             NUMBER_MASTERY_30,
             NUMBER_MASTERY_31,
             PERCENT_MASTERY_1,
             PERCENT_MASTERY_2,
             PERCENT_MASTERY_3,
             PERCENT_MASTERY_4,
             PERCENT_MASTERY_5,
             PERCENT_MASTERY_6,
             PERCENT_MASTERY_7,
             PERCENT_MASTERY_8,
             PERCENT_MASTERY_9,
             PERCENT_MASTERY_10,
             PERCENT_MASTERY_11,
             PERCENT_MASTERY_12,
             PERCENT_MASTERY_13,
             PERCENT_MASTERY_14,
             PERCENT_MASTERY_15,
             PERCENT_MASTERY_16,
             PERCENT_MASTERY_17,
             PERCENT_MASTERY_18,
             PERCENT_MASTERY_19,
             PERCENT_MASTERY_20,
             PERCENT_MASTERY_21,
             PERCENT_MASTERY_22,
             PERCENT_MASTERY_23,
             PERCENT_MASTERY_24,
             PERCENT_MASTERY_25,
             PERCENT_MASTERY_26,
             PERCENT_MASTERY_27,
             PERCENT_MASTERY_28,
             PERCENT_MASTERY_29,
             PERCENT_MASTERY_30,
             PERCENT_MASTERY_31,
             NRT_STUDENT_CNT_NRT_NOT_APPL,
             RETEST_FLAG,
             TOTAL_SOCIAL_PROF_MEAN_NCR,
             TOTAL_SOCIAL_PROF_MEAN_SS,
             STRUCTURE_LEVEL,
             ELEMENT_NUMBER,
             g.ispublic,
             sysdate
        FROM ISTEP_DATAMIG.MIG_RESULTS_SUMT G,
             TEST_TP_MAP TTM,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              --subtestid,subtest_name,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE assessmentid = IN_ASSESSMENT_ID) D
       WHERE G.ORG_TSTG_PGM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_RESULTS_SUMT_POPULATE;

  PROCEDURE PROC_RESULTS_STFD_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN

    INSERT \*+APPEND *\
    INTO STFD_FACT
      (STFD_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,

       CONTENTID,
       SUBTESTID,
       GRADEID,
       LEVELID,
       TESTDATE,
       SCALESCORE,
       FREQUENCY,
       PERCENT,
       CUMULATIVEFREQUENCY,
       CUMULATIVEPERCENT,
       NUMBEROFSTUDENTS,
       HIGHSCORE,
       LOWSCORE,
       LOCALPERCENTILE90,
       LOCALPERCENTILE75,
       LOCALPERCENTILE50,
       LOCALPERCENTILE25,
       LOCALPERCENTILE10,
       MEAN,
       STANDARDDEVIATION,
       PASSPLUS_HIGHSCORE,
       PASSPLUS_LOWSCORE,
       PASS_HIGHSCORE,
       PASS_LOWSCORE,
       DNP_HIGHSCORE,
       DNP_LOWSCORE,
       STRUCTURE_LEVEL,
       ELEMENT_NUMBER,
       ISPUBLIC,
       DATETIMESTAMP)

      SELECT RESULTS_STFDID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,
             CONTENTID,
             SUBTESTID,
             GRADEID,
             LEVELID,
             TESTDATE,
             SCALESCORE,
             FREQUENCY,
             PERCENT,
             CUMULATIVEFREQUENCY,
             CUMULATIVEPERCENT,
             NUMBEROFSTUDENTS,
             HIGHSCORE,
             LOWSCORE,
             LOCALPERCENTILE90,
             LOCALPERCENTILE75,
             LOCALPERCENTILE50,
             LOCALPERCENTILE25,
             LOCALPERCENTILE10,
             MEAN,
             STANDARDDEVIATION,
             PASSPLUS_HIGHSCORE,
             PASSPLUS_LOWSCORE,
             PASS_HIGHSCORE,
             PASS_LOWSCORE,
             DNP_HIGHSCORE,
             DNP_LOWSCORE,
             STRUCTURELEVEL,
             ELEMENTNUMBER,
             G.ISPUBLIC,
             SYSDATE

        FROM ISTEP_DATAMIG.MIG_RESULTS_STFD G,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D,
             TEST_TP_MAP TTM
       WHERE G.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND G.ORGTESTINGPGM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_RESULTS_STFD_POPULATE;

  PROCEDURE PROC_RESULTS_ASFD_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+ APPEND *\
    INTO ASFD_FACT
      (ASFD_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       CONTENTID,
       SUBTESTID,
       OBJECTIVEID,
       GRADEID,
       LEVELID,
       TEST_DATE,
       ITEM_NUMBER,
       ITEM_PART,
       TEST_NUMBER,
       POINTS_POSSIBLE,
       TOTAL_NUMBER_STUDENTS,
       NUMBER_0_PTS_OBTAINED,
       PERCENT_0_PTS_OBTAINED,
       NUMBER_1_PTS_OBTAINED,
       PERCENT_1_PTS_OBTAINED,
       NUMBER_2_PTS_OBTAINED,
       PERCENT_2_PTS_OBTAINED,
       NUMBER_3_PTS_OBTAINED,
       PERCENT_3_PTS_OBTAINED,
       NUMBER_4_PTS_OBTAINED,
       PERCENT_4_PTS_OBTAINED,
       NUMBER_5_PTS_OBTAINED,
       PERCENT_5_PTS_OBTAINED,
       NUMBER_6_PTS_OBTAINED,
       PERCENT_6_PTS_OBTAINED,
       NUMBER_COND_CODE_A,
       PERCENT_COND_CODE_A,
       NUMBER_COND_CODE_B,
       PERCENT_COND_CODE_B,
       NUMBER_COND_CODE_C,
       PERCENT_COND_CODE_C,
       NUMBER_COND_CODE_D,
       PERCENT_COND_CODE_D,
       NUMBER_COND_CODE_E,
       PERCENT_COND_CODE_E,
       NUMBER_INVALID_OMITTED,
       PERCENT_INVALID_OMITTED,
       STRUCTURE_LEVEL,
       ELEMENT_NUMBER,
       ISPUBLIC,
       ITEMSETID ,
       DATETIMESTAMP)

      SELECT RESULTS_ASFDID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,
             CONTENTID,
             SUBTESTID,
             OBJECTIVEID,
             GRADEID,
             LEVELID,
             TEST_DATE,
             ITEM_NUMBER,
             ITEM_PART,
             TEST_NUMBER,
             POINTS_POSSIBLE,
             TOTAL_NUMBER_STUDENTS,
             NUMBER_0_PTS_OBTAINED,
             PERCENT_0_PTS_OBTAINED,
             NUMBER_1_PTS_OBTAINED,
             PERCENT_1_PTS_OBTAINED,
             NUMBER_2_PTS_OBTAINED,
             PERCENT_2_PTS_OBTAINED,
             NUMBER_3_PTS_OBTAINED,
             PERCENT_3_PTS_OBTAINED,
             NUMBER_4_PTS_OBTAINED,
             PERCENT_4_PTS_OBTAINED,
             NUMBER_5_PTS_OBTAINED,
             PERCENT_5_PTS_OBTAINED,
             NUMBER_6_PTS_OBTAINED,
             PERCENT_6_PTS_OBTAINED,
             NUMBER_COND_CODE_A,
             PERCENT_COND_CODE_A,
             NUMBER_COND_CODE_B,
             PERCENT_COND_CODE_B,
             NUMBER_COND_CODE_C,
             PERCENT_COND_CODE_C,
             NUMBER_COND_CODE_D,
             PERCENT_COND_CODE_D,
             NUMBER_COND_CODE_E,
             PERCENT_COND_CODE_E,
             NUMBER_INVALID_OMITTED,
             PERCENT_INVALID_OMITTED,
             STRUCTURE_LEVEL,
             ELEMENT_NUMBER,
             G.ISPUBLIC,
             NULL, -- Itemsetid
             SYSDATE

        FROM ISTEP_DATAMIG.MIG_RESULTS_ASFD G,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              OBJECTIVE_NAME,
                              OBJECTIVEID,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D,
             TEST_TP_MAP TTM
       WHERE G.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND g.objective_name = d.objective_name
         AND G.Organization_testing_program = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;

  END PROC_RESULTS_ASFD_POPULATE;

  PROCEDURE PROC_RESULTS_DISA_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN

    INSERT \*+APPEND *\
    INTO DISA_FACT
      (DISA_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       CONTENTID,
       SUBTESTID,
       GRADEID,
       LEVELID,
       DISAGGREGATIONCATEGORYID,
       TESTDATE,
       TOTALTESTED,
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
       MEANSCALESCORE,
       LOWSCOREOBTAINED,
       HIGHSCOREOBTAINED,
       STANDARDDEVIATION,
       STRUCTURE_LEVEL,
       ELEMENT_NUMBER,
       ISPUBLIC,
       DATETIMESTAMP)

      SELECT RESULTS_DISAID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,
             CONTENTID,
             SUBTESTID,
             GRADEID,
             LEVELID,
             DISAGGREGATIONCATEGORYID,
             TESTDATE,
             TOTALTESTED,
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
             MEANSCALESCORE,
             LOWSCOREOBTAINED,
             HIGHSCOREOBTAINED,
             STANDARDDEVIATION,
             STRUCTURE_LEVEL,
             ELEMENT_NUMBER,
             g.ISPUBLIC,
             SYSDATE

        FROM ISTEP_DATAMIG.MIG_RESULTS_DISA G,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D,
             TEST_TP_MAP TTM
       WHERE G.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND G.ORG_TSTG_PGM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_RESULTS_DISA_POPULATE;

  PROCEDURE PROC_RESULTS_SPPR_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN

    INSERT \*+APPEND *\
    INTO SPPR_FACT
      (SPPR_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       CONTENTID,
       SUBTESTID,
       GRADEID,
       LEVELID,
       TESTDATE,
       PASSPLUS_MSS,
       PASSPLUS_SD,
       PASSPLUS_HIGH_SS_OBTAINED,
       PASSPLUS_LOW_SS_OBTAINED,
       PASSPLUS_HIGH_SS_POSSIBLE,
       PASSPLUS_LOW_SS_POSSIBLE,
       PASS_MSS,
       PASS_SD,
       PASS_HIGH_SS_OBTAINED,
       PASS_LOW_SS_OBTAINED,
       PASS_HIGH_SS_POSSIBLE,
       PASS_LOW_SS_POSSIBLE,
       DNP_MSS,
       DNP_SD,
       DNP_HIGH_SS_OBTAINED,
       DNP_LOW_SS_OBTAINED,
       DNP_HIGH_SS_POSSIBLE,
       DNP_LOW_SS_POSSIBLE,
       STRUCTURE_LEVEL,
       ELEMENT_NUMBER,
       ISPUBLIC,
       DATETIMESTAMP)

      SELECT RESULTS_SPPRID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,
             CONTENTID,
             SUBTESTID,
             GRADEID,
             LEVELID,
             TESTDATE,
             PASSPLUS_MSS,
             PASSPLUS_SD,
             PASSPLUS_HIGH_SS_OBTAINED,
             PASSPLUS_LOW_SS_OBTAINED,
             PASSPLUS_HIGH_SS_POSSIBLE,
             PASSPLUS_LOW_SS_POSSIBLE,
             PASS_MSS,
             PASS_SD,
             PASS_HIGH_SS_OBTAINED,
             PASS_LOW_SS_OBTAINED,
             PASS_HIGH_SS_POSSIBLE,
             PASS_LOW_SS_POSSIBLE,
             DNP_MSS,
             DNP_SD,
             DNP_HIGH_SS_OBTAINED,
             DNP_LOW_SS_OBTAINED,
             DNP_HIGH_SS_POSSIBLE,
             DNP_LOW_SS_POSSIBLE,

             STRUCTURELEVEL,
             ELEMENTNUMBER,
             g.ISPUBLIC,
             SYSDATE

        FROM ISTEP_DATAMIG.MIG_RESULTS_SPPR G,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D,
             TEST_TP_MAP TTM
       WHERE G.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND G.ORGTESTINGPGM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_RESULTS_SPPR_POPULATE;

  PROCEDURE PROC_RESULTS_UDTR_SUMM_POP(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO UDTR_SUMM_FACT
      (UDTR_SUMM_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       ENGLANG_ARTS_SUBTESTID,
       MATHEMATICS_SUBTESTID,
       SCIENCE_SUBTESTID,
       SOCIAL_SUBTESTID,
       GRADEID,
       LEVELID,
       TEST_DATE,
       ELA_UND_CNT,
       MATH_UND_CNT,
       SCIENCE_UND_CNT,
       SOCSTUDIES_UND_CNT,
       BIRTH_DATE,
       STUDENTID_A_I,
       FILLER_J,
       ETHNICITY_K,
       SPECIAL_EDUCATION_L,
       GRADE_M,
       SOCIO_ECONOMIC_STATUS_N,
       SECTION_504_O,
       ACCOMMODATIONS_ELA_P,
       ACCOMMODATIONS_MATH_Q,
       LOCAL_USE_R,
       LOCAL_USE_S,
       LOCAL_USE_T,
       EXCEPTIONALITY_U,
       LEP_STUDENT_V,
       FILLER_W,
       SCIENCE_ACCOMMODATIONS_X,
       FILLER_Z,
       ELA1_TEST_SESSION_NAME,
       ELA1_TEST_TYPE,
       ELA1_TESTNOTTAKEN_CNT,
       ELA1_TESTINVALIDATED_CNT,
       ELA2_TEST_SESSION_NAME,
       ELA2_TEST_TYPE,
       ELA2_TESTNOTTAKEN_CNT,
       ELA2_TESTINVALIDATED_CNT,
       ELA3_TEST_SESSION_NAME,
       ELA3_TEST_TYPE,
       ELA3_TESTNOTTAKEN_CNT,
       ELA3_TESTINVALIDATED_CNT,
       ELA4_TEST_SESSION_NAME,
       ELA4_TEST_TYPE,
       ELA4_TESTNOTTAKEN_CNT,
       ELA4_TESTINVALIDATED_CNT,
       ELA5_TEST_SESSION_NAME,
       ELA5_TEST_TYPE,
       ELA5_TESTNOTTAKEN_CNT,
       ELA5_TESTINVALIDATED_CNT,
       ELA6_TEST_SESSION_NAME,
       ELA6_TEST_TYPE,
       ELA6_TESTNOTTAKEN_CNT,
       ELA6_TESTINVALIDATED_CNT,
       MATH1_TEST_SESSION_NAME,
       MATH1_TEST_TYPE,
       MATH1_TESTNOTTAKEN_CNT,
       MATH1_TESTINVALIDATED_CNT,
       MATH2_TEST_SESSION_NAME,
       MATH2_TEST_TYPE,
       MATH2_TESTNOTTAKEN_CNT,
       MATH2_TESTINVALIDATED_CNT,
       MATH3_TEST_SESSION_NAME,
       MATH3_TEST_TYPE,
       MATH3_TESTNOTTAKEN_CNT,
       MATH3_TESTINVALIDATED_CNT,
       MATH4_TEST_SESSION_NAME,
       MATH4_TEST_TYPE,
       MATH4_TESTNOTTAKEN_CNT,
       MATH4_TESTINVALIDATED_CNT,
       MATH5_TEST_SESSION_NAME,
       MATH5_TEST_TYPE,
       MATH5_TESTNOTTAKEN_CNT,
       MATH5_TESTINVALIDATED_CNT,
       SCIENCE1_TEST_SESSION_NAME,
       SCIENCE1_TEST_TYPE,
       SCIENCE1_TESTNOTTAKEN_CNT,
       SCIENCE1_TESTINVALIDATED_CNT,
       SCIENCE2_TEST_SESSION_NAME,
       SCIENCE2_TEST_TYPE,
       SCIENCE2_TESTNOTTAKEN_CNT,
       SCIENCE2_TESTINVALIDATED_CNT,
       SCIENCE3_TEST_SESSION_NAME,
       SCIENCE3_TEST_TYPE,
       SCIENCE3_TESTNOTTAKEN_CNT,
       SCIENCE3_TESTINVALIDATED_CNT,
       SCIENCE4_TEST_SESSION_NAME,
       SCIENCE4_TEST_TYPE,
       SCIENCE4_TESTNOTTAKEN_CNT,
       SCIENCE4_TESTINVALIDATED_CNT,
       ELA1_TESTNOTRECEIVED_CNT,
       ELA2_TESTNOTRECEIVED_CNT,
       ELA3_TESTNOTRECEIVED_CNT,
       ELA4_TESTNOTRECEIVED_CNT,
       ELA5_TESTNOTRECEIVED_CNT,
       ELA6_TESTNOTRECEIVED_CNT,
       MATH1_TESTNOTRECEIVED_CNT,
       MATH2_TESTNOTRECEIVED_CNT,
       MATH3_TESTNOTRECEIVED_CNT,
       MATH4_TESTNOTRECEIVED_CNT,
       MATH5_TESTNOTRECEIVED_CNT,
       SCIENCE1_TESTNOTRECEIVED_CNT,
       SCIENCE2_TESTNOTRECEIVED_CNT,
       SCIENCE3_TESTNOTRECEIVED_CNT,
       SCIENCE4_TESTNOTRECEIVED_CNT,
       SOCIAL1_TEST_SESSION_NAME,
       SOCIAL1_TEST_TYPE,
       SOCIAL1_TESTNOTTAKEN_CNT,
       SOCIAL1_TESTNOTRECEIVED_CNT,
       SOCIAL1_TESTINVALIDATED_CNT,
       SOCIAL2_TEST_SESSION_NAME,
       SOCIAL2_TEST_TYPE,
       SOCIAL2_TESTNOTTAKEN_CNT,
       SOCIAL2_TESTNOTRECEIVED_CNT,
       SOCIAL2_TESTINVALIDATED_CNT,
       SOCIAL3_TEST_SESSION_NAME,
       SOCIAL3_TEST_TYPE,
       SOCIAL3_TESTNOTTAKEN_CNT,
       SOCIAL3_TESTNOTRECEIVED_CNT,
       SOCIAL3_TESTINVALIDATED_CNT,
       SOCIAL4_TEST_SESSION_NAME,
       SOCIAL4_TEST_TYPE,
       SOCIAL4_TESTNOTTAKEN_CNT,
       SOCIAL4_TESTNOTRECEIVED_CNT,
       SOCIAL4_TESTINVALIDATED_CNT,
       MIGRANT_Q,
       MATCH_UNMATCH_U,
       DUPLICATE_V,
       CTB_USE_W,
       SPECIAL_CODE_X,
       SPECIAL_CODE_Y,
       SPECIAL_CODE_Z,
       STRUCTURE_LEVEL,
       BOTHELAMATH_UND_CNT,
       ELA1_IMAST_CNT,
       ELA2_IMAST_CNT,
       ELA3_IMAST_CNT,
       ELA4_IMAST_CNT,
       ELA5_IMAST_CNT,
       ELA6_IMAST_CNT,
       MATH1_IMAST_CNT,
       MATH2_IMAST_CNT,
       MATH3_IMAST_CNT,
       MATH4_IMAST_CNT,
       MATH5_IMAST_CNT,
       SCIENCE1_IMAST_CNT,
       SCIENCE2_IMAST_CNT,
       SCIENCE3_IMAST_CNT,
       SCIENCE4_IMAST_CNT,
       SOCIAL1_IMAST_CNT,
       SOCIAL2_IMAST_CNT,
       SOCIAL3_IMAST_CNT,
       SOCIAL4_IMAST_CNT,
       ISPUBLIC,
       DATETIMESTAMP

       )

      SELECT RESULTS_UDTR_SUMMARYID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,

             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGANIZATION_TEST_PROGRAM
                 AND SUBTEST_CODE = 'ELA') ENGLANG_ARTS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGANIZATION_TEST_PROGRAM
                 AND M.SUBTEST_CODE = 'MATH') MATHEMATICS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGANIZATION_TEST_PROGRAM
                 AND M.SUBTEST_CODE = 'SCI') SCIENCE_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.ORGANIZATION_TEST_PROGRAM
                 AND M.SUBTEST_CODE = 'SS') SOCIAL_SUBTESTID,
             D.GRADEID,
             D.LEVELID,
             TEST_DATE,
             ELA_UNDCOUNT,
             MATH_UNDCOUNT,
             SCIENCE_UNDCOUNT,
             SOCSTUDIES_UNDCOUNT,
             BIRTH_DATE,
             STUDENT_ID_STUDENT_TEST_NUMBER,
             FILLER_J,
             ETHNICITY_K,
             SPECIAL_EDUCATION_L,
             GRADE_M,
             SOCIO_ECONOMIC_STATUS_N,
             SECTION_504_O,
             ACCOMMODATIONS_E_LA_P,
             ACCOMMODATIONS_MATH_Q,
             LOCAL_USE_R,
             LOCAL_USE_S,
             LOCAL_USE_T,
             EXCEPTIONALITY_U,
             LEP_STUDENT_V,
             FILLER_W,
             SCIENCE_ACCOMMODATIONS_X,
             FILLER_Z,
             ELA1_TEST_SESSION_NAME,
             ELA1_TEST_TYPE,
             ELA1_TESTNOTTAKENCOUNT,
             ELA1_TESTINVALIDATEDCOUNT,
             ELA2_TEST_SESSION_NAME,
             ELA2_TEST_TYPE,
             ELA2_TESTNOTTAKENCOUNT,
             ELA2_TESTINVALIDATEDCOUNT,
             ELA3_TEST_SESSION_NAME,
             ELA3_TEST_TYPE,
             ELA3_TESTNOTTAKENCOUNT,
             ELA3_TESTINVALIDATEDCOUNT,
             ELA4_TEST_SESSION_NAME,
             ELA4_TEST_TYPE,
             ELA4_TESTNOTTAKENCOUNT,
             ELA4_TESTINVALIDATEDCOUNT,
             ELA5_TEST_SESSION_NAME,
             ELA5_TEST_TYPE,
             ELA5_TESTNOTTAKENCOUNT,
             ELA5_TESTINVALIDATEDCOUNT,
             ELA6_TEST_SESSION_NAME,
             ELA6_TEST_TYPE,
             ELA6_TESTNOTTAKENCOUNT,
             ELA6_TESTINVALIDATEDCOUNT,
             MATH1_TEST_SESSION_NAME,
             MATH1_TEST_TYPE,
             MATH1_TESTNOTTAKENCOUNT,
             MATH1_TESTINVALIDATEDCOUNT,
             MATH2_TEST_SESSION_NAME,
             MATH2_TEST_TYPE,
             MATH2_TESTNOTTAKENCOUNT,
             MATH2_TESTINVALIDATEDCOUNT,
             MATH3_TEST_SESSION_NAME,
             MATH3_TEST_TYPE,
             MATH3_TESTNOTTAKENCOUNT,
             MATH3_TESTINVALIDATEDCOUNT,
             MATH4_TEST_SESSION_NAME,
             MATH4_TEST_TYPE,
             MATH4_TESTNOTTAKENCOUNT,
             MATH4_TESTINVALIDATEDCOUNT,
             MATH5_TEST_SESSION_NAME,
             MATH5_TEST_TYPE,
             MATH5_TESTNOTTAKENCOUNT,
             MATH5_TESTINVALIDATEDCOUNT,
             SCIENCE1_TEST_SESSION_NAME,
             SCIENCE1_TEST_TYPE,
             SCIENCE1_TESTNOTTAKENCOUNT,
             SCIENCE1_TESTINVALIDATEDCOUNT,
             SCIENCE2_TEST_SESSION_NAME,
             SCIENCE2_TEST_TYPE,
             SCIENCE2_TESTNOTTAKENCOUNT,
             SCIENCE2_TESTINVALIDATEDCOUNT,
             SCIENCE3_TEST_SESSION_NAME,
             SCIENCE3_TEST_TYPE,
             SCIENCE3_TESTNOTTAKENCOUNT,
             SCIENCE3_TESTINVALIDATEDCOUNT,
             SCIENCE4_TEST_SESSION_NAME,
             SCIENCE4_TEST_TYPE,
             SCIENCE4_TESTNOTTAKENCOUNT,
             SCIENCE4_TESTINVALIDATEDCOUNT,
             ELA1_TESTNOTRECEIVEDCOUNT,
             ELA2_TESTNOTRECEIVEDCOUNT,
             ELA3_TESTNOTRECEIVEDCOUNT,
             ELA4_TESTNOTRECEIVEDCOUNT,
             ELA5_TESTNOTRECEIVEDCOUNT,
             ELA6_TESTNOTRECEIVEDCOUNT,
             MATH1_TESTNOTRECEIVEDCOUNT,
             MATH2_TESTNOTRECEIVEDCOUNT,
             MATH3_TESTNOTRECEIVEDCOUNT,
             MATH4_TESTNOTRECEIVEDCOUNT,
             MATH5_TESTNOTRECEIVEDCOUNT,
             SCIENCE1_TESTNOTRECEIVEDCOUNT,
             SCIENCE2_TESTNOTRECEIVEDCOUNT,
             SCIENCE3_TESTNOTRECEIVEDCOUNT,
             SCIENCE4_TESTNOTRECEIVEDCOUNT,
             SOCIAL1_TEST_SESSION_NAME,
             SOCIAL1_TEST_TYPE,
             SOCIAL1_TESTNOTTAKENCOUNT,
             SOCIAL1_TESTNOTRECEIVEDCOUNT,
             SOCIAL1_TESTINVALIDATEDCOUNT,
             SOCIAL2_TEST_SESSION_NAME,
             SOCIAL2_TEST_TYPE,
             SOCIAL2_TESTNOTTAKENCOUNT,
             SOCIAL2_TESTNOTRECEIVEDCOUNT,
             SOCIAL2_TESTINVALIDATEDCOUNT,
             SOCIAL3_TEST_SESSION_NAME,
             SOCIAL3_TEST_TYPE,
             SOCIAL3_TESTNOTTAKENCOUNT,
             SOCIAL3_TESTNOTRECEIVEDCOUNT,
             SOCIAL3_TESTINVALIDATEDCOUNT,
             SOCIAL4_TEST_SESSION_NAME,
             SOCIAL4_TEST_TYPE,
             SOCIAL4_TESTNOTTAKENCOUNT,
             SOCIAL4_TESTNOTRECEIVEDCOUNT,
             SOCIAL4_TESTINVALIDATEDCOUNT,
             MIGRANT_Q,
             MATCH_UNMATCH_U,
             DUPLICATE_V,
             CTB_USE_W,
             SPECIAL_CODE_X,
             SPECIAL_CODE_Y,
             SPECIAL_CODE_Z,
             STRUCTURE_LEVEL,
             BOTHELAMATH_UNDCOUNT,
             ELA1_IMASTCOUNT,
             ELA2_IMASTCOUNT,
             ELA3_IMASTCOUNT,
             ELA4_IMASTCOUNT,
             ELA5_IMASTCOUNT,
             ELA6_IMASTCOUNT,
             MATH1_IMASTCOUNT,
             MATH2_IMASTCOUNT,
             MATH3_IMASTCOUNT,
             MATH4_IMASTCOUNT,
             MATH5_IMASTCOUNT,
             SCIENCE1_IMASTCOUNT,
             SCIENCE2_IMASTCOUNT,
             SCIENCE3_IMASTCOUNT,
             SCIENCE4_IMASTCOUNT,
             SOCIAL1_IMASTCOUNT,
             SOCIAL2_IMASTCOUNT,
             SOCIAL3_IMASTCOUNT,
             SOCIAL4_IMASTCOUNT,
             G.ISPUBLIC,
             SYSDATE
        FROM ISTEP_DATAMIG.MIG_RESULTS_UDTR_SUMMARY G,
             TEST_TP_MAP TTM,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              --subtestid,subtest_name,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D
       WHERE G.ORGANIZATION_TEST_PROGRAM = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_RESULTS_UDTR_SUMM_POP;

  PROCEDURE PROC_RESULTS_UDTR_ROSTER_POP(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO UDTR_ROSTER_FACT
      (UDTR_ROSTER_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       ENGLANG_ARTS_SUBTESTID,
       MATHEMATICS_SUBTESTID,
       SCIENCE_SUBTESTID,
       SOCIAL_SUBTESTID,
       GRADEID,
       LEVELID,
       STUDENT_BIO_ID,
       REPORT_MODE,
       TEST_DATE,
       STUDENT_ELEMENT_NUMBER,
       STUDENT_LAST_NAME,
       STUDENT_FIRST_NAME,
       STUDENT_MIDDLE_INITIAL,
       ELA_PFCATEGORY,
       MATH_PFCATEGORY,
       SCIENCE_PFCATEGORY,
       SOCSTUDIES_PFCATEGORY,
       BIRTH_DATE,
       STUDENTID_A_I,
       FILLER_J,
       ETHNICITY_K,
       SPECIAL_EDUCATION_L,
       GRADE_M,
       SOCIO_ECONOMIC_STATUS_N,
       SECTION_504_O,
       ACCOMMODATIONS_ELA_P,
       ACCOMMODATIONS_MATH_Q,
       LOCAL_USE_R,
       LOCAL_USE_S,
       LOCAL_USE_T,
       EXCEPTIONALITY_U,
       LEP_STUDENT_V,
       FILLER_W,
       SCIENCE_ACCOMMODATIONS_X,
       FILLER_Z,
       ELA1_TEST_SESSION_NAME,
       ELA1_TEST_TYPE,
       ELA1_TESTRESULT,
       ELA2_TEST_SESSION_NAME,
       ELA2_TEST_TYPE,
       ELA2_TESTRESULT,
       ELA3_TEST_SESSION_NAME,
       ELA3_TEST_TYPE,
       ELA3_TESTRESULT,
       ELA4_TEST_SESSION_NAME,
       ELA4_TEST_TYPE,
       ELA4_TESTRESULT,
       ELA5_TEST_SESSION_NAME,
       ELA5_TEST_TYPE,
       ELA5_TESTRESULT,
       ELA6_TEST_SESSION_NAME,
       ELA6_TEST_TYPE,
       ELA6_TESTRESULT,
       MATH1_TEST_SESSION_NAME,
       MATH1_TEST_TYPE,
       MATH1_TESTRESULT,
       MATH2_TEST_SESSION_NAME,
       MATH2_TEST_TYPE,
       MATH2_TESTRESULT,
       MATH3_TEST_SESSION_NAME,
       MATH3_TEST_TYPE,
       MATH3_TESTRESULT,
       MATH4_TEST_SESSION_NAME,
       MATH4_TEST_TYPE,
       MATH4_TESTRESULT,
       MATH5_TEST_SESSION_NAME,
       MATH5_TEST_TYPE,
       MATH5_TESTRESULT,
       SCIENCE1_TEST_SESSION_NAME,
       SCIENCE1_TEST_TYPE,
       SCIENCE1_TESTRESULT,
       SCIENCE2_TEST_SESSION_NAME,
       SCIENCE2_TEST_TYPE,
       SCIENCE2_TESTRESULT,
       SCIENCE3_TEST_SESSION_NAME,
       SCIENCE3_TEST_TYPE,
       SCIENCE3_TESTRESULT,
       SCIENCE4_TEST_SESSION_NAME,
       SCIENCE4_TEST_TYPE,
       SCIENCE4_TESTRESULT,
       SOCIAL1_TEST_SESSION_NAME,
       SOCIAL1_TEST_TYPE,
       SOCIAL1_TESTRESULT,
       SOCIAL2_TEST_SESSION_NAME,
       SOCIAL2_TEST_TYPE,
       SOCIAL2_TESTRESULT,
       SOCIAL3_TEST_SESSION_NAME,
       SOCIAL3_TEST_TYPE,
       SOCIAL3_TESTRESULT,
       SOCIAL4_TEST_SESSION_NAME,
       SOCIAL4_TEST_TYPE,
       SOCIAL4_TESTRESULT,
       MIGRANT_Q,
       MATCH_UNMATCH_U,
       DUPLICATE_V,
       CTB_USE_W,
       SPECIAL_CODE_X,
       SPECIAL_CODE_Y,
       SPECIAL_CODE_Z,
       STRUCTURE_LEVEL,
       NA_PFCATEGORY,
       ISPUBLIC,
       DATETIMESTAMP

       )

      SELECT RESULTS_UDTR_ROSTERID,
             CASE
               WHEN G.DISTRICTID = 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = G.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN G.DISTRICTID <> 0 AND G.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = G.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,

             TTM. ADMINID,

             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.org_test_program
                 AND SUBTEST_CODE = 'ELA') ENGLANG_ARTS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.org_test_program
                 AND M.SUBTEST_CODE = 'MATH') MATHEMATICS_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.org_test_program
                 AND M.SUBTEST_CODE = 'SCI') SCIENCE_SUBTESTID,
             (SELECT DISTINCT SUBTESTID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP M, TEST_TP_MAP T
               WHERE M.GRADEID = D.GRADEID
                 AND M.ASSESSMENTID = D.ASSESSMENTID
                 AND M.ASSESSMENTID = T.ASSESSMENTID
                 AND T.TESTID = G.TESTID
                 AND T.TP_CODE = G.org_test_program
                 AND M.SUBTEST_CODE = 'SS') SOCIAL_SUBTESTID,
             D.GRADEID,
             D.LEVELID,
             1,
             REPORT_MODE,
             TEST_DATE,
             STUDENT_ELEMENT_NUMBER,
             STUDENT_LAST_NAME,
             STUDENT_FIRST_NAME,
             STUDENT_MIDDLE_INITIAL,
             ELA_PFCATEGORY,
             MATH_PFCATEGORY,
             SCIENCE_PFCATEGORY,
             SOCSTUDIES_PFCATEGORY,
             BIRTH_DATE,
             STUDENTID_A_I,
             FILLER_J,
             ETHNICITY_K,
             SPECIAL_EDUCATION_L,
             GRADE_M,
             SOCIO_ECONOMIC_STATUS_N,
             SECTION_504_O,
             ACCOMMODATIONS_ELA_P,
             ACCOMMODATIONS_MATH_Q,
             LOCAL_USE_R,
             LOCAL_USE_S,
             LOCAL_USE_T,
             EXCEPTIONALITY_U,
             LEP_STUDENT_V,
             FILLER_W,
             SCIENCE_ACCOMMODATIONS_X,
             FILLER_Z,
             ELA1_TEST_SESSION_NAME,
             ELA1_TEST_TYPE,
             ELA1_TESTRESULT,
             ELA2_TEST_SESSION_NAME,
             ELA2_TEST_TYPE,
             ELA2_TESTRESULT,
             ELA3_TEST_SESSION_NAME,
             ELA3_TEST_TYPE,
             ELA3_TESTRESULT,
             ELA4_TEST_SESSION_NAME,
             ELA4_TEST_TYPE,
             ELA4_TESTRESULT,
             ELA5_TEST_SESSION_NAME,
             ELA5_TEST_TYPE,
             ELA5_TESTRESULT,
             ELA6_TEST_SESSION_NAME,
             ELA6_TEST_TYPE,
             ELA6_TESTRESULT,
             MATH1_TEST_SESSION_NAME,
             MATH1_TEST_TYPE,
             MATH1_TESTRESULT,
             MATH2_TEST_SESSION_NAME,
             MATH2_TEST_TYPE,
             MATH2_TESTRESULT,
             MATH3_TEST_SESSION_NAME,
             MATH3_TEST_TYPE,
             MATH3_TESTRESULT,
             MATH4_TEST_SESSION_NAME,
             MATH4_TEST_TYPE,
             MATH4_TESTRESULT,
             MATH5_TEST_SESSION_NAME,
             MATH5_TEST_TYPE,
             MATH5_TESTRESULT,
             SCIENCE1_TEST_SESSION_NAME,
             SCIENCE1_TEST_TYPE,
             SCIENCE1_TESTRESULT,
             SCIENCE2_TEST_SESSION_NAME,
             SCIENCE2_TEST_TYPE,
             SCIENCE2_TESTRESULT,
             SCIENCE3_TEST_SESSION_NAME,
             SCIENCE3_TEST_TYPE,
             SCIENCE3_TESTRESULT,
             SCIENCE4_TEST_SESSION_NAME,
             SCIENCE4_TEST_TYPE,
             SCIENCE4_TESTRESULT,
             SOCIAL1_TEST_SESSION_NAME,
             SOCIAL1_TEST_TYPE,
             SOCIAL1_TESTRESULT,
             SOCIAL2_TEST_SESSION_NAME,
             SOCIAL2_TEST_TYPE,
             SOCIAL2_TESTRESULT,
             SOCIAL3_TEST_SESSION_NAME,
             SOCIAL3_TEST_TYPE,
             SOCIAL3_TESTRESULT,
             SOCIAL4_TEST_SESSION_NAME,
             SOCIAL4_TEST_TYPE,
             SOCIAL4_TESTRESULT,
             MIGRANT_Q,
             MATCH_UNMATCH_U,
             DUPLICATE_V,
             CTB_USE_W,
             SPECIAL_CODE_X,
             SPECIAL_CODE_Y,
             SPECIAL_CODE_Z,
             STRUCTURE_LEVEL,
             NA_PFCATEGORY,
             g.ISPUBLIC,
             sysdate

        FROM ISTEP_DATAMIG.MIG_RESULTS_UDTR_ROSTER G,
             TEST_TP_MAP TTM,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              --subtestid,subtest_name,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D
       WHERE G.org_test_program = TTM.TP_CODE
         AND G.TESTID = TTM.TESTID
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_RESULTS_UDTR_ROSTER_POP;

  PROCEDURE PROC_RESULT_ACAD_SUMM_POP(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+ APPEND *\
    INTO ACAD_STD_SUMM_FACT
      (AS_SUMM_ID,
       ORG_NODEID,
       CUST_PROD_ID,
       ADMINID,
       CONTENTID,
       SUBTESTID,
       OBJECTIVEID,
       GRADEID,
       LEVELID,
       CTB_OBJECTIVE_CODE,
       CTB_OBJECTIVE_TITLE,
       ITEMTYPE,
       PONUMBERSPOSSIBLE,
       IPI_AT_PASS,
       STATE_OBJECTIVETITLE,
       STATE_MEANNUMBERCORRECT,
       STATE_MEANPERCENTCORRECT,
       STATE_MEANIPI,
       STATE_IPIDIFFERENCE,
       STATE_NUMBERMASTERY,
       STATE_PERCENTMASTERY,
       CORP_OBJECTIVETITLE,
       CORP_MEANNUMBERCORRECT,
       CORP_MEANPERCENTCORRECT,
       CORP_MEANIPI,
       CORP_IPIDIFFERENCE,
       CORP_NUMBERMASTERY,
       CORP_PERCENTMASTERY,
       SCHOOL_OBJECTIVETITLE,
       SCHOOL_MEANNUMBERCORRECT,
       SCHOOL_MEANPERCENTCORRECT,
       SCHOOL_MEANIPI,
       SCHOOL_IPIDIFFERENCE,
       SCHOOL_NUMBERMASTERY,
       SCHOOL_PERCENTMASTERY,
       ISPUBLIC,
       DATETIMESTAMP)

      SELECT RESULTS_ACADSTANDARDSUMID,
             CASE
               WHEN RE_VW.DISTRICTID = 0 AND RE_VW.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID = (SELECT G.ORG_PRISM_ID
                                        FROM ISTEP_DATAMIG.RECORD_MIG G
                                       WHERE G.INODE_TYPE = 'STATE'
                                      \*AND G.INODE_ID = 1  *\
                                      )
                    AND ORG_NODE_LEVEL = 1)
               WHEN RE_VW.DISTRICTID <> 0 AND RE_VW.SCHOOLID = 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'DISTRICT'
                            AND G.INODE_ID = RE_VW.DISTRICTID)
                    AND ORG_NODE_LEVEL = 2)
               WHEN RE_VW.DISTRICTID <> 0 AND RE_VW.SCHOOLID <> 0 THEN
                (SELECT ORG_NODEID
                   FROM ORG_NODE_DIM
                  WHERE ORG_NODEID =
                        (SELECT G.ORG_PRISM_ID
                           FROM ISTEP_DATAMIG.RECORD_MIG G
                          WHERE G.INODE_TYPE = 'SCHOOL'
                            AND G.INODE_ID = RE_VW.SCHOOLID)
                    AND ORG_NODE_LEVEL = 3)
             END AS ORG_NODEID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = RE_VW.PRODUCTID
                 AND A.ADMINID = RE_VW.ADMINID) AS CUST_PROD_ID,

             RE_VW.ADMINID,
             CONTENTID,
             SUBTESTID,
             OBJECTIVEID,
             COALESCE(GRADEID,
                      (SELECT GRADEID
                         FROM ISTEP_DATAMIG.MIG_GRADELEVEL GL,
                              (SELECT DISTINCT GRADE_CODE, GRADEID
                                 FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
                                WHERE ASSESSMENTID = IN_ASSESSMENT_ID) B
                        WHERE B.GRADE_CODE = GL."GradeLevelCode"
                          AND "GradeLevelID" = RE_VW.GRADELEVELID)) AS GRADEID,
             LEVELID,
             RE_VW.CTB_OBJECTIVE_CODE,
             CTB_OBJECTIVE_TITLE,
             ITEMTYPE,
             POINTSPOSSIBLE,
             IPI_AT_PASS,
             STATE_OBJECTIVETITLE,
             STATE_MEANNUMBERCORRECT,
             STATE_MEANPERCENTCORRECT,
             STATE_MEANIPI,
             STATE_IPIDIFFERENCE,
             STATE_NUMBERMASTERY,
             STATE_PERCENTMASTERY,
             CORP_OBJECTIVETITLE,
             CORP_MEANNUMBERCORRECT,
             CORP_MEANPERCENTCORRECT,
             CORP_MEANIPI,
             CORP_IPIDIFFERENCE,
             CORP_NUMBERMASTERY,
             CORP_PERCENTMASTERY,
             SCHOOL_OBJECTIVETITLE,
             SCHOOL_MEANNUMBERCORRECT,
             SCHOOL_MEANPERCENTCORRECT,
             SCHOOL_MEANIPI,
             SCHOOL_IPIDIFFERENCE,
             SCHOOL_NUMBERMASTERY,
             SCHOOL_PERCENTMASTERY,
             ISPUBLIC,
             SYSDATE
        FROM (SELECT DISTINCT MRACAD.*, ttm.adminid, ttm.productid
                FROM ISTEP_DATAMIG.MIG_RESULTS_ACADSTDSUMMARY MRACAD,
                     TEST_TP_MAP                              TTM
               WHERE MRACAD.TESTID = TTM.TESTID
                    \* AND MRACAD.ISPUBLIC = TTM.ISPUBLIC*\
                 AND ASSESSMENTID = IN_ASSESSMENT_ID) RE_VW,

             (SELECT DISTINCT VSGOMP.ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              VSGOMP.OBJECTIVE_CODE,
                              OBJECTIVE_NAME,
                              OBJECTIVEID,
                              GRADEID,
                              LEVELID,
                              GL."GradeLevelID" AS GRADELEVELID,
                              MS."SubjectID" AS SUBJECTID,
                              MRP.CTB_OBJECTIVE_CODE,
                              MRP.TESTID

                FROM ISTEP_DATAMIG.MIG_GRADELEVEL GL,
                     ISTEP_DATAMIG.MIG_RESULTS_PEID MRP,
                     VW_SUBTEST_GRADE_OBJECTIVE_MAP VSGOMP,
                     ISTEP_DATAMIG.MIG_SUBJECT MS,
                     (SELECT DISTINCT TESTID, ASSESSMENTID FROM TEST_TP_MAP) TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 AND VSGOMP.ASSESSMENTID = TTM.ASSESSMENTID
                 AND VSGOMP.GRADE_CODE = GL."GradeLevelCode"
                 AND GL."GradeLevelID" = MRP.GRADELEVELID
                 AND VSGOMP.SUBTEST_CODE = MS."SubjectCode"
                 AND MS."SubjectID" = MRP.SUBJECTID
                 AND MRP.CTB_OBJECTIVE_CODE = VSGOMP.OBJECTIVE_CODE) SUB_OBJ_VW

       WHERE RE_VW.TESTID = SUB_OBJ_VW.TESTID(+)
         AND RE_VW.SUBJECTID = SUB_OBJ_VW.SUBJECTID(+)
         AND RE_VW.GRADELEVELID = SUB_OBJ_VW.GRADELEVELID(+)
         AND RE_VW.CTB_OBJECTIVE_CODE = SUB_OBJ_VW.CTB_OBJECTIVE_CODE(+);

    COMMIT;

  END PROC_RESULT_ACAD_SUMM_POP;

  PROCEDURE PROC_RESULTS_PEID_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO PEID_FACT
      (RESULTS_PEIDID,
       GRADEID,
       SUBTESTID,
       ADMINID,
       CUST_PROD_ID,
       CTB_PROJECT_NAME,
       CTB_PROJECT_ID,
       CTB_MODULE_NAME,
       CTB_MODULE_ID,
       CTB_LEVEL_ID,
       CTB_PAGE_NO,
       CTB_CONTENT_AREA_TITLE,
       CTB_CONTENT_ID,
       CTB_ITEM_NO,
       CTB_PART,
       CTB_ITEM_ID,
       CTB_ITEM_TYPE,
       CTB_ANSWER_KEY,
       CTB_RIGHTS_KEY,
       CTB_NO_SCORE_POINTS,
       CTB_NAC,
       CTB_ITEMSYS_DESC,
       CTB_DESC,
       CTB_GI_RESPONSE,
       CTB_OBJECTIVE_ID,
       CTB_OBJECTIVE_CODE,
       CTB_OBJECTIVE_TITLE,
       CTB_SUBSKILL_ID,
       CTB_SUBSKILL_CODE,
       CTB_SUBSKILL_TITLE,
       CTB_THINK_ID,
       CTB_THINK_TITLE,
       CTB_SUBTEST_ID,
       CTB_SUBTEST_TITLE,
       CTB_SUBTEST_CODE,
       DATETIMESTAMP)

      SELECT G.RESULTS_PEIDID,
             D.GRADEID,
             D.SUBTESTID,
             TTM.ADMINID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,
             G.CTB_PROJECT_NAME,
             G.CTB_PROJECT_ID,
             G.CTB_MODULE_NAME,
             G.CTB_MODULE_ID,
             G.CTB_LEVEL_ID,
             G.CTB_PAGE_NO,
             G.CTB_CONTENT_AREA_TITLE,
             G.CTB_CONTENT_ID,
             G.CTB_ITEM_NO,
             G.CTB_PART,
             G.CTB_ITEM_ID,
             G.CTB_ITEM_TYPE,
             G.CTB_ANSWER_KEY,
             G.CTB_RIGHTS_KEY,
             G.CTB_NO_SCORE_POINTS,
             G.CTB_NAC,
             G.CTB_ITEMSYS_DESC,
             G.CTB_DESC,
             G.CTB_GI_RESPONSE,
             G.CTB_OBJECTIVE_ID,
             G.CTB_OBJECTIVE_CODE,
             G.CTB_OBJECTIVE_TITLE,
             G.CTB_SUBSKILL_ID,
             G.CTB_SUBSKILL_CODE,
             G.CTB_SUBSKILL_TITLE,
             G.CTB_THINK_ID,
             G.CTB_THINK_TITLE,
             G.CTB_SUBTEST_ID,
             G.CTB_SUBTEST_TITLE,
             G.CTB_SUBTEST_CODE,
             SYSDATE
        FROM ISTEP_DATAMIG.MIG_RESULTS_PEID G,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D,
             (SELECT DISTINCT ISOFTYEARID,
                              TESTID,
                              ASSESSMENTID,
                              ADMINID,
                              PRODUCTID
                FROM TEST_TP_MAP) TTM
       WHERE G.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
    --AND subtest_name ='English-Language Arts' ;
  END PROC_RESULTS_PEID_POPULATE;

  PROCEDURE PROC_CUTSCR_SCLESCR_POPULATE(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN
    INSERT \*+APPEND*\
    INTO CUTSCORESCALESCORE
      (CUTSCORESCALESCOREID,
       GRADEID,
       LEVELID,
       SUBTESTID,
       CUST_PROD_ID,
       LOSS,
       HOSS,
       PASS,
       PASSPLUS)

      SELECT G.CUTSCORESCALESCOREID,
             D.GRADEID,
             D.LEVELID,
             D.SUBTESTID,
             (SELECT CUST_PROD_ID
                FROM CUST_PRODUCT_LINK A
               WHERE A.PRODUCTID = TTM.PRODUCTID
                 AND A.ADMINID = TTM.ADMINID) AS CUST_PROD_ID,
             G.LOSS,
             G.HOSS,
             G.PASS,
             G.PASSPLUS
        FROM ISTEP_DATAMIG.MIG_CUTSCORESCALESCORE G,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE ASSESSMENTID = IN_ASSESSMENT_ID) D,
             (SELECT DISTINCT ISOFTYEARID,
                              TESTID,
                              ASSESSMENTID,
                              ADMINID,
                              PRODUCTID
                FROM TEST_TP_MAP) TTM
       WHERE G.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND G.GRADELEVELID = GL."GradeLevelID"
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND G.TESTID = TTM.TESTID
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;
    COMMIT;
  END PROC_CUTSCR_SCLESCR_POPULATE;

  PROCEDURE PRC_DISAGGREGATION_CATEGR_TYP(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN

    INSERT \*+APPEND*\
    INTO DISAGGREGATION_CATEGORY_TYPE
      select mig."DisaggregationCategoryTypeID",
             mig."DisaggregationCategoryTypeName",
             mig."OrderBy",
             (select CUST_PROD_ID
                from cust_product_link
               WHERE productid = TTM.PRODUCTID
                 AND adminid = TTM.ADMINID) CUST_PROD_ID,
                       SYSDATE ,
             SYSDATE
        from ISTEP_DATAMIG.mig_disaggregationcategorytype mig,
             (select DISTINCT ADMINID, PRODUCTID, testid, ASSESSMENTID
                from TEST_TP_MAP) TTM
       where TTM.testid = mig."TestID"
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;

    COMMIT;

  END PRC_DISAGGREGATION_CATEGR_TYP;

  PROCEDURE PRC_DISAGGREGATION_CATEGORY(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN

    INSERT \*+APPEND*\
    INTO DISAGGREGATION_CATEGORY
      select mig."DisaggregationCategoryID",
             mig."DisaggregationCategoryTypeID",
             mig."DisaggregationCategoryCode",
             mig."DisaggregationCategoryName",
             mig."OrderBy",
             (select CUST_PROD_ID
                from cust_product_link
               WHERE productid = TTM.PRODUCTID
                 AND adminid = TTM.ADMINID) CUST_PROD_ID,
             SYSDATE ,
             SYSDATE
        from ISTEP_DATAMIG.mig_disaggregationcategory mig,
             (select DISTINCT ADMINID, PRODUCTID, testid, ASSESSMENTID
                from TEST_TP_MAP) TTM
       where TTM.testid = mig."TestID"
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID;

    COMMIT;

  END PRC_DISAGGREGATION_CATEGORY;

  PROCEDURE PRC_CUTSCOREIPI(IN_ASSESSMENT_ID NUMBER) IS
  BEGIN

    INSERT \*+APPEND*\
    INTO cutscoreipi
      select mig.CUTSCOREIPIID,
             D.GRADEID GRADEID,
             D.SUBTESTID,
             (select CUST_PROD_ID
                from cust_product_link
               WHERE productid = TTM.PRODUCTID
                 AND adminid = TTM.ADMINID) CUST_PROD_ID,
             mig.STANDARD,
             mig.IPI_AT_PASS,
             SYSDATE ,
             SYSDATE
        from istep_datamig.mig_cutscoreipi mig,
             (select DISTINCT ADMINID, PRODUCTID, testid, ASSESSMENTID
                from TEST_TP_MAP) TTM,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
               WHERE assessmentid = IN_ASSESSMENT_ID) D
       where TTM.testid = mig.TESTID
         AND TTM.ASSESSMENTID = IN_ASSESSMENT_ID
         AND mig.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND mig.GRADELEVELID = GL."GradeLevelID"
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND GL."GradeLevelCode" = D.GRADE_CODE;

    COMMIT;

  END PRC_CUTSCOREIPI;

  PROCEDURE PRC_ASFD_ORDERBY IS
  BEGIN

    INSERT \*+APPEND*\
    INTO ASFD_ORDERBY
      select mig.ASFD_OrderByID,
             D.OBJECTIVEID,
             mig.Objective_Name,
             D.GRADEID GRADEID,
             D.SUBTESTID,
             mig.OrderBy,
             (select CUST_PROD_ID
                from cust_product_link
               WHERE productid = TTM.PRODUCTID
                 AND adminid = TTM.ADMINID) CUST_PROD_ID
        from ISTEP_DATAMIG.mig_ASFD_ORDERBY mig,
             (select DISTINCT ADMINID, PRODUCTID, testid, ASSESSMENTID
                from TEST_TP_MAP) TTM,
             ISTEP_DATAMIG.MIG_SUBJECT MS,
             ISTEP_DATAMIG.MIG_GRADELEVEL GL,
             (SELECT DISTINCT ASSESSMENTID,
                              SUBTESTID,
                              CONTENTID,
                              SUBTEST_CODE,
                              GRADE_CODE,
                              OBJECTIVE_NAME,
                              OBJECTIVEID,
                              GRADEID,
                              LEVELID
                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP) D
       where TTM.testid = mig.TestID
         AND mig.SUBJECTID = MS."SubjectID"
         AND MS."SubjectCode" = D.SUBTEST_CODE
         AND mig.GRADELEVELID = GL."GradeLevelID"
         AND D.ASSESSMENTID = TTM.ASSESSMENTID
         AND GL."GradeLevelCode" = D.GRADE_CODE
         AND trim(upper(mig.objective_name)) =
             trim(upper(replace(d.objective_name, '†')));

    COMMIT;

  END PRC_ASFD_ORDERBY;

  PROCEDURE PRC_GRADE_SELECTION IS
  BEGIN

  EXECUTE IMMEDIATE 'TRUNCATE TABLE grade_selection_lookup';

    INSERT \*+APPEND*\
    INTO grade_selection_lookup
      SELECT DISTINCT OLN.ORG_NODEID,

                      (SELECT ASSESSMENTID
                         FROM ASSESSMENT_DIM    A,
                              PRODUCT           P,
                              CUST_PRODUCT_LINK CPL
                        WHERE A.PRODUCTID = P.PRODUCTID
                          AND CPL.PRODUCTID = P.PRODUCTID
                          AND CPL.CUST_PROD_ID = A.CUST_PROD_ID
                       \*AND ROWNUM = 1 *\
                       ) ASSESSMENTid,
                      A.ADMINID,
                      GRADEID,
                      LEVELID,
                      (SELECT DISTINCT FORMID
                         FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP
                        WHERE GRADEID = A.GRADEID) FORMID,
                      SYSDATE

        FROM RESULTS_GRT_FACT A, ORG_LSTNODE_LINK OLN
       WHERE A.ORG_NODEID = OLN.ORG_LSTNODEID;

    COMMIT;
  END PRC_GRADE_SELECTION;

  PROCEDURE PRC_USER_UPLOAD IS
  BEGIN
    INSERT INTO user_migration_TEMP
      SELECT UserInformationID \*USER_ID_SEQ.nextval userid*\,
             username,
             username display_username,
             lastname,
             firstname,
             middlename,
             email,
             ' ' phone_no,
             ' ' STREET,
             ' ' COUNTRY,
             ' ' city,
             ' ' ZIPCODE,
             ' ' state,
             1000 customerid,
             'Y' IS_FIRSTTIME_LOGIN,
             SYSDATE LAST_LOGIN_ATTEMPT,
             ' ' PASSWORD_EXPR_DATE,
             'Y' IS_NEW_USER,
             UserPassword PASSWORD,
             ' ' SALT,
             ' ' LAST_LOGIN_DATE,
             SignedEndUserAgreement AS SIGNED_USER_AGREEMENT,
             ' ' AUTO_GENERATED_USER,
             ' ' ACTIVATION_STATUS,
             sysdate CREATED_DATE_TIME,
             sysdate UPDATED_DATE_TIME,
             "iSoftYearID",
             testid,
             (SELECT org_prism_id
                FROM Record_Mig
               WHERE inode_type = 'CLASS'
                 AND inode_id = "ClassID") class_org_nodeid
        FROM istep_datamig.mig_faculty         b,
             istep_datamig.mig_facultyclasscon e,
             istep_datamig.mig_person          c,
             istep_datamig.mig_userinformation d
       WHERE b."FacultyTypeID" = 3
         AND b."FacultyID" = e."FacultyID"
         AND b."PersonID" = c.personid
         AND d.currentroleid = 8
         AND c.personid = d.personid
         AND "iSoftYearID" IN (25, 22, 23, 24)
         AND "ClassID" <> 0;

    COMMIT;

    INSERT INTO users
      SELECT userid,
             username,
             display_username,
             lastname,
             firstname,
             middlename,
             email,
             phone_no,
             STREET,
             COUNTRY,
             city,
             ZIPCODE,
             state,
             customerid,
             IS_FIRSTTIME_LOGIN,
             LAST_LOGIN_ATTEMPT,
             (LAST_LOGIN_ATTEMPT + 365) PASSWORD_EXPR_DATE,
             IS_NEW_USER,
             PASSWORD,
             SALT,
             LAST_LOGIN_ATTEMPT - 365 AS LAST_LOGIN_DATE,
             SIGNED_USER_AGREEMENT,
             AUTO_GENERATED_USER,
             'AC' ACTIVATION_STATUS,
             CREATED_DATE_TIME,
             UPDATED_DATE_TIME
        FROM user_migration_TEMP;

UPDATE users
SET phone_no = NULL,
    street = NULL,
    country = NULL,
    city=NULL,
    zipcode = NULL,
    state ='IN'
WHERE userid IN (SELECT userid FROM user_migration_TEMP)  ;


MERGE INTO users e
    USING (SELECT UserInformationID AS userid,
    userinformation.LogonAttemptTime AS LAST_LOGIN_ATTEMPT,
    LastLogon ,
    UserInformation.isTurnleafCREATEdAccount
     from istep_datamig.mig_userinformation userinformation
inner join istep_datamig.mig_test test
on UserInformation.TESTID = Test."TestID"
inner join istep_datamig.mig_person person
on UserInformation.PersonID = Person.PersonID
inner join istep_datamig.mig_Faculty Faculty
on Person.PersonID = Faculty."PersonID"
inner join istep_datamig.mig_FacultyClassCon FacultyClassCon
on Faculty."FacultyID" = FacultyClassCon."FacultyID"
inner join istep_datamig.mig_Class Class on FacultyClassCon."ClassID" = Class.ClassID
inner join istep_datamig.mig_School School on Class.SchoolID = School.SchoolID
inner join istep_datamig.mig_District District on School.DistrictID = District.DistrictID
inner join istep_datamig.mig_Role  Role
on UserInformation.CurrentRoleID = Role."RoleID"
where Role."RoleName" = 'Teacher User') h
    ON (e.userid = h.userid)
  WHEN MATCHED THEN
    UPDATE SET e.LAST_LOGIN_ATTEMPT = h.LAST_LOGIN_ATTEMPT ,
              e.PASSWORD_EXPR_DATE = NULL ,
               e.LAST_LOGIN_DATE =h.LastLogon ,
               --e.IS_NEW_USER = NULL,
               e.AUTO_GENERATED_USER = h.isTurnleafCREATEdAccount,
               e.PASSWORD = TRIM(e.PASSWORD) ;

    INSERT INTO org_users
      SELECT org_USER_ID_SEQ.nextval,
             userid,
             class_org_nodeid,
             4,
             CASE
               WHEN "iSoftYearID" = 25 THEN
                101
               WHEN "iSoftYearID" = 24 THEN
                102
               WHEN "iSoftYearID" = 23 THEN
                103
               WHEN "iSoftYearID" = 22 THEN
                104
             END AS adminid,
             'AC',
             SYSDATE,
             NULL
        FROM user_migration_TEMP;


        INSERT INTO USER_ROLE
          SELECT USERID,
                 (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_USER') roleid,
                 SYSDATE,
                 SYSDATE
            FROM USERS U
           WHERE NOT EXISTS
           (SELECT 1 FROM USER_ROLE A WHERE U.USERID = A.USERID);
        COMMIT ;





  END;

  PROCEDURE proc_parent_user_load
  IS
  BEGIN
      INSERT INTO TEMP_PARENT_USERS
        SELECT USER_ID,
               A.LAST_NAME AS LAST_NAME,
               A.FIRST_NAME AS FIRST_NAME,
               A.MIDDLE_NAME AS MIDDLE_NAME,
               A.EMAIL_ADDRESS AS EMAIL,
               A.DATE_AGREED_FERPA,
               A.LAST_LOGIN_ATTEMPT,
               A.CREATE_DATE,
               STUDENT_BIO_ID,
               ADMINID,
               CUST_PROD_ID,
               (SELECT PARENT_ORG_NODEID
                  FROM ORG_NODE_DIM B
                 WHERE B.ORG_NODEID = C.ORG_NODEID) AS PARENT_ORG_NODEID,
               ORG_NODEID AS CLASS_ORG_NODEID,
               STUDENTID,
               C.STUDENT_IDENTITY_ID
          FROM ISTEP_DATAMIG.MIG_USER_PROFILE    A,
               ISTEP_DATAMIG.MIG_STUDENT_PROFILE B,
               TEST_COUNT                        C
         WHERE USER_PROFILE_ROLE_ID = 'PARENT'
           AND A.USER_PROFILE_ID = B.USER_PROFILE_ID
           AND B.STUDENT_IDENTITY_ID = C.STUDENT_IDENTITY_ID(+)
           AND a.market_id ='IN';
       COMMIT ;

      INSERT INTO users
      SELECT USER_ID_SEQ.NEXTVAL ,
             USER_ID AS USERNAME,
             substr(USER_ID,1,10)  AS display_username,
             A.LAST_NAME AS LAST_NAME,
             A.FIRST_NAME AS FIRST_NAME,
             A.MIDDLE_NAME AS MIDDLE_NAME,
             EMAIL,
             NULL PHONE_NO,
             NULL STREET,
             NULL COUNTRY,
             NULL CITY,
             NULL ZIPCODE,
             'IN' STATE,
             1000 CUSTOMERID,
             'Y' IS_FIRSTTIME_LOGIN,
             A.LAST_LOGIN_ATTEMPT LAST_LOGIN_ATTEMPT,
             NULL PASSWORD_EXPR_DATE,
             'Y' IS_NEW_USER,
             ' ' PASSWORD,
             ' ' SALT,
             A.LAST_LOGIN_ATTEMPT LAST_LOGIN_DATE,
             DATE_AGREED_FERPA AS SIGNED_USER_AGREEMENT,
             'N' AUTO_GENERATED_USER,
             'AC' ACTIVATION_STATUS,
             SYSDATE AS CREATED_DATE_TIME,
             NULL UPDATED_DATE_TIME

        FROM (

              SELECT DISTINCT USER_ID,
                               A.LAST_NAME AS LAST_NAME,
                               A.FIRST_NAME AS FIRST_NAME,
                               A.MIDDLE_NAME AS MIDDLE_NAME,
                               EMAIL,
                               A.DATE_AGREED_FERPA,
                               A.LAST_LOGIN_ATTEMPT,
                               A.CREATE_DATE
                FROM TEMP_PARENT_USERS A
                WHERE user_id NOT IN (
'tttt',
'abcd',
'scott',
'test',
'aaaa',
'tester',
'bbbb')) A;

   COMMIT ;

      INSERT INTO org_users

     SELECT org_USER_ID_SEQ.nextval, a.* FROM (
      \*SELECT  DISTINCT
             userid,
             PARENT_org_nodeid,
             0,
              adminid,
             'AC',
             SYSDATE,
             NULL
        FROM TEMP_PARENT_USERS A , USERS U
        WHERE A.USER_ID = U.USERNAME
        AND STUDENT_IDENTITY_ID IS NOT NULL*\
        SELECT
             userid,
             PARENT_org_nodeid,
             0,
             MIN(adminid)  adminid ,
             'AC',
             SYSDATE,
             NULL
        FROM TEMP_PARENT_USERS A , USERS U
        WHERE A.USER_ID = U.USERNAME
        AND STUDENT_IDENTITY_ID IS NOT NULL
        GROUP BY  userid,
             PARENT_org_nodeid) a ;

    INSERT INTO USER_ROLE
      SELECT USERID,
             (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_PARENT') ROLEID,
             SYSDATE,
             SYSDATE
        FROM TEMP_PARENT_USERS A, USERS U
       WHERE A.USER_ID = U.USERNAME
         AND STUDENT_IDENTITY_ID IS NOT NULL
         AND NOT EXISTS
       (SELECT 1 FROM USER_ROLE A WHERE U.USERID = A.USERID)
         AND A.USER_ID NOT IN
             ('tttt', 'abcd', 'scott', 'test', 'aaaa', 'tester', 'bbbb');

    INSERT INTO USER_ROLE
      SELECT
             USERID,
             (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_USER') ROLEID,
             SYSDATE,
             SYSDATE
        FROM TEMP_PARENT_USERS A, USERS U
       WHERE A.USER_ID = U.USERNAME
         AND STUDENT_IDENTITY_ID IS NOT NULL
         AND NOT EXISTS (SELECT 1
                FROM USER_ROLE A
               WHERE U.USERID = A.USERID
                 AND ROLEID = 1)
         AND A.USER_ID NOT IN ('tttt', 'abcd', 'scott', 'test',
              'aaaa', 'tester', 'bbbb');

           COMMIT ;


  END proc_parent_user_load;

   PROCEDURE LOAD_ISR_PDF_FILES (P_CUST_PROD_ID IN NUMBER)
    IS
    BEGIN

        \*  SELECT STUDENT_PDF_SEQ.NEXTVAL,
                 STUDENT_BIO_ID,
                 SUBSTR(LOCATION, instr(LOCATION, '/',-1,1)+1, LENGTH(LOCATION )) AS LOCATION,
                 PDF_REPORTID,
                 ADMINID,
                 IS_FILE_EXISTS ,
                 SYSDATE
            FROM (SELECT DISTINCT STUDENT_BIO_ID,
                                  LOCATION,
                                  PDF_REPORTID,
                                  ADMINID,
                                  'N' AS  IS_FILE_EXISTS,
                                  SYSDATE
                    FROM ISTEP_DATAMIG.MIG_ASSESSMENT_PDF_FILES A,
                         ISTEP_DATAMIG.MIG_TEST_EVENT           B,
                         TEST_COUNT                             C,
                         PDF_REPORTS                            D
                   WHERE PREGENERATED_REPORT_TYPE_ID = 'ISR'
                     AND A.DELETE_FLAG = 'N'
                     AND B.DELETE_FLAG = 'N'
                     AND A.TEST_EVENT_ID = B.TEST_EVENT_ID
                     AND C.CUST_PROD_ID = P_CUST_PROD_ID
                     AND B.STUDENT_IDENTITY_ID = C.STUDENT_IDENTITY_ID
                     AND C.CUST_PROD_ID = D.CUST_PROD_ID
                     AND D.CUST_PROD_ID = P_CUST_PROD_ID
                     AND D.REPORT_CODE = '1');
          *\
 \*SELECT STUDENT_PDF_SEQ.NEXTVAL,
        STUDENT_BIO_ID,

        case
          when A.CUST_PROD_ID IN (5001, 5005, 5009, 5013) and
               SUBSTR(ISTEP_TEST_NAME, 1, 5) = 'ISTEP' THEN

           'IN' || TEST_DATE || '.' || SUBSTR(ISTEP_TEST_NAME, 1, 5) || '.' ||
           ORGTSTGPGM || '.' ||
           (SELECT REPLACE(RTRIM(L2_ORG_NODE_NAME), ' ', '_')
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT L2_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT REPLACE(RTRIM(L3_ORG_NODE_NAME), ' ', '_')
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT L3_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT GRADE_CODE FROM GRADE_DIM S WHERE S.GRADEID = A.GRADEID) || '.' ||
           ELEMENT_NUMBER || '.ISR.pdf'

          WHEN A.CUST_PROD_ID IN (5002, 5006, 5010, 5014) and
               SUBSTR(ISTEP_TEST_NAME, 1, 5) = 'IMAST' THEN

           'IN' || TEST_DATE || '.' || SUBSTR(ISTEP_TEST_NAME, 1, 5) || '.' ||
           ORGTSTGPGM || '..' ||
           (SELECT L2_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '..' ||
           (SELECT L3_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT GRADE_CODE FROM GRADE_DIM S WHERE S.GRADEID = A.GRADEID) || '.' ||
           ELEMENT_NUMBER || '.ISR.pdf'

          WHEN A.CUST_PROD_ID IN (5004,5008,5003,5007) and
               SUBSTR(ISTEP_TEST_NAME, 1, 5) = 'IREAD' THEN

           'IN' || TEST_DATE || '.' || SUBSTR(ISTEP_TEST_NAME, 1, 5) || '.' ||
           ORGTSTGPGM || '..' ||
           (SELECT L2_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '..' ||
           (SELECT L3_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT GRADE_CODE FROM GRADE_DIM S WHERE S.GRADEID = A.GRADEID) || '.' ||
           ELEMENT_NUMBER || '.ISR.pdf'

          \*WHEN A.CUST_PROD_ID = 5008 and
               SUBSTR(ISTEP_TEST_NAME, 1, 5) = 'IREAD' THEN

           'IN' || '022812' || '.' || SUBSTR(ISTEP_TEST_NAME, 1, 5) || '.' ||
           ORGTSTGPGM || '..' ||
           (SELECT L2_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '..' ||
           (SELECT L3_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT GRADE_CODE FROM GRADE_DIM S WHERE S.GRADEID = A.GRADEID) || '.' ||
           ELEMENT_NUMBER || '.ISR.pdf'

          WHEN A.CUST_PROD_ID = 5003 and
               SUBSTR(ISTEP_TEST_NAME, 1, 5) = 'IREAD' THEN

           'IN' || '022813' || '.' || SUBSTR(ISTEP_TEST_NAME, 1, 5) || '.' ||
           ORGTSTGPGM || '..' ||
           (SELECT L2_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '..' ||
           (SELECT L3_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT GRADE_CODE FROM GRADE_DIM S WHERE S.GRADEID = A.GRADEID) || '.' ||
           ELEMENT_NUMBER || '.ISR.pdf'

          WHEN A.CUST_PROD_ID = 5007 and
               SUBSTR(ISTEP_TEST_NAME, 1, 5) = 'IREAD' THEN

           'IN' || '022812' || '.' || SUBSTR(ISTEP_TEST_NAME, 1, 5) || '.' ||
           ORGTSTGPGM || '..' ||
           (SELECT L2_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '..' ||
           (SELECT L3_ORG_NODE_CODE
              FROM VW_HIERARCHY_DETAILS_LOOKUP B
             WHERE B.L4_ORG_NODEID = A.ORG_NODEID) || '.' ||
           (SELECT GRADE_CODE FROM GRADE_DIM S WHERE S.GRADEID = A.GRADEID) || '.' ||
           ELEMENT_NUMBER || '.ISR.pdf'

          ELSE

           NULL

        END

        AS FILENAME,

        PDF_REPORTID,
        ADMINID,
        'N' AS IS_FILE_EXISTS,
        SYSDATE
   FROM RESULTS_GRT_FACT A, PDF_REPORTS D
  WHERE A.CUST_PROD_ID = D.CUST_PROD_ID
    AND REPORT_CODE = '1'
    AND A.CUST_PROD_ID = P_CUST_PROD_ID;
    *\

   INSERT \*+append*\  INTO STUDENT_PDF_FILES
    SELECT STUDENT_PDF_SEQ.NEXTVAL,
      STUDENT_BIO_ID,
       FILE_NAME,
       F.PDF_REPORTID,
       C.ADMINID,
       'N',
       SYSDATE
  FROM ISTEP_DATAMIG.MIG_ISR_PDF_FILES_INORS3 A,
       PRODUCT_MAP_MIG                        B,
       CUST_PRODUCT_LINK                      E,
       PDF_REPORTS                            F,
       STUDENT_BIO_DIM                        C,
       ORG_NODE_DIM                           D
 WHERE e.cust_prod_id = P_CUST_PROD_ID
   AND A.PRODUCT_NAME = B.PRODUCT_NAME
   AND B.PRODUCTID = E.PRODUCTID
   AND E.CUST_PROD_ID = F.CUST_PROD_ID
   AND F.REPORT_CODE = '1'
   AND A.TEST_ELEMENT_ID = C.TEST_ELEMENT_ID
   AND A.L4_ORG_NODE_CODE = D.ORG_NODE_CODE
   AND D.ORG_NODEID = C.ORG_NODEID ;

  COMMIT ;



     END LOAD_ISR_PDF_FILES;


    PROCEDURE LOAD_IP_PDF_FILES (P_CUST_PROD_ID IN NUMBER)
    IS
    BEGIN
        INSERT \*+append*\  INTO STUDENT_PDF_FILES
         \* SELECT STUDENT_PDF_SEQ.NEXTVAL,
                 STUDENT_BIO_ID,
                 LOCATION,
                 PDF_REPORTID,
                 ADMINID,
                 IS_FILE_EXISTS,
                 SYSDATE
            FROM (SELECT DISTINCT STUDENT_BIO_ID,
                                  SUBSTR(LOCATION, instr(LOCATION, '/',-1,1)+1, LENGTH(LOCATION )) AS LOCATION,
                                  PDF_REPORTID,
                                  ADMINID,
                                  'N' AS  IS_FILE_EXISTS,
                                  SYSDATE
                    FROM ISTEP_DATAMIG.MIG_ASSESSMENT_PDF_FILES A,
                         ISTEP_DATAMIG.MIG_TEST_EVENT           B,
                         TEST_COUNT                             C,
                         PDF_REPORTS                            D
                   WHERE PREGENERATED_REPORT_TYPE_ID = 'ITEM'
                     AND A.DELETE_FLAG = 'N'
                     AND B.DELETE_FLAG = 'N'
                     AND A.TEST_EVENT_ID = B.TEST_EVENT_ID
                     AND C.CUST_PROD_ID = P_CUST_PROD_ID
                     AND B.STUDENT_IDENTITY_ID = C.STUDENT_IDENTITY_ID
                     AND C.CUST_PROD_ID = D.CUST_PROD_ID
                     AND D.CUST_PROD_ID = P_CUST_PROD_ID
                     AND D.REPORT_CODE = '2');
               *\
                 SELECT STUDENT_PDF_SEQ.NEXTVAL,
                        STUDENT_BIO_ID,
                        -- LOCATION,
                        CASE
                          WHEN IMAGEID_APPLIED_SKILLS_PP IS NULL THEN
                           NULL
                          ELSE
                           IMAGEID_APPLIED_SKILLS_PP || '.pdf'
                        END AS FILENAME,
                        PDF_REPORTID,
                        ADMINID,
                        'N' AS IS_FILE_EXISTS,
                        SYSDATE
                   FROM RESULTS_GRT_FACT A, PDF_REPORTS D
                  WHERE A.CUST_PROD_ID = D.CUST_PROD_ID
                    AND REPORT_CODE = '2'
                    AND A.CUST_PROD_ID = P_CUST_PROD_ID;

                     COMMIT ;

     END LOAD_IP_PDF_FILES;



  PROCEDURE PRC_POST_MIGRATION_EXCEP_CHCK(IN_ASSESSMENT_ID NUMBER) IS

    LV_CUST_PROD_ID NUMBER;
    LV_TESTID       NUMBER;

  BEGIN

    SELECT DISTINCT CUST_PROD_ID, TESTID
      INTO LV_CUST_PROD_ID, LV_TESTID
      FROM TEST_TP_MAP A, CUST_PRODUCT_LINK B
     WHERE A.PRODUCTID = B.PRODUCTID
       AND A.ASSESSMENTID = IN_ASSESSMENT_ID;

    INSERT INTO EXP_RESULTS_CLASSSUMMARY
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_CLASSSUMMARY A
       WHERE A.testid = LV_TESTID
         AND NOT EXISTS
       (SELECT 1
                FROM CLASS_SUMM_FACT C
               WHERE C.CLASS_SUMM_ID = A.RESULTS_CLASSSUMMARYID
                 AND C.cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_GRT
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_GRT G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM RESULTS_GRT_FACT
               WHERE RESULTS_GRTID = G.RESULTS_GRTID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_MEDIA
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_MEDIA G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM MEDIA_FACT
               WHERE MEDIA_ID = G.RESULTS_MEDIAID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    -- COMMIT;

    INSERT INTO EXP_RESULTS_SUMT
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_SUMT G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM SUMT_FACT
               WHERE SUMT_ID = G.RESULTS_SUMTID
                 AND cust_prod_id = LV_CUST_PROD_ID);
    -- COMMIT;

    INSERT INTO EXP_RESULTS_STFD
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_STFD G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM STFD_FACT
               WHERE STFD_ID = G.RESULTS_STFDID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_ASFD
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_ASFD G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM ASFD_FACT
               WHERE ASFD_ID = G.RESULTS_ASFDID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_DISA
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_DISA G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM DISA_FACT
               WHERE DISA_ID = G.RESULTS_DISAID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    -- COMMIT;

    INSERT INTO EXP_RESULTS_SPPR
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_SPPR G
       WHERE testid = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM SPPR_FACT
               WHERE SPPR_ID = G.RESULTS_SPPRID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_UDTR_SUMMARY
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_UDTR_SUMMARY G
       WHERE testid = LV_TESTID
         AND NOT EXISTS
       (SELECT 1
                FROM UDTR_SUMM_FACT
               WHERE UDTR_SUMM_ID = G.RESULTS_UDTR_SUMMARYID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_UDTR_ROSTER
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_UDTR_ROSTER G
       WHERE testid = LV_TESTID
         AND NOT EXISTS
       (SELECT 1
                FROM UDTR_ROSTER_FACT
               WHERE G.RESULTS_UDTR_ROSTERID = UDTR_ROSTER_ID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_RESULTS_ACADSTDSUMMARY
      SELECT *
        FROM ISTEP_DATAMIG.MIG_RESULTS_ACADSTDSUMMARY MRACAD
       WHERE testid = LV_TESTID
         AND NOT EXISTS
       (SELECT 1
                FROM ACAD_STD_SUMM_FACT
               WHERE AS_SUMM_ID = MRACAD.RESULTS_ACADSTANDARDSUMID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_DISAGGREGATIONCATEGORYTYPE
      SELECT *
        FROM ISTEP_DATAMIG.mig_disaggregationcategorytype mig
       WHERE "TestID" = LV_TESTID
         AND NOT EXISTS (select 1
                FROM DISAGGREGATION_CATEGORY_TYPE
               where DisaggregationCategoryTypeID =
                     mig."DisaggregationCategoryTypeID"
                 AND cust_prod_id = LV_CUST_PROD_ID);

    --COMMIT;

    INSERT INTO EXP_DISAGGREGATIONCATEGORY
      SELECT *
        FROM ISTEP_DATAMIG.mig_disaggregationcategory mig
       WHERE "TestID" = LV_TESTID
         AND NOT EXISTS (SELECT 1
                FROM DISAGGREGATION_CATEGORY
               WHERE mig."DisaggregationCategoryID" =
                     DisaggregationCategoryID
                 AND cust_prod_id = LV_CUST_PROD_ID);
    -- COMMIT;

    INSERT INTO EXP_CUTSCOREIPI
      SELECT *
        FROM ISTEP_DATAMIG.mig_cutscoreipi mig
       WHERE TESTID = LV_TESTID
         AND NOT EXISTS (select 1
                FROM cutscoreipi p
               where p.CUTSCOREIPIID = mig.CUTSCOREIPIID
                 AND cust_prod_id = LV_CUST_PROD_ID);

    COMMIT;

  END PRC_POST_MIGRATION_EXCEP_CHCK;

  PROCEDURE PRC_POST_MIGRATION_EXCEP_RPRT(IN_ASSESSMENT_ID NUMBER) IS

    LV_ISTEP_TAB_COUNT     NUMBER;
    LV_SOURCE_TAB_COUNT    NUMBER;
    LV_EXCEPTION_TAB_COUNT NUMBER;

  BEGIN

    IF IN_ASSESSMENT_ID IS NULL THEN

      select count(1) INTO LV_ISTEP_TAB_COUNT from STUDENT_BIO_DIM;

      SELECT count(1) INTO LV_SOURCE_TAB_COUNT FROM RESULTS_GRT_FACT;

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_GRT_FACT;

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (NULL,
         'STUDENT_BIO_DIM',
         'RESULTS_GRT_FACT',
         'EXP_RESULTS_GRT_FACT',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

    ELSE

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from CLASS_SUMM_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_CLASSSUMMARY A
       WHERE A.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_CLASSSUMMARY A
       WHERE A.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'CLASS_SUMM_FACT',
         'MIG_RESULTS_CLASSSUMMARY',
         'EXP_RESULTS_CLASSSUMMARY',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;
      -------------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from RESULTS_GRT_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_GRT G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_GRT G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'RESULTS_GRT_FACT',
         'MIG_RESULTS_GRT',
         'EXP_RESULTS_GRT',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      -------------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from MEDIA_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_MEDIA G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_MEDIA G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'MEDIA_FACT',
         'MIG_RESULTS_MEDIA',
         'EXP_RESULTS_MEDIA',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ------------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from SUMT_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_SUMT G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_SUMT G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'SUMT_FACT',
         'MIG_RESULTS_SUMT',
         'EXP_RESULTS_SUMT',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ----------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from STFD_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_STFD G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_STFD G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'STFD_FACT',
         'MIG_RESULTS_STFD',
         'EXP_RESULTS_STFD',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from ASFD_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_ASFD G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_ASFD G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'ASFD_FACT',
         'MIG_RESULTS_ASFD',
         'EXP_RESULTS_ASFD',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ----------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from DISA_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_DISA G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_DISA G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'DISA_FACT',
         'MIG_RESULTS_DISA',
         'EXP_RESULTS_DISA',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ---------------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from SPPR_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_SPPR G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_SPPR G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'SPPR_FACT',
         'MIG_RESULTS_SPPR',
         'EXP_RESULTS_SPPR',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      -----------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from UDTR_SUMM_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      select count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_UDTR_SUMMARY G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      select count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_UDTR_SUMMARY G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'UDTR_SUMM_FACT',
         'MIG_RESULTS_UDTR_SUMMARY',
         'EXP_RESULTS_UDTR_SUMMARY',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      -----------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from UDTR_ROSTER_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      SELECT count(1)
        INTO LV_SOURCE_TAB_COUNT
        FROM ISTEP_DATAMIG.MIG_RESULTS_UDTR_ROSTER G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      SELECT count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        FROM EXP_RESULTS_UDTR_ROSTER G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'UDTR_ROSTER_FACT',
         'MIG_RESULTS_UDTR_ROSTER',
         'EXP_RESULTS_UDTR_ROSTER',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from ACAD_STD_SUMM_FACT
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      select count(1)
        INTO LV_SOURCE_TAB_COUNT
        from ISTEP_DATAMIG.MIG_RESULTS_ACADSTDSUMMARY G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      select count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        from EXP_RESULTS_ACADSTDSUMMARY G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);


       INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'ACAD_STD_SUMM_FACT',
         'MIG_RESULTS_ACADSTDSUMMARY',
         'EXP_RESULTS_ACADSTDSUMMARY',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      --------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from DISAGGREGATION_CATEGORY
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      select count(1)
        INTO LV_SOURCE_TAB_COUNT
        from ISTEP_DATAMIG.mig_disaggregationcategory G
       WHERE G."TestID" =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      select count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        from EXP_DISAGGREGATIONCATEGORY G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'DISAGGREGATION_CATEGORY',
         'MIG_DISAGGREGATIONCATEGORY',
         'EXP_DISAGGREGATIONCATEGORY',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      -------------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from DISAGGREGATION_CATEGORY_TYPE
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      select count(1)
        INTO LV_SOURCE_TAB_COUNT
        from ISTEP_DATAMIG.mig_disaggregationcategorytype G
       WHERE G."TestID" =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      select count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        from EXP_DISAGGREGATIONCATEGORYTYPE G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'DISAGGREGATION_CATEGORY_TYPE',
         'MIG_DISAGGREGATIONCATEGORYTYPE',
         'EXP_DISAGGREGATIONCATEGORYTYPE',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ------------

      select count(1)
        INTO LV_ISTEP_TAB_COUNT
        from cutscoreipi
       where CUST_PROD_ID =
             (select DISTINCT cpl.CUST_PROD_ID
                from TEST_TP_MAP TTM, cust_product_link cpl
               where TTM.ASSESSMENTID = IN_ASSESSMENT_ID
                 and cpl.productid = TTM.PRODUCTID
                 AND cpl.adminid = TTM.ADMINID);

      select count(1)
        INTO LV_SOURCE_TAB_COUNT
        from istep_datamig.mig_cutscoreipi G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      select count(1)
        INTO LV_EXCEPTION_TAB_COUNT
        from exp_cutscoreipi G
       WHERE G.TESTID =
             (select distinct TTM.TESTID
                from TEST_TP_MAP TTM
               WHERE TTM.ASSESSMENTID = IN_ASSESSMENT_ID);

      INSERT INTO migration_log_table
        (ASSESSMENT_ID,
         ISTEP_TAB_NAME,
         SOURCE_TAB_NAME,
         EXCEPTION_TAB_NAME,
         ISTEP_TAB_COUNT,
         SOURCE_TAB_COUNT,
         SRC_ISTEP_COUNT_DIFF,
         EXCEPTION_TAB_COUNT)
      VALUES
        (IN_ASSESSMENT_ID,
         'CUTSCOREIPI',
         'MIG_CUTSCOREIPI',
         'EXP_CUTSCOREIPI',
         LV_ISTEP_TAB_COUNT,
         LV_SOURCE_TAB_COUNT,
         LV_SOURCE_TAB_COUNT - LV_ISTEP_TAB_COUNT,
         LV_EXCEPTION_TAB_COUNT);

      COMMIT;

      ------------------

    END IF;

  END PRC_POST_MIGRATION_EXCEP_RPRT;

 PROCEDURE PROC_INVITATION_CODE_POP(P_CUST_PRODID IN NUMBER)
 IS
  BEGIN
        INSERT INTO INVITATION_CODE
          SELECT invitation_code_claim_id_seq.nextval ,
          --  COUNT(1) over() cnt,
           INVITATION_CODE_ID,
           A.TEST_ELEMENT_ID,
           TOTAL_AVAILABLE_CLAIMS,
           0,
           EXPIRY_DATE,
           a.INT_STUDENT_ID,
           d.cust_prod_id,
           A.STUDENT_BIO_ID,
           a.gradeid ,
           first_name|| ', ' ||last_name|| ', '|| middle_name AS Student_full_name,
           'N' IS_NEW_IC,
           NULL AS IC_PDF_FILENAME,
           'AC' AS ACTIVATION_STATUS,
           --a.org_nodeid , -- B.PARENT_ORG_NODEID,
          'PN' CREATED_SOURCE,
          1 CREATED_BY_ID,
          a.birthdate,
          (SELECT parent_org_nodeid FROM org_node_dim WHERE org_nodeid = a.org_nodeid)  school_orgnodeid,
           SYSDATE,
           SYSDATE
            FROM ISTEP_DATAMIG.MIG_INVITATION_CODE IC,
                 TEST_COUNT                        D,
                 STUDENT_BIO_DIM                   A\*,
                 ORG_NODE_DIM                      B*\
           WHERE IC.STUDENT_IDENTITY_ID = D.STUDENT_IDENTITY_ID
             AND A.STUDENT_BIO_ID = D.STUDENT_BIO_ID
             AND cust_prod_id =P_CUST_PRODID
             AND organization_id IS NOT NULL ;
            -- AND A.ORG_NODEID = B.ORG_NODEID;

             COMMIT ;

      INSERT INTO INVITATION_CODE_CLAIM
        SELECT IC.ICID,
               OU.ORG_USER_ID,
               'AC',
               ICC.CLAIM_DATE,
               NULL,
               INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL
          FROM ISTEP_DATAMIG.MIG_INVITATION_CODE_CLAIM ICC,
               INVITATION_CODE                         IC,
               TEMP_USER_STUDENT_MAP                   A,
               USERS                                   U,
               ORG_USERS                               OU
         WHERE ICC.STUDENT_IDENTITY_ID = A.STUDENT_IDENTITY_ID
           AND IC.INVITATION_CODE = ICC.INVITATION_CODE_ID
           AND a.cust_prod_id = P_CUST_PRODID
           AND IC.STUDENT_BIO_ID = A.STUDENT_BIO_ID
           AND ICC.USER_PROFILE_ID = A.USER_PROFILE_ID
              --AND ICC.student_identity_id = 89605982
           AND A.USER_ID = U.USERNAME
           AND U.USERID = OU.USERID
         --  AND OU.ADMINID = A.ADMINID
           AND A.PARENT_ORG_NODEID = OU.ORG_NODEID
           AND OU.ORG_NODE_LEVEL = 3;

           COMMIT ;
  END ;
  PROCEDURE proc_itemset_dim_load
  IS
  BEGIN
          INSERT INTO ITEMSET_DIM
            (ITEMSETID,
             ITEM_NAME,
             ITEM_CODE,
             ITEM_SEQ,
             POINT_POSSIBLE,
             ITEM_TYPE,
             SUBT_OBJ_MAPID)
            SELECT 4423 + ROW_NUMBER() OVER(ORDER BY CTB_NO_SCORE_POINTS),
                   LISTAGG(DISPLAYVALUE, ',') WITHIN
             GROUP(
             ORDER BY CTB_ITEM_TYPE) AS ITEM_NAME, LISTAGG(DISPLAYVALUE, ',') WITHIN
             GROUP(
             ORDER BY CTB_ITEM_TYPE) ITEM_CODE, 4423 + ROW_NUMBER() OVER(
             ORDER BY CTB_NO_SCORE_POINTS), CTB_NO_SCORE_POINTS, 'OBJ', SUBT_OBJ_MAPID --,

              FROM (SELECT DISTINCT SUM(CTB_NO_SCORE_POINTS) OVER(PARTITION BY TESTID, ISOFTYEARID, SUBT_OBJ_MAPID) CTB_NO_SCORE_POINTS,
                                    TESTID,
                                    ISOFTYEARID,
                                    SUBT_OBJ_MAPID,
                                    DISPLAYVALUE,
                                    CTB_ITEM_TYPE

                      FROM (SELECT CTB_NO_SCORE_POINTS,
                                   ISOFTYEARID,
                                   MRP.TESTID,
                                   SUBT_OBJ_MAPID,
                                   CTB_ITEM_TYPE,
                                   DISPLAYVALUE

                              FROM ISTEP_DATAMIG.MIG_RESULTS_PEID MRP,
                                   ISTEP_DATAMIG.MIG_PEIDITEMTYPE PTYPE,
                                   ISTEP_DATAMIG.MIG_GRADELEVEL GL,
                                   ISTEP_DATAMIG.MIG_SUBJECT MS,
                                   (SELECT SUBT_OBJ_MAPID,
                                           SUBTEST_CODE,
                                           OBJECTIVE_CODE,
                                           GRADE_CODE,
                                           A.ASSESSMENTID
                                      FROM SUBTEST_OBJECTIVE_MAP A,
                                           LEVEL_MAP             B,
                                           GRADE_LEVEL_MAP       C,
                                           SUBTEST_DIM           SD,
                                           OBJECTIVE_DIM         OD,
                                           GRADE_DIM             GD
                                     WHERE A.LEVEL_MAPID = B.LEVEL_MAPID
                                       AND B.LEVEL_MAPID = C.LEVEL_MAPID
                                       AND SD.SUBTESTID = A.SUBTESTID
                                       AND A.OBJECTIVEID = OD.OBJECTIVEID
                                       AND C.GRADEID = GD.GRADEID) VSGOMP,
                                   (SELECT DISTINCT TESTID, ASSESSMENTID
                                      FROM TEST_TP_MAP) TTP
                             WHERE CTBITEMTYPE = CTB_ITEM_TYPE
                               AND GL."GradeLevelID" = MRP.GRADELEVELID
                               AND MS."SubjectID" = MRP.SUBJECTID
                               AND MRP.CTB_OBJECTIVE_CODE = OBJECTIVE_CODE
                               AND VSGOMP.SUBTEST_CODE = MS."SubjectCode"
                               AND VSGOMP.GRADE_CODE = GL."GradeLevelCode"
                               AND TTP.TESTID = MRP.TESTID
                               AND TTP.ASSESSMENTID = VSGOMP.ASSESSMENTID))
             GROUP BY CTB_NO_SCORE_POINTS, TESTID, ISOFTYEARID, SUBT_OBJ_MAPID ;
  END ;

  PROCEDURE PROC_ASFD_ITEMSETID_POP(P_CUST_PRODID IN NUMBER)
  IS
  BEGIN
              MERGE INTO asfd_fact d
             USING (   SELECT a.asfd_id, c.itemsetid FROM asfd_fact a ,
                            subtest_objective_map b,
                            itemset_dim c
              WHERE a.subtestid = b.subtestid
              AND a.objectiveid = b.objectiveid
              AND b.subt_obj_mapid = c.subt_obj_mapid
              AND substr(a.item_number,2,1) ||item_part = c.item_name
              AND c.item_type ='CR')   b
              ON (d.asfd_id = b.asfd_id )
              WHEN MATCHED THEN
              UPDATE SET d.itemsetid = b.itemsetid ;

  END PROC_ASFD_ITEMSETID_POP;

  PROCEDURE PRC_MIGRATION_WRAPPER IS
  BEGIN
    FOR i IN (SELECT a.assessmentid, a.productid
                FROM assessment_dim a, product p
               WHERE a.productid = p.productid
                 AND a.assessmentid = 4001) LOOP

      INSERT INTO migation_time
      VALUES
        ('PROC_CLASS_SUMM_POPULATE Start:', SYSDATE);

      PROC_CLASS_SUMM_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_CLASS_SUMM_POPULATE End:', SYSDATE);

      PROC_RESULTS_GRT_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_GRT_POPULATE End:', SYSDATE);

      PROC_RESULTS_MEDIA_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_MEDIA_POPULATE End:', SYSDATE);

      PROC_RESULTS_SUMT_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_SUMT_POPULATE End:', SYSDATE);

      PROC_RESULTS_STFD_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_STFD_POPULATE End:', SYSDATE);

      PROC_RESULTS_ASFD_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_ASFD_POPULATE End:', SYSDATE);

      PROC_RESULTS_DISA_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_DISA_POPULATE End:', SYSDATE);

      PROC_RESULTS_SPPR_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_SPPR_POPULATE End:', SYSDATE);

      PROC_RESULTS_UDTR_SUMM_POP(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_UDTR_SUMM_POP End:', SYSDATE);

      PROC_RESULTS_UDTR_ROSTER_POP(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_UDTR_ROSTER_POP End:', SYSDATE);

      PROC_RESULT_ACAD_SUMM_POP(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULT_ACAD_SUMM_POP End:', SYSDATE);

      PROC_RESULTS_PEID_POPULATE(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PROC_RESULTS_PEID_POPULATE End:', SYSDATE);

      PRC_DISAGGREGATION_CATEGR_TYP(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PRC_DISAGGREGATION_CATEGR_TYP End:', SYSDATE);

      PRC_DISAGGREGATION_CATEGORY(IN_ASSESSMENT_ID => i.assessmentid);

      INSERT INTO migation_time
      VALUES
        ('PRC_DISAGGREGATION_CATEGORY End:', SYSDATE);

      PRC_GRADE_SELECTION;
      INSERT INTO migation_time
      VALUES
        ('PRC_GRADE_SELECTION End:', SYSDATE);
    END LOOP;
  END;
  PROCEDURE PRC_USER_UPLOAD_NEW IS
  BEGIN
    INSERT INTO user_migration_TEMP
      SELECT to_number(UserInformationID)  \*USER_ID_SEQ.nextval userid*\,
             username,
             username display_username,
             lastname,
             firstname,
             middlename,
             email,
             NULL phone_no,
             NULL STREET,
             NULL COUNTRY,
             NULL city,
             NULL ZIPCODE,
             'IN' state,
             1000 customerid,
             'N' IS_FIRSTTIME_LOGIN,
             to_date(logonattempttime,'yyyy-mm-dd hh24:mi:ss') LAST_LOGIN_ATTEMPT,
             NULL PASSWORD_EXPR_DATE,
             'N' IS_NEW_USER,
             UserPassword PASSWORD,
             ' ' SALT,
             ' ' LAST_LOGIN_DATE,
             SignedEndUserAgreement AS SIGNED_USER_AGREEMENT,
             isTurnleafCREATEdAccount AUTO_GENERATED_USER,
             'AC' ACTIVATION_STATUS,
             sysdate CREATED_DATE_TIME,
             sysdate UPDATED_DATE_TIME,
             to_number(iSoftYearID) iSoftYearID,
            to_number(testid)  testid,
             (SELECT org_prism_id
                FROM Record_Mig
               WHERE inode_type = 'CLASS'
                 AND inode_id = ClassID) class_org_nodeid
        FROM istep_datamig.Mig_FacultyUpdated         b,
             istep_datamig.Mig_FacultyClassConUpdated e,
             istep_datamig.Mig_PersonUpdated          c,
             istep_datamig.Mig_Userinformationupdated_1 d
       WHERE b.FacultyTypeID = 3
         AND b.FacultyID = e.FacultyID
         AND b.PersonID = c.personid
         AND d.currentroleid = 8
         AND c.personid = d.personid
         AND iSoftYearID IN (25, 22, 23, 24)
         AND ClassID <> 0;

    COMMIT;

    INSERT INTO users
      SELECT userid,
             username,
             display_username,
             lastname,
             firstname,
             middlename,
             email,
             phone_no,
             STREET,
             COUNTRY,
             city,
             ZIPCODE,
             state,
             customerid,
             IS_FIRSTTIME_LOGIN,
             LAST_LOGIN_ATTEMPT,
             (LAST_LOGIN_ATTEMPT + 365) PASSWORD_EXPR_DATE,
             IS_NEW_USER,
             PASSWORD,
             SALT,
             LAST_LOGIN_ATTEMPT - 365 AS LAST_LOGIN_DATE,
             SIGNED_USER_AGREEMENT,
             AUTO_GENERATED_USER,
             'AC' ACTIVATION_STATUS,
             CREATED_DATE_TIME,
             UPDATED_DATE_TIME
        FROM user_migration_TEMP;

\*UPDATE users
SET phone_no = NULL,
    street = NULL,
    country = NULL,
    city=NULL,
    zipcode = NULL,
    state ='IN'
WHERE userid IN (SELECT userid FROM user_migration_TEMP)  ;


MERGE INTO users e
    USING (SELECT UserInformationID AS userid,
    userinformation.LogonAttemptTime AS LAST_LOGIN_ATTEMPT,
    LastLogon ,
    UserInformation.isTurnleafCREATEdAccount
     from istep_datamig.mig_userinformation userinformation
inner join istep_datamig.mig_test test
on UserInformation.TESTID = Test."TestID"
inner join istep_datamig.mig_person person
on UserInformation.PersonID = Person.PersonID
inner join istep_datamig.mig_Faculty Faculty
on Person.PersonID = Faculty."PersonID"
inner join istep_datamig.mig_FacultyClassCon FacultyClassCon
on Faculty."FacultyID" = FacultyClassCon."FacultyID"
inner join istep_datamig.mig_Class Class on FacultyClassCon."ClassID" = Class.ClassID
inner join istep_datamig.mig_School School on Class.SchoolID = School.SchoolID
inner join istep_datamig.mig_District District on School.DistrictID = District.DistrictID
inner join istep_datamig.mig_Role  Role
on UserInformation.CurrentRoleID = Role."RoleID"
where Role."RoleName" = 'Teacher User') h
    ON (e.userid = h.userid)
  WHEN MATCHED THEN
    UPDATE SET e.LAST_LOGIN_ATTEMPT = h.LAST_LOGIN_ATTEMPT ,
              e.PASSWORD_EXPR_DATE = NULL ,
               e.LAST_LOGIN_DATE =h.LastLogon ,
               --e.IS_NEW_USER = NULL,
               e.AUTO_GENERATED_USER = h.isTurnleafCREATEdAccount,
               e.PASSWORD = TRIM(e.PASSWORD) ;*\

    INSERT INTO org_users
      SELECT org_USER_ID_SEQ.nextval,
             userid,
             class_org_nodeid,
             4,
             CASE
               WHEN "iSoftYearID" = 25 THEN
                101
               WHEN "iSoftYearID" = 24 THEN
                102
               WHEN "iSoftYearID" = 23 THEN
                103
               WHEN "iSoftYearID" = 22 THEN
                104
             END AS adminid,
             'AC',
             SYSDATE,
             NULL
        FROM user_migration_TEMP;


        INSERT INTO USER_ROLE
          SELECT USERID,
                 (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_USER') roleid,
                 SYSDATE,
                 SYSDATE
            FROM USERS U
           WHERE NOT EXISTS
           (SELECT 1 FROM USER_ROLE A WHERE U.USERID = A.USERID);
        COMMIT ;





  END;*/
  
  PROCEDURE PROC_OBJ_SCORE_POPULATE_NEW(IN_ASSESSMENTID IN NUMBER) IS
  BEGIN
    INSERT /*+ append */ INTO 
    OBJECTIVE_SCORE_FACT 
      SELECT OBJECTIVE_FACT_SEQ.nextval,
             ORG_NODEID,
             CUST_PROD_ID,
             IN_ASSESSMENTID AS  ASSESSMENTID,
             STUDENT_BIO_ID,
             CONTENTID,
             SUBTESTID,
             OBJECTIVEID,
             NVL((SELECT GENDERID
                   FROM GENDER_DIM
                  WHERE GENDER_CODE = STUDENTS_GENDER),
                 503) GENDERID,
             GRADEID,
             LEVELID,
             FORMID,
             ADMINID,
             NULL AS NCR,
             NULL AS OS,
             decode(REGEXP_SUBSTR(STR_OPI, '[0-9]+|[a-z]+|[A-Z]+', 1, RN),
                    'X',
                    'X',
                    REGEXP_SUBSTR(STR_OPI, '[0-9]+|[a-z]+|[A-Z]+', 1, RN)) AS OPI,
   
                    
          /*   decode( REGEXP_SUBSTR(STR_OPI_CUT, '[0-9]+\.[0-9]+|[a-z]+|[A-Z]+', 1, RN) ,
                   'X',
                    'X',
                     REGEXP_SUBSTR(STR_OPI_CUT, '[0-9]+\.[0-9]+|[a-z]+|[A-Z]+', 1, RN) )  
                      AS OPI_CUT,*/
                   ( SELECT  ipi_at_pass FROM  cutscoreipi d  
                   WHERE d.cust_prod_id =  a.CUST_PROD_ID 
                 AND a.subtestid = d.subtestid 
                 AND a.gradeid = d.gradeid 
                 /*AND a.objective_code = d.standard*/
                AND a.objectiveid = d.objectiveid  )  AS OPI_CUT,   
                    
             decode( REGEXP_SUBSTR(SUMT_MEAN_IPI, '[0-9]+\.[0-9]+|[a-z]+|[A-Z]+', 1, RN) ,
                   'X',
                    'X',
                    round( REGEXP_SUBSTR(SUMT_MEAN_IPI, '[0-9]+\.[0-9]+|[a-z]+|[A-Z]+', 1, RN) )) 
                       AS MEAN_IPI,
                    
                   
             NULL AS PC,
             NULL AS PP,
             NULL AS SS,
             replace(SUBSTR(REPLACE(STR_MST, ',', NULL), RN, 1),'N',NULL)  AS PL,
             NULL AS INRC,
             NULL,
             TEST_DATE,
             SYSDATE
      
        FROM (SELECT STUDENT_BIO_ID,
                     ORG_NODEID,
                     A.ADMINID,
                     A.CUST_PROD_ID,
                     B.RN,
                     B.CONTENTID,
                     
                     B.SUBTESTID,
                     B.OBJECTIVEID,
                     b.objective_code , 
                     B.GRADEID,
                     B.LEVELID,
                     B.FORMID,
                     STUDENTS_GENDER,
                     CASE WHEN A.CUST_PROD_ID IN (5007,5008) THEN
                     
                     to_date('02/28/2012','mm/dd/rrrr') 
          
                      WHEN A.CUST_PROD_ID = 5003 THEN 
                     
                     to_date('02/28/2013','mm/dd/rrrr') 
                     
                      WHEN A.CUST_PROD_ID = 5004  THEN 
                     
                     to_date('03/06/2013','mm/dd/rrrr') 
                     
                     ELSE 
                     TO_DATE(TEST_DATE, 'mm/dd/RRRR') 
                     
                     END AS TEST_DATE,
                    /* MASTERY_INDICATOR_1 || ',' || MASTERY_INDICATOR_2 || ',' ||
                     MASTERY_INDICATOR_3 || ',' || MASTERY_INDICATOR_4 || ',' ||
                     MASTERY_INDICATOR_5 || ',' || MASTERY_INDICATOR_6 || ',' ||
                     MASTERY_INDICATOR_7 || ',' || MASTERY_INDICATOR_8 || ',' ||
                     MASTERY_INDICATOR_9 || ',' || MASTERY_INDICATOR_10 || ',' ||
                     MASTERY_INDICATOR_11 || ',' || MASTERY_INDICATOR_12 || ',' ||
                     MASTERY_INDICATOR_13 || ',' || MASTERY_INDICATOR_14 || ',' ||
                     MASTERY_INDICATOR_15 || ',' || MASTERY_INDICATOR_16 || ',' ||
                     MASTERY_INDICATOR_17 || ',' || MASTERY_INDICATOR_18 || ',' ||
                     MASTERY_INDICATOR_19 || ',' || MASTERY_INDICATOR_20 || ',' ||
                     MASTERY_INDICATOR_21 || ',' || MASTERY_INDICATOR_22 || ',' ||
                     MASTERY_INDICATOR_23 || ',' || MASTERY_INDICATOR_24 || ',' ||
                     MASTERY_INDICATOR_25 || ',' || MASTERY_INDICATOR_26 || ',' ||
                     MASTERY_INDICATOR_27 || ',' || MASTERY_INDICATOR_28 || ',' ||
                     MASTERY_INDICATOR_29 || ',' || MASTERY_INDICATOR_30 || ',' ||
                     MASTERY_INDICATOR_31 || ',' || MASTERY_INDICATOR_32 || ',' ||
                     MASTERY_INDICATOR_33 || ',' || MASTERY_INDICATOR_34 || ',' ||
                     MASTERY_INDICATOR_35*/
                     nvl(MASTERY_INDICATOR_1,'N')  || ',' || nvl(MASTERY_INDICATOR_2,'N')  || ',' ||
                     nvl(MASTERY_INDICATOR_3,'N')  || ',' || nvl(MASTERY_INDICATOR_4,'N')  || ',' ||
                     nvl(MASTERY_INDICATOR_5,'N')  || ',' || nvl(MASTERY_INDICATOR_6,'N')  || ',' ||
                     nvl(MASTERY_INDICATOR_7,'N')  || ',' || nvl(MASTERY_INDICATOR_8,'N')  || ',' ||
                     nvl(MASTERY_INDICATOR_9,'N')  || ',' || nvl(MASTERY_INDICATOR_10,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_11,'N') || ',' || nvl(MASTERY_INDICATOR_12,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_13,'N') || ',' || nvl(MASTERY_INDICATOR_14,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_15,'N') || ',' || nvl(MASTERY_INDICATOR_16,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_17,'N') || ',' || nvl(MASTERY_INDICATOR_18,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_19,'N') || ',' || nvl(MASTERY_INDICATOR_20,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_21,'N') || ',' || nvl(MASTERY_INDICATOR_22,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_23,'N') || ',' || nvl(MASTERY_INDICATOR_24,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_25,'N') || ',' || nvl(MASTERY_INDICATOR_26,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_27,'N') || ',' || nvl(MASTERY_INDICATOR_28,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_29,'N') || ',' || nvl(MASTERY_INDICATOR_30,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_31,'N') || ',' || nvl(MASTERY_INDICATOR_32,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_33,'N') || ',' || nvl(MASTERY_INDICATOR_34,'N') || ',' ||
                     nvl(MASTERY_INDICATOR_35,'N') AS STR_MST,
                     
                     OPIIPI_1 || ',' || OPIIPI_2 || ',' || OPIIPI_3 || ',' ||
                     OPIIPI_4 || ',' || OPIIPI_5 || ',' || OPIIPI_6 || ',' ||
                     OPIIPI_7 || ',' || OPIIPI_8 || ',' || OPIIPI_9 || ',' ||
                     OPIIPI_10 || ',' || OPIIPI_11 || ',' || OPIIPI_12 || ',' ||
                     OPIIPI_13 || ',' || OPIIPI_14 || ',' || OPIIPI_15 || ',' ||
                     OPIIPI_16 || ',' || OPIIPI_17 || ',' || OPIIPI_18 || ',' ||
                     OPIIPI_19 || ',' || OPIIPI_20 || ',' || OPIIPI_21 || ',' ||
                     OPIIPI_22 || ',' || OPIIPI_23 || ',' || OPIIPI_24 || ',' ||
                     OPIIPI_25 || ',' || OPIIPI_26 || ',' || OPIIPI_27 || ',' ||
                     OPIIPI_28 || ',' || OPIIPI_29 || ',' || OPIIPI_30 || ',' ||
                     OPIIPI_31 || ',' || OPIIPI_32 || ',' || OPIIPI_33 || ',' ||
                     OPIIPI_34 || ',' || OPIIPI_35 AS STR_OPI,
                     
                  /*   trim(OPIIPI_CUT_1)          || ',' ||        trim(OPIIPI_CUT_2) || ',' || trim(OPIIPI_CUT_3) || ',' ||
                      trim(OPIIPI_CUT_4)    || ',' || trim(OPIIPI_CUT_5) || ',' || trim(OPIIPI_CUT_6) || ',' ||
                      trim(OPIIPI_CUT_7)    || ',' || trim(OPIIPI_CUT_8) || ',' || trim(OPIIPI_CUT_9) || ',' ||
                      trim(OPIIPI_CUT_10)    || ',' || trim(OPIIPI_CUT_11) || ',' || trim(OPIIPI_CUT_12) || ',' ||
                      trim(OPIIPI_CUT_13)    || ',' || trim(OPIIPI_CUT_14) || ',' || trim(OPIIPI_CUT_15) || ',' ||
                      trim(OPIIPI_CUT_16)          || ',' || trim(OPIIPI_CUT_17) || ',' || trim(OPIIPI_CUT_18) || ',' ||
                      trim(OPIIPI_CUT_19)    || ',' || trim(OPIIPI_CUT_20) || ',' || trim(OPIIPI_CUT_21) || ',' ||
                      trim(OPIIPI_CUT_22)    || ',' || trim(OPIIPI_CUT_23) || ',' || trim(OPIIPI_CUT_24) || ',' ||
                      trim(OPIIPI_CUT_25)    || ',' || trim(OPIIPI_CUT_26) || ',' || trim(OPIIPI_CUT_27) || ',' ||
                      trim(OPIIPI_CUT_28)    || ',' || trim(OPIIPI_CUT_29) || ',' || trim(OPIIPI_CUT_30) || ',' ||
                      trim(OPIIPI_CUT_31)    || ',' || trim(OPIIPI_CUT_32) || ',' || trim(OPIIPI_CUT_33) || ',' ||
                      trim(OPIIPI_CUT_34)    || ',' || trim(OPIIPI_CUT_35) || ',' || trim(OPIIPI_CUT_36) || ',' || 
                      trim(OPIIPI_CUT_37)    || ',' || trim(OPIIPI_CUT_38) || ',' || trim(OPIIPI_CUT_39) || ',' || 
                      trim(OPIIPI_CUT_40)  AS STR_OPI_CUT,*/
                     
                     (SELECT MEAN_IPI_1 || ',' || MEAN_IPI_2 || ',' || MEAN_IPI_3 || ',' ||
                             MEAN_IPI_4 || ',' || MEAN_IPI_5 || ',' || MEAN_IPI_6 || ',' ||
                             MEAN_IPI_7 || ',' || MEAN_IPI_8 || ',' || MEAN_IPI_9 || ',' ||
                             MEAN_IPI_10 || ',' || MEAN_IPI_11 || ',' || MEAN_IPI_12 || ',' ||
                             MEAN_IPI_13 || ',' || MEAN_IPI_14 || ',' || MEAN_IPI_15 || ',' ||
                             MEAN_IPI_16 || ',' || MEAN_IPI_17 || ',' || MEAN_IPI_18 || ',' ||
                             MEAN_IPI_19 || ',' || MEAN_IPI_20 || ',' || MEAN_IPI_21 || ',' ||
                             MEAN_IPI_22 || ',' || MEAN_IPI_23 || ',' || MEAN_IPI_24 || ',' ||
                             MEAN_IPI_25 || ',' || MEAN_IPI_26 || ',' || MEAN_IPI_27 || ',' ||
                             MEAN_IPI_28 || ',' || MEAN_IPI_29 || ',' || MEAN_IPI_30 || ',' ||
                             MEAN_IPI_31  AS SUMT_MEAN_IPI
                       FROM SUMT_FACT SUMT
                       WHERE SUMT.ORG_NODEID = (SELECT ORG_NODEID FROM ORG_NODE_DIM O WHERE org_node_level =1 AND CUSTOMERID = 1000)
                         AND SUMT.CUST_PROD_ID = C.CUST_PROD_ID
                         AND SUMT.ADMINID = A.ADMINID 
                         AND SUMT.GRADEID = B.GRADEID
                         AND sumt.ispublic = a.ispublic 
                         AND SUMT.LEVELID = B.LEVELID  
                         AND (SUMT.ENGLANG_ARTS_SUBTESTID = B.SUBTESTID
                              OR SUMT.MATHEMATICS_SUBTESTID = B.SUBTESTID
                              OR SUMT.SCIENCE_SUBTESTID = B.SUBTESTID
                              OR SUMT.SOCIAL_SUBTESTID = B.SUBTESTID
                              )) AS SUMT_MEAN_IPI
              
                FROM RESULTS_GRT_FACT               A,
                     CUST_PRODUCT_LINK              C,
                     VW_SUBTEST_GRADE_OBJECTIVE_MAP B
               WHERE B.ASSESSMENTID = IN_ASSESSMENTID
                 AND C.PRODUCTID IN
                     (SELECT PRODUCTID
                        FROM ASSESSMENT_DIM
                       WHERE ASSESSMENTID = IN_ASSESSMENTID)
                 AND A.CUST_PROD_ID = C.CUST_PROD_ID
                 AND A.GRADEID = B.GRADEID) a ;
    COMMIT;
  END; 
PROCEDURE proc_parent_user_load_excep
  IS
  BEGIN
  
     /* INSERT INTO TEMP_PARENT_USERS2
         SELECT  DISTINCT   a.USER_ID,
               A.LAST_NAME AS LAST_NAME,
               A.FIRST_NAME AS FIRST_NAME,
               A.MIDDLE_NAME AS MIDDLE_NAME,
               A.EMAIL_ADDRESS AS EMAIL,
               A.DATE_AGREED_FERPA,
               A.LAST_LOGIN_ATTEMPT,
               A.CREATE_DATE,
               STUDENT_BIO_ID,
               (SELECT adminid FROM results_grt_fact a WHERE a.student_bio_id = c.student_bio_id ) ADMINID,
               (SELECT cust_prod_id FROM results_grt_fact a WHERE a.student_bio_id = c.student_bio_id)   CUST_PROD_ID,
               (SELECT PARENT_ORG_NODEID
                  FROM ORG_NODE_DIM B
                 WHERE B.ORG_NODEID = C.ORG_NODEID) AS PARENT_ORG_NODEID,
               (SELECT org_nodeid FROM student_bio_dim a WHERE a.student_bio_id = c.student_bio_id ) AS CLASS_ORG_NODEID,
               STUDENT_bio_id,
               C.STUDENT_IDENTITY_ID
          FROM ISTEP_DATAMIG.MIG_USER_PROFILE    A,
               ISTEP_DATAMIG.MIG_STUDENT_PROFILE B,
               EXCEP_users_not_in_prism1@PROD_TO_ISTEPMASTER         C
         WHERE USER_PROFILE_ROLE_ID = 'PARENT'
           AND A.USER_PROFILE_ID = B.USER_PROFILE_ID
           AND c.user_profile_id = a.user_profile_id 
           AND B.STUDENT_IDENTITY_ID = C.STUDENT_IDENTITY_ID
           AND a.market_id ='IN';
      -- COMMIT ;*/

      INSERT INTO users
      SELECT USER_ID_SEQ.NEXTVAL ,
             USER_ID AS USERNAME,
             substr(USER_ID,1,10)  AS display_username,
             A.LAST_NAME AS LAST_NAME,
             A.FIRST_NAME AS FIRST_NAME,
             A.MIDDLE_NAME AS MIDDLE_NAME,
             EMAIL,
             NULL PHONE_NO,
             NULL STREET,
             NULL COUNTRY,
             NULL CITY,
             NULL ZIPCODE,
             'IN' STATE,
             1000 CUSTOMERID,
             'Y' IS_FIRSTTIME_LOGIN,
             A.LAST_LOGIN_ATTEMPT LAST_LOGIN_ATTEMPT,
             NULL PASSWORD_EXPR_DATE,
             'Y' IS_NEW_USER,
             ' ' PASSWORD,
             ' ' SALT,
             A.LAST_LOGIN_ATTEMPT LAST_LOGIN_DATE,
             DATE_AGREED_FERPA AS SIGNED_USER_AGREEMENT,
             'N' AUTO_GENERATED_USER,
             'AC' ACTIVATION_STATUS,
             SYSDATE AS CREATED_DATE_TIME,
             NULL UPDATED_DATE_TIME

        FROM (

              SELECT DISTINCT USER_ID,
                               A.LAST_NAME AS LAST_NAME,
                               A.FIRST_NAME AS FIRST_NAME,
                               A.MIDDLE_NAME AS MIDDLE_NAME,
                               EMAIL,
                               A.DATE_AGREED_FERPA,
                               A.LAST_LOGIN_ATTEMPT,
                               A.CREATE_DATE
                FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER A
                WHERE user_id NOT IN (
'tttt',
'abcd',
'scott',
'test',
'aaaa',
'tester',
'bbbb',
'itempdf',
'pamelajmarshall',
'rdavis12'  )) A;

  

     INSERT INTO org_users
     SELECT org_USER_ID_SEQ.nextval, a.* FROM (
      /*SELECT  DISTINCT
             userid,
             PARENT_org_nodeid,
             0,
              adminid,
             'AC',
             SYSDATE,
             NULL
        FROM TEMP_PARENT_USERS A , USERS U
        WHERE A.USER_ID = U.USERNAME
        AND STUDENT_IDENTITY_ID IS NOT NULL*/
        SELECT
             userid,
             PARENT_org_nodeid,
             3,
             MIN(adminid)  adminid ,
             'AC',
             SYSDATE,
             NULL
        FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER A , USERS U
        WHERE A.USER_ID = U.USERNAME
        AND STUDENT_IDENTITY_ID IS NOT NULL
        GROUP BY  userid,
             PARENT_org_nodeid) a ;

    INSERT INTO USER_ROLE
      SELECT USERID,
             (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_PARENT') ROLEID,
             SYSDATE,
             SYSDATE
        FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER A, USERS U
       WHERE A.USER_ID = U.USERNAME
         AND STUDENT_IDENTITY_ID IS NOT NULL
         AND NOT EXISTS
       (SELECT 1 FROM USER_ROLE A WHERE U.USERID = A.USERID)
         AND A.USER_ID NOT IN
             ('tttt', 'abcd', 'scott', 'test', 'aaaa', 'tester', 'bbbb','itempdf',
'pamelajmarshall',
'rdavis12');

    INSERT INTO USER_ROLE
      SELECT
             USERID,
             (SELECT ROLEID FROM ROLE WHERE ROLE_NAME = 'ROLE_USER') ROLEID,
             SYSDATE,
             SYSDATE
        FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER A, USERS U
       WHERE A.USER_ID = U.USERNAME
         AND STUDENT_IDENTITY_ID IS NOT NULL
         AND NOT EXISTS (SELECT 1
                FROM USER_ROLE A
               WHERE U.USERID = A.USERID
                 AND ROLEID = 1)
         AND A.USER_ID NOT IN ('tttt', 'abcd', 'scott', 'test',
              'aaaa', 'tester', 'bbbb','itempdf',
'pamelajmarshall',
'rdavis12');

           COMMIT ;


  END proc_parent_user_load_excep;  

END PKG_INORS_MIGRATION;
/
