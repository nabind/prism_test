create or replace function SF_ORG_USER_ID_GEN(IN_EXISTS NUMBER,IN_USERID NUMBER)
  return number as
  LV_ORG_USER_ID varchar2(4000);
begin

  IF IN_EXISTS = 1
  THEN

    SELECT ORG_USER_ID INTO LV_ORG_USER_ID FROM ORG_USERS OU
    WHERE OU.USERID = IN_USERID;

    return LV_ORG_USER_ID;

  ELSE

    LV_ORG_USER_ID:=ORG_USER_ID_SEQ.NEXTVAL;

    return LV_ORG_USER_ID;

  END IF;


end SF_ORG_USER_ID_GEN;
/
