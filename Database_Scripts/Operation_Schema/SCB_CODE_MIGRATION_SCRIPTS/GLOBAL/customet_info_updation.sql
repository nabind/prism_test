update customer_info set file_location = '/INORSREPORTS/ISTEP' where customerid in (1000,1001,1002,1003,1004,1005)    

update customer_info set file_location = '/TASCREPORTS/Datafiles/NY' where customerid =1006;
update customer_info set file_location = '/TASCREPORTS/Datafiles/NY' where customerid =1008;
update customer_info set file_location = '/TASCREPORTS/Datafiles/IN' where customerid =1009;
update customer_info set file_location = '/TASCREPORTS/Datafiles/IN' where customerid =1011;
update customer_info set file_location = '/TASCREPORTS/Datafiles/CA' where customerid =1015;
update customer_info set file_location = '/TASCREPORTS/Datafiles/NY' where customerid =1007;
update customer_info set file_location = '/TASCREPORTS/Datafiles/NY' where customerid =1010;
update customer_info set file_location = '/TASCREPORTS/Datafiles/WV' where customerid =1012;
update customer_info set file_location = '/TASCREPORTS/Datafiles/NV' where customerid =1013;
update customer_info set file_location = '/TASCREPORTS/Datafiles/NJ' where customerid =1014; 

commit ; 

