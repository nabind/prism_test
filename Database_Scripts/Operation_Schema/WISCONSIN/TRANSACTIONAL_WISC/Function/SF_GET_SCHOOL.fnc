CREATE OR REPLACE FUNCTION SF_GET_SCHOOL (
                              LoggedInUserJasperOrgId IN ORG_NODE_DIM.ORG_NODEID%TYPE
                             ,p_test_administration IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE
                             ,p_district IN VARCHAR2
                             ,p_customerid IN CUSTOMER_INFO.CUSTOMERID%TYPE
                             ,p_district_type IN VARCHAR2
                             ,p_is_summary IN VARCHAR2
                             )
RETURN PRS_COLL_PGT_GLOBAL_TEMP_OBJ
IS

  /*******************************************************************************
  * FUNCTION:  SF_GET_SCHOOL
  * PURPOSE:   To GET different SCHOOLS
  * CREATED:   TCS 06/JUN/2016
  * NOTE:
  *
  * MODIFIED :
  * DATE         AUTHOR:PARTHA     DESCRIPTION
  *-------------------------------------------------------------------------------
  *
  ************************************************************************************/

PRAGMA AUTONOMOUS_TRANSACTION;

  t_PRS_PGT_GLOBAL_TEMP_OBJ  PRS_PGT_GLOBAL_TEMP_OBJ;
  t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ PRS_COLL_PGT_GLOBAL_TEMP_OBJ := PRS_COLL_PGT_GLOBAL_TEMP_OBJ();

  v_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE;
  v_OrgNodeLevel ORG_NODE_DIM.ORG_NODE_LEVEL%TYPE;
  v_district  ORG_NODE_DIM.ORG_NODEID%TYPE;
  v_District_Type VARCHAR2(10);

 CURSOR c_Get_School (p_Cust_Product_Id_1 CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,p_Dist_Type_1 VARCHAR2,p_district_id ORG_NODE_DIM.ORG_NODEID%TYPE)
  IS
  SELECT OND.ORG_NODEID, OND.ORG_NODE_NAME||' '||OND.ORG_NODE_CODE AS ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.CUST_PROD_ID = p_Cust_Product_Id_1
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID = p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_NODEID = ORPD.ORG_NODEID
   AND OND.ORG_MODE = p_Dist_Type_1
   AND ((v_OrgNodeLevel=1 AND OND.PARENT_ORG_NODEID = p_district_id)
        OR (v_OrgNodeLevel=2 AND OND.PARENT_ORG_NODEID = p_district_id)
        OR (v_OrgNodeLevel=3  AND OND.ORG_NODEID = LoggedInUserJasperOrgId)
        )
   AND OND.ORG_NODE_LEVEL = 3
 ORDER BY 2;
                                     

 CURSOR c_Get_School_Default (p_Cust_Product_Id CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,p_Dist_Type VARCHAR2)
  IS
  SELECT OND.ORG_NODEID, OND.ORG_NODE_NAME||' '||OND.ORG_NODE_CODE AS ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.CUST_PROD_ID = p_Cust_Product_Id
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID =p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_NODEID = ORPD.ORG_NODEID
   AND OND.ORG_NODE_LEVEL = 3
    AND OND.ORG_MODE = p_Dist_Type
   START WITH OND.ORG_NODEID =LoggedInUserJasperOrgId
 CONNECT BY NOCYCLE PRIOR OND.ORG_NODEID=OND.PARENT_ORG_NODEID
 ORDER BY 2;
 
  CURSOR c_Get_School_Default_Count (p_Cust_Product_Id CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,p_Dist_Type VARCHAR2)
  IS
  SELECT COUNT(OND.ORG_NODEID) AS CNT                  
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.CUST_PROD_ID = p_Cust_Product_Id
   AND CPL.CUST_PROD_ID = ORPD.CUST_PROD_ID
   AND CPL.CUSTOMERID =p_customerid
   AND OND.CUSTOMERID = CPL.CUSTOMERID
   AND OND.ORG_NODEID = ORPD.ORG_NODEID
   AND OND.ORG_NODE_LEVEL = 3
    AND OND.ORG_MODE = p_Dist_Type
   START WITH OND.ORG_NODEID =LoggedInUserJasperOrgId
 CONNECT BY NOCYCLE PRIOR OND.ORG_NODEID=OND.PARENT_ORG_NODEID;



 CURSOR c_Get_District (p_Cust_ProductId CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,district_type_1 VARCHAR2)
  IS
    SELECT A.ORG_NODEID FROM
    (SELECT  OND.ORG_NODEID, OND.ORG_NODE_NAME||' '||OND.ORG_NODE_CODE AS ORG_NODE_NAME
  FROM CUST_PRODUCT_LINK     CPL,
       ORG_PRODUCT_LINK      ORPD,
       ORG_NODE_DIM          OND
 WHERE CPL.CUST_PROD_ID = p_Cust_ProductId
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
   AND OND.ORG_MODE = district_type_1
 ORDER BY 2) A
   WHERE ROWNUM=1;



 CURSOR c_Get_District_Type_Default
  IS
  SELECT ORG_MODE FROM ORG_NODE_DIM WHERE ORG_NODEID = LoggedInUserJasperOrgId AND ORG_NODE_LEVEL<>1
  UNION ALL
  SELECT 'PUBLIC' AS ORG_MODE FROM ORG_NODE_DIM WHERE ORG_NODEID = LoggedInUserJasperOrgId AND ORG_NODE_LEVEL=1;
    


CURSOR c_Get_Product_Specific
  IS
   SELECT  B.CUST_PROD_ID,
           B.ADMIN_NAME
   FROM
 (SELECT CUST.CUST_PROD_ID,
          ADM.ADMIN_NAME,
          ADM.ADMIN_SEQ
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
      ORDER BY ADM.ADMIN_SEQ DESC)B
      WHERE ROWNUM = 1;
      

 CURSOR c_Get_Org_Node_Level
  IS
 SELECT ORG.ORG_NODE_LEVEL AS ORG_NODE_LEVEL FROM ORG_NODE_DIM ORG  WHERE ORG.ORG_NODEID = LoggedInUserJasperOrgId AND ROWNUM =1;

BEGIN

       FOR r_Get_Org_Node_Level IN c_Get_Org_Node_Level
            LOOP
                   v_OrgNodeLevel := r_Get_Org_Node_Level.ORG_NODE_LEVEL;
       END LOOP;

      IF   (p_district = '-99' OR p_district IS NULL)  THEN

              --get default product
              FOR r_Get_Product_Specific IN c_Get_Product_Specific
               LOOP
                    v_Cust_ProductId := r_Get_Product_Specific.CUST_PROD_ID;
               END LOOP;
                   
            ---to get default district type
             FOR r_Get_District_Type_Default IN c_Get_District_Type_Default
               LOOP
                    v_District_Type := r_Get_District_Type_Default.ORG_MODE;
             END LOOP;

            --get default school
          FOR r_Get_School_Default_Count IN c_Get_School_Default_Count (v_Cust_ProductId,v_District_Type)
            LOOP
                IF ( r_Get_School_Default_Count.CNT>1  AND v_OrgNodeLevel =1 AND p_is_summary=1 ) THEN
                    t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'ALL';
                    t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

                    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                    t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                    
                ELSIF p_is_summary=0 THEN
                    -----get default district
                   FOR r_Get_District IN c_Get_District (v_Cust_ProductId,v_District_Type)
                   LOOP
                           v_district := r_Get_District.ORG_NODEID;
                   END LOOP; 
                  
                 -----get default school
                 FOR r_Get_School IN c_Get_School (v_Cust_ProductId,v_District_Type,v_district)
                  LOOP
                           t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School.ORG_NODEID;
                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School.ORG_NODE_NAME;
                           t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                           t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                  END LOOP;
                    
                ELSE  
                  FOR r_Get_School_Default IN c_Get_School_Default (v_Cust_ProductId,v_District_Type)
                      LOOP
                               t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School_Default.ORG_NODEID;
                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School_Default.ORG_NODE_NAME;
                               t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                               t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                      END LOOP;
               END IF;  
            END LOOP;
            
       ---FOR "ALL" OPTION IN DISTRICT DROP DOWN       
       ELSIF p_district = '-1' AND p_is_summary = 1 THEN
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'ALL';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       
         /*FOR r_Get_School_Default IN c_Get_School_Default (p_test_administration,p_district_type)
                LOOP
                         t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School_Default.ORG_NODEID;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School_Default.ORG_NODE_NAME;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
          END LOOP; */       
       ELSE
              FOR r_Get_School IN c_Get_School (p_test_administration,p_district_type,p_district)
                LOOP
                         t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();

                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := r_Get_School.ORG_NODEID;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := r_Get_School.ORG_NODE_NAME;
                         t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := 1;

                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
                         t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
                END LOOP;
      END IF;

      
       /* For 'All ' option  */
      IF   t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT >1 AND p_is_summary = 1 THEN
            t_PRS_PGT_GLOBAL_TEMP_OBJ := PRS_PGT_GLOBAL_TEMP_OBJ();
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc1 := -1;
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc2 := 'ALL';
            t_PRS_PGT_GLOBAL_TEMP_OBJ.vc3 := -1;

            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.EXTEND(1);
            t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ(t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT):= t_PRS_PGT_GLOBAL_TEMP_OBJ;
       
       ELSIF t_PRS_COLL_PGT_GLOBAL_TEMP_OBJ.COUNT = 0  THEN
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
  RAISE;
    RETURN NULL;
END SF_GET_SCHOOL;
/
