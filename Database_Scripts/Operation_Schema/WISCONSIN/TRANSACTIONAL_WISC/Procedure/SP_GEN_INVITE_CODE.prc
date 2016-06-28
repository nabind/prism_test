CREATE OR REPLACE PROCEDURE SP_GEN_INVITE_CODE(CHARSET     VARCHAR2 := 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789',
                                               LENGTH      NUMBER := 16,
                                               GROUPLENGTH NUMBER := 4) AS
  CHARSETLEN NUMBER;
  I          NUMBER;
  J          NUMBER := 0;
  V_RETVAL   VARCHAR2(50);
  CURSOR CUR_INVITATION_CODE IS
    SELECT /*adminid,*/
     ICID
      FROM INVITATION_CODE
     WHERE INVITATION_CODE = '1';
  REC_INVITATION_CODE CUR_INVITATION_CODE%ROWTYPE;
BEGIN

  FOR REC_INVITATION_CODE IN CUR_INVITATION_CODE LOOP

    V_RETVAL := '';

    SELECT LENGTH(CHARSET) INTO CHARSETLEN FROM DUAL;

    FOR I IN 1 .. LENGTH LOOP
      -- do not allow 0 to be an argument since
      -- substr(foo, 0) and substr(foo, 1) are the same
      V_RETVAL := V_RETVAL ||
                  SUBSTR(CHARSET,
                         1 + (DBMS_RANDOM.VALUE * (CHARSETLEN - 1)),
                         1);
      IF MOD(I, GROUPLENGTH) = 0 AND I <> LENGTH THEN
        V_RETVAL := V_RETVAL || '-';
      END IF;
    END LOOP;

    UPDATE INVITATION_CODE
       SET INVITATION_CODE = V_RETVAL
     WHERE ICID = REC_INVITATION_CODE.ICID/*
       AND INT_STUDENT_ID = REC_INVITATION_CODE.INT_STUDENT_ID
       AND CUST_PROD_ID = REC_INVITATION_CODE.CUST_PROD_ID*/
    /*and adminid=rec_invitation_code.adminid*/
    ;

    J := J + 1;

    IF J > 100 THEN
      COMMIT;
      J := 0;
    END IF;

  END LOOP;

  COMMIT;
  --   close cur_invitation_code;

END SP_GEN_INVITE_CODE;
/
