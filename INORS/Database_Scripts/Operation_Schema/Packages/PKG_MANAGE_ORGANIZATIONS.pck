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
         AND OND.ORG_NODE_LEVEL <> 0
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

END PKG_MANAGE_ORGANIZATIONS;
/
