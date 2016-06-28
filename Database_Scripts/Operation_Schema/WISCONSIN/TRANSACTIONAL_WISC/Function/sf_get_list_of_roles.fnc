CREATE OR REPLACE FUNCTION sf_get_list_of_roles (in_report_id VARCHAR2)
   RETURN VARCHAR2
IS
   Result   VARCHAR2 (4000);
BEGIN
   FOR rec
      IN (SELECT distinct ROLE_NAME role_name
            FROM role ur, dash_menu_rpt_access rr
           WHERE     ur.roleid = rr.roleid
                 AND db_reportid = in_report_id)
   LOOP
      Result := Result || rec.role_name || ',';
   END LOOP;

   RETURN (SUBSTR (Result, 0, LENGTH (result) - 1));
END;
/
