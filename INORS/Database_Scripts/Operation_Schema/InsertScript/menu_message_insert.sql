BEGIN
  FOR REC_CUST_PROD_ID IN (SELECT CPL.CUST_PROD_ID
                             FROM CUST_PRODUCT_LINK CPL, CUSTOMER_INFO CI
                            WHERE CPL.CUSTOMERID = CI.CUSTOMERID
                              AND CPL.ACTIVATION_STATUS = 'AC'
                           MINUS
                           SELECT CUST_PROD_ID
                             FROM DASH_MESSAGE_TYPE
                            WHERE MSG_TYPEID = 1091) LOOP
  
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
       REC_CUST_PROD_ID.CUST_PROD_ID,
       SYSDATE);
  
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE(UPPER(SUBSTR(SQLERRM, 0, 255)));
END;
