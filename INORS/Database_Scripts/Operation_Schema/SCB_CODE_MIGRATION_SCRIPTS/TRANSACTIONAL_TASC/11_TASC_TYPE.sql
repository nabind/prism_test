CREATE OR REPLACE TYPE PRS_PGT_GLOBAL_TEMP_OBJ AS OBJECT (
  VC1  VARCHAR2(100),
  VC2  VARCHAR2(100),
  VC3  VARCHAR2(100),
  VC4  VARCHAR2(100),
  VC5  VARCHAR2(100),
  VC6  VARCHAR2(100),
  VC7  VARCHAR2(100),
  VC8  VARCHAR2(100),
  VC9  VARCHAR2(100),
  VC10 VARCHAR2(100),
  VC11 VARCHAR2(100),
  VC12 VARCHAR2(100),
  VC13 VARCHAR2(100),
  VC14 VARCHAR2(100),
  VC15 VARCHAR2(100),
  CONSTRUCTOR FUNCTION PRS_PGT_GLOBAL_TEMP_OBJ RETURN SELF AS result
);
/

CREATE OR REPLACE TYPE BODY PRS_PGT_GLOBAL_TEMP_OBJ IS
  CONSTRUCTOR FUNCTION PRS_PGT_GLOBAL_TEMP_OBJ RETURN SELF AS RESULT
  IS
  BEGIN
    VC1  := NULL;
    VC2  := NULL;
    VC3  := NULL;
    VC4  := NULL;
    VC5  := NULL;
    VC6  := NULL;
    VC7  := NULL;
    VC8  := NULL;
    VC9  := NULL;
    VC10 := NULL;
    VC11  := NULL;
    VC12  := NULL;
    VC13  := NULL;
    VC14  := NULL;
    VC15 := NULL;
    RETURN;
  END PRS_PGT_GLOBAL_TEMP_OBJ;
END;
/

CREATE OR REPLACE TYPE PRS_COLL_PGT_GLOBAL_TEMP_OBJ AS TABLE OF PRS_PGT_GLOBAL_TEMP_OBJ;
/

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
  lithocode             VARCHAR2(30),
  ext_student_id        VARCHAR2(30),
  last_name             VARCHAR2(30),
  first_name            VARCHAR2(30),
  middle_name           VARCHAR2(30),
  birthdate             VARCHAR2(30),
  gender                VARCHAR2(30),
  org_cnt               NUMBER,
  Created_Date_Time     Varchar2(30),
  Last_Update_Date_Time Varchar2(30)
);
/

CREATE OR REPLACE TYPE ORG_DETAILS_TYP  IS TABLE OF ORG_DETAILS_OBJ;
/
CREATE OR REPLACE TYPE STUDENT_DEMO_DET_OBJ AS OBJECT(student_bio_id number, democode VARCHAR2(32), demoval VARCHAR2(32), subtestid NUMBER);
/

CREATE OR REPLACE TYPE STUDENT_DEMO_DET_TYP IS TABLE OF student_demo_det_obj;
/

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
  SCORE_VALUES   VARCHAR2(200)
);
/

CREATE OR REPLACE TYPE SUBOBJITM_SCR_DETAILS_TYP IS TABLE OF SUBOBJITM_SCR_DETAILS_OBJ;
/


CREATE OR REPLACE TYPE ER_demo_CODE_det_obj AS OBJECT(student_bio_id number,subtestid NUMBER,
 democode VARCHAR2(32), demoval VARCHAR2(32), date_test_taken DATE  )
/

CREATE OR REPLACE TYPE ER_demo_CODE_det_typ IS TABLE OF ER_demo_CODE_det_obj
/

drop type ORG_DETAILS_TYP ; 

drop type ORG_DETAILS_OBJ; 

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
  TEST_ELEMENT_ID       VARCHAR2(30),
  BARCODE               VARCHAR2(30),
  formid                NUMBER,
  lithocode             VARCHAR2(30),
  ext_student_id        VARCHAR2(30),
  last_name             VARCHAR2(30),
  first_name            VARCHAR2(30),
  middle_name           VARCHAR2(30),
  birthdate             VARCHAR2(30),
  gender                VARCHAR2(30),
  org_cnt               NUMBER,
  Created_Date_Time     Varchar2(30),
  Last_Update_Date_Time Varchar2(30)
)
/



CREATE OR REPLACE TYPE ORG_DETAILS_TYP IS TABLE OF ORG_DETAILS_OBJ
/