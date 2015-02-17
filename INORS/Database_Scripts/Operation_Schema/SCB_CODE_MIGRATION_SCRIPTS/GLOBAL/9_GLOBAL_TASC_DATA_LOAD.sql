--This script needs to be executed from the GlobalPrism schema --- 

--PROJECT_DIM
insert into PROJECT_DIM (PROJECTID, PROJECT_NAME, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (1, 'TASC', SYSDATE, SYSDATE);

commit;

--ADMIN_DIM

--Swap key load

INSERT INTO TASC_LOOKUP_DATA_SWAP(PRESENT_VALUE_NUM,SWAPPED_VALUE_NUM,COLUMN_TYPE) values(103,100,'ADMIN_ID');

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
        INSERT /*+ append */
        INTO ADMIN_DIM
          (ADMINID,
           ADMIN_NAME,
           ADMIN_SEASON,
           ADMIN_YEAR,
           IS_CURRENT_ADMIN,
           FILE_LOCATION,
           ADMIN_SEQ,
           PROJECTID,
           DATETIMESTAMP)
          select nvl((select SWAPPED_VALUE_NUM
                       from TASC_LOOKUP_DATA_SWAP
                      where COLUMN_TYPE = ''ADMIN_ID''
                        AND PRESENT_VALUE_NUM = ADMINID),
                     ADMINID) ADMINID,
                 ADMIN_NAME,
                 ADMIN_SEASON,
                 ADMIN_YEAR,
                 IS_CURRENT_ADMIN,
                 FILE_LOCATION,
                 ADMIN_SEQ,
                 1 PROJECTID,
                 DATETIMESTAMP
            from ' || V_SCHEMA_NAME || ' .ADMIN_DIM ';

  --commit;
end;

--PRODUCT

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
        INSERT /*+ append */
        INTO PRODUCT
          (PRODUCTID,
           PRODUCT_NAME,
           PRODUCT_TYPE,
           PRODUCT_SEQ,
           PRODUCT_CODE,
           IS_IC_REQUIRED,
           IS_EDITABLE,
           FILE_LOCATION,
           PROJECTID,
           DATETIMESTAMP)
          select PRODUCTID,
                 PRODUCT_NAME,
                 PRODUCT_TYPE,
                 PRODUCT_SEQ,
                 PRODUCT_CODE,
                 ''N'' IS_IC_REQUIRED,
                 ''Y'' IS_EDITABLE,
                 NULL FILE_LOCATION,
                 1 PROJECTID,
                 DATETIMESTAMP
            from ' || V_SCHEMA_NAME || ' .PRODUCT ';

  --commit;
end;

--ASSESSMENT_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
            INSERT /*+ append */
            INTO ASSESSMENT_DIM
            (ASSESSMENTID,
            ASSESSMENT_NAME,
            ASSESSMENT_TYPE,
            ASSESSMENT_CODE,
            PRODUCTID,
            DATETIMESTAMP)
            select ASSESSMENTID,
            ASSESSMENT_NAME,
            ASSESSMENT_TYPE,
            ASSESSMENT_CODE,
            PRODUCTID,
            DATETIMESTAMP
            from ' || V_SCHEMA_NAME ||
            ' .ASSESSMENT_DIM ';

  --commit;
end;

--FORM_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO FORM_DIM
        (FORMID, FORM_NAME, FORM_CODE, PROJECTID, DATETIMESTAMP)
        select FORMID, FORM_NAME, FORM_CODE, 1 PROJECTID, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .FORM_DIM ';

  --commit;
end;

--GENDER_DIM

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select GENDERID,
             (select max(GENDERID) from GENDER_DIM) + rownum,
             ''GENDER_ID''
        from ' || V_SCHEMA_NAME || '.GENDER_DIM';

end;



declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO GENDER_DIM
        (GENDERID, GENDER_NAME, GENDER_CODE, PROJECTID, DATETIMESTAMP)
        select (nvl((select SWAPPED_VALUE_NUM
                      from TASC_LOOKUP_DATA_SWAP
                     where COLUMN_TYPE = ''GENDER_ID''
                       AND PRESENT_VALUE_NUM = GENDERID),
                    GENDERID)) GENDERID,
               GENDER_NAME,
               GENDER_CODE,
               1 PROJECTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .GENDER_DIM ';

  --commit;
end;

--CONTENT_DIM

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select CONTENTID,
             (select max(CONTENTID) from CONTENT_DIM) + rownum,
             ''CONTENT_ID''
        from ' || V_SCHEMA_NAME || '.CONTENT_DIM';

end;


declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO CONTENT_DIM
        (CONTENTID, CONTENT_NAME, CONTENT_SEQ, ASSESSMENTID, DATETIMESTAMP)
        select nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = ''CONTENT_ID''
                      AND PRESENT_VALUE_NUM = CONTENTID),
                   CONTENTID) CONTENTID,
               CONTENT_NAME,
               CONTENT_SEQ,
               ASSESSMENTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .CONTENT_DIM ';

  --commit;
end;

--CUSTOMER_INFO

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select CUSTOMERID,
             (select max(CUSTOMERID) from CUSTOMER_INFO) + rownum,
             ''CUSTOMER_ID''
        from ' || V_SCHEMA_NAME || '.CUSTOMER_INFO';

end;


declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO CUSTOMER_INFO
        (CUSTOMERID,
         CUSTOMER_NAME,
         DISPLAY_TP_SELECTION,
         FILE_LOCATION,
         SUPPORT_EMAIL,
         SEND_LOGIN_PDF,
         CUSTOMER_CODE,
         PROJECTID,
         DATETIMESTAMP)
        select nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = ''CUSTOMER_ID''
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,
               CUSTOMER_NAME,
               DISPLAY_TP_SELECTION,
               FILE_LOCATION,
               SUPPORT_EMAIL,
               SEND_LOGIN_PDF,
               CUSTOMER_CODE,
               1 PROJECTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .CUSTOMER_INFO ';

  --commit;
end;


--CUST_PRODUCT_LINK

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
    INSERT /*+ append */
    INTO CUST_PRODUCT_LINK
      (CUST_PROD_ID,
       CUSTOMERID,
       PRODUCTID,
       ADMINID,
       ACTIVATION_STATUS,
       DATETIMESTAMP)
      select CUST_PROD_ID,
             nvl((select SWAPPED_VALUE_NUM
                   from TASC_LOOKUP_DATA_SWAP
                  where COLUMN_TYPE = ''CUSTOMER_ID''
                    AND PRESENT_VALUE_NUM = CUSTOMERID),
                 CUSTOMERID) CUSTOMERID,
             PRODUCTID,
             nvl((select SWAPPED_VALUE_NUM
                    from TASC_LOOKUP_DATA_SWAP
                   where COLUMN_TYPE = ''ADMIN_ID''
                     AND PRESENT_VALUE_NUM = ADMINID), ADMINID) ADMINID,
             ACTIVATION_STATUS,
             DATETIMESTAMP
        from ' || V_SCHEMA_NAME || ' .CUST_PRODUCT_LINK ';

  --commit;
end;

--GRADE_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO GRADE_DIM
        (GRADEID, GRADE_NAME, GRADE_SEQ, GRADE_CODE, PROJECTID, DATETIMESTAMP)
        select GRADEID,
               GRADE_NAME,
               GRADE_SEQ,
               GRADE_CODE,
               1 PROJECTID,
               DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .GRADE_DIM ';

  --commit;
end;

--LEVEL_DIM

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO LEVEL_DIM
        (LEVELID, LEVEL_NAME, LEVEL_CODE, PROJECTID, DATETIMESTAMP)
        select LEVELID, LEVEL_NAME, LEVEL_CODE, 1 PROJECTID, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .LEVEL_DIM ';

  --commit;
end;

--LEVEL_MAP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
    INSERT /*+ append */
    INTO LEVEL_MAP
      (LEVEL_MAPID, LEVELID, FORMID, ASSESSMENTID, DATETIMESTAMP)
      select LEVEL_MAPID, LEVELID, FORMID, ASSESSMENTID, DATETIMESTAMP
        from ' || V_SCHEMA_NAME || ' .LEVEL_MAP ';

  --commit;
end;

--GRADE_LEVEL_MAP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
      INSERT /*+ append */
      INTO GRADE_LEVEL_MAP
        (GRADEID, LEVEL_MAPID, ONLEVEL_FLAG, DATETIMESTAMP)
        select GRADEID, LEVEL_MAPID, ONLEVEL_FLAG, DATETIMESTAMP
          from ' || V_SCHEMA_NAME || ' .GRADE_LEVEL_MAP ';

  --commit;
end;

--ROLE(Same for TASC and ISTEP)

--SUBTEST_DIM

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select SUBTESTID,
             (select max(SUBTESTID) from SUBTEST_DIM) + rownum,
             ''SUBTEST_ID''
        from (select SUBTESTID
                 from ' || V_SCHEMA_NAME || ' .SUBTEST_DIM
                order by SUBTESTID) ';

end;


declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
            INSERT /*+ append */
            INTO SUBTEST_DIM
              (SUBTESTID,
               SUBTEST_NAME,
               SUBTEST_SEQ,
               SUBTEST_CODE,
               SUBTEST_TYPE,
               CONTENTID,
			   CANDIDATE_SUB_SEQ, 
               DATETIMESTAMP)
              select nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''SUBTEST_ID''
                            AND PRESENT_VALUE_NUM = SUBTESTID),
                         SUBTESTID) SUBTESTID,
                     SUBTEST_NAME,
                     SUBTEST_SEQ,
                     SUBTEST_CODE,
                     SUBTEST_TYPE,
                     nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''CONTENT_ID''
                            AND PRESENT_VALUE_NUM = CONTENTID),
                         CONTENTID) CONTENTID,
					 DECODE (SUBTEST_CODE,1,1,2,5,3,6,4,2,5,4,6,3,7) AS CANDIDATE_SUB_SEQ,	 
                     DATETIMESTAMP
                from ' || V_SCHEMA_NAME || ' .SUBTEST_DIM ';

  --commit;
end;

--OBJECTIVE_DIM

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select OBJECTIVEID,
             (select max(OBJECTIVEID) from OBJECTIVE_DIM) + rownum,
             ''OBJECTIVE_ID''
        from (select OBJECTIVEID
                 from ' || V_SCHEMA_NAME || ' .OBJECTIVE_DIM
                order by OBJECTIVEID) ';

end;


declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
          INSERT /*+ append */
          INTO OBJECTIVE_DIM
            (OBJECTIVEID,
             OBJECTIVE_NAME,
             OBJECTIVE_SEQ,
             OBJECTIVE_TYPE,
             OBJECTIVE_CODE,
             OBJECTIVE_DESC,
             PROJECTID,
             DATETIMESTAMP)
            select nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''OBJECTIVE_ID''
                            AND PRESENT_VALUE_NUM = OBJECTIVEID),
                         OBJECTIVEID) OBJECTIVEID,
                   OBJECTIVE_NAME,
                   OBJECTIVE_SEQ,
                   OBJECTIVE_TYPE,
                   OBJECTIVE_CODE,
                   NULL OBJECTIVE_DESC,
                   1 PROJECTID,
                   DATETIMESTAMP
              from ' || V_SCHEMA_NAME || ' .OBJECTIVE_DIM ';

  --commit;
end;

--SUBTEST_OBJECTIVE_MAP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
              INSERT /*+ append */
              INTO SUBTEST_OBJECTIVE_MAP
                (SUBT_OBJ_MAPID,
                 SUBTESTID,
                 OBJECTIVEID,
                 LEVEL_MAPID,
                 ASSESSMENTID,
                 DATETIMESTAMP)
                select SUBT_OBJ_MAPID,
                       nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''SUBTEST_ID''
                            AND PRESENT_VALUE_NUM = SUBTESTID),
                         SUBTESTID) SUBTESTID,
                       nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''OBJECTIVE_ID''
                            AND PRESENT_VALUE_NUM = OBJECTIVEID),
                         OBJECTIVEID) OBJECTIVEID,
                       LEVEL_MAPID,
                       ASSESSMENTID,
                       DATETIMESTAMP
                  from ' || V_SCHEMA_NAME || ' .SUBTEST_OBJECTIVE_MAP ';

  --commit;
end;


--ITEMSET_DIM

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select ITEMSETID,
             (select max(ITEMSETID) from ITEMSET_DIM) + rownum,
             ''ITEMSET_ID''
        from (select ITEMSETID
                 from ' || V_SCHEMA_NAME || ' .ITEMSET_DIM
                order by ITEMSETID) ';

end;



declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
              INSERT /*+ append */
              INTO ITEMSET_DIM
                (ITEMSETID,
                 ITEM_NAME,
                 ITEM_CODE,
                 SESSION_ID,
                 ITEM_SEQ,
                 POINT_POSSIBLE,
                 ITEM_TYPE,
                 PDF_FILENAME,
                 SUBT_OBJ_MAPID,
                 SUBTESTID,
                 --OBJECTIVEID,
                 ITEM_NUMBER,
                 ITEM_PART,
                 MODEULEID,
                 PROJECTID,
                 DATETIMESTAMP)
                select nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''ITEMSET_ID''
                            AND PRESENT_VALUE_NUM = ITEMSETID),
                         ITEMSETID) ITEMSETID,
                       ITEM_NAME,
                       ITEM_CODE,
                       NULL SESSION_ID,
                       ITEM_SEQ,
                       NULL POINT_POSSIBLE,
                       ITEM_TYPE,
                       NULL PDF_FILENAME,
                       NULL SUBT_OBJ_MAPID,
                       nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''SUBTEST_ID''
                            AND PRESENT_VALUE_NUM = SUBTESTID),
                         SUBTESTID) SUBTESTID,
                       /*nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''OBJECTIVE_ID''
                            AND PRESENT_VALUE_NUM = OBJECTIVEID),
                         OBJECTIVEID) OBJECTIVEID,*/
                       NULL ITEM_NUMBER,
                       NULL ITEM_PART,
                       NULL MODEULEID,
                       1 PROJECTID,
                       DATETIMESTAMP
                  from ' || V_SCHEMA_NAME || ' .ITEMSET_DIM ';

  --commit;
end;

--TEST_PROGRAM

-- Swap key load

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE '
    insert into TASC_LOOKUP_DATA_SWAP
      (PRESENT_VALUE_NUM, SWAPPED_VALUE_NUM, COLUMN_TYPE)
      select TP_ID,
             (select max(TP_ID) from TEST_PROGRAM) + rownum,
             ''TP_ID''
        from (select TP_ID
                 from ' || V_SCHEMA_NAME || ' .TEST_PROGRAM
                order by TP_ID) ';

end;


declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
            INSERT /*+ append */
            INTO TEST_PROGRAM
              (TP_ID,
               TP_CODE,
               TP_NAME,
               TP_TYPE,
               NUM_LEVELS,
               TP_MODE,
               CUSTOMERID,
               ADMINID,
               ACTIVATION_STATUS,
               DATETIMESTAMP)
              select nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''TP_ID''
                            AND PRESENT_VALUE_NUM = TP_ID),
                         TP_ID) TP_ID,
                     TP_CODE,
                     TP_NAME,
                     TP_TYPE,
                     NUM_LEVELS,
                     TP_MODE,
                     nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''CUSTOMER_ID''
                            AND PRESENT_VALUE_NUM = CUSTOMERID),
                         CUSTOMERID) CUSTOMERID,
                     nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''ADMIN_ID''
                            AND PRESENT_VALUE_NUM = ADMINID),
                         ADMINID) ADMINID,
                     ACTIVATION_STATUS,
                     DATETIMESTAMP
                from ' || V_SCHEMA_NAME || ' .TEST_PROGRAM ';

  --commit;
end;

--ORG_TP_STRUCTURE

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
            INSERT /*+ append */
            INTO ORG_TP_STRUCTURE
              (TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP)
              select nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''TP_ID''
                            AND PRESENT_VALUE_NUM = TP_ID),
                         TP_ID) TP_ID, ORG_LEVEL, ORG_LABEL, DATETIMESTAMP
                from ' || V_SCHEMA_NAME || ' .ORG_TP_STRUCTURE ';

  --commit;
end;

--ORG_USER_DEFINE_LOOKUP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
            INSERT /*+ append */
            INTO ORG_USER_DEFINE_LOOKUP
              (ROLEID,
               ORG_NODE_LEVEL,
               USER_SEQ,
               USER_NAME,
               USER_PASSWORD,
               CUSTOMERID,
               DATETIMESTAMP)
              select ROLEID,
                     ORG_NODE_LEVEL,
                     USER_SEQ,
                     USER_NAME,
                     USER_PASSWORD,
                     nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''CUSTOMER_ID''
                            AND PRESENT_VALUE_NUM = CUSTOMERID),
                         CUSTOMERID) CUSTOMERID,
                     DATETIMESTAMP
                from ' || V_SCHEMA_NAME || ' .ORG_USER_DEFINE_LOOKUP ';

  --commit;
end;

--NP_MEAN_NCE_LOOKUP

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
          INSERT /*+ append */
          INTO NP_MEAN_NCE_LOOKUP
            (NMN_LOOKUPID, NCE, NP, CUST_PROD_ID, DATETIMESTAMP)
            select NMN_LOOKUPID, NCE, NP, CUST_PROD_ID, DATETIMESTAMP
              from ' || V_SCHEMA_NAME || ' .NP_MEAN_NCE_LOOKUP ';

  --commit;
end;

--DASH_MENUS

insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (101, 'Reports', 'CUSTOM', 1, 'Reports', 1, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (103, 'Resources', 'CUSTOM', 4, 'Resources', 1, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (102, 'Downloads', 'CUSTOM', 3, 'Downloads', 1, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (104, 'Useful Links', 'CUSTOM', 5, 'Useful Links', 1, SYSDATE, SYSDATE);
insert into DASH_MENUS (DB_MENUID, MENU_NAME, MENU_TYPE, MENU_SEQ, DESCRIPTION, PROJECTID, CREATED_DATE_TIME, UPDATED_DATE_TIME)
values (105, 'Manage', 'CUSTOM', 2, 'Manage', 1, SYSDATE, SYSDATE);
--commit;

--DASH_REPORTS

-- Swap key load

INSERT INTO TASC_LOOKUP_DATA_SWAP(PRESENT_VALUE_NUM,SWAPPED_VALUE_NUM,COLUMN_TYPE) values(1022,5,'DB_REPORTID');
--commit;

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
            INSERT /*+ append */
            INTO DASH_REPORTS
              (DB_REPORTID,
               REPORT_NAME,
               REPORT_DESC,
               REPORT_TYPE,
               REPORT_FOLDER_URI,
               ACTIVATION_STATUS,
               PROJECTID,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME)
              select nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''DB_REPORTID''
                            AND PRESENT_VALUE_NUM = DB_REPORTID),
                         DB_REPORTID) DB_REPORTID,
                     REPORT_NAME,
                     REPORT_DESC,
                     REPORT_TYPE,
                     REPORT_FOLDER_URI,
                     ACTIVATION_STATUS,
                     1 PROJECTID,
                     CREATED_DATE_TIME,
                     UPDATED_DATE_TIME
                from ' || V_SCHEMA_NAME || ' .DASH_REPORTS ';

  --commit;
end;

--DASH_MENU_RPT_ACCESS

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
                INSERT /*+ append */
                INTO DASH_MENU_RPT_ACCESS
                  (DB_MENUID,
                   DB_REPORTID,
                   ROLEID,
                   ORG_LEVEL,
                   CUST_PROD_ID,
                   REPORT_SEQ,
                   PROJECTID,
                   ACTIVATION_STATUS,
                   CREATED_DATE_TIME,
                   UPDATED_DATE_TIME)
                  select DB_MENUID,
                         nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''DB_REPORTID''
                            AND PRESENT_VALUE_NUM = DB_REPORTID),
                         DB_REPORTID) DB_REPORTID,
                         ROLEID,
                         ORG_LEVEL,
                         CUST_PROD_ID,
                         REPORT_SEQ,
                         1 PROJECTID,
                         ACTIVATION_STATUS,
                         CREATED_DATE_TIME,
                         UPDATED_DATE_TIME
                    from ' || V_SCHEMA_NAME || ' .DASH_MENU_RPT_ACCESS ';

  --commit;
end;


--DASH_CONTRACT_PROP

insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'orglvl.user.not.added', '0', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'orglvl.admin.not.added', '4', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'aws.inors.cacheS3', 'Cache_Keys/TASC/', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'hmac.secret.key', 'WPZguVF49hXaRuZfe9L29ItsC2I', 'OAS|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'hmac.secret.key', 'ETCguRF49hEaRuZguVF49hXrc1', 'eResource|tasc', 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'role.not.added', 'ROLE_CTB,ROLE_PARENT,ROLE_SUPER,ROLE_EDU_ADMIN', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'password.history.day', '3', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'title.tab.home.application', 'TASC Dataonline', null, 1);
insert into DASH_CONTRACT_PROP (DB_PROPERTYID, DB_PROPERTY_NAME, DB_PROPERY_VALUE, SSO_SOURCE, PROJECTID)
values (seq_dash_contract_prop.nextval, 'title.tab.application', 'TASC-Login', null, 1);
insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,DB_PROPERTY_NAME,DB_PROPERY_VALUE,SSO_SOURCE,PROJECTID)
values
  (seq_dash_contract_prop.nextval,'password.expiry','90',null,1);
   
insert into DASH_CONTRACT_PROP
  (DB_PROPERTYID,DB_PROPERTY_NAME,DB_PROPERY_VALUE,SSO_SOURCE,PROJECTID)
values
  (seq_dash_contract_prop.nextval,'password.expiry.warning','85',null,1); 
insert into dash_contract_prop(db_propertyid,db_property_name,db_propery_value,projectid)
values (seq_dash_contract_prop.nextval,'static.pdf.location','TASCREPORTS/qa',1);  
--commit;

--CONDITION_CODES

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
                  INSERT /*+ append */
                  INTO CONDITION_CODES
                    (CONDCODE_ID,
                     SUBTESTID,
                     COND_CODE,
                     COND_CODE_NAME,
                     CREATED_DATE_TIME,
                     UPDATED_DATE_TIME)
                    select CONDCODE_ID,
                           nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = ''SUBTEST_ID''
                            AND PRESENT_VALUE_NUM = SUBTESTID),
                         SUBTESTID) SUBTESTID,
                           COND_CODE,
                           COND_CODE_NAME,
                           CREATED_DATE_TIME,
                           UPDATED_DATE_TIME
                      from ' || V_SCHEMA_NAME || ' .CONDITION_CODES ';

  --commit;
end;

--SCORE_TYPE_LOOKUP 

alter trigger TRG_SCORE_LKPID disable;

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
          INSERT /*+ append */
          INTO SCORE_TYPE_LOOKUP
            (SCORE_TYPEID,
             CATEGORY,
             SCORE_TYPE,
             SCORE_VALUE,
             SCORE_VALUE_NAME,
             CUST_PROD_ID,
             CREATED_DATE_TIME,
             UPDATED_DATE_TIME)
            select SCORE_TYPEID,
                   CATEGORY,
                   SCORE_TYPE,
                   SCORE_VALUE,
                   SCORE_VALUE_NAME,
                   CUST_PROD_ID,
                   CREATED_DATE_TIME,
                   UPDATED_DATE_TIME
              from ' || V_SCHEMA_NAME || ' .SCORE_TYPE_LOOKUP ';

  --commit;
end;

alter trigger TRG_SCORE_LKPID enable;

--FTP_CONFIG 

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE ' 
INSERT /*+APPEND*/
INTO FTP_CONFIG
  (FTPID,
   CUSTOMERID,
   FTP_NAME,
   HOST_NAME,
   FILE_PROTOCOL,
   USER_NAME,
   PASSWORD,
   FTP_LOCATION,
   FTP_MODE,
   ACTIVATION_STATUS,
   DATETIMESTAMP)
  SELECT FTPID,
         nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = ''CUSTOMER_ID''
                AND PRESENT_VALUE_NUM = CUSTOMERID),
             CUSTOMERID) CUSTOMERID,
         FTP_NAME,
         HOST_NAME,
         FILE_PROTOCOL,
         USER_NAME,
         PASSWORD,
         FTP_LOCATION,
         FTP_MODE,
         ACTIVATION_STATUS,
         DATETIMESTAMP
    FROM ' || V_SCHEMA_NAME || '.FTP_CONFIG';

  --commit;
end;

-- TASC

--ETL_PROJECT_CONFIG 

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE 'INSERT 
INTO ETL_PROJECT_CONFIG
  (SEQ_NO,
   COLUMN_NAME,
   START_POSITION,
   END_POSITION,
   LENGTH,
   CATEGORY,
   CATEGORY_TYPE,
   CATEGORY_VALUE,
   DATA_EXTRACT_BLOCK,
   REPLACE_FLAG,
   REPLACE_VALUE,
   IS_BIO_UPDATE,
   DESCRIPTION,
   ACTIVATION_STATUS,
   DATETIMESTAMP,
   PROJECTID)
  SELECT SEQ_NO,
         COLUMN_NAME,
         START_POSITION,
         END_POSITION,
         LENGTH,
         CATEGORY,
         CATEGORY_TYPE,
         CATEGORY_VALUE,
         DATA_EXTRACT_BLOCK,
         REPLACE_FLAG,
         REPLACE_VALUE,
         IS_BIO_UPDATE,
         DESCRIPTION,
         ACTIVATION_STATUS,
         DATETIMESTAMP,
         1
    FROM ' || V_SCHEMA_NAME || '.STG_DATA_LAYOUT_CONFIG';
END;

COMMIT;

--ETL_JOBMASTER_CONFIG 

declare

  V_SCHEMA_NAME VARCHAR2(100);

begin

  select PRESENT_VALUE_CHAR
    INTO V_SCHEMA_NAME
    from TASC_LOOKUP_DATA_SWAP
   where COLUMN_TYPE = 'SOURCE_SCHEMA_TASC';

  EXECUTE IMMEDIATE 'INSERT 
INTO ETL_JOBMASTER_CONFIG
  (MAPPING_NAME,
   SESSION_NAME,
   WORKFLOW_NAME,
   REPOSITORY_SERVICE,
   INTEGRATION_SERVICE,
   DOMAIN_NAME,
   CREATED_DATE_TIME,
   UPDATED_DATE_TIME,
   PROJECTID)
  SELECT MAPPING_NAME,
         SESSION_NAME,
         WORKFLOW_NAME,
         REPOSITORY_SERVICE,
         INTEGRATION_SERVICE,
         DOMAIN_NAME,
         CREATED_DATE_TIME,
         UPDATED_DATE_TIME,
         1
    FROM ' || V_SCHEMA_NAME || '.STG_ETL_JOBMASTER';
END;

COMMIT;



