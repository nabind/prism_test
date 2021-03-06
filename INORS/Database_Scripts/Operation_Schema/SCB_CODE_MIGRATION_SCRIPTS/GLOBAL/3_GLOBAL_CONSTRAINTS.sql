ALTER TABLE PROJECT_DIM ADD CONSTRAINT PK_PROJECT_DIM primary key(PROJECTID);

alter table CUSTOMER_INFO add constraint PK_CUSTOMER_INFO primary key (CUSTOMERID);
alter table CUSTOMER_INFO add constraint CHK_TP_SELECTION check (DISPLAY_TP_SELECTION IN ('Y','N'));
ALTER TABLE CUSTOMER_INFO ADD CONSTRAINT FK_CUSTOMER_INFO FOREIGN KEY (PROJECTID) REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE ADMIN_DIM ADD CONSTRAINT PK_PROGRAM_DIM PRIMARY KEY (ADMINID);
ALTER TABLE ADMIN_DIM ADD CONSTRAINT CHK_CURRENT_ADMIN CHECK (IS_CURRENT_ADMIN IN ('N','Y'));
ALTER TABLE ADMIN_DIM ADD CONSTRAINT FK_ADMIN_DIM FOREIGN KEY (PROJECTID) REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE PRODUCT ADD CONSTRAINT PK_PRODUCT PRIMARY KEY (PRODUCTID);
ALTER TABLE PRODUCT ADD CONSTRAINT FK_PRODUCT FOREIGN KEY (PROJECTID) REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE CUST_PRODUCT_LINK ADD CONSTRAINT PK_CUST_PRODUCT_LINK PRIMARY KEY (CUST_PROD_ID);
ALTER TABLE CUST_PRODUCT_LINK ADD CONSTRAINT CHK_ACT_PRODUCT CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));
ALTER TABLE CUST_PRODUCT_LINK ADD CONSTRAINT FK_CUST_PRODUCT_LINK_1 FOREIGN KEY (CUSTOMERID)
	  REFERENCES CUSTOMER_INFO (CUSTOMERID);
ALTER TABLE CUST_PRODUCT_LINK ADD CONSTRAINT FK_CUST_PRODUCT_LINK_2 FOREIGN KEY (PRODUCTID)
	  REFERENCES PRODUCT (PRODUCTID);
ALTER TABLE CUST_PRODUCT_LINK ADD CONSTRAINT FK_CUST_PRODUCT_LINK_3 FOREIGN KEY (ADMINID)
	  REFERENCES ADMIN_DIM (ADMINID);
	  
ALTER TABLE ASSESSMENT_DIM ADD CONSTRAINT PK_PRODUCT_DIM PRIMARY KEY (ASSESSMENTID);
ALTER TABLE ASSESSMENT_DIM ADD CONSTRAINT FK_ASSESSMENT_DIM_1 FOREIGN KEY (PRODUCTID)
	  REFERENCES PRODUCT (PRODUCTID);

ALTER TABLE GRADE_DIM ADD CONSTRAINT PK_GRADE_DIM PRIMARY KEY (GRADEID);
ALTER TABLE GRADE_DIM ADD CONSTRAINT FK_GRADE_DIM FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE GENDER_DIM ADD CONSTRAINT PK_GENDER_DIM PRIMARY KEY (GENDERID);
ALTER TABLE GENDER_DIM ADD CONSTRAINT FK_GENDER_DIM FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE LEVEL_DIM ADD CONSTRAINT PK_LEVEL_DIM PRIMARY KEY (LEVELID);
ALTER TABLE LEVEL_DIM ADD CONSTRAINT FK_LEVEL_DIM FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE FORM_DIM ADD CONSTRAINT PK_FORM_DIM PRIMARY KEY (FORMID);
ALTER TABLE FORM_DIM ADD CONSTRAINT FK_FORM_DIM FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE READ_DIM ADD CONSTRAINT PK_READ_DIM PRIMARY KEY (READID);
ALTER TABLE READ_DIM ADD CONSTRAINT CHK_READ_ITEM_TYPE CHECK (ITEM_TYPE IN ('SR', 'CR', 'GR'));
ALTER TABLE READ_DIM ADD CONSTRAINT FK_READ_DIM FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE CONTENT_DIM ADD CONSTRAINT PK_CONTENT_AREA_DIM PRIMARY KEY (CONTENTID);
ALTER TABLE CONTENT_DIM ADD CONSTRAINT FK_CONTENT_DIM_1 FOREIGN KEY (ASSESSMENTID)
	  REFERENCES ASSESSMENT_DIM (ASSESSMENTID);

ALTER TABLE SUBTEST_DIM ADD CONSTRAINT PK_SUBJECT_DIM PRIMARY KEY (SUBTESTID);
ALTER TABLE SUBTEST_DIM ADD CONSTRAINT FK_SUBTEST_DIM_1 FOREIGN KEY (CONTENTID)
	  REFERENCES CONTENT_DIM (CONTENTID);

ALTER TABLE OBJECTIVE_DIM ADD CONSTRAINT PK_OBJECTIVE_DIM PRIMARY KEY (OBJECTIVEID);
ALTER TABLE OBJECTIVE_DIM ADD CONSTRAINT FK_OBJECTIVE_DIM FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE TEST_PROGRAM ADD CONSTRAINT PK_TEST_PROGRAM PRIMARY KEY (TP_ID);
ALTER TABLE TEST_PROGRAM ADD CONSTRAINT CHK_TP_TYPE CHECK (TP_TYPE IN ('PUBLIC','NON-PUBLIC'));
ALTER TABLE TEST_PROGRAM ADD CONSTRAINT CHK_TP_MODE CHECK (TP_MODE IN ('OL','PP'));
ALTER TABLE TEST_PROGRAM ADD CONSTRAINT FK_TEST_PROGRAM_1 FOREIGN KEY (CUSTOMERID)
	  REFERENCES CUSTOMER_INFO (CUSTOMERID);
ALTER TABLE TEST_PROGRAM ADD CONSTRAINT FK_TEST_PROGRAM_2 FOREIGN KEY (ADMINID)
	  REFERENCES ADMIN_DIM (ADMINID);
alter table TEST_PROGRAM
  add constraint CHK_ACT_TEST_PROGRAM
  check (ACTIVATION_STATUS IN ('AC', 'IN'));	  

ALTER TABLE ORG_TP_STRUCTURE ADD CONSTRAINT PK_ORG_TP_STRUCTURE PRIMARY KEY (TP_ID, ORG_LEVEL);
ALTER TABLE ORG_TP_STRUCTURE ADD CONSTRAINT FK_ORG_TP_STRUCTURE_1 FOREIGN KEY (TP_ID)
	  REFERENCES TEST_PROGRAM (TP_ID);

ALTER TABLE LEVEL_MAP ADD CONSTRAINT PK_LEVEL_MAP PRIMARY KEY (LEVEL_MAPID);
ALTER TABLE LEVEL_MAP ADD CONSTRAINT FK_LEVEL_MAP_1 FOREIGN KEY (LEVELID)
	  REFERENCES LEVEL_DIM (LEVELID);
ALTER TABLE LEVEL_MAP ADD CONSTRAINT FK_LEVEL_MAP_2 FOREIGN KEY (FORMID)
	  REFERENCES FORM_DIM (FORMID);
ALTER TABLE LEVEL_MAP ADD CONSTRAINT FK_LEVEL_MAP_3 FOREIGN KEY (ASSESSMENTID)
	  REFERENCES ASSESSMENT_DIM (ASSESSMENTID);

ALTER TABLE SUBTEST_OBJECTIVE_MAP ADD CONSTRAINT PK_SUBTEST_OBJECTIVE_MAP PRIMARY KEY (SUBT_OBJ_MAPID);
ALTER TABLE SUBTEST_OBJECTIVE_MAP ADD CONSTRAINT FK_SUBTEST_OBJECTIVE_MAP_1 FOREIGN KEY (SUBTESTID)
	  REFERENCES SUBTEST_DIM (SUBTESTID);
ALTER TABLE SUBTEST_OBJECTIVE_MAP ADD CONSTRAINT FK_SUBTEST_OBJECTIVE_MAP_2 FOREIGN KEY (OBJECTIVEID)
	  REFERENCES OBJECTIVE_DIM (OBJECTIVEID);
ALTER TABLE SUBTEST_OBJECTIVE_MAP ADD CONSTRAINT FK_SUBTEST_OBJECTIVE_MAP_3 FOREIGN KEY (LEVEL_MAPID)
	  REFERENCES LEVEL_MAP (LEVEL_MAPID);
ALTER TABLE SUBTEST_OBJECTIVE_MAP ADD CONSTRAINT FK_SUBTEST_OBJECTIVE_MAP_4 FOREIGN KEY (ASSESSMENTID)
	  REFERENCES ASSESSMENT_DIM (ASSESSMENTID);

ALTER TABLE ITEMSET_DIM ADD CONSTRAINT PK_ITEMSET_DIM PRIMARY KEY (ITEMSETID);
ALTER TABLE ITEMSET_DIM ADD CONSTRAINT CHK_ITEM_TYPE CHECK (ITEM_TYPE IN ('SR', 'CR', 'GR','OBJ'));
ALTER TABLE ITEMSET_DIM ADD CONSTRAINT FK_ITEMSET_DIM_1 FOREIGN KEY (SUBTESTID)
      REFERENCES SUBTEST_DIM (SUBTESTID);
--ALTER TABLE ITEMSET_DIM ADD CONSTRAINT FK_ITEMSET_DIM_2 FOREIGN KEY (OBJECTIVEID) REFERENCES OBJECTIVE_DIM (OBJECTIVEID);
ALTER TABLE ITEMSET_DIM ADD CONSTRAINT FK_ITEMSET_DIM_3 FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE GRADE_LEVEL_MAP ADD CONSTRAINT PK_GRADE_LEVEL_MAP PRIMARY KEY (GRADEID, LEVEL_MAPID);
ALTER TABLE GRADE_LEVEL_MAP ADD CONSTRAINT FK_GRADE_LEVEL_MAP_1 FOREIGN KEY (GRADEID)
	  REFERENCES GRADE_DIM (GRADEID);
ALTER TABLE GRADE_LEVEL_MAP ADD CONSTRAINT FK_GRADE_LEVEL_MAP_2 FOREIGN KEY (LEVEL_MAPID)
	  REFERENCES LEVEL_MAP (LEVEL_MAPID);

ALTER TABLE ROLE ADD CONSTRAINT PK_ROLE PRIMARY KEY (ROLEID);

/*ALTER TABLE ROLE_CUSTOMER ADD CONSTRAINT PK_ROLE_CUSTOMER PRIMARY KEY (CUSTOMERID, ROLEID);
ALTER TABLE ROLE_CUSTOMER ADD CONSTRAINT FK_ROLE_CUSTOMER_1 FOREIGN KEY (ROLEID)
	  REFERENCES ROLE (ROLEID);
ALTER TABLE ROLE_CUSTOMER ADD CONSTRAINT FK_ROLE_CUSTOMER_2 FOREIGN KEY (CUSTOMERID)
	  REFERENCES CUSTOMER_INFO (CUSTOMERID);*/

ALTER TABLE DASH_REPORTS ADD CONSTRAINT PK_DASHBOARD_REPORTS PRIMARY KEY (DB_REPORTID);
ALTER TABLE DASH_REPORTS ADD CONSTRAINT CHK_ACT_STS_DB_RPT CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));
ALTER TABLE DASH_REPORTS ADD CONSTRAINT FK_DASH_REPORTS FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE DASH_MENUS ADD CONSTRAINT PK_DASH_MENUS PRIMARY KEY (DB_MENUID,PROJECTID);
ALTER TABLE DASH_MENUS ADD CONSTRAINT FK_DASH_MENUS FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);
	  
--ADDED CUST_PROD_ID
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT PK_DASHBOARD_MENU_RPT_ACCESS PRIMARY KEY (DB_MENUID, DB_REPORTID, ROLEID, ORG_LEVEL,CUST_PROD_ID);
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT CHK_ACT_STS_DB_MENUS CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT FK_DASH_MENU_RPT_ACCESS_1 FOREIGN KEY (DB_REPORTID)
	  REFERENCES DASH_REPORTS (DB_REPORTID);
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT FK_DASH_MENU_RPT_ACCESS_2 FOREIGN KEY (ROLEID)
	  REFERENCES ROLE (ROLEID);
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT FK_DASH_MENU_RPT_ACCESS_3 FOREIGN KEY (CUST_PROD_ID)
	  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT FK_DASH_MENU_RPT_ACCESS_4 FOREIGN KEY (DB_MENUID,PROJECTID)
	  REFERENCES DASH_MENUS (DB_MENUID,PROJECTID);
ALTER TABLE DASH_MENU_RPT_ACCESS ADD CONSTRAINT FK_DASH_MENU_RPT_ACCESS_5 FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);	  
	  

ALTER TABLE DASH_MESSAGE_TYPE ADD CONSTRAINT PK_MESSAGE_TYPE PRIMARY KEY (MSG_TYPEID, CUST_PROD_ID);
ALTER TABLE DASH_MESSAGE_TYPE ADD CONSTRAINT DASH_MESSAGE_TYPE_UNIQUE UNIQUE (MESSAGE_NAME, MESSAGE_TYPE, CUST_PROD_ID);
ALTER TABLE DASH_MESSAGE_TYPE ADD CONSTRAINT FK_DASH_MESSAGE_TYPE_1 FOREIGN KEY (CUST_PROD_ID)
	  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);


ALTER TABLE DASH_MESSAGES ADD CONSTRAINT PK_DASHBOARD_MESSAGES PRIMARY KEY (DB_REPORTID, MSG_TYPEID, CUST_PROD_ID);
ALTER TABLE DASH_MESSAGES ADD CONSTRAINT CHK_ACT_STS_DB_MSG CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));
ALTER TABLE DASH_MESSAGES ADD CONSTRAINT FK_DASH_MESSAGES_1 FOREIGN KEY (DB_REPORTID)
	  REFERENCES DASH_REPORTS (DB_REPORTID);
alter table DASH_MESSAGES
  add constraint FK_DASH_MESSAGES_3 foreign key (MSG_TYPEID, CUST_PROD_ID)
  references DASH_MESSAGE_TYPE (MSG_TYPEID, CUST_PROD_ID);	  


ALTER TABLE PWD_HINT_QUESTIONS ADD CONSTRAINT PK_PWD_HINT_QUESTIONS PRIMARY KEY (PH_QUESTIONID);
ALTER TABLE PWD_HINT_QUESTIONS ADD CONSTRAINT CHK_ACT_STS_PWD_QSTN CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));

ALTER TABLE CONDITION_CODES ADD CONSTRAINT PK_CONDCODE_ID PRIMARY KEY (CONDCODE_ID);
ALTER TABLE CONDITION_CODES ADD CONSTRAINT FK_SUBTESTID FOREIGN KEY (SUBTESTID)
	  REFERENCES SUBTEST_DIM (SUBTESTID);

ALTER TABLE ORG_USER_DEFINE_LOOKUP ADD CONSTRAINT PK_ORG_USER_DEFINE_LOOKUP PRIMARY KEY (ROLEID, ORG_NODE_LEVEL, USER_SEQ, CUSTOMERID);
ALTER TABLE ORG_USER_DEFINE_LOOKUP ADD CONSTRAINT FK_ORG_USER_DEFINE_LOOKUP_1 FOREIGN KEY (ROLEID)
	  REFERENCES ROLE (ROLEID);
ALTER TABLE ORG_USER_DEFINE_LOOKUP ADD CONSTRAINT FK_ORG_USER_DEFINE_LOOKUP_2 FOREIGN KEY (CUSTOMERID)
	  REFERENCES CUSTOMER_INFO (CUSTOMERID);

ALTER TABLE NATL_MEAN_SS_LOOKUP ADD CONSTRAINT PK_NATL_MEAN_SS_LOOKUP PRIMARY KEY (NMS_LOOKUPID);
ALTER TABLE NATL_MEAN_SS_LOOKUP ADD CONSTRAINT FK_NATL_MEAN_SS_LOOKUP_1 FOREIGN KEY (CUST_PROD_ID)
	  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);

ALTER TABLE NP_MEAN_NCE_LOOKUP ADD CONSTRAINT PK_NP_MEAN_NCE_LOOKUP PRIMARY KEY (NMN_LOOKUPID);
ALTER TABLE NP_MEAN_NCE_LOOKUP ADD CONSTRAINT FK_NP_MEAN_NCE_LOOKUP_1 FOREIGN KEY (CUST_PROD_ID)
	  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);

ALTER TABLE NP_NCE_LOOKUP ADD CONSTRAINT PK_NP_NCE_LOOKUP PRIMARY KEY (NN_LOOKUPID);
ALTER TABLE NP_NCE_LOOKUP ADD CONSTRAINT FK_NP_NCE_LOOKUP_1 FOREIGN KEY (CUST_PROD_ID)
	  REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);

ALTER TABLE ACTIVITY_TYPE ADD CONSTRAINT PK_ACTIVITY_TYPE PRIMARY KEY (ACTY_TYPEID);

ALTER TABLE DASH_RPT_ACTION ADD CONSTRAINT PK_DASH_RPT_ACTION PRIMARY KEY (DB_ACTIONID);
ALTER TABLE DASH_RPT_ACTION ADD CONSTRAINT FK_DASH_RPT_ACTION FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);

ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT PK_DASH_ACTION_ACCESS PRIMARY KEY (DB_ACT_ACCESSID, DB_MENUID, DB_REPORTID, DB_ACTIONID, ROLEID, ORG_LEVEL);
ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT FK_DASH_ACTION_ACCESS_1 FOREIGN KEY (DB_REPORTID)
      REFERENCES DASH_REPORTS (DB_REPORTID);
ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT FK_DASH_ACTION_ACCESS_2 FOREIGN KEY (ROLEID)
	  REFERENCES ROLE (ROLEID);
ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT FK_DASH_ACTION_ACCESS_3 FOREIGN KEY (CUST_PROD_ID)
      REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);
ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT FK_DASH_ACTION_ACCESS_4 FOREIGN KEY (DB_MENUID,PROJECTID)
      REFERENCES DASH_MENUS (DB_MENUID,PROJECTID);
ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT FK_DASH_ACTION_ACCESS_5 FOREIGN KEY (DB_ACTIONID)
      REFERENCES DASH_RPT_ACTION (DB_ACTIONID);
ALTER TABLE DASH_ACTION_ACCESS ADD CONSTRAINT FK_DASH_ACTION_ACCESS_6 FOREIGN KEY (PROJECTID)
      REFERENCES PROJECT_DIM (PROJECTID);
	  
ALTER TABLE DASH_CONTRACT_PROP ADD CONSTRAINT PK_DASH_CONTRACT_PROP primary key (DB_PROPERTYID);
ALTER TABLE DASH_CONTRACT_PROP ADD CONSTRAINT FK_DASH_CONTRACT_PROP FOREIGN KEY (PROJECTID)
	  REFERENCES PROJECT_DIM (PROJECTID);


ALTER TABLE SCORE_TYPE_LOOKUP ADD CONSTRAINT PK_SCORE_TYPE_LOOKUP PRIMARY KEY (SCORE_TYPEID);
  
ALTER TABLE SCORE_TYPE_LOOKUP ADD CONSTRAINT FK_SCORE_TYPE_LOOKUP_1 FOREIGN KEY (CUST_PROD_ID)
      REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);	 

ALTER TABLE PDF_REPORTS ADD CONSTRAINT PK_PDF_REPORTS PRIMARY KEY (PDF_REPORTID);
ALTER TABLE PDF_REPORTS ADD CONSTRAINT FK_PDF_REPORTS_1 FOREIGN KEY (CUST_PROD_ID) REFERENCES CUST_PRODUCT_LINK (CUST_PROD_ID);

ALTER TABLE PDF_REPORTS ADD CONSTRAINT CHK_ACT_STS_PDF_REPORTS CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));

ALTER TABLE FTP_CONFIG ADD CONSTRAINT PK_FTP_CONFIG PRIMARY KEY (FTPID);
ALTER TABLE FTP_CONFIG MODIFY FTP_MODE NOT NULL;
ALTER TABLE FTP_CONFIG ADD CONSTRAINT FK_FTP_CONFIG FOREIGN KEY (CUSTOMERID) REFERENCES CUSTOMER_INFO (CUSTOMERID);
ALTER TABLE FTP_CONFIG ADD CONSTRAINT CHK_ACT_FTP CHECK (ACTIVATION_STATUS IN ('AC', 'IN'));
ALTER TABLE FTP_CONFIG ADD CONSTRAINT CHK_FILE_PROTOCOL CHECK (FILE_PROTOCOL IN ('FTP','SFTP','FTPS','SCP'));
ALTER TABLE FTP_CONFIG ADD CONSTRAINT CHK_FTP_MODE CHECK (FTP_MODE IN ('D','W','R'));
  