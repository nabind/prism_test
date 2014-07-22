CREATE OR REPLACE TYPE typ_varchar_arr IS TABLE OF VARCHAR2(40);
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE GRW_DUPLICATE_STUDENTS
(
  CNT             NUMBER,
  STUDENT_BIO_ID  NUMBER NOT NULL,
  CUST_PROD_ID    NUMBER NOT NULL,
  STUDENT_TEST_AI VARCHAR2(9),
  ADMINID         NUMBER NOT NULL,
  GRADEID         NUMBER NOT NULL
)
TABLESPACE USERS
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    NEXT 1M
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  );
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE GRW_SUBTEST_SCORE_FACT
(
  ORG_NODEID     NUMBER,
  STUDENT_BIO_ID NUMBER NOT NULL,
  CUST_PROD_ID   NUMBER NOT NULL,
  SUBTESTID      NUMBER NOT NULL,
  SUBTEST_CODE   VARCHAR2(20) NOT NULL,
  GRADEID        NUMBER NOT NULL,
  ADMINID        NUMBER NOT NULL,
  SS             NUMBER,
  PL             VARCHAR2(10),
  DATETIMESTAMP  DATE
)
TABLESPACE USERS
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    NEXT 1M
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  ),
PARTITION BY  LIST (CUST_PROD_ID)
(
  PARTITION PART_C5005 VALUES (5005)
    TABLESPACE USERS
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
    PARTITION PART_C5001 VALUES (5001)
    TABLESPACE USERS
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
  PARTITION PART_OTHERS VALUES (DEFAULT)
    TABLESPACE USERS
    PCTFREE 10
    INITRANS 1
    MAXTRANS 255
    STORAGE
    (
      INITIAL 8M
      NEXT 1M
      MINEXTENTS 1
      MAXEXTENTS UNLIMITED
    ));
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE USERS_MAP
(
  USERID        NUMBER NOT NULL,
  DATETIMESTAMP DATE
)
TABLESPACE USERS
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    NEXT 1M
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  );
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE USC_LINK
(
  ORG_USER_ID    NUMBER,
  CUST_PROD_ID   NUMBER,
  STUDENT_BIO_ID NUMBER NOT NULL,
  SUBTESTID      NUMBER,
  DATETIMESTAMP  DATE
)
TABLESPACE USERS
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    NEXT 1M
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  );
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE USER_SELECTION_LOOKUP
(
  USERID        INTEGER,
  CUST_PROD_ID  NUMBER,
  GRADEID       NUMBER NOT NULL,
  SUBTESTID     NUMBER,
  DATETIMESTAMP DATE
)
TABLESPACE USERS
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    NEXT 1M
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  );
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE GLOBAL TEMPORARY TABLE MATRIX_SELECTION_LOOKUP_1
(
  USER_ID        NUMBER NOT NULL,
  USERNAME       VARCHAR2(30) NOT NULL,
  ADMINID        NUMBER NOT NULL,
  GRADEID        NUMBER NOT NULL,
  SUBTESTID      NUMBER NOT NULL,
  CUST_PROD_ID   NUMBER NOT NULL,
  ORG_NODEID     NUMBER NOT NULL,
  STUDENT_BIO_ID NUMBER NOT NULL
)
ON COMMIT DELETE ROWS;
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE GLOBAL TEMPORARY TABLE MATRIX_SELECTION_LOOKUP_2
(
  USER_ID        NUMBER NOT NULL,
  USERNAME       VARCHAR2(30) NOT NULL,
  ADMINID        NUMBER NOT NULL,
  GRADEID        NUMBER NOT NULL,
  SUBTESTID      NUMBER NOT NULL,
  CUST_PROD_ID   NUMBER NOT NULL,
  ORG_NODEID     NUMBER NOT NULL,
  STUDENT_BIO_ID NUMBER NOT NULL
)
ON COMMIT DELETE ROWS;
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE GLOBAL TEMPORARY TABLE MATRIX_SELECTION_LOOKUP_3
(
  USER_ID        NUMBER NOT NULL,
  USERNAME       VARCHAR2(30) NOT NULL,
  ADMINID        NUMBER NOT NULL,
  GRADEID        NUMBER NOT NULL,
  SUBTESTID      NUMBER NOT NULL,
  CUST_PROD_ID   NUMBER NOT NULL,
  ORG_NODEID     NUMBER NOT NULL,
  STUDENT_BIO_ID NUMBER NOT NULL,
  EXT_STUDENT_ID VARCHAR2(20),
  STDUENT_NAME   VARCHAR2(98)
)
ON COMMIT DELETE ROWS;
---------------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE CHECK_RECS
(
AA     NUMBER,
TYPE1  VARCHAR2(100),
TIMETO DATE
)
TABLESPACE USERS
PCTFREE 10
INITRANS 1
MAXTRANS 255
STORAGE
(
  INITIAL 64K
  NEXT 1M
  MINEXTENTS 1
  MAXEXTENTS UNLIMITED
);
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE PERF_MATRIX_FACT
(
  PERF_MATRIX_ID      NUMBER,
  ORG_NODEID          NUMBER,
  CUST_PROD_ID        NUMBER,
  STUDENT_NAME        VARCHAR2(40),
  CURR_STUDENT_BIO_ID NUMBER(22),
  PREV_STUDENT_BIO_ID NUMBER(22),
  SUBTESTID           NUMBER(22),
  CURR_GRADEID        NUMBER(22),
  PREV_GRADEID        NUMBER(22),
  CURR_ADMINID        NUMBER(22),
  PREV_ADMINID        NUMBER(22),
  CURR_SC             NUMBER(22),
  PREV_SC             NUMBER(22),
  USER_ID             NUMBER(22),
  PERF_LEVEL          VARCHAR2(40),
  DATETIMESTAMP       DATE
)
TABLESPACE PRISM_DATA1
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    NEXT 1M
    MINEXTENTS 1
    MAXEXTENTS UNLIMITED
  );
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE MATERIALIZED VIEW MV_PRINCIPAL_USER_SEL_LOOKUP
                            REFRESH COMPLETE ON DEMAND
                            AS
                            SELECT USR.CUSTOMERID ,
								   ORG.ORG_NODEID,
								   ORG.USERID AS PRINCIPAL_USERID,
								   USR.USERID  AS NORMAL_GRW_USERID,
								   SUBSTR(USR.USERNAME,1,8)  AS NORMAL_GRW_USER_SPN,
								   USL.CUST_PROD_ID,
								   USL.GRADEID,
								   USL.SUBTESTID
							FROM ORG_USERS OUSR, USERS USR,USER_SELECTION_LOOKUP USL,
								 (SELECT USR.USERID,OUSR.ORG_NODEID
								  FROM USERS USR,
									   ORG_USERS OUSR
								  WHERE USER_TYPE = 'GRW_P'
									AND USR.USERID = OUSR.USERID
									AND OUSR.ORG_NODE_LEVEL = 3)ORG
							WHERE OUSR.ORG_NODEID = ORG.ORG_NODEID
							  AND OUSR.USERID = USR.USERID
							  AND USR.USER_TYPE = 'GRW'
							  AND OUSR.USERID = USL.USERID;  
---------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE MATERIALIZED VIEW MV_LVL2_PRCPL_USER_SEL_LOOKUP
                            REFRESH COMPLETE ON DEMAND
                            AS
							SELECT USR.CUSTOMERID ,
								   ORG.PARENT_ORG_NODEID AS DISTRICT_ORG_NODEID,
								   ORGUSR.USERID AS DISTRICT_PRINCIPAL_USERID,
								   ORG.ORG_NODEID AS SCHOOL_ORG_NODEID,
								   USR.USERID  AS NORMAL_GRW_USERID,
								   SUBSTR(USR.USERNAME,1,8)  AS NORMAL_GRW_USER_SPN,
								   USL.CUST_PROD_ID,
								   USL.GRADEID,
								   USL.SUBTESTID
							FROM ORG_USERS OUSR, USERS USR,USER_SELECTION_LOOKUP USL,ORG_NODE_DIM ORG,
								 (SELECT USR.USERID,OUSR.ORG_NODEID
								  FROM USERS USR,
									   ORG_USERS OUSR
								  WHERE USER_TYPE = 'GRW_P'
									AND USR.USERID = OUSR.USERID
									AND OUSR.ORG_NODE_LEVEL = 2)ORGUSR
							WHERE ORG.PARENT_ORG_NODEID  = ORGUSR.ORG_NODEID
							  AND ORG.ORG_NODEID = OUSR.ORG_NODEID
							  AND OUSR.ORG_NODE_LEVEL =3
							  AND OUSR.USERID = USR.USERID
							  AND USR.USER_TYPE = 'GRW'
							  AND OUSR.USERID = USL.USERID;