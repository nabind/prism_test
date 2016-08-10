--Wisconsin(Wisconsin New Blank Schema) RUN FROM THE GLOBAL SCHEMA

--TABLE
GRANT SELECT,REFERENCES,UPDATE,INSERT,DELETE ON PDF_REPORTS TO Wisconsin;
GRANT SELECT ON FTP_CONFIG TO Wisconsin;
GRANT SELECT ON ETL_JOBMASTER_CONFIG TO Wisconsin;
GRANT SELECT ON ETL_PROJECT_CONFIG TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_ACTION_ACCESS TO Wisconsin;
GRANT SELECT ON DASH_CONTRACT_PROP TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_RPT_ACTION TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE ON SCORE_TYPE_LOOKUP TO Wisconsin;
GRANT SELECT,REFERENCES ON ITEMSET_DIM TO Wisconsin;
GRANT SELECT ON PROJECT_DIM TO Wisconsin;
GRANT SELECT,INSERT,REFERENCES,DELETE ON CUSTOMER_INFO TO Wisconsin;
GRANT SELECT,REFERENCES ON ADMIN_DIM TO Wisconsin;
GRANT SELECT ON PRODUCT TO Wisconsin;
GRANT SELECT,INSERT,DELETE,REFERENCES ON CUST_PRODUCT_LINK TO Wisconsin;
GRANT SELECT,REFERENCES ON ASSESSMENT_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON GRADE_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON GENDER_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON LEVEL_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON FORM_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON READ_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON CONTENT_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON SUBTEST_DIM TO Wisconsin;
GRANT SELECT,REFERENCES ON OBJECTIVE_DIM TO Wisconsin;
GRANT SELECT,INSERT,DELETE,REFERENCES ON TEST_PROGRAM TO Wisconsin;
GRANT SELECT,INSERT,DELETE ON ORG_TP_STRUCTURE TO Wisconsin;
GRANT SELECT ON LEVEL_MAP TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE,REFERENCES ON SUBTEST_OBJECTIVE_MAP TO Wisconsin;
GRANT SELECT ON GRADE_LEVEL_MAP TO Wisconsin;
GRANT SELECT,REFERENCES ON ROLE TO Wisconsin;
--GRANT SELECT ON ROLE_CUSTOMER TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_REPORTS TO Wisconsin;
GRANT SELECT ON DASH_MENUS TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_MENU_RPT_ACCESS TO Wisconsin;
GRANT SELECT,INSERT,DELETE ON DASH_MESSAGE_TYPE TO Wisconsin;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_MESSAGES TO Wisconsin;
GRANT SELECT,REFERENCES ON PWD_HINT_QUESTIONS TO Wisconsin;
GRANT SELECT,REFERENCES ON CONDITION_CODES TO Wisconsin;
GRANT SELECT,INSERT,DELETE ON ORG_USER_DEFINE_LOOKUP TO Wisconsin;
GRANT SELECT ON NATL_MEAN_SS_LOOKUP TO Wisconsin;
GRANT SELECT ON NP_MEAN_NCE_LOOKUP TO Wisconsin;
GRANT SELECT ON NP_NCE_LOOKUP TO Wisconsin;
GRANT SELECT,REFERENCES ON ACTIVITY_TYPE TO Wisconsin;

-- Sequences

GRANT SELECT ON SEQ_DASH_ACTION_ACCESS TO Wisconsin;
GRANT SELECT ON SEQ_DASH_CONTRACT_PROP TO Wisconsin;
GRANT SELECT ON SEQ_DASH_RPT_ACTION TO Wisconsin;
GRANT SELECT ON DB_REPORT_ID_SEQ TO Wisconsin;
GRANT SELECT ON DASH_MESSAGE_SEQ TO Wisconsin;
GRANT SELECT ON RPTMSGTYPE_SEQ TO Wisconsin;
GRANT SELECT ON SEQ_SCORE_LKP_ID TO Wisconsin;
GRANT SELECT ON NMN_LOOKUPID_SEQ TO Wisconsin;

-- TYPE

GRANT ALL ON DYN_ATTR_DTLS TO Wisconsin;
