DELETE FROM DASH_MENUS WHERE PROJECTID = 3;

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION,PROJECTID,CREATED_DATE_TIME)
values (101, 'Reports', 'CUSTOM', 1, 'Reports', 3, sysdate);

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION,PROJECTID,CREATED_DATE_TIME)
values (102, 'Downloads', 'CUSTOM', 2, 'Downloads', 3, sysdate);

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION,PROJECTID,CREATED_DATE_TIME)
values (103, 'Resources', 'CUSTOM', 4, 'Resources', 3, sysdate);

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION,PROJECTID,CREATED_DATE_TIME)
values (104, 'Useful Links', 'CUSTOM', 5, 'Useful Links', 3, sysdate);

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION,PROJECTID,CREATED_DATE_TIME)
values (105, 'Manage', 'CUSTOM', 3, 'Manage', 3, sysdate);
