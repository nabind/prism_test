CREATE OR REPLACE FUNCTION FN_GET_ER_EXCDID RETURN NUMBER IS
  ER_EXCDID_VAL NUMBER;
BEGIN
  SELECT SEQ_ER_EXCEPTION_DATA.NEXTVAL INTO ER_EXCDID_VAL FROM DUAL;
  RETURN(ER_EXCDID_VAL);
END FN_GET_ER_EXCDID;
/