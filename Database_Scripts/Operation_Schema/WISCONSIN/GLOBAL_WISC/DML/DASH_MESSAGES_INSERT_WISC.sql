--FOR WISC
SET DEFINE OFF;
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

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1087
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1087,
     '<p><img alt="" border="0" height="54" hspace="0" src="themes/wisc/img/logoDRCWI.png?v=1" style="height:54px;margin-top:0px;margin-bottom:0px;margin-left:0px;margin-right:0px;border:0px solid black;" vspace="0" /></p>',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1089
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1089,
     q'[<p>Copyright &copy; 2016 Data Recognition Corporation. All rights reserved. Read our <a href="http://www.datarecognitioncorp.com/Pages/privacy.aspx" style="text-decoration: underline;" target="_blank">Privacy Policy</a>.</p>
<p>DRC/Wisconsin Help Desk: Toll Free 1-800-459-6530 Email: <u><a href="mailto:WIHelpdesk@datarecognitioncorp.com">WIHelpdesk@datarecognitioncorp.com</a></u></p>]',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1093
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1093,
     q'[<div><img alt="" border="0" height="168" hspace="0" src="themes/usmo/img/login_welcome_mo.jpg" style="width:715px;height:168px;margin-top:0px;margin-bottom:0px;margin-left:0px;margin-right:0px;border:0px solid black;" vspace="0" width="715" /></div>

<div>&nbsp;</div>

<div class="boxsshade scrollable custom-scroll" style="position: relative;">
<div class="rowTwo">
<div class="h1style" style="margin-top:5px;margin-left: 0px; text-shadow: 0px 1px 5px rgba(0,0,0,0.25);"><b>Welcome to the Wisconsin Forward Exam Reporting System</b></div>

<div class="h1style" style="margin-left: 0px;">&nbsp;</div>

<div class="loginMsg">
<p>Please note: Direct login to this site is not allowed. Only authorized DRC System Administrators can access through this link. Please login through <u><a href="https://wi.drcedirect.com/"><span style="color:#0000CD;">eDIRECT</span></a></u>&nbsp;via SSO.&nbsp;</p>

<p>If you do not have eDIRECT login or SSO access to the Wisconsin Forward Exam Reporting System, please contact at <u><a href="mailto:wihelpdesk@datarecognitioncorp.com?subject=eDirect%20Login%20Needed"><span style="color:#0000CD;">WIHelpdesk@datarecognitioncorp.com</span></a></u> or 1-800-459-6530</p>
</div>

<div class="boxshade" style="margin-top: 10px;">
<div class="columns">
<div class="two-column" style="margin: 0px; padding-top: 0px; float: left;"><img src="themes/acsi/img/slide/ladybugicon64x64.gif" /></div>

<div class="nine-columns" style="padding: 20px; border: 1px solid rgb(204, 204, 204); box-shadow: 0px 0px 20px 5px #cccccc;">
<div class="relative" style="height: auto; text-align: justify;">
<p><b>Test Data Availability</b></p>

<p><span style="font-size: 13px;">The Spring 2016 student results will be available in July 2016.</span></p>

<p><b>About Your Privacy</b></p>

<p>Student information is protected by the Family Education Rights and Privacy Act, the individuals with Disabilities Education Act, and other federal and state laws.</p>
</div>
</div>
</div>
</div>
</div>
</div>]',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1095
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1095,
     '<p>The Wisconsin Forward Exam Reporting System is available from Sunday 3:00 A.M. to Saturday 11:00 P.M. Pacific Standard Time. Each Saturday night, between the hours of 11:00 P.M. and 3:00 A.M. Pacific Standard Time, users may experience slowness or system outage due to nightly maintenance.</p>',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1097
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1097,
     q'[<h2>Welcome to the Wisconsin Forward Exam Reporting System</h2>

<p><span style="font-size:16px;">The Online reports available on this site can be used to analyze curriculum strengths and needs in your district or school.</span></p>

<p>&nbsp;</p>

<p>&nbsp;</p>

<p>&nbsp;</p>

<div>
<p style="font-size: 13px; "><span style="font-size:18px;"><u><b>Test Data Availability</b></u></span></p>

<p style="font-size: 13px; "><span style="font-size:16px;">The Spring 2016 student results will be available in July 2016.</span></p>
</div>

<div>
<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>
</div>

<p><span style="font-size:16px;"><b>Privacy Notice:</b></span></p>

<p><span style="font-size:16px;">Student information is protected by the Family Education Rights and Privacy Act, the individuals with Disabilities Education Act, and other federal and state laws.</span></p>]',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1099
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'GENERIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1099,
     q'[<p>Copyright &copy; 2016 Data Recognition Corporation. All rights reserved. Read our <a href="http://www.datarecognitioncorp.com/Pages/privacy.aspx" style="text-decoration: underline;" target="_blank">Privacy Policy</a>.</p>
<p>DRC/Wisconsin Help Desk: Toll Free 1-800-459-6530 Email: <u><a href="mailto:WIHelpdesk@datarecognitioncorp.com">WIHelpdesk@datarecognitioncorp.com</a></u></p>]',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);

  DELETE FROM DASH_MESSAGES
   WHERE MSG_TYPEID = 1091
     AND CUST_PROD_ID = V_DEFAULT_CUST_PROD_ID;
  INSERT INTO DASH_MESSAGES
    (DB_REPORTID,
     MSG_TYPEID,
     REPORT_MSG,
     CUST_PROD_ID,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME)
  VALUES
    ((SELECT DB_REPORTID
       FROM DASH_REPORTS
      WHERE UPPER(REPORT_NAME) = 'PRODUCT SPECIFIC SYSTEM CONFIGURATION'
        AND PROJECTID = V_PROJECTID),
     1091,
     '<p><img class="productImage" id="productImage101" src="themes/wisc/img/wi-dpi-logo.png" width="110px" /></p>',
     V_DEFAULT_CUST_PROD_ID,
     'AC',
     SYSDATE);



  COMMIT;

END;
/
SET DEFINE ON;
