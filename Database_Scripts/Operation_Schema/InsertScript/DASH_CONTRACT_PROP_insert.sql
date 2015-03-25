delete from dash_contract_prop where projectid = 3;

insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,
   DB_PROPERTY_NAME,
   DB_PROPERY_VALUE,
   SSO_SOURCE,
   PROJECTID)
values
  ((select max(DB_PROPERTYID) + 1 from dash_contract_prop),
   'default.custProdId.gscm',
   '3001',
   null,
   1);
insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,
   DB_PROPERTY_NAME,
   DB_PROPERY_VALUE,
   SSO_SOURCE,
   PROJECTID)
values
  ((select max(DB_PROPERTYID) + 1 from dash_contract_prop),
   'default.custProdId.gscm',
   '5001',
   null,
   2);
insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,
   DB_PROPERTY_NAME,
   DB_PROPERY_VALUE,
   SSO_SOURCE,
   PROJECTID)
values
  ((select max(DB_PROPERTYID) + 1 from dash_contract_prop),
   'default.custProdId.gscm',
   '5027',
   null,
   3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'hmac.secret.key', 'MPIgsVF58hXsRuKfe7U92IrsC2I', 'DRC|usmo', 3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'password.history.day', '3', null, 3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'title.tab.home.application', 'DRC Online Reporting System', null, 3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'title.tab.application', 'DRC Online Reporting System', null, 3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'password.expiry', '90', null, 3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'password.expiry.warning', '85', null, 3);
insert into DASH_CONTRACT_PROP (db_propertyid, db_property_name, db_propery_value, sso_source, projectid)
values ((select max(DB_PROPERTYID) + 1 from dash_contract_prop), 'static.pdf.location', 'DRCREPORTS/qa', null, 3);
