-- Need to add new constraint on request_type in job_tracking for GRF

ALTER TABLE JOB_TRACKING
DROP CONSTRAINT CHK_REQUEST_TYPE;

ALTER TABLE JOB_TRACKING
  ADD CONSTRAINT CHK_REQUEST_TYPE
  CHECK (REQUEST_TYPE IN ('SDF','SBE','GDF','GRF'));