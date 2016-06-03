--FOR WISC
DECLARE
  V_PROJECTID PROJECT_DIM.PROJECTID%TYPE;
BEGIN

  SELECT PROJECTID
    INTO V_PROJECTID
    FROM PROJECT_DIM
   WHERE UPPER(PROJECT_NAME) LIKE 'WISCONSIN%';

  DELETE FROM DASH_CONTRACT_PROP WHERE PROJECTID = V_PROJECTID;

  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'sso.redirect.logout',
     'https://mo.drcedirect.com/',
     'DRC|wisc',
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'sso.redirect.loginfail',
     'https://mo.drcedirect.com/PublicContent/Errors/ErrorPage.aspx',
     'DRC|wisc',
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'email.sender',
     'maphelpdesk@ctb.com',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'support.email',
     'amit.dhara@ctb.com',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'default.custProdId.gscm',
     '5047',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'hmac.secret.key',
     'WBIgdSF66cYsZoNfe5S82IrsN9P',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'password.history.day',
     '3',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'title.tab.home.application',
     'DRC Online Reporting System',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'title.tab.application',
     'DRC Online Reporting System',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'password.expiry',
     '90',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'password.expiry.warning',
     '85',
     NULL,
     V_PROJECTID);
  INSERT INTO DASH_CONTRACT_PROP
    (DB_PROPERTYID,
     DB_PROPERTY_NAME,
     DB_PROPERY_VALUE,
     SSO_SOURCE,
     PROJECTID)
  VALUES
    ((SELECT MAX(DB_PROPERTYID) + 1 FROM DASH_CONTRACT_PROP),
     'static.pdf.location',
     'DRCREPORTS',
     NULL,
     V_PROJECTID);

  COMMIT;

END;
/
