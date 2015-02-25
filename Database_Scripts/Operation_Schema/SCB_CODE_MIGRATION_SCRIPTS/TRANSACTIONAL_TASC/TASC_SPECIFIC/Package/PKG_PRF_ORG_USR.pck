CREATE OR REPLACE PACKAGE PKG_PRF_ORG_USR IS

  TYPE GET_REFCURSOR IS REF CURSOR;
  PROCEDURE SP_SEARCH_ORG_HIER(p_userid       USERS.USERID%TYPE,
                               p_org_nodeid   ORG_NODE_DIM.ORG_NODEID%TYPE,
                               p_customerid   CUSTOMER_INFO.CUSTOMERID%TYPE,
                               p_trigger_type IN VARCHAR2);

  PROCEDURE SP_SEARCH_ORG_HIER(p_customerid CUSTOMER_INFO.CUSTOMERID%TYPE);
  PROCEDURE SP_STUDENT_DEMO_VALUES(P_PROCESS_ID IN NUMBER);
  PROCEDURE SP_GET_USER_DATA(P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                             P_OUT_CUR_USER_DATA OUT GET_REFCURSOR);

END PKG_PRF_ORG_USR;
/
CREATE OR REPLACE PACKAGE BODY PKG_PRF_ORG_USR IS

  PROCEDURE SP_SEARCH_ORG_HIER(p_userid       USERS.USERID%TYPE,
                               p_org_nodeid   ORG_NODE_DIM.ORG_NODEID%TYPE,
                               p_customerid   CUSTOMER_INFO.CUSTOMERID%TYPE,
                               p_trigger_type IN VARCHAR2) AS
    --PRAGMA AUTONOMOUS_TRANSACTION ;
  BEGIN
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE ORG_NODE_DIM_HIER' ;
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE ORGUSER_DETAILS' ;
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE ORGUSER_MAPPING' ;
  
    /*INSERT INTO  ORG_NODE_DIM_HIER
                   SELECT DISTINCT ORG.ORG_NODEID,
                                   ORG.ORG_NODE_NAME,
                                   ORG.PARENT_ORG_NODEID,
                                   ORG.ORG_NODE_CODE,
                                   ORG.ORG_NODE_LEVEL,
                                   ORG.ADMINID,
                                   CONNECT_BY_ROOT(ORG.ORG_NODEID) AS HIGHEST_ORG_NODE,
                                   LTRIM(SYS_CONNECT_BY_PATH(ORG.ORG_NODEID, '~'),'~') AS ORG_NODE_CODE_PATH,
                                   LTRIM(SYS_CONNECT_BY_PATH(ORG.ORG_NODE_NAME, '~'),'~') AS ORG_NODE_NAME_PATH
                            FROM  ORG_NODE_DIM ORG
                            START WITH ORG.ORG_NODE_LEVEL =1
                            CONNECT BY NOCYCLE PRIOR  ORG.ORG_NODEID=ORG.PARENT_ORG_NODEID;
    */
  
    IF p_trigger_type = 'UP' OR p_trigger_type = 'IN' THEN
    
      -- INSERT INTO temp_task VALUES (p_userid,p_org_nodeid,p_customerid, p_trigger_type,NULL ) ;
    
      DELETE FROM ORGUSER_DETAILS
       WHERE USERID = p_userid
         AND CUSTOMERID = p_customerid
         AND ORG_NODEID = p_org_nodeid;
      DELETE FROM ORGUSER_MAPPING
       WHERE USERID = p_userid
         AND CUSTOMERID = p_customerid
         AND ORG_NODEID = p_org_nodeid;
      DELETE FROM ORGUSER_MAPPING_SEARCH
       WHERE USERID = p_userid
         AND CUSTOMERID = p_customerid
         AND ORG_NODEID = p_org_nodeid;
    
      INSERT INTO ORGUSER_DETAILS
        SELECT OU.USERID,
               USR.USERNAME,
               USR.CUSTOMERID,
               ORG.ORG_NODEID,
               ORG.ORG_NODE_NAME,
               ORG.PARENT_ORG_NODEID,
               ORG.HIGHEST_ORG_NODE,
               --ORG.ORG_NODE_CODE_PATH,
               ORG.ORG_NODE_LEVEL,
               OU.ADMINID
          FROM ORG_NODE_DIM_HIER ORG, ORG_USERS OU, USERS USR
         WHERE OU.ORG_NODEID = ORG.ORG_NODEID
           AND ORG.ORG_NODE_LEVEL = OU.ORG_NODE_LEVEL
           AND OU.USERID = USR.USERID
           AND USR.USERID = p_userid
           AND USR.CUSTOMERID = p_customerid
           AND ORG.ORG_NODEID = p_org_nodeid
           AND OU.ORG_NODE_LEVEL <> 0
           AND ORG.customerid = USR.customerid
           AND OU.ACTIVATION_STATUS = 'AC';
    
      INSERT INTO ORGUSER_MAPPING
        SELECT OUSR.org_nodeid org_nodeid,
               OUSR.ORG_NODE_NAME,
               OUSR.USERID,
               OUSR.USERNAME,
               OUSR.CUSTOMERID,
               OUSR.HIGHEST_ORG_NODE,
               OUSR.parent_org_nodeid,
               OUSR.ADMINID,
               org.ORG_NODEID lowest_nodeid,
               OUSR.ORG_NODE_LEVEL,
               LEVEL8_NAME,
               LEVEL8_CODE,
               ORG.ORG_NODE_NAME AS EXAMINER_NAME,
               ORG.ORG_NODE_CODE AS EXAMINER_CODE,
               LEVEL7_NAME,
               LEVEL7_CODE,
               LEVEL6_NAME,
               LEVEL6_CODE,
               LEVEL5_NAME,
               LEVEL5_CODE,
               LEVEL4_NAME,
               LEVEL4_CODE,
               LEVEL3_NAME,
               LEVEL3_CODE,
               LEVEL2_NAME,
               LEVEL2_CODE,
               LEVEL1_NAME,
               LEVEL1_CODE,
               NMBR_OF_ORGS
          FROM ORGUSER_DETAILS   OUSR,
               ORG_NODE_DIM_HIER ORG,
               ORG_LSTNODE_LINK  LST
         WHERE LST.ORG_NODEID = OUSR.ORG_NODEID ---HIGHEST_ORG_NODE
           AND ORG.ORG_NODEID = LST.ORG_LSTNODEID
           AND OUSR.USERID = p_userid
           AND OUSR.ORG_NODEID = p_org_nodeid
           AND OUSR.CUSTOMERID = p_customerid
           AND OUSR.ADMINID = LST.ADMINID;
    
      INSERT INTO ORGUSER_MAPPING_SEARCH
        SELECT OUSR.org_nodeid org_nodeid,
               OUSR.ORG_NODE_NAME,
               OUSR.USERID,
               OUSR.USERNAME,
               OUSR.CUSTOMERID,
               OUSR.HIGHEST_ORG_NODE,
               OUSR.parent_org_nodeid,
               OUSR.ADMINID,
               org.ORG_NODEID lowest_nodeid,
               OUSR.ORG_NODE_LEVEL,
               LEVEL8_NAME,
               LEVEL8_CODE,
               ORG.ORG_NODE_NAME AS EXAMINER_NAME,
               ORG.ORG_NODE_CODE AS EXAMINER_CODE,
               LEVEL7_NAME,
               LEVEL7_CODE,
               LEVEL6_NAME,
               LEVEL6_CODE,
               LEVEL5_NAME,
               LEVEL5_CODE,
               LEVEL4_NAME,
               LEVEL4_CODE,
               LEVEL3_NAME,
               LEVEL3_CODE,
               LEVEL2_NAME,
               LEVEL2_CODE,
               LEVEL1_NAME,
               LEVEL1_CODE,
               NMBR_OF_ORGS
          FROM ORGUSER_DETAILS   OUSR,
               ORG_NODE_DIM_HIER ORG,
               ORG_LSTNODE_LINK  LST
         WHERE LST.ORG_NODEID = OUSR.HIGHEST_ORG_NODE
           AND ORG.ORG_NODEID = LST.ORG_LSTNODEID
           AND OUSR.USERID = p_userid
           AND OUSR.ORG_NODEID = p_org_nodeid
           AND OUSR.CUSTOMERID = p_customerid
           AND OUSR.ADMINID = LST.ADMINID;
      -- AND ORG.ADMINID = LST.ADMINID ;
    ELSE
    
      --INSERT INTO temp_task VALUES (p_userid,p_org_nodeid,p_customerid, p_trigger_type ) ;
    
      DELETE FROM ORGUSER_DETAILS
       WHERE USERID = p_userid
         AND CUSTOMERID = p_customerid
         AND ORG_NODEID = p_org_nodeid;
      DELETE FROM ORGUSER_MAPPING
       WHERE USERID = p_userid
         AND CUSTOMERID = p_customerid
         AND ORG_NODEID = p_org_nodeid;
      DELETE FROM ORGUSER_MAPPING_SEARCH
       WHERE USERID = p_userid
         AND CUSTOMERID = p_customerid
         AND ORG_NODEID = p_org_nodeid;
    END IF;
  
    --COMMIT;
  
  END SP_SEARCH_ORG_HIER;

  PROCEDURE SP_SEARCH_ORG_HIER(p_customerid CUSTOMER_INFO.CUSTOMERID%TYPE) AS
    --PRAGMA AUTONOMOUS_TRANSACTION ;
  BEGIN
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE ORG_NODE_DIM_HIER' ;
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE ORGUSER_DETAILS' ;
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE ORGUSER_MAPPING' ;
  
    /*INSERT INTO  ORG_NODE_DIM_HIER
                   SELECT DISTINCT ORG.ORG_NODEID,
                                   ORG.ORG_NODE_NAME,
                                   ORG.PARENT_ORG_NODEID,
                                   ORG.ORG_NODE_CODE,
                                   ORG.ORG_NODE_LEVEL,
                                   ORG.ADMINID,
                                   CONNECT_BY_ROOT(ORG.ORG_NODEID) AS HIGHEST_ORG_NODE,
                                   LTRIM(SYS_CONNECT_BY_PATH(ORG.ORG_NODEID, '~'),'~') AS ORG_NODE_CODE_PATH,
                                   LTRIM(SYS_CONNECT_BY_PATH(ORG.ORG_NODE_NAME, '~'),'~') AS ORG_NODE_NAME_PATH
                            FROM  ORG_NODE_DIM ORG
                            START WITH ORG.ORG_NODE_LEVEL =1
                            CONNECT BY NOCYCLE PRIOR  ORG.ORG_NODEID=ORG.PARENT_ORG_NODEID;
    */
  
    /* DELETE FROM ORGUSER_DETAILS WHERE CUSTOMERID = p_customerid;*/
    DELETE FROM ORGUSER_MAPPING WHERE CUSTOMERID = p_customerid;
    DELETE FROM ORGUSER_MAPPING_SEARCH WHERE CUSTOMERID = p_customerid;
  
    INSERT INTO ORGUSER_DETAILS
      SELECT OU.USERID,
             USR.USERNAME,
             USR.CUSTOMERID,
             ORG.ORG_NODEID,
             ORG.ORG_NODE_NAME,
             ORG.PARENT_ORG_NODEID,
             ORG.HIGHEST_ORG_NODE,
             --ORG.ORG_NODE_CODE_PATH,
             ORG.ORG_NODE_LEVEL,
             OU.ADMINID
        FROM ORG_NODE_DIM_HIER ORG, ORG_USERS OU, USERS USR
       WHERE OU.ORG_NODEID = ORG.ORG_NODEID
         AND ORG.ORG_NODE_LEVEL = OU.ORG_NODE_LEVEL
         AND OU.USERID = USR.USERID
            --AND   USR.USERID = p_userid
         AND USR.CUSTOMERID = p_customerid
            --AND   ORG.ORG_NODEID = p_org_nodeid
         AND OU.ORG_NODE_LEVEL <> 0
         AND ORG.customerid = USR.customerid
         AND OU.ACTIVATION_STATUS = 'AC';
  
    INSERT INTO ORGUSER_MAPPING
      SELECT OUSR.org_nodeid org_nodeid,
             OUSR.ORG_NODE_NAME,
             OUSR.USERID,
             OUSR.USERNAME,
             OUSR.CUSTOMERID,
             OUSR.HIGHEST_ORG_NODE,
             OUSR.parent_org_nodeid,
             OUSR.ADMINID,
             org.ORG_NODEID lowest_nodeid,
             OUSR.ORG_NODE_LEVEL,
             LEVEL8_NAME,
             LEVEL8_CODE,
             ORG.ORG_NODE_NAME AS EXAMINER_NAME,
             ORG.ORG_NODE_CODE AS EXAMINER_CODE,
             LEVEL7_NAME,
             LEVEL7_CODE,
             LEVEL6_NAME,
             LEVEL6_CODE,
             LEVEL5_NAME,
             LEVEL5_CODE,
             LEVEL4_NAME,
             LEVEL4_CODE,
             LEVEL3_NAME,
             LEVEL3_CODE,
             LEVEL2_NAME,
             LEVEL2_CODE,
             LEVEL1_NAME,
             LEVEL1_CODE,
             NMBR_OF_ORGS
      /*(SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 8) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL8_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 8) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL8_CODE,
                         ORG.ORG_NODE_NAME AS EXAMINER_NAME,
                         ORG.ORG_NODE_CODE AS EXAMINER_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 7) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL7_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 7) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL7_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 7) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL6_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 6) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL6_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 5) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL5_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 5) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL5_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 4) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL4_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 4) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL4_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 3) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL3_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 3) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL3_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 2) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL2_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 2) - 1)
                             AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL2_CODE,
                         (SELECT DISTINCT ORG1.ORG_NODE_NAME
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 1) - 1)
                          \*AND ORG1.PARENT_ORG_NODEID <> 0*\
                          ) AS LEVEL1_NAME,
                         (SELECT DISTINCT ORG1.ORG_NODE_CODE
                            FROM ORG_NODE_DIM_HIER ORG1
                           WHERE ORG1.ORG_NODE_CODE_PATH =
                                 SUBSTR(ORG.ORG_NODE_CODE_PATH,
                                        1,
                                        INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 1) - 1)
                          \*AND ORG1.PARENT_ORG_NODEID <> 0*\
                          ) AS LEVEL1_CODE,
                         REGEXP_COUNT(ORG.ORG_NODE_CODE_PATH, '~', 1, 'i') AS NMBR_OF_ORGS*/
        FROM ORGUSER_DETAILS   OUSR,
             ORG_NODE_DIM_HIER ORG,
             ORG_LSTNODE_LINK  LST
       WHERE LST.ORG_NODEID = OUSR.ORG_NODEid
            --LST.ORG_NODEID = OUSR.HIGHEST_ORG_NODE
         AND ORG.ORG_NODEID = LST.ORG_LSTNODEID
            --AND OUSR.USERID = p_userid
            --AND OUSR.ORG_NODEID = p_org_nodeid
         AND OUSR.CUSTOMERID = p_customerid
         AND OUSR.ADMINID = LST.ADMINID;
    -- AND ORG.ADMINID = LST.ADMINID ;
  
    INSERT INTO ORGUSER_MAPPING_SEARCH
      SELECT OUSR.org_nodeid org_nodeid,
             OUSR.ORG_NODE_NAME,
             OUSR.USERID,
             OUSR.USERNAME,
             OUSR.CUSTOMERID,
             OUSR.HIGHEST_ORG_NODE,
             OUSR.parent_org_nodeid,
             OUSR.ADMINID,
             org.ORG_NODEID lowest_nodeid,
             OUSR.ORG_NODE_LEVEL,
             LEVEL8_NAME,
             LEVEL8_CODE,
             ORG.ORG_NODE_NAME AS EXAMINER_NAME,
             ORG.ORG_NODE_CODE AS EXAMINER_CODE,
             LEVEL7_NAME,
             LEVEL7_CODE,
             LEVEL6_NAME,
             LEVEL6_CODE,
             LEVEL5_NAME,
             LEVEL5_CODE,
             LEVEL4_NAME,
             LEVEL4_CODE,
             LEVEL3_NAME,
             LEVEL3_CODE,
             LEVEL2_NAME,
             LEVEL2_CODE,
             LEVEL1_NAME,
             LEVEL1_CODE,
             NMBR_OF_ORGS
        FROM ORGUSER_DETAILS   OUSR,
             ORG_NODE_DIM_HIER ORG,
             ORG_LSTNODE_LINK  LST
       WHERE LST.ORG_NODEID = OUSR.HIGHEST_ORG_NODE
         AND ORG.ORG_NODEID = LST.ORG_LSTNODEID
            -- AND OUSR.USERID = p_userid
            -- AND OUSR.ORG_NODEID = p_org_nodeid
         AND OUSR.CUSTOMERID = p_customerid
         AND OUSR.ADMINID = LST.ADMINID;
  
    /*  INSERT INTO ORGUSER_MAPPING_SEARCH
    SELECT OUSR.org_nodeid org_nodeid,
           OUSR.ORG_NODE_NAME,
           OUSR.USERID,
           OUSR.USERNAME,
           OUSR.CUSTOMERID,
           OUSR.HIGHEST_ORG_NODE,
           OUSR.parent_org_nodeid,
           OUSR.ADMINID,
           org.ORG_NODEID lowest_nodeid,
           OUSR.ORG_NODE_LEVEL,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 8) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL8_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 8) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL8_CODE,
           ORG.ORG_NODE_NAME AS EXAMINER_NAME,
           ORG.ORG_NODE_CODE AS EXAMINER_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 7) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL7_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 7) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL7_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 7) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL6_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 6) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL6_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 5) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL5_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 5) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL5_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 4) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL4_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 4) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL4_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 3) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL3_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 3) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL3_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 2) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL2_NAME,
           (SELECT DISTINCT ORG1.ORG_NODE_CODE
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 2) - 1)
               AND ORG1.PARENT_ORG_NODEID <> 0) AS LEVEL2_CODE,
           (SELECT DISTINCT ORG1.ORG_NODE_NAME
              FROM ORG_NODE_DIM_HIER ORG1
             WHERE ORG1.ORG_NODE_CODE_PATH =
                   SUBSTR(ORG.ORG_NODE_CODE_PATH,
                          1,
                          INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 1) - 1)
            /*AND ORG1.PARENT_ORG_NODEID <> 0*/
    /*  ) AS LEVEL1_NAME,
    (SELECT DISTINCT ORG1.ORG_NODE_CODE
       FROM ORG_NODE_DIM_HIER ORG1
      WHERE ORG1.ORG_NODE_CODE_PATH =
            SUBSTR(ORG.ORG_NODE_CODE_PATH,
                   1,
                   INSTR(ORG.ORG_NODE_CODE_PATH, '~', 1, 1) - 1) */
    /*AND ORG1.PARENT_ORG_NODEID <> 0*/
    /*   ) AS LEVEL1_CODE,
            REGEXP_COUNT(ORG.ORG_NODE_CODE_PATH, '~', 1, 'i') AS NMBR_OF_ORGS
       FROM ORGUSER_DETAILS   OUSR,
            ORG_NODE_DIM_HIER ORG,
            ORG_LSTNODE_LINK  LST
      WHERE LST.ORG_NODEID = OUSR.HIGHEST_ORG_NODE
        AND ORG.ORG_NODEID = LST.ORG_LSTNODEID
        AND OUSR.CUSTOMERID = p_customerid
        AND OUSR.ADMINID = LST.ADMINID;
    */
    COMMIT;
  
  END SP_SEARCH_ORG_HIER;

  PROCEDURE SP_STUDENT_DEMO_VALUES(P_PROCESS_ID IN NUMBER) AS
  
  BEGIN
  
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE STU_DEMO_VAL' ;
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE table_1_stu' ;
    /*DELETE FROM STU_DEMO_VAL;
    DELETE FROM STU_DEMO_VAL_1;*/
  
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE STU_DEMO_VAL' ;
    --EXECUTE IMMEDIATE 'TRUNCATE TABLE table_1_stu' ;
    /*DELETE FROM STU_DEMO_VAL;
    DELETE FROM STU_DEMO_VAL_1;*/
  
    /*
       
       -- This is commented for fix of TASC refresh issue
       
       INSERT INTO STU_SUB_DETAILS
       SELECT (SELECT SUBTESTID FROM SUBTEST_DIM WHERE SUBTEST_CODE = CONTENT_NAME) subtestid ,
          -- b.process_id ,
          -- b.test_element_id ,
          (SELECT STUDENT_BIO_ID
             FROM STUDENT_BIO_DIM
            WHERE TEST_ELEMENT_ID = B.TEST_ELEMENT_ID
              AND CUSTOMERID =
                  (SELECT DISTINCT H.CUSTOMER_ID
                     FROM STG_HIER_DETAILS H
                    WHERE H.PROCESS_ID = A.PROCESS_ID))
     FROM STG_PROCESS_STATUS      A,
          STG_STD_BIO_DETAILS     B,
          STG_STD_SUBTEST_DETAILS C
    WHERE A.PROCESS_ID = P_PROCESS_ID
    AND A.PROCESS_ID = B.PROCESS_ID
      AND A.PROCESS_ID = C.PROCESS_ID
      AND B.STUDENT_BIO_DETAILS_ID = C.STUDENT_BIO_DETAILS_ID;*/
  
    -- This is added as fix of TASC refresh issue 
  
    INSERT INTO STU_SUB_DETAILS
      SELECT SUBTESTID,
             (SELECT STUDENT_BIO_ID
                FROM STUDENT_BIO_DIM
               WHERE TEST_ELEMENT_ID = B.TEST_ELEMENT_ID
                 AND CUSTOMERID =
                     (SELECT DISTINCT H.CUSTOMER_ID
                        FROM STG_HIER_DETAILS H
                       WHERE H.PROCESS_ID = A.PROCESS_ID
                       AND H.WKF_PARTITION_NAME = A.WKF_PARTITION_NAME))
        FROM STG_PROCESS_STATUS A,
             STG_STD_BIO_DETAILS B,
             (SELECT SUBTESTID FROM SUBTEST_DIM) C
       WHERE A.PROCESS_ID = P_PROCESS_ID
         AND A.PROCESS_ID = B.PROCESS_ID
         AND B.WKF_PARTITION_NAME = A.WKF_PARTITION_NAME;
  
    /*\*   AND EXISTS (SELECT 1
            FROM STUDENT_BIO_DIM A
           WHERE B.TEST_ELEMENT_ID = A.TEST_ELEMENT_ID)*\
    AND  A.PROCESS_ID = P_PROCESS_ID ;
     --AND TEST_ELEMENT_ID = 1175317531*/
  
    DELETE FROM table_1_stu a
     WHERE EXISTS (SELECT 1
              FROM STU_SUB_DETAILS b
             WHERE a.STUDENT_BIO_ID = b.student_bio_id
               AND a.subtestid = b.subtestid);
  
    INSERT INTO table_1_stu
      (STUDENT_BIO_ID,
       FIRST_NAME,
       MIDDLE_NAME,
       LAST_NAME,
       BIRTHDATE,
       TEST_ELEMENT_ID,
       INT_STUDENT_ID,
       EXT_STUDENT_ID,
       STUDENT_ID,
       GENDERID,
       GRADEID,
       EDU_CENTERID,
       ORG_NODEID,
       STUDENT_MODE,
       ADMINID,
       CUSTOMERID,
       SUBTESTID,
       SUBTEST_NAME,
       SUBTEST_SEQ,
       DATE_TEST_TAKEN)
      SELECT SBD.STUDENT_BIO_ID,
             SBD.FIRST_NAME,
             SBD.MIDDLE_NAME,
             SBD.LAST_NAME,
             SBD.BIRTHDATE,
             SBD.TEST_ELEMENT_ID,
             SBD.INT_STUDENT_ID,
             SBD.EXT_STUDENT_ID,
             CASE
               WHEN LENGTH(TO_CHAR(SBD.EXT_STUDENT_ID)) >= 5 THEN
                'XXXXX' || SUBSTR(TO_CHAR(SBD.EXT_STUDENT_ID), 6)
               WHEN SBD.EXT_STUDENT_ID IS NULL OR
                    LENGTH(TO_CHAR(SBD.EXT_STUDENT_ID)) = 0 THEN
                SBD.EXT_STUDENT_ID
               ELSE
                'XXXXX'
             END AS student_id,
             SBD.GENDERID,
             SBD.GRADEID,
             SBD.EDU_CENTERID,
             SBD.ORG_NODEID,
             SBD.STUDENT_MODE,
             SBD.ADMINID,
             SBD.CUSTOMERID,
             SBD.SUBTESTID,
             SBD.SUBTEST_NAME,
             SBD.SUBTEST_SEQ,
             TRUNC(sdv.TEST_DATE)
        FROM (SELECT * FROM STUDENT_BIO_DIM, SUBTEST_DIM) SBD,
             subtest_score_fact sdv
       WHERE SDV.STUDENT_BIO_ID(+) = SBD.STUDENT_BIO_ID
         AND sdv.subtestid(+) = sbd.subtestid
         AND EXISTS (SELECT 1
                FROM STU_SUB_DETAILS b
               WHERE SBD.STUDENT_BIO_ID = b.student_bio_id
                 AND SBD.subtestid = b.subtestid);
  
    COMMIT;
  
  END SP_STUDENT_DEMO_VALUES;

  --PROCEDURE TO FETCH USER DATA
  PROCEDURE SP_GET_USER_DATA(P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                             P_OUT_CUR_USER_DATA OUT GET_REFCURSOR) IS
  
  BEGIN
  
    OPEN P_OUT_CUR_USER_DATA FOR
    
      SELECT A3.USERNAME,
             A3.FULLNAME,
             A3.STATUS,
             A3.ORG_NODE_NAME,
             LISTAGG(A3.DESCRIPTION, ', ') WITHIN
       GROUP(
       ORDER BY A3.USERNAME) AS DESCRIPTION
        FROM (SELECT A2.USERNAME,
                     A2.FULLNAME,
                     A2.STATUS,
                     A2.ORG_NODE_NAME,
                     CONCAT(CONCAT(A2.ORG_LABEL, ' '), A2.DESCRIPTION) AS DESCRIPTION
                FROM (SELECT A1.USERNAME,
                             A1.FULLNAME,
                             A1.STATUS,
                             A1.ORG_NODE_NAME,
                             A1.ORG_LABEL,
                             EXTRACTVALUE(T.COLUMN_VALUE, '/E') AS DESCRIPTION
                        FROM (SELECT U.USERNAME,
                                     U.LAST_NAME || ' ' || U.FIRST_NAME AS FULLNAME,
                                     DECODE(U.ACTIVATION_STATUS,
                                            'AC',
                                            'ENABLED',
                                            'IN',
                                            'DISABLED',
                                            '') AS STATUS,
                                     OND.ORG_NODE_NAME,
                                     OND.ORG_LABEL,
                                     A.DESCRIPTION
                                FROM USERS U,
                                     (SELECT DISTINCT OM.USERID,
                                                      OM.ORG_NODEID,
                                                      OM.ORG_NODE_NAME,
                                                      OM.ORG_NODE_LEVEL,
                                                      OTS.ORG_LABEL
                                        FROM ORGUSER_MAPPING  OM,
                                             ORG_TP_STRUCTURE OTS
                                       WHERE OM.ORG_NODE_LEVEL = OTS.ORG_LEVEL
                                         AND (OM.ORG_NODEID = P_IN_ORG_NODEID OR
                                             HIGHEST_ORG_NODE =
                                             P_IN_ORG_NODEID OR
                                             PARENT_ORG_NODEID =
                                             P_IN_ORG_NODEID OR
                                             LOWEST_NODEID = P_IN_ORG_NODEID)) OND,
                                     (SELECT U1.USERID,
                                             LISTAGG(RO.DESCRIPTION, ', ') WITHIN
                                       GROUP(
                                       ORDER BY RO.DESCRIPTION) AS DESCRIPTION
                                        FROM USER_ROLE UR, ROLE RO, USERS U1
                                       WHERE UR.USERID = U1.USERID
                                         AND RO.ROLEID = UR.ROLEID
                                       GROUP BY U1.USERID) A
                              
                               WHERE U.USERID = OND.USERID
                                 AND A.USERID = U.USERID
                                 AND U.ACTIVATION_STATUS != 'SS'
                                 AND OND.ORG_NODE_LEVEL IN (1, 3)
                               ORDER BY U.USERNAME) A1,
                             
                             TABLE(XMLSEQUENCE(XMLTYPE('<E><E>' ||
                                                       REPLACE(A1.DESCRIPTION,
                                                               ',',
                                                               '</E><E>') ||
                                                       '</E></E>')
                                               .EXTRACT('E/E'))) T) A2) A3
       GROUP BY A3.USERNAME, A3.FULLNAME, A3.STATUS, A3.ORG_NODE_NAME
       ORDER BY A3.USERNAME;
  
  END SP_GET_USER_DATA;

END PKG_PRF_ORG_USR;
/
