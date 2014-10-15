CREATE OR REPLACE PACKAGE PKG_MY_ACCOUNT IS

  -- AUTHOR  : JOY KUMAR PAL
  -- CREATED : 10/14/2014 9:31:03 AM
  -- PURPOSE : MY ACCOUNT

  TYPE REF_CURSOR IS REF CURSOR;

  PROCEDURE SP_GET_ACCOUNT_DETAILS(P_IN_USERNAME                IN USERS.USERNAME%TYPE,
                                   P_OUT_USER_CURSOR            OUT REF_CURSOR,
                                   P_OUT_SECURITY_QUESTIONS_CUR OUT REF_CURSOR,
                                   P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2);

  PROCEDURE SP_GET_SECURITY_QUESTIONS(P_IN_USERNAME                IN USERS.USERNAME%TYPE,
                                      P_OUT_SECURITY_QUESTIONS_CUR OUT REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2);

  PROCEDURE SP_SAVE_PH_ANSWER(P_IN_USERID           IN NUMBER,
                              P_IN_PH_QUESTIONIDS   IN VARCHAR2,
                              P_IN_PH_ANSWERIDS     IN VARCHAR2,
                              P_IN_PH_ANSWER_VALUES IN VARCHAR2,
                              P_OUT_STATUS_NUMBER   OUT NUMBER,
                              P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

END PKG_MY_ACCOUNT;
/
CREATE OR REPLACE PACKAGE BODY PKG_MY_ACCOUNT IS

  ---- GET USERS ACCOUNT DETAILS.
  PROCEDURE SP_GET_ACCOUNT_DETAILS(P_IN_USERNAME                IN USERS.USERNAME%TYPE,
                                   P_OUT_USER_CURSOR            OUT REF_CURSOR,
                                   P_OUT_SECURITY_QUESTIONS_CUR OUT REF_CURSOR,
                                   P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2) IS
    P_OUT_EXCEP_ERR_MSG1 VARCHAR2(1000);
  BEGIN
  
    OPEN P_OUT_USER_CURSOR FOR
      SELECT USERID,
             USERNAME,
             DISPLAY_USERNAME,
             LAST_NAME,
             FIRST_NAME,
             MIDDLE_NAME,
             EMAIL_ADDRESS,
             PHONE_NO,
             COUNTRY,
             ZIPCODE,
             STATE,
             STREET,
             CITY,
             SALT
        FROM USERS USR
       WHERE UPPER(USR.USERNAME) = UPPER(P_IN_USERNAME);
  
    SP_GET_SECURITY_QUESTIONS(P_IN_USERNAME,
                              P_OUT_SECURITY_QUESTIONS_CUR,
                              P_OUT_EXCEP_ERR_MSG1);
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG || ' ' ||
                             P_OUT_EXCEP_ERR_MSG1;
  END SP_GET_ACCOUNT_DETAILS;

  ---- GET SECURITY QUESTIONS.
  PROCEDURE SP_GET_SECURITY_QUESTIONS(P_IN_USERNAME                IN USERS.USERNAME%TYPE,
                                      P_OUT_SECURITY_QUESTIONS_CUR OUT REF_CURSOR,
                                      P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2) IS
  
  BEGIN
  
    IF P_IN_USERNAME = '0' THEN
      OPEN P_OUT_SECURITY_QUESTIONS_CUR FOR
        SELECT PH_QUESTIONID  AS QUESTION_ID,
               QUESTION_VALUE AS QUESTION,
               QUESTION_SEQ   AS SNO
          FROM PWD_HINT_QUESTIONS
         WHERE ACTIVATION_STATUS IN ('AC', 'SS');
    ELSE
      OPEN P_OUT_SECURITY_QUESTIONS_CUR FOR
        SELECT DISTINCT Q.PH_QUESTIONID  AS QUESTION_ID,
                        Q.QUESTION_VALUE AS QUESTION,
                        Q.QUESTION_SEQ   AS SNO,
                        A.PH_ANSWERID    AS ANSWER_ID,
                        A.ANSWER_VALUE   AS ANSWER
          FROM PWD_HINT_QUESTIONS Q, PWD_HINT_ANSWERS A, USERS U
         WHERE U.USERID = A.USERID
           AND Q.PH_QUESTIONID = A.PH_QUESTIONID
           AND UPPER(U.USERNAME) = UPPER(P_IN_USERNAME)
           AND U.ACTIVATION_STATUS IN ('SS', 'AC')
         ORDER BY A.PH_ANSWERID;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_GET_SECURITY_QUESTIONS;

  /*
  SAVE PASSWORD HINT QUESTIONS AND ANSWERS
  */
  PROCEDURE SP_SAVE_PH_ANSWER(P_IN_USERID           IN NUMBER,
                              P_IN_PH_QUESTIONIDS   IN VARCHAR2,
                              P_IN_PH_ANSWERIDS     IN VARCHAR2,
                              P_IN_PH_ANSWER_VALUES IN VARCHAR2,
                              P_OUT_STATUS_NUMBER   OUT NUMBER,
                              P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  
    V_NO_OF_ANSWERS NUMBER(3) := 0;
  
  BEGIN
  
    P_OUT_STATUS_NUMBER := 0;
  
    SELECT COUNT(1)
      INTO V_NO_OF_ANSWERS
      FROM PWD_HINT_ANSWERS P
     WHERE P.USERID = P_IN_USERID;
  
    IF V_NO_OF_ANSWERS = 0 THEN
    
      --INSERTING ANSWERS IF THERE NO EXISTING ANSWERS  
      FOR REC_SECURITY_QAS IN (WITH T AS (SELECT P_IN_PH_QUESTIONIDS   COL1,
                                                  P_IN_PH_ANSWER_VALUES COL2
                                             FROM DUAL)SELECT REGEXP_SUBSTR(COL1,
                                                     '[^~]+',
                                                     1,
                                                     ROWNUM) PH_QUESTIONID,
                                       REGEXP_SUBSTR(COL2,
                                                     '[^~]+',
                                                     1,
                                                     ROWNUM) ANSWER_VALUE
                                 FROM T
                               CONNECT BY LEVEL <=
                                          REGEXP_COUNT(COL1,
                                                       '~') + 1) LOOP
      
        INSERT INTO PWD_HINT_ANSWERS
          (PH_ANSWERID,
           USERID,
           PH_QUESTIONID,
           ANSWER_VALUE,
           CREATED_DATE_TIME)
        VALUES
          (PWD_HINT_ANSWERS_SEQ.NEXTVAL,
           P_IN_USERID,
           REC_SECURITY_QAS.PH_QUESTIONID,
           REC_SECURITY_QAS.ANSWER_VALUE,
           SYSDATE);
      
      END LOOP;
    
    ELSE
    
      --UPDATING ANSWER IF THERE IS EXISTING ANSWER
      FOR REC_SECURITY_QAS IN (WITH T AS (SELECT P_IN_PH_QUESTIONIDS   COL1,
                                                  P_IN_PH_ANSWER_VALUES COL2,
                                                  P_IN_PH_ANSWERIDS     COL3
                                             FROM DUAL)SELECT REGEXP_SUBSTR(COL1,
                                                     '[^~]+',
                                                     1,
                                                     ROWNUM) PH_QUESTIONID,
                                       REGEXP_SUBSTR(COL2,
                                                     '[^~]+',
                                                     1,
                                                     ROWNUM) ANSWER_VALUE,
                                       REGEXP_SUBSTR(COL3,
                                                     '[^~]+',
                                                     1,
                                                     ROWNUM) PH_ANSWERID
                                 FROM T
                               CONNECT BY LEVEL <=
                                          REGEXP_COUNT(COL1,
                                                       '~') + 1) LOOP
      
        UPDATE PWD_HINT_ANSWERS PHA
           SET PHA.PH_QUESTIONID     = REC_SECURITY_QAS.PH_QUESTIONID,
               PHA.ANSWER_VALUE      = REC_SECURITY_QAS.ANSWER_VALUE,
               PHA.UPDATED_DATE_TIME = SYSDATE
         WHERE PHA.USERID = P_IN_USERID
           AND PHA.PH_ANSWERID = REC_SECURITY_QAS.PH_ANSWERID;
      
      END LOOP;
    
    END IF;
  
    P_OUT_STATUS_NUMBER := 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      ROLLBACK;
  END SP_SAVE_PH_ANSWER;

END PKG_MY_ACCOUNT;
/
