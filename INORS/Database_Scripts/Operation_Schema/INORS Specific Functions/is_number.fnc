CREATE OR REPLACE FUNCTION is_number( str IN VARCHAR2 ) RETURN NUMBER
 DETERMINISTIC IS
BEGIN
RETURN TO_NUMBER(str);
EXCEPTION
WHEN OTHERS THEN RETURN -99;
END;
/