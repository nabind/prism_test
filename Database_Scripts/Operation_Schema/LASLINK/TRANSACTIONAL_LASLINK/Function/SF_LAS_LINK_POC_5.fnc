CREATE OR REPLACE FUNCTION SF_LAS_LINK_POC_5 (
                               p_rowgroup IN VARCHAR2
                              ,p_disag IN VARCHAR2
                              ,p_transpose IN VARCHAR2
                              ,p_lastvalue IN VARCHAR2
                              ,p_org_nodeid IN VARCHAR2
                              ,p_hidden_columns IN VARCHAR2
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ --t_PRS_PGT_GLOBAL_TEMP_OBJ
PIPELINED
IS

  /*******************************************************************************
  * FUNCTION:  SF_LAS_LINK_POC
  * PURPOSE:   SF_LAS_LINK_POC
  * CREATED:   TCS  14/OCT/2015
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR:353639    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();
   p_Org_Id VARCHAR2(2000);
   p_ethnicity NUMBER;
   p_grade NUMBER;
   p_gender NUMBER;
   p_level NUMBER;
   p_org_list VARCHAR2(2000);
   p_context_menu VARCHAR2(4000);
   p_hidden_subtest_name VARCHAR2(4000);
   p_hidden_subtest_id VARCHAR2(4000);

 CURSOR c_Get_Las_Link_Default
  IS
  SELECT *
  FROM
  (   SELECT  A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.SUBTEST_NAME,
              A.SUBTESTID,
              A.SUBTEST_SEQ,
              COUNT (DISTINCT A.STUDENT_BIO_ID)  AS CNT,
              ROUND(AVG(A.NCR)) AS NCR,
              ROUND(AVG(A.SS)) AS SS

           FROM
             (SELECT MV.PARENT_ORG_NODEID AS ORDER_BY,
               MV.ORG_NODE_NAME AS ROW_GROUP1,
               'p_Org_Id~'||MV.ORG_NODEID AS ROW_GROUP1_ID,
               MV.STUDENT_BIO_ID,
               MV.SUBTEST_NAME ,
               MV.SUBTESTID,
               MV.SUBTEST_SEQ,
               MV.NCR,
               MV.SS
        FROM VW_LAS_MV_RPRT_STUD_DETAILS MV
        WHERE MV.ORG_NODEID IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||p_org_list||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                       LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)  --9810
          AND MV.SUBTESTID NOT IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||NVL(p_hidden_columns,'-1')||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)                                             
                                                      
          ) A
        GROUP BY
              A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.SUBTEST_NAME,
              A.SUBTESTID,
              A.SUBTEST_SEQ)
        UNPIVOT (SCORES FOR SCORE_TYPE IN (CNT AS '0_CNT',
                                           NCR AS '1_NCR' ,
                                           SS AS '2_SS'))
      ORDER BY ORDER_BY,
               ROW_GROUP1,
               ROW_GROUP1_ID,
               SUBTEST_SEQ,
               SCORE_TYPE;

 CURSOR c_Get_Las_Link_Default_Transp
  IS
   SELECT *
  FROM
   (   SELECT  A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.ROW_GROUP2,
              A.ROW_GROUP2_ID,
              A.ROW_GROUP2_SEQ,
              COUNT (DISTINCT A.STUDENT_BIO_ID)  AS CNT,
              ROUND(AVG(A.NCR)) AS NCR,
              ROUND(AVG(A.SS)) AS SS

           FROM
             (SELECT MV.PARENT_ORG_NODEID AS ORDER_BY,
               MV.ORG_NODE_NAME AS ROW_GROUP1,
               'p_Org_Id~'||MV.ORG_NODEID AS ROW_GROUP1_ID,
               MV.SUBTEST_NAME AS ROW_GROUP2,
               MV.SUBTEST_SEQ AS ROW_GROUP2_SEQ,
               'p_Org_Id~'||MV.ORG_NODEID AS ROW_GROUP2_ID,
               MV.STUDENT_BIO_ID,
               MV.NCR,
               MV.SS
        FROM VW_LAS_MV_RPRT_STUD_DETAILS MV
        WHERE MV.ORG_NODEID IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||p_org_list||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
                                                      
           AND MV.SUBTESTID NOT IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||NVL(p_hidden_columns,'-1')||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
          ) A
        GROUP BY
               A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.ROW_GROUP2,
              A.ROW_GROUP2_ID,
              A.ROW_GROUP2_SEQ)
        UNPIVOT (SCORES FOR SCORE_TYPE IN (CNT AS '0_CNT',
                                           NCR AS '1_NCR' ,
                                           SS AS '2_SS'))
      ORDER BY ORDER_BY,
               ROW_GROUP1,
               ROW_GROUP1_ID,
               ROW_GROUP2_SEQ,
               SCORE_TYPE;



CURSOR c_Get_Las_Link_Disag
  IS

SELECT *
  FROM
  (   SELECT  A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.ROW_GROUP2,
              A.ROW_GROUP3,
              A.ROW_GROUP3_SEQ,
              A.ROW_GROUP3_ID,
              A.SUBTEST_NAME,
              A.SUBTESTID,
              A.SUBTEST_SEQ,
              COUNT (DISTINCT A.STUDENT_BIO_ID)  AS CNT,
              ROUND(AVG(A.NCR)) AS NCR,
              ROUND(AVG(A.SS)) AS SS
           FROM
             (SELECT MV.PARENT_ORG_NODEID AS ORDER_BY,
               MV.ORG_NODE_NAME AS ROW_GROUP1,
               CASE WHEN p_lastvalue IS NULL OR p_lastvalue='' THEN
                    'p_Org_Id~'||MV.ORG_NODEID
               ELSE
                    p_lastvalue
               END AS ROW_GROUP1_ID,
               MV.DISAGGREGATION_TYPE AS ROW_GROUP2,
               MV.DISAGGREGATION_VALUE AS ROW_GROUP3,
               MV.DISAGGREGATION_SEQ AS ROW_GROUP3_SEQ,
               CASE
               WHEN p_Org_Id='-1' THEN
               'p_Org_Id~'||MV.ORG_NODEID||','||MV.DISAGGREGATION_TYPE||'~'||MV.DISAGGREGATION_ID
               ELSE
               p_lastvalue||','||MV.DISAGGREGATION_TYPE||'~'||MV.DISAGGREGATION_ID
               END AS ROW_GROUP3_ID,
               MV.STUDENT_BIO_ID,
               MV.SUBTEST_NAME,
               MV.SUBTESTID,
               MV.SUBTEST_SEQ,
               MV.NCR,
               MV.SS
        FROM VW_LAS_MV_RPRT_STUD_DETAILS MV
        WHERE MV.ORG_NODEID IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','|| DECODE (p_Org_Id,'-1',p_org_list,p_Org_Id)||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
         AND MV.DISAGGREGATION_TYPE IN (SELECT  A.U
                                           FROM
                                          (SELECT TRIM( SUBSTR ( txt
                                                               , INSTR (txt, ',', 1, level ) + 1
                                                               , INSTR (txt, ',', 1, level+1
                                                               )
                                                         - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                  FROM ( SELECT ','||p_rowgroup||',' AS txt
                                                            FROM dual )
                                                   CONNECT BY level <=
                                                                LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
          AND MV.SUBTESTID NOT IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||NVL(p_hidden_columns,'-1')||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)                                                      
            AND (p_ethnicity =-1 OR p_ethnicity = MV.ETHNICITYID)
            AND (p_grade = -1 OR p_grade = MV.GRADEID)
            AND (p_gender = -1 OR p_gender = MV.GENDERID)
            AND (p_level = -1 OR p_gender = MV.LEVELID)
          ) A
        GROUP BY
              A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.ROW_GROUP2,
              A.ROW_GROUP3,
              A.ROW_GROUP3_SEQ,
              A.ROW_GROUP3_ID,
              A.SUBTEST_NAME,
              A.SUBTESTID,
              A.SUBTEST_SEQ)
        UNPIVOT (SCORES FOR SCORE_TYPE IN (CNT AS '0_CNT',
                                           NCR AS '1_NCR' ,
                                           SS AS '2_SS'))
      ORDER BY ORDER_BY,
               ROW_GROUP1,
               ROW_GROUP1_ID,
               ROW_GROUP2,
               ROW_GROUP3_SEQ,
               SUBTEST_SEQ,
               SCORE_TYPE;

CURSOR c_Get_Las_Link_Disag_Transp
  IS
  SELECT *
  FROM
  (   SELECT  A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.ROW_GROUP2,
              A.ROW_GROUP2_SEQ,
              A.ROW_GROUP2_ID,
              A.ROW_GROUP3,
              A.ROW_GROUP4,
              A.ROW_GROUP4_SEQ,
              A.ROW_GROUP4_ID,
              COUNT (DISTINCT A.STUDENT_BIO_ID)  AS CNT,
              ROUND(AVG(A.NCR)) AS NCR,
              ROUND(AVG(A.SS)) AS SS
           FROM
             (SELECT MV.PARENT_ORG_NODEID AS ORDER_BY,
               MV.ORG_NODE_NAME AS ROW_GROUP1,
              CASE WHEN p_lastvalue IS NULL OR p_lastvalue='' THEN
                    'p_Org_Id~'||MV.ORG_NODEID
               ELSE
                    p_lastvalue
               END AS ROW_GROUP1_ID,
               MV.SUBTEST_NAME AS ROW_GROUP2,
               MV.SUBTEST_SEQ AS ROW_GROUP2_SEQ,
                CASE WHEN p_lastvalue IS NULL OR p_lastvalue='' THEN
                    'p_Org_Id~'||MV.ORG_NODEID
               ELSE
                    p_lastvalue
               END AS ROW_GROUP2_ID,
               MV.DISAGGREGATION_TYPE AS ROW_GROUP3,
               MV.DISAGGREGATION_VALUE AS ROW_GROUP4,
               MV.DISAGGREGATION_SEQ AS ROW_GROUP4_SEQ,
               CASE
               WHEN p_Org_Id='-1' THEN
               'p_Org_Id~'||MV.ORG_NODEID||','||MV.DISAGGREGATION_TYPE||'~'||MV.DISAGGREGATION_ID
               ELSE
                p_lastvalue||','||MV.DISAGGREGATION_TYPE||'~'||MV.DISAGGREGATION_ID
               END AS ROW_GROUP4_ID,
               MV.STUDENT_BIO_ID,
               MV.NCR,
               MV.SS
        FROM VW_LAS_MV_RPRT_STUD_DETAILS MV
        WHERE MV.ORG_NODEID IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','|| DECODE (p_Org_Id,'-1',p_org_list,p_Org_Id)||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
           AND MV.DISAGGREGATION_TYPE IN (SELECT  A.U
                                           FROM
                                          (SELECT TRIM( SUBSTR ( txt
                                                               , INSTR (txt, ',', 1, level ) + 1
                                                               , INSTR (txt, ',', 1, level+1
                                                               )
                                                         - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                  FROM ( SELECT ','||p_rowgroup||',' AS txt
                                                            FROM dual )
                                                   CONNECT BY level <=
                                                                LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
            AND MV.SUBTESTID NOT IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||NVL(p_hidden_columns,'-1')||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)                                                    
            AND (p_ethnicity =-1 OR p_ethnicity = MV.ETHNICITYID)
            AND (p_grade = -1 OR p_grade = MV.GRADEID)
            AND (p_gender = -1 OR p_gender = MV.GENDERID)
            AND (p_level = -1 OR p_gender = MV.LEVELID)
          ) A
        GROUP BY
              A.ORDER_BY,
              A.ROW_GROUP1,
              A.ROW_GROUP1_ID,
              A.ROW_GROUP2,
              A.ROW_GROUP2_SEQ,
              A.ROW_GROUP2_ID,
              A.ROW_GROUP3,
              A.ROW_GROUP4,
              A.ROW_GROUP4_SEQ,
              A.ROW_GROUP4_ID)
        UNPIVOT (SCORES FOR SCORE_TYPE IN (CNT AS '0_CNT',
                                           NCR AS '1_NCR' ,
                                           SS AS '2_SS'))
      ORDER BY ORDER_BY,
              ROW_GROUP1,
              ROW_GROUP1_ID,
              ROW_GROUP2_SEQ,
              ROW_GROUP3,
              ROW_GROUP4_SEQ;



  CURSOR c_Get_Input_Control_List
  IS
     SELECT REGEXP_SUBSTR(A.U, '[^~]+', 1, 1) NAME,
        REGEXP_SUBSTR(A.U, '[^~]+', 1, 2) VALUE,
        A.U AS FULL_VALUE
         FROM
        (SELECT TRIM( SUBSTR ( txt
                             , INSTR (txt, ',', 1, level ) + 1
                             , INSTR (txt, ',', 1, level+1
                             )
                       - INSTR (txt, ',', 1, level) -1 ) ) AS u
                FROM ( SELECT ','||p_lastvalue||',' AS txt
                          FROM dual )
                 CONNECT BY level <=
                              LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  ) A  ;


   CURSOR c_Get_All_Disag_Type
  IS
    SELECT LISTAGG(val, ', ') WITHIN GROUP (ORDER BY val) AS SUB_MENU
    FROM
     (SELECT DISTINCT q'[{'name':']'||disaggregation_type||q'[','title':']'||disaggregation_type||q'[','value': '-99'}]' AS val
       FROM VW_LAS_MV_RPRT_STUD_DETAILS
         WHERE disaggregation_type NOT IN (SELECT REGEXP_SUBSTR(A.U, '[^~]+', 1, 1) AS NAME
                                             FROM
                                            (SELECT TRIM( SUBSTR ( txt
                                                                 , INSTR (txt, ',', 1, level ) + 1
                                                                 , INSTR (txt, ',', 1, level+1
                                                                 )
                                                           - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                                    FROM ( SELECT ','|| nvl( p_lastvalue,'-1')||',' AS txt
                                                              FROM dual )
                                                     CONNECT BY level <=
                                                                  LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  ) A )
                                                                  
             AND disaggregation_type NOT IN (nvl( p_rowgroup,'-1')));


  BEGIN
   p_Org_Id := '-1';
   p_ethnicity := -1;
   p_grade := -1;
   p_gender := -1;
   p_level := -1;
   p_org_list := p_org_nodeid;

   FOR r_Get_Input_Control_List IN c_Get_Input_Control_List
   LOOP
      IF    'p_Org_Id' = r_Get_Input_Control_List.NAME THEN
             p_Org_Id := r_Get_Input_Control_List.VALUE;

            /*WHEN WE ARE REMOVING DISAGGREGATION AFTER DOING SOME ROWLEVEL DISAGGREGATION*/
            IF p_disag = '0' THEN
              p_org_list := r_Get_Input_Control_List.VALUE;
            END IF;

      ELSIF 'Ethnicity' = r_Get_Input_Control_List.NAME THEN
             p_ethnicity := r_Get_Input_Control_List.VALUE ;
      ELSIF 'Grade' = r_Get_Input_Control_List.NAME THEN
             p_grade := r_Get_Input_Control_List.VALUE ;
      ELSIF 'Gender' = r_Get_Input_Control_List.NAME THEN
             p_gender := r_Get_Input_Control_List.VALUE  ;
      ELSIF 'Level' = r_Get_Input_Control_List.NAME THEN
             p_level := r_Get_Input_Control_List.VALUE  ;
    END IF;
   END LOOP;
  
    SELECT LISTAGG(SUBTEST_NAME, ', ') WITHIN GROUP (ORDER BY SUBTEST_SEQ) ,
           LISTAGG(SUBTESTID, ', ') WITHIN GROUP (ORDER BY SUBTEST_SEQ) 
           INTO p_hidden_subtest_name, p_hidden_subtest_id
    FROM 
    (SELECT DISTINCT SUBTESTID, SUBTEST_NAME,SUBTEST_SEQ 
      FROM vw_subtest_grade_objective_map 
      WHERE productid = 3001 
       AND SUBTESTID IN (SELECT  A.U
                                 FROM
                                (SELECT TRIM( SUBSTR ( txt
                                                     , INSTR (txt, ',', 1, level ) + 1
                                                     , INSTR (txt, ',', 1, level+1
                                                     )
                                               - INSTR (txt, ',', 1, level) -1 ) ) AS u
                                        FROM ( SELECT ','||NVL(p_hidden_columns,'-1')||',' AS txt
                                                  FROM dual )
                                         CONNECT BY level <=
                                                      LENGTH(txt)-LENGTH(REPLACE(txt,',',''))-1  )A)
      ORDER BY SUBTEST_SEQ);
   
   p_context_menu := q'[[{'name': 'Drill To Roster','title': 'Drill To Roster', 'value':'-99', 'subMenu': [{'name': 'D/2','title': 'D/2', 'value': '-99'}]},{'name': 'Disaggregate By','title': 'Disaggregate By', 'value': '-99', 'subMenu': []';

  FOR   r_Get_All_Disag_Type IN c_Get_All_Disag_Type
    LOOP
      p_context_menu := p_context_menu||r_Get_All_Disag_Type.SUB_MENU;
      DBMS_OUTPUT.put_line(r_Get_All_Disag_Type.SUB_MENU);
    END LOOP;
      p_context_menu := p_context_menu||q'[  ]}]]';
      

     IF p_transpose = '0' THEN
     dbms_output.put_line('hi4');
           IF  p_disag = '1'  THEN

            FOR r_Get_Las_Link_Disag IN c_Get_Las_Link_Disag
            LOOP
            dbms_output.put_line('hi3');
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Las_Link_Disag.ORDER_BY;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Las_Link_Disag.ROW_GROUP1;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := q'[{'displayName':']'||r_Get_Las_Link_Disag.ROW_GROUP1||q'[','value':']'||r_Get_Las_Link_Disag.ROW_GROUP1_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Las_Link_Disag.ROW_GROUP2;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Las_Link_Disag.ROW_GROUP3;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Las_Link_Disag.ROW_GROUP3_SEQ;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := q'[{'displayName':']'||r_Get_Las_Link_Disag.ROW_GROUP3||q'[','value':']'||r_Get_Las_Link_Disag.ROW_GROUP3_ID ||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
                  
                  IF p_hidden_subtest_id IS NULL THEN
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Las_Link_Disag.SUBTEST_NAME||'|'||q'[{'displayName':']'||r_Get_Las_Link_Disag.SUBTEST_NAME ||q'[', 'value':']'|| p_lastvalue || q'[','hiddenColumns':']'|| p_hidden_subtest_id  || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup || q'[', 'contextMenu':[ { 'name': 'Hide this Content Area', 'title': 'Hide this Content Area', 'value':']' || r_Get_Las_Link_Disag.SUBTESTID || q'['}]}]';  
                  ELSE
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Las_Link_Disag.SUBTEST_NAME||'|'||q'[{'displayName':']'||r_Get_Las_Link_Disag.SUBTEST_NAME ||q'[', 'value':']'|| p_lastvalue || q'[','hiddenColumns':']'|| p_hidden_subtest_id  || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup  || q'[', 'contextMenu':[ { 'name': 'Hide this Content Area', 'title': 'Hide this Content Area', 'value':']' || r_Get_Las_Link_Disag.SUBTESTID || q'['}, {'name': 'Show Content Area', 'title': 'Show Content Area', 'value': '-99', 'subMenu': [{'name':']' || p_hidden_subtest_name || q'[', 'title':']' || p_hidden_subtest_name  || q'[', 'value':']' || p_hidden_subtest_id || q'['}]}]}]';  
                  END IF;
                  
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Las_Link_Disag.SUBTEST_SEQ;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Get_Las_Link_Disag.SCORE_TYPE;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Las_Link_Disag.SCORES;

                PIPE ROW (t_PRS_PGT_GLOBAL_TEMP_OBJ);
             END LOOP;

           ELSE
            FOR r_Get_Las_Link_Default IN c_Get_Las_Link_Default
            LOOP
            dbms_output.put_line('hi2');
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Las_Link_Default.ORDER_BY;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Las_Link_Default.ROW_GROUP1;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := q'[{'displayName':']'||r_Get_Las_Link_Default.ROW_GROUP1||q'[','value':']'||r_Get_Las_Link_Default.ROW_GROUP1_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id ||  q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup  ||q'[','contextMenu':]'||p_context_menu||'}';
                
                IF p_hidden_subtest_id IS NULL THEN
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Las_Link_Default.SUBTEST_NAME||'|'||q'[{'displayName':']'||r_Get_Las_Link_Default.SUBTEST_NAME ||q'[', 'value':']'|| p_org_list || q'[','hiddenColumns':']'|| p_hidden_subtest_id  || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup || q'[', 'contextMenu':[ { 'name': 'Hide this Content Area', 'title': 'Hide this Content Area', 'value':']' || r_Get_Las_Link_Default.SUBTESTID || q'['}]}]';  
                ELSE
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Las_Link_Default.SUBTEST_NAME||'|'||q'[{'displayName':']'||r_Get_Las_Link_Default.SUBTEST_NAME ||q'[', 'value':']'|| p_org_list || q'[','hiddenColumns':']'|| p_hidden_subtest_id  || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup || q'[', 'contextMenu':[ { 'name': 'Hide this Content Area', 'title': 'Hide this Content Area', 'value':']' || r_Get_Las_Link_Default.SUBTESTID || q'['}, {'name': 'Show Content Area', 'title': 'Show Content Area', 'subMenu': [{'name':']' || p_hidden_subtest_name || q'[', 'title':']' || p_hidden_subtest_name || q'[', 'value':']' || p_hidden_subtest_id || q'['}]}]}]';
                END IF;
                
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Las_Link_Default.SUBTEST_SEQ;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Las_Link_Default.SCORE_TYPE;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Las_Link_Default.SCORES;
                /*t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;*/
                PIPE ROW (t_PRS_PGT_GLOBAL_TEMP_OBJ);

            END LOOP;
          END IF;

     ELSE
     IF  p_disag = '1'  THEN

      FOR r_Get_Las_Link_Disag_Transp IN c_Get_Las_Link_Disag_Transp
      LOOP
      dbms_output.put_line('hi1');
      t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Las_Link_Disag_Transp.ORDER_BY;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Las_Link_Disag_Transp.ROW_GROUP1;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := q'[{'displayName':']'||r_Get_Las_Link_Disag_Transp.ROW_GROUP1||q'[','value':']'||r_Get_Las_Link_Disag_Transp.ROW_GROUP1_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Las_Link_Disag_Transp.ROW_GROUP2;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Get_Las_Link_Disag_Transp.ROW_GROUP2_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := q'[{'displayName':']'||r_Get_Las_Link_Disag_Transp.ROW_GROUP2||q'[','value':']'||r_Get_Las_Link_Disag_Transp.ROW_GROUP2_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Las_Link_Disag_Transp.ROW_GROUP3;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Las_Link_Disag_Transp.ROW_GROUP4;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Get_Las_Link_Disag_Transp.ROW_GROUP4_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := q'[{'displayName':']'||r_Get_Las_Link_Disag_Transp.ROW_GROUP4||q'[','value':']'||r_Get_Las_Link_Disag_Transp.ROW_GROUP4_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Get_Las_Link_Disag_Transp.SCORE_TYPE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Get_Las_Link_Disag_Transp.SCORES;
          PIPE ROW (t_PRS_PGT_GLOBAL_TEMP_OBJ);
       END LOOP;

     ELSE
      FOR r_Get_Las_Link_Default_Transp IN c_Get_Las_Link_Default_Transp
      LOOP
      dbms_output.put_line('hi');
      t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Las_Link_Default_Transp.ORDER_BY;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Las_Link_Default_Transp.ROW_GROUP1;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := q'[{'displayName':']'||r_Get_Las_Link_Default_Transp.ROW_GROUP1||q'[','value':']'||r_Get_Las_Link_Default_Transp.ROW_GROUP1_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Get_Las_Link_Default_Transp.ROW_GROUP2;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := q'[{'displayName':']'||r_Get_Las_Link_Default_Transp.ROW_GROUP2||q'[','value':']'||r_Get_Las_Link_Default_Transp.ROW_GROUP2_ID||q'[','hiddenColumns':']'|| p_hidden_subtest_id || q'[','disag':']'|| p_disag ||q'[','transpose':']'|| p_transpose || q'[', 'rowgroup':']' || p_rowgroup ||q'[','contextMenu':]'||p_context_menu||'}';
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Get_Las_Link_Default_Transp.ROW_GROUP2_SEQ;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Get_Las_Link_Default_Transp.SCORE_TYPE;
          t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Get_Las_Link_Default_Transp.SCORES;
          /*t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
          t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;*/
          PIPE ROW (t_PRS_PGT_GLOBAL_TEMP_OBJ);

      END LOOP;
    END IF;

     END IF;



--PIPE ROW (t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ);
RETURN;-- t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
  RAISE;
   -- RETURN NULL;
END SF_LAS_LINK_POC_5;
/
