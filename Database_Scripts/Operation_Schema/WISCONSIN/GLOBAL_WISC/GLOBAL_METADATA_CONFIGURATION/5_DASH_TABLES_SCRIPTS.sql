/*SELECT  * FROM DASH_MENUS WHERE MENU_NAME = 'Manage' AND PROJECTID = 5;
SELECT DB_REPORTID FROM dash_reports WHERE projectid = 5 AND REPORT_NAME = 'Manage Reports';
SELECT DB_ACTIONID FROM DASH_RPT_ACTION WHERE ACTION_NAME IN ('Add Report','Edit Report','Delete Report','Configure Report Message','Edit Actions') AND PROJECTID = 5;
SELECT CUST.CUST_PROD_ID
FROM CUSTOMER_INFO CI,
     PRODUCT PDT,
     ADMIN_DIM ADM,
     PROJECT_DIM PJT,
     CUST_PRODUCT_LINK CUST
WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
  AND PJT.PROJECTID = PDT.PROJECTID
  AND CI.PROJECTID = PDT.PROJECTID 
  AND ADM.PROJECTID = PDT.PROJECTID
  AND CUST.ADMINID = ADM.ADMINID
  AND CUST.CUSTOMERID = CI.CUSTOMERID
  AND CUST.PRODUCTID = PDT.PRODUCTID;
 SELECT * FROM dash_action_access WHERE projectid = 5 AND db_reportid = 10341; -- no 

*/

---DASH_MENUS
INSERT INTO DASH_MENUS VALUES(105,'Manage','CUSTOM',5,'Manage',5,SYSDATE,SYSDATE);
COMMIT;


DECLARE
V_PROJECTID NUMBER:=5;
BEGIN
  FOR R_MENUID IN (SELECT DB_MENUID FROM DASH_MENUS WHERE MENU_NAME IN ('Manage') AND PROJECTID = V_PROJECTID)
    LOOP
      FOR R_REPORT IN (SELECT DB_REPORTID FROM DASH_REPORTS WHERE PROJECTID = V_PROJECTID AND REPORT_NAME = 'Manage Reports')
        LOOP
          FOR R_ACTION IN (SELECT DB_ACTIONID FROM DASH_RPT_ACTION WHERE ACTION_NAME IN ('Add Report','Edit Report','Delete Report','Configure Report Message','Edit Actions') AND PROJECTID = V_PROJECTID)
            LOOP
              FOR R_ROLE IN (SELECT ROLEID FROM ROLE WHERE ROLE_NAME IN ('ROLE_CTB','ROLE_SUPER'))
               LOOP
                 FOR R_CUST_PROD IN (SELECT CUST.CUST_PROD_ID
                                      FROM CUSTOMER_INFO CI,
                                           PRODUCT PDT,
                                           ADMIN_DIM ADM,
                                           PROJECT_DIM PJT,
                                           CUST_PRODUCT_LINK CUST
                                      WHERE PJT.PROJECT_NAME='Wisconsin Forward Exam'
                                        AND PJT.PROJECTID = PDT.PROJECTID
                                        AND CI.PROJECTID = PDT.PROJECTID 
                                        AND ADM.PROJECTID = PDT.PROJECTID
                                        AND CUST.ADMINID = ADM.ADMINID
                                        AND CUST.CUSTOMERID = CI.CUSTOMERID
                                        AND CUST.PRODUCTID = PDT.PRODUCTID)
                LOOP
                  INSERT INTO DASH_ACTION_ACCESS VALUES(SEQ_DASH_ACTION_ACCESS.NEXTVAL,
                                                        R_MENUID.DB_MENUID,
                                                        R_REPORT.DB_REPORTID,
                                                        R_ACTION.DB_ACTIONID,
                                                        R_ROLE.ROLEID,
                                                        1,
                                                        R_CUST_PROD.CUST_PROD_ID,
                                                        R_ACTION.DB_ACTIONID,
                                                        V_PROJECTID,
                                                        'AC',
                                                        SYSDATE,
                                                        NULL);
                END LOOP;
               END LOOP;
            END LOOP;
        END LOOP;
    END LOOP;
 COMMIT;   
END;






