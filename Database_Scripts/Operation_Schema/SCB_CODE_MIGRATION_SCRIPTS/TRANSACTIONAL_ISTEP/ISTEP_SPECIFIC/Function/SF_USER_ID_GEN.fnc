create or replace function SF_USER_ID_GEN
  return number as
  LV_USER_ID varchar2(4000);
begin
  LV_USER_ID:=USER_ID_SEQ.NEXTVAL;
  return LV_USER_ID;
end SF_USER_ID_GEN;
/
