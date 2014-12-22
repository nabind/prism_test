UPDATE DASH_MENU_RPT_ACCESS
   SET REPORT_SEQ = 1
 WHERE DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_FOLDER_URI) =
               '/PUBLIC/TASC/REPORTS/TASC_EDU_CENTER/HIGH_SCHOOL_EQUIVALENCY_DASHBOARD_FILES');

UPDATE DASH_MENU_RPT_ACCESS
   SET REPORT_SEQ = 2
 WHERE DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_FOLDER_URI) =
               '/PUBLIC/TASC/REPORTS/TASC_EDU_CENTER/OBJECTIVES_DASHBOARD_FILES');

UPDATE DASH_MENU_RPT_ACCESS
   SET REPORT_SEQ = 3
 WHERE DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_FOLDER_URI) =
               '/PUBLIC/TASC/REPORTS/TASC_EDU_CENTER/STUDENT_ROSTER_FILES');

UPDATE DASH_MENU_RPT_ACCESS
   SET REPORT_SEQ = 4
 WHERE DB_REPORTID IN
       (SELECT DB_REPORTID
          FROM DASH_REPORTS
         WHERE UPPER(REPORT_FOLDER_URI) =
               '/PUBLIC/TASC/REPORTS/TASC_EDU_CENTER/PRINT_READY_ROSTER_FILES');
