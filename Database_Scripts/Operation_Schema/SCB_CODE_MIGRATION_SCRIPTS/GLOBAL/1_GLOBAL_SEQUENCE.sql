create sequence SEQ_DASH_ACTION_ACCESS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

create sequence NMN_LOOKUPID_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 40001
increment by 4
cache 20;

create sequence SEQ_DASH_CONTRACT_PROP
minvalue 1
maxvalue 999999999999999999999999999
start with 190
increment by 1
cache 20;

create sequence SEQ_DASH_RPT_ACTION
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

create sequence DB_REPORT_ID_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 10100
increment by 1
cache 20;

create sequence DASH_MESSAGE_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1500
increment by 1
nocache;

create sequence RPTMSGTYPE_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1500
increment by 1
nocache;

create sequence SEQ_SCORE_LKP_ID
minvalue 1
maxvalue 999999
start with 101
increment by 1
cache 20;
