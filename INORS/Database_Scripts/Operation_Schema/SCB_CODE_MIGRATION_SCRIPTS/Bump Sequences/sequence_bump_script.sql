/*DECLARE
  difference         INTEGER;
  sqlstmt            VARCHAR2(255) ;
  sqlstmt2           VARCHAR2(255) ;
  sqlstmt3           VARCHAR2(255) ;
  sequenceValue      NUMBER;
  sequencename       VARCHAR2(30) ;
  sequencelastnumber INTEGER;
  CURSOR allseq
  IS
     SELECT sequence_name, last_number FROM user_sequences ORDER BY sequence_name;
BEGIN
  DBMS_OUTPUT.enable(32000) ;
  OPEN allseq;
  LOOP
    FETCH allseq INTO sequencename, sequencelastnumber;
    EXIT
  WHEN allseq%NOTFOUND;
    sqlstmt  := 'ALTER SEQUENCE ' || sequencename || ' INCREMENT BY ';
    --Assuming: <tablename>_id is <sequencename>
    sqlstmt2 := 'select (nvl(Max(ID),0) - :1)+1 from ' || SUBSTR(sequencename, 1, LENGTH(sequencename) - 3) ;
    --DBMS_OUTPUT.PUT_LINE(sqlstmt2);
    --Attention: makes use of user_sequences.last_number --> possible cache problems!
    EXECUTE IMMEDIATE sqlstmt2 INTO difference USING sequencelastnumber;
    IF difference > 0 THEN
      DBMS_OUTPUT.PUT_LINE('EXECUTE IMMEDIATE ' || sqlstmt || difference) ;
      EXECUTE IMMEDIATE sqlstmt || difference;
      sqlstmt3 := 'SELECT ' || sequencename ||'.NEXTVAL from dual';
      DBMS_OUTPUT.PUT_LINE('EXECUTE IMMEDIATE ' || sqlstmt3 || ' INTO sequenceValue') ;
      EXECUTE IMMEDIATE sqlstmt3 INTO sequenceValue;
      DBMS_OUTPUT.PUT_LINE('EXECUTE IMMEDIATE ' || sqlstmt || 1) ;
      EXECUTE IMMEDIATE sqlstmt || 1;
      DBMS_OUTPUT.PUT_LINE('') ;
    END IF;
  END LOOP;
  CLOSE allseq;
END; 
*/



declare 
v_column_name varchar2(300); 
v_max_column_value NUMBER := NULL; 
begin 
for i in  (
      SELECT * FROM global_seq_bump  WHERE sequence_owner='TASC_POC' ORDER BY sequence_name) 
LOOP

--dbms_output.put_line( I.TABLE_NAME);
BEGIN 
    SELECT A.column_name
   into v_column_name 
                FROM USER_TAB_COLS     A,
                     USER_CONS_COLUMNS B,
                     USER_CONSTRAINTS  C
               WHERE A.TABLE_NAME = I.TABLE_NAME
                 AND A.TABLE_NAME = B.TABLE_NAME
                 AND A.COLUMN_NAME = B.COLUMN_NAME
                 AND B.TABLE_NAME = C.TABLE_NAME
                 AND B.CONSTRAINT_NAME = C.CONSTRAINT_NAME
                 AND C.CONSTRAINT_TYPE = 'P'
                 AND A.COLUMN_NAME NOT LIKE '%CUST_PROD_ID%' ; 
  execute immediate 'select max('||v_column_name||') from '||i.TABLE_NAME
    into v_max_column_value ; 

  
    if   v_max_column_value = i.last_number then 
    dbms_output.put_line('No Need to Bump Up table: '||I.TABLE_NAME); 
    else 
      dbms_output.put_line('  Need to Bump Up table: '||I.TABLE_NAME||' By '||(nvl(v_max_column_value,0) - i.last_number ));
      

     end if ; 
    -- dbms_output.put_line('  Need to Bump Up table: '||I.TABLE_NAME||' By '||nvl(v_max_column_value,0) - i.last_number );
     EXCEPTION
WHEN OTHERS THEN 
dbms_output.put_line(i.sequence_name||'--'|| SQLERRM || '---'|| dbms_utility.format_error_backtrace);  
END;
 
end loop ;   


end ; 

-- Global Sequences --- 
  Need to Bump Up table: DASH_MESSAGE_TYPE By -381
  Need to Bump Up table: DASH_REPORTS By -3
  Need to Bump Up table: NP_MEAN_NCE_LOOKUP By -4
  Need to Bump Up table: DASH_MESSAGES By -381
Issue with table: DASH_ACTION_ACCESS
  Need to Bump Up table: DASH_ACTION_ACCESS By 
  Need to Bump Up table: DASH_CONTRACT_PROP By -18
  Need to Bump Up table: DASH_RPT_ACTION By -7
  Need to Bump Up table: SCORE_TYPE_LOOKUP By -16 
 
--TASC Sequences --  
  Need to Bump Up table: USER_ACTIVITY_HISTORY By -4
  Need to Bump Up table: ARTICLE_CONTENT By -17394
  Need to Bump Up table: ARTICLE_METADATA By -25431
Issue with table:  NULL
  Need to Bump Up table:  NULL By -22944600
  Need to Bump Up table: JOB_TRACKING By -3
Issue with table:  NULL
  Need to Bump Up table:  NULL By -1860
  Need to Bump Up table: ORG_USERS By -4074078 --Need to changes 
Issue with table: PERF_LOG
  Need to Bump Up table: PERF_LOG By -2024300
Issue with table: PWD_HINT_ANSWERS
  Need to Bump Up table: PWD_HINT_ANSWERS By -5
  Need to Bump Up table: DEMOGRAPHIC By -7
Issue with table: DEMOGRAPHIC_VALUES
  Need to Bump Up table: DEMOGRAPHIC_VALUES By -10
  Need to Bump Up table: STG_HIER_PROCESS_STATUS By -20
  Need to Bump Up table: STG_PROCESS_STATUS By -7
  Need to Bump Up table: STUDENTDATA_EXTRACT By -2921
  Need to Bump Up table: USERS By -16
  
  -- ISTEP
  
    Need to Bump Up table: ACAD_STD_SUMM_FACT By 6597713
  Need to Bump Up table: USER_ACTIVITY_HISTORY By 20837968
  Need to Bump Up table: ARTICLE_CONTENT By -15825
  Need to Bump Up table: ARTICLE_METADATA By -21474
  Need to Bump Up table: INVITATION_CODE_CLAIM By -7546629
Issue with table: CATEGORY_SEQ
  Need to Bump Up table: CATEGORY_SEQ By -1860
  Need to Bump Up table: OBJECTIVE_SCORE_FACT By 50246338
  Need to Bump Up table: ORG_USERS By 3569465
Issue with table: PERF_MATRIX_FACT
  Need to Bump Up table: PERF_MATRIX_FACT By -38257421
Issue with table: PWD_HINT_ANSWERS
  Need to Bump Up table: PWD_HINT_ANSWERS By 1175467
  Need to Bump Up table: INVITATION_CODE By -8979517
  Need to Bump Up table: STG_PROCESS_STATUS By -2291301
  Need to Bump Up table: OBJECTIVE_SCORE_FACT By 50246338
  Need to Bump Up table: ORG_NODE_DIM By -83345
  Need to Bump Up table: ORG_PRODUCT_LINK By -11645
  Need to Bump Up table: STATE_MEAN_IPI_SCORE By -507
  Need to Bump Up table: STUDENT_PDF_FILES By -2148976
  Need to Bump Up table: SUBTEST_SCORE_FACT By -15250867
  Need to Bump Up table: USERS By 2351476
  
  
'ACAD_STD_SUMM_FACT_SEQ'
'ACTIVITYID_SEQ'
'ARTICLE_CONTENT_SEQ'
'ARTICLE_METADATA_SEQ'
'INVITATION_CODE_CLAIM_ID_SEQ'
'METADATA_CATEGORY_SEQ'
'OBJECTIVE_FACT_SEQ'
'ORG_USER_ID_SEQ'
'PERF_MATRIX_SEQ'
'JOB_SEQ'
'PERF_LOG_SEQ'
'PWD_HINT_ANSWERS_SEQ'
'SEQ_IC_ID'
'SEQ_ISTEP_PROCESS_ID'
'SEQ_OBJ_SCORE_FACT_ID'
'SEQ_ORG_NODE_DIM'
'SEQ_ORG_PRODUCT_LINK'
'SEQ_STATE_MEAN_IPI_SCORE'
'STUDENT_PDF_SEQ'
'SUBTEST_FACT_SEQ'
'SEQ_DEMOID'
'SEQ_DEMO_VALID'
'SEQ_TASC_HIER_PROCESS_ID'
'SEQ_TASC_PROCESS_ID'
'STUD_EXTRACTID_SEQ'
'USER_ID_SEQ'

  
  
