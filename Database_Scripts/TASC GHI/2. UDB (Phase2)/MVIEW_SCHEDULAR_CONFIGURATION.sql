CREATE TABLE TAB_MV_DDL_STORE (MV_NAME VARCHAR2(30), MV_DDL CLOB);

---UPDATE AND ENTER DDL FOR MV'S MV_STUDENT_FILE_DOWNLOAD AND MV_STUDENT_DETAILS
 1.SELECT * FROM TAB_MV_DDL_STORE FOR UPDATE;
 2.Include the DDLs for the MViews


---COMPILE THIS PROCEDURE
CREATE OR REPLACE PROCEDURE SP_RECREATE_TASC_MVIEWS (P_MV_NAME IN VARCHAR2) AS

  l_strsql clob;
BEGIN

  SELECT MV_DDL
    INTO l_strsql
    FROM TAB_MV_DDL_STORE
   WHERE MV_NAME = P_MV_NAME;
   
  EXECUTE IMMEDIATE ('DROP MATERIALIZED VIEW '||P_MV_NAME);
  DBMS_OUTPUT.PUT_LINE('DROPPED'||P_MV_NAME);
  
  EXECUTE IMMEDIATE l_strsql;  
  DBMS_OUTPUT.PUT_LINE('SUCCESSFULLY RECREATED '||P_MV_NAME);
  
END SP_RECREATE_TASC_MVIEWS;
/

--- GIVE THE GRANT BY LOGIN AS SYSDBA
GRANT CREATE ANY MATERIALIZED VIEW TO tasc;
GRANT CREATE ANY TABLE TO tasc;
GRANT CREATE ANY JOB TO tasc;

--SELECT * FROM MLOG$_STUDENT_BIO_DIM;


---TO START THE JOB  
	--FOR MV_STUDENT_FILE_DOWNLOAD
	 BEGIN
	  DBMS_SCHEDULER.CREATE_JOB(job_name        => 'RECREATE_STUDENT_FILE_DOWNLOAD',
							  job_type        => 'PLSQL_BLOCK',
							  JOB_ACTION      => q'[BEGIN SP_RECREATE_TASC_MVIEWS(P_MV_NAME=> 'MV_STUDENT_FILE_DOWNLOAD'); END;]',
							  start_date      => SYSTIMESTAMP,
							  --repeat_interval => 'freq=secondly; bysecond=0',
							  end_date        => NULL,
							  enabled         => TRUE,
							  comments        => 'Calls PLSQL once');
							  
	   END;
	   
	  --FOR MV_STUDENT_DETAILS 
	  BEGIN
	  DBMS_SCHEDULER.CREATE_JOB(job_name        => 'RECREATE_STUDENT_DETAILS',
							  job_type        => 'PLSQL_BLOCK',
							  JOB_ACTION      => q'[BEGIN SP_RECREATE_TASC_MVIEWS(P_MV_NAME=> 'MV_STUDENT_DETAILS'); END;]',
							  start_date      => SYSTIMESTAMP,
							  --repeat_interval => 'freq=secondly; bysecond=0',
							  end_date        => NULL,
							  enabled         => TRUE,
							  comments        => 'Calls PLSQL once');
							  
	  END;
   
---TO CHECK THE STATUS OF THE JOB
   SELECT JOB_NAME,STATE FROM DBA_SCHEDULER_JOBS WHERE JOB_NAME IN ('RECREATE_STUDENT_DETAILS','RECREATE_STUDENT_FILE_DOWNLOAD');   
  
--- IN ORDER TO STOP THE JOB 
BEGIN
  --DBMS_SCHEDULER.drop_job(job_name => 'RECREATE_TASC_MVIEW',force => TRUE);
   DBMS_SCHEDULER.stop_job(job_name =>'RECREATE_STUDENT_DETAILS');
   DBMS_SCHEDULER.stop_job(job_name =>'RECREATE_STUDENT_FILE_DOWNLOAD');
END;
/



---TO CHECK THE QUERY FROM SQL_ID
--SELECT * FROM V$SQLTEXT WHERE sql_id LIKE '3y8s%' ORDER BY piece;

--SELECT COUNT(1) FROM MV_STUDENT_FILE_DOWNLOAD;
--SELECT COUNT(1) FROM STUDENT_BIO_DIM;
   
   
--------------OR-----------------
  --STEP 1



CREATE OR REPLACE PROCEDURE REFRESH_MV_SFD AS
  l_strsql clob;
BEGIN

  SELECT MV_DDL
    INTO l_strsql
    FROM TAB_MV_DDL_STORE
   WHERE MV_NAME = 'MV_STUDENT_FILE_DOWNLOAD';
  EXECUTE IMMEDIATE ('DROP MATERIALIZED VIEW MV_STUDENT_FILE_DOWNLOAD');
  EXECUTE IMMEDIATE l_strsql;
END;

CREATE OR REPLACE PROCEDURE REFRESH_MV_SD AS
  l_strsql clob;
BEGIN

  SELECT MV_DDL
    INTO l_strsql
    FROM TAB_MV_DDL_STORE
   WHERE MV_NAME = 'MV_STUDENT_DETAILS';
  EXECUTE IMMEDIATE ('DROP MATERIALIZED VIEW MV_STUDENT_DETAILS');
  EXECUTE IMMEDIATE l_strsql;
END;

--STEP 2
BEGIN
  DBMS_SCHEDULER.CREATE_JOB(job_name   => 'REFRESH_MVIEW1',
                            job_type   => 'STORED_PROCEDURE', --'PLSQL_BLOCK',
                            job_action => 'REFRESH_MV_SFD',
                            start_date      => SYSTIMESTAMP,
                            repeat_interval => NULL,      
                            end_date        => NULL,        
                            comments        => 'test');
END;

BEGIN
  DBMS_SCHEDULER.CREATE_JOB(job_name   => 'REFRESH_MVIEW2',
                            job_type   => 'STORED_PROCEDURE', --'PLSQL_BLOCK',
                            job_action => 'REFRESH_MV_SD',
                            start_date      => SYSTIMESTAMP,
                            repeat_interval => NULL,      
                            end_date        => NULL,        
                            comments        => 'test');
END;


SELECT JOB_NAME, STATE FROM DBA_SCHEDULER_JOBS
WHERE JOB_NAME IN ('REFRESH_MVIEW1','REFRESH_MVIEW2')

BEGIN
DBMS_SCHEDULER.enable('REFRESH_MVIEW1');
END;

BEGIN
DBMS_SCHEDULER.enable('REFRESH_MVIEW2');
END;

SELECT JOB_NAME, STATE
FROM   USER_SCHEDULER_JOBS;

SELECT *
FROM   USER_SCHEDULER_JOB_LOG
ORDER BY LOG_DATE DESC;


select LAST_REFRESH_DATE from all_mviews where owner = 'TASC' AND MVIEW_NAME = 'MV_STUDENT_FILE_DOWNLOAD'
--9/6/2016 9:59:57 AM

MV_STUDENT_FILE_DOWNLOAD
   
