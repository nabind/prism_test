--STG_PROCESS_STATUS
create table STG_PROCESS_STATUS
(
  PROCESS_ID           NUMBER not null,
  FILE_NAME            VARCHAR2(256),
  SOURCE_SYSTEM        VARCHAR2(100),
  HIER_VALIDATION      VARCHAR2(2),
  BIO_VALIDATION       VARCHAR2(2),
  DEMO_VALIDATION      VARCHAR2(2),
  CONTENT_VALIDATION   VARCHAR2(2),
  OBJECTIVE_VALIDATION VARCHAR2(2),
  ITEM_VALIDATION      VARCHAR2(2),
  WKF_PARTITION_NAME   VARCHAR2(100),
  PROCESS_LOG          VARCHAR2(4000),
  DATETIMESTAMP        DATE default SYSDATE
);
alter table STG_PROCESS_STATUS add constraint PK_STG_PROCESS_STATUS primary key (PROCESS_ID);
alter table STG_PROCESS_STATUS add constraint CHK_STG_PROCESS_STATUS_1 check (HIER_VALIDATION IN ('AC','IN','CO','VA'));
alter table STG_PROCESS_STATUS add constraint CHK_STG_PROCESS_STATUS_2 check (BIO_VALIDATION IN ('AC','IN','CO','VA'));
alter table STG_PROCESS_STATUS add constraint CHK_STG_PROCESS_STATUS_3 check (DEMO_VALIDATION IN ('AC','IN','CO','VA'));
alter table STG_PROCESS_STATUS add constraint CHK_STG_PROCESS_STATUS_4 check (CONTENT_VALIDATION IN ('AC','IN','CO','VA'));
alter table STG_PROCESS_STATUS add constraint CHK_STG_PROCESS_STATUS_5 check (OBJECTIVE_VALIDATION IN ('AC','IN','CO','VA'));
alter table STG_PROCESS_STATUS add constraint CHK_STG_PROCESS_STATUS_6 check (ITEM_VALIDATION IN ('AC','IN','CO','VA'));


--STG_BIO_STUDENT_EXTRACT
create table STG_BIO_STUDENT_EXTRACT
(
  PROCESS_ID           NUMBER(20) not null,
  DISTRICT_NUMBER      VARCHAR2(26),
  DISTRICT_NAME        VARCHAR2(26),
  SCHOOL_NUMBER        VARCHAR2(26),
  SCHOOL_NAME          VARCHAR2(26),
  PRIMARY_STUDENT_ID   VARCHAR2(26),
  SECONDARY_STUDENT_ID VARCHAR2(26),
  PREID_STUDENT_ID     VARCHAR2(26),
  STUDENT_LAST_NAME    VARCHAR2(30),
  STUDENT_FIRST_NAME   VARCHAR2(30),
  STUDENT_MIDDLE_NAME  VARCHAR2(30),
  GENDER               VARCHAR2(1),
  DATE_OF_BIRTH        VARCHAR2(8),
  GRADE                VARCHAR2(2),
  ORG_TP               VARCHAR2(10),
  STRUCTURE_LEVEL      VARCHAR2(2),
  ELEMENT_NUMBER       VARCHAR2(7),
  PP_AS_IMAGING_ID     VARCHAR2(26),
  OAS_AS_IMAGING_ID    VARCHAR2(26),
  TEST_NAME            VARCHAR2(100),
  CREATED_DATE_TIME    DATE default SYSDATE not null
);

create index IDX_STG_BIO_STUDENT_EXTRACT_1 on STG_BIO_STUDENT_EXTRACT (PROCESS_ID);

/*--STG_DATA_LAYOUT_CONFIG
create table STG_DATA_LAYOUT_CONFIG
(
  SEQ_NO             NUMBER not null,
  COLUMN_NAME        VARCHAR2(200),
  START_POSITION     NUMBER,
  END_POSITION       NUMBER,
  LENGTH             NUMBER,
  CATEGORY           VARCHAR2(32),
  CATEGORY_TYPE      VARCHAR2(32),
  CATEGORY_VALUE     VARCHAR2(32),
  DATA_EXTRACT_BLOCK VARCHAR2(32),
  REPLACE_FLAG       VARCHAR2(10),
  REPLACE_VALUE      VARCHAR2(32),
  IS_BIO_UPDATE      VARCHAR2(10),
  DESCRIPTION        VARCHAR2(4000),
  ACTIVATION_STATUS  VARCHAR2(2) not null,
  DATETIMESTAMP      DATE not null,
  PRODUCT_CODE       VARCHAR2(30)
);

alter table STG_DATA_LAYOUT_CONFIG add constraint PK_STG_DATA_LAYOUT_CONFIG primary key (SEQ_NO);
alter table STG_DATA_LAYOUT_CONFIG add constraint CHK_STG_DATA_LAYOUT_CONFIG_1 check (REPLACE_FLAG IN ('Y','N'));
alter table STG_DATA_LAYOUT_CONFIG add constraint CHK_STG_DATA_LAYOUT_CONFIG_2 check (IS_BIO_UPDATE IN ('Y','N'));
alter table STG_DATA_LAYOUT_CONFIG add constraint CHK_STG_DATA_LAYOUT_CONFIG_3 check (ACTIVATION_STATUS IN ('AC','IN'));*/

--STG_ERRF_RESCORE
create table STG_ERRF_RESCORE
(
  PROCESS_ID            NUMBER,
  APPSKILLS_IMAGE_ID    VARCHAR2(52),
  CORPORATION_NAME      VARCHAR2(64),
  TEST_MODULE_ID_PEID   VARCHAR2(10),
  ITEM_NUMBER           VARCHAR2(10),
  ITEM_PART             VARCHAR2(5),
  ORIG_SCORE            VARCHAR2(20),
  ORIG_PL               VARCHAR2(20),
  RPT_ITEM_NUMBER       VARCHAR2(10),
  POINTS_POSSIBLE       NUMBER,
  ELIGIBLE_FOR_RESCORE  VARCHAR2(10),
  CONTENT_AREA          VARCHAR2(20),
  TEST_SESSION_NUMBER   NUMBER,
  ORG_TSTG_PGM          VARCHAR2(64),
  STRUCTURE_LEVEL_CORP  VARCHAR2(10),
  ELEMENT_NUMBER_CORP   VARCHAR2(10),
  CORPORATION_NUMBER    VARCHAR2(10),
  STRUCTURE_LEVEL_SCH   VARCHAR2(10),
  ELEMENT_NUMBER_SCH    VARCHAR2(10),
  SCHOOL_NAME           VARCHAR2(64),
  SCHOOL_NUMBER         VARCHAR2(10),
  GRADE                 VARCHAR2(10),
  STRUCTURE_LEVEL       VARCHAR2(10),
  ELEMENT_NUMBER        VARCHAR2(10),
  STUDENT_LAST_NAME     VARCHAR2(32),
  STUDENT_FIRST_NAME    VARCHAR2(32),
  PRIMARY_STUDENT_ID    VARCHAR2(26),
  TEST_NAME             VARCHAR2(10),
  IS_ROW_VALID          VARCHAR2(5),
  DATA_VALIDATION_ERROR VARCHAR2(4000),
  CREATED_DATE_TIME     DATE default SYSDATE
);

/*--STG_ETL_JOBMASTER
create table STG_ETL_JOBMASTER
(
  MAPPING_NAME        VARCHAR2(80),
  SESSION_NAME        VARCHAR2(80),
  WORKFLOW_NAME       VARCHAR2(80),
  REPOSITORY_SERVICE  VARCHAR2(100),
  INTEGRATION_SERVICE VARCHAR2(100),
  DOMAIN_NAME         VARCHAR2(100),
  CREATED_DATE_TIME   DATE,
  UPDATED_DATE_TIME   DATE
);*/

--STG_HIER_DETAILS
create table STG_HIER_DETAILS
(
  STG_HIERARCHY_DETAILS_ID NUMBER not null,
  ORG_NODE_ID              NUMBER,
  ORG_NAME                 VARCHAR2(100),
  ORG_CODE                 VARCHAR2(100),
  ORG_TYPE                 VARCHAR2(100),
  ORG_LEVEL                NUMBER,
  MAX_HIERARCHY            NUMBER,
  STRC_ELEMENT             VARCHAR2(100),
  SPECIAL_CODES            VARCHAR2(100),
  ORG_MODE                 VARCHAR2(100),
  PARENT_ORG_CODE          VARCHAR2(100),
  PARENT_ORG_LEVEL         NUMBER, 
  ORG_NODE_CODE_PATH       VARCHAR2(200),
  EMAIL                    VARCHAR2(100),
  ADMINID                  NUMBER,
  ORGTP                    VARCHAR2(100),
  CUSTOMER_ID              NUMBER,
  PRODUCT_ID               NUMBER,
  PROCESS_ID               NUMBER,
  WKF_PARTITION_NAME       VARCHAR2(100),
  DATETIMESTAMP            DATE default SYSDATE
);
alter table STG_HIER_DETAILS add constraint PK_STG_HIER_DETAILS primary key (STG_HIERARCHY_DETAILS_ID);
alter table STG_HIER_DETAILS add constraint FK_STG_HIER_DETAILS_1 foreign key (PROCESS_ID) references STG_PROCESS_STATUS (PROCESS_ID);

create index IDX_PROCESS_ID_1 on STG_HIER_DETAILS (PROCESS_ID);

--STG_LSTNODE_HIER_DETAILS
create table STG_LSTNODE_HIER_DETAILS
(
  STU_LSTNODE_HIER_ID NUMBER,
  ORG_NAME            VARCHAR2(50),
  ORG_CODE            VARCHAR2(50),
  ORG_TYPE            VARCHAR2(50),
  ORG_LEVEL           NUMBER,
  ORG_NODE_ID         NUMBER,
  ORG_NODE_PATH       VARCHAR2(200),
  WKF_PARTITION_NAME  VARCHAR2(100),
  DATETIMESTAMP       DATE default SYSDATE
);

--STG_OBJECTIVE_DETAILS_EXTRACT
create table STG_OBJECTIVE_DETAILS_EXTRACT
(
  STUDENT_BIO_DETAILS_ID NUMBER,
  TEST_FORM              VARCHAR2(100),
  DATE_TEST_TAKEN        DATE default SYSDATE,
  GRADE                  VARCHAR2(100),
  ASSESSMENTID           NUMBER,
  NCR                    VARCHAR2(200),
  OS                     VARCHAR2(200),
  OPI                    VARCHAR2(200),
  OPIQ                   VARCHAR2(200),
  OPIP                   VARCHAR2(200),
  PC                     VARCHAR2(200),
  PP                     VARCHAR2(200),
  SS                     VARCHAR2(200),
  PL                     VARCHAR2(200),
  INRC                   VARCHAR2(200),
  PROCESS_ID             NUMBER,
  DATETIMESTAMP          DATE default sysdate
);
alter table STG_OBJECTIVE_DETAILS_EXTRACT add constraint FK_STG_STD_OBJECTIVE_DETAILS_1 foreign key (PROCESS_ID) references STG_PROCESS_STATUS (PROCESS_ID);

--STG_ORG_LSTNODE_LINK
create table STG_ORG_LSTNODE_LINK
(
  ORG_NODE_LEVEL         NUMBER,
  ORG_NODE_CODE_PATH     VARCHAR2(200),
  LST_ORG_NODE_LEVEL     NUMBER,
  LST_ORG_NODE_CODE_PATH VARCHAR2(200),
  DATETIMESTAMP          DATE default SYSDATE
);

--STG_RESULTS_GRT_FACT
create table STG_RESULTS_GRT_FACT
(
  RESULTS_GRTID                  VARCHAR2(400),
  STUDENT_BIO_DETAILS_ID         NUMBER not null,
  ORG_NODEID                     NUMBER,
  ADMINID                        NUMBER not null,
  CUST_PROD_ID                   NUMBER not null,
  GRADEID                        NUMBER not null,
  LEVELID                        NUMBER not null,
  FACULTYID                      VARCHAR2(400),
  STUDENTID                      VARCHAR2(400),
  TAPE_MODE                      VARCHAR2(400),
  ORGTSTGPGM                     VARCHAR2(10),
  CITY                           VARCHAR2(30),
  STATE                          VARCHAR2(400),
  NRT_FORM                       VARCHAR2(400),
  NRT_LEVEL                      VARCHAR2(400),
  ABILITY_TEST_NAME              VARCHAR2(400),
  ABILITY_LEVEL                  VARCHAR2(400),
  ISTEP_TEST_NAME                VARCHAR2(8),
  ISTEP_BOOK_NUM                 VARCHAR2(5),
  ISTEP_FORM                     VARCHAR2(400),
  SCORING_PATTERN                VARCHAR2(400),
  QUARTER_MONTH                  VARCHAR2(400),
  TEST_DATE                      VARCHAR2(6),
  STUDENT_LAST_NAME              VARCHAR2(12),
  ELA_PF_INDICATOR               VARCHAR2(400),
  MATH_PF_INDICATOR              VARCHAR2(400),
  SCIENCE_PF_INDICATOR           VARCHAR2(400),
  NOT_USED_1                     VARCHAR2(5),
  STUDENT_FIRST_NAME             VARCHAR2(9),
  STUDENT_MIDDLE_INITIAL         VARCHAR2(400),
  STUDENT_TEST_AI                VARCHAR2(9),
  LOCAL_USE_J                    VARCHAR2(400),
  ETHNICITY_K                    VARCHAR2(400),
  SPECIAL_EDUCATION_L            VARCHAR2(400),
  GRADE_M                        VARCHAR2(400),
  SOCIOECONOMIC_STATUS_N         VARCHAR2(400),
  SECTION_504_O                  VARCHAR2(400),
  ACCOMMODATIONS_ELA_P           VARCHAR2(400),
  ACCOMMODATIONS_MATH_Q          VARCHAR2(400),
  LOCAL_USE_R                    VARCHAR2(400),
  LOCAL_USE_S                    VARCHAR2(400),
  LOCAL_USE_T                    VARCHAR2(400),
  EXCEPTIONALITY_U               VARCHAR2(400),
  LEPESL_V                       VARCHAR2(400),
  NOT_USED_W                     VARCHAR2(400),
  ACCOMMODATIONS_SCIENCE_X       VARCHAR2(400),
  FROM_UDD_120_MIGRANT_Y         VARCHAR2(400),
  FROM_UDD_118_RETEST_FLAG_Z     VARCHAR2(400),
  BIRTH_DATE                     VARCHAR2(6),
  CHRONOLOGICAL_AGE_IN_MONTHS    VARCHAR2(400),
  STUDENTS_GENDER                VARCHAR2(400),
  NOT_USED_2                     VARCHAR2(400),
  NO_ISTEP                       VARCHAR2(400),
  MASTERY_INDICATOR_1            VARCHAR2(400),
  MASTERY_INDICATOR_2            VARCHAR2(400),
  MASTERY_INDICATOR_3            VARCHAR2(400),
  MASTERY_INDICATOR_4            VARCHAR2(400),
  MASTERY_INDICATOR_5            VARCHAR2(400),
  MASTERY_INDICATOR_6            VARCHAR2(400),
  MASTERY_INDICATOR_7            VARCHAR2(400),
  MASTERY_INDICATOR_8            VARCHAR2(400),
  MASTERY_INDICATOR_9            VARCHAR2(400),
  MASTERY_INDICATOR_10           VARCHAR2(400),
  MASTERY_INDICATOR_11           VARCHAR2(400),
  MASTERY_INDICATOR_12           VARCHAR2(400),
  MASTERY_INDICATOR_13           VARCHAR2(400),
  MASTERY_INDICATOR_14           VARCHAR2(400),
  MASTERY_INDICATOR_15           VARCHAR2(400),
  MASTERY_INDICATOR_16           VARCHAR2(400),
  MASTERY_INDICATOR_17           VARCHAR2(400),
  MASTERY_INDICATOR_18           VARCHAR2(400),
  MASTERY_INDICATOR_19           VARCHAR2(400),
  MASTERY_INDICATOR_20           VARCHAR2(400),
  MASTERY_INDICATOR_21           VARCHAR2(400),
  MASTERY_INDICATOR_22           VARCHAR2(400),
  MASTERY_INDICATOR_23           VARCHAR2(400),
  MASTERY_INDICATOR_24           VARCHAR2(400),
  MASTERY_INDICATOR_25           VARCHAR2(400),
  MASTERY_INDICATOR_26           VARCHAR2(400),
  MASTERY_INDICATOR_27           VARCHAR2(400),
  MASTERY_INDICATOR_28           VARCHAR2(400),
  MASTERY_INDICATOR_29           VARCHAR2(400),
  MASTERY_INDICATOR_30           VARCHAR2(400),
  MASTERY_INDICATOR_31           VARCHAR2(400),
  MASTERY_INDICATOR_32           VARCHAR2(400),
  MASTERY_INDICATOR_33           VARCHAR2(400),
  MASTERY_INDICATOR_34           VARCHAR2(400),
  MASTERY_INDICATOR_35           VARCHAR2(400),
  OPIIPI_1                       VARCHAR2(400),
  OPIIPI_2                       VARCHAR2(400),
  OPIIPI_3                       VARCHAR2(400),
  OPIIPI_4                       VARCHAR2(400),
  OPIIPI_5                       VARCHAR2(400),
  OPIIPI_6                       VARCHAR2(400),
  OPIIPI_7                       VARCHAR2(400),
  OPIIPI_8                       VARCHAR2(400),
  OPIIPI_9                       VARCHAR2(400),
  OPIIPI_10                      VARCHAR2(400),
  OPIIPI_11                      VARCHAR2(400),
  OPIIPI_12                      VARCHAR2(400),
  OPIIPI_13                      VARCHAR2(400),
  OPIIPI_14                      VARCHAR2(400),
  OPIIPI_15                      VARCHAR2(400),
  OPIIPI_16                      VARCHAR2(400),
  OPIIPI_17                      VARCHAR2(400),
  OPIIPI_18                      VARCHAR2(400),
  OPIIPI_19                      VARCHAR2(400),
  OPIIPI_20                      VARCHAR2(400),
  OPIIPI_21                      VARCHAR2(400),
  OPIIPI_22                      VARCHAR2(400),
  OPIIPI_23                      VARCHAR2(400),
  OPIIPI_24                      VARCHAR2(400),
  OPIIPI_25                      VARCHAR2(400),
  OPIIPI_26                      VARCHAR2(400),
  OPIIPI_27                      VARCHAR2(400),
  OPIIPI_28                      VARCHAR2(400),
  OPIIPI_29                      VARCHAR2(400),
  OPIIPI_30                      VARCHAR2(400),
  OPIIPI_31                      VARCHAR2(400),
  OPIIPI_32                      VARCHAR2(400),
  OPIIPI_33                      VARCHAR2(400),
  OPIIPI_34                      VARCHAR2(400),
  OPIIPI_35                      VARCHAR2(400),
  ENGLANG_ARTS_SUBTESTID         NUMBER,
  ENGLANG_ARTS_NUM_CORRECT       VARCHAR2(400),
  MATHEMATICS_SUBTESTID          NUMBER,
  MATHEMATICS_NUM_CORRECT        VARCHAR2(400),
  SCIENCE_SUBTESTID              NUMBER,
  SCIENCE_NUM_CORRECT            VARCHAR2(400),
  ENGLAN_ARTS_SCALE_SCORE        VARCHAR2(400),
  MATHEMATICS_SCALE_SCORE        VARCHAR2(400),
  SCIENCE_SCALE_SCORE            VARCHAR2(400),
  TEST_01                        VARCHAR2(45),
  TEST_02                        VARCHAR2(45),
  TEST_03                        VARCHAR2(45),
  TEST_04                        VARCHAR2(45),
  TEST_05                        VARCHAR2(45),
  TEST_06                        VARCHAR2(45),
  TEST01_SCI_TEST07_NOT_USED     VARCHAR2(45),
  TEST02_SCI_TEST08_NOT_USED     VARCHAR2(45),
  PA01_NOT_USED                  VARCHAR2(20),
  PA02_OR_PA12_NOT_USED          VARCHAR2(20),
  PA03                           VARCHAR2(20),
  PA04                           VARCHAR2(20),
  PA05_OR_PA13_TEST_03_SCI       VARCHAR2(20),
  PA06                           VARCHAR2(20),
  PA07                           VARCHAR2(20),
  PA08                           VARCHAR2(20),
  GRID_ITEMS_RIGHT_RESP_ARR      VARCHAR2(15),
  GRIDDED_ITEMS_STATUS           VARCHAR2(15),
  NOT_USED_3                     VARCHAR2(400),
  IMAGEID_ELA                    VARCHAR2(26),
  IMAGEID_MATH                   VARCHAR2(26),
  IMAGE_ID_SCIENCE               VARCHAR2(26),
  CORPORATION_USE_ID             VARCHAR2(15),
  CUSTOMER_USE                   VARCHAR2(20),
  STRUCTURE_LEVEL                VARCHAR2(400),
  ELEMENT_                       VARCHAR2(7),
  ISPUBLIC                       CHAR(1),
  TEACHER_NAME                   VARCHAR2(30),
  BARCODE                        VARCHAR2(8),
  CLASSID                        VARCHAR2(400),
  TEACHER_ELEMENT                VARCHAR2(7),
  MATCH_UNMATCH_U                VARCHAR2(400),
  DUPLICATE_V                    VARCHAR2(400),
  SPECIAL_CODE_X                 VARCHAR2(400),
  SPECIAL_CODE_Y                 VARCHAR2(400),
  SPECIAL_CODE_Z                 VARCHAR2(400),
  ACCOMMODATIONS_SOCIAL          VARCHAR2(400),
  GRADE_FROM_SIQ                 VARCHAR2(400),
  SOCIAL_PF_INDICATOR            VARCHAR2(400),
  SOCIAL_SUBTESTID               NUMBER,
  SOCIAL_NUM_CORRECT             VARCHAR2(400),
  SOCIAL_SCALE_SCORE             VARCHAR2(400),
  ENGLAN_ARTS_SCALE_SCORE_SEM    VARCHAR2(400),
  MATHEMATICS_SCALE_SCORE_SEM    VARCHAR2(400),
  SCIENCE_SCALE_SCORE_SEM        VARCHAR2(400),
  SOCIAL_SCALE_SCORE_SEM         VARCHAR2(400),
  MASTERY_INDICATOR_36           VARCHAR2(400),
  MASTERY_INDICATOR_37           VARCHAR2(400),
  MASTERY_INDICATOR_38           VARCHAR2(400),
  MASTERY_INDICATOR_39           VARCHAR2(400),
  MASTERY_INDICATOR_40           VARCHAR2(400),
  OPIIPI_36                      VARCHAR2(400),
  OPIIPI_37                      VARCHAR2(400),
  OPIIPI_38                      VARCHAR2(400),
  OPIIPI_39                      VARCHAR2(400),
  OPIIPI_40                      VARCHAR2(400),
  ELA_MC_SESSION_1               VARCHAR2(40),
  ELA_MC_SESSION_2               VARCHAR2(40),
  ELA_MC_SESSION_3               VARCHAR2(40),
  MATH_MC_SESSION_1              VARCHAR2(40),
  MATH_MC_SESSION_2              VARCHAR2(40),
  MATH_MC_SESSION_3              VARCHAR2(40),
  SCIENCE_MC_SESSION_1           VARCHAR2(40),
  SCIENCE_MC_SESSION_2           VARCHAR2(40),
  SCIENCE_MC_SESSION_3           VARCHAR2(40),
  SOCIAL_MC_SESSION_1            VARCHAR2(40),
  SOCIAL_MC_SESSION_2            VARCHAR2(40),
  SOCIAL_MC_SESSION_3            VARCHAR2(40),
  ELA_CR_SESSION_1               VARCHAR2(10),
  ELA_CR_SESSION_2               VARCHAR2(10),
  ELA_CR_SESSION_3               VARCHAR2(10),
  MATH_CR_SESSION_1              VARCHAR2(10),
  MATH_CR_SESSION_2              VARCHAR2(10),
  MATH_CR_SESSION_3              VARCHAR2(10),
  SCIENCE_CR_SESSION_1           VARCHAR2(10),
  SCIENCE_CR_SESSION_2           VARCHAR2(10),
  SCIENCE_CR_SESSION_3           VARCHAR2(10),
  SOCIAL_CR_SESSION_1            VARCHAR2(10),
  SOCIAL_CR_SESSION_2            VARCHAR2(10),
  SOCIAL_CR_SESSION_3            VARCHAR2(10),
  FIELDTEST_ELA_MC_SESSION_1     VARCHAR2(40),
  FIELDTEST_ELA_MC_SESSION_2     VARCHAR2(40),
  FIELDTEST_ELA_MC_SESSION_3     VARCHAR2(40),
  FIELDTEST_MATH_MC_SESSION_1    VARCHAR2(40),
  FIELDTEST_MATH_MC_SESSION_2    VARCHAR2(40),
  FIELDTEST_MATH_MC_SESSION_3    VARCHAR2(40),
  FIELDTEST_SCIENCE_MC_SESSION_1 VARCHAR2(40),
  FIELDTEST_SCIENCE_MC_SESSION_2 VARCHAR2(40),
  FIELDTEST_SCIENCE_MC_SESSION_3 VARCHAR2(40),
  FIELDTEST_SOCIAL_MC_SESSION_1  VARCHAR2(40),
  FIELDTEST_SOCIAL_MC_SESSION_2  VARCHAR2(40),
  FIELDTEST_SOCIAL_MC_SESSION_3  VARCHAR2(40),
  FIELDTEST_ELA_CR_SESSION_1     VARCHAR2(10),
  FIELDTEST_ELA_CR_SESSION_2     VARCHAR2(10),
  FIELDTEST_ELA_CR_SESSION_3     VARCHAR2(10),
  FIELDTEST_MATH_CR_SESSION_1    VARCHAR2(10),
  FIELDTEST_MATH_CR_SESSION_2    VARCHAR2(10),
  FIELDTEST_MATH_CR_SESSION_3    VARCHAR2(10),
  FIELDTEST_SCIENCE_CR_SESSION_1 VARCHAR2(10),
  FIELDTEST_SCIENCE_CR_SESSION_2 VARCHAR2(10),
  FIELDTEST_SCIENCE_CR_SESSION_3 VARCHAR2(10),
  FIELDTEST_SOCIAL_CR_SESSION_1  VARCHAR2(10),
  FIELDTEST_SOCIAL_CR_SESSION_2  VARCHAR2(10),
  FIELDTEST_SOCIAL_CR_SESSION_3  VARCHAR2(10),
  FILST_RID_IT_RIGHT_RESPE_ARR   VARCHAR2(15),
  FIELDTEST_GRIDDED_ITEMS_STATUS VARCHAR2(15),
  IMAGEID_APPLIED_SKILLS_PP      VARCHAR2(26),
  IMAGEID_APPLIED_SKILLS_OAS     VARCHAR2(26),
  IMAGEID_MC_PP                  VARCHAR2(26),
  IMAGEID_MC_OAS                 VARCHAR2(26),
  BARCODE_ID_MULTIPLE_CHOICE     VARCHAR2(8),
  MIGRANT_Q                      VARCHAR2(400),
  TEST_FORM_SET_TO_DEFAULT_FLAG  VARCHAR2(400),
  MC_BLANK_BOOK_FLAG             VARCHAR2(400),
  TEST_FORM_APP_SKLS_FID_TEST    VARCHAR2(400),
  TEST_FORM_MC_FIELD_TEST        VARCHAR2(400),
  OAS_TSTD_IND_APPL_SKLS_TST     VARCHAR2(400),
  OAS_TESTED_INDICATOR_MC_TEST   VARCHAR2(400),
  SPN_1                          VARCHAR2(50),
  SPN_2                          VARCHAR2(8),
  SPN_3                          VARCHAR2(8),
  SPN_4                          VARCHAR2(8),
  SPN_5                          VARCHAR2(8),
  CGR                            VARCHAR2(8),
  ETHNICITY_HISPANIC             VARCHAR2(10),
  RACE_AMERICAN_INDIAN           VARCHAR2(10),
  RACE_ASIAN                     VARCHAR2(10),
  RACE_BLACK                     VARCHAR2(10),
  RACE_PACIFIC_ISLANDER          VARCHAR2(10),
  RACE_WHITE                     VARCHAR2(10),
  RESOLVED_REPORTING_STATUS_ELA  VARCHAR2(10),
  RESOLVED_REPORTING_STATUS_MATH VARCHAR2(10),
  RESLVD_REPOR_STUS_SCNCE        VARCHAR2(10),
  RESLVD_REPOR_STUS__SOCSTUD     VARCHAR2(10),
  CTB_USE_OPUNIT                 CHAR(5),
  AS_PO_IREAD_1                  CHAR(20),
  AS_PO_IREAD_2                  CHAR(20),
  AS_PO_IREAD_3                  CHAR(20),
  AS_PO_IREAD_4                  CHAR(20),
  AS_PO_IREAD_5                  CHAR(20),
  AS_PO_IREAD_6                  CHAR(20),
  AS_PO_IREAD_7                  CHAR(20),
  AS_PO_IREAD_8                  CHAR(20),
  AS_PO_IREAD_9                  CHAR(20),
  AS_PO_IREAD_10                 CHAR(20),
  AS_PP_IREAD_1                  CHAR(20),
  AS_PP_IREAD_2                  CHAR(20),
  AS_PP_IREAD_3                  CHAR(20),
  AS_PP_IREAD_4                  CHAR(20),
  AS_PP_IREAD_5                  CHAR(20),
  AS_PP_IREAD_6                  CHAR(20),
  AS_PP_IREAD_7                  CHAR(20),
  AS_PP_IREAD_8                  CHAR(20),
  AS_PP_IREAD_9                  CHAR(20),
  AS_PP_IREAD_10                 CHAR(20),
  OPIIPI_CUT_1                   CHAR(5),
  OPIIPI_CUT_2                   CHAR(5),
  OPIIPI_CUT_3                   CHAR(6),
  OPIIPI_CUT_4                   CHAR(5),
  OPIIPI_CUT_5                   CHAR(5),
  OPIIPI_CUT_6                   CHAR(5),
  OPIIPI_CUT_7                   CHAR(5),
  OPIIPI_CUT_8                   CHAR(5),
  OPIIPI_CUT_9                   CHAR(5),
  OPIIPI_CUT_10                  CHAR(5),
  OPIIPI_CUT_11                  CHAR(5),
  OPIIPI_CUT_12                  CHAR(5),
  OPIIPI_CUT_13                  CHAR(5),
  OPIIPI_CUT_14                  CHAR(5),
  OPIIPI_CUT_15                  CHAR(5),
  OPIIPI_CUT_16                  CHAR(5),
  OPIIPI_CUT_17                  CHAR(5),
  OPIIPI_CUT_18                  CHAR(5),
  OPIIPI_CUT_19                  CHAR(5),
  OPIIPI_CUT_20                  CHAR(5),
  OPIIPI_CUT_21                  CHAR(5),
  OPIIPI_CUT_22                  CHAR(5),
  OPIIPI_CUT_23                  CHAR(5),
  OPIIPI_CUT_24                  CHAR(5),
  OPIIPI_CUT_25                  CHAR(5),
  OPIIPI_CUT_26                  CHAR(5),
  OPIIPI_CUT_27                  CHAR(5),
  OPIIPI_CUT_28                  CHAR(5),
  OPIIPI_CUT_29                  CHAR(5),
  OPIIPI_CUT_30                  CHAR(5),
  OPIIPI_CUT_31                  CHAR(5),
  OPIIPI_CUT_32                  CHAR(5),
  OPIIPI_CUT_33                  CHAR(5),
  OPIIPI_CUT_34                  CHAR(5),
  OPIIPI_CUT_35                  CHAR(5),
  OPIIPI_CUT_36                  CHAR(5),
  OPIIPI_CUT_37                  CHAR(5),
  OPIIPI_CUT_38                  CHAR(5),
  OPIIPI_CUT_39                  CHAR(5),
  OPIIPI_CUT_40                  CHAR(5),
  CREATED_DATETIME               DATE,
  UPDATED_DATETIME               DATE
);

--STG_STATE_MEAN_IPI_SCORE
create table STG_STATE_MEAN_IPI_SCORE
(
  STG_STATE_MEAN_IPI_SCORE_ID NUMBER not null,
  ORG_NODEID                  NUMBER not null,
  CUST_PROD_ID                NUMBER not null,
  ADMINID                     NUMBER not null,
  ENGLANG_ARTS_SUBTESTID      NUMBER,
  MATHEMATICS_SUBTESTID       NUMBER,
  SCIENCE_SUBTESTID           NUMBER,
  SOCIAL_SUBTESTID            NUMBER,
  GRADEID                     NUMBER not null,
  LEVELID                     NUMBER not null,
  MEAN_IPI_1                  VARCHAR2(5),
  MEAN_IPI_2                  VARCHAR2(5),
  MEAN_IPI_3                  VARCHAR2(5),
  MEAN_IPI_4                  VARCHAR2(5),
  MEAN_IPI_5                  VARCHAR2(5),
  MEAN_IPI_6                  VARCHAR2(5),
  MEAN_IPI_7                  VARCHAR2(5),
  MEAN_IPI_8                  VARCHAR2(5),
  MEAN_IPI_9                  VARCHAR2(5),
  MEAN_IPI_10                 VARCHAR2(5),
  MEAN_IPI_11                 VARCHAR2(5),
  MEAN_IPI_12                 VARCHAR2(5),
  MEAN_IPI_13                 VARCHAR2(5),
  MEAN_IPI_14                 VARCHAR2(5),
  MEAN_IPI_15                 VARCHAR2(5),
  MEAN_IPI_16                 VARCHAR2(5),
  MEAN_IPI_17                 VARCHAR2(5),
  MEAN_IPI_18                 VARCHAR2(5),
  MEAN_IPI_19                 VARCHAR2(5),
  MEAN_IPI_20                 VARCHAR2(5),
  MEAN_IPI_21                 VARCHAR2(5),
  MEAN_IPI_22                 VARCHAR2(5),
  MEAN_IPI_23                 VARCHAR2(5),
  MEAN_IPI_24                 VARCHAR2(5),
  MEAN_IPI_25                 VARCHAR2(5),
  MEAN_IPI_26                 VARCHAR2(5),
  MEAN_IPI_27                 VARCHAR2(5),
  MEAN_IPI_28                 VARCHAR2(5),
  MEAN_IPI_29                 VARCHAR2(5),
  MEAN_IPI_30                 VARCHAR2(5),
  MEAN_IPI_31                 VARCHAR2(5),
  STRUCTURE_LEVEL             VARCHAR2(2),
  ELEMENT_NUMBER              VARCHAR2(7),
  ISPUBLIC                    VARCHAR2(20),
  DATETIMESTAMP               DATE not null
);

--STG_STD_BIO_DETAILS -- DIFF 
create table STG_STD_BIO_DETAILS
(
  STUDENT_BIO_DETAILS_ID NUMBER not null,
  FIRST_NAME             VARCHAR2(100),
  BIOFLAG_FIRST_NAME     VARCHAR2(100),
  MIDDLE_NAME            VARCHAR2(100),
  BIOFLAG_MIDDLE_NAME    VARCHAR2(100),
  LAST_NAME              VARCHAR2(100),
  BIOFLAG_LAST_NAME      VARCHAR2(100),
  BIRTHDATE              VARCHAR2(10),
  BIOFLAG_BIRTHDATE      VARCHAR2(100),
  GENDER                 VARCHAR2(10),
  BIOFLAG_GENDER         VARCHAR2(100),
  GRADE                  VARCHAR2(10),
  BIOFLAG_GRADE          VARCHAR2(100),
  EDU_CENTER             VARCHAR2(100),
  BIOFLAG_EDU_CENTER     VARCHAR2(100),
  BARCODE                VARCHAR2(26),
  BIOFLAG_BARCODE        VARCHAR2(100),
  SPECIAL_CODES          VARCHAR2(100),
  BIOFLAG_SPECIAL_CODES  VARCHAR2(100),
  STUDENT_MODE           VARCHAR2(10),
  STRUC_ELEMENT          VARCHAR2(26),
  BIOFLAG_STRUC_ELEMENT  VARCHAR2(100),
  TEST_ELEMENT_ID        VARCHAR2(30),
  INT_STUDENT_ID         VARCHAR2(52),
  BIOFLAG_INT_STUDENT_ID VARCHAR2(100),
  EXT_STUDENT_ID         VARCHAR2(20),
  BIOFLAG_EXT_STUDENT_ID VARCHAR2(100),
  LITHOCODE              VARCHAR2(30),
  BIOFLAG_LITHOCODE      VARCHAR2(100),
  STU_LSTNODE_HIER_ID    NUMBER,
  IS_BIO_UPDATE_CMPL     VARCHAR2(2),
  PROCESS_ID             NUMBER,
  NEED_PRISM_CONSUME     VARCHAR2(10),
  WKF_PARTITION_NAME     VARCHAR2(100),
  PP_IMAGING_ID          VARCHAR2(26),
  OAS_IMAGING_ID         VARCHAR2(26),
  RESCORE_REQUIRED_FLAG  NUMBER(2),
  DATETIMESTAMP          DATE default SYSDATE
);
alter table STG_STD_BIO_DETAILS add constraint PK_STG_STD_BIO_DETAILS primary key (STUDENT_BIO_DETAILS_ID);
alter table STG_STD_BIO_DETAILS add constraint FK_STG_STD_BIO_DETAILS_1 foreign key (PROCESS_ID) references STG_PROCESS_STATUS (PROCESS_ID);

create index IDX_STG_STD_BIO_DETAILS_1 on STG_STD_BIO_DETAILS (PROCESS_ID, TEST_ELEMENT_ID);

--STG_STD_DEMO_DETAILS
create table STG_STD_DEMO_DETAILS
(
  DEMO_CODE              VARCHAR2(200),
  ASSESSMENTID           NUMBER,
  DEMO_VALUE             VARCHAR2(200),
  STUDENT_BIO_DETAILS_ID NUMBER,
  CONTENT_CODE           VARCHAR2(100),
  NEED_PRISM_CONSUME     VARCHAR2(20),
  PROCESS_ID             NUMBER,
  WKF_PARTITION_NAME     VARCHAR2(100),
  IS_BIO_UPDATE          VARCHAR2(10),
  DATETIMESTAMP          DATE default sysdate
);
alter table STG_STD_DEMO_DETAILS add constraint FK_STG_STD_DEMO_DETAILS_1 foreign key (PROCESS_ID) references STG_PROCESS_STATUS (PROCESS_ID);

--STG_STD_OBJECTIVE_DETAILS
create table STG_STD_OBJECTIVE_DETAILS
(
  STUDENT_BIO_DETAILS_ID NUMBER,
  CONTENT_NAME           VARCHAR2(100),
  TEST_FORM              VARCHAR2(100),
  DATE_TEST_TAKEN        DATE default SYSDATE,
  OBJECTIVE_NAME         VARCHAR2(100),
  COND_CODE              VARCHAR2(100),
  NCR                    NUMBER,
  OS                     VARCHAR2(5),
  OPI                    VARCHAR2(100),
  OPIQ                   VARCHAR2(100),
  OPIP                   VARCHAR2(100),
  PC                     NUMBER,
  PP                     NUMBER,
  PL                     VARCHAR2(100),
  SS                     NUMBER,
  INRC                   VARCHAR2(100),
  OBJECTIVE_SS_RANGE     VARCHAR2(100),
  GRADE_CODE             VARCHAR2(100), --NOT IN TASC
  ASSESSMENTID           NUMBER, --NOT IN TASC
  PROCESS_ID             NUMBER,
  WKF_PARTITION_NAME     VARCHAR2(100),
  DATETIMESTAMP          DATE default sysdate
);
alter table STG_STD_OBJECTIVE_DETAILS add constraint FK_STG_STD_OBJECTIVE_DETAILS foreign key (PROCESS_ID) references STG_PROCESS_STATUS (PROCESS_ID);

create index IDX_OBJ_STUDENT_BIO_DETAILS_ID on STG_STD_OBJECTIVE_DETAILS (STUDENT_BIO_DETAILS_ID);

--STG_STD_STUDENT_PDF_FILES
create table STG_STD_STUDENT_PDF_FILES
(
  STU_PDF_FILEID NUMBER not null,
  STUDENT_BIO_ID NUMBER,
  FILENAME       VARCHAR2(1024) not null,
  PDF_REPORTID   NUMBER,
  ADMINID        NUMBER not null,
  IS_FILE_EXISTS VARCHAR2(3),
  DATETIMESTAMP  DATE
);

--STG_STD_SUBTEST_DETAILS
create table STG_STD_SUBTEST_DETAILS
(
  STUDENT_BIO_DETAILS_ID NUMBER,
  STATUS_CODE            VARCHAR2(20),
  CONTENT_NAME           VARCHAR2(100),
  SCORING_STATUS         VARCHAR2(100),
  TEST_FORM              VARCHAR2(100),
  DATE_TEST_TAKEN        DATE default SYSDATE,
  AAGE                   VARCHAR2(5),
  AANCE                  NUMBER,
  AANP                   NUMBER,
  AANS                   NUMBER,
  AASS                   NUMBER,
  ACSIP                  NUMBER,
  ACSIS                  NUMBER,
  ACSIN                  NUMBER,
  CSI                    NUMBER,
  CSIL                   NUMBER,
  CSIU                   NUMBER,
  DIFF                   VARCHAR2(5),
  GE                     VARCHAR2(5),
  HSE                    VARCHAR2(1),
  LEX                    NUMBER,
  LEXL                   NUMBER,
  LEXU                   NUMBER,
  NCE                    NUMBER,
  NCR                    NUMBER,
  NP                     NUMBER,
  NPA                    NUMBER,
  NPG                    NUMBER,
  NPL                    NUMBER,
  NPH                    NUMBER,
  NS                     NUMBER,
  NSA                    NUMBER,
  NSG                    NUMBER,
  OM                     VARCHAR2(5),
  OMS                    NUMBER,
  OP                     NUMBER,
  OPM                    NUMBER,
  PC                     NUMBER,
  PL                     VARCHAR2(10),
  PP                     NUMBER,
  PR                     NUMBER,
  SEM                    NUMBER,
  SNPC                   NUMBER,
  SS                     NUMBER,
  QTL                    NUMBER,
  QTLL                   NUMBER,
  QTLU                   NUMBER,
  SUBTEST_SS_RANGE       VARCHAR2(100),
  PROCESS_ID             NUMBER,
  NEED_PRISM_CONSUME     VARCHAR2(20),
  WKF_PARTITION_NAME     VARCHAR2(100),
  DATETIMESTAMP          DATE default SYSDATE,
  GRADE_ID               NUMBER, --NOT IN TASC
  ASSESSMENTID           NUMBER, --NOT IN TASC
  SUBTESTID              NUMBER  --NOT IN TASC
);
alter table STG_STD_SUBTEST_DETAILS add constraint FK_STG_STD_SUBTEST_DETAILS_1 foreign key (PROCESS_ID) references STG_PROCESS_STATUS (PROCESS_ID);

create index IDX_STUDENT_BIO_DETAILS_ID on STG_STD_SUBTEST_DETAILS (STUDENT_BIO_DETAILS_ID);

--STG_USC_LINK
create table STG_USC_LINK
(
  ORG_USER_ID    NUMBER,
  CUST_PROD_ID   NUMBER,
  STUDENT_BIO_ID NUMBER not null,
  SUBTESTID      NUMBER,
  DATETIMESTAMP  DATE
);

--TEMP_PARENT_USERS
create table TEMP_PARENT_USERS
(
  USER_ID             VARCHAR2(100 CHAR) not null,
  LAST_NAME           VARCHAR2(100 CHAR) not null,
  FIRST_NAME          VARCHAR2(100 CHAR) not null,
  MIDDLE_NAME         VARCHAR2(100 CHAR),
  EMAIL               VARCHAR2(100 CHAR),
  DATE_AGREED_FERPA   DATE,
  LAST_LOGIN_ATTEMPT  DATE,
  CREATE_DATE         DATE not null,
  STUDENT_BIO_ID      NUMBER,
  ADMINID             NUMBER,
  CUST_PROD_ID        NUMBER,
  PARENT_ORG_NODEID   NUMBER,
  CLASS_ORG_NODEID    NUMBER,
  STUDENTID           VARCHAR2(400),
  STUDENT_IDENTITY_ID NUMBER
);

--TEMP_USER_STUDENT_MAP
create table TEMP_USER_STUDENT_MAP
(
  STUDENT_BIO_ID      NUMBER not null,
  ADMINID             NUMBER not null,
  CUST_PROD_ID        NUMBER not null,
  CLASS_ORG_NODEID    NUMBER not null,
  STUDENTID           VARCHAR2(40),
  PARENT_ORG_NODEID   NUMBER,
  STUDENT_IDENTITY_ID NUMBER not null,
  USER_PROFILE_ID     NUMBER not null,
  USER_ID             VARCHAR2(100 CHAR) not null
);

--RECORD_MIG
create table RECORD_MIG
(
  ORG_PRISM_ID   NUMBER not null,
  INODE_ID       NUMBER,
  INODE_NAME     VARCHAR2(50),
  INODE_CODE     VARCHAR2(50),
  INODE_TYPE     VARCHAR2(50),
  INODE_PARENTID NUMBER
);
create index IDX_RECORD_MIG_1 on RECORD_MIG (INODE_TYPE, INODE_ID);

--CHECK_RECS
create table CHECK_RECS
(
  AA     NUMBER,
  TYPE1  VARCHAR2(100),
  TIMETO DATE
);

--EXCEP_ACAD_STD_SUMM_FACT
create table EXCEP_ACAD_STD_SUMM_FACT
(
  CUST_PROD_ID  NUMBER,
  ORGNODE_ID    NUMBER,
  ORGNODE_LEVEL NUMBER,
  GRADEID       NUMBER,
  ISPUBLIC      NUMBER,
  ERROR_MSG     VARCHAR2(4000)
);

--GLOB_TEMP_STG_STUDENT_BIO_ID
create table GLOB_TEMP_STG_STUDENT_BIO_ID
(
  STUDENT_BIO_ID NUMBER
);

--GRW_DUPLICATE_STUDENTS
create table GRW_DUPLICATE_STUDENTS
(
  CNT             NUMBER,
  STUDENT_BIO_ID  NUMBER not null,
  CUST_PROD_ID    NUMBER not null,
  STUDENT_TEST_AI VARCHAR2(9),
  ADMINID         NUMBER not null,
  GRADEID         NUMBER not null
);

--GRW_SUBTEST_SCORE_FACT
create table GRW_SUBTEST_SCORE_FACT
(
  ORG_NODEID     NUMBER,
  STUDENT_BIO_ID NUMBER not null,
  CUST_PROD_ID   NUMBER not null,
  SUBTESTID      NUMBER not null,
  SUBTEST_CODE   VARCHAR2(20) not null,
  GRADEID        NUMBER not null,
  ADMINID        NUMBER not null,
  SS             NUMBER,
  PL             VARCHAR2(10),
  DATETIMESTAMP  DATE
)
partition by list (CUST_PROD_ID)
(
  partition PART_C5005 values (5005)
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
  partition PART_C5001 values (5001)
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
create index IDX_GRW_SUBTEST_SCORE_FACT_1 on GRW_SUBTEST_SCORE_FACT (ORG_NODEID, ADMINID, GRADEID, SUBTESTID, CUST_PROD_ID, STUDENT_BIO_ID);
create index IDX_GRW_SUBTEST_SCORE_FACT_2 on GRW_SUBTEST_SCORE_FACT (CUST_PROD_ID, STUDENT_BIO_ID, SUBTESTID, ADMINID, GRADEID, PL);

--MATRIX_SELECTION_LOOKUP_1
create global temporary table MATRIX_SELECTION_LOOKUP_1
(
  USER_ID        NUMBER not null,
  USERNAME       VARCHAR2(30) not null,
  ADMINID        NUMBER not null,
  GRADEID        NUMBER not null,
  SUBTESTID      NUMBER not null,
  CUST_PROD_ID   NUMBER not null,
  ORG_NODEID     NUMBER not null,
  STUDENT_BIO_ID NUMBER not null
)
on commit delete rows;

--MATRIX_SELECTION_LOOKUP_2
create global temporary table MATRIX_SELECTION_LOOKUP_2
(
  USER_ID        NUMBER not null,
  USERNAME       VARCHAR2(30) not null,
  ADMINID        NUMBER not null,
  GRADEID        NUMBER not null,
  SUBTESTID      NUMBER not null,
  CUST_PROD_ID   NUMBER not null,
  ORG_NODEID     NUMBER not null,
  STUDENT_BIO_ID NUMBER not null
)
on commit delete rows;

--MATRIX_SELECTION_LOOKUP_3
create global temporary table MATRIX_SELECTION_LOOKUP_3
(
  USER_ID        NUMBER not null,
  USERNAME       VARCHAR2(30) not null,
  ADMINID        NUMBER not null,
  GRADEID        NUMBER not null,
  SUBTESTID      NUMBER not null,
  CUST_PROD_ID   NUMBER not null,
  ORG_NODEID     NUMBER not null,
  STUDENT_BIO_ID NUMBER not null,
  EXT_STUDENT_ID VARCHAR2(20),
  STDUENT_NAME   VARCHAR2(98)
)
on commit delete rows;