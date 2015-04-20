--For MO
DELETE FROM DASH_ACTION_ACCESS WHERE projectid = 3;
DELETE FROM DASH_RPT_ACTION WHERE projectid = 3;
--Manage Report
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add Report','RPT','Add Report',sysdate,3); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit Report','RPT','Edit Report',sysdate,3); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete Report','RPT','Delete Report',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Configure Report Message','RPT','Configure Report Message',sysdate,3);
insert into dash_rpt_action(DB_ACTIONID,ACTION_NAME,ACTION_TYPE,DESCRIPTION,projectid,CREATED_DATE_TIME)
values(seq_dash_rpt_action.nextval,'Edit Actions','RPT','Edit Actions',3,sysdate);

--Manage Content
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add Content','CON','Add Content',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit Content','CON','Edit Content',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete Content','CON','Delete Content',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','CON','More',sysdate,3);

--Group Download Files
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Refresh','GDF','Refresh Button in Group Download Files',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete','GDF','Delete Button in Group Download Files',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Download','GDF','Download Button in Group Download Files',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'File Size','GDF','File Size Button in Group Download Files',sysdate,3);

--Group Downloads
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Combined Pdf','GDL','Combined Pdf Button in Group Downloads',sysdate,3);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Separate Pdf','GDL','Separate Pdf Button in Group Downloads',sysdate,3);

--GRF Download
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'GRF Download','GRF','GRF Download Button in GRF Download',sysdate,3);
