CREATE OR REPLACE FUNCTION get_test_date (p_SUBTESTID NUMBER, p_sudent_bio_id NUMBER) RETURN DATE
 DETERMINISTIC IS

test_date DATE ;


begin
    SELECT DISTINCT  trunc(a.date_test_taken)
    INTO test_date
    FROM STU_SUBTEST_DEMO_VALUES A
                                   WHERE A.SUBTESTID =p_SUBTESTID
                                   AND A.STUDENT_BIO_ID =p_sudent_bio_id  ;
 RETURN test_date;
end ;
/
