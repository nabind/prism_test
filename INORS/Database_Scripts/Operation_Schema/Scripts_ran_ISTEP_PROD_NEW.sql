-- Scripts Ran IN PRE PROD Database -- 

--All Create and Alter Tables -- 

--1-- Objective Dim Alter and Update -- 

ALTER TABLE OBJECTIVE_DIM ADD (OBJECTIVE_DESC VARCHAR2(100),DATETIMESTAMP1  DATE);

UPDATE OBJECTIVE_DIM 
  SET DATETIMESTAMP1 = DATETIMESTAMP; 
  
COMMIT;
  
ALTER  TABLE OBJECTIVE_DIM MODIFY DATETIMESTAMP1 DEFAULT SYSDATE  NOT NULL; 
  
ALTER TABLE OBJECTIVE_DIM DROP COLUMN DATETIMESTAMP; 
  
ALTER TABLE OBJECTIVE_DIM RENAME COLUMN DATETIMESTAMP1 TO DATETIMESTAMP;  

--2-- ROLE Table Update -- 


 update role set DESCRIPTION = 'Admin User' where roleid = 3; 
 
 commit ; 
 
--3-- Drop & Create Article Content & Article_MetaData -- 


 -- Create Backup Table -- 
 
create table ARTICLE_CONTENT_bkp as select * from ARTICLE_CONTENT ; 



-- drop table
drop table ARTICLE_CONTENT;

-- Create table
create table ARTICLE_CONTENT
(
  ARTICLE_CONTENT_ID NUMBER not null,
  ARTICLE_CONTENT    CLOB,
  DESCRIPTION        VARCHAR2(4000),
  OBJECTIVEID        NUMBER,
  CREATED_DATE_TIME  DATE,
  UPDATED_DATE_TIME  DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table ARTICLE_CONTENT
  add constraint PK_ARTICLE_CONTENT primary key (ARTICLE_CONTENT_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table ARTICLE_CONTENT
  add constraint FK_ARTICLE_CONTENT_1 foreign key (OBJECTIVEID)
  references OBJECTIVE_DIM (OBJECTIVEID);

-- Create Backup Table --   
create table ARTICLE_metadata_bkp as select * from ARTICLE_metadata;  
  
  -- drop table
drop table ARTICLE_METADATA;

-- Create table
create table ARTICLE_METADATA
(
  ARTICLEID          NUMBER not null,
  ARTICLE_NAME       VARCHAR2(255),
  CUST_PROD_ID       NUMBER not null,
  SUBT_OBJ_MAPID     NUMBER,
  SUBTESTID          NUMBER,
  ARTICLE_CONTENT_ID NUMBER,
  CATEGORY           VARCHAR2(100),
  CATEGORY_TYPE      VARCHAR2(20),
  CATEGORY_SEQ       NUMBER,
  SUB_HEADER         VARCHAR2(255),
  DESCRIPTION        VARCHAR2(4000),
  GRADEID            NUMBER,
  PROFICIENCY_LEVEL  VARCHAR2(100),
  RESOLVED_RPRT_STATUS VARCHAR2(10),
  CREATED_DATE_TIME  DATE,
  UPDATED_DATE_TIME  DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table ARTICLE_METADATA
  add constraint PK_ARTICLE_METADATA primary key (ARTICLEID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_1 foreign key (CUST_PROD_ID)
  references CUST_PRODUCT_LINK (CUST_PROD_ID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_2 foreign key (GRADEID)
  references GRADE_DIM (GRADEID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_3 foreign key (SUBTESTID)
  references SUBTEST_DIM (SUBTESTID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_4 foreign key (SUBT_OBJ_MAPID)
  references SUBTEST_OBJECTIVE_MAP (SUBT_OBJ_MAPID);
alter table ARTICLE_METADATA
  add constraint FK_ARTICLE_METADATA_5 foreign key (ARTICLE_CONTENT_ID)
  references ARTICLE_CONTENT (ARTICLE_CONTENT_ID);

--4-- Invitation Code Alter -- 

alter table invitation_code modify filename varchar2(500);  


--5-- Objective Score Fact Alter -- 

ALTER TABLE OBJECTIVE_SCORE_FACT RENAME TO OBJECTIVE_SCORE_FACT_bkp<mmddyy>   ;  

ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT PK_OBJECTIVE_SCORE_FACT;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_1;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_10;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_11;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_12;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_13;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_2;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_3;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_4;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_5;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_6;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_7;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_8;
ALTER TABLE OBJECTIVE_SCORE_FACT_bkp<mmddyy> DROP CONSTRAINT FK_OBJECTIVE_SCORE_FACT_9; 

 drop INDEX IND_OBJECTIVE_SCORE_FACT_1 ;   
 
 DROP INDEX   PK_OBJECTIVE_SCORE_FACT ; 
 
 CREATE TABLE OBJECTIVE_SCORE_FACT
(
  OBJECTIVE_FACTID NUMBER NOT NULL,
  ORG_NODEID       NUMBER,
  CUST_PROD_ID     NUMBER NOT NULL,
  ASSESSMENTID     NUMBER NOT NULL,
  STUDENT_BIO_ID   NUMBER NOT NULL,
  CONTENTID        NUMBER NOT NULL,
  SUBTESTID        NUMBER NOT NULL,
  OBJECTIVEID      NUMBER NOT NULL,
  GENDERID         NUMBER NOT NULL,
  GRADEID          NUMBER NOT NULL,
  LEVELID          NUMBER NOT NULL,
  FORMID           NUMBER NOT NULL,
  ADMINID          NUMBER NOT NULL,
  NCR              NUMBER,
  OS               VARCHAR2(5),
  OPI              VARCHAR2(5),
  OPI_CUT          VARCHAR2(5),
  MEAN_IPI         VARCHAR2(5),
  PC               NUMBER,
  PP               NUMBER,
  SS               NUMBER,
  PL               VARCHAR2(1),
  INRC             VARCHAR2(1),
  CONDCODE_ID      NUMBER,
  TEST_DATE        DATE,
  DATETIMESTAMP    DATE DEFAULT SYSDATE NOT NULL
)
PARTITION BY LIST (CUST_PROD_ID)
(
  PARTITION PART_OBJ_P5001 VALUES (5001)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5002 VALUES (5002)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5003 VALUES (5003)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5004 VALUES (5004)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5005 VALUES (5005)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5006 VALUES (5006)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5007 VALUES (5007)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5008 VALUES (5008)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5009 VALUES (5009)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5010 VALUES (5010)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  
  PARTITION PART_OBJ_P5013 VALUES (5013)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5014 VALUES (5014)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  PARTITION PART_OBJ_P5019 VALUES (5019)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ),
  
	  
  PARTITION PART_OBJ_OTHERS VALUES (DEFAULT)
    TABLESPACE PRISM_DATA1
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    )
); 

--6-- STFD FACT Alter  -- 

CREATE TABLE stfd_fact_bkp<mmddyy> AS SELECT * FROM stfd_fact ;
 
 DROP TABLE stfd_fact ; 
 
 create table STFD_FACT
(
  STFD_ID             NUMBER not null,
  ORG_NODEID          NUMBER not null,
  CUST_PROD_ID        NUMBER not null,
  ADMINID             NUMBER not null,
  CONTENTID           NUMBER not null,
  SUBTESTID           NUMBER not null,
  GRADEID             NUMBER not null,
  LEVELID             NUMBER not null,
  TESTDATE            VARCHAR2(6),
  SCALESCORE          NUMBER,
  FREQUENCY           NUMBER,
  PERCENT             NUMBER,
  CUMULATIVEFREQUENCY VARCHAR2(6),
  CUMULATIVEPERCENT   VARCHAR2(6),
  NUMBEROFSTUDENTS    VARCHAR2(6),
  HIGHSCORE           VARCHAR2(3),
  LOWSCORE            VARCHAR2(3),
  LOCALPERCENTILE90   VARCHAR2(5),
  LOCALPERCENTILE75   VARCHAR2(5),
  LOCALPERCENTILE50   VARCHAR2(5),
  LOCALPERCENTILE25   VARCHAR2(5),
  LOCALPERCENTILE10   VARCHAR2(5),
  MEAN                VARCHAR2(5),
  STANDARDDEVIATION   VARCHAR2(5),
  PASSPLUS_HIGHSCORE  VARCHAR2(3),
  PASSPLUS_LOWSCORE   VARCHAR2(3),
  PASS_HIGHSCORE      VARCHAR2(3),
  PASS_LOWSCORE       VARCHAR2(3),
  DNP_HIGHSCORE       VARCHAR2(3),
  DNP_LOWSCORE        VARCHAR2(3),
  STRUCTURE_LEVEL     VARCHAR2(2),
  ELEMENT_NUMBER      VARCHAR2(7),
  ISPUBLIC            NUMBER,
  DATETIMESTAMP       DATE not null
)
partition by list (CUST_PROD_ID)
(
  partition PART_P5001 values (5001)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5002 values (5002)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5003 values (5003)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5004 values (5004)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5005 values (5005)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5006 values (5006)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5007 values (5007)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5008 values (5008)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5009 values (5009)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5010 values (5010)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5011 values (5011)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5012 values (5012)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5013 values (5013)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5014 values (5014)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5015 values (5015)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_P5016 values (5016)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    ),
  partition PART_OTHERS values (DEFAULT)
    tablespace USERS
    pctfree 10
    initrans 1
    maxtrans 255
    storage
    (
      initial 8M
      next 1M
      minextents 1
      maxextents unlimited
    )
);

--7-- Cutscore IPI Alter 
create table cutscoreipi_bkp
as
select * from cutscoreipi;


alter table cutscoreipi
rename column STANDARD to OBJECTIVEID;

 --------------------------------All Create and Alter Tables Ends----------------------------------------------
 
 --Data Load for STFD_FACT, OBJECTIVE_SCORE_FACT & cutscoreipi Starts -- 
 
 --8 -- STFD Load -- 
 
 INSERT /*+APPEND*/  INTO stfd_fact SELECT * FROM <Backup Table Created in STEP 6> ; 

 commit ; 

--9-- Cutscore Ipi loads -- 

update cutscoreipi c
   set c.objectiveid = (select vw.objectiveid
                        from cutscoreipi               cut,
                             vw_subtest_grade_objective_map vw,
                             cust_product_link              cpl,
                             assessment_dim                 ad
                       where cut.subtestid = vw.subtestid
                         and cut.gradeid = vw.gradeid
                         and cut.objectiveid = vw.objective_code
                         and cut.cust_prod_id = cpl.cust_prod_id
                         and cpl.productid = ad.productid
                         and ad.assessmentid = vw.assessmentid
                         and cut.cutscoreipiid=c.cutscoreipiid); 
						 
commit ;  
 
 --10-- Objective Score Fact Load-- 
 
 -- Note -- Before the load starts we need to compile the pkg_inors_migration by commenting all the procedure because ISTEP_DATAMIG schema is 
 -- not there, also we need to create a sequence. 

 SELECT MAX(objective_factid) FROM OBJECTIVE_SCORE_FACT_bkp051514 -- 67871950 ;  
 
 create sequence OBJECTIVE_FACT_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 67871955
increment by 1
cache 20;

BEGIN 
  FOR i IN (SELECT DISTINCT assessmentid FROM test_tp_map   ORDER BY assessmentid  ) 
  LOOP 
      pkg_inors_migration.PROC_OBJ_SCORE_POPULATE_NEW(i.assessmentid); 
      
  END LOOP ; 
END ; 

INSERT INTO objective_score_fact SELECT * FROM OBJECTIVE_SCORE_FACT_bkp051514  WHERE cust_prod_id IN (5019,5021,5022); 

--10-- Cutscore Ipi loads -- 

update cutscoreipi c
   set c.objectiveid = (select vw.objectiveid
                        from cutscoreipi               cut,
                             vw_subtest_grade_objective_map vw,
                             cust_product_link              cpl,
                             assessment_dim                 ad
                       where cut.subtestid = vw.subtestid
                         and cut.gradeid = vw.gradeid
                         and cut.objectiveid = vw.objective_code
                         and cut.cust_prod_id = cpl.cust_prod_id
                         and cpl.productid = ad.productid
                         and ad.assessmentid = vw.assessmentid
                         and cut.cutscoreipiid=c.cutscoreipiid); 
						 
commit ; 

						 
-----------------Data Load for STFD_FACT, OBJECTIVE_SCORE_FACT & cutscoreipi Ends ----------------------------------------------------------

 
--- Metadata Verification & Setup for 2014 All Report Messages & PN Content Migrations ------------------------------------------- 


 
--11-- Updating Objective Dim for Objective Description & ITEMSET_DIM changes  -- 

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'The Nature of Science and Technology'
 WHERE objective_name = 'Nature of Sci & Tech';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Scientific Thinking'
 WHERE objective_name = 'Scientific Thinking';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'The Physical Setting'
 WHERE objective_name = 'The Physical Setting';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'The Living Environment'
 WHERE objective_name = 'The Living Environment';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'The Mathematical World'
 WHERE objective_name = 'The Mathematical World';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Common Themes'
 WHERE objective_name = 'Common Themes'; 
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Earth Science'
 WHERE objective_name = 'Earth Science'; 
 
-- Grade 6 Science

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Physical Science'
 WHERE objective_name = 'Physical Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Earth and Space Science'
 WHERE objective_name = 'Earth & Space Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Life Science'
 WHERE objective_name = 'Life Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Science, Engineering and Technology'
 WHERE objective_name = 'Science Eng & Tech';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'The Nature of Science'
 WHERE objective_name = 'The Nature of Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'The Design Process'
 WHERE objective_name = 'The Design Process'; 
 
--For ELA

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Word Recognition, Fluency, and Vocabulary Development'
 WHERE objective_name = 'Vocabulary';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Comprehension and Analysis of Nonfiction and Informational Text'
 WHERE objective_name = 'Nonfiction/Info Text†';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Comprehension and Analysis of Literary Text'
 WHERE objective_name = 'Literary Text†';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'WRITING: Processes and Features'
 WHERE objective_name = 'Writing Process';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'WRITING: Applications (Different Types of Writing and Their Characteristics)'
 WHERE objective_name = 'Writing Applications';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'WRITING: English Language Conventions'
 WHERE objective_name = 'Lang. Conventions'; 

--For Mathematics

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Number Sense'
 WHERE objective_name = 'Number Sense';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Computation'
 WHERE objective_name = 'Computation';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Algebra and Functions'
 WHERE objective_name = 'Algebra & Functions';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Geometry'
 WHERE objective_name = 'Geometry';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Measurement'
 WHERE objective_name = 'Measurement';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Problem Solving'
 WHERE objective_name = 'Problem Solving'; 
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Data Analysis and Probability'
 WHERE objective_name = 'Data Analysis & Prob';  
 
 -- Social Study
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'History'
 WHERE objective_name = 'History';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Civics and Government'
 WHERE objective_name = 'Civics & Government'; 
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Geography'
 WHERE objective_name = 'Geography';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Economics'
 WHERE objective_name = 'Economics'; 
 
COMMIT;
End;

MERGE INTO ITEMSET_DIM TGT
USING (SELECT * FROM ITEMSET_DIM@PROD_TO_ISTEPMASTER) SRC
ON (TGT.ITEMSETID = SRC.ITEMSETID)
WHEN MATCHED THEN
  UPDATE
     SET TGT.PDF_FILENAME   = SRC.PDF_FILENAME,
         TGT.ITEM_NAME      = SRC.ITEM_NAME,
         TGT.ITEM_CODE      = SRC.ITEM_CODE,
         TGT.SESSION_ID     = SRC.SESSION_ID,
         TGT.ITEM_SEQ       = SRC.ITEM_SEQ,
         TGT.POINT_POSSIBLE = SRC.POINT_POSSIBLE,
         TGT.ITEM_TYPE      = SRC.ITEM_TYPE,
         TGT.SUBT_OBJ_MAPID = SRC.SUBT_OBJ_MAPID,
         TGT.DATETIMESTAMP  = SYSDATE
WHEN NOT MATCHED THEN
  INSERT
  VALUES
    (SRC.ITEMSETID,
     SRC.ITEM_NAME,
     SRC.ITEM_CODE,
     SRC.SESSION_ID,
     SRC.ITEM_SEQ,
     SRC.POINT_POSSIBLE,
     SRC.ITEM_TYPE,
     SRC.PDF_FILENAME,
     SRC.SUBT_OBJ_MAPID,
     SYSDATE) ; 

--12 -- Inserting the ISTEP+ Spring 2014 Records from ISTEP_MASTER for cutscoreipi & cutscorescalescore-- 

Note : In ISTEP_MASTER schema, cust_prod_id for Product 	ISTEP+ Spring 2014 is 5017 where as in PRODUCTION it is 
       5024, so when we are populating the records from ISTEP_MASTER we need to decode it to 5024 from 5017. 

INSERT INTO cutscoreipi 
SELECT CUTSCOREIPIID      ,
  GRADEID           ,
  SUBTESTID         ,
  decode(CUST_PROD_ID, 5017, 5024, CUST_PROD_ID)       ,
  OBJECTIVEID       ,
  IPI_AT_PASS       ,
  SYSDATE, 
  SYSDATE 
  FROM (SELECT CUTSCOREIPIID      ,
  GRADEID           ,
  SUBTESTID         ,
  CUST_PROD_ID      ,
  OBJECTIVEID       ,
  IPI_AT_PASS       ,
  CREATED_DATE_TIME ,
  UPDATED_DATE_TIME  FROM cutscoreipi@PROD_TO_ISTEPMASTER --WHERE cust_prod_id IN (SELECT cust_prod_id FROM cust_product_link WHERE customerid =1000) 
  MINUS 
SELECT CUTSCOREIPIID      ,
  GRADEID           ,
  SUBTESTID         ,
  CUST_PROD_ID      ,
  OBJECTIVEID       ,
  IPI_AT_PASS       ,
  CREATED_DATE_TIME ,
  UPDATED_DATE_TIME  FROM    cutscoreipi) ;

INSERT INTO cutscorescalescore 
 SELECT  CUTSCORESCALESCOREID   ,
  GRADEID              ,
  LEVELID              ,
  SUBTESTID            ,
  decode(CUST_PROD_ID, 5017, 5024,CUST_PROD_ID )         ,
  LOSS                 ,
  HOSS                 ,
  PASS                 ,
  PASSPLUS             ,
  SYSDATE     ,
  SYSDATE  
  FROM (SELECT CUTSCORESCALESCOREID   ,
  GRADEID              ,
  LEVELID              ,
  SUBTESTID            ,
  CUST_PROD_ID         ,
  LOSS                 ,
  HOSS                 ,
  PASS                 ,
  PASSPLUS             ,
  CREATED_DATE_TIME    ,
  UPDATED_DATE_TIME  FROM cutscorescalescore@PROD_TO_ISTEPMASTER  --WHERE cust_prod_id IN (SELECT cust_prod_id FROM cust_product_link WHERE customerid =1000) 
 MINUS 
 SELECT CUTSCORESCALESCOREID   ,
  GRADEID              ,
  LEVELID              ,
  SUBTESTID            ,
  CUST_PROD_ID         ,
  LOSS                 ,
  HOSS                 ,
  PASS                 ,
  PASSPLUS             ,
  CREATED_DATE_TIME    ,
  UPDATED_DATE_TIME FROM   cutscorescalescore) ; 	

--13 --  Inserting the ISTEP+ Spring 2014 Records from ISTEP_MASTER for disaggregation_category & disaggregation_category_type-- 

Note : In ISTEP_MASTER schema, cust_prod_id for Product 	ISTEP+ Spring 2014 is 5017 where as in PRODUCTION it is 
       5024, so when we are populating the records from ISTEP_MASTER we need to decode it to 5024 from 5017. 	 
	 
INSERT INTO disaggregation_category 
SELECT  DISAGGREGATIONCATEGORYID      ,
  DISAGGREGATIONCATEGORYTYPEID ,
  DISAGGREGATIONCATEGORYCODE   ,
  DISAGGREGATIONCATEGORYNAME   ,
  ORDERBY                      ,
  decode(CUST_PROD_ID, 5017, 5024,CUST_PROD_ID )                 ,
  SYSDATE            ,
  SYSDATE  
FROM (SELECT  DISAGGREGATIONCATEGORYID      ,
  DISAGGREGATIONCATEGORYTYPEID ,
  DISAGGREGATIONCATEGORYCODE   ,
  DISAGGREGATIONCATEGORYNAME   ,
  ORDERBY                      ,
  CUST_PROD_ID                 ,
  CREATED_DATE_TIME            ,
  UPDATED_DATE_TIME          FROM disaggregation_category@PROD_TO_ISTEPMASTER  --WHERE cust_prod_id IN (SELECT cust_prod_id FROM cust_product_link WHERE customerid =1000) 
MINUS 
SELECT  DISAGGREGATIONCATEGORYID      ,
  DISAGGREGATIONCATEGORYTYPEID ,
  DISAGGREGATIONCATEGORYCODE   ,
  DISAGGREGATIONCATEGORYNAME   ,
  ORDERBY                      ,
  CUST_PROD_ID                 ,
  CREATED_DATE_TIME            ,
  UPDATED_DATE_TIME          FROM   disaggregation_category)  ; 
  
   
INSERT INTO disaggregation_category_type 
 SELECT DISAGGREGATIONCATEGORYTYPEID    ,
  DISAGGREGATIONCATEGORYTYPENAME ,
  ORDERBY                        ,
  decode(CUST_PROD_ID, 5017, 5024,CUST_PROD_ID )  ,
  SYSDATE              ,
  SYSDATE FROM 
  (SELECT  DISAGGREGATIONCATEGORYTYPEID    ,
  DISAGGREGATIONCATEGORYTYPENAME ,
  ORDERBY                        ,
    
  CUST_PROD_ID                   ,
  CREATED_DATE_TIME              ,
  UPDATED_DATE_TIME               
  FROM disaggregation_category_type@PROD_TO_ISTEPMASTER  -- WHERE cust_prod_id IN (SELECT cust_prod_id FROM cust_product_link WHERE customerid =1000) 
MINUS 
SELECT  DISAGGREGATIONCATEGORYTYPEID    ,
  DISAGGREGATIONCATEGORYTYPENAME ,
  ORDERBY                        ,
  CUST_PROD_ID                   ,
  CREATED_DATE_TIME              ,
  UPDATED_DATE_TIME               
  FROM   disaggregation_category_type  )  ;  

--14 --   State IPI Score Table Creation and Population -- 

create table STATE_MEAN_IPI_SCORE
(
  STATE_MEAN_IPI_SCOREID NUMBER not null,
  GRADEID                NUMBER not null,
  SUBTESTID              NUMBER not null,
  CUST_PROD_ID           NUMBER,
  OBJECTIVEID            NUMBER,
  ISPUBLIC               VARCHAR2(20),
  MEAN_IPI               VARCHAR2(5),
  CREATED_DATE_TIME      DATE,
  UPDATED_DATE_TIME      DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table STATE_MEAN_IPI_SCORE
  add constraint PK_STATE_MEAN_IPI_SCORE primary key (STATE_MEAN_IPI_SCOREID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

ALTER TABLE STATE_MEAN_IPI_SCORE
  ADD CONSTRAINT FK_STATE_MEAN_IPI_SCORE_1 FOREIGN KEY (CUST_PROD_ID)
  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);

ALTER TABLE STATE_MEAN_IPI_SCORE
  ADD CONSTRAINT FK_STATE_MEAN_IPI_SCORE_2 FOREIGN KEY (SUBTESTID)
  REFERENCES SUBTEST_DIM (SUBTESTID);

ALTER TABLE STATE_MEAN_IPI_SCORE
  ADD CONSTRAINT FK_STATE_MEAN_IPI_SCORE_3 FOREIGN KEY (GRADEID)
  REFERENCES GRADE_DIM (GRADEID);

ALTER TABLE STATE_MEAN_IPI_SCORE
  ADD CONSTRAINT FK_STATE_MEAN_IPI_SCORE_4 FOREIGN KEY (OBJECTIVEID)
  REFERENCES OBJECTIVE_DIM (OBJECTIVEID);   
  
 
  INSERT INTO state_mean_IPI_SCORE SELECT  STATE_MEAN_IPI_SCOREID  ,
  GRADEID                ,
  SUBTESTID              ,
    decode(CUST_PROD_ID, 5017, 5024,CUST_PROD_ID )           ,
  OBJECTIVEID            ,
  ISPUBLIC               ,
  MEAN_IPI               ,
  CREATED_DATE_TIME      ,
  UPDATED_DATE_TIME       FROM STATE_MEAN_IPI_SCORE@PROD_TO_ISTEPMASTER  ; 

--15-- All Indexes and Constraints -- 


-- STFD_FACT Constraints -- 
alter table STFD_FACT
  add constraint STFD_FACT_PK primary key (STFD_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table STFD_FACT
  add constraint FK_STFD_FACT_1 foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID);
alter table STFD_FACT
  add constraint FK_STFD_FACT_2 foreign key (ADMINID)
  references ADMIN_DIM (ADMINID);
alter table STFD_FACT
  add constraint FK_STFD_FACT_3 foreign key (CONTENTID)
  references CONTENT_DIM (CONTENTID);
alter table STFD_FACT
  add constraint FK_STFD_FACT_4 foreign key (SUBTESTID)
  references SUBTEST_DIM (SUBTESTID);
alter table STFD_FACT
  add constraint FK_STFD_FACT_5 foreign key (GRADEID)
  references GRADE_DIM (GRADEID);
alter table STFD_FACT
  add constraint FK_STFD_FACT_6 foreign key (LEVELID)
  references LEVEL_DIM (LEVELID);
alter table STFD_FACT
  add constraint FK_STFD_FACT_7 foreign key (CUST_PROD_ID)
  references CUST_PRODUCT_LINK (CUST_PROD_ID);
-- Create/Recreate indexes 
create index IDX_STFD1 on STFD_FACT (ORG_NODEID, CUST_PROD_ID, ADMINID, GRADEID, ISPUBLIC, SUBTESTID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

 -- Objective Constraints -- 
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_1 FOREIGN KEY (STUDENT_BIO_ID)
  REFERENCES STUDENT_BIO_DIM (STUDENT_BIO_ID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_10 FOREIGN KEY (FORMID)
  REFERENCES FORM_DIM (FORMID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_11 FOREIGN KEY (ORG_NODEID)
  REFERENCES ORG_NODE_DIM (ORG_NODEID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_12 FOREIGN KEY (ASSESSMENTID)
  REFERENCES ASSESSMENT_DIM (ASSESSMENTID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_13 FOREIGN KEY (CONDCODE_ID)
  REFERENCES CONDITION_CODES (CONDCODE_ID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_2 FOREIGN KEY (CUST_PROD_ID)
  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_3 FOREIGN KEY (CONTENTID)
  REFERENCES CONTENT_DIM (CONTENTID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_4 FOREIGN KEY (SUBTESTID)
  REFERENCES SUBTEST_DIM (SUBTESTID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_5 FOREIGN KEY (ADMINID)
  REFERENCES ADMIN_DIM (ADMINID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_6 FOREIGN KEY (GRADEID)
  REFERENCES GRADE_DIM (GRADEID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_7 FOREIGN KEY (GENDERID)
  REFERENCES GENDER_DIM (GENDERID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_8 FOREIGN KEY (OBJECTIVEID)
  REFERENCES OBJECTIVE_DIM (OBJECTIVEID);
ALTER TABLE OBJECTIVE_SCORE_FACT
  ADD CONSTRAINT FK_OBJECTIVE_SCORE_FACT_9 FOREIGN KEY (LEVELID)
  REFERENCES LEVEL_DIM (LEVELID); 
  
  
 alter table OBJECTIVE_SCORE_FACT
add constraint PK_OBJECTIVE_SCORE_FACT PRIMARY KEY (OBJECTIVE_FACTID);  

--Objective Fact Indexes -- 
create bitmap index BIDX_OBJECTIVE_SCORE_FACT_1 on OBJECTIVE_SCORE_FACT (CUST_PROD_ID) local;
create bitmap index BIDX_OBJECTIVE_SCORE_FACT_2 on OBJECTIVE_SCORE_FACT (STUDENT_BIO_ID) local;
create bitmap index BIDX_OBJECTIVE_SCORE_FACT_3 on OBJECTIVE_SCORE_FACT (SUBTESTID) local;
create bitmap index BIDX_OBJECTIVE_SCORE_FACT_4 on OBJECTIVE_SCORE_FACT (OBJECTIVEID) local;
create bitmap index BIDX_OBJECTIVE_SCORE_FACT_5 on OBJECTIVE_SCORE_FACT (GRADEID) local;
create bitmap index BIDX_OBJECTIVE_SCORE_FACT_6 on OBJECTIVE_SCORE_FACT (ADMINID) local;

--Subtest Score Fact Indexes -- 
DROP INDEX IDX_SUBTEST_SCORE_FACT_1; 
create bitmap index BIDX_SUBTEST_SCORE_FACT_1 on SUBTEST_SCORE_FACT (CUST_PROD_ID);
create bitmap index BIDX_SUBTEST_SCORE_FACT_2 on SUBTEST_SCORE_FACT (STUDENT_BIO_ID);
create bitmap index BIDX_SUBTEST_SCORE_FACT_3 on SUBTEST_SCORE_FACT (SUBTESTID);
create bitmap index BIDX_SUBTEST_SCORE_FACT_4 on SUBTEST_SCORE_FACT (GRADEID);
create bitmap index BIDX_SUBTEST_SCORE_FACT_5 on SUBTEST_SCORE_FACT (ADMINID); 

--Invitation Code Indexes -- 
DROP INDEX IDX_INVITATION_CODE_1; 
DROP INDEX IDX_INVITATION_CODE_2;
DROP INDEX IDX_INVITATION_CODE_3;
DROP INDEX IDX_INVITATION_CODE_4;   
create index IDX_INVITATION_CODE_1 on INVITATION_CODE (ORG_NODEID, GRADE_ID, CUST_PROD_ID, ACTIVATION_STATUS, IS_NEW_IC) ;
create index IDX_INVITATION_CODE_2 on INVITATION_CODE (TEST_ELEMENT_ID, GRADE_ID, CUST_PROD_ID, ACTIVATION_STATUS, IS_NEW_IC) ;
create index IDX_INVITATION_CODE_3 on INVITATION_CODE (STUDENT_BIO_ID, CUST_PROD_ID) ;
create index IDX_INVITATION_CODE_4 on INVITATION_CODE (INT_STUDENT_ID, CUST_PROD_ID) ; 

-- Invitation Code Claim -- 

DROP INDEX IDX_INVITATION_CODE_CLAIM_1 ; 

create index IDX_INVITATION_CODE_CLAIM_1 on INVITATION_CODE_CLAIM (ORG_USER_ID,ICID,  ACTIVATION_STATUS) ;

--Student PDF Files 
DROP INDEX IDX_STUDENT_PDF_FILES; 
create index IDX_STUDENT_PDF_FILES on STUDENT_PDF_FILES (PDF_REPORTID, STUDENT_BIO_ID); 

-- Org User Index 
DROP INDEX IDX_ORG_USERS_2; 
create index IDX_ORG_USERS_2 on ORG_USERS (ORG_NODEID, ORG_USER_ID); 

--16-- Mview Creation -- 

CREATE TABLE UDTR_ROSTER_TEST_SESS_CNT_bkp AS SELECT * FROM UDTR_ROSTER_TEST_SESSION_CNT; 

DROP TABLE UDTR_ROSTER_TEST_SESSION_CNT; 

CREATE MATERIALIZED VIEW UDTR_ROSTER_TEST_SESSION_CNT
REFRESH COMPLETE ON DEMAND  
AS
SELECT 1 AS STUDENT_DETAILS,
       UDTR.ORG_NODEID,
       UDTR.GRADEID,
       UDTR.CUST_PROD_ID,
       UDTR.ADMINID , 
       UDTR.ISPUBLIC,
       COUNT(UDTR.ELA1_TEST_SESSION_NAME) AS ELA1_TEST_SESSION_NAME_CNT, 
       COUNT(UDTR.ELA2_TEST_SESSION_NAME) AS ELA2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA3_TEST_SESSION_NAME) AS ELA3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA4_TEST_SESSION_NAME) AS ELA4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA5_TEST_SESSION_NAME) AS ELA5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA6_TEST_SESSION_NAME) AS ELA6_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH1_TEST_SESSION_NAME) AS MATH1_TEST_SESSION_NAME_CNT ,
       COUNT(UDTR.MATH2_TEST_SESSION_NAME) AS MATH2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH3_TEST_SESSION_NAME) AS MATH3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH4_TEST_SESSION_NAME) AS MATH4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH5_TEST_SESSION_NAME) AS MATH5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE1_TEST_SESSION_NAME) AS SCIENCE1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE2_TEST_SESSION_NAME) AS SCIENCE2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE3_TEST_SESSION_NAME) AS SCIENCE3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE4_TEST_SESSION_NAME) AS SCIENCE4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL1_TEST_SESSION_NAME) AS SOCIAL1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL2_TEST_SESSION_NAME) AS SOCIAL2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL3_TEST_SESSION_NAME) AS SOCIAL3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL4_TEST_SESSION_NAME) AS SOCIAL4_TEST_SESSION_NAME_CNT,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA2_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA3_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA4_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA5_TESTRESULT = 'Test Not Taken'
               OR UDTR.ELA6_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH1_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH2_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH3_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH4_TESTRESULT = 'Test Not Taken'
               OR UDTR.MATH5_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE1_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE2_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE3_TESTRESULT = 'Test Not Taken'
               OR UDTR.SCIENCE4_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL1_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL2_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL3_TESTRESULT = 'Test Not Taken'
               OR UDTR.SOCIAL4_TESTRESULT = 'Test Not Taken' 
             THEN 1
             ELSE 0
          END) AS TEST_NOT_TAKEN ,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA2_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA3_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA4_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA5_TESTRESULT = 'Test Not Received'
               OR UDTR.ELA6_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH1_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH2_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH3_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH4_TESTRESULT = 'Test Not Received'
               OR UDTR.MATH5_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE1_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE2_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE3_TESTRESULT = 'Test Not Received'
               OR UDTR.SCIENCE4_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL1_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL2_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL3_TESTRESULT = 'Test Not Received'
               OR UDTR.SOCIAL4_TESTRESULT = 'Test Not Received' 
             THEN 1
             ELSE 0
          END) AS TEST_NOT_RECEIVED,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA2_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA3_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA4_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA5_TESTRESULT = 'Valid Attempt'
               OR UDTR.ELA6_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH1_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH2_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH3_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH4_TESTRESULT = 'Valid Attempt'
               OR UDTR.MATH5_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE1_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE2_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE3_TESTRESULT = 'Valid Attempt'
               OR UDTR.SCIENCE4_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL1_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL2_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL3_TESTRESULT = 'Valid Attempt'
               OR UDTR.SOCIAL4_TESTRESULT = 'Valid Attempt' 
             THEN 1
             ELSE 0
          END) AS VALID_ATTEMPT,
       SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA2_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA3_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA4_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA5_TESTRESULT = 'Invalid by School'
               OR UDTR.ELA6_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH1_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH2_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH3_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH4_TESTRESULT = 'Invalid by School'
               OR UDTR.MATH5_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE1_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE2_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE3_TESTRESULT = 'Invalid by School'
               OR UDTR.SCIENCE4_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL1_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL2_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL3_TESTRESULT = 'Invalid by School'
               OR UDTR.SOCIAL4_TESTRESULT = 'Invalid by School' 
             THEN 1
             ELSE 0
          END) AS INVALID_BY_SCHOOL ,
      SUM(
        CASE WHEN UDTR.ELA1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA4_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA5_TESTRESULT = 'IMAST Partic.'
               OR UDTR.ELA6_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH4_TESTRESULT = 'IMAST Partic.'
               OR UDTR.MATH5_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SCIENCE4_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL1_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL2_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL3_TESTRESULT = 'IMAST Partic.'
               OR UDTR.SOCIAL4_TESTRESULT = 'IMAST Partic.' 
             THEN 1
             ELSE 0
          END) AS IMAST_PARTIC  
FROM UDTR_ROSTER_FACT UDTR,
     CUST_PRODUCT_LINK CUST
WHERE  UDTR.CUST_PROD_ID = CUST.CUST_PROD_ID
  AND UDTR.ADMINID = CUST.ADMINID
GROUP BY UDTR.ORG_NODEID,
         UDTR.GRADEID,
         UDTR.CUST_PROD_ID,
         UDTR.ADMINID ,
         UDTR.ISPUBLIC 
UNION
SELECT  0 AS STUDENT_DETAILS,
       UDTR.ORG_NODEID,
       UDTR.GRADEID,
       UDTR.CUST_PROD_ID,
       UDTR.ADMINID , 
       UDTR.ISPUBLIC,
       COUNT(UDTR.ELA1_TEST_SESSION_NAME) AS ELA1_TEST_SESSION_NAME_CNT, 
       COUNT(UDTR.ELA2_TEST_SESSION_NAME) AS ELA2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA3_TEST_SESSION_NAME) AS ELA3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA4_TEST_SESSION_NAME) AS ELA4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA5_TEST_SESSION_NAME) AS ELA5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.ELA6_TEST_SESSION_NAME) AS ELA6_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH1_TEST_SESSION_NAME) AS MATH1_TEST_SESSION_NAME_CNT ,
       COUNT(UDTR.MATH2_TEST_SESSION_NAME) AS MATH2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH3_TEST_SESSION_NAME) AS MATH3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH4_TEST_SESSION_NAME) AS MATH4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.MATH5_TEST_SESSION_NAME) AS MATH5_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE1_TEST_SESSION_NAME) AS SCIENCE1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE2_TEST_SESSION_NAME) AS SCIENCE2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE3_TEST_SESSION_NAME) AS SCIENCE3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SCIENCE4_TEST_SESSION_NAME) AS SCIENCE4_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL1_TEST_SESSION_NAME) AS SOCIAL1_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL2_TEST_SESSION_NAME) AS SOCIAL2_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL3_TEST_SESSION_NAME) AS SOCIAL3_TEST_SESSION_NAME_CNT,
       COUNT(UDTR.SOCIAL4_TEST_SESSION_NAME) AS SOCIAL4_TEST_SESSION_NAME_CNT  ,
       -1 AS TEST_NOT_TAKEN ,
       -1 AS TEST_NOT_RECEIVED,
       -1 AS VALID_ATTEMPT,
          -1 AS INVALID_BY_SCHOOL ,
          -1 AS IMAST_PARTIC
FROM UDTR_SUMM_FACT UDTR,
     CUST_PRODUCT_LINK CUST
WHERE UDTR.CUST_PROD_ID = CUST.CUST_PROD_ID
  AND UDTR.ADMINID = CUST.ADMINID
  AND NOT EXISTS (SELECT 1 FROM UDTR_ROSTER_FACT UDT
                     WHERE UDT.ORG_NODEID =UDTR.ORG_NODEID
                       AND UDT.GRADEID = UDTR.GRADEID 
                       AND UDT.CUST_PROD_ID = UDTR.CUST_PROD_ID 
                       AND UDT.ADMINID = UDTR.ADMINID
                       AND UDT.ISPUBLIC =UDTR.ISPUBLIC
                                            )
GROUP BY UDTR.ORG_NODEID,
         UDTR.GRADEID,
         UDTR.CUST_PROD_ID,
         UDTR.ADMINID ,
         UDTR.ISPUBLIC ;

--
drop index IDX_VW_SUB_GRD_OBJ_MAP_1;
drop index IDX_VW_SUB_GRD_OBJ_MAP_2;
drop index IDX_VW_SUB_GRD_OBJ_MAP_3;

rename VW_SUBTEST_GRADE_OBJECTIVE_MAP to VW_SUBTEST_GRADE_OBJECTIVE_BKP;

CREATE MATERIALIZED VIEW VW_SUBTEST_GRADE_OBJECTIVE_MAP
REFRESH COMPLETE ON DEMAND
AS
SELECT A.ASSESSMENTID,
       A.ASSESSMENT_NAME,
       G.GRADE_CODE,
       G.GRADEID,
       G.GRADE_NAME,
       L.LEVELID,
       L.LEVEL_NAME,
       F.FORMID,
       F.FORM_NAME,
     F.FORM_CODE,
       C.CONTENTID,
       C.CONTENT_NAME,
       C.CONTENT_SEQ,
     S.SUBTESTID,
       S.SUBTEST_NAME,
       S.SUBTEST_SEQ,
     S.SUBTEST_CODE,
       O.OBJECTIVEID,
       O.OBJECTIVE_NAME,
       O.OBJECTIVE_SEQ,
     O.OBJECTIVE_CODE,
       'MASTERY_INDICATOR_'||ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) MAST_IND,
       'OPIIPI_'||ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) OPI_IPI,
       ROW_number() over (PARTITION BY g.gradeid, A.ASSESSMENTID ORDER BY O.OBJECTIVE_SEQ ) AS rn
  FROM CONTENT_DIM           C,
       SUBTEST_DIM           S,
       OBJECTIVE_DIM         O,
       SUBTEST_OBJECTIVE_MAP SOM,
       LEVEL_MAP             LM,
       LEVEL_DIM             L,
       FORM_DIM              F,
       GRADE_DIM             G,
       GRADE_LEVEL_MAP       GM,
       ASSESSMENT_DIM        A
 WHERE A.ASSESSMENTID = C.ASSESSMENTID
   AND C.CONTENTID = S.CONTENTID
   AND SOM.SUBTESTID = S.SUBTESTID
   AND SOM.OBJECTIVEID = O.OBJECTIVEID
   AND SOM.LEVEL_MAPID = LM.LEVEL_MAPID
   AND L.LEVELID = LM.LEVELID
   AND F.FORMID = LM.FORMID
   AND G.GRADEID = GM.GRADEID
   AND LM.LEVEL_MAPID = GM.LEVEL_MAPID;
   
create index IDX_VW_SUB_GRD_OBJ_MAP_1 on VW_SUBTEST_GRADE_OBJECTIVE_MAP (GRADEID, SUBTESTID, OBJECTIVEID);
create index IDX_VW_SUB_GRD_OBJ_MAP_2 on VW_SUBTEST_GRADE_OBJECTIVE_MAP (GRADEID, SUBTESTID, OBJECTIVEID, LEVELID);
create index IDX_VW_SUB_GRD_OBJ_MAP_3 on VW_SUBTEST_GRADE_OBJECTIVE_MAP (GRADEID, SUBTESTID, OBJECTIVE_NAME);  		 

-- Job Tracking Comment 
COMMENT ON COLUMN job_tracking.extract_category IS 'TTD (Test Taken Date), PD (Processed Date), AE (Auto Extract)' ;
COMMENT ON COLUMN job_tracking.Extract_Filetype is 'DAT (Student Data File in DAT format), XML (Student Data File in XML format), ICL (Invitation Code Letter), ISR (Iindividual Student Report), IPR (Image Print Report), BOTH (ISR & IPR), CR (Candidate Report)' ;
COMMENT ON COLUMN job_tracking.Request_Type is 'SDF (Student Data File), SBE (Student Bio Extract), GDF (Group Download Files)' ;
COMMENT ON COLUMN job_tracking.Job_Status is 'SU (Submitted), IP (In-Progress), CO (Completed),  ER (Error),  FT (File Transfer), DE (Deleted), AR (Archived)' ;


--17-- Org Users Changes -- 

DELETE   FROM users  a 
 WHERE USERID IN
       (SELECT USERID
          FROM USERS
         WHERE USERNAME IN (SELECT USER_ID FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER)
           AND NOT EXISTS (SELECT 1
                  FROM EXCEP_USERS_NOT_IN_PRISM2@PROD_TO_ISTEPMASTER B
                 WHERE USERS.USERID = B.USERID))  ;   
                
        DELETE FROM pwd_hint_answers WHERE userid IN   (SELECT USERID
          FROM USERS
         WHERE USERNAME IN (SELECT USER_ID FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER)
           AND NOT EXISTS (SELECT 1
                  FROM EXCEP_USERS_NOT_IN_PRISM2@PROD_TO_ISTEPMASTER B
                 WHERE USERS.USERID = B.USERID))  ;                
  
DELETE   FROM org_USERS a 
 WHERE USERID IN
       (SELECT USERID
          FROM USERS
         WHERE USERNAME IN (SELECT USER_ID FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER)
           AND NOT EXISTS (SELECT 1
                  FROM EXCEP_USERS_NOT_IN_PRISM2@PROD_TO_ISTEPMASTER B
                 WHERE USERS.USERID = B.USERID));


DELETE 
  FROM USER_ROLE D
 WHERE D.USERID IN
       (SELECT u.USERID
          FROM USERS u 
         WHERE USERNAME IN (SELECT USER_ID FROM TEMP_PARENT_USERS2@PROD_TO_ISTEPMASTER)
           AND NOT EXISTS (SELECT 1
                  FROM EXCEP_USERS_NOT_IN_PRISM2@PROD_TO_ISTEPMASTER B
                 WHERE u.USERID = B.USERID))   ; 
                 
BEGIN 
PKG_INORS_MIGRATION.proc_parent_user_load_excep;
END ;     

UPDATE ORG_USERS
   SET ORG_NODE_LEVEL = 3
 WHERE USERID IN (
                  
                  SELECT U.USERID USERID /*,
                  u.USERNAME */
                    FROM USERS U, ORG_USERS OU
                  
                   WHERE U.USERID = OU.USERID
                     AND ORG_NODE_LEVEL = 0
                     AND EXISTS (SELECT 1
                            FROM USER_ROLE UR
                           WHERE UR.USERID = U.USERID
                             AND ROLEID = 6));
                             
  DELETE FROM INVITATION_CODE_CLAIM
   WHERE ORG_USER_ID IN
         (SELECT ORG_USER_ID
            FROM ORG_USERS
           WHERE (USERID, ORG_NODEID) IN
                 (SELECT USERID, PARENT_ORG_NODEID
                    FROM NOT_IN_INORS_PRESENT_PRISM1@PROD_TO_ISTEPMASTER));
  
  DELETE FROM ORG_USERS
   WHERE (USERID, ORG_NODEID) IN
         (SELECT USERID, PARENT_ORG_NODEID
            FROM NOT_IN_INORS_PRESENT_PRISM1@PROD_TO_ISTEPMASTER); 
            
            
            
               
  UPDATE users 
  SET display_username =substr(display_username,1,10), 
      activation_status ='IN' , 
       IS_FIRSTTIME_LOGIN ='Y'
   WHERE USERID IN (
                  
                  SELECT U.USERID USERID /*,
                  u.USERNAME */
                    FROM USERS U, ORG_USERS OU
                  
                   WHERE U.USERID = OU.USERID
                     AND ORG_NODE_LEVEL = 3
                     AND EXISTS (SELECT 1
                            FROM USER_ROLE UR
                           WHERE UR.USERID = U.USERID
                             AND ROLEID = 6));      
                             
  UPDATE     users 
  SET    activation_status ='IN' 
  WHERE upper(username)  IN (  SELECT upper(user_id) user_id  FROM TEMP_PARENT_USERS    ) 
  AND activation_status ='AC' ;
  
  UPDATE users a 
  SET  activation_status ='AC' 
  WHERE EXISTS (SELECT 1 FROM vw_parent_users2@PROD_TO_ISTEPMASTER  b 
                 WHERE upper(a.username)  = upper(b.username)   );
				 
--18-- Update student_fullname  

MERGE INTO INVITATION_CODE I
USING (SELECT ICID,
              STUDENT_FULL_NAME,
              STUDENT_LAST_NAME,
              STUDENT_FIRST_NAME,
              STUDENT_MIDDLE_NAME,
              FULL_NAME
         FROM (SELECT IC.ICID,
                      IC.STUDENT_FULL_NAME,
                      S.LAST_NAME AS STUDENT_LAST_NAME,
                      S.FIRST_NAME AS STUDENT_FIRST_NAME,
                      S.MIDDLE_NAME AS STUDENT_MIDDLE_NAME,
                      CASE
                        WHEN S.MIDDLE_NAME IS NULL THEN
                         TRIM(S.LAST_NAME) || ', ' || TRIM(S.FIRST_NAME)
                        ELSE
                         TRIM(S.LAST_NAME) || ', ' || TRIM(S.FIRST_NAME) || ' ' ||
                         TRIM(S.MIDDLE_NAME)
                      END AS FULL_NAME
                 FROM INVITATION_CODE IC, STUDENT_BIO_DIM S
                WHERE IC.STUDENT_BIO_ID = S.STUDENT_BIO_ID)
        WHERE STUDENT_FULL_NAME <> FULL_NAME) E
ON (I.ICID = E.ICID)
WHEN MATCHED THEN
  UPDATE SET I.STUDENT_FULL_NAME = E.FULL_NAME;   	

  
--19-- Asfd_orderby Changes -- 
CREATE TABLE ASFD_ORDERBY_BKP_051614 AS SELECT * FROM ASFD_ORDERBY;
TRUNCATE TABLE ASFD_ORDERBY;
--SELECT COUNT(1) FROM ASFD_ORDERBY;
--INSERT INTO ASFD_ORDERBY SELECT * FROM ISTEP_DEV_ETL.ASFD_ORDERBY;
INSERT INTO ASFD_ORDERBY 
SELECT ORDR.ASFD_ORDERBYID,
        ORDR.OBJECTIVEID,
        ORDR.OBJECTIVE_NAME,
        ORDR.GRADEID,
        ORDR.SUBTESTID,
        ORDR.ORDERBY,
        DECODE (ORDR.CUST_PROD_ID,5017,5024,ORDR.CUST_PROD_ID) AS CUST_PROD_ID
FROM ASFD_ORDERBY@PROD_TO_ISTEPMASTER ORDR;


--20-- Dash Messages -- 
DECLARE
  l_query Varchar2(32000);
  l_timestamp Varchar2(32000);
BEGIN 
   SELECT '_BKP_'||TO_CHAR(SYSDATE,'MMDDYY')INTO l_timestamp FROM DUAL;
   
   l_query:='CREATE TABLE DASH_MESSAGES'||l_timestamp||' AS  SELECT * FROM DASH_MESSAGES';
    EXECUTE IMMEDIATE l_query;
    
   l_query:='CREATE TABLE DASH_MESSAGE_TYPE'||l_timestamp||' AS  SELECT * FROM DASH_MESSAGE_TYPE';
   EXECUTE IMMEDIATE l_query; 
END;

TRUNCATE TABLE DASH_MESSAGES;
TRUNCATE TABLE DASH_MESSAGE_TYPE;

INSERT INTO DASH_MESSAGE_TYPE 
SELECT MSG_TYP.MSG_TYPEID,
       MSG_TYP.MESSAGE_NAME,
       MSG_TYP.MESSAGE_TYPE,
       MSG_TYP.DESCRIPTION,
       DECODE(MSG_TYP.CUST_PROD_ID,5017,5024,MSG_TYP.CUST_PROD_ID) AS CUST_PROD_ID,
       MSG_TYP.CREATED_DATE_TIME,
       MSG_TYP.UPDATED_DATE_TIME 
FROM DASH_MESSAGE_TYPE@PROD_TO_ISTEPMASTER MSG_TYP
WHERE MSG_TYP.CUST_PROD_ID <> 5018;
INSERT INTO DASH_MESSAGES 
SELECT  MSG.DB_REPORTID,
        MSG.MSG_TYPEID,
        MSG.REPORT_MSG,
        DECODE(MSG.CUST_PROD_ID,5017,5024,MSG.CUST_PROD_ID) AS CUST_PROD_ID,
        MSG.ACTIVATION_STATUS,
        MSG.CREATED_DATE_TIME,
        MSG.UPDATED_DATE_TIME
FROM DASH_MESSAGES@PROD_TO_ISTEPMASTER MSG
WHERE MSG.CUST_PROD_ID <> 5018;

--SELECT MAX(MSG_TYPEID) FROM DASH_MESSAGE_TYPE; --1399+5=1404

DROP SEQUENCE RPTMSGTYPE_SEQ;

CREATE SEQUENCE RPTMSGTYPE_SEQ
MINVALUE 1086
MAXVALUE 9999999999999999999999999999
START WITH 1404
INCREMENT BY 1
CACHE 20;

DROP SEQUENCE DASH_MESSAGE_SEQ;

CREATE SEQUENCE DASH_MESSAGE_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
START WITH 1098
INCREMENT BY 1
CACHE 20;


--21-- Article Meatadata Changes -- 

INSERT INTO ARTICLE_CONTENT
SELECT AC.ARTICLE_CONTENT_ID,
        AC.ARTICLE_CONTENT,
        AC.DESCRIPTION,
        AC.OBJECTIVEID,
        AC.CREATED_DATE_TIME,
        AC.UPDATED_DATE_TIME
FROM ARTICLE_CONTENT@PROD_TO_ISTEPMASTER AC;
INSERT INTO ARTICLE_METADATA 
SELECT  AM.ARTICLEID,
        AM.ARTICLE_NAME,
        DECODE(AM.CUST_PROD_ID,5017,5024,AM.CUST_PROD_ID) AS CUST_PROD_ID,
        AM.SUBT_OBJ_MAPID,
        AM.SUBTESTID,
        AM.ARTICLE_CONTENT_ID,
        AM.CATEGORY,
        AM.CATEGORY_TYPE,
        AM.CATEGORY_SEQ,
        AM.SUB_HEADER,
        AM.DESCRIPTION,
        AM.GRADEID,
        AM.PROFICIENCY_LEVEL,
        AM.RESOLVED_RPRT_STATUS,
        AM.CREATED_DATE_TIME,
        AM.UPDATED_DATE_TIME 
FROM ARTICLE_METADATA@PROD_TO_ISTEPMASTER AM;

--SELECT DISTINCT CUST_PROD_ID FROM ARTICLE_METADATA;
--SELECT COUNT(1) FROM article_content;--9213
--SELECT COUNT(1) FROM article_metadata;--9207

--SELECT MAX(ARTICLEID)  FROM ARTICLE_METADATA;16002=5=16007
DROP SEQUENCE ARTICLE_METADATA_SEQ;

CREATE SEQUENCE ARTICLE_METADATA_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
START WITH 16007
INCREMENT BY 1
NOCACHE;

--SELECT MAX(ARTICLE_CONTENT_ID)  FROM ARTICLE_CONTENT; 9484+5=9489

DROP SEQUENCE ARTICLE_CONTENT_SEQ;
CREATE SEQUENCE ARTICLE_CONTENT_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
START WITH 9489
INCREMENT BY 1
NOCACHE;
  
  
  
--22-- Corrected report seq for 201X Spring Interpretive Guide -- 
 
--backup
create table dash_menu_rpt_access_bkp051714 as
(select * from dash_menu_rpt_access);
--mannual process 
select * from dash_menu_rpt_access where db_reportid in  (2046,2049,2048,2047,10000);


-- 23 -- Change REQUEST_FILENAME length in job_tracking
alter table job_tracking modify REQUEST_FILENAME varchar2(500);

CREATE INDEX idx_job_tracking_1 ON job_tracking(userid,job_status);


-- 24 -- Create sequence ACTIVITYID_SEQ
create sequence ACTIVITYID_SEQ
minvalue 1
maxvalue 999999999999
start with 10941
increment by 1
cache 20;


-- 25 -- Insert data in activity_type

prompt PL/SQL Developer import file
prompt Created on Saturday, May 17, 2014 by 233208
set feedback off
set define off
prompt Disabling triggers for ACTIVITY_TYPE...
alter table ACTIVITY_TYPE disable all triggers;
prompt Deleting ACTIVITY_TYPE...
delete from ACTIVITY_TYPE;
commit;
prompt Loading ACTIVITY_TYPE...
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1, 'Login', 'Login', 'Login Activity Tracking', to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'));
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (2, 'Reports', 'Report', 'Report URL Tracking', to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-04-2014 08:15:16', 'dd-mm-yyyy hh24:mi:ss'));
insert into ACTIVITY_TYPE (ACTY_TYPEID, ACTIVITY_NAME, ACTIVITY_TYPE, DESCRIPTION, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (3, 'Admin', 'Admin', 'User Admin URL Tracking', to_date('10-04-2014 08:15:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-04-2014 08:15:17', 'dd-mm-yyyy hh24:mi:ss'));
commit;
prompt 3 records loaded
prompt Enabling triggers for ACTIVITY_TYPE...
alter table ACTIVITY_TYPE enable all triggers;
set feedback on
set define on
prompt Done. 

-- 26-- ETL Related Changes -- 

create or replace procedure PRC_MV_REFRESH_UDTR_ROSTER
as
begin

 DBMS_MVIEW.REFRESH('UDTR_ROSTER_TEST_SESSION_CNT', 'C');

end PRC_MV_REFRESH_UDTR_ROSTER;

create table STG_STATE_MEAN_IPI_SCORE
(
  STG_STATE_MEAN_IPI_SCORE_ID                      NUMBER not null,
  ORG_NODEID                   NUMBER not null,
  CUST_PROD_ID                 NUMBER not null,
  ADMINID                      NUMBER not null,
  ENGLANG_ARTS_SUBTESTID       NUMBER,
  MATHEMATICS_SUBTESTID        NUMBER,
  SCIENCE_SUBTESTID            NUMBER,
  SOCIAL_SUBTESTID             NUMBER,
  GRADEID                      NUMBER not null,
  LEVELID                      NUMBER not null,  
  MEAN_IPI_1                   VARCHAR2(5),
  MEAN_IPI_2                   VARCHAR2(5),
  MEAN_IPI_3                   VARCHAR2(5),
  MEAN_IPI_4                   VARCHAR2(5),
  MEAN_IPI_5                   VARCHAR2(5),
  MEAN_IPI_6                   VARCHAR2(5),
  MEAN_IPI_7                   VARCHAR2(5),
  MEAN_IPI_8                   VARCHAR2(5),
  MEAN_IPI_9                   VARCHAR2(5),
  MEAN_IPI_10                  VARCHAR2(5),
  MEAN_IPI_11                  VARCHAR2(5),
  MEAN_IPI_12                  VARCHAR2(5),
  MEAN_IPI_13                  VARCHAR2(5),
  MEAN_IPI_14                  VARCHAR2(5),
  MEAN_IPI_15                  VARCHAR2(5),
  MEAN_IPI_16                  VARCHAR2(5),
  MEAN_IPI_17                  VARCHAR2(5),
  MEAN_IPI_18                  VARCHAR2(5),
  MEAN_IPI_19                  VARCHAR2(5),
  MEAN_IPI_20                  VARCHAR2(5),
  MEAN_IPI_21                  VARCHAR2(5),
  MEAN_IPI_22                  VARCHAR2(5),
  MEAN_IPI_23                  VARCHAR2(5),
  MEAN_IPI_24                  VARCHAR2(5),
  MEAN_IPI_25                  VARCHAR2(5),
  MEAN_IPI_26                  VARCHAR2(5),
  MEAN_IPI_27                  VARCHAR2(5),
  MEAN_IPI_28                  VARCHAR2(5),
  MEAN_IPI_29                  VARCHAR2(5),
  MEAN_IPI_30                  VARCHAR2(5),
  MEAN_IPI_31                  VARCHAR2(5),
  STRUCTURE_LEVEL              VARCHAR2(2),
  ELEMENT_NUMBER               VARCHAR2(7),
  ISPUBLIC                     VARCHAR2(20),
  DATETIMESTAMP                DATE not null
);

CREATE OR REPLACE TYPE INT_ARRAY AS TABLE OF NUMBER;


create sequence SEQ_STATE_MEAN_IPI_SCORE
minvalue 1
maxvalue 999999999999999999999999999
start with 3095
increment by 1
cache 20; 

-- Packages & Procedure to be copied (ETL)-- 

1- PKG_INORS_CREATE_CUSTOMER 
2- PRC_DELETE_RESCORE_FACT  

--Packages & Procedure to be copied (OPR) : 

1-PKG_MANAGE_CONTENT.pck
2-PKG_PARENT_NETWORK.pck 
3-PKG_GET_MIG_RESULTS_GRT.pck 

-- Partition Script -- 

Note- Please make sure that the cust_prod_id is validated, it should be the cust_prod_id for ISTEP+ Spring 2014 product customer 1000 

ALTER TABLE DISA_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE CLASS_SUMM_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE ASFD_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE MEDIA_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE SUMT_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE UDTR_ROSTER_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE UDTR_SUMM_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE PEID_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE DISAGGREGATION_CATEGORY SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE DISAGGREGATION_CATEGORY_TYPE SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE ACAD_STD_SUMM_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE SPPR_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE RESULTS_GRT_FACT SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE STFD_FACT  SPLIT PARTITION PART_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OTHERS );

ALTER TABLE OBJECTIVE_SCORE_FACT SPLIT PARTITION PART_OBJ_OTHERS  VALUES (5024)
  INTO ( PARTITION PART_P5024, PARTITION PART_OBJ_OTHERS );



 


 