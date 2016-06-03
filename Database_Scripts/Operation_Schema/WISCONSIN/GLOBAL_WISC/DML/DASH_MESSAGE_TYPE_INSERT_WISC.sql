--FOR WISC
DECLARE
  V_PROJECTID            PROJECT_DIM.PROJECTID%TYPE;
  V_DEFAULT_CUST_PROD_ID CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE := 0;
BEGIN

  SELECT PROJECTID
    INTO V_PROJECTID
    FROM PROJECT_DIM
   WHERE UPPER(PROJECT_NAME) LIKE 'WISCONSIN%';

  SELECT DB_PROPERY_VALUE
    INTO V_DEFAULT_CUST_PROD_ID
    FROM DASH_CONTRACT_PROP
   WHERE UPPER(DB_PROPERTY_NAME) = 'DEFAULT.CUSTPRODID.GSCM'
     AND PROJECTID = V_PROJECTID;

  DELETE FROM DASH_MESSAGES WHERE CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  DELETE FROM DASH_MESSAGE_TYPE
   WHERE CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1087,
     'Common Header',
     'GSCM',
     'Message configuration for common header',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1099,
     'Teacher Home Footer',
     'GSCM',
     'Footer for teacher home screen',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1097,
     'Teacher Home Page',
     'GSCM',
     'Content of Teacher Home Page',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1089,
     'Teacher Login Footer',
     'GSCM',
     'Footer for teacher login screen',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1095,
     'Teacher Login Outage Content',
     'GSCM',
     'Content of outage for Teacher Login page',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1093,
     'Teacher Login Page Content',
     'GSCM',
     'Image or Content for Teacher Login page',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  INSERT INTO DASH_MESSAGE_TYPE
    (MSG_TYPEID,
     MESSAGE_NAME,
     MESSAGE_TYPE,
     DESCRIPTION,
     CUST_PROD_ID,
     CREATED_DATE_TIME)
  VALUES
    (1091,
     'Menu Message',
     'PSCM',
     'Message configuration for right sided menu',
     V_DEFAULT_CUST_PROD_ID,
     SYSDATE);

  COMMIT;

END;
/
