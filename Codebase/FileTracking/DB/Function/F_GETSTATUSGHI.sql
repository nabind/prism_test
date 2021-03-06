CREATE OR REPLACE Function getStatusGHI
   ( processid IN NUMBER )
   RETURN VARCHAR2
IS
   REGVAL VARCHAR2(10);
   DOCVAL VARCHAR2(10);
   HVAL VARCHAR2(10);
   BVAL VARCHAR2(10);
   DVAL VARCHAR2(10);
   CVAL VARCHAR2(10);
   OVAL VARCHAR2(10);
   IVAL VARCHAR2(10);
   DT DATE;
   
   STATUS VARCHAR2(20);

   cursor c1 is
   SELECT REG_VALIDATION,
          DOC_VALIDATION,
          HIER_VALIDATION,
          BIO_VALIDATION,
          DEMO_VALIDATION,
          CONTENT_VALIDATION,
          OBJECTIVE_VALIDATION,
          ITEM_VALIDATION,
          DATETIMESTAMP
     FROM TASC_PROCESS_STATUS
    WHERE PROCESS_ID = PROCESSID;

BEGIN

   open c1;
   fetch c1 into REGVAL,DOCVAL,HVAL,BVAL,DVAL,CVAL,OVAL,IVAL,DT ;

   if REGVAL = 'CO' AND DOCVAL = 'CO' AND HVAL='CO' AND BVAL='CO' AND DVAL='CO' AND CVAL='CO' AND OVAL='CO' AND IVAL='CO' then
      STATUS := 'CO';
   ELSIF REGVAL = 'VA' OR DOCVAL = 'VA' OR HVAL='VA' OR BVAL='VA' OR DVAL='VA' OR CVAL='VA' OR OVAL='VA' OR IVAL='VA' then
      STATUS := 'ER';  
   ELSE 
      STATUS := 'IN';
   end if;

   close c1;

RETURN STATUS;

EXCEPTION
WHEN OTHERS THEN
   raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
END;
