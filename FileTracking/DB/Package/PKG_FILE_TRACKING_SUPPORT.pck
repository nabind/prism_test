CREATE OR REPLACE PACKAGE PKG_FILE_TRACKING_SUPPORT IS

  -- Author  : Abir
  -- Created : 4/11/2016 11:26:10 AM
  -- Purpose : For all Tier III suppport related procedures and functions

  -- Public type declarations
  TYPE GET_REFCURSOR IS REF CURSOR;

  -- Public function and procedure declarations
  PROCEDURE SP_UNLOCK_STUDENT_SCHEDULE(P_UUID              IN VARCHAR2,
                                       P_STATE_CODE        IN VARCHAR2,
                                       P_SCHEDID           IN NUMBER,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);
                                       
  PROCEDURE SP_UNLOCK_STUDENT_SCHE_UNDO(P_UUID              IN VARCHAR2,
                                        P_STATE_CODE        IN VARCHAR2,
                                        P_SCHEDID           IN NUMBER,
                                        P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);
                                        
  PROCEDURE SP_INVALIDATE_SCHEDULE(    P_UUID              IN VARCHAR2,
                                       P_STATE_CODE        IN VARCHAR2,
                                       P_SCHEDID           IN NUMBER,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);
                                       
  PROCEDURE SP_INVALIDATE_SCHEDULE_UNDO(P_UUID              IN VARCHAR2,
                                        P_STATE_CODE        IN VARCHAR2,
                                        P_SCHEDID           IN NUMBER,
                                        P_OUT_EXCEP_ERR_MSG OUT VARCHAR2);                                                                                 

END PKG_FILE_TRACKING_SUPPORT;
/
CREATE OR REPLACE PACKAGE BODY PKG_FILE_TRACKING_SUPPORT IS
  /*
  * This procedure is used to unlock the schedule for selected subtest of a specific student
  */     
  PROCEDURE SP_UNLOCK_STUDENT_SCHEDULE(P_UUID              IN VARCHAR2,
                                       P_STATE_CODE        IN VARCHAR2,
                                       P_SCHEDID           IN NUMBER,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  
  BEGIN
    -- To keep the backup for undo
    INSERT INTO ER_TEST_SCHEDULE_SUPPORT
      (SELECT * 
         FROM ER_TEST_SCHEDULE
        WHERE ER_STUDID IN (SELECT ER_STUDID
                              FROM ER_STUDENT_DEMO
                             WHERE UUID = P_UUID
                               AND STATE_CODE = P_STATE_CODE)
          AND SCHEDULE_ID = P_SCHEDID);
  
    UPDATE ER_TEST_SCHEDULE
       SET PP_OAS_LINKEDID = NULL, UPDATED_DATE_TIME = SYSDATE
     WHERE ER_STUDID IN (SELECT ER_STUDID
                           FROM ER_STUDENT_DEMO
                          WHERE UUID = P_UUID
                            AND STATE_CODE = P_STATE_CODE)
       AND SCHEDULE_ID = P_SCHEDID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_UNLOCK_STUDENT_SCHEDULE;
  
  
  /*
  * This procedure is used to undo the unlock the schedule for selected subtest of a specific student
  */  
  PROCEDURE SP_UNLOCK_STUDENT_SCHE_UNDO(P_UUID              IN VARCHAR2,
                                        P_STATE_CODE        IN VARCHAR2,
                                        P_SCHEDID           IN NUMBER,
                                        P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS 
  
  
  V_PP_OAS_LINKEDID NUMBER := 0;
  V_ER_TEST_SCHEDID NUMBER :=0;
  
  BEGIN
  
  SELECT PP_OAS_LINKEDID, ER_TEST_SCHEDID
    INTO V_PP_OAS_LINKEDID, V_ER_TEST_SCHEDID
    FROM ER_TEST_SCHEDULE_SUPPORT
   WHERE ER_STUDID IN (SELECT ER_STUDID
                         FROM ER_STUDENT_DEMO
                        WHERE UUID = P_UUID
                          AND STATE_CODE = P_STATE_CODE)
     AND SCHEDULE_ID = P_SCHEDID;
  
    UPDATE ER_TEST_SCHEDULE
       SET PP_OAS_LINKEDID = v_PP_OAS_LINKEDID, UPDATED_DATE_TIME = SYSDATE
     WHERE ER_TEST_SCHEDID = v_ER_TEST_SCHEDID;
  
  DELETE FROM ER_TEST_SCHEDULE_SUPPORT
   WHERE ER_TEST_SCHEDID = V_ER_TEST_SCHEDID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));
  END SP_UNLOCK_STUDENT_SCHE_UNDO;
  
 /*
  * This procedure is used to invalidate the schedule for selected subtest of a specific student
  */
  
  PROCEDURE SP_INVALIDATE_SCHEDULE(    P_UUID              IN VARCHAR2,
                                       P_STATE_CODE        IN VARCHAR2,
                                       P_SCHEDID           IN NUMBER,
                                       P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS
  BEGIN 
  
     -- To keep the backup for undo
    INSERT INTO ER_TEST_SCHEDULE_SUPPORT
      (SELECT * 
         FROM ER_TEST_SCHEDULE
        WHERE ER_STUDID IN (SELECT ER_STUDID
                              FROM ER_STUDENT_DEMO
                             WHERE UUID = P_UUID
                               AND STATE_CODE = P_STATE_CODE)
          AND SCHEDULE_ID = P_SCHEDID);
    
    UPDATE ER_TEST_SCHEDULE
       SET FORM              = -99,
           CONTENT_TEST_CODE = 99,
           PP_OAS_LINKEDID   = -99,
           SCHEDULE_ID       = 99,
           UPDATED_DATE_TIME = SYSDATE
     WHERE ER_STUDID IN (SELECT ER_STUDID
                           FROM ER_STUDENT_DEMO
                          WHERE UUID = P_UUID
                            AND STATE_CODE = P_STATE_CODE)
       AND SCHEDULE_ID = P_SCHEDID;
    
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_INVALIDATE_SCHEDULE; 
  
   /*
  * This procedure is used to undo the invalidate the schedule for selected subtest of a specific student
  */  
  PROCEDURE SP_INVALIDATE_SCHEDULE_UNDO(P_UUID              IN VARCHAR2,
                                        P_STATE_CODE        IN VARCHAR2,
                                        P_SCHEDID           IN NUMBER,
                                        P_OUT_EXCEP_ERR_MSG OUT VARCHAR2) IS 
  
  
  V_FORM ER_TEST_SCHEDULE.FORM%TYPE := NULL;
  V_CONTENT_TEST_CODE ER_TEST_SCHEDULE.CONTENT_TEST_CODE%TYPE := 0;
  V_PP_OAS_LINKEDID NUMBER := 0;
  V_ER_TEST_SCHEDID NUMBER :=0;
  V_SCHEDULE_ID NUMBER :=0;
  
  BEGIN
  
  SELECT PP_OAS_LINKEDID, ER_TEST_SCHEDID, FORM, CONTENT_TEST_CODE, SCHEDULE_ID
    INTO V_PP_OAS_LINKEDID, V_ER_TEST_SCHEDID,V_FORM,V_CONTENT_TEST_CODE,V_SCHEDULE_ID
    FROM ER_TEST_SCHEDULE_SUPPORT
   WHERE ER_STUDID IN (SELECT ER_STUDID
                         FROM ER_STUDENT_DEMO
                        WHERE UUID = P_UUID
                          AND STATE_CODE = P_STATE_CODE)
   --  AND SCHEDULE_ID = P_SCHEDID;
   AND ROWNUM = 1;

  
    UPDATE ER_TEST_SCHEDULE
       SET FORM              = V_FORM,
           CONTENT_TEST_CODE = V_CONTENT_TEST_CODE,
           PP_OAS_LINKEDID   = v_PP_OAS_LINKEDID,
           SCHEDULE_ID       = V_SCHEDULE_ID,
           UPDATED_DATE_TIME = SYSDATE
     WHERE ER_TEST_SCHEDID = v_ER_TEST_SCHEDID;

  
  DELETE FROM ER_TEST_SCHEDULE_SUPPORT
   WHERE ER_TEST_SCHEDID = V_ER_TEST_SCHEDID;

  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 12, 255));

  END SP_INVALIDATE_SCHEDULE_UNDO;                                     
  
END PKG_FILE_TRACKING_SUPPORT;
/
