--ISTEP_POC(Istep New Blank Schema)

--TABLE
GRANT SELECT,REFERENCES,UPDATE,INSERT,DELETE ON PDF_REPORTS TO ISTEP_POC;
GRANT SELECT ON FTP_CONFIG TO ISTEP_POC;
GRANT SELECT ON ETL_JOBMASTER_CONFIG TO ISTEP_POC;
GRANT SELECT ON ETL_PROJECT_CONFIG TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_ACTION_ACCESS TO ISTEP_POC;
GRANT SELECT ON DASH_CONTRACT_PROP TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_RPT_ACTION TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON SCORE_TYPE_LOOKUP TO ISTEP_POC;
GRANT SELECT,REFERENCES ON ITEMSET_DIM TO ISTEP_POC;
GRANT SELECT ON PROJECT_DIM TO ISTEP_POC;
GRANT SELECT,INSERT,REFERENCES,DELETE ON CUSTOMER_INFO TO ISTEP_POC;
GRANT SELECT,REFERENCES ON ADMIN_DIM TO ISTEP_POC;
GRANT SELECT ON PRODUCT TO ISTEP_POC;
GRANT SELECT,INSERT,DELETE,REFERENCES ON CUST_PRODUCT_LINK TO ISTEP_POC;
GRANT SELECT,REFERENCES ON ASSESSMENT_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON GRADE_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON GENDER_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON LEVEL_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON FORM_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON READ_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON CONTENT_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON SUBTEST_DIM TO ISTEP_POC;
GRANT SELECT,REFERENCES ON OBJECTIVE_DIM TO ISTEP_POC;
GRANT SELECT,INSERT,DELETE,REFERENCES ON TEST_PROGRAM TO ISTEP_POC;
GRANT SELECT,INSERT,DELETE ON ORG_TP_STRUCTURE TO ISTEP_POC;
GRANT SELECT ON LEVEL_MAP TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE,REFERENCES ON SUBTEST_OBJECTIVE_MAP TO ISTEP_POC;
GRANT SELECT ON GRADE_LEVEL_MAP TO ISTEP_POC;
GRANT SELECT,REFERENCES ON ROLE TO ISTEP_POC;
--GRANT SELECT ON ROLE_CUSTOMER TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_REPORTS TO ISTEP_POC;
GRANT SELECT ON DASH_MENUS TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_MENU_RPT_ACCESS TO ISTEP_POC;
GRANT SELECT,INSERT,DELETE ON DASH_MESSAGE_TYPE TO ISTEP_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_MESSAGES TO ISTEP_POC;
GRANT SELECT,REFERENCES ON PWD_HINT_QUESTIONS TO ISTEP_POC;
GRANT SELECT,REFERENCES ON CONDITION_CODES TO ISTEP_POC;
GRANT SELECT,INSERT,DELETE ON ORG_USER_DEFINE_LOOKUP TO ISTEP_POC;
GRANT SELECT ON NATL_MEAN_SS_LOOKUP TO ISTEP_POC;
GRANT SELECT ON NP_MEAN_NCE_LOOKUP TO ISTEP_POC;
GRANT SELECT ON NP_NCE_LOOKUP TO ISTEP_POC;
GRANT SELECT,REFERENCES ON ACTIVITY_TYPE TO ISTEP_POC;

-- Sequences

GRANT SELECT ON SEQ_DASH_ACTION_ACCESS TO ISTEP_POC;
GRANT SELECT ON SEQ_DASH_CONTRACT_PROP TO ISTEP_POC;
GRANT SELECT ON SEQ_DASH_RPT_ACTION TO ISTEP_POC;
GRANT SELECT ON DB_REPORT_ID_SEQ TO ISTEP_POC;
GRANT SELECT ON DASH_MESSAGE_SEQ TO ISTEP_POC;
GRANT SELECT ON RPTMSGTYPE_SEQ TO ISTEP_POC;
GRANT SELECT ON SEQ_SCORE_LKP_ID TO ISTEP_POC;
GRANT SELECT ON NMN_LOOKUPID_SEQ TO ISTEP_POC;

-- TYPE

GRANT ALL ON DYN_ATTR_DTLS TO ISTEP_POC;

------------------------------------------------------------------------------------------
--TASC_POC(TASC_POC New Blank Schema )

--TABLE
GRANT SELECT,REFERENCES,UPDATE,INSERT,DELETE ON PDF_REPORTS TO TASC_POC;
GRANT SELECT ON FTP_CONFIG TO TASC_POC;
GRANT SELECT ON ETL_JOBMASTER_CONFIG TO TASC_POC;
GRANT SELECT ON ETL_PROJECT_CONFIG TO TASC_POC;
GRANT SELECT ON TASC_LOOKUP_DATA_SWAP TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_ACTION_ACCESS TO TASC_POC;
GRANT SELECT ON DASH_CONTRACT_PROP TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_RPT_ACTION TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON SCORE_TYPE_LOOKUP TO TASC_POC;
GRANT SELECT,REFERENCES ON ITEMSET_DIM TO TASC_POC;
GRANT SELECT ON PROJECT_DIM TO TASC_POC;
GRANT SELECT,INSERT,REFERENCES,DELETE ON CUSTOMER_INFO TO TASC_POC;
GRANT SELECT,REFERENCES ON ADMIN_DIM TO TASC_POC;
GRANT SELECT ON PRODUCT TO TASC_POC;
GRANT SELECT,INSERT,DELETE,REFERENCES ON CUST_PRODUCT_LINK TO TASC_POC;
GRANT SELECT,REFERENCES ON ASSESSMENT_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON GRADE_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON GENDER_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON LEVEL_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON FORM_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON READ_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON CONTENT_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON SUBTEST_DIM TO TASC_POC;
GRANT SELECT,REFERENCES ON OBJECTIVE_DIM TO TASC_POC;
GRANT SELECT,INSERT,DELETE,REFERENCES ON TEST_PROGRAM TO TASC_POC;
GRANT SELECT,INSERT,DELETE ON ORG_TP_STRUCTURE TO TASC_POC;
GRANT SELECT ON LEVEL_MAP TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE,REFERENCES ON SUBTEST_OBJECTIVE_MAP TO TASC_POC;
GRANT SELECT ON GRADE_LEVEL_MAP TO TASC_POC;
GRANT SELECT,REFERENCES ON ROLE TO TASC_POC;
--GRANT SELECT ON ROLE_CUSTOMER TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_REPORTS TO TASC_POC;
GRANT SELECT ON DASH_MENUS TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_MENU_RPT_ACCESS TO TASC_POC;
GRANT SELECT ON DASH_MESSAGE_TYPE TO TASC_POC;
GRANT SELECT,UPDATE,INSERT,DELETE ON DASH_MESSAGES TO TASC_POC;
GRANT SELECT,REFERENCES ON PWD_HINT_QUESTIONS TO TASC_POC;
GRANT SELECT,REFERENCES ON CONDITION_CODES TO TASC_POC;
GRANT SELECT,INSERT,DELETE ON ORG_USER_DEFINE_LOOKUP TO TASC_POC;
GRANT SELECT ON NATL_MEAN_SS_LOOKUP TO TASC_POC;
GRANT SELECT,INSERT,DELETE ON NP_MEAN_NCE_LOOKUP TO TASC_POC;
GRANT SELECT ON NP_NCE_LOOKUP TO TASC_POC;
GRANT SELECT,REFERENCES ON ACTIVITY_TYPE TO TASC_POC;

-- Sequences

GRANT SELECT ON SEQ_DASH_ACTION_ACCESS TO TASC_POC;
GRANT SELECT ON SEQ_DASH_CONTRACT_PROP TO TASC_POC;
GRANT SELECT ON SEQ_DASH_RPT_ACTION TO TASC_POC;
GRANT SELECT ON DB_REPORT_ID_SEQ TO TASC_POC;
GRANT SELECT ON DASH_MESSAGE_SEQ TO TASC_POC;
GRANT SELECT ON RPTMSGTYPE_SEQ TO TASC_POC;
GRANT SELECT ON SEQ_SCORE_LKP_ID TO TASC_POC;
GRANT SELECT ON NMN_LOOKUPID_SEQ TO TASC_POC;

--Type

GRANT ALL ON DYN_ATTR_DTLS TO TASC_POC;