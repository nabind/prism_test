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

  PROCEDURE SP_UPDATE_USER_ACCOUNT(P_IN_USERID           IN USERS.USERID%TYPE,
                                   P_IN_PASSWORD         IN USERS.PASSWORD%TYPE,
                                   P_IN_SALT             IN USERS.SALT%TYPE,
                                   P_IN_FIRST_NAME       IN USERS.FIRST_NAME%TYPE,
                                   P_IN_LAST_NAME        IN USERS.LAST_NAME%TYPE,
                                   P_IN_EMAIL_ADDRESS    IN USERS.EMAIL_ADDRESS%TYPE,
                                   P_IN_PHONE_NO         IN USERS.PHONE_NO%TYPE,
                                   P_IN_COUNTRY          IN USERS.COUNTRY%TYPE,
                                   P_IN_ZIPCODE          IN USERS.ZIPCODE%TYPE,
                                   P_IN_STATE            IN USERS.STATE%TYPE,
                                   P_IN_STREET           IN USERS.STREET%TYPE,
                                   P_IN_CITY             IN USERS.CITY%TYPE,
                                   P_IN_DISPLAY_USERNAME IN USERS.DISPLAY_USERNAME%TYPE,
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
    V_ERR_MSG       VARCHAR2(1000) := 'PKG_MY_ACCOUNT.SP_SAVE_PH_ANSWER FAILED: ';
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
      ROLLBACK;
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := V_ERR_MSG || UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_SAVE_PH_ANSWER;

  PROCEDURE SP_UPDATE_USER_ACCOUNT(P_IN_USERID           IN USERS.USERID%TYPE,
                                   P_IN_PASSWORD         IN USERS.PASSWORD%TYPE,
                                   P_IN_SALT             IN USERS.SALT%TYPE,
                                   P_IN_FIRST_NAME       IN USERS.FIRST_NAME%TYPE,
                                   P_IN_LAST_NAME        IN USERS.LAST_NAME%TYPE,
                                   P_IN_EMAIL_ADDRESS    IN USERS.EMAIL_ADDRESS%TYPE,
                                   P_IN_PHONE_NO         IN USERS.PHONE_NO%TYPE,
                                   P_IN_COUNTRY          IN USERS.COUNTRY%TYPE,
                                   P_IN_ZIPCODE          IN USERS.ZIPCODE%TYPE,
                                   P_IN_STATE            IN USERS.STATE%TYPE,
                                   P_IN_STREET           IN USERS.STREET%TYPE,
                                   P_IN_CITY             IN USERS.CITY%TYPE,
                                   P_IN_DISPLAY_USERNAME IN USERS.DISPLAY_USERNAME%TYPE,
                                   P_IN_PH_QUESTIONIDS   IN VARCHAR2,
                                   P_IN_PH_ANSWERIDS     IN VARCHAR2,
                                   P_IN_PH_ANSWER_VALUES IN VARCHAR2,
                                   P_OUT_STATUS_NUMBER   OUT NUMBER,
                                   P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS

    P_OUT_STATUS_PWD_HISTORY NUMBER := 0;
    P_OUT_STATUS_PH_ANSWER   NUMBER := 0;
    P_OUT_EXCEP_ERR_MSG1     VARCHAR2(1000);
    V_ERR_MSG                VARCHAR2(1000) := 'PKG_MY_ACCOUNT.SP_UPDATE_USER_ACCOUNT FAILED: ';
  BEGIN

    P_OUT_STATUS_NUMBER := 0;

    IF P_IN_PASSWORD <> '-99' THEN

      --INSERTING PASSWORD DATA IN USERS TABLE
      UPDATE USERS
         SET IS_FIRSTTIME_LOGIN = 'N',
             PASSWORD           = P_IN_PASSWORD,
             SALT               = P_IN_SALT
       WHERE USERID = P_IN_USERID;

      PKG_ADMIN_MODULE.SP_SAVE_PASSWORD_HISTORY(P_IN_USERID,
                                                P_IN_PASSWORD,
                                                P_OUT_STATUS_PWD_HISTORY,
                                                P_OUT_EXCEP_ERR_MSG1);

      IF P_OUT_STATUS_PWD_HISTORY <> 1 THEN
        ROLLBACK;
        P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG1;
        RAISE_APPLICATION_ERROR(-20000, P_OUT_EXCEP_ERR_MSG);
      END IF;
    END IF;

    UPDATE USERS
       SET LAST_NAME         = P_IN_LAST_NAME,
           FIRST_NAME        = P_IN_FIRST_NAME,
           EMAIL_ADDRESS     = P_IN_EMAIL_ADDRESS,
           PHONE_NO          = P_IN_PHONE_NO,
           COUNTRY           = P_IN_COUNTRY,
           ZIPCODE           = P_IN_ZIPCODE,
           STATE             = P_IN_STATE,
           STREET            = P_IN_STREET,
           CITY              = P_IN_CITY,
           DISPLAY_USERNAME  = P_IN_DISPLAY_USERNAME,
           UPDATED_DATE_TIME = SYSDATE
     WHERE USERID = P_IN_USERID;

    SP_SAVE_PH_ANSWER(P_IN_USERID,
                      P_IN_PH_QUESTIONIDS,
                      P_IN_PH_ANSWERIDS,
                      P_IN_PH_ANSWER_VALUES,
                      P_OUT_STATUS_PH_ANSWER,
                      P_OUT_EXCEP_ERR_MSG1);

    IF P_OUT_STATUS_PH_ANSWER <> 1 THEN
      ROLLBACK;
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG1;
      RAISE_APPLICATION_ERROR(-20000, P_OUT_EXCEP_ERR_MSG);
    END IF;

    P_OUT_STATUS_NUMBER := 1;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      P_OUT_STATUS_NUMBER := 0;
      P_OUT_EXCEP_ERR_MSG := V_ERR_MSG || UPPER(SUBSTR(SQLERRM, 0, 255));
  END SP_UPDATE_USER_ACCOUNT;

END PKG_MY_ACCOUNT;
/
