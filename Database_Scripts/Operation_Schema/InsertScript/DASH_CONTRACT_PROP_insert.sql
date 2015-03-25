prompt PL/SQL Developer import file
prompt Created on Monday, March 22, 2015 by 541841
set feedback off
set define off
prompt Disabling triggers for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP disable all triggers;
prompt Disabling foreign key constraints for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP disable constraint FK_DASH_CONTRACT_PROP;
prompt Loading DASH_CONTRACT_PROP...
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (220, 'default.custProdId.gscm', '3001', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (221, 'default.custProdId.gscm', '5001', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (222, 'default.custProdId.gscm', '5027', null, 3);
prompt 3 records loaded
prompt Enabling foreign key constraints for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP enable constraint FK_DASH_CONTRACT_PROP;
prompt Enabling triggers for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP enable all triggers;
set feedback on
set define on
prompt Done.
