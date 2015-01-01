CREATE OR REPLACE FUNCTION SF_PPR_MAIN (       p_test_administration IN VARCHAR,
                                               p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE ,
                                               --p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                               p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                               p_class IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                               p_grade_ppr IN GRADE_DIM.GRADEID%TYPE,
                                               p_test_program IN VARCHAR)
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_PPR_MAIN
  * PURPOSE:   main query Of PRofciency roster
  * CREATED:   TCS  16/DEC/2013
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR     DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();


 CURSOR c_Prof_Main
  IS SELECT FINL.* ,
       REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 1) AS MSG_TYP_1,
       REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 2) AS MSG_TYP_2,
       REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 3) AS MSG_TYP_3,
       REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 4) AS MSG_TYP_4,
       REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 5) AS MSG_TYP_5,
	   REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 6) AS MSG_TYP_6,
	   REGEXP_SUBSTR(FINL.MSG, '[^,]+', 1, 7) AS MSG_TYP_7,
	   REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 1) AS MSG_1,
       REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 2) AS MSG_2,
       REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 3) AS MSG_3,
       REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 4) AS MSG_4,
       REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 5) AS MSG_5,
       REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 6) AS MSG_6,
	   REGEXP_SUBSTR(FINL.MESG, '[^|]+', 1, 7) AS MSG_7,	   
       REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 1) AS MSG_NAME_1, 
       REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 2) AS MSG_NAME_2, 
       REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 3) AS MSG_NAME_3, 
       REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 4) AS MSG_NAME_4, 
       REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 5) AS MSG_NAME_5, 
       REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 6) AS MSG_NAME_6,
	   REGEXP_SUBSTR(FINL.MSG_NAME, '[^,]+', 1, 7) AS MSG_NAME_7
   FROM
     (SELECT COUNT(DISTINCT GRT.STUDENT_BIO_ID) AS CNT,
             CASE WHEN INSTR(PDT.PRODUCT_CODE,'ISTEP',1) =1 THEN 1 ELSE 0 END AS PRODUCT_CODE,
             CASE WHEN INSTR(PDT.PRODUCT_CODE,'IREAD',1) =1 THEN 1 ELSE 0 END AS IS_IREAD,
             CUST.CUST_PROD_ID ,
             CUST.PRODUCTID ,
             SUB_CNT.SUBTEST_1 ,
             SUB_CNT.SUBTEST_2 ,
             SUB_CNT.SUBTEST_3 ,
             SUB_CNT.SUBTEST_4 ,
             SUB_CNT.SUBCNT,
			 MSG_TYP.MSG,
                MSG_TYP.MESG, 
                MSG_TYP.MSG_NAME

            FROM RESULTS_GRT_FACT GRT,
               ORG_LSTNODE_LINK OLNK,
               CUST_PRODUCT_LINK CUST,
               --ORG_PRODUCT_LINK OPLK,
                --ORG_TEST_PROGRAM_LINK OTPLK,
                --TEST_PROGRAM TP,
               PRODUCT PDT,
               (SELECT F.ASSESSMENTID ,
                 REGEXP_SUBSTR(F.SUBTEST_NAME, '[^,]+', 1, 1) AS SUBTEST_1,
                 REGEXP_SUBSTR(F.SUBTEST_NAME, '[^,]+', 1, 2) AS SUBTEST_2,
                 REGEXP_SUBSTR(F.SUBTEST_NAME, '[^,]+', 1, 3) AS SUBTEST_3,
                 REGEXP_SUBSTR(F.SUBTEST_NAME, '[^,]+', 1, 4) AS SUBTEST_4,
                 F.SUBCNT
                  FROM
                  (
                  SELECT  A.ASSESSMENTID,
                          LISTAGG(A.SUBTEST_NAME, ',') WITHIN GROUP(ORDER BY A.SUBTEST_SEQ) AS SUBTEST_NAME,
                          A.SUBCNT
                   FROM
                    (SELECT DISTINCT  V.ASSESSMENTID,V.SUBTESTID ,V.SUBTEST_NAME,V.SUBTEST_SEQ ,
                                 COUNT(DISTINCT V.SUBTESTID) OVER (PARTITION BY V.ASSESSMENTID) AS SUBCNT
                                FROM VW_SUBTEST_GRADE_OBJECTIVE_MAP V,ASSESSMENT_DIM ASES
                                WHERE GRADEID = p_grade_ppr
                                AND ASES.ASSESSMENTID = V.ASSESSMENTID
                                AND ASES.PRODUCTID =  p_test_administration
                                ORDER BY 4) A GROUP BY A.ASSESSMENTID,A.SUBCNT)F
                  WHERE ROWNUM = 1) SUB_CNT,
				  (SELECT LISTAGG(TYP.MESSAGE_TYPE, ',') WITHIN
                                          GROUP(
                                          ORDER BY TYP.CUST_PROD_ID,TYP.MSG_TYPEID) AS MSG,
                                          LISTAGG( REPORT_MSG, '|') WITHIN
                                          GROUP(
                                          ORDER BY TYP.CUST_PROD_ID,TYP.MSG_TYPEID) AS MESG, 
                                          LISTAGG( TYP.MESSAGE_NAME, ',') WITHIN 
                                          GROUP( 
                                          ORDER BY TYP.CUST_PROD_ID,TYP.MSG_TYPEID) AS MSG_NAME 
                                           FROM DASH_MESSAGES     MSG,
                                                DASH_MESSAGE_TYPE TYP,
                                                CUST_PRODUCT_LINK CUST1
                                          WHERE MSG.DB_REPORTID = 1000
                                            AND CUST1.PRODUCTID = p_test_administration
                                            AND TYP.CUST_PROD_ID = CUST1.CUST_PROD_ID
                                            AND CUST1.CUSTOMERID = p_customerid
                                            AND MSG.MSG_TYPEID = TYP.MSG_TYPEID
                                            AND MSG.DB_REPORTID = 1000
                                            AND MSG.CUST_PROD_ID = CUST1.CUST_PROD_ID
                                            AND MSG.ACTIVATION_STATUS = 'AC'
                                            AND TYP.MESSAGE_TYPE IN ('EN','DM','RM','FN','RP','RL','RPN')
                                           )  MSG_TYP
          WHERE GRT.CUST_PROD_ID = CUST.CUST_PROD_ID
            AND CUST.CUSTOMERID = p_customerid
            AND CUST.PRODUCTID =  p_test_administration
            AND CUST.ADMINID = GRT.ADMINID
            AND GRT.ORG_NODEID= OLNK.ORG_LSTNODEID
            AND GRT.CUST_PROD_ID = CUST.CUST_PROD_ID
            --AND OPLK.ORG_NODEID= OLNK.ORG_LSTNODEID
            --AND OTPLK.ORG_NODEID=OLNK.ORG_NODEID
            --AND TP.TP_ID = OTPLK.TP_ID
            --AND TP.TP_CODE = GRT.ORGTSTGPGM
            --AND TP.TP_TYPE = DECODE(p_test_program,0,'NON-PUBLIC','PUBLIC')
            AND GRT.ISPUBLIC = p_test_program
            AND GRT.GRADEID = p_grade_ppr
            AND GRT.ORGTSTGPGM NOT IN (SELECT TP.TP_CODE FROM TEST_PROGRAM TP WHERE TP.TP_NAME LIKE('%Braille') AND TP.CUSTOMERID = p_customerid AND TP.ADMINID = CUST.ADMINID)
            AND (
              (p_class<>-1 AND OLNK.ORG_NODEID=p_class)
              OR (p_class=-1 AND (p_school<> -2 OR p_school <> -99 ) AND OLNK.ORG_NODEID=p_school)
              )
            AND CUST.PRODUCTID=PDT.PRODUCTID
            GROUP BY PDT.PRODUCT_CODE,
                     CUST.CUST_PROD_ID ,
                     CUST.PRODUCTID ,
                     SUB_CNT.SUBTEST_1 ,
                     SUB_CNT.SUBTEST_2 ,
                     SUB_CNT.SUBTEST_3 ,
                     SUB_CNT.SUBTEST_4 ,
                     SUB_CNT.SUBCNT,
					 MSG_TYP.MSG,
					MSG_TYP.MESG, 
					MSG_TYP.MSG_NAME
          
          UNION ALL
          
          SELECT -1 AS CNT,
                 NULL AS PRODUCT_CODE,
                 NULL AS IS_IREAD,
                  MSG_TYP.CUST_PROD_ID AS CUST_PROD_ID,
                  MSG_TYP.PRODUCTID AS PRODUCTID,
                 NULL AS SUBTEST_1,
                 NULL AS SUBTEST_2,
                 NULL AS SUBTEST_3,
                 NULL AS SUBTEST_4,
                 NULL AS SUBCNT,
                MSG_TYP.MSG AS MSG,
                MSG_TYP.MESG AS MESG, 
					      MSG_TYP.MSG_NAME AS MSG_NAME
          FROM  (SELECT LISTAGG(TYP.MESSAGE_TYPE, ',') WITHIN
                                          GROUP(
                                          ORDER BY TYP.CUST_PROD_ID,TYP.MSG_TYPEID) AS MSG,
                                          LISTAGG( REPORT_MSG, '|') WITHIN
                                          GROUP(
                                          ORDER BY TYP.CUST_PROD_ID,TYP.MSG_TYPEID) AS MESG, 
                                          LISTAGG( TYP.MESSAGE_NAME, ',') WITHIN 
                                          GROUP( 
                                          ORDER BY TYP.CUST_PROD_ID,TYP.MSG_TYPEID) AS MSG_NAME ,
                                          CUST1.PRODUCTID AS PRODUCTID,
		                                      CUST1.CUST_PROD_ID AS CUST_PROD_ID
                                           FROM DASH_MESSAGES     MSG,
                                                DASH_MESSAGE_TYPE TYP,
                                                CUST_PRODUCT_LINK CUST1
                                          WHERE MSG.DB_REPORTID = 1000
                                            AND CUST1.PRODUCTID = p_test_administration
                                            AND TYP.CUST_PROD_ID = CUST1.CUST_PROD_ID
                                            AND CUST1.CUSTOMERID = p_customerid
                                            AND MSG.MSG_TYPEID = TYP.MSG_TYPEID
                                            AND MSG.DB_REPORTID = 1000
                                            AND MSG.CUST_PROD_ID = CUST1.CUST_PROD_ID
                                            AND MSG.ACTIVATION_STATUS = 'AC'
                                            AND TYP.MESSAGE_TYPE IN ('EN','DM','RM','FN','RP','RL','RPN')
                                            GROUP BY  CUST1.PRODUCTID,
                                                      CUST1.CUST_PROD_ID 
                                           )MSG_TYP
           UNION ALL                               
                                           
           SELECT -1 AS CNT,
                 NULL AS PRODUCT_CODE,
                 NULL AS IS_IREAD,
                 NULL AS CUST_PROD_ID,
                 NULL AS PRODUCTID,
                 NULL AS SUBTEST_1,
                 NULL AS SUBTEST_2,
                 NULL AS SUBTEST_3,
                 NULL AS SUBTEST_4,
                 NULL AS SUBCNT,
                 NULL AS MSG,
                 NULL AS MESG, 
					       NULL AS MSG_NAME
          FROM DUAL                               
          ORDER BY 1  DESC   )FINL
          WHERE ROWNUM = 1  ;        



BEGIN
       
         FOR r_Prof_Main IN c_Prof_Main
                LOOP
                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Prof_Main.CNT;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Prof_Main.PRODUCT_CODE;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Prof_Main.IS_IREAD;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4 := r_Prof_Main.CUST_PROD_ID;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5 := r_Prof_Main.PRODUCTID;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6 := r_Prof_Main.SUBTEST_1;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7 := r_Prof_Main.SUBTEST_2;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc8 := r_Prof_Main.SUBTEST_3;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc9 := r_Prof_Main.SUBTEST_4;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc10 := r_Prof_Main.SUBCNT;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Prof_Main.MSG;
                -- t_PRS_PGT_GLOBAL_TEMP_OBJ.vc11 := r_Prof_Main.MESG;
               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc12 := r_Prof_Main.MSG_NAME;                 
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc13 := r_Prof_Main.MSG_TYP_1;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc14 := r_Prof_Main.MSG_TYP_2;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc15 := r_Prof_Main.MSG_TYP_3;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc16 := r_Prof_Main.MSG_TYP_4;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc17 := r_Prof_Main.MSG_TYP_5;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc18 := r_Prof_Main.MSG_TYP_6;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc19 := r_Prof_Main.MSG_TYP_7;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1_CLOB := r_Prof_Main.MSG_1;
                  t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2_CLOB := r_Prof_Main.MSG_2;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3_CLOB := r_Prof_Main.MSG_3;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc4_CLOB := r_Prof_Main.MSG_4;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc5_CLOB := r_Prof_Main.MSG_5;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc6_CLOB := r_Prof_Main.MSG_6;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc7_CLOB := r_Prof_Main.MSG_7;
                t_PRS_PGT_GLOBAL_TEMP_OBJ.vc27 := r_Prof_Main.MSG_NAME_1;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc28 := r_Prof_Main.MSG_NAME_2;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc29 := r_Prof_Main.MSG_NAME_3;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc30 := r_Prof_Main.MSG_NAME_4;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc31 := r_Prof_Main.MSG_NAME_5;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc32 := r_Prof_Main.MSG_NAME_6;
                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc33 := r_Prof_Main.MSG_NAME_7;
                


                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       END LOOP;
      


RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
 /* dbms_output.put_line('ERROR_STACK: ' || DBMS_UTILITY.FORMAT_ERROR_STACK);
  dbms_output.put_line('ERROR_BACKTRACE: ' || DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
   RAISE ;*/
    RETURN NULL;
END SF_PPR_MAIN;
/
