CREATE OR REPLACE PACKAGE PKG_ER_MODULE IS

  TYPE REF_CURSOR IS REF CURSOR;

  PROCEDURE SP_CREATE_HISTORY(P_IN_UUID                     IN ER_STUDENT_SCHED_HISTORY.UUID%TYPE,
                              P_IN_CTB_CUSTOMER_ID          IN ER_STUDENT_SCHED_HISTORY.CTB_CUSTOMER_ID%TYPE,
                              P_IN_STATE_CODE               IN ER_STUDENT_SCHED_HISTORY.STATE_CODE%TYPE,
                              P_IN_STATENAME                IN ER_STUDENT_SCHED_HISTORY.STATENAME%TYPE,
                              P_IN_FIRSTNAME                IN ER_STUDENT_SCHED_HISTORY.FIRSTNAME%TYPE,
                              P_IN_MIDDLENAME               IN ER_STUDENT_SCHED_HISTORY.MIDDLENAME%TYPE,
                              P_IN_LASTNAME                 IN ER_STUDENT_SCHED_HISTORY.LASTNAME%TYPE,
                              P_IN_DATEOFBIRTH              IN ER_STUDENT_SCHED_HISTORY.DATEOFBIRTH%TYPE,
                              P_IN_GENDER                   IN ER_STUDENT_SCHED_HISTORY.GENDER%TYPE,
                              P_IN_GOVERNMENTID             IN ER_STUDENT_SCHED_HISTORY.GOVERNMENTID%TYPE,
                              P_IN_GOVERNMENTIDTYPE         IN ER_STUDENT_SCHED_HISTORY.GOVERNMENTIDTYPE%TYPE,
                              P_IN_ADDRESS1                 IN ER_STUDENT_SCHED_HISTORY.ADDRESS1%TYPE,
                              P_IN_ADDRESS2                 IN ER_STUDENT_SCHED_HISTORY.ADDRESS2%TYPE,
                              P_IN_CITY                     IN ER_STUDENT_SCHED_HISTORY.CITY%TYPE,
                              P_IN_COUNTY                   IN ER_STUDENT_SCHED_HISTORY.COUNTY%TYPE,
                              P_IN_STATE                    IN ER_STUDENT_SCHED_HISTORY.STATE%TYPE,
                              P_IN_ZIP                      IN ER_STUDENT_SCHED_HISTORY.ZIP%TYPE,
                              P_IN_EMAIL                    IN ER_STUDENT_SCHED_HISTORY.EMAIL%TYPE,
                              P_IN_ALTERNATEEMAIL           IN ER_STUDENT_SCHED_HISTORY.ALTERNATEEMAIL%TYPE,
                              P_IN_PRIMARYPHONENUMBER       IN ER_STUDENT_SCHED_HISTORY.PRIMARYPHONENUMBER%TYPE,
                              P_IN_CELLPHONENUMBER          IN ER_STUDENT_SCHED_HISTORY.CELLPHONENUMBER%TYPE,
                              P_IN_ALTERNATENUMBER          IN ER_STUDENT_SCHED_HISTORY.ALTERNATENUMBER%TYPE,
                              P_IN_ETHNICITY                IN ER_STUDENT_SCHED_HISTORY.RESOLVED_ETHNICITY_RACE%TYPE,
                              P_IN_HOMELANGUAGE             IN ER_STUDENT_SCHED_HISTORY.HOMELANGUAGE%TYPE,
                              P_IN_EDUCATIONLEVEL           IN ER_STUDENT_SCHED_HISTORY.EDUCATIONLEVEL%TYPE,
                              P_IN_ATTENDCOLLEGE            IN ER_STUDENT_SCHED_HISTORY.ATTENDCOLLEGE%TYPE,
                              P_IN_CONTACT                  IN ER_STUDENT_SCHED_HISTORY.CONTACT%TYPE,
                              P_IN_EXAMINEECOUNTYPARISHCODE IN ER_STUDENT_SCHED_HISTORY.EXAMINEECOUNTYPARISHCODE%TYPE,
                              P_IN_REGISTEREDON             IN ER_STUDENT_SCHED_HISTORY.REGISTEREDON%TYPE,
                              P_IN_REGISTEREDATTESTCENTER   IN ER_STUDENT_SCHED_HISTORY.REGISTEREDATTESTCENTER%TYPE,
                              P_IN_REGATTESTCENTERCODE      IN ER_STUDENT_SCHED_HISTORY.REGISTEREDATTESTCENTERCODE%TYPE,
                              P_IN_REGISTEREDCOUNTYPARISHCD IN ER_STUDENT_SCHED_HISTORY.Regst_TC_CountyParishCode%TYPE,
                              P_IN_SCHEDULEID               IN ER_STUDENT_SCHED_HISTORY.SCHEDULE_ID%TYPE,
                              P_IN_DATE_SCHEDULED           IN ER_STUDENT_SCHED_HISTORY.DATE_SCHEDULED%TYPE,
                              P_IN_TIMEOFDAY                IN ER_STUDENT_SCHED_HISTORY.TIMEOFDAY%TYPE,
                              P_IN_DATECHECKEDIN            IN ER_STUDENT_SCHED_HISTORY.DATECHECKEDIN%TYPE,
                              P_IN_CONTENT_AREA_CODE        IN ER_STUDENT_SCHED_HISTORY.CONTENT_AREA_CODE%TYPE,
                              P_IN_CONTENT_TEST_TYPE        IN ER_STUDENT_SCHED_HISTORY.CONTENT_TEST_TYPE%TYPE,
                              P_IN_CONTENT_TEST_CODE        IN ER_STUDENT_SCHED_HISTORY.CONTENT_TEST_CODE%TYPE,
                              P_IN_BARCODE                  IN ER_STUDENT_SCHED_HISTORY.BARCODE%TYPE,
                              P_IN_FORM                     IN ER_STUDENT_SCHED_HISTORY.FORM%TYPE,
                              P_IN_TASCREADINESS            IN ER_STUDENT_SCHED_HISTORY.TASCREADINESS%TYPE,
                              P_IN_ECC                      IN ER_STUDENT_SCHED_HISTORY.ECC%TYPE,
                              P_IN_TESTCENTERCODE           IN ER_STUDENT_SCHED_HISTORY.TESTCENTERCODE%TYPE,
                              P_IN_TESTCENTERNAME           IN ER_STUDENT_SCHED_HISTORY.TESTCENTERNAME%TYPE,
                              P_IN_SCHEDULEDCOUNTYPARISHCD  IN ER_STUDENT_SCHED_HISTORY.Regst_TC_CountyParishCode%TYPE,
                              P_OUT_HISTORY_ID              OUT NUMBER,
                              P_OUT_ERROR_CODE              OUT NUMBER,
                              P_OUT_EXCEP_ERR_MSG           OUT VARCHAR2);

  PROCEDURE SP_CREATE_EXCEPTION(P_IN_SOURCE_SYSTEM    IN VARCHAR2,
                                P_IN_UUID             IN ER_STUDENT_DEMO.UUID%TYPE,
                                P_IN_SS_HISTID        IN ER_STUDENT_SCHED_HISTORY.ER_SS_HISTID%TYPE,
                                P_IN_DESCRIPTION      IN ER_EXCEPTION_DATA.DESCRIPTION%TYPE,
                                p_IN_ERROR_CODE       IN ER_EXCEPTION_DATA.EXCEPTION_CODE%TYPE,
                                P_IN_EXCEPTION_STATUS IN ER_EXCEPTION_DATA.EXCEPTION_STATUS%TYPE,
                                P_OUT_EXCEPTION_ID    OUT NUMBER,
                                P_OUT_ERROR_CODE      OUT NUMBER,
                                P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2);

  PROCEDURE SP_CREATE_STUDENT_DEMO(P_IN_UUID                     IN ER_STUDENT_DEMO.UUID%TYPE,
                                   P_IN_STATE_CODE               IN ER_STUDENT_DEMO.STATE_CODE%TYPE,
                                   P_IN_STATENAME                IN ER_STUDENT_DEMO.STATENAME%TYPE,
                                   P_IN_CTB_CUSTOMER_ID          IN ER_STUDENT_DEMO.CTB_CUSTOMER_ID%TYPE,
                                   P_IN_FIRSTNAME                IN ER_STUDENT_DEMO.FIRSTNAME%TYPE,
                                   P_IN_MIDDLENAME               IN ER_STUDENT_DEMO.MIDDLENAME%TYPE,
                                   P_IN_LASTNAME                 IN ER_STUDENT_DEMO.LASTNAME%TYPE,
                                   P_IN_DATEOFBIRTH              IN ER_STUDENT_DEMO.DATEOFBIRTH%TYPE,
                                   P_IN_GENDER                   IN ER_STUDENT_DEMO.GENDER%TYPE,
                                   P_IN_GOVERNMENTID             IN ER_STUDENT_DEMO.GOVERNMENTID%TYPE,
                                   P_IN_GOVERNMENTIDTYPE         IN ER_STUDENT_DEMO.GOVERNMENTIDTYPE%TYPE,
                                   P_IN_ADDRESS1                 IN ER_STUDENT_DEMO.ADDRESS1%TYPE,
                                   P_IN_ADDRESS2                 IN ER_STUDENT_DEMO.ADDRESS2%TYPE,
                                   P_IN_CITY                     IN ER_STUDENT_DEMO.CITY%TYPE,
                                   P_IN_COUNTY                   IN ER_STUDENT_DEMO.COUNTY%TYPE,
                                   P_IN_STATE                    IN ER_STUDENT_DEMO.STATE%TYPE,
                                   P_IN_ZIP                      IN ER_STUDENT_DEMO.ZIP%TYPE,
                                   P_IN_EMAIL                    IN ER_STUDENT_DEMO.EMAIL%TYPE,
                                   P_IN_ALTERNATEEMAIL           IN ER_STUDENT_DEMO.ALTERNATEEMAIL%TYPE,
                                   P_IN_PRIMARYPHONENUMBER       IN ER_STUDENT_DEMO.PRIMARYPHONENUMBER%TYPE,
                                   P_IN_CELLPHONENUMBER          IN ER_STUDENT_DEMO.CELLPHONENUMBER%TYPE,
                                   P_IN_ALTERNATENUMBER          IN ER_STUDENT_DEMO.ALTERNATENUMBER%TYPE,
                                   P_IN_ETHNICITY                IN ER_STUDENT_DEMO.RESOLVED_ETHNICITY_RACE%TYPE,
                                   P_IN_HOMELANGUAGE             IN ER_STUDENT_DEMO.HOMELANGUAGE%TYPE,
                                   P_IN_EDUCATIONLEVEL           IN ER_STUDENT_DEMO.EDUCATIONLEVEL%TYPE,
                                   P_IN_ATTENDCOLLEGE            IN ER_STUDENT_DEMO.ATTENDCOLLEGE%TYPE,
                                   P_IN_CONTACT                  IN ER_STUDENT_DEMO.CONTACT%TYPE,
                                   P_IN_EXAMINEECOUNTYPARISHCODE IN ER_STUDENT_DEMO.EXAMINEECOUNTYPARISHCODE%TYPE,
                                   P_IN_REGISTEREDON             IN ER_STUDENT_DEMO.REGISTEREDON%TYPE,
                                   P_IN_REGISTEREDATTESTCENTER   IN ER_STUDENT_DEMO.REGISTEREDATTESTCENTER%TYPE,
                                   P_IN_REGATTESTCENTERCODE      IN ER_STUDENT_DEMO.REGISTEREDATTESTCENTERCODE%TYPE,
                                   P_IN_REGISTEREDCOUNTYPARISHCD IN ER_STUDENT_DEMO.Regst_TC_CountyParishCode%TYPE,
                                   P_OUT_STUD_ID                 OUT NUMBER,
                                   P_OUT_ERROR_CODE              OUT NUMBER,
                                   P_OUT_EXCEP_ERR_MSG           OUT VARCHAR2);

  PROCEDURE SP_CREATE_TEST_SCHEDULE(P_IN_UUID                    IN ER_STUDENT_DEMO.UUID%TYPE,
                                    P_IN_STUDENT_NAME            IN VARCHAR2,
                                    P_IN_STUDID                  IN ER_TEST_SCHEDULE.ER_STUDID%TYPE,
                                    P_IN_SCHEDULEID              IN ER_TEST_SCHEDULE.SCHEDULE_ID%TYPE,
                                    P_IN_DATE_SCHEDULED          IN ER_TEST_SCHEDULE.DATE_SCHEDULED%TYPE,
                                    P_IN_TIMEOFDAY               IN ER_TEST_SCHEDULE.TIMEOFDAY%TYPE,
                                    P_IN_DATECHECKEDIN           IN ER_TEST_SCHEDULE.DATECHECKEDIN%TYPE,
                                    P_IN_CONTENT_AREA_CODE       IN ER_TEST_SCHEDULE.CONTENT_AREA_CODE%TYPE,
                                    P_IN_CONTENT_TEST_TYPE       IN ER_TEST_SCHEDULE.CONTENT_TEST_TYPE%TYPE,
                                    P_IN_CONTENT_TEST_CODE       IN ER_TEST_SCHEDULE.CONTENT_TEST_CODE%TYPE,
                                    P_IN_BARCODE                 IN ER_TEST_SCHEDULE.BARCODE%TYPE,
                                    P_IN_FORM                    IN ER_TEST_SCHEDULE.FORM%TYPE,
                                    P_IN_TASCREADINESS           IN ER_TEST_SCHEDULE.TASCREADINESS%TYPE,
                                    P_IN_ECC                     IN ER_TEST_SCHEDULE.ECC%TYPE,
                                    P_IN_TESTCENTERCODE          IN ER_TEST_SCHEDULE.TESTCENTERCODE%TYPE,
                                    P_IN_TESTCENTERNAME          IN ER_TEST_SCHEDULE.TESTCENTERNAME%TYPE,
                                    P_IN_SCHEDULEDCOUNTYPARISHCD IN ER_TEST_SCHEDULE.Sched_TC_CountyParishCode%TYPE,
                                    P_IN_STATE_CODE              IN ER_STUDENT_DEMO.STATE_CODE%TYPE,
                                    P_OUT_SCHEDULE_ID            OUT NUMBER,
                                    P_OUT_ERROR_CODE             OUT NUMBER,
                                    P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2);

END PKG_ER_MODULE;
/
CREATE OR REPLACE PACKAGE BODY PKG_ER_MODULE IS

  ---- ADD TO HISTORY
  PROCEDURE SP_CREATE_HISTORY(P_IN_UUID                     IN ER_STUDENT_SCHED_HISTORY.UUID%TYPE,
                              P_IN_CTB_CUSTOMER_ID          IN ER_STUDENT_SCHED_HISTORY.CTB_CUSTOMER_ID%TYPE,
                              P_IN_STATE_CODE               IN ER_STUDENT_SCHED_HISTORY.STATE_CODE%TYPE,
                              P_IN_STATENAME                IN ER_STUDENT_SCHED_HISTORY.STATENAME%TYPE,
                              P_IN_FIRSTNAME                IN ER_STUDENT_SCHED_HISTORY.FIRSTNAME%TYPE,
                              P_IN_MIDDLENAME               IN ER_STUDENT_SCHED_HISTORY.MIDDLENAME%TYPE,
                              P_IN_LASTNAME                 IN ER_STUDENT_SCHED_HISTORY.LASTNAME%TYPE,
                              P_IN_DATEOFBIRTH              IN ER_STUDENT_SCHED_HISTORY.DATEOFBIRTH%TYPE,
                              P_IN_GENDER                   IN ER_STUDENT_SCHED_HISTORY.GENDER%TYPE,
                              P_IN_GOVERNMENTID             IN ER_STUDENT_SCHED_HISTORY.GOVERNMENTID%TYPE,
                              P_IN_GOVERNMENTIDTYPE         IN ER_STUDENT_SCHED_HISTORY.GOVERNMENTIDTYPE%TYPE,
                              P_IN_ADDRESS1                 IN ER_STUDENT_SCHED_HISTORY.ADDRESS1%TYPE,
                              P_IN_ADDRESS2                 IN ER_STUDENT_SCHED_HISTORY.ADDRESS2%TYPE,
                              P_IN_CITY                     IN ER_STUDENT_SCHED_HISTORY.CITY%TYPE,
                              P_IN_COUNTY                   IN ER_STUDENT_SCHED_HISTORY.COUNTY%TYPE,
                              P_IN_STATE                    IN ER_STUDENT_SCHED_HISTORY.STATE%TYPE,
                              P_IN_ZIP                      IN ER_STUDENT_SCHED_HISTORY.ZIP%TYPE,
                              P_IN_EMAIL                    IN ER_STUDENT_SCHED_HISTORY.EMAIL%TYPE,
                              P_IN_ALTERNATEEMAIL           IN ER_STUDENT_SCHED_HISTORY.ALTERNATEEMAIL%TYPE,
                              P_IN_PRIMARYPHONENUMBER       IN ER_STUDENT_SCHED_HISTORY.PRIMARYPHONENUMBER%TYPE,
                              P_IN_CELLPHONENUMBER          IN ER_STUDENT_SCHED_HISTORY.CELLPHONENUMBER%TYPE,
                              P_IN_ALTERNATENUMBER          IN ER_STUDENT_SCHED_HISTORY.ALTERNATENUMBER%TYPE,
                              P_IN_ETHNICITY                IN ER_STUDENT_SCHED_HISTORY.RESOLVED_ETHNICITY_RACE%TYPE,
                              P_IN_HOMELANGUAGE             IN ER_STUDENT_SCHED_HISTORY.HOMELANGUAGE%TYPE,
                              P_IN_EDUCATIONLEVEL           IN ER_STUDENT_SCHED_HISTORY.EDUCATIONLEVEL%TYPE,
                              P_IN_ATTENDCOLLEGE            IN ER_STUDENT_SCHED_HISTORY.ATTENDCOLLEGE%TYPE,
                              P_IN_CONTACT                  IN ER_STUDENT_SCHED_HISTORY.CONTACT%TYPE,
                              P_IN_EXAMINEECOUNTYPARISHCODE IN ER_STUDENT_SCHED_HISTORY.EXAMINEECOUNTYPARISHCODE%TYPE,
                              P_IN_REGISTEREDON             IN ER_STUDENT_SCHED_HISTORY.REGISTEREDON%TYPE,
                              P_IN_REGISTEREDATTESTCENTER   IN ER_STUDENT_SCHED_HISTORY.REGISTEREDATTESTCENTER%TYPE,
                              P_IN_REGATTESTCENTERCODE      IN ER_STUDENT_SCHED_HISTORY.REGISTEREDATTESTCENTERCODE%TYPE,
                              P_IN_REGISTEREDCOUNTYPARISHCD IN ER_STUDENT_SCHED_HISTORY.Regst_TC_CountyParishCode%TYPE,
                              P_IN_SCHEDULEID               IN ER_STUDENT_SCHED_HISTORY.SCHEDULE_ID%TYPE,
                              P_IN_DATE_SCHEDULED           IN ER_STUDENT_SCHED_HISTORY.DATE_SCHEDULED%TYPE,
                              P_IN_TIMEOFDAY                IN ER_STUDENT_SCHED_HISTORY.TIMEOFDAY%TYPE,
                              P_IN_DATECHECKEDIN            IN ER_STUDENT_SCHED_HISTORY.DATECHECKEDIN%TYPE,
                              P_IN_CONTENT_AREA_CODE        IN ER_STUDENT_SCHED_HISTORY.CONTENT_AREA_CODE%TYPE,
                              P_IN_CONTENT_TEST_TYPE        IN ER_STUDENT_SCHED_HISTORY.CONTENT_TEST_TYPE%TYPE,
                              P_IN_CONTENT_TEST_CODE        IN ER_STUDENT_SCHED_HISTORY.CONTENT_TEST_CODE%TYPE,
                              P_IN_BARCODE                  IN ER_STUDENT_SCHED_HISTORY.BARCODE%TYPE,
                              P_IN_FORM                     IN ER_STUDENT_SCHED_HISTORY.FORM%TYPE,
                              P_IN_TASCREADINESS            IN ER_STUDENT_SCHED_HISTORY.TASCREADINESS%TYPE,
                              P_IN_ECC                      IN ER_STUDENT_SCHED_HISTORY.ECC%TYPE,
                              P_IN_TESTCENTERCODE           IN ER_STUDENT_SCHED_HISTORY.TESTCENTERCODE%TYPE,
                              P_IN_TESTCENTERNAME           IN ER_STUDENT_SCHED_HISTORY.TESTCENTERNAME%TYPE,
                              P_IN_SCHEDULEDCOUNTYPARISHCD  IN ER_STUDENT_SCHED_HISTORY.Regst_TC_CountyParishCode%TYPE,
                              P_OUT_HISTORY_ID              OUT NUMBER,
                              P_OUT_ERROR_CODE              OUT NUMBER,
                              P_OUT_EXCEP_ERR_MSG           OUT VARCHAR2) IS
  
    HISTSEQID ER_STUDENT_SCHED_HISTORY.ER_SS_HISTID%TYPE := 0;
  
  BEGIN
  
    SELECT SEQ_ER_STUDENT_SCHED_HISTORY.NEXTVAL INTO HISTSEQID FROM DUAL;
  
    INSERT INTO ER_STUDENT_SCHED_HISTORY
      (er_ss_histid,
       uuid,
       ctb_customer_id,
       state_code,
       statename,
       firstname,
       middlename,
       lastname,
       dateofbirth,
       gender,
       governmentid,
       governmentidtype,
       address1,
       address2,
       city,
       county,
       state,
       zip,
       email,
       alternateemail,
       primaryphonenumber,
       cellphonenumber,
       alternatenumber,
       RESOLVED_ETHNICITY_RACE,
       homelanguage,
       educationlevel,
       attendcollege,
       contact,
       examineecountyparishcode,
       registeredon,
       registeredattestcenter,
       registeredattestcentercode,
       Regst_TC_CountyParishCode,
       schedule_id,
       date_scheduled,
       timeofday,
       datecheckedin,
       content_area_code,
       content_test_type,
       content_test_code,
       barcode,
       form,
       tascreadiness,
       ecc,
       testcentercode,
       testcentername,
       Sched_TC_CountyParishCode,
       datetimestamp)
    VALUES
      (HISTSEQID,
       P_IN_UUID,
       P_IN_CTB_CUSTOMER_ID,
       P_IN_STATE_CODE,
       P_IN_STATENAME,
       P_IN_FIRSTNAME,
       P_IN_MIDDLENAME,
       P_IN_LASTNAME,
       P_IN_DATEOFBIRTH, --decode(P_IN_DATEOFBIRTH, null, null, '', null, to_date(P_IN_DATEOFBIRTH, 'DD/MM/YYYY')),
       P_IN_GENDER,
       P_IN_GOVERNMENTID,
       P_IN_GOVERNMENTIDTYPE,
       P_IN_ADDRESS1,
       P_IN_ADDRESS2,
       P_IN_CITY,
       P_IN_COUNTY,
       P_IN_STATE,
       P_IN_ZIP,
       P_IN_EMAIL,
       P_IN_ALTERNATEEMAIL,
       P_IN_PRIMARYPHONENUMBER,
       P_IN_CELLPHONENUMBER,
       P_IN_ALTERNATENUMBER,
       P_IN_ETHNICITY,
       P_IN_HOMELANGUAGE,
       P_IN_EDUCATIONLEVEL,
       P_IN_ATTENDCOLLEGE,
       P_IN_CONTACT,
       P_IN_EXAMINEECOUNTYPARISHCODE,
       P_IN_REGISTEREDON,
       P_IN_REGISTEREDATTESTCENTER,
       P_IN_REGATTESTCENTERCODE,
       P_IN_REGISTEREDCOUNTYPARISHCD,
       P_IN_SCHEDULEID, --decode(nvl(P_IN_SCHEDULEID, 0), 0, 0, to_number(P_IN_SCHEDULEID)),
       P_IN_DATE_SCHEDULED,
       P_IN_TIMEOFDAY,
       P_IN_DATECHECKEDIN,
       P_IN_CONTENT_AREA_CODE,
       P_IN_CONTENT_TEST_TYPE,
       P_IN_CONTENT_TEST_CODE,
       P_IN_BARCODE,
       P_IN_FORM,
       P_IN_TASCREADINESS,
       P_IN_ECC,
       P_IN_TESTCENTERCODE,
       P_IN_TESTCENTERNAME,
       P_IN_SCHEDULEDCOUNTYPARISHCD,
       SYSDATE);
  
    P_OUT_HISTORY_ID := HISTSEQID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      P_OUT_ERROR_CODE    := 0;
    
  END SP_CREATE_HISTORY;

  ---- ADD TO EXCEPTION
  PROCEDURE SP_CREATE_EXCEPTION(P_IN_SOURCE_SYSTEM    IN VARCHAR2,
                                P_IN_UUID             IN ER_STUDENT_DEMO.UUID%TYPE,
                                P_IN_SS_HISTID        IN ER_STUDENT_SCHED_HISTORY.ER_SS_HISTID%TYPE,
                                P_IN_DESCRIPTION      IN ER_EXCEPTION_DATA.DESCRIPTION%TYPE,
                                p_IN_ERROR_CODE       IN ER_EXCEPTION_DATA.EXCEPTION_CODE%TYPE,
                                P_IN_EXCEPTION_STATUS IN ER_EXCEPTION_DATA.EXCEPTION_STATUS%TYPE,
                                P_OUT_EXCEPTION_ID    OUT NUMBER,
                                P_OUT_ERROR_CODE      OUT NUMBER,
                                P_OUT_EXCEP_ERR_MSG   OUT VARCHAR2) IS
  
    EXCEPTION_ID ER_STUDENT_SCHED_HISTORY.ER_SS_HISTID%TYPE := 0;
  
  -- get new parameters from history
   v_StateCode          ER_STUDENT_SCHED_HISTORY.STATE_CODE%TYPE;
   v_LastName           ER_STUDENT_SCHED_HISTORY.LASTNAME%TYPE;
   v_Form               ER_STUDENT_SCHED_HISTORY.FORM%TYPE;
   v_Barcode            ER_STUDENT_SCHED_HISTORY.BARCODE%TYPE;
   v_ContentCode        ER_STUDENT_SCHED_HISTORY.CONTENT_AREA_CODE%TYPE;
   v_TestCentreCode     ER_STUDENT_SCHED_HISTORY.Testcentercode%type;
   v_TestCentreName     ER_STUDENT_SCHED_HISTORY.Testcentername%type;
    BEGIN
      DECLARE
        CURSOR c_history IS
          SELECT STATE_CODE, LASTNAME, FORM, BARCODE, CONTENT_AREA_CODE,
          Testcentercode, Testcentername
            FROM ER_STUDENT_SCHED_HISTORY
           where er_ss_histid = P_IN_SS_HISTID;
      BEGIN
        OPEN c_history;
        LOOP
          FETCH c_history
            INTO v_StateCode, v_LastName, v_Form, v_Barcode, v_ContentCode, v_TestCentreCode, v_TestCentreName;
          EXIT WHEN c_history%NOTFOUND;
        END LOOP;
        CLOSE c_history;
      END;
    -- end : new parameters from history
    SELECT SEQ_ER_EXCEPTION_DATA.NEXTVAL INTO EXCEPTION_ID FROM DUAL;
      
    INSERT INTO ER_EXCEPTION_DATA
      (ER_EXCDID,
       SOURCE_SYSTEM,
       ER_UUID,
       ER_SS_HISTID,
       DESCRIPTION,
       EXCEPTION_CODE,
       EXCEPTION_STATUS,
       CREATED_DATE_TIME,
       STATE_CODE,
       LAST_NAME,
       FORM,
       BARCODE,
       CONTENT_CODE,
       TESTING_SITE_CODE,
       TESTING_SITE_NAME)
    VALUES
      (EXCEPTION_ID,
       P_IN_SOURCE_SYSTEM,
       P_IN_UUID,
       P_IN_SS_HISTID,
       REPLACE(P_IN_DESCRIPTION, '~', CHR(10)),
       p_IN_ERROR_CODE,
       P_IN_EXCEPTION_STATUS,
       SYSDATE,
       v_StateCode,
       v_LastName,
       v_Form,
       v_Barcode,
       v_ContentCode,
       v_TestCentreCode,
       v_TestCentreName);
  
    P_OUT_EXCEPTION_ID := EXCEPTION_ID;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      P_OUT_ERROR_CODE    := 0;
    
  END SP_CREATE_EXCEPTION;

  ---- ADD TO STUDENT DEMO
  PROCEDURE SP_CREATE_STUDENT_DEMO(P_IN_UUID                     IN ER_STUDENT_DEMO.UUID%TYPE,
                                   P_IN_STATE_CODE               IN ER_STUDENT_DEMO.STATE_CODE%TYPE,
                                   P_IN_STATENAME                IN ER_STUDENT_DEMO.STATENAME%TYPE,
                                   P_IN_CTB_CUSTOMER_ID          IN ER_STUDENT_DEMO.CTB_CUSTOMER_ID%TYPE,
                                   P_IN_FIRSTNAME                IN ER_STUDENT_DEMO.FIRSTNAME%TYPE,
                                   P_IN_MIDDLENAME               IN ER_STUDENT_DEMO.MIDDLENAME%TYPE,
                                   P_IN_LASTNAME                 IN ER_STUDENT_DEMO.LASTNAME%TYPE,
                                   P_IN_DATEOFBIRTH              IN ER_STUDENT_DEMO.DATEOFBIRTH%TYPE,
                                   P_IN_GENDER                   IN ER_STUDENT_DEMO.GENDER%TYPE,
                                   P_IN_GOVERNMENTID             IN ER_STUDENT_DEMO.GOVERNMENTID%TYPE,
                                   P_IN_GOVERNMENTIDTYPE         IN ER_STUDENT_DEMO.GOVERNMENTIDTYPE%TYPE,
                                   P_IN_ADDRESS1                 IN ER_STUDENT_DEMO.ADDRESS1%TYPE,
                                   P_IN_ADDRESS2                 IN ER_STUDENT_DEMO.ADDRESS2%TYPE,
                                   P_IN_CITY                     IN ER_STUDENT_DEMO.CITY%TYPE,
                                   P_IN_COUNTY                   IN ER_STUDENT_DEMO.COUNTY%TYPE,
                                   P_IN_STATE                    IN ER_STUDENT_DEMO.STATE%TYPE,
                                   P_IN_ZIP                      IN ER_STUDENT_DEMO.ZIP%TYPE,
                                   P_IN_EMAIL                    IN ER_STUDENT_DEMO.EMAIL%TYPE,
                                   P_IN_ALTERNATEEMAIL           IN ER_STUDENT_DEMO.ALTERNATEEMAIL%TYPE,
                                   P_IN_PRIMARYPHONENUMBER       IN ER_STUDENT_DEMO.PRIMARYPHONENUMBER%TYPE,
                                   P_IN_CELLPHONENUMBER          IN ER_STUDENT_DEMO.CELLPHONENUMBER%TYPE,
                                   P_IN_ALTERNATENUMBER          IN ER_STUDENT_DEMO.ALTERNATENUMBER%TYPE,
                                   P_IN_ETHNICITY                IN ER_STUDENT_DEMO.RESOLVED_ETHNICITY_RACE%TYPE,
                                   P_IN_HOMELANGUAGE             IN ER_STUDENT_DEMO.HOMELANGUAGE%TYPE,
                                   P_IN_EDUCATIONLEVEL           IN ER_STUDENT_DEMO.EDUCATIONLEVEL%TYPE,
                                   P_IN_ATTENDCOLLEGE            IN ER_STUDENT_DEMO.ATTENDCOLLEGE%TYPE,
                                   P_IN_CONTACT                  IN ER_STUDENT_DEMO.CONTACT%TYPE,
                                   P_IN_EXAMINEECOUNTYPARISHCODE IN ER_STUDENT_DEMO.EXAMINEECOUNTYPARISHCODE%TYPE,
                                   P_IN_REGISTEREDON             IN ER_STUDENT_DEMO.REGISTEREDON%TYPE,
                                   P_IN_REGISTEREDATTESTCENTER   IN ER_STUDENT_DEMO.REGISTEREDATTESTCENTER%TYPE,
                                   P_IN_REGATTESTCENTERCODE      IN ER_STUDENT_DEMO.REGISTEREDATTESTCENTERCODE%TYPE,
                                   P_IN_REGISTEREDCOUNTYPARISHCD IN ER_STUDENT_DEMO.Regst_TC_CountyParishCode%TYPE,
                                   P_OUT_STUD_ID                 OUT NUMBER,
                                   P_OUT_ERROR_CODE              OUT NUMBER,
                                   P_OUT_EXCEP_ERR_MSG           OUT VARCHAR2) IS
  
    INCOUNT    NUMBER := 0;
    ERSTUDID   NUMBER := 0;
    VALID_DATA NUMBER := 1;
    STUD_COUNT NUMBER := 0;
    STUDSEQID  ER_STUDENT_SCHED_HISTORY.ER_SS_HISTID%TYPE := 0;
  
  BEGIN
    -- MANDATORY DATA VALIDATION
    IF P_IN_UUID IS NULL OR P_IN_UUID = '' THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-101: Required field UUID is blank for student name: ' ||
                             P_IN_LASTNAME || ', ' || P_IN_FIRSTNAME || ' ' ||
                             P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 101;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_LASTNAME IS NULL OR P_IN_LASTNAME = '' THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-108: Required field Last Name is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 108;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_STATE_CODE IS NULL OR P_IN_STATE_CODE = '' THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-102: Required field State Code is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 102;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_GENDER IS NOT NULL AND P_IN_GENDER NOT IN ('M', 'F') THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-136: Gender value is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 136;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_ETHNICITY IS NOT NULL AND
       P_IN_ETHNICITY NOT IN ('1', '2', '3', '4', '5', '6', '7') THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-137: Resolved Ethnicity-Race value is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 137;
      VALID_DATA          := 0;
    END IF;
    if P_IN_DATEOFBIRTH = to_date('1969-12-31', 'YYYY-MM-DD') then
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-139: Date-of-birth is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 139;
      VALID_DATA          := 0;
    end if;
    if to_date(to_char(P_IN_REGISTEREDON, 'YYYY-MM-DD'), 'YYYY-MM-DD') =
       to_date('1969-12-31', 'YYYY-MM-DD') then
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-140: Date registered is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 140;
      VALID_DATA          := 0;
    end if;
  
    if P_IN_HOMELANGUAGE is not null AND
       P_IN_HOMELANGUAGE not in ('01',
                                 '02',
                                 '03',
                                 '04',
                                 '05',
                                 '06',
                                 '07',
                                 '08',
                                 '09',
                                 '10',
                                 '11',
                                 '12',
                                 '13',
                                 '14',
                                 '15',
                                 '16',
                                 '17',
                                 '--') THEN
      /*INCOUNT := 0;
      SELECT COUNT(1)
        INTO INCOUNT
        FROM demographic_values dv, demographic d
       WHERE d.demoid = dv.demoid
         and d.demo_code = 'Home_Language'
         and dv.demo_value_code = P_IN_HOMELANGUAGE;*/
    
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-142: Home language code is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 142;
      VALID_DATA          := 0;
    
    end if;
    IF P_IN_CONTACT IS NOT NULL AND P_IN_CONTACT NOT IN ('Y', 'N') THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-143: Contact value is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 143;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_ATTENDCOLLEGE IS NOT NULL AND
       P_IN_ATTENDCOLLEGE NOT IN ('Y', 'N') THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-144: Attend College value is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 144;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_EDUCATIONLEVEL IS NOT NULL AND
       P_IN_EDUCATIONLEVEL NOT IN ('KG',
                                   '1',
                                   '2',
                                   '3',
                                   '4',
                                   '5',
                                   '6',
                                   '7',
                                   '8',
                                   '9',
                                   '10',
                                   '11',
                                   '12',
                                   '--') THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-145: Education Level value is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                             P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
      P_OUT_ERROR_CODE    := 145;
      VALID_DATA          := 0;
    END IF;
  
    -- END : MANDATORY DATA VALIDATION
  
    IF VALID_DATA = 1 THEN
    
      -- validate state code present in PRISM
      SELECT COUNT(1)
        INTO INCOUNT
        FROM ORG_NODE_DIM
       WHERE ORG_NODE_CODE = P_IN_STATE_CODE
         AND ORG_NODE_LEVEL = 1;
    
      IF INCOUNT = 0 THEN
        P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                               '#ERR-131: Given State Code is invalid for student UUID: ' ||
                               P_IN_UUID || '; name: ' || P_IN_LASTNAME || ', ' ||
                               P_IN_FIRSTNAME || ' ' || P_IN_MIDDLENAME;
        P_OUT_ERROR_CODE    := 131;
      ELSE
      
        -- check if the student record present into PRISM ER BUCKET
        SELECT COUNT(1)
          INTO STUD_COUNT
          FROM ER_STUDENT_DEMO
         WHERE STATE_CODE = P_IN_STATE_CODE
           AND UUID = P_IN_UUID;
      
        IF STUD_COUNT = 0 THEN
        
          -- NEW STUDENT (INSERT CASE)
          SELECT SEQ_ER_STUDENT_DEMO.NEXTVAL INTO STUDSEQID FROM DUAL;
        
          INSERT INTO ER_STUDENT_DEMO
            (er_studid,
             uuid,
             ctb_customer_id,
             state_code,
             statename,
             firstname,
             middlename,
             lastname,
             dateofbirth,
             gender,
             governmentid,
             governmentidtype,
             address1,
             address2,
             city,
             county,
             state,
             zip,
             email,
             alternateemail,
             primaryphonenumber,
             cellphonenumber,
             alternatenumber,
             RESOLVED_ETHNICITY_RACE,
             homelanguage,
             educationlevel,
             attendcollege,
             contact,
             examineecountyparishcode,
             registeredon,
             registeredattestcenter,
             registeredattestcentercode,
             Regst_TC_CountyParishCode,
             created_date_time)
          VALUES
            (STUDSEQID,
             P_IN_UUID,
             P_IN_CTB_CUSTOMER_ID,
             P_IN_STATE_CODE,
             P_IN_STATENAME,
             P_IN_FIRSTNAME,
             P_IN_MIDDLENAME,
             P_IN_LASTNAME,
             P_IN_DATEOFBIRTH,
             P_IN_GENDER,
             P_IN_GOVERNMENTID,
             P_IN_GOVERNMENTIDTYPE,
             P_IN_ADDRESS1,
             P_IN_ADDRESS2,
             P_IN_CITY,
             P_IN_COUNTY,
             P_IN_STATE,
             P_IN_ZIP,
             P_IN_EMAIL,
             P_IN_ALTERNATEEMAIL,
             P_IN_PRIMARYPHONENUMBER,
             P_IN_CELLPHONENUMBER,
             P_IN_ALTERNATENUMBER,
             P_IN_ETHNICITY,
             P_IN_HOMELANGUAGE,
             P_IN_EDUCATIONLEVEL,
             P_IN_ATTENDCOLLEGE,
             P_IN_CONTACT,
             P_IN_EXAMINEECOUNTYPARISHCODE,
             P_IN_REGISTEREDON,
             P_IN_REGISTEREDATTESTCENTER,
             P_IN_REGATTESTCENTERCODE,
             P_IN_REGISTEREDCOUNTYPARISHCD,
             SYSDATE);
        
          P_OUT_STUD_ID := STUDSEQID;
        
        ELSE
        
          -- STUDENT ALREADY PRESENT (UPDATE CASE)
          SELECT er_studid
            INTO ERSTUDID
            FROM ER_STUDENT_DEMO
           WHERE STATE_CODE = P_IN_STATE_CODE
             AND UUID = P_IN_UUID;
        
          UPDATE ER_STUDENT_DEMO
             SET ctb_customer_id            = P_IN_CTB_CUSTOMER_ID,
                 statename                  = P_IN_STATENAME,
                 firstname                  = P_IN_FIRSTNAME,
                 middlename                 = P_IN_MIDDLENAME,
                 lastname                   = P_IN_LASTNAME,
                 dateofbirth                = P_IN_DATEOFBIRTH,
                 gender                     = P_IN_GENDER,
                 governmentid               = P_IN_GOVERNMENTID,
                 governmentidtype           = P_IN_GOVERNMENTIDTYPE,
                 address1                   = P_IN_ADDRESS1,
                 address2                   = P_IN_ADDRESS2,
                 city                       = P_IN_CITY,
                 county                     = P_IN_COUNTY,
                 state                      = P_IN_STATE,
                 zip                        = P_IN_ZIP,
                 email                      = P_IN_EMAIL,
                 alternateemail             = P_IN_ALTERNATEEMAIL,
                 primaryphonenumber         = P_IN_PRIMARYPHONENUMBER,
                 cellphonenumber            = P_IN_CELLPHONENUMBER,
                 alternatenumber            = P_IN_ALTERNATENUMBER,
                 RESOLVED_ETHNICITY_RACE    = P_IN_ETHNICITY,
                 homelanguage               = P_IN_HOMELANGUAGE,
                 educationlevel             = P_IN_EDUCATIONLEVEL,
                 attendcollege              = P_IN_ATTENDCOLLEGE,
                 contact                    = P_IN_CONTACT,
                 examineecountyparishcode   = P_IN_EXAMINEECOUNTYPARISHCODE,
                 registeredon               = P_IN_REGISTEREDON,
                 registeredattestcenter     = P_IN_REGISTEREDATTESTCENTER,
                 registeredattestcentercode = P_IN_REGATTESTCENTERCODE,
                 UPDATED_DATE_TIME          = SYSDATE
           WHERE er_studid = ERSTUDID;
        
          P_OUT_STUD_ID := ERSTUDID;
        
        END IF;
      END IF;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      P_OUT_ERROR_CODE    := 0;
    
  END SP_CREATE_STUDENT_DEMO;

  ---- ADD TO TEST SCHEDULE
  PROCEDURE SP_CREATE_TEST_SCHEDULE(P_IN_UUID                    IN ER_STUDENT_DEMO.UUID%TYPE,
                                    P_IN_STUDENT_NAME            IN VARCHAR2,
                                    P_IN_STUDID                  IN ER_TEST_SCHEDULE.ER_STUDID%TYPE,
                                    P_IN_SCHEDULEID              IN ER_TEST_SCHEDULE.SCHEDULE_ID%TYPE,
                                    P_IN_DATE_SCHEDULED          IN ER_TEST_SCHEDULE.DATE_SCHEDULED%TYPE,
                                    P_IN_TIMEOFDAY               IN ER_TEST_SCHEDULE.TIMEOFDAY%TYPE,
                                    P_IN_DATECHECKEDIN           IN ER_TEST_SCHEDULE.DATECHECKEDIN%TYPE,
                                    P_IN_CONTENT_AREA_CODE       IN ER_TEST_SCHEDULE.CONTENT_AREA_CODE%TYPE,
                                    P_IN_CONTENT_TEST_TYPE       IN ER_TEST_SCHEDULE.CONTENT_TEST_TYPE%TYPE,
                                    P_IN_CONTENT_TEST_CODE       IN ER_TEST_SCHEDULE.CONTENT_TEST_CODE%TYPE,
                                    P_IN_BARCODE                 IN ER_TEST_SCHEDULE.BARCODE%TYPE,
                                    P_IN_FORM                    IN ER_TEST_SCHEDULE.FORM%TYPE,
                                    P_IN_TASCREADINESS           IN ER_TEST_SCHEDULE.TASCREADINESS%TYPE,
                                    P_IN_ECC                     IN ER_TEST_SCHEDULE.ECC%TYPE,
                                    P_IN_TESTCENTERCODE          IN ER_TEST_SCHEDULE.TESTCENTERCODE%TYPE,
                                    P_IN_TESTCENTERNAME          IN ER_TEST_SCHEDULE.TESTCENTERNAME%TYPE,
                                    P_IN_SCHEDULEDCOUNTYPARISHCD IN ER_TEST_SCHEDULE.Sched_TC_CountyParishCode%TYPE,
                                    P_IN_STATE_CODE              IN ER_STUDENT_DEMO.STATE_CODE%TYPE,
                                    P_OUT_SCHEDULE_ID            OUT NUMBER,
                                    P_OUT_ERROR_CODE             OUT NUMBER,
                                    P_OUT_EXCEP_ERR_MSG          OUT VARCHAR2) IS
  
    SCHEDULESEQID   ER_STUDENT_SCHED_HISTORY.ER_SS_HISTID%TYPE := 0;
    TESTCODE        NUMBER := 0;
    schedid         NUMBER := 0;
    SCH_COUNT       NUMBER := 0;
    VALID_DATA      NUMBER := 1;
    test_code_COUNT NUMBER := 0;
    countt          number := 0;
  
  BEGIN
    -- MANDATORY DATA VALIDATION
    IF P_IN_CONTENT_AREA_CODE IS NULL OR P_IN_CONTENT_AREA_CODE = '' OR
       P_IN_CONTENT_AREA_CODE = 0 THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-105: Required field CONTENT AREA CODE is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 105;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_CONTENT_TEST_TYPE IS NULL OR P_IN_CONTENT_TEST_TYPE = '' THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-107: Required field CONTENT TEST TYPE is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 107;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_CONTENT_TEST_CODE IS NULL OR P_IN_CONTENT_TEST_CODE = '' OR
       P_IN_CONTENT_TEST_CODE = -99 THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-106: Required field CONTENT TEST CODE is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 106;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_SCHEDULEID IS NULL OR P_IN_SCHEDULEID = '' OR
       P_IN_SCHEDULEID = -99 THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-103: Required field SCHEDULE ID is blank OR invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 103;
      VALID_DATA          := 0;
    END IF;
    IF P_IN_DATE_SCHEDULED IS NULL OR P_IN_DATE_SCHEDULED = '' THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-104: Required field DATE SCHEDULED is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 104;
      VALID_DATA          := 0;
    
    ELSE
      if P_IN_DATE_SCHEDULED = to_date('1969-12-31', 'YYYY-MM-DD') then
        P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                               '#ERR-132: Required field DATE SCHEDULED is invalid for student UUID: ' ||
                               P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
        P_OUT_ERROR_CODE    := 132;
        VALID_DATA          := 0;
      end if;
    END IF;
    IF (P_IN_CONTENT_TEST_TYPE = '1' /*OR P_IN_CONTENT_TEST_TYPE = 1*/) AND
       (P_IN_BARCODE IS NULL OR P_IN_BARCODE = '') THEN
      -- TEST TYPE IS PAPER BASED AND BARCODE IS NULL
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-198: Required field BARCODE is blank for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 198;
      VALID_DATA          := 0;
    END IF;
    if to_date(to_char(P_IN_DATECHECKEDIN, 'YYYY-MM-DD'), 'YYYY-MM-DD') =
       to_date('1969-12-31', 'YYYY-MM-DD') then
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-138: DATE CHECKED-IN is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 138;
      VALID_DATA          := 0;
    end if;
    if to_date(to_char(P_IN_DATE_SCHEDULED, 'YYYY-MM-DD'), 'YYYY-MM-DD') =
       to_date('1969-12-31', 'YYYY-MM-DD') then
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-141: Date scheduled is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 141;
      VALID_DATA          := 0;
    end if;
    IF P_IN_CONTENT_TEST_TYPE IS NOT NULL AND
       P_IN_CONTENT_TEST_TYPE NOT IN ('0', '1') THEN
      P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                             '#ERR-135: CONTENT TEST TYPE is invalid for student UUID: ' ||
                             P_IN_UUID || '; name: ' || P_IN_STUDENT_NAME;
      P_OUT_ERROR_CODE    := 135;
      VALID_DATA          := 0;
    END IF;
    -- END : MANDATORY DATA VALIDATION
  
    IF VALID_DATA = 1 THEN
    
      -- CHECK CONTENT TEST CODE (INITIAL / RETEST)
      /*SELECT content_test_code
       INTO TESTCODE
       FROM ER_TEST_SCHEDULE
      WHERE content_area_code = P_IN_CONTENT_AREA_CODE
        AND content_test_type = P_IN_CONTENT_TEST_TYPE;*/
    
      SELECT COUNT(1)
        INTO SCH_COUNT
        FROM ER_TEST_SCHEDULE
       WHERE content_area_code = P_IN_CONTENT_AREA_CODE
         AND content_test_code = P_IN_CONTENT_TEST_CODE
         AND ER_STUDID = P_IN_STUDID;
    
      IF SCH_COUNT = 0 THEN
        -- NEW SCHEDULE (INSERT CASE)
        -- check for valid content code
      
        SELECT COUNT(1)
          INTO countt
          FROM SUBTEST_DIM
         WHERE subtest_code = to_char(P_IN_CONTENT_AREA_CODE)
           AND subtest_type = 'S';
      
        IF countt = 0 THEN
          -- RAISE ERROR 
          P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                                 '#ERR-133: CONTENT AREA CODE is invalid for student UUID: ' ||
                                 P_IN_UUID || '; name: ' ||
                                 P_IN_STUDENT_NAME;
          P_OUT_ERROR_CODE    := 133;
          VALID_DATA          := 0;
        END IF;
      
        countt := 0;
        SELECT COUNT(1)
          INTO countt
          FROM ER_TEST_SCHEDULE
         WHERE content_area_code = P_IN_CONTENT_AREA_CODE
           AND pp_oas_linkedid IS NULL
           AND ER_STUDID = P_IN_STUDID;
      
        IF countt > 0 THEN
          P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                                 '#ERR-197: Multiple test open for CONTENT AREA CODE: ' ||
                                 P_IN_CONTENT_AREA_CODE ||
                                 ' for student UUID: ' || P_IN_UUID ||
                                 '; name: ' || P_IN_STUDENT_NAME;
          P_OUT_ERROR_CODE    := 197;
          VALID_DATA          := 0;
        END IF;
      
        countt := 0;
        SELECT COUNT(1)
          INTO countt
          FROM ER_TEST_SCHEDULE test, ER_STUDENT_DEMO demo
         WHERE test.ER_STUDID = demo.ER_STUDID
           AND test.ER_STUDID = P_IN_STUDID
           and demo.state_code = P_IN_STATE_CODE
           and schedule_id = P_IN_SCHEDULEID;
      
        IF countt = 1 THEN
          -- duplicate schedule
          P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                                 '#ERR-152: Found duplicate schedule id for student UUID: ' ||
                                 P_IN_UUID || '; name: ' ||
                                 P_IN_STUDENT_NAME ||
                                 '; content area code: ' ||
                                 P_IN_CONTENT_AREA_CODE;
          P_OUT_ERROR_CODE    := 152;
          VALID_DATA          := 0;
        END IF;
      
        IF VALID_DATA = 1 THEN
          SELECT SEQ_ER_TEST_SCHEDULE.NEXTVAL INTO SCHEDULESEQID FROM DUAL;
        
          INSERT INTO ER_TEST_SCHEDULE
            (er_TEST_schedid,
             er_studid,
             schedule_id,
             date_scheduled,
             timeofday,
             datecheckedin,
             content_area_code,
             content_test_type,
             content_test_code,
             barcode,
             form,
             tascreadiness,
             ecc,
             testcentercode,
             testcentername,
             Sched_TC_CountyParishCode,
             created_date_time)
          VALUES
            (SCHEDULESEQID,
             P_IN_STUDID,
             P_IN_SCHEDULEID,
             P_IN_DATE_SCHEDULED,
             P_IN_TIMEOFDAY,
             P_IN_DATECHECKEDIN,
             P_IN_CONTENT_AREA_CODE,
             P_IN_CONTENT_TEST_TYPE,
             P_IN_CONTENT_TEST_CODE,
             P_IN_BARCODE,
             P_IN_FORM,
             P_IN_TASCREADINESS,
             P_IN_ECC,
             P_IN_TESTCENTERCODE,
             P_IN_TESTCENTERNAME,
             P_IN_SCHEDULEDCOUNTYPARISHCD,
             SYSDATE);
        
          P_OUT_SCHEDULE_ID := SCHEDULESEQID;
        END IF;
      ELSE
        
        -- UPDATE SCHEDULE 
        --check if schedule is locked
        countt := 0;
        SELECT COUNT(1)
          INTO countt
          FROM ER_TEST_SCHEDULE
         WHERE content_area_code = P_IN_CONTENT_AREA_CODE
           AND content_test_code = P_IN_CONTENT_TEST_CODE
           AND ER_STUDID = P_IN_STUDID
           and PP_OAS_LINKEDID is not null;
      
        if countt = 1 then
          -- raise locked error
          P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                                 '#ERR-199: Data is locked for update for student UUID: ' ||
                                 P_IN_UUID || '; name: ' ||
                                 P_IN_STUDENT_NAME ||
                                 '; content area code: ' ||
                                 P_IN_CONTENT_AREA_CODE;
          P_OUT_ERROR_CODE    := 199;
          VALID_DATA          := 0;
        end if;
      
        -- check for schedule id
        countt := 0;
        SELECT COUNT(1)
          INTO countt
          FROM ER_TEST_SCHEDULE
         WHERE content_area_code not in (P_IN_CONTENT_AREA_CODE)
           AND content_test_code = P_IN_CONTENT_TEST_CODE
           AND ER_STUDID = P_IN_STUDID
           and schedule_id = P_IN_SCHEDULEID;
      
        IF countt = 1 THEN
          -- duplicate schedule
          P_OUT_EXCEP_ERR_MSG := P_OUT_EXCEP_ERR_MSG ||
                                 '#ERR-152: Found duplicate schedule id for student UUID: ' ||
                                 P_IN_UUID || '; name: ' ||
                                 P_IN_STUDENT_NAME ||
                                 '; content area code: ' ||
                                 P_IN_CONTENT_AREA_CODE;
          P_OUT_ERROR_CODE    := 152;
          VALID_DATA          := 0;
        END IF;
      
        IF VALID_DATA = 1 THEN
          SELECT er_TEST_schedid
            INTO schedid
            FROM ER_TEST_SCHEDULE
           WHERE content_area_code = P_IN_CONTENT_AREA_CODE
             AND content_test_code = P_IN_CONTENT_TEST_CODE
             AND ER_STUDID = P_IN_STUDID;
        
          UPDATE ER_TEST_SCHEDULE
             SET schedule_id               = P_IN_SCHEDULEID,
                 date_scheduled            = P_IN_DATE_SCHEDULED,
                 timeofday                 = P_IN_TIMEOFDAY,
                 datecheckedin             = P_IN_DATECHECKEDIN,
                 barcode                   = P_IN_BARCODE,
                 form                      = P_IN_FORM,
                 tascreadiness             = P_IN_TASCREADINESS,
                 ecc                       = P_IN_ECC,
                 testcentercode            = P_IN_TESTCENTERCODE,
                 testcentername            = P_IN_TESTCENTERNAME,
                 Sched_TC_CountyParishCode = P_IN_SCHEDULEDCOUNTYPARISHCD,
                 CONTENT_TEST_TYPE         = P_IN_CONTENT_TEST_TYPE,
                 UPDATED_DATE_TIME         = SYSDATE
           WHERE er_TEST_schedid = schedid;
        END IF;
      END IF;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      P_OUT_EXCEP_ERR_MSG := UPPER(SUBSTR(SQLERRM, 0, 255));
      P_OUT_ERROR_CODE    := 0;
    
  END SP_CREATE_TEST_SCHEDULE;

END PKG_ER_MODULE;
/
