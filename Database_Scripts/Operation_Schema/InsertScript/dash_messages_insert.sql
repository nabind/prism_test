SET DEFINE OFF;

--For MO
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1085 AND CUST_PROD_ID = 5027;
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
      AND PROJECTID = 3),
   1085,
   '<p><b><span style="color:#FF0000">Click &quot;Refresh&quot; to see student lists for the selected criteria.</span></b></p>

<p><b><i>The Group download allows users to select ISR PDFs for multiple students and multiple Content Areas to be combined into one file to be downloaded and printed.</i></b></p>

<p><b><u>Filter Options</u></b></p>

<p>At the top of the Group Download page in the &ldquo;Filter Options&rdquo; section select the reporting criteria for each filter, using the arrows beside the filter, and click <b>Refresh</b>.</p>

<p><b><u>Selection Functionality</u></b></p>

<p>A check in the checkbox&nbsp;<img alt="check1" src="./themes/acsi/img/selected.bmp" />&nbsp;indicates&nbsp;the item is selected. An empty checkbox&nbsp;&nbsp;<img alt="check3" height="14" src="./themes/acsi/img/unselected.png" width="14" />&nbsp; indicates no students are selected. A green checkbox <img alt="check2" src="./themes/acsi/img/tristate.bmp" />&nbsp; indicates at least one student is selected.&nbsp;A check in the checkbox&nbsp;&nbsp;<img alt="check1" src="./themes/acsi/img/selected.bmp" />&nbsp;to the left of the &ldquo;Student&rdquo; heading will select all students within the filtered set. Deselect a student by clicking on a checked checkbox to the left of the student name or the &ldquo;Student&rdquo; heading. The checkbox will be empty to indicate the item has been deselected.</p>

<p><b><u>File Generation</u></b></p>

<p>To submit a request to generate a compressed downloadable file containing separate PDF report files for each of the selected students, click on &quot;Generate Download File - Separate PDFs&quot;.</p>

<p>To submit a request to generate a one combined PDF report files for the selected students, click on &quot;Generate Download File - Combined PDF&quot;.</p>

<p><b><u>Optional</u></b></p>

<p>&quot;Name of Generated File&quot; may be modified.</p>

<p>&quot;Email address for notification of Generated File complete&quot; needs to be provided to proceed with the file generation process. If an email is already present in the field, the user has the option of changing it.</p>',
   5027,
   'AC',
   SYSDATE); 
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1101 AND CUST_PROD_ID = 5027;
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
      AND PROJECTID = 3),
   1101,
   '<p class="big-message">This section includes files for the MAP Grade-Level Assessments Student Reports.<br />
<br />
If the ISR (Individual Student Report) is not available for a student, please contact the CTB/Missouri Help Desk.</p>',
   5027,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1102 AND CUST_PROD_ID = 5027;
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
      AND PROJECTID = 3),
   1102,
   '<p class="red"><span class="tag red-gradient">Please Note:</span> The length of time to create your file depends upon the volume of requests made by all MAP Grade-Level Assessment&nbsp;users. Thanks for your patience!</p>
<p class="red">Files will be deleted automatically after the expiration date/time. <b>All date and time shown in this page is in EST</b>. Click <b>Refresh</b> to get the current job status.</p>',
   5027,
   'AC',
   SYSDATE);    

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1087 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1087,
   '<p><img alt="" border="0" height="54" hspace="0" src="themes/usmo/img/logoCTBMO.png" style="width:305px;height:54px;margin-top:0px;margin-bottom:0px;margin-left:0px;margin-right:0px;border:0px solid black;" vspace="0" width="305" /></p>',
   5027,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1089 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1089,
   q'[<p>Copyright &copy; 2015 by CTB/McGraw-Hill LLC. All rights reserved. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>
<p>CTB/Missouri Help Desk: Toll Free 1-800-544-9868 Email: <u><a href="mailto:maphelpdesk@ctb.com">maphelpdesk@ctb.com</a></u></p>]',
   5027,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1093 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1093,
   q'[<div><img alt="" border="0" height="168" hspace="0" src="themes/usmo/img/login_welcome_mo.jpg" style="width:715px;height:168px;margin-top:0px;margin-bottom:0px;margin-left:0px;margin-right:0px;border:0px solid black;" vspace="0" width="715" /></div>

<div>&nbsp;</div>

<div class="boxsshade scrollable custom-scroll" style="position: relative;">
<div class="rowTwo">
<div class="h1style" style="margin-top:5px;margin-left: 0px; text-shadow: 0px 1px 5px rgba(0,0,0,0.25);"><b>Welcome to the MAP Grade-Level Assessments Reporting System</b></div>

<div class="h1style" style="margin-left: 0px;">&nbsp;</div>

<div class="loginMsg">
<p>Please note: Direct login to this site is not allowed. Only authorized CTB System Administrators can access through this link. Please login through <u><a href="https://mo.drcedirect.com/"><span style="color:#0000CD;">eDirect</span></a></u> via SSO.&nbsp;</p>

<p>If you do not have eDirect login or SSO access to the MAP Grade-Level Assessments Reporting System, please contact at <u><a href="mailto:maphelpdesk@ctb.com?subject=eDirect%20Login%20Needed"><span style="color:#0000CD;">maphelpdesk@ctb.com</span></a></u> or 1-800-544-9868</p>
</div>

<div class="boxshade" style="margin-top: 10px;">
<div class="columns">
<div class="two-column" style="margin: 0px; padding-top: 0px; float: left;"><img src="themes/acsi/img/slide/ladybugicon64x64.gif" /></div>

<div class="nine-columns" style="padding: 20px; border: 1px solid rgb(204, 204, 204); box-shadow: 0px 0px 20px 5px #cccccc;">
<div class="relative" style="height: auto; text-align: justify;">
<p><b>Test Data Availability - <i>Preliminary</i></b></p>

<p><span style="font-size: 13px;">The Spring 2015 MAP Grade-Level Assessments Student Results will be available 10-days from your district&#39;s testing completion date.</span></p>

<p><b>About Your Privacy</b></p>

<p>Student information is protected by the Family Education Rights and Privacy Act, the individuals with Disabilities Education Act, and other federal and state laws.</p>
</div>
</div>
</div>
</div>
</div>
</div>]',
   5027,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1095 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1095,
   '<p>The MAP Grade-Level Assessments Reporting System is available from Sunday 3:00 A.M. to Saturday 11:00 P.M. Pacific Standard Time. Each Saturday night, between the hours of 11:00 P.M. and 3:00 A.M. Pacific Standard Time, users may experience slowness or system outage due to nightly maintenance.</p>',
   5027,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1097 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1097,
   '<h2>Welcome to the MAP Grade-Level Assessments Reporting System</h2>

<p><span style="font-size: 12px;">The Online reports available on this site can help you learn how results from the MAP Grade-Level Spring 2015.</span></p>

<p><span style="font-size: 12px;">Assessments can be used to analyze curriculum strengths and needs in your district or school.</span></p>

<p><span style="font-size: 12px;"><b>Thank you for supporting the academic achievement of Missouri&#39;s students.</b></span></p>

<p><b><span style="font-size: 12px;">Dr. Margie Vandeven, Missouri Commissioner of Education.</span></b></p>

<p>&nbsp;</p>

<div>&nbsp;</div>

<div>
<div style="padding: 0px 0px; border: 0px solid red; font-size: 13px; width: 700px; background-color: rgb(255, 255, 255);">
<p style="font-size: 13px;"><u><b style="font-size: 13px;">Test Data Availability - <i>Preliminary</i></b></u></p>

<p><span style="font-size: 13px;">The Spring 2015 MAP Grade-Level Assessments Student Results will be available 10-days from your district&#39;s testing completion date.</span></p>
</div>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>

<p style="font-size: 13px; background-color: rgb(255, 255, 255);">&nbsp;</p>
</div>

<p><b>Privacy Notice:</b></p>

<p><span style="font-size: 12px;">Student information is protected by the Family Education Rights and Privacy Act, the individuals with Disabilities Education Act, and other federal and state laws.</span></p>',
   5027,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1099 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1099,
   q'[<p>Copyright &copy; 2015 by CTB/McGraw-Hill LLC. All rights reserved. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>
<p>CTB/Missouri Help Desk: Toll Free 1-800-544-9868 Email: <a href="mailto:maphelpdesk@ctb.com"><u>maphelpdesk@ctb.com</u></a></p>]',
   5027,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1091 AND CUST_PROD_ID = 5027;   
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
      AND PROJECTID = 3),
   1091,
   '<p><img class="productImage" id="productImage101" src="themes/usmo/img/DESE-color.jpg" width="220px" /></p>

<div class="MAPNote" style="display:none;text-align:left;color:red;border-top:1px solid grey;padding:10px !important;margin-top:12px;">
<p><b>PLEASE NOTE:</b><br />
The data and information in this report may differ from the data shown in the Missouri Comprehensive Data System portal due to data clean-up activities and/or appeals.</p>
</div>',
   5027,
   'AC',
   SYSDATE);
     
SET DEFINE ON;