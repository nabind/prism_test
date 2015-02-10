/*Customer Creation Script  																*/
/*																							*/
/*	Purpose- This script will create the customer from a dummy customer	
/*	P_CUSTOMER_NAME  'xyz',
	P_CUSTOMER_CODE  'TT'--TWO DIGIT UNIQUE VALUE AS PER CUSTOMER_INFO
	P_SEND_LOGIN_PDF --'Y' OR 'N'
	P_PP_TPCODE      --10 DIGIT OGTP,
	P_OL_TPCODE      --10 DIGIT OGTP,
	P_DATA_LOC       'PATH', --LIKE SOME EXISTING IN CUSTOMER_INFO, EXAmPLE: /TASCREPORTS/Datafiles/TT --TT AS P_CUSTOMER_CODE
	P_SUPPRT_EMAILS  IN VARCHAR2 --OPTIONAL 
/*																							*/
/*																							*/
/*																							*/
/****************************************************************************************	*/

WHENEVER SQLERROR EXIT SQL.SQLCODE
 
conn &1
 
show user

 -- Grant the Privs that are needed for Running the procedure from TASC to GLOBAL Prism Schema -- 
 
declare 
cursor c1 is select * from prismglobal.tasc_grant ;  
lv_sqltext varchar2(1000):=NULL; 
begin 
 for i in c1 
 loop 
    lv_sqltext := 'GRANT '||i.grant_type||' ON '||i.table_name ||' TO '||i.grantee_schema; 
	
	execute immediate lv_sqltext ; 
	
	
 end loop ;
end ; 
/
 

conn &2
--prismglobal14perf
show user

  
alter package PKG_TASC_customer_creation compile ; 
 
-- Accept The Input from the User, this variables will be passed on to the main Customer Creation Proc -- 
 

VARIABLE L_PROJECT_ID NUMBER 
VARIABLE L_CUSTOMER_NAME VARCHAR2(100) 
VARIABLE L_CUSTOMER_CODE VARCHAR2(100) 
VARIABLE L_SEND_LOGIN_PDF VARCHAR2(100) 
VARIABLE L_PP_TPCODE VARCHAR2(100) 
VARIABLE L_OL_TPCODE VARCHAR2(100) 
VARIABLE L_DATA_LOC VARCHAR2(100) 
VARIABLE L_SUPPRT_EMAILS VARCHAR2(100) 
VARIABLE OUT_RESULTS VARCHAR2(1000) 

 set serveroutput on 

begin 

--- This Portion Needs to be changed Manually for New Customer -- 
 :L_PROJECT_ID :=1;
 :L_CUSTOMER_NAME 	:='XYZ';
 :L_CUSTOMER_CODE 	:='XX';
 :L_SEND_LOGIN_PDF 	:='Y';
 :L_PP_TPCODE 		:='TPCODE1' ;
 :L_OL_TPCODE 		:='TPCODE2' ;
 :L_DATA_LOC 		:='/Data' ;
 :L_SUPPRT_EMAILS 	:='debashis.deb@tcs.com';

-- End of Change -------------------------------------------------

 
 
 
 PKG_TASC_customer_creation.SP_TASC_CREATE_CUSTOMER_NEW(P_CUSTOMER_NAME  =>  :L_CUSTOMER_NAME ,
                                                    P_CUSTOMER_CODE  => :L_CUSTOMER_CODE ,
                                                    P_SEND_LOGIN_PDF => :L_SEND_LOGIN_PDF ,
                                                    P_PP_TPCODE      => :L_PP_TPCODE ,
                                                    P_OL_TPCODE      => :L_OL_TPCODE ,
                                                    P_DATA_LOC       => :L_DATA_LOC ,
                                                    P_SUPPRT_EMAILS  => :L_SUPPRT_EMAILS ,
													P_PROJECT_ID  	 => :L_PROJECT_ID ,
                                                    OUT_RESULT       =>:OUT_RESULTS);
 													
end ; 		
/ 

 
											
exec dbms_output.put_line (:OUT_RESULTS); 

-- After Creating the customer Connect Back to TASC and Revoke the privileges 
conn &1
show user

-- Revoke all the Grants Given from the TASC User to the PrismGlobal User -- 
declare 
cursor c1 is select * from prismglobal.tasc_grant ;  
lv_sqltext varchar2(1000):=NULL; 
begin 
 for i in c1 
 loop 
    lv_sqltext := 'revoke '||i.grant_type||' ON '||i.table_name|| ' FROM  prismglobal'; 
	dbms_output.put_line (lv_sqltext);
	execute immediate lv_sqltext ; 
	
	
 end loop ;
end ;  
/


 






 





