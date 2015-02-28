--NY_DATA_STUDENT_IMPORT
create table NY_DATA_STUDENT_IMPORT
(
  EXT_STUDENT_ID VARCHAR2(20),
  FIRST_NAME     VARCHAR2(32),
  LAST_NAME      VARCHAR2(32),
  MIDDLE_NAME    VARCHAR2(32),
  BIRTHDATE      VARCHAR2(15),
  GENDER         VARCHAR2(10),
  EDU_CENTERID   NUMBER,
  TEST_DATE      VARCHAR2(15)
);

--STG_STUD_DOWNLOAD_DATA_LAYOUT
create table STG_STUD_DOWNLOAD_DATA_LAYOUT
(
  SEQ_NO                 NUMBER not null,
  CUSTOMER_DEFINED_FIELD VARCHAR2(1000),
  COLUMN_NAME            VARCHAR2(1000),
  START_POS              NUMBER,
  END_POS                NUMBER,
  LENGTH                 NUMBER,
  HEADER                 VARCHAR2(1000)
);

--STUDENT_CHECK
create table STUDENT_CHECK
(
  IMPORT_DATETIME     DATE,
  EXAMINEE_LAST_NAME  VARCHAR2(100),
  EXAMINEE_FIRST_NAME VARCHAR2(100),
  EXAMINEE_UUID       NUMBER,
  REGISTRATION_DATE   DATE
);