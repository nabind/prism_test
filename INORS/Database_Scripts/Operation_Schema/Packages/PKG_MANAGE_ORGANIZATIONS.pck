create or replace package PKG_MANAGE_ORGANIZATIONS is

  -- Author  : tcs
  -- Created : 9/15/2014 3:09:40 PM
  -- Purpose : Manage Organizations

  -- Public type declarations
  TYPE GET_REF_CURSOR IS REF CURSOR;

  PROCEDURE SP_GET_CURR_TENANT_DETAILS(P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_CUSTOMERID     IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);

  PROCEDURE SP_GET_TENANT_DETAILS_NON_ACSI(P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                           P_IN_PARENT_ORG_NODEID IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                           P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                           P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_GET_TENANT_DETAILS(P_IN_PARENT_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_CUSTOMERID               IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                  P_IN_CUST_PROD_ID             IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_OUT_REF_CURSOR              OUT GET_REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG           OUT VARCHAR2);

  PROCEDURE SP_GET_TENANT_DETAILS_NON_ROOT(P_IN_PARENT_ORG_NODEID   IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_CUSTOMERID          IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                           P_IN_CUST_PROD_ID        IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                           P_IN_ORG_MODE            IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                           P_OUT_REF_CURSOR         OUT GET_REF_CURSOR,
                                           P_OUT_EXCEP_ERR_MSG      OUT VARCHAR2);

  PROCEDURE SP_SEARCH_ORGANNIZATION( P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                     P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                     P_IN_ORG_NODE_NAME     IN ORG_NODE_DIM.ORG_NODE_NAME%TYPE,
                                     P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                     P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                     P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                     P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

  PROCEDURE SP_SEARCH_ORG_AUTO_COMPLETE( P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                         P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                         P_IN_ORG_NODE_NAME     IN ORG_NODE_DIM.ORG_NODE_NAME%TYPE,
                                         P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                         P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                         P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                         P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

   PROCEDURE SP_GET_ORG_HIER_ON_REDIRECT(P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                         P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                         P_IN_PARENT_ORG_NODEID IN ORG_NODE_DIM.PARENT_ORG_NODEID%TYPE,
                                         --P_IN_USERID            IN USERS.USERID%TYPE,
                                         P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                         P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

   PROCEDURE SP_GET_ORGANIZATION_LIST (P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                       P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

   PROCEDURE SP_GET_ORG_CHILDREN_LIST (P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                       P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2);

end PKG_MANAGE_ORGANIZATIONS;
/
CREATE OR REPLACE PACKAGE BODY PKG_MANAGE_ORGANIZATIONS IS

  PROCEDURE SP_GET_CURR_TENANT_DETAILS(P_IN_ORG_NODEID     IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_CUSTOMERID     IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                       P_OUT_REF_CURSOR    OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR
      SELECT ORG_NODEID,
             ORG_NODE_NAME,
             PARENT_ORG_NODEID,
             ORG_NODE_LEVEL,
             ORG_MODE
        FROM ORG_NODE_DIM
       WHERE ORG_NODEID = P_IN_ORG_NODEID
         AND CUSTOMERID = P_IN_CUSTOMERID
       ORDER BY ORG_NODE_NAME;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_CURR_TENANT_DETAILS;

  PROCEDURE SP_GET_TENANT_DETAILS_NON_ACSI(P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                           P_IN_PARENT_ORG_NODEID IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                           P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                           P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR

      SELECT OND.ORG_NODEID,
             OND.ORG_NODE_NAME,
             OND.PARENT_ORG_NODEID,
             OND.ORG_NODE_LEVEL,
             OND.ORG_MODE
        FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL
       WHERE OPL.ORG_NODEID = OND.ORG_NODEID
         AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND OND.ORG_MODE = P_IN_ORG_MODE
         AND OND.PARENT_ORG_NODEID = P_IN_PARENT_ORG_NODEID
         AND OND.ORG_NODEID = P_IN_ORG_NODEID
         AND OND.CUSTOMERID = P_IN_CUSTOMERID
      UNION
      SELECT OND.ORG_NODEID,
             OND.ORG_NODE_NAME,
             OND.PARENT_ORG_NODEID,
             OND.ORG_NODE_LEVEL,
             OND.ORG_MODE
        FROM ORG_NODE_DIM OND
       WHERE OND.ORG_NODEID = P_IN_ORG_NODEID
         AND OND.ORG_NODE_LEVEL = 1
       ORDER BY ORG_NODE_NAME;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_TENANT_DETAILS_NON_ACSI;

  PROCEDURE SP_GET_TENANT_DETAILS(P_IN_PARENT_ORG_NODEID IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                  P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                  P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                  P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                  P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS

  BEGIN
    OPEN P_OUT_REF_CURSOR FOR

      /*SELECT OND.ORG_NODEID,
             OND.ORG_NODE_NAME,
             OND.PARENT_ORG_NODEID,
             OND.ORG_NODE_LEVEL,
             OND.ORG_MODE
        FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL
       WHERE OPL.ORG_NODEID = OND.ORG_NODEID
         AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND PARENT_ORG_NODEID = P_IN_PARENT_ORG_NODEID
         AND CUSTOMERID = P_IN_CUSTOMERID
         AND OND.ORG_NODE_LEVEL <> 0
       ORDER BY ORG_NODE_NAME;*/
       
       SELECT OND2.ORG_NODEID,
             OND2.ORG_NODE_NAME,
             OND2.PARENT_ORG_NODEID,
             OND2.ORG_NODE_LEVEL,
             OND2.ORG_MODE
        FROM (SELECT ORG_NODEID,ORG_NODE_NAME,PARENT_ORG_NODEID,ORG_NODE_LEVEL,ORG_MODE FROM ORG_NODE_DIM OND
              WHERE OND.CUSTOMERID = P_IN_CUSTOMERID AND OND.ORG_NODE_LEVEL <> 0 AND OND.PARENT_ORG_NODEID = P_IN_PARENT_ORG_NODEID) OND2,
             ORG_PRODUCT_LINK OPL
       WHERE  OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID
          --AND OND.PARENT_ORG_NODEID = 0
        -- AND OND.CUSTOMERID = 1000
        -- AND OND.ORG_NODE_LEVEL > 0
         AND OPL.ORG_NODEID = OND2.ORG_NODEID
       ORDER BY ORG_NODE_NAME;
       
       

       

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));

  END SP_GET_TENANT_DETAILS;

  PROCEDURE SP_GET_TENANT_DETAILS_NON_ROOT(P_IN_PARENT_ORG_NODEID IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                           P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                           P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                           P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                           P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                           P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS
  BEGIN
    OPEN P_OUT_REF_CURSOR FOR

      SELECT OND.ORG_NODEID,
             OND.ORG_NODE_NAME,
             OND.PARENT_ORG_NODEID,
             OND.ORG_NODE_LEVEL,
             OND.ORG_MODE
        FROM ORG_NODE_DIM OND, ORG_PRODUCT_LINK OPL
       WHERE OPL.ORG_NODEID = OND.ORG_NODEID
         AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID
         AND PARENT_ORG_NODEID = P_IN_PARENT_ORG_NODEID
         AND CUSTOMERID = P_IN_CUSTOMERID
         AND OND.ORG_MODE = P_IN_ORG_MODE
         AND OND.ORG_NODE_LEVEL <> 0
       ORDER BY ORG_NODE_NAME;

  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_TENANT_DETAILS_NON_ROOT;

   PROCEDURE SP_SEARCH_ORGANNIZATION(P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                     P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                     P_IN_ORG_NODE_NAME     IN ORG_NODE_DIM.ORG_NODE_NAME%TYPE,
                                     P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                     P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                     P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                     P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS

   BEGIN
    OPEN P_OUT_REF_CURSOR FOR

      SELECT O.ORG_NODEID,
             O.ORG_NODE_NAME,
             (SELECT NVL(COUNT(1), 0)
                FROM ORG_NODE_DIM M
               WHERE M.PARENT_ORG_NODEID = O.ORG_NODEID
                 AND M.CUSTOMERID = P_IN_CUSTOMERID
                 AND EXISTS
               (SELECT 1
                        FROM ORG_PRODUCT_LINK OPL
                       WHERE M.ORG_NODEID = OPL.ORG_NODEID
                         AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)) CHILD_ORG_NO
        FROM ORG_NODE_DIM O
       WHERE UPPER(O.ORG_NODE_NAME) LIKE UPPER(P_IN_ORG_NODE_NAME)
         AND ORG_MODE = P_IN_ORG_MODE
         AND EXISTS (SELECT 1
                FROM ORG_PRODUCT_LINK OPL
               WHERE O.ORG_NODEID = OPL.ORG_NODEID
                 AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
         AND O.ORG_NODE_LEVEL > =
             (SELECT ORG_NODE_LEVEL
                FROM ORG_NODE_DIM
               WHERE ORG_NODEID = P_IN_ORG_NODEID)
      CONNECT BY NOCYCLE PRIOR O.ORG_NODEID = PARENT_ORG_NODEID
       START WITH ORG_NODEID = P_IN_ORG_NODEID;


   EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
   END SP_SEARCH_ORGANNIZATION;


   PROCEDURE SP_SEARCH_ORG_AUTO_COMPLETE(P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                         P_IN_CUST_PROD_ID      IN CUST_PRODUCT_LINK.CUST_PROD_ID%TYPE,
                                         P_IN_ORG_NODE_NAME     IN ORG_NODE_DIM.ORG_NODE_NAME%TYPE,
                                         P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                         P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                         P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                         P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) IS

   BEGIN
    OPEN P_OUT_REF_CURSOR FOR

      SELECT ABC.ORG_NODE_NAME
        FROM (SELECT O.ORG_NODE_NAME, O.ORG_NODE_LEVEL
                FROM ORG_NODE_DIM O
               WHERE O.CUSTOMERID = P_IN_CUSTOMERID
                 AND O.ORG_MODE = P_IN_ORG_MODE
                 AND EXISTS
               (SELECT 1
                        FROM ORG_PRODUCT_LINK OPL
                       WHERE O.ORG_NODEID = OPL.ORG_NODEID
                         AND OPL.CUST_PROD_ID = P_IN_CUST_PROD_ID)
               START WITH O.ORG_NODEID = P_IN_ORG_NODEID
              CONNECT BY NOCYCLE PRIOR O.ORG_NODEID = PARENT_ORG_NODEID
               ORDER BY O.ORG_NODE_NAME) ABC
       WHERE UPPER(ABC.ORG_NODE_NAME) LIKE UPPER(P_IN_ORG_NODE_NAME)
         AND ROWNUM <= 100;

   EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
   END SP_SEARCH_ORG_AUTO_COMPLETE;


   PROCEDURE SP_GET_ORG_HIER_ON_REDIRECT(P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                         P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                         P_IN_PARENT_ORG_NODEID IN ORG_NODE_DIM.PARENT_ORG_NODEID%TYPE,
                                         --P_IN_USERID            IN USERS.USERID%TYPE,
                                         P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                         P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) Is
   BEGIN
      OPEN P_OUT_REF_CURSOR For

      SELECT TO_CHAR(O.ORG_NODEID) AS ORG_ID,
             O.ORG_NODE_NAME,
             O.PARENT_ORG_NODEID,
             O.ORG_NODE_LEVEL
        FROM ORG_NODE_DIM O
       WHERE O.CUSTOMERID = P_IN_CUSTOMERID
       START WITH ORG_NODEID = P_IN_ORG_NODEID
      CONNECT BY PRIOR PARENT_ORG_NODEID = ORG_NODEID
             AND O.ORG_NODE_LEVEL >=
                 (SELECT DISTINCT U.ORG_NODE_LEVEL
                    FROM ORG_USERS U
                   WHERE U.ORG_NODEID = P_IN_PARENT_ORG_NODEID)
                    -- AND U.USERID = P_IN_USERID)
        ORDER BY O.ORG_NODE_LEVEL, O.PARENT_ORG_NODEID, O.ORG_NODE_NAME;



   EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
   END SP_GET_ORG_HIER_ON_REDIRECT;


   PROCEDURE SP_GET_ORGANIZATION_LIST (P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                       P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) Is

   BEGIN
    OPEN P_OUT_REF_CURSOR For
       SELECT ORG_NODEID,
              ORG_NODE_NAME,
              PARENT_ORG_NODEID,
              TO_CHAR(P_IN_ORG_NODEID) AS SELECTED_ORG_ID,
              ORG_NODE_LEVEL,
              (SELECT NVL(COUNT(1), 0)
                 FROM ORG_NODE_DIM
                WHERE PARENT_ORG_NODEID = P_IN_ORG_NODEID
                  AND CUSTOMERID = P_IN_CUSTOMERID
                  AND ORG_MODE = P_IN_ORG_MODE) CHILD_ORG_NO,
              (SELECT NVL(SUM(COUNT(1)), 0)
                 FROM USERS U, ORG_USERS O
                WHERE U.USERID = O.USERID
                  AND U.ACTIVATION_STATUS IN ('AC', 'SS')
                  AND O.ORG_NODEID = D.ORG_NODEID
                GROUP BY O.ORG_NODEID) USER_NO
         FROM ORG_NODE_DIM D
        WHERE D.ORG_NODEID = P_IN_ORG_NODEID
          AND CUSTOMERID = P_IN_CUSTOMERID
        ORDER BY ORG_NODE_NAME;

   EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
   END SP_GET_ORGANIZATION_LIST;


  PROCEDURE SP_GET_ORG_CHILDREN_LIST ( P_IN_ORG_NODEID        IN ORG_NODE_DIM.ORG_NODEID%TYPE,
                                       P_IN_CUSTOMERID        IN ORG_NODE_DIM.CUSTOMERID%TYPE,
                                       P_IN_ORG_MODE          IN ORG_NODE_DIM.ORG_MODE%TYPE,
                                       P_OUT_REF_CURSOR       OUT GET_REF_CURSOR,
                                       P_OUT_EXCEP_ERR_MSG    OUT VARCHAR2) Is

   BEGIN
    OPEN P_OUT_REF_CURSOR For

    SELECT ORG_NODEID,
           ORG_NODE_NAME,
           PARENT_ORG_NODEID,
           TO_CHAR(P_IN_ORG_NODEID) AS SELECTED_ORG_ID,
           ORG_NODE_LEVEL,
           (SELECT NVL(COUNT(1), 0)
              FROM ORG_NODE_DIM
             WHERE PARENT_ORG_NODEID = ORG_NODEID
               AND CUSTOMERID = P_IN_CUSTOMERID
               AND ORG_MODE = P_IN_ORG_MODE) CHILD_ORG_NO,
           (SELECT NVL(SUM(COUNT(1)), 0)
              FROM USERS U, ORG_USERS O
             WHERE U.USERID = O.USERID
               AND U.ACTIVATION_STATUS IN ('AC', 'SS')
               AND O.ORG_NODEID = D.ORG_NODEID
             GROUP BY O.ORG_NODEID) USER_NO
      FROM ORG_NODE_DIM D
     WHERE D.PARENT_ORG_NODEID = P_IN_ORG_NODEID
       AND CUSTOMERID = P_IN_CUSTOMERID
     ORDER BY ORG_NODE_NAME;

   EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_ORG_CHILDREN_LIST;

END PKG_MANAGE_ORGANIZATIONS;
/
