SET DEFINE OFF;

--For TASC
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1101 AND CUST_PROD_ID = 3001;
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
      AND PROJECTID = 1),
   1101,
   '<p>This section includes files for the bulk Candidate Report and Student Data File if they have been selected for download. To download the Candidate Report for a group of students, go to the Student Roster and choose the Bulk Download button. To download the Student Data File, go to the Student Data File option on the menu and choose the download button.</p>
<p class="big-message">Please note: The length of time to create your file depends on the volume of requests made by all TASC users. Thanks for your patience!</p>',
   3001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1102 AND CUST_PROD_ID = 3001;
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
      AND PROJECTID = 1),
   1102,
   '<p class="red">Files will be deleted automatically after the expiration date/time. Click <b>Refresh</b> to get the current job status.</p>',
   3001,
   'AC',
   SYSDATE);    

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1087 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1087,
   '<p><img alt="" src="themes/acsi/img/logoCTBTASC.png" /></p>',
   3001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1089 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1089,
   '<p><span style="font-size: 12px; background-color: rgb(255, 255, 255);">Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved.</span><br style="font-size: 12px; background-color: rgb(255, 255, 255);" />
<span style="font-size: 12px; background-color: rgb(255, 255, 255);">TASC Test Assessing Secondary Completion is a trademark of McGraw-Hill School Education Holdings, LLC.</span><br style="font-size: 12px; background-color: rgb(255, 255, 255);" />
<span style="font-size: 12px; background-color: rgb(255, 255, 255);">McGraw-Hill Education is not affiliated with The After-School Corporation, which is known as TASC. The After-School&nbsp;</span><span style="font-size: 12px; background-color: rgb(255, 255, 255);">Corporation has no affiliation with the Test Assessing Secondary Completion (the &quot;TASC test&quot;) offered by McGraw-Hill Education, and has not authorized, sponsored or otherwise approved of any of McGraw-Hill Education&#39;s products and services, including the TASC test.</span></p>',
   3001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1093 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1093,
   q'[<div><img alt="" src="themes/acsi/img/login_welcome_adult.jpg" /></div>

<div class="boxsshade scrollable custom-scroll" style="padding: 1px; position: relative;">
<div class="rowTwo">
<h1 class="h1style" style="margin: 0">Welcome to the TASC Online Reporting Dashboard</h1>

<div class="loginMsg" height="auto">
<p style="line-height:120%">Welcome to the TASC Administrator Online Reporting System. All relevant examinee data is available through this portal, although permission to view certain information will vary depending on your user status and location.</p>

<p style="line-height:120%"><br />
If you have any questions, please contact CTB&#39;s TASC Customer Service at TASC_Helpdesk@ctb.com or 888-282-0589.<br />
<br />
Representatives are available Monday through Friday from 7:30 AM to 8:00 PM EST.</p>
</div>
</div>
</div>

<div class="loginBtn margin-top margin-bottom-medium"><button class="button blue-gradient glossy icon-download" onclick="javascript:window.open('displayAssest.do?assetPath=TASCREPORTS/Static_PDF/Quick_Start_Guide.pdf',800,600)" type="button">Download Quick Start Guide PDF</button></div>]',
   3001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1095 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1095,
   '<p>The TASC Administrator Online Reporting System is available from Sunday 3:00 A.M. to Saturday 11:00 P.M. Pacific Standard Time. Each Saturday night, between the hours of 11:00 P.M. and 3:00 A.M. Pacific Standard Time, users may experience slowness or system outage due to nightly maintenance.</p>',
   3001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1097 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1097,
   '<h2>Welcome to the TASC Test Online Reporting System</h2>

<p><span style="font-size: 12px;">Welcome to the TASC Test Online Reporting System. </span></p>

<p><span style="font-size: 12px;">Use this application to access examinee test data by selecting from your options at the right. Examinee test results and testing history are also available via the TASC Test Online Registration and Testing System. </span></p>

<p><span style="font-size: 12px;">If you have questions regarding the TASC Test Online Reporting System (aka PRISM), please contact our Customer Service TASC TEST Specialist at: <u><a href="mailto:TASCTEST_HelpDesk@ctb.com" target="_blank">TASCTEST_HelpDesk@ctb.com</a></u> or call us at 1-800-282-0589. </span></p>',
   3001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1099 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1099,
   '<p style="font-size: 12px; padding: 0 19% 0 19%">Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved.<br />
TASC Test Assessing Secondary Completion is a trademark of McGraw-Hill School Education Holdings, LLC.<br />
McGraw-Hill Education is not affiliated with The After-School Corporation, which is known as TASC. The After-School Corporation has no affiliation with the Test Assessing Secondary Completion (the &quot;TASC test&quot;) offered by McGraw-Hill Education, and has not authorized, sponsored or otherwise approved of any of McGraw-Hill Education&#39;s products and services, including the TASC test.</p>',
   3001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1101 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1101,
   '<p>This section includes files for the bulk Candidate Report and Student Data File if they have been selected for download. To download the Candidate Report for a group of students, go to the Student Roster and choose the Bulk Download button. To download the Student Data File, go to the Student Data File option on the menu and choose the download button.</p>

<p class="big-message">Please note: The length of time to create your file depends on the volume of requests made by all TASC users. Thanks for your patience!</p>',
   3001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1102 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1102,
   '<p class="red">Files will be deleted automatically after the expiration date/time. Click <b>Refresh</b> to get the current job status.</p>',
   3001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1091 AND CUST_PROD_ID = 3001;   
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
      AND PROJECTID = 1),
   1091,
   '<p><img class="productImage" id="productImage101" src="themes/acsi/img/TASCLogo.png" /></p>',
   3001,
   'AC',
   SYSDATE);
   
--For INORS   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1085 AND CUST_PROD_ID = 5001;
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
      AND PROJECTID = 2),
   1085,
   '<p><b><span style="color:#FF0000">Click &quot;Refresh&quot; to see student lists for the selected criteria.</span></b></p>

<p><b><em>The Group download allows users to select ISR and/or image print PDFs for multiple students to be combined into one file to be downloaded and printed.</em></b></p>

<p><b><u>Filter Options</u></b></p>

<p>At the top of the Group Download page in the &ldquo;Filter Options&rdquo; section select the reporting criteria for each filter, using the arrows beside the filter, and click <b>Refresh</b>.</p>

<p><b><u>Selection Functionality</u></b></p>

<p>A check in the checkbox&nbsp;<img alt="check1" src="./themes/acsi/img/selected.bmp" />&nbsp;indicates&nbsp;the item is selected. An empty checkbox&nbsp;&nbsp;<img alt="check3" height="14" src="./themes/acsi/img/unselected.png" width="14" />&nbsp; indicates no students are selected. A green checkbox <img alt="check2" src="./themes/acsi/img/tristate.bmp" />&nbsp; indicates at least one student is selected.&nbsp;A check in the checkbox&nbsp;&nbsp;<img alt="check1" src="./themes/acsi/img/selected.bmp" />&nbsp;to the left of the &ldquo;Student&rdquo; heading will select all students within the filtered set. Deselect a student by clicking on a checked checkbox to the left of the student name or the &ldquo;Student&rdquo; heading. The checkbox will be empty to indicate the item has been deselected.</p>

<p><b><u>File Generation</u></b></p>

<p>To submit a request to generate a compressed downloadable file containing separate PDF report files for each of the selected students, click on &quot;Generate Download File - Separate PDFs&quot;.</p>

<p>To submit a request to generate a compressed downloadable file containing one PDF of combined PDF report files for the selected students, click on &quot;Generate Download File - Combined PDF&quot;.</p>

<p><b><u>Optional</u></b></p>

<p>&quot;Name of Generated File&quot; may be modified.</p>

<p>&quot;Email address for notification of Generated File complete&quot; may be changed.</p>',
   5001,
   'AC',
   SYSDATE);
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1101 AND CUST_PROD_ID = 5001;   
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
      AND PROJECTID = 2),
   1101,
   '<p class="big-message">This section includes files for the ISTEP+, IMAST and IREAD-3 Student Reports, and for ISTEP+ only the Image Prints and Invitation Code Letters.<br />
<br />
If the Image Print document (Applied Skills) is not available for a student, please contact your School or Corporation Test Coordinator.<br />
If the ISR (Individual Student Report) is not available for a student, please contact the CTB/Indiana Help Desk.</p>',
   5001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1102 AND CUST_PROD_ID = 5001;      
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
      AND PROJECTID = 2),
   1102,
   '<p class="red"><span class="tag red-gradient">Please Note:</span> The length of time to create your file depends upon the volume of requests made by all INORS users. Thanks for your patience!</p>
<p class="red">Files will be deleted automatically after the expiration date/time. Click <b>Refresh</b> to get the current job status.</p>',
   5001,
   'AC',
   SYSDATE);    
   
SET DEFINE ON;