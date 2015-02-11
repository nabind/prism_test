SET DEFINE OFF;

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
   
