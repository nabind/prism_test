CREATE OR REPLACE FUNCTION SF_CONV_TO_DATE(p_date IN VARCHAR2
                                        ,p_format IN VARCHAR2)
  RETURN DATE
  DETERMINISTIC
  IS
    v_date DATE;
   -- v_date_format VARCHAR2(100) DEFAULT 'DD-MON-YY';
BEGIN
    SELECT TO_DATE(p_date,p_format) INTO v_date FROM DUAL;
        RETURN v_date;
    EXCEPTION WHEN OTHERS THEN
        RETURN NULL;
END SF_CONV_TO_DATE;
/
