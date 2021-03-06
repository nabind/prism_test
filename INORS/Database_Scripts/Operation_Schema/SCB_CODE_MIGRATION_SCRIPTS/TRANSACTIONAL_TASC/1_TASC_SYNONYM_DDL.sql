--(remember to change the schema name for global schema) 
--Tables

CREATE OR REPLACE SYNONYM SCORE_TYPE_LOOKUP FOR prismglobal.SCORE_TYPE_LOOKUP;
CREATE OR REPLACE SYNONYM PROJECT_DIM FOR prismglobal.PROJECT_DIM;
CREATE OR REPLACE SYNONYM PWD_HINT_QUESTIONS FOR prismglobal.PWD_HINT_QUESTIONS;
CREATE OR REPLACE SYNONYM ACTIVITY_TYPE FOR prismglobal.ACTIVITY_TYPE;
--CREATE OR REPLACE SYNONYM ROLE FOR prismglobal.ROLE;


-- Sequences

CREATE OR REPLACE SYNONYM SEQ_DASH_ACTION_ACCESS FOR prismglobal.SEQ_DASH_ACTION_ACCESS;
CREATE OR REPLACE SYNONYM SEQ_DASH_CONTRACT_PROP FOR prismglobal.SEQ_DASH_CONTRACT_PROP;
CREATE OR REPLACE SYNONYM SEQ_DASH_RPT_ACTION FOR prismglobal.SEQ_DASH_RPT_ACTION;
CREATE OR REPLACE SYNONYM DB_REPORT_ID_SEQ FOR prismglobal.DB_REPORT_ID_SEQ;
CREATE OR REPLACE SYNONYM DASH_MESSAGE_SEQ FOR prismglobal.DASH_MESSAGE_SEQ;
CREATE OR REPLACE SYNONYM RPTMSGTYPE_SEQ FOR prismglobal.RPTMSGTYPE_SEQ;
CREATE OR REPLACE SYNONYM NMN_LOOKUPID_SEQ FOR prismglobal.NMN_LOOKUPID_SEQ;

-- TYPE

CREATE OR REPLACE SYNONYM DYN_ATTR_DTLS FOR prismglobal.DYN_ATTR_DTLS;
/