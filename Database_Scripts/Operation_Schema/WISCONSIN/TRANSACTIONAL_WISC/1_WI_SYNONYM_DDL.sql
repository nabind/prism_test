-- Run this is ISTEP FACT Schema -- (remember to change the schema name for global schema) 

--Tables

CREATE SYNONYM SCORE_TYPE_LOOKUP FOR prismglobal.SCORE_TYPE_LOOKUP;
CREATE SYNONYM PROJECT_DIM FOR prismglobal.PROJECT_DIM;
CREATE SYNONYM PWD_HINT_QUESTIONS FOR prismglobal.PWD_HINT_QUESTIONS;
CREATE SYNONYM ACTIVITY_TYPE FOR prismglobal.ACTIVITY_TYPE;
--CREATE SYNONYM ROLE FOR prismglobal.ROLE;


-- Sequences

CREATE SYNONYM SEQ_DASH_ACTION_ACCESS FOR prismglobal.SEQ_DASH_ACTION_ACCESS;
CREATE SYNONYM SEQ_DASH_CONTRACT_PROP FOR prismglobal.SEQ_DASH_CONTRACT_PROP;
CREATE SYNONYM SEQ_DASH_RPT_ACTION FOR prismglobal.SEQ_DASH_RPT_ACTION;
CREATE SYNONYM DB_REPORT_ID_SEQ FOR prismglobal.DB_REPORT_ID_SEQ;
CREATE SYNONYM DASH_MESSAGE_SEQ FOR prismglobal.DASH_MESSAGE_SEQ;
CREATE SYNONYM RPTMSGTYPE_SEQ FOR prismglobal.RPTMSGTYPE_SEQ;
CREATE SYNONYM NMN_LOOKUPID_SEQ FOR prismglobal.NMN_LOOKUPID_SEQ;

-- TYPE

CREATE SYNONYM DYN_ATTR_DTLS FOR prismglobal.DYN_ATTR_DTLS;