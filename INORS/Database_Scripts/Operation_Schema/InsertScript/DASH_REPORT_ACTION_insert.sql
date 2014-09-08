--Common
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Administration','GEN','Select Administration',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','GEN','Select Organization Mode',sysdate);

--Manage User
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Search User','USR','Search User',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Add User','USR','Add User',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Edit User','USR','Edit User',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Login As User','USR','Login As User',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Delete User','USR','Delete User',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'More','USR','More',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Download Users','USR','Download Users',sysdate);

--Manage Org
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Search Organization','ORG','Search Organization',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'User Count','ORG','User Count',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'More','ORG','More',sysdate);

--Manage Parent
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Search Parent','PAR','Search Parent',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Reset Password','PAR','Reset Password',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'View Children','PAR','View Children',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'More','PAR','More',sysdate);

--Manage Student
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Search Student','STD','Search Student',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'More','STD','More',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Assessment','STD','Assessment',sysdate);

--Manage Report
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Add Report','RPT','Add Report',sysdate); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Edit Report','RPT','Edit Report',sysdate); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Delete Report','RPT','Delete Report',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Configure Report Message','RPT','Configure Report Message',sysdate);

--Manage Content
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Grade','CON','Select Grade',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Subtest','CON','Select Subtest',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Standard','CON','Select Standard',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Standard','CON','Select Standard',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Select Article Type','CON','Select Article Type',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Add Content','CON','Add Content',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Edit Content','CON','Edit Content',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Delete Content','CON','Delete Content',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Search Content','CON','Search Content',sysdate);

--Reset Password
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Search Username','RPW','Search Username',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Clear Search','RPW','Clear Search',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'User Details','RPW','User Details',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Security Questions','RPW','Security Questions',sysdate);

--Group Download Filess
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Refresh','GDF','Refresh Button in Group Download Files',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Delete','GDF','Delete Button in Group Download Files',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Download','GDF','Download Button in Group Download Files',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'File Size','GDF','File Size Button in Group Download Files',sysdate);

--GRT/IC File Download
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'GRT Button','GDF','GRT Button in GRT/IC File Download',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'IC Button','GDF','IC Button in GRT/IC File Download',sysdate);

--Group Downloads
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Combined Pdf','GDF','Combined Pdf Button in Group Downloads',sysdate);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Separate Pdf','GDF','Separate Pdf Button in Group Downloads',sysdate);

--Student Data File
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time)
values(seq_dash_rpt_action.nextval,'Student Data File','GDF','Student Data File',sysdate);
commit;
