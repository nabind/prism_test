----CHANGE THE GLOBAL SCHEMA NAME
GRANT SELECT ON SEQ_ORG_NODE_DIM TO PRISMGLOBAL;
GRANT SELECT ON SEQ_ORG_PRODUCT_LINK TO PRISMGLOBAL;
GRANT SELECT ON SEQ_CUTSCOREIPI  TO PRISMGLOBAL;
GRANT SELECT ON ARTICLE_METADATA_SEQ TO PRISMGLOBAL;
GRANT SELECT ON SEQ_STATE_MEAN_IPI_SCORE TO PRISMGLOBAL;

GRANT SELECT,INSERT,DELETE ON ORG_NODE_DIM TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON ORG_PRODUCT_LINK TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON ORG_TEST_PROGRAM_LINK TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON DISAGGREGATION_CATEGORY_TYPE TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON DISAGGREGATION_CATEGORY TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON CUTSCOREIPI TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON CUTSCORESCALESCORE TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON ASFD_ORDERBY TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON ARTICLE_METADATA TO PRISMGLOBAL;
GRANT SELECT,INSERT,DELETE ON STATE_MEAN_IPI_SCORE TO PRISMGLOBAL;
GRANT SELECT,INSERT ON DEMOGRAPHIC TO PRISMGLOBAL;
GRANT SELECT,INSERT ON DEMOGRAPHIC_VALUES TO PRISMGLOBAL;

COMMIT;
----TO REVOKE THE GRANTS GIVEN ABOVE RUN THE BELOW COMMENTED REVOKE COMMANDS
/*REVOKE SELECT ON SEQ_ORG_NODE_DIM FROM PRISMGLOBAL;
REVOKE SELECT ON SEQ_ORG_PRODUCT_LINK FROM PRISMGLOBAL;
REVOKE SELECT ON SEQ_CUTSCOREIPI  FROM PRISMGLOBAL;
REVOKE SELECT ON ARTICLE_METADATA_SEQ FROM PRISMGLOBAL;
REVOKE SELECT ON SEQ_STATE_MEAN_IPI_SCORE FROM PRISMGLOBAL;

REVOKE SELECT,INSERT,DELETE ON ORG_NODE_DIM FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON ORG_PRODUCT_LINK FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON ORG_TEST_PROGRAM_LINK FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON DISAGGREGATION_CATEGORY_TYPE FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON DISAGGREGATION_CATEGORY FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON CUTSCOREIPI FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON CUTSCORESCALESCORE FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON ASFD_ORDERBY FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON ARTICLE_METADATA FROM PRISMGLOBAL;
REVOKE SELECT,INSERT,DELETE ON STATE_MEAN_IPI_SCORE FROM PRISMGLOBAL;
REVOKE SELECT,INSERT ON DEMOGRAPHIC FROM PRISMGLOBAL;
REVOKE SELECT,INSERT ON DEMOGRAPHIC_VALUES FROM PRISMGLOBAL;
*/
