prompt PL/SQL Developer import file
prompt Created on Friday, December 05, 2014 by 233208
set feedback off
set define off
prompt Disabling triggers for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP disable all triggers;
prompt Disabling foreign key constraints for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP disable constraint FK_DASH_CONTRACT_PROP;
prompt Deleting DASH_CONTRACT_PROP...
delete from DASH_CONTRACT_PROP;
commit;
prompt Loading DASH_CONTRACT_PROP...
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (190, 'role.not.added', 'ROLE_CTB,ROLE_PARENT,ROLE_SUPER,ROLE_EDU_ADMIN', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (191, 'orglvl.user.not.added', '0', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (192, 'orglvl.admin.not.added', '4', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (193, 'hmac.secret.key', 'BTCguSF49hYaPmAfe9Q29LtsQ2X', 'CTB.COM|inors', 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (194, 'aws.inors.cacheS3', 'Cache_Keys/INORS/', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (195, 'password.history.day', '3', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (196, 'title.tab.home.application', 'Indiana Online Reporting System', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (197, 'title.tab.application', 'Indiana Online Reporting System', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (198, 'password.expiry', '90', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (199, 'password.expiry.warning', '85', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (200, 'static.pdf.location', 'INORSREPORTS', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (201, 'orglvl.user.not.added', '0', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (202, 'orglvl.admin.not.added', '4', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (203, 'aws.inors.cacheS3', 'Cache_Keys/TASC/', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (204, 'hmac.secret.key', 'WPZguVF49hXaRuZfe9L29ItsC2I', 'OAS|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (205, 'hmac.secret.key', 'ETCguRF49hEaRuZguVF49hXrc1', 'eResource|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (206, 'role.not.added', 'ROLE_CTB,ROLE_PARENT,ROLE_SUPER,ROLE_EDU_ADMIN', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (207, 'password.history.day', '3', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (208, 'title.tab.home.application', 'TASC Dataonline', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (209, 'title.tab.application', 'TASC-Login', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (210, 'password.expiry', '90', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (211, 'password.expiry.warning', '85', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (212, 'static.pdf.location', 'TASCREPORTS', null, 1);
commit;
prompt 23 records loaded
prompt Enabling foreign key constraints for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP enable constraint FK_DASH_CONTRACT_PROP;
prompt Enabling triggers for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP enable all triggers;
set feedback on
set define on
prompt Done.
