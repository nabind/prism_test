CREATE OR REPLACE FUNCTION sf_get_list_of_level (in_report_id VARCHAR2)
   RETURN VARCHAR2
IS
   Result   VARCHAR2 (4000);
BEGIN
   FOR rec
      IN (SELECT ORG_LABEL ORG_LABEL
            FROM Dash_Menu_Rpt_Access ur, ORG_TP_STRUCTURE rr
           WHERE     ur.org_level = rr.org_level
                 AND db_reportid = in_report_id)
   LOOP
      Result := Result || rec.ORG_LABEL || ',';
   END LOOP;

   RETURN (SUBSTR (Result, 0, LENGTH (result) - 1));
END;
/
