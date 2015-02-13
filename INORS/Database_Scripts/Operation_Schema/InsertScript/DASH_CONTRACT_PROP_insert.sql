prompt PL/SQL Developer import file
prompt Created on Thursday, January 29, 2015 by 541841
set feedback off
set define off
prompt Disabling triggers for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP disable all triggers;
prompt Disabling foreign key constraints for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP disable constraint FK_DASH_CONTRACT_PROP;
prompt Deleting DASH_CONTRACT_PROP...
delete from DASH_CONTRACT_PROP;
prompt Loading DASH_CONTRACT_PROP...
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (190, 'static.pdf.location', 'INORSREPORTS', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (191, 'static.pdf.location', 'TASCREPORTS', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (192, 'sso.redirect.loginfail', 'https://renqa.ctb.com/ctb.com/control/main', 'CTB.COM|inors', 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (193, 'sso.redirect.loginfail', 'https://oastest.ctb.com/SessionWeb/login.jsp', 'OAS|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (194, 'sso.redirect.logout', 'https://renqa.ctb.com/ctb.com/control/main', 'CTB.COM|inors', 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (195, 'sso.redirect.logout', 'https://oastest.ctb.com/SessionWeb/login.jsp', 'OAS|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (131, 'role.not.added', 'ROLE_CTB,ROLE_PARENT,ROLE_SUPER,ROLE_EDU_ADMIN', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (132, 'orglvl.user.not.added', '0', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (133, 'orglvl.admin.not.added', '4', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (134, 'hmac.secret.key', 'BTCguSF49hYaPmAfe9Q29LtsQ2X', 'CTB.com|inors', 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (135, 'aws.inors.cacheS3', 'Cache_Keys/INORS/', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (162, 'password.history.day', '3', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (182, 'title.tab.home.application', 'Indiana Online Reporting System', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (164, 'title.tab.application', 'Indiana Online Reporting System', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (184, 'password.expiry', '90', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (186, 'password.expiry.warning', '85', null, 2);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (137, 'orglvl.user.not.added', '0', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (138, 'orglvl.admin.not.added', '4', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (139, 'aws.inors.cacheS3', 'Cache_Keys/TASC/', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (140, 'hmac.secret.key', 'WPZguVF49hXaRuZfe9L29ItsC2I', 'OAS|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (141, 'hmac.secret.key', 'RPEgeVS49XaUu29ItsC2IoZre9C', 'ERESOURCE|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (136, 'role.not.added', 'ROLE_CTB,ROLE_PARENT,ROLE_SUPER,ROLE_EDU_ADMIN', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (161, 'password.history.day', '3', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (181, 'title.tab.home.application', 'TASC Dataonline', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (163, 'title.tab.application', 'TASC-Login', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (183, 'password.expiry', '90', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (185, 'password.expiry.warning', '85', null, 1);
prompt 27 records loaded
prompt Enabling foreign key constraints for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP enable constraint FK_DASH_CONTRACT_PROP;
prompt Enabling triggers for DASH_CONTRACT_PROP...
alter table DASH_CONTRACT_PROP enable all triggers;
set feedback on
set define on
prompt Done.
