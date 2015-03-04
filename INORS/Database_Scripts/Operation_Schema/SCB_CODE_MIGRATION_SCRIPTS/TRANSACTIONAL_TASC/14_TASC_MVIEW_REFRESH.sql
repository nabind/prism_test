-- pkg_student_file_download should be there in TASC

begin

DBMS_MVIEW.REFRESH('CUSTOMER_INFO', 'C');

end;

begin

DBMS_MVIEW.REFRESH('CUST_PRODUCT_LINK', 'C');

end;

begin

DBMS_MVIEW.REFRESH('MV_ORG_TP_STRUCTURE', 'C');

end;

begin

DBMS_MVIEW.REFRESH('SUBTEST_DIM', 'C');

end;

begin

DBMS_MVIEW.REFRESH('FORM_DIM', 'C');

end;

begin

DBMS_MVIEW.REFRESH('GENDER_DIM', 'C');

end;

begin

DBMS_MVIEW.REFRESH('OBJECTIVE_DIM', 'C');

end;


begin

DBMS_MVIEW.REFRESH('MV_TEST_PROGRAM', 'C');

end;

begin

DBMS_MVIEW.REFRESH('ORG_NODE_DIM_HIER', 'C');

end;

begin

DBMS_MVIEW.REFRESH('MV_STUDENT_DETAILS', 'F');

end;

begin

DBMS_MVIEW.REFRESH('MV_STUDENT_FILE_DOWNLOAD', 'F');

end;

begin

DBMS_MVIEW.REFRESH('MV_SUB_OBJ_FORM_MAP', 'C');

end;

begin

DBMS_MVIEW.REFRESH('MV_SUBTEST_SCORE_TYPE_MAP', 'C');

end;



