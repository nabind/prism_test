create or replace package pkg_report_acad_stand_summary is

  PROCEDURE PRC_Rprt_Acad_Stand_Summary(IN_CUST_PROD_ID     number,
                                        IN_ORGNODE_SCHOL_ID number,
                                        IN_ORGNODE_DISTR_ID number,
                                        IN_ORGNODE_STATE_ID number,
                                        IN_GRADEID          number,
                                        IN_ISPUBLIC         number,
                                        IN_ASSESSMENT_ID    number);

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_wrapr(IN_CUST_PROD_ID number);

end pkg_report_acad_stand_summary;
/
create or replace package body pkg_report_acad_stand_summary is

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_grd3(IN_CUST_PROD_ID          number,
                                          IN_ASSESSMENT_ID         number,
                                          IN_GRADEID               number,
                                          IN_ISPUBLIC              number,
                                          IN_org_node_id           number,
                                          IN_ORGNODE_SCHOL_ID      number,
                                          IN_ORGNODE_DISTR_ID      number,
                                          IN_ORGNODE_STATE_ID      number,
                                          IN_NoOfStudentELA        varchar2,
                                          IN_NoOfStudentMath       varchar2,
                                          IN_NoOfStudentScience    varchar2,
                                          IN_NoOfStudentSocscience varchar2) AS

  BEGIN

    INSERT
    INTO acad_std_summ_fact
      (AS_SUMM_ID,
       cust_prod_id,
       GradeID,
       ISPUBLIC,
       DATETIMESTAMP,
       contentid,
       objectiveid,
       levelid,
       adminid,

       SubtestID,
       ctb_objective_code,
       ctb_objective_title,
       ItemType,
       PONUMBERSPOSSIBLE,
       IPI_at_Pass,
       State_ObjectiveTitle,
       State_MeanNumberCorrect,
       State_MeanPercentCorrect,
       State_MeanIPI,
       State_IPIDifference,
       State_NumberMastery,
       State_PercentMastery,
       Corp_ObjectiveTitle,
       Corp_MeanNumberCorrect,
       Corp_MeanPercentCorrect,
       Corp_MeanIPI,
       Corp_IPIDifference,
       Corp_NumberMastery,
       Corp_PercentMastery,
       School_ObjectiveTitle,
       School_MeanNumberCorrect,
       School_MeanPercentCorrect,
       School_MeanIPI,
       School_IPIDifference,
       School_NumberMastery,
       School_PercentMastery,
       ORG_NODEID

       )
      select ACAD_STD_SUMM_FACT_SEQ.Nextval,
             IN_CUST_PROD_ID AS cust_prod_id,
             IN_GRADEID AS GradeID,
             IN_ISPUBLIC AS ISPUBLIC,
             sysdate as DATETIMESTAMP,
             (select contentid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS contentid,
             (select objectiveid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS objectiveid,
             (select levelid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS levelid,
             (select adminid
                from cust_product_link cpl
               where cpl.cust_prod_id = IN_CUST_PROD_ID) AS adminid,

             (case
               WHEN ctb_objective_code <= '0.0' THEN
                NULL
               ELSE
                SubtestID
             END) as SubtestID,
             ctb_objective_code,
             ctb_objective_title,
             ItemType,
             PointsPossible,
             IPI_at_Pass,
             State_ObjectiveTitle,
             State_MeanNumberCorrect,
             State_MeanPercentCorrect,
             State_MeanIPI,
             State_IPIDifference,
             State_NumberMastery,
             State_PercentMastery,
             Corp_ObjectiveTitle,
             Corp_MeanNumberCorrect,
             Corp_MeanPercentCorrect,
             Corp_MeanIPI,
             Corp_IPIDifference,
             Corp_NumberMastery,
             Corp_PercentMastery,
             School_ObjectiveTitle,
             School_MeanNumberCorrect,
             School_MeanPercentCorrect,
             School_MeanIPI,
             School_IPIDifference,
             School_NumberMastery,
             School_PercentMastery,
             ORG_NODEID
        from (select *
                from (SELECT PEIDCutScoreIPI.SubtestID,
                             PEIDCutScoreIPI.ctb_objective_code,
                             PEIDCutScoreIPI.ctb_objective_title,
                             PEIDCutScoreIPI.ItemType,
                             (CASE (IS_NUMBER(PEIDCutScoreIPI.PointsPossible))
                               WHEN -99 THEN
                                NULL
                               ELSE
                                PEIDCutScoreIPI.PointsPossible
                             END) AS PointsPossible,
                             PEIDCutScoreIPI.IPI_at_Pass,

                             NULL /*StateSummary.ObjectiveTitle*/     AS State_ObjectiveTitle,
                             StateSummary.MeanNumberCorrect  AS State_MeanNumberCorrect,
                             StateSummary.MeanPercentCorrect AS State_MeanPercentCorrect,
                             StateSummary.MeanIPI            AS State_MeanIPI,
                             StateSummary.IPIDifference      AS State_IPIDifference,
                             StateSummary.NumberMastery      AS State_NumberMastery,
                             StateSummary.PercentMastery     AS State_PercentMastery,

                             NULL /*CorpSummary.ObjectiveTitle*/     AS Corp_ObjectiveTitle,
                             CorpSummary.MeanNumberCorrect  AS Corp_MeanNumberCorrect,
                             CorpSummary.MeanPercentCorrect AS Corp_MeanPercentCorrect,
                             CorpSummary.MeanIPI            AS Corp_MeanIPI,
                             CorpSummary.IPIDifference      AS Corp_IPIDifference,
                             CorpSummary.NumberMastery      AS Corp_NumberMastery,
                             CorpSummary.PercentMastery     AS Corp_PercentMastery,

                             NULL /*SchoolSummary.ObjectiveTitle*/     AS School_ObjectiveTitle,
                             SchoolSummary.MeanNumberCorrect  AS School_MeanNumberCorrect,
                             SchoolSummary.MeanPercentCorrect AS School_MeanPercentCorrect,
                             SchoolSummary.MeanIPI            AS School_MeanIPI,
                             SchoolSummary.IPIDifference      AS School_IPIDifference,
                             SchoolSummary.NumberMastery      AS School_NumberMastery,
                             SchoolSummary.PercentMastery     AS School_PercentMastery,

                             IN_org_node_id AS org_nodeid
                        FROM (
                              --Data from Result_PEID and CutScoreIPI

                              SELECT DISTINCT stomp.SubtestID as SubtestID,
                                               od.objective_code as ctb_objective_code,
                                               '   ' || od.objective_name AS ctb_objective_title,
                                               nvl2(TRIM(ITEM_NAME),
                                                    '(' || ITEM_NAME || ')',
                                                    ITEM_NAME) AS ItemType,
                                               POINT_POSSIBLE AS PointsPossible,
                                               (SELECT IPI_at_Pass
                                                  FROM CutScoreIPI
                                                 WHERE CutScoreIPI.GradeID =
                                                       vw.gradeid
                                                   AND CutScoreIPI.OBJECTIVEID =
                                                       od.OBJECTIVEID
                                                   AND CutScoreIPI.SubtestID =
                                                       stomp.SubtestID
                                                   AND CutScoreIPI.CUST_PROD_ID =
                                                       cpl.CUST_PROD_ID) AS IPI_at_pass
                                FROM itemset_dim                    B,
                                      subtest_objective_map          stomp,
                                      objective_dim                  od,
                                      vw_subtest_grade_objective_map vw,
                                      cust_product_link              cpl,
                                      assessment_dim                 adim
                               WHERE cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 and item_type = 'OBJ'
                                 and B.SUBT_OBJ_MAPID = stomp.subt_obj_mapid
                                 and od.objectiveid = stomp.objectiveid
                                 and vw.objectiveid = stomp.objectiveid
                                 and vw.subtestid = stomp.SubtestID
                                 and vw.gradeid = IN_GRADEID
                                 and adim.productid = cpl.productid
                                 and vw.assessmentid = adim.assessmentid

                              --Adding Subject Name 'English/language arts' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'ELA'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,

                                     '0.0' AS ctb_objective_code,
                                     'English/Language Arts' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --Adding Subject Name 'Mathematics' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'MATH'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Mathematics' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              ) PEIDCutScoreIPI

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_STATE_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) StateSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                      StateSummary.SubtestID
                                                                                  AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                      StateSummary.objective_code

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_DISTR_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) CorpSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                     CorpSummary.SubtestID
                                                                                 AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                     CorpSummary.objective_code
                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_SCHOL_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) SchoolSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                       SchoolSummary.SubtestID
                                                                                   AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                       SchoolSummary.objective_code

                      -- Adding values for Number of students in the subject English/Language arts
                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'ELA'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2.0' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentELA, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid

                        FROM DUAL

                      -- Adding values for Number of students in the subject Mathematics

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'MATH'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,

                             '-2.' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentMath, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT NULL AS SubtestID,
                             '-1' AS ctb_objective_code,
                             '****Total Number of students: ' ||
                             (select case
                                       when count(1) = 0 THEN
                                        ' '
                                       ELSE
                                        to_char(max(CRT_Student_Cnt))
                                     END
                                FROM SUMT_FACT
                               WHERE GRADEID = IN_GRADEID
                                 and org_nodeid = IN_org_node_id
                                 and ispublic = IN_ISPUBLIC
                                 and CUST_PROD_ID = IN_CUST_PROD_ID) AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        from dual

                      )
               order by SubtestID, ctb_objective_code) t
       where ctb_objective_title <> '   ';
    commit;

  END PRC_Rprt_Acad_Stand_Summ_grd3;

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_grd4(IN_CUST_PROD_ID          number,
                                          IN_ASSESSMENT_ID         number,
                                          IN_GRADEID               number,
                                          IN_ISPUBLIC              number,
                                          IN_org_node_id           number,
                                          IN_ORGNODE_SCHOL_ID      number,
                                          IN_ORGNODE_DISTR_ID      number,
                                          IN_ORGNODE_STATE_ID      number,
                                          IN_NoOfStudentELA        varchar2,
                                          IN_NoOfStudentMath       varchar2,
                                          IN_NoOfStudentScience    varchar2,
                                          IN_NoOfStudentSocscience varchar2) AS

  BEGIN

    INSERT
    INTO acad_std_summ_fact
      (AS_SUMM_ID,
       cust_prod_id,
       GradeID,
       ISPUBLIC,
       DATETIMESTAMP,
       contentid,
       objectiveid,
       levelid,
       adminid,
       SubtestID,
       ctb_objective_code,
       ctb_objective_title,
       ItemType,
       PONUMBERSPOSSIBLE,
       IPI_at_Pass,
       State_ObjectiveTitle,
       State_MeanNumberCorrect,
       State_MeanPercentCorrect,
       State_MeanIPI,
       State_IPIDifference,
       State_NumberMastery,
       State_PercentMastery,
       Corp_ObjectiveTitle,
       Corp_MeanNumberCorrect,
       Corp_MeanPercentCorrect,
       Corp_MeanIPI,
       Corp_IPIDifference,
       Corp_NumberMastery,
       Corp_PercentMastery,
       School_ObjectiveTitle,
       School_MeanNumberCorrect,
       School_MeanPercentCorrect,
       School_MeanIPI,
       School_IPIDifference,
       School_NumberMastery,
       School_PercentMastery,
       ORG_NODEID

       )
      select ACAD_STD_SUMM_FACT_SEQ.Nextval,
             IN_CUST_PROD_ID AS cust_prod_id,
             IN_GRADEID AS GradeID,
             IN_ISPUBLIC AS ISPUBLIC,
             sysdate as DATETIMESTAMP,
             (select contentid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS contentid,
             (select objectiveid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS objectiveid,
             (select levelid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS levelid,
             (select adminid
                from cust_product_link cpl
               where cpl.cust_prod_id = IN_CUST_PROD_ID) AS adminid,

             (case
               WHEN ctb_objective_code <= '0.0' THEN
                NULL
               ELSE
                SubtestID
             END) as SubtestID,
             ctb_objective_code,
             ctb_objective_title,
             ItemType,
             PointsPossible,
             IPI_at_Pass,
             State_ObjectiveTitle,
             State_MeanNumberCorrect,
             State_MeanPercentCorrect,
             State_MeanIPI,
             State_IPIDifference,
             State_NumberMastery,
             State_PercentMastery,
             Corp_ObjectiveTitle,
             Corp_MeanNumberCorrect,
             Corp_MeanPercentCorrect,
             Corp_MeanIPI,
             Corp_IPIDifference,
             Corp_NumberMastery,
             Corp_PercentMastery,
             School_ObjectiveTitle,
             School_MeanNumberCorrect,
             School_MeanPercentCorrect,
             School_MeanIPI,
             School_IPIDifference,
             School_NumberMastery,
             School_PercentMastery,
             ORG_NODEID
        from (select *
                from (SELECT

                       PEIDCutScoreIPI.SubtestID,
                       PEIDCutScoreIPI.ctb_objective_code,
                       PEIDCutScoreIPI.ctb_objective_title,
                       PEIDCutScoreIPI.ItemType,
                       (CASE (IS_NUMBER(PEIDCutScoreIPI.PointsPossible))
                         WHEN -99 THEN
                          NULL
                         ELSE
                          PEIDCutScoreIPI.PointsPossible
                       END) AS PointsPossible,
                       PEIDCutScoreIPI.IPI_at_Pass,

                       NULL /*StateSummary.ObjectiveTitle*/     AS State_ObjectiveTitle,
                       StateSummary.MeanNumberCorrect  AS State_MeanNumberCorrect,
                       StateSummary.MeanPercentCorrect AS State_MeanPercentCorrect,
                       StateSummary.MeanIPI            AS State_MeanIPI,
                       StateSummary.IPIDifference      AS State_IPIDifference,
                       StateSummary.NumberMastery      AS State_NumberMastery,
                       StateSummary.PercentMastery     AS State_PercentMastery,

                       NULL /*CorpSummary.ObjectiveTitle*/     AS Corp_ObjectiveTitle,
                       CorpSummary.MeanNumberCorrect  AS Corp_MeanNumberCorrect,
                       CorpSummary.MeanPercentCorrect AS Corp_MeanPercentCorrect,
                       CorpSummary.MeanIPI            AS Corp_MeanIPI,
                       CorpSummary.IPIDifference      AS Corp_IPIDifference,
                       CorpSummary.NumberMastery      AS Corp_NumberMastery,
                       CorpSummary.PercentMastery     AS Corp_PercentMastery,

                       NULL /*SchoolSummary.ObjectiveTitle*/     AS School_ObjectiveTitle,
                       SchoolSummary.MeanNumberCorrect  AS School_MeanNumberCorrect,
                       SchoolSummary.MeanPercentCorrect AS School_MeanPercentCorrect,
                       SchoolSummary.MeanIPI            AS School_MeanIPI,
                       SchoolSummary.IPIDifference      AS School_IPIDifference,
                       SchoolSummary.NumberMastery      AS School_NumberMastery,
                       SchoolSummary.PercentMastery     AS School_PercentMastery,

                       IN_org_node_id AS org_nodeid
                        FROM (
                              --Data from Result_PEID and CutScoreIPI
                              SELECT DISTINCT stomp.SubtestID as SubtestID,
                                               od.objective_code as ctb_objective_code,
                                               '   ' || od.objective_name AS ctb_objective_title,
                                               nvl2(TRIM(ITEM_NAME),
                                                    '(' || ITEM_NAME || ')',
                                                    ITEM_NAME) AS ItemType,
                                               POINT_POSSIBLE AS PointsPossible,
                                               (SELECT IPI_at_Pass
                                                  FROM CutScoreIPI
                                                 WHERE CutScoreIPI.GradeID =
                                                       vw.gradeid
                                                   AND CutScoreIPI.OBJECTIVEID =
                                                       od.OBJECTIVEID
                                                   AND CutScoreIPI.SubtestID =
                                                       stomp.SubtestID
                                                   AND CutScoreIPI.CUST_PROD_ID =
                                                       cpl.CUST_PROD_ID) AS IPI_at_pass
                                FROM itemset_dim                    B,
                                      subtest_objective_map          stomp,
                                      objective_dim                  od,
                                      vw_subtest_grade_objective_map vw,
                                      cust_product_link              cpl,
                                      assessment_dim                 adim
                               WHERE cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 and item_type = 'OBJ'
                                 and B.SUBT_OBJ_MAPID = stomp.subt_obj_mapid
                                 and od.objectiveid = stomp.objectiveid
                                 and vw.objectiveid = stomp.objectiveid
                                 and vw.subtestid = stomp.SubtestID
                                 and vw.gradeid = IN_GRADEID
                                 and adim.productid = cpl.productid
                                 and vw.assessmentid = adim.assessmentid

                              --Adding Subject Name 'English/language arts' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'ELA'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,

                                     '0.0' AS ctb_objective_code,
                                     'English/Language Arts' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --Adding Subject Name 'Mathematics' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'MATH'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Mathematics' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --This is only for grades 4
                              --Adding Subject Name 'Science' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'SCI'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Science' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              ) PEIDCutScoreIPI

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_STATE_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) StateSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                      StateSummary.SubtestID
                                                                                  AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                      StateSummary.objective_code

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_DISTR_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) CorpSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                     CorpSummary.SubtestID
                                                                                 AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                     CorpSummary.objective_code
                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_SCHOL_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) SchoolSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                       SchoolSummary.SubtestID
                                                                                   AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                       SchoolSummary.objective_code

                      -- Adding values for Number of students in the subject English/Language arts
                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'ELA'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,

                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentELA, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid

                        FROM DUAL

                      -- Adding values for Number of students in the subject Mathematics

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'MATH'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentMath, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'SCI'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentScience, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT NULL AS SubtestID,
                             '-1' AS ctb_objective_code,
                             '****Total Number of students: ' ||
                             (select case
                                       when count(1) = 0 THEN
                                        ' '
                                       ELSE
                                        to_char(max(CRT_Student_Cnt))
                                     END
                                FROM SUMT_FACT
                               WHERE GRADEID = IN_GRADEID
                                 and org_nodeid = IN_org_node_id
                                 and ispublic = IN_ISPUBLIC
                                 and CUST_PROD_ID = IN_CUST_PROD_ID) AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        from dual)
               order by SubtestID, ctb_objective_code) t
       where ctb_objective_title <> '   ';
    commit;

  END PRC_Rprt_Acad_Stand_Summ_grd4;

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_grd5(IN_CUST_PROD_ID          number,
                                          IN_ASSESSMENT_ID         number,
                                          IN_GRADEID               number,
                                          IN_ISPUBLIC              number,
                                          IN_org_node_id           number,
                                          IN_ORGNODE_SCHOL_ID      number,
                                          IN_ORGNODE_DISTR_ID      number,
                                          IN_ORGNODE_STATE_ID      number,
                                          IN_NoOfStudentELA        varchar2,
                                          IN_NoOfStudentMath       varchar2,
                                          IN_NoOfStudentScience    varchar2,
                                          IN_NoOfStudentSocscience varchar2) AS

  BEGIN

    INSERT
    INTO acad_std_summ_fact
      (AS_SUMM_ID,
       cust_prod_id,
       GradeID,
       ISPUBLIC,
       DATETIMESTAMP,
       contentid,
       objectiveid,
       levelid,
       adminid,

       SubtestID,
       ctb_objective_code,
       ctb_objective_title,
       ItemType,
       PONUMBERSPOSSIBLE,
       IPI_at_Pass,
       State_ObjectiveTitle,
       State_MeanNumberCorrect,
       State_MeanPercentCorrect,
       State_MeanIPI,
       State_IPIDifference,
       State_NumberMastery,
       State_PercentMastery,
       Corp_ObjectiveTitle,
       Corp_MeanNumberCorrect,
       Corp_MeanPercentCorrect,
       Corp_MeanIPI,
       Corp_IPIDifference,
       Corp_NumberMastery,
       Corp_PercentMastery,
       School_ObjectiveTitle,
       School_MeanNumberCorrect,
       School_MeanPercentCorrect,
       School_MeanIPI,
       School_IPIDifference,
       School_NumberMastery,
       School_PercentMastery,
       ORG_NODEID

       )
      select ACAD_STD_SUMM_FACT_SEQ.Nextval,
             IN_CUST_PROD_ID AS cust_prod_id,
             IN_GRADEID AS GradeID,
             IN_ISPUBLIC AS ISPUBLIC,
             sysdate as DATETIMESTAMP,
             (select contentid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS contentid,
             (select objectiveid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS objectiveid,
             (select levelid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS levelid,
             (select adminid
                from cust_product_link cpl
               where cpl.cust_prod_id = IN_CUST_PROD_ID) AS adminid,

             (case
               WHEN ctb_objective_code <= '0.0' THEN
                NULL
               ELSE
                SubtestID
             END) as SubtestID,
             ctb_objective_code,
             ctb_objective_title,
             ItemType,
             PointsPossible,
             IPI_at_Pass,
             State_ObjectiveTitle,
             State_MeanNumberCorrect,
             State_MeanPercentCorrect,
             State_MeanIPI,
             State_IPIDifference,
             State_NumberMastery,
             State_PercentMastery,
             Corp_ObjectiveTitle,
             Corp_MeanNumberCorrect,
             Corp_MeanPercentCorrect,
             Corp_MeanIPI,
             Corp_IPIDifference,
             Corp_NumberMastery,
             Corp_PercentMastery,
             School_ObjectiveTitle,
             School_MeanNumberCorrect,
             School_MeanPercentCorrect,
             School_MeanIPI,
             School_IPIDifference,
             School_NumberMastery,
             School_PercentMastery,
             ORG_NODEID
        from (select *
                from (SELECT

                       PEIDCutScoreIPI.SubtestID,
                       PEIDCutScoreIPI.ctb_objective_code,
                       PEIDCutScoreIPI.ctb_objective_title,
                       PEIDCutScoreIPI.ItemType,
                       (CASE (IS_NUMBER(PEIDCutScoreIPI.PointsPossible))
                         WHEN -99 THEN
                          NULL
                         ELSE
                          PEIDCutScoreIPI.PointsPossible
                       END) AS PointsPossible,
                       PEIDCutScoreIPI.IPI_at_Pass,

                       NULL /*StateSummary.ObjectiveTitle*/     AS State_ObjectiveTitle,
                       StateSummary.MeanNumberCorrect  AS State_MeanNumberCorrect,
                       StateSummary.MeanPercentCorrect AS State_MeanPercentCorrect,
                       StateSummary.MeanIPI            AS State_MeanIPI,
                       StateSummary.IPIDifference      AS State_IPIDifference,
                       StateSummary.NumberMastery      AS State_NumberMastery,
                       StateSummary.PercentMastery     AS State_PercentMastery,

                       NULL /*CorpSummary.ObjectiveTitle*/     AS Corp_ObjectiveTitle,
                       CorpSummary.MeanNumberCorrect  AS Corp_MeanNumberCorrect,
                       CorpSummary.MeanPercentCorrect AS Corp_MeanPercentCorrect,
                       CorpSummary.MeanIPI            AS Corp_MeanIPI,
                       CorpSummary.IPIDifference      AS Corp_IPIDifference,
                       CorpSummary.NumberMastery      AS Corp_NumberMastery,
                       CorpSummary.PercentMastery     AS Corp_PercentMastery,

                       NULL /*SchoolSummary.ObjectiveTitle*/     AS School_ObjectiveTitle,
                       SchoolSummary.MeanNumberCorrect  AS School_MeanNumberCorrect,
                       SchoolSummary.MeanPercentCorrect AS School_MeanPercentCorrect,
                       SchoolSummary.MeanIPI            AS School_MeanIPI,
                       SchoolSummary.IPIDifference      AS School_IPIDifference,
                       SchoolSummary.NumberMastery      AS School_NumberMastery,
                       SchoolSummary.PercentMastery     AS School_PercentMastery,

                       IN_org_node_id AS org_nodeid
                        FROM (
                              --Data from Result_PEID and CutScoreIPI
                              SELECT DISTINCT stomp.SubtestID as SubtestID,
                                               od.objective_code as ctb_objective_code,
                                               '   ' || od.objective_name AS ctb_objective_title,
                                               nvl2(TRIM(ITEM_NAME),
                                                    '(' || ITEM_NAME || ')',
                                                    ITEM_NAME) AS ItemType,
                                               POINT_POSSIBLE AS PointsPossible,
                                               (SELECT IPI_at_Pass
                                                  FROM CutScoreIPI
                                                 WHERE CutScoreIPI.GradeID =
                                                       vw.gradeid
                                                   AND CutScoreIPI.OBJECTIVEID =
                                                       od.OBJECTIVEID
                                                   AND CutScoreIPI.SubtestID =
                                                       stomp.SubtestID
                                                   AND CutScoreIPI.CUST_PROD_ID =
                                                       cpl.CUST_PROD_ID) AS IPI_at_pass
                                FROM itemset_dim                    B,
                                      subtest_objective_map          stomp,
                                      objective_dim                  od,
                                      vw_subtest_grade_objective_map vw,
                                      cust_product_link              cpl,
                                      assessment_dim                 adim
                               WHERE cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 and item_type = 'OBJ'
                                 and B.SUBT_OBJ_MAPID = stomp.subt_obj_mapid
                                 and od.objectiveid = stomp.objectiveid
                                 and vw.objectiveid = stomp.objectiveid
                                 and vw.subtestid = stomp.SubtestID
                                 and vw.gradeid = IN_GRADEID
                                 and adim.productid = cpl.productid
                                 and vw.assessmentid = adim.assessmentid

                              --Adding Subject Name 'English/language arts' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'ELA'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,

                                     '0.0' AS ctb_objective_code,
                                     'English/Language Arts' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --Adding Subject Name 'Mathematics' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'MATH'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Mathematics' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              --This is only for grades 5 and 7
                              --Adding Subject Name 'Socail' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'SS'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Social Studies' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              ) PEIDCutScoreIPI

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_STATE_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) StateSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                      StateSummary.SubtestID
                                                                                  AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                      StateSummary.objective_code

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_DISTR_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) CorpSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                     CorpSummary.SubtestID
                                                                                 AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                     CorpSummary.objective_code
                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_SCHOL_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) SchoolSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                       SchoolSummary.SubtestID
                                                                                   AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                       SchoolSummary.objective_code

                      -- Adding values for Number of students in the subject English/Language arts
                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'ELA'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentELA, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid

                        FROM DUAL

                      -- Adding values for Number of students in the subject Mathematics

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'MATH'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentMath, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'SS'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentSocscience, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT NULL AS SubtestID,
                             '-1' AS ctb_objective_code,
                             '****Total Number of students: ' ||
                             (select case
                                       when count(1) = 0 THEN
                                        ' '
                                       ELSE
                                        to_char(max(CRT_Student_Cnt))
                                     END
                                FROM SUMT_FACT
                               WHERE GRADEID = IN_GRADEID
                                 and org_nodeid = IN_org_node_id
                                 and ispublic = IN_ISPUBLIC
                                 and CUST_PROD_ID = IN_CUST_PROD_ID) AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        from dual)
               order by SubtestID, ctb_objective_code) t
       where ctb_objective_title <> '   ';
    commit;

  END PRC_Rprt_Acad_Stand_Summ_grd5;

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_grd6(IN_CUST_PROD_ID          number,
                                          IN_ASSESSMENT_ID         number,
                                          IN_GRADEID               number,
                                          IN_ISPUBLIC              number,
                                          IN_org_node_id           number,
                                          IN_ORGNODE_SCHOL_ID      number,
                                          IN_ORGNODE_DISTR_ID      number,
                                          IN_ORGNODE_STATE_ID      number,
                                          IN_NoOfStudentELA        varchar2,
                                          IN_NoOfStudentMath       varchar2,
                                          IN_NoOfStudentScience    varchar2,
                                          IN_NoOfStudentSocscience varchar2) AS

  BEGIN

    INSERT
    INTO acad_std_summ_fact
      (AS_SUMM_ID,
       cust_prod_id,
       GradeID,
       ISPUBLIC,
       DATETIMESTAMP,
       contentid,
       objectiveid,
       levelid,
       adminid,

       SubtestID,
       ctb_objective_code,
       ctb_objective_title,
       ItemType,
       PONUMBERSPOSSIBLE,
       IPI_at_Pass,
       State_ObjectiveTitle,
       State_MeanNumberCorrect,
       State_MeanPercentCorrect,
       State_MeanIPI,
       State_IPIDifference,
       State_NumberMastery,
       State_PercentMastery,
       Corp_ObjectiveTitle,
       Corp_MeanNumberCorrect,
       Corp_MeanPercentCorrect,
       Corp_MeanIPI,
       Corp_IPIDifference,
       Corp_NumberMastery,
       Corp_PercentMastery,
       School_ObjectiveTitle,
       School_MeanNumberCorrect,
       School_MeanPercentCorrect,
       School_MeanIPI,
       School_IPIDifference,
       School_NumberMastery,
       School_PercentMastery,
       ORG_NODEID

       )
      select ACAD_STD_SUMM_FACT_SEQ.Nextval,
             IN_CUST_PROD_ID AS cust_prod_id,
             IN_GRADEID AS GradeID,
             IN_ISPUBLIC AS ISPUBLIC,
             sysdate as DATETIMESTAMP,
             (select contentid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS contentid,
             (select objectiveid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS objectiveid,
             (select levelid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS levelid,
             (select adminid
                from cust_product_link cpl
               where cpl.cust_prod_id = IN_CUST_PROD_ID) AS adminid,

             (case
               WHEN ctb_objective_code <= '0.0' THEN
                NULL
               ELSE
                SubtestID
             END) as SubtestID,
             ctb_objective_code,
             ctb_objective_title,
             ItemType,
             PointsPossible,
             IPI_at_Pass,
             State_ObjectiveTitle,
             State_MeanNumberCorrect,
             State_MeanPercentCorrect,
             State_MeanIPI,
             State_IPIDifference,
             State_NumberMastery,
             State_PercentMastery,
             Corp_ObjectiveTitle,
             Corp_MeanNumberCorrect,
             Corp_MeanPercentCorrect,
             Corp_MeanIPI,
             Corp_IPIDifference,
             Corp_NumberMastery,
             Corp_PercentMastery,
             School_ObjectiveTitle,
             School_MeanNumberCorrect,
             School_MeanPercentCorrect,
             School_MeanIPI,
             School_IPIDifference,
             School_NumberMastery,
             School_PercentMastery,
             ORG_NODEID
        from (select *
                from (SELECT

                       PEIDCutScoreIPI.SubtestID,

                       PEIDCutScoreIPI.ctb_objective_code,
                       PEIDCutScoreIPI.ctb_objective_title,
                       PEIDCutScoreIPI.ItemType,
                       (CASE (IS_NUMBER(PEIDCutScoreIPI.PointsPossible))
                         WHEN -99 THEN
                          NULL
                         ELSE
                          PEIDCutScoreIPI.PointsPossible
                       END) AS PointsPossible,
                       PEIDCutScoreIPI.IPI_at_Pass,

                       NULL /*StateSummary.ObjectiveTitle*/     AS State_ObjectiveTitle,
                       StateSummary.MeanNumberCorrect  AS State_MeanNumberCorrect,
                       StateSummary.MeanPercentCorrect AS State_MeanPercentCorrect,
                       StateSummary.MeanIPI            AS State_MeanIPI,
                       StateSummary.IPIDifference      AS State_IPIDifference,
                       StateSummary.NumberMastery      AS State_NumberMastery,
                       StateSummary.PercentMastery     AS State_PercentMastery,

                       NULL /*CorpSummary.ObjectiveTitle*/     AS Corp_ObjectiveTitle,
                       CorpSummary.MeanNumberCorrect  AS Corp_MeanNumberCorrect,
                       CorpSummary.MeanPercentCorrect AS Corp_MeanPercentCorrect,
                       CorpSummary.MeanIPI            AS Corp_MeanIPI,
                       CorpSummary.IPIDifference      AS Corp_IPIDifference,
                       CorpSummary.NumberMastery      AS Corp_NumberMastery,
                       CorpSummary.PercentMastery     AS Corp_PercentMastery,

                       NULL /*SchoolSummary.ObjectiveTitle*/     AS School_ObjectiveTitle,
                       SchoolSummary.MeanNumberCorrect  AS School_MeanNumberCorrect,
                       SchoolSummary.MeanPercentCorrect AS School_MeanPercentCorrect,
                       SchoolSummary.MeanIPI            AS School_MeanIPI,
                       SchoolSummary.IPIDifference      AS School_IPIDifference,
                       SchoolSummary.NumberMastery      AS School_NumberMastery,
                       SchoolSummary.PercentMastery     AS School_PercentMastery,

                       IN_org_node_id AS org_nodeid
                        FROM (
                              --Data from Result_PEID and CutScoreIPI
                              SELECT DISTINCT stomp.SubtestID as SubtestID,
                                               od.objective_code as ctb_objective_code,
                                               '   ' || od.objective_name AS ctb_objective_title,
                                               nvl2(TRIM(ITEM_NAME),
                                                    '(' || ITEM_NAME || ')',
                                                    ITEM_NAME) AS ItemType,
                                               POINT_POSSIBLE AS PointsPossible,
                                               (SELECT IPI_at_Pass
                                                  FROM CutScoreIPI
                                                 WHERE CutScoreIPI.GradeID =
                                                       vw.gradeid
                                                   AND CutScoreIPI.OBJECTIVEID =
                                                       od.OBJECTIVEID
                                                   AND CutScoreIPI.SubtestID =
                                                       stomp.SubtestID
                                                   AND CutScoreIPI.CUST_PROD_ID =
                                                       cpl.CUST_PROD_ID) AS IPI_at_pass
                                FROM itemset_dim                    B,
                                      subtest_objective_map          stomp,
                                      objective_dim                  od,
                                      vw_subtest_grade_objective_map vw,
                                      cust_product_link              cpl,
                                      assessment_dim                 adim
                               WHERE cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 and item_type = 'OBJ'
                                 and B.SUBT_OBJ_MAPID = stomp.subt_obj_mapid
                                 and od.objectiveid = stomp.objectiveid
                                 and vw.objectiveid = stomp.objectiveid
                                 and vw.subtestid = stomp.SubtestID
                                 and vw.gradeid = IN_GRADEID
                                 and adim.productid = cpl.productid
                                 and vw.assessmentid = adim.assessmentid

                              --Adding Subject Name 'English/language arts' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'ELA'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,

                                     '0.0' AS ctb_objective_code,
                                     'English/Language Arts' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --Adding Subject Name 'Mathematics' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'MATH'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Mathematics' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --This is only for grades 4 and 6
                              --Adding Subject Name 'Science' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'SCI'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Science' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              ) PEIDCutScoreIPI

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_STATE_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) StateSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                      StateSummary.SubtestID
                                                                                  AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                      StateSummary.objective_code

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_DISTR_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) CorpSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                     CorpSummary.SubtestID
                                                                                 AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                     CorpSummary.objective_code
                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_SCHOL_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) SchoolSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                       SchoolSummary.SubtestID
                                                                                   AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                       SchoolSummary.objective_code

                      -- Adding values for Number of students in the subject English/Language arts
                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'ELA'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentELA, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid

                        FROM DUAL

                      -- Adding values for Number of students in the subject Mathematics

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'MATH'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,

                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentMath, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'SCI'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentScience, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT NULL AS SubtestID,
                             '-1' AS ctb_objective_code,
                             '****Total Number of students: ' ||
                             (select case
                                       when count(1) = 0 THEN
                                        ' '
                                       ELSE
                                        to_char(max(CRT_Student_Cnt))
                                     END
                                FROM SUMT_FACT
                               WHERE GRADEID = IN_GRADEID
                                 and org_nodeid = IN_org_node_id
                                 and ispublic = IN_ISPUBLIC
                                 and CUST_PROD_ID = IN_CUST_PROD_ID) AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        from dual)
               order by SubtestID, ctb_objective_code) t
       where ctb_objective_title <> '   ';
    commit;

  END PRC_Rprt_Acad_Stand_Summ_grd6;

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_grd7(IN_CUST_PROD_ID          number,
                                          IN_ASSESSMENT_ID         number,
                                          IN_GRADEID               number,
                                          IN_ISPUBLIC              number,
                                          IN_org_node_id           number,
                                          IN_ORGNODE_SCHOL_ID      number,
                                          IN_ORGNODE_DISTR_ID      number,
                                          IN_ORGNODE_STATE_ID      number,
                                          IN_NoOfStudentELA        varchar2,
                                          IN_NoOfStudentMath       varchar2,
                                          IN_NoOfStudentScience    varchar2,
                                          IN_NoOfStudentSocscience varchar2) AS

  BEGIN

    INSERT
    INTO acad_std_summ_fact
      (AS_SUMM_ID,
       cust_prod_id,
       GradeID,
       ISPUBLIC,
       DATETIMESTAMP,
       contentid,
       objectiveid,
       levelid,
       adminid,

       SubtestID,
       ctb_objective_code,
       ctb_objective_title,
       ItemType,
       PONUMBERSPOSSIBLE,
       IPI_at_Pass,
       State_ObjectiveTitle,
       State_MeanNumberCorrect,
       State_MeanPercentCorrect,
       State_MeanIPI,
       State_IPIDifference,
       State_NumberMastery,
       State_PercentMastery,
       Corp_ObjectiveTitle,
       Corp_MeanNumberCorrect,
       Corp_MeanPercentCorrect,
       Corp_MeanIPI,
       Corp_IPIDifference,
       Corp_NumberMastery,
       Corp_PercentMastery,
       School_ObjectiveTitle,
       School_MeanNumberCorrect,
       School_MeanPercentCorrect,
       School_MeanIPI,
       School_IPIDifference,
       School_NumberMastery,
       School_PercentMastery,
       ORG_NODEID

       )
      select ACAD_STD_SUMM_FACT_SEQ.Nextval,
             IN_CUST_PROD_ID AS cust_prod_id,
             IN_GRADEID AS GradeID,
             IN_ISPUBLIC AS ISPUBLIC,
             sysdate as DATETIMESTAMP,
             (select contentid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS contentid,
             (select objectiveid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS objectiveid,
             (select levelid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS levelid,
             (select adminid
                from cust_product_link cpl
               where cpl.cust_prod_id = IN_CUST_PROD_ID) AS adminid,

             (case
               WHEN ctb_objective_code <= '0.0' THEN
                NULL
               ELSE
                SubtestID
             END) as SubtestID,
             ctb_objective_code,
             ctb_objective_title,
             ItemType,
             PointsPossible,
             IPI_at_Pass,
             State_ObjectiveTitle,
             State_MeanNumberCorrect,
             State_MeanPercentCorrect,
             State_MeanIPI,
             State_IPIDifference,
             State_NumberMastery,
             State_PercentMastery,
             Corp_ObjectiveTitle,
             Corp_MeanNumberCorrect,
             Corp_MeanPercentCorrect,
             Corp_MeanIPI,
             Corp_IPIDifference,
             Corp_NumberMastery,
             Corp_PercentMastery,
             School_ObjectiveTitle,
             School_MeanNumberCorrect,
             School_MeanPercentCorrect,
             School_MeanIPI,
             School_IPIDifference,
             School_NumberMastery,
             School_PercentMastery,
             ORG_NODEID
        from (SELECT *
                FROM (SELECT

                       PEIDCutScoreIPI.SubtestID,
                       PEIDCutScoreIPI.ctb_objective_code,
                       PEIDCutScoreIPI.ctb_objective_title,
                       PEIDCutScoreIPI.ItemType,
                       (CASE (IS_NUMBER(PEIDCutScoreIPI.PointsPossible))
                         WHEN -99 THEN
                          NULL
                         ELSE
                          PEIDCutScoreIPI.PointsPossible
                       END) AS PointsPossible,
                       PEIDCutScoreIPI.IPI_at_Pass,

                       NULL /*StateSummary.ObjectiveTitle*/     AS State_ObjectiveTitle,
                       StateSummary.MeanNumberCorrect  AS State_MeanNumberCorrect,
                       StateSummary.MeanPercentCorrect AS State_MeanPercentCorrect,
                       StateSummary.MeanIPI            AS State_MeanIPI,
                       StateSummary.IPIDifference      AS State_IPIDifference,
                       StateSummary.NumberMastery      AS State_NumberMastery,
                       StateSummary.PercentMastery     AS State_PercentMastery,

                       NULL /*CorpSummary.ObjectiveTitle*/     AS Corp_ObjectiveTitle,
                       CorpSummary.MeanNumberCorrect  AS Corp_MeanNumberCorrect,
                       CorpSummary.MeanPercentCorrect AS Corp_MeanPercentCorrect,
                       CorpSummary.MeanIPI            AS Corp_MeanIPI,
                       CorpSummary.IPIDifference      AS Corp_IPIDifference,
                       CorpSummary.NumberMastery      AS Corp_NumberMastery,
                       CorpSummary.PercentMastery     AS Corp_PercentMastery,

                       NULL /*SchoolSummary.ObjectiveTitle*/     AS School_ObjectiveTitle,
                       SchoolSummary.MeanNumberCorrect  AS School_MeanNumberCorrect,
                       SchoolSummary.MeanPercentCorrect AS School_MeanPercentCorrect,
                       SchoolSummary.MeanIPI            AS School_MeanIPI,
                       SchoolSummary.IPIDifference      AS School_IPIDifference,
                       SchoolSummary.NumberMastery      AS School_NumberMastery,
                       SchoolSummary.PercentMastery     AS School_PercentMastery,

                       IN_org_node_id AS org_nodeid
                        FROM (
                              --Data from Result_PEID and CutScoreIPI
                              SELECT DISTINCT stomp.SubtestID as SubtestID,
                                               od.objective_code as ctb_objective_code,
                                               '   ' || od.objective_name AS ctb_objective_title,
                                               nvl2(TRIM(ITEM_NAME),
                                                    '(' || ITEM_NAME || ')',
                                                    ITEM_NAME) AS ItemType,
                                               POINT_POSSIBLE AS PointsPossible,
                                               (SELECT IPI_at_Pass
                                                  FROM CutScoreIPI
                                                 WHERE CutScoreIPI.GradeID =
                                                       vw.gradeid
                                                   AND CutScoreIPI.OBJECTIVEID =
                                                       od.OBJECTIVEID
                                                   AND CutScoreIPI.SubtestID =
                                                       stomp.SubtestID
                                                   AND CutScoreIPI.CUST_PROD_ID =
                                                       cpl.CUST_PROD_ID) AS IPI_at_pass
                                FROM itemset_dim                    B,
                                      subtest_objective_map          stomp,
                                      objective_dim                  od,
                                      vw_subtest_grade_objective_map vw,
                                      cust_product_link              cpl,
                                      assessment_dim                 adim
                               WHERE cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 and item_type = 'OBJ'
                                 and B.SUBT_OBJ_MAPID = stomp.subt_obj_mapid
                                 and od.objectiveid = stomp.objectiveid
                                 and vw.objectiveid = stomp.objectiveid
                                 and vw.subtestid = stomp.SubtestID
                                 and vw.gradeid = IN_GRADEID
                                 and adim.productid = cpl.productid
                                 and vw.assessmentid = adim.assessmentid

                              --Adding Subject Name 'English/language arts' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'ELA'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,

                                     '0.0' AS ctb_objective_code,
                                     'English/Language Arts' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --Adding Subject Name 'Mathematics' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'MATH'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Mathematics' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              --This is only for grades 5 and 7
                              --Adding Subject Name 'Socail' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'SS'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Social Studies' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              ) PEIDCutScoreIPI

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_STATE_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) StateSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                      StateSummary.SubtestID
                                                                                  AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                      StateSummary.objective_code

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' || (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_DISTR_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) CorpSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                     CorpSummary.SubtestID
                                                                                 AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                     CorpSummary.objective_code
                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_SCHOL_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) SchoolSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                       SchoolSummary.SubtestID
                                                                                   AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                       SchoolSummary.objective_code

                      -- Adding values for Number of students in the subject English/Language arts
                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'ELA'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentELA, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid

                        FROM DUAL

                      -- Adding values for Number of students in the subject Mathematics

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'MATH'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentMath, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'SS'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentSocscience, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT NULL AS SubtestID,
                             '-1' AS ctb_objective_code,
                             '****Total Number of students: ' ||
                             (select case
                                       when count(1) = 0 THEN
                                        ' '
                                       ELSE
                                        to_char(max(CRT_Student_Cnt))
                                     END
                                FROM SUMT_FACT
                               WHERE GRADEID = IN_GRADEID
                                 and org_nodeid = IN_org_node_id
                                 and ispublic = IN_ISPUBLIC
                                 and CUST_PROD_ID = IN_CUST_PROD_ID) AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        from dual)
               order by SubtestID, ctb_objective_code) t
       where ctb_objective_title <> '   ';
    commit;

  END PRC_Rprt_Acad_Stand_Summ_grd7;

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_grd8(IN_CUST_PROD_ID          number,
                                          IN_ASSESSMENT_ID         number,
                                          IN_GRADEID               number,
                                          IN_ISPUBLIC              number,
                                          IN_org_node_id           number,
                                          IN_ORGNODE_SCHOL_ID      number,
                                          IN_ORGNODE_DISTR_ID      number,
                                          IN_ORGNODE_STATE_ID      number,
                                          IN_NoOfStudentELA        varchar2,
                                          IN_NoOfStudentMath       varchar2,
                                          IN_NoOfStudentScience    varchar2,
                                          IN_NoOfStudentSocscience varchar2) AS

  BEGIN

    INSERT
    INTO acad_std_summ_fact
      (AS_SUMM_ID,
       cust_prod_id,
       GradeID,
       ISPUBLIC,
       DATETIMESTAMP,
       contentid,
       objectiveid,
       levelid,
       adminid,

       SubtestID,
       ctb_objective_code,
       ctb_objective_title,
       ItemType,
       PONUMBERSPOSSIBLE,
       IPI_at_Pass,
       State_ObjectiveTitle,
       State_MeanNumberCorrect,
       State_MeanPercentCorrect,
       State_MeanIPI,
       State_IPIDifference,
       State_NumberMastery,
       State_PercentMastery,
       Corp_ObjectiveTitle,
       Corp_MeanNumberCorrect,
       Corp_MeanPercentCorrect,
       Corp_MeanIPI,
       Corp_IPIDifference,
       Corp_NumberMastery,
       Corp_PercentMastery,
       School_ObjectiveTitle,
       School_MeanNumberCorrect,
       School_MeanPercentCorrect,
       School_MeanIPI,
       School_IPIDifference,
       School_NumberMastery,
       School_PercentMastery,
       ORG_NODEID

       )
      select ACAD_STD_SUMM_FACT_SEQ.Nextval,
             IN_CUST_PROD_ID AS cust_prod_id,
             IN_GRADEID AS GradeID,
             IN_ISPUBLIC AS ISPUBLIC,
             sysdate as DATETIMESTAMP,
             (select contentid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS contentid,
             (select objectiveid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS objectiveid,
             (select levelid
                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw
               where vw.gradeid = IN_GRADEID
                 and vw.subtestid = t.SubtestID
                 and vw.objective_code = t.ctb_objective_code) AS levelid,
             (select adminid
                from cust_product_link cpl
               where cpl.cust_prod_id = IN_CUST_PROD_ID) AS adminid,

             (case
               WHEN ctb_objective_code <= '0.0' THEN
                NULL
               ELSE
                SubtestID
             END) as SubtestID,
             ctb_objective_code,
             ctb_objective_title,
             ItemType,
             PointsPossible,
             IPI_at_Pass,
             State_ObjectiveTitle,
             State_MeanNumberCorrect,
             State_MeanPercentCorrect,
             State_MeanIPI,
             State_IPIDifference,
             State_NumberMastery,
             State_PercentMastery,
             Corp_ObjectiveTitle,
             Corp_MeanNumberCorrect,
             Corp_MeanPercentCorrect,
             Corp_MeanIPI,
             Corp_IPIDifference,
             Corp_NumberMastery,
             Corp_PercentMastery,
             School_ObjectiveTitle,
             School_MeanNumberCorrect,
             School_MeanPercentCorrect,
             School_MeanIPI,
             School_IPIDifference,
             School_NumberMastery,
             School_PercentMastery,
             ORG_NODEID

        from (select *
                from (SELECT

                       PEIDCutScoreIPI.SubtestID,

                       PEIDCutScoreIPI.ctb_objective_code,
                       PEIDCutScoreIPI.ctb_objective_title,
                       PEIDCutScoreIPI.ItemType,
                       (CASE (IS_NUMBER(PEIDCutScoreIPI.PointsPossible))
                         WHEN -99 THEN
                          NULL
                         ELSE
                          PEIDCutScoreIPI.PointsPossible
                       END) AS PointsPossible,
                       PEIDCutScoreIPI.IPI_at_Pass,

                       NULL /*StateSummary.ObjectiveTitle*/     AS State_ObjectiveTitle,
                       StateSummary.MeanNumberCorrect  AS State_MeanNumberCorrect,
                       StateSummary.MeanPercentCorrect AS State_MeanPercentCorrect,
                       StateSummary.MeanIPI            AS State_MeanIPI,
                       StateSummary.IPIDifference      AS State_IPIDifference,
                       StateSummary.NumberMastery      AS State_NumberMastery,
                       StateSummary.PercentMastery     AS State_PercentMastery,

                       NULL /*CorpSummary.ObjectiveTitle*/     AS Corp_ObjectiveTitle,
                       CorpSummary.MeanNumberCorrect  AS Corp_MeanNumberCorrect,
                       CorpSummary.MeanPercentCorrect AS Corp_MeanPercentCorrect,
                       CorpSummary.MeanIPI            AS Corp_MeanIPI,
                       CorpSummary.IPIDifference      AS Corp_IPIDifference,
                       CorpSummary.NumberMastery      AS Corp_NumberMastery,
                       CorpSummary.PercentMastery     AS Corp_PercentMastery,

                       NULL /*SchoolSummary.ObjectiveTitle*/     AS School_ObjectiveTitle,
                       SchoolSummary.MeanNumberCorrect  AS School_MeanNumberCorrect,
                       SchoolSummary.MeanPercentCorrect AS School_MeanPercentCorrect,
                       SchoolSummary.MeanIPI            AS School_MeanIPI,
                       SchoolSummary.IPIDifference      AS School_IPIDifference,
                       SchoolSummary.NumberMastery      AS School_NumberMastery,
                       SchoolSummary.PercentMastery     AS School_PercentMastery,

                       IN_org_node_id AS org_nodeid
                        FROM (
                              --Data from Result_PEID and CutScoreIPI
                              SELECT DISTINCT stomp.SubtestID as SubtestID,
                                               od.objective_code as ctb_objective_code,
                                               '   ' || od.objective_name AS ctb_objective_title,
                                               nvl2(TRIM(ITEM_NAME),
                                                    '(' || ITEM_NAME || ')',
                                                    ITEM_NAME) AS ItemType,
                                               POINT_POSSIBLE AS PointsPossible,
                                               (SELECT IPI_at_Pass
                                                  FROM CutScoreIPI
                                                 WHERE CutScoreIPI.GradeID =
                                                       vw.gradeid
                                                   AND CutScoreIPI.OBJECTIVEID =
                                                       od.OBJECTIVEID
                                                   AND CutScoreIPI.SubtestID =
                                                       stomp.SubtestID
                                                   AND CutScoreIPI.CUST_PROD_ID =
                                                       cpl.CUST_PROD_ID) AS IPI_at_pass
                                FROM itemset_dim                    B,
                                      subtest_objective_map          stomp,
                                      objective_dim                  od,
                                      vw_subtest_grade_objective_map vw,
                                      cust_product_link              cpl,
                                      assessment_dim                 adim
                               WHERE cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 and item_type = 'OBJ'
                                 and B.SUBT_OBJ_MAPID = stomp.subt_obj_mapid
                                 and od.objectiveid = stomp.objectiveid
                                 and vw.objectiveid = stomp.objectiveid
                                 and vw.subtestid = stomp.SubtestID
                                 and vw.gradeid = IN_GRADEID
                                 and adim.productid = cpl.productid
                                 and vw.assessmentid = adim.assessmentid

                              --Adding Subject Name 'English/language arts' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'ELA'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,

                                     '0.0' AS ctb_objective_code,
                                     'English/Language Arts' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual
                              --Adding Subject Name 'Mathematics' in the resultset returned
                              UNION
                              SELECT (select distinct subtestid
                                        from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                             assessment_dim                 assess,
                                             CUST_PRODUCT_LINK              cpl
                                       where vw.GRADEID = IN_GRADEID
                                         AND vw.SUBTEST_CODE = 'MATH'
                                         AND assess.assessmentid =
                                             vw.assessmentid
                                         AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                         AND cpl.productid = assess.productid) AS SubtestID,
                                     '0.0' AS ctb_objective_code,
                                     'Mathematics' AS ctb_objective_title,
                                     NULL AS ItemType,
                                     NULL AS PointsPossible,
                                     NULL AS IPI_at_Pass
                                from dual

                              ) PEIDCutScoreIPI

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_STATE_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) StateSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                      StateSummary.SubtestID
                                                                                  AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                      StateSummary.objective_code

                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_DISTR_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) CorpSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                     CorpSummary.SubtestID
                                                                                 AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                     CorpSummary.objective_code
                        LEFT OUTER JOIN (

                                        select REGEXP_SUBSTR(SubtestID || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS SubtestID,
                                                REGEXP_SUBSTR(Objective_Code || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS Objective_Code,
                                                REGEXP_SUBSTR(ObjectiveTitle || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS ObjectiveTitle,
                                                REGEXP_SUBSTR(MeanNumberCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanNumberCorrect,
                                                REGEXP_SUBSTR(MeanPercentCorrect || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanPercentCorrect,
                                                REGEXP_SUBSTR(MeanIPI || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS MeanIPI,
                                                REGEXP_SUBSTR(IPIDifference || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS IPIDifference,
                                                REGEXP_SUBSTR(NumberMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS NumberMastery,
                                                REGEXP_SUBSTR(PercentMastery || ',',
                                                              '([^,]*),|$',
                                                              1,
                                                              rn,
                                                              NULL,
                                                              1) AS PercentMastery,
                                                ORG_NODEID
                                          from (SELECT ORG_NODEID,
                                                        (select listagg(vw1.subtestid,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS SubtestID,
                                                        (select listagg(vw1.objective_code,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by subtestid)
                                                           from vw_subtest_grade_objective_map vw1
                                                          where vw1.gradeid =
                                                                IN_GRADEID
                                                            and vw1.assessmentid =
                                                                IN_ASSESSMENT_ID) AS Objective_Code,
                                                        (select listagg(objective_name,
                                                                        ',') WITHIN
                                                          GROUP(
                                                          order by rn1)

                                                           from (select ROW_NUMBER() over(partition by SubtestID order by SubtestID, objective_code) || '. ' ||
                                                           (case
                                                                when trim(vw1.objective_name) = 'Vocabulary' then
                                                                 'Reading Vocabulary'
                                                                when trim(vw1.objective_name) = 'Nonfiction/Info Text' then
                                                                 'Reading Comp.'
                                                                when trim(vw1.objective_name) = 'Literary Text' then
                                                                 'Lit Response & Analysis'
                                                                when trim(vw1.objective_name) = 'Nature of Sci & Tech' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Physical Science' then
                                                                 'Nature of Sci and Tech'
                                                                when trim(vw1.objective_name) = 'Science Eng & Tech' then
                                                                 'The Living Environment'
                                                                when trim(vw1.objective_name) = 'The Nature of Science' then
                                                                 'The Mathematical World'
                                                                when trim(vw1.objective_name) = 'Earth Science' then
                                                                 'Scientific Thinking'
                                                                when trim(vw1.objective_name) = 'Life Science' then
                                                                 'The Physical Setting'
                                                                when trim(vw1.objective_name) = 'The Design Process' then
                                                                 'Common Themes'
                                                                when trim(vw1.objective_name) = 'Earth & Space Science' then
                                                                 'Scientific Thinking'
                                                                else
                                                                 trim(vw1.objective_name)
                                                              end) objective_name,
                                                                        vw1.RN rn1
                                                                   from vw_subtest_grade_objective_map vw1
                                                                  where vw1.gradeid =
                                                                        IN_GRADEID
                                                                    and vw1.assessmentid =
                                                                        IN_ASSESSMENT_ID)) AS ObjectiveTitle,
                                                        Mean_number_correct_1 || ',' ||
                                                        Mean_number_correct_2 || ',' ||
                                                        Mean_number_correct_3 || ',' ||
                                                        Mean_number_correct_4 || ',' ||
                                                        Mean_number_correct_5 || ',' ||
                                                        Mean_number_correct_6 || ',' ||
                                                        Mean_number_correct_7 || ',' ||
                                                        Mean_number_correct_8 || ',' ||
                                                        Mean_number_correct_9 || ',' ||
                                                        Mean_number_correct_10 || ',' ||
                                                        Mean_number_correct_11 || ',' ||
                                                        Mean_number_correct_12 || ',' ||
                                                        Mean_number_correct_13 || ',' ||
                                                        Mean_number_correct_14 || ',' ||
                                                        Mean_number_correct_15 || ',' ||
                                                        Mean_number_correct_16 || ',' ||
                                                        Mean_number_correct_17 || ',' ||
                                                        Mean_number_correct_18 || ',' ||
                                                        Mean_number_correct_19 || ',' ||
                                                        Mean_number_correct_20 || ',' ||
                                                        Mean_number_correct_21 || ',' ||
                                                        Mean_number_correct_22 || ',' ||
                                                        Mean_number_correct_23 || ',' ||
                                                        Mean_number_correct_24 || ',' ||
                                                        Mean_number_correct_25 || ',' ||
                                                        Mean_number_correct_26 || ',' ||
                                                        Mean_number_correct_27 || ',' ||
                                                        Mean_number_correct_28 || ',' ||
                                                        Mean_number_correct_29 || ',' ||
                                                        Mean_number_correct_30 || ',' ||
                                                        Mean_number_correct_31 AS MeanNumberCorrect,
                                                        Mean_percent_correct_1 || ',' ||
                                                        Mean_percent_correct_2 || ',' ||
                                                        Mean_percent_correct_3 || ',' ||
                                                        Mean_percent_correct_4 || ',' ||
                                                        Mean_percent_correct_5 || ',' ||
                                                        Mean_percent_correct_6 || ',' ||
                                                        Mean_percent_correct_7 || ',' ||
                                                        Mean_percent_correct_8 || ',' ||
                                                        Mean_percent_correct_9 || ',' ||
                                                        Mean_percent_correct_10 || ',' ||
                                                        Mean_percent_correct_11 || ',' ||
                                                        Mean_percent_correct_12 || ',' ||
                                                        Mean_percent_correct_13 || ',' ||
                                                        Mean_percent_correct_14 || ',' ||
                                                        Mean_percent_correct_15 || ',' ||
                                                        Mean_percent_correct_16 || ',' ||
                                                        Mean_percent_correct_17 || ',' ||
                                                        Mean_percent_correct_18 || ',' ||
                                                        Mean_percent_correct_19 || ',' ||
                                                        Mean_percent_correct_20 || ',' ||
                                                        Mean_percent_correct_21 || ',' ||
                                                        Mean_percent_correct_22 || ',' ||
                                                        Mean_percent_correct_23 || ',' ||
                                                        Mean_percent_correct_24 || ',' ||
                                                        Mean_percent_correct_25 || ',' ||
                                                        Mean_percent_correct_26 || ',' ||
                                                        Mean_percent_correct_27 || ',' ||
                                                        Mean_percent_correct_28 || ',' ||
                                                        Mean_percent_correct_29 || ',' ||
                                                        Mean_percent_correct_30 || ',' ||
                                                        Mean_percent_correct_31 AS MeanPercentCorrect,

                                                        Mean_IPI_1 || ',' ||
                                                        Mean_IPI_2 || ',' ||
                                                        Mean_IPI_3 || ',' ||
                                                        Mean_IPI_4 || ',' ||
                                                        Mean_IPI_5 || ',' ||
                                                        Mean_IPI_6 || ',' ||
                                                        Mean_IPI_7 || ',' ||
                                                        Mean_IPI_8 || ',' ||
                                                        Mean_IPI_9 || ',' ||
                                                        Mean_IPI_10 || ',' ||
                                                        Mean_IPI_11 || ',' ||
                                                        Mean_IPI_12 || ',' ||
                                                        Mean_IPI_13 || ',' ||
                                                        Mean_IPI_14 || ',' ||
                                                        Mean_IPI_15 || ',' ||
                                                        Mean_IPI_16 || ',' ||
                                                        Mean_IPI_17 || ',' ||
                                                        Mean_IPI_18 || ',' ||
                                                        Mean_IPI_19 || ',' ||
                                                        Mean_IPI_20 || ',' ||
                                                        Mean_IPI_21 || ',' ||
                                                        Mean_IPI_22 || ',' ||
                                                        Mean_IPI_23 || ',' ||
                                                        Mean_IPI_24 || ',' ||
                                                        Mean_IPI_25 || ',' ||
                                                        Mean_IPI_26 || ',' ||
                                                        Mean_IPI_27 || ',' ||
                                                        Mean_IPI_28 || ',' ||
                                                        Mean_IPI_29 || ',' ||
                                                        Mean_IPI_30 || ',' ||
                                                        Mean_IPI_31 AS MeanIPI,
                                                        IPI_Difference_1 || ',' ||
                                                        IPI_Difference_2 || ',' ||
                                                        IPI_Difference_3 || ',' ||
                                                        IPI_Difference_4 || ',' ||
                                                        IPI_Difference_5 || ',' ||
                                                        IPI_Difference_6 || ',' ||
                                                        IPI_Difference_7 || ',' ||
                                                        IPI_Difference_8 || ',' ||
                                                        IPI_Difference_9 || ',' ||
                                                        IPI_Difference_10 || ',' ||
                                                        IPI_Difference_11 || ',' ||
                                                        IPI_Difference_12 || ',' ||
                                                        IPI_Difference_13 || ',' ||
                                                        IPI_Difference_14 || ',' ||
                                                        IPI_Difference_15 || ',' ||
                                                        IPI_Difference_16 || ',' ||
                                                        IPI_Difference_17 || ',' ||
                                                        IPI_Difference_18 || ',' ||
                                                        IPI_Difference_19 || ',' ||
                                                        IPI_Difference_20 || ',' ||
                                                        IPI_Difference_21 || ',' ||
                                                        IPI_Difference_22 || ',' ||
                                                        IPI_Difference_23 || ',' ||
                                                        IPI_Difference_24 || ',' ||
                                                        IPI_Difference_25 || ',' ||
                                                        IPI_Difference_26 || ',' ||
                                                        IPI_Difference_27 || ',' ||
                                                        IPI_Difference_28 || ',' ||
                                                        IPI_Difference_29 || ',' ||
                                                        IPI_Difference_30 || ',' ||
                                                        IPI_Difference_31 AS IPIDifference,
                                                        Number_Mastery_1 || ',' ||
                                                        Number_Mastery_2 || ',' ||
                                                        Number_Mastery_3 || ',' ||
                                                        Number_Mastery_4 || ',' ||
                                                        Number_Mastery_5 || ',' ||
                                                        Number_Mastery_6 || ',' ||
                                                        Number_Mastery_7 || ',' ||
                                                        Number_Mastery_8 || ',' ||
                                                        Number_Mastery_9 || ',' ||
                                                        Number_Mastery_10 || ',' ||
                                                        Number_Mastery_11 || ',' ||
                                                        Number_Mastery_12 || ',' ||
                                                        Number_Mastery_13 || ',' ||
                                                        Number_Mastery_14 || ',' ||
                                                        Number_Mastery_15 || ',' ||
                                                        Number_Mastery_16 || ',' ||
                                                        Number_Mastery_17 || ',' ||
                                                        Number_Mastery_18 || ',' ||
                                                        Number_Mastery_19 || ',' ||
                                                        Number_Mastery_20 || ',' ||
                                                        Number_Mastery_21 || ',' ||
                                                        Number_Mastery_22 || ',' ||
                                                        Number_Mastery_23 || ',' ||
                                                        Number_Mastery_24 || ',' ||
                                                        Number_Mastery_25 || ',' ||
                                                        Number_Mastery_26 || ',' ||
                                                        Number_Mastery_27 || ',' ||
                                                        Number_Mastery_28 || ',' ||
                                                        Number_Mastery_29 || ',' ||
                                                        Number_Mastery_30 || ',' ||
                                                        Number_Mastery_31 AS NumberMastery,
                                                        Percent_Mastery_1 || ',' ||
                                                        Percent_Mastery_2 || ',' ||
                                                        Percent_Mastery_3 || ',' ||
                                                        Percent_Mastery_4 || ',' ||
                                                        Percent_Mastery_5 || ',' ||
                                                        Percent_Mastery_6 || ',' ||
                                                        Percent_Mastery_7 || ',' ||
                                                        Percent_Mastery_8 || ',' ||
                                                        Percent_Mastery_9 || ',' ||
                                                        Percent_Mastery_10 || ',' ||
                                                        Percent_Mastery_11 || ',' ||
                                                        Percent_Mastery_12 || ',' ||
                                                        Percent_Mastery_13 || ',' ||
                                                        Percent_Mastery_14 || ',' ||
                                                        Percent_Mastery_15 || ',' ||
                                                        Percent_Mastery_16 || ',' ||
                                                        Percent_Mastery_17 || ',' ||
                                                        Percent_Mastery_18 || ',' ||
                                                        Percent_Mastery_19 || ',' ||
                                                        Percent_Mastery_20 || ',' ||
                                                        Percent_Mastery_21 || ',' ||
                                                        Percent_Mastery_22 || ',' ||
                                                        Percent_Mastery_23 || ',' ||
                                                        Percent_Mastery_24 || ',' ||
                                                        Percent_Mastery_25 || ',' ||
                                                        Percent_Mastery_26 || ',' ||
                                                        Percent_Mastery_27 || ',' ||
                                                        Percent_Mastery_28 || ',' ||
                                                        Percent_Mastery_29 || ',' ||
                                                        Percent_Mastery_30 || ',' ||
                                                        Percent_Mastery_31 AS PercentMastery,
                                                        vw.rn rn
                                                   FROM SUMT_FACT                      a,
                                                        vw_subtest_grade_objective_map vw,
                                                        cust_product_link              cpl,
                                                        assessment_dim                 asd
                                                  WHERE a.GRADEID = IN_GRADEID
                                                    and a.org_nodeid =
                                                        IN_ORGNODE_SCHOL_ID
                                                    and a.ispublic = IN_ISPUBLIC
                                                    and a.CUST_PROD_ID =
                                                        IN_CUST_PROD_ID
                                                    and vw.gradeid = a.GRADEID
                                                    and vw.assessmentid =
                                                        asd.assessmentid
                                                    and cpl.cust_prod_id =
                                                        a.CUST_PROD_ID
                                                    and cpl.productid =
                                                        asd.productid)) SchoolSummary ON PEIDCutScoreIPI.SubtestID =
                                                                                       SchoolSummary.SubtestID
                                                                                   AND PEIDCutScoreIPI.ctb_objective_code =
                                                                                       SchoolSummary.objective_code

                      -- Adding values for Number of students in the subject English/Language arts
                      UNION ALL

                      SELECT

                       (select distinct subtestid + 1
                          from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                               assessment_dim                 assess,
                               CUST_PRODUCT_LINK              cpl
                         where vw.GRADEID = IN_GRADEID
                           AND vw.SUBTEST_CODE = 'ELA'
                           AND assess.assessmentid = vw.assessmentid
                           AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                           AND cpl.productid = assess.productid) AS SubtestID,
                       '-2' AS ctb_objective_code,
                       '*Number of students: ' || NVL(IN_NoOfStudentELA, '0') AS ctb_objective_title,
                       NULL AS ItemType,
                       NULL AS PointsPossible,
                       NULL AS IPI_at_Pass,

                       NULL AS State_ObjectiveTitle,
                       NULL AS State_MeanNumberCorrect,
                       NULL AS State_MeanPercentCorrect,
                       NULL AS State_MeanIPI,
                       NULL AS State_IPIDifference,
                       NULL AS State_NumberMastery,
                       NULL AS State_PercentMastery,

                       NULL AS Corp_ObjectiveTitle,
                       NULL AS Corp_MeanNumberCorrect,
                       NULL AS Corp_MeanPercentCorrect,
                       NULL AS Corp_MeanIPI,
                       NULL AS Corp_IPIDifference,
                       NULL AS Corp_NumberMastery,
                       NULL AS Corp_PercentMastery,

                       NULL           AS School_ObjectiveTitle,
                       NULL           AS School_MeanNumberCorrect,
                       NULL           AS School_MeanPercentCorrect,
                       NULL           AS School_MeanIPI,
                       NULL           AS School_IPIDifference,
                       NULL           AS School_NumberMastery,
                       NULL           AS School_PercentMastery,
                       IN_org_node_id AS org_nodeid

                        FROM DUAL

                      -- Adding values for Number of students in the subject Mathematics

                      UNION ALL

                      SELECT (select distinct subtestid + 1
                                from VW_SUBTEST_GRADE_OBJECTIVE_MAP vw,
                                     assessment_dim                 assess,
                                     CUST_PRODUCT_LINK              cpl
                               where vw.GRADEID = IN_GRADEID
                                 AND vw.SUBTEST_CODE = 'MATH'
                                 AND assess.assessmentid = vw.assessmentid
                                 AND cpl.CUST_PROD_ID = IN_CUST_PROD_ID
                                 AND cpl.productid = assess.productid) AS SubtestID,
                             '-2' AS ctb_objective_code,
                             '*Number of students: ' ||
                             NVL(IN_NoOfStudentMath, '0') AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        FROM DUAL

                      UNION ALL

                      SELECT NULL AS SubtestID,
                             '-1' AS ctb_objective_code,
                             '****Total Number of students: ' ||
                             (select case
                                       when count(1) = 0 THEN
                                        ' '
                                       ELSE
                                        to_char(max(CRT_Student_Cnt))
                                     END
                                FROM SUMT_FACT
                               WHERE GRADEID = IN_GRADEID
                                 and org_nodeid = IN_org_node_id
                                 and ispublic = IN_ISPUBLIC
                                 and CUST_PROD_ID = IN_CUST_PROD_ID) AS ctb_objective_title,
                             NULL AS ItemType,
                             NULL AS PointsPossible,
                             NULL AS IPI_at_Pass,

                             NULL AS State_ObjectiveTitle,
                             NULL AS State_MeanNumberCorrect,
                             NULL AS State_MeanPercentCorrect,
                             NULL AS State_MeanIPI,
                             NULL AS State_IPIDifference,
                             NULL AS State_NumberMastery,
                             NULL AS State_PercentMastery,

                             NULL AS Corp_ObjectiveTitle,
                             NULL AS Corp_MeanNumberCorrect,
                             NULL AS Corp_MeanPercentCorrect,
                             NULL AS Corp_MeanIPI,
                             NULL AS Corp_IPIDifference,
                             NULL AS Corp_NumberMastery,
                             NULL AS Corp_PercentMastery,

                             NULL           AS School_ObjectiveTitle,
                             NULL           AS School_MeanNumberCorrect,
                             NULL           AS School_MeanPercentCorrect,
                             NULL           AS School_MeanIPI,
                             NULL           AS School_IPIDifference,
                             NULL           AS School_NumberMastery,
                             NULL           AS School_PercentMastery,
                             IN_org_node_id AS org_nodeid
                        from dual)
               order by SubtestID, ctb_objective_code) t
       where ctb_objective_title <> '   ';

    commit;

  END PRC_Rprt_Acad_Stand_Summ_grd8;

  PROCEDURE PRC_Rprt_Acad_Stand_Summary(IN_CUST_PROD_ID     number,
                                        IN_ORGNODE_SCHOL_ID number,
                                        IN_ORGNODE_DISTR_ID number,
                                        IN_ORGNODE_STATE_ID number,
                                        IN_GRADEID          number,
                                        IN_ISPUBLIC         number,
                                        IN_ASSESSMENT_ID    number)

   AS

    LV_ORG_NODE_ID           NUMBER;
    LV_NOOFSTUDENTELA        VARCHAR2(10);
    LV_NOOFSTUDENTMATH       VARCHAR2(10);
    LV_NOOFSTUDENTSCIENCE    VARCHAR2(10);
    LV_NOOFSTUDENTSOCSCIENCE VARCHAR2(10);

    lv_ORGNODE_SCHOL_ID number;
    lv_ORGNODE_DISTR_ID number;
    lv_ORGNODE_STATE_ID number;

  BEGIN

    /*
    * The Results_AcademicStandardSummary table serves as a cache for the results of
    * Results_AcademicStandardSummary procedure calls.  It is checked first; if results are
    * found, the cache is used.  Otherwise, the original proc runs and values are cached.
    */

    IF IN_ORGNODE_SCHOL_ID IS NOT NULL THEN

      /*
         When SCHOOL ID is sent then its parent district and parent state is fetched
         LV_ORGNODE_SCHOL_ID stores school id
         LV_ORGNODE_DISTR_ID stores district id
         LV_ORGNODE_STATE_ID stores state id

         LV_ORG_NODE_ID stores the ORG_NODEID for which the records will be inserted, in this case its the school id.

      */

      LV_ORG_NODE_ID := IN_ORGNODE_SCHOL_ID;

      LV_ORGNODE_SCHOL_ID := IN_ORGNODE_SCHOL_ID;

      select parent_org_nodeid
        INTO LV_ORGNODE_DISTR_ID
        from org_node_dim
       where org_nodeid = LV_ORGNODE_SCHOL_ID;

      select parent_org_nodeid
        INTO LV_ORGNODE_STATE_ID
        from org_node_dim
       where org_nodeid = LV_ORGNODE_DISTR_ID;

    ELSIF IN_ORGNODE_DISTR_ID IS NOT NULL THEN

      /*
         When District ID is sent then its parent state is fetched
         LV_ORGNODE_SCHOL_ID stores district id
         LV_ORGNODE_DISTR_ID stores district id
         LV_ORGNODE_STATE_ID stores state id

         LV_ORG_NODE_ID stores the ORG_NODEID for which the records will be inserted, in this case its the district id.

      */

      LV_ORG_NODE_ID := IN_ORGNODE_DISTR_ID;

      LV_ORGNODE_SCHOL_ID := -99999999/*IN_ORGNODE_DISTR_ID*/;
      LV_ORGNODE_DISTR_ID := IN_ORGNODE_DISTR_ID;

      select parent_org_nodeid
        INTO LV_ORGNODE_STATE_ID
        from org_node_dim
       where org_nodeid = LV_ORGNODE_DISTR_ID;

    ELSIF IN_ORGNODE_STATE_ID IS NOT NULL THEN

      /*
         When State ID is sent then
         LV_ORGNODE_SCHOL_ID stores state id
         LV_ORGNODE_DISTR_ID stores state id
         LV_ORGNODE_STATE_ID stores state id

         LV_ORG_NODE_ID stores the ORG_NODEID for which the records will be inserted, in this case its the state id.

      */

      LV_ORG_NODE_ID := IN_ORGNODE_STATE_ID;

      LV_ORGNODE_SCHOL_ID := -99999999/*IN_ORGNODE_STATE_ID*/;
      LV_ORGNODE_DISTR_ID := -99999999/*IN_ORGNODE_STATE_ID*/;
      LV_ORGNODE_STATE_ID := IN_ORGNODE_STATE_ID;

    END IF;

    select *
      into lv_NoOfStudentMath,
           lv_NoOfStudentELA,
           lv_NoOfStudentScience,
           lv_NoOfStudentSocscience
      from (SELECT distinct (CASE IS_Number(NumberUND)
                              When -99 THEN
                               TO_CHAR(NumberUND)
                              ELSE

                               TO_CHAR(TotalTested - NumberUND)

                            END) NoOfStudent,
                            subtest_code

              FROM DISA_FACT DF
              JOIN DISAGGREGATION_CATEGORY DC ON DF.DisaggregationCategoryID =
                                                 DC.DisaggregationCategoryID
              JOIN subtest_dim sd on sd.SubtestID = df.SubtestID
             WHERE DF.CUST_PROD_ID = IN_CUST_PROD_ID
               AND ORG_NODEID = lv_org_node_id
               AND GradeID = IN_GRADEID
               AND IsPublic = IN_ISPUBLIC
               AND DC.DisaggregationCategoryCode = '01') pivot(max(NoOfStudent) FOR subtest_code IN ('MATH', 'ELA', 'SCI', 'SS'));

    --For grade 3
    IF IN_GRADEID = 10001 THEN

      PRC_Rprt_Acad_Stand_Summ_grd3(IN_CUST_PROD_ID,
                                    IN_ASSESSMENT_ID,
                                    IN_GRADEID,
                                    IN_ISPUBLIC,
                                    lv_org_node_id,
                                    LV_ORGNODE_SCHOL_ID,
                                    LV_ORGNODE_DISTR_ID,
                                    LV_ORGNODE_STATE_ID,
                                    lv_NoOfStudentELA,
                                    lv_NoOfStudentMath,
                                    lv_NoOfStudentScience,
                                    lv_NoOfStudentSocscience);

      -- For grade 4
    ELSIF IN_GRADEID = 10002 THEN

      PRC_Rprt_Acad_Stand_Summ_grd4(IN_CUST_PROD_ID,
                                    IN_ASSESSMENT_ID,
                                    IN_GRADEID,
                                    IN_ISPUBLIC,
                                    lv_org_node_id,
                                    LV_ORGNODE_SCHOL_ID,
                                    LV_ORGNODE_DISTR_ID,
                                    LV_ORGNODE_STATE_ID,
                                    lv_NoOfStudentELA,
                                    lv_NoOfStudentMath,
                                    lv_NoOfStudentScience,
                                    lv_NoOfStudentSocscience);

      -- For grade 5
    ELSIF IN_GRADEID = 10003 THEN

      PRC_Rprt_Acad_Stand_Summ_grd5(IN_CUST_PROD_ID,
                                    IN_ASSESSMENT_ID,
                                    IN_GRADEID,
                                    IN_ISPUBLIC,
                                    lv_org_node_id,
                                    LV_ORGNODE_SCHOL_ID,
                                    LV_ORGNODE_DISTR_ID,
                                    LV_ORGNODE_STATE_ID,
                                    lv_NoOfStudentELA,
                                    lv_NoOfStudentMath,
                                    lv_NoOfStudentScience,
                                    lv_NoOfStudentSocscience);

      -- For grade 6
    ELSIF IN_GRADEID = 10004 THEN

      PRC_Rprt_Acad_Stand_Summ_grd6(IN_CUST_PROD_ID,
                                    IN_ASSESSMENT_ID,
                                    IN_GRADEID,
                                    IN_ISPUBLIC,
                                    lv_org_node_id,
                                    LV_ORGNODE_SCHOL_ID,
                                    LV_ORGNODE_DISTR_ID,
                                    LV_ORGNODE_STATE_ID,
                                    lv_NoOfStudentELA,
                                    lv_NoOfStudentMath,
                                    lv_NoOfStudentScience,
                                    lv_NoOfStudentSocscience);

      -- For grade 7

    ELSIF IN_GRADEID = 10005 THEN

      PRC_Rprt_Acad_Stand_Summ_grd7(IN_CUST_PROD_ID,
                                    IN_ASSESSMENT_ID,
                                    IN_GRADEID,
                                    IN_ISPUBLIC,
                                    lv_org_node_id,
                                    LV_ORGNODE_SCHOL_ID,
                                    LV_ORGNODE_DISTR_ID,
                                    LV_ORGNODE_STATE_ID,
                                    lv_NoOfStudentELA,
                                    lv_NoOfStudentMath,
                                    lv_NoOfStudentScience,
                                    lv_NoOfStudentSocscience);

      -- FOR Grade 8
    ELSIF IN_GRADEID = 10006 THEN

      PRC_Rprt_Acad_Stand_Summ_grd8(IN_CUST_PROD_ID,
                                    IN_ASSESSMENT_ID,
                                    IN_GRADEID,
                                    IN_ISPUBLIC,
                                    lv_org_node_id,
                                    LV_ORGNODE_SCHOL_ID,
                                    LV_ORGNODE_DISTR_ID,
                                    LV_ORGNODE_STATE_ID,
                                    lv_NoOfStudentELA,
                                    lv_NoOfStudentMath,
                                    lv_NoOfStudentScience,
                                    lv_NoOfStudentSocscience);

    END IF;

  END PRC_Rprt_Acad_Stand_Summary;

  PROCEDURE PRC_Rprt_Acad_Stand_Summ_wrapr(IN_CUST_PROD_ID number) AS

    LV_ERR_MSG       varchar2(4000);
    LV_ASSESSMENT_ID NUMBER;
    --POPULATION_ERROR EXCEPTION;

  BEGIN

    select assessmentid
      INTO LV_ASSESSMENT_ID
      from cust_product_link cpl, assessment_dim a
     where cpl.cust_prod_id = IN_CUST_PROD_ID
       and cpl.productid = a.productid;

    -- Deletes the records from ACAD_STD_SUMM_FACT if present

    DELETE FROM acad_std_summ_fact WHERE cust_prod_id = IN_CUST_PROD_ID;

    COMMIT;

    FOR rec in (SELECT OND.ORG_NODEID,
                       OND.ORG_NODE_LEVEL,
                       1 IS_PUBLIC,
                       IN_CUST_PROD_ID CUST_PROD_ID,
                       GSL.GRADEID
                  FROM ORG_NODE_DIM OND,
                       (SELECT DISTINCT ORG_NODEID, GRADEID
                          FROM GRADE_SELECTION_LOOKUP) GSL,
                       org_product_link opl
                 WHERE ORG_NODE_LEVEL = 1
                   AND OND.ORG_NODEID = GSL.ORG_NODEID
                   and opl.cust_prod_id = IN_CUST_PROD_ID
                   and opl.org_nodeid = OND.ORG_NODEID

                UNION

                SELECT OND.ORG_NODEID,
                       OND.ORG_NODE_LEVEL,
                       0 IS_PUBLIC,
                       IN_CUST_PROD_ID CUST_PROD_ID,
                       GSL.GRADEID
                  FROM ORG_NODE_DIM OND,
                       (SELECT DISTINCT ORG_NODEID, GRADEID
                          FROM GRADE_SELECTION_LOOKUP) GSL,
                       org_product_link opl
                 WHERE ORG_NODE_LEVEL = 1
                   AND OND.ORG_NODEID = GSL.ORG_NODEID
                   and opl.cust_prod_id = IN_CUST_PROD_ID
                   and opl.org_nodeid = OND.ORG_NODEID

                UNION

                SELECT OND.ORG_NODEID,
                       OND.ORG_NODE_LEVEL,
                       CASE
                         WHEN OND.ORG_MODE = 'PUBLIC' THEN
                          1
                         ELSE
                          0
                       END IS_PUBLIC,
                       IN_CUST_PROD_ID CUST_PROD_ID,
                       GSL.GRADEID
                  FROM ORG_NODE_DIM OND,
                       (SELECT DISTINCT ORG_NODEID, GRADEID
                          FROM GRADE_SELECTION_LOOKUP
                         WHERE ASSESSMENTID IN
                               (SELECT ASSESSMENTID
                                  FROM ASSESSMENT_DIM P, CUST_PRODUCT_LINK B
                                 WHERE P.PRODUCTID = B.PRODUCTID
                                   AND B.CUST_PROD_ID = IN_CUST_PROD_ID)) GSL,
                       ORG_PRODUCT_LINK OPL
                 WHERE ORG_NODE_LEVEL IN (2, 3)
                   AND OND.ORG_NODEID = GSL.ORG_NODEID
                   AND OPL.CUST_PROD_ID = IN_CUST_PROD_ID
                   AND OPL.ORG_NODEID = OND.ORG_NODEID
                 ORDER BY ORG_NODE_LEVEL) loop

      BEGIN

        IF (rec.org_node_level = 1) THEN
          -- State level

          PRC_Rprt_Acad_Stand_Summary(rec.cust_prod_id,
                                      NULL,
                                      NULL,
                                      rec.org_nodeid,
                                      rec.gradeid,
                                      rec.is_public,
                                      LV_ASSESSMENT_ID);

        ELSIF (rec.org_node_level = 2) THEN
          -- District level
          PRC_Rprt_Acad_Stand_Summary(rec.cust_prod_id,
                                      NULL,
                                      rec.org_nodeid,
                                      NULL,
                                      rec.gradeid,
                                      rec.is_public,
                                      LV_ASSESSMENT_ID);

        ELSE
          -- School level
          PRC_Rprt_Acad_Stand_Summary(rec.cust_prod_id,
                                      rec.org_nodeid,
                                      NULL,
                                      NULL,
                                      rec.gradeid,
                                      rec.is_public,
                                      LV_ASSESSMENT_ID);

        END IF;

      EXCEPTION

        WHEN OTHERS THEN

          LV_ERR_MSG := SUBSTR(SQLERRM, 1, 3999);

          INSERT INTO EXCEP_ACAD_STD_SUMM_FACT
            (CUST_PROD_ID,
             ORGNODE_ID,
             ORGNODE_LEVEL,
             GRADEID,
             ISPUBLIC,
             ERROR_MSG)
          VALUES
            (rec.cust_prod_id,
             rec.org_nodeid,
             rec.org_node_level,
             rec.gradeid,
             rec.is_public,
             LV_ERR_MSG);

          COMMIT;

      END;

    --RAISE POPULATION_ERROR;

    end loop;

  END PRC_Rprt_Acad_Stand_Summ_wrapr;

end pkg_report_acad_stand_summary;
/
