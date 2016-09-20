drop type ER_demo_CODE_det_typ;

drop type ER_demo_CODE_det_obj;

CREATE OR REPLACE TYPE ER_demo_CODE_det_obj AS OBJECT(student_bio_id number,subtestid NUMBER,
 democode VARCHAR2(32), demoval VARCHAR2(32), date_test_taken DATE  )
/

CREATE OR REPLACE TYPE ER_demo_CODE_det_typ IS TABLE OF ER_demo_CODE_det_obj
/

drop type ORG_DETAILS_TYP ; 

drop type ORG_DETAILS_OBJ; 

--Changed by Abir TEST_ELEMENT_ID ,lname, fname length
CREATE OR REPLACE TYPE ORG_DETAILS_OBJ AS OBJECT
(
  STUDENT_BIO_ID        NUMBER,
  customerid            NUMBER,
  ORG_NAME              VARCHAR2(50),
  ORG_TYPE              VARCHAR2(50),
  ORG_LEVEL             NUMBER,
  ORG_NODEID            NUMBER,
  og_lowestnodeid       NUMBER,
  ORG_CODE              VARCHAR2(50),
  INT_STUDENT_ID        VARCHAR2(30),
  TEST_ELEMENT_ID       VARCHAR2(50),
  BARCODE               VARCHAR2(30),
  formid                NUMBER,
  lithocode             VARCHAR2(30),
  ext_student_id        VARCHAR2(30),
  last_name             VARCHAR2(32),
  first_name            VARCHAR2(32),
  middle_name           VARCHAR2(32),
  birthdate             VARCHAR2(30),
  gender                VARCHAR2(30),
  org_cnt               NUMBER,
  Created_Date_Time     Varchar2(30),
  Last_Update_Date_Time Varchar2(30)
)
/



CREATE OR REPLACE TYPE ORG_DETAILS_TYP IS TABLE OF ORG_DETAILS_OBJ
/

drop type SUBOBJITM_SCR_DETAILS_TYP ; 

drop type SUBOBJITM_SCR_DETAILS_OBJ; 

/*New fileds added by Abir for GHI*/
CREATE OR REPLACE TYPE SUBOBJITM_SCR_DETAILS_OBJ AS OBJECT
(
  STUDENT_BIO_ID NUMBER ,
  SUBTESTID      NUMBER ,
  CUSTOMERID     NUMBER ,
  SUBTEST_NAME   VARCHAR2(32),
  SUBTEST_CODE   VARCHAR2(20) ,
  TEST_DATE      VARCHAR2(6),
  SCR_STR        VARCHAR2(182),
  OBJECTIVE_CODE VARCHAR2(20) ,
  OBJECTIVE_NAME VARCHAR2(35) ,
  SCORE_TYPE     CHAR(2),
  SCORE_VALUE    VARCHAR2(1),
  NT_ALL_ATTMPT  CHAR(1),
  ITEM_TYPE      VARCHAR2(3) ,
  ITEM_CODE      VARCHAR2(20) ,
  SCORE_VALUES   VARCHAR2(200),
  DRCDOCUMENT_ID NUMBER,
  TCASCHEDULEDATE DATE 
)
/

CREATE OR REPLACE TYPE SUBOBJITM_SCR_DETAILS_TYP IS TABLE OF SUBOBJITM_SCR_DETAILS_OBJ
/

 
 
