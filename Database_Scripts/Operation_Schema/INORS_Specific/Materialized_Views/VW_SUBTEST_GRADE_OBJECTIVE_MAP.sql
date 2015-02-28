CREATE MATERIALIZED VIEW VW_SUBTEST_GRADE_OBJECTIVE_MAP
REFRESH COMPLETE ON DEMAND
AS
SELECT A.ASSESSMENTID,
       A.ASSESSMENT_NAME,
       G.GRADE_CODE,
       G.GRADEID,
       G.GRADE_NAME,
       L.LEVELID,
       L.LEVEL_NAME,
       F.FORMID,
       F.FORM_NAME,
     F.FORM_CODE,
       C.CONTENTID,
       C.CONTENT_NAME,
       C.CONTENT_SEQ,
     S.SUBTESTID,
       S.SUBTEST_NAME,
       S.SUBTEST_SEQ,
     S.SUBTEST_CODE,
       O.OBJECTIVEID,
       O.OBJECTIVE_NAME,
       O.OBJECTIVE_SEQ,
     O.OBJECTIVE_CODE,
       'MASTERY_INDICATOR_'||ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) MAST_IND,
       'OPIIPI_'||ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) OPI_IPI,
       ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) AS rn
  FROM CONTENT_DIM           C,
       SUBTEST_DIM           S,
       OBJECTIVE_DIM         O,
       SUBTEST_OBJECTIVE_MAP SOM,
       LEVEL_MAP             LM,
       LEVEL_DIM             L,
       FORM_DIM              F,
       GRADE_DIM             G,
       GRADE_LEVEL_MAP       GM,
       ASSESSMENT_DIM        A
 WHERE A.ASSESSMENTID = C.ASSESSMENTID
   AND C.CONTENTID = S.CONTENTID
   AND SOM.SUBTESTID = S.SUBTESTID
   AND SOM.OBJECTIVEID = O.OBJECTIVEID
   AND SOM.LEVEL_MAPID = LM.LEVEL_MAPID
   AND L.LEVELID = LM.LEVELID
   AND F.FORMID = LM.FORMID
   AND G.GRADEID = GM.GRADEID
   AND LM.LEVEL_MAPID = GM.LEVEL_MAPID;