CREATE OR REPLACE Function getMapStatus
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
     FROM stg_task_status
    WHERE TASK_ID = PROCESSID;

BEGIN

   open c1;
   fetch c1 into HVAL,BVAL,DVAL,CVAL,OVAL,IVAL,DT ;

   if HVAL='CO' AND BVAL='CO' AND DVAL='CO' AND CVAL='CO' AND OVAL='CO' AND IVAL='CO' then
      STATUS := 'CO';
   ELSIF HVAL='ER' AND BVAL='ER' AND DVAL='ER' AND CVAL='ER' AND OVAL='ER' AND IVAL='ER' then
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
/
