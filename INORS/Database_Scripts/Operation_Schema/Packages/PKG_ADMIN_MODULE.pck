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
                                      
                                      
  Procedure Sp_Create_Parent(p_In_Username        In Users.Username%Type,
                             p_In_Dispname        In Users.Display_Username%Type,
                             p_In_Emailid         In Users.Email_Address%Type,
                             p_In_Userstatus      In Users.Activation_Status%Type,
                             p_In_Firsttime_Login In Users.Is_Firsttime_Login%Type,
                             p_In_Password        In Users.Password%Type,
                             p_In_Salt            In Users.Salt%Type,
                             p_In_New_User        In Users.Is_New_User%Type,
                             p_In_InvitaionCode   In Invitation_Code.Invitation_Code%Type,
                             p_In_Mobile          In users.phone_no%Type,
                             p_In_Country         In users.country%Type,
                             p_In_Zip             In users.zipcode%Type,
                             p_In_Street          In users.street%Type,
                             p_In_City            In users.city%Type,
                             p_In_State           In users.state%Type,
                             p_In_lastName        In users.last_name%type,
                             p_In_firstName       In users.first_name%type,
                             p_Out_UserId         Out Number,
                             p_Out_Excep_Err_Msg  Out Varchar2);                                      


end PKG_ADMIN_MODULE;
/
CREATE OR REPLACE Package Body Pkg_Admin_Module Is

  ---- Add admin and sso users.
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


 ---- Add Parent users.
  Procedure Sp_Create_Parent(p_In_Username        In Users.Username%Type,
                             p_In_Dispname        In Users.Display_Username%Type,
                             p_In_Emailid         In Users.Email_Address%Type,
                             p_In_Userstatus      In Users.Activation_Status%Type,
                             p_In_Firsttime_Login In Users.Is_Firsttime_Login%Type,
                             p_In_Password        In Users.Password%Type,
                             p_In_Salt            In Users.Salt%Type,
                             p_In_New_User        In Users.Is_New_User%Type,
                             p_In_InvitaionCode   In Invitation_Code.Invitation_Code%Type,
                             p_In_Mobile          In users.phone_no%Type,
                             p_In_Country         In users.country%Type,
                             p_In_Zip             In users.zipcode%Type,
                             p_In_Street          In users.street%Type,
                             p_In_City            In users.city%Type,
                             p_In_State           In users.state%Type,
                             p_In_lastName        In users.last_name%type,
                             p_In_firstName       In users.first_name%type,
                             p_Out_UserId         Out Number,
                             p_Out_Excep_Err_Msg  Out Varchar2) Is
  
    Incount           Number := 0;
    Userseqid         Users.Userid%Type := 0;
    orgUserseqid      org_users.org_user_id%Type := 0;
    claimAvailability number := 0;
    p_Node_Id         number :=0;
  
  Begin
  
    SELECT IC.TOTAL_AVAILABLE
      into claimAvailability
      FROM INVITATION_CODE IC
     WHERE IC.INVITATION_CODE = p_In_InvitaionCode
       AND IC.ACTIVATION_STATUS = 'AC'
       AND IC.EXPIRATION_DATE >= sysdate;
  
    if claimAvailability > 0 then
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
           last_name,
           first_name,
           middle_name,
           Email_Address,
           PHONE_NO,
           COUNTRY,
           ZIPCODE,
           STREET,
           CITY,
           STATE,
           CUSTOMERID,
           IS_FIRSTTIME_LOGIN,
           IS_NEW_USER,
           ACTIVATION_STATUS,
           CREATED_DATE_TIME,
           PASSWORD,
           SALT)
        Values
          (Userseqid,
           p_In_Username,
           p_In_Dispname,
           p_In_lastName,
           p_In_firstName,
           '',
           p_In_Emailid,
           p_In_Mobile,
           p_In_Country,
           p_In_Zip,
           p_In_Street,
           p_In_City,
           p_In_State,
           (SELECT DISTINCT CPL.CUSTOMERID
              FROM CUST_PRODUCT_LINK CPL, INVITATION_CODE INV
             WHERE CPL.CUST_PROD_ID = INV.CUST_PROD_ID
               AND INV.ACTIVATION_STATUS = 'AC'
               AND INV.INVITATION_CODE = p_In_InvitaionCode),
           p_In_Firsttime_Login,
           p_In_New_User,
           p_In_Userstatus,
           SYSDATE,
           p_In_Password,
           p_In_Salt);
      
        SELECT M.ORG_NODEID
          into p_Node_Id
          FROM INVITATION_CODE M
         WHERE M.INVITATION_CODE = p_In_InvitaionCode;
      
        Select Org_User_Id_Seq.Nextval Into orgUserseqid From Dual;
      
        Insert Into Org_Users
          (Org_User_Id,
           Userid,
           Org_Nodeid,
           Org_Node_Level,
           Adminid,
           Activation_Status,
           Created_Date_Time)
        Values
          (orgUserseqid,
           Userseqid,
           p_Node_Id,
           3,
           (SELECT CUST_PRODUCT_LINK.ADMINID
              FROM INVITATION_CODE, CUST_PRODUCT_LINK
             WHERE INVITATION_CODE.CUST_PROD_ID =
                   CUST_PRODUCT_LINK.CUST_PROD_ID
               AND INVITATION_CODE = p_In_InvitaionCode),
           'AC',
           Sysdate);
      
        For i In (Select Roleid
                    From Role
                   Where Role_Name In ('ROLE_USER', 'ROLE_PARENT')) Loop
          Insert Into User_Role
            (Roleid, Userid, Created_Date_Time)
          Values
            (i.Roleid, Userseqid, Sysdate);
        
        End Loop;
      
        INSERT INTO INVITATION_CODE_CLAIM
          (INVITATION_CODE_CLAIM_ID,
           ICID,
           ORG_USER_ID,
           ACTIVATION_STATUS,
           CLAIM_DATE,
           UPDATED_DATE_TIME)
        VALUES
          (INVITATION_CODE_CLAIM_ID_SEQ.NEXTVAL,
           (SELECT ICID
              FROM INVITATION_CODE INV
             where INV.ACTIVATION_STATUS = 'AC'
               and INV.INVITATION_CODE = p_In_InvitaionCode
               and rownum = 1),
           orgUserseqid,
           'AC',
           SYSDATE,
           SYSDATE);
      
        UPDATE INVITATION_CODE
             SET TOTAL_ATTEMPT   = (SELECT TOTAL_ATTEMPT
                                      FROM INVITATION_CODE
                                     WHERE INVITATION_CODE =
                                           p_In_InvitaionCode
                                       AND ACTIVATION_STATUS = 'AC') + 1,
               TOTAL_AVAILABLE   = (SELECT TOTAL_AVAILABLE
                                      FROM INVITATION_CODE
                                     WHERE INVITATION_CODE =
                                           p_In_InvitaionCode
                                       AND ACTIVATION_STATUS = 'AC') - 1,
               UPDATED_DATE_TIME = SYSDATE
         WHERE INVITATION_CODE = p_In_InvitaionCode;
      
      End If;
      
      p_Out_UserId := Userseqid;
    
    end if;
  
  Exception
    When Others Then
      p_Out_Excep_Err_Msg := Upper(Substr(Sqlerrm, 12, 255));
    
  End Sp_Create_Parent;


End Pkg_Admin_Module;
/
