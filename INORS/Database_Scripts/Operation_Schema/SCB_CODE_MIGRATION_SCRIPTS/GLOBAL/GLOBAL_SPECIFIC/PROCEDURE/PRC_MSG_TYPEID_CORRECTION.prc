CREATE OR REPLACE PROCEDURE PRC_MSG_TYPEID_CORRECTION(P_IN_PROJECTID NUMBER) AS
  V_MSG_TYPEID DASH_MESSAGE_TYPE.MSG_TYPEID%TYPE := 0;
BEGIN

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID IN
         (SELECT MSG_TYPEID
            FROM DASH_MESSAGE_TYPE
           WHERE UPPER(MESSAGE_NAME) IN
                 ('PARENT LOG IN', 'TEACHER LOG IN', 'INORS HOME PAGE'));

  DELETE FROM DASH_MESSAGE_TYPE
   WHERE UPPER(MESSAGE_NAME) IN
         ('PARENT LOG IN', 'TEACHER LOG IN', 'INORS HOME PAGE');

  FOR REC IN (SELECT MESSAGE_NAME, COUNT(DISTINCT MSG_TYPEID)
                FROM DASH_MESSAGE_TYPE
               WHERE CUST_PROD_ID IN
                     (SELECT CPL.CUST_PROD_ID
                        FROM CUST_PRODUCT_LINK CPL, PRODUCT P
                       WHERE CPL.PRODUCTID = P.PRODUCTID
                         AND P.PROJECTID = P_IN_PROJECTID)
               GROUP BY MESSAGE_NAME
              HAVING COUNT(DISTINCT MSG_TYPEID) > 1) LOOP
  
    IF UPPER(REC.MESSAGE_NAME) = 'MORE INFO' THEN
      V_MSG_TYPEID := 1050;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'CHILDREN OVERVIEW' THEN
      V_MSG_TYPEID := 1038;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'REPORT PRIVACY NOTICE' THEN
      V_MSG_TYPEID := 1060;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'TEACHER HOME PAGE' THEN
      V_MSG_TYPEID := 1097;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'DATALOAD MESSAGE' THEN
      V_MSG_TYPEID := 1056;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'GROUP DOWNLOAD INSTRUCTION' THEN
      V_MSG_TYPEID := 1085;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'EMBARGO NOTICE' THEN
      V_MSG_TYPEID := 1054;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'FOOT NOTE' THEN
      V_MSG_TYPEID := 1055;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'REPORT MESSAGE' THEN
      V_MSG_TYPEID := 1057;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'REPORT LEGEND' THEN
      V_MSG_TYPEID := 1059;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'REPORT PURPOSE' THEN
      V_MSG_TYPEID := 1058;
    ELSIF UPPER(REC.MESSAGE_NAME) = 'COMMON LOG IN' THEN
      V_MSG_TYPEID := 1037;
    END IF;
  
    UPDATE DASH_MESSAGES
       SET MSG_TYPEID = V_MSG_TYPEID, UPDATED_DATE_TIME = SYSDATE
     WHERE CUST_PROD_ID IN
           (SELECT CPL.CUST_PROD_ID
              FROM CUST_PRODUCT_LINK CPL, PRODUCT P
             WHERE CPL.PRODUCTID = P.PRODUCTID
               AND P.PROJECTID = P_IN_PROJECTID)
       AND MSG_TYPEID IN
           (SELECT DISTINCT MSG_TYPEID
              FROM DASH_MESSAGE_TYPE
             WHERE MESSAGE_NAME = REC.MESSAGE_NAME);
  
    COMMIT;
  
    UPDATE DASH_MESSAGE_TYPE
       SET MSG_TYPEID = V_MSG_TYPEID, UPDATED_DATE_TIME = SYSDATE
     WHERE CUST_PROD_ID IN
           (SELECT CPL.CUST_PROD_ID
              FROM CUST_PRODUCT_LINK CPL, PRODUCT P
             WHERE CPL.PRODUCTID = P.PRODUCTID
               AND P.PROJECTID = P_IN_PROJECTID)
       AND MESSAGE_NAME = REC.MESSAGE_NAME;
  
    COMMIT;
  
  END LOOP;

END PRC_MSG_TYPEID_CORRECTION;
/
