---Steps of Deployment

1. Hierarchy -- Hierarchy Script.sql --Phase 1
2. GHI UDB   -- GHI UDB Script.sql   --Phase 2 and Phase 3
3. GHI Reconcilation --Recon.sql


For New Customer:

1> We have to use new customer creation proc PKG_DRC_TASC_CUSTOMER_CREATION.pck
2> Make sure that all the new demo are already populated for the base customer.If not then execute
   SP_TASC_DEMO_ADD_GHI.prc and sp_tasc_upd_demo.prc for the base customer and then follow the step 1.

   
For Old Customer:

1> To add new GHI demo for the customer please execute 
   SP_TASC_DEMO_ADD_GHI.prc and sp_tasc_upd_demo.prc
   
To configure all customer at a time please execute below two block..

--Populate New Demo
DECLARE
V_CUSTOMER_SUCCESS VARCHAR2(500);
BEGIN
FOR I IN (SELECT CUSTOMERID FROM CUSTOMER_INFO) 
  LOOP 
    SP_TASC_DEMO_ADD_GHI(IN_CUSTOMER_ID =>I.CUSTOMERID,OUT_STATUS => V_CUSTOMER_SUCCESS);
    DBMS_OUTPUT.PUT_LINE(V_CUSTOMER_SUCCESS);
  END LOOP;                                            
END;


--Update Existing Demo
DECLARE
V_CUSTOMER_SUCCESS VARCHAR2(500);
BEGIN
FOR I IN (SELECT CUSTOMERID FROM CUSTOMER_INFO) 
  LOOP 
    SP_TASC_UPD_DEMO(IN_CUSTOMER_ID =>I.CUSTOMERID,OUT_STATUS => V_CUSTOMER_SUCCESS);
    DBMS_OUTPUT.PUT_LINE(V_CUSTOMER_SUCCESS);
  END LOOP;                                            
END;   