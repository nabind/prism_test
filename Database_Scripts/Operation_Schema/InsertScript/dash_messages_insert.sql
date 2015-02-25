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
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1037 AND CUST_PROD_ID = 5001;
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
   1037,
   '<p><span style="font-size:14px"><b><span style="color:#FF0000">Please note: This site is for Parents and Teachers. If you are an administrator please log in via&nbsp;SSO at CTB.com</span></b></span></p>
<p>&nbsp;</p>',
   5001,
   'AC',
   SYSDATE);
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1038 AND CUST_PROD_ID = 5001;   
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
   1038,
   '<p>The Action Plan gives you a clear road map for helping your child progress along the path to academic achievement and test success. Here you will find a specific plan for each tested subject that includes</p>

<ul>
	<li>&nbsp; Skill-Building Activities matched to each important area of the subject that you and your child will enjoy doing together</li>
	<li>&nbsp; The standards your state expects your child to achieve</li>
	<li>&nbsp; Additional web resources available to you</li>
	<li>&nbsp; Everyday Activities that show you practical ways to get involved with your child&rsquo;s education and create a learning-rich environment in your home.</li>
	<li>&nbsp; Test information to help you set goals and plan with your child.</li>
</ul>

<p>Choose from the subjects in the Action Plan menu on the right. You may want to begin with the subject your child needs to improve and then move to the stronger ones. Or, instead, you may want to start with your child&#39;s strengths to build confidence for working on areas that need improvement.</p>

<p><img alt="" height="250" src="themes/acsi/img/children_Overview.png" width="498" /></p>',
   5001,
   'AC',
   SYSDATE);  

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
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1086 AND CUST_PROD_ID = 5001;
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
   1086,
   '<p>This is&nbsp;<span style="line-height: 22px; font-size: 13px; background-color: rgb(255, 255, 255);">Growth Home Page</span></p>',
   5001,
   'AC',
   SYSDATE);   
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1087 AND CUST_PROD_ID = 5001;
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
   1087,
   '<p><img alt="" src="themes/acsi/img/logoCTBSPI.png" /></p>',
   5001,
   'AC',
   SYSDATE); 

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1088 AND CUST_PROD_ID = 5001;
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
   1088,
   q'[<p>&nbsp;Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved. Subject to <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'Terms of Use', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/terms_of_use.pdf', useIframe: true });" style="text-decoration: underline;">Terms of Use</a>. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>]',
   5001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1089 AND CUST_PROD_ID = 5001;
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
   1089,
   q'[<p>&nbsp;Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved. Subject to <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'Terms of Use', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/terms_of_use.pdf', useIframe: true });" style="text-decoration: underline;">Terms of Use</a>. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>

<p>CTB/Indiana Help Desk: Toll Free - 800-282-1132 &nbsp;&nbsp;&nbsp; Email - <a href="mailto:CTB_Indiana_Helpdesk@ctb.com" style="text-decoration: underline;">CTB_Indiana_Helpdesk@ctb.com</a></p>]',
   5001,
   'AC',
   SYSDATE); 

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1090 AND CUST_PROD_ID = 5001;
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
   1090,
   q'[<p>Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved. Subject to <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'Terms of Use', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/terms_of_use.pdf', useIframe: true });" style="text-decoration: underline;">Terms of Use</a>. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>
<p>CTB Support Desk: Toll Free - 800-481-4769 &nbsp;&nbsp;&nbsp; Email - <a href="mailto:support@ctb.com" style="text-decoration: underline;">support@ctb.com </a></p>]',
   5001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1092 AND CUST_PROD_ID = 5001;
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
   1092,
   q'[<p><img alt="" src="themes/acsi/img/slide/CommonLoginPageImage.jpg" />&nbsp;&nbsp;&nbsp;&nbsp;</p>]',
   5001,
   'AC',
   SYSDATE); 

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1093 AND CUST_PROD_ID = 5001;
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
   1093,
   q'[<div><img alt="" src="themes/acsi/img/login_welcome_inors.png" /></div>

<div>&nbsp;</div>

<div class="boxsshade scrollable custom-scroll" style="position: relative;">
<div class="rowTwo">
<div class="h1style" style="margin-left: 0px; text-shadow: 0px 1px 5px rgba(0,0,0,0.25);"><b>Welcome to the Indiana Online Reporting System</b></div>

<div class="h1style" style="margin-left: 0px;">&nbsp;</div>

<div class="loginMsg">
<p>If you do not know your username and/or password you need to contact your building or corp/diocese administrator. Your administrator can assist you further. Should you continue to have problems logging into the system you may contact CTB Indiana Customer Support.</p>
</div>

<div class="boxshade" style="margin-top: 10px;">
<div class="columns">
<div class="two-column" style="margin: 0px; padding-top: 0px; float: left;"><img src="themes/acsi/img/slide/ladybugicon64x64.gif" /></div>

<div class="nine-columns" style="padding: 20px; border: 1px solid rgb(204, 204, 204); box-shadow: 0px 0px 20px 5px #cccccc;">
<div class="relative" style="height: auto; text-align: justify;">
<p><b>Indiana Assessment Results</b><br />
<span style="font-size: 13px;">Rescore Assessment results for Spring 2014 ISTEP+ are now available.</span></p>

<p><b>About Your Privacy</b></p>

<p><b>Any information that you send to CTB/McGraw-Hill via email or web-form will be used only for the purpose of processing your request. <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Read our Privacy Policy</a>.</b></p>

<p><b>Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-align: center; font-size: 13px; text-decoration: underline;">COPPA Policy</a> and its requirements for parental consents to collect or use personal information concerning children.</b></p>
</div>
</div>
</div>
</div>
</div>
</div>

<div class="loginBtn margin-top margin-bottom-medium"><button class="button blue-gradient glossy icon-download" onclick="javascript:window.open('displayAssest.do?assetPath=INORSREPORTS/Static_PDF/Quick_Start_Guide.pdf',800,600)" type="button">Download Quick Start Guide PDF</button></div>]',
   5001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1094 AND CUST_PROD_ID = 5001;
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
   1094,
   q'[<div><img alt="" src="themes/acsi/img/login_welcome_inors.bmp" /></div>

<div class="boxsshade">
<div class="rowTwo"><!--<h1 class="h1style">Welcome to <span class="acsicol">Indiana Parent Network!</span></h1>-->
<h1 style="margin-left: 0px;">Welcome to the Indiana Parent Network!</h1>

<div class="loginMsg">
<p>The Indiana Department of Education is committed to supporting academic success and to promoting the growth of all students. You can help by playing an active role in your child&#39;s education. Create an account on this site to access personalized resources, activities, and information, which will help support your child&#39;s education throughout the year.<br />
<br />
This site provides your child&#39;s assessment results to help you understand strengths and learning needs. You will be able to see the results as soon as they are available. Also, you can review your child&#39;s progress year after year.</p>

<p>&nbsp;</p>
</div>
</div>
</div>

<div class="boxshade scrollable custom-scroll">
<div class="columns">
<div class="two-column" style="margin: 0px; padding-top: 0px; float: left;"><img src="themes/acsi/img/slide/butterflyicon64x64.gif" /></div>

<div class="nine-columns" style="padding: 20px; border: 1px solid rgb(204, 204, 204); box-shadow: 0px 0px 20px 5px #cccccc;"><!-- For parent -->
<div class="relative" id="contentDescription" style="height: auto; text-align: justify;">
<p><b>Indiana Assessment Results</b><br />
NEW - Assessment results for Spring 2014 ISTEP+ are now available. Parents may now request a rescore of open-ended/essay items. Requests for rescore must be submitted to the school between June 2 and June 20, 2014. For more information see the English/Spanish Guide to the Student Report.</p>

<p><b>About Your Privacy</b></p>

<p><span style="font-size: 10px;"><b>Any information that you send to CTB/McGraw-Hill via email or web-form will be used only for the purpose of processing your request. <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank"> Read our Privacy Policy </a></b></span></p>
</div>
</div>
</div>
</div>]',
   5001,
   'AC',
   SYSDATE); 

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1095 AND CUST_PROD_ID = 5001;
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
   1095,
   q'[<p><b class="tag">Security Agreement</b> I understand that some or all of the data and reports that can be accessed from this site contain &#39;personally identifiable information&#39; with respect to students and that such information is protected from disclosure without consent under state and federal law. I agree to protect such information from unauthorized disclosure as required by all relevant federal and state laws. <b>By accessing the services that are linked on this page you are acknowledging that you have read and agree to comply with the above-stated requirements.</b></p>]',
   5001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1096 AND CUST_PROD_ID = 5001;
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
   1096,
   q'[<p><b class="tag">Security Agreement</b><b>&nbsp;</b> I understand that some or all of the data and reports that can be accessed from this site contain &#39;personally identifiable information&#39; with respect to students and that such information is protected from disclosure without consent under state and federal law. I agree to protect such information from unauthorized disclosure as required by all relevant federal and state laws. By accessing the services that are linked on this page you are acknowledging that you have read and agree to comply with the above-stated requirements.</p>]',
   5001,
   'AC',
   SYSDATE);    
   
DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1097 AND CUST_PROD_ID = 5001;
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
   1097,
   q'[<h2>Welcome to the Indiana Online Reporting System</h2>

<p><span style="font-size: 12px;">The online reports available on this site can help you learn how results from the Indiana Statewide&nbsp;Testing for Educational Progress-Plus (ISTEP+), Indiana Modified Achievement Standards Test (IMAST), and the Indiana Reading Evaluation And Determination (IREAD-3) assessments can be used to analyze curriculum strengths and needs in your district or school.&nbsp;</span></p>

<p><b style="font-size: 12px; background-color: rgb(255, 255, 255);">Thank you for supporting the academic achievement of Indiana&#39;s students.</b><span style="font-size: 12px; color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);">&nbsp;</span></p>

<p><span style="font-size: 12px;"><span style="color: rgb(0, 0, 0); font-size: 12px; background-color: rgb(255, 255, 255);">Glenda S. Ritz,&nbsp;</span>State Superintendent of Public Instruction</span></p>

<div style="background: rgb(255, 255, 255); padding: 5px 10px; border: 1px solid red; width: 700px;">
<p><u><b>Test Data Availability</b></u></p>
IREAD-3 Spring 2014 student results available 04-09-2014.<br />
IREAD-3 Summer 2014 student results available 08-12-2014.<br />
ISTEP+ Spring 2014 student results available 05-30-2014.<br />
IMAST Spring 2014 student results available 06-11-2014.</div>

<p>&nbsp;</p>

<div>
<div style="padding: 5px 10px; border: 1px solid red; width: 700px; font-size: 13px; background-color: rgb(255, 255, 255);">
<p><u><b>Report Notification</b></u></p>

<p>IREAD-3 Student results are found only in the Proficiency Roster and Download sections.<br />
IMAST Student results are found only in the Proficiency Roster and Download sections.</p>
</div>
</div>

<p style="margin-left: 40px;">&nbsp;</p>

<p><span style="color: rgb(255, 0, 0);"><b>Indiana Assessment Results</b><br />
Rescore Assessment results for Spring 2014 ISTEP+ are now available.</span></p>

<p><b>Privacy Notice<span style="color: rgb(0, 0, 0); font-size: 11px; background-color: rgb(255, 255, 255);">&nbsp;</span></b></p>

<p><b><span style="font-size: 11px; background-color: rgb(255, 255, 255);">Student information is protected by the Family Educational Rights and Privacy Act, the Individuals with Disabilities Education Act, and other federal and state laws.</span></b></p>]',
   5001,
   'AC',
   SYSDATE); 

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1098 AND CUST_PROD_ID = 5001;
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
   1098,
   q'[<div style="height: auto">
<div class="slide-image-outer margin-bottom" id="header">
<div class="wrap">
<div id="slide-holder">
<div id="slide-runner"><br />
<img alt="" class="slide" id="slide-img-1" src="themes/acsi/img/slide/parentHome.jpg" style="left: 0px;" /> <img alt="" class="slide" id="slide-img-2" src="themes/acsi/img/slide/parentHome2.jpg" style="left: 1000px;" /> <img alt="" class="slide" id="slide-img-3" src="themes/acsi/img/slide/parentHome3.jpg" style="left: 2000px;" />
<div id="slide-controls">
<p class="text" id="slide-client">&nbsp;</p>

<p class="text" id="slide-desc">welcome to Indiana Parent Network</p>

<p id="slide-nav">&nbsp;</p>
</div>
</div>
</div>
</div>
</div>
<!-- End : Image slider -->

<div class="boxshade scrollable" style="max-width:938px; height: 96px;">
<p style="text-align:justify; margin-bottom: 0px;">To choose an Action Plan for your child, click on his or her name on the right side of this screen. The Action Plan includes detailed information about important subject standards. It also provides standards-based activities you can do together to help your child learn and be more successful in school. From the &quot;Explore&quot; menu on the right you may view additional information, including Why Standards Matter, an easy-to-understand overview of standards and testing; and informative websites recommended by your state education agency.&nbsp;&nbsp;&nbsp;</p>
</div>

<div class="margin-bottom" style="margin-left: -20px;margin-top: -30px;">
<div style="float:left; width: 240px;">
<div class="with-padding" style="height: 170px">
<h4 class="blue underline">Explore</h4>

<div class="boxshade">
<ul class="bullet-list">
	<li><a action="getStandardMatters" class="menu-link" href="#nogo">Why Standards Matter</a></li>
	<li><a action="getBrowseContent" class="menu-link" href="#nogo">Browse Content</a></li>
	<li><a href="displayAssest.do?assetPath=INORSREPORTS/Static_PDF/ISTEP_Translation_Guide_ENGLISH.PDF" target="_blank">English Guide to the Student Report</a></li>
	<li><a href="displayAssest.do?assetPath=INORSREPORTS/Static_PDF/ISTEP_Translation_Guide_ESPANOL.PDF" target="_blank">Spanish Guide to the Student Report</a></li>
	<li><a href="displayAssest.do?assetPath=INORSREPORTS/Static_PDF/Parent_Network_User_Guide.pdf" target="_blank">User Guide</a></li>
</ul>
</div>
</div>
</div>

<div style="margin-left: 220px;">
<div class="with-padding" style="height: 170px; margin-right: -19px;">
<h4 class="blue underline">Manage My Account</h4>

<div class="boxshade scrollable" style="height: 152px;">
<p>Use this page to update your personal information. You can also view information about which test data is available on the Parent Network for your school.</p>

<ul class="bullet-list">
	<li><a href="myAccount.do">My Account</a></li>
	<li><a class="claim-Invitation" href="#nogo">Claim new Invitation Code </a></li>
</ul>
</div>
</div>
</div>
</div>
</div>

<p>&nbsp;</p>]',
   5001,
   'AC',
   SYSDATE); 

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1099 AND CUST_PROD_ID = 5001;
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
   1099,
   q'[<p>Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved. Subject to <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'Terms of Use', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/terms_of_use.pdf', useIframe: true });" style="text-decoration: underline;">Terms of Use</a>. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>
<p>CTB/Indiana Help Desk: Toll Free - 800-282-1132 &nbsp;&nbsp;&nbsp; Email - <a href="mailto:CTB_Indiana_Helpdesk@ctb.com" style="text-decoration: underline;">CTB_Indiana_Helpdesk@ctb.com</a></p>]',
   5001,
   'AC',
   SYSDATE);  

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1100 AND CUST_PROD_ID = 5001;
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
   1100,
   q'[<p>Copyright &copy; 2014 by CTB/McGraw-Hill LLC. All rights reserved. Subject to <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'Terms of Use', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/terms_of_use.pdf', useIframe: true });" style="text-decoration: underline;">Terms of Use</a>. Read our <a href="https://www.ctb.com/ctb.com/control/EcomPrivacyMainView?p=privacy" style="text-decoration: underline;" target="_blank">Privacy Policy Online</a>. Review <a href="#" onclick="$.modal({ width: 550, height: 400, title: 'COPPA Policy', url: 'displayAssest.do?assetPath=INORSREPORTS/Static_PDF/COPPA.pdf', useIframe: true });" style="text-decoration: underline;">COPPA Policy</a>.</p>
<p>CTB Support Desk: Toll Free - 800-481-4769 &nbsp;&nbsp;&nbsp; Email - <a href="mailto:support@ctb.com" style="text-decoration: underline;">support@ctb.com</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>]',
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

DELETE FROM DASH_MESSAGES WHERE MSG_TYPEID = 1091 AND CUST_PROD_ID = 5001;      
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
      AND PROJECTID = 2),
   1091,
   '<p><img class="productImage" id="productImage101" src="themes/acsi/img/SPIlogo.jpg" width="250px" /></p>',
   5001,
   'AC',
   SYSDATE);    
   
SET DEFINE ON;