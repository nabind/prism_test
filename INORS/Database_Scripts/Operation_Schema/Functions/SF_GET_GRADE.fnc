CREATE OR REPLACE FUNCTION SF_GET_GRADE (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN PRODUCT.PRODUCTID%TYPE
                             ,p_corpdiocese IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_school IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_class IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_program  NUMBER
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_type IN VARCHAR )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_GRADE
  * PURPOSE:   To GET different grades
  * CREATED:   TCS  14/DEC/2013
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR :353639    DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();
  --v_CustomerId CONSTANT  NUMBER :=1000;
  --v_CLASS CONSTANT  VARCHAR2(10):='CLASS';
  v_SCHOOL_OTH CONSTANT  VARCHAR2(10):='SCHOOL_OTH';
  v_ALL_SCHOOL_OTH CONSTANT  VARCHAR2(14):='ALL_SCHOOL_OTH';
  v_ALL_OPTION_TOGGLE_WITH_LEVEL CONSTANT  VARCHAR2(20):='ALL_OPTION_TOGGLE';
  v_DISTRICT_ONLY_ALL_TOGGLE CONSTANT  VARCHAR2(25):='DISTRICT_ONLY_ALL_TOGGLE';
  v_ProductId PRODUCT.PRODUCTID%TYPE;
  v_CorpId ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_SchoolId ORG_NODE_DIM.ORG_NODEID%TYPE;
  --v_ClassId ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_test_program NUMBER;
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;

 CURSOR c_Get_Grade (Product_Id PRODUCT.PRODUCTID%TYPE,org_id ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
   
     SELECT  DISTINCT  GRD.GRADEID,
       GRD.GRADE_NAME,
       GRD.GRADE_SEQ
  FROM GRADE_SELECTION_LOOKUP GSL,
       CUST_PRODUCT_LINK CUST,
       ASSESSMENT_DIM ASSD,
       GRADE_DIM GRD,
       ORG_PRODUCT_LINK OLNK
  WHERE  GSL.ADMINID = CUST.ADMINID
     AND GSL.ORG_NODEID = org_id
     AND CUST.CUSTOMERID = p_customerid
     AND CUST.CUST_PROD_ID = OLNK.CUST_PROD_ID
     AND GSL.ORG_NODEID=OLNK.ORG_NODEID 
     AND cust.productid = assd.productid 
     AND ASSD.ASSESSMENTID = GSL.ASSESSMENTID 
     AND CUST.PRODUCTID= Product_Id
     AND GRD.GRADEID = GSL.GRADEID
  ORDER BY GRD.GRADE_SEQ; 
  
  
  CURSOR c_Get_Grade_SchoolOth (Product_Id PRODUCT.PRODUCTID%TYPE,test_program1 NUMBER)
  IS
  SELECT  GRD.GRADEID,
          GRD.GRADE_NAME,
          GRD.GRADE_SEQ
  FROM GRADE_SELECTION_LOOKUP GSL,
       CUST_PRODUCT_LINK CUST,
       GRADE_DIM GRD,
       ASSESSMENT_DIM ASES
  WHERE  GSL.ADMINID = CUST.ADMINID
    AND GSL.ASSESSMENTID  =  ASES.ASSESSMENTID
    AND  ASES.PRODUCTID = CUST.PRODUCTID
    AND GSL.ORG_NODEID =  (SELECT  ORG.ORG_NODEID                             
                            FROM  ORG_NODE_DIM ORG 
                            WHERE ORG.ORG_NODE_LEVEL= 1
                              /*AND ORG_MODE = DECODE(test_program1,
                                                    -99,
                                                    'PUBLIC',
                                                    NULL,
                                                    'PUBLIC',
                                                    0,
                                                    'NON-PUBLIC',
                                                    1,
                                                    'PUBLIC')*/
                            START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId 
                            CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)
    AND CUST.CUSTOMERID = p_customerid 
    AND CUST.PRODUCTID= Product_Id
    AND GRD.GRADEID = GSL.GRADEID
    ORDER BY GRD.GRADE_SEQ  ;
    
 
  CURSOR c_Get_Grade_School_For_All (Product_Id PRODUCT.PRODUCTID%TYPE)
  IS
  SELECT  GRD.GRADEID,
          GRD.GRADE_NAME,
          GRD.GRADE_SEQ
  FROM GRADE_SELECTION_LOOKUP GSL,
       CUST_PRODUCT_LINK CUST,
       GRADE_DIM GRD,
       ASSESSMENT_DIM ASES
  WHERE  GSL.ADMINID = CUST.ADMINID
    AND GSL.ASSESSMENTID  =  ASES.ASSESSMENTID
    AND  ASES.PRODUCTID = CUST.PRODUCTID
    AND GSL.ORG_NODEID =  (SELECT  ORG.ORG_NODEID                             
                            FROM  ORG_NODE_DIM ORG 
                            WHERE ORG.ORG_NODE_LEVEL= 1
                            START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId 
                            CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)
    AND CUST.CUSTOMERID = p_customerid 
    AND CUST.PRODUCTID= Product_Id
    AND GRD.GRADEID = GSL.GRADEID
    ORDER BY GRD.GRADE_SEQ  ;   
    
    


CURSOR c_Get_Corp_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,test_program2 NUMBER)
  IS
    SELECT A.ORG_NODEID FROM
    (SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.PRODUCTID = p_ProductId
   AND OND.ORG_MODE = DECODE(test_program2,
                            -99,
                            'PUBLIC',
                            NULL,
                            'PUBLIC',
                            0,
                            'NON-PUBLIC',
                            1,
                            'PUBLIC')
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND ORPD.ORG_NODEID = OND.ORG_NODEID
   AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = LoggedInUserJasperOrgId)
        OR (v_OrgNodeLevel=2 AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
        OR (v_OrgNodeLevel <> 1 
            AND v_OrgNodeLevel <>2 
            AND OND.ORG_NODEID = (SELECT DISTINCT ORG.ORG_NODEID                             
                                FROM  ORG_NODE_DIM ORG 
                                WHERE ORG.ORG_NODE_LEVEL= 2
                                START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId 
                                CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)))
   AND OND.ORG_NODE_LEVEL = 2
  ORDER BY OND.ORG_NODE_NAME) A
   WHERE ROWNUM=1;
   
   

CURSOR c_Get_School_Default (p_ProductId PRODUCT.PRODUCTID%TYPE,p_CorpId ORG_NODE_DIM.ORG_NODEID%TYPE,test_program3 NUMBER)
  IS
  SELECT A.ORG_NODEID FROM
  (SELECT OND.ORG_NODEID, OND.ORG_NODE_NAME

  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
   WHERE CPL.PRODUCTID = p_ProductId
     AND OND.ORG_MODE = DECODE(test_program3,
                              -99,
                              'PUBLIC',
                              NULL,
                              'PUBLIC',
                              0,
                              'NON-PUBLIC',
                              1,
                              'PUBLIC')
     AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
     AND CPL.CUSTOMERID = p_customerid
     AND OND.CUSTOMERID = CPL.CUSTOMERID
     AND OND.ORG_NODEID = ORPD.ORG_NODEID
     AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = p_CorpId)
          OR (v_OrgNodeLevel=2 AND OND.PARENT_ORG_NODEID = p_CorpId)
          OR (v_OrgNodeLevel=3  AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
          OR (v_OrgNodeLevel=4  AND  OND.ORG_NODEID = (SELECT DISTINCT ORG.ORG_NODEID                             
                                  FROM  ORG_NODE_DIM ORG 
                                  WHERE ORG.ORG_NODE_LEVEL= 3
                                  START WITH ORG.ORG_NODEID =LoggedInUserJasperOrgId 
                                  CONNECT BY NOCYCLE PRIOR ORG.PARENT_ORG_NODEID=ORG.ORG_NODEID)))
     AND OND.ORG_NODE_LEVEL = 3
   ORDER BY OND.ORG_NODE_NAME) A
   WHERE ROWNUM =1 ;


CURSOR c_Get_TestPgm_Default (p_org_nodeid ORG_NODE_DIM.ORG_NODEID%TYPE) 
  IS
  SELECT B.Test_Program_Type_Seq
  FROM 
  (
  SELECT (CASE
               WHEN A.org_mode = 'PUBLIC' THEN
                1
               ELSE
                0
             END) Test_Program_Type_Seq,
             (CASE
               WHEN A.org_mode= 'PUBLIC' THEN
                'Public Schools'
               ELSE
                'Non Public Schools'
             END) Test_Program_Type
    FROM 
    (SELECT DISTINCT ORG.org_mode/*TP.TP_TYPE */
    FROM ORG_NODE_DIM ORG
    WHERE ORG.ORG_NODEID = p_org_nodeid
      AND ORG.CUSTOMERID = p_customerid
    )A 
    ORDER BY 1 DESC) B WHERE  ROWNUM = 1 ;




CURSOR c_Get_Product_Default
  IS
  ( SELECT A.PRODUCTID 
       FROM 
       (SELECT PDT.PRODUCTID,
         PDT.PRODUCT_NAME,
         PDT.PRODUCT_SEQ
        FROM PRODUCT PDT,
             ADMIN_DIM ADM,
             (SELECT DISTINCT ADMIN_YEAR,IS_CURRENT_ADMIN FROM ADMIN_DIM)   ADM1,
             CUST_PRODUCT_LINK CUST
        WHERE CUST.CUSTOMERID = p_customerid
          AND CUST.PRODUCTID = PDT.PRODUCTID
          AND CUST.ADMINID = ADM.ADMINID
          AND ADM.ADMIN_YEAR <=  ADM1.ADMIN_YEAR
          AND ADM1.IS_CURRENT_ADMIN = 'Y'
        ORDER BY PDT.PRODUCT_SEQ DESC)A
        WHERE ROWNUM = 1);
        
/*CURSOR c_Get_Product_For_Class
  IS
  SELECT PDT.PRODUCTID,PDT.PRODUCT_NAME
    FROM ORG_PRODUCT_LINK OPRD,
         CUST_PRODUCT_LINK CUST,
         PRODUCT PDT
    WHERE OPRD.ORG_NODEID = LoggedInUserJasperOrgId
      AND OPRD.CUST_PROD_ID = CUST.CUST_PROD_ID
      AND PDT.PRODUCTID = CUST.PRODUCTID
      AND CUST.CUSTOMERID = p_customerid 
      AND ROWNUM =1;  */
      
  CURSOR c_Get_Product_Specific
  IS
   SELECT  B.PRODUCTID
   FROM        
 (SELECT PDT.PRODUCTID,
          PDT.PRODUCT_NAME,
          PDT.PRODUCT_SEQ
    FROM ORG_PRODUCT_LINK OPRD,
         CUST_PRODUCT_LINK CUST,
         PRODUCT PDT,
         ADMIN_DIM ADM,
        (SELECT DISTINCT ADMIN_YEAR,IS_CURRENT_ADMIN FROM ADMIN_DIM)   ADM1
    WHERE OPRD.ORG_NODEID =LoggedInUserJasperOrgId
      AND OPRD.CUST_PROD_ID = CUST.CUST_PROD_ID
      AND PDT.PRODUCTID = CUST.PRODUCTID
      AND CUST.CUSTOMERID = p_customerid
      AND CUST.ADMINID = ADM.ADMINID
      AND ADM.ADMIN_YEAR <=  ADM1.ADMIN_YEAR
      AND ADM1.IS_CURRENT_ADMIN = 'Y'
      ORDER BY PDT.PRODUCT_SEQ DESC)B
      WHERE ROWNUM = 1;           

CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;

BEGIN
       FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
       END LOOP;
       
        ------FOR TEST PROGRAM
       IF (p_test_program =-99 OR p_test_program IS NULL ) AND v_OrgNodeLevel = 1 THEN 
          v_test_program := 1;
        ELSIF (p_test_program =-99 OR p_test_program IS NULL ) AND v_OrgNodeLevel <> 1 THEN 
        
              FOR r_Get_TestPgm_Default IN c_Get_TestPgm_Default(LoggedInUserJasperOrgId)
                LOOP 
                       v_test_program := r_Get_TestPgm_Default.Test_Program_Type_Seq;
              END LOOP;
         ELSE 
           v_test_program := p_test_program;      
       END IF;   
       
      
     ------for reports having "All" in district and school dropdown as an options            
      IF p_type = v_ALL_SCHOOL_OTH THEN
                 IF  p_school = -99   OR  p_school IS NULL  THEN
                    -----get  default product
                     IF  v_OrgNodeLevel > 1 THEN
                       FOR r_Get_Product_Specific IN c_Get_Product_Specific
                         LOOP
                              v_ProductId := r_Get_Product_Specific.PRODUCTID;
                       END LOOP;
                     ELSE   
                          FOR r_Get_Product_Default IN c_Get_Product_Default
                          LOOP 
                                 v_ProductId := r_Get_Product_Default.PRODUCTID;
                          END LOOP;
                     END IF; 
                     ---get default grade         
                      FOR r_Get_Grade_SchoolOth IN c_Get_Grade_SchoolOth(v_ProductId,v_test_program)
                      LOOP
                               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_SchoolOth.GRADEID;
                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_SchoolOth.GRADE_NAME;
                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_SchoolOth.GRADE_SEQ;

                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                      END LOOP;

                    ELSIF p_school  = -1 AND p_corpdiocese = -1   THEN
                     
                       FOR r_Get_Grade_School_For_All IN c_Get_Grade_School_For_All (p_test_administration)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_School_For_All.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_School_For_All.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_School_For_All.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                       END LOOP;   
                     ELSIF p_school  = -1 AND p_corpdiocese <> -1   THEN
                     
                       FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_corpdiocese)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                       END LOOP; 
                     ELSIF p_school  <> -1 AND p_corpdiocese <> -1   THEN
                     
                       FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_school)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                         END LOOP;                
                     END IF; 
          ------for reports that do not have "All"  as an option in district and school dropdown, for a teacher user           
      ELSIF p_type = v_SCHOOL_OTH AND v_OrgNodeLevel = 4 THEN
                 IF  p_test_administration =-99 THEN
                     FOR r_Get_Product_Specific IN c_Get_Product_Specific
                       LOOP
                            v_ProductId := r_Get_Product_Specific.PRODUCTID;
                     END LOOP;
                   ELSE v_ProductId:=  p_test_administration; 
                 END IF;  
                 FOR r_Get_Grade IN c_Get_Grade (v_ProductId,LoggedInUserJasperOrgId)
                 LOOP
                         t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP; 
                     
       ------for reports that do not have "All"  as an option in district and school dropdown, for  users other than teacher users               
       ELSIF p_type = v_SCHOOL_OTH AND v_OrgNodeLevel < 4 THEN

                 IF  p_school = -99   OR  p_school IS NULL  THEN
                    -----get  default product
                    IF  v_OrgNodeLevel > 1 THEN
                       FOR r_Get_Product_Specific IN c_Get_Product_Specific
                         LOOP
                              v_ProductId := r_Get_Product_Specific.PRODUCTID;
                       END LOOP;
                     ELSE   
                          FOR r_Get_Product_Default IN c_Get_Product_Default
                          LOOP 
                                 v_ProductId := r_Get_Product_Default.PRODUCTID;
                          END LOOP;
                     END IF; 
                     /*FOR r_Get_Product_Default IN c_Get_Product_Default
                              LOOP
                                     v_ProductId := r_Get_Product_Default.PRODUCTID;
                      END LOOP;*/

                      ---get default Corp
                      FOR r_Get_Corp_Default IN c_Get_Corp_Default (v_ProductId,v_test_program)
                      LOOP
                             v_CorpId := r_Get_Corp_Default.ORG_NODEID;
                      END LOOP;


                       ---get default School
                      FOR r_Get_School_Default IN c_Get_School_Default (v_ProductId,v_CorpId,v_test_program)
                      LOOP
                             v_SchoolId := r_Get_School_Default.ORG_NODEID;
                      END LOOP;

                      ---get default grade
                      FOR r_Get_Grade IN c_Get_Grade (v_ProductId,v_SchoolId)
                      LOOP
                               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                      END LOOP;


                    ELSIF p_school  = -1 AND p_corpdiocese = -1   THEN
                     
                        FOR r_Get_Grade_School_For_All IN c_Get_Grade_School_For_All (p_test_administration)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_School_For_All.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_School_For_All.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_School_For_All.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                       END LOOP;  
                     ELSIF p_school  = -1 AND p_corpdiocese <> -1   THEN
                     
                       FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_corpdiocese)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                       END LOOP; 
                     ELSIF p_school  <> -1 AND p_corpdiocese <> -1   THEN
                     
                       FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_school)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                         END LOOP;                
                     END IF;
                     
        ------for reports that have "All"  as an option in district and school dropdown for level 1,  and oes not have all option in other levels               
       ELSIF p_type = v_ALL_OPTION_TOGGLE_WITH_LEVEL  THEN

                 IF  p_school = -99   OR  p_school IS NULL  THEN
                    -----get  default product
                    IF  v_OrgNodeLevel > 1 THEN
                       FOR r_Get_Product_Specific IN c_Get_Product_Specific
                         LOOP
                              v_ProductId := r_Get_Product_Specific.PRODUCTID;
                       END LOOP;
                     ELSE   
                          FOR r_Get_Product_Default IN c_Get_Product_Default
                          LOOP 
                                 v_ProductId := r_Get_Product_Default.PRODUCTID;
                          END LOOP;
                     END IF; 
                    
                     
                     IF  v_OrgNodeLevel = 1 THEN
                           ---get default grade         
                            FOR r_Get_Grade_SchoolOth IN c_Get_Grade_SchoolOth(v_ProductId,v_test_program)
                            LOOP
                                     t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_SchoolOth.GRADEID;
                                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_SchoolOth.GRADE_NAME;
                                     t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_SchoolOth.GRADE_SEQ;

                                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                     t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                            END LOOP;                      
                     ELSE  ---default case for other levels                    
                        FOR r_Get_Corp_Default IN c_Get_Corp_Default (v_ProductId,v_test_program)
                        LOOP
                               v_CorpId := r_Get_Corp_Default.ORG_NODEID;
                        END LOOP;
                       ---get default School
                        FOR r_Get_School_Default IN c_Get_School_Default (v_ProductId,v_CorpId,v_test_program)
                        LOOP
                               v_SchoolId := r_Get_School_Default.ORG_NODEID;
                        END LOOP;
                        ---get default grade
                        FOR r_Get_Grade IN c_Get_Grade (v_ProductId,v_SchoolId)
                        LOOP
                                 t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                 t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                 t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                        END LOOP;
                    END IF;     

                    ELSIF p_school  = -1 AND p_corpdiocese = -1   THEN
                     
                        FOR r_Get_Grade_School_For_All IN c_Get_Grade_School_For_All (p_test_administration)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_School_For_All.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_School_For_All.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_School_For_All.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                       END LOOP;  
                     ELSIF p_school  = -1 AND p_corpdiocese <> -1   THEN
                     
                       FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_corpdiocese)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                       END LOOP; 
                     ELSIF p_school  <> -1 AND p_corpdiocese <> -1   THEN
                     
                       FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_school)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                         END LOOP;                
                     END IF; 
                                                                 
      -----for district only reports for which there is ALL option in Districts drop down for usres of all level
      ELSIF p_type = 'DISTRICT_ONLY' THEN  
              IF  p_corpdiocese = -99   OR  p_corpdiocese IS NULL  THEN
                            -----get  default product
                             IF  v_OrgNodeLevel > 1 THEN
                                 FOR r_Get_Product_Specific IN c_Get_Product_Specific
                                   LOOP
                                        v_ProductId := r_Get_Product_Specific.PRODUCTID;
                                 END LOOP;
                               ELSE   
                                    FOR r_Get_Product_Default IN c_Get_Product_Default
                                    LOOP 
                                           v_ProductId := r_Get_Product_Default.PRODUCTID;
                                    END LOOP;
                               END IF; 
                             ----get default grade         
                              FOR r_Get_Grade_DistrictOnly IN c_Get_Grade_SchoolOth(v_ProductId,v_test_program)
                              LOOP
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_DistrictOnly.GRADEID;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_DistrictOnly.GRADE_NAME;
                                       t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_DistrictOnly.GRADE_SEQ;

                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                       t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                              END LOOP;

                            ELSIF p_corpdiocese = -1   THEN
                             
                                FOR r_Get_Grade_School_For_All IN c_Get_Grade_School_For_All (p_test_administration)
                                  LOOP
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_School_For_All.GRADEID;
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_School_For_All.GRADE_NAME;
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_School_For_All.GRADE_SEQ;

                                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                                   END LOOP; 
                               ELSE
                             
                               FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_corpdiocese)
                                      LOOP
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                               END LOOP;   
                                           
                             END IF; 
                             
       -----for district only reports for which there is ALL option in Districts in level 1 but no ALL option in other levels                     
       ELSIF p_type = v_DISTRICT_ONLY_ALL_TOGGLE THEN  
              IF  p_corpdiocese = -99   OR  p_corpdiocese IS NULL  THEN
                            -----get  default product
                             IF  v_OrgNodeLevel > 1 THEN
                                 FOR r_Get_Product_Specific IN c_Get_Product_Specific
                                   LOOP
                                        v_ProductId := r_Get_Product_Specific.PRODUCTID;
                                 END LOOP;
                               ELSE   
                                    FOR r_Get_Product_Default IN c_Get_Product_Default
                                    LOOP 
                                           v_ProductId := r_Get_Product_Default.PRODUCTID;
                                    END LOOP;
                               END IF; 
                              IF v_OrgNodeLevel = 1 THEN  
                                 ----get default grade         
                                  FOR r_Get_Grade_DistrictOnly IN c_Get_Grade_SchoolOth(v_ProductId,v_test_program)
                                  LOOP
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_DistrictOnly.GRADEID;
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_DistrictOnly.GRADE_NAME;
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_DistrictOnly.GRADE_SEQ;

                                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                                  END LOOP;
                               ELSE
                                     ---get default corp
                                     FOR r_Get_Corp_Default IN c_Get_Corp_Default (v_ProductId,v_test_program)
                                      LOOP
                                             v_CorpId := r_Get_Corp_Default.ORG_NODEID;
                                      END LOOP;
                                     ---get default grade
                                      FOR r_Get_Grade IN c_Get_Grade (v_ProductId,v_CorpId)
                                      LOOP
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                                      END LOOP; 
                                 END IF;      
                            ELSIF p_corpdiocese = -1   THEN
                             
                                FOR r_Get_Grade_School_For_All IN c_Get_Grade_School_For_All (p_test_administration)
                                  LOOP
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade_School_For_All.GRADEID;
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade_School_For_All.GRADE_NAME;
                                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade_School_For_All.GRADE_SEQ;

                                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                                   END LOOP; 
                               ELSE
                             
                               FOR r_Get_Grade IN c_Get_Grade (p_test_administration,p_corpdiocese)
                                      LOOP
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_Grade.GRADEID;
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_Grade.GRADE_NAME;
                                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := r_Get_Grade.GRADE_SEQ;

                                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                               END LOOP;   
                                           
                             END IF;                     
      END IF;
       IF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN

            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -2;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'None Available';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -2;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
     END IF;

RETURN t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END SF_GET_GRADE;
/
