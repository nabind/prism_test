CREATE OR REPLACE PACKAGE PKG_DELETE_FACT_TABLES IS

  -- Purpose : Deletes data from FACT tables for the input PRODUCT CODE

  PROCEDURE PRC_DELETE_ASFD_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_STFD_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_DISA_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_SPPR_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_ACAD_STD_SUMM_FACT(IN_PRODUCT_CODE VARCHAR2,
                                          IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_UDTR_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_CLASS_SUMM_FACT(IN_PRODUCT_CODE VARCHAR2,
                                       IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_SUMT_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_MEDIA_FACT(IN_PRODUCT_CODE VARCHAR2,
                                  IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_GRT_DATA(IN_PRODUCT_CODE VARCHAR2,
                                IN_ORG_TP       VARCHAR2);

  PROCEDURE PRC_DELETE_BIO_EXTRACT(IN_PRODUCT_CODE VARCHAR2,
                                   IN_ORG_TP       VARCHAR2);

END PKG_DELETE_FACT_TABLES;
/
CREATE OR REPLACE PACKAGE BODY PKG_DELETE_FACT_TABLES IS

  PROCEDURE PRC_DELETE_ASFD_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM ASFD_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from ASFD_FACT is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM ASFD_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);
    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from ASFD_FACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_ASFD_FACT;

  PROCEDURE PRC_DELETE_STFD_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM STFD_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from STFD_FACT is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM STFD_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from STFD_FACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_STFD_FACT;

  PROCEDURE PRC_DELETE_DISA_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM DISA_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from DISA_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM DISA_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from DISA_FACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_DISA_FACT;

  PROCEDURE PRC_DELETE_SPPR_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM SPPR_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from SPPR_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM SPPR_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from SPPR_FACT table count is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_SPPR_FACT;

  -- ACAD_STD_SUMM_FACT
  PROCEDURE PRC_DELETE_ACAD_STD_SUMM_FACT(IN_PRODUCT_CODE VARCHAR2,
                                          IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM ACAD_STD_SUMM_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from ACAD_STD_SUMM_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM ACAD_STD_SUMM_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from ACAD_STD_SUMM_FACT table count is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_ACAD_STD_SUMM_FACT;

  PROCEDURE PRC_DELETE_UDTR_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM UDTR_ROSTER_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from UDTR_ROSTER_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM UDTR_ROSTER_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from UDTR_ROSTER_FACT is = ' ||
                         SQL%ROWCOUNT);

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM UDTR_SUMM_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from UDTR_SUMM_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM UDTR_SUMM_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from UDTR_SUMM_FACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_UDTR_FACT;

  PROCEDURE PRC_DELETE_CLASS_SUMM_FACT(IN_PRODUCT_CODE VARCHAR2,
                                       IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM CLASS_SUMM_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from CLASS_SUMM_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM CLASS_SUMM_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from CLASS_SUMM_FACT table is = ' ||
                         SQL%ROWCOUNT);
    COMMIT;
  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_CLASS_SUMM_FACT;

  PROCEDURE PRC_DELETE_SUMT_FACT(IN_PRODUCT_CODE VARCHAR2,
                                 IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM SUMT_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from SUMT_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM SUMT_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from SUMT_FACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_SUMT_FACT;

  PROCEDURE PRC_DELETE_MEDIA_FACT(IN_PRODUCT_CODE VARCHAR2,
                                  IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM MEDIA_FACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from MEDIA_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM MEDIA_FACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from MEDIA_FACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_MEDIA_FACT;

  PROCEDURE PRC_DELETE_GRT_DATA(IN_PRODUCT_CODE VARCHAR2,
                                IN_ORG_TP       VARCHAR2)

   IS

    LV_PRE_DEL_COUNT NUMBER;
    LV_CUST_PROD_ID  NUMBER(20);
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);
    V_TEST         NUMBER;
    V_CUSTOMER     NUMBER;

  BEGIN

    EXECUTE IMMEDIATE 'TRUNCATE TABLE GLOB_TEMP_STG_STUDENT_BIO_ID';

    -- Disable Constraints for STUDENT_BIO_DIM deletion

    EXECUTE IMMEDIATE 'alter table STUDENT_PDF_FILES
    disable constraint FK_STUDENT_PDF_FILES_3';

    EXECUTE IMMEDIATE 'alter table SUBTEST_SCORE_FACT
    disable constraint FK_SUBTEST_SCORE_FACT_10';

    EXECUTE IMMEDIATE 'alter table OBJECTIVE_SCORE_FACT
    disable constraint FK_OBJECTIVE_SCORE_FACT_1';

    EXECUTE IMMEDIATE 'alter table INVITATION_CODE
      disable constraint FK_IC_STUD_BIO_ID';

    DBMS_OUTPUT.PUT_LINE('Constraints disabled for STUDENT_BIO_DIM deletion');

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    SELECT C.CUSTOMERID
      INTO V_CUSTOMER
      FROM TEST_PROGRAM C
     WHERE C.TP_CODE = IN_ORG_TP;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT CPL.CUST_PROD_ID
      INTO LV_CUST_PROD_ID
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE IS_EDITABLE = 'Y'
       AND PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    --DELETE INVITATION_CODE_CLAIM

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM INVITATION_CODE_CLAIM
     WHERE ICID IN (SELECT I.ICID
                      FROM INVITATION_CODE I
                     WHERE I.CUST_PROD_ID = LV_CUST_PROD_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from INVITATION_CODE_CLAIM table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM INVITATION_CODE_CLAIM ICM
     WHERE ICM.ICID IN
           (SELECT I.ICID
              FROM INVITATION_CODE I
             WHERE I.CUST_PROD_ID = LV_CUST_PROD_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from INVITATION_CODE_CLAIM table count is = ' ||
                         SQL%ROWCOUNT);

    --DELETE INVITATION_CODE

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM INVITATION_CODE
     WHERE CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from INVITATION_CODE table is = ' ||
                         LV_PRE_DEL_COUNT);

    UPDATE INVITATION_CODE
       SET STUDENT_BIO_ID = NULL
     WHERE CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from INVITATION_CODE table count is = ' ||
                         SQL%ROWCOUNT);

    --RESULTS_GRT_FACT

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM RESULTS_GRT_FACT
     WHERE CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from RESULTS_GRT_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM RESULTS_GRT_FACT RSGRT
     WHERE RSGRT.CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from RESULTS_GRT_FACT table count is = ' ||
                         SQL%ROWCOUNT);

    --OBJECTIVE_SCORE_FACT

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM OBJECTIVE_SCORE_FACT
     WHERE CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from OBJECTIVE_SCORE_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM OBJECTIVE_SCORE_FACT OSF
     WHERE OSF.CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from OBJECTIVE_SCORE_FACT table count is = ' ||
                         SQL%ROWCOUNT);

    --SUBTEST_SCORE_FACT

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM SUBTEST_SCORE_FACT
     WHERE CUST_PROD_ID = LV_CUST_PROD_ID;

    INSERT INTO GLOB_TEMP_STG_STUDENT_BIO_ID
      (STUDENT_BIO_ID)
      SELECT DISTINCT STUDENT_BIO_ID
        FROM SUBTEST_SCORE_FACT
       WHERE CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from SUBTEST_SCORE_FACT table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM SUBTEST_SCORE_FACT SCF
     WHERE SCF.CUST_PROD_ID = LV_CUST_PROD_ID;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from SUBTEST_SCORE_FACT table count is = ' ||
                         SQL%ROWCOUNT);

    --STUDENT_DEMO_VALUES

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM STUDENT_DEMO_VALUES
     WHERE STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from STUDENT_DEMO_VALUES table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM STUDENT_DEMO_VALUES SDV
     WHERE SDV.STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from STUDENT_DEMO_VALUES table count is = ' ||
                         SQL%ROWCOUNT);

    --STU_SUBTEST_DEMO_VALUES

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM STU_SUBTEST_DEMO_VALUES
     WHERE STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from STU_SUBTEST_DEMO_VALUES table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM STU_SUBTEST_DEMO_VALUES SSDV
     WHERE SSDV.STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from STU_SUBTEST_DEMO_VALUES table count is = ' ||
                         SQL%ROWCOUNT);

    --student_pdf_files

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM STUDENT_PDF_FILES
     WHERE STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from stg_student_pdf_files table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM STUDENT_PDF_FILES SSPF
     WHERE SSPF.STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from stg_student_pdf_files table count is = ' ||
                         SQL%ROWCOUNT);
    SELECT COUNT(1) INTO V_TEST FROM GLOB_TEMP_STG_STUDENT_BIO_ID;
    DBMS_OUTPUT.PUT_LINE('V_TEST WAS   ' || V_TEST);

    --STUDENT_BIO_DIM

    SELECT COUNT(1) INTO V_TEST FROM GLOB_TEMP_STG_STUDENT_BIO_ID;
    DBMS_OUTPUT.PUT_LINE('V_TEST IS   ' || V_TEST);

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM STUDENT_BIO_DIM
     WHERE STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from STUDENT_BIO_DIM table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM STUDENT_BIO_DIM SBD
     WHERE SBD.STUDENT_BIO_ID IN
           (SELECT STUDENT_BIO_ID FROM GLOB_TEMP_STG_STUDENT_BIO_ID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from STUDENT_BIO_DIM table count is = ' ||
                         SQL%ROWCOUNT);

    --ORG_PRODUCT_LINK

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM ORG_PRODUCT_LINK O
     WHERE O.CUST_PROD_ID = LV_CUST_PROD_ID
       AND O.ORG_NODEID NOT IN
           (SELECT ORG_NODEID
              FROM ORG_NODE_DIM
             WHERE ORG_NODE_LEVEL = 1
               AND customerid = V_CUSTOMER);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from ORG_PRODUCT_LINK table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM ORG_PRODUCT_LINK OPL
     WHERE OPL.CUST_PROD_ID = LV_CUST_PROD_ID
       AND OPL.ORG_NODEID NOT IN
           (SELECT ORG_NODEID
              FROM ORG_NODE_DIM
             WHERE ORG_NODE_LEVEL = 1
               AND customerid = V_CUSTOMER);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from ORG_PRODUCT_LINK table count is = ' ||
                         SQL%ROWCOUNT);

    --ORG_TEST_PROGRAM_LINK

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM ORG_TEST_PROGRAM_LINK
     WHERE TP_ID IN
           (SELECT TP_ID FROM TEST_PROGRAM TP1 WHERE TP_CODE = IN_ORG_TP)
       AND ORG_NODEID NOT IN
           (SELECT ORG_NODEID
              FROM ORG_NODE_DIM
             WHERE ORG_NODE_LEVEL = 1
               AND customerid = V_CUSTOMER);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from ORG_TEST_PROGRAM_LINK table is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM ORG_TEST_PROGRAM_LINK OTPL
     WHERE OTPL.TP_ID IN
           (SELECT TP_ID FROM TEST_PROGRAM TP1 WHERE TP_CODE = IN_ORG_TP)
       AND OTPL.ORG_NODEID NOT IN
           (SELECT ORG_NODEID
              FROM ORG_NODE_DIM
             WHERE ORG_NODE_LEVEL = 1
               AND customerid = V_CUSTOMER);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from ORG_TEST_PROGRAM_LINK table count is = ' ||
                         SQL%ROWCOUNT);

    COMMIT;

    -- Enable constraints

    EXECUTE IMMEDIATE 'alter table STUDENT_PDF_FILES
    enable constraint FK_STUDENT_PDF_FILES_3';

    EXECUTE IMMEDIATE 'alter table SUBTEST_SCORE_FACT
    enable constraint FK_SUBTEST_SCORE_FACT_10';

    EXECUTE IMMEDIATE 'alter table OBJECTIVE_SCORE_FACT
    enable constraint FK_OBJECTIVE_SCORE_FACT_1';

    EXECUTE IMMEDIATE 'alter table INVITATION_CODE
    enable constraint FK_IC_STUD_BIO_ID';

    DBMS_OUTPUT.PUT_LINE('Constraints enabled which were disabled for STUDENT_BIO_DIM deletion');

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      ROLLBACK;

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

    WHEN OTHERS THEN
      ROLLBACK;

      EXECUTE IMMEDIATE 'alter table STUDENT_PDF_FILES
            enable constraint FK_STUDENT_PDF_FILES_3';

      EXECUTE IMMEDIATE 'alter table SUBTEST_SCORE_FACT
            enable constraint FK_SUBTEST_SCORE_FACT_10';

      EXECUTE IMMEDIATE 'alter table OBJECTIVE_SCORE_FACT
            enable constraint FK_OBJECTIVE_SCORE_FACT_1';

      EXECUTE IMMEDIATE 'alter table INVITATION_CODE
          enable constraint FK_IC_STUD_BIO_ID';

      DBMS_OUTPUT.PUT_LINE('Exception occurred: Constraints enabled which were disabled for STUDENT_BIO_DIM deletion');

  END PRC_DELETE_GRT_DATA;

  ---------------------------------------------------

  PROCEDURE PRC_DELETE_BIO_EXTRACT(IN_PRODUCT_CODE VARCHAR2,
                                   IN_ORG_TP       VARCHAR2) AS

    LV_PRE_DEL_COUNT NUMBER;
    CUST_PROD_ID_NOT_EDITABLE EXCEPTION;
    LV_IS_EDITABLE VARCHAR2(10);

  BEGIN

    SELECT IS_EDITABLE
      INTO LV_IS_EDITABLE
      FROM PRODUCT P, CUST_PRODUCT_LINK CPL, ADMIN_DIM ADM, TEST_PROGRAM TP
     WHERE PRODUCT_CODE = IN_PRODUCT_CODE
       AND P.PRODUCTID = CPL.PRODUCTID
       AND ADM.IS_CURRENT_ADMIN = 'Y'
       AND CPL.ADMINID = ADM.ADMINID
       AND TP.TP_CODE = IN_ORG_TP
       AND TP.CUSTOMERID = CPL.CUSTOMERID
       AND TP.ADMINID = CPL.ADMINID;

    IF LV_IS_EDITABLE IS NULL OR LV_IS_EDITABLE = 'N' THEN

      RAISE CUST_PROD_ID_NOT_EDITABLE;

    END IF;

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM INVITATION_CODE
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from INVITATION_CODE is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM INVITATION_CODE A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from INVITATION_CODE table is = ' ||
                         SQL%ROWCOUNT);

    SELECT COUNT(1)
      INTO LV_PRE_DEL_COUNT
      FROM BIO_STUDENT_EXTRACT
     WHERE CUST_PROD_ID = (SELECT CPL.CUST_PROD_ID
                             FROM PRODUCT           P,
                                  CUST_PRODUCT_LINK CPL,
                                  ADMIN_DIM         ADM,
                                  TEST_PROGRAM      TP
                            WHERE IS_EDITABLE = 'Y'
                              AND PRODUCT_CODE = IN_PRODUCT_CODE
                              AND P.PRODUCTID = CPL.PRODUCTID
                              AND ADM.IS_CURRENT_ADMIN = 'Y'
                              AND CPL.ADMINID = ADM.ADMINID
                              AND TP.TP_CODE = IN_ORG_TP
                              AND TP.CUSTOMERID = CPL.CUSTOMERID
                              AND TP.ADMINID = CPL.ADMINID);

    DBMS_OUTPUT.PUT_LINE('Deletion started, Records to be deleted from BIO_STUDENT_EXTRACT is = ' ||
                         LV_PRE_DEL_COUNT);

    DELETE FROM BIO_STUDENT_EXTRACT A
     WHERE A.CUST_PROD_ID =
           (SELECT CPL.CUST_PROD_ID
              FROM PRODUCT           P,
                   CUST_PRODUCT_LINK CPL,
                   ADMIN_DIM         ADM,
                   TEST_PROGRAM      TP
             WHERE IS_EDITABLE = 'Y'
               AND PRODUCT_CODE = IN_PRODUCT_CODE
               AND P.PRODUCTID = CPL.PRODUCTID
               AND ADM.IS_CURRENT_ADMIN = 'Y'
               AND CPL.ADMINID = ADM.ADMINID
               AND TP.TP_CODE = IN_ORG_TP
               AND TP.CUSTOMERID = CPL.CUSTOMERID
               AND TP.ADMINID = CPL.ADMINID);
    --COMMIT;

    DBMS_OUTPUT.PUT_LINE('Deletion completed, Records deleted from BIO_STUDENT_EXTRACT table is = ' ||
                         SQL%ROWCOUNT);

  EXCEPTION
    WHEN CUST_PROD_ID_NOT_EDITABLE THEN

      DBMS_OUTPUT.PUT_LINE('Deletion failed, Product is not editable');

  END PRC_DELETE_BIO_EXTRACT;

---------------------------------------------------------------

END PKG_DELETE_FACT_TABLES;
/
