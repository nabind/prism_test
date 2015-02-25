CREATE OR REPLACE Function getStatus
   ( processid IN NUMBER )
   RETURN VARCHAR2
IS
   HVAL VARCHAR2(10);
   BVAL VARCHAR2(10);
   DVAL VARCHAR2(10);
   CVAL VARCHAR2(10);
   OVAL VARCHAR2(10);
   IVAL VARCHAR2(10);
   DT DATE;
   
   STATUS VARCHAR2(20);

   cursor c1 is
   SELECT HIER_VALIDATION,
          BIO_VALIDATION,
          DEMO_VALIDATION,
          CONTENT_VALIDATION,
          OBJECTIVE_VALIDATION,
          ITEM_VALIDATION,
          DATETIMESTAMP
     FROM STG_PROCESS_STATUS
    WHERE PROCESS_ID = PROCESSID;

BEGIN

   open c1;
   fetch c1 into HVAL,BVAL,DVAL,CVAL,OVAL,IVAL,DT ;

   if HVAL='CO' AND BVAL='CO' AND DVAL='CO' AND CVAL='CO' AND OVAL='CO' AND IVAL='CO' then
      STATUS := 'CO';
   ELSIF ((HVAL<>'CO' AND BVAL<>'CO' AND DVAL<>'CO' AND CVAL<>'CO' AND OVAL<>'CO' AND IVAL<>'CO') 
        AND 
        (SYSDATE-30/(24*60) - DT-0/(24*60)<0)) then
      STATUS := 'IN';
   ELSE 
      STATUS := 'ER';
   end if;

   close c1;

RETURN STATUS;

EXCEPTION
WHEN OTHERS THEN
   raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
END;
/
