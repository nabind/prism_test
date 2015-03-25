--For MO

delete from dash_menu_rpt_access
 where projectid = 3
   and db_reportid in
       (select db_reportid from dash_reports WHERE projectid = 3);
       
delete from DASH_ACTION_ACCESS
 where projectid = 3
   and db_reportid in
       (select db_reportid from dash_reports WHERE projectid = 3);        
       
delete from dash_messages
 where db_reportid in
       (select db_reportid from dash_reports WHERE projectid = 3);       

DELETE FROM dash_reports WHERE projectid = 3;

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Manage Reports','Manage Reports','API_LINK','manageReports.do','AC',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Manage Content','Manage Content','API_LINK','manageContent.do','AC',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Generic System Configuration','Generic System Configuration','API','IN',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Product Specific System Configuration','Product Specific System Configuration','API','IN',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Group Downloads','Group Downloads for MO','API_CUSTOM','/public/Missouri/Report/Group_Download_MO_files|groupDownloadForm.do','AC',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Group Download Files','Group Download Files for MO','API_NFCUSTOM','/public/ISTEP/Reports/Student_Tabular_Report_files|groupDownloadFiles.do','AC',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'User Guide','User Guide for MO','API_NFCUSTOM','resourcepdf|resourcepdf.do?pdfFileName=/Static_PDF/User_Guide.pdf','AC',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'http://dese.mo.gov/','ext link','API_NFCUSTOM','extLinks|http://dese.mo.gov/','AC',sysdate,3);

insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,'Student Roster','Student Roster for MO','API','/public/Missouri/Report/Student_Roster_files','AC',sysdate,3);

SET DEFINE OFF;
insert into dash_reports(db_reportid,report_name,report_desc,report_type,report_folder_uri,activation_status,created_date_time,projectid)
values(DB_REPORT_ID_SEQ.Nextval,q'[Guide to Interpreting Results &#40;GIR&#41;]','GIR','API_NFCUSTOM','resourcepdf|resourcepdf.do?pdfFileName=/Static_PDF/GIR.pdf','AC',sysdate,3);
SET DEFINE ON;
