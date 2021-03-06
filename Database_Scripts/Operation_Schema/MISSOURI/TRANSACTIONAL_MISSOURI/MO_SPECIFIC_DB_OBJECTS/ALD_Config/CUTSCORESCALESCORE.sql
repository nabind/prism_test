-- START >> FOR CHECKING
  --- VALUES SHOLUD BE 0,2,3,4,5
SELECT DISTINCT PL FROM SUBTEST_SCORE_FACT 
WHERE adminid = (SELECT ADMINID FROM ADMIN_DIM ADM WHERE ADM.ADMIN_YEAR = 2016)
AND cust_prod_id = (SELECT CUST.CUST_PROD_ID --5129
                   FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
                   WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
                    AND PDT.PRODUCTID = CUST.PRODUCTID 
                    AND ADM.ADMINID = CUST.ADMINID
                    AND ADM.ADMIN_YEAR = 2016
                    AND CUST.CUSTOMERID = 1061 ---CHANGE IT
                    );
                    
  --- VALUES SHOLUD BE 2,3,4,5	
SELECT DISTINCT PL FROM CUTSCORESCALESCORE 
WHERE CUST_PROD_ID = (SELECT CUST.CUST_PROD_ID --5129
						 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
						 WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
						  AND PDT.PRODUCTID = CUST.PRODUCTID 
						  AND ADM.ADMINID = CUST.ADMINID
						  AND ADM.ADMIN_YEAR = 2016
						  AND CUST.CUSTOMERID = 1061 ---CHANGE IT
              ); 

 ---CHECK THE SCORE_VALUE COLUMN IN SCORE_TYPE_LOOKUP IT SHOLD BE 0,2,3,4,5 WITH 0 = LEVEL NOT DETERMINED
SELECT SKLP.* 
 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST,SCORE_TYPE_LOOKUP SKLP
 WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
  AND PDT.PRODUCTID = CUST.PRODUCTID 
  AND ADM.ADMINID = CUST.ADMINID
  AND ADM.ADMIN_YEAR = 2016
  AND CUST.CUSTOMERID = 1061 ---CHANGE IT
  AND SKLP.CUST_PROD_ID = CUST.CUST_PROD_ID ;
 
 --IF FROM THE ABOVE QUERY THE VALUES ARE 1,2,3,4 IN CUTSCORESCALESCORE FROM THE 2nd QUERY
 -- AND IS NOT MATCHING WITH SCORE_TYPE_LOOKUP WHICH IS SHOWING 0,2,3,4,5,  IN THE 3rd QUERY
 -- RUN THE BELOW UPADTE QUERY TO UPDATE THE CUTSCORESCALESCORE PL COLUMN THE 
 -- THE PL VALUES IN BOTH THE TBALES AND FILE SHOULD BE SAME.
 
 UPDATE CUTSCORESCALESCORE SET PL = PL+1 WHERE CUST_PROD_ID = (SELECT CUST.CUST_PROD_ID --5129
                                                                 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
                                                                 WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
                                                                  AND PDT.PRODUCTID = CUST.PRODUCTID 
                                                                  AND ADM.ADMINID = CUST.ADMINID
                                                                  AND ADM.ADMIN_YEAR = 2016
                                                                  AND CUST.CUSTOMERID = 1061 ---CHANGE IT
                                                                  );
 ---56 ROWS WILL BE UPDATED
 COMMIT;
 
 --COMPILE THE PROCEDURE PRC_CUTSCORE_UPDATE_2016 (KEPT COMMENTED AT THE END OF THIS SCRIPT)
 
 
-- END >> FOR CHECKING

CREATE TABLE STG_CUTSCORESCALESCORE 
(
 itGrade VARCHAR2(30),	
 itContArea VARCHAR2(30),	
 PerformanceLevel VARCHAR2(30),	
 LevelDescription_PLD VARCHAR2(30), 
 Level_Pl_Lower_Score NUMBER,	
 Level_PL_Upper_Score NUMBER,	
 Loss NUMBER,	
 Hoss NUMBER
);

INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','ELA','2','Below Basic',230,415,230,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','ELA','3','Basic',416,446,230,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','ELA','4','Proficient',447,501,230,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','ELA','5','Advanced',502,730,230,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','ELA','2','Below Basic',240,435,240,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','ELA','3','Basic',436,472,240,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','ELA','4','Proficient',473,525,240,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','ELA','5','Advanced',526,740,240,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','ELA','2','Below Basic',250,448,250,780);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','ELA','3','Basic',449,487,250,780);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','ELA','4','Proficient',488,540,250,780);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','ELA','5','Advanced',541,780,250,780);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','ELA','2','Below Basic',260,467,260,790);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','ELA','3','Basic',468,498,260,790);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','ELA','4','Proficient',499,549,260,790);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','ELA','5','Advanced',550,790,260,790);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','ELA','2','Below Basic',280,475,280,810);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','ELA','3','Basic',476,505,280,810);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','ELA','4','Proficient',506,562,280,810);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','ELA','5','Advanced',563,810,280,810);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','ELA','2','Below Basic',290,485,290,820);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','ELA','3','Basic',486,517,290,820);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','ELA','4','Proficient',518,569,290,820);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','ELA','5','Advanced',570,820,290,820);

INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','MATH','2','Below Basic',290,414,290,650);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','MATH','3','Basic',415,456,290,650);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','MATH','4','Proficient',457,494,290,650);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('3','MATH','5','Advanced',495,650,290,650);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','MATH','2','Below Basic',320,437,320,680);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','MATH','3','Basic',438,485,320,680);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','MATH','4','Proficient',486,520,320,680);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('4','MATH','5','Advanced',521,680,320,680);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','MATH','2','Below Basic',340,462,340,710);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','MATH','3','Basic',463,507,340,710);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','MATH','4','Proficient',508,543,340,710);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('5','MATH','5','Advanced',544,710,340,710);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','MATH','2','Below Basic',350,469,350,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','MATH','3','Basic',470,517,350,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','MATH','4','Proficient',518,554,350,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('6','MATH','5','Advanced',555,730,350,730);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','MATH','2','Below Basic',360,481,360,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','MATH','3','Basic',482,527,360,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','MATH','4','Proficient',528,563,360,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('7','MATH','5','Advanced',564,740,360,740);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','MATH','2','Below Basic',390,495,390,770);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','MATH','3','Basic',496,543,390,770);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','MATH','4','Proficient',544,571,390,770);
INSERT INTO STG_CUTSCORESCALESCORE VALUES ('8','MATH','5','Advanced',572,770,390,770);

COMMIT;

CREATE TABLE CUTSCORESCALESCORE_BKP AS 
SELECT CUT.CUTSCORESCALESCOREID,
                       CUT.GRADEID,
                       CUT.LEVELID,
                       CUT.SUBTESTID,
                       CUT.CUST_PROD_ID,
                       CUT.LOSS,
                       CUT.HOSS,
                       CUT.PASS,
                       CUT.PASSPLUS,
                       CUT.PL
                  FROM LEVEL_MAP LM, 
                       ASSESSMENT_DIM ASSES, 
                       PRODUCT PDT,
                       GRADE_LEVEL_MAP GLM,
                       GRADE_DIM GRD,
                       CUTSCORESCALESCORE CUT,
                       CONTENT_DIM CONT,
                       SUBTEST_DIM SUB,
					   CUST_PRODUCT_LINK CUST,
					   ADMIN_DIM ADM
                  WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016' 
                    AND PDT.PRODUCTID = ASSES.PRODUCTID
                    AND LM.ASSESSMENTID = ASSES.ASSESSMENTID
                    AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
                    AND GLM.GRADEID = GRD.GRADEID
                    AND CUT.GRADEID = GRD.GRADEID
                    AND CUT.LEVELID = LM.LEVELID
                    AND CUT.CUST_PROD_ID  = CUST.CUST_PROD_ID
					AND CUST.PRODUCTID = PDT.PRODUCTID
					AND CUST.ADMINID = ADM.ADMINID
					AND CUST.CUSTOMERID = 1061 ---CHANGE IT
					AND ADM.ADMIN_YEAR = 2016 
                    AND CONT.ASSESSMENTID = ASSES.ASSESSMENTID
                    AND SUB.CONTENTID = CONT.CONTENTID
                    AND SUB.SUBTESTID = CUT.SUBTESTID
                    AND SUB.SUBTEST_CODE IN ('01','02')
                    ORDER BY CUT.GRADEID,
                             CUT.LEVELID,
                             CUT.SUBTESTID,
                             CUT.PL;

	---48 ROWS SHOULD BE UPDATED						 
BEGIN
  PRC_CUTSCORE_UPDATE_2016 (P_CUSTOMERID=>1061 ---CHANGE IT
                            );
END;							 
		
 ----TO REFRESH THE MV
DECLARE
V_CUSTOMERID NUMBER := 1061; ---CHANGE IT
V_PRODUCT_CODE VARCHAR2(20):= 'MAPSMTSPR2016';
V_OUT_STATUS VARCHAR2(20);
V_CUSTOMER_CODE VARCHAR2(10);
BEGIN
  SELECT CUSTOMER_CODE INTO V_CUSTOMER_CODE FROM CUSTOMER_INFO WHERE CUSTOMERID = V_CUSTOMERID;
  MO_MVIEW_REFRESH_MANUAL(P_IN_CUSTOMER_CODE =>V_CUSTOMER_CODE,
                          P_IN_PRODUCT_CODE =>V_PRODUCT_CODE,
                          OUT_STATUS   => V_OUT_STATUS);
  DBMS_OUTPUT.PUT_LINE(V_OUT_STATUS);                        
END; 

---FOR CHECKING
SELECT * FROM CUTSCORESCALESCORE 
WHERE CUST_PROD_ID = (SELECT CUST.CUST_PROD_ID --5129
						 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
						 WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
						  AND PDT.PRODUCTID = CUST.PRODUCTID 
						  AND ADM.ADMINID = CUST.ADMINID
						  AND ADM.ADMIN_YEAR = 2016
						  AND CUST.CUSTOMERID = 1061 ---CHANGE IT
              ); 
              
SELECT * FROM MV_PROF_LEVEL_SCORE_RANGE 
WHERE CUST_PROD_ID = (SELECT CUST.CUST_PROD_ID --5129
						 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
						 WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
						  AND PDT.PRODUCTID = CUST.PRODUCTID 
						  AND ADM.ADMINID = CUST.ADMINID
						  AND ADM.ADMIN_YEAR = 2016
						  AND CUST.CUSTOMERID = 1061 ---CHANGE IT
              );


SELECT * FROM MV_RPRT_STUD_DETAILS 
WHERE CUST_PROD_ID = (SELECT CUST.CUST_PROD_ID --5129
						 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
						 WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
						  AND PDT.PRODUCTID = CUST.PRODUCTID 
						  AND ADM.ADMINID = CUST.ADMINID
						  AND ADM.ADMIN_YEAR = 2016
						  AND CUST.CUSTOMERID = 1061 ---CHANGE IT
              )
  ORDER BY DISTRICT_CODE,DISTRICT_NAME,
            SCHOOL_CODE,SCHOOL_NAME,
            GRADEID,SUBTEST_SEQ,
            LAST_NAME,FIRST_NAME,MIDDLE_NAME;
	
---TO FIND THE FILE PATH IN S3 			
  SELECT * FROM CUSTOMER_INFO WHERE CUSTOMERID = 1061; -- /MAPREPORTS/MISSOURI
  SELECT * FROM PRODUCT -- /MAPSMTSPR2016
---ENTIRE PATH 
/MAPREPORTS/MISSOURI/MAPSMTSPR2016/ISR/<DISTRICT_CODE>/<SCHOOL_CODE>/MAP_ISR_<DISTRICT_CODE>_<SCHOOL_CODE>_<GRADE_CODE>_<LAST_NAME>_<MOSISID>_<STUDENT_BIO_ID>_<SUBTESTID>.PDF
  
			 
/*SELECT 1 AS CUTSCORESCALESCOREID,
       GLM.GRADEID,
       LM.LEVELID,
       SUB.SUBTESTID,
       1 AS CUST_PROD_ID,
       SCUT.LOSS,
       SCUT.HOSS,
       SCUT.LEVEL_PL_LOWER_SCORE AS PASS,
       SCUT.LEVEL_PL_UPPER_SCORE AS PASSPLUS,
       SCUT.PERFORMANCELEVEL AS PL,
       SYSDATE AS CREATED_DATE_TIME,
       SYSDATE AS UPDATED_DATE_TIME
FROM LEVEL_MAP LM, 
     ASSESSMENT_DIM ASSES, 
     PRODUCT PDT,
     GRADE_LEVEL_MAP GLM,
     GRADE_DIM GRD,
     STG_CUTSCORESCALESCORE SCUT,
     CONTENT_DIM CONT,
     SUBTEST_DIM SUB
WHERE PDT.PRODUCT_CODE = 'MAPSMTSPR2016'
  AND PDT.PRODUCTID = ASSES.PRODUCTID
  AND LM.ASSESSMENTID = ASSES.ASSESSMENTID
  AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
  AND GLM.GRADEID = GRD.GRADEID
  AND GRD.GRADE_CODE = '0'||SCUT.ITGRADE
  AND CONT.ASSESSMENTID = ASSES.ASSESSMENTID
  AND SUB.CONTENTID = CONT.CONTENTID
  AND SUB.SUBTEST_CODE =  DECODE (SCUT.ITCONTAREA,'ELA','01','MATH','02',SCUT.ITCONTAREA) ;*/
 
/*

CREATE OR REPLACE PROCEDURE PRC_CUTSCORE_UPDATE_2016(P_CUSTOMERID NUMBER) IS
 
V_PRODUCTID NUMBER;
V_CUST_PROD_ID NUMBER;
V_PRODUCT_CODE VARCHAR2(20) := 'MAPSMTSPR2016';
V_ADMIN_YEAR NUMBER := 2016;
V_COUNTER NUMBER;

BEGIN
  
 SELECT CUST_PROD_ID INTO V_CUST_PROD_ID 
 FROM PRODUCT PDT, ADMIN_DIM ADM, CUST_PRODUCT_LINK CUST
 WHERE PDT.PRODUCT_CODE = V_PRODUCT_CODE
  AND PDT.PRODUCTID = CUST.PRODUCTID 
  AND ADM.ADMINID = CUST.ADMINID
  AND ADM.ADMIN_YEAR = V_ADMIN_YEAR
  AND CUST.CUSTOMERID = P_CUSTOMERID;
  
 V_COUNTER := 0; 
 FOR R_EXISTS IN (SELECT CUT.CUTSCORESCALESCOREID,
                       CUT.GRADEID,
                       CUT.LEVELID,
                       CUT.SUBTESTID,
                       CUT.CUST_PROD_ID,
                       CUT.LOSS,
                       CUT.HOSS,
                       CUT.PASS,
                       CUT.PASSPLUS,
                       CUT.PL
                  FROM LEVEL_MAP LM, 
                       ASSESSMENT_DIM ASSES, 
                       PRODUCT PDT,
                       GRADE_LEVEL_MAP GLM,
                       GRADE_DIM GRD,
                       CUTSCORESCALESCORE CUT,
                       CONTENT_DIM CONT,
                       SUBTEST_DIM SUB
                  WHERE PDT.PRODUCT_CODE = V_PRODUCT_CODE
                    AND PDT.PRODUCTID = ASSES.PRODUCTID
                    AND LM.ASSESSMENTID = ASSES.ASSESSMENTID
                    AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
                    AND GLM.GRADEID = GRD.GRADEID
                    AND CUT.GRADEID = GRD.GRADEID
                    AND CUT.LEVELID = LM.LEVELID
                    AND CUT.CUST_PROD_ID  = V_CUST_PROD_ID
                    AND CONT.ASSESSMENTID = ASSES.ASSESSMENTID
                    AND SUB.CONTENTID = CONT.CONTENTID
                    AND SUB.SUBTESTID = CUT.SUBTESTID
                    AND SUB.SUBTEST_CODE IN ('01','02')
                    ORDER BY CUT.GRADEID,
                             CUT.LEVELID,
                             CUT.SUBTESTID,
                             CUT.PL) 
  
  LOOP
   
   FOR R_UPDATE IN (SELECT GLM.GRADEID,
                           LM.LEVELID,
                           SUB.SUBTESTID,
                           SCUT.LOSS,
                           SCUT.HOSS,
                           SCUT.LEVEL_PL_LOWER_SCORE AS PASS,
                           SCUT.LEVEL_PL_UPPER_SCORE AS PASSPLUS,
                           SCUT.PERFORMANCELEVEL AS PL
                    FROM LEVEL_MAP LM, 
                         ASSESSMENT_DIM ASSES, 
                         PRODUCT PDT,
                         GRADE_LEVEL_MAP GLM,
                         GRADE_DIM GRD,
                         STG_CUTSCORESCALESCORE SCUT,
                         CONTENT_DIM CONT,
                         SUBTEST_DIM SUB
                    WHERE PDT.PRODUCT_CODE = V_PRODUCT_CODE
                      AND PDT.PRODUCTID = ASSES.PRODUCTID
                      AND LM.ASSESSMENTID = ASSES.ASSESSMENTID
                      AND GLM.LEVEL_MAPID = LM.LEVEL_MAPID
                      AND GLM.GRADEID = GRD.GRADEID
                      AND GRD.GRADE_CODE = '0'||SCUT.ITGRADE
                      AND CONT.ASSESSMENTID = ASSES.ASSESSMENTID
                      AND SUB.CONTENTID = CONT.CONTENTID
                      AND SUB.SUBTEST_CODE =  DECODE (SCUT.ITCONTAREA,'ELA','01','MATH','02',SCUT.ITCONTAREA)
                      AND GRD.GRADEID = R_EXISTS.GRADEID
                      AND LM.LEVELID = R_EXISTS.LEVELID
                      AND SUB.SUBTESTID = R_EXISTS.SUBTESTID
                      AND TRIM(SCUT.LEVELDESCRIPTION_PLD )= (SELECT TRIM(SCORE_VALUE_NAME)
                                                              FROM SCORE_TYPE_LOOKUP 
                                                              WHERE CUST_PROD_ID = R_EXISTS.CUST_PROD_ID
                                                                AND SCORE_VALUE = R_EXISTS.PL))
      LOOP
        V_COUNTER := V_COUNTER +1;
        UPDATE CUTSCORESCALESCORE SET LOSS = R_UPDATE.LOSS, 
                                      HOSS = R_UPDATE.HOSS, 
                                      PASS = R_UPDATE.PASS,
                                      PASSPLUS = R_UPDATE.PASSPLUS,
                                      PL = R_UPDATE.PL,
                                      UPDATED_DATE_TIME = SYSDATE
                                WHERE CUTSCORESCALESCOREID = R_EXISTS.CUTSCORESCALESCOREID;       
                                      
      END LOOP;                
  
   END LOOP;
  
  COMMIT;
    
  
  IF V_COUNTER>0 THEN
    DBMS_OUTPUT.PUT_LINE(V_COUNTER || ' ROWS IN CUTSCORESCALESCORE UPDATED');
    
    DBMS_MVIEW.REFRESH('MV_PROF_LEVEL_SCORE_RANGE','C');
      
    DBMS_OUTPUT.PUT_LINE('MV_PROF_LEVEL_SCORE_RANGE REFRESHED');
  END IF;
   
    
  
END PRC_CUTSCORE_UPDATE_2016;

*/

