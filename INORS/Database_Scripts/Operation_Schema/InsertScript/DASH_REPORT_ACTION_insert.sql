DELETE FROM DASH_ACTION_ACCESS;
DELETE FROM DASH_RPT_ACTION;
--For TASC
--Manage User
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','USR','Select Administration',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','USR','Select Organization Mode',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search User','USR','Search User',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add User','USR','Add User',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit User','USR','Edit User',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Login As User','USR','Login As User',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete User','USR','Delete User',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','USR','More',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Download Users','USR','Download Users',sysdate,1);

--Manage Org
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','ORG','Select Administration',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','ORG','Select Organization Mode',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Organization','ORG','Search Organization',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'User Count','ORG','User Count',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','ORG','More',sysdate,1);

--Manage Parent
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','PAR','Select Administration',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','PAR','Select Organization Mode',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Parent','PAR','Search Parent',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Reset Password','PAR','Reset Password',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'View Children','PAR','View Children',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','PAR','More',sysdate,1);

--Manage Student
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','STD','Select Administration',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','STD','Select Organization Mode',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Student','STD','Search Student',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','STD','More',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Assessment','STD','Assessment',sysdate,1);

--Manage Report
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add Report','RPT','Add Report',sysdate,1); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit Report','RPT','Edit Report',sysdate,1); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete Report','RPT','Delete Report',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Configure Report Message','RPT','Configure Report Message',sysdate,1);
insert into dash_rpt_action(DB_ACTIONID,ACTION_NAME,ACTION_TYPE,DESCRIPTION,projectid,CREATED_DATE_TIME)
values(seq_dash_rpt_action.nextval,'Edit Actions','RPT','Edit Actions',1,sysdate);

--Manage Content
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add Content','CON','Add Content',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit Content','CON','Edit Content',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete Content','CON','Delete Content',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','CON','More',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Standard Description','CON','Standard Description',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Resource Description','CON','Resource Description',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Everyday Activity Description','CON','Everyday Activity Description',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'About the Test Description','CON','About the Test Description',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Result by Standard Description','CON','Result by Standard Description',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Overall Results Description','CON','Overall Results Description',sysdate,1);

--Reset Password
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Username','RPW','Search Username',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Clear Search','RPW','Clear Search',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'User Details','RPW','User Details',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Security Questions','RPW','Security Questions',sysdate,1);

--Group Download Filess
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Refresh','GDF','Refresh Button in Group Download Files',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete','GDF','Delete Button in Group Download Files',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Download','GDF','Download Button in Group Download Files',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'File Size','GDF','File Size Button in Group Download Files',sysdate,1);

--GRT/IC File Download
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'GRT Button','GRTIC','GRT Button in GRT/IC File Download',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'IC Button','GRTIC','IC Button in GRT/IC File Download',sysdate,1);

--Group Downloads
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Combined Pdf','GDL','Combined Pdf Button in Group Downloads',sysdate,1);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Separate Pdf','GDL','Separate Pdf Button in Group Downloads',sysdate,1);

--Student Data File
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Student Data File','SDF','Student Data File',sysdate,1);

--For INORS
--Manage User
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','USR','Select Administration',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','USR','Select Organization Mode',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search User','USR','Search User',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add User','USR','Add User',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit User','USR','Edit User',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Login As User','USR','Login As User',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete User','USR','Delete User',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','USR','More',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Download Users','USR','Download Users',sysdate,2);

--Manage Org
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','ORG','Select Administration',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','ORG','Select Organization Mode',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Organization','ORG','Search Organization',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'User Count','ORG','User Count',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','ORG','More',sysdate,2);

--Manage Parent
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','PAR','Select Administration',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','PAR','Select Organization Mode',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Parent','PAR','Search Parent',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Reset Password','PAR','Reset Password',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'View Children','PAR','View Children',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','PAR','More',sysdate,2);

--Manage Student
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Administration','STD','Select Administration',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Select Organization Mode','STD','Select Organization Mode',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Student','STD','Search Student',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','STD','More',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Assessment','STD','Assessment',sysdate,2);

--Manage Report
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add Report','RPT','Add Report',sysdate,2); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit Report','RPT','Edit Report',sysdate,2); 
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete Report','RPT','Delete Report',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Configure Report Message','RPT','Configure Report Message',sysdate,2);
insert into dash_rpt_action(DB_ACTIONID,ACTION_NAME,ACTION_TYPE,DESCRIPTION,projectid,CREATED_DATE_TIME)
values(seq_dash_rpt_action.nextval,'Edit Actions','RPT','Edit Actions',2,sysdate);

--Manage Content
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Add Content','CON','Add Content',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Edit Content','CON','Edit Content',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete Content','CON','Delete Content',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'More','CON','More',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Standard Description','CON','Standard Description',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Resource Description','CON','Resource Description',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Everyday Activity Description','CON','Everyday Activity Description',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'About the Test Description','CON','About the Test Description',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Result by Standard Description','CON','Result by Standard Description',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Overall Results Description','CON','Overall Results Description',sysdate,2);

--Reset Password
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Search Username','RPW','Search Username',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Clear Search','RPW','Clear Search',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'User Details','RPW','User Details',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Security Questions','RPW','Security Questions',sysdate,2);

--Group Download Filess
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Refresh','GDF','Refresh Button in Group Download Files',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Delete','GDF','Delete Button in Group Download Files',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Download','GDF','Download Button in Group Download Files',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'File Size','GDF','File Size Button in Group Download Files',sysdate,2);

--GRT/IC File Download
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'GRT Button','GRTIC','GRT Button in GRT/IC File Download',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'IC Button','GRTIC','IC Button in GRT/IC File Download',sysdate,2);

--Group Downloads
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Combined Pdf','GDL','Combined Pdf Button in Group Downloads',sysdate,2);
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Separate Pdf','GDL','Separate Pdf Button in Group Downloads',sysdate,2);

--Student Data File
insert into dash_rpt_action(db_actionid,action_name,action_type,description,created_date_time,projectid)
values(seq_dash_rpt_action.nextval,'Student Data File','SDF','Student Data File',sysdate,2);
commit;
