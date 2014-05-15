create or replace package PKG_ADMIN_MODULE is

  -- Author  : D-ABIR_DUTTA
  -- Created : 3/28/2014 9:31:03 AM
  -- Purpose : Admin module
  
  PROCEDURE SP_CREATE_USER (          P_IN_USERNAME         In USERS.USERNAME%TYPE,
                                      P_IN_DISPNAME         In USERS.DISPLAY_USERNAME%Type,
                                      P_IN_EMAILID          IN USERS.EMAIL_ADDRESS%TYPE,
                                      P_IN_USERSTATUS       IN USERS.ACTIVATION_STATUS%TYPE,
                                      P_IN_FIRSTTIME_LOGIN  In users.is_firsttime_login%Type,
                                      P_IN_PASSWORD         In USERS.PASSWORD%TYPE, 
                                      P_IN_SALT             In USERS.SALT%Type,
                                      P_IN_NEW_USER         In USERS.IS_NEW_USER%Type,
                                      P_IN_CUSTOMERID       In CUSTOMER_INFO.CUSTOMERID%Type,
                                      P_IN_ORGNODEID        In ORG_NODE_DIM.ORG_NODEID%Type,
                                      P_IN_ORG_LVL          In ORG_NODE_DIM.ORG_NODE_LEVEL%Type,
                                      P_IN_CUST_PROD_ID     In CUST_PRODUCT_LINK.CUST_PROD_ID%Type,
                                      P_IN_ACTIVE_FLAG      In Varchar2,
                                      P_IN_ROLES            In Varchar2,
                                      P_OUT_NODE_ID         OUT Number,
                                      P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);


end PKG_ADMIN_MODULE;
/
CREATE OR REPLACE Package Body Pkg_Admin_Module Is

  ----PROCEDURE TO FETCH ALL MIG_RESULTS_GRT BASED ON DISTRICTID.
  Procedure Sp_Create_User(p_In_Username        In Users.Username%Type,
                           p_In_Dispname        In Users.Display_Username%Type,
                           p_In_Emailid         In Users.Email_Address%Type,
                           p_In_Userstatus      In Users.Activation_Status%Type,
                           p_In_Firsttime_Login In Users.Is_Firsttime_Login%Type,
                           p_In_Password        In Users.Password%Type,
                           p_In_Salt            In Users.Salt%Type,
                           p_In_New_User        In Users.Is_New_User%Type,
                           p_In_Customerid      In Customer_Info.Customerid%Type,
                           p_In_Orgnodeid       In Org_Node_Dim.Org_Nodeid%Type,
                           p_In_Org_Lvl         In Org_Node_Dim.Org_Node_Level%Type,
                           p_In_Cust_Prod_Id    In Cust_Product_Link.Cust_Prod_Id%Type,
                           p_In_Active_Flag     In Varchar2,
                           p_In_Roles           In Varchar2,
                           p_Out_Node_Id        Out Number,
                           p_Out_Excep_Err_Msg  Out Varchar2) Is
  
    Incount   Number := 0;
    Userseqid Users.Userid%Type := 0;
  
  Begin
    Select Count(1)
      Into Incount
      From Users
     Where Upper(Username) = Upper(p_In_Username);
  
    If Incount = 0 Then
      Select User_Id_Seq.Nextval Into Userseqid From Dual;
    
      Insert Into Users
        (Userid,
         Username,
         Display_Username,
         Email_Address,
         Activation_Status,
         Is_Firsttime_Login,
         Password,
         Salt,
         Is_New_User,
         Customerid)
      Values
        (Userseqid,
         p_In_Username,
         p_In_Dispname,
         p_In_Emailid,
         p_In_Userstatus,
         p_In_Firsttime_Login,
         p_In_Password,
         p_In_Salt,
         p_In_New_User,
         p_In_Customerid);
    
      Insert Into Org_Users
        (Org_User_Id,
         Userid,
         Org_Nodeid,
         Org_Node_Level,
         Adminid,
         Activation_Status,
         Created_Date_Time)
      Values
        (Org_User_Id_Seq.Nextval,
         Userseqid,
         p_In_Orgnodeid,
         p_In_Org_Lvl,
         (Select Adminid
            From Cust_Product_Link
           Where Cust_Prod_Id = p_In_Cust_Prod_Id),
         p_In_Active_Flag,
         Sysdate);
    
      For i In (Select Roleid
                  From Role
                 Where Role_Name In (With t As (Select p_In_Roles From Dual)
                -- end of sample data
                  Select Regexp_Substr(p_In_Roles, '[^,]+', 1, Level)
                    From t
                  Connect By Level <=
                             Length(Regexp_Replace(p_In_Roles, '[^,]*')) + 1)
                )
      
       Loop
        Insert Into User_Role
          (Roleid, Userid, Created_Date_Time)
        Values
          (i.Roleid, Userseqid, Sysdate);
      
      End Loop;
    
      p_Out_Node_Id := p_In_Orgnodeid;
    
    End If;
  
  Exception
    When Others Then
      p_Out_Excep_Err_Msg := Upper(Substr(Sqlerrm, 12, 255));
    
  End Sp_Create_User;

End Pkg_Admin_Module;
/
