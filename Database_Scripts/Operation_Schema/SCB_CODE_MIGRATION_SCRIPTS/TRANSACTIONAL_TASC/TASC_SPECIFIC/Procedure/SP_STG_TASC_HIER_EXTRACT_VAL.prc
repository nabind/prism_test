CREATE OR REPLACE PROCEDURE SP_STG_TASC_HIER_EXTRACT_VAL(IN_PROCESS_ID IN NUMBER,
                                                         OUT_FLAG      OUT NUMBER) IS
  /*Returns 1 if there is a ISSUE else returns NULL*/
  V_FLAG           VARCHAR2(1) := NULL;
  V_PROCESS_ID     NUMBER := 0;
  V_LOG            VARCHAR2(4000) := NULL;
  V_IN_PROCESS_ID  NUMBER := IN_PROCESS_ID;
  V_ERR_PROCESS_ID NUMBER := 0;
  V_ERRM           VARCHAR2(300) := NULL;
  V_COUNT          NUMBER := 0;

BEGIN

  SELECT S.PROCESS_ID, S.PROCESS_LOG
    INTO V_PROCESS_ID, V_LOG
    FROM STG_HIER_PROCESS_STATUS S
   WHERE S.PROCESS_ID = V_IN_PROCESS_ID
     AND S.HIER_VALIDATION NOT IN ('VA', 'CO', 'ER');

  /*ORGTP CHECK START*/
  IF (V_FLAG IS NULL) THEN

    V_COUNT := 0;

    SELECT COUNT(1)
      INTO V_COUNT
      FROM (SELECT DISTINCT F.PROCESS_ID
              FROM STG_TASC_HIER_EXTRACT F
             WHERE F.ORG_TP IS NULL
                OR LENGTH(F.ORG_TP) <> 10
                OR (SELECT DISTINCT T.CUSTOMERID
                      FROM TEST_PROGRAM T
                     WHERE T.TP_CODE = F.ORG_TP) IS NULL);

    IF (V_COUNT > 0) THEN
      SELECT PROCESS_ID
        INTO V_ERR_PROCESS_ID
        FROM (SELECT DISTINCT F.PROCESS_ID
                FROM STG_TASC_HIER_EXTRACT F
               WHERE F.ORG_TP IS NULL
                  OR LENGTH(F.ORG_TP) <> 10
                  OR (SELECT DISTINCT T.CUSTOMERID
                        FROM TEST_PROGRAM T
                       WHERE T.TP_CODE = F.ORG_TP) IS NULL);

      IF (V_ERR_PROCESS_ID IS NOT NULL) THEN
        V_FLAG := 'Y';
        V_ERRM := '[' || TO_CHAR(SYSDATE, 'HH:MI:SS PM') || ']' ||
                  ' ORG_TP ERROR IN FILE, either ORG_TP is null or ORG_TP is not 10 Characters or ORG_TP is not matching';
        V_LOG  := V_LOG || CHR(10) || V_ERRM;
      END IF;
    END IF;
  END IF;
  /*ORGTP CHECK END*/

  /*FIELD VALIDATION START*/
  IF (V_FLAG IS NULL) THEN

    V_COUNT := 0;

    SELECT COUNT(*)
      INTO V_COUNT
      FROM (SELECT PROCESS_ID, LISTAGG(ERRM, ';') WITHIN
             GROUP(
             ORDER BY ERRM) AS ERRM
              FROM (SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_A_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_A_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_A_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_A_LABEL IS NULL'
                                      WHEN F.ELEMENT_A_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_A_LEVEL IS NULL'
                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_A_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_A_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_A_STRUCTURE_LEVEL IS NULL)
                       AND 1 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID)

                    UNION ALL

                    SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_B_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_B_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_B_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_B_LABEL IS NULL'
                                      WHEN F.ELEMENT_B_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_B_LEVEL IS NULL'
                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_B_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_B_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_B_STRUCTURE_LEVEL IS NULL)
                       AND 2 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID)
                    UNION ALL

                    SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_C_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_C_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_C_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_C_LABEL IS NULL'
                                      WHEN F.ELEMENT_C_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_C_LEVEL IS NULL'

                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_C_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_C_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_C_STRUCTURE_LEVEL IS NULL)
                       AND 3 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID)

                    UNION ALL

                    SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_D_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_D_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_D_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_D_LABEL IS NULL'
                                      WHEN F.ELEMENT_D_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_D_LEVEL IS NULL'

                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_D_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_D_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_D_STRUCTURE_LEVEL IS NULL)
                       AND 4 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID)

                    UNION ALL

                    SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_E_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_E_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_E_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_E_LABEL IS NULL'
                                      WHEN F.ELEMENT_E_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_E_LEVEL IS NULL'

                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_E_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_E_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_E_STRUCTURE_LEVEL IS NULL)
                       AND 5 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID)

                    UNION ALL

                    SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_F_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_F_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_F_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_F_LABEL IS NULL'
                                      WHEN F.ELEMENT_F_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_F_LEVEL IS NULL'

                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_F_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_F_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_F_STRUCTURE_LEVEL IS NULL )
                       AND 6 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID)

                    UNION ALL

                    SELECT DISTINCT F.PROCESS_ID,
                                    CASE
                                      WHEN F.ELEMENT_G_ORG_NODE_CODE IS NULL THEN
                                       'ELEMENT_G_ORG_CODE IS NULL'
                                      WHEN F.ELEMENT_G_HIERARCHY_LABEL IS NULL THEN
                                       'ELEMENT_G_LABEL IS NULL'
                                      WHEN F.ELEMENT_G_STRUCTURE_LEVEL IS NULL THEN
                                       'ELEMENT_G_LEVEL IS NULL'

                                    END AS ERRM
                      FROM STG_TASC_HIER_EXTRACT F
                     WHERE (F.ELEMENT_G_ORG_NODE_CODE IS NULL OR
                           F.ELEMENT_G_HIERARCHY_LABEL IS NULL OR
                           F.ELEMENT_G_STRUCTURE_LEVEL IS NULL )
                       AND 7 <= (SELECT MAX(ORG_LEVEL)
                                   FROM ORG_TP_STRUCTURE A,
                                        (SELECT DISTINCT TP_ID
                                           FROM TEST_PROGRAM          T,
                                                STG_TASC_HIER_EXTRACT H
                                          WHERE T.TP_CODE = H.ORG_TP) B
                                  WHERE A.TP_ID = B.TP_ID))
             GROUP BY PROCESS_ID);

    IF (V_COUNT > 0) THEN
      SELECT PROCESS_ID, LISTAGG(ERRM, ';') WITHIN
       GROUP(
       ORDER BY ERRM) AS ERRM
        INTO V_ERR_PROCESS_ID, V_ERRM
        FROM (SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_A_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_A_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_A_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_A_LABEL IS NULL'
                                WHEN F.ELEMENT_A_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_A_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_A_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_A_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_A_STRUCTURE_LEVEL IS NULL)
                 AND 1 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID)

              UNION ALL

              SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_B_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_B_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_B_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_B_LABEL IS NULL'
                                WHEN F.ELEMENT_B_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_B_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_B_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_B_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_B_STRUCTURE_LEVEL IS NULL)
                 AND 2 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID)
              UNION ALL

              SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_C_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_C_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_C_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_C_LABEL IS NULL'
                                WHEN F.ELEMENT_C_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_C_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_C_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_C_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_C_STRUCTURE_LEVEL IS NULL)
                 AND 3 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID)

              UNION ALL

              SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_D_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_D_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_D_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_D_LABEL IS NULL'
                                WHEN F.ELEMENT_D_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_D_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_D_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_D_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_D_STRUCTURE_LEVEL IS NULL)
                 AND 4 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID)

              UNION ALL

              SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_E_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_E_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_E_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_E_LABEL IS NULL'
                                WHEN F.ELEMENT_E_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_E_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_E_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_E_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_E_STRUCTURE_LEVEL IS NULL)
                 AND 5 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID)

              UNION ALL

              SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_F_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_F_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_F_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_F_LABEL IS NULL'
                                WHEN F.ELEMENT_F_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_F_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_F_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_F_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_F_STRUCTURE_LEVEL IS NULL)
                 AND 6 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID)

              UNION ALL

              SELECT DISTINCT F.PROCESS_ID,
                              CASE
                                WHEN F.ELEMENT_G_ORG_NODE_CODE IS NULL THEN
                                 'ELEMENT_G_ORG_CODE IS NULL'
                                WHEN F.ELEMENT_G_HIERARCHY_LABEL IS NULL THEN
                                 'ELEMENT_G_LABEL IS NULL'
                                WHEN F.ELEMENT_G_STRUCTURE_LEVEL IS NULL THEN
                                 'ELEMENT_G_LEVEL IS NULL'

                              END AS ERRM
                FROM STG_TASC_HIER_EXTRACT F
               WHERE (F.ELEMENT_G_ORG_NODE_CODE IS NULL OR
                     F.ELEMENT_G_HIERARCHY_LABEL IS NULL OR
                     F.ELEMENT_G_STRUCTURE_LEVEL IS NULL)
                 AND 7 <=
                     (SELECT MAX(ORG_LEVEL)
                        FROM ORG_TP_STRUCTURE A,
                             (SELECT DISTINCT TP_ID
                                FROM TEST_PROGRAM T, STG_TASC_HIER_EXTRACT H
                               WHERE T.TP_CODE = H.ORG_TP) B
                       WHERE A.TP_ID = B.TP_ID))
       GROUP BY PROCESS_ID;

      IF (V_ERR_PROCESS_ID IS NOT NULL) THEN
        V_FLAG := 'Y';
        V_ERRM := '[' || TO_CHAR(SYSDATE, 'HH:MI:SS PM') ||
                  '] FIELD VALUE VALIDATION ERROR IN FILE. ' || V_ERRM;
        V_LOG  := V_LOG || CHR(10) || V_ERRM;
      END IF;

    END IF;
  END IF;
  /*FIELD VALIDATION END*/

  /*HIER DUPLICATE VALIDATION WHOLE ORG START*/
  IF (V_FLAG IS NULL) THEN

    V_COUNT := 0;

    SELECT COUNT(*)
      INTO V_COUNT
      FROM (SELECT PROCESS_ID, LISTAGG(ERRM, ' :: ') WITHIN
             GROUP(
             ORDER BY ERRM) AS ERRM
              FROM (SELECT F.PROCESS_ID,
                           F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_D_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_E_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_F_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_G_ORG_NODE_CODE AS ERRM
                      FROM (SELECT F.PROCESS_ID,
                                   F.ELEMENT_A_ORG_NODE_CODE,
                                   F.ELEMENT_A_STRUCTURE_LEVEL,
                                   F.ELEMENT_B_ORG_NODE_CODE,
                                   F.ELEMENT_B_STRUCTURE_LEVEL,
                                   F.ELEMENT_C_ORG_NODE_CODE,
                                   F.ELEMENT_C_STRUCTURE_LEVEL,
                                   F.ELEMENT_D_ORG_NODE_CODE,
                                   F.ELEMENT_D_STRUCTURE_LEVEL,
                                   F.ELEMENT_E_ORG_NODE_CODE,
                                   F.ELEMENT_E_STRUCTURE_LEVEL,
                                   F.ELEMENT_F_ORG_NODE_CODE,
                                   F.ELEMENT_F_STRUCTURE_LEVEL,
                                   F.ELEMENT_G_ORG_NODE_CODE,
                                   F.ELEMENT_G_STRUCTURE_LEVEL
                              FROM STG_TASC_HIER_EXTRACT F
                             WHERE (SELECT MAX(ORG_LEVEL)
                                      FROM ORG_TP_STRUCTURE A,
                                           (SELECT DISTINCT TP_ID
                                              FROM TEST_PROGRAM          T,
                                                   STG_TASC_HIER_EXTRACT H
                                             WHERE T.TP_CODE = H.ORG_TP) B
                                     WHERE A.TP_ID = B.TP_ID) = 7
                             GROUP BY F.PROCESS_ID,
                                      F.ELEMENT_A_ORG_NODE_CODE,
                                      F.ELEMENT_A_STRUCTURE_LEVEL,
                                      F.ELEMENT_B_ORG_NODE_CODE,
                                      F.ELEMENT_B_STRUCTURE_LEVEL,
                                      F.ELEMENT_C_ORG_NODE_CODE,
                                      F.ELEMENT_C_STRUCTURE_LEVEL,
                                      F.ELEMENT_D_ORG_NODE_CODE,
                                      F.ELEMENT_D_STRUCTURE_LEVEL,
                                      F.ELEMENT_E_ORG_NODE_CODE,
                                      F.ELEMENT_E_STRUCTURE_LEVEL,
                                      F.ELEMENT_F_ORG_NODE_CODE,
                                      F.ELEMENT_F_STRUCTURE_LEVEL,
                                      F.ELEMENT_G_ORG_NODE_CODE,
                                      F.ELEMENT_G_STRUCTURE_LEVEL
                            HAVING COUNT(*) > 1) F
                    UNION ALL
                    SELECT F.PROCESS_ID,
                           F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_D_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_E_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_F_ORG_NODE_CODE AS LIST_OF_ORGS
                      FROM (SELECT F.PROCESS_ID,
                                   F.ELEMENT_A_ORG_NODE_CODE,
                                   F.ELEMENT_A_STRUCTURE_LEVEL,
                                   F.ELEMENT_B_ORG_NODE_CODE,
                                   F.ELEMENT_B_STRUCTURE_LEVEL,
                                   F.ELEMENT_C_ORG_NODE_CODE,
                                   F.ELEMENT_C_STRUCTURE_LEVEL,
                                   F.ELEMENT_D_ORG_NODE_CODE,
                                   F.ELEMENT_D_STRUCTURE_LEVEL,
                                   F.ELEMENT_E_ORG_NODE_CODE,
                                   F.ELEMENT_E_STRUCTURE_LEVEL,
                                   F.ELEMENT_F_ORG_NODE_CODE,
                                   F.ELEMENT_F_STRUCTURE_LEVEL
                              FROM STG_TASC_HIER_EXTRACT F
                             WHERE (SELECT MAX(ORG_LEVEL)
                                      FROM ORG_TP_STRUCTURE A,
                                           (SELECT DISTINCT TP_ID
                                              FROM TEST_PROGRAM          T,
                                                   STG_TASC_HIER_EXTRACT H
                                             WHERE T.TP_CODE = H.ORG_TP) B
                                     WHERE A.TP_ID = B.TP_ID) = 6
                             GROUP BY F.PROCESS_ID,
                                      F.ELEMENT_A_ORG_NODE_CODE,
                                      F.ELEMENT_A_STRUCTURE_LEVEL,
                                      F.ELEMENT_B_ORG_NODE_CODE,
                                      F.ELEMENT_B_STRUCTURE_LEVEL,
                                      F.ELEMENT_C_ORG_NODE_CODE,
                                      F.ELEMENT_C_STRUCTURE_LEVEL,
                                      F.ELEMENT_D_ORG_NODE_CODE,
                                      F.ELEMENT_D_STRUCTURE_LEVEL,
                                      F.ELEMENT_E_ORG_NODE_CODE,
                                      F.ELEMENT_E_STRUCTURE_LEVEL,
                                      F.ELEMENT_F_ORG_NODE_CODE,
                                      F.ELEMENT_F_STRUCTURE_LEVEL
                            HAVING COUNT(*) > 1) F
                    UNION ALL
                    SELECT F.PROCESS_ID,
                           F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_D_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_E_ORG_NODE_CODE AS LIST_OF_ORGS
                      FROM (SELECT F.PROCESS_ID,
                                   F.ELEMENT_A_ORG_NODE_CODE,
                                   F.ELEMENT_A_STRUCTURE_LEVEL,
                                   F.ELEMENT_B_ORG_NODE_CODE,
                                   F.ELEMENT_B_STRUCTURE_LEVEL,
                                   F.ELEMENT_C_ORG_NODE_CODE,
                                   F.ELEMENT_C_STRUCTURE_LEVEL,
                                   F.ELEMENT_D_ORG_NODE_CODE,
                                   F.ELEMENT_D_STRUCTURE_LEVEL,
                                   F.ELEMENT_E_ORG_NODE_CODE,
                                   F.ELEMENT_E_STRUCTURE_LEVEL
                              FROM STG_TASC_HIER_EXTRACT F
                             WHERE (SELECT MAX(ORG_LEVEL)
                                      FROM ORG_TP_STRUCTURE A,
                                           (SELECT DISTINCT TP_ID
                                              FROM TEST_PROGRAM          T,
                                                   STG_TASC_HIER_EXTRACT H
                                             WHERE T.TP_CODE = H.ORG_TP) B
                                     WHERE A.TP_ID = B.TP_ID) = 5
                             GROUP BY F.PROCESS_ID,
                                      F.ELEMENT_A_ORG_NODE_CODE,
                                      F.ELEMENT_A_STRUCTURE_LEVEL,
                                      F.ELEMENT_B_ORG_NODE_CODE,
                                      F.ELEMENT_B_STRUCTURE_LEVEL,
                                      F.ELEMENT_C_ORG_NODE_CODE,
                                      F.ELEMENT_C_STRUCTURE_LEVEL,
                                      F.ELEMENT_D_ORG_NODE_CODE,
                                      F.ELEMENT_D_STRUCTURE_LEVEL,
                                      F.ELEMENT_E_ORG_NODE_CODE,
                                      F.ELEMENT_E_STRUCTURE_LEVEL
                            HAVING COUNT(*) > 1) F
                    UNION ALL
                    SELECT F.PROCESS_ID,
                           F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_D_ORG_NODE_CODE AS LIST_OF_ORGS
                      FROM (SELECT F.PROCESS_ID,
                                   F.ELEMENT_A_ORG_NODE_CODE,
                                   F.ELEMENT_A_STRUCTURE_LEVEL,
                                   F.ELEMENT_B_ORG_NODE_CODE,
                                   F.ELEMENT_B_STRUCTURE_LEVEL,
                                   F.ELEMENT_C_ORG_NODE_CODE,
                                   F.ELEMENT_C_STRUCTURE_LEVEL,
                                   F.ELEMENT_D_ORG_NODE_CODE,
                                   F.ELEMENT_D_STRUCTURE_LEVEL
                              FROM STG_TASC_HIER_EXTRACT F
                             WHERE (SELECT MAX(ORG_LEVEL)
                                      FROM ORG_TP_STRUCTURE A,
                                           (SELECT DISTINCT TP_ID
                                              FROM TEST_PROGRAM          T,
                                                   STG_TASC_HIER_EXTRACT H
                                             WHERE T.TP_CODE = H.ORG_TP) B
                                     WHERE A.TP_ID = B.TP_ID) = 4
                             GROUP BY F.PROCESS_ID,
                                      F.ELEMENT_A_ORG_NODE_CODE,
                                      F.ELEMENT_A_STRUCTURE_LEVEL,
                                      F.ELEMENT_B_ORG_NODE_CODE,
                                      F.ELEMENT_B_STRUCTURE_LEVEL,
                                      F.ELEMENT_C_ORG_NODE_CODE,
                                      F.ELEMENT_C_STRUCTURE_LEVEL,
                                      F.ELEMENT_D_ORG_NODE_CODE,
                                      F.ELEMENT_D_STRUCTURE_LEVEL
                            HAVING COUNT(*) > 1) F
                    UNION ALL
                    SELECT F.PROCESS_ID,
                           F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_C_ORG_NODE_CODE AS LIST_OF_ORGS
                      FROM (SELECT F.PROCESS_ID,
                                   F.ELEMENT_A_ORG_NODE_CODE,
                                   F.ELEMENT_A_STRUCTURE_LEVEL,
                                   F.ELEMENT_B_ORG_NODE_CODE,
                                   F.ELEMENT_B_STRUCTURE_LEVEL,
                                   F.ELEMENT_C_ORG_NODE_CODE,
                                   F.ELEMENT_C_STRUCTURE_LEVEL
                              FROM STG_TASC_HIER_EXTRACT F
                             WHERE (SELECT MAX(ORG_LEVEL)
                                      FROM ORG_TP_STRUCTURE A,
                                           (SELECT DISTINCT TP_ID
                                              FROM TEST_PROGRAM          T,
                                                   STG_TASC_HIER_EXTRACT H
                                             WHERE T.TP_CODE = H.ORG_TP) B
                                     WHERE A.TP_ID = B.TP_ID) = 3
                             GROUP BY F.PROCESS_ID,
                                      F.ELEMENT_A_ORG_NODE_CODE,
                                      F.ELEMENT_A_STRUCTURE_LEVEL,
                                      F.ELEMENT_B_ORG_NODE_CODE,
                                      F.ELEMENT_B_STRUCTURE_LEVEL,
                                      F.ELEMENT_C_ORG_NODE_CODE,
                                      F.ELEMENT_C_STRUCTURE_LEVEL
                            HAVING COUNT(*) > 1) F
                    UNION ALL
                    SELECT F.PROCESS_ID,
                           F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                           F.ELEMENT_B_ORG_NODE_CODE AS LIST_OF_ORGS
                      FROM (SELECT F.PROCESS_ID,
                                   F.ELEMENT_A_ORG_NODE_CODE,
                                   F.ELEMENT_A_STRUCTURE_LEVEL,
                                   F.ELEMENT_B_ORG_NODE_CODE,
                                   F.ELEMENT_B_STRUCTURE_LEVEL
                              FROM STG_TASC_HIER_EXTRACT F
                             WHERE (SELECT MAX(ORG_LEVEL)
                                      FROM ORG_TP_STRUCTURE A,
                                           (SELECT DISTINCT TP_ID
                                              FROM TEST_PROGRAM          T,
                                                   STG_TASC_HIER_EXTRACT H
                                             WHERE T.TP_CODE = H.ORG_TP) B
                                     WHERE A.TP_ID = B.TP_ID) = 2
                             GROUP BY F.PROCESS_ID,
                                      F.ELEMENT_A_ORG_NODE_CODE,
                                      F.ELEMENT_A_STRUCTURE_LEVEL,
                                      F.ELEMENT_B_ORG_NODE_CODE,
                                      F.ELEMENT_B_STRUCTURE_LEVEL
                            HAVING COUNT(*) > 1) F)
             GROUP BY PROCESS_ID);

    IF (V_COUNT > 0) THEN
      SELECT PROCESS_ID, LISTAGG(ERRM, ' :: ') WITHIN
       GROUP(
       ORDER BY ERRM) AS ERRM
        INTO V_ERR_PROCESS_ID, V_ERRM
        FROM (SELECT F.PROCESS_ID,
                     F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_D_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_E_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_F_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_G_ORG_NODE_CODE AS ERRM
                FROM (SELECT F.PROCESS_ID,
                             F.ELEMENT_A_ORG_NODE_CODE,
                             F.ELEMENT_A_STRUCTURE_LEVEL,
                             F.ELEMENT_B_ORG_NODE_CODE,
                             F.ELEMENT_B_STRUCTURE_LEVEL,
                             F.ELEMENT_C_ORG_NODE_CODE,
                             F.ELEMENT_C_STRUCTURE_LEVEL,
                             F.ELEMENT_D_ORG_NODE_CODE,
                             F.ELEMENT_D_STRUCTURE_LEVEL,
                             F.ELEMENT_E_ORG_NODE_CODE,
                             F.ELEMENT_E_STRUCTURE_LEVEL,
                             F.ELEMENT_F_ORG_NODE_CODE,
                             F.ELEMENT_F_STRUCTURE_LEVEL,
                             F.ELEMENT_G_ORG_NODE_CODE,
                             F.ELEMENT_G_STRUCTURE_LEVEL
                        FROM STG_TASC_HIER_EXTRACT F
                       WHERE (SELECT MAX(ORG_LEVEL)
                                FROM ORG_TP_STRUCTURE A,
                                     (SELECT DISTINCT TP_ID
                                        FROM TEST_PROGRAM          T,
                                             STG_TASC_HIER_EXTRACT H
                                       WHERE T.TP_CODE = H.ORG_TP) B
                               WHERE A.TP_ID = B.TP_ID) = 7
                       GROUP BY F.PROCESS_ID,
                                F.ELEMENT_A_ORG_NODE_CODE,
                                F.ELEMENT_A_STRUCTURE_LEVEL,
                                F.ELEMENT_B_ORG_NODE_CODE,
                                F.ELEMENT_B_STRUCTURE_LEVEL,
                                F.ELEMENT_C_ORG_NODE_CODE,
                                F.ELEMENT_C_STRUCTURE_LEVEL,
                                F.ELEMENT_D_ORG_NODE_CODE,
                                F.ELEMENT_D_STRUCTURE_LEVEL,
                                F.ELEMENT_E_ORG_NODE_CODE,
                                F.ELEMENT_E_STRUCTURE_LEVEL,
                                F.ELEMENT_F_ORG_NODE_CODE,
                                F.ELEMENT_F_STRUCTURE_LEVEL,
                                F.ELEMENT_G_ORG_NODE_CODE,
                                F.ELEMENT_G_STRUCTURE_LEVEL
                      HAVING COUNT(*) > 1) F
              UNION ALL
              SELECT F.PROCESS_ID,
                     F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_D_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_E_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_F_ORG_NODE_CODE AS LIST_OF_ORGS
                FROM (SELECT F.PROCESS_ID,
                             F.ELEMENT_A_ORG_NODE_CODE,
                             F.ELEMENT_A_STRUCTURE_LEVEL,
                             F.ELEMENT_B_ORG_NODE_CODE,
                             F.ELEMENT_B_STRUCTURE_LEVEL,
                             F.ELEMENT_C_ORG_NODE_CODE,
                             F.ELEMENT_C_STRUCTURE_LEVEL,
                             F.ELEMENT_D_ORG_NODE_CODE,
                             F.ELEMENT_D_STRUCTURE_LEVEL,
                             F.ELEMENT_E_ORG_NODE_CODE,
                             F.ELEMENT_E_STRUCTURE_LEVEL,
                             F.ELEMENT_F_ORG_NODE_CODE,
                             F.ELEMENT_F_STRUCTURE_LEVEL
                        FROM STG_TASC_HIER_EXTRACT F
                       WHERE (SELECT MAX(ORG_LEVEL)
                                FROM ORG_TP_STRUCTURE A,
                                     (SELECT DISTINCT TP_ID
                                        FROM TEST_PROGRAM          T,
                                             STG_TASC_HIER_EXTRACT H
                                       WHERE T.TP_CODE = H.ORG_TP) B
                               WHERE A.TP_ID = B.TP_ID) = 6
                       GROUP BY F.PROCESS_ID,
                                F.ELEMENT_A_ORG_NODE_CODE,
                                F.ELEMENT_A_STRUCTURE_LEVEL,
                                F.ELEMENT_B_ORG_NODE_CODE,
                                F.ELEMENT_B_STRUCTURE_LEVEL,
                                F.ELEMENT_C_ORG_NODE_CODE,
                                F.ELEMENT_C_STRUCTURE_LEVEL,
                                F.ELEMENT_D_ORG_NODE_CODE,
                                F.ELEMENT_D_STRUCTURE_LEVEL,
                                F.ELEMENT_E_ORG_NODE_CODE,
                                F.ELEMENT_E_STRUCTURE_LEVEL,
                                F.ELEMENT_F_ORG_NODE_CODE,
                                F.ELEMENT_F_STRUCTURE_LEVEL
                      HAVING COUNT(*) > 1) F
              UNION ALL
              SELECT F.PROCESS_ID,
                     F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_D_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_E_ORG_NODE_CODE AS LIST_OF_ORGS
                FROM (SELECT F.PROCESS_ID,
                             F.ELEMENT_A_ORG_NODE_CODE,
                             F.ELEMENT_A_STRUCTURE_LEVEL,
                             F.ELEMENT_B_ORG_NODE_CODE,
                             F.ELEMENT_B_STRUCTURE_LEVEL,
                             F.ELEMENT_C_ORG_NODE_CODE,
                             F.ELEMENT_C_STRUCTURE_LEVEL,
                             F.ELEMENT_D_ORG_NODE_CODE,
                             F.ELEMENT_D_STRUCTURE_LEVEL,
                             F.ELEMENT_E_ORG_NODE_CODE,
                             F.ELEMENT_E_STRUCTURE_LEVEL
                        FROM STG_TASC_HIER_EXTRACT F
                       WHERE (SELECT MAX(ORG_LEVEL)
                                FROM ORG_TP_STRUCTURE A,
                                     (SELECT DISTINCT TP_ID
                                        FROM TEST_PROGRAM          T,
                                             STG_TASC_HIER_EXTRACT H
                                       WHERE T.TP_CODE = H.ORG_TP) B
                               WHERE A.TP_ID = B.TP_ID) = 5
                       GROUP BY F.PROCESS_ID,
                                F.ELEMENT_A_ORG_NODE_CODE,
                                F.ELEMENT_A_STRUCTURE_LEVEL,
                                F.ELEMENT_B_ORG_NODE_CODE,
                                F.ELEMENT_B_STRUCTURE_LEVEL,
                                F.ELEMENT_C_ORG_NODE_CODE,
                                F.ELEMENT_C_STRUCTURE_LEVEL,
                                F.ELEMENT_D_ORG_NODE_CODE,
                                F.ELEMENT_D_STRUCTURE_LEVEL,
                                F.ELEMENT_E_ORG_NODE_CODE,
                                F.ELEMENT_E_STRUCTURE_LEVEL
                      HAVING COUNT(*) > 1) F
              UNION ALL
              SELECT F.PROCESS_ID,
                     F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_C_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_D_ORG_NODE_CODE AS LIST_OF_ORGS
                FROM (SELECT F.PROCESS_ID,
                             F.ELEMENT_A_ORG_NODE_CODE,
                             F.ELEMENT_A_STRUCTURE_LEVEL,
                             F.ELEMENT_B_ORG_NODE_CODE,
                             F.ELEMENT_B_STRUCTURE_LEVEL,
                             F.ELEMENT_C_ORG_NODE_CODE,
                             F.ELEMENT_C_STRUCTURE_LEVEL,
                             F.ELEMENT_D_ORG_NODE_CODE,
                             F.ELEMENT_D_STRUCTURE_LEVEL
                        FROM STG_TASC_HIER_EXTRACT F
                       WHERE (SELECT MAX(ORG_LEVEL)
                                FROM ORG_TP_STRUCTURE A,
                                     (SELECT DISTINCT TP_ID
                                        FROM TEST_PROGRAM          T,
                                             STG_TASC_HIER_EXTRACT H
                                       WHERE T.TP_CODE = H.ORG_TP) B
                               WHERE A.TP_ID = B.TP_ID) = 4
                       GROUP BY F.PROCESS_ID,
                                F.ELEMENT_A_ORG_NODE_CODE,
                                F.ELEMENT_A_STRUCTURE_LEVEL,
                                F.ELEMENT_B_ORG_NODE_CODE,
                                F.ELEMENT_B_STRUCTURE_LEVEL,
                                F.ELEMENT_C_ORG_NODE_CODE,
                                F.ELEMENT_C_STRUCTURE_LEVEL,
                                F.ELEMENT_D_ORG_NODE_CODE,
                                F.ELEMENT_D_STRUCTURE_LEVEL
                      HAVING COUNT(*) > 1) F
              UNION ALL
              SELECT F.PROCESS_ID,
                     F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_B_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_C_ORG_NODE_CODE AS LIST_OF_ORGS
                FROM (SELECT F.PROCESS_ID,
                             F.ELEMENT_A_ORG_NODE_CODE,
                             F.ELEMENT_A_STRUCTURE_LEVEL,
                             F.ELEMENT_B_ORG_NODE_CODE,
                             F.ELEMENT_B_STRUCTURE_LEVEL,
                             F.ELEMENT_C_ORG_NODE_CODE,
                             F.ELEMENT_C_STRUCTURE_LEVEL
                        FROM STG_TASC_HIER_EXTRACT F
                       WHERE (SELECT MAX(ORG_LEVEL)
                                FROM ORG_TP_STRUCTURE A,
                                     (SELECT DISTINCT TP_ID
                                        FROM TEST_PROGRAM          T,
                                             STG_TASC_HIER_EXTRACT H
                                       WHERE T.TP_CODE = H.ORG_TP) B
                               WHERE A.TP_ID = B.TP_ID) = 3
                       GROUP BY F.PROCESS_ID,
                                F.ELEMENT_A_ORG_NODE_CODE,
                                F.ELEMENT_A_STRUCTURE_LEVEL,
                                F.ELEMENT_B_ORG_NODE_CODE,
                                F.ELEMENT_B_STRUCTURE_LEVEL,
                                F.ELEMENT_C_ORG_NODE_CODE,
                                F.ELEMENT_C_STRUCTURE_LEVEL
                      HAVING COUNT(*) > 1) F
              UNION ALL
              SELECT F.PROCESS_ID,
                     F.ELEMENT_A_ORG_NODE_CODE || ',' ||
                     F.ELEMENT_B_ORG_NODE_CODE AS LIST_OF_ORGS
                FROM (SELECT F.PROCESS_ID,
                             F.ELEMENT_A_ORG_NODE_CODE,
                             F.ELEMENT_A_STRUCTURE_LEVEL,
                             F.ELEMENT_B_ORG_NODE_CODE,
                             F.ELEMENT_B_STRUCTURE_LEVEL
                        FROM STG_TASC_HIER_EXTRACT F
                       WHERE (SELECT MAX(ORG_LEVEL)
                                FROM ORG_TP_STRUCTURE A,
                                     (SELECT DISTINCT TP_ID
                                        FROM TEST_PROGRAM          T,
                                             STG_TASC_HIER_EXTRACT H
                                       WHERE T.TP_CODE = H.ORG_TP) B
                               WHERE A.TP_ID = B.TP_ID) = 2
                       GROUP BY F.PROCESS_ID,
                                F.ELEMENT_A_ORG_NODE_CODE,
                                F.ELEMENT_A_STRUCTURE_LEVEL,
                                F.ELEMENT_B_ORG_NODE_CODE,
                                F.ELEMENT_B_STRUCTURE_LEVEL
                      HAVING COUNT(*) > 1) F)
       GROUP BY PROCESS_ID;

      IF (V_ERR_PROCESS_ID IS NOT NULL) THEN
        V_FLAG := 'Y';
        V_ERRM := '[' || TO_CHAR(SYSDATE, 'HH:MI:SS PM') || ']' ||
                  ' DUPLICATE HIERARCHY PRESENT IN FILE. The duplicate Hierarchy Org codes are := ' ||
                  V_ERRM;
        V_LOG  := V_LOG || CHR(10) || V_ERRM;
      END IF;
    END IF;
  END IF;
  /*HIER DUPLICATE VALIDATION WHOLE ORG END*/

  IF (V_FLAG = 'Y') THEN
    UPDATE STG_HIER_PROCESS_STATUS U
       SET U.PROCESS_LOG = V_LOG, U.HIER_VALIDATION = 'ER'
     WHERE U.PROCESS_ID = V_PROCESS_ID;
    OUT_FLAG := 1;
  ELSE
    V_LOG := V_LOG || CHR(10) || '[' || TO_CHAR(SYSDATE, 'HH:MI:SS PM') || ']' ||
             ' STAGE VALIDATION COMPLETED WITHOUT ANY ERRORS';
    UPDATE STG_HIER_PROCESS_STATUS U
       SET U.PROCESS_LOG = V_LOG, U.HIER_VALIDATION = 'VA'
     WHERE U.PROCESS_ID = V_PROCESS_ID;
    OUT_FLAG := NULL;
  END IF;
  COMMIT;

END SP_STG_TASC_HIER_EXTRACT_VAL;
/
