create table TASC_HIERARCHY_EXTRACT
(
  tasc_hier_extract_id NUMBER,
  client_code          VARCHAR2(16),
  adminid              NUMBER,
  orgtp                VARCHAR2(10),
  state_code           VARCHAR2(2),
  level_1_org_name     VARCHAR2(30),
  level_1_org_code     VARCHAR2(12),
  level_1_email_1      VARCHAR2(70),
  level_1_email_2      VARCHAR2(70),
  level_2_org_name     VARCHAR2(30),
  level_2_org_code     VARCHAR2(12),
  level_2_email_1      VARCHAR2(70),
  level_2_email_2      VARCHAR2(70),
  level_3_org_name     VARCHAR2(30),
  level_3_org_code     VARCHAR2(12),
  level_3_email_1      VARCHAR2(70),
  level_3_email_2      VARCHAR2(70),
  validation_status    VARCHAR2(2),
  validation_log       VARCHAR2(400),
  process_id           NUMBER,
  datetimestamp        DATE default SYSDATE
);

--GRANTS FOR THE NEW CUSTOMER CREATION PACKAGE PKG_DRC_TASC_CUSTOMER_CREATION

GRANT SELECT,INSERT ON DEMOGRAPHIC TO PRISMGLOBAL;
GRANT SELECT,INSERT ON DEMOGRAPHIC_VALUES TO PRISMGLOBAL;
GRANT SELECT,INSERT ON ORG_NODE_DIM TO PRISMGLOBAL;
GRANT SELECT ON SEQ_DEMOID TO PRISMGLOBAL;

---update the level 1 org_node_code to Customer code
DECLARE 
V_ORG_NODE_CODE ORG_NODE_DIM.ORG_NODE_CODE%TYPE;

BEGIN
 
FOR I IN (SELECT C.CUSTOMER_CODE,C.CUSTOMERID FROM CUSTOMER_INFO C WHERE  EXISTS (SELECT 1  
                                                                         FROM ORG_NODE_DIM O 
                                                                         WHERE O.CUSTOMERID =C.CUSTOMERID )
                                                                         )
  LOOP
 
  SELECT ORG_NODE_CODE INTO V_ORG_NODE_CODE 
  FROM ORG_NODE_DIM 
  WHERE CUSTOMERID =  I.CUSTOMERID  
  AND ORG_NODE_LEVEL = 1;
  
  --DBMS_OUTPUT.PUT_LINE(V_ORG_NODE_CODE);
   IF I.CUSTOMER_CODE <> V_ORG_NODE_CODE THEN
      
	   UPDATE ORG_NODE_DIM 
			   SET ORG_NODE_CODE = I.CUSTOMER_CODE
			   WHERE ORG_NODE_LEVEL = 1
			   AND CUSTOMERID = I.CUSTOMERID;
						  
	   UPDATE ORG_NODE_DIM 
		SET ORG_NODE_CODE_PATH = REPLACE(ORG_NODE_CODE_PATH,V_ORG_NODE_CODE,I.CUSTOMER_CODE)
		WHERE CUSTOMERID = I.CUSTOMERID;  
   END IF;
  END LOOP;
    
  COMMIT;
 
END;


--Update Existing Org Data (Append S to existing level 1 org_node_code)
DECLARE
  V_CUSTOMERID    NUMBER := 0;
  V_CUSTOMER_CODE CUSTOMER_INFO.CUSTOMER_CODE%TYPE;
  V_ORG_NODE_CODE ORG_NODE_DIM.ORG_NODE_CODE%TYPE;

BEGIN

  FOR I IN (SELECT C.CUSTOMER_CODE, C.CUSTOMERID
              FROM CUSTOMER_INFO C
             WHERE EXISTS (SELECT 1
                      FROM ORG_NODE_DIM O
                     WHERE O.CUSTOMERID = C.CUSTOMERID)) LOOP
  
    SELECT ORG_NODE_CODE
      INTO V_ORG_NODE_CODE
      FROM ORG_NODE_DIM
     WHERE CUSTOMERID = I.CUSTOMERID
       AND ORG_NODE_LEVEL = 1;
  
    IF I.CUSTOMER_CODE <> V_ORG_NODE_CODE THEN
      DBMS_OUTPUT.PUT_LINE('CUSTOMER_CODE AND LEVEL 1 ORG CODE IS NOT EQUAL FOR CUSTOEMR: ' ||
                           I.CUSTOMER_CODE);
    ELSE
      UPDATE ORG_NODE_DIM
         SET ORG_NODE_CODE = REPLACE(ORG_NODE_CODE,
                                     I.CUSTOMER_CODE,
                                     I.CUSTOMER_CODE || 'S')
       WHERE CUSTOMERID = I.CUSTOMERID
         AND ORG_NODE_LEVEL = 1;
    
      UPDATE ORG_NODE_DIM
         SET ORG_NODE_CODE_PATH = REPLACE(ORG_NODE_CODE_PATH,
                                          '0~' || I.CUSTOMER_CODE,
                                          '0~' || I.CUSTOMER_CODE || 'S')
       WHERE CUSTOMERID = I.CUSTOMERID;
      COMMIT;
      DBMS_OUTPUT.PUT_LINE('DONE FOR CUSTOMER: ' || I.CUSTOMER_CODE);
    END IF;
  END LOOP;
END;



--Update Existing ER Data
DECLARE
  V_ST_LEN     NUMBER := 0;
BEGIN
 FOR I IN(SELECT A.STATE_CODE,LENGTH(A.STATE_CODE) AS V_ST_LEN
    FROM (SELECT DISTINCT STATE_CODE
            FROM ER_STUDENT_DEMO
           ) A) LOOP
           
  IF I.V_ST_LEN = 2 THEN
    UPDATE ER_STUDENT_DEMO
       SET STATE_CODE = I.STATE_CODE || 'S'
     WHERE STATE_CODE = I.STATE_CODE;
  DBMS_OUTPUT.PUT_LINE('Updated for state: '||I.STATE_CODE);
  ELSE
    DBMS_OUTPUT.PUT_LINE('The Length of State Code is not equal to 2..Please verify');
  END IF;
  COMMIT;
  END LOOP;
END;



--Demo_Config

create table DEMO_CONFIG
(
  SL_NO             NUMBER,
  DEMO_NAME         VARCHAR2(50) not null,
  DEMO_CODE         VARCHAR2(20) not null,
  DEMO_MODE         VARCHAR2(20) not null,
  SUBTESTID         NUMBER,
  CATEGORY          VARCHAR2(20),
  IS_DEMO_VALUE_AVL VARCHAR2(1) not null,
  DEMO_VALUE_NAME   VARCHAR2(200),
  DEMO_VALUE_CODE   VARCHAR2(200),
  IS_DEFAULT        VARCHAR2(1)
);

alter table DEMO_CONFIG add primary key (DEMO_CODE);

---------------------
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (1, 'RegisteredAtTestCenterCountyParishCode', 'Stdnt_Reg_Tccp_Cd', 'BO', null, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (2, 'RegisteredAtTestCenterCode', 'Stdnt_Reg_Tc_Cd', 'BO', null, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (3, 'Sched_DateChkdIn', 'Cont_Dt_Chk_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (4, 'Sched_DateChkdIn', 'Cont_Dt_Chk_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (5, 'Sched_DateChkdIn', 'Cont_Dt_Chk_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (6, 'Sched_DateChkdIn', 'Cont_Dt_Chk_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (7, 'Sched_DateChkdIn', 'Cont_Dt_Chk_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (8, 'Sched_ECC', 'Cont_Ecc_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (9, 'Sched_ECC', 'Cont_Ecc_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (10, 'Sched_ECC', 'Cont_Ecc_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (11, 'Sched_ECC', 'Cont_Ecc_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (12, 'Sched_ECC', 'Cont_Ecc_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (13, 'Sched_TC_Ctr_Cd', 'Cont_Tc_Cd_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (14, 'Sched_TC_Ctr_Cd', 'Cont_Tc_Cd_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (15, 'Sched_TC_Ctr_Cd', 'Cont_Tc_Cd_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (16, 'Sched_TC_Ctr_Cd', 'Cont_Tc_Cd_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (17, 'Sched_TC_Ctr_Cd', 'Cont_Tc_Cd_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (18, 'Sched_TC_CountyParishCode', 'Cont_Sch_Tp_Cd_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (19, 'Sched_TC_CountyParishCode', 'Cont_Sch_Tp_Cd_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (20, 'Sched_TC_CountyParishCode', 'Cont_Sch_Tp_Cd_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (21, 'Sched_TC_CountyParishCode', 'Cont_Sch_Tp_Cd_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (22, 'Sched_TC_CountyParishCode', 'Cont_Sch_Tp_Cd_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (23, 'Schedule_ID', 'Cont_Schld_Id_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (24, 'Schedule_ID', 'Cont_Schld_Id_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (25, 'Schedule_ID', 'Cont_Schld_Id_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (26, 'Schedule_ID', 'Cont_Schld_Id_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (27, 'Schedule_ID', 'Cont_Schld_Id_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (28, 'Sched_Date', 'Cont_Dt_Schld_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (29, 'Sched_Date', 'Cont_Dt_Schld_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (30, 'Sched_Date', 'Cont_Dt_Schld_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (31, 'Sched_Date', 'Cont_Dt_Schld_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (32, 'Sched_Date', 'Cont_Dt_Schld_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (33, 'TASCReadiness', 'Stdnt_Tasc_Rd', 'BO', null, null, 'Y', 'Y,N, ', 'Y,N,<BLANK>', 'N');
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (34, 'Content_Test_Code', 'Cont_Tst_Cd_Read', 'BO', 2047, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (35, 'Content_Test_Code', 'Cont_Tst_Cd_Wrt', 'BO', 2048, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (36, 'Content_Test_Code', 'Cont_Tst_Cd_Math', 'BO', 2050, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (37, 'Content_Test_Code', 'Cont_Tst_Cd_Sci', 'BO', 2051, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (38, 'Content_Test_Code', 'Cont_Tst_Cd_Sc', 'BO', 2052, null, 'N', null, null, null);
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (39, 'Test Form', 'Test_Form', 'BO', null, null, 'Y', 'D,E,F,BR-E,BR-F', 'D,E,F,BR-E,BR-F', 'N');
insert into DEMO_CONFIG (sl_no, demo_name, demo_code, demo_mode, subtestid, category, is_demo_value_avl, demo_value_name, demo_value_code, is_default)
values (40, 'Field Test Form', 'Fld_Tst_Form', 'OL', null, null, 'N', null, null, 'N');
commit;
