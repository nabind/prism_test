-- Need to add new constraint on job_status in job_tracking for GRF
ALTER TABLE JOB_TRACKING
DROP CONSTRAINT CHK_JOB_STATUS;  
  
ALTER TABLE JOB_TRACKING
  ADD CONSTRAINT CHK_JOB_STATUS
  CHECK (JOB_STATUS  IN('SU','IP','CO','ER','DE','AR','DL','FT','NA','CR'));  